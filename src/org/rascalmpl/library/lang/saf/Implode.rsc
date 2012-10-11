module lang::saf::Implode

import lang::saf::AST;
import lang::saf::SAF;

import ParseTree;

// start[lang::saf::SAF::Fighter] 
public lang::saf::AST::Fighter implode(Tree pt) 
  = implode(#lang::saf::AST::Fighter, pt);

