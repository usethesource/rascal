module experiments::vis2::vega::ParameterTypes

public data TICKLABELS = tickLabels(int angle = 0,   int dx = 99999, int dy = 99999, 
       int title_dx = 99999, int title_dy = 99999,
       int fontSize = 99999, str fontStyle="italic", str fontWeight="normal",
       str fill = "black"
       ); 
       
 @doc{Create a fixed color palette}
public list[str] color12 = [ "red", "aqua", "navy", "violet", 
                          "yellow", "darkviolet", "maroon", "green",
                          "teal", "blue", "olive", "lime"];  
                          
 public list[str] color12X =   [                     
            "goldenrod",
			"gray",
			"green",
			"khaki",
			"magenta",
			"olive",
			"orange",
			"orchid",
			"red",
			"salmon",
			"seagreen",
			"slateblue",
			"slategray",
			"turquoise",
			"violet"
			];