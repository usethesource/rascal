module experiments::scm::Timer

@doc{Starts a timer}
@javaClass{org.rascalmpl.library.experiments.scm.Timer}
public datetime java startTimer();

@doc{Stops the last timer}
@javaClass{org.rascalmpl.library.experiments.scm.Timer}
public int java stopTimer();

@doc{Gets the current datetime as an int}
@javaClass{org.rascalmpl.library.experiments.scm.Timer}
public int java currentDate();

@doc{Return the difference between two dates and/or datetimes in hours.} 
@javaClass{org.rascalmpl.library.experiments.scm.Timer}
public int java hoursDiff(datetime start, datetime end);

@doc{Return the difference between two dates and/or datetimes in minutes.} 
@javaClass{org.rascalmpl.library.experiments.scm.Timer}
public int java minutesDiff(datetime start, datetime end);

@doc{Return the difference between two dates and/or datetimes in seconds.} 
@javaClass{org.rascalmpl.library.experiments.scm.Timer}
public int java secondsDiff(datetime start, datetime end);

@doc{Return the difference between two dates and/or datetimes in milliseconds.} 
@javaClass{org.rascalmpl.library.experiments.scm.Timer}
public int java millisDiff(datetime start, datetime end);