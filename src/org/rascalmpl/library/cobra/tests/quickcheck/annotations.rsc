module cobra::tests::quickcheck::annotations
import cobra::util::outputlogger;
import cobra::tests::quickcheck::imported;


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