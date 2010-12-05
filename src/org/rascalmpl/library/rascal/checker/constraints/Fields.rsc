module rascal::checker::constraints::Fields

import rascal::types::Types;
import rascal::scoping::SymbolTable;

// TODO: Guessing at type of children in loc, not implemented yet
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

public RType typeForField(RType source, str fieldName) {
    if (source in fieldMap) {
        if (fieldName in fieldMap[source])
            return fieldMap[source][fieldName];
    }
    throw "Invalid looking: field <fieldName> for type <prettyPrintType(source)> not in field type map.";
}

public bool dateTimeHasField(RName fieldName) {
    str fn = prettyPrintName(fieldName);
    return (fn in fieldMap[RDateTimeType()]);
}

public bool locHasField(RName fieldName) {
    str fn = prettyPrintName(fieldName);
    return (fn in fieldMap[RLocType()]);
}

public bool typeAllowsFields(RType rt) {
    return (isADTType(rt) || isTupleType(rt) || isRelType(rt) || isLocType(rt) || isDateTimeType(rt) || isMapType(rt));
}

public bool typeHasField(RType rt, RName fn, SymbolTable symbolTable) {
    if (isADTType(rt)) return adtHasField(rt, fn, symbolTable);
    if (isTupleType(rt)) return tupleHasField(rt, fn);
    if (isRelType(rt)) return relHasField(rt, fn);
    if (isLocType(rt)) return locHasField(fn);
    if (isDateTimeType(rt)) return dateTimeHasField(fn);
    if (isMapType(rt)) return mapHasField(rt, fn);

    throw "Type <prettyPrintType(rt)> does not allow fields.";
}

public bool typeHasField(RType rt, int fn, SymbolTable symbolTable) {
    if (isTupleType(rt)) return tupleHasField(rt, fn);
    if (isRelType(rt)) return relHasField(rt, fn);
    if (isMapType(rt)) return mapHasField(rt, fn);

    throw "Type <prettyPrintType(rt)> does not allow integer-indexed fields.";
}

public bool typeHasFieldNames(RType rt, SymbolTable symbolTable) {
    if (isTupleType(rt)) return tupleHasFieldNames(rt);
    if (isRelType(rt)) return tupleHasFieldNames(getRelElementType(rt));
    if (isMapType(rt)) return mapHasFieldNames(rt);

    // TODO: May want to include other types, but this is currently only used when
    // we are using integer-indexed fields, which are only used with the above three
    // types currently.
    throw "Type <prettyPrintType(rt)> does not allow integer-indexed fields.";
}

public bool getFieldName(RType rt, int idx, SymbolTable symbolTable) {
    if (isTupleType(rt)) return getTupleFieldName(rt,idx);
    if (isRelType(rt)) return getRelFieldName(rt,idx);
    if (isMapType(rt)) return getMapFieldName(rt,idx);

    // TODO: May want to include other types, but this is currently only used when
    // we are using integer-indexed fields, which are only used with the above three
    // types currently.
    throw "Type <prettyPrintType(rt)> does not allow integer-indexed fields.";
}

public RType getFieldType(RType rt, RName fn, SymbolTable symbolTable, loc l) {
    if (isADTType(rt) && typeHasField(rt,fn,symbolTable)) return getADTFieldType(rt, fn, symbolTable);
    if (isADTType(rt)) return makeFailType("ADT <prettyPrintType(rt)> does not define field <prettyPrintName(fn)>", l);

    if (isTupleType(rt) && typeHasField(rt,fn,symbolTable)) return getTupleFieldType(rt, fn);
    if (isTupleType(rt)) return makeFailType("Tuple <prettyPrintType(rt)> does not define field <prettyPrintName(fn)>", l);

    if (isRelType(rt) && typeHasField(rt,fn,symbolTable)) return getRelFieldType(rt, fn);
    if (isRelType(rt)) return makeFailType("Relation <prettyPrintType(rt)> does not define field <prettyPrintName(fn)>", l);

    if (isMapType(rt) && typeHasField(rt,fn,symbolTable)) return getMapFieldType(rt, fn);
    if (isMapType(rt)) return makeFailType("Map <prettyPrintType(rt)> does not define field <prettyPrintName(fn)>", l);

    if (isLocType(rt) && typeHasField(rt,fn,symbolTable)) return typeForField(rt, prettyPrintName(fn));
    if (isLocType(rt)) return makeFailType("Location <prettyPrintType(rt)> does not define field <prettyPrintName(fn)>", l);

    if (isDateTimeType(rt) && typeHasField(rt,fn,symbolTable)) return typeForField(rt, prettyPrintName(fn));
    if (isDateTimeType(rt)) return makeFailType("DateTime <prettyPrintType(rt)> does not define field <prettyPrintName(fn)>", l);
    
    return makeFailType("Type <prettyType(rt)> does not have fields", l);
}

public RType getFieldType(RType rt, int fn, SymbolTable symbolTable, loc l) {
    if (isTupleType(rt) && typeHasField(rt,fn,symbolTable)) return getTupleFieldType(rt, fn);
    if (isTupleType(rt)) return makeFailType("Tuple <prettyPrintType(rt)> does not define a field at index <fn>", l);

    if (isRelType(rt) && typeHasField(rt,fn,symbolTable)) return getRelFieldType(rt, fn);
    if (isRelType(rt)) return makeFailType("Relation <prettyPrintType(rt)> does not define a field at index <fn>", l);

    if (isMapType(rt) && typeHasField(rt,fn,symbolTable)) return getMapFieldType(rt, fn);
    if (isMapType(rt)) return makeFailType("Map <prettyPrintType(rt)> does not define a field at index <fn>", l);

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
public bool adtHasField(RType t, RName fn, SymbolTable symbolTable) {
    if (isADTType(t)) {
        for (ci <- symbolTable.adtMap[getADTName(t)].consItems, ConstructorItem(_,cts,_,_) := symbolTable.scopeItemMap[ci]) {
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
public RType getADTFieldType(RType t, RName fn, SymbolTable symbolTable) {
    if (isADTType(t)) {
        for (ci <- symbolTable.adtMap[getADTName(t)].consItems, ConstructorItem(_,cts,_,_) := symbolTable.scopeItemMap[ci]) {
            for (ta <- cts) {
                // See if we have a match on the field name
                if (RNamedType(ft,fn) := ta) {
                    return markUserTypes(ft,symbolTable,symbolTable.scopeItemMap[ci].parentId);
                }
            }   
        }
        throw "ADT <prettyPrintType(t)> does not have field <prettyPrintName(fn)>";
    }   
    throw "adtHasField: given unexpected type <prettyPrintType(t)>";
}
