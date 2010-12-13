package org.rascalmpl.semantics.dynamic;

import java.lang.String;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import org.eclipse.imp.pdb.facts.INode;
import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.IValue;
import org.rascalmpl.ast.NullASTVisitor;
import org.rascalmpl.ast.PathPart;
import org.rascalmpl.ast.ProtocolPart;
import org.rascalmpl.interpreter.Evaluator;
import org.rascalmpl.interpreter.result.Result;

public abstract class LocationLiteral extends org.rascalmpl.ast.LocationLiteral {

	public LocationLiteral(INode __param1) {
		super(__param1);
	}

	static public class Default extends org.rascalmpl.ast.LocationLiteral.Default {

		public Default(INode __param1, ProtocolPart __param2, PathPart __param3) {
			super(__param1, __param2, __param3);
		}

		@Override
		public Result<IValue> __evaluate(Evaluator __eval) {

			Result<IValue> protocolPart = this.getProtocolPart().__evaluate(__eval);
			Result<IValue> pathPart = this.getPathPart().__evaluate(__eval);

			String uri = ((IString) protocolPart.getValue()).getValue() + "://" + ((IString) pathPart.getValue()).getValue();

			try {
				URI url = new URI(uri);
				ISourceLocation r = __eval.__getVf().sourceLocation(url);
				return org.rascalmpl.interpreter.result.ResultFactory.makeResult(org.rascalmpl.interpreter.Evaluator.__getTf().sourceLocationType(), r, __eval);
			} catch (URISyntaxException e) {
				throw org.rascalmpl.interpreter.utils.RuntimeExceptionFactory.malformedURI(uri, this, __eval.getStackTrace());
			}

		}

		@Override
		public <T> T __evaluate(NullASTVisitor<T> __eval) {
			return null;
		}

	}

	static public class Ambiguity extends org.rascalmpl.ast.LocationLiteral.Ambiguity {

		public Ambiguity(INode __param1, List<org.rascalmpl.ast.LocationLiteral> __param2) {
			super(__param1, __param2);
		}

		@Override
		public <T> T __evaluate(NullASTVisitor<T> __eval) {
			return null;
		}

	}
}