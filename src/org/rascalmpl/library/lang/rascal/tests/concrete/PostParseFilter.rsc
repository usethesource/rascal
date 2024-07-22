module lang::rascal::tests::concrete::PostParseFilter

// copied from issue report #1210
import Exception;
import ParseTree;

syntax X = a: A | b: B ;
syntax A = "X" ;
syntax B = "X" ;

// Match a concrete pattern to have an X as return value.
X amb({f:(X)`<A _>`, *value _}) = f ;

test bool t1()
{
	try
	{
	    // TODO: if allowAmbiguity=false one might expect this test to succeed as well,
	    // but it doesn't because the rewrite rule is never executed before the exception
	    // has been thrown already. See the discussion with issue #1210
		Tree t = parse( #X, "X", allowAmbiguity=true ) ;
		return true ;
	}
	catch Ambiguity( loc _, str _, str _ ):
	{
		Tree t = parse( #X, "X", allowAmbiguity=true ) ;
		assert /amb(_) !:= t : "Tree still contains an ambiguity node." ;
		return false ;
	}
}
