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
public class $Map 
    extends
        org.rascalmpl.runtime.$RascalModule
    implements 
    	rascal.$Map_$I {

    private final $Map_$I $me;
    private final IList $constants;
    
    

    
    final org.rascalmpl.library.Prelude $Prelude; // TODO: asBaseClassName will generate name collisions if there are more of the same name in different packages

    
    public final io.usethesource.vallang.type.Type $T1;	/*avalue()*/
    public final io.usethesource.vallang.type.Type $T3;	/*aparameter("V",avalue(),closed=false)*/
    public final io.usethesource.vallang.type.Type $T12;	/*aparameter("W",avalue(),closed=false)*/
    public final io.usethesource.vallang.type.Type $T15;	/*aparameter("V",avalue(),closed=true)*/
    public final io.usethesource.vallang.type.Type $T21;	/*aparameter("L",avalue(),closed=true)*/
    public final io.usethesource.vallang.type.Type $T22;	/*aparameter("W",avalue(),closed=true)*/
    public final io.usethesource.vallang.type.Type $T27;	/*astr()*/
    public final io.usethesource.vallang.type.Type $T10;	/*aparameter("L",avalue(),closed=false)*/
    public final io.usethesource.vallang.type.Type $T14;	/*aparameter("K",avalue(),closed=true)*/
    public final io.usethesource.vallang.type.Type $T2;	/*aparameter("K",avalue(),closed=false)*/
    public final io.usethesource.vallang.type.Type $T24;	/*aint()*/
    public final io.usethesource.vallang.type.Type $T8;	/*alist(aparameter("V",avalue(),closed=false))*/
    public final io.usethesource.vallang.type.Type $T7;	/*amap(aparameter("K",avalue(),closed=false),alist(aparameter("V",avalue(),closed=false)))*/
    public final io.usethesource.vallang.type.Type $T9;	/*afunc(aparameter("L",avalue(),closed=false),[aparameter("K",avalue(),closed=false)],[])*/
    public final io.usethesource.vallang.type.Type $T13;	/*amap(aparameter("K",avalue(),closed=true),aparameter("V",avalue(),closed=true))*/
    public final io.usethesource.vallang.type.Type $T11;	/*afunc(aparameter("W",avalue(),closed=false),[aparameter("V",avalue(),closed=false)],[])*/
    public final io.usethesource.vallang.type.Type $T26;	/*arel(atypeList([aparameter("K",avalue(),closed=true),aparameter("V",avalue(),closed=true)]))*/
    public final io.usethesource.vallang.type.Type $T5;	/*aset(aparameter("V",avalue(),closed=false))*/
    public final io.usethesource.vallang.type.Type $T4;	/*aset(aparameter("K",avalue(),closed=false))*/
    public final io.usethesource.vallang.type.Type $T0;	/*amap(aparameter("K",avalue(),closed=false),aparameter("V",avalue(),closed=false))*/
    public final io.usethesource.vallang.type.Type $T25;	/*alrel(atypeList([aparameter("K",avalue(),closed=true),aparameter("V",avalue(),closed=true)]))*/
    public final io.usethesource.vallang.type.Type $T16;	/*aset(aparameter("K",avalue(),closed=true))*/
    public final io.usethesource.vallang.type.Type $T23;	/*aset(aparameter("V",avalue(),closed=true))*/
    public final io.usethesource.vallang.type.Type $T17;	/*amap(aparameter("V",avalue(),closed=true),aset(aparameter("K",avalue(),closed=true)))*/
    public final io.usethesource.vallang.type.Type $T6;	/*amap(aparameter("K",avalue(),closed=false),aset(aparameter("V",avalue(),closed=false)))*/
    public final io.usethesource.vallang.type.Type $T18;	/*amap(aparameter("V",avalue(),closed=true),aparameter("K",avalue(),closed=true))*/
    public final io.usethesource.vallang.type.Type $T20;	/*amap(aparameter("L",avalue(),closed=true),aparameter("W",avalue(),closed=true))*/
    public final io.usethesource.vallang.type.Type $T19;	/*abool()*/

    public $Map(RascalExecutionContext rex){
        this(rex, null);
    }
    
    public $Map(RascalExecutionContext rex, Object extended){
       super(rex);
       this.$me = extended == null ? this : ($Map_$I)extended;
       ModuleStore mstore = rex.getModuleStore();
       mstore.put(rascal.$Map.class, this);
        
        
       
                          
       
       
       $Prelude = $initLibrary("org.rascalmpl.library.Prelude"); 
    
       $constants = readBinaryConstantsFile(this.getClass(), "rascal//$Map.constants", 1, "f9e544f77b7eac7add281ef28ca5559f");
       $T1 = $TF.valueType();
       $T3 = $TF.parameterType("V", $T1);
       $T12 = $TF.parameterType("W", $T1);
       $T15 = $TF.parameterType("V", $T1);
       $T21 = $TF.parameterType("L", $T1);
       $T22 = $TF.parameterType("W", $T1);
       $T27 = $TF.stringType();
       $T10 = $TF.parameterType("L", $T1);
       $T14 = $TF.parameterType("K", $T1);
       $T2 = $TF.parameterType("K", $T1);
       $T24 = $TF.integerType();
       $T8 = $TF.listType($T3);
       $T7 = $TF.mapType($T2,$T8);
       $T9 = $TF.functionType($T10, $TF.tupleType($T2), $TF.tupleEmpty());
       $T13 = $TF.mapType($T14,$T15);
       $T11 = $TF.functionType($T12, $TF.tupleType($T3), $TF.tupleEmpty());
       $T26 = $TF.setType($TF.tupleType($T14, $T15));
       $T5 = $TF.setType($T3);
       $T4 = $TF.setType($T2);
       $T0 = $TF.mapType($T2,$T3);
       $T25 = $TF.listType($TF.tupleType($T14, $T15));
       $T16 = $TF.setType($T14);
       $T23 = $TF.setType($T15);
       $T17 = $TF.mapType($T15,$T16);
       $T6 = $TF.mapType($T2,$T5);
       $T18 = $TF.mapType($T15,$T14);
       $T20 = $TF.mapType($T21,$T22);
       $T19 = $TF.boolType();
    
       
       
    }
    public IBool isEmpty(IValue $P0){ // Generated by Resolver
       IBool $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type,$T0)){
         $result = (IBool)Map_isEmpty$ed672b4b9c5f3bbb((IMap) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public ISet domain(IValue $P0){ // Generated by Resolver
       ISet $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type,$T0)){
         $result = (ISet)Map_domain$9b1288a4ad0237b3((IMap) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public IMap invertUnique(IValue $P0){ // Generated by Resolver
       IMap $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type,$T0)){
         $result = (IMap)Map_invertUnique$5b3b386b9fc483b2((IMap) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public IMap delete(IValue $P0, IValue $P1){ // Generated by Resolver
       IMap $result = null;
       Type $P0Type = $P0.getType();
       Type $P1Type = $P1.getType();
       if($isSubtypeOf($P0Type,$T0) && $isSubtypeOf($P1Type,$T2)){
         $result = (IMap)Map_delete$b1d5cae977fe2443((IMap) $P0, (IValue) $P1);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0, $P1));
    }
    public IString toString(IValue $P0){ // Generated by Resolver
       IString $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type,$T0)){
         $result = (IString)Map_toString$abf5d325b231fef4((IMap) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public IMap domainR(IValue $P0, IValue $P1){ // Generated by Resolver
       IMap $result = null;
       Type $P0Type = $P0.getType();
       Type $P1Type = $P1.getType();
       if($isSubtypeOf($P0Type,$T0) && $isSubtypeOf($P1Type,$T4)){
         $result = (IMap)Map_domainR$a8f99b67701b72b1((IMap) $P0, (ISet) $P1);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0, $P1));
    }
    public IMap domainX(IValue $P0, IValue $P1){ // Generated by Resolver
       IMap $result = null;
       Type $P0Type = $P0.getType();
       Type $P1Type = $P1.getType();
       if($isSubtypeOf($P0Type,$T0) && $isSubtypeOf($P1Type,$T4)){
         $result = (IMap)Map_domainX$afe984f893f4a301((IMap) $P0, (ISet) $P1);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0, $P1));
    }
    public IMap invert(IValue $P0){ // Generated by Resolver
       IMap $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type,$T0)){
         $result = (IMap)Map_invert$64aa6cd1282cb3fa((IMap) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public IList toList(IValue $P0){ // Generated by Resolver
       IList $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type,$T0)){
         $result = (IList)Map_toList$0b33bf725d0e6d0f((IMap) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public IValue getOneFrom(IValue $P0){ // Generated by Resolver
       IValue $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type,$T0)){
         $result = (IValue)Map_getOneFrom$29ec312a4cfff141((IMap) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public IMap rangeX(IValue $P0, IValue $P1){ // Generated by Resolver
       IMap $result = null;
       Type $P0Type = $P0.getType();
       Type $P1Type = $P1.getType();
       if($isSubtypeOf($P0Type,$T0) && $isSubtypeOf($P1Type,$T5)){
         $result = (IMap)Map_rangeX$ee9d95de88722a3d((IMap) $P0, (ISet) $P1);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0, $P1));
    }
    public ISet range(IValue $P0){ // Generated by Resolver
       ISet $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type,$T0)){
         $result = (ISet)Map_range$253fbdb0dfd1c58e((IMap) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public ISet toRel(IValue $P0){ // Generated by Resolver
       ISet $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type,$T6)){
         $result = (ISet)Map_toRel$11f169637ca77288((IMap) $P0);
         if($result != null) return $result;
       }
       if($isSubtypeOf($P0Type,$T7)){
         $result = (ISet)Map_toRel$9956856de4e2d212((IMap) $P0);
         if($result != null) return $result;
       }
       if($isSubtypeOf($P0Type,$T0)){
         $result = (ISet)Map_toRel$387f97e911755202((IMap) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public IInteger size(IValue $P0){ // Generated by Resolver
       IInteger $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type,$T0)){
         $result = (IInteger)Map_size$9404b041dab68eb5((IMap) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public IMap mapper(IValue $P0, IValue $P1, IValue $P2){ // Generated by Resolver
       IMap $result = null;
       Type $P0Type = $P0.getType();
       Type $P1Type = $P1.getType();
       Type $P2Type = $P2.getType();
       if($isSubtypeOf($P0Type,$T0) && $isSubtypeOf($P1Type,$T9) && $isSubtypeOf($P2Type,$T11)){
         $result = (IMap)Map_mapper$b5a90d96017a8cf1((IMap) $P0, (TypedFunctionInstance1<IValue, IValue>) $P1, (TypedFunctionInstance1<IValue, IValue>) $P2);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0, $P1, $P2));
    }
    public IString itoString(IValue $P0){ // Generated by Resolver
       IString $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type,$T0)){
         $result = (IString)Map_itoString$5b37f50401db6c5c((IMap) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public IMap rangeR(IValue $P0, IValue $P1){ // Generated by Resolver
       IMap $result = null;
       Type $P0Type = $P0.getType();
       Type $P1Type = $P1.getType();
       if($isSubtypeOf($P0Type,$T0) && $isSubtypeOf($P1Type,$T5)){
         $result = (IMap)Map_rangeR$fdf13a0a4c50674a((IMap) $P0, (ISet) $P1);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0, $P1));
    }

    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Map.rsc|(648,267,<21,0>,<32,50>) 
    public IMap Map_delete$b1d5cae977fe2443(IMap m_0, IValue k_1){ 
        
        
        HashMap<io.usethesource.vallang.type.Type,io.usethesource.vallang.type.Type> $typeBindings = new HashMap<>();
        if($T0.match(m_0.getType(), $typeBindings)){
           if($T2.match(k_1.getType(), $typeBindings)){
              final IMap $result0 = ((IMap)((IMap)$Prelude.delete(m_0, k_1)));
              if($T13.instantiate($typeBindings) != $TF.voidType() && $isSubtypeOf($result0.getType(),$T13)){
                 return ((IMap)($result0));
              
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
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Map.rsc|(921,279,<36,0>,<47,42>) 
    public ISet Map_domain$9b1288a4ad0237b3(IMap M_0){ 
        
        
        HashMap<io.usethesource.vallang.type.Type,io.usethesource.vallang.type.Type> $typeBindings = new HashMap<>();
        if($T0.match(M_0.getType(), $typeBindings)){
           final ISet $result1 = ((ISet)((ISet)$Prelude.domain(M_0)));
           if($T16.instantiate($typeBindings) != $TF.voidType() && $isSubtypeOf($result1.getType(),$T16)){
              return ((ISet)($result1));
           
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Map.rsc|(1203,328,<50,0>,<61,49>) 
    public IMap Map_domainR$a8f99b67701b72b1(IMap M_0, ISet S_1){ 
        
        
        HashMap<io.usethesource.vallang.type.Type,io.usethesource.vallang.type.Type> $typeBindings = new HashMap<>();
        if($T0.match(M_0.getType(), $typeBindings)){
           if(true){
              if($T4.match(S_1.getType(), $typeBindings)){
                 if(true){
                    if((((IBool)($me.isEmpty(((IMap)M_0))))).getValue()){
                       final IMap $result6 = ((IMap)M_0);
                       if($T13.instantiate($typeBindings) != $TF.voidType() && $isSubtypeOf($result6.getType(),$T13)){
                          return ((IMap)($result6));
                       
                       } else {
                          return null;
                       }
                    } else {
                       final IMapWriter $mapwriter3 = (IMapWriter)$RVF.mapWriter();
                       $MCOMP4_GEN1512:
                       for(IValue $elem5_for : ((IMap)M_0)){
                           IValue $elem5 = (IValue) $elem5_for;
                           if($isSubtypeOf($elem5.getType(),$T14.instantiate($typeBindings))){
                              IValue k_2 = null;
                              if((((IBool)($RVF.bool(((ISet)S_1).contains(((IValue)($elem5))))))).getValue()){
                                $mapwriter3.insert($RVF.tuple($elem5, $amap_subscript(((IMap)M_0),((IValue)($elem5)))));
                              
                              } else {
                                continue $MCOMP4_GEN1512;
                              }
                           
                           } else {
                              continue $MCOMP4_GEN1512;
                           }
                       }
                       
                                   final IMap $result6 = ((IMap)($mapwriter3.done()));
                       if($T13.instantiate($typeBindings) != $TF.voidType() && $isSubtypeOf($result6.getType(),$T13)){
                          return ((IMap)($result6));
                       
                       } else {
                          return null;
                       }
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
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Map.rsc|(1534,335,<64,0>,<75,52>) 
    public IMap Map_domainX$afe984f893f4a301(IMap M_0, ISet S_1){ 
        
        
        HashMap<io.usethesource.vallang.type.Type,io.usethesource.vallang.type.Type> $typeBindings = new HashMap<>();
        if($T0.match(M_0.getType(), $typeBindings)){
           if(true){
              if($T4.match(S_1.getType(), $typeBindings)){
                 if(true){
                    if((((IBool)($me.isEmpty(((IMap)M_0))))).getValue()){
                       final IMap $result11 = ((IMap)M_0);
                       if($T13.instantiate($typeBindings) != $TF.voidType() && $isSubtypeOf($result11.getType(),$T13)){
                          return ((IMap)($result11));
                       
                       } else {
                          return null;
                       }
                    } else {
                       final IMapWriter $mapwriter8 = (IMapWriter)$RVF.mapWriter();
                       $MCOMP9_GEN1847:
                       for(IValue $elem10_for : ((IMap)M_0)){
                           IValue $elem10 = (IValue) $elem10_for;
                           if($isSubtypeOf($elem10.getType(),$T14.instantiate($typeBindings))){
                              IValue k_2 = null;
                              if((((IBool)($RVF.bool(!(((ISet)S_1)).contains(((IValue)($elem10))))))).getValue()){
                                $mapwriter8.insert($RVF.tuple($elem10, $amap_subscript(((IMap)M_0),((IValue)($elem10)))));
                              
                              } else {
                                continue $MCOMP9_GEN1847;
                              }
                           
                           } else {
                              continue $MCOMP9_GEN1847;
                           }
                       }
                       
                                   final IMap $result11 = ((IMap)($mapwriter8.done()));
                       if($T13.instantiate($typeBindings) != $TF.voidType() && $isSubtypeOf($result11.getType(),$T13)){
                          return ((IMap)($result11));
                       
                       } else {
                          return null;
                       }
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
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Map.rsc|(1872,385,<78,0>,<91,41>) 
    public IValue Map_getOneFrom$29ec312a4cfff141(IMap M_0){ 
        
        
        HashMap<io.usethesource.vallang.type.Type,io.usethesource.vallang.type.Type> $typeBindings = new HashMap<>();
        if($T0.match(M_0.getType(), $typeBindings)){
           final IValue $result12 = ((IValue)((IValue)$Prelude.getOneFrom(M_0)));
           if($T14.instantiate($typeBindings) != $TF.voidType() && $isSubtypeOf($result12.getType(),$T14)){
              return ((IValue)($result12));
           
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Map.rsc|(2260,396,<94,0>,<106,53>) 
    public IMap Map_invert$64aa6cd1282cb3fa(IMap M_0){ 
        
        
        HashMap<io.usethesource.vallang.type.Type,io.usethesource.vallang.type.Type> $typeBindings = new HashMap<>();
        if($T0.match(M_0.getType(), $typeBindings)){
           final IMap $result13 = ((IMap)((IMap)$Prelude.invert(M_0)));
           if($T17.instantiate($typeBindings) != $TF.voidType() && $isSubtypeOf($result13.getType(),$T17)){
              return ((IMap)($result13));
           
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Map.rsc|(2659,708,<109,0>,<130,54>) 
    public IMap Map_invertUnique$5b3b386b9fc483b2(IMap M_0){ 
        
        
        HashMap<io.usethesource.vallang.type.Type,io.usethesource.vallang.type.Type> $typeBindings = new HashMap<>();
        if($T0.match(M_0.getType(), $typeBindings)){
           final IMap $result14 = ((IMap)((IMap)$Prelude.invertUnique(M_0)));
           if($T18.instantiate($typeBindings) != $TF.voidType() && $isSubtypeOf($result14.getType(),$T18)){
              return ((IMap)($result14));
           
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Map.rsc|(3370,302,<133,0>,<145,40>) 
    public IBool Map_isEmpty$ed672b4b9c5f3bbb(IMap M_0){ 
        
        
        HashMap<io.usethesource.vallang.type.Type,io.usethesource.vallang.type.Type> $typeBindings = new HashMap<>();
        if($T0.match(M_0.getType(), $typeBindings)){
           final IBool $result15 = ((IBool)((IBool)$Prelude.isEmpty(M_0)));
           if($T19.instantiate($typeBindings) != $TF.voidType() && $isSubtypeOf($result15.getType(),$T19)){
              return ((IBool)($result15));
           
           } else {
              return ((IBool)$constants.get(0)/*false*/);
           
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Map.rsc|(3675,454,<148,0>,<161,38>) 
    public IMap Map_mapper$b5a90d96017a8cf1(IMap M_0, TypedFunctionInstance1<IValue, IValue> F_1, TypedFunctionInstance1<IValue, IValue> G_2){ 
        
        
        HashMap<io.usethesource.vallang.type.Type,io.usethesource.vallang.type.Type> $typeBindings = new HashMap<>();
        if($T0.match(M_0.getType(), $typeBindings)){
           if(true){
              if($T9.match(F_1.getType(), $typeBindings)){
                 if(true){
                    if($T11.match(G_2.getType(), $typeBindings)){
                       if(true){
                          final IMapWriter $mapwriter16 = (IMapWriter)$RVF.mapWriter();
                          $MCOMP17_GEN4116:
                          for(IValue $elem18_for : ((IMap)M_0)){
                              IValue $elem18 = (IValue) $elem18_for;
                              if($isSubtypeOf($elem18.getType(),$T14.instantiate($typeBindings))){
                                 IValue key_3 = null;
                                 $mapwriter16.insert($RVF.tuple(((TypedFunctionInstance1<IValue, IValue>)F_1).typedCall(((IValue)($elem18))), ((TypedFunctionInstance1<IValue, IValue>)G_2).typedCall(((IValue)($amap_subscript(((IMap)M_0),((IValue)($elem18))))))));
                              
                              } else {
                                 continue $MCOMP17_GEN4116;
                              }
                          }
                          
                                      final IMap $result19 = ((IMap)($mapwriter16.done()));
                          if($T20.instantiate($typeBindings) != $TF.voidType() && $isSubtypeOf($result19.getType(),$T20)){
                             return ((IMap)($result19));
                          
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
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Map.rsc|(4133,297,<165,0>,<176,41>) 
    public ISet Map_range$253fbdb0dfd1c58e(IMap M_0){ 
        
        
        HashMap<io.usethesource.vallang.type.Type,io.usethesource.vallang.type.Type> $typeBindings = new HashMap<>();
        if($T0.match(M_0.getType(), $typeBindings)){
           final ISet $result20 = ((ISet)((ISet)$Prelude.range(M_0)));
           if($T23.instantiate($typeBindings) != $TF.voidType() && $isSubtypeOf($result20.getType(),$T23)){
              return ((ISet)($result20));
           
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Map.rsc|(4433,342,<179,0>,<190,52>) 
    public IMap Map_rangeR$fdf13a0a4c50674a(IMap M_0, ISet S_1){ 
        
        
        HashMap<io.usethesource.vallang.type.Type,io.usethesource.vallang.type.Type> $typeBindings = new HashMap<>();
        if($T0.match(M_0.getType(), $typeBindings)){
           if(true){
              if($T5.match(S_1.getType(), $typeBindings)){
                 if(true){
                    if((((IBool)($me.isEmpty(((IMap)M_0))))).getValue()){
                       final IMap $result25 = ((IMap)M_0);
                       if($T13.instantiate($typeBindings) != $TF.voidType() && $isSubtypeOf($result25.getType(),$T13)){
                          return ((IMap)($result25));
                       
                       } else {
                          return null;
                       }
                    } else {
                       final IMapWriter $mapwriter22 = (IMapWriter)$RVF.mapWriter();
                       $MCOMP23_GEN4753:
                       for(IValue $elem24_for : ((IMap)M_0)){
                           IValue $elem24 = (IValue) $elem24_for;
                           if($isSubtypeOf($elem24.getType(),$T14.instantiate($typeBindings))){
                              IValue k_2 = null;
                              if((((IBool)($RVF.bool(((ISet)S_1).contains(((IValue)($amap_subscript(((IMap)M_0),((IValue)($elem24)))))))))).getValue()){
                                $mapwriter22.insert($RVF.tuple($elem24, $amap_subscript(((IMap)M_0),((IValue)($elem24)))));
                              
                              } else {
                                continue $MCOMP23_GEN4753;
                              }
                           
                           } else {
                              continue $MCOMP23_GEN4753;
                           }
                       }
                       
                                   final IMap $result25 = ((IMap)($mapwriter22.done()));
                       if($T13.instantiate($typeBindings) != $TF.voidType() && $isSubtypeOf($result25.getType(),$T13)){
                          return ((IMap)($result25));
                       
                       } else {
                          return null;
                       }
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
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Map.rsc|(4778,348,<193,0>,<204,55>) 
    public IMap Map_rangeX$ee9d95de88722a3d(IMap M_0, ISet S_1){ 
        
        
        HashMap<io.usethesource.vallang.type.Type,io.usethesource.vallang.type.Type> $typeBindings = new HashMap<>();
        if($T0.match(M_0.getType(), $typeBindings)){
           if(true){
              if($T5.match(S_1.getType(), $typeBindings)){
                 if(true){
                    if((((IBool)($me.isEmpty(((IMap)M_0))))).getValue()){
                       final IMap $result30 = ((IMap)M_0);
                       if($T13.instantiate($typeBindings) != $TF.voidType() && $isSubtypeOf($result30.getType(),$T13)){
                          return ((IMap)($result30));
                       
                       } else {
                          return null;
                       }
                    } else {
                       final IMapWriter $mapwriter27 = (IMapWriter)$RVF.mapWriter();
                       $MCOMP28_GEN5101:
                       for(IValue $elem29_for : ((IMap)M_0)){
                           IValue $elem29 = (IValue) $elem29_for;
                           if($isSubtypeOf($elem29.getType(),$T14.instantiate($typeBindings))){
                              IValue k_2 = null;
                              if((((IBool)($RVF.bool(!(((ISet)S_1)).contains(((IValue)($amap_subscript(((IMap)M_0),((IValue)($elem29)))))))))).getValue()){
                                $mapwriter27.insert($RVF.tuple($elem29, $amap_subscript(((IMap)M_0),((IValue)($elem29)))));
                              
                              } else {
                                continue $MCOMP28_GEN5101;
                              }
                           
                           } else {
                              continue $MCOMP28_GEN5101;
                           }
                       }
                       
                                   final IMap $result30 = ((IMap)($mapwriter27.done()));
                       if($T13.instantiate($typeBindings) != $TF.voidType() && $isSubtypeOf($result30.getType(),$T13)){
                          return ((IMap)($result30));
                       
                       } else {
                          return null;
                       }
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
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Map.rsc|(5129,273,<207,0>,<218,36>) 
    public IInteger Map_size$9404b041dab68eb5(IMap M_0){ 
        
        
        HashMap<io.usethesource.vallang.type.Type,io.usethesource.vallang.type.Type> $typeBindings = new HashMap<>();
        if($T0.match(M_0.getType(), $typeBindings)){
           final IInteger $result31 = ((IInteger)((IInteger)$Prelude.size(M_0)));
           if($T24.instantiate($typeBindings) != $TF.voidType() && $isSubtypeOf($result31.getType(),$T24)){
              return ((IInteger)($result31));
           
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Map.rsc|(5405,233,<221,0>,<229,54>) 
    public IList Map_toList$0b33bf725d0e6d0f(IMap M_0){ 
        
        
        HashMap<io.usethesource.vallang.type.Type,io.usethesource.vallang.type.Type> $typeBindings = new HashMap<>();
        if($T0.match(M_0.getType(), $typeBindings)){
           final IList $result32 = ((IList)((IList)$Prelude.toList(M_0)));
           if($T25.instantiate($typeBindings) != $TF.voidType() && $isSubtypeOf($result32.getType(),$T25)){
              return ((IList)($result32));
           
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Map.rsc|(5641,211,<232,0>,<239,81>) 
    public ISet Map_toRel$11f169637ca77288(IMap M_0){ 
        
        
        HashMap<io.usethesource.vallang.type.Type,io.usethesource.vallang.type.Type> $typeBindings = new HashMap<>();
        if($T6.match(M_0.getType(), $typeBindings)){
           if(true){
              final ISetWriter $setwriter33 = (ISetWriter)$RVF.setWriter();
              ;
              $SCOMP34_GEN5827:
              for(IValue $elem36_for : ((IMap)M_0)){
                  IValue $elem36 = (IValue) $elem36_for;
                  if($isSubtypeOf($elem36.getType(),$T14.instantiate($typeBindings))){
                     IValue k_1 = null;
                     $SCOMP34_GEN5827_GEN5838:
                     for(IValue $elem35_for : ((ISet)($amap_subscript(((IMap)M_0),((IValue)($elem36)))))){
                         IValue $elem35 = (IValue) $elem35_for;
                         if($isSubtypeOf($elem35.getType(),$T15.instantiate($typeBindings))){
                            IValue v_2 = null;
                            $setwriter33.insert($RVF.tuple(((IValue)($elem36)), ((IValue)($elem35))));
                         
                         } else {
                            continue $SCOMP34_GEN5827_GEN5838;
                         }
                     }
                     continue $SCOMP34_GEN5827;
                                 
                  } else {
                     continue $SCOMP34_GEN5827;
                  }
              }
              
                          final ISet $result37 = ((ISet)($setwriter33.done()));
              if($T26.instantiate($typeBindings) != $TF.voidType() && $isSubtypeOf($result37.getType(),$T26)){
                 return ((ISet)($result37));
              
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
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Map.rsc|(5853,81,<240,0>,<240,81>) 
    public ISet Map_toRel$9956856de4e2d212(IMap M_0){ 
        
        
        HashMap<io.usethesource.vallang.type.Type,io.usethesource.vallang.type.Type> $typeBindings = new HashMap<>();
        if($T7.match(M_0.getType(), $typeBindings)){
           if(true){
              final ISetWriter $setwriter38 = (ISetWriter)$RVF.setWriter();
              ;
              $SCOMP39_GEN5909:
              for(IValue $elem41_for : ((IMap)M_0)){
                  IValue $elem41 = (IValue) $elem41_for;
                  if($isSubtypeOf($elem41.getType(),$T14.instantiate($typeBindings))){
                     IValue k_1 = null;
                     $SCOMP39_GEN5909_GEN5920:
                     for(IValue $elem40_for : ((IList)($amap_subscript(((IMap)M_0),((IValue)($elem41)))))){
                         IValue $elem40 = (IValue) $elem40_for;
                         if($isSubtypeOf($elem40.getType(),$T15.instantiate($typeBindings))){
                            IValue v_2 = null;
                            $setwriter38.insert($RVF.tuple(((IValue)($elem41)), ((IValue)($elem40))));
                         
                         } else {
                            continue $SCOMP39_GEN5909_GEN5920;
                         }
                     }
                     continue $SCOMP39_GEN5909;
                                 
                  } else {
                     continue $SCOMP39_GEN5909;
                  }
              }
              
                          final ISet $result42 = ((ISet)($setwriter38.done()));
              if($T26.instantiate($typeBindings) != $TF.voidType() && $isSubtypeOf($result42.getType(),$T26)){
                 return ((ISet)($result42));
              
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
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Map.rsc|(5935,95,<241,0>,<242,53>) 
    public ISet Map_toRel$387f97e911755202(IMap M_0){ 
        
        
        HashMap<io.usethesource.vallang.type.Type,io.usethesource.vallang.type.Type> $typeBindings = new HashMap<>();
        if($T0.match(M_0.getType(), $typeBindings)){
           final ISet $result43 = ((ISet)((ISet)$Prelude.toRel(M_0)));
           if($T26.instantiate($typeBindings) != $TF.voidType() && $isSubtypeOf($result43.getType(),$T26)){
              return ((ISet)($result43));
           
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Map.rsc|(6033,213,<245,0>,<253,40>) 
    public IString Map_toString$abf5d325b231fef4(IMap M_0){ 
        
        
        HashMap<io.usethesource.vallang.type.Type,io.usethesource.vallang.type.Type> $typeBindings = new HashMap<>();
        if($T0.match(M_0.getType(), $typeBindings)){
           final IString $result44 = ((IString)((IString)$Prelude.toString(M_0)));
           if($T27.instantiate($typeBindings) != $TF.voidType() && $isSubtypeOf($result44.getType(),$T27)){
              return ((IString)($result44));
           
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/Map.rsc|(6249,224,<256,0>,<264,41>) 
    public IString Map_itoString$5b37f50401db6c5c(IMap M_0){ 
        
        
        HashMap<io.usethesource.vallang.type.Type,io.usethesource.vallang.type.Type> $typeBindings = new HashMap<>();
        if($T0.match(M_0.getType(), $typeBindings)){
           final IString $result45 = ((IString)((IString)$Prelude.itoString(M_0)));
           if($T27.instantiate($typeBindings) != $TF.voidType() && $isSubtypeOf($result45.getType(),$T27)){
              return ((IString)($result45));
           
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    

    public static void main(String[] args) {
      throw new RuntimeException("No function `main` found in Rascal module `Map`");
    }
}