@license{
  Copyright (c) 2009-2011 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}

@contributor{Paul Klint - Paul.Klint@cwi.nl - CWI}

module experiments::RascalTutor::CourseMaintainer

import String;
import List;
import Set;
import Map;
import IO;
import Graph;
import experiments::RascalTutor::CourseModel;
import experiments::RascalTutor::CourseCompiler;
import experiments::RascalTutor::CourseManager;

// Compile a complete course

public void compileCourse(ConceptName rootConcept){
  if(rootConcept in listEntries(courseDir)){
     compileCourse(rootConcept);
  } else
     throw "Course <rootConcept> not found";
}

// Compile one concept

public void compileConcept(ConceptName cn){
  file = conceptFile(cn);
  compileAndGenerateConcept(file, false);
}

// Compile one concept as an exam

public void compileConceptAsExam(ConceptName cn){
  file = conceptFile(cn);
  compileConceptAsExam(file);
}

loc resultDir = |file:///Users/paulklint/exam-results|;
str resultExtension = ".examresults";
str eol = "/|*|/";

private map[str,str] processFile(loc file){
   res = ();
   for(line <- readFileLines(file)){
      if(/<key:[^\t]+>\t<val:.+><eol>$/ := line){
         println("key = <key>, val = <val>");
         res[key] = val;
      } else
         throw "Unexpected line: <line>";
   }
   return res;
}

public list[examResult] processExams(loc resultDir){
  seen = {};
  return 
	  for(str entry <- listEntries(resultDir))
	      if(endsWith(entry, resultExtension)){
	         res = validateExam(processFile(resultDir + entry));
	         if(res.studentMail notin seen){
	            seen += {res.studentMail};
	            append res;
	         }
	       };
}

bool leq(str a, str b){
    if(size(a) == size(b))
       return a <= b;
    return size(a) < size(b);
}

public void validateExams(){
  scores = processExams(resultDir);
  for(sc <- scores){
      println("<sc.studentName>;<sc.score><for(q <- sc.evaluation){>;<sc.evaluation[q]><}>");
  }
  
  for(sc <- scores){
      println("mail -s \"Exam Results\" <sc.studentMail> \<\<--eod--");
      println("Dear <sc.studentName>,
              '
              '<sc.score >= 6.0 ? "Congratulations you have passed the exam!" : "Unfortunately you did not pass this exam.">
              'Your final score is <sc.score>.
              '
              'The scores for individual exercises are:
              '<for(q <- sort(toList(domain(sc.evaluation)), leq)){><q>:\t<sc.evaluation[q]>\n<}>
              '
              'Best regards,
              '
              'The Rascal Team
              ");
      println("--eod--");
  }
}

/*
 * Compute statistics on section occurrence in a course
 */

public void statistics(ConceptName rootConcept){
  courseFiles = getCourseFiles(rootConcept);
  
  map[str,int] stats = ();
  for(sectionName <- sectionKeywords){
     stats[sectionName] = 0;
   }
  
  for(file <- courseFiles){
   sections = getSections(readFileLines(file));
   for(sectionName <- sectionKeywords){
    if(sections[sectionName] ? && size(sections[sectionName]) > 0)
      stats[sectionName] += 1;
   }
  }
  
  nconcepts = size(courseFiles);
  for(sectionName <- sectionKeywords){
    perc = substring("<100.0*stats[sectionName]/nconcepts>", 0, 3);
  	println("<left(sectionName, 15)><perc>%");
  }
}

/*
 * List all missing sections with given name in a course.
 */

public void missingSection(ConceptName rootConcept, str section){
   courseFiles = getCourseFiles(rootConcept);
   n = 0;
   println("Concepts with missing <section>:");
   for(file <- courseFiles){
       sections = getSections(readFileLines(file));
       if(!sections[section]? || sections[section] == []){
          println(file);
          n += 1;
       }
   }
   if(n == 0)
      println("NONE!");
}

/*
 * List all sections with given name in a course.
 */

public void listSection(ConceptName rootConcept, str section){
   courseFiles = getCourseFiles(rootConcept);
   for(file <- courseFiles){
       sections = getSections(readFileLines(file));
       if(sections[section]? && sections[section] != []){
         cn = getFullConceptName(file);
          println("<left(basename(cn), 25)>: <for(ln <- sections[section]){><ln><}> (<cn>)");
       }
   }
}

/*
 * Create a new course
 */

public void createNewCourse(ConceptName rootConcept){
  root = catenate(courseDir, rootConcept);
  mkDirectory(root);
  cpFile = catenate(root, "<rootConcept>.concept");
  writeFile(cpFile,  mkConceptTemplate(rootConcept));
  compileCourse(rootConcept);
}

// Some older maintenance tasks, generalize or throw away.

public void deleteCategories(ConceptName rootConcept){
   courseFiles = getCourseFiles(rootConcept);
   
    for(file <- courseFiles){
      sections = getSections(readFileLines(file));
      newConcept = [];
      for(sectionName <- sectionKeywords){
                       if(sectionName == "Name"){
                          newConcept += ["<sectionName>: " + sections[sectionName][0]];
                       } else
                       if(sections[sectionName]? && sectionName != "Categories"){
                          newConcept += ["<sectionName>:"] + sections[sectionName];
                       }
                       newConcept += [""];
                   };
      println("<for(ln <- newConcept){><ln>\n<}>");
      writeFile(file, combine(newConcept));
    }
}

public void reorder(ConceptName rootConcept){
   courseFiles = getCourseFiles(rootConcept);
   
    for(file <- courseFiles){
      sections = getSections(readFileLines(file));
      newConcept = [];
      for(sectionName <- sectionKeywords){
                       if(sectionName == "Name"){
                          newConcept += ["<sectionName>: " + sections[sectionName][0]];
                       } else
                       if(sections[sectionName]?){
                          newConcept += ["<sectionName>:"] + sections[sectionName];
                       } else
                          newConcept += ["<sectionName>:"];
                       newConcept += [""];
                   };
      println("<for(ln <- newConcept){><ln>\n<}>");
      writeFile(file, combine(newConcept));
    }
}

