package org.rascalmpl.library.experiments.Compiler.Commands;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.rascalmpl.library.util.PathConfig;
import org.rascalmpl.uri.URIUtil;
import org.rascalmpl.value.IBool;
import org.rascalmpl.value.IList;
import org.rascalmpl.value.IMap;
import org.rascalmpl.value.IMapWriter;
import org.rascalmpl.value.ISourceLocation;
import org.rascalmpl.value.IString;
import org.rascalmpl.value.IValue;
import org.rascalmpl.value.IValueFactory;
import org.rascalmpl.value.exceptions.FactTypeUseException;
import org.rascalmpl.value.io.StandardTextReader;
import org.rascalmpl.value.type.Type;
import org.rascalmpl.value.type.TypeFactory;
import org.rascalmpl.value.type.TypeStore;
import org.rascalmpl.values.ValueFactoryFactory;

/**
 * Option values can have the foloowing types;
 *
 */
enum OptionType {INT, STR, BOOL, PATH, LOC};

/**
 * Create CommandOptions for a main program.
 * 
 * A command option is one of
 * <ul>
 * <li>boolean: --optionName
 * <li>string:  --optionName stringValue 
 * <li>loc:     --optionName sourceLocationValue
 * <li>path:    --optionName sourceLocationValue
 *                   sourceLocationValue are either (quoted) Rascal source locations or file path names
 *                   multiple path options of the same name accumulate to a list of source locations
 * </ul>
 * Command options define properties for the initialized and configuration of the command.
 * <p>
 * A moduleName is a qualified Rascal module name. A command can have 1 or more than one modules names.
 * <p>
 * A command looks like this:
 * <p>
 * commandName commandOptions* moduleName+ moduleOptions*
 * <p>
 * A module option looks the same as a command option but is associated with the module(s) given in the command.
 * CommandOptions provides a fluent interface to create options of the above type, e.g. intOption, boolOption, etc.
 * Each option is created by a sequence starting with the option creation and ending with the associated help message for that option.
 * In between, defaults can be set and omitted defaults have sensible values.
 * <p>
 *<code>
 * CommandOptions opts = new CommandOptions("commandName");
 * <p>
 * opts.intOption("X").intDefault(42).help("X is a very good option")
 * <p>
 *     .boolOption("Y).help("and Y too!")
 *     <p>
 *     .rascalModule("Module to analyze")
 *     <p>
 *     .handleArgs(args); // the string arguments coming from the command line
 * </code>
 */
public class CommandOptions {

	protected TypeFactory tf;
	protected org.rascalmpl.value.IValueFactory vf;
	private boolean inCommandOptions = true;
	private boolean singleModule = true;
	
	private IList rascalModules;
	private String rascalModuleHelp;
	
	protected Options commandOptions;
	private Options moduleOptions;
	
	private String commandName;
	
	public CommandOptions(String commandName){
		this.commandName = commandName;
		tf = TypeFactory.getInstance();
		vf = ValueFactoryFactory.getValueFactory();
		commandOptions = new Options();
		moduleOptions = new Options();
		rascalModules = vf.list();
		rascalModuleHelp = "Rascal Module";
	}
	
	public CommandOptions addOption(Option option){
		(inCommandOptions ? commandOptions : moduleOptions).add(option);
		return this;
	}
	
	/****************************************************************************/
	/*			Bool options													*/
	/****************************************************************************/
	
	/**
	 * Declare a bool option (a single boolean value)
	 * @param name of option
	 * @return OptionBuilder
	 */
	public OptionBuilder boolOption(String name){
		return new OptionBuilder(this, OptionType.BOOL, name);
	}

	/**
	 * Get the value of a bool option from the command options
	 * @param name
	 * @return value of option
	 */
	public boolean getCommandBoolOption(String name){
		return ((IBool) commandOptions.get(OptionType.BOOL, name)).getValue();
	}

	/**
	 * Get the value of a bool option from the module options
	 * @param name
	 * @return value of option
	 */
	public boolean getModuleBoolOption(String name){
		return ((IBool) moduleOptions.get(OptionType.BOOL, name)).getValue();
	}
	
	/****************************************************************************/
	/*			String options													*/
	/****************************************************************************/

	/**
	 * Declare a string option (a single string value)
	 * @param name of option
	 * @return OptionBuilder
	 */
	public OptionBuilder strOption(String name){
		return new OptionBuilder(this, OptionType.STR, name);
	}

	/**
	 * Get the value of a string option from the command options
	 * @param name
	 * @return value of option
	 */
	public String getCommandStringOption(String name){
		return ((IString) commandOptions.get(OptionType.STR, name)).getValue();
	}

	/**
	 * Get the value of a string option from the module options
	 * @param name
	 * @return value of option
	 */
	public String getModuleStringOption(String name){
		return ((IString) moduleOptions.get(OptionType.STR, name)).getValue();
	}
	
	/****************************************************************************/
	/*			Loc options														*/
	/****************************************************************************/

	/**
	 * Declare a loc option (a single location)
	 * @param name of option
	 * @return OptionBuilder
	 */
	public OptionBuilder locOption(String name){
		return new OptionBuilder(this, OptionType.LOC, name);
	}

	/**
	 * Get the value of a loc option from the command options
	 * @param name
	 * @return value of option
	 */
	public ISourceLocation getCommandLocOption(String name){
		return (ISourceLocation) commandOptions.get(OptionType.LOC, name);
	}

	/**
	 * Get the value of a loc option from the module options
	 * @param name
	 * @return value of option
	 */
	public ISourceLocation getModuleLocOption(String name){
		return (ISourceLocation) moduleOptions.get(OptionType.LOC, name);
	}
	
	/****************************************************************************/
	/*			Path options													*/
	/****************************************************************************/

	/**
	 * Declare a path option (a list of locations)
	 * @param name of option
	 * @return OptionBuilder
	 */
	public OptionBuilder pathOption(String name){
		return new OptionBuilder(this, OptionType.PATH, name);
	}

	/**
	 * Get the value of a path option from the command options
	 * @param name
	 * @return value of option
	 */
	public IList getCommandPathOption(String name){
		return (IList) commandOptions.get(OptionType.PATH, name);
	}

	/**
	 * Get the value of a path option from the module options
	 * @param name
	 * @return value of option
	 */
	public IList getModulePathOption(String name){
		return (IList) moduleOptions.get(OptionType.PATH, name);
	}

	/****************************************************************************/
	/*			Module argument(s)												*/
	/****************************************************************************/
	
	/**
	 * Command has one Rascal Module as argument
	 * @param helpText describes the role of the single module argument
	 * @return this CommandOptions
	 */
	public CommandOptions rascalModule(String helpText){
		singleModule = true;
		inCommandOptions = false;
		rascalModuleHelp = helpText;
		return this;
	}
	
	/**
	 * Get the name of the single Rascal module argument of the command
	 * @return module name
	 */
	public IString getRascalModule(){
		return (IString) rascalModules.get(0);
	}
	
	/**
	 * Command has one or more Rascal Modules as argument
	 * @param helpText describes the role of the one or more module arguments
	 * @return this CommandOptions
	 */
	public CommandOptions rascalModules(String helpText){
		singleModule = false;
		inCommandOptions = false;
		rascalModuleHelp = helpText;
		return this;
	}

	/**
	 * Get the names of the Rascal module arguments of the command
	 * @return list of module names
	 */
	IList getRascalModules(){
		return rascalModules;
	}
	
	/****************************************************************************/
	/*			Processing of all command line arguments						*/
	/****************************************************************************/
	
	private String getOptionValue(String[] args, int i){
		if(i >= args.length - 1 || args[i + 1].startsWith("--")){
			printUsageAndExit("Missing value for option " + args[i]);
			return "";
		}
		return args[i + 1];
	}

	/**
	 * Handle command line options and create help and usage info
	 * @param args a list of options and their values
	 */
	public void handleArgs(String[] args){
		vf = ValueFactoryFactory.getValueFactory();
		boolean mainSeen = false;
		Options currentOptions;
		int i = 0;
		while(i < args.length){
			if(args[i].startsWith("--")){
				String option = args[i].substring(2, args[i].length());
				currentOptions = !mainSeen ? commandOptions : moduleOptions;

				if(currentOptions.contains(OptionType.BOOL, option)){
					currentOptions.set(OptionType.BOOL, option, vf.bool(true));
					i += 1;
				} else {
					if(currentOptions.contains(OptionType.STR, option)){
						currentOptions.set(OptionType.STR, option, vf.string(getOptionValue(args, i)));
					} else if(currentOptions.contains(OptionType.PATH, option)){
						ISourceLocation newLoc = convertLoc(getOptionValue(args, i));
						currentOptions.update(OptionType.PATH, option, (current) -> current == null ? vf.list(newLoc) : ((IList) current).append(newLoc));
					} else if(currentOptions.contains(OptionType.LOC, option)){
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
		
		String ndHelp = noDefaultsHelpText();

		if(commandOptions.contains(OptionType.BOOL, "noDefaults")){
			if(!ndHelp.isEmpty()){
				for(Option option : commandOptions){
					if(option.name.equals("noDefaults")){
						option.helpText = ndHelp;
					}
				}
			}
			
		} else {
			if(!ndHelp.isEmpty()){
				commandOptions.add(new Option(OptionType.BOOL, "noDefaults", vf.bool(false), null, false, ndHelp));
			}
		}
		
		if(commandOptions.hasNonDefaultValue(OptionType.BOOL, "help")){
			help();
			System.exit(0);
		}
		checkDefaults();
		
		if(rascalModules.length() == 0){
			printUsageAndExit("Missing Rascal module" + (singleModule ? "" : "s"));
		} else if(rascalModules.length() > 1 && singleModule){
			printUsageAndExit("Duplicate modules defined: " + rascalModules);
		}
	}
	
	/****************************************************************************/
	/*			(Utilities for) consistency checking							*/
	/****************************************************************************/

	private void checkDefaults(){
		for(Option option : commandOptions){
			option.checkDefault(this);
		}
		for(Option option : moduleOptions){
			option.checkDefault(this);
		}
	}
	
	/****************************************************************************/
	/*			Usage and help generation										*/
	/****************************************************************************/
	
	/**
	 * Print usage of the command and exit
	 * 
	 * @param msg	error message
	 */
	
	private void printUsageAndExit(String msg){
		System.err.println(usage(msg));;
		System.exit(-1);
	}
	
	/**
	 * @param msg	error message
	 * @return the usage string for the command
	 */
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
	
	private String noDefaultsHelpText(){
		List<String> respectNoDefaults = commandOptions.getAllRespectNoDefaults();
		respectNoDefaults.addAll(moduleOptions.getAllRespectNoDefaults());
		if(respectNoDefaults.isEmpty()){
			return "";
		}
		StringWriter w = new StringWriter();
		w.append("Forbid use of default values for");
		String sep = " ";
		for(String name : respectNoDefaults){
			w.append(sep).append(name);
			sep = ", ";
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
	
	/****************************************************************************/
	/*			Convenience methods												*/
	/****************************************************************************/
	
	/**
	 * @return all module options as a Java Map
	 */
	public Map<String,IValue> getModuleOptions(){
		HashMap<String, IValue> mainOptionsMap = new HashMap<>();
		for(Option option : moduleOptions){
			mainOptionsMap.put(option.name, option.currentValue);
		}
		return mainOptionsMap;
	}
	
	/**
	 * @return all module options as an IMap
	 */
	public IMap getModuleOptionsAsIMap(){
		IMapWriter w = vf.mapWriter();
		for(Option option : moduleOptions){
			w.put(vf.string(option.name), option.currentValue);
		}
		return w.done();
	}
	
	/**
	 * Convert a textual source locations that is either:
	 * - a slash-separated path (absolute or relative)
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
			return URIUtil.correctLocation(loc.startsWith("/") ? "file" : "cwd", "", loc);
		}
		return null;
	}

	public ISourceLocation getDefaultStdLocation(){
		try {
			return vf.sourceLocation("std", "", "");
		} catch (URISyntaxException e) {
			printUsageAndExit("Cannot create default location: " + e.getMessage());
			return null;
		}
	}

	public IList getDefaultStdPath(){
		return vf.list(getDefaultStdLocation());
	}

	public ISourceLocation getKernelLocation(){
		try {
			ISourceLocation bootDir = getCommandLocOption("bootDir");
			return vf.sourceLocation("compressed+" + bootDir.getScheme(), "", bootDir.getPath() + "Kernel.rvm.ser.gz");
		} catch (URISyntaxException e) {
			printUsageAndExit("Cannot create default location: " + e.getMessage());
			return null;
		}
	}

	public ISourceLocation getDefaultBootLocation(){
		try {
			return vf.sourceLocation("boot", "", "");
		} catch (URISyntaxException e) {
			printUsageAndExit("Cannot create default location: " + e.getMessage());
			return null;
		}
	}
	
	public PathConfig getPathConfig(){
		return new PathConfig(getCommandPathOption("srcPath"),
							  getCommandPathOption("libPath"),
							  getCommandLocOption("binDir"),
							  getCommandLocOption("bootDir"));
	}

}

class  OptionBuilder {
	CommandOptions commandOptions;
	IValueFactory vf;
	OptionType optionType;
	String name;
	private IValue initialValue;
	private Object defaultValue;
	private boolean mayForceNoDefault;
	
	OptionBuilder(CommandOptions commandOptions, OptionType optionType, String name){
		this.commandOptions = commandOptions;
		this.vf = commandOptions.vf;
		this.optionType = optionType;
		this.name = name;
		switch(optionType){
		case INT: initialValue = vf.integer(0) ; break;
		case STR: initialValue = vf.string(""); break;
		case LOC: initialValue = null; break;
		case PATH:initialValue = vf.list(); break;
		case BOOL:initialValue = vf.bool(false);
		}
	}
	
	void check(OptionType ot){
		if(optionType != ot){
			throw new RuntimeException("Default value required of type " + ot.toString().toLowerCase());
		}
	}
	
	/**
	 * @param defaultValue for a boolean option
	 * @return this OptionBuilder
	 */
	OptionBuilder boolDefault(boolean defaultValue){
		check(OptionType.BOOL);
		this.defaultValue = commandOptions.vf.bool(defaultValue);
		return this;
	}
	
	/**
	 * @param defaultValue for a boolean option as a function that returns a bool value
	 * @return this OptionBuilder
	 */
	OptionBuilder boolDefault(Function<CommandOptions, Boolean> defaultValue){
		check(OptionType.BOOL);
		this.defaultValue = defaultValue;
		return this;
	}
	
	/**
	 * @param defaultValue for an int option
	 * @return this OptionBuilder
	 */
	OptionBuilder intDefault(int defaultValue){
		check(OptionType.INT);
		this.defaultValue = commandOptions.vf.integer(defaultValue);
		return this;
	}
	
	/**
	 * @param defaultValue for an int option as a function that returns an int value
	 * @return this OptionBuilder
	 */
	OptionBuilder intDefault(Function<CommandOptions, Integer> defaultValue){
		check(OptionType.INT);
		this.defaultValue = defaultValue;
		return this;
	}
	
	/**
	 * @param defaultValue for a str option
	 * @return this OptionBuilder
	 */
	OptionBuilder strDefault(String defaultValue){
		check(OptionType.STR);
		this.defaultValue = commandOptions.vf.string(defaultValue);
		return this;
	}
	
	/**
	 * @param defaultValue for a str option as a function that returns a str value
	 * @return this OptionBuilder
	 */
	OptionBuilder strDefault(Function<CommandOptions, String> defaultValue){
		check(OptionType.STR);
		this.defaultValue = defaultValue;
		return this;
	}
	
	/**
	 * @param defaultValue for a loc option
	 * @return this OptionBuilder
	 */
	OptionBuilder locDefault(ISourceLocation defaultValue){
		check(OptionType.LOC);
		this.defaultValue = defaultValue;
		return this;
	}
	
	/**
	 * @param defaultValue for a loc option as a function that returns a loc value
	 * @return this OptionBuilder
	 */
	OptionBuilder locDefault(Function<CommandOptions, ISourceLocation> defaultValue){
		check(OptionType.LOC);
		this.defaultValue = defaultValue;
		return this;
	}
	
	/**
	 * @param defaultValue for a path option
	 * @return this OptionBuilder
	 */
	OptionBuilder pathDefault(IList defaultValue){
		check(OptionType.PATH);
		this.defaultValue = defaultValue;
		return this;
	}
	
	/**
	 * @param defaultValue for a path option as a function that returns a list of locs
	 * @return this OptionBuilder
	 */
	OptionBuilder pathDefault(Function<CommandOptions, IList> defaultValue){
		check(OptionType.PATH);
		this.defaultValue = defaultValue;
		return this;
	}
	
	/**
	 * If "noDefaults" is set, the value of this option may not depend on its default but should be explicitly set
	 * @return this OptionBuilder
	 */
	OptionBuilder respectNoDefaults(){
		this.mayForceNoDefault = true;
		return this;
	}
	
	/**
	 * 
	 * @param helpText defines help text for the current option and ends its definition
	 * @return CommandOptions and can therefore be used in the fluent interface of CommandOptions
	 */
	CommandOptions help(String helpText){
		return commandOptions.addOption(new Option(optionType, name, initialValue, defaultValue, mayForceNoDefault, helpText));
	}
}

class Option {
	final OptionType optionType;
	final String name;
	IValue initialValue;
	IValue currentValue;
	final Object defaultValue;
	final boolean respectsNoDefaults;
	String helpText;
	
	Option(OptionType optionType, String name, IValue initialValue, Object defaultValue, boolean respectsNoDefaults, String helpText){
		this.optionType = optionType;
		this.name = name;
		this.initialValue = initialValue;
		this.currentValue = initialValue;
		this.defaultValue = defaultValue;
		this.respectsNoDefaults = respectsNoDefaults;
		this.helpText = helpText;
	}
	
	public boolean set(OptionType optionType, String name, IValue newValue){
		if(this.optionType == optionType && this.name.equals(name)){
			if(currentValue == initialValue){
				currentValue = newValue;
				return true;
			}
		}
		return false;
	}
	
	public boolean update(OptionType optionType, String name, Function<IValue, IValue> updater) {
		if(this.optionType == optionType && this.name.equals(name)){
			if(currentValue == null){
				currentValue = initialValue;
			}
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
	
	@SuppressWarnings("unchecked")
	public boolean checkDefault(CommandOptions commandOptions){
		boolean noDefaults = commandOptions.commandOptions.contains(OptionType.BOOL, "noDefaults") && commandOptions.getCommandBoolOption("noDefaults");
		if(currentValue == initialValue){
			if(noDefaults && respectsNoDefaults){
				throw new RuntimeException("Option " + name + " requires a value");
			}
			if(defaultValue != null){
				// type check has been done at creation
				if(defaultValue instanceof Function<?,?>){
					currentValue = ((Function<CommandOptions,IValue>) defaultValue).apply(commandOptions);
				}  else {
					currentValue = (IValue) defaultValue;
				}
			}
		}
		if(currentValue == null){
			throw new RuntimeException("Option " + name + " requires a value");
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
	
	public boolean hasNonDefaultValue(OptionType optionType, String name) {
		for(Option option : options){
			if(option.provides(optionType, name)){
				return option.currentValue != option.initialValue;
			}
		}
		return false;
	}
	
	public boolean contains(OptionType optionType, String name){
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
	
	public List<String> getAllRespectNoDefaults(){
		List<String> result = new ArrayList<>();
		for(Option option : options){
			if(option.respectsNoDefaults){
				result.add(option.name);
			}
		}
		return result;
	}

	@Override
	public Iterator<Option> iterator() {
		return options.iterator();
	}
}