/*******************************************************************************
 * Copyright (c) 2012-2013 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
*******************************************************************************/
package org.rascalmpl.parser.gtd.util;

public class BitSet{
	public final static int OFF = 0;
	public final static int ON = 1;
	
	private final static int BLOCK_SHIFT = 5;
	private final static int BLOCK_MASK = 0x0000001f;
	
	private int[] bits;
	
	public BitSet(){
		super();
		
		bits = new int[1];
	}
	
	public BitSet(int size){
		super();
		
		int nrOfBlocks = (size >> BLOCK_SHIFT) + (((size & BLOCK_MASK) != 0) ? 1 : 0);
		bits = new int[nrOfBlocks];
	}
	
	public void enlargeTo(int size){
		int[] oldBits = bits;
		int oldNrOfBlocks = oldBits.length;
		int newNrOfBlocks = (size >> BLOCK_SHIFT) + (((size & BLOCK_MASK) != 0) ? 1 : 0);
		if(newNrOfBlocks > oldNrOfBlocks){
			bits = new int[newNrOfBlocks];
			System.arraycopy(oldBits, 0, bits, 0, oldNrOfBlocks);
		}
	}
	
	public void set(int index){
		bits[index >> BLOCK_SHIFT] |= (1 << (index & BLOCK_MASK));
	}
	
	public void unset(int index){
		bits[index >> BLOCK_SHIFT] &= (~(1 << (index & BLOCK_MASK)));
	}
	
	public void flip(int index){
		bits[index >> BLOCK_SHIFT] ^= (1 << (index & BLOCK_MASK));
	}
	
	public void or(int index, int bit){
		bits[index >> BLOCK_SHIFT] |= (bit << (index & BLOCK_MASK));
	}
	
	public void and(int index, int bit){
		bits[index >> BLOCK_SHIFT] &= (bit << (index & BLOCK_MASK));
	}
	
	public void xor(int index, int bit){
		bits[index >> BLOCK_SHIFT] ^= (bit << (index & BLOCK_MASK));
	}
	
	public int get(int index){
		return ((bits[index >> BLOCK_SHIFT] >> (index & BLOCK_MASK)) & 0x00000001);
	}
	
	public boolean isSet(int index){
		return (((bits[index >> BLOCK_SHIFT] >> (index & BLOCK_MASK)) & 0x00000001) == 0x00000001);
	}
	
	public void clear(){
		bits = new int[1];
	}
	
	public String toString(){
		StringBuilder sb = new StringBuilder();
		
		sb.append('[');
		sb.append(get(0));
		
		for(int i = 1; i < (bits.length << BLOCK_SHIFT); ++i){
			sb.append(',');
			sb.append(get(i));
		}
		sb.append(']');
		
		return sb.toString();
	}
}
