package org.rascalmpl.interpreter.load;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.rascalmpl.interpreter.Configuration;
import org.rascalmpl.interpreter.utils.Names;

public class SDFSearchPath {
	private List<ISdfSearchPathContributor> contributors = new ArrayList<ISdfSearchPathContributor>();
	
	public void addSdfSearchPathContributor(ISdfSearchPathContributor contrib){
		contributors.add(0, contrib);
	}
	
	public boolean isSdfModule(String name){
		for(String path : getSdfSearchPath()){
			if(new File(new File(path), getSdfFileName(name)).exists()){
			   return true;
			}
		}
		
		return false;
	}

	public List<String> getSdfSearchPath(){
		List<String> result = new ArrayList<String>();
		for (ISdfSearchPathContributor c : contributors){
			result.addAll(c.contributePaths());
		}
		return result;
	}

	
	private static String getSdfFileName(String moduleName) {
		String fileName = moduleName.replaceAll(Configuration.RASCAL_MODULE_SEP, Configuration.RASCAL_PATH_SEP) + Configuration.SDF_EXT;
		fileName = Names.unescape(fileName);
		return fileName;
	}
}
