module Bool-abstract-rules

data Bool btrue;
data Bool bfalse;
data Bool band(Bool L, Bool R);
data Bool bor(Bool L, Bool R);  

rule a1 band(btrue, btrue)    => btrue;
rule a2 band(btrue, bfalse)   => bfalse;
rule a3 band(bfalse, btrue)   => bfalse;
rule a4 band(bfalse, bfalse)  => bfalse;

rule o1 bor(btrue, btrue)     => btrue;
rule o2 bor(btrue, bfalse)    => btrue;
rule o3 bor(bfalse, btrue)    => btrue;
rule o4 bor(bfalse, bfalse)   => bfalse;

public Bool test1()
{
	return bor(band(btrue,btrue),band(btrue, bfalse));
}