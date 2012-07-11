module cobra::util::outputlogger
import IO;
import Exception;

@javaClass{org.rascalmpl.library.cobra.util.OutputLogger}
@reflect
public java void startLog();

@javaClass{org.rascalmpl.library.cobra.util.OutputLogger}
@reflect
public java str getLog();

test bool startLogShouldResetLogger(){
	startLog();
	print("test1");
	startLog();
	print("test2");
	return getLog() == "test2";
}


test bool testOutputLogger(){
	startLog();
	print("test1");
	str log1 = getLog();
	startLog();
	print("test2");
	str log2 = getLog();
	return log1+log2 == "test1test2";
}

test bool testFailsWhenGetLogCalledTwice(){
	try {
	startLog();
	print("test1");
	getLog();
	getLog();
	} catch PermissionDenied( "getLog called before startLog" ): return true;
	
	return false;
}


test bool testFailsWhenGetLogCalledEarly(){
	try getLog();
	catch PermissionDenied( "getLog called before startLog" ): return true;
	
	return false; 
}

public test bool shouldReportPrintedChars( str a ){
	startLog();
	print(a);
	return getLog() == a;
}


test bool testPrintln(){
	startLog();
	println("test1");
	println("test2");
	str result = getLog();
	return "test1\ntest2\n" == result;
}
