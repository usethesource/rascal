package org.meta_environment.rascal.interpreter.result;

import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeStore;
import org.meta_environment.rascal.interpreter.exceptions.TypeErrorException;
import static org.meta_environment.rascal.interpreter.result.ResultFactory.makeResult;


public class SourceLocationResult extends AbstractResult<ISourceLocation> {

	protected SourceLocationResult(Type type, ISourceLocation loc) {
		super(type, loc);
	}

	@Override
	public <U extends IValue> AbstractResult<U> fieldAccess(String name, TypeStore store) {
		if (name.equals("length")) {
			return makeResult(getTypeFactory().integerType(), getValueFactory()
					.integer(getValue().getLength()));
		} 
		else if (name.equals("offset")) {
			return makeResult(getTypeFactory().integerType(), getValueFactory()
					.integer(getValue().getOffset()));
		} 
		else if (name.equals("beginLine")) {
			return makeResult(getTypeFactory().integerType(), getValueFactory()
					.integer(getValue().getBeginLine()));
		} 
		else if (name.equals("beginColumn")) {
			return makeResult(getTypeFactory().integerType(), getValueFactory()
					.integer(getValue().getBeginColumn()));
		} 
		else if (name.equals("endLine")) {
			return makeResult(getTypeFactory().integerType(), getValueFactory()
					.integer(getValue().getEndLine()));
		} 
		else if (name.equals("endColumn")) {
			return makeResult(getTypeFactory().integerType(), getValueFactory()
					.integer(getValue().getEndColumn()));
		} 
		else if (name.equals("url")) {
			return makeResult(getTypeFactory().stringType(), getValueFactory()
					.string(getValue().getURL().toString()));
		} 
		else {
			throw new TypeErrorException("Field `" + name
					+ "` not defined on loc", null);
		}
	}
	
}
