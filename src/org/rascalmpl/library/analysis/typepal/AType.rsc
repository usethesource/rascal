module analysis::typepal::AType

import String;
import Message;
extend analysis::typepal::ScopeGraph;

// Extend AType for type checking purposes
data AType
    = tvar(loc name)                             // type variable, used for type inference
    | lazyLub(list[AType] atypes)                // lazily computed LUB of a list of types
    | atypeList(list[AType] atypes)              // built-in list-of-ATypes type
    | overloadedAType(rel[Key, IdRole, AType] overloads) // built-in-overloaded type; each key provides an alternative type
    ;

// Pretty print ATypes
str prettyPrintAType(tvar(loc name))                = "<name>";
str prettyPrintAType(lazyLub(list[AType] atypes))   = "lub(<atypes>))";
str prettyPrintAType(atypeList(list[AType] atypes)) = size(atypes) == 0 ? "empty list of types" : intercalate(", ", [prettyPrintAType(a) | a <- atypes]);
default str prettyPrintAType(overloadedAType(rel[Key, IdRole, AType] overloads)) 
                                                    = "overloaded: {" + intercalate(", ", [prettyPrintAType(t) | <k, r, t> <- overloads]) + "}";
default str prettyPrintAType(AType tp)              = "<tp>";

// AType utilities
bool isTypeVariable(loc tv) = tv.scheme == "typevar"; 

data RuntimeException
    = checkFailed(set[Message] msgs)
    ;