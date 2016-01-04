package org.rascalmpl.library.experiments.Compiler.Commands;

import java.io.IOException;
import java.io.StringReader;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.function.Function;

import org.rascalmpl.value.IBool;
import org.rascalmpl.value.IList;
import org.rascalmpl.value.IMap;
import org.rascalmpl.value.IMapWriter;
import org.rascalmpl.value.ISourceLocation;
import org.rascalmpl.value.IString;
import org.rascalmpl.value.IValue;
import org.rascalmpl.value.exceptions.FactTypeUseException;
import org.rascalmpl.value.io.StandardTextReader;
import org.rascalmpl.value.type.Type;
import org.rascalmpl.value.type.TypeFactory;
import org.rascalmpl.value.type.TypeStore;
import org.rascalmpl.values.ValueFactoryFactory;

/**
 * There two classes of options: for the command itself and for the
 * Rascal module when executed
 *
 */
enum OptionClass {COMMAND, MAIN};


/**
 * Option values can have the foloowing types;
 *
 */
enum OptionType {INT, STR, BOOL, PATH, LOC};

public class CommandOptions {

	protected TypeFactory tf;
	protected org.rascalmpl.value.IValueFactory vf;
	private boolean inCommandOptions = true;
	private boolean singleModule = true;
	
	private IList rascalModules;
	private String rascalModuleHelp;
	
	private Options commandOptions;
	private Options moduleOptions;
	
	private String commandName;
	
	CommandOptions(){
		tf = TypeFactory.getInstance();
		vf = ValueFactoryFactory.getValueFactory();
		commandOptions = new Options();
		moduleOptions = new Options();
		rascalModules = vf.list();
		rascalModuleHelp = "Rascal Module";
	}

	/**
	 * Convert a textual source locations that is either 
	 * - a slash-separated path, or
	 * - a Rascal source location enclosed between | and |.
	 * 
	 * @param loc	string representation of a location
	 * @return 		the loc converted to a source location value
	 */
	ISourceLocation convertLoc(String loc){
		if(loc.startsWith("|") && loc.endsWith("|")){
			TypeStore store = new TypeStore();
			Type start = TypeFactory.getInstance().sourceLocationType();

			try (StringReader in = new StringReader(loc)) {
				return (ISourceLocation) new StandardTextReader().read(vf, store, start, in);
			} 
			catch (FactTypeUseException e) {
				printUsageAndExit(e.getMessage());
			} 
			catch (IOException e) {
				printUsageAndExit(e.getMessage());
			}
		} else {
			return vf.sourceLocation(loc);
		}
		return null;
	}

	CommandOptions rascalModule(String helpText){
		singleModule = true;
		inCommandOptions = false;
		rascalModuleHelp = helpText;
		return this;
	}
	
	CommandOptions rascalModules(String helpText){
		singleModule = false;
		inCommandOptions = false;
		rascalModuleHelp = helpText;
		return this;
	}

	CommandOptions pathOption(String name, IList defaultValue, String helpText){
		(inCommandOptions ? commandOptions : moduleOptions).add(new Option(OptionType.PATH, name, defaultValue, helpText));
		return this;
	}
	
	CommandOptions pathOption(String name, Function<CommandOptions, IList> defaultValue, String helpText){
		(inCommandOptions ? commandOptions : moduleOptions).add(new Option(OptionType.PATH, name, defaultValue, helpText));
		return this;
	}

	IList getCommandPathOption(String name){
		return (IList) commandOptions.get(OptionType.PATH, name);
	}

	IList getMainPathOption(String name){
		return (IList) moduleOptions.get(OptionType.PATH, name);
	}

	CommandOptions dirOption(String name, ISourceLocation defaultValue, String helpText){
		(inCommandOptions ? commandOptions : moduleOptions).add(new Option(OptionType.LOC, name, defaultValue, helpText));
		return this;
	}
	
	CommandOptions dirOption(String name, Function<CommandOptions, ISourceLocation> defaultValue, String helpText){
		(inCommandOptions ? commandOptions : moduleOptions).add(new Option(OptionType.LOC, name, defaultValue, helpText));
		return this;
	}

	ISourceLocation getCommandDirOption(String name){
		return (ISourceLocation) commandOptions.get(OptionType.LOC, name);
	}

	ISourceLocation getMainDirOption(String name){
		return (ISourceLocation) moduleOptions.get(OptionType.LOC, name);
	}

	CommandOptions boolOption(String name, boolean defaultValue, String helpText){
		(inCommandOptions ? commandOptions : moduleOptions).add(new Option(OptionType.BOOL, name, vf.bool(defaultValue), helpText));
		return this;
	}
	
	CommandOptions boolOption(String name, Function<CommandOptions, Boolean> defaultValue, String helpText){
		(inCommandOptions ? commandOptions : moduleOptions).add(new Option(OptionType.BOOL, name, defaultValue, helpText));
		return this;
	}

	boolean getCommandBoolOption(String name){
		return ((IBool) commandOptions.get(OptionType.BOOL, name)).getValue();
	}

	boolean getMainBoolOption(String name){
		return ((IBool) moduleOptions.get(OptionType.BOOL, name)).getValue();
	}

	CommandOptions stringOption(String name, String defaultValue, String helpText){
		(inCommandOptions ? commandOptions : moduleOptions).add(new Option(OptionType.STR, name, vf.string(defaultValue), helpText));
		return this;
	}
	
	CommandOptions stringOption(String name, Function<CommandOptions, String> defaultValue, String helpText){
		(inCommandOptions ? commandOptions : moduleOptions).add(new Option(OptionType.STR, name, defaultValue, helpText));
		return this;
	}

	String getCommandStringOption(String name){
		return ((IString) commandOptions.get(OptionType.STR, name)).getValue();
	}

	String getMainStringOption(String name){
		return ((IString) moduleOptions.get(OptionType.STR, name)).getValue();
	}

	IString getRascalModule(){
		return (IString) rascalModules.get(0);
	}
	
	IList getRascalModules(){
		return rascalModules;
	}

	Map<String,IValue> getMainOptions(){
		HashMap<String, IValue> mainOptionsMap = new HashMap<>();
		for(Option option : moduleOptions){
			mainOptionsMap.put(option.name, option.currentValue);
		}
		return mainOptionsMap;
	}
	
	IMap getMainOptionsAsIMap(){
		IMapWriter w = vf.mapWriter();
		for(Option option : moduleOptions){
			w.put(vf.string(option.name), option.currentValue);
		}
		return w.done();
	}
	/**
	 * Print usage of the command
	 * 
	 * @param msg	optional error message
	 */
	
	private void printUsageAndExit(String msg){
		System.err.println(usage(msg));;
		System.exit(-1);
	}
	
	private String usage(String msg){
		StringBuffer w = new StringBuffer();
		if(!msg.isEmpty()){
			w.append(msg).append("\n");
		}
		
		String prefix = "Usage: " + commandName;
		String indent = new String(new char[prefix.length()]).replace('\0', ' ');
		w.append(prefix);
		int nopt = 0;
		for(Option option : commandOptions){
			nopt++;
			if(nopt == 10){
				nopt = 0;
				w.append("\n").append(indent);
			}
			w.append(option.help());
		}
		w.append("\n").append(indent).append(singleModule ? " <RascalModule>" : " <RascalModules>");
		nopt = 0;
		for(Option option : moduleOptions){
			nopt++;
			if(nopt == 10){
				nopt = 0;
				w.append("\n").append(indent);
			}
			w.append(option.help());
		}
		return w.toString();
	}
	
	private void help(){
		System.err.println(usage(""));
		System.err.println();
		for(Option option : commandOptions){
			System.err.printf("%20s  %s\n", option.help(), option.helpText);
		}

		System.err.printf("%20s  %s\n", singleModule ? " <RascalModule>" : " <RascalModules>", rascalModuleHelp);
		for(Option option : moduleOptions){
			System.err.printf("%20s  %s\n", option.help(), option.helpText);
		}
		System.exit(-1);
	}
	
	IBool requiredBool(String name){
		printUsageAndExit("Value required for " + name);
		return null;
	}
	
	IString requiredString(String name){
		printUsageAndExit("Value required for " + name);
		return null;
	}
	
	ISourceLocation requiredDir(String name){
		printUsageAndExit("Value required for " + name);
		return null;
	}
	
	IList requiredPath(String name){
		printUsageAndExit("Value required for " + name);
		return null;
	}

	public ISourceLocation getDefaultStdLocation(){
		try {
			return vf.sourceLocation("std", "", "");
		} catch (URISyntaxException e) {
			printUsageAndExit("Cannot create default locations: " + e.getMessage());
			return null;
		}
	}

	public IList getDefaultStdPath(){
		return vf.list(getDefaultStdLocation());
	}

	public ISourceLocation getDefaultKernelLocation(){
		try {
			return vf.sourceLocation("compressed+boot", "", "Kernel.rvm.ser.gz");
		} catch (URISyntaxException e) {
			printUsageAndExit("Cannot create default locations: " + e.getMessage());
			return null;
		}
	}

	public ISourceLocation getDefaultBootLocation(){
		try {
			return vf.sourceLocation("boot", "", "");
		} catch (URISyntaxException e) {
			printUsageAndExit("Cannot create default locations: " + e.getMessage());
			return null;
		}
	}

	private void checkDefaults(){
		for(Option option : commandOptions){
			option.checkDefault(this);
		}
		for(Option option : moduleOptions){
			option.checkDefault(this);
		}
	}
	
	private String getOptionValue(String[] args, int i){
		if(i >= args.length - 1 || args[i + 1].startsWith("--")){
			printUsageAndExit("Missing value for option " + args[i]);
			return "";
		}
		return args[i + 1];
	}

	/**
	 * Handle command line options 
	 * @param commandName of command that is being executed
	 * @param list of options and values
	 */
	protected void handleArgs(String name, String[] args){
		vf = ValueFactoryFactory.getValueFactory();
		commandName = name;
		boolean mainSeen = false;
		Options currentOptions;
		int i = 0;
		while(i < args.length){
			if(args[i].startsWith("--")){
				String option = args[i].substring(2, args[i].length());
				currentOptions = !mainSeen ? commandOptions : moduleOptions;

				if(currentOptions.provides(OptionType.BOOL, option)){
					currentOptions.set(OptionType.BOOL, option, vf.bool(true));
					i += 1;
				} else {
					if(currentOptions.provides(OptionType.STR, option)){
						currentOptions.set(OptionType.STR, option, vf.string(getOptionValue(args, i)));
					} else if(currentOptions.provides(OptionType.PATH, option)){
						ISourceLocation newLoc = convertLoc(getOptionValue(args, i));
						currentOptions.update(OptionType.PATH, option, (current) -> current == null ? vf.list(newLoc) : ((IList) current).append(newLoc));
					} else if(currentOptions.provides(OptionType.LOC, option)){
						currentOptions.set(OptionType.LOC, option, convertLoc(getOptionValue(args, i)));
					} else {
						printUsageAndExit("Unknown command option " + args[i]);
						return;
					}
					i += 2;
				}
			} else {
				rascalModules = rascalModules.append(vf.string(args[i]));
				mainSeen = true;
				i++;
			}
		}

		checkDefaults();
		
		if(getCommandBoolOption("help")){
			help();
			System.exit(0);
		}
		
		if(rascalModules.length() == 0){
			printUsageAndExit("Missing Rascal module" + (singleModule ? "" : "s"));
		} else if(rascalModules.length() > 1 && singleModule){
			printUsageAndExit("Duplicate modules defined: " + rascalModules);
		}
	}
}

class Option {
	OptionType optionType;
	String name;
	IValue currentValue;
	Object defaultValue;
	String helpText;
	
	Option(OptionType optionType, String name, Object defaultValue, String helpText){
		this.optionType = optionType;
		this.name = name;
		this.defaultValue = defaultValue;
		this.helpText = helpText;
	}
	
	public boolean set(OptionType optionType, String name, IValue newValue){
		if(this.optionType == optionType && this.name.equals(name)){
			if(currentValue == null){
				currentValue = newValue;
				return true;
			}
		}
		return false;
	}
	
	public boolean update(OptionType optionType, String name, Function<IValue, IValue> updater) {
		if(this.optionType == optionType && this.name.equals(name)){
			currentValue = updater.apply(currentValue);
			return true;
		}
		return false;
	}
	
	public boolean provides(OptionType optionType, String name){
		return this.optionType == optionType && this.name.equals(name);
	}
	
	public IValue get(OptionType optionType, String name) {
		if(this.optionType.equals(optionType) && this.name.equals(name)){
			if(currentValue != null){
				return currentValue;
			}
			throw new RuntimeException("Option " + name + " has undefined value");
		}
		return null;
	}
	
	public boolean checkDefault(CommandOptions commandOptions){
		if(currentValue == null){
			if(defaultValue == null){
				throw new RuntimeException("Option " + name + " requires a value");
			}
			// TODO: type check needed
			if(defaultValue instanceof Function<?,?>){
				currentValue = ((Function<CommandOptions,IValue>) defaultValue).apply(commandOptions);
			}  else {
			   currentValue = (IValue) defaultValue;
			}
		}
		return true;
	}
	
	String help(){
		String res = "--" + name;
		switch(optionType){
		case INT: res += " <int>"; break;
		case STR: res += " <str>"; break;
		case LOC: res += " <loc>"; break;
		case PATH: res += " <path>"; break;
		case BOOL:
			break;
		}
		return defaultValue == null ? " " + res : " [" + res + "]";
	}
}

class Options implements Iterable<Option>{
	ArrayList<Option> options = new ArrayList<>();
	
	Options add(Option option){
		options.add(option);
		return this;
	}

	public IValue get(OptionType optionType, String name) {
		for(Option option : options){
		    IValue v = option.get(optionType, name);
		    if(v != null){
		    	return v;
		    }
		}
		throw new RuntimeException("Option " + name + " has not been declared");
	}
	
	public boolean provides(OptionType optionType, String name){
		for(Option option : options){
			if(option.provides(optionType, name)){
				return true;
			}
		}
		return false;
	}
	
	public boolean set(OptionType optionType, String name, IValue newValue){
		for(Option option : options){
		    if(option.set(optionType, name, newValue)){
		    	return true;
		    }
		}
		throw new RuntimeException("Option " + name + " could not be set");
	}
	
	public void update(OptionType optionType, String name,  Function<IValue, IValue> updater){
		for(Option option : options){
		    if(option.update(optionType, name, updater)){
		    	return;
		    }
		}
		throw new RuntimeException("Option " + name + " could not be updated");
	}

	@Override
	public Iterator<Option> iterator() {
		return options.iterator();
	}
}