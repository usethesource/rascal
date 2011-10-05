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
import IO;
import Graph;
import experiments::RascalTutor::CourseModel;
import experiments::RascalTutor::CourseCompiler;


public list[loc] getCourseFiles(ConceptName rootConcept){
  return crawl(catenate(courseDir, rootConcept), conceptExtension);
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
  compileCourse(rootConcept, "regenerate");
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

