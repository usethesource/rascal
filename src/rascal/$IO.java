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
public class $IO 
    extends
        org.rascalmpl.runtime.$RascalModule
    implements 
    	rascal.$IO_$I {

    private final $IO_$I $me;
    private final IList $constants;
    final java.util.Map<java.lang.String,IValue> $kwpDefaults_IO_iprint$8519f29f09d8b95d;
    final java.util.Map<java.lang.String,IValue> $kwpDefaults_IO_iprintln$e630d4c5c6614fca;
    final java.util.Map<java.lang.String,IValue> $kwpDefaults_IO_readBase64$e403bc4c7f63d90b;
    final java.util.Map<java.lang.String,IValue> $kwpDefaults_IO_readBase32$c3263d5cb496b291;
    final java.util.Map<java.lang.String,IValue> $kwpDefaults_IO_remove$7ee95f40dde4956c;
    final java.util.Map<java.lang.String,IValue> $kwpDefaults_IO_toBase64$444f08dbde4c25dd;
    final java.util.Map<java.lang.String,IValue> $kwpDefaults_IO_copy$2b7816b64efe7fa8;
    final java.util.Map<java.lang.String,IValue> $kwpDefaults_IO_move$7b018fae0f5aced6;

    
    public final rascal.$Exception M_Exception;

    
    final org.rascalmpl.library.Prelude $Prelude; // TODO: asBaseClassName will generate name collisions if there are more of the same name in different packages

    
    public IString DEFAULT_CHARSET;
    public final io.usethesource.vallang.type.Type $T0;	/*aloc()*/
    public final io.usethesource.vallang.type.Type $T3;	/*astr()*/
    public final io.usethesource.vallang.type.Type $T15;	/*aloc(alabel="physical")*/
    public final io.usethesource.vallang.type.Type $T19;	/*aloc(alabel="singleton")*/
    public final io.usethesource.vallang.type.Type $T14;	/*aloc(alabel="logical")*/
    public final io.usethesource.vallang.type.Type $T9;	/*avoid()*/
    public final io.usethesource.vallang.type.Type $T1;	/*avalue()*/
    public final io.usethesource.vallang.type.Type $T2;	/*adatetime()*/
    public final io.usethesource.vallang.type.Type $T11;	/*aint()*/
    public final io.usethesource.vallang.type.Type $T20;	/*aparameter("T",avalue(),closed=true)*/
    public final io.usethesource.vallang.type.Type $T16;	/*aloc(alabel="src")*/
    public final io.usethesource.vallang.type.Type $T6;	/*aparameter("T",avalue(),closed=false)*/
    public final io.usethesource.vallang.type.Type ADT_LocationChangeType;	/*aadt("LocationChangeType",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_IOCapability;	/*aadt("IOCapability",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type IOCapability_watching_;	/*acons(aadt("IOCapability",[],dataSyntax()),[],[],alabel="watching")*/
    public final io.usethesource.vallang.type.Type ADT_LocationChangeEvent;	/*aadt("LocationChangeEvent",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type LocationChangeType_created_;	/*acons(aadt("LocationChangeType",[],dataSyntax()),[],[],alabel="created")*/
    public final io.usethesource.vallang.type.Type LocationChangeType_deleted_;	/*acons(aadt("LocationChangeType",[],dataSyntax()),[],[],alabel="deleted")*/
    public final io.usethesource.vallang.type.Type $T18;	/*aset(aloc(alabel="singleton"))*/
    public final io.usethesource.vallang.type.Type $T10;	/*alist(aint())*/
    public final io.usethesource.vallang.type.Type IOCapability_writing_;	/*acons(aadt("IOCapability",[],dataSyntax()),[],[],alabel="writing")*/
    public final io.usethesource.vallang.type.Type $T5;	/*alist(avalue())*/
    public final io.usethesource.vallang.type.Type $T13;	/*amap(aloc(alabel="logical"),aloc(alabel="physical"))*/
    public final io.usethesource.vallang.type.Type $T4;	/*alist(astr())*/
    public final io.usethesource.vallang.type.Type IOCapability_classloading_;	/*acons(aadt("IOCapability",[],dataSyntax()),[],[],alabel="classloading")*/
    public final io.usethesource.vallang.type.Type ADT_RuntimeException;	/*aadt("RuntimeException",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type IOCapability_resolving_;	/*acons(aadt("IOCapability",[],dataSyntax()),[],[],alabel="resolving")*/
    public final io.usethesource.vallang.type.Type ADT_LocationType;	/*aadt("LocationType",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type $T17;	/*aset(avoid())*/
    public final io.usethesource.vallang.type.Type LocationChangeType_modified_;	/*acons(aadt("LocationChangeType",[],dataSyntax()),[],[],alabel="modified")*/
    public final io.usethesource.vallang.type.Type IOCapability_reading_;	/*acons(aadt("IOCapability",[],dataSyntax()),[],[],alabel="reading")*/
    public final io.usethesource.vallang.type.Type LocationType_file_;	/*acons(aadt("LocationType",[],dataSyntax()),[],[],alabel="file")*/
    public final io.usethesource.vallang.type.Type $T12;	/*alist(aloc())*/
    public final io.usethesource.vallang.type.Type LocationChangeEvent_changeEvent_loc_LocationChangeType_LocationType;	/*acons(aadt("LocationChangeEvent",[],dataSyntax()),[aloc(alabel="src"),aadt("LocationChangeType",[],dataSyntax(),alabel="changeType"),aadt("LocationType",[],dataSyntax(),alabel="type")],[],alabel="changeEvent")*/
    public final io.usethesource.vallang.type.Type LocationType_directory_;	/*acons(aadt("LocationType",[],dataSyntax()),[],[],alabel="directory")*/
    public final io.usethesource.vallang.type.Type $T8;	/*afunc(avoid(),[aadt("LocationChangeEvent",[],dataSyntax(),alabel="event")],[])*/
    public final io.usethesource.vallang.type.Type $T7;	/*abool()*/

    public $IO(RascalExecutionContext rex){
        this(rex, null);
    }
    
    public $IO(RascalExecutionContext rex, Object extended){
       super(rex);
       this.$me = extended == null ? this : ($IO_$I)extended;
       ModuleStore mstore = rex.getModuleStore();
       mstore.put(rascal.$IO.class, this);
       
       mstore.importModule(rascal.$Exception.class, rex, rascal.$Exception::new); 
       
       M_Exception = mstore.getModule(rascal.$Exception.class); 
       
                          
       
       $TS.importStore(M_Exception.$TS);
       
       $Prelude = $initLibrary("org.rascalmpl.library.Prelude"); 
    
       $constants = readBinaryConstantsFile(this.getClass(), "rascal//$IO.constants", 6, "f4ee18b8e7f7c5632a4110e0b791c5aa");
       ADT_LocationChangeType = $adt("LocationChangeType");
       ADT_IOCapability = $adt("IOCapability");
       ADT_LocationChangeEvent = $adt("LocationChangeEvent");
       ADT_RuntimeException = $adt("RuntimeException");
       ADT_LocationType = $adt("LocationType");
       $T0 = $TF.sourceLocationType();
       $T3 = $TF.stringType();
       $T15 = $TF.sourceLocationType();
       $T19 = $TF.sourceLocationType();
       $T14 = $TF.sourceLocationType();
       $T9 = $TF.voidType();
       $T1 = $TF.valueType();
       $T2 = $TF.dateTimeType();
       $T11 = $TF.integerType();
       $T20 = $TF.parameterType("T", $T1);
       $T16 = $TF.sourceLocationType();
       $T6 = $TF.parameterType("T", $T1);
       $T18 = $TF.setType($T19);
       $T10 = $TF.listType($T11);
       $T5 = $TF.listType($T1);
       $T13 = $TF.mapType($T14, "logical", $T15, "physical");
       $T4 = $TF.listType($T3);
       $T17 = $TF.setType($T9);
       $T12 = $TF.listType($T0);
       $T8 = $TF.functionType($T9, $TF.tupleType(ADT_LocationChangeEvent, "event"), $TF.tupleEmpty());
       $T7 = $TF.boolType();
       IOCapability_watching_ = $TF.constructor($TS, ADT_IOCapability, "watching");
       LocationChangeType_created_ = $TF.constructor($TS, ADT_LocationChangeType, "created");
       LocationChangeType_deleted_ = $TF.constructor($TS, ADT_LocationChangeType, "deleted");
       IOCapability_writing_ = $TF.constructor($TS, ADT_IOCapability, "writing");
       IOCapability_classloading_ = $TF.constructor($TS, ADT_IOCapability, "classloading");
       IOCapability_resolving_ = $TF.constructor($TS, ADT_IOCapability, "resolving");
       LocationChangeType_modified_ = $TF.constructor($TS, ADT_LocationChangeType, "modified");
       IOCapability_reading_ = $TF.constructor($TS, ADT_IOCapability, "reading");
       LocationType_file_ = $TF.constructor($TS, ADT_LocationType, "file");
       LocationChangeEvent_changeEvent_loc_LocationChangeType_LocationType = $TF.constructor($TS, ADT_LocationChangeEvent, "changeEvent", $TF.sourceLocationType(), "src", ADT_LocationChangeType, "changeType", ADT_LocationType, "type");
       LocationType_directory_ = $TF.constructor($TS, ADT_LocationType, "directory");
    
       DEFAULT_CHARSET = ((IString)$constants.get(5)/*"UTF-8"*/);
    
       $kwpDefaults_IO_iprint$8519f29f09d8b95d = Util.kwpMap("lineLimit", ((IInteger)$constants.get(4)/*1000*/));
       $kwpDefaults_IO_iprintln$e630d4c5c6614fca = Util.kwpMap("lineLimit", ((IInteger)$constants.get(4)/*1000*/));
       $kwpDefaults_IO_readBase64$e403bc4c7f63d90b = Util.kwpMap("includePadding", ((IBool)$constants.get(3)/*true*/));
       $kwpDefaults_IO_readBase32$c3263d5cb496b291 = Util.kwpMap("includePadding", ((IBool)$constants.get(3)/*true*/));
       $kwpDefaults_IO_remove$7ee95f40dde4956c = Util.kwpMap("recursive", ((IBool)$constants.get(3)/*true*/));
       $kwpDefaults_IO_toBase64$444f08dbde4c25dd = Util.kwpMap("includePadding", ((IBool)$constants.get(3)/*true*/));
       $kwpDefaults_IO_copy$2b7816b64efe7fa8 = Util.kwpMap("recursive", ((IBool)$constants.get(2)/*false*/), "overwrite", ((IBool)$constants.get(3)/*true*/));
       $kwpDefaults_IO_move$7b018fae0f5aced6 = Util.kwpMap("overwrite", ((IBool)$constants.get(3)/*true*/));
    
    }
    public IBool isDirectory(IValue $P0){ // Generated by Resolver
       IBool $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type,$T0)){
         $result = (IBool)IO_isDirectory$f70ff48d80811e3c((ISourceLocation) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public ISet charsets(){ // Generated by Resolver
       ISet $result = null;
       $result = (ISet)IO_charsets$5741e8924db30a5d();
       if($result != null) return $result;
       throw RuntimeExceptionFactory.callFailed($RVF.list());
    }
    public void rprintln(IValue $P0){ // Generated by Resolver
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type,$T1)){
         try { IO_rprintln$8b703d3f54a01b68((IValue) $P0); return; } catch (FailReturnFromVoidException e){};
       
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public void setLastModified(IValue $P0, IValue $P1){ // Generated by Resolver
       Type $P0Type = $P0.getType();
       Type $P1Type = $P1.getType();
       if($isSubtypeOf($P0Type,$T0) && $isSubtypeOf($P1Type,$T2)){
         try { IO_setLastModified$77ace42c2a009ccb((ISourceLocation) $P0, (IDateTime) $P1); return; } catch (FailReturnFromVoidException e){};
       
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0, $P1));
    }
    public ISourceLocation resolveLocation(IValue $P0){ // Generated by Resolver
       ISourceLocation $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type,$T0)){
         $result = (ISourceLocation)IO_resolveLocation$ad3865e0ee0205af((ISourceLocation) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public void unregisterLocations(IValue $P0, IValue $P1){ // Generated by Resolver
       Type $P0Type = $P0.getType();
       Type $P1Type = $P1.getType();
       if($isSubtypeOf($P0Type,$T3) && $isSubtypeOf($P1Type,$T3)){
         try { IO_unregisterLocations$8ea0884c966fa575((IString) $P0, (IString) $P1); return; } catch (FailReturnFromVoidException e){};
       
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0, $P1));
    }
    public void remove(IValue $P0, java.util.Map<java.lang.String,IValue> $kwpActuals){ // Generated by Resolver
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type,$T0)){
         try { IO_remove$7ee95f40dde4956c((ISourceLocation) $P0, $kwpActuals); return; } catch (FailReturnFromVoidException e){};
       
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public ISourceLocation getResource(IValue $P0){ // Generated by Resolver
       ISourceLocation $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type,$T3)){
         $result = (ISourceLocation)IO_getResource$a62508a79e67475b((IString) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public void writeFileLines(IValue $P0, IValue $P1, java.util.Map<java.lang.String,IValue> $kwpActuals){ // Generated by Resolver
       Type $P0Type = $P0.getType();
       Type $P1Type = $P1.getType();
       if($isSubtypeOf($P0Type,$T0) && $isSubtypeOf($P1Type,$T4)){
         try { IO_writeFileLines$c9280a460e0da623((ISourceLocation) $P0, (IList) $P1, $kwpActuals); return; } catch (FailReturnFromVoidException e){};
       
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0, $P1));
    }
    public void iprint(IValue $P0, java.util.Map<java.lang.String,IValue> $kwpActuals){ // Generated by Resolver
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type,$T1)){
         try { IO_iprint$8519f29f09d8b95d((IValue) $P0, $kwpActuals); return; } catch (FailReturnFromVoidException e){};
       
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public IString readBase64(IValue $P0, java.util.Map<java.lang.String,IValue> $kwpActuals){ // Generated by Resolver
       IString $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type,$T0)){
         $result = (IString)IO_readBase64$e403bc4c7f63d90b((ISourceLocation) $P0, $kwpActuals);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public void iprintln(IValue $P0, java.util.Map<java.lang.String,IValue> $kwpActuals){ // Generated by Resolver
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type,$T1)){
         try { IO_iprintln$e630d4c5c6614fca((IValue) $P0, $kwpActuals); return; } catch (FailReturnFromVoidException e){};
       
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public IBool isFile(IValue $P0){ // Generated by Resolver
       IBool $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type,$T0)){
         $result = (IBool)IO_isFile$f74fdf3db6ed7ce0((ISourceLocation) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public ISet canEncode(IValue $P0){ // Generated by Resolver
       ISet $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type,$T3)){
         $result = (ISet)IO_canEncode$8f2f7cac6b8255e8((IString) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public IString readBase32(IValue $P0, java.util.Map<java.lang.String,IValue> $kwpActuals){ // Generated by Resolver
       IString $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type,$T0)){
         $result = (IString)IO_readBase32$c3263d5cb496b291((ISourceLocation) $P0, $kwpActuals);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public IDateTime created(IValue $P0){ // Generated by Resolver
       IDateTime $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type,$T0)){
         $result = (IDateTime)IO_created$b9550524d3128d61((ISourceLocation) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public IString md5HashFile(IValue $P0){ // Generated by Resolver
       IString $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type,$T0)){
         $result = (IString)IO_md5HashFile$4f8764080ca4ef11((ISourceLocation) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public IString readFileEnc(IValue $P0, IValue $P1){ // Generated by Resolver
       IString $result = null;
       Type $P0Type = $P0.getType();
       Type $P1Type = $P1.getType();
       if($isSubtypeOf($P0Type,$T0) && $isSubtypeOf($P1Type,$T3)){
         $result = (IString)IO_readFileEnc$28e8b07737a85a02((ISourceLocation) $P0, (IString) $P1);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0, $P1));
    }
    public void appendToFileEnc(IValue $P0, IValue $P1, IValue $P2){ // Generated by Resolver
       Type $P0Type = $P0.getType();
       Type $P1Type = $P1.getType();
       Type $P2Type = $P2.getType();
       if($isSubtypeOf($P0Type,$T0) && $isSubtypeOf($P1Type,$T3) && $isSubtypeOf($P2Type,$T5)){
         try { IO_appendToFileEnc$b0af0d043e746edc((ISourceLocation) $P0, (IString) $P1, (IList) $P2); return; } catch (FailReturnFromVoidException e){};
       
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0, $P1, $P2));
    }
    public IString toBase64(IValue $P0, java.util.Map<java.lang.String,IValue> $kwpActuals){ // Generated by Resolver
       IString $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type,$T0)){
         $result = (IString)IO_toBase64$444f08dbde4c25dd((ISourceLocation) $P0, $kwpActuals);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public ISet findResources(IValue $P0){ // Generated by Resolver
       ISet $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type,$T0)){
         $result = (ISet)IO_findResources$dc47b57efa67f3b1((ISourceLocation) $P0);
         if($result != null) return $result;
       }
       if($isSubtypeOf($P0Type,$T3)){
         $result = (ISet)IO_findResources$e54d9f39f2ac43e7((IString) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public IString md5Hash(IValue $P0){ // Generated by Resolver
       IString $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type,$T1)){
         $result = (IString)IO_md5Hash$8849b64c711c3dc4((IValue) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public IList readFileLines(IValue $P0, java.util.Map<java.lang.String,IValue> $kwpActuals){ // Generated by Resolver
       IList $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type,$T0)){
         $result = (IList)IO_readFileLines$4b1aaff71652c293((ISourceLocation) $P0, $kwpActuals);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public IBool bprintln(IValue $P0){ // Generated by Resolver
       IBool $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type,$T1)){
         $result = (IBool)IO_bprintln$3dd978ad22652384((IValue) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public void print(IValue $P0){ // Generated by Resolver
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type,$T1)){
         try { IO_print$92b0eb45ba8d0cab((IValue) $P0); return; } catch (FailReturnFromVoidException e){};
       
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public IList readFileLinesEnc(IValue $P0, IValue $P1){ // Generated by Resolver
       IList $result = null;
       Type $P0Type = $P0.getType();
       Type $P1Type = $P1.getType();
       if($isSubtypeOf($P0Type,$T0) && $isSubtypeOf($P1Type,$T3)){
         $result = (IList)IO_readFileLinesEnc$3684048af0816067((ISourceLocation) $P0, (IString) $P1);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0, $P1));
    }
    public void uudecode(IValue $P0, IValue $P1){ // Generated by Resolver
       Type $P0Type = $P0.getType();
       Type $P1Type = $P1.getType();
       if($isSubtypeOf($P0Type,$T0) && $isSubtypeOf($P1Type,$T3)){
         try { IO_uudecode$7c69e2e7099865ab((ISourceLocation) $P0, (IString) $P1); return; } catch (FailReturnFromVoidException e){};
       
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0, $P1));
    }
    public void writeBase64(IValue $P0, IValue $P1){ // Generated by Resolver
       Type $P0Type = $P0.getType();
       Type $P1Type = $P1.getType();
       if($isSubtypeOf($P0Type,$T0) && $isSubtypeOf($P1Type,$T3)){
         try { IO_writeBase64$9e98f0af060196b2((ISourceLocation) $P0, (IString) $P1); return; } catch (FailReturnFromVoidException e){};
       
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0, $P1));
    }
    public IValue printlnExp(IValue $P0){ // Generated by Resolver
       IValue $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type,$T6)){
         $result = (IValue)IO_printlnExp$b49750d88cd18560((IValue) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public IValue printlnExp(IValue $P0, IValue $P1){ // Generated by Resolver
       IValue $result = null;
       Type $P0Type = $P0.getType();
       Type $P1Type = $P1.getType();
       if($isSubtypeOf($P0Type,$T3) && $isSubtypeOf($P1Type,$T6)){
         $result = (IValue)IO_printlnExp$a88ede004ad859eb((IString) $P0, (IValue) $P1);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0, $P1));
    }
    public void watch(IValue $P0, IValue $P1, IValue $P2){ // Generated by Resolver
       Type $P0Type = $P0.getType();
       Type $P1Type = $P1.getType();
       Type $P2Type = $P2.getType();
       if($isSubtypeOf($P0Type,$T0) && $isSubtypeOf($P1Type,$T7) && $isSubtypeOf($P2Type,$T8)){
         try { IO_watch$109532ab0f0f0dda((ISourceLocation) $P0, (IBool) $P1, (TypedFunctionInstance1<IValue, IValue>) $P2); return; } catch (FailReturnFromVoidException e){};
       
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0, $P1, $P2));
    }
    public void writeBase32(IValue $P0, IValue $P1){ // Generated by Resolver
       Type $P0Type = $P0.getType();
       Type $P1Type = $P1.getType();
       if($isSubtypeOf($P0Type,$T0) && $isSubtypeOf($P1Type,$T3)){
         try { IO_writeBase32$e46c26e9b423a0ef((ISourceLocation) $P0, (IString) $P1); return; } catch (FailReturnFromVoidException e){};
       
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0, $P1));
    }
    public void mkDirectory(IValue $P0){ // Generated by Resolver
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type,$T0)){
         try { IO_mkDirectory$a327c555c346e973((ISourceLocation) $P0); return; } catch (FailReturnFromVoidException e){};
       
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public ISourceLocation arbLoc(){ // Generated by Resolver
       ISourceLocation $result = null;
       $result = (ISourceLocation)IO_arbLoc$7d94a673b878c7c6();
       if($result != null) return $result;
       throw RuntimeExceptionFactory.callFailed($RVF.list());
    }
    public void println(IValue $P0){ // Generated by Resolver
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type,$T1)){
         try { IO_println$940af00050348a50((IValue) $P0); return; } catch (FailReturnFromVoidException e){};
       
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public void println(){ // Generated by Resolver
       try { IO_println$65b99fec44a8d78d(); return; } catch (FailReturnFromVoidException e){};
       
       throw RuntimeExceptionFactory.callFailed($RVF.list());
    }
    public IString iprintToString(IValue $P0){ // Generated by Resolver
       IString $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type,$T1)){
         $result = (IString)IO_iprintToString$c747648c89d779e2((IValue) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public void writeFileBytes(IValue $P0, IValue $P1){ // Generated by Resolver
       Type $P0Type = $P0.getType();
       Type $P1Type = $P1.getType();
       if($isSubtypeOf($P0Type,$T0) && $isSubtypeOf($P1Type,$T10)){
         try { IO_writeFileBytes$0c8db72a6ebf3e9d((ISourceLocation) $P0, (IList) $P1); return; } catch (FailReturnFromVoidException e){};
       
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0, $P1));
    }
    public IValue printExp(IValue $P0){ // Generated by Resolver
       IValue $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type,$T6)){
         $result = (IValue)IO_printExp$aa807cbdeda63b88((IValue) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public IValue printExp(IValue $P0, IValue $P1){ // Generated by Resolver
       IValue $result = null;
       Type $P0Type = $P0.getType();
       Type $P1Type = $P1.getType();
       if($isSubtypeOf($P0Type,$T3) && $isSubtypeOf($P1Type,$T6)){
         $result = (IValue)IO_printExp$948ec89b717e30e4((IString) $P0, (IValue) $P1);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0, $P1));
    }
    public void appendToFile(IValue $P0, IValue $P1, java.util.Map<java.lang.String,IValue> $kwpActuals){ // Generated by Resolver
       Type $P0Type = $P0.getType();
       Type $P1Type = $P1.getType();
       if($isSubtypeOf($P0Type,$T0) && $isSubtypeOf($P1Type,$T5)){
         try { IO_appendToFile$b2c3019bb1d0700f((ISourceLocation) $P0, (IList) $P1, $kwpActuals); return; } catch (FailReturnFromVoidException e){};
       
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0, $P1));
    }
    public void copyFile(IValue $P0, IValue $P1){ // Generated by Resolver
       Type $P0Type = $P0.getType();
       Type $P1Type = $P1.getType();
       if($isSubtypeOf($P0Type,$T0) && $isSubtypeOf($P1Type,$T0)){
         try { IO_copyFile$bc8c4d06598998e1((ISourceLocation) $P0, (ISourceLocation) $P1); return; } catch (FailReturnFromVoidException e){};
       
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0, $P1));
    }
    public void move(IValue $P0, IValue $P1, java.util.Map<java.lang.String,IValue> $kwpActuals){ // Generated by Resolver
       Type $P0Type = $P0.getType();
       Type $P1Type = $P1.getType();
       if($isSubtypeOf($P0Type,$T0) && $isSubtypeOf($P1Type,$T0)){
         try { IO_move$7b018fae0f5aced6((ISourceLocation) $P0, (ISourceLocation) $P1, $kwpActuals); return; } catch (FailReturnFromVoidException e){};
       
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0, $P1));
    }
    public IString readFile(IValue $P0, java.util.Map<java.lang.String,IValue> $kwpActuals){ // Generated by Resolver
       IString $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type,$T0)){
         $result = (IString)IO_readFile$b19e69121dd2f077((ISourceLocation) $P0, $kwpActuals);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public IList listEntries(IValue $P0){ // Generated by Resolver
       IList $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type,$T0)){
         $result = (IList)IO_listEntries$2acbb365e81a5100((ISourceLocation) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public void copy(IValue $P0, IValue $P1, java.util.Map<java.lang.String,IValue> $kwpActuals){ // Generated by Resolver
       Type $P0Type = $P0.getType();
       Type $P1Type = $P1.getType();
       if($isSubtypeOf($P0Type,$T0) && $isSubtypeOf($P1Type,$T0)){
         try { IO_copy$2b7816b64efe7fa8((ISourceLocation) $P0, (ISourceLocation) $P1, $kwpActuals); return; } catch (FailReturnFromVoidException e){};
       
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0, $P1));
    }
    public IString uuencode(IValue $P0){ // Generated by Resolver
       IString $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type,$T0)){
         $result = (IString)IO_uuencode$8a10b8c9efe38cc0((ISourceLocation) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public void unwatch(IValue $P0, IValue $P1, IValue $P2){ // Generated by Resolver
       Type $P0Type = $P0.getType();
       Type $P1Type = $P1.getType();
       Type $P2Type = $P2.getType();
       if($isSubtypeOf($P0Type,$T0) && $isSubtypeOf($P1Type,$T7) && $isSubtypeOf($P2Type,$T8)){
         try { IO_unwatch$29fe25f5d595f14f((ISourceLocation) $P0, (IBool) $P1, (TypedFunctionInstance1<IValue, IValue>) $P2); return; } catch (FailReturnFromVoidException e){};
       
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0, $P1, $P2));
    }
    public IString createLink(IValue $P0, IValue $P1){ // Generated by Resolver
       IString $result = null;
       Type $P0Type = $P0.getType();
       Type $P1Type = $P1.getType();
       if($isSubtypeOf($P0Type,$T3) && $isSubtypeOf($P1Type,$T3)){
         $result = (IString)IO_createLink$77fce1eedcd41a29((IString) $P0, (IString) $P1);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0, $P1));
    }
    public ISet capabilities(IValue $P0){ // Generated by Resolver
       ISet $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type,$T0)){
         $result = (ISet)IO_capabilities$690ea121bc6a3a4e((ISourceLocation) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public IValue iprintlnExp(IValue $P0){ // Generated by Resolver
       IValue $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type,$T6)){
         $result = (IValue)IO_iprintlnExp$39578a072d3af0a3((IValue) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public void copyDirectory(IValue $P0, IValue $P1){ // Generated by Resolver
       Type $P0Type = $P0.getType();
       Type $P1Type = $P1.getType();
       if($isSubtypeOf($P0Type,$T0) && $isSubtypeOf($P1Type,$T0)){
         try { IO_copyDirectory$616cbca28ab11f77((ISourceLocation) $P0, (ISourceLocation) $P1); return; } catch (FailReturnFromVoidException e){};
       
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0, $P1));
    }
    public ISourceLocation find(IValue $P0, IValue $P1){ // Generated by Resolver
       ISourceLocation $result = null;
       Type $P0Type = $P0.getType();
       Type $P1Type = $P1.getType();
       if($isSubtypeOf($P0Type,$T3) && $isSubtypeOf($P1Type,$T12)){
         $result = (ISourceLocation)IO_find$b7dd966cf6c0e7c2((IString) $P0, (IList) $P1);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0, $P1));
    }
    public IList readFileBytes(IValue $P0){ // Generated by Resolver
       IList $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type,$T0)){
         $result = (IList)IO_readFileBytes$5083dcf66416aa92((ISourceLocation) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public IValue iprintExp(IValue $P0){ // Generated by Resolver
       IValue $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type,$T6)){
         $result = (IValue)IO_iprintExp$2ee37e5de7a1d10f((IValue) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public void rprint(IValue $P0){ // Generated by Resolver
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type,$T1)){
         try { IO_rprint$a233373bf203ad10((IValue) $P0); return; } catch (FailReturnFromVoidException e){};
       
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public void writeFile(IValue $P0, IValue $P1, java.util.Map<java.lang.String,IValue> $kwpActuals){ // Generated by Resolver
       Type $P0Type = $P0.getType();
       Type $P1Type = $P1.getType();
       if($isSubtypeOf($P0Type,$T0) && $isSubtypeOf($P1Type,$T5)){
         try { IO_writeFile$e568a8263ade98e3((ISourceLocation) $P0, (IList) $P1, $kwpActuals); return; } catch (FailReturnFromVoidException e){};
       
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0, $P1));
    }
    public void iprintToFile(IValue $P0, IValue $P1, java.util.Map<java.lang.String,IValue> $kwpActuals){ // Generated by Resolver
       Type $P0Type = $P0.getType();
       Type $P1Type = $P1.getType();
       if($isSubtypeOf($P0Type,$T0) && $isSubtypeOf($P1Type,$T1)){
         try { IO_iprintToFile$2fcda4fb1d15a6b5((ISourceLocation) $P0, (IValue) $P1, $kwpActuals); return; } catch (FailReturnFromVoidException e){};
       
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0, $P1));
    }
    public void registerLocations(IValue $P0, IValue $P1, IValue $P2){ // Generated by Resolver
       Type $P0Type = $P0.getType();
       Type $P1Type = $P1.getType();
       Type $P2Type = $P2.getType();
       if($isSubtypeOf($P0Type,$T3) && $isSubtypeOf($P1Type,$T3) && $isSubtypeOf($P2Type,$T13)){
         try { IO_registerLocations$6708266b2894b2b9((IString) $P0, (IString) $P1, (IMap) $P2); return; } catch (FailReturnFromVoidException e){};
       
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0, $P1, $P2));
    }
    public IBool exists(IValue $P0){ // Generated by Resolver
       IBool $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type,$T0)){
         $result = (IBool)IO_exists$1434fcb1b8dcf974((ISourceLocation) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public void writeFileEnc(IValue $P0, IValue $P1, IValue $P2){ // Generated by Resolver
       Type $P0Type = $P0.getType();
       Type $P1Type = $P1.getType();
       Type $P2Type = $P2.getType();
       if($isSubtypeOf($P0Type,$T0) && $isSubtypeOf($P1Type,$T3) && $isSubtypeOf($P2Type,$T5)){
         try { IO_writeFileEnc$366c164ad64d9b51((ISourceLocation) $P0, (IString) $P1, (IList) $P2); return; } catch (FailReturnFromVoidException e){};
       
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0, $P1, $P2));
    }
    public void touch(IValue $P0){ // Generated by Resolver
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type,$T0)){
         try { IO_touch$ffcc8dcde35abdbc((ISourceLocation) $P0); return; } catch (FailReturnFromVoidException e){};
       
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public IDateTime lastModified(IValue $P0){ // Generated by Resolver
       IDateTime $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type,$T0)){
         $result = (IDateTime)IO_lastModified$535dddb3603abc18((ISourceLocation) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }

    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/IO.rsc|(832,1286,<25,0>,<46,89>) 
    public void IO_registerLocations$6708266b2894b2b9(IString scheme_0, IString authority_1, IMap m_2){ 
        
        
        $Prelude.registerLocations(scheme_0, authority_1, m_2); 
        return;
        
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/IO.rsc|(2120,245,<48,0>,<53,57>) 
    public void IO_unregisterLocations$8ea0884c966fa575(IString scheme_0, IString authority_1){ 
        
        
        $Prelude.unregisterLocations(scheme_0, authority_1); 
        return;
        
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/IO.rsc|(2367,74,<55,0>,<56,32>) 
    public ISourceLocation IO_resolveLocation$ad3865e0ee0205af(ISourceLocation l_0){ 
        
        
        return ((ISourceLocation)((ISourceLocation)$Prelude.resolveLocation(l_0)));
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/IO.rsc|(2443,3344,<58,0>,<100,42>) 
    public ISet IO_findResources$e54d9f39f2ac43e7(IString fileName_0){ 
        
        
        return ((ISet)((ISet)$Prelude.findResources(fileName_0)));
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/IO.rsc|(5788,91,<101,0>,<101,91>) 
    public ISet IO_findResources$dc47b57efa67f3b1(ISourceLocation path_0){ 
        
        
        if((((IBool)($equal(((IString)(((IString)($aloc_get_field(((ISourceLocation)path_0), "scheme"))))), ((IString)$constants.get(0)/*"relative"*/))))).getValue()){
           return ((ISet)($me.findResources(((IString)(((IString)($aloc_get_field(((ISourceLocation)path_0), "path"))))))));
        
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/IO.rsc|(5881,1158,<103,0>,<131,1>) 
    public ISourceLocation IO_getResource$a62508a79e67475b(IString fileName_0){ 
        
        
        ISet result_1 = ((ISet)($me.findResources(((IString)fileName_0))));
        final ISet $switchVal0 = ((ISet)result_1);
        boolean noCaseMatched_$switchVal0 = true;
        SWITCH0: switch(Fingerprint.getFingerprint($switchVal0)){
        
            case 113762:
                if(noCaseMatched_$switchVal0){
                    noCaseMatched_$switchVal0 = false;
                    if($isSubtypeOf($switchVal0.getType(),$T17)){
                       /*muExists*/CASE_113762_0: 
                           do {
                               if($switchVal0.equals(((ISet)$constants.get(1)/*{}*/))){
                                  final Template $template2 = (Template)new Template($RVF, "");
                                  $template2.addStr(((IString)fileName_0).getValue());
                                  $template2.addStr(" not found");
                                  throw new Throw($RVF.constructor(M_Exception.RuntimeException_IO_str, new IValue[]{((IString)($template2.close()))}));
                               }
                       
                           } while(false);
                    
                    }
                    if($isSubtypeOf($switchVal0.getType(),$T18)){
                       /*muExists*/CASE_113762_1: 
                           do {
                               ISet $subject3 = (ISet)($switchVal0);
                               if(((ISet)($subject3)).size() >= 1){
                                  CASE_113762_1_SET_VARsingleton:
                                  for(IValue $elem6_for : ((ISet)($subject3))){
                                      ISourceLocation $elem6 = (ISourceLocation) $elem6_for;
                                      ISourceLocation singleton_2 = ((ISourceLocation)($elem6));
                                      final ISet $subject5 = ((ISet)(((ISet)($subject3)).delete(((ISourceLocation)singleton_2))));
                                      if(((ISet)($subject5)).size() == 0){
                                         return ((ISourceLocation)singleton_2);
                                      
                                      } else {
                                         continue CASE_113762_1_SET_VARsingleton;/*set pat3*/
                                      }
                                  }
                                  
                                              
                               }
                       
                           } while(false);
                    
                    }
        
                }
                
        
            default: final Template $template1 = (Template)new Template($RVF, "");
                     $template1.addStr(((IString)fileName_0).getValue());
                     $template1.addStr(" found more than once: ");
                     $template1.beginIndent("                       ");
                     $template1.addVal(result_1);
                     $template1.endIndent("                       ");
                      
                     throw new Throw($RVF.constructor(M_Exception.RuntimeException_IO_str, new IValue[]{((IString)($template1.close()))}));
        }
        
                   
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/IO.rsc|(7042,868,<133,0>,<150,24>) 
    public void IO_appendToFile$b2c3019bb1d0700f(ISourceLocation file_0, IList V_1, java.util.Map<java.lang.String,IValue> $kwpActuals){ 
        
        java.util.Map<java.lang.String,IValue> $kwpDefaults = Util.kwpMap();
        IString $kwpDefault_charset = ((IString)DEFAULT_CHARSET);
        $kwpDefaults.put("charset", $kwpDefault_charset);IBool $kwpDefault_inferCharset = $RVF.bool(!$kwpActuals.containsKey("charset"));
        $kwpDefaults.put("inferCharset", $kwpDefault_inferCharset);
        $Prelude.appendToFile(file_0, V_1, (IString)($kwpActuals.containsKey("charset") ? $kwpActuals.get("charset") : $kwpDefaults.get("charset")), (IBool)($kwpActuals.containsKey("inferCharset") ? $kwpActuals.get("inferCharset") : $kwpDefaults.get("inferCharset"))); 
        return;
        
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/IO.rsc|(7912,682,<152,0>,<167,63>) 
    public void IO_appendToFileEnc$b0af0d043e746edc(ISourceLocation file_0, IString charset_1, IList V_2){ 
        
        
        $me.appendToFile(((ISourceLocation)file_0), V_2, Util.kwpMap("charset", charset_1, "inferCharset", ((IBool)$constants.get(2)/*false*/))); 
        return;
        
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/IO.rsc|(8596,123,<169,0>,<171,32>) 
    public ISet IO_charsets$5741e8924db30a5d(){ 
        
        
        return ((ISet)((ISet)$Prelude.charsets()));
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/IO.rsc|(8721,176,<173,0>,<175,44>) 
    public ISet IO_canEncode$8f2f7cac6b8255e8(IString charset_0){ 
        
        
        return ((ISet)((ISet)$Prelude.canEncode(charset_0)));
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/IO.rsc|(8899,418,<177,0>,<192,1>) 
    public IBool IO_bprintln$3dd978ad22652384(IValue arg_0){ 
        
        
        $me.println(((IValue)arg_0));
        return ((IBool)$constants.get(3)/*true*/);
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/IO.rsc|(9319,372,<194,0>,<209,34>) 
    public IBool IO_exists$1434fcb1b8dcf974(ISourceLocation file_0){ 
        
        
        return ((IBool)((IBool)$Prelude.exists(file_0)));
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/IO.rsc|(9694,396,<212,0>,<227,1>) 
    public ISourceLocation IO_find$b7dd966cf6c0e7c2(IString name_0, IList path_1){ 
        
        
        /*muExists*/IF1: 
            do {
                IF1_GEN9967:
                for(IValue $elem9_for : ((IList)path_1)){
                    ISourceLocation $elem9 = (ISourceLocation) $elem9_for;
                    ISourceLocation dir_2 = ((ISourceLocation)($elem9));
                    final Template $template7 = (Template)new Template($RVF, "/");
                    $template7.beginIndent(" ");
                    $template7.addStr(((IString)name_0).getValue());
                    $template7.endIndent(" ");
                    final ISourceLocation $subject_val8 = ((ISourceLocation)($aloc_add_astr(((ISourceLocation)dir_2),((IString)($template7.close())))));
                    ISourceLocation f_3 = ((ISourceLocation)($subject_val8));
                    if((((IBool)($me.exists(((ISourceLocation)f_3))))).getValue()){
                       return ((ISourceLocation)f_3);
                    
                    } else {
                       continue IF1_GEN9967;
                    }
                }
                
                            
            } while(false);
        final ISetWriter $setwriter10 = (ISetWriter)$RVF.setWriter();
        ;
        $SCOMP11_GEN10074:
        for(IValue $elem13_for : ((IList)path_1)){
            ISourceLocation $elem13 = (ISourceLocation) $elem13_for;
            ISourceLocation dir_4 = null;
            final Template $template12 = (Template)new Template($RVF, "/");
            $template12.beginIndent(" ");
            $template12.addStr(((IString)name_0).getValue());
            $template12.endIndent(" ");
            $setwriter10.insert($aloc_add_astr(((ISourceLocation)($elem13)),((IString)($template12.close()))));
        
        }
        
                    throw new Throw($RVF.constructor(M_Exception.RuntimeException_PathNotFound_set_loc, new IValue[]{((ISet)($setwriter10.done()))}));
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/IO.rsc|(10092,205,<229,0>,<234,39>) 
    public IBool IO_isDirectory$f70ff48d80811e3c(ISourceLocation file_0){ 
        
        
        return ((IBool)((IBool)$Prelude.isDirectory(file_0)));
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/IO.rsc|(10299,464,<236,0>,<249,57>) 
    public void IO_iprint$8519f29f09d8b95d(IValue arg_0, java.util.Map<java.lang.String,IValue> $kwpActuals){ 
        
        java.util.Map<java.lang.String,IValue> $kwpDefaults = $kwpDefaults_IO_iprint$8519f29f09d8b95d;
    
        $Prelude.iprint(arg_0, (IInteger)($kwpActuals.containsKey("lineLimit") ? $kwpActuals.get("lineLimit") : $kwpDefaults.get("lineLimit"))); 
        return;
        
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/IO.rsc|(10766,557,<251,0>,<264,80>) 
    public void IO_iprintToFile$2fcda4fb1d15a6b5(ISourceLocation file_0, IValue arg_1, java.util.Map<java.lang.String,IValue> $kwpActuals){ 
        
        java.util.Map<java.lang.String,IValue> $kwpDefaults = Util.kwpMap();
        IString $kwpDefault_charset = ((IString)DEFAULT_CHARSET);
        $kwpDefaults.put("charset", $kwpDefault_charset);
        $Prelude.iprintToFile(file_0, arg_1, (IString)($kwpActuals.containsKey("charset") ? $kwpActuals.get("charset") : $kwpDefaults.get("charset"))); 
        return;
        
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/IO.rsc|(11326,84,<266,0>,<267,42>) 
    public IString IO_iprintToString$c747648c89d779e2(IValue arg_0){ 
        
        
        return ((IString)((IString)$Prelude.iprintToString(arg_0)));
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/IO.rsc|(11413,324,<270,0>,<283,1>) 
    public IValue IO_iprintExp$2ee37e5de7a1d10f(IValue v_0){ 
        
        
        HashMap<io.usethesource.vallang.type.Type,io.usethesource.vallang.type.Type> $typeBindings = new HashMap<>();
        if($T6.match(v_0.getType(), $typeBindings)){
           $me.iprint(((IValue)v_0), Util.kwpMap());
           final IValue $result14 = ((IValue)v_0);
           if($T20.instantiate($typeBindings) != $TF.voidType() && $isSubtypeOf($result14.getType(),$T20)){
              return ((IValue)($result14));
           
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/IO.rsc|(11740,358,<286,0>,<299,1>) 
    public IValue IO_iprintlnExp$39578a072d3af0a3(IValue v_0){ 
        
        
        HashMap<io.usethesource.vallang.type.Type,io.usethesource.vallang.type.Type> $typeBindings = new HashMap<>();
        if($T6.match(v_0.getType(), $typeBindings)){
           $me.iprintln(((IValue)v_0), Util.kwpMap());
           final IValue $result15 = ((IValue)v_0);
           if($T20.instantiate($typeBindings) != $TF.voidType() && $isSubtypeOf($result15.getType(),$T20)){
              return ((IValue)($result15));
           
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/IO.rsc|(12102,675,<303,0>,<319,59>) 
    public void IO_iprintln$e630d4c5c6614fca(IValue arg_0, java.util.Map<java.lang.String,IValue> $kwpActuals){ 
        
        java.util.Map<java.lang.String,IValue> $kwpDefaults = $kwpDefaults_IO_iprintln$e630d4c5c6614fca;
    
        $Prelude.iprintln(arg_0, (IInteger)($kwpActuals.containsKey("lineLimit") ? $kwpActuals.get("lineLimit") : $kwpDefaults.get("lineLimit"))); 
        return;
        
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/IO.rsc|(12781,226,<322,0>,<327,34>) 
    public IBool IO_isFile$f74fdf3db6ed7ce0(ISourceLocation file_0){ 
        
        
        return ((IBool)((IBool)$Prelude.isFile(file_0)));
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/IO.rsc|(13011,387,<331,0>,<345,44>) 
    public IDateTime IO_lastModified$535dddb3603abc18(ISourceLocation file_0){ 
        
        
        return ((IDateTime)((IDateTime)$Prelude.lastModified(file_0)));
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/IO.rsc|(13401,367,<348,0>,<362,39>) 
    public IDateTime IO_created$b9550524d3128d61(ISourceLocation file_0){ 
        
        
        return ((IDateTime)((IDateTime)$Prelude.created(file_0)));
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/IO.rsc|(13772,167,<366,0>,<368,26>) 
    public void IO_touch$ffcc8dcde35abdbc(ISourceLocation file_0){ 
        
        
        $Prelude.touch(file_0); 
        return;
        
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/IO.rsc|(13942,162,<371,0>,<373,56>) 
    public void IO_setLastModified$77ace42c2a009ccb(ISourceLocation file_0, IDateTime timestamp_1){ 
        
        
        $Prelude.setLastModified(file_0, timestamp_1); 
        return;
        
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/IO.rsc|(14107,323,<376,0>,<390,44>) 
    public IList IO_listEntries$2acbb365e81a5100(ISourceLocation file_0){ 
        
        
        return ((IList)((IList)$Prelude.listEntries(file_0)));
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/IO.rsc|(14434,195,<394,0>,<400,24>) 
    public void IO_mkDirectory$a327c555c346e973(ISourceLocation file_0){ 
        
        
        $Prelude.mkDirectory(file_0); 
        return;
        
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/IO.rsc|(14632,602,<403,0>,<421,34>) 
    public void IO_print$92b0eb45ba8d0cab(IValue arg_0){ 
        
        
        $Prelude.print(arg_0); 
        return;
        
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/IO.rsc|(15237,217,<424,0>,<435,1>) 
    public IValue IO_printExp$aa807cbdeda63b88(IValue v_0){ 
        
        
        HashMap<io.usethesource.vallang.type.Type,io.usethesource.vallang.type.Type> $typeBindings = new HashMap<>();
        if($T6.match(v_0.getType(), $typeBindings)){
           final Template $template16 = (Template)new Template($RVF, "");
           $template16.addVal(v_0);
           $me.print(((IValue)($template16.close())));
           final IValue $result17 = ((IValue)v_0);
           if($T20.instantiate($typeBindings) != $TF.voidType() && $isSubtypeOf($result17.getType(),$T20)){
              return ((IValue)($result17));
           
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/IO.rsc|(15456,68,<437,0>,<440,1>) 
    public IValue IO_printExp$948ec89b717e30e4(IString msg_0, IValue v_1){ 
        
        
        HashMap<io.usethesource.vallang.type.Type,io.usethesource.vallang.type.Type> $typeBindings = new HashMap<>();
        if($T6.match(v_1.getType(), $typeBindings)){
           final Template $template18 = (Template)new Template($RVF, "");
           $template18.addStr(((IString)msg_0).getValue());
           ;$template18.addVal(v_1);
           $me.print(((IValue)($template18.close())));
           final IValue $result19 = ((IValue)v_1);
           if($T20.instantiate($typeBindings) != $TF.voidType() && $isSubtypeOf($result19.getType(),$T20)){
              return ((IValue)($result19));
           
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/IO.rsc|(15527,766,<443,0>,<474,36>) 
    public void IO_println$940af00050348a50(IValue arg_0){ 
        
        
        $Prelude.println(arg_0); 
        return;
        
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/IO.rsc|(16295,69,<476,0>,<477,27>) 
    public void IO_println$65b99fec44a8d78d(){ 
        
        
        $Prelude.println(); 
        return;
        
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/IO.rsc|(16367,383,<480,0>,<493,1>) 
    public IValue IO_printlnExp$b49750d88cd18560(IValue v_0){ 
        
        
        HashMap<io.usethesource.vallang.type.Type,io.usethesource.vallang.type.Type> $typeBindings = new HashMap<>();
        if($T6.match(v_0.getType(), $typeBindings)){
           final Template $template20 = (Template)new Template($RVF, "");
           $template20.addVal(v_0);
           $me.println(((IValue)($template20.close())));
           final IValue $result21 = ((IValue)v_0);
           if($T20.instantiate($typeBindings) != $TF.voidType() && $isSubtypeOf($result21.getType(),$T20)){
              return ((IValue)($result21));
           
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/IO.rsc|(16752,72,<495,0>,<498,1>) 
    public IValue IO_printlnExp$a88ede004ad859eb(IString msg_0, IValue v_1){ 
        
        
        HashMap<io.usethesource.vallang.type.Type,io.usethesource.vallang.type.Type> $typeBindings = new HashMap<>();
        if($T6.match(v_1.getType(), $typeBindings)){
           final Template $template22 = (Template)new Template($RVF, "");
           $template22.addStr(((IString)msg_0).getValue());
           ;$template22.addVal(v_1);
           $me.println(((IValue)($template22.close())));
           final IValue $result23 = ((IValue)v_1);
           if($T20.instantiate($typeBindings) != $TF.voidType() && $isSubtypeOf($result23.getType(),$T20)){
              return ((IValue)($result23));
           
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/IO.rsc|(16827,221,<501,0>,<509,35>) 
    public void IO_rprint$a233373bf203ad10(IValue arg_0){ 
        
        
        $Prelude.rprint(arg_0); 
        return;
        
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/IO.rsc|(17056,243,<513,0>,<521,37>) 
    public void IO_rprintln$8b703d3f54a01b68(IValue arg_0){ 
        
        
        $Prelude.rprintln(arg_0); 
        return;
        
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/IO.rsc|(17302,1660,<524,0>,<553,24>) 
    public IString IO_readFile$b19e69121dd2f077(ISourceLocation file_0, java.util.Map<java.lang.String,IValue> $kwpActuals){ 
        
        java.util.Map<java.lang.String,IValue> $kwpDefaults = Util.kwpMap();
        IString $kwpDefault_charset = ((IString)DEFAULT_CHARSET);
        $kwpDefaults.put("charset", $kwpDefault_charset);IBool $kwpDefault_inferCharset = $RVF.bool(!$kwpActuals.containsKey("charset"));
        $kwpDefaults.put("inferCharset", $kwpDefault_inferCharset);
        return ((IString)((IString)$Prelude.readFile(file_0, (IString)($kwpActuals.containsKey("charset") ? $kwpActuals.get("charset") : $kwpDefaults.get("charset")), (IBool)($kwpActuals.containsKey("inferCharset") ? $kwpActuals.get("inferCharset") : $kwpDefaults.get("inferCharset")))));
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/IO.rsc|(18964,439,<555,0>,<564,56>) 
    public IString IO_readFileEnc$28e8b07737a85a02(ISourceLocation file_0, IString charset_1){ 
        
        
        return ((IString)($me.readFile(((ISourceLocation)file_0), Util.kwpMap("inferCharset", ((IBool)$constants.get(2)/*false*/), "charset", charset_1))));
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/IO.rsc|(19405,227,<566,0>,<571,24>) 
    public IString IO_readBase64$e403bc4c7f63d90b(ISourceLocation file_0, java.util.Map<java.lang.String,IValue> $kwpActuals){ 
        
        java.util.Map<java.lang.String,IValue> $kwpDefaults = $kwpDefaults_IO_readBase64$e403bc4c7f63d90b;
    
        return ((IString)((IString)$Prelude.readBase64(file_0, (IBool)($kwpActuals.containsKey("includePadding") ? $kwpActuals.get("includePadding") : $kwpDefaults.get("includePadding")))));
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/IO.rsc|(19634,88,<573,0>,<576,49>) 
    public IString IO_uuencode$8a10b8c9efe38cc0(ISourceLocation file_0){ 
        
        
        return ((IString)($me.readBase64(((ISourceLocation)file_0), Util.kwpMap())));
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/IO.rsc|(19724,219,<578,0>,<583,24>) 
    public void IO_writeBase64$9e98f0af060196b2(ISourceLocation file_0, IString content_1){ 
        
        
        $Prelude.writeBase64(file_0, content_1); 
        return;
        
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/IO.rsc|(19945,113,<585,0>,<588,73>) 
    public void IO_uudecode$7c69e2e7099865ab(ISourceLocation file_0, IString content_1){ 
        
        
        $me.writeBase64(((ISourceLocation)file_0), ((IString)content_1)); 
        return;
        
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/IO.rsc|(20060,227,<590,0>,<595,24>) 
    public IString IO_readBase32$c3263d5cb496b291(ISourceLocation file_0, java.util.Map<java.lang.String,IValue> $kwpActuals){ 
        
        java.util.Map<java.lang.String,IValue> $kwpDefaults = $kwpDefaults_IO_readBase32$c3263d5cb496b291;
    
        return ((IString)((IString)$Prelude.readBase32(file_0, (IBool)($kwpActuals.containsKey("includePadding") ? $kwpActuals.get("includePadding") : $kwpDefaults.get("includePadding")))));
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/IO.rsc|(20289,219,<597,0>,<602,24>) 
    public void IO_writeBase32$e46c26e9b423a0ef(ISourceLocation file_0, IString content_1){ 
        
        
        $Prelude.writeBase32(file_0, content_1); 
        return;
        
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/IO.rsc|(20510,185,<604,0>,<607,24>) 
    public IList IO_readFileBytes$5083dcf66416aa92(ISourceLocation file_0){ 
        
        
        return ((IList)((IList)$Prelude.readFileBytes(file_0)));
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/IO.rsc|(20699,781,<611,0>,<626,24>) 
    public IList IO_readFileLines$4b1aaff71652c293(ISourceLocation file_0, java.util.Map<java.lang.String,IValue> $kwpActuals){ 
        
        java.util.Map<java.lang.String,IValue> $kwpDefaults = Util.kwpMap();
        IString $kwpDefault_charset = ((IString)DEFAULT_CHARSET);
        $kwpDefaults.put("charset", $kwpDefault_charset);
        return ((IList)((IList)$Prelude.readFileLines(file_0, (IString)($kwpActuals.containsKey("charset") ? $kwpActuals.get("charset") : $kwpDefaults.get("charset")))));
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/IO.rsc|(21482,487,<628,0>,<639,1>) 
    public void IO_writeFileLines$c9280a460e0da623(ISourceLocation file_0, IList lines_1, java.util.Map<java.lang.String,IValue> $kwpActuals){ 
        
        java.util.Map<java.lang.String,IValue> $kwpDefaults = Util.kwpMap();
        IString $kwpDefault_charset = ((IString)DEFAULT_CHARSET);
        $kwpDefaults.put("charset", $kwpDefault_charset);
        final Template $template24 = (Template)new Template($RVF, "");
        /*muExists*/LAB2: 
            do {
                LAB2_GEN21879:
                for(IValue $elem25_for : ((IList)lines_1)){
                    IString $elem25 = (IString) $elem25_for;
                    IString line_3 = null;
                    ;$template24.addStr(((IString)($elem25)).getValue());
                    $template24.addStr("\n");
                
                }
                continue LAB2;
                            
            } while(false);
        $me.writeFile(((ISourceLocation)file_0), $RVF.list($template24.close()), Util.kwpMapExtend(Util.kwpMapRemoveRedeclared($kwpActuals, "charset"), "charset", ((IString) ($kwpActuals.containsKey("charset") ? $kwpActuals.get("charset") : $kwpDefaults.get("charset")))));
        return;
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/IO.rsc|(21971,427,<641,0>,<651,41>) 
    public IList IO_readFileLinesEnc$3684048af0816067(ISourceLocation file_0, IString charset_1){ 
        
        
        return ((IList)($me.readFileLines(((ISourceLocation)file_0), Util.kwpMap("charset", charset_1))));
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/IO.rsc|(22401,107,<654,0>,<655,65>) 
    public void IO_remove$7ee95f40dde4956c(ISourceLocation file_0, java.util.Map<java.lang.String,IValue> $kwpActuals){ 
        
        java.util.Map<java.lang.String,IValue> $kwpDefaults = $kwpDefaults_IO_remove$7ee95f40dde4956c;
    
        $Prelude.remove(file_0, (IBool)($kwpActuals.containsKey("recursive") ? $kwpActuals.get("recursive") : $kwpDefaults.get("recursive"))); 
        return;
        
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/IO.rsc|(22511,600,<658,0>,<671,24>) 
    public void IO_writeFile$e568a8263ade98e3(ISourceLocation file_0, IList V_1, java.util.Map<java.lang.String,IValue> $kwpActuals){ 
        
        java.util.Map<java.lang.String,IValue> $kwpDefaults = Util.kwpMap();
        IString $kwpDefault_charset = ((IString)DEFAULT_CHARSET);
        $kwpDefaults.put("charset", $kwpDefault_charset);
        $Prelude.writeFile(file_0, V_1, (IString)($kwpActuals.containsKey("charset") ? $kwpActuals.get("charset") : $kwpDefaults.get("charset"))); 
        return;
        
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/IO.rsc|(23113,169,<673,0>,<676,24>) 
    public void IO_writeFileBytes$0c8db72a6ebf3e9d(ISourceLocation file_0, IList bytes_1){ 
        
        
        $Prelude.writeFileBytes(file_0, bytes_1); 
        return;
        
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/IO.rsc|(23285,606,<679,0>,<694,40>) 
    public void IO_writeFileEnc$366c164ad64d9b51(ISourceLocation file_0, IString charset_1, IList V_2){ 
        
        
        $me.writeFile(((ISourceLocation)file_0), V_2, Util.kwpMap("charset", charset_1)); 
        return;
        
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/IO.rsc|(23894,231,<697,0>,<704,24>) 
    public IString IO_md5HashFile$4f8764080ca4ef11(ISourceLocation file_0){ 
        
        
        return ((IString)((IString)$Prelude.md5HashFile(file_0)));
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/IO.rsc|(24127,75,<706,0>,<707,33>) 
    public IString IO_md5Hash$8849b64c711c3dc4(IValue v_0){ 
        
        
        return ((IString)((IString)$Prelude.md5Hash(v_0)));
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/IO.rsc|(24204,92,<709,0>,<710,50>) 
    public IString IO_createLink$77fce1eedcd41a29(IString title_0, IString target_1){ 
        
        
        return ((IString)((IString)$Prelude.createLink(title_0, target_1)));
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/IO.rsc|(24299,168,<713,0>,<718,24>) 
    public IString IO_toBase64$444f08dbde4c25dd(ISourceLocation file_0, java.util.Map<java.lang.String,IValue> $kwpActuals){ 
        
        java.util.Map<java.lang.String,IValue> $kwpDefaults = $kwpDefaults_IO_toBase64$444f08dbde4c25dd;
    
        return ((IString)((IString)$Prelude.toBase64(file_0, (IBool)($kwpActuals.containsKey("includePadding") ? $kwpActuals.get("includePadding") : $kwpDefaults.get("includePadding")))));
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/IO.rsc|(24469,134,<720,0>,<721,92>) 
    public void IO_copy$2b7816b64efe7fa8(ISourceLocation source_0, ISourceLocation target_1, java.util.Map<java.lang.String,IValue> $kwpActuals){ 
        
        java.util.Map<java.lang.String,IValue> $kwpDefaults = $kwpDefaults_IO_copy$2b7816b64efe7fa8;
    
        $Prelude.copy(source_0, target_1, (IBool)($kwpActuals.containsKey("recursive") ? $kwpActuals.get("recursive") : $kwpDefaults.get("recursive")), (IBool)($kwpActuals.containsKey("overwrite") ? $kwpActuals.get("overwrite") : $kwpDefaults.get("overwrite"))); 
        return;
        
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/IO.rsc|(24605,145,<723,0>,<728,1>) 
    public void IO_copyFile$bc8c4d06598998e1(ISourceLocation source_0, ISourceLocation target_1){ 
        
        
        $me.copy(((ISourceLocation)source_0), ((ISourceLocation)target_1), Util.kwpMap("recursive", ((IBool)$constants.get(2)/*false*/), "overwrite", ((IBool)$constants.get(3)/*true*/)));
        return;
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/IO.rsc|(24752,149,<730,0>,<735,1>) 
    public void IO_copyDirectory$616cbca28ab11f77(ISourceLocation source_0, ISourceLocation target_1){ 
        
        
        $me.copy(((ISourceLocation)source_0), ((ISourceLocation)target_1), Util.kwpMap("recursive", ((IBool)$constants.get(3)/*true*/), "overwrite", ((IBool)$constants.get(3)/*true*/)));
        return;
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/IO.rsc|(24903,112,<737,0>,<738,70>) 
    public void IO_move$7b018fae0f5aced6(ISourceLocation source_0, ISourceLocation target_1, java.util.Map<java.lang.String,IValue> $kwpActuals){ 
        
        java.util.Map<java.lang.String,IValue> $kwpDefaults = $kwpDefaults_IO_move$7b018fae0f5aced6;
    
        $Prelude.move(source_0, target_1, (IBool)($kwpActuals.containsKey("overwrite") ? $kwpActuals.get("overwrite") : $kwpDefaults.get("overwrite"))); 
        return;
        
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/IO.rsc|(25017,60,<740,0>,<741,18>) 
    public ISourceLocation IO_arbLoc$7d94a673b878c7c6(){ 
        
        
        return ((ISourceLocation)((ISourceLocation)$Prelude.arbLoc()));
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/IO.rsc|(25313,125,<755,0>,<756,83>) 
    public void IO_watch$109532ab0f0f0dda(ISourceLocation src_0, IBool recursive_1, TypedFunctionInstance1<IValue, IValue> watcher_2){ 
        
        
        $Prelude.watch(src_0, recursive_1, watcher_2); 
        return;
        
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/IO.rsc|(25440,127,<758,0>,<759,85>) 
    public void IO_unwatch$29fe25f5d595f14f(ISourceLocation src_0, IBool recursive_1, TypedFunctionInstance1<IValue, IValue> watcher_2){ 
        
        
        $Prelude.unwatch(src_0, recursive_1, watcher_2); 
        return;
        
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/IO.rsc|(26991,150,<796,0>,<798,50>) 
    public ISet IO_capabilities$690ea121bc6a3a4e(ISourceLocation location_0){ 
        
        
        return ((ISet)((ISet)$Prelude.capabilities(location_0)));
    
    }
    

    public static void main(String[] args) {
      throw new RuntimeException("No function `main` found in Rascal module `IO`");
    }
}