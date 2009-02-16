module BoolAbstractRules

data Bool = btrue;
data Bool = bfalse;
data Bool = band(Bool left, Bool right);
data Bool = bor(Bool left, Bool right);  

rule a1 band(btrue, Bool B)    => B;
rule a2 band(bfalse, Bool B)   => bfalse;

rule o1 bor(btrue, btrue)     => btrue;
rule o2 bor(btrue, bfalse)    => btrue;
rule o3 bor(bfalse, btrue)    => btrue;
rule o4 bor(bfalse, bfalse)   => bfalse;

public bool test()
{
		assert bor(band(btrue,btrue),band(btrue, bfalse)) == btrue;
		assert btrue == btrue;
		assert bfalse == bfalse;
		assert btrue != bfalse;
		assert band(btrue,bfalse) == bfalse;	
		assert band(band(btrue,btrue),band(btrue, bfalse)) == bfalse;
		assert bor(btrue,bfalse) == btrue;
		assert bor(bor(btrue,btrue),bor(btrue, bfalse)) == btrue;
		assert bor(bor(bfalse,bfalse),bor(bfalse, bfalse)) == bfalse;
		assert bor(band(btrue,btrue),band(btrue, bfalse)) == btrue;
		assert band(bor(btrue,btrue),band(btrue, bfalse)) == bfalse;
		return true;
}