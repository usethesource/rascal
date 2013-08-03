package org.rascalmpl.library.experiments.CoreRascal.Translation;

enum State {FIRST, INIT, FINISHED};
public class Counter {
	State state = State.FIRST;
	int n;
	
	Counter(int n){
		this.n = n;
	}
	
	void start() {
	}

	boolean hasMore() { return state != State.FINISHED; }
	
	int resume(){
		switch(state){
			case FIRST:		state = State.INIT;
			case FINISHED: 	throw new RuntimeException();
			default:
							break;
		}
		n -= 1;
		if(n == 0)
			state = State.FINISHED;
		return n;
	}
	
	public static void main(){
		Counter c = new Counter(10);
		while(c.hasMore())
			System.out.println(c.resume());
	}
}
