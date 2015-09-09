package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter;

import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;

public class NameCompleter {

	SortedSet<String> result;
	
	public NameCompleter(){
		result = new TreeSet<>(new Comparator<String>() {
			@Override
			public int compare(String a, String b) {
				if (a.charAt(0) == '\\') {
					a = a.substring(1);
				}
				if (b.charAt(0) == '\\') {
					b = b.substring(1);
				}
				return a.compareTo(b);
			}
		});
		
	}
	
	public SortedSet<String> getResult(){
		return result;
	}
	
    public void add(String completeName, String partialName) {
		if(completeName.startsWith("Library/")){
			return;
		}
		String shortName;
		int start = completeName.indexOf("/");
		int end = completeName.indexOf("(");
		if(start >= 0 && end > 0){
			shortName = completeName.substring(start + 1, end);
		} else {
			shortName = completeName;
		}
		if (shortName.startsWith(partialName) && !shortName.equals(partialName)) {
			if (shortName.contains("-")) {
				shortName = "\\" + shortName;
			}
			result.add(shortName);
		}
	}
}
