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
package org.rascalmpl.parser.gtd.result;

import org.eclipse.imp.pdb.facts.IValueFactory;
import org.rascalmpl.values.ValueFactoryFactory;

public abstract class AbstractNode{
	protected final static IValueFactory VF = ValueFactoryFactory.getValueFactory();
	
	public AbstractNode(){
		super();
	}
	
	public abstract int getID();
	
	public final boolean isEpsilon(){
		return (this instanceof EpsilonNode);
	}
	
	public abstract boolean isEmpty();
	
	public abstract boolean isSeparator();
	
	public abstract void setRejected();
	
	public abstract boolean isRejected();
	
	public static class CycleMark{
		public int depth = Integer.MAX_VALUE;
		
		public CycleMark(){
			super();
		}
		
		public void setMark(int depth){
			if(depth < this.depth){
				this.depth = depth;
			}
		}
		
		public void reset(){
			depth = Integer.MAX_VALUE;
		}
	}
}
