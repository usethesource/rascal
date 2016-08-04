package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.serialize;

public class SValue {
	// Atomic values
	protected static final byte BOOL = 1;
	protected static final byte DATETIME = 2;
	protected static final byte DATE = 3;
	protected static final byte TIME = 4;
	protected static final byte INT = 5;
	protected static final byte BIGINT = 6;
	protected static final byte LOC = 7;
	protected static final byte RAT = 8;
	protected static final byte REAL = 9;
	protected static final byte STR = 10;

	// Compound values

	protected static final byte CONSTRUCTOR = 11;
	protected static final byte LIST = 12;
	protected static final byte MAP = 13;
	protected static final byte NODE = 14;
	protected static final byte SET = 15;
	protected static final byte TUPLE = 16;

	// Value back reference
	protected static final byte PREVIOUS = 17;
	// Value end-of-value marker
	protected static final byte END_OF_VALUE = 18;
	
	// Fields on values
	
	public static final int PREVIOUS_VALUE = 1;	// Common field for all values!

	protected static final byte BOOL_VALUE = 2;
	
	protected static final byte DATETIME_YEAR = 2;
	protected static final byte DATETIME_MONTH = 3;
	protected static final byte DATETIME_DAY = 4;
	protected static final byte DATETIME_HOUR = 5;
	protected static final byte DATETIME_MINUTE = 6;
	protected static final byte DATETIME_SECOND = 7;
	protected static final byte DATETIME_MILLISECOND = 8;
	protected static final byte DATETIME_TZ_HOUR = 9;
	protected static final byte DATETIME_TZ_MINUTE = 10;
	
	protected static final byte DATE_YEAR = 2;
	protected static final byte DATE_MONTH = 3;
	protected static final byte DATE_DAY = 4;
	
	protected static final byte TIME_HOUR = 2;
	protected static final byte TIME_MINUTE = 3;
	protected static final byte TIME_SECOND = 4;
	protected static final byte TIME_MILLISECOND = 5;
	protected static final byte TIME_TZ_HOUR = 6;
	protected static final byte TIME_TZ_MINUTE = 7;
	
	protected static final byte INT_VALUE = 2;
	
	protected static final byte BIGINT_VALUE = 2;
	
	protected static final byte LOC_URI = 2;
	protected static final byte LOC_OFFSET = 3;
	protected static final byte LOC_LENGTH = 4;
	protected static final byte LOC_BEGINLINE = 5;
	protected static final byte LOC_ENDLINE = 6;
	protected static final byte LOC_BEGINCOLUMN = 7;
	protected static final byte LOC_ENDCOLUMN = 8;
	protected static final byte LOC_SCHEME = 9;
	protected static final byte LOC_AUTHORITY = 10;
	protected static final byte LOC_HOST = 11;
	protected static final byte LOC_PATH = 12;
	protected static final byte LOC_QUERY = 13;
	protected static final byte LOC_FRAGMENT = 14;
	
	protected static final byte RAT_NUMERATOR = 2;
	protected static final byte RAT_DENOMINATOR = 3;
	
	public static final int REAL_VALUE = 2;
	public static final int REAL_SCALE = 3;
	
	protected static final byte STR_VALUE = 2;
	
	public static final int CONSTRUCTOR_ARITY = 2;
	public static final int CONSTRUCTOR_KWPARAMS = 3;
	public static final int CONSTRUCTOR_ANNOS = 4;
	public static final int CONSTRUCTOR_TYPE = 5;
	
	public static final int LIST_SIZE = 2;
	
	public static final int MAP_SIZE = 2;
	
	public static final int NODE_NAME = 2;
	public static final int NODE_ARITY = 3;
	public static final int NODE_KWPARAMS = 4;
	public static final int NODE_ANNOS = 5;
	
	public static final int SET_SIZE = 2;
	
	public static final int TUPLE_SIZE = 2;
	
}
