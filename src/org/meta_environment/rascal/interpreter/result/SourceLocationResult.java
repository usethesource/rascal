package org.meta_environment.rascal.interpreter.result;

import static org.meta_environment.rascal.interpreter.result.ResultFactory.makeResult;

import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeStore;
import org.meta_environment.rascal.interpreter.exceptions.NoSuchFieldException;


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
			throw new NoSuchFieldException("Field `" + name
					+ "` not defined on locations", null);
		}
	}
	
	@Override
	public <U extends IValue, V extends IValue> AbstractResult<U> compare(AbstractResult<V> result) {
		return result.compareSourceLocation(this);
	}
	
	/////
	
	@Override
	protected <U extends IValue> AbstractResult<U> compareSourceLocation(SourceLocationResult that) {
		// Note reverse of args
		ISourceLocation left = that.getValue();
		ISourceLocation right = this.getValue();
		if (left.isEqual(right)) {
			return makeIntegerResult(0);
		}
		int compare = left.getURL().toString().compareTo(right.getURL().toString());
		if (compare != 0) {
			return makeIntegerResult(compare);
		}
		int lBeginLine = left.getBeginLine();
		int rBeginLine = right.getBeginLine();

		int lEndLine = left.getEndLine();
		int rEndLine = right.getEndLine();

		int lBeginColumn = left.getBeginColumn();
		int rBeginColumn = right.getBeginColumn();

		int lEndColumn = left.getEndColumn();
		int rEndColumn = right.getEndColumn();
			
		if ((lBeginLine > rBeginLine || (lBeginLine == rBeginLine && lBeginColumn > rBeginColumn)) &&
				(lEndLine < rEndLine || ((lEndLine == rEndLine) && lEndColumn < rEndColumn))) {
			return makeIntegerResult(-1);
		} 
		return makeIntegerResult(1);
	}
	

	
}
