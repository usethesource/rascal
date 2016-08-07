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
    protected static final int PREVIOUS_VALUE = 1;
    
	// Atomic values
	protected static final int BOOL_VALUE = 2;
	protected static final int DATETIME_VALUE = 3;
	protected static final int DATE_VALUE = 4;
	protected static final int TIME_VALUE = 5;
	protected static final int INT_VALUE = 6;
	protected static final int BIGINT_VALUE = 7;
	protected static final int LOC_VALUE = 8;
	protected static final int RATIONAL_VALUE = 9;
	protected static final int REAL_VALUE = 10;
	protected static final int STR_VALUE = 11;

	// Compound values

	protected static final int CONSTRUCTOR_VALUE = 12;
	protected static final int LIST_VALUE = 13;
	protected static final int MAP_VALUE = 14;
	protected static final int NODE_VALUE = 15;
	protected static final int SET_VALUE = 16;
	protected static final int TUPLE_VALUE = 17;
	
	// Fields on values
	
	public static final int PREVIOUS_VALUE_ID = 1;	// Common field for all values!

	protected static final int BOOL_BOOL = 2;
	
	protected static final int DATETIME_YEAR = 2;
	protected static final int DATETIME_MONTH = 3;
	protected static final int DATETIME_DAY = 4;
	protected static final int DATETIME_HOUR = 5;
	protected static final int DATETIME_MINUTE = 6;
	protected static final int DATETIME_SECOND = 7;
	protected static final int DATETIME_MILLISECOND = 8;
	protected static final int DATETIME_TZ_HOUR = 9;
	protected static final int DATETIME_TZ_MINUTE = 10;
	
	protected static final int DATE_YEAR = 2;
	protected static final int DATE_MONTH = 3;
	protected static final int DATE_DAY = 4;
	
	protected static final int TIME_HOUR = 2;
	protected static final int TIME_MINUTE = 3;
	protected static final int TIME_SECOND = 4;
	protected static final int TIME_MILLISECOND = 5;
	protected static final int TIME_TZ_HOUR = 6;
	protected static final int TIME_TZ_MINUTE = 7;
	
	protected static final int INT_INT = 2;
	
	protected static final int BIGINT_BIGINT = 2;
	
	protected static final int LOC_PREVIOUS_URI = 2;
	protected static final int LOC_OFFSET = 3;
	protected static final int LOC_LENGTH = 4;
	protected static final int LOC_BEGINLINE = 5;
	protected static final int LOC_ENDLINE = 6;
	protected static final int LOC_BEGINCOLUMN = 7;
	protected static final int LOC_ENDCOLUMN = 8;
	protected static final int LOC_SCHEME = 9;
	protected static final int LOC_AUTHORITY = 10;
	protected static final int LOC_HOST = 11;
	protected static final int LOC_PATH = 12;
	protected static final int LOC_QUERY = 13;
	protected static final int LOC_FRAGMENT = 14;

	protected static final int RAT_NUMERATOR = 2;
	protected static final int RAT_DENOMINATOR = 3;
	
	public static final int REAL_REAL = 2;
	public static final int REAL_SCALE = 3;
	
	protected static final int STR_STR = 2;
	
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
    protected static final int PREVIOUS_TYPE_ID = 101;
	
	// Atomic types
    protected static final int BOOL_TYPE = 102;
    protected static final int DATETIME_TYPE = 103;
    protected static final int INT_TYPE = 104;
    protected static final int LOC_TYPE = 105;
    protected static final int NUMBER_TYPE = 106;
    protected static final int PARAMETER_TYPE = 107;
    protected static final int RATIONAL_TYPE = 108;
    protected static final int REAL_TYPE = 109;
    protected static final int STR_TYPE = 110;
    protected static final int VALUE_TYPE = 111;
    protected static final int VOID_TYPE = 112;
    
    // Compound types
    protected static final int ADT_TYPE = 113;
    protected static final int ALIAS_TYPE = 114;
    protected static final int CONSTRUCTOR_TYPE = 115;
    protected static final int FUNCTION_TYPE = 116;
    protected static final int LIST_TYPE = 117;
    protected static final int MAP_TYPE= 118;
    protected static final int NODE_TYPE = 119;
    protected static final int NONTERMINAL_TYPE = 120;
    protected static final int OVERLOADED_TYPE = 121;
    protected static final int REIFIED_TYPE = 122;
    protected static final int SET_TYPE = 123;
    protected static final int TUPLE_TYPE = 124;
    
    // Fields on types
    
    public static final int PREVIOUS_ID = 1;    // Common field for all types!
    
    protected static final int ADT_NAME = 2;
    
    protected static final int ALIAS_NAME = 2;
    
    protected static final int CONSTRUCTOR_NAME = 2;
    
    protected static final int MAP_KEY_LABEL = 2;
    protected static final int MAP_VAL_LABEL = 3;
    
    
    protected static final int OVERLOADED_SIZE = 2;
    
    protected static final int PARAMETER_NAME = 2;
    
    protected static final int TUPLE_ARITY = 2;
    protected static final int TUPLE_NAMES = 3;
	
}
