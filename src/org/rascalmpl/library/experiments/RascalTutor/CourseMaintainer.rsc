@license{
  Copyright (c) 2009-2011 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}

@contributor{Paul Klint - Paul.Klint@cwi.nl - CWI}

module experiments::RascalTutor::CourseMaintainer

import experiments::RascalTutor::CourseModel;
import experiments::RascalTutor::CourseCompiler;
import String;
import Set;
import List;
import Relation;
import Map;
import Graph;
import IO;
import DateTime;
import experiments::RascalTutor::HTMLUtils;
import experiments::RascalTutor::HTMLGenerator;
import experiments::RascalTutor::ValueGenerator;


public list[loc] getCourseFiles(ConceptName rootConcept){
  return crawl(catenate(courseDir, rootConcept), conceptExtension);
}

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