package org.rascalmpl.library.util;

import org.rascalmpl.interpreter.utils.RuntimeExceptionFactory;
import org.rascalmpl.value.IString;
import org.rascalmpl.value.IValue;
import org.rascalmpl.value.IValueFactory;

/**
 * Rascal library wrapper for Semantic Versioning (SemVer) library
 *
 */
public class SemVerLib {
	private final IValueFactory vf;

	public SemVerLib(IValueFactory values){
		super();
		this.vf = values;
	}
	
	private SemVer makeSemVer(IString version){
		try {
			return new SemVer(version.getValue());
		} catch(Exception e){
			throw RuntimeExceptionFactory.illegalArgument(version, null, null);
		}
	}
	
	public IValue satisfiesVersion(IString version, IString rangeSet){
		try {
			return vf.bool(makeSemVer(version).satisfiesVersion(rangeSet.getValue()));
		} catch(Exception e){
			throw RuntimeExceptionFactory.illegalArgument(rangeSet, null, null);
		}
	}

	public IValue lessVersion(IString version1, IString version2){
		return vf.bool(makeSemVer(version1).lessVersion(makeSemVer(version2)));
	}
	
	public IValue lessEqualVersion(IString version1, IString version2){
		return vf.bool(makeSemVer(version1).lessEqualVersion(makeSemVer(version2)));
	}
	
	public IValue greaterVersion(IString version1, IString version2){
		return vf.bool(makeSemVer(version1).greaterVersion(makeSemVer(version2)));
	}
	
	public IValue greaterEqualVersion(IString version1, IString version2){
		return vf.bool(makeSemVer(version1).greaterEqualVersion(makeSemVer(version2)));
	}
	
	public IValue equalVersion(IString version1, IString version2){
		return vf.bool(makeSemVer(version1).equalVersion(makeSemVer(version2)));
	}
}
