package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.serialize;

public class SType {
	
	// Atomic types
		protected static final byte BOOL = 1;
		protected static final byte DATETIME = 2;
		protected static final byte INT = 3;
		protected static final byte LOC = 4;
		protected static final byte NUMBER = 5;
		protected static final byte PARAMETER = 6;
		protected static final byte RAT = 7;
		protected static final byte REAL = 8;
		protected static final byte STR = 9;
		protected static final byte VALUE = 10;
		protected static final byte VOID = 11;
		
		// Compound types
		protected static final byte ADT = 12;
		protected static final byte ALIAS = 13;
		protected static final byte CONSTRUCTOR = 14;
		protected static final byte FUNCTION = 15;
		protected static final byte LIST = 16;
		protected static final byte MAP = 17;
		protected static final byte NODE = 18;
		protected static final byte NONTERMINAL = 19;
		protected static final byte OVERLOADED = 20;
		protected static final byte REIFIED = 21;
		protected static final byte SET = 22;
		protected static final byte TUPLE = 23;
		
		// Type back reference
		protected static final byte PREVIOUS = 24;
		
		// Fields on types
		
		public static final int PREVIOUS_TYPE = 1;	// Common field for all types!
		
		protected static final int ADT_NAME = 2;
		protected static final int ADT_TYPE_PARAMETERS = 3;
		
		protected static final int ALIAS_NAME = 2;
		protected static final int ALIAS_ALIASED = 3;
		protected static final int ALIAS_TYPE_PARAMETERS = 4;
		
		protected static final int CONSTRUCTOR_NAME = 2;
		protected static final int CONSTRUCTOR_ABSTRACT_DATA_TYPE = 3;
		protected static final int CONSTRUCTOR_TYPE = 4;
		
		protected static final int FUNCTION_RETURN_TYPE = 2;
		protected static final int FUNCTION_ARGUMENT_TYPES = 3;
		protected static final int FUNCTION_KEYWORD_PARAMETER_TYPES = 4;
		
		protected static final int LIST_ELEMENT_TYPE = 2;
		
		protected static final int MAP_KEY_LABEL = 2;
		protected static final int MAP_VAL_LABEL = 3;
		protected static final int MAP_KEY_TYPE = 4;
		protected static final int MAP_VAL_TYPE = 5;
		
		protected static final int REIFIED_ELEMENT_TYPE = 2;
		
		protected static final int OVERLOADED_TYPES = 2;
		
		protected static final int NONTERMINAL_CONSTRUCTOR = 2;
		

		protected static final int PARAMETER_NAME = 2;
		protected static final int PARAMETER_BOUND = 3;
		
		protected static final int SET_ELEMENT_TYPE = 2;
		
		protected static final int TUPLE_TYPES = 2;
		protected static final int TUPLE_NAMES = 3;
		
}
