module Test

data Bool = btrue | bfalse;
data Bool = bor(Bool L, Bool R); 

rule o1 bor(btrue, btrue)     => btrue;
rule o2 bor(btrue, bfalse)    => btrue;
rule o3 bor(bfalse, btrue)    => btrue;
rule o4 bor(bfalse, bfalse)   => bfalse;

public bool test(){
	return bor(btrue, bfalse) == btrue;
}