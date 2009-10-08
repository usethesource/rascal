module experiments::Processing::tagcloud

import Processing::TagCloud;

map[str,int] words =
    ("Family Guy": 50, "House": 20, "30 Rock" : 10, "The Simpsons" : 10, "The Angry Beavers" : 50,
     "Pete and Pete" : 60, "American Idol" : 80, "Firefly" : 40, "Batman" : 5, "Bob" : 2,
     "Extreme Makeover Home Edition" : 6, "Arrested Development" : 2,
     "That 70's show" : 4, "The Price is Right" : 8);
     
public void main(){
  tagcloud(words);
}
              