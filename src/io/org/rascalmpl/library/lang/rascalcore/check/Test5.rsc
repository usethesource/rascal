module lang::rascalcore::check::Test5


// pointers into the stack

import demo::lang::Func::AST;

import List;

alias Env = map[str, Address];
alias PEnv = map[str, Func];

alias Result3 = tuple[Mem, int];

alias Address = int;
alias Mem = list[int];

Address push(Mem mem) {
  return size(mem);
}