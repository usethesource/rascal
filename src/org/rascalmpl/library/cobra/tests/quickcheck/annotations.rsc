@license{
  Copyright (c) 2009-2015 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Wietse Venema - wietsevenema@gmail.com - CWI}
module cobra::tests::quickcheck::annotations

import cobra::quickcheck;
import cobra::util::outputlogger;
import cobra::tests::quickcheck::imported;
import Exception;


test bool annotationsHaveEffect(){
	startLog();
	quickcheck( annotatedProper );
	return /^Not refuted after 12 tries with maximum depth 3$/ := getLog();
}

test bool noAnnotationMeansDefault(){
	startLog();
	quickcheck( bool(int a){ return true; } );
	return /^Not refuted after 100 tries with maximum depth 5$/ := getLog();
}

test bool canOverrideAnnotation(){
	startLog();
	quickcheck( annotatedProper, 1, 2 );
	return /^Not refuted after 2 tries with maximum depth 1$/ := getLog();
}

test bool wrongDepthAnnotationRaisesError(){
	try quickcheck( annotatedWrongDepth );
	catch IllegalArgument("maxDepth","Annotation: maxDepth smaller than 1"): return true;
	
	return false; 
}

test bool wrongTriesAnnotationRaisesError(){
	try quickcheck( annotatedWrongTries );
	catch IllegalArgument("tries","Annotation: tries smaller than 1"): return true;
	
	return false;
}