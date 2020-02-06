module lang::rascalcore::compile::Examples::Tst1

@doc{
.Synopsis
control points in source code

.Description

Control points in executable units of code are either straightline
code (block), or forks. Each executable unit has an entry and an exit
node. This is the simplest model for control flow nodes which may hold
all the possible structures we find in real executable units, but it
does require an analysis which resolves the locations of each block
and the labels which are used to jump to. 
}
data ControlNode
  = 
   \entry(loc id) // start node of an executable unit
  ;
       

data CFG = cfg(loc id, ControlNode entry = entry(id));


//void(int) f() { return void(int i) { return; }; }
