package org.rascalmpl.library.lang.rascal.newsyntax;

import java.io.IOException;
import java.io.StringReader;

import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.exceptions.FactTypeUseException;
import org.eclipse.imp.pdb.facts.io.StandardTextReader;
import org.rascalmpl.parser.gtd.stack.*;
import org.rascalmpl.parser.gtd.stack.filter.*;
import org.rascalmpl.parser.gtd.stack.filter.follow.*;
import org.rascalmpl.parser.gtd.stack.filter.match.*;
import org.rascalmpl.parser.gtd.stack.filter.precede.*;
import org.rascalmpl.parser.gtd.preprocessing.ExpectBuilder;
import org.rascalmpl.parser.gtd.util.IntegerKeyedHashMap;
import org.rascalmpl.parser.gtd.util.IntegerList;
import org.rascalmpl.parser.gtd.util.IntegerMap;
import org.rascalmpl.values.ValueFactoryFactory;
import org.rascalmpl.values.uptr.Factory;

@SuppressWarnings("all")
public class RascalParser extends org.rascalmpl.parser.gtd.SGTDBF<IConstructor, IConstructor, ISourceLocation> {
  protected final static IValueFactory VF = ValueFactoryFactory.getValueFactory();
  
  protected static IValue _read(java.lang.String s, org.eclipse.imp.pdb.facts.type.Type type) {
    try {
      return new StandardTextReader().read(VF, org.rascalmpl.values.uptr.Factory.uptr, type, new StringReader(s));
    }
    catch (FactTypeUseException e) {
      throw new RuntimeException("unexpected exception in generated parser", e);  
    } catch (IOException e) {
      throw new RuntimeException("unexpected exception in generated parser", e);  
    }
  }
	
  protected static java.lang.String _concat(String ...args) {
    int length = 0;
    for (java.lang.String s :args) {
      length += s.length();
    }
    java.lang.StringBuilder b = new java.lang.StringBuilder(length);
    for (java.lang.String s : args) {
      b.append(s);
    }
    return b.toString();
  }
  protected static final TypeFactory _tf = TypeFactory.getInstance();
  
  private static final IntegerMap _resultStoreIdMappings;
  private static final IntegerKeyedHashMap<IntegerList> _dontNest;
	
  protected static void _putDontNest(IntegerKeyedHashMap<IntegerList> result, int parentId, int childId) {
    IntegerList donts = result.get(childId);
    if (donts == null) {
      donts = new IntegerList();
      result.put(childId, donts);
    }
    donts.add(parentId);
  }
    
  protected int getResultStoreId(int parentId) {
    return _resultStoreIdMappings.get(parentId);
  }
    
  protected static IntegerKeyedHashMap<IntegerList> _initDontNest() {
    IntegerKeyedHashMap<IntegerList> result = new IntegerKeyedHashMap<IntegerList>(); 
    
    
    
    
    _putDontNest(result, -7833, -7855);
    
    _putDontNest(result, -7608, -7897);
    
    _putDontNest(result, -7246, -7318);
    
    _putDontNest(result, -7164, -7178);
    
    _putDontNest(result, -7109, -7318);
    
    _putDontNest(result, -7095, -7237);
    
    _putDontNest(result, -7064, -7254);
    
    _putDontNest(result, -7033, -7178);
    
    _putDontNest(result, -7017, -7178);
    
    _putDontNest(result, -7002, -7147);
    
    _putDontNest(result, -6928, -7133);
    
    _putDontNest(result, -6928, -7086);
    
    _putDontNest(result, -6889, -7055);
    
    _putDontNest(result, -6889, -6928);
    
    _putDontNest(result, -6856, -7027);
    
    _putDontNest(result, -6856, -7011);
    
    _putDontNest(result, -6706, -6338);
    
    _putDontNest(result, -6642, -6883);
    
    _putDontNest(result, -6584, -7282);
    
    _putDontNest(result, -6527, -7192);
    
    _putDontNest(result, -6367, -6962);
    
    _putDontNest(result, -6345, -7147);
    
    _putDontNest(result, -7237, -7223);
    
    _putDontNest(result, -7223, -7209);
    
    _putDontNest(result, -7156, -7282);
    
    _putDontNest(result, -7086, -7103);
    
    _putDontNest(result, -7047, -7237);
    
    _putDontNest(result, -7027, -7296);
    
    _putDontNest(result, -7011, -7296);
    
    _putDontNest(result, -6996, -7041);
    
    _putDontNest(result, -6934, -7237);
    
    _putDontNest(result, -6934, -6996);
    
    _putDontNest(result, -6928, -7117);
    
    _putDontNest(result, -6889, -7237);
    
    _putDontNest(result, -6874, -7254);
    
    _putDontNest(result, -6874, -7209);
    
    _putDontNest(result, -6856, -7192);
    
    _putDontNest(result, -6847, -7086);
    
    _putDontNest(result, -6793, -7237);
    
    _putDontNest(result, -6642, -7268);
    
    _putDontNest(result, -6564, -7254);
    
    _putDontNest(result, -6513, -6897);
    
    _putDontNest(result, -6367, -6914);
    
    _putDontNest(result, -181, -350);
    
    _putDontNest(result, -7675, -7855);
    
    _putDontNest(result, -7209, -7268);
    
    _putDontNest(result, -7103, -7147);
    
    _putDontNest(result, -7086, -7164);
    
    _putDontNest(result, -7078, -7268);
    
    _putDontNest(result, -7055, -7147);
    
    _putDontNest(result, -6968, -7318);
    
    _putDontNest(result, -6948, -7041);
    
    _putDontNest(result, -6942, -7055);
    
    _putDontNest(result, -6928, -6942);
    
    _putDontNest(result, -6920, -7318);
    
    _putDontNest(result, -6906, -7254);
    
    _putDontNest(result, -6906, -7209);
    
    _putDontNest(result, -6847, -6942);
    
    _putDontNest(result, -6832, -7296);
    
    _putDontNest(result, -6832, -7164);
    
    _putDontNest(result, -6564, -6897);
    
    _putDontNest(result, -6513, -7254);
    
    _putDontNest(result, -6367, -7117);
    
    _putDontNest(result, -6345, -6874);
    
    _putDontNest(result, -6332, -7192);
    
    _putDontNest(result, -1592, -1567);
    
    _putDontNest(result, -7889, -7911);
    
    _putDontNest(result, -7622, -7883);
    
    _putDontNest(result, -7109, -7268);
    
    _putDontNest(result, -7047, -7223);
    
    _putDontNest(result, -7027, -7041);
    
    _putDontNest(result, -6988, -7164);
    
    _putDontNest(result, -6978, -7296);
    
    _putDontNest(result, -6978, -7027);
    
    _putDontNest(result, -6934, -7209);
    
    _putDontNest(result, -6920, -7223);
    
    _putDontNest(result, -6889, -7072);
    
    _putDontNest(result, -6874, -7318);
    
    _putDontNest(result, -6793, -7055);
    
    _putDontNest(result, -6793, -6928);
    
    _putDontNest(result, -6642, -7103);
    
    _putDontNest(result, -6584, -6856);
    
    _putDontNest(result, -6527, -7103);
    
    _putDontNest(result, -6367, -7133);
    
    _putDontNest(result, -6367, -6978);
    
    _putDontNest(result, -6345, -7164);
    
    _putDontNest(result, -6332, -7164);
    
    _putDontNest(result, -1567, -1592);
    
    _putDontNest(result, -7260, -7296);
    
    _putDontNest(result, -7147, -7178);
    
    _putDontNest(result, -7047, -7254);
    
    _putDontNest(result, -7011, -7041);
    
    _putDontNest(result, -6978, -7011);
    
    _putDontNest(result, -6948, -7282);
    
    _putDontNest(result, -6942, -7103);
    
    _putDontNest(result, -6920, -7254);
    
    _putDontNest(result, -6793, -7254);
    
    _putDontNest(result, -6793, -7223);
    
    _putDontNest(result, -6642, -7055);
    
    _putDontNest(result, -6642, -6962);
    
    _putDontNest(result, -6642, -6914);
    
    _putDontNest(result, -6564, -7237);
    
    _putDontNest(result, -6564, -7133);
    
    _putDontNest(result, -6564, -7117);
    
    _putDontNest(result, -6527, -7041);
    
    _putDontNest(result, -6513, -7055);
    
    _putDontNest(result, -6513, -6962);
    
    _putDontNest(result, -6513, -6914);
    
    _putDontNest(result, -6367, -6897);
    
    _putDontNest(result, -6332, -7086);
    
    _putDontNest(result, -7721, -7855);
    
    _putDontNest(result, -7178, -7282);
    
    _putDontNest(result, -7147, -7164);
    
    _putDontNest(result, -7117, -7103);
    
    _putDontNest(result, -7095, -7254);
    
    _putDontNest(result, -7095, -7223);
    
    _putDontNest(result, -7072, -7133);
    
    _putDontNest(result, -7072, -7117);
    
    _putDontNest(result, -7064, -7237);
    
    _putDontNest(result, -7033, -7147);
    
    _putDontNest(result, -7017, -7147);
    
    _putDontNest(result, -7002, -7192);
    
    _putDontNest(result, -6988, -7178);
    
    _putDontNest(result, -6968, -7254);
    
    _putDontNest(result, -6968, -7223);
    
    _putDontNest(result, -6906, -7318);
    
    _putDontNest(result, -6883, -6897);
    
    _putDontNest(result, -6874, -6928);
    
    _putDontNest(result, -6832, -7086);
    
    _putDontNest(result, -6706, -6836);
    
    _putDontNest(result, -6345, -7178);
    
    _putDontNest(result, -1592, -1618);
    
    _putDontNest(result, -7246, -7254);
    
    _putDontNest(result, -7184, -7268);
    
    _putDontNest(result, -7170, -7164);
    
    _putDontNest(result, -7133, -7103);
    
    _putDontNest(result, -7064, -7318);
    
    _putDontNest(result, -7041, -7296);
    
    _putDontNest(result, -7041, -7027);
    
    _putDontNest(result, -7041, -7011);
    
    _putDontNest(result, -7027, -6996);
    
    _putDontNest(result, -7011, -6996);
    
    _putDontNest(result, -6914, -7296);
    
    _putDontNest(result, -6914, -7027);
    
    _putDontNest(result, -6914, -7011);
    
    _putDontNest(result, -6906, -7133);
    
    _putDontNest(result, -6906, -7117);
    
    _putDontNest(result, -6832, -6942);
    
    _putDontNest(result, -6367, -7209);
    
    _putDontNest(result, -6367, -7103);
    
    _putDontNest(result, -6332, -6942);
    
    _putDontNest(result, -1146, -1146);
    
    _putDontNest(result, -1130, -1146);
    
    _putDontNest(result, -1130, -1130);
    
    _putDontNest(result, -7170, -7178);
    
    _putDontNest(result, -7139, -7282);
    
    _putDontNest(result, -7123, -7282);
    
    _putDontNest(result, -7109, -7209);
    
    _putDontNest(result, -7055, -7072);
    
    _putDontNest(result, -7033, -7086);
    
    _putDontNest(result, -7017, -7086);
    
    _putDontNest(result, -6996, -7282);
    
    _putDontNest(result, -6962, -7011);
    
    _putDontNest(result, -6934, -7268);
    
    _putDontNest(result, -6928, -7072);
    
    _putDontNest(result, -6906, -7237);
    
    _putDontNest(result, -6889, -7223);
    
    _putDontNest(result, -6874, -7117);
    
    _putDontNest(result, -6793, -7072);
    
    _putDontNest(result, -6706, -6883);
    
    _putDontNest(result, -6642, -6978);
    
    _putDontNest(result, -6564, -6928);
    
    _putDontNest(result, -6527, -6996);
    
    _putDontNest(result, -6527, -6847);
    
    _putDontNest(result, -6513, -6978);
    
    _putDontNest(result, -6332, -7147);
    
    _putDontNest(result, -7833, -7869);
    
    _putDontNest(result, -7608, -7883);
    
    _putDontNest(result, -7209, -7282);
    
    _putDontNest(result, -7103, -7133);
    
    _putDontNest(result, -7103, -7117);
    
    _putDontNest(result, -7072, -7103);
    
    _putDontNest(result, -7017, -7192);
    
    _putDontNest(result, -7002, -7318);
    
    _putDontNest(result, -6988, -7147);
    
    _putDontNest(result, -6962, -7296);
    
    _putDontNest(result, -6962, -7027);
    
    _putDontNest(result, -6948, -6996);
    
    _putDontNest(result, -6889, -7254);
    
    _putDontNest(result, -6889, -6942);
    
    _putDontNest(result, -6874, -7237);
    
    _putDontNest(result, -6874, -7133);
    
    _putDontNest(result, -6847, -7296);
    
    _putDontNest(result, -6832, -7147);
    
    _putDontNest(result, -6793, -7086);
    
    _putDontNest(result, -6620, -6338);
    
    _putDontNest(result, -6564, -7318);
    
    _putDontNest(result, -6527, -7178);
    
    _putDontNest(result, -7296, -7318);
    
    _putDontNest(result, -7086, -7282);
    
    _putDontNest(result, -7064, -7223);
    
    _putDontNest(result, -7055, -7133);
    
    _putDontNest(result, -7055, -7117);
    
    _putDontNest(result, -7041, -6996);
    
    _putDontNest(result, -7033, -7192);
    
    _putDontNest(result, -7033, -7164);
    
    _putDontNest(result, -7017, -7164);
    
    _putDontNest(result, -6978, -7041);
    
    _putDontNest(result, -6968, -7237);
    
    _putDontNest(result, -6942, -7133);
    
    _putDontNest(result, -6942, -7117);
    
    _putDontNest(result, -6920, -7237);
    
    _putDontNest(result, -6906, -7072);
    
    _putDontNest(result, -6874, -7268);
    
    _putDontNest(result, -6832, -7282);
    
    _putDontNest(result, -6793, -7133);
    
    _putDontNest(result, -6642, -6897);
    
    _putDontNest(result, -6564, -7103);
    
    _putDontNest(result, -6513, -7072);
    
    _putDontNest(result, -6367, -7055);
    
    _putDontNest(result, -7184, -7318);
    
    _putDontNest(result, -7164, -7268);
    
    _putDontNest(result, -7164, -7164);
    
    _putDontNest(result, -7078, -7209);
    
    _putDontNest(result, -7027, -7282);
    
    _putDontNest(result, -7002, -7086);
    
    _putDontNest(result, -6962, -7041);
    
    _putDontNest(result, -6942, -6978);
    
    _putDontNest(result, -6906, -7268);
    
    _putDontNest(result, -6889, -7318);
    
    _putDontNest(result, -6889, -7133);
    
    _putDontNest(result, -6883, -6996);
    
    _putDontNest(result, -6874, -7072);
    
    _putDontNest(result, -6856, -7178);
    
    _putDontNest(result, -6847, -6928);
    
    _putDontNest(result, -6793, -7117);
    
    _putDontNest(result, -6642, -7282);
    
    _putDontNest(result, -6620, -7192);
    
    _putDontNest(result, -6564, -7072);
    
    _putDontNest(result, -6527, -6897);
    
    _putDontNest(result, -6513, -7268);
    
    _putDontNest(result, -6332, -7178);
    
    _putDontNest(result, -7622, -7897);
    
    _putDontNest(result, -7095, -7318);
    
    _putDontNest(result, -7078, -7178);
    
    _putDontNest(result, -7072, -7296);
    
    _putDontNest(result, -7072, -7072);
    
    _putDontNest(result, -7047, -7318);
    
    _putDontNest(result, -7011, -7282);
    
    _putDontNest(result, -6996, -6996);
    
    _putDontNest(result, -6942, -6962);
    
    _putDontNest(result, -6942, -6914);
    
    _putDontNest(result, -6928, -7055);
    
    _putDontNest(result, -6928, -6928);
    
    _putDontNest(result, -6914, -7041);
    
    _putDontNest(result, -6889, -7117);
    
    _putDontNest(result, -6889, -7086);
    
    _putDontNest(result, -6847, -7147);
    
    _putDontNest(result, -6793, -7318);
    
    _putDontNest(result, -6793, -6942);
    
    _putDontNest(result, -6620, -7223);
    
    _putDontNest(result, -6564, -7296);
    
    _putDontNest(result, -6564, -6883);
    
    _putDontNest(result, -830, -7701);
    
    _putDontNest(result, -7246, -7268);
    
    _putDontNest(result, -7086, -7133);
    
    _putDontNest(result, -7064, -7192);
    
    _putDontNest(result, -7055, -7103);
    
    _putDontNest(result, -7002, -7164);
    
    _putDontNest(result, -6996, -7011);
    
    _putDontNest(result, -6988, -7268);
    
    _putDontNest(result, -6928, -7103);
    
    _putDontNest(result, -6906, -7223);
    
    _putDontNest(result, -6874, -7147);
    
    _putDontNest(result, -6847, -7282);
    
    _putDontNest(result, -6832, -7133);
    
    _putDontNest(result, -6642, -6928);
    
    _putDontNest(result, -6584, -7268);
    
    _putDontNest(result, -6584, -7164);
    
    _putDontNest(result, -6564, -6978);
    
    _putDontNest(result, -6513, -7103);
    
    _putDontNest(result, -6513, -6928);
    
    _putDontNest(result, -6367, -6996);
    
    _putDontNest(result, -6367, -6883);
    
    _putDontNest(result, -6345, -6856);
    
    _putDontNest(result, -6332, -6856);
    
    _putDontNest(result, -7875, -7911);
    
    _putDontNest(result, -7721, -7869);
    
    _putDontNest(result, -7274, -7296);
    
    _putDontNest(result, -7184, -7254);
    
    _putDontNest(result, -7170, -7209);
    
    _putDontNest(result, -7147, -7268);
    
    _putDontNest(result, -7095, -7209);
    
    _putDontNest(result, -7086, -7117);
    
    _putDontNest(result, -7047, -7209);
    
    _putDontNest(result, -7033, -7318);
    
    _putDontNest(result, -6996, -7027);
    
    _putDontNest(result, -6968, -7209);
    
    _putDontNest(result, -6942, -7282);
    
    _putDontNest(result, -6934, -7223);
    
    _putDontNest(result, -6920, -7209);
    
    _putDontNest(result, -6906, -7147);
    
    _putDontNest(result, -6897, -7011);
    
    _putDontNest(result, -6889, -7268);
    
    _putDontNest(result, -6832, -7117);
    
    _putDontNest(result, -6832, -6847);
    
    _putDontNest(result, -6584, -6874);
    
    _putDontNest(result, -6345, -7192);
    
    _putDontNest(result, -7608, -7869);
    
    _putDontNest(result, -7178, -7296);
    
    _putDontNest(result, -7109, -7223);
    
    _putDontNest(result, -7109, -7178);
    
    _putDontNest(result, -7103, -7103);
    
    _putDontNest(result, -7055, -7296);
    
    _putDontNest(result, -7017, -7318);
    
    _putDontNest(result, -7017, -7237);
    
    _putDontNest(result, -7002, -7254);
    
    _putDontNest(result, -6928, -7296);
    
    _putDontNest(result, -6897, -7027);
    
    _putDontNest(result, -6897, -6897);
    
    _putDontNest(result, -6874, -7086);
    
    _putDontNest(result, -6874, -6942);
    
    _putDontNest(result, -6832, -6928);
    
    _putDontNest(result, -6827, -817);
    
    _putDontNest(result, -6793, -7268);
    
    _putDontNest(result, -6642, -7117);
    
    _putDontNest(result, -6584, -7086);
    
    _putDontNest(result, -6564, -6914);
    
    _putDontNest(result, -6513, -7117);
    
    _putDontNest(result, -1102, -1116);
    
    _putDontNest(result, -7675, -7897);
    
    _putDontNest(result, -7622, -7855);
    
    _putDontNest(result, -7260, -7282);
    
    _putDontNest(result, -7170, -7192);
    
    _putDontNest(result, -7133, -7282);
    
    _putDontNest(result, -7095, -7192);
    
    _putDontNest(result, -7047, -7192);
    
    _putDontNest(result, -7033, -7237);
    
    _putDontNest(result, -6968, -7192);
    
    _putDontNest(result, -6948, -7027);
    
    _putDontNest(result, -6934, -7178);
    
    _putDontNest(result, -6906, -7086);
    
    _putDontNest(result, -6847, -7072);
    
    _putDontNest(result, -6706, -7011);
    
    _putDontNest(result, -6642, -7133);
    
    _putDontNest(result, -6584, -6942);
    
    _putDontNest(result, -6564, -7055);
    
    _putDontNest(result, -6564, -6962);
    
    _putDontNest(result, -6513, -7133);
    
    _putDontNest(result, -6345, -7209);
    
    _putDontNest(result, -1592, -1540);
    
    _putDontNest(result, -1116, -1130);
    
    _putDontNest(result, -7117, -7282);
    
    _putDontNest(result, -7103, -7296);
    
    _putDontNest(result, -7064, -7209);
    
    _putDontNest(result, -7011, -7072);
    
    _putDontNest(result, -6988, -7318);
    
    _putDontNest(result, -6962, -6996);
    
    _putDontNest(result, -6948, -7133);
    
    _putDontNest(result, -6948, -7011);
    
    _putDontNest(result, -6942, -7072);
    
    _putDontNest(result, -6920, -7192);
    
    _putDontNest(result, -6883, -7041);
    
    _putDontNest(result, -6847, -7133);
    
    _putDontNest(result, -6847, -7117);
    
    _putDontNest(result, -6847, -6978);
    
    _putDontNest(result, -6706, -7027);
    
    _putDontNest(result, -6706, -6897);
    
    _putDontNest(result, -6642, -7072);
    
    _putDontNest(result, -6642, -6847);
    
    _putDontNest(result, -6584, -7147);
    
    _putDontNest(result, -6564, -7282);
    
    _putDontNest(result, -6513, -6847);
    
    _putDontNest(result, -1116, -1146);
    
    _putDontNest(result, -7861, -7855);
    
    _putDontNest(result, -7847, -7869);
    
    _putDontNest(result, -7721, -7883);
    
    _putDontNest(result, -7282, -7318);
    
    _putDontNest(result, -7184, -7237);
    
    _putDontNest(result, -7147, -7147);
    
    _putDontNest(result, -7078, -7223);
    
    _putDontNest(result, -7072, -7282);
    
    _putDontNest(result, -7027, -7072);
    
    _putDontNest(result, -7027, -7027);
    
    _putDontNest(result, -7027, -7011);
    
    _putDontNest(result, -7011, -7027);
    
    _putDontNest(result, -6948, -7117);
    
    _putDontNest(result, -6928, -6978);
    
    _putDontNest(result, -6914, -6996);
    
    _putDontNest(result, -6832, -7103);
    
    _putDontNest(result, -6793, -6874);
    
    _putDontNest(result, -6620, -7178);
    
    _putDontNest(result, -6584, -7254);
    
    _putDontNest(result, -6527, -7011);
    
    _putDontNest(result, -6527, -6883);
    
    _putDontNest(result, -6367, -7041);
    
    _putDontNest(result, -7170, -7318);
    
    _putDontNest(result, -7086, -7072);
    
    _putDontNest(result, -7078, -7192);
    
    _putDontNest(result, -7011, -7011);
    
    _putDontNest(result, -6988, -7086);
    
    _putDontNest(result, -6978, -7055);
    
    _putDontNest(result, -6962, -7055);
    
    _putDontNest(result, -6934, -7147);
    
    _putDontNest(result, -6906, -7282);
    
    _putDontNest(result, -6897, -6996);
    
    _putDontNest(result, -6889, -7147);
    
    _putDontNest(result, -6847, -6962);
    
    _putDontNest(result, -6847, -6914);
    
    _putDontNest(result, -6832, -7072);
    
    _putDontNest(result, -6793, -7147);
    
    _putDontNest(result, -6706, -7041);
    
    _putDontNest(result, -6642, -7296);
    
    _putDontNest(result, -6620, -7209);
    
    _putDontNest(result, -6527, -7027);
    
    _putDontNest(result, -6513, -7282);
    
    _putDontNest(result, -6332, -7223);
    
    _putDontNest(result, -1745, -6700);
    
    _putDontNest(result, -7209, -7296);
    
    _putDontNest(result, -7164, -7254);
    
    _putDontNest(result, -7133, -7133);
    
    _putDontNest(result, -7133, -7117);
    
    _putDontNest(result, -7117, -7133);
    
    _putDontNest(result, -7117, -7117);
    
    _putDontNest(result, -7086, -7296);
    
    _putDontNest(result, -7064, -7178);
    
    _putDontNest(result, -7055, -7055);
    
    _putDontNest(result, -7041, -7041);
    
    _putDontNest(result, -7033, -7254);
    
    _putDontNest(result, -7017, -7254);
    
    _putDontNest(result, -7002, -7237);
    
    _putDontNest(result, -6996, -7117);
    
    _putDontNest(result, -6978, -6996);
    
    _putDontNest(result, -6942, -6928);
    
    _putDontNest(result, -6928, -6962);
    
    _putDontNest(result, -6928, -6914);
    
    _putDontNest(result, -6874, -7282);
    
    _putDontNest(result, -6832, -7268);
    
    _putDontNest(result, -6345, -7237);
    
    _putDontNest(result, -6332, -7268);
    
    _putDontNest(result, -7170, -7223);
    
    _putDontNest(result, -7133, -7296);
    
    _putDontNest(result, -7117, -7296);
    
    _putDontNest(result, -7103, -7282);
    
    _putDontNest(result, -7041, -7103);
    
    _putDontNest(result, -7002, -7268);
    
    _putDontNest(result, -6996, -7133);
    
    _putDontNest(result, -6928, -7041);
    
    _putDontNest(result, -6914, -7103);
    
    _putDontNest(result, -6914, -7055);
    
    _putDontNest(result, -6897, -7178);
    
    _putDontNest(result, -6883, -7192);
    
    _putDontNest(result, -6832, -6978);
    
    _putDontNest(result, -6642, -6942);
    
    _putDontNest(result, -6584, -7237);
    
    _putDontNest(result, -6584, -7133);
    
    _putDontNest(result, -6584, -7117);
    
    _putDontNest(result, -6513, -6942);
    
    _putDontNest(result, -6367, -7296);
    
    _putDontNest(result, -6367, -7027);
    
    _putDontNest(result, -6367, -7011);
    
    _putDontNest(result, -6345, -6883);
    
    _putDontNest(result, -7833, -7883);
    
    _putDontNest(result, -7663, -7911);
    
    _putDontNest(result, -7608, -7855);
    
    _putDontNest(result, -7268, -7268);
    
    _putDontNest(result, -7170, -7254);
    
    _putDontNest(result, -7164, -7318);
    
    _putDontNest(result, -7109, -7192);
    
    _putDontNest(result, -7055, -7282);
    
    _putDontNest(result, -6962, -7209);
    
    _putDontNest(result, -6948, -7223);
    
    _putDontNest(result, -6928, -7282);
    
    _putDontNest(result, -6889, -7164);
    
    _putDontNest(result, -6883, -7027);
    
    _putDontNest(result, -6883, -7011);
    
    _putDontNest(result, -6847, -7103);
    
    _putDontNest(result, -6793, -7282);
    
    _putDontNest(result, -6642, -7147);
    
    _putDontNest(result, -6620, -7041);
    
    _putDontNest(result, -6564, -6847);
    
    _putDontNest(result, -6513, -7147);
    
    _putDontNest(result, -3803, -7192);
    
    _putDontNest(result, -2088, -1618);
    
    _putDontNest(result, -7675, -7883);
    
    _putDontNest(result, -7622, -7869);
    
    _putDontNest(result, -7095, -7178);
    
    _putDontNest(result, -7078, -7086);
    
    _putDontNest(result, -7047, -7164);
    
    _putDontNest(result, -7027, -7117);
    
    _putDontNest(result, -7011, -7133);
    
    _putDontNest(result, -6996, -7072);
    
    _putDontNest(result, -6978, -7103);
    
    _putDontNest(result, -6968, -7178);
    
    _putDontNest(result, -6962, -7103);
    
    _putDontNest(result, -6948, -7072);
    
    _putDontNest(result, -6942, -7011);
    
    _putDontNest(result, -6920, -7164);
    
    _putDontNest(result, -6832, -6914);
    
    _putDontNest(result, -6793, -7164);
    
    _putDontNest(result, -6706, -7178);
    
    _putDontNest(result, -6584, -6928);
    
    _putDontNest(result, -6345, -7223);
    
    _putDontNest(result, -6345, -6836);
    
    _putDontNest(result, -5785, -1540);
    
    _putDontNest(result, -181, -319);
    
    _putDontNest(result, -7897, -7883);
    
    _putDontNest(result, -7883, -7897);
    
    _putDontNest(result, -7869, -7911);
    
    _putDontNest(result, -7254, -7282);
    
    _putDontNest(result, -7192, -7296);
    
    _putDontNest(result, -7184, -7192);
    
    _putDontNest(result, -7139, -7223);
    
    _putDontNest(result, -7123, -7223);
    
    _putDontNest(result, -7027, -7133);
    
    _putDontNest(result, -7011, -7117);
    
    _putDontNest(result, -6988, -7254);
    
    _putDontNest(result, -6942, -7296);
    
    _putDontNest(result, -6942, -7027);
    
    _putDontNest(result, -6889, -7282);
    
    _putDontNest(result, -6832, -7055);
    
    _putDontNest(result, -6832, -6962);
    
    _putDontNest(result, -6642, -7086);
    
    _putDontNest(result, -6620, -6856);
    
    _putDontNest(result, -6584, -7318);
    
    _putDontNest(result, -6513, -7086);
    
    _putDontNest(result, -6345, -7254);
    
    _putDontNest(result, -1582, -1552);
    
    _putDontNest(result, -7721, -7897);
    
    _putDontNest(result, -7599, -7911);
    
    _putDontNest(result, -7147, -7254);
    
    _putDontNest(result, -7095, -7164);
    
    _putDontNest(result, -7078, -7147);
    
    _putDontNest(result, -7047, -7178);
    
    _putDontNest(result, -6968, -7164);
    
    _putDontNest(result, -6962, -7117);
    
    _putDontNest(result, -6934, -7192);
    
    _putDontNest(result, -6934, -7086);
    
    _putDontNest(result, -6920, -7178);
    
    _putDontNest(result, -6897, -7041);
    
    _putDontNest(result, -6897, -6962);
    
    _putDontNest(result, -6874, -7296);
    
    _putDontNest(result, -6874, -7164);
    
    _putDontNest(result, -6847, -7268);
    
    _putDontNest(result, -6847, -7237);
    
    _putDontNest(result, -6847, -6897);
    
    _putDontNest(result, -6706, -6996);
    
    _putDontNest(result, -6620, -7011);
    
    _putDontNest(result, -6620, -6996);
    
    _putDontNest(result, -1552, -1582);
    
    _putDontNest(result, -7807, -7911);
    
    _putDontNest(result, -7170, -7237);
    
    _putDontNest(result, -7156, -7192);
    
    _putDontNest(result, -7109, -7147);
    
    _putDontNest(result, -7047, -7086);
    
    _putDontNest(result, -7017, -7268);
    
    _putDontNest(result, -6996, -7055);
    
    _putDontNest(result, -6962, -7133);
    
    _putDontNest(result, -6962, -6978);
    
    _putDontNest(result, -6928, -6996);
    
    _putDontNest(result, -6914, -7133);
    
    _putDontNest(result, -6914, -6978);
    
    _putDontNest(result, -6906, -7296);
    
    _putDontNest(result, -6906, -7164);
    
    _putDontNest(result, -6897, -6914);
    
    _putDontNest(result, -6883, -6928);
    
    _putDontNest(result, -6847, -6996);
    
    _putDontNest(result, -6620, -7268);
    
    _putDontNest(result, -6620, -7027);
    
    _putDontNest(result, -6345, -7318);
    
    _putDontNest(result, -6332, -7254);
    
    _putDontNest(result, -6332, -7209);
    
    _putDontNest(result, -7861, -7869);
    
    _putDontNest(result, -7847, -7855);
    
    _putDontNest(result, -7268, -7318);
    
    _putDontNest(result, -7164, -7296);
    
    _putDontNest(result, -7147, -7318);
    
    _putDontNest(result, -7139, -7133);
    
    _putDontNest(result, -7086, -7254);
    
    _putDontNest(result, -7033, -7268);
    
    _putDontNest(result, -6962, -6962);
    
    _putDontNest(result, -6942, -7318);
    
    _putDontNest(result, -6914, -7117);
    
    _putDontNest(result, -6847, -7055);
    
    _putDontNest(result, -6832, -7254);
    
    _putDontNest(result, -6793, -6856);
    
    _putDontNest(result, -6706, -6847);
    
    _putDontNest(result, -6584, -7223);
    
    _putDontNest(result, -6584, -7103);
    
    _putDontNest(result, -6513, -7296);
    
    _putDontNest(result, -6513, -7164);
    
    _putDontNest(result, -2088, -1344);
    
    _putDontNest(result, -1488, -1618);
    
    _putDontNest(result, -449, -473);
    
    _putDontNest(result, -7156, -7223);
    
    _putDontNest(result, -7139, -7117);
    
    _putDontNest(result, -7123, -7117);
    
    _putDontNest(result, -7072, -7268);
    
    _putDontNest(result, -7064, -7164);
    
    _putDontNest(result, -7011, -7209);
    
    _putDontNest(result, -6988, -7237);
    
    _putDontNest(result, -6978, -7133);
    
    _putDontNest(result, -6978, -6978);
    
    _putDontNest(result, -6968, -7147);
    
    _putDontNest(result, -6962, -6914);
    
    _putDontNest(result, -6948, -7055);
    
    _putDontNest(result, -6942, -7041);
    
    _putDontNest(result, -6920, -7147);
    
    _putDontNest(result, -6914, -6914);
    
    _putDontNest(result, -6897, -6978);
    
    _putDontNest(result, -6832, -6897);
    
    _putDontNest(result, -6706, -7055);
    
    _putDontNest(result, -6584, -7192);
    
    _putDontNest(result, -6584, -7072);
    
    _putDontNest(result, -6564, -7164);
    
    _putDontNest(result, -6332, -6897);
    
    _putDontNest(result, -7599, -7897);
    
    _putDontNest(result, -7237, -7282);
    
    _putDontNest(result, -7223, -7296);
    
    _putDontNest(result, -7184, -7223);
    
    _putDontNest(result, -7139, -7192);
    
    _putDontNest(result, -7123, -7133);
    
    _putDontNest(result, -7109, -7164);
    
    _putDontNest(result, -7064, -7086);
    
    _putDontNest(result, -7041, -7055);
    
    _putDontNest(result, -7027, -7055);
    
    _putDontNest(result, -6988, -7296);
    
    _putDontNest(result, -6978, -7117);
    
    _putDontNest(result, -6942, -7254);
    
    _putDontNest(result, -6914, -6962);
    
    _putDontNest(result, -6883, -7072);
    
    _putDontNest(result, -6832, -7237);
    
    _putDontNest(result, -6832, -6996);
    
    _putDontNest(result, -6564, -6874);
    
    _putDontNest(result, -6345, -7268);
    
    _putDontNest(result, -6345, -7027);
    
    _putDontNest(result, -6345, -6897);
    
    _putDontNest(result, -1582, -1567);
    
    _putDontNest(result, -7254, -7296);
    
    _putDontNest(result, -7192, -7282);
    
    _putDontNest(result, -7147, -7296);
    
    _putDontNest(result, -7123, -7192);
    
    _putDontNest(result, -7011, -7055);
    
    _putDontNest(result, -6996, -7192);
    
    _putDontNest(result, -6978, -7178);
    
    _putDontNest(result, -6978, -7072);
    
    _putDontNest(result, -6948, -7209);
    
    _putDontNest(result, -6948, -7103);
    
    _putDontNest(result, -6897, -7192);
    
    _putDontNest(result, -6889, -7296);
    
    _putDontNest(result, -6883, -7178);
    
    _putDontNest(result, -6847, -7254);
    
    _putDontNest(result, -6706, -7209);
    
    _putDontNest(result, -6584, -6978);
    
    _putDontNest(result, -6345, -7011);
    
    _putDontNest(result, -6332, -7237);
    
    _putDontNest(result, -6332, -6996);
    
    _putDontNest(result, -5785, -1618);
    
    _putDontNest(result, -1836, -1618);
    
    _putDontNest(result, -1567, -1582);
    
    _putDontNest(result, -7833, -7897);
    
    _putDontNest(result, -7663, -7897);
    
    _putDontNest(result, -7170, -7268);
    
    _putDontNest(result, -7133, -7254);
    
    _putDontNest(result, -7123, -7209);
    
    _putDontNest(result, -7117, -7254);
    
    _putDontNest(result, -7064, -7147);
    
    _putDontNest(result, -7055, -7268);
    
    _putDontNest(result, -6962, -7178);
    
    _putDontNest(result, -6948, -7192);
    
    _putDontNest(result, -6928, -7268);
    
    _putDontNest(result, -6928, -7027);
    
    _putDontNest(result, -6793, -7296);
    
    _putDontNest(result, -6706, -7192);
    
    _putDontNest(result, -6706, -7103);
    
    _putDontNest(result, -6584, -7055);
    
    _putDontNest(result, -6584, -6962);
    
    _putDontNest(result, -6584, -6914);
    
    _putDontNest(result, -6564, -7086);
    
    _putDontNest(result, -6564, -6942);
    
    _putDontNest(result, -6332, -7041);
    
    _putDontNest(result, -1567, -1567);
    
    _putDontNest(result, -7139, -7209);
    
    _putDontNest(result, -7139, -7103);
    
    _putDontNest(result, -7123, -7103);
    
    _putDontNest(result, -7103, -7268);
    
    _putDontNest(result, -7086, -7318);
    
    _putDontNest(result, -7041, -7178);
    
    _putDontNest(result, -7041, -7072);
    
    _putDontNest(result, -7002, -7282);
    
    _putDontNest(result, -6996, -7103);
    
    _putDontNest(result, -6962, -7072);
    
    _putDontNest(result, -6934, -7164);
    
    _putDontNest(result, -6928, -7011);
    
    _putDontNest(result, -6914, -7178);
    
    _putDontNest(result, -6914, -7072);
    
    _putDontNest(result, -6832, -7318);
    
    _putDontNest(result, -6832, -7041);
    
    _putDontNest(result, -6367, -7282);
    
    _putDontNest(result, -6332, -7318);
    
    _putDontNest(result, -2088, -1540);
    
    _putDontNest(result, -1592, -1592);
    
    _putDontNest(result, -1552, -1552);
    
    _putDontNest(result, -1376, -1618);
    
    _putDontNest(result, -7897, -7897);
    
    _putDontNest(result, -7883, -7883);
    
    _putDontNest(result, -7855, -7911);
    
    _putDontNest(result, -7268, -7254);
    
    _putDontNest(result, -7254, -7318);
    
    _putDontNest(result, -7237, -7296);
    
    _putDontNest(result, -7223, -7282);
    
    _putDontNest(result, -7156, -7178);
    
    _putDontNest(result, -7133, -7318);
    
    _putDontNest(result, -7117, -7318);
    
    _putDontNest(result, -7072, -7254);
    
    _putDontNest(result, -7011, -7192);
    
    _putDontNest(result, -6968, -7117);
    
    _putDontNest(result, -6948, -6978);
    
    _putDontNest(result, -6897, -7103);
    
    _putDontNest(result, -6897, -7055);
    
    _putDontNest(result, -6897, -6928);
    
    _putDontNest(result, -6883, -6914);
    
    _putDontNest(result, -6856, -7296);
    
    _putDontNest(result, -6856, -7164);
    
    _putDontNest(result, -6706, -7117);
    
    _putDontNest(result, -6564, -7147);
    
    _putDontNest(result, -7078, -7103);
    
    _putDontNest(result, -7041, -7133);
    
    _putDontNest(result, -7041, -7086);
    
    _putDontNest(result, -7027, -7192);
    
    _putDontNest(result, -7027, -7164);
    
    _putDontNest(result, -7011, -7164);
    
    _putDontNest(result, -6978, -7147);
    
    _putDontNest(result, -6968, -7133);
    
    _putDontNest(result, -6968, -7086);
    
    _putDontNest(result, -6920, -7133);
    
    _putDontNest(result, -6920, -7086);
    
    _putDontNest(result, -6883, -6962);
    
    _putDontNest(result, -6706, -7133);
    
    _putDontNest(result, -6706, -6978);
    
    _putDontNest(result, -6642, -7164);
    
    _putDontNest(result, -6620, -6897);
    
    _putDontNest(result, -6584, -7209);
    
    _putDontNest(result, -6527, -7296);
    
    _putDontNest(result, -6527, -7164);
    
    _putDontNest(result, -1582, -1582);
    
    _putDontNest(result, -1488, -1540);
    
    _putDontNest(result, -830, -7716);
    
    _putDontNest(result, -7807, -7897);
    
    _putDontNest(result, -7164, -7282);
    
    _putDontNest(result, -7047, -7147);
    
    _putDontNest(result, -7041, -7117);
    
    _putDontNest(result, -7033, -7282);
    
    _putDontNest(result, -7017, -7282);
    
    _putDontNest(result, -6962, -6928);
    
    _putDontNest(result, -6942, -6996);
    
    _putDontNest(result, -6928, -7318);
    
    _putDontNest(result, -6920, -7117);
    
    _putDontNest(result, -6914, -7147);
    
    _putDontNest(result, -6883, -7133);
    
    _putDontNest(result, -6883, -6978);
    
    _putDontNest(result, -6847, -7041);
    
    _putDontNest(result, -6832, -7192);
    
    _putDontNest(result, -6706, -6962);
    
    _putDontNest(result, -6706, -6914);
    
    _putDontNest(result, -6620, -7254);
    
    _putDontNest(result, -6620, -6836);
    
    _putDontNest(result, -6584, -7178);
    
    _putDontNest(result, -6345, -6847);
    
    _putDontNest(result, -1552, -1567);
    
    _putDontNest(result, -7721, -7911);
    
    _putDontNest(result, -7156, -7209);
    
    _putDontNest(result, -7095, -7147);
    
    _putDontNest(result, -7086, -7268);
    
    _putDontNest(result, -7078, -7164);
    
    _putDontNest(result, -7027, -7103);
    
    _putDontNest(result, -7011, -7103);
    
    _putDontNest(result, -6962, -7147);
    
    _putDontNest(result, -6934, -7055);
    
    _putDontNest(result, -6883, -7117);
    
    _putDontNest(result, -6847, -7318);
    
    _putDontNest(result, -6832, -7027);
    
    _putDontNest(result, -6832, -6883);
    
    _putDontNest(result, -6706, -7318);
    
    _putDontNest(result, -6513, -7209);
    
    _putDontNest(result, -6367, -6856);
    
    _putDontNest(result, -6345, -6996);
    
    _putDontNest(result, -6332, -7296);
    
    _putDontNest(result, -6332, -7027);
    
    _putDontNest(result, -6332, -7011);
    
    _putDontNest(result, -6332, -6883);
    
    _putDontNest(result, -1792, -1618);
    
    _putDontNest(result, -7897, -7911);
    
    _putDontNest(result, -7869, -7883);
    
    _putDontNest(result, -7855, -7897);
    
    _putDontNest(result, -7599, -7883);
    
    _putDontNest(result, -7237, -7318);
    
    _putDontNest(result, -7139, -7178);
    
    _putDontNest(result, -7133, -7268);
    
    _putDontNest(result, -7117, -7268);
    
    _putDontNest(result, -7055, -7254);
    
    _putDontNest(result, -7002, -7296);
    
    _putDontNest(result, -6996, -7164);
    
    _putDontNest(result, -6988, -6373);
    
    _putDontNest(result, -6928, -7254);
    
    _putDontNest(result, -6914, -6928);
    
    _putDontNest(result, -6832, -7011);
    
    _putDontNest(result, -6620, -7318);
    
    _putDontNest(result, -6584, -6996);
    
    _putDontNest(result, -6367, -7268);
    
    _putDontNest(result, -1695, -1618);
    
    _putDontNest(result, -1567, -1552);
    
    _putDontNest(result, -1552, -1618);
    
    _putDontNest(result, -1376, -1540);
    
    _putDontNest(result, -7675, -7911);
    
    _putDontNest(result, -7268, -7296);
    
    _putDontNest(result, -7184, -7209);
    
    _putDontNest(result, -7170, -7282);
    
    _putDontNest(result, -7139, -7164);
    
    _putDontNest(result, -7123, -7178);
    
    _putDontNest(result, -7123, -7164);
    
    _putDontNest(result, -7103, -7254);
    
    _putDontNest(result, -7064, -7117);
    
    _putDontNest(result, -6996, -7178);
    
    _putDontNest(result, -6978, -7192);
    
    _putDontNest(result, -6934, -7103);
    
    _putDontNest(result, -6897, -7072);
    
    _putDontNest(result, -6883, -7268);
    
    _putDontNest(result, -6856, -7147);
    
    _putDontNest(result, -6856, -6942);
    
    _putDontNest(result, -6706, -7254);
    
    _putDontNest(result, -6706, -7223);
    
    _putDontNest(result, -6620, -6928);
    
    _putDontNest(result, -6584, -6847);
    
    _putDontNest(result, -6345, -6373);
    
    _putDontNest(result, -830, -7593);
    
    _putDontNest(result, -7833, -7911);
    
    _putDontNest(result, -7663, -7883);
    
    _putDontNest(result, -7302, -7318);
    
    _putDontNest(result, -7282, -7282);
    
    _putDontNest(result, -7254, -7254);
    
    _putDontNest(result, -7192, -7268);
    
    _putDontNest(result, -7178, -7164);
    
    _putDontNest(result, -7147, -7282);
    
    _putDontNest(result, -7109, -7103);
    
    _putDontNest(result, -7072, -7318);
    
    _putDontNest(result, -7064, -7133);
    
    _putDontNest(result, -6968, -7072);
    
    _putDontNest(result, -6962, -7192);
    
    _putDontNest(result, -6948, -7178);
    
    _putDontNest(result, -6942, -7268);
    
    _putDontNest(result, -6856, -7086);
    
    _putDontNest(result, -6793, -6883);
    
    _putDontNest(result, -6642, -6874);
    
    _putDontNest(result, -6584, -7041);
    
    _putDontNest(result, -6527, -6856);
    
    _putDontNest(result, -6513, -6874);
    
    _putDontNest(result, -1122, -1146);
    
    _putDontNest(result, -7237, -7237);
    
    _putDontNest(result, -7178, -7178);
    
    _putDontNest(result, -7047, -7072);
    
    _putDontNest(result, -7041, -7192);
    
    _putDontNest(result, -7027, -7178);
    
    _putDontNest(result, -6988, -7282);
    
    _putDontNest(result, -6948, -7164);
    
    _putDontNest(result, -6920, -7072);
    
    _putDontNest(result, -6914, -7192);
    
    _putDontNest(result, -6897, -7254);
    
    _putDontNest(result, -6883, -7103);
    
    _putDontNest(result, -6847, -7192);
    
    _putDontNest(result, -6847, -7027);
    
    _putDontNest(result, -6847, -7011);
    
    _putDontNest(result, -6706, -7072);
    
    _putDontNest(result, -6620, -7237);
    
    _putDontNest(result, -6620, -7133);
    
    _putDontNest(result, -6620, -7117);
    
    _putDontNest(result, -6345, -7282);
    
    _putDontNest(result, -6332, -7282);
    
    _putDontNest(result, -1836, -1540);
    
    _putDontNest(result, -1582, -1540);
    
    _putDontNest(result, -7599, -7869);
    
    _putDontNest(result, -7095, -7133);
    
    _putDontNest(result, -7095, -7117);
    
    _putDontNest(result, -7078, -7282);
    
    _putDontNest(result, -7064, -7103);
    
    _putDontNest(result, -7017, -7296);
    
    _putDontNest(result, -7011, -7178);
    
    _putDontNest(result, -6996, -7147);
    
    _putDontNest(result, -6978, -7318);
    
    _putDontNest(result, -6914, -7086);
    
    _putDontNest(result, -6897, -6942);
    
    _putDontNest(result, -6883, -7055);
    
    _putDontNest(result, -6874, -7027);
    
    _putDontNest(result, -6874, -7011);
    
    _putDontNest(result, -6706, -7237);
    
    _putDontNest(result, -6706, -6874);
    
    _putDontNest(result, -6642, -7178);
    
    _putDontNest(result, -6620, -7296);
    
    _putDontNest(result, -6620, -6883);
    
    _putDontNest(result, -6564, -7223);
    
    _putDontNest(result, -6345, -6914);
    
    _putDontNest(result, -1567, -1618);
    
    _putDontNest(result, -7288, -7318);
    
    _putDontNest(result, -7209, -7209);
    
    _putDontNest(result, -7192, -7318);
    
    _putDontNest(result, -7156, -7268);
    
    _putDontNest(result, -7156, -7164);
    
    _putDontNest(result, -7047, -7133);
    
    _putDontNest(result, -7047, -7117);
    
    _putDontNest(result, -7033, -7296);
    
    _putDontNest(result, -6968, -7055);
    
    _putDontNest(result, -6962, -7086);
    
    _putDontNest(result, -6934, -7133);
    
    _putDontNest(result, -6934, -7117);
    
    _putDontNest(result, -6934, -6978);
    
    _putDontNest(result, -6906, -7027);
    
    _putDontNest(result, -6906, -7011);
    
    _putDontNest(result, -6897, -7318);
    
    _putDontNest(result, -6620, -7072);
    
    _putDontNest(result, -6564, -7192);
    
    _putDontNest(result, -6527, -7282);
    
    _putDontNest(result, -6345, -7041);
    
    _putDontNest(result, -6345, -6962);
    
    _putDontNest(result, -1792, -1540);
    
    _putDontNest(result, -7807, -7883);
    
    _putDontNest(result, -7663, -7869);
    
    _putDontNest(result, -7237, -7254);
    
    _putDontNest(result, -7103, -7318);
    
    _putDontNest(result, -7086, -7178);
    
    _putDontNest(result, -7064, -7296);
    
    _putDontNest(result, -7055, -7318);
    
    _putDontNest(result, -7041, -7147);
    
    _putDontNest(result, -6978, -7086);
    
    _putDontNest(result, -6962, -6942);
    
    _putDontNest(result, -6914, -7318);
    
    _putDontNest(result, -6914, -6942);
    
    _putDontNest(result, -6897, -7133);
    
    _putDontNest(result, -6897, -7117);
    
    _putDontNest(result, -6832, -7178);
    
    _putDontNest(result, -6706, -6928);
    
    _putDontNest(result, -6642, -7209);
    
    _putDontNest(result, -6620, -7103);
    
    _putDontNest(result, -6584, -7011);
    
    _putDontNest(result, -6345, -6978);
    
    _putDontNest(result, -830, -7812);
    
    _putDontNest(result, -181, -300);
    
    _putDontNest(result, -7223, -7268);
    
    _putDontNest(result, -7192, -7254);
    
    _putDontNest(result, -7078, -7133);
    
    _putDontNest(result, -7072, -7192);
    
    _putDontNest(result, -7064, -7072);
    
    _putDontNest(result, -7047, -7103);
    
    _putDontNest(result, -6978, -7164);
    
    _putDontNest(result, -6962, -7318);
    
    _putDontNest(result, -6948, -7147);
    
    _putDontNest(result, -6920, -7103);
    
    _putDontNest(result, -6920, -7055);
    
    _putDontNest(result, -6897, -7086);
    
    _putDontNest(result, -6856, -7282);
    
    _putDontNest(result, -6706, -7147);
    
    _putDontNest(result, -6584, -7027);
    
    _putDontNest(result, -6527, -6942);
    
    _putDontNest(result, -6513, -7223);
    
    _putDontNest(result, -6367, -7237);
    
    _putDontNest(result, -6367, -6874);
    
    _putDontNest(result, -6345, -7296);
    
    _putDontNest(result, -6332, -7117);
    
    _putDontNest(result, -5785, -1344);
    
    _putDontNest(result, -7883, -7911);
    
    _putDontNest(result, -7869, -7897);
    
    _putDontNest(result, -7855, -7883);
    
    _putDontNest(result, -7254, -7268);
    
    _putDontNest(result, -7223, -7237);
    
    _putDontNest(result, -7139, -7268);
    
    _putDontNest(result, -7133, -7178);
    
    _putDontNest(result, -7123, -7268);
    
    _putDontNest(result, -7078, -7117);
    
    _putDontNest(result, -7041, -7254);
    
    _putDontNest(result, -7027, -7147);
    
    _putDontNest(result, -6996, -7268);
    
    _putDontNest(result, -6914, -7254);
    
    _putDontNest(result, -6889, -7027);
    
    _putDontNest(result, -6883, -7164);
    
    _putDontNest(result, -6832, -6856);
    
    _putDontNest(result, -6620, -7055);
    
    _putDontNest(result, -6620, -6962);
    
    _putDontNest(result, -6527, -7147);
    
    _putDontNest(result, -6367, -7164);
    
    _putDontNest(result, -6332, -7133);
    
    _putDontNest(result, -1582, -1618);
    
    _putDontNest(result, -7282, -7296);
    
    _putDontNest(result, -7170, -7296);
    
    _putDontNest(result, -7117, -7178);
    
    _putDontNest(result, -7095, -7103);
    
    _putDontNest(result, -7041, -7164);
    
    _putDontNest(result, -7011, -7147);
    
    _putDontNest(result, -6968, -7103);
    
    _putDontNest(result, -6962, -7254);
    
    _putDontNest(result, -6934, -7282);
    
    _putDontNest(result, -6914, -7164);
    
    _putDontNest(result, -6897, -7268);
    
    _putDontNest(result, -6889, -7011);
    
    _putDontNest(result, -6856, -6928);
    
    _putDontNest(result, -6793, -6897);
    
    _putDontNest(result, -6642, -6856);
    
    _putDontNest(result, -6620, -6914);
    
    _putDontNest(result, -6513, -6856);
    
    _putDontNest(result, -6367, -7254);
    
    _putDontNest(result, -6345, -7103);
    
    _putDontNest(result, -6332, -6928);
    
    _putDontNest(result, -6332, -6847);
    
    _putDontNest(result, -1695, -1540);
    
    _putDontNest(result, -7268, -7282);
    
    _putDontNest(result, -7223, -7318);
    
    _putDontNest(result, -7178, -7192);
    
    _putDontNest(result, -7103, -7192);
    
    _putDontNest(result, -7055, -7192);
    
    _putDontNest(result, -7047, -7296);
    
    _putDontNest(result, -7027, -7086);
    
    _putDontNest(result, -6978, -7254);
    
    _putDontNest(result, -6948, -7268);
    
    _putDontNest(result, -6942, -7178);
    
    _putDontNest(result, -6934, -7072);
    
    _putDontNest(result, -6928, -7192);
    
    _putDontNest(result, -6920, -7296);
    
    _putDontNest(result, -6856, -7133);
    
    _putDontNest(result, -6847, -7178);
    
    _putDontNest(result, -6793, -7027);
    
    _putDontNest(result, -6793, -7011);
    
    _putDontNest(result, -6620, -6978);
    
    _putDontNest(result, -6527, -7086);
    
    _putDontNest(result, -6527, -6874);
    
    _putDontNest(result, -1552, -1540);
    
    _putDontNest(result, -1108, -1130);
    
    _putDontNest(result, -7622, -7911);
    
    _putDontNest(result, -7237, -7268);
    
    _putDontNest(result, -7123, -7147);
    
    _putDontNest(result, -7109, -7282);
    
    _putDontNest(result, -7095, -7296);
    
    _putDontNest(result, -7011, -7086);
    
    _putDontNest(result, -6996, -7318);
    
    _putDontNest(result, -6968, -7296);
    
    _putDontNest(result, -6962, -7164);
    
    _putDontNest(result, -6906, -7178);
    
    _putDontNest(result, -6883, -7254);
    
    _putDontNest(result, -6883, -6942);
    
    _putDontNest(result, -6856, -7268);
    
    _putDontNest(result, -6856, -7117);
    
    _putDontNest(result, -6793, -6847);
    
    _putDontNest(result, -6706, -7268);
    
    _putDontNest(result, -6706, -6856);
    
    _putDontNest(result, -6642, -7192);
    
    _putDontNest(result, -6527, -7072);
    
    _putDontNest(result, -6513, -7178);
    
    _putDontNest(result, -6367, -7318);
    
    _putDontNest(result, -1108, -1146);
    
    _putDontNest(result, -7223, -7254);
    
    _putDontNest(result, -7209, -7223);
    
    _putDontNest(result, -7064, -7282);
    
    _putDontNest(result, -7011, -7268);
    
    _putDontNest(result, -7002, -7055);
    
    _putDontNest(result, -6962, -7237);
    
    _putDontNest(result, -6920, -6978);
    
    _putDontNest(result, -6897, -7147);
    
    _putDontNest(result, -6874, -7178);
    
    _putDontNest(result, -6874, -6897);
    
    _putDontNest(result, -6856, -7072);
    
    _putDontNest(result, -6832, -7223);
    
    _putDontNest(result, -6793, -6996);
    
    _putDontNest(result, -6706, -7086);
    
    _putDontNest(result, -6642, -7027);
    
    _putDontNest(result, -6642, -7011);
    
    _putDontNest(result, -6564, -7209);
    
    _putDontNest(result, -6527, -7268);
    
    _putDontNest(result, -6367, -6942);
    
    _putDontNest(result, -6345, -6928);
    
    _putDontNest(result, -6332, -7103);
    
    _putDontNest(result, -1393, -1540);
    
    _putDontNest(result, -7599, -7855);
    
    _putDontNest(result, -7274, -7318);
    
    _putDontNest(result, -7178, -7318);
    
    _putDontNest(result, -7139, -7237);
    
    _putDontNest(result, -7139, -7147);
    
    _putDontNest(result, -7086, -7192);
    
    _putDontNest(result, -7078, -7072);
    
    _putDontNest(result, -7041, -7318);
    
    _putDontNest(result, -7027, -7268);
    
    _putDontNest(result, -6996, -7086);
    
    _putDontNest(result, -6968, -7041);
    
    _putDontNest(result, -6948, -7086);
    
    _putDontNest(result, -6942, -7147);
    
    _putDontNest(result, -6889, -6996);
    
    _putDontNest(result, -6883, -7318);
    
    _putDontNest(result, -6856, -7103);
    
    _putDontNest(result, -6706, -6942);
    
    _putDontNest(result, -6564, -7178);
    
    _putDontNest(result, -6367, -7086);
    
    _putDontNest(result, -6345, -7055);
    
    _putDontNest(result, -6332, -7072);
    
    _putDontNest(result, -1567, -1540);
    
    _putDontNest(result, -7663, -7855);
    
    _putDontNest(result, -7156, -7254);
    
    _putDontNest(result, -7123, -7237);
    
    _putDontNest(result, -7109, -7133);
    
    _putDontNest(result, -7109, -7117);
    
    _putDontNest(result, -7078, -7296);
    
    _putDontNest(result, -7072, -7178);
    
    _putDontNest(result, -7033, -7209);
    
    _putDontNest(result, -7033, -7055);
    
    _putDontNest(result, -7017, -7209);
    
    _putDontNest(result, -6988, -7133);
    
    _putDontNest(result, -6988, -7117);
    
    _putDontNest(result, -6988, -6338);
    
    _putDontNest(result, -6948, -7318);
    
    _putDontNest(result, -6920, -7041);
    
    _putDontNest(result, -6883, -7086);
    
    _putDontNest(result, -6642, -7223);
    
    _putDontNest(result, -6620, -7282);
    
    _putDontNest(result, -6584, -6897);
    
    _putDontNest(result, -6564, -6996);
    
    _putDontNest(result, -6527, -7223);
    
    _putDontNest(result, -6527, -6928);
    
    _putDontNest(result, -7260, -7268);
    
    _putDontNest(result, -7209, -7237);
    
    _putDontNest(result, -7178, -7254);
    
    _putDontNest(result, -7109, -7296);
    
    _putDontNest(result, -7095, -7282);
    
    _putDontNest(result, -7017, -7055);
    
    _putDontNest(result, -6988, -7072);
    
    _putDontNest(result, -6978, -7268);
    
    _putDontNest(result, -6968, -7282);
    
    _putDontNest(result, -6906, -7041);
    
    _putDontNest(result, -6889, -7178);
    
    _putDontNest(result, -6883, -7296);
    
    _putDontNest(result, -6874, -7041);
    
    _putDontNest(result, -6856, -6914);
    
    _putDontNest(result, -6832, -6874);
    
    _putDontNest(result, -6706, -7282);
    
    _putDontNest(result, -6564, -6856);
    
    _putDontNest(result, -6527, -7318);
    
    _putDontNest(result, -6345, -7072);
    
    _putDontNest(result, -6332, -6978);
    
    _putDontNest(result, -6332, -6874);
    
    _putDontNest(result, -181, -359);
    
    _putDontNest(result, -7296, -7296);
    
    _putDontNest(result, -7184, -7296);
    
    _putDontNest(result, -7156, -7318);
    
    _putDontNest(result, -7133, -7192);
    
    _putDontNest(result, -7117, -7192);
    
    _putDontNest(result, -7086, -7086);
    
    _putDontNest(result, -7055, -7164);
    
    _putDontNest(result, -7047, -7282);
    
    _putDontNest(result, -7027, -7318);
    
    _putDontNest(result, -7011, -7318);
    
    _putDontNest(result, -6948, -7254);
    
    _putDontNest(result, -6928, -7164);
    
    _putDontNest(result, -6920, -7282);
    
    _putDontNest(result, -6897, -7164);
    
    _putDontNest(result, -6856, -7055);
    
    _putDontNest(result, -6856, -6962);
    
    _putDontNest(result, -6847, -7209);
    
    _putDontNest(result, -6527, -7117);
    
    _putDontNest(result, -6367, -7192);
    
    _putDontNest(result, -6332, -7055);
    
    _putDontNest(result, -6332, -6962);
    
    _putDontNest(result, -2095, -6700);
    
    _putDontNest(result, -7156, -7237);
    
    _putDontNest(result, -7123, -7254);
    
    _putDontNest(result, -7103, -7178);
    
    _putDontNest(result, -7041, -7268);
    
    _putDontNest(result, -7011, -7237);
    
    _putDontNest(result, -7002, -7209);
    
    _putDontNest(result, -7002, -7103);
    
    _putDontNest(result, -6996, -7254);
    
    _putDontNest(result, -6988, -7223);
    
    _putDontNest(result, -6934, -7296);
    
    _putDontNest(result, -6934, -7027);
    
    _putDontNest(result, -6934, -7011);
    
    _putDontNest(result, -6914, -7268);
    
    _putDontNest(result, -6906, -6996);
    
    _putDontNest(result, -6897, -7282);
    
    _putDontNest(result, -6620, -6847);
    
    _putDontNest(result, -6564, -7041);
    
    _putDontNest(result, -6527, -7237);
    
    _putDontNest(result, -6527, -7133);
    
    _putDontNest(result, -6332, -6914);
    
    _putDontNest(result, -1582, -1592);
    
    _putDontNest(result, -1116, -1116);
    
    _putDontNest(result, -7861, -7911);
    
    _putDontNest(result, -7807, -7869);
    
    _putDontNest(result, -7260, -7318);
    
    _putDontNest(result, -7246, -7282);
    
    _putDontNest(result, -7209, -7318);
    
    _putDontNest(result, -7192, -7192);
    
    _putDontNest(result, -7139, -7254);
    
    _putDontNest(result, -7103, -7164);
    
    _putDontNest(result, -7086, -7147);
    
    _putDontNest(result, -7055, -7178);
    
    _putDontNest(result, -7017, -7072);
    
    _putDontNest(result, -6962, -7268);
    
    _putDontNest(result, -6942, -7192);
    
    _putDontNest(result, -6928, -7178);
    
    _putDontNest(result, -6874, -6996);
    
    _putDontNest(result, -6856, -6978);
    
    _putDontNest(result, -6793, -7178);
    
    _putDontNest(result, -6706, -7164);
    
    _putDontNest(result, -6706, -6373);
    
    _putDontNest(result, -6367, -7147);
    
    _putDontNest(result, -1102, -1146);
    
    _putDontNest(result, -1102, -1130);
    
    _putDontNest(result, -7164, -7192);
    
    _putDontNest(result, -7133, -7147);
    
    _putDontNest(result, -7123, -7318);
    
    _putDontNest(result, -7117, -7147);
    
    _putDontNest(result, -7055, -7086);
    
    _putDontNest(result, -7033, -7072);
    
    _putDontNest(result, -6968, -6996);
    
    _putDontNest(result, -6948, -7237);
    
    _putDontNest(result, -6942, -7086);
    
    _putDontNest(result, -6906, -7192);
    
    _putDontNest(result, -6889, -7041);
    
    _putDontNest(result, -6889, -6962);
    
    _putDontNest(result, -6889, -6914);
    
    _putDontNest(result, -6856, -6897);
    
    _putDontNest(result, -6620, -7164);
    
    _putDontNest(result, -6620, -6373);
    
    _putDontNest(result, -6513, -7192);
    
    _putDontNest(result, -6513, -7011);
    
    _putDontNest(result, -6513, -6883);
    
    _putDontNest(result, -6367, -6928);
    
    _putDontNest(result, -6345, -6942);
    
    _putDontNest(result, -181, -336);
    
    _putDontNest(result, -7223, -7223);
    
    _putDontNest(result, -7209, -7254);
    
    _putDontNest(result, -7156, -7296);
    
    _putDontNest(result, -7147, -7133);
    
    _putDontNest(result, -7147, -7117);
    
    _putDontNest(result, -7139, -7318);
    
    _putDontNest(result, -7078, -7254);
    
    _putDontNest(result, -7072, -7164);
    
    _putDontNest(result, -6988, -7055);
    
    _putDontNest(result, -6934, -7318);
    
    _putDontNest(result, -6928, -7147);
    
    _putDontNest(result, -6920, -6996);
    
    _putDontNest(result, -6889, -6978);
    
    _putDontNest(result, -6883, -7147);
    
    _putDontNest(result, -6874, -7192);
    
    _putDontNest(result, -6832, -7209);
    
    _putDontNest(result, -6793, -6978);
    
    _putDontNest(result, -6513, -7027);
    
    _putDontNest(result, -6367, -6847);
    
    _putDontNest(result, -6345, -7086);
    
    _putDontNest(result, -1592, -1582);
    
    _putDontNest(result, -457, -473);
    
    _putDontNest(result, -7608, -7911);
    
    _putDontNest(result, -7237, -7209);
    
    _putDontNest(result, -7109, -7237);
    
    _putDontNest(result, -7064, -7268);
    
    _putDontNest(result, -7027, -7254);
    
    _putDontNest(result, -7017, -7223);
    
    _putDontNest(result, -7017, -7103);
    
    _putDontNest(result, -7002, -7117);
    
    _putDontNest(result, -6934, -7041);
    
    _putDontNest(result, -6874, -7103);
    
    _putDontNest(result, -6856, -7254);
    
    _putDontNest(result, -6793, -6914);
    
    _putDontNest(result, -6642, -7254);
    
    _putDontNest(result, -6584, -6883);
    
    _putDontNest(result, -6564, -7268);
    
    _putDontNest(result, -6564, -7027);
    
    _putDontNest(result, -6527, -7254);
    
    _putDontNest(result, -6527, -7209);
    
    _putDontNest(result, -6345, -7133);
    
    _putDontNest(result, -6345, -6338);
    
    _putDontNest(result, -473, -473);
    
    _putDontNest(result, -457, -457);
    
    _putDontNest(result, -7675, -7869);
    
    _putDontNest(result, -7246, -7296);
    
    _putDontNest(result, -7117, -7164);
    
    _putDontNest(result, -7072, -7086);
    
    _putDontNest(result, -7033, -7223);
    
    _putDontNest(result, -7033, -7103);
    
    _putDontNest(result, -7011, -7254);
    
    _putDontNest(result, -7002, -7133);
    
    _putDontNest(result, -6942, -6942);
    
    _putDontNest(result, -6906, -7103);
    
    _putDontNest(result, -6793, -7103);
    
    _putDontNest(result, -6793, -7041);
    
    _putDontNest(result, -6793, -6962);
    
    _putDontNest(result, -6642, -7041);
    
    _putDontNest(result, -6620, -7147);
    
    _putDontNest(result, -6584, -7296);
    
    _putDontNest(result, -6564, -7011);
    
    _putDontNest(result, -6527, -7055);
    
    _putDontNest(result, -6527, -6962);
    
    _putDontNest(result, -6513, -7041);
    
    _putDontNest(result, -6345, -7117);
    
    _putDontNest(result, -1393, -1618);
    
    _putDontNest(result, -7147, -7192);
    
    _putDontNest(result, -7139, -7296);
    
    _putDontNest(result, -7133, -7164);
    
    _putDontNest(result, -7123, -7296);
    
    _putDontNest(result, -7078, -7237);
    
    _putDontNest(result, -7002, -7178);
    
    _putDontNest(result, -6996, -7296);
    
    _putDontNest(result, -6988, -7192);
    
    _putDontNest(result, -6962, -7282);
    
    _putDontNest(result, -6934, -7254);
    
    _putDontNest(result, -6906, -7055);
    
    _putDontNest(result, -6889, -7192);
    
    _putDontNest(result, -6874, -7055);
    
    _putDontNest(result, -6874, -6962);
    
    _putDontNest(result, -6856, -7318);
    
    _putDontNest(result, -6793, -7209);
    
    _putDontNest(result, -6642, -7318);
    
    _putDontNest(result, -6620, -6942);
    
    _putDontNest(result, -6527, -6914);
    
    _putDontNest(result, -6513, -7318);
    
    _putDontNest(result, -6367, -7178);
    
    _putDontNest(result, -1745, -6413);
    
    _putDontNest(result, -7911, -7911);
    
    _putDontNest(result, -7260, -7254);
    
    _putDontNest(result, -7184, -7282);
    
    _putDontNest(result, -7178, -7268);
    
    _putDontNest(result, -7072, -7147);
    
    _putDontNest(result, -7047, -7268);
    
    _putDontNest(result, -7041, -7282);
    
    _putDontNest(result, -7033, -7117);
    
    _putDontNest(result, -7017, -7133);
    
    _putDontNest(result, -7002, -7072);
    
    _putDontNest(result, -6988, -7103);
    
    _putDontNest(result, -6920, -7268);
    
    _putDontNest(result, -6920, -7027);
    
    _putDontNest(result, -6914, -7282);
    
    _putDontNest(result, -6897, -7296);
    
    _putDontNest(result, -6874, -6914);
    
    _putDontNest(result, -6856, -7041);
    
    _putDontNest(result, -6642, -6996);
    
    _putDontNest(result, -6620, -7086);
    
    _putDontNest(result, -6527, -6978);
    
    _putDontNest(result, -6513, -6996);
    
    _putDontNest(result, -6367, -7072);
    
    _putDontNest(result, -7807, -7855);
    
    _putDontNest(result, -7147, -7103);
    
    _putDontNest(result, -7109, -7254);
    
    _putDontNest(result, -7033, -7133);
    
    _putDontNest(result, -7017, -7117);
    
    _putDontNest(result, -7002, -7223);
    
    _putDontNest(result, -6988, -7209);
    
    _putDontNest(result, -6968, -7011);
    
    _putDontNest(result, -6948, -7296);
    
    _putDontNest(result, -6942, -7164);
    
    _putDontNest(result, -6920, -7011);
    
    _putDontNest(result, -6906, -6978);
    
    _putDontNest(result, -6889, -7103);
    
    _putDontNest(result, -6883, -7282);
    
    _putDontNest(result, -6856, -6996);
    
    _putDontNest(result, -6793, -7192);
    
    _putDontNest(result, -6642, -7237);
    
    _putDontNest(result, -6620, -6874);
    
    _putDontNest(result, -6513, -7237);
    
    _putDontNest(result, -6367, -7223);
    
    _putDontNest(result, -1592, -1552);
    
    _putDontNest(result, -1552, -1592);
    
    _putDontNest(result, -7847, -7911);
    
    _putDontNest(result, -7095, -7268);
    
    _putDontNest(result, -7078, -7318);
    
    _putDontNest(result, -6978, -7282);
    
    _putDontNest(result, -6968, -7268);
    
    _putDontNest(result, -6968, -7027);
    
    _putDontNest(result, -6889, -7209);
    
    _putDontNest(result, -6874, -6978);
    
    _putDontNest(result, -6847, -7164);
    
    _putDontNest(result, -6706, -7296);
   return result;
  }
    
  protected static IntegerMap _initDontNestGroups() {
    IntegerMap result = new IntegerMap();
    int resultStoreId = result.size();
    
    
    ++resultStoreId;
    
    result.putUnsafe(-3803, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(-7302, resultStoreId);
    result.putUnsafe(-7288, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(-7861, resultStoreId);
    result.putUnsafe(-7847, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(-7833, resultStoreId);
    result.putUnsafe(-7807, resultStoreId);
    result.putUnsafe(-7721, resultStoreId);
    result.putUnsafe(-7675, resultStoreId);
    result.putUnsafe(-7663, resultStoreId);
    result.putUnsafe(-7622, resultStoreId);
    result.putUnsafe(-7608, resultStoreId);
    result.putUnsafe(-7599, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(-6897, resultStoreId);
    result.putUnsafe(-6883, resultStoreId);
    result.putUnsafe(-6856, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(-1592, resultStoreId);
    result.putUnsafe(-1582, resultStoreId);
    result.putUnsafe(-1567, resultStoreId);
    result.putUnsafe(-1552, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(-1146, resultStoreId);
    result.putUnsafe(-1122, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(-7147, resultStoreId);
    result.putUnsafe(-7133, resultStoreId);
    result.putUnsafe(-7117, resultStoreId);
    result.putUnsafe(-7103, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(-6942, resultStoreId);
    result.putUnsafe(-6928, resultStoreId);
    result.putUnsafe(-6914, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(-6874, resultStoreId);
    result.putUnsafe(-6847, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(-7170, resultStoreId);
    result.putUnsafe(-7156, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(-7055, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(-6988, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(-6948, resultStoreId);
    result.putUnsafe(-6934, resultStoreId);
    result.putUnsafe(-6920, resultStoreId);
    result.putUnsafe(-6906, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(-181, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(-7011, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(-1836, resultStoreId);
    result.putUnsafe(-1792, resultStoreId);
    result.putUnsafe(-1695, resultStoreId);
    result.putUnsafe(-1488, resultStoreId);
    result.putUnsafe(-1393, resultStoreId);
    result.putUnsafe(-1376, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(-830, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(-7086, resultStoreId);
    result.putUnsafe(-7072, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(-7184, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(-6968, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(-6889, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(-6832, resultStoreId);
    result.putUnsafe(-6793, resultStoreId);
    result.putUnsafe(-6642, resultStoreId);
    result.putUnsafe(-6584, resultStoreId);
    result.putUnsafe(-6564, resultStoreId);
    result.putUnsafe(-6527, resultStoreId);
    result.putUnsafe(-6513, resultStoreId);
    result.putUnsafe(-6367, resultStoreId);
    result.putUnsafe(-6332, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(-5785, resultStoreId);
    result.putUnsafe(-2088, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(-7897, resultStoreId);
    result.putUnsafe(-7883, resultStoreId);
    result.putUnsafe(-7869, resultStoreId);
    result.putUnsafe(-7855, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(-1745, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(-1130, resultStoreId);
    result.putUnsafe(-1108, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(-7296, resultStoreId);
    result.putUnsafe(-7274, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(-7282, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(-7268, resultStoreId);
    result.putUnsafe(-7260, resultStoreId);
    result.putUnsafe(-7254, resultStoreId);
    result.putUnsafe(-7246, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(-7192, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(-7237, resultStoreId);
    result.putUnsafe(-7223, resultStoreId);
    result.putUnsafe(-7209, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(-7139, resultStoreId);
    result.putUnsafe(-7123, resultStoreId);
    result.putUnsafe(-7109, resultStoreId);
    result.putUnsafe(-7095, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(-7078, resultStoreId);
    result.putUnsafe(-7064, resultStoreId);
    result.putUnsafe(-7047, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(-6978, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(-7033, resultStoreId);
    result.putUnsafe(-7017, resultStoreId);
    result.putUnsafe(-7002, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(-2095, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(-7911, resultStoreId);
    result.putUnsafe(-7889, resultStoreId);
    result.putUnsafe(-7875, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(-473, resultStoreId);
    result.putUnsafe(-449, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(-6827, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(-7178, resultStoreId);
    result.putUnsafe(-7164, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(-457, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(-1116, resultStoreId);
    result.putUnsafe(-1102, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(-7041, resultStoreId);
    result.putUnsafe(-7027, resultStoreId);
    result.putUnsafe(-6996, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(-6962, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(-6706, resultStoreId);
    result.putUnsafe(-6620, resultStoreId);
    result.putUnsafe(-6345, resultStoreId);
      
    return result;
  }
  
  protected boolean hasNestingRestrictions(String name){
		return (_dontNest.size() != 0); // TODO Make more specific.
  }
    
  protected IntegerList getFilteredParents(int childId) {
		return _dontNest.get(childId);
  }
    
  // initialize priorities     
  static {
    _dontNest = _initDontNest();
    _resultStoreIdMappings = _initDontNestGroups();
  }
    
  // Production declarations
	
  private static final IConstructor prod__lit___45_61__char_class___range__45_45_char_class___range__61_61_ = (IConstructor) _read("prod(lit(\"-=\"),[\\char-class([range(45,45)]),\\char-class([range(61,61)])],{})", Factory.Production);
  private static final IConstructor prod__dateLiteral_$DateTimeLiteral__date_$JustDate_ = (IConstructor) _read("prod(label(\"dateLiteral\",sort(\"DateTimeLiteral\")),[label(\"date\",lex(\"JustDate\"))],{})", Factory.Production);
  private static final IConstructor prod__default_$ImportedModule__name_$QualifiedName_ = (IConstructor) _read("prod(label(\"default\",sort(\"ImportedModule\")),[label(\"name\",sort(\"QualifiedName\"))],{})", Factory.Production);
  private static final IConstructor prod__default_$LocationLiteral__protocolPart_$ProtocolPart_$layouts_LAYOUTLIST_pathPart_$PathPart_ = (IConstructor) _read("prod(label(\"default\",sort(\"LocationLiteral\")),[label(\"protocolPart\",sort(\"ProtocolPart\")),layouts(\"LAYOUTLIST\"),label(\"pathPart\",sort(\"PathPart\"))],{})", Factory.Production);
  private static final IConstructor prod__expression_$FunctionDeclaration__tags_$Tags_$layouts_LAYOUTLIST_visibility_$Visibility_$layouts_LAYOUTLIST_signature_$Signature_$layouts_LAYOUTLIST_lit___61_$layouts_LAYOUTLIST_expression_$Expression_$layouts_LAYOUTLIST_lit___59__tag__Foldable_tag__breakable___123_101_120_112_114_101_115_115_105_111_110_125 = (IConstructor) _read("prod(label(\"expression\",sort(\"FunctionDeclaration\")),[label(\"tags\",sort(\"Tags\")),layouts(\"LAYOUTLIST\"),label(\"visibility\",sort(\"Visibility\")),layouts(\"LAYOUTLIST\"),label(\"signature\",sort(\"Signature\")),layouts(\"LAYOUTLIST\"),lit(\"=\"),layouts(\"LAYOUTLIST\"),label(\"expression\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\";\")],{tag(Foldable()),tag(breakable(\"{expression}\"))})", Factory.Production);
  private static final IConstructor prod__expression_$Statement__expression_$Expression_$layouts_LAYOUTLIST_lit___59__tag__breakable = (IConstructor) _read("prod(label(\"expression\",sort(\"Statement\")),[label(\"expression\",conditional(sort(\"Expression\"),{except(\"visit\"),except(\"nonEmptyBlock\")})),layouts(\"LAYOUTLIST\"),lit(\";\")],{tag(breakable())})", Factory.Production);
  private static final IConstructor prod__lit_fail__char_class___range__102_102_char_class___range__97_97_char_class___range__105_105_char_class___range__108_108_ = (IConstructor) _read("prod(lit(\"fail\"),[\\char-class([range(102,102)]),\\char-class([range(97,97)]),\\char-class([range(105,105)]),\\char-class([range(108,108)])],{})", Factory.Production);
  private static final IConstructor prod__globalDirective_$Statement__lit_global_$layouts_LAYOUTLIST_type_$Type_$layouts_LAYOUTLIST_names_iter_seps__$QualifiedName__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___59__tag__breakable = (IConstructor) _read("prod(label(\"globalDirective\",sort(\"Statement\")),[lit(\"global\"),layouts(\"LAYOUTLIST\"),label(\"type\",sort(\"Type\")),layouts(\"LAYOUTLIST\"),label(\"names\",\\iter-seps(sort(\"QualifiedName\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\";\")],{tag(breakable())})", Factory.Production);
  private static final IConstructor prod__labeled_$DataTarget__label_$Name_$layouts_LAYOUTLIST_lit___58_ = (IConstructor) _read("prod(label(\"labeled\",sort(\"DataTarget\")),[label(\"label\",lex(\"Name\")),layouts(\"LAYOUTLIST\"),lit(\":\")],{})", Factory.Production);
  private static final IConstructor prod__listRelation_$BasicType__lit_lrel_ = (IConstructor) _read("prod(label(\"listRelation\",sort(\"BasicType\")),[lit(\"lrel\")],{})", Factory.Production);
  private static final IConstructor prod__match_$Expression__pattern_$Pattern_$layouts_LAYOUTLIST_lit___58_61_$layouts_LAYOUTLIST_expression_$Expression__assoc__non_assoc = (IConstructor) _read("prod(label(\"match\",sort(\"Expression\")),[label(\"pattern\",sort(\"Pattern\")),layouts(\"LAYOUTLIST\"),lit(\":=\"),layouts(\"LAYOUTLIST\"),label(\"expression\",sort(\"Expression\"))],{assoc(\\non-assoc())})", Factory.Production);
  private static final IConstructor prod__midTemplate_$StringTail__mid_$MidStringChars_$layouts_LAYOUTLIST_template_$StringTemplate_$layouts_LAYOUTLIST_tail_$StringTail_ = (IConstructor) _read("prod(label(\"midTemplate\",sort(\"StringTail\")),[label(\"mid\",lex(\"MidStringChars\")),layouts(\"LAYOUTLIST\"),label(\"template\",sort(\"StringTemplate\")),layouts(\"LAYOUTLIST\"),label(\"tail\",sort(\"StringTail\"))],{})", Factory.Production);
  private static final IConstructor prod__negative_$Pattern__lit___$layouts_LAYOUTLIST_argument_$Pattern_ = (IConstructor) _read("prod(label(\"negative\",sort(\"Pattern\")),[lit(\"-\"),layouts(\"LAYOUTLIST\"),label(\"argument\",sort(\"Pattern\"))],{})", Factory.Production);
  private static final IConstructor prod__noThrows_$Signature__modifiers_$FunctionModifiers_$layouts_LAYOUTLIST_type_$Type_$layouts_LAYOUTLIST_name_$Name_$layouts_LAYOUTLIST_parameters_$Parameters_ = (IConstructor) _read("prod(label(\"noThrows\",sort(\"Signature\")),[label(\"modifiers\",sort(\"FunctionModifiers\")),layouts(\"LAYOUTLIST\"),label(\"type\",sort(\"Type\")),layouts(\"LAYOUTLIST\"),label(\"name\",lex(\"Name\")),layouts(\"LAYOUTLIST\"),label(\"parameters\",sort(\"Parameters\"))],{})", Factory.Production);
  private static final IConstructor prod__one_$ConcreteHole__lit___60_$layouts_LAYOUTLIST_hole_$Pattern_$layouts_LAYOUTLIST_lit___62_ = (IConstructor) _read("prod(label(\"one\",sort(\"ConcreteHole\")),[lit(\"\\<\"),layouts(\"LAYOUTLIST\"),label(\"hole\",sort(\"Pattern\")),layouts(\"LAYOUTLIST\"),lit(\"\\>\")],{})", Factory.Production);
  private static final IConstructor prod__parametric_$UserType__name_$QualifiedName_$layouts_LAYOUTLIST_lit___91_$layouts_LAYOUTLIST_parameters_iter_seps__$Type__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___93_ = (IConstructor) _read("prod(label(\"parametric\",sort(\"UserType\")),[conditional(label(\"name\",sort(\"QualifiedName\")),{follow(lit(\"[\"))}),layouts(\"LAYOUTLIST\"),lit(\"[\"),layouts(\"LAYOUTLIST\"),label(\"parameters\",\\iter-seps(sort(\"Type\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\"]\")],{})", Factory.Production);
  private static final IConstructor prod__shell_$Command__lit___58_$layouts_LAYOUTLIST_command_$ShellCommand_ = (IConstructor) _read("prod(label(\"shell\",sort(\"Command\")),[lit(\":\"),layouts(\"LAYOUTLIST\"),label(\"command\",sort(\"ShellCommand\"))],{})", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_assoc_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"assoc\")],{})", Factory.Production);
  private static final IConstructor prod__$RegExpLiteral__lit___47_iter_star__$RegExp_lit___47_$RegExpModifier_ = (IConstructor) _read("prod(lex(\"RegExpLiteral\"),[lit(\"/\"),\\iter-star(lex(\"RegExp\")),lit(\"/\"),lex(\"RegExpModifier\")],{})", Factory.Production);
  private static final IConstructor regular__iter_star__$LAYOUT = (IConstructor) _read("regular(\\iter-star(lex(\"LAYOUT\")))", Factory.Production);
  private static final IConstructor regular__iter_seps__$QualifiedName__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST = (IConstructor) _read("regular(\\iter-seps(sort(\"QualifiedName\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")]))", Factory.Production);
  private static final IConstructor regular__iter_star__char_class___range__48_57 = (IConstructor) _read("regular(\\iter-star(\\char-class([range(48,57)])))", Factory.Production);
  private static final IConstructor prod__timeLiteral_$DateTimeLiteral__time_$JustTime_ = (IConstructor) _read("prod(label(\"timeLiteral\",sort(\"DateTimeLiteral\")),[label(\"time\",lex(\"JustTime\"))],{})", Factory.Production);
  private static final IConstructor regular__opt__char_class___range__48_57 = (IConstructor) _read("regular(opt(\\char-class([range(48,57)])))", Factory.Production);
  private static final IConstructor prod__lit___47_61__char_class___range__47_47_char_class___range__61_61_ = (IConstructor) _read("prod(lit(\"/=\"),[\\char-class([range(47,47)]),\\char-class([range(61,61)])],{})", Factory.Production);
  private static final IConstructor prod__alias_$Kind__lit_alias_ = (IConstructor) _read("prod(label(\"alias\",sort(\"Kind\")),[lit(\"alias\")],{})", Factory.Production);
  private static final IConstructor prod__lit_case__char_class___range__99_99_char_class___range__97_97_char_class___range__115_115_char_class___range__101_101_ = (IConstructor) _read("prod(lit(\"case\"),[\\char-class([range(99,99)]),\\char-class([range(97,97)]),\\char-class([range(115,115)]),\\char-class([range(101,101)])],{})", Factory.Production);
  private static final IConstructor prod__lit_catch__char_class___range__99_99_char_class___range__97_97_char_class___range__116_116_char_class___range__99_99_char_class___range__104_104_ = (IConstructor) _read("prod(lit(\"catch\"),[\\char-class([range(99,99)]),\\char-class([range(97,97)]),\\char-class([range(116,116)]),\\char-class([range(99,99)]),\\char-class([range(104,104)])],{})", Factory.Production);
  private static final IConstructor prod__dataAbstract_$Declaration__tags_$Tags_$layouts_LAYOUTLIST_visibility_$Visibility_$layouts_LAYOUTLIST_lit_data_$layouts_LAYOUTLIST_user_$UserType_$layouts_LAYOUTLIST_lit___59_ = (IConstructor) _read("prod(label(\"dataAbstract\",sort(\"Declaration\")),[label(\"tags\",sort(\"Tags\")),layouts(\"LAYOUTLIST\"),label(\"visibility\",sort(\"Visibility\")),layouts(\"LAYOUTLIST\"),lit(\"data\"),layouts(\"LAYOUTLIST\"),label(\"user\",sort(\"UserType\")),layouts(\"LAYOUTLIST\"),lit(\";\")],{})", Factory.Production);
  private static final IConstructor prod__dateTime_$Literal__dateTimeLiteral_$DateTimeLiteral_ = (IConstructor) _read("prod(label(\"dateTime\",sort(\"Literal\")),[label(\"dateTimeLiteral\",sort(\"DateTimeLiteral\"))],{})", Factory.Production);
  private static final IConstructor prod__default_$Declarator__type_$Type_$layouts_LAYOUTLIST_variables_iter_seps__$Variable__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_ = (IConstructor) _read("prod(label(\"default\",sort(\"Declarator\")),[label(\"type\",sort(\"Type\")),layouts(\"LAYOUTLIST\"),label(\"variables\",\\iter-seps(sort(\"Variable\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")]))],{})", Factory.Production);
  private static final IConstructor prod__division_$Assignment__lit___47_61_ = (IConstructor) _read("prod(label(\"division\",sort(\"Assignment\")),[lit(\"/=\")],{})", Factory.Production);
  private static final IConstructor prod__filter_$Statement__lit_filter_$layouts_LAYOUTLIST_lit___59__tag__breakable = (IConstructor) _read("prod(label(\"filter\",sort(\"Statement\")),[lit(\"filter\"),layouts(\"LAYOUTLIST\"),lit(\";\")],{tag(breakable())})", Factory.Production);
  private static final IConstructor prod__lit_function__char_class___range__102_102_char_class___range__117_117_char_class___range__110_110_char_class___range__99_99_char_class___range__116_116_char_class___range__105_105_char_class___range__111_111_char_class___range__110_110_ = (IConstructor) _read("prod(lit(\"function\"),[\\char-class([range(102,102)]),\\char-class([range(117,117)]),\\char-class([range(110,110)]),\\char-class([range(99,99)]),\\char-class([range(116,116)]),\\char-class([range(105,105)]),\\char-class([range(111,111)]),\\char-class([range(110,110)])],{})", Factory.Production);
  private static final IConstructor prod__lit_int__char_class___range__105_105_char_class___range__110_110_char_class___range__116_116_ = (IConstructor) _read("prod(lit(\"int\"),[\\char-class([range(105,105)]),\\char-class([range(110,110)]),\\char-class([range(116,116)])],{})", Factory.Production);
  private static final IConstructor prod__lessThanOrEq_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___60_61_$layouts_LAYOUTLIST_rhs_$Expression__assoc__non_assoc = (IConstructor) _read("prod(label(\"lessThanOrEq\",sort(\"Expression\")),[label(\"lhs\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"\\<=\"),layouts(\"LAYOUTLIST\"),label(\"rhs\",sort(\"Expression\"))],{assoc(\\non-assoc())})", Factory.Production);
  private static final IConstructor prod__list_$Expression__lit___91_$layouts_LAYOUTLIST_elements_iter_star_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___93_ = (IConstructor) _read("prod(label(\"list\",sort(\"Expression\")),[lit(\"[\"),layouts(\"LAYOUTLIST\"),label(\"elements\",\\iter-star-seps(sort(\"Expression\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\"]\")],{})", Factory.Production);
  private static final IConstructor prod__lit_modules__char_class___range__109_109_char_class___range__111_111_char_class___range__100_100_char_class___range__117_117_char_class___range__108_108_char_class___range__101_101_char_class___range__115_115_ = (IConstructor) _read("prod(lit(\"modules\"),[\\char-class([range(109,109)]),\\char-class([range(111,111)]),\\char-class([range(100,100)]),\\char-class([range(117,117)]),\\char-class([range(108,108)]),\\char-class([range(101,101)]),\\char-class([range(115,115)])],{})", Factory.Production);
  private static final IConstructor prod__name_$Field__fieldName_$Name_ = (IConstructor) _read("prod(label(\"name\",sort(\"Field\")),[label(\"fieldName\",lex(\"Name\"))],{})", Factory.Production);
  private static final IConstructor prod__topDownBreak_$Strategy__lit_top_down_break_ = (IConstructor) _read("prod(label(\"topDownBreak\",sort(\"Strategy\")),[lit(\"top-down-break\")],{})", Factory.Production);
  private static final IConstructor prod__lit_type__char_class___range__116_116_char_class___range__121_121_char_class___range__112_112_char_class___range__101_101_ = (IConstructor) _read("prod(lit(\"type\"),[\\char-class([range(116,116)]),\\char-class([range(121,121)]),\\char-class([range(112,112)]),\\char-class([range(101,101)])],{})", Factory.Production);
  private static final IConstructor prod__lit_when__char_class___range__119_119_char_class___range__104_104_char_class___range__101_101_char_class___range__110_110_ = (IConstructor) _read("prod(lit(\"when\"),[\\char-class([range(119,119)]),\\char-class([range(104,104)]),\\char-class([range(101,101)]),\\char-class([range(110,110)])],{})", Factory.Production);
  private static final IConstructor prod__$JustTime__lit___36_84_$TimePartNoTZ_$TimeZonePart_ = (IConstructor) _read("prod(lex(\"JustTime\"),[lit(\"$T\"),lex(\"TimePartNoTZ\"),lex(\"TimeZonePart\")],{})", Factory.Production);
  private static final IConstructor prod__$Name__seq___char_class___range__65_90_range__95_95_range__97_122_iter_star__char_class___range__48_57_range__65_90_range__95_95_range__97_122_ = (IConstructor) _read("prod(lex(\"Name\"),[conditional(seq([conditional(\\char-class([range(65,90),range(95,95),range(97,122)]),{\\not-precede(\\char-class([range(65,90),range(95,95),range(97,122)]))}),conditional(\\iter-star(\\char-class([range(48,57),range(65,90),range(95,95),range(97,122)])),{\\not-follow(\\char-class([range(48,57),range(65,90),range(95,95),range(97,122)]))})]),{delete(keywords(\"RascalKeywords\"))})],{})", Factory.Production);
  private static final IConstructor regular__iter_star_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST = (IConstructor) _read("regular(\\iter-star-seps(sort(\"Expression\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")]))", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_dynamic_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"dynamic\")],{})", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_data_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"data\")],{})", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_catch_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"catch\")],{})", Factory.Production);
  private static final IConstructor regular__seq___char_class___range__44_44_range__46_46_char_class___range__48_57_opt__seq___char_class___range__48_57_opt__char_class___range__48_57 = (IConstructor) _read("regular(seq([\\char-class([range(44,44),range(46,46)]),\\char-class([range(48,57)]),opt(seq([\\char-class([range(48,57)]),opt(\\char-class([range(48,57)]))]))]))", Factory.Production);
  private static final IConstructor prod__quit_$ShellCommand__lit_quit_ = (IConstructor) _read("prod(label(\"quit\",sort(\"ShellCommand\")),[lit(\"quit\")],{})", Factory.Production);
  private static final IConstructor regular__iter_seps__$TypeVar__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST = (IConstructor) _read("regular(\\iter-seps(sort(\"TypeVar\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")]))", Factory.Production);
  private static final IConstructor prod__lit___47_47__char_class___range__47_47_char_class___range__47_47_ = (IConstructor) _read("prod(lit(\"//\"),[\\char-class([range(47,47)]),\\char-class([range(47,47)])],{})", Factory.Production);
  private static final IConstructor prod__lit___58_47_47__char_class___range__58_58_char_class___range__47_47_char_class___range__47_47_ = (IConstructor) _read("prod(lit(\"://\"),[\\char-class([range(58,58)]),\\char-class([range(47,47)]),\\char-class([range(47,47)])],{})", Factory.Production);
  private static final IConstructor prod__conditional_$FunctionDeclaration__tags_$Tags_$layouts_LAYOUTLIST_visibility_$Visibility_$layouts_LAYOUTLIST_signature_$Signature_$layouts_LAYOUTLIST_lit___61_$layouts_LAYOUTLIST_expression_$Expression_$layouts_LAYOUTLIST_lit_when_$layouts_LAYOUTLIST_conditions_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___59__tag__Foldable_tag__breakable___123_101_120_112_114_101_115_115_105_111_110_44_99_111_110_100_105_116_105_111_110_115_125 = (IConstructor) _read("prod(label(\"conditional\",sort(\"FunctionDeclaration\")),[label(\"tags\",sort(\"Tags\")),layouts(\"LAYOUTLIST\"),label(\"visibility\",sort(\"Visibility\")),layouts(\"LAYOUTLIST\"),label(\"signature\",sort(\"Signature\")),layouts(\"LAYOUTLIST\"),lit(\"=\"),layouts(\"LAYOUTLIST\"),label(\"expression\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"when\"),layouts(\"LAYOUTLIST\"),label(\"conditions\",\\iter-seps(sort(\"Expression\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\";\")],{tag(Foldable()),tag(breakable(\"{expression,conditions}\"))})", Factory.Production);
  private static final IConstructor prod__dateTime_$BasicType__lit_datetime_ = (IConstructor) _read("prod(label(\"dateTime\",sort(\"BasicType\")),[lit(\"datetime\")],{})", Factory.Production);
  private static final IConstructor prod__default_$QualifiedName__names_iter_seps__$Name__$layouts_LAYOUTLIST_lit___58_58_$layouts_LAYOUTLIST_ = (IConstructor) _read("prod(label(\"default\",sort(\"QualifiedName\")),[conditional(label(\"names\",\\iter-seps(lex(\"Name\"),[layouts(\"LAYOUTLIST\"),lit(\"::\"),layouts(\"LAYOUTLIST\")])),{\\not-follow(lit(\"::\"))})],{})", Factory.Production);
  private static final IConstructor prod__default_$TypeArg__type_$Type_ = (IConstructor) _read("prod(label(\"default\",sort(\"TypeArg\")),[label(\"type\",sort(\"Type\"))],{})", Factory.Production);
  private static final IConstructor prod__dynamic_$LocalVariableDeclaration__lit_dynamic_$layouts_LAYOUTLIST_declarator_$Declarator_ = (IConstructor) _read("prod(label(\"dynamic\",sort(\"LocalVariableDeclaration\")),[lit(\"dynamic\"),layouts(\"LAYOUTLIST\"),label(\"declarator\",sort(\"Declarator\"))],{})", Factory.Production);
  private static final IConstructor prod__equivalence_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___60_61_61_62_$layouts_LAYOUTLIST_rhs_$Expression__assoc__non_assoc = (IConstructor) _read("prod(label(\"equivalence\",sort(\"Expression\")),[label(\"lhs\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"\\<==\\>\"),layouts(\"LAYOUTLIST\"),label(\"rhs\",sort(\"Expression\"))],{assoc(\\non-assoc())})", Factory.Production);
  private static final IConstructor prod__lit_help__char_class___range__104_104_char_class___range__101_101_char_class___range__108_108_char_class___range__112_112_ = (IConstructor) _read("prod(lit(\"help\"),[\\char-class([range(104,104)]),\\char-class([range(101,101)]),\\char-class([range(108,108)]),\\char-class([range(112,112)])],{})", Factory.Production);
  private static final IConstructor prod__lit_num__char_class___range__110_110_char_class___range__117_117_char_class___range__109_109_ = (IConstructor) _read("prod(lit(\"num\"),[\\char-class([range(110,110)]),\\char-class([range(117,117)]),\\char-class([range(109,109)])],{})", Factory.Production);
  private static final IConstructor prod__structured_$Type__structured_$StructuredType_ = (IConstructor) _read("prod(label(\"structured\",sort(\"Type\")),[label(\"structured\",sort(\"StructuredType\"))],{})", Factory.Production);
  private static final IConstructor prod__lit_tag__char_class___range__116_116_char_class___range__97_97_char_class___range__103_103_ = (IConstructor) _read("prod(lit(\"tag\"),[\\char-class([range(116,116)]),\\char-class([range(97,97)]),\\char-class([range(103,103)])],{})", Factory.Production);
  private static final IConstructor regular__iter__char_class___range__48_55 = (IConstructor) _read("regular(iter(\\char-class([range(48,55)])))", Factory.Production);
  private static final IConstructor regular__iter__char_class___range__48_57 = (IConstructor) _read("regular(iter(\\char-class([range(48,57)])))", Factory.Production);
  private static final IConstructor prod__template_$StringLiteral__pre_$PreStringChars_$layouts_LAYOUTLIST_template_$StringTemplate_$layouts_LAYOUTLIST_tail_$StringTail_ = (IConstructor) _read("prod(label(\"template\",sort(\"StringLiteral\")),[label(\"pre\",lex(\"PreStringChars\")),layouts(\"LAYOUTLIST\"),label(\"template\",sort(\"StringTemplate\")),layouts(\"LAYOUTLIST\"),label(\"tail\",sort(\"StringTail\"))],{})", Factory.Production);
  private static final IConstructor prod__$JustDate__lit___36_$DatePart_ = (IConstructor) _read("prod(lex(\"JustDate\"),[lit(\"$\"),lex(\"DatePart\")],{})", Factory.Production);
  private static final IConstructor prod__$PreStringChars__char_class___range__34_34_iter_star__$StringCharacter_char_class___range__60_60__tag__category___67_111_110_115_116_97_110_116 = (IConstructor) _read("prod(lex(\"PreStringChars\"),[\\char-class([range(34,34)]),\\iter-star(lex(\"StringCharacter\")),\\char-class([range(60,60)])],{tag(category(\"Constant\"))})", Factory.Production);
  private static final IConstructor regular__iter_star_seps__$Import__$layouts_LAYOUTLIST = (IConstructor) _read("regular(\\iter-star-seps(sort(\"Import\"),[layouts(\"LAYOUTLIST\")]))", Factory.Production);
  private static final IConstructor regular__iter_star_seps__$Mapping__$Pattern__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST = (IConstructor) _read("regular(\\iter-star-seps(\\parameterized-sort(\"Mapping\",[sort(\"Pattern\")]),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")]))", Factory.Production);
  private static final IConstructor prod__negative_$Expression__lit___$layouts_LAYOUTLIST_argument_$Expression_ = (IConstructor) _read("prod(label(\"negative\",sort(\"Expression\")),[lit(\"-\"),layouts(\"LAYOUTLIST\"),label(\"argument\",sort(\"Expression\"))],{})", Factory.Production);
  private static final IConstructor prod__reifiedType_$Expression__lit_type_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_symbol_$Expression_$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_definitions_$Expression_$layouts_LAYOUTLIST_lit___41_ = (IConstructor) _read("prod(label(\"reifiedType\",sort(\"Expression\")),[lit(\"type\"),layouts(\"LAYOUTLIST\"),lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"symbol\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\"),label(\"definitions\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\")\")],{})", Factory.Production);
  private static final IConstructor prod__assignment_$Statement__assignable_$Assignable_$layouts_LAYOUTLIST_operator_$Assignment_$layouts_LAYOUTLIST_statement_$Statement__tag__breakable = (IConstructor) _read("prod(label(\"assignment\",sort(\"Statement\")),[label(\"assignable\",sort(\"Assignable\")),layouts(\"LAYOUTLIST\"),label(\"operator\",sort(\"Assignment\")),layouts(\"LAYOUTLIST\"),label(\"statement\",conditional(sort(\"Statement\"),{except(\"functionDeclaration\"),except(\"variableDeclaration\")}))],{tag(breakable())})", Factory.Production);
  private static final IConstructor prod__while_$Statement__label_$Label_$layouts_LAYOUTLIST_lit_while_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_conditions_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_$layouts_LAYOUTLIST_body_$Statement__tag__breakable = (IConstructor) _read("prod(label(\"while\",sort(\"Statement\")),[label(\"label\",sort(\"Label\")),layouts(\"LAYOUTLIST\"),lit(\"while\"),layouts(\"LAYOUTLIST\"),lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"conditions\",\\iter-seps(sort(\"Expression\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\")\"),layouts(\"LAYOUTLIST\"),label(\"body\",conditional(sort(\"Statement\"),{except(\"functionDeclaration\"),except(\"variableDeclaration\")}))],{tag(breakable())})", Factory.Production);
  private static final IConstructor regular__iter_star__char_class___range__100_100_range__105_105_range__109_109_range__115_115 = (IConstructor) _read("regular(\\iter-star(\\char-class([range(100,100),range(105,105),range(109,109),range(115,115)])))", Factory.Production);
  private static final IConstructor prod__lit___33_62_62__char_class___range__33_33_char_class___range__62_62_char_class___range__62_62_ = (IConstructor) _read("prod(lit(\"!\\>\\>\"),[\\char-class([range(33,33)]),\\char-class([range(62,62)]),\\char-class([range(62,62)])],{})", Factory.Production);
  private static final IConstructor prod__lit___46_46__char_class___range__46_46_char_class___range__46_46_ = (IConstructor) _read("prod(lit(\"..\"),[\\char-class([range(46,46)]),\\char-class([range(46,46)])],{})", Factory.Production);
  private static final IConstructor prod__lit___47_42__char_class___range__47_47_char_class___range__42_42_ = (IConstructor) _read("prod(lit(\"/*\"),[\\char-class([range(47,47)]),\\char-class([range(42,42)])],{})", Factory.Production);
  private static final IConstructor prod__ascii_$UnicodeEscape__lit___92_char_class___range__97_97_char_class___range__48_55_char_class___range__48_57_range__65_70_range__97_102_ = (IConstructor) _read("prod(label(\"ascii\",lex(\"UnicodeEscape\")),[lit(\"\\\\\"),\\char-class([range(97,97)]),\\char-class([range(48,55)]),\\char-class([range(48,57),range(65,70),range(97,102)])],{})", Factory.Production);
  private static final IConstructor prod__basic_$Type__basic_$BasicType_ = (IConstructor) _read("prod(label(\"basic\",sort(\"Type\")),[label(\"basic\",sort(\"BasicType\"))],{})", Factory.Production);
  private static final IConstructor prod__default_$Renaming__from_$Name_$layouts_LAYOUTLIST_lit___61_62_$layouts_LAYOUTLIST_to_$Name_ = (IConstructor) _read("prod(label(\"default\",sort(\"Renaming\")),[label(\"from\",lex(\"Name\")),layouts(\"LAYOUTLIST\"),lit(\"=\\>\"),layouts(\"LAYOUTLIST\"),label(\"to\",lex(\"Name\"))],{})", Factory.Production);
  private static final IConstructor prod__equals_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___61_61_$layouts_LAYOUTLIST_rhs_$Expression__assoc__non_assoc = (IConstructor) _read("prod(label(\"equals\",sort(\"Expression\")),[label(\"lhs\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"==\"),layouts(\"LAYOUTLIST\"),label(\"rhs\",sort(\"Expression\"))],{assoc(\\non-assoc())})", Factory.Production);
  private static final IConstructor prod__fieldUpdate_$Expression__expression_$Expression_$layouts_LAYOUTLIST_lit___91_$layouts_LAYOUTLIST_key_$Name_$layouts_LAYOUTLIST_lit___61_$layouts_LAYOUTLIST_replacement_$Expression_$layouts_LAYOUTLIST_lit___93_ = (IConstructor) _read("prod(label(\"fieldUpdate\",sort(\"Expression\")),[label(\"expression\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"[\"),layouts(\"LAYOUTLIST\"),label(\"key\",lex(\"Name\")),layouts(\"LAYOUTLIST\"),lit(\"=\"),layouts(\"LAYOUTLIST\"),label(\"replacement\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"]\")],{})", Factory.Production);
  private static final IConstructor prod__lit_is__char_class___range__105_105_char_class___range__115_115_ = (IConstructor) _read("prod(lit(\"is\"),[\\char-class([range(105,105)]),\\char-class([range(115,115)])],{})", Factory.Production);
  private static final IConstructor prod__loc_$BasicType__lit_loc_ = (IConstructor) _read("prod(label(\"loc\",sort(\"BasicType\")),[lit(\"loc\")],{})", Factory.Production);
  private static final IConstructor prod__mid_$PathTail__mid_$MidPathChars_$layouts_LAYOUTLIST_expression_$Expression_$layouts_LAYOUTLIST_tail_$PathTail_ = (IConstructor) _read("prod(label(\"mid\",sort(\"PathTail\")),[label(\"mid\",lex(\"MidPathChars\")),layouts(\"LAYOUTLIST\"),label(\"expression\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),label(\"tail\",sort(\"PathTail\"))],{})", Factory.Production);
  private static final IConstructor prod__lit_on__char_class___range__111_111_char_class___range__110_110_ = (IConstructor) _read("prod(lit(\"on\"),[\\char-class([range(111,111)]),\\char-class([range(110,110)])],{})", Factory.Production);
  private static final IConstructor prod__product_$Assignment__lit___42_61_ = (IConstructor) _read("prod(label(\"product\",sort(\"Assignment\")),[lit(\"*=\")],{})", Factory.Production);
  private static final IConstructor prod__renamings_$ImportedModule__name_$QualifiedName_$layouts_LAYOUTLIST_renamings_$Renamings_ = (IConstructor) _read("prod(label(\"renamings\",sort(\"ImportedModule\")),[label(\"name\",sort(\"QualifiedName\")),layouts(\"LAYOUTLIST\"),label(\"renamings\",sort(\"Renamings\"))],{})", Factory.Production);
  private static final IConstructor prod__right_$Assoc__lit_right_ = (IConstructor) _read("prod(label(\"right\",sort(\"Assoc\")),[lit(\"right\")],{})", Factory.Production);
  private static final IConstructor prod__lit_throw__char_class___range__116_116_char_class___range__104_104_char_class___range__114_114_char_class___range__111_111_char_class___range__119_119_ = (IConstructor) _read("prod(lit(\"throw\"),[\\char-class([range(116,116)]),\\char-class([range(104,104)]),\\char-class([range(114,114)]),\\char-class([range(111,111)]),\\char-class([range(119,119)])],{})", Factory.Production);
  private static final IConstructor prod__tuple_$Assignable__lit___60_$layouts_LAYOUTLIST_elements_iter_seps__$Assignable__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___62_ = (IConstructor) _read("prod(label(\"tuple\",sort(\"Assignable\")),[lit(\"\\<\"),layouts(\"LAYOUTLIST\"),label(\"elements\",\\iter-seps(sort(\"Assignable\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\"\\>\")],{})", Factory.Production);
  private static final IConstructor prod__undeclare_$ShellCommand__lit_undeclare_$layouts_LAYOUTLIST_name_$QualifiedName_ = (IConstructor) _read("prod(label(\"undeclare\",sort(\"ShellCommand\")),[lit(\"undeclare\"),layouts(\"LAYOUTLIST\"),label(\"name\",sort(\"QualifiedName\"))],{})", Factory.Production);
  private static final IConstructor prod__variable_$Type__typeVar_$TypeVar_ = (IConstructor) _read("prod(label(\"variable\",sort(\"Type\")),[label(\"typeVar\",sort(\"TypeVar\"))],{})", Factory.Production);
  private static final IConstructor prod__$DecimalIntegerLiteral__lit_0_ = (IConstructor) _read("prod(lex(\"DecimalIntegerLiteral\"),[conditional(lit(\"0\"),{\\not-follow(\\char-class([range(48,57),range(65,90),range(95,95),range(97,122)]))})],{})", Factory.Production);
  private static final IConstructor prod__$StringCharacter__$UnicodeEscape_ = (IConstructor) _read("prod(lex(\"StringCharacter\"),[lex(\"UnicodeEscape\")],{})", Factory.Production);
  private static final IConstructor prod__lit___10__char_class___range__10_10_ = (IConstructor) _read("prod(lit(\"\\n\"),[\\char-class([range(10,10)])],{})", Factory.Production);
  private static final IConstructor prod__lit___58__char_class___range__58_58_ = (IConstructor) _read("prod(lit(\":\"),[\\char-class([range(58,58)])],{})", Factory.Production);
  private static final IConstructor prod__lit___59__char_class___range__59_59_ = (IConstructor) _read("prod(lit(\";\"),[\\char-class([range(59,59)])],{})", Factory.Production);
  private static final IConstructor prod__append_$Statement__lit_append_$layouts_LAYOUTLIST_dataTarget_$DataTarget_$layouts_LAYOUTLIST_statement_$Statement__assoc__non_assoc_tag__breakable = (IConstructor) _read("prod(label(\"append\",sort(\"Statement\")),[lit(\"append\"),layouts(\"LAYOUTLIST\"),label(\"dataTarget\",sort(\"DataTarget\")),layouts(\"LAYOUTLIST\"),label(\"statement\",conditional(sort(\"Statement\"),{except(\"functionDeclaration\"),except(\"variableDeclaration\")}))],{assoc(\\non-assoc()),tag(breakable())})", Factory.Production);
  private static final IConstructor prod__declaration_$EvalCommand__declaration_$Declaration_ = (IConstructor) _read("prod(label(\"declaration\",sort(\"EvalCommand\")),[label(\"declaration\",sort(\"Declaration\"))],{})", Factory.Production);
  private static final IConstructor prod__lit_declarations__char_class___range__100_100_char_class___range__101_101_char_class___range__99_99_char_class___range__108_108_char_class___range__97_97_char_class___range__114_114_char_class___range__97_97_char_class___range__116_116_char_class___range__105_105_char_class___range__111_111_char_class___range__110_110_char_class___range__115_115_ = (IConstructor) _read("prod(lit(\"declarations\"),[\\char-class([range(100,100)]),\\char-class([range(101,101)]),\\char-class([range(99,99)]),\\char-class([range(108,108)]),\\char-class([range(97,97)]),\\char-class([range(114,114)]),\\char-class([range(97,97)]),\\char-class([range(116,116)]),\\char-class([range(105,105)]),\\char-class([range(111,111)]),\\char-class([range(110,110)]),\\char-class([range(115,115)])],{})", Factory.Production);
  private static final IConstructor prod__default_$Parameters__lit___40_$layouts_LAYOUTLIST_formals_$Formals_$layouts_LAYOUTLIST_lit___41_ = (IConstructor) _read("prod(label(\"default\",sort(\"Parameters\")),[lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"formals\",sort(\"Formals\")),layouts(\"LAYOUTLIST\"),lit(\")\")],{})", Factory.Production);
  private static final IConstructor prod__default_$Tags__tags_iter_star_seps__$Tag__$layouts_LAYOUTLIST_ = (IConstructor) _read("prod(label(\"default\",sort(\"Tags\")),[label(\"tags\",\\iter-star-seps(sort(\"Tag\"),[layouts(\"LAYOUTLIST\")]))],{})", Factory.Production);
  private static final IConstructor prod__ifThenElse_$Statement__label_$Label_$layouts_LAYOUTLIST_lit_if_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_conditions_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_$layouts_LAYOUTLIST_thenStatement_$Statement_$layouts_LAYOUTLIST_lit_else_$layouts_LAYOUTLIST_elseStatement_$Statement__tag__breakable = (IConstructor) _read("prod(label(\"ifThenElse\",sort(\"Statement\")),[label(\"label\",sort(\"Label\")),layouts(\"LAYOUTLIST\"),lit(\"if\"),layouts(\"LAYOUTLIST\"),lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"conditions\",\\iter-seps(sort(\"Expression\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\")\"),layouts(\"LAYOUTLIST\"),label(\"thenStatement\",sort(\"Statement\")),layouts(\"LAYOUTLIST\"),lit(\"else\"),layouts(\"LAYOUTLIST\"),label(\"elseStatement\",conditional(sort(\"Statement\"),{except(\"functionDeclaration\"),except(\"variableDeclaration\")}))],{tag(breakable())})", Factory.Production);
  private static final IConstructor prod__lit_in__char_class___range__105_105_char_class___range__110_110_ = (IConstructor) _read("prod(lit(\"in\"),[\\char-class([range(105,105)]),\\char-class([range(110,110)])],{})", Factory.Production);
  private static final IConstructor prod__insertBefore_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___62_62_$layouts_LAYOUTLIST_rhs_$Expression__assoc__left = (IConstructor) _read("prod(label(\"insertBefore\",sort(\"Expression\")),[label(\"lhs\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"\\>\\>\"),layouts(\"LAYOUTLIST\"),label(\"rhs\",sort(\"Expression\"))],{assoc(left())})", Factory.Production);
  private static final IConstructor prod__integer_$Literal__integerLiteral_$IntegerLiteral_ = (IConstructor) _read("prod(label(\"integer\",sort(\"Literal\")),[label(\"integerLiteral\",sort(\"IntegerLiteral\"))],{})", Factory.Production);
  private static final IConstructor prod__lit_it__char_class___range__105_105_char_class___range__116_116_ = (IConstructor) _read("prod(lit(\"it\"),[\\char-class([range(105,105)]),\\char-class([range(116,116)])],{})", Factory.Production);
  private static final IConstructor prod__labeled_$Target__name_$Name_ = (IConstructor) _read("prod(label(\"labeled\",sort(\"Target\")),[label(\"name\",lex(\"Name\"))],{})", Factory.Production);
  private static final IConstructor prod__lit_loc__char_class___range__108_108_char_class___range__111_111_char_class___range__99_99_ = (IConstructor) _read("prod(lit(\"loc\"),[\\char-class([range(108,108)]),\\char-class([range(111,111)]),\\char-class([range(99,99)])],{})", Factory.Production);
  private static final IConstructor prod__negation_$Expression__lit___33_$layouts_LAYOUTLIST_argument_$Expression_ = (IConstructor) _read("prod(label(\"negation\",sort(\"Expression\")),[lit(\"!\"),layouts(\"LAYOUTLIST\"),label(\"argument\",conditional(sort(\"Expression\"),{except(\"noMatch\"),except(\"match\")}))],{})", Factory.Production);
  private static final IConstructor prod__lit_right__char_class___range__114_114_char_class___range__105_105_char_class___range__103_103_char_class___range__104_104_char_class___range__116_116_ = (IConstructor) _read("prod(lit(\"right\"),[\\char-class([range(114,114)]),\\char-class([range(105,105)]),\\char-class([range(103,103)]),\\char-class([range(104,104)]),\\char-class([range(116,116)])],{})", Factory.Production);
  private static final IConstructor prod__topDown_$Strategy__lit_top_down_ = (IConstructor) _read("prod(label(\"topDown\",sort(\"Strategy\")),[lit(\"top-down\")],{})", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_bag_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"bag\")],{})", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_layout_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"layout\")],{})", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_case_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"case\")],{})", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_all_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"all\")],{})", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_any_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"any\")],{})", Factory.Production);
  private static final IConstructor prod__$MidStringChars__char_class___range__62_62_iter_star__$StringCharacter_char_class___range__60_60__tag__category___67_111_110_115_116_97_110_116 = (IConstructor) _read("prod(lex(\"MidStringChars\"),[\\char-class([range(62,62)]),\\iter-star(lex(\"StringCharacter\")),\\char-class([range(60,60)])],{tag(category(\"Constant\"))})", Factory.Production);
  private static final IConstructor prod__lit___33_60_60__char_class___range__33_33_char_class___range__60_60_char_class___range__60_60_ = (IConstructor) _read("prod(lit(\"!\\<\\<\"),[\\char-class([range(33,33)]),\\char-class([range(60,60)]),\\char-class([range(60,60)])],{})", Factory.Production);
  private static final IConstructor prod__lit___40__char_class___range__40_40_ = (IConstructor) _read("prod(lit(\"(\"),[\\char-class([range(40,40)])],{})", Factory.Production);
  private static final IConstructor prod__lit___41__char_class___range__41_41_ = (IConstructor) _read("prod(lit(\")\"),[\\char-class([range(41,41)])],{})", Factory.Production);
  private static final IConstructor prod__lit___42__char_class___range__42_42_ = (IConstructor) _read("prod(lit(\"*\"),[\\char-class([range(42,42)])],{})", Factory.Production);
  private static final IConstructor prod__lit___43__char_class___range__43_43_ = (IConstructor) _read("prod(lit(\"+\"),[\\char-class([range(43,43)])],{})", Factory.Production);
  private static final IConstructor prod__lit___44__char_class___range__44_44_ = (IConstructor) _read("prod(lit(\",\"),[\\char-class([range(44,44)])],{})", Factory.Production);
  private static final IConstructor prod__lit____char_class___range__45_45_ = (IConstructor) _read("prod(lit(\"-\"),[\\char-class([range(45,45)])],{})", Factory.Production);
  private static final IConstructor prod__lit___47__char_class___range__47_47_ = (IConstructor) _read("prod(lit(\"/\"),[\\char-class([range(47,47)])],{})", Factory.Production);
  private static final IConstructor prod__lit_0__char_class___range__48_48_ = (IConstructor) _read("prod(lit(\"0\"),[\\char-class([range(48,48)])],{})", Factory.Production);
  private static final IConstructor prod__lit___60__char_class___range__60_60_ = (IConstructor) _read("prod(lit(\"\\<\"),[\\char-class([range(60,60)])],{})", Factory.Production);
  private static final IConstructor prod__lit___61__char_class___range__61_61_ = (IConstructor) _read("prod(lit(\"=\"),[\\char-class([range(61,61)])],{})", Factory.Production);
  private static final IConstructor prod__lit___62__char_class___range__62_62_ = (IConstructor) _read("prod(lit(\"\\>\"),[\\char-class([range(62,62)])],{})", Factory.Production);
  private static final IConstructor prod__lit___63__char_class___range__63_63_ = (IConstructor) _read("prod(lit(\"?\"),[\\char-class([range(63,63)])],{})", Factory.Production);
  private static final IConstructor prod__all_$Kind__lit_all_ = (IConstructor) _read("prod(label(\"all\",sort(\"Kind\")),[lit(\"all\")],{})", Factory.Production);
  private static final IConstructor prod__assertWithMessage_$Statement__lit_assert_$layouts_LAYOUTLIST_expression_$Expression_$layouts_LAYOUTLIST_lit___58_$layouts_LAYOUTLIST_message_$Expression_$layouts_LAYOUTLIST_lit___59__tag__breakable = (IConstructor) _read("prod(label(\"assertWithMessage\",sort(\"Statement\")),[lit(\"assert\"),layouts(\"LAYOUTLIST\"),label(\"expression\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\":\"),layouts(\"LAYOUTLIST\"),label(\"message\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\";\")],{tag(breakable())})", Factory.Production);
  private static final IConstructor prod__lit_data__char_class___range__100_100_char_class___range__97_97_char_class___range__116_116_char_class___range__97_97_ = (IConstructor) _read("prod(lit(\"data\"),[\\char-class([range(100,100)]),\\char-class([range(97,97)]),\\char-class([range(116,116)]),\\char-class([range(97,97)])],{})", Factory.Production);
  private static final IConstructor prod__decimalIntegerLiteral_$IntegerLiteral__decimal_$DecimalIntegerLiteral_ = (IConstructor) _read("prod(label(\"decimalIntegerLiteral\",sort(\"IntegerLiteral\")),[label(\"decimal\",lex(\"DecimalIntegerLiteral\"))],{})", Factory.Production);
  private static final IConstructor prod__default_$LocalVariableDeclaration__declarator_$Declarator_ = (IConstructor) _read("prod(label(\"default\",sort(\"LocalVariableDeclaration\")),[label(\"declarator\",sort(\"Declarator\"))],{})", Factory.Production);
  private static final IConstructor prod__expression_$Tag__lit___64_$layouts_LAYOUTLIST_name_$Name_$layouts_LAYOUTLIST_lit___61_$layouts_LAYOUTLIST_expression_$Expression__tag__Folded_tag__category___67_111_109_109_101_110_116 = (IConstructor) _read("prod(label(\"expression\",sort(\"Tag\")),[lit(\"@\"),layouts(\"LAYOUTLIST\"),label(\"name\",lex(\"Name\")),layouts(\"LAYOUTLIST\"),lit(\"=\"),layouts(\"LAYOUTLIST\"),label(\"expression\",sort(\"Expression\"))],{tag(Folded()),tag(category(\"Comment\"))})", Factory.Production);
  private static final IConstructor prod__fieldProject_$Expression__expression_$Expression_$layouts_LAYOUTLIST_lit___60_$layouts_LAYOUTLIST_fields_iter_seps__$Field__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___62_ = (IConstructor) _read("prod(label(\"fieldProject\",sort(\"Expression\")),[conditional(label(\"expression\",sort(\"Expression\")),{except(\"transitiveReflexiveClosure\"),except(\"transitiveClosure\"),except(\"isDefined\")}),layouts(\"LAYOUTLIST\"),lit(\"\\<\"),layouts(\"LAYOUTLIST\"),label(\"fields\",\\iter-seps(sort(\"Field\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\"\\>\")],{})", Factory.Production);
  private static final IConstructor prod__history_$ShellCommand__lit_history_ = (IConstructor) _read("prod(label(\"history\",sort(\"ShellCommand\")),[lit(\"history\")],{})", Factory.Production);
  private static final IConstructor prod__statement_$EvalCommand__statement_$Statement_ = (IConstructor) _read("prod(label(\"statement\",sort(\"EvalCommand\")),[label(\"statement\",conditional(sort(\"Statement\"),{except(\"functionDeclaration\"),except(\"visit\"),except(\"variableDeclaration\")}))],{})", Factory.Production);
  private static final IConstructor prod__visit_$Expression__label_$Label_$layouts_LAYOUTLIST_visit_$Visit_ = (IConstructor) _read("prod(label(\"visit\",sort(\"Expression\")),[label(\"label\",sort(\"Label\")),layouts(\"LAYOUTLIST\"),label(\"visit\",sort(\"Visit\"))],{})", Factory.Production);
  private static final IConstructor regular__opt__char_class___range__43_43_range__45_45 = (IConstructor) _read("regular(opt(\\char-class([range(43,43),range(45,45)])))", Factory.Production);
  private static final IConstructor prod__lit___33__char_class___range__33_33_ = (IConstructor) _read("prod(lit(\"!\"),[\\char-class([range(33,33)])],{})", Factory.Production);
  private static final IConstructor prod__lit___34__char_class___range__34_34_ = (IConstructor) _read("prod(lit(\"\\\"\"),[\\char-class([range(34,34)])],{})", Factory.Production);
  private static final IConstructor prod__lit___35__char_class___range__35_35_ = (IConstructor) _read("prod(lit(\"#\"),[\\char-class([range(35,35)])],{})", Factory.Production);
  private static final IConstructor prod__lit___36__char_class___range__36_36_ = (IConstructor) _read("prod(lit(\"$\"),[\\char-class([range(36,36)])],{})", Factory.Production);
  private static final IConstructor prod__lit___37__char_class___range__37_37_ = (IConstructor) _read("prod(lit(\"%\"),[\\char-class([range(37,37)])],{})", Factory.Production);
  private static final IConstructor prod__lit___38__char_class___range__38_38_ = (IConstructor) _read("prod(lit(\"&\"),[\\char-class([range(38,38)])],{})", Factory.Production);
  private static final IConstructor prod__lit___39__char_class___range__39_39_ = (IConstructor) _read("prod(lit(\"\\'\\\\\"),[\\char-class([range(39,39)])],{})", Factory.Production);
  private static final IConstructor prod__lit___46__char_class___range__46_46_ = (IConstructor) _read("prod(lit(\".\"),[\\char-class([range(46,46)])],{})", Factory.Production);
  private static final IConstructor prod__lit_Z__char_class___range__90_90_ = (IConstructor) _read("prod(lit(\"Z\"),[\\char-class([range(90,90)])],{})", Factory.Production);
  private static final IConstructor prod__lit___91__char_class___range__91_91_ = (IConstructor) _read("prod(lit(\"[\"),[\\char-class([range(91,91)])],{})", Factory.Production);
  private static final IConstructor prod__lit___92__char_class___range__92_92_ = (IConstructor) _read("prod(lit(\"\\\\\"),[\\char-class([range(92,92)])],{})", Factory.Production);
  private static final IConstructor prod__lit___93__char_class___range__93_93_ = (IConstructor) _read("prod(lit(\"]\"),[\\char-class([range(93,93)])],{})", Factory.Production);
  private static final IConstructor prod__lit___94__char_class___range__94_94_ = (IConstructor) _read("prod(lit(\"^\"),[\\char-class([range(94,94)])],{})", Factory.Production);
  private static final IConstructor prod__lit_datetime__char_class___range__100_100_char_class___range__97_97_char_class___range__116_116_char_class___range__101_101_char_class___range__116_116_char_class___range__105_105_char_class___range__109_109_char_class___range__101_101_ = (IConstructor) _read("prod(lit(\"datetime\"),[\\char-class([range(100,100)]),\\char-class([range(97,97)]),\\char-class([range(116,116)]),\\char-class([range(101,101)]),\\char-class([range(116,116)]),\\char-class([range(105,105)]),\\char-class([range(109,109)]),\\char-class([range(101,101)])],{})", Factory.Production);
  private static final IConstructor prod__difference_$Class__lhs_$Class_$layouts_LAYOUTLIST_lit___$layouts_LAYOUTLIST_rhs_$Class__assoc__left = (IConstructor) _read("prod(label(\"difference\",sort(\"Class\")),[label(\"lhs\",sort(\"Class\")),layouts(\"LAYOUTLIST\"),lit(\"-\"),layouts(\"LAYOUTLIST\"),label(\"rhs\",sort(\"Class\"))],{assoc(left())})", Factory.Production);
  private static final IConstructor prod__extend_$Import__lit_extend_$layouts_LAYOUTLIST_module_$ImportedModule_$layouts_LAYOUTLIST_lit___59_ = (IConstructor) _read("prod(label(\"extend\",sort(\"Import\")),[lit(\"extend\"),layouts(\"LAYOUTLIST\"),label(\"module\",sort(\"ImportedModule\")),layouts(\"LAYOUTLIST\"),lit(\";\")],{})", Factory.Production);
  private static final IConstructor prod__lit_if__char_class___range__105_105_char_class___range__102_102_ = (IConstructor) _read("prod(lit(\"if\"),[\\char-class([range(105,105)]),\\char-class([range(102,102)])],{})", Factory.Production);
  private static final IConstructor prod__literal_$Expression__literal_$Literal_ = (IConstructor) _read("prod(label(\"literal\",sort(\"Expression\")),[label(\"literal\",sort(\"Literal\"))],{})", Factory.Production);
  private static final IConstructor prod__range_$Expression__lit___91_$layouts_LAYOUTLIST_first_$Expression_$layouts_LAYOUTLIST_lit___46_46_$layouts_LAYOUTLIST_last_$Expression_$layouts_LAYOUTLIST_lit___93_ = (IConstructor) _read("prod(label(\"range\",sort(\"Expression\")),[lit(\"[\"),layouts(\"LAYOUTLIST\"),label(\"first\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"..\"),layouts(\"LAYOUTLIST\"),label(\"last\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"]\")],{})", Factory.Production);
  private static final IConstructor prod__lit_return__char_class___range__114_114_char_class___range__101_101_char_class___range__116_116_char_class___range__117_117_char_class___range__114_114_char_class___range__110_110_ = (IConstructor) _read("prod(lit(\"return\"),[\\char-class([range(114,114)]),\\char-class([range(101,101)]),\\char-class([range(116,116)]),\\char-class([range(117,117)]),\\char-class([range(114,114)]),\\char-class([range(110,110)])],{})", Factory.Production);
  private static final IConstructor prod__set_$Expression__lit___123_$layouts_LAYOUTLIST_elements_iter_star_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_ = (IConstructor) _read("prod(label(\"set\",sort(\"Expression\")),[lit(\"{\"),layouts(\"LAYOUTLIST\"),label(\"elements\",\\iter-star-seps(sort(\"Expression\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\"}\")],{})", Factory.Production);
  private static final IConstructor prod__tag_$Kind__lit_tag_ = (IConstructor) _read("prod(label(\"tag\",sort(\"Kind\")),[lit(\"tag\")],{})", Factory.Production);
  private static final IConstructor regular__seq___lit___92_char_class___range__123_123_range__125_125 = (IConstructor) _read("regular(seq([lit(\"\\\\\"),\\char-class([range(123,123),range(125,125)])]))", Factory.Production);
  private static final IConstructor prod__visit_$Statement__label_$Label_$layouts_LAYOUTLIST_visit_$Visit__tag__breakable = (IConstructor) _read("prod(label(\"visit\",sort(\"Statement\")),[label(\"label\",sort(\"Label\")),layouts(\"LAYOUTLIST\"),label(\"visit\",sort(\"Visit\"))],{tag(breakable())})", Factory.Production);
  private static final IConstructor prod__lit___33_58_61__char_class___range__33_33_char_class___range__58_58_char_class___range__61_61_ = (IConstructor) _read("prod(lit(\"!:=\"),[\\char-class([range(33,33)]),\\char-class([range(58,58)]),\\char-class([range(61,61)])],{})", Factory.Production);
  private static final IConstructor prod__lit___43_61__char_class___range__43_43_char_class___range__61_61_ = (IConstructor) _read("prod(lit(\"+=\"),[\\char-class([range(43,43)]),\\char-class([range(61,61)])],{})", Factory.Production);
  private static final IConstructor prod__lit___64__char_class___range__64_64_ = (IConstructor) _read("prod(lit(\"@\"),[\\char-class([range(64,64)])],{})", Factory.Production);
  private static final IConstructor prod__lit_T__char_class___range__84_84_ = (IConstructor) _read("prod(lit(\"T\"),[\\char-class([range(84,84)])],{})", Factory.Production);
  private static final IConstructor prod__actuals_$ImportedModule__name_$QualifiedName_$layouts_LAYOUTLIST_actuals_$ModuleActuals_ = (IConstructor) _read("prod(label(\"actuals\",sort(\"ImportedModule\")),[label(\"name\",sort(\"QualifiedName\")),layouts(\"LAYOUTLIST\"),label(\"actuals\",sort(\"ModuleActuals\"))],{})", Factory.Production);
  private static final IConstructor prod__lit_alias__char_class___range__97_97_char_class___range__108_108_char_class___range__105_105_char_class___range__97_97_char_class___range__115_115_ = (IConstructor) _read("prod(lit(\"alias\"),[\\char-class([range(97,97)]),\\char-class([range(108,108)]),\\char-class([range(105,105)]),\\char-class([range(97,97)]),\\char-class([range(115,115)])],{})", Factory.Production);
  private static final IConstructor prod__default_$Case__lit_default_$layouts_LAYOUTLIST_lit___58_$layouts_LAYOUTLIST_statement_$Statement__tag__Foldable = (IConstructor) _read("prod(label(\"default\",sort(\"Case\")),[lit(\"default\"),layouts(\"LAYOUTLIST\"),lit(\":\"),layouts(\"LAYOUTLIST\"),label(\"statement\",sort(\"Statement\"))],{tag(Foldable())})", Factory.Production);
  private static final IConstructor prod__default_$Renamings__lit_renaming_$layouts_LAYOUTLIST_renamings_iter_seps__$Renaming__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_ = (IConstructor) _read("prod(label(\"default\",sort(\"Renamings\")),[lit(\"renaming\"),layouts(\"LAYOUTLIST\"),label(\"renamings\",\\iter-seps(sort(\"Renaming\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")]))],{})", Factory.Production);
  private static final IConstructor prod__import_$Command__imported_$Import_ = (IConstructor) _read("prod(label(\"import\",sort(\"Command\")),[label(\"imported\",sort(\"Import\"))],{})", Factory.Production);
  private static final IConstructor prod__interpolated_$ProtocolPart__pre_$PreProtocolChars_$layouts_LAYOUTLIST_expression_$Expression_$layouts_LAYOUTLIST_tail_$ProtocolTail_ = (IConstructor) _read("prod(label(\"interpolated\",sort(\"ProtocolPart\")),[label(\"pre\",lex(\"PreProtocolChars\")),layouts(\"LAYOUTLIST\"),label(\"expression\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),label(\"tail\",sort(\"ProtocolTail\"))],{})", Factory.Production);
  private static final IConstructor prod__lit_mod__char_class___range__109_109_char_class___range__111_111_char_class___range__100_100_ = (IConstructor) _read("prod(lit(\"mod\"),[\\char-class([range(109,109)]),\\char-class([range(111,111)]),\\char-class([range(100,100)])],{})", Factory.Production);
  private static final IConstructor prod__nonAssociative_$Assoc__lit_non_assoc_ = (IConstructor) _read("prod(label(\"nonAssociative\",sort(\"Assoc\")),[lit(\"non-assoc\")],{})", Factory.Production);
  private static final IConstructor prod__outermost_$Strategy__lit_outermost_ = (IConstructor) _read("prod(label(\"outermost\",sort(\"Strategy\")),[lit(\"outermost\")],{})", Factory.Production);
  private static final IConstructor prod__text_$ConcretePart__char_class___range__0_9_range__11_59_range__61_61_range__63_91_range__93_95_range__97_16777215_ = (IConstructor) _read("prod(label(\"text\",lex(\"ConcretePart\")),[\\char-class([range(0,9),range(11,59),range(61,61),range(63,91),range(93,95),range(97,16777215)])],{})", Factory.Production);
  private static final IConstructor prod__tryFinally_$Statement__lit_try_$layouts_LAYOUTLIST_body_$Statement_$layouts_LAYOUTLIST_handlers_iter_seps__$Catch__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit_finally_$layouts_LAYOUTLIST_finallyBody_$Statement__tag__breakable = (IConstructor) _read("prod(label(\"tryFinally\",sort(\"Statement\")),[lit(\"try\"),layouts(\"LAYOUTLIST\"),label(\"body\",sort(\"Statement\")),layouts(\"LAYOUTLIST\"),label(\"handlers\",\\iter-seps(sort(\"Catch\"),[layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\"finally\"),layouts(\"LAYOUTLIST\"),label(\"finallyBody\",conditional(sort(\"Statement\"),{except(\"functionDeclaration\"),except(\"variableDeclaration\")}))],{tag(breakable())})", Factory.Production);
  private static final IConstructor prod__typedVariable_$Pattern__type_$Type_$layouts_LAYOUTLIST_name_$Name_ = (IConstructor) _read("prod(label(\"typedVariable\",sort(\"Pattern\")),[label(\"type\",sort(\"Type\")),layouts(\"LAYOUTLIST\"),label(\"name\",lex(\"Name\"))],{})", Factory.Production);
  private static final IConstructor prod__variable_$Assignable__qualifiedName_$QualifiedName_ = (IConstructor) _read("prod(label(\"variable\",sort(\"Assignable\")),[label(\"qualifiedName\",sort(\"QualifiedName\"))],{})", Factory.Production);
  private static final IConstructor prod__lit___123__char_class___range__123_123_ = (IConstructor) _read("prod(lit(\"{\"),[\\char-class([range(123,123)])],{})", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_import_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"import\")],{})", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_assert_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"assert\")],{})", Factory.Production);
  private static final IConstructor prod__$NamedRegExp__$NamedBackslash_ = (IConstructor) _read("prod(lex(\"NamedRegExp\"),[lex(\"NamedBackslash\")],{})", Factory.Production);
  private static final IConstructor prod__$RegExp__$Backslash_ = (IConstructor) _read("prod(lex(\"RegExp\"),[lex(\"Backslash\")],{})", Factory.Production);
  private static final IConstructor prod__$StringCharacter__char_class___range__0_33_range__35_38_range__40_59_range__61_61_range__63_91_range__93_16777215_ = (IConstructor) _read("prod(lex(\"StringCharacter\"),[\\char-class([range(0,33),range(35,38),range(40,59),range(61,61),range(63,91),range(93,16777215)])],{})", Factory.Production);
  private static final IConstructor regular__iter_star_seps__$Tag__$layouts_LAYOUTLIST = (IConstructor) _read("regular(\\iter-star-seps(sort(\"Tag\"),[layouts(\"LAYOUTLIST\")]))", Factory.Production);
  private static final IConstructor prod__lit___42_47__char_class___range__42_42_char_class___range__47_47_ = (IConstructor) _read("prod(lit(\"*/\"),[\\char-class([range(42,42)]),\\char-class([range(47,47)])],{})", Factory.Production);
  private static final IConstructor prod__append_$Assignment__lit___60_60_61_ = (IConstructor) _read("prod(label(\"append\",sort(\"Assignment\")),[lit(\"\\<\\<=\")],{})", Factory.Production);
  private static final IConstructor prod__caseInsensitiveLiteral_$Sym__cistring_$CaseInsensitiveStringConstant_ = (IConstructor) _read("prod(label(\"caseInsensitiveLiteral\",sort(\"Sym\")),[label(\"cistring\",lex(\"CaseInsensitiveStringConstant\"))],{})", Factory.Production);
  private static final IConstructor prod__default_$ModuleActuals__lit___91_$layouts_LAYOUTLIST_types_iter_seps__$Type__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___93_ = (IConstructor) _read("prod(label(\"default\",sort(\"ModuleActuals\")),[lit(\"[\"),layouts(\"LAYOUTLIST\"),label(\"types\",\\iter-seps(sort(\"Type\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\"]\")],{})", Factory.Production);
  private static final IConstructor prod__defaultStrategy_$Visit__lit_visit_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_subject_$Expression_$layouts_LAYOUTLIST_lit___41_$layouts_LAYOUTLIST_lit___123_$layouts_LAYOUTLIST_cases_iter_seps__$Case__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_ = (IConstructor) _read("prod(label(\"defaultStrategy\",sort(\"Visit\")),[lit(\"visit\"),layouts(\"LAYOUTLIST\"),lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"subject\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\")\"),layouts(\"LAYOUTLIST\"),lit(\"{\"),layouts(\"LAYOUTLIST\"),label(\"cases\",\\iter-seps(sort(\"Case\"),[layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\"}\")],{})", Factory.Production);
  private static final IConstructor prod__labeled_$Sym__symbol_$Sym_$layouts_LAYOUTLIST_label_$NonterminalLabel_ = (IConstructor) _read("prod(label(\"labeled\",sort(\"Sym\")),[label(\"symbol\",sort(\"Sym\")),layouts(\"LAYOUTLIST\"),label(\"label\",lex(\"NonterminalLabel\"))],{})", Factory.Production);
  private static final IConstructor prod__list_$Comprehension__lit___91_$layouts_LAYOUTLIST_results_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___124_$layouts_LAYOUTLIST_generators_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___93__tag__breakable___123_114_101_115_117_108_116_115_44_103_101_110_101_114_97_116_111_114_115_125 = (IConstructor) _read("prod(label(\"list\",sort(\"Comprehension\")),[lit(\"[\"),layouts(\"LAYOUTLIST\"),label(\"results\",\\iter-seps(sort(\"Expression\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\"|\"),layouts(\"LAYOUTLIST\"),label(\"generators\",\\iter-seps(sort(\"Expression\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\"]\")],{tag(breakable(\"{results,generators}\"))})", Factory.Production);
  private static final IConstructor prod__mid_$StringMiddle__mid_$MidStringChars_ = (IConstructor) _read("prod(label(\"mid\",sort(\"StringMiddle\")),[label(\"mid\",lex(\"MidStringChars\"))],{})", Factory.Production);
  private static final IConstructor prod__num_$BasicType__lit_num_ = (IConstructor) _read("prod(label(\"num\",sort(\"BasicType\")),[lit(\"num\")],{})", Factory.Production);
  private static final IConstructor prod__lit_o__char_class___range__111_111_ = (IConstructor) _read("prod(lit(\"o\"),[\\char-class([range(111,111)])],{})", Factory.Production);
  private static final IConstructor prod__subtraction_$Assignment__lit___45_61_ = (IConstructor) _read("prod(label(\"subtraction\",sort(\"Assignment\")),[lit(\"-=\")],{})", Factory.Production);
  private static final IConstructor prod__symbol_$Type__symbol_$Sym_ = (IConstructor) _read("prod(label(\"symbol\",sort(\"Type\")),[label(\"symbol\",conditional(sort(\"Sym\"),{except(\"labeled\"),except(\"parametrized\"),except(\"parameter\"),except(\"nonterminal\")}))],{})", Factory.Production);
  private static final IConstructor prod__unconditional_$Replacement__replacementExpression_$Expression_ = (IConstructor) _read("prod(label(\"unconditional\",sort(\"Replacement\")),[label(\"replacementExpression\",sort(\"Expression\"))],{})", Factory.Production);
  private static final IConstructor prod__lit_unimport__char_class___range__117_117_char_class___range__110_110_char_class___range__105_105_char_class___range__109_109_char_class___range__112_112_char_class___range__111_111_char_class___range__114_114_char_class___range__116_116_ = (IConstructor) _read("prod(lit(\"unimport\"),[\\char-class([range(117,117)]),\\char-class([range(110,110)]),\\char-class([range(105,105)]),\\char-class([range(109,109)]),\\char-class([range(112,112)]),\\char-class([range(111,111)]),\\char-class([range(114,114)]),\\char-class([range(116,116)])],{})", Factory.Production);
  private static final IConstructor prod__utf32_$UnicodeEscape__lit___92_char_class___range__85_85_char_class___range__48_57_range__65_70_range__97_102_char_class___range__48_57_range__65_70_range__97_102_char_class___range__48_57_range__65_70_range__97_102_char_class___range__48_57_range__65_70_range__97_102_char_class___range__48_57_range__65_70_range__97_102_char_class___range__48_57_range__65_70_range__97_102_ = (IConstructor) _read("prod(label(\"utf32\",lex(\"UnicodeEscape\")),[lit(\"\\\\\"),\\char-class([range(85,85)]),\\char-class([range(48,57),range(65,70),range(97,102)]),\\char-class([range(48,57),range(65,70),range(97,102)]),\\char-class([range(48,57),range(65,70),range(97,102)]),\\char-class([range(48,57),range(65,70),range(97,102)]),\\char-class([range(48,57),range(65,70),range(97,102)]),\\char-class([range(48,57),range(65,70),range(97,102)])],{})", Factory.Production);
  private static final IConstructor prod__variableDeclaration_$Statement__declaration_$LocalVariableDeclaration_$layouts_LAYOUTLIST_lit___59__tag__breakable = (IConstructor) _read("prod(label(\"variableDeclaration\",sort(\"Statement\")),[label(\"declaration\",sort(\"LocalVariableDeclaration\")),layouts(\"LAYOUTLIST\"),lit(\";\")],{tag(breakable())})", Factory.Production);
  private static final IConstructor prod__lit___124__char_class___range__124_124_ = (IConstructor) _read("prod(lit(\"|\"),[\\char-class([range(124,124)])],{})", Factory.Production);
  private static final IConstructor prod__lit___125__char_class___range__125_125_ = (IConstructor) _read("prod(lit(\"}\"),[\\char-class([range(125,125)])],{})", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_true_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"true\")],{})", Factory.Production);
  private static final IConstructor prod__$DateAndTime__lit___36_$DatePart_lit_T_$TimePartNoTZ_ = (IConstructor) _read("prod(lex(\"DateAndTime\"),[lit(\"$\"),lex(\"DatePart\"),lit(\"T\"),conditional(lex(\"TimePartNoTZ\"),{\\not-follow(\\char-class([range(43,43),range(45,45)]))})],{})", Factory.Production);
  private static final IConstructor prod__$RationalLiteral__char_class___range__48_57_iter_star__char_class___range__48_57_char_class___range__114_114_ = (IConstructor) _read("prod(lex(\"RationalLiteral\"),[\\char-class([range(48,57)]),\\iter-star(\\char-class([range(48,57)])),\\char-class([range(114,114)])],{})", Factory.Production);
  private static final IConstructor prod__$TimeZonePart__char_class___range__43_43_range__45_45_char_class___range__48_49_char_class___range__48_57_lit___58_char_class___range__48_53_char_class___range__48_57_ = (IConstructor) _read("prod(lex(\"TimeZonePart\"),[\\char-class([range(43,43),range(45,45)]),\\char-class([range(48,49)]),\\char-class([range(48,57)]),lit(\":\"),\\char-class([range(48,53)]),\\char-class([range(48,57)])],{})", Factory.Production);
  private static final IConstructor regular__iter_star_seps__$ProdModifier__$layouts_LAYOUTLIST = (IConstructor) _read("regular(\\iter-star-seps(sort(\"ProdModifier\"),[layouts(\"LAYOUTLIST\")]))", Factory.Production);
  private static final IConstructor regular__iter_star_seps__$Toplevel__$layouts_LAYOUTLIST = (IConstructor) _read("regular(\\iter-star-seps(sort(\"Toplevel\"),[layouts(\"LAYOUTLIST\")]))", Factory.Production);
  private static final IConstructor prod__lit___42_61__char_class___range__42_42_char_class___range__61_61_ = (IConstructor) _read("prod(lit(\"*=\"),[\\char-class([range(42,42)]),\\char-class([range(61,61)])],{})", Factory.Production);
  private static final IConstructor prod__lit___96__char_class___range__96_96_ = (IConstructor) _read("prod(lit(\"`\"),[\\char-class([range(96,96)])],{})", Factory.Production);
  private static final IConstructor prod__bool_$BasicType__lit_bool_ = (IConstructor) _read("prod(label(\"bool\",sort(\"BasicType\")),[lit(\"bool\")],{})", Factory.Production);
  private static final IConstructor prod__declaration_$Command__declaration_$Declaration_ = (IConstructor) _read("prod(label(\"declaration\",sort(\"Command\")),[label(\"declaration\",sort(\"Declaration\"))],{})", Factory.Production);
  private static final IConstructor prod__lit_edit__char_class___range__101_101_char_class___range__100_100_char_class___range__105_105_char_class___range__116_116_ = (IConstructor) _read("prod(lit(\"edit\"),[\\char-class([range(101,101)]),\\char-class([range(100,100)]),\\char-class([range(105,105)]),\\char-class([range(116,116)])],{})", Factory.Production);
  private static final IConstructor prod__hole_$ConcretePart__hole_$ConcreteHole_ = (IConstructor) _read("prod(label(\"hole\",lex(\"ConcretePart\")),[label(\"hole\",sort(\"ConcreteHole\"))],{})", Factory.Production);
  private static final IConstructor prod__ifThen_$Statement__label_$Label_$layouts_LAYOUTLIST_lit_if_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_conditions_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_$layouts_LAYOUTLIST_thenStatement_$Statement_$layouts_LAYOUTLIST_empty__tag__breakable = (IConstructor) _read("prod(label(\"ifThen\",sort(\"Statement\")),[label(\"label\",sort(\"Label\")),layouts(\"LAYOUTLIST\"),lit(\"if\"),layouts(\"LAYOUTLIST\"),lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"conditions\",\\iter-seps(sort(\"Expression\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\")\"),layouts(\"LAYOUTLIST\"),label(\"thenStatement\",conditional(sort(\"Statement\"),{except(\"functionDeclaration\"),except(\"variableDeclaration\")})),layouts(\"LAYOUTLIST\"),conditional(empty(),{\\not-follow(lit(\"else\"))})],{tag(breakable())})", Factory.Production);
  private static final IConstructor prod__ifThenElse_$Expression__condition_$Expression_$layouts_LAYOUTLIST_lit___63_$layouts_LAYOUTLIST_thenExp_$Expression_$layouts_LAYOUTLIST_lit___58_$layouts_LAYOUTLIST_elseExp_$Expression__assoc__right = (IConstructor) _read("prod(label(\"ifThenElse\",sort(\"Expression\")),[label(\"condition\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"?\"),layouts(\"LAYOUTLIST\"),label(\"thenExp\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\":\"),layouts(\"LAYOUTLIST\"),label(\"elseExp\",sort(\"Expression\"))],{assoc(right())})", Factory.Production);
  private static final IConstructor prod__intersection_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___38_$layouts_LAYOUTLIST_rhs_$Expression__assoc__left = (IConstructor) _read("prod(label(\"intersection\",sort(\"Expression\")),[label(\"lhs\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),conditional(lit(\"&\"),{\\not-follow(lit(\"&\"))}),layouts(\"LAYOUTLIST\"),label(\"rhs\",sort(\"Expression\"))],{assoc(left())})", Factory.Production);
  private static final IConstructor prod__midInterpolated_$StringTail__mid_$MidStringChars_$layouts_LAYOUTLIST_expression_$Expression_$layouts_LAYOUTLIST_tail_$StringTail_ = (IConstructor) _read("prod(label(\"midInterpolated\",sort(\"StringTail\")),[label(\"mid\",lex(\"MidStringChars\")),layouts(\"LAYOUTLIST\"),label(\"expression\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),label(\"tail\",sort(\"StringTail\"))],{})", Factory.Production);
  private static final IConstructor prod__lit_node__char_class___range__110_110_char_class___range__111_111_char_class___range__100_100_char_class___range__101_101_ = (IConstructor) _read("prod(lit(\"node\"),[\\char-class([range(110,110)]),\\char-class([range(111,111)]),\\char-class([range(100,100)]),\\char-class([range(101,101)])],{})", Factory.Production);
  private static final IConstructor prod__qualifiedName_$Pattern__qualifiedName_$QualifiedName_ = (IConstructor) _read("prod(label(\"qualifiedName\",sort(\"Pattern\")),[label(\"qualifiedName\",sort(\"QualifiedName\"))],{})", Factory.Production);
  private static final IConstructor prod__set_$Comprehension__lit___123_$layouts_LAYOUTLIST_results_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___124_$layouts_LAYOUTLIST_generators_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125__tag__breakable___123_114_101_115_117_108_116_115_44_103_101_110_101_114_97_116_111_114_115_125 = (IConstructor) _read("prod(label(\"set\",sort(\"Comprehension\")),[lit(\"{\"),layouts(\"LAYOUTLIST\"),label(\"results\",\\iter-seps(sort(\"Expression\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\"|\"),layouts(\"LAYOUTLIST\"),label(\"generators\",\\iter-seps(sort(\"Expression\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\"}\")],{tag(breakable(\"{results,generators}\"))})", Factory.Production);
  private static final IConstructor prod__subtraction_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___$layouts_LAYOUTLIST_rhs_$Expression__assoc__left = (IConstructor) _read("prod(label(\"subtraction\",sort(\"Expression\")),[label(\"lhs\",conditional(sort(\"Expression\"),{except(\"transitiveReflexiveClosure\"),except(\"transitiveClosure\")})),layouts(\"LAYOUTLIST\"),lit(\"-\"),layouts(\"LAYOUTLIST\"),label(\"rhs\",sort(\"Expression\"))],{assoc(left())})", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_while_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"while\")],{})", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_filter_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"filter\")],{})", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_finally_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"finally\")],{})", Factory.Production);
  private static final IConstructor prod__$Comment__lit___47_42_iter_star__alt___char_class___range__42_42_char_class___range__0_41_range__43_16777215_lit___42_47__tag__category___67_111_109_109_101_110_116 = (IConstructor) _read("prod(lex(\"Comment\"),[lit(\"/*\"),\\iter-star(alt({conditional(\\char-class([range(42,42)]),{\\not-follow(\\char-class([range(47,47)]))}),\\char-class([range(0,41),range(43,16777215)])})),lit(\"*/\")],{tag(category(\"Comment\"))})", Factory.Production);
  private static final IConstructor prod__$NamedRegExp__lit___60_$Name_lit___62_ = (IConstructor) _read("prod(lex(\"NamedRegExp\"),[lit(\"\\<\"),lex(\"Name\"),lit(\"\\>\")],{})", Factory.Production);
  private static final IConstructor prod__$RegExpModifier__iter_star__char_class___range__100_100_range__105_105_range__109_109_range__115_115_ = (IConstructor) _read("prod(lex(\"RegExpModifier\"),[\\iter-star(\\char-class([range(100,100),range(105,105),range(109,109),range(115,115)]))],{})", Factory.Production);
  private static final IConstructor prod__$TimeZonePart__char_class___range__43_43_range__45_45_char_class___range__48_49_char_class___range__48_57_char_class___range__48_53_char_class___range__48_57_ = (IConstructor) _read("prod(lex(\"TimeZonePart\"),[\\char-class([range(43,43),range(45,45)]),\\char-class([range(48,49)]),\\char-class([range(48,57)]),\\char-class([range(48,53)]),\\char-class([range(48,57)])],{})", Factory.Production);
  private static final IConstructor regular__alt___char_class___range__42_42_char_class___range__0_41_range__43_16777215 = (IConstructor) _read("regular(alt({conditional(\\char-class([range(42,42)]),{\\not-follow(\\char-class([range(47,47)]))}),\\char-class([range(0,41),range(43,16777215)])}))", Factory.Production);
  private static final IConstructor regular__alt___$TagString_char_class___range__0_122_range__124_124_range__126_16777215_seq___lit___92_char_class___range__123_123_range__125_125 = (IConstructor) _read("regular(alt({lex(\"TagString\"),\\char-class([range(0,122),range(124,124),range(126,16777215)]),seq([lit(\"\\\\\"),\\char-class([range(123,123),range(125,125)])])}))", Factory.Production);
  private static final IConstructor regular__iter__char_class___range__48_57_range__65_70_range__97_102 = (IConstructor) _read("regular(iter(\\char-class([range(48,57),range(65,70),range(97,102)])))", Factory.Production);
  private static final IConstructor prod__lit___36_84__char_class___range__36_36_char_class___range__84_84_ = (IConstructor) _read("prod(lit(\"$T\"),[\\char-class([range(36,36)]),\\char-class([range(84,84)])],{})", Factory.Production);
  private static final IConstructor regular__iter_seps__$Name__$layouts_LAYOUTLIST_lit___58_58_$layouts_LAYOUTLIST = (IConstructor) _read("regular(\\iter-seps(lex(\"Name\"),[layouts(\"LAYOUTLIST\"),lit(\"::\"),layouts(\"LAYOUTLIST\")]))", Factory.Production);
  private static final IConstructor prod__alias_$Declaration__tags_$Tags_$layouts_LAYOUTLIST_visibility_$Visibility_$layouts_LAYOUTLIST_lit_alias_$layouts_LAYOUTLIST_user_$UserType_$layouts_LAYOUTLIST_lit___61_$layouts_LAYOUTLIST_base_$Type_$layouts_LAYOUTLIST_lit___59_ = (IConstructor) _read("prod(label(\"alias\",sort(\"Declaration\")),[label(\"tags\",sort(\"Tags\")),layouts(\"LAYOUTLIST\"),label(\"visibility\",sort(\"Visibility\")),layouts(\"LAYOUTLIST\"),lit(\"alias\"),layouts(\"LAYOUTLIST\"),label(\"user\",sort(\"UserType\")),layouts(\"LAYOUTLIST\"),lit(\"=\"),layouts(\"LAYOUTLIST\"),label(\"base\",sort(\"Type\")),layouts(\"LAYOUTLIST\"),lit(\";\")],{})", Factory.Production);
  private static final IConstructor prod__binding_$Catch__lit_catch_$layouts_LAYOUTLIST_pattern_$Pattern_$layouts_LAYOUTLIST_lit___58_$layouts_LAYOUTLIST_body_$Statement_ = (IConstructor) _read("prod(label(\"binding\",sort(\"Catch\")),[lit(\"catch\"),layouts(\"LAYOUTLIST\"),label(\"pattern\",sort(\"Pattern\")),layouts(\"LAYOUTLIST\"),lit(\":\"),layouts(\"LAYOUTLIST\"),label(\"body\",sort(\"Statement\"))],{})", Factory.Production);
  private static final IConstructor prod__bracket_$Assignable__lit___40_$layouts_LAYOUTLIST_arg_$Assignable_$layouts_LAYOUTLIST_lit___41__bracket = (IConstructor) _read("prod(label(\"bracket\",sort(\"Assignable\")),[lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"arg\",sort(\"Assignable\")),layouts(\"LAYOUTLIST\"),lit(\")\")],{bracket()})", Factory.Production);
  private static final IConstructor prod__lit_do__char_class___range__100_100_char_class___range__111_111_ = (IConstructor) _read("prod(lit(\"do\"),[\\char-class([range(100,100)]),\\char-class([range(111,111)])],{})", Factory.Production);
  private static final IConstructor prod__fieldAccess_$Assignable__receiver_$Assignable_$layouts_LAYOUTLIST_lit___46_$layouts_LAYOUTLIST_field_$Name_ = (IConstructor) _read("prod(label(\"fieldAccess\",sort(\"Assignable\")),[label(\"receiver\",sort(\"Assignable\")),layouts(\"LAYOUTLIST\"),lit(\".\"),layouts(\"LAYOUTLIST\"),label(\"field\",lex(\"Name\"))],{})", Factory.Production);
  private static final IConstructor prod__function_$Type__function_$FunctionType_ = (IConstructor) _read("prod(label(\"function\",sort(\"Type\")),[label(\"function\",sort(\"FunctionType\"))],{})", Factory.Production);
  private static final IConstructor prod__lit_java__char_class___range__106_106_char_class___range__97_97_char_class___range__118_118_char_class___range__97_97_ = (IConstructor) _read("prod(lit(\"java\"),[\\char-class([range(106,106)]),\\char-class([range(97,97)]),\\char-class([range(118,118)]),\\char-class([range(97,97)])],{})", Factory.Production);
  private static final IConstructor prod__nAryConstructor_$Variant__name_$Name_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_arguments_iter_star_seps__$TypeArg__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_ = (IConstructor) _read("prod(label(\"nAryConstructor\",sort(\"Variant\")),[label(\"name\",lex(\"Name\")),layouts(\"LAYOUTLIST\"),lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"arguments\",\\iter-star-seps(sort(\"TypeArg\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\")\")],{})", Factory.Production);
  private static final IConstructor prod__tuple_$Pattern__lit___60_$layouts_LAYOUTLIST_elements_iter_seps__$Pattern__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___62_ = (IConstructor) _read("prod(label(\"tuple\",sort(\"Pattern\")),[lit(\"\\<\"),layouts(\"LAYOUTLIST\"),label(\"elements\",\\iter-seps(sort(\"Pattern\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\"\\>\")],{})", Factory.Production);
  private static final IConstructor prod__start__$Command__$layouts_LAYOUTLIST_top_$Command_$layouts_LAYOUTLIST_ = (IConstructor) _read("prod(start(sort(\"Command\")),[layouts(\"LAYOUTLIST\"),label(\"top\",sort(\"Command\")),layouts(\"LAYOUTLIST\")],{})", Factory.Production);
  private static final IConstructor prod__givenStrategy_$Visit__strategy_$Strategy_$layouts_LAYOUTLIST_lit_visit_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_subject_$Expression_$layouts_LAYOUTLIST_lit___41_$layouts_LAYOUTLIST_lit___123_$layouts_LAYOUTLIST_cases_iter_seps__$Case__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_ = (IConstructor) _read("prod(label(\"givenStrategy\",sort(\"Visit\")),[label(\"strategy\",sort(\"Strategy\")),layouts(\"LAYOUTLIST\"),lit(\"visit\"),layouts(\"LAYOUTLIST\"),lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"subject\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\")\"),layouts(\"LAYOUTLIST\"),lit(\"{\"),layouts(\"LAYOUTLIST\"),label(\"cases\",\\iter-seps(sort(\"Case\"),[layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\"}\")],{})", Factory.Production);
  private static final IConstructor prod__newline_$ConcretePart__lit___10_iter_star__char_class___range__9_9_range__32_32_range__65_65_range__160_160_range__5760_5760_range__8192_8192_range__8239_8239_range__8287_8287_range__12288_12288_lit___39_ = (IConstructor) _read("prod(label(\"newline\",lex(\"ConcretePart\")),[lit(\"\\n\"),\\iter-star(\\char-class([range(9,9),range(32,32),range(65,65),range(160,160),range(5760,5760),range(8192,8192),range(8239,8239),range(8287,8287),range(12288,12288)])),lit(\"\\'\\\\\")],{})", Factory.Production);
  private static final IConstructor prod__reifiedType_$Pattern__lit_type_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_symbol_$Pattern_$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_definitions_$Pattern_$layouts_LAYOUTLIST_lit___41_ = (IConstructor) _read("prod(label(\"reifiedType\",sort(\"Pattern\")),[lit(\"type\"),layouts(\"LAYOUTLIST\"),lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"symbol\",sort(\"Pattern\")),layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\"),label(\"definitions\",sort(\"Pattern\")),layouts(\"LAYOUTLIST\"),lit(\")\")],{})", Factory.Production);
  private static final IConstructor prod__statement_$Command__statement_$Statement_ = (IConstructor) _read("prod(label(\"statement\",sort(\"Command\")),[label(\"statement\",conditional(sort(\"Statement\"),{except(\"functionDeclaration\"),except(\"visit\"),except(\"variableDeclaration\")}))],{})", Factory.Production);
  private static final IConstructor prod__void_$BasicType__lit_void_ = (IConstructor) _read("prod(label(\"void\",sort(\"BasicType\")),[lit(\"void\")],{})", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_append_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"append\")],{})", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_notin_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"notin\")],{})", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_visit_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"visit\")],{})", Factory.Production);
  private static final IConstructor prod__$JustTime__lit___36_84_$TimePartNoTZ_ = (IConstructor) _read("prod(lex(\"JustTime\"),[lit(\"$T\"),conditional(lex(\"TimePartNoTZ\"),{\\not-follow(\\char-class([range(43,43),range(45,45)]))})],{})", Factory.Production);
  private static final IConstructor prod__$MidProtocolChars__lit___62_$URLChars_lit___60_ = (IConstructor) _read("prod(lex(\"MidProtocolChars\"),[lit(\"\\>\"),lex(\"URLChars\"),lit(\"\\<\")],{})", Factory.Production);
  private static final IConstructor prod__$StringCharacter__char_class___range__10_10_iter_star__char_class___range__9_9_range__32_32_range__65_65_range__160_160_range__5760_5760_range__8192_8192_range__8239_8239_range__8287_8287_range__12288_12288_char_class___range__39_39_ = (IConstructor) _read("prod(lex(\"StringCharacter\"),[\\char-class([range(10,10)]),\\iter-star(\\char-class([range(9,9),range(32,32),range(65,65),range(160,160),range(5760,5760),range(8192,8192),range(8239,8239),range(8287,8287),range(12288,12288)])),\\char-class([range(39,39)])],{})", Factory.Production);
  private static final IConstructor regular__iter_star_seps__$Sym__$layouts_LAYOUTLIST = (IConstructor) _read("regular(\\iter-star-seps(sort(\"Sym\"),[layouts(\"LAYOUTLIST\")]))", Factory.Production);
  private static final IConstructor prod__callOrTree_$Pattern__expression_$Pattern_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_arguments_iter_star_seps__$Pattern__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_ = (IConstructor) _read("prod(label(\"callOrTree\",sort(\"Pattern\")),[label(\"expression\",sort(\"Pattern\")),layouts(\"LAYOUTLIST\"),lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"arguments\",\\iter-star-seps(sort(\"Pattern\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\")\")],{})", Factory.Production);
  private static final IConstructor prod__constructor_$Assignable__name_$Name_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_arguments_iter_seps__$Assignable__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_ = (IConstructor) _read("prod(label(\"constructor\",sort(\"Assignable\")),[label(\"name\",lex(\"Name\")),layouts(\"LAYOUTLIST\"),lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"arguments\",\\iter-seps(sort(\"Assignable\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\")\")],{})", Factory.Production);
  private static final IConstructor prod__data_$Declaration__tags_$Tags_$layouts_LAYOUTLIST_visibility_$Visibility_$layouts_LAYOUTLIST_lit_data_$layouts_LAYOUTLIST_user_$UserType_$layouts_LAYOUTLIST_lit___61_$layouts_LAYOUTLIST_variants_iter_seps__$Variant__$layouts_LAYOUTLIST_lit___124_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___59__tag__Foldable = (IConstructor) _read("prod(label(\"data\",sort(\"Declaration\")),[label(\"tags\",sort(\"Tags\")),layouts(\"LAYOUTLIST\"),label(\"visibility\",sort(\"Visibility\")),layouts(\"LAYOUTLIST\"),lit(\"data\"),layouts(\"LAYOUTLIST\"),label(\"user\",sort(\"UserType\")),layouts(\"LAYOUTLIST\"),lit(\"=\"),layouts(\"LAYOUTLIST\"),label(\"variants\",\\iter-seps(sort(\"Variant\"),[layouts(\"LAYOUTLIST\"),lit(\"|\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\";\")],{tag(Foldable())})", Factory.Production);
  private static final IConstructor prod__default_$Mapping__$Expression__from_$Expression_$layouts_LAYOUTLIST_lit___58_$layouts_LAYOUTLIST_to_$Expression_ = (IConstructor) _read("prod(label(\"default\",\\parameterized-sort(\"Mapping\",[sort(\"Expression\")])),[label(\"from\",conditional(sort(\"Expression\"),{except(\"ifDefinedOtherwise\")})),layouts(\"LAYOUTLIST\"),lit(\":\"),layouts(\"LAYOUTLIST\"),label(\"to\",sort(\"Expression\"))],{})", Factory.Production);
  private static final IConstructor prod__descendant_$Pattern__lit___47_$layouts_LAYOUTLIST_pattern_$Pattern_ = (IConstructor) _read("prod(label(\"descendant\",sort(\"Pattern\")),[lit(\"/\"),layouts(\"LAYOUTLIST\"),label(\"pattern\",sort(\"Pattern\"))],{})", Factory.Production);
  private static final IConstructor prod__function_$Declaration__functionDeclaration_$FunctionDeclaration_ = (IConstructor) _read("prod(label(\"function\",sort(\"Declaration\")),[label(\"functionDeclaration\",sort(\"FunctionDeclaration\"))],{})", Factory.Production);
  private static final IConstructor prod__lit_has__char_class___range__104_104_char_class___range__97_97_char_class___range__115_115_ = (IConstructor) _read("prod(lit(\"has\"),[\\char-class([range(104,104)]),\\char-class([range(97,97)]),\\char-class([range(115,115)])],{})", Factory.Production);
  private static final IConstructor prod__implication_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___61_61_62_$layouts_LAYOUTLIST_rhs_$Expression__assoc__non_assoc = (IConstructor) _read("prod(label(\"implication\",sort(\"Expression\")),[label(\"lhs\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"==\\>\"),layouts(\"LAYOUTLIST\"),label(\"rhs\",sort(\"Expression\"))],{assoc(\\non-assoc())})", Factory.Production);
  private static final IConstructor prod__node_$BasicType__lit_node_ = (IConstructor) _read("prod(label(\"node\",sort(\"BasicType\")),[lit(\"node\")],{})", Factory.Production);
  private static final IConstructor prod__lit_outermost__char_class___range__111_111_char_class___range__117_117_char_class___range__116_116_char_class___range__101_101_char_class___range__114_114_char_class___range__109_109_char_class___range__111_111_char_class___range__115_115_char_class___range__116_116_ = (IConstructor) _read("prod(lit(\"outermost\"),[\\char-class([range(111,111)]),\\char-class([range(117,117)]),\\char-class([range(116,116)]),\\char-class([range(101,101)]),\\char-class([range(114,114)]),\\char-class([range(109,109)]),\\char-class([range(111,111)]),\\char-class([range(115,115)]),\\char-class([range(116,116)])],{})", Factory.Production);
  private static final IConstructor prod__parameter_$Sym__lit___38_$layouts_LAYOUTLIST_nonterminal_$Nonterminal_ = (IConstructor) _read("prod(label(\"parameter\",sort(\"Sym\")),[lit(\"&\"),layouts(\"LAYOUTLIST\"),label(\"nonterminal\",lex(\"Nonterminal\"))],{})", Factory.Production);
  private static final IConstructor prod__patternWithAction_$Case__lit_case_$layouts_LAYOUTLIST_patternWithAction_$PatternWithAction__tag__Foldable = (IConstructor) _read("prod(label(\"patternWithAction\",sort(\"Case\")),[lit(\"case\"),layouts(\"LAYOUTLIST\"),label(\"patternWithAction\",sort(\"PatternWithAction\"))],{tag(Foldable())})", Factory.Production);
  private static final IConstructor prod__lit_quit__char_class___range__113_113_char_class___range__117_117_char_class___range__105_105_char_class___range__116_116_ = (IConstructor) _read("prod(lit(\"quit\"),[\\char-class([range(113,113)]),\\char-class([range(117,117)]),\\char-class([range(105,105)]),\\char-class([range(116,116)])],{})", Factory.Production);
  private static final IConstructor prod__lit_tuple__char_class___range__116_116_char_class___range__117_117_char_class___range__112_112_char_class___range__108_108_char_class___range__101_101_ = (IConstructor) _read("prod(lit(\"tuple\"),[\\char-class([range(116,116)]),\\char-class([range(117,117)]),\\char-class([range(112,112)]),\\char-class([range(108,108)]),\\char-class([range(101,101)])],{})", Factory.Production);
  private static final IConstructor prod__variable_$Declaration__tags_$Tags_$layouts_LAYOUTLIST_visibility_$Visibility_$layouts_LAYOUTLIST_type_$Type_$layouts_LAYOUTLIST_variables_iter_seps__$Variable__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___59_ = (IConstructor) _read("prod(label(\"variable\",sort(\"Declaration\")),[label(\"tags\",sort(\"Tags\")),layouts(\"LAYOUTLIST\"),label(\"visibility\",sort(\"Visibility\")),layouts(\"LAYOUTLIST\"),label(\"type\",sort(\"Type\")),layouts(\"LAYOUTLIST\"),label(\"variables\",\\iter-seps(sort(\"Variable\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\";\")],{})", Factory.Production);
  private static final IConstructor prod__$OctalIntegerLiteral__char_class___range__48_48_iter__char_class___range__48_55_ = (IConstructor) _read("prod(lex(\"OctalIntegerLiteral\"),[\\char-class([range(48,48)]),conditional(iter(\\char-class([range(48,55)])),{\\not-follow(\\char-class([range(48,57),range(65,90),range(95,95),range(97,122)]))})],{})", Factory.Production);
  private static final IConstructor regular__iter_star_seps__$ConcretePart__$layouts_LAYOUTLIST = (IConstructor) _read("regular(\\iter-star-seps(lex(\"ConcretePart\"),[layouts(\"LAYOUTLIST\")]))", Factory.Production);
  private static final IConstructor regular__iter_seps__$Variant__$layouts_LAYOUTLIST_lit___124_$layouts_LAYOUTLIST = (IConstructor) _read("regular(\\iter-seps(sort(\"Variant\"),[layouts(\"LAYOUTLIST\"),lit(\"|\"),layouts(\"LAYOUTLIST\")]))", Factory.Production);
  private static final IConstructor prod__type_$BasicType__lit_type_ = (IConstructor) _read("prod(label(\"type\",sort(\"BasicType\")),[lit(\"type\")],{})", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_start_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"start\")],{})", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_anno_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"anno\")],{})", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_insert_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"insert\")],{})", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_try_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"try\")],{})", Factory.Production);
  private static final IConstructor regular__seq___char_class___range__65_90_range__95_95_range__97_122_iter_star__char_class___range__48_57_range__65_90_range__95_95_range__97_122 = (IConstructor) _read("regular(seq([conditional(\\char-class([range(65,90),range(95,95),range(97,122)]),{\\not-precede(\\char-class([range(65,90),range(95,95),range(97,122)]))}),conditional(\\iter-star(\\char-class([range(48,57),range(65,90),range(95,95),range(97,122)])),{\\not-follow(\\char-class([range(48,57),range(65,90),range(95,95),range(97,122)]))})]))", Factory.Production);
  private static final IConstructor prod__lit___38_38__char_class___range__38_38_char_class___range__38_38_ = (IConstructor) _read("prod(lit(\"&&\"),[\\char-class([range(38,38)]),\\char-class([range(38,38)])],{})", Factory.Production);
  private static final IConstructor prod__lit___38_61__char_class___range__38_38_char_class___range__61_61_ = (IConstructor) _read("prod(lit(\"&=\"),[\\char-class([range(38,38)]),\\char-class([range(61,61)])],{})", Factory.Production);
  private static final IConstructor prod__alternative_$Sym__lit___40_$layouts_LAYOUTLIST_first_$Sym_$layouts_LAYOUTLIST_lit___124_$layouts_LAYOUTLIST_alternatives_iter_seps__$Sym__$layouts_LAYOUTLIST_lit___124_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_ = (IConstructor) _read("prod(label(\"alternative\",sort(\"Sym\")),[lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"first\",sort(\"Sym\")),layouts(\"LAYOUTLIST\"),lit(\"|\"),layouts(\"LAYOUTLIST\"),label(\"alternatives\",\\iter-seps(sort(\"Sym\"),[layouts(\"LAYOUTLIST\"),lit(\"|\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\")\")],{})", Factory.Production);
  private static final IConstructor prod__default_$FunctionModifier__lit_default_ = (IConstructor) _read("prod(label(\"default\",sort(\"FunctionModifier\")),[lit(\"default\")],{})", Factory.Production);
  private static final IConstructor prod__default_$StructuredType__basicType_$BasicType_$layouts_LAYOUTLIST_lit___91_$layouts_LAYOUTLIST_arguments_iter_seps__$TypeArg__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___93_ = (IConstructor) _read("prod(label(\"default\",sort(\"StructuredType\")),[label(\"basicType\",sort(\"BasicType\")),layouts(\"LAYOUTLIST\"),lit(\"[\"),layouts(\"LAYOUTLIST\"),label(\"arguments\",\\iter-seps(sort(\"TypeArg\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\"]\")],{})", Factory.Production);
  private static final IConstructor prod__empty_$Label__ = (IConstructor) _read("prod(label(\"empty\",sort(\"Label\")),[],{})", Factory.Production);
  private static final IConstructor prod__ifThenElse_$StringTemplate__lit_if_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_conditions_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_$layouts_LAYOUTLIST_lit___123_$layouts_LAYOUTLIST_preStatsThen_iter_star_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_thenString_$StringMiddle_$layouts_LAYOUTLIST_postStatsThen_iter_star_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_$layouts_LAYOUTLIST_lit_else_$layouts_LAYOUTLIST_lit___123_$layouts_LAYOUTLIST_preStatsElse_iter_star_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_elseString_$StringMiddle_$layouts_LAYOUTLIST_postStatsElse_iter_star_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_ = (IConstructor) _read("prod(label(\"ifThenElse\",sort(\"StringTemplate\")),[lit(\"if\"),layouts(\"LAYOUTLIST\"),lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"conditions\",\\iter-seps(sort(\"Expression\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\")\"),layouts(\"LAYOUTLIST\"),lit(\"{\"),layouts(\"LAYOUTLIST\"),label(\"preStatsThen\",\\iter-star-seps(sort(\"Statement\"),[layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),label(\"thenString\",sort(\"StringMiddle\")),layouts(\"LAYOUTLIST\"),label(\"postStatsThen\",\\iter-star-seps(sort(\"Statement\"),[layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\"}\"),layouts(\"LAYOUTLIST\"),lit(\"else\"),layouts(\"LAYOUTLIST\"),lit(\"{\"),layouts(\"LAYOUTLIST\"),label(\"preStatsElse\",\\iter-star-seps(sort(\"Statement\"),[layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),label(\"elseString\",sort(\"StringMiddle\")),layouts(\"LAYOUTLIST\"),label(\"postStatsElse\",\\iter-star-seps(sort(\"Statement\"),[layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\"}\")],{})", Factory.Production);
  private static final IConstructor prod__import_$EvalCommand__imported_$Import_ = (IConstructor) _read("prod(label(\"import\",sort(\"EvalCommand\")),[label(\"imported\",sort(\"Import\"))],{})", Factory.Production);
  private static final IConstructor prod__int_$BasicType__lit_int_ = (IConstructor) _read("prod(label(\"int\",sort(\"BasicType\")),[lit(\"int\")],{})", Factory.Production);
  private static final IConstructor prod__join_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit_join_$layouts_LAYOUTLIST_rhs_$Expression__assoc__left = (IConstructor) _read("prod(label(\"join\",sort(\"Expression\")),[label(\"lhs\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"join\"),layouts(\"LAYOUTLIST\"),label(\"rhs\",sort(\"Expression\"))],{assoc(left())})", Factory.Production);
  private static final IConstructor prod__labeled_$Prod__modifiers_iter_star_seps__$ProdModifier__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_name_$Name_$layouts_LAYOUTLIST_lit___58_$layouts_LAYOUTLIST_args_iter_star_seps__$Sym__$layouts_LAYOUTLIST_ = (IConstructor) _read("prod(label(\"labeled\",sort(\"Prod\")),[label(\"modifiers\",\\iter-star-seps(sort(\"ProdModifier\"),[layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),label(\"name\",lex(\"Name\")),layouts(\"LAYOUTLIST\"),lit(\":\"),layouts(\"LAYOUTLIST\"),label(\"args\",\\iter-star-seps(sort(\"Sym\"),[layouts(\"LAYOUTLIST\")]))],{})", Factory.Production);
  private static final IConstructor prod__left_$Assoc__lit_left_ = (IConstructor) _read("prod(label(\"left\",sort(\"Assoc\")),[lit(\"left\")],{})", Factory.Production);
  private static final IConstructor prod__modulo_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit_mod_$layouts_LAYOUTLIST_rhs_$Expression__assoc__left = (IConstructor) _read("prod(label(\"modulo\",sort(\"Expression\")),[label(\"lhs\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"mod\"),layouts(\"LAYOUTLIST\"),label(\"rhs\",sort(\"Expression\"))],{assoc(left())})", Factory.Production);
  private static final IConstructor prod__post_$ProtocolTail__post_$PostProtocolChars_ = (IConstructor) _read("prod(label(\"post\",sort(\"ProtocolTail\")),[label(\"post\",lex(\"PostProtocolChars\"))],{})", Factory.Production);
  private static final IConstructor prod__real_$BasicType__lit_real_ = (IConstructor) _read("prod(label(\"real\",sort(\"BasicType\")),[lit(\"real\")],{})", Factory.Production);
  private static final IConstructor prod__reducer_$Expression__lit___40_$layouts_LAYOUTLIST_init_$Expression_$layouts_LAYOUTLIST_lit___124_$layouts_LAYOUTLIST_result_$Expression_$layouts_LAYOUTLIST_lit___124_$layouts_LAYOUTLIST_generators_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_ = (IConstructor) _read("prod(label(\"reducer\",sort(\"Expression\")),[lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"init\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"|\"),layouts(\"LAYOUTLIST\"),label(\"result\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"|\"),layouts(\"LAYOUTLIST\"),label(\"generators\",\\iter-seps(sort(\"Expression\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\")\")],{})", Factory.Production);
  private static final IConstructor prod__solve_$Statement__lit_solve_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_variables_iter_seps__$QualifiedName__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_bound_$Bound_$layouts_LAYOUTLIST_lit___41_$layouts_LAYOUTLIST_body_$Statement__tag__breakable = (IConstructor) _read("prod(label(\"solve\",sort(\"Statement\")),[lit(\"solve\"),layouts(\"LAYOUTLIST\"),lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"variables\",\\iter-seps(sort(\"QualifiedName\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),label(\"bound\",sort(\"Bound\")),layouts(\"LAYOUTLIST\"),lit(\")\"),layouts(\"LAYOUTLIST\"),label(\"body\",conditional(sort(\"Statement\"),{except(\"functionDeclaration\"),except(\"variableDeclaration\")}))],{tag(breakable())})", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_test_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"test\")],{})", Factory.Production);
  private static final IConstructor prod__$BooleanLiteral__lit_false_ = (IConstructor) _read("prod(lex(\"BooleanLiteral\"),[lit(\"false\")],{})", Factory.Production);
  private static final IConstructor prod__$DatePart__char_class___range__48_57_char_class___range__48_57_char_class___range__48_57_char_class___range__48_57_char_class___range__48_49_char_class___range__48_57_char_class___range__48_51_char_class___range__48_57_ = (IConstructor) _read("prod(lex(\"DatePart\"),[\\char-class([range(48,57)]),\\char-class([range(48,57)]),\\char-class([range(48,57)]),\\char-class([range(48,57)]),\\char-class([range(48,49)]),\\char-class([range(48,57)]),\\char-class([range(48,51)]),\\char-class([range(48,57)])],{})", Factory.Production);
  private static final IConstructor prod__$RegExp__lit___60_$Name_lit___62_ = (IConstructor) _read("prod(lex(\"RegExp\"),[lit(\"\\<\"),lex(\"Name\"),lit(\"\\>\")],{})", Factory.Production);
  private static final IConstructor prod__$URLChars__iter_star__char_class___range__0_8_range__11_12_range__14_31_range__33_59_range__61_123_range__125_16777215_ = (IConstructor) _read("prod(lex(\"URLChars\"),[\\iter-star(\\char-class([range(0,8),range(11,12),range(14,31),range(33,59),range(61,123),range(125,16777215)]))],{})", Factory.Production);
  private static final IConstructor regular__iter_star_seps__$FunctionModifier__$layouts_LAYOUTLIST = (IConstructor) _read("regular(\\iter-star-seps(sort(\"FunctionModifier\"),[layouts(\"LAYOUTLIST\")]))", Factory.Production);
  private static final IConstructor regular__iter_seps__$Statement__$layouts_LAYOUTLIST = (IConstructor) _read("regular(\\iter-seps(sort(\"Statement\"),[layouts(\"LAYOUTLIST\")]))", Factory.Production);
  private static final IConstructor prod__lit___33_61__char_class___range__33_33_char_class___range__61_61_ = (IConstructor) _read("prod(lit(\"!=\"),[\\char-class([range(33,33)]),\\char-class([range(61,61)])],{})", Factory.Production);
  private static final IConstructor prod__lit___46_46_46__char_class___range__46_46_char_class___range__46_46_char_class___range__46_46_ = (IConstructor) _read("prod(lit(\"...\"),[\\char-class([range(46,46)]),\\char-class([range(46,46)]),\\char-class([range(46,46)])],{})", Factory.Production);
  private static final IConstructor prod__lit_anno__char_class___range__97_97_char_class___range__110_110_char_class___range__110_110_char_class___range__111_111_ = (IConstructor) _read("prod(lit(\"anno\"),[\\char-class([range(97,97)]),\\char-class([range(110,110)]),\\char-class([range(110,110)]),\\char-class([range(111,111)])],{})", Factory.Production);
  private static final IConstructor prod__appendAfter_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___60_60_$layouts_LAYOUTLIST_rhs_$Expression__assoc__left = (IConstructor) _read("prod(label(\"appendAfter\",sort(\"Expression\")),[label(\"lhs\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),conditional(lit(\"\\<\\<\"),{\\not-follow(lit(\"=\"))}),layouts(\"LAYOUTLIST\"),label(\"rhs\",sort(\"Expression\"))],{assoc(left())})", Factory.Production);
  private static final IConstructor prod__asType_$Expression__lit___91_$layouts_LAYOUTLIST_type_$Type_$layouts_LAYOUTLIST_lit___93_$layouts_LAYOUTLIST_argument_$Expression_ = (IConstructor) _read("prod(label(\"asType\",sort(\"Expression\")),[lit(\"[\"),layouts(\"LAYOUTLIST\"),label(\"type\",sort(\"Type\")),layouts(\"LAYOUTLIST\"),lit(\"]\"),layouts(\"LAYOUTLIST\"),label(\"argument\",conditional(sort(\"Expression\"),{except(\"noMatch\"),except(\"match\")}))],{})", Factory.Production);
  private static final IConstructor prod__dateAndTimeLiteral_$DateTimeLiteral__dateAndTime_$DateAndTime_ = (IConstructor) _read("prod(label(\"dateAndTimeLiteral\",sort(\"DateTimeLiteral\")),[label(\"dateAndTime\",lex(\"DateAndTime\"))],{})", Factory.Production);
  private static final IConstructor prod__default_$Bound__lit___59_$layouts_LAYOUTLIST_expression_$Expression_ = (IConstructor) _read("prod(label(\"default\",sort(\"Bound\")),[lit(\";\"),layouts(\"LAYOUTLIST\"),label(\"expression\",sort(\"Expression\"))],{})", Factory.Production);
  private static final IConstructor prod__default_$FunctionBody__lit___123_$layouts_LAYOUTLIST_statements_iter_star_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_ = (IConstructor) _read("prod(label(\"default\",sort(\"FunctionBody\")),[lit(\"{\"),layouts(\"LAYOUTLIST\"),label(\"statements\",\\iter-star-seps(sort(\"Statement\"),[layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\"}\")],{})", Factory.Production);
  private static final IConstructor prod__default_$Module__header_$Header_$layouts_LAYOUTLIST_body_$Body_ = (IConstructor) _read("prod(label(\"default\",sort(\"Module\")),[label(\"header\",sort(\"Header\")),layouts(\"LAYOUTLIST\"),label(\"body\",sort(\"Body\"))],{})", Factory.Production);
  private static final IConstructor prod__endOfLine_$Sym__symbol_$Sym_$layouts_LAYOUTLIST_lit___36_ = (IConstructor) _read("prod(label(\"endOfLine\",sort(\"Sym\")),[label(\"symbol\",sort(\"Sym\")),layouts(\"LAYOUTLIST\"),lit(\"$\")],{})", Factory.Production);
  private static final IConstructor prod__interpolated_$StringLiteral__pre_$PreStringChars_$layouts_LAYOUTLIST_expression_$Expression_$layouts_LAYOUTLIST_tail_$StringTail_ = (IConstructor) _read("prod(label(\"interpolated\",sort(\"StringLiteral\")),[label(\"pre\",lex(\"PreStringChars\")),layouts(\"LAYOUTLIST\"),label(\"expression\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),label(\"tail\",sort(\"StringTail\"))],{})", Factory.Production);
  private static final IConstructor prod__lessThan_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___60_$layouts_LAYOUTLIST_rhs_$Expression__assoc__non_assoc = (IConstructor) _read("prod(label(\"lessThan\",sort(\"Expression\")),[label(\"lhs\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),conditional(lit(\"\\<\"),{\\not-follow(lit(\"-\"))}),layouts(\"LAYOUTLIST\"),label(\"rhs\",sort(\"Expression\"))],{assoc(\\non-assoc())})", Factory.Production);
  private static final IConstructor prod__module_$Kind__lit_module_ = (IConstructor) _read("prod(label(\"module\",sort(\"Kind\")),[lit(\"module\")],{})", Factory.Production);
  private static final IConstructor prod__nonInterpolated_$StringLiteral__constant_$StringConstant_ = (IConstructor) _read("prod(label(\"nonInterpolated\",sort(\"StringLiteral\")),[label(\"constant\",lex(\"StringConstant\"))],{})", Factory.Production);
  private static final IConstructor prod__notIn_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit_notin_$layouts_LAYOUTLIST_rhs_$Expression__assoc__non_assoc = (IConstructor) _read("prod(label(\"notIn\",sort(\"Expression\")),[label(\"lhs\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"notin\"),layouts(\"LAYOUTLIST\"),label(\"rhs\",sort(\"Expression\"))],{assoc(\\non-assoc())})", Factory.Production);
  private static final IConstructor prod__public_$Visibility__lit_public_ = (IConstructor) _read("prod(label(\"public\",sort(\"Visibility\")),[lit(\"public\")],{})", Factory.Production);
  private static final IConstructor prod__lit_top_down__char_class___range__116_116_char_class___range__111_111_char_class___range__112_112_char_class___range__45_45_char_class___range__100_100_char_class___range__111_111_char_class___range__119_119_char_class___range__110_110_ = (IConstructor) _read("prod(lit(\"top-down\"),[\\char-class([range(116,116)]),\\char-class([range(111,111)]),\\char-class([range(112,112)]),\\char-class([range(45,45)]),\\char-class([range(100,100)]),\\char-class([range(111,111)]),\\char-class([range(119,119)]),\\char-class([range(110,110)])],{})", Factory.Production);
  private static final IConstructor prod__lit_top_down_break__char_class___range__116_116_char_class___range__111_111_char_class___range__112_112_char_class___range__45_45_char_class___range__100_100_char_class___range__111_111_char_class___range__119_119_char_class___range__110_110_char_class___range__45_45_char_class___range__98_98_char_class___range__114_114_char_class___range__101_101_char_class___range__97_97_char_class___range__107_107_ = (IConstructor) _read("prod(lit(\"top-down-break\"),[\\char-class([range(116,116)]),\\char-class([range(111,111)]),\\char-class([range(112,112)]),\\char-class([range(45,45)]),\\char-class([range(100,100)]),\\char-class([range(111,111)]),\\char-class([range(119,119)]),\\char-class([range(110,110)]),\\char-class([range(45,45)]),\\char-class([range(98,98)]),\\char-class([range(114,114)]),\\char-class([range(101,101)]),\\char-class([range(97,97)]),\\char-class([range(107,107)])],{})", Factory.Production);
  private static final IConstructor prod__transitiveReflexiveClosure_$Expression__argument_$Expression_$layouts_LAYOUTLIST_lit___42_ = (IConstructor) _read("prod(label(\"transitiveReflexiveClosure\",sort(\"Expression\")),[label(\"argument\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),conditional(lit(\"*\"),{\\not-follow(lit(\"=\"))})],{})", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_real_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"real\")],{})", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_break_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"break\")],{})", Factory.Production);
  private static final IConstructor prod__$PreProtocolChars__lit___124_$URLChars_lit___60_ = (IConstructor) _read("prod(lex(\"PreProtocolChars\"),[lit(\"|\"),lex(\"URLChars\"),lit(\"\\<\")],{})", Factory.Production);
  private static final IConstructor prod__$RegExp__char_class___range__92_92_char_class___range__47_47_range__60_60_range__62_62_range__92_92_ = (IConstructor) _read("prod(lex(\"RegExp\"),[\\char-class([range(92,92)]),\\char-class([range(47,47),range(60,60),range(62,62),range(92,92)])],{})", Factory.Production);
  private static final IConstructor prod__$StringConstant__lit___34_iter_star__$StringCharacter_lit___34__tag__category___67_111_110_115_116_97_110_116 = (IConstructor) _read("prod(lex(\"StringConstant\"),[lit(\"\\\"\"),\\iter-star(lex(\"StringCharacter\")),lit(\"\\\"\")],{tag(category(\"Constant\"))})", Factory.Production);
  private static final IConstructor prod__bottomUpBreak_$Strategy__lit_bottom_up_break_ = (IConstructor) _read("prod(label(\"bottomUpBreak\",sort(\"Strategy\")),[lit(\"bottom-up-break\")],{})", Factory.Production);
  private static final IConstructor prod__concrete_$Pattern__concrete_$Concrete_ = (IConstructor) _read("prod(label(\"concrete\",sort(\"Pattern\")),[label(\"concrete\",sort(\"Concrete\"))],{})", Factory.Production);
  private static final IConstructor prod__default_$FunctionDeclaration__tags_$Tags_$layouts_LAYOUTLIST_visibility_$Visibility_$layouts_LAYOUTLIST_signature_$Signature_$layouts_LAYOUTLIST_body_$FunctionBody__tag__Foldable = (IConstructor) _read("prod(label(\"default\",sort(\"FunctionDeclaration\")),[label(\"tags\",sort(\"Tags\")),layouts(\"LAYOUTLIST\"),label(\"visibility\",sort(\"Visibility\")),layouts(\"LAYOUTLIST\"),label(\"signature\",sort(\"Signature\")),layouts(\"LAYOUTLIST\"),label(\"body\",sort(\"FunctionBody\"))],{tag(Foldable())})", Factory.Production);
  private static final IConstructor prod__default_$ModuleParameters__lit___91_$layouts_LAYOUTLIST_parameters_iter_seps__$TypeVar__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___93_ = (IConstructor) _read("prod(label(\"default\",sort(\"ModuleParameters\")),[lit(\"[\"),layouts(\"LAYOUTLIST\"),label(\"parameters\",\\iter-seps(sort(\"TypeVar\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\"]\")],{})", Factory.Production);
  private static final IConstructor prod__expression_$Command__expression_$Expression_ = (IConstructor) _read("prod(label(\"expression\",sort(\"Command\")),[label(\"expression\",conditional(sort(\"Expression\"),{except(\"nonEmptyBlock\")}))],{})", Factory.Production);
  private static final IConstructor prod__ifDefinedOrDefault_$Assignable__receiver_$Assignable_$layouts_LAYOUTLIST_lit___63_$layouts_LAYOUTLIST_defaultExpression_$Expression_ = (IConstructor) _read("prod(label(\"ifDefinedOrDefault\",sort(\"Assignable\")),[label(\"receiver\",sort(\"Assignable\")),layouts(\"LAYOUTLIST\"),lit(\"?\"),layouts(\"LAYOUTLIST\"),label(\"defaultExpression\",sort(\"Expression\"))],{})", Factory.Production);
  private static final IConstructor prod__lit_innermost__char_class___range__105_105_char_class___range__110_110_char_class___range__110_110_char_class___range__101_101_char_class___range__114_114_char_class___range__109_109_char_class___range__111_111_char_class___range__115_115_char_class___range__116_116_ = (IConstructor) _read("prod(lit(\"innermost\"),[\\char-class([range(105,105)]),\\char-class([range(110,110)]),\\char-class([range(110,110)]),\\char-class([range(101,101)]),\\char-class([range(114,114)]),\\char-class([range(109,109)]),\\char-class([range(111,111)]),\\char-class([range(115,115)]),\\char-class([range(116,116)])],{})", Factory.Production);
  private static final IConstructor prod__iter_$Sym__symbol_$Sym_$layouts_LAYOUTLIST_lit___43_ = (IConstructor) _read("prod(label(\"iter\",sort(\"Sym\")),[label(\"symbol\",sort(\"Sym\")),layouts(\"LAYOUTLIST\"),lit(\"+\")],{})", Factory.Production);
  private static final IConstructor prod__list_$Commands__commands_iter_seps__$EvalCommand__$layouts_LAYOUTLIST_ = (IConstructor) _read("prod(label(\"list\",sort(\"Commands\")),[label(\"commands\",\\iter-seps(sort(\"EvalCommand\"),[layouts(\"LAYOUTLIST\")]))],{})", Factory.Production);
  private static final IConstructor prod__lit_notin__char_class___range__110_110_char_class___range__111_111_char_class___range__116_116_char_class___range__105_105_char_class___range__110_110_ = (IConstructor) _read("prod(lit(\"notin\"),[\\char-class([range(110,110)]),\\char-class([range(111,111)]),\\char-class([range(116,116)]),\\char-class([range(105,105)]),\\char-class([range(110,110)])],{})", Factory.Production);
  private static final IConstructor prod__lit_one__char_class___range__111_111_char_class___range__110_110_char_class___range__101_101_ = (IConstructor) _read("prod(lit(\"one\"),[\\char-class([range(111,111)]),\\char-class([range(110,110)]),\\char-class([range(101,101)])],{})", Factory.Production);
  private static final IConstructor prod__optional_$Sym__symbol_$Sym_$layouts_LAYOUTLIST_lit___63_ = (IConstructor) _read("prod(label(\"optional\",sort(\"Sym\")),[label(\"symbol\",sort(\"Sym\")),layouts(\"LAYOUTLIST\"),lit(\"?\")],{})", Factory.Production);
  private static final IConstructor prod__test_$ShellCommand__lit_test_ = (IConstructor) _read("prod(label(\"test\",sort(\"ShellCommand\")),[lit(\"test\")],{})", Factory.Production);
  private static final IConstructor prod__toplevels_$Body__toplevels_iter_star_seps__$Toplevel__$layouts_LAYOUTLIST_ = (IConstructor) _read("prod(label(\"toplevels\",sort(\"Body\")),[label(\"toplevels\",\\iter-star-seps(sort(\"Toplevel\"),[layouts(\"LAYOUTLIST\")]))],{})", Factory.Production);
  private static final IConstructor prod__typed_$Concrete__lit___40_$layouts_LAYOUTLIST_$Sym_$layouts_LAYOUTLIST_lit___41_$layouts_LAYOUTLIST_lit___96_$layouts_LAYOUTLIST_parts_iter_star_seps__$ConcretePart__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___96_ = (IConstructor) _read("prod(label(\"typed\",sort(\"Concrete\")),[lit(\"(\"),layouts(\"LAYOUTLIST\"),sort(\"Sym\"),layouts(\"LAYOUTLIST\"),lit(\")\"),layouts(\"LAYOUTLIST\"),lit(\"`\"),layouts(\"LAYOUTLIST\"),label(\"parts\",\\iter-star-seps(lex(\"ConcretePart\"),[layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\"`\")],{})", Factory.Production);
  private static final IConstructor prod__lit_view__char_class___range__118_118_char_class___range__105_105_char_class___range__101_101_char_class___range__119_119_ = (IConstructor) _read("prod(lit(\"view\"),[\\char-class([range(118,118)]),\\char-class([range(105,105)]),\\char-class([range(101,101)]),\\char-class([range(119,119)])],{})", Factory.Production);
  private static final IConstructor prod__view_$Kind__lit_view_ = (IConstructor) _read("prod(label(\"view\",sort(\"Kind\")),[lit(\"view\")],{})", Factory.Production);
  private static final IConstructor prod__lit_while__char_class___range__119_119_char_class___range__104_104_char_class___range__105_105_char_class___range__108_108_char_class___range__101_101_ = (IConstructor) _read("prod(lit(\"while\"),[\\char-class([range(119,119)]),\\char-class([range(104,104)]),\\char-class([range(105,105)]),\\char-class([range(108,108)]),\\char-class([range(101,101)])],{})", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_lrel_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"lrel\")],{})", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_join_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"join\")],{})", Factory.Production);
  private static final IConstructor prod__$CaseInsensitiveStringConstant__lit___39_iter_star__$StringCharacter_lit___39__tag__category___67_111_110_115_116_97_110_116 = (IConstructor) _read("prod(lex(\"CaseInsensitiveStringConstant\"),[lit(\"\\'\\\\\"),\\iter-star(lex(\"StringCharacter\")),lit(\"\\'\\\\\")],{tag(category(\"Constant\"))})", Factory.Production);
  private static final IConstructor prod__$Char__lit___92_char_class___range__32_32_range__34_34_range__39_39_range__45_45_range__60_60_range__62_62_range__91_93_range__98_98_range__102_102_range__110_110_range__114_114_range__116_116__tag__category___67_111_110_115_116_97_110_116 = (IConstructor) _read("prod(lex(\"Char\"),[lit(\"\\\\\"),\\char-class([range(32,32),range(34,34),range(39,39),range(45,45),range(60,60),range(62,62),range(91,93),range(98,98),range(102,102),range(110,110),range(114,114),range(116,116)])],{tag(category(\"Constant\"))})", Factory.Production);
  private static final IConstructor regular__iter_seps__$Renaming__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST = (IConstructor) _read("regular(\\iter-seps(sort(\"Renaming\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")]))", Factory.Production);
  private static final IConstructor regular__iter_seps__$TypeArg__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST = (IConstructor) _read("regular(\\iter-seps(sort(\"TypeArg\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")]))", Factory.Production);
  private static final IConstructor prod__absent_$Start__ = (IConstructor) _read("prod(label(\"absent\",sort(\"Start\")),[],{})", Factory.Production);
  private static final IConstructor regular__opt__seq___char_class___range__48_57_opt__char_class___range__48_57 = (IConstructor) _read("regular(opt(seq([\\char-class([range(48,57)]),opt(\\char-class([range(48,57)]))])))", Factory.Production);
  private static final IConstructor prod__bag_$BasicType__lit_bag_ = (IConstructor) _read("prod(label(\"bag\",sort(\"BasicType\")),[lit(\"bag\")],{})", Factory.Production);
  private static final IConstructor prod__lit_bool__char_class___range__98_98_char_class___range__111_111_char_class___range__111_111_char_class___range__108_108_ = (IConstructor) _read("prod(lit(\"bool\"),[\\char-class([range(98,98)]),\\char-class([range(111,111)]),\\char-class([range(111,111)]),\\char-class([range(108,108)])],{})", Factory.Production);
  private static final IConstructor prod__continue_$Statement__lit_continue_$layouts_LAYOUTLIST_target_$Target_$layouts_LAYOUTLIST_lit___59__tag__breakable = (IConstructor) _read("prod(label(\"continue\",sort(\"Statement\")),[lit(\"continue\"),layouts(\"LAYOUTLIST\"),label(\"target\",sort(\"Target\")),layouts(\"LAYOUTLIST\"),lit(\";\")],{tag(breakable())})", Factory.Production);
  private static final IConstructor prod__lit_default__char_class___range__100_100_char_class___range__101_101_char_class___range__102_102_char_class___range__97_97_char_class___range__117_117_char_class___range__108_108_char_class___range__116_116_ = (IConstructor) _read("prod(lit(\"default\"),[\\char-class([range(100,100)]),\\char-class([range(101,101)]),\\char-class([range(102,102)]),\\char-class([range(97,97)]),\\char-class([range(117,117)]),\\char-class([range(108,108)]),\\char-class([range(116,116)])],{})", Factory.Production);
  private static final IConstructor prod__doWhile_$Statement__label_$Label_$layouts_LAYOUTLIST_lit_do_$layouts_LAYOUTLIST_body_$Statement_$layouts_LAYOUTLIST_lit_while_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_condition_$Expression_$layouts_LAYOUTLIST_lit___41_$layouts_LAYOUTLIST_lit___59__tag__breakable = (IConstructor) _read("prod(label(\"doWhile\",sort(\"Statement\")),[label(\"label\",sort(\"Label\")),layouts(\"LAYOUTLIST\"),lit(\"do\"),layouts(\"LAYOUTLIST\"),label(\"body\",sort(\"Statement\")),layouts(\"LAYOUTLIST\"),lit(\"while\"),layouts(\"LAYOUTLIST\"),lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"condition\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\")\"),layouts(\"LAYOUTLIST\"),lit(\";\")],{tag(breakable())})", Factory.Production);
  private static final IConstructor prod__first_$Prod__lhs_$Prod_$layouts_LAYOUTLIST_lit___62_$layouts_LAYOUTLIST_rhs_$Prod__assoc__left = (IConstructor) _read("prod(label(\"first\",sort(\"Prod\")),[label(\"lhs\",sort(\"Prod\")),layouts(\"LAYOUTLIST\"),conditional(lit(\"\\>\"),{\\not-follow(lit(\"\\>\"))}),layouts(\"LAYOUTLIST\"),label(\"rhs\",sort(\"Prod\"))],{assoc(left())})", Factory.Production);
  private static final IConstructor prod__for_$Statement__label_$Label_$layouts_LAYOUTLIST_lit_for_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_generators_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_$layouts_LAYOUTLIST_body_$Statement__tag__breakable___123_103_101_110_101_114_97_116_111_114_115_125_tag__breakable = (IConstructor) _read("prod(label(\"for\",sort(\"Statement\")),[label(\"label\",sort(\"Label\")),layouts(\"LAYOUTLIST\"),lit(\"for\"),layouts(\"LAYOUTLIST\"),lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"generators\",\\iter-seps(sort(\"Expression\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\")\"),layouts(\"LAYOUTLIST\"),label(\"body\",sort(\"Statement\"))],{tag(breakable(\"{generators}\")),tag(breakable())})", Factory.Production);
  private static final IConstructor prod__getAnnotation_$Expression__expression_$Expression_$layouts_LAYOUTLIST_lit___64_$layouts_LAYOUTLIST_name_$Name_ = (IConstructor) _read("prod(label(\"getAnnotation\",sort(\"Expression\")),[label(\"expression\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"@\"),layouts(\"LAYOUTLIST\"),label(\"name\",lex(\"Name\"))],{})", Factory.Production);
  private static final IConstructor prod__ifDefinedOtherwise_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___63_$layouts_LAYOUTLIST_rhs_$Expression__assoc__non_assoc = (IConstructor) _read("prod(label(\"ifDefinedOtherwise\",sort(\"Expression\")),[label(\"lhs\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"?\"),layouts(\"LAYOUTLIST\"),label(\"rhs\",sort(\"Expression\"))],{assoc(\\non-assoc())})", Factory.Production);
  private static final IConstructor prod__literal_$Sym__string_$StringConstant_ = (IConstructor) _read("prod(label(\"literal\",sort(\"Sym\")),[label(\"string\",lex(\"StringConstant\"))],{})", Factory.Production);
  private static final IConstructor prod__noMatch_$Expression__pattern_$Pattern_$layouts_LAYOUTLIST_lit___33_58_61_$layouts_LAYOUTLIST_expression_$Expression__assoc__non_assoc = (IConstructor) _read("prod(label(\"noMatch\",sort(\"Expression\")),[label(\"pattern\",sort(\"Pattern\")),layouts(\"LAYOUTLIST\"),lit(\"!:=\"),layouts(\"LAYOUTLIST\"),label(\"expression\",sort(\"Expression\"))],{assoc(\\non-assoc())})", Factory.Production);
  private static final IConstructor prod__others_$Prod__lit___46_46_46_ = (IConstructor) _read("prod(label(\"others\",sort(\"Prod\")),[lit(\"...\")],{})", Factory.Production);
  private static final IConstructor prod__remainder_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___37_$layouts_LAYOUTLIST_rhs_$Expression__assoc__left = (IConstructor) _read("prod(label(\"remainder\",sort(\"Expression\")),[label(\"lhs\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"%\"),layouts(\"LAYOUTLIST\"),label(\"rhs\",sort(\"Expression\"))],{assoc(left())})", Factory.Production);
  private static final IConstructor prod__string_$Literal__stringLiteral_$StringLiteral_ = (IConstructor) _read("prod(label(\"string\",sort(\"Literal\")),[label(\"stringLiteral\",sort(\"StringLiteral\"))],{})", Factory.Production);
  private static final IConstructor prod__switch_$Statement__label_$Label_$layouts_LAYOUTLIST_lit_switch_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_expression_$Expression_$layouts_LAYOUTLIST_lit___41_$layouts_LAYOUTLIST_lit___123_$layouts_LAYOUTLIST_cases_iter_seps__$Case__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125__tag__breakable = (IConstructor) _read("prod(label(\"switch\",sort(\"Statement\")),[label(\"label\",sort(\"Label\")),layouts(\"LAYOUTLIST\"),lit(\"switch\"),layouts(\"LAYOUTLIST\"),lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"expression\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\")\"),layouts(\"LAYOUTLIST\"),lit(\"{\"),layouts(\"LAYOUTLIST\"),label(\"cases\",\\iter-seps(sort(\"Case\"),[layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\"}\")],{tag(breakable())})", Factory.Production);
  private static final IConstructor prod__transitiveClosure_$Expression__argument_$Expression_$layouts_LAYOUTLIST_lit___43_ = (IConstructor) _read("prod(label(\"transitiveClosure\",sort(\"Expression\")),[label(\"argument\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),conditional(lit(\"+\"),{\\not-follow(lit(\"=\"))})],{})", Factory.Production);
  private static final IConstructor prod__tuple_$BasicType__lit_tuple_ = (IConstructor) _read("prod(label(\"tuple\",sort(\"BasicType\")),[lit(\"tuple\")],{})", Factory.Production);
  private static final IConstructor prod__unequal_$Sym__symbol_$Sym_$layouts_LAYOUTLIST_lit___92_$layouts_LAYOUTLIST_match_$Sym__assoc__left = (IConstructor) _read("prod(label(\"unequal\",sort(\"Sym\")),[label(\"symbol\",sort(\"Sym\")),layouts(\"LAYOUTLIST\"),lit(\"\\\\\"),layouts(\"LAYOUTLIST\"),label(\"match\",sort(\"Sym\"))],{assoc(left())})", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_void_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"void\")],{})", Factory.Production);
  private static final IConstructor prod__$Nonterminal__char_class___range__65_90_iter_star__char_class___range__48_57_range__65_90_range__95_95_range__97_122_ = (IConstructor) _read("prod(lex(\"Nonterminal\"),[conditional(\\char-class([range(65,90)]),{\\not-precede(\\char-class([range(65,90)]))}),conditional(\\iter-star(\\char-class([range(48,57),range(65,90),range(95,95),range(97,122)])),{\\not-follow(\\char-class([range(48,57),range(65,90),range(95,95),range(97,122)])),delete(sort(\"RascalReservedKeywords\"))})],{})", Factory.Production);
  private static final IConstructor regular__iter_star_seps__$Pattern__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST = (IConstructor) _read("regular(\\iter-star-seps(sort(\"Pattern\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")]))", Factory.Production);
  private static final IConstructor regular__opt__char_class___range__68_68_range__70_70_range__100_100_range__102_102 = (IConstructor) _read("regular(opt(\\char-class([range(68,68),range(70,70),range(100,100),range(102,102)])))", Factory.Production);
  private static final IConstructor prod__notFollow_$Sym__symbol_$Sym_$layouts_LAYOUTLIST_lit___33_62_62_$layouts_LAYOUTLIST_match_$Sym__assoc__left = (IConstructor) _read("prod(label(\"notFollow\",sort(\"Sym\")),[label(\"symbol\",sort(\"Sym\")),layouts(\"LAYOUTLIST\"),lit(\"!\\>\\>\"),layouts(\"LAYOUTLIST\"),label(\"match\",sort(\"Sym\"))],{assoc(left())})", Factory.Production);
  private static final IConstructor regular__iter_seps__$Field__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST = (IConstructor) _read("regular(\\iter-seps(sort(\"Field\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")]))", Factory.Production);
  private static final IConstructor prod__lit___60_61_61_62__char_class___range__60_60_char_class___range__61_61_char_class___range__61_61_char_class___range__62_62_ = (IConstructor) _read("prod(lit(\"\\<==\\>\"),[\\char-class([range(60,60)]),\\char-class([range(61,61)]),\\char-class([range(61,61)]),\\char-class([range(62,62)])],{})", Factory.Production);
  private static final IConstructor prod__lit___61_62__char_class___range__61_61_char_class___range__62_62_ = (IConstructor) _read("prod(lit(\"=\\>\"),[\\char-class([range(61,61)]),\\char-class([range(62,62)])],{})", Factory.Production);
  private static final IConstructor prod__boolean_$Literal__booleanLiteral_$BooleanLiteral_ = (IConstructor) _read("prod(label(\"boolean\",sort(\"Literal\")),[label(\"booleanLiteral\",lex(\"BooleanLiteral\"))],{})", Factory.Production);
  private static final IConstructor prod__data_$Kind__lit_data_ = (IConstructor) _read("prod(label(\"data\",sort(\"Kind\")),[lit(\"data\")],{})", Factory.Production);
  private static final IConstructor prod__default_$Catch__lit_catch_$layouts_LAYOUTLIST_lit___58_$layouts_LAYOUTLIST_body_$Statement_ = (IConstructor) _read("prod(label(\"default\",sort(\"Catch\")),[lit(\"catch\"),layouts(\"LAYOUTLIST\"),lit(\":\"),layouts(\"LAYOUTLIST\"),label(\"body\",sort(\"Statement\"))],{})", Factory.Production);
  private static final IConstructor prod__edit_$ShellCommand__lit_edit_$layouts_LAYOUTLIST_name_$QualifiedName_ = (IConstructor) _read("prod(label(\"edit\",sort(\"ShellCommand\")),[lit(\"edit\"),layouts(\"LAYOUTLIST\"),label(\"name\",sort(\"QualifiedName\"))],{})", Factory.Production);
  private static final IConstructor prod__except_$Sym__symbol_$Sym_$layouts_LAYOUTLIST_lit___33_$layouts_LAYOUTLIST_label_$NonterminalLabel_ = (IConstructor) _read("prod(label(\"except\",sort(\"Sym\")),[label(\"symbol\",sort(\"Sym\")),layouts(\"LAYOUTLIST\"),lit(\"!\"),layouts(\"LAYOUTLIST\"),label(\"label\",lex(\"NonterminalLabel\"))],{})", Factory.Production);
  private static final IConstructor prod__precede_$Sym__match_$Sym_$layouts_LAYOUTLIST_lit___60_60_$layouts_LAYOUTLIST_symbol_$Sym__assoc__right = (IConstructor) _read("prod(label(\"precede\",sort(\"Sym\")),[label(\"match\",sort(\"Sym\")),layouts(\"LAYOUTLIST\"),lit(\"\\<\\<\"),layouts(\"LAYOUTLIST\"),label(\"symbol\",sort(\"Sym\"))],{assoc(right())})", Factory.Production);
  private static final IConstructor prod__real_$Literal__realLiteral_$RealLiteral_ = (IConstructor) _read("prod(label(\"real\",sort(\"Literal\")),[label(\"realLiteral\",lex(\"RealLiteral\"))],{})", Factory.Production);
  private static final IConstructor prod__splice_$Expression__lit___42_$layouts_LAYOUTLIST_argument_$Expression__assoc__non_assoc = (IConstructor) _read("prod(label(\"splice\",sort(\"Expression\")),[lit(\"*\"),layouts(\"LAYOUTLIST\"),label(\"argument\",sort(\"Expression\"))],{assoc(\\non-assoc())})", Factory.Production);
  private static final IConstructor prod__splicePlus_$Pattern__lit___43_$layouts_LAYOUTLIST_argument_$Pattern_ = (IConstructor) _read("prod(label(\"splicePlus\",sort(\"Pattern\")),[lit(\"+\"),layouts(\"LAYOUTLIST\"),label(\"argument\",sort(\"Pattern\"))],{})", Factory.Production);
  private static final IConstructor prod__lit_str__char_class___range__115_115_char_class___range__116_116_char_class___range__114_114_ = (IConstructor) _read("prod(lit(\"str\"),[\\char-class([range(115,115)]),\\char-class([range(116,116)]),\\char-class([range(114,114)])],{})", Factory.Production);
  private static final IConstructor prod__varArgs_$Parameters__lit___40_$layouts_LAYOUTLIST_formals_$Formals_$layouts_LAYOUTLIST_lit___46_46_46_$layouts_LAYOUTLIST_lit___41_ = (IConstructor) _read("prod(label(\"varArgs\",sort(\"Parameters\")),[lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"formals\",sort(\"Formals\")),layouts(\"LAYOUTLIST\"),lit(\"...\"),layouts(\"LAYOUTLIST\"),lit(\")\")],{})", Factory.Production);
  private static final IConstructor prod__lit_variable__char_class___range__118_118_char_class___range__97_97_char_class___range__114_114_char_class___range__105_105_char_class___range__97_97_char_class___range__98_98_char_class___range__108_108_char_class___range__101_101_ = (IConstructor) _read("prod(lit(\"variable\"),[\\char-class([range(118,118)]),\\char-class([range(97,97)]),\\char-class([range(114,114)]),\\char-class([range(105,105)]),\\char-class([range(97,97)]),\\char-class([range(98,98)]),\\char-class([range(108,108)]),\\char-class([range(101,101)])],{})", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_solve_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"solve\")],{})", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_fail_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"fail\")],{})", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_default_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"default\")],{})", Factory.Production);
  private static final IConstructor prod__$NamedRegExp__char_class___range__92_92_char_class___range__47_47_range__60_60_range__62_62_range__92_92_ = (IConstructor) _read("prod(lex(\"NamedRegExp\"),[\\char-class([range(92,92)]),\\char-class([range(47,47),range(60,60),range(62,62),range(92,92)])],{})", Factory.Production);
  private static final IConstructor prod__$NonterminalLabel__char_class___range__97_122_iter_star__char_class___range__48_57_range__65_90_range__95_95_range__97_122_ = (IConstructor) _read("prod(lex(\"NonterminalLabel\"),[\\char-class([range(97,122)]),conditional(\\iter-star(\\char-class([range(48,57),range(65,90),range(95,95),range(97,122)])),{\\not-follow(\\char-class([range(48,57),range(65,90),range(95,95),range(97,122)]))})],{})", Factory.Production);
  private static final IConstructor prod__$RationalLiteral__char_class___range__49_57_iter_star__char_class___range__48_57_char_class___range__114_114_char_class___range__48_57_iter_star__char_class___range__48_57_ = (IConstructor) _read("prod(lex(\"RationalLiteral\"),[\\char-class([range(49,57)]),\\iter-star(\\char-class([range(48,57)])),\\char-class([range(114,114)]),\\char-class([range(48,57)]),conditional(\\iter-star(\\char-class([range(48,57)])),{\\not-follow(\\char-class([range(48,57),range(65,90),range(95,95),range(97,122)]))})],{})", Factory.Production);
  private static final IConstructor prod__$TimeZonePart__lit_Z_ = (IConstructor) _read("prod(lex(\"TimeZonePart\"),[lit(\"Z\")],{})", Factory.Production);
  private static final IConstructor regular__iter_star__char_class___range__48_57_range__65_90_range__95_95_range__97_122 = (IConstructor) _read("regular(\\iter-star(\\char-class([range(48,57),range(65,90),range(95,95),range(97,122)])))", Factory.Production);
  private static final IConstructor prod__lit___61_61__char_class___range__61_61_char_class___range__61_61_ = (IConstructor) _read("prod(lit(\"==\"),[\\char-class([range(61,61)]),\\char-class([range(61,61)])],{})", Factory.Production);
  private static final IConstructor prod__lit___62_61__char_class___range__62_62_char_class___range__61_61_ = (IConstructor) _read("prod(lit(\"\\>=\"),[\\char-class([range(62,62)]),\\char-class([range(61,61)])],{})", Factory.Production);
  private static final IConstructor prod__lit_any__char_class___range__97_97_char_class___range__110_110_char_class___range__121_121_ = (IConstructor) _read("prod(lit(\"any\"),[\\char-class([range(97,97)]),\\char-class([range(110,110)]),\\char-class([range(121,121)])],{})", Factory.Production);
  private static final IConstructor prod__conditional_$Replacement__replacementExpression_$Expression_$layouts_LAYOUTLIST_lit_when_$layouts_LAYOUTLIST_conditions_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_ = (IConstructor) _read("prod(label(\"conditional\",sort(\"Replacement\")),[label(\"replacementExpression\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"when\"),layouts(\"LAYOUTLIST\"),label(\"conditions\",\\iter-seps(sort(\"Expression\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")]))],{})", Factory.Production);
  private static final IConstructor prod__default_$Tag__lit___64_$layouts_LAYOUTLIST_name_$Name_$layouts_LAYOUTLIST_contents_$TagString__tag__Folded_tag__category___67_111_109_109_101_110_116 = (IConstructor) _read("prod(label(\"default\",sort(\"Tag\")),[lit(\"@\"),layouts(\"LAYOUTLIST\"),label(\"name\",lex(\"Name\")),layouts(\"LAYOUTLIST\"),label(\"contents\",lex(\"TagString\"))],{tag(Folded()),tag(category(\"Comment\"))})", Factory.Production);
  private static final IConstructor prod__enumerator_$Expression__pattern_$Pattern_$layouts_LAYOUTLIST_lit___60_45_$layouts_LAYOUTLIST_expression_$Expression__assoc__non_assoc = (IConstructor) _read("prod(label(\"enumerator\",sort(\"Expression\")),[label(\"pattern\",sort(\"Pattern\")),layouts(\"LAYOUTLIST\"),lit(\"\\<-\"),layouts(\"LAYOUTLIST\"),label(\"expression\",sort(\"Expression\"))],{assoc(\\non-assoc())})", Factory.Production);
  private static final IConstructor prod__help_$ShellCommand__lit_help_ = (IConstructor) _read("prod(label(\"help\",sort(\"ShellCommand\")),[lit(\"help\")],{})", Factory.Production);
  private static final IConstructor prod__intersection_$Assignment__lit___38_61_ = (IConstructor) _read("prod(label(\"intersection\",sort(\"Assignment\")),[lit(\"&=\")],{})", Factory.Production);
  private static final IConstructor prod__lit_join__char_class___range__106_106_char_class___range__111_111_char_class___range__105_105_char_class___range__110_110_ = (IConstructor) _read("prod(lit(\"join\"),[\\char-class([range(106,106)]),\\char-class([range(111,111)]),\\char-class([range(105,105)]),\\char-class([range(110,110)])],{})", Factory.Production);
  private static final IConstructor prod__language_$SyntaxDefinition__start_$Start_$layouts_LAYOUTLIST_lit_syntax_$layouts_LAYOUTLIST_defined_$Sym_$layouts_LAYOUTLIST_lit___61_$layouts_LAYOUTLIST_production_$Prod_$layouts_LAYOUTLIST_lit___59__tag__Foldable = (IConstructor) _read("prod(label(\"language\",sort(\"SyntaxDefinition\")),[label(\"start\",sort(\"Start\")),layouts(\"LAYOUTLIST\"),lit(\"syntax\"),layouts(\"LAYOUTLIST\"),label(\"defined\",sort(\"Sym\")),layouts(\"LAYOUTLIST\"),lit(\"=\"),layouts(\"LAYOUTLIST\"),label(\"production\",sort(\"Prod\")),layouts(\"LAYOUTLIST\"),lit(\";\")],{tag(Foldable())})", Factory.Production);
  private static final IConstructor prod__lit_non_assoc__char_class___range__110_110_char_class___range__111_111_char_class___range__110_110_char_class___range__45_45_char_class___range__97_97_char_class___range__115_115_char_class___range__115_115_char_class___range__111_111_char_class___range__99_99_ = (IConstructor) _read("prod(lit(\"non-assoc\"),[\\char-class([range(110,110)]),\\char-class([range(111,111)]),\\char-class([range(110,110)]),\\char-class([range(45,45)]),\\char-class([range(97,97)]),\\char-class([range(115,115)]),\\char-class([range(115,115)]),\\char-class([range(111,111)]),\\char-class([range(99,99)])],{})", Factory.Production);
  private static final IConstructor prod__parameters_$Header__tags_$Tags_$layouts_LAYOUTLIST_lit_module_$layouts_LAYOUTLIST_name_$QualifiedName_$layouts_LAYOUTLIST_params_$ModuleParameters_$layouts_LAYOUTLIST_imports_iter_star_seps__$Import__$layouts_LAYOUTLIST_ = (IConstructor) _read("prod(label(\"parameters\",sort(\"Header\")),[label(\"tags\",sort(\"Tags\")),layouts(\"LAYOUTLIST\"),lit(\"module\"),layouts(\"LAYOUTLIST\"),label(\"name\",sort(\"QualifiedName\")),layouts(\"LAYOUTLIST\"),label(\"params\",sort(\"ModuleParameters\")),layouts(\"LAYOUTLIST\"),label(\"imports\",\\iter-star-seps(sort(\"Import\"),[layouts(\"LAYOUTLIST\")]))],{})", Factory.Production);
  private static final IConstructor prod__lit_private__char_class___range__112_112_char_class___range__114_114_char_class___range__105_105_char_class___range__118_118_char_class___range__97_97_char_class___range__116_116_char_class___range__101_101_ = (IConstructor) _read("prod(lit(\"private\"),[\\char-class([range(112,112)]),\\char-class([range(114,114)]),\\char-class([range(105,105)]),\\char-class([range(118,118)]),\\char-class([range(97,97)]),\\char-class([range(116,116)]),\\char-class([range(101,101)])],{})", Factory.Production);
  private static final IConstructor prod__relation_$BasicType__lit_rel_ = (IConstructor) _read("prod(label(\"relation\",sort(\"BasicType\")),[lit(\"rel\")],{})", Factory.Production);
  private static final IConstructor prod__return_$Statement__lit_return_$layouts_LAYOUTLIST_statement_$Statement__assoc__non_assoc_tag__breakable = (IConstructor) _read("prod(label(\"return\",sort(\"Statement\")),[lit(\"return\"),layouts(\"LAYOUTLIST\"),label(\"statement\",conditional(sort(\"Statement\"),{except(\"functionDeclaration\"),except(\"variableDeclaration\")}))],{assoc(\\non-assoc()),tag(breakable())})", Factory.Production);
  private static final IConstructor prod__sequence_$Sym__lit___40_$layouts_LAYOUTLIST_first_$Sym_$layouts_LAYOUTLIST_sequence_iter_seps__$Sym__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_ = (IConstructor) _read("prod(label(\"sequence\",sort(\"Sym\")),[lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"first\",sort(\"Sym\")),layouts(\"LAYOUTLIST\"),label(\"sequence\",\\iter-seps(sort(\"Sym\"),[layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\")\")],{})", Factory.Production);
  private static final IConstructor prod__startOfLine_$Sym__lit___94_$layouts_LAYOUTLIST_symbol_$Sym_ = (IConstructor) _read("prod(label(\"startOfLine\",sort(\"Sym\")),[lit(\"^\"),layouts(\"LAYOUTLIST\"),label(\"symbol\",sort(\"Sym\"))],{})", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_bracket_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"bracket\")],{})", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_num_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"num\")],{})", Factory.Production);
  private static final IConstructor prod__$BooleanLiteral__lit_true_ = (IConstructor) _read("prod(lex(\"BooleanLiteral\"),[lit(\"true\")],{})", Factory.Production);
  private static final IConstructor prod__$NamedRegExp__char_class___range__0_46_range__48_59_range__61_61_range__63_91_range__93_16777215_ = (IConstructor) _read("prod(lex(\"NamedRegExp\"),[\\char-class([range(0,46),range(48,59),range(61,61),range(63,91),range(93,16777215)])],{})", Factory.Production);
  private static final IConstructor prod__$RealLiteral__iter__char_class___range__48_57_char_class___range__68_68_range__70_70_range__100_100_range__102_102_ = (IConstructor) _read("prod(lex(\"RealLiteral\"),[iter(\\char-class([range(48,57)])),\\char-class([range(68,68),range(70,70),range(100,100),range(102,102)])],{})", Factory.Production);
  private static final IConstructor prod__$RealLiteral__iter__char_class___range__48_57_lit___46_iter_star__char_class___range__48_57_char_class___range__69_69_range__101_101_opt__char_class___range__43_43_range__45_45_iter__char_class___range__48_57_opt__char_class___range__68_68_range__70_70_range__100_100_range__102_102_ = (IConstructor) _read("prod(lex(\"RealLiteral\"),[iter(\\char-class([range(48,57)])),lit(\".\"),\\iter-star(\\char-class([range(48,57)])),\\char-class([range(69,69),range(101,101)]),opt(\\char-class([range(43,43),range(45,45)])),iter(\\char-class([range(48,57)])),opt(\\char-class([range(68,68),range(70,70),range(100,100),range(102,102)]))],{})", Factory.Production);
  private static final IConstructor regular__iter_star_seps__$TypeArg__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST = (IConstructor) _read("regular(\\iter-star-seps(sort(\"TypeArg\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")]))", Factory.Production);
  private static final IConstructor prod__lit___60_60__char_class___range__60_60_char_class___range__60_60_ = (IConstructor) _read("prod(lit(\"\\<\\<\"),[\\char-class([range(60,60)]),\\char-class([range(60,60)])],{})", Factory.Production);
  private static final IConstructor prod__lit___60_61__char_class___range__60_60_char_class___range__61_61_ = (IConstructor) _read("prod(lit(\"\\<=\"),[\\char-class([range(60,60)]),\\char-class([range(61,61)])],{})", Factory.Production);
  private static final IConstructor prod__lit___92_92__char_class___range__92_92_char_class___range__92_92_ = (IConstructor) _read("prod(lit(\"\\\\\\\\\"),[\\char-class([range(92,92)]),\\char-class([range(92,92)])],{})", Factory.Production);
  private static final IConstructor prod__lit___92_96__char_class___range__92_92_char_class___range__96_96_ = (IConstructor) _read("prod(lit(\"\\\\`\"),[\\char-class([range(92,92)]),\\char-class([range(96,96)])],{})", Factory.Production);
  private static final IConstructor prod__default_$Label__name_$Name_$layouts_LAYOUTLIST_lit___58_ = (IConstructor) _read("prod(label(\"default\",sort(\"Label\")),[label(\"name\",lex(\"Name\")),layouts(\"LAYOUTLIST\"),lit(\":\")],{})", Factory.Production);
  private static final IConstructor prod__lit_else__char_class___range__101_101_char_class___range__108_108_char_class___range__115_115_char_class___range__101_101_ = (IConstructor) _read("prod(lit(\"else\"),[\\char-class([range(101,101)]),\\char-class([range(108,108)]),\\char-class([range(115,115)]),\\char-class([range(101,101)])],{})", Factory.Production);
  private static final IConstructor prod__lit_finally__char_class___range__102_102_char_class___range__105_105_char_class___range__110_110_char_class___range__97_97_char_class___range__108_108_char_class___range__108_108_char_class___range__121_121_ = (IConstructor) _read("prod(lit(\"finally\"),[\\char-class([range(102,102)]),\\char-class([range(105,105)]),\\char-class([range(110,110)]),\\char-class([range(97,97)]),\\char-class([range(108,108)]),\\char-class([range(108,108)]),\\char-class([range(121,121)])],{})", Factory.Production);
  private static final IConstructor prod__lit_global__char_class___range__103_103_char_class___range__108_108_char_class___range__111_111_char_class___range__98_98_char_class___range__97_97_char_class___range__108_108_ = (IConstructor) _read("prod(lit(\"global\"),[\\char-class([range(103,103)]),\\char-class([range(108,108)]),\\char-class([range(111,111)]),\\char-class([range(98,98)]),\\char-class([range(97,97)]),\\char-class([range(108,108)])],{})", Factory.Production);
  private static final IConstructor prod__has_$Expression__expression_$Expression_$layouts_LAYOUTLIST_lit_has_$layouts_LAYOUTLIST_name_$Name_ = (IConstructor) _read("prod(label(\"has\",sort(\"Expression\")),[label(\"expression\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"has\"),layouts(\"LAYOUTLIST\"),label(\"name\",lex(\"Name\"))],{})", Factory.Production);
  private static final IConstructor prod__lit_import__char_class___range__105_105_char_class___range__109_109_char_class___range__112_112_char_class___range__111_111_char_class___range__114_114_char_class___range__116_116_ = (IConstructor) _read("prod(lit(\"import\"),[\\char-class([range(105,105)]),\\char-class([range(109,109)]),\\char-class([range(112,112)]),\\char-class([range(111,111)]),\\char-class([range(114,114)]),\\char-class([range(116,116)])],{})", Factory.Production);
  private static final IConstructor prod__innermost_$Strategy__lit_innermost_ = (IConstructor) _read("prod(label(\"innermost\",sort(\"Strategy\")),[lit(\"innermost\")],{})", Factory.Production);
  private static final IConstructor prod__it_$Expression__lit_it_ = (IConstructor) _read("prod(label(\"it\",sort(\"Expression\")),[conditional(lit(\"it\"),{\\not-precede(\\char-class([range(65,90),range(95,95),range(97,122)])),\\not-follow(\\char-class([range(65,90),range(95,95),range(97,122)]))})],{})", Factory.Production);
  private static final IConstructor prod__location_$Literal__locationLiteral_$LocationLiteral_ = (IConstructor) _read("prod(label(\"location\",sort(\"Literal\")),[label(\"locationLiteral\",sort(\"LocationLiteral\"))],{})", Factory.Production);
  private static final IConstructor prod__product_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___42_$layouts_LAYOUTLIST_empty_$layouts_LAYOUTLIST_rhs_$Expression__assoc__left = (IConstructor) _read("prod(label(\"product\",sort(\"Expression\")),[label(\"lhs\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"*\"),layouts(\"LAYOUTLIST\"),conditional(empty(),{\\not-follow(lit(\"*\"))}),layouts(\"LAYOUTLIST\"),label(\"rhs\",conditional(sort(\"Expression\"),{except(\"noMatch\"),except(\"match\")}))],{assoc(left())})", Factory.Production);
  private static final IConstructor prod__start_$Sym__lit_start_$layouts_LAYOUTLIST_lit___91_$layouts_LAYOUTLIST_nonterminal_$Nonterminal_$layouts_LAYOUTLIST_lit___93_ = (IConstructor) _read("prod(label(\"start\",sort(\"Sym\")),[lit(\"start\"),layouts(\"LAYOUTLIST\"),lit(\"[\"),layouts(\"LAYOUTLIST\"),label(\"nonterminal\",lex(\"Nonterminal\")),layouts(\"LAYOUTLIST\"),lit(\"]\")],{})", Factory.Production);
  private static final IConstructor prod__stepRange_$Expression__lit___91_$layouts_LAYOUTLIST_first_$Expression_$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_second_$Expression_$layouts_LAYOUTLIST_lit___46_46_$layouts_LAYOUTLIST_last_$Expression_$layouts_LAYOUTLIST_lit___93_ = (IConstructor) _read("prod(label(\"stepRange\",sort(\"Expression\")),[lit(\"[\"),layouts(\"LAYOUTLIST\"),label(\"first\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\"),label(\"second\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"..\"),layouts(\"LAYOUTLIST\"),label(\"last\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"]\")],{})", Factory.Production);
  private static final IConstructor prod__unlabeled_$Prod__modifiers_iter_star_seps__$ProdModifier__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_args_iter_star_seps__$Sym__$layouts_LAYOUTLIST_ = (IConstructor) _read("prod(label(\"unlabeled\",sort(\"Prod\")),[label(\"modifiers\",\\iter-star-seps(sort(\"ProdModifier\"),[layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),label(\"args\",\\iter-star-seps(sort(\"Sym\"),[layouts(\"LAYOUTLIST\")]))],{})", Factory.Production);
  private static final IConstructor prod__variable_$Kind__lit_variable_ = (IConstructor) _read("prod(label(\"variable\",sort(\"Kind\")),[lit(\"variable\")],{})", Factory.Production);
  private static final IConstructor prod__lit___124_124__char_class___range__124_124_char_class___range__124_124_ = (IConstructor) _read("prod(lit(\"||\"),[\\char-class([range(124,124)]),\\char-class([range(124,124)])],{})", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_node_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"node\")],{})", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_one_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"one\")],{})", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_continue_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"continue\")],{})", Factory.Production);
  private static final IConstructor prod__$PostPathChars__lit___62_$URLChars_lit___124_ = (IConstructor) _read("prod(lex(\"PostPathChars\"),[lit(\"\\>\"),lex(\"URLChars\"),lit(\"|\")],{})", Factory.Production);
  private static final IConstructor regular__iter_star__$RegExp = (IConstructor) _read("regular(\\iter-star(lex(\"RegExp\")))", Factory.Production);
  private static final IConstructor prod__hexIntegerLiteral_$IntegerLiteral__hex_$HexIntegerLiteral_ = (IConstructor) _read("prod(label(\"hexIntegerLiteral\",sort(\"IntegerLiteral\")),[label(\"hex\",lex(\"HexIntegerLiteral\"))],{})", Factory.Production);
  private static final IConstructor regular__iter_star__alt___char_class___range__42_42_char_class___range__0_41_range__43_16777215 = (IConstructor) _read("regular(\\iter-star(alt({conditional(\\char-class([range(42,42)]),{\\not-follow(\\char-class([range(47,47)]))}),\\char-class([range(0,41),range(43,16777215)])})))", Factory.Production);
  private static final IConstructor prod__addition_$Assignment__lit___43_61_ = (IConstructor) _read("prod(label(\"addition\",sort(\"Assignment\")),[lit(\"+=\")],{})", Factory.Production);
  private static final IConstructor prod__asType_$Pattern__lit___91_$layouts_LAYOUTLIST_type_$Type_$layouts_LAYOUTLIST_lit___93_$layouts_LAYOUTLIST_argument_$Pattern_ = (IConstructor) _read("prod(label(\"asType\",sort(\"Pattern\")),[lit(\"[\"),layouts(\"LAYOUTLIST\"),label(\"type\",sort(\"Type\")),layouts(\"LAYOUTLIST\"),lit(\"]\"),layouts(\"LAYOUTLIST\"),label(\"argument\",sort(\"Pattern\"))],{})", Factory.Production);
  private static final IConstructor prod__associative_$Assoc__lit_assoc_ = (IConstructor) _read("prod(label(\"associative\",sort(\"Assoc\")),[lit(\"assoc\")],{})", Factory.Production);
  private static final IConstructor prod__composition_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit_o_$layouts_LAYOUTLIST_rhs_$Expression__assoc__left = (IConstructor) _read("prod(label(\"composition\",sort(\"Expression\")),[label(\"lhs\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"o\"),layouts(\"LAYOUTLIST\"),label(\"rhs\",sort(\"Expression\"))],{assoc(left())})", Factory.Production);
  private static final IConstructor prod__doWhile_$StringTemplate__lit_do_$layouts_LAYOUTLIST_lit___123_$layouts_LAYOUTLIST_preStats_iter_star_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_body_$StringMiddle_$layouts_LAYOUTLIST_postStats_iter_star_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_$layouts_LAYOUTLIST_lit_while_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_condition_$Expression_$layouts_LAYOUTLIST_lit___41_ = (IConstructor) _read("prod(label(\"doWhile\",sort(\"StringTemplate\")),[lit(\"do\"),layouts(\"LAYOUTLIST\"),lit(\"{\"),layouts(\"LAYOUTLIST\"),label(\"preStats\",\\iter-star-seps(sort(\"Statement\"),[layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),label(\"body\",sort(\"StringMiddle\")),layouts(\"LAYOUTLIST\"),label(\"postStats\",\\iter-star-seps(sort(\"Statement\"),[layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\"}\"),layouts(\"LAYOUTLIST\"),lit(\"while\"),layouts(\"LAYOUTLIST\"),lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"condition\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\")\")],{})", Factory.Production);
  private static final IConstructor prod__empty_$Sym__lit___40_$layouts_LAYOUTLIST_lit___41_ = (IConstructor) _read("prod(label(\"empty\",sort(\"Sym\")),[lit(\"(\"),layouts(\"LAYOUTLIST\"),lit(\")\")],{})", Factory.Production);
  private static final IConstructor prod__gt_$ConcretePart__lit___92_62_ = (IConstructor) _read("prod(label(\"gt\",lex(\"ConcretePart\")),[lit(\"\\\\\\>\")],{})", Factory.Production);
  private static final IConstructor prod__lit_keyword__char_class___range__107_107_char_class___range__101_101_char_class___range__121_121_char_class___range__119_119_char_class___range__111_111_char_class___range__114_114_char_class___range__100_100_ = (IConstructor) _read("prod(lit(\"keyword\"),[\\char-class([range(107,107)]),\\char-class([range(101,101)]),\\char-class([range(121,121)]),\\char-class([range(119,119)]),\\char-class([range(111,111)]),\\char-class([range(114,114)]),\\char-class([range(100,100)])],{})", Factory.Production);
  private static final IConstructor prod__map_$BasicType__lit_map_ = (IConstructor) _read("prod(label(\"map\",sort(\"BasicType\")),[lit(\"map\")],{})", Factory.Production);
  private static final IConstructor prod__nonInterpolated_$PathPart__pathChars_$PathChars_ = (IConstructor) _read("prod(label(\"nonInterpolated\",sort(\"PathPart\")),[label(\"pathChars\",lex(\"PathChars\"))],{})", Factory.Production);
  private static final IConstructor prod__rational_$Literal__rationalLiteral_$RationalLiteral_ = (IConstructor) _read("prod(label(\"rational\",sort(\"Literal\")),[label(\"rationalLiteral\",lex(\"RationalLiteral\"))],{})", Factory.Production);
  private static final IConstructor prod__splice_$Pattern__lit___42_$layouts_LAYOUTLIST_argument_$Pattern_ = (IConstructor) _read("prod(label(\"splice\",sort(\"Pattern\")),[lit(\"*\"),layouts(\"LAYOUTLIST\"),label(\"argument\",sort(\"Pattern\"))],{})", Factory.Production);
  private static final IConstructor prod__utf16_$UnicodeEscape__lit___92_char_class___range__117_117_char_class___range__48_57_range__65_70_range__97_102_char_class___range__48_57_range__65_70_range__97_102_char_class___range__48_57_range__65_70_range__97_102_char_class___range__48_57_range__65_70_range__97_102_ = (IConstructor) _read("prod(label(\"utf16\",lex(\"UnicodeEscape\")),[lit(\"\\\\\"),\\char-class([range(117,117)]),\\char-class([range(48,57),range(65,70),range(97,102)]),\\char-class([range(48,57),range(65,70),range(97,102)]),\\char-class([range(48,57),range(65,70),range(97,102)]),\\char-class([range(48,57),range(65,70),range(97,102)])],{})", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_loc_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"loc\")],{})", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__$BasicType_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[sort(\"BasicType\")],{})", Factory.Production);
  private static final IConstructor regular__iter_star__alt___$TagString_char_class___range__0_122_range__124_124_range__126_16777215_seq___lit___92_char_class___range__123_123_range__125_125 = (IConstructor) _read("regular(\\iter-star(alt({lex(\"TagString\"),\\char-class([range(0,122),range(124,124),range(126,16777215)]),seq([lit(\"\\\\\"),\\char-class([range(123,123),range(125,125)])])})))", Factory.Production);
  private static final IConstructor prod__$PostProtocolChars__lit___62_$URLChars_lit___58_47_47_ = (IConstructor) _read("prod(lex(\"PostProtocolChars\"),[lit(\"\\>\"),lex(\"URLChars\"),lit(\"://\")],{})", Factory.Production);
  private static final IConstructor prod__$RegExp__char_class___range__0_46_range__48_59_range__61_61_range__63_91_range__93_16777215_ = (IConstructor) _read("prod(lex(\"RegExp\"),[\\char-class([range(0,46),range(48,59),range(61,61),range(63,91),range(93,16777215)])],{})", Factory.Production);
  private static final IConstructor prod__$RegExp__lit___60_$Name_lit___58_iter_star__$NamedRegExp_lit___62_ = (IConstructor) _read("prod(lex(\"RegExp\"),[lit(\"\\<\"),lex(\"Name\"),lit(\":\"),\\iter-star(lex(\"NamedRegExp\")),lit(\"\\>\")],{})", Factory.Production);
  private static final IConstructor regular__iter_star__$NamedRegExp = (IConstructor) _read("regular(\\iter-star(lex(\"NamedRegExp\")))", Factory.Production);
  private static final IConstructor regular__iter_seps__$Pattern__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST = (IConstructor) _read("regular(\\iter-seps(sort(\"Pattern\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")]))", Factory.Production);
  private static final IConstructor prod__subscript_$Expression__expression_$Expression_$layouts_LAYOUTLIST_lit___91_$layouts_LAYOUTLIST_subscripts_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___93_ = (IConstructor) _read("prod(label(\"subscript\",sort(\"Expression\")),[conditional(label(\"expression\",sort(\"Expression\")),{except(\"transitiveReflexiveClosure\"),except(\"transitiveClosure\"),except(\"isDefined\")}),layouts(\"LAYOUTLIST\"),lit(\"[\"),layouts(\"LAYOUTLIST\"),label(\"subscripts\",\\iter-seps(sort(\"Expression\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\"]\")],{})", Factory.Production);
  private static final IConstructor prod__lit___62_62__char_class___range__62_62_char_class___range__62_62_ = (IConstructor) _read("prod(lit(\"\\>\\>\"),[\\char-class([range(62,62)]),\\char-class([range(62,62)])],{})", Factory.Production);
  private static final IConstructor prod__lit___63_61__char_class___range__63_63_char_class___range__61_61_ = (IConstructor) _read("prod(lit(\"?=\"),[\\char-class([range(63,63)]),\\char-class([range(61,61)])],{})", Factory.Production);
  private static final IConstructor prod__lit_all__char_class___range__97_97_char_class___range__108_108_char_class___range__108_108_ = (IConstructor) _read("prod(lit(\"all\"),[\\char-class([range(97,97)]),\\char-class([range(108,108)]),\\char-class([range(108,108)])],{})", Factory.Production);
  private static final IConstructor prod__anno_$Kind__lit_anno_ = (IConstructor) _read("prod(label(\"anno\",sort(\"Kind\")),[lit(\"anno\")],{})", Factory.Production);
  private static final IConstructor prod__associativityGroup_$Prod__associativity_$Assoc_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_group_$Prod_$layouts_LAYOUTLIST_lit___41__tag__Foldable = (IConstructor) _read("prod(label(\"associativityGroup\",sort(\"Prod\")),[label(\"associativity\",sort(\"Assoc\")),layouts(\"LAYOUTLIST\"),lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"group\",sort(\"Prod\")),layouts(\"LAYOUTLIST\"),lit(\")\")],{tag(Foldable())})", Factory.Production);
  private static final IConstructor prod__lit_bottom_up__char_class___range__98_98_char_class___range__111_111_char_class___range__116_116_char_class___range__116_116_char_class___range__111_111_char_class___range__109_109_char_class___range__45_45_char_class___range__117_117_char_class___range__112_112_ = (IConstructor) _read("prod(lit(\"bottom-up\"),[\\char-class([range(98,98)]),\\char-class([range(111,111)]),\\char-class([range(116,116)]),\\char-class([range(116,116)]),\\char-class([range(111,111)]),\\char-class([range(109,109)]),\\char-class([range(45,45)]),\\char-class([range(117,117)]),\\char-class([range(112,112)])],{})", Factory.Production);
  private static final IConstructor prod__lit_break__char_class___range__98_98_char_class___range__114_114_char_class___range__101_101_char_class___range__97_97_char_class___range__107_107_ = (IConstructor) _read("prod(lit(\"break\"),[\\char-class([range(98,98)]),\\char-class([range(114,114)]),\\char-class([range(101,101)]),\\char-class([range(97,97)]),\\char-class([range(107,107)])],{})", Factory.Production);
  private static final IConstructor prod__column_$Sym__symbol_$Sym_$layouts_LAYOUTLIST_lit___64_$layouts_LAYOUTLIST_column_$IntegerLiteral_ = (IConstructor) _read("prod(label(\"column\",sort(\"Sym\")),[label(\"symbol\",sort(\"Sym\")),layouts(\"LAYOUTLIST\"),lit(\"@\"),layouts(\"LAYOUTLIST\"),label(\"column\",sort(\"IntegerLiteral\"))],{})", Factory.Production);
  private static final IConstructor prod__is_$Expression__expression_$Expression_$layouts_LAYOUTLIST_lit_is_$layouts_LAYOUTLIST_name_$Name_ = (IConstructor) _read("prod(label(\"is\",sort(\"Expression\")),[label(\"expression\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"is\"),layouts(\"LAYOUTLIST\"),label(\"name\",lex(\"Name\"))],{})", Factory.Production);
  private static final IConstructor prod__mid_$ProtocolTail__mid_$MidProtocolChars_$layouts_LAYOUTLIST_expression_$Expression_$layouts_LAYOUTLIST_tail_$ProtocolTail_ = (IConstructor) _read("prod(label(\"mid\",sort(\"ProtocolTail\")),[label(\"mid\",lex(\"MidProtocolChars\")),layouts(\"LAYOUTLIST\"),label(\"expression\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),label(\"tail\",sort(\"ProtocolTail\"))],{})", Factory.Production);
  private static final IConstructor prod__subscript_$Assignable__receiver_$Assignable_$layouts_LAYOUTLIST_lit___91_$layouts_LAYOUTLIST_subscript_$Expression_$layouts_LAYOUTLIST_lit___93_ = (IConstructor) _read("prod(label(\"subscript\",sort(\"Assignable\")),[label(\"receiver\",sort(\"Assignable\")),layouts(\"LAYOUTLIST\"),lit(\"[\"),layouts(\"LAYOUTLIST\"),label(\"subscript\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"]\")],{})", Factory.Production);
  private static final IConstructor prod__lit_syntax__char_class___range__115_115_char_class___range__121_121_char_class___range__110_110_char_class___range__116_116_char_class___range__97_97_char_class___range__120_120_ = (IConstructor) _read("prod(lit(\"syntax\"),[\\char-class([range(115,115)]),\\char-class([range(121,121)]),\\char-class([range(110,110)]),\\char-class([range(116,116)]),\\char-class([range(97,97)]),\\char-class([range(120,120)])],{})", Factory.Production);
  private static final IConstructor prod__template_$StringMiddle__mid_$MidStringChars_$layouts_LAYOUTLIST_template_$StringTemplate_$layouts_LAYOUTLIST_tail_$StringMiddle_ = (IConstructor) _read("prod(label(\"template\",sort(\"StringMiddle\")),[label(\"mid\",lex(\"MidStringChars\")),layouts(\"LAYOUTLIST\"),label(\"template\",sort(\"StringTemplate\")),layouts(\"LAYOUTLIST\"),label(\"tail\",sort(\"StringMiddle\"))],{})", Factory.Production);
  private static final IConstructor prod__lit_try__char_class___range__116_116_char_class___range__114_114_char_class___range__121_121_ = (IConstructor) _read("prod(lit(\"try\"),[\\char-class([range(116,116)]),\\char-class([range(114,114)]),\\char-class([range(121,121)])],{})", Factory.Production);
  private static final IConstructor prod__typedVariableBecomes_$Pattern__type_$Type_$layouts_LAYOUTLIST_name_$Name_$layouts_LAYOUTLIST_lit___58_$layouts_LAYOUTLIST_pattern_$Pattern_ = (IConstructor) _read("prod(label(\"typedVariableBecomes\",sort(\"Pattern\")),[label(\"type\",sort(\"Type\")),layouts(\"LAYOUTLIST\"),label(\"name\",lex(\"Name\")),layouts(\"LAYOUTLIST\"),lit(\":\"),layouts(\"LAYOUTLIST\"),label(\"pattern\",sort(\"Pattern\"))],{})", Factory.Production);
  private static final IConstructor prod__variableBecomes_$Pattern__name_$Name_$layouts_LAYOUTLIST_lit___58_$layouts_LAYOUTLIST_pattern_$Pattern_ = (IConstructor) _read("prod(label(\"variableBecomes\",sort(\"Pattern\")),[label(\"name\",lex(\"Name\")),layouts(\"LAYOUTLIST\"),lit(\":\"),layouts(\"LAYOUTLIST\"),label(\"pattern\",sort(\"Pattern\"))],{})", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_rat_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"rat\")],{})", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_mod_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"mod\")],{})", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_throw_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"throw\")],{})", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_module_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"module\")],{})", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_private_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"private\")],{})", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_map_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"map\")],{})", Factory.Production);
  private static final IConstructor regular__iter_star_seps__$Range__$layouts_LAYOUTLIST = (IConstructor) _read("regular(\\iter-star-seps(sort(\"Range\"),[layouts(\"LAYOUTLIST\")]))", Factory.Production);
  private static final IConstructor regular__iter_seps__$Type__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST = (IConstructor) _read("regular(\\iter-seps(sort(\"Type\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")]))", Factory.Production);
  private static final IConstructor prod__addition_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___43_$layouts_LAYOUTLIST_rhs_$Expression__assoc__left = (IConstructor) _read("prod(label(\"addition\",sort(\"Expression\")),[label(\"lhs\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"+\"),layouts(\"LAYOUTLIST\"),label(\"rhs\",conditional(sort(\"Expression\"),{except(\"noMatch\"),except(\"match\")}))],{assoc(left())})", Factory.Production);
  private static final IConstructor prod__lit_append__char_class___range__97_97_char_class___range__112_112_char_class___range__112_112_char_class___range__101_101_char_class___range__110_110_char_class___range__100_100_ = (IConstructor) _read("prod(lit(\"append\"),[\\char-class([range(97,97)]),\\char-class([range(112,112)]),\\char-class([range(112,112)]),\\char-class([range(101,101)]),\\char-class([range(110,110)]),\\char-class([range(100,100)])],{})", Factory.Production);
  private static final IConstructor prod__arbitrary_$PatternWithAction__pattern_$Pattern_$layouts_LAYOUTLIST_lit___58_$layouts_LAYOUTLIST_statement_$Statement_ = (IConstructor) _read("prod(label(\"arbitrary\",sort(\"PatternWithAction\")),[label(\"pattern\",sort(\"Pattern\")),layouts(\"LAYOUTLIST\"),lit(\":\"),layouts(\"LAYOUTLIST\"),label(\"statement\",sort(\"Statement\"))],{})", Factory.Production);
  private static final IConstructor prod__lit_assoc__char_class___range__97_97_char_class___range__115_115_char_class___range__115_115_char_class___range__111_111_char_class___range__99_99_ = (IConstructor) _read("prod(lit(\"assoc\"),[\\char-class([range(97,97)]),\\char-class([range(115,115)]),\\char-class([range(115,115)]),\\char-class([range(111,111)]),\\char-class([range(99,99)])],{})", Factory.Production);
  private static final IConstructor prod__bracket_$Class__lit___40_$layouts_LAYOUTLIST_charclass_$Class_$layouts_LAYOUTLIST_lit___41__bracket = (IConstructor) _read("prod(label(\"bracket\",sort(\"Class\")),[lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"charclass\",sort(\"Class\")),layouts(\"LAYOUTLIST\"),lit(\")\")],{bracket()})", Factory.Production);
  private static final IConstructor prod__lit_extend__char_class___range__101_101_char_class___range__120_120_char_class___range__116_116_char_class___range__101_101_char_class___range__110_110_char_class___range__100_100_ = (IConstructor) _read("prod(lit(\"extend\"),[\\char-class([range(101,101)]),\\char-class([range(120,120)]),\\char-class([range(116,116)]),\\char-class([range(101,101)]),\\char-class([range(110,110)]),\\char-class([range(100,100)])],{})", Factory.Production);
  private static final IConstructor prod__function_$Kind__lit_function_ = (IConstructor) _read("prod(label(\"function\",sort(\"Kind\")),[lit(\"function\")],{})", Factory.Production);
  private static final IConstructor prod__greaterThanOrEq_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___62_61_$layouts_LAYOUTLIST_rhs_$Expression__assoc__non_assoc = (IConstructor) _read("prod(label(\"greaterThanOrEq\",sort(\"Expression\")),[label(\"lhs\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"\\>=\"),layouts(\"LAYOUTLIST\"),label(\"rhs\",sort(\"Expression\"))],{assoc(\\non-assoc())})", Factory.Production);
  private static final IConstructor prod__iterStarSep_$Sym__lit___123_$layouts_LAYOUTLIST_symbol_$Sym_$layouts_LAYOUTLIST_sep_$Sym_$layouts_LAYOUTLIST_lit___125_$layouts_LAYOUTLIST_lit___42_ = (IConstructor) _read("prod(label(\"iterStarSep\",sort(\"Sym\")),[lit(\"{\"),layouts(\"LAYOUTLIST\"),label(\"symbol\",sort(\"Sym\")),layouts(\"LAYOUTLIST\"),label(\"sep\",sort(\"Sym\")),layouts(\"LAYOUTLIST\"),lit(\"}\"),layouts(\"LAYOUTLIST\"),lit(\"*\")],{})", Factory.Production);
  private static final IConstructor prod__lit_map__char_class___range__109_109_char_class___range__97_97_char_class___range__112_112_ = (IConstructor) _read("prod(lit(\"map\"),[\\char-class([range(109,109)]),\\char-class([range(97,97)]),\\char-class([range(112,112)])],{})", Factory.Production);
  private static final IConstructor prod__nonEmptyBlock_$Statement__label_$Label_$layouts_LAYOUTLIST_lit___123_$layouts_LAYOUTLIST_statements_iter_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_ = (IConstructor) _read("prod(label(\"nonEmptyBlock\",sort(\"Statement\")),[label(\"label\",sort(\"Label\")),layouts(\"LAYOUTLIST\"),lit(\"{\"),layouts(\"LAYOUTLIST\"),label(\"statements\",\\iter-seps(sort(\"Statement\"),[layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\"}\")],{})", Factory.Production);
  private static final IConstructor prod__lit_real__char_class___range__114_114_char_class___range__101_101_char_class___range__97_97_char_class___range__108_108_ = (IConstructor) _read("prod(lit(\"real\"),[\\char-class([range(114,114)]),\\char-class([range(101,101)]),\\char-class([range(97,97)]),\\char-class([range(108,108)])],{})", Factory.Production);
  private static final IConstructor prod__selector_$DataTypeSelector__sort_$QualifiedName_$layouts_LAYOUTLIST_lit___46_$layouts_LAYOUTLIST_production_$Name_ = (IConstructor) _read("prod(label(\"selector\",sort(\"DataTypeSelector\")),[label(\"sort\",sort(\"QualifiedName\")),layouts(\"LAYOUTLIST\"),lit(\".\"),layouts(\"LAYOUTLIST\"),label(\"production\",lex(\"Name\"))],{})", Factory.Production);
  private static final IConstructor prod__lit_true__char_class___range__116_116_char_class___range__114_114_char_class___range__117_117_char_class___range__101_101_ = (IConstructor) _read("prod(lit(\"true\"),[\\char-class([range(116,116)]),\\char-class([range(114,114)]),\\char-class([range(117,117)]),\\char-class([range(101,101)])],{})", Factory.Production);
  private static final IConstructor prod__tuple_$Expression__lit___60_$layouts_LAYOUTLIST_elements_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___62_ = (IConstructor) _read("prod(label(\"tuple\",sort(\"Expression\")),[lit(\"\\<\"),layouts(\"LAYOUTLIST\"),label(\"elements\",\\iter-seps(sort(\"Expression\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\"\\>\")],{})", Factory.Production);
  private static final IConstructor prod__lit_void__char_class___range__118_118_char_class___range__111_111_char_class___range__105_105_char_class___range__100_100_ = (IConstructor) _read("prod(lit(\"void\"),[\\char-class([range(118,118)]),\\char-class([range(111,111)]),\\char-class([range(105,105)]),\\char-class([range(100,100)])],{})", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_datetime_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"datetime\")],{})", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_bool_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"bool\")],{})", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_rel_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"rel\")],{})", Factory.Production);
  private static final IConstructor prod__$Char__char_class___range__0_31_range__33_33_range__35_38_range__40_44_range__46_59_range__61_61_range__63_90_range__94_16777215__tag__category___67_111_110_115_116_97_110_116 = (IConstructor) _read("prod(lex(\"Char\"),[\\char-class([range(0,31),range(33,33),range(35,38),range(40,44),range(46,59),range(61,61),range(63,90),range(94,16777215)])],{tag(category(\"Constant\"))})", Factory.Production);
  private static final IConstructor prod__$MidPathChars__lit___62_$URLChars_lit___60_ = (IConstructor) _read("prod(lex(\"MidPathChars\"),[lit(\"\\>\"),lex(\"URLChars\"),lit(\"\\<\")],{})", Factory.Production);
  private static final IConstructor prod__$RealLiteral__iter__char_class___range__48_57_lit___46_iter_star__char_class___range__48_57_opt__char_class___range__68_68_range__70_70_range__100_100_range__102_102_ = (IConstructor) _read("prod(lex(\"RealLiteral\"),[iter(\\char-class([range(48,57)])),conditional(lit(\".\"),{\\not-follow(lit(\".\"))}),\\iter-star(\\char-class([range(48,57)])),opt(\\char-class([range(68,68),range(70,70),range(100,100),range(102,102)]))],{})", Factory.Production);
  private static final IConstructor regular__iter_seps__$Sym__$layouts_LAYOUTLIST = (IConstructor) _read("regular(\\iter-seps(sort(\"Sym\"),[layouts(\"LAYOUTLIST\")]))", Factory.Production);
  private static final IConstructor regular__iter_star__char_class___range__45_45_range__48_57_range__65_90_range__95_95_range__97_122 = (IConstructor) _read("regular(\\iter-star(\\char-class([range(45,45),range(48,57),range(65,90),range(95,95),range(97,122)])))", Factory.Production);
  private static final IConstructor prod__empty__ = (IConstructor) _read("prod(empty(),[],{})", Factory.Production);
  private static final IConstructor prod__all_$Prod__lhs_$Prod_$layouts_LAYOUTLIST_lit___124_$layouts_LAYOUTLIST_rhs_$Prod__assoc__left = (IConstructor) _read("prod(label(\"all\",sort(\"Prod\")),[label(\"lhs\",sort(\"Prod\")),layouts(\"LAYOUTLIST\"),lit(\"|\"),layouts(\"LAYOUTLIST\"),label(\"rhs\",sort(\"Prod\"))],{assoc(left())})", Factory.Production);
  private static final IConstructor prod__and_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___38_38_$layouts_LAYOUTLIST_rhs_$Expression__assoc__left = (IConstructor) _read("prod(label(\"and\",sort(\"Expression\")),[label(\"lhs\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"&&\"),layouts(\"LAYOUTLIST\"),label(\"rhs\",sort(\"Expression\"))],{assoc(left())})", Factory.Production);
  private static final IConstructor prod__callOrTree_$Expression__expression_$Expression_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_arguments_iter_star_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_ = (IConstructor) _read("prod(label(\"callOrTree\",sort(\"Expression\")),[label(\"expression\",conditional(sort(\"Expression\"),{except(\"transitiveReflexiveClosure\"),except(\"transitiveClosure\"),except(\"isDefined\")})),layouts(\"LAYOUTLIST\"),lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"arguments\",\\iter-star-seps(sort(\"Expression\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\")\")],{})", Factory.Production);
  private static final IConstructor prod__concrete_$Expression__concrete_$Concrete_ = (IConstructor) _read("prod(label(\"concrete\",sort(\"Expression\")),[label(\"concrete\",sort(\"Concrete\"))],{})", Factory.Production);
  private static final IConstructor prod__empty_$Tag__lit___64_$layouts_LAYOUTLIST_name_$Name__tag__Folded_tag__category___67_111_109_109_101_110_116 = (IConstructor) _read("prod(label(\"empty\",sort(\"Tag\")),[lit(\"@\"),layouts(\"LAYOUTLIST\"),label(\"name\",lex(\"Name\"))],{tag(Folded()),tag(category(\"Comment\"))})", Factory.Production);
  private static final IConstructor prod__follow_$Sym__symbol_$Sym_$layouts_LAYOUTLIST_lit___62_62_$layouts_LAYOUTLIST_match_$Sym__assoc__left = (IConstructor) _read("prod(label(\"follow\",sort(\"Sym\")),[label(\"symbol\",sort(\"Sym\")),layouts(\"LAYOUTLIST\"),lit(\"\\>\\>\"),layouts(\"LAYOUTLIST\"),label(\"match\",sort(\"Sym\"))],{assoc(left())})", Factory.Production);
  private static final IConstructor prod__free_$TypeVar__lit___38_$layouts_LAYOUTLIST_name_$Name_ = (IConstructor) _read("prod(label(\"free\",sort(\"TypeVar\")),[lit(\"&\"),layouts(\"LAYOUTLIST\"),label(\"name\",lex(\"Name\"))],{})", Factory.Production);
  private static final IConstructor prod__ifDefined_$Assignment__lit___63_61_ = (IConstructor) _read("prod(label(\"ifDefined\",sort(\"Assignment\")),[lit(\"?=\")],{})", Factory.Production);
  private static final IConstructor prod__initialized_$Variable__name_$Name_$layouts_LAYOUTLIST_lit___61_$layouts_LAYOUTLIST_initial_$Expression_ = (IConstructor) _read("prod(label(\"initialized\",sort(\"Variable\")),[label(\"name\",lex(\"Name\")),layouts(\"LAYOUTLIST\"),lit(\"=\"),layouts(\"LAYOUTLIST\"),label(\"initial\",sort(\"Expression\"))],{})", Factory.Production);
  private static final IConstructor prod__lit_layout__char_class___range__108_108_char_class___range__97_97_char_class___range__121_121_char_class___range__111_111_char_class___range__117_117_char_class___range__116_116_ = (IConstructor) _read("prod(lit(\"layout\"),[\\char-class([range(108,108)]),\\char-class([range(97,97)]),\\char-class([range(121,121)]),\\char-class([range(111,111)]),\\char-class([range(117,117)]),\\char-class([range(116,116)])],{})", Factory.Production);
  private static final IConstructor prod__list_$FunctionModifiers__modifiers_iter_star_seps__$FunctionModifier__$layouts_LAYOUTLIST_ = (IConstructor) _read("prod(label(\"list\",sort(\"FunctionModifiers\")),[label(\"modifiers\",\\iter-star-seps(sort(\"FunctionModifier\"),[layouts(\"LAYOUTLIST\")]))],{})", Factory.Production);
  private static final IConstructor prod__named_$TypeArg__type_$Type_$layouts_LAYOUTLIST_name_$Name_ = (IConstructor) _read("prod(label(\"named\",sort(\"TypeArg\")),[label(\"type\",sort(\"Type\")),layouts(\"LAYOUTLIST\"),label(\"name\",lex(\"Name\"))],{})", Factory.Production);
  private static final IConstructor prod__selector_$Type__selector_$DataTypeSelector_ = (IConstructor) _read("prod(label(\"selector\",sort(\"Type\")),[label(\"selector\",sort(\"DataTypeSelector\"))],{})", Factory.Production);
  private static final IConstructor prod__lit_solve__char_class___range__115_115_char_class___range__111_111_char_class___range__108_108_char_class___range__118_118_char_class___range__101_101_ = (IConstructor) _read("prod(lit(\"solve\"),[\\char-class([range(115,115)]),\\char-class([range(111,111)]),\\char-class([range(108,108)]),\\char-class([range(118,118)]),\\char-class([range(101,101)])],{})", Factory.Production);
  private static final IConstructor prod__lit_switch__char_class___range__115_115_char_class___range__119_119_char_class___range__105_105_char_class___range__116_116_char_class___range__99_99_char_class___range__104_104_ = (IConstructor) _read("prod(lit(\"switch\"),[\\char-class([range(115,115)]),\\char-class([range(119,119)]),\\char-class([range(105,105)]),\\char-class([range(116,116)]),\\char-class([range(99,99)]),\\char-class([range(104,104)])],{})", Factory.Production);
  private static final IConstructor prod__throw_$Statement__lit_throw_$layouts_LAYOUTLIST_statement_$Statement__assoc__non_assoc_tag__breakable = (IConstructor) _read("prod(label(\"throw\",sort(\"Statement\")),[lit(\"throw\"),layouts(\"LAYOUTLIST\"),label(\"statement\",conditional(sort(\"Statement\"),{except(\"functionDeclaration\"),except(\"variableDeclaration\")}))],{assoc(\\non-assoc()),tag(breakable())})", Factory.Production);
  private static final IConstructor prod__user_$Type__user_$UserType_ = (IConstructor) _read("prod(label(\"user\",sort(\"Type\")),[label(\"user\",sort(\"UserType\"))],{})", Factory.Production);
  private static final IConstructor prod__voidClosure_$Expression__parameters_$Parameters_$layouts_LAYOUTLIST_lit___123_$layouts_LAYOUTLIST_statements_iter_star_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_ = (IConstructor) _read("prod(label(\"voidClosure\",sort(\"Expression\")),[label(\"parameters\",sort(\"Parameters\")),layouts(\"LAYOUTLIST\"),lit(\"{\"),layouts(\"LAYOUTLIST\"),label(\"statements\",\\iter-star-seps(sort(\"Statement\"),[layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\"}\")],{})", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_str_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"str\")],{})", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_return_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"return\")],{})", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_set_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"set\")],{})", Factory.Production);
  private static final IConstructor prod__$RealLiteral__lit___46_iter__char_class___range__48_57_char_class___range__69_69_range__101_101_opt__char_class___range__43_43_range__45_45_iter__char_class___range__48_57_opt__char_class___range__68_68_range__70_70_range__100_100_range__102_102_ = (IConstructor) _read("prod(lex(\"RealLiteral\"),[conditional(lit(\".\"),{\\not-precede(\\char-class([range(46,46)]))}),iter(\\char-class([range(48,57)])),\\char-class([range(69,69),range(101,101)]),opt(\\char-class([range(43,43),range(45,45)])),iter(\\char-class([range(48,57)])),opt(\\char-class([range(68,68),range(70,70),range(100,100),range(102,102)]))],{})", Factory.Production);
  private static final IConstructor regular__seq___char_class___range__48_57_opt__char_class___range__48_57 = (IConstructor) _read("regular(seq([\\char-class([range(48,57)]),opt(\\char-class([range(48,57)]))]))", Factory.Production);
  private static final IConstructor prod__lit___60_58__char_class___range__60_60_char_class___range__58_58_ = (IConstructor) _read("prod(lit(\"\\<:\"),[\\char-class([range(60,60)]),\\char-class([range(58,58)])],{})", Factory.Production);
  private static final IConstructor prod__lit___92_60__char_class___range__92_92_char_class___range__60_60_ = (IConstructor) _read("prod(lit(\"\\\\\\<\"),[\\char-class([range(92,92)]),\\char-class([range(60,60)])],{})", Factory.Production);
  private static final IConstructor prod__annotation_$Assignable__receiver_$Assignable_$layouts_LAYOUTLIST_lit___64_$layouts_LAYOUTLIST_annotation_$Name_ = (IConstructor) _read("prod(label(\"annotation\",sort(\"Assignable\")),[label(\"receiver\",sort(\"Assignable\")),layouts(\"LAYOUTLIST\"),lit(\"@\"),layouts(\"LAYOUTLIST\"),label(\"annotation\",lex(\"Name\"))],{})", Factory.Production);
  private static final IConstructor prod__lit_assert__char_class___range__97_97_char_class___range__115_115_char_class___range__115_115_char_class___range__101_101_char_class___range__114_114_char_class___range__116_116_ = (IConstructor) _read("prod(lit(\"assert\"),[\\char-class([range(97,97)]),\\char-class([range(115,115)]),\\char-class([range(115,115)]),\\char-class([range(101,101)]),\\char-class([range(114,114)]),\\char-class([range(116,116)])],{})", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_extend_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"extend\")],{})", Factory.Production);
  private static final IConstructor regular__iter_seps__$Catch__$layouts_LAYOUTLIST = (IConstructor) _read("regular(\\iter-seps(sort(\"Catch\"),[layouts(\"LAYOUTLIST\")]))", Factory.Production);
  private static final IConstructor prod__lit___92_62__char_class___range__92_92_char_class___range__62_62_ = (IConstructor) _read("prod(lit(\"\\\\\\>\"),[\\char-class([range(92,92)]),\\char-class([range(62,62)])],{})", Factory.Production);
  private static final IConstructor prod__bq_$ConcretePart__lit___92_96_ = (IConstructor) _read("prod(label(\"bq\",lex(\"ConcretePart\")),[lit(\"\\\\`\")],{})", Factory.Production);
  private static final IConstructor prod__bracket_$Expression__lit___40_$layouts_LAYOUTLIST_expression_$Expression_$layouts_LAYOUTLIST_lit___41__bracket = (IConstructor) _read("prod(label(\"bracket\",sort(\"Expression\")),[lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"expression\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\")\")],{bracket()})", Factory.Production);
  private static final IConstructor prod__bracket_$ProdModifier__lit_bracket_ = (IConstructor) _read("prod(label(\"bracket\",sort(\"ProdModifier\")),[lit(\"bracket\")],{})", Factory.Production);
  private static final IConstructor prod__character_$Range__character_$Char_ = (IConstructor) _read("prod(label(\"character\",sort(\"Range\")),[label(\"character\",lex(\"Char\"))],{})", Factory.Production);
  private static final IConstructor prod__fieldAccess_$Expression__expression_$Expression_$layouts_LAYOUTLIST_lit___46_$layouts_LAYOUTLIST_field_$Name_ = (IConstructor) _read("prod(label(\"fieldAccess\",sort(\"Expression\")),[label(\"expression\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\".\"),layouts(\"LAYOUTLIST\"),label(\"field\",lex(\"Name\"))],{})", Factory.Production);
  private static final IConstructor prod__iterSep_$Sym__lit___123_$layouts_LAYOUTLIST_symbol_$Sym_$layouts_LAYOUTLIST_sep_$Sym_$layouts_LAYOUTLIST_lit___125_$layouts_LAYOUTLIST_lit___43_ = (IConstructor) _read("prod(label(\"iterSep\",sort(\"Sym\")),[lit(\"{\"),layouts(\"LAYOUTLIST\"),label(\"symbol\",sort(\"Sym\")),layouts(\"LAYOUTLIST\"),label(\"sep\",sort(\"Sym\")),layouts(\"LAYOUTLIST\"),lit(\"}\"),layouts(\"LAYOUTLIST\"),lit(\"+\")],{})", Factory.Production);
  private static final IConstructor prod__rational_$BasicType__lit_rat_ = (IConstructor) _read("prod(label(\"rational\",sort(\"BasicType\")),[lit(\"rat\")],{})", Factory.Production);
  private static final IConstructor prod__replacing_$PatternWithAction__pattern_$Pattern_$layouts_LAYOUTLIST_lit___61_62_$layouts_LAYOUTLIST_replacement_$Replacement_ = (IConstructor) _read("prod(label(\"replacing\",sort(\"PatternWithAction\")),[label(\"pattern\",sort(\"Pattern\")),layouts(\"LAYOUTLIST\"),lit(\"=\\>\"),layouts(\"LAYOUTLIST\"),label(\"replacement\",sort(\"Replacement\"))],{})", Factory.Production);
  private static final IConstructor prod__tag_$ProdModifier__tag_$Tag_ = (IConstructor) _read("prod(label(\"tag\",sort(\"ProdModifier\")),[label(\"tag\",sort(\"Tag\"))],{})", Factory.Production);
  private static final IConstructor prod__union_$Class__lhs_$Class_$layouts_LAYOUTLIST_lit___124_124_$layouts_LAYOUTLIST_rhs_$Class__assoc__left = (IConstructor) _read("prod(label(\"union\",sort(\"Class\")),[label(\"lhs\",sort(\"Class\")),layouts(\"LAYOUTLIST\"),lit(\"||\"),layouts(\"LAYOUTLIST\"),label(\"rhs\",sort(\"Class\"))],{assoc(left())})", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_public_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"public\")],{})", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_list_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"list\")],{})", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_tag_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"tag\")],{})", Factory.Production);
  private static final IConstructor prod__$layouts_$default$__ = (IConstructor) _read("prod(layouts(\"$default$\"),[],{})", Factory.Production);
  private static final IConstructor regular__iter_seps__$EvalCommand__$layouts_LAYOUTLIST = (IConstructor) _read("regular(\\iter-seps(sort(\"EvalCommand\"),[layouts(\"LAYOUTLIST\")]))", Factory.Production);
  private static final IConstructor regular__iter_star_seps__$Statement__$layouts_LAYOUTLIST = (IConstructor) _read("regular(\\iter-star-seps(sort(\"Statement\"),[layouts(\"LAYOUTLIST\")]))", Factory.Production);
  private static final IConstructor regular__iter_star__$StringCharacter = (IConstructor) _read("regular(\\iter-star(lex(\"StringCharacter\")))", Factory.Production);
  private static final IConstructor prod__lit___58_58__char_class___range__58_58_char_class___range__58_58_ = (IConstructor) _read("prod(lit(\"::\"),[\\char-class([range(58,58)]),\\char-class([range(58,58)])],{})", Factory.Production);
  private static final IConstructor prod__lit___58_61__char_class___range__58_58_char_class___range__61_61_ = (IConstructor) _read("prod(lit(\":=\"),[\\char-class([range(58,58)]),\\char-class([range(61,61)])],{})", Factory.Production);
  private static final IConstructor prod__lit___60_45__char_class___range__60_60_char_class___range__45_45_ = (IConstructor) _read("prod(lit(\"\\<-\"),[\\char-class([range(60,60)]),\\char-class([range(45,45)])],{})", Factory.Production);
  private static final IConstructor prod__bottomUp_$Strategy__lit_bottom_up_ = (IConstructor) _read("prod(label(\"bottomUp\",sort(\"Strategy\")),[lit(\"bottom-up\")],{})", Factory.Production);
  private static final IConstructor prod__bs_$ConcretePart__lit___92_92_ = (IConstructor) _read("prod(label(\"bs\",lex(\"ConcretePart\")),[lit(\"\\\\\\\\\")],{})", Factory.Production);
  private static final IConstructor prod__default_$Assignment__lit___61_ = (IConstructor) _read("prod(label(\"default\",sort(\"Assignment\")),[lit(\"=\")],{})", Factory.Production);
  private static final IConstructor prod__default_$Mapping__$Pattern__from_$Pattern_$layouts_LAYOUTLIST_lit___58_$layouts_LAYOUTLIST_to_$Pattern_ = (IConstructor) _read("prod(label(\"default\",\\parameterized-sort(\"Mapping\",[sort(\"Pattern\")])),[label(\"from\",conditional(sort(\"Pattern\"),{except(\"ifDefinedOtherwise\")})),layouts(\"LAYOUTLIST\"),lit(\":\"),layouts(\"LAYOUTLIST\"),label(\"to\",sort(\"Pattern\"))],{})", Factory.Production);
  private static final IConstructor prod__lit_filter__char_class___range__102_102_char_class___range__105_105_char_class___range__108_108_char_class___range__116_116_char_class___range__101_101_char_class___range__114_114_ = (IConstructor) _read("prod(lit(\"filter\"),[\\char-class([range(102,102)]),\\char-class([range(105,105)]),\\char-class([range(108,108)]),\\char-class([range(116,116)]),\\char-class([range(101,101)]),\\char-class([range(114,114)])],{})", Factory.Production);
  private static final IConstructor prod__interpolated_$PathPart__pre_$PrePathChars_$layouts_LAYOUTLIST_expression_$Expression_$layouts_LAYOUTLIST_tail_$PathTail_ = (IConstructor) _read("prod(label(\"interpolated\",sort(\"PathPart\")),[label(\"pre\",lex(\"PrePathChars\")),layouts(\"LAYOUTLIST\"),label(\"expression\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),label(\"tail\",sort(\"PathTail\"))],{})", Factory.Production);
  private static final IConstructor prod__java_$FunctionModifier__lit_java_ = (IConstructor) _read("prod(label(\"java\",sort(\"FunctionModifier\")),[lit(\"java\")],{})", Factory.Production);
  private static final IConstructor prod__lit_lexical__char_class___range__108_108_char_class___range__101_101_char_class___range__120_120_char_class___range__105_105_char_class___range__99_99_char_class___range__97_97_char_class___range__108_108_ = (IConstructor) _read("prod(lit(\"lexical\"),[\\char-class([range(108,108)]),\\char-class([range(101,101)]),\\char-class([range(120,120)]),\\char-class([range(105,105)]),\\char-class([range(99,99)]),\\char-class([range(97,97)]),\\char-class([range(108,108)])],{})", Factory.Production);
  private static final IConstructor prod__map_$Expression__lit___40_$layouts_LAYOUTLIST_mappings_iter_star_seps__$Mapping__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_ = (IConstructor) _read("prod(label(\"map\",sort(\"Expression\")),[lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"mappings\",\\iter-star-seps(\\parameterized-sort(\"Mapping\",[sort(\"Expression\")]),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\")\")],{})", Factory.Production);
  private static final IConstructor prod__name_$UserType__name_$QualifiedName_ = (IConstructor) _read("prod(label(\"name\",sort(\"UserType\")),[label(\"name\",sort(\"QualifiedName\"))],{})", Factory.Production);
  private static final IConstructor prod__nonEmptyBlock_$Expression__lit___123_$layouts_LAYOUTLIST_statements_iter_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_ = (IConstructor) _read("prod(label(\"nonEmptyBlock\",sort(\"Expression\")),[lit(\"{\"),layouts(\"LAYOUTLIST\"),label(\"statements\",\\iter-seps(sort(\"Statement\"),[layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\"}\")],{})", Factory.Production);
  private static final IConstructor prod__private_$Visibility__lit_private_ = (IConstructor) _read("prod(label(\"private\",sort(\"Visibility\")),[lit(\"private\")],{})", Factory.Production);
  private static final IConstructor prod__lit_start__char_class___range__115_115_char_class___range__116_116_char_class___range__97_97_char_class___range__114_114_char_class___range__116_116_ = (IConstructor) _read("prod(lit(\"start\"),[\\char-class([range(115,115)]),\\char-class([range(116,116)]),\\char-class([range(97,97)]),\\char-class([range(114,114)]),\\char-class([range(116,116)])],{})", Factory.Production);
  private static final IConstructor prod__syntax_$Import__syntax_$SyntaxDefinition_ = (IConstructor) _read("prod(label(\"syntax\",sort(\"Import\")),[label(\"syntax\",sort(\"SyntaxDefinition\"))],{})", Factory.Production);
  private static final IConstructor prod__try_$Statement__lit_try_$layouts_LAYOUTLIST_body_$Statement_$layouts_LAYOUTLIST_handlers_iter_seps__$Catch__$layouts_LAYOUTLIST__assoc__non_assoc_tag__breakable = (IConstructor) _read("prod(label(\"try\",sort(\"Statement\")),[lit(\"try\"),layouts(\"LAYOUTLIST\"),label(\"body\",sort(\"Statement\")),layouts(\"LAYOUTLIST\"),label(\"handlers\",\\iter-seps(sort(\"Catch\"),[layouts(\"LAYOUTLIST\")]))],{assoc(\\non-assoc()),tag(breakable())})", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_switch_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"switch\")],{})", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_throws_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"throws\")],{})", Factory.Production);
  private static final IConstructor prod__$Backslash__char_class___range__92_92_ = (IConstructor) _read("prod(lex(\"Backslash\"),[conditional(\\char-class([range(92,92)]),{\\not-follow(\\char-class([range(47,47),range(60,60),range(62,62),range(92,92)]))})],{})", Factory.Production);
  private static final IConstructor prod__$DatePart__char_class___range__48_57_char_class___range__48_57_char_class___range__48_57_char_class___range__48_57_lit___char_class___range__48_49_char_class___range__48_57_lit___char_class___range__48_51_char_class___range__48_57_ = (IConstructor) _read("prod(lex(\"DatePart\"),[\\char-class([range(48,57)]),\\char-class([range(48,57)]),\\char-class([range(48,57)]),\\char-class([range(48,57)]),lit(\"-\"),\\char-class([range(48,49)]),\\char-class([range(48,57)]),lit(\"-\"),\\char-class([range(48,51)]),\\char-class([range(48,57)])],{})", Factory.Production);
  private static final IConstructor prod__$PostStringChars__char_class___range__62_62_iter_star__$StringCharacter_char_class___range__34_34__tag__category___67_111_110_115_116_97_110_116 = (IConstructor) _read("prod(lex(\"PostStringChars\"),[\\char-class([range(62,62)]),\\iter-star(lex(\"StringCharacter\")),\\char-class([range(34,34)])],{tag(category(\"Constant\"))})", Factory.Production);
  private static final IConstructor prod__$TimePartNoTZ__char_class___range__48_50_char_class___range__48_57_lit___58_char_class___range__48_53_char_class___range__48_57_lit___58_char_class___range__48_53_char_class___range__48_57_opt__seq___char_class___range__44_44_range__46_46_char_class___range__48_57_opt__seq___char_class___range__48_57_opt__char_class___range__48_57_ = (IConstructor) _read("prod(lex(\"TimePartNoTZ\"),[\\char-class([range(48,50)]),\\char-class([range(48,57)]),lit(\":\"),\\char-class([range(48,53)]),\\char-class([range(48,57)]),lit(\":\"),\\char-class([range(48,53)]),\\char-class([range(48,57)]),opt(seq([\\char-class([range(44,44),range(46,46)]),\\char-class([range(48,57)]),opt(seq([\\char-class([range(48,57)]),opt(\\char-class([range(48,57)]))]))]))],{})", Factory.Production);
  private static final IConstructor regular__empty = (IConstructor) _read("regular(empty())", Factory.Production);
  private static final IConstructor prod__anti_$Pattern__lit___33_$layouts_LAYOUTLIST_pattern_$Pattern_ = (IConstructor) _read("prod(label(\"anti\",sort(\"Pattern\")),[lit(\"!\"),layouts(\"LAYOUTLIST\"),label(\"pattern\",sort(\"Pattern\"))],{})", Factory.Production);
  private static final IConstructor prod__lit_bracket__char_class___range__98_98_char_class___range__114_114_char_class___range__97_97_char_class___range__99_99_char_class___range__107_107_char_class___range__101_101_char_class___range__116_116_ = (IConstructor) _read("prod(lit(\"bracket\"),[\\char-class([range(98,98)]),\\char-class([range(114,114)]),\\char-class([range(97,97)]),\\char-class([range(99,99)]),\\char-class([range(107,107)]),\\char-class([range(101,101)]),\\char-class([range(116,116)])],{})", Factory.Production);
  private static final IConstructor prod__default_$Formals__formals_iter_star_seps__$Pattern__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_ = (IConstructor) _read("prod(label(\"default\",sort(\"Formals\")),[label(\"formals\",\\iter-star-seps(sort(\"Pattern\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")]))],{})", Factory.Production);
  private static final IConstructor prod__empty_$DataTarget__ = (IConstructor) _read("prod(label(\"empty\",sort(\"DataTarget\")),[],{})", Factory.Production);
  private static final IConstructor prod__lit_for__char_class___range__102_102_char_class___range__111_111_char_class___range__114_114_ = (IConstructor) _read("prod(lit(\"for\"),[\\char-class([range(102,102)]),\\char-class([range(111,111)]),\\char-class([range(114,114)])],{})", Factory.Production);
  private static final IConstructor prod__for_$StringTemplate__lit_for_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_generators_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_$layouts_LAYOUTLIST_lit___123_$layouts_LAYOUTLIST_preStats_iter_star_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_body_$StringMiddle_$layouts_LAYOUTLIST_postStats_iter_star_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_ = (IConstructor) _read("prod(label(\"for\",sort(\"StringTemplate\")),[lit(\"for\"),layouts(\"LAYOUTLIST\"),lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"generators\",\\iter-seps(sort(\"Expression\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\")\"),layouts(\"LAYOUTLIST\"),lit(\"{\"),layouts(\"LAYOUTLIST\"),label(\"preStats\",\\iter-star-seps(sort(\"Statement\"),[layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),label(\"body\",sort(\"StringMiddle\")),layouts(\"LAYOUTLIST\"),label(\"postStats\",\\iter-star-seps(sort(\"Statement\"),[layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\"}\")],{})", Factory.Production);
  private static final IConstructor prod__fromTo_$Range__start_$Char_$layouts_LAYOUTLIST_lit___$layouts_LAYOUTLIST_end_$Char_ = (IConstructor) _read("prod(label(\"fromTo\",sort(\"Range\")),[label(\"start\",lex(\"Char\")),layouts(\"LAYOUTLIST\"),lit(\"-\"),layouts(\"LAYOUTLIST\"),label(\"end\",lex(\"Char\"))],{})", Factory.Production);
  private static final IConstructor prod__ifThen_$StringTemplate__lit_if_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_conditions_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_$layouts_LAYOUTLIST_lit___123_$layouts_LAYOUTLIST_preStats_iter_star_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_body_$StringMiddle_$layouts_LAYOUTLIST_postStats_iter_star_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_ = (IConstructor) _read("prod(label(\"ifThen\",sort(\"StringTemplate\")),[lit(\"if\"),layouts(\"LAYOUTLIST\"),lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"conditions\",\\iter-seps(sort(\"Expression\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\")\"),layouts(\"LAYOUTLIST\"),lit(\"{\"),layouts(\"LAYOUTLIST\"),label(\"preStats\",\\iter-star-seps(sort(\"Statement\"),[layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),label(\"body\",sort(\"StringMiddle\")),layouts(\"LAYOUTLIST\"),label(\"postStats\",\\iter-star-seps(sort(\"Statement\"),[layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\"}\")],{})", Factory.Production);
  private static final IConstructor prod__lit_insert__char_class___range__105_105_char_class___range__110_110_char_class___range__115_115_char_class___range__101_101_char_class___range__114_114_char_class___range__116_116_ = (IConstructor) _read("prod(lit(\"insert\"),[\\char-class([range(105,105)]),\\char-class([range(110,110)]),\\char-class([range(115,115)]),\\char-class([range(101,101)]),\\char-class([range(114,114)]),\\char-class([range(116,116)])],{})", Factory.Production);
  private static final IConstructor prod__lit_left__char_class___range__108_108_char_class___range__101_101_char_class___range__102_102_char_class___range__116_116_ = (IConstructor) _read("prod(lit(\"left\"),[\\char-class([range(108,108)]),\\char-class([range(101,101)]),\\char-class([range(102,102)]),\\char-class([range(116,116)])],{})", Factory.Production);
  private static final IConstructor prod__lt_$ConcretePart__lit___92_60_ = (IConstructor) _read("prod(label(\"lt\",lex(\"ConcretePart\")),[lit(\"\\\\\\<\")],{})", Factory.Production);
  private static final IConstructor prod__nonterminal_$Sym__nonterminal_$Nonterminal_ = (IConstructor) _read("prod(label(\"nonterminal\",sort(\"Sym\")),[conditional(label(\"nonterminal\",lex(\"Nonterminal\")),{\\not-follow(lit(\"[\"))})],{})", Factory.Production);
  private static final IConstructor prod__octalIntegerLiteral_$IntegerLiteral__octal_$OctalIntegerLiteral_ = (IConstructor) _read("prod(label(\"octalIntegerLiteral\",sort(\"IntegerLiteral\")),[label(\"octal\",lex(\"OctalIntegerLiteral\"))],{})", Factory.Production);
  private static final IConstructor prod__reference_$Prod__lit___58_$layouts_LAYOUTLIST_referenced_$Name_ = (IConstructor) _read("prod(label(\"reference\",sort(\"Prod\")),[lit(\":\"),layouts(\"LAYOUTLIST\"),label(\"referenced\",lex(\"Name\"))],{})", Factory.Production);
  private static final IConstructor prod__unInitialized_$Variable__name_$Name_ = (IConstructor) _read("prod(label(\"unInitialized\",sort(\"Variable\")),[label(\"name\",lex(\"Name\"))],{})", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_it_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"it\")],{})", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_for_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"for\")],{})", Factory.Production);
  private static final IConstructor prod__$Comment__lit___47_47_iter_star__char_class___range__0_9_range__11_16777215__tag__category___67_111_109_109_101_110_116 = (IConstructor) _read("prod(lex(\"Comment\"),[lit(\"//\"),conditional(\\iter-star(\\char-class([range(0,9),range(11,16777215)])),{\\not-follow(\\char-class([range(9,9),range(13,13),range(32,32),range(65,65),range(160,160),range(5760,5760),range(8192,8192),range(8239,8239),range(8287,8287),range(12288,12288)])),\\end-of-line()})],{tag(category(\"Comment\"))})", Factory.Production);
  private static final IConstructor prod__$TimePartNoTZ__char_class___range__48_50_char_class___range__48_57_char_class___range__48_53_char_class___range__48_57_char_class___range__48_53_char_class___range__48_57_opt__seq___char_class___range__44_44_range__46_46_char_class___range__48_57_opt__seq___char_class___range__48_57_opt__char_class___range__48_57_ = (IConstructor) _read("prod(lex(\"TimePartNoTZ\"),[\\char-class([range(48,50)]),\\char-class([range(48,57)]),\\char-class([range(48,53)]),\\char-class([range(48,57)]),\\char-class([range(48,53)]),\\char-class([range(48,57)]),opt(seq([\\char-class([range(44,44),range(46,46)]),\\char-class([range(48,57)]),opt(seq([\\char-class([range(48,57)]),opt(\\char-class([range(48,57)]))]))]))],{})", Factory.Production);
  private static final IConstructor regular__iter_star__char_class___range__0_9_range__11_16777215 = (IConstructor) _read("regular(\\iter-star(\\char-class([range(0,9),range(11,16777215)])))", Factory.Production);
  private static final IConstructor regular__iter_star__char_class___range__0_8_range__11_12_range__14_31_range__33_59_range__61_123_range__125_16777215 = (IConstructor) _read("regular(\\iter-star(\\char-class([range(0,8),range(11,12),range(14,31),range(33,59),range(61,123),range(125,16777215)])))", Factory.Production);
  private static final IConstructor prod__test_$FunctionModifier__lit_test_ = (IConstructor) _read("prod(label(\"test\",sort(\"FunctionModifier\")),[lit(\"test\")],{})", Factory.Production);
  private static final IConstructor prod__$StringCharacter__lit___92_char_class___range__34_34_range__39_39_range__60_60_range__62_62_range__92_92_range__98_98_range__102_102_range__110_110_range__114_114_range__116_116_ = (IConstructor) _read("prod(lex(\"StringCharacter\"),[lit(\"\\\\\"),\\char-class([range(34,34),range(39,39),range(60,60),range(62,62),range(92,92),range(98,98),range(102,102),range(110,110),range(114,114),range(116,116)])],{})", Factory.Production);
  private static final IConstructor prod__annotation_$Declaration__tags_$Tags_$layouts_LAYOUTLIST_visibility_$Visibility_$layouts_LAYOUTLIST_lit_anno_$layouts_LAYOUTLIST_annoType_$Type_$layouts_LAYOUTLIST_onType_$Type_$layouts_LAYOUTLIST_lit___64_$layouts_LAYOUTLIST_name_$Name_$layouts_LAYOUTLIST_lit___59_ = (IConstructor) _read("prod(label(\"annotation\",sort(\"Declaration\")),[label(\"tags\",sort(\"Tags\")),layouts(\"LAYOUTLIST\"),label(\"visibility\",sort(\"Visibility\")),layouts(\"LAYOUTLIST\"),lit(\"anno\"),layouts(\"LAYOUTLIST\"),label(\"annoType\",sort(\"Type\")),layouts(\"LAYOUTLIST\"),label(\"onType\",sort(\"Type\")),layouts(\"LAYOUTLIST\"),lit(\"@\"),layouts(\"LAYOUTLIST\"),label(\"name\",lex(\"Name\")),layouts(\"LAYOUTLIST\"),lit(\";\")],{})", Factory.Production);
  private static final IConstructor prod__associativity_$ProdModifier__associativity_$Assoc_ = (IConstructor) _read("prod(label(\"associativity\",sort(\"ProdModifier\")),[label(\"associativity\",sort(\"Assoc\"))],{})", Factory.Production);
  private static final IConstructor prod__characterClass_$Sym__charClass_$Class_ = (IConstructor) _read("prod(label(\"characterClass\",sort(\"Sym\")),[label(\"charClass\",sort(\"Class\"))],{})", Factory.Production);
  private static final IConstructor prod__division_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___47_$layouts_LAYOUTLIST_rhs_$Expression__assoc__left = (IConstructor) _read("prod(label(\"division\",sort(\"Expression\")),[label(\"lhs\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"/\"),layouts(\"LAYOUTLIST\"),label(\"rhs\",sort(\"Expression\"))],{assoc(left())})", Factory.Production);
  private static final IConstructor prod__external_$Import__lit_import_$layouts_LAYOUTLIST_name_$QualifiedName_$layouts_LAYOUTLIST_lit___61_$layouts_LAYOUTLIST_at_$LocationLiteral_$layouts_LAYOUTLIST_lit___59_ = (IConstructor) _read("prod(label(\"external\",sort(\"Import\")),[lit(\"import\"),layouts(\"LAYOUTLIST\"),label(\"name\",sort(\"QualifiedName\")),layouts(\"LAYOUTLIST\"),lit(\"=\"),layouts(\"LAYOUTLIST\"),label(\"at\",sort(\"LocationLiteral\")),layouts(\"LAYOUTLIST\"),lit(\";\")],{})", Factory.Production);
  private static final IConstructor prod__lit_history__char_class___range__104_104_char_class___range__105_105_char_class___range__115_115_char_class___range__116_116_char_class___range__111_111_char_class___range__114_114_char_class___range__121_121_ = (IConstructor) _read("prod(lit(\"history\"),[\\char-class([range(104,104)]),\\char-class([range(105,105)]),\\char-class([range(115,115)]),\\char-class([range(116,116)]),\\char-class([range(111,111)]),\\char-class([range(114,114)]),\\char-class([range(121,121)])],{})", Factory.Production);
  private static final IConstructor prod__iterStar_$Sym__symbol_$Sym_$layouts_LAYOUTLIST_lit___42_ = (IConstructor) _read("prod(label(\"iterStar\",sort(\"Sym\")),[label(\"symbol\",sort(\"Sym\")),layouts(\"LAYOUTLIST\"),lit(\"*\")],{})", Factory.Production);
  private static final IConstructor prod__layout_$SyntaxDefinition__vis_$Visibility_$layouts_LAYOUTLIST_lit_layout_$layouts_LAYOUTLIST_defined_$Sym_$layouts_LAYOUTLIST_lit___61_$layouts_LAYOUTLIST_production_$Prod_$layouts_LAYOUTLIST_lit___59__tag__Foldable = (IConstructor) _read("prod(label(\"layout\",sort(\"SyntaxDefinition\")),[label(\"vis\",sort(\"Visibility\")),layouts(\"LAYOUTLIST\"),lit(\"layout\"),layouts(\"LAYOUTLIST\"),label(\"defined\",sort(\"Sym\")),layouts(\"LAYOUTLIST\"),lit(\"=\"),layouts(\"LAYOUTLIST\"),label(\"production\",sort(\"Prod\")),layouts(\"LAYOUTLIST\"),lit(\";\")],{tag(Foldable())})", Factory.Production);
  private static final IConstructor prod__list_$BasicType__lit_list_ = (IConstructor) _read("prod(label(\"list\",sort(\"BasicType\")),[lit(\"list\")],{})", Factory.Production);
  private static final IConstructor prod__listDeclarations_$ShellCommand__lit_declarations_ = (IConstructor) _read("prod(label(\"listDeclarations\",sort(\"ShellCommand\")),[lit(\"declarations\")],{})", Factory.Production);
  private static final IConstructor prod__listModules_$ShellCommand__lit_modules_ = (IConstructor) _read("prod(label(\"listModules\",sort(\"ShellCommand\")),[lit(\"modules\")],{})", Factory.Production);
  private static final IConstructor prod__lit_undeclare__char_class___range__117_117_char_class___range__110_110_char_class___range__100_100_char_class___range__101_101_char_class___range__99_99_char_class___range__108_108_char_class___range__97_97_char_class___range__114_114_char_class___range__101_101_ = (IConstructor) _read("prod(lit(\"undeclare\"),[\\char-class([range(117,117)]),\\char-class([range(110,110)]),\\char-class([range(100,100)]),\\char-class([range(101,101)]),\\char-class([range(99,99)]),\\char-class([range(108,108)]),\\char-class([range(97,97)]),\\char-class([range(114,114)]),\\char-class([range(101,101)])],{})", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_o_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"o\")],{})", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_type_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"type\")],{})", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_if_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"if\")],{})", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_else_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"else\")],{})", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_in_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"in\")],{})", Factory.Production);
  private static final IConstructor prod__$Char__$UnicodeEscape__tag__category___67_111_110_115_116_97_110_116 = (IConstructor) _read("prod(lex(\"Char\"),[lex(\"UnicodeEscape\")],{tag(category(\"Constant\"))})", Factory.Production);
  private static final IConstructor prod__$DateAndTime__lit___36_$DatePart_lit_T_$TimePartNoTZ_$TimeZonePart_ = (IConstructor) _read("prod(lex(\"DateAndTime\"),[lit(\"$\"),lex(\"DatePart\"),lit(\"T\"),lex(\"TimePartNoTZ\"),lex(\"TimeZonePart\")],{})", Factory.Production);
  private static final IConstructor prod__$LAYOUT__char_class___range__9_13_range__32_32_range__133_133_range__160_160_range__5760_5760_range__6158_6158_range__8192_8202_range__8232_8233_range__8239_8239_range__8287_8287_range__12288_12288_ = (IConstructor) _read("prod(lex(\"LAYOUT\"),[\\char-class([range(9,13),range(32,32),range(133,133),range(160,160),range(5760,5760),range(6158,6158),range(8192,8202),range(8232,8233),range(8239,8239),range(8287,8287),range(12288,12288)])],{})", Factory.Production);
  private static final IConstructor prod__$RealLiteral__iter__char_class___range__48_57_char_class___range__69_69_range__101_101_opt__char_class___range__43_43_range__45_45_iter__char_class___range__48_57_opt__char_class___range__68_68_range__70_70_range__100_100_range__102_102_ = (IConstructor) _read("prod(lex(\"RealLiteral\"),[iter(\\char-class([range(48,57)])),\\char-class([range(69,69),range(101,101)]),opt(\\char-class([range(43,43),range(45,45)])),iter(\\char-class([range(48,57)])),opt(\\char-class([range(68,68),range(70,70),range(100,100),range(102,102)]))],{})", Factory.Production);
  private static final IConstructor prod__start__$EvalCommand__$layouts_LAYOUTLIST_top_$EvalCommand_$layouts_LAYOUTLIST_ = (IConstructor) _read("prod(start(sort(\"EvalCommand\")),[layouts(\"LAYOUTLIST\"),label(\"top\",sort(\"EvalCommand\")),layouts(\"LAYOUTLIST\")],{})", Factory.Production);
  private static final IConstructor regular__iter_star_seps__$Mapping__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST = (IConstructor) _read("regular(\\iter-star-seps(\\parameterized-sort(\"Mapping\",[sort(\"Expression\")]),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")]))", Factory.Production);
  private static final IConstructor regular__opt__seq___char_class___range__44_44_range__46_46_char_class___range__48_57_opt__seq___char_class___range__48_57_opt__char_class___range__48_57 = (IConstructor) _read("regular(opt(seq([\\char-class([range(44,44),range(46,46)]),\\char-class([range(48,57)]),opt(seq([\\char-class([range(48,57)]),opt(\\char-class([range(48,57)]))]))])))", Factory.Production);
  private static final IConstructor prod__lit___60_60_61__char_class___range__60_60_char_class___range__60_60_char_class___range__61_61_ = (IConstructor) _read("prod(lit(\"\\<\\<=\"),[\\char-class([range(60,60)]),\\char-class([range(60,60)]),\\char-class([range(61,61)])],{})", Factory.Production);
  private static final IConstructor prod__lit___61_61_62__char_class___range__61_61_char_class___range__61_61_char_class___range__62_62_ = (IConstructor) _read("prod(lit(\"==\\>\"),[\\char-class([range(61,61)]),\\char-class([range(61,61)]),\\char-class([range(62,62)])],{})", Factory.Production);
  private static final IConstructor prod__actualsRenaming_$ImportedModule__name_$QualifiedName_$layouts_LAYOUTLIST_actuals_$ModuleActuals_$layouts_LAYOUTLIST_renamings_$Renamings_ = (IConstructor) _read("prod(label(\"actualsRenaming\",sort(\"ImportedModule\")),[label(\"name\",sort(\"QualifiedName\")),layouts(\"LAYOUTLIST\"),label(\"actuals\",sort(\"ModuleActuals\")),layouts(\"LAYOUTLIST\"),label(\"renamings\",sort(\"Renamings\"))],{})", Factory.Production);
  private static final IConstructor prod__all_$Expression__lit_all_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_generators_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_ = (IConstructor) _read("prod(label(\"all\",sort(\"Expression\")),[lit(\"all\"),layouts(\"LAYOUTLIST\"),lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"generators\",\\iter-seps(sort(\"Expression\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\")\")],{})", Factory.Production);
  private static final IConstructor prod__bracket_$Type__lit___40_$layouts_LAYOUTLIST_type_$Type_$layouts_LAYOUTLIST_lit___41__bracket = (IConstructor) _read("prod(label(\"bracket\",sort(\"Type\")),[lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"type\",sort(\"Type\")),layouts(\"LAYOUTLIST\"),lit(\")\")],{bracket()})", Factory.Production);
  private static final IConstructor prod__comprehension_$Expression__comprehension_$Comprehension_ = (IConstructor) _read("prod(label(\"comprehension\",sort(\"Expression\")),[label(\"comprehension\",sort(\"Comprehension\"))],{})", Factory.Production);
  private static final IConstructor prod__lit_continue__char_class___range__99_99_char_class___range__111_111_char_class___range__110_110_char_class___range__116_116_char_class___range__105_105_char_class___range__110_110_char_class___range__117_117_char_class___range__101_101_ = (IConstructor) _read("prod(lit(\"continue\"),[\\char-class([range(99,99)]),\\char-class([range(111,111)]),\\char-class([range(110,110)]),\\char-class([range(116,116)]),\\char-class([range(105,105)]),\\char-class([range(110,110)]),\\char-class([range(117,117)]),\\char-class([range(101,101)])],{})", Factory.Production);
  private static final IConstructor prod__empty_$Bound__ = (IConstructor) _read("prod(label(\"empty\",sort(\"Bound\")),[],{})", Factory.Production);
  private static final IConstructor prod__emptyStatement_$Statement__lit___59_ = (IConstructor) _read("prod(label(\"emptyStatement\",sort(\"Statement\")),[lit(\";\")],{})", Factory.Production);
  private static final IConstructor prod__lexical_$SyntaxDefinition__lit_lexical_$layouts_LAYOUTLIST_defined_$Sym_$layouts_LAYOUTLIST_lit___61_$layouts_LAYOUTLIST_production_$Prod_$layouts_LAYOUTLIST_lit___59__tag__Foldable = (IConstructor) _read("prod(label(\"lexical\",sort(\"SyntaxDefinition\")),[lit(\"lexical\"),layouts(\"LAYOUTLIST\"),label(\"defined\",sort(\"Sym\")),layouts(\"LAYOUTLIST\"),lit(\"=\"),layouts(\"LAYOUTLIST\"),label(\"production\",sort(\"Prod\")),layouts(\"LAYOUTLIST\"),lit(\";\")],{tag(Foldable())})", Factory.Production);
  private static final IConstructor prod__nonEquals_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___33_61_$layouts_LAYOUTLIST_rhs_$Expression__assoc__non_assoc = (IConstructor) _read("prod(label(\"nonEquals\",sort(\"Expression\")),[label(\"lhs\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"!=\"),layouts(\"LAYOUTLIST\"),label(\"rhs\",sort(\"Expression\"))],{assoc(\\non-assoc())})", Factory.Production);
  private static final IConstructor prod__parametrized_$Sym__nonterminal_$Nonterminal_$layouts_LAYOUTLIST_lit___91_$layouts_LAYOUTLIST_parameters_iter_seps__$Sym__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___93_ = (IConstructor) _read("prod(label(\"parametrized\",sort(\"Sym\")),[conditional(label(\"nonterminal\",lex(\"Nonterminal\")),{follow(lit(\"[\"))}),layouts(\"LAYOUTLIST\"),lit(\"[\"),layouts(\"LAYOUTLIST\"),label(\"parameters\",\\iter-seps(sort(\"Sym\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\"]\")],{})", Factory.Production);
  private static final IConstructor prod__post_$StringTail__post_$PostStringChars_ = (IConstructor) _read("prod(label(\"post\",sort(\"StringTail\")),[label(\"post\",lex(\"PostStringChars\"))],{})", Factory.Production);
  private static final IConstructor prod__typeArguments_$FunctionType__type_$Type_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_arguments_iter_star_seps__$TypeArg__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_ = (IConstructor) _read("prod(label(\"typeArguments\",sort(\"FunctionType\")),[label(\"type\",sort(\"Type\")),layouts(\"LAYOUTLIST\"),lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"arguments\",\\iter-star-seps(sort(\"TypeArg\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\")\")],{})", Factory.Production);
  private static final IConstructor prod__$TagString__lit___123_contents_iter_star__alt___$TagString_char_class___range__0_122_range__124_124_range__126_16777215_seq___lit___92_char_class___range__123_123_range__125_125_lit___125_ = (IConstructor) _read("prod(lex(\"TagString\"),[conditional(lit(\"{\"),{\\not-precede(lit(\"\\\\\"))}),label(\"contents\",\\iter-star(alt({lex(\"TagString\"),\\char-class([range(0,122),range(124,124),range(126,16777215)]),seq([lit(\"\\\\\"),\\char-class([range(123,123),range(125,125)])])}))),conditional(lit(\"}\"),{\\not-precede(lit(\"\\\\\"))})],{})", Factory.Production);
  private static final IConstructor regular__iter_star__char_class___range__9_9_range__32_32_range__65_65_range__160_160_range__5760_5760_range__8192_8192_range__8239_8239_range__8287_8287_range__12288_12288 = (IConstructor) _read("regular(\\iter-star(\\char-class([range(9,9),range(32,32),range(65,65),range(160,160),range(5760,5760),range(8192,8192),range(8239,8239),range(8287,8287),range(12288,12288)])))", Factory.Production);
  private static final IConstructor prod__lit_rel__char_class___range__114_114_char_class___range__101_101_char_class___range__108_108_ = (IConstructor) _read("prod(lit(\"rel\"),[\\char-class([range(114,114)]),\\char-class([range(101,101)]),\\char-class([range(108,108)])],{})", Factory.Production);
  private static final IConstructor prod__set_$Pattern__lit___123_$layouts_LAYOUTLIST_elements_iter_star_seps__$Pattern__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_ = (IConstructor) _read("prod(label(\"set\",sort(\"Pattern\")),[lit(\"{\"),layouts(\"LAYOUTLIST\"),label(\"elements\",\\iter-star-seps(sort(\"Pattern\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\"}\")],{})", Factory.Production);
  private static final IConstructor prod__simpleCharclass_$Class__lit___91_$layouts_LAYOUTLIST_ranges_iter_star_seps__$Range__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___93_ = (IConstructor) _read("prod(label(\"simpleCharclass\",sort(\"Class\")),[lit(\"[\"),layouts(\"LAYOUTLIST\"),label(\"ranges\",\\iter-star-seps(sort(\"Range\"),[layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\"]\")],{})", Factory.Production);
  private static final IConstructor prod__tag_$Declaration__tags_$Tags_$layouts_LAYOUTLIST_visibility_$Visibility_$layouts_LAYOUTLIST_lit_tag_$layouts_LAYOUTLIST_kind_$Kind_$layouts_LAYOUTLIST_name_$Name_$layouts_LAYOUTLIST_lit_on_$layouts_LAYOUTLIST_types_iter_seps__$Type__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___59_ = (IConstructor) _read("prod(label(\"tag\",sort(\"Declaration\")),[label(\"tags\",sort(\"Tags\")),layouts(\"LAYOUTLIST\"),label(\"visibility\",sort(\"Visibility\")),layouts(\"LAYOUTLIST\"),lit(\"tag\"),layouts(\"LAYOUTLIST\"),label(\"kind\",sort(\"Kind\")),layouts(\"LAYOUTLIST\"),label(\"name\",lex(\"Name\")),layouts(\"LAYOUTLIST\"),lit(\"on\"),layouts(\"LAYOUTLIST\"),label(\"types\",\\iter-seps(sort(\"Type\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\";\")],{})", Factory.Production);
  private static final IConstructor prod__$layouts_LAYOUTLIST__iter_star__$LAYOUT_ = (IConstructor) _read("prod(layouts(\"LAYOUTLIST\"),[conditional(\\iter-star(lex(\"LAYOUT\")),{\\not-follow(lit(\"//\")),\\not-follow(lit(\"/*\")),\\not-follow(\\char-class([range(9,13),range(32,32),range(133,133),range(160,160),range(5760,5760),range(6158,6158),range(8192,8202),range(8232,8233),range(8239,8239),range(8287,8287),range(12288,12288)]))})],{})", Factory.Production);
  private static final IConstructor prod__$PrePathChars__$URLChars_lit___60_ = (IConstructor) _read("prod(lex(\"PrePathChars\"),[lex(\"URLChars\"),lit(\"\\<\")],{})", Factory.Production);
  private static final IConstructor prod__$TimeZonePart__char_class___range__43_43_range__45_45_char_class___range__48_49_char_class___range__48_57_ = (IConstructor) _read("prod(lex(\"TimeZonePart\"),[\\char-class([range(43,43),range(45,45)]),\\char-class([range(48,49)]),\\char-class([range(48,57)])],{})", Factory.Production);
  private static final IConstructor regular__iter_seps__$Case__$layouts_LAYOUTLIST = (IConstructor) _read("regular(\\iter-seps(sort(\"Case\"),[layouts(\"LAYOUTLIST\")]))", Factory.Production);
  private static final IConstructor prod__abstract_$FunctionDeclaration__tags_$Tags_$layouts_LAYOUTLIST_visibility_$Visibility_$layouts_LAYOUTLIST_signature_$Signature_$layouts_LAYOUTLIST_lit___59_ = (IConstructor) _read("prod(label(\"abstract\",sort(\"FunctionDeclaration\")),[label(\"tags\",sort(\"Tags\")),layouts(\"LAYOUTLIST\"),label(\"visibility\",sort(\"Visibility\")),layouts(\"LAYOUTLIST\"),label(\"signature\",sort(\"Signature\")),layouts(\"LAYOUTLIST\"),lit(\";\")],{})", Factory.Production);
  private static final IConstructor prod__assert_$Statement__lit_assert_$layouts_LAYOUTLIST_expression_$Expression_$layouts_LAYOUTLIST_lit___59__tag__breakable = (IConstructor) _read("prod(label(\"assert\",sort(\"Statement\")),[lit(\"assert\"),layouts(\"LAYOUTLIST\"),label(\"expression\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\";\")],{tag(breakable())})", Factory.Production);
  private static final IConstructor prod__lit_dynamic__char_class___range__100_100_char_class___range__121_121_char_class___range__110_110_char_class___range__97_97_char_class___range__109_109_char_class___range__105_105_char_class___range__99_99_ = (IConstructor) _read("prod(lit(\"dynamic\"),[\\char-class([range(100,100)]),\\char-class([range(121,121)]),\\char-class([range(110,110)]),\\char-class([range(97,97)]),\\char-class([range(109,109)]),\\char-class([range(105,105)]),\\char-class([range(99,99)])],{})", Factory.Production);
  private static final IConstructor prod__greaterThan_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___62_$layouts_LAYOUTLIST_rhs_$Expression__assoc__non_assoc = (IConstructor) _read("prod(label(\"greaterThan\",sort(\"Expression\")),[label(\"lhs\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"\\>\"),layouts(\"LAYOUTLIST\"),label(\"rhs\",sort(\"Expression\"))],{assoc(\\non-assoc())})", Factory.Production);
  private static final IConstructor prod__index_$Field__fieldIndex_$IntegerLiteral_ = (IConstructor) _read("prod(label(\"index\",sort(\"Field\")),[label(\"fieldIndex\",sort(\"IntegerLiteral\"))],{})", Factory.Production);
  private static final IConstructor prod__interpolated_$StringMiddle__mid_$MidStringChars_$layouts_LAYOUTLIST_expression_$Expression_$layouts_LAYOUTLIST_tail_$StringMiddle_ = (IConstructor) _read("prod(label(\"interpolated\",sort(\"StringMiddle\")),[label(\"mid\",lex(\"MidStringChars\")),layouts(\"LAYOUTLIST\"),label(\"expression\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),label(\"tail\",sort(\"StringMiddle\"))],{})", Factory.Production);
  private static final IConstructor prod__keyword_$SyntaxDefinition__lit_keyword_$layouts_LAYOUTLIST_defined_$Sym_$layouts_LAYOUTLIST_lit___61_$layouts_LAYOUTLIST_production_$Prod_$layouts_LAYOUTLIST_lit___59__tag__Foldable = (IConstructor) _read("prod(label(\"keyword\",sort(\"SyntaxDefinition\")),[lit(\"keyword\"),layouts(\"LAYOUTLIST\"),label(\"defined\",sort(\"Sym\")),layouts(\"LAYOUTLIST\"),lit(\"=\"),layouts(\"LAYOUTLIST\"),label(\"production\",sort(\"Prod\")),layouts(\"LAYOUTLIST\"),lit(\";\")],{tag(Foldable())})", Factory.Production);
  private static final IConstructor prod__lit_list__char_class___range__108_108_char_class___range__105_105_char_class___range__115_115_char_class___range__116_116_ = (IConstructor) _read("prod(lit(\"list\"),[\\char-class([range(108,108)]),\\char-class([range(105,105)]),\\char-class([range(115,115)]),\\char-class([range(116,116)])],{})", Factory.Production);
  private static final IConstructor prod__map_$Comprehension__lit___40_$layouts_LAYOUTLIST_from_$Expression_$layouts_LAYOUTLIST_lit___58_$layouts_LAYOUTLIST_to_$Expression_$layouts_LAYOUTLIST_lit___124_$layouts_LAYOUTLIST_generators_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41__tag__breakable___123_102_114_111_109_44_116_111_44_103_101_110_101_114_97_116_111_114_115_125 = (IConstructor) _read("prod(label(\"map\",sort(\"Comprehension\")),[lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"from\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\":\"),layouts(\"LAYOUTLIST\"),label(\"to\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"|\"),layouts(\"LAYOUTLIST\"),label(\"generators\",\\iter-seps(sort(\"Expression\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\")\")],{tag(breakable(\"{from,to,generators}\"))})", Factory.Production);
  private static final IConstructor prod__map_$Pattern__lit___40_$layouts_LAYOUTLIST_mappings_iter_star_seps__$Mapping__$Pattern__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_ = (IConstructor) _read("prod(label(\"map\",sort(\"Pattern\")),[lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"mappings\",\\iter-star-seps(\\parameterized-sort(\"Mapping\",[sort(\"Pattern\")]),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\")\")],{})", Factory.Production);
  private static final IConstructor prod__present_$Start__lit_start_ = (IConstructor) _read("prod(label(\"present\",sort(\"Start\")),[lit(\"start\")],{})", Factory.Production);
  private static final IConstructor prod__qualifiedName_$Expression__qualifiedName_$QualifiedName_ = (IConstructor) _read("prod(label(\"qualifiedName\",sort(\"Expression\")),[label(\"qualifiedName\",sort(\"QualifiedName\"))],{})", Factory.Production);
  private static final IConstructor prod__reifyType_$Expression__lit___35_$layouts_LAYOUTLIST_type_$Type_ = (IConstructor) _read("prod(label(\"reifyType\",sort(\"Expression\")),[lit(\"#\"),layouts(\"LAYOUTLIST\"),conditional(label(\"type\",sort(\"Type\")),{\\not-follow(lit(\"[\")),except(\"selector\")})],{})", Factory.Production);
  private static final IConstructor prod__setAnnotation_$Expression__expression_$Expression_$layouts_LAYOUTLIST_lit___91_$layouts_LAYOUTLIST_lit___64_$layouts_LAYOUTLIST_name_$Name_$layouts_LAYOUTLIST_lit___61_$layouts_LAYOUTLIST_value_$Expression_$layouts_LAYOUTLIST_lit___93_ = (IConstructor) _read("prod(label(\"setAnnotation\",sort(\"Expression\")),[label(\"expression\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"[\"),layouts(\"LAYOUTLIST\"),lit(\"@\"),layouts(\"LAYOUTLIST\"),label(\"name\",lex(\"Name\")),layouts(\"LAYOUTLIST\"),lit(\"=\"),layouts(\"LAYOUTLIST\"),label(\"value\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"]\")],{})", Factory.Production);
  private static final IConstructor prod__lit_test__char_class___range__116_116_char_class___range__101_101_char_class___range__115_115_char_class___range__116_116_ = (IConstructor) _read("prod(lit(\"test\"),[\\char-class([range(116,116)]),\\char-class([range(101,101)]),\\char-class([range(115,115)]),\\char-class([range(116,116)])],{})", Factory.Production);
  private static final IConstructor prod__withThrows_$Signature__modifiers_$FunctionModifiers_$layouts_LAYOUTLIST_type_$Type_$layouts_LAYOUTLIST_name_$Name_$layouts_LAYOUTLIST_parameters_$Parameters_$layouts_LAYOUTLIST_lit_throws_$layouts_LAYOUTLIST_exceptions_iter_seps__$Type__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_ = (IConstructor) _read("prod(label(\"withThrows\",sort(\"Signature\")),[label(\"modifiers\",sort(\"FunctionModifiers\")),layouts(\"LAYOUTLIST\"),label(\"type\",sort(\"Type\")),layouts(\"LAYOUTLIST\"),label(\"name\",lex(\"Name\")),layouts(\"LAYOUTLIST\"),label(\"parameters\",sort(\"Parameters\")),layouts(\"LAYOUTLIST\"),lit(\"throws\"),layouts(\"LAYOUTLIST\"),label(\"exceptions\",\\iter-seps(sort(\"Type\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")]))],{})", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_false_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"false\")],{})", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_tuple_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"tuple\")],{})", Factory.Production);
  private static final IConstructor prod__$DecimalIntegerLiteral__char_class___range__49_57_iter_star__char_class___range__48_57_ = (IConstructor) _read("prod(lex(\"DecimalIntegerLiteral\"),[\\char-class([range(49,57)]),conditional(\\iter-star(\\char-class([range(48,57)])),{\\not-follow(\\char-class([range(48,57),range(65,90),range(95,95),range(97,122)]))})],{})", Factory.Production);
  private static final IConstructor prod__$LAYOUT__$Comment_ = (IConstructor) _read("prod(lex(\"LAYOUT\"),[lex(\"Comment\")],{})", Factory.Production);
  private static final IConstructor prod__$ProtocolChars__char_class___range__124_124_$URLChars_lit___58_47_47_ = (IConstructor) _read("prod(lex(\"ProtocolChars\"),[\\char-class([range(124,124)]),lex(\"URLChars\"),conditional(lit(\"://\"),{\\not-follow(\\char-class([range(9,10),range(13,13),range(32,32),range(65,65),range(160,160),range(5760,5760),range(8192,8192),range(8239,8239),range(8287,8287),range(12288,12288)]))})],{})", Factory.Production);
  private static final IConstructor regular__iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST = (IConstructor) _read("regular(\\iter-seps(sort(\"Expression\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")]))", Factory.Production);
  private static final IConstructor regular__iter_seps__$Sym__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST = (IConstructor) _read("regular(\\iter-seps(sort(\"Sym\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")]))", Factory.Production);
  private static final IConstructor regular__iter_seps__$Sym__$layouts_LAYOUTLIST_lit___124_$layouts_LAYOUTLIST = (IConstructor) _read("regular(\\iter-seps(sort(\"Sym\"),[layouts(\"LAYOUTLIST\"),lit(\"|\"),layouts(\"LAYOUTLIST\")]))", Factory.Production);
  private static final IConstructor prod__any_$Expression__lit_any_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_generators_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_ = (IConstructor) _read("prod(label(\"any\",sort(\"Expression\")),[lit(\"any\"),layouts(\"LAYOUTLIST\"),lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"generators\",\\iter-seps(sort(\"Expression\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\")\")],{})", Factory.Production);
  private static final IConstructor prod__lit_bag__char_class___range__98_98_char_class___range__97_97_char_class___range__103_103_ = (IConstructor) _read("prod(lit(\"bag\"),[\\char-class([range(98,98)]),\\char-class([range(97,97)]),\\char-class([range(103,103)])],{})", Factory.Production);
  private static final IConstructor prod__lit_bottom_up_break__char_class___range__98_98_char_class___range__111_111_char_class___range__116_116_char_class___range__116_116_char_class___range__111_111_char_class___range__109_109_char_class___range__45_45_char_class___range__117_117_char_class___range__112_112_char_class___range__45_45_char_class___range__98_98_char_class___range__114_114_char_class___range__101_101_char_class___range__97_97_char_class___range__107_107_ = (IConstructor) _read("prod(lit(\"bottom-up-break\"),[\\char-class([range(98,98)]),\\char-class([range(111,111)]),\\char-class([range(116,116)]),\\char-class([range(116,116)]),\\char-class([range(111,111)]),\\char-class([range(109,109)]),\\char-class([range(45,45)]),\\char-class([range(117,117)]),\\char-class([range(112,112)]),\\char-class([range(45,45)]),\\char-class([range(98,98)]),\\char-class([range(114,114)]),\\char-class([range(101,101)]),\\char-class([range(97,97)]),\\char-class([range(107,107)])],{})", Factory.Production);
  private static final IConstructor prod__bounded_$TypeVar__lit___38_$layouts_LAYOUTLIST_name_$Name_$layouts_LAYOUTLIST_lit___60_58_$layouts_LAYOUTLIST_bound_$Type_ = (IConstructor) _read("prod(label(\"bounded\",sort(\"TypeVar\")),[lit(\"&\"),layouts(\"LAYOUTLIST\"),label(\"name\",lex(\"Name\")),layouts(\"LAYOUTLIST\"),lit(\"\\<:\"),layouts(\"LAYOUTLIST\"),label(\"bound\",sort(\"Type\"))],{})", Factory.Production);
  private static final IConstructor prod__break_$Statement__lit_break_$layouts_LAYOUTLIST_target_$Target_$layouts_LAYOUTLIST_lit___59__tag__breakable = (IConstructor) _read("prod(label(\"break\",sort(\"Statement\")),[lit(\"break\"),layouts(\"LAYOUTLIST\"),label(\"target\",sort(\"Target\")),layouts(\"LAYOUTLIST\"),lit(\";\")],{tag(breakable())})", Factory.Production);
  private static final IConstructor prod__closure_$Expression__type_$Type_$layouts_LAYOUTLIST_parameters_$Parameters_$layouts_LAYOUTLIST_lit___123_$layouts_LAYOUTLIST_statements_iter_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_ = (IConstructor) _read("prod(label(\"closure\",sort(\"Expression\")),[label(\"type\",sort(\"Type\")),layouts(\"LAYOUTLIST\"),label(\"parameters\",sort(\"Parameters\")),layouts(\"LAYOUTLIST\"),lit(\"{\"),layouts(\"LAYOUTLIST\"),label(\"statements\",\\iter-seps(sort(\"Statement\"),[layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\"}\")],{})", Factory.Production);
  private static final IConstructor prod__complement_$Class__lit___33_$layouts_LAYOUTLIST_charClass_$Class_ = (IConstructor) _read("prod(label(\"complement\",sort(\"Class\")),[lit(\"!\"),layouts(\"LAYOUTLIST\"),label(\"charClass\",sort(\"Class\"))],{})", Factory.Production);
  private static final IConstructor prod__default_$Import__lit_import_$layouts_LAYOUTLIST_module_$ImportedModule_$layouts_LAYOUTLIST_lit___59_ = (IConstructor) _read("prod(label(\"default\",sort(\"Import\")),[lit(\"import\"),layouts(\"LAYOUTLIST\"),label(\"module\",sort(\"ImportedModule\")),layouts(\"LAYOUTLIST\"),lit(\";\")],{})", Factory.Production);
  private static final IConstructor prod__fail_$Statement__lit_fail_$layouts_LAYOUTLIST_target_$Target_$layouts_LAYOUTLIST_lit___59__tag__breakable = (IConstructor) _read("prod(label(\"fail\",sort(\"Statement\")),[lit(\"fail\"),layouts(\"LAYOUTLIST\"),label(\"target\",sort(\"Target\")),layouts(\"LAYOUTLIST\"),lit(\";\")],{tag(breakable())})", Factory.Production);
  private static final IConstructor prod__lit_false__char_class___range__102_102_char_class___range__97_97_char_class___range__108_108_char_class___range__115_115_char_class___range__101_101_ = (IConstructor) _read("prod(lit(\"false\"),[\\char-class([range(102,102)]),\\char-class([range(97,97)]),\\char-class([range(108,108)]),\\char-class([range(115,115)]),\\char-class([range(101,101)])],{})", Factory.Production);
  private static final IConstructor prod__in_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit_in_$layouts_LAYOUTLIST_rhs_$Expression__assoc__non_assoc = (IConstructor) _read("prod(label(\"in\",sort(\"Expression\")),[label(\"lhs\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"in\"),layouts(\"LAYOUTLIST\"),label(\"rhs\",sort(\"Expression\"))],{assoc(\\non-assoc())})", Factory.Production);
  private static final IConstructor prod__list_$Pattern__lit___91_$layouts_LAYOUTLIST_elements_iter_star_seps__$Pattern__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___93_ = (IConstructor) _read("prod(label(\"list\",sort(\"Pattern\")),[lit(\"[\"),layouts(\"LAYOUTLIST\"),label(\"elements\",\\iter-star-seps(sort(\"Pattern\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\"]\")],{})", Factory.Production);
  private static final IConstructor prod__literal_$Pattern__literal_$Literal_ = (IConstructor) _read("prod(label(\"literal\",sort(\"Pattern\")),[label(\"literal\",sort(\"Literal\"))],{})", Factory.Production);
  private static final IConstructor prod__lit_lrel__char_class___range__108_108_char_class___range__114_114_char_class___range__101_101_char_class___range__108_108_ = (IConstructor) _read("prod(lit(\"lrel\"),[\\char-class([range(108,108)]),\\char-class([range(114,114)]),\\char-class([range(101,101)]),\\char-class([range(108,108)])],{})", Factory.Production);
  private static final IConstructor prod__lit_set__char_class___range__115_115_char_class___range__101_101_char_class___range__116_116_ = (IConstructor) _read("prod(lit(\"set\"),[\\char-class([range(115,115)]),\\char-class([range(101,101)]),\\char-class([range(116,116)])],{})", Factory.Production);
  private static final IConstructor prod__set_$BasicType__lit_set_ = (IConstructor) _read("prod(label(\"set\",sort(\"BasicType\")),[lit(\"set\")],{})", Factory.Production);
  private static final IConstructor prod__string_$BasicType__lit_str_ = (IConstructor) _read("prod(label(\"string\",sort(\"BasicType\")),[lit(\"str\")],{})", Factory.Production);
  private static final IConstructor prod__lit_throws__char_class___range__116_116_char_class___range__104_104_char_class___range__114_114_char_class___range__111_111_char_class___range__119_119_char_class___range__115_115_ = (IConstructor) _read("prod(lit(\"throws\"),[\\char-class([range(116,116)]),\\char-class([range(104,104)]),\\char-class([range(114,114)]),\\char-class([range(111,111)]),\\char-class([range(119,119)]),\\char-class([range(115,115)])],{})", Factory.Production);
  private static final IConstructor prod__$NamedBackslash__char_class___range__92_92_ = (IConstructor) _read("prod(lex(\"NamedBackslash\"),[conditional(\\char-class([range(92,92)]),{\\not-follow(\\char-class([range(60,60),range(62,62),range(92,92)]))})],{})", Factory.Production);
  private static final IConstructor prod__start__$Commands__$layouts_LAYOUTLIST_top_$Commands_$layouts_LAYOUTLIST_ = (IConstructor) _read("prod(start(sort(\"Commands\")),[layouts(\"LAYOUTLIST\"),label(\"top\",sort(\"Commands\")),layouts(\"LAYOUTLIST\")],{})", Factory.Production);
  private static final IConstructor prod__lit_visit__char_class___range__118_118_char_class___range__105_105_char_class___range__115_115_char_class___range__105_105_char_class___range__116_116_ = (IConstructor) _read("prod(lit(\"visit\"),[\\char-class([range(118,118)]),\\char-class([range(105,105)]),\\char-class([range(115,115)]),\\char-class([range(105,105)]),\\char-class([range(116,116)])],{})", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_non_assoc_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"non-assoc\")],{})", Factory.Production);
  private static final IConstructor regular__iter_seps__$Assignable__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST = (IConstructor) _read("regular(\\iter-seps(sort(\"Assignable\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")]))", Factory.Production);
  private static final IConstructor regular__iter_seps__$Variable__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST = (IConstructor) _read("regular(\\iter-seps(sort(\"Variable\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")]))", Factory.Production);
  private static final IConstructor prod__default_$Header__tags_$Tags_$layouts_LAYOUTLIST_lit_module_$layouts_LAYOUTLIST_name_$QualifiedName_$layouts_LAYOUTLIST_imports_iter_star_seps__$Import__$layouts_LAYOUTLIST_ = (IConstructor) _read("prod(label(\"default\",sort(\"Header\")),[label(\"tags\",sort(\"Tags\")),layouts(\"LAYOUTLIST\"),lit(\"module\"),layouts(\"LAYOUTLIST\"),label(\"name\",sort(\"QualifiedName\")),layouts(\"LAYOUTLIST\"),label(\"imports\",\\iter-star-seps(sort(\"Import\"),[layouts(\"LAYOUTLIST\")]))],{})", Factory.Production);
  private static final IConstructor prod__default_$Visibility__ = (IConstructor) _read("prod(label(\"default\",sort(\"Visibility\")),[],{})", Factory.Production);
  private static final IConstructor prod__functionDeclaration_$Statement__functionDeclaration_$FunctionDeclaration__tag__breakable = (IConstructor) _read("prod(label(\"functionDeclaration\",sort(\"Statement\")),[label(\"functionDeclaration\",sort(\"FunctionDeclaration\"))],{tag(breakable())})", Factory.Production);
  private static final IConstructor prod__givenVisibility_$Toplevel__declaration_$Declaration_ = (IConstructor) _read("prod(label(\"givenVisibility\",sort(\"Toplevel\")),[label(\"declaration\",sort(\"Declaration\"))],{})", Factory.Production);
  private static final IConstructor prod__insert_$Statement__lit_insert_$layouts_LAYOUTLIST_dataTarget_$DataTarget_$layouts_LAYOUTLIST_statement_$Statement__assoc__non_assoc_tag__breakable = (IConstructor) _read("prod(label(\"insert\",sort(\"Statement\")),[lit(\"insert\"),layouts(\"LAYOUTLIST\"),label(\"dataTarget\",sort(\"DataTarget\")),layouts(\"LAYOUTLIST\"),label(\"statement\",conditional(sort(\"Statement\"),{except(\"functionDeclaration\"),except(\"variableDeclaration\")}))],{assoc(\\non-assoc()),tag(breakable())})", Factory.Production);
  private static final IConstructor prod__intersection_$Class__lhs_$Class_$layouts_LAYOUTLIST_lit___38_38_$layouts_LAYOUTLIST_rhs_$Class__assoc__left = (IConstructor) _read("prod(label(\"intersection\",sort(\"Class\")),[label(\"lhs\",sort(\"Class\")),layouts(\"LAYOUTLIST\"),lit(\"&&\"),layouts(\"LAYOUTLIST\"),label(\"rhs\",sort(\"Class\"))],{assoc(left())})", Factory.Production);
  private static final IConstructor prod__isDefined_$Expression__argument_$Expression_$layouts_LAYOUTLIST_lit___63_ = (IConstructor) _read("prod(label(\"isDefined\",sort(\"Expression\")),[label(\"argument\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"?\")],{})", Factory.Production);
  private static final IConstructor prod__multiVariable_$Pattern__qualifiedName_$QualifiedName_$layouts_LAYOUTLIST_lit___42_ = (IConstructor) _read("prod(label(\"multiVariable\",sort(\"Pattern\")),[label(\"qualifiedName\",sort(\"QualifiedName\")),layouts(\"LAYOUTLIST\"),lit(\"*\")],{})", Factory.Production);
  private static final IConstructor prod__notPrecede_$Sym__match_$Sym_$layouts_LAYOUTLIST_lit___33_60_60_$layouts_LAYOUTLIST_symbol_$Sym__assoc__right = (IConstructor) _read("prod(label(\"notPrecede\",sort(\"Sym\")),[label(\"match\",sort(\"Sym\")),layouts(\"LAYOUTLIST\"),lit(\"!\\<\\<\"),layouts(\"LAYOUTLIST\"),label(\"symbol\",sort(\"Sym\"))],{assoc(right())})", Factory.Production);
  private static final IConstructor prod__or_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___124_124_$layouts_LAYOUTLIST_rhs_$Expression__assoc__left = (IConstructor) _read("prod(label(\"or\",sort(\"Expression\")),[label(\"lhs\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"||\"),layouts(\"LAYOUTLIST\"),label(\"rhs\",sort(\"Expression\"))],{assoc(left())})", Factory.Production);
  private static final IConstructor prod__lit_public__char_class___range__112_112_char_class___range__117_117_char_class___range__98_98_char_class___range__108_108_char_class___range__105_105_char_class___range__99_99_ = (IConstructor) _read("prod(lit(\"public\"),[\\char-class([range(112,112)]),\\char-class([range(117,117)]),\\char-class([range(98,98)]),\\char-class([range(108,108)]),\\char-class([range(105,105)]),\\char-class([range(99,99)])],{})", Factory.Production);
  private static final IConstructor prod__lit_rat__char_class___range__114_114_char_class___range__97_97_char_class___range__116_116_ = (IConstructor) _read("prod(lit(\"rat\"),[\\char-class([range(114,114)]),\\char-class([range(97,97)]),\\char-class([range(116,116)])],{})", Factory.Production);
  private static final IConstructor prod__regExp_$Literal__regExpLiteral_$RegExpLiteral_ = (IConstructor) _read("prod(label(\"regExp\",sort(\"Literal\")),[label(\"regExpLiteral\",lex(\"RegExpLiteral\"))],{})", Factory.Production);
  private static final IConstructor prod__lit_renaming__char_class___range__114_114_char_class___range__101_101_char_class___range__110_110_char_class___range__97_97_char_class___range__109_109_char_class___range__105_105_char_class___range__110_110_char_class___range__103_103_ = (IConstructor) _read("prod(lit(\"renaming\"),[\\char-class([range(114,114)]),\\char-class([range(101,101)]),\\char-class([range(110,110)]),\\char-class([range(97,97)]),\\char-class([range(109,109)]),\\char-class([range(105,105)]),\\char-class([range(110,110)]),\\char-class([range(103,103)])],{})", Factory.Production);
  private static final IConstructor prod__setOption_$ShellCommand__lit_set_$layouts_LAYOUTLIST_name_$QualifiedName_$layouts_LAYOUTLIST_expression_$Expression_ = (IConstructor) _read("prod(label(\"setOption\",sort(\"ShellCommand\")),[lit(\"set\"),layouts(\"LAYOUTLIST\"),label(\"name\",sort(\"QualifiedName\")),layouts(\"LAYOUTLIST\"),label(\"expression\",sort(\"Expression\"))],{})", Factory.Production);
  private static final IConstructor prod__unimport_$ShellCommand__lit_unimport_$layouts_LAYOUTLIST_name_$QualifiedName_ = (IConstructor) _read("prod(label(\"unimport\",sort(\"ShellCommand\")),[lit(\"unimport\"),layouts(\"LAYOUTLIST\"),label(\"name\",sort(\"QualifiedName\"))],{})", Factory.Production);
  private static final IConstructor prod__lit_value__char_class___range__118_118_char_class___range__97_97_char_class___range__108_108_char_class___range__117_117_char_class___range__101_101_ = (IConstructor) _read("prod(lit(\"value\"),[\\char-class([range(118,118)]),\\char-class([range(97,97)]),\\char-class([range(108,108)]),\\char-class([range(117,117)]),\\char-class([range(101,101)])],{})", Factory.Production);
  private static final IConstructor prod__value_$BasicType__lit_value_ = (IConstructor) _read("prod(label(\"value\",sort(\"BasicType\")),[lit(\"value\")],{})", Factory.Production);
  private static final IConstructor prod__while_$StringTemplate__lit_while_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_condition_$Expression_$layouts_LAYOUTLIST_lit___41_$layouts_LAYOUTLIST_lit___123_$layouts_LAYOUTLIST_preStats_iter_star_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_body_$StringMiddle_$layouts_LAYOUTLIST_postStats_iter_star_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_ = (IConstructor) _read("prod(label(\"while\",sort(\"StringTemplate\")),[lit(\"while\"),layouts(\"LAYOUTLIST\"),lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"condition\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\")\"),layouts(\"LAYOUTLIST\"),lit(\"{\"),layouts(\"LAYOUTLIST\"),label(\"preStats\",\\iter-star-seps(sort(\"Statement\"),[layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),label(\"body\",sort(\"StringMiddle\")),layouts(\"LAYOUTLIST\"),label(\"postStats\",\\iter-star-seps(sort(\"Statement\"),[layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\"}\")],{})", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_alias_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"alias\")],{})", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_value_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"value\")],{})", Factory.Production);
  private static final IConstructor prod__$Name__char_class___range__92_92_char_class___range__65_90_range__95_95_range__97_122_iter_star__char_class___range__45_45_range__48_57_range__65_90_range__95_95_range__97_122_ = (IConstructor) _read("prod(lex(\"Name\"),[\\char-class([range(92,92)]),\\char-class([range(65,90),range(95,95),range(97,122)]),conditional(\\iter-star(\\char-class([range(45,45),range(48,57),range(65,90),range(95,95),range(97,122)])),{\\not-follow(\\char-class([range(45,45),range(48,57),range(65,90),range(95,95),range(97,122)]))})],{})", Factory.Production);
  private static final IConstructor prod__$PathChars__$URLChars_char_class___range__124_124_ = (IConstructor) _read("prod(lex(\"PathChars\"),[lex(\"URLChars\"),\\char-class([range(124,124)])],{})", Factory.Production);
  private static final IConstructor prod__$RealLiteral__lit___46_iter__char_class___range__48_57_opt__char_class___range__68_68_range__70_70_range__100_100_range__102_102_ = (IConstructor) _read("prod(lex(\"RealLiteral\"),[conditional(lit(\".\"),{\\not-precede(\\char-class([range(46,46)]))}),iter(\\char-class([range(48,57)])),opt(\\char-class([range(68,68),range(70,70),range(100,100),range(102,102)]))],{})", Factory.Production);
  private static final IConstructor prod__empty_$Target__ = (IConstructor) _read("prod(label(\"empty\",sort(\"Target\")),[],{})", Factory.Production);
  private static final IConstructor prod__lit_module__char_class___range__109_109_char_class___range__111_111_char_class___range__100_100_char_class___range__117_117_char_class___range__108_108_char_class___range__101_101_ = (IConstructor) _read("prod(lit(\"module\"),[\\char-class([range(109,109)]),\\char-class([range(111,111)]),\\char-class([range(100,100)]),\\char-class([range(117,117)]),\\char-class([range(108,108)]),\\char-class([range(101,101)])],{})", Factory.Production);
  private static final IConstructor prod__nonInterpolated_$ProtocolPart__protocolChars_$ProtocolChars_ = (IConstructor) _read("prod(label(\"nonInterpolated\",sort(\"ProtocolPart\")),[label(\"protocolChars\",lex(\"ProtocolChars\"))],{})", Factory.Production);
  private static final IConstructor prod__post_$PathTail__post_$PostPathChars_ = (IConstructor) _read("prod(label(\"post\",sort(\"PathTail\")),[label(\"post\",lex(\"PostPathChars\"))],{})", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_int_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"int\")],{})", Factory.Production);
  private static final IConstructor prod__$HexIntegerLiteral__char_class___range__48_48_char_class___range__88_88_range__120_120_iter__char_class___range__48_57_range__65_70_range__97_102_ = (IConstructor) _read("prod(lex(\"HexIntegerLiteral\"),[\\char-class([range(48,48)]),\\char-class([range(88,88),range(120,120)]),conditional(iter(\\char-class([range(48,57),range(65,70),range(97,102)])),{\\not-follow(\\char-class([range(48,57),range(65,90),range(95,95),range(97,122)]))})],{})", Factory.Production);
  private static final IConstructor prod__start__$Module__$layouts_LAYOUTLIST_top_$Module_$layouts_LAYOUTLIST_ = (IConstructor) _read("prod(start(sort(\"Module\")),[layouts(\"LAYOUTLIST\"),label(\"top\",sort(\"Module\")),layouts(\"LAYOUTLIST\")],{})", Factory.Production);
    
  // Item declarations
	
	
  protected static class $BooleanLiteral {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__$BooleanLiteral__lit_true_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-593, 0, prod__lit_true__char_class___range__116_116_char_class___range__114_114_char_class___range__117_117_char_class___range__101_101_, new int[] {116,114,117,101}, null, null);
      builder.addAlternative(RascalParser.prod__$BooleanLiteral__lit_true_, tmp);
	}
    protected static final void _init_prod__$BooleanLiteral__lit_false_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-590, 0, prod__lit_false__char_class___range__102_102_char_class___range__97_97_char_class___range__108_108_char_class___range__115_115_char_class___range__101_101_, new int[] {102,97,108,115,101}, null, null);
      builder.addAlternative(RascalParser.prod__$BooleanLiteral__lit_false_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__$BooleanLiteral__lit_true_(builder);
      
        _init_prod__$BooleanLiteral__lit_false_(builder);
      
    }
  }
	
  protected static class $DateAndTime {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__$DateAndTime__lit___36_$DatePart_lit_T_$TimePartNoTZ_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[4];
      
      tmp[3] = new NonTerminalStackNode<IConstructor>(-75, 3, "$TimePartNoTZ", null, new ICompletionFilter[] {new CharFollowRestriction(new int[][]{{43,43},{45,45}})});
      tmp[2] = new LiteralStackNode<IConstructor>(-71, 2, prod__lit_T__char_class___range__84_84_, new int[] {84}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-70, 1, "$DatePart", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-68, 0, prod__lit___36__char_class___range__36_36_, new int[] {36}, null, null);
      builder.addAlternative(RascalParser.prod__$DateAndTime__lit___36_$DatePart_lit_T_$TimePartNoTZ_, tmp);
	}
    protected static final void _init_prod__$DateAndTime__lit___36_$DatePart_lit_T_$TimePartNoTZ_$TimeZonePart_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(-85, 4, "$TimeZonePart", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-83, 3, "$TimePartNoTZ", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(-81, 2, prod__lit_T__char_class___range__84_84_, new int[] {84}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-80, 1, "$DatePart", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-78, 0, prod__lit___36__char_class___range__36_36_, new int[] {36}, null, null);
      builder.addAlternative(RascalParser.prod__$DateAndTime__lit___36_$DatePart_lit_T_$TimePartNoTZ_$TimeZonePart_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__$DateAndTime__lit___36_$DatePart_lit_T_$TimePartNoTZ_(builder);
      
        _init_prod__$DateAndTime__lit___36_$DatePart_lit_T_$TimePartNoTZ_$TimeZonePart_(builder);
      
    }
  }
	
  protected static class $Pattern {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__anti_$Pattern__lit___33_$layouts_LAYOUTLIST_pattern_$Pattern_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode<IConstructor>(-359, 2, "$Pattern", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-357, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-355, 0, prod__lit___33__char_class___range__33_33_, new int[] {33}, null, null);
      builder.addAlternative(RascalParser.prod__anti_$Pattern__lit___33_$layouts_LAYOUTLIST_pattern_$Pattern_, tmp);
	}
    protected static final void _init_prod__asType_$Pattern__lit___91_$layouts_LAYOUTLIST_type_$Type_$layouts_LAYOUTLIST_lit___93_$layouts_LAYOUTLIST_argument_$Pattern_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[7];
      
      tmp[6] = new NonTerminalStackNode<IConstructor>(-336, 6, "$Pattern", null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(-334, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode<IConstructor>(-332, 4, prod__lit___93__char_class___range__93_93_, new int[] {93}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-331, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(-328, 2, "$Type", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-326, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-324, 0, prod__lit___91__char_class___range__91_91_, new int[] {91}, null, null);
      builder.addAlternative(RascalParser.prod__asType_$Pattern__lit___91_$layouts_LAYOUTLIST_type_$Type_$layouts_LAYOUTLIST_lit___93_$layouts_LAYOUTLIST_argument_$Pattern_, tmp);
	}
    protected static final void _init_prod__callOrTree_$Pattern__expression_$Pattern_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_arguments_iter_star_seps__$Pattern__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[7];
      
      tmp[6] = new LiteralStackNode<IConstructor>(-199, 6, prod__lit___41__char_class___range__41_41_, new int[] {41}, null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(-198, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new SeparatedListStackNode<IConstructor>(-195, 4, regular__iter_star_seps__$Pattern__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(-189, 0, "$Pattern", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(-191, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode<IConstructor>(-192, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode<IConstructor>(-194, 3, "$layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-187, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(-185, 2, prod__lit___40__char_class___range__40_40_, new int[] {40}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-184, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-181, 0, "$Pattern", null, null);
      builder.addAlternative(RascalParser.prod__callOrTree_$Pattern__expression_$Pattern_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_arguments_iter_star_seps__$Pattern__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_, tmp);
	}
    protected static final void _init_prod__concrete_$Pattern__concrete_$Concrete_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(-106, 0, "$Concrete", null, null);
      builder.addAlternative(RascalParser.prod__concrete_$Pattern__concrete_$Concrete_, tmp);
	}
    protected static final void _init_prod__descendant_$Pattern__lit___47_$layouts_LAYOUTLIST_pattern_$Pattern_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode<IConstructor>(-300, 2, "$Pattern", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-298, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-296, 0, prod__lit___47__char_class___range__47_47_, new int[] {47}, null, null);
      builder.addAlternative(RascalParser.prod__descendant_$Pattern__lit___47_$layouts_LAYOUTLIST_pattern_$Pattern_, tmp);
	}
    protected static final void _init_prod__list_$Pattern__lit___91_$layouts_LAYOUTLIST_elements_iter_star_seps__$Pattern__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___93_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new LiteralStackNode<IConstructor>(-167, 4, prod__lit___93__char_class___range__93_93_, new int[] {93}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-166, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new SeparatedListStackNode<IConstructor>(-163, 2, regular__iter_star_seps__$Pattern__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(-157, 0, "$Pattern", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(-159, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode<IConstructor>(-160, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode<IConstructor>(-162, 3, "$layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-155, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-153, 0, prod__lit___91__char_class___range__91_91_, new int[] {91}, null, null);
      builder.addAlternative(RascalParser.prod__list_$Pattern__lit___91_$layouts_LAYOUTLIST_elements_iter_star_seps__$Pattern__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___93_, tmp);
	}
    protected static final void _init_prod__literal_$Pattern__literal_$Literal_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(-228, 0, "$Literal", null, null);
      builder.addAlternative(RascalParser.prod__literal_$Pattern__literal_$Literal_, tmp);
	}
    protected static final void _init_prod__map_$Pattern__lit___40_$layouts_LAYOUTLIST_mappings_iter_star_seps__$Mapping__$Pattern__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new LiteralStackNode<IConstructor>(-249, 4, prod__lit___41__char_class___range__41_41_, new int[] {41}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-248, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new SeparatedListStackNode<IConstructor>(-245, 2, regular__iter_star_seps__$Mapping__$Pattern__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(-239, 0, "$Mapping__$Pattern", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(-241, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode<IConstructor>(-242, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode<IConstructor>(-244, 3, "$layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-235, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-233, 0, prod__lit___40__char_class___range__40_40_, new int[] {40}, null, null);
      builder.addAlternative(RascalParser.prod__map_$Pattern__lit___40_$layouts_LAYOUTLIST_mappings_iter_star_seps__$Mapping__$Pattern__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_, tmp);
	}
    protected static final void _init_prod__multiVariable_$Pattern__qualifiedName_$QualifiedName_$layouts_LAYOUTLIST_lit___42_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new LiteralStackNode<IConstructor>(-129, 2, prod__lit___42__char_class___range__42_42_, new int[] {42}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-128, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-125, 0, "$QualifiedName", null, null);
      builder.addAlternative(RascalParser.prod__multiVariable_$Pattern__qualifiedName_$QualifiedName_$layouts_LAYOUTLIST_lit___42_, tmp);
	}
    protected static final void _init_prod__negative_$Pattern__lit___$layouts_LAYOUTLIST_argument_$Pattern_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode<IConstructor>(-119, 2, "$Pattern", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-117, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-115, 0, prod__lit____char_class___range__45_45_, new int[] {45}, null, null);
      builder.addAlternative(RascalParser.prod__negative_$Pattern__lit___$layouts_LAYOUTLIST_argument_$Pattern_, tmp);
	}
    protected static final void _init_prod__qualifiedName_$Pattern__qualifiedName_$QualifiedName_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(-222, 0, "$QualifiedName", null, null);
      builder.addAlternative(RascalParser.prod__qualifiedName_$Pattern__qualifiedName_$QualifiedName_, tmp);
	}
    protected static final void _init_prod__reifiedType_$Pattern__lit_type_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_symbol_$Pattern_$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_definitions_$Pattern_$layouts_LAYOUTLIST_lit___41_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[11];
      
      tmp[10] = new LiteralStackNode<IConstructor>(-272, 10, prod__lit___41__char_class___range__41_41_, new int[] {41}, null, null);
      tmp[9] = new NonTerminalStackNode<IConstructor>(-271, 9, "$layouts_LAYOUTLIST", null, null);
      tmp[8] = new NonTerminalStackNode<IConstructor>(-268, 8, "$Pattern", null, null);
      tmp[7] = new NonTerminalStackNode<IConstructor>(-266, 7, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new LiteralStackNode<IConstructor>(-264, 6, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(-263, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new NonTerminalStackNode<IConstructor>(-260, 4, "$Pattern", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-258, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(-256, 2, prod__lit___40__char_class___range__40_40_, new int[] {40}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-255, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-253, 0, prod__lit_type__char_class___range__116_116_char_class___range__121_121_char_class___range__112_112_char_class___range__101_101_, new int[] {116,121,112,101}, null, null);
      builder.addAlternative(RascalParser.prod__reifiedType_$Pattern__lit_type_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_symbol_$Pattern_$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_definitions_$Pattern_$layouts_LAYOUTLIST_lit___41_, tmp);
	}
    protected static final void _init_prod__set_$Pattern__lit___123_$layouts_LAYOUTLIST_elements_iter_star_seps__$Pattern__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new LiteralStackNode<IConstructor>(-217, 4, prod__lit___125__char_class___range__125_125_, new int[] {125}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-216, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new SeparatedListStackNode<IConstructor>(-213, 2, regular__iter_star_seps__$Pattern__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(-207, 0, "$Pattern", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(-209, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode<IConstructor>(-210, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode<IConstructor>(-212, 3, "$layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-205, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-203, 0, prod__lit___123__char_class___range__123_123_, new int[] {123}, null, null);
      builder.addAlternative(RascalParser.prod__set_$Pattern__lit___123_$layouts_LAYOUTLIST_elements_iter_star_seps__$Pattern__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_, tmp);
	}
    protected static final void _init_prod__splice_$Pattern__lit___42_$layouts_LAYOUTLIST_argument_$Pattern_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode<IConstructor>(-175, 2, "$Pattern", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-173, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-171, 0, prod__lit___42__char_class___range__42_42_, new int[] {42}, null, null);
      builder.addAlternative(RascalParser.prod__splice_$Pattern__lit___42_$layouts_LAYOUTLIST_argument_$Pattern_, tmp);
	}
    protected static final void _init_prod__splicePlus_$Pattern__lit___43_$layouts_LAYOUTLIST_argument_$Pattern_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode<IConstructor>(-137, 2, "$Pattern", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-135, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-133, 0, prod__lit___43__char_class___range__43_43_, new int[] {43}, null, null);
      builder.addAlternative(RascalParser.prod__splicePlus_$Pattern__lit___43_$layouts_LAYOUTLIST_argument_$Pattern_, tmp);
	}
    protected static final void _init_prod__tuple_$Pattern__lit___60_$layouts_LAYOUTLIST_elements_iter_seps__$Pattern__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___62_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new LiteralStackNode<IConstructor>(-290, 4, prod__lit___62__char_class___range__62_62_, new int[] {62}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-289, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new SeparatedListStackNode<IConstructor>(-286, 2, regular__iter_seps__$Pattern__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(-280, 0, "$Pattern", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(-282, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode<IConstructor>(-283, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode<IConstructor>(-285, 3, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-278, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-276, 0, prod__lit___60__char_class___range__60_60_, new int[] {60}, null, null);
      builder.addAlternative(RascalParser.prod__tuple_$Pattern__lit___60_$layouts_LAYOUTLIST_elements_iter_seps__$Pattern__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___62_, tmp);
	}
    protected static final void _init_prod__typedVariable_$Pattern__type_$Type_$layouts_LAYOUTLIST_name_$Name_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(-143, 0, "$Type", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-146, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(-148, 2, "$Name", null, null);
      builder.addAlternative(RascalParser.prod__typedVariable_$Pattern__type_$Type_$layouts_LAYOUTLIST_name_$Name_, tmp);
	}
    protected static final void _init_prod__typedVariableBecomes_$Pattern__type_$Type_$layouts_LAYOUTLIST_name_$Name_$layouts_LAYOUTLIST_lit___58_$layouts_LAYOUTLIST_pattern_$Pattern_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[7];
      
      tmp[6] = new NonTerminalStackNode<IConstructor>(-319, 6, "$Pattern", null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(-317, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode<IConstructor>(-315, 4, prod__lit___58__char_class___range__58_58_, new int[] {58}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-314, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(-311, 2, "$Name", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-309, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-306, 0, "$Type", null, null);
      builder.addAlternative(RascalParser.prod__typedVariableBecomes_$Pattern__type_$Type_$layouts_LAYOUTLIST_name_$Name_$layouts_LAYOUTLIST_lit___58_$layouts_LAYOUTLIST_pattern_$Pattern_, tmp);
	}
    protected static final void _init_prod__variableBecomes_$Pattern__name_$Name_$layouts_LAYOUTLIST_lit___58_$layouts_LAYOUTLIST_pattern_$Pattern_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(-350, 4, "$Pattern", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-348, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(-346, 2, prod__lit___58__char_class___range__58_58_, new int[] {58}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-345, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-342, 0, "$Name", null, null);
      builder.addAlternative(RascalParser.prod__variableBecomes_$Pattern__name_$Name_$layouts_LAYOUTLIST_lit___58_$layouts_LAYOUTLIST_pattern_$Pattern_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__anti_$Pattern__lit___33_$layouts_LAYOUTLIST_pattern_$Pattern_(builder);
      
        _init_prod__asType_$Pattern__lit___91_$layouts_LAYOUTLIST_type_$Type_$layouts_LAYOUTLIST_lit___93_$layouts_LAYOUTLIST_argument_$Pattern_(builder);
      
        _init_prod__callOrTree_$Pattern__expression_$Pattern_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_arguments_iter_star_seps__$Pattern__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_(builder);
      
        _init_prod__concrete_$Pattern__concrete_$Concrete_(builder);
      
        _init_prod__descendant_$Pattern__lit___47_$layouts_LAYOUTLIST_pattern_$Pattern_(builder);
      
        _init_prod__list_$Pattern__lit___91_$layouts_LAYOUTLIST_elements_iter_star_seps__$Pattern__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___93_(builder);
      
        _init_prod__literal_$Pattern__literal_$Literal_(builder);
      
        _init_prod__map_$Pattern__lit___40_$layouts_LAYOUTLIST_mappings_iter_star_seps__$Mapping__$Pattern__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_(builder);
      
        _init_prod__multiVariable_$Pattern__qualifiedName_$QualifiedName_$layouts_LAYOUTLIST_lit___42_(builder);
      
        _init_prod__negative_$Pattern__lit___$layouts_LAYOUTLIST_argument_$Pattern_(builder);
      
        _init_prod__qualifiedName_$Pattern__qualifiedName_$QualifiedName_(builder);
      
        _init_prod__reifiedType_$Pattern__lit_type_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_symbol_$Pattern_$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_definitions_$Pattern_$layouts_LAYOUTLIST_lit___41_(builder);
      
        _init_prod__set_$Pattern__lit___123_$layouts_LAYOUTLIST_elements_iter_star_seps__$Pattern__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_(builder);
      
        _init_prod__splice_$Pattern__lit___42_$layouts_LAYOUTLIST_argument_$Pattern_(builder);
      
        _init_prod__splicePlus_$Pattern__lit___43_$layouts_LAYOUTLIST_argument_$Pattern_(builder);
      
        _init_prod__tuple_$Pattern__lit___60_$layouts_LAYOUTLIST_elements_iter_seps__$Pattern__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___62_(builder);
      
        _init_prod__typedVariable_$Pattern__type_$Type_$layouts_LAYOUTLIST_name_$Name_(builder);
      
        _init_prod__typedVariableBecomes_$Pattern__type_$Type_$layouts_LAYOUTLIST_name_$Name_$layouts_LAYOUTLIST_lit___58_$layouts_LAYOUTLIST_pattern_$Pattern_(builder);
      
        _init_prod__variableBecomes_$Pattern__name_$Name_$layouts_LAYOUTLIST_lit___58_$layouts_LAYOUTLIST_pattern_$Pattern_(builder);
      
    }
  }
	
  protected static class $Prod {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__all_$Prod__lhs_$Prod_$layouts_LAYOUTLIST_lit___124_$layouts_LAYOUTLIST_rhs_$Prod__assoc__left(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(-457, 4, "$Prod", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-455, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(-453, 2, prod__lit___124__char_class___range__124_124_, new int[] {124}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-452, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-449, 0, "$Prod", null, null);
      builder.addAlternative(RascalParser.prod__all_$Prod__lhs_$Prod_$layouts_LAYOUTLIST_lit___124_$layouts_LAYOUTLIST_rhs_$Prod__assoc__left, tmp);
	}
    protected static final void _init_prod__associativityGroup_$Prod__associativity_$Assoc_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_group_$Prod_$layouts_LAYOUTLIST_lit___41__tag__Foldable(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[7];
      
      tmp[6] = new LiteralStackNode<IConstructor>(-406, 6, prod__lit___41__char_class___range__41_41_, new int[] {41}, null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(-405, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new NonTerminalStackNode<IConstructor>(-402, 4, "$Prod", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-400, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(-398, 2, prod__lit___40__char_class___range__40_40_, new int[] {40}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-397, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-394, 0, "$Assoc", null, null);
      builder.addAlternative(RascalParser.prod__associativityGroup_$Prod__associativity_$Assoc_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_group_$Prod_$layouts_LAYOUTLIST_lit___41__tag__Foldable, tmp);
	}
    protected static final void _init_prod__first_$Prod__lhs_$Prod_$layouts_LAYOUTLIST_lit___62_$layouts_LAYOUTLIST_rhs_$Prod__assoc__left(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(-473, 4, "$Prod", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-471, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(-469, 2, prod__lit___62__char_class___range__62_62_, new int[] {62}, null, new ICompletionFilter[] {new StringFollowRestriction(new int[] {62})});
      tmp[1] = new NonTerminalStackNode<IConstructor>(-466, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-463, 0, "$Prod", null, null);
      builder.addAlternative(RascalParser.prod__first_$Prod__lhs_$Prod_$layouts_LAYOUTLIST_lit___62_$layouts_LAYOUTLIST_rhs_$Prod__assoc__left, tmp);
	}
    protected static final void _init_prod__labeled_$Prod__modifiers_iter_star_seps__$ProdModifier__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_name_$Name_$layouts_LAYOUTLIST_lit___58_$layouts_LAYOUTLIST_args_iter_star_seps__$Sym__$layouts_LAYOUTLIST_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[7];
      
      tmp[6] = new SeparatedListStackNode<IConstructor>(-443, 6, regular__iter_star_seps__$Sym__$layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(-440, 0, "$Sym", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(-442, 1, "$layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(-438, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode<IConstructor>(-436, 4, prod__lit___58__char_class___range__58_58_, new int[] {58}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-435, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(-432, 2, "$Name", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-430, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new SeparatedListStackNode<IConstructor>(-427, 0, regular__iter_star_seps__$ProdModifier__$layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(-424, 0, "$ProdModifier", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(-426, 1, "$layouts_LAYOUTLIST", null, null)}, false, null, null);
      builder.addAlternative(RascalParser.prod__labeled_$Prod__modifiers_iter_star_seps__$ProdModifier__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_name_$Name_$layouts_LAYOUTLIST_lit___58_$layouts_LAYOUTLIST_args_iter_star_seps__$Sym__$layouts_LAYOUTLIST_, tmp);
	}
    protected static final void _init_prod__others_$Prod__lit___46_46_46_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-419, 0, prod__lit___46_46_46__char_class___range__46_46_char_class___range__46_46_char_class___range__46_46_, new int[] {46,46,46}, null, null);
      builder.addAlternative(RascalParser.prod__others_$Prod__lit___46_46_46_, tmp);
	}
    protected static final void _init_prod__reference_$Prod__lit___58_$layouts_LAYOUTLIST_referenced_$Name_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode<IConstructor>(-414, 2, "$Name", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-412, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-410, 0, prod__lit___58__char_class___range__58_58_, new int[] {58}, null, null);
      builder.addAlternative(RascalParser.prod__reference_$Prod__lit___58_$layouts_LAYOUTLIST_referenced_$Name_, tmp);
	}
    protected static final void _init_prod__unlabeled_$Prod__modifiers_iter_star_seps__$ProdModifier__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_args_iter_star_seps__$Sym__$layouts_LAYOUTLIST_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new SeparatedListStackNode<IConstructor>(-388, 2, regular__iter_star_seps__$Sym__$layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(-385, 0, "$Sym", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(-387, 1, "$layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-383, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new SeparatedListStackNode<IConstructor>(-380, 0, regular__iter_star_seps__$ProdModifier__$layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(-377, 0, "$ProdModifier", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(-379, 1, "$layouts_LAYOUTLIST", null, null)}, false, null, null);
      builder.addAlternative(RascalParser.prod__unlabeled_$Prod__modifiers_iter_star_seps__$ProdModifier__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_args_iter_star_seps__$Sym__$layouts_LAYOUTLIST_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__all_$Prod__lhs_$Prod_$layouts_LAYOUTLIST_lit___124_$layouts_LAYOUTLIST_rhs_$Prod__assoc__left(builder);
      
        _init_prod__associativityGroup_$Prod__associativity_$Assoc_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_group_$Prod_$layouts_LAYOUTLIST_lit___41__tag__Foldable(builder);
      
        _init_prod__first_$Prod__lhs_$Prod_$layouts_LAYOUTLIST_lit___62_$layouts_LAYOUTLIST_rhs_$Prod__assoc__left(builder);
      
        _init_prod__labeled_$Prod__modifiers_iter_star_seps__$ProdModifier__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_name_$Name_$layouts_LAYOUTLIST_lit___58_$layouts_LAYOUTLIST_args_iter_star_seps__$Sym__$layouts_LAYOUTLIST_(builder);
      
        _init_prod__others_$Prod__lit___46_46_46_(builder);
      
        _init_prod__reference_$Prod__lit___58_$layouts_LAYOUTLIST_referenced_$Name_(builder);
      
        _init_prod__unlabeled_$Prod__modifiers_iter_star_seps__$ProdModifier__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_args_iter_star_seps__$Sym__$layouts_LAYOUTLIST_(builder);
      
    }
  }
	
  protected static class $Tag {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__default_$Tag__lit___64_$layouts_LAYOUTLIST_name_$Name_$layouts_LAYOUTLIST_contents_$TagString__tag__Folded_tag__category___67_111_109_109_101_110_116(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(-20, 4, "$TagString", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-18, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(-15, 2, "$Name", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-13, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-11, 0, prod__lit___64__char_class___range__64_64_, new int[] {64}, null, null);
      builder.addAlternative(RascalParser.prod__default_$Tag__lit___64_$layouts_LAYOUTLIST_name_$Name_$layouts_LAYOUTLIST_contents_$TagString__tag__Folded_tag__category___67_111_109_109_101_110_116, tmp);
	}
    protected static final void _init_prod__empty_$Tag__lit___64_$layouts_LAYOUTLIST_name_$Name__tag__Folded_tag__category___67_111_109_109_101_110_116(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode<IConstructor>(-46, 2, "$Name", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-44, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-42, 0, prod__lit___64__char_class___range__64_64_, new int[] {64}, null, null);
      builder.addAlternative(RascalParser.prod__empty_$Tag__lit___64_$layouts_LAYOUTLIST_name_$Name__tag__Folded_tag__category___67_111_109_109_101_110_116, tmp);
	}
    protected static final void _init_prod__expression_$Tag__lit___64_$layouts_LAYOUTLIST_name_$Name_$layouts_LAYOUTLIST_lit___61_$layouts_LAYOUTLIST_expression_$Expression__tag__Folded_tag__category___67_111_109_109_101_110_116(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[7];
      
      tmp[6] = new NonTerminalStackNode<IConstructor>(-37, 6, "$Expression", null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(-35, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode<IConstructor>(-33, 4, prod__lit___61__char_class___range__61_61_, new int[] {61}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-32, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(-29, 2, "$Name", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-27, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-25, 0, prod__lit___64__char_class___range__64_64_, new int[] {64}, null, null);
      builder.addAlternative(RascalParser.prod__expression_$Tag__lit___64_$layouts_LAYOUTLIST_name_$Name_$layouts_LAYOUTLIST_lit___61_$layouts_LAYOUTLIST_expression_$Expression__tag__Folded_tag__category___67_111_109_109_101_110_116, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__default_$Tag__lit___64_$layouts_LAYOUTLIST_name_$Name_$layouts_LAYOUTLIST_contents_$TagString__tag__Folded_tag__category___67_111_109_109_101_110_116(builder);
      
        _init_prod__empty_$Tag__lit___64_$layouts_LAYOUTLIST_name_$Name__tag__Folded_tag__category___67_111_109_109_101_110_116(builder);
      
        _init_prod__expression_$Tag__lit___64_$layouts_LAYOUTLIST_name_$Name_$layouts_LAYOUTLIST_lit___61_$layouts_LAYOUTLIST_expression_$Expression__tag__Folded_tag__category___67_111_109_109_101_110_116(builder);
      
    }
  }
	
  protected static class $OctalIntegerLiteral {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__$OctalIntegerLiteral__char_class___range__48_48_iter__char_class___range__48_55_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[2];
      
      tmp[1] = new ListStackNode<IConstructor>(-548, 1, regular__iter__char_class___range__48_55, new CharStackNode<IConstructor>(-545, 0, new int[][]{{48,55}}, null, null), true, null, new ICompletionFilter[] {new CharFollowRestriction(new int[][]{{48,57},{65,90},{95,95},{97,122}})});
      tmp[0] = new CharStackNode<IConstructor>(-544, 0, new int[][]{{48,48}}, null, null);
      builder.addAlternative(RascalParser.prod__$OctalIntegerLiteral__char_class___range__48_48_iter__char_class___range__48_55_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__$OctalIntegerLiteral__char_class___range__48_48_iter__char_class___range__48_55_(builder);
      
    }
  }
	
  protected static class $ProdModifier {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__associativity_$ProdModifier__associativity_$Assoc_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(-616, 0, "$Assoc", null, null);
      builder.addAlternative(RascalParser.prod__associativity_$ProdModifier__associativity_$Assoc_, tmp);
	}
    protected static final void _init_prod__bracket_$ProdModifier__lit_bracket_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-611, 0, prod__lit_bracket__char_class___range__98_98_char_class___range__114_114_char_class___range__97_97_char_class___range__99_99_char_class___range__107_107_char_class___range__101_101_char_class___range__116_116_, new int[] {98,114,97,99,107,101,116}, null, null);
      builder.addAlternative(RascalParser.prod__bracket_$ProdModifier__lit_bracket_, tmp);
	}
    protected static final void _init_prod__tag_$ProdModifier__tag_$Tag_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(-622, 0, "$Tag", null, null);
      builder.addAlternative(RascalParser.prod__tag_$ProdModifier__tag_$Tag_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__associativity_$ProdModifier__associativity_$Assoc_(builder);
      
        _init_prod__bracket_$ProdModifier__lit_bracket_(builder);
      
        _init_prod__tag_$ProdModifier__tag_$Tag_(builder);
      
    }
  }
	
  protected static class $RationalLiteral {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__$RationalLiteral__char_class___range__48_57_iter_star__char_class___range__48_57_char_class___range__114_114_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new CharStackNode<IConstructor>(-573, 2, new int[][]{{114,114}}, null, null);
      tmp[1] = new ListStackNode<IConstructor>(-572, 1, regular__iter_star__char_class___range__48_57, new CharStackNode<IConstructor>(-571, 0, new int[][]{{48,57}}, null, null), false, null, null);
      tmp[0] = new CharStackNode<IConstructor>(-570, 0, new int[][]{{48,57}}, null, null);
      builder.addAlternative(RascalParser.prod__$RationalLiteral__char_class___range__48_57_iter_star__char_class___range__48_57_char_class___range__114_114_, tmp);
	}
    protected static final void _init_prod__$RationalLiteral__char_class___range__49_57_iter_star__char_class___range__48_57_char_class___range__114_114_char_class___range__48_57_iter_star__char_class___range__48_57_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new ListStackNode<IConstructor>(-567, 4, regular__iter_star__char_class___range__48_57, new CharStackNode<IConstructor>(-564, 0, new int[][]{{48,57}}, null, null), false, null, new ICompletionFilter[] {new CharFollowRestriction(new int[][]{{48,57},{65,90},{95,95},{97,122}})});
      tmp[3] = new CharStackNode<IConstructor>(-563, 3, new int[][]{{48,57}}, null, null);
      tmp[2] = new CharStackNode<IConstructor>(-562, 2, new int[][]{{114,114}}, null, null);
      tmp[1] = new ListStackNode<IConstructor>(-561, 1, regular__iter_star__char_class___range__48_57, new CharStackNode<IConstructor>(-560, 0, new int[][]{{48,57}}, null, null), false, null, null);
      tmp[0] = new CharStackNode<IConstructor>(-559, 0, new int[][]{{49,57}}, null, null);
      builder.addAlternative(RascalParser.prod__$RationalLiteral__char_class___range__49_57_iter_star__char_class___range__48_57_char_class___range__114_114_char_class___range__48_57_iter_star__char_class___range__48_57_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__$RationalLiteral__char_class___range__48_57_iter_star__char_class___range__48_57_char_class___range__114_114_(builder);
      
        _init_prod__$RationalLiteral__char_class___range__49_57_iter_star__char_class___range__48_57_char_class___range__114_114_char_class___range__48_57_iter_star__char_class___range__48_57_(builder);
      
    }
  }
	
  protected static class $Toplevel {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__givenVisibility_$Toplevel__declaration_$Declaration_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(-602, 0, "$Declaration", null, null);
      builder.addAlternative(RascalParser.prod__givenVisibility_$Toplevel__declaration_$Declaration_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__givenVisibility_$Toplevel__declaration_$Declaration_(builder);
      
    }
  }
	
  protected static class $TypeVar {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__bounded_$TypeVar__lit___38_$layouts_LAYOUTLIST_name_$Name_$layouts_LAYOUTLIST_lit___60_58_$layouts_LAYOUTLIST_bound_$Type_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[7];
      
      tmp[6] = new NonTerminalStackNode<IConstructor>(-643, 6, "$Type", null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(-641, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode<IConstructor>(-639, 4, prod__lit___60_58__char_class___range__60_60_char_class___range__58_58_, new int[] {60,58}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-638, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(-635, 2, "$Name", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-633, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-631, 0, prod__lit___38__char_class___range__38_38_, new int[] {38}, null, null);
      builder.addAlternative(RascalParser.prod__bounded_$TypeVar__lit___38_$layouts_LAYOUTLIST_name_$Name_$layouts_LAYOUTLIST_lit___60_58_$layouts_LAYOUTLIST_bound_$Type_, tmp);
	}
    protected static final void _init_prod__free_$TypeVar__lit___38_$layouts_LAYOUTLIST_name_$Name_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode<IConstructor>(-652, 2, "$Name", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-650, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-648, 0, prod__lit___38__char_class___range__38_38_, new int[] {38}, null, null);
      builder.addAlternative(RascalParser.prod__free_$TypeVar__lit___38_$layouts_LAYOUTLIST_name_$Name_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__bounded_$TypeVar__lit___38_$layouts_LAYOUTLIST_name_$Name_$layouts_LAYOUTLIST_lit___60_58_$layouts_LAYOUTLIST_bound_$Type_(builder);
      
        _init_prod__free_$TypeVar__lit___38_$layouts_LAYOUTLIST_name_$Name_(builder);
      
    }
  }
	
  protected static class $UnicodeEscape {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__ascii_$UnicodeEscape__lit___92_char_class___range__97_97_char_class___range__48_55_char_class___range__48_57_range__65_70_range__97_102_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[4];
      
      tmp[3] = new CharStackNode<IConstructor>(-485, 3, new int[][]{{48,57},{65,70},{97,102}}, null, null);
      tmp[2] = new CharStackNode<IConstructor>(-484, 2, new int[][]{{48,55}}, null, null);
      tmp[1] = new CharStackNode<IConstructor>(-483, 1, new int[][]{{97,97}}, null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-482, 0, prod__lit___92__char_class___range__92_92_, new int[] {92}, null, null);
      builder.addAlternative(RascalParser.prod__ascii_$UnicodeEscape__lit___92_char_class___range__97_97_char_class___range__48_55_char_class___range__48_57_range__65_70_range__97_102_, tmp);
	}
    protected static final void _init_prod__utf16_$UnicodeEscape__lit___92_char_class___range__117_117_char_class___range__48_57_range__65_70_range__97_102_char_class___range__48_57_range__65_70_range__97_102_char_class___range__48_57_range__65_70_range__97_102_char_class___range__48_57_range__65_70_range__97_102_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[6];
      
      tmp[5] = new CharStackNode<IConstructor>(-494, 5, new int[][]{{48,57},{65,70},{97,102}}, null, null);
      tmp[4] = new CharStackNode<IConstructor>(-493, 4, new int[][]{{48,57},{65,70},{97,102}}, null, null);
      tmp[3] = new CharStackNode<IConstructor>(-492, 3, new int[][]{{48,57},{65,70},{97,102}}, null, null);
      tmp[2] = new CharStackNode<IConstructor>(-491, 2, new int[][]{{48,57},{65,70},{97,102}}, null, null);
      tmp[1] = new CharStackNode<IConstructor>(-490, 1, new int[][]{{117,117}}, null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-489, 0, prod__lit___92__char_class___range__92_92_, new int[] {92}, null, null);
      builder.addAlternative(RascalParser.prod__utf16_$UnicodeEscape__lit___92_char_class___range__117_117_char_class___range__48_57_range__65_70_range__97_102_char_class___range__48_57_range__65_70_range__97_102_char_class___range__48_57_range__65_70_range__97_102_char_class___range__48_57_range__65_70_range__97_102_, tmp);
	}
    protected static final void _init_prod__utf32_$UnicodeEscape__lit___92_char_class___range__85_85_char_class___range__48_57_range__65_70_range__97_102_char_class___range__48_57_range__65_70_range__97_102_char_class___range__48_57_range__65_70_range__97_102_char_class___range__48_57_range__65_70_range__97_102_char_class___range__48_57_range__65_70_range__97_102_char_class___range__48_57_range__65_70_range__97_102_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[8];
      
      tmp[7] = new CharStackNode<IConstructor>(-505, 7, new int[][]{{48,57},{65,70},{97,102}}, null, null);
      tmp[6] = new CharStackNode<IConstructor>(-504, 6, new int[][]{{48,57},{65,70},{97,102}}, null, null);
      tmp[5] = new CharStackNode<IConstructor>(-503, 5, new int[][]{{48,57},{65,70},{97,102}}, null, null);
      tmp[4] = new CharStackNode<IConstructor>(-502, 4, new int[][]{{48,57},{65,70},{97,102}}, null, null);
      tmp[3] = new CharStackNode<IConstructor>(-501, 3, new int[][]{{48,57},{65,70},{97,102}}, null, null);
      tmp[2] = new CharStackNode<IConstructor>(-500, 2, new int[][]{{48,57},{65,70},{97,102}}, null, null);
      tmp[1] = new CharStackNode<IConstructor>(-499, 1, new int[][]{{85,85}}, null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-498, 0, prod__lit___92__char_class___range__92_92_, new int[] {92}, null, null);
      builder.addAlternative(RascalParser.prod__utf32_$UnicodeEscape__lit___92_char_class___range__85_85_char_class___range__48_57_range__65_70_range__97_102_char_class___range__48_57_range__65_70_range__97_102_char_class___range__48_57_range__65_70_range__97_102_char_class___range__48_57_range__65_70_range__97_102_char_class___range__48_57_range__65_70_range__97_102_char_class___range__48_57_range__65_70_range__97_102_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__ascii_$UnicodeEscape__lit___92_char_class___range__97_97_char_class___range__48_55_char_class___range__48_57_range__65_70_range__97_102_(builder);
      
        _init_prod__utf16_$UnicodeEscape__lit___92_char_class___range__117_117_char_class___range__48_57_range__65_70_range__97_102_char_class___range__48_57_range__65_70_range__97_102_char_class___range__48_57_range__65_70_range__97_102_char_class___range__48_57_range__65_70_range__97_102_(builder);
      
        _init_prod__utf32_$UnicodeEscape__lit___92_char_class___range__85_85_char_class___range__48_57_range__65_70_range__97_102_char_class___range__48_57_range__65_70_range__97_102_char_class___range__48_57_range__65_70_range__97_102_char_class___range__48_57_range__65_70_range__97_102_char_class___range__48_57_range__65_70_range__97_102_char_class___range__48_57_range__65_70_range__97_102_(builder);
      
    }
  }
	
  protected static class $Bound {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__default_$Bound__lit___59_$layouts_LAYOUTLIST_expression_$Expression_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode<IConstructor>(-1174, 2, "$Expression", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-1172, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-1170, 0, prod__lit___59__char_class___range__59_59_, new int[] {59}, null, null);
      builder.addAlternative(RascalParser.prod__default_$Bound__lit___59_$layouts_LAYOUTLIST_expression_$Expression_, tmp);
	}
    protected static final void _init_prod__empty_$Bound__(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new EpsilonStackNode<IConstructor>(-1166, 0);
      builder.addAlternative(RascalParser.prod__empty_$Bound__, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__default_$Bound__lit___59_$layouts_LAYOUTLIST_expression_$Expression_(builder);
      
        _init_prod__empty_$Bound__(builder);
      
    }
  }
	
  protected static class $Case {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__default_$Case__lit_default_$layouts_LAYOUTLIST_lit___58_$layouts_LAYOUTLIST_statement_$Statement__tag__Foldable(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(-1256, 4, "$Statement", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-1254, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(-1252, 2, prod__lit___58__char_class___range__58_58_, new int[] {58}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-1251, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-1249, 0, prod__lit_default__char_class___range__100_100_char_class___range__101_101_char_class___range__102_102_char_class___range__97_97_char_class___range__117_117_char_class___range__108_108_char_class___range__116_116_, new int[] {100,101,102,97,117,108,116}, null, null);
      builder.addAlternative(RascalParser.prod__default_$Case__lit_default_$layouts_LAYOUTLIST_lit___58_$layouts_LAYOUTLIST_statement_$Statement__tag__Foldable, tmp);
	}
    protected static final void _init_prod__patternWithAction_$Case__lit_case_$layouts_LAYOUTLIST_patternWithAction_$PatternWithAction__tag__Foldable(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode<IConstructor>(-1244, 2, "$PatternWithAction", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-1242, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-1240, 0, prod__lit_case__char_class___range__99_99_char_class___range__97_97_char_class___range__115_115_char_class___range__101_101_, new int[] {99,97,115,101}, null, null);
      builder.addAlternative(RascalParser.prod__patternWithAction_$Case__lit_case_$layouts_LAYOUTLIST_patternWithAction_$PatternWithAction__tag__Foldable, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__default_$Case__lit_default_$layouts_LAYOUTLIST_lit___58_$layouts_LAYOUTLIST_statement_$Statement__tag__Foldable(builder);
      
        _init_prod__patternWithAction_$Case__lit_case_$layouts_LAYOUTLIST_patternWithAction_$PatternWithAction__tag__Foldable(builder);
      
    }
  }
	
  protected static class $Class {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__bracket_$Class__lit___40_$layouts_LAYOUTLIST_charclass_$Class_$layouts_LAYOUTLIST_lit___41__bracket(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new LiteralStackNode<IConstructor>(-1159, 4, prod__lit___41__char_class___range__41_41_, new int[] {41}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-1158, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(-1155, 2, "$Class", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-1153, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-1151, 0, prod__lit___40__char_class___range__40_40_, new int[] {40}, null, null);
      builder.addAlternative(RascalParser.prod__bracket_$Class__lit___40_$layouts_LAYOUTLIST_charclass_$Class_$layouts_LAYOUTLIST_lit___41__bracket, tmp);
	}
    protected static final void _init_prod__complement_$Class__lit___33_$layouts_LAYOUTLIST_charClass_$Class_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode<IConstructor>(-1102, 2, "$Class", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-1100, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-1098, 0, prod__lit___33__char_class___range__33_33_, new int[] {33}, null, null);
      builder.addAlternative(RascalParser.prod__complement_$Class__lit___33_$layouts_LAYOUTLIST_charClass_$Class_, tmp);
	}
    protected static final void _init_prod__difference_$Class__lhs_$Class_$layouts_LAYOUTLIST_lit___$layouts_LAYOUTLIST_rhs_$Class__assoc__left(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(-1116, 4, "$Class", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-1114, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(-1112, 2, prod__lit____char_class___range__45_45_, new int[] {45}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-1111, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-1108, 0, "$Class", null, null);
      builder.addAlternative(RascalParser.prod__difference_$Class__lhs_$Class_$layouts_LAYOUTLIST_lit___$layouts_LAYOUTLIST_rhs_$Class__assoc__left, tmp);
	}
    protected static final void _init_prod__intersection_$Class__lhs_$Class_$layouts_LAYOUTLIST_lit___38_38_$layouts_LAYOUTLIST_rhs_$Class__assoc__left(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(-1130, 4, "$Class", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-1128, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(-1126, 2, prod__lit___38_38__char_class___range__38_38_char_class___range__38_38_, new int[] {38,38}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-1125, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-1122, 0, "$Class", null, null);
      builder.addAlternative(RascalParser.prod__intersection_$Class__lhs_$Class_$layouts_LAYOUTLIST_lit___38_38_$layouts_LAYOUTLIST_rhs_$Class__assoc__left, tmp);
	}
    protected static final void _init_prod__simpleCharclass_$Class__lit___91_$layouts_LAYOUTLIST_ranges_iter_star_seps__$Range__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___93_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new LiteralStackNode<IConstructor>(-1094, 4, prod__lit___93__char_class___range__93_93_, new int[] {93}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-1093, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new SeparatedListStackNode<IConstructor>(-1090, 2, regular__iter_star_seps__$Range__$layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(-1087, 0, "$Range", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(-1089, 1, "$layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-1085, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-1083, 0, prod__lit___91__char_class___range__91_91_, new int[] {91}, null, null);
      builder.addAlternative(RascalParser.prod__simpleCharclass_$Class__lit___91_$layouts_LAYOUTLIST_ranges_iter_star_seps__$Range__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___93_, tmp);
	}
    protected static final void _init_prod__union_$Class__lhs_$Class_$layouts_LAYOUTLIST_lit___124_124_$layouts_LAYOUTLIST_rhs_$Class__assoc__left(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(-1146, 4, "$Class", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-1144, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(-1142, 2, prod__lit___124_124__char_class___range__124_124_char_class___range__124_124_, new int[] {124,124}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-1141, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-1138, 0, "$Class", null, null);
      builder.addAlternative(RascalParser.prod__union_$Class__lhs_$Class_$layouts_LAYOUTLIST_lit___124_124_$layouts_LAYOUTLIST_rhs_$Class__assoc__left, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__bracket_$Class__lit___40_$layouts_LAYOUTLIST_charclass_$Class_$layouts_LAYOUTLIST_lit___41__bracket(builder);
      
        _init_prod__complement_$Class__lit___33_$layouts_LAYOUTLIST_charClass_$Class_(builder);
      
        _init_prod__difference_$Class__lhs_$Class_$layouts_LAYOUTLIST_lit___$layouts_LAYOUTLIST_rhs_$Class__assoc__left(builder);
      
        _init_prod__intersection_$Class__lhs_$Class_$layouts_LAYOUTLIST_lit___38_38_$layouts_LAYOUTLIST_rhs_$Class__assoc__left(builder);
      
        _init_prod__simpleCharclass_$Class__lit___91_$layouts_LAYOUTLIST_ranges_iter_star_seps__$Range__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___93_(builder);
      
        _init_prod__union_$Class__lhs_$Class_$layouts_LAYOUTLIST_lit___124_124_$layouts_LAYOUTLIST_rhs_$Class__assoc__left(builder);
      
    }
  }
	
  protected static class $Comprehension {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__list_$Comprehension__lit___91_$layouts_LAYOUTLIST_results_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___124_$layouts_LAYOUTLIST_generators_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___93__tag__breakable___123_114_101_115_117_108_116_115_44_103_101_110_101_114_97_116_111_114_115_125(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[9];
      
      tmp[8] = new LiteralStackNode<IConstructor>(-795, 8, prod__lit___93__char_class___range__93_93_, new int[] {93}, null, null);
      tmp[7] = new NonTerminalStackNode<IConstructor>(-794, 7, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new SeparatedListStackNode<IConstructor>(-791, 6, regular__iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(-785, 0, "$Expression", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(-787, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode<IConstructor>(-788, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode<IConstructor>(-790, 3, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(-783, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode<IConstructor>(-781, 4, prod__lit___124__char_class___range__124_124_, new int[] {124}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-780, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new SeparatedListStackNode<IConstructor>(-777, 2, regular__iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(-771, 0, "$Expression", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(-773, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode<IConstructor>(-774, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode<IConstructor>(-776, 3, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-769, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-767, 0, prod__lit___91__char_class___range__91_91_, new int[] {91}, null, null);
      builder.addAlternative(RascalParser.prod__list_$Comprehension__lit___91_$layouts_LAYOUTLIST_results_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___124_$layouts_LAYOUTLIST_generators_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___93__tag__breakable___123_114_101_115_117_108_116_115_44_103_101_110_101_114_97_116_111_114_115_125, tmp);
	}
    protected static final void _init_prod__map_$Comprehension__lit___40_$layouts_LAYOUTLIST_from_$Expression_$layouts_LAYOUTLIST_lit___58_$layouts_LAYOUTLIST_to_$Expression_$layouts_LAYOUTLIST_lit___124_$layouts_LAYOUTLIST_generators_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41__tag__breakable___123_102_114_111_109_44_116_111_44_103_101_110_101_114_97_116_111_114_115_125(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[13];
      
      tmp[12] = new LiteralStackNode<IConstructor>(-763, 12, prod__lit___41__char_class___range__41_41_, new int[] {41}, null, null);
      tmp[11] = new NonTerminalStackNode<IConstructor>(-762, 11, "$layouts_LAYOUTLIST", null, null);
      tmp[10] = new SeparatedListStackNode<IConstructor>(-759, 10, regular__iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(-753, 0, "$Expression", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(-755, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode<IConstructor>(-756, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode<IConstructor>(-758, 3, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[9] = new NonTerminalStackNode<IConstructor>(-751, 9, "$layouts_LAYOUTLIST", null, null);
      tmp[8] = new LiteralStackNode<IConstructor>(-749, 8, prod__lit___124__char_class___range__124_124_, new int[] {124}, null, null);
      tmp[7] = new NonTerminalStackNode<IConstructor>(-748, 7, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new NonTerminalStackNode<IConstructor>(-745, 6, "$Expression", null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(-743, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode<IConstructor>(-741, 4, prod__lit___58__char_class___range__58_58_, new int[] {58}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-740, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(-737, 2, "$Expression", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-735, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-733, 0, prod__lit___40__char_class___range__40_40_, new int[] {40}, null, null);
      builder.addAlternative(RascalParser.prod__map_$Comprehension__lit___40_$layouts_LAYOUTLIST_from_$Expression_$layouts_LAYOUTLIST_lit___58_$layouts_LAYOUTLIST_to_$Expression_$layouts_LAYOUTLIST_lit___124_$layouts_LAYOUTLIST_generators_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41__tag__breakable___123_102_114_111_109_44_116_111_44_103_101_110_101_114_97_116_111_114_115_125, tmp);
	}
    protected static final void _init_prod__set_$Comprehension__lit___123_$layouts_LAYOUTLIST_results_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___124_$layouts_LAYOUTLIST_generators_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125__tag__breakable___123_114_101_115_117_108_116_115_44_103_101_110_101_114_97_116_111_114_115_125(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[9];
      
      tmp[8] = new LiteralStackNode<IConstructor>(-729, 8, prod__lit___125__char_class___range__125_125_, new int[] {125}, null, null);
      tmp[7] = new NonTerminalStackNode<IConstructor>(-728, 7, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new SeparatedListStackNode<IConstructor>(-725, 6, regular__iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(-719, 0, "$Expression", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(-721, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode<IConstructor>(-722, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode<IConstructor>(-724, 3, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(-717, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode<IConstructor>(-715, 4, prod__lit___124__char_class___range__124_124_, new int[] {124}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-714, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new SeparatedListStackNode<IConstructor>(-711, 2, regular__iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(-705, 0, "$Expression", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(-707, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode<IConstructor>(-708, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode<IConstructor>(-710, 3, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-703, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-701, 0, prod__lit___123__char_class___range__123_123_, new int[] {123}, null, null);
      builder.addAlternative(RascalParser.prod__set_$Comprehension__lit___123_$layouts_LAYOUTLIST_results_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___124_$layouts_LAYOUTLIST_generators_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125__tag__breakable___123_114_101_115_117_108_116_115_44_103_101_110_101_114_97_116_111_114_115_125, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__list_$Comprehension__lit___91_$layouts_LAYOUTLIST_results_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___124_$layouts_LAYOUTLIST_generators_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___93__tag__breakable___123_114_101_115_117_108_116_115_44_103_101_110_101_114_97_116_111_114_115_125(builder);
      
        _init_prod__map_$Comprehension__lit___40_$layouts_LAYOUTLIST_from_$Expression_$layouts_LAYOUTLIST_lit___58_$layouts_LAYOUTLIST_to_$Expression_$layouts_LAYOUTLIST_lit___124_$layouts_LAYOUTLIST_generators_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41__tag__breakable___123_102_114_111_109_44_116_111_44_103_101_110_101_114_97_116_111_114_115_125(builder);
      
        _init_prod__set_$Comprehension__lit___123_$layouts_LAYOUTLIST_results_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___124_$layouts_LAYOUTLIST_generators_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125__tag__breakable___123_114_101_115_117_108_116_115_44_103_101_110_101_114_97_116_111_114_115_125(builder);
      
    }
  }
	
  protected static class $Declaration {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__alias_$Declaration__tags_$Tags_$layouts_LAYOUTLIST_visibility_$Visibility_$layouts_LAYOUTLIST_lit_alias_$layouts_LAYOUTLIST_user_$UserType_$layouts_LAYOUTLIST_lit___61_$layouts_LAYOUTLIST_base_$Type_$layouts_LAYOUTLIST_lit___59_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[13];
      
      tmp[12] = new LiteralStackNode<IConstructor>(-907, 12, prod__lit___59__char_class___range__59_59_, new int[] {59}, null, null);
      tmp[11] = new NonTerminalStackNode<IConstructor>(-906, 11, "$layouts_LAYOUTLIST", null, null);
      tmp[10] = new NonTerminalStackNode<IConstructor>(-903, 10, "$Type", null, null);
      tmp[9] = new NonTerminalStackNode<IConstructor>(-901, 9, "$layouts_LAYOUTLIST", null, null);
      tmp[8] = new LiteralStackNode<IConstructor>(-899, 8, prod__lit___61__char_class___range__61_61_, new int[] {61}, null, null);
      tmp[7] = new NonTerminalStackNode<IConstructor>(-898, 7, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new NonTerminalStackNode<IConstructor>(-895, 6, "$UserType", null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(-893, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode<IConstructor>(-891, 4, prod__lit_alias__char_class___range__97_97_char_class___range__108_108_char_class___range__105_105_char_class___range__97_97_char_class___range__115_115_, new int[] {97,108,105,97,115}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-890, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(-887, 2, "$Visibility", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-885, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-882, 0, "$Tags", null, null);
      builder.addAlternative(RascalParser.prod__alias_$Declaration__tags_$Tags_$layouts_LAYOUTLIST_visibility_$Visibility_$layouts_LAYOUTLIST_lit_alias_$layouts_LAYOUTLIST_user_$UserType_$layouts_LAYOUTLIST_lit___61_$layouts_LAYOUTLIST_base_$Type_$layouts_LAYOUTLIST_lit___59_, tmp);
	}
    protected static final void _init_prod__annotation_$Declaration__tags_$Tags_$layouts_LAYOUTLIST_visibility_$Visibility_$layouts_LAYOUTLIST_lit_anno_$layouts_LAYOUTLIST_annoType_$Type_$layouts_LAYOUTLIST_onType_$Type_$layouts_LAYOUTLIST_lit___64_$layouts_LAYOUTLIST_name_$Name_$layouts_LAYOUTLIST_lit___59_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[15];
      
      tmp[14] = new LiteralStackNode<IConstructor>(-942, 14, prod__lit___59__char_class___range__59_59_, new int[] {59}, null, null);
      tmp[13] = new NonTerminalStackNode<IConstructor>(-941, 13, "$layouts_LAYOUTLIST", null, null);
      tmp[12] = new NonTerminalStackNode<IConstructor>(-938, 12, "$Name", null, null);
      tmp[11] = new NonTerminalStackNode<IConstructor>(-936, 11, "$layouts_LAYOUTLIST", null, null);
      tmp[10] = new LiteralStackNode<IConstructor>(-934, 10, prod__lit___64__char_class___range__64_64_, new int[] {64}, null, null);
      tmp[9] = new NonTerminalStackNode<IConstructor>(-933, 9, "$layouts_LAYOUTLIST", null, null);
      tmp[8] = new NonTerminalStackNode<IConstructor>(-930, 8, "$Type", null, null);
      tmp[7] = new NonTerminalStackNode<IConstructor>(-928, 7, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new NonTerminalStackNode<IConstructor>(-925, 6, "$Type", null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(-923, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode<IConstructor>(-921, 4, prod__lit_anno__char_class___range__97_97_char_class___range__110_110_char_class___range__110_110_char_class___range__111_111_, new int[] {97,110,110,111}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-920, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(-917, 2, "$Visibility", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-915, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-912, 0, "$Tags", null, null);
      builder.addAlternative(RascalParser.prod__annotation_$Declaration__tags_$Tags_$layouts_LAYOUTLIST_visibility_$Visibility_$layouts_LAYOUTLIST_lit_anno_$layouts_LAYOUTLIST_annoType_$Type_$layouts_LAYOUTLIST_onType_$Type_$layouts_LAYOUTLIST_lit___64_$layouts_LAYOUTLIST_name_$Name_$layouts_LAYOUTLIST_lit___59_, tmp);
	}
    protected static final void _init_prod__data_$Declaration__tags_$Tags_$layouts_LAYOUTLIST_visibility_$Visibility_$layouts_LAYOUTLIST_lit_data_$layouts_LAYOUTLIST_user_$UserType_$layouts_LAYOUTLIST_lit___61_$layouts_LAYOUTLIST_variants_iter_seps__$Variant__$layouts_LAYOUTLIST_lit___124_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___59__tag__Foldable(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[13];
      
      tmp[12] = new LiteralStackNode<IConstructor>(-1071, 12, prod__lit___59__char_class___range__59_59_, new int[] {59}, null, null);
      tmp[11] = new NonTerminalStackNode<IConstructor>(-1070, 11, "$layouts_LAYOUTLIST", null, null);
      tmp[10] = new SeparatedListStackNode<IConstructor>(-1067, 10, regular__iter_seps__$Variant__$layouts_LAYOUTLIST_lit___124_$layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(-1061, 0, "$Variant", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(-1063, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode<IConstructor>(-1064, 2, prod__lit___124__char_class___range__124_124_, new int[] {124}, null, null), new NonTerminalStackNode<IConstructor>(-1066, 3, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[9] = new NonTerminalStackNode<IConstructor>(-1059, 9, "$layouts_LAYOUTLIST", null, null);
      tmp[8] = new LiteralStackNode<IConstructor>(-1057, 8, prod__lit___61__char_class___range__61_61_, new int[] {61}, null, null);
      tmp[7] = new NonTerminalStackNode<IConstructor>(-1056, 7, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new NonTerminalStackNode<IConstructor>(-1053, 6, "$UserType", null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(-1051, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode<IConstructor>(-1049, 4, prod__lit_data__char_class___range__100_100_char_class___range__97_97_char_class___range__116_116_char_class___range__97_97_, new int[] {100,97,116,97}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-1048, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(-1045, 2, "$Visibility", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-1043, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-1040, 0, "$Tags", null, null);
      builder.addAlternative(RascalParser.prod__data_$Declaration__tags_$Tags_$layouts_LAYOUTLIST_visibility_$Visibility_$layouts_LAYOUTLIST_lit_data_$layouts_LAYOUTLIST_user_$UserType_$layouts_LAYOUTLIST_lit___61_$layouts_LAYOUTLIST_variants_iter_seps__$Variant__$layouts_LAYOUTLIST_lit___124_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___59__tag__Foldable, tmp);
	}
    protected static final void _init_prod__dataAbstract_$Declaration__tags_$Tags_$layouts_LAYOUTLIST_visibility_$Visibility_$layouts_LAYOUTLIST_lit_data_$layouts_LAYOUTLIST_user_$UserType_$layouts_LAYOUTLIST_lit___59_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[9];
      
      tmp[8] = new LiteralStackNode<IConstructor>(-1035, 8, prod__lit___59__char_class___range__59_59_, new int[] {59}, null, null);
      tmp[7] = new NonTerminalStackNode<IConstructor>(-1034, 7, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new NonTerminalStackNode<IConstructor>(-1031, 6, "$UserType", null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(-1029, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode<IConstructor>(-1027, 4, prod__lit_data__char_class___range__100_100_char_class___range__97_97_char_class___range__116_116_char_class___range__97_97_, new int[] {100,97,116,97}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-1026, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(-1023, 2, "$Visibility", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-1021, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-1018, 0, "$Tags", null, null);
      builder.addAlternative(RascalParser.prod__dataAbstract_$Declaration__tags_$Tags_$layouts_LAYOUTLIST_visibility_$Visibility_$layouts_LAYOUTLIST_lit_data_$layouts_LAYOUTLIST_user_$UserType_$layouts_LAYOUTLIST_lit___59_, tmp);
	}
    protected static final void _init_prod__function_$Declaration__functionDeclaration_$FunctionDeclaration_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(-876, 0, "$FunctionDeclaration", null, null);
      builder.addAlternative(RascalParser.prod__function_$Declaration__functionDeclaration_$FunctionDeclaration_, tmp);
	}
    protected static final void _init_prod__tag_$Declaration__tags_$Tags_$layouts_LAYOUTLIST_visibility_$Visibility_$layouts_LAYOUTLIST_lit_tag_$layouts_LAYOUTLIST_kind_$Kind_$layouts_LAYOUTLIST_name_$Name_$layouts_LAYOUTLIST_lit_on_$layouts_LAYOUTLIST_types_iter_seps__$Type__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___59_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[15];
      
      tmp[14] = new LiteralStackNode<IConstructor>(-983, 14, prod__lit___59__char_class___range__59_59_, new int[] {59}, null, null);
      tmp[13] = new NonTerminalStackNode<IConstructor>(-982, 13, "$layouts_LAYOUTLIST", null, null);
      tmp[12] = new SeparatedListStackNode<IConstructor>(-979, 12, regular__iter_seps__$Type__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(-973, 0, "$Type", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(-975, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode<IConstructor>(-976, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode<IConstructor>(-978, 3, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[11] = new NonTerminalStackNode<IConstructor>(-971, 11, "$layouts_LAYOUTLIST", null, null);
      tmp[10] = new LiteralStackNode<IConstructor>(-969, 10, prod__lit_on__char_class___range__111_111_char_class___range__110_110_, new int[] {111,110}, null, null);
      tmp[9] = new NonTerminalStackNode<IConstructor>(-968, 9, "$layouts_LAYOUTLIST", null, null);
      tmp[8] = new NonTerminalStackNode<IConstructor>(-965, 8, "$Name", null, null);
      tmp[7] = new NonTerminalStackNode<IConstructor>(-963, 7, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new NonTerminalStackNode<IConstructor>(-960, 6, "$Kind", null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(-958, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode<IConstructor>(-956, 4, prod__lit_tag__char_class___range__116_116_char_class___range__97_97_char_class___range__103_103_, new int[] {116,97,103}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-955, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(-952, 2, "$Visibility", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-950, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-947, 0, "$Tags", null, null);
      builder.addAlternative(RascalParser.prod__tag_$Declaration__tags_$Tags_$layouts_LAYOUTLIST_visibility_$Visibility_$layouts_LAYOUTLIST_lit_tag_$layouts_LAYOUTLIST_kind_$Kind_$layouts_LAYOUTLIST_name_$Name_$layouts_LAYOUTLIST_lit_on_$layouts_LAYOUTLIST_types_iter_seps__$Type__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___59_, tmp);
	}
    protected static final void _init_prod__variable_$Declaration__tags_$Tags_$layouts_LAYOUTLIST_visibility_$Visibility_$layouts_LAYOUTLIST_type_$Type_$layouts_LAYOUTLIST_variables_iter_seps__$Variable__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___59_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[9];
      
      tmp[8] = new LiteralStackNode<IConstructor>(-1013, 8, prod__lit___59__char_class___range__59_59_, new int[] {59}, null, null);
      tmp[7] = new NonTerminalStackNode<IConstructor>(-1012, 7, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new SeparatedListStackNode<IConstructor>(-1009, 6, regular__iter_seps__$Variable__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(-1003, 0, "$Variable", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(-1005, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode<IConstructor>(-1006, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode<IConstructor>(-1008, 3, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(-1001, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new NonTerminalStackNode<IConstructor>(-998, 4, "$Type", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-996, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(-993, 2, "$Visibility", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-991, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-988, 0, "$Tags", null, null);
      builder.addAlternative(RascalParser.prod__variable_$Declaration__tags_$Tags_$layouts_LAYOUTLIST_visibility_$Visibility_$layouts_LAYOUTLIST_type_$Type_$layouts_LAYOUTLIST_variables_iter_seps__$Variable__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___59_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__alias_$Declaration__tags_$Tags_$layouts_LAYOUTLIST_visibility_$Visibility_$layouts_LAYOUTLIST_lit_alias_$layouts_LAYOUTLIST_user_$UserType_$layouts_LAYOUTLIST_lit___61_$layouts_LAYOUTLIST_base_$Type_$layouts_LAYOUTLIST_lit___59_(builder);
      
        _init_prod__annotation_$Declaration__tags_$Tags_$layouts_LAYOUTLIST_visibility_$Visibility_$layouts_LAYOUTLIST_lit_anno_$layouts_LAYOUTLIST_annoType_$Type_$layouts_LAYOUTLIST_onType_$Type_$layouts_LAYOUTLIST_lit___64_$layouts_LAYOUTLIST_name_$Name_$layouts_LAYOUTLIST_lit___59_(builder);
      
        _init_prod__data_$Declaration__tags_$Tags_$layouts_LAYOUTLIST_visibility_$Visibility_$layouts_LAYOUTLIST_lit_data_$layouts_LAYOUTLIST_user_$UserType_$layouts_LAYOUTLIST_lit___61_$layouts_LAYOUTLIST_variants_iter_seps__$Variant__$layouts_LAYOUTLIST_lit___124_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___59__tag__Foldable(builder);
      
        _init_prod__dataAbstract_$Declaration__tags_$Tags_$layouts_LAYOUTLIST_visibility_$Visibility_$layouts_LAYOUTLIST_lit_data_$layouts_LAYOUTLIST_user_$UserType_$layouts_LAYOUTLIST_lit___59_(builder);
      
        _init_prod__function_$Declaration__functionDeclaration_$FunctionDeclaration_(builder);
      
        _init_prod__tag_$Declaration__tags_$Tags_$layouts_LAYOUTLIST_visibility_$Visibility_$layouts_LAYOUTLIST_lit_tag_$layouts_LAYOUTLIST_kind_$Kind_$layouts_LAYOUTLIST_name_$Name_$layouts_LAYOUTLIST_lit_on_$layouts_LAYOUTLIST_types_iter_seps__$Type__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___59_(builder);
      
        _init_prod__variable_$Declaration__tags_$Tags_$layouts_LAYOUTLIST_visibility_$Visibility_$layouts_LAYOUTLIST_type_$Type_$layouts_LAYOUTLIST_variables_iter_seps__$Variable__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___59_(builder);
      
    }
  }
	
  protected static class $FunctionModifiers {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__list_$FunctionModifiers__modifiers_iter_star_seps__$FunctionModifier__$layouts_LAYOUTLIST_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new SeparatedListStackNode<IConstructor>(-807, 0, regular__iter_star_seps__$FunctionModifier__$layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(-804, 0, "$FunctionModifier", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(-806, 1, "$layouts_LAYOUTLIST", null, null)}, false, null, null);
      builder.addAlternative(RascalParser.prod__list_$FunctionModifiers__modifiers_iter_star_seps__$FunctionModifier__$layouts_LAYOUTLIST_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__list_$FunctionModifiers__modifiers_iter_star_seps__$FunctionModifier__$layouts_LAYOUTLIST_(builder);
      
    }
  }
	
  protected static class $FunctionType {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__typeArguments_$FunctionType__type_$Type_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_arguments_iter_star_seps__$TypeArg__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[7];
      
      tmp[6] = new LiteralStackNode<IConstructor>(-1217, 6, prod__lit___41__char_class___range__41_41_, new int[] {41}, null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(-1216, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new SeparatedListStackNode<IConstructor>(-1213, 4, regular__iter_star_seps__$TypeArg__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(-1207, 0, "$TypeArg", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(-1209, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode<IConstructor>(-1210, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode<IConstructor>(-1212, 3, "$layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-1205, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(-1203, 2, prod__lit___40__char_class___range__40_40_, new int[] {40}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-1202, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-1199, 0, "$Type", null, null);
      builder.addAlternative(RascalParser.prod__typeArguments_$FunctionType__type_$Type_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_arguments_iter_star_seps__$TypeArg__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__typeArguments_$FunctionType__type_$Type_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_arguments_iter_star_seps__$TypeArg__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_(builder);
      
    }
  }
	
  protected static class $Type {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__basic_$Type__basic_$BasicType_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(-860, 0, "$BasicType", null, null);
      builder.addAlternative(RascalParser.prod__basic_$Type__basic_$BasicType_, tmp);
	}
    protected static final void _init_prod__bracket_$Type__lit___40_$layouts_LAYOUTLIST_type_$Type_$layouts_LAYOUTLIST_lit___41__bracket(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new LiteralStackNode<IConstructor>(-849, 4, prod__lit___41__char_class___range__41_41_, new int[] {41}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-848, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(-845, 2, "$Type", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-843, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-841, 0, prod__lit___40__char_class___range__40_40_, new int[] {40}, null, null);
      builder.addAlternative(RascalParser.prod__bracket_$Type__lit___40_$layouts_LAYOUTLIST_type_$Type_$layouts_LAYOUTLIST_lit___41__bracket, tmp);
	}
    protected static final void _init_prod__function_$Type__function_$FunctionType_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(-854, 0, "$FunctionType", null, null);
      builder.addAlternative(RascalParser.prod__function_$Type__function_$FunctionType_, tmp);
	}
    protected static final void _init_prod__selector_$Type__selector_$DataTypeSelector_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(-817, 0, "$DataTypeSelector", null, null);
      builder.addAlternative(RascalParser.prod__selector_$Type__selector_$DataTypeSelector_, tmp);
	}
    protected static final void _init_prod__structured_$Type__structured_$StructuredType_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(-836, 0, "$StructuredType", null, null);
      builder.addAlternative(RascalParser.prod__structured_$Type__structured_$StructuredType_, tmp);
	}
    protected static final void _init_prod__symbol_$Type__symbol_$Sym_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(-830, 0, "$Sym", null, null);
      builder.addAlternative(RascalParser.prod__symbol_$Type__symbol_$Sym_, tmp);
	}
    protected static final void _init_prod__user_$Type__user_$UserType_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(-823, 0, "$UserType", null, null);
      builder.addAlternative(RascalParser.prod__user_$Type__user_$UserType_, tmp);
	}
    protected static final void _init_prod__variable_$Type__typeVar_$TypeVar_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(-866, 0, "$TypeVar", null, null);
      builder.addAlternative(RascalParser.prod__variable_$Type__typeVar_$TypeVar_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__basic_$Type__basic_$BasicType_(builder);
      
        _init_prod__bracket_$Type__lit___40_$layouts_LAYOUTLIST_type_$Type_$layouts_LAYOUTLIST_lit___41__bracket(builder);
      
        _init_prod__function_$Type__function_$FunctionType_(builder);
      
        _init_prod__selector_$Type__selector_$DataTypeSelector_(builder);
      
        _init_prod__structured_$Type__structured_$StructuredType_(builder);
      
        _init_prod__symbol_$Type__symbol_$Sym_(builder);
      
        _init_prod__user_$Type__user_$UserType_(builder);
      
        _init_prod__variable_$Type__typeVar_$TypeVar_(builder);
      
    }
  }
	
  protected static class $Statement {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__append_$Statement__lit_append_$layouts_LAYOUTLIST_dataTarget_$DataTarget_$layouts_LAYOUTLIST_statement_$Statement__assoc__non_assoc_tag__breakable(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(-1567, 4, "$Statement", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-1564, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(-1561, 2, "$DataTarget", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-1559, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-1557, 0, prod__lit_append__char_class___range__97_97_char_class___range__112_112_char_class___range__112_112_char_class___range__101_101_char_class___range__110_110_char_class___range__100_100_, new int[] {97,112,112,101,110,100}, null, null);
      builder.addAlternative(RascalParser.prod__append_$Statement__lit_append_$layouts_LAYOUTLIST_dataTarget_$DataTarget_$layouts_LAYOUTLIST_statement_$Statement__assoc__non_assoc_tag__breakable, tmp);
	}
    protected static final void _init_prod__assert_$Statement__lit_assert_$layouts_LAYOUTLIST_expression_$Expression_$layouts_LAYOUTLIST_lit___59__tag__breakable(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new LiteralStackNode<IConstructor>(-1761, 4, prod__lit___59__char_class___range__59_59_, new int[] {59}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-1760, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(-1757, 2, "$Expression", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-1755, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-1753, 0, prod__lit_assert__char_class___range__97_97_char_class___range__115_115_char_class___range__115_115_char_class___range__101_101_char_class___range__114_114_char_class___range__116_116_, new int[] {97,115,115,101,114,116}, null, null);
      builder.addAlternative(RascalParser.prod__assert_$Statement__lit_assert_$layouts_LAYOUTLIST_expression_$Expression_$layouts_LAYOUTLIST_lit___59__tag__breakable, tmp);
	}
    protected static final void _init_prod__assertWithMessage_$Statement__lit_assert_$layouts_LAYOUTLIST_expression_$Expression_$layouts_LAYOUTLIST_lit___58_$layouts_LAYOUTLIST_message_$Expression_$layouts_LAYOUTLIST_lit___59__tag__breakable(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[9];
      
      tmp[8] = new LiteralStackNode<IConstructor>(-1613, 8, prod__lit___59__char_class___range__59_59_, new int[] {59}, null, null);
      tmp[7] = new NonTerminalStackNode<IConstructor>(-1612, 7, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new NonTerminalStackNode<IConstructor>(-1609, 6, "$Expression", null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(-1607, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode<IConstructor>(-1605, 4, prod__lit___58__char_class___range__58_58_, new int[] {58}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-1604, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(-1601, 2, "$Expression", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-1599, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-1597, 0, prod__lit_assert__char_class___range__97_97_char_class___range__115_115_char_class___range__115_115_char_class___range__101_101_char_class___range__114_114_char_class___range__116_116_, new int[] {97,115,115,101,114,116}, null, null);
      builder.addAlternative(RascalParser.prod__assertWithMessage_$Statement__lit_assert_$layouts_LAYOUTLIST_expression_$Expression_$layouts_LAYOUTLIST_lit___58_$layouts_LAYOUTLIST_message_$Expression_$layouts_LAYOUTLIST_lit___59__tag__breakable, tmp);
	}
    protected static final void _init_prod__assignment_$Statement__assignable_$Assignable_$layouts_LAYOUTLIST_operator_$Assignment_$layouts_LAYOUTLIST_statement_$Statement__tag__breakable(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(-1393, 4, "$Statement", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-1390, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(-1387, 2, "$Assignment", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-1385, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-1382, 0, "$Assignable", null, null);
      builder.addAlternative(RascalParser.prod__assignment_$Statement__assignable_$Assignable_$layouts_LAYOUTLIST_operator_$Assignment_$layouts_LAYOUTLIST_statement_$Statement__tag__breakable, tmp);
	}
    protected static final void _init_prod__break_$Statement__lit_break_$layouts_LAYOUTLIST_target_$Target_$layouts_LAYOUTLIST_lit___59__tag__breakable(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new LiteralStackNode<IConstructor>(-1508, 4, prod__lit___59__char_class___range__59_59_, new int[] {59}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-1507, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(-1504, 2, "$Target", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-1502, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-1500, 0, prod__lit_break__char_class___range__98_98_char_class___range__114_114_char_class___range__101_101_char_class___range__97_97_char_class___range__107_107_, new int[] {98,114,101,97,107}, null, null);
      builder.addAlternative(RascalParser.prod__break_$Statement__lit_break_$layouts_LAYOUTLIST_target_$Target_$layouts_LAYOUTLIST_lit___59__tag__breakable, tmp);
	}
    protected static final void _init_prod__continue_$Statement__lit_continue_$layouts_LAYOUTLIST_target_$Target_$layouts_LAYOUTLIST_lit___59__tag__breakable(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new LiteralStackNode<IConstructor>(-1739, 4, prod__lit___59__char_class___range__59_59_, new int[] {59}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-1738, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(-1735, 2, "$Target", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-1733, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-1731, 0, prod__lit_continue__char_class___range__99_99_char_class___range__111_111_char_class___range__110_110_char_class___range__116_116_char_class___range__105_105_char_class___range__110_110_char_class___range__117_117_char_class___range__101_101_, new int[] {99,111,110,116,105,110,117,101}, null, null);
      builder.addAlternative(RascalParser.prod__continue_$Statement__lit_continue_$layouts_LAYOUTLIST_target_$Target_$layouts_LAYOUTLIST_lit___59__tag__breakable, tmp);
	}
    protected static final void _init_prod__doWhile_$Statement__label_$Label_$layouts_LAYOUTLIST_lit_do_$layouts_LAYOUTLIST_body_$Statement_$layouts_LAYOUTLIST_lit_while_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_condition_$Expression_$layouts_LAYOUTLIST_lit___41_$layouts_LAYOUTLIST_lit___59__tag__breakable(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[15];
      
      tmp[14] = new LiteralStackNode<IConstructor>(-1727, 14, prod__lit___59__char_class___range__59_59_, new int[] {59}, null, null);
      tmp[13] = new NonTerminalStackNode<IConstructor>(-1726, 13, "$layouts_LAYOUTLIST", null, null);
      tmp[12] = new LiteralStackNode<IConstructor>(-1724, 12, prod__lit___41__char_class___range__41_41_, new int[] {41}, null, null);
      tmp[11] = new NonTerminalStackNode<IConstructor>(-1723, 11, "$layouts_LAYOUTLIST", null, null);
      tmp[10] = new NonTerminalStackNode<IConstructor>(-1720, 10, "$Expression", null, null);
      tmp[9] = new NonTerminalStackNode<IConstructor>(-1718, 9, "$layouts_LAYOUTLIST", null, null);
      tmp[8] = new LiteralStackNode<IConstructor>(-1716, 8, prod__lit___40__char_class___range__40_40_, new int[] {40}, null, null);
      tmp[7] = new NonTerminalStackNode<IConstructor>(-1715, 7, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new LiteralStackNode<IConstructor>(-1713, 6, prod__lit_while__char_class___range__119_119_char_class___range__104_104_char_class___range__105_105_char_class___range__108_108_char_class___range__101_101_, new int[] {119,104,105,108,101}, null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(-1712, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new NonTerminalStackNode<IConstructor>(-1709, 4, "$Statement", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-1707, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(-1705, 2, prod__lit_do__char_class___range__100_100_char_class___range__111_111_, new int[] {100,111}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-1704, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-1701, 0, "$Label", null, null);
      builder.addAlternative(RascalParser.prod__doWhile_$Statement__label_$Label_$layouts_LAYOUTLIST_lit_do_$layouts_LAYOUTLIST_body_$Statement_$layouts_LAYOUTLIST_lit_while_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_condition_$Expression_$layouts_LAYOUTLIST_lit___41_$layouts_LAYOUTLIST_lit___59__tag__breakable, tmp);
	}
    protected static final void _init_prod__emptyStatement_$Statement__lit___59_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-1463, 0, prod__lit___59__char_class___range__59_59_, new int[] {59}, null, null);
      builder.addAlternative(RascalParser.prod__emptyStatement_$Statement__lit___59_, tmp);
	}
    protected static final void _init_prod__expression_$Statement__expression_$Expression_$layouts_LAYOUTLIST_lit___59__tag__breakable(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new LiteralStackNode<IConstructor>(-1749, 2, prod__lit___59__char_class___range__59_59_, new int[] {59}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-1748, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-1745, 0, "$Expression", null, null);
      builder.addAlternative(RascalParser.prod__expression_$Statement__expression_$Expression_$layouts_LAYOUTLIST_lit___59__tag__breakable, tmp);
	}
    protected static final void _init_prod__fail_$Statement__lit_fail_$layouts_LAYOUTLIST_target_$Target_$layouts_LAYOUTLIST_lit___59__tag__breakable(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new LiteralStackNode<IConstructor>(-1805, 4, prod__lit___59__char_class___range__59_59_, new int[] {59}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-1804, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(-1801, 2, "$Target", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-1799, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-1797, 0, prod__lit_fail__char_class___range__102_102_char_class___range__97_97_char_class___range__105_105_char_class___range__108_108_, new int[] {102,97,105,108}, null, null);
      builder.addAlternative(RascalParser.prod__fail_$Statement__lit_fail_$layouts_LAYOUTLIST_target_$Target_$layouts_LAYOUTLIST_lit___59__tag__breakable, tmp);
	}
    protected static final void _init_prod__filter_$Statement__lit_filter_$layouts_LAYOUTLIST_lit___59__tag__breakable(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new LiteralStackNode<IConstructor>(-1496, 2, prod__lit___59__char_class___range__59_59_, new int[] {59}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-1495, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-1493, 0, prod__lit_filter__char_class___range__102_102_char_class___range__105_105_char_class___range__108_108_char_class___range__116_116_char_class___range__101_101_char_class___range__114_114_, new int[] {102,105,108,116,101,114}, null, null);
      builder.addAlternative(RascalParser.prod__filter_$Statement__lit_filter_$layouts_LAYOUTLIST_lit___59__tag__breakable, tmp);
	}
    protected static final void _init_prod__for_$Statement__label_$Label_$layouts_LAYOUTLIST_lit_for_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_generators_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_$layouts_LAYOUTLIST_body_$Statement__tag__breakable___123_103_101_110_101_114_97_116_111_114_115_125_tag__breakable(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[11];
      
      tmp[10] = new NonTerminalStackNode<IConstructor>(-1424, 10, "$Statement", null, null);
      tmp[9] = new NonTerminalStackNode<IConstructor>(-1422, 9, "$layouts_LAYOUTLIST", null, null);
      tmp[8] = new LiteralStackNode<IConstructor>(-1420, 8, prod__lit___41__char_class___range__41_41_, new int[] {41}, null, null);
      tmp[7] = new NonTerminalStackNode<IConstructor>(-1419, 7, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new SeparatedListStackNode<IConstructor>(-1416, 6, regular__iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(-1410, 0, "$Expression", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(-1412, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode<IConstructor>(-1413, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode<IConstructor>(-1415, 3, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(-1408, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode<IConstructor>(-1406, 4, prod__lit___40__char_class___range__40_40_, new int[] {40}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-1405, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(-1403, 2, prod__lit_for__char_class___range__102_102_char_class___range__111_111_char_class___range__114_114_, new int[] {102,111,114}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-1402, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-1399, 0, "$Label", null, null);
      builder.addAlternative(RascalParser.prod__for_$Statement__label_$Label_$layouts_LAYOUTLIST_lit_for_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_generators_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_$layouts_LAYOUTLIST_body_$Statement__tag__breakable___123_103_101_110_101_114_97_116_111_114_115_125_tag__breakable, tmp);
	}
    protected static final void _init_prod__functionDeclaration_$Statement__functionDeclaration_$FunctionDeclaration__tag__breakable(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(-1618, 0, "$FunctionDeclaration", null, null);
      builder.addAlternative(RascalParser.prod__functionDeclaration_$Statement__functionDeclaration_$FunctionDeclaration__tag__breakable, tmp);
	}
    protected static final void _init_prod__globalDirective_$Statement__lit_global_$layouts_LAYOUTLIST_type_$Type_$layouts_LAYOUTLIST_names_iter_seps__$QualifiedName__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___59__tag__breakable(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[7];
      
      tmp[6] = new LiteralStackNode<IConstructor>(-1531, 6, prod__lit___59__char_class___range__59_59_, new int[] {59}, null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(-1530, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new SeparatedListStackNode<IConstructor>(-1527, 4, regular__iter_seps__$QualifiedName__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(-1521, 0, "$QualifiedName", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(-1523, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode<IConstructor>(-1524, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode<IConstructor>(-1526, 3, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-1519, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(-1516, 2, "$Type", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-1514, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-1512, 0, prod__lit_global__char_class___range__103_103_char_class___range__108_108_char_class___range__111_111_char_class___range__98_98_char_class___range__97_97_char_class___range__108_108_, new int[] {103,108,111,98,97,108}, null, null);
      builder.addAlternative(RascalParser.prod__globalDirective_$Statement__lit_global_$layouts_LAYOUTLIST_type_$Type_$layouts_LAYOUTLIST_names_iter_seps__$QualifiedName__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___59__tag__breakable, tmp);
	}
    protected static final void _init_prod__ifThen_$Statement__label_$Label_$layouts_LAYOUTLIST_lit_if_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_conditions_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_$layouts_LAYOUTLIST_thenStatement_$Statement_$layouts_LAYOUTLIST_empty__tag__breakable(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[13];
      
      tmp[12] = new EmptyStackNode<IConstructor>(-1842, 12, regular__empty, null, new ICompletionFilter[] {new StringFollowRestriction(new int[] {101,108,115,101})});
      tmp[11] = new NonTerminalStackNode<IConstructor>(-1839, 11, "$layouts_LAYOUTLIST", null, null);
      tmp[10] = new NonTerminalStackNode<IConstructor>(-1836, 10, "$Statement", null, null);
      tmp[9] = new NonTerminalStackNode<IConstructor>(-1833, 9, "$layouts_LAYOUTLIST", null, null);
      tmp[8] = new LiteralStackNode<IConstructor>(-1831, 8, prod__lit___41__char_class___range__41_41_, new int[] {41}, null, null);
      tmp[7] = new NonTerminalStackNode<IConstructor>(-1830, 7, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new SeparatedListStackNode<IConstructor>(-1827, 6, regular__iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(-1821, 0, "$Expression", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(-1823, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode<IConstructor>(-1824, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode<IConstructor>(-1826, 3, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(-1819, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode<IConstructor>(-1817, 4, prod__lit___40__char_class___range__40_40_, new int[] {40}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-1816, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(-1814, 2, prod__lit_if__char_class___range__105_105_char_class___range__102_102_, new int[] {105,102}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-1813, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-1810, 0, "$Label", null, null);
      builder.addAlternative(RascalParser.prod__ifThen_$Statement__label_$Label_$layouts_LAYOUTLIST_lit_if_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_conditions_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_$layouts_LAYOUTLIST_thenStatement_$Statement_$layouts_LAYOUTLIST_empty__tag__breakable, tmp);
	}
    protected static final void _init_prod__ifThenElse_$Statement__label_$Label_$layouts_LAYOUTLIST_lit_if_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_conditions_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_$layouts_LAYOUTLIST_thenStatement_$Statement_$layouts_LAYOUTLIST_lit_else_$layouts_LAYOUTLIST_elseStatement_$Statement__tag__breakable(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[15];
      
      tmp[14] = new NonTerminalStackNode<IConstructor>(-1695, 14, "$Statement", null, null);
      tmp[13] = new NonTerminalStackNode<IConstructor>(-1692, 13, "$layouts_LAYOUTLIST", null, null);
      tmp[12] = new LiteralStackNode<IConstructor>(-1690, 12, prod__lit_else__char_class___range__101_101_char_class___range__108_108_char_class___range__115_115_char_class___range__101_101_, new int[] {101,108,115,101}, null, null);
      tmp[11] = new NonTerminalStackNode<IConstructor>(-1689, 11, "$layouts_LAYOUTLIST", null, null);
      tmp[10] = new NonTerminalStackNode<IConstructor>(-1686, 10, "$Statement", null, null);
      tmp[9] = new NonTerminalStackNode<IConstructor>(-1684, 9, "$layouts_LAYOUTLIST", null, null);
      tmp[8] = new LiteralStackNode<IConstructor>(-1682, 8, prod__lit___41__char_class___range__41_41_, new int[] {41}, null, null);
      tmp[7] = new NonTerminalStackNode<IConstructor>(-1681, 7, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new SeparatedListStackNode<IConstructor>(-1678, 6, regular__iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(-1672, 0, "$Expression", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(-1674, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode<IConstructor>(-1675, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode<IConstructor>(-1677, 3, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(-1670, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode<IConstructor>(-1668, 4, prod__lit___40__char_class___range__40_40_, new int[] {40}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-1667, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(-1665, 2, prod__lit_if__char_class___range__105_105_char_class___range__102_102_, new int[] {105,102}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-1664, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-1661, 0, "$Label", null, null);
      builder.addAlternative(RascalParser.prod__ifThenElse_$Statement__label_$Label_$layouts_LAYOUTLIST_lit_if_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_conditions_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_$layouts_LAYOUTLIST_thenStatement_$Statement_$layouts_LAYOUTLIST_lit_else_$layouts_LAYOUTLIST_elseStatement_$Statement__tag__breakable, tmp);
	}
    protected static final void _init_prod__insert_$Statement__lit_insert_$layouts_LAYOUTLIST_dataTarget_$DataTarget_$layouts_LAYOUTLIST_statement_$Statement__assoc__non_assoc_tag__breakable(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(-1582, 4, "$Statement", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-1579, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(-1576, 2, "$DataTarget", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-1574, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-1572, 0, prod__lit_insert__char_class___range__105_105_char_class___range__110_110_char_class___range__115_115_char_class___range__101_101_char_class___range__114_114_char_class___range__116_116_, new int[] {105,110,115,101,114,116}, null, null);
      builder.addAlternative(RascalParser.prod__insert_$Statement__lit_insert_$layouts_LAYOUTLIST_dataTarget_$DataTarget_$layouts_LAYOUTLIST_statement_$Statement__assoc__non_assoc_tag__breakable, tmp);
	}
    protected static final void _init_prod__nonEmptyBlock_$Statement__label_$Label_$layouts_LAYOUTLIST_lit___123_$layouts_LAYOUTLIST_statements_iter_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[7];
      
      tmp[6] = new LiteralStackNode<IConstructor>(-1639, 6, prod__lit___125__char_class___range__125_125_, new int[] {125}, null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(-1638, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new SeparatedListStackNode<IConstructor>(-1635, 4, regular__iter_seps__$Statement__$layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(-1632, 0, "$Statement", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(-1634, 1, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-1630, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(-1628, 2, prod__lit___123__char_class___range__123_123_, new int[] {123}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-1627, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-1624, 0, "$Label", null, null);
      builder.addAlternative(RascalParser.prod__nonEmptyBlock_$Statement__label_$Label_$layouts_LAYOUTLIST_lit___123_$layouts_LAYOUTLIST_statements_iter_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_, tmp);
	}
    protected static final void _init_prod__return_$Statement__lit_return_$layouts_LAYOUTLIST_statement_$Statement__assoc__non_assoc_tag__breakable(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode<IConstructor>(-1592, 2, "$Statement", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-1589, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-1587, 0, prod__lit_return__char_class___range__114_114_char_class___range__101_101_char_class___range__116_116_char_class___range__117_117_char_class___range__114_114_char_class___range__110_110_, new int[] {114,101,116,117,114,110}, null, null);
      builder.addAlternative(RascalParser.prod__return_$Statement__lit_return_$layouts_LAYOUTLIST_statement_$Statement__assoc__non_assoc_tag__breakable, tmp);
	}
    protected static final void _init_prod__solve_$Statement__lit_solve_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_variables_iter_seps__$QualifiedName__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_bound_$Bound_$layouts_LAYOUTLIST_lit___41_$layouts_LAYOUTLIST_body_$Statement__tag__breakable(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[11];
      
      tmp[10] = new NonTerminalStackNode<IConstructor>(-1792, 10, "$Statement", null, null);
      tmp[9] = new NonTerminalStackNode<IConstructor>(-1789, 9, "$layouts_LAYOUTLIST", null, null);
      tmp[8] = new LiteralStackNode<IConstructor>(-1787, 8, prod__lit___41__char_class___range__41_41_, new int[] {41}, null, null);
      tmp[7] = new NonTerminalStackNode<IConstructor>(-1786, 7, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new NonTerminalStackNode<IConstructor>(-1783, 6, "$Bound", null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(-1781, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new SeparatedListStackNode<IConstructor>(-1778, 4, regular__iter_seps__$QualifiedName__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(-1772, 0, "$QualifiedName", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(-1774, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode<IConstructor>(-1775, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode<IConstructor>(-1777, 3, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-1770, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(-1768, 2, prod__lit___40__char_class___range__40_40_, new int[] {40}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-1767, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-1765, 0, prod__lit_solve__char_class___range__115_115_char_class___range__111_111_char_class___range__108_108_char_class___range__118_118_char_class___range__101_101_, new int[] {115,111,108,118,101}, null, null);
      builder.addAlternative(RascalParser.prod__solve_$Statement__lit_solve_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_variables_iter_seps__$QualifiedName__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_bound_$Bound_$layouts_LAYOUTLIST_lit___41_$layouts_LAYOUTLIST_body_$Statement__tag__breakable, tmp);
	}
    protected static final void _init_prod__switch_$Statement__label_$Label_$layouts_LAYOUTLIST_lit_switch_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_expression_$Expression_$layouts_LAYOUTLIST_lit___41_$layouts_LAYOUTLIST_lit___123_$layouts_LAYOUTLIST_cases_iter_seps__$Case__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125__tag__breakable(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[15];
      
      tmp[14] = new LiteralStackNode<IConstructor>(-1459, 14, prod__lit___125__char_class___range__125_125_, new int[] {125}, null, null);
      tmp[13] = new NonTerminalStackNode<IConstructor>(-1458, 13, "$layouts_LAYOUTLIST", null, null);
      tmp[12] = new SeparatedListStackNode<IConstructor>(-1455, 12, regular__iter_seps__$Case__$layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(-1452, 0, "$Case", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(-1454, 1, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[11] = new NonTerminalStackNode<IConstructor>(-1450, 11, "$layouts_LAYOUTLIST", null, null);
      tmp[10] = new LiteralStackNode<IConstructor>(-1448, 10, prod__lit___123__char_class___range__123_123_, new int[] {123}, null, null);
      tmp[9] = new NonTerminalStackNode<IConstructor>(-1447, 9, "$layouts_LAYOUTLIST", null, null);
      tmp[8] = new LiteralStackNode<IConstructor>(-1445, 8, prod__lit___41__char_class___range__41_41_, new int[] {41}, null, null);
      tmp[7] = new NonTerminalStackNode<IConstructor>(-1444, 7, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new NonTerminalStackNode<IConstructor>(-1441, 6, "$Expression", null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(-1439, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode<IConstructor>(-1437, 4, prod__lit___40__char_class___range__40_40_, new int[] {40}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-1436, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(-1434, 2, prod__lit_switch__char_class___range__115_115_char_class___range__119_119_char_class___range__105_105_char_class___range__116_116_char_class___range__99_99_char_class___range__104_104_, new int[] {115,119,105,116,99,104}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-1433, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-1430, 0, "$Label", null, null);
      builder.addAlternative(RascalParser.prod__switch_$Statement__label_$Label_$layouts_LAYOUTLIST_lit_switch_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_expression_$Expression_$layouts_LAYOUTLIST_lit___41_$layouts_LAYOUTLIST_lit___123_$layouts_LAYOUTLIST_cases_iter_seps__$Case__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125__tag__breakable, tmp);
	}
    protected static final void _init_prod__throw_$Statement__lit_throw_$layouts_LAYOUTLIST_statement_$Statement__assoc__non_assoc_tag__breakable(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode<IConstructor>(-1552, 2, "$Statement", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-1549, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-1547, 0, prod__lit_throw__char_class___range__116_116_char_class___range__104_104_char_class___range__114_114_char_class___range__111_111_char_class___range__119_119_, new int[] {116,104,114,111,119}, null, null);
      builder.addAlternative(RascalParser.prod__throw_$Statement__lit_throw_$layouts_LAYOUTLIST_statement_$Statement__assoc__non_assoc_tag__breakable, tmp);
	}
    protected static final void _init_prod__try_$Statement__lit_try_$layouts_LAYOUTLIST_body_$Statement_$layouts_LAYOUTLIST_handlers_iter_seps__$Catch__$layouts_LAYOUTLIST__assoc__non_assoc_tag__breakable(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new SeparatedListStackNode<IConstructor>(-1655, 4, regular__iter_seps__$Catch__$layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(-1652, 0, "$Catch", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(-1654, 1, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-1650, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(-1647, 2, "$Statement", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-1645, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-1643, 0, prod__lit_try__char_class___range__116_116_char_class___range__114_114_char_class___range__121_121_, new int[] {116,114,121}, null, null);
      builder.addAlternative(RascalParser.prod__try_$Statement__lit_try_$layouts_LAYOUTLIST_body_$Statement_$layouts_LAYOUTLIST_handlers_iter_seps__$Catch__$layouts_LAYOUTLIST__assoc__non_assoc_tag__breakable, tmp);
	}
    protected static final void _init_prod__tryFinally_$Statement__lit_try_$layouts_LAYOUTLIST_body_$Statement_$layouts_LAYOUTLIST_handlers_iter_seps__$Catch__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit_finally_$layouts_LAYOUTLIST_finallyBody_$Statement__tag__breakable(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[9];
      
      tmp[8] = new NonTerminalStackNode<IConstructor>(-1488, 8, "$Statement", null, null);
      tmp[7] = new NonTerminalStackNode<IConstructor>(-1485, 7, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new LiteralStackNode<IConstructor>(-1483, 6, prod__lit_finally__char_class___range__102_102_char_class___range__105_105_char_class___range__110_110_char_class___range__97_97_char_class___range__108_108_char_class___range__108_108_char_class___range__121_121_, new int[] {102,105,110,97,108,108,121}, null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(-1482, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new SeparatedListStackNode<IConstructor>(-1479, 4, regular__iter_seps__$Catch__$layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(-1476, 0, "$Catch", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(-1478, 1, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-1474, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(-1471, 2, "$Statement", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-1469, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-1467, 0, prod__lit_try__char_class___range__116_116_char_class___range__114_114_char_class___range__121_121_, new int[] {116,114,121}, null, null);
      builder.addAlternative(RascalParser.prod__tryFinally_$Statement__lit_try_$layouts_LAYOUTLIST_body_$Statement_$layouts_LAYOUTLIST_handlers_iter_seps__$Catch__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit_finally_$layouts_LAYOUTLIST_finallyBody_$Statement__tag__breakable, tmp);
	}
    protected static final void _init_prod__variableDeclaration_$Statement__declaration_$LocalVariableDeclaration_$layouts_LAYOUTLIST_lit___59__tag__breakable(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new LiteralStackNode<IConstructor>(-1540, 2, prod__lit___59__char_class___range__59_59_, new int[] {59}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-1539, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-1536, 0, "$LocalVariableDeclaration", null, null);
      builder.addAlternative(RascalParser.prod__variableDeclaration_$Statement__declaration_$LocalVariableDeclaration_$layouts_LAYOUTLIST_lit___59__tag__breakable, tmp);
	}
    protected static final void _init_prod__visit_$Statement__label_$Label_$layouts_LAYOUTLIST_visit_$Visit__tag__breakable(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(-1339, 0, "$Label", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-1342, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(-1344, 2, "$Visit", null, null);
      builder.addAlternative(RascalParser.prod__visit_$Statement__label_$Label_$layouts_LAYOUTLIST_visit_$Visit__tag__breakable, tmp);
	}
    protected static final void _init_prod__while_$Statement__label_$Label_$layouts_LAYOUTLIST_lit_while_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_conditions_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_$layouts_LAYOUTLIST_body_$Statement__tag__breakable(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[11];
      
      tmp[10] = new NonTerminalStackNode<IConstructor>(-1376, 10, "$Statement", null, null);
      tmp[9] = new NonTerminalStackNode<IConstructor>(-1373, 9, "$layouts_LAYOUTLIST", null, null);
      tmp[8] = new LiteralStackNode<IConstructor>(-1371, 8, prod__lit___41__char_class___range__41_41_, new int[] {41}, null, null);
      tmp[7] = new NonTerminalStackNode<IConstructor>(-1370, 7, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new SeparatedListStackNode<IConstructor>(-1367, 6, regular__iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(-1361, 0, "$Expression", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(-1363, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode<IConstructor>(-1364, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode<IConstructor>(-1366, 3, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(-1359, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode<IConstructor>(-1357, 4, prod__lit___40__char_class___range__40_40_, new int[] {40}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-1356, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(-1354, 2, prod__lit_while__char_class___range__119_119_char_class___range__104_104_char_class___range__105_105_char_class___range__108_108_char_class___range__101_101_, new int[] {119,104,105,108,101}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-1353, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-1350, 0, "$Label", null, null);
      builder.addAlternative(RascalParser.prod__while_$Statement__label_$Label_$layouts_LAYOUTLIST_lit_while_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_conditions_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_$layouts_LAYOUTLIST_body_$Statement__tag__breakable, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__append_$Statement__lit_append_$layouts_LAYOUTLIST_dataTarget_$DataTarget_$layouts_LAYOUTLIST_statement_$Statement__assoc__non_assoc_tag__breakable(builder);
      
        _init_prod__assert_$Statement__lit_assert_$layouts_LAYOUTLIST_expression_$Expression_$layouts_LAYOUTLIST_lit___59__tag__breakable(builder);
      
        _init_prod__assertWithMessage_$Statement__lit_assert_$layouts_LAYOUTLIST_expression_$Expression_$layouts_LAYOUTLIST_lit___58_$layouts_LAYOUTLIST_message_$Expression_$layouts_LAYOUTLIST_lit___59__tag__breakable(builder);
      
        _init_prod__assignment_$Statement__assignable_$Assignable_$layouts_LAYOUTLIST_operator_$Assignment_$layouts_LAYOUTLIST_statement_$Statement__tag__breakable(builder);
      
        _init_prod__break_$Statement__lit_break_$layouts_LAYOUTLIST_target_$Target_$layouts_LAYOUTLIST_lit___59__tag__breakable(builder);
      
        _init_prod__continue_$Statement__lit_continue_$layouts_LAYOUTLIST_target_$Target_$layouts_LAYOUTLIST_lit___59__tag__breakable(builder);
      
        _init_prod__doWhile_$Statement__label_$Label_$layouts_LAYOUTLIST_lit_do_$layouts_LAYOUTLIST_body_$Statement_$layouts_LAYOUTLIST_lit_while_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_condition_$Expression_$layouts_LAYOUTLIST_lit___41_$layouts_LAYOUTLIST_lit___59__tag__breakable(builder);
      
        _init_prod__emptyStatement_$Statement__lit___59_(builder);
      
        _init_prod__expression_$Statement__expression_$Expression_$layouts_LAYOUTLIST_lit___59__tag__breakable(builder);
      
        _init_prod__fail_$Statement__lit_fail_$layouts_LAYOUTLIST_target_$Target_$layouts_LAYOUTLIST_lit___59__tag__breakable(builder);
      
        _init_prod__filter_$Statement__lit_filter_$layouts_LAYOUTLIST_lit___59__tag__breakable(builder);
      
        _init_prod__for_$Statement__label_$Label_$layouts_LAYOUTLIST_lit_for_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_generators_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_$layouts_LAYOUTLIST_body_$Statement__tag__breakable___123_103_101_110_101_114_97_116_111_114_115_125_tag__breakable(builder);
      
        _init_prod__functionDeclaration_$Statement__functionDeclaration_$FunctionDeclaration__tag__breakable(builder);
      
        _init_prod__globalDirective_$Statement__lit_global_$layouts_LAYOUTLIST_type_$Type_$layouts_LAYOUTLIST_names_iter_seps__$QualifiedName__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___59__tag__breakable(builder);
      
        _init_prod__ifThen_$Statement__label_$Label_$layouts_LAYOUTLIST_lit_if_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_conditions_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_$layouts_LAYOUTLIST_thenStatement_$Statement_$layouts_LAYOUTLIST_empty__tag__breakable(builder);
      
        _init_prod__ifThenElse_$Statement__label_$Label_$layouts_LAYOUTLIST_lit_if_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_conditions_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_$layouts_LAYOUTLIST_thenStatement_$Statement_$layouts_LAYOUTLIST_lit_else_$layouts_LAYOUTLIST_elseStatement_$Statement__tag__breakable(builder);
      
        _init_prod__insert_$Statement__lit_insert_$layouts_LAYOUTLIST_dataTarget_$DataTarget_$layouts_LAYOUTLIST_statement_$Statement__assoc__non_assoc_tag__breakable(builder);
      
        _init_prod__nonEmptyBlock_$Statement__label_$Label_$layouts_LAYOUTLIST_lit___123_$layouts_LAYOUTLIST_statements_iter_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_(builder);
      
        _init_prod__return_$Statement__lit_return_$layouts_LAYOUTLIST_statement_$Statement__assoc__non_assoc_tag__breakable(builder);
      
        _init_prod__solve_$Statement__lit_solve_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_variables_iter_seps__$QualifiedName__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_bound_$Bound_$layouts_LAYOUTLIST_lit___41_$layouts_LAYOUTLIST_body_$Statement__tag__breakable(builder);
      
        _init_prod__switch_$Statement__label_$Label_$layouts_LAYOUTLIST_lit_switch_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_expression_$Expression_$layouts_LAYOUTLIST_lit___41_$layouts_LAYOUTLIST_lit___123_$layouts_LAYOUTLIST_cases_iter_seps__$Case__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125__tag__breakable(builder);
      
        _init_prod__throw_$Statement__lit_throw_$layouts_LAYOUTLIST_statement_$Statement__assoc__non_assoc_tag__breakable(builder);
      
        _init_prod__try_$Statement__lit_try_$layouts_LAYOUTLIST_body_$Statement_$layouts_LAYOUTLIST_handlers_iter_seps__$Catch__$layouts_LAYOUTLIST__assoc__non_assoc_tag__breakable(builder);
      
        _init_prod__tryFinally_$Statement__lit_try_$layouts_LAYOUTLIST_body_$Statement_$layouts_LAYOUTLIST_handlers_iter_seps__$Catch__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit_finally_$layouts_LAYOUTLIST_finallyBody_$Statement__tag__breakable(builder);
      
        _init_prod__variableDeclaration_$Statement__declaration_$LocalVariableDeclaration_$layouts_LAYOUTLIST_lit___59__tag__breakable(builder);
      
        _init_prod__visit_$Statement__label_$Label_$layouts_LAYOUTLIST_visit_$Visit__tag__breakable(builder);
      
        _init_prod__while_$Statement__label_$Label_$layouts_LAYOUTLIST_lit_while_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_conditions_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_$layouts_LAYOUTLIST_body_$Statement__tag__breakable(builder);
      
    }
  }
	
  protected static class $Command {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__declaration_$Command__declaration_$Declaration_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(-2101, 0, "$Declaration", null, null);
      builder.addAlternative(RascalParser.prod__declaration_$Command__declaration_$Declaration_, tmp);
	}
    protected static final void _init_prod__expression_$Command__expression_$Expression_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(-2095, 0, "$Expression", null, null);
      builder.addAlternative(RascalParser.prod__expression_$Command__expression_$Expression_, tmp);
	}
    protected static final void _init_prod__import_$Command__imported_$Import_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(-2081, 0, "$Import", null, null);
      builder.addAlternative(RascalParser.prod__import_$Command__imported_$Import_, tmp);
	}
    protected static final void _init_prod__shell_$Command__lit___58_$layouts_LAYOUTLIST_command_$ShellCommand_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode<IConstructor>(-2110, 2, "$ShellCommand", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-2108, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-2106, 0, prod__lit___58__char_class___range__58_58_, new int[] {58}, null, null);
      builder.addAlternative(RascalParser.prod__shell_$Command__lit___58_$layouts_LAYOUTLIST_command_$ShellCommand_, tmp);
	}
    protected static final void _init_prod__statement_$Command__statement_$Statement_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(-2088, 0, "$Statement", null, null);
      builder.addAlternative(RascalParser.prod__statement_$Command__statement_$Statement_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__declaration_$Command__declaration_$Declaration_(builder);
      
        _init_prod__expression_$Command__expression_$Expression_(builder);
      
        _init_prod__import_$Command__imported_$Import_(builder);
      
        _init_prod__shell_$Command__lit___58_$layouts_LAYOUTLIST_command_$ShellCommand_(builder);
      
        _init_prod__statement_$Command__statement_$Statement_(builder);
      
    }
  }
	
  protected static class $NamedBackslash {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__$NamedBackslash__char_class___range__92_92_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new CharStackNode<IConstructor>(-1990, 0, new int[][]{{92,92}}, null, new ICompletionFilter[] {new CharFollowRestriction(new int[][]{{60,60},{62,62},{92,92}})});
      builder.addAlternative(RascalParser.prod__$NamedBackslash__char_class___range__92_92_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__$NamedBackslash__char_class___range__92_92_(builder);
      
    }
  }
	
  protected static class $PostPathChars {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__$PostPathChars__lit___62_$URLChars_lit___124_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new LiteralStackNode<IConstructor>(-1981, 2, prod__lit___124__char_class___range__124_124_, new int[] {124}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-1980, 1, "$URLChars", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-1978, 0, prod__lit___62__char_class___range__62_62_, new int[] {62}, null, null);
      builder.addAlternative(RascalParser.prod__$PostPathChars__lit___62_$URLChars_lit___124_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__$PostPathChars__lit___62_$URLChars_lit___124_(builder);
      
    }
  }
	
  protected static class $PreStringChars {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__$PreStringChars__char_class___range__34_34_iter_star__$StringCharacter_char_class___range__60_60__tag__category___67_111_110_115_116_97_110_116(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new CharStackNode<IConstructor>(-2220, 2, new int[][]{{60,60}}, null, null);
      tmp[1] = new ListStackNode<IConstructor>(-2219, 1, regular__iter_star__$StringCharacter, new NonTerminalStackNode<IConstructor>(-2218, 0, "$StringCharacter", null, null), false, null, null);
      tmp[0] = new CharStackNode<IConstructor>(-2216, 0, new int[][]{{34,34}}, null, null);
      builder.addAlternative(RascalParser.prod__$PreStringChars__char_class___range__34_34_iter_star__$StringCharacter_char_class___range__60_60__tag__category___67_111_110_115_116_97_110_116, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__$PreStringChars__char_class___range__34_34_iter_star__$StringCharacter_char_class___range__60_60__tag__category___67_111_110_115_116_97_110_116(builder);
      
    }
  }
	
  protected static class $ProtocolTail {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__mid_$ProtocolTail__mid_$MidProtocolChars_$layouts_LAYOUTLIST_expression_$Expression_$layouts_LAYOUTLIST_tail_$ProtocolTail_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(-2136, 4, "$ProtocolTail", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-2134, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(-2131, 2, "$Expression", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-2129, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-2126, 0, "$MidProtocolChars", null, null);
      builder.addAlternative(RascalParser.prod__mid_$ProtocolTail__mid_$MidProtocolChars_$layouts_LAYOUTLIST_expression_$Expression_$layouts_LAYOUTLIST_tail_$ProtocolTail_, tmp);
	}
    protected static final void _init_prod__post_$ProtocolTail__post_$PostProtocolChars_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(-2120, 0, "$PostProtocolChars", null, null);
      builder.addAlternative(RascalParser.prod__post_$ProtocolTail__post_$PostProtocolChars_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__mid_$ProtocolTail__mid_$MidProtocolChars_$layouts_LAYOUTLIST_expression_$Expression_$layouts_LAYOUTLIST_tail_$ProtocolTail_(builder);
      
        _init_prod__post_$ProtocolTail__post_$PostProtocolChars_(builder);
      
    }
  }
	
  protected static class $Renamings {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__default_$Renamings__lit_renaming_$layouts_LAYOUTLIST_renamings_iter_seps__$Renaming__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new SeparatedListStackNode<IConstructor>(-1894, 2, regular__iter_seps__$Renaming__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(-1888, 0, "$Renaming", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(-1890, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode<IConstructor>(-1891, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode<IConstructor>(-1893, 3, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-1886, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-1884, 0, prod__lit_renaming__char_class___range__114_114_char_class___range__101_101_char_class___range__110_110_char_class___range__97_97_char_class___range__109_109_char_class___range__105_105_char_class___range__110_110_char_class___range__103_103_, new int[] {114,101,110,97,109,105,110,103}, null, null);
      builder.addAlternative(RascalParser.prod__default_$Renamings__lit_renaming_$layouts_LAYOUTLIST_renamings_iter_seps__$Renaming__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__default_$Renamings__lit_renaming_$layouts_LAYOUTLIST_renamings_iter_seps__$Renaming__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_(builder);
      
    }
  }
	
  protected static class $StringLiteral {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__interpolated_$StringLiteral__pre_$PreStringChars_$layouts_LAYOUTLIST_expression_$Expression_$layouts_LAYOUTLIST_tail_$StringTail_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(-1936, 4, "$StringTail", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-1934, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(-1931, 2, "$Expression", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-1929, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-1926, 0, "$PreStringChars", null, null);
      builder.addAlternative(RascalParser.prod__interpolated_$StringLiteral__pre_$PreStringChars_$layouts_LAYOUTLIST_expression_$Expression_$layouts_LAYOUTLIST_tail_$StringTail_, tmp);
	}
    protected static final void _init_prod__nonInterpolated_$StringLiteral__constant_$StringConstant_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(-1904, 0, "$StringConstant", null, null);
      builder.addAlternative(RascalParser.prod__nonInterpolated_$StringLiteral__constant_$StringConstant_, tmp);
	}
    protected static final void _init_prod__template_$StringLiteral__pre_$PreStringChars_$layouts_LAYOUTLIST_template_$StringTemplate_$layouts_LAYOUTLIST_tail_$StringTail_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(-1920, 4, "$StringTail", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-1918, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(-1915, 2, "$StringTemplate", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-1913, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-1910, 0, "$PreStringChars", null, null);
      builder.addAlternative(RascalParser.prod__template_$StringLiteral__pre_$PreStringChars_$layouts_LAYOUTLIST_template_$StringTemplate_$layouts_LAYOUTLIST_tail_$StringTail_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__interpolated_$StringLiteral__pre_$PreStringChars_$layouts_LAYOUTLIST_expression_$Expression_$layouts_LAYOUTLIST_tail_$StringTail_(builder);
      
        _init_prod__nonInterpolated_$StringLiteral__constant_$StringConstant_(builder);
      
        _init_prod__template_$StringLiteral__pre_$PreStringChars_$layouts_LAYOUTLIST_template_$StringTemplate_$layouts_LAYOUTLIST_tail_$StringTail_(builder);
      
    }
  }
	
  protected static class $Visibility {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__default_$Visibility__(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new EpsilonStackNode<IConstructor>(-1967, 0);
      builder.addAlternative(RascalParser.prod__default_$Visibility__, tmp);
	}
    protected static final void _init_prod__private_$Visibility__lit_private_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-1971, 0, prod__lit_private__char_class___range__112_112_char_class___range__114_114_char_class___range__105_105_char_class___range__118_118_char_class___range__97_97_char_class___range__116_116_char_class___range__101_101_, new int[] {112,114,105,118,97,116,101}, null, null);
      builder.addAlternative(RascalParser.prod__private_$Visibility__lit_private_, tmp);
	}
    protected static final void _init_prod__public_$Visibility__lit_public_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-1964, 0, prod__lit_public__char_class___range__112_112_char_class___range__117_117_char_class___range__98_98_char_class___range__108_108_char_class___range__105_105_char_class___range__99_99_, new int[] {112,117,98,108,105,99}, null, null);
      builder.addAlternative(RascalParser.prod__public_$Visibility__lit_public_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__default_$Visibility__(builder);
      
        _init_prod__private_$Visibility__lit_private_(builder);
      
        _init_prod__public_$Visibility__lit_public_(builder);
      
    }
  }
	
  protected static class $Visit {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__defaultStrategy_$Visit__lit_visit_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_subject_$Expression_$layouts_LAYOUTLIST_lit___41_$layouts_LAYOUTLIST_lit___123_$layouts_LAYOUTLIST_cases_iter_seps__$Case__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[13];
      
      tmp[12] = new LiteralStackNode<IConstructor>(-2038, 12, prod__lit___125__char_class___range__125_125_, new int[] {125}, null, null);
      tmp[11] = new NonTerminalStackNode<IConstructor>(-2037, 11, "$layouts_LAYOUTLIST", null, null);
      tmp[10] = new SeparatedListStackNode<IConstructor>(-2034, 10, regular__iter_seps__$Case__$layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(-2031, 0, "$Case", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(-2033, 1, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[9] = new NonTerminalStackNode<IConstructor>(-2029, 9, "$layouts_LAYOUTLIST", null, null);
      tmp[8] = new LiteralStackNode<IConstructor>(-2027, 8, prod__lit___123__char_class___range__123_123_, new int[] {123}, null, null);
      tmp[7] = new NonTerminalStackNode<IConstructor>(-2026, 7, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new LiteralStackNode<IConstructor>(-2024, 6, prod__lit___41__char_class___range__41_41_, new int[] {41}, null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(-2023, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new NonTerminalStackNode<IConstructor>(-2020, 4, "$Expression", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-2018, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(-2016, 2, prod__lit___40__char_class___range__40_40_, new int[] {40}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-2015, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-2013, 0, prod__lit_visit__char_class___range__118_118_char_class___range__105_105_char_class___range__115_115_char_class___range__105_105_char_class___range__116_116_, new int[] {118,105,115,105,116}, null, null);
      builder.addAlternative(RascalParser.prod__defaultStrategy_$Visit__lit_visit_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_subject_$Expression_$layouts_LAYOUTLIST_lit___41_$layouts_LAYOUTLIST_lit___123_$layouts_LAYOUTLIST_cases_iter_seps__$Case__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_, tmp);
	}
    protected static final void _init_prod__givenStrategy_$Visit__strategy_$Strategy_$layouts_LAYOUTLIST_lit_visit_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_subject_$Expression_$layouts_LAYOUTLIST_lit___41_$layouts_LAYOUTLIST_lit___123_$layouts_LAYOUTLIST_cases_iter_seps__$Case__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[15];
      
      tmp[14] = new LiteralStackNode<IConstructor>(-2072, 14, prod__lit___125__char_class___range__125_125_, new int[] {125}, null, null);
      tmp[13] = new NonTerminalStackNode<IConstructor>(-2071, 13, "$layouts_LAYOUTLIST", null, null);
      tmp[12] = new SeparatedListStackNode<IConstructor>(-2068, 12, regular__iter_seps__$Case__$layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(-2065, 0, "$Case", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(-2067, 1, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[11] = new NonTerminalStackNode<IConstructor>(-2063, 11, "$layouts_LAYOUTLIST", null, null);
      tmp[10] = new LiteralStackNode<IConstructor>(-2061, 10, prod__lit___123__char_class___range__123_123_, new int[] {123}, null, null);
      tmp[9] = new NonTerminalStackNode<IConstructor>(-2060, 9, "$layouts_LAYOUTLIST", null, null);
      tmp[8] = new LiteralStackNode<IConstructor>(-2058, 8, prod__lit___41__char_class___range__41_41_, new int[] {41}, null, null);
      tmp[7] = new NonTerminalStackNode<IConstructor>(-2057, 7, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new NonTerminalStackNode<IConstructor>(-2054, 6, "$Expression", null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(-2052, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode<IConstructor>(-2050, 4, prod__lit___40__char_class___range__40_40_, new int[] {40}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-2049, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(-2047, 2, prod__lit_visit__char_class___range__118_118_char_class___range__105_105_char_class___range__115_115_char_class___range__105_105_char_class___range__116_116_, new int[] {118,105,115,105,116}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-2046, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-2043, 0, "$Strategy", null, null);
      builder.addAlternative(RascalParser.prod__givenStrategy_$Visit__strategy_$Strategy_$layouts_LAYOUTLIST_lit_visit_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_subject_$Expression_$layouts_LAYOUTLIST_lit___41_$layouts_LAYOUTLIST_lit___123_$layouts_LAYOUTLIST_cases_iter_seps__$Case__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__defaultStrategy_$Visit__lit_visit_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_subject_$Expression_$layouts_LAYOUTLIST_lit___41_$layouts_LAYOUTLIST_lit___123_$layouts_LAYOUTLIST_cases_iter_seps__$Case__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_(builder);
      
        _init_prod__givenStrategy_$Visit__strategy_$Strategy_$layouts_LAYOUTLIST_lit_visit_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_subject_$Expression_$layouts_LAYOUTLIST_lit___41_$layouts_LAYOUTLIST_lit___123_$layouts_LAYOUTLIST_cases_iter_seps__$Case__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_(builder);
      
    }
  }
	
  protected static class $QualifiedName {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__default_$QualifiedName__names_iter_seps__$Name__$layouts_LAYOUTLIST_lit___58_58_$layouts_LAYOUTLIST_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new SeparatedListStackNode<IConstructor>(-2238, 0, regular__iter_seps__$Name__$layouts_LAYOUTLIST_lit___58_58_$layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(-2229, 0, "$Name", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(-2231, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode<IConstructor>(-2232, 2, prod__lit___58_58__char_class___range__58_58_char_class___range__58_58_, new int[] {58,58}, null, null), new NonTerminalStackNode<IConstructor>(-2234, 3, "$layouts_LAYOUTLIST", null, null)}, true, null, new ICompletionFilter[] {new StringFollowRestriction(new int[] {58,58})});
      builder.addAlternative(RascalParser.prod__default_$QualifiedName__names_iter_seps__$Name__$layouts_LAYOUTLIST_lit___58_58_$layouts_LAYOUTLIST_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__default_$QualifiedName__names_iter_seps__$Name__$layouts_LAYOUTLIST_lit___58_58_$layouts_LAYOUTLIST_(builder);
      
    }
  }
	
  protected static class $JustTime {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__$JustTime__lit___36_84_$TimePartNoTZ_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[2];
      
      tmp[1] = new NonTerminalStackNode<IConstructor>(-2531, 1, "$TimePartNoTZ", null, new ICompletionFilter[] {new CharFollowRestriction(new int[][]{{43,43},{45,45}})});
      tmp[0] = new LiteralStackNode<IConstructor>(-2527, 0, prod__lit___36_84__char_class___range__36_36_char_class___range__84_84_, new int[] {36,84}, null, null);
      builder.addAlternative(RascalParser.prod__$JustTime__lit___36_84_$TimePartNoTZ_, tmp);
	}
    protected static final void _init_prod__$JustTime__lit___36_84_$TimePartNoTZ_$TimeZonePart_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode<IConstructor>(-2524, 2, "$TimeZonePart", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-2522, 1, "$TimePartNoTZ", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-2520, 0, prod__lit___36_84__char_class___range__36_36_char_class___range__84_84_, new int[] {36,84}, null, null);
      builder.addAlternative(RascalParser.prod__$JustTime__lit___36_84_$TimePartNoTZ_$TimeZonePart_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__$JustTime__lit___36_84_$TimePartNoTZ_(builder);
      
        _init_prod__$JustTime__lit___36_84_$TimePartNoTZ_$TimeZonePart_(builder);
      
    }
  }
	
  protected static class $layouts_LAYOUTLIST {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__$layouts_LAYOUTLIST__iter_star__$LAYOUT_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new ListStackNode<IConstructor>(-2584, 0, regular__iter_star__$LAYOUT, new NonTerminalStackNode<IConstructor>(-2579, 0, "$LAYOUT", null, null), false, null, new ICompletionFilter[] {new CharFollowRestriction(new int[][]{{9,13},{32,32},{133,133},{160,160},{5760,5760},{6158,6158},{8192,8202},{8232,8233},{8239,8239},{8287,8287},{12288,12288}}), new StringFollowRestriction(new int[] {47,47}), new StringFollowRestriction(new int[] {47,42})});
      builder.addAlternative(RascalParser.prod__$layouts_LAYOUTLIST__iter_star__$LAYOUT_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__$layouts_LAYOUTLIST__iter_star__$LAYOUT_(builder);
      
    }
  }
	
  protected static class $LocationLiteral {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__default_$LocationLiteral__protocolPart_$ProtocolPart_$layouts_LAYOUTLIST_pathPart_$PathPart_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode<IConstructor>(-2422, 2, "$PathPart", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-2420, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-2417, 0, "$ProtocolPart", null, null);
      builder.addAlternative(RascalParser.prod__default_$LocationLiteral__protocolPart_$ProtocolPart_$layouts_LAYOUTLIST_pathPart_$PathPart_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__default_$LocationLiteral__protocolPart_$ProtocolPart_$layouts_LAYOUTLIST_pathPart_$PathPart_(builder);
      
    }
  }
	
  protected static class $Range {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__character_$Range__character_$Char_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(-2490, 0, "$Char", null, null);
      builder.addAlternative(RascalParser.prod__character_$Range__character_$Char_, tmp);
	}
    protected static final void _init_prod__fromTo_$Range__start_$Char_$layouts_LAYOUTLIST_lit___$layouts_LAYOUTLIST_end_$Char_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(-2504, 4, "$Char", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-2502, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(-2500, 2, prod__lit____char_class___range__45_45_, new int[] {45}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-2499, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-2496, 0, "$Char", null, null);
      builder.addAlternative(RascalParser.prod__fromTo_$Range__start_$Char_$layouts_LAYOUTLIST_lit___$layouts_LAYOUTLIST_end_$Char_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__character_$Range__character_$Char_(builder);
      
        _init_prod__fromTo_$Range__start_$Char_$layouts_LAYOUTLIST_lit___$layouts_LAYOUTLIST_end_$Char_(builder);
      
    }
  }
	
  protected static class $ShellCommand {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__edit_$ShellCommand__lit_edit_$layouts_LAYOUTLIST_name_$QualifiedName_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode<IConstructor>(-2351, 2, "$QualifiedName", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-2349, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-2347, 0, prod__lit_edit__char_class___range__101_101_char_class___range__100_100_char_class___range__105_105_char_class___range__116_116_, new int[] {101,100,105,116}, null, null);
      builder.addAlternative(RascalParser.prod__edit_$ShellCommand__lit_edit_$layouts_LAYOUTLIST_name_$QualifiedName_, tmp);
	}
    protected static final void _init_prod__help_$ShellCommand__lit_help_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-2356, 0, prod__lit_help__char_class___range__104_104_char_class___range__101_101_char_class___range__108_108_char_class___range__112_112_, new int[] {104,101,108,112}, null, null);
      builder.addAlternative(RascalParser.prod__help_$ShellCommand__lit_help_, tmp);
	}
    protected static final void _init_prod__history_$ShellCommand__lit_history_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-2378, 0, prod__lit_history__char_class___range__104_104_char_class___range__105_105_char_class___range__115_115_char_class___range__116_116_char_class___range__111_111_char_class___range__114_114_char_class___range__121_121_, new int[] {104,105,115,116,111,114,121}, null, null);
      builder.addAlternative(RascalParser.prod__history_$ShellCommand__lit_history_, tmp);
	}
    protected static final void _init_prod__listDeclarations_$ShellCommand__lit_declarations_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-2408, 0, prod__lit_declarations__char_class___range__100_100_char_class___range__101_101_char_class___range__99_99_char_class___range__108_108_char_class___range__97_97_char_class___range__114_114_char_class___range__97_97_char_class___range__116_116_char_class___range__105_105_char_class___range__111_111_char_class___range__110_110_char_class___range__115_115_, new int[] {100,101,99,108,97,114,97,116,105,111,110,115}, null, null);
      builder.addAlternative(RascalParser.prod__listDeclarations_$ShellCommand__lit_declarations_, tmp);
	}
    protected static final void _init_prod__listModules_$ShellCommand__lit_modules_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-2400, 0, prod__lit_modules__char_class___range__109_109_char_class___range__111_111_char_class___range__100_100_char_class___range__117_117_char_class___range__108_108_char_class___range__101_101_char_class___range__115_115_, new int[] {109,111,100,117,108,101,115}, null, null);
      builder.addAlternative(RascalParser.prod__listModules_$ShellCommand__lit_modules_, tmp);
	}
    protected static final void _init_prod__quit_$ShellCommand__lit_quit_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-2360, 0, prod__lit_quit__char_class___range__113_113_char_class___range__117_117_char_class___range__105_105_char_class___range__116_116_, new int[] {113,117,105,116}, null, null);
      builder.addAlternative(RascalParser.prod__quit_$ShellCommand__lit_quit_, tmp);
	}
    protected static final void _init_prod__setOption_$ShellCommand__lit_set_$layouts_LAYOUTLIST_name_$QualifiedName_$layouts_LAYOUTLIST_expression_$Expression_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(-2373, 4, "$Expression", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-2371, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(-2368, 2, "$QualifiedName", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-2366, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-2364, 0, prod__lit_set__char_class___range__115_115_char_class___range__101_101_char_class___range__116_116_, new int[] {115,101,116}, null, null);
      builder.addAlternative(RascalParser.prod__setOption_$ShellCommand__lit_set_$layouts_LAYOUTLIST_name_$QualifiedName_$layouts_LAYOUTLIST_expression_$Expression_, tmp);
	}
    protected static final void _init_prod__test_$ShellCommand__lit_test_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-2404, 0, prod__lit_test__char_class___range__116_116_char_class___range__101_101_char_class___range__115_115_char_class___range__116_116_, new int[] {116,101,115,116}, null, null);
      builder.addAlternative(RascalParser.prod__test_$ShellCommand__lit_test_, tmp);
	}
    protected static final void _init_prod__undeclare_$ShellCommand__lit_undeclare_$layouts_LAYOUTLIST_name_$QualifiedName_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode<IConstructor>(-2395, 2, "$QualifiedName", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-2393, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-2391, 0, prod__lit_undeclare__char_class___range__117_117_char_class___range__110_110_char_class___range__100_100_char_class___range__101_101_char_class___range__99_99_char_class___range__108_108_char_class___range__97_97_char_class___range__114_114_char_class___range__101_101_, new int[] {117,110,100,101,99,108,97,114,101}, null, null);
      builder.addAlternative(RascalParser.prod__undeclare_$ShellCommand__lit_undeclare_$layouts_LAYOUTLIST_name_$QualifiedName_, tmp);
	}
    protected static final void _init_prod__unimport_$ShellCommand__lit_unimport_$layouts_LAYOUTLIST_name_$QualifiedName_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode<IConstructor>(-2386, 2, "$QualifiedName", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-2384, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-2382, 0, prod__lit_unimport__char_class___range__117_117_char_class___range__110_110_char_class___range__105_105_char_class___range__109_109_char_class___range__112_112_char_class___range__111_111_char_class___range__114_114_char_class___range__116_116_, new int[] {117,110,105,109,112,111,114,116}, null, null);
      builder.addAlternative(RascalParser.prod__unimport_$ShellCommand__lit_unimport_$layouts_LAYOUTLIST_name_$QualifiedName_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__edit_$ShellCommand__lit_edit_$layouts_LAYOUTLIST_name_$QualifiedName_(builder);
      
        _init_prod__help_$ShellCommand__lit_help_(builder);
      
        _init_prod__history_$ShellCommand__lit_history_(builder);
      
        _init_prod__listDeclarations_$ShellCommand__lit_declarations_(builder);
      
        _init_prod__listModules_$ShellCommand__lit_modules_(builder);
      
        _init_prod__quit_$ShellCommand__lit_quit_(builder);
      
        _init_prod__setOption_$ShellCommand__lit_set_$layouts_LAYOUTLIST_name_$QualifiedName_$layouts_LAYOUTLIST_expression_$Expression_(builder);
      
        _init_prod__test_$ShellCommand__lit_test_(builder);
      
        _init_prod__undeclare_$ShellCommand__lit_undeclare_$layouts_LAYOUTLIST_name_$QualifiedName_(builder);
      
        _init_prod__unimport_$ShellCommand__lit_unimport_$layouts_LAYOUTLIST_name_$QualifiedName_(builder);
      
    }
  }
	
  protected static class $StringCharacter {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__$StringCharacter__char_class___range__0_33_range__35_38_range__40_59_range__61_61_range__63_91_range__93_16777215_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new CharStackNode<IConstructor>(-2557, 0, new int[][]{{0,33},{35,38},{40,59},{61,61},{63,91},{93,16777215}}, null, null);
      builder.addAlternative(RascalParser.prod__$StringCharacter__char_class___range__0_33_range__35_38_range__40_59_range__61_61_range__63_91_range__93_16777215_, tmp);
	}
    protected static final void _init_prod__$StringCharacter__$UnicodeEscape_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(-2554, 0, "$UnicodeEscape", null, null);
      builder.addAlternative(RascalParser.prod__$StringCharacter__$UnicodeEscape_, tmp);
	}
    protected static final void _init_prod__$StringCharacter__lit___92_char_class___range__34_34_range__39_39_range__60_60_range__62_62_range__92_92_range__98_98_range__102_102_range__110_110_range__114_114_range__116_116_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[2];
      
      tmp[1] = new CharStackNode<IConstructor>(-2561, 1, new int[][]{{34,34},{39,39},{60,60},{62,62},{92,92},{98,98},{102,102},{110,110},{114,114},{116,116}}, null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-2560, 0, prod__lit___92__char_class___range__92_92_, new int[] {92}, null, null);
      builder.addAlternative(RascalParser.prod__$StringCharacter__lit___92_char_class___range__34_34_range__39_39_range__60_60_range__62_62_range__92_92_range__98_98_range__102_102_range__110_110_range__114_114_range__116_116_, tmp);
	}
    protected static final void _init_prod__$StringCharacter__char_class___range__10_10_iter_star__char_class___range__9_9_range__32_32_range__65_65_range__160_160_range__5760_5760_range__8192_8192_range__8239_8239_range__8287_8287_range__12288_12288_char_class___range__39_39_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new CharStackNode<IConstructor>(-2567, 2, new int[][]{{39,39}}, null, null);
      tmp[1] = new ListStackNode<IConstructor>(-2566, 1, regular__iter_star__char_class___range__9_9_range__32_32_range__65_65_range__160_160_range__5760_5760_range__8192_8192_range__8239_8239_range__8287_8287_range__12288_12288, new CharStackNode<IConstructor>(-2565, 0, new int[][]{{9,9},{32,32},{65,65},{160,160},{5760,5760},{8192,8192},{8239,8239},{8287,8287},{12288,12288}}, null, null), false, null, null);
      tmp[0] = new CharStackNode<IConstructor>(-2564, 0, new int[][]{{10,10}}, null, null);
      builder.addAlternative(RascalParser.prod__$StringCharacter__char_class___range__10_10_iter_star__char_class___range__9_9_range__32_32_range__65_65_range__160_160_range__5760_5760_range__8192_8192_range__8239_8239_range__8287_8287_range__12288_12288_char_class___range__39_39_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__$StringCharacter__char_class___range__0_33_range__35_38_range__40_59_range__61_61_range__63_91_range__93_16777215_(builder);
      
        _init_prod__$StringCharacter__$UnicodeEscape_(builder);
      
        _init_prod__$StringCharacter__lit___92_char_class___range__34_34_range__39_39_range__60_60_range__62_62_range__92_92_range__98_98_range__102_102_range__110_110_range__114_114_range__116_116_(builder);
      
        _init_prod__$StringCharacter__char_class___range__10_10_iter_star__char_class___range__9_9_range__32_32_range__65_65_range__160_160_range__5760_5760_range__8192_8192_range__8239_8239_range__8287_8287_range__12288_12288_char_class___range__39_39_(builder);
      
    }
  }
	
  protected static class $StringMiddle {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__interpolated_$StringMiddle__mid_$MidStringChars_$layouts_LAYOUTLIST_expression_$Expression_$layouts_LAYOUTLIST_tail_$StringMiddle_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(-2305, 4, "$StringMiddle", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-2303, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(-2300, 2, "$Expression", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-2298, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-2295, 0, "$MidStringChars", null, null);
      builder.addAlternative(RascalParser.prod__interpolated_$StringMiddle__mid_$MidStringChars_$layouts_LAYOUTLIST_expression_$Expression_$layouts_LAYOUTLIST_tail_$StringMiddle_, tmp);
	}
    protected static final void _init_prod__mid_$StringMiddle__mid_$MidStringChars_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(-2311, 0, "$MidStringChars", null, null);
      builder.addAlternative(RascalParser.prod__mid_$StringMiddle__mid_$MidStringChars_, tmp);
	}
    protected static final void _init_prod__template_$StringMiddle__mid_$MidStringChars_$layouts_LAYOUTLIST_template_$StringTemplate_$layouts_LAYOUTLIST_tail_$StringMiddle_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(-2289, 4, "$StringMiddle", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-2287, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(-2284, 2, "$StringTemplate", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-2282, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-2279, 0, "$MidStringChars", null, null);
      builder.addAlternative(RascalParser.prod__template_$StringMiddle__mid_$MidStringChars_$layouts_LAYOUTLIST_template_$StringTemplate_$layouts_LAYOUTLIST_tail_$StringMiddle_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__interpolated_$StringMiddle__mid_$MidStringChars_$layouts_LAYOUTLIST_expression_$Expression_$layouts_LAYOUTLIST_tail_$StringMiddle_(builder);
      
        _init_prod__mid_$StringMiddle__mid_$MidStringChars_(builder);
      
        _init_prod__template_$StringMiddle__mid_$MidStringChars_$layouts_LAYOUTLIST_template_$StringTemplate_$layouts_LAYOUTLIST_tail_$StringMiddle_(builder);
      
    }
  }
	
  protected static class $TimeZonePart {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__$TimeZonePart__lit_Z_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-2319, 0, prod__lit_Z__char_class___range__90_90_, new int[] {90}, null, null);
      builder.addAlternative(RascalParser.prod__$TimeZonePart__lit_Z_, tmp);
	}
    protected static final void _init_prod__$TimeZonePart__char_class___range__43_43_range__45_45_char_class___range__48_49_char_class___range__48_57_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new CharStackNode<IConstructor>(-2324, 2, new int[][]{{48,57}}, null, null);
      tmp[1] = new CharStackNode<IConstructor>(-2323, 1, new int[][]{{48,49}}, null, null);
      tmp[0] = new CharStackNode<IConstructor>(-2322, 0, new int[][]{{43,43},{45,45}}, null, null);
      builder.addAlternative(RascalParser.prod__$TimeZonePart__char_class___range__43_43_range__45_45_char_class___range__48_49_char_class___range__48_57_, tmp);
	}
    protected static final void _init_prod__$TimeZonePart__char_class___range__43_43_range__45_45_char_class___range__48_49_char_class___range__48_57_char_class___range__48_53_char_class___range__48_57_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new CharStackNode<IConstructor>(-2331, 4, new int[][]{{48,57}}, null, null);
      tmp[3] = new CharStackNode<IConstructor>(-2330, 3, new int[][]{{48,53}}, null, null);
      tmp[2] = new CharStackNode<IConstructor>(-2329, 2, new int[][]{{48,57}}, null, null);
      tmp[1] = new CharStackNode<IConstructor>(-2328, 1, new int[][]{{48,49}}, null, null);
      tmp[0] = new CharStackNode<IConstructor>(-2327, 0, new int[][]{{43,43},{45,45}}, null, null);
      builder.addAlternative(RascalParser.prod__$TimeZonePart__char_class___range__43_43_range__45_45_char_class___range__48_49_char_class___range__48_57_char_class___range__48_53_char_class___range__48_57_, tmp);
	}
    protected static final void _init_prod__$TimeZonePart__char_class___range__43_43_range__45_45_char_class___range__48_49_char_class___range__48_57_lit___58_char_class___range__48_53_char_class___range__48_57_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[6];
      
      tmp[5] = new CharStackNode<IConstructor>(-2339, 5, new int[][]{{48,57}}, null, null);
      tmp[4] = new CharStackNode<IConstructor>(-2338, 4, new int[][]{{48,53}}, null, null);
      tmp[3] = new LiteralStackNode<IConstructor>(-2337, 3, prod__lit___58__char_class___range__58_58_, new int[] {58}, null, null);
      tmp[2] = new CharStackNode<IConstructor>(-2336, 2, new int[][]{{48,57}}, null, null);
      tmp[1] = new CharStackNode<IConstructor>(-2335, 1, new int[][]{{48,49}}, null, null);
      tmp[0] = new CharStackNode<IConstructor>(-2334, 0, new int[][]{{43,43},{45,45}}, null, null);
      builder.addAlternative(RascalParser.prod__$TimeZonePart__char_class___range__43_43_range__45_45_char_class___range__48_49_char_class___range__48_57_lit___58_char_class___range__48_53_char_class___range__48_57_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__$TimeZonePart__lit_Z_(builder);
      
        _init_prod__$TimeZonePart__char_class___range__43_43_range__45_45_char_class___range__48_49_char_class___range__48_57_(builder);
      
        _init_prod__$TimeZonePart__char_class___range__43_43_range__45_45_char_class___range__48_49_char_class___range__48_57_char_class___range__48_53_char_class___range__48_57_(builder);
      
        _init_prod__$TimeZonePart__char_class___range__43_43_range__45_45_char_class___range__48_49_char_class___range__48_57_lit___58_char_class___range__48_53_char_class___range__48_57_(builder);
      
    }
  }
	
  protected static class $URLChars {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__$URLChars__iter_star__char_class___range__0_8_range__11_12_range__14_31_range__33_59_range__61_123_range__125_16777215_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new ListStackNode<IConstructor>(-2270, 0, regular__iter_star__char_class___range__0_8_range__11_12_range__14_31_range__33_59_range__61_123_range__125_16777215, new CharStackNode<IConstructor>(-2269, 0, new int[][]{{0,8},{11,12},{14,31},{33,59},{61,123},{125,16777215}}, null, null), false, null, null);
      builder.addAlternative(RascalParser.prod__$URLChars__iter_star__char_class___range__0_8_range__11_12_range__14_31_range__33_59_range__61_123_range__125_16777215_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__$URLChars__iter_star__char_class___range__0_8_range__11_12_range__14_31_range__33_59_range__61_123_range__125_16777215_(builder);
      
    }
  }
	
  protected static class $ConcreteHole {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__one_$ConcreteHole__lit___60_$layouts_LAYOUTLIST_hole_$Pattern_$layouts_LAYOUTLIST_lit___62_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new LiteralStackNode<IConstructor>(-2728, 4, prod__lit___62__char_class___range__62_62_, new int[] {62}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-2727, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(-2724, 2, "$Pattern", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-2722, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-2720, 0, prod__lit___60__char_class___range__60_60_, new int[] {60}, null, null);
      builder.addAlternative(RascalParser.prod__one_$ConcreteHole__lit___60_$layouts_LAYOUTLIST_hole_$Pattern_$layouts_LAYOUTLIST_lit___62_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__one_$ConcreteHole__lit___60_$layouts_LAYOUTLIST_hole_$Pattern_$layouts_LAYOUTLIST_lit___62_(builder);
      
    }
  }
	
  protected static class $RealLiteral {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__$RealLiteral__iter__char_class___range__48_57_char_class___range__68_68_range__70_70_range__100_100_range__102_102_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[2];
      
      tmp[1] = new CharStackNode<IConstructor>(-2757, 1, new int[][]{{68,68},{70,70},{100,100},{102,102}}, null, null);
      tmp[0] = new ListStackNode<IConstructor>(-2756, 0, regular__iter__char_class___range__48_57, new CharStackNode<IConstructor>(-2755, 0, new int[][]{{48,57}}, null, null), true, null, null);
      builder.addAlternative(RascalParser.prod__$RealLiteral__iter__char_class___range__48_57_char_class___range__68_68_range__70_70_range__100_100_range__102_102_, tmp);
	}
    protected static final void _init_prod__$RealLiteral__lit___46_iter__char_class___range__48_57_opt__char_class___range__68_68_range__70_70_range__100_100_range__102_102_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new OptionalStackNode<IConstructor>(-2791, 2, regular__opt__char_class___range__68_68_range__70_70_range__100_100_range__102_102, new CharStackNode<IConstructor>(-2790, 0, new int[][]{{68,68},{70,70},{100,100},{102,102}}, null, null), null, null);
      tmp[1] = new ListStackNode<IConstructor>(-2789, 1, regular__iter__char_class___range__48_57, new CharStackNode<IConstructor>(-2788, 0, new int[][]{{48,57}}, null, null), true, null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-2787, 0, prod__lit___46__char_class___range__46_46_, new int[] {46}, new IEnterFilter[] {new CharPrecedeRestriction(new int[][]{{46,46}})}, null);
      builder.addAlternative(RascalParser.prod__$RealLiteral__lit___46_iter__char_class___range__48_57_opt__char_class___range__68_68_range__70_70_range__100_100_range__102_102_, tmp);
	}
    protected static final void _init_prod__$RealLiteral__iter__char_class___range__48_57_lit___46_iter_star__char_class___range__48_57_opt__char_class___range__68_68_range__70_70_range__100_100_range__102_102_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[4];
      
      tmp[3] = new OptionalStackNode<IConstructor>(-2802, 3, regular__opt__char_class___range__68_68_range__70_70_range__100_100_range__102_102, new CharStackNode<IConstructor>(-2801, 0, new int[][]{{68,68},{70,70},{100,100},{102,102}}, null, null), null, null);
      tmp[2] = new ListStackNode<IConstructor>(-2800, 2, regular__iter_star__char_class___range__48_57, new CharStackNode<IConstructor>(-2799, 0, new int[][]{{48,57}}, null, null), false, null, null);
      tmp[1] = new LiteralStackNode<IConstructor>(-2798, 1, prod__lit___46__char_class___range__46_46_, new int[] {46}, null, new ICompletionFilter[] {new StringFollowRestriction(new int[] {46})});
      tmp[0] = new ListStackNode<IConstructor>(-2795, 0, regular__iter__char_class___range__48_57, new CharStackNode<IConstructor>(-2794, 0, new int[][]{{48,57}}, null, null), true, null, null);
      builder.addAlternative(RascalParser.prod__$RealLiteral__iter__char_class___range__48_57_lit___46_iter_star__char_class___range__48_57_opt__char_class___range__68_68_range__70_70_range__100_100_range__102_102_, tmp);
	}
    protected static final void _init_prod__$RealLiteral__iter__char_class___range__48_57_char_class___range__69_69_range__101_101_opt__char_class___range__43_43_range__45_45_iter__char_class___range__48_57_opt__char_class___range__68_68_range__70_70_range__100_100_range__102_102_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new OptionalStackNode<IConstructor>(-2768, 4, regular__opt__char_class___range__68_68_range__70_70_range__100_100_range__102_102, new CharStackNode<IConstructor>(-2767, 0, new int[][]{{68,68},{70,70},{100,100},{102,102}}, null, null), null, null);
      tmp[3] = new ListStackNode<IConstructor>(-2766, 3, regular__iter__char_class___range__48_57, new CharStackNode<IConstructor>(-2765, 0, new int[][]{{48,57}}, null, null), true, null, null);
      tmp[2] = new OptionalStackNode<IConstructor>(-2764, 2, regular__opt__char_class___range__43_43_range__45_45, new CharStackNode<IConstructor>(-2763, 0, new int[][]{{43,43},{45,45}}, null, null), null, null);
      tmp[1] = new CharStackNode<IConstructor>(-2762, 1, new int[][]{{69,69},{101,101}}, null, null);
      tmp[0] = new ListStackNode<IConstructor>(-2761, 0, regular__iter__char_class___range__48_57, new CharStackNode<IConstructor>(-2760, 0, new int[][]{{48,57}}, null, null), true, null, null);
      builder.addAlternative(RascalParser.prod__$RealLiteral__iter__char_class___range__48_57_char_class___range__69_69_range__101_101_opt__char_class___range__43_43_range__45_45_iter__char_class___range__48_57_opt__char_class___range__68_68_range__70_70_range__100_100_range__102_102_, tmp);
	}
    protected static final void _init_prod__$RealLiteral__lit___46_iter__char_class___range__48_57_char_class___range__69_69_range__101_101_opt__char_class___range__43_43_range__45_45_iter__char_class___range__48_57_opt__char_class___range__68_68_range__70_70_range__100_100_range__102_102_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[6];
      
      tmp[5] = new OptionalStackNode<IConstructor>(-2816, 5, regular__opt__char_class___range__68_68_range__70_70_range__100_100_range__102_102, new CharStackNode<IConstructor>(-2815, 0, new int[][]{{68,68},{70,70},{100,100},{102,102}}, null, null), null, null);
      tmp[4] = new ListStackNode<IConstructor>(-2814, 4, regular__iter__char_class___range__48_57, new CharStackNode<IConstructor>(-2813, 0, new int[][]{{48,57}}, null, null), true, null, null);
      tmp[3] = new OptionalStackNode<IConstructor>(-2812, 3, regular__opt__char_class___range__43_43_range__45_45, new CharStackNode<IConstructor>(-2811, 0, new int[][]{{43,43},{45,45}}, null, null), null, null);
      tmp[2] = new CharStackNode<IConstructor>(-2810, 2, new int[][]{{69,69},{101,101}}, null, null);
      tmp[1] = new ListStackNode<IConstructor>(-2809, 1, regular__iter__char_class___range__48_57, new CharStackNode<IConstructor>(-2808, 0, new int[][]{{48,57}}, null, null), true, null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-2807, 0, prod__lit___46__char_class___range__46_46_, new int[] {46}, new IEnterFilter[] {new CharPrecedeRestriction(new int[][]{{46,46}})}, null);
      builder.addAlternative(RascalParser.prod__$RealLiteral__lit___46_iter__char_class___range__48_57_char_class___range__69_69_range__101_101_opt__char_class___range__43_43_range__45_45_iter__char_class___range__48_57_opt__char_class___range__68_68_range__70_70_range__100_100_range__102_102_, tmp);
	}
    protected static final void _init_prod__$RealLiteral__iter__char_class___range__48_57_lit___46_iter_star__char_class___range__48_57_char_class___range__69_69_range__101_101_opt__char_class___range__43_43_range__45_45_iter__char_class___range__48_57_opt__char_class___range__68_68_range__70_70_range__100_100_range__102_102_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[7];
      
      tmp[0] = new ListStackNode<IConstructor>(-2772, 0, regular__iter__char_class___range__48_57, new CharStackNode<IConstructor>(-2771, 0, new int[][]{{48,57}}, null, null), true, null, null);
      tmp[1] = new LiteralStackNode<IConstructor>(-2773, 1, prod__lit___46__char_class___range__46_46_, new int[] {46}, null, null);
      tmp[2] = new ListStackNode<IConstructor>(-2775, 2, regular__iter_star__char_class___range__48_57, new CharStackNode<IConstructor>(-2774, 0, new int[][]{{48,57}}, null, null), false, null, null);
      tmp[3] = new CharStackNode<IConstructor>(-2776, 3, new int[][]{{69,69},{101,101}}, null, null);
      tmp[4] = new OptionalStackNode<IConstructor>(-2778, 4, regular__opt__char_class___range__43_43_range__45_45, new CharStackNode<IConstructor>(-2777, 0, new int[][]{{43,43},{45,45}}, null, null), null, null);
      tmp[5] = new ListStackNode<IConstructor>(-2780, 5, regular__iter__char_class___range__48_57, new CharStackNode<IConstructor>(-2779, 0, new int[][]{{48,57}}, null, null), true, null, null);
      tmp[6] = new OptionalStackNode<IConstructor>(-2782, 6, regular__opt__char_class___range__68_68_range__70_70_range__100_100_range__102_102, new CharStackNode<IConstructor>(-2781, 0, new int[][]{{68,68},{70,70},{100,100},{102,102}}, null, null), null, null);
      builder.addAlternative(RascalParser.prod__$RealLiteral__iter__char_class___range__48_57_lit___46_iter_star__char_class___range__48_57_char_class___range__69_69_range__101_101_opt__char_class___range__43_43_range__45_45_iter__char_class___range__48_57_opt__char_class___range__68_68_range__70_70_range__100_100_range__102_102_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__$RealLiteral__iter__char_class___range__48_57_char_class___range__68_68_range__70_70_range__100_100_range__102_102_(builder);
      
        _init_prod__$RealLiteral__lit___46_iter__char_class___range__48_57_opt__char_class___range__68_68_range__70_70_range__100_100_range__102_102_(builder);
      
        _init_prod__$RealLiteral__iter__char_class___range__48_57_lit___46_iter_star__char_class___range__48_57_opt__char_class___range__68_68_range__70_70_range__100_100_range__102_102_(builder);
      
        _init_prod__$RealLiteral__iter__char_class___range__48_57_char_class___range__69_69_range__101_101_opt__char_class___range__43_43_range__45_45_iter__char_class___range__48_57_opt__char_class___range__68_68_range__70_70_range__100_100_range__102_102_(builder);
      
        _init_prod__$RealLiteral__lit___46_iter__char_class___range__48_57_char_class___range__69_69_range__101_101_opt__char_class___range__43_43_range__45_45_iter__char_class___range__48_57_opt__char_class___range__68_68_range__70_70_range__100_100_range__102_102_(builder);
      
        _init_prod__$RealLiteral__iter__char_class___range__48_57_lit___46_iter_star__char_class___range__48_57_char_class___range__69_69_range__101_101_opt__char_class___range__43_43_range__45_45_iter__char_class___range__48_57_opt__char_class___range__68_68_range__70_70_range__100_100_range__102_102_(builder);
      
    }
  }
	
  protected static class $DataTarget {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__empty_$DataTarget__(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new EpsilonStackNode<IConstructor>(-2748, 0);
      builder.addAlternative(RascalParser.prod__empty_$DataTarget__, tmp);
	}
    protected static final void _init_prod__labeled_$DataTarget__label_$Name_$layouts_LAYOUTLIST_lit___58_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new LiteralStackNode<IConstructor>(-2745, 2, prod__lit___58__char_class___range__58_58_, new int[] {58}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-2744, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-2741, 0, "$Name", null, null);
      builder.addAlternative(RascalParser.prod__labeled_$DataTarget__label_$Name_$layouts_LAYOUTLIST_lit___58_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__empty_$DataTarget__(builder);
      
        _init_prod__labeled_$DataTarget__label_$Name_$layouts_LAYOUTLIST_lit___58_(builder);
      
    }
  }
	
  protected static class $layouts_$default$ {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__$layouts_$default$__(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new EpsilonStackNode<IConstructor>(-2837, 0);
      builder.addAlternative(RascalParser.prod__$layouts_$default$__, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__$layouts_$default$__(builder);
      
    }
  }
	
  protected static class $PathPart {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__interpolated_$PathPart__pre_$PrePathChars_$layouts_LAYOUTLIST_expression_$Expression_$layouts_LAYOUTLIST_tail_$PathTail_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(-3044, 4, "$PathTail", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-3042, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(-3039, 2, "$Expression", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-3037, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-3034, 0, "$PrePathChars", null, null);
      builder.addAlternative(RascalParser.prod__interpolated_$PathPart__pre_$PrePathChars_$layouts_LAYOUTLIST_expression_$Expression_$layouts_LAYOUTLIST_tail_$PathTail_, tmp);
	}
    protected static final void _init_prod__nonInterpolated_$PathPart__pathChars_$PathChars_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(-3028, 0, "$PathChars", null, null);
      builder.addAlternative(RascalParser.prod__nonInterpolated_$PathPart__pathChars_$PathChars_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__interpolated_$PathPart__pre_$PrePathChars_$layouts_LAYOUTLIST_expression_$Expression_$layouts_LAYOUTLIST_tail_$PathTail_(builder);
      
        _init_prod__nonInterpolated_$PathPart__pathChars_$PathChars_(builder);
      
    }
  }
	
  protected static class $RegExp {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__$RegExp__$Backslash_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(-2983, 0, "$Backslash", null, null);
      builder.addAlternative(RascalParser.prod__$RegExp__$Backslash_, tmp);
	}
    protected static final void _init_prod__$RegExp__char_class___range__0_46_range__48_59_range__61_61_range__63_91_range__93_16777215_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new CharStackNode<IConstructor>(-2959, 0, new int[][]{{0,46},{48,59},{61,61},{63,91},{93,16777215}}, null, null);
      builder.addAlternative(RascalParser.prod__$RegExp__char_class___range__0_46_range__48_59_range__61_61_range__63_91_range__93_16777215_, tmp);
	}
    protected static final void _init_prod__$RegExp__char_class___range__92_92_char_class___range__47_47_range__60_60_range__62_62_range__92_92_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[2];
      
      tmp[1] = new CharStackNode<IConstructor>(-2979, 1, new int[][]{{47,47},{60,60},{62,62},{92,92}}, null, null);
      tmp[0] = new CharStackNode<IConstructor>(-2978, 0, new int[][]{{92,92}}, null, null);
      builder.addAlternative(RascalParser.prod__$RegExp__char_class___range__92_92_char_class___range__47_47_range__60_60_range__62_62_range__92_92_, tmp);
	}
    protected static final void _init_prod__$RegExp__lit___60_$Name_lit___62_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new LiteralStackNode<IConstructor>(-2975, 2, prod__lit___62__char_class___range__62_62_, new int[] {62}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-2974, 1, "$Name", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-2972, 0, prod__lit___60__char_class___range__60_60_, new int[] {60}, null, null);
      builder.addAlternative(RascalParser.prod__$RegExp__lit___60_$Name_lit___62_, tmp);
	}
    protected static final void _init_prod__$RegExp__lit___60_$Name_lit___58_iter_star__$NamedRegExp_lit___62_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new LiteralStackNode<IConstructor>(-2969, 4, prod__lit___62__char_class___range__62_62_, new int[] {62}, null, null);
      tmp[3] = new ListStackNode<IConstructor>(-2968, 3, regular__iter_star__$NamedRegExp, new NonTerminalStackNode<IConstructor>(-2967, 0, "$NamedRegExp", null, null), false, null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(-2965, 2, prod__lit___58__char_class___range__58_58_, new int[] {58}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-2964, 1, "$Name", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-2962, 0, prod__lit___60__char_class___range__60_60_, new int[] {60}, null, null);
      builder.addAlternative(RascalParser.prod__$RegExp__lit___60_$Name_lit___58_iter_star__$NamedRegExp_lit___62_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__$RegExp__$Backslash_(builder);
      
        _init_prod__$RegExp__char_class___range__0_46_range__48_59_range__61_61_range__63_91_range__93_16777215_(builder);
      
        _init_prod__$RegExp__char_class___range__92_92_char_class___range__47_47_range__60_60_range__62_62_range__92_92_(builder);
      
        _init_prod__$RegExp__lit___60_$Name_lit___62_(builder);
      
        _init_prod__$RegExp__lit___60_$Name_lit___58_iter_star__$NamedRegExp_lit___62_(builder);
      
    }
  }
	
  protected static class $Replacement {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__conditional_$Replacement__replacementExpression_$Expression_$layouts_LAYOUTLIST_lit_when_$layouts_LAYOUTLIST_conditions_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new SeparatedListStackNode<IConstructor>(-2871, 4, regular__iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(-2865, 0, "$Expression", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(-2867, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode<IConstructor>(-2868, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode<IConstructor>(-2870, 3, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-2863, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(-2861, 2, prod__lit_when__char_class___range__119_119_char_class___range__104_104_char_class___range__101_101_char_class___range__110_110_, new int[] {119,104,101,110}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-2860, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-2857, 0, "$Expression", null, null);
      builder.addAlternative(RascalParser.prod__conditional_$Replacement__replacementExpression_$Expression_$layouts_LAYOUTLIST_lit_when_$layouts_LAYOUTLIST_conditions_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_, tmp);
	}
    protected static final void _init_prod__unconditional_$Replacement__replacementExpression_$Expression_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(-2851, 0, "$Expression", null, null);
      builder.addAlternative(RascalParser.prod__unconditional_$Replacement__replacementExpression_$Expression_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__conditional_$Replacement__replacementExpression_$Expression_$layouts_LAYOUTLIST_lit_when_$layouts_LAYOUTLIST_conditions_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_(builder);
      
        _init_prod__unconditional_$Replacement__replacementExpression_$Expression_(builder);
      
    }
  }
	
  protected static class $Assoc {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__associative_$Assoc__lit_assoc_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-2928, 0, prod__lit_assoc__char_class___range__97_97_char_class___range__115_115_char_class___range__115_115_char_class___range__111_111_char_class___range__99_99_, new int[] {97,115,115,111,99}, null, null);
      builder.addAlternative(RascalParser.prod__associative_$Assoc__lit_assoc_, tmp);
	}
    protected static final void _init_prod__left_$Assoc__lit_left_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-2924, 0, prod__lit_left__char_class___range__108_108_char_class___range__101_101_char_class___range__102_102_char_class___range__116_116_, new int[] {108,101,102,116}, null, null);
      builder.addAlternative(RascalParser.prod__left_$Assoc__lit_left_, tmp);
	}
    protected static final void _init_prod__nonAssociative_$Assoc__lit_non_assoc_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-2920, 0, prod__lit_non_assoc__char_class___range__110_110_char_class___range__111_111_char_class___range__110_110_char_class___range__45_45_char_class___range__97_97_char_class___range__115_115_char_class___range__115_115_char_class___range__111_111_char_class___range__99_99_, new int[] {110,111,110,45,97,115,115,111,99}, null, null);
      builder.addAlternative(RascalParser.prod__nonAssociative_$Assoc__lit_non_assoc_, tmp);
	}
    protected static final void _init_prod__right_$Assoc__lit_right_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-2916, 0, prod__lit_right__char_class___range__114_114_char_class___range__105_105_char_class___range__103_103_char_class___range__104_104_char_class___range__116_116_, new int[] {114,105,103,104,116}, null, null);
      builder.addAlternative(RascalParser.prod__right_$Assoc__lit_right_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__associative_$Assoc__lit_assoc_(builder);
      
        _init_prod__left_$Assoc__lit_left_(builder);
      
        _init_prod__nonAssociative_$Assoc__lit_non_assoc_(builder);
      
        _init_prod__right_$Assoc__lit_right_(builder);
      
    }
  }
	
  protected static class $DecimalIntegerLiteral {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__$DecimalIntegerLiteral__lit_0_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-3386, 0, prod__lit_0__char_class___range__48_48_, new int[] {48}, null, new ICompletionFilter[] {new CharFollowRestriction(new int[][]{{48,57},{65,90},{95,95},{97,122}})});
      builder.addAlternative(RascalParser.prod__$DecimalIntegerLiteral__lit_0_, tmp);
	}
    protected static final void _init_prod__$DecimalIntegerLiteral__char_class___range__49_57_iter_star__char_class___range__48_57_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[2];
      
      tmp[1] = new ListStackNode<IConstructor>(-3381, 1, regular__iter_star__char_class___range__48_57, new CharStackNode<IConstructor>(-3378, 0, new int[][]{{48,57}}, null, null), false, null, new ICompletionFilter[] {new CharFollowRestriction(new int[][]{{48,57},{65,90},{95,95},{97,122}})});
      tmp[0] = new CharStackNode<IConstructor>(-3377, 0, new int[][]{{49,57}}, null, null);
      builder.addAlternative(RascalParser.prod__$DecimalIntegerLiteral__char_class___range__49_57_iter_star__char_class___range__48_57_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__$DecimalIntegerLiteral__lit_0_(builder);
      
        _init_prod__$DecimalIntegerLiteral__char_class___range__49_57_iter_star__char_class___range__48_57_(builder);
      
    }
  }
	
  protected static class $MidStringChars {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__$MidStringChars__char_class___range__62_62_iter_star__$StringCharacter_char_class___range__60_60__tag__category___67_111_110_115_116_97_110_116(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new CharStackNode<IConstructor>(-2952, 2, new int[][]{{60,60}}, null, null);
      tmp[1] = new ListStackNode<IConstructor>(-2951, 1, regular__iter_star__$StringCharacter, new NonTerminalStackNode<IConstructor>(-2950, 0, "$StringCharacter", null, null), false, null, null);
      tmp[0] = new CharStackNode<IConstructor>(-2948, 0, new int[][]{{62,62}}, null, null);
      builder.addAlternative(RascalParser.prod__$MidStringChars__char_class___range__62_62_iter_star__$StringCharacter_char_class___range__60_60__tag__category___67_111_110_115_116_97_110_116, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__$MidStringChars__char_class___range__62_62_iter_star__$StringCharacter_char_class___range__60_60__tag__category___67_111_110_115_116_97_110_116(builder);
      
    }
  }
	
  protected static class $RegExpModifier {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__$RegExpModifier__iter_star__char_class___range__100_100_range__105_105_range__109_109_range__115_115_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new ListStackNode<IConstructor>(-2941, 0, regular__iter_star__char_class___range__100_100_range__105_105_range__109_109_range__115_115, new CharStackNode<IConstructor>(-2940, 0, new int[][]{{100,100},{105,105},{109,109},{115,115}}, null, null), false, null, null);
      builder.addAlternative(RascalParser.prod__$RegExpModifier__iter_star__char_class___range__100_100_range__105_105_range__109_109_range__115_115_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__$RegExpModifier__iter_star__char_class___range__100_100_range__105_105_range__109_109_range__115_115_(builder);
      
    }
  }
	
  protected static class $StringTemplate {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__doWhile_$StringTemplate__lit_do_$layouts_LAYOUTLIST_lit___123_$layouts_LAYOUTLIST_preStats_iter_star_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_body_$StringMiddle_$layouts_LAYOUTLIST_postStats_iter_star_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_$layouts_LAYOUTLIST_lit_while_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_condition_$Expression_$layouts_LAYOUTLIST_lit___41_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[19];
      
      tmp[18] = new LiteralStackNode<IConstructor>(-3154, 18, prod__lit___41__char_class___range__41_41_, new int[] {41}, null, null);
      tmp[17] = new NonTerminalStackNode<IConstructor>(-3153, 17, "$layouts_LAYOUTLIST", null, null);
      tmp[16] = new NonTerminalStackNode<IConstructor>(-3150, 16, "$Expression", null, null);
      tmp[15] = new NonTerminalStackNode<IConstructor>(-3148, 15, "$layouts_LAYOUTLIST", null, null);
      tmp[14] = new LiteralStackNode<IConstructor>(-3146, 14, prod__lit___40__char_class___range__40_40_, new int[] {40}, null, null);
      tmp[13] = new NonTerminalStackNode<IConstructor>(-3145, 13, "$layouts_LAYOUTLIST", null, null);
      tmp[12] = new LiteralStackNode<IConstructor>(-3143, 12, prod__lit_while__char_class___range__119_119_char_class___range__104_104_char_class___range__105_105_char_class___range__108_108_char_class___range__101_101_, new int[] {119,104,105,108,101}, null, null);
      tmp[11] = new NonTerminalStackNode<IConstructor>(-3142, 11, "$layouts_LAYOUTLIST", null, null);
      tmp[10] = new LiteralStackNode<IConstructor>(-3140, 10, prod__lit___125__char_class___range__125_125_, new int[] {125}, null, null);
      tmp[9] = new NonTerminalStackNode<IConstructor>(-3139, 9, "$layouts_LAYOUTLIST", null, null);
      tmp[8] = new SeparatedListStackNode<IConstructor>(-3136, 8, regular__iter_star_seps__$Statement__$layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(-3133, 0, "$Statement", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(-3135, 1, "$layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[7] = new NonTerminalStackNode<IConstructor>(-3131, 7, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new NonTerminalStackNode<IConstructor>(-3128, 6, "$StringMiddle", null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(-3126, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new SeparatedListStackNode<IConstructor>(-3123, 4, regular__iter_star_seps__$Statement__$layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(-3120, 0, "$Statement", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(-3122, 1, "$layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-3118, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(-3116, 2, prod__lit___123__char_class___range__123_123_, new int[] {123}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-3115, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-3113, 0, prod__lit_do__char_class___range__100_100_char_class___range__111_111_, new int[] {100,111}, null, null);
      builder.addAlternative(RascalParser.prod__doWhile_$StringTemplate__lit_do_$layouts_LAYOUTLIST_lit___123_$layouts_LAYOUTLIST_preStats_iter_star_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_body_$StringMiddle_$layouts_LAYOUTLIST_postStats_iter_star_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_$layouts_LAYOUTLIST_lit_while_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_condition_$Expression_$layouts_LAYOUTLIST_lit___41_, tmp);
	}
    protected static final void _init_prod__for_$StringTemplate__lit_for_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_generators_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_$layouts_LAYOUTLIST_lit___123_$layouts_LAYOUTLIST_preStats_iter_star_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_body_$StringMiddle_$layouts_LAYOUTLIST_postStats_iter_star_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[17];
      
      tmp[16] = new LiteralStackNode<IConstructor>(-3244, 16, prod__lit___125__char_class___range__125_125_, new int[] {125}, null, null);
      tmp[15] = new NonTerminalStackNode<IConstructor>(-3243, 15, "$layouts_LAYOUTLIST", null, null);
      tmp[14] = new SeparatedListStackNode<IConstructor>(-3240, 14, regular__iter_star_seps__$Statement__$layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(-3237, 0, "$Statement", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(-3239, 1, "$layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[13] = new NonTerminalStackNode<IConstructor>(-3235, 13, "$layouts_LAYOUTLIST", null, null);
      tmp[12] = new NonTerminalStackNode<IConstructor>(-3232, 12, "$StringMiddle", null, null);
      tmp[11] = new NonTerminalStackNode<IConstructor>(-3230, 11, "$layouts_LAYOUTLIST", null, null);
      tmp[10] = new SeparatedListStackNode<IConstructor>(-3227, 10, regular__iter_star_seps__$Statement__$layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(-3224, 0, "$Statement", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(-3226, 1, "$layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[9] = new NonTerminalStackNode<IConstructor>(-3222, 9, "$layouts_LAYOUTLIST", null, null);
      tmp[8] = new LiteralStackNode<IConstructor>(-3220, 8, prod__lit___123__char_class___range__123_123_, new int[] {123}, null, null);
      tmp[7] = new NonTerminalStackNode<IConstructor>(-3219, 7, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new LiteralStackNode<IConstructor>(-3217, 6, prod__lit___41__char_class___range__41_41_, new int[] {41}, null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(-3216, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new SeparatedListStackNode<IConstructor>(-3213, 4, regular__iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(-3207, 0, "$Expression", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(-3209, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode<IConstructor>(-3210, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode<IConstructor>(-3212, 3, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-3205, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(-3203, 2, prod__lit___40__char_class___range__40_40_, new int[] {40}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-3202, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-3200, 0, prod__lit_for__char_class___range__102_102_char_class___range__111_111_char_class___range__114_114_, new int[] {102,111,114}, null, null);
      builder.addAlternative(RascalParser.prod__for_$StringTemplate__lit_for_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_generators_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_$layouts_LAYOUTLIST_lit___123_$layouts_LAYOUTLIST_preStats_iter_star_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_body_$StringMiddle_$layouts_LAYOUTLIST_postStats_iter_star_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_, tmp);
	}
    protected static final void _init_prod__ifThen_$StringTemplate__lit_if_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_conditions_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_$layouts_LAYOUTLIST_lit___123_$layouts_LAYOUTLIST_preStats_iter_star_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_body_$StringMiddle_$layouts_LAYOUTLIST_postStats_iter_star_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[17];
      
      tmp[16] = new LiteralStackNode<IConstructor>(-3370, 16, prod__lit___125__char_class___range__125_125_, new int[] {125}, null, null);
      tmp[15] = new NonTerminalStackNode<IConstructor>(-3369, 15, "$layouts_LAYOUTLIST", null, null);
      tmp[14] = new SeparatedListStackNode<IConstructor>(-3366, 14, regular__iter_star_seps__$Statement__$layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(-3363, 0, "$Statement", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(-3365, 1, "$layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[13] = new NonTerminalStackNode<IConstructor>(-3361, 13, "$layouts_LAYOUTLIST", null, null);
      tmp[12] = new NonTerminalStackNode<IConstructor>(-3358, 12, "$StringMiddle", null, null);
      tmp[11] = new NonTerminalStackNode<IConstructor>(-3356, 11, "$layouts_LAYOUTLIST", null, null);
      tmp[10] = new SeparatedListStackNode<IConstructor>(-3353, 10, regular__iter_star_seps__$Statement__$layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(-3350, 0, "$Statement", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(-3352, 1, "$layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[9] = new NonTerminalStackNode<IConstructor>(-3348, 9, "$layouts_LAYOUTLIST", null, null);
      tmp[8] = new LiteralStackNode<IConstructor>(-3346, 8, prod__lit___123__char_class___range__123_123_, new int[] {123}, null, null);
      tmp[7] = new NonTerminalStackNode<IConstructor>(-3345, 7, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new LiteralStackNode<IConstructor>(-3343, 6, prod__lit___41__char_class___range__41_41_, new int[] {41}, null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(-3342, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new SeparatedListStackNode<IConstructor>(-3339, 4, regular__iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(-3333, 0, "$Expression", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(-3335, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode<IConstructor>(-3336, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode<IConstructor>(-3338, 3, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-3331, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(-3329, 2, prod__lit___40__char_class___range__40_40_, new int[] {40}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-3328, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-3326, 0, prod__lit_if__char_class___range__105_105_char_class___range__102_102_, new int[] {105,102}, null, null);
      builder.addAlternative(RascalParser.prod__ifThen_$StringTemplate__lit_if_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_conditions_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_$layouts_LAYOUTLIST_lit___123_$layouts_LAYOUTLIST_preStats_iter_star_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_body_$StringMiddle_$layouts_LAYOUTLIST_postStats_iter_star_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_, tmp);
	}
    protected static final void _init_prod__ifThenElse_$StringTemplate__lit_if_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_conditions_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_$layouts_LAYOUTLIST_lit___123_$layouts_LAYOUTLIST_preStatsThen_iter_star_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_thenString_$StringMiddle_$layouts_LAYOUTLIST_postStatsThen_iter_star_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_$layouts_LAYOUTLIST_lit_else_$layouts_LAYOUTLIST_lit___123_$layouts_LAYOUTLIST_preStatsElse_iter_star_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_elseString_$StringMiddle_$layouts_LAYOUTLIST_postStatsElse_iter_star_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[29];
      
      tmp[28] = new LiteralStackNode<IConstructor>(-3322, 28, prod__lit___125__char_class___range__125_125_, new int[] {125}, null, null);
      tmp[27] = new NonTerminalStackNode<IConstructor>(-3321, 27, "$layouts_LAYOUTLIST", null, null);
      tmp[26] = new SeparatedListStackNode<IConstructor>(-3318, 26, regular__iter_star_seps__$Statement__$layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(-3315, 0, "$Statement", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(-3317, 1, "$layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[25] = new NonTerminalStackNode<IConstructor>(-3313, 25, "$layouts_LAYOUTLIST", null, null);
      tmp[24] = new NonTerminalStackNode<IConstructor>(-3310, 24, "$StringMiddle", null, null);
      tmp[23] = new NonTerminalStackNode<IConstructor>(-3308, 23, "$layouts_LAYOUTLIST", null, null);
      tmp[22] = new SeparatedListStackNode<IConstructor>(-3305, 22, regular__iter_star_seps__$Statement__$layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(-3302, 0, "$Statement", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(-3304, 1, "$layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[21] = new NonTerminalStackNode<IConstructor>(-3300, 21, "$layouts_LAYOUTLIST", null, null);
      tmp[20] = new LiteralStackNode<IConstructor>(-3298, 20, prod__lit___123__char_class___range__123_123_, new int[] {123}, null, null);
      tmp[19] = new NonTerminalStackNode<IConstructor>(-3297, 19, "$layouts_LAYOUTLIST", null, null);
      tmp[18] = new LiteralStackNode<IConstructor>(-3295, 18, prod__lit_else__char_class___range__101_101_char_class___range__108_108_char_class___range__115_115_char_class___range__101_101_, new int[] {101,108,115,101}, null, null);
      tmp[17] = new NonTerminalStackNode<IConstructor>(-3294, 17, "$layouts_LAYOUTLIST", null, null);
      tmp[16] = new LiteralStackNode<IConstructor>(-3292, 16, prod__lit___125__char_class___range__125_125_, new int[] {125}, null, null);
      tmp[15] = new NonTerminalStackNode<IConstructor>(-3291, 15, "$layouts_LAYOUTLIST", null, null);
      tmp[14] = new SeparatedListStackNode<IConstructor>(-3288, 14, regular__iter_star_seps__$Statement__$layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(-3285, 0, "$Statement", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(-3287, 1, "$layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[13] = new NonTerminalStackNode<IConstructor>(-3283, 13, "$layouts_LAYOUTLIST", null, null);
      tmp[12] = new NonTerminalStackNode<IConstructor>(-3280, 12, "$StringMiddle", null, null);
      tmp[11] = new NonTerminalStackNode<IConstructor>(-3278, 11, "$layouts_LAYOUTLIST", null, null);
      tmp[10] = new SeparatedListStackNode<IConstructor>(-3275, 10, regular__iter_star_seps__$Statement__$layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(-3272, 0, "$Statement", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(-3274, 1, "$layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[9] = new NonTerminalStackNode<IConstructor>(-3270, 9, "$layouts_LAYOUTLIST", null, null);
      tmp[8] = new LiteralStackNode<IConstructor>(-3268, 8, prod__lit___123__char_class___range__123_123_, new int[] {123}, null, null);
      tmp[7] = new NonTerminalStackNode<IConstructor>(-3267, 7, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new LiteralStackNode<IConstructor>(-3265, 6, prod__lit___41__char_class___range__41_41_, new int[] {41}, null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(-3264, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new SeparatedListStackNode<IConstructor>(-3261, 4, regular__iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(-3255, 0, "$Expression", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(-3257, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode<IConstructor>(-3258, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode<IConstructor>(-3260, 3, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-3253, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(-3251, 2, prod__lit___40__char_class___range__40_40_, new int[] {40}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-3250, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-3248, 0, prod__lit_if__char_class___range__105_105_char_class___range__102_102_, new int[] {105,102}, null, null);
      builder.addAlternative(RascalParser.prod__ifThenElse_$StringTemplate__lit_if_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_conditions_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_$layouts_LAYOUTLIST_lit___123_$layouts_LAYOUTLIST_preStatsThen_iter_star_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_thenString_$StringMiddle_$layouts_LAYOUTLIST_postStatsThen_iter_star_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_$layouts_LAYOUTLIST_lit_else_$layouts_LAYOUTLIST_lit___123_$layouts_LAYOUTLIST_preStatsElse_iter_star_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_elseString_$StringMiddle_$layouts_LAYOUTLIST_postStatsElse_iter_star_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_, tmp);
	}
    protected static final void _init_prod__while_$StringTemplate__lit_while_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_condition_$Expression_$layouts_LAYOUTLIST_lit___41_$layouts_LAYOUTLIST_lit___123_$layouts_LAYOUTLIST_preStats_iter_star_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_body_$StringMiddle_$layouts_LAYOUTLIST_postStats_iter_star_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[17];
      
      tmp[16] = new LiteralStackNode<IConstructor>(-3196, 16, prod__lit___125__char_class___range__125_125_, new int[] {125}, null, null);
      tmp[15] = new NonTerminalStackNode<IConstructor>(-3195, 15, "$layouts_LAYOUTLIST", null, null);
      tmp[14] = new SeparatedListStackNode<IConstructor>(-3192, 14, regular__iter_star_seps__$Statement__$layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(-3189, 0, "$Statement", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(-3191, 1, "$layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[13] = new NonTerminalStackNode<IConstructor>(-3187, 13, "$layouts_LAYOUTLIST", null, null);
      tmp[12] = new NonTerminalStackNode<IConstructor>(-3184, 12, "$StringMiddle", null, null);
      tmp[11] = new NonTerminalStackNode<IConstructor>(-3182, 11, "$layouts_LAYOUTLIST", null, null);
      tmp[10] = new SeparatedListStackNode<IConstructor>(-3179, 10, regular__iter_star_seps__$Statement__$layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(-3176, 0, "$Statement", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(-3178, 1, "$layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[9] = new NonTerminalStackNode<IConstructor>(-3174, 9, "$layouts_LAYOUTLIST", null, null);
      tmp[8] = new LiteralStackNode<IConstructor>(-3172, 8, prod__lit___123__char_class___range__123_123_, new int[] {123}, null, null);
      tmp[7] = new NonTerminalStackNode<IConstructor>(-3171, 7, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new LiteralStackNode<IConstructor>(-3169, 6, prod__lit___41__char_class___range__41_41_, new int[] {41}, null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(-3168, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new NonTerminalStackNode<IConstructor>(-3165, 4, "$Expression", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-3163, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(-3161, 2, prod__lit___40__char_class___range__40_40_, new int[] {40}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-3160, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-3158, 0, prod__lit_while__char_class___range__119_119_char_class___range__104_104_char_class___range__105_105_char_class___range__108_108_char_class___range__101_101_, new int[] {119,104,105,108,101}, null, null);
      builder.addAlternative(RascalParser.prod__while_$StringTemplate__lit_while_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_condition_$Expression_$layouts_LAYOUTLIST_lit___41_$layouts_LAYOUTLIST_lit___123_$layouts_LAYOUTLIST_preStats_iter_star_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_body_$StringMiddle_$layouts_LAYOUTLIST_postStats_iter_star_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__doWhile_$StringTemplate__lit_do_$layouts_LAYOUTLIST_lit___123_$layouts_LAYOUTLIST_preStats_iter_star_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_body_$StringMiddle_$layouts_LAYOUTLIST_postStats_iter_star_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_$layouts_LAYOUTLIST_lit_while_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_condition_$Expression_$layouts_LAYOUTLIST_lit___41_(builder);
      
        _init_prod__for_$StringTemplate__lit_for_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_generators_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_$layouts_LAYOUTLIST_lit___123_$layouts_LAYOUTLIST_preStats_iter_star_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_body_$StringMiddle_$layouts_LAYOUTLIST_postStats_iter_star_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_(builder);
      
        _init_prod__ifThen_$StringTemplate__lit_if_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_conditions_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_$layouts_LAYOUTLIST_lit___123_$layouts_LAYOUTLIST_preStats_iter_star_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_body_$StringMiddle_$layouts_LAYOUTLIST_postStats_iter_star_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_(builder);
      
        _init_prod__ifThenElse_$StringTemplate__lit_if_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_conditions_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_$layouts_LAYOUTLIST_lit___123_$layouts_LAYOUTLIST_preStatsThen_iter_star_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_thenString_$StringMiddle_$layouts_LAYOUTLIST_postStatsThen_iter_star_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_$layouts_LAYOUTLIST_lit_else_$layouts_LAYOUTLIST_lit___123_$layouts_LAYOUTLIST_preStatsElse_iter_star_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_elseString_$StringMiddle_$layouts_LAYOUTLIST_postStatsElse_iter_star_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_(builder);
      
        _init_prod__while_$StringTemplate__lit_while_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_condition_$Expression_$layouts_LAYOUTLIST_lit___41_$layouts_LAYOUTLIST_lit___123_$layouts_LAYOUTLIST_preStats_iter_star_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_body_$StringMiddle_$layouts_LAYOUTLIST_postStats_iter_star_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_(builder);
      
    }
  }
	
  protected static class $Body {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__toplevels_$Body__toplevels_iter_star_seps__$Toplevel__$layouts_LAYOUTLIST_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new SeparatedListStackNode<IConstructor>(-3413, 0, regular__iter_star_seps__$Toplevel__$layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(-3410, 0, "$Toplevel", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(-3412, 1, "$layouts_LAYOUTLIST", null, null)}, false, null, null);
      builder.addAlternative(RascalParser.prod__toplevels_$Body__toplevels_iter_star_seps__$Toplevel__$layouts_LAYOUTLIST_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__toplevels_$Body__toplevels_iter_star_seps__$Toplevel__$layouts_LAYOUTLIST_(builder);
      
    }
  }
	
  protected static class $FunctionBody {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__default_$FunctionBody__lit___123_$layouts_LAYOUTLIST_statements_iter_star_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new LiteralStackNode<IConstructor>(-3540, 4, prod__lit___125__char_class___range__125_125_, new int[] {125}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-3539, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new SeparatedListStackNode<IConstructor>(-3536, 2, regular__iter_star_seps__$Statement__$layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(-3533, 0, "$Statement", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(-3535, 1, "$layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-3531, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-3529, 0, prod__lit___123__char_class___range__123_123_, new int[] {123}, null, null);
      builder.addAlternative(RascalParser.prod__default_$FunctionBody__lit___123_$layouts_LAYOUTLIST_statements_iter_star_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__default_$FunctionBody__lit___123_$layouts_LAYOUTLIST_statements_iter_star_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_(builder);
      
    }
  }
	
  protected static class $Import {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__default_$Import__lit_import_$layouts_LAYOUTLIST_module_$ImportedModule_$layouts_LAYOUTLIST_lit___59_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new LiteralStackNode<IConstructor>(-3482, 4, prod__lit___59__char_class___range__59_59_, new int[] {59}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-3481, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(-3478, 2, "$ImportedModule", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-3476, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-3474, 0, prod__lit_import__char_class___range__105_105_char_class___range__109_109_char_class___range__112_112_char_class___range__111_111_char_class___range__114_114_char_class___range__116_116_, new int[] {105,109,112,111,114,116}, null, null);
      builder.addAlternative(RascalParser.prod__default_$Import__lit_import_$layouts_LAYOUTLIST_module_$ImportedModule_$layouts_LAYOUTLIST_lit___59_, tmp);
	}
    protected static final void _init_prod__extend_$Import__lit_extend_$layouts_LAYOUTLIST_module_$ImportedModule_$layouts_LAYOUTLIST_lit___59_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new LiteralStackNode<IConstructor>(-3494, 4, prod__lit___59__char_class___range__59_59_, new int[] {59}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-3493, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(-3490, 2, "$ImportedModule", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-3488, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-3486, 0, prod__lit_extend__char_class___range__101_101_char_class___range__120_120_char_class___range__116_116_char_class___range__101_101_char_class___range__110_110_char_class___range__100_100_, new int[] {101,120,116,101,110,100}, null, null);
      builder.addAlternative(RascalParser.prod__extend_$Import__lit_extend_$layouts_LAYOUTLIST_module_$ImportedModule_$layouts_LAYOUTLIST_lit___59_, tmp);
	}
    protected static final void _init_prod__external_$Import__lit_import_$layouts_LAYOUTLIST_name_$QualifiedName_$layouts_LAYOUTLIST_lit___61_$layouts_LAYOUTLIST_at_$LocationLiteral_$layouts_LAYOUTLIST_lit___59_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[9];
      
      tmp[8] = new LiteralStackNode<IConstructor>(-3514, 8, prod__lit___59__char_class___range__59_59_, new int[] {59}, null, null);
      tmp[7] = new NonTerminalStackNode<IConstructor>(-3513, 7, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new NonTerminalStackNode<IConstructor>(-3510, 6, "$LocationLiteral", null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(-3508, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode<IConstructor>(-3506, 4, prod__lit___61__char_class___range__61_61_, new int[] {61}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-3505, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(-3502, 2, "$QualifiedName", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-3500, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-3498, 0, prod__lit_import__char_class___range__105_105_char_class___range__109_109_char_class___range__112_112_char_class___range__111_111_char_class___range__114_114_char_class___range__116_116_, new int[] {105,109,112,111,114,116}, null, null);
      builder.addAlternative(RascalParser.prod__external_$Import__lit_import_$layouts_LAYOUTLIST_name_$QualifiedName_$layouts_LAYOUTLIST_lit___61_$layouts_LAYOUTLIST_at_$LocationLiteral_$layouts_LAYOUTLIST_lit___59_, tmp);
	}
    protected static final void _init_prod__syntax_$Import__syntax_$SyntaxDefinition_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(-3469, 0, "$SyntaxDefinition", null, null);
      builder.addAlternative(RascalParser.prod__syntax_$Import__syntax_$SyntaxDefinition_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__default_$Import__lit_import_$layouts_LAYOUTLIST_module_$ImportedModule_$layouts_LAYOUTLIST_lit___59_(builder);
      
        _init_prod__extend_$Import__lit_extend_$layouts_LAYOUTLIST_module_$ImportedModule_$layouts_LAYOUTLIST_lit___59_(builder);
      
        _init_prod__external_$Import__lit_import_$layouts_LAYOUTLIST_name_$QualifiedName_$layouts_LAYOUTLIST_lit___61_$layouts_LAYOUTLIST_at_$LocationLiteral_$layouts_LAYOUTLIST_lit___59_(builder);
      
        _init_prod__syntax_$Import__syntax_$SyntaxDefinition_(builder);
      
    }
  }
	
  protected static class $IntegerLiteral {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__decimalIntegerLiteral_$IntegerLiteral__decimal_$DecimalIntegerLiteral_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(-3581, 0, "$DecimalIntegerLiteral", null, null);
      builder.addAlternative(RascalParser.prod__decimalIntegerLiteral_$IntegerLiteral__decimal_$DecimalIntegerLiteral_, tmp);
	}
    protected static final void _init_prod__hexIntegerLiteral_$IntegerLiteral__hex_$HexIntegerLiteral_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(-3593, 0, "$HexIntegerLiteral", null, null);
      builder.addAlternative(RascalParser.prod__hexIntegerLiteral_$IntegerLiteral__hex_$HexIntegerLiteral_, tmp);
	}
    protected static final void _init_prod__octalIntegerLiteral_$IntegerLiteral__octal_$OctalIntegerLiteral_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(-3587, 0, "$OctalIntegerLiteral", null, null);
      builder.addAlternative(RascalParser.prod__octalIntegerLiteral_$IntegerLiteral__octal_$OctalIntegerLiteral_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__decimalIntegerLiteral_$IntegerLiteral__decimal_$DecimalIntegerLiteral_(builder);
      
        _init_prod__hexIntegerLiteral_$IntegerLiteral__hex_$HexIntegerLiteral_(builder);
      
        _init_prod__octalIntegerLiteral_$IntegerLiteral__octal_$OctalIntegerLiteral_(builder);
      
    }
  }
	
  protected static class $Kind {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__alias_$Kind__lit_alias_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-3760, 0, prod__lit_alias__char_class___range__97_97_char_class___range__108_108_char_class___range__105_105_char_class___range__97_97_char_class___range__115_115_, new int[] {97,108,105,97,115}, null, null);
      builder.addAlternative(RascalParser.prod__alias_$Kind__lit_alias_, tmp);
	}
    protected static final void _init_prod__all_$Kind__lit_all_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-3728, 0, prod__lit_all__char_class___range__97_97_char_class___range__108_108_char_class___range__108_108_, new int[] {97,108,108}, null, null);
      builder.addAlternative(RascalParser.prod__all_$Kind__lit_all_, tmp);
	}
    protected static final void _init_prod__anno_$Kind__lit_anno_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-3740, 0, prod__lit_anno__char_class___range__97_97_char_class___range__110_110_char_class___range__110_110_char_class___range__111_111_, new int[] {97,110,110,111}, null, null);
      builder.addAlternative(RascalParser.prod__anno_$Kind__lit_anno_, tmp);
	}
    protected static final void _init_prod__data_$Kind__lit_data_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-3744, 0, prod__lit_data__char_class___range__100_100_char_class___range__97_97_char_class___range__116_116_char_class___range__97_97_, new int[] {100,97,116,97}, null, null);
      builder.addAlternative(RascalParser.prod__data_$Kind__lit_data_, tmp);
	}
    protected static final void _init_prod__function_$Kind__lit_function_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-3752, 0, prod__lit_function__char_class___range__102_102_char_class___range__117_117_char_class___range__110_110_char_class___range__99_99_char_class___range__116_116_char_class___range__105_105_char_class___range__111_111_char_class___range__110_110_, new int[] {102,117,110,99,116,105,111,110}, null, null);
      builder.addAlternative(RascalParser.prod__function_$Kind__lit_function_, tmp);
	}
    protected static final void _init_prod__module_$Kind__lit_module_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-3732, 0, prod__lit_module__char_class___range__109_109_char_class___range__111_111_char_class___range__100_100_char_class___range__117_117_char_class___range__108_108_char_class___range__101_101_, new int[] {109,111,100,117,108,101}, null, null);
      builder.addAlternative(RascalParser.prod__module_$Kind__lit_module_, tmp);
	}
    protected static final void _init_prod__tag_$Kind__lit_tag_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-3748, 0, prod__lit_tag__char_class___range__116_116_char_class___range__97_97_char_class___range__103_103_, new int[] {116,97,103}, null, null);
      builder.addAlternative(RascalParser.prod__tag_$Kind__lit_tag_, tmp);
	}
    protected static final void _init_prod__variable_$Kind__lit_variable_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-3736, 0, prod__lit_variable__char_class___range__118_118_char_class___range__97_97_char_class___range__114_114_char_class___range__105_105_char_class___range__97_97_char_class___range__98_98_char_class___range__108_108_char_class___range__101_101_, new int[] {118,97,114,105,97,98,108,101}, null, null);
      builder.addAlternative(RascalParser.prod__variable_$Kind__lit_variable_, tmp);
	}
    protected static final void _init_prod__view_$Kind__lit_view_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-3756, 0, prod__lit_view__char_class___range__118_118_char_class___range__105_105_char_class___range__101_101_char_class___range__119_119_, new int[] {118,105,101,119}, null, null);
      builder.addAlternative(RascalParser.prod__view_$Kind__lit_view_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__alias_$Kind__lit_alias_(builder);
      
        _init_prod__all_$Kind__lit_all_(builder);
      
        _init_prod__anno_$Kind__lit_anno_(builder);
      
        _init_prod__data_$Kind__lit_data_(builder);
      
        _init_prod__function_$Kind__lit_function_(builder);
      
        _init_prod__module_$Kind__lit_module_(builder);
      
        _init_prod__tag_$Kind__lit_tag_(builder);
      
        _init_prod__variable_$Kind__lit_variable_(builder);
      
        _init_prod__view_$Kind__lit_view_(builder);
      
    }
  }
	
  protected static class $LAYOUT {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__$LAYOUT__char_class___range__9_13_range__32_32_range__133_133_range__160_160_range__5760_5760_range__6158_6158_range__8192_8202_range__8232_8233_range__8239_8239_range__8287_8287_range__12288_12288_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new CharStackNode<IConstructor>(-3425, 0, new int[][]{{9,13},{32,32},{133,133},{160,160},{5760,5760},{6158,6158},{8192,8202},{8232,8233},{8239,8239},{8287,8287},{12288,12288}}, null, null);
      builder.addAlternative(RascalParser.prod__$LAYOUT__char_class___range__9_13_range__32_32_range__133_133_range__160_160_range__5760_5760_range__6158_6158_range__8192_8202_range__8232_8233_range__8239_8239_range__8287_8287_range__12288_12288_, tmp);
	}
    protected static final void _init_prod__$LAYOUT__$Comment_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(-3422, 0, "$Comment", null, null);
      builder.addAlternative(RascalParser.prod__$LAYOUT__$Comment_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__$LAYOUT__char_class___range__9_13_range__32_32_range__133_133_range__160_160_range__5760_5760_range__6158_6158_range__8192_8202_range__8232_8233_range__8239_8239_range__8287_8287_range__12288_12288_(builder);
      
        _init_prod__$LAYOUT__$Comment_(builder);
      
    }
  }
	
  protected static class $PostProtocolChars {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__$PostProtocolChars__lit___62_$URLChars_lit___58_47_47_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new LiteralStackNode<IConstructor>(-3867, 2, prod__lit___58_47_47__char_class___range__58_58_char_class___range__47_47_char_class___range__47_47_, new int[] {58,47,47}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-3866, 1, "$URLChars", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-3864, 0, prod__lit___62__char_class___range__62_62_, new int[] {62}, null, null);
      builder.addAlternative(RascalParser.prod__$PostProtocolChars__lit___62_$URLChars_lit___58_47_47_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__$PostProtocolChars__lit___62_$URLChars_lit___58_47_47_(builder);
      
    }
  }
	
  protected static class $UserType {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__name_$UserType__name_$QualifiedName_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(-3459, 0, "$QualifiedName", null, null);
      builder.addAlternative(RascalParser.prod__name_$UserType__name_$QualifiedName_, tmp);
	}
    protected static final void _init_prod__parametric_$UserType__name_$QualifiedName_$layouts_LAYOUTLIST_lit___91_$layouts_LAYOUTLIST_parameters_iter_seps__$Type__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___93_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[7];
      
      tmp[6] = new LiteralStackNode<IConstructor>(-3454, 6, prod__lit___93__char_class___range__93_93_, new int[] {93}, null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(-3453, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new SeparatedListStackNode<IConstructor>(-3450, 4, regular__iter_seps__$Type__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(-3444, 0, "$Type", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(-3446, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode<IConstructor>(-3447, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode<IConstructor>(-3449, 3, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-3442, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(-3440, 2, prod__lit___91__char_class___range__91_91_, new int[] {91}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-3439, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-3437, 0, "$QualifiedName", null, new ICompletionFilter[] {new StringFollowRequirement(new int[] {91})});
      builder.addAlternative(RascalParser.prod__parametric_$UserType__name_$QualifiedName_$layouts_LAYOUTLIST_lit___91_$layouts_LAYOUTLIST_parameters_iter_seps__$Type__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___93_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__name_$UserType__name_$QualifiedName_(builder);
      
        _init_prod__parametric_$UserType__name_$QualifiedName_$layouts_LAYOUTLIST_lit___91_$layouts_LAYOUTLIST_parameters_iter_seps__$Type__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___93_(builder);
      
    }
  }
	
  protected static class start__$Command {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__start__$Command__$layouts_LAYOUTLIST_top_$Command_$layouts_LAYOUTLIST_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode<IConstructor>(-3566, 2, "$layouts_LAYOUTLIST", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-3563, 1, "$Command", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-3561, 0, "$layouts_LAYOUTLIST", null, null);
      builder.addAlternative(RascalParser.prod__start__$Command__$layouts_LAYOUTLIST_top_$Command_$layouts_LAYOUTLIST_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__start__$Command__$layouts_LAYOUTLIST_top_$Command_$layouts_LAYOUTLIST_(builder);
      
    }
  }
	
  protected static class $Mapping__$Expression {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__default_$Mapping__$Expression__from_$Expression_$layouts_LAYOUTLIST_lit___58_$layouts_LAYOUTLIST_to_$Expression_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(-3811, 4, "$Expression", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-3809, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(-3807, 2, prod__lit___58__char_class___range__58_58_, new int[] {58}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-3806, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-3803, 0, "$Expression", null, null);
      builder.addAlternative(RascalParser.prod__default_$Mapping__$Expression__from_$Expression_$layouts_LAYOUTLIST_lit___58_$layouts_LAYOUTLIST_to_$Expression_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__default_$Mapping__$Expression__from_$Expression_$layouts_LAYOUTLIST_lit___58_$layouts_LAYOUTLIST_to_$Expression_(builder);
      
    }
  }
	
  protected static class $SyntaxDefinition {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__keyword_$SyntaxDefinition__lit_keyword_$layouts_LAYOUTLIST_defined_$Sym_$layouts_LAYOUTLIST_lit___61_$layouts_LAYOUTLIST_production_$Prod_$layouts_LAYOUTLIST_lit___59__tag__Foldable(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[9];
      
      tmp[8] = new LiteralStackNode<IConstructor>(-3675, 8, prod__lit___59__char_class___range__59_59_, new int[] {59}, null, null);
      tmp[7] = new NonTerminalStackNode<IConstructor>(-3674, 7, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new NonTerminalStackNode<IConstructor>(-3671, 6, "$Prod", null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(-3669, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode<IConstructor>(-3667, 4, prod__lit___61__char_class___range__61_61_, new int[] {61}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-3666, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(-3663, 2, "$Sym", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-3661, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-3659, 0, prod__lit_keyword__char_class___range__107_107_char_class___range__101_101_char_class___range__121_121_char_class___range__119_119_char_class___range__111_111_char_class___range__114_114_char_class___range__100_100_, new int[] {107,101,121,119,111,114,100}, null, null);
      builder.addAlternative(RascalParser.prod__keyword_$SyntaxDefinition__lit_keyword_$layouts_LAYOUTLIST_defined_$Sym_$layouts_LAYOUTLIST_lit___61_$layouts_LAYOUTLIST_production_$Prod_$layouts_LAYOUTLIST_lit___59__tag__Foldable, tmp);
	}
    protected static final void _init_prod__language_$SyntaxDefinition__start_$Start_$layouts_LAYOUTLIST_lit_syntax_$layouts_LAYOUTLIST_defined_$Sym_$layouts_LAYOUTLIST_lit___61_$layouts_LAYOUTLIST_production_$Prod_$layouts_LAYOUTLIST_lit___59__tag__Foldable(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[11];
      
      tmp[10] = new LiteralStackNode<IConstructor>(-3655, 10, prod__lit___59__char_class___range__59_59_, new int[] {59}, null, null);
      tmp[9] = new NonTerminalStackNode<IConstructor>(-3654, 9, "$layouts_LAYOUTLIST", null, null);
      tmp[8] = new NonTerminalStackNode<IConstructor>(-3651, 8, "$Prod", null, null);
      tmp[7] = new NonTerminalStackNode<IConstructor>(-3649, 7, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new LiteralStackNode<IConstructor>(-3647, 6, prod__lit___61__char_class___range__61_61_, new int[] {61}, null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(-3646, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new NonTerminalStackNode<IConstructor>(-3643, 4, "$Sym", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-3641, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(-3639, 2, prod__lit_syntax__char_class___range__115_115_char_class___range__121_121_char_class___range__110_110_char_class___range__116_116_char_class___range__97_97_char_class___range__120_120_, new int[] {115,121,110,116,97,120}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-3638, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-3635, 0, "$Start", null, null);
      builder.addAlternative(RascalParser.prod__language_$SyntaxDefinition__start_$Start_$layouts_LAYOUTLIST_lit_syntax_$layouts_LAYOUTLIST_defined_$Sym_$layouts_LAYOUTLIST_lit___61_$layouts_LAYOUTLIST_production_$Prod_$layouts_LAYOUTLIST_lit___59__tag__Foldable, tmp);
	}
    protected static final void _init_prod__layout_$SyntaxDefinition__vis_$Visibility_$layouts_LAYOUTLIST_lit_layout_$layouts_LAYOUTLIST_defined_$Sym_$layouts_LAYOUTLIST_lit___61_$layouts_LAYOUTLIST_production_$Prod_$layouts_LAYOUTLIST_lit___59__tag__Foldable(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[11];
      
      tmp[10] = new LiteralStackNode<IConstructor>(-3720, 10, prod__lit___59__char_class___range__59_59_, new int[] {59}, null, null);
      tmp[9] = new NonTerminalStackNode<IConstructor>(-3719, 9, "$layouts_LAYOUTLIST", null, null);
      tmp[8] = new NonTerminalStackNode<IConstructor>(-3716, 8, "$Prod", null, null);
      tmp[7] = new NonTerminalStackNode<IConstructor>(-3714, 7, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new LiteralStackNode<IConstructor>(-3712, 6, prod__lit___61__char_class___range__61_61_, new int[] {61}, null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(-3711, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new NonTerminalStackNode<IConstructor>(-3708, 4, "$Sym", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-3706, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(-3704, 2, prod__lit_layout__char_class___range__108_108_char_class___range__97_97_char_class___range__121_121_char_class___range__111_111_char_class___range__117_117_char_class___range__116_116_, new int[] {108,97,121,111,117,116}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-3703, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-3700, 0, "$Visibility", null, null);
      builder.addAlternative(RascalParser.prod__layout_$SyntaxDefinition__vis_$Visibility_$layouts_LAYOUTLIST_lit_layout_$layouts_LAYOUTLIST_defined_$Sym_$layouts_LAYOUTLIST_lit___61_$layouts_LAYOUTLIST_production_$Prod_$layouts_LAYOUTLIST_lit___59__tag__Foldable, tmp);
	}
    protected static final void _init_prod__lexical_$SyntaxDefinition__lit_lexical_$layouts_LAYOUTLIST_defined_$Sym_$layouts_LAYOUTLIST_lit___61_$layouts_LAYOUTLIST_production_$Prod_$layouts_LAYOUTLIST_lit___59__tag__Foldable(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[9];
      
      tmp[8] = new LiteralStackNode<IConstructor>(-3695, 8, prod__lit___59__char_class___range__59_59_, new int[] {59}, null, null);
      tmp[7] = new NonTerminalStackNode<IConstructor>(-3694, 7, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new NonTerminalStackNode<IConstructor>(-3691, 6, "$Prod", null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(-3689, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode<IConstructor>(-3687, 4, prod__lit___61__char_class___range__61_61_, new int[] {61}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-3686, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(-3683, 2, "$Sym", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-3681, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-3679, 0, prod__lit_lexical__char_class___range__108_108_char_class___range__101_101_char_class___range__120_120_char_class___range__105_105_char_class___range__99_99_char_class___range__97_97_char_class___range__108_108_, new int[] {108,101,120,105,99,97,108}, null, null);
      builder.addAlternative(RascalParser.prod__lexical_$SyntaxDefinition__lit_lexical_$layouts_LAYOUTLIST_defined_$Sym_$layouts_LAYOUTLIST_lit___61_$layouts_LAYOUTLIST_production_$Prod_$layouts_LAYOUTLIST_lit___59__tag__Foldable, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__keyword_$SyntaxDefinition__lit_keyword_$layouts_LAYOUTLIST_defined_$Sym_$layouts_LAYOUTLIST_lit___61_$layouts_LAYOUTLIST_production_$Prod_$layouts_LAYOUTLIST_lit___59__tag__Foldable(builder);
      
        _init_prod__language_$SyntaxDefinition__start_$Start_$layouts_LAYOUTLIST_lit_syntax_$layouts_LAYOUTLIST_defined_$Sym_$layouts_LAYOUTLIST_lit___61_$layouts_LAYOUTLIST_production_$Prod_$layouts_LAYOUTLIST_lit___59__tag__Foldable(builder);
      
        _init_prod__layout_$SyntaxDefinition__vis_$Visibility_$layouts_LAYOUTLIST_lit_layout_$layouts_LAYOUTLIST_defined_$Sym_$layouts_LAYOUTLIST_lit___61_$layouts_LAYOUTLIST_production_$Prod_$layouts_LAYOUTLIST_lit___59__tag__Foldable(builder);
      
        _init_prod__lexical_$SyntaxDefinition__lit_lexical_$layouts_LAYOUTLIST_defined_$Sym_$layouts_LAYOUTLIST_lit___61_$layouts_LAYOUTLIST_production_$Prod_$layouts_LAYOUTLIST_lit___59__tag__Foldable(builder);
      
    }
  }
	
  protected static class $Target {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__empty_$Target__(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new EpsilonStackNode<IConstructor>(-3607, 0);
      builder.addAlternative(RascalParser.prod__empty_$Target__, tmp);
	}
    protected static final void _init_prod__labeled_$Target__name_$Name_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(-3603, 0, "$Name", null, null);
      builder.addAlternative(RascalParser.prod__labeled_$Target__name_$Name_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__empty_$Target__(builder);
      
        _init_prod__labeled_$Target__name_$Name_(builder);
      
    }
  }
	
  protected static class $RascalKeywords {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__$RascalKeywords__lit_tuple_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-4244, 0, prod__lit_tuple__char_class___range__116_116_char_class___range__117_117_char_class___range__112_112_char_class___range__108_108_char_class___range__101_101_, new int[] {116,117,112,108,101}, null, null);
      builder.addAlternative(RascalParser.prod__$RascalKeywords__lit_tuple_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_int_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-4241, 0, prod__lit_int__char_class___range__105_105_char_class___range__110_110_char_class___range__116_116_, new int[] {105,110,116}, null, null);
      builder.addAlternative(RascalParser.prod__$RascalKeywords__lit_int_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_mod_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-4238, 0, prod__lit_mod__char_class___range__109_109_char_class___range__111_111_char_class___range__100_100_, new int[] {109,111,100}, null, null);
      builder.addAlternative(RascalParser.prod__$RascalKeywords__lit_mod_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_fail_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-4235, 0, prod__lit_fail__char_class___range__102_102_char_class___range__97_97_char_class___range__105_105_char_class___range__108_108_, new int[] {102,97,105,108}, null, null);
      builder.addAlternative(RascalParser.prod__$RascalKeywords__lit_fail_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_switch_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-4232, 0, prod__lit_switch__char_class___range__115_115_char_class___range__119_119_char_class___range__105_105_char_class___range__116_116_char_class___range__99_99_char_class___range__104_104_, new int[] {115,119,105,116,99,104}, null, null);
      builder.addAlternative(RascalParser.prod__$RascalKeywords__lit_switch_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_throw_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-4229, 0, prod__lit_throw__char_class___range__116_116_char_class___range__104_104_char_class___range__114_114_char_class___range__111_111_char_class___range__119_119_, new int[] {116,104,114,111,119}, null, null);
      builder.addAlternative(RascalParser.prod__$RascalKeywords__lit_throw_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_alias_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-4226, 0, prod__lit_alias__char_class___range__97_97_char_class___range__108_108_char_class___range__105_105_char_class___range__97_97_char_class___range__115_115_, new int[] {97,108,105,97,115}, null, null);
      builder.addAlternative(RascalParser.prod__$RascalKeywords__lit_alias_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_default_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-4223, 0, prod__lit_default__char_class___range__100_100_char_class___range__101_101_char_class___range__102_102_char_class___range__97_97_char_class___range__117_117_char_class___range__108_108_char_class___range__116_116_, new int[] {100,101,102,97,117,108,116}, null, null);
      builder.addAlternative(RascalParser.prod__$RascalKeywords__lit_default_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_throws_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-4220, 0, prod__lit_throws__char_class___range__116_116_char_class___range__104_104_char_class___range__114_114_char_class___range__111_111_char_class___range__119_119_char_class___range__115_115_, new int[] {116,104,114,111,119,115}, null, null);
      builder.addAlternative(RascalParser.prod__$RascalKeywords__lit_throws_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_module_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-4217, 0, prod__lit_module__char_class___range__109_109_char_class___range__111_111_char_class___range__100_100_char_class___range__117_117_char_class___range__108_108_char_class___range__101_101_, new int[] {109,111,100,117,108,101}, null, null);
      builder.addAlternative(RascalParser.prod__$RascalKeywords__lit_module_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_true_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-4211, 0, prod__lit_true__char_class___range__116_116_char_class___range__114_114_char_class___range__117_117_char_class___range__101_101_, new int[] {116,114,117,101}, null, null);
      builder.addAlternative(RascalParser.prod__$RascalKeywords__lit_true_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_private_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-4214, 0, prod__lit_private__char_class___range__112_112_char_class___range__114_114_char_class___range__105_105_char_class___range__118_118_char_class___range__97_97_char_class___range__116_116_char_class___range__101_101_, new int[] {112,114,105,118,97,116,101}, null, null);
      builder.addAlternative(RascalParser.prod__$RascalKeywords__lit_private_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_map_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-4208, 0, prod__lit_map__char_class___range__109_109_char_class___range__97_97_char_class___range__112_112_, new int[] {109,97,112}, null, null);
      builder.addAlternative(RascalParser.prod__$RascalKeywords__lit_map_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_test_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-4205, 0, prod__lit_test__char_class___range__116_116_char_class___range__101_101_char_class___range__115_115_char_class___range__116_116_, new int[] {116,101,115,116}, null, null);
      builder.addAlternative(RascalParser.prod__$RascalKeywords__lit_test_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_start_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-4202, 0, prod__lit_start__char_class___range__115_115_char_class___range__116_116_char_class___range__97_97_char_class___range__114_114_char_class___range__116_116_, new int[] {115,116,97,114,116}, null, null);
      builder.addAlternative(RascalParser.prod__$RascalKeywords__lit_start_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_import_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-4199, 0, prod__lit_import__char_class___range__105_105_char_class___range__109_109_char_class___range__112_112_char_class___range__111_111_char_class___range__114_114_char_class___range__116_116_, new int[] {105,109,112,111,114,116}, null, null);
      builder.addAlternative(RascalParser.prod__$RascalKeywords__lit_import_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_assert_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-4193, 0, prod__lit_assert__char_class___range__97_97_char_class___range__115_115_char_class___range__115_115_char_class___range__101_101_char_class___range__114_114_char_class___range__116_116_, new int[] {97,115,115,101,114,116}, null, null);
      builder.addAlternative(RascalParser.prod__$RascalKeywords__lit_assert_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_loc_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-4196, 0, prod__lit_loc__char_class___range__108_108_char_class___range__111_111_char_class___range__99_99_, new int[] {108,111,99}, null, null);
      builder.addAlternative(RascalParser.prod__$RascalKeywords__lit_loc_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_anno_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-4187, 0, prod__lit_anno__char_class___range__97_97_char_class___range__110_110_char_class___range__110_110_char_class___range__111_111_, new int[] {97,110,110,111}, null, null);
      builder.addAlternative(RascalParser.prod__$RascalKeywords__lit_anno_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_insert_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-4190, 0, prod__lit_insert__char_class___range__105_105_char_class___range__110_110_char_class___range__115_115_char_class___range__101_101_char_class___range__114_114_char_class___range__116_116_, new int[] {105,110,115,101,114,116}, null, null);
      builder.addAlternative(RascalParser.prod__$RascalKeywords__lit_insert_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_public_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-4184, 0, prod__lit_public__char_class___range__112_112_char_class___range__117_117_char_class___range__98_98_char_class___range__108_108_char_class___range__105_105_char_class___range__99_99_, new int[] {112,117,98,108,105,99}, null, null);
      builder.addAlternative(RascalParser.prod__$RascalKeywords__lit_public_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_void_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-4181, 0, prod__lit_void__char_class___range__118_118_char_class___range__111_111_char_class___range__105_105_char_class___range__100_100_, new int[] {118,111,105,100}, null, null);
      builder.addAlternative(RascalParser.prod__$RascalKeywords__lit_void_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_try_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-4178, 0, prod__lit_try__char_class___range__116_116_char_class___range__114_114_char_class___range__121_121_, new int[] {116,114,121}, null, null);
      builder.addAlternative(RascalParser.prod__$RascalKeywords__lit_try_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__$BasicType_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(-4175, 0, "$BasicType", null, null);
      builder.addAlternative(RascalParser.prod__$RascalKeywords__$BasicType_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_value_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-4171, 0, prod__lit_value__char_class___range__118_118_char_class___range__97_97_char_class___range__108_108_char_class___range__117_117_char_class___range__101_101_, new int[] {118,97,108,117,101}, null, null);
      builder.addAlternative(RascalParser.prod__$RascalKeywords__lit_value_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_list_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-4168, 0, prod__lit_list__char_class___range__108_108_char_class___range__105_105_char_class___range__115_115_char_class___range__116_116_, new int[] {108,105,115,116}, null, null);
      builder.addAlternative(RascalParser.prod__$RascalKeywords__lit_list_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_lrel_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-4165, 0, prod__lit_lrel__char_class___range__108_108_char_class___range__114_114_char_class___range__101_101_char_class___range__108_108_, new int[] {108,114,101,108}, null, null);
      builder.addAlternative(RascalParser.prod__$RascalKeywords__lit_lrel_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_dynamic_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-4162, 0, prod__lit_dynamic__char_class___range__100_100_char_class___range__121_121_char_class___range__110_110_char_class___range__97_97_char_class___range__109_109_char_class___range__105_105_char_class___range__99_99_, new int[] {100,121,110,97,109,105,99}, null, null);
      builder.addAlternative(RascalParser.prod__$RascalKeywords__lit_dynamic_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_o_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-4159, 0, prod__lit_o__char_class___range__111_111_, new int[] {111}, null, null);
      builder.addAlternative(RascalParser.prod__$RascalKeywords__lit_o_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_tag_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-4156, 0, prod__lit_tag__char_class___range__116_116_char_class___range__97_97_char_class___range__103_103_, new int[] {116,97,103}, null, null);
      builder.addAlternative(RascalParser.prod__$RascalKeywords__lit_tag_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_data_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-4153, 0, prod__lit_data__char_class___range__100_100_char_class___range__97_97_char_class___range__116_116_char_class___range__97_97_, new int[] {100,97,116,97}, null, null);
      builder.addAlternative(RascalParser.prod__$RascalKeywords__lit_data_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_append_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-4150, 0, prod__lit_append__char_class___range__97_97_char_class___range__112_112_char_class___range__112_112_char_class___range__101_101_char_class___range__110_110_char_class___range__100_100_, new int[] {97,112,112,101,110,100}, null, null);
      builder.addAlternative(RascalParser.prod__$RascalKeywords__lit_append_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_extend_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-4147, 0, prod__lit_extend__char_class___range__101_101_char_class___range__120_120_char_class___range__116_116_char_class___range__101_101_char_class___range__110_110_char_class___range__100_100_, new int[] {101,120,116,101,110,100}, null, null);
      builder.addAlternative(RascalParser.prod__$RascalKeywords__lit_extend_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_notin_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-4141, 0, prod__lit_notin__char_class___range__110_110_char_class___range__111_111_char_class___range__116_116_char_class___range__105_105_char_class___range__110_110_, new int[] {110,111,116,105,110}, null, null);
      builder.addAlternative(RascalParser.prod__$RascalKeywords__lit_notin_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_type_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-4144, 0, prod__lit_type__char_class___range__116_116_char_class___range__121_121_char_class___range__112_112_char_class___range__101_101_, new int[] {116,121,112,101}, null, null);
      builder.addAlternative(RascalParser.prod__$RascalKeywords__lit_type_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_catch_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-4138, 0, prod__lit_catch__char_class___range__99_99_char_class___range__97_97_char_class___range__116_116_char_class___range__99_99_char_class___range__104_104_, new int[] {99,97,116,99,104}, null, null);
      builder.addAlternative(RascalParser.prod__$RascalKeywords__lit_catch_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_node_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-4135, 0, prod__lit_node__char_class___range__110_110_char_class___range__111_111_char_class___range__100_100_char_class___range__101_101_, new int[] {110,111,100,101}, null, null);
      builder.addAlternative(RascalParser.prod__$RascalKeywords__lit_node_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_str_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-4132, 0, prod__lit_str__char_class___range__115_115_char_class___range__116_116_char_class___range__114_114_, new int[] {115,116,114}, null, null);
      builder.addAlternative(RascalParser.prod__$RascalKeywords__lit_str_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_one_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-4129, 0, prod__lit_one__char_class___range__111_111_char_class___range__110_110_char_class___range__101_101_, new int[] {111,110,101}, null, null);
      builder.addAlternative(RascalParser.prod__$RascalKeywords__lit_one_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_non_assoc_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-4126, 0, prod__lit_non_assoc__char_class___range__110_110_char_class___range__111_111_char_class___range__110_110_char_class___range__45_45_char_class___range__97_97_char_class___range__115_115_char_class___range__115_115_char_class___range__111_111_char_class___range__99_99_, new int[] {110,111,110,45,97,115,115,111,99}, null, null);
      builder.addAlternative(RascalParser.prod__$RascalKeywords__lit_non_assoc_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_if_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-4123, 0, prod__lit_if__char_class___range__105_105_char_class___range__102_102_, new int[] {105,102}, null, null);
      builder.addAlternative(RascalParser.prod__$RascalKeywords__lit_if_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_join_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-4108, 0, prod__lit_join__char_class___range__106_106_char_class___range__111_111_char_class___range__105_105_char_class___range__110_110_, new int[] {106,111,105,110}, null, null);
      builder.addAlternative(RascalParser.prod__$RascalKeywords__lit_join_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_it_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-4111, 0, prod__lit_it__char_class___range__105_105_char_class___range__116_116_, new int[] {105,116}, null, null);
      builder.addAlternative(RascalParser.prod__$RascalKeywords__lit_it_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_in_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-4114, 0, prod__lit_in__char_class___range__105_105_char_class___range__110_110_, new int[] {105,110}, null, null);
      builder.addAlternative(RascalParser.prod__$RascalKeywords__lit_in_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_else_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-4117, 0, prod__lit_else__char_class___range__101_101_char_class___range__108_108_char_class___range__115_115_char_class___range__101_101_, new int[] {101,108,115,101}, null, null);
      builder.addAlternative(RascalParser.prod__$RascalKeywords__lit_else_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_return_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-4120, 0, prod__lit_return__char_class___range__114_114_char_class___range__101_101_char_class___range__116_116_char_class___range__117_117_char_class___range__114_114_char_class___range__110_110_, new int[] {114,101,116,117,114,110}, null, null);
      builder.addAlternative(RascalParser.prod__$RascalKeywords__lit_return_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_visit_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-4105, 0, prod__lit_visit__char_class___range__118_118_char_class___range__105_105_char_class___range__115_115_char_class___range__105_105_char_class___range__116_116_, new int[] {118,105,115,105,116}, null, null);
      builder.addAlternative(RascalParser.prod__$RascalKeywords__lit_visit_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_bracket_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-4102, 0, prod__lit_bracket__char_class___range__98_98_char_class___range__114_114_char_class___range__97_97_char_class___range__99_99_char_class___range__107_107_char_class___range__101_101_char_class___range__116_116_, new int[] {98,114,97,99,107,101,116}, null, null);
      builder.addAlternative(RascalParser.prod__$RascalKeywords__lit_bracket_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_continue_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-4099, 0, prod__lit_continue__char_class___range__99_99_char_class___range__111_111_char_class___range__110_110_char_class___range__116_116_char_class___range__105_105_char_class___range__110_110_char_class___range__117_117_char_class___range__101_101_, new int[] {99,111,110,116,105,110,117,101}, null, null);
      builder.addAlternative(RascalParser.prod__$RascalKeywords__lit_continue_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_for_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-4096, 0, prod__lit_for__char_class___range__102_102_char_class___range__111_111_char_class___range__114_114_, new int[] {102,111,114}, null, null);
      builder.addAlternative(RascalParser.prod__$RascalKeywords__lit_for_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_assoc_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-4093, 0, prod__lit_assoc__char_class___range__97_97_char_class___range__115_115_char_class___range__115_115_char_class___range__111_111_char_class___range__99_99_, new int[] {97,115,115,111,99}, null, null);
      builder.addAlternative(RascalParser.prod__$RascalKeywords__lit_assoc_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_bag_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-4090, 0, prod__lit_bag__char_class___range__98_98_char_class___range__97_97_char_class___range__103_103_, new int[] {98,97,103}, null, null);
      builder.addAlternative(RascalParser.prod__$RascalKeywords__lit_bag_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_set_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-4087, 0, prod__lit_set__char_class___range__115_115_char_class___range__101_101_char_class___range__116_116_, new int[] {115,101,116}, null, null);
      builder.addAlternative(RascalParser.prod__$RascalKeywords__lit_set_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_num_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-4084, 0, prod__lit_num__char_class___range__110_110_char_class___range__117_117_char_class___range__109_109_, new int[] {110,117,109}, null, null);
      builder.addAlternative(RascalParser.prod__$RascalKeywords__lit_num_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_layout_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-4075, 0, prod__lit_layout__char_class___range__108_108_char_class___range__97_97_char_class___range__121_121_char_class___range__111_111_char_class___range__117_117_char_class___range__116_116_, new int[] {108,97,121,111,117,116}, null, null);
      builder.addAlternative(RascalParser.prod__$RascalKeywords__lit_layout_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_case_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-4078, 0, prod__lit_case__char_class___range__99_99_char_class___range__97_97_char_class___range__115_115_char_class___range__101_101_, new int[] {99,97,115,101}, null, null);
      builder.addAlternative(RascalParser.prod__$RascalKeywords__lit_case_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_while_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-4081, 0, prod__lit_while__char_class___range__119_119_char_class___range__104_104_char_class___range__105_105_char_class___range__108_108_char_class___range__101_101_, new int[] {119,104,105,108,101}, null, null);
      builder.addAlternative(RascalParser.prod__$RascalKeywords__lit_while_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_datetime_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-4072, 0, prod__lit_datetime__char_class___range__100_100_char_class___range__97_97_char_class___range__116_116_char_class___range__101_101_char_class___range__116_116_char_class___range__105_105_char_class___range__109_109_char_class___range__101_101_, new int[] {100,97,116,101,116,105,109,101}, null, null);
      builder.addAlternative(RascalParser.prod__$RascalKeywords__lit_datetime_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_filter_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-4069, 0, prod__lit_filter__char_class___range__102_102_char_class___range__105_105_char_class___range__108_108_char_class___range__116_116_char_class___range__101_101_char_class___range__114_114_, new int[] {102,105,108,116,101,114}, null, null);
      builder.addAlternative(RascalParser.prod__$RascalKeywords__lit_filter_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_bool_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-4066, 0, prod__lit_bool__char_class___range__98_98_char_class___range__111_111_char_class___range__111_111_char_class___range__108_108_, new int[] {98,111,111,108}, null, null);
      builder.addAlternative(RascalParser.prod__$RascalKeywords__lit_bool_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_finally_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-4060, 0, prod__lit_finally__char_class___range__102_102_char_class___range__105_105_char_class___range__110_110_char_class___range__97_97_char_class___range__108_108_char_class___range__108_108_char_class___range__121_121_, new int[] {102,105,110,97,108,108,121}, null, null);
      builder.addAlternative(RascalParser.prod__$RascalKeywords__lit_finally_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_real_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-4063, 0, prod__lit_real__char_class___range__114_114_char_class___range__101_101_char_class___range__97_97_char_class___range__108_108_, new int[] {114,101,97,108}, null, null);
      builder.addAlternative(RascalParser.prod__$RascalKeywords__lit_real_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_all_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-4057, 0, prod__lit_all__char_class___range__97_97_char_class___range__108_108_char_class___range__108_108_, new int[] {97,108,108}, null, null);
      builder.addAlternative(RascalParser.prod__$RascalKeywords__lit_all_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_any_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-4054, 0, prod__lit_any__char_class___range__97_97_char_class___range__110_110_char_class___range__121_121_, new int[] {97,110,121}, null, null);
      builder.addAlternative(RascalParser.prod__$RascalKeywords__lit_any_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_false_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-4051, 0, prod__lit_false__char_class___range__102_102_char_class___range__97_97_char_class___range__108_108_char_class___range__115_115_char_class___range__101_101_, new int[] {102,97,108,115,101}, null, null);
      builder.addAlternative(RascalParser.prod__$RascalKeywords__lit_false_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_rel_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-4048, 0, prod__lit_rel__char_class___range__114_114_char_class___range__101_101_char_class___range__108_108_, new int[] {114,101,108}, null, null);
      builder.addAlternative(RascalParser.prod__$RascalKeywords__lit_rel_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_break_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-4045, 0, prod__lit_break__char_class___range__98_98_char_class___range__114_114_char_class___range__101_101_char_class___range__97_97_char_class___range__107_107_, new int[] {98,114,101,97,107}, null, null);
      builder.addAlternative(RascalParser.prod__$RascalKeywords__lit_break_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_rat_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-4039, 0, prod__lit_rat__char_class___range__114_114_char_class___range__97_97_char_class___range__116_116_, new int[] {114,97,116}, null, null);
      builder.addAlternative(RascalParser.prod__$RascalKeywords__lit_rat_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_solve_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-4042, 0, prod__lit_solve__char_class___range__115_115_char_class___range__111_111_char_class___range__108_108_char_class___range__118_118_char_class___range__101_101_, new int[] {115,111,108,118,101}, null, null);
      builder.addAlternative(RascalParser.prod__$RascalKeywords__lit_solve_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__$RascalKeywords__lit_tuple_(builder);
      
        _init_prod__$RascalKeywords__lit_int_(builder);
      
        _init_prod__$RascalKeywords__lit_mod_(builder);
      
        _init_prod__$RascalKeywords__lit_fail_(builder);
      
        _init_prod__$RascalKeywords__lit_switch_(builder);
      
        _init_prod__$RascalKeywords__lit_throw_(builder);
      
        _init_prod__$RascalKeywords__lit_alias_(builder);
      
        _init_prod__$RascalKeywords__lit_default_(builder);
      
        _init_prod__$RascalKeywords__lit_throws_(builder);
      
        _init_prod__$RascalKeywords__lit_module_(builder);
      
        _init_prod__$RascalKeywords__lit_true_(builder);
      
        _init_prod__$RascalKeywords__lit_private_(builder);
      
        _init_prod__$RascalKeywords__lit_map_(builder);
      
        _init_prod__$RascalKeywords__lit_test_(builder);
      
        _init_prod__$RascalKeywords__lit_start_(builder);
      
        _init_prod__$RascalKeywords__lit_import_(builder);
      
        _init_prod__$RascalKeywords__lit_assert_(builder);
      
        _init_prod__$RascalKeywords__lit_loc_(builder);
      
        _init_prod__$RascalKeywords__lit_anno_(builder);
      
        _init_prod__$RascalKeywords__lit_insert_(builder);
      
        _init_prod__$RascalKeywords__lit_public_(builder);
      
        _init_prod__$RascalKeywords__lit_void_(builder);
      
        _init_prod__$RascalKeywords__lit_try_(builder);
      
        _init_prod__$RascalKeywords__$BasicType_(builder);
      
        _init_prod__$RascalKeywords__lit_value_(builder);
      
        _init_prod__$RascalKeywords__lit_list_(builder);
      
        _init_prod__$RascalKeywords__lit_lrel_(builder);
      
        _init_prod__$RascalKeywords__lit_dynamic_(builder);
      
        _init_prod__$RascalKeywords__lit_o_(builder);
      
        _init_prod__$RascalKeywords__lit_tag_(builder);
      
        _init_prod__$RascalKeywords__lit_data_(builder);
      
        _init_prod__$RascalKeywords__lit_append_(builder);
      
        _init_prod__$RascalKeywords__lit_extend_(builder);
      
        _init_prod__$RascalKeywords__lit_notin_(builder);
      
        _init_prod__$RascalKeywords__lit_type_(builder);
      
        _init_prod__$RascalKeywords__lit_catch_(builder);
      
        _init_prod__$RascalKeywords__lit_node_(builder);
      
        _init_prod__$RascalKeywords__lit_str_(builder);
      
        _init_prod__$RascalKeywords__lit_one_(builder);
      
        _init_prod__$RascalKeywords__lit_non_assoc_(builder);
      
        _init_prod__$RascalKeywords__lit_if_(builder);
      
        _init_prod__$RascalKeywords__lit_join_(builder);
      
        _init_prod__$RascalKeywords__lit_it_(builder);
      
        _init_prod__$RascalKeywords__lit_in_(builder);
      
        _init_prod__$RascalKeywords__lit_else_(builder);
      
        _init_prod__$RascalKeywords__lit_return_(builder);
      
        _init_prod__$RascalKeywords__lit_visit_(builder);
      
        _init_prod__$RascalKeywords__lit_bracket_(builder);
      
        _init_prod__$RascalKeywords__lit_continue_(builder);
      
        _init_prod__$RascalKeywords__lit_for_(builder);
      
        _init_prod__$RascalKeywords__lit_assoc_(builder);
      
        _init_prod__$RascalKeywords__lit_bag_(builder);
      
        _init_prod__$RascalKeywords__lit_set_(builder);
      
        _init_prod__$RascalKeywords__lit_num_(builder);
      
        _init_prod__$RascalKeywords__lit_layout_(builder);
      
        _init_prod__$RascalKeywords__lit_case_(builder);
      
        _init_prod__$RascalKeywords__lit_while_(builder);
      
        _init_prod__$RascalKeywords__lit_datetime_(builder);
      
        _init_prod__$RascalKeywords__lit_filter_(builder);
      
        _init_prod__$RascalKeywords__lit_bool_(builder);
      
        _init_prod__$RascalKeywords__lit_finally_(builder);
      
        _init_prod__$RascalKeywords__lit_real_(builder);
      
        _init_prod__$RascalKeywords__lit_all_(builder);
      
        _init_prod__$RascalKeywords__lit_any_(builder);
      
        _init_prod__$RascalKeywords__lit_false_(builder);
      
        _init_prod__$RascalKeywords__lit_rel_(builder);
      
        _init_prod__$RascalKeywords__lit_break_(builder);
      
        _init_prod__$RascalKeywords__lit_rat_(builder);
      
        _init_prod__$RascalKeywords__lit_solve_(builder);
      
    }
  }
	
  protected static class $Char {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__$Char__char_class___range__0_31_range__33_33_range__35_38_range__40_44_range__46_59_range__61_61_range__63_90_range__94_16777215__tag__category___67_111_110_115_116_97_110_116(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new CharStackNode<IConstructor>(-3931, 0, new int[][]{{0,31},{33,33},{35,38},{40,44},{46,59},{61,61},{63,90},{94,16777215}}, null, null);
      builder.addAlternative(RascalParser.prod__$Char__char_class___range__0_31_range__33_33_range__35_38_range__40_44_range__46_59_range__61_61_range__63_90_range__94_16777215__tag__category___67_111_110_115_116_97_110_116, tmp);
	}
    protected static final void _init_prod__$Char__$UnicodeEscape__tag__category___67_111_110_115_116_97_110_116(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(-3928, 0, "$UnicodeEscape", null, null);
      builder.addAlternative(RascalParser.prod__$Char__$UnicodeEscape__tag__category___67_111_110_115_116_97_110_116, tmp);
	}
    protected static final void _init_prod__$Char__lit___92_char_class___range__32_32_range__34_34_range__39_39_range__45_45_range__60_60_range__62_62_range__91_93_range__98_98_range__102_102_range__110_110_range__114_114_range__116_116__tag__category___67_111_110_115_116_97_110_116(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[2];
      
      tmp[1] = new CharStackNode<IConstructor>(-3935, 1, new int[][]{{32,32},{34,34},{39,39},{45,45},{60,60},{62,62},{91,93},{98,98},{102,102},{110,110},{114,114},{116,116}}, null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-3934, 0, prod__lit___92__char_class___range__92_92_, new int[] {92}, null, null);
      builder.addAlternative(RascalParser.prod__$Char__lit___92_char_class___range__32_32_range__34_34_range__39_39_range__45_45_range__60_60_range__62_62_range__91_93_range__98_98_range__102_102_range__110_110_range__114_114_range__116_116__tag__category___67_111_110_115_116_97_110_116, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__$Char__char_class___range__0_31_range__33_33_range__35_38_range__40_44_range__46_59_range__61_61_range__63_90_range__94_16777215__tag__category___67_111_110_115_116_97_110_116(builder);
      
        _init_prod__$Char__$UnicodeEscape__tag__category___67_111_110_115_116_97_110_116(builder);
      
        _init_prod__$Char__lit___92_char_class___range__32_32_range__34_34_range__39_39_range__45_45_range__60_60_range__62_62_range__91_93_range__98_98_range__102_102_range__110_110_range__114_114_range__116_116__tag__category___67_111_110_115_116_97_110_116(builder);
      
    }
  }
	
  protected static class $MidPathChars {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__$MidPathChars__lit___62_$URLChars_lit___60_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new LiteralStackNode<IConstructor>(-3920, 2, prod__lit___60__char_class___range__60_60_, new int[] {60}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-3919, 1, "$URLChars", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-3917, 0, prod__lit___62__char_class___range__62_62_, new int[] {62}, null, null);
      builder.addAlternative(RascalParser.prod__$MidPathChars__lit___62_$URLChars_lit___60_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__$MidPathChars__lit___62_$URLChars_lit___60_(builder);
      
    }
  }
	
  protected static class $ModuleParameters {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__default_$ModuleParameters__lit___91_$layouts_LAYOUTLIST_parameters_iter_seps__$TypeVar__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___93_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new LiteralStackNode<IConstructor>(-4026, 4, prod__lit___93__char_class___range__93_93_, new int[] {93}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-4025, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new SeparatedListStackNode<IConstructor>(-4022, 2, regular__iter_seps__$TypeVar__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(-4016, 0, "$TypeVar", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(-4018, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode<IConstructor>(-4019, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode<IConstructor>(-4021, 3, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-4014, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-4012, 0, prod__lit___91__char_class___range__91_91_, new int[] {91}, null, null);
      builder.addAlternative(RascalParser.prod__default_$ModuleParameters__lit___91_$layouts_LAYOUTLIST_parameters_iter_seps__$TypeVar__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___93_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__default_$ModuleParameters__lit___91_$layouts_LAYOUTLIST_parameters_iter_seps__$TypeVar__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___93_(builder);
      
    }
  }
	
  protected static class $NamedRegExp {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__$NamedRegExp__char_class___range__0_46_range__48_59_range__61_61_range__63_91_range__93_16777215_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new CharStackNode<IConstructor>(-4271, 0, new int[][]{{0,46},{48,59},{61,61},{63,91},{93,16777215}}, null, null);
      builder.addAlternative(RascalParser.prod__$NamedRegExp__char_class___range__0_46_range__48_59_range__61_61_range__63_91_range__93_16777215_, tmp);
	}
    protected static final void _init_prod__$NamedRegExp__$NamedBackslash_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(-4268, 0, "$NamedBackslash", null, null);
      builder.addAlternative(RascalParser.prod__$NamedRegExp__$NamedBackslash_, tmp);
	}
    protected static final void _init_prod__$NamedRegExp__char_class___range__92_92_char_class___range__47_47_range__60_60_range__62_62_range__92_92_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[2];
      
      tmp[1] = new CharStackNode<IConstructor>(-4264, 1, new int[][]{{47,47},{60,60},{62,62},{92,92}}, null, null);
      tmp[0] = new CharStackNode<IConstructor>(-4263, 0, new int[][]{{92,92}}, null, null);
      builder.addAlternative(RascalParser.prod__$NamedRegExp__char_class___range__92_92_char_class___range__47_47_range__60_60_range__62_62_range__92_92_, tmp);
	}
    protected static final void _init_prod__$NamedRegExp__lit___60_$Name_lit___62_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new LiteralStackNode<IConstructor>(-4260, 2, prod__lit___62__char_class___range__62_62_, new int[] {62}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-4259, 1, "$Name", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-4257, 0, prod__lit___60__char_class___range__60_60_, new int[] {60}, null, null);
      builder.addAlternative(RascalParser.prod__$NamedRegExp__lit___60_$Name_lit___62_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__$NamedRegExp__char_class___range__0_46_range__48_59_range__61_61_range__63_91_range__93_16777215_(builder);
      
        _init_prod__$NamedRegExp__$NamedBackslash_(builder);
      
        _init_prod__$NamedRegExp__char_class___range__92_92_char_class___range__47_47_range__60_60_range__62_62_range__92_92_(builder);
      
        _init_prod__$NamedRegExp__lit___60_$Name_lit___62_(builder);
      
    }
  }
	
  protected static class $PrePathChars {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__$PrePathChars__$URLChars_lit___60_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[2];
      
      tmp[1] = new LiteralStackNode<IConstructor>(-3944, 1, prod__lit___60__char_class___range__60_60_, new int[] {60}, null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-3943, 0, "$URLChars", null, null);
      builder.addAlternative(RascalParser.prod__$PrePathChars__$URLChars_lit___60_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__$PrePathChars__$URLChars_lit___60_(builder);
      
    }
  }
	
  protected static class $RegExpLiteral {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__$RegExpLiteral__lit___47_iter_star__$RegExp_lit___47_$RegExpModifier_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[4];
      
      tmp[3] = new NonTerminalStackNode<IConstructor>(-4319, 3, "$RegExpModifier", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(-4317, 2, prod__lit___47__char_class___range__47_47_, new int[] {47}, null, null);
      tmp[1] = new ListStackNode<IConstructor>(-4316, 1, regular__iter_star__$RegExp, new NonTerminalStackNode<IConstructor>(-4315, 0, "$RegExp", null, null), false, null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-4313, 0, prod__lit___47__char_class___range__47_47_, new int[] {47}, null, null);
      builder.addAlternative(RascalParser.prod__$RegExpLiteral__lit___47_iter_star__$RegExp_lit___47_$RegExpModifier_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__$RegExpLiteral__lit___47_iter_star__$RegExp_lit___47_$RegExpModifier_(builder);
      
    }
  }
	
  protected static class $Strategy {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__bottomUp_$Strategy__lit_bottom_up_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-3975, 0, prod__lit_bottom_up__char_class___range__98_98_char_class___range__111_111_char_class___range__116_116_char_class___range__116_116_char_class___range__111_111_char_class___range__109_109_char_class___range__45_45_char_class___range__117_117_char_class___range__112_112_, new int[] {98,111,116,116,111,109,45,117,112}, null, null);
      builder.addAlternative(RascalParser.prod__bottomUp_$Strategy__lit_bottom_up_, tmp);
	}
    protected static final void _init_prod__bottomUpBreak_$Strategy__lit_bottom_up_break_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-3967, 0, prod__lit_bottom_up_break__char_class___range__98_98_char_class___range__111_111_char_class___range__116_116_char_class___range__116_116_char_class___range__111_111_char_class___range__109_109_char_class___range__45_45_char_class___range__117_117_char_class___range__112_112_char_class___range__45_45_char_class___range__98_98_char_class___range__114_114_char_class___range__101_101_char_class___range__97_97_char_class___range__107_107_, new int[] {98,111,116,116,111,109,45,117,112,45,98,114,101,97,107}, null, null);
      builder.addAlternative(RascalParser.prod__bottomUpBreak_$Strategy__lit_bottom_up_break_, tmp);
	}
    protected static final void _init_prod__innermost_$Strategy__lit_innermost_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-3979, 0, prod__lit_innermost__char_class___range__105_105_char_class___range__110_110_char_class___range__110_110_char_class___range__101_101_char_class___range__114_114_char_class___range__109_109_char_class___range__111_111_char_class___range__115_115_char_class___range__116_116_, new int[] {105,110,110,101,114,109,111,115,116}, null, null);
      builder.addAlternative(RascalParser.prod__innermost_$Strategy__lit_innermost_, tmp);
	}
    protected static final void _init_prod__outermost_$Strategy__lit_outermost_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-3971, 0, prod__lit_outermost__char_class___range__111_111_char_class___range__117_117_char_class___range__116_116_char_class___range__101_101_char_class___range__114_114_char_class___range__109_109_char_class___range__111_111_char_class___range__115_115_char_class___range__116_116_, new int[] {111,117,116,101,114,109,111,115,116}, null, null);
      builder.addAlternative(RascalParser.prod__outermost_$Strategy__lit_outermost_, tmp);
	}
    protected static final void _init_prod__topDown_$Strategy__lit_top_down_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-3987, 0, prod__lit_top_down__char_class___range__116_116_char_class___range__111_111_char_class___range__112_112_char_class___range__45_45_char_class___range__100_100_char_class___range__111_111_char_class___range__119_119_char_class___range__110_110_, new int[] {116,111,112,45,100,111,119,110}, null, null);
      builder.addAlternative(RascalParser.prod__topDown_$Strategy__lit_top_down_, tmp);
	}
    protected static final void _init_prod__topDownBreak_$Strategy__lit_top_down_break_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-3983, 0, prod__lit_top_down_break__char_class___range__116_116_char_class___range__111_111_char_class___range__112_112_char_class___range__45_45_char_class___range__100_100_char_class___range__111_111_char_class___range__119_119_char_class___range__110_110_char_class___range__45_45_char_class___range__98_98_char_class___range__114_114_char_class___range__101_101_char_class___range__97_97_char_class___range__107_107_, new int[] {116,111,112,45,100,111,119,110,45,98,114,101,97,107}, null, null);
      builder.addAlternative(RascalParser.prod__topDownBreak_$Strategy__lit_top_down_break_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__bottomUp_$Strategy__lit_bottom_up_(builder);
      
        _init_prod__bottomUpBreak_$Strategy__lit_bottom_up_break_(builder);
      
        _init_prod__innermost_$Strategy__lit_innermost_(builder);
      
        _init_prod__outermost_$Strategy__lit_outermost_(builder);
      
        _init_prod__topDown_$Strategy__lit_top_down_(builder);
      
        _init_prod__topDownBreak_$Strategy__lit_top_down_break_(builder);
      
    }
  }
	
  protected static class $BasicType {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__bag_$BasicType__lit_bag_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-4549, 0, prod__lit_bag__char_class___range__98_98_char_class___range__97_97_char_class___range__103_103_, new int[] {98,97,103}, null, null);
      builder.addAlternative(RascalParser.prod__bag_$BasicType__lit_bag_, tmp);
	}
    protected static final void _init_prod__bool_$BasicType__lit_bool_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-4605, 0, prod__lit_bool__char_class___range__98_98_char_class___range__111_111_char_class___range__111_111_char_class___range__108_108_, new int[] {98,111,111,108}, null, null);
      builder.addAlternative(RascalParser.prod__bool_$BasicType__lit_bool_, tmp);
	}
    protected static final void _init_prod__dateTime_$BasicType__lit_datetime_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-4545, 0, prod__lit_datetime__char_class___range__100_100_char_class___range__97_97_char_class___range__116_116_char_class___range__101_101_char_class___range__116_116_char_class___range__105_105_char_class___range__109_109_char_class___range__101_101_, new int[] {100,97,116,101,116,105,109,101}, null, null);
      builder.addAlternative(RascalParser.prod__dateTime_$BasicType__lit_datetime_, tmp);
	}
    protected static final void _init_prod__int_$BasicType__lit_int_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-4565, 0, prod__lit_int__char_class___range__105_105_char_class___range__110_110_char_class___range__116_116_, new int[] {105,110,116}, null, null);
      builder.addAlternative(RascalParser.prod__int_$BasicType__lit_int_, tmp);
	}
    protected static final void _init_prod__list_$BasicType__lit_list_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-4577, 0, prod__lit_list__char_class___range__108_108_char_class___range__105_105_char_class___range__115_115_char_class___range__116_116_, new int[] {108,105,115,116}, null, null);
      builder.addAlternative(RascalParser.prod__list_$BasicType__lit_list_, tmp);
	}
    protected static final void _init_prod__listRelation_$BasicType__lit_lrel_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-4601, 0, prod__lit_lrel__char_class___range__108_108_char_class___range__114_114_char_class___range__101_101_char_class___range__108_108_, new int[] {108,114,101,108}, null, null);
      builder.addAlternative(RascalParser.prod__listRelation_$BasicType__lit_lrel_, tmp);
	}
    protected static final void _init_prod__loc_$BasicType__lit_loc_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-4585, 0, prod__lit_loc__char_class___range__108_108_char_class___range__111_111_char_class___range__99_99_, new int[] {108,111,99}, null, null);
      builder.addAlternative(RascalParser.prod__loc_$BasicType__lit_loc_, tmp);
	}
    protected static final void _init_prod__map_$BasicType__lit_map_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-4569, 0, prod__lit_map__char_class___range__109_109_char_class___range__97_97_char_class___range__112_112_, new int[] {109,97,112}, null, null);
      builder.addAlternative(RascalParser.prod__map_$BasicType__lit_map_, tmp);
	}
    protected static final void _init_prod__node_$BasicType__lit_node_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-4561, 0, prod__lit_node__char_class___range__110_110_char_class___range__111_111_char_class___range__100_100_char_class___range__101_101_, new int[] {110,111,100,101}, null, null);
      builder.addAlternative(RascalParser.prod__node_$BasicType__lit_node_, tmp);
	}
    protected static final void _init_prod__num_$BasicType__lit_num_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-4581, 0, prod__lit_num__char_class___range__110_110_char_class___range__117_117_char_class___range__109_109_, new int[] {110,117,109}, null, null);
      builder.addAlternative(RascalParser.prod__num_$BasicType__lit_num_, tmp);
	}
    protected static final void _init_prod__rational_$BasicType__lit_rat_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-4553, 0, prod__lit_rat__char_class___range__114_114_char_class___range__97_97_char_class___range__116_116_, new int[] {114,97,116}, null, null);
      builder.addAlternative(RascalParser.prod__rational_$BasicType__lit_rat_, tmp);
	}
    protected static final void _init_prod__real_$BasicType__lit_real_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-4557, 0, prod__lit_real__char_class___range__114_114_char_class___range__101_101_char_class___range__97_97_char_class___range__108_108_, new int[] {114,101,97,108}, null, null);
      builder.addAlternative(RascalParser.prod__real_$BasicType__lit_real_, tmp);
	}
    protected static final void _init_prod__relation_$BasicType__lit_rel_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-4593, 0, prod__lit_rel__char_class___range__114_114_char_class___range__101_101_char_class___range__108_108_, new int[] {114,101,108}, null, null);
      builder.addAlternative(RascalParser.prod__relation_$BasicType__lit_rel_, tmp);
	}
    protected static final void _init_prod__set_$BasicType__lit_set_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-4537, 0, prod__lit_set__char_class___range__115_115_char_class___range__101_101_char_class___range__116_116_, new int[] {115,101,116}, null, null);
      builder.addAlternative(RascalParser.prod__set_$BasicType__lit_set_, tmp);
	}
    protected static final void _init_prod__string_$BasicType__lit_str_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-4541, 0, prod__lit_str__char_class___range__115_115_char_class___range__116_116_char_class___range__114_114_, new int[] {115,116,114}, null, null);
      builder.addAlternative(RascalParser.prod__string_$BasicType__lit_str_, tmp);
	}
    protected static final void _init_prod__tuple_$BasicType__lit_tuple_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-4573, 0, prod__lit_tuple__char_class___range__116_116_char_class___range__117_117_char_class___range__112_112_char_class___range__108_108_char_class___range__101_101_, new int[] {116,117,112,108,101}, null, null);
      builder.addAlternative(RascalParser.prod__tuple_$BasicType__lit_tuple_, tmp);
	}
    protected static final void _init_prod__type_$BasicType__lit_type_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-4533, 0, prod__lit_type__char_class___range__116_116_char_class___range__121_121_char_class___range__112_112_char_class___range__101_101_, new int[] {116,121,112,101}, null, null);
      builder.addAlternative(RascalParser.prod__type_$BasicType__lit_type_, tmp);
	}
    protected static final void _init_prod__value_$BasicType__lit_value_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-4589, 0, prod__lit_value__char_class___range__118_118_char_class___range__97_97_char_class___range__108_108_char_class___range__117_117_char_class___range__101_101_, new int[] {118,97,108,117,101}, null, null);
      builder.addAlternative(RascalParser.prod__value_$BasicType__lit_value_, tmp);
	}
    protected static final void _init_prod__void_$BasicType__lit_void_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-4597, 0, prod__lit_void__char_class___range__118_118_char_class___range__111_111_char_class___range__105_105_char_class___range__100_100_, new int[] {118,111,105,100}, null, null);
      builder.addAlternative(RascalParser.prod__void_$BasicType__lit_void_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__bag_$BasicType__lit_bag_(builder);
      
        _init_prod__bool_$BasicType__lit_bool_(builder);
      
        _init_prod__dateTime_$BasicType__lit_datetime_(builder);
      
        _init_prod__int_$BasicType__lit_int_(builder);
      
        _init_prod__list_$BasicType__lit_list_(builder);
      
        _init_prod__listRelation_$BasicType__lit_lrel_(builder);
      
        _init_prod__loc_$BasicType__lit_loc_(builder);
      
        _init_prod__map_$BasicType__lit_map_(builder);
      
        _init_prod__node_$BasicType__lit_node_(builder);
      
        _init_prod__num_$BasicType__lit_num_(builder);
      
        _init_prod__rational_$BasicType__lit_rat_(builder);
      
        _init_prod__real_$BasicType__lit_real_(builder);
      
        _init_prod__relation_$BasicType__lit_rel_(builder);
      
        _init_prod__set_$BasicType__lit_set_(builder);
      
        _init_prod__string_$BasicType__lit_str_(builder);
      
        _init_prod__tuple_$BasicType__lit_tuple_(builder);
      
        _init_prod__type_$BasicType__lit_type_(builder);
      
        _init_prod__value_$BasicType__lit_value_(builder);
      
        _init_prod__void_$BasicType__lit_void_(builder);
      
    }
  }
	
  protected static class $DateTimeLiteral {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__dateAndTimeLiteral_$DateTimeLiteral__dateAndTime_$DateAndTime_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(-4501, 0, "$DateAndTime", null, null);
      builder.addAlternative(RascalParser.prod__dateAndTimeLiteral_$DateTimeLiteral__dateAndTime_$DateAndTime_, tmp);
	}
    protected static final void _init_prod__dateLiteral_$DateTimeLiteral__date_$JustDate_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(-4495, 0, "$JustDate", null, null);
      builder.addAlternative(RascalParser.prod__dateLiteral_$DateTimeLiteral__date_$JustDate_, tmp);
	}
    protected static final void _init_prod__timeLiteral_$DateTimeLiteral__time_$JustTime_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(-4489, 0, "$JustTime", null, null);
      builder.addAlternative(RascalParser.prod__timeLiteral_$DateTimeLiteral__time_$JustTime_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__dateAndTimeLiteral_$DateTimeLiteral__dateAndTime_$DateAndTime_(builder);
      
        _init_prod__dateLiteral_$DateTimeLiteral__date_$JustDate_(builder);
      
        _init_prod__timeLiteral_$DateTimeLiteral__time_$JustTime_(builder);
      
    }
  }
	
  protected static class $FunctionDeclaration {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__abstract_$FunctionDeclaration__tags_$Tags_$layouts_LAYOUTLIST_visibility_$Visibility_$layouts_LAYOUTLIST_signature_$Signature_$layouts_LAYOUTLIST_lit___59_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[7];
      
      tmp[6] = new LiteralStackNode<IConstructor>(-4677, 6, prod__lit___59__char_class___range__59_59_, new int[] {59}, null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(-4676, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new NonTerminalStackNode<IConstructor>(-4673, 4, "$Signature", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-4671, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(-4668, 2, "$Visibility", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-4666, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-4663, 0, "$Tags", null, null);
      builder.addAlternative(RascalParser.prod__abstract_$FunctionDeclaration__tags_$Tags_$layouts_LAYOUTLIST_visibility_$Visibility_$layouts_LAYOUTLIST_signature_$Signature_$layouts_LAYOUTLIST_lit___59_, tmp);
	}
    protected static final void _init_prod__conditional_$FunctionDeclaration__tags_$Tags_$layouts_LAYOUTLIST_visibility_$Visibility_$layouts_LAYOUTLIST_signature_$Signature_$layouts_LAYOUTLIST_lit___61_$layouts_LAYOUTLIST_expression_$Expression_$layouts_LAYOUTLIST_lit_when_$layouts_LAYOUTLIST_conditions_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___59__tag__Foldable_tag__breakable___123_101_120_112_114_101_115_115_105_111_110_44_99_111_110_100_105_116_105_111_110_115_125(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[15];
      
      tmp[14] = new LiteralStackNode<IConstructor>(-4739, 14, prod__lit___59__char_class___range__59_59_, new int[] {59}, null, null);
      tmp[13] = new NonTerminalStackNode<IConstructor>(-4738, 13, "$layouts_LAYOUTLIST", null, null);
      tmp[12] = new SeparatedListStackNode<IConstructor>(-4735, 12, regular__iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(-4729, 0, "$Expression", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(-4731, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode<IConstructor>(-4732, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode<IConstructor>(-4734, 3, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[11] = new NonTerminalStackNode<IConstructor>(-4727, 11, "$layouts_LAYOUTLIST", null, null);
      tmp[10] = new LiteralStackNode<IConstructor>(-4725, 10, prod__lit_when__char_class___range__119_119_char_class___range__104_104_char_class___range__101_101_char_class___range__110_110_, new int[] {119,104,101,110}, null, null);
      tmp[9] = new NonTerminalStackNode<IConstructor>(-4724, 9, "$layouts_LAYOUTLIST", null, null);
      tmp[8] = new NonTerminalStackNode<IConstructor>(-4721, 8, "$Expression", null, null);
      tmp[7] = new NonTerminalStackNode<IConstructor>(-4719, 7, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new LiteralStackNode<IConstructor>(-4717, 6, prod__lit___61__char_class___range__61_61_, new int[] {61}, null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(-4716, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new NonTerminalStackNode<IConstructor>(-4713, 4, "$Signature", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-4711, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(-4708, 2, "$Visibility", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-4706, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-4703, 0, "$Tags", null, null);
      builder.addAlternative(RascalParser.prod__conditional_$FunctionDeclaration__tags_$Tags_$layouts_LAYOUTLIST_visibility_$Visibility_$layouts_LAYOUTLIST_signature_$Signature_$layouts_LAYOUTLIST_lit___61_$layouts_LAYOUTLIST_expression_$Expression_$layouts_LAYOUTLIST_lit_when_$layouts_LAYOUTLIST_conditions_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___59__tag__Foldable_tag__breakable___123_101_120_112_114_101_115_115_105_111_110_44_99_111_110_100_105_116_105_111_110_115_125, tmp);
	}
    protected static final void _init_prod__default_$FunctionDeclaration__tags_$Tags_$layouts_LAYOUTLIST_visibility_$Visibility_$layouts_LAYOUTLIST_signature_$Signature_$layouts_LAYOUTLIST_body_$FunctionBody__tag__Foldable(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[7];
      
      tmp[6] = new NonTerminalStackNode<IConstructor>(-4697, 6, "$FunctionBody", null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(-4695, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new NonTerminalStackNode<IConstructor>(-4692, 4, "$Signature", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-4690, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(-4687, 2, "$Visibility", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-4685, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-4682, 0, "$Tags", null, null);
      builder.addAlternative(RascalParser.prod__default_$FunctionDeclaration__tags_$Tags_$layouts_LAYOUTLIST_visibility_$Visibility_$layouts_LAYOUTLIST_signature_$Signature_$layouts_LAYOUTLIST_body_$FunctionBody__tag__Foldable, tmp);
	}
    protected static final void _init_prod__expression_$FunctionDeclaration__tags_$Tags_$layouts_LAYOUTLIST_visibility_$Visibility_$layouts_LAYOUTLIST_signature_$Signature_$layouts_LAYOUTLIST_lit___61_$layouts_LAYOUTLIST_expression_$Expression_$layouts_LAYOUTLIST_lit___59__tag__Foldable_tag__breakable___123_101_120_112_114_101_115_115_105_111_110_125(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[11];
      
      tmp[10] = new LiteralStackNode<IConstructor>(-4658, 10, prod__lit___59__char_class___range__59_59_, new int[] {59}, null, null);
      tmp[9] = new NonTerminalStackNode<IConstructor>(-4657, 9, "$layouts_LAYOUTLIST", null, null);
      tmp[8] = new NonTerminalStackNode<IConstructor>(-4654, 8, "$Expression", null, null);
      tmp[7] = new NonTerminalStackNode<IConstructor>(-4652, 7, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new LiteralStackNode<IConstructor>(-4650, 6, prod__lit___61__char_class___range__61_61_, new int[] {61}, null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(-4649, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new NonTerminalStackNode<IConstructor>(-4646, 4, "$Signature", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-4644, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(-4641, 2, "$Visibility", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-4639, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-4636, 0, "$Tags", null, null);
      builder.addAlternative(RascalParser.prod__expression_$FunctionDeclaration__tags_$Tags_$layouts_LAYOUTLIST_visibility_$Visibility_$layouts_LAYOUTLIST_signature_$Signature_$layouts_LAYOUTLIST_lit___61_$layouts_LAYOUTLIST_expression_$Expression_$layouts_LAYOUTLIST_lit___59__tag__Foldable_tag__breakable___123_101_120_112_114_101_115_115_105_111_110_125, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__abstract_$FunctionDeclaration__tags_$Tags_$layouts_LAYOUTLIST_visibility_$Visibility_$layouts_LAYOUTLIST_signature_$Signature_$layouts_LAYOUTLIST_lit___59_(builder);
      
        _init_prod__conditional_$FunctionDeclaration__tags_$Tags_$layouts_LAYOUTLIST_visibility_$Visibility_$layouts_LAYOUTLIST_signature_$Signature_$layouts_LAYOUTLIST_lit___61_$layouts_LAYOUTLIST_expression_$Expression_$layouts_LAYOUTLIST_lit_when_$layouts_LAYOUTLIST_conditions_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___59__tag__Foldable_tag__breakable___123_101_120_112_114_101_115_115_105_111_110_44_99_111_110_100_105_116_105_111_110_115_125(builder);
      
        _init_prod__default_$FunctionDeclaration__tags_$Tags_$layouts_LAYOUTLIST_visibility_$Visibility_$layouts_LAYOUTLIST_signature_$Signature_$layouts_LAYOUTLIST_body_$FunctionBody__tag__Foldable(builder);
      
        _init_prod__expression_$FunctionDeclaration__tags_$Tags_$layouts_LAYOUTLIST_visibility_$Visibility_$layouts_LAYOUTLIST_signature_$Signature_$layouts_LAYOUTLIST_lit___61_$layouts_LAYOUTLIST_expression_$Expression_$layouts_LAYOUTLIST_lit___59__tag__Foldable_tag__breakable___123_101_120_112_114_101_115_115_105_111_110_125(builder);
      
    }
  }
	
  protected static class $HexIntegerLiteral {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__$HexIntegerLiteral__char_class___range__48_48_char_class___range__88_88_range__120_120_iter__char_class___range__48_57_range__65_70_range__97_102_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new ListStackNode<IConstructor>(-4407, 2, regular__iter__char_class___range__48_57_range__65_70_range__97_102, new CharStackNode<IConstructor>(-4404, 0, new int[][]{{48,57},{65,70},{97,102}}, null, null), true, null, new ICompletionFilter[] {new CharFollowRestriction(new int[][]{{48,57},{65,90},{95,95},{97,122}})});
      tmp[1] = new CharStackNode<IConstructor>(-4403, 1, new int[][]{{88,88},{120,120}}, null, null);
      tmp[0] = new CharStackNode<IConstructor>(-4402, 0, new int[][]{{48,48}}, null, null);
      builder.addAlternative(RascalParser.prod__$HexIntegerLiteral__char_class___range__48_48_char_class___range__88_88_range__120_120_iter__char_class___range__48_57_range__65_70_range__97_102_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__$HexIntegerLiteral__char_class___range__48_48_char_class___range__88_88_range__120_120_iter__char_class___range__48_57_range__65_70_range__97_102_(builder);
      
    }
  }
	
  protected static class $ModuleActuals {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__default_$ModuleActuals__lit___91_$layouts_LAYOUTLIST_types_iter_seps__$Type__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___93_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new LiteralStackNode<IConstructor>(-4450, 4, prod__lit___93__char_class___range__93_93_, new int[] {93}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-4449, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new SeparatedListStackNode<IConstructor>(-4446, 2, regular__iter_seps__$Type__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(-4440, 0, "$Type", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(-4442, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode<IConstructor>(-4443, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode<IConstructor>(-4445, 3, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-4438, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-4436, 0, prod__lit___91__char_class___range__91_91_, new int[] {91}, null, null);
      builder.addAlternative(RascalParser.prod__default_$ModuleActuals__lit___91_$layouts_LAYOUTLIST_types_iter_seps__$Type__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___93_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__default_$ModuleActuals__lit___91_$layouts_LAYOUTLIST_types_iter_seps__$Type__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___93_(builder);
      
    }
  }
	
  protected static class $PathChars {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__$PathChars__$URLChars_char_class___range__124_124_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[2];
      
      tmp[1] = new CharStackNode<IConstructor>(-4459, 1, new int[][]{{124,124}}, null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-4458, 0, "$URLChars", null, null);
      builder.addAlternative(RascalParser.prod__$PathChars__$URLChars_char_class___range__124_124_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__$PathChars__$URLChars_char_class___range__124_124_(builder);
      
    }
  }
	
  protected static class $PostStringChars {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__$PostStringChars__char_class___range__62_62_iter_star__$StringCharacter_char_class___range__34_34__tag__category___67_111_110_115_116_97_110_116(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new CharStackNode<IConstructor>(-4395, 2, new int[][]{{34,34}}, null, null);
      tmp[1] = new ListStackNode<IConstructor>(-4394, 1, regular__iter_star__$StringCharacter, new NonTerminalStackNode<IConstructor>(-4393, 0, "$StringCharacter", null, null), false, null, null);
      tmp[0] = new CharStackNode<IConstructor>(-4391, 0, new int[][]{{62,62}}, null, null);
      builder.addAlternative(RascalParser.prod__$PostStringChars__char_class___range__62_62_iter_star__$StringCharacter_char_class___range__34_34__tag__category___67_111_110_115_116_97_110_116, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__$PostStringChars__char_class___range__62_62_iter_star__$StringCharacter_char_class___range__34_34__tag__category___67_111_110_115_116_97_110_116(builder);
      
    }
  }
	
  protected static class start__$EvalCommand {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__start__$EvalCommand__$layouts_LAYOUTLIST_top_$EvalCommand_$layouts_LAYOUTLIST_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode<IConstructor>(-4428, 2, "$layouts_LAYOUTLIST", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-4425, 1, "$EvalCommand", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-4423, 0, "$layouts_LAYOUTLIST", null, null);
      builder.addAlternative(RascalParser.prod__start__$EvalCommand__$layouts_LAYOUTLIST_top_$EvalCommand_$layouts_LAYOUTLIST_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__start__$EvalCommand__$layouts_LAYOUTLIST_top_$EvalCommand_$layouts_LAYOUTLIST_(builder);
      
    }
  }
	
  protected static class $Formals {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__default_$Formals__formals_iter_star_seps__$Pattern__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new SeparatedListStackNode<IConstructor>(-5075, 0, regular__iter_star_seps__$Pattern__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(-5069, 0, "$Pattern", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(-5071, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode<IConstructor>(-5072, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode<IConstructor>(-5074, 3, "$layouts_LAYOUTLIST", null, null)}, false, null, null);
      builder.addAlternative(RascalParser.prod__default_$Formals__formals_iter_star_seps__$Pattern__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__default_$Formals__formals_iter_star_seps__$Pattern__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_(builder);
      
    }
  }
	
  protected static class $Variant {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__nAryConstructor_$Variant__name_$Name_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_arguments_iter_star_seps__$TypeArg__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[7];
      
      tmp[6] = new LiteralStackNode<IConstructor>(-4796, 6, prod__lit___41__char_class___range__41_41_, new int[] {41}, null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(-4795, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new SeparatedListStackNode<IConstructor>(-4792, 4, regular__iter_star_seps__$TypeArg__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(-4786, 0, "$TypeArg", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(-4788, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode<IConstructor>(-4789, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode<IConstructor>(-4791, 3, "$layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-4784, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(-4782, 2, prod__lit___40__char_class___range__40_40_, new int[] {40}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-4781, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-4778, 0, "$Name", null, null);
      builder.addAlternative(RascalParser.prod__nAryConstructor_$Variant__name_$Name_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_arguments_iter_star_seps__$TypeArg__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__nAryConstructor_$Variant__name_$Name_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_arguments_iter_star_seps__$TypeArg__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_(builder);
      
    }
  }
	
  protected static class $Declarator {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__default_$Declarator__type_$Type_$layouts_LAYOUTLIST_variables_iter_seps__$Variable__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new SeparatedListStackNode<IConstructor>(-4869, 2, regular__iter_seps__$Variable__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(-4863, 0, "$Variable", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(-4865, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode<IConstructor>(-4866, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode<IConstructor>(-4868, 3, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-4861, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-4858, 0, "$Type", null, null);
      builder.addAlternative(RascalParser.prod__default_$Declarator__type_$Type_$layouts_LAYOUTLIST_variables_iter_seps__$Variable__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__default_$Declarator__type_$Type_$layouts_LAYOUTLIST_variables_iter_seps__$Variable__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_(builder);
      
    }
  }
	
  protected static class $Name {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__$Name__seq___char_class___range__65_90_range__95_95_range__97_122_iter_star__char_class___range__48_57_range__65_90_range__95_95_range__97_122_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new SequenceStackNode<IConstructor>(-5026, 0, regular__seq___char_class___range__65_90_range__95_95_range__97_122_iter_star__char_class___range__48_57_range__65_90_range__95_95_range__97_122, (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new CharStackNode<IConstructor>(-5018, 0, new int[][]{{65,90},{95,95},{97,122}}, new IEnterFilter[] {new CharPrecedeRestriction(new int[][]{{65,90},{95,95},{97,122}})}, null), new ListStackNode<IConstructor>(-5022, 1, regular__iter_star__char_class___range__48_57_range__65_90_range__95_95_range__97_122, new CharStackNode<IConstructor>(-5019, 0, new int[][]{{48,57},{65,90},{95,95},{97,122}}, null, null), false, null, new ICompletionFilter[] {new CharFollowRestriction(new int[][]{{48,57},{65,90},{95,95},{97,122}})})}, null, new ICompletionFilter[] {new StringMatchRestriction(new int[] {114,97,116}), new StringMatchRestriction(new int[] {105,110}), new StringMatchRestriction(new int[] {105,109,112,111,114,116}), new StringMatchRestriction(new int[] {99,111,110,116,105,110,117,101}), new StringMatchRestriction(new int[] {97,108,108}), new StringMatchRestriction(new int[] {102,97,108,115,101}), new StringMatchRestriction(new int[] {108,114,101,108}), new StringMatchRestriction(new int[] {97,110,110,111}), new StringMatchRestriction(new int[] {98,114,97,99,107,101,116}), new StringMatchRestriction(new int[] {100,97,116,97}), new StringMatchRestriction(new int[] {106,111,105,110}), new StringMatchRestriction(new int[] {108,97,121,111,117,116}), new StringMatchRestriction(new int[] {105,116}), new StringMatchRestriction(new int[] {115,119,105,116,99,104}), new StringMatchRestriction(new int[] {99,97,115,101}), new StringMatchRestriction(new int[] {114,101,116,117,114,110}), new StringMatchRestriction(new int[] {119,104,105,108,101}), new StringMatchRestriction(new int[] {115,116,114}), new StringMatchRestriction(new int[] {115,111,108,118,101}), new StringMatchRestriction(new int[] {100,121,110,97,109,105,99}), new StringMatchRestriction(new int[] {110,111,116,105,110}), new StringMatchRestriction(new int[] {101,108,115,101}), new StringMatchRestriction(new int[] {105,110,115,101,114,116}), new StringMatchRestriction(new int[] {116,121,112,101}), new StringMatchRestriction(new int[] {116,114,121}), new StringMatchRestriction(new int[] {99,97,116,99,104}), new StringMatchRestriction(new int[] {110,117,109}), new StringMatchRestriction(new int[] {110,111,100,101}), new StringMatchRestriction(new int[] {109,111,100}), new StringMatchRestriction(new int[] {112,114,105,118,97,116,101}), new StringMatchRestriction(new int[] {102,105,110,97,108,108,121}), new StringMatchRestriction(new int[] {116,114,117,101}), new StringMatchRestriction(new int[] {98,97,103}), new StringMatchRestriction(new int[] {118,111,105,100}), new StringMatchRestriction(new int[] {97,115,115,111,99}), new StringMatchRestriction(new int[] {110,111,110,45,97,115,115,111,99}), new StringMatchRestriction(new int[] {116,101,115,116}), new StringMatchRestriction(new int[] {105,102}), new StringMatchRestriction(new int[] {114,101,97,108}), new StringMatchRestriction(new int[] {108,105,115,116}), new StringMatchRestriction(new int[] {102,97,105,108}), new StringMatchRestriction(new int[] {114,101,108}), new StringMatchRestriction(new int[] {97,112,112,101,110,100}), new StringMatchRestriction(new int[] {101,120,116,101,110,100}), new StringMatchRestriction(new int[] {116,97,103}), new StringMatchRestriction(new int[] {111,110,101}), new StringMatchRestriction(new int[] {116,104,114,111,119}), new StringMatchRestriction(new int[] {115,101,116}), new StringMatchRestriction(new int[] {115,116,97,114,116}), new StringMatchRestriction(new int[] {97,110,121}), new StringMatchRestriction(new int[] {109,111,100,117,108,101}), new StringMatchRestriction(new int[] {111}), new StringMatchRestriction(new int[] {105,110,116}), new StringMatchRestriction(new int[] {112,117,98,108,105,99}), new StringMatchRestriction(new int[] {98,111,111,108}), new StringMatchRestriction(new int[] {118,97,108,117,101}), new StringMatchRestriction(new int[] {98,114,101,97,107}), new StringMatchRestriction(new int[] {102,105,108,116,101,114}), new StringMatchRestriction(new int[] {100,97,116,101,116,105,109,101}), new StringMatchRestriction(new int[] {97,115,115,101,114,116}), new StringMatchRestriction(new int[] {108,111,99}), new StringMatchRestriction(new int[] {100,101,102,97,117,108,116}), new StringMatchRestriction(new int[] {116,104,114,111,119,115}), new StringMatchRestriction(new int[] {116,117,112,108,101}), new StringMatchRestriction(new int[] {102,111,114}), new StringMatchRestriction(new int[] {118,105,115,105,116}), new StringMatchRestriction(new int[] {97,108,105,97,115}), new StringMatchRestriction(new int[] {109,97,112})});
      builder.addAlternative(RascalParser.prod__$Name__seq___char_class___range__65_90_range__95_95_range__97_122_iter_star__char_class___range__48_57_range__65_90_range__95_95_range__97_122_, tmp);
	}
    protected static final void _init_prod__$Name__char_class___range__92_92_char_class___range__65_90_range__95_95_range__97_122_iter_star__char_class___range__45_45_range__48_57_range__65_90_range__95_95_range__97_122_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new ListStackNode<IConstructor>(-5034, 2, regular__iter_star__char_class___range__45_45_range__48_57_range__65_90_range__95_95_range__97_122, new CharStackNode<IConstructor>(-5031, 0, new int[][]{{45,45},{48,57},{65,90},{95,95},{97,122}}, null, null), false, null, new ICompletionFilter[] {new CharFollowRestriction(new int[][]{{45,45},{48,57},{65,90},{95,95},{97,122}})});
      tmp[1] = new CharStackNode<IConstructor>(-5030, 1, new int[][]{{65,90},{95,95},{97,122}}, null, null);
      tmp[0] = new CharStackNode<IConstructor>(-5029, 0, new int[][]{{92,92}}, null, null);
      builder.addAlternative(RascalParser.prod__$Name__char_class___range__92_92_char_class___range__65_90_range__95_95_range__97_122_iter_star__char_class___range__45_45_range__48_57_range__65_90_range__95_95_range__97_122_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__$Name__seq___char_class___range__65_90_range__95_95_range__97_122_iter_star__char_class___range__48_57_range__65_90_range__95_95_range__97_122_(builder);
      
        _init_prod__$Name__char_class___range__92_92_char_class___range__65_90_range__95_95_range__97_122_iter_star__char_class___range__45_45_range__48_57_range__65_90_range__95_95_range__97_122_(builder);
      
    }
  }
	
  protected static class $TimePartNoTZ {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__$TimePartNoTZ__char_class___range__48_50_char_class___range__48_57_char_class___range__48_53_char_class___range__48_57_char_class___range__48_53_char_class___range__48_57_opt__seq___char_class___range__44_44_range__46_46_char_class___range__48_57_opt__seq___char_class___range__48_57_opt__char_class___range__48_57_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[7];
      
      tmp[6] = new OptionalStackNode<IConstructor>(-4999, 6, regular__opt__seq___char_class___range__44_44_range__46_46_char_class___range__48_57_opt__seq___char_class___range__48_57_opt__char_class___range__48_57, new SequenceStackNode<IConstructor>(-4998, 0, regular__seq___char_class___range__44_44_range__46_46_char_class___range__48_57_opt__seq___char_class___range__48_57_opt__char_class___range__48_57, (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new CharStackNode<IConstructor>(-4991, 0, new int[][]{{44,44},{46,46}}, null, null), new CharStackNode<IConstructor>(-4992, 1, new int[][]{{48,57}}, null, null), new OptionalStackNode<IConstructor>(-4997, 2, regular__opt__seq___char_class___range__48_57_opt__char_class___range__48_57, new SequenceStackNode<IConstructor>(-4996, 0, regular__seq___char_class___range__48_57_opt__char_class___range__48_57, (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new CharStackNode<IConstructor>(-4993, 0, new int[][]{{48,57}}, null, null), new OptionalStackNode<IConstructor>(-4995, 1, regular__opt__char_class___range__48_57, new CharStackNode<IConstructor>(-4994, 0, new int[][]{{48,57}}, null, null), null, null)}, null, null), null, null)}, null, null), null, null);
      tmp[5] = new CharStackNode<IConstructor>(-4990, 5, new int[][]{{48,57}}, null, null);
      tmp[4] = new CharStackNode<IConstructor>(-4989, 4, new int[][]{{48,53}}, null, null);
      tmp[3] = new CharStackNode<IConstructor>(-4988, 3, new int[][]{{48,57}}, null, null);
      tmp[2] = new CharStackNode<IConstructor>(-4987, 2, new int[][]{{48,53}}, null, null);
      tmp[1] = new CharStackNode<IConstructor>(-4986, 1, new int[][]{{48,57}}, null, null);
      tmp[0] = new CharStackNode<IConstructor>(-4985, 0, new int[][]{{48,50}}, null, null);
      builder.addAlternative(RascalParser.prod__$TimePartNoTZ__char_class___range__48_50_char_class___range__48_57_char_class___range__48_53_char_class___range__48_57_char_class___range__48_53_char_class___range__48_57_opt__seq___char_class___range__44_44_range__46_46_char_class___range__48_57_opt__seq___char_class___range__48_57_opt__char_class___range__48_57_, tmp);
	}
    protected static final void _init_prod__$TimePartNoTZ__char_class___range__48_50_char_class___range__48_57_lit___58_char_class___range__48_53_char_class___range__48_57_lit___58_char_class___range__48_53_char_class___range__48_57_opt__seq___char_class___range__44_44_range__46_46_char_class___range__48_57_opt__seq___char_class___range__48_57_opt__char_class___range__48_57_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[9];
      
      tmp[8] = new OptionalStackNode<IConstructor>(-4982, 8, regular__opt__seq___char_class___range__44_44_range__46_46_char_class___range__48_57_opt__seq___char_class___range__48_57_opt__char_class___range__48_57, new SequenceStackNode<IConstructor>(-4981, 0, regular__seq___char_class___range__44_44_range__46_46_char_class___range__48_57_opt__seq___char_class___range__48_57_opt__char_class___range__48_57, (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new CharStackNode<IConstructor>(-4974, 0, new int[][]{{44,44},{46,46}}, null, null), new CharStackNode<IConstructor>(-4975, 1, new int[][]{{48,57}}, null, null), new OptionalStackNode<IConstructor>(-4980, 2, regular__opt__seq___char_class___range__48_57_opt__char_class___range__48_57, new SequenceStackNode<IConstructor>(-4979, 0, regular__seq___char_class___range__48_57_opt__char_class___range__48_57, (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new CharStackNode<IConstructor>(-4976, 0, new int[][]{{48,57}}, null, null), new OptionalStackNode<IConstructor>(-4978, 1, regular__opt__char_class___range__48_57, new CharStackNode<IConstructor>(-4977, 0, new int[][]{{48,57}}, null, null), null, null)}, null, null), null, null)}, null, null), null, null);
      tmp[7] = new CharStackNode<IConstructor>(-4973, 7, new int[][]{{48,57}}, null, null);
      tmp[6] = new CharStackNode<IConstructor>(-4972, 6, new int[][]{{48,53}}, null, null);
      tmp[5] = new LiteralStackNode<IConstructor>(-4971, 5, prod__lit___58__char_class___range__58_58_, new int[] {58}, null, null);
      tmp[4] = new CharStackNode<IConstructor>(-4970, 4, new int[][]{{48,57}}, null, null);
      tmp[3] = new CharStackNode<IConstructor>(-4969, 3, new int[][]{{48,53}}, null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(-4968, 2, prod__lit___58__char_class___range__58_58_, new int[] {58}, null, null);
      tmp[1] = new CharStackNode<IConstructor>(-4967, 1, new int[][]{{48,57}}, null, null);
      tmp[0] = new CharStackNode<IConstructor>(-4966, 0, new int[][]{{48,50}}, null, null);
      builder.addAlternative(RascalParser.prod__$TimePartNoTZ__char_class___range__48_50_char_class___range__48_57_lit___58_char_class___range__48_53_char_class___range__48_57_lit___58_char_class___range__48_53_char_class___range__48_57_opt__seq___char_class___range__44_44_range__46_46_char_class___range__48_57_opt__seq___char_class___range__48_57_opt__char_class___range__48_57_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__$TimePartNoTZ__char_class___range__48_50_char_class___range__48_57_char_class___range__48_53_char_class___range__48_57_char_class___range__48_53_char_class___range__48_57_opt__seq___char_class___range__44_44_range__46_46_char_class___range__48_57_opt__seq___char_class___range__48_57_opt__char_class___range__48_57_(builder);
      
        _init_prod__$TimePartNoTZ__char_class___range__48_50_char_class___range__48_57_lit___58_char_class___range__48_53_char_class___range__48_57_lit___58_char_class___range__48_53_char_class___range__48_57_opt__seq___char_class___range__44_44_range__46_46_char_class___range__48_57_opt__seq___char_class___range__48_57_opt__char_class___range__48_57_(builder);
      
    }
  }
	
  protected static class $Start {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__absent_$Start__(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new EpsilonStackNode<IConstructor>(-5041, 0);
      builder.addAlternative(RascalParser.prod__absent_$Start__, tmp);
	}
    protected static final void _init_prod__present_$Start__lit_start_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-5045, 0, prod__lit_start__char_class___range__115_115_char_class___range__116_116_char_class___range__97_97_char_class___range__114_114_char_class___range__116_116_, new int[] {115,116,97,114,116}, null, null);
      builder.addAlternative(RascalParser.prod__present_$Start__lit_start_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__absent_$Start__(builder);
      
        _init_prod__present_$Start__lit_start_(builder);
      
    }
  }
	
  protected static class $StructuredType {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__default_$StructuredType__basicType_$BasicType_$layouts_LAYOUTLIST_lit___91_$layouts_LAYOUTLIST_arguments_iter_seps__$TypeArg__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___93_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[7];
      
      tmp[6] = new LiteralStackNode<IConstructor>(-4948, 6, prod__lit___93__char_class___range__93_93_, new int[] {93}, null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(-4947, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new SeparatedListStackNode<IConstructor>(-4944, 4, regular__iter_seps__$TypeArg__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(-4938, 0, "$TypeArg", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(-4940, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode<IConstructor>(-4941, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode<IConstructor>(-4943, 3, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-4936, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(-4934, 2, prod__lit___91__char_class___range__91_91_, new int[] {91}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-4933, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-4930, 0, "$BasicType", null, null);
      builder.addAlternative(RascalParser.prod__default_$StructuredType__basicType_$BasicType_$layouts_LAYOUTLIST_lit___91_$layouts_LAYOUTLIST_arguments_iter_seps__$TypeArg__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___93_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__default_$StructuredType__basicType_$BasicType_$layouts_LAYOUTLIST_lit___91_$layouts_LAYOUTLIST_arguments_iter_seps__$TypeArg__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___93_(builder);
      
    }
  }
	
  protected static class $Backslash {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__$Backslash__char_class___range__92_92_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new CharStackNode<IConstructor>(-5125, 0, new int[][]{{92,92}}, null, new ICompletionFilter[] {new CharFollowRestriction(new int[][]{{47,47},{60,60},{62,62},{92,92}})});
      builder.addAlternative(RascalParser.prod__$Backslash__char_class___range__92_92_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__$Backslash__char_class___range__92_92_(builder);
      
    }
  }
	
  protected static class $CaseInsensitiveStringConstant {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__$CaseInsensitiveStringConstant__lit___39_iter_star__$StringCharacter_lit___39__tag__category___67_111_110_115_116_97_110_116(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new LiteralStackNode<IConstructor>(-5116, 2, prod__lit___39__char_class___range__39_39_, new int[] {39}, null, null);
      tmp[1] = new ListStackNode<IConstructor>(-5115, 1, regular__iter_star__$StringCharacter, new NonTerminalStackNode<IConstructor>(-5114, 0, "$StringCharacter", null, null), false, null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-5112, 0, prod__lit___39__char_class___range__39_39_, new int[] {39}, null, null);
      builder.addAlternative(RascalParser.prod__$CaseInsensitiveStringConstant__lit___39_iter_star__$StringCharacter_lit___39__tag__category___67_111_110_115_116_97_110_116, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__$CaseInsensitiveStringConstant__lit___39_iter_star__$StringCharacter_lit___39__tag__category___67_111_110_115_116_97_110_116(builder);
      
    }
  }
	
  protected static class $DataTypeSelector {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__selector_$DataTypeSelector__sort_$QualifiedName_$layouts_LAYOUTLIST_lit___46_$layouts_LAYOUTLIST_production_$Name_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(-5300, 4, "$Name", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-5298, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(-5296, 2, prod__lit___46__char_class___range__46_46_, new int[] {46}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-5295, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-5292, 0, "$QualifiedName", null, null);
      builder.addAlternative(RascalParser.prod__selector_$DataTypeSelector__sort_$QualifiedName_$layouts_LAYOUTLIST_lit___46_$layouts_LAYOUTLIST_production_$Name_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__selector_$DataTypeSelector__sort_$QualifiedName_$layouts_LAYOUTLIST_lit___46_$layouts_LAYOUTLIST_production_$Name_(builder);
      
    }
  }
	
  protected static class $JustDate {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__$JustDate__lit___36_$DatePart_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[2];
      
      tmp[1] = new NonTerminalStackNode<IConstructor>(-5164, 1, "$DatePart", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-5162, 0, prod__lit___36__char_class___range__36_36_, new int[] {36}, null, null);
      builder.addAlternative(RascalParser.prod__$JustDate__lit___36_$DatePart_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__$JustDate__lit___36_$DatePart_(builder);
      
    }
  }
	
  protected static class $MidProtocolChars {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__$MidProtocolChars__lit___62_$URLChars_lit___60_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new LiteralStackNode<IConstructor>(-5187, 2, prod__lit___60__char_class___range__60_60_, new int[] {60}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-5186, 1, "$URLChars", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-5184, 0, prod__lit___62__char_class___range__62_62_, new int[] {62}, null, null);
      builder.addAlternative(RascalParser.prod__$MidProtocolChars__lit___62_$URLChars_lit___60_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__$MidProtocolChars__lit___62_$URLChars_lit___60_(builder);
      
    }
  }
	
  protected static class $Tags {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__default_$Tags__tags_iter_star_seps__$Tag__$layouts_LAYOUTLIST_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new SeparatedListStackNode<IConstructor>(-5088, 0, regular__iter_star_seps__$Tag__$layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(-5085, 0, "$Tag", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(-5087, 1, "$layouts_LAYOUTLIST", null, null)}, false, null, null);
      builder.addAlternative(RascalParser.prod__default_$Tags__tags_iter_star_seps__$Tag__$layouts_LAYOUTLIST_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__default_$Tags__tags_iter_star_seps__$Tag__$layouts_LAYOUTLIST_(builder);
      
    }
  }
	
  protected static class start__$Commands {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__start__$Commands__$layouts_LAYOUTLIST_top_$Commands_$layouts_LAYOUTLIST_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode<IConstructor>(-5105, 2, "$layouts_LAYOUTLIST", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-5102, 1, "$Commands", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-5100, 0, "$layouts_LAYOUTLIST", null, null);
      builder.addAlternative(RascalParser.prod__start__$Commands__$layouts_LAYOUTLIST_top_$Commands_$layouts_LAYOUTLIST_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__start__$Commands__$layouts_LAYOUTLIST_top_$Commands_$layouts_LAYOUTLIST_(builder);
      
    }
  }
	
  protected static class $PathTail {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__mid_$PathTail__mid_$MidPathChars_$layouts_LAYOUTLIST_expression_$Expression_$layouts_LAYOUTLIST_tail_$PathTail_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(-5206, 4, "$PathTail", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-5204, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(-5201, 2, "$Expression", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-5199, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-5196, 0, "$MidPathChars", null, null);
      builder.addAlternative(RascalParser.prod__mid_$PathTail__mid_$MidPathChars_$layouts_LAYOUTLIST_expression_$Expression_$layouts_LAYOUTLIST_tail_$PathTail_, tmp);
	}
    protected static final void _init_prod__post_$PathTail__post_$PostPathChars_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(-5212, 0, "$PostPathChars", null, null);
      builder.addAlternative(RascalParser.prod__post_$PathTail__post_$PostPathChars_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__mid_$PathTail__mid_$MidPathChars_$layouts_LAYOUTLIST_expression_$Expression_$layouts_LAYOUTLIST_tail_$PathTail_(builder);
      
        _init_prod__post_$PathTail__post_$PostPathChars_(builder);
      
    }
  }
	
  protected static class $PatternWithAction {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__arbitrary_$PatternWithAction__pattern_$Pattern_$layouts_LAYOUTLIST_lit___58_$layouts_LAYOUTLIST_statement_$Statement_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(-5268, 4, "$Statement", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-5266, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(-5264, 2, prod__lit___58__char_class___range__58_58_, new int[] {58}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-5263, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-5260, 0, "$Pattern", null, null);
      builder.addAlternative(RascalParser.prod__arbitrary_$PatternWithAction__pattern_$Pattern_$layouts_LAYOUTLIST_lit___58_$layouts_LAYOUTLIST_statement_$Statement_, tmp);
	}
    protected static final void _init_prod__replacing_$PatternWithAction__pattern_$Pattern_$layouts_LAYOUTLIST_lit___61_62_$layouts_LAYOUTLIST_replacement_$Replacement_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(-5282, 4, "$Replacement", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-5280, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(-5278, 2, prod__lit___61_62__char_class___range__61_61_char_class___range__62_62_, new int[] {61,62}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-5277, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-5274, 0, "$Pattern", null, null);
      builder.addAlternative(RascalParser.prod__replacing_$PatternWithAction__pattern_$Pattern_$layouts_LAYOUTLIST_lit___61_62_$layouts_LAYOUTLIST_replacement_$Replacement_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__arbitrary_$PatternWithAction__pattern_$Pattern_$layouts_LAYOUTLIST_lit___58_$layouts_LAYOUTLIST_statement_$Statement_(builder);
      
        _init_prod__replacing_$PatternWithAction__pattern_$Pattern_$layouts_LAYOUTLIST_lit___61_62_$layouts_LAYOUTLIST_replacement_$Replacement_(builder);
      
    }
  }
	
  protected static class $StringTail {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__midInterpolated_$StringTail__mid_$MidStringChars_$layouts_LAYOUTLIST_expression_$Expression_$layouts_LAYOUTLIST_tail_$StringTail_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(-5342, 4, "$StringTail", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-5340, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(-5337, 2, "$Expression", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-5335, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-5332, 0, "$MidStringChars", null, null);
      builder.addAlternative(RascalParser.prod__midInterpolated_$StringTail__mid_$MidStringChars_$layouts_LAYOUTLIST_expression_$Expression_$layouts_LAYOUTLIST_tail_$StringTail_, tmp);
	}
    protected static final void _init_prod__midTemplate_$StringTail__mid_$MidStringChars_$layouts_LAYOUTLIST_template_$StringTemplate_$layouts_LAYOUTLIST_tail_$StringTail_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(-5326, 4, "$StringTail", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-5324, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(-5321, 2, "$StringTemplate", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-5319, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-5316, 0, "$MidStringChars", null, null);
      builder.addAlternative(RascalParser.prod__midTemplate_$StringTail__mid_$MidStringChars_$layouts_LAYOUTLIST_template_$StringTemplate_$layouts_LAYOUTLIST_tail_$StringTail_, tmp);
	}
    protected static final void _init_prod__post_$StringTail__post_$PostStringChars_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(-5310, 0, "$PostStringChars", null, null);
      builder.addAlternative(RascalParser.prod__post_$StringTail__post_$PostStringChars_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__midInterpolated_$StringTail__mid_$MidStringChars_$layouts_LAYOUTLIST_expression_$Expression_$layouts_LAYOUTLIST_tail_$StringTail_(builder);
      
        _init_prod__midTemplate_$StringTail__mid_$MidStringChars_$layouts_LAYOUTLIST_template_$StringTemplate_$layouts_LAYOUTLIST_tail_$StringTail_(builder);
      
        _init_prod__post_$StringTail__post_$PostStringChars_(builder);
      
    }
  }
	
  protected static class start__$Module {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__start__$Module__$layouts_LAYOUTLIST_top_$Module_$layouts_LAYOUTLIST_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode<IConstructor>(-5359, 2, "$layouts_LAYOUTLIST", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-5356, 1, "$Module", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-5354, 0, "$layouts_LAYOUTLIST", null, null);
      builder.addAlternative(RascalParser.prod__start__$Module__$layouts_LAYOUTLIST_top_$Module_$layouts_LAYOUTLIST_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__start__$Module__$layouts_LAYOUTLIST_top_$Module_$layouts_LAYOUTLIST_(builder);
      
    }
  }
	
  protected static class $DatePart {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__$DatePart__char_class___range__48_57_char_class___range__48_57_char_class___range__48_57_char_class___range__48_57_char_class___range__48_49_char_class___range__48_57_char_class___range__48_51_char_class___range__48_57_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[8];
      
      tmp[7] = new CharStackNode<IConstructor>(-5534, 7, new int[][]{{48,57}}, null, null);
      tmp[6] = new CharStackNode<IConstructor>(-5533, 6, new int[][]{{48,51}}, null, null);
      tmp[5] = new CharStackNode<IConstructor>(-5532, 5, new int[][]{{48,57}}, null, null);
      tmp[4] = new CharStackNode<IConstructor>(-5531, 4, new int[][]{{48,49}}, null, null);
      tmp[3] = new CharStackNode<IConstructor>(-5530, 3, new int[][]{{48,57}}, null, null);
      tmp[2] = new CharStackNode<IConstructor>(-5529, 2, new int[][]{{48,57}}, null, null);
      tmp[1] = new CharStackNode<IConstructor>(-5528, 1, new int[][]{{48,57}}, null, null);
      tmp[0] = new CharStackNode<IConstructor>(-5527, 0, new int[][]{{48,57}}, null, null);
      builder.addAlternative(RascalParser.prod__$DatePart__char_class___range__48_57_char_class___range__48_57_char_class___range__48_57_char_class___range__48_57_char_class___range__48_49_char_class___range__48_57_char_class___range__48_51_char_class___range__48_57_, tmp);
	}
    protected static final void _init_prod__$DatePart__char_class___range__48_57_char_class___range__48_57_char_class___range__48_57_char_class___range__48_57_lit___char_class___range__48_49_char_class___range__48_57_lit___char_class___range__48_51_char_class___range__48_57_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[10];
      
      tmp[9] = new CharStackNode<IConstructor>(-5546, 9, new int[][]{{48,57}}, null, null);
      tmp[8] = new CharStackNode<IConstructor>(-5545, 8, new int[][]{{48,51}}, null, null);
      tmp[7] = new LiteralStackNode<IConstructor>(-5544, 7, prod__lit____char_class___range__45_45_, new int[] {45}, null, null);
      tmp[6] = new CharStackNode<IConstructor>(-5543, 6, new int[][]{{48,57}}, null, null);
      tmp[5] = new CharStackNode<IConstructor>(-5542, 5, new int[][]{{48,49}}, null, null);
      tmp[4] = new LiteralStackNode<IConstructor>(-5541, 4, prod__lit____char_class___range__45_45_, new int[] {45}, null, null);
      tmp[3] = new CharStackNode<IConstructor>(-5540, 3, new int[][]{{48,57}}, null, null);
      tmp[2] = new CharStackNode<IConstructor>(-5539, 2, new int[][]{{48,57}}, null, null);
      tmp[1] = new CharStackNode<IConstructor>(-5538, 1, new int[][]{{48,57}}, null, null);
      tmp[0] = new CharStackNode<IConstructor>(-5537, 0, new int[][]{{48,57}}, null, null);
      builder.addAlternative(RascalParser.prod__$DatePart__char_class___range__48_57_char_class___range__48_57_char_class___range__48_57_char_class___range__48_57_lit___char_class___range__48_49_char_class___range__48_57_lit___char_class___range__48_51_char_class___range__48_57_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__$DatePart__char_class___range__48_57_char_class___range__48_57_char_class___range__48_57_char_class___range__48_57_char_class___range__48_49_char_class___range__48_57_char_class___range__48_51_char_class___range__48_57_(builder);
      
        _init_prod__$DatePart__char_class___range__48_57_char_class___range__48_57_char_class___range__48_57_char_class___range__48_57_lit___char_class___range__48_49_char_class___range__48_57_lit___char_class___range__48_51_char_class___range__48_57_(builder);
      
    }
  }
	
  protected static class $LocalVariableDeclaration {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__default_$LocalVariableDeclaration__declarator_$Declarator_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(-5435, 0, "$Declarator", null, null);
      builder.addAlternative(RascalParser.prod__default_$LocalVariableDeclaration__declarator_$Declarator_, tmp);
	}
    protected static final void _init_prod__dynamic_$LocalVariableDeclaration__lit_dynamic_$layouts_LAYOUTLIST_declarator_$Declarator_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode<IConstructor>(-5429, 2, "$Declarator", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-5427, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-5425, 0, prod__lit_dynamic__char_class___range__100_100_char_class___range__121_121_char_class___range__110_110_char_class___range__97_97_char_class___range__109_109_char_class___range__105_105_char_class___range__99_99_, new int[] {100,121,110,97,109,105,99}, null, null);
      builder.addAlternative(RascalParser.prod__dynamic_$LocalVariableDeclaration__lit_dynamic_$layouts_LAYOUTLIST_declarator_$Declarator_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__default_$LocalVariableDeclaration__declarator_$Declarator_(builder);
      
        _init_prod__dynamic_$LocalVariableDeclaration__lit_dynamic_$layouts_LAYOUTLIST_declarator_$Declarator_(builder);
      
    }
  }
	
  protected static class $StringConstant {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__$StringConstant__lit___34_iter_star__$StringCharacter_lit___34__tag__category___67_111_110_115_116_97_110_116(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new LiteralStackNode<IConstructor>(-5394, 2, prod__lit___34__char_class___range__34_34_, new int[] {34}, null, null);
      tmp[1] = new ListStackNode<IConstructor>(-5393, 1, regular__iter_star__$StringCharacter, new NonTerminalStackNode<IConstructor>(-5392, 0, "$StringCharacter", null, null), false, null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-5390, 0, prod__lit___34__char_class___range__34_34_, new int[] {34}, null, null);
      builder.addAlternative(RascalParser.prod__$StringConstant__lit___34_iter_star__$StringCharacter_lit___34__tag__category___67_111_110_115_116_97_110_116, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__$StringConstant__lit___34_iter_star__$StringCharacter_lit___34__tag__category___67_111_110_115_116_97_110_116(builder);
      
    }
  }
	
  protected static class $Parameters {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__default_$Parameters__lit___40_$layouts_LAYOUTLIST_formals_$Formals_$layouts_LAYOUTLIST_lit___41_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new LiteralStackNode<IConstructor>(-5505, 4, prod__lit___41__char_class___range__41_41_, new int[] {41}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-5504, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(-5501, 2, "$Formals", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-5499, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-5497, 0, prod__lit___40__char_class___range__40_40_, new int[] {40}, null, null);
      builder.addAlternative(RascalParser.prod__default_$Parameters__lit___40_$layouts_LAYOUTLIST_formals_$Formals_$layouts_LAYOUTLIST_lit___41_, tmp);
	}
    protected static final void _init_prod__varArgs_$Parameters__lit___40_$layouts_LAYOUTLIST_formals_$Formals_$layouts_LAYOUTLIST_lit___46_46_46_$layouts_LAYOUTLIST_lit___41_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[7];
      
      tmp[6] = new LiteralStackNode<IConstructor>(-5520, 6, prod__lit___41__char_class___range__41_41_, new int[] {41}, null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(-5519, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode<IConstructor>(-5517, 4, prod__lit___46_46_46__char_class___range__46_46_char_class___range__46_46_char_class___range__46_46_, new int[] {46,46,46}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-5516, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(-5513, 2, "$Formals", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-5511, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-5509, 0, prod__lit___40__char_class___range__40_40_, new int[] {40}, null, null);
      builder.addAlternative(RascalParser.prod__varArgs_$Parameters__lit___40_$layouts_LAYOUTLIST_formals_$Formals_$layouts_LAYOUTLIST_lit___46_46_46_$layouts_LAYOUTLIST_lit___41_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__default_$Parameters__lit___40_$layouts_LAYOUTLIST_formals_$Formals_$layouts_LAYOUTLIST_lit___41_(builder);
      
        _init_prod__varArgs_$Parameters__lit___40_$layouts_LAYOUTLIST_formals_$Formals_$layouts_LAYOUTLIST_lit___46_46_46_$layouts_LAYOUTLIST_lit___41_(builder);
      
    }
  }
	
  protected static class $Assignable {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__annotation_$Assignable__receiver_$Assignable_$layouts_LAYOUTLIST_lit___64_$layouts_LAYOUTLIST_annotation_$Name_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(-5715, 4, "$Name", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-5713, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(-5711, 2, prod__lit___64__char_class___range__64_64_, new int[] {64}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-5710, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-5707, 0, "$Assignable", null, null);
      builder.addAlternative(RascalParser.prod__annotation_$Assignable__receiver_$Assignable_$layouts_LAYOUTLIST_lit___64_$layouts_LAYOUTLIST_annotation_$Name_, tmp);
	}
    protected static final void _init_prod__bracket_$Assignable__lit___40_$layouts_LAYOUTLIST_arg_$Assignable_$layouts_LAYOUTLIST_lit___41__bracket(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new LiteralStackNode<IConstructor>(-5665, 4, prod__lit___41__char_class___range__41_41_, new int[] {41}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-5664, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(-5661, 2, "$Assignable", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-5659, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-5657, 0, prod__lit___40__char_class___range__40_40_, new int[] {40}, null, null);
      builder.addAlternative(RascalParser.prod__bracket_$Assignable__lit___40_$layouts_LAYOUTLIST_arg_$Assignable_$layouts_LAYOUTLIST_lit___41__bracket, tmp);
	}
    protected static final void _init_prod__constructor_$Assignable__name_$Name_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_arguments_iter_seps__$Assignable__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[7];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(-5670, 0, "$Name", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-5673, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(-5674, 2, prod__lit___40__char_class___range__40_40_, new int[] {40}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-5676, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new SeparatedListStackNode<IConstructor>(-5684, 4, regular__iter_seps__$Assignable__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(-5678, 0, "$Assignable", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(-5680, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode<IConstructor>(-5681, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode<IConstructor>(-5683, 3, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(-5687, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new LiteralStackNode<IConstructor>(-5688, 6, prod__lit___41__char_class___range__41_41_, new int[] {41}, null, null);
      builder.addAlternative(RascalParser.prod__constructor_$Assignable__name_$Name_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_arguments_iter_seps__$Assignable__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_, tmp);
	}
    protected static final void _init_prod__fieldAccess_$Assignable__receiver_$Assignable_$layouts_LAYOUTLIST_lit___46_$layouts_LAYOUTLIST_field_$Name_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(-5701, 4, "$Name", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-5699, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(-5697, 2, prod__lit___46__char_class___range__46_46_, new int[] {46}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-5696, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-5693, 0, "$Assignable", null, null);
      builder.addAlternative(RascalParser.prod__fieldAccess_$Assignable__receiver_$Assignable_$layouts_LAYOUTLIST_lit___46_$layouts_LAYOUTLIST_field_$Name_, tmp);
	}
    protected static final void _init_prod__ifDefinedOrDefault_$Assignable__receiver_$Assignable_$layouts_LAYOUTLIST_lit___63_$layouts_LAYOUTLIST_defaultExpression_$Expression_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(-5729, 4, "$Expression", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-5727, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(-5725, 2, prod__lit___63__char_class___range__63_63_, new int[] {63}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-5724, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-5721, 0, "$Assignable", null, null);
      builder.addAlternative(RascalParser.prod__ifDefinedOrDefault_$Assignable__receiver_$Assignable_$layouts_LAYOUTLIST_lit___63_$layouts_LAYOUTLIST_defaultExpression_$Expression_, tmp);
	}
    protected static final void _init_prod__subscript_$Assignable__receiver_$Assignable_$layouts_LAYOUTLIST_lit___91_$layouts_LAYOUTLIST_subscript_$Expression_$layouts_LAYOUTLIST_lit___93_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[7];
      
      tmp[6] = new LiteralStackNode<IConstructor>(-5753, 6, prod__lit___93__char_class___range__93_93_, new int[] {93}, null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(-5752, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new NonTerminalStackNode<IConstructor>(-5749, 4, "$Expression", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-5747, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(-5745, 2, prod__lit___91__char_class___range__91_91_, new int[] {91}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-5744, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-5741, 0, "$Assignable", null, null);
      builder.addAlternative(RascalParser.prod__subscript_$Assignable__receiver_$Assignable_$layouts_LAYOUTLIST_lit___91_$layouts_LAYOUTLIST_subscript_$Expression_$layouts_LAYOUTLIST_lit___93_, tmp);
	}
    protected static final void _init_prod__tuple_$Assignable__lit___60_$layouts_LAYOUTLIST_elements_iter_seps__$Assignable__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___62_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new LiteralStackNode<IConstructor>(-5653, 4, prod__lit___62__char_class___range__62_62_, new int[] {62}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-5652, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new SeparatedListStackNode<IConstructor>(-5649, 2, regular__iter_seps__$Assignable__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(-5643, 0, "$Assignable", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(-5645, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode<IConstructor>(-5646, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode<IConstructor>(-5648, 3, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-5641, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-5639, 0, prod__lit___60__char_class___range__60_60_, new int[] {60}, null, null);
      builder.addAlternative(RascalParser.prod__tuple_$Assignable__lit___60_$layouts_LAYOUTLIST_elements_iter_seps__$Assignable__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___62_, tmp);
	}
    protected static final void _init_prod__variable_$Assignable__qualifiedName_$QualifiedName_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(-5735, 0, "$QualifiedName", null, null);
      builder.addAlternative(RascalParser.prod__variable_$Assignable__qualifiedName_$QualifiedName_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__annotation_$Assignable__receiver_$Assignable_$layouts_LAYOUTLIST_lit___64_$layouts_LAYOUTLIST_annotation_$Name_(builder);
      
        _init_prod__bracket_$Assignable__lit___40_$layouts_LAYOUTLIST_arg_$Assignable_$layouts_LAYOUTLIST_lit___41__bracket(builder);
      
        _init_prod__constructor_$Assignable__name_$Name_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_arguments_iter_seps__$Assignable__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_(builder);
      
        _init_prod__fieldAccess_$Assignable__receiver_$Assignable_$layouts_LAYOUTLIST_lit___46_$layouts_LAYOUTLIST_field_$Name_(builder);
      
        _init_prod__ifDefinedOrDefault_$Assignable__receiver_$Assignable_$layouts_LAYOUTLIST_lit___63_$layouts_LAYOUTLIST_defaultExpression_$Expression_(builder);
      
        _init_prod__subscript_$Assignable__receiver_$Assignable_$layouts_LAYOUTLIST_lit___91_$layouts_LAYOUTLIST_subscript_$Expression_$layouts_LAYOUTLIST_lit___93_(builder);
      
        _init_prod__tuple_$Assignable__lit___60_$layouts_LAYOUTLIST_elements_iter_seps__$Assignable__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___62_(builder);
      
        _init_prod__variable_$Assignable__qualifiedName_$QualifiedName_(builder);
      
    }
  }
	
  protected static class $Assignment {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__addition_$Assignment__lit___43_61_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-5812, 0, prod__lit___43_61__char_class___range__43_43_char_class___range__61_61_, new int[] {43,61}, null, null);
      builder.addAlternative(RascalParser.prod__addition_$Assignment__lit___43_61_, tmp);
	}
    protected static final void _init_prod__append_$Assignment__lit___60_60_61_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-5832, 0, prod__lit___60_60_61__char_class___range__60_60_char_class___range__60_60_char_class___range__61_61_, new int[] {60,60,61}, null, null);
      builder.addAlternative(RascalParser.prod__append_$Assignment__lit___60_60_61_, tmp);
	}
    protected static final void _init_prod__default_$Assignment__lit___61_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-5828, 0, prod__lit___61__char_class___range__61_61_, new int[] {61}, null, null);
      builder.addAlternative(RascalParser.prod__default_$Assignment__lit___61_, tmp);
	}
    protected static final void _init_prod__division_$Assignment__lit___47_61_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-5820, 0, prod__lit___47_61__char_class___range__47_47_char_class___range__61_61_, new int[] {47,61}, null, null);
      builder.addAlternative(RascalParser.prod__division_$Assignment__lit___47_61_, tmp);
	}
    protected static final void _init_prod__ifDefined_$Assignment__lit___63_61_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-5824, 0, prod__lit___63_61__char_class___range__63_63_char_class___range__61_61_, new int[] {63,61}, null, null);
      builder.addAlternative(RascalParser.prod__ifDefined_$Assignment__lit___63_61_, tmp);
	}
    protected static final void _init_prod__intersection_$Assignment__lit___38_61_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-5836, 0, prod__lit___38_61__char_class___range__38_38_char_class___range__61_61_, new int[] {38,61}, null, null);
      builder.addAlternative(RascalParser.prod__intersection_$Assignment__lit___38_61_, tmp);
	}
    protected static final void _init_prod__product_$Assignment__lit___42_61_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-5816, 0, prod__lit___42_61__char_class___range__42_42_char_class___range__61_61_, new int[] {42,61}, null, null);
      builder.addAlternative(RascalParser.prod__product_$Assignment__lit___42_61_, tmp);
	}
    protected static final void _init_prod__subtraction_$Assignment__lit___45_61_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-5840, 0, prod__lit___45_61__char_class___range__45_45_char_class___range__61_61_, new int[] {45,61}, null, null);
      builder.addAlternative(RascalParser.prod__subtraction_$Assignment__lit___45_61_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__addition_$Assignment__lit___43_61_(builder);
      
        _init_prod__append_$Assignment__lit___60_60_61_(builder);
      
        _init_prod__default_$Assignment__lit___61_(builder);
      
        _init_prod__division_$Assignment__lit___47_61_(builder);
      
        _init_prod__ifDefined_$Assignment__lit___63_61_(builder);
      
        _init_prod__intersection_$Assignment__lit___38_61_(builder);
      
        _init_prod__product_$Assignment__lit___42_61_(builder);
      
        _init_prod__subtraction_$Assignment__lit___45_61_(builder);
      
    }
  }
	
  protected static class $Comment {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__$Comment__lit___47_47_iter_star__char_class___range__0_9_range__11_16777215__tag__category___67_111_109_109_101_110_116(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[2];
      
      tmp[1] = new ListStackNode<IConstructor>(-5984, 1, regular__iter_star__char_class___range__0_9_range__11_16777215, new CharStackNode<IConstructor>(-5981, 0, new int[][]{{0,9},{11,16777215}}, null, null), false, null, new ICompletionFilter[] {new CharFollowRestriction(new int[][]{{9,9},{13,13},{32,32},{65,65},{160,160},{5760,5760},{8192,8192},{8239,8239},{8287,8287},{12288,12288}}), new AtEndOfLineRequirement()});
      tmp[0] = new LiteralStackNode<IConstructor>(-5980, 0, prod__lit___47_47__char_class___range__47_47_char_class___range__47_47_, new int[] {47,47}, null, null);
      builder.addAlternative(RascalParser.prod__$Comment__lit___47_47_iter_star__char_class___range__0_9_range__11_16777215__tag__category___67_111_109_109_101_110_116, tmp);
	}
    protected static final void _init_prod__$Comment__lit___47_42_iter_star__alt___char_class___range__42_42_char_class___range__0_41_range__43_16777215_lit___42_47__tag__category___67_111_109_109_101_110_116(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new LiteralStackNode<IConstructor>(-5994, 2, prod__lit___42_47__char_class___range__42_42_char_class___range__47_47_, new int[] {42,47}, null, null);
      tmp[1] = new ListStackNode<IConstructor>(-5993, 1, regular__iter_star__alt___char_class___range__42_42_char_class___range__0_41_range__43_16777215, new AlternativeStackNode<IConstructor>(-5992, 0, regular__alt___char_class___range__42_42_char_class___range__0_41_range__43_16777215, (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new CharStackNode<IConstructor>(-5990, 0, new int[][]{{42,42}}, null, new ICompletionFilter[] {new CharFollowRestriction(new int[][]{{47,47}})}), new CharStackNode<IConstructor>(-5991, 0, new int[][]{{0,41},{43,16777215}}, null, null)}, null, null), false, null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-5987, 0, prod__lit___47_42__char_class___range__47_47_char_class___range__42_42_, new int[] {47,42}, null, null);
      builder.addAlternative(RascalParser.prod__$Comment__lit___47_42_iter_star__alt___char_class___range__42_42_char_class___range__0_41_range__43_16777215_lit___42_47__tag__category___67_111_109_109_101_110_116, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__$Comment__lit___47_47_iter_star__char_class___range__0_9_range__11_16777215__tag__category___67_111_109_109_101_110_116(builder);
      
        _init_prod__$Comment__lit___47_42_iter_star__alt___char_class___range__42_42_char_class___range__0_41_range__43_16777215_lit___42_47__tag__category___67_111_109_109_101_110_116(builder);
      
    }
  }
	
  protected static class $ConcretePart {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__bq_$ConcretePart__lit___92_96_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-5965, 0, prod__lit___92_96__char_class___range__92_92_char_class___range__96_96_, new int[] {92,96}, null, null);
      builder.addAlternative(RascalParser.prod__bq_$ConcretePart__lit___92_96_, tmp);
	}
    protected static final void _init_prod__bs_$ConcretePart__lit___92_92_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-5969, 0, prod__lit___92_92__char_class___range__92_92_char_class___range__92_92_, new int[] {92,92}, null, null);
      builder.addAlternative(RascalParser.prod__bs_$ConcretePart__lit___92_92_, tmp);
	}
    protected static final void _init_prod__gt_$ConcretePart__lit___92_62_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-5944, 0, prod__lit___92_62__char_class___range__92_92_char_class___range__62_62_, new int[] {92,62}, null, null);
      builder.addAlternative(RascalParser.prod__gt_$ConcretePart__lit___92_62_, tmp);
	}
    protected static final void _init_prod__hole_$ConcretePart__hole_$ConcreteHole_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(-5956, 0, "$ConcreteHole", null, null);
      builder.addAlternative(RascalParser.prod__hole_$ConcretePart__hole_$ConcreteHole_, tmp);
	}
    protected static final void _init_prod__lt_$ConcretePart__lit___92_60_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-5961, 0, prod__lit___92_60__char_class___range__92_92_char_class___range__60_60_, new int[] {92,60}, null, null);
      builder.addAlternative(RascalParser.prod__lt_$ConcretePart__lit___92_60_, tmp);
	}
    protected static final void _init_prod__newline_$ConcretePart__lit___10_iter_star__char_class___range__9_9_range__32_32_range__65_65_range__160_160_range__5760_5760_range__8192_8192_range__8239_8239_range__8287_8287_range__12288_12288_lit___39_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new LiteralStackNode<IConstructor>(-5951, 2, prod__lit___39__char_class___range__39_39_, new int[] {39}, null, null);
      tmp[1] = new ListStackNode<IConstructor>(-5950, 1, regular__iter_star__char_class___range__9_9_range__32_32_range__65_65_range__160_160_range__5760_5760_range__8192_8192_range__8239_8239_range__8287_8287_range__12288_12288, new CharStackNode<IConstructor>(-5949, 0, new int[][]{{9,9},{32,32},{65,65},{160,160},{5760,5760},{8192,8192},{8239,8239},{8287,8287},{12288,12288}}, null, null), false, null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-5948, 0, prod__lit___10__char_class___range__10_10_, new int[] {10}, null, null);
      builder.addAlternative(RascalParser.prod__newline_$ConcretePart__lit___10_iter_star__char_class___range__9_9_range__32_32_range__65_65_range__160_160_range__5760_5760_range__8192_8192_range__8239_8239_range__8287_8287_range__12288_12288_lit___39_, tmp);
	}
    protected static final void _init_prod__text_$ConcretePart__char_class___range__0_9_range__11_59_range__61_61_range__63_91_range__93_95_range__97_16777215_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new CharStackNode<IConstructor>(-5973, 0, new int[][]{{0,9},{11,59},{61,61},{63,91},{93,95},{97,16777215}}, null, null);
      builder.addAlternative(RascalParser.prod__text_$ConcretePart__char_class___range__0_9_range__11_59_range__61_61_range__63_91_range__93_95_range__97_16777215_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__bq_$ConcretePart__lit___92_96_(builder);
      
        _init_prod__bs_$ConcretePart__lit___92_92_(builder);
      
        _init_prod__gt_$ConcretePart__lit___92_62_(builder);
      
        _init_prod__hole_$ConcretePart__hole_$ConcreteHole_(builder);
      
        _init_prod__lt_$ConcretePart__lit___92_60_(builder);
      
        _init_prod__newline_$ConcretePart__lit___10_iter_star__char_class___range__9_9_range__32_32_range__65_65_range__160_160_range__5760_5760_range__8192_8192_range__8239_8239_range__8287_8287_range__12288_12288_lit___39_(builder);
      
        _init_prod__text_$ConcretePart__char_class___range__0_9_range__11_59_range__61_61_range__63_91_range__93_95_range__97_16777215_(builder);
      
    }
  }
	
  protected static class $EvalCommand {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__declaration_$EvalCommand__declaration_$Declaration_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(-5778, 0, "$Declaration", null, null);
      builder.addAlternative(RascalParser.prod__declaration_$EvalCommand__declaration_$Declaration_, tmp);
	}
    protected static final void _init_prod__import_$EvalCommand__imported_$Import_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(-5791, 0, "$Import", null, null);
      builder.addAlternative(RascalParser.prod__import_$EvalCommand__imported_$Import_, tmp);
	}
    protected static final void _init_prod__statement_$EvalCommand__statement_$Statement_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(-5785, 0, "$Statement", null, null);
      builder.addAlternative(RascalParser.prod__statement_$EvalCommand__statement_$Statement_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__declaration_$EvalCommand__declaration_$Declaration_(builder);
      
        _init_prod__import_$EvalCommand__imported_$Import_(builder);
      
        _init_prod__statement_$EvalCommand__statement_$Statement_(builder);
      
    }
  }
	
  protected static class $Field {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__index_$Field__fieldIndex_$IntegerLiteral_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(-5887, 0, "$IntegerLiteral", null, null);
      builder.addAlternative(RascalParser.prod__index_$Field__fieldIndex_$IntegerLiteral_, tmp);
	}
    protected static final void _init_prod__name_$Field__fieldName_$Name_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(-5881, 0, "$Name", null, null);
      builder.addAlternative(RascalParser.prod__name_$Field__fieldName_$Name_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__index_$Field__fieldIndex_$IntegerLiteral_(builder);
      
        _init_prod__name_$Field__fieldName_$Name_(builder);
      
    }
  }
	
  protected static class $FunctionModifier {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__default_$FunctionModifier__lit_default_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-5765, 0, prod__lit_default__char_class___range__100_100_char_class___range__101_101_char_class___range__102_102_char_class___range__97_97_char_class___range__117_117_char_class___range__108_108_char_class___range__116_116_, new int[] {100,101,102,97,117,108,116}, null, null);
      builder.addAlternative(RascalParser.prod__default_$FunctionModifier__lit_default_, tmp);
	}
    protected static final void _init_prod__java_$FunctionModifier__lit_java_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-5769, 0, prod__lit_java__char_class___range__106_106_char_class___range__97_97_char_class___range__118_118_char_class___range__97_97_, new int[] {106,97,118,97}, null, null);
      builder.addAlternative(RascalParser.prod__java_$FunctionModifier__lit_java_, tmp);
	}
    protected static final void _init_prod__test_$FunctionModifier__lit_test_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-5761, 0, prod__lit_test__char_class___range__116_116_char_class___range__101_101_char_class___range__115_115_char_class___range__116_116_, new int[] {116,101,115,116}, null, null);
      builder.addAlternative(RascalParser.prod__test_$FunctionModifier__lit_test_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__default_$FunctionModifier__lit_default_(builder);
      
        _init_prod__java_$FunctionModifier__lit_java_(builder);
      
        _init_prod__test_$FunctionModifier__lit_test_(builder);
      
    }
  }
	
  protected static class $ProtocolChars {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__$ProtocolChars__char_class___range__124_124_$URLChars_lit___58_47_47_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new LiteralStackNode<IConstructor>(-5804, 2, prod__lit___58_47_47__char_class___range__58_58_char_class___range__47_47_char_class___range__47_47_, new int[] {58,47,47}, null, new ICompletionFilter[] {new CharFollowRestriction(new int[][]{{9,10},{13,13},{32,32},{65,65},{160,160},{5760,5760},{8192,8192},{8239,8239},{8287,8287},{12288,12288}})});
      tmp[1] = new NonTerminalStackNode<IConstructor>(-5801, 1, "$URLChars", null, null);
      tmp[0] = new CharStackNode<IConstructor>(-5799, 0, new int[][]{{124,124}}, null, null);
      builder.addAlternative(RascalParser.prod__$ProtocolChars__char_class___range__124_124_$URLChars_lit___58_47_47_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__$ProtocolChars__char_class___range__124_124_$URLChars_lit___58_47_47_(builder);
      
    }
  }
	
  protected static class $Label {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__default_$Label__name_$Name_$layouts_LAYOUTLIST_lit___58_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new LiteralStackNode<IConstructor>(-6049, 2, prod__lit___58__char_class___range__58_58_, new int[] {58}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-6048, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-6045, 0, "$Name", null, null);
      builder.addAlternative(RascalParser.prod__default_$Label__name_$Name_$layouts_LAYOUTLIST_lit___58_, tmp);
	}
    protected static final void _init_prod__empty_$Label__(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new EpsilonStackNode<IConstructor>(-6052, 0);
      builder.addAlternative(RascalParser.prod__empty_$Label__, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__default_$Label__name_$Name_$layouts_LAYOUTLIST_lit___58_(builder);
      
        _init_prod__empty_$Label__(builder);
      
    }
  }
	
  protected static class $ProtocolPart {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__interpolated_$ProtocolPart__pre_$PreProtocolChars_$layouts_LAYOUTLIST_expression_$Expression_$layouts_LAYOUTLIST_tail_$ProtocolTail_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(-6077, 4, "$ProtocolTail", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-6075, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(-6072, 2, "$Expression", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-6070, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-6067, 0, "$PreProtocolChars", null, null);
      builder.addAlternative(RascalParser.prod__interpolated_$ProtocolPart__pre_$PreProtocolChars_$layouts_LAYOUTLIST_expression_$Expression_$layouts_LAYOUTLIST_tail_$ProtocolTail_, tmp);
	}
    protected static final void _init_prod__nonInterpolated_$ProtocolPart__protocolChars_$ProtocolChars_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(-6061, 0, "$ProtocolChars", null, null);
      builder.addAlternative(RascalParser.prod__nonInterpolated_$ProtocolPart__protocolChars_$ProtocolChars_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__interpolated_$ProtocolPart__pre_$PreProtocolChars_$layouts_LAYOUTLIST_expression_$Expression_$layouts_LAYOUTLIST_tail_$ProtocolTail_(builder);
      
        _init_prod__nonInterpolated_$ProtocolPart__protocolChars_$ProtocolChars_(builder);
      
    }
  }
	
  protected static class $Nonterminal {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__$Nonterminal__char_class___range__65_90_iter_star__char_class___range__48_57_range__65_90_range__95_95_range__97_122_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[2];
      
      tmp[1] = new ListStackNode<IConstructor>(-6206, 1, regular__iter_star__char_class___range__48_57_range__65_90_range__95_95_range__97_122, new CharStackNode<IConstructor>(-6201, 0, new int[][]{{48,57},{65,90},{95,95},{97,122}}, null, null), false, null, new ICompletionFilter[] {new CharFollowRestriction(new int[][]{{48,57},{65,90},{95,95},{97,122}})});
      tmp[0] = new CharStackNode<IConstructor>(-6200, 0, new int[][]{{65,90}}, new IEnterFilter[] {new CharPrecedeRestriction(new int[][]{{65,90}})}, null);
      builder.addAlternative(RascalParser.prod__$Nonterminal__char_class___range__65_90_iter_star__char_class___range__48_57_range__65_90_range__95_95_range__97_122_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__$Nonterminal__char_class___range__65_90_iter_star__char_class___range__48_57_range__65_90_range__95_95_range__97_122_(builder);
      
    }
  }
	
  protected static class $TagString {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__$TagString__lit___123_contents_iter_star__alt___$TagString_char_class___range__0_122_range__124_124_range__126_16777215_seq___lit___92_char_class___range__123_123_range__125_125_lit___125_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new LiteralStackNode<IConstructor>(-6191, 2, prod__lit___125__char_class___range__125_125_, new int[] {125}, new IEnterFilter[] {new StringPrecedeRestriction(new int[] {92})}, null);
      tmp[1] = new ListStackNode<IConstructor>(-6187, 1, regular__iter_star__alt___$TagString_char_class___range__0_122_range__124_124_range__126_16777215_seq___lit___92_char_class___range__123_123_range__125_125, new AlternativeStackNode<IConstructor>(-6186, 0, regular__alt___$TagString_char_class___range__0_122_range__124_124_range__126_16777215_seq___lit___92_char_class___range__123_123_range__125_125, (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(-6181, 0, "$TagString", null, null), new CharStackNode<IConstructor>(-6182, 0, new int[][]{{0,122},{124,124},{126,16777215}}, null, null), new SequenceStackNode<IConstructor>(-6185, 0, regular__seq___lit___92_char_class___range__123_123_range__125_125, (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new LiteralStackNode<IConstructor>(-6183, 0, prod__lit___92__char_class___range__92_92_, new int[] {92}, null, null), new CharStackNode<IConstructor>(-6184, 1, new int[][]{{123,123},{125,125}}, null, null)}, null, null)}, null, null), false, null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-6179, 0, prod__lit___123__char_class___range__123_123_, new int[] {123}, new IEnterFilter[] {new StringPrecedeRestriction(new int[] {92})}, null);
      builder.addAlternative(RascalParser.prod__$TagString__lit___123_contents_iter_star__alt___$TagString_char_class___range__0_122_range__124_124_range__126_16777215_seq___lit___92_char_class___range__123_123_range__125_125_lit___125_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__$TagString__lit___123_contents_iter_star__alt___$TagString_char_class___range__0_122_range__124_124_range__126_16777215_seq___lit___92_char_class___range__123_123_range__125_125_lit___125_(builder);
      
    }
  }
	
  protected static class $Expression {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__addition_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___43_$layouts_LAYOUTLIST_rhs_$Expression__assoc__left(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(-7011, 4, "$Expression", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-7008, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(-7006, 2, prod__lit___43__char_class___range__43_43_, new int[] {43}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-7005, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-7002, 0, "$Expression", null, null);
      builder.addAlternative(RascalParser.prod__addition_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___43_$layouts_LAYOUTLIST_rhs_$Expression__assoc__left, tmp);
	}
    protected static final void _init_prod__appendAfter_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___60_60_$layouts_LAYOUTLIST_rhs_$Expression__assoc__left(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(-7027, 4, "$Expression", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-7025, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(-7023, 2, prod__lit___60_60__char_class___range__60_60_char_class___range__60_60_, new int[] {60,60}, null, new ICompletionFilter[] {new StringFollowRestriction(new int[] {61})});
      tmp[1] = new NonTerminalStackNode<IConstructor>(-7020, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-7017, 0, "$Expression", null, null);
      builder.addAlternative(RascalParser.prod__appendAfter_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___60_60_$layouts_LAYOUTLIST_rhs_$Expression__assoc__left, tmp);
	}
    protected static final void _init_prod__asType_$Expression__lit___91_$layouts_LAYOUTLIST_type_$Type_$layouts_LAYOUTLIST_lit___93_$layouts_LAYOUTLIST_argument_$Expression_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[7];
      
      tmp[6] = new NonTerminalStackNode<IConstructor>(-6874, 6, "$Expression", null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(-6871, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode<IConstructor>(-6869, 4, prod__lit___93__char_class___range__93_93_, new int[] {93}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-6868, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(-6865, 2, "$Type", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-6863, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-6861, 0, prod__lit___91__char_class___range__91_91_, new int[] {91}, null, null);
      builder.addAlternative(RascalParser.prod__asType_$Expression__lit___91_$layouts_LAYOUTLIST_type_$Type_$layouts_LAYOUTLIST_lit___93_$layouts_LAYOUTLIST_argument_$Expression_, tmp);
	}
    protected static final void _init_prod__composition_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit_o_$layouts_LAYOUTLIST_rhs_$Expression__assoc__left(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(-6897, 4, "$Expression", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-6895, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(-6893, 2, prod__lit_o__char_class___range__111_111_, new int[] {111}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-6892, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-6889, 0, "$Expression", null, null);
      builder.addAlternative(RascalParser.prod__composition_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit_o_$layouts_LAYOUTLIST_rhs_$Expression__assoc__left, tmp);
	}
    protected static final void _init_prod__comprehension_$Expression__comprehension_$Comprehension_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(-6402, 0, "$Comprehension", null, null);
      builder.addAlternative(RascalParser.prod__comprehension_$Expression__comprehension_$Comprehension_, tmp);
	}
    protected static final void _init_prod__division_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___47_$layouts_LAYOUTLIST_rhs_$Expression__assoc__left(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(-6914, 4, "$Expression", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-6912, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(-6910, 2, prod__lit___47__char_class___range__47_47_, new int[] {47}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-6909, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-6906, 0, "$Expression", null, null);
      builder.addAlternative(RascalParser.prod__division_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___47_$layouts_LAYOUTLIST_rhs_$Expression__assoc__left, tmp);
	}
    protected static final void _init_prod__ifDefinedOtherwise_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___63_$layouts_LAYOUTLIST_rhs_$Expression__assoc__non_assoc(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(-7192, 4, "$Expression", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-7190, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(-7188, 2, prod__lit___63__char_class___range__63_63_, new int[] {63}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-7187, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-7184, 0, "$Expression", null, null);
      builder.addAlternative(RascalParser.prod__ifDefinedOtherwise_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___63_$layouts_LAYOUTLIST_rhs_$Expression__assoc__non_assoc, tmp);
	}
    protected static final void _init_prod__ifThenElse_$Expression__condition_$Expression_$layouts_LAYOUTLIST_lit___63_$layouts_LAYOUTLIST_thenExp_$Expression_$layouts_LAYOUTLIST_lit___58_$layouts_LAYOUTLIST_elseExp_$Expression__assoc__right(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[9];
      
      tmp[8] = new NonTerminalStackNode<IConstructor>(-7318, 8, "$Expression", null, null);
      tmp[7] = new NonTerminalStackNode<IConstructor>(-7316, 7, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new LiteralStackNode<IConstructor>(-7314, 6, prod__lit___58__char_class___range__58_58_, new int[] {58}, null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(-7313, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new NonTerminalStackNode<IConstructor>(-7310, 4, "$Expression", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-7308, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(-7306, 2, prod__lit___63__char_class___range__63_63_, new int[] {63}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-7305, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-7302, 0, "$Expression", null, null);
      builder.addAlternative(RascalParser.prod__ifThenElse_$Expression__condition_$Expression_$layouts_LAYOUTLIST_lit___63_$layouts_LAYOUTLIST_thenExp_$Expression_$layouts_LAYOUTLIST_lit___58_$layouts_LAYOUTLIST_elseExp_$Expression__assoc__right, tmp);
	}
    protected static final void _init_prod__implication_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___61_61_62_$layouts_LAYOUTLIST_rhs_$Expression__assoc__non_assoc(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(-7268, 4, "$Expression", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-7266, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(-7264, 2, prod__lit___61_61_62__char_class___range__61_61_char_class___range__61_61_char_class___range__62_62_, new int[] {61,61,62}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-7263, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-7260, 0, "$Expression", null, null);
      builder.addAlternative(RascalParser.prod__implication_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___61_61_62_$layouts_LAYOUTLIST_rhs_$Expression__assoc__non_assoc, tmp);
	}
    protected static final void _init_prod__insertBefore_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___62_62_$layouts_LAYOUTLIST_rhs_$Expression__assoc__left(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(-7041, 4, "$Expression", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-7039, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(-7037, 2, prod__lit___62_62__char_class___range__62_62_char_class___range__62_62_, new int[] {62,62}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-7036, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-7033, 0, "$Expression", null, null);
      builder.addAlternative(RascalParser.prod__insertBefore_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___62_62_$layouts_LAYOUTLIST_rhs_$Expression__assoc__left, tmp);
	}
    protected static final void _init_prod__lessThan_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___60_$layouts_LAYOUTLIST_rhs_$Expression__assoc__non_assoc(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(-7133, 4, "$Expression", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-7131, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(-7129, 2, prod__lit___60__char_class___range__60_60_, new int[] {60}, null, new ICompletionFilter[] {new StringFollowRestriction(new int[] {45})});
      tmp[1] = new NonTerminalStackNode<IConstructor>(-7126, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-7123, 0, "$Expression", null, null);
      builder.addAlternative(RascalParser.prod__lessThan_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___60_$layouts_LAYOUTLIST_rhs_$Expression__assoc__non_assoc, tmp);
	}
    protected static final void _init_prod__literal_$Expression__literal_$Literal_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(-6378, 0, "$Literal", null, null);
      builder.addAlternative(RascalParser.prod__literal_$Expression__literal_$Literal_, tmp);
	}
    protected static final void _init_prod__nonEquals_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___33_61_$layouts_LAYOUTLIST_rhs_$Expression__assoc__non_assoc(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(-7178, 4, "$Expression", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-7176, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(-7174, 2, prod__lit___33_61__char_class___range__33_33_char_class___range__61_61_, new int[] {33,61}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-7173, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-7170, 0, "$Expression", null, null);
      builder.addAlternative(RascalParser.prod__nonEquals_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___33_61_$layouts_LAYOUTLIST_rhs_$Expression__assoc__non_assoc, tmp);
	}
    protected static final void _init_prod__range_$Expression__lit___91_$layouts_LAYOUTLIST_first_$Expression_$layouts_LAYOUTLIST_lit___46_46_$layouts_LAYOUTLIST_last_$Expression_$layouts_LAYOUTLIST_lit___93_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[9];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-6311, 0, prod__lit___91__char_class___range__91_91_, new int[] {91}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-6313, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(-6315, 2, "$Expression", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-6318, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode<IConstructor>(-6319, 4, prod__lit___46_46__char_class___range__46_46_char_class___range__46_46_, new int[] {46,46}, null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(-6321, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new NonTerminalStackNode<IConstructor>(-6323, 6, "$Expression", null, null);
      tmp[7] = new NonTerminalStackNode<IConstructor>(-6326, 7, "$layouts_LAYOUTLIST", null, null);
      tmp[8] = new LiteralStackNode<IConstructor>(-6327, 8, prod__lit___93__char_class___range__93_93_, new int[] {93}, null, null);
      builder.addAlternative(RascalParser.prod__range_$Expression__lit___91_$layouts_LAYOUTLIST_first_$Expression_$layouts_LAYOUTLIST_lit___46_46_$layouts_LAYOUTLIST_last_$Expression_$layouts_LAYOUTLIST_lit___93_, tmp);
	}
    protected static final void _init_prod__remainder_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___37_$layouts_LAYOUTLIST_rhs_$Expression__assoc__left(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(-6942, 4, "$Expression", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-6940, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(-6938, 2, prod__lit___37__char_class___range__37_37_, new int[] {37}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-6937, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-6934, 0, "$Expression", null, null);
      builder.addAlternative(RascalParser.prod__remainder_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___37_$layouts_LAYOUTLIST_rhs_$Expression__assoc__left, tmp);
	}
    protected static final void _init_prod__set_$Expression__lit___123_$layouts_LAYOUTLIST_elements_iter_star_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new LiteralStackNode<IConstructor>(-6432, 4, prod__lit___125__char_class___range__125_125_, new int[] {125}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-6431, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new SeparatedListStackNode<IConstructor>(-6428, 2, regular__iter_star_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(-6422, 0, "$Expression", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(-6424, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode<IConstructor>(-6425, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode<IConstructor>(-6427, 3, "$layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-6420, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-6418, 0, prod__lit___123__char_class___range__123_123_, new int[] {123}, null, null);
      builder.addAlternative(RascalParser.prod__set_$Expression__lit___123_$layouts_LAYOUTLIST_elements_iter_star_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_, tmp);
	}
    protected static final void _init_prod__transitiveReflexiveClosure_$Expression__argument_$Expression_$layouts_LAYOUTLIST_lit___42_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new LiteralStackNode<IConstructor>(-6373, 2, prod__lit___42__char_class___range__42_42_, new int[] {42}, null, new ICompletionFilter[] {new StringFollowRestriction(new int[] {61})});
      tmp[1] = new NonTerminalStackNode<IConstructor>(-6370, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-6367, 0, "$Expression", null, null);
      builder.addAlternative(RascalParser.prod__transitiveReflexiveClosure_$Expression__argument_$Expression_$layouts_LAYOUTLIST_lit___42_, tmp);
	}
    protected static final void _init_prod__bracket_$Expression__lit___40_$layouts_LAYOUTLIST_expression_$Expression_$layouts_LAYOUTLIST_lit___41__bracket(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new LiteralStackNode<IConstructor>(-6559, 4, prod__lit___41__char_class___range__41_41_, new int[] {41}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-6558, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(-6555, 2, "$Expression", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-6553, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-6551, 0, prod__lit___40__char_class___range__40_40_, new int[] {40}, null, null);
      builder.addAlternative(RascalParser.prod__bracket_$Expression__lit___40_$layouts_LAYOUTLIST_expression_$Expression_$layouts_LAYOUTLIST_lit___41__bracket, tmp);
	}
    protected static final void _init_prod__closure_$Expression__type_$Type_$layouts_LAYOUTLIST_parameters_$Parameters_$layouts_LAYOUTLIST_lit___123_$layouts_LAYOUTLIST_statements_iter_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[9];
      
      tmp[8] = new LiteralStackNode<IConstructor>(-6749, 8, prod__lit___125__char_class___range__125_125_, new int[] {125}, null, null);
      tmp[7] = new NonTerminalStackNode<IConstructor>(-6748, 7, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new SeparatedListStackNode<IConstructor>(-6745, 6, regular__iter_seps__$Statement__$layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(-6742, 0, "$Statement", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(-6744, 1, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(-6740, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode<IConstructor>(-6738, 4, prod__lit___123__char_class___range__123_123_, new int[] {123}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-6737, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(-6734, 2, "$Parameters", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-6732, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-6729, 0, "$Type", null, null);
      builder.addAlternative(RascalParser.prod__closure_$Expression__type_$Type_$layouts_LAYOUTLIST_parameters_$Parameters_$layouts_LAYOUTLIST_lit___123_$layouts_LAYOUTLIST_statements_iter_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_, tmp);
	}
    protected static final void _init_prod__enumerator_$Expression__pattern_$Pattern_$layouts_LAYOUTLIST_lit___60_45_$layouts_LAYOUTLIST_expression_$Expression__assoc__non_assoc(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(-7223, 4, "$Expression", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-7221, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(-7219, 2, prod__lit___60_45__char_class___range__60_60_char_class___range__45_45_, new int[] {60,45}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-7218, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-7215, 0, "$Pattern", null, null);
      builder.addAlternative(RascalParser.prod__enumerator_$Expression__pattern_$Pattern_$layouts_LAYOUTLIST_lit___60_45_$layouts_LAYOUTLIST_expression_$Expression__assoc__non_assoc, tmp);
	}
    protected static final void _init_prod__equivalence_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___60_61_61_62_$layouts_LAYOUTLIST_rhs_$Expression__assoc__non_assoc(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(-7254, 4, "$Expression", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-7252, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(-7250, 2, prod__lit___60_61_61_62__char_class___range__60_60_char_class___range__61_61_char_class___range__61_61_char_class___range__62_62_, new int[] {60,61,61,62}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-7249, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-7246, 0, "$Expression", null, null);
      builder.addAlternative(RascalParser.prod__equivalence_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___60_61_61_62_$layouts_LAYOUTLIST_rhs_$Expression__assoc__non_assoc, tmp);
	}
    protected static final void _init_prod__fieldAccess_$Expression__expression_$Expression_$layouts_LAYOUTLIST_lit___46_$layouts_LAYOUTLIST_field_$Name_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(-6572, 4, "$Name", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-6570, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(-6568, 2, prod__lit___46__char_class___range__46_46_, new int[] {46}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-6567, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-6564, 0, "$Expression", null, null);
      builder.addAlternative(RascalParser.prod__fieldAccess_$Expression__expression_$Expression_$layouts_LAYOUTLIST_lit___46_$layouts_LAYOUTLIST_field_$Name_, tmp);
	}
    protected static final void _init_prod__getAnnotation_$Expression__expression_$Expression_$layouts_LAYOUTLIST_lit___64_$layouts_LAYOUTLIST_name_$Name_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(-6592, 4, "$Name", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-6590, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(-6588, 2, prod__lit___64__char_class___range__64_64_, new int[] {64}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-6587, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-6584, 0, "$Expression", null, null);
      builder.addAlternative(RascalParser.prod__getAnnotation_$Expression__expression_$Expression_$layouts_LAYOUTLIST_lit___64_$layouts_LAYOUTLIST_name_$Name_, tmp);
	}
    protected static final void _init_prod__greaterThan_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___62_$layouts_LAYOUTLIST_rhs_$Expression__assoc__non_assoc(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(-7103, 4, "$Expression", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-7101, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(-7099, 2, prod__lit___62__char_class___range__62_62_, new int[] {62}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-7098, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-7095, 0, "$Expression", null, null);
      builder.addAlternative(RascalParser.prod__greaterThan_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___62_$layouts_LAYOUTLIST_rhs_$Expression__assoc__non_assoc, tmp);
	}
    protected static final void _init_prod__greaterThanOrEq_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___62_61_$layouts_LAYOUTLIST_rhs_$Expression__assoc__non_assoc(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(-7117, 4, "$Expression", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-7115, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(-7113, 2, prod__lit___62_61__char_class___range__62_62_char_class___range__61_61_, new int[] {62,61}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-7112, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-7109, 0, "$Expression", null, null);
      builder.addAlternative(RascalParser.prod__greaterThanOrEq_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___62_61_$layouts_LAYOUTLIST_rhs_$Expression__assoc__non_assoc, tmp);
	}
    protected static final void _init_prod__intersection_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___38_$layouts_LAYOUTLIST_rhs_$Expression__assoc__left(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(-6978, 4, "$Expression", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-6976, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(-6974, 2, prod__lit___38__char_class___range__38_38_, new int[] {38}, null, new ICompletionFilter[] {new StringFollowRestriction(new int[] {38})});
      tmp[1] = new NonTerminalStackNode<IConstructor>(-6971, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-6968, 0, "$Expression", null, null);
      builder.addAlternative(RascalParser.prod__intersection_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___38_$layouts_LAYOUTLIST_rhs_$Expression__assoc__left, tmp);
	}
    protected static final void _init_prod__isDefined_$Expression__argument_$Expression_$layouts_LAYOUTLIST_lit___63_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new LiteralStackNode<IConstructor>(-6836, 2, prod__lit___63__char_class___range__63_63_, new int[] {63}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-6835, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-6832, 0, "$Expression", null, null);
      builder.addAlternative(RascalParser.prod__isDefined_$Expression__argument_$Expression_$layouts_LAYOUTLIST_lit___63_, tmp);
	}
    protected static final void _init_prod__negation_$Expression__lit___33_$layouts_LAYOUTLIST_argument_$Expression_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode<IConstructor>(-6847, 2, "$Expression", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-6844, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-6842, 0, prod__lit___33__char_class___range__33_33_, new int[] {33}, null, null);
      builder.addAlternative(RascalParser.prod__negation_$Expression__lit___33_$layouts_LAYOUTLIST_argument_$Expression_, tmp);
	}
    protected static final void _init_prod__negative_$Expression__lit___$layouts_LAYOUTLIST_argument_$Expression_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode<IConstructor>(-6883, 2, "$Expression", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-6881, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-6879, 0, prod__lit____char_class___range__45_45_, new int[] {45}, null, null);
      builder.addAlternative(RascalParser.prod__negative_$Expression__lit___$layouts_LAYOUTLIST_argument_$Expression_, tmp);
	}
    protected static final void _init_prod__noMatch_$Expression__pattern_$Pattern_$layouts_LAYOUTLIST_lit___33_58_61_$layouts_LAYOUTLIST_expression_$Expression__assoc__non_assoc(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(-7209, 4, "$Expression", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-7207, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(-7205, 2, prod__lit___33_58_61__char_class___range__33_33_char_class___range__58_58_char_class___range__61_61_, new int[] {33,58,61}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-7204, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-7201, 0, "$Pattern", null, null);
      builder.addAlternative(RascalParser.prod__noMatch_$Expression__pattern_$Pattern_$layouts_LAYOUTLIST_lit___33_58_61_$layouts_LAYOUTLIST_expression_$Expression__assoc__non_assoc, tmp);
	}
    protected static final void _init_prod__nonEmptyBlock_$Expression__lit___123_$layouts_LAYOUTLIST_statements_iter_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new LiteralStackNode<IConstructor>(-6700, 4, prod__lit___125__char_class___range__125_125_, new int[] {125}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-6699, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new SeparatedListStackNode<IConstructor>(-6696, 2, regular__iter_seps__$Statement__$layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(-6693, 0, "$Statement", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(-6695, 1, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-6691, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-6689, 0, prod__lit___123__char_class___range__123_123_, new int[] {123}, null, null);
      builder.addAlternative(RascalParser.prod__nonEmptyBlock_$Expression__lit___123_$layouts_LAYOUTLIST_statements_iter_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_, tmp);
	}
    protected static final void _init_prod__notIn_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit_notin_$layouts_LAYOUTLIST_rhs_$Expression__assoc__non_assoc(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(-7086, 4, "$Expression", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-7084, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(-7082, 2, prod__lit_notin__char_class___range__110_110_char_class___range__111_111_char_class___range__116_116_char_class___range__105_105_char_class___range__110_110_, new int[] {110,111,116,105,110}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-7081, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-7078, 0, "$Expression", null, null);
      builder.addAlternative(RascalParser.prod__notIn_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit_notin_$layouts_LAYOUTLIST_rhs_$Expression__assoc__non_assoc, tmp);
	}
    protected static final void _init_prod__qualifiedName_$Expression__qualifiedName_$QualifiedName_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(-6578, 0, "$QualifiedName", null, null);
      builder.addAlternative(RascalParser.prod__qualifiedName_$Expression__qualifiedName_$QualifiedName_, tmp);
	}
    protected static final void _init_prod__subtraction_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___$layouts_LAYOUTLIST_rhs_$Expression__assoc__left(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(-6996, 4, "$Expression", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-6994, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(-6992, 2, prod__lit____char_class___range__45_45_, new int[] {45}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-6991, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-6988, 0, "$Expression", null, null);
      builder.addAlternative(RascalParser.prod__subtraction_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___$layouts_LAYOUTLIST_rhs_$Expression__assoc__left, tmp);
	}
    protected static final void _init_prod__all_$Expression__lit_all_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_generators_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[7];
      
      tmp[6] = new LiteralStackNode<IConstructor>(-6453, 6, prod__lit___41__char_class___range__41_41_, new int[] {41}, null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(-6452, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new SeparatedListStackNode<IConstructor>(-6449, 4, regular__iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(-6443, 0, "$Expression", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(-6445, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode<IConstructor>(-6446, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode<IConstructor>(-6448, 3, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-6441, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(-6439, 2, prod__lit___40__char_class___range__40_40_, new int[] {40}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-6438, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-6436, 0, prod__lit_all__char_class___range__97_97_char_class___range__108_108_char_class___range__108_108_, new int[] {97,108,108}, null, null);
      builder.addAlternative(RascalParser.prod__all_$Expression__lit_all_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_generators_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_, tmp);
	}
    protected static final void _init_prod__and_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___38_38_$layouts_LAYOUTLIST_rhs_$Expression__assoc__left(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(-7282, 4, "$Expression", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-7280, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(-7278, 2, prod__lit___38_38__char_class___range__38_38_char_class___range__38_38_, new int[] {38,38}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-7277, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-7274, 0, "$Expression", null, null);
      builder.addAlternative(RascalParser.prod__and_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___38_38_$layouts_LAYOUTLIST_rhs_$Expression__assoc__left, tmp);
	}
    protected static final void _init_prod__concrete_$Expression__concrete_$Concrete_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(-6279, 0, "$Concrete", null, null);
      builder.addAlternative(RascalParser.prod__concrete_$Expression__concrete_$Concrete_, tmp);
	}
    protected static final void _init_prod__equals_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___61_61_$layouts_LAYOUTLIST_rhs_$Expression__assoc__non_assoc(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(-7164, 4, "$Expression", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-7162, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(-7160, 2, prod__lit___61_61__char_class___range__61_61_char_class___range__61_61_, new int[] {61,61}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-7159, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-7156, 0, "$Expression", null, null);
      builder.addAlternative(RascalParser.prod__equals_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___61_61_$layouts_LAYOUTLIST_rhs_$Expression__assoc__non_assoc, tmp);
	}
    protected static final void _init_prod__fieldUpdate_$Expression__expression_$Expression_$layouts_LAYOUTLIST_lit___91_$layouts_LAYOUTLIST_key_$Name_$layouts_LAYOUTLIST_lit___61_$layouts_LAYOUTLIST_replacement_$Expression_$layouts_LAYOUTLIST_lit___93_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[11];
      
      tmp[10] = new LiteralStackNode<IConstructor>(-6547, 10, prod__lit___93__char_class___range__93_93_, new int[] {93}, null, null);
      tmp[9] = new NonTerminalStackNode<IConstructor>(-6546, 9, "$layouts_LAYOUTLIST", null, null);
      tmp[8] = new NonTerminalStackNode<IConstructor>(-6543, 8, "$Expression", null, null);
      tmp[7] = new NonTerminalStackNode<IConstructor>(-6541, 7, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new LiteralStackNode<IConstructor>(-6539, 6, prod__lit___61__char_class___range__61_61_, new int[] {61}, null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(-6538, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new NonTerminalStackNode<IConstructor>(-6535, 4, "$Name", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-6533, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(-6531, 2, prod__lit___91__char_class___range__91_91_, new int[] {91}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-6530, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-6527, 0, "$Expression", null, null);
      builder.addAlternative(RascalParser.prod__fieldUpdate_$Expression__expression_$Expression_$layouts_LAYOUTLIST_lit___91_$layouts_LAYOUTLIST_key_$Name_$layouts_LAYOUTLIST_lit___61_$layouts_LAYOUTLIST_replacement_$Expression_$layouts_LAYOUTLIST_lit___93_, tmp);
	}
    protected static final void _init_prod__has_$Expression__expression_$Expression_$layouts_LAYOUTLIST_lit_has_$layouts_LAYOUTLIST_name_$Name_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(-6521, 4, "$Name", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-6519, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(-6517, 2, prod__lit_has__char_class___range__104_104_char_class___range__97_97_char_class___range__115_115_, new int[] {104,97,115}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-6516, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-6513, 0, "$Expression", null, null);
      builder.addAlternative(RascalParser.prod__has_$Expression__expression_$Expression_$layouts_LAYOUTLIST_lit_has_$layouts_LAYOUTLIST_name_$Name_, tmp);
	}
    protected static final void _init_prod__in_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit_in_$layouts_LAYOUTLIST_rhs_$Expression__assoc__non_assoc(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(-7072, 4, "$Expression", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-7070, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(-7068, 2, prod__lit_in__char_class___range__105_105_char_class___range__110_110_, new int[] {105,110}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-7067, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-7064, 0, "$Expression", null, null);
      builder.addAlternative(RascalParser.prod__in_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit_in_$layouts_LAYOUTLIST_rhs_$Expression__assoc__non_assoc, tmp);
	}
    protected static final void _init_prod__join_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit_join_$layouts_LAYOUTLIST_rhs_$Expression__assoc__left(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(-6928, 4, "$Expression", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-6926, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(-6924, 2, prod__lit_join__char_class___range__106_106_char_class___range__111_111_char_class___range__105_105_char_class___range__110_110_, new int[] {106,111,105,110}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-6923, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-6920, 0, "$Expression", null, null);
      builder.addAlternative(RascalParser.prod__join_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit_join_$layouts_LAYOUTLIST_rhs_$Expression__assoc__left, tmp);
	}
    protected static final void _init_prod__list_$Expression__lit___91_$layouts_LAYOUTLIST_elements_iter_star_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___93_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new LiteralStackNode<IConstructor>(-6397, 4, prod__lit___93__char_class___range__93_93_, new int[] {93}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-6396, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new SeparatedListStackNode<IConstructor>(-6393, 2, regular__iter_star_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(-6387, 0, "$Expression", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(-6389, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode<IConstructor>(-6390, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode<IConstructor>(-6392, 3, "$layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-6385, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-6383, 0, prod__lit___91__char_class___range__91_91_, new int[] {91}, null, null);
      builder.addAlternative(RascalParser.prod__list_$Expression__lit___91_$layouts_LAYOUTLIST_elements_iter_star_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___93_, tmp);
	}
    protected static final void _init_prod__match_$Expression__pattern_$Pattern_$layouts_LAYOUTLIST_lit___58_61_$layouts_LAYOUTLIST_expression_$Expression__assoc__non_assoc(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(-7237, 4, "$Expression", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-7235, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(-7233, 2, prod__lit___58_61__char_class___range__58_58_char_class___range__61_61_, new int[] {58,61}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-7232, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-7229, 0, "$Pattern", null, null);
      builder.addAlternative(RascalParser.prod__match_$Expression__pattern_$Pattern_$layouts_LAYOUTLIST_lit___58_61_$layouts_LAYOUTLIST_expression_$Expression__assoc__non_assoc, tmp);
	}
    protected static final void _init_prod__reifiedType_$Expression__lit_type_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_symbol_$Expression_$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_definitions_$Expression_$layouts_LAYOUTLIST_lit___41_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[11];
      
      tmp[10] = new LiteralStackNode<IConstructor>(-6307, 10, prod__lit___41__char_class___range__41_41_, new int[] {41}, null, null);
      tmp[9] = new NonTerminalStackNode<IConstructor>(-6306, 9, "$layouts_LAYOUTLIST", null, null);
      tmp[8] = new NonTerminalStackNode<IConstructor>(-6303, 8, "$Expression", null, null);
      tmp[7] = new NonTerminalStackNode<IConstructor>(-6301, 7, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new LiteralStackNode<IConstructor>(-6299, 6, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(-6298, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new NonTerminalStackNode<IConstructor>(-6295, 4, "$Expression", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-6293, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(-6291, 2, prod__lit___40__char_class___range__40_40_, new int[] {40}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-6290, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-6288, 0, prod__lit_type__char_class___range__116_116_char_class___range__121_121_char_class___range__112_112_char_class___range__101_101_, new int[] {116,121,112,101}, null, null);
      builder.addAlternative(RascalParser.prod__reifiedType_$Expression__lit_type_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_symbol_$Expression_$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_definitions_$Expression_$layouts_LAYOUTLIST_lit___41_, tmp);
	}
    protected static final void _init_prod__stepRange_$Expression__lit___91_$layouts_LAYOUTLIST_first_$Expression_$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_second_$Expression_$layouts_LAYOUTLIST_lit___46_46_$layouts_LAYOUTLIST_last_$Expression_$layouts_LAYOUTLIST_lit___93_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[13];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-6457, 0, prod__lit___91__char_class___range__91_91_, new int[] {91}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-6459, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(-6461, 2, "$Expression", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-6464, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode<IConstructor>(-6465, 4, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(-6467, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new NonTerminalStackNode<IConstructor>(-6469, 6, "$Expression", null, null);
      tmp[7] = new NonTerminalStackNode<IConstructor>(-6472, 7, "$layouts_LAYOUTLIST", null, null);
      tmp[8] = new LiteralStackNode<IConstructor>(-6473, 8, prod__lit___46_46__char_class___range__46_46_char_class___range__46_46_, new int[] {46,46}, null, null);
      tmp[9] = new NonTerminalStackNode<IConstructor>(-6475, 9, "$layouts_LAYOUTLIST", null, null);
      tmp[10] = new NonTerminalStackNode<IConstructor>(-6477, 10, "$Expression", null, null);
      tmp[11] = new NonTerminalStackNode<IConstructor>(-6480, 11, "$layouts_LAYOUTLIST", null, null);
      tmp[12] = new LiteralStackNode<IConstructor>(-6481, 12, prod__lit___93__char_class___range__93_93_, new int[] {93}, null, null);
      builder.addAlternative(RascalParser.prod__stepRange_$Expression__lit___91_$layouts_LAYOUTLIST_first_$Expression_$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_second_$Expression_$layouts_LAYOUTLIST_lit___46_46_$layouts_LAYOUTLIST_last_$Expression_$layouts_LAYOUTLIST_lit___93_, tmp);
	}
    protected static final void _init_prod__subscript_$Expression__expression_$Expression_$layouts_LAYOUTLIST_lit___91_$layouts_LAYOUTLIST_subscripts_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___93_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[7];
      
      tmp[6] = new LiteralStackNode<IConstructor>(-6362, 6, prod__lit___93__char_class___range__93_93_, new int[] {93}, null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(-6361, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new SeparatedListStackNode<IConstructor>(-6358, 4, regular__iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(-6352, 0, "$Expression", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(-6354, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode<IConstructor>(-6355, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode<IConstructor>(-6357, 3, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-6350, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(-6348, 2, prod__lit___91__char_class___range__91_91_, new int[] {91}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-6347, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-6345, 0, "$Expression", null, null);
      builder.addAlternative(RascalParser.prod__subscript_$Expression__expression_$Expression_$layouts_LAYOUTLIST_lit___91_$layouts_LAYOUTLIST_subscripts_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___93_, tmp);
	}
    protected static final void _init_prod__transitiveClosure_$Expression__argument_$Expression_$layouts_LAYOUTLIST_lit___43_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new LiteralStackNode<IConstructor>(-6338, 2, prod__lit___43__char_class___range__43_43_, new int[] {43}, null, new ICompletionFilter[] {new StringFollowRestriction(new int[] {61})});
      tmp[1] = new NonTerminalStackNode<IConstructor>(-6335, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-6332, 0, "$Expression", null, null);
      builder.addAlternative(RascalParser.prod__transitiveClosure_$Expression__argument_$Expression_$layouts_LAYOUTLIST_lit___43_, tmp);
	}
    protected static final void _init_prod__tuple_$Expression__lit___60_$layouts_LAYOUTLIST_elements_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___62_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new LiteralStackNode<IConstructor>(-6767, 4, prod__lit___62__char_class___range__62_62_, new int[] {62}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-6766, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new SeparatedListStackNode<IConstructor>(-6763, 2, regular__iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(-6757, 0, "$Expression", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(-6759, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode<IConstructor>(-6760, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode<IConstructor>(-6762, 3, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-6755, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-6753, 0, prod__lit___60__char_class___range__60_60_, new int[] {60}, null, null);
      builder.addAlternative(RascalParser.prod__tuple_$Expression__lit___60_$layouts_LAYOUTLIST_elements_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___62_, tmp);
	}
    protected static final void _init_prod__visit_$Expression__label_$Label_$layouts_LAYOUTLIST_visit_$Visit_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode<IConstructor>(-6413, 2, "$Visit", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-6411, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-6408, 0, "$Label", null, null);
      builder.addAlternative(RascalParser.prod__visit_$Expression__label_$Label_$layouts_LAYOUTLIST_visit_$Visit_, tmp);
	}
    protected static final void _init_prod__voidClosure_$Expression__parameters_$Parameters_$layouts_LAYOUTLIST_lit___123_$layouts_LAYOUTLIST_statements_iter_star_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[7];
      
      tmp[6] = new LiteralStackNode<IConstructor>(-6501, 6, prod__lit___125__char_class___range__125_125_, new int[] {125}, null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(-6500, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new SeparatedListStackNode<IConstructor>(-6497, 4, regular__iter_star_seps__$Statement__$layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(-6494, 0, "$Statement", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(-6496, 1, "$layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-6492, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(-6490, 2, prod__lit___123__char_class___range__123_123_, new int[] {123}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-6489, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-6486, 0, "$Parameters", null, null);
      builder.addAlternative(RascalParser.prod__voidClosure_$Expression__parameters_$Parameters_$layouts_LAYOUTLIST_lit___123_$layouts_LAYOUTLIST_statements_iter_star_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_, tmp);
	}
    protected static final void _init_prod__any_$Expression__lit_any_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_generators_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[7];
      
      tmp[6] = new LiteralStackNode<IConstructor>(-6788, 6, prod__lit___41__char_class___range__41_41_, new int[] {41}, null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(-6787, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new SeparatedListStackNode<IConstructor>(-6784, 4, regular__iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(-6778, 0, "$Expression", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(-6780, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode<IConstructor>(-6781, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode<IConstructor>(-6783, 3, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-6776, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(-6774, 2, prod__lit___40__char_class___range__40_40_, new int[] {40}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-6773, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-6771, 0, prod__lit_any__char_class___range__97_97_char_class___range__110_110_char_class___range__121_121_, new int[] {97,110,121}, null, null);
      builder.addAlternative(RascalParser.prod__any_$Expression__lit_any_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_generators_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_, tmp);
	}
    protected static final void _init_prod__callOrTree_$Expression__expression_$Expression_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_arguments_iter_star_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[7];
      
      tmp[6] = new LiteralStackNode<IConstructor>(-6724, 6, prod__lit___41__char_class___range__41_41_, new int[] {41}, null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(-6723, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new SeparatedListStackNode<IConstructor>(-6720, 4, regular__iter_star_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(-6714, 0, "$Expression", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(-6716, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode<IConstructor>(-6717, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode<IConstructor>(-6719, 3, "$layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-6712, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(-6710, 2, prod__lit___40__char_class___range__40_40_, new int[] {40}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-6709, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-6706, 0, "$Expression", null, null);
      builder.addAlternative(RascalParser.prod__callOrTree_$Expression__expression_$Expression_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_arguments_iter_star_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_, tmp);
	}
    protected static final void _init_prod__fieldProject_$Expression__expression_$Expression_$layouts_LAYOUTLIST_lit___60_$layouts_LAYOUTLIST_fields_iter_seps__$Field__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___62_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[7];
      
      tmp[6] = new LiteralStackNode<IConstructor>(-6637, 6, prod__lit___62__char_class___range__62_62_, new int[] {62}, null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(-6636, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new SeparatedListStackNode<IConstructor>(-6633, 4, regular__iter_seps__$Field__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(-6627, 0, "$Field", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(-6629, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode<IConstructor>(-6630, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode<IConstructor>(-6632, 3, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-6625, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(-6623, 2, prod__lit___60__char_class___range__60_60_, new int[] {60}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-6622, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-6620, 0, "$Expression", null, null);
      builder.addAlternative(RascalParser.prod__fieldProject_$Expression__expression_$Expression_$layouts_LAYOUTLIST_lit___60_$layouts_LAYOUTLIST_fields_iter_seps__$Field__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___62_, tmp);
	}
    protected static final void _init_prod__is_$Expression__expression_$Expression_$layouts_LAYOUTLIST_lit_is_$layouts_LAYOUTLIST_name_$Name_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(-6650, 4, "$Name", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-6648, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(-6646, 2, prod__lit_is__char_class___range__105_105_char_class___range__115_115_, new int[] {105,115}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-6645, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-6642, 0, "$Expression", null, null);
      builder.addAlternative(RascalParser.prod__is_$Expression__expression_$Expression_$layouts_LAYOUTLIST_lit_is_$layouts_LAYOUTLIST_name_$Name_, tmp);
	}
    protected static final void _init_prod__it_$Expression__lit_it_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-6508, 0, prod__lit_it__char_class___range__105_105_char_class___range__116_116_, new int[] {105,116}, new IEnterFilter[] {new CharPrecedeRestriction(new int[][]{{65,90},{95,95},{97,122}})}, new ICompletionFilter[] {new CharFollowRestriction(new int[][]{{65,90},{95,95},{97,122}})});
      builder.addAlternative(RascalParser.prod__it_$Expression__lit_it_, tmp);
	}
    protected static final void _init_prod__lessThanOrEq_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___60_61_$layouts_LAYOUTLIST_rhs_$Expression__assoc__non_assoc(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(-7147, 4, "$Expression", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-7145, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(-7143, 2, prod__lit___60_61__char_class___range__60_60_char_class___range__61_61_, new int[] {60,61}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-7142, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-7139, 0, "$Expression", null, null);
      builder.addAlternative(RascalParser.prod__lessThanOrEq_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___60_61_$layouts_LAYOUTLIST_rhs_$Expression__assoc__non_assoc, tmp);
	}
    protected static final void _init_prod__map_$Expression__lit___40_$layouts_LAYOUTLIST_mappings_iter_star_seps__$Mapping__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new LiteralStackNode<IConstructor>(-6613, 4, prod__lit___41__char_class___range__41_41_, new int[] {41}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-6612, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new SeparatedListStackNode<IConstructor>(-6609, 2, regular__iter_star_seps__$Mapping__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(-6603, 0, "$Mapping__$Expression", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(-6605, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode<IConstructor>(-6606, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode<IConstructor>(-6608, 3, "$layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-6599, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-6597, 0, prod__lit___40__char_class___range__40_40_, new int[] {40}, null, null);
      builder.addAlternative(RascalParser.prod__map_$Expression__lit___40_$layouts_LAYOUTLIST_mappings_iter_star_seps__$Mapping__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_, tmp);
	}
    protected static final void _init_prod__modulo_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit_mod_$layouts_LAYOUTLIST_rhs_$Expression__assoc__left(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(-7055, 4, "$Expression", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-7053, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(-7051, 2, prod__lit_mod__char_class___range__109_109_char_class___range__111_111_char_class___range__100_100_, new int[] {109,111,100}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-7050, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-7047, 0, "$Expression", null, null);
      builder.addAlternative(RascalParser.prod__modulo_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit_mod_$layouts_LAYOUTLIST_rhs_$Expression__assoc__left, tmp);
	}
    protected static final void _init_prod__or_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___124_124_$layouts_LAYOUTLIST_rhs_$Expression__assoc__left(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(-7296, 4, "$Expression", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-7294, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(-7292, 2, prod__lit___124_124__char_class___range__124_124_char_class___range__124_124_, new int[] {124,124}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-7291, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-7288, 0, "$Expression", null, null);
      builder.addAlternative(RascalParser.prod__or_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___124_124_$layouts_LAYOUTLIST_rhs_$Expression__assoc__left, tmp);
	}
    protected static final void _init_prod__product_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___42_$layouts_LAYOUTLIST_empty_$layouts_LAYOUTLIST_rhs_$Expression__assoc__left(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[7];
      
      tmp[6] = new NonTerminalStackNode<IConstructor>(-6962, 6, "$Expression", null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(-6959, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new EmptyStackNode<IConstructor>(-6957, 4, regular__empty, null, new ICompletionFilter[] {new StringFollowRestriction(new int[] {42})});
      tmp[3] = new NonTerminalStackNode<IConstructor>(-6954, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(-6952, 2, prod__lit___42__char_class___range__42_42_, new int[] {42}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-6951, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-6948, 0, "$Expression", null, null);
      builder.addAlternative(RascalParser.prod__product_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___42_$layouts_LAYOUTLIST_empty_$layouts_LAYOUTLIST_rhs_$Expression__assoc__left, tmp);
	}
    protected static final void _init_prod__reducer_$Expression__lit___40_$layouts_LAYOUTLIST_init_$Expression_$layouts_LAYOUTLIST_lit___124_$layouts_LAYOUTLIST_result_$Expression_$layouts_LAYOUTLIST_lit___124_$layouts_LAYOUTLIST_generators_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[13];
      
      tmp[12] = new LiteralStackNode<IConstructor>(-6685, 12, prod__lit___41__char_class___range__41_41_, new int[] {41}, null, null);
      tmp[11] = new NonTerminalStackNode<IConstructor>(-6684, 11, "$layouts_LAYOUTLIST", null, null);
      tmp[10] = new SeparatedListStackNode<IConstructor>(-6681, 10, regular__iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(-6675, 0, "$Expression", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(-6677, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode<IConstructor>(-6678, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode<IConstructor>(-6680, 3, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[9] = new NonTerminalStackNode<IConstructor>(-6673, 9, "$layouts_LAYOUTLIST", null, null);
      tmp[8] = new LiteralStackNode<IConstructor>(-6671, 8, prod__lit___124__char_class___range__124_124_, new int[] {124}, null, null);
      tmp[7] = new NonTerminalStackNode<IConstructor>(-6670, 7, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new NonTerminalStackNode<IConstructor>(-6667, 6, "$Expression", null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(-6665, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode<IConstructor>(-6663, 4, prod__lit___124__char_class___range__124_124_, new int[] {124}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-6662, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(-6659, 2, "$Expression", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-6657, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-6655, 0, prod__lit___40__char_class___range__40_40_, new int[] {40}, null, null);
      builder.addAlternative(RascalParser.prod__reducer_$Expression__lit___40_$layouts_LAYOUTLIST_init_$Expression_$layouts_LAYOUTLIST_lit___124_$layouts_LAYOUTLIST_result_$Expression_$layouts_LAYOUTLIST_lit___124_$layouts_LAYOUTLIST_generators_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_, tmp);
	}
    protected static final void _init_prod__reifyType_$Expression__lit___35_$layouts_LAYOUTLIST_type_$Type_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode<IConstructor>(-6827, 2, "$Type", null, new ICompletionFilter[] {new StringFollowRestriction(new int[] {91})});
      tmp[1] = new NonTerminalStackNode<IConstructor>(-6822, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-6820, 0, prod__lit___35__char_class___range__35_35_, new int[] {35}, null, null);
      builder.addAlternative(RascalParser.prod__reifyType_$Expression__lit___35_$layouts_LAYOUTLIST_type_$Type_, tmp);
	}
    protected static final void _init_prod__setAnnotation_$Expression__expression_$Expression_$layouts_LAYOUTLIST_lit___91_$layouts_LAYOUTLIST_lit___64_$layouts_LAYOUTLIST_name_$Name_$layouts_LAYOUTLIST_lit___61_$layouts_LAYOUTLIST_value_$Expression_$layouts_LAYOUTLIST_lit___93_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[13];
      
      tmp[12] = new LiteralStackNode<IConstructor>(-6816, 12, prod__lit___93__char_class___range__93_93_, new int[] {93}, null, null);
      tmp[11] = new NonTerminalStackNode<IConstructor>(-6815, 11, "$layouts_LAYOUTLIST", null, null);
      tmp[10] = new NonTerminalStackNode<IConstructor>(-6812, 10, "$Expression", null, null);
      tmp[9] = new NonTerminalStackNode<IConstructor>(-6810, 9, "$layouts_LAYOUTLIST", null, null);
      tmp[8] = new LiteralStackNode<IConstructor>(-6808, 8, prod__lit___61__char_class___range__61_61_, new int[] {61}, null, null);
      tmp[7] = new NonTerminalStackNode<IConstructor>(-6807, 7, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new NonTerminalStackNode<IConstructor>(-6804, 6, "$Name", null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(-6802, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode<IConstructor>(-6800, 4, prod__lit___64__char_class___range__64_64_, new int[] {64}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-6799, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(-6797, 2, prod__lit___91__char_class___range__91_91_, new int[] {91}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-6796, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-6793, 0, "$Expression", null, null);
      builder.addAlternative(RascalParser.prod__setAnnotation_$Expression__expression_$Expression_$layouts_LAYOUTLIST_lit___91_$layouts_LAYOUTLIST_lit___64_$layouts_LAYOUTLIST_name_$Name_$layouts_LAYOUTLIST_lit___61_$layouts_LAYOUTLIST_value_$Expression_$layouts_LAYOUTLIST_lit___93_, tmp);
	}
    protected static final void _init_prod__splice_$Expression__lit___42_$layouts_LAYOUTLIST_argument_$Expression__assoc__non_assoc(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode<IConstructor>(-6856, 2, "$Expression", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-6854, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-6852, 0, prod__lit___42__char_class___range__42_42_, new int[] {42}, null, null);
      builder.addAlternative(RascalParser.prod__splice_$Expression__lit___42_$layouts_LAYOUTLIST_argument_$Expression__assoc__non_assoc, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__addition_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___43_$layouts_LAYOUTLIST_rhs_$Expression__assoc__left(builder);
      
        _init_prod__appendAfter_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___60_60_$layouts_LAYOUTLIST_rhs_$Expression__assoc__left(builder);
      
        _init_prod__asType_$Expression__lit___91_$layouts_LAYOUTLIST_type_$Type_$layouts_LAYOUTLIST_lit___93_$layouts_LAYOUTLIST_argument_$Expression_(builder);
      
        _init_prod__composition_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit_o_$layouts_LAYOUTLIST_rhs_$Expression__assoc__left(builder);
      
        _init_prod__comprehension_$Expression__comprehension_$Comprehension_(builder);
      
        _init_prod__division_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___47_$layouts_LAYOUTLIST_rhs_$Expression__assoc__left(builder);
      
        _init_prod__ifDefinedOtherwise_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___63_$layouts_LAYOUTLIST_rhs_$Expression__assoc__non_assoc(builder);
      
        _init_prod__ifThenElse_$Expression__condition_$Expression_$layouts_LAYOUTLIST_lit___63_$layouts_LAYOUTLIST_thenExp_$Expression_$layouts_LAYOUTLIST_lit___58_$layouts_LAYOUTLIST_elseExp_$Expression__assoc__right(builder);
      
        _init_prod__implication_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___61_61_62_$layouts_LAYOUTLIST_rhs_$Expression__assoc__non_assoc(builder);
      
        _init_prod__insertBefore_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___62_62_$layouts_LAYOUTLIST_rhs_$Expression__assoc__left(builder);
      
        _init_prod__lessThan_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___60_$layouts_LAYOUTLIST_rhs_$Expression__assoc__non_assoc(builder);
      
        _init_prod__literal_$Expression__literal_$Literal_(builder);
      
        _init_prod__nonEquals_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___33_61_$layouts_LAYOUTLIST_rhs_$Expression__assoc__non_assoc(builder);
      
        _init_prod__range_$Expression__lit___91_$layouts_LAYOUTLIST_first_$Expression_$layouts_LAYOUTLIST_lit___46_46_$layouts_LAYOUTLIST_last_$Expression_$layouts_LAYOUTLIST_lit___93_(builder);
      
        _init_prod__remainder_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___37_$layouts_LAYOUTLIST_rhs_$Expression__assoc__left(builder);
      
        _init_prod__set_$Expression__lit___123_$layouts_LAYOUTLIST_elements_iter_star_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_(builder);
      
        _init_prod__transitiveReflexiveClosure_$Expression__argument_$Expression_$layouts_LAYOUTLIST_lit___42_(builder);
      
        _init_prod__bracket_$Expression__lit___40_$layouts_LAYOUTLIST_expression_$Expression_$layouts_LAYOUTLIST_lit___41__bracket(builder);
      
        _init_prod__closure_$Expression__type_$Type_$layouts_LAYOUTLIST_parameters_$Parameters_$layouts_LAYOUTLIST_lit___123_$layouts_LAYOUTLIST_statements_iter_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_(builder);
      
        _init_prod__enumerator_$Expression__pattern_$Pattern_$layouts_LAYOUTLIST_lit___60_45_$layouts_LAYOUTLIST_expression_$Expression__assoc__non_assoc(builder);
      
        _init_prod__equivalence_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___60_61_61_62_$layouts_LAYOUTLIST_rhs_$Expression__assoc__non_assoc(builder);
      
        _init_prod__fieldAccess_$Expression__expression_$Expression_$layouts_LAYOUTLIST_lit___46_$layouts_LAYOUTLIST_field_$Name_(builder);
      
        _init_prod__getAnnotation_$Expression__expression_$Expression_$layouts_LAYOUTLIST_lit___64_$layouts_LAYOUTLIST_name_$Name_(builder);
      
        _init_prod__greaterThan_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___62_$layouts_LAYOUTLIST_rhs_$Expression__assoc__non_assoc(builder);
      
        _init_prod__greaterThanOrEq_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___62_61_$layouts_LAYOUTLIST_rhs_$Expression__assoc__non_assoc(builder);
      
        _init_prod__intersection_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___38_$layouts_LAYOUTLIST_rhs_$Expression__assoc__left(builder);
      
        _init_prod__isDefined_$Expression__argument_$Expression_$layouts_LAYOUTLIST_lit___63_(builder);
      
        _init_prod__negation_$Expression__lit___33_$layouts_LAYOUTLIST_argument_$Expression_(builder);
      
        _init_prod__negative_$Expression__lit___$layouts_LAYOUTLIST_argument_$Expression_(builder);
      
        _init_prod__noMatch_$Expression__pattern_$Pattern_$layouts_LAYOUTLIST_lit___33_58_61_$layouts_LAYOUTLIST_expression_$Expression__assoc__non_assoc(builder);
      
        _init_prod__nonEmptyBlock_$Expression__lit___123_$layouts_LAYOUTLIST_statements_iter_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_(builder);
      
        _init_prod__notIn_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit_notin_$layouts_LAYOUTLIST_rhs_$Expression__assoc__non_assoc(builder);
      
        _init_prod__qualifiedName_$Expression__qualifiedName_$QualifiedName_(builder);
      
        _init_prod__subtraction_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___$layouts_LAYOUTLIST_rhs_$Expression__assoc__left(builder);
      
        _init_prod__all_$Expression__lit_all_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_generators_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_(builder);
      
        _init_prod__and_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___38_38_$layouts_LAYOUTLIST_rhs_$Expression__assoc__left(builder);
      
        _init_prod__concrete_$Expression__concrete_$Concrete_(builder);
      
        _init_prod__equals_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___61_61_$layouts_LAYOUTLIST_rhs_$Expression__assoc__non_assoc(builder);
      
        _init_prod__fieldUpdate_$Expression__expression_$Expression_$layouts_LAYOUTLIST_lit___91_$layouts_LAYOUTLIST_key_$Name_$layouts_LAYOUTLIST_lit___61_$layouts_LAYOUTLIST_replacement_$Expression_$layouts_LAYOUTLIST_lit___93_(builder);
      
        _init_prod__has_$Expression__expression_$Expression_$layouts_LAYOUTLIST_lit_has_$layouts_LAYOUTLIST_name_$Name_(builder);
      
        _init_prod__in_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit_in_$layouts_LAYOUTLIST_rhs_$Expression__assoc__non_assoc(builder);
      
        _init_prod__join_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit_join_$layouts_LAYOUTLIST_rhs_$Expression__assoc__left(builder);
      
        _init_prod__list_$Expression__lit___91_$layouts_LAYOUTLIST_elements_iter_star_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___93_(builder);
      
        _init_prod__match_$Expression__pattern_$Pattern_$layouts_LAYOUTLIST_lit___58_61_$layouts_LAYOUTLIST_expression_$Expression__assoc__non_assoc(builder);
      
        _init_prod__reifiedType_$Expression__lit_type_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_symbol_$Expression_$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_definitions_$Expression_$layouts_LAYOUTLIST_lit___41_(builder);
      
        _init_prod__stepRange_$Expression__lit___91_$layouts_LAYOUTLIST_first_$Expression_$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_second_$Expression_$layouts_LAYOUTLIST_lit___46_46_$layouts_LAYOUTLIST_last_$Expression_$layouts_LAYOUTLIST_lit___93_(builder);
      
        _init_prod__subscript_$Expression__expression_$Expression_$layouts_LAYOUTLIST_lit___91_$layouts_LAYOUTLIST_subscripts_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___93_(builder);
      
        _init_prod__transitiveClosure_$Expression__argument_$Expression_$layouts_LAYOUTLIST_lit___43_(builder);
      
        _init_prod__tuple_$Expression__lit___60_$layouts_LAYOUTLIST_elements_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___62_(builder);
      
        _init_prod__visit_$Expression__label_$Label_$layouts_LAYOUTLIST_visit_$Visit_(builder);
      
        _init_prod__voidClosure_$Expression__parameters_$Parameters_$layouts_LAYOUTLIST_lit___123_$layouts_LAYOUTLIST_statements_iter_star_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_(builder);
      
        _init_prod__any_$Expression__lit_any_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_generators_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_(builder);
      
        _init_prod__callOrTree_$Expression__expression_$Expression_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_arguments_iter_star_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_(builder);
      
        _init_prod__fieldProject_$Expression__expression_$Expression_$layouts_LAYOUTLIST_lit___60_$layouts_LAYOUTLIST_fields_iter_seps__$Field__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___62_(builder);
      
        _init_prod__is_$Expression__expression_$Expression_$layouts_LAYOUTLIST_lit_is_$layouts_LAYOUTLIST_name_$Name_(builder);
      
        _init_prod__it_$Expression__lit_it_(builder);
      
        _init_prod__lessThanOrEq_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___60_61_$layouts_LAYOUTLIST_rhs_$Expression__assoc__non_assoc(builder);
      
        _init_prod__map_$Expression__lit___40_$layouts_LAYOUTLIST_mappings_iter_star_seps__$Mapping__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_(builder);
      
        _init_prod__modulo_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit_mod_$layouts_LAYOUTLIST_rhs_$Expression__assoc__left(builder);
      
        _init_prod__or_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___124_124_$layouts_LAYOUTLIST_rhs_$Expression__assoc__left(builder);
      
        _init_prod__product_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___42_$layouts_LAYOUTLIST_empty_$layouts_LAYOUTLIST_rhs_$Expression__assoc__left(builder);
      
        _init_prod__reducer_$Expression__lit___40_$layouts_LAYOUTLIST_init_$Expression_$layouts_LAYOUTLIST_lit___124_$layouts_LAYOUTLIST_result_$Expression_$layouts_LAYOUTLIST_lit___124_$layouts_LAYOUTLIST_generators_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_(builder);
      
        _init_prod__reifyType_$Expression__lit___35_$layouts_LAYOUTLIST_type_$Type_(builder);
      
        _init_prod__setAnnotation_$Expression__expression_$Expression_$layouts_LAYOUTLIST_lit___91_$layouts_LAYOUTLIST_lit___64_$layouts_LAYOUTLIST_name_$Name_$layouts_LAYOUTLIST_lit___61_$layouts_LAYOUTLIST_value_$Expression_$layouts_LAYOUTLIST_lit___93_(builder);
      
        _init_prod__splice_$Expression__lit___42_$layouts_LAYOUTLIST_argument_$Expression__assoc__non_assoc(builder);
      
    }
  }
	
  protected static class $Mapping__$Pattern {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__default_$Mapping__$Pattern__from_$Pattern_$layouts_LAYOUTLIST_lit___58_$layouts_LAYOUTLIST_to_$Pattern_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(-6251, 4, "$Pattern", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-6249, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(-6247, 2, prod__lit___58__char_class___range__58_58_, new int[] {58}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-6246, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-6243, 0, "$Pattern", null, null);
      builder.addAlternative(RascalParser.prod__default_$Mapping__$Pattern__from_$Pattern_$layouts_LAYOUTLIST_lit___58_$layouts_LAYOUTLIST_to_$Pattern_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__default_$Mapping__$Pattern__from_$Pattern_$layouts_LAYOUTLIST_lit___58_$layouts_LAYOUTLIST_to_$Pattern_(builder);
      
    }
  }
	
  protected static class $ImportedModule {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__actuals_$ImportedModule__name_$QualifiedName_$layouts_LAYOUTLIST_actuals_$ModuleActuals_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode<IConstructor>(-7382, 2, "$ModuleActuals", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-7380, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-7377, 0, "$QualifiedName", null, null);
      builder.addAlternative(RascalParser.prod__actuals_$ImportedModule__name_$QualifiedName_$layouts_LAYOUTLIST_actuals_$ModuleActuals_, tmp);
	}
    protected static final void _init_prod__actualsRenaming_$ImportedModule__name_$QualifiedName_$layouts_LAYOUTLIST_actuals_$ModuleActuals_$layouts_LAYOUTLIST_renamings_$Renamings_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(-7398, 4, "$Renamings", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-7396, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(-7393, 2, "$ModuleActuals", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-7391, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-7388, 0, "$QualifiedName", null, null);
      builder.addAlternative(RascalParser.prod__actualsRenaming_$ImportedModule__name_$QualifiedName_$layouts_LAYOUTLIST_actuals_$ModuleActuals_$layouts_LAYOUTLIST_renamings_$Renamings_, tmp);
	}
    protected static final void _init_prod__default_$ImportedModule__name_$QualifiedName_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(-7404, 0, "$QualifiedName", null, null);
      builder.addAlternative(RascalParser.prod__default_$ImportedModule__name_$QualifiedName_, tmp);
	}
    protected static final void _init_prod__renamings_$ImportedModule__name_$QualifiedName_$layouts_LAYOUTLIST_renamings_$Renamings_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode<IConstructor>(-7415, 2, "$Renamings", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-7413, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-7410, 0, "$QualifiedName", null, null);
      builder.addAlternative(RascalParser.prod__renamings_$ImportedModule__name_$QualifiedName_$layouts_LAYOUTLIST_renamings_$Renamings_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__actuals_$ImportedModule__name_$QualifiedName_$layouts_LAYOUTLIST_actuals_$ModuleActuals_(builder);
      
        _init_prod__actualsRenaming_$ImportedModule__name_$QualifiedName_$layouts_LAYOUTLIST_actuals_$ModuleActuals_$layouts_LAYOUTLIST_renamings_$Renamings_(builder);
      
        _init_prod__default_$ImportedModule__name_$QualifiedName_(builder);
      
        _init_prod__renamings_$ImportedModule__name_$QualifiedName_$layouts_LAYOUTLIST_renamings_$Renamings_(builder);
      
    }
  }
	
  protected static class $Catch {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__binding_$Catch__lit_catch_$layouts_LAYOUTLIST_pattern_$Pattern_$layouts_LAYOUTLIST_lit___58_$layouts_LAYOUTLIST_body_$Statement_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[7];
      
      tmp[6] = new NonTerminalStackNode<IConstructor>(-7996, 6, "$Statement", null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(-7994, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode<IConstructor>(-7992, 4, prod__lit___58__char_class___range__58_58_, new int[] {58}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-7991, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(-7988, 2, "$Pattern", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-7986, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-7984, 0, prod__lit_catch__char_class___range__99_99_char_class___range__97_97_char_class___range__116_116_char_class___range__99_99_char_class___range__104_104_, new int[] {99,97,116,99,104}, null, null);
      builder.addAlternative(RascalParser.prod__binding_$Catch__lit_catch_$layouts_LAYOUTLIST_pattern_$Pattern_$layouts_LAYOUTLIST_lit___58_$layouts_LAYOUTLIST_body_$Statement_, tmp);
	}
    protected static final void _init_prod__default_$Catch__lit_catch_$layouts_LAYOUTLIST_lit___58_$layouts_LAYOUTLIST_body_$Statement_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(-8008, 4, "$Statement", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-8006, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(-8004, 2, prod__lit___58__char_class___range__58_58_, new int[] {58}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-8003, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-8001, 0, prod__lit_catch__char_class___range__99_99_char_class___range__97_97_char_class___range__116_116_char_class___range__99_99_char_class___range__104_104_, new int[] {99,97,116,99,104}, null, null);
      builder.addAlternative(RascalParser.prod__default_$Catch__lit_catch_$layouts_LAYOUTLIST_lit___58_$layouts_LAYOUTLIST_body_$Statement_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__binding_$Catch__lit_catch_$layouts_LAYOUTLIST_pattern_$Pattern_$layouts_LAYOUTLIST_lit___58_$layouts_LAYOUTLIST_body_$Statement_(builder);
      
        _init_prod__default_$Catch__lit_catch_$layouts_LAYOUTLIST_lit___58_$layouts_LAYOUTLIST_body_$Statement_(builder);
      
    }
  }
	
  protected static class $Commands {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__list_$Commands__commands_iter_seps__$EvalCommand__$layouts_LAYOUTLIST_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new SeparatedListStackNode<IConstructor>(-7475, 0, regular__iter_seps__$EvalCommand__$layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(-7472, 0, "$EvalCommand", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(-7474, 1, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      builder.addAlternative(RascalParser.prod__list_$Commands__commands_iter_seps__$EvalCommand__$layouts_LAYOUTLIST_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__list_$Commands__commands_iter_seps__$EvalCommand__$layouts_LAYOUTLIST_(builder);
      
    }
  }
	
  protected static class $Concrete {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__typed_$Concrete__lit___40_$layouts_LAYOUTLIST_$Sym_$layouts_LAYOUTLIST_lit___41_$layouts_LAYOUTLIST_lit___96_$layouts_LAYOUTLIST_parts_iter_star_seps__$ConcretePart__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___96_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[11];
      
      tmp[10] = new LiteralStackNode<IConstructor>(-7958, 10, prod__lit___96__char_class___range__96_96_, new int[] {96}, null, null);
      tmp[9] = new NonTerminalStackNode<IConstructor>(-7957, 9, "$layouts_LAYOUTLIST", null, null);
      tmp[8] = new SeparatedListStackNode<IConstructor>(-7954, 8, regular__iter_star_seps__$ConcretePart__$layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(-7951, 0, "$ConcretePart", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(-7953, 1, "$layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[7] = new NonTerminalStackNode<IConstructor>(-7949, 7, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new LiteralStackNode<IConstructor>(-7947, 6, prod__lit___96__char_class___range__96_96_, new int[] {96}, null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(-7946, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode<IConstructor>(-7944, 4, prod__lit___41__char_class___range__41_41_, new int[] {41}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-7943, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(-7941, 2, "$Sym", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-7939, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-7937, 0, prod__lit___40__char_class___range__40_40_, new int[] {40}, null, null);
      builder.addAlternative(RascalParser.prod__typed_$Concrete__lit___40_$layouts_LAYOUTLIST_$Sym_$layouts_LAYOUTLIST_lit___41_$layouts_LAYOUTLIST_lit___96_$layouts_LAYOUTLIST_parts_iter_star_seps__$ConcretePart__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___96_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__typed_$Concrete__lit___40_$layouts_LAYOUTLIST_$Sym_$layouts_LAYOUTLIST_lit___41_$layouts_LAYOUTLIST_lit___96_$layouts_LAYOUTLIST_parts_iter_star_seps__$ConcretePart__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___96_(builder);
      
    }
  }
	
  protected static class $Header {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__default_$Header__tags_$Tags_$layouts_LAYOUTLIST_lit_module_$layouts_LAYOUTLIST_name_$QualifiedName_$layouts_LAYOUTLIST_imports_iter_star_seps__$Import__$layouts_LAYOUTLIST_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[7];
      
      tmp[6] = new SeparatedListStackNode<IConstructor>(-7522, 6, regular__iter_star_seps__$Import__$layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(-7519, 0, "$Import", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(-7521, 1, "$layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(-7517, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new NonTerminalStackNode<IConstructor>(-7514, 4, "$QualifiedName", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-7512, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(-7510, 2, prod__lit_module__char_class___range__109_109_char_class___range__111_111_char_class___range__100_100_char_class___range__117_117_char_class___range__108_108_char_class___range__101_101_, new int[] {109,111,100,117,108,101}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-7509, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-7506, 0, "$Tags", null, null);
      builder.addAlternative(RascalParser.prod__default_$Header__tags_$Tags_$layouts_LAYOUTLIST_lit_module_$layouts_LAYOUTLIST_name_$QualifiedName_$layouts_LAYOUTLIST_imports_iter_star_seps__$Import__$layouts_LAYOUTLIST_, tmp);
	}
    protected static final void _init_prod__parameters_$Header__tags_$Tags_$layouts_LAYOUTLIST_lit_module_$layouts_LAYOUTLIST_name_$QualifiedName_$layouts_LAYOUTLIST_params_$ModuleParameters_$layouts_LAYOUTLIST_imports_iter_star_seps__$Import__$layouts_LAYOUTLIST_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[9];
      
      tmp[8] = new SeparatedListStackNode<IConstructor>(-7549, 8, regular__iter_star_seps__$Import__$layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(-7546, 0, "$Import", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(-7548, 1, "$layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[7] = new NonTerminalStackNode<IConstructor>(-7544, 7, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new NonTerminalStackNode<IConstructor>(-7541, 6, "$ModuleParameters", null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(-7539, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new NonTerminalStackNode<IConstructor>(-7536, 4, "$QualifiedName", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-7534, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(-7532, 2, prod__lit_module__char_class___range__109_109_char_class___range__111_111_char_class___range__100_100_char_class___range__117_117_char_class___range__108_108_char_class___range__101_101_, new int[] {109,111,100,117,108,101}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-7531, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-7528, 0, "$Tags", null, null);
      builder.addAlternative(RascalParser.prod__parameters_$Header__tags_$Tags_$layouts_LAYOUTLIST_lit_module_$layouts_LAYOUTLIST_name_$QualifiedName_$layouts_LAYOUTLIST_params_$ModuleParameters_$layouts_LAYOUTLIST_imports_iter_star_seps__$Import__$layouts_LAYOUTLIST_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__default_$Header__tags_$Tags_$layouts_LAYOUTLIST_lit_module_$layouts_LAYOUTLIST_name_$QualifiedName_$layouts_LAYOUTLIST_imports_iter_star_seps__$Import__$layouts_LAYOUTLIST_(builder);
      
        _init_prod__parameters_$Header__tags_$Tags_$layouts_LAYOUTLIST_lit_module_$layouts_LAYOUTLIST_name_$QualifiedName_$layouts_LAYOUTLIST_params_$ModuleParameters_$layouts_LAYOUTLIST_imports_iter_star_seps__$Import__$layouts_LAYOUTLIST_(builder);
      
    }
  }
	
  protected static class $NonterminalLabel {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__$NonterminalLabel__char_class___range__97_122_iter_star__char_class___range__48_57_range__65_90_range__95_95_range__97_122_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[2];
      
      tmp[1] = new ListStackNode<IConstructor>(-7561, 1, regular__iter_star__char_class___range__48_57_range__65_90_range__95_95_range__97_122, new CharStackNode<IConstructor>(-7558, 0, new int[][]{{48,57},{65,90},{95,95},{97,122}}, null, null), false, null, new ICompletionFilter[] {new CharFollowRestriction(new int[][]{{48,57},{65,90},{95,95},{97,122}})});
      tmp[0] = new CharStackNode<IConstructor>(-7557, 0, new int[][]{{97,122}}, null, null);
      builder.addAlternative(RascalParser.prod__$NonterminalLabel__char_class___range__97_122_iter_star__char_class___range__48_57_range__65_90_range__95_95_range__97_122_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__$NonterminalLabel__char_class___range__97_122_iter_star__char_class___range__48_57_range__65_90_range__95_95_range__97_122_(builder);
      
    }
  }
	
  protected static class $Renaming {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__default_$Renaming__from_$Name_$layouts_LAYOUTLIST_lit___61_62_$layouts_LAYOUTLIST_to_$Name_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(-7975, 4, "$Name", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-7973, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(-7971, 2, prod__lit___61_62__char_class___range__61_61_char_class___range__62_62_, new int[] {61,62}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-7970, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-7967, 0, "$Name", null, null);
      builder.addAlternative(RascalParser.prod__default_$Renaming__from_$Name_$layouts_LAYOUTLIST_lit___61_62_$layouts_LAYOUTLIST_to_$Name_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__default_$Renaming__from_$Name_$layouts_LAYOUTLIST_lit___61_62_$layouts_LAYOUTLIST_to_$Name_(builder);
      
    }
  }
	
  protected static class $Sym {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__alternative_$Sym__lit___40_$layouts_LAYOUTLIST_first_$Sym_$layouts_LAYOUTLIST_lit___124_$layouts_LAYOUTLIST_alternatives_iter_seps__$Sym__$layouts_LAYOUTLIST_lit___124_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[9];
      
      tmp[8] = new LiteralStackNode<IConstructor>(-7776, 8, prod__lit___41__char_class___range__41_41_, new int[] {41}, null, null);
      tmp[7] = new NonTerminalStackNode<IConstructor>(-7775, 7, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new SeparatedListStackNode<IConstructor>(-7772, 6, regular__iter_seps__$Sym__$layouts_LAYOUTLIST_lit___124_$layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(-7766, 0, "$Sym", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(-7768, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode<IConstructor>(-7769, 2, prod__lit___124__char_class___range__124_124_, new int[] {124}, null, null), new NonTerminalStackNode<IConstructor>(-7771, 3, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(-7764, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode<IConstructor>(-7762, 4, prod__lit___124__char_class___range__124_124_, new int[] {124}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-7761, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(-7758, 2, "$Sym", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-7756, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-7754, 0, prod__lit___40__char_class___range__40_40_, new int[] {40}, null, null);
      builder.addAlternative(RascalParser.prod__alternative_$Sym__lit___40_$layouts_LAYOUTLIST_first_$Sym_$layouts_LAYOUTLIST_lit___124_$layouts_LAYOUTLIST_alternatives_iter_seps__$Sym__$layouts_LAYOUTLIST_lit___124_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_, tmp);
	}
    protected static final void _init_prod__caseInsensitiveLiteral_$Sym__cistring_$CaseInsensitiveStringConstant_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(-7631, 0, "$CaseInsensitiveStringConstant", null, null);
      builder.addAlternative(RascalParser.prod__caseInsensitiveLiteral_$Sym__cistring_$CaseInsensitiveStringConstant_, tmp);
	}
    protected static final void _init_prod__characterClass_$Sym__charClass_$Class_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(-7801, 0, "$Class", null, null);
      builder.addAlternative(RascalParser.prod__characterClass_$Sym__charClass_$Class_, tmp);
	}
    protected static final void _init_prod__column_$Sym__symbol_$Sym_$layouts_LAYOUTLIST_lit___64_$layouts_LAYOUTLIST_column_$IntegerLiteral_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(-7729, 4, "$IntegerLiteral", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-7727, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(-7725, 2, prod__lit___64__char_class___range__64_64_, new int[] {64}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-7724, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-7721, 0, "$Sym", null, null);
      builder.addAlternative(RascalParser.prod__column_$Sym__symbol_$Sym_$layouts_LAYOUTLIST_lit___64_$layouts_LAYOUTLIST_column_$IntegerLiteral_, tmp);
	}
    protected static final void _init_prod__empty_$Sym__lit___40_$layouts_LAYOUTLIST_lit___41_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-7705, 0, prod__lit___40__char_class___range__40_40_, new int[] {40}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-7707, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(-7708, 2, prod__lit___41__char_class___range__41_41_, new int[] {41}, null, null);
      builder.addAlternative(RascalParser.prod__empty_$Sym__lit___40_$layouts_LAYOUTLIST_lit___41_, tmp);
	}
    protected static final void _init_prod__endOfLine_$Sym__symbol_$Sym_$layouts_LAYOUTLIST_lit___36_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new LiteralStackNode<IConstructor>(-7603, 2, prod__lit___36__char_class___range__36_36_, new int[] {36}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-7602, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-7599, 0, "$Sym", null, null);
      builder.addAlternative(RascalParser.prod__endOfLine_$Sym__symbol_$Sym_$layouts_LAYOUTLIST_lit___36_, tmp);
	}
    protected static final void _init_prod__except_$Sym__symbol_$Sym_$layouts_LAYOUTLIST_lit___33_$layouts_LAYOUTLIST_label_$NonterminalLabel_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(-7616, 4, "$NonterminalLabel", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-7614, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(-7612, 2, prod__lit___33__char_class___range__33_33_, new int[] {33}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-7611, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-7608, 0, "$Sym", null, null);
      builder.addAlternative(RascalParser.prod__except_$Sym__symbol_$Sym_$layouts_LAYOUTLIST_lit___33_$layouts_LAYOUTLIST_label_$NonterminalLabel_, tmp);
	}
    protected static final void _init_prod__follow_$Sym__symbol_$Sym_$layouts_LAYOUTLIST_lit___62_62_$layouts_LAYOUTLIST_match_$Sym__assoc__left(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(-7883, 4, "$Sym", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-7881, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(-7879, 2, prod__lit___62_62__char_class___range__62_62_char_class___range__62_62_, new int[] {62,62}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-7878, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-7875, 0, "$Sym", null, null);
      builder.addAlternative(RascalParser.prod__follow_$Sym__symbol_$Sym_$layouts_LAYOUTLIST_lit___62_62_$layouts_LAYOUTLIST_match_$Sym__assoc__left, tmp);
	}
    protected static final void _init_prod__iter_$Sym__symbol_$Sym_$layouts_LAYOUTLIST_lit___43_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new LiteralStackNode<IConstructor>(-7837, 2, prod__lit___43__char_class___range__43_43_, new int[] {43}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-7836, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-7833, 0, "$Sym", null, null);
      builder.addAlternative(RascalParser.prod__iter_$Sym__symbol_$Sym_$layouts_LAYOUTLIST_lit___43_, tmp);
	}
    protected static final void _init_prod__iterSep_$Sym__lit___123_$layouts_LAYOUTLIST_symbol_$Sym_$layouts_LAYOUTLIST_sep_$Sym_$layouts_LAYOUTLIST_lit___125_$layouts_LAYOUTLIST_lit___43_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[9];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-7636, 0, prod__lit___123__char_class___range__123_123_, new int[] {123}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-7638, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(-7640, 2, "$Sym", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-7643, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new NonTerminalStackNode<IConstructor>(-7645, 4, "$Sym", null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(-7648, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new LiteralStackNode<IConstructor>(-7649, 6, prod__lit___125__char_class___range__125_125_, new int[] {125}, null, null);
      tmp[7] = new NonTerminalStackNode<IConstructor>(-7651, 7, "$layouts_LAYOUTLIST", null, null);
      tmp[8] = new LiteralStackNode<IConstructor>(-7652, 8, prod__lit___43__char_class___range__43_43_, new int[] {43}, null, null);
      builder.addAlternative(RascalParser.prod__iterSep_$Sym__lit___123_$layouts_LAYOUTLIST_symbol_$Sym_$layouts_LAYOUTLIST_sep_$Sym_$layouts_LAYOUTLIST_lit___125_$layouts_LAYOUTLIST_lit___43_, tmp);
	}
    protected static final void _init_prod__iterStar_$Sym__symbol_$Sym_$layouts_LAYOUTLIST_lit___42_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new LiteralStackNode<IConstructor>(-7667, 2, prod__lit___42__char_class___range__42_42_, new int[] {42}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-7666, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-7663, 0, "$Sym", null, null);
      builder.addAlternative(RascalParser.prod__iterStar_$Sym__symbol_$Sym_$layouts_LAYOUTLIST_lit___42_, tmp);
	}
    protected static final void _init_prod__iterStarSep_$Sym__lit___123_$layouts_LAYOUTLIST_symbol_$Sym_$layouts_LAYOUTLIST_sep_$Sym_$layouts_LAYOUTLIST_lit___125_$layouts_LAYOUTLIST_lit___42_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[9];
      
      tmp[8] = new LiteralStackNode<IConstructor>(-7796, 8, prod__lit___42__char_class___range__42_42_, new int[] {42}, null, null);
      tmp[7] = new NonTerminalStackNode<IConstructor>(-7795, 7, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new LiteralStackNode<IConstructor>(-7793, 6, prod__lit___125__char_class___range__125_125_, new int[] {125}, null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(-7792, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new NonTerminalStackNode<IConstructor>(-7789, 4, "$Sym", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-7787, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(-7784, 2, "$Sym", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-7782, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-7780, 0, prod__lit___123__char_class___range__123_123_, new int[] {123}, null, null);
      builder.addAlternative(RascalParser.prod__iterStarSep_$Sym__lit___123_$layouts_LAYOUTLIST_symbol_$Sym_$layouts_LAYOUTLIST_sep_$Sym_$layouts_LAYOUTLIST_lit___125_$layouts_LAYOUTLIST_lit___42_, tmp);
	}
    protected static final void _init_prod__labeled_$Sym__symbol_$Sym_$layouts_LAYOUTLIST_label_$NonterminalLabel_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode<IConstructor>(-7812, 2, "$NonterminalLabel", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-7810, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-7807, 0, "$Sym", null, null);
      builder.addAlternative(RascalParser.prod__labeled_$Sym__symbol_$Sym_$layouts_LAYOUTLIST_label_$NonterminalLabel_, tmp);
	}
    protected static final void _init_prod__literal_$Sym__string_$StringConstant_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(-7657, 0, "$StringConstant", null, null);
      builder.addAlternative(RascalParser.prod__literal_$Sym__string_$StringConstant_, tmp);
	}
    protected static final void _init_prod__nonterminal_$Sym__nonterminal_$Nonterminal_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(-7716, 0, "$Nonterminal", null, new ICompletionFilter[] {new StringFollowRestriction(new int[] {91})});
      builder.addAlternative(RascalParser.prod__nonterminal_$Sym__nonterminal_$Nonterminal_, tmp);
	}
    protected static final void _init_prod__notFollow_$Sym__symbol_$Sym_$layouts_LAYOUTLIST_lit___33_62_62_$layouts_LAYOUTLIST_match_$Sym__assoc__left(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(-7897, 4, "$Sym", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-7895, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(-7893, 2, prod__lit___33_62_62__char_class___range__33_33_char_class___range__62_62_char_class___range__62_62_, new int[] {33,62,62}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-7892, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-7889, 0, "$Sym", null, null);
      builder.addAlternative(RascalParser.prod__notFollow_$Sym__symbol_$Sym_$layouts_LAYOUTLIST_lit___33_62_62_$layouts_LAYOUTLIST_match_$Sym__assoc__left, tmp);
	}
    protected static final void _init_prod__notPrecede_$Sym__match_$Sym_$layouts_LAYOUTLIST_lit___33_60_60_$layouts_LAYOUTLIST_symbol_$Sym__assoc__right(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(-7869, 4, "$Sym", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-7867, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(-7865, 2, prod__lit___33_60_60__char_class___range__33_33_char_class___range__60_60_char_class___range__60_60_, new int[] {33,60,60}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-7864, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-7861, 0, "$Sym", null, null);
      builder.addAlternative(RascalParser.prod__notPrecede_$Sym__match_$Sym_$layouts_LAYOUTLIST_lit___33_60_60_$layouts_LAYOUTLIST_symbol_$Sym__assoc__right, tmp);
	}
    protected static final void _init_prod__optional_$Sym__symbol_$Sym_$layouts_LAYOUTLIST_lit___63_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new LiteralStackNode<IConstructor>(-7626, 2, prod__lit___63__char_class___range__63_63_, new int[] {63}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-7625, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-7622, 0, "$Sym", null, null);
      builder.addAlternative(RascalParser.prod__optional_$Sym__symbol_$Sym_$layouts_LAYOUTLIST_lit___63_, tmp);
	}
    protected static final void _init_prod__parameter_$Sym__lit___38_$layouts_LAYOUTLIST_nonterminal_$Nonterminal_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-7589, 0, prod__lit___38__char_class___range__38_38_, new int[] {38}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-7591, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(-7593, 2, "$Nonterminal", null, null);
      builder.addAlternative(RascalParser.prod__parameter_$Sym__lit___38_$layouts_LAYOUTLIST_nonterminal_$Nonterminal_, tmp);
	}
    protected static final void _init_prod__parametrized_$Sym__nonterminal_$Nonterminal_$layouts_LAYOUTLIST_lit___91_$layouts_LAYOUTLIST_parameters_iter_seps__$Sym__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___93_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[7];
      
      tmp[6] = new LiteralStackNode<IConstructor>(-7701, 6, prod__lit___93__char_class___range__93_93_, new int[] {93}, null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(-7700, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new SeparatedListStackNode<IConstructor>(-7697, 4, regular__iter_seps__$Sym__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(-7691, 0, "$Sym", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(-7693, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode<IConstructor>(-7694, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode<IConstructor>(-7696, 3, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-7689, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(-7687, 2, prod__lit___91__char_class___range__91_91_, new int[] {91}, null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-7684, 0, "$Nonterminal", null, new ICompletionFilter[] {new StringFollowRequirement(new int[] {91})});
      tmp[1] = new NonTerminalStackNode<IConstructor>(-7686, 1, "$layouts_LAYOUTLIST", null, null);
      builder.addAlternative(RascalParser.prod__parametrized_$Sym__nonterminal_$Nonterminal_$layouts_LAYOUTLIST_lit___91_$layouts_LAYOUTLIST_parameters_iter_seps__$Sym__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___93_, tmp);
	}
    protected static final void _init_prod__precede_$Sym__match_$Sym_$layouts_LAYOUTLIST_lit___60_60_$layouts_LAYOUTLIST_symbol_$Sym__assoc__right(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(-7855, 4, "$Sym", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-7853, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(-7851, 2, prod__lit___60_60__char_class___range__60_60_char_class___range__60_60_, new int[] {60,60}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-7850, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-7847, 0, "$Sym", null, null);
      builder.addAlternative(RascalParser.prod__precede_$Sym__match_$Sym_$layouts_LAYOUTLIST_lit___60_60_$layouts_LAYOUTLIST_symbol_$Sym__assoc__right, tmp);
	}
    protected static final void _init_prod__sequence_$Sym__lit___40_$layouts_LAYOUTLIST_first_$Sym_$layouts_LAYOUTLIST_sequence_iter_seps__$Sym__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[7];
      
      tmp[6] = new LiteralStackNode<IConstructor>(-7750, 6, prod__lit___41__char_class___range__41_41_, new int[] {41}, null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(-7749, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new SeparatedListStackNode<IConstructor>(-7746, 4, regular__iter_seps__$Sym__$layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(-7743, 0, "$Sym", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(-7745, 1, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-7741, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(-7738, 2, "$Sym", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-7736, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-7734, 0, prod__lit___40__char_class___range__40_40_, new int[] {40}, null, null);
      builder.addAlternative(RascalParser.prod__sequence_$Sym__lit___40_$layouts_LAYOUTLIST_first_$Sym_$layouts_LAYOUTLIST_sequence_iter_seps__$Sym__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_, tmp);
	}
    protected static final void _init_prod__start_$Sym__lit_start_$layouts_LAYOUTLIST_lit___91_$layouts_LAYOUTLIST_nonterminal_$Nonterminal_$layouts_LAYOUTLIST_lit___93_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[7];
      
      tmp[6] = new LiteralStackNode<IConstructor>(-7828, 6, prod__lit___93__char_class___range__93_93_, new int[] {93}, null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(-7827, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new NonTerminalStackNode<IConstructor>(-7824, 4, "$Nonterminal", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-7822, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(-7820, 2, prod__lit___91__char_class___range__91_91_, new int[] {91}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-7819, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-7817, 0, prod__lit_start__char_class___range__115_115_char_class___range__116_116_char_class___range__97_97_char_class___range__114_114_char_class___range__116_116_, new int[] {115,116,97,114,116}, null, null);
      builder.addAlternative(RascalParser.prod__start_$Sym__lit_start_$layouts_LAYOUTLIST_lit___91_$layouts_LAYOUTLIST_nonterminal_$Nonterminal_$layouts_LAYOUTLIST_lit___93_, tmp);
	}
    protected static final void _init_prod__startOfLine_$Sym__lit___94_$layouts_LAYOUTLIST_symbol_$Sym_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode<IConstructor>(-7675, 2, "$Sym", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-7673, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-7671, 0, prod__lit___94__char_class___range__94_94_, new int[] {94}, null, null);
      builder.addAlternative(RascalParser.prod__startOfLine_$Sym__lit___94_$layouts_LAYOUTLIST_symbol_$Sym_, tmp);
	}
    protected static final void _init_prod__unequal_$Sym__symbol_$Sym_$layouts_LAYOUTLIST_lit___92_$layouts_LAYOUTLIST_match_$Sym__assoc__left(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(-7911, 4, "$Sym", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-7909, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(-7907, 2, prod__lit___92__char_class___range__92_92_, new int[] {92}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-7906, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-7903, 0, "$Sym", null, null);
      builder.addAlternative(RascalParser.prod__unequal_$Sym__symbol_$Sym_$layouts_LAYOUTLIST_lit___92_$layouts_LAYOUTLIST_match_$Sym__assoc__left, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__alternative_$Sym__lit___40_$layouts_LAYOUTLIST_first_$Sym_$layouts_LAYOUTLIST_lit___124_$layouts_LAYOUTLIST_alternatives_iter_seps__$Sym__$layouts_LAYOUTLIST_lit___124_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_(builder);
      
        _init_prod__caseInsensitiveLiteral_$Sym__cistring_$CaseInsensitiveStringConstant_(builder);
      
        _init_prod__characterClass_$Sym__charClass_$Class_(builder);
      
        _init_prod__column_$Sym__symbol_$Sym_$layouts_LAYOUTLIST_lit___64_$layouts_LAYOUTLIST_column_$IntegerLiteral_(builder);
      
        _init_prod__empty_$Sym__lit___40_$layouts_LAYOUTLIST_lit___41_(builder);
      
        _init_prod__endOfLine_$Sym__symbol_$Sym_$layouts_LAYOUTLIST_lit___36_(builder);
      
        _init_prod__except_$Sym__symbol_$Sym_$layouts_LAYOUTLIST_lit___33_$layouts_LAYOUTLIST_label_$NonterminalLabel_(builder);
      
        _init_prod__follow_$Sym__symbol_$Sym_$layouts_LAYOUTLIST_lit___62_62_$layouts_LAYOUTLIST_match_$Sym__assoc__left(builder);
      
        _init_prod__iter_$Sym__symbol_$Sym_$layouts_LAYOUTLIST_lit___43_(builder);
      
        _init_prod__iterSep_$Sym__lit___123_$layouts_LAYOUTLIST_symbol_$Sym_$layouts_LAYOUTLIST_sep_$Sym_$layouts_LAYOUTLIST_lit___125_$layouts_LAYOUTLIST_lit___43_(builder);
      
        _init_prod__iterStar_$Sym__symbol_$Sym_$layouts_LAYOUTLIST_lit___42_(builder);
      
        _init_prod__iterStarSep_$Sym__lit___123_$layouts_LAYOUTLIST_symbol_$Sym_$layouts_LAYOUTLIST_sep_$Sym_$layouts_LAYOUTLIST_lit___125_$layouts_LAYOUTLIST_lit___42_(builder);
      
        _init_prod__labeled_$Sym__symbol_$Sym_$layouts_LAYOUTLIST_label_$NonterminalLabel_(builder);
      
        _init_prod__literal_$Sym__string_$StringConstant_(builder);
      
        _init_prod__nonterminal_$Sym__nonterminal_$Nonterminal_(builder);
      
        _init_prod__notFollow_$Sym__symbol_$Sym_$layouts_LAYOUTLIST_lit___33_62_62_$layouts_LAYOUTLIST_match_$Sym__assoc__left(builder);
      
        _init_prod__notPrecede_$Sym__match_$Sym_$layouts_LAYOUTLIST_lit___33_60_60_$layouts_LAYOUTLIST_symbol_$Sym__assoc__right(builder);
      
        _init_prod__optional_$Sym__symbol_$Sym_$layouts_LAYOUTLIST_lit___63_(builder);
      
        _init_prod__parameter_$Sym__lit___38_$layouts_LAYOUTLIST_nonterminal_$Nonterminal_(builder);
      
        _init_prod__parametrized_$Sym__nonterminal_$Nonterminal_$layouts_LAYOUTLIST_lit___91_$layouts_LAYOUTLIST_parameters_iter_seps__$Sym__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___93_(builder);
      
        _init_prod__precede_$Sym__match_$Sym_$layouts_LAYOUTLIST_lit___60_60_$layouts_LAYOUTLIST_symbol_$Sym__assoc__right(builder);
      
        _init_prod__sequence_$Sym__lit___40_$layouts_LAYOUTLIST_first_$Sym_$layouts_LAYOUTLIST_sequence_iter_seps__$Sym__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_(builder);
      
        _init_prod__start_$Sym__lit_start_$layouts_LAYOUTLIST_lit___91_$layouts_LAYOUTLIST_nonterminal_$Nonterminal_$layouts_LAYOUTLIST_lit___93_(builder);
      
        _init_prod__startOfLine_$Sym__lit___94_$layouts_LAYOUTLIST_symbol_$Sym_(builder);
      
        _init_prod__unequal_$Sym__symbol_$Sym_$layouts_LAYOUTLIST_lit___92_$layouts_LAYOUTLIST_match_$Sym__assoc__left(builder);
      
    }
  }
	
  protected static class $Signature {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__noThrows_$Signature__modifiers_$FunctionModifiers_$layouts_LAYOUTLIST_type_$Type_$layouts_LAYOUTLIST_name_$Name_$layouts_LAYOUTLIST_parameters_$Parameters_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[7];
      
      tmp[6] = new NonTerminalStackNode<IConstructor>(-8086, 6, "$Parameters", null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(-8084, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new NonTerminalStackNode<IConstructor>(-8081, 4, "$Name", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-8079, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(-8076, 2, "$Type", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-8074, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-8071, 0, "$FunctionModifiers", null, null);
      builder.addAlternative(RascalParser.prod__noThrows_$Signature__modifiers_$FunctionModifiers_$layouts_LAYOUTLIST_type_$Type_$layouts_LAYOUTLIST_name_$Name_$layouts_LAYOUTLIST_parameters_$Parameters_, tmp);
	}
    protected static final void _init_prod__withThrows_$Signature__modifiers_$FunctionModifiers_$layouts_LAYOUTLIST_type_$Type_$layouts_LAYOUTLIST_name_$Name_$layouts_LAYOUTLIST_parameters_$Parameters_$layouts_LAYOUTLIST_lit_throws_$layouts_LAYOUTLIST_exceptions_iter_seps__$Type__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[11];
      
      tmp[10] = new SeparatedListStackNode<IConstructor>(-8121, 10, regular__iter_seps__$Type__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(-8115, 0, "$Type", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(-8117, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode<IConstructor>(-8118, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode<IConstructor>(-8120, 3, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[9] = new NonTerminalStackNode<IConstructor>(-8113, 9, "$layouts_LAYOUTLIST", null, null);
      tmp[8] = new LiteralStackNode<IConstructor>(-8111, 8, prod__lit_throws__char_class___range__116_116_char_class___range__104_104_char_class___range__114_114_char_class___range__111_111_char_class___range__119_119_char_class___range__115_115_, new int[] {116,104,114,111,119,115}, null, null);
      tmp[7] = new NonTerminalStackNode<IConstructor>(-8110, 7, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new NonTerminalStackNode<IConstructor>(-8107, 6, "$Parameters", null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(-8105, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new NonTerminalStackNode<IConstructor>(-8102, 4, "$Name", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-8100, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(-8097, 2, "$Type", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-8095, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-8092, 0, "$FunctionModifiers", null, null);
      builder.addAlternative(RascalParser.prod__withThrows_$Signature__modifiers_$FunctionModifiers_$layouts_LAYOUTLIST_type_$Type_$layouts_LAYOUTLIST_name_$Name_$layouts_LAYOUTLIST_parameters_$Parameters_$layouts_LAYOUTLIST_lit_throws_$layouts_LAYOUTLIST_exceptions_iter_seps__$Type__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__noThrows_$Signature__modifiers_$FunctionModifiers_$layouts_LAYOUTLIST_type_$Type_$layouts_LAYOUTLIST_name_$Name_$layouts_LAYOUTLIST_parameters_$Parameters_(builder);
      
        _init_prod__withThrows_$Signature__modifiers_$FunctionModifiers_$layouts_LAYOUTLIST_type_$Type_$layouts_LAYOUTLIST_name_$Name_$layouts_LAYOUTLIST_parameters_$Parameters_$layouts_LAYOUTLIST_lit_throws_$layouts_LAYOUTLIST_exceptions_iter_seps__$Type__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_(builder);
      
    }
  }
	
  protected static class $TypeArg {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__default_$TypeArg__type_$Type_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(-8142, 0, "$Type", null, null);
      builder.addAlternative(RascalParser.prod__default_$TypeArg__type_$Type_, tmp);
	}
    protected static final void _init_prod__named_$TypeArg__type_$Type_$layouts_LAYOUTLIST_name_$Name_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode<IConstructor>(-8136, 2, "$Name", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-8134, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-8131, 0, "$Type", null, null);
      builder.addAlternative(RascalParser.prod__named_$TypeArg__type_$Type_$layouts_LAYOUTLIST_name_$Name_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__default_$TypeArg__type_$Type_(builder);
      
        _init_prod__named_$TypeArg__type_$Type_$layouts_LAYOUTLIST_name_$Name_(builder);
      
    }
  }
	
  protected static class $Variable {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__initialized_$Variable__name_$Name_$layouts_LAYOUTLIST_lit___61_$layouts_LAYOUTLIST_initial_$Expression_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(-8160, 4, "$Expression", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-8158, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(-8156, 2, prod__lit___61__char_class___range__61_61_, new int[] {61}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-8155, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-8152, 0, "$Name", null, null);
      builder.addAlternative(RascalParser.prod__initialized_$Variable__name_$Name_$layouts_LAYOUTLIST_lit___61_$layouts_LAYOUTLIST_initial_$Expression_, tmp);
	}
    protected static final void _init_prod__unInitialized_$Variable__name_$Name_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(-8166, 0, "$Name", null, null);
      builder.addAlternative(RascalParser.prod__unInitialized_$Variable__name_$Name_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__initialized_$Variable__name_$Name_$layouts_LAYOUTLIST_lit___61_$layouts_LAYOUTLIST_initial_$Expression_(builder);
      
        _init_prod__unInitialized_$Variable__name_$Name_(builder);
      
    }
  }
	
  protected static class $Literal {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__boolean_$Literal__booleanLiteral_$BooleanLiteral_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(-8270, 0, "$BooleanLiteral", null, null);
      builder.addAlternative(RascalParser.prod__boolean_$Literal__booleanLiteral_$BooleanLiteral_, tmp);
	}
    protected static final void _init_prod__dateTime_$Literal__dateTimeLiteral_$DateTimeLiteral_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(-8276, 0, "$DateTimeLiteral", null, null);
      builder.addAlternative(RascalParser.prod__dateTime_$Literal__dateTimeLiteral_$DateTimeLiteral_, tmp);
	}
    protected static final void _init_prod__integer_$Literal__integerLiteral_$IntegerLiteral_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(-8264, 0, "$IntegerLiteral", null, null);
      builder.addAlternative(RascalParser.prod__integer_$Literal__integerLiteral_$IntegerLiteral_, tmp);
	}
    protected static final void _init_prod__location_$Literal__locationLiteral_$LocationLiteral_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(-8240, 0, "$LocationLiteral", null, null);
      builder.addAlternative(RascalParser.prod__location_$Literal__locationLiteral_$LocationLiteral_, tmp);
	}
    protected static final void _init_prod__rational_$Literal__rationalLiteral_$RationalLiteral_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(-8258, 0, "$RationalLiteral", null, null);
      builder.addAlternative(RascalParser.prod__rational_$Literal__rationalLiteral_$RationalLiteral_, tmp);
	}
    protected static final void _init_prod__real_$Literal__realLiteral_$RealLiteral_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(-8246, 0, "$RealLiteral", null, null);
      builder.addAlternative(RascalParser.prod__real_$Literal__realLiteral_$RealLiteral_, tmp);
	}
    protected static final void _init_prod__regExp_$Literal__regExpLiteral_$RegExpLiteral_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(-8252, 0, "$RegExpLiteral", null, null);
      builder.addAlternative(RascalParser.prod__regExp_$Literal__regExpLiteral_$RegExpLiteral_, tmp);
	}
    protected static final void _init_prod__string_$Literal__stringLiteral_$StringLiteral_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(-8282, 0, "$StringLiteral", null, null);
      builder.addAlternative(RascalParser.prod__string_$Literal__stringLiteral_$StringLiteral_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__boolean_$Literal__booleanLiteral_$BooleanLiteral_(builder);
      
        _init_prod__dateTime_$Literal__dateTimeLiteral_$DateTimeLiteral_(builder);
      
        _init_prod__integer_$Literal__integerLiteral_$IntegerLiteral_(builder);
      
        _init_prod__location_$Literal__locationLiteral_$LocationLiteral_(builder);
      
        _init_prod__rational_$Literal__rationalLiteral_$RationalLiteral_(builder);
      
        _init_prod__real_$Literal__realLiteral_$RealLiteral_(builder);
      
        _init_prod__regExp_$Literal__regExpLiteral_$RegExpLiteral_(builder);
      
        _init_prod__string_$Literal__stringLiteral_$StringLiteral_(builder);
      
    }
  }
	
  protected static class $Module {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__default_$Module__header_$Header_$layouts_LAYOUTLIST_body_$Body_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode<IConstructor>(-8306, 2, "$Body", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-8304, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-8301, 0, "$Header", null, null);
      builder.addAlternative(RascalParser.prod__default_$Module__header_$Header_$layouts_LAYOUTLIST_body_$Body_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__default_$Module__header_$Header_$layouts_LAYOUTLIST_body_$Body_(builder);
      
    }
  }
	
  protected static class $PreProtocolChars {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__$PreProtocolChars__lit___124_$URLChars_lit___60_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new LiteralStackNode<IConstructor>(-8203, 2, prod__lit___60__char_class___range__60_60_, new int[] {60}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-8202, 1, "$URLChars", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-8200, 0, prod__lit___124__char_class___range__124_124_, new int[] {124}, null, null);
      builder.addAlternative(RascalParser.prod__$PreProtocolChars__lit___124_$URLChars_lit___60_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__$PreProtocolChars__lit___124_$URLChars_lit___60_(builder);
      
    }
  }
	
  public RascalParser() {
    super();
  }

  // Parse methods    
  
  public AbstractStackNode<IConstructor>[] $BooleanLiteral() {
    return $BooleanLiteral.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $DateAndTime() {
    return $DateAndTime.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $Pattern() {
    return $Pattern.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $Prod() {
    return $Prod.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $Tag() {
    return $Tag.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $OctalIntegerLiteral() {
    return $OctalIntegerLiteral.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $ProdModifier() {
    return $ProdModifier.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $RationalLiteral() {
    return $RationalLiteral.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $Toplevel() {
    return $Toplevel.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $TypeVar() {
    return $TypeVar.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $UnicodeEscape() {
    return $UnicodeEscape.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $Bound() {
    return $Bound.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $Case() {
    return $Case.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $Class() {
    return $Class.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $Comprehension() {
    return $Comprehension.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $Declaration() {
    return $Declaration.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $FunctionModifiers() {
    return $FunctionModifiers.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $FunctionType() {
    return $FunctionType.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $Type() {
    return $Type.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $Statement() {
    return $Statement.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $Command() {
    return $Command.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $NamedBackslash() {
    return $NamedBackslash.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $PostPathChars() {
    return $PostPathChars.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $PreStringChars() {
    return $PreStringChars.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $ProtocolTail() {
    return $ProtocolTail.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $Renamings() {
    return $Renamings.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $StringLiteral() {
    return $StringLiteral.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $Visibility() {
    return $Visibility.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $Visit() {
    return $Visit.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $JustTime() {
    return $JustTime.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $layouts_LAYOUTLIST() {
    return $layouts_LAYOUTLIST.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $LocationLiteral() {
    return $LocationLiteral.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $QualifiedName() {
    return $QualifiedName.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $Range() {
    return $Range.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $ShellCommand() {
    return $ShellCommand.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $StringCharacter() {
    return $StringCharacter.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $StringMiddle() {
    return $StringMiddle.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $TimeZonePart() {
    return $TimeZonePart.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $URLChars() {
    return $URLChars.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $ConcreteHole() {
    return $ConcreteHole.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $DataTarget() {
    return $DataTarget.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $RealLiteral() {
    return $RealLiteral.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $layouts_$default$() {
    return $layouts_$default$.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $PathPart() {
    return $PathPart.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $RegExp() {
    return $RegExp.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $Replacement() {
    return $Replacement.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $Assoc() {
    return $Assoc.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $MidStringChars() {
    return $MidStringChars.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $RegExpModifier() {
    return $RegExpModifier.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $DecimalIntegerLiteral() {
    return $DecimalIntegerLiteral.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $StringTemplate() {
    return $StringTemplate.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $Body() {
    return $Body.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $FunctionBody() {
    return $FunctionBody.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $Import() {
    return $Import.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $IntegerLiteral() {
    return $IntegerLiteral.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $Kind() {
    return $Kind.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $LAYOUT() {
    return $LAYOUT.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $PostProtocolChars() {
    return $PostProtocolChars.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $UserType() {
    return $UserType.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] start__$Command() {
    return start__$Command.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $Mapping__$Expression() {
    return $Mapping__$Expression.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $SyntaxDefinition() {
    return $SyntaxDefinition.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $Target() {
    return $Target.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $RascalKeywords() {
    return $RascalKeywords.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $Char() {
    return $Char.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $MidPathChars() {
    return $MidPathChars.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $PrePathChars() {
    return $PrePathChars.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $ModuleParameters() {
    return $ModuleParameters.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $RegExpLiteral() {
    return $RegExpLiteral.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $Strategy() {
    return $Strategy.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $NamedRegExp() {
    return $NamedRegExp.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $BasicType() {
    return $BasicType.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $DateTimeLiteral() {
    return $DateTimeLiteral.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $FunctionDeclaration() {
    return $FunctionDeclaration.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $HexIntegerLiteral() {
    return $HexIntegerLiteral.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $ModuleActuals() {
    return $ModuleActuals.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $PathChars() {
    return $PathChars.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $PostStringChars() {
    return $PostStringChars.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] start__$EvalCommand() {
    return start__$EvalCommand.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $Declarator() {
    return $Declarator.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $Name() {
    return $Name.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $Start() {
    return $Start.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $TimePartNoTZ() {
    return $TimePartNoTZ.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $Variant() {
    return $Variant.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $Formals() {
    return $Formals.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $StructuredType() {
    return $StructuredType.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $Backslash() {
    return $Backslash.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $CaseInsensitiveStringConstant() {
    return $CaseInsensitiveStringConstant.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $DataTypeSelector() {
    return $DataTypeSelector.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $JustDate() {
    return $JustDate.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $MidProtocolChars() {
    return $MidProtocolChars.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $PathTail() {
    return $PathTail.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $Tags() {
    return $Tags.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] start__$Commands() {
    return start__$Commands.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $PatternWithAction() {
    return $PatternWithAction.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $StringTail() {
    return $StringTail.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] start__$Module() {
    return start__$Module.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $DatePart() {
    return $DatePart.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $LocalVariableDeclaration() {
    return $LocalVariableDeclaration.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $StringConstant() {
    return $StringConstant.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $Parameters() {
    return $Parameters.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $Assignable() {
    return $Assignable.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $Assignment() {
    return $Assignment.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $Comment() {
    return $Comment.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $ConcretePart() {
    return $ConcretePart.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $EvalCommand() {
    return $EvalCommand.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $Field() {
    return $Field.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $FunctionModifier() {
    return $FunctionModifier.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $ProtocolChars() {
    return $ProtocolChars.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $Label() {
    return $Label.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $ProtocolPart() {
    return $ProtocolPart.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $Expression() {
    return $Expression.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $ImportedModule() {
    return $ImportedModule.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $Mapping__$Pattern() {
    return $Mapping__$Pattern.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $Nonterminal() {
    return $Nonterminal.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $TagString() {
    return $TagString.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $Catch() {
    return $Catch.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $Commands() {
    return $Commands.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $Concrete() {
    return $Concrete.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $Header() {
    return $Header.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $NonterminalLabel() {
    return $NonterminalLabel.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $Renaming() {
    return $Renaming.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $Sym() {
    return $Sym.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $Signature() {
    return $Signature.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $TypeArg() {
    return $TypeArg.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $Variable() {
    return $Variable.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $PreProtocolChars() {
    return $PreProtocolChars.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $Literal() {
    return $Literal.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $Module() {
    return $Module.EXPECTS;
  }
}