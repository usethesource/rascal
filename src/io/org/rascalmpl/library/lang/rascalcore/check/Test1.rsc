module lang::rascalcore::check::Test1

data Exp = call(str name, list[Exp] args);

int push(list[int] mem);

tuple[list[int], int] eval3(call(str name, list[Exp] args), map[str, int] env, list[int] mem) {
   int f;
   for (Exp a <- args) {
     <mem, v> = <[1], 0>; //eval3(a, env, mem);
     append v;
   }
   <mem, v> = <[], 0>; //eval3(f.body, env, mem);
   return <[], 0>; //<pop(mem, scope), v>; 
}