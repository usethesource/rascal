module experiments::Chart::barchart

import Chart;

public void main(){
           
  barchart("Experiment",                         // title
           "Quarters",                           // domainLabel
            "Sales",                             // rangeLabel
            ["2001", "2002"],                    //series
            ["First Quarter", "Second Quarter"], //Categories
            [[20, 25],                           // actual data.
              [40, 60]]);
}