package org.rascalmpl.parser.gtd.util;

public class ForwardLink<E>{
	public final static ForwardLink TERMINATOR = new ForwardLink();
	
	public final ForwardLink<E> next;
	public final int length;
	
	public final E element;
	
	private ForwardLink(){
		super();
		
		this.next = null;
		this.length = 0;
		
		this.element = null;
	}
	
	public ForwardLink(ForwardLink next, E element){
		super();
		
		this.next = next;
		this.length = next.length + 1;
		
		this.element = element;
	}
}
