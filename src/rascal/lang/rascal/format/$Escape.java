package rascal.lang.rascal.format;
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
public class $Escape 
    extends
        org.rascalmpl.runtime.$RascalModule
    implements 
    	rascal.lang.rascal.format.$Escape_$I {

    private final $Escape_$I $me;
    private final IList $constants;
    
    
    public final rascal.$Exception M_Exception;
    public final rascal.$String M_String;

    
    
    public IList ascii;
    public IList hex;
    public final io.usethesource.vallang.type.Type $T0;	/*astr()*/
    public final io.usethesource.vallang.type.Type $T1;	/*aint()*/
    public final io.usethesource.vallang.type.Type ADT_Symbol;	/*aadt("Symbol",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Tree;	/*aadt("Tree",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Associativity;	/*aadt("Associativity",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_CharRange;	/*aadt("CharRange",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type $T2;	/*aparameter("T",aadt("Tree",[],dataSyntax()),closed=true)*/
    public final io.usethesource.vallang.type.Type ADT_Production;	/*aadt("Production",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_RuntimeException;	/*aadt("RuntimeException",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Exception;	/*aadt("Exception",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Attr;	/*aadt("Attr",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Condition;	/*aadt("Condition",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_TreeSearchResult_1;	/*aadt("TreeSearchResult",[aparameter("T",aadt("Tree",[],dataSyntax()),closed=true)],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Message;	/*aadt("Message",[],dataSyntax())*/

    public $Escape(RascalExecutionContext rex){
        this(rex, null);
    }
    
    public $Escape(RascalExecutionContext rex, Object extended){
       super(rex);
       this.$me = extended == null ? this : ($Escape_$I)extended;
       ModuleStore mstore = rex.getModuleStore();
       mstore.put(rascal.lang.rascal.format.$Escape.class, this);
       
       mstore.importModule(rascal.$Exception.class, rex, rascal.$Exception::new);
       mstore.importModule(rascal.$String.class, rex, rascal.$String::new); 
       
       M_Exception = mstore.getModule(rascal.$Exception.class);
       M_String = mstore.getModule(rascal.$String.class); 
       
                          
       
       $TS.importStore(M_Exception.$TS);
       $TS.importStore(M_String.$TS);
       
       $constants = readBinaryConstantsFile(this.getClass(), "rascal/lang/rascal/format/$Escape.constants", 30, "e47cdd6650bade5a3fc5118603a838be");
       ADT_Symbol = $adt("Symbol");
       ADT_Tree = $adt("Tree");
       ADT_Associativity = $adt("Associativity");
       ADT_CharRange = $adt("CharRange");
       ADT_Production = $adt("Production");
       ADT_RuntimeException = $adt("RuntimeException");
       ADT_Exception = $adt("Exception");
       ADT_Attr = $adt("Attr");
       ADT_Condition = $adt("Condition");
       ADT_Message = $adt("Message");
       $T0 = $TF.stringType();
       $T1 = $TF.integerType();
       $T2 = $TF.parameterType("T", ADT_Tree);
       ADT_TreeSearchResult_1 = $parameterizedAdt("TreeSearchResult", new Type[] { $T2 });
    
       ascii = ((IList)$constants.get(28)/*["\\a00","\\a01","\\a02","\\a03","\\a04","\\a05","\\a06","\\a07","\\b","\\t","\\n","\\a0B","\\a0C"," ...*/);
       final IListWriter $listwriter1 = (IListWriter)$RVF.listWriter();
       
       $LCOMP2_GEN5753:
       for(IInteger $elem4 = ((IInteger)$constants.get(25)/*0*/); $aint_less_aint($elem4,((IInteger)$constants.get(16)/*10*/)).getValue(); $elem4 = $aint_add_aint($elem4,((IInteger)$constants.get(26)/*1*/))){
           IInteger i_0 = null;
           final Template $template3 = (Template)new Template($RVF, "");
           $template3.addVal($elem4);
           $listwriter1.append($template3.close());
       }
       
       hex = ((IList)($alist_add_alist(((IList)($listwriter1.done())),((IList)$constants.get(29)/*["A","B","C","D","E","F"]*/))));
    
       
    }
    public IString escape(IValue $P0){ // Generated by Resolver
       IString $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type,$T0)){
         $result = (IString)lang_rascal_format_Escape_escape$1086776b187d3e03((IString) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public IString escape(IValue $P0, IValue $P1){ // Generated by Resolver
       return (IString) M_String.escape($P0, $P1);
    }
    public IBool testHex(){ // Generated by Resolver
       IBool $result = null;
       $result = (IBool)lang_rascal_format_Escape_testHex$fd1b8ad4dcbc462b();
       if($result != null) return $result;
       throw RuntimeExceptionFactory.callFailed($RVF.list());
    }
    public IBool testQuote(){ // Generated by Resolver
       IBool $result = null;
       $result = (IBool)lang_rascal_format_Escape_testQuote$6d4d4c5923ccb91a();
       if($result != null) return $result;
       throw RuntimeExceptionFactory.callFailed($RVF.list());
    }
    public IBool testEOF(){ // Generated by Resolver
       IBool $result = null;
       $result = (IBool)lang_rascal_format_Escape_testEOF$b76e22e4828b55be();
       if($result != null) return $result;
       throw RuntimeExceptionFactory.callFailed($RVF.list());
    }
    public IString makeCharClassChar(IValue $P0){ // Generated by Resolver
       IString $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type,$T1)){
         $result = (IString)lang_rascal_format_Escape_makeCharClassChar$af136242216e0eb8((IInteger) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public IBool testA(){ // Generated by Resolver
       IBool $result = null;
       $result = (IBool)lang_rascal_format_Escape_testA$5e74e2513d683cc6();
       if($result != null) return $result;
       throw RuntimeExceptionFactory.callFailed($RVF.list());
    }
    public IBool testNl(){ // Generated by Resolver
       IBool $result = null;
       $result = (IBool)lang_rascal_format_Escape_testNl$c702cf32d456a861();
       if($result != null) return $result;
       throw RuntimeExceptionFactory.callFailed($RVF.list());
    }
    public IString makeStringChar(IValue $P0){ // Generated by Resolver
       IString $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type,$T1)){
         $result = (IString)lang_rascal_format_Escape_makeStringChar$2d8526200f5c166a((IInteger) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public IString ciquote(IValue $P0){ // Generated by Resolver
       IString $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type,$T0)){
         $result = (IString)lang_rascal_format_Escape_ciquote$233f8471794b1ec0((IString) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public IString quote(IValue $P0){ // Generated by Resolver
       IString $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type,$T0)){
         $result = (IString)lang_rascal_format_Escape_quote$c06370ee310cb7a7((IString) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }

    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/lang/rascal/format/Escape.rsc|(5281,437,<151,0>,<162,1>) 
    public IString lang_rascal_format_Escape_makeCharClassChar$af136242216e0eb8(IInteger ch_0){ 
        
        
        final IInteger $switchVal0 = ((IInteger)ch_0);
        boolean noCaseMatched_$switchVal0 = true;
        SWITCH0: switch(Fingerprint.getFingerprint($switchVal0)){
        
            case -1907019488:
                if(noCaseMatched_$switchVal0){
                    noCaseMatched_$switchVal0 = false;
                    if($isSubtypeOf($switchVal0.getType(),$T1)){
                       /*muExists*/CASE_1907019488_3: 
                           do {
                               if(((IInteger)$constants.get(0)/*93*/).equals($switchVal0)){
                                  return ((IString)$constants.get(1)/*"\]"*/);
                               
                               }
                       
                           } while(false);
                    
                    }
        
                }
                
        
            case -918232909:
                if(noCaseMatched_$switchVal0){
                    noCaseMatched_$switchVal0 = false;
                    if($isSubtypeOf($switchVal0.getType(),$T1)){
                       /*muExists*/CASE_918232909_0: 
                           do {
                               if(((IInteger)$constants.get(2)/*32*/).equals($switchVal0)){
                                  return ((IString)$constants.get(3)/*"\ "*/);
                               
                               }
                       
                           } while(false);
                    
                    }
        
                }
                
        
            case -591751282:
                if(noCaseMatched_$switchVal0){
                    noCaseMatched_$switchVal0 = false;
                    if($isSubtypeOf($switchVal0.getType(),$T1)){
                       /*muExists*/CASE_591751282_1: 
                           do {
                               if(((IInteger)$constants.get(4)/*45*/).equals($switchVal0)){
                                  return ((IString)$constants.get(5)/*"\-"*/);
                               
                               }
                       
                           } while(false);
                    
                    }
        
                }
                
        
            case 1173916482:
                if(noCaseMatched_$switchVal0){
                    noCaseMatched_$switchVal0 = false;
                    if($isSubtypeOf($switchVal0.getType(),$T1)){
                       /*muExists*/CASE_1173916482_4: 
                           do {
                               if(((IInteger)$constants.get(6)/*95*/).equals($switchVal0)){
                                  return ((IString)$constants.get(7)/*"_"*/);
                               
                               }
                       
                           } while(false);
                    
                    }
        
                }
                
        
            case -1254175367:
                if(noCaseMatched_$switchVal0){
                    noCaseMatched_$switchVal0 = false;
                    if($isSubtypeOf($switchVal0.getType(),$T1)){
                       /*muExists*/CASE_1254175367_2: 
                           do {
                               if(((IInteger)$constants.get(8)/*91*/).equals($switchVal0)){
                                  return ((IString)$constants.get(9)/*"\["*/);
                               
                               }
                       
                           } while(false);
                    
                    }
        
                }
                
        
            default: return ((IString)($me.makeStringChar(((IInteger)ch_0))));
        }
        
                   
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/lang/rascal/format/Escape.rsc|(5797,595,<166,0>,<185,1>) 
    public IString lang_rascal_format_Escape_makeStringChar$2d8526200f5c166a(IInteger ch_0){ 
        
        
        if((((IBool)($aint_less_aint(((IInteger)ch_0),((IInteger)$constants.get(10)/*128*/))))).getValue()){
           return ((IString)($alist_subscript_int(((IList)ascii),((IInteger)ch_0).intValue())));
        
        } else {
           if((((IBool)($aint_less_aint(((IInteger)ch_0),((IInteger)$constants.get(10)/*128*/)).not()))).getValue()){
              if((((IBool)($aint_lessequal_aint(((IInteger)ch_0),((IInteger)$constants.get(11)/*4095*/))))).getValue()){
                 IInteger d1_1 = ((IInteger)(((IInteger)ch_0).remainder(((IInteger)$constants.get(12)/*8*/))));
                 IInteger r1_2 = ((IInteger)($aint_divide_aint(((IInteger)ch_0),((IInteger)$constants.get(12)/*8*/))));
                 IInteger d2_3 = ((IInteger)(((IInteger)r1_2).remainder(((IInteger)$constants.get(12)/*8*/))));
                 IInteger r2_4 = ((IInteger)($aint_divide_aint(((IInteger)r1_2),((IInteger)$constants.get(12)/*8*/))));
                 IInteger d3_5 = ((IInteger)(((IInteger)r2_4).remainder(((IInteger)$constants.get(12)/*8*/))));
                 IInteger r3_6 = ((IInteger)($aint_divide_aint(((IInteger)r2_4),((IInteger)$constants.get(12)/*8*/))));
                 IInteger d4_7 = ((IInteger)r3_6);
                 final Template $template5 = (Template)new Template($RVF, "\\u");
                 $template5.beginIndent("   ");
                 $template5.addVal(d4_7);
                 $template5.endIndent("   ");
                 ;$template5.addVal(d3_5);
                 ;$template5.addVal(d2_3);
                 ;$template5.addVal(d1_1);
                 return ((IString)($template5.close()));
              
              }
           
           }
        
        }IInteger d1_8 = ((IInteger)(((IInteger)ch_0).remainder(((IInteger)$constants.get(13)/*16*/))));
        IInteger r1_9 = ((IInteger)($aint_divide_aint(((IInteger)ch_0),((IInteger)$constants.get(13)/*16*/))));
        IInteger d2_10 = ((IInteger)(((IInteger)r1_9).remainder(((IInteger)$constants.get(13)/*16*/))));
        IInteger r2_11 = ((IInteger)($aint_divide_aint(((IInteger)r1_9),((IInteger)$constants.get(13)/*16*/))));
        IInteger d3_12 = ((IInteger)(((IInteger)r2_11).remainder(((IInteger)$constants.get(13)/*16*/))));
        IInteger r3_13 = ((IInteger)($aint_divide_aint(((IInteger)r2_11),((IInteger)$constants.get(13)/*16*/))));
        IInteger d4_14 = ((IInteger)(((IInteger)r3_13).remainder(((IInteger)$constants.get(13)/*16*/))));
        IInteger r4_15 = ((IInteger)($aint_divide_aint(((IInteger)r3_13),((IInteger)$constants.get(13)/*16*/))));
        IInteger d5_16 = ((IInteger)(((IInteger)r4_15).remainder(((IInteger)$constants.get(13)/*16*/))));
        IInteger r5_17 = ((IInteger)($aint_divide_aint(((IInteger)r4_15),((IInteger)$constants.get(13)/*16*/))));
        IInteger d6_18 = ((IInteger)r5_17);
        final Template $template6 = (Template)new Template($RVF, "\\U");
        $template6.beginIndent("   ");
        $template6.addStr(((IString)($alist_subscript_int(((IList)hex),((IInteger)d6_18).intValue()))).getValue());
        $template6.endIndent("   ");
        ;$template6.addStr(((IString)($alist_subscript_int(((IList)hex),((IInteger)d5_16).intValue()))).getValue());
        ;$template6.addStr(((IString)($alist_subscript_int(((IList)hex),((IInteger)d4_14).intValue()))).getValue());
        ;$template6.addStr(((IString)($alist_subscript_int(((IList)hex),((IInteger)d3_12).intValue()))).getValue());
        ;$template6.addStr(((IString)($alist_subscript_int(((IList)hex),((IInteger)d2_10).intValue()))).getValue());
        ;$template6.addStr(((IString)($alist_subscript_int(((IList)hex),((IInteger)d1_8).intValue()))).getValue());
        return ((IString)($template6.close()));
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/lang/rascal/format/Escape.rsc|(6394,47,<187,0>,<187,47>) 
    public IBool lang_rascal_format_Escape_testA$5e74e2513d683cc6(){ 
        
        
        return ((IBool)($equal(((IString)($me.makeStringChar(((IInteger)$constants.get(14)/*97*/)))), ((IString)$constants.get(15)/*"a"*/))));
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/lang/rascal/format/Escape.rsc|(6442,50,<188,0>,<188,50>) 
    public IBool lang_rascal_format_Escape_testNl$c702cf32d456a861(){ 
        
        
        return ((IBool)($equal(((IString)($me.makeStringChar(((IInteger)$constants.get(16)/*10*/)))), ((IString)$constants.get(17)/*"\n"*/))));
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/lang/rascal/format/Escape.rsc|(6493,54,<189,0>,<189,54>) 
    public IBool lang_rascal_format_Escape_testQuote$6d4d4c5923ccb91a(){ 
        
        
        return ((IBool)($equal(((IString)($me.makeStringChar(((IInteger)$constants.get(18)/*34*/)))), ((IString)$constants.get(19)/*"\""*/))));
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/lang/rascal/format/Escape.rsc|(6548,55,<190,0>,<190,55>) 
    public IBool lang_rascal_format_Escape_testEOF$b76e22e4828b55be(){ 
        
        
        return ((IBool)($equal(((IString)($me.makeStringChar(((IInteger)$constants.get(20)/*255*/)))), ((IString)$constants.get(21)/*"\u0377"*/))));
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/lang/rascal/format/Escape.rsc|(6604,62,<191,0>,<191,62>) 
    public IBool lang_rascal_format_Escape_testHex$fd1b8ad4dcbc462b(){ 
        
        
        return ((IBool)($equal(((IString)($me.makeStringChar(((IInteger)$constants.get(22)/*11259375*/)))), ((IString)$constants.get(23)/*"\UABCDEF"*/))));
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/lang/rascal/format/Escape.rsc|(6668,215,<193,0>,<197,1>) 
    public IString lang_rascal_format_Escape_escape$1086776b187d3e03(IString s_0){ 
        
        
        if((((IBool)($equal(((IString)s_0), ((IString)$constants.get(24)/*""*/))))).getValue()){
           return ((IString)s_0);
        
        }
        IString $reducer8 = (IString)(((IString)$constants.get(24)/*""*/));
        final IInteger $lst2 = ((IInteger)(M_String.size(((IString)s_0))));
        final boolean $dir3 = ((IInteger)$constants.get(25)/*0*/).less($lst2).getValue();
        
        $REDUCER7_GEN6862:
        for(IInteger $elem9 = ((IInteger)$constants.get(25)/*0*/); $dir3 ? $aint_less_aint($elem9,$lst2).getValue() 
                                  : $aint_lessequal_aint($elem9,$lst2).not().getValue(); $elem9 = $aint_add_aint($elem9,$dir3 ? ((IInteger)$constants.get(26)/*1*/) : ((IInteger)$constants.get(27)/*-1*/))){
            IInteger i_2 = ((IInteger)($elem9));
            $reducer8 = ((IString)($astr_add_astr(((IString)($reducer8)),((IString)($me.makeStringChar(((IInteger)(M_String.charAt(((IString)s_0), ((IInteger)i_2))))))))));
        }
        
        return ((IString)($reducer8));
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/lang/rascal/format/Escape.rsc|(6885,176,<199,0>,<203,1>) 
    public IString lang_rascal_format_Escape_quote$c06370ee310cb7a7(IString s_0){ 
        
        
        final Template $template10 = (Template)new Template($RVF, "\"");
        $template10.beginIndent("  ");
        $template10.addStr(((IString)($me.escape(((IString)s_0)))).getValue());
        $template10.endIndent("  ");
        $template10.addStr("\"");
        return ((IString)($template10.close()));
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/lang/rascal/format/Escape.rsc|(7063,178,<205,0>,<209,1>) 
    public IString lang_rascal_format_Escape_ciquote$233f8471794b1ec0(IString s_0){ 
        
        
        final Template $template11 = (Template)new Template($RVF, "\'");
        $template11.beginIndent("  ");
        $template11.addStr(((IString)($me.escape(((IString)s_0)))).getValue());
        $template11.endIndent("  ");
        $template11.addStr("\'");
        return ((IString)($template11.close()));
    
    }
    

    public static void main(String[] args) {
      throw new RuntimeException("No function `main` found in Rascal module `lang::rascal::format::Escape`");
    }
}