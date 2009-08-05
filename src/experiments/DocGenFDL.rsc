module demo::DocGenFDL

import Set;

public set[set[str]] PresentationSpecializations = power({"frameSize"}) - {{}};


public set[str] Interaction = {"crawled", "dynamic"};

public set[str] Localization = {"english", "dutch"};

public set[set[str]] SourceSections = power({"annotationSection", "activationSection", "entitiesSection",  "parametersSection"});

public set[set[str]] UsersGuide = power({ "indexpage", "programHelp", "copybookHelp", "statisticsHelp",
           			   "annotationHelp", "activationHelp", "entitiesHelp", "parametersHelp"});

public set[set[str]] AnalysisSpecializations = power({"callHandlers"}) - {{}}; //,
           			//  "columnPositions", "codingConventions", "fileNameConventions"})

public set[set[str]] RelationSet = {P | set[str] P <- power({"annotationRelation", "callRelation", "entitiesRelation",  "entityOperationRelation"}),
			 	("entityOperationRelation" in P) ==>  ("entitiesRelation" in P)}; //C1

public set[set[str]] Blocks	=  power({"programBlock", "copybookBlock", "statisticsBlock"});

public set[set[str]] MB = { U + B | set[str] U <- UsersGuide, set[str] B <- Blocks ,
			"programBlock" in B ==> "programHelp" in U	// C5
		};

public set[set[str]] AN =  { R + A | set[str] R <- RelationSet, set[str] A <- AnalysisSpecializations };

public set[set[str]] PR =  { {L} + {I} + M + S + P  | 
			   str L <- Localization, str I <- Interaction, set[str] M <-  MainBlocks, set[str] S <- SourceSections,
		
        		   set[str] P <- PresentationSpecializations };
        		   
public set[set[str]] G = { A + P | set[str] A <- Analysis, set[str] P <- Presentation };
        		   
/*
public set[set[str]] DocGen =
	{ DocGen1 | 

	set[str] MainBlocks<- { U + B | set[str] U <- UsersGuide, set[str] B <- Blocks ,
			"programBlock" in B ==> "programHelp" in U	// C5
		},

	set[str] Analysis <- { R + A | set[str] R <- RelationSet, set[str] A <- AnalysisSpecializations },

	set[str] Presentation <-  { {L} + {I} + M + S + P  | 
			   str L <- Localization, str I <- Interaction, set[str] M <-  MainBlocks, set[str] S <- SourceSections,
		
        		   set[str] P <- PresentationSpecializations },

	"entitiesSection" in S ==> ("entitiesRelation" in DocGen1 &&  "entitiesOperationRelation" in DocGen1),

	set[str] DocGen1 <-  { A + P | set[str] A <- Analysis, set[str] P <- Presentation }

//v C1: "entityOperationRelation" in DocGen1 implies "entitiesRelation" in DocGen1,
//  C2	"annotationSection" in DocGen1 implies "annotationRelation" in DocGen1,
//  C3	"activationSection" in DocGen1 implies "callRelation" in DocGen1,
//  C4	"entitiesSection" in DocGen1 implies "entitiesRelation" in DocGen1 and  "entitiesOperationRelation" in DocGen1,
//v C5	"programBlock" in DocGen1 implies "programHelp" in DocGen1,
//  C6	"statisticsBlock" in DocGen1 implies "statisticsHelp" in DocGen1,
//  C7	"annotationSection" in DocGen1 implies "annotationHelp" in DocGen1,
//  C8	"crawled" in DocGen1 implies "annotationRelation" notin DocGen1,
//  C9	"crawled" in DocGen1 implies "annotationSection" notin DocGen1
	
	};
*/

public int nconfigurations = size(DocGen);
