@license{
  Copyright (c) 2009-2011 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}

@contributor{Paul Klint - Paul.Klint@cwi.nl - CWI}

@bootstrapParser
module experiments::RascalTutor::CourseMaintainer

import String;
import List;
import Set;
import Map;
import IO;
import  analysis::graphs::Graph;
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
  compileAndGenerateConcept(cn, false);
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
         //println("key = <key>, val = <val>");
         res[key] = val;
      } else
         throw "Unexpected line: <line>";
   }
   return res;
}

public set[examResult] processExams(loc resultDir){
  seen = ();
  for(str entry <- listEntries(resultDir))
      if(endsWith(entry, resultExtension)){
         timestamp = "99999999999";
         if(/_<stamp:[0-9]+>\.examresult/ := entry){
           timestamp = stamp;
           println("<entry>: timestamp = <timestamp>");
         } else
           println("<entry>: no stamp found");
            
         cur = validateExam(timestamp, processFile(resultDir + entry));
         studentMail = cur.studentMail;
         if(!seen[studentMail]?){
            seen[studentMail] = cur;
         } else {
           prev = seen[studentMail];
           if(prev.timestamp < cur.timestamp){
             println("<studentMail>: REPLACE <prev.timestamp> by <cur.timestamp>");
             seen[studentMail] = cur;
           }
           println("<studentMail>: KEEP LATEST: <cur.timestamp>");
         }
       };
  return range(seen);
}

bool leq(str a, str b){
    if(size(a) == size(b))
       return a <= b;
    return size(a) < size(b);
}

str round(num n){
    s = "<n>";
    if(/<before:[0-9]+>\.<after:[0-9]+>/ := s)
       return "<before>.<after[0]>";
    return s;
}

public void validateExams(){
  scores = processExams(resultDir);
  allscores = [sc.score | sc <- scores];
  nstudents = size(allscores);
  npassed =  (0 | it + 1 | sc <- scores, sc.score >= 6.0);
  
  // Compute averages per question
  qavg = ();
  for(sc <- scores){
      for(q <- sc.evaluation)
          qavg[q] ? 0.0 += (sc.evaluation[q] == "pass") ? 1 : 0;
  }
  for(q <- qavg){
      qavg[q] = (100 * qavg[q]) / nstudents;
  }
  sortedqs = sort(toList(domain(qavg)), leq);
  
  goodAnswers = ();
  badAnswers = ();
  
  for(sc <- scores){
      for(q <- sc.evaluation)
          if(sc.evaluation[q] == "pass")
            goodAnswers[q] = (goodAnswers[q] ? {}) + {sc.answers[q]};
          else
            badAnswers[q] = (badAnswers[q] ? {}) + {sc.answers[q]};
  }
  
  println("goodAnswers: <goodAnswers>
          'badAnswers: <badAnswers>");
  
  writeFile(resultDir + "Statistics.txt",
  		  "Number of students: <nstudents>
  		  'Lowest score:       <round(min(allscores))>
          'Highest score:      <round(max(allscores))>
          'Average score:      <round((0 | it + n | num n <- allscores)/nstudents)>
          'Passed:             <npassed>
          'Percentage passed:  <round(npassed*100.0/nstudents)>
          '
          'Average score per question:
          '<for(q <- sortedqs){><q>:\t<round(qavg[q])>\n<}>
          '
          'Good/bad answers per question:
          '<for(q <- sortedqs){><q>:\n\tgood:<goodAnswers[q]?"{}">\n\tbad:<badAnswers[q]?"{}">\n<}>
          ");
  println("Written Statistics.txt");
          
  writeFile(resultDir + "Results.csv",
          "<for(sc <- scores){>
          '<sc.studentName>;<round(sc.score)><for(q <- sortedqs){>;<sc.evaluation[q]?"fail"><}><}>"
          );
  println("Written Results.csv");     
 /* 
  for(sc <- scores){
     println(sc.studentName);
	  for(q <- sortedqs){
	      if(!sc.evaluation[q]? || sc.evaluation[q] == "fail"){
	        println("question <q>"); 
	        println("Incorrect answer = <(sc.answers[q])?"none">");
	        println("goodAnswers: " + (goodAnswers[q] ? "unknown"));
	      }
	      println("question <q> ... done");
	  }   
  }
  println("Start writing Mailing.sh");
  */
  writeFile(resultDir + "Mailing.sh",
          "<for(sc <- scores){>
          'mail -s \"Exam Results\" <sc.studentMail> \<\<==eod==
          'Dear <sc.studentName>,
          '
          '<sc.score >= 6.0 ? "Congratulations you have passed the exam!" : "Unfortunately you did not pass this exam.">
          '
          'The scores for individual questions are:
          '<for(q <- sortedqs){><q>:\t<sc.evaluation[q]?"fail">\n<}>
          '
          'Detailed summary of failed questions:
          '<for(q <- sortedqs){><suggestGoodAnswer(sc,q, goodAnswers)><}>
          '
          'Best regards,
          '
          'The Rascal Team
          '==eod==
          <}>
          ");
   println("Written Mailing.sh");
   println("ValidateExams ... done!");
}

str suggestGoodAnswer(examResult sc, str q, map[str,set[str]] goodAnswers){
  if(sc.evaluation[q]? && sc.evaluation[q] == "pass")
     return "";
     
  ans = "<q>: Your (incorrect) answer: <sc.answers[q]?"no answer given">\n";
    
  if(sc.expectedAnswers[q]?)
     return "<ans>Expected answer is <sc.expectedAnswers[q]>\n";
  if(goodAnswers[q]?)
     return "<ans>Set of possible good answers: <goodAnswers[q]>\n";
  return "<ans>No correct answers available";
}

/*
 * Compute statistics on section occurrence in a course
 */

public void statistics(ConceptName rootConcept){
  course = getCourse(rootConcept);
  
  map[str,int] stats = ();
  for(sectionName <- sectionKeywords){
     stats[sectionName] = 0;
   }
   nquestions = 0;
  
  for(cn <- course.concepts){
   sections = getSections(cn);
   for(sectionName <- sectionKeywords){
    if(sections[sectionName] ? && size(sections[sectionName]) > 0)
      stats[sectionName] += 1;
      if(sectionName == "Questions")
         nquestions += size(course.concepts[cn].questions);
   }
  }
  
  nconcepts = size(course.concepts);
  for(sectionName <- sectionKeywords){
    perc = substring("<100.0*stats[sectionName]/nconcepts>", 0, 3);
  	println("<left(sectionName, 15)><perc>%");
  }
  println("Concepts:  <nconcepts>");
  println("Questions: <nquestions>");
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
  root = courseDir + rootConcept;
  mkDirectory(root);
  cpFile = root + "<rootConcept>.concept";
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
