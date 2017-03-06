package org.rascalmpl.library.experiments.Compiler.Commands;

import java.util.function.Function;

import io.usethesource.vallang.IList;
import io.usethesource.vallang.ISourceLocation;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.IValueFactory;

public class  OptionBuilder {
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
		case LOCS:initialValue = vf.list(); break;
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
	public OptionBuilder boolDefault(boolean defaultValue){
		check(OptionType.BOOL);
		this.defaultValue = commandOptions.vf.bool(defaultValue);
		return this;
	}
	
	/**
	 * @param defaultValue for a boolean option as a function that returns a bool value
	 * @return this OptionBuilder
	 */
	public OptionBuilder boolDefault(Function<CommandOptions, Boolean> defaultValue){
		check(OptionType.BOOL);
		this.defaultValue = defaultValue;
		return this;
	}
	
	/**
	 * @param defaultValue for an int option
	 * @return this OptionBuilder
	 */
	public OptionBuilder intDefault(int defaultValue){
		check(OptionType.INT);
		this.defaultValue = commandOptions.vf.integer(defaultValue);
		return this;
	}
	
	/**
	 * @param defaultValue for an int option as a function that returns an int value
	 * @return this OptionBuilder
	 */
	public OptionBuilder intDefault(Function<CommandOptions, Integer> defaultValue){
		check(OptionType.INT);
		this.defaultValue = defaultValue;
		return this;
	}
	
	/**
	 * @param defaultValue for a str option
	 * @return this OptionBuilder
	 */
	public OptionBuilder strDefault(String defaultValue){
		check(OptionType.STR);
		this.defaultValue = commandOptions.vf.string(defaultValue);
		return this;
	}
	
	/**
	 * @param defaultValue for a str option as a function that returns a str value
	 * @return this OptionBuilder
	 */
	public OptionBuilder strDefault(Function<CommandOptions, String> defaultValue){
		check(OptionType.STR);
		this.defaultValue = defaultValue;
		return this;
	}
	
	/**
	 * @param defaultValue for a loc option
	 * @return this OptionBuilder
	 */
	public OptionBuilder locDefault(ISourceLocation defaultValue){
		check(OptionType.LOC);
		this.defaultValue = defaultValue;
		return this;
	}
	
	/**
	 * @param defaultValue for a loc option as a function that returns a loc value
	 * @return this OptionBuilder
	 */
	public OptionBuilder locDefault(Function<CommandOptions, ISourceLocation> defaultValue){
		check(OptionType.LOC);
		this.defaultValue = defaultValue;
		return this;
	}
	
	/**
	 * @param defaultValue for a path option
	 * @return this OptionBuilder
	 */
	public OptionBuilder locsDefault(IList defaultValue){
		check(OptionType.LOCS);
		this.defaultValue = defaultValue;
		return this;
	}
	
	/**
	 * @param defaultValue for a path option as a function that returns a list of locs
	 * @return this OptionBuilder
	 */
	public OptionBuilder locsDefault(Function<CommandOptions, IList> defaultValue){
		check(OptionType.LOCS);
		this.defaultValue = defaultValue;
		return this;
	}
	
	/**
	 * If "noDefaults" is set, the value of this option may not depend on its default but should be explicitly set
	 * @return this OptionBuilder
	 */
	public OptionBuilder respectNoDefaults(){
		this.mayForceNoDefault = true;
		return this;
	}
	
	/**
	 * 
	 * @param helpText defines help text for the current option and ends its definition
	 * @return CommandOptions and can therefore be used in the fluent interface of CommandOptions
	 */
	public CommandOptions help(String helpText){
		return commandOptions.addOption(new Option(optionType, name, initialValue, defaultValue, mayForceNoDefault, helpText));
	}
}