module WordCount

import IO;
 
/* this is a * test * comment */

public void main(str argv ...){
  int total = 0;
  for(str fileName : argv){
    try {
       int count = wordCount(readFile(fileName));
       println("<count> word in file <fileName>");
       total = total + count;
 	}
 	   catch {println("Skipping file <fileName>");}
  }
  
  println("<total> words in all files");
}

int wordCount(list[str] input)
{
  count = 0;
  for(line : input){
     count = count + wordCount1(line);
  }
  return count;
}

int wordCount1(str S){
  int count = 0;
  for(/[a-zA-Z0-9]+/: S){
       count = count + 1;
  }
  return count;
}

// Here is an alternative (but less desirable) declaration:
int wordCount2(str S){
  int count = 0;
  
  // \w matches any word character
  // \W match3es any non-word character
  // <...> are groups and should appear at the top level.
  while (/^\W*<word:\w+><rest:.*$>/ ~= S) { 
    count = count + 1; 
    S = rest; 
  }
  return count;
}

// Maintain word count per word.
// Note how the =? operator initializes each map entry
// to an appropriate value (0 in this case)

map[str,int] wordCount3(str S){
  map[str,int] allCounts = ();

 for(/<word:[a-zA-Z0-9]+>/: S){
       /* allCounts[word] ? 0 += 1; */
	/* This is the same as */
       allCounts[word] = (allCounts[word] =? 0) + 1;
  }
  return allCounts;
}