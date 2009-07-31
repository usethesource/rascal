module demo::WordCount

import IO;
import Map;
import List;
import Exception;
import UnitTest;
 
/* this is a * test * comment */

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

public int wordCount(list[str] input, int (str s) countLine)
{
  count = 0;
  for(str line <- input){
  println("line = <line>");
     count = count + countLine(line);
  }
  return count;
}

public int wordCount1(list[str] input)
{
  count = 0;
  for (str line <- input) {
     println("line = <line>");
     count = count + countLine1(line);
  }
  return count;
}

public int countLine1(str S){
  int count = 0;
  for(/[a-zA-Z0-9]+/<- S){
       count = count + 1;
  }
  return count;
}

// Here is an alternative (but less desirable) declaration:
public int countLine2(str S){
  int count = 0;
  
  // \w matches any word character
  // \W match3es any non-word character
  // <...> are groups and should appear at the top level.
  while (/^\W*<word:\w+><rest:.*$>/ := S) { 
    count = count + 1; 
    S = rest; 
  }
  return count;
}

// Maintain word count per word.
// Note how the =? operator initializes each map entry
// to an appropriate value (0 in this case)
// The function as a whole makes no sense, since we throw
// the map away.

public int sum(list[int] l) {
  int sum = 0;
  for (int i <- l) sum += i;
  return sum;
}

public int countLine3(str S){
  map[str,int] allCounts = ();
  int cnt = 0;
  for(/<word:\w+>/<- S){
       cnt = cnt + 1;
       try {
         allCounts[word] = allCounts[word] + 1;
       } catch NoSuchKey(value key):{
       		allCounts[word] = 1;
       }
  }
  return sum([allCounts[K] | str K <- allCounts], 0);
}

public list[str] Jabberwocky = [
	"Jabberwocky by Lewis Carroll",
	"",
	"'Twas brillig, and the slithy toves",
	"Did gyre and gimble in the wabe;",
	"All mimsy were the borogoves,",
	"And the mome raths outgrabe.",
	"",
	"\"Beware the Jabberwock, my son!",
	"The jaws that bite, the claws that catch!",
	"Beware the Jubjub bird, and shun",
	"The frumious Bandersnatch!\"",
	"",
	"'Twas brillig, and the slithy toves",
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
	"'Twas brillig, and the slithy toves",
	"Did gyre and gimble in the wabe;",
	"All mimsy were the borogoves,",
	"And the mome raths outgrabe."
];

public bool test(){
	assertEqual(wordCount(Jabberwocky, countLine1), 216);
	assertEqual(wordCount(Jabberwocky, countLine2), 216);
	assertEqual(wordCount(Jabberwocky, countLine3), 216);
	return report();
}