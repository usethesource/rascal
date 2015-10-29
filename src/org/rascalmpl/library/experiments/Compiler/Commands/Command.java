package org.rascalmpl.library.experiments.Compiler.Commands;

import java.io.IOException;
import java.io.StringReader;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import org.rascalmpl.value.type.Type;
import org.rascalmpl.value.IList;
import org.rascalmpl.value.IListWriter;
import org.rascalmpl.value.ISourceLocation;
import org.rascalmpl.value.IString;
import org.rascalmpl.value.IValue;
import org.rascalmpl.value.exceptions.FactTypeUseException;
import org.rascalmpl.value.io.StandardTextReader;
import org.rascalmpl.value.type.TypeFactory;
import org.rascalmpl.value.type.TypeStore;
import org.rascalmpl.values.ValueFactoryFactory;

public class Command {

	protected static org.rascalmpl.value.IValueFactory vf;
	protected static IString mainModule;		// A Rascal qualified name of the A::B::C".
	protected static IValue[] commandArgs;
	protected static Map<String,IValue> commandKwArgs;
	protected static ISourceLocation kernelBinaryLocation;
	
	private static String commandName;

	/**
	 * Convert a textual source locations that is either 
	 * - a slash-separated path, or
	 * - a Rascal source location enclosed between | and |.
	 * 
	 * @param loc	string representation of a location
	 * @return 		the loc converted to a source location value
	 */
	static ISourceLocation convertLoc(String loc){
		if(loc.startsWith("|") && loc.endsWith("|")){
			TypeStore store = new TypeStore();
			Type start = TypeFactory.getInstance().sourceLocationType();
			
			try (StringReader in = new StringReader(loc)) {
				return (ISourceLocation) new StandardTextReader().read(vf, store, start, in);
			} 
			catch (FactTypeUseException e) {
				usage(e.getMessage());
				return null;
			} 
			catch (IOException e) {
				usage(e.getMessage());
				return null;
			}
		} else {
			return vf.sourceLocation(loc);
		}
	}

	/**
	 * Convert a list of colon-separated textual source locations to a list of source location values
	 * 
	 * @param path	a colon (:) separated list of textual source locations
	 * @return		a list of source location values
	 */
	static IList convertPath(String path){
		IListWriter w = vf.listWriter();
		int begin = 0;
		while (begin >= 0) {
			if(path.startsWith("|")){
				int end = path.indexOf("|", begin + 1);
				if(end > 0){
					w.append(convertLoc(path.substring(begin, end + 1)));
					begin = end + 2;
				} else {
					usage("Malformed location in " + path);
					System.exit(-1);
				}
				if(begin >= path.length()){
					begin = -1;
				}
			} else {
				int end = path.indexOf(":", begin);
				if(end > 0){
					w.append(convertLoc(path.substring(begin, end)));
					begin = end + 1;
				} else {
					w.append(convertLoc(path.substring(begin, path.length())));
					begin = -1;
				}
			}

		};
		return w.done();
	}
	
	/**
	 * Print usage of the command
	 * 
	 * @param msg	optional error message
	 */
	static void usage(String msg){
		if(!msg.isEmpty()){
			System.err.println(msg);
		}
		System.err.println("Usage: " + commandName + " -srcPath <path> -libPath <path> -binDir <dir> <mainModule>");
		System.exit(-1);
	}
	
	static void setDefaults(){
		ISourceLocation stdLoc;
		ISourceLocation homeLoc;
		commandKwArgs = new HashMap<>();
		try {
			stdLoc = vf.sourceLocation("std", "", "");
			homeLoc = vf.sourceLocation("home", "", "bin");
			kernelBinaryLocation = vf.sourceLocation("compressed+boot", "", "Kernel.rvm.ser.gz");
		} catch (URISyntaxException e) {
			System.err.println("setDefaults: " + e.getMessage());
			usage("Cannot create default locations: " + e.getMessage());
			return;
		}
		commandKwArgs.put("srcPath", vf.list(stdLoc));
		commandKwArgs.put("libPath", vf.list(stdLoc));
		commandKwArgs.put("binDir", homeLoc);
	}
	
	/**
	 * Handle command line arguments 
	 * @param commandName of command that is being executed
	 * @param args
	 */
	protected static void handleArgs(String name, String[] args){
		vf = ValueFactoryFactory.getValueFactory();
		commandName = name;
		setDefaults();
		int i = 0;
		while(i < args.length){
			if(args[i].startsWith("-")){
				if(i >= args.length - 1 || args[i + 1].startsWith("-")){
					usage("Missing value for option " + args[i]);
					return;
				}
				switch(args[i]){
				case "-srcPath":
					commandKwArgs.put("srcPath", convertPath(args[i + 1])); i += 2;	break;
				case "-libPath":
					commandKwArgs.put("libPath", convertPath(args[i + 1]));	i += 2;	break;
				case "-binDir":
					commandKwArgs.put("binDir", convertLoc(args[i + 1])); i += 2; break;
				default:
					usage("Unknown option " + args[i]);
					return;
				}
			} else {
				if(mainModule != null){
					usage("Duplicate definition of main Module: " + args[i]);
					return;
				}
				mainModule = vf.string(args[i]);
				i++;
			}
		}
		if(mainModule == null){
			usage("Missing definition of main Module");
		}
		System.out.println("srcPath = " + commandKwArgs.get("srcPath"));
		System.out.println("libPath = " + commandKwArgs.get("libPath"));
		System.out.println("binDir = " + commandKwArgs.get("binDir"));
		System.out.println("mainModule = " + mainModule);
		
		commandArgs = new IValue[] {mainModule};
	}
	
}
