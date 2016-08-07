package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.serialize;

/**
 * Constants for Rascal Serialization Format (RSF),
 * a binary format for the (de)serialization of Rascal values and types
 */
public class RSF {
    
    /********************************/
    /*  Values:                     */
    /*  - Message id per value      */
    /*  - Fields per message        */
    /********************************/
    
    // Value back reference
    public static final int PREVIOUS_VALUE = 1;
    
	// Atomic values
	public static final int BOOL_VALUE = 2;
	public static final int DATETIME_VALUE = 3;
	public static final int INT_VALUE = 4;
	public static final int LOC_VALUE = 5;
	public static final int RATIONAL_VALUE = 6;
	public static final int REAL_VALUE = 7;
	public static final int STR_VALUE = 8;

	// Compound values

	public static final int CONSTRUCTOR_VALUE = 9;
	public static final int LIST_VALUE = 10;
	public static final int MAP_VALUE = 11;
	public static final int NODE_VALUE = 12;
	public static final int SET_VALUE = 13;
	public static final int TUPLE_VALUE = 14;
	
//	// Values related to compiled executables
//	public static final int RVM_FUNCTION_VALUE = 15;
//	public static final int RVM_OVERLOADED_FUNCTION_VALUE = 16;
//	public static final int RVM_CODEBLOCK_VALUE = 17;
//	public static final int RVM_EXECUTABLE_VALUE = 18;
	
	// Fields on values
	
	public static final int PREVIOUS_VALUE_ID = 1;	// Common field for all values!

	public static final int BOOL_CONTENT = 2;
	
	public static final int DATETIME_VARIANT = 2;
	public static final int DATETIME_YEAR = 3;
	public static final int DATETIME_MONTH = 4;
	public static final int DATETIME_DAY = 5;
	public static final int DATETIME_HOUR = 6;
	public static final int DATETIME_MINUTE = 7;
	public static final int DATETIME_SECOND = 8;
	public static final int DATETIME_MILLISECOND = 9;
	public static final int DATETIME_TZ_HOUR = 10;
	public static final int DATETIME_TZ_MINUTE = 11;
	
	public static final int DATETIME_VARIANT_DATETIME = 1;  // TODO Use enum
	public static final int DATETIME_VARIANT_DATE = 2;
	public static final int DATETIME_VARIANT_TIME = 3;
	
	public static final int INT_SMALL = 2;
	public static final int INT_BIG = 3;
	
	public static final int BIGINT_CONTENT = 2;
	
	public static final int LOC_PREVIOUS_URI = 2;
	public static final int LOC_OFFSET = 3;
	public static final int LOC_LENGTH = 4;
	public static final int LOC_BEGINLINE = 5;
	public static final int LOC_ENDLINE = 6;
	public static final int LOC_BEGINCOLUMN = 7;
	public static final int LOC_ENDCOLUMN = 8;
	public static final int LOC_SCHEME = 9;
	public static final int LOC_AUTHORITY = 10;
	public static final int LOC_HOST = 11;
	public static final int LOC_PATH = 12;
	public static final int LOC_QUERY = 13;
	public static final int LOC_FRAGMENT = 14;

	public static final int RAT_NUMERATOR = 2;
	public static final int RAT_DENOMINATOR = 3;
	
	public static final int REAL_CONTENT = 2;
	public static final int REAL_SCALE = 3;
	
	public static final int STR_CONTENT = 2;
	
	public static final int CONSTRUCTOR_ARITY = 2;
	public static final int CONSTRUCTOR_KWPARAMS = 3;
	public static final int CONSTRUCTOR_ANNOS = 4;
	
	public static final int LIST_SIZE = 2;
	
	public static final int MAP_SIZE = 2;
	
	public static final int NODE_NAME = 2;
	public static final int NODE_ARITY = 3;
	public static final int NODE_KWPARAMS = 4;
	public static final int NODE_ANNOS = 5;
	
	public static final int SET_SIZE = 2;
	
	public static final int TUPLE_SIZE = 2;
	
	/********************************/
    /*  Types:                      */
    /*  - Message id per type       */
    /*  - Fields per message        */
    /********************************/
   
	// TODO: Types start now at 100 to enable potential refactoring of reader code
	
	// Type back reference
    public static final int PREVIOUS_TYPE_ID = 101;
	
	// Atomic types
    public static final int BOOL_TYPE = 102;
    public static final int DATETIME_TYPE = 103;
    public static final int INT_TYPE = 104;
    public static final int LOC_TYPE = 105;
    public static final int NUMBER_TYPE = 106;
    public static final int PARAMETER_TYPE = 107;
    public static final int RATIONAL_TYPE = 108;
    public static final int REAL_TYPE = 109;
    public static final int STR_TYPE = 110;
    public static final int VALUE_TYPE = 111;
    public static final int VOID_TYPE = 112;
    
    // Compound types
    public static final int ADT_TYPE = 113;
    public static final int ALIAS_TYPE = 114;
    public static final int CONSTRUCTOR_TYPE = 115;
    public static final int FUNCTION_TYPE = 116;
    public static final int LIST_TYPE = 117;
    public static final int MAP_TYPE= 118;
    public static final int NODE_TYPE = 119;
    public static final int NONTERMINAL_TYPE = 120;
    public static final int OVERLOADED_TYPE = 121;
    public static final int REIFIED_TYPE = 122;
    public static final int SET_TYPE = 123;
    public static final int TUPLE_TYPE = 124;
    
    // Fields on types
    
    public static final int PREVIOUS_ID = 1;    // Common field for all types!
    
    public static final int ADT_NAME = 2;
    
    public static final int ALIAS_NAME = 2;
    
    public static final int CONSTRUCTOR_NAME = 2;
    
    public static final int MAP_KEY_LABEL = 2;
    public static final int MAP_VAL_LABEL = 3;
    
    public static final int OVERLOADED_SIZE = 2;
    
    public static final int PARAMETER_NAME = 2;
    
    public static final int TUPLE_ARITY = 2;
    public static final int TUPLE_NAMES = 3;

//    public static final int RVM_FUNCTION_NAME = 2;
//    public static final int RVM_FUNCTION_SCOPE_ID = 3;
//    public static final int RVM_FUNCTION_FUN_IN = 4;
//    public static final int RVM_FUNCTION_SCOPE_IN = 5;
//    public static final int RVM_FUNCTION_NFORMALS = 6;
//    public static final int RVM_FUNCTION_NLOCALS = 7;
//    public static final int RVM_FUNCTION_IS_DEFAULT = 8;
//    public static final int RVM_FUNCTION_MAX_STACK = 9;
//    public static final int RVM_FUNCTION_CONCRETE_ARG = 10;
//    public static final int RVM_FUNCTION_ABSTRACT_FINGERPRINT = 11;
//    public static final int RVM_FUNCTION_CONCRETE_FINGERPRINT = 12;
//    public static final int RVM_FUNCTION_FROMS = 13;
//    public static final int RVM_FUNCTION_TOS = 14;
//    public static final int RVM_FUNCTION_TYPES = 15;
//    public static final int RVM_FUNCTION_HANDLERS = 16;
//    public static final int RVM_FUNCTION_FROM_SPS = 17;
//    public static final int RVM_FUNCTION_LAST_HANDLER = 18;
//    public static final int RVM_FUNCTION_FUN_ID = 19;
//    public static final int RVM_FUNCTION_IS_COROUTINE = 20;
//    public static final int RVM_FUNCTION_REFS = 21;
//    public static final int RVM_FUNCTION_IS_VARARGS = 22;
//    public static final int RVM_FUNCTION_CONTINUATION_POINTS = 23;
	
}
