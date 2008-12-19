module ATerms

import languages/aterm/syntax/IntCon;
import languages/aterm/syntax/RealCon;
import basic/StrCon;
import basic/IdentifierCon;

data AFun quoted(StrCon);
data AFun unquoted(IdCon);

data ATerm aint(IntCon);
data ATerm areal(RealCon);
data ATerm afun(AFun);
data ATerm appl(AFun fn, list[ATerm] args);
data ATerm placeholder(ATerm atype);
data ATerm \list(list[ATerm] elems);
data ATerm annotated(ATerm trm, Annotation);

data Annotation \default(list[ATerm] annos);




