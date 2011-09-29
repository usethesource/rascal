@license{
  Copyright (c) 2009-2011 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Paul Klint - Paul.Klint@cwi.nl - CWI}
module experiments::RolePlay

import IO;
import List;

// Representation of an Ensemble

data Ensemble = ensemble(Play play, list[Actor] actors, rel[Actor, Role] playsRole);
alias Role    = str;
alias Actor   = str;
data Play     = play(list[Act] acts);
data Act      = act(list[Scene] scenes);
data Scene    = scene(list[Speech] speeches);
data Speech   = speech(Role role, str text);

// List the acts an actor has an appearance in

public rel[Actor,Act] appearance(Ensemble ens){
   return {<actor, act> | Act act <- ens.play.acts, 
                          <Actor actor, Role role> <- ens.playsRole, 
                          role in rolesOf(act)};
}

// Get the roles in an act

private set[Role] rolesOf(Act act){
   return {role | /speech(role, txt) <- act};
}
//-------------------------------------------------

// Get role script for a given role

public list[Speech] roleScriptOf(Play play, Role role){
      return [speechOf(speeches, role) | /scene(speeches) <- play];
}

// Get the speeches for a given role from a list of speeches

private list[Speech] speechOf(list[Speech] speeches, Role role){
     if([S1*, speech(role, t1), S2*, speech(role, t2), S3*] := speeches &&
        noRoleOccurrence(S1, role) && noRoleOccurrence(S3, role))
        return [tail(S1, 1) ? [], speech(role,t1), S2, speech(role, t2), head(S3) ? []];
     return [];
}

// Check that a role does not occur in a list of speeches

private bool noRoleOccurrence(list[Speech] speeches, Role role){
   return !(/speech(role, _) := speeches);
}



//---------  Tests -------------

Role firstWitch = "firstWitch";
Role secondWitch ="secondWitch";
Role thirdWitch ="thirdWitch";

Actor cane = "cane";
Actor hopkins = "hopkins";
Actor dalton = "dalton";

list[Actor] actors = [cane, hopkins, dalton];

rel[Actor, Role] playsRole = {<cane, firstWitch>, 
                              <hopkins, secondWitch>, 
                              <dalton, thirdWitch>};

Scene actOneFirst = scene( [speech(firstWitch,  "When shall we ..."),
				            speech(secondWitch, "When the hurlyburly's ..."),
			                speech(thirdWitch,  "That will be ..."),
			                speech(firstWitch,  "Where the place?"),
			                speech(secondWitch, "Upon the heath."),
			                speech(thirdWitch,  "There to meet ...")]);
Scene actTwoFirst = scene([ speech(secondWitch, "Here to test!") ]);

Act first = act([actOneFirst]);
Act second = act([actTwoFirst]);
public Play macbeth = play([first, second]);
public Ensemble dagstuhlRascals = ensemble(macbeth, actors, playsRole);

public void tst(){
	println("appearance: <appearance(dagstuhlRascals)>");
	println("<roleScriptOf(macbeth, secondWitch)>");
}




