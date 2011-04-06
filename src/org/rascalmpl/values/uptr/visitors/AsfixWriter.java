/*******************************************************************************
 * Copyright (c) 2009-2011 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
 *   * Tijs van der Storm - Tijs.van.der.Storm@cwi.nl
 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
*******************************************************************************/
package org.rascalmpl.values.uptr.visitors;

import java.io.IOException;
import java.io.Writer;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IExternalValue;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.ISet;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.visitors.VisitorException;
import org.rascalmpl.values.uptr.TreeAdapter;

public class AsfixWriter extends IdentityTreeVisitor {

	private Writer writer;
	//int nesting = 0;

	public AsfixWriter(Writer writer) {
		this.writer = writer;
		//this.nesting = 0;
	}
	
	@Override
	public IConstructor visitTreeAmb(IConstructor arg) throws VisitorException {
		try {
			writer.write("amb([\n");
			ISet set = TreeAdapter.getAlternatives(arg);
			int len = set.size();
			int i = 0;
			for (IValue x: set) {
				x.accept(this);
				if (i < len - 1) {
					writer.write(",\n");
				}
				++i;
			}
			writer.write("])");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public IConstructor visitTreeAppl(IConstructor arg) throws VisitorException {
		try{
			writer.write("appl(");
			writer.write(TreeAdapter.getProduction(arg).toString());
			writer.write(", [");
			IList list = TreeAdapter.getArgs(arg);
			int len = list.length();
			int i = 0;
//			++nesting;
			for (IValue x: list) {
//				for (int j = 0; j < nesting; ++j) {
//					writer.write(" ");
//				}
				x.accept(this);
				if (i < len - 1) {
					writer.write(", \n");
				}
				i++;
			}
//			--nesting;
			writer.write("])");
		}catch(IOException e){
			e.printStackTrace();
		}		
		return null;
	}

	@Override
	public IConstructor visitTreeChar(IConstructor arg) throws VisitorException {
		try {
			writer.write(new Integer(TreeAdapter.getCharacter(arg)).toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return arg;
	}

	@Override
	public IConstructor visitTreeCycle(IConstructor arg)  throws VisitorException{
		try{
			writer.write("cycle(");
			writer.write(arg.get("symbol").toString());
			writer.write(arg.get("cycleLength").toString());
		}catch(IOException e){
			e.printStackTrace();
		}
		return null;
	}

	public IValue visitExternal(IExternalValue externalValue){
		throw new UnsupportedOperationException();
	}
	
	public IConstructor visitTreeError(IConstructor arg) throws VisitorException{
		try{
			writer.write("error(");
			writer.write(TreeAdapter.getProduction(arg).toString());
			writer.write(", [");
			
			IList list = TreeAdapter.getArgs(arg);
			int len = list.length();
			int i = 0;
//			++nesting;
			for(IValue x: list){
//				for (int j = 0; j < nesting; ++j) {
//					writer.write(" ");
//				}
				x.accept(this);
				if(i < len - 1){
					writer.write(", \n");
				}
				i++;
			}
//			--nesting;
			writer.write("], [");
//			++nesting;
			IList rest = (IList) arg.get("rest");
			len = rest.length();
			i = 0;
			for(IValue x: rest){
//				for (int j = 0; j < nesting; ++j) {
//					writer.write(" ");
//				}
				x.accept(this);
				if(i < len - 1){
					writer.write(", \n");
				}
				i++;
			}
//			--nesting;
			writer.write("])");
		}catch(IOException e){
			e.printStackTrace();
		}		
		return null;
	}
	
	public IConstructor visitTreeExpected(IConstructor arg) throws VisitorException{
		try{
			writer.write("expected(");
			writer.write(arg.get("symbol").toString());
			writer.write(", [");
	
	//		--nesting;
			writer.write("], [");
	//		++nesting;
			IList rest = (IList) arg.get("rest");
			int len = rest.length();
			int i = 0;
			for (IValue x: rest) {
	//			for (int j = 0; j < nesting; ++j) {
	//				writer.write(" ");
	//			}
				x.accept(this);
				if (i < len - 1) {
					writer.write(", \n");
				}
				i++;
			}
	//		--nesting;
			writer.write("])");
		}catch(IOException e){
			e.printStackTrace();
		}
		return null;
	}
}
