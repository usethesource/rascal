module cobra::tests::util

import cobra::util;

test bool testGetSomeFromSet(){
	set[int] elms = {1,2,3,4};
	return getSomeFrom(elms) <= elms; 
}

test bool testGetSomeFromEmptySet(){
	set[int] elms = {};
	set[int] subset = getSomeFrom(elms); 
	return subset <= elms && subset == {}; 
}

test bool testGetSomeFromList(){
	list[int] elms = [1,2,3,4];
	return getSomeFrom(elms) <= elms; 
}

test bool testGetSomeFromEmptyList(){
	list[int] elms = [];
	list[int] sublist = getSomeFrom(elms); 
	return sublist <= elms && sublist == []; 
}


