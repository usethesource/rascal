@license{
  Copyright (c) 2009-2011 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}

@contributor{Paul Klint - Paul.Klint@cwi.nl - CWI}

@bootstrapParser
module ExamManager

import String;
import List;
import Set;
import Map;
import IO;
import util::Math;
import lang::csv::IO;
import analysis::graphs::Graph;
import CourseModel;
import CourseCompiler;
import CourseManager;
import ValueIO;

import Type;

loc resultsDir = examsDir + "results/";
str resultExtension = ".examresults";
str eol = "/|*|/";

private str examConceptName = "";
private set[examResult] scores = {};        // all scores for all students
private list[str] sortedqs = [];            // sorted list of question names
private map[str,set[str]] goodAnswers = (); // harvested good answers
private map[str,set[str]] badAnswers = ();  // harvested bad answers


// Start validation of an exam
public void main(){
   validateExam("AP2012/Test1");
}

public void validateExam(str cn){
  bool leq(str a, str b) = (size(a) == size(b)) ? a <= b : size(a) < size(b);
	
  examConceptName = cn;
  resultsDir = examsDir + "results/" + cn;
  scores = processSubmissions(resultsDir);
  println(scores);
 
  sortedqs = sort(toList({*domain(sc.answers) | sc <- scores}), leq);
  
  collectAnswers();
  createReview();
 
  println("1. Review the results in <resultsDir + "Reviews.csv">");
  println("2. Save changes in <resultsDir + "Reviews.csv">");
  println("3. Call validateExam2(\"<cn>\") to complete processing of this exam");
}

public void validateExam2(str cn){
  resultsDir = examsDir + "results/" + cn;

  scores = update();
  createStatistics();
  createResults();
  createMailing();
  println("ValidateExams ... done!");
}

// Process the raw submissions and evaluate all answers

public set[examResult] processSubmissions(loc results){
	private map[str,str] getExamResults(loc file){
	   res = ();
	   for(line <- readFileLines(file)){
	      if(/<key:[^\t]+>\t<val:.+><eol>$/ := line){
	         res[key] = val;
	      } else
	         throw "Unexpected line: <line>";
	   }
	   return res;
	}
  isExam = true;

  seenResults = ();
  for(str entry <- listEntries(results))
      if(endsWith(entry, resultExtension)){
         timestamp = "99999999999";
         if(/_<stamp:[0-9]+>\.examresult/ := entry){
           timestamp = stamp;
           println("<entry>: timestamp = <timestamp>");
         } else
           println("<entry>: no stamp found");
            
         cur = validateExamSubmission(timestamp, getExamResults(results + entry));
         studentMail = cur.studentMail;
         if(!seenResults[studentMail]?){
            seenResults[studentMail] = cur;
         } else {
           prev = seenResults[studentMail];
           if(prev.timestamp < cur.timestamp){
             println("<studentMail>: REPLACE <prev.timestamp> by <cur.timestamp>");
             seenResults[studentMail] = cur;
           }
           println("<studentMail>: KEEP LATEST: <cur.timestamp>");
         }
       };
  return range(seenResults);
}

public void collectAnswers(){
  goodAnswers = ();
  badAnswers = ();
  
  for(sc <- scores){
      for(q <- sc.points)
          if(sc.points[q] > 0)
            goodAnswers[q] = (goodAnswers[q] ? {}) + {sc.answers[q]};
          else
            badAnswers[q] = (badAnswers[q] ? {}) + {sc.answers[q]};
  }
  
  println("goodAnswers: <goodAnswers>
          'badAnswers: <badAnswers>");
}

alias reviewType = tuple[str Question, num Score, str Wrong, str Expected, str Comment];

void createReview(){
  set[reviewType] reviews = {};
 
  println("*** createReview ***");
  seenAnswers = {};
  for(sc <- scores){
	  for(q <- sortedqs){
	      ans = (sc.answers[q]) ? "none";
	      if(!(sc.points[q])? || sc.points[q] == 0){
	         if(<q, ans> notin seenAnswers){
	          ga = (goodAnswers[q])? ? intercalate(" OR ", toList(goodAnswers[q])) : "";
	          println(sc.expectedAnswers[q]);
	          if(sc.expectedAnswers[q]?)
	             ga += sc.expectedAnswers[q];
	          reviews += {<q, 0.0, ans, ga, "">};
	          seenAnswers += {<q, ans>};
	        }
	      }
	  }   
  }
  writeCSV(reviews, resultsDir + "Review.csv", ("separator" : ";"));
}

public set[examResult] update(){
  reviews = readCSV(#set[reviewType], resultsDir + "Review.csv", ("separator" : ";"));
  println("reviews = <typeOf(reviews)>: <reviews>");
  
  rmap = (rev.Question + rev.Wrong : rev | rev <- reviews, rev.Score != 0);
  nquestions = size(sortedqs);
  uscores = for(sc <- scores){
              score = 0.0;
              points = sc.points;
              comments = sc.comments;
	          for(q <- sortedqs){
	             ans = (sc.answers[q]) ? "none";
	             if(rmap[q + ans]?){
	                review = rmap[q + ans];
	                points[q] = review.Score;
	                comments[q] = review.Comment;
	             }
	             score += points[q];
	          }
	          append examResult(sc.studentName, sc.studentMail, sc.StudentNumber, sc.timestamp, 
                                sc.answers, sc.expectedAnswers, comments, points, 10.0 * score / nquestions);
	       };
  scores = toSet(uscores);
  println("updated scores: <scores>");
  return scores;
}

public void createStatistics(){

  allscores = [sc.score | sc <- scores];
  nstudents = size(allscores);
  npassed =  (0 | it + 1 | sc <- scores, sc.score >= 6.0);
  
  // Compute averages per question
  qavg = ();
  for(sc <- scores){
      for(q <- sc.points)
          qavg[q] ? 0.0 += (sc.points[q] > 0) ? 1 : 0;
  }
  for(q <- qavg){
      qavg[q] = (100 * qavg[q]) / nstudents;
  }
  
  writeFile(resultsDir + "Statistics.txt",
  		  "Number of students: <nstudents>
  		  'Lowest score:       <round(min(allscores), 0.1)>
          'Highest score:      <round(max(allscores), 0.1)>
          'Average score:      <round((0 | it + n | num n <- allscores)/nstudents, 0.1)>
          'Passed:             <npassed>
          'Percentage passed:  <round(npassed*100.0/nstudents, 0.1)>
          '
          'Average score per question:
          '<for(q <- sortedqs){><q>:\t<round(qavg[q], 0.1)>\n<}>
          '
          'Good/bad answers per question:
          '<for(q <- sortedqs){><q>:\n\tgood:<goodAnswers[q]?"{}">\n\tbad:<badAnswers[q]?"{}">\n<}>
          ");
  println("Written Statistics.txt");
}

void createResults(){
 writeFile(resultsDir + "Results.csv",
          "<for(sc <- scores){>
          '<sc.studentName>;<round(sc.score, 0.1)><for(q <- sortedqs){>;<sc.points[q]?"fail"><}><}>"
          );
  println("Written Results.csv");    
}

void createMailing(){
writeFile(resultsDir + "Mailing.sh",
          "<for(sc <- scores){>
          'mail -s \"Exam Results\" <sc.studentMail> \<\<==eod==
          'Dear <sc.studentName>,
          '
          '<sc.score >= 6.0 ? "Congratulations you have passed this exam!" :  "Unfortunately you did not pass this exam.">
          'Grade: <round(sc.score, 0.1)>
          '
          'The scores for individual questions are:
          '<for(q <- sortedqs){><q>: \t<sc.points[q]?"fail">\n<}>
          '<summaryWrongAnswers(sc, goodAnswers)>
          'Best regards,
          '
          'The Rascal Team
          '==eod==
          <}>
          ");
   println("Written Mailing.sh");
}

str summaryWrongAnswers(examResult sc, map[str,set[str]] goodAnswers){
  s = "<for(q <- sortedqs){><suggestGoodAnswer(sc,q, goodAnswers)><}>";
  return s == "" ? "" : "Detailed summary of questions you have not answered correctly:\n<s>";
}

str suggestGoodAnswer(examResult sc, str q, map[str,set[str]] goodAnswers){
  if(sc.points[q]? && sc.points[q] == 1)
     return "";
     
  ans = "\n<q>:\n\nYour (incorrect) answer: <sc.answers[q]?"no answer given">\n";
   
  comment = sc.comments[q] ? "";
  if(comment != "")
     comment = "\nComment: <comment>";
    
  if(sc.expectedAnswers[q]? && goodAnswers[q]? && sc.expectedAnswers[q] notin goodAnswers[q])
     ans += "\nFeedback: <sc.expectedAnswers[q]>\n";
 
  if(goodAnswers[q]?)
     ans += "\nSuggested good answer(s): <intercalate(" OR ", toList(goodAnswers[q]))>\n";
     
  return  ans + comment;
  //return "<ans>No correct answers available\n<comment>\n";
}