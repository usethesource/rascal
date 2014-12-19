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
		}
		rex.getRVM().setLocationCollector(profileCollector);
		//System.err.println("startCoverage");
	}
	
	public void stopProfile(RascalExecutionContext rex){
		rex.getRVM().resetLocationCollector();
	}
	
	public IList getProfile(RascalExecutionContext rex){
		//System.err.println("getProfile");
		assert profileCollector != null: "startProfile not called before getProfile";
		IList res = ProfileCompiled.profileCollector.getData();
		rex.getRVM().resetLocationCollector();
		//System.err.println("getProfile, returns " + res);
		profileCollector = null;
		return res;
	}
	
//	public void printProfile(RascalExecutionContext rex){
//		System.err.println("printProfile");
//		profileCollector.printData(rex.getStdOut());
//		rex.getRVM().resetLocationCollector();
//	}
}
