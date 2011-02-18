module rascal::types::TypeExceptions

import Exception;
import rascal::types::Types;

data RuntimeException =
      UnexpectedRType(RType t1)
    | UnexpectedRTypes(RType t1, RType t2)
    | UnimplementedRType(RType t1)
    | CannotCalculateBindings(RType t1, RType t2, loc l)
    ;
