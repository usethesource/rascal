package rascal;
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
public class $Exception 
    extends
        org.rascalmpl.runtime.$RascalModule
    implements 
    	rascal.$Exception_$I {

    private final $Exception_$I $me;
    private final IList $constants;
    
    

    
    
    public final io.usethesource.vallang.type.Type $T6;	/*avalue(alabel="second")*/
    public final io.usethesource.vallang.type.Type $T1;	/*astr(alabel="message")*/
    public final io.usethesource.vallang.type.Type $T12;	/*astr(alabel="label")*/
    public final io.usethesource.vallang.type.Type $T8;	/*astr(alabel="class")*/
    public final io.usethesource.vallang.type.Type $T15;	/*astr(alabel="nonterminal")*/
    public final io.usethesource.vallang.type.Type $T18;	/*aint(alabel="column")*/
    public final io.usethesource.vallang.type.Type $T9;	/*aint(alabel="index")*/
    public final io.usethesource.vallang.type.Type $T13;	/*aloc()*/
    public final io.usethesource.vallang.type.Type $T5;	/*avalue(alabel="first")*/
    public final io.usethesource.vallang.type.Type $T16;	/*astr(alabel="sentence")*/
    public final io.usethesource.vallang.type.Type $T19;	/*astr(alabel="source")*/
    public final io.usethesource.vallang.type.Type $T0;	/*aloc(alabel="location")*/
    public final io.usethesource.vallang.type.Type $T10;	/*avalue()*/
    public final io.usethesource.vallang.type.Type $T3;	/*astr(alabel="uri")*/
    public final io.usethesource.vallang.type.Type $T21;	/*astr(alabel="cls")*/
    public final io.usethesource.vallang.type.Type $T17;	/*aint(alabel="line")*/
    public final io.usethesource.vallang.type.Type $T2;	/*avalue(alabel="v")*/
    public final io.usethesource.vallang.type.Type $T4;	/*avalue(alabel="key")*/
    public final io.usethesource.vallang.type.Type $T7;	/*astr(alabel="name")*/
    public final io.usethesource.vallang.type.Type ADT_RuntimeException;	/*aadt("RuntimeException",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type $T11;	/*alist(avalue(),alabel="arguments")*/
    public final io.usethesource.vallang.type.Type RuntimeException_IllegalTypeArgument_str_str;	/*acons(aadt("RuntimeException",[],dataSyntax()),[astr(alabel="cls"),astr(alabel="message")],[],alabel="IllegalTypeArgument")*/
    public final io.usethesource.vallang.type.Type RuntimeException_Timeout_;	/*acons(aadt("RuntimeException",[],dataSyntax()),[],[],alabel="Timeout")*/
    public final io.usethesource.vallang.type.Type RuntimeException_NoSuchAnnotation_str;	/*acons(aadt("RuntimeException",[],dataSyntax()),[astr(alabel="label")],[],alabel="NoSuchAnnotation")*/
    public final io.usethesource.vallang.type.Type RuntimeException_EmptyList_;	/*acons(aadt("RuntimeException",[],dataSyntax()),[],[],alabel="EmptyList")*/
    public final io.usethesource.vallang.type.Type RuntimeException_NoParent_loc;	/*acons(aadt("RuntimeException",[],dataSyntax()),[aloc(alabel="location")],[],alabel="NoParent")*/
    public final io.usethesource.vallang.type.Type RuntimeException_Java_str_str;	/*acons(aadt("RuntimeException",[],dataSyntax()),[astr(alabel="class"),astr(alabel="message")],[],alabel="Java")*/
    public final io.usethesource.vallang.type.Type RuntimeException_MultipleKey_value_value_value;	/*acons(aadt("RuntimeException",[],dataSyntax()),[avalue(alabel="key"),avalue(alabel="first"),avalue(alabel="second")],[],alabel="MultipleKey")*/
    public final io.usethesource.vallang.type.Type RuntimeException_InvalidURI_str;	/*acons(aadt("RuntimeException",[],dataSyntax()),[astr(alabel="uri")],[],alabel="InvalidURI")*/
    public final io.usethesource.vallang.type.Type RuntimeException_NoMainFunction_;	/*acons(aadt("RuntimeException",[],dataSyntax()),[],[],alabel="NoMainFunction")*/
    public final io.usethesource.vallang.type.Type RuntimeException_StackOverflow_;	/*acons(aadt("RuntimeException",[],dataSyntax()),[],[],alabel="StackOverflow")*/
    public final io.usethesource.vallang.type.Type RuntimeException_MalFormedURI_str;	/*acons(aadt("RuntimeException",[],dataSyntax()),[astr(alabel="uri")],[],alabel="MalFormedURI")*/
    public final io.usethesource.vallang.type.Type RuntimeException_NotImplemented_str;	/*acons(aadt("RuntimeException",[],dataSyntax()),[astr(alabel="message")],[],alabel="NotImplemented")*/
    public final io.usethesource.vallang.type.Type RuntimeException_EmptySet_;	/*acons(aadt("RuntimeException",[],dataSyntax()),[],[],alabel="EmptySet")*/
    public final io.usethesource.vallang.type.Type RuntimeException_IllegalArgument_;	/*acons(aadt("RuntimeException",[],dataSyntax()),[],[],alabel="IllegalArgument")*/
    public final io.usethesource.vallang.type.Type RuntimeException_RegExpSyntaxError_str;	/*acons(aadt("RuntimeException",[],dataSyntax()),[astr(alabel="message")],[],alabel="RegExpSyntaxError")*/
    public final io.usethesource.vallang.type.Type RuntimeException_IO_str;	/*acons(aadt("RuntimeException",[],dataSyntax()),[astr(alabel="message")],[],alabel="IO")*/
    public final io.usethesource.vallang.type.Type RuntimeException_InvalidUseOfLocation_str;	/*acons(aadt("RuntimeException",[],dataSyntax()),[astr(alabel="message")],[],alabel="InvalidUseOfLocation")*/
    public final io.usethesource.vallang.type.Type RuntimeException_PermissionDenied_;	/*acons(aadt("RuntimeException",[],dataSyntax()),[],[],alabel="PermissionDenied")*/
    public final io.usethesource.vallang.type.Type $T20;	/*alist(aloc(),alabel="classpath")*/
    public final io.usethesource.vallang.type.Type RuntimeException_JavaCompilation_str_int_int_str_list_loc;	/*acons(aadt("RuntimeException",[],dataSyntax()),[astr(alabel="message"),aint(alabel="line"),aint(alabel="column"),astr(alabel="source"),alist(aloc(),alabel="classpath")],[],alabel="JavaCompilation")*/
    public final io.usethesource.vallang.type.Type RuntimeException_ArithmeticException_str;	/*acons(aadt("RuntimeException",[],dataSyntax()),[astr(alabel="message")],[],alabel="ArithmeticException")*/
    public final io.usethesource.vallang.type.Type RuntimeException_NoSuchElement_value;	/*acons(aadt("RuntimeException",[],dataSyntax()),[avalue(alabel="v")],[],alabel="NoSuchElement")*/
    public final io.usethesource.vallang.type.Type RuntimeException_IndexOutOfBounds_int;	/*acons(aadt("RuntimeException",[],dataSyntax()),[aint(alabel="index")],[],alabel="IndexOutOfBounds")*/
    public final io.usethesource.vallang.type.Type RuntimeException_CallFailed_list_value;	/*acons(aadt("RuntimeException",[],dataSyntax()),[alist(avalue(),alabel="arguments")],[],alabel="CallFailed")*/
    public final io.usethesource.vallang.type.Type RuntimeException_ParseError_loc;	/*acons(aadt("RuntimeException",[],dataSyntax()),[aloc(alabel="location")],[],alabel="ParseError")*/
    public final io.usethesource.vallang.type.Type RuntimeException_ImplodeError_str;	/*acons(aadt("RuntimeException",[],dataSyntax()),[astr(alabel="message")],[],alabel="ImplodeError")*/
    public final io.usethesource.vallang.type.Type RuntimeException_NoSuchKey_value;	/*acons(aadt("RuntimeException",[],dataSyntax()),[avalue(alabel="key")],[],alabel="NoSuchKey")*/
    public final io.usethesource.vallang.type.Type RuntimeException_JavaException_str_str;	/*acons(aadt("RuntimeException",[],dataSyntax()),[astr(alabel="class"),astr(alabel="message")],[],alabel="JavaException")*/
    public final io.usethesource.vallang.type.Type RuntimeException_DateTimePrintingError_str;	/*acons(aadt("RuntimeException",[],dataSyntax()),[astr(alabel="message")],[],alabel="DateTimePrintingError")*/
    public final io.usethesource.vallang.type.Type RuntimeException_AssertionFailed_;	/*acons(aadt("RuntimeException",[],dataSyntax()),[],[],alabel="AssertionFailed")*/
    public final io.usethesource.vallang.type.Type RuntimeException_SchemeNotSupported_loc;	/*acons(aadt("RuntimeException",[],dataSyntax()),[aloc(alabel="location")],[],alabel="SchemeNotSupported")*/
    public final io.usethesource.vallang.type.Type RuntimeException_NoSuchField_str;	/*acons(aadt("RuntimeException",[],dataSyntax()),[astr(alabel="name")],[],alabel="NoSuchField")*/
    public final io.usethesource.vallang.type.Type RuntimeException_InvalidUseOfDateTime_str;	/*acons(aadt("RuntimeException",[],dataSyntax()),[astr(alabel="message")],[],alabel="InvalidUseOfDateTime")*/
    public final io.usethesource.vallang.type.Type RuntimeException_UnavailableInformation_;	/*acons(aadt("RuntimeException",[],dataSyntax()),[],[],alabel="UnavailableInformation")*/
    public final io.usethesource.vallang.type.Type $T14;	/*aset(aloc(),alabel="locs")*/
    public final io.usethesource.vallang.type.Type RuntimeException_Ambiguity_loc_str_str;	/*acons(aadt("RuntimeException",[],dataSyntax()),[aloc(alabel="location"),astr(alabel="nonterminal"),astr(alabel="sentence")],[],alabel="Ambiguity")*/
    public final io.usethesource.vallang.type.Type RuntimeException_DateTimeParsingError_str;	/*acons(aadt("RuntimeException",[],dataSyntax()),[astr(alabel="message")],[],alabel="DateTimeParsingError")*/
    public final io.usethesource.vallang.type.Type RuntimeException_PermissionDenied_str;	/*acons(aadt("RuntimeException",[],dataSyntax()),[astr(alabel="message")],[],alabel="PermissionDenied")*/
    public final io.usethesource.vallang.type.Type RuntimeException_InvalidUseOfTime_str;	/*acons(aadt("RuntimeException",[],dataSyntax()),[astr(alabel="message")],[],alabel="InvalidUseOfTime")*/
    public final io.usethesource.vallang.type.Type RuntimeException_IllegalArgument_value_str;	/*acons(aadt("RuntimeException",[],dataSyntax()),[avalue(alabel="v"),astr(alabel="message")],[],alabel="IllegalArgument")*/
    public final io.usethesource.vallang.type.Type RuntimeException_InvalidUseOfDate_str;	/*acons(aadt("RuntimeException",[],dataSyntax()),[astr(alabel="message")],[],alabel="InvalidUseOfDate")*/
    public final io.usethesource.vallang.type.Type RuntimeException_IllegalArgument_value;	/*acons(aadt("RuntimeException",[],dataSyntax()),[avalue(alabel="v")],[],alabel="IllegalArgument")*/
    public final io.usethesource.vallang.type.Type RuntimeException_PathNotFound_loc;	/*acons(aadt("RuntimeException",[],dataSyntax()),[aloc(alabel="location")],[],alabel="PathNotFound")*/
    public final io.usethesource.vallang.type.Type RuntimeException_AssertionFailed_str;	/*acons(aadt("RuntimeException",[],dataSyntax()),[astr(alabel="label")],[],alabel="AssertionFailed")*/
    public final io.usethesource.vallang.type.Type RuntimeException_ModuleNotFound_str;	/*acons(aadt("RuntimeException",[],dataSyntax()),[astr(alabel="name")],[],alabel="ModuleNotFound")*/
    public final io.usethesource.vallang.type.Type RuntimeException_PathNotFound_set_loc;	/*acons(aadt("RuntimeException",[],dataSyntax()),[aset(aloc(),alabel="locs")],[],alabel="PathNotFound")*/
    public final io.usethesource.vallang.type.Type RuntimeException_JavaException_str_str_RuntimeException;	/*acons(aadt("RuntimeException",[],dataSyntax()),[astr(alabel="class"),astr(alabel="message"),aadt("RuntimeException",[],dataSyntax(),alabel="cause")],[],alabel="JavaException")*/
    public final io.usethesource.vallang.type.Type RuntimeException_EmptyMap_;	/*acons(aadt("RuntimeException",[],dataSyntax()),[],[],alabel="EmptyMap")*/
    public final io.usethesource.vallang.type.Type RuntimeException_Java_str_str_RuntimeException;	/*acons(aadt("RuntimeException",[],dataSyntax()),[astr(alabel="class"),astr(alabel="message"),aadt("RuntimeException",[],dataSyntax(),alabel="cause")],[],alabel="Java")*/

    public $Exception(RascalExecutionContext rex){
        this(rex, null);
    }
    
    public $Exception(RascalExecutionContext rex, Object extended){
       super(rex);
       this.$me = extended == null ? this : ($Exception_$I)extended;
       ModuleStore mstore = rex.getModuleStore();
       mstore.put(rascal.$Exception.class, this);
        
        
       
                          
       
       
       $constants = readBinaryConstantsFile(this.getClass(), "rascal/$Exception.constants", 0, "d751713988987e9331980363e24189ce");
       ADT_RuntimeException = $adt("RuntimeException");
       $T6 = $TF.valueType();
       $T1 = $TF.stringType();
       $T12 = $TF.stringType();
       $T8 = $TF.stringType();
       $T15 = $TF.stringType();
       $T18 = $TF.integerType();
       $T9 = $TF.integerType();
       $T13 = $TF.sourceLocationType();
       $T5 = $TF.valueType();
       $T16 = $TF.stringType();
       $T19 = $TF.stringType();
       $T0 = $TF.sourceLocationType();
       $T10 = $TF.valueType();
       $T3 = $TF.stringType();
       $T21 = $TF.stringType();
       $T17 = $TF.integerType();
       $T2 = $TF.valueType();
       $T4 = $TF.valueType();
       $T7 = $TF.stringType();
       $T11 = $TF.listType($T10);
       $T20 = $TF.listType($T13);
       $T14 = $TF.setType($T13);
       RuntimeException_IllegalTypeArgument_str_str = $TF.constructor($TS, ADT_RuntimeException, "IllegalTypeArgument", $TF.stringType(), "cls", $TF.stringType(), "message");
       RuntimeException_Timeout_ = $TF.constructor($TS, ADT_RuntimeException, "Timeout");
       RuntimeException_NoSuchAnnotation_str = $TF.constructor($TS, ADT_RuntimeException, "NoSuchAnnotation", $TF.stringType(), "label");
       RuntimeException_EmptyList_ = $TF.constructor($TS, ADT_RuntimeException, "EmptyList");
       RuntimeException_NoParent_loc = $TF.constructor($TS, ADT_RuntimeException, "NoParent", $TF.sourceLocationType(), "location");
       RuntimeException_Java_str_str = $TF.constructor($TS, ADT_RuntimeException, "Java", $TF.stringType(), "class", $TF.stringType(), "message");
       RuntimeException_MultipleKey_value_value_value = $TF.constructor($TS, ADT_RuntimeException, "MultipleKey", $TF.valueType(), "key", $TF.valueType(), "first", $TF.valueType(), "second");
       RuntimeException_InvalidURI_str = $TF.constructor($TS, ADT_RuntimeException, "InvalidURI", $TF.stringType(), "uri");
       RuntimeException_NoMainFunction_ = $TF.constructor($TS, ADT_RuntimeException, "NoMainFunction");
       RuntimeException_StackOverflow_ = $TF.constructor($TS, ADT_RuntimeException, "StackOverflow");
       RuntimeException_MalFormedURI_str = $TF.constructor($TS, ADT_RuntimeException, "MalFormedURI", $TF.stringType(), "uri");
       RuntimeException_NotImplemented_str = $TF.constructor($TS, ADT_RuntimeException, "NotImplemented", $TF.stringType(), "message");
       RuntimeException_EmptySet_ = $TF.constructor($TS, ADT_RuntimeException, "EmptySet");
       RuntimeException_IllegalArgument_ = $TF.constructor($TS, ADT_RuntimeException, "IllegalArgument");
       RuntimeException_RegExpSyntaxError_str = $TF.constructor($TS, ADT_RuntimeException, "RegExpSyntaxError", $TF.stringType(), "message");
       RuntimeException_IO_str = $TF.constructor($TS, ADT_RuntimeException, "IO", $TF.stringType(), "message");
       RuntimeException_InvalidUseOfLocation_str = $TF.constructor($TS, ADT_RuntimeException, "InvalidUseOfLocation", $TF.stringType(), "message");
       RuntimeException_PermissionDenied_ = $TF.constructor($TS, ADT_RuntimeException, "PermissionDenied");
       RuntimeException_JavaCompilation_str_int_int_str_list_loc = $TF.constructor($TS, ADT_RuntimeException, "JavaCompilation", $TF.stringType(), "message", $TF.integerType(), "line", $TF.integerType(), "column", $TF.stringType(), "source", $TF.listType($T13), "classpath");
       RuntimeException_ArithmeticException_str = $TF.constructor($TS, ADT_RuntimeException, "ArithmeticException", $TF.stringType(), "message");
       RuntimeException_NoSuchElement_value = $TF.constructor($TS, ADT_RuntimeException, "NoSuchElement", $TF.valueType(), "v");
       RuntimeException_IndexOutOfBounds_int = $TF.constructor($TS, ADT_RuntimeException, "IndexOutOfBounds", $TF.integerType(), "index");
       RuntimeException_CallFailed_list_value = $TF.constructor($TS, ADT_RuntimeException, "CallFailed", $TF.listType($T10), "arguments");
       RuntimeException_ParseError_loc = $TF.constructor($TS, ADT_RuntimeException, "ParseError", $TF.sourceLocationType(), "location");
       RuntimeException_ImplodeError_str = $TF.constructor($TS, ADT_RuntimeException, "ImplodeError", $TF.stringType(), "message");
       RuntimeException_NoSuchKey_value = $TF.constructor($TS, ADT_RuntimeException, "NoSuchKey", $TF.valueType(), "key");
       RuntimeException_JavaException_str_str = $TF.constructor($TS, ADT_RuntimeException, "JavaException", $TF.stringType(), "class", $TF.stringType(), "message");
       RuntimeException_DateTimePrintingError_str = $TF.constructor($TS, ADT_RuntimeException, "DateTimePrintingError", $TF.stringType(), "message");
       RuntimeException_AssertionFailed_ = $TF.constructor($TS, ADT_RuntimeException, "AssertionFailed");
       RuntimeException_SchemeNotSupported_loc = $TF.constructor($TS, ADT_RuntimeException, "SchemeNotSupported", $TF.sourceLocationType(), "location");
       RuntimeException_NoSuchField_str = $TF.constructor($TS, ADT_RuntimeException, "NoSuchField", $TF.stringType(), "name");
       RuntimeException_InvalidUseOfDateTime_str = $TF.constructor($TS, ADT_RuntimeException, "InvalidUseOfDateTime", $TF.stringType(), "message");
       RuntimeException_UnavailableInformation_ = $TF.constructor($TS, ADT_RuntimeException, "UnavailableInformation");
       RuntimeException_Ambiguity_loc_str_str = $TF.constructor($TS, ADT_RuntimeException, "Ambiguity", $TF.sourceLocationType(), "location", $TF.stringType(), "nonterminal", $TF.stringType(), "sentence");
       RuntimeException_DateTimeParsingError_str = $TF.constructor($TS, ADT_RuntimeException, "DateTimeParsingError", $TF.stringType(), "message");
       RuntimeException_PermissionDenied_str = $TF.constructor($TS, ADT_RuntimeException, "PermissionDenied", $TF.stringType(), "message");
       RuntimeException_InvalidUseOfTime_str = $TF.constructor($TS, ADT_RuntimeException, "InvalidUseOfTime", $TF.stringType(), "message");
       RuntimeException_IllegalArgument_value_str = $TF.constructor($TS, ADT_RuntimeException, "IllegalArgument", $TF.valueType(), "v", $TF.stringType(), "message");
       RuntimeException_InvalidUseOfDate_str = $TF.constructor($TS, ADT_RuntimeException, "InvalidUseOfDate", $TF.stringType(), "message");
       RuntimeException_IllegalArgument_value = $TF.constructor($TS, ADT_RuntimeException, "IllegalArgument", $TF.valueType(), "v");
       RuntimeException_PathNotFound_loc = $TF.constructor($TS, ADT_RuntimeException, "PathNotFound", $TF.sourceLocationType(), "location");
       RuntimeException_AssertionFailed_str = $TF.constructor($TS, ADT_RuntimeException, "AssertionFailed", $TF.stringType(), "label");
       RuntimeException_ModuleNotFound_str = $TF.constructor($TS, ADT_RuntimeException, "ModuleNotFound", $TF.stringType(), "name");
       RuntimeException_PathNotFound_set_loc = $TF.constructor($TS, ADT_RuntimeException, "PathNotFound", $TF.setType($T13), "locs");
       RuntimeException_JavaException_str_str_RuntimeException = $TF.constructor($TS, ADT_RuntimeException, "JavaException", $TF.stringType(), "class", $TF.stringType(), "message", ADT_RuntimeException, "cause");
       RuntimeException_EmptyMap_ = $TF.constructor($TS, ADT_RuntimeException, "EmptyMap");
       RuntimeException_Java_str_str_RuntimeException = $TF.constructor($TS, ADT_RuntimeException, "Java", $TF.stringType(), "class", $TF.stringType(), "message", ADT_RuntimeException, "cause");
    
       
       
    }
    
    
    public static void main(String[] args) {
      throw new RuntimeException("No function `main` found in Rascal module `Exception`");
    }
}