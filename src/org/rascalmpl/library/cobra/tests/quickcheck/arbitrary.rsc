module cobra::tests::quickcheck::arbitrary

import Type;
import Prelude;
import cobra::quickcheck;
import Node;
import cobra::tests::quickcheck::imported;

test bool testArbitraryIntShouldBeInt(){
	return typeOf(arbitrary(#int, 5)) == \int();
}

test bool testArbitraryStringShouldBeString(){
	return typeOf(arbitrary(#str, 5)) == \str();
}

data myData = myData(int n);

test bool testArbitraryShouldThrowIllegalArgument(){
	try arbitrary(#myData, 0);
	catch IllegalArgument(0, _): return true;
	return false;  
}


test bool testArbitraryShouldGenerateEmptyLists(){
	bool result = true;
	for( int n <- [1..10] ) result = result && arbitrary(#(list[myData]), 1) == [];
	return result;
}

test bool testArbitraryShouldGenerateEmptySets(){
	bool result = true;
	for( int n <- [1..10] ) result = result && arbitrary(#(set[myData]), 1) == {};
	return result;
}


test bool testArbitraryShouldGenerateEmptyMaps1(){
	bool result = true;
	for( int n <- [1..30] ) result = result && arbitrary(#(map[int,myData]),1) == ();
	return result;
}

test bool testArbitraryShouldGenerateEmptyMaps2(){
	bool result = true;
	for( int n <- [1..30] ) result = result && arbitrary(#(map[myData,int]),1) == ();
	return result;
}



test bool illegalArgumentWithTuple(){
	try arbitrary(#(tuple[myData]), 1);
	catch IllegalArgument(1,"No construction possible at this depth or less."): return true;
	
	return false;  
}


test bool arbitraryShouldWorkWithImportedDataType(){
	return con(_) := arbitrary(#Imported, 1); 
}

data MyGraph = vertex(str name, int x, int y) | edge(str from, str to);
data MyLayoutStrategy = dot() | tree() | force() | hierarchy() | fisheye();
anno MyLayoutStrategy MyGraph@strategy;

test bool arbitraryGeneratesAnnotations(){
	MyGraph g = arbitrary(#MyGraph, 5);
	return MyLayoutStrategy l := g@strategy;
}


