/** 
 * Copyright (c) 2016, Davy Landman, Paul Klint, Centrum Wiskunde & Informatica (CWI) 
 * All rights reserved. 
 *  
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met: 
 *  
 * 1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer. 
 *  
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution. 
 *  
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. 
 */ 
package org.rascalmpl.value.io.binary.message;

/**
 * Constants for Rascal Serialization Format
 */
public class IValueIDs {
    
    // This is an overview of the message id's, low values are quicker and more compact to encode
    // Never change them, that would break backwards compatiblity
    
    private static final int HEADER_ID = 4242;  
    private static final int LAST_VALUE_ID = 4243; // a special marker to signal the end of the stream of values
    private static final int LAST_TYPE_ID = 4342; // a special marker to signal the end of the stream of types
    
    public static final class Header {
        public static final int ID = HEADER_ID;
        public static final int VALUE_WINDOW = 1;
        public static final int TYPE_WINDOW = 2;
        public static final int SOURCE_LOCATION_WINDOW = 3;
        public static final int VALUE = 4;
        public static final int TYPE = 5;
    }
    public static final class LastValue {
        public static final int ID = LAST_VALUE_ID;
    }
    public static final class LastType {
        public static final int ID = LAST_TYPE_ID;
    }
    
    // Atomic values
    private static final int PREVIOUS_VALUE_ID = 1;
    private static final int BOOLEAN_VALUE_ID = 2;
    private static final int INTEGER_VALUE_ID = 3;
    private static final int REAL_VALUE_ID = 4;
    private static final int SOURCE_LOCATION_VALUE_ID = 5;
    private static final int STRING_VALUE_ID = 6;

    // above 32 for less often occuring messages (they take a byte extra to encode and decode)
    private static final int DATETIME_VALUE_ID = 32; 
    private static final int RAT_VALUE_ID = 33;
    private static final int EXTERNAL_VALUE_ID = 34;
    
    // Compound values

    private static final int CONSTRUCTOR_VALUE_ID = 7;
    private static final int NODE_VALUE_ID = 8;
    private static final int TUPLE_VALUE_ID = 9;
    private static final int LIST_VALUE_ID = 10;
    private static final int MAP_VALUE_ID = 11;
    private static final int SET_VALUE_ID = 12;
    private static final int NAMED_VALUE_ID = 13;
    // WARNING: when adding here, don't forget to update the ranges at the end of this class
    
    public static class Common {
        public static final int CAN_BE_BACK_REFERENCED = 31;
    }
    
    public static class PreviousValue {
        public static final int ID = PREVIOUS_VALUE_ID;
        public static final int HOW_FAR_BACK = 1;    
    }

    public static class BoolValue {
        public static final int ID = BOOLEAN_VALUE_ID;
        public static final int VALUE = 1;
    }
    
    public static class DateTimeValue {
        public static final int ID = DATETIME_VALUE_ID;
        public static final int YEAR = 1;
        public static final int MONTH = 2;
        public static final int DAY = 3;
        public static final int HOUR = 4;
        public static final int MINUTE = 5;
        public static final int SECOND = 6;
        public static final int MILLISECOND = 7;
        public static final int TZ_HOUR = 8;
        public static final int TZ_MINUTE = 9;
    }
    
    public static class IntegerValue {
        public static final int ID = INTEGER_VALUE_ID;
        public static final int INTVALUE = 1;
        public static final int BIGVALUE = 2;
    }
    
    public static class SourceLocationValue {
        public static final int ID = SOURCE_LOCATION_VALUE_ID;
        public static final int PREVIOUS_URI = 1;
        public static final int OFFSET = 2;
        public static final int LENGTH = 3;
        public static final int BEGINLINE = 4;
        public static final int ENDLINE = 5;
        public static final int BEGINCOLUMN = 6;
        public static final int ENDCOLUMN = 7;
        public static final int SCHEME = 8;
        public static final int AUTHORITY = 9;
        public static final int PATH = 10;
        public static final int QUERY = 11;
        public static final int FRAGMENT = 12;
    }

    public static class RationalValue {
        public static final int ID = RAT_VALUE_ID;
        public static final int NUMERATOR = 1;
        public static final int DENOMINATOR = 2;
    }
    
    public static class RealValue {
        public static final int ID = REAL_VALUE_ID;
        public static final int CONTENT = 1;
        public static final int SCALE = 2;
    }
    
    public static class StringValue {
        public static final int ID = STRING_VALUE_ID;
        public static final int CONTENT = 1;
    }
    
    public static class ConstructorValue {
        public static final int ID = CONSTRUCTOR_VALUE_ID;
        public static final int ARITY = 1;
        public static final int KWPARAMS = 2;
        public static final int ANNOS = 3;
        public static final int TYPE = 4;
        public static final int PARAMS = 5;
    }

    public static class NodeValue {
        public static final int ID = NODE_VALUE_ID;
        public static final int NAME = 1;
        public static final int ARITY = 2;
        public static final int KWPARAMS = 3;
        public static final int ANNOS = 4;
        public static final int PARAMS = 5;
    }

    public static class TupleValue {
        public static final int ID = TUPLE_VALUE_ID;
        public static final int SIZE = 1;
        public static final int ELEMENTS = 2;
    }
    
    public static class ListValue {
        public static final int ID = LIST_VALUE_ID;
        public static final int SIZE = 1;
        public static final int ELEMENTS = 2;
    }
    
    public static class MapValue {
        public static final int ID = MAP_VALUE_ID;
        public static final int SIZE = 1;
        public static final int KV_PAIRS = 2;
    }

    public static class SetValue {
        public static final int ID = SET_VALUE_ID;
        public static final int SIZE = 1;
        public static final int ELEMENTS = 2;
    }
    public static class NamedValue {
        public static final int ID = NAMED_VALUE_ID;
        public static final int NAME = 1;
        public static final int VALUE = 2;
    }
    public static class ExternalValue {
        public static final int ID = EXTERNAL_VALUE_ID;
        public static final int VALUE = 1;
    }

    // Type message ID's start at 100 to leave room for new values
    // Types aren't serialized that often so the overhead is acceptable

    private static final int PREVIOUS_TYPE_ID_ID = 101;
    
    // Atomic types
    private static final int BOOL_TYPE_ID = 102;
    private static final int LOC_TYPE_ID = 103;
    private static final int DATETIME_TYPE_ID = 104;
    private static final int INT_TYPE_ID = 105;
    private static final int NUMBER_TYPE_ID = 106;
    private static final int RATIONAL_TYPE_ID = 107;
    private static final int REAL_TYPE_ID = 108;
    private static final int STR_TYPE_ID = 109;
    private static final int VALUE_TYPE_ID = 110;
    private static final int VOID_TYPE_ID = 111;
    
    // Compound types
    private static final int NODE_TYPE_ID = 112;
    private static final int ADT_TYPE_ID = 113;
    private static final int CONSTRUCTOR_TYPE_ID = 114;
    private static final int EXTERNAL_TYPE_ID = 115;
    private static final int TUPLE_TYPE_ID = 116;
    private static final int LIST_TYPE_ID = 117;
    private static final int MAP_TYPE_ID= 118;
    private static final int SET_TYPE_ID = 119;
    private static final int PARAMETER_TYPE_ID = 120;
    private static final int ALIAS_TYPE_ID = 121;
    private static final int FUNCTION_TYPE_ID = 122;
    
    public static class PreviousType {
        public static final int ID = PREVIOUS_TYPE_ID_ID;
        public static final int HOW_LONG_AGO = 1;
    }

    public static class BoolType {
        public static final int ID = BOOL_TYPE_ID;
    }
    public static class DateTimeType {
        public static final int ID = DATETIME_TYPE_ID;
    }
    public static class SourceLocationType {
        public static final int ID = LOC_TYPE_ID;
    }
    public static class IntegerType {
        public static final int ID = INT_TYPE_ID;
    }
    public static class NumberType {
        public static final int ID = NUMBER_TYPE_ID;
    }
    public static class RationalType {
        public static final int ID = RATIONAL_TYPE_ID;
    }
    public static class RealType {
        public static final int ID = REAL_TYPE_ID;
    }
    public static class StringType {
        public static final int ID = STR_TYPE_ID;
    }
    public static class ValueType {
        public static final int ID = VALUE_TYPE_ID;
    }
    public static class VoidType {
        public static final int ID = VOID_TYPE_ID;
    }

    public static class ParameterType {
        public static final int ID = PARAMETER_TYPE_ID;
        public static final int NAME = 1;
        public static final int BOUND = 2;
    }
    
    public static class ADTType {
        public static final int ID = ADT_TYPE_ID;
        public static final int NAME = 1;
        public static final int TYPE_PARAMS = 2;
    }
    public static class ConstructorType {
        public static final int ID = CONSTRUCTOR_TYPE_ID;
        public static final int NAME = 1;
        public static final int ADT = 2;
        public static final int FIELD_TYPES = 3;
    }
    
    public static class AliasType {
        public static final int ID = ALIAS_TYPE_ID;
        public static final int NAME = 1;
        public static final int ALIASED = 2;
        public static final int TYPE_PARAMS = 3;
    }
    public static class ListType {
        public static final int ID = LIST_TYPE_ID;
        public static final int ELEMENT_TYPE = 1;
    }
    public static class SetType {
        public static final int ID = SET_TYPE_ID;
        public static final int ELEMENT_TYPE = 1;
    }
    public static class MapType {
        public static final int ID = MAP_TYPE_ID;
        public static final int KEY_TYPE = 1;
        public static final int VALUE_TYPE = 1;
    }
    
    public static class NodeType {
        public static final int ID = NODE_TYPE_ID;
    }
    
    public static class ExternalType {
        public static final int ID = EXTERNAL_TYPE_ID;
        public static final int SYMBOL = 1;
    }
    
    public static class TupleType {
        public static final int ID = TUPLE_TYPE_ID;
        public static final int ARITY = 1;
        public static final int NAMES = 2;
        public static final int TYPES = 3;
    }
    
    public static final class Ranges {
        // these ranges are for splitting up the reader
        public static final int VALUES_MIN = PREVIOUS_VALUE_ID;
        public static final int VALUES_MAX = RAT_VALUE_ID;
        public static final int COMMON_VALUES_MIN = PREVIOUS_VALUE_ID;
        public static final int COMMON_VALUES_MAX = SET_VALUE_ID;

        
        public static final int TYPES_MIN = PREVIOUS_TYPE_ID_ID;
        public static final int TYPES_MAX = FUNCTION_TYPE_ID;
    }
}
