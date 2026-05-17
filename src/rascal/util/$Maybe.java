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
public class $Maybe 
    extends
        org.rascalmpl.runtime.$RascalModule
    implements 
    	rascal.util.$Maybe_$I {

    private final $Maybe_$I $me;
    private final IList $constants;
    
    

    
    
    public final io.usethesource.vallang.type.Type $T0;	/*avalue()*/
    public final io.usethesource.vallang.type.Type $T1;	/*aparameter("A",avalue(),closed=true)*/
    public final io.usethesource.vallang.type.Type $T2;	/*aparameter("A",avalue(),closed=false,alabel="val")*/
    public final io.usethesource.vallang.type.Type ADT_Maybe_1;	/*aadt("Maybe",[aparameter("A",avalue(),closed=true)],dataSyntax())*/
    public final io.usethesource.vallang.type.Type Maybe_1_nothing_;	/*acons(aadt("Maybe",[aparameter("A",avalue(),closed=true)],dataSyntax()),[],[],alabel="nothing")*/
    public final io.usethesource.vallang.type.Type Maybe_1_just_;	/*acons(aadt("Maybe",[aparameter("A",avalue(),closed=true)],dataSyntax()),[aparameter("A",avalue(),closed=false,alabel="val")],[],alabel="just")*/

    public $Maybe(RascalExecutionContext rex){
        this(rex, null);
    }
    
    public $Maybe(RascalExecutionContext rex, Object extended){
       super(rex);
       this.$me = extended == null ? this : ($Maybe_$I)extended;
       ModuleStore mstore = rex.getModuleStore();
       mstore.put(rascal.util.$Maybe.class, this);
        
        
       
                          
       
       
       $constants = readBinaryConstantsFile(this.getClass(), "rascal/util/$Maybe.constants", 0, "d751713988987e9331980363e24189ce");
       $T0 = $TF.valueType();
       $T1 = $TF.parameterType("A", $T0);
       $T2 = $TF.parameterType("A", $T0);
       ADT_Maybe_1 = $parameterizedAdt("Maybe", new Type[] { $T1 });
       Maybe_1_nothing_ = $TF.constructor($TS, ADT_Maybe_1, "nothing");
       Maybe_1_just_ = $TF.constructor($TS, ADT_Maybe_1, "just", $TF.parameterType("A", $T0), "val");
    
       
       
    }
    
    
    public static void main(String[] args) {
      throw new RuntimeException("No function `main` found in Rascal module `util::Maybe`");
    }
}