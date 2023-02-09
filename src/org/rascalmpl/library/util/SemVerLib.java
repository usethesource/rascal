package org.rascalmpl.library.util;

import org.rascalmpl.exceptions.RuntimeExceptionFactory;
import org.rascalmpl.util.VersionInfo;

import io.usethesource.vallang.IString;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.IValueFactory;

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
			throw RuntimeExceptionFactory.illegalArgument(version);
		}
	}
	
	public IValue satisfiesVersion(IString version, IString rangeSet){
		try {
			return vf.bool(makeSemVer(version).satisfiesVersion(rangeSet.getValue()));
		} catch(Exception e){
			throw RuntimeExceptionFactory.illegalArgument(rangeSet);
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
	
	public IValue getRascalVersion(){
		return vf.string(VersionInfo.RASCAL_VERSION);
	}
	
	public IValue getRascalRuntimeVersion(){
		return vf.string(VersionInfo.RASCAL_RUNTIME_VERSION);
	}
	
	public IValue getRascalCompilerVersion(){
		return vf.string(VersionInfo.RASCAL_COMPILER_VERSION);
	}

	public IValue getJavaRuntimeVersion() {
		return vf.string(Runtime.version().toString());
	}
}
