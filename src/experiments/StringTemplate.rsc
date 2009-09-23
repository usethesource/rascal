module experiments::StringTemplate

import String;

public map[str, str] fields = (
   "name" : "String",
   "age" : "Integer",
   "address" : "String"
);

public str capitalize(str s) {
  return toUpperCase(substring(s, 0, 1)) + substring(s, 1);
}

public str genClass(str name, map[str,str] fields) {
  return "
    public class <name> {
      <for (x <- fields) {>
        private <fields[x]> <x>;
        public void set<capitalize(x)>(<fields[x]> <x>) {
          this.<x> = <x>;
        }
        public <fields[x]> get<capitalize(x)>() {
          return <x>;
        }
      <}>
    }
  ";

}

