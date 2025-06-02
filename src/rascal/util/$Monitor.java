package rascal.util;
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
public class $Monitor 
    extends
        org.rascalmpl.runtime.$RascalModule
    implements 
    	rascal.util.$Monitor_$I {

    private final $Monitor_$I $me;
    private final IList $constants;
    final java.util.Map<java.lang.String,IValue> $kwpDefaults_util_Monitor_jobStart$6eca9de903da5efd;
    final java.util.Map<java.lang.String,IValue> $kwpDefaults_util_Monitor_jobStep$c4ecb3797f3fc35b;
    final java.util.Map<java.lang.String,IValue> $kwpDefaults_util_Monitor_jobEnd$0d9e8a42d2c9205b;
    final java.util.Map<java.lang.String,IValue> $kwpDefaults_util_Monitor_jobTodo$e0b8657ec7d3254d;
    final java.util.Map<java.lang.String,IValue> $kwpDefaults_util_Monitor_job$709fc0dd3ef7ac2b;
    final java.util.Map<java.lang.String,IValue> $kwpDefaults_util_Monitor_job$099df0963e4ce399;
    final java.util.Map<java.lang.String,IValue> $kwpDefaults_util_Monitor_job$67c9665eb8ce1e4f;
    final java.util.Map<java.lang.String,IValue> $kwpDefaults_util_Monitor_job$93fbc1bd1b37a8be;

    
    public final rascal.$IO M_IO;
    public final rascal.util.$Math M_util_Math;
    public final rascal.$Exception M_Exception;

    
    final org.rascalmpl.library.util.Monitor $Monitor; // TODO: asBaseClassName will generate name collisions if there are more of the same name in different packages

    
    public final io.usethesource.vallang.type.Type $T13;	/*aloc()*/
    public final io.usethesource.vallang.type.Type $T9;	/*aint(alabel="worked")*/
    public final io.usethesource.vallang.type.Type $T0;	/*astr()*/
    public final io.usethesource.vallang.type.Type $T2;	/*avalue()*/
    public final io.usethesource.vallang.type.Type $T17;	/*aint(alabel="_")*/
    public final io.usethesource.vallang.type.Type $T15;	/*aparameter("T",avalue(),closed=true)*/
    public final io.usethesource.vallang.type.Type $T3;	/*aparameter("T",avalue(),closed=false)*/
    public final io.usethesource.vallang.type.Type $T5;	/*avoid()*/
    public final io.usethesource.vallang.type.Type $T8;	/*astr(alabel="message")*/
    public final io.usethesource.vallang.type.Type $T18;	/*afunc(avoid(),[aint(alabel="_")],[],alabel="_")*/
    public final io.usethesource.vallang.type.Type $T16;	/*afunc(avoid(),[afunc(avoid(),[aint(alabel="_")],[],alabel="_")],[])*/
    public final io.usethesource.vallang.type.Type $T6;	/*afunc(avoid(),[],[],alabel="step")*/
    public final io.usethesource.vallang.type.Type ADT_LocationType;	/*aadt("LocationType",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type $T4;	/*afunc(aparameter("T",avalue(),closed=false),[afunc(avoid(),[],[],alabel="step")],[])*/
    public final io.usethesource.vallang.type.Type ADT_LocationChangeType;	/*aadt("LocationChangeType",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type $T12;	/*afunc(avoid(),[aint(alabel="worked")],[],alabel="step")*/
    public final io.usethesource.vallang.type.Type $T11;	/*afunc(aparameter("T",avalue(),closed=false),[afunc(avoid(),[aint(alabel="worked")],[],alabel="step")],[])*/
    public final io.usethesource.vallang.type.Type $T1;	/*afunc(aparameter("T",avalue(),closed=false),[],[])*/
    public final io.usethesource.vallang.type.Type $T10;	/*afunc(avoid(),[astr(alabel="message"),aint(alabel="worked")],[],alabel="step")*/
    public final io.usethesource.vallang.type.Type $T7;	/*afunc(aparameter("T",avalue(),closed=false),[afunc(avoid(),[astr(alabel="message"),aint(alabel="worked")],[],alabel="step")],[])*/
    public final io.usethesource.vallang.type.Type $T20;	/*afunc(avoid(),[],[],returnsViaAllPath=false)*/
    public final io.usethesource.vallang.type.Type ADT_LocationChangeEvent;	/*aadt("LocationChangeEvent",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type $T19;	/*afunc(avoid(),[aint(alabel="worked")],[],returnsViaAllPath=false)*/
    public final io.usethesource.vallang.type.Type ADT_RuntimeException;	/*aadt("RuntimeException",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type $T14;	/*afunc(avoid(),[astr(alabel="message"),aint(alabel="worked")],[],returnsViaAllPath=false)*/
    public final io.usethesource.vallang.type.Type ADT_IOCapability;	/*aadt("IOCapability",[],dataSyntax())*/

    public $Monitor(RascalExecutionContext rex){
        this(rex, null);
    }
    
    public $Monitor(RascalExecutionContext rex, Object extended){
       super(rex);
       this.$me = extended == null ? this : ($Monitor_$I)extended;
       ModuleStore mstore = rex.getModuleStore();
       mstore.put(rascal.util.$Monitor.class, this);
       
       mstore.importModule(rascal.$IO.class, rex, rascal.$IO::new);
       mstore.importModule(rascal.util.$Math.class, rex, rascal.util.$Math::new);
       mstore.importModule(rascal.$Exception.class, rex, rascal.$Exception::new); 
       
       M_IO = mstore.getModule(rascal.$IO.class);
       M_util_Math = mstore.getModule(rascal.util.$Math.class);
       M_Exception = mstore.getModule(rascal.$Exception.class); 
       
                          
       
       $TS.importStore(M_IO.$TS);
       $TS.importStore(M_util_Math.$TS);
       $TS.importStore(M_Exception.$TS);
       
       $Monitor = $initLibrary("org.rascalmpl.library.util.Monitor"); 
    
       $constants = readBinaryConstantsFile(this.getClass(), "rascal/util/$Monitor.constants", 33, "ada24d8086546d9541cf3e78dea45c77");
       ADT_LocationType = $adt("LocationType");
       ADT_LocationChangeType = $adt("LocationChangeType");
       ADT_LocationChangeEvent = $adt("LocationChangeEvent");
       ADT_RuntimeException = $adt("RuntimeException");
       ADT_IOCapability = $adt("IOCapability");
       $T13 = $TF.sourceLocationType();
       $T9 = $TF.integerType();
       $T0 = $TF.stringType();
       $T2 = $TF.valueType();
       $T17 = $TF.integerType();
       $T15 = $TF.parameterType("T", $T2);
       $T3 = $TF.parameterType("T", $T2);
       $T5 = $TF.voidType();
       $T8 = $TF.stringType();
       $T18 = $TF.functionType($T5, $TF.tupleType($T17, "_"), $TF.tupleEmpty());
       $T16 = $TF.functionType($T5, $TF.tupleType($T18, "_"), $TF.tupleEmpty());
       $T6 = $TF.functionType($T5, $TF.tupleEmpty(), $TF.tupleEmpty());
       $T4 = $TF.functionType($T3, $TF.tupleType($T6, "step"), $TF.tupleEmpty());
       $T12 = $TF.functionType($T5, $TF.tupleType($T9, "worked"), $TF.tupleEmpty());
       $T11 = $TF.functionType($T3, $TF.tupleType($T12, "step"), $TF.tupleEmpty());
       $T1 = $TF.functionType($T3, $TF.tupleEmpty(), $TF.tupleEmpty());
       $T10 = $TF.functionType($T5, $TF.tupleType($T8, "message", $T9, "worked"), $TF.tupleEmpty());
       $T7 = $TF.functionType($T3, $TF.tupleType($T10, "step"), $TF.tupleEmpty());
       $T20 = $TF.functionType($T5, $TF.tupleEmpty(), $TF.tupleEmpty());
       $T19 = $TF.functionType($T5, $TF.tupleType($T9, "worked"), $TF.tupleEmpty());
       $T14 = $TF.functionType($T5, $TF.tupleType($T8, "message", $T9, "worked"), $TF.tupleEmpty());
    
       
       $kwpDefaults_util_Monitor_jobStart$6eca9de903da5efd = Util.kwpMap("work", ((IInteger)$constants.get(0)/*1*/), "totalWork", ((IInteger)$constants.get(1)/*100*/));
       $kwpDefaults_util_Monitor_jobStep$c4ecb3797f3fc35b = Util.kwpMap("work", ((IInteger)$constants.get(0)/*1*/));
       $kwpDefaults_util_Monitor_jobEnd$0d9e8a42d2c9205b = Util.kwpMap("success", ((IBool)$constants.get(2)/*true*/));
       $kwpDefaults_util_Monitor_jobTodo$e0b8657ec7d3254d = Util.kwpMap("work", ((IInteger)$constants.get(0)/*1*/));
       $kwpDefaults_util_Monitor_job$709fc0dd3ef7ac2b = Util.kwpMap("totalWork", ((IInteger)$constants.get(1)/*100*/));
       $kwpDefaults_util_Monitor_job$099df0963e4ce399 = Util.kwpMap("totalWork", ((IInteger)$constants.get(0)/*1*/));
       $kwpDefaults_util_Monitor_job$67c9665eb8ce1e4f = Util.kwpMap("totalWork", ((IInteger)$constants.get(0)/*1*/));
       $kwpDefaults_util_Monitor_job$93fbc1bd1b37a8be = Util.kwpMap("totalWork", ((IInteger)$constants.get(0)/*1*/));
    
    }
    public IValue job(IValue $P0, IValue $P1, java.util.Map<java.lang.String,IValue> $kwpActuals){ // Generated by Resolver
       IValue $result = null;
       Type $P0Type = $P0.getType();
       Type $P1Type = $P1.getType();
       if($isSubtypeOf($P0Type,$T0) && $isSubtypeOf($P1Type,$T1)){
         $result = (IValue)util_Monitor_job$93fbc1bd1b37a8be((IString) $P0, (TypedFunctionInstance0<IValue>) $P1, $kwpActuals);
         if($result != null) return $result;
       }
       if($isSubtypeOf($P0Type,$T0) && $isSubtypeOf($P1Type,$T4)){
         $result = (IValue)util_Monitor_job$67c9665eb8ce1e4f((IString) $P0, (TypedFunctionInstance1<IValue, IValue>) $P1, $kwpActuals);
         if($result != null) return $result;
       }
       if($isSubtypeOf($P0Type,$T0) && $isSubtypeOf($P1Type,$T7)){
         $result = (IValue)util_Monitor_job$709fc0dd3ef7ac2b((IString) $P0, (TypedFunctionInstance1<IValue, IValue>) $P1, $kwpActuals);
         if($result != null) return $result;
       }
       if($isSubtypeOf($P0Type,$T0) && $isSubtypeOf($P1Type,$T11)){
         $result = (IValue)util_Monitor_job$099df0963e4ce399((IString) $P0, (TypedFunctionInstance1<IValue, IValue>) $P1, $kwpActuals);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0, $P1));
    }
    public IBool horseRaceTest(){ // Generated by Resolver
       IBool $result = null;
       $result = (IBool)util_Monitor_horseRaceTest$4c48533f01fc7dde();
       if($result != null) return $result;
       throw RuntimeExceptionFactory.callFailed($RVF.list());
    }
    public IBool unfinishedLinesAtTheEndTest(){ // Generated by Resolver
       IBool $result = null;
       $result = (IBool)util_Monitor_unfinishedLinesAtTheEndTest$570d0a42211dec4a();
       if($result != null) return $result;
       throw RuntimeExceptionFactory.callFailed($RVF.list());
    }
    public void jobTodo(IValue $P0, java.util.Map<java.lang.String,IValue> $kwpActuals){ // Generated by Resolver
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type,$T0)){
         try { util_Monitor_jobTodo$e0b8657ec7d3254d((IString) $P0, $kwpActuals); return; } catch (FailReturnFromVoidException e){};
       
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public void jobStart(IValue $P0, java.util.Map<java.lang.String,IValue> $kwpActuals){ // Generated by Resolver
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type,$T0)){
         try { util_Monitor_jobStart$6eca9de903da5efd((IString) $P0, $kwpActuals); return; } catch (FailReturnFromVoidException e){};
       
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public IBool printLongUnfinishedLine(){ // Generated by Resolver
       IBool $result = null;
       $result = (IBool)util_Monitor_printLongUnfinishedLine$2f78e7621c7925e7();
       if($result != null) return $result;
       throw RuntimeExceptionFactory.callFailed($RVF.list());
    }
    public void jobStep(IValue $P0, IValue $P1, java.util.Map<java.lang.String,IValue> $kwpActuals){ // Generated by Resolver
       Type $P0Type = $P0.getType();
       Type $P1Type = $P1.getType();
       if($isSubtypeOf($P0Type,$T0) && $isSubtypeOf($P1Type,$T8)){
         try { util_Monitor_jobStep$c4ecb3797f3fc35b((IString) $P0, (IString) $P1, $kwpActuals); return; } catch (FailReturnFromVoidException e){};
       
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0, $P1));
    }
    public void jobIsCancelled(IValue $P0){ // Generated by Resolver
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type,$T0)){
         try { util_Monitor_jobIsCancelled$ae51a0847a2bc628((IString) $P0); return; } catch (FailReturnFromVoidException e){};
       
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public void println(IValue $P0){ // Generated by Resolver
        M_IO.println($P0);
    }
    public void println(){ // Generated by Resolver
        M_IO.println();
    }
    public IBool unfinishedInputTest(){ // Generated by Resolver
       IBool $result = null;
       $result = (IBool)util_Monitor_unfinishedInputTest$da9d8a429289172b();
       if($result != null) return $result;
       throw RuntimeExceptionFactory.callFailed($RVF.list());
    }
    public IBool simpleAsyncPrintTest(){ // Generated by Resolver
       IBool $result = null;
       $result = (IBool)util_Monitor_simpleAsyncPrintTest$924211391293f22a();
       if($result != null) return $result;
       throw RuntimeExceptionFactory.callFailed($RVF.list());
    }
    public IInteger jobEnd(IValue $P0, java.util.Map<java.lang.String,IValue> $kwpActuals){ // Generated by Resolver
       IInteger $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type,$T0)){
         $result = (IInteger)util_Monitor_jobEnd$0d9e8a42d2c9205b((IString) $P0, $kwpActuals);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public IInteger arbInt(IValue $P0){ // Generated by Resolver
       return (IInteger) M_util_Math.arbInt($P0);
    }
    public IInteger arbInt(){ // Generated by Resolver
       return (IInteger) M_util_Math.arbInt();
    }
    public void jobWarning(IValue $P0, IValue $P1){ // Generated by Resolver
       Type $P0Type = $P0.getType();
       Type $P1Type = $P1.getType();
       if($isSubtypeOf($P0Type,$T8) && $isSubtypeOf($P1Type,$T13)){
         try { util_Monitor_jobWarning$616762a66a90052b((IString) $P0, (ISourceLocation) $P1); return; } catch (FailReturnFromVoidException e){};
       
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0, $P1));
    }

    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/util/Monitor.rsc|(478,1430,<18,0>,<41,61>) 
    public void util_Monitor_jobStart$6eca9de903da5efd(IString label_0, java.util.Map<java.lang.String,IValue> $kwpActuals){ 
        
        java.util.Map<java.lang.String,IValue> $kwpDefaults = $kwpDefaults_util_Monitor_jobStart$6eca9de903da5efd;
    
        $Monitor.jobStart(label_0, (IInteger)($kwpActuals.containsKey("work") ? $kwpActuals.get("work") : $kwpDefaults.get("work")), (IInteger)($kwpActuals.containsKey("totalWork") ? $kwpActuals.get("totalWork") : $kwpDefaults.get("totalWork"))); 
        return;
        
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/util/Monitor.rsc|(1910,200,<43,0>,<45,56>) 
    public void util_Monitor_jobStep$c4ecb3797f3fc35b(IString label_0, IString message_1, java.util.Map<java.lang.String,IValue> $kwpActuals){ 
        
        java.util.Map<java.lang.String,IValue> $kwpDefaults = $kwpDefaults_util_Monitor_jobStep$c4ecb3797f3fc35b;
    
        $Monitor.jobStep(label_0, message_1, (IInteger)($kwpActuals.containsKey("work") ? $kwpActuals.get("work") : $kwpDefaults.get("work"))); 
        return;
        
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/util/Monitor.rsc|(2112,126,<47,0>,<49,46>) 
    public IInteger util_Monitor_jobEnd$0d9e8a42d2c9205b(IString label_0, java.util.Map<java.lang.String,IValue> $kwpActuals){ 
        
        java.util.Map<java.lang.String,IValue> $kwpDefaults = $kwpDefaults_util_Monitor_jobEnd$0d9e8a42d2c9205b;
    
        return ((IInteger)((IInteger)$Monitor.jobEnd(label_0, (IBool)($kwpActuals.containsKey("success") ? $kwpActuals.get("success") : $kwpDefaults.get("success")))));
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/util/Monitor.rsc|(2240,147,<51,0>,<53,41>) 
    public void util_Monitor_jobTodo$e0b8657ec7d3254d(IString label_0, java.util.Map<java.lang.String,IValue> $kwpActuals){ 
        
        java.util.Map<java.lang.String,IValue> $kwpDefaults = $kwpDefaults_util_Monitor_jobTodo$e0b8657ec7d3254d;
    
        $Monitor.jobTodo(label_0, (IInteger)($kwpActuals.containsKey("work") ? $kwpActuals.get("work") : $kwpDefaults.get("work"))); 
        return;
        
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/util/Monitor.rsc|(2389,159,<55,0>,<57,36>) 
    public void util_Monitor_jobIsCancelled$ae51a0847a2bc628(IString label_0){ 
        
        
        $Monitor.jobIsCancelled(label_0); 
        return;
        
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/util/Monitor.rsc|(2550,167,<59,0>,<61,43>) 
    public void util_Monitor_jobWarning$616762a66a90052b(IString message_0, ISourceLocation src_1){ 
        
        
        $Monitor.jobWarning(message_0, src_1); 
        return;
        
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/util/Monitor.rsc|(3447,83,<78,17>,<80,5>) 
    public void $CLOSURE_0(IString message_0, IInteger worked_1, ValueRef<IString> label_0){ 
        
        
        $me.jobStep(label_0.getValue(), ((IString)message_0), Util.kwpMap("work", worked_1));
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/util/Monitor.rsc|(2719,885,<63,0>,<88,1>) 
    public IValue util_Monitor_job$709fc0dd3ef7ac2b(IString $aux_label_0, TypedFunctionInstance1<IValue, IValue> block_1, java.util.Map<java.lang.String,IValue> $kwpActuals){ 
        ValueRef<IString> label_0 = new ValueRef<IString>("label_0", $aux_label_0);
    
        java.util.Map<java.lang.String,IValue> $kwpDefaults = $kwpDefaults_util_Monitor_job$709fc0dd3ef7ac2b;
    
        HashMap<io.usethesource.vallang.type.Type,io.usethesource.vallang.type.Type> $typeBindings = new HashMap<>();
        if($T7.match(block_1.getType(), $typeBindings)){
           try {
                $me.jobStart(label_0.getValue(), Util.kwpMapExtend(Util.kwpMapRemoveRedeclared($kwpActuals, "totalWork"), "totalWork", ((IInteger) ($kwpActuals.containsKey("totalWork") ? $kwpActuals.get("totalWork") : $kwpDefaults.get("totalWork")))));
                final IValue $result1 = ((IValue)(((TypedFunctionInstance1<IValue, IValue>)block_1).typedCall(new TypedFunctionInstance2<IValue,IValue,IValue>(($3447_0, $3447_1) -> { $CLOSURE_0((IString)$3447_0, (IInteger)$3447_1, label_0);return null; }, $T14))));
                if($T15.instantiate($typeBindings) != $TF.voidType() && $isSubtypeOf($result1.getType(),$T15)){
                   return ((IValue)($result1));
                
                } else {
                   return null;
                }
           } catch (Throw $thrown0_as_exception) {
                IValue $thrown0 = $thrown0_as_exception.getException();
               
                IValue x_3 = ((IValue)($thrown0));
                throw new Throw(x_3);
            }
           finally { 
               $me.jobEnd(label_0.getValue(), $kwpActuals);} 
                      
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/util/Monitor.rsc|(4502,68,<109,17>,<111,5>) 
    public void $CLOSURE_1(IInteger worked_0, ValueRef<IString> label_0){ 
        
        
        $me.jobStep(label_0.getValue(), label_0.getValue(), Util.kwpMap("work", worked_0));
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/util/Monitor.rsc|(3606,1037,<90,0>,<119,1>) 
    public IValue util_Monitor_job$099df0963e4ce399(IString $aux_label_0, TypedFunctionInstance1<IValue, IValue> block_1, java.util.Map<java.lang.String,IValue> $kwpActuals){ 
        ValueRef<IString> label_0 = new ValueRef<IString>("label_0", $aux_label_0);
    
        java.util.Map<java.lang.String,IValue> $kwpDefaults = $kwpDefaults_util_Monitor_job$099df0963e4ce399;
    
        HashMap<io.usethesource.vallang.type.Type,io.usethesource.vallang.type.Type> $typeBindings = new HashMap<>();
        if($T11.match(block_1.getType(), $typeBindings)){
           if($isSubtypeOf(block_1.getType(),$T16.instantiate($typeBindings))){
              throw new Throw($RVF.constructor(M_Exception.RuntimeException_IllegalArgument_value_str, new IValue[]{((IValue)block_1), ((IString)$constants.get(3)/*"`block` argument can not be used by job because it returns `void` and `job` must return something."*/)}));
           }
           try {
                $me.jobStart(label_0.getValue(), Util.kwpMapExtend(Util.kwpMapRemoveRedeclared($kwpActuals, "totalWork"), "totalWork", ((IInteger) ($kwpActuals.containsKey("totalWork") ? $kwpActuals.get("totalWork") : $kwpDefaults.get("totalWork")))));
                final IValue $result3 = ((IValue)(((TypedFunctionInstance1<IValue, IValue>)block_1).typedCall(new TypedFunctionInstance1<IValue,IValue>(($4502_0) -> { $CLOSURE_1((IInteger)$4502_0, label_0);return null; }, $T19))));
                if($T15.instantiate($typeBindings) != $TF.voidType() && $isSubtypeOf($result3.getType(),$T15)){
                   return ((IValue)($result3));
                
                } else {
                   return null;
                }
           } catch (Throw $thrown2_as_exception) {
                IValue $thrown2 = $thrown2_as_exception.getException();
               
                IValue x_3 = ((IValue)($thrown2));
                throw new Throw(x_3);
            }
           finally { 
               $me.jobEnd(label_0.getValue(), $kwpActuals);} 
                      
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/util/Monitor.rsc|(5336,52,<136,17>,<138,5>) 
    public void $CLOSURE_2(ValueRef<IString> label_0){ 
        
        
        $me.jobStep(label_0.getValue(), label_0.getValue(), Util.kwpMap("work", ((IInteger)$constants.get(0)/*1*/)));
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/util/Monitor.rsc|(4645,816,<121,0>,<146,1>) 
    public IValue util_Monitor_job$67c9665eb8ce1e4f(IString $aux_label_0, TypedFunctionInstance1<IValue, IValue> block_1, java.util.Map<java.lang.String,IValue> $kwpActuals){ 
        ValueRef<IString> label_0 = new ValueRef<IString>("label_0", $aux_label_0);
    
        java.util.Map<java.lang.String,IValue> $kwpDefaults = $kwpDefaults_util_Monitor_job$67c9665eb8ce1e4f;
    
        HashMap<io.usethesource.vallang.type.Type,io.usethesource.vallang.type.Type> $typeBindings = new HashMap<>();
        if($T4.match(block_1.getType(), $typeBindings)){
           try {
                $me.jobStart(label_0.getValue(), Util.kwpMapExtend(Util.kwpMapRemoveRedeclared($kwpActuals, "totalWork"), "totalWork", ((IInteger) ($kwpActuals.containsKey("totalWork") ? $kwpActuals.get("totalWork") : $kwpDefaults.get("totalWork")))));
                final IValue $result5 = ((IValue)(((TypedFunctionInstance1<IValue, IValue>)block_1).typedCall(new TypedFunctionInstance0<IValue>(() -> { $CLOSURE_2(label_0);return null; }, $T20))));
                if($T15.instantiate($typeBindings) != $TF.voidType() && $isSubtypeOf($result5.getType(),$T15)){
                   return ((IValue)($result5));
                
                } else {
                   return null;
                }
           } catch (Throw $thrown4_as_exception) {
                IValue $thrown4 = $thrown4_as_exception.getException();
               
                IValue x_3 = ((IValue)($thrown4));
                throw new Throw(x_3);
            }
           finally { 
               $me.jobEnd(label_0.getValue(), $kwpActuals);} 
                      
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/util/Monitor.rsc|(5463,455,<148,0>,<164,1>) 
    public IValue util_Monitor_job$93fbc1bd1b37a8be(IString label_0, TypedFunctionInstance0<IValue> block_1, java.util.Map<java.lang.String,IValue> $kwpActuals){ 
        
        java.util.Map<java.lang.String,IValue> $kwpDefaults = $kwpDefaults_util_Monitor_job$93fbc1bd1b37a8be;
    
        HashMap<io.usethesource.vallang.type.Type,io.usethesource.vallang.type.Type> $typeBindings = new HashMap<>();
        if($T1.match(block_1.getType(), $typeBindings)){
           try {
                $me.jobStart(((IString)label_0), Util.kwpMapExtend(Util.kwpMapRemoveRedeclared($kwpActuals, "totalWork"), "totalWork", ((IInteger) ($kwpActuals.containsKey("totalWork") ? $kwpActuals.get("totalWork") : $kwpDefaults.get("totalWork")))));
                final IValue $result7 = ((IValue)(((TypedFunctionInstance0<IValue>)block_1).typedCall()));
                if($T15.instantiate($typeBindings) != $TF.voidType() && $isSubtypeOf($result7.getType(),$T15)){
                   return ((IValue)($result7));
                
                } else {
                   return null;
                }
           } catch (Throw $thrown6_as_exception) {
                IValue $thrown6 = $thrown6_as_exception.getException();
               
                IValue x_3 = ((IValue)($thrown6));
                throw new Throw(x_3);
            }
           finally { 
               $me.jobEnd(((IString)label_0), $kwpActuals);} 
                      
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/util/Monitor.rsc|(5920,824,<166,0>,<194,1>) 
    public IBool util_Monitor_horseRaceTest$4c48533f01fc7dde(){ 
        
        
        IInteger distance_0 = ((IInteger)$constants.get(4)/*3000000*/);
        IInteger stride_1 = ((IInteger)$constants.get(5)/*50*/);
        IInteger horses_2 = ((IInteger)$constants.get(6)/*5*/);
        final IListWriter $listwriter8 = (IListWriter)$RVF.listWriter();
        final IInteger $lst2 = ((IInteger)horses_2);
        final boolean $dir3 = ((IInteger)$constants.get(7)/*0*/).less($lst2).getValue();
        
        $LCOMP9_GEN6132:
        for(IInteger $elem10 = ((IInteger)$constants.get(7)/*0*/); $dir3 ? $aint_less_aint($elem10,$lst2).getValue() 
                                  : $aint_lessequal_aint($elem10,$lst2).not().getValue(); $elem10 = $aint_add_aint($elem10,$dir3 ? ((IInteger)$constants.get(0)/*1*/) : ((IInteger)$constants.get(8)/*-1*/))){
            $listwriter8.append(M_util_Math.arbInt(((IInteger)($aint_divide_aint(((IInteger)($aint_product_aint(((IInteger)stride_1),((IInteger)$constants.get(9)/*15*/)))),((IInteger)$constants.get(1)/*100*/))))));
        }
        
        IList handicaps_3 = ((IList)($listwriter8.done()));
        final IListWriter $listwriter11 = (IListWriter)$RVF.listWriter();
        final IInteger $lst7 = ((IInteger)horses_2);
        final boolean $dir8 = ((IInteger)$constants.get(7)/*0*/).less($lst7).getValue();
        
        $LCOMP12_GEN6210:
        for(IInteger $elem14 = ((IInteger)$constants.get(7)/*0*/); $dir8 ? $aint_less_aint($elem14,$lst7).getValue() 
                                  : $aint_lessequal_aint($elem14,$lst7).not().getValue(); $elem14 = $aint_add_aint($elem14,$dir8 ? ((IInteger)$constants.get(0)/*1*/) : ((IInteger)$constants.get(8)/*-1*/))){
            IInteger h_5 = null;
            final Template $template13 = (Template)new Template($RVF, "Horse ");
            $template13.beginIndent("      ");
            $template13.addVal($elem14);
            $template13.endIndent("      ");
            $template13.addStr(" (handicap is ");
            $template13.beginIndent("              ");
            $template13.addVal($alist_subscript_int(((IList)handicaps_3),((IInteger)($elem14)).intValue()));
            $template13.endIndent("              ");
            $template13.addStr(")");
            $listwriter11.append($template13.close());
        }
        
        IList labels_4 = ((IList)($listwriter11.done()));
        final IListWriter $listwriter15 = (IListWriter)$RVF.listWriter();
        final IInteger $lst12 = ((IInteger)horses_2);
        final boolean $dir13 = ((IInteger)$constants.get(7)/*0*/).less($lst12).getValue();
        
        $LCOMP16_GEN6276:
        for(IInteger $elem17 = ((IInteger)$constants.get(7)/*0*/); $dir13 ? $aint_less_aint($elem17,$lst12).getValue() 
                                  : $aint_lessequal_aint($elem17,$lst12).not().getValue(); $elem17 = $aint_add_aint($elem17,$dir13 ? ((IInteger)$constants.get(0)/*1*/) : ((IInteger)$constants.get(8)/*-1*/))){
            $listwriter15.append(((IInteger)$constants.get(7)/*0*/));
        }
        
        IList progress_6 = ((IList)($listwriter15.done()));
        /*muExists*/FOR5: 
            do {
                final IInteger $lst17 = ((IInteger)horses_2);
                final boolean $dir18 = ((IInteger)$constants.get(7)/*0*/).less($lst17).getValue();
                
                FOR5_GEN6304:
                for(IInteger $elem18 = ((IInteger)$constants.get(7)/*0*/); $dir18 ? $aint_less_aint($elem18,$lst17).getValue() 
                                          : $aint_lessequal_aint($elem18,$lst17).not().getValue(); $elem18 = $aint_add_aint($elem18,$dir18 ? ((IInteger)$constants.get(0)/*1*/) : ((IInteger)$constants.get(8)/*-1*/))){
                    IInteger h_7 = ((IInteger)($elem18));
                    $me.jobStart(((IString)($alist_subscript_int(((IList)labels_4),((IInteger)h_7).intValue()))), Util.kwpMap("totalWork", distance_0));
                }
                continue FOR5;
        
            } while(false);
        /* void:  muCon([]) *//*muExists*/race_BT: 
            do {
                race:
                    while(true){
                        /*muExists*/FOR6: 
                            do {
                                final IInteger $lst22 = ((IInteger)horses_2);
                                final boolean $dir23 = ((IInteger)$constants.get(7)/*0*/).less($lst22).getValue();
                                
                                FOR6_GEN6402:
                                for(IInteger $elem20 = ((IInteger)$constants.get(7)/*0*/); $dir23 ? $aint_less_aint($elem20,$lst22).getValue() 
                                                          : $aint_lessequal_aint($elem20,$lst22).not().getValue(); $elem20 = $aint_add_aint($elem20,$dir23 ? ((IInteger)$constants.get(0)/*1*/) : ((IInteger)$constants.get(8)/*-1*/))){
                                    IInteger h_8 = ((IInteger)($elem20));
                                    IInteger advance_9 = ((IInteger)(M_util_Math.arbInt(((IInteger)(((IInteger) ((IInteger)stride_1).subtract(((IInteger)($alist_subscript_int(((IList)handicaps_3),((IInteger)h_8).intValue()))))))))));
                                    progress_6 = ((IList)($alist_update(((IInteger)h_8).intValue(),$aint_add_aint(((IInteger)($alist_subscript_int(((IList)progress_6),((IInteger)h_8).intValue()))),((IInteger)advance_9)),((IList)(progress_6)))));
                                    final Template $template19 = (Template)new Template($RVF, "Pacing horse ");
                                    $template19.beginIndent("             ");
                                    $template19.addVal(h_8);
                                    $template19.endIndent("             ");
                                    $template19.addStr(" with ");
                                    $template19.beginIndent("      ");
                                    $template19.addVal(advance_9);
                                    $template19.endIndent("      ");
                                    $template19.addStr("...");
                                    $me.jobStep(((IString)($alist_subscript_int(((IList)labels_4),((IInteger)h_8).intValue()))), ((IString)($template19.close())), Util.kwpMap("work", advance_9));
                                    if((((IBool)($aint_less_aint(((IInteger)($alist_subscript_int(((IList)progress_6),((IInteger)h_8).intValue()))),((IInteger)distance_0)).not()))).getValue()){
                                       break race; // muBreak
                                    
                                    }
                                }
                                continue FOR6;
                        
                            } while(false);
                        /* void:  muCon([]) */
                    }
        
            } while(false);
        /* void:  muCon([]) *//*muExists*/FOR8: 
            do {
                final IInteger $lst27 = ((IInteger)horses_2);
                final boolean $dir28 = ((IInteger)$constants.get(7)/*0*/).less($lst27).getValue();
                
                FOR8_GEN6681:
                for(IInteger $elem21 = ((IInteger)$constants.get(7)/*0*/); $dir28 ? $aint_less_aint($elem21,$lst27).getValue() 
                                          : $aint_lessequal_aint($elem21,$lst27).not().getValue(); $elem21 = $aint_add_aint($elem21,$dir28 ? ((IInteger)$constants.get(0)/*1*/) : ((IInteger)$constants.get(8)/*-1*/))){
                    IInteger h_10 = ((IInteger)($elem21));
                    $me.jobEnd(((IString)($alist_subscript_int(((IList)labels_4),((IInteger)h_10).intValue()))), Util.kwpMap());
                }
                continue FOR8;
        
            } while(false);
        /* void:  muCon([]) */return ((IBool)$constants.get(2)/*true*/);
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/util/Monitor.rsc|(6746,272,<196,0>,<207,1>) 
    public IBool util_Monitor_simpleAsyncPrintTest$924211391293f22a(){ 
        
        
        $me.jobStart(((IString)$constants.get(10)/*"job"*/), Util.kwpMap("totalWork", ((IInteger)$constants.get(11)/*3*/)));
        M_IO.println(((IString)$constants.get(12)/*"a"*/));
        $me.jobStep(((IString)$constants.get(10)/*"job"*/), ((IString)$constants.get(13)/*"step 1"*/), Util.kwpMap("work", ((IInteger)$constants.get(0)/*1*/)));
        M_IO.println(((IString)$constants.get(14)/*"b"*/));
        $me.jobStep(((IString)$constants.get(10)/*"job"*/), ((IString)$constants.get(15)/*"step 2"*/), Util.kwpMap("work", ((IInteger)$constants.get(0)/*1*/)));
        M_IO.println(((IString)$constants.get(16)/*"c"*/));
        $me.jobStep(((IString)$constants.get(10)/*"job"*/), ((IString)$constants.get(17)/*"step 3"*/), Util.kwpMap("work", ((IInteger)$constants.get(0)/*1*/)));
        M_IO.println(((IString)$constants.get(18)/*"d"*/));
        $me.jobEnd(((IString)$constants.get(10)/*"job"*/), Util.kwpMap());
        return ((IBool)$constants.get(2)/*true*/);
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/util/Monitor.rsc|(7020,319,<209,0>,<221,1>) 
    public IBool util_Monitor_unfinishedInputTest$da9d8a429289172b(){ 
        
        
        $me.jobStart(((IString)$constants.get(10)/*"job"*/), Util.kwpMap("totalWork", ((IInteger)$constants.get(19)/*26*/)));
        /*muExists*/FOR9: 
            do {
                final IString $subject_val23 = ((IString)$constants.get(20)/*"abcdefghijklmnopqrstuwvxyz"*/);
                final Matcher $matcher24 = (Matcher)$regExpCompile("([a-z])", ((IString)($subject_val23)).getValue());
                boolean $found25 = true;
                
                    while($found25){
                        $found25 = $matcher24.find();
                        if($found25){
                           IString l_0 = ((IString)($RVF.string($matcher24.group(1))));
                           M_IO.print(((IValue)l_0));
                           final Template $template22 = (Template)new Template($RVF, "letter ");
                           $template22.beginIndent("       ");
                           $template22.addStr(((IString)l_0).getValue());
                           $template22.endIndent("       ");
                           $me.jobStep(((IString)$constants.get(10)/*"job"*/), ((IString)($template22.close())), Util.kwpMap("work", ((IInteger)$constants.get(0)/*1*/)));
                           if((((IBool)($equal(((IInteger)(M_util_Math.arbInt(((IInteger)$constants.get(21)/*10*/)))), ((IInteger)$constants.get(7)/*0*/))))).getValue()){
                              M_IO.println();
                           
                           }
                        
                        } else {
                           continue FOR9;
                        }
                    }
        
            } while(false);
        /* void:  muCon([]) */$me.jobEnd(((IString)$constants.get(10)/*"job"*/), Util.kwpMap());
        return ((IBool)$constants.get(2)/*true*/);
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/util/Monitor.rsc|(7341,259,<223,0>,<233,1>) 
    public IBool util_Monitor_unfinishedLinesAtTheEndTest$570d0a42211dec4a(){ 
        
        
        $me.jobStart(((IString)$constants.get(10)/*"job"*/), Util.kwpMap("totalWork", ((IInteger)$constants.get(11)/*3*/)));
        M_IO.print(((IString)$constants.get(22)/*"ab
        c"*/));
        $me.jobStep(((IString)$constants.get(10)/*"job"*/), ((IString)$constants.get(23)/*"1.5"*/), Util.kwpMap("work", ((IInteger)$constants.get(0)/*1*/)));
        M_IO.print(((IString)$constants.get(24)/*"d
        e"*/));
        $me.jobStep(((IString)$constants.get(10)/*"job"*/), ((IString)$constants.get(25)/*"2.5"*/), Util.kwpMap("work", ((IInteger)$constants.get(0)/*1*/)));
        M_IO.print(((IString)$constants.get(26)/*"f
        gh
        "*/));
        $me.jobStep(((IString)$constants.get(10)/*"job"*/), ((IString)$constants.get(27)/*"3"*/), Util.kwpMap("work", ((IInteger)$constants.get(0)/*1*/)));
        $me.jobEnd(((IString)$constants.get(10)/*"job"*/), Util.kwpMap());
        return ((IBool)$constants.get(2)/*true*/);
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/util/Monitor.rsc|(7625,292,<236,0>,<244,1>) 
    public IBool util_Monitor_printLongUnfinishedLine$2f78e7621c7925e7(){ 
        
        
        $me.jobStart(((IString)$constants.get(10)/*"job"*/), Util.kwpMap("totalWork", ((IInteger)$constants.get(0)/*1*/)));
        IString $reducer27 = (IString)(((IString)$constants.get(28)/*""*/));
        
        $REDUCER26_GEN7745:
        for(IInteger $elem28 = ((IInteger)$constants.get(7)/*0*/); $aint_less_aint($elem28,((IInteger)$constants.get(29)/*1000000*/)).getValue(); $elem28 = $aint_add_aint($elem28,((IInteger)$constants.get(0)/*1*/))){
            IInteger i_2 = null;
            $reducer27 = ((IString)($astr_add_astr(((IString)($reducer27)),((IString)$constants.get(30)/*"ab"*/))));
        }
        
        IString singleString_0 = ((IString)(M_IO.iprintToString(((IValue)($reducer27)))));
        M_IO.println(((IValue)singleString_0));
        $me.jobStep(((IString)$constants.get(10)/*"job"*/), ((IString)$constants.get(31)/*"prog"*/), Util.kwpMap("work", ((IInteger)$constants.get(0)/*1*/)));
        M_IO.println(((IString)$constants.get(32)/*"Done"*/));
        $me.jobEnd(((IString)$constants.get(10)/*"job"*/), Util.kwpMap());
        return ((IBool)$constants.get(2)/*true*/);
    
    }
    

    public static void main(String[] args) {
      throw new RuntimeException("No function `main` found in Rascal module `util::Monitor`");
    }
}