module CarFDL

import Set;

type str feature;

set[feature] Transmission = { "automatic", "manual" };
set[set[feature]] Engine = power({"electric", "gasoline"}); //   {{"electric"}, {"gasoline"}, {"electric", "gasoline"}};
set[feature] HorsePower = {"lowPower", "mediumPower", "highPower"};

set[feature] PullsTrailerOpt = {"pullsTrailer", "pullsNoTrailer"};

set[set[feature]] CarFeatures = { {T} + E + {H} + {PT} | 
			feature T : Transmission, 
			set[feature] E : Engine, 
			feature H : HorsePower, 
			feature PT : PullsTrailerOpt, 
			(PT == "pullsTrailer") ==> (H == "highPower")
};

public int ncf(){
	return 	size(CarFeatures);
}

