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