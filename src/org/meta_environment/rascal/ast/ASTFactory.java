package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
public class ASTFactory
{
  java.util.Map < AbstractAST, AbstractAST > table =
    new java.util.Hashtable < AbstractAST, AbstractAST > ();

  public org.meta_environment.rascal.ast.Body.
    Ambiguity makeBodyAmbiguity (java.util.LisT <
				 org.meta_environment.rascal.ast.Body >
				 alternatives)
  {
    org.meta_environment.rascal.ast.Body.Ambiguity amb =
      new org.meta_environment.rascal.ast.Body.Ambiguity (alternatives);
    if (!table.containsKey (amb))
      {
	table.put (amb, amb);
      }
    return (org.meta_environment.rascal.ast.Body.Ambiguity) table.get (amb);
  }
  public org.meta_environment.rascal.ast.Body.
    Toplevels makeBodyToplevels (ITree tree,
				 java.util.LisT <
				 org.meta_environment.rascal.ast.Toplevel >
				 toplevels)
  {
    org.meta_environment.rascal.ast.Body.Toplevels x =
      new org.meta_environment.rascal.ast.Body.Toplevels (tree, toplevels);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Body.Toplevels) table.get (x);
  }
  public org.meta_environment.rascal.ast.StrChar.
    Lexical makeStrCharLexical (ITree tree, String string)
  {
    org.meta_environment.rascal.ast.StrChar.Lexical x =
      new org.meta_environment.rascal.ast.StrChar.Lexical (tree, string);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.StrChar.Lexical) table.get (x);
  }
  public org.meta_environment.rascal.ast.StrChar.
    Ambiguity makeStrCharAmbiguity (java.util.LisT <
				    org.meta_environment.rascal.ast.StrChar >
				    alternatives)
  {
    org.meta_environment.rascal.ast.StrChar.Ambiguity amb =
      new org.meta_environment.rascal.ast.StrChar.Ambiguity (alternatives);
    if (!table.containsKey (amb))
      {
	table.put (amb, amb);
      }
    return (org.meta_environment.rascal.ast.StrChar.Ambiguity) table.
      get (amb);
  }
  public org.meta_environment.rascal.ast.StrChar.
    newline makeStrCharnewline (ITree tree)
  {
    org.meta_environment.rascal.ast.StrChar.newline x =
      new org.meta_environment.rascal.ast.StrChar.newline (tree);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.StrChar.newline) table.get (x);
  }
  public org.meta_environment.rascal.ast.StrCon.
    Ambiguity makeStrConAmbiguity (java.util.LisT <
				   org.meta_environment.rascal.ast.StrCon >
				   alternatives)
  {
    org.meta_environment.rascal.ast.StrCon.Ambiguity amb =
      new org.meta_environment.rascal.ast.StrCon.Ambiguity (alternatives);
    if (!table.containsKey (amb))
      {
	table.put (amb, amb);
      }
    return (org.meta_environment.rascal.ast.StrCon.Ambiguity) table.get (amb);
  }
  public org.meta_environment.rascal.ast.StrCon.
    Lexical makeStrConLexical (ITree tree, String string)
  {
    org.meta_environment.rascal.ast.StrCon.Lexical x =
      new org.meta_environment.rascal.ast.StrCon.Lexical (tree, string);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.StrCon.Lexical) table.get (x);
  }
  public org.meta_environment.rascal.ast.Sort.
    Ambiguity makeSortAmbiguity (java.util.LisT <
				 org.meta_environment.rascal.ast.Sort >
				 alternatives)
  {
    org.meta_environment.rascal.ast.Sort.Ambiguity amb =
      new org.meta_environment.rascal.ast.Sort.Ambiguity (alternatives);
    if (!table.containsKey (amb))
      {
	table.put (amb, amb);
      }
    return (org.meta_environment.rascal.ast.Sort.Ambiguity) table.get (amb);
  }
  public org.meta_environment.rascal.ast.Sort.
    Lexical makeSortLexical (ITree tree, String string)
  {
    org.meta_environment.rascal.ast.Sort.Lexical x =
      new org.meta_environment.rascal.ast.Sort.Lexical (tree, string);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Sort.Lexical) table.get (x);
  }
  public org.meta_environment.rascal.ast.Symbol.
    CaseInsensitiveLiteral makeSymbolCaseInsensitiveLiteral (ITree tree,
							     org.
							     meta_environment.
							     rascal.ast.
							     SingleQuotedStrCon
							     singelQuotedString)
  {
    org.meta_environment.rascal.ast.Symbol.CaseInsensitiveLiteral x =
      new org.meta_environment.rascal.ast.Symbol.CaseInsensitiveLiteral (tree,
									 singelQuotedString);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Symbol.
	    CaseInsensitiveLiteral) table.get (x);
  }
  public org.meta_environment.rascal.ast.Symbol.
    Literal makeSymbolLiteral (ITree tree,
			       org.meta_environment.rascal.ast.StrCon string)
  {
    org.meta_environment.rascal.ast.Symbol.Literal x =
      new org.meta_environment.rascal.ast.Symbol.Literal (tree, string);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Symbol.Literal) table.get (x);
  }
  public org.meta_environment.rascal.ast.Symbol.
    LiftedSymbol makeSymbolLiftedSymbol (ITree tree,
					 org.meta_environment.rascal.ast.
					 Symbol symbol)
  {
    org.meta_environment.rascal.ast.Symbol.LiftedSymbol x =
      new org.meta_environment.rascal.ast.Symbol.LiftedSymbol (tree, symbol);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Symbol.LiftedSymbol) table.
      get (x);
  }
  public org.meta_environment.rascal.ast.Symbol.
    CharacterClass makeSymbolCharacterClass (ITree tree,
					     org.meta_environment.rascal.ast.
					     CharClass charClass)
  {
    org.meta_environment.rascal.ast.Symbol.CharacterClass x =
      new org.meta_environment.rascal.ast.Symbol.CharacterClass (tree,
								 charClass);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Symbol.CharacterClass) table.
      get (x);
  }
  public org.meta_environment.rascal.ast.Symbol.
    Alternative makeSymbolAlternative (ITree tree,
				       org.meta_environment.rascal.ast.
				       Symbol lhs,
				       org.meta_environment.rascal.ast.
				       Symbol rhs)
  {
    org.meta_environment.rascal.ast.Symbol.Alternative x =
      new org.meta_environment.rascal.ast.Symbol.Alternative (tree, lhs, rhs);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Symbol.Alternative) table.get (x);
  }
  public org.meta_environment.rascal.ast.Symbol.
    IterStarSep makeSymbolIterStarSep (ITree tree,
				       org.meta_environment.rascal.ast.
				       Symbol symbol,
				       org.meta_environment.rascal.ast.
				       StrCon sep)
  {
    org.meta_environment.rascal.ast.Symbol.IterStarSep x =
      new org.meta_environment.rascal.ast.Symbol.IterStarSep (tree, symbol,
							      sep);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Symbol.IterStarSep) table.get (x);
  }
  public org.meta_environment.rascal.ast.Symbol.
    IterSep makeSymbolIterSep (ITree tree,
			       org.meta_environment.rascal.ast.Symbol symbol,
			       org.meta_environment.rascal.ast.StrCon sep)
  {
    org.meta_environment.rascal.ast.Symbol.IterSep x =
      new org.meta_environment.rascal.ast.Symbol.IterSep (tree, symbol, sep);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Symbol.IterSep) table.get (x);
  }
  public org.meta_environment.rascal.ast.Symbol.
    IterStar makeSymbolIterStar (ITree tree,
				 org.meta_environment.rascal.ast.
				 Symbol symbol)
  {
    org.meta_environment.rascal.ast.Symbol.IterStar x =
      new org.meta_environment.rascal.ast.Symbol.IterStar (tree, symbol);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Symbol.IterStar) table.get (x);
  }
  public org.meta_environment.rascal.ast.Symbol.
    Iter makeSymbolIter (ITree tree,
			 org.meta_environment.rascal.ast.Symbol symbol)
  {
    org.meta_environment.rascal.ast.Symbol.Iter x =
      new org.meta_environment.rascal.ast.Symbol.Iter (tree, symbol);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Symbol.Iter) table.get (x);
  }
  public org.meta_environment.rascal.ast.Symbol.
    Optional makeSymbolOptional (ITree tree,
				 org.meta_environment.rascal.ast.
				 Symbol symbol)
  {
    org.meta_environment.rascal.ast.Symbol.Optional x =
      new org.meta_environment.rascal.ast.Symbol.Optional (tree, symbol);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Symbol.Optional) table.get (x);
  }
  public org.meta_environment.rascal.ast.Symbol.
    Sequence makeSymbolSequence (ITree tree,
				 org.meta_environment.rascal.ast.Symbol head,
				 java.util.LisT <
				 org.meta_environment.rascal.ast.Symbol >
				 tail)
  {
    org.meta_environment.rascal.ast.Symbol.Sequence x =
      new org.meta_environment.rascal.ast.Symbol.Sequence (tree, head, tail);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Symbol.Sequence) table.get (x);
  }
  public org.meta_environment.rascal.ast.Symbol.
    Empty makeSymbolEmpty (ITree tree)
  {
    org.meta_environment.rascal.ast.Symbol.Empty x =
      new org.meta_environment.rascal.ast.Symbol.Empty (tree);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Symbol.Empty) table.get (x);
  }
  public org.meta_environment.rascal.ast.Symbol.
    ParameterizedSort makeSymbolParameterizedSort (ITree tree,
						   org.meta_environment.
						   rascal.ast.Sort sort,
						   java.util.LisT <
						   org.meta_environment.
						   rascal.ast.Symbol >
						   parameters)
  {
    org.meta_environment.rascal.ast.Symbol.ParameterizedSort x =
      new org.meta_environment.rascal.ast.Symbol.ParameterizedSort (tree,
								    sort,
								    parameters);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Symbol.ParameterizedSort) table.
      get (x);
  }
  public org.meta_environment.rascal.ast.Symbol.
    Ambiguity makeSymbolAmbiguity (java.util.LisT <
				   org.meta_environment.rascal.ast.Symbol >
				   alternatives)
  {
    org.meta_environment.rascal.ast.Symbol.Ambiguity amb =
      new org.meta_environment.rascal.ast.Symbol.Ambiguity (alternatives);
    if (!table.containsKey (amb))
      {
	table.put (amb, amb);
      }
    return (org.meta_environment.rascal.ast.Symbol.Ambiguity) table.get (amb);
  }
  public org.meta_environment.rascal.ast.Symbol.
    Sort makeSymbolSort (ITree tree,
			 org.meta_environment.rascal.ast.Sort sort)
  {
    org.meta_environment.rascal.ast.Symbol.Sort x =
      new org.meta_environment.rascal.ast.Symbol.Sort (tree, sort);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Symbol.Sort) table.get (x);
  }
  public org.meta_environment.rascal.ast.SingleQuotedStrChar.
    Ambiguity makeSingleQuotedStrCharAmbiguity (java.util.LisT <
						org.meta_environment.rascal.
						ast.SingleQuotedStrChar >
						alternatives)
  {
    org.meta_environment.rascal.ast.SingleQuotedStrChar.Ambiguity amb =
      new org.meta_environment.rascal.ast.SingleQuotedStrChar.
      Ambiguity (alternatives);
    if (!table.containsKey (amb))
      {
	table.put (amb, amb);
      }
    return (org.meta_environment.rascal.ast.SingleQuotedStrChar.
	    Ambiguity) table.get (amb);
  }
  public org.meta_environment.rascal.ast.SingleQuotedStrChar.
    Lexical makeSingleQuotedStrCharLexical (ITree tree, String string)
  {
    org.meta_environment.rascal.ast.SingleQuotedStrChar.Lexical x =
      new org.meta_environment.rascal.ast.SingleQuotedStrChar.Lexical (tree,
								       string);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.SingleQuotedStrChar.
	    Lexical) table.get (x);
  }
  public org.meta_environment.rascal.ast.SingleQuotedStrCon.
    Ambiguity makeSingleQuotedStrConAmbiguity (java.util.LisT <
					       org.meta_environment.rascal.
					       ast.SingleQuotedStrCon >
					       alternatives)
  {
    org.meta_environment.rascal.ast.SingleQuotedStrCon.Ambiguity amb =
      new org.meta_environment.rascal.ast.SingleQuotedStrCon.
      Ambiguity (alternatives);
    if (!table.containsKey (amb))
      {
	table.put (amb, amb);
      }
    return (org.meta_environment.rascal.ast.SingleQuotedStrCon.
	    Ambiguity) table.get (amb);
  }
  public org.meta_environment.rascal.ast.SingleQuotedStrCon.
    Lexical makeSingleQuotedStrConLexical (ITree tree, String string)
  {
    org.meta_environment.rascal.ast.SingleQuotedStrCon.Lexical x =
      new org.meta_environment.rascal.ast.SingleQuotedStrCon.Lexical (tree,
								      string);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.SingleQuotedStrCon.Lexical) table.
      get (x);
  }
  public org.meta_environment.rascal.ast.CharRange.
    Range makeCharRangeRange (ITree tree,
			      org.meta_environment.rascal.ast.Character start,
			      org.meta_environment.rascal.ast.Character end)
  {
    org.meta_environment.rascal.ast.CharRange.Range x =
      new org.meta_environment.rascal.ast.CharRange.Range (tree, start, end);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.CharRange.Range) table.get (x);
  }
  public org.meta_environment.rascal.ast.CharRange.
    Ambiguity makeCharRangeAmbiguity (java.util.LisT <
				      org.meta_environment.rascal.ast.
				      CharRange > alternatives)
  {
    org.meta_environment.rascal.ast.CharRange.Ambiguity amb =
      new org.meta_environment.rascal.ast.CharRange.Ambiguity (alternatives);
    if (!table.containsKey (amb))
      {
	table.put (amb, amb);
      }
    return (org.meta_environment.rascal.ast.CharRange.Ambiguity) table.
      get (amb);
  }
  public org.meta_environment.rascal.ast.CharRange.
    Character makeCharRangeCharacter (ITree tree,
				      org.meta_environment.rascal.ast.
				      Character character)
  {
    org.meta_environment.rascal.ast.CharRange.Character x =
      new org.meta_environment.rascal.ast.CharRange.Character (tree,
							       character);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.CharRange.Character) table.
      get (x);
  }
  public org.meta_environment.rascal.ast.CharRanges.
    Bracket makeCharRangesBracket (ITree tree,
				   org.meta_environment.rascal.ast.
				   CharRanges ranges)
  {
    org.meta_environment.rascal.ast.CharRanges.Bracket x =
      new org.meta_environment.rascal.ast.CharRanges.Bracket (tree, ranges);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.CharRanges.Bracket) table.get (x);
  }
  public org.meta_environment.rascal.ast.CharRanges.
    Concatenate makeCharRangesConcatenate (ITree tree,
					   org.meta_environment.rascal.ast.
					   CharRanges lhs,
					   org.meta_environment.rascal.ast.
					   CharRanges rhs)
  {
    org.meta_environment.rascal.ast.CharRanges.Concatenate x =
      new org.meta_environment.rascal.ast.CharRanges.Concatenate (tree, lhs,
								  rhs);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.CharRanges.Concatenate) table.
      get (x);
  }
  public org.meta_environment.rascal.ast.CharRanges.
    Ambiguity makeCharRangesAmbiguity (java.util.LisT <
				       org.meta_environment.rascal.ast.
				       CharRanges > alternatives)
  {
    org.meta_environment.rascal.ast.CharRanges.Ambiguity amb =
      new org.meta_environment.rascal.ast.CharRanges.Ambiguity (alternatives);
    if (!table.containsKey (amb))
      {
	table.put (amb, amb);
      }
    return (org.meta_environment.rascal.ast.CharRanges.Ambiguity) table.
      get (amb);
  }
  public org.meta_environment.rascal.ast.CharRanges.
    Range makeCharRangesRange (ITree tree,
			       org.meta_environment.rascal.ast.
			       CharRange range)
  {
    org.meta_environment.rascal.ast.CharRanges.Range x =
      new org.meta_environment.rascal.ast.CharRanges.Range (tree, range);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.CharRanges.Range) table.get (x);
  }
  public org.meta_environment.rascal.ast.OptCharRanges.
    Present makeOptCharRangesPresent (ITree tree,
				      org.meta_environment.rascal.ast.
				      CharRanges ranges)
  {
    org.meta_environment.rascal.ast.OptCharRanges.Present x =
      new org.meta_environment.rascal.ast.OptCharRanges.Present (tree,
								 ranges);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.OptCharRanges.Present) table.
      get (x);
  }
  public org.meta_environment.rascal.ast.OptCharRanges.
    Ambiguity makeOptCharRangesAmbiguity (java.util.LisT <
					  org.meta_environment.rascal.ast.
					  OptCharRanges > alternatives)
  {
    org.meta_environment.rascal.ast.OptCharRanges.Ambiguity amb =
      new org.meta_environment.rascal.ast.OptCharRanges.
      Ambiguity (alternatives);
    if (!table.containsKey (amb))
      {
	table.put (amb, amb);
      }
    return (org.meta_environment.rascal.ast.OptCharRanges.Ambiguity) table.
      get (amb);
  }
  public org.meta_environment.rascal.ast.OptCharRanges.
    Absent makeOptCharRangesAbsent (ITree tree)
  {
    org.meta_environment.rascal.ast.OptCharRanges.Absent x =
      new org.meta_environment.rascal.ast.OptCharRanges.Absent (tree);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.OptCharRanges.Absent) table.
      get (x);
  }
  public org.meta_environment.rascal.ast.CharClass.
    Union makeCharClassUnion (ITree tree,
			      org.meta_environment.rascal.ast.CharClass lhs,
			      org.meta_environment.rascal.ast.CharClass rhs)
  {
    org.meta_environment.rascal.ast.CharClass.Union x =
      new org.meta_environment.rascal.ast.CharClass.Union (tree, lhs, rhs);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.CharClass.Union) table.get (x);
  }
  public org.meta_environment.rascal.ast.CharClass.
    Intersection makeCharClassIntersection (ITree tree,
					    org.meta_environment.rascal.ast.
					    CharClass lhs,
					    org.meta_environment.rascal.ast.
					    CharClass rhs)
  {
    org.meta_environment.rascal.ast.CharClass.Intersection x =
      new org.meta_environment.rascal.ast.CharClass.Intersection (tree, lhs,
								  rhs);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.CharClass.Intersection) table.
      get (x);
  }
  public org.meta_environment.rascal.ast.CharClass.
    Difference makeCharClassDifference (ITree tree,
					org.meta_environment.rascal.ast.
					CharClass lhs,
					org.meta_environment.rascal.ast.
					CharClass rhs)
  {
    org.meta_environment.rascal.ast.CharClass.Difference x =
      new org.meta_environment.rascal.ast.CharClass.Difference (tree, lhs,
								rhs);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.CharClass.Difference) table.
      get (x);
  }
  public org.meta_environment.rascal.ast.CharClass.
    Complement makeCharClassComplement (ITree tree,
					org.meta_environment.rascal.ast.
					CharClass charClass)
  {
    org.meta_environment.rascal.ast.CharClass.Complement x =
      new org.meta_environment.rascal.ast.CharClass.Complement (tree,
								charClass);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.CharClass.Complement) table.
      get (x);
  }
  public org.meta_environment.rascal.ast.CharClass.
    Bracket makeCharClassBracket (ITree tree,
				  org.meta_environment.rascal.ast.
				  CharClass charClass)
  {
    org.meta_environment.rascal.ast.CharClass.Bracket x =
      new org.meta_environment.rascal.ast.CharClass.Bracket (tree, charClass);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.CharClass.Bracket) table.get (x);
  }
  public org.meta_environment.rascal.ast.CharClass.
    Ambiguity makeCharClassAmbiguity (java.util.LisT <
				      org.meta_environment.rascal.ast.
				      CharClass > alternatives)
  {
    org.meta_environment.rascal.ast.CharClass.Ambiguity amb =
      new org.meta_environment.rascal.ast.CharClass.Ambiguity (alternatives);
    if (!table.containsKey (amb))
      {
	table.put (amb, amb);
      }
    return (org.meta_environment.rascal.ast.CharClass.Ambiguity) table.
      get (amb);
  }
  public org.meta_environment.rascal.ast.CharClass.
    SimpleCharclass makeCharClassSimpleCharclass (ITree tree,
						  org.meta_environment.rascal.
						  ast.
						  OptCharRanges
						  optionalCharRanges)
  {
    org.meta_environment.rascal.ast.CharClass.SimpleCharclass x =
      new org.meta_environment.rascal.ast.CharClass.SimpleCharclass (tree,
								     optionalCharRanges);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.CharClass.SimpleCharclass) table.
      get (x);
  }
  public org.meta_environment.rascal.ast.NumChar.
    Ambiguity makeNumCharAmbiguity (java.util.LisT <
				    org.meta_environment.rascal.ast.NumChar >
				    alternatives)
  {
    org.meta_environment.rascal.ast.NumChar.Ambiguity amb =
      new org.meta_environment.rascal.ast.NumChar.Ambiguity (alternatives);
    if (!table.containsKey (amb))
      {
	table.put (amb, amb);
      }
    return (org.meta_environment.rascal.ast.NumChar.Ambiguity) table.
      get (amb);
  }
  public org.meta_environment.rascal.ast.NumChar.
    Lexical makeNumCharLexical (ITree tree, String string)
  {
    org.meta_environment.rascal.ast.NumChar.Lexical x =
      new org.meta_environment.rascal.ast.NumChar.Lexical (tree, string);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.NumChar.Lexical) table.get (x);
  }
  public org.meta_environment.rascal.ast.ShortChar.
    Ambiguity makeShortCharAmbiguity (java.util.LisT <
				      org.meta_environment.rascal.ast.
				      ShortChar > alternatives)
  {
    org.meta_environment.rascal.ast.ShortChar.Ambiguity amb =
      new org.meta_environment.rascal.ast.ShortChar.Ambiguity (alternatives);
    if (!table.containsKey (amb))
      {
	table.put (amb, amb);
      }
    return (org.meta_environment.rascal.ast.ShortChar.Ambiguity) table.
      get (amb);
  }
  public org.meta_environment.rascal.ast.ShortChar.
    Lexical makeShortCharLexical (ITree tree, String string)
  {
    org.meta_environment.rascal.ast.ShortChar.Lexical x =
      new org.meta_environment.rascal.ast.ShortChar.Lexical (tree, string);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.ShortChar.Lexical) table.get (x);
  }
  public org.meta_environment.rascal.ast.Character.
    LabelStart makeCharacterLabelStart (ITree tree)
  {
    org.meta_environment.rascal.ast.Character.LabelStart x =
      new org.meta_environment.rascal.ast.Character.LabelStart (tree);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Character.LabelStart) table.
      get (x);
  }
  public org.meta_environment.rascal.ast.Character.
    Bottom makeCharacterBottom (ITree tree)
  {
    org.meta_environment.rascal.ast.Character.Bottom x =
      new org.meta_environment.rascal.ast.Character.Bottom (tree);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Character.Bottom) table.get (x);
  }
  public org.meta_environment.rascal.ast.Character.
    EOF makeCharacterEOF (ITree tree)
  {
    org.meta_environment.rascal.ast.Character.EOF x =
      new org.meta_environment.rascal.ast.Character.EOF (tree);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Character.EOF) table.get (x);
  }
  public org.meta_environment.rascal.ast.Character.
    Top makeCharacterTop (ITree tree)
  {
    org.meta_environment.rascal.ast.Character.Top x =
      new org.meta_environment.rascal.ast.Character.Top (tree);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Character.Top) table.get (x);
  }
  public org.meta_environment.rascal.ast.Character.
    Short makeCharacterShort (ITree tree,
			      org.meta_environment.rascal.ast.
			      ShortChar shortChar)
  {
    org.meta_environment.rascal.ast.Character.Short x =
      new org.meta_environment.rascal.ast.Character.Short (tree, shortChar);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Character.Short) table.get (x);
  }
  public org.meta_environment.rascal.ast.Character.
    Ambiguity makeCharacterAmbiguity (java.util.LisT <
				      org.meta_environment.rascal.ast.
				      Character > alternatives)
  {
    org.meta_environment.rascal.ast.Character.Ambiguity amb =
      new org.meta_environment.rascal.ast.Character.Ambiguity (alternatives);
    if (!table.containsKey (amb))
      {
	table.put (amb, amb);
      }
    return (org.meta_environment.rascal.ast.Character.Ambiguity) table.
      get (amb);
  }
  public org.meta_environment.rascal.ast.Character.
    Numeric makeCharacterNumeric (ITree tree,
				  org.meta_environment.rascal.ast.
				  NumChar numChar)
  {
    org.meta_environment.rascal.ast.Character.Numeric x =
      new org.meta_environment.rascal.ast.Character.Numeric (tree, numChar);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Character.Numeric) table.get (x);
  }
  public org.meta_environment.rascal.ast.BasicType.
    Loc makeBasicTypeLoc (ITree tree)
  {
    org.meta_environment.rascal.ast.BasicType.Loc x =
      new org.meta_environment.rascal.ast.BasicType.Loc (tree);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.BasicType.Loc) table.get (x);
  }
  public org.meta_environment.rascal.ast.BasicType.
    Void makeBasicTypeVoid (ITree tree)
  {
    org.meta_environment.rascal.ast.BasicType.Void x =
      new org.meta_environment.rascal.ast.BasicType.Void (tree);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.BasicType.Void) table.get (x);
  }
  public org.meta_environment.rascal.ast.BasicType.
    Term makeBasicTypeTerm (ITree tree)
  {
    org.meta_environment.rascal.ast.BasicType.Term x =
      new org.meta_environment.rascal.ast.BasicType.Term (tree);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.BasicType.Term) table.get (x);
  }
  public org.meta_environment.rascal.ast.BasicType.
    Value makeBasicTypeValue (ITree tree)
  {
    org.meta_environment.rascal.ast.BasicType.Value x =
      new org.meta_environment.rascal.ast.BasicType.Value (tree);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.BasicType.Value) table.get (x);
  }
  public org.meta_environment.rascal.ast.BasicType.
    String makeBasicTypeString (ITree tree)
  {
    org.meta_environment.rascal.ast.BasicType.String x =
      new org.meta_environment.rascal.ast.BasicType.String (tree);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.BasicType.String) table.get (x);
  }
  public org.meta_environment.rascal.ast.BasicType.
    Double makeBasicTypeDouble (ITree tree)
  {
    org.meta_environment.rascal.ast.BasicType.Double x =
      new org.meta_environment.rascal.ast.BasicType.Double (tree);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.BasicType.Double) table.get (x);
  }
  public org.meta_environment.rascal.ast.BasicType.
    Int makeBasicTypeInt (ITree tree)
  {
    org.meta_environment.rascal.ast.BasicType.Int x =
      new org.meta_environment.rascal.ast.BasicType.Int (tree);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.BasicType.Int) table.get (x);
  }
  public org.meta_environment.rascal.ast.BasicType.
    Ambiguity makeBasicTypeAmbiguity (java.util.LisT <
				      org.meta_environment.rascal.ast.
				      BasicType > alternatives)
  {
    org.meta_environment.rascal.ast.BasicType.Ambiguity amb =
      new org.meta_environment.rascal.ast.BasicType.Ambiguity (alternatives);
    if (!table.containsKey (amb))
      {
	table.put (amb, amb);
      }
    return (org.meta_environment.rascal.ast.BasicType.Ambiguity) table.
      get (amb);
  }
  public org.meta_environment.rascal.ast.BasicType.
    Bool makeBasicTypeBool (ITree tree)
  {
    org.meta_environment.rascal.ast.BasicType.Bool x =
      new org.meta_environment.rascal.ast.BasicType.Bool (tree);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.BasicType.Bool) table.get (x);
  }
  public org.meta_environment.rascal.ast.TypeArg.
    Named makeTypeArgNamed (ITree tree,
			    org.meta_environment.rascal.ast.Type type,
			    org.meta_environment.rascal.ast.Name name)
  {
    org.meta_environment.rascal.ast.TypeArg.Named x =
      new org.meta_environment.rascal.ast.TypeArg.Named (tree, type, name);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.TypeArg.Named) table.get (x);
  }
  public org.meta_environment.rascal.ast.TypeArg.
    Ambiguity makeTypeArgAmbiguity (java.util.LisT <
				    org.meta_environment.rascal.ast.TypeArg >
				    alternatives)
  {
    org.meta_environment.rascal.ast.TypeArg.Ambiguity amb =
      new org.meta_environment.rascal.ast.TypeArg.Ambiguity (alternatives);
    if (!table.containsKey (amb))
      {
	table.put (amb, amb);
      }
    return (org.meta_environment.rascal.ast.TypeArg.Ambiguity) table.
      get (amb);
  }
  public org.meta_environment.rascal.ast.TypeArg.
    Default makeTypeArgDefault (ITree tree,
				org.meta_environment.rascal.ast.Type type)
  {
    org.meta_environment.rascal.ast.TypeArg.Default x =
      new org.meta_environment.rascal.ast.TypeArg.Default (tree, type);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.TypeArg.Default) table.get (x);
  }
  public org.meta_environment.rascal.ast.StructuredType.
    Tuple makeStructuredTypeTuple (ITree tree,
				   org.meta_environment.rascal.ast.
				   TypeArg first,
				   java.util.LisT <
				   org.meta_environment.rascal.ast.TypeArg >
				   rest)
  {
    org.meta_environment.rascal.ast.StructuredType.Tuple x =
      new org.meta_environment.rascal.ast.StructuredType.Tuple (tree, first,
								rest);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.StructuredType.Tuple) table.
      get (x);
  }
  public org.meta_environment.rascal.ast.StructuredType.
    Relation makeStructuredTypeRelation (ITree tree,
					 org.meta_environment.rascal.ast.
					 TypeArg first,
					 java.util.LisT <
					 org.meta_environment.rascal.ast.
					 TypeArg > rest)
  {
    org.meta_environment.rascal.ast.StructuredType.Relation x =
      new org.meta_environment.rascal.ast.StructuredType.Relation (tree,
								   first,
								   rest);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.StructuredType.Relation) table.
      get (x);
  }
  public org.meta_environment.rascal.ast.StructuredType.
    Map makeStructuredTypeMap (ITree tree,
			       org.meta_environment.rascal.ast.TypeArg first,
			       org.meta_environment.rascal.ast.TypeArg second)
  {
    org.meta_environment.rascal.ast.StructuredType.Map x =
      new org.meta_environment.rascal.ast.StructuredType.Map (tree, first,
							      second);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.StructuredType.Map) table.get (x);
  }
  public org.meta_environment.rascal.ast.StructuredType.
    Set makeStructuredTypeSet (ITree tree,
			       org.meta_environment.rascal.ast.
			       TypeArg typeArg)
  {
    org.meta_environment.rascal.ast.StructuredType.Set x =
      new org.meta_environment.rascal.ast.StructuredType.Set (tree, typeArg);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.StructuredType.Set) table.get (x);
  }
  public org.meta_environment.rascal.ast.StructuredType.
    Ambiguity makeStructuredTypeAmbiguity (java.util.LisT <
					   org.meta_environment.rascal.ast.
					   StructuredType > alternatives)
  {
    org.meta_environment.rascal.ast.StructuredType.Ambiguity amb =
      new org.meta_environment.rascal.ast.StructuredType.
      Ambiguity (alternatives);
    if (!table.containsKey (amb))
      {
	table.put (amb, amb);
      }
    return (org.meta_environment.rascal.ast.StructuredType.Ambiguity) table.
      get (amb);
  }
  public org.meta_environment.rascal.ast.StructuredType.
    LisT makeStructuredTypeLisT (ITree tree,
				 org.meta_environment.rascal.ast.
				 TypeArg typeArg)
  {
    org.meta_environment.rascal.ast.StructuredType.LisT x =
      new org.meta_environment.rascal.ast.StructuredType.LisT (tree, typeArg);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.StructuredType.LisT) table.
      get (x);
  }
  public org.meta_environment.rascal.ast.FunctionType.
    Ambiguity makeFunctionTypeAmbiguity (java.util.LisT <
					 org.meta_environment.rascal.ast.
					 FunctionType > alternatives)
  {
    org.meta_environment.rascal.ast.FunctionType.Ambiguity amb =
      new org.meta_environment.rascal.ast.FunctionType.
      Ambiguity (alternatives);
    if (!table.containsKey (amb))
      {
	table.put (amb, amb);
      }
    return (org.meta_environment.rascal.ast.FunctionType.Ambiguity) table.
      get (amb);
  }
  public org.meta_environment.rascal.ast.FunctionType.
    TypeArguments makeFunctionTypeTypeArguments (ITree tree,
						 org.meta_environment.rascal.
						 ast.Type type,
						 java.util.LisT <
						 org.meta_environment.rascal.
						 ast.TypeArg > arguments)
  {
    org.meta_environment.rascal.ast.FunctionType.TypeArguments x =
      new org.meta_environment.rascal.ast.FunctionType.TypeArguments (tree,
								      type,
								      arguments);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.FunctionType.TypeArguments) table.
      get (x);
  }
  public org.meta_environment.rascal.ast.TypeVar.
    Bounded makeTypeVarBounded (ITree tree,
				org.meta_environment.rascal.ast.Name name,
				org.meta_environment.rascal.ast.Type bound)
  {
    org.meta_environment.rascal.ast.TypeVar.Bounded x =
      new org.meta_environment.rascal.ast.TypeVar.Bounded (tree, name, bound);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.TypeVar.Bounded) table.get (x);
  }
  public org.meta_environment.rascal.ast.TypeVar.
    Ambiguity makeTypeVarAmbiguity (java.util.LisT <
				    org.meta_environment.rascal.ast.TypeVar >
				    alternatives)
  {
    org.meta_environment.rascal.ast.TypeVar.Ambiguity amb =
      new org.meta_environment.rascal.ast.TypeVar.Ambiguity (alternatives);
    if (!table.containsKey (amb))
      {
	table.put (amb, amb);
      }
    return (org.meta_environment.rascal.ast.TypeVar.Ambiguity) table.
      get (amb);
  }
  public org.meta_environment.rascal.ast.TypeVar.
    Free makeTypeVarFree (ITree tree,
			  org.meta_environment.rascal.ast.Name name)
  {
    org.meta_environment.rascal.ast.TypeVar.Free x =
      new org.meta_environment.rascal.ast.TypeVar.Free (tree, name);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.TypeVar.Free) table.get (x);
  }
  public org.meta_environment.rascal.ast.UserType.
    Parametric makeUserTypeParametric (ITree tree,
				       org.meta_environment.rascal.ast.
				       Name name,
				       java.util.LisT <
				       org.meta_environment.rascal.ast.
				       TypeVar > parameters)
  {
    org.meta_environment.rascal.ast.UserType.Parametric x =
      new org.meta_environment.rascal.ast.UserType.Parametric (tree, name,
							       parameters);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.UserType.Parametric) table.
      get (x);
  }
  public org.meta_environment.rascal.ast.UserType.
    Ambiguity makeUserTypeAmbiguity (java.util.LisT <
				     org.meta_environment.rascal.ast.
				     UserType > alternatives)
  {
    org.meta_environment.rascal.ast.UserType.Ambiguity amb =
      new org.meta_environment.rascal.ast.UserType.Ambiguity (alternatives);
    if (!table.containsKey (amb))
      {
	table.put (amb, amb);
      }
    return (org.meta_environment.rascal.ast.UserType.Ambiguity) table.
      get (amb);
  }
  public org.meta_environment.rascal.ast.UserType.
    Name makeUserTypeName (ITree tree,
			   org.meta_environment.rascal.ast.Name name)
  {
    org.meta_environment.rascal.ast.UserType.Name x =
      new org.meta_environment.rascal.ast.UserType.Name (tree, name);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.UserType.Name) table.get (x);
  }
  public org.meta_environment.rascal.ast.DataTypeSelector.
    Ambiguity makeDataTypeSelectorAmbiguity (java.util.LisT <
					     org.meta_environment.rascal.ast.
					     DataTypeSelector > alternatives)
  {
    org.meta_environment.rascal.ast.DataTypeSelector.Ambiguity amb =
      new org.meta_environment.rascal.ast.DataTypeSelector.
      Ambiguity (alternatives);
    if (!table.containsKey (amb))
      {
	table.put (amb, amb);
      }
    return (org.meta_environment.rascal.ast.DataTypeSelector.Ambiguity) table.
      get (amb);
  }
  public org.meta_environment.rascal.ast.DataTypeSelector.
    Selector makeDataTypeSelectorSelector (ITree tree,
					   org.meta_environment.rascal.ast.
					   Name sort,
					   org.meta_environment.rascal.ast.
					   Name production)
  {
    org.meta_environment.rascal.ast.DataTypeSelector.Selector x =
      new org.meta_environment.rascal.ast.DataTypeSelector.Selector (tree,
								     sort,
								     production);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.DataTypeSelector.Selector) table.
      get (x);
  }
  public org.meta_environment.rascal.ast.Type.
    Selector makeTypeSelector (ITree tree,
			       org.meta_environment.rascal.ast.
			       DataTypeSelector selector)
  {
    org.meta_environment.rascal.ast.Type.Selector x =
      new org.meta_environment.rascal.ast.Type.Selector (tree, selector);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Type.Selector) table.get (x);
  }
  public org.meta_environment.rascal.ast.Type.
    Symbol makeTypeSymbol (ITree tree,
			   org.meta_environment.rascal.ast.Symbol symbol)
  {
    org.meta_environment.rascal.ast.Type.Symbol x =
      new org.meta_environment.rascal.ast.Type.Symbol (tree, symbol);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Type.Symbol) table.get (x);
  }
  public org.meta_environment.rascal.ast.Type.User makeTypeUser (ITree tree,
								 org.
								 meta_environment.
								 rascal.ast.
								 UserType
								 user)
  {
    org.meta_environment.rascal.ast.Type.User x =
      new org.meta_environment.rascal.ast.Type.User (tree, user);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Type.User) table.get (x);
  }
  public org.meta_environment.rascal.ast.Type.
    Variable makeTypeVariable (ITree tree,
			       org.meta_environment.rascal.ast.
			       TypeVar typeVar)
  {
    org.meta_environment.rascal.ast.Type.Variable x =
      new org.meta_environment.rascal.ast.Type.Variable (tree, typeVar);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Type.Variable) table.get (x);
  }
  public org.meta_environment.rascal.ast.Type.
    Function makeTypeFunction (ITree tree,
			       org.meta_environment.rascal.ast.
			       FunctionType function)
  {
    org.meta_environment.rascal.ast.Type.Function x =
      new org.meta_environment.rascal.ast.Type.Function (tree, function);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Type.Function) table.get (x);
  }
  public org.meta_environment.rascal.ast.Type.
    Structured makeTypeStructured (ITree tree,
				   org.meta_environment.rascal.ast.
				   StructuredType structured)
  {
    org.meta_environment.rascal.ast.Type.Structured x =
      new org.meta_environment.rascal.ast.Type.Structured (tree, structured);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Type.Structured) table.get (x);
  }
  public org.meta_environment.rascal.ast.Type.
    Ambiguity makeTypeAmbiguity (java.util.LisT <
				 org.meta_environment.rascal.ast.Type >
				 alternatives)
  {
    org.meta_environment.rascal.ast.Type.Ambiguity amb =
      new org.meta_environment.rascal.ast.Type.Ambiguity (alternatives);
    if (!table.containsKey (amb))
      {
	table.put (amb, amb);
      }
    return (org.meta_environment.rascal.ast.Type.Ambiguity) table.get (amb);
  }
  public org.meta_environment.rascal.ast.Type.Basic makeTypeBasic (ITree tree,
								   org.
								   meta_environment.
								   rascal.ast.
								   BasicType
								   basic)
  {
    org.meta_environment.rascal.ast.Type.Basic x =
      new org.meta_environment.rascal.ast.Type.Basic (tree, basic);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Type.Basic) table.get (x);
  }
  public org.meta_environment.rascal.ast.UnicodeEscape.
    Ambiguity makeUnicodeEscapeAmbiguity (java.util.LisT <
					  org.meta_environment.rascal.ast.
					  UnicodeEscape > alternatives)
  {
    org.meta_environment.rascal.ast.UnicodeEscape.Ambiguity amb =
      new org.meta_environment.rascal.ast.UnicodeEscape.
      Ambiguity (alternatives);
    if (!table.containsKey (amb))
      {
	table.put (amb, amb);
      }
    return (org.meta_environment.rascal.ast.UnicodeEscape.Ambiguity) table.
      get (amb);
  }
  public org.meta_environment.rascal.ast.UnicodeEscape.
    Lexical makeUnicodeEscapeLexical (ITree tree, String string)
  {
    org.meta_environment.rascal.ast.UnicodeEscape.Lexical x =
      new org.meta_environment.rascal.ast.UnicodeEscape.Lexical (tree,
								 string);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.UnicodeEscape.Lexical) table.
      get (x);
  }
  public org.meta_environment.rascal.ast.DecimalIntegerLiteral.
    Ambiguity makeDecimalIntegerLiteralAmbiguity (java.util.LisT <
						  org.meta_environment.rascal.
						  ast.DecimalIntegerLiteral >
						  alternatives)
  {
    org.meta_environment.rascal.ast.DecimalIntegerLiteral.Ambiguity amb =
      new org.meta_environment.rascal.ast.DecimalIntegerLiteral.
      Ambiguity (alternatives);
    if (!table.containsKey (amb))
      {
	table.put (amb, amb);
      }
    return (org.meta_environment.rascal.ast.DecimalIntegerLiteral.
	    Ambiguity) table.get (amb);
  }
  public org.meta_environment.rascal.ast.DecimalIntegerLiteral.
    Lexical makeDecimalIntegerLiteralLexical (ITree tree, String string)
  {
    org.meta_environment.rascal.ast.DecimalIntegerLiteral.Lexical x =
      new org.meta_environment.rascal.ast.DecimalIntegerLiteral.Lexical (tree,
									 string);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.DecimalIntegerLiteral.
	    Lexical) table.get (x);
  }
  public org.meta_environment.rascal.ast.HexIntegerLiteral.
    Ambiguity makeHexIntegerLiteralAmbiguity (java.util.LisT <
					      org.meta_environment.rascal.ast.
					      HexIntegerLiteral >
					      alternatives)
  {
    org.meta_environment.rascal.ast.HexIntegerLiteral.Ambiguity amb =
      new org.meta_environment.rascal.ast.HexIntegerLiteral.
      Ambiguity (alternatives);
    if (!table.containsKey (amb))
      {
	table.put (amb, amb);
      }
    return (org.meta_environment.rascal.ast.HexIntegerLiteral.
	    Ambiguity) table.get (amb);
  }
  public org.meta_environment.rascal.ast.HexIntegerLiteral.
    Lexical makeHexIntegerLiteralLexical (ITree tree, String string)
  {
    org.meta_environment.rascal.ast.HexIntegerLiteral.Lexical x =
      new org.meta_environment.rascal.ast.HexIntegerLiteral.Lexical (tree,
								     string);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.HexIntegerLiteral.Lexical) table.
      get (x);
  }
  public org.meta_environment.rascal.ast.OctalIntegerLiteral.
    Ambiguity makeOctalIntegerLiteralAmbiguity (java.util.LisT <
						org.meta_environment.rascal.
						ast.OctalIntegerLiteral >
						alternatives)
  {
    org.meta_environment.rascal.ast.OctalIntegerLiteral.Ambiguity amb =
      new org.meta_environment.rascal.ast.OctalIntegerLiteral.
      Ambiguity (alternatives);
    if (!table.containsKey (amb))
      {
	table.put (amb, amb);
      }
    return (org.meta_environment.rascal.ast.OctalIntegerLiteral.
	    Ambiguity) table.get (amb);
  }
  public org.meta_environment.rascal.ast.OctalIntegerLiteral.
    Lexical makeOctalIntegerLiteralLexical (ITree tree, String string)
  {
    org.meta_environment.rascal.ast.OctalIntegerLiteral.Lexical x =
      new org.meta_environment.rascal.ast.OctalIntegerLiteral.Lexical (tree,
								       string);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.OctalIntegerLiteral.
	    Lexical) table.get (x);
  }
  public org.meta_environment.rascal.ast.DecimalLongLiteral.
    Ambiguity makeDecimalLongLiteralAmbiguity (java.util.LisT <
					       org.meta_environment.rascal.
					       ast.DecimalLongLiteral >
					       alternatives)
  {
    org.meta_environment.rascal.ast.DecimalLongLiteral.Ambiguity amb =
      new org.meta_environment.rascal.ast.DecimalLongLiteral.
      Ambiguity (alternatives);
    if (!table.containsKey (amb))
      {
	table.put (amb, amb);
      }
    return (org.meta_environment.rascal.ast.DecimalLongLiteral.
	    Ambiguity) table.get (amb);
  }
  public org.meta_environment.rascal.ast.DecimalLongLiteral.
    Lexical makeDecimalLongLiteralLexical (ITree tree, String string)
  {
    org.meta_environment.rascal.ast.DecimalLongLiteral.Lexical x =
      new org.meta_environment.rascal.ast.DecimalLongLiteral.Lexical (tree,
								      string);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.DecimalLongLiteral.Lexical) table.
      get (x);
  }
  public org.meta_environment.rascal.ast.HexLongLiteral.
    Ambiguity makeHexLongLiteralAmbiguity (java.util.LisT <
					   org.meta_environment.rascal.ast.
					   HexLongLiteral > alternatives)
  {
    org.meta_environment.rascal.ast.HexLongLiteral.Ambiguity amb =
      new org.meta_environment.rascal.ast.HexLongLiteral.
      Ambiguity (alternatives);
    if (!table.containsKey (amb))
      {
	table.put (amb, amb);
      }
    return (org.meta_environment.rascal.ast.HexLongLiteral.Ambiguity) table.
      get (amb);
  }
  public org.meta_environment.rascal.ast.HexLongLiteral.
    Lexical makeHexLongLiteralLexical (ITree tree, String string)
  {
    org.meta_environment.rascal.ast.HexLongLiteral.Lexical x =
      new org.meta_environment.rascal.ast.HexLongLiteral.Lexical (tree,
								  string);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.HexLongLiteral.Lexical) table.
      get (x);
  }
  public org.meta_environment.rascal.ast.OctalLongLiteral.
    Ambiguity makeOctalLongLiteralAmbiguity (java.util.LisT <
					     org.meta_environment.rascal.ast.
					     OctalLongLiteral > alternatives)
  {
    org.meta_environment.rascal.ast.OctalLongLiteral.Ambiguity amb =
      new org.meta_environment.rascal.ast.OctalLongLiteral.
      Ambiguity (alternatives);
    if (!table.containsKey (amb))
      {
	table.put (amb, amb);
      }
    return (org.meta_environment.rascal.ast.OctalLongLiteral.Ambiguity) table.
      get (amb);
  }
  public org.meta_environment.rascal.ast.OctalLongLiteral.
    Lexical makeOctalLongLiteralLexical (ITree tree, String string)
  {
    org.meta_environment.rascal.ast.OctalLongLiteral.Lexical x =
      new org.meta_environment.rascal.ast.OctalLongLiteral.Lexical (tree,
								    string);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.OctalLongLiteral.Lexical) table.
      get (x);
  }
  public org.meta_environment.rascal.ast.FloatingPointLiteral.
    Ambiguity makeFloatingPointLiteralAmbiguity (java.util.LisT <
						 org.meta_environment.rascal.
						 ast.FloatingPointLiteral >
						 alternatives)
  {
    org.meta_environment.rascal.ast.FloatingPointLiteral.Ambiguity amb =
      new org.meta_environment.rascal.ast.FloatingPointLiteral.
      Ambiguity (alternatives);
    if (!table.containsKey (amb))
      {
	table.put (amb, amb);
      }
    return (org.meta_environment.rascal.ast.FloatingPointLiteral.
	    Ambiguity) table.get (amb);
  }
  public org.meta_environment.rascal.ast.FloatingPointLiteral.
    Lexical makeFloatingPointLiteralLexical (ITree tree, String string)
  {
    org.meta_environment.rascal.ast.FloatingPointLiteral.Lexical x =
      new org.meta_environment.rascal.ast.FloatingPointLiteral.Lexical (tree,
									string);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.FloatingPointLiteral.
	    Lexical) table.get (x);
  }
  public org.meta_environment.rascal.ast.DoubleLiteral.
    Ambiguity makeDoubleLiteralAmbiguity (java.util.LisT <
					  org.meta_environment.rascal.ast.
					  DoubleLiteral > alternatives)
  {
    org.meta_environment.rascal.ast.DoubleLiteral.Ambiguity amb =
      new org.meta_environment.rascal.ast.DoubleLiteral.
      Ambiguity (alternatives);
    if (!table.containsKey (amb))
      {
	table.put (amb, amb);
      }
    return (org.meta_environment.rascal.ast.DoubleLiteral.Ambiguity) table.
      get (amb);
  }
  public org.meta_environment.rascal.ast.DoubleLiteral.
    Lexical makeDoubleLiteralLexical (ITree tree, String string)
  {
    org.meta_environment.rascal.ast.DoubleLiteral.Lexical x =
      new org.meta_environment.rascal.ast.DoubleLiteral.Lexical (tree,
								 string);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.DoubleLiteral.Lexical) table.
      get (x);
  }
  public org.meta_environment.rascal.ast.BooleanLiteral.
    Ambiguity makeBooleanLiteralAmbiguity (java.util.LisT <
					   org.meta_environment.rascal.ast.
					   BooleanLiteral > alternatives)
  {
    org.meta_environment.rascal.ast.BooleanLiteral.Ambiguity amb =
      new org.meta_environment.rascal.ast.BooleanLiteral.
      Ambiguity (alternatives);
    if (!table.containsKey (amb))
      {
	table.put (amb, amb);
      }
    return (org.meta_environment.rascal.ast.BooleanLiteral.Ambiguity) table.
      get (amb);
  }
  public org.meta_environment.rascal.ast.BooleanLiteral.
    Lexical makeBooleanLiteralLexical (ITree tree, String string)
  {
    org.meta_environment.rascal.ast.BooleanLiteral.Lexical x =
      new org.meta_environment.rascal.ast.BooleanLiteral.Lexical (tree,
								  string);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.BooleanLiteral.Lexical) table.
      get (x);
  }
  public org.meta_environment.rascal.ast.SingleCharacter.
    Ambiguity makeSingleCharacterAmbiguity (java.util.LisT <
					    org.meta_environment.rascal.ast.
					    SingleCharacter > alternatives)
  {
    org.meta_environment.rascal.ast.SingleCharacter.Ambiguity amb =
      new org.meta_environment.rascal.ast.SingleCharacter.
      Ambiguity (alternatives);
    if (!table.containsKey (amb))
      {
	table.put (amb, amb);
      }
    return (org.meta_environment.rascal.ast.SingleCharacter.Ambiguity) table.
      get (amb);
  }
  public org.meta_environment.rascal.ast.SingleCharacter.
    Lexical makeSingleCharacterLexical (ITree tree, String string)
  {
    org.meta_environment.rascal.ast.SingleCharacter.Lexical x =
      new org.meta_environment.rascal.ast.SingleCharacter.Lexical (tree,
								   string);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.SingleCharacter.Lexical) table.
      get (x);
  }
  public org.meta_environment.rascal.ast.CharacterLiteral.
    Ambiguity makeCharacterLiteralAmbiguity (java.util.LisT <
					     org.meta_environment.rascal.ast.
					     CharacterLiteral > alternatives)
  {
    org.meta_environment.rascal.ast.CharacterLiteral.Ambiguity amb =
      new org.meta_environment.rascal.ast.CharacterLiteral.
      Ambiguity (alternatives);
    if (!table.containsKey (amb))
      {
	table.put (amb, amb);
      }
    return (org.meta_environment.rascal.ast.CharacterLiteral.Ambiguity) table.
      get (amb);
  }
  public org.meta_environment.rascal.ast.CharacterLiteral.
    Lexical makeCharacterLiteralLexical (ITree tree, String string)
  {
    org.meta_environment.rascal.ast.CharacterLiteral.Lexical x =
      new org.meta_environment.rascal.ast.CharacterLiteral.Lexical (tree,
								    string);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.CharacterLiteral.Lexical) table.
      get (x);
  }
  public org.meta_environment.rascal.ast.EscapeSequence.
    Ambiguity makeEscapeSequenceAmbiguity (java.util.LisT <
					   org.meta_environment.rascal.ast.
					   EscapeSequence > alternatives)
  {
    org.meta_environment.rascal.ast.EscapeSequence.Ambiguity amb =
      new org.meta_environment.rascal.ast.EscapeSequence.
      Ambiguity (alternatives);
    if (!table.containsKey (amb))
      {
	table.put (amb, amb);
      }
    return (org.meta_environment.rascal.ast.EscapeSequence.Ambiguity) table.
      get (amb);
  }
  public org.meta_environment.rascal.ast.EscapeSequence.
    Lexical makeEscapeSequenceLexical (ITree tree, String string)
  {
    org.meta_environment.rascal.ast.EscapeSequence.Lexical x =
      new org.meta_environment.rascal.ast.EscapeSequence.Lexical (tree,
								  string);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.EscapeSequence.Lexical) table.
      get (x);
  }
  public org.meta_environment.rascal.ast.StringCharacter.
    Ambiguity makeStringCharacterAmbiguity (java.util.LisT <
					    org.meta_environment.rascal.ast.
					    StringCharacter > alternatives)
  {
    org.meta_environment.rascal.ast.StringCharacter.Ambiguity amb =
      new org.meta_environment.rascal.ast.StringCharacter.
      Ambiguity (alternatives);
    if (!table.containsKey (amb))
      {
	table.put (amb, amb);
      }
    return (org.meta_environment.rascal.ast.StringCharacter.Ambiguity) table.
      get (amb);
  }
  public org.meta_environment.rascal.ast.StringCharacter.
    Lexical makeStringCharacterLexical (ITree tree, String string)
  {
    org.meta_environment.rascal.ast.StringCharacter.Lexical x =
      new org.meta_environment.rascal.ast.StringCharacter.Lexical (tree,
								   string);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.StringCharacter.Lexical) table.
      get (x);
  }
  public org.meta_environment.rascal.ast.StringLiteral.
    Ambiguity makeStringLiteralAmbiguity (java.util.LisT <
					  org.meta_environment.rascal.ast.
					  StringLiteral > alternatives)
  {
    org.meta_environment.rascal.ast.StringLiteral.Ambiguity amb =
      new org.meta_environment.rascal.ast.StringLiteral.
      Ambiguity (alternatives);
    if (!table.containsKey (amb))
      {
	table.put (amb, amb);
      }
    return (org.meta_environment.rascal.ast.StringLiteral.Ambiguity) table.
      get (amb);
  }
  public org.meta_environment.rascal.ast.StringLiteral.
    Lexical makeStringLiteralLexical (ITree tree, String string)
  {
    org.meta_environment.rascal.ast.StringLiteral.Lexical x =
      new org.meta_environment.rascal.ast.StringLiteral.Lexical (tree,
								 string);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.StringLiteral.Lexical) table.
      get (x);
  }
  public org.meta_environment.rascal.ast.IntegerLiteral.
    OctalIntegerLiteral makeIntegerLiteralOctalIntegerLiteral (ITree tree,
							       org.
							       meta_environment.
							       rascal.ast.
							       OctalIntegerLiteral
							       octal)
  {
    org.meta_environment.rascal.ast.IntegerLiteral.OctalIntegerLiteral x =
      new org.meta_environment.rascal.ast.IntegerLiteral.
      OctalIntegerLiteral (tree, octal);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.IntegerLiteral.
	    OctalIntegerLiteral) table.get (x);
  }
  public org.meta_environment.rascal.ast.IntegerLiteral.
    HexIntegerLiteral makeIntegerLiteralHexIntegerLiteral (ITree tree,
							   org.
							   meta_environment.
							   rascal.ast.
							   HexIntegerLiteral
							   hex)
  {
    org.meta_environment.rascal.ast.IntegerLiteral.HexIntegerLiteral x =
      new org.meta_environment.rascal.ast.IntegerLiteral.
      HexIntegerLiteral (tree, hex);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.IntegerLiteral.
	    HexIntegerLiteral) table.get (x);
  }
  public org.meta_environment.rascal.ast.IntegerLiteral.
    Ambiguity makeIntegerLiteralAmbiguity (java.util.LisT <
					   org.meta_environment.rascal.ast.
					   IntegerLiteral > alternatives)
  {
    org.meta_environment.rascal.ast.IntegerLiteral.Ambiguity amb =
      new org.meta_environment.rascal.ast.IntegerLiteral.
      Ambiguity (alternatives);
    if (!table.containsKey (amb))
      {
	table.put (amb, amb);
      }
    return (org.meta_environment.rascal.ast.IntegerLiteral.Ambiguity) table.
      get (amb);
  }
  public org.meta_environment.rascal.ast.IntegerLiteral.
    DecimalIntegerLiteral makeIntegerLiteralDecimalIntegerLiteral (ITree tree,
								   org.
								   meta_environment.
								   rascal.ast.
								   DecimalIntegerLiteral
								   decimal)
  {
    org.meta_environment.rascal.ast.IntegerLiteral.DecimalIntegerLiteral x =
      new org.meta_environment.rascal.ast.IntegerLiteral.
      DecimalIntegerLiteral (tree, decimal);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.IntegerLiteral.
	    DecimalIntegerLiteral) table.get (x);
  }
  public org.meta_environment.rascal.ast.LongLiteral.
    OctalLongLiteral makeLongLiteralOctalLongLiteral (ITree tree,
						      org.meta_environment.
						      rascal.ast.
						      OctalLongLiteral
						      octalLong)
  {
    org.meta_environment.rascal.ast.LongLiteral.OctalLongLiteral x =
      new org.meta_environment.rascal.ast.LongLiteral.OctalLongLiteral (tree,
									octalLong);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.LongLiteral.
	    OctalLongLiteral) table.get (x);
  }
  public org.meta_environment.rascal.ast.LongLiteral.
    HexLongLiteral makeLongLiteralHexLongLiteral (ITree tree,
						  org.meta_environment.rascal.
						  ast.HexLongLiteral hexLong)
  {
    org.meta_environment.rascal.ast.LongLiteral.HexLongLiteral x =
      new org.meta_environment.rascal.ast.LongLiteral.HexLongLiteral (tree,
								      hexLong);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.LongLiteral.HexLongLiteral) table.
      get (x);
  }
  public org.meta_environment.rascal.ast.LongLiteral.
    Ambiguity makeLongLiteralAmbiguity (java.util.LisT <
					org.meta_environment.rascal.ast.
					LongLiteral > alternatives)
  {
    org.meta_environment.rascal.ast.LongLiteral.Ambiguity amb =
      new org.meta_environment.rascal.ast.LongLiteral.
      Ambiguity (alternatives);
    if (!table.containsKey (amb))
      {
	table.put (amb, amb);
      }
    return (org.meta_environment.rascal.ast.LongLiteral.Ambiguity) table.
      get (amb);
  }
  public org.meta_environment.rascal.ast.LongLiteral.
    DecimalLongLiteral makeLongLiteralDecimalLongLiteral (ITree tree,
							  org.
							  meta_environment.
							  rascal.ast.
							  DecimalLongLiteral
							  decimalLong)
  {
    org.meta_environment.rascal.ast.LongLiteral.DecimalLongLiteral x =
      new org.meta_environment.rascal.ast.LongLiteral.
      DecimalLongLiteral (tree, decimalLong);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.LongLiteral.
	    DecimalLongLiteral) table.get (x);
  }
  public org.meta_environment.rascal.ast.Comment.
    Ambiguity makeCommentAmbiguity (java.util.LisT <
				    org.meta_environment.rascal.ast.Comment >
				    alternatives)
  {
    org.meta_environment.rascal.ast.Comment.Ambiguity amb =
      new org.meta_environment.rascal.ast.Comment.Ambiguity (alternatives);
    if (!table.containsKey (amb))
      {
	table.put (amb, amb);
      }
    return (org.meta_environment.rascal.ast.Comment.Ambiguity) table.
      get (amb);
  }
  public org.meta_environment.rascal.ast.Comment.
    Lexical makeCommentLexical (ITree tree, String string)
  {
    org.meta_environment.rascal.ast.Comment.Lexical x =
      new org.meta_environment.rascal.ast.Comment.Lexical (tree, string);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Comment.Lexical) table.get (x);
  }
  public org.meta_environment.rascal.ast.CommentChar.
    Ambiguity makeCommentCharAmbiguity (java.util.LisT <
					org.meta_environment.rascal.ast.
					CommentChar > alternatives)
  {
    org.meta_environment.rascal.ast.CommentChar.Ambiguity amb =
      new org.meta_environment.rascal.ast.CommentChar.
      Ambiguity (alternatives);
    if (!table.containsKey (amb))
      {
	table.put (amb, amb);
      }
    return (org.meta_environment.rascal.ast.CommentChar.Ambiguity) table.
      get (amb);
  }
  public org.meta_environment.rascal.ast.CommentChar.
    Lexical makeCommentCharLexical (ITree tree, String string)
  {
    org.meta_environment.rascal.ast.CommentChar.Lexical x =
      new org.meta_environment.rascal.ast.CommentChar.Lexical (tree, string);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.CommentChar.Lexical) table.
      get (x);
  }
  public org.meta_environment.rascal.ast.AsterisK.
    Ambiguity makeAsterisKAmbiguity (java.util.LisT <
				     org.meta_environment.rascal.ast.
				     AsterisK > alternatives)
  {
    org.meta_environment.rascal.ast.AsterisK.Ambiguity amb =
      new org.meta_environment.rascal.ast.AsterisK.Ambiguity (alternatives);
    if (!table.containsKey (amb))
      {
	table.put (amb, amb);
      }
    return (org.meta_environment.rascal.ast.AsterisK.Ambiguity) table.
      get (amb);
  }
  public org.meta_environment.rascal.ast.AsterisK.
    Lexical makeAsterisKLexical (ITree tree, String string)
  {
    org.meta_environment.rascal.ast.AsterisK.Lexical x =
      new org.meta_environment.rascal.ast.AsterisK.Lexical (tree, string);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.AsterisK.Lexical) table.get (x);
  }
  public org.meta_environment.rascal.ast.Expression.
    VisIt makeExpressionVisIt (ITree tree,
			       org.meta_environment.rascal.ast.VisIt visIt)
  {
    org.meta_environment.rascal.ast.Expression.VisIt x =
      new org.meta_environment.rascal.ast.Expression.VisIt (tree, visIt);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Expression.VisIt) table.get (x);
  }
  public org.meta_environment.rascal.ast.Expression.
    ExisTs makeExpressionExisTs (ITree tree,
				 org.meta_environment.rascal.ast.
				 ValueProducer producer,
				 org.meta_environment.rascal.ast.
				 Expression expression)
  {
    org.meta_environment.rascal.ast.Expression.ExisTs x =
      new org.meta_environment.rascal.ast.Expression.ExisTs (tree, producer,
							     expression);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Expression.ExisTs) table.get (x);
  }
  public org.meta_environment.rascal.ast.Expression.
    ForAll makeExpressionForAll (ITree tree,
				 org.meta_environment.rascal.ast.
				 ValueProducer producer,
				 org.meta_environment.rascal.ast.
				 Expression expression)
  {
    org.meta_environment.rascal.ast.Expression.ForAll x =
      new org.meta_environment.rascal.ast.Expression.ForAll (tree, producer,
							     expression);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Expression.ForAll) table.get (x);
  }
  public org.meta_environment.rascal.ast.Expression.
    Comprehension makeExpressionComprehension (ITree tree,
					       org.meta_environment.rascal.
					       ast.
					       Comprehension comprehension)
  {
    org.meta_environment.rascal.ast.Expression.Comprehension x =
      new org.meta_environment.rascal.ast.Expression.Comprehension (tree,
								    comprehension);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Expression.Comprehension) table.
      get (x);
  }
  public org.meta_environment.rascal.ast.Expression.
    NoMatch makeExpressionNoMatch (ITree tree,
				   org.meta_environment.rascal.ast.
				   Expression pattern,
				   org.meta_environment.rascal.ast.
				   Expression expression)
  {
    org.meta_environment.rascal.ast.Expression.NoMatch x =
      new org.meta_environment.rascal.ast.Expression.NoMatch (tree, pattern,
							      expression);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Expression.NoMatch) table.get (x);
  }
  public org.meta_environment.rascal.ast.Expression.
    Match makeExpressionMatch (ITree tree,
			       org.meta_environment.rascal.ast.
			       Expression pattern,
			       org.meta_environment.rascal.ast.
			       Expression expression)
  {
    org.meta_environment.rascal.ast.Expression.Match x =
      new org.meta_environment.rascal.ast.Expression.Match (tree, pattern,
							    expression);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Expression.Match) table.get (x);
  }
  public org.meta_environment.rascal.ast.Expression.
    TypedVariable makeExpressionTypedVariable (ITree tree,
					       org.meta_environment.rascal.
					       ast.Type type,
					       org.meta_environment.rascal.
					       ast.Name name)
  {
    org.meta_environment.rascal.ast.Expression.TypedVariable x =
      new org.meta_environment.rascal.ast.Expression.TypedVariable (tree,
								    type,
								    name);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Expression.TypedVariable) table.
      get (x);
  }
  public org.meta_environment.rascal.ast.Expression.
    Operator makeExpressionOperator (ITree tree,
				     org.meta_environment.rascal.ast.
				     StandardOperator operator)
  {
    org.meta_environment.rascal.ast.Expression.Operator x =
      new org.meta_environment.rascal.ast.Expression.Operator (tree,
							       operator);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Expression.Operator) table.
      get (x);
  }
  public org.meta_environment.rascal.ast.Expression.
    IfThenElse makeExpressionIfThenElse (ITree tree,
					 org.meta_environment.rascal.ast.
					 Expression condition,
					 org.meta_environment.rascal.ast.
					 Expression thenExp,
					 org.meta_environment.rascal.ast.
					 Expression elseExp)
  {
    org.meta_environment.rascal.ast.Expression.IfThenElse x =
      new org.meta_environment.rascal.ast.Expression.IfThenElse (tree,
								 condition,
								 thenExp,
								 elseExp);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Expression.IfThenElse) table.
      get (x);
  }
  public org.meta_environment.rascal.ast.Expression.
    IfDefined makeExpressionIfDefined (ITree tree,
				       org.meta_environment.rascal.ast.
				       Expression lhs,
				       org.meta_environment.rascal.ast.
				       Expression rhs)
  {
    org.meta_environment.rascal.ast.Expression.IfDefined x =
      new org.meta_environment.rascal.ast.Expression.IfDefined (tree, lhs,
								rhs);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Expression.IfDefined) table.
      get (x);
  }
  public org.meta_environment.rascal.ast.Expression.
    Or makeExpressionOr (ITree tree,
			 org.meta_environment.rascal.ast.Expression lhs,
			 org.meta_environment.rascal.ast.Expression rhs)
  {
    org.meta_environment.rascal.ast.Expression.Or x =
      new org.meta_environment.rascal.ast.Expression.Or (tree, lhs, rhs);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Expression.Or) table.get (x);
  }
  public org.meta_environment.rascal.ast.Expression.
    And makeExpressionAnd (ITree tree,
			   org.meta_environment.rascal.ast.Expression lhs,
			   org.meta_environment.rascal.ast.Expression rhs)
  {
    org.meta_environment.rascal.ast.Expression.And x =
      new org.meta_environment.rascal.ast.Expression.And (tree, lhs, rhs);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Expression.And) table.get (x);
  }
  public org.meta_environment.rascal.ast.Expression.
    In makeExpressionIn (ITree tree,
			 org.meta_environment.rascal.ast.Expression lhs,
			 org.meta_environment.rascal.ast.Expression rhs)
  {
    org.meta_environment.rascal.ast.Expression.In x =
      new org.meta_environment.rascal.ast.Expression.In (tree, lhs, rhs);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Expression.In) table.get (x);
  }
  public org.meta_environment.rascal.ast.Expression.
    NotIn makeExpressionNotIn (ITree tree,
			       org.meta_environment.rascal.ast.Expression lhs,
			       org.meta_environment.rascal.ast.Expression rhs)
  {
    org.meta_environment.rascal.ast.Expression.NotIn x =
      new org.meta_environment.rascal.ast.Expression.NotIn (tree, lhs, rhs);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Expression.NotIn) table.get (x);
  }
  public org.meta_environment.rascal.ast.Expression.
    NonEquals makeExpressionNonEquals (ITree tree,
				       org.meta_environment.rascal.ast.
				       Expression lhs,
				       org.meta_environment.rascal.ast.
				       Expression rhs)
  {
    org.meta_environment.rascal.ast.Expression.NonEquals x =
      new org.meta_environment.rascal.ast.Expression.NonEquals (tree, lhs,
								rhs);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Expression.NonEquals) table.
      get (x);
  }
  public org.meta_environment.rascal.ast.Expression.
    Equals makeExpressionEquals (ITree tree,
				 org.meta_environment.rascal.ast.
				 Expression lhs,
				 org.meta_environment.rascal.ast.
				 Expression rhs)
  {
    org.meta_environment.rascal.ast.Expression.Equals x =
      new org.meta_environment.rascal.ast.Expression.Equals (tree, lhs, rhs);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Expression.Equals) table.get (x);
  }
  public org.meta_environment.rascal.ast.Expression.
    GreaterThanOrEq makeExpressionGreaterThanOrEq (ITree tree,
						   org.meta_environment.
						   rascal.ast.Expression lhs,
						   org.meta_environment.
						   rascal.ast.Expression rhs)
  {
    org.meta_environment.rascal.ast.Expression.GreaterThanOrEq x =
      new org.meta_environment.rascal.ast.Expression.GreaterThanOrEq (tree,
								      lhs,
								      rhs);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Expression.GreaterThanOrEq) table.
      get (x);
  }
  public org.meta_environment.rascal.ast.Expression.
    GreaterThan makeExpressionGreaterThan (ITree tree,
					   org.meta_environment.rascal.ast.
					   Expression lhs,
					   org.meta_environment.rascal.ast.
					   Expression rhs)
  {
    org.meta_environment.rascal.ast.Expression.GreaterThan x =
      new org.meta_environment.rascal.ast.Expression.GreaterThan (tree, lhs,
								  rhs);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Expression.GreaterThan) table.
      get (x);
  }
  public org.meta_environment.rascal.ast.Expression.
    LessThanOrEq makeExpressionLessThanOrEq (ITree tree,
					     org.meta_environment.rascal.ast.
					     Expression lhs,
					     org.meta_environment.rascal.ast.
					     Expression rhs)
  {
    org.meta_environment.rascal.ast.Expression.LessThanOrEq x =
      new org.meta_environment.rascal.ast.Expression.LessThanOrEq (tree, lhs,
								   rhs);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Expression.LessThanOrEq) table.
      get (x);
  }
  public org.meta_environment.rascal.ast.Expression.
    LessThan makeExpressionLessThan (ITree tree,
				     org.meta_environment.rascal.ast.
				     Expression lhs,
				     org.meta_environment.rascal.ast.
				     Expression rhs)
  {
    org.meta_environment.rascal.ast.Expression.LessThan x =
      new org.meta_environment.rascal.ast.Expression.LessThan (tree, lhs,
							       rhs);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Expression.LessThan) table.
      get (x);
  }
  public org.meta_environment.rascal.ast.Expression.
    RegExpNoMatch makeExpressionRegExpNoMatch (ITree tree,
					       org.meta_environment.rascal.
					       ast.Expression lhs,
					       org.meta_environment.rascal.
					       ast.Expression rhs)
  {
    org.meta_environment.rascal.ast.Expression.RegExpNoMatch x =
      new org.meta_environment.rascal.ast.Expression.RegExpNoMatch (tree, lhs,
								    rhs);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Expression.RegExpNoMatch) table.
      get (x);
  }
  public org.meta_environment.rascal.ast.Expression.
    RegExpMatch makeExpressionRegExpMatch (ITree tree,
					   org.meta_environment.rascal.ast.
					   Expression lhs,
					   org.meta_environment.rascal.ast.
					   Expression rhs)
  {
    org.meta_environment.rascal.ast.Expression.RegExpMatch x =
      new org.meta_environment.rascal.ast.Expression.RegExpMatch (tree, lhs,
								  rhs);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Expression.RegExpMatch) table.
      get (x);
  }
  public org.meta_environment.rascal.ast.Expression.
    Substraction makeExpressionSubstraction (ITree tree,
					     org.meta_environment.rascal.ast.
					     Expression lhs,
					     org.meta_environment.rascal.ast.
					     Expression rhs)
  {
    org.meta_environment.rascal.ast.Expression.Substraction x =
      new org.meta_environment.rascal.ast.Expression.Substraction (tree, lhs,
								   rhs);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Expression.Substraction) table.
      get (x);
  }
  public org.meta_environment.rascal.ast.Expression.
    Addition makeExpressionAddition (ITree tree,
				     org.meta_environment.rascal.ast.
				     Expression lhs,
				     org.meta_environment.rascal.ast.
				     Expression rhs)
  {
    org.meta_environment.rascal.ast.Expression.Addition x =
      new org.meta_environment.rascal.ast.Expression.Addition (tree, lhs,
							       rhs);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Expression.Addition) table.
      get (x);
  }
  public org.meta_environment.rascal.ast.Expression.
    DivisIon makeExpressionDivisIon (ITree tree,
				     org.meta_environment.rascal.ast.
				     Expression lhs,
				     org.meta_environment.rascal.ast.
				     Expression rhs)
  {
    org.meta_environment.rascal.ast.Expression.DivisIon x =
      new org.meta_environment.rascal.ast.Expression.DivisIon (tree, lhs,
							       rhs);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Expression.DivisIon) table.
      get (x);
  }
  public org.meta_environment.rascal.ast.Expression.
    Intersection makeExpressionIntersection (ITree tree,
					     org.meta_environment.rascal.ast.
					     Expression lhs,
					     org.meta_environment.rascal.ast.
					     Expression rhs)
  {
    org.meta_environment.rascal.ast.Expression.Intersection x =
      new org.meta_environment.rascal.ast.Expression.Intersection (tree, lhs,
								   rhs);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Expression.Intersection) table.
      get (x);
  }
  public org.meta_environment.rascal.ast.Expression.
    Product makeExpressionProduct (ITree tree,
				   org.meta_environment.rascal.ast.
				   Expression lhs,
				   org.meta_environment.rascal.ast.
				   Expression rhs)
  {
    org.meta_environment.rascal.ast.Expression.Product x =
      new org.meta_environment.rascal.ast.Expression.Product (tree, lhs, rhs);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Expression.Product) table.get (x);
  }
  public org.meta_environment.rascal.ast.Expression.
    Negation makeExpressionNegation (ITree tree,
				     org.meta_environment.rascal.ast.
				     Expression argument)
  {
    org.meta_environment.rascal.ast.Expression.Negation x =
      new org.meta_environment.rascal.ast.Expression.Negation (tree,
							       argument);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Expression.Negation) table.
      get (x);
  }
  public org.meta_environment.rascal.ast.Expression.
    Annotation makeExpressionAnnotation (ITree tree,
					 org.meta_environment.rascal.ast.
					 Expression expression,
					 org.meta_environment.rascal.ast.
					 Name name)
  {
    org.meta_environment.rascal.ast.Expression.Annotation x =
      new org.meta_environment.rascal.ast.Expression.Annotation (tree,
								 expression,
								 name);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Expression.Annotation) table.
      get (x);
  }
  public org.meta_environment.rascal.ast.Expression.
    TransitiveClosure makeExpressionTransitiveClosure (ITree tree,
						       org.meta_environment.
						       rascal.ast.
						       Expression argument)
  {
    org.meta_environment.rascal.ast.Expression.TransitiveClosure x =
      new org.meta_environment.rascal.ast.Expression.TransitiveClosure (tree,
									argument);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Expression.
	    TransitiveClosure) table.get (x);
  }
  public org.meta_environment.rascal.ast.Expression.
    TransitiveReflexiveClosure makeExpressionTransitiveReflexiveClosure (ITree
									 tree,
									 org.
									 meta_environment.
									 rascal.
									 ast.
									 Expression
									 argument)
  {
    org.meta_environment.rascal.ast.Expression.TransitiveReflexiveClosure x =
      new org.meta_environment.rascal.ast.Expression.
      TransitiveReflexiveClosure (tree, argument);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Expression.
	    TransitiveReflexiveClosure) table.get (x);
  }
  public org.meta_environment.rascal.ast.Expression.
    Subscript makeExpressionSubscript (ITree tree,
				       org.meta_environment.rascal.ast.
				       Expression expression,
				       org.meta_environment.rascal.ast.
				       Expression subscript)
  {
    org.meta_environment.rascal.ast.Expression.Subscript x =
      new org.meta_environment.rascal.ast.Expression.Subscript (tree,
								expression,
								subscript);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Expression.Subscript) table.
      get (x);
  }
  public org.meta_environment.rascal.ast.Expression.
    FieldAccess makeExpressionFieldAccess (ITree tree,
					   org.meta_environment.rascal.ast.
					   Expression expression,
					   org.meta_environment.rascal.ast.
					   Name field)
  {
    org.meta_environment.rascal.ast.Expression.FieldAccess x =
      new org.meta_environment.rascal.ast.Expression.FieldAccess (tree,
								  expression,
								  field);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Expression.FieldAccess) table.
      get (x);
  }
  public org.meta_environment.rascal.ast.Expression.
    FieldUpdate makeExpressionFieldUpdate (ITree tree,
					   org.meta_environment.rascal.ast.
					   Expression expression,
					   org.meta_environment.rascal.ast.
					   Name key,
					   org.meta_environment.rascal.ast.
					   Expression replacement)
  {
    org.meta_environment.rascal.ast.Expression.FieldUpdate x =
      new org.meta_environment.rascal.ast.Expression.FieldUpdate (tree,
								  expression,
								  key,
								  replacement);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Expression.FieldUpdate) table.
      get (x);
  }
  public org.meta_environment.rascal.ast.Expression.
    StepRange makeExpressionStepRange (ITree tree,
				       org.meta_environment.rascal.ast.
				       Expression from,
				       org.meta_environment.rascal.ast.
				       Expression by,
				       org.meta_environment.rascal.ast.
				       Expression to)
  {
    org.meta_environment.rascal.ast.Expression.StepRange x =
      new org.meta_environment.rascal.ast.Expression.StepRange (tree, from,
								by, to);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Expression.StepRange) table.
      get (x);
  }
  public org.meta_environment.rascal.ast.Expression.
    Range makeExpressionRange (ITree tree,
			       org.meta_environment.rascal.ast.
			       Expression from,
			       org.meta_environment.rascal.ast.Expression to)
  {
    org.meta_environment.rascal.ast.Expression.Range x =
      new org.meta_environment.rascal.ast.Expression.Range (tree, from, to);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Expression.Range) table.get (x);
  }
  public org.meta_environment.rascal.ast.Expression.
    ClosureCall makeExpressionClosureCall (ITree tree,
					   org.meta_environment.rascal.ast.
					   Expression closure,
					   java.util.LisT <
					   org.meta_environment.rascal.ast.
					   Expression > arguments)
  {
    org.meta_environment.rascal.ast.Expression.ClosureCall x =
      new org.meta_environment.rascal.ast.Expression.ClosureCall (tree,
								  closure,
								  arguments);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Expression.ClosureCall) table.
      get (x);
  }
  public org.meta_environment.rascal.ast.Expression.
    Bracket makeExpressionBracket (ITree tree,
				   org.meta_environment.rascal.ast.
				   Expression expression)
  {
    org.meta_environment.rascal.ast.Expression.Bracket x =
      new org.meta_environment.rascal.ast.Expression.Bracket (tree,
							      expression);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Expression.Bracket) table.get (x);
  }
  public org.meta_environment.rascal.ast.Expression.
    Closure makeExpressionClosure (ITree tree,
				   org.meta_environment.rascal.ast.Type type,
				   org.meta_environment.rascal.ast.
				   Parameters parameters,
				   java.util.LisT <
				   org.meta_environment.rascal.ast.Statement >
				   statements)
  {
    org.meta_environment.rascal.ast.Expression.Closure x =
      new org.meta_environment.rascal.ast.Expression.Closure (tree, type,
							      parameters,
							      statements);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Expression.Closure) table.get (x);
  }
  public org.meta_environment.rascal.ast.Expression.
    QualifiedName makeExpressionQualifiedName (ITree tree,
					       org.meta_environment.rascal.
					       ast.
					       QualifiedName qualifiedName)
  {
    org.meta_environment.rascal.ast.Expression.QualifiedName x =
      new org.meta_environment.rascal.ast.Expression.QualifiedName (tree,
								    qualifiedName);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Expression.QualifiedName) table.
      get (x);
  }
  public org.meta_environment.rascal.ast.Expression.
    AreaInFileLocation makeExpressionAreaInFileLocation (ITree tree,
							 org.meta_environment.
							 rascal.ast.
							 Expression filename,
							 org.meta_environment.
							 rascal.ast.
							 Expression area)
  {
    org.meta_environment.rascal.ast.Expression.AreaInFileLocation x =
      new org.meta_environment.rascal.ast.Expression.AreaInFileLocation (tree,
									 filename,
									 area);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Expression.
	    AreaInFileLocation) table.get (x);
  }
  public org.meta_environment.rascal.ast.Expression.
    AreaLocation makeExpressionAreaLocation (ITree tree,
					     org.meta_environment.rascal.ast.
					     Expression area)
  {
    org.meta_environment.rascal.ast.Expression.AreaLocation x =
      new org.meta_environment.rascal.ast.Expression.AreaLocation (tree,
								   area);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Expression.AreaLocation) table.
      get (x);
  }
  public org.meta_environment.rascal.ast.Expression.
    FileLocation makeExpressionFileLocation (ITree tree,
					     org.meta_environment.rascal.ast.
					     Expression filename)
  {
    org.meta_environment.rascal.ast.Expression.FileLocation x =
      new org.meta_environment.rascal.ast.Expression.FileLocation (tree,
								   filename);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Expression.FileLocation) table.
      get (x);
  }
  public org.meta_environment.rascal.ast.Expression.
    Area makeExpressionArea (ITree tree)
  {
    org.meta_environment.rascal.ast.Expression.Area x =
      new org.meta_environment.rascal.ast.Expression.Area (tree);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Expression.Area) table.get (x);
  }
  public org.meta_environment.rascal.ast.Expression.
    Location makeExpressionLocation (ITree tree)
  {
    org.meta_environment.rascal.ast.Expression.Location x =
      new org.meta_environment.rascal.ast.Expression.Location (tree);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Expression.Location) table.
      get (x);
  }
  public org.meta_environment.rascal.ast.Expression.
    MapTuple makeExpressionMapTuple (ITree tree,
				     org.meta_environment.rascal.ast.
				     Expression from,
				     org.meta_environment.rascal.ast.
				     Expression to)
  {
    org.meta_environment.rascal.ast.Expression.MapTuple x =
      new org.meta_environment.rascal.ast.Expression.MapTuple (tree, from,
							       to);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Expression.MapTuple) table.
      get (x);
  }
  public org.meta_environment.rascal.ast.Expression.
    Tuple makeExpressionTuple (ITree tree,
			       org.meta_environment.rascal.ast.
			       Expression first,
			       java.util.LisT <
			       org.meta_environment.rascal.ast.Expression >
			       rest)
  {
    org.meta_environment.rascal.ast.Expression.Tuple x =
      new org.meta_environment.rascal.ast.Expression.Tuple (tree, first,
							    rest);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Expression.Tuple) table.get (x);
  }
  public org.meta_environment.rascal.ast.Expression.
    Set makeExpressionSet (ITree tree,
			   java.util.LisT <
			   org.meta_environment.rascal.ast.Expression >
			   elements)
  {
    org.meta_environment.rascal.ast.Expression.Set x =
      new org.meta_environment.rascal.ast.Expression.Set (tree, elements);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Expression.Set) table.get (x);
  }
  public org.meta_environment.rascal.ast.Expression.
    LisT makeExpressionLisT (ITree tree,
			     java.util.LisT <
			     org.meta_environment.rascal.ast.Expression >
			     elements)
  {
    org.meta_environment.rascal.ast.Expression.LisT x =
      new org.meta_environment.rascal.ast.Expression.LisT (tree, elements);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Expression.LisT) table.get (x);
  }
  public org.meta_environment.rascal.ast.Expression.
    CallOrTree makeExpressionCallOrTree (ITree tree,
					 org.meta_environment.rascal.ast.
					 Name name,
					 java.util.LisT <
					 org.meta_environment.rascal.ast.
					 Expression > arguments)
  {
    org.meta_environment.rascal.ast.Expression.CallOrTree x =
      new org.meta_environment.rascal.ast.Expression.CallOrTree (tree, name,
								 arguments);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Expression.CallOrTree) table.
      get (x);
  }
  public org.meta_environment.rascal.ast.Expression.
    Ambiguity makeExpressionAmbiguity (java.util.LisT <
				       org.meta_environment.rascal.ast.
				       Expression > alternatives)
  {
    org.meta_environment.rascal.ast.Expression.Ambiguity amb =
      new org.meta_environment.rascal.ast.Expression.Ambiguity (alternatives);
    if (!table.containsKey (amb))
      {
	table.put (amb, amb);
      }
    return (org.meta_environment.rascal.ast.Expression.Ambiguity) table.
      get (amb);
  }
  public org.meta_environment.rascal.ast.Expression.
    Literal makeExpressionLiteral (ITree tree,
				   org.meta_environment.rascal.ast.
				   Literal literal)
  {
    org.meta_environment.rascal.ast.Expression.Literal x =
      new org.meta_environment.rascal.ast.Expression.Literal (tree, literal);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Expression.Literal) table.get (x);
  }
  public org.meta_environment.rascal.ast.Area.
    Ambiguity makeAreaAmbiguity (java.util.LisT <
				 org.meta_environment.rascal.ast.Area >
				 alternatives)
  {
    org.meta_environment.rascal.ast.Area.Ambiguity amb =
      new org.meta_environment.rascal.ast.Area.Ambiguity (alternatives);
    if (!table.containsKey (amb))
      {
	table.put (amb, amb);
      }
    return (org.meta_environment.rascal.ast.Area.Ambiguity) table.get (amb);
  }
  public org.meta_environment.rascal.ast.Area.
    Default makeAreaDefault (ITree tree,
			     org.meta_environment.rascal.ast.
			     Expression beginLine,
			     org.meta_environment.rascal.ast.
			     Expression beginColumn,
			     org.meta_environment.rascal.ast.
			     Expression endLine,
			     org.meta_environment.rascal.ast.
			     Expression endColumn,
			     org.meta_environment.rascal.ast.
			     Expression offset,
			     org.meta_environment.rascal.ast.
			     Expression length)
  {
    org.meta_environment.rascal.ast.Area.Default x =
      new org.meta_environment.rascal.ast.Area.Default (tree, beginLine,
							beginColumn, endLine,
							endColumn, offset,
							length);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Area.Default) table.get (x);
  }
  public org.meta_environment.rascal.ast.TagString.
    Ambiguity makeTagStringAmbiguity (java.util.LisT <
				      org.meta_environment.rascal.ast.
				      TagString > alternatives)
  {
    org.meta_environment.rascal.ast.TagString.Ambiguity amb =
      new org.meta_environment.rascal.ast.TagString.Ambiguity (alternatives);
    if (!table.containsKey (amb))
      {
	table.put (amb, amb);
      }
    return (org.meta_environment.rascal.ast.TagString.Ambiguity) table.
      get (amb);
  }
  public org.meta_environment.rascal.ast.TagString.
    Lexical makeTagStringLexical (ITree tree, String string)
  {
    org.meta_environment.rascal.ast.TagString.Lexical x =
      new org.meta_environment.rascal.ast.TagString.Lexical (tree, string);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.TagString.Lexical) table.get (x);
  }
  public org.meta_environment.rascal.ast.TagChar.
    Ambiguity makeTagCharAmbiguity (java.util.LisT <
				    org.meta_environment.rascal.ast.TagChar >
				    alternatives)
  {
    org.meta_environment.rascal.ast.TagChar.Ambiguity amb =
      new org.meta_environment.rascal.ast.TagChar.Ambiguity (alternatives);
    if (!table.containsKey (amb))
      {
	table.put (amb, amb);
      }
    return (org.meta_environment.rascal.ast.TagChar.Ambiguity) table.
      get (amb);
  }
  public org.meta_environment.rascal.ast.TagChar.
    Lexical makeTagCharLexical (ITree tree, String string)
  {
    org.meta_environment.rascal.ast.TagChar.Lexical x =
      new org.meta_environment.rascal.ast.TagChar.Lexical (tree, string);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.TagChar.Lexical) table.get (x);
  }
  public org.meta_environment.rascal.ast.Tag.Ambiguity makeTagAmbiguity (java.
									 util.
									 LisT
									 <
									 org.
									 meta_environment.
									 rascal.
									 ast.
									 Tag >
									 alternatives)
  {
    org.meta_environment.rascal.ast.Tag.Ambiguity amb =
      new org.meta_environment.rascal.ast.Tag.Ambiguity (alternatives);
    if (!table.containsKey (amb))
      {
	table.put (amb, amb);
      }
    return (org.meta_environment.rascal.ast.Tag.Ambiguity) table.get (amb);
  }
  public org.meta_environment.rascal.ast.Tag.
    Default makeTagDefault (ITree tree,
			    org.meta_environment.rascal.ast.Name name,
			    org.meta_environment.rascal.ast.
			    TagString contents)
  {
    org.meta_environment.rascal.ast.Tag.Default x =
      new org.meta_environment.rascal.ast.Tag.Default (tree, name, contents);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Tag.Default) table.get (x);
  }
  public org.meta_environment.rascal.ast.Tags.
    Ambiguity makeTagsAmbiguity (java.util.LisT <
				 org.meta_environment.rascal.ast.Tags >
				 alternatives)
  {
    org.meta_environment.rascal.ast.Tags.Ambiguity amb =
      new org.meta_environment.rascal.ast.Tags.Ambiguity (alternatives);
    if (!table.containsKey (amb))
      {
	table.put (amb, amb);
      }
    return (org.meta_environment.rascal.ast.Tags.Ambiguity) table.get (amb);
  }
  public org.meta_environment.rascal.ast.Tags.
    Default makeTagsDefault (ITree tree,
			     java.util.LisT <
			     org.meta_environment.rascal.ast.Tag >
			     annotations)
  {
    org.meta_environment.rascal.ast.Tags.Default x =
      new org.meta_environment.rascal.ast.Tags.Default (tree, annotations);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Tags.Default) table.get (x);
  }
  public org.meta_environment.rascal.ast.SymbolLiteral.
    Ambiguity makeSymbolLiteralAmbiguity (java.util.LisT <
					  org.meta_environment.rascal.ast.
					  SymbolLiteral > alternatives)
  {
    org.meta_environment.rascal.ast.SymbolLiteral.Ambiguity amb =
      new org.meta_environment.rascal.ast.SymbolLiteral.
      Ambiguity (alternatives);
    if (!table.containsKey (amb))
      {
	table.put (amb, amb);
      }
    return (org.meta_environment.rascal.ast.SymbolLiteral.Ambiguity) table.
      get (amb);
  }
  public org.meta_environment.rascal.ast.SymbolLiteral.
    Lexical makeSymbolLiteralLexical (ITree tree, String string)
  {
    org.meta_environment.rascal.ast.SymbolLiteral.Lexical x =
      new org.meta_environment.rascal.ast.SymbolLiteral.Lexical (tree,
								 string);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.SymbolLiteral.Lexical) table.
      get (x);
  }
  public org.meta_environment.rascal.ast.Literal.
    String makeLiteralString (ITree tree,
			      org.meta_environment.rascal.ast.
			      StringLiteral stringLiteral)
  {
    org.meta_environment.rascal.ast.Literal.String x =
      new org.meta_environment.rascal.ast.Literal.String (tree,
							  stringLiteral);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Literal.String) table.get (x);
  }
  public org.meta_environment.rascal.ast.Literal.
    Double makeLiteralDouble (ITree tree,
			      org.meta_environment.rascal.ast.
			      FloatingPointLiteral doubleLiteral)
  {
    org.meta_environment.rascal.ast.Literal.Double x =
      new org.meta_environment.rascal.ast.Literal.Double (tree,
							  doubleLiteral);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Literal.Double) table.get (x);
  }
  public org.meta_environment.rascal.ast.Literal.
    Integer makeLiteralInteger (ITree tree,
				org.meta_environment.rascal.ast.
				IntegerLiteral integerLiteral)
  {
    org.meta_environment.rascal.ast.Literal.Integer x =
      new org.meta_environment.rascal.ast.Literal.Integer (tree,
							   integerLiteral);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Literal.Integer) table.get (x);
  }
  public org.meta_environment.rascal.ast.Literal.
    Boolean makeLiteralBoolean (ITree tree,
				org.meta_environment.rascal.ast.
				BooleanLiteral booleanLiteral)
  {
    org.meta_environment.rascal.ast.Literal.Boolean x =
      new org.meta_environment.rascal.ast.Literal.Boolean (tree,
							   booleanLiteral);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Literal.Boolean) table.get (x);
  }
  public org.meta_environment.rascal.ast.Literal.
    Symbol makeLiteralSymbol (ITree tree,
			      org.meta_environment.rascal.ast.
			      SymbolLiteral symbolLiteral)
  {
    org.meta_environment.rascal.ast.Literal.Symbol x =
      new org.meta_environment.rascal.ast.Literal.Symbol (tree,
							  symbolLiteral);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Literal.Symbol) table.get (x);
  }
  public org.meta_environment.rascal.ast.Literal.
    Ambiguity makeLiteralAmbiguity (java.util.LisT <
				    org.meta_environment.rascal.ast.Literal >
				    alternatives)
  {
    org.meta_environment.rascal.ast.Literal.Ambiguity amb =
      new org.meta_environment.rascal.ast.Literal.Ambiguity (alternatives);
    if (!table.containsKey (amb))
      {
	table.put (amb, amb);
      }
    return (org.meta_environment.rascal.ast.Literal.Ambiguity) table.
      get (amb);
  }
  public org.meta_environment.rascal.ast.Literal.
    RegExp makeLiteralRegExp (ITree tree,
			      org.meta_environment.rascal.ast.
			      RegExpLiteral regExpLiteral)
  {
    org.meta_environment.rascal.ast.Literal.RegExp x =
      new org.meta_environment.rascal.ast.Literal.RegExp (tree,
							  regExpLiteral);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Literal.RegExp) table.get (x);
  }
  public org.meta_environment.rascal.ast.RegExpLiteral.
    Ambiguity makeRegExpLiteralAmbiguity (java.util.LisT <
					  org.meta_environment.rascal.ast.
					  RegExpLiteral > alternatives)
  {
    org.meta_environment.rascal.ast.RegExpLiteral.Ambiguity amb =
      new org.meta_environment.rascal.ast.RegExpLiteral.
      Ambiguity (alternatives);
    if (!table.containsKey (amb))
      {
	table.put (amb, amb);
      }
    return (org.meta_environment.rascal.ast.RegExpLiteral.Ambiguity) table.
      get (amb);
  }
  public org.meta_environment.rascal.ast.RegExpLiteral.
    Lexical makeRegExpLiteralLexical (ITree tree, String string)
  {
    org.meta_environment.rascal.ast.RegExpLiteral.Lexical x =
      new org.meta_environment.rascal.ast.RegExpLiteral.Lexical (tree,
								 string);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.RegExpLiteral.Lexical) table.
      get (x);
  }
  public org.meta_environment.rascal.ast.RegExpModifier.
    Ambiguity makeRegExpModifierAmbiguity (java.util.LisT <
					   org.meta_environment.rascal.ast.
					   RegExpModifier > alternatives)
  {
    org.meta_environment.rascal.ast.RegExpModifier.Ambiguity amb =
      new org.meta_environment.rascal.ast.RegExpModifier.
      Ambiguity (alternatives);
    if (!table.containsKey (amb))
      {
	table.put (amb, amb);
      }
    return (org.meta_environment.rascal.ast.RegExpModifier.Ambiguity) table.
      get (amb);
  }
  public org.meta_environment.rascal.ast.RegExpModifier.
    Lexical makeRegExpModifierLexical (ITree tree, String string)
  {
    org.meta_environment.rascal.ast.RegExpModifier.Lexical x =
      new org.meta_environment.rascal.ast.RegExpModifier.Lexical (tree,
								  string);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.RegExpModifier.Lexical) table.
      get (x);
  }
  public org.meta_environment.rascal.ast.Backslash.
    Ambiguity makeBackslashAmbiguity (java.util.LisT <
				      org.meta_environment.rascal.ast.
				      Backslash > alternatives)
  {
    org.meta_environment.rascal.ast.Backslash.Ambiguity amb =
      new org.meta_environment.rascal.ast.Backslash.Ambiguity (alternatives);
    if (!table.containsKey (amb))
      {
	table.put (amb, amb);
      }
    return (org.meta_environment.rascal.ast.Backslash.Ambiguity) table.
      get (amb);
  }
  public org.meta_environment.rascal.ast.Backslash.
    Lexical makeBackslashLexical (ITree tree, String string)
  {
    org.meta_environment.rascal.ast.Backslash.Lexical x =
      new org.meta_environment.rascal.ast.Backslash.Lexical (tree, string);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Backslash.Lexical) table.get (x);
  }
  public org.meta_environment.rascal.ast.RegExp.
    Ambiguity makeRegExpAmbiguity (java.util.LisT <
				   org.meta_environment.rascal.ast.RegExp >
				   alternatives)
  {
    org.meta_environment.rascal.ast.RegExp.Ambiguity amb =
      new org.meta_environment.rascal.ast.RegExp.Ambiguity (alternatives);
    if (!table.containsKey (amb))
      {
	table.put (amb, amb);
      }
    return (org.meta_environment.rascal.ast.RegExp.Ambiguity) table.get (amb);
  }
  public org.meta_environment.rascal.ast.RegExp.
    Lexical makeRegExpLexical (ITree tree, String string)
  {
    org.meta_environment.rascal.ast.RegExp.Lexical x =
      new org.meta_environment.rascal.ast.RegExp.Lexical (tree, string);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.RegExp.Lexical) table.get (x);
  }
  public org.meta_environment.rascal.ast.NamedRegExp.
    Ambiguity makeNamedRegExpAmbiguity (java.util.LisT <
					org.meta_environment.rascal.ast.
					NamedRegExp > alternatives)
  {
    org.meta_environment.rascal.ast.NamedRegExp.Ambiguity amb =
      new org.meta_environment.rascal.ast.NamedRegExp.
      Ambiguity (alternatives);
    if (!table.containsKey (amb))
      {
	table.put (amb, amb);
      }
    return (org.meta_environment.rascal.ast.NamedRegExp.Ambiguity) table.
      get (amb);
  }
  public org.meta_environment.rascal.ast.NamedRegExp.
    Lexical makeNamedRegExpLexical (ITree tree, String string)
  {
    org.meta_environment.rascal.ast.NamedRegExp.Lexical x =
      new org.meta_environment.rascal.ast.NamedRegExp.Lexical (tree, string);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.NamedRegExp.Lexical) table.
      get (x);
  }
  public org.meta_environment.rascal.ast.NamedBackslash.
    Ambiguity makeNamedBackslashAmbiguity (java.util.LisT <
					   org.meta_environment.rascal.ast.
					   NamedBackslash > alternatives)
  {
    org.meta_environment.rascal.ast.NamedBackslash.Ambiguity amb =
      new org.meta_environment.rascal.ast.NamedBackslash.
      Ambiguity (alternatives);
    if (!table.containsKey (amb))
      {
	table.put (amb, amb);
      }
    return (org.meta_environment.rascal.ast.NamedBackslash.Ambiguity) table.
      get (amb);
  }
  public org.meta_environment.rascal.ast.NamedBackslash.
    Lexical makeNamedBackslashLexical (ITree tree, String string)
  {
    org.meta_environment.rascal.ast.NamedBackslash.Lexical x =
      new org.meta_environment.rascal.ast.NamedBackslash.Lexical (tree,
								  string);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.NamedBackslash.Lexical) table.
      get (x);
  }
  public org.meta_environment.rascal.ast.Bound.
    Default makeBoundDefault (ITree tree,
			      org.meta_environment.rascal.ast.
			      Expression expression)
  {
    org.meta_environment.rascal.ast.Bound.Default x =
      new org.meta_environment.rascal.ast.Bound.Default (tree, expression);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Bound.Default) table.get (x);
  }
  public org.meta_environment.rascal.ast.Bound.
    Ambiguity makeBoundAmbiguity (java.util.LisT <
				  org.meta_environment.rascal.ast.Bound >
				  alternatives)
  {
    org.meta_environment.rascal.ast.Bound.Ambiguity amb =
      new org.meta_environment.rascal.ast.Bound.Ambiguity (alternatives);
    if (!table.containsKey (amb))
      {
	table.put (amb, amb);
      }
    return (org.meta_environment.rascal.ast.Bound.Ambiguity) table.get (amb);
  }
  public org.meta_environment.rascal.ast.Bound.
    Empty makeBoundEmpty (ITree tree)
  {
    org.meta_environment.rascal.ast.Bound.Empty x =
      new org.meta_environment.rascal.ast.Bound.Empty (tree);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Bound.Empty) table.get (x);
  }
  public org.meta_environment.rascal.ast.Statement.
    GlobalDirective makeStatementGlobalDirective (ITree tree,
						  org.meta_environment.rascal.
						  ast.Type type,
						  java.util.LisT <
						  org.meta_environment.rascal.
						  ast.QualifiedName > names)
  {
    org.meta_environment.rascal.ast.Statement.GlobalDirective x =
      new org.meta_environment.rascal.ast.Statement.GlobalDirective (tree,
								     type,
								     names);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Statement.GlobalDirective) table.
      get (x);
  }
  public org.meta_environment.rascal.ast.Statement.
    VariableDeclaration makeStatementVariableDeclaration (ITree tree,
							  org.
							  meta_environment.
							  rascal.ast.
							  LocalVariableDeclaration
							  declaration)
  {
    org.meta_environment.rascal.ast.Statement.VariableDeclaration x =
      new org.meta_environment.rascal.ast.Statement.VariableDeclaration (tree,
									 declaration);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Statement.
	    VariableDeclaration) table.get (x);
  }
  public org.meta_environment.rascal.ast.Statement.
    FunctionDeclaration makeStatementFunctionDeclaration (ITree tree,
							  org.
							  meta_environment.
							  rascal.ast.
							  FunctionDeclaration
							  functionDeclaration)
  {
    org.meta_environment.rascal.ast.Statement.FunctionDeclaration x =
      new org.meta_environment.rascal.ast.Statement.FunctionDeclaration (tree,
									 functionDeclaration);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Statement.
	    FunctionDeclaration) table.get (x);
  }
  public org.meta_environment.rascal.ast.Statement.
    Block makeStatementBlock (ITree tree,
			      org.meta_environment.rascal.ast.Label label,
			      java.util.LisT <
			      org.meta_environment.rascal.ast.Statement >
			      statements)
  {
    org.meta_environment.rascal.ast.Statement.Block x =
      new org.meta_environment.rascal.ast.Statement.Block (tree, label,
							   statements);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Statement.Block) table.get (x);
  }
  public org.meta_environment.rascal.ast.Statement.
    TryFinally makeStatementTryFinally (ITree tree,
					org.meta_environment.rascal.ast.
					Statement body,
					java.util.LisT <
					org.meta_environment.rascal.ast.
					Catch > handlers,
					org.meta_environment.rascal.ast.
					Statement finallyBody)
  {
    org.meta_environment.rascal.ast.Statement.TryFinally x =
      new org.meta_environment.rascal.ast.Statement.TryFinally (tree, body,
								handlers,
								finallyBody);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Statement.TryFinally) table.
      get (x);
  }
  public org.meta_environment.rascal.ast.Statement.
    Try makeStatementTry (ITree tree,
			  org.meta_environment.rascal.ast.Statement body,
			  java.util.LisT <
			  org.meta_environment.rascal.ast.Catch > handlers)
  {
    org.meta_environment.rascal.ast.Statement.Try x =
      new org.meta_environment.rascal.ast.Statement.Try (tree, body,
							 handlers);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Statement.Try) table.get (x);
  }
  public org.meta_environment.rascal.ast.Statement.
    Throw makeStatementThrow (ITree tree,
			      org.meta_environment.rascal.ast.
			      Expression expression)
  {
    org.meta_environment.rascal.ast.Statement.Throw x =
      new org.meta_environment.rascal.ast.Statement.Throw (tree, expression);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Statement.Throw) table.get (x);
  }
  public org.meta_environment.rascal.ast.Statement.
    Insert makeStatementInsert (ITree tree,
				org.meta_environment.rascal.ast.
				Expression expression)
  {
    org.meta_environment.rascal.ast.Statement.Insert x =
      new org.meta_environment.rascal.ast.Statement.Insert (tree, expression);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Statement.Insert) table.get (x);
  }
  public org.meta_environment.rascal.ast.Statement.
    Assert makeStatementAssert (ITree tree,
				org.meta_environment.rascal.ast.
				StringLiteral message,
				org.meta_environment.rascal.ast.
				Expression expression)
  {
    org.meta_environment.rascal.ast.Statement.Assert x =
      new org.meta_environment.rascal.ast.Statement.Assert (tree, message,
							    expression);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Statement.Assert) table.get (x);
  }
  public org.meta_environment.rascal.ast.Statement.
    Continue makeStatementContinue (ITree tree)
  {
    org.meta_environment.rascal.ast.Statement.Continue x =
      new org.meta_environment.rascal.ast.Statement.Continue (tree);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Statement.Continue) table.get (x);
  }
  public org.meta_environment.rascal.ast.Statement.
    Return makeStatementReturn (ITree tree,
				org.meta_environment.rascal.ast.Return ret)
  {
    org.meta_environment.rascal.ast.Statement.Return x =
      new org.meta_environment.rascal.ast.Statement.Return (tree, ret);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Statement.Return) table.get (x);
  }
  public org.meta_environment.rascal.ast.Statement.
    Fail makeStatementFail (ITree tree,
			    org.meta_environment.rascal.ast.Fail fail)
  {
    org.meta_environment.rascal.ast.Statement.Fail x =
      new org.meta_environment.rascal.ast.Statement.Fail (tree, fail);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Statement.Fail) table.get (x);
  }
  public org.meta_environment.rascal.ast.Statement.
    Break makeStatementBreak (ITree tree,
			      org.meta_environment.rascal.ast.Break brk)
  {
    org.meta_environment.rascal.ast.Statement.Break x =
      new org.meta_environment.rascal.ast.Statement.Break (tree, brk);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Statement.Break) table.get (x);
  }
  public org.meta_environment.rascal.ast.Statement.
    Assignment makeStatementAssignment (ITree tree,
					java.util.LisT <
					org.meta_environment.rascal.ast.
					Assignable > assignables,
					org.meta_environment.rascal.ast.
					Assignment operator,
					java.util.LisT <
					org.meta_environment.rascal.ast.
					Expression > expressions)
  {
    org.meta_environment.rascal.ast.Statement.Assignment x =
      new org.meta_environment.rascal.ast.Statement.Assignment (tree,
								assignables,
								operator,
								expressions);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Statement.Assignment) table.
      get (x);
  }
  public org.meta_environment.rascal.ast.Statement.
    VisIt makeStatementVisIt (ITree tree,
			      org.meta_environment.rascal.ast.VisIt visIt)
  {
    org.meta_environment.rascal.ast.Statement.VisIt x =
      new org.meta_environment.rascal.ast.Statement.VisIt (tree, visIt);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Statement.VisIt) table.get (x);
  }
  public org.meta_environment.rascal.ast.Statement.
    Expression makeStatementExpression (ITree tree,
					org.meta_environment.rascal.ast.
					Expression expression)
  {
    org.meta_environment.rascal.ast.Statement.Expression x =
      new org.meta_environment.rascal.ast.Statement.Expression (tree,
								expression);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Statement.Expression) table.
      get (x);
  }
  public org.meta_environment.rascal.ast.Statement.
    Switch makeStatementSwitch (ITree tree,
				org.meta_environment.rascal.ast.Label label,
				org.meta_environment.rascal.ast.
				Expression expression,
				java.util.LisT <
				org.meta_environment.rascal.ast.Case > cases)
  {
    org.meta_environment.rascal.ast.Statement.Switch x =
      new org.meta_environment.rascal.ast.Statement.Switch (tree, label,
							    expression,
							    cases);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Statement.Switch) table.get (x);
  }
  public org.meta_environment.rascal.ast.Statement.
    IfThen makeStatementIfThen (ITree tree,
				org.meta_environment.rascal.ast.Label label,
				java.util.LisT <
				org.meta_environment.rascal.ast.Expression >
				conditions,
				org.meta_environment.rascal.ast.
				Statement thenStatement,
				org.meta_environment.rascal.ast.
				NoElseMayFollow noElseMayFollow)
  {
    org.meta_environment.rascal.ast.Statement.IfThen x =
      new org.meta_environment.rascal.ast.Statement.IfThen (tree, label,
							    conditions,
							    thenStatement,
							    noElseMayFollow);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Statement.IfThen) table.get (x);
  }
  public org.meta_environment.rascal.ast.Statement.
    IfThenElse makeStatementIfThenElse (ITree tree,
					org.meta_environment.rascal.ast.
					Label label,
					java.util.LisT <
					org.meta_environment.rascal.ast.
					Expression > conditions,
					org.meta_environment.rascal.ast.
					Statement thenStatement,
					org.meta_environment.rascal.ast.
					Statement elseStatement)
  {
    org.meta_environment.rascal.ast.Statement.IfThenElse x =
      new org.meta_environment.rascal.ast.Statement.IfThenElse (tree, label,
								conditions,
								thenStatement,
								elseStatement);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Statement.IfThenElse) table.
      get (x);
  }
  public org.meta_environment.rascal.ast.Statement.
    DoWhile makeStatementDoWhile (ITree tree,
				  org.meta_environment.rascal.ast.Label label,
				  org.meta_environment.rascal.ast.
				  Statement body,
				  org.meta_environment.rascal.ast.
				  Expression condition)
  {
    org.meta_environment.rascal.ast.Statement.DoWhile x =
      new org.meta_environment.rascal.ast.Statement.DoWhile (tree, label,
							     body, condition);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Statement.DoWhile) table.get (x);
  }
  public org.meta_environment.rascal.ast.Statement.
    While makeStatementWhile (ITree tree,
			      org.meta_environment.rascal.ast.Label label,
			      org.meta_environment.rascal.ast.
			      Expression condition,
			      org.meta_environment.rascal.ast.Statement body)
  {
    org.meta_environment.rascal.ast.Statement.While x =
      new org.meta_environment.rascal.ast.Statement.While (tree, label,
							   condition, body);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Statement.While) table.get (x);
  }
  public org.meta_environment.rascal.ast.Statement.
    First makeStatementFirst (ITree tree,
			      org.meta_environment.rascal.ast.Label label,
			      java.util.LisT <
			      org.meta_environment.rascal.ast.Generator >
			      generators,
			      org.meta_environment.rascal.ast.Statement body)
  {
    org.meta_environment.rascal.ast.Statement.First x =
      new org.meta_environment.rascal.ast.Statement.First (tree, label,
							   generators, body);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Statement.First) table.get (x);
  }
  public org.meta_environment.rascal.ast.Statement.
    All makeStatementAll (ITree tree,
			  org.meta_environment.rascal.ast.Label label,
			  java.util.LisT <
			  org.meta_environment.rascal.ast.Generator >
			  generators,
			  org.meta_environment.rascal.ast.Statement body)
  {
    org.meta_environment.rascal.ast.Statement.All x =
      new org.meta_environment.rascal.ast.Statement.All (tree, label,
							 generators, body);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Statement.All) table.get (x);
  }
  public org.meta_environment.rascal.ast.Statement.
    For makeStatementFor (ITree tree,
			  org.meta_environment.rascal.ast.Label label,
			  java.util.LisT <
			  org.meta_environment.rascal.ast.Generator >
			  generators,
			  org.meta_environment.rascal.ast.Statement body)
  {
    org.meta_environment.rascal.ast.Statement.For x =
      new org.meta_environment.rascal.ast.Statement.For (tree, label,
							 generators, body);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Statement.For) table.get (x);
  }
  public org.meta_environment.rascal.ast.Statement.
    Ambiguity makeStatementAmbiguity (java.util.LisT <
				      org.meta_environment.rascal.ast.
				      Statement > alternatives)
  {
    org.meta_environment.rascal.ast.Statement.Ambiguity amb =
      new org.meta_environment.rascal.ast.Statement.Ambiguity (alternatives);
    if (!table.containsKey (amb))
      {
	table.put (amb, amb);
      }
    return (org.meta_environment.rascal.ast.Statement.Ambiguity) table.
      get (amb);
  }
  public org.meta_environment.rascal.ast.Statement.
    Solve makeStatementSolve (ITree tree,
			      java.util.LisT <
			      org.meta_environment.rascal.ast.Declarator >
			      declarations,
			      org.meta_environment.rascal.ast.Statement body)
  {
    org.meta_environment.rascal.ast.Statement.Solve x =
      new org.meta_environment.rascal.ast.Statement.Solve (tree, declarations,
							   body);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Statement.Solve) table.get (x);
  }
  public org.meta_environment.rascal.ast.NoElseMayFollow.
    Ambiguity makeNoElseMayFollowAmbiguity (java.util.LisT <
					    org.meta_environment.rascal.ast.
					    NoElseMayFollow > alternatives)
  {
    org.meta_environment.rascal.ast.NoElseMayFollow.Ambiguity amb =
      new org.meta_environment.rascal.ast.NoElseMayFollow.
      Ambiguity (alternatives);
    if (!table.containsKey (amb))
      {
	table.put (amb, amb);
      }
    return (org.meta_environment.rascal.ast.NoElseMayFollow.Ambiguity) table.
      get (amb);
  }
  public org.meta_environment.rascal.ast.NoElseMayFollow.
    Default makeNoElseMayFollowDefault (ITree tree)
  {
    org.meta_environment.rascal.ast.NoElseMayFollow.Default x =
      new org.meta_environment.rascal.ast.NoElseMayFollow.Default (tree);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.NoElseMayFollow.Default) table.
      get (x);
  }
  public org.meta_environment.rascal.ast.Assignable.
    Constructor makeAssignableConstructor (ITree tree,
					   org.meta_environment.rascal.ast.
					   Name name,
					   java.util.LisT <
					   org.meta_environment.rascal.ast.
					   Assignable > arguments)
  {
    org.meta_environment.rascal.ast.Assignable.Constructor x =
      new org.meta_environment.rascal.ast.Assignable.Constructor (tree, name,
								  arguments);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Assignable.Constructor) table.
      get (x);
  }
  public org.meta_environment.rascal.ast.Assignable.
    Tuple makeAssignableTuple (ITree tree,
			       org.meta_environment.rascal.ast.
			       Assignable first,
			       java.util.LisT <
			       org.meta_environment.rascal.ast.Assignable >
			       rest)
  {
    org.meta_environment.rascal.ast.Assignable.Tuple x =
      new org.meta_environment.rascal.ast.Assignable.Tuple (tree, first,
							    rest);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Assignable.Tuple) table.get (x);
  }
  public org.meta_environment.rascal.ast.Assignable.
    Annotation makeAssignableAnnotation (ITree tree,
					 org.meta_environment.rascal.ast.
					 Assignable receiver,
					 org.meta_environment.rascal.ast.
					 Expression annotation)
  {
    org.meta_environment.rascal.ast.Assignable.Annotation x =
      new org.meta_environment.rascal.ast.Assignable.Annotation (tree,
								 receiver,
								 annotation);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Assignable.Annotation) table.
      get (x);
  }
  public org.meta_environment.rascal.ast.Assignable.
    IfDefined makeAssignableIfDefined (ITree tree,
				       org.meta_environment.rascal.ast.
				       Assignable receiver,
				       org.meta_environment.rascal.ast.
				       Expression condition)
  {
    org.meta_environment.rascal.ast.Assignable.IfDefined x =
      new org.meta_environment.rascal.ast.Assignable.IfDefined (tree,
								receiver,
								condition);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Assignable.IfDefined) table.
      get (x);
  }
  public org.meta_environment.rascal.ast.Assignable.
    FieldAccess makeAssignableFieldAccess (ITree tree,
					   org.meta_environment.rascal.ast.
					   Assignable receiver,
					   org.meta_environment.rascal.ast.
					   Name field)
  {
    org.meta_environment.rascal.ast.Assignable.FieldAccess x =
      new org.meta_environment.rascal.ast.Assignable.FieldAccess (tree,
								  receiver,
								  field);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Assignable.FieldAccess) table.
      get (x);
  }
  public org.meta_environment.rascal.ast.Assignable.
    Subscript makeAssignableSubscript (ITree tree,
				       org.meta_environment.rascal.ast.
				       Assignable receiver,
				       org.meta_environment.rascal.ast.
				       Expression subscript)
  {
    org.meta_environment.rascal.ast.Assignable.Subscript x =
      new org.meta_environment.rascal.ast.Assignable.Subscript (tree,
								receiver,
								subscript);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Assignable.Subscript) table.
      get (x);
  }
  public org.meta_environment.rascal.ast.Assignable.
    Ambiguity makeAssignableAmbiguity (java.util.LisT <
				       org.meta_environment.rascal.ast.
				       Assignable > alternatives)
  {
    org.meta_environment.rascal.ast.Assignable.Ambiguity amb =
      new org.meta_environment.rascal.ast.Assignable.Ambiguity (alternatives);
    if (!table.containsKey (amb))
      {
	table.put (amb, amb);
      }
    return (org.meta_environment.rascal.ast.Assignable.Ambiguity) table.
      get (amb);
  }
  public org.meta_environment.rascal.ast.Assignable.
    Variable makeAssignableVariable (ITree tree,
				     org.meta_environment.rascal.ast.
				     QualifiedName qualifiedName)
  {
    org.meta_environment.rascal.ast.Assignable.Variable x =
      new org.meta_environment.rascal.ast.Assignable.Variable (tree,
							       qualifiedName);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Assignable.Variable) table.
      get (x);
  }
  public org.meta_environment.rascal.ast.Assignment.
    Interesection makeAssignmentInteresection (ITree tree)
  {
    org.meta_environment.rascal.ast.Assignment.Interesection x =
      new org.meta_environment.rascal.ast.Assignment.Interesection (tree);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Assignment.Interesection) table.
      get (x);
  }
  public org.meta_environment.rascal.ast.Assignment.
    DivisIon makeAssignmentDivisIon (ITree tree)
  {
    org.meta_environment.rascal.ast.Assignment.DivisIon x =
      new org.meta_environment.rascal.ast.Assignment.DivisIon (tree);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Assignment.DivisIon) table.
      get (x);
  }
  public org.meta_environment.rascal.ast.Assignment.
    Product makeAssignmentProduct (ITree tree)
  {
    org.meta_environment.rascal.ast.Assignment.Product x =
      new org.meta_environment.rascal.ast.Assignment.Product (tree);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Assignment.Product) table.get (x);
  }
  public org.meta_environment.rascal.ast.Assignment.
    Substraction makeAssignmentSubstraction (ITree tree)
  {
    org.meta_environment.rascal.ast.Assignment.Substraction x =
      new org.meta_environment.rascal.ast.Assignment.Substraction (tree);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Assignment.Substraction) table.
      get (x);
  }
  public org.meta_environment.rascal.ast.Assignment.
    Addition makeAssignmentAddition (ITree tree)
  {
    org.meta_environment.rascal.ast.Assignment.Addition x =
      new org.meta_environment.rascal.ast.Assignment.Addition (tree);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Assignment.Addition) table.
      get (x);
  }
  public org.meta_environment.rascal.ast.Assignment.
    Ambiguity makeAssignmentAmbiguity (java.util.LisT <
				       org.meta_environment.rascal.ast.
				       Assignment > alternatives)
  {
    org.meta_environment.rascal.ast.Assignment.Ambiguity amb =
      new org.meta_environment.rascal.ast.Assignment.Ambiguity (alternatives);
    if (!table.containsKey (amb))
      {
	table.put (amb, amb);
      }
    return (org.meta_environment.rascal.ast.Assignment.Ambiguity) table.
      get (amb);
  }
  public org.meta_environment.rascal.ast.Assignment.
    Default makeAssignmentDefault (ITree tree)
  {
    org.meta_environment.rascal.ast.Assignment.Default x =
      new org.meta_environment.rascal.ast.Assignment.Default (tree);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Assignment.Default) table.get (x);
  }
  public org.meta_environment.rascal.ast.Label.
    Default makeLabelDefault (ITree tree,
			      org.meta_environment.rascal.ast.Name name)
  {
    org.meta_environment.rascal.ast.Label.Default x =
      new org.meta_environment.rascal.ast.Label.Default (tree, name);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Label.Default) table.get (x);
  }
  public org.meta_environment.rascal.ast.Label.
    Ambiguity makeLabelAmbiguity (java.util.LisT <
				  org.meta_environment.rascal.ast.Label >
				  alternatives)
  {
    org.meta_environment.rascal.ast.Label.Ambiguity amb =
      new org.meta_environment.rascal.ast.Label.Ambiguity (alternatives);
    if (!table.containsKey (amb))
      {
	table.put (amb, amb);
      }
    return (org.meta_environment.rascal.ast.Label.Ambiguity) table.get (amb);
  }
  public org.meta_environment.rascal.ast.Label.
    Empty makeLabelEmpty (ITree tree)
  {
    org.meta_environment.rascal.ast.Label.Empty x =
      new org.meta_environment.rascal.ast.Label.Empty (tree);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Label.Empty) table.get (x);
  }
  public org.meta_environment.rascal.ast.Break.
    NoLabel makeBreakNoLabel (ITree tree)
  {
    org.meta_environment.rascal.ast.Break.NoLabel x =
      new org.meta_environment.rascal.ast.Break.NoLabel (tree);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Break.NoLabel) table.get (x);
  }
  public org.meta_environment.rascal.ast.Break.
    Ambiguity makeBreakAmbiguity (java.util.LisT <
				  org.meta_environment.rascal.ast.Break >
				  alternatives)
  {
    org.meta_environment.rascal.ast.Break.Ambiguity amb =
      new org.meta_environment.rascal.ast.Break.Ambiguity (alternatives);
    if (!table.containsKey (amb))
      {
	table.put (amb, amb);
      }
    return (org.meta_environment.rascal.ast.Break.Ambiguity) table.get (amb);
  }
  public org.meta_environment.rascal.ast.Break.
    WithLabel makeBreakWithLabel (ITree tree,
				  org.meta_environment.rascal.ast.Name label)
  {
    org.meta_environment.rascal.ast.Break.WithLabel x =
      new org.meta_environment.rascal.ast.Break.WithLabel (tree, label);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Break.WithLabel) table.get (x);
  }
  public org.meta_environment.rascal.ast.Fail.
    NoLabel makeFailNoLabel (ITree tree)
  {
    org.meta_environment.rascal.ast.Fail.NoLabel x =
      new org.meta_environment.rascal.ast.Fail.NoLabel (tree);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Fail.NoLabel) table.get (x);
  }
  public org.meta_environment.rascal.ast.Fail.
    Ambiguity makeFailAmbiguity (java.util.LisT <
				 org.meta_environment.rascal.ast.Fail >
				 alternatives)
  {
    org.meta_environment.rascal.ast.Fail.Ambiguity amb =
      new org.meta_environment.rascal.ast.Fail.Ambiguity (alternatives);
    if (!table.containsKey (amb))
      {
	table.put (amb, amb);
      }
    return (org.meta_environment.rascal.ast.Fail.Ambiguity) table.get (amb);
  }
  public org.meta_environment.rascal.ast.Fail.
    WithLabel makeFailWithLabel (ITree tree,
				 org.meta_environment.rascal.ast.Name label)
  {
    org.meta_environment.rascal.ast.Fail.WithLabel x =
      new org.meta_environment.rascal.ast.Fail.WithLabel (tree, label);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Fail.WithLabel) table.get (x);
  }
  public org.meta_environment.rascal.ast.Return.
    NoExpression makeReturnNoExpression (ITree tree)
  {
    org.meta_environment.rascal.ast.Return.NoExpression x =
      new org.meta_environment.rascal.ast.Return.NoExpression (tree);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Return.NoExpression) table.
      get (x);
  }
  public org.meta_environment.rascal.ast.Return.
    Ambiguity makeReturnAmbiguity (java.util.LisT <
				   org.meta_environment.rascal.ast.Return >
				   alternatives)
  {
    org.meta_environment.rascal.ast.Return.Ambiguity amb =
      new org.meta_environment.rascal.ast.Return.Ambiguity (alternatives);
    if (!table.containsKey (amb))
      {
	table.put (amb, amb);
      }
    return (org.meta_environment.rascal.ast.Return.Ambiguity) table.get (amb);
  }
  public org.meta_environment.rascal.ast.Return.
    WithExpression makeReturnWithExpression (ITree tree,
					     org.meta_environment.rascal.ast.
					     Expression expression)
  {
    org.meta_environment.rascal.ast.Return.WithExpression x =
      new org.meta_environment.rascal.ast.Return.WithExpression (tree,
								 expression);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Return.WithExpression) table.
      get (x);
  }
  public org.meta_environment.rascal.ast.Catch.
    Binding makeCatchBinding (ITree tree,
			      org.meta_environment.rascal.ast.Type type,
			      org.meta_environment.rascal.ast.Name name,
			      org.meta_environment.rascal.ast.Statement body)
  {
    org.meta_environment.rascal.ast.Catch.Binding x =
      new org.meta_environment.rascal.ast.Catch.Binding (tree, type, name,
							 body);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Catch.Binding) table.get (x);
  }
  public org.meta_environment.rascal.ast.Catch.
    Ambiguity makeCatchAmbiguity (java.util.LisT <
				  org.meta_environment.rascal.ast.Catch >
				  alternatives)
  {
    org.meta_environment.rascal.ast.Catch.Ambiguity amb =
      new org.meta_environment.rascal.ast.Catch.Ambiguity (alternatives);
    if (!table.containsKey (amb))
      {
	table.put (amb, amb);
      }
    return (org.meta_environment.rascal.ast.Catch.Ambiguity) table.get (amb);
  }
  public org.meta_environment.rascal.ast.Catch.
    Default makeCatchDefault (ITree tree,
			      org.meta_environment.rascal.ast.Statement body)
  {
    org.meta_environment.rascal.ast.Catch.Default x =
      new org.meta_environment.rascal.ast.Catch.Default (tree, body);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Catch.Default) table.get (x);
  }
  public org.meta_environment.rascal.ast.Declarator.
    Ambiguity makeDeclaratorAmbiguity (java.util.LisT <
				       org.meta_environment.rascal.ast.
				       Declarator > alternatives)
  {
    org.meta_environment.rascal.ast.Declarator.Ambiguity amb =
      new org.meta_environment.rascal.ast.Declarator.Ambiguity (alternatives);
    if (!table.containsKey (amb))
      {
	table.put (amb, amb);
      }
    return (org.meta_environment.rascal.ast.Declarator.Ambiguity) table.
      get (amb);
  }
  public org.meta_environment.rascal.ast.Declarator.
    Default makeDeclaratorDefault (ITree tree,
				   org.meta_environment.rascal.ast.Type type,
				   java.util.LisT <
				   org.meta_environment.rascal.ast.Variable >
				   variables)
  {
    org.meta_environment.rascal.ast.Declarator.Default x =
      new org.meta_environment.rascal.ast.Declarator.Default (tree, type,
							      variables);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Declarator.Default) table.get (x);
  }
  public org.meta_environment.rascal.ast.LocalVariableDeclaration.
    Dynamic makeLocalVariableDeclarationDynamic (ITree tree,
						 org.meta_environment.rascal.
						 ast.Declarator declarator)
  {
    org.meta_environment.rascal.ast.LocalVariableDeclaration.Dynamic x =
      new org.meta_environment.rascal.ast.LocalVariableDeclaration.
      Dynamic (tree, declarator);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.LocalVariableDeclaration.
	    Dynamic) table.get (x);
  }
  public org.meta_environment.rascal.ast.LocalVariableDeclaration.
    Ambiguity makeLocalVariableDeclarationAmbiguity (java.util.LisT <
						     org.meta_environment.
						     rascal.ast.
						     LocalVariableDeclaration
						     > alternatives)
  {
    org.meta_environment.rascal.ast.LocalVariableDeclaration.Ambiguity amb =
      new org.meta_environment.rascal.ast.LocalVariableDeclaration.
      Ambiguity (alternatives);
    if (!table.containsKey (amb))
      {
	table.put (amb, amb);
      }
    return (org.meta_environment.rascal.ast.LocalVariableDeclaration.
	    Ambiguity) table.get (amb);
  }
  public org.meta_environment.rascal.ast.LocalVariableDeclaration.
    Default makeLocalVariableDeclarationDefault (ITree tree,
						 org.meta_environment.rascal.
						 ast.Declarator declarator)
  {
    org.meta_environment.rascal.ast.LocalVariableDeclaration.Default x =
      new org.meta_environment.rascal.ast.LocalVariableDeclaration.
      Default (tree, declarator);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.LocalVariableDeclaration.
	    Default) table.get (x);
  }
  public org.meta_environment.rascal.ast.Formal.
    Ambiguity makeFormalAmbiguity (java.util.LisT <
				   org.meta_environment.rascal.ast.Formal >
				   alternatives)
  {
    org.meta_environment.rascal.ast.Formal.Ambiguity amb =
      new org.meta_environment.rascal.ast.Formal.Ambiguity (alternatives);
    if (!table.containsKey (amb))
      {
	table.put (amb, amb);
      }
    return (org.meta_environment.rascal.ast.Formal.Ambiguity) table.get (amb);
  }
  public org.meta_environment.rascal.ast.Formal.
    TypeName makeFormalTypeName (ITree tree,
				 org.meta_environment.rascal.ast.Type type,
				 org.meta_environment.rascal.ast.Name name)
  {
    org.meta_environment.rascal.ast.Formal.TypeName x =
      new org.meta_environment.rascal.ast.Formal.TypeName (tree, type, name);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Formal.TypeName) table.get (x);
  }
  public org.meta_environment.rascal.ast.Formals.
    Ambiguity makeFormalsAmbiguity (java.util.LisT <
				    org.meta_environment.rascal.ast.Formals >
				    alternatives)
  {
    org.meta_environment.rascal.ast.Formals.Ambiguity amb =
      new org.meta_environment.rascal.ast.Formals.Ambiguity (alternatives);
    if (!table.containsKey (amb))
      {
	table.put (amb, amb);
      }
    return (org.meta_environment.rascal.ast.Formals.Ambiguity) table.
      get (amb);
  }
  public org.meta_environment.rascal.ast.Formals.
    Default makeFormalsDefault (ITree tree,
				java.util.LisT <
				org.meta_environment.rascal.ast.Formal >
				formals)
  {
    org.meta_environment.rascal.ast.Formals.Default x =
      new org.meta_environment.rascal.ast.Formals.Default (tree, formals);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Formals.Default) table.get (x);
  }
  public org.meta_environment.rascal.ast.Parameters.
    VarArgs makeParametersVarArgs (ITree tree,
				   org.meta_environment.rascal.ast.
				   Formals formals)
  {
    org.meta_environment.rascal.ast.Parameters.VarArgs x =
      new org.meta_environment.rascal.ast.Parameters.VarArgs (tree, formals);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Parameters.VarArgs) table.get (x);
  }
  public org.meta_environment.rascal.ast.Parameters.
    Ambiguity makeParametersAmbiguity (java.util.LisT <
				       org.meta_environment.rascal.ast.
				       Parameters > alternatives)
  {
    org.meta_environment.rascal.ast.Parameters.Ambiguity amb =
      new org.meta_environment.rascal.ast.Parameters.Ambiguity (alternatives);
    if (!table.containsKey (amb))
      {
	table.put (amb, amb);
      }
    return (org.meta_environment.rascal.ast.Parameters.Ambiguity) table.
      get (amb);
  }
  public org.meta_environment.rascal.ast.Parameters.
    Default makeParametersDefault (ITree tree,
				   org.meta_environment.rascal.ast.
				   Formals formals)
  {
    org.meta_environment.rascal.ast.Parameters.Default x =
      new org.meta_environment.rascal.ast.Parameters.Default (tree, formals);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Parameters.Default) table.get (x);
  }
  public org.meta_environment.rascal.ast.Name.
    Ambiguity makeNameAmbiguity (java.util.LisT <
				 org.meta_environment.rascal.ast.Name >
				 alternatives)
  {
    org.meta_environment.rascal.ast.Name.Ambiguity amb =
      new org.meta_environment.rascal.ast.Name.Ambiguity (alternatives);
    if (!table.containsKey (amb))
      {
	table.put (amb, amb);
      }
    return (org.meta_environment.rascal.ast.Name.Ambiguity) table.get (amb);
  }
  public org.meta_environment.rascal.ast.Name.
    Lexical makeNameLexical (ITree tree, String string)
  {
    org.meta_environment.rascal.ast.Name.Lexical x =
      new org.meta_environment.rascal.ast.Name.Lexical (tree, string);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Name.Lexical) table.get (x);
  }
  public org.meta_environment.rascal.ast.QualifiedName.
    Ambiguity makeQualifiedNameAmbiguity (java.util.LisT <
					  org.meta_environment.rascal.ast.
					  QualifiedName > alternatives)
  {
    org.meta_environment.rascal.ast.QualifiedName.Ambiguity amb =
      new org.meta_environment.rascal.ast.QualifiedName.
      Ambiguity (alternatives);
    if (!table.containsKey (amb))
      {
	table.put (amb, amb);
      }
    return (org.meta_environment.rascal.ast.QualifiedName.Ambiguity) table.
      get (amb);
  }
  public org.meta_environment.rascal.ast.QualifiedName.
    Default makeQualifiedNameDefault (ITree tree,
				      java.util.LisT <
				      org.meta_environment.rascal.ast.Name >
				      names)
  {
    org.meta_environment.rascal.ast.QualifiedName.Default x =
      new org.meta_environment.rascal.ast.QualifiedName.Default (tree, names);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.QualifiedName.Default) table.
      get (x);
  }
  public org.meta_environment.rascal.ast.Module.
    Ambiguity makeModuleAmbiguity (java.util.LisT <
				   org.meta_environment.rascal.ast.Module >
				   alternatives)
  {
    org.meta_environment.rascal.ast.Module.Ambiguity amb =
      new org.meta_environment.rascal.ast.Module.Ambiguity (alternatives);
    if (!table.containsKey (amb))
      {
	table.put (amb, amb);
      }
    return (org.meta_environment.rascal.ast.Module.Ambiguity) table.get (amb);
  }
  public org.meta_environment.rascal.ast.Module.
    Default makeModuleDefault (ITree tree,
			       org.meta_environment.rascal.ast.Header header,
			       org.meta_environment.rascal.ast.Body body)
  {
    org.meta_environment.rascal.ast.Module.Default x =
      new org.meta_environment.rascal.ast.Module.Default (tree, header, body);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Module.Default) table.get (x);
  }
  public org.meta_environment.rascal.ast.ModuleWord.
    Ambiguity makeModuleWordAmbiguity (java.util.LisT <
				       org.meta_environment.rascal.ast.
				       ModuleWord > alternatives)
  {
    org.meta_environment.rascal.ast.ModuleWord.Ambiguity amb =
      new org.meta_environment.rascal.ast.ModuleWord.Ambiguity (alternatives);
    if (!table.containsKey (amb))
      {
	table.put (amb, amb);
      }
    return (org.meta_environment.rascal.ast.ModuleWord.Ambiguity) table.
      get (amb);
  }
  public org.meta_environment.rascal.ast.ModuleWord.
    Lexical makeModuleWordLexical (ITree tree, String string)
  {
    org.meta_environment.rascal.ast.ModuleWord.Lexical x =
      new org.meta_environment.rascal.ast.ModuleWord.Lexical (tree, string);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.ModuleWord.Lexical) table.get (x);
  }
  public org.meta_environment.rascal.ast.ModuleName.
    Ambiguity makeModuleNameAmbiguity (java.util.LisT <
				       org.meta_environment.rascal.ast.
				       ModuleName > alternatives)
  {
    org.meta_environment.rascal.ast.ModuleName.Ambiguity amb =
      new org.meta_environment.rascal.ast.ModuleName.Ambiguity (alternatives);
    if (!table.containsKey (amb))
      {
	table.put (amb, amb);
      }
    return (org.meta_environment.rascal.ast.ModuleName.Ambiguity) table.
      get (amb);
  }
  public org.meta_environment.rascal.ast.ModuleName.
    Lexical makeModuleNameLexical (ITree tree, String string)
  {
    org.meta_environment.rascal.ast.ModuleName.Lexical x =
      new org.meta_environment.rascal.ast.ModuleName.Lexical (tree, string);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.ModuleName.Lexical) table.get (x);
  }
  public org.meta_environment.rascal.ast.ModuleActuals.
    Ambiguity makeModuleActualsAmbiguity (java.util.LisT <
					  org.meta_environment.rascal.ast.
					  ModuleActuals > alternatives)
  {
    org.meta_environment.rascal.ast.ModuleActuals.Ambiguity amb =
      new org.meta_environment.rascal.ast.ModuleActuals.
      Ambiguity (alternatives);
    if (!table.containsKey (amb))
      {
	table.put (amb, amb);
      }
    return (org.meta_environment.rascal.ast.ModuleActuals.Ambiguity) table.
      get (amb);
  }
  public org.meta_environment.rascal.ast.ModuleActuals.
    Default makeModuleActualsDefault (ITree tree,
				      java.util.LisT <
				      org.meta_environment.rascal.ast.Type >
				      types)
  {
    org.meta_environment.rascal.ast.ModuleActuals.Default x =
      new org.meta_environment.rascal.ast.ModuleActuals.Default (tree, types);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.ModuleActuals.Default) table.
      get (x);
  }
  public org.meta_environment.rascal.ast.ImportedModule.
    Default makeImportedModuleDefault (ITree tree,
				       org.meta_environment.rascal.ast.
				       ModuleName name)
  {
    org.meta_environment.rascal.ast.ImportedModule.Default x =
      new org.meta_environment.rascal.ast.ImportedModule.Default (tree, name);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.ImportedModule.Default) table.
      get (x);
  }
  public org.meta_environment.rascal.ast.ImportedModule.
    Renamings makeImportedModuleRenamings (ITree tree,
					   org.meta_environment.rascal.ast.
					   ModuleName name,
					   org.meta_environment.rascal.ast.
					   Renamings renamings)
  {
    org.meta_environment.rascal.ast.ImportedModule.Renamings x =
      new org.meta_environment.rascal.ast.ImportedModule.Renamings (tree,
								    name,
								    renamings);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.ImportedModule.Renamings) table.
      get (x);
  }
  public org.meta_environment.rascal.ast.ImportedModule.
    Actuals makeImportedModuleActuals (ITree tree,
				       org.meta_environment.rascal.ast.
				       ModuleName name,
				       org.meta_environment.rascal.ast.
				       ModuleActuals actuals)
  {
    org.meta_environment.rascal.ast.ImportedModule.Actuals x =
      new org.meta_environment.rascal.ast.ImportedModule.Actuals (tree, name,
								  actuals);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.ImportedModule.Actuals) table.
      get (x);
  }
  public org.meta_environment.rascal.ast.ImportedModule.
    Ambiguity makeImportedModuleAmbiguity (java.util.LisT <
					   org.meta_environment.rascal.ast.
					   ImportedModule > alternatives)
  {
    org.meta_environment.rascal.ast.ImportedModule.Ambiguity amb =
      new org.meta_environment.rascal.ast.ImportedModule.
      Ambiguity (alternatives);
    if (!table.containsKey (amb))
      {
	table.put (amb, amb);
      }
    return (org.meta_environment.rascal.ast.ImportedModule.Ambiguity) table.
      get (amb);
  }
  public org.meta_environment.rascal.ast.ImportedModule.
    ActualsRenaming makeImportedModuleActualsRenaming (ITree tree,
						       org.meta_environment.
						       rascal.ast.
						       ModuleName name,
						       org.meta_environment.
						       rascal.ast.
						       ModuleActuals actuals,
						       org.meta_environment.
						       rascal.ast.
						       Renamings renamings)
  {
    org.meta_environment.rascal.ast.ImportedModule.ActualsRenaming x =
      new org.meta_environment.rascal.ast.ImportedModule.
      ActualsRenaming (tree, name, actuals, renamings);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.ImportedModule.
	    ActualsRenaming) table.get (x);
  }
  public org.meta_environment.rascal.ast.Renaming.
    Ambiguity makeRenamingAmbiguity (java.util.LisT <
				     org.meta_environment.rascal.ast.
				     Renaming > alternatives)
  {
    org.meta_environment.rascal.ast.Renaming.Ambiguity amb =
      new org.meta_environment.rascal.ast.Renaming.Ambiguity (alternatives);
    if (!table.containsKey (amb))
      {
	table.put (amb, amb);
      }
    return (org.meta_environment.rascal.ast.Renaming.Ambiguity) table.
      get (amb);
  }
  public org.meta_environment.rascal.ast.Renaming.
    Default makeRenamingDefault (ITree tree,
				 org.meta_environment.rascal.ast.Name from,
				 org.meta_environment.rascal.ast.Name to)
  {
    org.meta_environment.rascal.ast.Renaming.Default x =
      new org.meta_environment.rascal.ast.Renaming.Default (tree, from, to);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Renaming.Default) table.get (x);
  }
  public org.meta_environment.rascal.ast.Renamings.
    Ambiguity makeRenamingsAmbiguity (java.util.LisT <
				      org.meta_environment.rascal.ast.
				      Renamings > alternatives)
  {
    org.meta_environment.rascal.ast.Renamings.Ambiguity amb =
      new org.meta_environment.rascal.ast.Renamings.Ambiguity (alternatives);
    if (!table.containsKey (amb))
      {
	table.put (amb, amb);
      }
    return (org.meta_environment.rascal.ast.Renamings.Ambiguity) table.
      get (amb);
  }
  public org.meta_environment.rascal.ast.Renamings.
    Default makeRenamingsDefault (ITree tree,
				  java.util.LisT <
				  org.meta_environment.rascal.ast.Renaming >
				  renamings)
  {
    org.meta_environment.rascal.ast.Renamings.Default x =
      new org.meta_environment.rascal.ast.Renamings.Default (tree, renamings);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Renamings.Default) table.get (x);
  }
  public org.meta_environment.rascal.ast.Import.
    Extend makeImportExtend (ITree tree,
			     org.meta_environment.rascal.ast.
			     ImportedModule module)
  {
    org.meta_environment.rascal.ast.Import.Extend x =
      new org.meta_environment.rascal.ast.Import.Extend (tree, module);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Import.Extend) table.get (x);
  }
  public org.meta_environment.rascal.ast.Import.
    Ambiguity makeImportAmbiguity (java.util.LisT <
				   org.meta_environment.rascal.ast.Import >
				   alternatives)
  {
    org.meta_environment.rascal.ast.Import.Ambiguity amb =
      new org.meta_environment.rascal.ast.Import.Ambiguity (alternatives);
    if (!table.containsKey (amb))
      {
	table.put (amb, amb);
      }
    return (org.meta_environment.rascal.ast.Import.Ambiguity) table.get (amb);
  }
  public org.meta_environment.rascal.ast.Import.
    Default makeImportDefault (ITree tree,
			       org.meta_environment.rascal.ast.
			       ImportedModule module)
  {
    org.meta_environment.rascal.ast.Import.Default x =
      new org.meta_environment.rascal.ast.Import.Default (tree, module);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Import.Default) table.get (x);
  }
  public org.meta_environment.rascal.ast.ModuleParameters.
    Ambiguity makeModuleParametersAmbiguity (java.util.LisT <
					     org.meta_environment.rascal.ast.
					     ModuleParameters > alternatives)
  {
    org.meta_environment.rascal.ast.ModuleParameters.Ambiguity amb =
      new org.meta_environment.rascal.ast.ModuleParameters.
      Ambiguity (alternatives);
    if (!table.containsKey (amb))
      {
	table.put (amb, amb);
      }
    return (org.meta_environment.rascal.ast.ModuleParameters.Ambiguity) table.
      get (amb);
  }
  public org.meta_environment.rascal.ast.ModuleParameters.
    Default makeModuleParametersDefault (ITree tree,
					 java.util.LisT <
					 org.meta_environment.rascal.ast.
					 TypeVar > parameters)
  {
    org.meta_environment.rascal.ast.ModuleParameters.Default x =
      new org.meta_environment.rascal.ast.ModuleParameters.Default (tree,
								    parameters);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.ModuleParameters.Default) table.
      get (x);
  }
  public org.meta_environment.rascal.ast.Header.
    Parameters makeHeaderParameters (ITree tree,
				     org.meta_environment.rascal.ast.
				     ModuleName name,
				     org.meta_environment.rascal.ast.
				     ModuleParameters params,
				     org.meta_environment.rascal.ast.
				     Tags tags,
				     java.util.LisT <
				     org.meta_environment.rascal.ast.Import >
				     imports)
  {
    org.meta_environment.rascal.ast.Header.Parameters x =
      new org.meta_environment.rascal.ast.Header.Parameters (tree, name,
							     params, tags,
							     imports);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Header.Parameters) table.get (x);
  }
  public org.meta_environment.rascal.ast.Header.
    Ambiguity makeHeaderAmbiguity (java.util.LisT <
				   org.meta_environment.rascal.ast.Header >
				   alternatives)
  {
    org.meta_environment.rascal.ast.Header.Ambiguity amb =
      new org.meta_environment.rascal.ast.Header.Ambiguity (alternatives);
    if (!table.containsKey (amb))
      {
	table.put (amb, amb);
      }
    return (org.meta_environment.rascal.ast.Header.Ambiguity) table.get (amb);
  }
  public org.meta_environment.rascal.ast.Header.
    Default makeHeaderDefault (ITree tree,
			       org.meta_environment.rascal.ast.
			       ModuleName name,
			       org.meta_environment.rascal.ast.Tags tags,
			       java.util.LisT <
			       org.meta_environment.rascal.ast.Import >
			       imports)
  {
    org.meta_environment.rascal.ast.Header.Default x =
      new org.meta_environment.rascal.ast.Header.Default (tree, name, tags,
							  imports);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Header.Default) table.get (x);
  }
  public org.meta_environment.rascal.ast.VisIbility.
    Private makeVisIbilityPrivate (ITree tree)
  {
    org.meta_environment.rascal.ast.VisIbility.Private x =
      new org.meta_environment.rascal.ast.VisIbility.Private (tree);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.VisIbility.Private) table.get (x);
  }
  public org.meta_environment.rascal.ast.VisIbility.
    Ambiguity makeVisIbilityAmbiguity (java.util.LisT <
				       org.meta_environment.rascal.ast.
				       VisIbility > alternatives)
  {
    org.meta_environment.rascal.ast.VisIbility.Ambiguity amb =
      new org.meta_environment.rascal.ast.VisIbility.Ambiguity (alternatives);
    if (!table.containsKey (amb))
      {
	table.put (amb, amb);
      }
    return (org.meta_environment.rascal.ast.VisIbility.Ambiguity) table.
      get (amb);
  }
  public org.meta_environment.rascal.ast.VisIbility.
    Public makeVisIbilityPublic (ITree tree)
  {
    org.meta_environment.rascal.ast.VisIbility.Public x =
      new org.meta_environment.rascal.ast.VisIbility.Public (tree);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.VisIbility.Public) table.get (x);
  }
  public org.meta_environment.rascal.ast.Toplevel.
    DefaultVisIbility makeToplevelDefaultVisIbility (ITree tree,
						     org.meta_environment.
						     rascal.ast.
						     Declaration declaration)
  {
    org.meta_environment.rascal.ast.Toplevel.DefaultVisIbility x =
      new org.meta_environment.rascal.ast.Toplevel.DefaultVisIbility (tree,
								      declaration);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Toplevel.DefaultVisIbility) table.
      get (x);
  }
  public org.meta_environment.rascal.ast.Toplevel.
    Ambiguity makeToplevelAmbiguity (java.util.LisT <
				     org.meta_environment.rascal.ast.
				     Toplevel > alternatives)
  {
    org.meta_environment.rascal.ast.Toplevel.Ambiguity amb =
      new org.meta_environment.rascal.ast.Toplevel.Ambiguity (alternatives);
    if (!table.containsKey (amb))
      {
	table.put (amb, amb);
      }
    return (org.meta_environment.rascal.ast.Toplevel.Ambiguity) table.
      get (amb);
  }
  public org.meta_environment.rascal.ast.Toplevel.
    GivenVisIbility makeToplevelGivenVisIbility (ITree tree,
						 org.meta_environment.rascal.
						 ast.VisIbility visIbility,
						 org.meta_environment.rascal.
						 ast.Declaration declaration)
  {
    org.meta_environment.rascal.ast.Toplevel.GivenVisIbility x =
      new org.meta_environment.rascal.ast.Toplevel.GivenVisIbility (tree,
								    visIbility,
								    declaration);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Toplevel.GivenVisIbility) table.
      get (x);
  }
  public org.meta_environment.rascal.ast.Declaration.
    Tag makeDeclarationTag (ITree tree,
			    org.meta_environment.rascal.ast.Kind kind,
			    org.meta_environment.rascal.ast.Name name,
			    org.meta_environment.rascal.ast.Tags tags,
			    java.util.LisT <
			    org.meta_environment.rascal.ast.Type > types)
  {
    org.meta_environment.rascal.ast.Declaration.Tag x =
      new org.meta_environment.rascal.ast.Declaration.Tag (tree, kind, name,
							   tags, types);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Declaration.Tag) table.get (x);
  }
  public org.meta_environment.rascal.ast.Declaration.
    Annotation makeDeclarationAnnotation (ITree tree,
					  org.meta_environment.rascal.ast.
					  Type type,
					  org.meta_environment.rascal.ast.
					  Name name,
					  org.meta_environment.rascal.ast.
					  Tags tags,
					  java.util.LisT <
					  org.meta_environment.rascal.ast.
					  Type > types)
  {
    org.meta_environment.rascal.ast.Declaration.Annotation x =
      new org.meta_environment.rascal.ast.Declaration.Annotation (tree, type,
								  name, tags,
								  types);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Declaration.Annotation) table.
      get (x);
  }
  public org.meta_environment.rascal.ast.Declaration.
    Rule makeDeclarationRule (ITree tree,
			      org.meta_environment.rascal.ast.Name name,
			      org.meta_environment.rascal.ast.Tags tags,
			      org.meta_environment.rascal.ast.Rule rule)
  {
    org.meta_environment.rascal.ast.Declaration.Rule x =
      new org.meta_environment.rascal.ast.Declaration.Rule (tree, name, tags,
							    rule);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Declaration.Rule) table.get (x);
  }
  public org.meta_environment.rascal.ast.Declaration.
    Variable makeDeclarationVariable (ITree tree,
				      org.meta_environment.rascal.ast.
				      Type type,
				      java.util.LisT <
				      org.meta_environment.rascal.ast.
				      Variable > variables)
  {
    org.meta_environment.rascal.ast.Declaration.Variable x =
      new org.meta_environment.rascal.ast.Declaration.Variable (tree, type,
								variables);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Declaration.Variable) table.
      get (x);
  }
  public org.meta_environment.rascal.ast.Declaration.
    Function makeDeclarationFunction (ITree tree,
				      org.meta_environment.rascal.ast.
				      FunctionDeclaration functionDeclaration)
  {
    org.meta_environment.rascal.ast.Declaration.Function x =
      new org.meta_environment.rascal.ast.Declaration.Function (tree,
								functionDeclaration);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Declaration.Function) table.
      get (x);
  }
  public org.meta_environment.rascal.ast.Declaration.
    Data makeDeclarationData (ITree tree,
			      org.meta_environment.rascal.ast.UserType user,
			      org.meta_environment.rascal.ast.Tags tags,
			      java.util.LisT <
			      org.meta_environment.rascal.ast.Variant >
			      variants)
  {
    org.meta_environment.rascal.ast.Declaration.Data x =
      new org.meta_environment.rascal.ast.Declaration.Data (tree, user, tags,
							    variants);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Declaration.Data) table.get (x);
  }
  public org.meta_environment.rascal.ast.Declaration.
    Type makeDeclarationType (ITree tree,
			      org.meta_environment.rascal.ast.Type base,
			      org.meta_environment.rascal.ast.UserType user,
			      org.meta_environment.rascal.ast.Tags tags)
  {
    org.meta_environment.rascal.ast.Declaration.Type x =
      new org.meta_environment.rascal.ast.Declaration.Type (tree, base, user,
							    tags);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Declaration.Type) table.get (x);
  }
  public org.meta_environment.rascal.ast.Declaration.
    Ambiguity makeDeclarationAmbiguity (java.util.LisT <
					org.meta_environment.rascal.ast.
					Declaration > alternatives)
  {
    org.meta_environment.rascal.ast.Declaration.Ambiguity amb =
      new org.meta_environment.rascal.ast.Declaration.
      Ambiguity (alternatives);
    if (!table.containsKey (amb))
      {
	table.put (amb, amb);
      }
    return (org.meta_environment.rascal.ast.Declaration.Ambiguity) table.
      get (amb);
  }
  public org.meta_environment.rascal.ast.Declaration.
    View makeDeclarationView (ITree tree,
			      org.meta_environment.rascal.ast.Name view,
			      org.meta_environment.rascal.ast.Name superType,
			      org.meta_environment.rascal.ast.Tags tags,
			      java.util.LisT <
			      org.meta_environment.rascal.ast.Alternative >
			      alts)
  {
    org.meta_environment.rascal.ast.Declaration.View x =
      new org.meta_environment.rascal.ast.Declaration.View (tree, view,
							    superType, tags,
							    alts);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Declaration.View) table.get (x);
  }
  public org.meta_environment.rascal.ast.Alternative.
    Ambiguity makeAlternativeAmbiguity (java.util.LisT <
					org.meta_environment.rascal.ast.
					Alternative > alternatives)
  {
    org.meta_environment.rascal.ast.Alternative.Ambiguity amb =
      new org.meta_environment.rascal.ast.Alternative.
      Ambiguity (alternatives);
    if (!table.containsKey (amb))
      {
	table.put (amb, amb);
      }
    return (org.meta_environment.rascal.ast.Alternative.Ambiguity) table.
      get (amb);
  }
  public org.meta_environment.rascal.ast.Alternative.
    NamedType makeAlternativeNamedType (ITree tree,
					org.meta_environment.rascal.ast.
					Name name,
					org.meta_environment.rascal.ast.
					Type type)
  {
    org.meta_environment.rascal.ast.Alternative.NamedType x =
      new org.meta_environment.rascal.ast.Alternative.NamedType (tree, name,
								 type);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Alternative.NamedType) table.
      get (x);
  }
  public org.meta_environment.rascal.ast.Variant.
    NillaryConstructor makeVariantNillaryConstructor (ITree tree,
						      org.meta_environment.
						      rascal.ast.Name name)
  {
    org.meta_environment.rascal.ast.Variant.NillaryConstructor x =
      new org.meta_environment.rascal.ast.Variant.NillaryConstructor (tree,
								      name);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Variant.NillaryConstructor) table.
      get (x);
  }
  public org.meta_environment.rascal.ast.Variant.
    NAryConstructor makeVariantNAryConstructor (ITree tree,
						org.meta_environment.rascal.
						ast.Name name,
						java.util.LisT <
						org.meta_environment.rascal.
						ast.TypeArg > arguments)
  {
    org.meta_environment.rascal.ast.Variant.NAryConstructor x =
      new org.meta_environment.rascal.ast.Variant.NAryConstructor (tree, name,
								   arguments);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Variant.NAryConstructor) table.
      get (x);
  }
  public org.meta_environment.rascal.ast.Variant.
    Ambiguity makeVariantAmbiguity (java.util.LisT <
				    org.meta_environment.rascal.ast.Variant >
				    alternatives)
  {
    org.meta_environment.rascal.ast.Variant.Ambiguity amb =
      new org.meta_environment.rascal.ast.Variant.Ambiguity (alternatives);
    if (!table.containsKey (amb))
      {
	table.put (amb, amb);
      }
    return (org.meta_environment.rascal.ast.Variant.Ambiguity) table.
      get (amb);
  }
  public org.meta_environment.rascal.ast.Variant.
    AnonymousConstructor makeVariantAnonymousConstructor (ITree tree,
							  org.
							  meta_environment.
							  rascal.ast.
							  Type type,
							  org.
							  meta_environment.
							  rascal.ast.
							  Name name)
  {
    org.meta_environment.rascal.ast.Variant.AnonymousConstructor x =
      new org.meta_environment.rascal.ast.Variant.AnonymousConstructor (tree,
									type,
									name);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Variant.
	    AnonymousConstructor) table.get (x);
  }
  public org.meta_environment.rascal.ast.StandardOperator.
    NotIn makeStandardOperatorNotIn (ITree tree)
  {
    org.meta_environment.rascal.ast.StandardOperator.NotIn x =
      new org.meta_environment.rascal.ast.StandardOperator.NotIn (tree);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.StandardOperator.NotIn) table.
      get (x);
  }
  public org.meta_environment.rascal.ast.StandardOperator.
    In makeStandardOperatorIn (ITree tree)
  {
    org.meta_environment.rascal.ast.StandardOperator.In x =
      new org.meta_environment.rascal.ast.StandardOperator.In (tree);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.StandardOperator.In) table.
      get (x);
  }
  public org.meta_environment.rascal.ast.StandardOperator.
    Not makeStandardOperatorNot (ITree tree)
  {
    org.meta_environment.rascal.ast.StandardOperator.Not x =
      new org.meta_environment.rascal.ast.StandardOperator.Not (tree);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.StandardOperator.Not) table.
      get (x);
  }
  public org.meta_environment.rascal.ast.StandardOperator.
    Or makeStandardOperatorOr (ITree tree)
  {
    org.meta_environment.rascal.ast.StandardOperator.Or x =
      new org.meta_environment.rascal.ast.StandardOperator.Or (tree);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.StandardOperator.Or) table.
      get (x);
  }
  public org.meta_environment.rascal.ast.StandardOperator.
    And makeStandardOperatorAnd (ITree tree)
  {
    org.meta_environment.rascal.ast.StandardOperator.And x =
      new org.meta_environment.rascal.ast.StandardOperator.And (tree);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.StandardOperator.And) table.
      get (x);
  }
  public org.meta_environment.rascal.ast.StandardOperator.
    GreaterThanOrEq makeStandardOperatorGreaterThanOrEq (ITree tree)
  {
    org.meta_environment.rascal.ast.StandardOperator.GreaterThanOrEq x =
      new org.meta_environment.rascal.ast.StandardOperator.
      GreaterThanOrEq (tree);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.StandardOperator.
	    GreaterThanOrEq) table.get (x);
  }
  public org.meta_environment.rascal.ast.StandardOperator.
    GreaterThan makeStandardOperatorGreaterThan (ITree tree)
  {
    org.meta_environment.rascal.ast.StandardOperator.GreaterThan x =
      new org.meta_environment.rascal.ast.StandardOperator.GreaterThan (tree);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.StandardOperator.
	    GreaterThan) table.get (x);
  }
  public org.meta_environment.rascal.ast.StandardOperator.
    LessThanOrEq makeStandardOperatorLessThanOrEq (ITree tree)
  {
    org.meta_environment.rascal.ast.StandardOperator.LessThanOrEq x =
      new org.meta_environment.rascal.ast.StandardOperator.
      LessThanOrEq (tree);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.StandardOperator.
	    LessThanOrEq) table.get (x);
  }
  public org.meta_environment.rascal.ast.StandardOperator.
    LessThan makeStandardOperatorLessThan (ITree tree)
  {
    org.meta_environment.rascal.ast.StandardOperator.LessThan x =
      new org.meta_environment.rascal.ast.StandardOperator.LessThan (tree);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.StandardOperator.LessThan) table.
      get (x);
  }
  public org.meta_environment.rascal.ast.StandardOperator.
    NotEquals makeStandardOperatorNotEquals (ITree tree)
  {
    org.meta_environment.rascal.ast.StandardOperator.NotEquals x =
      new org.meta_environment.rascal.ast.StandardOperator.NotEquals (tree);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.StandardOperator.NotEquals) table.
      get (x);
  }
  public org.meta_environment.rascal.ast.StandardOperator.
    Equals makeStandardOperatorEquals (ITree tree)
  {
    org.meta_environment.rascal.ast.StandardOperator.Equals x =
      new org.meta_environment.rascal.ast.StandardOperator.Equals (tree);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.StandardOperator.Equals) table.
      get (x);
  }
  public org.meta_environment.rascal.ast.StandardOperator.
    Intersection makeStandardOperatorIntersection (ITree tree)
  {
    org.meta_environment.rascal.ast.StandardOperator.Intersection x =
      new org.meta_environment.rascal.ast.StandardOperator.
      Intersection (tree);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.StandardOperator.
	    Intersection) table.get (x);
  }
  public org.meta_environment.rascal.ast.StandardOperator.
    DivisIon makeStandardOperatorDivisIon (ITree tree)
  {
    org.meta_environment.rascal.ast.StandardOperator.DivisIon x =
      new org.meta_environment.rascal.ast.StandardOperator.DivisIon (tree);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.StandardOperator.DivisIon) table.
      get (x);
  }
  public org.meta_environment.rascal.ast.StandardOperator.
    Product makeStandardOperatorProduct (ITree tree)
  {
    org.meta_environment.rascal.ast.StandardOperator.Product x =
      new org.meta_environment.rascal.ast.StandardOperator.Product (tree);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.StandardOperator.Product) table.
      get (x);
  }
  public org.meta_environment.rascal.ast.StandardOperator.
    Substraction makeStandardOperatorSubstraction (ITree tree)
  {
    org.meta_environment.rascal.ast.StandardOperator.Substraction x =
      new org.meta_environment.rascal.ast.StandardOperator.
      Substraction (tree);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.StandardOperator.
	    Substraction) table.get (x);
  }
  public org.meta_environment.rascal.ast.StandardOperator.
    Ambiguity makeStandardOperatorAmbiguity (java.util.LisT <
					     org.meta_environment.rascal.ast.
					     StandardOperator > alternatives)
  {
    org.meta_environment.rascal.ast.StandardOperator.Ambiguity amb =
      new org.meta_environment.rascal.ast.StandardOperator.
      Ambiguity (alternatives);
    if (!table.containsKey (amb))
      {
	table.put (amb, amb);
      }
    return (org.meta_environment.rascal.ast.StandardOperator.Ambiguity) table.
      get (amb);
  }
  public org.meta_environment.rascal.ast.StandardOperator.
    Addition makeStandardOperatorAddition (ITree tree)
  {
    org.meta_environment.rascal.ast.StandardOperator.Addition x =
      new org.meta_environment.rascal.ast.StandardOperator.Addition (tree);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.StandardOperator.Addition) table.
      get (x);
  }
  public org.meta_environment.rascal.ast.FunctionName.
    Operator makeFunctionNameOperator (ITree tree,
				       org.meta_environment.rascal.ast.
				       StandardOperator operator)
  {
    org.meta_environment.rascal.ast.FunctionName.Operator x =
      new org.meta_environment.rascal.ast.FunctionName.Operator (tree,
								 operator);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.FunctionName.Operator) table.
      get (x);
  }
  public org.meta_environment.rascal.ast.FunctionName.
    Ambiguity makeFunctionNameAmbiguity (java.util.LisT <
					 org.meta_environment.rascal.ast.
					 FunctionName > alternatives)
  {
    org.meta_environment.rascal.ast.FunctionName.Ambiguity amb =
      new org.meta_environment.rascal.ast.FunctionName.
      Ambiguity (alternatives);
    if (!table.containsKey (amb))
      {
	table.put (amb, amb);
      }
    return (org.meta_environment.rascal.ast.FunctionName.Ambiguity) table.
      get (amb);
  }
  public org.meta_environment.rascal.ast.FunctionName.
    Name makeFunctionNameName (ITree tree,
			       org.meta_environment.rascal.ast.Name name)
  {
    org.meta_environment.rascal.ast.FunctionName.Name x =
      new org.meta_environment.rascal.ast.FunctionName.Name (tree, name);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.FunctionName.Name) table.get (x);
  }
  public org.meta_environment.rascal.ast.FunctionModifier.
    Ambiguity makeFunctionModifierAmbiguity (java.util.LisT <
					     org.meta_environment.rascal.ast.
					     FunctionModifier > alternatives)
  {
    org.meta_environment.rascal.ast.FunctionModifier.Ambiguity amb =
      new org.meta_environment.rascal.ast.FunctionModifier.
      Ambiguity (alternatives);
    if (!table.containsKey (amb))
      {
	table.put (amb, amb);
      }
    return (org.meta_environment.rascal.ast.FunctionModifier.Ambiguity) table.
      get (amb);
  }
  public org.meta_environment.rascal.ast.FunctionModifier.
    Java makeFunctionModifierJava (ITree tree)
  {
    org.meta_environment.rascal.ast.FunctionModifier.Java x =
      new org.meta_environment.rascal.ast.FunctionModifier.Java (tree);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.FunctionModifier.Java) table.
      get (x);
  }
  public org.meta_environment.rascal.ast.FunctionModifiers.
    Ambiguity makeFunctionModifiersAmbiguity (java.util.LisT <
					      org.meta_environment.rascal.ast.
					      FunctionModifiers >
					      alternatives)
  {
    org.meta_environment.rascal.ast.FunctionModifiers.Ambiguity amb =
      new org.meta_environment.rascal.ast.FunctionModifiers.
      Ambiguity (alternatives);
    if (!table.containsKey (amb))
      {
	table.put (amb, amb);
      }
    return (org.meta_environment.rascal.ast.FunctionModifiers.
	    Ambiguity) table.get (amb);
  }
  public org.meta_environment.rascal.ast.FunctionModifiers.
    LisT makeFunctionModifiersLisT (ITree tree,
				    java.util.LisT <
				    org.meta_environment.rascal.ast.
				    FunctionModifier > modifiers)
  {
    org.meta_environment.rascal.ast.FunctionModifiers.LisT x =
      new org.meta_environment.rascal.ast.FunctionModifiers.LisT (tree,
								  modifiers);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.FunctionModifiers.LisT) table.
      get (x);
  }
  public org.meta_environment.rascal.ast.Signature.
    WithThrows makeSignatureWithThrows (ITree tree,
					org.meta_environment.rascal.ast.
					Type type,
					org.meta_environment.rascal.ast.
					FunctionModifiers modifiers,
					org.meta_environment.rascal.ast.
					FunctionName name,
					org.meta_environment.rascal.ast.
					Parameters parameters,
					java.util.LisT <
					org.meta_environment.rascal.ast.Type >
					exceptions)
  {
    org.meta_environment.rascal.ast.Signature.WithThrows x =
      new org.meta_environment.rascal.ast.Signature.WithThrows (tree, type,
								modifiers,
								name,
								parameters,
								exceptions);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Signature.WithThrows) table.
      get (x);
  }
  public org.meta_environment.rascal.ast.Signature.
    Ambiguity makeSignatureAmbiguity (java.util.LisT <
				      org.meta_environment.rascal.ast.
				      Signature > alternatives)
  {
    org.meta_environment.rascal.ast.Signature.Ambiguity amb =
      new org.meta_environment.rascal.ast.Signature.Ambiguity (alternatives);
    if (!table.containsKey (amb))
      {
	table.put (amb, amb);
      }
    return (org.meta_environment.rascal.ast.Signature.Ambiguity) table.
      get (amb);
  }
  public org.meta_environment.rascal.ast.Signature.
    NoThrows makeSignatureNoThrows (ITree tree,
				    org.meta_environment.rascal.ast.Type type,
				    org.meta_environment.rascal.ast.
				    FunctionModifiers modifiers,
				    org.meta_environment.rascal.ast.
				    FunctionName name,
				    org.meta_environment.rascal.ast.
				    Parameters parameters)
  {
    org.meta_environment.rascal.ast.Signature.NoThrows x =
      new org.meta_environment.rascal.ast.Signature.NoThrows (tree, type,
							      modifiers, name,
							      parameters);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Signature.NoThrows) table.get (x);
  }
  public org.meta_environment.rascal.ast.FunctionDeclaration.
    Abstract makeFunctionDeclarationAbstract (ITree tree,
					      org.meta_environment.rascal.ast.
					      Signature signature,
					      org.meta_environment.rascal.ast.
					      Tags tags)
  {
    org.meta_environment.rascal.ast.FunctionDeclaration.Abstract x =
      new org.meta_environment.rascal.ast.FunctionDeclaration.Abstract (tree,
									signature,
									tags);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.FunctionDeclaration.
	    Abstract) table.get (x);
  }
  public org.meta_environment.rascal.ast.FunctionDeclaration.
    Ambiguity makeFunctionDeclarationAmbiguity (java.util.LisT <
						org.meta_environment.rascal.
						ast.FunctionDeclaration >
						alternatives)
  {
    org.meta_environment.rascal.ast.FunctionDeclaration.Ambiguity amb =
      new org.meta_environment.rascal.ast.FunctionDeclaration.
      Ambiguity (alternatives);
    if (!table.containsKey (amb))
      {
	table.put (amb, amb);
      }
    return (org.meta_environment.rascal.ast.FunctionDeclaration.
	    Ambiguity) table.get (amb);
  }
  public org.meta_environment.rascal.ast.FunctionDeclaration.
    Default makeFunctionDeclarationDefault (ITree tree,
					    org.meta_environment.rascal.ast.
					    Signature signature,
					    org.meta_environment.rascal.ast.
					    Tags tags,
					    org.meta_environment.rascal.ast.
					    FunctionBody body)
  {
    org.meta_environment.rascal.ast.FunctionDeclaration.Default x =
      new org.meta_environment.rascal.ast.FunctionDeclaration.Default (tree,
								       signature,
								       tags,
								       body);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.FunctionDeclaration.
	    Default) table.get (x);
  }
  public org.meta_environment.rascal.ast.FunctionBody.
    Ambiguity makeFunctionBodyAmbiguity (java.util.LisT <
					 org.meta_environment.rascal.ast.
					 FunctionBody > alternatives)
  {
    org.meta_environment.rascal.ast.FunctionBody.Ambiguity amb =
      new org.meta_environment.rascal.ast.FunctionBody.
      Ambiguity (alternatives);
    if (!table.containsKey (amb))
      {
	table.put (amb, amb);
      }
    return (org.meta_environment.rascal.ast.FunctionBody.Ambiguity) table.
      get (amb);
  }
  public org.meta_environment.rascal.ast.FunctionBody.
    Default makeFunctionBodyDefault (ITree tree,
				     java.util.LisT <
				     org.meta_environment.rascal.ast.
				     Statement > statements)
  {
    org.meta_environment.rascal.ast.FunctionBody.Default x =
      new org.meta_environment.rascal.ast.FunctionBody.Default (tree,
								statements);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.FunctionBody.Default) table.
      get (x);
  }
  public org.meta_environment.rascal.ast.Variable.
    Ambiguity makeVariableAmbiguity (java.util.LisT <
				     org.meta_environment.rascal.ast.
				     Variable > alternatives)
  {
    org.meta_environment.rascal.ast.Variable.Ambiguity amb =
      new org.meta_environment.rascal.ast.Variable.Ambiguity (alternatives);
    if (!table.containsKey (amb))
      {
	table.put (amb, amb);
      }
    return (org.meta_environment.rascal.ast.Variable.Ambiguity) table.
      get (amb);
  }
  public org.meta_environment.rascal.ast.Variable.
    GivenInitialization makeVariableGivenInitialization (ITree tree,
							 org.meta_environment.
							 rascal.ast.Name name,
							 org.meta_environment.
							 rascal.ast.Tags tags,
							 org.meta_environment.
							 rascal.ast.
							 Expression initial)
  {
    org.meta_environment.rascal.ast.Variable.GivenInitialization x =
      new org.meta_environment.rascal.ast.Variable.GivenInitialization (tree,
									name,
									tags,
									initial);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Variable.
	    GivenInitialization) table.get (x);
  }
  public org.meta_environment.rascal.ast.Kind.All makeKindAll (ITree tree)
  {
    org.meta_environment.rascal.ast.Kind.All x =
      new org.meta_environment.rascal.ast.Kind.All (tree);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Kind.All) table.get (x);
  }
  public org.meta_environment.rascal.ast.Kind.Tag makeKindTag (ITree tree)
  {
    org.meta_environment.rascal.ast.Kind.Tag x =
      new org.meta_environment.rascal.ast.Kind.Tag (tree);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Kind.Tag) table.get (x);
  }
  public org.meta_environment.rascal.ast.Kind.Anno makeKindAnno (ITree tree)
  {
    org.meta_environment.rascal.ast.Kind.Anno x =
      new org.meta_environment.rascal.ast.Kind.Anno (tree);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Kind.Anno) table.get (x);
  }
  public org.meta_environment.rascal.ast.Kind.Type makeKindType (ITree tree)
  {
    org.meta_environment.rascal.ast.Kind.Type x =
      new org.meta_environment.rascal.ast.Kind.Type (tree);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Kind.Type) table.get (x);
  }
  public org.meta_environment.rascal.ast.Kind.View makeKindView (ITree tree)
  {
    org.meta_environment.rascal.ast.Kind.View x =
      new org.meta_environment.rascal.ast.Kind.View (tree);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Kind.View) table.get (x);
  }
  public org.meta_environment.rascal.ast.Kind.Data makeKindData (ITree tree)
  {
    org.meta_environment.rascal.ast.Kind.Data x =
      new org.meta_environment.rascal.ast.Kind.Data (tree);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Kind.Data) table.get (x);
  }
  public org.meta_environment.rascal.ast.Kind.
    Variable makeKindVariable (ITree tree)
  {
    org.meta_environment.rascal.ast.Kind.Variable x =
      new org.meta_environment.rascal.ast.Kind.Variable (tree);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Kind.Variable) table.get (x);
  }
  public org.meta_environment.rascal.ast.Kind.
    Function makeKindFunction (ITree tree)
  {
    org.meta_environment.rascal.ast.Kind.Function x =
      new org.meta_environment.rascal.ast.Kind.Function (tree);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Kind.Function) table.get (x);
  }
  public org.meta_environment.rascal.ast.Kind.
    Ambiguity makeKindAmbiguity (java.util.LisT <
				 org.meta_environment.rascal.ast.Kind >
				 alternatives)
  {
    org.meta_environment.rascal.ast.Kind.Ambiguity amb =
      new org.meta_environment.rascal.ast.Kind.Ambiguity (alternatives);
    if (!table.containsKey (amb))
      {
	table.put (amb, amb);
      }
    return (org.meta_environment.rascal.ast.Kind.Ambiguity) table.get (amb);
  }
  public org.meta_environment.rascal.ast.Kind.
    Module makeKindModule (ITree tree)
  {
    org.meta_environment.rascal.ast.Kind.Module x =
      new org.meta_environment.rascal.ast.Kind.Module (tree);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Kind.Module) table.get (x);
  }
  public org.meta_environment.rascal.ast.ValueProducer.
    GivenStrategy makeValueProducerGivenStrategy (ITree tree,
						  org.meta_environment.rascal.
						  ast.Strategy strategy,
						  org.meta_environment.rascal.
						  ast.Expression pattern,
						  org.meta_environment.rascal.
						  ast.Expression expression)
  {
    org.meta_environment.rascal.ast.ValueProducer.GivenStrategy x =
      new org.meta_environment.rascal.ast.ValueProducer.GivenStrategy (tree,
								       strategy,
								       pattern,
								       expression);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.ValueProducer.
	    GivenStrategy) table.get (x);
  }
  public org.meta_environment.rascal.ast.ValueProducer.
    Ambiguity makeValueProducerAmbiguity (java.util.LisT <
					  org.meta_environment.rascal.ast.
					  ValueProducer > alternatives)
  {
    org.meta_environment.rascal.ast.ValueProducer.Ambiguity amb =
      new org.meta_environment.rascal.ast.ValueProducer.
      Ambiguity (alternatives);
    if (!table.containsKey (amb))
      {
	table.put (amb, amb);
      }
    return (org.meta_environment.rascal.ast.ValueProducer.Ambiguity) table.
      get (amb);
  }
  public org.meta_environment.rascal.ast.ValueProducer.
    DefaultStrategy makeValueProducerDefaultStrategy (ITree tree,
						      org.meta_environment.
						      rascal.ast.
						      Expression pattern,
						      org.meta_environment.
						      rascal.ast.
						      Expression expression)
  {
    org.meta_environment.rascal.ast.ValueProducer.DefaultStrategy x =
      new org.meta_environment.rascal.ast.ValueProducer.DefaultStrategy (tree,
									 pattern,
									 expression);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.ValueProducer.
	    DefaultStrategy) table.get (x);
  }
  public org.meta_environment.rascal.ast.Generator.
    Producer makeGeneratorProducer (ITree tree,
				    org.meta_environment.rascal.ast.
				    ValueProducer producer)
  {
    org.meta_environment.rascal.ast.Generator.Producer x =
      new org.meta_environment.rascal.ast.Generator.Producer (tree, producer);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Generator.Producer) table.get (x);
  }
  public org.meta_environment.rascal.ast.Generator.
    Ambiguity makeGeneratorAmbiguity (java.util.LisT <
				      org.meta_environment.rascal.ast.
				      Generator > alternatives)
  {
    org.meta_environment.rascal.ast.Generator.Ambiguity amb =
      new org.meta_environment.rascal.ast.Generator.Ambiguity (alternatives);
    if (!table.containsKey (amb))
      {
	table.put (amb, amb);
      }
    return (org.meta_environment.rascal.ast.Generator.Ambiguity) table.
      get (amb);
  }
  public org.meta_environment.rascal.ast.Generator.
    Expression makeGeneratorExpression (ITree tree,
					org.meta_environment.rascal.ast.
					Expression expression)
  {
    org.meta_environment.rascal.ast.Generator.Expression x =
      new org.meta_environment.rascal.ast.Generator.Expression (tree,
								expression);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Generator.Expression) table.
      get (x);
  }
  public org.meta_environment.rascal.ast.Strategy.
    Innermost makeStrategyInnermost (ITree tree)
  {
    org.meta_environment.rascal.ast.Strategy.Innermost x =
      new org.meta_environment.rascal.ast.Strategy.Innermost (tree);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Strategy.Innermost) table.get (x);
  }
  public org.meta_environment.rascal.ast.Strategy.
    Outermost makeStrategyOutermost (ITree tree)
  {
    org.meta_environment.rascal.ast.Strategy.Outermost x =
      new org.meta_environment.rascal.ast.Strategy.Outermost (tree);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Strategy.Outermost) table.get (x);
  }
  public org.meta_environment.rascal.ast.Strategy.
    BottomUpBreak makeStrategyBottomUpBreak (ITree tree)
  {
    org.meta_environment.rascal.ast.Strategy.BottomUpBreak x =
      new org.meta_environment.rascal.ast.Strategy.BottomUpBreak (tree);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Strategy.BottomUpBreak) table.
      get (x);
  }
  public org.meta_environment.rascal.ast.Strategy.
    BottomUp makeStrategyBottomUp (ITree tree)
  {
    org.meta_environment.rascal.ast.Strategy.BottomUp x =
      new org.meta_environment.rascal.ast.Strategy.BottomUp (tree);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Strategy.BottomUp) table.get (x);
  }
  public org.meta_environment.rascal.ast.Strategy.
    TopDownBreak makeStrategyTopDownBreak (ITree tree)
  {
    org.meta_environment.rascal.ast.Strategy.TopDownBreak x =
      new org.meta_environment.rascal.ast.Strategy.TopDownBreak (tree);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Strategy.TopDownBreak) table.
      get (x);
  }
  public org.meta_environment.rascal.ast.Strategy.
    Ambiguity makeStrategyAmbiguity (java.util.LisT <
				     org.meta_environment.rascal.ast.
				     Strategy > alternatives)
  {
    org.meta_environment.rascal.ast.Strategy.Ambiguity amb =
      new org.meta_environment.rascal.ast.Strategy.Ambiguity (alternatives);
    if (!table.containsKey (amb))
      {
	table.put (amb, amb);
      }
    return (org.meta_environment.rascal.ast.Strategy.Ambiguity) table.
      get (amb);
  }
  public org.meta_environment.rascal.ast.Strategy.
    TopDown makeStrategyTopDown (ITree tree)
  {
    org.meta_environment.rascal.ast.Strategy.TopDown x =
      new org.meta_environment.rascal.ast.Strategy.TopDown (tree);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Strategy.TopDown) table.get (x);
  }
  public org.meta_environment.rascal.ast.Comprehension.
    LisT makeComprehensionLisT (ITree tree,
				org.meta_environment.rascal.ast.
				Expression result,
				java.util.LisT <
				org.meta_environment.rascal.ast.Generator >
				generators)
  {
    org.meta_environment.rascal.ast.Comprehension.LisT x =
      new org.meta_environment.rascal.ast.Comprehension.LisT (tree, result,
							      generators);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Comprehension.LisT) table.get (x);
  }
  public org.meta_environment.rascal.ast.Comprehension.
    Ambiguity makeComprehensionAmbiguity (java.util.LisT <
					  org.meta_environment.rascal.ast.
					  Comprehension > alternatives)
  {
    org.meta_environment.rascal.ast.Comprehension.Ambiguity amb =
      new org.meta_environment.rascal.ast.Comprehension.
      Ambiguity (alternatives);
    if (!table.containsKey (amb))
      {
	table.put (amb, amb);
      }
    return (org.meta_environment.rascal.ast.Comprehension.Ambiguity) table.
      get (amb);
  }
  public org.meta_environment.rascal.ast.Comprehension.
    Set makeComprehensionSet (ITree tree,
			      org.meta_environment.rascal.ast.
			      Expression result,
			      java.util.LisT <
			      org.meta_environment.rascal.ast.Generator >
			      generators)
  {
    org.meta_environment.rascal.ast.Comprehension.Set x =
      new org.meta_environment.rascal.ast.Comprehension.Set (tree, result,
							     generators);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Comprehension.Set) table.get (x);
  }
  public org.meta_environment.rascal.ast.Match.
    Arbitrary makeMatchArbitrary (ITree tree,
				  org.meta_environment.rascal.ast.
				  Expression match,
				  org.meta_environment.rascal.ast.
				  Statement statement)
  {
    org.meta_environment.rascal.ast.Match.Arbitrary x =
      new org.meta_environment.rascal.ast.Match.Arbitrary (tree, match,
							   statement);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Match.Arbitrary) table.get (x);
  }
  public org.meta_environment.rascal.ast.Match.
    Ambiguity makeMatchAmbiguity (java.util.LisT <
				  org.meta_environment.rascal.ast.Match >
				  alternatives)
  {
    org.meta_environment.rascal.ast.Match.Ambiguity amb =
      new org.meta_environment.rascal.ast.Match.Ambiguity (alternatives);
    if (!table.containsKey (amb))
      {
	table.put (amb, amb);
      }
    return (org.meta_environment.rascal.ast.Match.Ambiguity) table.get (amb);
  }
  public org.meta_environment.rascal.ast.Match.
    Replacing makeMatchReplacing (ITree tree,
				  org.meta_environment.rascal.ast.
				  Expression match,
				  org.meta_environment.rascal.ast.
				  Expression replacement)
  {
    org.meta_environment.rascal.ast.Match.Replacing x =
      new org.meta_environment.rascal.ast.Match.Replacing (tree, match,
							   replacement);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Match.Replacing) table.get (x);
  }
  public org.meta_environment.rascal.ast.Rule.
    NoGuard makeRuleNoGuard (ITree tree,
			     org.meta_environment.rascal.ast.Match match)
  {
    org.meta_environment.rascal.ast.Rule.NoGuard x =
      new org.meta_environment.rascal.ast.Rule.NoGuard (tree, match);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Rule.NoGuard) table.get (x);
  }
  public org.meta_environment.rascal.ast.Rule.
    Ambiguity makeRuleAmbiguity (java.util.LisT <
				 org.meta_environment.rascal.ast.Rule >
				 alternatives)
  {
    org.meta_environment.rascal.ast.Rule.Ambiguity amb =
      new org.meta_environment.rascal.ast.Rule.Ambiguity (alternatives);
    if (!table.containsKey (amb))
      {
	table.put (amb, amb);
      }
    return (org.meta_environment.rascal.ast.Rule.Ambiguity) table.get (amb);
  }
  public org.meta_environment.rascal.ast.Rule.
    WithGuard makeRuleWithGuard (ITree tree,
				 org.meta_environment.rascal.ast.Type type,
				 org.meta_environment.rascal.ast.Match match)
  {
    org.meta_environment.rascal.ast.Rule.WithGuard x =
      new org.meta_environment.rascal.ast.Rule.WithGuard (tree, type, match);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Rule.WithGuard) table.get (x);
  }
  public org.meta_environment.rascal.ast.Case.
    Default makeCaseDefault (ITree tree,
			     org.meta_environment.rascal.ast.
			     Statement statement)
  {
    org.meta_environment.rascal.ast.Case.Default x =
      new org.meta_environment.rascal.ast.Case.Default (tree, statement);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Case.Default) table.get (x);
  }
  public org.meta_environment.rascal.ast.Case.
    Ambiguity makeCaseAmbiguity (java.util.LisT <
				 org.meta_environment.rascal.ast.Case >
				 alternatives)
  {
    org.meta_environment.rascal.ast.Case.Ambiguity amb =
      new org.meta_environment.rascal.ast.Case.Ambiguity (alternatives);
    if (!table.containsKey (amb))
      {
	table.put (amb, amb);
      }
    return (org.meta_environment.rascal.ast.Case.Ambiguity) table.get (amb);
  }
  public org.meta_environment.rascal.ast.Case.Rule makeCaseRule (ITree tree,
								 org.
								 meta_environment.
								 rascal.ast.
								 Rule rule)
  {
    org.meta_environment.rascal.ast.Case.Rule x =
      new org.meta_environment.rascal.ast.Case.Rule (tree, rule);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.Case.Rule) table.get (x);
  }
  public org.meta_environment.rascal.ast.VisIt.
    GivenStrategy makeVisItGivenStrategy (ITree tree,
					  org.meta_environment.rascal.ast.
					  Strategy strategy,
					  org.meta_environment.rascal.ast.
					  Expression subject,
					  java.util.LisT <
					  org.meta_environment.rascal.ast.
					  Case > cases)
  {
    org.meta_environment.rascal.ast.VisIt.GivenStrategy x =
      new org.meta_environment.rascal.ast.VisIt.GivenStrategy (tree, strategy,
							       subject,
							       cases);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.VisIt.GivenStrategy) table.
      get (x);
  }
  public org.meta_environment.rascal.ast.VisIt.
    Ambiguity makeVisItAmbiguity (java.util.LisT <
				  org.meta_environment.rascal.ast.VisIt >
				  alternatives)
  {
    org.meta_environment.rascal.ast.VisIt.Ambiguity amb =
      new org.meta_environment.rascal.ast.VisIt.Ambiguity (alternatives);
    if (!table.containsKey (amb))
      {
	table.put (amb, amb);
      }
    return (org.meta_environment.rascal.ast.VisIt.Ambiguity) table.get (amb);
  }
  public org.meta_environment.rascal.ast.VisIt.
    DefaultStrategy makeVisItDefaultStrategy (ITree tree,
					      org.meta_environment.rascal.ast.
					      Expression subject,
					      java.util.LisT <
					      org.meta_environment.rascal.ast.
					      Case > cases)
  {
    org.meta_environment.rascal.ast.VisIt.DefaultStrategy x =
      new org.meta_environment.rascal.ast.VisIt.DefaultStrategy (tree,
								 subject,
								 cases);
    if (!table.containsKey (x))
      {
	table.put (x, x);
      }
    return (org.meta_environment.rascal.ast.VisIt.DefaultStrategy) table.
      get (x);
  }
}
