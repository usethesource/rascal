@doc{
Synopsis: provides language independent lines of code metrics on top of [M3] models.
}
module analysis::m3::metrics::LOC

import lang::java::m3::Facts;
import lang::java::m3::JavaM3;
import analysis::graphs::Graph;

int countProjectTotalLoc(M3 model) = (0 | it + countFileTotalLoc(m, cu) | cu <- top(model@containment));

int countProjectCommentedLoc(M3 model) = (0 | it + countCommentedLoc(m, cu) | cu <- top(model@containment));

int countFileTotalLoc(M3 projectModel, loc cu) = src.end.line when {src} := projectModel@declarations[cu];
default int countFileTotalLoc(M3 projectModel, loc cu) { throw ("Multiple source for compilation unit <cu> found."); }

int countCommentedLoc(M3 projectModel, loc cu) 
  = (0 | it + (doc.end.lint - doc.begin.line + 1 - checkForSourceLines(doc)) | doc <- projectModel@documentation[cu]); 

private int checkForSourceLines(loc commentLoc) {
	str comment = readFile(commentLoc);
	
	// We will check to see if there are any source code in the commented lines
	loc commentedLines = commentLoc;
	// start from start of the line
	commentedLines.begin.column = 0;
	// increase to the next line to cover the full line
	commentedLines.end.line += 1;
	// we won't take any character from the next line
	commentedLines.end.column = 0;
	
	list[str] contents = readFileLines(commentedLines);

	str commentedLinesSrc = intercalate("\n", contents);
	
	// since we look till the start of the next line, we need to make sure we remove the extra \n from the end	
	if (isEmpty(last(contents)))
		commentedLinesSrc = replaceLast(commentedLinesSrc, "\n" , "");
	
	if (comment == trim(commentedLinesSrc))
		return 0;
	return 1; 
}

int countEmptyLoc(M3 projectModel, loc cu) 
  =	(0 | it + 1 | loc doc <- projectModel@declarations[cu], /^\s*$/ <- split("\n", readFile(doc)));

int countProjectEmptyLoc(M3 projectModel) 
  = (0 | it + countEmptyLoc(projectModel, cu) | cu <- top(projectModel@containment));

int countSourceLoc(M3 projectModel, loc cu) 
  =	countFileTotalLoc(projectModel, cu) - countCommentedLoc(projectModel, cu) - countEmptyLoc(projectModel, cu);

int countProjectSourceLoc(M3 projectModel) 
  = countProjectTotalLoc(projectModel) - countProjectCommentedLoc(projectModel) - countProjectEmptyLoc(projectModel);

