/*******************************************************************************
 * Copyright (c) 2009-2011 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
*******************************************************************************/
package org.rascalmpl.parser.uptr;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IListWriter;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.rascalmpl.parser.gtd.result.CharNode;
import org.rascalmpl.parser.gtd.result.SkippedNode;
import org.rascalmpl.values.ValueFactoryFactory;
import org.rascalmpl.values.uptr.Factory;

/**
 * A converter for result nodes that contain skipped characters for error recovery
 */
public class RecoveryNodeConverter{
	private final static IValueFactory VF = ValueFactoryFactory.getValueFactory();
	
	public RecoveryNodeConverter(){
		super();
	}
	
	/**
	 * Converts the given literal result node to the UPTR format.
	 */
	public IConstructor convertToUPTR(SkippedNode node){
		CharNode[] content = node.getSkippedChars();
		
		int numberOfCharacters = content.length;
		
		IListWriter listWriter = VF.listWriter(Factory.Tree);
		for(int i = 0; i < numberOfCharacters; ++i){
			listWriter.append(VF.constructor(Factory.Tree_Char, VF.integer(content[i].getCharacter())));
		}
		
		return (IConstructor) Factory.Tree_Appl.make(VF, Factory.Production_Skipped.make(VF), listWriter.done());
	}
}
