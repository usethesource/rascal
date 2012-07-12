module cobra::tests::quickcheck::imported

data Imported = con(int n);

public bool propImported( Imported e ) {
	return con(_) := e;
}

public bool propOverloaded( int n ) {
	return true;
}
public bool propOverloaded( str n ) {
	return true;
}

@maxDepth{3}
@tries{12}
public bool annotatedProper( str n ){
	return true;
}

@maxDepth{0}
public bool annotatedWrongDepth( set[int] is ){
	return true;
}

@tries{0}
public test bool annotatedWrongTries( list[bool] bs ){
	return true;
}