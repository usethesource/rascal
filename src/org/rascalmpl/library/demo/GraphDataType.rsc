@license{
  Copyright (c) 2009-2011 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
module demo::GraphDataType

// A data declaration for a graph data type that supports various shapes and attributes.
// Observe how various names are escaped with \ because they contain non-identifier 
// characters (e.g., bounding-box) or conflict with Rascal keywords (e.g., node)

data Point = point(int x, int y);

alias Polygon = list[Point];

alias File = str;

data Color = rgb(int red, int green, int blue);

data Style = bold() | dashed() | dotted() | filled() | invisible() | solid();

data Shape = box() | circle() | diamond() | egg() | elipse() | hexagon() | 
             house() | octagon() | parallelogram() | plaintext() | 
             trapezium() | triangle();

data Direction = forward() | back() | both() | none();

data Attribute = \bounding-box(Point first, Point second);
data Attribute = \color(Color color);
data Attribute = \curve-points(Polygon points);
data Attribute = \direction(Direction direction);
data Attribute = \fill-color(Color color);
data Attribute = \info(str key, value val);
data Attribute = \label(str label);
data Attribute = \tooltip(str tooltip);
data Attribute = \location(int x, int y);
data Attribute = \shape(Shape shape);
data Attribute = \size(int width, int height);
data Attribute = \style(Style style);
data Attribute = \level(str level);
data Attribute = \file(File \file);

alias AttributeList = list[Attribute] ;

data NodeId = id(node id);

data Edge = edge(NodeId from, 
                 NodeId to, 
                 AttributeList attributes);
               
alias EdgeList = list[Edge];

alias Polygon = list[Point];

data Node = \node(NodeId id,  
                  AttributeList attributes);
               
alias NodeList = list[Node];

data Graph = graph(NodeList nodes, 
                   EdgeList edges, 
                   AttributeList attributes);

data Node = subgraph(NodeId id, 
                     NodeList nodes, 
                     EdgeList edges, 
                     AttributeList attributes);      
                   
