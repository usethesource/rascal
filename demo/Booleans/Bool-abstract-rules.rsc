module Bool-abstract-rules

data Bool btrue;
data Bool bfalse;
data Bool band(Bool L, Bool R);
data Bool bor(Bool L, Bool R);  

rule a1 band(btrue, Bool B)    => B;
rule a2 band(bfalse, Bool B)   => bfalse;

rule o1 bor(btrue, btrue)     => btrue;
rule o2 bor(btrue, bfalse)    => btrue;
rule o3 bor(bfalse, btrue)    => btrue;
rule o4 bor(bfalse, bfalse)   => bfalse;

public Bool testOr()
{
	return bor(band(btrue,btrue),band(btrue, bfalse));
}