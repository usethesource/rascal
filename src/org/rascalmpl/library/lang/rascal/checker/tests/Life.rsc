module lang::rascal::checker::tests::Life

import vis::Figure;
import vis::Render;
import Set;

alias Coordinate = tuple[int,int];
alias Dimensions = tuple[int,int];

bool inside(Coordinate c, Dimensions d){ return c[0] >= 0 && c[0] < d[0] && c[1] >= 0 && c[1] < d[1]; } 

set[Coordinate] adjacentCoordinates(Coordinate c){ return {<i,j> | i <- [c[0]-1..c[0]+2] , j <- [c[1]-1..c[1]+2], i != c[0] || j != c[1]}; }

alias AliveCells = set[Coordinate];

int numberOfAliveNeighbours(AliveCells alive, Coordinate c) {  return ( 0 | it + 1 | c1 <- adjacentCoordinates(c), c1 in alive);}

bool aliveInNextGeneration(AliveCells alive, Coordinate c) {
	int numNeighbours = numberOfAliveNeighbours(alive,c);
	return numNeighbours == 3 || ( c in alive &&  numNeighbours == 2 );
} 
		
public AliveCells aliveInNextGeneration(AliveCells alive) {
	set[Coordinate] toCheck = {*adjacentCoordinates(c) | c <- alive} + alive ;
	return {c | c <- toCheck, aliveInNextGeneration(alive,c)};
}

public AliveCells killOutSideBoard(Dimensions dimensions, AliveCells aliveCells) {
	return { alive | alive <- aliveCells, inside(alive,dimensions)};
}

public AliveCells nextGeneration(Dimensions dimensions, AliveCells alive) {
	return killOutSideBoard(dimensions,aliveInNextGeneration(alive));
}



public list[list[bool]] toBoard(Dimensions dimensions,AliveCells alive) {
	return for( i <- [0..dimensions[0]] ) {
				append for(j <- [0..dimensions[1]]){
						append <i,j> in alive;
					   };
			};
} 


public AliveCells glider() { return  { <i,4> | i <- [3..6] } + {<5,5> , <4,6>}; }


public void renderLifeOld(Dimensions dimensions,AliveCells firstGeneration) {

	currentGeneration = firstGeneration;

	Figure createBox(Coordinate coordinate){
		return if(coordinate in currentGeneration) {
			box(size(10,10), 
			fillColor("red" ),
			onClick(void () { currentGeneration-=coordinate; } ));
		} else {
			box(size(10,10), 
			fillColor("white"),
			onClick(void () { currentGeneration+=coordinate; } ));
		}
		
	}


	Figure lifeFigure(Dimensions dimensions, AliveCells alive) {
		collumns = for( i <- [0..dimensions[0]] ) {
				 	append vcat([ createBox(<i,j>) | j <- [0..dimensions[1]]],gap(0));
				 };
		return hcat(collumns,gap(0));
	}

	but = button("next",void () {  currentGeneration = nextGeneration(dimensions,currentGeneration);},fillColor("white"));
	life = computeFigure(Figure () {return lifeFigure(dimensions, currentGeneration);} ); 
	render(vcat([life, but],gap(15)));
}



public void renderLife(Dimensions dimensions,AliveCells firstGeneration) {

	currentGeneration = firstGeneration;

	Figure createBox(Coordinate coordinate){
		return box(size(10,10), 
			   fillColor(Color () { return coordinate in currentGeneration ? color("red") : color("white"); } ),
			   onClick(void () { 
			   			if(coordinate in currentGeneration) 
			   				currentGeneration-={coordinate};
			   			else  
			   				currentGeneration+={coordinate};
			   		})
			   	);
		
	}


	Figure lifeFigure(Dimensions dimensions, AliveCells alive) {
		collumns = for( i <- [0..dimensions[0]] ) {
				 	append vcat([ createBox(<i,j>) | j <- [0..dimensions[1]]],gap(0));
				 };
		return hcat(collumns,gap(0));
	}

	but = button("next",void () {  currentGeneration = nextGeneration(dimensions,currentGeneration);},fillColor("white"));
	life = lifeFigure(dimensions, currentGeneration); 
	render("Game of Life", vcat([life, but],gap(15)));
}

