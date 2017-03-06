package org.rascalmpl.library.experiments.Compiler;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.observers.ProfileFrameObserver;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.RascalExecutionContext;
import io.usethesource.vallang.IList;
import io.usethesource.vallang.IValueFactory;

public class ProfileCompiled extends Profile {
	
	private ProfileFrameObserver profileCollector;
	
	public ProfileCompiled(IValueFactory values){
		super(values);
	}
	
	public void startProfile(RascalExecutionContext rex){
		if(profileCollector == null){
			profileCollector = new ProfileFrameObserver(null);
		}
		profileCollector.startObserving();
	}
	
	public void stopProfile(RascalExecutionContext rex){
		profileCollector.stopObserving();
	}
	
	public IList getProfile(RascalExecutionContext rex){
		assert profileCollector != null: "startProfile not called before getProfile";
		IList res = profileCollector.getData();
		profileCollector.stopObserving();
		return res;
	}
	
	public void reportProfile(RascalExecutionContext rex){
		profileCollector.report();
	}
	
	public void reportProfile(IList profileData, RascalExecutionContext rex){
		profileCollector.report(profileData);
	}
}
