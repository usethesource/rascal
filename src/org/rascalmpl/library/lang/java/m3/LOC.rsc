module lang::java::m3::LOC

import lang::java::m3::Facts;
import lang::java::m3::JavaM3;
import Prelude;

public int countFileTotalLoc(M3 projectModel, loc cu) {
	set[loc] fileSource = projectModel@source[cu];
	if (size(fileSource) > 1)
		throw ("Multiple source for compilation unit <cu> found.");
	return getOneFrom(fileSource).end.line;
}

public int countProjectTotalLoc(M3 projectModel) {
	int result = 0;
	set[loc] compilationUnits = files(projectModel@containment);
	for (loc cu <- compilationUnits) {
		result += countFileTotalLoc(projectModel, cu);
	}
	return result;
}

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

public int countCommentedLoc(M3 projectModel, loc cu) {
	int result = 0;
	set[loc] fileSource = projectModel@documentation[cu];
	for(loc doc <- fileSource) {
		int commentsWithSource = checkForSourceLines(doc);
		result += doc.end.line - doc.begin.line + 1 - commentsWithSource;
	}
	return result;
}

public int countProjectCommentedLoc(M3 projectModel) {
	int result = 0;
	set[loc] compilationUnits = files(projectModel@containment);
	for (loc cu <- compilationUnits) {
		result += countCommentedLoc(projectModel, cu);
	}
	return result;
}

public int countEmptyLoc(M3 projectModel, loc cu) {
	int result = 0;
	set[loc] fileSource = projectModel@source[cu];
	for(loc doc <- fileSource) {
		list[str] fileLines = split("\n", readFile(doc));
		for (str line <- fileLines)
			if (/^\s*$/ := line)
				result += 1;
	}
	return result;
}

public int countProjectEmptyLoc(M3 projectModel) {
	int result = 0;
	set[loc] compilationUnits = files(projectModel@containment);
	for (loc cu <- compilationUnits) {
		result += countEmptyLoc(projectModel, cu);
	}
	return result;
}

public int countSourceLoc(M3 projectModel, loc cu) {
	return countFileTotalLoc(projectModel, cu) - countCommentedLoc(projectModel, cu) - countEmptyLoc(projectModel, cu);
}

public int countProjectSourceLoc(M3 projectModel) {
	return countProjectTotalLoc(projectModel) - countProjectCommentedLoc(projectModel) - countProjectEmptyLoc(projectModel);
}

public int countLoc(rel[value, loc] rels) {
	set[loc] compilationUnits = {};
	visit(rels) {
		case <loc decl, loc src>: {
			if (isCompilationUnit(decl))
				compilationUnits += src;
		}
	}
	int result = 0;
	for (loc cu <- compilationUnits) {
		result += cu.end.line;
	}
	return result;
}
