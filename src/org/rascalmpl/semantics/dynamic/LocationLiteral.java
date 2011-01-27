package org.rascalmpl.semantics.dynamic;

public abstract class LocationLiteral extends org.rascalmpl.ast.LocationLiteral {


public LocationLiteral (org.eclipse.imp.pdb.facts.INode __param1) {
	super(__param1);
}
static public class Default extends org.rascalmpl.ast.LocationLiteral.Default {


public Default (org.eclipse.imp.pdb.facts.INode __param1,org.rascalmpl.ast.ProtocolPart __param2,org.rascalmpl.ast.PathPart __param3) {
	super(__param1,__param2,__param3);
}
@Override
public org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> interpret(org.rascalmpl.interpreter.Evaluator __eval) {
	
		org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> protocolPart = this.getProtocolPart().interpret(__eval);
		org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> pathPart = this.getPathPart().interpret(__eval);

		java.lang.String uri = ((org.eclipse.imp.pdb.facts.IString) protocolPart.getValue()).getValue() + "://" + ((org.eclipse.imp.pdb.facts.IString) pathPart.getValue()).getValue();

		try {
			java.net.URI url = new java.net.URI(uri);
			org.eclipse.imp.pdb.facts.ISourceLocation r = __eval.__getVf().sourceLocation(url);
			return org.rascalmpl.interpreter.result.ResultFactory.makeResult(org.rascalmpl.interpreter.Evaluator.__getTf().sourceLocationType(), r, __eval);
		} catch (java.net.URISyntaxException e) {
			throw org.rascalmpl.interpreter.utils.RuntimeExceptionFactory.malformedURI(uri, this, __eval.getStackTrace());
		}
	
}

@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

}
static public class Ambiguity extends org.rascalmpl.ast.LocationLiteral.Ambiguity {


public Ambiguity (org.eclipse.imp.pdb.facts.INode __param1,java.util.List<org.rascalmpl.ast.LocationLiteral> __param2) {
	super(__param1,__param2);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

}
}