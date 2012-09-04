@license{
  Copyright (c) 2009-2011 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}

@contributor{Paul Klint - Paul.Klint@cwi.nl - CWI}

@bootstrapParser
module CourseMaintainer

import String;
import List;
import Set;
import Map;
import IO;
import  analysis::graphs::Graph;
import CourseModel;
import CourseCompiler;
import CourseManager;


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
