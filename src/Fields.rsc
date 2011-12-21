@license{
  Copyright (c) 2009-2011 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@contributor{Mark Hills - Mark.Hills@cwi.nl (CWI)}
module lang::rascal::checker::constraints::Fields

import IO;

import lang::rascal::types::Types;
import lang::rascal::scoping::SymbolTable;

//
// TODOs:
//
// 1. Set proper type for field children in loc in fieldMap once this is implemented
//

//
// This contains the types of fields predefined on the built-in Rascal types.
//
private map[RType,map[str,RType]] fieldMap =
    ( RLocType() :
        ( "scheme" : RStrType(), "authority" : RStrType(), "host" : RStrType(), "path" : RStrType(), "parent" : RStrType(),
          "file" : RStrType(), "children" : makeListType(makeLocType()), "extension" : RStrType(), 
          "fragment" : RStrType(), "query" : RStrType(), "user" : RStrType(), "port" : RIntType(), "length" : RIntType(), 
          "offset" : RIntType(), "begin" : makeTupleType([RIntType(),RIntType()]),
          "end" : makeTupleType([RIntType(),RIntType()]), "uri" : RStrType()
        ),
      RDateTimeType() :
        ( "year" : RIntType(), "month" : RIntType(), "day" : RIntType(), "hour" : RIntType(), "minute" : RIntType(), "second" : RIntType(),
          "millisecond" : RIntType(), "timezoneOffsetHours" : RIntType(), "timezoneOffsetMinutes" : RIntType(), "century" : RIntType(),
          "isDate" : RBoolType(), "isTime" : RBoolType(), "isDateTime" : RBoolType(), "justDate" : RDateTimeType(), "justTime" : RDateTimeType()
        )
    );

//
// Look up the type of a predefined field based on the field name
//
public RType typeForField(RType source, str fieldName) {
    if (source in fieldMap) {
        if (fieldName in fieldMap[source])
            return fieldMap[source][fieldName];
    }
    throw "Invalid looking: field <fieldName> for type <prettyPrintType(source)> not in field type map.";
}

//
// Does the datetime type support field fieldName?
//
public bool dateTimeHasField(RName fieldName) {
    str fn = prettyPrintName(fieldName);
    return (fn in fieldMap[RDateTimeType()]);
}

//
// Does the loc type support field fieldName?
//
public bool locHasField(RName fieldName) {
    str fn = prettyPrintName(fieldName);
    return (fn in fieldMap[RLocType()]);
}

//
// Does type rt support field access?
//
public bool typeAllowsFields(RType rt) {
    return (isADTType(rt) || isTupleType(rt) || isRelType(rt) || isLocType(rt) || isDateTimeType(rt) || isMapType(rt));
}

//
// Does type rt support field update access? (i.e., writes through field names)
//
public bool typeAllowsUpdatableFields(RType rt) {
    return (isADTType(rt) || isTupleType(rt) || isLocType(rt) || isDateTimeType(rt) || isMapType(rt));
}

//
// Does type rt provide field fn?
//
public bool typeHasField(RType rt, RName fn, STBuilder stBuilder) {
    if (isADTType(rt)) return adtHasField(rt, fn, stBuilder);
    if (isTupleType(rt)) return tupleHasField(rt, fn);
    if (isRelType(rt)) return relHasField(rt, fn);
    if (isLocType(rt)) return locHasField(fn);
    if (isDateTimeType(rt)) return dateTimeHasField(fn);
    if (isMapType(rt)) return mapHasField(rt, fn);

    throw "Type <prettyPrintType(rt)> does not allow fields.";
}

//
// Does type rt have a field at integer index idx?
//
public bool typeHasField(RType rt, int idx, STBuilder stBuilder) {
    if (isTupleType(rt)) return tupleHasField(rt, idx);
    if (isRelType(rt)) return relHasField(rt, idx);
    if (isMapType(rt)) return mapHasField(rt, idx);

    throw "Type <prettyPrintType(rt)> does not allow integer-indexed fields.";
}

//
// Does type rt include field names?
//
public bool typeHasFieldNames(RType rt, STBuilder stBuilder) {
    if (isTupleType(rt)) return tupleHasFieldNames(rt);
    if (isRelType(rt)) return tupleHasFieldNames(getRelElementType(rt));
    if (isMapType(rt)) return mapHasFieldNames(rt);

    // TODO: May want to include other types, but this is currently only used when
    // we are using integer-indexed fields, which are only used with the above three
    // types currently.
    throw "Type <prettyPrintType(rt)> does not allow integer-indexed fields.";
}

//
// Get the name of the field at index idx from type rt
//
public str getFieldName(RType rt, int idx, STBuilder stBuilder) {
    if (isTupleType(rt)) return getTupleFieldName(rt,idx);
    if (isRelType(rt)) return getRelFieldName(rt,idx);
    if (isMapType(rt)) return getMapFieldName(rt,idx);

    // TODO: May want to include other types, but this is currently only used when
    // we are using integer-indexed fields, which are only used with the above three
    // types currently.
    throw "Type <prettyPrintType(rt)> does not allow integer-indexed fields.";
}

//
// Get the type of field fn on type rt; loc l is used in any error messages.
//
public RType getFieldType(RType rt, RName fn, STBuilder stBuilder, loc l) {
    if (isADTType(rt) && typeHasField(rt,fn,stBuilder)) return getADTFieldType(rt, fn, stBuilder);
    if (isADTType(rt)) return makeFailType("ADT <prettyPrintType(rt)> does not define field <prettyPrintName(fn)>", l);

    if (isTupleType(rt) && typeHasField(rt,fn,stBuilder)) return getTupleFieldType(rt, fn);
    if (isTupleType(rt)) return makeFailType("Tuple <prettyPrintType(rt)> does not define field <prettyPrintName(fn)>", l);

    if (isRelType(rt) && typeHasField(rt,fn,stBuilder)) return getRelFieldType(rt, fn);
    if (isRelType(rt)) return makeFailType("Relation <prettyPrintType(rt)> does not define field <prettyPrintName(fn)>", l);

    if (isMapType(rt) && typeHasField(rt,fn,stBuilder)) return getMapFieldType(rt, fn);
    if (isMapType(rt)) return makeFailType("Map <prettyPrintType(rt)> does not define field <prettyPrintName(fn)>", l);

    if (isLocType(rt) && typeHasField(rt,fn,stBuilder)) return typeForField(rt, prettyPrintName(fn));
    if (isLocType(rt)) return makeFailType("Location <prettyPrintType(rt)> does not define field <prettyPrintName(fn)>", l);

    if (isDateTimeType(rt) && typeHasField(rt,fn,stBuilder)) return typeForField(rt, prettyPrintName(fn));
    if (isDateTimeType(rt)) return makeFailType("DateTime <prettyPrintType(rt)> does not define field <prettyPrintName(fn)>", l);
    
    return makeFailType("Type <prettyType(rt)> does not have fields", l);
}

//
// Get the type of field at index idx in type rt; loc l is used in any error messages.
//
public RType getFieldType(RType rt, int idx, STBuilder stBuilder, loc l) {
    if (isTupleType(rt) && typeHasField(rt,idx,stBuilder)) return getTupleFieldType(rt, idx);
    if (isTupleType(rt)) return makeFailType("Tuple <prettyPrintType(rt)> does not define a field at index <idx>", l);

    if (isRelType(rt) && typeHasField(rt,idx,stBuilder)) return getRelFieldType(rt, idx);
    if (isRelType(rt)) return makeFailType("Relation <prettyPrintType(rt)> does not define a field at index <idx>", l);

    if (isMapType(rt) && typeHasField(rt,idx,stBuilder)) return getMapFieldType(rt, idx);
    if (isMapType(rt)) return makeFailType("Map <prettyPrintType(rt)> does not define a field at index <idx>", l);

    return makeFailType("Type <prettyType(rt)> does not have integer-indexed fields", l);
}

@doc{Check to see if a relation defines a field.}
public bool relHasField(RType t, RName fn) {
    if (isRelType(t)) {
        list[RNamedType] tas = getTupleFieldsWithNames(getRelElementType(t));
        for (ta <- tas) {
            if (RNamedType(_,fn) := ta) return true;    
        }
        return false;
    }
    throw "Cannot check for relation field on type <prettyPrintType(t)>";   
}

@doc{Check to see if a relation defines a field (by index).}
public bool relHasField(RType t, int fn) {
    if (isRelType(t)) {
        list[RNamedType] tas = getTupleFieldsWithNames(getRelElementType(t));
        return (0 <= fn) && (fn < size(tas));
    }
    throw "Cannot check for relation field on type <prettyPrintType(t)>";   
}

@doc{Return the type of a field defined on a relation (by name).}
public RType getRelFieldType(RType t, RName fn) {
    if (isRelType(t)) {
        list[RNamedType] tas = getTupleFieldsWithNames(getRelElementType(t));
        for (ta <- tas) {
            if (RNamedType(ft,fn) := ta) return ft; 
        }
        throw "Relation <prettyPrintType(t)> does not have field <prettyPrintName(fn)>";
    }
    throw "Cannot get relation field type from type <prettyPrintType(t)>";  
}

@doc{Return the type of a field defined on a relation (by index).}
public RType getRelFieldType(RType t, int fn) {
    if (isRelType(t)) {
        list[RNamedType] tas = getTupleFieldsWithNames(getRelElementType(t));
        if (0 <= fn && fn < size(tas)) return getElementType(tas[fn]);
        throw "Relation <prettyPrintType(t)> does not have a field at index <fn>";
    }
    throw "Cannot get relation field type from type <prettyPrintType(t)>";  
}

public list[RType] getRelFields(RType t) {
    if (isRelType(t)) return getTupleFields(getRelElementType(t));
    throw "Cannot get relation fields from type <prettyPrintType(t)>";  
}

public list[RNamedType] getRelFieldsWithNames(RType t) {
    if (isRelType(t)) return getTupleFieldsWithNames(getRelElementType(t));
    throw "Cannot get relation fields from type <prettyPrintType(t)>";  
}

public list[RName] getRelFieldNames(RType t) {
    if (isRelType(t)) return getTupleFieldNames(getRelElementType(t));
    throw "Cannot get relation fields from type <prettyPrintType(t)>";  
}

public RName getRelFieldName(RType t, int idx) {
    list[RName] names = getRelFieldNames(t);
    if (0 <= idx && idx < size(names)) return names[idx];
    throw "getRelFieldName given index out of bounds <idx>";
}

@doc{Check to see if an ADT defines a field.}
public bool adtHasField(RType t, RName fn, STBuilder stBuilder) {
    if (isADTType(t)) {
        if (getADTName(t) notin stBuilder.adtMap) {
            println("Warning, ADT <getADTName(t)> is not in the ADT map: type <prettyPrintType(t)>, field <fn>");
        }
        for (getADTName(t) in stBuilder.adtMap, ci <- stBuilder.adtMap[getADTName(t)].consItems, ConstructorItem(_,cts,_,_) := stBuilder.scopeItemMap[ci]) {
            for (ta <- cts) {
                if (RNamedType(_,fn) := ta) return true;
            }   
        }
        return false;
    }
    throw "adtHasField: given unexpected type <prettyPrintType(t)>";
}

//
// Look up the type of field fn on ADT t. Note that fields have a unique type in a given ADT, even if
// they appear on multiple constructors, so we can always use the first occurrence of the field we
// find on a constructor.
//
@doc{Return the type of a field on an ADT.}
public RType getADTFieldType(RType t, RName fn, STBuilder stBuilder) {
    if (isADTType(t)) {
        for (ci <- stBuilder.adtMap[getADTName(t)].consItems, ConstructorItem(_,cts,_,_) := stBuilder.scopeItemMap[ci]) {
            for (ta <- cts) {
                // See if we have a match on the field name
                if (RNamedType(ft,fn) := ta) {
                    return markUserTypes(ft,stBuilder,stBuilder.scopeItemMap[ci].parentId);
                }
            }   
        }
        throw "ADT <prettyPrintType(t)> does not have field <prettyPrintName(fn)>";
    }   
    throw "adtHasField: given unexpected type <prettyPrintType(t)>";
}
