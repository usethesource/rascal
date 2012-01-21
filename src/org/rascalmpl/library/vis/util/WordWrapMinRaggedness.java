package org.rascalmpl.library.vis.util;

import java.util.TreeMap;

import org.rascalmpl.library.vis.graphics.FontStyle;
import org.rascalmpl.library.vis.swt.SWTFontsAndColors;

public class WordWrapMinRaggedness {
	
	public class WordWrapElement{
		
		String[] syllables;
		double[] widthsBeforeHyphen;
		double[] widthsAfterHyphen;
		double wordWidth;
		
		public WordWrapElement(String[] syllables){
			this.syllables = syllables;
			widthsBeforeHyphen = new double[syllables.length];
			widthsAfterHyphen = new double[syllables.length];
			String s = "";
			for(int i = 0 ; i < syllables.length; i++){
				widthsBeforeHyphen[i] = widthsAfterHyphen[i] = -1;
				s+= syllables[i];
			}
			wordWidth = 
					SWTFontsAndColors.textWidth(s, fontName, fontSize, styles);
		}

		double getWidth(){
			return getWidthAfterHyphen(0);
		}
		
		double getWidthBeforeHyphen(int syllableBeforeHyphen){
			int syllableAfterHyphen = syllableBeforeHyphen +1;
			if(widthsBeforeHyphen[syllableAfterHyphen] >= 0){
				return widthsBeforeHyphen[syllableAfterHyphen];
			}
			if(syllableAfterHyphen == 0){
				widthsBeforeHyphen[0] = 0;
			}
			String s = syllables[0];
			for(int i = 1 ; i <= syllableAfterHyphen ; i++){
				s += syllables[i];
			}
			s+="-";
			widthsBeforeHyphen[syllableAfterHyphen] = 
					SWTFontsAndColors.textWidth(s, fontName, fontSize, styles);
			return widthsBeforeHyphen[syllableAfterHyphen];
		}
		
		double getWidthAfterHyphen(int syllableAfterHyphen){
			if(widthsAfterHyphen[syllableAfterHyphen] >= 0){
				return widthsAfterHyphen[syllableAfterHyphen];
			}
			String s = syllables[syllableAfterHyphen+1];
			for(int i = syllableAfterHyphen+1 ; i <= syllables.length ; i++){
				s += syllables[i];
			}
			widthsAfterHyphen[syllableAfterHyphen] = 
					SWTFontsAndColors.textWidth(s, fontName, fontSize, styles);
			return widthsAfterHyphen[syllableAfterHyphen];
		}
		
		boolean isHyphenated(int lastSyllable){
			return lastSyllable != syllables.length-1;
		}
		
	}
	
	public class LineElements implements Comparable<LineElements>{
		int startWord, endWord;
		int startSyllable, endSyllable;
		double lineWidth;
		
		LineElements(int startWord, int startSyllable, int endWord,  int endSyllable,double lineWidth){
			this.startWord = startWord;
			this.startSyllable = startSyllable;
			this.endWord = endWord;
			this.endSyllable = endSyllable;
			this.lineWidth = lineWidth;
		}
		
		@Override
		public int compareTo(LineElements o) {
			if(o.lineWidth != lineWidth){
				return (int)Math.signum(lineWidth - o.lineWidth);
			} if(o.startWord != o.startWord){
				return startWord - o.startWord;
			} else if(o.startSyllable != startSyllable){
				return startSyllable - o.startSyllable;
			} else if ( endWord != o.endWord){
				return endWord - o.endWord; 
			} else {
				return endSyllable - o.endSyllable; 
			}
		}
		
		public int getStartWord() { return startWord; }
		public int getEndWord() { return startWord; }
		public int getStartHyphen() { return startWord; }
		public int getEndHyphen() { return startWord; }
	}
	
	
	WordWrapElement[] elems; double desiredSpaceSize; double minSpaceWidth; double[] lineWidths;
	String fontName; int fontSize; FontStyle[] styles; double hyphenPenalty;
	TreeMap<LineElements,Double> costs;
	double minCost;
	
	public WordWrapMinRaggedness(WordWrapElement[] elems, double desiredSpaceSize, double[] lineWidths,
			double hyphenPenalty, String fontName, int fontSize,FontStyle ... styles) {
		this.elems = elems;
		this.desiredSpaceSize = desiredSpaceSize;
		this.lineWidths = lineWidths;
		this.fontName = fontName;
		this.fontSize = fontSize;
		this.styles = styles;
		costs = new TreeMap<LineElements,Double>();
		minCost = Double.POSITIVE_INFINITY;
	}
	
	double costOf(int startWord,  int endWord, double lineWidth){
		return costOf(startWord,0,endWord,elems[endWord].syllables.length-1,lineWidth,0,false);
	}
	
	double costOf(int startWord,  int endWord, double lineWidth,double prevCost){
		return costOf(startWord,0,endWord,elems[endWord].syllables.length-1,lineWidth,lineWidth-prevCost,true);
	}
	
	double costOf(int startWord, int startSyllable, int endWord, int endSyllable, double lineWidth, double prevWidth, boolean prev){
		LineElements thisLine = new LineElements(startWord, startSyllable, endWord, endSyllable,lineWidth);
		if(costs.containsKey(thisLine)){
			return costs.get(thisLine);
		} else{
			double width = 0;
			thisLine.endWord--;
			if(prev){
				width = prevWidth;
				thisLine.endWord++;
				width+=minSpaceWidth;
				width+=elems[endWord].getWidthBeforeHyphen(endSyllable);
			} else {
				width+=elems[startWord].getWidthAfterHyphen(startSyllable);
				width+=elems[endWord].getWidthBeforeHyphen(endSyllable);
				width+=minSpaceWidth;
				for(int i = startWord +1; i <= endWord && width <= lineWidth; i++){
					width+=minSpaceWidth;
					width+=elems[i].getWidth();
				}
			}
			double cost;
			if(width > lineWidth){
				cost = Double.POSITIVE_INFINITY;
			} else {
				cost = lineWidth - width +
						(elems[endWord].isHyphenated(endSyllable) ? hyphenPenalty : 0);
			}
			costs.put(thisLine, cost);
			return cost;
		}
	}
	
//	LineElements getBest(){
//		ArrayList<LineElements> lines = new ArrayList<LineElements>();
//		int line = 0;
//		for(int startWord = 0; startWord < elems.length; startWord++){
//			int endWord = startWord;
//			while(costOf(startWord,endWord,)
//		}
//	}


}
