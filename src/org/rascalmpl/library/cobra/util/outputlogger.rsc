@license{
  Copyright (c) 2009-2015 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Wietse Venema - wietsevenema@gmail.com - CWI}
module cobra::util::outputlogger
import IO;
import Exception;

@javaClass{org.rascalmpl.library.cobra.util.OutputLogger}
@reflect
public java void startLog();

@javaClass{org.rascalmpl.library.cobra.util.OutputLogger}
@reflect
public java str getLog();

test bool startLogShouldResetLogger(str a, str b){
	startLog();
	print(a);
	startLog();
	print(b);
	return getLog() == b;
}


test bool testOutputLogger( str a, str b){
	startLog();
	print(a);
	str log1 = getLog();
	startLog();
	print(b);
	str log2 = getLog();
	return log1+log2 == "<a><b>";
}

test bool testFailsWhenGetLogCalledTwice( str a){
	try {
	startLog();
	print(a);
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


test bool testPrintln(str a, str b){
	startLog();
	println(a);
	println(b);
	str result = getLog();
	return "<a>\n<b>\n" == result;
}
