package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.commonmark;
import java.io.IOException;
import java.util.Stack;

public class CommonMarkWriter {
	private final Appendable buffer;
	
	private boolean inBlockQuote = false;
	private int indent = 0;
	private int column = 0;
	private int lineWidth = 80;
	
	private ListBlockParams current = null;
	
	private Stack<ListBlockParams> listBlocks = new Stack<>();
	
	CommonMarkWriter(Appendable out, int lineWidth){
		this.buffer = out;
		this.lineWidth = lineWidth;
	}

	void setInBlockQuote(boolean inBlockQuote){
		this.inBlockQuote = inBlockQuote;
	}
	
	void increaseIndent(int delta){
		indent += delta;
	}
	
	void decreaseIndent(int delta){
		indent -= delta;
	}
	
	void enterOrderedList(int start, Character delimiter){
		current = new ListBlockParams(true, start, delimiter);
		listBlocks.push(current);
		indent += current.startIndent;
	}
	
	void leaveOrderedList(){
		indent -=  current.startIndent;
		listBlocks.pop();
		if(!listBlocks.isEmpty()){
			current = listBlocks.peek();
		}
		line();
	}
	
	void enterBulletList(Character marker){
		current = new ListBlockParams(marker);
		listBlocks.push(current);
		indent += current.startIndent;
	}
	
	void leaveBulletList(){
		indent -=  current.startIndent;
		listBlocks.pop();
		if(!listBlocks.isEmpty()){
			current = listBlocks.peek();
		}
		line();
	}
	
	void optionalLineBreak(){
		if(column + 10 >= lineWidth){
			line();
		}
	}
	
	void optionalLineBreak(int len){
		if(column + len >= lineWidth){
			line();
		}
	}
	
	int available(){
		return lineWidth - column;
	}
	
	void insertListItem(){
		indent -= current.startIndent;
		line();
		indent += current.startIndent;
		append(Integer.toString(current.start));
		current.start++;
		append(current.delimiter + " ");
	}
	
	public void line() {
		append("\n");
		column = 0;
		if(inBlockQuote){
			append("> ");
			column += 2;
		}
		if(indent > 0){
			for(int i = 0; i < indent; i++){
				append(" ");
			}
		}
		column += indent;
	}
	
	protected void append(String s) {
		try {
			buffer.append(s);
			column += s.length();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}

class ListBlockParams {
	int start = 0;
	int startIndent = 0;
	Character delimiter = '.';
	boolean ordered;
	
	ListBlockParams(boolean ordered, int start, Character delimiter){
		this.ordered = ordered;
		this.start = start;
		this.delimiter = delimiter;
		startIndent = String.valueOf(start).length() + 2;
	}
	
	ListBlockParams(Character delimiter){
		this(false, 0, delimiter);
		startIndent = 3;
	}
}
