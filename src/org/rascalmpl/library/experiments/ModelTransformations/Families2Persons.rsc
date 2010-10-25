module experiments::ModelTransformations::Families2Persons

/*
 * Example taken from "ATL Basic Examples and Patterns" at
 * http://www.eclipse.org/m2m/atl/basicExamples_Patterns/
 *
 * See http://www.eclipse.org/m2m/atl/doc/ATLUseCase_Families2Persons.pdf
 */

// Source model: Families

data Member = father(str firstName) 
            | mother(str firstName) 
            | son(str firstName) 
            | daughter(str firstName);
            
data Family = family(str lastName, set[Member] members);

alias Families = set[Family];

// Target model: Persons

data Gender = mr() | mrs();
data Person = person(Gender gender, str firstName, str lastName);
alias Persons = set[Person];

// families2persons

public Persons families2persons(Families families){
   return {person(gender(m), m.firstName, f.lastName) | f <- families, m <- f.members};
}

Gender gender(Member mem){
   switch(mem){
     case father(_):   return mr();
     case mother(_):   return mrs();
     case son(_):      return mr();
     case daughter(_): return mrs();
   } 
}

// Tests

private Families input = { 
                 family("March", {father("Jim"), mother("Cindy"), son("Brandon"), daughter("Brenda")}),
                 family("Sailor",{father("Peter"), mother("Jacky"), son("David"), son("Dylan"), daughter("Kelly")})
               };
private Persons output = { person(mr(),  "Jim",     "March"),
                     person(mrs(), "Cindy",   "March"),
                     person(mr(),  "Brandon", "March"),
                     person(mrs(), "Brenda",  "March"),
                     person(mr(),  "Peter",   "Sailor"),
                     person(mrs(), "Jacky",   "Sailor"),
                     person(mr(),  "David",   "Sailor"),
                     person(mr(),  "Dylan",   "Sailor"),
                     person(mrs(), "Kelly",   "Sailor")
                   };
test families2persons(input) == output;
