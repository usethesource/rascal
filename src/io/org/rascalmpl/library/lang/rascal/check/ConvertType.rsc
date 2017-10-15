@license{
  Copyright (c) 2009-2015 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}

@contributor{Mark Hills - Mark.Hills@cwi.nl (CWI)}
@contributor{Paul Klint - Paul.Klint@cwi.nl (CWI)}
module lang::rascal::check::ConvertType

import Set;
import String;
import IO;

extend analysis::typepal::AType;
import analysis::typepal::ExtractFRModel;
extend lang::rascal::check::AType;

import lang::rascal::check::ATypeUtils;

import lang::rascal::\syntax::Rascal;
import lang::rascal::grammar::definition::Symbols;
 
@doc{Convert qualified names into an abstract representation.}
public QName convertName(QualifiedName qn) {
    if ((QualifiedName)`<{Name "::"}+ nl>` := qn) { 
        nameParts = [ (startsWith("<n>","\\") ? substring("<n>",1) : "<n>") | n <- nl ];
        if (size(nameParts) > 1) {
            return qualName(intercalate("::", nameParts[..-1]), nameParts[-1]);
        } else {
            return qualName("", nameParts[0]);
        } 
    }
    throw "Unexpected syntax for qualified name: <qn>";
}

@doc{Convert names into an abstract representation.}
public QName convertName(Name n) {
    if (startsWith("<n>","\\"))
        return qualName("", substring("<n>",1));
    else
        return qualName("","<n>");
}
@doc{Get the last part of a qualified name.}
public Name getLastName(QualifiedName qn) 
    = convertName(qn).name;

public bool isQualified(QName qn) = !isEmpty(qn.qualifier);

str prettyPrintQName(QName qname) = isEmpty(qname.qualifier) ? qname.name : "<qname.qualifier>::<qname.name>";

@doc{Convert from the concrete to the abstract representations of Rascal basic types.}
public AType convertBasicType(BasicType t, FRBuilder frb) {
    switch(t) {
        case (BasicType)`bool` : return abool();
        case (BasicType)`int` : return aint();
        case (BasicType)`rat` : return arat();
        case (BasicType)`real` : return areal();
        case (BasicType)`num` : return anum();
        case (BasicType)`str` : return astr();
        case (BasicType)`value` : return avalue();
        case (BasicType)`node` : return anode();
        case (BasicType)`void` : return avoid();
        case (BasicType)`loc` : return aloc();
        case (BasicType)`datetime` : return adatetime();

        case (BasicType)`list` : { frb.reportError(t, "Non-well-formed type, type should have one type argument"); return alist(avoid());  }
        case (BasicType)`set` : { frb.reportError(t, "Non-well-formed type, type should have one type argument"); return aset(avoid()); }
        case (BasicType)`bag` : { frb.reportError(t, "Non-well-formed type, type should have one type argument"); return abag(avoid()); }
        case (BasicType)`map` : { frb.reportError(t, "Non-well-formed type, type should have two type arguments"); return amap(avoid(),avoid()); }
        case (BasicType)`rel` : { frb.reportError(t, "Non-well-formed type, type should have one or more type arguments"); return arel([]); }
        case (BasicType)`lrel` : { frb.reportError(t, "Non-well-formed type, type should have one or more type arguments"); return alrel([]); }
        case (BasicType)`tuple` : { frb.reportError(t, "Non-well-formed type, type should have one or more type arguments"); return atuple([]); }
        case (BasicType)`type` : { frb.reportError(t, "Non-well-formed type, type should have one type argument"); return areified(avoid()); }
    }
}

@doc{Convert from the concrete to the abstract representations of Rascal type arguments.}
public AType convertTypeArg(TypeArg ta, FRBuilder frb) {
    switch(ta) {
        case (TypeArg) `<Type t>` : return convertType(t, frb);
        case (TypeArg) `<Type t> <Name n>` :  return convertType(t, frb)[label="<n>"];
    }
}

@doc{Convert lists of type arguments.}
public list[AType atype] convertTypeArgList({TypeArg ","}* tas, FRBuilder frb)
    = [convertTypeArg(ta, frb) | ta <- tas];

@doc{Convert structured types, such as list<<int>>. Check here for certain syntactical 
conditions, such as: all field names must be distinct in a given type; lists require 
exactly one type argument; etc.}
public AType convertStructuredType(StructuredType st, FRBuilder frb) {
    switch(st) {
        case (StructuredType) `list [ < {TypeArg ","}+ tas > ]` : {
            l = convertTypeArgList(tas, frb);
            if (size(l) == 1) {
                return makeListType(l[0]);        
            } else {
                frb.reportError(st, "Non-well-formed type, type should have one type argument");
                return alist(avoid()); 
            }
        }

        case (StructuredType) `set [ < {TypeArg ","}+ tas > ]` : {
            l = convertTypeArgList(tas, frb);
            if (size(l) == 1) {
                return makeSetType(l[0]);          
            } else {
                frb.reportError(st, "Non-well-formed type, type should have one type argument");
                return aset(avoid()); 
            }
        }

        case (StructuredType) `bag [ < {TypeArg ","}+ tas > ]` : {
            l = convertTypeArgList(tas, frb);
            if (size(l) == 1) {
                return abag(l[0]);        
            } else {
                frb.reportError(st, "Non-well-formed type, type should have one type argument");
                return abag(avoid()); 
            }
        }

        case (StructuredType) `map [ < {TypeArg ","}+ tas > ]` : {
            l = convertTypeArgList(tas, frb);
            if (size(l) == 2) {
                dt = l[0]; rt = l[1];
                if (!isEmpty(dt.label) && !isEmpty(rt.label) && dt.label != rt.label) { 
                    return makeMapType(dt, rt);
                } else if (!isEmpty(dt.label) && !isEmpty(rt.label) && dt.label == rt.label) {
                    frb.reportError(st,"Non-well-formed type, labels must be distinct");
                    return makeMapType(unset(dt, "label"),unset(rt,"label"));
                } else if (!isEmpty(dt.label) && isEmpty(rt.label)) {
                    frb.reportWarning(st, "Field name <fmt(dt.label)> ignored, field names must be provided for both fields or for none");
                    return makeMapType(unset(dt, "label"),rt);
                } else if (isEmpty(dt.label) && !isEmpty(rt.label)) {
                   frb.reportWarning(st, "Field name <fmt(rt.label)> ignored, field names must be provided for both fields or for none");
                    return makeMapType(dt, unset(rt, "label"));
                } else {
                    return makeMapType(dt,rt);
                }            
            } else {
                frb.reportError(st, "Non-well-formed map type, type should have two type argument");
                return makeMapType(avoid(),avoid()); 
            }
        }

        case (StructuredType) `rel [ < {TypeArg ","}+ tas > ]` : {
            l = convertTypeArgList(tas, frb);
            labelsList = [tp.label | tp <- l];
            nonEmptyLabels = [ lbl | lbl <- labelsList, !isEmpty(lbl) ];
            distinctLabels = toSet(nonEmptyLabels);
            if (size(l) == size(distinctLabels)){
                return makeRelType(l);
            } else if(size(distinctLabels) == 0) {
                return makeRelType(l);
            } else if (size(distinctLabels) != size(nonEmptyLabels)) {
                frb.reportError(st, "Non-well-formed relation type, labels must be distinct");
                return makeRelType([unset(tp, "label") | tp <- l]);
            } else if (size(distinctLabels) > 0) {
                frb.reportWarning(st, "Field name ignored, field names must be provided for all fields or for none");
                return makeRelType([unset(tp, "label") | tp <- l]);
            }
        }
        
        case (StructuredType) `lrel [ < {TypeArg ","}+ tas > ]` : {
            l = convertTypeArgList(tas, frb);
            labelsList = [tp.label | tp <- l];
            nonEmptyLabels = [ lbl | lbl <- labelsList, !isEmpty(lbl) ];
            distinctLabels = toSet(nonEmptyLabels);
            if (size(l) == size(distinctLabels)){
                return makeListRelType(l);
            } else if(size(distinctLabels) == 0) {
                return makeListRelType(l);
            } else if (size(distinctLabels) != size(nonEmptyLabels)) {
                frb.reportError(st, "Non-well-formed list relation type, labels must be distinct");
                return makeListRelType([unset(tp, "label") | tp <- l]);
            } else if (size(distinctLabels) > 0) {
                frb.reportWarning(st, "Field name ignored, field names must be provided for all fields or for none");
                return makeListRelType([unset(tp, "label") | tp <- l]);
            }
        }
        
         case (StructuredType) `tuple [ < {TypeArg ","}+ tas > ]` : {
            l = convertTypeArgList(tas, frb);
            labelsList = [tp.label | tp <- l];
            nonEmptyLabels = [ lbl | lbl <- labelsList, !isEmpty(lbl) ];
            distinctLabels = toSet(nonEmptyLabels);
            if (size(l) == size(distinctLabels)){
                return makeTupleType(l);
            } else if(size(distinctLabels) == 0) {
                return makeTupleType(l);
            } else if (size(distinctLabels) != size(nonEmptyLabels)) {
                frb.reportError(st, "Non-well-formed tuple type, labels must be distinct");
                return makeTupleType([unset(tp, "label") | tp <- l]);
            } else if (size(distinctLabels) > 0) {
                frb.reportWarning(st, "Field name ignored, field names must be provided for all fields or for none");
                return makeTupleType([unset(tp, "label") | tp <- l]);
            }
        }

        case (StructuredType) `type [ < {TypeArg ","}+ tas > ]` : { // TODO
            l = convertTypeArgList(tas, frb);
            if (size(l) == 1) {
                if (!isEmpty(l[0].label)) {
                    frb.reportWarning(st, "Field name <fmt(l[0].label)> ignored");
                    return areified(l[0]);
                } else {
                    return areified(l[0]);
                }            
            } else {
                frb.reportError(st, "Non-well-formed type, type should have one type argument");
                return areified(avoid()); 
            }
        }

        case (StructuredType) `<BasicType bt> [ < {TypeArg ","}+ tas > ]` : {
                frb.reportError(st, "Type <bt> does not accept type parameters");
                return avoid();
        }
    }
}

@doc{Convert Rascal function types into their abstract representation.}
public AType convertFunctionType(FunctionType ft, FRBuilder frb) {
    if ((FunctionType) `<Type t> ( <{TypeArg ","}* tas> )` := ft) {
        l = convertTypeArgList(tas, frb);
        tp = convertType(t, frb);
        if (size(l) == 0) {
            return afunc(tp, atypeList([]), []);
        } else {
            labelsList = [tp.label | tp <- l];;
            nonEmptyLabels = [ lbl | lbl <- labelsList, !isEmpty(lbl) ];
            distinctLabels = toSet(nonEmptyLabels);
            if(size(distinctLabels) == 0)
                return afunc(tp, atypeList(l), []);
            if (size(l) == size(distinctLabels)) {
                return afunc(tp, atypeList(l), []);
            } else if (size(distinctLabels) > 0 && size(distinctLabels) != size(labelsList)) {
                 frb.reportError(ft, "Non-well-formed type, labels must be distinct");
                return afunc(tp, atypeList([unset(tp, "label") | tp <- l]), []);
            } else if (size(labels) > 0) {
                frb.reportWarning(ft, "Field name ignored, field names must be provided for all fields or for none");
                return afunc(tp, atypeList([unset(tp, "label") | tp <- l]), []);
            }
        } 
    }
}

@doc{Convert Rascal user types into their abstract representation.}
public AType convertUserType(UserType ut, FRBuilder frb) {
    switch(ut) {
        case (UserType) `<QualifiedName n>` : return auser(convertName(n),[]);
        case (UserType) `<QualifiedName n>[ <{Type ","}+ ts> ]` : {
            paramTypes = [convertType(ti, frb) | ti <- ts ];
            return auser(convertName(n), paramTypes);
        }
    }
}

//public Symbol convertSymbol(Sym sym) = sym2symbol(sym)[@at=sym@\loc];  

@doc{Get the raw Name component from a user type.}
public Name getUserTypeRawName(UserType ut, FRBuilder frb) {
    switch(ut) {
        case (UserType) `<QualifiedName n>` : return getLastName(n);
        case (UserType) `<QualifiedName n>[ <{Type ","}+ ts> ]` : return getLastName(n);
    }
}

@doc{Convert Rascal type variables into their abstract representation.}
public AType convertTypeVar(TypeVar tv, FRBuilder frb) {
    switch(tv) {
        case (TypeVar) `& <Name n>` : return aparameter("<n>",avalue());
        case (TypeVar) `& <Name n> \<: <Type tb>` : {
            return aparameter("<n>",convertType(tb, frb));
        }
    }
}

@doc{Convert Rascal data type selectors into an abstract representation.}
@todo{Implement once this is in use.}
public AType convertDataTypeSelector(DataTypeSelector dts, FRBuilder frb) {
    switch(dts) {
        case (DataTypeSelector) `<QualifiedName n1> . <Name n2>` : throw "Not implemented";
    }
}

@doc{Main driver routine for converting Rascal types into abstract type representations.}
public AType convertType(Type t, FRBuilder frb) {
    switch(t) {
        case (Type) `<BasicType bt>` : return convertBasicType(bt, frb);
        case (Type) `<StructuredType st>` : return convertStructuredType(st, frb);
        case (Type) `<FunctionType ft>` : return convertFunctionType(ft, frb);
        case (Type) `<TypeVar tv>` : return convertTypeVar(tv, frb);
        case (Type) `<UserType ut>` : return convertUserType(ut, frb);
        case (Type) `<DataTypeSelector dts>` : return convertDataTypeSelector(dts, frb);
        case (Type) `<Sym sym>` : return convertSymbol(sym, frb);
        case (Type) `( <Type tp> )` : return convertType(tp, frb);
        default : { throw "Error in convertType, unexpected type syntax: <t>"; }
    }
}

@doc{A parsing function, useful for generating test cases.}
public Type parseType(str s) {
    return parse(#Type, s);
}