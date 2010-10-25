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
      <for (x <- fields) {
          t = fields[x];
          n = capitalize(x); >
        private <t> <x>;
        public void set<n>(<t> <x>) {
          this.<x> = <x>;
        }
        public <t> get<n>() {
          return <x>;
        }
      <}>
    }
  ";

}

