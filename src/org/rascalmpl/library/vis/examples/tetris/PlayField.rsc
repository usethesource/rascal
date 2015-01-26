@license{
  Copyright (c) 2009-2015 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Atze van der Ploeg - ploeg@cwi.nl - CWI}

module vis::examples::tetris::PlayField

import List;

alias Coordinate = tuple[int row,int col];
alias Coordinates = list[Coordinate];
alias Color = int;

data PlayFieldState 
   = empty()
   | block(Color color)
   | prediction(Color color); // only used in the onscreen board
   
data Row         = row(list[PlayFieldState] elems);   
alias PlayField  = list[Row];
alias RowIndexes = list[int];

public int nrRows(PlayField b)            = size(b); 
public int nrCols(PlayField b)             = size(b[0].elems); 
public int maxRowIndex(PlayField b)      = nrRows(b)-1;
public int maxColIndex(PlayField b)      = nrCols(b)-1;
public list[int] rowIndexes(PlayField b) = [0 .. maxRowIndex(b)+1 ];
public list[int] colIndexes(PlayField b) = [0 .. maxColIndex(b)+1 ];

public PlayField emptyPF(int rows,int cols) =
    [emptyPFRow(cols) | _ <- [1,2..rows+1] ];
Row emptyPFRow(int cols) = row([empty() | _ <- [1..cols+1]]);
   
bool isRowFull(Row r) = (true | it && (block(_) := e) | e <- r.elems);

public RowIndexes fullRows(PlayField b) =  
   [ r | r <- rowIndexes(b), isRowFull(b[r])];
   
public PlayField removeRows(PlayField b, RowIndexes rows) =
   addEmptyRowsToTop(keepRows(b,rowIndexes(b) - rows),size(rows));
   
PlayField keepRows(PlayField b, RowIndexes rows) = [b[i] | i <- rows];

PlayField addEmptyRowsToTop(PlayField b, int nr) = 
   emptyPF(nr,nrCols(b)) + b;

bool onBoard(PlayField b, Coordinate c) =
   c.row >= 0 && c.row < nrRows(b) && c.col >= 0 && c.col < nrCols(b);
bool isEmpty(PlayField b, Coordinate c) = onBoard(b,c) && empty() := getPF(b,c);
   
public PlayFieldState getPF(PlayField b, Coordinate c) =  b[c.row].elems[c.col];

PlayField setPF(PlayField b, Coordinate c, PlayFieldState s) {
   b[c.row].elems[c.col] = s;
   return b;
}

public bool isEmpty(PlayField b, Coordinates cs) =
   (true | it && isEmpty(b,c) | c <- cs);

public PlayField add(PlayField b, Coordinates cs, PlayFieldState s) =
   (b | setPF(it,c,s) | c <- cs);
