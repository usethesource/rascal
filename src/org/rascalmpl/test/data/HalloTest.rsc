module HalloTest

import languages::pico::syntax::Pico;
import IO;

public Tree simple = 
begin
  declare
    x: natural,
    s: string;

  x := 10;
  while x do
    x := x - 1;
    s := s || "#"
  od
end;



public bool test() {
  if ([| begin declare <decls>; <stats> end |] := simple) {
    println("decls: <decls>");
    println("stats: <stats>");
    return true;
  }
  return false;
}
