@license{
  Copyright (c) 2009-2015 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}

@contributor{Mark Hills - Mark.Hills@cwi.nl (CWI)}
@bootstrapParser
module lang::rascal::types::ConvertType

import Set;
import String;
import IO;
import Message;
import Type;

import lang::rascal::types::AbstractName;
import lang::rascal::types::AbstractType;

import lang::rascal::\syntax::Rascal;
import lang::rascal::grammar::definition::Symbols;

@doc{Annotations for adding error and warning information to types} 
data Symbol(set[Message] errinfo = {});

@doc{Mark the location of the type in the source file} 
data Symbol(loc at = |unknown:///|);

@doc{Convert from the concrete to the abstract representations of Rascal basic types.}
public Symbol convertBasicType(BasicType t) {
    switch(t) {
        case (BasicType)`bool` : return \bool();
        case (BasicType)`int` : return \int();
        case (BasicType)`rat` : return \rat();
        case (BasicType)`real` : return \real();
        case (BasicType)`num` : return \num();
        case (BasicType)`str` : return \str();
        case (BasicType)`value` : return \value();
        case (BasicType)`node` : return \node();
        case (BasicType)`void` : return \void();
        case (BasicType)`loc` : return \loc();
        case (BasicType)`datetime` : return \datetime();

        case (BasicType)`list` : 
            return \list(Symbol::\void())[errinfo={ error("Non-well-formed type, type should have one type argument", t.origin) }];
        case (BasicType)`set` : 
            return \set(Symbol::\void())[errinfo={ error("Non-well-formed type, type should have one type argument", t.origin) }];
        case (BasicType)`bag` : 
            return \bag(Symbol::\void())[errinfo={ error("Non-well-formed type, type should have one type argument", t.origin) }];
        case (BasicType)`map` : 
            return \map(Symbol::\void(),Symbol::\void())[errinfo={ error("Non-well-formed type, type should have two type arguments", t.origin) }];
        case (BasicType)`rel` : 
            return \rel([])[errinfo={ error("Non-well-formed type, type should have one or more type arguments", t.origin) }];
        case (BasicType)`lrel` : 
            return \lrel([])[errinfo={ error("Non-well-formed type, type should have one or more type arguments", t.origin) }];
        case (BasicType)`tuple` : 
            return \tuple([])[errinfo={ error("Non-well-formed type, type should have one or more type arguments", t.origin) }];
        case (BasicType)`type` : 
            return \reified(Symbol::\void())[errinfo={ error("Non-well-formed type, type should have one type argument", t.origin) }];
    }
}

@doc{Convert from the concrete to the abstract representations of Rascal type arguments.}
public Symbol convertTypeArg(TypeArg ta) {
    switch(ta) {
        case (TypeArg) `<Type t>` : return convertType(t);
        case (TypeArg) `<Type t> <Name n>` : return \label(getSimpleName(convertName(n)), convertType(t));
    }
}

@doc{Convert lists of type arguments.}
public list[Symbol] convertTypeArgList({TypeArg ","}* tas) {
    return [convertTypeArg(ta) | ta <- tas];
}

@doc{Convert structured types, such as list[int]. Check here for certain syntactical 
conditions, such as: all field names must be distinct in a given type; lists require 
exactly one type argument; etc.}
public Symbol convertStructuredType(StructuredType st) {
    switch(st) {
        case (StructuredType) `list [ < {TypeArg ","}+ tas > ]` : {
            l = convertTypeArgList(tas);
            if (size(l) == 1) {
				if (\label(_,ltype) := l[0]) {
					return \list(ltype)[errinfo={ warning("Field name ignored", st.origin) }];
				} else {
					return \list(l[0]);
				}            
            } else {
            	return \list(Symbol::\void())[errinfo={ error("Non-well-formed type, type should have one type argument",st.origin) }]; 
            }
        }

        case (StructuredType) `set [ < {TypeArg ","}+ tas > ]` : {
            l = convertTypeArgList(tas);
            if (size(l) == 1) {
				if (\label(_,ltype) := l[0]) {
					return \set(ltype)[errinfo={ warning("Field name ignored", st.origin) }];
				} else {
					return \set(l[0]);
				}            
            } else {
            	return \set(Symbol::\void())[errinfo={ error("Non-well-formed type, type should have one type argument",st.origin) }]; 
            }
        }

        case (StructuredType) `bag [ < {TypeArg ","}+ tas > ]` : {
            l = convertTypeArgList(tas);
            if (size(l) == 1) {
				if (\label(_,ltype) := l[0]) {
					return \bag(ltype)[errinfo={ warning("Field name ignored", st.origin) }];
				} else {
					return \bag(l[0]);
				}            
            } else {
            	return \bag(Symbol::\void())[errinfo={ error("Non-well-formed type, type should have one type argument",st.origin) }]; 
            }
        }

        case (StructuredType) `map [ < {TypeArg ","}+ tas > ]` : {
            l = convertTypeArgList(tas);
            if (size(l) == 2) {
				if (\label(dl,dt) := l[0] && \label(rl,rt) := l[1] && dl != rl) {
					return \map(l[0],l[1]);
				} else if (\label(dl,dt) := l[0] && \label(rl,rt) := l[1] && dl == rl) {
					return \map(dt,rt)[errinfo={ error("Non-well-formed type, labels must be distinct", st.origin) }];
				} else if (\label(dl,dt) := l[0] && \label(rl,rt) !:= l[1]) {
					return \map(dt,l[1])[errinfo={ warning("Field name ignored, field names must be provided for both fields or for none", st.origin) }];
				} else if (\label(dl,dt) !:= l[0] && \label(rl,rt) := l[1]) {
					return \map(l[0],rt)[errinfo={ warning("Field name ignored, field names must be provided for both fields or for none", st.origin) }];
				} else {
					return \map(l[0],l[1]);
				}            
            } else {
            	return \map(Symbol::\void(),Symbol::\void())[errinfo={ error("Non-well-formed type, type should have two type argument",st.origin) }]; 
            }
        }

        case (StructuredType) `rel [ < {TypeArg ","}+ tas > ]` : {
            l = convertTypeArgList(tas);
            labels = {fl | \label(fl,_) <- l};
            labelsList = [fl | \label(fl,_) <- l];
            if (size(l) == size(labels) || size(labels) == 0) {
            	return \rel(l);
            } else if (size(labels) > 0 && size(labels) != size(labelsList)) {
            	return \rel([ (\label(fl,ft) := li) ? ft : li | li <- l ])[errinfo={ error("Non-well-formed type, labels must be distinct", st.origin) }];
            } else if (size(labels) > 0) {
            	return \rel([ (\label(fl,ft) := li) ? ft : li | li <- l ])[errinfo={ warning("Field name ignored, field names must be provided for all fields or for none", st.origin) }];
            }
        }
        
        case (StructuredType) `lrel [ < {TypeArg ","}+ tas > ]` : {
            l = convertTypeArgList(tas);
            labelsList = [fl | \label(fl,_) <- l];
            labels = {*labelsList};
            if (size(l) == size(labels) || size(labels) == 0) {
            	return \lrel(l);
            } else if (size(labels) > 0 && size(labels) != size(labelsList)) {
            	return \lrel([ (\label(fl,ft) := li) ? ft : li | li <- l ])[errinfo={ error("Non-well-formed type, labels must be distinct", st.origin) }];
            } else if (size(labels) > 0) {
            	return \lrel([ (\label(fl,ft) := li) ? ft : li | li <- l ])[errinfo={ warning("Field name ignored, field names must be provided for all fields or for none", st.origin) }];
            }
        }

        case (StructuredType) `tuple [ < {TypeArg ","}+ tas > ]` : {
            l = convertTypeArgList(tas);
            labels = {fl | \label(fl,_) <- l};
            labelsList = [fl | \label(fl,_) <- l];
            if (size(l) == size(labels) || size(labels) == 0) {
            	return \tuple(l);
            } else if (size(labels) > 0 && size(labels) != size(labelsList)) {
            	return \tuple([ (\label(fl,ft) := li) ? ft : li | li <- l ])[errinfo={ error("Non-well-formed type, labels must be distinct", st.origin) }];
            } else if (size(labels) > 0) {
            	return \tuple([ (\label(fl,ft) := li) ? ft : li | li <- l ])[errinfo={ warning("Field name ignored, field names must be provided for all fields or for none", st.origin) }];
            }
        }

        case (StructuredType) `type [ < {TypeArg ","}+ tas > ]` : {
            l = convertTypeArgList(tas);
            if (size(l) == 1) {
				if (\label(_,ltype) := l[0]) {
					return \reified(ltype)[errinfo={ warning("Field name ignored", st.origin) }];
				} else {
					return \reified(l[0]);
				}            
            } else {
            	return \reified(Symbol::\void())[errinfo={ error("Non-well-formed type, type should have one type argument",st.origin) }]; 
            }
        }

		case (StructuredType) `<BasicType bt> [ < {TypeArg ","}+ tas > ]` : {
		        return Symbol::\void()[errinfo={error("Type <bt> does not accept type parameters",st.origin)}];
		}
	}
}

@doc{Convert Rascal function types into their abstract representation.}
public Symbol convertFunctionType(FunctionType ft) {
    if ((FunctionType) `<Type t> ( <{TypeArg ","}* tas> )` := ft) {
        l = convertTypeArgList(tas);
        if (size(l) == 0) {
        	return \func(convertType(t), []);
        } else {
            labels = {fl | \label(fl,_) <- l};
            labelsList = [fl | \label(fl,_) <- l];
            if (size(l) == size(labels) || size(labels) == 0) {
            	return \func(convertType(t), l);
            } else if (size(labels) > 0 && size(labels) != size(labelsList)) {
            	return \func(convertType(t), [ (\label(fl,ftype) := li) ? ftype : li | li <- l ])[errinfo={ error("Non-well-formed type, labels must be distinct", ft.origin) }];
            } else if (size(labels) > 0) {
            	return \func(convertType(t), [ (\label(fl,ftype) := li) ? ftype : li | li <- l ])[errinfo={ warning("Field name ignored, field names must be provided for all fields or for none", ft.origin) }];
            }
        } 
    }
}

@doc{Convert Rascal user types into their abstract representation.}
public Symbol convertUserType(UserType ut) {
    switch(ut) {
        case (UserType) `<QualifiedName n>` : return \user(convertName(n),[])[at=ut.origin];
        case (UserType) `<QualifiedName n>[ <{Type ","}+ ts> ]` : return \user(convertName(n),[convertType(ti) | ti <- ts])[at=ut.origin];
    }
}

// TODO: Adding this at is harmful for efficiency, since symbols are used as identities for types:
public Symbol convertSymbol(Sym sym) = sym2symbol(sym)[@at=sym.origin];  
 
@doc{Get the raw Name component from a user type.}
public Name getUserTypeRawName(UserType ut) {
    switch(ut) {
        case (UserType) `<QualifiedName n>` : return getLastName(n);
        case (UserType) `<QualifiedName n>[ <{Type ","}+ ts> ]` : return getLastName(n);
    }
}

@doc{Convert Rascal type variables into their abstract representation.}
public Symbol convertTypeVar(TypeVar tv) {
    switch(tv) {
        case (TypeVar) `& <Name n>` : return \parameter(getSimpleName(convertName(n)),Symbol::\value())[boundGiven=false];
        case (TypeVar) `& <Name n> \<: <Type tb>` : return \parameter(getSimpleName(convertName(n)),convertType(tb))[boundGiven=true];
    }
}

@doc{Convert Rascal data type selectors into an abstract representation.}
@todo{Implement once this is in use.}
public Symbol convertDataTypeSelector(DataTypeSelector dts) {
    switch(dts) {
        case (DataTypeSelector) `<QualifiedName n1> . <Name n2>` : throw "Not implemented";
    }
}

@doc{Main driver routine for converting Rascal types into abstract type representations.}
public Symbol convertType(Type t) {
    switch(t) {
        case (Type) `<BasicType bt>` : return convertBasicType(bt);
        case (Type) `<StructuredType st>` : return convertStructuredType(st);
        case (Type) `<FunctionType ft>` : return convertFunctionType(ft);
        case (Type) `<TypeVar tv>` : return convertTypeVar(tv);
        case (Type) `<UserType ut>` : return convertUserType(ut);
        case (Type) `<DataTypeSelector dts>` : return convertDataTypeSelector(dts);
        case (Type) `<Sym sym>` : return convertSymbol(sym);
        case (Type) `( <Type tp> )` : return convertType(tp);
        default : { throw "Error in convertType, unexpected type syntax: <t>"; }
    }
}

@doc{A parsing function, useful for generating test cases.}
public Type parseType(str s) {
	return parse(#Type, s);
}
