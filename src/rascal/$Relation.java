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
public class $Relation 
    extends
        org.rascalmpl.runtime.$RascalModule
    implements 
    	rascal.$Relation_$I {

    private final $Relation_$I $me;
    private final IList $constants;
    
    

    
    final org.rascalmpl.library.Prelude $Prelude; // TODO: asBaseClassName will generate name collisions if there are more of the same name in different packages

    
    public final io.usethesource.vallang.type.Type $T1;	/*avalue()*/
    public final io.usethesource.vallang.type.Type $T14;	/*aparameter("T3",avalue(),closed=false)*/
    public final io.usethesource.vallang.type.Type $T41;	/*aparameter("T4",avalue(),closed=true)*/
    public final io.usethesource.vallang.type.Type $T36;	/*aparameter("T1",avalue(),closed=true)*/
    public final io.usethesource.vallang.type.Type $T37;	/*aparameter("T2",avalue(),closed=true)*/
    public final io.usethesource.vallang.type.Type $T9;	/*aparameter("T",avalue(),closed=false,alabel="ran")*/
    public final io.usethesource.vallang.type.Type $T58;	/*aparameter("K",avalue(),closed=true)*/
    public final io.usethesource.vallang.type.Type $T21;	/*aparameter("U",avalue(),closed=false)*/
    public final io.usethesource.vallang.type.Type $T2;	/*aparameter("T",avalue(),closed=false)*/
    public final io.usethesource.vallang.type.Type $T11;	/*aparameter("T0",avalue(),closed=false)*/
    public final io.usethesource.vallang.type.Type $T12;	/*aparameter("T1",avalue(),closed=false)*/
    public final io.usethesource.vallang.type.Type $T59;	/*aparameter("V",avalue(),closed=true)*/
    public final io.usethesource.vallang.type.Type $T8;	/*aparameter("U",avalue(),closed=false,alabel="dom")*/
    public final io.usethesource.vallang.type.Type $T35;	/*aparameter("T0",avalue(),closed=true)*/
    public final io.usethesource.vallang.type.Type $T13;	/*aparameter("T2",avalue(),closed=false)*/
    public final io.usethesource.vallang.type.Type $T15;	/*aparameter("T4",avalue(),closed=false)*/
    public final io.usethesource.vallang.type.Type $T25;	/*aparameter("K",avalue(),closed=false)*/
    public final io.usethesource.vallang.type.Type $T45;	/*aparameter("U",avalue(),closed=true)*/
    public final io.usethesource.vallang.type.Type $T26;	/*aparameter("V",avalue(),closed=false)*/
    public final io.usethesource.vallang.type.Type $T29;	/*aparameter("T",avalue(),closed=true)*/
    public final io.usethesource.vallang.type.Type $T39;	/*aparameter("T3",avalue(),closed=true)*/
    public final io.usethesource.vallang.type.Type $T53;	/*aset(aparameter("T1",avalue(),closed=true))*/
    public final io.usethesource.vallang.type.Type $T3;	/*aset(aparameter("T",avalue(),closed=false))*/
    public final io.usethesource.vallang.type.Type $T52;	/*arel(atypeList([aparameter("T4",avalue(),closed=true),aparameter("T3",avalue(),closed=true),aparameter("T2",avalue(),closed=true),aparameter("T1",avalue(),closed=true),aparameter("T0",avalue(),closed=true)]))*/
    public final io.usethesource.vallang.type.Type $T33;	/*arel(atypeList([aparameter("T",avalue(),closed=true),aparameter("T",avalue(),closed=true),aparameter("T",avalue(),closed=true),aparameter("T",avalue(),closed=true),aparameter("T",avalue(),closed=true)]))*/
    public final io.usethesource.vallang.type.Type $T30;	/*arel(atypeList([aparameter("T",avalue(),closed=true),aparameter("T",avalue(),closed=true)]))*/
    public final io.usethesource.vallang.type.Type $T55;	/*arel(atypeList([aparameter("T1",avalue(),closed=true),aparameter("T2",avalue(),closed=true),aparameter("T3",avalue(),closed=true)]))*/
    public final io.usethesource.vallang.type.Type $T0;	/*arel(atypeList([aparameter("T",avalue(),closed=false),aparameter("T",avalue(),closed=false),aparameter("T",avalue(),closed=false),aparameter("T",avalue(),closed=false),aparameter("T",avalue(),closed=false)]))*/
    public final io.usethesource.vallang.type.Type $T56;	/*arel(atypeList([aparameter("T1",avalue(),closed=true),aparameter("T2",avalue(),closed=true),aparameter("T3",avalue(),closed=true),aparameter("T4",avalue(),closed=true)]))*/
    public final io.usethesource.vallang.type.Type $T17;	/*arel(atypeList([aparameter("T0",avalue(),closed=false),aparameter("T1",avalue(),closed=false),aparameter("T2",avalue(),closed=false)]))*/
    public final io.usethesource.vallang.type.Type $T47;	/*aset(aparameter("U",avalue(),closed=true))*/
    public final io.usethesource.vallang.type.Type $T46;	/*aset(aset(aparameter("U",avalue(),closed=true)))*/
    public final io.usethesource.vallang.type.Type $T4;	/*arel(atypeList([aparameter("T",avalue(),closed=false),aparameter("T",avalue(),closed=false)]))*/
    public final io.usethesource.vallang.type.Type $T5;	/*arel(atypeList([aparameter("T",avalue(),closed=false),aparameter("T",avalue(),closed=false),aparameter("T",avalue(),closed=false)]))*/
    public final io.usethesource.vallang.type.Type $T24;	/*arel(atypeList([aparameter("K",avalue(),closed=false),aparameter("V",avalue(),closed=false)]))*/
    public final io.usethesource.vallang.type.Type $T7;	/*arel(atypeList([aparameter("U",avalue(),closed=false,alabel="dom"),aparameter("T",avalue(),closed=false,alabel="ran")]))*/
    public final io.usethesource.vallang.type.Type $T44;	/*arel(atypeList([aparameter("T",avalue(),closed=true),aparameter("U",avalue(),closed=true)]))*/
    public final io.usethesource.vallang.type.Type $T6;	/*arel(atypeList([aparameter("T",avalue(),closed=false),aparameter("T",avalue(),closed=false),aparameter("T",avalue(),closed=false),aparameter("T",avalue(),closed=false)]))*/
    public final io.usethesource.vallang.type.Type $T19;	/*aset(aparameter("T0",avalue(),closed=false))*/
    public final io.usethesource.vallang.type.Type $T40;	/*arel(atypeList([aparameter("T0",avalue(),closed=true),aparameter("T1",avalue(),closed=true),aparameter("T2",avalue(),closed=true),aparameter("T3",avalue(),closed=true)]))*/
    public final io.usethesource.vallang.type.Type $T60;	/*aset(aparameter("V",avalue(),closed=true))*/
    public final io.usethesource.vallang.type.Type $T43;	/*aset(aparameter("T0",avalue(),closed=true))*/
    public final io.usethesource.vallang.type.Type $T51;	/*arel(atypeList([aparameter("T3",avalue(),closed=true),aparameter("T2",avalue(),closed=true),aparameter("T1",avalue(),closed=true),aparameter("T0",avalue(),closed=true)]))*/
    public final io.usethesource.vallang.type.Type $T18;	/*arel(atypeList([aparameter("T0",avalue(),closed=false),aparameter("T1",avalue(),closed=false),aparameter("T2",avalue(),closed=false),aparameter("T3",avalue(),closed=false)]))*/
    public final io.usethesource.vallang.type.Type $T57;	/*amap(aparameter("K",avalue(),closed=true),aset(aparameter("V",avalue(),closed=true)))*/
    public final io.usethesource.vallang.type.Type $T42;	/*arel(atypeList([aparameter("T0",avalue(),closed=true),aparameter("T1",avalue(),closed=true),aparameter("T2",avalue(),closed=true),aparameter("T3",avalue(),closed=true),aparameter("T4",avalue(),closed=true)]))*/
    public final io.usethesource.vallang.type.Type $T23;	/*abool()*/
    public final io.usethesource.vallang.type.Type $T54;	/*arel(atypeList([aparameter("T1",avalue(),closed=true),aparameter("T2",avalue(),closed=true)]))*/
    public final io.usethesource.vallang.type.Type $T20;	/*arel(atypeList([aparameter("T",avalue(),closed=false),aparameter("U",avalue(),closed=false)]))*/
    public final io.usethesource.vallang.type.Type $T50;	/*arel(atypeList([aparameter("T2",avalue(),closed=true),aparameter("T1",avalue(),closed=true),aparameter("T0",avalue(),closed=true)]))*/
    public final io.usethesource.vallang.type.Type $T32;	/*arel(atypeList([aparameter("T",avalue(),closed=true),aparameter("T",avalue(),closed=true),aparameter("T",avalue(),closed=true),aparameter("T",avalue(),closed=true)]))*/
    public final io.usethesource.vallang.type.Type $T28;	/*aset(aparameter("T",avalue(),closed=true))*/
    public final io.usethesource.vallang.type.Type $T48;	/*aset(aset(aparameter("T",avalue(),closed=true)))*/
    public final io.usethesource.vallang.type.Type $T34;	/*arel(atypeList([aparameter("T0",avalue(),closed=true),aparameter("T1",avalue(),closed=true)]))*/
    public final io.usethesource.vallang.type.Type $T16;	/*arel(atypeList([aparameter("T0",avalue(),closed=false),aparameter("T1",avalue(),closed=false)]))*/
    public final io.usethesource.vallang.type.Type $T10;	/*arel(atypeList([aparameter("T0",avalue(),closed=false),aparameter("T1",avalue(),closed=false),aparameter("T2",avalue(),closed=false),aparameter("T3",avalue(),closed=false),aparameter("T4",avalue(),closed=false)]))*/
    public final io.usethesource.vallang.type.Type $T31;	/*arel(atypeList([aparameter("T",avalue(),closed=true),aparameter("T",avalue(),closed=true),aparameter("T",avalue(),closed=true)]))*/
    public final io.usethesource.vallang.type.Type $T22;	/*afunc(abool(),[aparameter("T",avalue(),closed=false)],[])*/
    public final io.usethesource.vallang.type.Type $T38;	/*arel(atypeList([aparameter("T0",avalue(),closed=true),aparameter("T1",avalue(),closed=true),aparameter("T2",avalue(),closed=true)]))*/
    public final io.usethesource.vallang.type.Type $T27;	/*aset(aparameter("T1",avalue(),closed=false))*/
    public final io.usethesource.vallang.type.Type $T49;	/*arel(atypeList([aparameter("T1",avalue(),closed=true),aparameter("T0",avalue(),closed=true)]))*/

    public $Relation(RascalExecutionContext rex){
        this(rex, null);
    }
    
    public $Relation(RascalExecutionContext rex, Object extended){
       super(rex);
       this.$me = extended == null ? this : ($Relation_$I)extended;
       ModuleStore mstore = rex.getModuleStore();
       mstore.put(rascal.$Relation.class, this);
        
        
       
                          
       
       
       $Prelude = $initLibrary("org.rascalmpl.library.Prelude"); 
    
       $constants = readBinaryConstantsFile(this.getClass(), "rascal/$Relation.constants", 5, "d62345a0b802833ae0f26ba12f6b3af4");
       $T1 = $TF.valueType();
       $T14 = $TF.parameterType("T3", $T1);
       $T41 = $TF.parameterType("T4", $T1);
       $T36 = $TF.parameterType("T1", $T1);
       $T37 = $TF.parameterType("T2", $T1);
       $T9 = $TF.parameterType("T", $T1);
       $T58 = $TF.parameterType("K", $T1);
       $T21 = $TF.parameterType("U", $T1);
       $T2 = $TF.parameterType("T", $T1);
       $T11 = $TF.parameterType("T0", $T1);
       $T12 = $TF.parameterType("T1", $T1);
       $T59 = $TF.parameterType("V", $T1);
       $T8 = $TF.parameterType("U", $T1);
       $T35 = $TF.parameterType("T0", $T1);
       $T13 = $TF.parameterType("T2", $T1);
       $T15 = $TF.parameterType("T4", $T1);
       $T25 = $TF.parameterType("K", $T1);
       $T45 = $TF.parameterType("U", $T1);
       $T26 = $TF.parameterType("V", $T1);
       $T29 = $TF.parameterType("T", $T1);
       $T39 = $TF.parameterType("T3", $T1);
       $T53 = $TF.setType($T36);
       $T3 = $TF.setType($T2);
       $T52 = $TF.setType($TF.tupleType($T41, $T39, $T37, $T36, $T35));
       $T33 = $TF.setType($TF.tupleType($T29, $T29, $T29, $T29, $T29));
       $T30 = $TF.setType($TF.tupleType($T29, $T29));
       $T55 = $TF.setType($TF.tupleType($T36, $T37, $T39));
       $T0 = $TF.setType($TF.tupleType($T2, $T2, $T2, $T2, $T2));
       $T56 = $TF.setType($TF.tupleType($T36, $T37, $T39, $T41));
       $T17 = $TF.setType($TF.tupleType($T11, $T12, $T13));
       $T47 = $TF.setType($T45);
       $T46 = $TF.setType($T47);
       $T4 = $TF.setType($TF.tupleType($T2, $T2));
       $T5 = $TF.setType($TF.tupleType($T2, $T2, $T2));
       $T24 = $TF.setType($TF.tupleType($T25, $T26));
       $T7 = $TF.setType($TF.tupleType($T8, $T9));
       $T44 = $TF.setType($TF.tupleType($T29, $T45));
       $T6 = $TF.setType($TF.tupleType($T2, $T2, $T2, $T2));
       $T19 = $TF.setType($T11);
       $T40 = $TF.setType($TF.tupleType($T35, $T36, $T37, $T39));
       $T60 = $TF.setType($T59);
       $T43 = $TF.setType($T35);
       $T51 = $TF.setType($TF.tupleType($T39, $T37, $T36, $T35));
       $T18 = $TF.setType($TF.tupleType($T11, $T12, $T13, $T14));
       $T57 = $TF.mapType($T58,$T60);
       $T42 = $TF.setType($TF.tupleType($T35, $T36, $T37, $T39, $T41));
       $T23 = $TF.boolType();
       $T54 = $TF.setType($TF.tupleType($T36, $T37));
       $T20 = $TF.setType($TF.tupleType($T2, $T21));
       $T50 = $TF.setType($TF.tupleType($T37, $T36, $T35));
       $T32 = $TF.setType($TF.tupleType($T29, $T29, $T29, $T29));
       $T28 = $TF.setType($T29);
       $T48 = $TF.setType($T28);
       $T34 = $TF.setType($TF.tupleType($T35, $T36));
       $T16 = $TF.setType($TF.tupleType($T11, $T12));
       $T10 = $TF.setType($TF.tupleType($T11, $T12, $T13, $T14, $T15));
       $T31 = $TF.setType($TF.tupleType($T29, $T29, $T29));
       $T22 = $TF.functionType($T23, $TF.tupleType($T2), $TF.tupleEmpty());
       $T38 = $TF.setType($TF.tupleType($T35, $T36, $T37));
       $T27 = $TF.setType($T12);
       $T49 = $TF.setType($TF.tupleType($T36, $T35));
    
       
       
    }
    public ISet carrierX(IValue $P0, IValue $P1){ // Generated by Resolver
       ISet $result = null;
       Type $P0Type = $P0.getType();
       Type $P1Type = $P1.getType();
       if($isSubtypeOf($P0Type,$T0) && $isSubtypeOf($P1Type,$T3)){
         $result = (ISet)Relation_carrierX$dc7d4f1475a31c1a((ISet) $P0, (ISet) $P1);
         if($result != null) return $result;
       }
       if($isSubtypeOf($P0Type,$T4) && $isSubtypeOf($P1Type,$T3)){
         $result = (ISet)Relation_carrierX$5e8f4248c208f606((ISet) $P0, (ISet) $P1);
         if($result != null) return $result;
       }
       if($isSubtypeOf($P0Type,$T5) && $isSubtypeOf($P1Type,$T3)){
         $result = (ISet)Relation_carrierX$7005b62f0e805b5f((ISet) $P0, (ISet) $P1);
         if($result != null) return $result;
       }
       if($isSubtypeOf($P0Type,$T6) && $isSubtypeOf($P1Type,$T3)){
         $result = (ISet)Relation_carrierX$c8228c40b970c38b((ISet) $P0, (ISet) $P1);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0, $P1));
    }
    public ISet groupDomainByRange(IValue $P0){ // Generated by Resolver
       ISet $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type,$T7)){
         $result = (ISet)Relation_groupDomainByRange$327972fc187fd972((ISet) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public ISet domain(IValue $P0){ // Generated by Resolver
       ISet $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type,$T10)){
         $result = (ISet)Relation_domain$7e7cc8be3201741e((ISet) $P0);
         if($result != null) return $result;
       }
       if($isSubtypeOf($P0Type,$T16)){
         $result = (ISet)Relation_domain$82c6d674586e8686((ISet) $P0);
         if($result != null) return $result;
       }
       if($isSubtypeOf($P0Type,$T17)){
         $result = (ISet)Relation_domain$2a41e81f2232a510((ISet) $P0);
         if($result != null) return $result;
       }
       if($isSubtypeOf($P0Type,$T18)){
         $result = (ISet)Relation_domain$ceb529b2fc10d7d4((ISet) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public ISet complement(IValue $P0){ // Generated by Resolver
       ISet $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type,$T10)){
         $result = (ISet)Relation_complement$56d58cd1d8429e72((ISet) $P0);
         if($result != null) return $result;
       }
       if($isSubtypeOf($P0Type,$T17)){
         $result = (ISet)Relation_complement$00086bfeeba07066((ISet) $P0);
         if($result != null) return $result;
       }
       if($isSubtypeOf($P0Type,$T16)){
         $result = (ISet)Relation_complement$4bb4b4dc0b4215a5((ISet) $P0);
         if($result != null) return $result;
       }
       if($isSubtypeOf($P0Type,$T18)){
         $result = (ISet)Relation_complement$1dc10ce8d46ef909((ISet) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public ISet domainR(IValue $P0, IValue $P1){ // Generated by Resolver
       ISet $result = null;
       Type $P0Type = $P0.getType();
       Type $P1Type = $P1.getType();
       if($isSubtypeOf($P0Type,$T16) && $isSubtypeOf($P1Type,$T19)){
         $result = (ISet)Relation_domainR$6df70016a64d6b6b((ISet) $P0, (ISet) $P1);
         if($result != null) return $result;
       }
       if($isSubtypeOf($P0Type,$T17) && $isSubtypeOf($P1Type,$T19)){
         $result = (ISet)Relation_domainR$acdf919373377fa5((ISet) $P0, (ISet) $P1);
         if($result != null) return $result;
       }
       if($isSubtypeOf($P0Type,$T18) && $isSubtypeOf($P1Type,$T19)){
         $result = (ISet)Relation_domainR$4e560a92f3102f3a((ISet) $P0, (ISet) $P1);
         if($result != null) return $result;
       }
       if($isSubtypeOf($P0Type,$T20) && $isSubtypeOf($P1Type,$T22)){
         $result = (ISet)Relation_domainR$c52c9514cb94dac5((ISet) $P0, (TypedFunctionInstance1<IValue, IValue>) $P1);
         if($result != null) return $result;
       }
       if($isSubtypeOf($P0Type,$T10) && $isSubtypeOf($P1Type,$T19)){
         $result = (ISet)Relation_domainR$c0b08c3c279262a0((ISet) $P0, (ISet) $P1);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0, $P1));
    }
    public ISet ident(IValue $P0){ // Generated by Resolver
       ISet $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type,$T3)){
         $result = (ISet)Relation_ident$5a8fd748b3a21c74((ISet) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public IMap index(IValue $P0){ // Generated by Resolver
       IMap $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type,$T24)){
         $result = (IMap)Relation_index$d81a1657f0d245c9((ISet) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public ISet domainX(IValue $P0, IValue $P1){ // Generated by Resolver
       ISet $result = null;
       Type $P0Type = $P0.getType();
       Type $P1Type = $P1.getType();
       if($isSubtypeOf($P0Type,$T10) && $isSubtypeOf($P1Type,$T19)){
         $result = (ISet)Relation_domainX$b4c31366bacacc90((ISet) $P0, (ISet) $P1);
         if($result != null) return $result;
       }
       if($isSubtypeOf($P0Type,$T18) && $isSubtypeOf($P1Type,$T19)){
         $result = (ISet)Relation_domainX$7ed378a6babc8601((ISet) $P0, (ISet) $P1);
         if($result != null) return $result;
       }
       if($isSubtypeOf($P0Type,$T16) && $isSubtypeOf($P1Type,$T19)){
         $result = (ISet)Relation_domainX$227b267edf852d93((ISet) $P0, (ISet) $P1);
         if($result != null) return $result;
       }
       if($isSubtypeOf($P0Type,$T17) && $isSubtypeOf($P1Type,$T19)){
         $result = (ISet)Relation_domainX$3378efc5bc069d9d((ISet) $P0, (ISet) $P1);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0, $P1));
    }
    public ISet rangeR(IValue $P0, IValue $P1){ // Generated by Resolver
       ISet $result = null;
       Type $P0Type = $P0.getType();
       Type $P1Type = $P1.getType();
       if($isSubtypeOf($P0Type,$T16) && $isSubtypeOf($P1Type,$T27)){
         $result = (ISet)Relation_rangeR$442c89d533c8dd3c((ISet) $P0, (ISet) $P1);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0, $P1));
    }
    public ISet invert(IValue $P0){ // Generated by Resolver
       ISet $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type,$T18)){
         $result = (ISet)Relation_invert$23c95694a74f9dc2((ISet) $P0);
         if($result != null) return $result;
       }
       if($isSubtypeOf($P0Type,$T10)){
         $result = (ISet)Relation_invert$7bfa0a46912a50e5((ISet) $P0);
         if($result != null) return $result;
       }
       if($isSubtypeOf($P0Type,$T17)){
         $result = (ISet)Relation_invert$02b31d4ba203e6f2((ISet) $P0);
         if($result != null) return $result;
       }
       if($isSubtypeOf($P0Type,$T16)){
         $result = (ISet)Relation_invert$ab51fefa560e8d82((ISet) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public ISet carrier(IValue $P0){ // Generated by Resolver
       ISet $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type,$T5)){
         $result = (ISet)Relation_carrier$6e6c58db994362e2((ISet) $P0);
         if($result != null) return $result;
       }
       if($isSubtypeOf($P0Type,$T0)){
         $result = (ISet)Relation_carrier$dedcc54b07b49f23((ISet) $P0);
         if($result != null) return $result;
       }
       if($isSubtypeOf($P0Type,$T6)){
         $result = (ISet)Relation_carrier$cfeb33350bad999c((ISet) $P0);
         if($result != null) return $result;
       }
       if($isSubtypeOf($P0Type,$T4)){
         $result = (ISet)Relation_carrier$fdfb882af871e8e7((ISet) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public ISet groupRangeByDomain(IValue $P0){ // Generated by Resolver
       ISet $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type,$T7)){
         $result = (ISet)Relation_groupRangeByDomain$bc2bb3150125fdcf((ISet) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public ISet carrierR(IValue $P0, IValue $P1){ // Generated by Resolver
       ISet $result = null;
       Type $P0Type = $P0.getType();
       Type $P1Type = $P1.getType();
       if($isSubtypeOf($P0Type,$T4) && $isSubtypeOf($P1Type,$T3)){
         $result = (ISet)Relation_carrierR$0c3cc6c60ce2992e((ISet) $P0, (ISet) $P1);
         if($result != null) return $result;
       }
       if($isSubtypeOf($P0Type,$T0) && $isSubtypeOf($P1Type,$T3)){
         $result = (ISet)Relation_carrierR$eb31e23e51d7fd68((ISet) $P0, (ISet) $P1);
         if($result != null) return $result;
       }
       if($isSubtypeOf($P0Type,$T5) && $isSubtypeOf($P1Type,$T3)){
         $result = (ISet)Relation_carrierR$f255ce7884c94485((ISet) $P0, (ISet) $P1);
         if($result != null) return $result;
       }
       if($isSubtypeOf($P0Type,$T6) && $isSubtypeOf($P1Type,$T3)){
         $result = (ISet)Relation_carrierR$52b35d289144910a((ISet) $P0, (ISet) $P1);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0, $P1));
    }
    public ISet rangeX(IValue $P0, IValue $P1){ // Generated by Resolver
       ISet $result = null;
       Type $P0Type = $P0.getType();
       Type $P1Type = $P1.getType();
       if($isSubtypeOf($P0Type,$T16) && $isSubtypeOf($P1Type,$T27)){
         $result = (ISet)Relation_rangeX$2ca0c43b311ef4db((ISet) $P0, (ISet) $P1);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0, $P1));
    }
    public ISet range(IValue $P0){ // Generated by Resolver
       ISet $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type,$T10)){
         $result = (ISet)Relation_range$22ad49e0807b0b4b((ISet) $P0);
         if($result != null) return $result;
       }
       if($isSubtypeOf($P0Type,$T16)){
         $result = (ISet)Relation_range$2db5a57a784252a8((ISet) $P0);
         if($result != null) return $result;
       }
       if($isSubtypeOf($P0Type,$T18)){
         $result = (ISet)Relation_range$3bab8d1f24ba88b1((ISet) $P0);
         if($result != null) return $result;
       }
       if($isSubtypeOf($P0Type,$T17)){
         $result = (ISet)Relation_range$86a90cd62434b56a((ISet) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }

    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Relation.rsc|(543,256,<19,0>,<30,1>) 
    public ISet Relation_carrier$fdfb882af871e8e7(ISet R_0){ 
        
        
        HashMap<io.usethesource.vallang.type.Type,io.usethesource.vallang.type.Type> $typeBindings = new HashMap<>();
        if($T4.match(R_0.getType(), $typeBindings)){
           final ISet $result0 = ((ISet)($aset_add_aset(((ISet)($arel_field_project((ISet)((ISet)R_0), ((IInteger)$constants.get(0)/*0*/)))),((ISet)($arel_field_project((ISet)((ISet)R_0), ((IInteger)$constants.get(1)/*1*/)))))));
           if($T28.instantiate($typeBindings) != $TF.voidType() && $isSubtypeOf($result0.getType(),$T28)){
              return ((ISet)($result0));
           
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Relation.rsc|(801,78,<32,0>,<35,1>) 
    public ISet Relation_carrier$6e6c58db994362e2(ISet R_0){ 
        
        
        HashMap<io.usethesource.vallang.type.Type,io.usethesource.vallang.type.Type> $typeBindings = new HashMap<>();
        if($T5.match(R_0.getType(), $typeBindings)){
           final ISet $result1 = ((ISet)($aset_add_aset(((ISet)($aset_add_aset(((ISet)($arel_field_project((ISet)((ISet)R_0), ((IInteger)$constants.get(0)/*0*/)))),((ISet)($arel_field_project((ISet)((ISet)R_0), ((IInteger)$constants.get(1)/*1*/))))))),((ISet)($arel_field_project((ISet)((ISet)R_0), ((IInteger)$constants.get(2)/*2*/)))))));
           if($T28.instantiate($typeBindings) != $TF.voidType() && $isSubtypeOf($result1.getType(),$T28)){
              return ((ISet)($result1));
           
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Relation.rsc|(881,89,<37,0>,<40,1>) 
    public ISet Relation_carrier$cfeb33350bad999c(ISet R_0){ 
        
        
        HashMap<io.usethesource.vallang.type.Type,io.usethesource.vallang.type.Type> $typeBindings = new HashMap<>();
        if($T6.match(R_0.getType(), $typeBindings)){
           final ISet $result2 = ((ISet)($aset_add_aset(((ISet)($aset_add_aset(((ISet)($aset_add_aset(((ISet)($arel_field_project((ISet)((ISet)R_0), ((IInteger)$constants.get(0)/*0*/)))),((ISet)($arel_field_project((ISet)((ISet)R_0), ((IInteger)$constants.get(1)/*1*/))))))),((ISet)($arel_field_project((ISet)((ISet)R_0), ((IInteger)$constants.get(2)/*2*/))))))),((ISet)($arel_field_project((ISet)((ISet)R_0), ((IInteger)$constants.get(3)/*3*/)))))));
           if($T28.instantiate($typeBindings) != $TF.voidType() && $isSubtypeOf($result2.getType(),$T28)){
              return ((ISet)($result2));
           
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Relation.rsc|(972,101,<42,0>,<45,1>) 
    public ISet Relation_carrier$dedcc54b07b49f23(ISet R_0){ 
        
        
        HashMap<io.usethesource.vallang.type.Type,io.usethesource.vallang.type.Type> $typeBindings = new HashMap<>();
        if($T0.match(R_0.getType(), $typeBindings)){
           final ISet $result3 = ((ISet)($aset_add_aset(((ISet)($aset_add_aset(((ISet)($aset_add_aset(((ISet)($aset_add_aset(((ISet)($arel_field_project((ISet)((ISet)R_0), ((IInteger)$constants.get(0)/*0*/)))),((ISet)($arel_field_project((ISet)((ISet)R_0), ((IInteger)$constants.get(1)/*1*/))))))),((ISet)($arel_field_project((ISet)((ISet)R_0), ((IInteger)$constants.get(2)/*2*/))))))),((ISet)($arel_field_project((ISet)((ISet)R_0), ((IInteger)$constants.get(3)/*3*/))))))),((ISet)($arel_field_project((ISet)((ISet)R_0), ((IInteger)$constants.get(4)/*4*/)))))));
           if($T28.instantiate($typeBindings) != $TF.voidType() && $isSubtypeOf($result3.getType(),$T28)){
              return ((ISet)($result3));
           
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Relation.rsc|(1076,372,<48,0>,<61,1>) 
    public ISet Relation_carrierR$0c3cc6c60ce2992e(ISet R_0, ISet S_1){ 
        
        
        HashMap<io.usethesource.vallang.type.Type,io.usethesource.vallang.type.Type> $typeBindings = new HashMap<>();
        if($T4.match(R_0.getType(), $typeBindings)){
           if(true){
              if($T3.match(S_1.getType(), $typeBindings)){
                 if(true){
                    final ISetWriter $setwriter4 = (ISetWriter)$RVF.setWriter();
                    ;
                    $SCOMP5_GEN1406:
                    for(IValue $elem6_for : ((ISet)R_0)){
                        IValue $elem6 = (IValue) $elem6_for;
                        final IValue $tuple_subject7 = ((IValue)($elem6));
                        if($tuple_subject7 instanceof ITuple && ((ITuple)$tuple_subject7).arity() == 2){
                           /*muExists*/$SCOMP5_GEN1406_TUPLE: 
                               do {
                                   if(true){
                                      IValue V0_2 = null;
                                      if(true){
                                         IValue V1_3 = null;
                                         if((((IBool)($RVF.bool(((ISet)S_1).contains(((IValue)($subscript_int(((IValue)($tuple_subject7)),0)))))))).getValue()){
                                           if((((IBool)($RVF.bool(((ISet)S_1).contains(((IValue)($subscript_int(((IValue)($tuple_subject7)),1)))))))).getValue()){
                                             $setwriter4.insert($RVF.tuple(((IValue)($subscript_int(((IValue)($tuple_subject7)),0))), ((IValue)($subscript_int(((IValue)($tuple_subject7)),1)))));
                                           
                                           } else {
                                             continue $SCOMP5_GEN1406_TUPLE;
                                           }
                                         
                                         } else {
                                           continue $SCOMP5_GEN1406_TUPLE;
                                         }
                                      
                                      } else {
                                         continue $SCOMP5_GEN1406_TUPLE;/*computeFail*/
                                      }
                                   } else {
                                      continue $SCOMP5_GEN1406;
                                   }
                               } while(false);
                        
                        } else {
                           continue $SCOMP5_GEN1406;
                        }
                    }
                    
                                final ISet $result8 = ((ISet)($setwriter4.done()));
                    if($T30.instantiate($typeBindings) != $TF.voidType() && $isSubtypeOf($result8.getType(),$T30)){
                       return ((ISet)($result8));
                    
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
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Relation.rsc|(1450,145,<63,0>,<66,1>) 
    public ISet Relation_carrierR$f255ce7884c94485(ISet R_0, ISet S_1){ 
        
        
        HashMap<io.usethesource.vallang.type.Type,io.usethesource.vallang.type.Type> $typeBindings = new HashMap<>();
        if($T5.match(R_0.getType(), $typeBindings)){
           if(true){
              if($T3.match(S_1.getType(), $typeBindings)){
                 if(true){
                    final ISetWriter $setwriter9 = (ISetWriter)$RVF.setWriter();
                    ;
                    $SCOMP10_GEN1537:
                    for(IValue $elem11_for : ((ISet)R_0)){
                        IValue $elem11 = (IValue) $elem11_for;
                        final IValue $tuple_subject12 = ((IValue)($elem11));
                        if($tuple_subject12 instanceof ITuple && ((ITuple)$tuple_subject12).arity() == 3){
                           /*muExists*/$SCOMP10_GEN1537_TUPLE: 
                               do {
                                   if(true){
                                      IValue V0_2 = null;
                                      if(true){
                                         IValue V1_3 = null;
                                         if(true){
                                            IValue V2_4 = null;
                                            if((((IBool)($RVF.bool(((ISet)S_1).contains(((IValue)($subscript_int(((IValue)($tuple_subject12)),0)))))))).getValue()){
                                              if((((IBool)($RVF.bool(((ISet)S_1).contains(((IValue)($subscript_int(((IValue)($tuple_subject12)),1)))))))).getValue()){
                                                if((((IBool)($RVF.bool(((ISet)S_1).contains(((IValue)($subscript_int(((IValue)($tuple_subject12)),2)))))))).getValue()){
                                                  $setwriter9.insert($RVF.tuple(((IValue)($subscript_int(((IValue)($tuple_subject12)),0))), ((IValue)($subscript_int(((IValue)($tuple_subject12)),1))), ((IValue)($subscript_int(((IValue)($tuple_subject12)),2)))));
                                                
                                                } else {
                                                  continue $SCOMP10_GEN1537_TUPLE;
                                                }
                                              
                                              } else {
                                                continue $SCOMP10_GEN1537_TUPLE;
                                              }
                                            
                                            } else {
                                              continue $SCOMP10_GEN1537_TUPLE;
                                            }
                                         
                                         } else {
                                            continue $SCOMP10_GEN1537_TUPLE;/*computeFail*/
                                         }
                                      } else {
                                         continue $SCOMP10_GEN1537_TUPLE;/*computeFail*/
                                      }
                                   } else {
                                      continue $SCOMP10_GEN1537;
                                   }
                               } while(false);
                        
                        } else {
                           continue $SCOMP10_GEN1537;
                        }
                    }
                    
                                final ISet $result13 = ((ISet)($setwriter9.done()));
                    if($T31.instantiate($typeBindings) != $TF.voidType() && $isSubtypeOf($result13.getType(),$T31)){
                       return ((ISet)($result13));
                    
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
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Relation.rsc|(1597,171,<68,0>,<71,1>) 
    public ISet Relation_carrierR$52b35d289144910a(ISet R_0, ISet S_1){ 
        
        
        HashMap<io.usethesource.vallang.type.Type,io.usethesource.vallang.type.Type> $typeBindings = new HashMap<>();
        if($T6.match(R_0.getType(), $typeBindings)){
           if(true){
              if($T3.match(S_1.getType(), $typeBindings)){
                 if(true){
                    final ISetWriter $setwriter14 = (ISetWriter)$RVF.setWriter();
                    ;
                    $SCOMP15_GEN1694:
                    for(IValue $elem16_for : ((ISet)R_0)){
                        IValue $elem16 = (IValue) $elem16_for;
                        final IValue $tuple_subject17 = ((IValue)($elem16));
                        if($tuple_subject17 instanceof ITuple && ((ITuple)$tuple_subject17).arity() == 4){
                           /*muExists*/$SCOMP15_GEN1694_TUPLE: 
                               do {
                                   if(true){
                                      IValue V0_2 = null;
                                      if(true){
                                         IValue V1_3 = null;
                                         if(true){
                                            IValue V2_4 = null;
                                            if(true){
                                               IValue V3_5 = null;
                                               if((((IBool)($RVF.bool(((ISet)S_1).contains(((IValue)($subscript_int(((IValue)($tuple_subject17)),0)))))))).getValue()){
                                                 if((((IBool)($RVF.bool(((ISet)S_1).contains(((IValue)($subscript_int(((IValue)($tuple_subject17)),1)))))))).getValue()){
                                                   if((((IBool)($RVF.bool(((ISet)S_1).contains(((IValue)($subscript_int(((IValue)($tuple_subject17)),2)))))))).getValue()){
                                                     if((((IBool)($RVF.bool(((ISet)S_1).contains(((IValue)($subscript_int(((IValue)($tuple_subject17)),3)))))))).getValue()){
                                                       $setwriter14.insert($RVF.tuple(((IValue)($subscript_int(((IValue)($tuple_subject17)),0))), ((IValue)($subscript_int(((IValue)($tuple_subject17)),1))), ((IValue)($subscript_int(((IValue)($tuple_subject17)),2))), ((IValue)($subscript_int(((IValue)($tuple_subject17)),3)))));
                                                     
                                                     } else {
                                                       continue $SCOMP15_GEN1694_TUPLE;
                                                     }
                                                   
                                                   } else {
                                                     continue $SCOMP15_GEN1694_TUPLE;
                                                   }
                                                 
                                                 } else {
                                                   continue $SCOMP15_GEN1694_TUPLE;
                                                 }
                                               
                                               } else {
                                                 continue $SCOMP15_GEN1694_TUPLE;
                                               }
                                            
                                            } else {
                                               continue $SCOMP15_GEN1694_TUPLE;/*computeFail*/
                                            }
                                         } else {
                                            continue $SCOMP15_GEN1694_TUPLE;/*computeFail*/
                                         }
                                      } else {
                                         continue $SCOMP15_GEN1694_TUPLE;/*computeFail*/
                                      }
                                   } else {
                                      continue $SCOMP15_GEN1694;
                                   }
                               } while(false);
                        
                        } else {
                           continue $SCOMP15_GEN1694;
                        }
                    }
                    
                                final ISet $result18 = ((ISet)($setwriter14.done()));
                    if($T32.instantiate($typeBindings) != $TF.voidType() && $isSubtypeOf($result18.getType(),$T32)){
                       return ((ISet)($result18));
                    
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
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Relation.rsc|(1770,232,<73,0>,<77,1>) 
    public ISet Relation_carrierR$eb31e23e51d7fd68(ISet R_0, ISet S_1){ 
        
        
        HashMap<io.usethesource.vallang.type.Type,io.usethesource.vallang.type.Type> $typeBindings = new HashMap<>();
        if($T0.match(R_0.getType(), $typeBindings)){
           if(true){
              if($T3.match(S_1.getType(), $typeBindings)){
                 if(true){
                    final ISetWriter $setwriter19 = (ISetWriter)$RVF.setWriter();
                    ;
                    $SCOMP20_GEN1877:
                    for(IValue $elem21_for : ((ISet)R_0)){
                        IValue $elem21 = (IValue) $elem21_for;
                        final IValue $tuple_subject22 = ((IValue)($elem21));
                        if($tuple_subject22 instanceof ITuple && ((ITuple)$tuple_subject22).arity() == 5){
                           /*muExists*/$SCOMP20_GEN1877_TUPLE: 
                               do {
                                   if(true){
                                      IValue V0_2 = null;
                                      if(true){
                                         IValue V1_3 = null;
                                         if(true){
                                            IValue V2_4 = null;
                                            if(true){
                                               IValue V3_5 = null;
                                               if(true){
                                                  IValue V4_6 = null;
                                                  if((((IBool)($RVF.bool(((ISet)S_1).contains(((IValue)($subscript_int(((IValue)($tuple_subject22)),0)))))))).getValue()){
                                                    if((((IBool)($RVF.bool(((ISet)S_1).contains(((IValue)($subscript_int(((IValue)($tuple_subject22)),1)))))))).getValue()){
                                                      if((((IBool)($RVF.bool(((ISet)S_1).contains(((IValue)($subscript_int(((IValue)($tuple_subject22)),2)))))))).getValue()){
                                                        if((((IBool)($RVF.bool(((ISet)S_1).contains(((IValue)($subscript_int(((IValue)($tuple_subject22)),3)))))))).getValue()){
                                                          if((((IBool)($RVF.bool(((ISet)S_1).contains(((IValue)($subscript_int(((IValue)($tuple_subject22)),4)))))))).getValue()){
                                                            $setwriter19.insert($RVF.tuple(((IValue)($subscript_int(((IValue)($tuple_subject22)),0))), ((IValue)($subscript_int(((IValue)($tuple_subject22)),1))), ((IValue)($subscript_int(((IValue)($tuple_subject22)),2))), ((IValue)($subscript_int(((IValue)($tuple_subject22)),3))), ((IValue)($subscript_int(((IValue)($tuple_subject22)),4)))));
                                                          
                                                          } else {
                                                            continue $SCOMP20_GEN1877_TUPLE;
                                                          }
                                                        
                                                        } else {
                                                          continue $SCOMP20_GEN1877_TUPLE;
                                                        }
                                                      
                                                      } else {
                                                        continue $SCOMP20_GEN1877_TUPLE;
                                                      }
                                                    
                                                    } else {
                                                      continue $SCOMP20_GEN1877_TUPLE;
                                                    }
                                                  
                                                  } else {
                                                    continue $SCOMP20_GEN1877_TUPLE;
                                                  }
                                               
                                               } else {
                                                  continue $SCOMP20_GEN1877_TUPLE;/*computeFail*/
                                               }
                                            } else {
                                               continue $SCOMP20_GEN1877_TUPLE;/*computeFail*/
                                            }
                                         } else {
                                            continue $SCOMP20_GEN1877_TUPLE;/*computeFail*/
                                         }
                                      } else {
                                         continue $SCOMP20_GEN1877_TUPLE;/*computeFail*/
                                      }
                                   } else {
                                      continue $SCOMP20_GEN1877;
                                   }
                               } while(false);
                        
                        } else {
                           continue $SCOMP20_GEN1877;
                        }
                    }
                    
                                final ISet $result23 = ((ISet)($setwriter19.done()));
                    if($T33.instantiate($typeBindings) != $TF.voidType() && $isSubtypeOf($result23.getType(),$T33)){
                       return ((ISet)($result23));
                    
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
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Relation.rsc|(2005,545,<80,0>,<101,1>) 
    public ISet Relation_carrierX$5e8f4248c208f606(ISet R_0, ISet S_1){ 
        
        
        HashMap<io.usethesource.vallang.type.Type,io.usethesource.vallang.type.Type> $typeBindings = new HashMap<>();
        if($T4.match(R_0.getType(), $typeBindings)){
           if(true){
              if($T3.match(S_1.getType(), $typeBindings)){
                 if(true){
                    final ISetWriter $setwriter24 = (ISetWriter)$RVF.setWriter();
                    ;
                    $SCOMP25_GEN2502:
                    for(IValue $elem26_for : ((ISet)R_0)){
                        IValue $elem26 = (IValue) $elem26_for;
                        final IValue $tuple_subject27 = ((IValue)($elem26));
                        if($tuple_subject27 instanceof ITuple && ((ITuple)$tuple_subject27).arity() == 2){
                           /*muExists*/$SCOMP25_GEN2502_TUPLE: 
                               do {
                                   if(true){
                                      IValue V0_2 = null;
                                      if(true){
                                         IValue V1_3 = null;
                                         if((((IBool)($RVF.bool(!(((ISet)S_1)).contains(((IValue)($subscript_int(((IValue)($tuple_subject27)),0)))))))).getValue()){
                                           if((((IBool)($RVF.bool(!(((ISet)S_1)).contains(((IValue)($subscript_int(((IValue)($tuple_subject27)),1)))))))).getValue()){
                                             $setwriter24.insert($RVF.tuple(((IValue)($subscript_int(((IValue)($tuple_subject27)),0))), ((IValue)($subscript_int(((IValue)($tuple_subject27)),1)))));
                                           
                                           } else {
                                             continue $SCOMP25_GEN2502_TUPLE;
                                           }
                                         
                                         } else {
                                           continue $SCOMP25_GEN2502_TUPLE;
                                         }
                                      
                                      } else {
                                         continue $SCOMP25_GEN2502_TUPLE;/*computeFail*/
                                      }
                                   } else {
                                      continue $SCOMP25_GEN2502;
                                   }
                               } while(false);
                        
                        } else {
                           continue $SCOMP25_GEN2502;
                        }
                    }
                    
                                final ISet $result28 = ((ISet)($setwriter24.done()));
                    if($T30.instantiate($typeBindings) != $TF.voidType() && $isSubtypeOf($result28.getType(),$T30)){
                       return ((ISet)($result28));
                    
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
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Relation.rsc|(2552,154,<103,0>,<106,1>) 
    public ISet Relation_carrierX$7005b62f0e805b5f(ISet R_0, ISet S_1){ 
        
        
        HashMap<io.usethesource.vallang.type.Type,io.usethesource.vallang.type.Type> $typeBindings = new HashMap<>();
        if($T5.match(R_0.getType(), $typeBindings)){
           if(true){
              if($T3.match(S_1.getType(), $typeBindings)){
                 if(true){
                    final ISetWriter $setwriter29 = (ISetWriter)$RVF.setWriter();
                    ;
                    $SCOMP30_GEN2639:
                    for(IValue $elem31_for : ((ISet)R_0)){
                        IValue $elem31 = (IValue) $elem31_for;
                        final IValue $tuple_subject32 = ((IValue)($elem31));
                        if($tuple_subject32 instanceof ITuple && ((ITuple)$tuple_subject32).arity() == 3){
                           /*muExists*/$SCOMP30_GEN2639_TUPLE: 
                               do {
                                   if(true){
                                      IValue V0_2 = null;
                                      if(true){
                                         IValue V1_3 = null;
                                         if(true){
                                            IValue V2_4 = null;
                                            if((((IBool)($RVF.bool(!(((ISet)S_1)).contains(((IValue)($subscript_int(((IValue)($tuple_subject32)),0)))))))).getValue()){
                                              if((((IBool)($RVF.bool(!(((ISet)S_1)).contains(((IValue)($subscript_int(((IValue)($tuple_subject32)),1)))))))).getValue()){
                                                if((((IBool)($RVF.bool(!(((ISet)S_1)).contains(((IValue)($subscript_int(((IValue)($tuple_subject32)),2)))))))).getValue()){
                                                  $setwriter29.insert($RVF.tuple(((IValue)($subscript_int(((IValue)($tuple_subject32)),0))), ((IValue)($subscript_int(((IValue)($tuple_subject32)),1))), ((IValue)($subscript_int(((IValue)($tuple_subject32)),2)))));
                                                
                                                } else {
                                                  continue $SCOMP30_GEN2639_TUPLE;
                                                }
                                              
                                              } else {
                                                continue $SCOMP30_GEN2639_TUPLE;
                                              }
                                            
                                            } else {
                                              continue $SCOMP30_GEN2639_TUPLE;
                                            }
                                         
                                         } else {
                                            continue $SCOMP30_GEN2639_TUPLE;/*computeFail*/
                                         }
                                      } else {
                                         continue $SCOMP30_GEN2639_TUPLE;/*computeFail*/
                                      }
                                   } else {
                                      continue $SCOMP30_GEN2639;
                                   }
                               } while(false);
                        
                        } else {
                           continue $SCOMP30_GEN2639;
                        }
                    }
                    
                                final ISet $result33 = ((ISet)($setwriter29.done()));
                    if($T31.instantiate($typeBindings) != $TF.voidType() && $isSubtypeOf($result33.getType(),$T31)){
                       return ((ISet)($result33));
                    
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
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Relation.rsc|(2708,183,<108,0>,<111,1>) 
    public ISet Relation_carrierX$c8228c40b970c38b(ISet R_0, ISet S_1){ 
        
        
        HashMap<io.usethesource.vallang.type.Type,io.usethesource.vallang.type.Type> $typeBindings = new HashMap<>();
        if($T6.match(R_0.getType(), $typeBindings)){
           if(true){
              if($T3.match(S_1.getType(), $typeBindings)){
                 if(true){
                    final ISetWriter $setwriter34 = (ISetWriter)$RVF.setWriter();
                    ;
                    $SCOMP35_GEN2805:
                    for(IValue $elem36_for : ((ISet)R_0)){
                        IValue $elem36 = (IValue) $elem36_for;
                        final IValue $tuple_subject37 = ((IValue)($elem36));
                        if($tuple_subject37 instanceof ITuple && ((ITuple)$tuple_subject37).arity() == 4){
                           /*muExists*/$SCOMP35_GEN2805_TUPLE: 
                               do {
                                   if(true){
                                      IValue V0_2 = null;
                                      if(true){
                                         IValue V1_3 = null;
                                         if(true){
                                            IValue V2_4 = null;
                                            if(true){
                                               IValue V3_5 = null;
                                               if((((IBool)($RVF.bool(!(((ISet)S_1)).contains(((IValue)($subscript_int(((IValue)($tuple_subject37)),0)))))))).getValue()){
                                                 if((((IBool)($RVF.bool(!(((ISet)S_1)).contains(((IValue)($subscript_int(((IValue)($tuple_subject37)),1)))))))).getValue()){
                                                   if((((IBool)($RVF.bool(!(((ISet)S_1)).contains(((IValue)($subscript_int(((IValue)($tuple_subject37)),2)))))))).getValue()){
                                                     if((((IBool)($RVF.bool(!(((ISet)S_1)).contains(((IValue)($subscript_int(((IValue)($tuple_subject37)),3)))))))).getValue()){
                                                       $setwriter34.insert($RVF.tuple(((IValue)($subscript_int(((IValue)($tuple_subject37)),0))), ((IValue)($subscript_int(((IValue)($tuple_subject37)),1))), ((IValue)($subscript_int(((IValue)($tuple_subject37)),2))), ((IValue)($subscript_int(((IValue)($tuple_subject37)),3)))));
                                                     
                                                     } else {
                                                       continue $SCOMP35_GEN2805_TUPLE;
                                                     }
                                                   
                                                   } else {
                                                     continue $SCOMP35_GEN2805_TUPLE;
                                                   }
                                                 
                                                 } else {
                                                   continue $SCOMP35_GEN2805_TUPLE;
                                                 }
                                               
                                               } else {
                                                 continue $SCOMP35_GEN2805_TUPLE;
                                               }
                                            
                                            } else {
                                               continue $SCOMP35_GEN2805_TUPLE;/*computeFail*/
                                            }
                                         } else {
                                            continue $SCOMP35_GEN2805_TUPLE;/*computeFail*/
                                         }
                                      } else {
                                         continue $SCOMP35_GEN2805_TUPLE;/*computeFail*/
                                      }
                                   } else {
                                      continue $SCOMP35_GEN2805;
                                   }
                               } while(false);
                        
                        } else {
                           continue $SCOMP35_GEN2805;
                        }
                    }
                    
                                final ISet $result38 = ((ISet)($setwriter34.done()));
                    if($T32.instantiate($typeBindings) != $TF.voidType() && $isSubtypeOf($result38.getType(),$T32)){
                       return ((ISet)($result38));
                    
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
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Relation.rsc|(2893,247,<113,0>,<117,1>) 
    public ISet Relation_carrierX$dc7d4f1475a31c1a(ISet R_0, ISet S_1){ 
        
        
        HashMap<io.usethesource.vallang.type.Type,io.usethesource.vallang.type.Type> $typeBindings = new HashMap<>();
        if($T0.match(R_0.getType(), $typeBindings)){
           if(true){
              if($T3.match(S_1.getType(), $typeBindings)){
                 if(true){
                    final ISetWriter $setwriter39 = (ISetWriter)$RVF.setWriter();
                    ;
                    $SCOMP40_GEN3000:
                    for(IValue $elem41_for : ((ISet)R_0)){
                        IValue $elem41 = (IValue) $elem41_for;
                        final IValue $tuple_subject42 = ((IValue)($elem41));
                        if($tuple_subject42 instanceof ITuple && ((ITuple)$tuple_subject42).arity() == 5){
                           /*muExists*/$SCOMP40_GEN3000_TUPLE: 
                               do {
                                   if(true){
                                      IValue V0_2 = null;
                                      if(true){
                                         IValue V1_3 = null;
                                         if(true){
                                            IValue V2_4 = null;
                                            if(true){
                                               IValue V3_5 = null;
                                               if(true){
                                                  IValue V4_6 = null;
                                                  if((((IBool)($RVF.bool(!(((ISet)S_1)).contains(((IValue)($subscript_int(((IValue)($tuple_subject42)),0)))))))).getValue()){
                                                    if((((IBool)($RVF.bool(!(((ISet)S_1)).contains(((IValue)($subscript_int(((IValue)($tuple_subject42)),1)))))))).getValue()){
                                                      if((((IBool)($RVF.bool(!(((ISet)S_1)).contains(((IValue)($subscript_int(((IValue)($tuple_subject42)),2)))))))).getValue()){
                                                        if((((IBool)($RVF.bool(!(((ISet)S_1)).contains(((IValue)($subscript_int(((IValue)($tuple_subject42)),3)))))))).getValue()){
                                                          if((((IBool)($RVF.bool(!(((ISet)S_1)).contains(((IValue)($subscript_int(((IValue)($tuple_subject42)),4)))))))).getValue()){
                                                            $setwriter39.insert($RVF.tuple(((IValue)($subscript_int(((IValue)($tuple_subject42)),0))), ((IValue)($subscript_int(((IValue)($tuple_subject42)),1))), ((IValue)($subscript_int(((IValue)($tuple_subject42)),2))), ((IValue)($subscript_int(((IValue)($tuple_subject42)),3))), ((IValue)($subscript_int(((IValue)($tuple_subject42)),4)))));
                                                          
                                                          } else {
                                                            continue $SCOMP40_GEN3000_TUPLE;
                                                          }
                                                        
                                                        } else {
                                                          continue $SCOMP40_GEN3000_TUPLE;
                                                        }
                                                      
                                                      } else {
                                                        continue $SCOMP40_GEN3000_TUPLE;
                                                      }
                                                    
                                                    } else {
                                                      continue $SCOMP40_GEN3000_TUPLE;
                                                    }
                                                  
                                                  } else {
                                                    continue $SCOMP40_GEN3000_TUPLE;
                                                  }
                                               
                                               } else {
                                                  continue $SCOMP40_GEN3000_TUPLE;/*computeFail*/
                                               }
                                            } else {
                                               continue $SCOMP40_GEN3000_TUPLE;/*computeFail*/
                                            }
                                         } else {
                                            continue $SCOMP40_GEN3000_TUPLE;/*computeFail*/
                                         }
                                      } else {
                                         continue $SCOMP40_GEN3000_TUPLE;/*computeFail*/
                                      }
                                   } else {
                                      continue $SCOMP40_GEN3000;
                                   }
                               } while(false);
                        
                        } else {
                           continue $SCOMP40_GEN3000;
                        }
                    }
                    
                                final ISet $result43 = ((ISet)($setwriter39.done()));
                    if($T33.instantiate($typeBindings) != $TF.voidType() && $isSubtypeOf($result43.getType(),$T33)){
                       return ((ISet)($result43));
                    
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
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Relation.rsc|(3143,713,<120,0>,<144,1>) 
    public ISet Relation_complement$4bb4b4dc0b4215a5(ISet R_0){ 
        
        
        HashMap<io.usethesource.vallang.type.Type,io.usethesource.vallang.type.Type> $typeBindings = new HashMap<>();
        if($T16.match(R_0.getType(), $typeBindings)){
           final ISet $result44 = ((ISet)(((ISet)($aset_product_aset(((ISet)($me.domain(((ISet)R_0)))),((ISet)($me.range(((ISet)R_0))))))).subtract(((ISet)R_0))));
           if($T34.instantiate($typeBindings) != $TF.voidType() && $isSubtypeOf($result44.getType(),$T34)){
              return ((ISet)($result44));
           
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Relation.rsc|(3858,159,<146,0>,<149,1>) 
    public ISet Relation_complement$00086bfeeba07066(ISet R_0){ 
        
        
        HashMap<io.usethesource.vallang.type.Type,io.usethesource.vallang.type.Type> $typeBindings = new HashMap<>();
        if($T17.match(R_0.getType(), $typeBindings)){
           if(true){
              final ISetWriter $setwriter45 = (ISetWriter)$RVF.setWriter();
              ;
              $SCOMP46_GEN3944:
              for(IValue $elem49_for : ((ISet)($arel_field_project((ISet)((ISet)R_0), ((IInteger)$constants.get(0)/*0*/))))){
                  IValue $elem49 = (IValue) $elem49_for;
                  if($isSubtypeOf($elem49.getType(),$T35.instantiate($typeBindings))){
                     IValue V0_1 = null;
                     $SCOMP46_GEN3944_GEN3960:
                     for(IValue $elem48_for : ((ISet)($arel_field_project((ISet)((ISet)R_0), ((IInteger)$constants.get(1)/*1*/))))){
                         IValue $elem48 = (IValue) $elem48_for;
                         if($isSubtypeOf($elem48.getType(),$T36.instantiate($typeBindings))){
                            IValue V1_2 = null;
                            $SCOMP46_GEN3944_GEN3960_GEN3977:
                            for(IValue $elem47_for : ((ISet)($arel_field_project((ISet)((ISet)R_0), ((IInteger)$constants.get(2)/*2*/))))){
                                IValue $elem47 = (IValue) $elem47_for;
                                if($isSubtypeOf($elem47.getType(),$T37.instantiate($typeBindings))){
                                   IValue V2_3 = null;
                                   if((((IBool)($RVF.bool(!(((ISet)R_0)).contains(((ITuple)($RVF.tuple(((IValue)($elem49)), ((IValue)($elem48)), ((IValue)($elem47)))))))))).getValue()){
                                     $setwriter45.insert($RVF.tuple(((IValue)($elem49)), ((IValue)($elem48)), ((IValue)($elem47))));
                                   
                                   } else {
                                     continue $SCOMP46_GEN3944_GEN3960_GEN3977;
                                   }
                                
                                } else {
                                   continue $SCOMP46_GEN3944_GEN3960_GEN3977;
                                }
                            }
                            continue $SCOMP46_GEN3944_GEN3960;
                                        
                         } else {
                            continue $SCOMP46_GEN3944_GEN3960;
                         }
                     }
                     continue $SCOMP46_GEN3944;
                                 
                  } else {
                     continue $SCOMP46_GEN3944;
                  }
              }
              
                          final ISet $result50 = ((ISet)($setwriter45.done()));
              if($T38.instantiate($typeBindings) != $TF.voidType() && $isSubtypeOf($result50.getType(),$T38)){
                 return ((ISet)($result50));
              
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
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Relation.rsc|(4019,193,<151,0>,<154,1>) 
    public ISet Relation_complement$1dc10ce8d46ef909(ISet R_0){ 
        
        
        HashMap<io.usethesource.vallang.type.Type,io.usethesource.vallang.type.Type> $typeBindings = new HashMap<>();
        if($T18.match(R_0.getType(), $typeBindings)){
           if(true){
              final ISetWriter $setwriter51 = (ISetWriter)$RVF.setWriter();
              ;
              $SCOMP52_GEN4119:
              for(IValue $elem56_for : ((ISet)($arel_field_project((ISet)((ISet)R_0), ((IInteger)$constants.get(0)/*0*/))))){
                  IValue $elem56 = (IValue) $elem56_for;
                  if($isSubtypeOf($elem56.getType(),$T35.instantiate($typeBindings))){
                     IValue V0_1 = null;
                     $SCOMP52_GEN4119_GEN4135:
                     for(IValue $elem55_for : ((ISet)($arel_field_project((ISet)((ISet)R_0), ((IInteger)$constants.get(1)/*1*/))))){
                         IValue $elem55 = (IValue) $elem55_for;
                         if($isSubtypeOf($elem55.getType(),$T36.instantiate($typeBindings))){
                            IValue V1_2 = null;
                            $SCOMP52_GEN4119_GEN4135_GEN4152:
                            for(IValue $elem54_for : ((ISet)($arel_field_project((ISet)((ISet)R_0), ((IInteger)$constants.get(2)/*2*/))))){
                                IValue $elem54 = (IValue) $elem54_for;
                                if($isSubtypeOf($elem54.getType(),$T37.instantiate($typeBindings))){
                                   IValue V2_3 = null;
                                   $SCOMP52_GEN4119_GEN4135_GEN4152_GEN4168:
                                   for(IValue $elem53_for : ((ISet)($arel_field_project((ISet)((ISet)R_0), ((IInteger)$constants.get(3)/*3*/))))){
                                       IValue $elem53 = (IValue) $elem53_for;
                                       if($isSubtypeOf($elem53.getType(),$T39.instantiate($typeBindings))){
                                          IValue V3_4 = null;
                                          if((((IBool)($RVF.bool(!(((ISet)R_0)).contains(((ITuple)($RVF.tuple(((IValue)($elem56)), ((IValue)($elem55)), ((IValue)($elem54)), ((IValue)($elem53)))))))))).getValue()){
                                            $setwriter51.insert($RVF.tuple(((IValue)($elem56)), ((IValue)($elem55)), ((IValue)($elem54)), ((IValue)($elem53))));
                                          
                                          } else {
                                            continue $SCOMP52_GEN4119_GEN4135_GEN4152_GEN4168;
                                          }
                                       
                                       } else {
                                          continue $SCOMP52_GEN4119_GEN4135_GEN4152_GEN4168;
                                       }
                                   }
                                   continue $SCOMP52_GEN4119_GEN4135_GEN4152;
                                               
                                } else {
                                   continue $SCOMP52_GEN4119_GEN4135_GEN4152;
                                }
                            }
                            continue $SCOMP52_GEN4119_GEN4135;
                                        
                         } else {
                            continue $SCOMP52_GEN4119_GEN4135;
                         }
                     }
                     continue $SCOMP52_GEN4119;
                                 
                  } else {
                     continue $SCOMP52_GEN4119;
                  }
              }
              
                          final ISet $result57 = ((ISet)($setwriter51.done()));
              if($T40.instantiate($typeBindings) != $TF.voidType() && $isSubtypeOf($result57.getType(),$T40)){
                 return ((ISet)($result57));
              
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
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Relation.rsc|(4214,261,<156,0>,<160,1>) 
    public ISet Relation_complement$56d58cd1d8429e72(ISet R_0){ 
        
        
        HashMap<io.usethesource.vallang.type.Type,io.usethesource.vallang.type.Type> $typeBindings = new HashMap<>();
        if($T10.match(R_0.getType(), $typeBindings)){
           if(true){
              final ISetWriter $setwriter58 = (ISetWriter)$RVF.setWriter();
              ;
              $SCOMP59_GEN4328:
              for(IValue $elem64_for : ((ISet)($arel_field_project((ISet)((ISet)R_0), ((IInteger)$constants.get(0)/*0*/))))){
                  IValue $elem64 = (IValue) $elem64_for;
                  if($isSubtypeOf($elem64.getType(),$T35.instantiate($typeBindings))){
                     IValue V0_1 = null;
                     $SCOMP59_GEN4328_GEN4344:
                     for(IValue $elem63_for : ((ISet)($arel_field_project((ISet)((ISet)R_0), ((IInteger)$constants.get(1)/*1*/))))){
                         IValue $elem63 = (IValue) $elem63_for;
                         if($isSubtypeOf($elem63.getType(),$T36.instantiate($typeBindings))){
                            IValue V1_2 = null;
                            $SCOMP59_GEN4328_GEN4344_GEN4361:
                            for(IValue $elem62_for : ((ISet)($arel_field_project((ISet)((ISet)R_0), ((IInteger)$constants.get(2)/*2*/))))){
                                IValue $elem62 = (IValue) $elem62_for;
                                if($isSubtypeOf($elem62.getType(),$T37.instantiate($typeBindings))){
                                   IValue V2_3 = null;
                                   $SCOMP59_GEN4328_GEN4344_GEN4361_GEN4377:
                                   for(IValue $elem61_for : ((ISet)($arel_field_project((ISet)((ISet)R_0), ((IInteger)$constants.get(3)/*3*/))))){
                                       IValue $elem61 = (IValue) $elem61_for;
                                       if($isSubtypeOf($elem61.getType(),$T39.instantiate($typeBindings))){
                                          IValue V3_4 = null;
                                          $SCOMP59_GEN4328_GEN4344_GEN4361_GEN4377_GEN4427:
                                          for(IValue $elem60_for : ((ISet)($arel_field_project((ISet)((ISet)R_0), ((IInteger)$constants.get(4)/*4*/))))){
                                              IValue $elem60 = (IValue) $elem60_for;
                                              if($isSubtypeOf($elem60.getType(),$T41.instantiate($typeBindings))){
                                                 IValue V4_5 = null;
                                                 if((((IBool)($RVF.bool(!(((ISet)R_0)).contains(((ITuple)($RVF.tuple(((IValue)($elem64)), ((IValue)($elem63)), ((IValue)($elem62)), ((IValue)($elem61)), ((IValue)($elem60)))))))))).getValue()){
                                                   $setwriter58.insert($RVF.tuple(((IValue)($elem64)), ((IValue)($elem63)), ((IValue)($elem62)), ((IValue)($elem61)), ((IValue)($elem60))));
                                                 
                                                 } else {
                                                   continue $SCOMP59_GEN4328_GEN4344_GEN4361_GEN4377_GEN4427;
                                                 }
                                              
                                              } else {
                                                 continue $SCOMP59_GEN4328_GEN4344_GEN4361_GEN4377_GEN4427;
                                              }
                                          }
                                          continue $SCOMP59_GEN4328_GEN4344_GEN4361_GEN4377;
                                                      
                                       } else {
                                          continue $SCOMP59_GEN4328_GEN4344_GEN4361_GEN4377;
                                       }
                                   }
                                   continue $SCOMP59_GEN4328_GEN4344_GEN4361;
                                               
                                } else {
                                   continue $SCOMP59_GEN4328_GEN4344_GEN4361;
                                }
                            }
                            continue $SCOMP59_GEN4328_GEN4344;
                                        
                         } else {
                            continue $SCOMP59_GEN4328_GEN4344;
                         }
                     }
                     continue $SCOMP59_GEN4328;
                                 
                  } else {
                     continue $SCOMP59_GEN4328;
                  }
              }
              
                          final ISet $result65 = ((ISet)($setwriter58.done()));
              if($T42.instantiate($typeBindings) != $TF.voidType() && $isSubtypeOf($result65.getType(),$T42)){
                 return ((ISet)($result65));
              
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
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Relation.rsc|(4478,255,<163,0>,<174,1>) 
    public ISet Relation_domain$82c6d674586e8686(ISet R_0){ 
        
        
        HashMap<io.usethesource.vallang.type.Type,io.usethesource.vallang.type.Type> $typeBindings = new HashMap<>();
        if($T16.match(R_0.getType(), $typeBindings)){
           final ISet $result66 = ((ISet)($arel_field_project((ISet)((ISet)R_0), ((IInteger)$constants.get(0)/*0*/))));
           if($T43.instantiate($typeBindings) != $TF.voidType() && $isSubtypeOf($result66.getType(),$T43)){
              return ((ISet)($result66));
           
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Relation.rsc|(4735,64,<176,0>,<179,3>) 
    public ISet Relation_domain$2a41e81f2232a510(ISet R_0){ 
        
        
        HashMap<io.usethesource.vallang.type.Type,io.usethesource.vallang.type.Type> $typeBindings = new HashMap<>();
        if($T17.match(R_0.getType(), $typeBindings)){
           final ISet $result67 = ((ISet)($arel_field_project((ISet)((ISet)R_0), ((IInteger)$constants.get(0)/*0*/))));
           if($T43.instantiate($typeBindings) != $TF.voidType() && $isSubtypeOf($result67.getType(),$T43)){
              return ((ISet)($result67));
           
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Relation.rsc|(4801,66,<181,0>,<184,1>) 
    public ISet Relation_domain$ceb529b2fc10d7d4(ISet R_0){ 
        
        
        HashMap<io.usethesource.vallang.type.Type,io.usethesource.vallang.type.Type> $typeBindings = new HashMap<>();
        if($T18.match(R_0.getType(), $typeBindings)){
           final ISet $result68 = ((ISet)($arel_field_project((ISet)((ISet)R_0), ((IInteger)$constants.get(0)/*0*/))));
           if($T43.instantiate($typeBindings) != $TF.voidType() && $isSubtypeOf($result68.getType(),$T43)){
              return ((ISet)($result68));
           
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Relation.rsc|(4869,70,<186,0>,<189,1>) 
    public ISet Relation_domain$7e7cc8be3201741e(ISet R_0){ 
        
        
        HashMap<io.usethesource.vallang.type.Type,io.usethesource.vallang.type.Type> $typeBindings = new HashMap<>();
        if($T10.match(R_0.getType(), $typeBindings)){
           final ISet $result69 = ((ISet)($arel_field_project((ISet)((ISet)R_0), ((IInteger)$constants.get(0)/*0*/))));
           if($T43.instantiate($typeBindings) != $TF.voidType() && $isSubtypeOf($result69.getType(),$T43)){
              return ((ISet)($result69));
           
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Relation.rsc|(4942,351,<192,0>,<205,1>) 
    public ISet Relation_domainR$6df70016a64d6b6b(ISet R_0, ISet S_1){ 
        
        
        HashMap<io.usethesource.vallang.type.Type,io.usethesource.vallang.type.Type> $typeBindings = new HashMap<>();
        if($T16.match(R_0.getType(), $typeBindings)){
           if(true){
              if($T19.match(S_1.getType(), $typeBindings)){
                 if(true){
                    final ISetWriter $setwriter70 = (ISetWriter)$RVF.setWriter();
                    ;
                    $SCOMP71_GEN5258:
                    for(IValue $elem72_for : ((ISet)R_0)){
                        IValue $elem72 = (IValue) $elem72_for;
                        final IValue $tuple_subject73 = ((IValue)($elem72));
                        if($tuple_subject73 instanceof ITuple && ((ITuple)$tuple_subject73).arity() == 2){
                           /*muExists*/$SCOMP71_GEN5258_TUPLE: 
                               do {
                                   if(true){
                                      IValue V0_2 = null;
                                      if(true){
                                         IValue V1_3 = null;
                                         if((((IBool)($RVF.bool(((ISet)S_1).contains(((IValue)($subscript_int(((IValue)($tuple_subject73)),0)))))))).getValue()){
                                           $setwriter70.insert($RVF.tuple(((IValue)($subscript_int(((IValue)($tuple_subject73)),0))), ((IValue)($subscript_int(((IValue)($tuple_subject73)),1)))));
                                         
                                         } else {
                                           continue $SCOMP71_GEN5258_TUPLE;
                                         }
                                      
                                      } else {
                                         continue $SCOMP71_GEN5258_TUPLE;/*computeFail*/
                                      }
                                   } else {
                                      continue $SCOMP71_GEN5258;
                                   }
                               } while(false);
                        
                        } else {
                           continue $SCOMP71_GEN5258;
                        }
                    }
                    
                                final ISet $result74 = ((ISet)($setwriter70.done()));
                    if($T34.instantiate($typeBindings) != $TF.voidType() && $isSubtypeOf($result74.getType(),$T34)){
                       return ((ISet)($result74));
                    
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
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Relation.rsc|(5295,89,<207,0>,<208,37>) 
    public ISet Relation_domainR$c52c9514cb94dac5(ISet R_0, TypedFunctionInstance1<IValue, IValue> accept_1){ 
        
        
        HashMap<io.usethesource.vallang.type.Type,io.usethesource.vallang.type.Type> $typeBindings = new HashMap<>();
        if($T20.match(R_0.getType(), $typeBindings)){
           if(true){
              if($T22.match(accept_1.getType(), $typeBindings)){
                 if(true){
                    final ISetWriter $setwriter75 = (ISetWriter)$RVF.setWriter();
                    ;
                    $SCOMP76_GEN5361:
                    for(IValue $elem77_for : ((ISet)R_0)){
                        IValue $elem77 = (IValue) $elem77_for;
                        final IValue $tuple_subject78 = ((IValue)($elem77));
                        if($tuple_subject78 instanceof ITuple && ((ITuple)$tuple_subject78).arity() == 2){
                           /*muExists*/$SCOMP76_GEN5361_TUPLE: 
                               do {
                                   IValue t_2 = null;
                                   IValue u_3 = null;
                                   if((((IBool)(((TypedFunctionInstance1<IValue, IValue>)accept_1).typedCall(((IValue)($subscript_int(((IValue)($tuple_subject78)),0))))))).getValue()){
                                     $setwriter75.insert($RVF.tuple(((IValue)($subscript_int(((IValue)($tuple_subject78)),0))), ((IValue)($subscript_int(((IValue)($tuple_subject78)),1)))));
                                   
                                   } else {
                                     continue $SCOMP76_GEN5361_TUPLE;
                                   }
                           
                               } while(false);
                        
                        } else {
                           continue $SCOMP76_GEN5361;
                        }
                    }
                    
                                final ISet $result79 = ((ISet)($setwriter75.done()));
                    if($T44.instantiate($typeBindings) != $TF.voidType() && $isSubtypeOf($result79.getType(),$T44)){
                       return ((ISet)($result79));
                    
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
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Relation.rsc|(5386,136,<210,0>,<213,1>) 
    public ISet Relation_domainR$acdf919373377fa5(ISet R_0, ISet S_1){ 
        
        
        HashMap<io.usethesource.vallang.type.Type,io.usethesource.vallang.type.Type> $typeBindings = new HashMap<>();
        if($T17.match(R_0.getType(), $typeBindings)){
           if(true){
              if($T19.match(S_1.getType(), $typeBindings)){
                 if(true){
                    final ISetWriter $setwriter80 = (ISetWriter)$RVF.setWriter();
                    ;
                    $SCOMP81_GEN5479:
                    for(IValue $elem82_for : ((ISet)R_0)){
                        IValue $elem82 = (IValue) $elem82_for;
                        final IValue $tuple_subject83 = ((IValue)($elem82));
                        if($tuple_subject83 instanceof ITuple && ((ITuple)$tuple_subject83).arity() == 3){
                           /*muExists*/$SCOMP81_GEN5479_TUPLE: 
                               do {
                                   if(true){
                                      IValue V0_2 = null;
                                      if(true){
                                         IValue V1_3 = null;
                                         if(true){
                                            IValue V2_4 = null;
                                            if((((IBool)($RVF.bool(((ISet)S_1).contains(((IValue)($subscript_int(((IValue)($tuple_subject83)),0)))))))).getValue()){
                                              $setwriter80.insert($RVF.tuple(((IValue)($subscript_int(((IValue)($tuple_subject83)),0))), ((IValue)($subscript_int(((IValue)($tuple_subject83)),1))), ((IValue)($subscript_int(((IValue)($tuple_subject83)),2)))));
                                            
                                            } else {
                                              continue $SCOMP81_GEN5479_TUPLE;
                                            }
                                         
                                         } else {
                                            continue $SCOMP81_GEN5479_TUPLE;/*computeFail*/
                                         }
                                      } else {
                                         continue $SCOMP81_GEN5479_TUPLE;/*computeFail*/
                                      }
                                   } else {
                                      continue $SCOMP81_GEN5479;
                                   }
                               } while(false);
                        
                        } else {
                           continue $SCOMP81_GEN5479;
                        }
                    }
                    
                                final ISet $result84 = ((ISet)($setwriter80.done()));
                    if($T38.instantiate($typeBindings) != $TF.voidType() && $isSubtypeOf($result84.getType(),$T38)){
                       return ((ISet)($result84));
                    
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
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Relation.rsc|(5524,156,<215,0>,<218,1>) 
    public ISet Relation_domainR$4e560a92f3102f3a(ISet R_0, ISet S_1){ 
        
        
        HashMap<io.usethesource.vallang.type.Type,io.usethesource.vallang.type.Type> $typeBindings = new HashMap<>();
        if($T18.match(R_0.getType(), $typeBindings)){
           if(true){
              if($T19.match(S_1.getType(), $typeBindings)){
                 if(true){
                    final ISetWriter $setwriter85 = (ISetWriter)$RVF.setWriter();
                    ;
                    $SCOMP86_GEN5629:
                    for(IValue $elem87_for : ((ISet)R_0)){
                        IValue $elem87 = (IValue) $elem87_for;
                        final IValue $tuple_subject88 = ((IValue)($elem87));
                        if($tuple_subject88 instanceof ITuple && ((ITuple)$tuple_subject88).arity() == 4){
                           /*muExists*/$SCOMP86_GEN5629_TUPLE: 
                               do {
                                   if(true){
                                      IValue V0_2 = null;
                                      if(true){
                                         IValue V1_3 = null;
                                         if(true){
                                            IValue V2_4 = null;
                                            if(true){
                                               IValue V3_5 = null;
                                               if((((IBool)($RVF.bool(((ISet)S_1).contains(((IValue)($subscript_int(((IValue)($tuple_subject88)),0)))))))).getValue()){
                                                 $setwriter85.insert($RVF.tuple(((IValue)($subscript_int(((IValue)($tuple_subject88)),0))), ((IValue)($subscript_int(((IValue)($tuple_subject88)),1))), ((IValue)($subscript_int(((IValue)($tuple_subject88)),2))), ((IValue)($subscript_int(((IValue)($tuple_subject88)),3)))));
                                               
                                               } else {
                                                 continue $SCOMP86_GEN5629_TUPLE;
                                               }
                                            
                                            } else {
                                               continue $SCOMP86_GEN5629_TUPLE;/*computeFail*/
                                            }
                                         } else {
                                            continue $SCOMP86_GEN5629_TUPLE;/*computeFail*/
                                         }
                                      } else {
                                         continue $SCOMP86_GEN5629_TUPLE;/*computeFail*/
                                      }
                                   } else {
                                      continue $SCOMP86_GEN5629;
                                   }
                               } while(false);
                        
                        } else {
                           continue $SCOMP86_GEN5629;
                        }
                    }
                    
                                final ISet $result89 = ((ISet)($setwriter85.done()));
                    if($T40.instantiate($typeBindings) != $TF.voidType() && $isSubtypeOf($result89.getType(),$T40)){
                       return ((ISet)($result89));
                    
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
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Relation.rsc|(5682,176,<220,0>,<223,1>) 
    public ISet Relation_domainR$c0b08c3c279262a0(ISet R_0, ISet S_1){ 
        
        
        HashMap<io.usethesource.vallang.type.Type,io.usethesource.vallang.type.Type> $typeBindings = new HashMap<>();
        if($T10.match(R_0.getType(), $typeBindings)){
           if(true){
              if($T19.match(S_1.getType(), $typeBindings)){
                 if(true){
                    final ISetWriter $setwriter90 = (ISetWriter)$RVF.setWriter();
                    ;
                    $SCOMP91_GEN5799:
                    for(IValue $elem92_for : ((ISet)R_0)){
                        IValue $elem92 = (IValue) $elem92_for;
                        final IValue $tuple_subject93 = ((IValue)($elem92));
                        if($tuple_subject93 instanceof ITuple && ((ITuple)$tuple_subject93).arity() == 5){
                           /*muExists*/$SCOMP91_GEN5799_TUPLE: 
                               do {
                                   if(true){
                                      IValue V0_2 = null;
                                      if(true){
                                         IValue V1_3 = null;
                                         if(true){
                                            IValue V2_4 = null;
                                            if(true){
                                               IValue V3_5 = null;
                                               if(true){
                                                  IValue V4_6 = null;
                                                  if((((IBool)($RVF.bool(((ISet)S_1).contains(((IValue)($subscript_int(((IValue)($tuple_subject93)),0)))))))).getValue()){
                                                    $setwriter90.insert($RVF.tuple(((IValue)($subscript_int(((IValue)($tuple_subject93)),0))), ((IValue)($subscript_int(((IValue)($tuple_subject93)),1))), ((IValue)($subscript_int(((IValue)($tuple_subject93)),2))), ((IValue)($subscript_int(((IValue)($tuple_subject93)),3))), ((IValue)($subscript_int(((IValue)($tuple_subject93)),4)))));
                                                  
                                                  } else {
                                                    continue $SCOMP91_GEN5799_TUPLE;
                                                  }
                                               
                                               } else {
                                                  continue $SCOMP91_GEN5799_TUPLE;/*computeFail*/
                                               }
                                            } else {
                                               continue $SCOMP91_GEN5799_TUPLE;/*computeFail*/
                                            }
                                         } else {
                                            continue $SCOMP91_GEN5799_TUPLE;/*computeFail*/
                                         }
                                      } else {
                                         continue $SCOMP91_GEN5799_TUPLE;/*computeFail*/
                                      }
                                   } else {
                                      continue $SCOMP91_GEN5799;
                                   }
                               } while(false);
                        
                        } else {
                           continue $SCOMP91_GEN5799;
                        }
                    }
                    
                                final ISet $result94 = ((ISet)($setwriter90.done()));
                    if($T42.instantiate($typeBindings) != $TF.voidType() && $isSubtypeOf($result94.getType(),$T42)){
                       return ((ISet)($result94));
                    
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
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Relation.rsc|(5861,337,<226,0>,<239,1>) 
    public ISet Relation_domainX$227b267edf852d93(ISet R_0, ISet S_1){ 
        
        
        HashMap<io.usethesource.vallang.type.Type,io.usethesource.vallang.type.Type> $typeBindings = new HashMap<>();
        if($T16.match(R_0.getType(), $typeBindings)){
           if(true){
              if($T19.match(S_1.getType(), $typeBindings)){
                 if(true){
                    final ISetWriter $setwriter95 = (ISetWriter)$RVF.setWriter();
                    ;
                    $SCOMP96_GEN6160:
                    for(IValue $elem97_for : ((ISet)R_0)){
                        IValue $elem97 = (IValue) $elem97_for;
                        final IValue $tuple_subject98 = ((IValue)($elem97));
                        if($tuple_subject98 instanceof ITuple && ((ITuple)$tuple_subject98).arity() == 2){
                           /*muExists*/$SCOMP96_GEN6160_TUPLE: 
                               do {
                                   if(true){
                                      IValue V0_2 = null;
                                      if(true){
                                         IValue V1_3 = null;
                                         if((((IBool)($RVF.bool(!(((ISet)S_1)).contains(((IValue)($subscript_int(((IValue)($tuple_subject98)),0)))))))).getValue()){
                                           $setwriter95.insert($RVF.tuple(((IValue)($subscript_int(((IValue)($tuple_subject98)),0))), ((IValue)($subscript_int(((IValue)($tuple_subject98)),1)))));
                                         
                                         } else {
                                           continue $SCOMP96_GEN6160_TUPLE;
                                         }
                                      
                                      } else {
                                         continue $SCOMP96_GEN6160_TUPLE;/*computeFail*/
                                      }
                                   } else {
                                      continue $SCOMP96_GEN6160;
                                   }
                               } while(false);
                        
                        } else {
                           continue $SCOMP96_GEN6160;
                        }
                    }
                    
                                final ISet $result99 = ((ISet)($setwriter95.done()));
                    if($T34.instantiate($typeBindings) != $TF.voidType() && $isSubtypeOf($result99.getType(),$T34)){
                       return ((ISet)($result99));
                    
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
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Relation.rsc|(6200,139,<241,0>,<244,1>) 
    public ISet Relation_domainX$3378efc5bc069d9d(ISet R_0, ISet S_1){ 
        
        
        HashMap<io.usethesource.vallang.type.Type,io.usethesource.vallang.type.Type> $typeBindings = new HashMap<>();
        if($T17.match(R_0.getType(), $typeBindings)){
           if(true){
              if($T19.match(S_1.getType(), $typeBindings)){
                 if(true){
                    final ISetWriter $setwriter100 = (ISetWriter)$RVF.setWriter();
                    ;
                    $SCOMP101_GEN6293:
                    for(IValue $elem102_for : ((ISet)R_0)){
                        IValue $elem102 = (IValue) $elem102_for;
                        final IValue $tuple_subject103 = ((IValue)($elem102));
                        if($tuple_subject103 instanceof ITuple && ((ITuple)$tuple_subject103).arity() == 3){
                           /*muExists*/$SCOMP101_GEN6293_TUPLE: 
                               do {
                                   if(true){
                                      IValue V0_2 = null;
                                      if(true){
                                         IValue V1_3 = null;
                                         if(true){
                                            IValue V2_4 = null;
                                            if((((IBool)($RVF.bool(!(((ISet)S_1)).contains(((IValue)($subscript_int(((IValue)($tuple_subject103)),0)))))))).getValue()){
                                              $setwriter100.insert($RVF.tuple(((IValue)($subscript_int(((IValue)($tuple_subject103)),0))), ((IValue)($subscript_int(((IValue)($tuple_subject103)),1))), ((IValue)($subscript_int(((IValue)($tuple_subject103)),2)))));
                                            
                                            } else {
                                              continue $SCOMP101_GEN6293_TUPLE;
                                            }
                                         
                                         } else {
                                            continue $SCOMP101_GEN6293_TUPLE;/*computeFail*/
                                         }
                                      } else {
                                         continue $SCOMP101_GEN6293_TUPLE;/*computeFail*/
                                      }
                                   } else {
                                      continue $SCOMP101_GEN6293;
                                   }
                               } while(false);
                        
                        } else {
                           continue $SCOMP101_GEN6293;
                        }
                    }
                    
                                final ISet $result104 = ((ISet)($setwriter100.done()));
                    if($T38.instantiate($typeBindings) != $TF.voidType() && $isSubtypeOf($result104.getType(),$T38)){
                       return ((ISet)($result104));
                    
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
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Relation.rsc|(6341,159,<246,0>,<249,1>) 
    public ISet Relation_domainX$7ed378a6babc8601(ISet R_0, ISet S_1){ 
        
        
        HashMap<io.usethesource.vallang.type.Type,io.usethesource.vallang.type.Type> $typeBindings = new HashMap<>();
        if($T18.match(R_0.getType(), $typeBindings)){
           if(true){
              if($T19.match(S_1.getType(), $typeBindings)){
                 if(true){
                    final ISetWriter $setwriter105 = (ISetWriter)$RVF.setWriter();
                    ;
                    $SCOMP106_GEN6446:
                    for(IValue $elem107_for : ((ISet)R_0)){
                        IValue $elem107 = (IValue) $elem107_for;
                        final IValue $tuple_subject108 = ((IValue)($elem107));
                        if($tuple_subject108 instanceof ITuple && ((ITuple)$tuple_subject108).arity() == 4){
                           /*muExists*/$SCOMP106_GEN6446_TUPLE: 
                               do {
                                   if(true){
                                      IValue V0_2 = null;
                                      if(true){
                                         IValue V1_3 = null;
                                         if(true){
                                            IValue V2_4 = null;
                                            if(true){
                                               IValue V3_5 = null;
                                               if((((IBool)($RVF.bool(!(((ISet)S_1)).contains(((IValue)($subscript_int(((IValue)($tuple_subject108)),0)))))))).getValue()){
                                                 $setwriter105.insert($RVF.tuple(((IValue)($subscript_int(((IValue)($tuple_subject108)),0))), ((IValue)($subscript_int(((IValue)($tuple_subject108)),1))), ((IValue)($subscript_int(((IValue)($tuple_subject108)),2))), ((IValue)($subscript_int(((IValue)($tuple_subject108)),3)))));
                                               
                                               } else {
                                                 continue $SCOMP106_GEN6446_TUPLE;
                                               }
                                            
                                            } else {
                                               continue $SCOMP106_GEN6446_TUPLE;/*computeFail*/
                                            }
                                         } else {
                                            continue $SCOMP106_GEN6446_TUPLE;/*computeFail*/
                                         }
                                      } else {
                                         continue $SCOMP106_GEN6446_TUPLE;/*computeFail*/
                                      }
                                   } else {
                                      continue $SCOMP106_GEN6446;
                                   }
                               } while(false);
                        
                        } else {
                           continue $SCOMP106_GEN6446;
                        }
                    }
                    
                                final ISet $result109 = ((ISet)($setwriter105.done()));
                    if($T40.instantiate($typeBindings) != $TF.voidType() && $isSubtypeOf($result109.getType(),$T40)){
                       return ((ISet)($result109));
                    
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
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Relation.rsc|(6502,179,<251,0>,<254,1>) 
    public ISet Relation_domainX$b4c31366bacacc90(ISet R_0, ISet S_1){ 
        
        
        HashMap<io.usethesource.vallang.type.Type,io.usethesource.vallang.type.Type> $typeBindings = new HashMap<>();
        if($T10.match(R_0.getType(), $typeBindings)){
           if(true){
              if($T19.match(S_1.getType(), $typeBindings)){
                 if(true){
                    final ISetWriter $setwriter110 = (ISetWriter)$RVF.setWriter();
                    ;
                    $SCOMP111_GEN6619:
                    for(IValue $elem112_for : ((ISet)R_0)){
                        IValue $elem112 = (IValue) $elem112_for;
                        final IValue $tuple_subject113 = ((IValue)($elem112));
                        if($tuple_subject113 instanceof ITuple && ((ITuple)$tuple_subject113).arity() == 5){
                           /*muExists*/$SCOMP111_GEN6619_TUPLE: 
                               do {
                                   if(true){
                                      IValue V0_2 = null;
                                      if(true){
                                         IValue V1_3 = null;
                                         if(true){
                                            IValue V2_4 = null;
                                            if(true){
                                               IValue V3_5 = null;
                                               if(true){
                                                  IValue V4_6 = null;
                                                  if((((IBool)($RVF.bool(!(((ISet)S_1)).contains(((IValue)($subscript_int(((IValue)($tuple_subject113)),0)))))))).getValue()){
                                                    $setwriter110.insert($RVF.tuple(((IValue)($subscript_int(((IValue)($tuple_subject113)),0))), ((IValue)($subscript_int(((IValue)($tuple_subject113)),1))), ((IValue)($subscript_int(((IValue)($tuple_subject113)),2))), ((IValue)($subscript_int(((IValue)($tuple_subject113)),3))), ((IValue)($subscript_int(((IValue)($tuple_subject113)),4)))));
                                                  
                                                  } else {
                                                    continue $SCOMP111_GEN6619_TUPLE;
                                                  }
                                               
                                               } else {
                                                  continue $SCOMP111_GEN6619_TUPLE;/*computeFail*/
                                               }
                                            } else {
                                               continue $SCOMP111_GEN6619_TUPLE;/*computeFail*/
                                            }
                                         } else {
                                            continue $SCOMP111_GEN6619_TUPLE;/*computeFail*/
                                         }
                                      } else {
                                         continue $SCOMP111_GEN6619_TUPLE;/*computeFail*/
                                      }
                                   } else {
                                      continue $SCOMP111_GEN6619;
                                   }
                               } while(false);
                        
                        } else {
                           continue $SCOMP111_GEN6619;
                        }
                    }
                    
                                final ISet $result114 = ((ISet)($setwriter110.done()));
                    if($T42.instantiate($typeBindings) != $TF.voidType() && $isSubtypeOf($result114.getType(),$T42)){
                       return ((ISet)($result114));
                    
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
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Relation.rsc|(6684,407,<257,0>,<267,1>) 
    public ISet Relation_groupDomainByRange$327972fc187fd972(ISet input_0){ 
        
        
        HashMap<io.usethesource.vallang.type.Type,io.usethesource.vallang.type.Type> $typeBindings = new HashMap<>();
        if($T7.match(input_0.getType(), $typeBindings)){
           final IMapWriter $mapwriter115 = (IMapWriter)$RVF.mapWriter();
           $MCOMP116_GEN7069:
           for(IValue $elem117_for : ((ISet)($arel_field_project((ISet)((ISet)input_0), ((IInteger)$constants.get(1)/*1*/))))){
               IValue $elem117 = (IValue) $elem117_for;
               IValue i_1 = null;
               $mapwriter115.insert($RVF.tuple($elem117, $arel_subscript1_noset(((ISet)($arel_field_project((ISet)((ISet)input_0), ((IInteger)$constants.get(1)/*1*/), ((IInteger)$constants.get(0)/*0*/)))),((IValue)($elem117)))));
           
           }
           
                       final ISet $result118 = ((ISet)($amap_field_project((IMap)((IMap)($mapwriter115.done())), ((IInteger)$constants.get(1)/*1*/))));
           if($T46.instantiate($typeBindings) != $TF.voidType() && $isSubtypeOf($result118.getType(),$T46)){
              return ((ISet)($result118));
           
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Relation.rsc|(7094,454,<270,0>,<280,1>) 
    public ISet Relation_groupRangeByDomain$bc2bb3150125fdcf(ISet input_0){ 
        
        
        HashMap<io.usethesource.vallang.type.Type,io.usethesource.vallang.type.Type> $typeBindings = new HashMap<>();
        if($T7.match(input_0.getType(), $typeBindings)){
           final IMapWriter $mapwriter119 = (IMapWriter)$RVF.mapWriter();
           $MCOMP120_GEN7526:
           for(IValue $elem121_for : ((ISet)($arel_field_project((ISet)((ISet)input_0), ((IInteger)$constants.get(0)/*0*/))))){
               IValue $elem121 = (IValue) $elem121_for;
               IValue i_1 = null;
               $mapwriter119.insert($RVF.tuple($elem121, $arel_subscript1_noset(((ISet)input_0),((IValue)($elem121)))));
           
           }
           
                       final ISet $result122 = ((ISet)($amap_field_project((IMap)((IMap)($mapwriter119.done())), ((IInteger)$constants.get(1)/*1*/))));
           if($T48.instantiate($typeBindings) != $TF.voidType() && $isSubtypeOf($result122.getType(),$T48)){
              return ((ISet)($result122));
           
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Relation.rsc|(7551,233,<283,0>,<296,1>) 
    public ISet Relation_ident$5a8fd748b3a21c74(ISet S_0){ 
        
        
        HashMap<io.usethesource.vallang.type.Type,io.usethesource.vallang.type.Type> $typeBindings = new HashMap<>();
        if($T3.match(S_0.getType(), $typeBindings)){
           final ISetWriter $setwriter123 = (ISetWriter)$RVF.setWriter();
           ;
           $SCOMP124_GEN7774:
           for(IValue $elem125_for : ((ISet)S_0)){
               IValue $elem125 = (IValue) $elem125_for;
               IValue V_1 = null;
               $setwriter123.insert($RVF.tuple(((IValue)($elem125)), ((IValue)($elem125))));
           
           }
           
                       final ISet $result126 = ((ISet)($setwriter123.done()));
           if($T30.instantiate($typeBindings) != $TF.voidType() && $isSubtypeOf($result126.getType(),$T30)){
              return ((ISet)($result126));
           
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Relation.rsc|(7787,187,<299,0>,<309,1>) 
    public ISet Relation_invert$ab51fefa560e8d82(ISet R_0){ 
        
        
        HashMap<io.usethesource.vallang.type.Type,io.usethesource.vallang.type.Type> $typeBindings = new HashMap<>();
        if($T16.match(R_0.getType(), $typeBindings)){
           final ISet $result127 = ((ISet)($arel_field_project((ISet)((ISet)R_0), ((IInteger)$constants.get(1)/*1*/), ((IInteger)$constants.get(0)/*0*/))));
           if($T49.instantiate($typeBindings) != $TF.voidType() && $isSubtypeOf($result127.getType(),$T49)){
              return ((ISet)($result127));
           
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Relation.rsc|(7976,80,<311,0>,<314,1>) 
    public ISet Relation_invert$02b31d4ba203e6f2(ISet R_0){ 
        
        
        HashMap<io.usethesource.vallang.type.Type,io.usethesource.vallang.type.Type> $typeBindings = new HashMap<>();
        if($T17.match(R_0.getType(), $typeBindings)){
           final ISet $result128 = ((ISet)($arel_field_project((ISet)((ISet)R_0), ((IInteger)$constants.get(2)/*2*/), ((IInteger)$constants.get(1)/*1*/), ((IInteger)$constants.get(0)/*0*/))));
           if($T50.instantiate($typeBindings) != $TF.voidType() && $isSubtypeOf($result128.getType(),$T50)){
              return ((ISet)($result128));
           
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Relation.rsc|(8058,93,<316,0>,<319,1>) 
    public ISet Relation_invert$23c95694a74f9dc2(ISet R_0){ 
        
        
        HashMap<io.usethesource.vallang.type.Type,io.usethesource.vallang.type.Type> $typeBindings = new HashMap<>();
        if($T18.match(R_0.getType(), $typeBindings)){
           final ISet $result129 = ((ISet)($arel_field_project((ISet)((ISet)R_0), ((IInteger)$constants.get(3)/*3*/), ((IInteger)$constants.get(2)/*2*/), ((IInteger)$constants.get(1)/*1*/), ((IInteger)$constants.get(0)/*0*/))));
           if($T51.instantiate($typeBindings) != $TF.voidType() && $isSubtypeOf($result129.getType(),$T51)){
              return ((ISet)($result129));
           
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Relation.rsc|(8153,106,<321,0>,<324,1>) 
    public ISet Relation_invert$7bfa0a46912a50e5(ISet R_0){ 
        
        
        HashMap<io.usethesource.vallang.type.Type,io.usethesource.vallang.type.Type> $typeBindings = new HashMap<>();
        if($T10.match(R_0.getType(), $typeBindings)){
           final ISet $result130 = ((ISet)($arel_field_project((ISet)((ISet)R_0), ((IInteger)$constants.get(4)/*4*/), ((IInteger)$constants.get(3)/*3*/), ((IInteger)$constants.get(2)/*2*/), ((IInteger)$constants.get(1)/*1*/), ((IInteger)$constants.get(0)/*0*/))));
           if($T52.instantiate($typeBindings) != $TF.voidType() && $isSubtypeOf($result130.getType(),$T52)){
              return ((ISet)($result130));
           
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Relation.rsc|(8262,249,<327,0>,<338,1>) 
    public ISet Relation_range$2db5a57a784252a8(ISet R_0){ 
        
        
        HashMap<io.usethesource.vallang.type.Type,io.usethesource.vallang.type.Type> $typeBindings = new HashMap<>();
        if($T16.match(R_0.getType(), $typeBindings)){
           final ISet $result131 = ((ISet)($arel_field_project((ISet)((ISet)R_0), ((IInteger)$constants.get(1)/*1*/))));
           if($T53.instantiate($typeBindings) != $TF.voidType() && $isSubtypeOf($result131.getType(),$T53)){
              return ((ISet)($result131));
           
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Relation.rsc|(8513,68,<340,0>,<343,1>) 
    public ISet Relation_range$86a90cd62434b56a(ISet R_0){ 
        
        
        HashMap<io.usethesource.vallang.type.Type,io.usethesource.vallang.type.Type> $typeBindings = new HashMap<>();
        if($T17.match(R_0.getType(), $typeBindings)){
           final ISet $result132 = ((ISet)($arel_field_project((ISet)((ISet)R_0), ((IInteger)$constants.get(1)/*1*/), ((IInteger)$constants.get(2)/*2*/))));
           if($T54.instantiate($typeBindings) != $TF.voidType() && $isSubtypeOf($result132.getType(),$T54)){
              return ((ISet)($result132));
           
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Relation.rsc|(8583,77,<345,0>,<348,1>) 
    public ISet Relation_range$3bab8d1f24ba88b1(ISet R_0){ 
        
        
        HashMap<io.usethesource.vallang.type.Type,io.usethesource.vallang.type.Type> $typeBindings = new HashMap<>();
        if($T18.match(R_0.getType(), $typeBindings)){
           final ISet $result133 = ((ISet)($arel_field_project((ISet)((ISet)R_0), ((IInteger)$constants.get(1)/*1*/), ((IInteger)$constants.get(2)/*2*/), ((IInteger)$constants.get(3)/*3*/))));
           if($T55.instantiate($typeBindings) != $TF.voidType() && $isSubtypeOf($result133.getType(),$T55)){
              return ((ISet)($result133));
           
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Relation.rsc|(8662,87,<350,0>,<353,1>) 
    public ISet Relation_range$22ad49e0807b0b4b(ISet R_0){ 
        
        
        HashMap<io.usethesource.vallang.type.Type,io.usethesource.vallang.type.Type> $typeBindings = new HashMap<>();
        if($T10.match(R_0.getType(), $typeBindings)){
           final ISet $result134 = ((ISet)($arel_field_project((ISet)((ISet)R_0), ((IInteger)$constants.get(1)/*1*/), ((IInteger)$constants.get(2)/*2*/), ((IInteger)$constants.get(3)/*3*/), ((IInteger)$constants.get(4)/*4*/))));
           if($T56.instantiate($typeBindings) != $TF.voidType() && $isSubtypeOf($result134.getType(),$T56)){
              return ((ISet)($result134));
           
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Relation.rsc|(8752,358,<356,0>,<369,1>) 
    public ISet Relation_rangeR$442c89d533c8dd3c(ISet R_0, ISet S_1){ 
        
        
        HashMap<io.usethesource.vallang.type.Type,io.usethesource.vallang.type.Type> $typeBindings = new HashMap<>();
        if($T16.match(R_0.getType(), $typeBindings)){
           if(true){
              if($T27.match(S_1.getType(), $typeBindings)){
                 if(true){
                    final ISetWriter $setwriter135 = (ISetWriter)$RVF.setWriter();
                    ;
                    $SCOMP136_GEN9075:
                    for(IValue $elem137_for : ((ISet)R_0)){
                        IValue $elem137 = (IValue) $elem137_for;
                        final IValue $tuple_subject138 = ((IValue)($elem137));
                        if($tuple_subject138 instanceof ITuple && ((ITuple)$tuple_subject138).arity() == 2){
                           /*muExists*/$SCOMP136_GEN9075_TUPLE: 
                               do {
                                   if(true){
                                      IValue V0_2 = null;
                                      if(true){
                                         IValue V1_3 = null;
                                         if((((IBool)($RVF.bool(((ISet)S_1).contains(((IValue)($subscript_int(((IValue)($tuple_subject138)),1)))))))).getValue()){
                                           $setwriter135.insert($RVF.tuple(((IValue)($subscript_int(((IValue)($tuple_subject138)),0))), ((IValue)($subscript_int(((IValue)($tuple_subject138)),1)))));
                                         
                                         } else {
                                           continue $SCOMP136_GEN9075_TUPLE;
                                         }
                                      
                                      } else {
                                         continue $SCOMP136_GEN9075_TUPLE;/*computeFail*/
                                      }
                                   } else {
                                      continue $SCOMP136_GEN9075;
                                   }
                               } while(false);
                        
                        } else {
                           continue $SCOMP136_GEN9075;
                        }
                    }
                    
                                final ISet $result139 = ((ISet)($setwriter135.done()));
                    if($T34.instantiate($typeBindings) != $TF.voidType() && $isSubtypeOf($result139.getType(),$T34)){
                       return ((ISet)($result139));
                    
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
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Relation.rsc|(9113,361,<372,0>,<385,1>) 
    public ISet Relation_rangeX$2ca0c43b311ef4db(ISet R_0, ISet S_1){ 
        
        
        HashMap<io.usethesource.vallang.type.Type,io.usethesource.vallang.type.Type> $typeBindings = new HashMap<>();
        if($T16.match(R_0.getType(), $typeBindings)){
           if(true){
              if($T27.match(S_1.getType(), $typeBindings)){
                 if(true){
                    final ISetWriter $setwriter140 = (ISetWriter)$RVF.setWriter();
                    ;
                    $SCOMP141_GEN9436:
                    for(IValue $elem142_for : ((ISet)R_0)){
                        IValue $elem142 = (IValue) $elem142_for;
                        final IValue $tuple_subject143 = ((IValue)($elem142));
                        if($tuple_subject143 instanceof ITuple && ((ITuple)$tuple_subject143).arity() == 2){
                           /*muExists*/$SCOMP141_GEN9436_TUPLE: 
                               do {
                                   if(true){
                                      IValue V0_2 = null;
                                      if(true){
                                         IValue V1_3 = null;
                                         if((((IBool)($RVF.bool(!(((ISet)S_1)).contains(((IValue)($subscript_int(((IValue)($tuple_subject143)),1)))))))).getValue()){
                                           $setwriter140.insert($RVF.tuple(((IValue)($subscript_int(((IValue)($tuple_subject143)),0))), ((IValue)($subscript_int(((IValue)($tuple_subject143)),1)))));
                                         
                                         } else {
                                           continue $SCOMP141_GEN9436_TUPLE;
                                         }
                                      
                                      } else {
                                         continue $SCOMP141_GEN9436_TUPLE;/*computeFail*/
                                      }
                                   } else {
                                      continue $SCOMP141_GEN9436;
                                   }
                               } while(false);
                        
                        } else {
                           continue $SCOMP141_GEN9436;
                        }
                    }
                    
                                final ISet $result144 = ((ISet)($setwriter140.done()));
                    if($T34.instantiate($typeBindings) != $TF.voidType() && $isSubtypeOf($result144.getType(),$T34)){
                       return ((ISet)($result144));
                    
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
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Relation.rsc|(9477,319,<388,0>,<399,50>) 
    public IMap Relation_index$d81a1657f0d245c9(ISet R_0){ 
        
        
        HashMap<io.usethesource.vallang.type.Type,io.usethesource.vallang.type.Type> $typeBindings = new HashMap<>();
        if($T24.match(R_0.getType(), $typeBindings)){
           final IMap $result145 = ((IMap)((IMap)$Prelude.index(R_0)));
           if($T57.instantiate($typeBindings) != $TF.voidType() && $isSubtypeOf($result145.getType(),$T57)){
              return ((IMap)($result145));
           
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    

    public static void main(String[] args) {
      throw new RuntimeException("No function `main` found in Rascal module `Relation`");
    }
}