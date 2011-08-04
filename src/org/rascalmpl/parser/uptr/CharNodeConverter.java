package org.rascalmpl.parser.uptr;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.rascalmpl.parser.gtd.result.CharNode;
import org.rascalmpl.values.ValueFactoryFactory;
import org.rascalmpl.values.uptr.Factory;

/**
 * A converter for character result nodes.
 */
public class CharNodeConverter{
	private final static IValueFactory VF = ValueFactoryFactory.getValueFactory();
	
	private final static IConstructor[] cache = new IConstructor[128];
	
	private CharNodeConverter(){
		super();
	}
	
	/**
	 * Converts the given character result node to the UPTR format.
	 */
	public static IConstructor convertToUPTR(CharNode node){
		int charNumber = node.getCharacter();
		
		// Cache 7-bit ASCII character results.
		if(charNumber < 128){
			IConstructor result = cache[charNumber];
			if(result != null) return result;
			
			result = VF.constructor(Factory.Tree_Char, VF.integer(charNumber));
			cache[charNumber] = result;
		}
		
		return VF.constructor(Factory.Tree_Char, VF.integer(charNumber));
	}
}
