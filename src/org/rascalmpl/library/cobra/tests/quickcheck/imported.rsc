@license{
  Copyright (c) 2009-2015 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Wietse Venema - wietsevenema@gmail.com - CWI}
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