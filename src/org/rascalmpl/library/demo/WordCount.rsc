@license{
  Copyright (c) 2009-2011 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@contributor{Paul Klint - Paul.Klint@cwi.nl - CWI}
module demo::WordCount

import IO;
import Map;
import List;
import Exception;
import Benchmark;
import String;
 
// Various ways to count words in a string.

// wordCount takes a list of strings and a count function
// that is applied to each line. The total number of words is returned

public int wordCount(list[str] input, int (str s) countLine)
{
  count = 0;
  for(str line <- input){
     count += countLine(line);
  }
  return count;
}

// Three versions of a countLine function.

// countLine1: use a for loop and enumerator to loop over all words.

public int countLine1(str S){
  int count = 0;
  for(/[a-zA-Z0-9]+/<- S){
       count += 1;
  }
  return count;
}

// countLine2: using a while loop

public int countLine2(str S){
  int count = 0;
  
  // \w matches any word character
  // \W match3es any non-word character
  // <...> are groups and should appear at the top level.
  while (/^\W*<word:\w+><rest:.*$>/ := S) { 
    count += 1; 
    S = rest; 
  }
  return count;
}

// countLine3: Maintain word count per word.

// (This is clearly overkill for the present example.
// The function as a whole makes no sense, since we throw
// the map away.)

// Note how we use an exception to catch the case of a not yet
// initialized table entry. One could also use the =? operator here.
// 

public int countLine3(str S){
  map[str,int] allCounts = ();
  int cnt = 0;
  for(/<word:\w+>/<- S){
       cnt = cnt + 1;
       try {
         allCounts[word] = allCounts[word] + 1;   //TODO += does not work
       } catch NoSuchKey(value key):{
       		allCounts[word] = 1;
       }
  }
  return sum([allCounts[K] | str K <- allCounts]);
}

// Auxiliary function to sum all elements of a list of integers

public int sum(list[int] l) {
  int sum = 0;
  for (int i <- l) sum += i;
  return sum;
}

// Example how wordCount can be used in a complete Rascal program

public void main(str argv ...){
  int total = 0;
  for(str fileName <- argv){
    try {
       int count = wordCount(readFile(fileName));
       println("<count> word in file <fileName>");
       total = total + count;
 	}
 	catch: {println("Skipping file <fileName>");}
  }
  
  println("<total> words in all files");
}

public list[str] Jabberwocky = [
	"Jabberwocky by Lewis Carroll",
	"",
	"\'Twas brillig, and the slithy toves",
	"Did gyre and gimble in the wabe;",
	"All mimsy were the borogoves,",
	"And the mome raths outgrabe.",
	"",
	"\"Beware the Jabberwock, my son!",
	"The jaws that bite, the claws that catch!",
	"Beware the Jubjub bird, and shun",
	"The frumious Bandersnatch!\"",
	"",
	"\'Twas brillig, and the slithy toves",
	"Did gyre and gimble in the wabe;",
	"All mimsy were the borogoves,",
	"And the mome raths outgrabe.",
	"",
	"\"Beware the Jabberwock, my son!",
	"The jaws that bite, the claws that catch!",
	"Beware the Jubjub bird, and shun",
	"The frumious Bandersnatch!\"",
	"",
	"He took his vorpal sword in hand:",
	"Long time the manxome foe he sought—",
	"So rested he by the Tumtum tree,",
	"And stood awhile in thought.",
	"",
	"And as in uffish thought he stood,",
	"The Jabberwock, with eyes of flame,",
	"Came whiffling through the tulgey wood",
	"And burbled as it came!",
	"",
	"One, two! One, two! and through and through",
	"The vorpal blade went snicker-snack!",
	"He left it dead, and with its head",
	"He went galumphing back.",
	"",
	"\"And hast thou slain the Jabberwock?",
	"Come to my arms, my beamish boy!",
	"O frabjous day! Callooh! Callay!",
	"He chortled in his joy.",
	"",
	"\'Twas brillig, and the slithy toves",
	"Did gyre and gimble in the wabe;",
	"All mimsy were the borogoves,",
	"And the mome raths outgrabe."
];

public test bool t1() = wordCount(Jabberwocky, countLine1) == 216;
public test bool t2() = wordCount(Jabberwocky, countLine2) == 216;
public test bool t3() = wordCount(Jabberwocky, countLine3) == 216;

// Without caching:
// txt: 94208 lines
// countLine1: 5040. msec, countLine2: 16051. msec, countLine3: 15585. msec
// With caching of regexps and matchers:
// txt: 94208 lines
// countLine1: 3649. msec, countLine2: 3504. msec, countLine3: 3249. msec

// With caching of matchers only
// txt: 94208 lines
// countLine1: 6388. msec, countLine2: 34059. msec, countLine3: 49253. msec


public void measure(){
   list[str] txt = Jabberwocky;
   for(int i <- [1 .. 11])
       txt = txt + txt;
       
	time0 = currentTimeMillis();
	res1 = wordCount(txt, countLine1);          time1 = currentTimeMillis();
	res2 = wordCount(txt, countLine2);          time2 = currentTimeMillis();
	res3 = wordCount(txt, countLine2);          time3 = currentTimeMillis();
	
	d1 = time1 - time0;
	d2 = time2 - time1;
	d3 = time3 - time2;
	
	println("txt: <size(txt)> lines\ncountLine1: <d1> msec, countLine2: <d2> msec, countLine3: <d3> msec");
}
