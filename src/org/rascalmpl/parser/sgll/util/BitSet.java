package org.rascalmpl.parser.sgll.util;

public class BitSet{
	private final static int BLOCK_SHIFT = 5;
	private final static int BLOCK_SIZE = 1 << BLOCK_SHIFT;
	private final static int BLOCK_MASK = 0x0000001f;
	
	private int[] bits;
	private int capacity;
	private int position;
	
	public BitSet(){
		super();
		
		bits = new int[1];
		capacity = 1 << BLOCK_SHIFT;
	}
	
	public BitSet(int size){
		super();
		
		int nrOfBlocks = (size >> BLOCK_SHIFT) + ((size != BLOCK_SIZE) ? 1 : 0);
		capacity = nrOfBlocks << BLOCK_SHIFT;
		bits = new int[nrOfBlocks];
		position = size;
	}
	
	private void enlarge(){
		int[] oldBits = bits;
		int oldNrOfBlocks = oldBits.length;
		bits = new int[oldNrOfBlocks << 1];
		System.arraycopy(oldBits, 0, bits, 0, oldNrOfBlocks);
		capacity <<= 1;
	}
	
	public void addSet(){
		if(position == capacity){
			enlarge();
		}
		
		bits[position >> BLOCK_SHIFT] |= (1 << (position++ & BLOCK_MASK));
	}
	
	public void addUnset(){
		if(position == capacity){
			enlarge();
		}
		
		++position;
	}
	
	public void set(int index){
		bits[index >> BLOCK_SHIFT] |= (1 << (index & BLOCK_MASK));
	}
	
	public void unset(int index){
		bits[index >> BLOCK_SHIFT] &= (~(1 << (index & BLOCK_MASK)));
	}
	
	public int get(int index){
		return ((bits[index >> BLOCK_SHIFT] >> (index & BLOCK_MASK)) & 0x00000001);
	}
}
