package org.rascalmpl.library.experiments.Compiler;

import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.ProfileLocationCollector;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.RascalExecutionContext;

public class ProfileCompiled extends Profile {
	
	private static ProfileLocationCollector profileCollector;
	
	public ProfileCompiled(IValueFactory values){
		super(values);
	}
	
	public void startProfile(RascalExecutionContext rex){
		if(profileCollector == null){
			profileCollector = new ProfileLocationCollector();
			profileCollector.start();
		} else {
			profileCollector.restart();
		}
		rex.getRVM().setLocationCollector(profileCollector);
	}
	
	public void stopProfile(RascalExecutionContext rex){
		profileCollector.stop();
		rex.getRVM().resetLocationCollector();
	}
	
	public IList getProfile(RascalExecutionContext rex){
		assert profileCollector != null: "startProfile not called before getProfile";
		IList res = ProfileCompiled.profileCollector.getData();
		rex.getRVM().resetLocationCollector();
		profileCollector = null;
		return res;
	}
	
	public void reportProfile(RascalExecutionContext rex){
		profileCollector.report(rex.getStdOut());
	}
	
	public void reportProfile(IList profileData, RascalExecutionContext rex){
		profileCollector.report(profileData, rex.getStdOut());
	}
}
