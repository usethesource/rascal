
module lang::rascalcore::check::Test2

import ValueIO;

value f() = $2010-15-15T09:15:23.123+03:00$;

str s = "\uf0000";

value g() { try return readTextValueString("$2010-15-15T09:15:23.123+03:00$");
            catch IO(msg): return msg;
          }