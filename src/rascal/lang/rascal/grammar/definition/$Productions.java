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
public class $Productions 
    extends
        org.rascalmpl.runtime.$RascalModule
    implements 
    	rascal.$ParseTree_$I,
    	rascal.lang.rascal.grammar.definition.$Productions_$I,
    	rascal.$Type_$I,
    	rascal.$List_$I,
    	rascal.$Grammar_$I,
    	rascal.$Message_$I {

    private final $Productions_$I $me;
    private final IList $constants;
    
    
    public final rascal.lang.rascal.syntax.$Rascal M_lang_rascal_syntax_Rascal;
    public final rascal.$ParseTree M_ParseTree;
    public final rascal.$Type M_Type;
    public final rascal.$List M_List;
    public final rascal.lang.rascal.grammar.definition.$Names M_lang_rascal_grammar_definition_Names;
    public final rascal.$IO M_IO;
    public final rascal.util.$Maybe M_util_Maybe;
    public final rascal.$Grammar M_Grammar;
    public final rascal.$Message M_Message;
    public final rascal.lang.rascal.grammar.definition.$Attributes M_lang_rascal_grammar_definition_Attributes;
    public final rascal.lang.rascal.grammar.definition.$Symbols M_lang_rascal_grammar_definition_Symbols;

    
    
    public final io.usethesource.vallang.type.Type $T7;	/*avalue()*/
    public final io.usethesource.vallang.type.Type $T17;	/*aparameter("A",avalue(),closed=true)*/
    public final io.usethesource.vallang.type.Type $T14;	/*astr()*/
    public final io.usethesource.vallang.type.Type $T12;	/*aparameter("A",avalue(),closed=false)*/
    public final io.usethesource.vallang.type.Type $T22;	/*aparameter("A",avalue())*/
    public final io.usethesource.vallang.type.Type $T10;	/*aparameter("U",avalue(),closed=false)*/
    public final io.usethesource.vallang.type.Type $T8;	/*aparameter("T",avalue(),closed=false)*/
    public final io.usethesource.vallang.type.Type $T2;	/*alit(",")*/
    public final io.usethesource.vallang.type.Type ADT_Attr;	/*aadt("Attr",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Replacement;	/*aadt("Replacement",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Replacement;	/*aadt("Replacement",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Kind;	/*aadt("Kind",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Kind;	/*aadt("Kind",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_ProtocolChars;	/*aadt("ProtocolChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_ProtocolChars;	/*aadt("ProtocolChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Visibility;	/*aadt("Visibility",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Visibility;	/*aadt("Visibility",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Expression;	/*aadt("Expression",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Expression;	/*aadt("Expression",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Mapping_Expression;	/*aadt("Mapping",[aadt("Expression",[],contextFreeSyntax())],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Mapping_Expression;	/*aadt("Mapping",[aadt("Expression",[],contextFreeSyntax())],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_LocalVariableDeclaration;	/*aadt("LocalVariableDeclaration",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_LocalVariableDeclaration;	/*aadt("LocalVariableDeclaration",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Strategy;	/*aadt("Strategy",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Strategy;	/*aadt("Strategy",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Tree;	/*aadt("Tree",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type $T15;	/*aparameter("T",aadt("Tree",[],dataSyntax()),closed=true)*/
    public final io.usethesource.vallang.type.Type ADT_KeywordArguments_1;	/*aadt("KeywordArguments",[aparameter("T",aadt("Tree",[],dataSyntax()),closed=true)],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_KeywordArguments_1;	/*aadt("KeywordArguments",[aparameter("T",aadt("Tree",[],dataSyntax()),closed=true)],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_MidStringChars;	/*aadt("MidStringChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_MidStringChars;	/*aadt("MidStringChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_LAYOUT;	/*aadt("LAYOUT",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_LAYOUT;	/*aadt("LAYOUT",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Pattern;	/*aadt("Pattern",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Pattern;	/*aadt("Pattern",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Mapping_Pattern;	/*aadt("Mapping",[aadt("Pattern",[],contextFreeSyntax())],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Mapping_Pattern;	/*aadt("Mapping",[aadt("Pattern",[],contextFreeSyntax())],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_LocationChangeType;	/*aadt("LocationChangeType",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_IOCapability;	/*aadt("IOCapability",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_TagString;	/*aadt("TagString",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_TagString;	/*aadt("TagString",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_FunctionType;	/*aadt("FunctionType",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_FunctionType;	/*aadt("FunctionType",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Label;	/*aadt("Label",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Label;	/*aadt("Label",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_PostProtocolChars;	/*aadt("PostProtocolChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_PostProtocolChars;	/*aadt("PostProtocolChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Range;	/*aadt("Range",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Range;	/*aadt("Range",[],contextFreeSyntax())*/
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
    public final io.usethesource.vallang.type.Type ADT_Prod;	/*aadt("Prod",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Prod;	/*aadt("Prod",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type $T16;	/*aparameter("T",aadt("Tree",[],dataSyntax()),closed=false)*/
    public final io.usethesource.vallang.type.Type ADT_ModuleParameters;	/*aadt("ModuleParameters",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_ModuleParameters;	/*aadt("ModuleParameters",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Output;	/*aadt("Output",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Output;	/*aadt("Output",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_SyntaxDefinition;	/*aadt("SyntaxDefinition",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_SyntaxDefinition;	/*aadt("SyntaxDefinition",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_RegExpModifier;	/*aadt("RegExpModifier",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_RegExpModifier;	/*aadt("RegExpModifier",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_ImportedModule;	/*aadt("ImportedModule",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_ImportedModule;	/*aadt("ImportedModule",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_BooleanLiteral;	/*aadt("BooleanLiteral",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_BooleanLiteral;	/*aadt("BooleanLiteral",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Case;	/*aadt("Case",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Case;	/*aadt("Case",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_DateTimeLiteral;	/*aadt("DateTimeLiteral",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_DateTimeLiteral;	/*aadt("DateTimeLiteral",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_LocationChangeEvent;	/*aadt("LocationChangeEvent",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_DatePart;	/*aadt("DatePart",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_DatePart;	/*aadt("DatePart",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Target;	/*aadt("Target",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Target;	/*aadt("Target",[],contextFreeSyntax())*/
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
    public final io.usethesource.vallang.type.Type ADT_Maybe_1;	/*aadt("Maybe",[aparameter("A",avalue(),closed=true)],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Catch;	/*aadt("Catch",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Catch;	/*aadt("Catch",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Production;	/*aadt("Production",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_PostStringChars;	/*aadt("PostStringChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_PostStringChars;	/*aadt("PostStringChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Renamings;	/*aadt("Renamings",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Renamings;	/*aadt("Renamings",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_KeywordFormal;	/*aadt("KeywordFormal",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_KeywordFormal;	/*aadt("KeywordFormal",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_OptionalExpression;	/*aadt("OptionalExpression",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_OptionalExpression;	/*aadt("OptionalExpression",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_GrammarDefinition;	/*aadt("GrammarDefinition",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Field;	/*aadt("Field",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Field;	/*aadt("Field",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Maybe_Attr;	/*aadt("Maybe",[aadt("Attr",[],dataSyntax())],dataSyntax())*/
    public final io.usethesource.vallang.type.Type Maybe_Attr_just_Attr;	/*acons(aadt("Maybe",[aadt("Attr",[],dataSyntax())],dataSyntax()),[aadt("Attr",[],dataSyntax(),alabel="val")],[],alabel="just")*/
    public final io.usethesource.vallang.type.Type ADT_LocationLiteral;	/*aadt("LocationLiteral",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_LocationLiteral;	/*aadt("LocationLiteral",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Tag;	/*aadt("Tag",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Tag;	/*aadt("Tag",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Type;	/*aadt("Type",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Type;	/*aadt("Type",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Symbol;	/*aadt("Symbol",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_HexIntegerLiteral;	/*aadt("HexIntegerLiteral",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_HexIntegerLiteral;	/*aadt("HexIntegerLiteral",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_DataTarget;	/*aadt("DataTarget",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_DataTarget;	/*aadt("DataTarget",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_TimeZonePart;	/*aadt("TimeZonePart",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_TimeZonePart;	/*aadt("TimeZonePart",[],lexicalSyntax())*/
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
    public final io.usethesource.vallang.type.Type ADT_StringLiteral;	/*aadt("StringLiteral",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_StringLiteral;	/*aadt("StringLiteral",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type $T13;	/*alist(aparameter("T",avalue(),closed=false))*/
    public final io.usethesource.vallang.type.Type ADT_KeywordArguments_Pattern;	/*aadt("KeywordArguments",[aadt("Pattern",[],contextFreeSyntax())],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_KeywordArguments_Pattern;	/*aadt("KeywordArguments",[aadt("Pattern",[],contextFreeSyntax())],contextFreeSyntax())*/
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
    public final io.usethesource.vallang.type.Type ADT_Associativity;	/*aadt("Associativity",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Sym;	/*aadt("Sym",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Sym;	/*aadt("Sym",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_LAYOUTLIST;	/*aadt("LAYOUTLIST",[],layoutSyntax())*/
    public final io.usethesource.vallang.type.Type $T18;	/*\iter-seps(aadt("Sym",[],contextFreeSyntax()),[aadt("LAYOUTLIST",[],layoutSyntax()),alit(","),aadt("LAYOUTLIST",[],layoutSyntax())])*/
    public final io.usethesource.vallang.type.Type ADT_RegExp;	/*aadt("RegExp",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_RegExp;	/*aadt("RegExp",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Tags;	/*aadt("Tags",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Tags;	/*aadt("Tags",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_URLChars;	/*aadt("URLChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_URLChars;	/*aadt("URLChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_ModuleActuals;	/*aadt("ModuleActuals",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_ModuleActuals;	/*aadt("ModuleActuals",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_DataTypeSelector;	/*aadt("DataTypeSelector",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_DataTypeSelector;	/*aadt("DataTypeSelector",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type $T4;	/*aset(aadt("SyntaxDefinition",[],contextFreeSyntax()))*/
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
    public final io.usethesource.vallang.type.Type $T21;	/*alist(aadt("Tree",[],dataSyntax()))*/
    public final io.usethesource.vallang.type.Type ADT_CaseInsensitiveStringConstant;	/*aadt("CaseInsensitiveStringConstant",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_CaseInsensitiveStringConstant;	/*aadt("CaseInsensitiveStringConstant",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Char;	/*aadt("Char",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Char;	/*aadt("Char",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_JustDate;	/*aadt("JustDate",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_JustDate;	/*aadt("JustDate",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_ProdModifier;	/*aadt("ProdModifier",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_ProdModifier;	/*aadt("ProdModifier",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type $T19;	/*\iter-star-seps(aadt("ProdModifier",[],contextFreeSyntax()),[aadt("LAYOUTLIST",[],layoutSyntax())])*/
    public final io.usethesource.vallang.type.Type $T9;	/*areified(aparameter("U",avalue(),closed=false))*/
    public final io.usethesource.vallang.type.Type ADT_Header;	/*aadt("Header",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Header;	/*aadt("Header",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_LocationType;	/*aadt("LocationType",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Assignment;	/*aadt("Assignment",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Assignment;	/*aadt("Assignment",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_OptionalComma;	/*aadt("OptionalComma",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_OptionalComma;	/*aadt("OptionalComma",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_StringConstant;	/*aadt("StringConstant",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_StringConstant;	/*aadt("StringConstant",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type $T11;	/*aset(aadt("Production",[],dataSyntax()))*/
    public final io.usethesource.vallang.type.Type ADT_FunctionDeclaration;	/*aadt("FunctionDeclaration",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_FunctionDeclaration;	/*aadt("FunctionDeclaration",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_TypeVar;	/*aadt("TypeVar",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_TypeVar;	/*aadt("TypeVar",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Variant;	/*aadt("Variant",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Variant;	/*aadt("Variant",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_UserType;	/*aadt("UserType",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_UserType;	/*aadt("UserType",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Import;	/*aadt("Import",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Import;	/*aadt("Import",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_FunctionModifiers;	/*aadt("FunctionModifiers",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_FunctionModifiers;	/*aadt("FunctionModifiers",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Formals;	/*aadt("Formals",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Formals;	/*aadt("Formals",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_ConcreteHole;	/*aadt("ConcreteHole",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_ConcreteHole;	/*aadt("ConcreteHole",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_TreeSearchResult_1;	/*aadt("TreeSearchResult",[aparameter("T",aadt("Tree",[],dataSyntax()),closed=true)],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Message;	/*aadt("Message",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_CharRange;	/*aadt("CharRange",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Grammar;	/*aadt("Grammar",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type $T5;	/*alist(aadt("Symbol",[],dataSyntax()))*/
    public final io.usethesource.vallang.type.Type ADT_StringMiddle;	/*aadt("StringMiddle",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_StringMiddle;	/*aadt("StringMiddle",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type $T1;	/*\iter-seps(aadt("Expression",[],contextFreeSyntax()),[aadt("LAYOUTLIST",[],layoutSyntax()),alit(","),aadt("LAYOUTLIST",[],layoutSyntax())])*/
    public final io.usethesource.vallang.type.Type ADT_RealLiteral;	/*aadt("RealLiteral",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_RealLiteral;	/*aadt("RealLiteral",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Comprehension;	/*aadt("Comprehension",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Comprehension;	/*aadt("Comprehension",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type $T20;	/*\iter-star-seps(aadt("Sym",[],contextFreeSyntax()),[aadt("LAYOUTLIST",[],layoutSyntax())])*/
    public final io.usethesource.vallang.type.Type ADT_QualifiedName;	/*aadt("QualifiedName",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_QualifiedName;	/*aadt("QualifiedName",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_MidPathChars;	/*aadt("MidPathChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_MidPathChars;	/*aadt("MidPathChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_StringTemplate;	/*aadt("StringTemplate",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_StringTemplate;	/*aadt("StringTemplate",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_OctalIntegerLiteral;	/*aadt("OctalIntegerLiteral",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_OctalIntegerLiteral;	/*aadt("OctalIntegerLiteral",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_ProtocolPart;	/*aadt("ProtocolPart",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_ProtocolPart;	/*aadt("ProtocolPart",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_NamedBackslash;	/*aadt("NamedBackslash",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_NamedBackslash;	/*aadt("NamedBackslash",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_RascalKeywords;	/*aadt("RascalKeywords",[],keywordSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Commands;	/*aadt("Commands",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Commands;	/*aadt("Commands",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type $T6;	/*areified(aparameter("T",avalue(),closed=false))*/
    public final io.usethesource.vallang.type.Type ADT_Comment;	/*aadt("Comment",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Comment;	/*aadt("Comment",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_StructuredType;	/*aadt("StructuredType",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_StructuredType;	/*aadt("StructuredType",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Visit;	/*aadt("Visit",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Visit;	/*aadt("Visit",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_NamedRegExp;	/*aadt("NamedRegExp",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_NamedRegExp;	/*aadt("NamedRegExp",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_PreProtocolChars;	/*aadt("PreProtocolChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_PreProtocolChars;	/*aadt("PreProtocolChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_RuntimeException;	/*aadt("RuntimeException",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_DecimalIntegerLiteral;	/*aadt("DecimalIntegerLiteral",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_DecimalIntegerLiteral;	/*aadt("DecimalIntegerLiteral",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Assoc;	/*aadt("Assoc",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Assoc;	/*aadt("Assoc",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Maybe_Symbol;	/*aadt("Maybe",[aadt("Symbol",[],dataSyntax())],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_MidProtocolChars;	/*aadt("MidProtocolChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_MidProtocolChars;	/*aadt("MidProtocolChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Command;	/*aadt("Command",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Command;	/*aadt("Command",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Parameters;	/*aadt("Parameters",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Parameters;	/*aadt("Parameters",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type Maybe_Associativity_just_Associativity;	/*acons(aadt("Maybe",[aadt("Associativity",[],dataSyntax())],dataSyntax()),[aadt("Associativity",[],dataSyntax(),alabel="val")],[],alabel="just")*/
    public final io.usethesource.vallang.type.Type Maybe_Symbol_just_Symbol;	/*acons(aadt("Maybe",[aadt("Symbol",[],dataSyntax())],dataSyntax()),[aadt("Symbol",[],dataSyntax(),alabel="val")],[],alabel="just")*/
    public final io.usethesource.vallang.type.Type ADT_PathTail;	/*aadt("PathTail",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_PathTail;	/*aadt("PathTail",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Literal;	/*aadt("Literal",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Literal;	/*aadt("Literal",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Statement;	/*aadt("Statement",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Statement;	/*aadt("Statement",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_ConcretePart;	/*aadt("ConcretePart",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_ConcretePart;	/*aadt("ConcretePart",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_PathChars;	/*aadt("PathChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_PathChars;	/*aadt("PathChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Toplevel;	/*aadt("Toplevel",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Toplevel;	/*aadt("Toplevel",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type $T0;	/*aset(aadt("Condition",[],dataSyntax()))*/
    public final io.usethesource.vallang.type.Type ADT_Exception;	/*aadt("Exception",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_DateAndTime;	/*aadt("DateAndTime",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_DateAndTime;	/*aadt("DateAndTime",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type $T3;	/*aset(aadt("Symbol",[],dataSyntax()))*/
    public final io.usethesource.vallang.type.Type ADT_Module;	/*aadt("Module",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Module;	/*aadt("Module",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_PatternWithAction;	/*aadt("PatternWithAction",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_PatternWithAction;	/*aadt("PatternWithAction",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_UnicodeEscape;	/*aadt("UnicodeEscape",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_UnicodeEscape;	/*aadt("UnicodeEscape",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Assignable;	/*aadt("Assignable",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Assignable;	/*aadt("Assignable",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_BasicType;	/*aadt("BasicType",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_BasicType;	/*aadt("BasicType",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_CommonKeywordParameters;	/*aadt("CommonKeywordParameters",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_CommonKeywordParameters;	/*aadt("CommonKeywordParameters",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_ProtocolTail;	/*aadt("ProtocolTail",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_ProtocolTail;	/*aadt("ProtocolTail",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_PostPathChars;	/*aadt("PostPathChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_PostPathChars;	/*aadt("PostPathChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_StringCharacter;	/*aadt("StringCharacter",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_StringCharacter;	/*aadt("StringCharacter",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_EvalCommand;	/*aadt("EvalCommand",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_EvalCommand;	/*aadt("EvalCommand",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_JustTime;	/*aadt("JustTime",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_JustTime;	/*aadt("JustTime",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_FunctionModifier;	/*aadt("FunctionModifier",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_FunctionModifier;	/*aadt("FunctionModifier",[],contextFreeSyntax())*/

    public $Productions(RascalExecutionContext rex){
        this(rex, null);
    }
    
    public $Productions(RascalExecutionContext rex, Object extended){
       super(rex);
       this.$me = extended == null ? this : ($Productions_$I)extended;
       ModuleStore mstore = rex.getModuleStore();
       mstore.put(rascal.lang.rascal.grammar.definition.$Productions.class, this);
       
       mstore.importModule(rascal.lang.rascal.syntax.$Rascal.class, rex, rascal.lang.rascal.syntax.$Rascal::new);
       mstore.importModule(rascal.lang.rascal.grammar.definition.$Names.class, rex, rascal.lang.rascal.grammar.definition.$Names::new);
       mstore.importModule(rascal.$IO.class, rex, rascal.$IO::new);
       mstore.importModule(rascal.util.$Maybe.class, rex, rascal.util.$Maybe::new);
       mstore.importModule(rascal.lang.rascal.grammar.definition.$Attributes.class, rex, rascal.lang.rascal.grammar.definition.$Attributes::new);
       mstore.importModule(rascal.lang.rascal.grammar.definition.$Symbols.class, rex, rascal.lang.rascal.grammar.definition.$Symbols::new); 
       
       M_lang_rascal_syntax_Rascal = mstore.getModule(rascal.lang.rascal.syntax.$Rascal.class);
       M_lang_rascal_grammar_definition_Names = mstore.getModule(rascal.lang.rascal.grammar.definition.$Names.class);
       M_IO = mstore.getModule(rascal.$IO.class);
       M_util_Maybe = mstore.getModule(rascal.util.$Maybe.class);
       M_lang_rascal_grammar_definition_Attributes = mstore.getModule(rascal.lang.rascal.grammar.definition.$Attributes.class);
       M_lang_rascal_grammar_definition_Symbols = mstore.getModule(rascal.lang.rascal.grammar.definition.$Symbols.class); 
       
       M_ParseTree = mstore.extendModule(rascal.$ParseTree.class, rex, rascal.$ParseTree::new, $me);
       M_Type = mstore.extendModule(rascal.$Type.class, rex, rascal.$Type::new, $me);
       M_List = mstore.extendModule(rascal.$List.class, rex, rascal.$List::new, $me);
       M_Grammar = mstore.extendModule(rascal.$Grammar.class, rex, rascal.$Grammar::new, $me);
       M_Message = mstore.extendModule(rascal.$Message.class, rex, rascal.$Message::new, $me);
                          
       
       $TS.importStore(M_lang_rascal_syntax_Rascal.$TS);
       $TS.importStore(M_ParseTree.$TS);
       $TS.importStore(M_Type.$TS);
       $TS.importStore(M_List.$TS);
       $TS.importStore(M_lang_rascal_grammar_definition_Names.$TS);
       $TS.importStore(M_IO.$TS);
       $TS.importStore(M_util_Maybe.$TS);
       $TS.importStore(M_Grammar.$TS);
       $TS.importStore(M_Message.$TS);
       $TS.importStore(M_lang_rascal_grammar_definition_Attributes.$TS);
       $TS.importStore(M_lang_rascal_grammar_definition_Symbols.$TS);
       
       $constants = readBinaryConstantsFile(this.getClass(), "rascal/lang/rascal/grammar/definition/$Productions.constants", 11, "4b6a557f9d384a8d2690c4149b4fa16e");
       ADT_Attr = $adt("Attr");
       NT_Replacement = $sort("Replacement");
       ADT_Replacement = $adt("Replacement");
       NT_Kind = $sort("Kind");
       ADT_Kind = $adt("Kind");
       NT_ProtocolChars = $lex("ProtocolChars");
       ADT_ProtocolChars = $adt("ProtocolChars");
       NT_Visibility = $sort("Visibility");
       ADT_Visibility = $adt("Visibility");
       NT_Expression = $sort("Expression");
       ADT_Expression = $adt("Expression");
       NT_LocalVariableDeclaration = $sort("LocalVariableDeclaration");
       ADT_LocalVariableDeclaration = $adt("LocalVariableDeclaration");
       NT_Strategy = $sort("Strategy");
       ADT_Strategy = $adt("Strategy");
       ADT_Tree = $adt("Tree");
       NT_MidStringChars = $lex("MidStringChars");
       ADT_MidStringChars = $adt("MidStringChars");
       NT_LAYOUT = $lex("LAYOUT");
       ADT_LAYOUT = $adt("LAYOUT");
       NT_Pattern = $sort("Pattern");
       ADT_Pattern = $adt("Pattern");
       ADT_LocationChangeType = $adt("LocationChangeType");
       ADT_IOCapability = $adt("IOCapability");
       NT_TagString = $lex("TagString");
       ADT_TagString = $adt("TagString");
       NT_FunctionType = $sort("FunctionType");
       ADT_FunctionType = $adt("FunctionType");
       NT_Label = $sort("Label");
       ADT_Label = $adt("Label");
       NT_PostProtocolChars = $lex("PostProtocolChars");
       ADT_PostProtocolChars = $adt("PostProtocolChars");
       NT_Range = $sort("Range");
       ADT_Range = $adt("Range");
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
       NT_RegExpModifier = $lex("RegExpModifier");
       ADT_RegExpModifier = $adt("RegExpModifier");
       NT_ImportedModule = $sort("ImportedModule");
       ADT_ImportedModule = $adt("ImportedModule");
       NT_BooleanLiteral = $lex("BooleanLiteral");
       ADT_BooleanLiteral = $adt("BooleanLiteral");
       NT_Case = $sort("Case");
       ADT_Case = $adt("Case");
       NT_DateTimeLiteral = $sort("DateTimeLiteral");
       ADT_DateTimeLiteral = $adt("DateTimeLiteral");
       ADT_LocationChangeEvent = $adt("LocationChangeEvent");
       NT_DatePart = $lex("DatePart");
       ADT_DatePart = $adt("DatePart");
       NT_Target = $sort("Target");
       ADT_Target = $adt("Target");
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
       NT_Renamings = $sort("Renamings");
       ADT_Renamings = $adt("Renamings");
       NT_KeywordFormal = $sort("KeywordFormal");
       ADT_KeywordFormal = $adt("KeywordFormal");
       NT_OptionalExpression = $sort("OptionalExpression");
       ADT_OptionalExpression = $adt("OptionalExpression");
       ADT_GrammarDefinition = $adt("GrammarDefinition");
       NT_Field = $sort("Field");
       ADT_Field = $adt("Field");
       NT_LocationLiteral = $sort("LocationLiteral");
       ADT_LocationLiteral = $adt("LocationLiteral");
       NT_Tag = $sort("Tag");
       ADT_Tag = $adt("Tag");
       NT_Type = $sort("Type");
       ADT_Type = $adt("Type");
       ADT_Symbol = $adt("Symbol");
       NT_HexIntegerLiteral = $lex("HexIntegerLiteral");
       ADT_HexIntegerLiteral = $adt("HexIntegerLiteral");
       NT_DataTarget = $sort("DataTarget");
       ADT_DataTarget = $adt("DataTarget");
       NT_TimeZonePart = $lex("TimeZonePart");
       ADT_TimeZonePart = $adt("TimeZonePart");
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
       ADT_Associativity = $adt("Associativity");
       NT_Sym = $sort("Sym");
       ADT_Sym = $adt("Sym");
       ADT_LAYOUTLIST = $layouts("LAYOUTLIST");
       NT_RegExp = $lex("RegExp");
       ADT_RegExp = $adt("RegExp");
       NT_Tags = $sort("Tags");
       ADT_Tags = $adt("Tags");
       NT_URLChars = $lex("URLChars");
       ADT_URLChars = $adt("URLChars");
       NT_ModuleActuals = $sort("ModuleActuals");
       ADT_ModuleActuals = $adt("ModuleActuals");
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
       NT_CaseInsensitiveStringConstant = $lex("CaseInsensitiveStringConstant");
       ADT_CaseInsensitiveStringConstant = $adt("CaseInsensitiveStringConstant");
       NT_Char = $lex("Char");
       ADT_Char = $adt("Char");
       NT_JustDate = $lex("JustDate");
       ADT_JustDate = $adt("JustDate");
       NT_ProdModifier = $sort("ProdModifier");
       ADT_ProdModifier = $adt("ProdModifier");
       NT_Header = $sort("Header");
       ADT_Header = $adt("Header");
       ADT_LocationType = $adt("LocationType");
       NT_Assignment = $sort("Assignment");
       ADT_Assignment = $adt("Assignment");
       NT_OptionalComma = $lex("OptionalComma");
       ADT_OptionalComma = $adt("OptionalComma");
       NT_StringConstant = $lex("StringConstant");
       ADT_StringConstant = $adt("StringConstant");
       NT_FunctionDeclaration = $sort("FunctionDeclaration");
       ADT_FunctionDeclaration = $adt("FunctionDeclaration");
       NT_TypeVar = $sort("TypeVar");
       ADT_TypeVar = $adt("TypeVar");
       NT_Variant = $sort("Variant");
       ADT_Variant = $adt("Variant");
       NT_UserType = $sort("UserType");
       ADT_UserType = $adt("UserType");
       NT_Import = $sort("Import");
       ADT_Import = $adt("Import");
       NT_FunctionModifiers = $sort("FunctionModifiers");
       ADT_FunctionModifiers = $adt("FunctionModifiers");
       NT_Formals = $sort("Formals");
       ADT_Formals = $adt("Formals");
       NT_ConcreteHole = $sort("ConcreteHole");
       ADT_ConcreteHole = $adt("ConcreteHole");
       ADT_Message = $adt("Message");
       ADT_CharRange = $adt("CharRange");
       ADT_Grammar = $adt("Grammar");
       NT_StringMiddle = $sort("StringMiddle");
       ADT_StringMiddle = $adt("StringMiddle");
       NT_RealLiteral = $lex("RealLiteral");
       ADT_RealLiteral = $adt("RealLiteral");
       NT_Comprehension = $sort("Comprehension");
       ADT_Comprehension = $adt("Comprehension");
       NT_QualifiedName = $sort("QualifiedName");
       ADT_QualifiedName = $adt("QualifiedName");
       NT_MidPathChars = $lex("MidPathChars");
       ADT_MidPathChars = $adt("MidPathChars");
       NT_StringTemplate = $sort("StringTemplate");
       ADT_StringTemplate = $adt("StringTemplate");
       NT_OctalIntegerLiteral = $lex("OctalIntegerLiteral");
       ADT_OctalIntegerLiteral = $adt("OctalIntegerLiteral");
       NT_ProtocolPart = $sort("ProtocolPart");
       ADT_ProtocolPart = $adt("ProtocolPart");
       NT_NamedBackslash = $lex("NamedBackslash");
       ADT_NamedBackslash = $adt("NamedBackslash");
       ADT_RascalKeywords = $keywords("RascalKeywords");
       NT_Commands = $sort("Commands");
       ADT_Commands = $adt("Commands");
       NT_Comment = $lex("Comment");
       ADT_Comment = $adt("Comment");
       NT_StructuredType = $sort("StructuredType");
       ADT_StructuredType = $adt("StructuredType");
       NT_Visit = $sort("Visit");
       ADT_Visit = $adt("Visit");
       NT_NamedRegExp = $lex("NamedRegExp");
       ADT_NamedRegExp = $adt("NamedRegExp");
       NT_PreProtocolChars = $lex("PreProtocolChars");
       ADT_PreProtocolChars = $adt("PreProtocolChars");
       ADT_RuntimeException = $adt("RuntimeException");
       NT_DecimalIntegerLiteral = $lex("DecimalIntegerLiteral");
       ADT_DecimalIntegerLiteral = $adt("DecimalIntegerLiteral");
       NT_Assoc = $sort("Assoc");
       ADT_Assoc = $adt("Assoc");
       NT_MidProtocolChars = $lex("MidProtocolChars");
       ADT_MidProtocolChars = $adt("MidProtocolChars");
       NT_Command = $sort("Command");
       ADT_Command = $adt("Command");
       NT_Parameters = $sort("Parameters");
       ADT_Parameters = $adt("Parameters");
       NT_PathTail = $sort("PathTail");
       ADT_PathTail = $adt("PathTail");
       NT_Literal = $sort("Literal");
       ADT_Literal = $adt("Literal");
       NT_Statement = $sort("Statement");
       ADT_Statement = $adt("Statement");
       NT_ConcretePart = $lex("ConcretePart");
       ADT_ConcretePart = $adt("ConcretePart");
       NT_PathChars = $lex("PathChars");
       ADT_PathChars = $adt("PathChars");
       NT_Toplevel = $sort("Toplevel");
       ADT_Toplevel = $adt("Toplevel");
       ADT_Exception = $adt("Exception");
       NT_DateAndTime = $lex("DateAndTime");
       ADT_DateAndTime = $adt("DateAndTime");
       NT_Module = $sort("Module");
       ADT_Module = $adt("Module");
       NT_PatternWithAction = $sort("PatternWithAction");
       ADT_PatternWithAction = $adt("PatternWithAction");
       NT_UnicodeEscape = $lex("UnicodeEscape");
       ADT_UnicodeEscape = $adt("UnicodeEscape");
       NT_Assignable = $sort("Assignable");
       ADT_Assignable = $adt("Assignable");
       NT_BasicType = $sort("BasicType");
       ADT_BasicType = $adt("BasicType");
       NT_CommonKeywordParameters = $sort("CommonKeywordParameters");
       ADT_CommonKeywordParameters = $adt("CommonKeywordParameters");
       NT_ProtocolTail = $sort("ProtocolTail");
       ADT_ProtocolTail = $adt("ProtocolTail");
       NT_PostPathChars = $lex("PostPathChars");
       ADT_PostPathChars = $adt("PostPathChars");
       NT_StringCharacter = $lex("StringCharacter");
       ADT_StringCharacter = $adt("StringCharacter");
       NT_EvalCommand = $sort("EvalCommand");
       ADT_EvalCommand = $adt("EvalCommand");
       NT_JustTime = $lex("JustTime");
       ADT_JustTime = $adt("JustTime");
       NT_FunctionModifier = $sort("FunctionModifier");
       ADT_FunctionModifier = $adt("FunctionModifier");
       $T7 = $TF.valueType();
       $T17 = $TF.parameterType("A", $T7);
       $T14 = $TF.stringType();
       $T12 = $TF.parameterType("A", $T7);
       $T22 = $TF.parameterType("A", $T7);
       $T10 = $TF.parameterType("U", $T7);
       $T8 = $TF.parameterType("T", $T7);
       $T2 = $RTF.nonTerminalType($RVF.constructor(RascalValueFactory.Symbol_Lit, $RVF.string(",")));
       NT_Mapping_Expression = $parameterizedSort("Mapping", new Type[] { ADT_Expression }, $RVF.list($RVF.constructor(RascalValueFactory.Symbol_Sort, $RVF.string("Expression"))));
       $T15 = $TF.parameterType("T", ADT_Tree);
       NT_KeywordArguments_1 = $parameterizedSort("KeywordArguments", new Type[] { $T15 }, $RVF.list($RVF.constructor(RascalValueFactory.Symbol_Parameter, $RVF.string("T"), $RVF.constructor(RascalValueFactory.Symbol_Adt, $RVF.string("Tree"), $RVF.list()))));
       NT_Mapping_Pattern = $parameterizedSort("Mapping", new Type[] { ADT_Pattern }, $RVF.list($RVF.constructor(RascalValueFactory.Symbol_Sort, $RVF.string("Pattern"))));
       $T16 = $TF.parameterType("T", ADT_Tree);
       NT_KeywordArguments_Expression = $parameterizedSort("KeywordArguments", new Type[] { ADT_Expression }, $RVF.list($RVF.constructor(RascalValueFactory.Symbol_Sort, $RVF.string("Expression"))));
       NT_Mapping_1 = $parameterizedSort("Mapping", new Type[] { $T15 }, $RVF.list($RVF.constructor(RascalValueFactory.Symbol_Parameter, $RVF.string("T"), $RVF.constructor(RascalValueFactory.Symbol_Adt, $RVF.string("Tree"), $RVF.list()))));
       ADT_Maybe_1 = $parameterizedAdt("Maybe", new Type[] { $T17 });
       ADT_Maybe_Attr = $parameterizedAdt("Maybe", new Type[] { ADT_Attr });
       $T13 = $TF.listType($T8);
       NT_KeywordArguments_Pattern = $parameterizedSort("KeywordArguments", new Type[] { ADT_Pattern }, $RVF.list($RVF.constructor(RascalValueFactory.Symbol_Sort, $RVF.string("Pattern"))));
       NT_KeywordArgument_1 = $parameterizedSort("KeywordArgument", new Type[] { $T15 }, $RVF.list($RVF.constructor(RascalValueFactory.Symbol_Parameter, $RVF.string("T"), $RVF.constructor(RascalValueFactory.Symbol_Adt, $RVF.string("Tree"), $RVF.list()))));
       $T18 = $RTF.nonTerminalType($RVF.constructor(RascalValueFactory.Symbol_IterSeps, $RVF.constructor(RascalValueFactory.Symbol_Sort, $RVF.string("Sym")), $RVF.list($RVF.constructor(RascalValueFactory.Symbol_Layouts, $RVF.string("LAYOUTLIST")), $RVF.constructor(RascalValueFactory.Symbol_Lit, $RVF.string(",")), $RVF.constructor(RascalValueFactory.Symbol_Layouts, $RVF.string("LAYOUTLIST")))));
       $T4 = $TF.setType(NT_SyntaxDefinition);
       ADT_Maybe_Associativity = $parameterizedAdt("Maybe", new Type[] { ADT_Associativity });
       $T21 = $TF.listType(ADT_Tree);
       $T19 = $RTF.nonTerminalType($RVF.constructor(RascalValueFactory.Symbol_IterStarSeps, $RVF.constructor(RascalValueFactory.Symbol_Sort, $RVF.string("ProdModifier")), $RVF.list($RVF.constructor(RascalValueFactory.Symbol_Layouts, $RVF.string("LAYOUTLIST")))));
       $T9 = $RTF.reifiedType($T10);
       $T11 = $TF.setType(ADT_Production);
       ADT_TreeSearchResult_1 = $parameterizedAdt("TreeSearchResult", new Type[] { $T15 });
       $T5 = $TF.listType(ADT_Symbol);
       $T1 = $RTF.nonTerminalType($RVF.constructor(RascalValueFactory.Symbol_IterSeps, $RVF.constructor(RascalValueFactory.Symbol_Sort, $RVF.string("Expression")), $RVF.list($RVF.constructor(RascalValueFactory.Symbol_Layouts, $RVF.string("LAYOUTLIST")), $RVF.constructor(RascalValueFactory.Symbol_Lit, $RVF.string(",")), $RVF.constructor(RascalValueFactory.Symbol_Layouts, $RVF.string("LAYOUTLIST")))));
       $T20 = $RTF.nonTerminalType($RVF.constructor(RascalValueFactory.Symbol_IterStarSeps, $RVF.constructor(RascalValueFactory.Symbol_Sort, $RVF.string("Sym")), $RVF.list($RVF.constructor(RascalValueFactory.Symbol_Layouts, $RVF.string("LAYOUTLIST")))));
       $T6 = $RTF.reifiedType($T8);
       ADT_Maybe_Symbol = $parameterizedAdt("Maybe", new Type[] { ADT_Symbol });
       $T0 = $TF.setType(ADT_Condition);
       $T3 = $TF.setType(ADT_Symbol);
       ADT_Mapping_Expression = $TF.abstractDataType($TS, "Mapping", new Type[] { ADT_Expression });
       ADT_KeywordArguments_1 = $TF.abstractDataType($TS, "KeywordArguments", new Type[] { $T15 });
       ADT_Mapping_Pattern = $TF.abstractDataType($TS, "Mapping", new Type[] { ADT_Pattern });
       ADT_KeywordArguments_Expression = $TF.abstractDataType($TS, "KeywordArguments", new Type[] { ADT_Expression });
       ADT_Mapping_1 = $TF.abstractDataType($TS, "Mapping", new Type[] { $T15 });
       ADT_KeywordArguments_Pattern = $TF.abstractDataType($TS, "KeywordArguments", new Type[] { ADT_Pattern });
       ADT_KeywordArgument_1 = $TF.abstractDataType($TS, "KeywordArgument", new Type[] { $T15 });
       Maybe_Attr_just_Attr = $TF.constructor($TS, ADT_Maybe_Attr, "just", M_ParseTree.ADT_Attr, "val");
       Maybe_Associativity_just_Associativity = $TF.constructor($TS, ADT_Maybe_Associativity, "just", M_ParseTree.ADT_Associativity, "val");
       Maybe_Symbol_just_Symbol = $TF.constructor($TS, ADT_Maybe_Symbol, "just", M_ParseTree.ADT_Symbol, "val");
    
       
       
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
    public IString intercalate(IValue $P0, IValue $P1){ // Generated by Resolver
       return (IString) M_List.intercalate($P0, $P1);
    }
    public IValue head(IValue $P0){ // Generated by Resolver
       return (IValue) M_List.head($P0);
    }
    public IList head(IValue $P0, IValue $P1){ // Generated by Resolver
       return (IList) M_List.head($P0, $P1);
    }
    public IInteger size(IValue $P0){ // Generated by Resolver
       return (IInteger) M_List.size($P0);
    }
    public IBool isStrType(IValue $P0){ // Generated by Resolver
       return (IBool) M_Type.isStrType($P0);
    }
    public IMap toMap(IValue $P0){ // Generated by Resolver
       return (IMap) M_List.toMap($P0);
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
    public IList mapper(IValue $P0, IValue $P1){ // Generated by Resolver
       return (IList) M_List.mapper($P0, $P1);
    }
    public IBool isListType(IValue $P0){ // Generated by Resolver
       return (IBool) M_Type.isListType($P0);
    }
    public IBool isRealType(IValue $P0){ // Generated by Resolver
       return (IBool) M_Type.isRealType($P0);
    }
    public IList reverse(IValue $P0){ // Generated by Resolver
       return (IList) M_List.reverse($P0);
    }
    public IBool isTypeVar(IValue $P0){ // Generated by Resolver
       return (IBool) M_Type.isTypeVar($P0);
    }
    public TypedFunctionInstance2<IValue, IValue, IValue> firstAmbiguityFinder(IValue $P0, java.util.Map<java.lang.String,IValue> $kwpActuals){ // Generated by Resolver
       return (TypedFunctionInstance2<IValue, IValue, IValue>) M_ParseTree.firstAmbiguityFinder($P0, $kwpActuals);
    }
    public IBool isEmpty(IValue $P0){ // Generated by Resolver
       return (IBool) M_List.isEmpty($P0);
    }
    public IList remove(IValue $P0, IValue $P1){ // Generated by Resolver
       return (IList) M_List.remove($P0, $P1);
    }
    public IValue max(IValue $P0){ // Generated by Resolver
       return (IValue) M_List.max($P0);
    }
    public IConstructor priority(IValue $P0, IValue $P1){ // Generated by Resolver
       return (IConstructor) M_ParseTree.priority($P0, $P1);
    }
    public IValue getFirstFrom(IValue $P0){ // Generated by Resolver
       return (IValue) M_List.getFirstFrom($P0);
    }
    public IMap distribution(IValue $P0){ // Generated by Resolver
       return (IMap) M_List.distribution($P0);
    }
    public IConstructor mod2assoc(IValue $P0){ // Generated by Resolver
       return (IConstructor) M_lang_rascal_grammar_definition_Attributes.mod2assoc($P0);
    }
    public TypedFunctionInstance2<IValue, IValue, IValue> loadParser(IValue $P0, IValue $P1, java.util.Map<java.lang.String,IValue> $kwpActuals){ // Generated by Resolver
       return (TypedFunctionInstance2<IValue, IValue, IValue>) M_ParseTree.loadParser($P0, $P1, $kwpActuals);
    }
    public IString printSymbol(IValue $P0, IValue $P1){ // Generated by Resolver
       return (IString) M_ParseTree.printSymbol($P0, $P1);
    }
    public IBool isNodeType(IValue $P0){ // Generated by Resolver
       return (IBool) M_Type.isNodeType($P0);
    }
    public INode conditional(IValue $P0, IValue $P1){ // Generated by Resolver
       INode $result = null;
       Type $P0Type = $P0.getType();
       Type $P1Type = $P1.getType();
       if($isSubtypeOf($P0Type, M_ParseTree.ADT_Symbol) && $isSubtypeOf($P1Type,$T0)){
         $result = (INode)M_lang_rascal_grammar_definition_Symbols.lang_rascal_grammar_definition_Symbols_conditional$f9ac60504818807f((IConstructor) $P0, (ISet) $P1);
         if($result != null) return $result;
       }
       if($isSubtypeOf($P0Type, M_ParseTree.ADT_Symbol) && $isSubtypeOf($P1Type,$T0)){
         $result = (INode)M_lang_rascal_grammar_definition_Symbols.lang_rascal_grammar_definition_Symbols_conditional$a78f69e7726562ef((IConstructor) $P0, (ISet) $P1);
         if($result != null) return $result;
       }
       if($isNonTerminal($P0Type, M_lang_rascal_syntax_Rascal.NT_Expression) && $isSubtypeOf($P1Type,$T1)){
         return $RVF.constructor(M_lang_rascal_syntax_Rascal.Replacement_conditional_Expression_iter_seps_Expression, new IValue[]{(ITree) $P0, (ITree) $P1});
       }
       if($isSubtypeOf($P0Type, M_ParseTree.ADT_Symbol) && $isSubtypeOf($P1Type,$T0)){
         return $RVF.constructor(M_ParseTree.Symbol_conditional_Symbol_set_Condition, new IValue[]{(IConstructor) $P0, (ISet) $P1});
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0, $P1));
    }
    public IConstructor grammar(IValue $P0){ // Generated by Resolver
       return (IConstructor) M_Grammar.grammar($P0);
    }
    public IConstructor grammar(IValue $P0, IValue $P1){ // Generated by Resolver
       return (IConstructor) M_Grammar.grammar($P0, $P1);
    }
    public IBool isSorted(IValue $P0, java.util.Map<java.lang.String,IValue> $kwpActuals){ // Generated by Resolver
       return (IBool) M_List.isSorted($P0, $kwpActuals);
    }
    public IList take(IValue $P0, IValue $P1){ // Generated by Resolver
       return (IList) M_List.take($P0, $P1);
    }
    public ISet toSet(IValue $P0){ // Generated by Resolver
       return (ISet) M_List.toSet($P0);
    }
    public IBool isReifiedType(IValue $P0){ // Generated by Resolver
       return (IBool) M_Type.isReifiedType($P0);
    }
    public ITuple takeOneFrom(IValue $P0){ // Generated by Resolver
       return (ITuple) M_List.takeOneFrom($P0);
    }
    public ISet dependencies(IValue $P0){ // Generated by Resolver
       return (ISet) M_Grammar.dependencies($P0);
    }
    public IConstructor alt(IValue $P0){ // Generated by Resolver
       IConstructor $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type,$T3)){
         $result = (IConstructor)M_lang_rascal_grammar_definition_Symbols.lang_rascal_grammar_definition_Symbols_alt$01fd93bf17a1bf85((ISet) $P0);
         if($result != null) return $result;
       }
       if($isSubtypeOf($P0Type,$T3)){
         return $RVF.constructor(M_ParseTree.Symbol_alt_set_Symbol, new IValue[]{(ISet) $P0});
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public IInteger indexOf(IValue $P0, IValue $P1){ // Generated by Resolver
       return (IInteger) M_List.indexOf($P0, $P1);
    }
    public IBool isRelType(IValue $P0){ // Generated by Resolver
       return (IBool) M_Type.isRelType($P0);
    }
    public ITuple unzip2(IValue $P0){ // Generated by Resolver
       return (ITuple) M_List.unzip2($P0);
    }
    public IBool isConstructorType(IValue $P0){ // Generated by Resolver
       return (IBool) M_Type.isConstructorType($P0);
    }
    public IConstructor var_func(IValue $P0, IValue $P1, IValue $P2){ // Generated by Resolver
       return (IConstructor) M_Type.var_func($P0, $P1, $P2);
    }
    public IBool equivalent(IValue $P0, IValue $P1){ // Generated by Resolver
       return (IBool) M_Type.equivalent($P0, $P1);
    }
    public IList takeWhile(IValue $P0, IValue $P1){ // Generated by Resolver
       return (IList) M_List.takeWhile($P0, $P1);
    }
    public INumber sum(IValue $P0){ // Generated by Resolver
       return (INumber) M_List.sum($P0);
    }
    public IList addParamLabels(IValue $P0, IValue $P1){ // Generated by Resolver
       return (IList) M_Type.addParamLabels($P0, $P1);
    }
    public IList delete(IValue $P0, IValue $P1){ // Generated by Resolver
       return (IList) M_List.delete($P0, $P1);
    }
    public IBool keepParams(IValue $P0, IValue $P1){ // Generated by Resolver
       return (IBool) M_Type.keepParams($P0, $P1);
    }
    public IBool isListRelType(IValue $P0){ // Generated by Resolver
       return (IBool) M_Type.isListRelType($P0);
    }
    public ITuple unzip3(IValue $P0){ // Generated by Resolver
       return (ITuple) M_List.unzip3($P0);
    }
    public IBool eq(IValue $P0, IValue $P1){ // Generated by Resolver
       return (IBool) M_Type.eq($P0, $P1);
    }
    public IList insertAt(IValue $P0, IValue $P1, IValue $P2){ // Generated by Resolver
       return (IList) M_List.insertAt($P0, $P1, $P2);
    }
    public IBool isMapType(IValue $P0){ // Generated by Resolver
       return (IBool) M_Type.isMapType($P0);
    }
    public IValue reducer(IValue $P0, IValue $P1, IValue $P2){ // Generated by Resolver
       return (IValue) M_List.reducer($P0, $P1, $P2);
    }
    public IString toString(IValue $P0){ // Generated by Resolver
       return (IString) M_List.toString($P0);
    }
    public ITuple rule2prod(IValue $P0){ // Generated by Resolver
       ITuple $result = null;
       Type $P0Type = $P0.getType();
       if($isNonTerminal($P0Type, M_lang_rascal_syntax_Rascal.NT_SyntaxDefinition)){
         $result = (ITuple)lang_rascal_grammar_definition_Productions_rule2prod$30da62f1b584d9ad((ITree) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public ISet permutations(IValue $P0){ // Generated by Resolver
       return (ISet) M_List.permutations($P0);
    }
    public TypedFunctionInstance3<IValue, IValue, IValue, IValue> loadParsers(IValue $P0, java.util.Map<java.lang.String,IValue> $kwpActuals){ // Generated by Resolver
       return (TypedFunctionInstance3<IValue, IValue, IValue, IValue>) M_ParseTree.loadParsers($P0, $kwpActuals);
    }
    public IBool isBoolType(IValue $P0){ // Generated by Resolver
       return (IBool) M_Type.isBoolType($P0);
    }
    public ISet imports(IValue $P0){ // Generated by Resolver
       return (ISet) M_Grammar.imports($P0);
    }
    public IValue make(IValue $P0, IValue $P1, IValue $P2){ // Generated by Resolver
       return (IValue) M_Type.make($P0, $P1, $P2);
    }
    public IValue make(IValue $P0, IValue $P1, IValue $P2, IValue $P3){ // Generated by Resolver
       return (IValue) M_Type.make($P0, $P1, $P2, $P3);
    }
    public IList drop(IValue $P0, IValue $P1){ // Generated by Resolver
       return (IList) M_List.drop($P0, $P1);
    }
    public IValue elementAt(IValue $P0, IValue $P1){ // Generated by Resolver
       return (IValue) M_List.elementAt($P0, $P1);
    }
    public IBool isLocType(IValue $P0){ // Generated by Resolver
       return (IBool) M_Type.isLocType($P0);
    }
    public IValue implode(IValue $P0, IValue $P1){ // Generated by Resolver
       return (IValue) M_ParseTree.implode($P0, $P1);
    }
    public TypedFunctionInstance3<IValue, IValue, IValue, IValue> firstAmbiguityFinders(IValue $P0, java.util.Map<java.lang.String,IValue> $kwpActuals){ // Generated by Resolver
       return (TypedFunctionInstance3<IValue, IValue, IValue, IValue>) M_ParseTree.firstAmbiguityFinders($P0, $kwpActuals);
    }
    public IList upTill(IValue $P0){ // Generated by Resolver
       return (IList) M_List.upTill($P0);
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
    public IList zip2(IValue $P0, IValue $P1){ // Generated by Resolver
       return (IList) M_List.zip2($P0, $P1);
    }
    public ITuple pop(IValue $P0){ // Generated by Resolver
       return (ITuple) M_List.pop($P0);
    }
    public IConstructor typeOf(IValue $P0){ // Generated by Resolver
       return (IConstructor) M_Type.typeOf($P0);
    }
    public IConstructor treeAt(IValue $P0, IValue $P1, IValue $P2){ // Generated by Resolver
       return (IConstructor) M_ParseTree.treeAt($P0, $P1, $P2);
    }
    public IMap toMapUnique(IValue $P0){ // Generated by Resolver
       return (IMap) M_List.toMapUnique($P0);
    }
    public IList index(IValue $P0){ // Generated by Resolver
       return (IList) M_List.index($P0);
    }
    public IBool allLabeled(IValue $P0){ // Generated by Resolver
       return (IBool) M_Type.allLabeled($P0);
    }
    public IValue min(IValue $P0){ // Generated by Resolver
       return (IValue) M_List.min($P0);
    }
    public IList prefix(IValue $P0){ // Generated by Resolver
       return (IList) M_List.prefix($P0);
    }
    public IList zip3(IValue $P0, IValue $P1, IValue $P2){ // Generated by Resolver
       return (IList) M_List.zip3($P0, $P1, $P2);
    }
    public IConstructor prod2prod(IValue $P0, IValue $P1){ // Generated by Resolver
       IConstructor $result = null;
       Type $P0Type = $P0.getType();
       Type $P1Type = $P1.getType();
       if($isSubtypeOf($P0Type, M_ParseTree.ADT_Symbol) && $isNonTerminal($P1Type, M_lang_rascal_syntax_Rascal.NT_Prod)){
         $result = (IConstructor)lang_rascal_grammar_definition_Productions_prod2prod$70dc8b016f745552((IConstructor) $P0, (ITree) $P1);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0, $P1));
    }
    public IList slice(IValue $P0, IValue $P1, IValue $P2){ // Generated by Resolver
       return (IList) M_List.slice($P0, $P1, $P2);
    }
    public IConstructor compose(IValue $P0, IValue $P1){ // Generated by Resolver
       return (IConstructor) M_Grammar.compose($P0, $P1);
    }
    public IBool isSetType(IValue $P0){ // Generated by Resolver
       return (IBool) M_Type.isSetType($P0);
    }
    public void storeParsers(IValue $P0, IValue $P1){ // Generated by Resolver
        M_ParseTree.storeParsers($P0, $P1);
    }
    public IInteger lastIndexOf(IValue $P0, IValue $P1){ // Generated by Resolver
       return (IInteger) M_List.lastIndexOf($P0, $P1);
    }
    public ITree parse(IValue $P0, IValue $P1, IValue $P2, java.util.Map<java.lang.String,IValue> $kwpActuals){ // Generated by Resolver
       return (ITree) M_ParseTree.parse($P0, $P1, $P2, $kwpActuals);
    }
    public ITree parse(IValue $P0, IValue $P1, java.util.Map<java.lang.String,IValue> $kwpActuals){ // Generated by Resolver
       return (ITree) M_ParseTree.parse($P0, $P1, $kwpActuals);
    }
    public TypedFunctionInstance3<IValue, IValue, IValue, IValue> parsers(IValue $P0, java.util.Map<java.lang.String,IValue> $kwpActuals){ // Generated by Resolver
       return (TypedFunctionInstance3<IValue, IValue, IValue, IValue>) M_ParseTree.parsers($P0, $kwpActuals);
    }
    public IList concat(IValue $P0){ // Generated by Resolver
       return (IList) M_List.concat($P0);
    }
    public IBool isRatType(IValue $P0){ // Generated by Resolver
       return (IBool) M_Type.isRatType($P0);
    }
    public IValue top(IValue $P0){ // Generated by Resolver
       return (IValue) M_List.top($P0);
    }
    public IString itoString(IValue $P0){ // Generated by Resolver
       return (IString) M_List.itoString($P0);
    }
    public IList getLabels(IValue $P0){ // Generated by Resolver
       return (IList) M_Type.getLabels($P0);
    }
    public IValue last(IValue $P0){ // Generated by Resolver
       return (IValue) M_List.last($P0);
    }
    public IList getParamLabels(IValue $P0){ // Generated by Resolver
       return (IList) M_Type.getParamLabels($P0);
    }
    public IBool isNumType(IValue $P0){ // Generated by Resolver
       return (IBool) M_Type.isNumType($P0);
    }
    public IList stripLabels(IValue $P0){ // Generated by Resolver
       return (IList) M_Type.stripLabels($P0);
    }
    public IConstructor syntax2grammar(IValue $P0){ // Generated by Resolver
       IConstructor $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type,$T4)){
         $result = (IConstructor)lang_rascal_grammar_definition_Productions_syntax2grammar$d142b5044e543aa6((ISet) $P0);
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
    public IList intersperse(IValue $P0, IValue $P1){ // Generated by Resolver
       return (IList) M_List.intersperse($P0, $P1);
    }
    public IList merge(IValue $P0, IValue $P1, IValue $P2){ // Generated by Resolver
       return (IList) M_List.merge($P0, $P1, $P2);
    }
    public IList merge(IValue $P0, IValue $P1){ // Generated by Resolver
       return (IList) M_List.merge($P0, $P1);
    }
    public IBool isVoidType(IValue $P0){ // Generated by Resolver
       return (IBool) M_Type.isVoidType($P0);
    }
    public IConstructor strip(IValue $P0){ // Generated by Resolver
       return (IConstructor) M_lang_rascal_grammar_definition_Symbols.strip($P0);
    }
    public IBool isNonTerminalType(IValue $P0){ // Generated by Resolver
       return (IBool) M_ParseTree.isNonTerminalType($P0);
    }
    public IValue typeCast(IValue $P0, IValue $P1){ // Generated by Resolver
       return (IValue) M_Type.typeCast($P0, $P1);
    }
    public IValue lub(IValue $P0, IValue $P1){ // Generated by Resolver
       return (IValue) M_Type.lub($P0, $P1);
    }
    public IList shuffle(IValue $P0){ // Generated by Resolver
       return (IList) M_List.shuffle($P0);
    }
    public IList shuffle(IValue $P0, IValue $P1){ // Generated by Resolver
       return (IList) M_List.shuffle($P0, $P1);
    }
    public ISet $extends(IValue $P0){ // Generated by Resolver
       return (ISet) M_Grammar.$extends($P0);
    }
    public IBool comparable(IValue $P0, IValue $P1){ // Generated by Resolver
       return (IBool) M_Type.comparable($P0, $P1);
    }
    public IBool subtype(IValue $P0, IValue $P1){ // Generated by Resolver
       IBool $result = null;
       Type $P0Type = $P0.getType();
       Type $P1Type = $P1.getType();
       switch(Fingerprint.getFingerprint($P0)){
       	
       case 1643638592:
       		if($isSubtypeOf($P0Type, M_ParseTree.ADT_Symbol) && $isSubtypeOf($P1Type, M_ParseTree.ADT_Symbol)){
       		  $result = (IBool)M_Type.Type_subtype$162da85a0f5a9f0d((IConstructor) $P0, (IConstructor) $P1);
       		  if($result != null) return $result;
       		}
       		break;	
       case 26576112:
       		if($isSubtypeOf($P0Type, M_ParseTree.ADT_Symbol) && $isSubtypeOf($P1Type, M_ParseTree.ADT_Symbol)){
       		  $result = (IBool)M_Type.Type_subtype$258479665eae36af((IConstructor) $P0, (IConstructor) $P1);
       		  if($result != null) return $result;
       		  $result = (IBool)M_Type.Type_subtype$0462d461bde80a82((IConstructor) $P0, (IConstructor) $P1);
       		  if($result != null) return $result;
       		}
       		break;	
       case 1725888:
       		if($isSubtypeOf($P0Type, M_ParseTree.ADT_Symbol) && $isSubtypeOf($P1Type, M_ParseTree.ADT_Symbol)){
       		  $result = (IBool)M_Type.Type_subtype$f6957636a33615ae((IConstructor) $P0, (IConstructor) $P1);
       		  if($result != null) return $result;
       		}
       		break;	
       case 1206598288:
       		if($isSubtypeOf($P0Type, M_ParseTree.ADT_Symbol) && $isSubtypeOf($P1Type, M_ParseTree.ADT_Symbol)){
       		  $result = (IBool)M_Type.Type_subtype$b674428cffef84bc((IConstructor) $P0, (IConstructor) $P1);
       		  if($result != null) return $result;
       		}
       		break;	
       case 97904160:
       		if($isSubtypeOf($P0Type, M_ParseTree.ADT_Symbol) && $isSubtypeOf($P1Type, M_ParseTree.ADT_Symbol)){
       		  $result = (IBool)M_Type.Type_subtype$98167e340333c9a5((IConstructor) $P0, (IConstructor) $P1);
       		  if($result != null) return $result;
       		  $result = (IBool)M_Type.Type_subtype$4fe5b133e2ee1de9((IConstructor) $P0, (IConstructor) $P1);
       		  if($result != null) return $result;
       		}
       		break;	
       case 28290288:
       		if($isSubtypeOf($P0Type, M_ParseTree.ADT_Symbol) && $isSubtypeOf($P1Type, M_ParseTree.ADT_Symbol)){
       		  $result = (IBool)M_ParseTree.ParseTree_subtype$384d8d76f0c7a053((IConstructor) $P0, (IConstructor) $P1);
       		  if($result != null) return $result;
       		}
       		break;	
       case 910096:
       		if($isSubtypeOf($P0Type, M_ParseTree.ADT_Symbol) && $isSubtypeOf($P1Type, M_ParseTree.ADT_Symbol)){
       		  $result = (IBool)M_Type.Type_subtype$ca59d9bf5276e15d((IConstructor) $P0, (IConstructor) $P1);
       		  if($result != null) return $result;
       		  $result = (IBool)M_Type.Type_subtype$e77633ea9a4ac6a5((IConstructor) $P0, (IConstructor) $P1);
       		  if($result != null) return $result;
       		}
       		break;	
       case 902344:
       		if($isSubtypeOf($P0Type, M_ParseTree.ADT_Symbol) && $isSubtypeOf($P1Type, M_ParseTree.ADT_Symbol)){
       		  $result = (IBool)M_Type.Type_subtype$21c6b8b775030d1d((IConstructor) $P0, (IConstructor) $P1);
       		  if($result != null) return $result;
       		  $result = (IBool)M_Type.Type_subtype$98e19b11a09faf67((IConstructor) $P0, (IConstructor) $P1);
       		  if($result != null) return $result;
       		}
       		break;	
       case -1322071552:
       		if($isSubtypeOf($P0Type, M_ParseTree.ADT_Symbol) && $isSubtypeOf($P1Type, M_ParseTree.ADT_Symbol)){
       		  $result = (IBool)M_Type.Type_subtype$0862159b9fa78cf9((IConstructor) $P0, (IConstructor) $P1);
       		  if($result != null) return $result;
       		}
       		break;	
       case 26641768:
       		if($isSubtypeOf($P0Type, M_ParseTree.ADT_Symbol) && $isSubtypeOf($P1Type, M_ParseTree.ADT_Symbol)){
       		  $result = (IBool)M_Type.Type_subtype$ab363c241c416a71((IConstructor) $P0, (IConstructor) $P1);
       		  if($result != null) return $result;
       		  $result = (IBool)M_Type.Type_subtype$4de9a977591be6e5((IConstructor) $P0, (IConstructor) $P1);
       		  if($result != null) return $result;
       		}
       		break;	
       case 778304:
       		if($isSubtypeOf($P0Type, M_ParseTree.ADT_Symbol) && $isSubtypeOf($P1Type, M_ParseTree.ADT_Symbol)){
       		  $result = (IBool)M_Type.Type_subtype$23f59dc1171dc69d((IConstructor) $P0, (IConstructor) $P1);
       		  if($result != null) return $result;
       		}
       		break;	
       case 100948096:
       		if($isSubtypeOf($P0Type, M_ParseTree.ADT_Symbol) && $isSubtypeOf($P1Type, M_ParseTree.ADT_Symbol)){
       		  $result = (IBool)M_Type.Type_subtype$ddf53e134f4d5416((IConstructor) $P0, (IConstructor) $P1);
       		  if($result != null) return $result;
       		}
       		break;	
       case 112955840:
       		if($isSubtypeOf($P0Type, M_ParseTree.ADT_Symbol) && $isSubtypeOf($P1Type, M_ParseTree.ADT_Symbol)){
       		  $result = (IBool)M_Type.Type_subtype$bc5943e83a6df899((IConstructor) $P0, (IConstructor) $P1);
       		  if($result != null) return $result;
       		  $result = (IBool)M_Type.Type_subtype$282ad33dd55efdcc((IConstructor) $P0, (IConstructor) $P1);
       		  if($result != null) return $result;
       		}
       		break;	
       case 1542928:
       		if($isSubtypeOf($P0Type, M_ParseTree.ADT_Symbol) && $isSubtypeOf($P1Type, M_ParseTree.ADT_Symbol)){
       		  $result = (IBool)M_Type.Type_subtype$5f5250bbf1aff423((IConstructor) $P0, (IConstructor) $P1);
       		  if($result != null) return $result;
       		  $result = (IBool)M_Type.Type_subtype$15cedff9916fdbee((IConstructor) $P0, (IConstructor) $P1);
       		  if($result != null) return $result;
       		}
       		break;	
       case 885800512:
       		if($isSubtypeOf($P0Type, M_ParseTree.ADT_Symbol) && $isSubtypeOf($P1Type, M_ParseTree.ADT_Symbol)){
       		  $result = (IBool)M_Type.Type_subtype$44422dfea95218a8((IConstructor) $P0, (IConstructor) $P1);
       		  if($result != null) return $result;
       		}
       		break;
       }
       if($isSubtypeOf($P0Type,$T5) && $isSubtypeOf($P1Type,$T5)){
         $result = (IBool)M_Type.Type_subtype$e6962df5576407da((IList) $P0, (IList) $P1);
         if($result != null) return $result;
       }
       if($isSubtypeOf($P0Type, M_ParseTree.ADT_Symbol) && $isSubtypeOf($P1Type, M_ParseTree.ADT_Symbol)){
         $result = (IBool)M_Type.Type_subtype$cfecefb3bc3fa773((IConstructor) $P0, (IConstructor) $P1);
         if($result != null) return $result;
         $result = (IBool)M_Type.Type_subtype$53c4de769757bddc((IConstructor) $P0, (IConstructor) $P1);
         if($result != null) return $result;
         $result = (IBool)M_Type.Type_subtype$2750c116f0b05084((IConstructor) $P0, (IConstructor) $P1);
         if($result != null) return $result;
         $result = (IBool)M_Type.Type_subtype$39fbab80e9db10e1((IConstructor) $P0, (IConstructor) $P1);
         if($result != null) return $result;
         $result = (IBool)M_Type.Type_subtype$3eada106dbc66d2d((IConstructor) $P0, (IConstructor) $P1);
         if($result != null) return $result;
         $result = (IBool)M_Type.Type_subtype$30215aaed6c33fd7((IConstructor) $P0, (IConstructor) $P1);
         if($result != null) return $result;
         $result = (IBool)M_Type.Type_subtype$1b2387a35f10c1e0((IConstructor) $P0, (IConstructor) $P1);
         if($result != null) return $result;
         $result = (IBool)M_Type.Type_subtype$80633493313ebd18((IConstructor) $P0, (IConstructor) $P1);
         if($result != null) return $result;
         $result = (IBool)M_Type.Type_subtype$3aa09e73e41fcf84((IConstructor) $P0, (IConstructor) $P1);
         if($result != null) return $result;
       }
       if($isSubtypeOf($P0Type,$T6) && $isSubtypeOf($P1Type,$T9)){
         $result = (IBool)M_Type.Type_subtype$7b9c005ac35dd586((IConstructor) $P0, (IConstructor) $P1);
         if($result != null) return $result;
       }
       if($isSubtypeOf($P0Type, M_ParseTree.ADT_Symbol) && $isSubtypeOf($P1Type, M_ParseTree.ADT_Symbol)){
         $result = (IBool)M_Type.Type_subtype$06d2c71d010480ef((IConstructor) $P0, (IConstructor) $P1);
         if($result != null) return $result;
       }
       if($isSubtypeOf($P0Type,$T5) && $isSubtypeOf($P1Type,$T5)){
         $result = (IBool)M_Type.Type_subtype$812a7f34ff841fdb((IList) $P0, (IList) $P1);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0, $P1));
    }
    public IValue getOneFrom(IValue $P0){ // Generated by Resolver
       return (IValue) M_List.getOneFrom($P0);
    }
    public IString unparse(IValue $P0){ // Generated by Resolver
       return (IString) M_ParseTree.unparse($P0);
    }
    public IConstructor associativity(IValue $P0, IValue $P1, IValue $P2){ // Generated by Resolver
       IConstructor $result = null;
       Type $P0Type = $P0.getType();
       Type $P1Type = $P1.getType();
       Type $P2Type = $P2.getType();
       if($isSubtypeOf($P0Type, M_ParseTree.ADT_Symbol) && $isSubtypeOf($P1Type, M_ParseTree.ADT_Associativity) && $isSubtypeOf($P2Type,$T11)){
         $result = (IConstructor)M_ParseTree.ParseTree_associativity$9299e943b00366a7((IConstructor) $P0, (IConstructor) $P1, (ISet) $P2);
         if($result != null) return $result;
         $result = (IConstructor)M_ParseTree.ParseTree_associativity$95843a2f3959b22f((IConstructor) $P0, (IConstructor) $P1, (ISet) $P2);
         if($result != null) return $result;
         $result = (IConstructor)M_ParseTree.ParseTree_associativity$05ee42b13b7e96fb((IConstructor) $P0, (IConstructor) $P1, (ISet) $P2);
         if($result != null) return $result;
       }
       if($isSubtypeOf($P0Type, M_ParseTree.ADT_Symbol) && $isSubtypeOf($P1Type, M_util_Maybe.ADT_Maybe_1) && $isSubtypeOf($P2Type, M_ParseTree.ADT_Production)){
         $result = (IConstructor)lang_rascal_grammar_definition_Productions_associativity$09cd814bba935894((IConstructor) $P0, (IConstructor) $P1, (IConstructor) $P2);
         if($result != null) return $result;
       }
       if($isSubtypeOf($P0Type, M_ParseTree.ADT_Symbol) && $isSubtypeOf($P1Type, ADT_Maybe_Associativity) && $isSubtypeOf($P2Type, M_ParseTree.ADT_Production)){
         $result = (IConstructor)lang_rascal_grammar_definition_Productions_associativity$fe1234ba22a8be5e((IConstructor) $P0, (IConstructor) $P1, (IConstructor) $P2);
         if($result != null) return $result;
       }
       if($isSubtypeOf($P0Type, M_ParseTree.ADT_Symbol) && $isSubtypeOf($P1Type, M_ParseTree.ADT_Associativity) && $isSubtypeOf($P2Type,$T11)){
         return $RVF.constructor(M_ParseTree.Production_associativity_Symbol_Associativity_set_Production, new IValue[]{(IConstructor) $P0, (IConstructor) $P1, (ISet) $P2});
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0, $P1, $P2));
    }
    public IMap removeFromBag(IValue $P0, IValue $P1, IValue $P2){ // Generated by Resolver
       return (IMap) M_List.removeFromBag($P0, $P1, $P2);
    }
    public IMap removeFromBag(IValue $P0, IValue $P1){ // Generated by Resolver
       return (IMap) M_List.removeFromBag($P0, $P1);
    }
    public ITuple split(IValue $P0){ // Generated by Resolver
       return (ITuple) M_List.split($P0);
    }
    public IList push(IValue $P0, IValue $P1){ // Generated by Resolver
       return (IList) M_List.push($P0, $P1);
    }
    public IBool noneLabeled(IValue $P0){ // Generated by Resolver
       return (IBool) M_Type.noneLabeled($P0);
    }
    public ISet permutationsBag(IValue $P0){ // Generated by Resolver
       return (ISet) M_List.permutationsBag($P0);
    }
    public IList mix(IValue $P0, IValue $P1){ // Generated by Resolver
       return (IList) M_List.mix($P0, $P1);
    }
    public IInteger mainMessageHandler(IValue $P0, java.util.Map<java.lang.String,IValue> $kwpActuals){ // Generated by Resolver
       return (IInteger) M_Message.mainMessageHandler($P0, $kwpActuals);
    }
    public IBool isFunctionType(IValue $P0){ // Generated by Resolver
       return (IBool) M_Type.isFunctionType($P0);
    }
    public IValue glb(IValue $P0, IValue $P1){ // Generated by Resolver
       return (IValue) M_Type.glb($P0, $P1);
    }
    public ITree firstAmbiguity(IValue $P0, IValue $P1){ // Generated by Resolver
       return (ITree) M_ParseTree.firstAmbiguity($P0, $P1);
    }
    public ISet toRel(IValue $P0){ // Generated by Resolver
       return (ISet) M_List.toRel($P0);
    }
    public IValue sort(IValue $P0){ // Generated by Resolver
       IValue $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type,$T13)){
         $result = (IValue)M_List.List_sort$1fe4426c8c8039da((IList) $P0);
         if($result != null) return $result;
       }
       if($isSubtypeOf($P0Type,$T14)){
         return $RVF.constructor(M_ParseTree.Symbol_sort_str, new IValue[]{(IString) $P0});
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public IList sort(IValue $P0, IValue $P1){ // Generated by Resolver
       return (IList) M_List.sort($P0, $P1);
    }
    public TypedFunctionInstance2<IValue, IValue, IValue> parser(IValue $P0, java.util.Map<java.lang.String,IValue> $kwpActuals){ // Generated by Resolver
       return (TypedFunctionInstance2<IValue, IValue, IValue>) M_ParseTree.parser($P0, $kwpActuals);
    }
    public IConstructor seq(IValue $P0){ // Generated by Resolver
       IConstructor $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type,$T5)){
         $result = (IConstructor)M_lang_rascal_grammar_definition_Symbols.lang_rascal_grammar_definition_Symbols_seq$5dde90ea795fac79((IList) $P0);
         if($result != null) return $result;
       }
       if($isSubtypeOf($P0Type,$T5)){
         return $RVF.constructor(M_ParseTree.Symbol_seq_list_Symbol, new IValue[]{(IList) $P0});
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public IList dup(IValue $P0){ // Generated by Resolver
       return (IList) M_List.dup($P0);
    }
    public IBool isIntType(IValue $P0){ // Generated by Resolver
       return (IBool) M_Type.isIntType($P0);
    }
    public IBool isDateTimeType(IValue $P0){ // Generated by Resolver
       return (IBool) M_Type.isDateTimeType($P0);
    }
    public IString write(IValue $P0, java.util.Map<java.lang.String,IValue> $kwpActuals){ // Generated by Resolver
       return (IString) M_Message.write($P0, $kwpActuals);
    }

    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/lang/rascal/grammar/definition/Productions.rsc|(739,333,<24,0>,<36,1>) 
    public IConstructor lang_rascal_grammar_definition_Productions_syntax2grammar$d142b5044e543aa6(ISet defs_0){ 
        
        
        ISet prods_1 = ((ISet)($RVF.set(((IConstructor)($RVF.constructor(M_ParseTree.Production_prod_Symbol_list_Symbol_set_Attr, new IValue[]{((IConstructor)($RVF.constructor(M_ParseTree.Symbol_empty_, new IValue[]{}))), ((IList)$constants.get(0)/*[]*/), ((ISet)$constants.get(1)/*{}*/)}))), ((IConstructor)$constants.get(2)/*prod(layouts("$default$"),[],{})*/))));
        ISet starts_2 = ((ISet)$constants.get(1)/*{}*/);
        /*muExists*/FOR0: 
            do {
                FOR0_GEN927:
                for(IValue $elem1_for : ((ISet)defs_0)){
                    ITree $elem1 = (ITree) $elem1_for;
                    ITree sd_3 = ((ITree)($elem1));
                    ITuple $TMP0 = (ITuple)($me.rule2prod(((ITree)sd_3)));
                    ISet ps_4 = ((ISet)($atuple_subscript_int(((ITuple)($TMP0)),0)));
                    IConstructor st_5 = ((IConstructor)($atuple_subscript_int(((ITuple)($TMP0)),1)));
                    prods_1 = ((ISet)($aset_add_aset(((ISet)prods_1),((ISet)ps_4))));
                    if($is(((IConstructor)st_5),((IString)$constants.get(4)/*"just"*/))){
                       starts_2 = ((ISet)($aset_add_elm(((ISet)starts_2),((IConstructor)(((IConstructor)($aadt_get_field(((IConstructor)st_5), "val"))))))));
                    
                    }
                
                }
                continue FOR0;
                            
            } while(false);
        /* void:  muCon([]) */return ((IConstructor)($me.grammar(((ISet)starts_2), ((ISet)prods_1))));
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/lang/rascal/grammar/definition/Productions.rsc|(1074,1297,<38,0>,<58,1>) 
    public ITuple lang_rascal_grammar_definition_Productions_rule2prod$30da62f1b584d9ad(ITree sd_0){ 
        
        
        final ITree $switchVal2 = ((ITree)sd_0);
        boolean noCaseMatched_$switchVal2 = true;
        SWITCH2: switch(Fingerprint.getFingerprint($switchVal2)){
        
            case 0:
                if(noCaseMatched_$switchVal2){
                    noCaseMatched_$switchVal2 = false;
                    
                }
                
        
            default: if($isSubtypeOf($switchVal2.getType(),M_lang_rascal_syntax_Rascal.NT_SyntaxDefinition)){
                        /*muExists*/CASE_0_0: 
                            do {
                                if($nonterminal_has_name_and_arity($switchVal2, "layout", 3)){
                                   IValue $arg0_8 = (IValue)($nonterminal_get_arg(((ITree)((ITree)($switchVal2))), ((IInteger)$constants.get(5)/*0*/).intValue()));
                                   if($isComparable($arg0_8.getType(), $T7)){
                                      IValue $arg1_6 = (IValue)($nonterminal_get_arg(((ITree)((ITree)($switchVal2))), ((IInteger)$constants.get(6)/*1*/).intValue()));
                                      if($isComparable($arg1_6.getType(), M_lang_rascal_syntax_Rascal.NT_Sym)){
                                         if($nonterminal_has_name_and_arity($arg1_6, "nonterminal", 1)){
                                            IValue $arg0_7 = (IValue)($nonterminal_get_arg(((ITree)((ITree)($arg1_6))), ((IInteger)$constants.get(5)/*0*/).intValue()));
                                            if($isComparable($arg0_7.getType(), M_lang_rascal_syntax_Rascal.NT_Nonterminal)){
                                               ITree n_1 = ((ITree)($arg0_7));
                                               IValue $arg2_5 = (IValue)($nonterminal_get_arg(((ITree)((ITree)($switchVal2))), ((IInteger)$constants.get(7)/*2*/).intValue()));
                                               if($isComparable($arg2_5.getType(), M_lang_rascal_syntax_Rascal.NT_Prod)){
                                                  ITree p_2 = ((ITree)($arg2_5));
                                                  final Template $template4 = (Template)new Template($RVF, "");
                                                  $template4.addVal($arg0_7);
                                                  return ((ITuple)($RVF.tuple(((ISet)($RVF.set(((IConstructor)($me.prod2prod(((IConstructor)($RVF.constructor(M_ParseTree.Symbol_layouts_str, new IValue[]{((IString)($template4.close()))}))), ((ITree)($arg2_5)))))))), ((IConstructor)($RVF.constructor(M_util_Maybe.Maybe_1_nothing_, new IValue[]{}))))));
                                               
                                               }
                                            
                                            }
                                         
                                         }
                                      
                                      }
                                   
                                   }
                                
                                }
                        
                            } while(false);
                     
                     }
                     if($isSubtypeOf($switchVal2.getType(),M_lang_rascal_syntax_Rascal.NT_SyntaxDefinition)){
                        /*muExists*/CASE_0_1: 
                            do {
                                if($nonterminal_has_name_and_arity($switchVal2, "language", 3)){
                                   IValue $arg0_16 = (IValue)($nonterminal_get_arg(((ITree)((ITree)($switchVal2))), ((IInteger)$constants.get(5)/*0*/).intValue()));
                                   if($isComparable($arg0_16.getType(), M_lang_rascal_syntax_Rascal.NT_Start)){
                                      if($nonterminal_has_name_and_arity($arg0_16, "present", 0)){
                                         IValue $arg1_14 = (IValue)($nonterminal_get_arg(((ITree)((ITree)($switchVal2))), ((IInteger)$constants.get(6)/*1*/).intValue()));
                                         if($isComparable($arg1_14.getType(), M_lang_rascal_syntax_Rascal.NT_Sym)){
                                            if($nonterminal_has_name_and_arity($arg1_14, "nonterminal", 1)){
                                               IValue $arg0_15 = (IValue)($nonterminal_get_arg(((ITree)((ITree)($arg1_14))), ((IInteger)$constants.get(5)/*0*/).intValue()));
                                               if($isComparable($arg0_15.getType(), M_lang_rascal_syntax_Rascal.NT_Nonterminal)){
                                                  ITree n_3 = ((ITree)($arg0_15));
                                                  IValue $arg2_13 = (IValue)($nonterminal_get_arg(((ITree)((ITree)($switchVal2))), ((IInteger)$constants.get(7)/*2*/).intValue()));
                                                  if($isComparable($arg2_13.getType(), M_lang_rascal_syntax_Rascal.NT_Prod)){
                                                     ITree p_4 = ((ITree)($arg2_13));
                                                     final Template $template9 = (Template)new Template($RVF, "");
                                                     $template9.addVal($arg0_15);
                                                     final Template $template10 = (Template)new Template($RVF, "");
                                                     $template10.addVal($arg0_15);
                                                     final Template $template11 = (Template)new Template($RVF, "");
                                                     $template11.addVal($arg0_15);
                                                     final Template $template12 = (Template)new Template($RVF, "");
                                                     $template12.addVal($arg0_15);
                                                     return ((ITuple)($RVF.tuple(((ISet)($RVF.set(((IConstructor)($RVF.constructor(M_ParseTree.Production_prod_Symbol_list_Symbol_set_Attr, new IValue[]{((IConstructor)($RVF.constructor(M_ParseTree.Symbol_start_Symbol, new IValue[]{((IConstructor)($RVF.constructor(M_ParseTree.Symbol_sort_str, new IValue[]{((IString)($template9.close()))})))}))), ((IList)($RVF.list(((IConstructor)($RVF.constructor(M_Type.Symbol_label_str_Symbol, new IValue[]{((IString)$constants.get(8)/*"top"*/), ((IConstructor)($RVF.constructor(M_ParseTree.Symbol_sort_str, new IValue[]{((IString)($template10.close()))})))})))))), ((ISet)$constants.get(1)/*{}*/)}))), $me.prod2prod(((IConstructor)($RVF.constructor(M_ParseTree.Symbol_sort_str, new IValue[]{((IString)($template11.close()))}))), ((ITree)($arg2_13)))))), ((IConstructor)($RVF.constructor(Maybe_Symbol_just_Symbol, new IValue[]{((IConstructor)($RVF.constructor(M_ParseTree.Symbol_start_Symbol, new IValue[]{((IConstructor)($RVF.constructor(M_ParseTree.Symbol_sort_str, new IValue[]{((IString)($template12.close()))})))})))}))))));
                                                  
                                                  }
                                               
                                               }
                                            
                                            }
                                         
                                         }
                                      
                                      }
                                   
                                   }
                                
                                }
                        
                            } while(false);
                     
                     }
                     if($isSubtypeOf($switchVal2.getType(),M_lang_rascal_syntax_Rascal.NT_SyntaxDefinition)){
                        /*muExists*/CASE_0_2: 
                            do {
                                if($nonterminal_has_name_and_arity($switchVal2, "language", 3)){
                                   IValue $arg0_22 = (IValue)($nonterminal_get_arg(((ITree)((ITree)($switchVal2))), ((IInteger)$constants.get(5)/*0*/).intValue()));
                                   if($isComparable($arg0_22.getType(), M_lang_rascal_syntax_Rascal.NT_Start)){
                                      if($nonterminal_has_name_and_arity($arg0_22, "absent", 0)){
                                         IValue $arg1_19 = (IValue)($nonterminal_get_arg(((ITree)((ITree)($switchVal2))), ((IInteger)$constants.get(6)/*1*/).intValue()));
                                         if($isComparable($arg1_19.getType(), M_lang_rascal_syntax_Rascal.NT_Sym)){
                                            if($nonterminal_has_name_and_arity($arg1_19, "parametrized", 2)){
                                               IValue $arg0_21 = (IValue)($nonterminal_get_arg(((ITree)((ITree)($arg1_19))), ((IInteger)$constants.get(5)/*0*/).intValue()));
                                               if($isComparable($arg0_21.getType(), M_lang_rascal_syntax_Rascal.NT_Nonterminal)){
                                                  ITree l_5 = ((ITree)($arg0_21));
                                                  IValue $arg1_20 = (IValue)($nonterminal_get_arg(((ITree)((ITree)($arg1_19))), ((IInteger)$constants.get(6)/*1*/).intValue()));
                                                  if($isComparable($arg1_20.getType(), $T18)){
                                                     if(org.rascalmpl.values.parsetrees.TreeAdapter.getArgs((ITree)$arg1_20).length() >= 1){
                                                        ITree syms_6 = ((ITree)($arg1_20));
                                                        IValue $arg2_18 = (IValue)($nonterminal_get_arg(((ITree)((ITree)($switchVal2))), ((IInteger)$constants.get(7)/*2*/).intValue()));
                                                        if($isComparable($arg2_18.getType(), M_lang_rascal_syntax_Rascal.NT_Prod)){
                                                           ITree p_7 = ((ITree)($arg2_18));
                                                           final Template $template17 = (Template)new Template($RVF, "");
                                                           $template17.addVal($arg0_21);
                                                           return ((ITuple)($RVF.tuple(((ISet)($RVF.set(((IConstructor)($me.prod2prod(((IConstructor)($RVF.constructor(M_ParseTree.Symbol_parameterized_sort_str_list_Symbol, new IValue[]{((IString)($template17.close())), ((IList)(M_lang_rascal_grammar_definition_Symbols.separgs2symbols(((ITree)($arg1_20)))))}))), ((ITree)($arg2_18)))))))), ((IConstructor)($RVF.constructor(M_util_Maybe.Maybe_1_nothing_, new IValue[]{}))))));
                                                        
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
                     if($isSubtypeOf($switchVal2.getType(),M_lang_rascal_syntax_Rascal.NT_SyntaxDefinition)){
                        /*muExists*/CASE_0_3: 
                            do {
                                if($nonterminal_has_name_and_arity($switchVal2, "language", 3)){
                                   IValue $arg0_27 = (IValue)($nonterminal_get_arg(((ITree)((ITree)($switchVal2))), ((IInteger)$constants.get(5)/*0*/).intValue()));
                                   if($isComparable($arg0_27.getType(), M_lang_rascal_syntax_Rascal.NT_Start)){
                                      if($nonterminal_has_name_and_arity($arg0_27, "absent", 0)){
                                         IValue $arg1_25 = (IValue)($nonterminal_get_arg(((ITree)((ITree)($switchVal2))), ((IInteger)$constants.get(6)/*1*/).intValue()));
                                         if($isComparable($arg1_25.getType(), M_lang_rascal_syntax_Rascal.NT_Sym)){
                                            if($nonterminal_has_name_and_arity($arg1_25, "nonterminal", 1)){
                                               IValue $arg0_26 = (IValue)($nonterminal_get_arg(((ITree)((ITree)($arg1_25))), ((IInteger)$constants.get(5)/*0*/).intValue()));
                                               if($isComparable($arg0_26.getType(), M_lang_rascal_syntax_Rascal.NT_Nonterminal)){
                                                  ITree n_8 = ((ITree)($arg0_26));
                                                  IValue $arg2_24 = (IValue)($nonterminal_get_arg(((ITree)((ITree)($switchVal2))), ((IInteger)$constants.get(7)/*2*/).intValue()));
                                                  if($isComparable($arg2_24.getType(), M_lang_rascal_syntax_Rascal.NT_Prod)){
                                                     ITree p_9 = ((ITree)($arg2_24));
                                                     final Template $template23 = (Template)new Template($RVF, "");
                                                     $template23.addVal($arg0_26);
                                                     return ((ITuple)($RVF.tuple(((ISet)($RVF.set(((IConstructor)($me.prod2prod(((IConstructor)($RVF.constructor(M_ParseTree.Symbol_sort_str, new IValue[]{((IString)($template23.close()))}))), ((ITree)($arg2_24)))))))), ((IConstructor)($RVF.constructor(M_util_Maybe.Maybe_1_nothing_, new IValue[]{}))))));
                                                  
                                                  }
                                               
                                               }
                                            
                                            }
                                         
                                         }
                                      
                                      }
                                   
                                   }
                                
                                }
                        
                            } while(false);
                     
                     }
                     if($isSubtypeOf($switchVal2.getType(),M_lang_rascal_syntax_Rascal.NT_SyntaxDefinition)){
                        /*muExists*/CASE_0_4: 
                            do {
                                if($nonterminal_has_name_and_arity($switchVal2, "lexical", 2)){
                                   IValue $arg0_30 = (IValue)($nonterminal_get_arg(((ITree)((ITree)($switchVal2))), ((IInteger)$constants.get(5)/*0*/).intValue()));
                                   if($isComparable($arg0_30.getType(), M_lang_rascal_syntax_Rascal.NT_Sym)){
                                      if($nonterminal_has_name_and_arity($arg0_30, "parametrized", 2)){
                                         IValue $arg0_32 = (IValue)($nonterminal_get_arg(((ITree)((ITree)($arg0_30))), ((IInteger)$constants.get(5)/*0*/).intValue()));
                                         if($isComparable($arg0_32.getType(), M_lang_rascal_syntax_Rascal.NT_Nonterminal)){
                                            ITree l_10 = ((ITree)($arg0_32));
                                            IValue $arg1_31 = (IValue)($nonterminal_get_arg(((ITree)((ITree)($arg0_30))), ((IInteger)$constants.get(6)/*1*/).intValue()));
                                            if($isComparable($arg1_31.getType(), $T18)){
                                               if(org.rascalmpl.values.parsetrees.TreeAdapter.getArgs((ITree)$arg1_31).length() >= 1){
                                                  ITree syms_11 = ((ITree)($arg1_31));
                                                  IValue $arg1_29 = (IValue)($nonterminal_get_arg(((ITree)((ITree)($switchVal2))), ((IInteger)$constants.get(6)/*1*/).intValue()));
                                                  if($isComparable($arg1_29.getType(), M_lang_rascal_syntax_Rascal.NT_Prod)){
                                                     ITree p_12 = ((ITree)($arg1_29));
                                                     final Template $template28 = (Template)new Template($RVF, "");
                                                     $template28.addVal($arg0_32);
                                                     return ((ITuple)($RVF.tuple(((ISet)($RVF.set(((IConstructor)($me.prod2prod(((IConstructor)($RVF.constructor(M_ParseTree.Symbol_parameterized_lex_str_list_Symbol, new IValue[]{((IString)($template28.close())), ((IList)(M_lang_rascal_grammar_definition_Symbols.separgs2symbols(((ITree)($arg1_31)))))}))), ((ITree)($arg1_29)))))))), ((IConstructor)($RVF.constructor(M_util_Maybe.Maybe_1_nothing_, new IValue[]{}))))));
                                                  
                                                  }
                                               
                                               }
                                            
                                            }
                                         
                                         }
                                      
                                      }
                                   
                                   }
                                
                                }
                        
                            } while(false);
                     
                     }
                     if($isSubtypeOf($switchVal2.getType(),M_lang_rascal_syntax_Rascal.NT_SyntaxDefinition)){
                        /*muExists*/CASE_0_5: 
                            do {
                                if($nonterminal_has_name_and_arity($switchVal2, "lexical", 2)){
                                   IValue $arg0_35 = (IValue)($nonterminal_get_arg(((ITree)((ITree)($switchVal2))), ((IInteger)$constants.get(5)/*0*/).intValue()));
                                   if($isComparable($arg0_35.getType(), M_lang_rascal_syntax_Rascal.NT_Sym)){
                                      if($nonterminal_has_name_and_arity($arg0_35, "nonterminal", 1)){
                                         IValue $arg0_36 = (IValue)($nonterminal_get_arg(((ITree)((ITree)($arg0_35))), ((IInteger)$constants.get(5)/*0*/).intValue()));
                                         if($isComparable($arg0_36.getType(), M_lang_rascal_syntax_Rascal.NT_Nonterminal)){
                                            ITree n_13 = ((ITree)($arg0_36));
                                            IValue $arg1_34 = (IValue)($nonterminal_get_arg(((ITree)((ITree)($switchVal2))), ((IInteger)$constants.get(6)/*1*/).intValue()));
                                            if($isComparable($arg1_34.getType(), M_lang_rascal_syntax_Rascal.NT_Prod)){
                                               ITree p_14 = ((ITree)($arg1_34));
                                               final Template $template33 = (Template)new Template($RVF, "");
                                               $template33.addVal($arg0_36);
                                               return ((ITuple)($RVF.tuple(((ISet)($RVF.set(((IConstructor)($me.prod2prod(((IConstructor)($RVF.constructor(M_ParseTree.Symbol_lex_str, new IValue[]{((IString)($template33.close()))}))), ((ITree)($arg1_34)))))))), ((IConstructor)($RVF.constructor(M_util_Maybe.Maybe_1_nothing_, new IValue[]{}))))));
                                            
                                            }
                                         
                                         }
                                      
                                      }
                                   
                                   }
                                
                                }
                        
                            } while(false);
                     
                     }
                     if($isSubtypeOf($switchVal2.getType(),M_lang_rascal_syntax_Rascal.NT_SyntaxDefinition)){
                        /*muExists*/CASE_0_6: 
                            do {
                                if($nonterminal_has_name_and_arity($switchVal2, "keyword", 2)){
                                   IValue $arg0_39 = (IValue)($nonterminal_get_arg(((ITree)((ITree)($switchVal2))), ((IInteger)$constants.get(5)/*0*/).intValue()));
                                   if($isComparable($arg0_39.getType(), M_lang_rascal_syntax_Rascal.NT_Sym)){
                                      if($nonterminal_has_name_and_arity($arg0_39, "nonterminal", 1)){
                                         IValue $arg0_40 = (IValue)($nonterminal_get_arg(((ITree)((ITree)($arg0_39))), ((IInteger)$constants.get(5)/*0*/).intValue()));
                                         if($isComparable($arg0_40.getType(), M_lang_rascal_syntax_Rascal.NT_Nonterminal)){
                                            ITree n_15 = ((ITree)($arg0_40));
                                            IValue $arg1_38 = (IValue)($nonterminal_get_arg(((ITree)((ITree)($switchVal2))), ((IInteger)$constants.get(6)/*1*/).intValue()));
                                            if($isComparable($arg1_38.getType(), M_lang_rascal_syntax_Rascal.NT_Prod)){
                                               ITree p_16 = ((ITree)($arg1_38));
                                               final Template $template37 = (Template)new Template($RVF, "");
                                               $template37.addVal($arg0_40);
                                               return ((ITuple)($RVF.tuple(((ISet)($RVF.set(((IConstructor)($me.prod2prod(((IConstructor)($RVF.constructor(M_ParseTree.Symbol_keywords_str, new IValue[]{((IString)($template37.close()))}))), ((ITree)($arg1_38)))))))), ((IConstructor)($RVF.constructor(M_util_Maybe.Maybe_1_nothing_, new IValue[]{}))))));
                                            
                                            }
                                         
                                         }
                                      
                                      }
                                   
                                   }
                                
                                }
                        
                            } while(false);
                     
                     }
                     M_IO.iprintln(((IValue)sd_0), Util.kwpMap());
                     final Template $template3 = (Template)new Template($RVF, "unsupported kind of syntax definition? ");
                     $template3.beginIndent("                                       ");
                     $template3.addVal(sd_0);
                     $template3.endIndent("                                       ");
                     $template3.addStr(" at ");
                     $template3.beginIndent("    ");
                     $template3.addVal($annotation_get(((INode)sd_0),"src"));
                     $template3.endIndent("    ");
                     throw new Throw($template3.close());
        }
        
                   
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/lang/rascal/grammar/definition/Productions.rsc|(2377,1561,<60,0>,<89,1>) 
    public IConstructor lang_rascal_grammar_definition_Productions_prod2prod$70dc8b016f745552(IConstructor nt_0, ITree p_1){ 
        
        
        final ITree $switchVal41 = ((ITree)p_1);
        boolean noCaseMatched_$switchVal41 = true;
        SWITCH3: switch(Fingerprint.getFingerprint($switchVal41)){
        
            case 0:
                if(noCaseMatched_$switchVal41){
                    noCaseMatched_$switchVal41 = false;
                    
                }
                
        
            default: if($isSubtypeOf($switchVal41.getType(),M_lang_rascal_syntax_Rascal.NT_Prod)){
                        /*muExists*/CASE_0_0: 
                            do {
                                if($nonterminal_has_name_and_arity($switchVal41, "labeled", 3)){
                                   IValue $arg0_49 = (IValue)($nonterminal_get_arg(((ITree)((ITree)($switchVal41))), ((IInteger)$constants.get(5)/*0*/).intValue()));
                                   if($isComparable($arg0_49.getType(), $T19)){
                                      ITree ms_2 = ((ITree)($arg0_49));
                                      IValue $arg1_48 = (IValue)($nonterminal_get_arg(((ITree)((ITree)($switchVal41))), ((IInteger)$constants.get(6)/*1*/).intValue()));
                                      if($isComparable($arg1_48.getType(), M_lang_rascal_syntax_Rascal.NT_Name)){
                                         ITree n_3 = ((ITree)($arg1_48));
                                         IValue $arg2_47 = (IValue)($nonterminal_get_arg(((ITree)((ITree)($switchVal41))), ((IInteger)$constants.get(7)/*2*/).intValue()));
                                         if($isComparable($arg2_47.getType(), $T20)){
                                            ITree args_4 = ((ITree)($arg2_47));
                                            /*muExists*/IF4: 
                                                do {
                                                    final IList $subject_val44 = ((IList)(((IList)($aadt_get_field(((ITree)($arg2_47)), "args")))));
                                                    final IList $subject45 = ((IList)($subject_val44));
                                                    int $subject45_cursor = 0;
                                                    if($isSubtypeOf($subject45.getType(),$T21)){
                                                       final int $subject45_len = (int)((IList)($subject45)).length();
                                                       if($subject45_len == 1){
                                                          if($subject45_cursor < $subject45_len && $isComparable($alist_subscript_int(((IList)($subject45)),$subject45_cursor).getType(), M_lang_rascal_syntax_Rascal.NT_Sym)){
                                                             ITree x_5 = ((ITree)($alist_subscript_int(((IList)($subject45)),$subject45_cursor)));
                                                             $subject45_cursor += 1;
                                                             if($subject45_cursor == $subject45_len){
                                                                if($is(((ITree)x_5),((IString)$constants.get(9)/*"empty"*/))){
                                                                   final Template $template43 = (Template)new Template($RVF, "");
                                                                   $template43.addVal($arg1_48);
                                                                   return ((IConstructor)($me.associativity(((IConstructor)nt_0), ((IConstructor)(M_lang_rascal_grammar_definition_Attributes.mods2assoc(((ITree)($arg0_49))))), ((IConstructor)($RVF.constructor(M_ParseTree.Production_prod_Symbol_list_Symbol_set_Attr, new IValue[]{((IConstructor)($RVF.constructor(M_Type.Symbol_label_str_Symbol, new IValue[]{((IString)(M_lang_rascal_grammar_definition_Names.unescape(((IString)($template43.close()))))), ((IConstructor)nt_0)}))), ((IList)$constants.get(0)/*[]*/), ((ISet)(M_lang_rascal_grammar_definition_Attributes.mods2attrs(((ITree)($arg0_49)))))}))))));
                                                                
                                                                } else {
                                                                   continue IF4;
                                                                }
                                                             } else {
                                                                continue IF4;/*list match1*/
                                                             }
                                                          }
                                                       
                                                       }
                                                    
                                                    }
                                            
                                                } while(false);
                                            final Template $template46 = (Template)new Template($RVF, "");
                                            $template46.addVal($arg1_48);
                                            return ((IConstructor)($me.associativity(((IConstructor)nt_0), ((IConstructor)(M_lang_rascal_grammar_definition_Attributes.mods2assoc(((ITree)($arg0_49))))), ((IConstructor)($RVF.constructor(M_ParseTree.Production_prod_Symbol_list_Symbol_set_Attr, new IValue[]{((IConstructor)($RVF.constructor(M_Type.Symbol_label_str_Symbol, new IValue[]{((IString)(M_lang_rascal_grammar_definition_Names.unescape(((IString)($template46.close()))))), ((IConstructor)nt_0)}))), ((IList)(M_lang_rascal_grammar_definition_Symbols.args2symbols(((ITree)($arg2_47))))), ((ISet)(M_lang_rascal_grammar_definition_Attributes.mods2attrs(((ITree)($arg0_49)))))}))))));
                                         
                                         }
                                      
                                      }
                                   
                                   }
                                
                                }
                        
                            } while(false);
                     
                     }
                     if($isSubtypeOf($switchVal41.getType(),M_lang_rascal_syntax_Rascal.NT_Prod)){
                        /*muExists*/CASE_0_1: 
                            do {
                                if($nonterminal_has_name_and_arity($switchVal41, "unlabeled", 2)){
                                   IValue $arg0_53 = (IValue)($nonterminal_get_arg(((ITree)((ITree)($switchVal41))), ((IInteger)$constants.get(5)/*0*/).intValue()));
                                   if($isComparable($arg0_53.getType(), $T19)){
                                      ITree ms_6 = ((ITree)($arg0_53));
                                      IValue $arg1_52 = (IValue)($nonterminal_get_arg(((ITree)((ITree)($switchVal41))), ((IInteger)$constants.get(6)/*1*/).intValue()));
                                      if($isComparable($arg1_52.getType(), $T20)){
                                         ITree args_7 = ((ITree)($arg1_52));
                                         /*muExists*/IF5: 
                                             do {
                                                 final IList $subject_val50 = ((IList)(((IList)($aadt_get_field(((ITree)($arg1_52)), "args")))));
                                                 final IList $subject51 = ((IList)($subject_val50));
                                                 int $subject51_cursor = 0;
                                                 if($isSubtypeOf($subject51.getType(),$T21)){
                                                    final int $subject51_len = (int)((IList)($subject51)).length();
                                                    if($subject51_len == 1){
                                                       if($subject51_cursor < $subject51_len && $isComparable($alist_subscript_int(((IList)($subject51)),$subject51_cursor).getType(), M_lang_rascal_syntax_Rascal.NT_Sym)){
                                                          ITree x_8 = ((ITree)($alist_subscript_int(((IList)($subject51)),$subject51_cursor)));
                                                          $subject51_cursor += 1;
                                                          if($subject51_cursor == $subject51_len){
                                                             if($is(((ITree)x_8),((IString)$constants.get(9)/*"empty"*/))){
                                                                return ((IConstructor)($me.associativity(((IConstructor)nt_0), ((IConstructor)(M_lang_rascal_grammar_definition_Attributes.mods2assoc(((ITree)($arg0_53))))), ((IConstructor)($RVF.constructor(M_ParseTree.Production_prod_Symbol_list_Symbol_set_Attr, new IValue[]{((IConstructor)nt_0), ((IList)$constants.get(0)/*[]*/), ((ISet)(M_lang_rascal_grammar_definition_Attributes.mods2attrs(((ITree)($arg0_53)))))}))))));
                                                             
                                                             } else {
                                                                continue IF5;
                                                             }
                                                          } else {
                                                             continue IF5;/*list match1*/
                                                          }
                                                       }
                                                    
                                                    }
                                                 
                                                 }
                                         
                                             } while(false);
                                         return ((IConstructor)($me.associativity(((IConstructor)nt_0), ((IConstructor)(M_lang_rascal_grammar_definition_Attributes.mods2assoc(((ITree)($arg0_53))))), ((IConstructor)($RVF.constructor(M_ParseTree.Production_prod_Symbol_list_Symbol_set_Attr, new IValue[]{((IConstructor)nt_0), ((IList)(M_lang_rascal_grammar_definition_Symbols.args2symbols(((ITree)($arg1_52))))), ((ISet)(M_lang_rascal_grammar_definition_Attributes.mods2attrs(((ITree)($arg0_53)))))}))))));
                                      
                                      }
                                   
                                   }
                                
                                }
                        
                            } while(false);
                     
                     }
                     if($isSubtypeOf($switchVal41.getType(),M_lang_rascal_syntax_Rascal.NT_Prod)){
                        /*muExists*/CASE_0_2: 
                            do {
                                if($nonterminal_has_name_and_arity($switchVal41, "all", 2)){
                                   IValue $arg0_55 = (IValue)($nonterminal_get_arg(((ITree)((ITree)($switchVal41))), ((IInteger)$constants.get(5)/*0*/).intValue()));
                                   if($isComparable($arg0_55.getType(), M_lang_rascal_syntax_Rascal.NT_Prod)){
                                      ITree l_9 = ((ITree)($arg0_55));
                                      IValue $arg1_54 = (IValue)($nonterminal_get_arg(((ITree)((ITree)($switchVal41))), ((IInteger)$constants.get(6)/*1*/).intValue()));
                                      if($isComparable($arg1_54.getType(), M_lang_rascal_syntax_Rascal.NT_Prod)){
                                         ITree r_10 = ((ITree)($arg1_54));
                                         return ((IConstructor)($me.choice(((IConstructor)nt_0), ((ISet)($RVF.set(((IConstructor)($me.prod2prod(((IConstructor)nt_0), ((ITree)($arg0_55))))), $me.prod2prod(((IConstructor)nt_0), ((ITree)($arg1_54)))))))));
                                      
                                      }
                                   
                                   }
                                
                                }
                        
                            } while(false);
                     
                     }
                     if($isSubtypeOf($switchVal41.getType(),M_lang_rascal_syntax_Rascal.NT_Prod)){
                        /*muExists*/CASE_0_3: 
                            do {
                                if($nonterminal_has_name_and_arity($switchVal41, "first", 2)){
                                   IValue $arg0_57 = (IValue)($nonterminal_get_arg(((ITree)((ITree)($switchVal41))), ((IInteger)$constants.get(5)/*0*/).intValue()));
                                   if($isComparable($arg0_57.getType(), M_lang_rascal_syntax_Rascal.NT_Prod)){
                                      ITree l_11 = ((ITree)($arg0_57));
                                      IValue $arg1_56 = (IValue)($nonterminal_get_arg(((ITree)((ITree)($switchVal41))), ((IInteger)$constants.get(6)/*1*/).intValue()));
                                      if($isComparable($arg1_56.getType(), M_lang_rascal_syntax_Rascal.NT_Prod)){
                                         ITree r_12 = ((ITree)($arg1_56));
                                         return ((IConstructor)($me.priority(((IConstructor)nt_0), ((IList)($RVF.list(((IConstructor)($me.prod2prod(((IConstructor)nt_0), ((ITree)($arg0_57))))), $me.prod2prod(((IConstructor)nt_0), ((ITree)($arg1_56)))))))));
                                      
                                      }
                                   
                                   }
                                
                                }
                        
                            } while(false);
                     
                     }
                     if($isSubtypeOf($switchVal41.getType(),M_lang_rascal_syntax_Rascal.NT_Prod)){
                        /*muExists*/CASE_0_4: 
                            do {
                                if($nonterminal_has_name_and_arity($switchVal41, "associativityGroup", 2)){
                                   IValue $arg0_59 = (IValue)($nonterminal_get_arg(((ITree)((ITree)($switchVal41))), ((IInteger)$constants.get(5)/*0*/).intValue()));
                                   if($isComparable($arg0_59.getType(), M_lang_rascal_syntax_Rascal.NT_Assoc)){
                                      if($nonterminal_has_name_and_arity($arg0_59, "left", 0)){
                                         IValue $arg1_58 = (IValue)($nonterminal_get_arg(((ITree)((ITree)($switchVal41))), ((IInteger)$constants.get(6)/*1*/).intValue()));
                                         if($isComparable($arg1_58.getType(), M_lang_rascal_syntax_Rascal.NT_Prod)){
                                            ITree q_13 = ((ITree)($arg1_58));
                                            return ((IConstructor)($me.associativity(((IConstructor)nt_0), ((IConstructor)($RVF.constructor(M_ParseTree.Associativity_left_, new IValue[]{}))), ((ISet)($RVF.set(((IConstructor)($me.prod2prod(((IConstructor)nt_0), ((ITree)($arg1_58)))))))))));
                                         
                                         }
                                      
                                      }
                                   
                                   }
                                
                                }
                        
                            } while(false);
                     
                     }
                     if($isSubtypeOf($switchVal41.getType(),M_lang_rascal_syntax_Rascal.NT_Prod)){
                        /*muExists*/CASE_0_5: 
                            do {
                                if($nonterminal_has_name_and_arity($switchVal41, "associativityGroup", 2)){
                                   IValue $arg0_61 = (IValue)($nonterminal_get_arg(((ITree)((ITree)($switchVal41))), ((IInteger)$constants.get(5)/*0*/).intValue()));
                                   if($isComparable($arg0_61.getType(), M_lang_rascal_syntax_Rascal.NT_Assoc)){
                                      if($nonterminal_has_name_and_arity($arg0_61, "right", 0)){
                                         IValue $arg1_60 = (IValue)($nonterminal_get_arg(((ITree)((ITree)($switchVal41))), ((IInteger)$constants.get(6)/*1*/).intValue()));
                                         if($isComparable($arg1_60.getType(), M_lang_rascal_syntax_Rascal.NT_Prod)){
                                            ITree q_14 = ((ITree)($arg1_60));
                                            return ((IConstructor)($me.associativity(((IConstructor)nt_0), ((IConstructor)($RVF.constructor(M_ParseTree.Associativity_right_, new IValue[]{}))), ((ISet)($RVF.set(((IConstructor)($me.prod2prod(((IConstructor)nt_0), ((ITree)($arg1_60)))))))))));
                                         
                                         }
                                      
                                      }
                                   
                                   }
                                
                                }
                        
                            } while(false);
                     
                     }
                     if($isSubtypeOf($switchVal41.getType(),M_lang_rascal_syntax_Rascal.NT_Prod)){
                        /*muExists*/CASE_0_6: 
                            do {
                                if($nonterminal_has_name_and_arity($switchVal41, "associativityGroup", 2)){
                                   IValue $arg0_63 = (IValue)($nonterminal_get_arg(((ITree)((ITree)($switchVal41))), ((IInteger)$constants.get(5)/*0*/).intValue()));
                                   if($isComparable($arg0_63.getType(), M_lang_rascal_syntax_Rascal.NT_Assoc)){
                                      if($nonterminal_has_name_and_arity($arg0_63, "nonAssociative", 0)){
                                         IValue $arg1_62 = (IValue)($nonterminal_get_arg(((ITree)((ITree)($switchVal41))), ((IInteger)$constants.get(6)/*1*/).intValue()));
                                         if($isComparable($arg1_62.getType(), M_lang_rascal_syntax_Rascal.NT_Prod)){
                                            ITree q_15 = ((ITree)($arg1_62));
                                            return ((IConstructor)($me.associativity(((IConstructor)nt_0), ((INode)$constants.get(10)/*\non-assoc()*/), ((ISet)($RVF.set(((IConstructor)($me.prod2prod(((IConstructor)nt_0), ((ITree)($arg1_62)))))))))));
                                         
                                         }
                                      
                                      }
                                   
                                   }
                                
                                }
                        
                            } while(false);
                     
                     }
                     if($isSubtypeOf($switchVal41.getType(),M_lang_rascal_syntax_Rascal.NT_Prod)){
                        /*muExists*/CASE_0_7: 
                            do {
                                if($nonterminal_has_name_and_arity($switchVal41, "associativityGroup", 2)){
                                   IValue $arg0_65 = (IValue)($nonterminal_get_arg(((ITree)((ITree)($switchVal41))), ((IInteger)$constants.get(5)/*0*/).intValue()));
                                   if($isComparable($arg0_65.getType(), M_lang_rascal_syntax_Rascal.NT_Assoc)){
                                      if($nonterminal_has_name_and_arity($arg0_65, "associative", 0)){
                                         IValue $arg1_64 = (IValue)($nonterminal_get_arg(((ITree)((ITree)($switchVal41))), ((IInteger)$constants.get(6)/*1*/).intValue()));
                                         if($isComparable($arg1_64.getType(), M_lang_rascal_syntax_Rascal.NT_Prod)){
                                            ITree q_16 = ((ITree)($arg1_64));
                                            return ((IConstructor)($me.associativity(((IConstructor)nt_0), ((IConstructor)($RVF.constructor(M_ParseTree.Associativity_left_, new IValue[]{}))), ((ISet)($RVF.set(((IConstructor)($me.prod2prod(((IConstructor)nt_0), ((ITree)($arg1_64)))))))))));
                                         
                                         }
                                      
                                      }
                                   
                                   }
                                
                                }
                        
                            } while(false);
                     
                     }
                     if($isSubtypeOf($switchVal41.getType(),M_lang_rascal_syntax_Rascal.NT_Prod)){
                        /*muExists*/CASE_0_8: 
                            do {
                                if($nonterminal_has_name_and_arity($switchVal41, "reference", 1)){
                                   IValue $arg0_67 = (IValue)($nonterminal_get_arg(((ITree)((ITree)($switchVal41))), ((IInteger)$constants.get(5)/*0*/).intValue()));
                                   if($isComparable($arg0_67.getType(), M_lang_rascal_syntax_Rascal.NT_Name)){
                                      ITree n_17 = ((ITree)($arg0_67));
                                      final Template $template66 = (Template)new Template($RVF, "");
                                      $template66.addVal($arg0_67);
                                      return ((IConstructor)($RVF.constructor(M_ParseTree.Production_reference_Symbol_str, new IValue[]{((IConstructor)nt_0), ((IString)(M_lang_rascal_grammar_definition_Names.unescape(((IString)($template66.close())))))})));
                                   
                                   }
                                
                                }
                        
                            } while(false);
                     
                     }
                     final Template $template42 = (Template)new Template($RVF, "prod2prod, missed a case ");
                     $template42.beginIndent("                         ");
                     $template42.addVal(p_1);
                     $template42.endIndent("                         ");
                     throw new Throw($template42.close());
        }
        
                   
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/lang/rascal/grammar/definition/Productions.rsc|(3942,73,<93,0>,<93,73>) 
    public IConstructor lang_rascal_grammar_definition_Productions_associativity$09cd814bba935894(IConstructor nt_0, IConstructor $1, IConstructor p_1){ 
        
        
        HashMap<io.usethesource.vallang.type.Type,io.usethesource.vallang.type.Type> $typeBindings = new HashMap<>();
        if(M_util_Maybe.ADT_Maybe_1.match($1.getType(), $typeBindings)){
           if($has_type_and_arity($1, M_util_Maybe.Maybe_1_nothing_, 0)){
              final IConstructor $result68 = ((IConstructor)p_1);
              if(M_ParseTree.ADT_Production.instantiate($typeBindings) != $TF.voidType() && $isSubtypeOf($result68.getType(),M_ParseTree.ADT_Production)){
                 return ((IConstructor)($result68));
              
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
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/lang/rascal/grammar/definition/Productions.rsc|(4016,117,<94,0>,<94,117>) 
    public IConstructor lang_rascal_grammar_definition_Productions_associativity$fe1234ba22a8be5e(IConstructor nt_0, IConstructor $1, IConstructor p_2){ 
        
        
        if($has_type_and_arity($1, M_util_Maybe.Maybe_1_just_, 1)){
           IValue $arg0_69 = (IValue)($aadt_subscript_int(((IConstructor)$1),0));
           if($isComparable($arg0_69.getType(), M_ParseTree.ADT_Associativity)){
              IConstructor a_1 = null;
              return ((IConstructor)($me.associativity(((IConstructor)nt_0), ((IConstructor)($arg0_69)), ((ISet)($RVF.set(((IConstructor)p_2)))))));
           
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    

    public static void main(String[] args) {
      throw new RuntimeException("No function `main` found in Rascal module `lang::rascal::grammar::definition::Productions`");
    }
}