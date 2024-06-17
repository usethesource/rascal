package org.rascalmpl.shell;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.rascalmpl.library.util.PathConfig;
import org.rascalmpl.uri.URIUtil;
import org.rascalmpl.values.RascalValueFactory;
import org.rascalmpl.values.ValueFactoryFactory;

import io.usethesource.vallang.IBool;
import io.usethesource.vallang.IList;
import io.usethesource.vallang.ISourceLocation;
import io.usethesource.vallang.IString;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.exceptions.FactTypeUseException;
import io.usethesource.vallang.io.StandardTextReader;
import io.usethesource.vallang.type.Type;
import io.usethesource.vallang.type.TypeFactory;
import io.usethesource.vallang.type.TypeStore;

/**
 * Option values can have the following types;
 *
 */
enum OptionType {INT, STR, BOOL, LOCS, LOC};

/**
 * Create CommandOptions for a main program.
 * 
 * A command option is one of
 * <ul>
 * <li>boolean: --optionName
 * <li>string:  --optionName stringValue 
 * <li>loc:     --optionName sourceLocationValue
 * <li>locs:    --optionName sourceLocationValue
 *                   sourceLocationValue are either (quoted) Rascal source locations or file path names
 *                   multiple locs options of the same name accumulate to a list of source locations
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
 *     .boolOption("Y").help("and Y too!")
 *     <p>
 *     .mdule("Module to analyze")
 *     <p>
 *     .handleArgs(args); // the string arguments coming from the command line
 * </code>
 */
public class CommandOptions {
    private static final String IGNORES_PATH_CONFIG_OPTION = "ignores";
    private static final String BIN_PATH_CONFIG_OPTION = "bin";
    private static final String LIB_PATH_CONFIG_OPTION = "lib";
	private static final String GENERATED_SOURCES_PATH_CONFIG_OPTION = "generated-sources";
	private static final String PROJECT_PATH_CONFIG_OPTION = "project";
    private static final String SRC_PATH_CONFIG_OPTION = "src";
    
    protected TypeFactory tf;
	protected io.usethesource.vallang.IValueFactory vf;
	private boolean inCommandOptions = true;
	private boolean singleModule = true;
	
	private IList modules;
	private int minModules = 0;
	private int maxModules = 0;
	private String moduleHelp;
	
	protected Options commandOptions;
	private Options moduleOptions;
	
	private String commandName;
	
	public CommandOptions(String commandName){
		this.commandName = commandName;
		tf = TypeFactory.getInstance();
		vf = ValueFactoryFactory.getValueFactory();
		commandOptions = new Options();
		moduleOptions = new Options();
		modules = vf.list();
		moduleHelp = "Rascal Module";
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
	/*			Locs options													*/
	/****************************************************************************/

	/**
	 * Declare a locs option (a list of locations)
	 * @param name of option
	 * @return OptionBuilder
	 */
	public OptionBuilder locsOption(String name){
		return new OptionBuilder(this, OptionType.LOCS, name);
	}

	/**
	 * Get the value of a locs option from the command options
	 * @param name
	 * @return value of option
	 */
	public IList getCommandLocsOption(String name){
		return (IList) commandOptions.get(OptionType.LOCS, name);
	}

	/**
	 * Get the value of a locs option from the module options
	 * @param name
	 * @return value of option
	 */
	public IList getModuleLocsOption(String name){
		return (IList) moduleOptions.get(OptionType.LOCS, name);
	}

	/****************************************************************************/
	/*			Module argument(s)												*/
	/****************************************************************************/
	
	/**
	 * Command has one Module (Rascal Module, Concept file, ...) as argument
	 * @param helpText describes the role of the single module argument
	 * @return this CommandOptions
	 */
	public CommandOptions module(String helpText){
	    return modules(helpText, 1, 1);
	}
	
	/**
	 * Get the name of the single module argument of the command
	 * @return module name
	 */
	public IString getModule(){
		return (IString) modules.get(0);
	}
	
	/**
	 * Command has one or more Modules as argument
	 * @param helpText describes the role of the one or more module arguments
	 * @return this CommandOptions
	 */
	public CommandOptions modules(String helpText){
	  return modules(helpText, 1, 1000000);
	}
	
	/**
     * Command has min or more Modules as argument
     * @param helpText describes the role of the one or more module arguments
     * @return this CommandOptions
     */
    public CommandOptions modules(String helpText, int min){
      return modules(helpText, min, 1000000);
    }
	
	/**
     * Command has between min and max Modules as argument
     * @param helpText describes the role of the one or more module arguments
     * @return this CommandOptions
     */
    public CommandOptions modules(String helpText, int min, int max){
        minModules = min;
        maxModules = max;
        inCommandOptions = false;
        moduleHelp = helpText;
        return this;
    }

	/**
	 * Get the names of the module arguments of the command
	 * @return list of module names
	 */
	public IList getModules(){
		return modules;
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
					} else if(currentOptions.contains(OptionType.LOCS, option)){
						ISourceLocation newLoc = convertLoc(getOptionValue(args, i));
						currentOptions.update(OptionType.LOCS, option, (current) -> current == null ? vf.list(newLoc) : ((IList) current).append(newLoc));
					} else if(currentOptions.contains(OptionType.LOC, option)){
						currentOptions.set(OptionType.LOC, option, convertLoc(getOptionValue(args, i)));
					} else {
						printUsageAndExit("Unknown command option " + args[i]);
						return;
					}
					i += 2;
				}
			} else {
				modules = modules.append(vf.string(args[i]));
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
		
		if(commandOptions.hasNonDefaultValue(OptionType.BOOL, "help")) {
			help();
			System.exit(0);
		}
		checkDefaults();
		
		if (modules.length() == 0 && minModules > 0) {
			printUsageAndExit("Missing Rascal module" + (singleModule ? "" : "s"));
		} else if (modules.length() > 0 && maxModules == 0) {
		    printUsageAndExit("No modules expected");
		} else if(modules.length() > maxModules) {
			printUsageAndExit("Too many modules defined: " + modules);
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
		StringBuilder sb = new StringBuilder();
		sb.append("Forbid use of default values for");
		String sep = " ";
		for(String name : respectNoDefaults){
			sb.append(sep).append(name);
			sep = ", ";
		}
		return sb.toString();
	}
	
	private void help(){
		System.err.println(usage(""));
		System.err.println();
		for(Option option : commandOptions){
			System.err.printf("%20s  %s\n", option.help(), option.helpText);
		}

		System.err.printf("%20s  %s\n", singleModule ? " <RascalModule>" : " <RascalModules>", moduleHelp);
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
	 * @return all module options as a Map
	 */
	public Map<String, IValue> getModuleOptionsAsMap(){
	    Map<String,IValue> result = new HashMap<>();
		for(Option option : moduleOptions){
			result.put(option.name, option.currentValue);
		}
		return result;
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
			TypeStore store = new TypeStore(RascalValueFactory.getStore());
			Type start = TypeFactory.getInstance().sourceLocationType();

			try (StringReader in = new StringReader(loc)) {
				return (ISourceLocation) new StandardTextReader().read(vf, store, start, in);
			} 
			catch (FactTypeUseException | IOException e) {
				printUsageAndExit(e.getMessage());
                throw new RuntimeException(e);
			} 
		} else {
            try {
                File file = new File(loc);
                if (file.isAbsolute()) {
                    return URIUtil.createFileLocation(file.getAbsolutePath());
                }
                else {
                    return URIUtil.correctLocation("cwd", "", loc);
                }
            }
            catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }

		}
	}

	public ISourceLocation getDefaultStdLocation(){
		try {
			return vf.sourceLocation("std", "", "");
		} catch (URISyntaxException e) {
			printUsageAndExit("Cannot create default location: " + e.getMessage());
			return null;
		}
	}
	
	
	
	public IList getDefaultStdlocs(){
		return vf.list(getDefaultStdLocation());
	}
	
	public IList getDefaultIgnores(){
		return PathConfig.getDefaultIgnoresList();
	}

	public ISourceLocation getDefaultRelocLocation(){
          return URIUtil.correctLocation("noreloc", "", "");
	}
	
	public ISourceLocation getDefaultProjectLocation(){
        return URIUtil.correctLocation("noproject", "", "");
  }
	
	public PathConfig getPathConfig(PathConfig.RascalConfigMode mode) throws IOException {
        ISourceLocation project = getCommandLocOption(PROJECT_PATH_CONFIG_OPTION);
        
        if (!project.equals(getDefaultProjectLocation())) {
	        return PathConfig.fromSourceProjectRascalManifest(project, mode);
	    }
        else {
            return new PathConfig(getCommandLocsOption(SRC_PATH_CONFIG_OPTION),
                getCommandLocsOption(LIB_PATH_CONFIG_OPTION),
                getCommandLocOption(BIN_PATH_CONFIG_OPTION),
                getCommandLocsOption(IGNORES_PATH_CONFIG_OPTION)
			);
        }
	}

    public CommandOptions noModuleArgument() {
        return this;
    }

    public CommandOptions pathConfigOptions() {
        this
        .locOption(PROJECT_PATH_CONFIG_OPTION)
        .locDefault(getDefaultProjectLocation())
        .help("Top level location where a project is located with its META-INF/RASCAL.MF file")
        
      
        .locsOption(SRC_PATH_CONFIG_OPTION)      
        .locsDefault(getDefaultStdlocs().isEmpty() ? vf.list(getDefaultStdlocs()) : getDefaultStdlocs())
        .help("Add (absolute!) source location, use multiple --src arguments for multiple locations")

        .locOption(BIN_PATH_CONFIG_OPTION)
        .locDefault(v -> {
            System.err.println("WARNING: using cwd:///rascal-bin as default bin target folder for Rascal compiler.");
            return URIUtil.correctLocation("cwd", "", "rascal-bin");
        })
        .help("Directory for Rascal binaries")

        .locsOption(LIB_PATH_CONFIG_OPTION)      
        .locsDefault((co) -> vf.list(co.getCommandLocOption(BIN_PATH_CONFIG_OPTION)))
        .help("Add new lib location, use multiple --lib arguments for multiple locations")

        .locsOption(IGNORES_PATH_CONFIG_OPTION)
        .locsDefault(PathConfig.getDefaultIgnoresList())
        .help("Add new ignored locations, use multiple --ignores arguments for multiple locations")
        
        .locOption(GENERATED_SOURCES_PATH_CONFIG_OPTION)
		.locDefault(PathConfig.getDefaultGeneratedSources())
        .help("Add new target location for generated sources")
        
        ;
    
        return this;
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
	
	@Override
	public String toString() {
	    return name + ":" + currentValue != null ? currentValue.toString() : defaultValue.toString();
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
		case LOCS: res += " <locs>"; break;
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