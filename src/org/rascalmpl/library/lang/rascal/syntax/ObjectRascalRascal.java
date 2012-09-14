package org.rascalmpl.library.lang.rascal.syntax;

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
public class ObjectRascalRascal extends org.rascalmpl.library.lang.rascal.syntax.RascalRascal {
  protected final static IValueFactory VF = ValueFactoryFactory.getValueFactory();
  
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
    IntegerKeyedHashMap<IntegerList> result = org.rascalmpl.library.lang.rascal.syntax.RascalRascal._initDontNest(); 
    
    
    
    
    _putDontNest(result, 1308, 1338);
    
    _putDontNest(result, 1318, 1328);
    
    _putDontNest(result, 1360, 1358);
    
    _putDontNest(result, 1418, 1428);
    
    _putDontNest(result, 1408, 1438);
    
    _putDontNest(result, 416, 434);
    
    _putDontNest(result, 876, 1278);
    
    _putDontNest(result, 770, 1288);
    
    _putDontNest(result, 896, 1418);
    
    _putDontNest(result, 1194, 1458);
    
    _putDontNest(result, 1196, 1468);
    
    _putDontNest(result, 1458, 1468);
    
    _putDontNest(result, 1378, 1388);
    
    _putDontNest(result, 1380, 1378);
    
    _putDontNest(result, 1298, 1308);
    
    _putDontNest(result, 1320, 1318);
    
    _putDontNest(result, 5926, 5962);
    
    _putDontNest(result, 886, 1268);
    
    _putDontNest(result, 852, 1238);
    
    _putDontNest(result, 906, 1408);
    
    _putDontNest(result, 1230, 1486);
    
    _putDontNest(result, 408, 474);
    
    _putDontNest(result, 1310, 1368);
    
    _putDontNest(result, 1440, 1438);
    
    _putDontNest(result, 1340, 1338);
    
    _putDontNest(result, 5384, 5414);
    
    _putDontNest(result, 5926, 5946);
    
    _putDontNest(result, 886, 1348);
    
    _putDontNest(result, 1188, 1428);
    
    _putDontNest(result, 1278, 1486);
    
    _putDontNest(result, 1428, 1458);
    
    _putDontNest(result, 1438, 1448);
    
    _putDontNest(result, 1368, 1398);
    
    _putDontNest(result, 1348, 1378);
    
    _putDontNest(result, 1288, 1318);
    
    _putDontNest(result, 842, 1248);
    
    _putDontNest(result, 950, 1428);
    
    _putDontNest(result, 770, 1368);
    
    _putDontNest(result, 876, 1358);
    
    _putDontNest(result, 852, 1398);
    
    _putDontNest(result, 1058, 1338);
    
    _putDontNest(result, 1168, 1448);
    
    _putDontNest(result, 1204, 1428);
    
    _putDontNest(result, 1320, 1398);
    
    _putDontNest(result, 1330, 1388);
    
    _putDontNest(result, 1058, 1278);
    
    _putDontNest(result, 886, 1188);
    
    _putDontNest(result, 842, 1168);
    
    _putDontNest(result, 1240, 1408);
    
    _putDontNest(result, 1258, 1458);
    
    _putDontNest(result, 1220, 1428);
    
    _putDontNest(result, 1218, 1418);
    
    _putDontNest(result, 1248, 1448);
    
    _putDontNest(result, 1230, 1438);
    
    _putDontNest(result, 1260, 1468);
    
    _putDontNest(result, 1408, 1486);
    
    _putDontNest(result, 1280, 1358);
    
    _putDontNest(result, 1290, 1348);
    
    _putDontNest(result, 852, 1174);
    
    _putDontNest(result, 886, 1204);
    
    _putDontNest(result, 1338, 1348);
    
    _putDontNest(result, 1300, 1378);
    
    _putDontNest(result, 1328, 1358);
    
    _putDontNest(result, 1288, 1398);
    
    _putDontNest(result, 1298, 1388);
    
    _putDontNest(result, 712, 1468);
    
    _putDontNest(result, 842, 1328);
    
    _putDontNest(result, 852, 1318);
    
    _putDontNest(result, 1250, 1418);
    
    _putDontNest(result, 1228, 1468);
    
    _putDontNest(result, 456, 474);
    
    _putDontNest(result, 1440, 1486);
    
    _putDontNest(result, 1358, 1368);
    
    _putDontNest(result, 464, 434);
    
    _putDontNest(result, 4602, 4998);
    
    _putDontNest(result, 1278, 1438);
    
    _putDontNest(result, 1268, 1428);
    
    _putDontNest(result, 1258, 1268);
    
    _putDontNest(result, 1248, 1278);
    
    _putDontNest(result, 1158, 1168);
    
    _putDontNest(result, 1194, 1204);
    
    _putDontNest(result, 1290, 1428);
    
    _putDontNest(result, 1280, 1438);
    
    _putDontNest(result, 1360, 1486);
    
    _putDontNest(result, 322, 464);
    
    _putDontNest(result, 340, 454);
    
    _putDontNest(result, 5740, 5740);
    
    _putDontNest(result, 690, 1318);
    
    _putDontNest(result, 1206, 1318);
    
    _putDontNest(result, 1218, 1228);
    
    _putDontNest(result, 1308, 1418);
    
    _putDontNest(result, 1330, 1468);
    
    _putDontNest(result, 950, 1194);
    
    _putDontNest(result, 712, 1218);
    
    _putDontNest(result, 896, 1338);
    
    _putDontNest(result, 1220, 1348);
    
    _putDontNest(result, 1168, 1288);
    
    _putDontNest(result, 1258, 1378);
    
    _putDontNest(result, 1230, 1358);
    
    _putDontNest(result, 1260, 1388);
    
    _putDontNest(result, 1270, 1398);
    
    _putDontNest(result, 1238, 1248);
    
    _putDontNest(result, 1228, 1258);
    
    _putDontNest(result, 896, 1204);
    
    _putDontNest(result, 770, 1448);
    
    _putDontNest(result, 906, 1328);
    
    _putDontNest(result, 1278, 1358);
    
    _putDontNest(result, 1268, 1348);
    
    _putDontNest(result, 1058, 1418);
    
    _putDontNest(result, 1168, 1368);
    
    _putDontNest(result, 1196, 1308);
    
    _putDontNest(result, 1174, 1318);
    
    _putDontNest(result, 1194, 1298);
    
    _putDontNest(result, 1318, 1408);
    
    _putDontNest(result, 1338, 1428);
    
    _putDontNest(result, 1298, 1468);
    
    _putDontNest(result, 1328, 1438);
    
    _putDontNest(result, 1310, 1448);
    
    _putDontNest(result, 1340, 1418);
    
    _putDontNest(result, 1300, 1458);
    
    _putDontNest(result, 896, 1188);
    
    _putDontNest(result, 950, 1162);
    
    _putDontNest(result, 712, 1388);
    
    _putDontNest(result, 876, 1486);
    
    _putDontNest(result, 1158, 1318);
    
    _putDontNest(result, 1238, 1398);
    
    _putDontNest(result, 1228, 1388);
    
    _putDontNest(result, 1196, 1258);
    
    _putDontNest(result, 1194, 1268);
    
    _putDontNest(result, 1390, 1448);
    
    _putDontNest(result, 1378, 1468);
    
    _putDontNest(result, 1380, 1458);
    
    _putDontNest(result, 4674, 4992);
    
    _putDontNest(result, 712, 1308);
    
    _putDontNest(result, 1270, 1318);
    
    _putDontNest(result, 1228, 1308);
    
    _putDontNest(result, 1360, 1438);
    
    _putDontNest(result, 1350, 1408);
    
    _putDontNest(result, 260, 454);
    
    _putDontNest(result, 1280, 1486);
    
    _putDontNest(result, 1370, 1428);
    
    _putDontNest(result, 950, 1258);
    
    _putDontNest(result, 690, 1398);
    
    _putDontNest(result, 842, 1408);
    
    _putDontNest(result, 1196, 1388);
    
    _putDontNest(result, 1206, 1398);
    
    _putDontNest(result, 1248, 1368);
    
    _putDontNest(result, 1194, 1378);
    
    _putDontNest(result, 1218, 1338);
    
    _putDontNest(result, 1204, 1218);
    
    _putDontNest(result, 1174, 1248);
    
    _putDontNest(result, 1328, 1486);
    
    _putDontNest(result, 1358, 1448);
    
    _putDontNest(result, 1348, 1458);
    
    _putDontNest(result, 1398, 1408);
    
    _putDontNest(result, 1388, 1418);
    
    _putDontNest(result, 906, 1278);
    
    _putDontNest(result, 896, 1268);
    
    _putDontNest(result, 876, 1438);
    
    _putDontNest(result, 950, 1348);
    
    _putDontNest(result, 1158, 1398);
    
    _putDontNest(result, 1258, 1298);
    
    _putDontNest(result, 1260, 1308);
    
    _putDontNest(result, 1248, 1288);
    
    _putDontNest(result, 1204, 1348);
    
    _putDontNest(result, 1238, 1318);
    
    _putDontNest(result, 1158, 1248);
    
    _putDontNest(result, 1168, 1278);
    
    _putDontNest(result, 1188, 1218);
    
    _putDontNest(result, 328, 474);
    
    _putDontNest(result, 4720, 4998);
    
    _putDontNest(result, 4990, 4998);
    
    _putDontNest(result, 5384, 5404);
    
    _putDontNest(result, 886, 1428);
    
    _putDontNest(result, 1240, 1328);
    
    _putDontNest(result, 1174, 1398);
    
    _putDontNest(result, 1250, 1338);
    
    _putDontNest(result, 1188, 1348);
    
    _putDontNest(result, 770, 1174);
    
    _putDontNest(result, 906, 1438);
    
    _putDontNest(result, 896, 1428);
    
    _putDontNest(result, 1158, 1428);
    
    _putDontNest(result, 1440, 1448);
    
    _putDontNest(result, 1328, 1368);
    
    _putDontNest(result, 5414, 5414);
    
    _putDontNest(result, 1298, 1338);
    
    _putDontNest(result, 1320, 1328);
    
    _putDontNest(result, 5926, 5976);
    
    _putDontNest(result, 712, 1486);
    
    _putDontNest(result, 886, 1258);
    
    _putDontNest(result, 876, 1248);
    
    _putDontNest(result, 842, 1358);
    
    _putDontNest(result, 1174, 1428);
    
    _putDontNest(result, 1168, 1418);
    
    _putDontNest(result, 1058, 1368);
    
    _putDontNest(result, 1228, 1486);
    
    _putDontNest(result, 1428, 1428);
    
    _putDontNest(result, 1318, 1318);
    
    _putDontNest(result, 1398, 1398);
    
    _putDontNest(result, 1438, 1438);
    
    _putDontNest(result, 1358, 1358);
    
    _putDontNest(result, 1388, 1388);
    
    _putDontNest(result, 1468, 1468);
    
    _putDontNest(result, 1348, 1348);
    
    _putDontNest(result, 1308, 1308);
    
    _putDontNest(result, 842, 1278);
    
    _putDontNest(result, 1058, 1288);
    
    _putDontNest(result, 1196, 1438);
    
    _putDontNest(result, 1408, 1448);
    
    _putDontNest(result, 1350, 1398);
    
    _putDontNest(result, 1418, 1458);
    
    _putDontNest(result, 1370, 1378);
    
    _putDontNest(result, 1330, 1338);
    
    _putDontNest(result, 1288, 1328);
    
    _putDontNest(result, 690, 1428);
    
    _putDontNest(result, 770, 1318);
    
    _putDontNest(result, 950, 1418);
    
    _putDontNest(result, 1194, 1408);
    
    _putDontNest(result, 1206, 1428);
    
    _putDontNest(result, 1260, 1486);
    
    _putDontNest(result, 416, 444);
    
    _putDontNest(result, 1280, 1368);
    
    _putDontNest(result, 712, 1438);
    
    _putDontNest(result, 770, 1238);
    
    _putDontNest(result, 876, 1328);
    
    _putDontNest(result, 1250, 1448);
    
    _putDontNest(result, 1228, 1438);
    
    _putDontNest(result, 1338, 1378);
    
    _putDontNest(result, 1300, 1348);
    
    _putDontNest(result, 1340, 1388);
    
    _putDontNest(result, 410, 454);
    
    _putDontNest(result, 464, 444);
    
    _putDontNest(result, 1310, 1358);
    
    _putDontNest(result, 1438, 1486);
    
    _putDontNest(result, 1318, 1398);
    
    _putDontNest(result, 886, 1194);
    
    _putDontNest(result, 852, 1288);
    
    _putDontNest(result, 906, 1486);
    
    _putDontNest(result, 1278, 1468);
    
    _putDontNest(result, 1238, 1428);
    
    _putDontNest(result, 876, 1168);
    
    _putDontNest(result, 852, 1368);
    
    _putDontNest(result, 886, 1338);
    
    _putDontNest(result, 770, 1398);
    
    _putDontNest(result, 1230, 1468);
    
    _putDontNest(result, 1260, 1438);
    
    _putDontNest(result, 1218, 1448);
    
    _putDontNest(result, 1248, 1418);
    
    _putDontNest(result, 1308, 1388);
    
    _putDontNest(result, 1360, 1368);
    
    _putDontNest(result, 1470, 1486);
    
    _putDontNest(result, 1058, 1228);
    
    _putDontNest(result, 4720, 4740);
    
    _putDontNest(result, 886, 1162);
    
    _putDontNest(result, 1196, 1486);
    
    _putDontNest(result, 1258, 1408);
    
    _putDontNest(result, 1270, 1428);
    
    _putDontNest(result, 1240, 1458);
    
    _putDontNest(result, 1290, 1378);
    
    _putDontNest(result, 454, 474);
    
    _putDontNest(result, 896, 1174);
    
    _putDontNest(result, 1194, 1328);
    
    _putDontNest(result, 1238, 1348);
    
    _putDontNest(result, 1278, 1388);
    
    _putDontNest(result, 1058, 1448);
    
    _putDontNest(result, 1168, 1338);
    
    _putDontNest(result, 1204, 1318);
    
    _putDontNest(result, 1188, 1204);
    
    _putDontNest(result, 1158, 1174);
    
    _putDontNest(result, 712, 1358);
    
    _putDontNest(result, 712, 1228);
    
    _putDontNest(result, 842, 1486);
    
    _putDontNest(result, 1228, 1358);
    
    _putDontNest(result, 1268, 1398);
    
    _putDontNest(result, 1188, 1318);
    
    _putDontNest(result, 1228, 1228);
    
    _putDontNest(result, 1204, 1204);
    
    _putDontNest(result, 1238, 1238);
    
    _putDontNest(result, 1278, 1278);
    
    _putDontNest(result, 1268, 1268);
    
    _putDontNest(result, 1328, 1448);
    
    _putDontNest(result, 1340, 1468);
    
    _putDontNest(result, 1310, 1438);
    
    _putDontNest(result, 1358, 1486);
    
    _putDontNest(result, 1288, 1408);
    
    _putDontNest(result, 1338, 1458);
    
    _putDontNest(result, 1300, 1428);
    
    _putDontNest(result, 1298, 1418);
    
    _putDontNest(result, 950, 1338);
    
    _putDontNest(result, 1220, 1398);
    
    _putDontNest(result, 1240, 1378);
    
    _putDontNest(result, 1270, 1348);
    
    _putDontNest(result, 1230, 1278);
    
    _putDontNest(result, 1220, 1268);
    
    _putDontNest(result, 1218, 1258);
    
    _putDontNest(result, 1290, 1458);
    
    _putDontNest(result, 1280, 1448);
    
    _putDontNest(result, 5740, 5750);
    
    _putDontNest(result, 690, 1174);
    
    _putDontNest(result, 1260, 1358);
    
    _putDontNest(result, 1230, 1388);
    
    _putDontNest(result, 1330, 1418);
    
    _putDontNest(result, 1308, 1468);
    
    _putDontNest(result, 1390, 1486);
    
    _putDontNest(result, 1320, 1408);
    
    _putDontNest(result, 4674, 4998);
    
    _putDontNest(result, 896, 1238);
    
    _putDontNest(result, 842, 1438);
    
    _putDontNest(result, 1268, 1318);
    
    _putDontNest(result, 1230, 1308);
    
    _putDontNest(result, 1188, 1398);
    
    _putDontNest(result, 1258, 1328);
    
    _putDontNest(result, 1218, 1288);
    
    _putDontNest(result, 1174, 1348);
    
    _putDontNest(result, 1158, 1238);
    
    _putDontNest(result, 1188, 1268);
    
    _putDontNest(result, 1368, 1408);
    
    _putDontNest(result, 1358, 1438);
    
    _putDontNest(result, 1388, 1468);
    
    _putDontNest(result, 1310, 1486);
    
    _putDontNest(result, 1348, 1428);
    
    _putDontNest(result, 906, 1228);
    
    _putDontNest(result, 896, 1348);
    
    _putDontNest(result, 906, 1358);
    
    _putDontNest(result, 1204, 1398);
    
    _putDontNest(result, 1250, 1368);
    
    _putDontNest(result, 1158, 1348);
    
    _putDontNest(result, 1240, 1298);
    
    _putDontNest(result, 1204, 1268);
    
    _putDontNest(result, 1174, 1238);
    
    _putDontNest(result, 266, 454);
    
    _putDontNest(result, 690, 1348);
    
    _putDontNest(result, 1206, 1348);
    
    _putDontNest(result, 1250, 1288);
    
    _putDontNest(result, 1378, 1418);
    
    _putDontNest(result, 1390, 1438);
    
    _putDontNest(result, 1380, 1428);
    
    _putDontNest(result, 5828, 5976);
    
    _putDontNest(result, 4720, 4992);
    
    _putDontNest(result, 5386, 5404);
    
    _putDontNest(result, 690, 1238);
    
    _putDontNest(result, 852, 1448);
    
    _putDontNest(result, 886, 1418);
    
    _putDontNest(result, 876, 1408);
    
    _putDontNest(result, 1278, 1308);
    
    _putDontNest(result, 1218, 1368);
    
    _putDontNest(result, 1248, 1338);
    
    _putDontNest(result, 1196, 1358);
    
    _putDontNest(result, 1220, 1318);
    
    _putDontNest(result, 1196, 1228);
    
    _putDontNest(result, 1194, 1218);
    
    _putDontNest(result, 1370, 1458);
    
    _putDontNest(result, 1360, 1448);
    
    _putDontNest(result, 4740, 4998);
    
    _putDontNest(result, 1188, 1448);
    
    _putDontNest(result, 1158, 1418);
    
    _putDontNest(result, 436, 474);
    
    _putDontNest(result, 1448, 1458);
    
    _putDontNest(result, 1330, 1368);
    
    _putDontNest(result, 1408, 1418);
    
    _putDontNest(result, 1380, 1398);
    
    _putDontNest(result, 1288, 1298);
    
    _putDontNest(result, 1058, 1194);
    
    _putDontNest(result, 690, 1458);
    
    _putDontNest(result, 842, 1228);
    
    _putDontNest(result, 950, 1448);
    
    _putDontNest(result, 876, 1378);
    
    _putDontNest(result, 1058, 1318);
    
    _putDontNest(result, 1204, 1448);
    
    _putDontNest(result, 1174, 1418);
    
    _putDontNest(result, 1168, 1428);
    
    _putDontNest(result, 444, 434);
    
    _putDontNest(result, 1418, 1408);
    
    _putDontNest(result, 1298, 1288);
    
    _putDontNest(result, 1350, 1348);
    
    _putDontNest(result, 456, 454);
    
    _putDontNest(result, 1280, 1338);
    
    _putDontNest(result, 770, 1204);
    
    _putDontNest(result, 906, 1468);
    
    _putDontNest(result, 1194, 1438);
    
    _putDontNest(result, 1300, 1318);
    
    _putDontNest(result, 1368, 1378);
    
    _putDontNest(result, 434, 444);
    
    _putDontNest(result, 1348, 1398);
    
    _putDontNest(result, 1290, 1328);
    
    _putDontNest(result, 1298, 1368);
    
    _putDontNest(result, 1328, 1338);
    
    _putDontNest(result, 1058, 1162);
    
    _putDontNest(result, 852, 1258);
    
    _putDontNest(result, 876, 1218);
    
    _putDontNest(result, 770, 1188);
    
    _putDontNest(result, 842, 1388);
    
    _putDontNest(result, 1196, 1408);
    
    _putDontNest(result, 1258, 1486);
    
    _putDontNest(result, 1206, 1418);
    
    _putDontNest(result, 1438, 1468);
    
    _putDontNest(result, 1358, 1388);
    
    _putDontNest(result, 842, 1308);
    
    _putDontNest(result, 852, 1338);
    
    _putDontNest(result, 886, 1368);
    
    _putDontNest(result, 1058, 1398);
    
    _putDontNest(result, 1308, 1358);
    
    _putDontNest(result, 1360, 1338);
    
    _putDontNest(result, 408, 454);
    
    _putDontNest(result, 1058, 1258);
    
    _putDontNest(result, 852, 1162);
    
    _putDontNest(result, 770, 1348);
    
    _putDontNest(result, 1228, 1408);
    
    _putDontNest(result, 1278, 1458);
    
    _putDontNest(result, 1238, 1418);
    
    _putDontNest(result, 1268, 1448);
    
    _putDontNest(result, 1320, 1378);
    
    _putDontNest(result, 712, 1448);
    
    _putDontNest(result, 770, 1268);
    
    _putDontNest(result, 876, 1298);
    
    _putDontNest(result, 1258, 1438);
    
    _putDontNest(result, 1248, 1428);
    
    _putDontNest(result, 1220, 1448);
    
    _putDontNest(result, 1230, 1458);
    
    _putDontNest(result, 1340, 1358);
    
    _putDontNest(result, 1468, 1486);
    
    _putDontNest(result, 1310, 1388);
    
    _putDontNest(result, 852, 1194);
    
    _putDontNest(result, 886, 1288);
    
    _putDontNest(result, 1270, 1418);
    
    _putDontNest(result, 1194, 1486);
    
    _putDontNest(result, 1240, 1468);
    
    _putDontNest(result, 1260, 1408);
    
    _putDontNest(result, 1300, 1398);
    
    _putDontNest(result, 1318, 1348);
    
    _putDontNest(result, 446, 464);
    
    _putDontNest(result, 1288, 1378);
    
    _putDontNest(result, 5820, 5962);
    
    _putDontNest(result, 906, 1162);
    
    _putDontNest(result, 896, 1168);
    
    _putDontNest(result, 690, 1188);
    
    _putDontNest(result, 770, 1428);
    
    _putDontNest(result, 906, 1308);
    
    _putDontNest(result, 950, 1368);
    
    _putDontNest(result, 1174, 1338);
    
    _putDontNest(result, 1204, 1368);
    
    _putDontNest(result, 1278, 1378);
    
    _putDontNest(result, 1196, 1328);
    
    _putDontNest(result, 1250, 1398);
    
    _putDontNest(result, 1248, 1258);
    
    _putDontNest(result, 1280, 1418);
    
    _putDontNest(result, 1320, 1458);
    
    _putDontNest(result, 690, 1204);
    
    _putDontNest(result, 1188, 1368);
    
    _putDontNest(result, 1158, 1338);
    
    _putDontNest(result, 1290, 1408);
    
    _putDontNest(result, 1308, 1438);
    
    _putDontNest(result, 1330, 1448);
    
    _putDontNest(result, 328, 454);
    
    _putDontNest(result, 4644, 4998);
    
    _putDontNest(result, 4720, 4720);
    
    _putDontNest(result, 906, 1194);
    
    _putDontNest(result, 712, 1278);
    
    _putDontNest(result, 1218, 1398);
    
    _putDontNest(result, 1240, 1388);
    
    _putDontNest(result, 1206, 1338);
    
    _putDontNest(result, 1188, 1288);
    
    _putDontNest(result, 1228, 1278);
    
    _putDontNest(result, 1158, 1204);
    
    _putDontNest(result, 1288, 1458);
    
    _putDontNest(result, 1338, 1408);
    
    _putDontNest(result, 1318, 1428);
    
    _putDontNest(result, 5820, 5946);
    
    _putDontNest(result, 690, 1298);
    
    _putDontNest(result, 950, 1288);
    
    _putDontNest(result, 896, 1318);
    
    _putDontNest(result, 1230, 1378);
    
    _putDontNest(result, 1204, 1288);
    
    _putDontNest(result, 1258, 1358);
    
    _putDontNest(result, 1248, 1348);
    
    _putDontNest(result, 1174, 1204);
    
    _putDontNest(result, 1158, 1188);
    
    _putDontNest(result, 1238, 1268);
    
    _putDontNest(result, 1298, 1448);
    
    _putDontNest(result, 1310, 1468);
    
    _putDontNest(result, 1340, 1438);
    
    _putDontNest(result, 1328, 1418);
    
    _putDontNest(result, 1388, 1486);
    
    _putDontNest(result, 260, 474);
    
    _putDontNest(result, 690, 1378);
    
    _putDontNest(result, 712, 1288);
    
    _putDontNest(result, 876, 1458);
    
    _putDontNest(result, 1220, 1288);
    
    _putDontNest(result, 1238, 1338);
    
    _putDontNest(result, 1260, 1328);
    
    _putDontNest(result, 1268, 1368);
    
    _putDontNest(result, 1230, 1298);
    
    _putDontNest(result, 1168, 1348);
    
    _putDontNest(result, 1196, 1278);
    
    _putDontNest(result, 1350, 1428);
    
    _putDontNest(result, 1308, 1486);
    
    _putDontNest(result, 1390, 1468);
    
    _putDontNest(result, 1378, 1448);
    
    _putDontNest(result, 1370, 1408);
    
    _putDontNest(result, 950, 1278);
    
    _putDontNest(result, 690, 1268);
    
    _putDontNest(result, 852, 1418);
    
    _putDontNest(result, 886, 1448);
    
    _putDontNest(result, 1240, 1308);
    
    _putDontNest(result, 1250, 1318);
    
    _putDontNest(result, 1206, 1268);
    
    _putDontNest(result, 1194, 1248);
    
    _putDontNest(result, 322, 444);
    
    _putDontNest(result, 1360, 1418);
    
    _putDontNest(result, 906, 1258);
    
    _putDontNest(result, 5394, 5394);
    
    _putDontNest(result, 896, 1398);
    
    _putDontNest(result, 842, 1468);
    
    _putDontNest(result, 1270, 1338);
    
    _putDontNest(result, 1228, 1328);
    
    _putDontNest(result, 1188, 1238);
    
    _putDontNest(result, 1158, 1268);
    
    _putDontNest(result, 340, 474);
    
    _putDontNest(result, 1358, 1468);
    
    _putDontNest(result, 1388, 1438);
    
    _putDontNest(result, 1340, 1486);
    
    _putDontNest(result, 896, 1248);
    
    _putDontNest(result, 712, 1368);
    
    _putDontNest(result, 5384, 5384);
    
    _putDontNest(result, 906, 1388);
    
    _putDontNest(result, 1220, 1368);
    
    _putDontNest(result, 1268, 1288);
    
    _putDontNest(result, 1218, 1318);
    
    _putDontNest(result, 1194, 1358);
    
    _putDontNest(result, 1278, 1298);
    
    _putDontNest(result, 1174, 1268);
    
    _putDontNest(result, 1168, 1258);
    
    _putDontNest(result, 1204, 1238);
    
    _putDontNest(result, 1368, 1458);
    
    _putDontNest(result, 1398, 1428);
    
    _putDontNest(result, 4740, 4992);
    
    _putDontNest(result, 5900, 5946);
    
    _putDontNest(result, 852, 1228);
    
    _putDontNest(result, 876, 1268);
    
    _putDontNest(result, 770, 1298);
    
    _putDontNest(result, 906, 1418);
    
    _putDontNest(result, 1196, 1458);
    
    _putDontNest(result, 1240, 1486);
    
    _putDontNest(result, 1194, 1468);
    
    _putDontNest(result, 434, 474);
    
    _putDontNest(result, 1408, 1428);
    
    _putDontNest(result, 1360, 1348);
    
    _putDontNest(result, 1418, 1438);
    
    _putDontNest(result, 1378, 1398);
    
    _putDontNest(result, 456, 464);
    
    _putDontNest(result, 1448, 1468);
    
    _putDontNest(result, 1288, 1308);
    
    _putDontNest(result, 886, 1278);
    
    _putDontNest(result, 896, 1408);
    
    _putDontNest(result, 1206, 1448);
    
    _putDontNest(result, 690, 1408);
    
    _putDontNest(result, 842, 1258);
    
    _putDontNest(result, 852, 1388);
    
    _putDontNest(result, 886, 1358);
    
    _putDontNest(result, 1158, 1448);
    
    _putDontNest(result, 1188, 1418);
    
    _putDontNest(result, 1300, 1368);
    
    _putDontNest(result, 1368, 1388);
    
    _putDontNest(result, 1298, 1318);
    
    _putDontNest(result, 5900, 5962);
    
    _putDontNest(result, 950, 1438);
    
    _putDontNest(result, 876, 1348);
    
    _putDontNest(result, 1204, 1418);
    
    _putDontNest(result, 1174, 1448);
    
    _putDontNest(result, 1438, 1458);
    
    _putDontNest(result, 1428, 1448);
    
    _putDontNest(result, 1358, 1378);
    
    _putDontNest(result, 410, 434);
    
    _putDontNest(result, 1308, 1328);
    
    _putDontNest(result, 1318, 1338);
    
    _putDontNest(result, 5394, 5414);
    
    _putDontNest(result, 712, 1418);
    
    _putDontNest(result, 876, 1204);
    
    _putDontNest(result, 842, 1162);
    
    _putDontNest(result, 1220, 1418);
    
    _putDontNest(result, 1218, 1428);
    
    _putDontNest(result, 1260, 1458);
    
    _putDontNest(result, 1258, 1468);
    
    _putDontNest(result, 770, 1218);
    
    _putDontNest(result, 876, 1188);
    
    _putDontNest(result, 852, 1308);
    
    _putDontNest(result, 842, 1338);
    
    _putDontNest(result, 1270, 1448);
    
    _putDontNest(result, 1230, 1408);
    
    _putDontNest(result, 1240, 1438);
    
    _putDontNest(result, 408, 464);
    
    _putDontNest(result, 1290, 1358);
    
    _putDontNest(result, 1330, 1398);
    
    _putDontNest(result, 1320, 1388);
    
    _putDontNest(result, 1418, 1486);
    
    _putDontNest(result, 1280, 1348);
    
    _putDontNest(result, 1350, 1338);
    
    _putDontNest(result, 842, 1194);
    
    _putDontNest(result, 950, 1486);
    
    _putDontNest(result, 1228, 1458);
    
    _putDontNest(result, 1250, 1428);
    
    _putDontNest(result, 1278, 1408);
    
    _putDontNest(result, 1328, 1348);
    
    _putDontNest(result, 1338, 1358);
    
    _putDontNest(result, 446, 454);
    
    _putDontNest(result, 1310, 1378);
    
    _putDontNest(result, 770, 1378);
    
    _putDontNest(result, 1238, 1448);
    
    _putDontNest(result, 1268, 1418);
    
    _putDontNest(result, 1058, 1348);
    
    _putDontNest(result, 1348, 1368);
    
    _putDontNest(result, 1450, 1486);
    
    _putDontNest(result, 1298, 1398);
    
    _putDontNest(result, 1288, 1388);
    
    _putDontNest(result, 4746, 4740);
    
    _putDontNest(result, 1248, 1398);
    
    _putDontNest(result, 1158, 1288);
    
    _putDontNest(result, 1240, 1358);
    
    _putDontNest(result, 1206, 1368);
    
    _putDontNest(result, 1258, 1278);
    
    _putDontNest(result, 1218, 1238);
    
    _putDontNest(result, 1248, 1268);
    
    _putDontNest(result, 1308, 1408);
    
    _putDontNest(result, 1280, 1428);
    
    _putDontNest(result, 328, 464);
    
    _putDontNest(result, 1290, 1438);
    
    _putDontNest(result, 1370, 1486);
    
    _putDontNest(result, 1320, 1468);
    
    _putDontNest(result, 690, 1328);
    
    _putDontNest(result, 906, 1338);
    
    _putDontNest(result, 1258, 1388);
    
    _putDontNest(result, 1260, 1378);
    
    _putDontNest(result, 1174, 1288);
    
    _putDontNest(result, 1218, 1348);
    
    _putDontNest(result, 260, 444);
    
    _putDontNest(result, 4644, 4992);
    
    _putDontNest(result, 5820, 5940);
    
    _putDontNest(result, 896, 1328);
    
    _putDontNest(result, 770, 1458);
    
    _putDontNest(result, 886, 1486);
    
    _putDontNest(result, 1168, 1318);
    
    _putDontNest(result, 1196, 1298);
    
    _putDontNest(result, 1204, 1338);
    
    _putDontNest(result, 1174, 1368);
    
    _putDontNest(result, 1058, 1428);
    
    _putDontNest(result, 1194, 1308);
    
    _putDontNest(result, 1158, 1194);
    
    _putDontNest(result, 266, 434);
    
    _putDontNest(result, 1340, 1408);
    
    _putDontNest(result, 1288, 1468);
    
    _putDontNest(result, 1318, 1418);
    
    _putDontNest(result, 1206, 1288);
    
    _putDontNest(result, 1228, 1378);
    
    _putDontNest(result, 1250, 1348);
    
    _putDontNest(result, 1158, 1368);
    
    _putDontNest(result, 1188, 1338);
    
    _putDontNest(result, 1168, 1204);
    
    _putDontNest(result, 1238, 1258);
    
    _putDontNest(result, 1228, 1248);
    
    _putDontNest(result, 1300, 1448);
    
    _putDontNest(result, 1328, 1428);
    
    _putDontNest(result, 1338, 1438);
    
    _putDontNest(result, 1310, 1458);
    
    _putDontNest(result, 842, 1418);
    
    _putDontNest(result, 1270, 1368);
    
    _putDontNest(result, 1228, 1298);
    
    _putDontNest(result, 1194, 1278);
    
    _putDontNest(result, 340, 444);
    
    _putDontNest(result, 1390, 1458);
    
    _putDontNest(result, 1380, 1448);
    
    _putDontNest(result, 1350, 1418);
    
    _putDontNest(result, 896, 1218);
    
    _putDontNest(result, 712, 1338);
    
    _putDontNest(result, 1238, 1288);
    
    _putDontNest(result, 1220, 1338);
    
    _putDontNest(result, 5360, 5414);
    
    _putDontNest(result, 1194, 1388);
    
    _putDontNest(result, 1196, 1378);
    
    _putDontNest(result, 1248, 1318);
    
    _putDontNest(result, 1278, 1328);
    
    _putDontNest(result, 1206, 1258);
    
    _putDontNest(result, 1196, 1248);
    
    _putDontNest(result, 1360, 1428);
    
    _putDontNest(result, 1370, 1438);
    
    _putDontNest(result, 1290, 1486);
    
    _putDontNest(result, 950, 1228);
    
    _putDontNest(result, 690, 1218);
    
    _putDontNest(result, 876, 1428);
    
    _putDontNest(result, 950, 1358);
    
    _putDontNest(result, 1230, 1328);
    
    _putDontNest(result, 1258, 1308);
    
    _putDontNest(result, 1268, 1338);
    
    _putDontNest(result, 1238, 1368);
    
    _putDontNest(result, 1158, 1258);
    
    _putDontNest(result, 1338, 1486);
    
    _putDontNest(result, 1348, 1448);
    
    _putDontNest(result, 1358, 1458);
    
    _putDontNest(result, 5820, 5956);
    
    _putDontNest(result, 5376, 5394);
    
    _putDontNest(result, 852, 1468);
    
    _putDontNest(result, 886, 1438);
    
    _putDontNest(result, 1168, 1398);
    
    _putDontNest(result, 1168, 1268);
    
    _putDontNest(result, 1174, 1258);
    
    _putDontNest(result, 1398, 1418);
    
    _putDontNest(result, 1388, 1408);
    
    _putDontNest(result, 322, 474);
    
    _putDontNest(result, 1368, 1468);
    
    _putDontNest(result, 770, 1168);
    
    _putDontNest(result, 5360, 5394);
    
    _putDontNest(result, 876, 1398);
    
    _putDontNest(result, 852, 1358);
    
    _putDontNest(result, 886, 1388);
    
    _putDontNest(result, 1158, 1438);
    
    _putDontNest(result, 1188, 1468);
    
    _putDontNest(result, 1238, 1486);
    
    _putDontNest(result, 1168, 1408);
    
    _putDontNest(result, 1368, 1358);
    
    _putDontNest(result, 1290, 1308);
    
    _putDontNest(result, 1328, 1318);
    
    _putDontNest(result, 1450, 1468);
    
    _putDontNest(result, 1300, 1338);
    
    _putDontNest(result, 4746, 4992);
    
    _putDontNest(result, 5900, 5940);
    
    _putDontNest(result, 950, 1468);
    
    _putDontNest(result, 1174, 1438);
    
    _putDontNest(result, 1204, 1468);
    
    _putDontNest(result, 1428, 1418);
    
    _putDontNest(result, 416, 474);
    
    _putDontNest(result, 1388, 1378);
    
    _putDontNest(result, 1318, 1368);
    
    _putDontNest(result, 876, 1238);
    
    _putDontNest(result, 886, 1228);
    
    _putDontNest(result, 896, 1458);
    
    _putDontNest(result, 770, 1328);
    
    _putDontNest(result, 906, 1448);
    
    _putDontNest(result, 1270, 1486);
    
    _putDontNest(result, 1194, 1418);
    
    _putDontNest(result, 1058, 1298);
    
    _putDontNest(result, 1196, 1428);
    
    _putDontNest(result, 5376, 5414);
    
    _putDontNest(result, 1448, 1438);
    
    _putDontNest(result, 1370, 1388);
    
    _putDontNest(result, 1418, 1468);
    
    _putDontNest(result, 1058, 1174);
    
    _putDontNest(result, 4776, 4998);
    
    _putDontNest(result, 690, 1438);
    
    _putDontNest(result, 852, 1278);
    
    _putDontNest(result, 1206, 1438);
    
    _putDontNest(result, 1360, 1398);
    
    _putDontNest(result, 408, 434);
    
    _putDontNest(result, 1310, 1328);
    
    _putDontNest(result, 1430, 1448);
    
    _putDontNest(result, 1280, 1318);
    
    _putDontNest(result, 5900, 5956);
    
    _putDontNest(result, 712, 1428);
    
    _putDontNest(result, 842, 1288);
    
    _putDontNest(result, 1174, 1486);
    
    _putDontNest(result, 1250, 1458);
    
    _putDontNest(result, 1228, 1428);
    
    _putDontNest(result, 1340, 1378);
    
    _putDontNest(result, 1298, 1348);
    
    _putDontNest(result, 1338, 1388);
    
    _putDontNest(result, 876, 1318);
    
    _putDontNest(result, 1058, 1378);
    
    _putDontNest(result, 1268, 1468);
    
    _putDontNest(result, 1158, 1486);
    
    _putDontNest(result, 1238, 1438);
    
    _putDontNest(result, 1348, 1338);
    
    _putDontNest(result, 1288, 1358);
    
    _putDontNest(result, 454, 444);
    
    _putDontNest(result, 410, 464);
    
    _putDontNest(result, 1328, 1398);
    
    _putDontNest(result, 4826, 4992);
    
    _putDontNest(result, 690, 1486);
    
    _putDontNest(result, 876, 1174);
    
    _putDontNest(result, 1218, 1458);
    
    _putDontNest(result, 1260, 1428);
    
    _putDontNest(result, 1220, 1468);
    
    _putDontNest(result, 1206, 1486);
    
    _putDontNest(result, 1258, 1418);
    
    _putDontNest(result, 1288, 1278);
    
    _putDontNest(result, 464, 474);
    
    _putDontNest(result, 1308, 1378);
    
    _putDontNest(result, 1280, 1398);
    
    _putDontNest(result, 1330, 1348);
    
    _putDontNest(result, 1058, 1238);
    
    _putDontNest(result, 4730, 4740);
    
    _putDontNest(result, 770, 1248);
    
    _putDontNest(result, 842, 1368);
    
    _putDontNest(result, 886, 1308);
    
    _putDontNest(result, 1248, 1408);
    
    _putDontNest(result, 1270, 1438);
    
    _putDontNest(result, 1240, 1448);
    
    _putDontNest(result, 1298, 1268);
    
    _putDontNest(result, 1320, 1358);
    
    _putDontNest(result, 1290, 1388);
    
    _putDontNest(result, 1448, 1486);
    
    _putDontNest(result, 1350, 1368);
    
    _putDontNest(result, 4730, 4746);
    
    _putDontNest(result, 906, 1174);
    
    _putDontNest(result, 852, 1486);
    
    _putDontNest(result, 896, 1298);
    
    _putDontNest(result, 906, 1288);
    
    _putDontNest(result, 1238, 1358);
    
    _putDontNest(result, 1058, 1458);
    
    _putDontNest(result, 1268, 1388);
    
    _putDontNest(result, 1250, 1268);
    
    _putDontNest(result, 1368, 1486);
    
    _putDontNest(result, 1318, 1448);
    
    _putDontNest(result, 1288, 1438);
    
    _putDontNest(result, 1310, 1408);
    
    _putDontNest(result, 712, 1348);
    
    _putDontNest(result, 770, 1408);
    
    _putDontNest(result, 1228, 1348);
    
    _putDontNest(result, 1250, 1378);
    
    _putDontNest(result, 1278, 1398);
    
    _putDontNest(result, 1228, 1218);
    
    _putDontNest(result, 1338, 1468);
    
    _putDontNest(result, 1340, 1458);
    
    _putDontNest(result, 1298, 1428);
    
    _putDontNest(result, 1300, 1418);
    
    _putDontNest(result, 712, 1258);
    
    _putDontNest(result, 1230, 1398);
    
    _putDontNest(result, 1270, 1358);
    
    _putDontNest(result, 1188, 1308);
    
    _putDontNest(result, 1248, 1238);
    
    _putDontNest(result, 1220, 1258);
    
    _putDontNest(result, 1218, 1268);
    
    _putDontNest(result, 1320, 1438);
    
    _putDontNest(result, 1290, 1468);
    
    _putDontNest(result, 4730, 4730);
    
    _putDontNest(result, 690, 1168);
    
    _putDontNest(result, 950, 1308);
    
    _putDontNest(result, 906, 1368);
    
    _putDontNest(result, 1194, 1338);
    
    _putDontNest(result, 1218, 1378);
    
    _putDontNest(result, 1204, 1308);
    
    _putDontNest(result, 1168, 1328);
    
    _putDontNest(result, 1260, 1348);
    
    _putDontNest(result, 1220, 1388);
    
    _putDontNest(result, 1240, 1278);
    
    _putDontNest(result, 1330, 1428);
    
    _putDontNest(result, 1308, 1458);
    
    _putDontNest(result, 906, 1238);
    
    _putDontNest(result, 712, 1162);
    
    _putDontNest(result, 690, 1248);
    
    _putDontNest(result, 950, 1388);
    
    _putDontNest(result, 1218, 1298);
    
    _putDontNest(result, 1174, 1358);
    
    _putDontNest(result, 1278, 1318);
    
    _putDontNest(result, 1248, 1328);
    
    _putDontNest(result, 1204, 1388);
    
    _putDontNest(result, 1220, 1308);
    
    _putDontNest(result, 1188, 1258);
    
    _putDontNest(result, 266, 464);
    
    _putDontNest(result, 1348, 1418);
    
    _putDontNest(result, 1388, 1458);
    
    _putDontNest(result, 896, 1228);
    
    _putDontNest(result, 886, 1468);
    
    _putDontNest(result, 852, 1438);
    
    _putDontNest(result, 1240, 1288);
    
    _putDontNest(result, 1158, 1358);
    
    _putDontNest(result, 1188, 1388);
    
    _putDontNest(result, 1168, 1238);
    
    _putDontNest(result, 1204, 1258);
    
    _putDontNest(result, 1398, 1448);
    
    _putDontNest(result, 1288, 1486);
    
    _putDontNest(result, 1358, 1408);
    
    _putDontNest(result, 1368, 1438);
    
    _putDontNest(result, 950, 1218);
    
    _putDontNest(result, 690, 1358);
    
    _putDontNest(result, 712, 1194);
    
    _putDontNest(result, 842, 1448);
    
    _putDontNest(result, 1250, 1298);
    
    _putDontNest(result, 1206, 1358);
    
    _putDontNest(result, 1240, 1368);
    
    _putDontNest(result, 1380, 1418);
    
    _putDontNest(result, 328, 434);
    
    _putDontNest(result, 1350, 1448);
    
    _putDontNest(result, 1378, 1428);
    
    _putDontNest(result, 5376, 5404);
    
    _putDontNest(result, 896, 1378);
    
    _putDontNest(result, 1230, 1318);
    
    _putDontNest(result, 1196, 1348);
    
    _putDontNest(result, 1258, 1338);
    
    _putDontNest(result, 1268, 1308);
    
    _putDontNest(result, 1194, 1228);
    
    _putDontNest(result, 1196, 1218);
    
    _putDontNest(result, 1370, 1468);
    
    _putDontNest(result, 1390, 1408);
    
    _putDontNest(result, 1320, 1486);
    
    _putDontNest(result, 4730, 4992);
    
    _putDontNest(result, 842, 1238);
    
    _putDontNest(result, 886, 1378);
    
    _putDontNest(result, 5360, 5404);
    
    _putDontNest(result, 1194, 1448);
    
    _putDontNest(result, 1058, 1328);
    
    _putDontNest(result, 408, 444);
    
    _putDontNest(result, 1288, 1288);
    
    _putDontNest(result, 446, 474);
    
    _putDontNest(result, 1390, 1398);
    
    _putDontNest(result, 1418, 1418);
    
    _putDontNest(result, 1448, 1448);
    
    _putDontNest(result, 1058, 1204);
    
    _putDontNest(result, 4746, 4998);
    
    _putDontNest(result, 690, 1468);
    
    _putDontNest(result, 950, 1458);
    
    _putDontNest(result, 1206, 1468);
    
    _putDontNest(result, 1220, 1486);
    
    _putDontNest(result, 1408, 1408);
    
    _putDontNest(result, 1430, 1438);
    
    _putDontNest(result, 1380, 1388);
    
    _putDontNest(result, 1298, 1298);
    
    _putDontNest(result, 1328, 1328);
    
    _putDontNest(result, 1350, 1358);
    
    _putDontNest(result, 1378, 1378);
    
    _putDontNest(result, 1458, 1458);
    
    _putDontNest(result, 1320, 1368);
    
    _putDontNest(result, 1290, 1338);
    
    _putDontNest(result, 1058, 1188);
    
    _putDontNest(result, 886, 1218);
    
    _putDontNest(result, 852, 1248);
    
    _putDontNest(result, 842, 1398);
    
    _putDontNest(result, 896, 1468);
    
    _putDontNest(result, 1158, 1468);
    
    _putDontNest(result, 1268, 1486);
    
    _putDontNest(result, 1188, 1438);
    
    _putDontNest(result, 1358, 1398);
    
    _putDontNest(result, 1280, 1328);
    
    _putDontNest(result, 1338, 1338);
    
    _putDontNest(result, 1310, 1318);
    
    _putDontNest(result, 4776, 4992);
    
    _putDontNest(result, 1204, 1438);
    
    _putDontNest(result, 1174, 1468);
    
    _putDontNest(result, 1168, 1458);
    
    _putDontNest(result, 1348, 1388);
    
    _putDontNest(result, 1288, 1368);
    
    _putDontNest(result, 1428, 1468);
    
    _putDontNest(result, 4746, 4730);
    
    _putDontNest(result, 842, 1174);
    
    _putDontNest(result, 1220, 1438);
    
    _putDontNest(result, 1230, 1428);
    
    _putDontNest(result, 1248, 1458);
    
    _putDontNest(result, 1258, 1448);
    
    _putDontNest(result, 1308, 1348);
    
    _putDontNest(result, 1430, 1486);
    
    _putDontNest(result, 1330, 1378);
    
    _putDontNest(result, 1058, 1268);
    
    _putDontNest(result, 4740, 4720);
    
    _putDontNest(result, 852, 1168);
    
    _putDontNest(result, 876, 1368);
    
    _putDontNest(result, 770, 1358);
    
    _putDontNest(result, 1218, 1408);
    
    _putDontNest(result, 1240, 1418);
    
    _putDontNest(result, 1270, 1468);
    
    _putDontNest(result, 4826, 4998);
    
    _putDontNest(result, 770, 1278);
    
    _putDontNest(result, 876, 1288);
    
    _putDontNest(result, 1204, 1486);
    
    _putDontNest(result, 1340, 1348);
    
    _putDontNest(result, 1298, 1378);
    
    _putDontNest(result, 1368, 1368);
    
    _putDontNest(result, 454, 434);
    
    _putDontNest(result, 1300, 1388);
    
    _putDontNest(result, 4746, 4746);
    
    _putDontNest(result, 842, 1318);
    
    _putDontNest(result, 852, 1328);
    
    _putDontNest(result, 886, 1298);
    
    _putDontNest(result, 1238, 1468);
    
    _putDontNest(result, 1250, 1408);
    
    _putDontNest(result, 1278, 1428);
    
    _putDontNest(result, 1188, 1486);
    
    _putDontNest(result, 1268, 1438);
    
    _putDontNest(result, 1310, 1398);
    
    _putDontNest(result, 1318, 1358);
    
    _putDontNest(result, 896, 1308);
    
    _putDontNest(result, 770, 1438);
    
    _putDontNest(result, 1260, 1398);
    
    _putDontNest(result, 1270, 1388);
    
    _putDontNest(result, 1158, 1308);
    
    _putDontNest(result, 1258, 1258);
    
    _putDontNest(result, 1290, 1418);
    
    _putDontNest(result, 1320, 1448);
    
    _putDontNest(result, 5820, 5976);
    
    _putDontNest(result, 1230, 1348);
    
    _putDontNest(result, 1196, 1318);
    
    _putDontNest(result, 1168, 1298);
    
    _putDontNest(result, 1248, 1378);
    
    _putDontNest(result, 1174, 1308);
    
    _putDontNest(result, 1220, 1358);
    
    _putDontNest(result, 1194, 1368);
    
    _putDontNest(result, 1248, 1248);
    
    _putDontNest(result, 1218, 1218);
    
    _putDontNest(result, 1280, 1408);
    
    _putDontNest(result, 340, 464);
    
    _putDontNest(result, 1308, 1428);
    
    _putDontNest(result, 1350, 1486);
    
    _putDontNest(result, 322, 454);
    
    _putDontNest(result, 1330, 1458);
    
    _putDontNest(result, 906, 1204);
    
    _putDontNest(result, 712, 1398);
    
    _putDontNest(result, 712, 1268);
    
    _putDontNest(result, 1194, 1288);
    
    _putDontNest(result, 1268, 1358);
    
    _putDontNest(result, 1278, 1348);
    
    _putDontNest(result, 1228, 1398);
    
    _putDontNest(result, 1238, 1388);
    
    _putDontNest(result, 1228, 1268);
    
    _putDontNest(result, 1398, 1486);
    
    _putDontNest(result, 1328, 1408);
    
    _putDontNest(result, 1288, 1448);
    
    _putDontNest(result, 1318, 1438);
    
    _putDontNest(result, 4730, 4720);
    
    _putDontNest(result, 906, 1188);
    
    _putDontNest(result, 950, 1168);
    
    _putDontNest(result, 690, 1308);
    
    _putDontNest(result, 906, 1318);
    
    _putDontNest(result, 950, 1298);
    
    _putDontNest(result, 1206, 1308);
    
    _putDontNest(result, 1058, 1408);
    
    _putDontNest(result, 1238, 1278);
    
    _putDontNest(result, 1300, 1468);
    
    _putDontNest(result, 1298, 1458);
    
    _putDontNest(result, 1340, 1428);
    
    _putDontNest(result, 1338, 1418);
    
    _putDontNest(result, 5732, 5750);
    
    _putDontNest(result, 950, 1248);
    
    _putDontNest(result, 690, 1388);
    
    _putDontNest(result, 852, 1408);
    
    _putDontNest(result, 950, 1378);
    
    _putDontNest(result, 876, 1448);
    
    _putDontNest(result, 1250, 1328);
    
    _putDontNest(result, 1240, 1338);
    
    _putDontNest(result, 1196, 1398);
    
    _putDontNest(result, 1206, 1388);
    
    _putDontNest(result, 1194, 1258);
    
    _putDontNest(result, 1196, 1268);
    
    _putDontNest(result, 1378, 1458);
    
    _putDontNest(result, 1350, 1438);
    
    _putDontNest(result, 1360, 1408);
    
    _putDontNest(result, 260, 464);
    
    _putDontNest(result, 1380, 1468);
    
    _putDontNest(result, 690, 1278);
    
    _putDontNest(result, 886, 1458);
    
    _putDontNest(result, 770, 1486);
    
    _putDontNest(result, 1238, 1308);
    
    _putDontNest(result, 1258, 1368);
    
    _putDontNest(result, 1260, 1318);
    
    _putDontNest(result, 1206, 1278);
    
    _putDontNest(result, 328, 444);
    
    _putDontNest(result, 1370, 1418);
    
    _putDontNest(result, 896, 1278);
    
    _putDontNest(result, 906, 1268);
    
    _putDontNest(result, 5394, 5404);
    
    _putDontNest(result, 712, 1204);
    
    _putDontNest(result, 906, 1398);
    
    _putDontNest(result, 1174, 1388);
    
    _putDontNest(result, 1168, 1378);
    
    _putDontNest(result, 1248, 1298);
    
    _putDontNest(result, 1204, 1358);
    
    _putDontNest(result, 1258, 1288);
    
    _putDontNest(result, 1218, 1328);
    
    _putDontNest(result, 1158, 1278);
    
    _putDontNest(result, 1168, 1248);
    
    _putDontNest(result, 1204, 1228);
    
    _putDontNest(result, 1388, 1428);
    
    _putDontNest(result, 1348, 1468);
    
    _putDontNest(result, 712, 1318);
    
    _putDontNest(result, 712, 1188);
    
    _putDontNest(result, 896, 1388);
    
    _putDontNest(result, 1228, 1318);
    
    _putDontNest(result, 1158, 1388);
    
    _putDontNest(result, 1270, 1308);
    
    _putDontNest(result, 1188, 1358);
    
    _putDontNest(result, 1174, 1278);
    
    _putDontNest(result, 1188, 1228);
    
    _putDontNest(result, 1368, 1448);
    
    _putDontNest(result, 1398, 1438);
    
    _putDontNest(result, 1318, 1486);
    
    _putDontNest(result, 4730, 4998);
    
    _putDontNest(result, 886, 1248);
    
    _putDontNest(result, 852, 1218);
    
    _putDontNest(result, 876, 1258);
    
    _putDontNest(result, 896, 1438);
    
    _putDontNest(result, 906, 1428);
    
    _putDontNest(result, 770, 1308);
    
    _putDontNest(result, 1196, 1448);
    
    _putDontNest(result, 1388, 1398);
    
    _putDontNest(result, 444, 474);
    
    _putDontNest(result, 1440, 1458);
    
    _putDontNest(result, 410, 444);
    
    _putDontNest(result, 1338, 1368);
    
    _putDontNest(result, 842, 1348);
    
    _putDontNest(result, 1206, 1458);
    
    _putDontNest(result, 1218, 1486);
    
    _putDontNest(result, 1428, 1438);
    
    _putDontNest(result, 1288, 1338);
    
    _putDontNest(result, 1348, 1358);
    
    _putDontNest(result, 1358, 1348);
    
    _putDontNest(result, 842, 1268);
    
    _putDontNest(result, 950, 1408);
    
    _putDontNest(result, 852, 1378);
    
    _putDontNest(result, 1158, 1458);
    
    _putDontNest(result, 1204, 1408);
    
    _putDontNest(result, 1360, 1378);
    
    _putDontNest(result, 1418, 1448);
    
    _putDontNest(result, 1408, 1458);
    
    _putDontNest(result, 1308, 1318);
    
    _putDontNest(result, 1174, 1458);
    
    _putDontNest(result, 1168, 1468);
    
    _putDontNest(result, 1188, 1408);
    
    _putDontNest(result, 1250, 1486);
    
    _putDontNest(result, 1290, 1368);
    
    _putDontNest(result, 1430, 1468);
    
    _putDontNest(result, 1320, 1338);
    
    _putDontNest(result, 1350, 1388);
    
    _putDontNest(result, 1298, 1328);
    
    _putDontNest(result, 5900, 5976);
    
    _putDontNest(result, 690, 1418);
    
    _putDontNest(result, 876, 1194);
    
    _putDontNest(result, 1248, 1468);
    
    _putDontNest(result, 1218, 1438);
    
    _putDontNest(result, 1230, 1418);
    
    _putDontNest(result, 1260, 1448);
    
    _putDontNest(result, 1328, 1378);
    
    _putDontNest(result, 1428, 1486);
    
    _putDontNest(result, 1368, 1338);
    
    _putDontNest(result, 1310, 1348);
    
    _putDontNest(result, 1300, 1358);
    
    _putDontNest(result, 4740, 4730);
    
    _putDontNest(result, 770, 1228);
    
    _putDontNest(result, 896, 1486);
    
    _putDontNest(result, 886, 1328);
    
    _putDontNest(result, 852, 1298);
    
    _putDontNest(result, 1270, 1458);
    
    _putDontNest(result, 1240, 1428);
    
    _putDontNest(result, 1220, 1408);
    
    _putDontNest(result, 1340, 1398);
    
    _putDontNest(result, 1318, 1388);
    
    _putDontNest(result, 4746, 4720);
    
    _putDontNest(result, 712, 1408);
    
    _putDontNest(result, 842, 1204);
    
    _putDontNest(result, 876, 1162);
    
    _putDontNest(result, 1268, 1408);
    
    _putDontNest(result, 1228, 1448);
    
    _putDontNest(result, 1250, 1438);
    
    _putDontNest(result, 1460, 1486);
    
    _putDontNest(result, 886, 1168);
    
    _putDontNest(result, 842, 1188);
    
    _putDontNest(result, 770, 1388);
    
    _putDontNest(result, 876, 1338);
    
    _putDontNest(result, 1058, 1358);
    
    _putDontNest(result, 1278, 1418);
    
    _putDontNest(result, 1238, 1458);
    
    _putDontNest(result, 416, 454);
    
    _putDontNest(result, 1308, 1398);
    
    _putDontNest(result, 1280, 1378);
    
    _putDontNest(result, 1058, 1218);
    
    _putDontNest(result, 4740, 4746);
    
    _putDontNest(result, 712, 1238);
    
    _putDontNest(result, 1240, 1348);
    
    _putDontNest(result, 1258, 1398);
    
    _putDontNest(result, 1188, 1328);
    
    _putDontNest(result, 1158, 1298);
    
    _putDontNest(result, 1270, 1378);
    
    _putDontNest(result, 1228, 1238);
    
    _putDontNest(result, 1238, 1228);
    
    _putDontNest(result, 1298, 1408);
    
    _putDontNest(result, 1318, 1468);
    
    _putDontNest(result, 1288, 1418);
    
    _putDontNest(result, 4740, 4740);
    
    _putDontNest(result, 950, 1328);
    
    _putDontNest(result, 1168, 1308);
    
    _putDontNest(result, 1196, 1368);
    
    _putDontNest(result, 1248, 1388);
    
    _putDontNest(result, 1174, 1298);
    
    _putDontNest(result, 1204, 1328);
    
    _putDontNest(result, 1194, 1318);
    
    _putDontNest(result, 1218, 1358);
    
    _putDontNest(result, 1278, 1268);
    
    _putDontNest(result, 1268, 1278);
    
    _putDontNest(result, 1338, 1448);
    
    _putDontNest(result, 1348, 1486);
    
    _putDontNest(result, 266, 444);
    
    _putDontNest(result, 1310, 1428);
    
    _putDontNest(result, 1328, 1458);
    
    _putDontNest(result, 1300, 1438);
    
    _putDontNest(result, 770, 1468);
    
    _putDontNest(result, 1058, 1438);
    
    _putDontNest(result, 1238, 1378);
    
    _putDontNest(result, 1196, 1288);
    
    _putDontNest(result, 1220, 1278);
    
    _putDontNest(result, 1230, 1268);
    
    _putDontNest(result, 1320, 1418);
    
    _putDontNest(result, 1330, 1408);
    
    _putDontNest(result, 260, 434);
    
    _putDontNest(result, 1290, 1448);
    
    _putDontNest(result, 1280, 1458);
    
    _putDontNest(result, 5750, 5750);
    
    _putDontNest(result, 690, 1338);
    
    _putDontNest(result, 1250, 1358);
    
    _putDontNest(result, 1206, 1298);
    
    _putDontNest(result, 1218, 1248);
    
    _putDontNest(result, 1248, 1218);
    
    _putDontNest(result, 1240, 1258);
    
    _putDontNest(result, 1380, 1486);
    
    _putDontNest(result, 950, 1174);
    
    _putDontNest(result, 712, 1174);
    
    _putDontNest(result, 842, 1428);
    
    _putDontNest(result, 1194, 1398);
    
    _putDontNest(result, 1228, 1288);
    
    _putDontNest(result, 1206, 1378);
    
    _putDontNest(result, 1168, 1218);
    
    _putDontNest(result, 1188, 1278);
    
    _putDontNest(result, 1174, 1228);
    
    _putDontNest(result, 1300, 1486);
    
    _putDontNest(result, 1358, 1428);
    
    _putDontNest(result, 1348, 1438);
    
    _putDontNest(result, 5828, 5946);
    
    _putDontNest(result, 906, 1348);
    
    _putDontNest(result, 896, 1358);
    
    _putDontNest(result, 1260, 1368);
    
    _putDontNest(result, 1238, 1298);
    
    _putDontNest(result, 1258, 1318);
    
    _putDontNest(result, 1268, 1328);
    
    _putDontNest(result, 1230, 1338);
    
    _putDontNest(result, 1158, 1228);
    
    _putDontNest(result, 1204, 1278);
    
    _putDontNest(result, 1398, 1468);
    
    _putDontNest(result, 1368, 1418);
    
    _putDontNest(result, 906, 1218);
    
    _putDontNest(result, 690, 1228);
    
    _putDontNest(result, 886, 1408);
    
    _putDontNest(result, 876, 1418);
    
    _putDontNest(result, 1278, 1338);
    
    _putDontNest(result, 1168, 1388);
    
    _putDontNest(result, 1220, 1328);
    
    _putDontNest(result, 1248, 1308);
    
    _putDontNest(result, 1174, 1378);
    
    _putDontNest(result, 1196, 1238);
    
    _putDontNest(result, 1380, 1438);
    
    _putDontNest(result, 1350, 1468);
    
    _putDontNest(result, 1390, 1428);
    
    _putDontNest(result, 712, 1328);
    
    _putDontNest(result, 852, 1458);
    
    _putDontNest(result, 1158, 1378);
    
    _putDontNest(result, 1058, 1486);
    
    _putDontNest(result, 1228, 1368);
    
    _putDontNest(result, 1370, 1448);
    
    _putDontNest(result, 340, 434);
    
    _putDontNest(result, 1360, 1458);
    
    _putDontNest(result, 1378, 1408);
    
    _putDontNest(result, 5828, 5962);
    
    _putDontNest(result, 950, 1238);
    
    _putDontNest(result, 1188, 1458);
    
    _putDontNest(result, 1174, 1408);
    
    _putDontNest(result, 1368, 1348);
    
    _putDontNest(result, 1280, 1308);
    
    _putDontNest(result, 1340, 1368);
    
    _putDontNest(result, 1428, 1408);
    
    _putDontNest(result, 1440, 1468);
    
    _putDontNest(result, 1310, 1338);
    
    _putDontNest(result, 770, 1162);
    
    _putDontNest(result, 852, 1348);
    
    _putDontNest(result, 5360, 5384);
    
    _putDontNest(result, 1168, 1438);
    
    _putDontNest(result, 1158, 1408);
    
    _putDontNest(result, 1204, 1458);
    
    _putDontNest(result, 434, 434);
    
    _putDontNest(result, 426, 474);
    
    _putDontNest(result, 690, 1448);
    
    _putDontNest(result, 842, 1218);
    
    _putDontNest(result, 886, 1398);
    
    _putDontNest(result, 876, 1388);
    
    _putDontNest(result, 770, 1338);
    
    _putDontNest(result, 1194, 1428);
    
    _putDontNest(result, 1206, 1408);
    
    _putDontNest(result, 1058, 1308);
    
    _putDontNest(result, 1196, 1418);
    
    _putDontNest(result, 444, 444);
    
    _putDontNest(result, 1408, 1468);
    
    _putDontNest(result, 410, 474);
    
    _putDontNest(result, 1308, 1368);
    
    _putDontNest(result, 5386, 5414);
    
    _putDontNest(result, 1360, 1388);
    
    _putDontNest(result, 1058, 1168);
    
    _putDontNest(result, 770, 1194);
    
    _putDontNest(result, 896, 1448);
    
    _putDontNest(result, 906, 1458);
    
    _putDontNest(result, 1248, 1486);
    
    _putDontNest(result, 1370, 1398);
    
    _putDontNest(result, 1300, 1328);
    
    _putDontNest(result, 1430, 1458);
    
    _putDontNest(result, 1350, 1378);
    
    _putDontNest(result, 1290, 1318);
    
    _putDontNest(result, 5926, 5940);
    
    _putDontNest(result, 852, 1268);
    
    _putDontNest(result, 886, 1238);
    
    _putDontNest(result, 876, 1228);
    
    _putDontNest(result, 842, 1378);
    
    _putDontNest(result, 1238, 1408);
    
    _putDontNest(result, 1250, 1468);
    
    _putDontNest(result, 1228, 1418);
    
    _putDontNest(result, 1168, 1486);
    
    _putDontNest(result, 1298, 1358);
    
    _putDontNest(result, 1328, 1388);
    
    _putDontNest(result, 886, 1318);
    
    _putDontNest(result, 842, 1298);
    
    _putDontNest(result, 1268, 1458);
    
    _putDontNest(result, 1058, 1388);
    
    _putDontNest(result, 1278, 1448);
    
    _putDontNest(result, 1288, 1348);
    
    _putDontNest(result, 1318, 1378);
    
    _putDontNest(result, 1358, 1338);
    
    _putDontNest(result, 1338, 1398);
    
    _putDontNest(result, 1058, 1248);
    
    _putDontNest(result, 1260, 1418);
    
    _putDontNest(result, 1248, 1438);
    
    _putDontNest(result, 1270, 1408);
    
    _putDontNest(result, 1220, 1458);
    
    _putDontNest(result, 1258, 1428);
    
    _putDontNest(result, 1230, 1448);
    
    _putDontNest(result, 1218, 1468);
    
    _putDontNest(result, 1288, 1268);
    
    _putDontNest(result, 416, 464);
    
    _putDontNest(result, 1330, 1358);
    
    _putDontNest(result, 474, 474);
    
    _putDontNest(result, 1290, 1398);
    
    _putDontNest(result, 1458, 1486);
    
    _putDontNest(result, 5926, 5956);
    
    _putDontNest(result, 712, 1458);
    
    _putDontNest(result, 770, 1258);
    
    _putDontNest(result, 852, 1188);
    
    _putDontNest(result, 876, 1308);
    
    _putDontNest(result, 1298, 1278);
    
    _putDontNest(result, 1280, 1388);
    
    _putDontNest(result, 1320, 1348);
    
    _putDontNest(result, 886, 1174);
    
    _putDontNest(result, 852, 1204);
    
    _putDontNest(result, 4602, 4992);
    
    _putDontNest(result, 1058, 1468);
    
    _putDontNest(result, 1268, 1378);
    
    _putDontNest(result, 1238, 1218);
    
    _putDontNest(result, 1250, 1278);
    
    _putDontNest(result, 1318, 1458);
    
    _putDontNest(result, 1300, 1408);
    
    _putDontNest(result, 1288, 1428);
    
    _putDontNest(result, 5828, 5956);
    
    _putDontNest(result, 4720, 4746);
    
    _putDontNest(result, 906, 1168);
    
    _putDontNest(result, 896, 1162);
    
    _putDontNest(result, 950, 1188);
    
    _putDontNest(result, 690, 1368);
    
    _putDontNest(result, 896, 1288);
    
    _putDontNest(result, 906, 1298);
    
    _putDontNest(result, 770, 1418);
    
    _putDontNest(result, 950, 1318);
    
    _putDontNest(result, 1250, 1388);
    
    _putDontNest(result, 1206, 1328);
    
    _putDontNest(result, 1328, 1468);
    
    _putDontNest(result, 1298, 1438);
    
    _putDontNest(result, 1310, 1418);
    
    _putDontNest(result, 1340, 1448);
    
    _putDontNest(result, 950, 1204);
    
    _putDontNest(result, 690, 1194);
    
    _putDontNest(result, 1188, 1298);
    
    _putDontNest(result, 1158, 1328);
    
    _putDontNest(result, 1218, 1278);
    
    _putDontNest(result, 1230, 1258);
    
    _putDontNest(result, 1320, 1428);
    
    _putDontNest(result, 1280, 1468);
    
    _putDontNest(result, 896, 1194);
    
    _putDontNest(result, 1258, 1348);
    
    _putDontNest(result, 1196, 1338);
    
    _putDontNest(result, 1240, 1398);
    
    _putDontNest(result, 1204, 1298);
    
    _putDontNest(result, 1174, 1328);
    
    _putDontNest(result, 1220, 1378);
    
    _putDontNest(result, 1248, 1358);
    
    _putDontNest(result, 1218, 1388);
    
    _putDontNest(result, 1240, 1268);
    
    _putDontNest(result, 1248, 1228);
    
    _putDontNest(result, 266, 474);
    
    _putDontNest(result, 1330, 1438);
    
    _putDontNest(result, 1308, 1448);
    
    _putDontNest(result, 1378, 1486);
    
    _putDontNest(result, 4720, 4730);
    
    _putDontNest(result, 690, 1288);
    
    _putDontNest(result, 712, 1378);
    
    _putDontNest(result, 712, 1248);
    
    _putDontNest(result, 690, 1162);
    
    _putDontNest(result, 896, 1368);
    
    _putDontNest(result, 1278, 1368);
    
    _putDontNest(result, 1168, 1358);
    
    _putDontNest(result, 1218, 1308);
    
    _putDontNest(result, 1230, 1288);
    
    _putDontNest(result, 1204, 1378);
    
    _putDontNest(result, 1220, 1298);
    
    _putDontNest(result, 1204, 1248);
    
    _putDontNest(result, 1174, 1218);
    
    _putDontNest(result, 1168, 1228);
    
    _putDontNest(result, 1358, 1418);
    
    _putDontNest(result, 1298, 1486);
    
    _putDontNest(result, 1388, 1448);
    
    _putDontNest(result, 712, 1298);
    
    _putDontNest(result, 712, 1168);
    
    _putDontNest(result, 876, 1468);
    
    _putDontNest(result, 1188, 1378);
    
    _putDontNest(result, 1270, 1328);
    
    _putDontNest(result, 1228, 1338);
    
    _putDontNest(result, 1188, 1248);
    
    _putDontNest(result, 1158, 1218);
    
    _putDontNest(result, 1368, 1428);
    
    _putDontNest(result, 1398, 1458);
    
    _putDontNest(result, 1348, 1408);
    
    _putDontNest(result, 5828, 5940);
    
    _putDontNest(result, 950, 1268);
    
    _putDontNest(result, 690, 1258);
    
    _putDontNest(result, 950, 1398);
    
    _putDontNest(result, 852, 1428);
    
    _putDontNest(result, 1240, 1318);
    
    _putDontNest(result, 1250, 1308);
    
    _putDontNest(result, 1194, 1238);
    
    _putDontNest(result, 1350, 1458);
    
    _putDontNest(result, 322, 434);
    
    _putDontNest(result, 1330, 1486);
    
    _putDontNest(result, 1390, 1418);
    
    _putDontNest(result, 1378, 1438);
    
    _putDontNest(result, 896, 1258);
    
    _putDontNest(result, 842, 1458);
    
    _putDontNest(result, 1260, 1338);
    
    _putDontNest(result, 1238, 1328);
    
    _putDontNest(result, 1278, 1288);
    
    _putDontNest(result, 1194, 1348);
    
    _putDontNest(result, 1230, 1368);
    
    _putDontNest(result, 1268, 1298);
    
    _putDontNest(result, 1360, 1468);
    
    _putDontNest(result, 1380, 1408);
    
    _putDontNest(result, 4990, 4992);
    
    _putDontNest(result, 906, 1248);
    
    _putDontNest(result, 5384, 5394);
    
    _putDontNest(result, 906, 1378);
   return result;
  }
    
  protected static IntegerMap _initDontNestGroups() {
    IntegerMap result = org.rascalmpl.library.lang.rascal.syntax.RascalRascal._initDontNestGroups();
    int resultStoreId = result.size();
    
    
    ++resultStoreId;
    
    result.putUnsafe(1388, resultStoreId);
    result.putUnsafe(1370, resultStoreId);
    result.putUnsafe(1380, resultStoreId);
    result.putUnsafe(1378, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(1196, resultStoreId);
    result.putUnsafe(1228, resultStoreId);
    result.putUnsafe(1238, resultStoreId);
    result.putUnsafe(1218, resultStoreId);
    result.putUnsafe(1248, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(1468, resultStoreId);
    result.putUnsafe(1450, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(4746, resultStoreId);
    result.putUnsafe(4730, resultStoreId);
    result.putUnsafe(4740, resultStoreId);
    result.putUnsafe(4720, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(5394, resultStoreId);
    result.putUnsafe(5376, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(1390, resultStoreId);
    result.putUnsafe(1398, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(5414, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(5386, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(1278, resultStoreId);
    result.putUnsafe(1288, resultStoreId);
    result.putUnsafe(1268, resultStoreId);
    result.putUnsafe(1250, resultStoreId);
    result.putUnsafe(1298, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(4990, resultStoreId);
    result.putUnsafe(4602, resultStoreId);
    result.putUnsafe(4826, resultStoreId);
    result.putUnsafe(4776, resultStoreId);
    result.putUnsafe(4644, resultStoreId);
    result.putUnsafe(4674, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(1470, resultStoreId);
    result.putUnsafe(1460, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(1358, resultStoreId);
    result.putUnsafe(1340, resultStoreId);
    result.putUnsafe(1338, resultStoreId);
    result.putUnsafe(1368, resultStoreId);
    result.putUnsafe(1350, resultStoreId);
    result.putUnsafe(1348, resultStoreId);
    result.putUnsafe(1330, resultStoreId);
    result.putUnsafe(1360, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(1458, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(410, resultStoreId);
    result.putUnsafe(266, resultStoreId);
    result.putUnsafe(328, resultStoreId);
    result.putUnsafe(408, resultStoreId);
    result.putUnsafe(340, resultStoreId);
    result.putUnsafe(260, resultStoreId);
    result.putUnsafe(322, resultStoreId);
    result.putUnsafe(416, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(444, resultStoreId);
    result.putUnsafe(454, resultStoreId);
    result.putUnsafe(434, resultStoreId);
    result.putUnsafe(464, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(5740, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(1230, resultStoreId);
    result.putUnsafe(1258, resultStoreId);
    result.putUnsafe(1240, resultStoreId);
    result.putUnsafe(1206, resultStoreId);
    result.putUnsafe(1220, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(1158, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(5820, resultStoreId);
    result.putUnsafe(5900, resultStoreId);
    result.putUnsafe(5926, resultStoreId);
    result.putUnsafe(5828, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(1438, resultStoreId);
    result.putUnsafe(1448, resultStoreId);
    result.putUnsafe(1430, resultStoreId);
    result.putUnsafe(1440, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(5384, resultStoreId);
    result.putUnsafe(5360, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(5750, resultStoreId);
    result.putUnsafe(5732, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(1260, resultStoreId);
    result.putUnsafe(1308, resultStoreId);
    result.putUnsafe(1290, resultStoreId);
    result.putUnsafe(1270, resultStoreId);
    result.putUnsafe(1280, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(1194, resultStoreId);
    result.putUnsafe(1174, resultStoreId);
    result.putUnsafe(1204, resultStoreId);
    result.putUnsafe(1188, resultStoreId);
    result.putUnsafe(1168, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(876, resultStoreId);
    result.putUnsafe(906, resultStoreId);
    result.putUnsafe(842, resultStoreId);
    result.putUnsafe(712, resultStoreId);
    result.putUnsafe(886, resultStoreId);
    result.putUnsafe(950, resultStoreId);
    result.putUnsafe(852, resultStoreId);
    result.putUnsafe(1058, resultStoreId);
    result.putUnsafe(770, resultStoreId);
    result.putUnsafe(690, resultStoreId);
    result.putUnsafe(896, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(1418, resultStoreId);
    result.putUnsafe(1428, resultStoreId);
    result.putUnsafe(1408, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(1310, resultStoreId);
    result.putUnsafe(1320, resultStoreId);
    result.putUnsafe(1318, resultStoreId);
    result.putUnsafe(1300, resultStoreId);
    result.putUnsafe(1328, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(474, resultStoreId);
    result.putUnsafe(426, resultStoreId);
    result.putUnsafe(436, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(446, resultStoreId);
    result.putUnsafe(456, resultStoreId);
      
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
	
  private static final IConstructor regular__iter_seps__QualifiedName__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST = (IConstructor) _read("regular(\\iter-seps(sort(\"QualifiedName\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")]))", Factory.Production);
  private static final IConstructor prod__Open_FunctionModifier__lit_open_ = (IConstructor) _read("prod(label(\"Open\",sort(\"FunctionModifier\")),[lit(\"open\")],{})", Factory.Production);
  private static final IConstructor prod__CharacterClass_Sym__charClass_Class_ = (IConstructor) _read("prod(label(\"CharacterClass\",sort(\"Sym\")),[label(\"charClass\",sort(\"Class\"))],{})", Factory.Production);
  private static final IConstructor prod__ListModules_ShellCommand__lit_modules_ = (IConstructor) _read("prod(label(\"ListModules\",sort(\"ShellCommand\")),[lit(\"modules\")],{})", Factory.Production);
  private static final IConstructor prod__Modulo_Expression__lhs_Expression_layouts_LAYOUTLIST_lit_mod_layouts_LAYOUTLIST_rhs_Expression__assoc__left = (IConstructor) _read("prod(label(\"Modulo\",sort(\"Expression\")),[label(\"lhs\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"mod\"),layouts(\"LAYOUTLIST\"),label(\"rhs\",sort(\"Expression\"))],{assoc(left())})", Factory.Production);
  private static final IConstructor prod__Char__UnicodeEscape__tag__category___67_111_110_115_116_97_110_116 = (IConstructor) _read("prod(lex(\"Char\"),[lex(\"UnicodeEscape\")],{tag(category(\"Constant\"))})", Factory.Production);
  private static final IConstructor prod__IfDefinedOrDefault_Assignable__receiver_Assignable_layouts_LAYOUTLIST_lit___63_layouts_LAYOUTLIST_defaultExpression_Expression_ = (IConstructor) _read("prod(label(\"IfDefinedOrDefault\",sort(\"Assignable\")),[label(\"receiver\",sort(\"Assignable\")),layouts(\"LAYOUTLIST\"),lit(\"?\"),layouts(\"LAYOUTLIST\"),label(\"defaultExpression\",sort(\"Expression\"))],{})", Factory.Production);
  private static final IConstructor prod__RascalKeywords__lit_fail_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"fail\")],{})", Factory.Production);
  private static final IConstructor prod__start__Commands__layouts_LAYOUTLIST_top_Commands_layouts_LAYOUTLIST_ = (IConstructor) _read("prod(start(sort(\"Commands\")),[layouts(\"LAYOUTLIST\"),label(\"top\",sort(\"Commands\")),layouts(\"LAYOUTLIST\")],{})", Factory.Production);
  private static final IConstructor prod__NamedRegExp__char_class___range__92_92_char_class___range__47_47_range__60_60_range__62_62_range__92_92_ = (IConstructor) _read("prod(lex(\"NamedRegExp\"),[\\char-class([range(92,92)]),\\char-class([range(47,47),range(60,60),range(62,62),range(92,92)])],{})", Factory.Production);
  private static final IConstructor prod__RationalLiteral__char_class___range__49_57_iter_star__char_class___range__48_57_char_class___range__114_114_char_class___range__48_57_conditional__iter_star__char_class___range__48_57__not_follow__char_class___range__48_57_range__65_90_range__95_95_range__97_122_ = (IConstructor) _read("prod(lex(\"RationalLiteral\"),[\\char-class([range(49,57)]),\\iter-star(\\char-class([range(48,57)])),\\char-class([range(114,114)]),\\char-class([range(48,57)]),conditional(\\iter-star(\\char-class([range(48,57)])),{\\not-follow(\\char-class([range(48,57),range(65,90),range(95,95),range(97,122)]))})],{})", Factory.Production);
  private static final IConstructor prod__TimeZonePart__lit_Z_ = (IConstructor) _read("prod(lex(\"TimeZonePart\"),[lit(\"Z\")],{})", Factory.Production);
  private static final IConstructor prod__RascalKeywords__lit_default_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"default\")],{})", Factory.Production);
  private static final IConstructor prod__RegExpLiteral__lit___47_iter_star__RegExp_lit___47_RegExpModifier_ = (IConstructor) _read("prod(lex(\"RegExpLiteral\"),[lit(\"/\"),\\iter-star(lex(\"RegExp\")),lit(\"/\"),lex(\"RegExpModifier\")],{})", Factory.Production);
  private static final IConstructor prod__HeaderKeyword__lit_layout_ = (IConstructor) _read("prod(keywords(\"HeaderKeyword\"),[lit(\"layout\")],{})", Factory.Production);
  private static final IConstructor prod__lit_fail__char_class___range__102_102_char_class___range__97_97_char_class___range__105_105_char_class___range__108_108_ = (IConstructor) _read("prod(lit(\"fail\"),[\\char-class([range(102,102)]),\\char-class([range(97,97)]),\\char-class([range(105,105)]),\\char-class([range(108,108)])],{})", Factory.Production);
  private static final IConstructor prod__List_Comprehension__lit___91_layouts_LAYOUTLIST_results_iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___124_layouts_LAYOUTLIST_generators_iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___93_ = (IConstructor) _read("prod(label(\"List\",sort(\"Comprehension\")),[lit(\"[\"),layouts(\"LAYOUTLIST\"),label(\"results\",\\iter-seps(sort(\"Expression\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\"|\"),layouts(\"LAYOUTLIST\"),label(\"generators\",\\iter-seps(sort(\"Expression\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\"]\")],{})", Factory.Production);
  private static final IConstructor prod__Name__conditional__seq___conditional__char_class___range__65_90_range__95_95_range__97_122__not_precede__char_class___range__65_90_range__95_95_range__97_122_conditional__iter_star__char_class___range__48_57_range__65_90_range__95_95_range__97_122__not_follow__char_class___range__48_57_range__65_90_range__95_95_range__97_122__delete__RascalKeywords_ = (IConstructor) _read("prod(lex(\"Name\"),[conditional(seq([conditional(\\char-class([range(65,90),range(95,95),range(97,122)]),{\\not-precede(\\char-class([range(65,90),range(95,95),range(97,122)]))}),conditional(\\iter-star(\\char-class([range(48,57),range(65,90),range(95,95),range(97,122)])),{\\not-follow(\\char-class([range(48,57),range(65,90),range(95,95),range(97,122)]))})]),{delete(keywords(\"RascalKeywords\"))})],{})", Factory.Production);
  private static final IConstructor prod__Unlabeled_Prod__modifiers_iter_star_seps__ProdModifier__layouts_LAYOUTLIST_layouts_LAYOUTLIST_args_iter_star_seps__Sym__layouts_LAYOUTLIST_ = (IConstructor) _read("prod(label(\"Unlabeled\",sort(\"Prod\")),[label(\"modifiers\",\\iter-star-seps(sort(\"ProdModifier\"),[layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),label(\"args\",\\iter-star-seps(sort(\"Sym\"),[layouts(\"LAYOUTLIST\")]))],{})", Factory.Production);
  private static final IConstructor prod__NonEmptyBlock_Statement__label_Label_layouts_LAYOUTLIST_lit___123_layouts_LAYOUTLIST_statements_iter_seps__Statement__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_ = (IConstructor) _read("prod(label(\"NonEmptyBlock\",sort(\"Statement\")),[label(\"label\",sort(\"Label\")),layouts(\"LAYOUTLIST\"),lit(\"{\"),layouts(\"LAYOUTLIST\"),label(\"statements\",\\iter-seps(sort(\"Statement\"),[layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\"}\")],{})", Factory.Production);
  private static final IConstructor prod__lit___45_61__char_class___range__45_45_char_class___range__61_61_ = (IConstructor) _read("prod(lit(\"-=\"),[\\char-class([range(45,45)]),\\char-class([range(61,61)])],{})", Factory.Production);
  private static final IConstructor regular__iter_star__char_class___range__48_57 = (IConstructor) _read("regular(\\iter-star(\\char-class([range(48,57)])))", Factory.Production);
  private static final IConstructor prod__Variable_Kind__lit_variable_ = (IConstructor) _read("prod(label(\"Variable\",sort(\"Kind\")),[lit(\"variable\")],{})", Factory.Production);
  private static final IConstructor prod__UnInitialized_Variable__name_Name_ = (IConstructor) _read("prod(label(\"UnInitialized\",sort(\"Variable\")),[label(\"name\",lex(\"Name\"))],{})", Factory.Production);
  private static final IConstructor prod__Interpolated_PathPart__pre_PrePathChars_layouts_LAYOUTLIST_expression_Expression_layouts_LAYOUTLIST_tail_PathTail_ = (IConstructor) _read("prod(label(\"Interpolated\",sort(\"PathPart\")),[label(\"pre\",lex(\"PrePathChars\")),layouts(\"LAYOUTLIST\"),label(\"expression\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),label(\"tail\",sort(\"PathTail\"))],{})", Factory.Production);
  private static final IConstructor prod__List_Expression__lit___91_layouts_LAYOUTLIST_elements_iter_star_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___93_ = (IConstructor) _read("prod(label(\"List\",sort(\"Expression\")),[lit(\"[\"),layouts(\"LAYOUTLIST\"),label(\"elements\",\\iter-star-seps(sort(\"Expression\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\"]\")],{})", Factory.Production);
  private static final IConstructor prod__Subscript_Expression__expression_Expression_layouts_LAYOUTLIST_lit___91_layouts_LAYOUTLIST_subscripts_iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___93_ = (IConstructor) _read("prod(label(\"Subscript\",sort(\"Expression\")),[label(\"expression\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"[\"),layouts(\"LAYOUTLIST\"),label(\"subscripts\",\\iter-seps(sort(\"Expression\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\"]\")],{})", Factory.Production);
  private static final IConstructor regular__opt__char_class___range__48_57 = (IConstructor) _read("regular(opt(\\char-class([range(48,57)])))", Factory.Production);
  private static final IConstructor prod__lit_function__char_class___range__102_102_char_class___range__117_117_char_class___range__110_110_char_class___range__99_99_char_class___range__116_116_char_class___range__105_105_char_class___range__111_111_char_class___range__110_110_ = (IConstructor) _read("prod(lit(\"function\"),[\\char-class([range(102,102)]),\\char-class([range(117,117)]),\\char-class([range(110,110)]),\\char-class([range(99,99)]),\\char-class([range(116,116)]),\\char-class([range(105,105)]),\\char-class([range(111,111)]),\\char-class([range(110,110)])],{})", Factory.Production);
  private static final IConstructor prod__Visit_Statement__label_Label_layouts_LAYOUTLIST_visit_Visit_ = (IConstructor) _read("prod(label(\"Visit\",sort(\"Statement\")),[label(\"label\",sort(\"Label\")),layouts(\"LAYOUTLIST\"),label(\"visit\",sort(\"Visit\"))],{})", Factory.Production);
  private static final IConstructor prod__Default_Module__header_Header_layouts_LAYOUTLIST_body_Body_ = (IConstructor) _read("prod(label(\"Default\",sort(\"Module\")),[label(\"header\",sort(\"Header\")),layouts(\"LAYOUTLIST\"),label(\"body\",sort(\"Body\"))],{})", Factory.Production);
  private static final IConstructor regular__iter_star_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST = (IConstructor) _read("regular(\\iter-star-seps(sort(\"Expression\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")]))", Factory.Production);
  private static final IConstructor prod__lit_int__char_class___range__105_105_char_class___range__110_110_char_class___range__116_116_ = (IConstructor) _read("prod(lit(\"int\"),[\\char-class([range(105,105)]),\\char-class([range(110,110)]),\\char-class([range(116,116)])],{})", Factory.Production);
  private static final IConstructor prod__AssertWithMessage_Statement__lit_assert_layouts_LAYOUTLIST_expression_Expression_layouts_LAYOUTLIST_lit___58_layouts_LAYOUTLIST_message_Expression_layouts_LAYOUTLIST_lit___59_ = (IConstructor) _read("prod(label(\"AssertWithMessage\",sort(\"Statement\")),[lit(\"assert\"),layouts(\"LAYOUTLIST\"),label(\"expression\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\":\"),layouts(\"LAYOUTLIST\"),label(\"message\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\";\")],{})", Factory.Production);
  private static final IConstructor prod__Splice_Pattern__lit___42_layouts_LAYOUTLIST_argument_Pattern_ = (IConstructor) _read("prod(label(\"Splice\",sort(\"Pattern\")),[lit(\"*\"),layouts(\"LAYOUTLIST\"),label(\"argument\",sort(\"Pattern\"))],{})", Factory.Production);
  private static final IConstructor prod__While_Statement__label_Label_layouts_LAYOUTLIST_lit_while_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_conditions_iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_layouts_LAYOUTLIST_body_Statement_ = (IConstructor) _read("prod(label(\"While\",sort(\"Statement\")),[label(\"label\",sort(\"Label\")),layouts(\"LAYOUTLIST\"),lit(\"while\"),layouts(\"LAYOUTLIST\"),lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"conditions\",\\iter-seps(sort(\"Expression\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\")\"),layouts(\"LAYOUTLIST\"),label(\"body\",sort(\"Statement\"))],{})", Factory.Production);
  private static final IConstructor prod__OctalIntegerLiteral_IntegerLiteral__octal_OctalIntegerLiteral_ = (IConstructor) _read("prod(label(\"OctalIntegerLiteral\",sort(\"IntegerLiteral\")),[label(\"octal\",lex(\"OctalIntegerLiteral\"))],{})", Factory.Production);
  private static final IConstructor prod__lit_when__char_class___range__119_119_char_class___range__104_104_char_class___range__101_101_char_class___range__110_110_ = (IConstructor) _read("prod(lit(\"when\"),[\\char-class([range(119,119)]),\\char-class([range(104,104)]),\\char-class([range(101,101)]),\\char-class([range(110,110)])],{})", Factory.Production);
  private static final IConstructor prod__lit_type__char_class___range__116_116_char_class___range__121_121_char_class___range__112_112_char_class___range__101_101_ = (IConstructor) _read("prod(lit(\"type\"),[\\char-class([range(116,116)]),\\char-class([range(121,121)]),\\char-class([range(112,112)]),\\char-class([range(101,101)])],{})", Factory.Production);
  private static final IConstructor prod__RascalKeywords__lit_void_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"void\")],{})", Factory.Production);
  private static final IConstructor prod__LessThan_Expression__lhs_Expression_layouts_LAYOUTLIST_conditional__lit___60__not_follow__lit___layouts_LAYOUTLIST_rhs_Expression__assoc__non_assoc = (IConstructor) _read("prod(label(\"LessThan\",sort(\"Expression\")),[label(\"lhs\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),conditional(lit(\"\\<\"),{\\not-follow(lit(\"-\"))}),layouts(\"LAYOUTLIST\"),label(\"rhs\",sort(\"Expression\"))],{assoc(\\non-assoc())})", Factory.Production);
  private static final IConstructor prod__AppendAfter_Expression__lhs_Expression_layouts_LAYOUTLIST_conditional__lit___60_60__not_follow__lit___61_layouts_LAYOUTLIST_rhs_Expression__assoc__left = (IConstructor) _read("prod(label(\"AppendAfter\",sort(\"Expression\")),[label(\"lhs\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),conditional(lit(\"\\<\\<\"),{\\not-follow(lit(\"=\"))}),layouts(\"LAYOUTLIST\"),label(\"rhs\",sort(\"Expression\"))],{assoc(left())})", Factory.Production);
  private static final IConstructor prod__NonEmptyBlock_Expression__lit___123_layouts_LAYOUTLIST_statements_iter_seps__Statement__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_ = (IConstructor) _read("prod(label(\"NonEmptyBlock\",sort(\"Expression\")),[lit(\"{\"),layouts(\"LAYOUTLIST\"),label(\"statements\",\\iter-seps(sort(\"Statement\"),[layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\"}\")],{})", Factory.Production);
  private static final IConstructor prod__Tuple_Assignable__lit___60_layouts_LAYOUTLIST_elements_iter_seps__Assignable__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___62_ = (IConstructor) _read("prod(label(\"Tuple\",sort(\"Assignable\")),[lit(\"\\<\"),layouts(\"LAYOUTLIST\"),label(\"elements\",\\iter-seps(sort(\"Assignable\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\"\\>\")],{})", Factory.Production);
  private static final IConstructor prod__lit_modules__char_class___range__109_109_char_class___range__111_111_char_class___range__100_100_char_class___range__117_117_char_class___range__108_108_char_class___range__101_101_char_class___range__115_115_ = (IConstructor) _read("prod(lit(\"modules\"),[\\char-class([range(109,109)]),\\char-class([range(111,111)]),\\char-class([range(100,100)]),\\char-class([range(117,117)]),\\char-class([range(108,108)]),\\char-class([range(101,101)]),\\char-class([range(115,115)])],{})", Factory.Production);
  private static final IConstructor prod__PreStringChars__char_class___range__34_34_iter_star__StringCharacter_char_class___range__60_60__tag__category___67_111_110_115_116_97_110_116 = (IConstructor) _read("prod(lex(\"PreStringChars\"),[\\char-class([range(34,34)]),\\iter-star(lex(\"StringCharacter\")),\\char-class([range(60,60)])],{tag(category(\"Constant\"))})", Factory.Production);
  private static final IConstructor prod__Bracket_ProdModifier__lit_bracket_ = (IConstructor) _read("prod(label(\"Bracket\",sort(\"ProdModifier\")),[lit(\"bracket\")],{})", Factory.Production);
  private static final IConstructor prod__lit_catch__char_class___range__99_99_char_class___range__97_97_char_class___range__116_116_char_class___range__99_99_char_class___range__104_104_ = (IConstructor) _read("prod(lit(\"catch\"),[\\char-class([range(99,99)]),\\char-class([range(97,97)]),\\char-class([range(116,116)]),\\char-class([range(99,99)]),\\char-class([range(104,104)])],{})", Factory.Production);
  private static final IConstructor prod__Post_StringTail__post_PostStringChars_ = (IConstructor) _read("prod(label(\"Post\",sort(\"StringTail\")),[label(\"post\",lex(\"PostStringChars\"))],{})", Factory.Production);
  private static final IConstructor prod__lit_case__char_class___range__99_99_char_class___range__97_97_char_class___range__115_115_char_class___range__101_101_ = (IConstructor) _read("prod(lit(\"case\"),[\\char-class([range(99,99)]),\\char-class([range(97,97)]),\\char-class([range(115,115)]),\\char-class([range(101,101)])],{})", Factory.Production);
  private static final IConstructor prod__lit___47_61__char_class___range__47_47_char_class___range__61_61_ = (IConstructor) _read("prod(lit(\"/=\"),[\\char-class([range(47,47)]),\\char-class([range(61,61)])],{})", Factory.Production);
  private static final IConstructor regular__iter_seps__Case__layouts_LAYOUTLIST = (IConstructor) _read("regular(\\iter-seps(sort(\"Case\"),[layouts(\"LAYOUTLIST\")]))", Factory.Production);
  private static final IConstructor regular__seq___char_class___range__44_44_range__46_46_char_class___range__48_57_opt__seq___char_class___range__48_57_opt__char_class___range__48_57 = (IConstructor) _read("regular(seq([\\char-class([range(44,44),range(46,46)]),\\char-class([range(48,57)]),opt(seq([\\char-class([range(48,57)]),opt(\\char-class([range(48,57)]))]))]))", Factory.Production);
  private static final IConstructor prod__DataAbstract_Declaration__tags_Tags_layouts_LAYOUTLIST_visibility_Visibility_layouts_LAYOUTLIST_lit_data_layouts_LAYOUTLIST_user_UserType_layouts_LAYOUTLIST_lit___59_ = (IConstructor) _read("prod(label(\"DataAbstract\",sort(\"Declaration\")),[label(\"tags\",sort(\"Tags\")),layouts(\"LAYOUTLIST\"),label(\"visibility\",sort(\"Visibility\")),layouts(\"LAYOUTLIST\"),lit(\"data\"),layouts(\"LAYOUTLIST\"),label(\"user\",sort(\"UserType\")),layouts(\"LAYOUTLIST\"),lit(\";\")],{})", Factory.Production);
  private static final IConstructor prod__RegExp__lit___60_Name_lit___58_iter_star__NamedRegExp_lit___62_ = (IConstructor) _read("prod(lex(\"RegExp\"),[lit(\"\\<\"),lex(\"Name\"),lit(\":\"),\\iter-star(lex(\"NamedRegExp\")),lit(\"\\>\")],{})", Factory.Production);
  private static final IConstructor prod__SetOption_ShellCommand__lit_set_layouts_LAYOUTLIST_name_QualifiedName_layouts_LAYOUTLIST_expression_Expression_ = (IConstructor) _read("prod(label(\"SetOption\",sort(\"ShellCommand\")),[lit(\"set\"),layouts(\"LAYOUTLIST\"),label(\"name\",sort(\"QualifiedName\")),layouts(\"LAYOUTLIST\"),label(\"expression\",sort(\"Expression\"))],{})", Factory.Production);
  private static final IConstructor prod__lit_self__char_class___range__115_115_char_class___range__101_101_char_class___range__108_108_char_class___range__102_102_ = (IConstructor) _read("prod(lit(\"self\"),[\\char-class([range(115,115)]),\\char-class([range(101,101)]),\\char-class([range(108,108)]),\\char-class([range(102,102)])],{})", Factory.Production);
  private static final IConstructor prod__LAYOUT__Comment_ = (IConstructor) _read("prod(lex(\"LAYOUT\"),[lex(\"Comment\")],{})", Factory.Production);
  private static final IConstructor regular__iter_seps__TypeVar__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST = (IConstructor) _read("regular(\\iter-seps(sort(\"TypeVar\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")]))", Factory.Production);
  private static final IConstructor prod__IfDefined_Assignment__lit___63_61_ = (IConstructor) _read("prod(label(\"IfDefined\",sort(\"Assignment\")),[lit(\"?=\")],{})", Factory.Production);
  private static final IConstructor prod__HeaderKeyword__lit_import_ = (IConstructor) _read("prod(keywords(\"HeaderKeyword\"),[lit(\"import\")],{})", Factory.Production);
  private static final IConstructor prod__Java_FunctionModifier__lit_java_ = (IConstructor) _read("prod(label(\"Java\",sort(\"FunctionModifier\")),[lit(\"java\")],{})", Factory.Production);
  private static final IConstructor prod__Default_Case__lit_default_layouts_LAYOUTLIST_lit___58_layouts_LAYOUTLIST_statement_Statement__tag__Foldable = (IConstructor) _read("prod(label(\"Default\",sort(\"Case\")),[lit(\"default\"),layouts(\"LAYOUTLIST\"),lit(\":\"),layouts(\"LAYOUTLIST\"),label(\"statement\",sort(\"Statement\"))],{tag(Foldable())})", Factory.Production);
  private static final IConstructor prod__lit___47_47__char_class___range__47_47_char_class___range__47_47_ = (IConstructor) _read("prod(lit(\"//\"),[\\char-class([range(47,47)]),\\char-class([range(47,47)])],{})", Factory.Production);
  private static final IConstructor prod__lit_help__char_class___range__104_104_char_class___range__101_101_char_class___range__108_108_char_class___range__112_112_ = (IConstructor) _read("prod(lit(\"help\"),[\\char-class([range(104,104)]),\\char-class([range(101,101)]),\\char-class([range(108,108)]),\\char-class([range(112,112)])],{})", Factory.Production);
  private static final IConstructor prod__lit_num__char_class___range__110_110_char_class___range__117_117_char_class___range__109_109_ = (IConstructor) _read("prod(lit(\"num\"),[\\char-class([range(110,110)]),\\char-class([range(117,117)]),\\char-class([range(109,109)])],{})", Factory.Production);
  private static final IConstructor prod__lit_tag__char_class___range__116_116_char_class___range__97_97_char_class___range__103_103_ = (IConstructor) _read("prod(lit(\"tag\"),[\\char-class([range(116,116)]),\\char-class([range(97,97)]),\\char-class([range(103,103)])],{})", Factory.Production);
  private static final IConstructor prod__Char__lit___92_char_class___range__32_32_range__34_34_range__39_39_range__45_45_range__60_60_range__62_62_range__91_93_range__98_98_range__102_102_range__110_110_range__114_114_range__116_116__tag__category___67_111_110_115_116_97_110_116 = (IConstructor) _read("prod(lex(\"Char\"),[lit(\"\\\\\"),\\char-class([range(32,32),range(34,34),range(39,39),range(45,45),range(60,60),range(62,62),range(91,93),range(98,98),range(102,102),range(110,110),range(114,114),range(116,116)])],{tag(category(\"Constant\"))})", Factory.Production);
  private static final IConstructor prod__NotFollow_Sym__symbol_Sym_layouts_LAYOUTLIST_lit___33_62_62_layouts_LAYOUTLIST_match_Sym__assoc__left = (IConstructor) _read("prod(label(\"NotFollow\",sort(\"Sym\")),[label(\"symbol\",sort(\"Sym\")),layouts(\"LAYOUTLIST\"),lit(\"!\\>\\>\"),layouts(\"LAYOUTLIST\"),label(\"match\",sort(\"Sym\"))],{assoc(left())})", Factory.Production);
  private static final IConstructor regular__iter__char_class___range__48_55 = (IConstructor) _read("regular(iter(\\char-class([range(48,55)])))", Factory.Production);
  private static final IConstructor regular__iter__char_class___range__48_57 = (IConstructor) _read("regular(iter(\\char-class([range(48,57)])))", Factory.Production);
  private static final IConstructor prod__Template_StringMiddle__mid_MidStringChars_layouts_LAYOUTLIST_template_StringTemplate_layouts_LAYOUTLIST_tail_StringMiddle_ = (IConstructor) _read("prod(label(\"Template\",sort(\"StringMiddle\")),[label(\"mid\",lex(\"MidStringChars\")),layouts(\"LAYOUTLIST\"),label(\"template\",sort(\"StringTemplate\")),layouts(\"LAYOUTLIST\"),label(\"tail\",sort(\"StringMiddle\"))],{})", Factory.Production);
  private static final IConstructor prod__RascalKeywords__lit_join_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"join\")],{})", Factory.Production);
  private static final IConstructor prod__Default_Assignment__lit___61_ = (IConstructor) _read("prod(label(\"Default\",sort(\"Assignment\")),[lit(\"=\")],{})", Factory.Production);
  private static final IConstructor prod__start__Module__layouts_LAYOUTLIST_top_Module_layouts_LAYOUTLIST_ = (IConstructor) _read("prod(start(sort(\"Module\")),[layouts(\"LAYOUTLIST\"),label(\"top\",sort(\"Module\")),layouts(\"LAYOUTLIST\")],{})", Factory.Production);
  private static final IConstructor prod__Empty_Tag__lit___64_layouts_LAYOUTLIST_name_Name__tag__Folded_tag__category___67_111_109_109_101_110_116 = (IConstructor) _read("prod(label(\"Empty\",sort(\"Tag\")),[lit(\"@\"),layouts(\"LAYOUTLIST\"),label(\"name\",lex(\"Name\"))],{tag(Folded()),tag(category(\"Comment\"))})", Factory.Production);
  private static final IConstructor prod__lit___58_47_47__char_class___range__58_58_char_class___range__47_47_char_class___range__47_47_ = (IConstructor) _read("prod(lit(\"://\"),[\\char-class([range(58,58)]),\\char-class([range(47,47)]),\\char-class([range(47,47)])],{})", Factory.Production);
  private static final IConstructor prod__BottomUp_Strategy__lit_bottom_up_ = (IConstructor) _read("prod(label(\"BottomUp\",sort(\"Strategy\")),[lit(\"bottom-up\")],{})", Factory.Production);
  private static final IConstructor prod__Parameters_Header__tags_Tags_layouts_LAYOUTLIST_lit_module_layouts_LAYOUTLIST_name_QualifiedName_layouts_LAYOUTLIST_params_ModuleParameters_layouts_LAYOUTLIST_imports_iter_star_seps__Import__layouts_LAYOUTLIST_ = (IConstructor) _read("prod(label(\"Parameters\",sort(\"Header\")),[label(\"tags\",sort(\"Tags\")),layouts(\"LAYOUTLIST\"),lit(\"module\"),layouts(\"LAYOUTLIST\"),label(\"name\",sort(\"QualifiedName\")),layouts(\"LAYOUTLIST\"),label(\"params\",sort(\"ModuleParameters\")),layouts(\"LAYOUTLIST\"),label(\"imports\",\\iter-star-seps(sort(\"Import\"),[layouts(\"LAYOUTLIST\")]))],{})", Factory.Production);
  private static final IConstructor prod__RegExp__char_class___range__92_92_char_class___range__47_47_range__60_60_range__62_62_range__92_92_ = (IConstructor) _read("prod(lex(\"RegExp\"),[\\char-class([range(92,92)]),\\char-class([range(47,47),range(60,60),range(62,62),range(92,92)])],{})", Factory.Production);
  private static final IConstructor prod__CallOrTree_Expression__expression_Expression_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_arguments_iter_star_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_ = (IConstructor) _read("prod(label(\"CallOrTree\",sort(\"Expression\")),[label(\"expression\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"arguments\",\\iter-star-seps(sort(\"Expression\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\")\")],{})", Factory.Production);
  private static final IConstructor prod__Sequence_Sym__lit___40_layouts_LAYOUTLIST_first_Sym_layouts_LAYOUTLIST_sequence_iter_seps__Sym__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_ = (IConstructor) _read("prod(label(\"Sequence\",sort(\"Sym\")),[lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"first\",sort(\"Sym\")),layouts(\"LAYOUTLIST\"),label(\"sequence\",\\iter-seps(sort(\"Sym\"),[layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\")\")],{})", Factory.Production);
  private static final IConstructor prod__Rational_BasicType__lit_rat_ = (IConstructor) _read("prod(label(\"Rational\",sort(\"BasicType\")),[lit(\"rat\")],{})", Factory.Production);
  private static final IConstructor prod__NotIn_Expression__lhs_Expression_layouts_LAYOUTLIST_lit_notin_layouts_LAYOUTLIST_rhs_Expression__assoc__non_assoc = (IConstructor) _read("prod(label(\"NotIn\",sort(\"Expression\")),[label(\"lhs\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"notin\"),layouts(\"LAYOUTLIST\"),label(\"rhs\",sort(\"Expression\"))],{assoc(\\non-assoc())})", Factory.Production);
  private static final IConstructor prod__First_Prod__lhs_Prod_layouts_LAYOUTLIST_conditional__lit___62__not_follow__lit___62_layouts_LAYOUTLIST_rhs_Prod__assoc__left = (IConstructor) _read("prod(label(\"First\",sort(\"Prod\")),[label(\"lhs\",sort(\"Prod\")),layouts(\"LAYOUTLIST\"),conditional(lit(\"\\>\"),{\\not-follow(lit(\"\\>\"))}),layouts(\"LAYOUTLIST\"),label(\"rhs\",sort(\"Prod\"))],{assoc(left())})", Factory.Production);
  private static final IConstructor regular__iter_star__char_class___range__100_100_range__105_105_range__109_109_range__115_115 = (IConstructor) _read("regular(\\iter-star(\\char-class([range(100,100),range(105,105),range(109,109),range(115,115)])))", Factory.Production);
  private static final IConstructor prod__Parametric_UserType__conditional__name_QualifiedName__follow__lit___91_layouts_LAYOUTLIST_lit___91_layouts_LAYOUTLIST_parameters_iter_seps__Type__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___93_ = (IConstructor) _read("prod(label(\"Parametric\",sort(\"UserType\")),[conditional(label(\"name\",sort(\"QualifiedName\")),{follow(lit(\"[\"))}),layouts(\"LAYOUTLIST\"),lit(\"[\"),layouts(\"LAYOUTLIST\"),label(\"parameters\",\\iter-seps(sort(\"Type\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\"]\")],{})", Factory.Production);
  private static final IConstructor prod__lit___33_62_62__char_class___range__33_33_char_class___range__62_62_char_class___range__62_62_ = (IConstructor) _read("prod(lit(\"!\\>\\>\"),[\\char-class([range(33,33)]),\\char-class([range(62,62)]),\\char-class([range(62,62)])],{})", Factory.Production);
  private static final IConstructor prod__GetAnnotation_Expression__expression_Expression_layouts_LAYOUTLIST_lit___64_layouts_LAYOUTLIST_name_Name_ = (IConstructor) _read("prod(label(\"GetAnnotation\",sort(\"Expression\")),[label(\"expression\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"@\"),layouts(\"LAYOUTLIST\"),label(\"name\",lex(\"Name\"))],{})", Factory.Production);
  private static final IConstructor prod__lit___46_46__char_class___range__46_46_char_class___range__46_46_ = (IConstructor) _read("prod(lit(\"..\"),[\\char-class([range(46,46)]),\\char-class([range(46,46)])],{})", Factory.Production);
  private static final IConstructor prod__Toplevels_Body__toplevels_iter_star_seps__Toplevel__layouts_LAYOUTLIST_ = (IConstructor) _read("prod(label(\"Toplevels\",sort(\"Body\")),[label(\"toplevels\",\\iter-star-seps(sort(\"Toplevel\"),[layouts(\"LAYOUTLIST\")]))],{})", Factory.Production);
  private static final IConstructor prod__Nonterminal_Sym__conditional__nonterminal_Nonterminal__not_follow__lit___91_ = (IConstructor) _read("prod(label(\"Nonterminal\",sort(\"Sym\")),[conditional(label(\"nonterminal\",lex(\"Nonterminal\")),{\\not-follow(lit(\"[\"))})],{})", Factory.Production);
  private static final IConstructor prod__lit_throw__char_class___range__116_116_char_class___range__104_104_char_class___range__114_114_char_class___range__111_111_char_class___range__119_119_ = (IConstructor) _read("prod(lit(\"throw\"),[\\char-class([range(116,116)]),\\char-class([range(104,104)]),\\char-class([range(114,114)]),\\char-class([range(111,111)]),\\char-class([range(119,119)])],{})", Factory.Production);
  private static final IConstructor prod__VarArgs_Parameters__lit___40_layouts_LAYOUTLIST_formals_Formals_layouts_LAYOUTLIST_lit___46_46_46_layouts_LAYOUTLIST_lit___41_ = (IConstructor) _read("prod(label(\"VarArgs\",sort(\"Parameters\")),[lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"formals\",sort(\"Formals\")),layouts(\"LAYOUTLIST\"),lit(\"...\"),layouts(\"LAYOUTLIST\"),lit(\")\")],{})", Factory.Production);
  private static final IConstructor prod__Data_Declaration__tags_Tags_layouts_LAYOUTLIST_visibility_Visibility_layouts_LAYOUTLIST_lit_data_layouts_LAYOUTLIST_user_UserType_layouts_LAYOUTLIST_lit___61_layouts_LAYOUTLIST_variants_iter_seps__Variant__layouts_LAYOUTLIST_lit___124_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___59__tag__Foldable = (IConstructor) _read("prod(label(\"Data\",sort(\"Declaration\")),[label(\"tags\",sort(\"Tags\")),layouts(\"LAYOUTLIST\"),label(\"visibility\",sort(\"Visibility\")),layouts(\"LAYOUTLIST\"),lit(\"data\"),layouts(\"LAYOUTLIST\"),label(\"user\",sort(\"UserType\")),layouts(\"LAYOUTLIST\"),lit(\"=\"),layouts(\"LAYOUTLIST\"),label(\"variants\",\\iter-seps(sort(\"Variant\"),[layouts(\"LAYOUTLIST\"),lit(\"|\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\";\")],{tag(Foldable())})", Factory.Production);
  private static final IConstructor prod__Unequal_Sym__symbol_Sym_layouts_LAYOUTLIST_lit___92_layouts_LAYOUTLIST_match_Sym__assoc__left = (IConstructor) _read("prod(label(\"Unequal\",sort(\"Sym\")),[label(\"symbol\",sort(\"Sym\")),layouts(\"LAYOUTLIST\"),lit(\"\\\\\"),layouts(\"LAYOUTLIST\"),label(\"match\",sort(\"Sym\"))],{assoc(left())})", Factory.Production);
  private static final IConstructor prod__Return_Statement__lit_return_layouts_LAYOUTLIST_statement_Statement__assoc__non_assoc = (IConstructor) _read("prod(label(\"Return\",sort(\"Statement\")),[lit(\"return\"),layouts(\"LAYOUTLIST\"),label(\"statement\",sort(\"Statement\"))],{assoc(\\non-assoc())})", Factory.Production);
  private static final IConstructor prod__RascalKeywords__lit_real_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"real\")],{})", Factory.Production);
  private static final IConstructor prod__Function_Kind__lit_function_ = (IConstructor) _read("prod(label(\"Function\",sort(\"Kind\")),[lit(\"function\")],{})", Factory.Production);
  private static final IConstructor prod__Remainder_Expression__lhs_Expression_layouts_LAYOUTLIST_lit___37_layouts_LAYOUTLIST_rhs_Expression__assoc__left = (IConstructor) _read("prod(label(\"Remainder\",sort(\"Expression\")),[label(\"lhs\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"%\"),layouts(\"LAYOUTLIST\"),label(\"rhs\",sort(\"Expression\"))],{assoc(left())})", Factory.Production);
  private static final IConstructor prod__lit_on__char_class___range__111_111_char_class___range__110_110_ = (IConstructor) _read("prod(lit(\"on\"),[\\char-class([range(111,111)]),\\char-class([range(110,110)])],{})", Factory.Production);
  private static final IConstructor prod__lit___47_42__char_class___range__47_47_char_class___range__42_42_ = (IConstructor) _read("prod(lit(\"/*\"),[\\char-class([range(47,47)]),\\char-class([range(42,42)])],{})", Factory.Production);
  private static final IConstructor prod__RascalKeywords__lit_break_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"break\")],{})", Factory.Production);
  private static final IConstructor prod__lit_is__char_class___range__105_105_char_class___range__115_115_ = (IConstructor) _read("prod(lit(\"is\"),[\\char-class([range(105,105)]),\\char-class([range(115,115)])],{})", Factory.Production);
  private static final IConstructor prod__lit_it__char_class___range__105_105_char_class___range__116_116_ = (IConstructor) _read("prod(lit(\"it\"),[\\char-class([range(105,105)]),\\char-class([range(116,116)])],{})", Factory.Production);
  private static final IConstructor prod__utf16_UnicodeEscape__lit___92_char_class___range__117_117_char_class___range__48_57_range__65_70_range__97_102_char_class___range__48_57_range__65_70_range__97_102_char_class___range__48_57_range__65_70_range__97_102_char_class___range__48_57_range__65_70_range__97_102_ = (IConstructor) _read("prod(label(\"utf16\",lex(\"UnicodeEscape\")),[lit(\"\\\\\"),\\char-class([range(117,117)]),\\char-class([range(48,57),range(65,70),range(97,102)]),\\char-class([range(48,57),range(65,70),range(97,102)]),\\char-class([range(48,57),range(65,70),range(97,102)]),\\char-class([range(48,57),range(65,70),range(97,102)])],{})", Factory.Production);
  private static final IConstructor prod__TryFinally_Statement__lit_try_layouts_LAYOUTLIST_body_Statement_layouts_LAYOUTLIST_handlers_iter_seps__Catch__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit_finally_layouts_LAYOUTLIST_finallyBody_Statement_ = (IConstructor) _read("prod(label(\"TryFinally\",sort(\"Statement\")),[lit(\"try\"),layouts(\"LAYOUTLIST\"),label(\"body\",sort(\"Statement\")),layouts(\"LAYOUTLIST\"),label(\"handlers\",\\iter-seps(sort(\"Catch\"),[layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\"finally\"),layouts(\"LAYOUTLIST\"),label(\"finallyBody\",sort(\"Statement\"))],{})", Factory.Production);
  private static final IConstructor prod__User_Type__conditional__user_UserType__delete__HeaderKeyword_ = (IConstructor) _read("prod(label(\"User\",sort(\"Type\")),[conditional(label(\"user\",sort(\"UserType\")),{delete(keywords(\"HeaderKeyword\"))})],{})", Factory.Production);
  private static final IConstructor prod__Post_PathTail__post_PostPathChars_ = (IConstructor) _read("prod(label(\"Post\",sort(\"PathTail\")),[label(\"post\",lex(\"PostPathChars\"))],{})", Factory.Production);
  private static final IConstructor prod__Abstract_FunctionDeclaration__tags_Tags_layouts_LAYOUTLIST_visibility_Visibility_layouts_LAYOUTLIST_signature_Signature_layouts_LAYOUTLIST_lit___59_ = (IConstructor) _read("prod(label(\"Abstract\",sort(\"FunctionDeclaration\")),[label(\"tags\",sort(\"Tags\")),layouts(\"LAYOUTLIST\"),label(\"visibility\",sort(\"Visibility\")),layouts(\"LAYOUTLIST\"),label(\"signature\",sort(\"Signature\")),layouts(\"LAYOUTLIST\"),lit(\";\")],{})", Factory.Production);
  private static final IConstructor prod__FieldAccess_Assignable__receiver_Assignable_layouts_LAYOUTLIST_lit___46_layouts_LAYOUTLIST_field_Name_ = (IConstructor) _read("prod(label(\"FieldAccess\",sort(\"Assignable\")),[label(\"receiver\",sort(\"Assignable\")),layouts(\"LAYOUTLIST\"),lit(\".\"),layouts(\"LAYOUTLIST\"),label(\"field\",lex(\"Name\"))],{})", Factory.Production);
  private static final IConstructor prod__Bracket_Expression__lit___40_layouts_LAYOUTLIST_expression_Expression_layouts_LAYOUTLIST_lit___41__bracket = (IConstructor) _read("prod(label(\"Bracket\",sort(\"Expression\")),[lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"expression\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\")\")],{bracket()})", Factory.Production);
  private static final IConstructor prod__lit_loc__char_class___range__108_108_char_class___range__111_111_char_class___range__99_99_ = (IConstructor) _read("prod(lit(\"loc\"),[\\char-class([range(108,108)]),\\char-class([range(111,111)]),\\char-class([range(99,99)])],{})", Factory.Production);
  private static final IConstructor prod__URLChars__iter_star__char_class___range__0_8_range__11_12_range__14_31_range__33_59_range__61_123_range__125_16777215_ = (IConstructor) _read("prod(lex(\"URLChars\"),[\\iter-star(\\char-class([range(0,8),range(11,12),range(14,31),range(33,59),range(61,123),range(125,16777215)]))],{})", Factory.Production);
  private static final IConstructor prod__Layout_SyntaxDefinition__vis_Visibility_layouts_LAYOUTLIST_lit_layout_layouts_LAYOUTLIST_defined_Sym_layouts_LAYOUTLIST_lit___61_layouts_LAYOUTLIST_production_Prod_layouts_LAYOUTLIST_lit___59__tag__Foldable = (IConstructor) _read("prod(label(\"Layout\",sort(\"SyntaxDefinition\")),[label(\"vis\",sort(\"Visibility\")),layouts(\"LAYOUTLIST\"),lit(\"layout\"),layouts(\"LAYOUTLIST\"),label(\"defined\",sort(\"Sym\")),layouts(\"LAYOUTLIST\"),lit(\"=\"),layouts(\"LAYOUTLIST\"),label(\"production\",sort(\"Prod\")),layouts(\"LAYOUTLIST\"),lit(\";\")],{tag(Foldable())})", Factory.Production);
  private static final IConstructor prod__Precede_Sym__match_Sym_layouts_LAYOUTLIST_lit___60_60_layouts_LAYOUTLIST_symbol_Sym__assoc__right = (IConstructor) _read("prod(label(\"Precede\",sort(\"Sym\")),[label(\"match\",sort(\"Sym\")),layouts(\"LAYOUTLIST\"),lit(\"\\<\\<\"),layouts(\"LAYOUTLIST\"),label(\"symbol\",sort(\"Sym\"))],{assoc(right())})", Factory.Production);
  private static final IConstructor prod__BooleanLiteral__lit_false_ = (IConstructor) _read("prod(lex(\"BooleanLiteral\"),[lit(\"false\")],{})", Factory.Production);
  private static final IConstructor prod__lit_in__char_class___range__105_105_char_class___range__110_110_ = (IConstructor) _read("prod(lit(\"in\"),[\\char-class([range(105,105)]),\\char-class([range(110,110)])],{})", Factory.Production);
  private static final IConstructor prod__ReifyType_Expression__lit___35_layouts_LAYOUTLIST_conditional__type_Type__not_follow__lit___91_ = (IConstructor) _read("prod(label(\"ReifyType\",sort(\"Expression\")),[lit(\"#\"),layouts(\"LAYOUTLIST\"),conditional(label(\"type\",sort(\"Type\")),{\\not-follow(lit(\"[\"))})],{})", Factory.Production);
  private static final IConstructor prod__Assert_Statement__lit_assert_layouts_LAYOUTLIST_expression_Expression_layouts_LAYOUTLIST_lit___59_ = (IConstructor) _read("prod(label(\"Assert\",sort(\"Statement\")),[lit(\"assert\"),layouts(\"LAYOUTLIST\"),label(\"expression\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\";\")],{})", Factory.Production);
  private static final IConstructor prod__Literal_Pattern__literal_Literal_ = (IConstructor) _read("prod(label(\"Literal\",sort(\"Pattern\")),[label(\"literal\",sort(\"Literal\"))],{})", Factory.Production);
  private static final IConstructor prod__start__EvalCommand__layouts_LAYOUTLIST_top_EvalCommand_layouts_LAYOUTLIST_ = (IConstructor) _read("prod(start(sort(\"EvalCommand\")),[layouts(\"LAYOUTLIST\"),label(\"top\",sort(\"EvalCommand\")),layouts(\"LAYOUTLIST\")],{})", Factory.Production);
  private static final IConstructor prod__lit_right__char_class___range__114_114_char_class___range__105_105_char_class___range__103_103_char_class___range__104_104_char_class___range__116_116_ = (IConstructor) _read("prod(lit(\"right\"),[\\char-class([range(114,114)]),\\char-class([range(105,105)]),\\char-class([range(103,103)]),\\char-class([range(104,104)]),\\char-class([range(116,116)])],{})", Factory.Production);
  private static final IConstructor prod__lit_declarations__char_class___range__100_100_char_class___range__101_101_char_class___range__99_99_char_class___range__108_108_char_class___range__97_97_char_class___range__114_114_char_class___range__97_97_char_class___range__116_116_char_class___range__105_105_char_class___range__111_111_char_class___range__110_110_char_class___range__115_115_ = (IConstructor) _read("prod(lit(\"declarations\"),[\\char-class([range(100,100)]),\\char-class([range(101,101)]),\\char-class([range(99,99)]),\\char-class([range(108,108)]),\\char-class([range(97,97)]),\\char-class([range(114,114)]),\\char-class([range(97,97)]),\\char-class([range(116,116)]),\\char-class([range(105,105)]),\\char-class([range(111,111)]),\\char-class([range(110,110)]),\\char-class([range(115,115)])],{})", Factory.Production);
  private static final IConstructor prod__DatePart__char_class___range__48_57_char_class___range__48_57_char_class___range__48_57_char_class___range__48_57_char_class___range__48_49_char_class___range__48_57_char_class___range__48_51_char_class___range__48_57_ = (IConstructor) _read("prod(lex(\"DatePart\"),[\\char-class([range(48,57)]),\\char-class([range(48,57)]),\\char-class([range(48,57)]),\\char-class([range(48,57)]),\\char-class([range(48,49)]),\\char-class([range(48,57)]),\\char-class([range(48,51)]),\\char-class([range(48,57)])],{})", Factory.Production);
  private static final IConstructor prod__Closure_Expression__type_Type_layouts_LAYOUTLIST_parameters_Parameters_layouts_LAYOUTLIST_lit___123_layouts_LAYOUTLIST_statements_iter_seps__Statement__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_ = (IConstructor) _read("prod(label(\"Closure\",sort(\"Expression\")),[label(\"type\",sort(\"Type\")),layouts(\"LAYOUTLIST\"),label(\"parameters\",sort(\"Parameters\")),layouts(\"LAYOUTLIST\"),lit(\"{\"),layouts(\"LAYOUTLIST\"),label(\"statements\",\\iter-seps(sort(\"Statement\"),[layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\"}\")],{})", Factory.Production);
  private static final IConstructor prod__Anti_Pattern__lit___33_layouts_LAYOUTLIST_pattern_Pattern_ = (IConstructor) _read("prod(label(\"Anti\",sort(\"Pattern\")),[lit(\"!\"),layouts(\"LAYOUTLIST\"),label(\"pattern\",sort(\"Pattern\"))],{})", Factory.Production);
  private static final IConstructor prod__DoWhile_Statement__label_Label_layouts_LAYOUTLIST_lit_do_layouts_LAYOUTLIST_body_Statement_layouts_LAYOUTLIST_lit_while_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_condition_Expression_layouts_LAYOUTLIST_lit___41_layouts_LAYOUTLIST_lit___59_ = (IConstructor) _read("prod(label(\"DoWhile\",sort(\"Statement\")),[label(\"label\",sort(\"Label\")),layouts(\"LAYOUTLIST\"),lit(\"do\"),layouts(\"LAYOUTLIST\"),label(\"body\",sort(\"Statement\")),layouts(\"LAYOUTLIST\"),lit(\"while\"),layouts(\"LAYOUTLIST\"),lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"condition\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\")\"),layouts(\"LAYOUTLIST\"),lit(\";\")],{})", Factory.Production);
  private static final IConstructor prod__RascalKeywords__lit_test_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"test\")],{})", Factory.Production);
  private static final IConstructor prod__Join_Expression__lhs_Expression_layouts_LAYOUTLIST_lit_join_layouts_LAYOUTLIST_rhs_Expression__assoc__left = (IConstructor) _read("prod(label(\"Join\",sort(\"Expression\")),[label(\"lhs\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"join\"),layouts(\"LAYOUTLIST\"),label(\"rhs\",sort(\"Expression\"))],{assoc(left())})", Factory.Production);
  private static final IConstructor prod__Innermost_Strategy__lit_innermost_ = (IConstructor) _read("prod(label(\"Innermost\",sort(\"Strategy\")),[lit(\"innermost\")],{})", Factory.Production);
  private static final IConstructor prod__RascalKeywords__lit_start_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"start\")],{})", Factory.Production);
  private static final IConstructor prod__Throw_Statement__lit_throw_layouts_LAYOUTLIST_statement_Statement__assoc__non_assoc = (IConstructor) _read("prod(label(\"Throw\",sort(\"Statement\")),[lit(\"throw\"),layouts(\"LAYOUTLIST\"),label(\"statement\",sort(\"Statement\"))],{assoc(\\non-assoc())})", Factory.Production);
  private static final IConstructor prod__IfThenElse_Expression__condition_Expression_layouts_LAYOUTLIST_lit___63_layouts_LAYOUTLIST_thenExp_Expression_layouts_LAYOUTLIST_lit___58_layouts_LAYOUTLIST_elseExp_Expression__assoc__right = (IConstructor) _read("prod(label(\"IfThenElse\",sort(\"Expression\")),[label(\"condition\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"?\"),layouts(\"LAYOUTLIST\"),label(\"thenExp\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\":\"),layouts(\"LAYOUTLIST\"),label(\"elseExp\",sort(\"Expression\"))],{assoc(right())})", Factory.Production);
  private static final IConstructor prod__It_Expression__conditional__lit_it__not_precede__char_class___range__65_90_range__95_95_range__97_122_not_follow__char_class___range__65_90_range__95_95_range__97_122_ = (IConstructor) _read("prod(label(\"It\",sort(\"Expression\")),[conditional(lit(\"it\"),{\\not-precede(\\char-class([range(65,90),range(95,95),range(97,122)])),\\not-follow(\\char-class([range(65,90),range(95,95),range(97,122)]))})],{})", Factory.Production);
  private static final IConstructor prod__Expression_Statement__expression_Expression_layouts_LAYOUTLIST_lit___59_ = (IConstructor) _read("prod(label(\"Expression\",sort(\"Statement\")),[label(\"expression\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\";\")],{})", Factory.Production);
  private static final IConstructor prod__lit___59__char_class___range__59_59_ = (IConstructor) _read("prod(lit(\";\"),[\\char-class([range(59,59)])],{})", Factory.Production);
  private static final IConstructor prod__lit___58__char_class___range__58_58_ = (IConstructor) _read("prod(lit(\":\"),[\\char-class([range(58,58)])],{})", Factory.Production);
  private static final IConstructor prod__Insert_Statement__lit_insert_layouts_LAYOUTLIST_dataTarget_DataTarget_layouts_LAYOUTLIST_statement_Statement__assoc__non_assoc = (IConstructor) _read("prod(label(\"Insert\",sort(\"Statement\")),[lit(\"insert\"),layouts(\"LAYOUTLIST\"),label(\"dataTarget\",sort(\"DataTarget\")),layouts(\"LAYOUTLIST\"),label(\"statement\",sort(\"Statement\"))],{assoc(\\non-assoc())})", Factory.Production);
  private static final IConstructor prod__lit___61__char_class___range__61_61_ = (IConstructor) _read("prod(lit(\"=\"),[\\char-class([range(61,61)])],{})", Factory.Production);
  private static final IConstructor prod__lit_data__char_class___range__100_100_char_class___range__97_97_char_class___range__116_116_char_class___range__97_97_ = (IConstructor) _read("prod(lit(\"data\"),[\\char-class([range(100,100)]),\\char-class([range(97,97)]),\\char-class([range(116,116)]),\\char-class([range(97,97)])],{})", Factory.Production);
  private static final IConstructor prod__lit___60__char_class___range__60_60_ = (IConstructor) _read("prod(lit(\"\\<\"),[\\char-class([range(60,60)])],{})", Factory.Production);
  private static final IConstructor prod__Data_Kind__lit_data_ = (IConstructor) _read("prod(label(\"Data\",sort(\"Kind\")),[lit(\"data\")],{})", Factory.Production);
  private static final IConstructor prod__lit___63__char_class___range__63_63_ = (IConstructor) _read("prod(lit(\"?\"),[\\char-class([range(63,63)])],{})", Factory.Production);
  private static final IConstructor prod__Break_Statement__lit_break_layouts_LAYOUTLIST_target_Target_layouts_LAYOUTLIST_lit___59_ = (IConstructor) _read("prod(label(\"Break\",sort(\"Statement\")),[lit(\"break\"),layouts(\"LAYOUTLIST\"),label(\"target\",sort(\"Target\")),layouts(\"LAYOUTLIST\"),lit(\";\")],{})", Factory.Production);
  private static final IConstructor prod__Edit_ShellCommand__lit_edit_layouts_LAYOUTLIST_name_QualifiedName_ = (IConstructor) _read("prod(label(\"Edit\",sort(\"ShellCommand\")),[lit(\"edit\"),layouts(\"LAYOUTLIST\"),label(\"name\",sort(\"QualifiedName\"))],{})", Factory.Production);
  private static final IConstructor regular__opt__char_class___range__43_43_range__45_45 = (IConstructor) _read("regular(opt(\\char-class([range(43,43),range(45,45)])))", Factory.Production);
  private static final IConstructor prod__lit___33_60_60__char_class___range__33_33_char_class___range__60_60_char_class___range__60_60_ = (IConstructor) _read("prod(lit(\"!\\<\\<\"),[\\char-class([range(33,33)]),\\char-class([range(60,60)]),\\char-class([range(60,60)])],{})", Factory.Production);
  private static final IConstructor prod__lit___62__char_class___range__62_62_ = (IConstructor) _read("prod(lit(\"\\>\"),[\\char-class([range(62,62)])],{})", Factory.Production);
  private static final IConstructor prod__Default_ModuleActuals__lit___91_layouts_LAYOUTLIST_types_iter_seps__Type__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___93_ = (IConstructor) _read("prod(label(\"Default\",sort(\"ModuleActuals\")),[lit(\"[\"),layouts(\"LAYOUTLIST\"),label(\"types\",\\iter-seps(sort(\"Type\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\"]\")],{})", Factory.Production);
  private static final IConstructor prod__lit_0__char_class___range__48_48_ = (IConstructor) _read("prod(lit(\"0\"),[\\char-class([range(48,48)])],{})", Factory.Production);
  private static final IConstructor prod__RascalKeywords__lit_anno_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"anno\")],{})", Factory.Production);
  private static final IConstructor prod__RascalKeywords__lit_insert_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"insert\")],{})", Factory.Production);
  private static final IConstructor prod__NoMatch_Expression__pattern_Pattern_layouts_LAYOUTLIST_lit___33_58_61_layouts_LAYOUTLIST_expression_Expression__assoc__non_assoc = (IConstructor) _read("prod(label(\"NoMatch\",sort(\"Expression\")),[label(\"pattern\",sort(\"Pattern\")),layouts(\"LAYOUTLIST\"),lit(\"!:=\"),layouts(\"LAYOUTLIST\"),label(\"expression\",sort(\"Expression\"))],{assoc(\\non-assoc())})", Factory.Production);
  private static final IConstructor prod__AssociativityGroup_Prod__associativity_Assoc_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_group_Prod_layouts_LAYOUTLIST_lit___41__tag__Foldable = (IConstructor) _read("prod(label(\"AssociativityGroup\",sort(\"Prod\")),[label(\"associativity\",sort(\"Assoc\")),layouts(\"LAYOUTLIST\"),lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"group\",sort(\"Prod\")),layouts(\"LAYOUTLIST\"),lit(\")\")],{tag(Foldable())})", Factory.Production);
  private static final IConstructor prod__lit___41__char_class___range__41_41_ = (IConstructor) _read("prod(lit(\")\"),[\\char-class([range(41,41)])],{})", Factory.Production);
  private static final IConstructor prod__lit___40__char_class___range__40_40_ = (IConstructor) _read("prod(lit(\"(\"),[\\char-class([range(40,40)])],{})", Factory.Production);
  private static final IConstructor prod__Conditional_FunctionDeclaration__tags_Tags_layouts_LAYOUTLIST_visibility_Visibility_layouts_LAYOUTLIST_signature_Signature_layouts_LAYOUTLIST_lit___61_layouts_LAYOUTLIST_expression_Expression_layouts_LAYOUTLIST_lit_when_layouts_LAYOUTLIST_conditions_iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___59__tag__Foldable = (IConstructor) _read("prod(label(\"Conditional\",sort(\"FunctionDeclaration\")),[label(\"tags\",sort(\"Tags\")),layouts(\"LAYOUTLIST\"),label(\"visibility\",sort(\"Visibility\")),layouts(\"LAYOUTLIST\"),label(\"signature\",sort(\"Signature\")),layouts(\"LAYOUTLIST\"),lit(\"=\"),layouts(\"LAYOUTLIST\"),label(\"expression\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"when\"),layouts(\"LAYOUTLIST\"),label(\"conditions\",\\iter-seps(sort(\"Expression\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\";\")],{tag(Foldable())})", Factory.Production);
  private static final IConstructor prod__lit___43__char_class___range__43_43_ = (IConstructor) _read("prod(lit(\"+\"),[\\char-class([range(43,43)])],{})", Factory.Production);
  private static final IConstructor prod__RascalKeywords__lit_try_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"try\")],{})", Factory.Production);
  private static final IConstructor prod__lit___42__char_class___range__42_42_ = (IConstructor) _read("prod(lit(\"*\"),[\\char-class([range(42,42)])],{})", Factory.Production);
  private static final IConstructor prod__lit____char_class___range__45_45_ = (IConstructor) _read("prod(lit(\"-\"),[\\char-class([range(45,45)])],{})", Factory.Production);
  private static final IConstructor prod__OctalIntegerLiteral__char_class___range__48_48_conditional__iter__char_class___range__48_55__not_follow__char_class___range__48_57_range__65_90_range__95_95_range__97_122_ = (IConstructor) _read("prod(lex(\"OctalIntegerLiteral\"),[\\char-class([range(48,48)]),conditional(iter(\\char-class([range(48,55)])),{\\not-follow(\\char-class([range(48,57),range(65,90),range(95,95),range(97,122)]))})],{})", Factory.Production);
  private static final IConstructor prod__lit___44__char_class___range__44_44_ = (IConstructor) _read("prod(lit(\",\"),[\\char-class([range(44,44)])],{})", Factory.Production);
  private static final IConstructor prod__Fail_Statement__lit_fail_layouts_LAYOUTLIST_target_Target_layouts_LAYOUTLIST_lit___59_ = (IConstructor) _read("prod(label(\"Fail\",sort(\"Statement\")),[lit(\"fail\"),layouts(\"LAYOUTLIST\"),label(\"target\",sort(\"Target\")),layouts(\"LAYOUTLIST\"),lit(\";\")],{})", Factory.Production);
  private static final IConstructor prod__lit___47__char_class___range__47_47_ = (IConstructor) _read("prod(lit(\"/\"),[\\char-class([range(47,47)])],{})", Factory.Production);
  private static final IConstructor prod__StartOfLine_Sym__lit___94_layouts_LAYOUTLIST_symbol_Sym_ = (IConstructor) _read("prod(label(\"StartOfLine\",sort(\"Sym\")),[lit(\"^\"),layouts(\"LAYOUTLIST\"),label(\"symbol\",sort(\"Sym\"))],{})", Factory.Production);
  private static final IConstructor prod__lit_if__char_class___range__105_105_char_class___range__102_102_ = (IConstructor) _read("prod(lit(\"if\"),[\\char-class([range(105,105)]),\\char-class([range(102,102)])],{})", Factory.Production);
  private static final IConstructor prod__lit___46__char_class___range__46_46_ = (IConstructor) _read("prod(lit(\".\"),[\\char-class([range(46,46)])],{})", Factory.Production);
  private static final IConstructor prod__Expression_FunctionDeclaration__tags_Tags_layouts_LAYOUTLIST_visibility_Visibility_layouts_LAYOUTLIST_signature_Signature_layouts_LAYOUTLIST_lit___61_layouts_LAYOUTLIST_expression_Expression_layouts_LAYOUTLIST_lit___59__tag__Foldable = (IConstructor) _read("prod(label(\"Expression\",sort(\"FunctionDeclaration\")),[label(\"tags\",sort(\"Tags\")),layouts(\"LAYOUTLIST\"),label(\"visibility\",sort(\"Visibility\")),layouts(\"LAYOUTLIST\"),label(\"signature\",sort(\"Signature\")),layouts(\"LAYOUTLIST\"),lit(\"=\"),layouts(\"LAYOUTLIST\"),label(\"expression\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\";\")],{tag(Foldable())})", Factory.Production);
  private static final IConstructor prod__lit___33__char_class___range__33_33_ = (IConstructor) _read("prod(lit(\"!\"),[\\char-class([range(33,33)])],{})", Factory.Production);
  private static final IConstructor prod__lit___35__char_class___range__35_35_ = (IConstructor) _read("prod(lit(\"#\"),[\\char-class([range(35,35)])],{})", Factory.Production);
  private static final IConstructor prod__Default_QualifiedName__conditional__names_iter_seps__Name__layouts_LAYOUTLIST_lit___58_58_layouts_LAYOUTLIST__not_follow__lit___58_58_ = (IConstructor) _read("prod(label(\"Default\",sort(\"QualifiedName\")),[conditional(label(\"names\",\\iter-seps(lex(\"Name\"),[layouts(\"LAYOUTLIST\"),lit(\"::\"),layouts(\"LAYOUTLIST\")])),{\\not-follow(lit(\"::\"))})],{})", Factory.Production);
  private static final IConstructor prod__lit___34__char_class___range__34_34_ = (IConstructor) _read("prod(lit(\"\\\"\"),[\\char-class([range(34,34)])],{})", Factory.Production);
  private static final IConstructor prod__lit___37__char_class___range__37_37_ = (IConstructor) _read("prod(lit(\"%\"),[\\char-class([range(37,37)])],{})", Factory.Production);
  private static final IConstructor prod__lit___36__char_class___range__36_36_ = (IConstructor) _read("prod(lit(\"$\"),[\\char-class([range(36,36)])],{})", Factory.Production);
  private static final IConstructor prod__lit_return__char_class___range__114_114_char_class___range__101_101_char_class___range__116_116_char_class___range__117_117_char_class___range__114_114_char_class___range__110_110_ = (IConstructor) _read("prod(lit(\"return\"),[\\char-class([range(114,114)]),\\char-class([range(101,101)]),\\char-class([range(116,116)]),\\char-class([range(117,117)]),\\char-class([range(114,114)]),\\char-class([range(110,110)])],{})", Factory.Production);
  private static final IConstructor prod__lit___39__char_class___range__39_39_ = (IConstructor) _read("prod(lit(\"\\'\\\\\"),[\\char-class([range(39,39)])],{})", Factory.Production);
  private static final IConstructor prod__lit_datetime__char_class___range__100_100_char_class___range__97_97_char_class___range__116_116_char_class___range__101_101_char_class___range__116_116_char_class___range__105_105_char_class___range__109_109_char_class___range__101_101_ = (IConstructor) _read("prod(lit(\"datetime\"),[\\char-class([range(100,100)]),\\char-class([range(97,97)]),\\char-class([range(116,116)]),\\char-class([range(101,101)]),\\char-class([range(116,116)]),\\char-class([range(105,105)]),\\char-class([range(109,109)]),\\char-class([range(101,101)])],{})", Factory.Production);
  private static final IConstructor prod__lit___38__char_class___range__38_38_ = (IConstructor) _read("prod(lit(\"&\"),[\\char-class([range(38,38)])],{})", Factory.Production);
  private static final IConstructor prod__Comprehension_Expression__comprehension_Comprehension_ = (IConstructor) _read("prod(label(\"Comprehension\",sort(\"Expression\")),[label(\"comprehension\",sort(\"Comprehension\"))],{})", Factory.Production);
  private static final IConstructor prod__Associativity_ProdModifier__associativity_Assoc_ = (IConstructor) _read("prod(label(\"Associativity\",sort(\"ProdModifier\")),[label(\"associativity\",sort(\"Assoc\"))],{})", Factory.Production);
  private static final IConstructor prod__Bracket_Class__lit___40_layouts_LAYOUTLIST_charclass_Class_layouts_LAYOUTLIST_lit___41__bracket = (IConstructor) _read("prod(label(\"Bracket\",sort(\"Class\")),[lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"charclass\",sort(\"Class\")),layouts(\"LAYOUTLIST\"),lit(\")\")],{bracket()})", Factory.Production);
  private static final IConstructor regular__seq___lit___92_char_class___range__123_123_range__125_125 = (IConstructor) _read("regular(seq([lit(\"\\\\\"),\\char-class([range(123,123),range(125,125)])]))", Factory.Production);
  private static final IConstructor prod__NonInterpolated_ProtocolPart__protocolChars_ProtocolChars_ = (IConstructor) _read("prod(label(\"NonInterpolated\",sort(\"ProtocolPart\")),[label(\"protocolChars\",lex(\"ProtocolChars\"))],{})", Factory.Production);
  private static final IConstructor prod__lit_Z__char_class___range__90_90_ = (IConstructor) _read("prod(lit(\"Z\"),[\\char-class([range(90,90)])],{})", Factory.Production);
  private static final IConstructor prod__lit___91__char_class___range__91_91_ = (IConstructor) _read("prod(lit(\"[\"),[\\char-class([range(91,91)])],{})", Factory.Production);
  private static final IConstructor prod__Free_TypeVar__lit___38_layouts_LAYOUTLIST_name_Name_ = (IConstructor) _read("prod(label(\"Free\",sort(\"TypeVar\")),[lit(\"&\"),layouts(\"LAYOUTLIST\"),label(\"name\",lex(\"Name\"))],{})", Factory.Production);
  private static final IConstructor prod__RascalKeywords__lit_self_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"self\")],{})", Factory.Production);
  private static final IConstructor prod__lit___94__char_class___range__94_94_ = (IConstructor) _read("prod(lit(\"^\"),[\\char-class([range(94,94)])],{})", Factory.Production);
  private static final IConstructor prod__RascalKeywords__lit_append_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"append\")],{})", Factory.Production);
  private static final IConstructor prod__lit___92__char_class___range__92_92_ = (IConstructor) _read("prod(lit(\"\\\\\"),[\\char-class([range(92,92)])],{})", Factory.Production);
  private static final IConstructor prod__RascalKeywords__lit_notin_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"notin\")],{})", Factory.Production);
  private static final IConstructor prod__lit___93__char_class___range__93_93_ = (IConstructor) _read("prod(lit(\"]\"),[\\char-class([range(93,93)])],{})", Factory.Production);
  private static final IConstructor prod__Anno_Kind__lit_anno_ = (IConstructor) _read("prod(label(\"Anno\",sort(\"Kind\")),[lit(\"anno\")],{})", Factory.Production);
  private static final IConstructor prod__lit_mod__char_class___range__109_109_char_class___range__111_111_char_class___range__100_100_ = (IConstructor) _read("prod(lit(\"mod\"),[\\char-class([range(109,109)]),\\char-class([range(111,111)]),\\char-class([range(100,100)])],{})", Factory.Production);
  private static final IConstructor prod__Help_ShellCommand__lit_help_ = (IConstructor) _read("prod(label(\"Help\",sort(\"ShellCommand\")),[lit(\"help\")],{})", Factory.Production);
  private static final IConstructor prod__lit_T__char_class___range__84_84_ = (IConstructor) _read("prod(lit(\"T\"),[\\char-class([range(84,84)])],{})", Factory.Production);
  private static final IConstructor prod__Set_Expression__lit___123_layouts_LAYOUTLIST_elements_iter_star_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_ = (IConstructor) _read("prod(label(\"Set\",sort(\"Expression\")),[lit(\"{\"),layouts(\"LAYOUTLIST\"),label(\"elements\",\\iter-star-seps(sort(\"Expression\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\"}\")],{})", Factory.Production);
  private static final IConstructor prod__lit_alias__char_class___range__97_97_char_class___range__108_108_char_class___range__105_105_char_class___range__97_97_char_class___range__115_115_ = (IConstructor) _read("prod(lit(\"alias\"),[\\char-class([range(97,97)]),\\char-class([range(108,108)]),\\char-class([range(105,105)]),\\char-class([range(97,97)]),\\char-class([range(115,115)])],{})", Factory.Production);
  private static final IConstructor prod__GivenVisibility_Toplevel__declaration_Declaration_ = (IConstructor) _read("prod(label(\"GivenVisibility\",sort(\"Toplevel\")),[label(\"declaration\",sort(\"Declaration\"))],{})", Factory.Production);
  private static final IConstructor prod__RascalKeywords__lit_visit_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"visit\")],{})", Factory.Production);
  private static final IConstructor regular__iter_star__char_class___range__9_9_range__32_32 = (IConstructor) _read("regular(\\iter-star(\\char-class([range(9,9),range(32,32)])))", Factory.Production);
  private static final IConstructor prod__IfThen_Statement__label_Label_layouts_LAYOUTLIST_lit_if_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_conditions_iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_layouts_LAYOUTLIST_thenStatement_Statement_layouts_LAYOUTLIST_conditional__empty__not_follow__lit_else_ = (IConstructor) _read("prod(label(\"IfThen\",sort(\"Statement\")),[label(\"label\",sort(\"Label\")),layouts(\"LAYOUTLIST\"),lit(\"if\"),layouts(\"LAYOUTLIST\"),lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"conditions\",\\iter-seps(sort(\"Expression\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\")\"),layouts(\"LAYOUTLIST\"),label(\"thenStatement\",sort(\"Statement\")),layouts(\"LAYOUTLIST\"),conditional(empty(),{\\not-follow(lit(\"else\"))})],{})", Factory.Production);
  private static final IConstructor prod__Intersection_Expression__lhs_Expression_layouts_LAYOUTLIST_lit___38_layouts_LAYOUTLIST_rhs_Expression__assoc__left = (IConstructor) _read("prod(label(\"Intersection\",sort(\"Expression\")),[label(\"lhs\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"&\"),layouts(\"LAYOUTLIST\"),label(\"rhs\",sort(\"Expression\"))],{assoc(left())})", Factory.Production);
  private static final IConstructor prod__Private_Visibility__lit_private_ = (IConstructor) _read("prod(label(\"Private\",sort(\"Visibility\")),[lit(\"private\")],{})", Factory.Production);
  private static final IConstructor prod__Annotation_Declaration__tags_Tags_layouts_LAYOUTLIST_visibility_Visibility_layouts_LAYOUTLIST_lit_anno_layouts_LAYOUTLIST_annoType_Type_layouts_LAYOUTLIST_onType_Type_layouts_LAYOUTLIST_lit___64_layouts_LAYOUTLIST_name_Name_layouts_LAYOUTLIST_lit___59_ = (IConstructor) _read("prod(label(\"Annotation\",sort(\"Declaration\")),[label(\"tags\",sort(\"Tags\")),layouts(\"LAYOUTLIST\"),label(\"visibility\",sort(\"Visibility\")),layouts(\"LAYOUTLIST\"),lit(\"anno\"),layouts(\"LAYOUTLIST\"),label(\"annoType\",sort(\"Type\")),layouts(\"LAYOUTLIST\"),label(\"onType\",sort(\"Type\")),layouts(\"LAYOUTLIST\"),lit(\"@\"),layouts(\"LAYOUTLIST\"),label(\"name\",lex(\"Name\")),layouts(\"LAYOUTLIST\"),lit(\";\")],{})", Factory.Production);
  private static final IConstructor prod__Mid_ProtocolTail__mid_MidProtocolChars_layouts_LAYOUTLIST_expression_Expression_layouts_LAYOUTLIST_tail_ProtocolTail_ = (IConstructor) _read("prod(label(\"Mid\",sort(\"ProtocolTail\")),[label(\"mid\",lex(\"MidProtocolChars\")),layouts(\"LAYOUTLIST\"),label(\"expression\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),label(\"tail\",sort(\"ProtocolTail\"))],{})", Factory.Production);
  private static final IConstructor prod__lit___64__char_class___range__64_64_ = (IConstructor) _read("prod(lit(\"@\"),[\\char-class([range(64,64)])],{})", Factory.Production);
  private static final IConstructor prod__Default_Tags__tags_iter_star_seps__Tag__layouts_LAYOUTLIST_ = (IConstructor) _read("prod(label(\"Default\",sort(\"Tags\")),[label(\"tags\",\\iter-star-seps(sort(\"Tag\"),[layouts(\"LAYOUTLIST\")]))],{})", Factory.Production);
  private static final IConstructor prod__lit___43_61__char_class___range__43_43_char_class___range__61_61_ = (IConstructor) _read("prod(lit(\"+=\"),[\\char-class([range(43,43)]),\\char-class([range(61,61)])],{})", Factory.Production);
  private static final IConstructor prod__lit___123__char_class___range__123_123_ = (IConstructor) _read("prod(lit(\"{\"),[\\char-class([range(123,123)])],{})", Factory.Production);
  private static final IConstructor prod__Associative_Assoc__lit_assoc_ = (IConstructor) _read("prod(label(\"Associative\",sort(\"Assoc\")),[lit(\"assoc\")],{})", Factory.Production);
  private static final IConstructor prod__lit___33_58_61__char_class___range__33_33_char_class___range__58_58_char_class___range__61_61_ = (IConstructor) _read("prod(lit(\"!:=\"),[\\char-class([range(33,33)]),\\char-class([range(58,58)]),\\char-class([range(61,61)])],{})", Factory.Production);
  private static final IConstructor prod__Comment__lit___47_47_conditional__iter_star__char_class___range__0_9_range__11_16777215__not_follow__char_class___range__9_9_range__13_13_range__32_32_end_of_line__tag__category___67_111_109_109_101_110_116 = (IConstructor) _read("prod(lex(\"Comment\"),[lit(\"//\"),conditional(\\iter-star(\\char-class([range(0,9),range(11,16777215)])),{\\not-follow(\\char-class([range(9,9),range(13,13),range(32,32)])),\\end-of-line()})],{tag(category(\"Comment\"))})", Factory.Production);
  private static final IConstructor prod__Bag_BasicType__lit_bag_ = (IConstructor) _read("prod(label(\"Bag\",sort(\"BasicType\")),[lit(\"bag\")],{})", Factory.Production);
  private static final IConstructor prod__SplicePlus_Pattern__lit___43_layouts_LAYOUTLIST_argument_Pattern_ = (IConstructor) _read("prod(label(\"SplicePlus\",sort(\"Pattern\")),[lit(\"+\"),layouts(\"LAYOUTLIST\"),label(\"argument\",sort(\"Pattern\"))],{})", Factory.Production);
  private static final IConstructor prod__RascalKeywords__lit_filter_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"filter\")],{})", Factory.Production);
  private static final IConstructor prod__lit___125__char_class___range__125_125_ = (IConstructor) _read("prod(lit(\"}\"),[\\char-class([range(125,125)])],{})", Factory.Production);
  private static final IConstructor prod__RascalKeywords__lit_while_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"while\")],{})", Factory.Production);
  private static final IConstructor prod__lit___124__char_class___range__124_124_ = (IConstructor) _read("prod(lit(\"|\"),[\\char-class([range(124,124)])],{})", Factory.Production);
  private static final IConstructor prod__lit___42_47__char_class___range__42_42_char_class___range__47_47_ = (IConstructor) _read("prod(lit(\"*/\"),[\\char-class([range(42,42)]),\\char-class([range(47,47)])],{})", Factory.Production);
  private static final IConstructor prod__TypedVariable_Pattern__type_Type_layouts_LAYOUTLIST_name_Name_ = (IConstructor) _read("prod(label(\"TypedVariable\",sort(\"Pattern\")),[label(\"type\",sort(\"Type\")),layouts(\"LAYOUTLIST\"),label(\"name\",lex(\"Name\"))],{})", Factory.Production);
  private static final IConstructor prod__QualifiedName_Expression__qualifiedName_QualifiedName_ = (IConstructor) _read("prod(label(\"QualifiedName\",sort(\"Expression\")),[label(\"qualifiedName\",sort(\"QualifiedName\"))],{})", Factory.Production);
  private static final IConstructor prod__Conditional_Replacement__replacementExpression_Expression_layouts_LAYOUTLIST_lit_when_layouts_LAYOUTLIST_conditions_iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_ = (IConstructor) _read("prod(label(\"Conditional\",sort(\"Replacement\")),[label(\"replacementExpression\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"when\"),layouts(\"LAYOUTLIST\"),label(\"conditions\",\\iter-seps(sort(\"Expression\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")]))],{})", Factory.Production);
  private static final IConstructor regular__alt___TagString_char_class___range__0_122_range__124_124_range__126_16777215_seq___lit___92_char_class___range__123_123_range__125_125 = (IConstructor) _read("regular(alt({lex(\"TagString\"),\\char-class([range(0,122),range(124,124),range(126,16777215)]),seq([lit(\"\\\\\"),\\char-class([range(123,123),range(125,125)])])}))", Factory.Production);
  private static final IConstructor prod__OpenRecursion_FunctionModifier__lit_openrec_ = (IConstructor) _read("prod(label(\"OpenRecursion\",sort(\"FunctionModifier\")),[lit(\"openrec\")],{})", Factory.Production);
  private static final IConstructor prod__Index_Field__fieldIndex_IntegerLiteral_ = (IConstructor) _read("prod(label(\"Index\",sort(\"Field\")),[label(\"fieldIndex\",sort(\"IntegerLiteral\"))],{})", Factory.Production);
  private static final IConstructor prod__RascalKeywords__lit_finally_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"finally\")],{})", Factory.Production);
  private static final IConstructor prod__Implication_Expression__lhs_Expression_layouts_LAYOUTLIST_lit___61_61_62_layouts_LAYOUTLIST_rhs_Expression__assoc__non_assoc = (IConstructor) _read("prod(label(\"Implication\",sort(\"Expression\")),[label(\"lhs\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"==\\>\"),layouts(\"LAYOUTLIST\"),label(\"rhs\",sort(\"Expression\"))],{assoc(\\non-assoc())})", Factory.Production);
  private static final IConstructor prod__Labeled_Prod__modifiers_iter_star_seps__ProdModifier__layouts_LAYOUTLIST_layouts_LAYOUTLIST_name_Name_layouts_LAYOUTLIST_lit___58_layouts_LAYOUTLIST_args_iter_star_seps__Sym__layouts_LAYOUTLIST_ = (IConstructor) _read("prod(label(\"Labeled\",sort(\"Prod\")),[label(\"modifiers\",\\iter-star-seps(sort(\"ProdModifier\"),[layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),label(\"name\",lex(\"Name\")),layouts(\"LAYOUTLIST\"),lit(\":\"),layouts(\"LAYOUTLIST\"),label(\"args\",\\iter-star-seps(sort(\"Sym\"),[layouts(\"LAYOUTLIST\")]))],{})", Factory.Production);
  private static final IConstructor prod__lit_o__char_class___range__111_111_ = (IConstructor) _read("prod(lit(\"o\"),[\\char-class([range(111,111)])],{})", Factory.Production);
  private static final IConstructor prod__DateAndTime__lit___36_DatePart_lit_T_conditional__TimePartNoTZ__not_follow__char_class___range__43_43_range__45_45_ = (IConstructor) _read("prod(lex(\"DateAndTime\"),[lit(\"$\"),lex(\"DatePart\"),lit(\"T\"),conditional(lex(\"TimePartNoTZ\"),{\\not-follow(\\char-class([range(43,43),range(45,45)]))})],{})", Factory.Production);
  private static final IConstructor prod__MidStringChars__char_class___range__62_62_iter_star__StringCharacter_char_class___range__60_60__tag__category___67_111_110_115_116_97_110_116 = (IConstructor) _read("prod(lex(\"MidStringChars\"),[\\char-class([range(62,62)]),\\iter-star(lex(\"StringCharacter\")),\\char-class([range(60,60)])],{tag(category(\"Constant\"))})", Factory.Production);
  private static final IConstructor prod__Expression_Tag__lit___64_layouts_LAYOUTLIST_name_Name_layouts_LAYOUTLIST_lit___61_layouts_LAYOUTLIST_expression_Expression__tag__Folded_tag__category___67_111_109_109_101_110_116 = (IConstructor) _read("prod(label(\"Expression\",sort(\"Tag\")),[lit(\"@\"),layouts(\"LAYOUTLIST\"),label(\"name\",lex(\"Name\")),layouts(\"LAYOUTLIST\"),lit(\"=\"),layouts(\"LAYOUTLIST\"),label(\"expression\",sort(\"Expression\"))],{tag(Folded()),tag(category(\"Comment\"))})", Factory.Production);
  private static final IConstructor prod__Intersection_Assignment__lit___38_61_ = (IConstructor) _read("prod(label(\"Intersection\",sort(\"Assignment\")),[lit(\"&=\")],{})", Factory.Production);
  private static final IConstructor prod__Labeled_DataTarget__label_Name_layouts_LAYOUTLIST_lit___58_ = (IConstructor) _read("prod(label(\"Labeled\",sort(\"DataTarget\")),[label(\"label\",lex(\"Name\")),layouts(\"LAYOUTLIST\"),lit(\":\")],{})", Factory.Production);
  private static final IConstructor prod__lit_unimport__char_class___range__117_117_char_class___range__110_110_char_class___range__105_105_char_class___range__109_109_char_class___range__112_112_char_class___range__111_111_char_class___range__114_114_char_class___range__116_116_ = (IConstructor) _read("prod(lit(\"unimport\"),[\\char-class([range(117,117)]),\\char-class([range(110,110)]),\\char-class([range(105,105)]),\\char-class([range(109,109)]),\\char-class([range(112,112)]),\\char-class([range(111,111)]),\\char-class([range(114,114)]),\\char-class([range(116,116)])],{})", Factory.Production);
  private static final IConstructor prod__Map_BasicType__lit_map_ = (IConstructor) _read("prod(label(\"Map\",sort(\"BasicType\")),[lit(\"map\")],{})", Factory.Production);
  private static final IConstructor prod__RegExpModifier__iter_star__char_class___range__100_100_range__105_105_range__109_109_range__115_115_ = (IConstructor) _read("prod(lex(\"RegExpModifier\"),[\\iter-star(\\char-class([range(100,100),range(105,105),range(109,109),range(115,115)]))],{})", Factory.Production);
  private static final IConstructor prod__lit___42_61__char_class___range__42_42_char_class___range__61_61_ = (IConstructor) _read("prod(lit(\"*=\"),[\\char-class([range(42,42)]),\\char-class([range(61,61)])],{})", Factory.Production);
  private static final IConstructor prod__Range_Expression__lit___91_layouts_LAYOUTLIST_first_Expression_layouts_LAYOUTLIST_lit___46_46_layouts_LAYOUTLIST_last_Expression_layouts_LAYOUTLIST_lit___93_ = (IConstructor) _read("prod(label(\"Range\",sort(\"Expression\")),[lit(\"[\"),layouts(\"LAYOUTLIST\"),label(\"first\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"..\"),layouts(\"LAYOUTLIST\"),label(\"last\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"]\")],{})", Factory.Production);
  private static final IConstructor prod__Comment__lit___47_42_iter_star__alt___conditional__char_class___range__42_42__not_follow__char_class___range__47_47_char_class___range__0_41_range__43_16777215_lit___42_47__tag__category___67_111_109_109_101_110_116 = (IConstructor) _read("prod(lex(\"Comment\"),[lit(\"/*\"),\\iter-star(alt({conditional(\\char-class([range(42,42)]),{\\not-follow(\\char-class([range(47,47)]))}),\\char-class([range(0,41),range(43,16777215)])})),lit(\"*/\")],{tag(category(\"Comment\"))})", Factory.Production);
  private static final IConstructor prod__TimeZonePart__char_class___range__43_43_range__45_45_char_class___range__48_49_char_class___range__48_57_char_class___range__48_53_char_class___range__48_57_ = (IConstructor) _read("prod(lex(\"TimeZonePart\"),[\\char-class([range(43,43),range(45,45)]),\\char-class([range(48,49)]),\\char-class([range(48,57)]),\\char-class([range(48,53)]),\\char-class([range(48,57)])],{})", Factory.Production);
  private static final IConstructor prod__lit_edit__char_class___range__101_101_char_class___range__100_100_char_class___range__105_105_char_class___range__116_116_ = (IConstructor) _read("prod(lit(\"edit\"),[\\char-class([range(101,101)]),\\char-class([range(100,100)]),\\char-class([range(105,105)]),\\char-class([range(116,116)])],{})", Factory.Production);
  private static final IConstructor regular__alt___conditional__char_class___range__42_42__not_follow__char_class___range__47_47_char_class___range__0_41_range__43_16777215 = (IConstructor) _read("regular(alt({conditional(\\char-class([range(42,42)]),{\\not-follow(\\char-class([range(47,47)]))}),\\char-class([range(0,41),range(43,16777215)])}))", Factory.Production);
  private static final IConstructor prod__Variable_Declaration__tags_Tags_layouts_LAYOUTLIST_visibility_Visibility_layouts_LAYOUTLIST_type_Type_layouts_LAYOUTLIST_variables_iter_seps__Variable__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___59_ = (IConstructor) _read("prod(label(\"Variable\",sort(\"Declaration\")),[label(\"tags\",sort(\"Tags\")),layouts(\"LAYOUTLIST\"),label(\"visibility\",sort(\"Visibility\")),layouts(\"LAYOUTLIST\"),label(\"type\",sort(\"Type\")),layouts(\"LAYOUTLIST\"),label(\"variables\",\\iter-seps(sort(\"Variable\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\";\")],{})", Factory.Production);
  private static final IConstructor prod__RegExp__lit___60_Name_lit___62_ = (IConstructor) _read("prod(lex(\"RegExp\"),[lit(\"\\<\"),lex(\"Name\"),lit(\"\\>\")],{})", Factory.Production);
  private static final IConstructor prod__DoWhile_StringTemplate__lit_do_layouts_LAYOUTLIST_lit___123_layouts_LAYOUTLIST_preStats_iter_star_seps__Statement__layouts_LAYOUTLIST_layouts_LAYOUTLIST_body_StringMiddle_layouts_LAYOUTLIST_postStats_iter_star_seps__Statement__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_layouts_LAYOUTLIST_lit_while_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_condition_Expression_layouts_LAYOUTLIST_lit___41_ = (IConstructor) _read("prod(label(\"DoWhile\",sort(\"StringTemplate\")),[lit(\"do\"),layouts(\"LAYOUTLIST\"),lit(\"{\"),layouts(\"LAYOUTLIST\"),label(\"preStats\",\\iter-star-seps(sort(\"Statement\"),[layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),label(\"body\",sort(\"StringMiddle\")),layouts(\"LAYOUTLIST\"),label(\"postStats\",\\iter-star-seps(sort(\"Statement\"),[layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\"}\"),layouts(\"LAYOUTLIST\"),lit(\"while\"),layouts(\"LAYOUTLIST\"),lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"condition\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\")\")],{})", Factory.Production);
  private static final IConstructor prod__IfThenElse_StringTemplate__lit_if_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_conditions_iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_layouts_LAYOUTLIST_lit___123_layouts_LAYOUTLIST_preStatsThen_iter_star_seps__Statement__layouts_LAYOUTLIST_layouts_LAYOUTLIST_thenString_StringMiddle_layouts_LAYOUTLIST_postStatsThen_iter_star_seps__Statement__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_layouts_LAYOUTLIST_lit_else_layouts_LAYOUTLIST_lit___123_layouts_LAYOUTLIST_preStatsElse_iter_star_seps__Statement__layouts_LAYOUTLIST_layouts_LAYOUTLIST_elseString_StringMiddle_layouts_LAYOUTLIST_postStatsElse_iter_star_seps__Statement__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_ = (IConstructor) _read("prod(label(\"IfThenElse\",sort(\"StringTemplate\")),[lit(\"if\"),layouts(\"LAYOUTLIST\"),lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"conditions\",\\iter-seps(sort(\"Expression\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\")\"),layouts(\"LAYOUTLIST\"),lit(\"{\"),layouts(\"LAYOUTLIST\"),label(\"preStatsThen\",\\iter-star-seps(sort(\"Statement\"),[layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),label(\"thenString\",sort(\"StringMiddle\")),layouts(\"LAYOUTLIST\"),label(\"postStatsThen\",\\iter-star-seps(sort(\"Statement\"),[layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\"}\"),layouts(\"LAYOUTLIST\"),lit(\"else\"),layouts(\"LAYOUTLIST\"),lit(\"{\"),layouts(\"LAYOUTLIST\"),label(\"preStatsElse\",\\iter-star-seps(sort(\"Statement\"),[layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),label(\"elseString\",sort(\"StringMiddle\")),layouts(\"LAYOUTLIST\"),label(\"postStatsElse\",\\iter-star-seps(sort(\"Statement\"),[layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\"}\")],{})", Factory.Production);
  private static final IConstructor prod__RascalKeywords__lit_openrec_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"openrec\")],{})", Factory.Production);
  private static final IConstructor prod__RationalLiteral__char_class___range__48_57_iter_star__char_class___range__48_57_char_class___range__114_114_ = (IConstructor) _read("prod(lex(\"RationalLiteral\"),[\\char-class([range(48,57)]),\\iter-star(\\char-class([range(48,57)])),\\char-class([range(114,114)])],{})", Factory.Production);
  private static final IConstructor prod__lit_node__char_class___range__110_110_char_class___range__111_111_char_class___range__100_100_char_class___range__101_101_ = (IConstructor) _read("prod(lit(\"node\"),[\\char-class([range(110,110)]),\\char-class([range(111,111)]),\\char-class([range(100,100)]),\\char-class([range(101,101)])],{})", Factory.Production);
  private static final IConstructor prod__TimeZonePart__char_class___range__43_43_range__45_45_char_class___range__48_49_char_class___range__48_57_lit___58_char_class___range__48_53_char_class___range__48_57_ = (IConstructor) _read("prod(lex(\"TimeZonePart\"),[\\char-class([range(43,43),range(45,45)]),\\char-class([range(48,49)]),\\char-class([range(48,57)]),lit(\":\"),\\char-class([range(48,53)]),\\char-class([range(48,57)])],{})", Factory.Production);
  private static final IConstructor regular__iter_seps__Name__layouts_LAYOUTLIST_lit___58_58_layouts_LAYOUTLIST = (IConstructor) _read("regular(\\iter-seps(lex(\"Name\"),[layouts(\"LAYOUTLIST\"),lit(\"::\"),layouts(\"LAYOUTLIST\")]))", Factory.Production);
  private static final IConstructor prod__RascalKeywords__lit_true_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"true\")],{})", Factory.Production);
  private static final IConstructor prod__Product_Expression__lhs_Expression_layouts_LAYOUTLIST_lit___42_layouts_LAYOUTLIST_conditional__empty__not_follow__lit___42_layouts_LAYOUTLIST_rhs_Expression__assoc__left = (IConstructor) _read("prod(label(\"Product\",sort(\"Expression\")),[label(\"lhs\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"*\"),layouts(\"LAYOUTLIST\"),conditional(empty(),{\\not-follow(lit(\"*\"))}),layouts(\"LAYOUTLIST\"),label(\"rhs\",sort(\"Expression\"))],{assoc(left())})", Factory.Production);
  private static final IConstructor prod__Solve_Statement__lit_solve_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_variables_iter_seps__QualifiedName__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_bound_Bound_layouts_LAYOUTLIST_lit___41_layouts_LAYOUTLIST_body_Statement_ = (IConstructor) _read("prod(label(\"Solve\",sort(\"Statement\")),[lit(\"solve\"),layouts(\"LAYOUTLIST\"),lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"variables\",\\iter-seps(sort(\"QualifiedName\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),label(\"bound\",sort(\"Bound\")),layouts(\"LAYOUTLIST\"),lit(\")\"),layouts(\"LAYOUTLIST\"),label(\"body\",sort(\"Statement\"))],{})", Factory.Production);
  private static final IConstructor prod__Visit_Expression__label_Label_layouts_LAYOUTLIST_visit_Visit_ = (IConstructor) _read("prod(label(\"Visit\",sort(\"Expression\")),[label(\"label\",sort(\"Label\")),layouts(\"LAYOUTLIST\"),label(\"visit\",sort(\"Visit\"))],{})", Factory.Production);
  private static final IConstructor prod__Actuals_ImportedModule__name_QualifiedName_layouts_LAYOUTLIST_actuals_ModuleActuals_ = (IConstructor) _read("prod(label(\"Actuals\",sort(\"ImportedModule\")),[label(\"name\",sort(\"QualifiedName\")),layouts(\"LAYOUTLIST\"),label(\"actuals\",sort(\"ModuleActuals\"))],{})", Factory.Production);
  private static final IConstructor prod__StringCharacter__char_class___range__0_33_range__35_38_range__40_59_range__61_61_range__63_91_range__93_16777215_ = (IConstructor) _read("prod(lex(\"StringCharacter\"),[\\char-class([range(0,33),range(35,38),range(40,59),range(61,61),range(63,91),range(93,16777215)])],{})", Factory.Production);
  private static final IConstructor regular__iter__char_class___range__48_57_range__65_70_range__97_102 = (IConstructor) _read("regular(iter(\\char-class([range(48,57),range(65,70),range(97,102)])))", Factory.Production);
  private static final IConstructor prod__lit___36_84__char_class___range__36_36_char_class___range__84_84_ = (IConstructor) _read("prod(lit(\"$T\"),[\\char-class([range(36,36)]),\\char-class([range(84,84)])],{})", Factory.Production);
  private static final IConstructor prod__lit_do__char_class___range__100_100_char_class___range__111_111_ = (IConstructor) _read("prod(lit(\"do\"),[\\char-class([range(100,100)]),\\char-class([range(111,111)])],{})", Factory.Production);
  private static final IConstructor prod__RascalKeywords__lit_import_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"import\")],{})", Factory.Production);
  private static final IConstructor prod__RascalKeywords__lit_assert_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"assert\")],{})", Factory.Production);
  private static final IConstructor prod__Default_Mapping__Expression__from_Expression_layouts_LAYOUTLIST_lit___58_layouts_LAYOUTLIST_to_Expression_ = (IConstructor) _read("prod(label(\"Default\",\\parameterized-sort(\"Mapping\",[sort(\"Expression\")])),[label(\"from\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\":\"),layouts(\"LAYOUTLIST\"),label(\"to\",sort(\"Expression\"))],{})", Factory.Production);
  private static final IConstructor prod__RegExp_Literal__regExpLiteral_RegExpLiteral_ = (IConstructor) _read("prod(label(\"RegExp\",sort(\"Literal\")),[label(\"regExpLiteral\",lex(\"RegExpLiteral\"))],{})", Factory.Production);
  private static final IConstructor prod__Rational_Literal__rationalLiteral_RationalLiteral_ = (IConstructor) _read("prod(label(\"Rational\",sort(\"Literal\")),[label(\"rationalLiteral\",lex(\"RationalLiteral\"))],{})", Factory.Production);
  private static final IConstructor prod__String_BasicType__lit_str_ = (IConstructor) _read("prod(label(\"String\",sort(\"BasicType\")),[lit(\"str\")],{})", Factory.Production);
  private static final IConstructor prod__lit_java__char_class___range__106_106_char_class___range__97_97_char_class___range__118_118_char_class___range__97_97_ = (IConstructor) _read("prod(lit(\"java\"),[\\char-class([range(106,106)]),\\char-class([range(97,97)]),\\char-class([range(118,118)]),\\char-class([range(97,97)])],{})", Factory.Production);
  private static final IConstructor prod__Optional_Sym__symbol_Sym_layouts_LAYOUTLIST_lit___63_ = (IConstructor) _read("prod(label(\"Optional\",sort(\"Sym\")),[label(\"symbol\",sort(\"Sym\")),layouts(\"LAYOUTLIST\"),lit(\"?\")],{})", Factory.Production);
  private static final IConstructor prod__Bracket_Type__lit___40_layouts_LAYOUTLIST_type_Type_layouts_LAYOUTLIST_lit___41__bracket = (IConstructor) _read("prod(label(\"Bracket\",sort(\"Type\")),[lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"type\",sort(\"Type\")),layouts(\"LAYOUTLIST\"),lit(\")\")],{bracket()})", Factory.Production);
  private static final IConstructor prod__InsertBefore_Expression__lhs_Expression_layouts_LAYOUTLIST_lit___62_62_layouts_LAYOUTLIST_rhs_Expression__assoc__left = (IConstructor) _read("prod(label(\"InsertBefore\",sort(\"Expression\")),[label(\"lhs\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"\\>\\>\"),layouts(\"LAYOUTLIST\"),label(\"rhs\",sort(\"Expression\"))],{assoc(left())})", Factory.Production);
  private static final IConstructor prod__IterSep_Sym__lit___123_layouts_LAYOUTLIST_symbol_Sym_layouts_LAYOUTLIST_sep_Sym_layouts_LAYOUTLIST_lit___125_layouts_LAYOUTLIST_lit___43_ = (IConstructor) _read("prod(label(\"IterSep\",sort(\"Sym\")),[lit(\"{\"),layouts(\"LAYOUTLIST\"),label(\"symbol\",sort(\"Sym\")),layouts(\"LAYOUTLIST\"),label(\"sep\",sort(\"Sym\")),layouts(\"LAYOUTLIST\"),lit(\"}\"),layouts(\"LAYOUTLIST\"),lit(\"+\")],{})", Factory.Production);
  private static final IConstructor prod__Value_BasicType__lit_value_ = (IConstructor) _read("prod(label(\"Value\",sort(\"BasicType\")),[lit(\"value\")],{})", Factory.Production);
  private static final IConstructor prod__Difference_Class__lhs_Class_layouts_LAYOUTLIST_lit___layouts_LAYOUTLIST_rhs_Class__assoc__left = (IConstructor) _read("prod(label(\"Difference\",sort(\"Class\")),[label(\"lhs\",sort(\"Class\")),layouts(\"LAYOUTLIST\"),lit(\"-\"),layouts(\"LAYOUTLIST\"),label(\"rhs\",sort(\"Class\"))],{assoc(left())})", Factory.Production);
  private static final IConstructor prod__Constructor_Assignable__name_Name_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_arguments_iter_seps__Assignable__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_ = (IConstructor) _read("prod(label(\"Constructor\",sort(\"Assignable\")),[label(\"name\",lex(\"Name\")),layouts(\"LAYOUTLIST\"),lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"arguments\",\\iter-seps(sort(\"Assignable\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\")\")],{})", Factory.Production);
  private static final IConstructor prod__CallOrTree_Pattern__expression_Pattern_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_arguments_iter_star_seps__Pattern__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_ = (IConstructor) _read("prod(label(\"CallOrTree\",sort(\"Pattern\")),[label(\"expression\",sort(\"Pattern\")),layouts(\"LAYOUTLIST\"),lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"arguments\",\\iter-star-seps(sort(\"Pattern\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\")\")],{})", Factory.Production);
  private static final IConstructor prod__IfThen_StringTemplate__lit_if_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_conditions_iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_layouts_LAYOUTLIST_lit___123_layouts_LAYOUTLIST_preStats_iter_star_seps__Statement__layouts_LAYOUTLIST_layouts_LAYOUTLIST_body_StringMiddle_layouts_LAYOUTLIST_postStats_iter_star_seps__Statement__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_ = (IConstructor) _read("prod(label(\"IfThen\",sort(\"StringTemplate\")),[lit(\"if\"),layouts(\"LAYOUTLIST\"),lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"conditions\",\\iter-seps(sort(\"Expression\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\")\"),layouts(\"LAYOUTLIST\"),lit(\"{\"),layouts(\"LAYOUTLIST\"),label(\"preStats\",\\iter-star-seps(sort(\"Statement\"),[layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),label(\"body\",sort(\"StringMiddle\")),layouts(\"LAYOUTLIST\"),label(\"postStats\",\\iter-star-seps(sort(\"Statement\"),[layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\"}\")],{})", Factory.Production);
  private static final IConstructor prod__Complement_Class__lit___33_layouts_LAYOUTLIST_charClass_Class_ = (IConstructor) _read("prod(label(\"Complement\",sort(\"Class\")),[lit(\"!\"),layouts(\"LAYOUTLIST\"),label(\"charClass\",sort(\"Class\"))],{})", Factory.Production);
  private static final IConstructor prod__lit_open__char_class___range__111_111_char_class___range__112_112_char_class___range__101_101_char_class___range__110_110_ = (IConstructor) _read("prod(lit(\"open\"),[\\char-class([range(111,111)]),\\char-class([range(112,112)]),\\char-class([range(101,101)]),\\char-class([range(110,110)])],{})", Factory.Production);
  private static final IConstructor prod__lit_quit__char_class___range__113_113_char_class___range__117_117_char_class___range__105_105_char_class___range__116_116_ = (IConstructor) _read("prod(lit(\"quit\"),[\\char-class([range(113,113)]),\\char-class([range(117,117)]),\\char-class([range(105,105)]),\\char-class([range(116,116)])],{})", Factory.Production);
  private static final IConstructor prod__Tag_ProdModifier__tag_Tag_ = (IConstructor) _read("prod(label(\"Tag\",sort(\"ProdModifier\")),[label(\"tag\",sort(\"Tag\"))],{})", Factory.Production);
  private static final IConstructor prod__lit_tuple__char_class___range__116_116_char_class___range__117_117_char_class___range__112_112_char_class___range__108_108_char_class___range__101_101_ = (IConstructor) _read("prod(lit(\"tuple\"),[\\char-class([range(116,116)]),\\char-class([range(117,117)]),\\char-class([range(112,112)]),\\char-class([range(108,108)]),\\char-class([range(101,101)])],{})", Factory.Production);
  private static final IConstructor prod__Default_StructuredType__basicType_BasicType_layouts_LAYOUTLIST_lit___91_layouts_LAYOUTLIST_arguments_iter_seps__TypeArg__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___93_ = (IConstructor) _read("prod(label(\"Default\",sort(\"StructuredType\")),[label(\"basicType\",sort(\"BasicType\")),layouts(\"LAYOUTLIST\"),lit(\"[\"),layouts(\"LAYOUTLIST\"),label(\"arguments\",\\iter-seps(sort(\"TypeArg\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\"]\")],{})", Factory.Production);
  private static final IConstructor regular__iter_seps__Variant__layouts_LAYOUTLIST_lit___124_layouts_LAYOUTLIST = (IConstructor) _read("regular(\\iter-seps(sort(\"Variant\"),[layouts(\"LAYOUTLIST\"),lit(\"|\"),layouts(\"LAYOUTLIST\")]))", Factory.Production);
  private static final IConstructor prod__MidProtocolChars__lit___62_URLChars_lit___60_ = (IConstructor) _read("prod(lex(\"MidProtocolChars\"),[lit(\"\\>\"),lex(\"URLChars\"),lit(\"\\<\")],{})", Factory.Production);
  private static final IConstructor prod__Location_Literal__locationLiteral_LocationLiteral_ = (IConstructor) _read("prod(label(\"Location\",sort(\"Literal\")),[label(\"locationLiteral\",sort(\"LocationLiteral\"))],{})", Factory.Production);
  private static final IConstructor prod__Default_Visibility__ = (IConstructor) _read("prod(label(\"Default\",sort(\"Visibility\")),[],{})", Factory.Production);
  private static final IConstructor prod__layouts_LAYOUTLIST__conditional__iter_star__LAYOUT__not_follow__char_class___range__9_10_range__13_13_range__32_32_not_follow__lit___47_47_not_follow__lit___47_42_ = (IConstructor) _read("prod(layouts(\"LAYOUTLIST\"),[conditional(\\iter-star(lex(\"LAYOUT\")),{\\not-follow(\\char-class([range(9,10),range(13,13),range(32,32)])),\\not-follow(lit(\"//\")),\\not-follow(lit(\"/*\"))})],{})", Factory.Production);
  private static final IConstructor prod__Set_BasicType__lit_set_ = (IConstructor) _read("prod(label(\"Set\",sort(\"BasicType\")),[lit(\"set\")],{})", Factory.Production);
  private static final IConstructor prod__Addition_Assignment__lit___43_61_ = (IConstructor) _read("prod(label(\"Addition\",sort(\"Assignment\")),[lit(\"+=\")],{})", Factory.Production);
  private static final IConstructor prod__FunctionDeclaration_Statement__functionDeclaration_FunctionDeclaration_ = (IConstructor) _read("prod(label(\"FunctionDeclaration\",sort(\"Statement\")),[label(\"functionDeclaration\",sort(\"FunctionDeclaration\"))],{})", Factory.Production);
  private static final IConstructor prod__Empty_Target__ = (IConstructor) _read("prod(label(\"Empty\",sort(\"Target\")),[],{})", Factory.Production);
  private static final IConstructor prod__lit_has__char_class___range__104_104_char_class___range__97_97_char_class___range__115_115_ = (IConstructor) _read("prod(lit(\"has\"),[\\char-class([range(104,104)]),\\char-class([range(97,97)]),\\char-class([range(115,115)])],{})", Factory.Production);
  private static final IConstructor prod__Tuple_Pattern__lit___60_layouts_LAYOUTLIST_elements_iter_seps__Pattern__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___62_ = (IConstructor) _read("prod(label(\"Tuple\",sort(\"Pattern\")),[lit(\"\\<\"),layouts(\"LAYOUTLIST\"),label(\"elements\",\\iter-seps(sort(\"Pattern\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\"\\>\")],{})", Factory.Production);
  private static final IConstructor prod__Right_Assoc__lit_right_ = (IConstructor) _read("prod(label(\"Right\",sort(\"Assoc\")),[lit(\"right\")],{})", Factory.Production);
  private static final IConstructor prod__RascalKeywords__lit_bag_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"bag\")],{})", Factory.Production);
  private static final IConstructor prod__NamedRegExp__lit___60_Name_lit___62_ = (IConstructor) _read("prod(lex(\"NamedRegExp\"),[lit(\"\\<\"),lex(\"Name\"),lit(\"\\>\")],{})", Factory.Production);
  private static final IConstructor prod__Empty_Sym__lit___40_layouts_LAYOUTLIST_lit___41_ = (IConstructor) _read("prod(label(\"Empty\",sort(\"Sym\")),[lit(\"(\"),layouts(\"LAYOUTLIST\"),lit(\")\")],{})", Factory.Production);
  private static final IConstructor regular__seq___conditional__char_class___range__65_90_range__95_95_range__97_122__not_precede__char_class___range__65_90_range__95_95_range__97_122_conditional__iter_star__char_class___range__48_57_range__65_90_range__95_95_range__97_122__not_follow__char_class___range__48_57_range__65_90_range__95_95_range__97_122 = (IConstructor) _read("regular(seq([conditional(\\char-class([range(65,90),range(95,95),range(97,122)]),{\\not-precede(\\char-class([range(65,90),range(95,95),range(97,122)]))}),conditional(\\iter-star(\\char-class([range(48,57),range(65,90),range(95,95),range(97,122)])),{\\not-follow(\\char-class([range(48,57),range(65,90),range(95,95),range(97,122)]))})]))", Factory.Production);
  private static final IConstructor prod__lit_outermost__char_class___range__111_111_char_class___range__117_117_char_class___range__116_116_char_class___range__101_101_char_class___range__114_114_char_class___range__109_109_char_class___range__111_111_char_class___range__115_115_char_class___range__116_116_ = (IConstructor) _read("prod(lit(\"outermost\"),[\\char-class([range(111,111)]),\\char-class([range(117,117)]),\\char-class([range(116,116)]),\\char-class([range(101,101)]),\\char-class([range(114,114)]),\\char-class([range(109,109)]),\\char-class([range(111,111)]),\\char-class([range(115,115)]),\\char-class([range(116,116)])],{})", Factory.Production);
  private static final IConstructor prod__Negation_Expression__lit___33_layouts_LAYOUTLIST_argument_Expression_ = (IConstructor) _read("prod(label(\"Negation\",sort(\"Expression\")),[lit(\"!\"),layouts(\"LAYOUTLIST\"),label(\"argument\",sort(\"Expression\"))],{})", Factory.Production);
  private static final IConstructor prod__Interpolated_StringMiddle__mid_MidStringChars_layouts_LAYOUTLIST_expression_Expression_layouts_LAYOUTLIST_tail_StringMiddle_ = (IConstructor) _read("prod(label(\"Interpolated\",sort(\"StringMiddle\")),[label(\"mid\",lex(\"MidStringChars\")),layouts(\"LAYOUTLIST\"),label(\"expression\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),label(\"tail\",sort(\"StringMiddle\"))],{})", Factory.Production);
  private static final IConstructor prod__Default_Catch__lit_catch_layouts_LAYOUTLIST_lit___58_layouts_LAYOUTLIST_body_Statement_ = (IConstructor) _read("prod(label(\"Default\",sort(\"Catch\")),[lit(\"catch\"),layouts(\"LAYOUTLIST\"),lit(\":\"),layouts(\"LAYOUTLIST\"),label(\"body\",sort(\"Statement\"))],{})", Factory.Production);
  private static final IConstructor prod__RascalKeywords__lit_case_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"case\")],{})", Factory.Production);
  private static final IConstructor prod__RascalKeywords__lit_layout_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"layout\")],{})", Factory.Production);
  private static final IConstructor prod__IterStarSep_Sym__lit___123_layouts_LAYOUTLIST_symbol_Sym_layouts_LAYOUTLIST_sep_Sym_layouts_LAYOUTLIST_lit___125_layouts_LAYOUTLIST_lit___42_ = (IConstructor) _read("prod(label(\"IterStarSep\",sort(\"Sym\")),[lit(\"{\"),layouts(\"LAYOUTLIST\"),label(\"symbol\",sort(\"Sym\")),layouts(\"LAYOUTLIST\"),label(\"sep\",sort(\"Sym\")),layouts(\"LAYOUTLIST\"),lit(\"}\"),layouts(\"LAYOUTLIST\"),lit(\"*\")],{})", Factory.Production);
  private static final IConstructor prod__NonInterpolated_PathPart__pathChars_PathChars_ = (IConstructor) _read("prod(label(\"NonInterpolated\",sort(\"PathPart\")),[label(\"pathChars\",lex(\"PathChars\"))],{})", Factory.Production);
  private static final IConstructor prod__lit___38_38__char_class___range__38_38_char_class___range__38_38_ = (IConstructor) _read("prod(lit(\"&&\"),[\\char-class([range(38,38)]),\\char-class([range(38,38)])],{})", Factory.Production);
  private static final IConstructor prod__Addition_Expression__lhs_Expression_layouts_LAYOUTLIST_lit___43_layouts_LAYOUTLIST_rhs_Expression__assoc__left = (IConstructor) _read("prod(label(\"Addition\",sort(\"Expression\")),[label(\"lhs\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"+\"),layouts(\"LAYOUTLIST\"),label(\"rhs\",sort(\"Expression\"))],{assoc(left())})", Factory.Production);
  private static final IConstructor prod__For_StringTemplate__lit_for_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_generators_iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_layouts_LAYOUTLIST_lit___123_layouts_LAYOUTLIST_preStats_iter_star_seps__Statement__layouts_LAYOUTLIST_layouts_LAYOUTLIST_body_StringMiddle_layouts_LAYOUTLIST_postStats_iter_star_seps__Statement__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_ = (IConstructor) _read("prod(label(\"For\",sort(\"StringTemplate\")),[lit(\"for\"),layouts(\"LAYOUTLIST\"),lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"generators\",\\iter-seps(sort(\"Expression\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\")\"),layouts(\"LAYOUTLIST\"),lit(\"{\"),layouts(\"LAYOUTLIST\"),label(\"preStats\",\\iter-star-seps(sort(\"Statement\"),[layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),label(\"body\",sort(\"StringMiddle\")),layouts(\"LAYOUTLIST\"),label(\"postStats\",\\iter-star-seps(sort(\"Statement\"),[layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\"}\")],{})", Factory.Production);
  private static final IConstructor regular__iter_star__char_class___range__9_10_range__13_13_range__32_32 = (IConstructor) _read("regular(\\iter-star(\\char-class([range(9,10),range(13,13),range(32,32)])))", Factory.Production);
  private static final IConstructor prod__RascalKeywords__lit_any_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"any\")],{})", Factory.Production);
  private static final IConstructor prod__RascalKeywords__lit_all_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"all\")],{})", Factory.Production);
  private static final IConstructor prod__Symbol_Type__symbol_Sym_ = (IConstructor) _read("prod(label(\"Symbol\",sort(\"Type\")),[label(\"symbol\",sort(\"Sym\"))],{})", Factory.Production);
  private static final IConstructor prod__Present_Start__lit_start_ = (IConstructor) _read("prod(label(\"Present\",sort(\"Start\")),[lit(\"start\")],{})", Factory.Production);
  private static final IConstructor prod__RascalKeywords__BasicType_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[sort(\"BasicType\")],{})", Factory.Production);
  private static final IConstructor prod__lit___38_61__char_class___range__38_38_char_class___range__61_61_ = (IConstructor) _read("prod(lit(\"&=\"),[\\char-class([range(38,38)]),\\char-class([range(61,61)])],{})", Factory.Production);
  private static final IConstructor prod__DecimalIntegerLiteral__conditional__lit_0__not_follow__char_class___range__48_57_range__65_90_range__95_95_range__97_122_ = (IConstructor) _read("prod(lex(\"DecimalIntegerLiteral\"),[conditional(lit(\"0\"),{\\not-follow(\\char-class([range(48,57),range(65,90),range(95,95),range(97,122)]))})],{})", Factory.Production);
  private static final IConstructor prod__Default_ModuleParameters__lit___91_layouts_LAYOUTLIST_parameters_iter_seps__TypeVar__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___93_ = (IConstructor) _read("prod(label(\"Default\",sort(\"ModuleParameters\")),[lit(\"[\"),layouts(\"LAYOUTLIST\"),label(\"parameters\",\\iter-seps(sort(\"TypeVar\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\"]\")],{})", Factory.Production);
  private static final IConstructor prod__Character_Range__character_Char_ = (IConstructor) _read("prod(label(\"Character\",sort(\"Range\")),[label(\"character\",lex(\"Char\"))],{})", Factory.Production);
  private static final IConstructor prod__Or_Expression__lhs_Expression_layouts_LAYOUTLIST_lit___124_124_layouts_LAYOUTLIST_rhs_Expression__assoc__left = (IConstructor) _read("prod(label(\"Or\",sort(\"Expression\")),[label(\"lhs\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"||\"),layouts(\"LAYOUTLIST\"),label(\"rhs\",sort(\"Expression\"))],{assoc(left())})", Factory.Production);
  private static final IConstructor prod__Selector_Type__selector_DataTypeSelector_ = (IConstructor) _read("prod(label(\"Selector\",sort(\"Type\")),[label(\"selector\",sort(\"DataTypeSelector\"))],{})", Factory.Production);
  private static final IConstructor regular__iter_seps__Catch__layouts_LAYOUTLIST = (IConstructor) _read("regular(\\iter-seps(sort(\"Catch\"),[layouts(\"LAYOUTLIST\")]))", Factory.Production);
  private static final IConstructor prod__TransitiveClosure_Expression__argument_Expression_layouts_LAYOUTLIST_conditional__lit___43__not_follow__lit___61_ = (IConstructor) _read("prod(label(\"TransitiveClosure\",sort(\"Expression\")),[label(\"argument\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),conditional(lit(\"+\"),{\\not-follow(lit(\"=\"))})],{})", Factory.Production);
  private static final IConstructor prod__lit___33_61__char_class___range__33_33_char_class___range__61_61_ = (IConstructor) _read("prod(lit(\"!=\"),[\\char-class([range(33,33)]),\\char-class([range(61,61)])],{})", Factory.Production);
  private static final IConstructor prod__Start_Sym__lit_start_layouts_LAYOUTLIST_lit___91_layouts_LAYOUTLIST_nonterminal_Nonterminal_layouts_LAYOUTLIST_lit___93_ = (IConstructor) _read("prod(label(\"Start\",sort(\"Sym\")),[lit(\"start\"),layouts(\"LAYOUTLIST\"),lit(\"[\"),layouts(\"LAYOUTLIST\"),label(\"nonterminal\",lex(\"Nonterminal\")),layouts(\"LAYOUTLIST\"),lit(\"]\")],{})", Factory.Production);
  private static final IConstructor prod__Equals_Expression__lhs_Expression_layouts_LAYOUTLIST_lit___61_61_layouts_LAYOUTLIST_rhs_Expression__assoc__non_assoc = (IConstructor) _read("prod(label(\"Equals\",sort(\"Expression\")),[label(\"lhs\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"==\"),layouts(\"LAYOUTLIST\"),label(\"rhs\",sort(\"Expression\"))],{assoc(\\non-assoc())})", Factory.Production);
  private static final IConstructor prod__Default_Renaming__from_Name_layouts_LAYOUTLIST_lit___61_62_layouts_LAYOUTLIST_to_Name_ = (IConstructor) _read("prod(label(\"Default\",sort(\"Renaming\")),[label(\"from\",lex(\"Name\")),layouts(\"LAYOUTLIST\"),lit(\"=\\>\"),layouts(\"LAYOUTLIST\"),label(\"to\",lex(\"Name\"))],{})", Factory.Production);
  private static final IConstructor prod__Iter_Sym__symbol_Sym_layouts_LAYOUTLIST_lit___43_ = (IConstructor) _read("prod(label(\"Iter\",sort(\"Sym\")),[label(\"symbol\",sort(\"Sym\")),layouts(\"LAYOUTLIST\"),lit(\"+\")],{})", Factory.Production);
  private static final IConstructor prod__Subscript_Assignable__receiver_Assignable_layouts_LAYOUTLIST_lit___91_layouts_LAYOUTLIST_subscript_Expression_layouts_LAYOUTLIST_lit___93_ = (IConstructor) _read("prod(label(\"Subscript\",sort(\"Assignable\")),[label(\"receiver\",sort(\"Assignable\")),layouts(\"LAYOUTLIST\"),lit(\"[\"),layouts(\"LAYOUTLIST\"),label(\"subscript\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"]\")],{})", Factory.Production);
  private static final IConstructor prod__lit_anno__char_class___range__97_97_char_class___range__110_110_char_class___range__110_110_char_class___range__111_111_ = (IConstructor) _read("prod(lit(\"anno\"),[\\char-class([range(97,97)]),\\char-class([range(110,110)]),\\char-class([range(110,110)]),\\char-class([range(111,111)])],{})", Factory.Production);
  private static final IConstructor prod__Default_LocationLiteral__protocolPart_ProtocolPart_layouts_LAYOUTLIST_pathPart_PathPart_ = (IConstructor) _read("prod(label(\"Default\",sort(\"LocationLiteral\")),[label(\"protocolPart\",sort(\"ProtocolPart\")),layouts(\"LAYOUTLIST\"),label(\"pathPart\",sort(\"PathPart\"))],{})", Factory.Production);
  private static final IConstructor prod__lit_top_down__char_class___range__116_116_char_class___range__111_111_char_class___range__112_112_char_class___range__45_45_char_class___range__100_100_char_class___range__111_111_char_class___range__119_119_char_class___range__110_110_ = (IConstructor) _read("prod(lit(\"top-down\"),[\\char-class([range(116,116)]),\\char-class([range(111,111)]),\\char-class([range(112,112)]),\\char-class([range(45,45)]),\\char-class([range(100,100)]),\\char-class([range(111,111)]),\\char-class([range(119,119)]),\\char-class([range(110,110)])],{})", Factory.Production);
  private static final IConstructor prod__lit_openrec__char_class___range__111_111_char_class___range__112_112_char_class___range__101_101_char_class___range__110_110_char_class___range__114_114_char_class___range__101_101_char_class___range__99_99_ = (IConstructor) _read("prod(lit(\"openrec\"),[\\char-class([range(111,111)]),\\char-class([range(112,112)]),\\char-class([range(101,101)]),\\char-class([range(110,110)]),\\char-class([range(114,114)]),\\char-class([range(101,101)]),\\char-class([range(99,99)])],{})", Factory.Production);
  private static final IConstructor prod__lit___46_46_46__char_class___range__46_46_char_class___range__46_46_char_class___range__46_46_ = (IConstructor) _read("prod(lit(\"...\"),[\\char-class([range(46,46)]),\\char-class([range(46,46)]),\\char-class([range(46,46)])],{})", Factory.Production);
  private static final IConstructor prod__EndOfLine_Sym__symbol_Sym_layouts_LAYOUTLIST_lit___36_ = (IConstructor) _read("prod(label(\"EndOfLine\",sort(\"Sym\")),[label(\"symbol\",sort(\"Sym\")),layouts(\"LAYOUTLIST\"),lit(\"$\")],{})", Factory.Production);
  private static final IConstructor prod__lit_top_down_break__char_class___range__116_116_char_class___range__111_111_char_class___range__112_112_char_class___range__45_45_char_class___range__100_100_char_class___range__111_111_char_class___range__119_119_char_class___range__110_110_char_class___range__45_45_char_class___range__98_98_char_class___range__114_114_char_class___range__101_101_char_class___range__97_97_char_class___range__107_107_ = (IConstructor) _read("prod(lit(\"top-down-break\"),[\\char-class([range(116,116)]),\\char-class([range(111,111)]),\\char-class([range(112,112)]),\\char-class([range(45,45)]),\\char-class([range(100,100)]),\\char-class([range(111,111)]),\\char-class([range(119,119)]),\\char-class([range(110,110)]),\\char-class([range(45,45)]),\\char-class([range(98,98)]),\\char-class([range(114,114)]),\\char-class([range(101,101)]),\\char-class([range(97,97)]),\\char-class([range(107,107)])],{})", Factory.Production);
  private static final IConstructor prod__lit_view__char_class___range__118_118_char_class___range__105_105_char_class___range__101_101_char_class___range__119_119_ = (IConstructor) _read("prod(lit(\"view\"),[\\char-class([range(118,118)]),\\char-class([range(105,105)]),\\char-class([range(101,101)]),\\char-class([range(119,119)])],{})", Factory.Production);
  private static final IConstructor prod__lit_innermost__char_class___range__105_105_char_class___range__110_110_char_class___range__110_110_char_class___range__101_101_char_class___range__114_114_char_class___range__109_109_char_class___range__111_111_char_class___range__115_115_char_class___range__116_116_ = (IConstructor) _read("prod(lit(\"innermost\"),[\\char-class([range(105,105)]),\\char-class([range(110,110)]),\\char-class([range(110,110)]),\\char-class([range(101,101)]),\\char-class([range(114,114)]),\\char-class([range(109,109)]),\\char-class([range(111,111)]),\\char-class([range(115,115)]),\\char-class([range(116,116)])],{})", Factory.Production);
  private static final IConstructor prod__lit_one__char_class___range__111_111_char_class___range__110_110_char_class___range__101_101_ = (IConstructor) _read("prod(lit(\"one\"),[\\char-class([range(111,111)]),\\char-class([range(110,110)]),\\char-class([range(101,101)])],{})", Factory.Production);
  private static final IConstructor prod__SetAnnotation_Expression__expression_Expression_layouts_LAYOUTLIST_lit___91_layouts_LAYOUTLIST_lit___64_layouts_LAYOUTLIST_name_Name_layouts_LAYOUTLIST_lit___61_layouts_LAYOUTLIST_value_Expression_layouts_LAYOUTLIST_lit___93_ = (IConstructor) _read("prod(label(\"SetAnnotation\",sort(\"Expression\")),[label(\"expression\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"[\"),layouts(\"LAYOUTLIST\"),lit(\"@\"),layouts(\"LAYOUTLIST\"),label(\"name\",lex(\"Name\")),layouts(\"LAYOUTLIST\"),lit(\"=\"),layouts(\"LAYOUTLIST\"),label(\"value\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"]\")],{})", Factory.Production);
  private static final IConstructor prod__Empty_DataTarget__ = (IConstructor) _read("prod(label(\"Empty\",sort(\"DataTarget\")),[],{})", Factory.Production);
  private static final IConstructor prod__Binding_Catch__lit_catch_layouts_LAYOUTLIST_pattern_Pattern_layouts_LAYOUTLIST_lit___58_layouts_LAYOUTLIST_body_Statement_ = (IConstructor) _read("prod(label(\"Binding\",sort(\"Catch\")),[lit(\"catch\"),layouts(\"LAYOUTLIST\"),label(\"pattern\",sort(\"Pattern\")),layouts(\"LAYOUTLIST\"),lit(\":\"),layouts(\"LAYOUTLIST\"),label(\"body\",sort(\"Statement\"))],{})", Factory.Production);
  private static final IConstructor regular__iter_seps__Sym__layouts_LAYOUTLIST = (IConstructor) _read("regular(\\iter-seps(sort(\"Sym\"),[layouts(\"LAYOUTLIST\")]))", Factory.Production);
  private static final IConstructor prod__IfDefinedOtherwise_Expression__lhs_Expression_layouts_LAYOUTLIST_lit___63_layouts_LAYOUTLIST_rhs_Expression__assoc__non_assoc = (IConstructor) _read("prod(label(\"IfDefinedOtherwise\",sort(\"Expression\")),[label(\"lhs\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"?\"),layouts(\"LAYOUTLIST\"),label(\"rhs\",sort(\"Expression\"))],{assoc(\\non-assoc())})", Factory.Production);
  private static final IConstructor regular__iter_star_seps__Range__layouts_LAYOUTLIST = (IConstructor) _read("regular(\\iter-star-seps(sort(\"Range\"),[layouts(\"LAYOUTLIST\")]))", Factory.Production);
  private static final IConstructor prod__lit_while__char_class___range__119_119_char_class___range__104_104_char_class___range__105_105_char_class___range__108_108_char_class___range__101_101_ = (IConstructor) _read("prod(lit(\"while\"),[\\char-class([range(119,119)]),\\char-class([range(104,104)]),\\char-class([range(105,105)]),\\char-class([range(108,108)]),\\char-class([range(101,101)])],{})", Factory.Production);
  private static final IConstructor regular__iter_seps__TypeArg__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST = (IConstructor) _read("regular(\\iter-seps(sort(\"TypeArg\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")]))", Factory.Production);
  private static final IConstructor prod__lit_notin__char_class___range__110_110_char_class___range__111_111_char_class___range__116_116_char_class___range__105_105_char_class___range__110_110_ = (IConstructor) _read("prod(lit(\"notin\"),[\\char-class([range(110,110)]),\\char-class([range(111,111)]),\\char-class([range(116,116)]),\\char-class([range(105,105)]),\\char-class([range(110,110)])],{})", Factory.Production);
  private static final IConstructor regular__iter_seps__Renaming__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST = (IConstructor) _read("regular(\\iter-seps(sort(\"Renaming\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")]))", Factory.Production);
  private static final IConstructor prod__Unimport_ShellCommand__lit_unimport_layouts_LAYOUTLIST_name_QualifiedName_ = (IConstructor) _read("prod(label(\"Unimport\",sort(\"ShellCommand\")),[lit(\"unimport\"),layouts(\"LAYOUTLIST\"),label(\"name\",sort(\"QualifiedName\"))],{})", Factory.Production);
  private static final IConstructor prod__Default_Renamings__lit_renaming_layouts_LAYOUTLIST_renamings_iter_seps__Renaming__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_ = (IConstructor) _read("prod(label(\"Default\",sort(\"Renamings\")),[lit(\"renaming\"),layouts(\"LAYOUTLIST\"),label(\"renamings\",\\iter-seps(sort(\"Renaming\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")]))],{})", Factory.Production);
  private static final IConstructor prod__RascalKeywords__lit_dynamic_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"dynamic\")],{})", Factory.Production);
  private static final IConstructor prod__Renamings_ImportedModule__name_QualifiedName_layouts_LAYOUTLIST_renamings_Renamings_ = (IConstructor) _read("prod(label(\"Renamings\",sort(\"ImportedModule\")),[label(\"name\",sort(\"QualifiedName\")),layouts(\"LAYOUTLIST\"),label(\"renamings\",sort(\"Renamings\"))],{})", Factory.Production);
  private static final IConstructor prod__RascalKeywords__lit_data_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"data\")],{})", Factory.Production);
  private static final IConstructor prod__Labeled_Sym__symbol_Sym_layouts_LAYOUTLIST_label_NonterminalLabel_ = (IConstructor) _read("prod(label(\"Labeled\",sort(\"Sym\")),[label(\"symbol\",sort(\"Sym\")),layouts(\"LAYOUTLIST\"),label(\"label\",lex(\"NonterminalLabel\"))],{})", Factory.Production);
  private static final IConstructor prod__HexIntegerLiteral_IntegerLiteral__hex_HexIntegerLiteral_ = (IConstructor) _read("prod(label(\"HexIntegerLiteral\",sort(\"IntegerLiteral\")),[label(\"hex\",lex(\"HexIntegerLiteral\"))],{})", Factory.Production);
  private static final IConstructor regular__opt__seq___char_class___range__48_57_opt__char_class___range__48_57 = (IConstructor) _read("regular(opt(seq([\\char-class([range(48,57)]),opt(\\char-class([range(48,57)]))])))", Factory.Production);
  private static final IConstructor prod__RascalKeywords__lit_catch_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"catch\")],{})", Factory.Production);
  private static final IConstructor prod__GlobalDirective_Statement__lit_global_layouts_LAYOUTLIST_type_Type_layouts_LAYOUTLIST_names_iter_seps__QualifiedName__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___59_ = (IConstructor) _read("prod(label(\"GlobalDirective\",sort(\"Statement\")),[lit(\"global\"),layouts(\"LAYOUTLIST\"),label(\"type\",sort(\"Type\")),layouts(\"LAYOUTLIST\"),label(\"names\",\\iter-seps(sort(\"QualifiedName\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\";\")],{})", Factory.Production);
  private static final IConstructor prod__Test_FunctionModifier__lit_test_ = (IConstructor) _read("prod(label(\"Test\",sort(\"FunctionModifier\")),[lit(\"test\")],{})", Factory.Production);
  private static final IConstructor prod__Default_Tag__lit___64_layouts_LAYOUTLIST_name_Name_layouts_LAYOUTLIST_contents_TagString__tag__Folded_tag__category___67_111_109_109_101_110_116 = (IConstructor) _read("prod(label(\"Default\",sort(\"Tag\")),[lit(\"@\"),layouts(\"LAYOUTLIST\"),label(\"name\",lex(\"Name\")),layouts(\"LAYOUTLIST\"),label(\"contents\",lex(\"TagString\"))],{tag(Folded()),tag(category(\"Comment\"))})", Factory.Production);
  private static final IConstructor prod__Language_SyntaxDefinition__start_Start_layouts_LAYOUTLIST_lit_syntax_layouts_LAYOUTLIST_defined_Sym_layouts_LAYOUTLIST_lit___61_layouts_LAYOUTLIST_production_Prod_layouts_LAYOUTLIST_lit___59__tag__Foldable = (IConstructor) _read("prod(label(\"Language\",sort(\"SyntaxDefinition\")),[label(\"start\",sort(\"Start\")),layouts(\"LAYOUTLIST\"),lit(\"syntax\"),layouts(\"LAYOUTLIST\"),label(\"defined\",sort(\"Sym\")),layouts(\"LAYOUTLIST\"),lit(\"=\"),layouts(\"LAYOUTLIST\"),label(\"production\",sort(\"Prod\")),layouts(\"LAYOUTLIST\"),lit(\";\")],{tag(Foldable())})", Factory.Production);
  private static final IConstructor prod__Default_Import__lit_import_layouts_LAYOUTLIST_module_ImportedModule_layouts_LAYOUTLIST_lit___59_ = (IConstructor) _read("prod(label(\"Default\",sort(\"Import\")),[lit(\"import\"),layouts(\"LAYOUTLIST\"),label(\"module\",sort(\"ImportedModule\")),layouts(\"LAYOUTLIST\"),lit(\";\")],{})", Factory.Production);
  private static final IConstructor prod__List_Commands__commands_iter_seps__EvalCommand__layouts_LAYOUTLIST_ = (IConstructor) _read("prod(label(\"List\",sort(\"Commands\")),[label(\"commands\",\\iter-seps(sort(\"EvalCommand\"),[layouts(\"LAYOUTLIST\")]))],{})", Factory.Production);
  private static final IConstructor prod__Switch_Statement__label_Label_layouts_LAYOUTLIST_lit_switch_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_expression_Expression_layouts_LAYOUTLIST_lit___41_layouts_LAYOUTLIST_lit___123_layouts_LAYOUTLIST_cases_iter_seps__Case__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_ = (IConstructor) _read("prod(label(\"Switch\",sort(\"Statement\")),[label(\"label\",sort(\"Label\")),layouts(\"LAYOUTLIST\"),lit(\"switch\"),layouts(\"LAYOUTLIST\"),lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"expression\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\")\"),layouts(\"LAYOUTLIST\"),lit(\"{\"),layouts(\"LAYOUTLIST\"),label(\"cases\",\\iter-seps(sort(\"Case\"),[layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\"}\")],{})", Factory.Production);
  private static final IConstructor prod__List_BasicType__lit_list_ = (IConstructor) _read("prod(label(\"List\",sort(\"BasicType\")),[lit(\"list\")],{})", Factory.Production);
  private static final IConstructor prod__VariableDeclaration_Statement__declaration_LocalVariableDeclaration_layouts_LAYOUTLIST_lit___59_ = (IConstructor) _read("prod(label(\"VariableDeclaration\",sort(\"Statement\")),[label(\"declaration\",sort(\"LocalVariableDeclaration\")),layouts(\"LAYOUTLIST\"),lit(\";\")],{})", Factory.Production);
  private static final IConstructor prod__ProtocolChars__char_class___range__124_124_URLChars_conditional__lit___58_47_47__not_follow__char_class___range__9_10_range__13_13_range__32_32_ = (IConstructor) _read("prod(lex(\"ProtocolChars\"),[\\char-class([range(124,124)]),lex(\"URLChars\"),conditional(lit(\"://\"),{\\not-follow(\\char-class([range(9,10),range(13,13),range(32,32)]))})],{})", Factory.Production);
  private static final IConstructor prod__CaseInsensitiveStringConstant__lit___39_iter_star__StringCharacter_lit___39__tag__category___67_111_110_115_116_97_110_116 = (IConstructor) _read("prod(lex(\"CaseInsensitiveStringConstant\"),[lit(\"\\'\\\\\"),\\iter-star(lex(\"StringCharacter\")),lit(\"\\'\\\\\")],{tag(category(\"Constant\"))})", Factory.Production);
  private static final IConstructor prod__RascalKeywords__lit_assoc_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"assoc\")],{})", Factory.Production);
  private static final IConstructor prod__Match_Expression__pattern_Pattern_layouts_LAYOUTLIST_lit___58_61_layouts_LAYOUTLIST_expression_Expression__assoc__non_assoc = (IConstructor) _read("prod(label(\"Match\",sort(\"Expression\")),[label(\"pattern\",sort(\"Pattern\")),layouts(\"LAYOUTLIST\"),lit(\":=\"),layouts(\"LAYOUTLIST\"),label(\"expression\",sort(\"Expression\"))],{assoc(\\non-assoc())})", Factory.Production);
  private static final IConstructor prod__Nonterminal__conditional__char_class___range__65_90__not_precede__char_class___range__65_90_conditional__iter_star__char_class___range__48_57_range__65_90_range__95_95_range__97_122__not_follow__char_class___range__48_57_range__65_90_range__95_95_range__97_122_delete__RascalReservedKeywords_ = (IConstructor) _read("prod(lex(\"Nonterminal\"),[conditional(\\char-class([range(65,90)]),{\\not-precede(\\char-class([range(65,90)]))}),conditional(\\iter-star(\\char-class([range(48,57),range(65,90),range(95,95),range(97,122)])),{\\not-follow(\\char-class([range(48,57),range(65,90),range(95,95),range(97,122)])),delete(sort(\"RascalReservedKeywords\"))})],{})", Factory.Production);
  private static final IConstructor prod__ActualsRenaming_ImportedModule__name_QualifiedName_layouts_LAYOUTLIST_actuals_ModuleActuals_layouts_LAYOUTLIST_renamings_Renamings_ = (IConstructor) _read("prod(label(\"ActualsRenaming\",sort(\"ImportedModule\")),[label(\"name\",sort(\"QualifiedName\")),layouts(\"LAYOUTLIST\"),label(\"actuals\",sort(\"ModuleActuals\")),layouts(\"LAYOUTLIST\"),label(\"renamings\",sort(\"Renamings\"))],{})", Factory.Production);
  private static final IConstructor prod__lit_default__char_class___range__100_100_char_class___range__101_101_char_class___range__102_102_char_class___range__97_97_char_class___range__117_117_char_class___range__108_108_char_class___range__116_116_ = (IConstructor) _read("prod(lit(\"default\"),[\\char-class([range(100,100)]),\\char-class([range(101,101)]),\\char-class([range(102,102)]),\\char-class([range(97,97)]),\\char-class([range(117,117)]),\\char-class([range(108,108)]),\\char-class([range(116,116)])],{})", Factory.Production);
  private static final IConstructor prod__Name_UserType__name_QualifiedName_ = (IConstructor) _read("prod(label(\"Name\",sort(\"UserType\")),[label(\"name\",sort(\"QualifiedName\"))],{})", Factory.Production);
  private static final IConstructor prod__lit_bool__char_class___range__98_98_char_class___range__111_111_char_class___range__111_111_char_class___range__108_108_ = (IConstructor) _read("prod(lit(\"bool\"),[\\char-class([range(98,98)]),\\char-class([range(111,111)]),\\char-class([range(111,111)]),\\char-class([range(108,108)])],{})", Factory.Production);
  private static final IConstructor prod__Expression_Command__expression_Expression_ = (IConstructor) _read("prod(label(\"Expression\",sort(\"Command\")),[label(\"expression\",sort(\"Expression\"))],{})", Factory.Production);
  private static final IConstructor regular__iter_star_seps__Statement__layouts_LAYOUTLIST = (IConstructor) _read("regular(\\iter-star-seps(sort(\"Statement\"),[layouts(\"LAYOUTLIST\")]))", Factory.Production);
  private static final IConstructor prod__EmptyStatement_Statement__lit___59_ = (IConstructor) _read("prod(label(\"EmptyStatement\",sort(\"Statement\")),[lit(\";\")],{})", Factory.Production);
  private static final IConstructor prod__NAryConstructor_Variant__name_Name_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_arguments_iter_star_seps__TypeArg__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_ = (IConstructor) _read("prod(label(\"NAryConstructor\",sort(\"Variant\")),[label(\"name\",lex(\"Name\")),layouts(\"LAYOUTLIST\"),lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"arguments\",\\iter-star-seps(sort(\"TypeArg\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\")\")],{})", Factory.Production);
  private static final IConstructor regular__opt__char_class___range__68_68_range__70_70_range__100_100_range__102_102 = (IConstructor) _read("regular(opt(\\char-class([range(68,68),range(70,70),range(100,100),range(102,102)])))", Factory.Production);
  private static final IConstructor prod__Equivalence_Expression__lhs_Expression_layouts_LAYOUTLIST_lit___60_61_61_62_layouts_LAYOUTLIST_rhs_Expression__assoc__non_assoc = (IConstructor) _read("prod(label(\"Equivalence\",sort(\"Expression\")),[label(\"lhs\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"\\<==\\>\"),layouts(\"LAYOUTLIST\"),label(\"rhs\",sort(\"Expression\"))],{assoc(\\non-assoc())})", Factory.Production);
  private static final IConstructor prod__TransitiveReflexiveClosure_Expression__argument_Expression_layouts_LAYOUTLIST_conditional__lit___42__not_follow__lit___61_ = (IConstructor) _read("prod(label(\"TransitiveReflexiveClosure\",sort(\"Expression\")),[label(\"argument\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),conditional(lit(\"*\"),{\\not-follow(lit(\"=\"))})],{})", Factory.Production);
  private static final IConstructor regular__iter_star__LAYOUT = (IConstructor) _read("regular(\\iter-star(lex(\"LAYOUT\")))", Factory.Production);
  private static final IConstructor prod__Set_Comprehension__lit___123_layouts_LAYOUTLIST_results_iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___124_layouts_LAYOUTLIST_generators_iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_ = (IConstructor) _read("prod(label(\"Set\",sort(\"Comprehension\")),[lit(\"{\"),layouts(\"LAYOUTLIST\"),label(\"results\",\\iter-seps(sort(\"Expression\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\"|\"),layouts(\"LAYOUTLIST\"),label(\"generators\",\\iter-seps(sort(\"Expression\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\"}\")],{})", Factory.Production);
  private static final IConstructor regular__iter_star_seps__Pattern__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST = (IConstructor) _read("regular(\\iter-star-seps(sort(\"Pattern\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")]))", Factory.Production);
  private static final IConstructor prod__Reference_Prod__lit___58_layouts_LAYOUTLIST_referenced_Name_ = (IConstructor) _read("prod(label(\"Reference\",sort(\"Prod\")),[lit(\":\"),layouts(\"LAYOUTLIST\"),label(\"referenced\",lex(\"Name\"))],{})", Factory.Production);
  private static final IConstructor prod__lit_variable__char_class___range__118_118_char_class___range__97_97_char_class___range__114_114_char_class___range__105_105_char_class___range__97_97_char_class___range__98_98_char_class___range__108_108_char_class___range__101_101_ = (IConstructor) _read("prod(lit(\"variable\"),[\\char-class([range(118,118)]),\\char-class([range(97,97)]),\\char-class([range(114,114)]),\\char-class([range(105,105)]),\\char-class([range(97,97)]),\\char-class([range(98,98)]),\\char-class([range(108,108)]),\\char-class([range(101,101)])],{})", Factory.Production);
  private static final IConstructor prod__Empty_Bound__ = (IConstructor) _read("prod(label(\"Empty\",sort(\"Bound\")),[],{})", Factory.Production);
  private static final IConstructor prod__HeaderKeyword__lit_start_ = (IConstructor) _read("prod(keywords(\"HeaderKeyword\"),[lit(\"start\")],{})", Factory.Production);
  private static final IConstructor prod__lit___60_61_61_62__char_class___range__60_60_char_class___range__61_61_char_class___range__61_61_char_class___range__62_62_ = (IConstructor) _read("prod(lit(\"\\<==\\>\"),[\\char-class([range(60,60)]),\\char-class([range(61,61)]),\\char-class([range(61,61)]),\\char-class([range(62,62)])],{})", Factory.Production);
  private static final IConstructor prod__StringConstant__lit___34_iter_star__StringCharacter_lit___34__tag__category___67_111_110_115_116_97_110_116 = (IConstructor) _read("prod(lex(\"StringConstant\"),[lit(\"\\\"\"),\\iter-star(lex(\"StringCharacter\")),lit(\"\\\"\")],{tag(category(\"Constant\"))})", Factory.Production);
  private static final IConstructor regular__iter_star_seps__Mapping__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST = (IConstructor) _read("regular(\\iter-star-seps(\\parameterized-sort(\"Mapping\",[sort(\"Expression\")]),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")]))", Factory.Production);
  private static final IConstructor prod__lit_str__char_class___range__115_115_char_class___range__116_116_char_class___range__114_114_ = (IConstructor) _read("prod(lit(\"str\"),[\\char-class([range(115,115)]),\\char-class([range(116,116)]),\\char-class([range(114,114)])],{})", Factory.Production);
  private static final IConstructor prod__layouts_$QUOTES__conditional__iter_star__char_class___range__9_10_range__13_13_range__32_32__not_follow__char_class___range__9_10_range__13_13_range__32_32_ = (IConstructor) _read("prod(layouts(\"$QUOTES\"),[conditional(\\iter-star(\\char-class([range(9,10),range(13,13),range(32,32)])),{\\not-follow(\\char-class([range(9,10),range(13,13),range(32,32)]))})],{})", Factory.Production);
  private static final IConstructor regular__iter_seps__Field__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST = (IConstructor) _read("regular(\\iter-seps(sort(\"Field\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")]))", Factory.Production);
  private static final IConstructor regular__iter_seps__EvalCommand__layouts_LAYOUTLIST = (IConstructor) _read("regular(\\iter-seps(sort(\"EvalCommand\"),[layouts(\"LAYOUTLIST\")]))", Factory.Production);
  private static final IConstructor prod__SimpleCharclass_Class__lit___91_layouts_LAYOUTLIST_ranges_iter_star_seps__Range__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___93_ = (IConstructor) _read("prod(label(\"SimpleCharclass\",sort(\"Class\")),[lit(\"[\"),layouts(\"LAYOUTLIST\"),label(\"ranges\",\\iter-star-seps(sort(\"Range\"),[layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\"]\")],{})", Factory.Production);
  private static final IConstructor prod__PreProtocolChars__lit___124_URLChars_lit___60_ = (IConstructor) _read("prod(lex(\"PreProtocolChars\"),[lit(\"|\"),lex(\"URLChars\"),lit(\"\\<\")],{})", Factory.Production);
  private static final IConstructor prod__Subtraction_Assignment__lit___45_61_ = (IConstructor) _read("prod(label(\"Subtraction\",sort(\"Assignment\")),[lit(\"-=\")],{})", Factory.Production);
  private static final IConstructor prod__RascalKeywords__lit_int_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"int\")],{})", Factory.Production);
  private static final IConstructor regular__iter_star_seps__TypeArg__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST = (IConstructor) _read("regular(\\iter-star-seps(sort(\"TypeArg\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")]))", Factory.Production);
  private static final IConstructor prod__Post_ProtocolTail__post_PostProtocolChars_ = (IConstructor) _read("prod(label(\"Post\",sort(\"ProtocolTail\")),[label(\"post\",lex(\"PostProtocolChars\"))],{})", Factory.Production);
  private static final IConstructor prod__HexIntegerLiteral__char_class___range__48_48_char_class___range__88_88_range__120_120_conditional__iter__char_class___range__48_57_range__65_70_range__97_102__not_follow__char_class___range__48_57_range__65_90_range__95_95_range__97_122_ = (IConstructor) _read("prod(lex(\"HexIntegerLiteral\"),[\\char-class([range(48,48)]),\\char-class([range(88,88),range(120,120)]),conditional(iter(\\char-class([range(48,57),range(65,70),range(97,102)])),{\\not-follow(\\char-class([range(48,57),range(65,90),range(95,95),range(97,122)]))})],{})", Factory.Production);
  private static final IConstructor prod__AsType_Pattern__lit___91_layouts_LAYOUTLIST_type_Type_layouts_LAYOUTLIST_lit___93_layouts_LAYOUTLIST_argument_Pattern_ = (IConstructor) _read("prod(label(\"AsType\",sort(\"Pattern\")),[lit(\"[\"),layouts(\"LAYOUTLIST\"),label(\"type\",sort(\"Type\")),layouts(\"LAYOUTLIST\"),lit(\"]\"),layouts(\"LAYOUTLIST\"),label(\"argument\",sort(\"Pattern\"))],{})", Factory.Production);
  private static final IConstructor regular__iter_star__char_class___range__48_57_range__65_90_range__95_95_range__97_122 = (IConstructor) _read("regular(\\iter-star(\\char-class([range(48,57),range(65,90),range(95,95),range(97,122)])))", Factory.Production);
  private static final IConstructor prod__Descendant_Pattern__lit___47_layouts_LAYOUTLIST_pattern_Pattern_ = (IConstructor) _read("prod(label(\"Descendant\",sort(\"Pattern\")),[lit(\"/\"),layouts(\"LAYOUTLIST\"),label(\"pattern\",sort(\"Pattern\"))],{})", Factory.Production);
  private static final IConstructor prod__Append_Assignment__lit___60_60_61_ = (IConstructor) _read("prod(label(\"Append\",sort(\"Assignment\")),[lit(\"\\<\\<=\")],{})", Factory.Production);
  private static final IConstructor prod__Assignment_Statement__assignable_Assignable_layouts_LAYOUTLIST_operator_Assignment_layouts_LAYOUTLIST_statement_Statement_ = (IConstructor) _read("prod(label(\"Assignment\",sort(\"Statement\")),[label(\"assignable\",sort(\"Assignable\")),layouts(\"LAYOUTLIST\"),label(\"operator\",sort(\"Assignment\")),layouts(\"LAYOUTLIST\"),label(\"statement\",sort(\"Statement\"))],{})", Factory.Production);
  private static final IConstructor prod__Map_Expression__lit___40_layouts_LAYOUTLIST_mappings_iter_star_seps__Mapping__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_ = (IConstructor) _read("prod(label(\"Map\",sort(\"Expression\")),[lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"mappings\",\\iter-star-seps(\\parameterized-sort(\"Mapping\",[sort(\"Expression\")]),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\")\")],{})", Factory.Production);
  private static final IConstructor prod__lit___61_62__char_class___range__61_61_char_class___range__62_62_ = (IConstructor) _read("prod(lit(\"=\\>\"),[\\char-class([range(61,61)]),\\char-class([range(62,62)])],{})", Factory.Production);
  private static final IConstructor prod__RascalKeywords__lit_alias_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"alias\")],{})", Factory.Production);
  private static final IConstructor prod__lit___61_61__char_class___range__61_61_char_class___range__61_61_ = (IConstructor) _read("prod(lit(\"==\"),[\\char-class([range(61,61)]),\\char-class([range(61,61)])],{})", Factory.Production);
  private static final IConstructor regular__iter_star_seps__Sym__layouts_LAYOUTLIST = (IConstructor) _read("regular(\\iter-star-seps(sort(\"Sym\"),[layouts(\"LAYOUTLIST\")]))", Factory.Production);
  private static final IConstructor prod__Function_Type__function_FunctionType_ = (IConstructor) _read("prod(label(\"Function\",sort(\"Type\")),[label(\"function\",sort(\"FunctionType\"))],{})", Factory.Production);
  private static final IConstructor prod__ListDeclarations_ShellCommand__lit_declarations_ = (IConstructor) _read("prod(label(\"ListDeclarations\",sort(\"ShellCommand\")),[lit(\"declarations\")],{})", Factory.Production);
  private static final IConstructor prod__RealLiteral__conditional__lit___46__not_precede__char_class___range__46_46_iter__char_class___range__48_57_opt__char_class___range__68_68_range__70_70_range__100_100_range__102_102_ = (IConstructor) _read("prod(lex(\"RealLiteral\"),[conditional(lit(\".\"),{\\not-precede(\\char-class([range(46,46)]))}),iter(\\char-class([range(48,57)])),opt(\\char-class([range(68,68),range(70,70),range(100,100),range(102,102)]))],{})", Factory.Production);
  private static final IConstructor prod__lit___62_61__char_class___range__62_62_char_class___range__61_61_ = (IConstructor) _read("prod(lit(\"\\>=\"),[\\char-class([range(62,62)]),\\char-class([range(61,61)])],{})", Factory.Production);
  private static final IConstructor prod__Name__char_class___range__92_92_char_class___range__65_90_range__95_95_range__97_122_conditional__iter_star__char_class___range__45_45_range__48_57_range__65_90_range__95_95_range__97_122__not_follow__char_class___range__45_45_range__48_57_range__65_90_range__95_95_range__97_122_ = (IConstructor) _read("prod(lex(\"Name\"),[\\char-class([range(92,92)]),\\char-class([range(65,90),range(95,95),range(97,122)]),conditional(\\iter-star(\\char-class([range(45,45),range(48,57),range(65,90),range(95,95),range(97,122)])),{\\not-follow(\\char-class([range(45,45),range(48,57),range(65,90),range(95,95),range(97,122)]))})],{})", Factory.Production);
  private static final IConstructor prod__Import_EvalCommand__imported_Import_ = (IConstructor) _read("prod(label(\"Import\",sort(\"EvalCommand\")),[label(\"imported\",sort(\"Import\"))],{})", Factory.Production);
  private static final IConstructor prod__JustTime__lit___36_84_conditional__TimePartNoTZ__not_follow__char_class___range__43_43_range__45_45_ = (IConstructor) _read("prod(lex(\"JustTime\"),[lit(\"$T\"),conditional(lex(\"TimePartNoTZ\"),{\\not-follow(\\char-class([range(43,43),range(45,45)]))})],{})", Factory.Production);
  private static final IConstructor prod__lit_non_assoc__char_class___range__110_110_char_class___range__111_111_char_class___range__110_110_char_class___range__45_45_char_class___range__97_97_char_class___range__115_115_char_class___range__115_115_char_class___range__111_111_char_class___range__99_99_ = (IConstructor) _read("prod(lit(\"non-assoc\"),[\\char-class([range(110,110)]),\\char-class([range(111,111)]),\\char-class([range(110,110)]),\\char-class([range(45,45)]),\\char-class([range(97,97)]),\\char-class([range(115,115)]),\\char-class([range(115,115)]),\\char-class([range(111,111)]),\\char-class([range(99,99)])],{})", Factory.Production);
  private static final IConstructor prod__Syntax_Import__syntax_SyntaxDefinition_ = (IConstructor) _read("prod(label(\"Syntax\",sort(\"Import\")),[label(\"syntax\",sort(\"SyntaxDefinition\"))],{})", Factory.Production);
  private static final IConstructor prod__lit_private__char_class___range__112_112_char_class___range__114_114_char_class___range__105_105_char_class___range__118_118_char_class___range__97_97_char_class___range__116_116_char_class___range__101_101_ = (IConstructor) _read("prod(lit(\"private\"),[\\char-class([range(112,112)]),\\char-class([range(114,114)]),\\char-class([range(105,105)]),\\char-class([range(118,118)]),\\char-class([range(97,97)]),\\char-class([range(116,116)]),\\char-class([range(101,101)])],{})", Factory.Production);
  private static final IConstructor prod__utf32_UnicodeEscape__lit___92_char_class___range__85_85_char_class___range__48_57_range__65_70_range__97_102_char_class___range__48_57_range__65_70_range__97_102_char_class___range__48_57_range__65_70_range__97_102_char_class___range__48_57_range__65_70_range__97_102_char_class___range__48_57_range__65_70_range__97_102_char_class___range__48_57_range__65_70_range__97_102_ = (IConstructor) _read("prod(label(\"utf32\",lex(\"UnicodeEscape\")),[lit(\"\\\\\"),\\char-class([range(85,85)]),\\char-class([range(48,57),range(65,70),range(97,102)]),\\char-class([range(48,57),range(65,70),range(97,102)]),\\char-class([range(48,57),range(65,70),range(97,102)]),\\char-class([range(48,57),range(65,70),range(97,102)]),\\char-class([range(48,57),range(65,70),range(97,102)]),\\char-class([range(48,57),range(65,70),range(97,102)])],{})", Factory.Production);
  private static final IConstructor prod__NotPrecede_Sym__match_Sym_layouts_LAYOUTLIST_lit___33_60_60_layouts_LAYOUTLIST_symbol_Sym__assoc__right = (IConstructor) _read("prod(label(\"NotPrecede\",sort(\"Sym\")),[label(\"match\",sort(\"Sym\")),layouts(\"LAYOUTLIST\"),lit(\"!\\<\\<\"),layouts(\"LAYOUTLIST\"),label(\"symbol\",sort(\"Sym\"))],{assoc(right())})", Factory.Production);
  private static final IConstructor prod__Declaration_Command__declaration_Declaration_ = (IConstructor) _read("prod(label(\"Declaration\",sort(\"Command\")),[label(\"declaration\",sort(\"Declaration\"))],{})", Factory.Production);
  private static final IConstructor prod__Subtraction_Expression__lhs_Expression_layouts_LAYOUTLIST_lit___layouts_LAYOUTLIST_rhs_Expression__assoc__left = (IConstructor) _read("prod(label(\"Subtraction\",sort(\"Expression\")),[label(\"lhs\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"-\"),layouts(\"LAYOUTLIST\"),label(\"rhs\",sort(\"Expression\"))],{assoc(left())})", Factory.Production);
  private static final IConstructor prod__lit_any__char_class___range__97_97_char_class___range__110_110_char_class___range__121_121_ = (IConstructor) _read("prod(lit(\"any\"),[\\char-class([range(97,97)]),\\char-class([range(110,110)]),\\char-class([range(121,121)])],{})", Factory.Production);
  private static final IConstructor prod__lit_join__char_class___range__106_106_char_class___range__111_111_char_class___range__105_105_char_class___range__110_110_ = (IConstructor) _read("prod(lit(\"join\"),[\\char-class([range(106,106)]),\\char-class([range(111,111)]),\\char-class([range(105,105)]),\\char-class([range(110,110)])],{})", Factory.Production);
  private static final IConstructor prod__GreaterThan_Expression__lhs_Expression_layouts_LAYOUTLIST_lit___62_layouts_LAYOUTLIST_rhs_Expression__assoc__non_assoc = (IConstructor) _read("prod(label(\"GreaterThan\",sort(\"Expression\")),[label(\"lhs\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"\\>\"),layouts(\"LAYOUTLIST\"),label(\"rhs\",sort(\"Expression\"))],{assoc(\\non-assoc())})", Factory.Production);
  private static final IConstructor prod__Interpolated_ProtocolPart__pre_PreProtocolChars_layouts_LAYOUTLIST_expression_Expression_layouts_LAYOUTLIST_tail_ProtocolTail_ = (IConstructor) _read("prod(label(\"Interpolated\",sort(\"ProtocolPart\")),[label(\"pre\",lex(\"PreProtocolChars\")),layouts(\"LAYOUTLIST\"),label(\"expression\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),label(\"tail\",sort(\"ProtocolTail\"))],{})", Factory.Production);
  private static final IConstructor prod__lit___124_124__char_class___range__124_124_char_class___range__124_124_ = (IConstructor) _read("prod(lit(\"||\"),[\\char-class([range(124,124)]),\\char-class([range(124,124)])],{})", Factory.Production);
  private static final IConstructor prod__lit___60_61__char_class___range__60_60_char_class___range__61_61_ = (IConstructor) _read("prod(lit(\"\\<=\"),[\\char-class([range(60,60)]),\\char-class([range(61,61)])],{})", Factory.Production);
  private static final IConstructor prod__lit_finally__char_class___range__102_102_char_class___range__105_105_char_class___range__110_110_char_class___range__97_97_char_class___range__108_108_char_class___range__108_108_char_class___range__121_121_ = (IConstructor) _read("prod(lit(\"finally\"),[\\char-class([range(102,102)]),\\char-class([range(105,105)]),\\char-class([range(110,110)]),\\char-class([range(97,97)]),\\char-class([range(108,108)]),\\char-class([range(108,108)]),\\char-class([range(121,121)])],{})", Factory.Production);
  private static final IConstructor prod__PostProtocolChars__lit___62_URLChars_lit___58_47_47_ = (IConstructor) _read("prod(lex(\"PostProtocolChars\"),[lit(\"\\>\"),lex(\"URLChars\"),lit(\"://\")],{})", Factory.Production);
  private static final IConstructor prod__lit___60_60__char_class___range__60_60_char_class___range__60_60_ = (IConstructor) _read("prod(lit(\"\\<\\<\"),[\\char-class([range(60,60)]),\\char-class([range(60,60)])],{})", Factory.Production);
  private static final IConstructor prod__HeaderKeyword__lit_extend_ = (IConstructor) _read("prod(keywords(\"HeaderKeyword\"),[lit(\"extend\")],{})", Factory.Production);
  private static final IConstructor prod__WithThrows_Signature__modifiers_FunctionModifiers_layouts_LAYOUTLIST_type_Type_layouts_LAYOUTLIST_name_Name_layouts_LAYOUTLIST_parameters_Parameters_layouts_LAYOUTLIST_lit_throws_layouts_LAYOUTLIST_exceptions_iter_seps__Type__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_ = (IConstructor) _read("prod(label(\"WithThrows\",sort(\"Signature\")),[label(\"modifiers\",sort(\"FunctionModifiers\")),layouts(\"LAYOUTLIST\"),label(\"type\",sort(\"Type\")),layouts(\"LAYOUTLIST\"),label(\"name\",lex(\"Name\")),layouts(\"LAYOUTLIST\"),label(\"parameters\",sort(\"Parameters\")),layouts(\"LAYOUTLIST\"),lit(\"throws\"),layouts(\"LAYOUTLIST\"),label(\"exceptions\",\\iter-seps(sort(\"Type\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")]))],{})", Factory.Production);
  private static final IConstructor prod__Bool_BasicType__lit_bool_ = (IConstructor) _read("prod(label(\"Bool\",sort(\"BasicType\")),[lit(\"bool\")],{})", Factory.Production);
  private static final IConstructor prod__Alias_Declaration__tags_Tags_layouts_LAYOUTLIST_visibility_Visibility_layouts_LAYOUTLIST_lit_alias_layouts_LAYOUTLIST_user_UserType_layouts_LAYOUTLIST_lit___61_layouts_LAYOUTLIST_base_Type_layouts_LAYOUTLIST_lit___59_ = (IConstructor) _read("prod(label(\"Alias\",sort(\"Declaration\")),[label(\"tags\",sort(\"Tags\")),layouts(\"LAYOUTLIST\"),label(\"visibility\",sort(\"Visibility\")),layouts(\"LAYOUTLIST\"),lit(\"alias\"),layouts(\"LAYOUTLIST\"),label(\"user\",sort(\"UserType\")),layouts(\"LAYOUTLIST\"),lit(\"=\"),layouts(\"LAYOUTLIST\"),label(\"base\",sort(\"Type\")),layouts(\"LAYOUTLIST\"),lit(\";\")],{})", Factory.Production);
  private static final IConstructor prod__RascalKeywords__lit_value_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"value\")],{})", Factory.Production);
  private static final IConstructor prod__lit_import__char_class___range__105_105_char_class___range__109_109_char_class___range__112_112_char_class___range__111_111_char_class___range__114_114_char_class___range__116_116_ = (IConstructor) _read("prod(lit(\"import\"),[\\char-class([range(105,105)]),\\char-class([range(109,109)]),\\char-class([range(112,112)]),\\char-class([range(111,111)]),\\char-class([range(114,114)]),\\char-class([range(116,116)])],{})", Factory.Production);
  private static final IConstructor prod__lit_else__char_class___range__101_101_char_class___range__108_108_char_class___range__115_115_char_class___range__101_101_ = (IConstructor) _read("prod(lit(\"else\"),[\\char-class([range(101,101)]),\\char-class([range(108,108)]),\\char-class([range(115,115)]),\\char-class([range(101,101)])],{})", Factory.Production);
  private static final IConstructor prod__lit_global__char_class___range__103_103_char_class___range__108_108_char_class___range__111_111_char_class___range__98_98_char_class___range__97_97_char_class___range__108_108_ = (IConstructor) _read("prod(lit(\"global\"),[\\char-class([range(103,103)]),\\char-class([range(108,108)]),\\char-class([range(111,111)]),\\char-class([range(98,98)]),\\char-class([range(97,97)]),\\char-class([range(108,108)])],{})", Factory.Production);
  private static final IConstructor prod__NonAssociative_Assoc__lit_non_assoc_ = (IConstructor) _read("prod(label(\"NonAssociative\",sort(\"Assoc\")),[lit(\"non-assoc\")],{})", Factory.Production);
  private static final IConstructor prod__Outermost_Strategy__lit_outermost_ = (IConstructor) _read("prod(label(\"Outermost\",sort(\"Strategy\")),[lit(\"outermost\")],{})", Factory.Production);
  private static final IConstructor prod__Template_StringLiteral__pre_PreStringChars_layouts_LAYOUTLIST_template_StringTemplate_layouts_LAYOUTLIST_tail_StringTail_ = (IConstructor) _read("prod(label(\"Template\",sort(\"StringLiteral\")),[label(\"pre\",lex(\"PreStringChars\")),layouts(\"LAYOUTLIST\"),label(\"template\",sort(\"StringTemplate\")),layouts(\"LAYOUTLIST\"),label(\"tail\",sort(\"StringTail\"))],{})", Factory.Production);
  private static final IConstructor regular__iter_star__alt___conditional__char_class___range__42_42__not_follow__char_class___range__47_47_char_class___range__0_41_range__43_16777215 = (IConstructor) _read("regular(\\iter-star(alt({conditional(\\char-class([range(42,42)]),{\\not-follow(\\char-class([range(47,47)]))}),\\char-class([range(0,41),range(43,16777215)])})))", Factory.Production);
  private static final IConstructor prod__LessThanOrEq_Expression__lhs_Expression_layouts_LAYOUTLIST_lit___60_61_layouts_LAYOUTLIST_rhs_Expression__assoc__non_assoc = (IConstructor) _read("prod(label(\"LessThanOrEq\",sort(\"Expression\")),[label(\"lhs\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"\\<=\"),layouts(\"LAYOUTLIST\"),label(\"rhs\",sort(\"Expression\"))],{assoc(\\non-assoc())})", Factory.Production);
  private static final IConstructor regular__iter_star_seps__FunctionModifier__layouts_LAYOUTLIST = (IConstructor) _read("regular(\\iter-star-seps(sort(\"FunctionModifier\"),[layouts(\"LAYOUTLIST\")]))", Factory.Production);
  private static final IConstructor prod__Any_Expression__lit_any_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_generators_iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_ = (IConstructor) _read("prod(label(\"Any\",sort(\"Expression\")),[lit(\"any\"),layouts(\"LAYOUTLIST\"),lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"generators\",\\iter-seps(sort(\"Expression\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\")\")],{})", Factory.Production);
  private static final IConstructor prod__PostPathChars__lit___62_URLChars_lit___124_ = (IConstructor) _read("prod(lex(\"PostPathChars\"),[lit(\"\\>\"),lex(\"URLChars\"),lit(\"|\")],{})", Factory.Production);
  private static final IConstructor prod__Tag_Declaration__tags_Tags_layouts_LAYOUTLIST_visibility_Visibility_layouts_LAYOUTLIST_lit_tag_layouts_LAYOUTLIST_kind_Kind_layouts_LAYOUTLIST_name_Name_layouts_LAYOUTLIST_lit_on_layouts_LAYOUTLIST_types_iter_seps__Type__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___59_ = (IConstructor) _read("prod(label(\"Tag\",sort(\"Declaration\")),[label(\"tags\",sort(\"Tags\")),layouts(\"LAYOUTLIST\"),label(\"visibility\",sort(\"Visibility\")),layouts(\"LAYOUTLIST\"),lit(\"tag\"),layouts(\"LAYOUTLIST\"),label(\"kind\",sort(\"Kind\")),layouts(\"LAYOUTLIST\"),label(\"name\",lex(\"Name\")),layouts(\"LAYOUTLIST\"),lit(\"on\"),layouts(\"LAYOUTLIST\"),label(\"types\",\\iter-seps(sort(\"Type\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\";\")],{})", Factory.Production);
  private static final IConstructor prod__Intersection_Class__lhs_Class_layouts_LAYOUTLIST_lit___38_38_layouts_LAYOUTLIST_rhs_Class__assoc__left = (IConstructor) _read("prod(label(\"Intersection\",sort(\"Class\")),[label(\"lhs\",sort(\"Class\")),layouts(\"LAYOUTLIST\"),lit(\"&&\"),layouts(\"LAYOUTLIST\"),label(\"rhs\",sort(\"Class\"))],{assoc(left())})", Factory.Production);
  private static final IConstructor prod__Keyword_SyntaxDefinition__lit_keyword_layouts_LAYOUTLIST_defined_Sym_layouts_LAYOUTLIST_lit___61_layouts_LAYOUTLIST_production_Prod_layouts_LAYOUTLIST_lit___59__tag__Foldable = (IConstructor) _read("prod(label(\"Keyword\",sort(\"SyntaxDefinition\")),[lit(\"keyword\"),layouts(\"LAYOUTLIST\"),label(\"defined\",sort(\"Sym\")),layouts(\"LAYOUTLIST\"),lit(\"=\"),layouts(\"LAYOUTLIST\"),label(\"production\",sort(\"Prod\")),layouts(\"LAYOUTLIST\"),lit(\";\")],{tag(Foldable())})", Factory.Production);
  private static final IConstructor prod__AsType_Expression__lit___91_layouts_LAYOUTLIST_type_Type_layouts_LAYOUTLIST_lit___93_layouts_LAYOUTLIST_argument_Expression_ = (IConstructor) _read("prod(label(\"AsType\",sort(\"Expression\")),[lit(\"[\"),layouts(\"LAYOUTLIST\"),label(\"type\",sort(\"Type\")),layouts(\"LAYOUTLIST\"),lit(\"]\"),layouts(\"LAYOUTLIST\"),label(\"argument\",sort(\"Expression\"))],{})", Factory.Production);
  private static final IConstructor prod__While_StringTemplate__lit_while_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_condition_Expression_layouts_LAYOUTLIST_lit___41_layouts_LAYOUTLIST_lit___123_layouts_LAYOUTLIST_preStats_iter_star_seps__Statement__layouts_LAYOUTLIST_layouts_LAYOUTLIST_body_StringMiddle_layouts_LAYOUTLIST_postStats_iter_star_seps__Statement__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_ = (IConstructor) _read("prod(label(\"While\",sort(\"StringTemplate\")),[lit(\"while\"),layouts(\"LAYOUTLIST\"),lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"condition\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\")\"),layouts(\"LAYOUTLIST\"),lit(\"{\"),layouts(\"LAYOUTLIST\"),label(\"preStats\",\\iter-star-seps(sort(\"Statement\"),[layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),label(\"body\",sort(\"StringMiddle\")),layouts(\"LAYOUTLIST\"),label(\"postStats\",\\iter-star-seps(sort(\"Statement\"),[layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\"}\")],{})", Factory.Production);
  private static final IConstructor prod__RascalKeywords__lit_non_assoc_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"non-assoc\")],{})", Factory.Production);
  private static final IConstructor regular__iter_seps__Pattern__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST = (IConstructor) _read("regular(\\iter-seps(sort(\"Pattern\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")]))", Factory.Production);
  private static final IConstructor prod__In_Expression__lhs_Expression_layouts_LAYOUTLIST_lit_in_layouts_LAYOUTLIST_rhs_Expression__assoc__non_assoc = (IConstructor) _read("prod(label(\"In\",sort(\"Expression\")),[label(\"lhs\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"in\"),layouts(\"LAYOUTLIST\"),label(\"rhs\",sort(\"Expression\"))],{assoc(\\non-assoc())})", Factory.Production);
  private static final IConstructor regular__iter_seps__Statement__layouts_LAYOUTLIST = (IConstructor) _read("regular(\\iter-seps(sort(\"Statement\"),[layouts(\"LAYOUTLIST\")]))", Factory.Production);
  private static final IConstructor prod__Default_Label__name_Name_layouts_LAYOUTLIST_lit___58_ = (IConstructor) _read("prod(label(\"Default\",sort(\"Label\")),[label(\"name\",lex(\"Name\")),layouts(\"LAYOUTLIST\"),lit(\":\")],{})", Factory.Production);
  private static final IConstructor prod__All_Kind__lit_all_ = (IConstructor) _read("prod(label(\"All\",sort(\"Kind\")),[lit(\"all\")],{})", Factory.Production);
  private static final IConstructor prod__lit_keyword__char_class___range__107_107_char_class___range__101_101_char_class___range__121_121_char_class___range__119_119_char_class___range__111_111_char_class___range__114_114_char_class___range__100_100_ = (IConstructor) _read("prod(lit(\"keyword\"),[\\char-class([range(107,107)]),\\char-class([range(101,101)]),\\char-class([range(121,121)]),\\char-class([range(119,119)]),\\char-class([range(111,111)]),\\char-class([range(114,114)]),\\char-class([range(100,100)])],{})", Factory.Production);
  private static final IConstructor prod__NamedBackslash__conditional__char_class___range__92_92__not_follow__char_class___range__60_60_range__62_62_range__92_92_ = (IConstructor) _read("prod(lex(\"NamedBackslash\"),[conditional(\\char-class([range(92,92)]),{\\not-follow(\\char-class([range(60,60),range(62,62),range(92,92)]))})],{})", Factory.Production);
  private static final IConstructor prod__NoThrows_Signature__modifiers_FunctionModifiers_layouts_LAYOUTLIST_type_Type_layouts_LAYOUTLIST_name_Name_layouts_LAYOUTLIST_parameters_Parameters_ = (IConstructor) _read("prod(label(\"NoThrows\",sort(\"Signature\")),[label(\"modifiers\",sort(\"FunctionModifiers\")),layouts(\"LAYOUTLIST\"),label(\"type\",sort(\"Type\")),layouts(\"LAYOUTLIST\"),label(\"name\",lex(\"Name\")),layouts(\"LAYOUTLIST\"),label(\"parameters\",sort(\"Parameters\"))],{})", Factory.Production);
  private static final IConstructor prod__lit_syntax__char_class___range__115_115_char_class___range__121_121_char_class___range__110_110_char_class___range__116_116_char_class___range__97_97_char_class___range__120_120_ = (IConstructor) _read("prod(lit(\"syntax\"),[\\char-class([range(115,115)]),\\char-class([range(121,121)]),\\char-class([range(110,110)]),\\char-class([range(116,116)]),\\char-class([range(97,97)]),\\char-class([range(120,120)])],{})", Factory.Production);
  private static final IConstructor prod__lit_all__char_class___range__97_97_char_class___range__108_108_char_class___range__108_108_ = (IConstructor) _read("prod(lit(\"all\"),[\\char-class([range(97,97)]),\\char-class([range(108,108)]),\\char-class([range(108,108)])],{})", Factory.Production);
  private static final IConstructor prod__DecimalIntegerLiteral__char_class___range__49_57_conditional__iter_star__char_class___range__48_57__not_follow__char_class___range__48_57_range__65_90_range__95_95_range__97_122_ = (IConstructor) _read("prod(lex(\"DecimalIntegerLiteral\"),[\\char-class([range(49,57)]),conditional(\\iter-star(\\char-class([range(48,57)])),{\\not-follow(\\char-class([range(48,57),range(65,90),range(95,95),range(97,122)]))})],{})", Factory.Production);
  private static final IConstructor prod__Default_Declarator__type_Type_layouts_LAYOUTLIST_variables_iter_seps__Variable__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_ = (IConstructor) _read("prod(label(\"Default\",sort(\"Declarator\")),[label(\"type\",sort(\"Type\")),layouts(\"LAYOUTLIST\"),label(\"variables\",\\iter-seps(sort(\"Variable\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")]))],{})", Factory.Production);
  private static final IConstructor prod__Function_Declaration__functionDeclaration_FunctionDeclaration_ = (IConstructor) _read("prod(label(\"Function\",sort(\"Declaration\")),[label(\"functionDeclaration\",sort(\"FunctionDeclaration\"))],{})", Factory.Production);
  private static final IConstructor prod__lit___62_62__char_class___range__62_62_char_class___range__62_62_ = (IConstructor) _read("prod(lit(\"\\>\\>\"),[\\char-class([range(62,62)]),\\char-class([range(62,62)])],{})", Factory.Production);
  private static final IConstructor prod__lit_try__char_class___range__116_116_char_class___range__114_114_char_class___range__121_121_ = (IConstructor) _read("prod(lit(\"try\"),[\\char-class([range(116,116)]),\\char-class([range(114,114)]),\\char-class([range(121,121)])],{})", Factory.Production);
  private static final IConstructor prod__lit___63_61__char_class___range__63_63_char_class___range__61_61_ = (IConstructor) _read("prod(lit(\"?=\"),[\\char-class([range(63,63)]),\\char-class([range(61,61)])],{})", Factory.Production);
  private static final IConstructor prod__lit_bottom_up__char_class___range__98_98_char_class___range__111_111_char_class___range__116_116_char_class___range__116_116_char_class___range__111_111_char_class___range__109_109_char_class___range__45_45_char_class___range__117_117_char_class___range__112_112_ = (IConstructor) _read("prod(lit(\"bottom-up\"),[\\char-class([range(98,98)]),\\char-class([range(111,111)]),\\char-class([range(116,116)]),\\char-class([range(116,116)]),\\char-class([range(111,111)]),\\char-class([range(109,109)]),\\char-class([range(45,45)]),\\char-class([range(117,117)]),\\char-class([range(112,112)])],{})", Factory.Production);
  private static final IConstructor prod__RascalKeywords__lit_false_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"false\")],{})", Factory.Production);
  private static final IConstructor prod__lit_break__char_class___range__98_98_char_class___range__114_114_char_class___range__101_101_char_class___range__97_97_char_class___range__107_107_ = (IConstructor) _read("prod(lit(\"break\"),[\\char-class([range(98,98)]),\\char-class([range(114,114)]),\\char-class([range(101,101)]),\\char-class([range(97,97)]),\\char-class([range(107,107)])],{})", Factory.Production);
  private static final IConstructor prod__IfThenElse_Statement__label_Label_layouts_LAYOUTLIST_lit_if_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_conditions_iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_layouts_LAYOUTLIST_thenStatement_Statement_layouts_LAYOUTLIST_lit_else_layouts_LAYOUTLIST_elseStatement_Statement_ = (IConstructor) _read("prod(label(\"IfThenElse\",sort(\"Statement\")),[label(\"label\",sort(\"Label\")),layouts(\"LAYOUTLIST\"),lit(\"if\"),layouts(\"LAYOUTLIST\"),lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"conditions\",\\iter-seps(sort(\"Expression\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\")\"),layouts(\"LAYOUTLIST\"),label(\"thenStatement\",sort(\"Statement\")),layouts(\"LAYOUTLIST\"),lit(\"else\"),layouts(\"LAYOUTLIST\"),label(\"elseStatement\",sort(\"Statement\"))],{})", Factory.Production);
  private static final IConstructor regular__iter_seps__Type__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST = (IConstructor) _read("regular(\\iter-seps(sort(\"Type\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")]))", Factory.Production);
  private static final IConstructor prod__HeaderKeyword__lit_lexical_ = (IConstructor) _read("prod(keywords(\"HeaderKeyword\"),[lit(\"lexical\")],{})", Factory.Production);
  private static final IConstructor prod__RascalKeywords__lit_tuple_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"tuple\")],{})", Factory.Production);
  private static final IConstructor prod__JustTime__lit___36_84_TimePartNoTZ_TimeZonePart_ = (IConstructor) _read("prod(lex(\"JustTime\"),[lit(\"$T\"),lex(\"TimePartNoTZ\"),lex(\"TimeZonePart\")],{})", Factory.Production);
  private static final IConstructor prod__lit_true__char_class___range__116_116_char_class___range__114_114_char_class___range__117_117_char_class___range__101_101_ = (IConstructor) _read("prod(lit(\"true\"),[\\char-class([range(116,116)]),\\char-class([range(114,114)]),\\char-class([range(117,117)]),\\char-class([range(101,101)])],{})", Factory.Production);
  private static final IConstructor prod__History_ShellCommand__lit_history_ = (IConstructor) _read("prod(label(\"History\",sort(\"ShellCommand\")),[lit(\"history\")],{})", Factory.Production);
  private static final IConstructor prod__TopDownBreak_Strategy__lit_top_down_break_ = (IConstructor) _read("prod(label(\"TopDownBreak\",sort(\"Strategy\")),[lit(\"top-down-break\")],{})", Factory.Production);
  private static final IConstructor prod__start__Command__layouts_LAYOUTLIST_top_Command_layouts_LAYOUTLIST_ = (IConstructor) _read("prod(start(sort(\"Command\")),[layouts(\"LAYOUTLIST\"),label(\"top\",sort(\"Command\")),layouts(\"LAYOUTLIST\")],{})", Factory.Production);
  private static final IConstructor prod__HeaderKeyword__lit_syntax_ = (IConstructor) _read("prod(keywords(\"HeaderKeyword\"),[lit(\"syntax\")],{})", Factory.Production);
  private static final IConstructor prod__DateTime_BasicType__lit_datetime_ = (IConstructor) _read("prod(label(\"DateTime\",sort(\"BasicType\")),[lit(\"datetime\")],{})", Factory.Production);
  private static final IConstructor prod__lit_extend__char_class___range__101_101_char_class___range__120_120_char_class___range__116_116_char_class___range__101_101_char_class___range__110_110_char_class___range__100_100_ = (IConstructor) _read("prod(lit(\"extend\"),[\\char-class([range(101,101)]),\\char-class([range(120,120)]),\\char-class([range(116,116)]),\\char-class([range(101,101)]),\\char-class([range(110,110)]),\\char-class([range(100,100)])],{})", Factory.Production);
  private static final IConstructor prod__lit_append__char_class___range__97_97_char_class___range__112_112_char_class___range__112_112_char_class___range__101_101_char_class___range__110_110_char_class___range__100_100_ = (IConstructor) _read("prod(lit(\"append\"),[\\char-class([range(97,97)]),\\char-class([range(112,112)]),\\char-class([range(112,112)]),\\char-class([range(101,101)]),\\char-class([range(110,110)]),\\char-class([range(100,100)])],{})", Factory.Production);
  private static final IConstructor prod__Extend_Import__lit_extend_layouts_LAYOUTLIST_module_ImportedModule_layouts_LAYOUTLIST_lit___59_ = (IConstructor) _read("prod(label(\"Extend\",sort(\"Import\")),[lit(\"extend\"),layouts(\"LAYOUTLIST\"),label(\"module\",sort(\"ImportedModule\")),layouts(\"LAYOUTLIST\"),lit(\";\")],{})", Factory.Production);
  private static final IConstructor prod__Alias_Kind__lit_alias_ = (IConstructor) _read("prod(label(\"Alias\",sort(\"Kind\")),[lit(\"alias\")],{})", Factory.Production);
  private static final IConstructor prod__All_Expression__lit_all_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_generators_iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_ = (IConstructor) _read("prod(label(\"All\",sort(\"Expression\")),[lit(\"all\"),layouts(\"LAYOUTLIST\"),lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"generators\",\\iter-seps(sort(\"Expression\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\")\")],{})", Factory.Production);
  private static final IConstructor prod__Default_Mapping__Pattern__from_Pattern_layouts_LAYOUTLIST_lit___58_layouts_LAYOUTLIST_to_Pattern_ = (IConstructor) _read("prod(label(\"Default\",\\parameterized-sort(\"Mapping\",[sort(\"Pattern\")])),[label(\"from\",sort(\"Pattern\")),layouts(\"LAYOUTLIST\"),lit(\":\"),layouts(\"LAYOUTLIST\"),label(\"to\",sort(\"Pattern\"))],{})", Factory.Production);
  private static final IConstructor prod__lit_assoc__char_class___range__97_97_char_class___range__115_115_char_class___range__115_115_char_class___range__111_111_char_class___range__99_99_ = (IConstructor) _read("prod(lit(\"assoc\"),[\\char-class([range(97,97)]),\\char-class([range(115,115)]),\\char-class([range(115,115)]),\\char-class([range(111,111)]),\\char-class([range(99,99)])],{})", Factory.Production);
  private static final IConstructor prod__Continue_Statement__lit_continue_layouts_LAYOUTLIST_target_Target_layouts_LAYOUTLIST_lit___59_ = (IConstructor) _read("prod(label(\"Continue\",sort(\"Statement\")),[lit(\"continue\"),layouts(\"LAYOUTLIST\"),label(\"target\",sort(\"Target\")),layouts(\"LAYOUTLIST\"),lit(\";\")],{})", Factory.Production);
  private static final IConstructor prod__Tag_Kind__lit_tag_ = (IConstructor) _read("prod(label(\"Tag\",sort(\"Kind\")),[lit(\"tag\")],{})", Factory.Production);
  private static final IConstructor prod__lit_map__char_class___range__109_109_char_class___range__97_97_char_class___range__112_112_ = (IConstructor) _read("prod(lit(\"map\"),[\\char-class([range(109,109)]),\\char-class([range(97,97)]),\\char-class([range(112,112)])],{})", Factory.Production);
  private static final IConstructor prod__lit_real__char_class___range__114_114_char_class___range__101_101_char_class___range__97_97_char_class___range__108_108_ = (IConstructor) _read("prod(lit(\"real\"),[\\char-class([range(114,114)]),\\char-class([range(101,101)]),\\char-class([range(97,97)]),\\char-class([range(108,108)])],{})", Factory.Production);
  private static final IConstructor regular__iter_star_seps__Mapping__Pattern__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST = (IConstructor) _read("regular(\\iter-star-seps(\\parameterized-sort(\"Mapping\",[sort(\"Pattern\")]),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")]))", Factory.Production);
  private static final IConstructor regular__iter_star__char_class___range__45_45_range__48_57_range__65_90_range__95_95_range__97_122 = (IConstructor) _read("regular(\\iter-star(\\char-class([range(45,45),range(48,57),range(65,90),range(95,95),range(97,122)])))", Factory.Production);
  private static final IConstructor prod__Division_Assignment__lit___47_61_ = (IConstructor) _read("prod(label(\"Division\",sort(\"Assignment\")),[lit(\"/=\")],{})", Factory.Production);
  private static final IConstructor prod__Product_Assignment__lit___42_61_ = (IConstructor) _read("prod(label(\"Product\",sort(\"Assignment\")),[lit(\"*=\")],{})", Factory.Production);
  private static final IConstructor prod__PostStringChars__char_class___range__62_62_iter_star__StringCharacter_char_class___range__34_34__tag__category___67_111_110_115_116_97_110_116 = (IConstructor) _read("prod(lex(\"PostStringChars\"),[\\char-class([range(62,62)]),\\iter-star(lex(\"StringCharacter\")),\\char-class([range(34,34)])],{tag(category(\"Constant\"))})", Factory.Production);
  private static final IConstructor prod__lit_void__char_class___range__118_118_char_class___range__111_111_char_class___range__105_105_char_class___range__100_100_ = (IConstructor) _read("prod(lit(\"void\"),[\\char-class([range(118,118)]),\\char-class([range(111,111)]),\\char-class([range(105,105)]),\\char-class([range(100,100)])],{})", Factory.Production);
  private static final IConstructor prod__ascii_UnicodeEscape__lit___92_char_class___range__97_97_char_class___range__48_55_char_class___range__48_57_range__65_70_range__97_102_ = (IConstructor) _read("prod(label(\"ascii\",lex(\"UnicodeEscape\")),[lit(\"\\\\\"),\\char-class([range(97,97)]),\\char-class([range(48,55)]),\\char-class([range(48,57),range(65,70),range(97,102)])],{})", Factory.Production);
  private static final IConstructor prod__FromTo_Range__start_Char_layouts_LAYOUTLIST_lit___layouts_LAYOUTLIST_end_Char_ = (IConstructor) _read("prod(label(\"FromTo\",sort(\"Range\")),[label(\"start\",lex(\"Char\")),layouts(\"LAYOUTLIST\"),lit(\"-\"),layouts(\"LAYOUTLIST\"),label(\"end\",lex(\"Char\"))],{})", Factory.Production);
  private static final IConstructor prod__TimeZonePart__char_class___range__43_43_range__45_45_char_class___range__48_49_char_class___range__48_57_ = (IConstructor) _read("prod(lex(\"TimeZonePart\"),[\\char-class([range(43,43),range(45,45)]),\\char-class([range(48,49)]),\\char-class([range(48,57)])],{})", Factory.Production);
  private static final IConstructor prod__Map_Comprehension__lit___40_layouts_LAYOUTLIST_from_Expression_layouts_LAYOUTLIST_lit___58_layouts_LAYOUTLIST_to_Expression_layouts_LAYOUTLIST_lit___124_layouts_LAYOUTLIST_generators_iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_ = (IConstructor) _read("prod(label(\"Map\",sort(\"Comprehension\")),[lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"from\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\":\"),layouts(\"LAYOUTLIST\"),label(\"to\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"|\"),layouts(\"LAYOUTLIST\"),label(\"generators\",\\iter-seps(sort(\"Expression\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\")\")],{})", Factory.Production);
  private static final IConstructor prod__Default_PreModule__header_Header_layouts_LAYOUTLIST_conditional__empty__not_follow__HeaderKeyword_layouts_LAYOUTLIST_rest_Rest_ = (IConstructor) _read("prod(label(\"Default\",sort(\"PreModule\")),[label(\"header\",sort(\"Header\")),layouts(\"LAYOUTLIST\"),conditional(empty(),{\\not-follow(keywords(\"HeaderKeyword\"))}),layouts(\"LAYOUTLIST\"),label(\"rest\",lex(\"Rest\"))],{})", Factory.Production);
  private static final IConstructor prod__Shell_Command__lit___58_layouts_LAYOUTLIST_command_ShellCommand_ = (IConstructor) _read("prod(label(\"Shell\",sort(\"Command\")),[lit(\":\"),layouts(\"LAYOUTLIST\"),label(\"command\",sort(\"ShellCommand\"))],{})", Factory.Production);
  private static final IConstructor prod__Quit_ShellCommand__lit_quit_ = (IConstructor) _read("prod(label(\"Quit\",sort(\"ShellCommand\")),[lit(\"quit\")],{})", Factory.Production);
  private static final IConstructor prod__empty__ = (IConstructor) _read("prod(empty(),[],{})", Factory.Production);
  private static final IConstructor prod__DateAndTimeLiteral_DateTimeLiteral__dateAndTime_DateAndTime_ = (IConstructor) _read("prod(label(\"DateAndTimeLiteral\",sort(\"DateTimeLiteral\")),[label(\"dateAndTime\",lex(\"DateAndTime\"))],{})", Factory.Production);
  private static final IConstructor prod__ReifiedType_Expression__lit_type_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_symbol_Expression_layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_definitions_Expression_layouts_LAYOUTLIST_lit___41_ = (IConstructor) _read("prod(label(\"ReifiedType\",sort(\"Expression\")),[lit(\"type\"),layouts(\"LAYOUTLIST\"),lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"symbol\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\"),label(\"definitions\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\")\")],{})", Factory.Production);
  private static final IConstructor prod__Negative_Expression__lit___layouts_LAYOUTLIST_argument_Expression_ = (IConstructor) _read("prod(label(\"Negative\",sort(\"Expression\")),[lit(\"-\"),layouts(\"LAYOUTLIST\"),label(\"argument\",sort(\"Expression\"))],{})", Factory.Production);
  private static final IConstructor prod__TopDown_Strategy__lit_top_down_ = (IConstructor) _read("prod(label(\"TopDown\",sort(\"Strategy\")),[lit(\"top-down\")],{})", Factory.Production);
  private static final IConstructor prod__lit_switch__char_class___range__115_115_char_class___range__119_119_char_class___range__105_105_char_class___range__116_116_char_class___range__99_99_char_class___range__104_104_ = (IConstructor) _read("prod(lit(\"switch\"),[\\char-class([range(115,115)]),\\char-class([range(119,119)]),\\char-class([range(105,105)]),\\char-class([range(116,116)]),\\char-class([range(99,99)]),\\char-class([range(104,104)])],{})", Factory.Production);
  private static final IConstructor prod__DefaultStrategy_Visit__lit_visit_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_subject_Expression_layouts_LAYOUTLIST_lit___41_layouts_LAYOUTLIST_lit___123_layouts_LAYOUTLIST_cases_iter_seps__Case__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_ = (IConstructor) _read("prod(label(\"DefaultStrategy\",sort(\"Visit\")),[lit(\"visit\"),layouts(\"LAYOUTLIST\"),lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"subject\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\")\"),layouts(\"LAYOUTLIST\"),lit(\"{\"),layouts(\"LAYOUTLIST\"),label(\"cases\",\\iter-seps(sort(\"Case\"),[layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\"}\")],{})", Factory.Production);
  private static final IConstructor prod__Default_Parameters__lit___40_layouts_LAYOUTLIST_formals_Formals_layouts_LAYOUTLIST_lit___41_ = (IConstructor) _read("prod(label(\"Default\",sort(\"Parameters\")),[lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"formals\",sort(\"Formals\")),layouts(\"LAYOUTLIST\"),lit(\")\")],{})", Factory.Production);
  private static final IConstructor prod__lit_solve__char_class___range__115_115_char_class___range__111_111_char_class___range__108_108_char_class___range__118_118_char_class___range__101_101_ = (IConstructor) _read("prod(lit(\"solve\"),[\\char-class([range(115,115)]),\\char-class([range(111,111)]),\\char-class([range(108,108)]),\\char-class([range(118,118)]),\\char-class([range(101,101)])],{})", Factory.Production);
  private static final IConstructor prod__Lexical_SyntaxDefinition__lit_lexical_layouts_LAYOUTLIST_defined_Sym_layouts_LAYOUTLIST_lit___61_layouts_LAYOUTLIST_production_Prod_layouts_LAYOUTLIST_lit___59__tag__Foldable = (IConstructor) _read("prod(label(\"Lexical\",sort(\"SyntaxDefinition\")),[lit(\"lexical\"),layouts(\"LAYOUTLIST\"),label(\"defined\",sort(\"Sym\")),layouts(\"LAYOUTLIST\"),lit(\"=\"),layouts(\"LAYOUTLIST\"),label(\"production\",sort(\"Prod\")),layouts(\"LAYOUTLIST\"),lit(\";\")],{tag(Foldable())})", Factory.Production);
  private static final IConstructor prod__lit_layout__char_class___range__108_108_char_class___range__97_97_char_class___range__121_121_char_class___range__111_111_char_class___range__117_117_char_class___range__116_116_ = (IConstructor) _read("prod(lit(\"layout\"),[\\char-class([range(108,108)]),\\char-class([range(97,97)]),\\char-class([range(121,121)]),\\char-class([range(111,111)]),\\char-class([range(117,117)]),\\char-class([range(116,116)])],{})", Factory.Production);
  private static final IConstructor regular__seq___char_class___range__48_57_opt__char_class___range__48_57 = (IConstructor) _read("regular(seq([\\char-class([range(48,57)]),opt(\\char-class([range(48,57)]))]))", Factory.Production);
  private static final IConstructor prod__List_FunctionModifiers__modifiers_iter_star_seps__FunctionModifier__layouts_LAYOUTLIST_ = (IConstructor) _read("prod(label(\"List\",sort(\"FunctionModifiers\")),[label(\"modifiers\",\\iter-star-seps(sort(\"FunctionModifier\"),[layouts(\"LAYOUTLIST\")]))],{})", Factory.Production);
  private static final IConstructor prod__NonInterpolated_StringLiteral__constant_StringConstant_ = (IConstructor) _read("prod(label(\"NonInterpolated\",sort(\"StringLiteral\")),[label(\"constant\",lex(\"StringConstant\"))],{})", Factory.Production);
  private static final IConstructor prod__Division_Expression__lhs_Expression_layouts_LAYOUTLIST_lit___47_layouts_LAYOUTLIST_rhs_Expression__assoc__left = (IConstructor) _read("prod(label(\"Division\",sort(\"Expression\")),[label(\"lhs\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"/\"),layouts(\"LAYOUTLIST\"),label(\"rhs\",sort(\"Expression\"))],{assoc(left())})", Factory.Production);
  private static final IConstructor prod__Filter_Statement__lit_filter_layouts_LAYOUTLIST_lit___59_ = (IConstructor) _read("prod(label(\"Filter\",sort(\"Statement\")),[lit(\"filter\"),layouts(\"LAYOUTLIST\"),lit(\";\")],{})", Factory.Production);
  private static final IConstructor prod__Statement_Command__statement_Statement_ = (IConstructor) _read("prod(label(\"Statement\",sort(\"Command\")),[label(\"statement\",sort(\"Statement\"))],{})", Factory.Production);
  private static final IConstructor prod__RealLiteral__iter__char_class___range__48_57_char_class___range__69_69_range__101_101_opt__char_class___range__43_43_range__45_45_iter__char_class___range__48_57_opt__char_class___range__68_68_range__70_70_range__100_100_range__102_102_ = (IConstructor) _read("prod(lex(\"RealLiteral\"),[iter(\\char-class([range(48,57)])),\\char-class([range(69,69),range(101,101)]),opt(\\char-class([range(43,43),range(45,45)])),iter(\\char-class([range(48,57)])),opt(\\char-class([range(68,68),range(70,70),range(100,100),range(102,102)]))],{})", Factory.Production);
  private static final IConstructor prod__RascalKeywords__lit_type_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"type\")],{})", Factory.Production);
  private static final IConstructor prod__Real_Literal__realLiteral_RealLiteral_ = (IConstructor) _read("prod(label(\"Real\",sort(\"Literal\")),[label(\"realLiteral\",lex(\"RealLiteral\"))],{})", Factory.Production);
  private static final IConstructor prod__Undeclare_ShellCommand__lit_undeclare_layouts_LAYOUTLIST_name_QualifiedName_ = (IConstructor) _read("prod(label(\"Undeclare\",sort(\"ShellCommand\")),[lit(\"undeclare\"),layouts(\"LAYOUTLIST\"),label(\"name\",sort(\"QualifiedName\"))],{})", Factory.Production);
  private static final IConstructor prod__Boolean_Literal__booleanLiteral_BooleanLiteral_ = (IConstructor) _read("prod(label(\"Boolean\",sort(\"Literal\")),[label(\"booleanLiteral\",lex(\"BooleanLiteral\"))],{})", Factory.Production);
  private static final IConstructor prod__lit___60_58__char_class___range__60_60_char_class___range__58_58_ = (IConstructor) _read("prod(lit(\"\\<:\"),[\\char-class([range(60,60)]),\\char-class([range(58,58)])],{})", Factory.Production);
  private static final IConstructor prod__lit_assert__char_class___range__97_97_char_class___range__115_115_char_class___range__115_115_char_class___range__101_101_char_class___range__114_114_char_class___range__116_116_ = (IConstructor) _read("prod(lit(\"assert\"),[\\char-class([range(97,97)]),\\char-class([range(115,115)]),\\char-class([range(115,115)]),\\char-class([range(101,101)]),\\char-class([range(114,114)]),\\char-class([range(116,116)])],{})", Factory.Production);
  private static final IConstructor prod__Literal_Sym__string_StringConstant_ = (IConstructor) _read("prod(label(\"Literal\",sort(\"Sym\")),[label(\"string\",lex(\"StringConstant\"))],{})", Factory.Production);
  private static final IConstructor prod__StringCharacter__lit___92_char_class___range__34_34_range__39_39_range__60_60_range__62_62_range__92_92_range__98_98_range__102_102_range__110_110_range__114_114_range__116_116_ = (IConstructor) _read("prod(lex(\"StringCharacter\"),[lit(\"\\\\\"),\\char-class([range(34,34),range(39,39),range(60,60),range(62,62),range(92,92),range(98,98),range(102,102),range(110,110),range(114,114),range(116,116)])],{})", Factory.Production);
  private static final IConstructor prod__RascalKeywords__lit_if_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"if\")],{})", Factory.Production);
  private static final IConstructor prod__Num_BasicType__lit_num_ = (IConstructor) _read("prod(label(\"Num\",sort(\"BasicType\")),[lit(\"num\")],{})", Factory.Production);
  private static final IConstructor prod__Loc_BasicType__lit_loc_ = (IConstructor) _read("prod(label(\"Loc\",sort(\"BasicType\")),[lit(\"loc\")],{})", Factory.Production);
  private static final IConstructor prod__Mid_PathTail__mid_MidPathChars_layouts_LAYOUTLIST_expression_Expression_layouts_LAYOUTLIST_tail_PathTail_ = (IConstructor) _read("prod(label(\"Mid\",sort(\"PathTail\")),[label(\"mid\",lex(\"MidPathChars\")),layouts(\"LAYOUTLIST\"),label(\"expression\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),label(\"tail\",sort(\"PathTail\"))],{})", Factory.Production);
  private static final IConstructor prod__RascalKeywords__lit_it_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"it\")],{})", Factory.Production);
  private static final IConstructor prod__RascalKeywords__lit_in_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"in\")],{})", Factory.Production);
  private static final IConstructor prod__RascalKeywords__lit_else_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"else\")],{})", Factory.Production);
  private static final IConstructor prod__RascalKeywords__lit_for_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"for\")],{})", Factory.Production);
  private static final IConstructor prod__lit_lexical__char_class___range__108_108_char_class___range__101_101_char_class___range__120_120_char_class___range__105_105_char_class___range__99_99_char_class___range__97_97_char_class___range__108_108_ = (IConstructor) _read("prod(lit(\"lexical\"),[\\char-class([range(108,108)]),\\char-class([range(101,101)]),\\char-class([range(120,120)]),\\char-class([range(105,105)]),\\char-class([range(99,99)]),\\char-class([range(97,97)]),\\char-class([range(108,108)])],{})", Factory.Production);
  private static final IConstructor prod__Tuple_Expression__lit___60_layouts_LAYOUTLIST_elements_iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___62_ = (IConstructor) _read("prod(label(\"Tuple\",sort(\"Expression\")),[lit(\"\\<\"),layouts(\"LAYOUTLIST\"),label(\"elements\",\\iter-seps(sort(\"Expression\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\"\\>\")],{})", Factory.Production);
  private static final IConstructor prod__lit___60_45__char_class___range__60_60_char_class___range__45_45_ = (IConstructor) _read("prod(lit(\"\\<-\"),[\\char-class([range(60,60)]),\\char-class([range(45,45)])],{})", Factory.Production);
  private static final IConstructor prod__StepRange_Expression__lit___91_layouts_LAYOUTLIST_first_Expression_layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_second_Expression_layouts_LAYOUTLIST_lit___46_46_layouts_LAYOUTLIST_last_Expression_layouts_LAYOUTLIST_lit___93_ = (IConstructor) _read("prod(label(\"StepRange\",sort(\"Expression\")),[lit(\"[\"),layouts(\"LAYOUTLIST\"),label(\"first\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\"),label(\"second\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"..\"),layouts(\"LAYOUTLIST\"),label(\"last\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"]\")],{})", Factory.Production);
  private static final IConstructor prod__lit_filter__char_class___range__102_102_char_class___range__105_105_char_class___range__108_108_char_class___range__116_116_char_class___range__101_101_char_class___range__114_114_ = (IConstructor) _read("prod(lit(\"filter\"),[\\char-class([range(102,102)]),\\char-class([range(105,105)]),\\char-class([range(108,108)]),\\char-class([range(116,116)]),\\char-class([range(101,101)]),\\char-class([range(114,114)])],{})", Factory.Production);
  private static final IConstructor prod__MidTemplate_StringTail__mid_MidStringChars_layouts_LAYOUTLIST_template_StringTemplate_layouts_LAYOUTLIST_tail_StringTail_ = (IConstructor) _read("prod(label(\"MidTemplate\",sort(\"StringTail\")),[label(\"mid\",lex(\"MidStringChars\")),layouts(\"LAYOUTLIST\"),label(\"template\",sort(\"StringTemplate\")),layouts(\"LAYOUTLIST\"),label(\"tail\",sort(\"StringTail\"))],{})", Factory.Production);
  private static final IConstructor prod__MidPathChars__lit___62_URLChars_lit___60_ = (IConstructor) _read("prod(lex(\"MidPathChars\"),[lit(\"\\>\"),lex(\"URLChars\"),lit(\"\\<\")],{})", Factory.Production);
  private static final IConstructor prod__String_Literal__stringLiteral_StringLiteral_ = (IConstructor) _read("prod(label(\"String\",sort(\"Literal\")),[label(\"stringLiteral\",sort(\"StringLiteral\"))],{})", Factory.Production);
  private static final IConstructor prod__Negative_Pattern__lit___layouts_LAYOUTLIST_argument_Pattern_ = (IConstructor) _read("prod(label(\"Negative\",sort(\"Pattern\")),[lit(\"-\"),layouts(\"LAYOUTLIST\"),label(\"argument\",sort(\"Pattern\"))],{})", Factory.Production);
  private static final IConstructor prod__Alternative_Sym__lit___40_layouts_LAYOUTLIST_first_Sym_layouts_LAYOUTLIST_lit___124_layouts_LAYOUTLIST_alternatives_iter_seps__Sym__layouts_LAYOUTLIST_lit___124_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_ = (IConstructor) _read("prod(label(\"Alternative\",sort(\"Sym\")),[lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"first\",sort(\"Sym\")),layouts(\"LAYOUTLIST\"),lit(\"|\"),layouts(\"LAYOUTLIST\"),label(\"alternatives\",\\iter-seps(sort(\"Sym\"),[layouts(\"LAYOUTLIST\"),lit(\"|\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\")\")],{})", Factory.Production);
  private static final IConstructor prod__lit___58_58__char_class___range__58_58_char_class___range__58_58_ = (IConstructor) _read("prod(lit(\"::\"),[\\char-class([range(58,58)]),\\char-class([range(58,58)])],{})", Factory.Production);
  private static final IConstructor prod__lit_start__char_class___range__115_115_char_class___range__116_116_char_class___range__97_97_char_class___range__114_114_char_class___range__116_116_ = (IConstructor) _read("prod(lit(\"start\"),[\\char-class([range(115,115)]),\\char-class([range(116,116)]),\\char-class([range(97,97)]),\\char-class([range(114,114)]),\\char-class([range(116,116)])],{})", Factory.Production);
  private static final IConstructor prod__External_Import__lit_import_layouts_LAYOUTLIST_name_QualifiedName_layouts_LAYOUTLIST_lit___61_layouts_LAYOUTLIST_at_LocationLiteral_layouts_LAYOUTLIST_lit___59_ = (IConstructor) _read("prod(label(\"External\",sort(\"Import\")),[lit(\"import\"),layouts(\"LAYOUTLIST\"),label(\"name\",sort(\"QualifiedName\")),layouts(\"LAYOUTLIST\"),lit(\"=\"),layouts(\"LAYOUTLIST\"),label(\"at\",sort(\"LocationLiteral\")),layouts(\"LAYOUTLIST\"),lit(\";\")],{})", Factory.Production);
  private static final IConstructor prod__lit___58_61__char_class___range__58_58_char_class___range__61_61_ = (IConstructor) _read("prod(lit(\":=\"),[\\char-class([range(58,58)]),\\char-class([range(61,61)])],{})", Factory.Production);
  private static final IConstructor prod__NonEquals_Expression__lhs_Expression_layouts_LAYOUTLIST_lit___33_61_layouts_LAYOUTLIST_rhs_Expression__assoc__non_assoc = (IConstructor) _read("prod(label(\"NonEquals\",sort(\"Expression\")),[label(\"lhs\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"!=\"),layouts(\"LAYOUTLIST\"),label(\"rhs\",sort(\"Expression\"))],{assoc(\\non-assoc())})", Factory.Production);
  private static final IConstructor prod__TimePartNoTZ__char_class___range__48_50_char_class___range__48_57_char_class___range__48_53_char_class___range__48_57_char_class___range__48_53_char_class___range__48_57_opt__seq___char_class___range__44_44_range__46_46_char_class___range__48_57_opt__seq___char_class___range__48_57_opt__char_class___range__48_57_ = (IConstructor) _read("prod(lex(\"TimePartNoTZ\"),[\\char-class([range(48,50)]),\\char-class([range(48,57)]),\\char-class([range(48,53)]),\\char-class([range(48,57)]),\\char-class([range(48,53)]),\\char-class([range(48,57)]),opt(seq([\\char-class([range(44,44),range(46,46)]),\\char-class([range(48,57)]),opt(seq([\\char-class([range(48,57)]),opt(\\char-class([range(48,57)]))]))]))],{})", Factory.Production);
  private static final IConstructor prod__Dynamic_LocalVariableDeclaration__lit_dynamic_layouts_LAYOUTLIST_declarator_Declarator_ = (IConstructor) _read("prod(label(\"Dynamic\",sort(\"LocalVariableDeclaration\")),[lit(\"dynamic\"),layouts(\"LAYOUTLIST\"),label(\"declarator\",sort(\"Declarator\"))],{})", Factory.Production);
  private static final IConstructor prod__lit_for__char_class___range__102_102_char_class___range__111_111_char_class___range__114_114_ = (IConstructor) _read("prod(lit(\"for\"),[\\char-class([range(102,102)]),\\char-class([range(111,111)]),\\char-class([range(114,114)])],{})", Factory.Production);
  private static final IConstructor prod__Replacing_PatternWithAction__pattern_Pattern_layouts_LAYOUTLIST_lit___61_62_layouts_LAYOUTLIST_replacement_Replacement_ = (IConstructor) _read("prod(label(\"Replacing\",sort(\"PatternWithAction\")),[label(\"pattern\",sort(\"Pattern\")),layouts(\"LAYOUTLIST\"),lit(\"=\\>\"),layouts(\"LAYOUTLIST\"),label(\"replacement\",sort(\"Replacement\"))],{})", Factory.Production);
  private static final IConstructor prod__View_Kind__lit_view_ = (IConstructor) _read("prod(label(\"View\",sort(\"Kind\")),[lit(\"view\")],{})", Factory.Production);
  private static final IConstructor prod__Default_FunctionDeclaration__tags_Tags_layouts_LAYOUTLIST_visibility_Visibility_layouts_LAYOUTLIST_signature_Signature_layouts_LAYOUTLIST_body_FunctionBody__tag__Foldable = (IConstructor) _read("prod(label(\"Default\",sort(\"FunctionDeclaration\")),[label(\"tags\",sort(\"Tags\")),layouts(\"LAYOUTLIST\"),label(\"visibility\",sort(\"Visibility\")),layouts(\"LAYOUTLIST\"),label(\"signature\",sort(\"Signature\")),layouts(\"LAYOUTLIST\"),label(\"body\",sort(\"FunctionBody\"))],{tag(Foldable())})", Factory.Production);
  private static final IConstructor prod__Try_Statement__lit_try_layouts_LAYOUTLIST_body_Statement_layouts_LAYOUTLIST_handlers_iter_seps__Catch__layouts_LAYOUTLIST__assoc__non_assoc = (IConstructor) _read("prod(label(\"Try\",sort(\"Statement\")),[lit(\"try\"),layouts(\"LAYOUTLIST\"),label(\"body\",sort(\"Statement\")),layouts(\"LAYOUTLIST\"),label(\"handlers\",\\iter-seps(sort(\"Catch\"),[layouts(\"LAYOUTLIST\")]))],{assoc(\\non-assoc())})", Factory.Production);
  private static final IConstructor prod__TagString__lit___123_contents_iter_star__alt___TagString_char_class___range__0_122_range__124_124_range__126_16777215_seq___lit___92_char_class___range__123_123_range__125_125_lit___125_ = (IConstructor) _read("prod(lex(\"TagString\"),[lit(\"{\"),label(\"contents\",\\iter-star(alt({lex(\"TagString\"),\\char-class([range(0,122),range(124,124),range(126,16777215)]),seq([lit(\"\\\\\"),\\char-class([range(123,123),range(125,125)])])}))),lit(\"}\")],{})", Factory.Production);
  private static final IConstructor prod__IterStar_Sym__symbol_Sym_layouts_LAYOUTLIST_lit___42_ = (IConstructor) _read("prod(label(\"IterStar\",sort(\"Sym\")),[label(\"symbol\",sort(\"Sym\")),layouts(\"LAYOUTLIST\"),lit(\"*\")],{})", Factory.Production);
  private static final IConstructor prod__DatePart__char_class___range__48_57_char_class___range__48_57_char_class___range__48_57_char_class___range__48_57_lit___char_class___range__48_49_char_class___range__48_57_lit___char_class___range__48_51_char_class___range__48_57_ = (IConstructor) _read("prod(lex(\"DatePart\"),[\\char-class([range(48,57)]),\\char-class([range(48,57)]),\\char-class([range(48,57)]),\\char-class([range(48,57)]),lit(\"-\"),\\char-class([range(48,49)]),\\char-class([range(48,57)]),lit(\"-\"),\\char-class([range(48,51)]),\\char-class([range(48,57)])],{})", Factory.Production);
  private static final IConstructor prod__RegExp__char_class___range__60_60_expression_Expression_char_class___range__62_62__tag__category___77_101_116_97_86_97_114_105_97_98_108_101 = (IConstructor) _read("prod(lex(\"RegExp\"),[\\char-class([range(60,60)]),label(\"expression\",sort(\"Expression\")),\\char-class([range(62,62)])],{tag(category(\"MetaVariable\"))})", Factory.Production);
  private static final IConstructor prod__RascalKeywords__lit_switch_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"switch\")],{})", Factory.Production);
  private static final IConstructor prod__For_Statement__label_Label_layouts_LAYOUTLIST_lit_for_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_generators_iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_layouts_LAYOUTLIST_body_Statement_ = (IConstructor) _read("prod(label(\"For\",sort(\"Statement\")),[label(\"label\",sort(\"Label\")),layouts(\"LAYOUTLIST\"),lit(\"for\"),layouts(\"LAYOUTLIST\"),lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"generators\",\\iter-seps(sort(\"Expression\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\")\"),layouts(\"LAYOUTLIST\"),label(\"body\",sort(\"Statement\"))],{})", Factory.Production);
  private static final IConstructor prod__lit_left__char_class___range__108_108_char_class___range__101_101_char_class___range__102_102_char_class___range__116_116_ = (IConstructor) _read("prod(lit(\"left\"),[\\char-class([range(108,108)]),\\char-class([range(101,101)]),\\char-class([range(102,102)]),\\char-class([range(116,116)])],{})", Factory.Production);
  private static final IConstructor prod__All_Prod__lhs_Prod_layouts_LAYOUTLIST_lit___124_layouts_LAYOUTLIST_rhs_Prod__assoc__left = (IConstructor) _read("prod(label(\"All\",sort(\"Prod\")),[label(\"lhs\",sort(\"Prod\")),layouts(\"LAYOUTLIST\"),lit(\"|\"),layouts(\"LAYOUTLIST\"),label(\"rhs\",sort(\"Prod\"))],{assoc(left())})", Factory.Production);
  private static final IConstructor prod__RascalKeywords__lit_throws_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"throws\")],{})", Factory.Production);
  private static final IConstructor prod__Self_Expression__lit_self_ = (IConstructor) _read("prod(label(\"Self\",sort(\"Expression\")),[lit(\"self\")],{})", Factory.Production);
  private static final IConstructor prod__Backslash__conditional__char_class___range__92_92__not_follow__char_class___range__47_47_range__60_60_range__62_62_range__92_92_ = (IConstructor) _read("prod(lex(\"Backslash\"),[conditional(\\char-class([range(92,92)]),{\\not-follow(\\char-class([range(47,47),range(60,60),range(62,62),range(92,92)]))})],{})", Factory.Production);
  private static final IConstructor regular__empty = (IConstructor) _read("regular(empty())", Factory.Production);
  private static final IConstructor regular__iter_star__char_class___range__0_9_range__11_16777215 = (IConstructor) _read("regular(\\iter-star(\\char-class([range(0,9),range(11,16777215)])))", Factory.Production);
  private static final IConstructor prod__PrePathChars__URLChars_lit___60_ = (IConstructor) _read("prod(lex(\"PrePathChars\"),[lex(\"URLChars\"),lit(\"\\<\")],{})", Factory.Production);
  private static final IConstructor prod__lit_bracket__char_class___range__98_98_char_class___range__114_114_char_class___range__97_97_char_class___range__99_99_char_class___range__107_107_char_class___range__101_101_char_class___range__116_116_ = (IConstructor) _read("prod(lit(\"bracket\"),[\\char-class([range(98,98)]),\\char-class([range(114,114)]),\\char-class([range(97,97)]),\\char-class([range(99,99)]),\\char-class([range(107,107)]),\\char-class([range(101,101)]),\\char-class([range(116,116)])],{})", Factory.Production);
  private static final IConstructor prod__DateTime_Literal__dateTimeLiteral_DateTimeLiteral_ = (IConstructor) _read("prod(label(\"DateTime\",sort(\"Literal\")),[label(\"dateTimeLiteral\",sort(\"DateTimeLiteral\"))],{})", Factory.Production);
  private static final IConstructor regular__iter_star__char_class___range__0_8_range__11_12_range__14_31_range__33_59_range__61_123_range__125_16777215 = (IConstructor) _read("regular(\\iter-star(\\char-class([range(0,8),range(11,12),range(14,31),range(33,59),range(61,123),range(125,16777215)])))", Factory.Production);
  private static final IConstructor prod__Parametrized_Sym__conditional__nonterminal_Nonterminal__follow__lit___91_layouts_LAYOUTLIST_lit___91_layouts_LAYOUTLIST_parameters_iter_seps__Sym__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___93_ = (IConstructor) _read("prod(label(\"Parametrized\",sort(\"Sym\")),[conditional(label(\"nonterminal\",lex(\"Nonterminal\")),{follow(lit(\"[\"))}),layouts(\"LAYOUTLIST\"),lit(\"[\"),layouts(\"LAYOUTLIST\"),label(\"parameters\",\\iter-seps(sort(\"Sym\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\"]\")],{})", Factory.Production);
  private static final IConstructor prod__TimeLiteral_DateTimeLiteral__time_JustTime_ = (IConstructor) _read("prod(label(\"TimeLiteral\",sort(\"DateTimeLiteral\")),[label(\"time\",lex(\"JustTime\"))],{})", Factory.Production);
  private static final IConstructor prod__TypedVariableBecomes_Pattern__type_Type_layouts_LAYOUTLIST_name_Name_layouts_LAYOUTLIST_lit___58_layouts_LAYOUTLIST_pattern_Pattern_ = (IConstructor) _read("prod(label(\"TypedVariableBecomes\",sort(\"Pattern\")),[label(\"type\",sort(\"Type\")),layouts(\"LAYOUTLIST\"),label(\"name\",lex(\"Name\")),layouts(\"LAYOUTLIST\"),lit(\":\"),layouts(\"LAYOUTLIST\"),label(\"pattern\",sort(\"Pattern\"))],{})", Factory.Production);
  private static final IConstructor prod__start__PreModule__layouts_LAYOUTLIST_top_PreModule_layouts_LAYOUTLIST_ = (IConstructor) _read("prod(start(sort(\"PreModule\")),[layouts(\"LAYOUTLIST\"),label(\"top\",sort(\"PreModule\")),layouts(\"LAYOUTLIST\")],{})", Factory.Production);
  private static final IConstructor prod__DateLiteral_DateTimeLiteral__date_JustDate_ = (IConstructor) _read("prod(label(\"DateLiteral\",sort(\"DateTimeLiteral\")),[label(\"date\",lex(\"JustDate\"))],{})", Factory.Production);
  private static final IConstructor prod__TimePartNoTZ__char_class___range__48_50_char_class___range__48_57_lit___58_char_class___range__48_53_char_class___range__48_57_lit___58_char_class___range__48_53_char_class___range__48_57_opt__seq___char_class___range__44_44_range__46_46_char_class___range__48_57_opt__seq___char_class___range__48_57_opt__char_class___range__48_57_ = (IConstructor) _read("prod(lex(\"TimePartNoTZ\"),[\\char-class([range(48,50)]),\\char-class([range(48,57)]),lit(\":\"),\\char-class([range(48,53)]),\\char-class([range(48,57)]),lit(\":\"),\\char-class([range(48,53)]),\\char-class([range(48,57)]),opt(seq([\\char-class([range(44,44),range(46,46)]),\\char-class([range(48,57)]),opt(seq([\\char-class([range(48,57)]),opt(\\char-class([range(48,57)]))]))]))],{})", Factory.Production);
  private static final IConstructor prod__Name_Field__fieldName_Name_ = (IConstructor) _read("prod(label(\"Name\",sort(\"Field\")),[label(\"fieldName\",lex(\"Name\"))],{})", Factory.Production);
  private static final IConstructor prod__Bounded_TypeVar__lit___38_layouts_LAYOUTLIST_name_Name_layouts_LAYOUTLIST_lit___60_58_layouts_LAYOUTLIST_bound_Type_ = (IConstructor) _read("prod(label(\"Bounded\",sort(\"TypeVar\")),[lit(\"&\"),layouts(\"LAYOUTLIST\"),label(\"name\",lex(\"Name\")),layouts(\"LAYOUTLIST\"),lit(\"\\<:\"),layouts(\"LAYOUTLIST\"),label(\"bound\",sort(\"Type\"))],{})", Factory.Production);
  private static final IConstructor prod__layouts_$BACKTICKS__ = (IConstructor) _read("prod(layouts(\"$BACKTICKS\"),[],{})", Factory.Production);
  private static final IConstructor prod__Arbitrary_PatternWithAction__pattern_Pattern_layouts_LAYOUTLIST_lit___58_layouts_LAYOUTLIST_statement_Statement_ = (IConstructor) _read("prod(label(\"Arbitrary\",sort(\"PatternWithAction\")),[label(\"pattern\",sort(\"Pattern\")),layouts(\"LAYOUTLIST\"),lit(\":\"),layouts(\"LAYOUTLIST\"),label(\"statement\",sort(\"Statement\"))],{})", Factory.Production);
  private static final IConstructor prod__lit_insert__char_class___range__105_105_char_class___range__110_110_char_class___range__115_115_char_class___range__101_101_char_class___range__114_114_char_class___range__116_116_ = (IConstructor) _read("prod(lit(\"insert\"),[\\char-class([range(105,105)]),\\char-class([range(110,110)]),\\char-class([range(115,115)]),\\char-class([range(101,101)]),\\char-class([range(114,114)]),\\char-class([range(116,116)])],{})", Factory.Production);
  private static final IConstructor prod__RascalKeywords__lit_public_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"public\")],{})", Factory.Production);
  private static final IConstructor prod__lit_history__char_class___range__104_104_char_class___range__105_105_char_class___range__115_115_char_class___range__116_116_char_class___range__111_111_char_class___range__114_114_char_class___range__121_121_ = (IConstructor) _read("prod(lit(\"history\"),[\\char-class([range(104,104)]),\\char-class([range(105,105)]),\\char-class([range(115,115)]),\\char-class([range(116,116)]),\\char-class([range(111,111)]),\\char-class([range(114,114)]),\\char-class([range(121,121)])],{})", Factory.Production);
  private static final IConstructor prod__layouts_$default$__ = (IConstructor) _read("prod(layouts(\"$default$\"),[],{})", Factory.Production);
  private static final IConstructor prod__BottomUpBreak_Strategy__lit_bottom_up_break_ = (IConstructor) _read("prod(label(\"BottomUpBreak\",sort(\"Strategy\")),[lit(\"bottom-up-break\")],{})", Factory.Production);
  private static final IConstructor prod__TypeArguments_FunctionType__type_Type_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_arguments_iter_star_seps__TypeArg__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_ = (IConstructor) _read("prod(label(\"TypeArguments\",sort(\"FunctionType\")),[label(\"type\",sort(\"Type\")),layouts(\"LAYOUTLIST\"),lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"arguments\",\\iter-star-seps(sort(\"TypeArg\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\")\")],{})", Factory.Production);
  private static final IConstructor prod__Variable_Assignable__qualifiedName_QualifiedName_ = (IConstructor) _read("prod(label(\"Variable\",sort(\"Assignable\")),[label(\"qualifiedName\",sort(\"QualifiedName\"))],{})", Factory.Production);
  private static final IConstructor prod__Relation_BasicType__lit_rel_ = (IConstructor) _read("prod(label(\"Relation\",sort(\"BasicType\")),[lit(\"rel\")],{})", Factory.Production);
  private static final IConstructor prod__Selector_DataTypeSelector__sort_QualifiedName_layouts_LAYOUTLIST_lit___46_layouts_LAYOUTLIST_production_Name_ = (IConstructor) _read("prod(label(\"Selector\",sort(\"DataTypeSelector\")),[label(\"sort\",sort(\"QualifiedName\")),layouts(\"LAYOUTLIST\"),lit(\".\"),layouts(\"LAYOUTLIST\"),label(\"production\",lex(\"Name\"))],{})", Factory.Production);
  private static final IConstructor prod__Default_TypeArg__type_Type_ = (IConstructor) _read("prod(label(\"Default\",sort(\"TypeArg\")),[label(\"type\",sort(\"Type\"))],{})", Factory.Production);
  private static final IConstructor prod__HeaderKeyword__lit_keyword_ = (IConstructor) _read("prod(keywords(\"HeaderKeyword\"),[lit(\"keyword\")],{})", Factory.Production);
  private static final IConstructor prod__RascalKeywords__lit_list_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"list\")],{})", Factory.Production);
  private static final IConstructor prod__lit_undeclare__char_class___range__117_117_char_class___range__110_110_char_class___range__100_100_char_class___range__101_101_char_class___range__99_99_char_class___range__108_108_char_class___range__97_97_char_class___range__114_114_char_class___range__101_101_ = (IConstructor) _read("prod(lit(\"undeclare\"),[\\char-class([range(117,117)]),\\char-class([range(110,110)]),\\char-class([range(100,100)]),\\char-class([range(101,101)]),\\char-class([range(99,99)]),\\char-class([range(108,108)]),\\char-class([range(97,97)]),\\char-class([range(114,114)]),\\char-class([range(101,101)])],{})", Factory.Production);
  private static final IConstructor regular__iter_star__StringCharacter = (IConstructor) _read("regular(\\iter-star(lex(\"StringCharacter\")))", Factory.Production);
  private static final IConstructor regular__opt__seq___char_class___range__44_44_range__46_46_char_class___range__48_57_opt__seq___char_class___range__48_57_opt__char_class___range__48_57 = (IConstructor) _read("regular(opt(seq([\\char-class([range(44,44),range(46,46)]),\\char-class([range(48,57)]),opt(seq([\\char-class([range(48,57)]),opt(\\char-class([range(48,57)]))]))])))", Factory.Production);
  private static final IConstructor prod__Union_Class__lhs_Class_layouts_LAYOUTLIST_lit___124_124_layouts_LAYOUTLIST_rhs_Class__assoc__left = (IConstructor) _read("prod(label(\"Union\",sort(\"Class\")),[label(\"lhs\",sort(\"Class\")),layouts(\"LAYOUTLIST\"),lit(\"||\"),layouts(\"LAYOUTLIST\"),label(\"rhs\",sort(\"Class\"))],{assoc(left())})", Factory.Production);
  private static final IConstructor prod__StringCharacter__char_class___range__10_10_iter_star__char_class___range__9_9_range__32_32_char_class___range__39_39_ = (IConstructor) _read("prod(lex(\"StringCharacter\"),[\\char-class([range(10,10)]),\\iter-star(\\char-class([range(9,9),range(32,32)])),\\char-class([range(39,39)])],{})", Factory.Production);
  private static final IConstructor prod__Default_FunctionBody__lit___123_layouts_LAYOUTLIST_statements_iter_star_seps__Statement__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_ = (IConstructor) _read("prod(label(\"Default\",sort(\"FunctionBody\")),[lit(\"{\"),layouts(\"LAYOUTLIST\"),label(\"statements\",\\iter-star-seps(sort(\"Statement\"),[layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\"}\")],{})", Factory.Production);
  private static final IConstructor prod__Test_ShellCommand__lit_test_ = (IConstructor) _read("prod(label(\"Test\",sort(\"ShellCommand\")),[lit(\"test\")],{})", Factory.Production);
  private static final IConstructor prod__RascalKeywords__lit_tag_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"tag\")],{})", Factory.Production);
  private static final IConstructor prod__Annotation_Assignable__receiver_Assignable_layouts_LAYOUTLIST_lit___64_layouts_LAYOUTLIST_annotation_Name_ = (IConstructor) _read("prod(label(\"Annotation\",sort(\"Assignable\")),[label(\"receiver\",sort(\"Assignable\")),layouts(\"LAYOUTLIST\"),lit(\"@\"),layouts(\"LAYOUTLIST\"),label(\"annotation\",lex(\"Name\"))],{})", Factory.Production);
  private static final IConstructor prod__RascalKeywords__lit_extend_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"extend\")],{})", Factory.Production);
  private static final IConstructor prod__StringCharacter__UnicodeEscape_ = (IConstructor) _read("prod(lex(\"StringCharacter\"),[lex(\"UnicodeEscape\")],{})", Factory.Production);
  private static final IConstructor prod__Follow_Sym__symbol_Sym_layouts_LAYOUTLIST_lit___62_62_layouts_LAYOUTLIST_match_Sym__assoc__left = (IConstructor) _read("prod(label(\"Follow\",sort(\"Sym\")),[label(\"symbol\",sort(\"Sym\")),layouts(\"LAYOUTLIST\"),lit(\"\\>\\>\"),layouts(\"LAYOUTLIST\"),label(\"match\",sort(\"Sym\"))],{assoc(left())})", Factory.Production);
  private static final IConstructor prod__Statement_EvalCommand__statement_Statement_ = (IConstructor) _read("prod(label(\"Statement\",sort(\"EvalCommand\")),[label(\"statement\",sort(\"Statement\"))],{})", Factory.Production);
  private static final IConstructor prod__Named_TypeArg__type_Type_layouts_LAYOUTLIST_name_Name_ = (IConstructor) _read("prod(label(\"Named\",sort(\"TypeArg\")),[label(\"type\",sort(\"Type\")),layouts(\"LAYOUTLIST\"),label(\"name\",lex(\"Name\"))],{})", Factory.Production);
  private static final IConstructor prod__Set_Pattern__lit___123_layouts_LAYOUTLIST_elements_iter_star_seps__Pattern__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_ = (IConstructor) _read("prod(label(\"Set\",sort(\"Pattern\")),[lit(\"{\"),layouts(\"LAYOUTLIST\"),label(\"elements\",\\iter-star-seps(sort(\"Pattern\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\"}\")],{})", Factory.Production);
  private static final IConstructor prod__Has_Expression__expression_Expression_layouts_LAYOUTLIST_lit_has_layouts_LAYOUTLIST_name_Name_ = (IConstructor) _read("prod(label(\"Has\",sort(\"Expression\")),[label(\"expression\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"has\"),layouts(\"LAYOUTLIST\"),label(\"name\",lex(\"Name\"))],{})", Factory.Production);
  private static final IConstructor prod__RascalKeywords__lit_str_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"str\")],{})", Factory.Production);
  private static final IConstructor prod__Initialized_Variable__name_Name_layouts_LAYOUTLIST_lit___61_layouts_LAYOUTLIST_initial_Expression_ = (IConstructor) _read("prod(label(\"Initialized\",sort(\"Variable\")),[label(\"name\",lex(\"Name\")),layouts(\"LAYOUTLIST\"),lit(\"=\"),layouts(\"LAYOUTLIST\"),label(\"initial\",sort(\"Expression\"))],{})", Factory.Production);
  private static final IConstructor prod__Basic_Type__basic_BasicType_ = (IConstructor) _read("prod(label(\"Basic\",sort(\"Type\")),[label(\"basic\",sort(\"BasicType\"))],{})", Factory.Production);
  private static final IConstructor prod__lit_rel__char_class___range__114_114_char_class___range__101_101_char_class___range__108_108_ = (IConstructor) _read("prod(lit(\"rel\"),[\\char-class([range(114,114)]),\\char-class([range(101,101)]),\\char-class([range(108,108)])],{})", Factory.Production);
  private static final IConstructor prod__JustDate__lit___36_DatePart_ = (IConstructor) _read("prod(lex(\"JustDate\"),[lit(\"$\"),lex(\"DatePart\")],{})", Factory.Production);
  private static final IConstructor prod__RascalKeywords__lit_return_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"return\")],{})", Factory.Production);
  private static final IConstructor prod__Structured_Type__structured_StructuredType_ = (IConstructor) _read("prod(label(\"Structured\",sort(\"Type\")),[label(\"structured\",sort(\"StructuredType\"))],{})", Factory.Production);
  private static final IConstructor prod__And_Expression__lhs_Expression_layouts_LAYOUTLIST_lit___38_38_layouts_LAYOUTLIST_rhs_Expression__assoc__left = (IConstructor) _read("prod(label(\"And\",sort(\"Expression\")),[label(\"lhs\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"&&\"),layouts(\"LAYOUTLIST\"),label(\"rhs\",sort(\"Expression\"))],{assoc(left())})", Factory.Production);
  private static final IConstructor prod__lit___61_61_62__char_class___range__61_61_char_class___range__61_61_char_class___range__62_62_ = (IConstructor) _read("prod(lit(\"==\\>\"),[\\char-class([range(61,61)]),\\char-class([range(61,61)]),\\char-class([range(62,62)])],{})", Factory.Production);
  private static final IConstructor prod__RascalKeywords__lit_set_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"set\")],{})", Factory.Production);
  private static final IConstructor prod__Char__char_class___range__0_31_range__33_33_range__35_38_range__40_44_range__46_59_range__61_61_range__63_90_range__94_16777215__tag__category___67_111_110_115_116_97_110_116 = (IConstructor) _read("prod(lex(\"Char\"),[\\char-class([range(0,31),range(33,33),range(35,38),range(40,44),range(46,59),range(61,61),range(63,90),range(94,16777215)])],{tag(category(\"Constant\"))})", Factory.Production);
  private static final IConstructor prod__RealLiteral__conditional__lit___46__not_precede__char_class___range__46_46_iter__char_class___range__48_57_char_class___range__69_69_range__101_101_opt__char_class___range__43_43_range__45_45_iter__char_class___range__48_57_opt__char_class___range__68_68_range__70_70_range__100_100_range__102_102_ = (IConstructor) _read("prod(lex(\"RealLiteral\"),[conditional(lit(\".\"),{\\not-precede(\\char-class([range(46,46)]))}),iter(\\char-class([range(48,57)])),\\char-class([range(69,69),range(101,101)]),opt(\\char-class([range(43,43),range(45,45)])),iter(\\char-class([range(48,57)])),opt(\\char-class([range(68,68),range(70,70),range(100,100),range(102,102)]))],{})", Factory.Production);
  private static final IConstructor prod__RascalKeywords__lit_datetime_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"datetime\")],{})", Factory.Production);
  private static final IConstructor prod__GreaterThanOrEq_Expression__lhs_Expression_layouts_LAYOUTLIST_lit___62_61_layouts_LAYOUTLIST_rhs_Expression__assoc__non_assoc = (IConstructor) _read("prod(label(\"GreaterThanOrEq\",sort(\"Expression\")),[label(\"lhs\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"\\>=\"),layouts(\"LAYOUTLIST\"),label(\"rhs\",sort(\"Expression\"))],{assoc(\\non-assoc())})", Factory.Production);
  private static final IConstructor prod__RascalKeywords__lit_bool_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"bool\")],{})", Factory.Production);
  private static final IConstructor prod__Absent_Start__ = (IConstructor) _read("prod(label(\"Absent\",sort(\"Start\")),[],{})", Factory.Production);
  private static final IConstructor prod__Default_Header__tags_Tags_layouts_LAYOUTLIST_lit_module_layouts_LAYOUTLIST_name_QualifiedName_layouts_LAYOUTLIST_imports_iter_star_seps__Import__layouts_LAYOUTLIST_ = (IConstructor) _read("prod(label(\"Default\",sort(\"Header\")),[label(\"tags\",sort(\"Tags\")),layouts(\"LAYOUTLIST\"),lit(\"module\"),layouts(\"LAYOUTLIST\"),label(\"name\",sort(\"QualifiedName\")),layouts(\"LAYOUTLIST\"),label(\"imports\",\\iter-star-seps(sort(\"Import\"),[layouts(\"LAYOUTLIST\")]))],{})", Factory.Production);
  private static final IConstructor prod__Labeled_Target__name_Name_ = (IConstructor) _read("prod(label(\"Labeled\",sort(\"Target\")),[label(\"name\",lex(\"Name\"))],{})", Factory.Production);
  private static final IConstructor regular__iter_star_seps__Import__layouts_LAYOUTLIST = (IConstructor) _read("regular(\\iter-star-seps(sort(\"Import\"),[layouts(\"LAYOUTLIST\")]))", Factory.Production);
  private static final IConstructor prod__FieldAccess_Expression__expression_Expression_layouts_LAYOUTLIST_lit___46_layouts_LAYOUTLIST_field_Name_ = (IConstructor) _read("prod(label(\"FieldAccess\",sort(\"Expression\")),[label(\"expression\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\".\"),layouts(\"LAYOUTLIST\"),label(\"field\",lex(\"Name\"))],{})", Factory.Production);
  private static final IConstructor prod__Bracket_Assignable__lit___40_layouts_LAYOUTLIST_arg_Assignable_layouts_LAYOUTLIST_lit___41__bracket = (IConstructor) _read("prod(label(\"Bracket\",sort(\"Assignable\")),[lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"arg\",sort(\"Assignable\")),layouts(\"LAYOUTLIST\"),lit(\")\")],{bracket()})", Factory.Production);
  private static final IConstructor prod__Interpolated_StringLiteral__pre_PreStringChars_layouts_LAYOUTLIST_expression_Expression_layouts_LAYOUTLIST_tail_StringTail_ = (IConstructor) _read("prod(label(\"Interpolated\",sort(\"StringLiteral\")),[label(\"pre\",lex(\"PreStringChars\")),layouts(\"LAYOUTLIST\"),label(\"expression\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),label(\"tail\",sort(\"StringTail\"))],{})", Factory.Production);
  private static final IConstructor prod__RealLiteral__iter__char_class___range__48_57_conditional__lit___46__not_follow__lit___46_iter_star__char_class___range__48_57_opt__char_class___range__68_68_range__70_70_range__100_100_range__102_102_ = (IConstructor) _read("prod(lex(\"RealLiteral\"),[iter(\\char-class([range(48,57)])),conditional(lit(\".\"),{\\not-follow(lit(\".\"))}),\\iter-star(\\char-class([range(48,57)])),opt(\\char-class([range(68,68),range(70,70),range(100,100),range(102,102)]))],{})", Factory.Production);
  private static final IConstructor prod__Default_ImportedModule__name_QualifiedName_ = (IConstructor) _read("prod(label(\"Default\",sort(\"ImportedModule\")),[label(\"name\",sort(\"QualifiedName\"))],{})", Factory.Production);
  private static final IConstructor prod__lit_continue__char_class___range__99_99_char_class___range__111_111_char_class___range__110_110_char_class___range__116_116_char_class___range__105_105_char_class___range__110_110_char_class___range__117_117_char_class___range__101_101_ = (IConstructor) _read("prod(lit(\"continue\"),[\\char-class([range(99,99)]),\\char-class([range(111,111)]),\\char-class([range(110,110)]),\\char-class([range(116,116)]),\\char-class([range(105,105)]),\\char-class([range(110,110)]),\\char-class([range(117,117)]),\\char-class([range(101,101)])],{})", Factory.Production);
  private static final IConstructor prod__FieldProject_Expression__expression_Expression_layouts_LAYOUTLIST_lit___60_layouts_LAYOUTLIST_fields_iter_seps__Field__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___62_ = (IConstructor) _read("prod(label(\"FieldProject\",sort(\"Expression\")),[label(\"expression\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"\\<\"),layouts(\"LAYOUTLIST\"),label(\"fields\",\\iter-seps(sort(\"Field\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\"\\>\")],{})", Factory.Production);
  private static final IConstructor prod__lit___60_60_61__char_class___range__60_60_char_class___range__60_60_char_class___range__61_61_ = (IConstructor) _read("prod(lit(\"\\<\\<=\"),[\\char-class([range(60,60)]),\\char-class([range(60,60)]),\\char-class([range(61,61)])],{})", Factory.Production);
  private static final IConstructor prod__RascalKeywords__lit_rel_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"rel\")],{})", Factory.Production);
  private static final IConstructor prod__Tuple_BasicType__lit_tuple_ = (IConstructor) _read("prod(label(\"Tuple\",sort(\"BasicType\")),[lit(\"tuple\")],{})", Factory.Production);
  private static final IConstructor prod__RascalKeywords__lit_rat_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"rat\")],{})", Factory.Production);
  private static final IConstructor prod__Map_Pattern__lit___40_layouts_LAYOUTLIST_mappings_iter_star_seps__Mapping__Pattern__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_ = (IConstructor) _read("prod(label(\"Map\",sort(\"Pattern\")),[lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"mappings\",\\iter-star-seps(\\parameterized-sort(\"Mapping\",[sort(\"Pattern\")]),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\")\")],{})", Factory.Production);
  private static final IConstructor prod__Unconditional_Replacement__replacementExpression_Expression_ = (IConstructor) _read("prod(label(\"Unconditional\",sort(\"Replacement\")),[label(\"replacementExpression\",sort(\"Expression\"))],{})", Factory.Production);
  private static final IConstructor regular__iter_seps__Sym__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST = (IConstructor) _read("regular(\\iter-seps(sort(\"Sym\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")]))", Factory.Production);
  private static final IConstructor prod__FieldUpdate_Expression__expression_Expression_layouts_LAYOUTLIST_lit___91_layouts_LAYOUTLIST_key_Name_layouts_LAYOUTLIST_lit___61_layouts_LAYOUTLIST_replacement_Expression_layouts_LAYOUTLIST_lit___93_ = (IConstructor) _read("prod(label(\"FieldUpdate\",sort(\"Expression\")),[label(\"expression\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"[\"),layouts(\"LAYOUTLIST\"),label(\"key\",lex(\"Name\")),layouts(\"LAYOUTLIST\"),lit(\"=\"),layouts(\"LAYOUTLIST\"),label(\"replacement\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"]\")],{})", Factory.Production);
  private static final IConstructor prod__lit_dynamic__char_class___range__100_100_char_class___range__121_121_char_class___range__110_110_char_class___range__97_97_char_class___range__109_109_char_class___range__105_105_char_class___range__99_99_ = (IConstructor) _read("prod(lit(\"dynamic\"),[\\char-class([range(100,100)]),\\char-class([range(121,121)]),\\char-class([range(110,110)]),\\char-class([range(97,97)]),\\char-class([range(109,109)]),\\char-class([range(105,105)]),\\char-class([range(99,99)])],{})", Factory.Production);
  private static final IConstructor prod__QualifiedName_Pattern__qualifiedName_QualifiedName_ = (IConstructor) _read("prod(label(\"QualifiedName\",sort(\"Pattern\")),[label(\"qualifiedName\",sort(\"QualifiedName\"))],{})", Factory.Production);
  private static final IConstructor regular__iter_seps__Sym__layouts_LAYOUTLIST_lit___124_layouts_LAYOUTLIST = (IConstructor) _read("regular(\\iter-seps(sort(\"Sym\"),[layouts(\"LAYOUTLIST\"),lit(\"|\"),layouts(\"LAYOUTLIST\")]))", Factory.Production);
  private static final IConstructor prod__RascalKeywords__lit_mod_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"mod\")],{})", Factory.Production);
  private static final IConstructor prod__MultiVariable_Pattern__qualifiedName_QualifiedName_layouts_LAYOUTLIST_lit___42_ = (IConstructor) _read("prod(label(\"MultiVariable\",sort(\"Pattern\")),[label(\"qualifiedName\",sort(\"QualifiedName\")),layouts(\"LAYOUTLIST\"),lit(\"*\")],{})", Factory.Production);
  private static final IConstructor prod__Is_Expression__expression_Expression_layouts_LAYOUTLIST_lit_is_layouts_LAYOUTLIST_name_Name_ = (IConstructor) _read("prod(label(\"Is\",sort(\"Expression\")),[label(\"expression\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"is\"),layouts(\"LAYOUTLIST\"),label(\"name\",lex(\"Name\"))],{})", Factory.Production);
  private static final IConstructor prod__RascalKeywords__lit_throw_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"throw\")],{})", Factory.Production);
  private static final IConstructor prod__Public_Visibility__lit_public_ = (IConstructor) _read("prod(label(\"Public\",sort(\"Visibility\")),[lit(\"public\")],{})", Factory.Production);
  private static final IConstructor prod__RascalKeywords__lit_open_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"open\")],{})", Factory.Production);
  private static final IConstructor prod__RascalKeywords__lit_module_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"module\")],{})", Factory.Production);
  private static final IConstructor prod__Mid_StringMiddle__mid_MidStringChars_ = (IConstructor) _read("prod(label(\"Mid\",sort(\"StringMiddle\")),[label(\"mid\",lex(\"MidStringChars\"))],{})", Factory.Production);
  private static final IConstructor prod__CaseInsensitiveLiteral_Sym__cistring_CaseInsensitiveStringConstant_ = (IConstructor) _read("prod(label(\"CaseInsensitiveLiteral\",sort(\"Sym\")),[label(\"cistring\",lex(\"CaseInsensitiveStringConstant\"))],{})", Factory.Production);
  private static final IConstructor prod__RascalKeywords__lit_private_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"private\")],{})", Factory.Production);
  private static final IConstructor prod__lit_list__char_class___range__108_108_char_class___range__105_105_char_class___range__115_115_char_class___range__116_116_ = (IConstructor) _read("prod(lit(\"list\"),[\\char-class([range(108,108)]),\\char-class([range(105,105)]),\\char-class([range(115,115)]),\\char-class([range(116,116)])],{})", Factory.Production);
  private static final IConstructor prod__Variable_Type__typeVar_TypeVar_ = (IConstructor) _read("prod(label(\"Variable\",sort(\"Type\")),[label(\"typeVar\",sort(\"TypeVar\"))],{})", Factory.Production);
  private static final IConstructor prod__RascalKeywords__lit_map_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"map\")],{})", Factory.Production);
  private static final IConstructor prod__Splice_Expression__lit___42_layouts_LAYOUTLIST_argument_Expression__assoc__non_assoc = (IConstructor) _read("prod(label(\"Splice\",sort(\"Expression\")),[lit(\"*\"),layouts(\"LAYOUTLIST\"),label(\"argument\",sort(\"Expression\"))],{assoc(\\non-assoc())})", Factory.Production);
  private static final IConstructor prod__Append_Statement__lit_append_layouts_LAYOUTLIST_dataTarget_DataTarget_layouts_LAYOUTLIST_statement_Statement__assoc__non_assoc = (IConstructor) _read("prod(label(\"Append\",sort(\"Statement\")),[lit(\"append\"),layouts(\"LAYOUTLIST\"),label(\"dataTarget\",sort(\"DataTarget\")),layouts(\"LAYOUTLIST\"),label(\"statement\",sort(\"Statement\"))],{assoc(\\non-assoc())})", Factory.Production);
  private static final IConstructor prod__Left_Assoc__lit_left_ = (IConstructor) _read("prod(label(\"Left\",sort(\"Assoc\")),[lit(\"left\")],{})", Factory.Production);
  private static final IConstructor prod__Import_Command__imported_Import_ = (IConstructor) _read("prod(label(\"Import\",sort(\"Command\")),[label(\"imported\",sort(\"Import\"))],{})", Factory.Production);
  private static final IConstructor prod__RegExp__char_class___range__0_46_range__48_59_range__61_61_range__63_91_range__93_16777215_ = (IConstructor) _read("prod(lex(\"RegExp\"),[\\char-class([range(0,46),range(48,59),range(61,61),range(63,91),range(93,16777215)])],{})", Factory.Production);
  private static final IConstructor prod__lit_test__char_class___range__116_116_char_class___range__101_101_char_class___range__115_115_char_class___range__116_116_ = (IConstructor) _read("prod(lit(\"test\"),[\\char-class([range(116,116)]),\\char-class([range(101,101)]),\\char-class([range(115,115)]),\\char-class([range(116,116)])],{})", Factory.Production);
  private static final IConstructor prod__PathChars__URLChars_char_class___range__124_124_ = (IConstructor) _read("prod(lex(\"PathChars\"),[lex(\"URLChars\"),\\char-class([range(124,124)])],{})", Factory.Production);
  private static final IConstructor prod__Default_Bound__lit___59_layouts_LAYOUTLIST_expression_Expression_ = (IConstructor) _read("prod(label(\"Default\",sort(\"Bound\")),[lit(\";\"),layouts(\"LAYOUTLIST\"),label(\"expression\",sort(\"Expression\"))],{})", Factory.Production);
  private static final IConstructor prod__Int_BasicType__lit_int_ = (IConstructor) _read("prod(label(\"Int\",sort(\"BasicType\")),[lit(\"int\")],{})", Factory.Production);
  private static final IConstructor prod__Real_BasicType__lit_real_ = (IConstructor) _read("prod(label(\"Real\",sort(\"BasicType\")),[lit(\"real\")],{})", Factory.Production);
  private static final IConstructor regular__iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST = (IConstructor) _read("regular(\\iter-seps(sort(\"Expression\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")]))", Factory.Production);
  private static final IConstructor prod__List_Pattern__lit___91_layouts_LAYOUTLIST_elements_iter_star_seps__Pattern__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___93_ = (IConstructor) _read("prod(label(\"List\",sort(\"Pattern\")),[lit(\"[\"),layouts(\"LAYOUTLIST\"),label(\"elements\",\\iter-star-seps(sort(\"Pattern\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\"]\")],{})", Factory.Production);
  private static final IConstructor prod__lit_false__char_class___range__102_102_char_class___range__97_97_char_class___range__108_108_char_class___range__115_115_char_class___range__101_101_ = (IConstructor) _read("prod(lit(\"false\"),[\\char-class([range(102,102)]),\\char-class([range(97,97)]),\\char-class([range(108,108)]),\\char-class([range(115,115)]),\\char-class([range(101,101)])],{})", Factory.Production);
  private static final IConstructor prod__RascalKeywords__lit_loc_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"loc\")],{})", Factory.Production);
  private static final IConstructor regular__iter_star__NamedRegExp = (IConstructor) _read("regular(\\iter-star(lex(\"NamedRegExp\")))", Factory.Production);
  private static final IConstructor prod__Enumerator_Expression__pattern_Pattern_layouts_LAYOUTLIST_lit___60_45_layouts_LAYOUTLIST_expression_Expression__assoc__non_assoc = (IConstructor) _read("prod(label(\"Enumerator\",sort(\"Expression\")),[label(\"pattern\",sort(\"Pattern\")),layouts(\"LAYOUTLIST\"),lit(\"\\<-\"),layouts(\"LAYOUTLIST\"),label(\"expression\",sort(\"Expression\"))],{assoc(\\non-assoc())})", Factory.Production);
  private static final IConstructor prod__lit_set__char_class___range__115_115_char_class___range__101_101_char_class___range__116_116_ = (IConstructor) _read("prod(lit(\"set\"),[\\char-class([range(115,115)]),\\char-class([range(101,101)]),\\char-class([range(116,116)])],{})", Factory.Production);
  private static final IConstructor prod__Declaration_EvalCommand__declaration_Declaration_ = (IConstructor) _read("prod(label(\"Declaration\",sort(\"EvalCommand\")),[label(\"declaration\",sort(\"Declaration\"))],{})", Factory.Production);
  private static final IConstructor prod__Module_Kind__lit_module_ = (IConstructor) _read("prod(label(\"Module\",sort(\"Kind\")),[lit(\"module\")],{})", Factory.Production);
  private static final IConstructor prod__lit_bottom_up_break__char_class___range__98_98_char_class___range__111_111_char_class___range__116_116_char_class___range__116_116_char_class___range__111_111_char_class___range__109_109_char_class___range__45_45_char_class___range__117_117_char_class___range__112_112_char_class___range__45_45_char_class___range__98_98_char_class___range__114_114_char_class___range__101_101_char_class___range__97_97_char_class___range__107_107_ = (IConstructor) _read("prod(lit(\"bottom-up-break\"),[\\char-class([range(98,98)]),\\char-class([range(111,111)]),\\char-class([range(116,116)]),\\char-class([range(116,116)]),\\char-class([range(111,111)]),\\char-class([range(109,109)]),\\char-class([range(45,45)]),\\char-class([range(117,117)]),\\char-class([range(112,112)]),\\char-class([range(45,45)]),\\char-class([range(98,98)]),\\char-class([range(114,114)]),\\char-class([range(101,101)]),\\char-class([range(97,97)]),\\char-class([range(107,107)])],{})", Factory.Production);
  private static final IConstructor prod__Integer_Literal__integerLiteral_IntegerLiteral_ = (IConstructor) _read("prod(label(\"Integer\",sort(\"Literal\")),[label(\"integerLiteral\",sort(\"IntegerLiteral\"))],{})", Factory.Production);
  private static final IConstructor prod__PatternWithAction_Case__lit_case_layouts_LAYOUTLIST_patternWithAction_PatternWithAction__tag__Foldable = (IConstructor) _read("prod(label(\"PatternWithAction\",sort(\"Case\")),[lit(\"case\"),layouts(\"LAYOUTLIST\"),label(\"patternWithAction\",sort(\"PatternWithAction\"))],{tag(Foldable())})", Factory.Production);
  private static final IConstructor regular__iter_star__alt___TagString_char_class___range__0_122_range__124_124_range__126_16777215_seq___lit___92_char_class___range__123_123_range__125_125 = (IConstructor) _read("regular(\\iter-star(alt({lex(\"TagString\"),\\char-class([range(0,122),range(124,124),range(126,16777215)]),seq([lit(\"\\\\\"),\\char-class([range(123,123),range(125,125)])])})))", Factory.Production);
  private static final IConstructor regular__iter_seps__Variable__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST = (IConstructor) _read("regular(\\iter-seps(sort(\"Variable\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")]))", Factory.Production);
  private static final IConstructor prod__Except_Sym__symbol_Sym_layouts_LAYOUTLIST_lit___33_layouts_LAYOUTLIST_label_NonterminalLabel_ = (IConstructor) _read("prod(label(\"Except\",sort(\"Sym\")),[label(\"symbol\",sort(\"Sym\")),layouts(\"LAYOUTLIST\"),lit(\"!\"),layouts(\"LAYOUTLIST\"),label(\"label\",lex(\"NonterminalLabel\"))],{})", Factory.Production);
  private static final IConstructor prod__Node_BasicType__lit_node_ = (IConstructor) _read("prod(label(\"Node\",sort(\"BasicType\")),[lit(\"node\")],{})", Factory.Production);
  private static final IConstructor prod__lit_throws__char_class___range__116_116_char_class___range__104_104_char_class___range__114_114_char_class___range__111_111_char_class___range__119_119_char_class___range__115_115_ = (IConstructor) _read("prod(lit(\"throws\"),[\\char-class([range(116,116)]),\\char-class([range(104,104)]),\\char-class([range(114,114)]),\\char-class([range(111,111)]),\\char-class([range(119,119)]),\\char-class([range(115,115)])],{})", Factory.Production);
  private static final IConstructor prod__lit_bag__char_class___range__98_98_char_class___range__97_97_char_class___range__103_103_ = (IConstructor) _read("prod(lit(\"bag\"),[\\char-class([range(98,98)]),\\char-class([range(97,97)]),\\char-class([range(103,103)])],{})", Factory.Production);
  private static final IConstructor prod__lit_visit__char_class___range__118_118_char_class___range__105_105_char_class___range__115_115_char_class___range__105_105_char_class___range__116_116_ = (IConstructor) _read("prod(lit(\"visit\"),[\\char-class([range(118,118)]),\\char-class([range(105,105)]),\\char-class([range(115,115)]),\\char-class([range(105,105)]),\\char-class([range(116,116)])],{})", Factory.Production);
  private static final IConstructor prod__Others_Prod__lit___46_46_46_ = (IConstructor) _read("prod(label(\"Others\",sort(\"Prod\")),[lit(\"...\")],{})", Factory.Production);
  private static final IConstructor regular__iter_star_seps__ProdModifier__layouts_LAYOUTLIST = (IConstructor) _read("regular(\\iter-star-seps(sort(\"ProdModifier\"),[layouts(\"LAYOUTLIST\")]))", Factory.Production);
  private static final IConstructor regular__iter_star_seps__Toplevel__layouts_LAYOUTLIST = (IConstructor) _read("regular(\\iter-star-seps(sort(\"Toplevel\"),[layouts(\"LAYOUTLIST\")]))", Factory.Production);
  private static final IConstructor prod__Parameter_Sym__lit___38_layouts_LAYOUTLIST_nonterminal_Nonterminal_ = (IConstructor) _read("prod(label(\"Parameter\",sort(\"Sym\")),[lit(\"&\"),layouts(\"LAYOUTLIST\"),label(\"nonterminal\",lex(\"Nonterminal\"))],{})", Factory.Production);
  private static final IConstructor prod__DecimalIntegerLiteral_IntegerLiteral__decimal_DecimalIntegerLiteral_ = (IConstructor) _read("prod(label(\"DecimalIntegerLiteral\",sort(\"IntegerLiteral\")),[label(\"decimal\",lex(\"DecimalIntegerLiteral\"))],{})", Factory.Production);
  private static final IConstructor prod__Empty_Label__ = (IConstructor) _read("prod(label(\"Empty\",sort(\"Label\")),[],{})", Factory.Production);
  private static final IConstructor prod__Column_Sym__symbol_Sym_layouts_LAYOUTLIST_lit___64_layouts_LAYOUTLIST_column_IntegerLiteral_ = (IConstructor) _read("prod(label(\"Column\",sort(\"Sym\")),[label(\"symbol\",sort(\"Sym\")),layouts(\"LAYOUTLIST\"),lit(\"@\"),layouts(\"LAYOUTLIST\"),label(\"column\",sort(\"IntegerLiteral\"))],{})", Factory.Production);
  private static final IConstructor prod__Default_FunctionModifier__lit_default_ = (IConstructor) _read("prod(label(\"Default\",sort(\"FunctionModifier\")),[lit(\"default\")],{})", Factory.Production);
  private static final IConstructor prod__RascalKeywords__lit_one_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"one\")],{})", Factory.Production);
  private static final IConstructor regular__iter_star__RegExp = (IConstructor) _read("regular(\\iter-star(lex(\"RegExp\")))", Factory.Production);
  private static final IConstructor regular__iter_seps__Assignable__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST = (IConstructor) _read("regular(\\iter-seps(sort(\"Assignable\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")]))", Factory.Production);
  private static final IConstructor prod__Default_Formals__formals_iter_star_seps__Pattern__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_ = (IConstructor) _read("prod(label(\"Default\",sort(\"Formals\")),[label(\"formals\",\\iter-star-seps(sort(\"Pattern\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")]))],{})", Factory.Production);
  private static final IConstructor prod__RascalKeywords__lit_node_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"node\")],{})", Factory.Production);
  private static final IConstructor prod__Type_BasicType__lit_type_ = (IConstructor) _read("prod(label(\"Type\",sort(\"BasicType\")),[lit(\"type\")],{})", Factory.Production);
  private static final IConstructor regular__iter_star_seps__Tag__layouts_LAYOUTLIST = (IConstructor) _read("regular(\\iter-star-seps(sort(\"Tag\"),[layouts(\"LAYOUTLIST\")]))", Factory.Production);
  private static final IConstructor regular__iter_star__char_class___range__0_16777215 = (IConstructor) _read("regular(\\iter-star(\\char-class([range(0,16777215)])))", Factory.Production);
  private static final IConstructor prod__Void_BasicType__lit_void_ = (IConstructor) _read("prod(label(\"Void\",sort(\"BasicType\")),[lit(\"void\")],{})", Factory.Production);
  private static final IConstructor prod__Reducer_Expression__lit___40_layouts_LAYOUTLIST_init_Expression_layouts_LAYOUTLIST_lit___124_layouts_LAYOUTLIST_result_Expression_layouts_LAYOUTLIST_lit___124_layouts_LAYOUTLIST_generators_iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_ = (IConstructor) _read("prod(label(\"Reducer\",sort(\"Expression\")),[lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"init\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"|\"),layouts(\"LAYOUTLIST\"),label(\"result\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"|\"),layouts(\"LAYOUTLIST\"),label(\"generators\",\\iter-seps(sort(\"Expression\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\")\")],{})", Factory.Production);
  private static final IConstructor prod__VariableBecomes_Pattern__name_Name_layouts_LAYOUTLIST_lit___58_layouts_LAYOUTLIST_pattern_Pattern_ = (IConstructor) _read("prod(label(\"VariableBecomes\",sort(\"Pattern\")),[label(\"name\",lex(\"Name\")),layouts(\"LAYOUTLIST\"),lit(\":\"),layouts(\"LAYOUTLIST\"),label(\"pattern\",sort(\"Pattern\"))],{})", Factory.Production);
  private static final IConstructor prod__RascalKeywords__lit_bracket_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"bracket\")],{})", Factory.Production);
  private static final IConstructor prod__RascalKeywords__lit_continue_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"continue\")],{})", Factory.Production);
  private static final IConstructor prod__RealLiteral__iter__char_class___range__48_57_char_class___range__68_68_range__70_70_range__100_100_range__102_102_ = (IConstructor) _read("prod(lex(\"RealLiteral\"),[iter(\\char-class([range(48,57)])),\\char-class([range(68,68),range(70,70),range(100,100),range(102,102)])],{})", Factory.Production);
  private static final IConstructor prod__Default_LocalVariableDeclaration__declarator_Declarator_ = (IConstructor) _read("prod(label(\"Default\",sort(\"LocalVariableDeclaration\")),[label(\"declarator\",sort(\"Declarator\"))],{})", Factory.Production);
  private static final IConstructor prod__RealLiteral__iter__char_class___range__48_57_lit___46_iter_star__char_class___range__48_57_char_class___range__69_69_range__101_101_opt__char_class___range__43_43_range__45_45_iter__char_class___range__48_57_opt__char_class___range__68_68_range__70_70_range__100_100_range__102_102_ = (IConstructor) _read("prod(lex(\"RealLiteral\"),[iter(\\char-class([range(48,57)])),lit(\".\"),\\iter-star(\\char-class([range(48,57)])),\\char-class([range(69,69),range(101,101)]),opt(\\char-class([range(43,43),range(45,45)])),iter(\\char-class([range(48,57)])),opt(\\char-class([range(68,68),range(70,70),range(100,100),range(102,102)]))],{})", Factory.Production);
  private static final IConstructor prod__RascalKeywords__lit_num_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"num\")],{})", Factory.Production);
  private static final IConstructor prod__VoidClosure_Expression__parameters_Parameters_layouts_LAYOUTLIST_lit___123_layouts_LAYOUTLIST_statements_iter_star_seps__Statement__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_ = (IConstructor) _read("prod(label(\"VoidClosure\",sort(\"Expression\")),[label(\"parameters\",sort(\"Parameters\")),layouts(\"LAYOUTLIST\"),lit(\"{\"),layouts(\"LAYOUTLIST\"),label(\"statements\",\\iter-star-seps(sort(\"Statement\"),[layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\"}\")],{})", Factory.Production);
  private static final IConstructor prod__LAYOUT__char_class___range__9_10_range__13_13_range__32_32_ = (IConstructor) _read("prod(lex(\"LAYOUT\"),[\\char-class([range(9,10),range(13,13),range(32,32)])],{})", Factory.Production);
  private static final IConstructor prod__MidInterpolated_StringTail__mid_MidStringChars_layouts_LAYOUTLIST_expression_Expression_layouts_LAYOUTLIST_tail_StringTail_ = (IConstructor) _read("prod(label(\"MidInterpolated\",sort(\"StringTail\")),[label(\"mid\",lex(\"MidStringChars\")),layouts(\"LAYOUTLIST\"),label(\"expression\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),label(\"tail\",sort(\"StringTail\"))],{})", Factory.Production);
  private static final IConstructor prod__BooleanLiteral__lit_true_ = (IConstructor) _read("prod(lex(\"BooleanLiteral\"),[lit(\"true\")],{})", Factory.Production);
  private static final IConstructor prod__lit_public__char_class___range__112_112_char_class___range__117_117_char_class___range__98_98_char_class___range__108_108_char_class___range__105_105_char_class___range__99_99_ = (IConstructor) _read("prod(lit(\"public\"),[\\char-class([range(112,112)]),\\char-class([range(117,117)]),\\char-class([range(98,98)]),\\char-class([range(108,108)]),\\char-class([range(105,105)]),\\char-class([range(99,99)])],{})", Factory.Production);
  private static final IConstructor prod__ReifiedType_Pattern__lit_type_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_symbol_Pattern_layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_definitions_Pattern_layouts_LAYOUTLIST_lit___41_ = (IConstructor) _read("prod(label(\"ReifiedType\",sort(\"Pattern\")),[lit(\"type\"),layouts(\"LAYOUTLIST\"),lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"symbol\",sort(\"Pattern\")),layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\"),label(\"definitions\",sort(\"Pattern\")),layouts(\"LAYOUTLIST\"),lit(\")\")],{})", Factory.Production);
  private static final IConstructor prod__lit_rat__char_class___range__114_114_char_class___range__97_97_char_class___range__116_116_ = (IConstructor) _read("prod(lit(\"rat\"),[\\char-class([range(114,114)]),\\char-class([range(97,97)]),\\char-class([range(116,116)])],{})", Factory.Production);
  private static final IConstructor prod__DateAndTime__lit___36_DatePart_lit_T_TimePartNoTZ_TimeZonePart_ = (IConstructor) _read("prod(lex(\"DateAndTime\"),[lit(\"$\"),lex(\"DatePart\"),lit(\"T\"),lex(\"TimePartNoTZ\"),lex(\"TimeZonePart\")],{})", Factory.Production);
  private static final IConstructor prod__GivenStrategy_Visit__strategy_Strategy_layouts_LAYOUTLIST_lit_visit_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_subject_Expression_layouts_LAYOUTLIST_lit___41_layouts_LAYOUTLIST_lit___123_layouts_LAYOUTLIST_cases_iter_seps__Case__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_ = (IConstructor) _read("prod(label(\"GivenStrategy\",sort(\"Visit\")),[label(\"strategy\",sort(\"Strategy\")),layouts(\"LAYOUTLIST\"),lit(\"visit\"),layouts(\"LAYOUTLIST\"),lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"subject\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\")\"),layouts(\"LAYOUTLIST\"),lit(\"{\"),layouts(\"LAYOUTLIST\"),label(\"cases\",\\iter-seps(sort(\"Case\"),[layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\"}\")],{})", Factory.Production);
  private static final IConstructor prod__lit_renaming__char_class___range__114_114_char_class___range__101_101_char_class___range__110_110_char_class___range__97_97_char_class___range__109_109_char_class___range__105_105_char_class___range__110_110_char_class___range__103_103_ = (IConstructor) _read("prod(lit(\"renaming\"),[\\char-class([range(114,114)]),\\char-class([range(101,101)]),\\char-class([range(110,110)]),\\char-class([range(97,97)]),\\char-class([range(109,109)]),\\char-class([range(105,105)]),\\char-class([range(110,110)]),\\char-class([range(103,103)])],{})", Factory.Production);
  private static final IConstructor prod__NamedRegExp__char_class___range__0_46_range__48_59_range__61_61_range__63_91_range__93_16777215_ = (IConstructor) _read("prod(lex(\"NamedRegExp\"),[\\char-class([range(0,46),range(48,59),range(61,61),range(63,91),range(93,16777215)])],{})", Factory.Production);
  private static final IConstructor prod__Composition_Expression__lhs_Expression_layouts_LAYOUTLIST_lit_o_layouts_LAYOUTLIST_rhs_Expression__assoc__left = (IConstructor) _read("prod(label(\"Composition\",sort(\"Expression\")),[label(\"lhs\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"o\"),layouts(\"LAYOUTLIST\"),label(\"rhs\",sort(\"Expression\"))],{assoc(left())})", Factory.Production);
  private static final IConstructor prod__NonterminalLabel__char_class___range__97_122_conditional__iter_star__char_class___range__48_57_range__65_90_range__95_95_range__97_122__not_follow__char_class___range__48_57_range__65_90_range__95_95_range__97_122_ = (IConstructor) _read("prod(lex(\"NonterminalLabel\"),[\\char-class([range(97,122)]),conditional(\\iter-star(\\char-class([range(48,57),range(65,90),range(95,95),range(97,122)])),{\\not-follow(\\char-class([range(48,57),range(65,90),range(95,95),range(97,122)]))})],{})", Factory.Production);
  private static final IConstructor prod__lit_value__char_class___range__118_118_char_class___range__97_97_char_class___range__108_108_char_class___range__117_117_char_class___range__101_101_ = (IConstructor) _read("prod(lit(\"value\"),[\\char-class([range(118,118)]),\\char-class([range(97,97)]),\\char-class([range(108,108)]),\\char-class([range(117,117)]),\\char-class([range(101,101)])],{})", Factory.Production);
  private static final IConstructor prod__Literal_Expression__literal_Literal_ = (IConstructor) _read("prod(label(\"Literal\",sort(\"Expression\")),[label(\"literal\",sort(\"Literal\"))],{})", Factory.Production);
  private static final IConstructor prod__RegExp__Backslash_ = (IConstructor) _read("prod(lex(\"RegExp\"),[lex(\"Backslash\")],{})", Factory.Production);
  private static final IConstructor prod__IsDefined_Expression__argument_Expression_layouts_LAYOUTLIST_lit___63_ = (IConstructor) _read("prod(label(\"IsDefined\",sort(\"Expression\")),[label(\"argument\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"?\")],{})", Factory.Production);
  private static final IConstructor prod__Rest__conditional__iter_star__char_class___range__0_16777215__not_follow__char_class___range__0_16777215_ = (IConstructor) _read("prod(lex(\"Rest\"),[conditional(\\iter-star(\\char-class([range(0,16777215)])),{\\not-follow(\\char-class([range(0,16777215)]))})],{})", Factory.Production);
  private static final IConstructor prod__lit_module__char_class___range__109_109_char_class___range__111_111_char_class___range__100_100_char_class___range__117_117_char_class___range__108_108_char_class___range__101_101_ = (IConstructor) _read("prod(lit(\"module\"),[\\char-class([range(109,109)]),\\char-class([range(111,111)]),\\char-class([range(100,100)]),\\char-class([range(117,117)]),\\char-class([range(108,108)]),\\char-class([range(101,101)])],{})", Factory.Production);
  private static final IConstructor prod__RascalKeywords__lit_solve_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"solve\")],{})", Factory.Production);
  private static final IConstructor prod__NamedRegExp__NamedBackslash_ = (IConstructor) _read("prod(lex(\"NamedRegExp\"),[lex(\"NamedBackslash\")],{})", Factory.Production);
    
  // Item declarations
	
	
  protected static class Literal {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__DateTime_Literal__dateTimeLiteral_DateTimeLiteral_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(36, 0, "DateTimeLiteral", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__DateTime_Literal__dateTimeLiteral_DateTimeLiteral_, tmp);
	}
    protected static final void _init_prod__String_Literal__stringLiteral_StringLiteral_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(38, 0, "StringLiteral", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__String_Literal__stringLiteral_StringLiteral_, tmp);
	}
    protected static final void _init_prod__Real_Literal__realLiteral_RealLiteral_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(40, 0, "RealLiteral", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Real_Literal__realLiteral_RealLiteral_, tmp);
	}
    protected static final void _init_prod__Location_Literal__locationLiteral_LocationLiteral_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(42, 0, "LocationLiteral", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Location_Literal__locationLiteral_LocationLiteral_, tmp);
	}
    protected static final void _init_prod__Rational_Literal__rationalLiteral_RationalLiteral_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(44, 0, "RationalLiteral", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Rational_Literal__rationalLiteral_RationalLiteral_, tmp);
	}
    protected static final void _init_prod__RegExp_Literal__regExpLiteral_RegExpLiteral_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(46, 0, "RegExpLiteral", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RegExp_Literal__regExpLiteral_RegExpLiteral_, tmp);
	}
    protected static final void _init_prod__Boolean_Literal__booleanLiteral_BooleanLiteral_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(48, 0, "BooleanLiteral", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Boolean_Literal__booleanLiteral_BooleanLiteral_, tmp);
	}
    protected static final void _init_prod__Integer_Literal__integerLiteral_IntegerLiteral_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(50, 0, "IntegerLiteral", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Integer_Literal__integerLiteral_IntegerLiteral_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__DateTime_Literal__dateTimeLiteral_DateTimeLiteral_(builder);
      
        _init_prod__String_Literal__stringLiteral_StringLiteral_(builder);
      
        _init_prod__Real_Literal__realLiteral_RealLiteral_(builder);
      
        _init_prod__Location_Literal__locationLiteral_LocationLiteral_(builder);
      
        _init_prod__Rational_Literal__rationalLiteral_RationalLiteral_(builder);
      
        _init_prod__RegExp_Literal__regExpLiteral_RegExpLiteral_(builder);
      
        _init_prod__Boolean_Literal__booleanLiteral_BooleanLiteral_(builder);
      
        _init_prod__Integer_Literal__integerLiteral_IntegerLiteral_(builder);
      
    }
  }
	
  protected static class Module {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__Default_Module__header_Header_layouts_LAYOUTLIST_body_Body_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode<IConstructor>(56, 2, "Body", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(54, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(52, 0, "Header", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Default_Module__header_Header_layouts_LAYOUTLIST_body_Body_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__Default_Module__header_Header_layouts_LAYOUTLIST_body_Body_(builder);
      
    }
  }
	
  protected static class PreProtocolChars {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__PreProtocolChars__lit___124_URLChars_lit___60_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new LiteralStackNode<IConstructor>(62, 2, prod__lit___60__char_class___range__60_60_, new int[] {60}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(60, 1, "URLChars", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(58, 0, prod__lit___124__char_class___range__124_124_, new int[] {124}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__PreProtocolChars__lit___124_URLChars_lit___60_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__PreProtocolChars__lit___124_URLChars_lit___60_(builder);
      
    }
  }
	
  protected static class Variable {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__UnInitialized_Variable__name_Name_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(122, 0, "Name", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__UnInitialized_Variable__name_Name_, tmp);
	}
    protected static final void _init_prod__Initialized_Variable__name_Name_layouts_LAYOUTLIST_lit___61_layouts_LAYOUTLIST_initial_Expression_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(132, 4, "Expression", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(130, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(128, 2, prod__lit___61__char_class___range__61_61_, new int[] {61}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(126, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(124, 0, "Name", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Initialized_Variable__name_Name_layouts_LAYOUTLIST_lit___61_layouts_LAYOUTLIST_initial_Expression_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__UnInitialized_Variable__name_Name_(builder);
      
        _init_prod__Initialized_Variable__name_Name_layouts_LAYOUTLIST_lit___61_layouts_LAYOUTLIST_initial_Expression_(builder);
      
    }
  }
	
  protected static class TypeArg {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__Default_TypeArg__type_Type_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(114, 0, "Type", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Default_TypeArg__type_Type_, tmp);
	}
    protected static final void _init_prod__Named_TypeArg__type_Type_layouts_LAYOUTLIST_name_Name_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode<IConstructor>(120, 2, "Name", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(118, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(116, 0, "Type", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Named_TypeArg__type_Type_layouts_LAYOUTLIST_name_Name_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__Default_TypeArg__type_Type_(builder);
      
        _init_prod__Named_TypeArg__type_Type_layouts_LAYOUTLIST_name_Name_(builder);
      
    }
  }
	
  protected static class Catch {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__Binding_Catch__lit_catch_layouts_LAYOUTLIST_pattern_Pattern_layouts_LAYOUTLIST_lit___58_layouts_LAYOUTLIST_body_Statement_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[7];
      
      tmp[6] = new NonTerminalStackNode<IConstructor>(164, 6, "Statement", null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(162, 5, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode<IConstructor>(160, 4, prod__lit___58__char_class___range__58_58_, new int[] {58}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(158, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(156, 2, "Pattern", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(154, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(152, 0, prod__lit_catch__char_class___range__99_99_char_class___range__97_97_char_class___range__116_116_char_class___range__99_99_char_class___range__104_104_, new int[] {99,97,116,99,104}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Binding_Catch__lit_catch_layouts_LAYOUTLIST_pattern_Pattern_layouts_LAYOUTLIST_lit___58_layouts_LAYOUTLIST_body_Statement_, tmp);
	}
    protected static final void _init_prod__Default_Catch__lit_catch_layouts_LAYOUTLIST_lit___58_layouts_LAYOUTLIST_body_Statement_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(174, 4, "Statement", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(172, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(170, 2, prod__lit___58__char_class___range__58_58_, new int[] {58}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(168, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(166, 0, prod__lit_catch__char_class___range__99_99_char_class___range__97_97_char_class___range__116_116_char_class___range__99_99_char_class___range__104_104_, new int[] {99,97,116,99,104}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Default_Catch__lit_catch_layouts_LAYOUTLIST_lit___58_layouts_LAYOUTLIST_body_Statement_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__Binding_Catch__lit_catch_layouts_LAYOUTLIST_pattern_Pattern_layouts_LAYOUTLIST_lit___58_layouts_LAYOUTLIST_body_Statement_(builder);
      
        _init_prod__Default_Catch__lit_catch_layouts_LAYOUTLIST_lit___58_layouts_LAYOUTLIST_body_Statement_(builder);
      
    }
  }
	
  protected static class Renaming {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__Default_Renaming__from_Name_layouts_LAYOUTLIST_lit___61_62_layouts_LAYOUTLIST_to_Name_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(150, 4, "Name", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(148, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(146, 2, prod__lit___61_62__char_class___range__61_61_char_class___range__62_62_, new int[] {61,62}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(144, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(142, 0, "Name", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Default_Renaming__from_Name_layouts_LAYOUTLIST_lit___61_62_layouts_LAYOUTLIST_to_Name_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__Default_Renaming__from_Name_layouts_LAYOUTLIST_lit___61_62_layouts_LAYOUTLIST_to_Name_(builder);
      
    }
  }
	
  protected static class Signature {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__WithThrows_Signature__modifiers_FunctionModifiers_layouts_LAYOUTLIST_type_Type_layouts_LAYOUTLIST_name_Name_layouts_LAYOUTLIST_parameters_Parameters_layouts_LAYOUTLIST_lit_throws_layouts_LAYOUTLIST_exceptions_iter_seps__Type__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[11];
      
      tmp[10] = new SeparatedListStackNode<IConstructor>(210, 10, regular__iter_seps__Type__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(212, 0, "Type", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(214, 1, "layouts_LAYOUTLIST", null, null), new LiteralStackNode<IConstructor>(216, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode<IConstructor>(218, 3, "layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(190, 0, "FunctionModifiers", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(192, 1, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(194, 2, "Type", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(196, 3, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new NonTerminalStackNode<IConstructor>(198, 4, "Name", null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(200, 5, "layouts_LAYOUTLIST", null, null);
      tmp[6] = new NonTerminalStackNode<IConstructor>(202, 6, "Parameters", null, null);
      tmp[7] = new NonTerminalStackNode<IConstructor>(204, 7, "layouts_LAYOUTLIST", null, null);
      tmp[8] = new LiteralStackNode<IConstructor>(206, 8, prod__lit_throws__char_class___range__116_116_char_class___range__104_104_char_class___range__114_114_char_class___range__111_111_char_class___range__119_119_char_class___range__115_115_, new int[] {116,104,114,111,119,115}, null, null);
      tmp[9] = new NonTerminalStackNode<IConstructor>(208, 9, "layouts_LAYOUTLIST", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__WithThrows_Signature__modifiers_FunctionModifiers_layouts_LAYOUTLIST_type_Type_layouts_LAYOUTLIST_name_Name_layouts_LAYOUTLIST_parameters_Parameters_layouts_LAYOUTLIST_lit_throws_layouts_LAYOUTLIST_exceptions_iter_seps__Type__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_, tmp);
	}
    protected static final void _init_prod__NoThrows_Signature__modifiers_FunctionModifiers_layouts_LAYOUTLIST_type_Type_layouts_LAYOUTLIST_name_Name_layouts_LAYOUTLIST_parameters_Parameters_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[7];
      
      tmp[6] = new NonTerminalStackNode<IConstructor>(188, 6, "Parameters", null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(186, 5, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new NonTerminalStackNode<IConstructor>(184, 4, "Name", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(182, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(180, 2, "Type", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(178, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(176, 0, "FunctionModifiers", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__NoThrows_Signature__modifiers_FunctionModifiers_layouts_LAYOUTLIST_type_Type_layouts_LAYOUTLIST_name_Name_layouts_LAYOUTLIST_parameters_Parameters_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__WithThrows_Signature__modifiers_FunctionModifiers_layouts_LAYOUTLIST_type_Type_layouts_LAYOUTLIST_name_Name_layouts_LAYOUTLIST_parameters_Parameters_layouts_LAYOUTLIST_lit_throws_layouts_LAYOUTLIST_exceptions_iter_seps__Type__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_(builder);
      
        _init_prod__NoThrows_Signature__modifiers_FunctionModifiers_layouts_LAYOUTLIST_type_Type_layouts_LAYOUTLIST_name_Name_layouts_LAYOUTLIST_parameters_Parameters_(builder);
      
    }
  }
	
  protected static class Sym {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__Empty_Sym__lit___40_layouts_LAYOUTLIST_lit___41_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new LiteralStackNode<IConstructor>(238, 2, prod__lit___41__char_class___range__41_41_, new int[] {41}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(236, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(234, 0, prod__lit___40__char_class___range__40_40_, new int[] {40}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Empty_Sym__lit___40_layouts_LAYOUTLIST_lit___41_, tmp);
	}
    protected static final void _init_prod__CharacterClass_Sym__charClass_Class_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(240, 0, "Class", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__CharacterClass_Sym__charClass_Class_, tmp);
	}
    protected static final void _init_prod__Sequence_Sym__lit___40_layouts_LAYOUTLIST_first_Sym_layouts_LAYOUTLIST_sequence_iter_seps__Sym__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[7];
      
      tmp[6] = new LiteralStackNode<IConstructor>(258, 6, prod__lit___41__char_class___range__41_41_, new int[] {41}, null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(256, 5, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new SeparatedListStackNode<IConstructor>(250, 4, regular__iter_seps__Sym__layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(252, 0, "Sym", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(254, 1, "layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(248, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(246, 2, "Sym", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(244, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(242, 0, prod__lit___40__char_class___range__40_40_, new int[] {40}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Sequence_Sym__lit___40_layouts_LAYOUTLIST_first_Sym_layouts_LAYOUTLIST_sequence_iter_seps__Sym__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_, tmp);
	}
    protected static final void _init_prod__NotPrecede_Sym__match_Sym_layouts_LAYOUTLIST_lit___33_60_60_layouts_LAYOUTLIST_symbol_Sym__assoc__right(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(454, 4, "Sym", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(452, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(450, 2, prod__lit___33_60_60__char_class___range__33_33_char_class___range__60_60_char_class___range__60_60_, new int[] {33,60,60}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(448, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(446, 0, "Sym", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__NotPrecede_Sym__match_Sym_layouts_LAYOUTLIST_lit___33_60_60_layouts_LAYOUTLIST_symbol_Sym__assoc__right, tmp);
	}
    protected static final void _init_prod__IterStar_Sym__symbol_Sym_layouts_LAYOUTLIST_lit___42_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new LiteralStackNode<IConstructor>(264, 2, prod__lit___42__char_class___range__42_42_, new int[] {42}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(262, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(260, 0, "Sym", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__IterStar_Sym__symbol_Sym_layouts_LAYOUTLIST_lit___42_, tmp);
	}
    protected static final void _init_prod__Labeled_Sym__symbol_Sym_layouts_LAYOUTLIST_label_NonterminalLabel_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode<IConstructor>(270, 2, "NonterminalLabel", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(268, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(266, 0, "Sym", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Labeled_Sym__symbol_Sym_layouts_LAYOUTLIST_label_NonterminalLabel_, tmp);
	}
    protected static final void _init_prod__Parameter_Sym__lit___38_layouts_LAYOUTLIST_nonterminal_Nonterminal_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode<IConstructor>(276, 2, "Nonterminal", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(274, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(272, 0, prod__lit___38__char_class___range__38_38_, new int[] {38}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Parameter_Sym__lit___38_layouts_LAYOUTLIST_nonterminal_Nonterminal_, tmp);
	}
    protected static final void _init_prod__IterStarSep_Sym__lit___123_layouts_LAYOUTLIST_symbol_Sym_layouts_LAYOUTLIST_sep_Sym_layouts_LAYOUTLIST_lit___125_layouts_LAYOUTLIST_lit___42_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[9];
      
      tmp[8] = new LiteralStackNode<IConstructor>(294, 8, prod__lit___42__char_class___range__42_42_, new int[] {42}, null, null);
      tmp[7] = new NonTerminalStackNode<IConstructor>(292, 7, "layouts_LAYOUTLIST", null, null);
      tmp[6] = new LiteralStackNode<IConstructor>(290, 6, prod__lit___125__char_class___range__125_125_, new int[] {125}, null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(288, 5, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new NonTerminalStackNode<IConstructor>(286, 4, "Sym", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(284, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(282, 2, "Sym", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(280, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(278, 0, prod__lit___123__char_class___range__123_123_, new int[] {123}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__IterStarSep_Sym__lit___123_layouts_LAYOUTLIST_symbol_Sym_layouts_LAYOUTLIST_sep_Sym_layouts_LAYOUTLIST_lit___125_layouts_LAYOUTLIST_lit___42_, tmp);
	}
    protected static final void _init_prod__Alternative_Sym__lit___40_layouts_LAYOUTLIST_first_Sym_layouts_LAYOUTLIST_lit___124_layouts_LAYOUTLIST_alternatives_iter_seps__Sym__layouts_LAYOUTLIST_lit___124_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[9];
      
      tmp[8] = new LiteralStackNode<IConstructor>(320, 8, prod__lit___41__char_class___range__41_41_, new int[] {41}, null, null);
      tmp[7] = new NonTerminalStackNode<IConstructor>(318, 7, "layouts_LAYOUTLIST", null, null);
      tmp[6] = new SeparatedListStackNode<IConstructor>(308, 6, regular__iter_seps__Sym__layouts_LAYOUTLIST_lit___124_layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(310, 0, "Sym", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(312, 1, "layouts_LAYOUTLIST", null, null), new LiteralStackNode<IConstructor>(314, 2, prod__lit___124__char_class___range__124_124_, new int[] {124}, null, null), new NonTerminalStackNode<IConstructor>(316, 3, "layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(306, 5, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode<IConstructor>(304, 4, prod__lit___124__char_class___range__124_124_, new int[] {124}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(302, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(300, 2, "Sym", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(298, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(296, 0, prod__lit___40__char_class___range__40_40_, new int[] {40}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Alternative_Sym__lit___40_layouts_LAYOUTLIST_first_Sym_layouts_LAYOUTLIST_lit___124_layouts_LAYOUTLIST_alternatives_iter_seps__Sym__layouts_LAYOUTLIST_lit___124_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_, tmp);
	}
    protected static final void _init_prod__Follow_Sym__symbol_Sym_layouts_LAYOUTLIST_lit___62_62_layouts_LAYOUTLIST_match_Sym__assoc__left(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(444, 4, "Sym", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(442, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(440, 2, prod__lit___62_62__char_class___range__62_62_char_class___range__62_62_, new int[] {62,62}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(438, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(436, 0, "Sym", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Follow_Sym__symbol_Sym_layouts_LAYOUTLIST_lit___62_62_layouts_LAYOUTLIST_match_Sym__assoc__left, tmp);
	}
    protected static final void _init_prod__EndOfLine_Sym__symbol_Sym_layouts_LAYOUTLIST_lit___36_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new LiteralStackNode<IConstructor>(326, 2, prod__lit___36__char_class___range__36_36_, new int[] {36}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(324, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(322, 0, "Sym", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__EndOfLine_Sym__symbol_Sym_layouts_LAYOUTLIST_lit___36_, tmp);
	}
    protected static final void _init_prod__Precede_Sym__match_Sym_layouts_LAYOUTLIST_lit___60_60_layouts_LAYOUTLIST_symbol_Sym__assoc__right(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(464, 4, "Sym", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(462, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(460, 2, prod__lit___60_60__char_class___range__60_60_char_class___range__60_60_, new int[] {60,60}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(458, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(456, 0, "Sym", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Precede_Sym__match_Sym_layouts_LAYOUTLIST_lit___60_60_layouts_LAYOUTLIST_symbol_Sym__assoc__right, tmp);
	}
    protected static final void _init_prod__Column_Sym__symbol_Sym_layouts_LAYOUTLIST_lit___64_layouts_LAYOUTLIST_column_IntegerLiteral_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(336, 4, "IntegerLiteral", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(334, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(332, 2, prod__lit___64__char_class___range__64_64_, new int[] {64}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(330, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(328, 0, "Sym", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Column_Sym__symbol_Sym_layouts_LAYOUTLIST_lit___64_layouts_LAYOUTLIST_column_IntegerLiteral_, tmp);
	}
    protected static final void _init_prod__Nonterminal_Sym__conditional__nonterminal_Nonterminal__not_follow__lit___91_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(338, 0, "Nonterminal", null, new ICompletionFilter[] {new StringFollowRestriction(new int[] {91})});
      builder.addAlternative(ObjectRascalRascal.prod__Nonterminal_Sym__conditional__nonterminal_Nonterminal__not_follow__lit___91_, tmp);
	}
    protected static final void _init_prod__Optional_Sym__symbol_Sym_layouts_LAYOUTLIST_lit___63_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new LiteralStackNode<IConstructor>(344, 2, prod__lit___63__char_class___range__63_63_, new int[] {63}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(342, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(340, 0, "Sym", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Optional_Sym__symbol_Sym_layouts_LAYOUTLIST_lit___63_, tmp);
	}
    protected static final void _init_prod__Start_Sym__lit_start_layouts_LAYOUTLIST_lit___91_layouts_LAYOUTLIST_nonterminal_Nonterminal_layouts_LAYOUTLIST_lit___93_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[7];
      
      tmp[6] = new LiteralStackNode<IConstructor>(360, 6, prod__lit___93__char_class___range__93_93_, new int[] {93}, null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(358, 5, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new NonTerminalStackNode<IConstructor>(356, 4, "Nonterminal", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(354, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(352, 2, prod__lit___91__char_class___range__91_91_, new int[] {91}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(350, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(348, 0, prod__lit_start__char_class___range__115_115_char_class___range__116_116_char_class___range__97_97_char_class___range__114_114_char_class___range__116_116_, new int[] {115,116,97,114,116}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Start_Sym__lit_start_layouts_LAYOUTLIST_lit___91_layouts_LAYOUTLIST_nonterminal_Nonterminal_layouts_LAYOUTLIST_lit___93_, tmp);
	}
    protected static final void _init_prod__CaseInsensitiveLiteral_Sym__cistring_CaseInsensitiveStringConstant_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(346, 0, "CaseInsensitiveStringConstant", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__CaseInsensitiveLiteral_Sym__cistring_CaseInsensitiveStringConstant_, tmp);
	}
    protected static final void _init_prod__Unequal_Sym__symbol_Sym_layouts_LAYOUTLIST_lit___92_layouts_LAYOUTLIST_match_Sym__assoc__left(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(474, 4, "Sym", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(472, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(470, 2, prod__lit___92__char_class___range__92_92_, new int[] {92}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(468, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(466, 0, "Sym", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Unequal_Sym__symbol_Sym_layouts_LAYOUTLIST_lit___92_layouts_LAYOUTLIST_match_Sym__assoc__left, tmp);
	}
    protected static final void _init_prod__NotFollow_Sym__symbol_Sym_layouts_LAYOUTLIST_lit___33_62_62_layouts_LAYOUTLIST_match_Sym__assoc__left(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(434, 4, "Sym", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(432, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(430, 2, prod__lit___33_62_62__char_class___range__33_33_char_class___range__62_62_char_class___range__62_62_, new int[] {33,62,62}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(428, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(426, 0, "Sym", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__NotFollow_Sym__symbol_Sym_layouts_LAYOUTLIST_lit___33_62_62_layouts_LAYOUTLIST_match_Sym__assoc__left, tmp);
	}
    protected static final void _init_prod__Literal_Sym__string_StringConstant_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(362, 0, "StringConstant", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Literal_Sym__string_StringConstant_, tmp);
	}
    protected static final void _init_prod__Parametrized_Sym__conditional__nonterminal_Nonterminal__follow__lit___91_layouts_LAYOUTLIST_lit___91_layouts_LAYOUTLIST_parameters_iter_seps__Sym__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___93_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[7];
      
      tmp[6] = new LiteralStackNode<IConstructor>(384, 6, prod__lit___93__char_class___range__93_93_, new int[] {93}, null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(382, 5, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new SeparatedListStackNode<IConstructor>(372, 4, regular__iter_seps__Sym__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(374, 0, "Sym", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(376, 1, "layouts_LAYOUTLIST", null, null), new LiteralStackNode<IConstructor>(378, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode<IConstructor>(380, 3, "layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(370, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(368, 2, prod__lit___91__char_class___range__91_91_, new int[] {91}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(366, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(364, 0, "Nonterminal", null, new ICompletionFilter[] {new StringFollowRequirement(new int[] {91})});
      builder.addAlternative(ObjectRascalRascal.prod__Parametrized_Sym__conditional__nonterminal_Nonterminal__follow__lit___91_layouts_LAYOUTLIST_lit___91_layouts_LAYOUTLIST_parameters_iter_seps__Sym__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___93_, tmp);
	}
    protected static final void _init_prod__IterSep_Sym__lit___123_layouts_LAYOUTLIST_symbol_Sym_layouts_LAYOUTLIST_sep_Sym_layouts_LAYOUTLIST_lit___125_layouts_LAYOUTLIST_lit___43_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[9];
      
      tmp[8] = new LiteralStackNode<IConstructor>(402, 8, prod__lit___43__char_class___range__43_43_, new int[] {43}, null, null);
      tmp[7] = new NonTerminalStackNode<IConstructor>(400, 7, "layouts_LAYOUTLIST", null, null);
      tmp[6] = new LiteralStackNode<IConstructor>(398, 6, prod__lit___125__char_class___range__125_125_, new int[] {125}, null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(396, 5, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new NonTerminalStackNode<IConstructor>(394, 4, "Sym", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(392, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(390, 2, "Sym", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(388, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(386, 0, prod__lit___123__char_class___range__123_123_, new int[] {123}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__IterSep_Sym__lit___123_layouts_LAYOUTLIST_symbol_Sym_layouts_LAYOUTLIST_sep_Sym_layouts_LAYOUTLIST_lit___125_layouts_LAYOUTLIST_lit___43_, tmp);
	}
    protected static final void _init_prod__StartOfLine_Sym__lit___94_layouts_LAYOUTLIST_symbol_Sym_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode<IConstructor>(408, 2, "Sym", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(406, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(404, 0, prod__lit___94__char_class___range__94_94_, new int[] {94}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__StartOfLine_Sym__lit___94_layouts_LAYOUTLIST_symbol_Sym_, tmp);
	}
    protected static final void _init_prod__Iter_Sym__symbol_Sym_layouts_LAYOUTLIST_lit___43_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new LiteralStackNode<IConstructor>(414, 2, prod__lit___43__char_class___range__43_43_, new int[] {43}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(412, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(410, 0, "Sym", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Iter_Sym__symbol_Sym_layouts_LAYOUTLIST_lit___43_, tmp);
	}
    protected static final void _init_prod__Except_Sym__symbol_Sym_layouts_LAYOUTLIST_lit___33_layouts_LAYOUTLIST_label_NonterminalLabel_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(424, 4, "NonterminalLabel", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(422, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(420, 2, prod__lit___33__char_class___range__33_33_, new int[] {33}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(418, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(416, 0, "Sym", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Except_Sym__symbol_Sym_layouts_LAYOUTLIST_lit___33_layouts_LAYOUTLIST_label_NonterminalLabel_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__Empty_Sym__lit___40_layouts_LAYOUTLIST_lit___41_(builder);
      
        _init_prod__CharacterClass_Sym__charClass_Class_(builder);
      
        _init_prod__Sequence_Sym__lit___40_layouts_LAYOUTLIST_first_Sym_layouts_LAYOUTLIST_sequence_iter_seps__Sym__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_(builder);
      
        _init_prod__NotPrecede_Sym__match_Sym_layouts_LAYOUTLIST_lit___33_60_60_layouts_LAYOUTLIST_symbol_Sym__assoc__right(builder);
      
        _init_prod__IterStar_Sym__symbol_Sym_layouts_LAYOUTLIST_lit___42_(builder);
      
        _init_prod__Labeled_Sym__symbol_Sym_layouts_LAYOUTLIST_label_NonterminalLabel_(builder);
      
        _init_prod__Parameter_Sym__lit___38_layouts_LAYOUTLIST_nonterminal_Nonterminal_(builder);
      
        _init_prod__IterStarSep_Sym__lit___123_layouts_LAYOUTLIST_symbol_Sym_layouts_LAYOUTLIST_sep_Sym_layouts_LAYOUTLIST_lit___125_layouts_LAYOUTLIST_lit___42_(builder);
      
        _init_prod__Alternative_Sym__lit___40_layouts_LAYOUTLIST_first_Sym_layouts_LAYOUTLIST_lit___124_layouts_LAYOUTLIST_alternatives_iter_seps__Sym__layouts_LAYOUTLIST_lit___124_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_(builder);
      
        _init_prod__Follow_Sym__symbol_Sym_layouts_LAYOUTLIST_lit___62_62_layouts_LAYOUTLIST_match_Sym__assoc__left(builder);
      
        _init_prod__EndOfLine_Sym__symbol_Sym_layouts_LAYOUTLIST_lit___36_(builder);
      
        _init_prod__Precede_Sym__match_Sym_layouts_LAYOUTLIST_lit___60_60_layouts_LAYOUTLIST_symbol_Sym__assoc__right(builder);
      
        _init_prod__Column_Sym__symbol_Sym_layouts_LAYOUTLIST_lit___64_layouts_LAYOUTLIST_column_IntegerLiteral_(builder);
      
        _init_prod__Nonterminal_Sym__conditional__nonterminal_Nonterminal__not_follow__lit___91_(builder);
      
        _init_prod__Optional_Sym__symbol_Sym_layouts_LAYOUTLIST_lit___63_(builder);
      
        _init_prod__Start_Sym__lit_start_layouts_LAYOUTLIST_lit___91_layouts_LAYOUTLIST_nonterminal_Nonterminal_layouts_LAYOUTLIST_lit___93_(builder);
      
        _init_prod__CaseInsensitiveLiteral_Sym__cistring_CaseInsensitiveStringConstant_(builder);
      
        _init_prod__Unequal_Sym__symbol_Sym_layouts_LAYOUTLIST_lit___92_layouts_LAYOUTLIST_match_Sym__assoc__left(builder);
      
        _init_prod__NotFollow_Sym__symbol_Sym_layouts_LAYOUTLIST_lit___33_62_62_layouts_LAYOUTLIST_match_Sym__assoc__left(builder);
      
        _init_prod__Literal_Sym__string_StringConstant_(builder);
      
        _init_prod__Parametrized_Sym__conditional__nonterminal_Nonterminal__follow__lit___91_layouts_LAYOUTLIST_lit___91_layouts_LAYOUTLIST_parameters_iter_seps__Sym__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___93_(builder);
      
        _init_prod__IterSep_Sym__lit___123_layouts_LAYOUTLIST_symbol_Sym_layouts_LAYOUTLIST_sep_Sym_layouts_LAYOUTLIST_lit___125_layouts_LAYOUTLIST_lit___43_(builder);
      
        _init_prod__StartOfLine_Sym__lit___94_layouts_LAYOUTLIST_symbol_Sym_(builder);
      
        _init_prod__Iter_Sym__symbol_Sym_layouts_LAYOUTLIST_lit___43_(builder);
      
        _init_prod__Except_Sym__symbol_Sym_layouts_LAYOUTLIST_lit___33_layouts_LAYOUTLIST_label_NonterminalLabel_(builder);
      
    }
  }
	
  protected static class NonterminalLabel {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__NonterminalLabel__char_class___range__97_122_conditional__iter_star__char_class___range__48_57_range__65_90_range__95_95_range__97_122__not_follow__char_class___range__48_57_range__65_90_range__95_95_range__97_122_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[2];
      
      tmp[1] = new ListStackNode<IConstructor>(486, 1, regular__iter_star__char_class___range__48_57_range__65_90_range__95_95_range__97_122, new CharStackNode<IConstructor>(488, 0, new int[][]{{48,57},{65,90},{95,95},{97,122}}, null, null), false, null, new ICompletionFilter[] {new CharFollowRestriction(new int[][]{{48,57},{65,90},{95,95},{97,122}})});
      tmp[0] = new CharStackNode<IConstructor>(484, 0, new int[][]{{97,122}}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__NonterminalLabel__char_class___range__97_122_conditional__iter_star__char_class___range__48_57_range__65_90_range__95_95_range__97_122__not_follow__char_class___range__48_57_range__65_90_range__95_95_range__97_122_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__NonterminalLabel__char_class___range__97_122_conditional__iter_star__char_class___range__48_57_range__65_90_range__95_95_range__97_122__not_follow__char_class___range__48_57_range__65_90_range__95_95_range__97_122_(builder);
      
    }
  }
	
  protected static class Header {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__Default_Header__tags_Tags_layouts_LAYOUTLIST_lit_module_layouts_LAYOUTLIST_name_QualifiedName_layouts_LAYOUTLIST_imports_iter_star_seps__Import__layouts_LAYOUTLIST_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[7];
      
      tmp[6] = new SeparatedListStackNode<IConstructor>(528, 6, regular__iter_star_seps__Import__layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(530, 0, "Import", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(532, 1, "layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(526, 5, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new NonTerminalStackNode<IConstructor>(524, 4, "QualifiedName", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(522, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(520, 2, prod__lit_module__char_class___range__109_109_char_class___range__111_111_char_class___range__100_100_char_class___range__117_117_char_class___range__108_108_char_class___range__101_101_, new int[] {109,111,100,117,108,101}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(518, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(516, 0, "Tags", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Default_Header__tags_Tags_layouts_LAYOUTLIST_lit_module_layouts_LAYOUTLIST_name_QualifiedName_layouts_LAYOUTLIST_imports_iter_star_seps__Import__layouts_LAYOUTLIST_, tmp);
	}
    protected static final void _init_prod__Parameters_Header__tags_Tags_layouts_LAYOUTLIST_lit_module_layouts_LAYOUTLIST_name_QualifiedName_layouts_LAYOUTLIST_params_ModuleParameters_layouts_LAYOUTLIST_imports_iter_star_seps__Import__layouts_LAYOUTLIST_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[9];
      
      tmp[8] = new SeparatedListStackNode<IConstructor>(550, 8, regular__iter_star_seps__Import__layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(552, 0, "Import", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(554, 1, "layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[7] = new NonTerminalStackNode<IConstructor>(548, 7, "layouts_LAYOUTLIST", null, null);
      tmp[6] = new NonTerminalStackNode<IConstructor>(546, 6, "ModuleParameters", null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(544, 5, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new NonTerminalStackNode<IConstructor>(542, 4, "QualifiedName", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(540, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(538, 2, prod__lit_module__char_class___range__109_109_char_class___range__111_111_char_class___range__100_100_char_class___range__117_117_char_class___range__108_108_char_class___range__101_101_, new int[] {109,111,100,117,108,101}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(536, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(534, 0, "Tags", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Parameters_Header__tags_Tags_layouts_LAYOUTLIST_lit_module_layouts_LAYOUTLIST_name_QualifiedName_layouts_LAYOUTLIST_params_ModuleParameters_layouts_LAYOUTLIST_imports_iter_star_seps__Import__layouts_LAYOUTLIST_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__Default_Header__tags_Tags_layouts_LAYOUTLIST_lit_module_layouts_LAYOUTLIST_name_QualifiedName_layouts_LAYOUTLIST_imports_iter_star_seps__Import__layouts_LAYOUTLIST_(builder);
      
        _init_prod__Parameters_Header__tags_Tags_layouts_LAYOUTLIST_lit_module_layouts_LAYOUTLIST_name_QualifiedName_layouts_LAYOUTLIST_params_ModuleParameters_layouts_LAYOUTLIST_imports_iter_star_seps__Import__layouts_LAYOUTLIST_(builder);
      
    }
  }
	
  protected static class Commands {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__List_Commands__commands_iter_seps__EvalCommand__layouts_LAYOUTLIST_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new SeparatedListStackNode<IConstructor>(600, 0, regular__iter_seps__EvalCommand__layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(602, 0, "EvalCommand", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(604, 1, "layouts_LAYOUTLIST", null, null)}, true, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__List_Commands__commands_iter_seps__EvalCommand__layouts_LAYOUTLIST_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__List_Commands__commands_iter_seps__EvalCommand__layouts_LAYOUTLIST_(builder);
      
    }
  }
	
  protected static class ImportedModule {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__Renamings_ImportedModule__name_QualifiedName_layouts_LAYOUTLIST_renamings_Renamings_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode<IConstructor>(634, 2, "Renamings", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(632, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(630, 0, "QualifiedName", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Renamings_ImportedModule__name_QualifiedName_layouts_LAYOUTLIST_renamings_Renamings_, tmp);
	}
    protected static final void _init_prod__ActualsRenaming_ImportedModule__name_QualifiedName_layouts_LAYOUTLIST_actuals_ModuleActuals_layouts_LAYOUTLIST_renamings_Renamings_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(644, 4, "Renamings", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(642, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(640, 2, "ModuleActuals", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(638, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(636, 0, "QualifiedName", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__ActualsRenaming_ImportedModule__name_QualifiedName_layouts_LAYOUTLIST_actuals_ModuleActuals_layouts_LAYOUTLIST_renamings_Renamings_, tmp);
	}
    protected static final void _init_prod__Default_ImportedModule__name_QualifiedName_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(646, 0, "QualifiedName", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Default_ImportedModule__name_QualifiedName_, tmp);
	}
    protected static final void _init_prod__Actuals_ImportedModule__name_QualifiedName_layouts_LAYOUTLIST_actuals_ModuleActuals_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode<IConstructor>(652, 2, "ModuleActuals", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(650, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(648, 0, "QualifiedName", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Actuals_ImportedModule__name_QualifiedName_layouts_LAYOUTLIST_actuals_ModuleActuals_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__Renamings_ImportedModule__name_QualifiedName_layouts_LAYOUTLIST_renamings_Renamings_(builder);
      
        _init_prod__ActualsRenaming_ImportedModule__name_QualifiedName_layouts_LAYOUTLIST_actuals_ModuleActuals_layouts_LAYOUTLIST_renamings_Renamings_(builder);
      
        _init_prod__Default_ImportedModule__name_QualifiedName_(builder);
      
        _init_prod__Actuals_ImportedModule__name_QualifiedName_layouts_LAYOUTLIST_actuals_ModuleActuals_(builder);
      
    }
  }
	
  protected static class Expression {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__FieldUpdate_Expression__expression_Expression_layouts_LAYOUTLIST_lit___91_layouts_LAYOUTLIST_key_Name_layouts_LAYOUTLIST_lit___61_layouts_LAYOUTLIST_replacement_Expression_layouts_LAYOUTLIST_lit___93_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[11];
      
      tmp[10] = new LiteralStackNode<IConstructor>(710, 10, prod__lit___93__char_class___range__93_93_, new int[] {93}, null, null);
      tmp[9] = new NonTerminalStackNode<IConstructor>(708, 9, "layouts_LAYOUTLIST", null, null);
      tmp[8] = new NonTerminalStackNode<IConstructor>(706, 8, "Expression", null, null);
      tmp[7] = new NonTerminalStackNode<IConstructor>(704, 7, "layouts_LAYOUTLIST", null, null);
      tmp[6] = new LiteralStackNode<IConstructor>(702, 6, prod__lit___61__char_class___range__61_61_, new int[] {61}, null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(700, 5, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new NonTerminalStackNode<IConstructor>(698, 4, "Name", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(696, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(694, 2, prod__lit___91__char_class___range__91_91_, new int[] {91}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(692, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(690, 0, "Expression", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__FieldUpdate_Expression__expression_Expression_layouts_LAYOUTLIST_lit___91_layouts_LAYOUTLIST_key_Name_layouts_LAYOUTLIST_lit___61_layouts_LAYOUTLIST_replacement_Expression_layouts_LAYOUTLIST_lit___93_, tmp);
	}
    protected static final void _init_prod__Range_Expression__lit___91_layouts_LAYOUTLIST_first_Expression_layouts_LAYOUTLIST_lit___46_46_layouts_LAYOUTLIST_last_Expression_layouts_LAYOUTLIST_lit___93_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[9];
      
      tmp[8] = new LiteralStackNode<IConstructor>(750, 8, prod__lit___93__char_class___range__93_93_, new int[] {93}, null, null);
      tmp[7] = new NonTerminalStackNode<IConstructor>(748, 7, "layouts_LAYOUTLIST", null, null);
      tmp[6] = new NonTerminalStackNode<IConstructor>(746, 6, "Expression", null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(744, 5, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode<IConstructor>(742, 4, prod__lit___46_46__char_class___range__46_46_char_class___range__46_46_, new int[] {46,46}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(740, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(738, 2, "Expression", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(736, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(734, 0, prod__lit___91__char_class___range__91_91_, new int[] {91}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Range_Expression__lit___91_layouts_LAYOUTLIST_first_Expression_layouts_LAYOUTLIST_lit___46_46_layouts_LAYOUTLIST_last_Expression_layouts_LAYOUTLIST_lit___93_, tmp);
	}
    protected static final void _init_prod__Modulo_Expression__lhs_Expression_layouts_LAYOUTLIST_lit_mod_layouts_LAYOUTLIST_rhs_Expression__assoc__left(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(1308, 4, "Expression", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(1306, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(1304, 2, prod__lit_mod__char_class___range__109_109_char_class___range__111_111_char_class___range__100_100_, new int[] {109,111,100}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(1302, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(1300, 0, "Expression", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Modulo_Expression__lhs_Expression_layouts_LAYOUTLIST_lit_mod_layouts_LAYOUTLIST_rhs_Expression__assoc__left, tmp);
	}
    protected static final void _init_prod__Or_Expression__lhs_Expression_layouts_LAYOUTLIST_lit___124_124_layouts_LAYOUTLIST_rhs_Expression__assoc__left(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(1468, 4, "Expression", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(1466, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(1464, 2, prod__lit___124_124__char_class___range__124_124_char_class___range__124_124_, new int[] {124,124}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(1462, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(1460, 0, "Expression", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Or_Expression__lhs_Expression_layouts_LAYOUTLIST_lit___124_124_layouts_LAYOUTLIST_rhs_Expression__assoc__left, tmp);
	}
    protected static final void _init_prod__Bracket_Expression__lit___40_layouts_LAYOUTLIST_expression_Expression_layouts_LAYOUTLIST_lit___41__bracket(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new LiteralStackNode<IConstructor>(818, 4, prod__lit___41__char_class___range__41_41_, new int[] {41}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(816, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(814, 2, "Expression", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(812, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(810, 0, prod__lit___40__char_class___range__40_40_, new int[] {40}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Bracket_Expression__lit___40_layouts_LAYOUTLIST_expression_Expression_layouts_LAYOUTLIST_lit___41__bracket, tmp);
	}
    protected static final void _init_prod__Is_Expression__expression_Expression_layouts_LAYOUTLIST_lit_is_layouts_LAYOUTLIST_name_Name_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(850, 4, "Name", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(848, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(846, 2, prod__lit_is__char_class___range__105_105_char_class___range__115_115_, new int[] {105,115}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(844, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(842, 0, "Expression", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Is_Expression__expression_Expression_layouts_LAYOUTLIST_lit_is_layouts_LAYOUTLIST_name_Name_, tmp);
	}
    protected static final void _init_prod__TransitiveClosure_Expression__argument_Expression_layouts_LAYOUTLIST_conditional__lit___43__not_follow__lit___61_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new LiteralStackNode<IConstructor>(856, 2, prod__lit___43__char_class___range__43_43_, new int[] {43}, null, new ICompletionFilter[] {new StringFollowRestriction(new int[] {61})});
      tmp[1] = new NonTerminalStackNode<IConstructor>(854, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(852, 0, "Expression", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__TransitiveClosure_Expression__argument_Expression_layouts_LAYOUTLIST_conditional__lit___43__not_follow__lit___61_, tmp);
	}
    protected static final void _init_prod__Map_Expression__lit___40_layouts_LAYOUTLIST_mappings_iter_star_seps__Mapping__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[0] = new LiteralStackNode<IConstructor>(858, 0, prod__lit___40__char_class___range__40_40_, new int[] {40}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(860, 1, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new SeparatedListStackNode<IConstructor>(862, 2, regular__iter_star_seps__Mapping__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(864, 0, "Mapping__Expression", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(866, 1, "layouts_LAYOUTLIST", null, null), new LiteralStackNode<IConstructor>(868, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode<IConstructor>(870, 3, "layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(872, 3, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode<IConstructor>(874, 4, prod__lit___41__char_class___range__41_41_, new int[] {41}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Map_Expression__lit___40_layouts_LAYOUTLIST_mappings_iter_star_seps__Mapping__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_, tmp);
	}
    protected static final void _init_prod__Self_Expression__lit_self_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(894, 0, prod__lit_self__char_class___range__115_115_char_class___range__101_101_char_class___range__108_108_char_class___range__102_102_, new int[] {115,101,108,102}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Self_Expression__lit_self_, tmp);
	}
    protected static final void _init_prod__ReifyType_Expression__lit___35_layouts_LAYOUTLIST_conditional__type_Type__not_follow__lit___91_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode<IConstructor>(964, 2, "Type", null, new ICompletionFilter[] {new StringFollowRestriction(new int[] {91})});
      tmp[1] = new NonTerminalStackNode<IConstructor>(962, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(960, 0, prod__lit___35__char_class___range__35_35_, new int[] {35}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__ReifyType_Expression__lit___35_layouts_LAYOUTLIST_conditional__type_Type__not_follow__lit___91_, tmp);
	}
    protected static final void _init_prod__All_Expression__lit_all_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_generators_iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[7];
      
      tmp[6] = new LiteralStackNode<IConstructor>(986, 6, prod__lit___41__char_class___range__41_41_, new int[] {41}, null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(984, 5, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new SeparatedListStackNode<IConstructor>(974, 4, regular__iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(976, 0, "Expression", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(978, 1, "layouts_LAYOUTLIST", null, null), new LiteralStackNode<IConstructor>(980, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode<IConstructor>(982, 3, "layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(972, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(970, 2, prod__lit___40__char_class___range__40_40_, new int[] {40}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(968, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(966, 0, prod__lit_all__char_class___range__97_97_char_class___range__108_108_char_class___range__108_108_, new int[] {97,108,108}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__All_Expression__lit_all_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_generators_iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_, tmp);
	}
    protected static final void _init_prod__Equals_Expression__lhs_Expression_layouts_LAYOUTLIST_lit___61_61_layouts_LAYOUTLIST_rhs_Expression__assoc__non_assoc(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(1388, 4, "Expression", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(1386, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(1384, 2, prod__lit___61_61__char_class___range__61_61_char_class___range__61_61_, new int[] {61,61}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(1382, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(1380, 0, "Expression", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Equals_Expression__lhs_Expression_layouts_LAYOUTLIST_lit___61_61_layouts_LAYOUTLIST_rhs_Expression__assoc__non_assoc, tmp);
	}
    protected static final void _init_prod__Product_Expression__lhs_Expression_layouts_LAYOUTLIST_lit___42_layouts_LAYOUTLIST_conditional__empty__not_follow__lit___42_layouts_LAYOUTLIST_rhs_Expression__assoc__left(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[7];
      
      tmp[6] = new NonTerminalStackNode<IConstructor>(1218, 6, "Expression", null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(1216, 5, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new EmptyStackNode<IConstructor>(1214, 4, regular__empty, null, new ICompletionFilter[] {new StringFollowRestriction(new int[] {42})});
      tmp[3] = new NonTerminalStackNode<IConstructor>(1212, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(1210, 2, prod__lit___42__char_class___range__42_42_, new int[] {42}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(1208, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(1206, 0, "Expression", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Product_Expression__lhs_Expression_layouts_LAYOUTLIST_lit___42_layouts_LAYOUTLIST_conditional__empty__not_follow__lit___42_layouts_LAYOUTLIST_rhs_Expression__assoc__left, tmp);
	}
    protected static final void _init_prod__Splice_Expression__lit___42_layouts_LAYOUTLIST_argument_Expression__assoc__non_assoc(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode<IConstructor>(1194, 2, "Expression", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(1192, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(1190, 0, prod__lit___42__char_class___range__42_42_, new int[] {42}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Splice_Expression__lit___42_layouts_LAYOUTLIST_argument_Expression__assoc__non_assoc, tmp);
	}
    protected static final void _init_prod__Closure_Expression__type_Type_layouts_LAYOUTLIST_parameters_Parameters_layouts_LAYOUTLIST_lit___123_layouts_LAYOUTLIST_statements_iter_seps__Statement__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[9];
      
      tmp[8] = new LiteralStackNode<IConstructor>(1100, 8, prod__lit___125__char_class___range__125_125_, new int[] {125}, null, null);
      tmp[7] = new NonTerminalStackNode<IConstructor>(1098, 7, "layouts_LAYOUTLIST", null, null);
      tmp[6] = new SeparatedListStackNode<IConstructor>(1092, 6, regular__iter_seps__Statement__layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(1094, 0, "Statement", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(1096, 1, "layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(1090, 5, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode<IConstructor>(1088, 4, prod__lit___123__char_class___range__123_123_, new int[] {123}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(1086, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(1084, 2, "Parameters", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(1082, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(1080, 0, "Type", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Closure_Expression__type_Type_layouts_LAYOUTLIST_parameters_Parameters_layouts_LAYOUTLIST_lit___123_layouts_LAYOUTLIST_statements_iter_seps__Statement__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_, tmp);
	}
    protected static final void _init_prod__Visit_Expression__label_Label_layouts_LAYOUTLIST_visit_Visit_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode<IConstructor>(1106, 2, "Visit", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(1104, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(1102, 0, "Label", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Visit_Expression__label_Label_layouts_LAYOUTLIST_visit_Visit_, tmp);
	}
    protected static final void _init_prod__Join_Expression__lhs_Expression_layouts_LAYOUTLIST_lit_join_layouts_LAYOUTLIST_rhs_Expression__assoc__left(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(1238, 4, "Expression", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(1236, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(1234, 2, prod__lit_join__char_class___range__106_106_char_class___range__111_111_char_class___range__105_105_char_class___range__110_110_, new int[] {106,111,105,110}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(1232, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(1230, 0, "Expression", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Join_Expression__lhs_Expression_layouts_LAYOUTLIST_lit_join_layouts_LAYOUTLIST_rhs_Expression__assoc__left, tmp);
	}
    protected static final void _init_prod__It_Expression__conditional__lit_it__not_precede__char_class___range__65_90_range__95_95_range__97_122_not_follow__char_class___range__65_90_range__95_95_range__97_122_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(1154, 0, prod__lit_it__char_class___range__105_105_char_class___range__116_116_, new int[] {105,116}, new IEnterFilter[] {new CharPrecedeRestriction(new int[][]{{65,90},{95,95},{97,122}})}, new ICompletionFilter[] {new CharFollowRestriction(new int[][]{{65,90},{95,95},{97,122}})});
      builder.addAlternative(ObjectRascalRascal.prod__It_Expression__conditional__lit_it__not_precede__char_class___range__65_90_range__95_95_range__97_122_not_follow__char_class___range__65_90_range__95_95_range__97_122_, tmp);
	}
    protected static final void _init_prod__IfThenElse_Expression__condition_Expression_layouts_LAYOUTLIST_lit___63_layouts_LAYOUTLIST_thenExp_Expression_layouts_LAYOUTLIST_lit___58_layouts_LAYOUTLIST_elseExp_Expression__assoc__right(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[9];
      
      tmp[8] = new NonTerminalStackNode<IConstructor>(1486, 8, "Expression", null, null);
      tmp[7] = new NonTerminalStackNode<IConstructor>(1484, 7, "layouts_LAYOUTLIST", null, null);
      tmp[6] = new LiteralStackNode<IConstructor>(1482, 6, prod__lit___58__char_class___range__58_58_, new int[] {58}, null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(1480, 5, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new NonTerminalStackNode<IConstructor>(1478, 4, "Expression", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(1476, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(1474, 2, prod__lit___63__char_class___range__63_63_, new int[] {63}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(1472, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(1470, 0, "Expression", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__IfThenElse_Expression__condition_Expression_layouts_LAYOUTLIST_lit___63_layouts_LAYOUTLIST_thenExp_Expression_layouts_LAYOUTLIST_lit___58_layouts_LAYOUTLIST_elseExp_Expression__assoc__right, tmp);
	}
    protected static final void _init_prod__List_Expression__lit___91_layouts_LAYOUTLIST_elements_iter_star_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___93_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new LiteralStackNode<IConstructor>(768, 4, prod__lit___93__char_class___range__93_93_, new int[] {93}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(766, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new SeparatedListStackNode<IConstructor>(756, 2, regular__iter_star_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(758, 0, "Expression", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(760, 1, "layouts_LAYOUTLIST", null, null), new LiteralStackNode<IConstructor>(762, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode<IConstructor>(764, 3, "layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(754, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(752, 0, prod__lit___91__char_class___range__91_91_, new int[] {91}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__List_Expression__lit___91_layouts_LAYOUTLIST_elements_iter_star_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___93_, tmp);
	}
    protected static final void _init_prod__Subscript_Expression__expression_Expression_layouts_LAYOUTLIST_lit___91_layouts_LAYOUTLIST_subscripts_iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___93_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[7];
      
      tmp[6] = new LiteralStackNode<IConstructor>(790, 6, prod__lit___93__char_class___range__93_93_, new int[] {93}, null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(788, 5, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new SeparatedListStackNode<IConstructor>(778, 4, regular__iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(780, 0, "Expression", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(782, 1, "layouts_LAYOUTLIST", null, null), new LiteralStackNode<IConstructor>(784, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode<IConstructor>(786, 3, "layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(776, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(774, 2, prod__lit___91__char_class___range__91_91_, new int[] {91}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(772, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(770, 0, "Expression", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Subscript_Expression__expression_Expression_layouts_LAYOUTLIST_lit___91_layouts_LAYOUTLIST_subscripts_iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___93_, tmp);
	}
    protected static final void _init_prod__Subtraction_Expression__lhs_Expression_layouts_LAYOUTLIST_lit___layouts_LAYOUTLIST_rhs_Expression__assoc__left(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(1278, 4, "Expression", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(1276, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(1274, 2, prod__lit____char_class___range__45_45_, new int[] {45}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(1272, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(1270, 0, "Expression", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Subtraction_Expression__lhs_Expression_layouts_LAYOUTLIST_lit___layouts_LAYOUTLIST_rhs_Expression__assoc__left, tmp);
	}
    protected static final void _init_prod__GreaterThan_Expression__lhs_Expression_layouts_LAYOUTLIST_lit___62_layouts_LAYOUTLIST_rhs_Expression__assoc__non_assoc(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(1358, 4, "Expression", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(1356, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(1354, 2, prod__lit___62__char_class___range__62_62_, new int[] {62}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(1352, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(1350, 0, "Expression", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__GreaterThan_Expression__lhs_Expression_layouts_LAYOUTLIST_lit___62_layouts_LAYOUTLIST_rhs_Expression__assoc__non_assoc, tmp);
	}
    protected static final void _init_prod__Enumerator_Expression__pattern_Pattern_layouts_LAYOUTLIST_lit___60_45_layouts_LAYOUTLIST_expression_Expression__assoc__non_assoc(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(1418, 4, "Expression", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(1416, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(1414, 2, prod__lit___60_45__char_class___range__60_60_char_class___range__45_45_, new int[] {60,45}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(1412, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(1410, 0, "Pattern", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Enumerator_Expression__pattern_Pattern_layouts_LAYOUTLIST_lit___60_45_layouts_LAYOUTLIST_expression_Expression__assoc__non_assoc, tmp);
	}
    protected static final void _init_prod__NoMatch_Expression__pattern_Pattern_layouts_LAYOUTLIST_lit___33_58_61_layouts_LAYOUTLIST_expression_Expression__assoc__non_assoc(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(1428, 4, "Expression", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(1426, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(1424, 2, prod__lit___33_58_61__char_class___range__33_33_char_class___range__58_58_char_class___range__61_61_, new int[] {33,58,61}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(1422, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(1420, 0, "Pattern", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__NoMatch_Expression__pattern_Pattern_layouts_LAYOUTLIST_lit___33_58_61_layouts_LAYOUTLIST_expression_Expression__assoc__non_assoc, tmp);
	}
    protected static final void _init_prod__SetAnnotation_Expression__expression_Expression_layouts_LAYOUTLIST_lit___91_layouts_LAYOUTLIST_lit___64_layouts_LAYOUTLIST_name_Name_layouts_LAYOUTLIST_lit___61_layouts_LAYOUTLIST_value_Expression_layouts_LAYOUTLIST_lit___93_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[13];
      
      tmp[12] = new LiteralStackNode<IConstructor>(930, 12, prod__lit___93__char_class___range__93_93_, new int[] {93}, null, null);
      tmp[11] = new NonTerminalStackNode<IConstructor>(928, 11, "layouts_LAYOUTLIST", null, null);
      tmp[10] = new NonTerminalStackNode<IConstructor>(926, 10, "Expression", null, null);
      tmp[9] = new NonTerminalStackNode<IConstructor>(924, 9, "layouts_LAYOUTLIST", null, null);
      tmp[8] = new LiteralStackNode<IConstructor>(922, 8, prod__lit___61__char_class___range__61_61_, new int[] {61}, null, null);
      tmp[7] = new NonTerminalStackNode<IConstructor>(920, 7, "layouts_LAYOUTLIST", null, null);
      tmp[6] = new NonTerminalStackNode<IConstructor>(918, 6, "Name", null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(916, 5, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode<IConstructor>(914, 4, prod__lit___64__char_class___range__64_64_, new int[] {64}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(912, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(910, 2, prod__lit___91__char_class___range__91_91_, new int[] {91}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(908, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(906, 0, "Expression", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__SetAnnotation_Expression__expression_Expression_layouts_LAYOUTLIST_lit___91_layouts_LAYOUTLIST_lit___64_layouts_LAYOUTLIST_name_Name_layouts_LAYOUTLIST_lit___61_layouts_LAYOUTLIST_value_Expression_layouts_LAYOUTLIST_lit___93_, tmp);
	}
    protected static final void _init_prod__ReifiedType_Expression__lit_type_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_symbol_Expression_layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_definitions_Expression_layouts_LAYOUTLIST_lit___41_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[11];
      
      tmp[10] = new LiteralStackNode<IConstructor>(1008, 10, prod__lit___41__char_class___range__41_41_, new int[] {41}, null, null);
      tmp[9] = new NonTerminalStackNode<IConstructor>(1006, 9, "layouts_LAYOUTLIST", null, null);
      tmp[8] = new NonTerminalStackNode<IConstructor>(1004, 8, "Expression", null, null);
      tmp[7] = new NonTerminalStackNode<IConstructor>(1002, 7, "layouts_LAYOUTLIST", null, null);
      tmp[6] = new LiteralStackNode<IConstructor>(1000, 6, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(998, 5, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new NonTerminalStackNode<IConstructor>(996, 4, "Expression", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(994, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(992, 2, prod__lit___40__char_class___range__40_40_, new int[] {40}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(990, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(988, 0, prod__lit_type__char_class___range__116_116_char_class___range__121_121_char_class___range__112_112_char_class___range__101_101_, new int[] {116,121,112,101}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__ReifiedType_Expression__lit_type_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_symbol_Expression_layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_definitions_Expression_layouts_LAYOUTLIST_lit___41_, tmp);
	}
    protected static final void _init_prod__Negative_Expression__lit___layouts_LAYOUTLIST_argument_Expression_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode<IConstructor>(1174, 2, "Expression", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(1172, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(1170, 0, prod__lit____char_class___range__45_45_, new int[] {45}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Negative_Expression__lit___layouts_LAYOUTLIST_argument_Expression_, tmp);
	}
    protected static final void _init_prod__NonEmptyBlock_Expression__lit___123_layouts_LAYOUTLIST_statements_iter_seps__Statement__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new LiteralStackNode<IConstructor>(1022, 4, prod__lit___125__char_class___range__125_125_, new int[] {125}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(1020, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new SeparatedListStackNode<IConstructor>(1014, 2, regular__iter_seps__Statement__layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(1016, 0, "Statement", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(1018, 1, "layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(1012, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(1010, 0, prod__lit___123__char_class___range__123_123_, new int[] {123}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__NonEmptyBlock_Expression__lit___123_layouts_LAYOUTLIST_statements_iter_seps__Statement__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_, tmp);
	}
    protected static final void _init_prod__AppendAfter_Expression__lhs_Expression_layouts_LAYOUTLIST_conditional__lit___60_60__not_follow__lit___61_layouts_LAYOUTLIST_rhs_Expression__assoc__left(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(1268, 4, "Expression", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(1266, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(1264, 2, prod__lit___60_60__char_class___range__60_60_char_class___range__60_60_, new int[] {60,60}, null, new ICompletionFilter[] {new StringFollowRestriction(new int[] {61})});
      tmp[1] = new NonTerminalStackNode<IConstructor>(1262, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(1260, 0, "Expression", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__AppendAfter_Expression__lhs_Expression_layouts_LAYOUTLIST_conditional__lit___60_60__not_follow__lit___61_layouts_LAYOUTLIST_rhs_Expression__assoc__left, tmp);
	}
    protected static final void _init_prod__LessThan_Expression__lhs_Expression_layouts_LAYOUTLIST_conditional__lit___60__not_follow__lit___layouts_LAYOUTLIST_rhs_Expression__assoc__non_assoc(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(1348, 4, "Expression", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(1346, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(1344, 2, prod__lit___60__char_class___range__60_60_, new int[] {60}, null, new ICompletionFilter[] {new StringFollowRestriction(new int[] {45})});
      tmp[1] = new NonTerminalStackNode<IConstructor>(1342, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(1340, 0, "Expression", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__LessThan_Expression__lhs_Expression_layouts_LAYOUTLIST_conditional__lit___60__not_follow__lit___layouts_LAYOUTLIST_rhs_Expression__assoc__non_assoc, tmp);
	}
    protected static final void _init_prod__IfDefinedOtherwise_Expression__lhs_Expression_layouts_LAYOUTLIST_lit___63_layouts_LAYOUTLIST_rhs_Expression__assoc__non_assoc(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(1398, 4, "Expression", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(1396, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(1394, 2, prod__lit___63__char_class___range__63_63_, new int[] {63}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(1392, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(1390, 0, "Expression", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__IfDefinedOtherwise_Expression__lhs_Expression_layouts_LAYOUTLIST_lit___63_layouts_LAYOUTLIST_rhs_Expression__assoc__non_assoc, tmp);
	}
    protected static final void _init_prod__InsertBefore_Expression__lhs_Expression_layouts_LAYOUTLIST_lit___62_62_layouts_LAYOUTLIST_rhs_Expression__assoc__left(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(1288, 4, "Expression", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(1286, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(1284, 2, prod__lit___62_62__char_class___range__62_62_char_class___range__62_62_, new int[] {62,62}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(1282, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(1280, 0, "Expression", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__InsertBefore_Expression__lhs_Expression_layouts_LAYOUTLIST_lit___62_62_layouts_LAYOUTLIST_rhs_Expression__assoc__left, tmp);
	}
    protected static final void _init_prod__Division_Expression__lhs_Expression_layouts_LAYOUTLIST_lit___47_layouts_LAYOUTLIST_rhs_Expression__assoc__left(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(1248, 4, "Expression", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(1246, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(1244, 2, prod__lit___47__char_class___range__47_47_, new int[] {47}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(1242, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(1240, 0, "Expression", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Division_Expression__lhs_Expression_layouts_LAYOUTLIST_lit___47_layouts_LAYOUTLIST_rhs_Expression__assoc__left, tmp);
	}
    protected static final void _init_prod__Comprehension_Expression__comprehension_Comprehension_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(1156, 0, "Comprehension", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Comprehension_Expression__comprehension_Comprehension_, tmp);
	}
    protected static final void _init_prod__LessThanOrEq_Expression__lhs_Expression_layouts_LAYOUTLIST_lit___60_61_layouts_LAYOUTLIST_rhs_Expression__assoc__non_assoc(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(1338, 4, "Expression", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(1336, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(1334, 2, prod__lit___60_61__char_class___range__60_60_char_class___range__61_61_, new int[] {60,61}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(1332, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(1330, 0, "Expression", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__LessThanOrEq_Expression__lhs_Expression_layouts_LAYOUTLIST_lit___60_61_layouts_LAYOUTLIST_rhs_Expression__assoc__non_assoc, tmp);
	}
    protected static final void _init_prod__Any_Expression__lit_any_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_generators_iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[7];
      
      tmp[0] = new LiteralStackNode<IConstructor>(820, 0, prod__lit_any__char_class___range__97_97_char_class___range__110_110_char_class___range__121_121_, new int[] {97,110,121}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(822, 1, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(824, 2, prod__lit___40__char_class___range__40_40_, new int[] {40}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(826, 3, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new SeparatedListStackNode<IConstructor>(828, 4, regular__iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(830, 0, "Expression", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(832, 1, "layouts_LAYOUTLIST", null, null), new LiteralStackNode<IConstructor>(834, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode<IConstructor>(836, 3, "layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(838, 5, "layouts_LAYOUTLIST", null, null);
      tmp[6] = new LiteralStackNode<IConstructor>(840, 6, prod__lit___41__char_class___range__41_41_, new int[] {41}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Any_Expression__lit_any_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_generators_iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_, tmp);
	}
    protected static final void _init_prod__Has_Expression__expression_Expression_layouts_LAYOUTLIST_lit_has_layouts_LAYOUTLIST_name_Name_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(904, 4, "Name", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(902, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(900, 2, prod__lit_has__char_class___range__104_104_char_class___range__97_97_char_class___range__115_115_, new int[] {104,97,115}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(898, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(896, 0, "Expression", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Has_Expression__expression_Expression_layouts_LAYOUTLIST_lit_has_layouts_LAYOUTLIST_name_Name_, tmp);
	}
    protected static final void _init_prod__Set_Expression__lit___123_layouts_LAYOUTLIST_elements_iter_star_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new LiteralStackNode<IConstructor>(948, 4, prod__lit___125__char_class___range__125_125_, new int[] {125}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(946, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new SeparatedListStackNode<IConstructor>(936, 2, regular__iter_star_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(938, 0, "Expression", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(940, 1, "layouts_LAYOUTLIST", null, null), new LiteralStackNode<IConstructor>(942, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode<IConstructor>(944, 3, "layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(934, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(932, 0, prod__lit___123__char_class___range__123_123_, new int[] {123}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Set_Expression__lit___123_layouts_LAYOUTLIST_elements_iter_star_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_, tmp);
	}
    protected static final void _init_prod__AsType_Expression__lit___91_layouts_LAYOUTLIST_type_Type_layouts_LAYOUTLIST_lit___93_layouts_LAYOUTLIST_argument_Expression_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[7];
      
      tmp[6] = new NonTerminalStackNode<IConstructor>(1188, 6, "Expression", null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(1186, 5, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode<IConstructor>(1184, 4, prod__lit___93__char_class___range__93_93_, new int[] {93}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(1182, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(1180, 2, "Type", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(1178, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(1176, 0, prod__lit___91__char_class___range__91_91_, new int[] {91}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__AsType_Expression__lit___91_layouts_LAYOUTLIST_type_Type_layouts_LAYOUTLIST_lit___93_layouts_LAYOUTLIST_argument_Expression_, tmp);
	}
    protected static final void _init_prod__Reducer_Expression__lit___40_layouts_LAYOUTLIST_init_Expression_layouts_LAYOUTLIST_lit___124_layouts_LAYOUTLIST_result_Expression_layouts_LAYOUTLIST_lit___124_layouts_LAYOUTLIST_generators_iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[13];
      
      tmp[12] = new LiteralStackNode<IConstructor>(1056, 12, prod__lit___41__char_class___range__41_41_, new int[] {41}, null, null);
      tmp[11] = new NonTerminalStackNode<IConstructor>(1054, 11, "layouts_LAYOUTLIST", null, null);
      tmp[10] = new SeparatedListStackNode<IConstructor>(1044, 10, regular__iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(1046, 0, "Expression", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(1048, 1, "layouts_LAYOUTLIST", null, null), new LiteralStackNode<IConstructor>(1050, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode<IConstructor>(1052, 3, "layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[9] = new NonTerminalStackNode<IConstructor>(1042, 9, "layouts_LAYOUTLIST", null, null);
      tmp[8] = new LiteralStackNode<IConstructor>(1040, 8, prod__lit___124__char_class___range__124_124_, new int[] {124}, null, null);
      tmp[7] = new NonTerminalStackNode<IConstructor>(1038, 7, "layouts_LAYOUTLIST", null, null);
      tmp[6] = new NonTerminalStackNode<IConstructor>(1036, 6, "Expression", null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(1034, 5, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode<IConstructor>(1032, 4, prod__lit___124__char_class___range__124_124_, new int[] {124}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(1030, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(1028, 2, "Expression", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(1026, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(1024, 0, prod__lit___40__char_class___range__40_40_, new int[] {40}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Reducer_Expression__lit___40_layouts_LAYOUTLIST_init_Expression_layouts_LAYOUTLIST_lit___124_layouts_LAYOUTLIST_result_Expression_layouts_LAYOUTLIST_lit___124_layouts_LAYOUTLIST_generators_iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_, tmp);
	}
    protected static final void _init_prod__Intersection_Expression__lhs_Expression_layouts_LAYOUTLIST_lit___38_layouts_LAYOUTLIST_rhs_Expression__assoc__left(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(1258, 4, "Expression", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(1256, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(1254, 2, prod__lit___38__char_class___range__38_38_, new int[] {38}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(1252, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(1250, 0, "Expression", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Intersection_Expression__lhs_Expression_layouts_LAYOUTLIST_lit___38_layouts_LAYOUTLIST_rhs_Expression__assoc__left, tmp);
	}
    protected static final void _init_prod__And_Expression__lhs_Expression_layouts_LAYOUTLIST_lit___38_38_layouts_LAYOUTLIST_rhs_Expression__assoc__left(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(1458, 4, "Expression", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(1456, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(1454, 2, prod__lit___38_38__char_class___range__38_38_char_class___range__38_38_, new int[] {38,38}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(1452, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(1450, 0, "Expression", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__And_Expression__lhs_Expression_layouts_LAYOUTLIST_lit___38_38_layouts_LAYOUTLIST_rhs_Expression__assoc__left, tmp);
	}
    protected static final void _init_prod__Tuple_Expression__lit___60_layouts_LAYOUTLIST_elements_iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___62_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new LiteralStackNode<IConstructor>(1126, 4, prod__lit___62__char_class___range__62_62_, new int[] {62}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(1124, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new SeparatedListStackNode<IConstructor>(1114, 2, regular__iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(1116, 0, "Expression", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(1118, 1, "layouts_LAYOUTLIST", null, null), new LiteralStackNode<IConstructor>(1120, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode<IConstructor>(1122, 3, "layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(1112, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(1110, 0, prod__lit___60__char_class___range__60_60_, new int[] {60}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Tuple_Expression__lit___60_layouts_LAYOUTLIST_elements_iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___62_, tmp);
	}
    protected static final void _init_prod__In_Expression__lhs_Expression_layouts_LAYOUTLIST_lit_in_layouts_LAYOUTLIST_rhs_Expression__assoc__non_assoc(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(1328, 4, "Expression", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(1326, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(1324, 2, prod__lit_in__char_class___range__105_105_char_class___range__110_110_, new int[] {105,110}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(1322, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(1320, 0, "Expression", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__In_Expression__lhs_Expression_layouts_LAYOUTLIST_lit_in_layouts_LAYOUTLIST_rhs_Expression__assoc__non_assoc, tmp);
	}
    protected static final void _init_prod__StepRange_Expression__lit___91_layouts_LAYOUTLIST_first_Expression_layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_second_Expression_layouts_LAYOUTLIST_lit___46_46_layouts_LAYOUTLIST_last_Expression_layouts_LAYOUTLIST_lit___93_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[13];
      
      tmp[12] = new LiteralStackNode<IConstructor>(1152, 12, prod__lit___93__char_class___range__93_93_, new int[] {93}, null, null);
      tmp[11] = new NonTerminalStackNode<IConstructor>(1150, 11, "layouts_LAYOUTLIST", null, null);
      tmp[10] = new NonTerminalStackNode<IConstructor>(1148, 10, "Expression", null, null);
      tmp[9] = new NonTerminalStackNode<IConstructor>(1146, 9, "layouts_LAYOUTLIST", null, null);
      tmp[8] = new LiteralStackNode<IConstructor>(1144, 8, prod__lit___46_46__char_class___range__46_46_char_class___range__46_46_, new int[] {46,46}, null, null);
      tmp[7] = new NonTerminalStackNode<IConstructor>(1142, 7, "layouts_LAYOUTLIST", null, null);
      tmp[6] = new NonTerminalStackNode<IConstructor>(1140, 6, "Expression", null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(1138, 5, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode<IConstructor>(1136, 4, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(1134, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(1132, 2, "Expression", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(1130, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(1128, 0, prod__lit___91__char_class___range__91_91_, new int[] {91}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__StepRange_Expression__lit___91_layouts_LAYOUTLIST_first_Expression_layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_second_Expression_layouts_LAYOUTLIST_lit___46_46_layouts_LAYOUTLIST_last_Expression_layouts_LAYOUTLIST_lit___93_, tmp);
	}
    protected static final void _init_prod__Match_Expression__pattern_Pattern_layouts_LAYOUTLIST_lit___58_61_layouts_LAYOUTLIST_expression_Expression__assoc__non_assoc(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(1408, 4, "Expression", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(1406, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(1404, 2, prod__lit___58_61__char_class___range__58_58_char_class___range__61_61_, new int[] {58,61}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(1402, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(1400, 0, "Pattern", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Match_Expression__pattern_Pattern_layouts_LAYOUTLIST_lit___58_61_layouts_LAYOUTLIST_expression_Expression__assoc__non_assoc, tmp);
	}
    protected static final void _init_prod__CallOrTree_Expression__expression_Expression_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_arguments_iter_star_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[7];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(712, 0, "Expression", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(714, 1, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(716, 2, prod__lit___40__char_class___range__40_40_, new int[] {40}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(718, 3, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new SeparatedListStackNode<IConstructor>(720, 4, regular__iter_star_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(722, 0, "Expression", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(724, 1, "layouts_LAYOUTLIST", null, null), new LiteralStackNode<IConstructor>(726, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode<IConstructor>(728, 3, "layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(730, 5, "layouts_LAYOUTLIST", null, null);
      tmp[6] = new LiteralStackNode<IConstructor>(732, 6, prod__lit___41__char_class___range__41_41_, new int[] {41}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__CallOrTree_Expression__expression_Expression_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_arguments_iter_star_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_, tmp);
	}
    protected static final void _init_prod__Negation_Expression__lit___33_layouts_LAYOUTLIST_argument_Expression_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode<IConstructor>(1168, 2, "Expression", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(1166, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(1164, 0, prod__lit___33__char_class___range__33_33_, new int[] {33}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Negation_Expression__lit___33_layouts_LAYOUTLIST_argument_Expression_, tmp);
	}
    protected static final void _init_prod__VoidClosure_Expression__parameters_Parameters_layouts_LAYOUTLIST_lit___123_layouts_LAYOUTLIST_statements_iter_star_seps__Statement__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[7];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(792, 0, "Parameters", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(794, 1, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(796, 2, prod__lit___123__char_class___range__123_123_, new int[] {123}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(798, 3, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new SeparatedListStackNode<IConstructor>(800, 4, regular__iter_star_seps__Statement__layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(802, 0, "Statement", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(804, 1, "layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(806, 5, "layouts_LAYOUTLIST", null, null);
      tmp[6] = new LiteralStackNode<IConstructor>(808, 6, prod__lit___125__char_class___range__125_125_, new int[] {125}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__VoidClosure_Expression__parameters_Parameters_layouts_LAYOUTLIST_lit___123_layouts_LAYOUTLIST_statements_iter_star_seps__Statement__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_, tmp);
	}
    protected static final void _init_prod__NotIn_Expression__lhs_Expression_layouts_LAYOUTLIST_lit_notin_layouts_LAYOUTLIST_rhs_Expression__assoc__non_assoc(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(1318, 4, "Expression", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(1316, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(1314, 2, prod__lit_notin__char_class___range__110_110_char_class___range__111_111_char_class___range__116_116_char_class___range__105_105_char_class___range__110_110_, new int[] {110,111,116,105,110}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(1312, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(1310, 0, "Expression", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__NotIn_Expression__lhs_Expression_layouts_LAYOUTLIST_lit_notin_layouts_LAYOUTLIST_rhs_Expression__assoc__non_assoc, tmp);
	}
    protected static final void _init_prod__GreaterThanOrEq_Expression__lhs_Expression_layouts_LAYOUTLIST_lit___62_61_layouts_LAYOUTLIST_rhs_Expression__assoc__non_assoc(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(1368, 4, "Expression", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(1366, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(1364, 2, prod__lit___62_61__char_class___range__62_62_char_class___range__61_61_, new int[] {62,61}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(1362, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(1360, 0, "Expression", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__GreaterThanOrEq_Expression__lhs_Expression_layouts_LAYOUTLIST_lit___62_61_layouts_LAYOUTLIST_rhs_Expression__assoc__non_assoc, tmp);
	}
    protected static final void _init_prod__Equivalence_Expression__lhs_Expression_layouts_LAYOUTLIST_lit___60_61_61_62_layouts_LAYOUTLIST_rhs_Expression__assoc__non_assoc(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(1448, 4, "Expression", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(1446, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(1444, 2, prod__lit___60_61_61_62__char_class___range__60_60_char_class___range__61_61_char_class___range__61_61_char_class___range__62_62_, new int[] {60,61,61,62}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(1442, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(1440, 0, "Expression", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Equivalence_Expression__lhs_Expression_layouts_LAYOUTLIST_lit___60_61_61_62_layouts_LAYOUTLIST_rhs_Expression__assoc__non_assoc, tmp);
	}
    protected static final void _init_prod__GetAnnotation_Expression__expression_Expression_layouts_LAYOUTLIST_lit___64_layouts_LAYOUTLIST_name_Name_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(884, 4, "Name", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(882, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(880, 2, prod__lit___64__char_class___range__64_64_, new int[] {64}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(878, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(876, 0, "Expression", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__GetAnnotation_Expression__expression_Expression_layouts_LAYOUTLIST_lit___64_layouts_LAYOUTLIST_name_Name_, tmp);
	}
    protected static final void _init_prod__TransitiveReflexiveClosure_Expression__argument_Expression_layouts_LAYOUTLIST_conditional__lit___42__not_follow__lit___61_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new LiteralStackNode<IConstructor>(890, 2, prod__lit___42__char_class___range__42_42_, new int[] {42}, null, new ICompletionFilter[] {new StringFollowRestriction(new int[] {61})});
      tmp[1] = new NonTerminalStackNode<IConstructor>(888, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(886, 0, "Expression", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__TransitiveReflexiveClosure_Expression__argument_Expression_layouts_LAYOUTLIST_conditional__lit___42__not_follow__lit___61_, tmp);
	}
    protected static final void _init_prod__QualifiedName_Expression__qualifiedName_QualifiedName_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(892, 0, "QualifiedName", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__QualifiedName_Expression__qualifiedName_QualifiedName_, tmp);
	}
    protected static final void _init_prod__Addition_Expression__lhs_Expression_layouts_LAYOUTLIST_lit___43_layouts_LAYOUTLIST_rhs_Expression__assoc__left(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(1298, 4, "Expression", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(1296, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(1294, 2, prod__lit___43__char_class___range__43_43_, new int[] {43}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(1292, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(1290, 0, "Expression", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Addition_Expression__lhs_Expression_layouts_LAYOUTLIST_lit___43_layouts_LAYOUTLIST_rhs_Expression__assoc__left, tmp);
	}
    protected static final void _init_prod__FieldAccess_Expression__expression_Expression_layouts_LAYOUTLIST_lit___46_layouts_LAYOUTLIST_field_Name_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(958, 4, "Name", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(956, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(954, 2, prod__lit___46__char_class___range__46_46_, new int[] {46}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(952, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(950, 0, "Expression", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__FieldAccess_Expression__expression_Expression_layouts_LAYOUTLIST_lit___46_layouts_LAYOUTLIST_field_Name_, tmp);
	}
    protected static final void _init_prod__NonEquals_Expression__lhs_Expression_layouts_LAYOUTLIST_lit___33_61_layouts_LAYOUTLIST_rhs_Expression__assoc__non_assoc(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(1378, 4, "Expression", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(1376, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(1374, 2, prod__lit___33_61__char_class___range__33_33_char_class___range__61_61_, new int[] {33,61}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(1372, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(1370, 0, "Expression", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__NonEquals_Expression__lhs_Expression_layouts_LAYOUTLIST_lit___33_61_layouts_LAYOUTLIST_rhs_Expression__assoc__non_assoc, tmp);
	}
    protected static final void _init_prod__Composition_Expression__lhs_Expression_layouts_LAYOUTLIST_lit_o_layouts_LAYOUTLIST_rhs_Expression__assoc__left(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(1204, 4, "Expression", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(1202, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(1200, 2, prod__lit_o__char_class___range__111_111_, new int[] {111}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(1198, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(1196, 0, "Expression", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Composition_Expression__lhs_Expression_layouts_LAYOUTLIST_lit_o_layouts_LAYOUTLIST_rhs_Expression__assoc__left, tmp);
	}
    protected static final void _init_prod__Implication_Expression__lhs_Expression_layouts_LAYOUTLIST_lit___61_61_62_layouts_LAYOUTLIST_rhs_Expression__assoc__non_assoc(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(1438, 4, "Expression", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(1436, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(1434, 2, prod__lit___61_61_62__char_class___range__61_61_char_class___range__61_61_char_class___range__62_62_, new int[] {61,61,62}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(1432, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(1430, 0, "Expression", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Implication_Expression__lhs_Expression_layouts_LAYOUTLIST_lit___61_61_62_layouts_LAYOUTLIST_rhs_Expression__assoc__non_assoc, tmp);
	}
    protected static final void _init_prod__Remainder_Expression__lhs_Expression_layouts_LAYOUTLIST_lit___37_layouts_LAYOUTLIST_rhs_Expression__assoc__left(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(1228, 4, "Expression", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(1226, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(1224, 2, prod__lit___37__char_class___range__37_37_, new int[] {37}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(1222, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(1220, 0, "Expression", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Remainder_Expression__lhs_Expression_layouts_LAYOUTLIST_lit___37_layouts_LAYOUTLIST_rhs_Expression__assoc__left, tmp);
	}
    protected static final void _init_prod__FieldProject_Expression__expression_Expression_layouts_LAYOUTLIST_lit___60_layouts_LAYOUTLIST_fields_iter_seps__Field__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___62_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[7];
      
      tmp[6] = new LiteralStackNode<IConstructor>(1078, 6, prod__lit___62__char_class___range__62_62_, new int[] {62}, null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(1076, 5, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new SeparatedListStackNode<IConstructor>(1066, 4, regular__iter_seps__Field__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(1068, 0, "Field", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(1070, 1, "layouts_LAYOUTLIST", null, null), new LiteralStackNode<IConstructor>(1072, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode<IConstructor>(1074, 3, "layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(1064, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(1062, 2, prod__lit___60__char_class___range__60_60_, new int[] {60}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(1060, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(1058, 0, "Expression", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__FieldProject_Expression__expression_Expression_layouts_LAYOUTLIST_lit___60_layouts_LAYOUTLIST_fields_iter_seps__Field__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___62_, tmp);
	}
    protected static final void _init_prod__Literal_Expression__literal_Literal_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(1108, 0, "Literal", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Literal_Expression__literal_Literal_, tmp);
	}
    protected static final void _init_prod__IsDefined_Expression__argument_Expression_layouts_LAYOUTLIST_lit___63_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new LiteralStackNode<IConstructor>(1162, 2, prod__lit___63__char_class___range__63_63_, new int[] {63}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(1160, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(1158, 0, "Expression", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__IsDefined_Expression__argument_Expression_layouts_LAYOUTLIST_lit___63_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__FieldUpdate_Expression__expression_Expression_layouts_LAYOUTLIST_lit___91_layouts_LAYOUTLIST_key_Name_layouts_LAYOUTLIST_lit___61_layouts_LAYOUTLIST_replacement_Expression_layouts_LAYOUTLIST_lit___93_(builder);
      
        _init_prod__Range_Expression__lit___91_layouts_LAYOUTLIST_first_Expression_layouts_LAYOUTLIST_lit___46_46_layouts_LAYOUTLIST_last_Expression_layouts_LAYOUTLIST_lit___93_(builder);
      
        _init_prod__Modulo_Expression__lhs_Expression_layouts_LAYOUTLIST_lit_mod_layouts_LAYOUTLIST_rhs_Expression__assoc__left(builder);
      
        _init_prod__Or_Expression__lhs_Expression_layouts_LAYOUTLIST_lit___124_124_layouts_LAYOUTLIST_rhs_Expression__assoc__left(builder);
      
        _init_prod__Bracket_Expression__lit___40_layouts_LAYOUTLIST_expression_Expression_layouts_LAYOUTLIST_lit___41__bracket(builder);
      
        _init_prod__Is_Expression__expression_Expression_layouts_LAYOUTLIST_lit_is_layouts_LAYOUTLIST_name_Name_(builder);
      
        _init_prod__TransitiveClosure_Expression__argument_Expression_layouts_LAYOUTLIST_conditional__lit___43__not_follow__lit___61_(builder);
      
        _init_prod__Map_Expression__lit___40_layouts_LAYOUTLIST_mappings_iter_star_seps__Mapping__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_(builder);
      
        _init_prod__Self_Expression__lit_self_(builder);
      
        _init_prod__ReifyType_Expression__lit___35_layouts_LAYOUTLIST_conditional__type_Type__not_follow__lit___91_(builder);
      
        _init_prod__All_Expression__lit_all_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_generators_iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_(builder);
      
        _init_prod__Equals_Expression__lhs_Expression_layouts_LAYOUTLIST_lit___61_61_layouts_LAYOUTLIST_rhs_Expression__assoc__non_assoc(builder);
      
        _init_prod__Product_Expression__lhs_Expression_layouts_LAYOUTLIST_lit___42_layouts_LAYOUTLIST_conditional__empty__not_follow__lit___42_layouts_LAYOUTLIST_rhs_Expression__assoc__left(builder);
      
        _init_prod__Splice_Expression__lit___42_layouts_LAYOUTLIST_argument_Expression__assoc__non_assoc(builder);
      
        _init_prod__Closure_Expression__type_Type_layouts_LAYOUTLIST_parameters_Parameters_layouts_LAYOUTLIST_lit___123_layouts_LAYOUTLIST_statements_iter_seps__Statement__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_(builder);
      
        _init_prod__Visit_Expression__label_Label_layouts_LAYOUTLIST_visit_Visit_(builder);
      
        _init_prod__Join_Expression__lhs_Expression_layouts_LAYOUTLIST_lit_join_layouts_LAYOUTLIST_rhs_Expression__assoc__left(builder);
      
        _init_prod__It_Expression__conditional__lit_it__not_precede__char_class___range__65_90_range__95_95_range__97_122_not_follow__char_class___range__65_90_range__95_95_range__97_122_(builder);
      
        _init_prod__IfThenElse_Expression__condition_Expression_layouts_LAYOUTLIST_lit___63_layouts_LAYOUTLIST_thenExp_Expression_layouts_LAYOUTLIST_lit___58_layouts_LAYOUTLIST_elseExp_Expression__assoc__right(builder);
      
        _init_prod__List_Expression__lit___91_layouts_LAYOUTLIST_elements_iter_star_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___93_(builder);
      
        _init_prod__Subscript_Expression__expression_Expression_layouts_LAYOUTLIST_lit___91_layouts_LAYOUTLIST_subscripts_iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___93_(builder);
      
        _init_prod__Subtraction_Expression__lhs_Expression_layouts_LAYOUTLIST_lit___layouts_LAYOUTLIST_rhs_Expression__assoc__left(builder);
      
        _init_prod__GreaterThan_Expression__lhs_Expression_layouts_LAYOUTLIST_lit___62_layouts_LAYOUTLIST_rhs_Expression__assoc__non_assoc(builder);
      
        _init_prod__Enumerator_Expression__pattern_Pattern_layouts_LAYOUTLIST_lit___60_45_layouts_LAYOUTLIST_expression_Expression__assoc__non_assoc(builder);
      
        _init_prod__NoMatch_Expression__pattern_Pattern_layouts_LAYOUTLIST_lit___33_58_61_layouts_LAYOUTLIST_expression_Expression__assoc__non_assoc(builder);
      
        _init_prod__SetAnnotation_Expression__expression_Expression_layouts_LAYOUTLIST_lit___91_layouts_LAYOUTLIST_lit___64_layouts_LAYOUTLIST_name_Name_layouts_LAYOUTLIST_lit___61_layouts_LAYOUTLIST_value_Expression_layouts_LAYOUTLIST_lit___93_(builder);
      
        _init_prod__ReifiedType_Expression__lit_type_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_symbol_Expression_layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_definitions_Expression_layouts_LAYOUTLIST_lit___41_(builder);
      
        _init_prod__Negative_Expression__lit___layouts_LAYOUTLIST_argument_Expression_(builder);
      
        _init_prod__NonEmptyBlock_Expression__lit___123_layouts_LAYOUTLIST_statements_iter_seps__Statement__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_(builder);
      
        _init_prod__AppendAfter_Expression__lhs_Expression_layouts_LAYOUTLIST_conditional__lit___60_60__not_follow__lit___61_layouts_LAYOUTLIST_rhs_Expression__assoc__left(builder);
      
        _init_prod__LessThan_Expression__lhs_Expression_layouts_LAYOUTLIST_conditional__lit___60__not_follow__lit___layouts_LAYOUTLIST_rhs_Expression__assoc__non_assoc(builder);
      
        _init_prod__IfDefinedOtherwise_Expression__lhs_Expression_layouts_LAYOUTLIST_lit___63_layouts_LAYOUTLIST_rhs_Expression__assoc__non_assoc(builder);
      
        _init_prod__InsertBefore_Expression__lhs_Expression_layouts_LAYOUTLIST_lit___62_62_layouts_LAYOUTLIST_rhs_Expression__assoc__left(builder);
      
        _init_prod__Division_Expression__lhs_Expression_layouts_LAYOUTLIST_lit___47_layouts_LAYOUTLIST_rhs_Expression__assoc__left(builder);
      
        _init_prod__Comprehension_Expression__comprehension_Comprehension_(builder);
      
        _init_prod__LessThanOrEq_Expression__lhs_Expression_layouts_LAYOUTLIST_lit___60_61_layouts_LAYOUTLIST_rhs_Expression__assoc__non_assoc(builder);
      
        _init_prod__Any_Expression__lit_any_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_generators_iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_(builder);
      
        _init_prod__Has_Expression__expression_Expression_layouts_LAYOUTLIST_lit_has_layouts_LAYOUTLIST_name_Name_(builder);
      
        _init_prod__Set_Expression__lit___123_layouts_LAYOUTLIST_elements_iter_star_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_(builder);
      
        _init_prod__AsType_Expression__lit___91_layouts_LAYOUTLIST_type_Type_layouts_LAYOUTLIST_lit___93_layouts_LAYOUTLIST_argument_Expression_(builder);
      
        _init_prod__Reducer_Expression__lit___40_layouts_LAYOUTLIST_init_Expression_layouts_LAYOUTLIST_lit___124_layouts_LAYOUTLIST_result_Expression_layouts_LAYOUTLIST_lit___124_layouts_LAYOUTLIST_generators_iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_(builder);
      
        _init_prod__Intersection_Expression__lhs_Expression_layouts_LAYOUTLIST_lit___38_layouts_LAYOUTLIST_rhs_Expression__assoc__left(builder);
      
        _init_prod__And_Expression__lhs_Expression_layouts_LAYOUTLIST_lit___38_38_layouts_LAYOUTLIST_rhs_Expression__assoc__left(builder);
      
        _init_prod__Tuple_Expression__lit___60_layouts_LAYOUTLIST_elements_iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___62_(builder);
      
        _init_prod__In_Expression__lhs_Expression_layouts_LAYOUTLIST_lit_in_layouts_LAYOUTLIST_rhs_Expression__assoc__non_assoc(builder);
      
        _init_prod__StepRange_Expression__lit___91_layouts_LAYOUTLIST_first_Expression_layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_second_Expression_layouts_LAYOUTLIST_lit___46_46_layouts_LAYOUTLIST_last_Expression_layouts_LAYOUTLIST_lit___93_(builder);
      
        _init_prod__Match_Expression__pattern_Pattern_layouts_LAYOUTLIST_lit___58_61_layouts_LAYOUTLIST_expression_Expression__assoc__non_assoc(builder);
      
        _init_prod__CallOrTree_Expression__expression_Expression_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_arguments_iter_star_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_(builder);
      
        _init_prod__Negation_Expression__lit___33_layouts_LAYOUTLIST_argument_Expression_(builder);
      
        _init_prod__VoidClosure_Expression__parameters_Parameters_layouts_LAYOUTLIST_lit___123_layouts_LAYOUTLIST_statements_iter_star_seps__Statement__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_(builder);
      
        _init_prod__NotIn_Expression__lhs_Expression_layouts_LAYOUTLIST_lit_notin_layouts_LAYOUTLIST_rhs_Expression__assoc__non_assoc(builder);
      
        _init_prod__GreaterThanOrEq_Expression__lhs_Expression_layouts_LAYOUTLIST_lit___62_61_layouts_LAYOUTLIST_rhs_Expression__assoc__non_assoc(builder);
      
        _init_prod__Equivalence_Expression__lhs_Expression_layouts_LAYOUTLIST_lit___60_61_61_62_layouts_LAYOUTLIST_rhs_Expression__assoc__non_assoc(builder);
      
        _init_prod__GetAnnotation_Expression__expression_Expression_layouts_LAYOUTLIST_lit___64_layouts_LAYOUTLIST_name_Name_(builder);
      
        _init_prod__TransitiveReflexiveClosure_Expression__argument_Expression_layouts_LAYOUTLIST_conditional__lit___42__not_follow__lit___61_(builder);
      
        _init_prod__QualifiedName_Expression__qualifiedName_QualifiedName_(builder);
      
        _init_prod__Addition_Expression__lhs_Expression_layouts_LAYOUTLIST_lit___43_layouts_LAYOUTLIST_rhs_Expression__assoc__left(builder);
      
        _init_prod__FieldAccess_Expression__expression_Expression_layouts_LAYOUTLIST_lit___46_layouts_LAYOUTLIST_field_Name_(builder);
      
        _init_prod__NonEquals_Expression__lhs_Expression_layouts_LAYOUTLIST_lit___33_61_layouts_LAYOUTLIST_rhs_Expression__assoc__non_assoc(builder);
      
        _init_prod__Composition_Expression__lhs_Expression_layouts_LAYOUTLIST_lit_o_layouts_LAYOUTLIST_rhs_Expression__assoc__left(builder);
      
        _init_prod__Implication_Expression__lhs_Expression_layouts_LAYOUTLIST_lit___61_61_62_layouts_LAYOUTLIST_rhs_Expression__assoc__non_assoc(builder);
      
        _init_prod__Remainder_Expression__lhs_Expression_layouts_LAYOUTLIST_lit___37_layouts_LAYOUTLIST_rhs_Expression__assoc__left(builder);
      
        _init_prod__FieldProject_Expression__expression_Expression_layouts_LAYOUTLIST_lit___60_layouts_LAYOUTLIST_fields_iter_seps__Field__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___62_(builder);
      
        _init_prod__Literal_Expression__literal_Literal_(builder);
      
        _init_prod__IsDefined_Expression__argument_Expression_layouts_LAYOUTLIST_lit___63_(builder);
      
    }
  }
	
  protected static class TagString {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__TagString__lit___123_contents_iter_star__alt___TagString_char_class___range__0_122_range__124_124_range__126_16777215_seq___lit___92_char_class___range__123_123_range__125_125_lit___125_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new LiteralStackNode<IConstructor>(1506, 2, prod__lit___125__char_class___range__125_125_, new int[] {125}, null, null);
      tmp[1] = new ListStackNode<IConstructor>(1492, 1, regular__iter_star__alt___TagString_char_class___range__0_122_range__124_124_range__126_16777215_seq___lit___92_char_class___range__123_123_range__125_125, new AlternativeStackNode<IConstructor>(1494, 0, regular__alt___TagString_char_class___range__0_122_range__124_124_range__126_16777215_seq___lit___92_char_class___range__123_123_range__125_125, (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(1496, 0, "TagString", null, null), new CharStackNode<IConstructor>(1498, 0, new int[][]{{0,122},{124,124},{126,16777215}}, null, null), new SequenceStackNode<IConstructor>(1500, 0, regular__seq___lit___92_char_class___range__123_123_range__125_125, (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new LiteralStackNode<IConstructor>(1502, 0, prod__lit___92__char_class___range__92_92_, new int[] {92}, null, null), new CharStackNode<IConstructor>(1504, 1, new int[][]{{123,123},{125,125}}, null, null)}, null, null)}, null, null), false, null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(1490, 0, prod__lit___123__char_class___range__123_123_, new int[] {123}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__TagString__lit___123_contents_iter_star__alt___TagString_char_class___range__0_122_range__124_124_range__126_16777215_seq___lit___92_char_class___range__123_123_range__125_125_lit___125_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__TagString__lit___123_contents_iter_star__alt___TagString_char_class___range__0_122_range__124_124_range__126_16777215_seq___lit___92_char_class___range__123_123_range__125_125_lit___125_(builder);
      
    }
  }
	
  protected static class Nonterminal {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__Nonterminal__conditional__char_class___range__65_90__not_precede__char_class___range__65_90_conditional__iter_star__char_class___range__48_57_range__65_90_range__95_95_range__97_122__not_follow__char_class___range__48_57_range__65_90_range__95_95_range__97_122_delete__RascalReservedKeywords_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[2];
      
      tmp[1] = new ListStackNode<IConstructor>(1520, 1, regular__iter_star__char_class___range__48_57_range__65_90_range__95_95_range__97_122, new CharStackNode<IConstructor>(1522, 0, new int[][]{{48,57},{65,90},{95,95},{97,122}}, null, null), false, null, new ICompletionFilter[] {new CharFollowRestriction(new int[][]{{48,57},{65,90},{95,95},{97,122}})});
      tmp[0] = new CharStackNode<IConstructor>(1518, 0, new int[][]{{65,90}}, new IEnterFilter[] {new CharPrecedeRestriction(new int[][]{{65,90}})}, null);
      builder.addAlternative(ObjectRascalRascal.prod__Nonterminal__conditional__char_class___range__65_90__not_precede__char_class___range__65_90_conditional__iter_star__char_class___range__48_57_range__65_90_range__95_95_range__97_122__not_follow__char_class___range__48_57_range__65_90_range__95_95_range__97_122_delete__RascalReservedKeywords_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__Nonterminal__conditional__char_class___range__65_90__not_precede__char_class___range__65_90_conditional__iter_star__char_class___range__48_57_range__65_90_range__95_95_range__97_122__not_follow__char_class___range__48_57_range__65_90_range__95_95_range__97_122_delete__RascalReservedKeywords_(builder);
      
    }
  }
	
  protected static class PreModule {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__Default_PreModule__header_Header_layouts_LAYOUTLIST_conditional__empty__not_follow__HeaderKeyword_layouts_LAYOUTLIST_rest_Rest_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(1548, 4, "Rest", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(1546, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new EmptyStackNode<IConstructor>(1544, 2, regular__empty, null, new ICompletionFilter[] {new StringFollowRestriction(new int[] {108,101,120,105,99,97,108}), new StringFollowRestriction(new int[] {105,109,112,111,114,116}), new StringFollowRestriction(new int[] {115,116,97,114,116}), new StringFollowRestriction(new int[] {115,121,110,116,97,120}), new StringFollowRestriction(new int[] {101,120,116,101,110,100}), new StringFollowRestriction(new int[] {108,97,121,111,117,116}), new StringFollowRestriction(new int[] {107,101,121,119,111,114,100})});
      tmp[1] = new NonTerminalStackNode<IConstructor>(1542, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(1540, 0, "Header", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Default_PreModule__header_Header_layouts_LAYOUTLIST_conditional__empty__not_follow__HeaderKeyword_layouts_LAYOUTLIST_rest_Rest_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__Default_PreModule__header_Header_layouts_LAYOUTLIST_conditional__empty__not_follow__HeaderKeyword_layouts_LAYOUTLIST_rest_Rest_(builder);
      
    }
  }
	
  protected static class ProtocolPart {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__NonInterpolated_ProtocolPart__protocolChars_ProtocolChars_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(1552, 0, "ProtocolChars", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__NonInterpolated_ProtocolPart__protocolChars_ProtocolChars_, tmp);
	}
    protected static final void _init_prod__Interpolated_ProtocolPart__pre_PreProtocolChars_layouts_LAYOUTLIST_expression_Expression_layouts_LAYOUTLIST_tail_ProtocolTail_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(1562, 4, "ProtocolTail", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(1560, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(1558, 2, "Expression", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(1556, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(1554, 0, "PreProtocolChars", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Interpolated_ProtocolPart__pre_PreProtocolChars_layouts_LAYOUTLIST_expression_Expression_layouts_LAYOUTLIST_tail_ProtocolTail_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__NonInterpolated_ProtocolPart__protocolChars_ProtocolChars_(builder);
      
        _init_prod__Interpolated_ProtocolPart__pre_PreProtocolChars_layouts_LAYOUTLIST_expression_Expression_layouts_LAYOUTLIST_tail_ProtocolTail_(builder);
      
    }
  }
	
  protected static class Comment {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__Comment__lit___47_42_iter_star__alt___conditional__char_class___range__42_42__not_follow__char_class___range__47_47_char_class___range__0_41_range__43_16777215_lit___42_47__tag__category___67_111_109_109_101_110_116(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new LiteralStackNode<IConstructor>(1600, 2, prod__lit___42_47__char_class___range__42_42_char_class___range__47_47_, new int[] {42,47}, null, null);
      tmp[1] = new ListStackNode<IConstructor>(1592, 1, regular__iter_star__alt___conditional__char_class___range__42_42__not_follow__char_class___range__47_47_char_class___range__0_41_range__43_16777215, new AlternativeStackNode<IConstructor>(1594, 0, regular__alt___conditional__char_class___range__42_42__not_follow__char_class___range__47_47_char_class___range__0_41_range__43_16777215, (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new CharStackNode<IConstructor>(1596, 0, new int[][]{{42,42}}, null, new ICompletionFilter[] {new CharFollowRestriction(new int[][]{{47,47}})}), new CharStackNode<IConstructor>(1598, 0, new int[][]{{0,41},{43,16777215}}, null, null)}, null, null), false, null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(1590, 0, prod__lit___47_42__char_class___range__47_47_char_class___range__42_42_, new int[] {47,42}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Comment__lit___47_42_iter_star__alt___conditional__char_class___range__42_42__not_follow__char_class___range__47_47_char_class___range__0_41_range__43_16777215_lit___42_47__tag__category___67_111_109_109_101_110_116, tmp);
	}
    protected static final void _init_prod__Comment__lit___47_47_conditional__iter_star__char_class___range__0_9_range__11_16777215__not_follow__char_class___range__9_9_range__13_13_range__32_32_end_of_line__tag__category___67_111_109_109_101_110_116(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[2];
      
      tmp[1] = new ListStackNode<IConstructor>(1604, 1, regular__iter_star__char_class___range__0_9_range__11_16777215, new CharStackNode<IConstructor>(1606, 0, new int[][]{{0,9},{11,16777215}}, null, null), false, null, new ICompletionFilter[] {new CharFollowRestriction(new int[][]{{9,9},{13,13},{32,32}}), new AtEndOfLineRequirement()});
      tmp[0] = new LiteralStackNode<IConstructor>(1602, 0, prod__lit___47_47__char_class___range__47_47_char_class___range__47_47_, new int[] {47,47}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Comment__lit___47_47_conditional__iter_star__char_class___range__0_9_range__11_16777215__not_follow__char_class___range__9_9_range__13_13_range__32_32_end_of_line__tag__category___67_111_109_109_101_110_116, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__Comment__lit___47_42_iter_star__alt___conditional__char_class___range__42_42__not_follow__char_class___range__47_47_char_class___range__0_41_range__43_16777215_lit___42_47__tag__category___67_111_109_109_101_110_116(builder);
      
        _init_prod__Comment__lit___47_47_conditional__iter_star__char_class___range__0_9_range__11_16777215__not_follow__char_class___range__9_9_range__13_13_range__32_32_end_of_line__tag__category___67_111_109_109_101_110_116(builder);
      
    }
  }
	
  protected static class Label {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__Empty_Label__(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new EpsilonStackNode<IConstructor>(1608, 0);
      builder.addAlternative(ObjectRascalRascal.prod__Empty_Label__, tmp);
	}
    protected static final void _init_prod__Default_Label__name_Name_layouts_LAYOUTLIST_lit___58_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new LiteralStackNode<IConstructor>(1614, 2, prod__lit___58__char_class___range__58_58_, new int[] {58}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(1612, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(1610, 0, "Name", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Default_Label__name_Name_layouts_LAYOUTLIST_lit___58_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__Empty_Label__(builder);
      
        _init_prod__Default_Label__name_Name_layouts_LAYOUTLIST_lit___58_(builder);
      
    }
  }
	
  protected static class Field {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__Index_Field__fieldIndex_IntegerLiteral_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(1688, 0, "IntegerLiteral", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Index_Field__fieldIndex_IntegerLiteral_, tmp);
	}
    protected static final void _init_prod__Name_Field__fieldName_Name_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(1690, 0, "Name", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Name_Field__fieldName_Name_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__Index_Field__fieldIndex_IntegerLiteral_(builder);
      
        _init_prod__Name_Field__fieldName_Name_(builder);
      
    }
  }
	
  protected static class FunctionModifier {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__OpenRecursion_FunctionModifier__lit_openrec_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(1722, 0, prod__lit_openrec__char_class___range__111_111_char_class___range__112_112_char_class___range__101_101_char_class___range__110_110_char_class___range__114_114_char_class___range__101_101_char_class___range__99_99_, new int[] {111,112,101,110,114,101,99}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__OpenRecursion_FunctionModifier__lit_openrec_, tmp);
	}
    protected static final void _init_prod__Open_FunctionModifier__lit_open_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(1724, 0, prod__lit_open__char_class___range__111_111_char_class___range__112_112_char_class___range__101_101_char_class___range__110_110_, new int[] {111,112,101,110}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Open_FunctionModifier__lit_open_, tmp);
	}
    protected static final void _init_prod__Java_FunctionModifier__lit_java_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(1726, 0, prod__lit_java__char_class___range__106_106_char_class___range__97_97_char_class___range__118_118_char_class___range__97_97_, new int[] {106,97,118,97}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Java_FunctionModifier__lit_java_, tmp);
	}
    protected static final void _init_prod__Default_FunctionModifier__lit_default_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(1728, 0, prod__lit_default__char_class___range__100_100_char_class___range__101_101_char_class___range__102_102_char_class___range__97_97_char_class___range__117_117_char_class___range__108_108_char_class___range__116_116_, new int[] {100,101,102,97,117,108,116}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Default_FunctionModifier__lit_default_, tmp);
	}
    protected static final void _init_prod__Test_FunctionModifier__lit_test_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(1730, 0, prod__lit_test__char_class___range__116_116_char_class___range__101_101_char_class___range__115_115_char_class___range__116_116_, new int[] {116,101,115,116}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Test_FunctionModifier__lit_test_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__OpenRecursion_FunctionModifier__lit_openrec_(builder);
      
        _init_prod__Open_FunctionModifier__lit_open_(builder);
      
        _init_prod__Java_FunctionModifier__lit_java_(builder);
      
        _init_prod__Default_FunctionModifier__lit_default_(builder);
      
        _init_prod__Test_FunctionModifier__lit_test_(builder);
      
    }
  }
	
  protected static class EvalCommand {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__Import_EvalCommand__imported_Import_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(1716, 0, "Import", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Import_EvalCommand__imported_Import_, tmp);
	}
    protected static final void _init_prod__Declaration_EvalCommand__declaration_Declaration_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(1718, 0, "Declaration", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Declaration_EvalCommand__declaration_Declaration_, tmp);
	}
    protected static final void _init_prod__Statement_EvalCommand__statement_Statement_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(1720, 0, "Statement", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Statement_EvalCommand__statement_Statement_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__Import_EvalCommand__imported_Import_(builder);
      
        _init_prod__Declaration_EvalCommand__declaration_Declaration_(builder);
      
        _init_prod__Statement_EvalCommand__statement_Statement_(builder);
      
    }
  }
	
  protected static class ProtocolChars {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__ProtocolChars__char_class___range__124_124_URLChars_conditional__lit___58_47_47__not_follow__char_class___range__9_10_range__13_13_range__32_32_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new LiteralStackNode<IConstructor>(1776, 2, prod__lit___58_47_47__char_class___range__58_58_char_class___range__47_47_char_class___range__47_47_, new int[] {58,47,47}, null, new ICompletionFilter[] {new CharFollowRestriction(new int[][]{{9,10},{13,13},{32,32}})});
      tmp[1] = new NonTerminalStackNode<IConstructor>(1774, 1, "URLChars", null, null);
      tmp[0] = new CharStackNode<IConstructor>(1772, 0, new int[][]{{124,124}}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__ProtocolChars__char_class___range__124_124_URLChars_conditional__lit___58_47_47__not_follow__char_class___range__9_10_range__13_13_range__32_32_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__ProtocolChars__char_class___range__124_124_URLChars_conditional__lit___58_47_47__not_follow__char_class___range__9_10_range__13_13_range__32_32_(builder);
      
    }
  }
	
  protected static class Assignment {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__Subtraction_Assignment__lit___45_61_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(1756, 0, prod__lit___45_61__char_class___range__45_45_char_class___range__61_61_, new int[] {45,61}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Subtraction_Assignment__lit___45_61_, tmp);
	}
    protected static final void _init_prod__Intersection_Assignment__lit___38_61_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(1758, 0, prod__lit___38_61__char_class___range__38_38_char_class___range__61_61_, new int[] {38,61}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Intersection_Assignment__lit___38_61_, tmp);
	}
    protected static final void _init_prod__Append_Assignment__lit___60_60_61_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(1760, 0, prod__lit___60_60_61__char_class___range__60_60_char_class___range__60_60_char_class___range__61_61_, new int[] {60,60,61}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Append_Assignment__lit___60_60_61_, tmp);
	}
    protected static final void _init_prod__Default_Assignment__lit___61_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(1762, 0, prod__lit___61__char_class___range__61_61_, new int[] {61}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Default_Assignment__lit___61_, tmp);
	}
    protected static final void _init_prod__IfDefined_Assignment__lit___63_61_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(1764, 0, prod__lit___63_61__char_class___range__63_63_char_class___range__61_61_, new int[] {63,61}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__IfDefined_Assignment__lit___63_61_, tmp);
	}
    protected static final void _init_prod__Division_Assignment__lit___47_61_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(1766, 0, prod__lit___47_61__char_class___range__47_47_char_class___range__61_61_, new int[] {47,61}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Division_Assignment__lit___47_61_, tmp);
	}
    protected static final void _init_prod__Product_Assignment__lit___42_61_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(1768, 0, prod__lit___42_61__char_class___range__42_42_char_class___range__61_61_, new int[] {42,61}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Product_Assignment__lit___42_61_, tmp);
	}
    protected static final void _init_prod__Addition_Assignment__lit___43_61_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(1770, 0, prod__lit___43_61__char_class___range__43_43_char_class___range__61_61_, new int[] {43,61}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Addition_Assignment__lit___43_61_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__Subtraction_Assignment__lit___45_61_(builder);
      
        _init_prod__Intersection_Assignment__lit___38_61_(builder);
      
        _init_prod__Append_Assignment__lit___60_60_61_(builder);
      
        _init_prod__Default_Assignment__lit___61_(builder);
      
        _init_prod__IfDefined_Assignment__lit___63_61_(builder);
      
        _init_prod__Division_Assignment__lit___47_61_(builder);
      
        _init_prod__Product_Assignment__lit___42_61_(builder);
      
        _init_prod__Addition_Assignment__lit___43_61_(builder);
      
    }
  }
	
  protected static class Assignable {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__Bracket_Assignable__lit___40_layouts_LAYOUTLIST_arg_Assignable_layouts_LAYOUTLIST_lit___41__bracket(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new LiteralStackNode<IConstructor>(1830, 4, prod__lit___41__char_class___range__41_41_, new int[] {41}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(1828, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(1826, 2, "Assignable", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(1824, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(1822, 0, prod__lit___40__char_class___range__40_40_, new int[] {40}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Bracket_Assignable__lit___40_layouts_LAYOUTLIST_arg_Assignable_layouts_LAYOUTLIST_lit___41__bracket, tmp);
	}
    protected static final void _init_prod__Variable_Assignable__qualifiedName_QualifiedName_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(1832, 0, "QualifiedName", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Variable_Assignable__qualifiedName_QualifiedName_, tmp);
	}
    protected static final void _init_prod__Tuple_Assignable__lit___60_layouts_LAYOUTLIST_elements_iter_seps__Assignable__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___62_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[0] = new LiteralStackNode<IConstructor>(1844, 0, prod__lit___60__char_class___range__60_60_, new int[] {60}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(1846, 1, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new SeparatedListStackNode<IConstructor>(1848, 2, regular__iter_seps__Assignable__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(1850, 0, "Assignable", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(1852, 1, "layouts_LAYOUTLIST", null, null), new LiteralStackNode<IConstructor>(1854, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode<IConstructor>(1856, 3, "layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(1858, 3, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode<IConstructor>(1860, 4, prod__lit___62__char_class___range__62_62_, new int[] {62}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Tuple_Assignable__lit___60_layouts_LAYOUTLIST_elements_iter_seps__Assignable__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___62_, tmp);
	}
    protected static final void _init_prod__IfDefinedOrDefault_Assignable__receiver_Assignable_layouts_LAYOUTLIST_lit___63_layouts_LAYOUTLIST_defaultExpression_Expression_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(1842, 4, "Expression", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(1840, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(1838, 2, prod__lit___63__char_class___range__63_63_, new int[] {63}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(1836, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(1834, 0, "Assignable", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__IfDefinedOrDefault_Assignable__receiver_Assignable_layouts_LAYOUTLIST_lit___63_layouts_LAYOUTLIST_defaultExpression_Expression_, tmp);
	}
    protected static final void _init_prod__Annotation_Assignable__receiver_Assignable_layouts_LAYOUTLIST_lit___64_layouts_LAYOUTLIST_annotation_Name_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(1870, 4, "Name", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(1868, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(1866, 2, prod__lit___64__char_class___range__64_64_, new int[] {64}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(1864, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(1862, 0, "Assignable", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Annotation_Assignable__receiver_Assignable_layouts_LAYOUTLIST_lit___64_layouts_LAYOUTLIST_annotation_Name_, tmp);
	}
    protected static final void _init_prod__FieldAccess_Assignable__receiver_Assignable_layouts_LAYOUTLIST_lit___46_layouts_LAYOUTLIST_field_Name_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(1880, 4, "Name", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(1878, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(1876, 2, prod__lit___46__char_class___range__46_46_, new int[] {46}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(1874, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(1872, 0, "Assignable", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__FieldAccess_Assignable__receiver_Assignable_layouts_LAYOUTLIST_lit___46_layouts_LAYOUTLIST_field_Name_, tmp);
	}
    protected static final void _init_prod__Subscript_Assignable__receiver_Assignable_layouts_LAYOUTLIST_lit___91_layouts_LAYOUTLIST_subscript_Expression_layouts_LAYOUTLIST_lit___93_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[7];
      
      tmp[6] = new LiteralStackNode<IConstructor>(1894, 6, prod__lit___93__char_class___range__93_93_, new int[] {93}, null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(1892, 5, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new NonTerminalStackNode<IConstructor>(1890, 4, "Expression", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(1888, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(1886, 2, prod__lit___91__char_class___range__91_91_, new int[] {91}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(1884, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(1882, 0, "Assignable", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Subscript_Assignable__receiver_Assignable_layouts_LAYOUTLIST_lit___91_layouts_LAYOUTLIST_subscript_Expression_layouts_LAYOUTLIST_lit___93_, tmp);
	}
    protected static final void _init_prod__Constructor_Assignable__name_Name_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_arguments_iter_seps__Assignable__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[7];
      
      tmp[6] = new LiteralStackNode<IConstructor>(1916, 6, prod__lit___41__char_class___range__41_41_, new int[] {41}, null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(1914, 5, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new SeparatedListStackNode<IConstructor>(1904, 4, regular__iter_seps__Assignable__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(1906, 0, "Assignable", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(1908, 1, "layouts_LAYOUTLIST", null, null), new LiteralStackNode<IConstructor>(1910, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode<IConstructor>(1912, 3, "layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(1902, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(1900, 2, prod__lit___40__char_class___range__40_40_, new int[] {40}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(1898, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(1896, 0, "Name", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Constructor_Assignable__name_Name_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_arguments_iter_seps__Assignable__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__Bracket_Assignable__lit___40_layouts_LAYOUTLIST_arg_Assignable_layouts_LAYOUTLIST_lit___41__bracket(builder);
      
        _init_prod__Variable_Assignable__qualifiedName_QualifiedName_(builder);
      
        _init_prod__Tuple_Assignable__lit___60_layouts_LAYOUTLIST_elements_iter_seps__Assignable__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___62_(builder);
      
        _init_prod__IfDefinedOrDefault_Assignable__receiver_Assignable_layouts_LAYOUTLIST_lit___63_layouts_LAYOUTLIST_defaultExpression_Expression_(builder);
      
        _init_prod__Annotation_Assignable__receiver_Assignable_layouts_LAYOUTLIST_lit___64_layouts_LAYOUTLIST_annotation_Name_(builder);
      
        _init_prod__FieldAccess_Assignable__receiver_Assignable_layouts_LAYOUTLIST_lit___46_layouts_LAYOUTLIST_field_Name_(builder);
      
        _init_prod__Subscript_Assignable__receiver_Assignable_layouts_LAYOUTLIST_lit___91_layouts_LAYOUTLIST_subscript_Expression_layouts_LAYOUTLIST_lit___93_(builder);
      
        _init_prod__Constructor_Assignable__name_Name_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_arguments_iter_seps__Assignable__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_(builder);
      
    }
  }
	
  protected static class HeaderKeyword {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__HeaderKeyword__lit_start_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(1960, 0, prod__lit_start__char_class___range__115_115_char_class___range__116_116_char_class___range__97_97_char_class___range__114_114_char_class___range__116_116_, new int[] {115,116,97,114,116}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__HeaderKeyword__lit_start_, tmp);
	}
    protected static final void _init_prod__HeaderKeyword__lit_keyword_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(1962, 0, prod__lit_keyword__char_class___range__107_107_char_class___range__101_101_char_class___range__121_121_char_class___range__119_119_char_class___range__111_111_char_class___range__114_114_char_class___range__100_100_, new int[] {107,101,121,119,111,114,100}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__HeaderKeyword__lit_keyword_, tmp);
	}
    protected static final void _init_prod__HeaderKeyword__lit_syntax_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(1964, 0, prod__lit_syntax__char_class___range__115_115_char_class___range__121_121_char_class___range__110_110_char_class___range__116_116_char_class___range__97_97_char_class___range__120_120_, new int[] {115,121,110,116,97,120}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__HeaderKeyword__lit_syntax_, tmp);
	}
    protected static final void _init_prod__HeaderKeyword__lit_import_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(1966, 0, prod__lit_import__char_class___range__105_105_char_class___range__109_109_char_class___range__112_112_char_class___range__111_111_char_class___range__114_114_char_class___range__116_116_, new int[] {105,109,112,111,114,116}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__HeaderKeyword__lit_import_, tmp);
	}
    protected static final void _init_prod__HeaderKeyword__lit_lexical_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(1968, 0, prod__lit_lexical__char_class___range__108_108_char_class___range__101_101_char_class___range__120_120_char_class___range__105_105_char_class___range__99_99_char_class___range__97_97_char_class___range__108_108_, new int[] {108,101,120,105,99,97,108}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__HeaderKeyword__lit_lexical_, tmp);
	}
    protected static final void _init_prod__HeaderKeyword__lit_extend_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(1970, 0, prod__lit_extend__char_class___range__101_101_char_class___range__120_120_char_class___range__116_116_char_class___range__101_101_char_class___range__110_110_char_class___range__100_100_, new int[] {101,120,116,101,110,100}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__HeaderKeyword__lit_extend_, tmp);
	}
    protected static final void _init_prod__HeaderKeyword__lit_layout_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(1972, 0, prod__lit_layout__char_class___range__108_108_char_class___range__97_97_char_class___range__121_121_char_class___range__111_111_char_class___range__117_117_char_class___range__116_116_, new int[] {108,97,121,111,117,116}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__HeaderKeyword__lit_layout_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__HeaderKeyword__lit_start_(builder);
      
        _init_prod__HeaderKeyword__lit_keyword_(builder);
      
        _init_prod__HeaderKeyword__lit_syntax_(builder);
      
        _init_prod__HeaderKeyword__lit_import_(builder);
      
        _init_prod__HeaderKeyword__lit_lexical_(builder);
      
        _init_prod__HeaderKeyword__lit_extend_(builder);
      
        _init_prod__HeaderKeyword__lit_layout_(builder);
      
    }
  }
	
  protected static class Parameters {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__Default_Parameters__lit___40_layouts_LAYOUTLIST_formals_Formals_layouts_LAYOUTLIST_lit___41_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new LiteralStackNode<IConstructor>(2026, 4, prod__lit___41__char_class___range__41_41_, new int[] {41}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(2024, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(2022, 2, "Formals", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(2020, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(2018, 0, prod__lit___40__char_class___range__40_40_, new int[] {40}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Default_Parameters__lit___40_layouts_LAYOUTLIST_formals_Formals_layouts_LAYOUTLIST_lit___41_, tmp);
	}
    protected static final void _init_prod__VarArgs_Parameters__lit___40_layouts_LAYOUTLIST_formals_Formals_layouts_LAYOUTLIST_lit___46_46_46_layouts_LAYOUTLIST_lit___41_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[7];
      
      tmp[6] = new LiteralStackNode<IConstructor>(2040, 6, prod__lit___41__char_class___range__41_41_, new int[] {41}, null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(2038, 5, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode<IConstructor>(2036, 4, prod__lit___46_46_46__char_class___range__46_46_char_class___range__46_46_char_class___range__46_46_, new int[] {46,46,46}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(2034, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(2032, 2, "Formals", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(2030, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(2028, 0, prod__lit___40__char_class___range__40_40_, new int[] {40}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__VarArgs_Parameters__lit___40_layouts_LAYOUTLIST_formals_Formals_layouts_LAYOUTLIST_lit___46_46_46_layouts_LAYOUTLIST_lit___41_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__Default_Parameters__lit___40_layouts_LAYOUTLIST_formals_Formals_layouts_LAYOUTLIST_lit___41_(builder);
      
        _init_prod__VarArgs_Parameters__lit___40_layouts_LAYOUTLIST_formals_Formals_layouts_LAYOUTLIST_lit___46_46_46_layouts_LAYOUTLIST_lit___41_(builder);
      
    }
  }
	
  protected static class DatePart {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__DatePart__char_class___range__48_57_char_class___range__48_57_char_class___range__48_57_char_class___range__48_57_lit___char_class___range__48_49_char_class___range__48_57_lit___char_class___range__48_51_char_class___range__48_57_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[10];
      
      tmp[9] = new CharStackNode<IConstructor>(2060, 9, new int[][]{{48,57}}, null, null);
      tmp[8] = new CharStackNode<IConstructor>(2058, 8, new int[][]{{48,51}}, null, null);
      tmp[7] = new LiteralStackNode<IConstructor>(2056, 7, prod__lit____char_class___range__45_45_, new int[] {45}, null, null);
      tmp[6] = new CharStackNode<IConstructor>(2054, 6, new int[][]{{48,57}}, null, null);
      tmp[5] = new CharStackNode<IConstructor>(2052, 5, new int[][]{{48,49}}, null, null);
      tmp[4] = new LiteralStackNode<IConstructor>(2050, 4, prod__lit____char_class___range__45_45_, new int[] {45}, null, null);
      tmp[3] = new CharStackNode<IConstructor>(2048, 3, new int[][]{{48,57}}, null, null);
      tmp[2] = new CharStackNode<IConstructor>(2046, 2, new int[][]{{48,57}}, null, null);
      tmp[1] = new CharStackNode<IConstructor>(2044, 1, new int[][]{{48,57}}, null, null);
      tmp[0] = new CharStackNode<IConstructor>(2042, 0, new int[][]{{48,57}}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__DatePart__char_class___range__48_57_char_class___range__48_57_char_class___range__48_57_char_class___range__48_57_lit___char_class___range__48_49_char_class___range__48_57_lit___char_class___range__48_51_char_class___range__48_57_, tmp);
	}
    protected static final void _init_prod__DatePart__char_class___range__48_57_char_class___range__48_57_char_class___range__48_57_char_class___range__48_57_char_class___range__48_49_char_class___range__48_57_char_class___range__48_51_char_class___range__48_57_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[8];
      
      tmp[7] = new CharStackNode<IConstructor>(2076, 7, new int[][]{{48,57}}, null, null);
      tmp[6] = new CharStackNode<IConstructor>(2074, 6, new int[][]{{48,51}}, null, null);
      tmp[5] = new CharStackNode<IConstructor>(2072, 5, new int[][]{{48,57}}, null, null);
      tmp[4] = new CharStackNode<IConstructor>(2070, 4, new int[][]{{48,49}}, null, null);
      tmp[3] = new CharStackNode<IConstructor>(2068, 3, new int[][]{{48,57}}, null, null);
      tmp[2] = new CharStackNode<IConstructor>(2066, 2, new int[][]{{48,57}}, null, null);
      tmp[1] = new CharStackNode<IConstructor>(2064, 1, new int[][]{{48,57}}, null, null);
      tmp[0] = new CharStackNode<IConstructor>(2062, 0, new int[][]{{48,57}}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__DatePart__char_class___range__48_57_char_class___range__48_57_char_class___range__48_57_char_class___range__48_57_char_class___range__48_49_char_class___range__48_57_char_class___range__48_51_char_class___range__48_57_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__DatePart__char_class___range__48_57_char_class___range__48_57_char_class___range__48_57_char_class___range__48_57_lit___char_class___range__48_49_char_class___range__48_57_lit___char_class___range__48_51_char_class___range__48_57_(builder);
      
        _init_prod__DatePart__char_class___range__48_57_char_class___range__48_57_char_class___range__48_57_char_class___range__48_57_char_class___range__48_49_char_class___range__48_57_char_class___range__48_51_char_class___range__48_57_(builder);
      
    }
  }
	
  protected static class Mapping__Pattern {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__Default_Mapping__Pattern__from_Pattern_layouts_LAYOUTLIST_lit___58_layouts_LAYOUTLIST_to_Pattern_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(2086, 4, "Pattern", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(2084, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(2082, 2, prod__lit___58__char_class___range__58_58_, new int[] {58}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(2080, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(2078, 0, "Pattern", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Default_Mapping__Pattern__from_Pattern_layouts_LAYOUTLIST_lit___58_layouts_LAYOUTLIST_to_Pattern_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__Default_Mapping__Pattern__from_Pattern_layouts_LAYOUTLIST_lit___58_layouts_LAYOUTLIST_to_Pattern_(builder);
      
    }
  }
	
  protected static class LocalVariableDeclaration {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__Dynamic_LocalVariableDeclaration__lit_dynamic_layouts_LAYOUTLIST_declarator_Declarator_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode<IConstructor>(2096, 2, "Declarator", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(2094, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(2092, 0, prod__lit_dynamic__char_class___range__100_100_char_class___range__121_121_char_class___range__110_110_char_class___range__97_97_char_class___range__109_109_char_class___range__105_105_char_class___range__99_99_, new int[] {100,121,110,97,109,105,99}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Dynamic_LocalVariableDeclaration__lit_dynamic_layouts_LAYOUTLIST_declarator_Declarator_, tmp);
	}
    protected static final void _init_prod__Default_LocalVariableDeclaration__declarator_Declarator_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(2098, 0, "Declarator", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Default_LocalVariableDeclaration__declarator_Declarator_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__Dynamic_LocalVariableDeclaration__lit_dynamic_layouts_LAYOUTLIST_declarator_Declarator_(builder);
      
        _init_prod__Default_LocalVariableDeclaration__declarator_Declarator_(builder);
      
    }
  }
	
  protected static class StringConstant {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__StringConstant__lit___34_iter_star__StringCharacter_lit___34__tag__category___67_111_110_115_116_97_110_116(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new LiteralStackNode<IConstructor>(2112, 2, prod__lit___34__char_class___range__34_34_, new int[] {34}, null, null);
      tmp[1] = new ListStackNode<IConstructor>(2108, 1, regular__iter_star__StringCharacter, new NonTerminalStackNode<IConstructor>(2110, 0, "StringCharacter", null, null), false, null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(2106, 0, prod__lit___34__char_class___range__34_34_, new int[] {34}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__StringConstant__lit___34_iter_star__StringCharacter_lit___34__tag__category___67_111_110_115_116_97_110_116, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__StringConstant__lit___34_iter_star__StringCharacter_lit___34__tag__category___67_111_110_115_116_97_110_116(builder);
      
    }
  }
	
  protected static class start__Module {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__start__Module__layouts_LAYOUTLIST_top_Module_layouts_LAYOUTLIST_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode<IConstructor>(2118, 2, "layouts_LAYOUTLIST", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(2116, 1, "Module", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(2114, 0, "layouts_LAYOUTLIST", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__start__Module__layouts_LAYOUTLIST_top_Module_layouts_LAYOUTLIST_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__start__Module__layouts_LAYOUTLIST_top_Module_layouts_LAYOUTLIST_(builder);
      
    }
  }
	
  protected static class DataTypeSelector {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__Selector_DataTypeSelector__sort_QualifiedName_layouts_LAYOUTLIST_lit___46_layouts_LAYOUTLIST_production_Name_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(2128, 4, "Name", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(2126, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(2124, 2, prod__lit___46__char_class___range__46_46_, new int[] {46}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(2122, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(2120, 0, "QualifiedName", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Selector_DataTypeSelector__sort_QualifiedName_layouts_LAYOUTLIST_lit___46_layouts_LAYOUTLIST_production_Name_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__Selector_DataTypeSelector__sort_QualifiedName_layouts_LAYOUTLIST_lit___46_layouts_LAYOUTLIST_production_Name_(builder);
      
    }
  }
	
  protected static class StringTail {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__MidInterpolated_StringTail__mid_MidStringChars_layouts_LAYOUTLIST_expression_Expression_layouts_LAYOUTLIST_tail_StringTail_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(2138, 4, "StringTail", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(2136, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(2134, 2, "Expression", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(2132, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(2130, 0, "MidStringChars", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__MidInterpolated_StringTail__mid_MidStringChars_layouts_LAYOUTLIST_expression_Expression_layouts_LAYOUTLIST_tail_StringTail_, tmp);
	}
    protected static final void _init_prod__MidTemplate_StringTail__mid_MidStringChars_layouts_LAYOUTLIST_template_StringTemplate_layouts_LAYOUTLIST_tail_StringTail_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(2148, 4, "StringTail", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(2146, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(2144, 2, "StringTemplate", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(2142, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(2140, 0, "MidStringChars", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__MidTemplate_StringTail__mid_MidStringChars_layouts_LAYOUTLIST_template_StringTemplate_layouts_LAYOUTLIST_tail_StringTail_, tmp);
	}
    protected static final void _init_prod__Post_StringTail__post_PostStringChars_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(2150, 0, "PostStringChars", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Post_StringTail__post_PostStringChars_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__MidInterpolated_StringTail__mid_MidStringChars_layouts_LAYOUTLIST_expression_Expression_layouts_LAYOUTLIST_tail_StringTail_(builder);
      
        _init_prod__MidTemplate_StringTail__mid_MidStringChars_layouts_LAYOUTLIST_template_StringTemplate_layouts_LAYOUTLIST_tail_StringTail_(builder);
      
        _init_prod__Post_StringTail__post_PostStringChars_(builder);
      
    }
  }
	
  protected static class PatternWithAction {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__Replacing_PatternWithAction__pattern_Pattern_layouts_LAYOUTLIST_lit___61_62_layouts_LAYOUTLIST_replacement_Replacement_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(2160, 4, "Replacement", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(2158, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(2156, 2, prod__lit___61_62__char_class___range__61_61_char_class___range__62_62_, new int[] {61,62}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(2154, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(2152, 0, "Pattern", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Replacing_PatternWithAction__pattern_Pattern_layouts_LAYOUTLIST_lit___61_62_layouts_LAYOUTLIST_replacement_Replacement_, tmp);
	}
    protected static final void _init_prod__Arbitrary_PatternWithAction__pattern_Pattern_layouts_LAYOUTLIST_lit___58_layouts_LAYOUTLIST_statement_Statement_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(2170, 4, "Statement", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(2168, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(2166, 2, prod__lit___58__char_class___range__58_58_, new int[] {58}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(2164, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(2162, 0, "Pattern", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Arbitrary_PatternWithAction__pattern_Pattern_layouts_LAYOUTLIST_lit___58_layouts_LAYOUTLIST_statement_Statement_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__Replacing_PatternWithAction__pattern_Pattern_layouts_LAYOUTLIST_lit___61_62_layouts_LAYOUTLIST_replacement_Replacement_(builder);
      
        _init_prod__Arbitrary_PatternWithAction__pattern_Pattern_layouts_LAYOUTLIST_lit___58_layouts_LAYOUTLIST_statement_Statement_(builder);
      
    }
  }
	
  protected static class MidProtocolChars {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__MidProtocolChars__lit___62_URLChars_lit___60_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new LiteralStackNode<IConstructor>(2214, 2, prod__lit___60__char_class___range__60_60_, new int[] {60}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(2212, 1, "URLChars", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(2210, 0, prod__lit___62__char_class___range__62_62_, new int[] {62}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__MidProtocolChars__lit___62_URLChars_lit___60_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__MidProtocolChars__lit___62_URLChars_lit___60_(builder);
      
    }
  }
	
  protected static class PathTail {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__Mid_PathTail__mid_MidPathChars_layouts_LAYOUTLIST_expression_Expression_layouts_LAYOUTLIST_tail_PathTail_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(2206, 4, "PathTail", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(2204, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(2202, 2, "Expression", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(2200, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(2198, 0, "MidPathChars", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Mid_PathTail__mid_MidPathChars_layouts_LAYOUTLIST_expression_Expression_layouts_LAYOUTLIST_tail_PathTail_, tmp);
	}
    protected static final void _init_prod__Post_PathTail__post_PostPathChars_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(2208, 0, "PostPathChars", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Post_PathTail__post_PostPathChars_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__Mid_PathTail__mid_MidPathChars_layouts_LAYOUTLIST_expression_Expression_layouts_LAYOUTLIST_tail_PathTail_(builder);
      
        _init_prod__Post_PathTail__post_PostPathChars_(builder);
      
    }
  }
	
  protected static class JustDate {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__JustDate__lit___36_DatePart_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[2];
      
      tmp[1] = new NonTerminalStackNode<IConstructor>(2228, 1, "DatePart", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(2226, 0, prod__lit___36__char_class___range__36_36_, new int[] {36}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__JustDate__lit___36_DatePart_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__JustDate__lit___36_DatePart_(builder);
      
    }
  }
	
  protected static class Backslash {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__Backslash__conditional__char_class___range__92_92__not_follow__char_class___range__47_47_range__60_60_range__62_62_range__92_92_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new CharStackNode<IConstructor>(2252, 0, new int[][]{{92,92}}, null, new ICompletionFilter[] {new CharFollowRestriction(new int[][]{{47,47},{60,60},{62,62},{92,92}})});
      builder.addAlternative(ObjectRascalRascal.prod__Backslash__conditional__char_class___range__92_92__not_follow__char_class___range__47_47_range__60_60_range__62_62_range__92_92_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__Backslash__conditional__char_class___range__92_92__not_follow__char_class___range__47_47_range__60_60_range__62_62_range__92_92_(builder);
      
    }
  }
	
  protected static class start__Commands {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__start__Commands__layouts_LAYOUTLIST_top_Commands_layouts_LAYOUTLIST_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode<IConstructor>(2250, 2, "layouts_LAYOUTLIST", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(2248, 1, "Commands", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(2246, 0, "layouts_LAYOUTLIST", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__start__Commands__layouts_LAYOUTLIST_top_Commands_layouts_LAYOUTLIST_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__start__Commands__layouts_LAYOUTLIST_top_Commands_layouts_LAYOUTLIST_(builder);
      
    }
  }
	
  protected static class CaseInsensitiveStringConstant {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__CaseInsensitiveStringConstant__lit___39_iter_star__StringCharacter_lit___39__tag__category___67_111_110_115_116_97_110_116(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new LiteralStackNode<IConstructor>(2244, 2, prod__lit___39__char_class___range__39_39_, new int[] {39}, null, null);
      tmp[1] = new ListStackNode<IConstructor>(2240, 1, regular__iter_star__StringCharacter, new NonTerminalStackNode<IConstructor>(2242, 0, "StringCharacter", null, null), false, null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(2238, 0, prod__lit___39__char_class___range__39_39_, new int[] {39}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__CaseInsensitiveStringConstant__lit___39_iter_star__StringCharacter_lit___39__tag__category___67_111_110_115_116_97_110_116, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__CaseInsensitiveStringConstant__lit___39_iter_star__StringCharacter_lit___39__tag__category___67_111_110_115_116_97_110_116(builder);
      
    }
  }
	
  protected static class Tags {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__Default_Tags__tags_iter_star_seps__Tag__layouts_LAYOUTLIST_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new SeparatedListStackNode<IConstructor>(2254, 0, regular__iter_star_seps__Tag__layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(2256, 0, "Tag", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(2258, 1, "layouts_LAYOUTLIST", null, null)}, false, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Default_Tags__tags_iter_star_seps__Tag__layouts_LAYOUTLIST_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__Default_Tags__tags_iter_star_seps__Tag__layouts_LAYOUTLIST_(builder);
      
    }
  }
	
  protected static class Formals {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__Default_Formals__formals_iter_star_seps__Pattern__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new SeparatedListStackNode<IConstructor>(2268, 0, regular__iter_star_seps__Pattern__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(2270, 0, "Pattern", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(2272, 1, "layouts_LAYOUTLIST", null, null), new LiteralStackNode<IConstructor>(2274, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode<IConstructor>(2276, 3, "layouts_LAYOUTLIST", null, null)}, false, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Default_Formals__formals_iter_star_seps__Pattern__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__Default_Formals__formals_iter_star_seps__Pattern__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_(builder);
      
    }
  }
	
  protected static class Start {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__Present_Start__lit_start_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(2278, 0, prod__lit_start__char_class___range__115_115_char_class___range__116_116_char_class___range__97_97_char_class___range__114_114_char_class___range__116_116_, new int[] {115,116,97,114,116}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Present_Start__lit_start_, tmp);
	}
    protected static final void _init_prod__Absent_Start__(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new EpsilonStackNode<IConstructor>(2280, 0);
      builder.addAlternative(ObjectRascalRascal.prod__Absent_Start__, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__Present_Start__lit_start_(builder);
      
        _init_prod__Absent_Start__(builder);
      
    }
  }
	
  protected static class Name {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__Name__char_class___range__92_92_char_class___range__65_90_range__95_95_range__97_122_conditional__iter_star__char_class___range__45_45_range__48_57_range__65_90_range__95_95_range__97_122__not_follow__char_class___range__45_45_range__48_57_range__65_90_range__95_95_range__97_122_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new ListStackNode<IConstructor>(2286, 2, regular__iter_star__char_class___range__45_45_range__48_57_range__65_90_range__95_95_range__97_122, new CharStackNode<IConstructor>(2288, 0, new int[][]{{45,45},{48,57},{65,90},{95,95},{97,122}}, null, null), false, null, new ICompletionFilter[] {new CharFollowRestriction(new int[][]{{45,45},{48,57},{65,90},{95,95},{97,122}})});
      tmp[1] = new CharStackNode<IConstructor>(2284, 1, new int[][]{{65,90},{95,95},{97,122}}, null, null);
      tmp[0] = new CharStackNode<IConstructor>(2282, 0, new int[][]{{92,92}}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Name__char_class___range__92_92_char_class___range__65_90_range__95_95_range__97_122_conditional__iter_star__char_class___range__45_45_range__48_57_range__65_90_range__95_95_range__97_122__not_follow__char_class___range__45_45_range__48_57_range__65_90_range__95_95_range__97_122_, tmp);
	}
    protected static final void _init_prod__Name__conditional__seq___conditional__char_class___range__65_90_range__95_95_range__97_122__not_precede__char_class___range__65_90_range__95_95_range__97_122_conditional__iter_star__char_class___range__48_57_range__65_90_range__95_95_range__97_122__not_follow__char_class___range__48_57_range__65_90_range__95_95_range__97_122__delete__RascalKeywords_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new SequenceStackNode<IConstructor>(2290, 0, regular__seq___conditional__char_class___range__65_90_range__95_95_range__97_122__not_precede__char_class___range__65_90_range__95_95_range__97_122_conditional__iter_star__char_class___range__48_57_range__65_90_range__95_95_range__97_122__not_follow__char_class___range__48_57_range__65_90_range__95_95_range__97_122, (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new CharStackNode<IConstructor>(2292, 0, new int[][]{{65,90},{95,95},{97,122}}, new IEnterFilter[] {new CharPrecedeRestriction(new int[][]{{65,90},{95,95},{97,122}})}, null), new ListStackNode<IConstructor>(2294, 1, regular__iter_star__char_class___range__48_57_range__65_90_range__95_95_range__97_122, new CharStackNode<IConstructor>(2296, 0, new int[][]{{48,57},{65,90},{95,95},{97,122}}, null, null), false, null, new ICompletionFilter[] {new CharFollowRestriction(new int[][]{{48,57},{65,90},{95,95},{97,122}})})}, null, new ICompletionFilter[] {new StringMatchRestriction(new int[] {105,109,112,111,114,116}), new StringMatchRestriction(new int[] {105,110}), new StringMatchRestriction(new int[] {114,97,116}), new StringMatchRestriction(new int[] {99,111,110,116,105,110,117,101}), new StringMatchRestriction(new int[] {97,108,108}), new StringMatchRestriction(new int[] {102,97,108,115,101}), new StringMatchRestriction(new int[] {97,110,110,111}), new StringMatchRestriction(new int[] {98,114,97,99,107,101,116}), new StringMatchRestriction(new int[] {100,97,116,97}), new StringMatchRestriction(new int[] {106,111,105,110}), new StringMatchRestriction(new int[] {108,97,121,111,117,116}), new StringMatchRestriction(new int[] {105,116}), new StringMatchRestriction(new int[] {115,119,105,116,99,104}), new StringMatchRestriction(new int[] {114,101,116,117,114,110}), new StringMatchRestriction(new int[] {99,97,115,101}), new StringMatchRestriction(new int[] {115,116,114}), new StringMatchRestriction(new int[] {119,104,105,108,101}), new StringMatchRestriction(new int[] {100,121,110,97,109,105,99}), new StringMatchRestriction(new int[] {115,111,108,118,101}), new StringMatchRestriction(new int[] {110,111,116,105,110}), new StringMatchRestriction(new int[] {105,110,115,101,114,116}), new StringMatchRestriction(new int[] {101,108,115,101}), new StringMatchRestriction(new int[] {116,121,112,101}), new StringMatchRestriction(new int[] {116,114,121}), new StringMatchRestriction(new int[] {99,97,116,99,104}), new StringMatchRestriction(new int[] {110,117,109}), new StringMatchRestriction(new int[] {111,112,101,110,114,101,99}), new StringMatchRestriction(new int[] {109,111,100}), new StringMatchRestriction(new int[] {110,111,100,101}), new StringMatchRestriction(new int[] {102,105,110,97,108,108,121}), new StringMatchRestriction(new int[] {112,114,105,118,97,116,101}), new StringMatchRestriction(new int[] {116,114,117,101}), new StringMatchRestriction(new int[] {98,97,103}), new StringMatchRestriction(new int[] {118,111,105,100}), new StringMatchRestriction(new int[] {110,111,110,45,97,115,115,111,99}), new StringMatchRestriction(new int[] {97,115,115,111,99}), new StringMatchRestriction(new int[] {116,101,115,116}), new StringMatchRestriction(new int[] {105,102}), new StringMatchRestriction(new int[] {102,97,105,108}), new StringMatchRestriction(new int[] {108,105,115,116}), new StringMatchRestriction(new int[] {114,101,97,108}), new StringMatchRestriction(new int[] {114,101,108}), new StringMatchRestriction(new int[] {116,97,103}), new StringMatchRestriction(new int[] {101,120,116,101,110,100}), new StringMatchRestriction(new int[] {97,112,112,101,110,100}), new StringMatchRestriction(new int[] {116,104,114,111,119}), new StringMatchRestriction(new int[] {111,110,101}), new StringMatchRestriction(new int[] {115,116,97,114,116}), new StringMatchRestriction(new int[] {115,101,116}), new StringMatchRestriction(new int[] {109,111,100,117,108,101}), new StringMatchRestriction(new int[] {97,110,121}), new StringMatchRestriction(new int[] {105,110,116}), new StringMatchRestriction(new int[] {112,117,98,108,105,99}), new StringMatchRestriction(new int[] {98,111,111,108}), new StringMatchRestriction(new int[] {118,97,108,117,101}), new StringMatchRestriction(new int[] {98,114,101,97,107}), new StringMatchRestriction(new int[] {102,105,108,116,101,114}), new StringMatchRestriction(new int[] {100,97,116,101,116,105,109,101}), new StringMatchRestriction(new int[] {97,115,115,101,114,116}), new StringMatchRestriction(new int[] {108,111,99}), new StringMatchRestriction(new int[] {100,101,102,97,117,108,116}), new StringMatchRestriction(new int[] {115,101,108,102}), new StringMatchRestriction(new int[] {116,104,114,111,119,115}), new StringMatchRestriction(new int[] {116,117,112,108,101}), new StringMatchRestriction(new int[] {102,111,114}), new StringMatchRestriction(new int[] {111,112,101,110}), new StringMatchRestriction(new int[] {118,105,115,105,116}), new StringMatchRestriction(new int[] {97,108,105,97,115}), new StringMatchRestriction(new int[] {109,97,112})});
      builder.addAlternative(ObjectRascalRascal.prod__Name__conditional__seq___conditional__char_class___range__65_90_range__95_95_range__97_122__not_precede__char_class___range__65_90_range__95_95_range__97_122_conditional__iter_star__char_class___range__48_57_range__65_90_range__95_95_range__97_122__not_follow__char_class___range__48_57_range__65_90_range__95_95_range__97_122__delete__RascalKeywords_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__Name__char_class___range__92_92_char_class___range__65_90_range__95_95_range__97_122_conditional__iter_star__char_class___range__45_45_range__48_57_range__65_90_range__95_95_range__97_122__not_follow__char_class___range__45_45_range__48_57_range__65_90_range__95_95_range__97_122_(builder);
      
        _init_prod__Name__conditional__seq___conditional__char_class___range__65_90_range__95_95_range__97_122__not_precede__char_class___range__65_90_range__95_95_range__97_122_conditional__iter_star__char_class___range__48_57_range__65_90_range__95_95_range__97_122__not_follow__char_class___range__48_57_range__65_90_range__95_95_range__97_122__delete__RascalKeywords_(builder);
      
    }
  }
	
  protected static class TimePartNoTZ {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__TimePartNoTZ__char_class___range__48_50_char_class___range__48_57_char_class___range__48_53_char_class___range__48_57_char_class___range__48_53_char_class___range__48_57_opt__seq___char_class___range__44_44_range__46_46_char_class___range__48_57_opt__seq___char_class___range__48_57_opt__char_class___range__48_57_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[7];
      
      tmp[6] = new OptionalStackNode<IConstructor>(2330, 6, regular__opt__seq___char_class___range__44_44_range__46_46_char_class___range__48_57_opt__seq___char_class___range__48_57_opt__char_class___range__48_57, new SequenceStackNode<IConstructor>(2332, 0, regular__seq___char_class___range__44_44_range__46_46_char_class___range__48_57_opt__seq___char_class___range__48_57_opt__char_class___range__48_57, (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new CharStackNode<IConstructor>(2334, 0, new int[][]{{44,44},{46,46}}, null, null), new CharStackNode<IConstructor>(2336, 1, new int[][]{{48,57}}, null, null), new OptionalStackNode<IConstructor>(2338, 2, regular__opt__seq___char_class___range__48_57_opt__char_class___range__48_57, new SequenceStackNode<IConstructor>(2340, 0, regular__seq___char_class___range__48_57_opt__char_class___range__48_57, (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new CharStackNode<IConstructor>(2342, 0, new int[][]{{48,57}}, null, null), new OptionalStackNode<IConstructor>(2344, 1, regular__opt__char_class___range__48_57, new CharStackNode<IConstructor>(2346, 0, new int[][]{{48,57}}, null, null), null, null)}, null, null), null, null)}, null, null), null, null);
      tmp[5] = new CharStackNode<IConstructor>(2328, 5, new int[][]{{48,57}}, null, null);
      tmp[4] = new CharStackNode<IConstructor>(2326, 4, new int[][]{{48,53}}, null, null);
      tmp[3] = new CharStackNode<IConstructor>(2324, 3, new int[][]{{48,57}}, null, null);
      tmp[2] = new CharStackNode<IConstructor>(2322, 2, new int[][]{{48,53}}, null, null);
      tmp[1] = new CharStackNode<IConstructor>(2320, 1, new int[][]{{48,57}}, null, null);
      tmp[0] = new CharStackNode<IConstructor>(2318, 0, new int[][]{{48,50}}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__TimePartNoTZ__char_class___range__48_50_char_class___range__48_57_char_class___range__48_53_char_class___range__48_57_char_class___range__48_53_char_class___range__48_57_opt__seq___char_class___range__44_44_range__46_46_char_class___range__48_57_opt__seq___char_class___range__48_57_opt__char_class___range__48_57_, tmp);
	}
    protected static final void _init_prod__TimePartNoTZ__char_class___range__48_50_char_class___range__48_57_lit___58_char_class___range__48_53_char_class___range__48_57_lit___58_char_class___range__48_53_char_class___range__48_57_opt__seq___char_class___range__44_44_range__46_46_char_class___range__48_57_opt__seq___char_class___range__48_57_opt__char_class___range__48_57_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[9];
      
      tmp[8] = new OptionalStackNode<IConstructor>(2364, 8, regular__opt__seq___char_class___range__44_44_range__46_46_char_class___range__48_57_opt__seq___char_class___range__48_57_opt__char_class___range__48_57, new SequenceStackNode<IConstructor>(2366, 0, regular__seq___char_class___range__44_44_range__46_46_char_class___range__48_57_opt__seq___char_class___range__48_57_opt__char_class___range__48_57, (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new CharStackNode<IConstructor>(2368, 0, new int[][]{{44,44},{46,46}}, null, null), new CharStackNode<IConstructor>(2370, 1, new int[][]{{48,57}}, null, null), new OptionalStackNode<IConstructor>(2372, 2, regular__opt__seq___char_class___range__48_57_opt__char_class___range__48_57, new SequenceStackNode<IConstructor>(2374, 0, regular__seq___char_class___range__48_57_opt__char_class___range__48_57, (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new CharStackNode<IConstructor>(2376, 0, new int[][]{{48,57}}, null, null), new OptionalStackNode<IConstructor>(2378, 1, regular__opt__char_class___range__48_57, new CharStackNode<IConstructor>(2380, 0, new int[][]{{48,57}}, null, null), null, null)}, null, null), null, null)}, null, null), null, null);
      tmp[7] = new CharStackNode<IConstructor>(2362, 7, new int[][]{{48,57}}, null, null);
      tmp[6] = new CharStackNode<IConstructor>(2360, 6, new int[][]{{48,53}}, null, null);
      tmp[5] = new LiteralStackNode<IConstructor>(2358, 5, prod__lit___58__char_class___range__58_58_, new int[] {58}, null, null);
      tmp[4] = new CharStackNode<IConstructor>(2356, 4, new int[][]{{48,57}}, null, null);
      tmp[3] = new CharStackNode<IConstructor>(2354, 3, new int[][]{{48,53}}, null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(2352, 2, prod__lit___58__char_class___range__58_58_, new int[] {58}, null, null);
      tmp[1] = new CharStackNode<IConstructor>(2350, 1, new int[][]{{48,57}}, null, null);
      tmp[0] = new CharStackNode<IConstructor>(2348, 0, new int[][]{{48,50}}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__TimePartNoTZ__char_class___range__48_50_char_class___range__48_57_lit___58_char_class___range__48_53_char_class___range__48_57_lit___58_char_class___range__48_53_char_class___range__48_57_opt__seq___char_class___range__44_44_range__46_46_char_class___range__48_57_opt__seq___char_class___range__48_57_opt__char_class___range__48_57_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__TimePartNoTZ__char_class___range__48_50_char_class___range__48_57_char_class___range__48_53_char_class___range__48_57_char_class___range__48_53_char_class___range__48_57_opt__seq___char_class___range__44_44_range__46_46_char_class___range__48_57_opt__seq___char_class___range__48_57_opt__char_class___range__48_57_(builder);
      
        _init_prod__TimePartNoTZ__char_class___range__48_50_char_class___range__48_57_lit___58_char_class___range__48_53_char_class___range__48_57_lit___58_char_class___range__48_53_char_class___range__48_57_opt__seq___char_class___range__44_44_range__46_46_char_class___range__48_57_opt__seq___char_class___range__48_57_opt__char_class___range__48_57_(builder);
      
    }
  }
	
  protected static class StructuredType {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__Default_StructuredType__basicType_BasicType_layouts_LAYOUTLIST_lit___91_layouts_LAYOUTLIST_arguments_iter_seps__TypeArg__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___93_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[7];
      
      tmp[6] = new LiteralStackNode<IConstructor>(2422, 6, prod__lit___93__char_class___range__93_93_, new int[] {93}, null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(2420, 5, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new SeparatedListStackNode<IConstructor>(2410, 4, regular__iter_seps__TypeArg__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(2412, 0, "TypeArg", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(2414, 1, "layouts_LAYOUTLIST", null, null), new LiteralStackNode<IConstructor>(2416, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode<IConstructor>(2418, 3, "layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(2408, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(2406, 2, prod__lit___91__char_class___range__91_91_, new int[] {91}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(2404, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(2402, 0, "BasicType", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Default_StructuredType__basicType_BasicType_layouts_LAYOUTLIST_lit___91_layouts_LAYOUTLIST_arguments_iter_seps__TypeArg__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___93_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__Default_StructuredType__basicType_BasicType_layouts_LAYOUTLIST_lit___91_layouts_LAYOUTLIST_arguments_iter_seps__TypeArg__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___93_(builder);
      
    }
  }
	
  protected static class Declarator {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__Default_Declarator__type_Type_layouts_LAYOUTLIST_variables_iter_seps__Variable__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new SeparatedListStackNode<IConstructor>(2446, 2, regular__iter_seps__Variable__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(2448, 0, "Variable", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(2450, 1, "layouts_LAYOUTLIST", null, null), new LiteralStackNode<IConstructor>(2452, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode<IConstructor>(2454, 3, "layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(2444, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(2442, 0, "Type", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Default_Declarator__type_Type_layouts_LAYOUTLIST_variables_iter_seps__Variable__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__Default_Declarator__type_Type_layouts_LAYOUTLIST_variables_iter_seps__Variable__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_(builder);
      
    }
  }
	
  protected static class start__PreModule {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__start__PreModule__layouts_LAYOUTLIST_top_PreModule_layouts_LAYOUTLIST_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode<IConstructor>(2464, 2, "layouts_LAYOUTLIST", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(2462, 1, "PreModule", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(2460, 0, "layouts_LAYOUTLIST", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__start__PreModule__layouts_LAYOUTLIST_top_PreModule_layouts_LAYOUTLIST_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__start__PreModule__layouts_LAYOUTLIST_top_PreModule_layouts_LAYOUTLIST_(builder);
      
    }
  }
	
  protected static class Variant {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__NAryConstructor_Variant__name_Name_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_arguments_iter_star_seps__TypeArg__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[7];
      
      tmp[6] = new LiteralStackNode<IConstructor>(2508, 6, prod__lit___41__char_class___range__41_41_, new int[] {41}, null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(2506, 5, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new SeparatedListStackNode<IConstructor>(2496, 4, regular__iter_star_seps__TypeArg__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(2498, 0, "TypeArg", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(2500, 1, "layouts_LAYOUTLIST", null, null), new LiteralStackNode<IConstructor>(2502, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode<IConstructor>(2504, 3, "layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(2494, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(2492, 2, prod__lit___40__char_class___range__40_40_, new int[] {40}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(2490, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(2488, 0, "Name", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__NAryConstructor_Variant__name_Name_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_arguments_iter_star_seps__TypeArg__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__NAryConstructor_Variant__name_Name_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_arguments_iter_star_seps__TypeArg__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_(builder);
      
    }
  }
	
  protected static class FunctionDeclaration {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__Conditional_FunctionDeclaration__tags_Tags_layouts_LAYOUTLIST_visibility_Visibility_layouts_LAYOUTLIST_signature_Signature_layouts_LAYOUTLIST_lit___61_layouts_LAYOUTLIST_expression_Expression_layouts_LAYOUTLIST_lit_when_layouts_LAYOUTLIST_conditions_iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___59__tag__Foldable(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[15];
      
      tmp[14] = new LiteralStackNode<IConstructor>(2546, 14, prod__lit___59__char_class___range__59_59_, new int[] {59}, null, null);
      tmp[13] = new NonTerminalStackNode<IConstructor>(2544, 13, "layouts_LAYOUTLIST", null, null);
      tmp[12] = new SeparatedListStackNode<IConstructor>(2534, 12, regular__iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(2536, 0, "Expression", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(2538, 1, "layouts_LAYOUTLIST", null, null), new LiteralStackNode<IConstructor>(2540, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode<IConstructor>(2542, 3, "layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[11] = new NonTerminalStackNode<IConstructor>(2532, 11, "layouts_LAYOUTLIST", null, null);
      tmp[10] = new LiteralStackNode<IConstructor>(2530, 10, prod__lit_when__char_class___range__119_119_char_class___range__104_104_char_class___range__101_101_char_class___range__110_110_, new int[] {119,104,101,110}, null, null);
      tmp[9] = new NonTerminalStackNode<IConstructor>(2528, 9, "layouts_LAYOUTLIST", null, null);
      tmp[8] = new NonTerminalStackNode<IConstructor>(2526, 8, "Expression", null, null);
      tmp[7] = new NonTerminalStackNode<IConstructor>(2524, 7, "layouts_LAYOUTLIST", null, null);
      tmp[6] = new LiteralStackNode<IConstructor>(2522, 6, prod__lit___61__char_class___range__61_61_, new int[] {61}, null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(2520, 5, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new NonTerminalStackNode<IConstructor>(2518, 4, "Signature", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(2516, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(2514, 2, "Visibility", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(2512, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(2510, 0, "Tags", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Conditional_FunctionDeclaration__tags_Tags_layouts_LAYOUTLIST_visibility_Visibility_layouts_LAYOUTLIST_signature_Signature_layouts_LAYOUTLIST_lit___61_layouts_LAYOUTLIST_expression_Expression_layouts_LAYOUTLIST_lit_when_layouts_LAYOUTLIST_conditions_iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___59__tag__Foldable, tmp);
	}
    protected static final void _init_prod__Default_FunctionDeclaration__tags_Tags_layouts_LAYOUTLIST_visibility_Visibility_layouts_LAYOUTLIST_signature_Signature_layouts_LAYOUTLIST_body_FunctionBody__tag__Foldable(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[7];
      
      tmp[6] = new NonTerminalStackNode<IConstructor>(2560, 6, "FunctionBody", null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(2558, 5, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new NonTerminalStackNode<IConstructor>(2556, 4, "Signature", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(2554, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(2552, 2, "Visibility", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(2550, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(2548, 0, "Tags", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Default_FunctionDeclaration__tags_Tags_layouts_LAYOUTLIST_visibility_Visibility_layouts_LAYOUTLIST_signature_Signature_layouts_LAYOUTLIST_body_FunctionBody__tag__Foldable, tmp);
	}
    protected static final void _init_prod__Abstract_FunctionDeclaration__tags_Tags_layouts_LAYOUTLIST_visibility_Visibility_layouts_LAYOUTLIST_signature_Signature_layouts_LAYOUTLIST_lit___59_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[7];
      
      tmp[6] = new LiteralStackNode<IConstructor>(2574, 6, prod__lit___59__char_class___range__59_59_, new int[] {59}, null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(2572, 5, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new NonTerminalStackNode<IConstructor>(2570, 4, "Signature", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(2568, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(2566, 2, "Visibility", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(2564, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(2562, 0, "Tags", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Abstract_FunctionDeclaration__tags_Tags_layouts_LAYOUTLIST_visibility_Visibility_layouts_LAYOUTLIST_signature_Signature_layouts_LAYOUTLIST_lit___59_, tmp);
	}
    protected static final void _init_prod__Expression_FunctionDeclaration__tags_Tags_layouts_LAYOUTLIST_visibility_Visibility_layouts_LAYOUTLIST_signature_Signature_layouts_LAYOUTLIST_lit___61_layouts_LAYOUTLIST_expression_Expression_layouts_LAYOUTLIST_lit___59__tag__Foldable(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[11];
      
      tmp[10] = new LiteralStackNode<IConstructor>(2596, 10, prod__lit___59__char_class___range__59_59_, new int[] {59}, null, null);
      tmp[9] = new NonTerminalStackNode<IConstructor>(2594, 9, "layouts_LAYOUTLIST", null, null);
      tmp[8] = new NonTerminalStackNode<IConstructor>(2592, 8, "Expression", null, null);
      tmp[7] = new NonTerminalStackNode<IConstructor>(2590, 7, "layouts_LAYOUTLIST", null, null);
      tmp[6] = new LiteralStackNode<IConstructor>(2588, 6, prod__lit___61__char_class___range__61_61_, new int[] {61}, null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(2586, 5, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new NonTerminalStackNode<IConstructor>(2584, 4, "Signature", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(2582, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(2580, 2, "Visibility", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(2578, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(2576, 0, "Tags", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Expression_FunctionDeclaration__tags_Tags_layouts_LAYOUTLIST_visibility_Visibility_layouts_LAYOUTLIST_signature_Signature_layouts_LAYOUTLIST_lit___61_layouts_LAYOUTLIST_expression_Expression_layouts_LAYOUTLIST_lit___59__tag__Foldable, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__Conditional_FunctionDeclaration__tags_Tags_layouts_LAYOUTLIST_visibility_Visibility_layouts_LAYOUTLIST_signature_Signature_layouts_LAYOUTLIST_lit___61_layouts_LAYOUTLIST_expression_Expression_layouts_LAYOUTLIST_lit_when_layouts_LAYOUTLIST_conditions_iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___59__tag__Foldable(builder);
      
        _init_prod__Default_FunctionDeclaration__tags_Tags_layouts_LAYOUTLIST_visibility_Visibility_layouts_LAYOUTLIST_signature_Signature_layouts_LAYOUTLIST_body_FunctionBody__tag__Foldable(builder);
      
        _init_prod__Abstract_FunctionDeclaration__tags_Tags_layouts_LAYOUTLIST_visibility_Visibility_layouts_LAYOUTLIST_signature_Signature_layouts_LAYOUTLIST_lit___59_(builder);
      
        _init_prod__Expression_FunctionDeclaration__tags_Tags_layouts_LAYOUTLIST_visibility_Visibility_layouts_LAYOUTLIST_signature_Signature_layouts_LAYOUTLIST_lit___61_layouts_LAYOUTLIST_expression_Expression_layouts_LAYOUTLIST_lit___59__tag__Foldable(builder);
      
    }
  }
	
  protected static class BasicType {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__Rational_BasicType__lit_rat_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(2624, 0, prod__lit_rat__char_class___range__114_114_char_class___range__97_97_char_class___range__116_116_, new int[] {114,97,116}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Rational_BasicType__lit_rat_, tmp);
	}
    protected static final void _init_prod__Bag_BasicType__lit_bag_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(2626, 0, prod__lit_bag__char_class___range__98_98_char_class___range__97_97_char_class___range__103_103_, new int[] {98,97,103}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Bag_BasicType__lit_bag_, tmp);
	}
    protected static final void _init_prod__DateTime_BasicType__lit_datetime_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(2628, 0, prod__lit_datetime__char_class___range__100_100_char_class___range__97_97_char_class___range__116_116_char_class___range__101_101_char_class___range__116_116_char_class___range__105_105_char_class___range__109_109_char_class___range__101_101_, new int[] {100,97,116,101,116,105,109,101}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__DateTime_BasicType__lit_datetime_, tmp);
	}
    protected static final void _init_prod__String_BasicType__lit_str_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(2630, 0, prod__lit_str__char_class___range__115_115_char_class___range__116_116_char_class___range__114_114_, new int[] {115,116,114}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__String_BasicType__lit_str_, tmp);
	}
    protected static final void _init_prod__Set_BasicType__lit_set_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(2632, 0, prod__lit_set__char_class___range__115_115_char_class___range__101_101_char_class___range__116_116_, new int[] {115,101,116}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Set_BasicType__lit_set_, tmp);
	}
    protected static final void _init_prod__Type_BasicType__lit_type_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(2634, 0, prod__lit_type__char_class___range__116_116_char_class___range__121_121_char_class___range__112_112_char_class___range__101_101_, new int[] {116,121,112,101}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Type_BasicType__lit_type_, tmp);
	}
    protected static final void _init_prod__Bool_BasicType__lit_bool_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(2636, 0, prod__lit_bool__char_class___range__98_98_char_class___range__111_111_char_class___range__111_111_char_class___range__108_108_, new int[] {98,111,111,108}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Bool_BasicType__lit_bool_, tmp);
	}
    protected static final void _init_prod__Relation_BasicType__lit_rel_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(2638, 0, prod__lit_rel__char_class___range__114_114_char_class___range__101_101_char_class___range__108_108_, new int[] {114,101,108}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Relation_BasicType__lit_rel_, tmp);
	}
    protected static final void _init_prod__Void_BasicType__lit_void_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(2640, 0, prod__lit_void__char_class___range__118_118_char_class___range__111_111_char_class___range__105_105_char_class___range__100_100_, new int[] {118,111,105,100}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Void_BasicType__lit_void_, tmp);
	}
    protected static final void _init_prod__Value_BasicType__lit_value_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(2642, 0, prod__lit_value__char_class___range__118_118_char_class___range__97_97_char_class___range__108_108_char_class___range__117_117_char_class___range__101_101_, new int[] {118,97,108,117,101}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Value_BasicType__lit_value_, tmp);
	}
    protected static final void _init_prod__Loc_BasicType__lit_loc_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(2644, 0, prod__lit_loc__char_class___range__108_108_char_class___range__111_111_char_class___range__99_99_, new int[] {108,111,99}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Loc_BasicType__lit_loc_, tmp);
	}
    protected static final void _init_prod__Num_BasicType__lit_num_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(2646, 0, prod__lit_num__char_class___range__110_110_char_class___range__117_117_char_class___range__109_109_, new int[] {110,117,109}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Num_BasicType__lit_num_, tmp);
	}
    protected static final void _init_prod__List_BasicType__lit_list_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(2648, 0, prod__lit_list__char_class___range__108_108_char_class___range__105_105_char_class___range__115_115_char_class___range__116_116_, new int[] {108,105,115,116}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__List_BasicType__lit_list_, tmp);
	}
    protected static final void _init_prod__Tuple_BasicType__lit_tuple_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(2650, 0, prod__lit_tuple__char_class___range__116_116_char_class___range__117_117_char_class___range__112_112_char_class___range__108_108_char_class___range__101_101_, new int[] {116,117,112,108,101}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Tuple_BasicType__lit_tuple_, tmp);
	}
    protected static final void _init_prod__Map_BasicType__lit_map_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(2652, 0, prod__lit_map__char_class___range__109_109_char_class___range__97_97_char_class___range__112_112_, new int[] {109,97,112}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Map_BasicType__lit_map_, tmp);
	}
    protected static final void _init_prod__Int_BasicType__lit_int_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(2654, 0, prod__lit_int__char_class___range__105_105_char_class___range__110_110_char_class___range__116_116_, new int[] {105,110,116}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Int_BasicType__lit_int_, tmp);
	}
    protected static final void _init_prod__Node_BasicType__lit_node_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(2656, 0, prod__lit_node__char_class___range__110_110_char_class___range__111_111_char_class___range__100_100_char_class___range__101_101_, new int[] {110,111,100,101}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Node_BasicType__lit_node_, tmp);
	}
    protected static final void _init_prod__Real_BasicType__lit_real_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(2658, 0, prod__lit_real__char_class___range__114_114_char_class___range__101_101_char_class___range__97_97_char_class___range__108_108_, new int[] {114,101,97,108}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Real_BasicType__lit_real_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__Rational_BasicType__lit_rat_(builder);
      
        _init_prod__Bag_BasicType__lit_bag_(builder);
      
        _init_prod__DateTime_BasicType__lit_datetime_(builder);
      
        _init_prod__String_BasicType__lit_str_(builder);
      
        _init_prod__Set_BasicType__lit_set_(builder);
      
        _init_prod__Type_BasicType__lit_type_(builder);
      
        _init_prod__Bool_BasicType__lit_bool_(builder);
      
        _init_prod__Relation_BasicType__lit_rel_(builder);
      
        _init_prod__Void_BasicType__lit_void_(builder);
      
        _init_prod__Value_BasicType__lit_value_(builder);
      
        _init_prod__Loc_BasicType__lit_loc_(builder);
      
        _init_prod__Num_BasicType__lit_num_(builder);
      
        _init_prod__List_BasicType__lit_list_(builder);
      
        _init_prod__Tuple_BasicType__lit_tuple_(builder);
      
        _init_prod__Map_BasicType__lit_map_(builder);
      
        _init_prod__Int_BasicType__lit_int_(builder);
      
        _init_prod__Node_BasicType__lit_node_(builder);
      
        _init_prod__Real_BasicType__lit_real_(builder);
      
    }
  }
	
  protected static class DateTimeLiteral {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__DateAndTimeLiteral_DateTimeLiteral__dateAndTime_DateAndTime_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(2668, 0, "DateAndTime", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__DateAndTimeLiteral_DateTimeLiteral__dateAndTime_DateAndTime_, tmp);
	}
    protected static final void _init_prod__TimeLiteral_DateTimeLiteral__time_JustTime_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(2670, 0, "JustTime", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__TimeLiteral_DateTimeLiteral__time_JustTime_, tmp);
	}
    protected static final void _init_prod__DateLiteral_DateTimeLiteral__date_JustDate_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(2672, 0, "JustDate", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__DateLiteral_DateTimeLiteral__date_JustDate_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__DateAndTimeLiteral_DateTimeLiteral__dateAndTime_DateAndTime_(builder);
      
        _init_prod__TimeLiteral_DateTimeLiteral__time_JustTime_(builder);
      
        _init_prod__DateLiteral_DateTimeLiteral__date_JustDate_(builder);
      
    }
  }
	
  protected static class PathChars {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__PathChars__URLChars_char_class___range__124_124_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[2];
      
      tmp[1] = new CharStackNode<IConstructor>(2692, 1, new int[][]{{124,124}}, null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(2690, 0, "URLChars", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__PathChars__URLChars_char_class___range__124_124_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__PathChars__URLChars_char_class___range__124_124_(builder);
      
    }
  }
	
  protected static class start__EvalCommand {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__start__EvalCommand__layouts_LAYOUTLIST_top_EvalCommand_layouts_LAYOUTLIST_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode<IConstructor>(2792, 2, "layouts_LAYOUTLIST", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(2790, 1, "EvalCommand", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(2788, 0, "layouts_LAYOUTLIST", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__start__EvalCommand__layouts_LAYOUTLIST_top_EvalCommand_layouts_LAYOUTLIST_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__start__EvalCommand__layouts_LAYOUTLIST_top_EvalCommand_layouts_LAYOUTLIST_(builder);
      
    }
  }
	
  protected static class ModuleActuals {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__Default_ModuleActuals__lit___91_layouts_LAYOUTLIST_types_iter_seps__Type__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___93_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new LiteralStackNode<IConstructor>(2786, 4, prod__lit___93__char_class___range__93_93_, new int[] {93}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(2784, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new SeparatedListStackNode<IConstructor>(2774, 2, regular__iter_seps__Type__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(2776, 0, "Type", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(2778, 1, "layouts_LAYOUTLIST", null, null), new LiteralStackNode<IConstructor>(2780, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode<IConstructor>(2782, 3, "layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(2772, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(2770, 0, prod__lit___91__char_class___range__91_91_, new int[] {91}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Default_ModuleActuals__lit___91_layouts_LAYOUTLIST_types_iter_seps__Type__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___93_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__Default_ModuleActuals__lit___91_layouts_LAYOUTLIST_types_iter_seps__Type__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___93_(builder);
      
    }
  }
	
  protected static class PostStringChars {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__PostStringChars__char_class___range__62_62_iter_star__StringCharacter_char_class___range__34_34__tag__category___67_111_110_115_116_97_110_116(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new CharStackNode<IConstructor>(2804, 2, new int[][]{{34,34}}, null, null);
      tmp[1] = new ListStackNode<IConstructor>(2800, 1, regular__iter_star__StringCharacter, new NonTerminalStackNode<IConstructor>(2802, 0, "StringCharacter", null, null), false, null, null);
      tmp[0] = new CharStackNode<IConstructor>(2798, 0, new int[][]{{62,62}}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__PostStringChars__char_class___range__62_62_iter_star__StringCharacter_char_class___range__34_34__tag__category___67_111_110_115_116_97_110_116, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__PostStringChars__char_class___range__62_62_iter_star__StringCharacter_char_class___range__34_34__tag__category___67_111_110_115_116_97_110_116(builder);
      
    }
  }
	
  protected static class HexIntegerLiteral {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__HexIntegerLiteral__char_class___range__48_48_char_class___range__88_88_range__120_120_conditional__iter__char_class___range__48_57_range__65_70_range__97_102__not_follow__char_class___range__48_57_range__65_90_range__95_95_range__97_122_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new ListStackNode<IConstructor>(2810, 2, regular__iter__char_class___range__48_57_range__65_70_range__97_102, new CharStackNode<IConstructor>(2812, 0, new int[][]{{48,57},{65,70},{97,102}}, null, null), true, null, new ICompletionFilter[] {new CharFollowRestriction(new int[][]{{48,57},{65,90},{95,95},{97,122}})});
      tmp[1] = new CharStackNode<IConstructor>(2808, 1, new int[][]{{88,88},{120,120}}, null, null);
      tmp[0] = new CharStackNode<IConstructor>(2806, 0, new int[][]{{48,48}}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__HexIntegerLiteral__char_class___range__48_48_char_class___range__88_88_range__120_120_conditional__iter__char_class___range__48_57_range__65_70_range__97_102__not_follow__char_class___range__48_57_range__65_90_range__95_95_range__97_122_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__HexIntegerLiteral__char_class___range__48_48_char_class___range__88_88_range__120_120_conditional__iter__char_class___range__48_57_range__65_70_range__97_102__not_follow__char_class___range__48_57_range__65_90_range__95_95_range__97_122_(builder);
      
    }
  }
	
  protected static class RegExpLiteral {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__RegExpLiteral__lit___47_iter_star__RegExp_lit___47_RegExpModifier_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[4];
      
      tmp[3] = new NonTerminalStackNode<IConstructor>(2822, 3, "RegExpModifier", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(2820, 2, prod__lit___47__char_class___range__47_47_, new int[] {47}, null, null);
      tmp[1] = new ListStackNode<IConstructor>(2816, 1, regular__iter_star__RegExp, new NonTerminalStackNode<IConstructor>(2818, 0, "RegExp", null, null), false, null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(2814, 0, prod__lit___47__char_class___range__47_47_, new int[] {47}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RegExpLiteral__lit___47_iter_star__RegExp_lit___47_RegExpModifier_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__RegExpLiteral__lit___47_iter_star__RegExp_lit___47_RegExpModifier_(builder);
      
    }
  }
	
  protected static class NamedRegExp {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__NamedRegExp__lit___60_Name_lit___62_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new LiteralStackNode<IConstructor>(2828, 2, prod__lit___62__char_class___range__62_62_, new int[] {62}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(2826, 1, "Name", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(2824, 0, prod__lit___60__char_class___range__60_60_, new int[] {60}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__NamedRegExp__lit___60_Name_lit___62_, tmp);
	}
    protected static final void _init_prod__NamedRegExp__char_class___range__0_46_range__48_59_range__61_61_range__63_91_range__93_16777215_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new CharStackNode<IConstructor>(2830, 0, new int[][]{{0,46},{48,59},{61,61},{63,91},{93,16777215}}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__NamedRegExp__char_class___range__0_46_range__48_59_range__61_61_range__63_91_range__93_16777215_, tmp);
	}
    protected static final void _init_prod__NamedRegExp__char_class___range__92_92_char_class___range__47_47_range__60_60_range__62_62_range__92_92_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[2];
      
      tmp[1] = new CharStackNode<IConstructor>(2834, 1, new int[][]{{47,47},{60,60},{62,62},{92,92}}, null, null);
      tmp[0] = new CharStackNode<IConstructor>(2832, 0, new int[][]{{92,92}}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__NamedRegExp__char_class___range__92_92_char_class___range__47_47_range__60_60_range__62_62_range__92_92_, tmp);
	}
    protected static final void _init_prod__NamedRegExp__NamedBackslash_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(2836, 0, "NamedBackslash", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__NamedRegExp__NamedBackslash_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__NamedRegExp__lit___60_Name_lit___62_(builder);
      
        _init_prod__NamedRegExp__char_class___range__0_46_range__48_59_range__61_61_range__63_91_range__93_16777215_(builder);
      
        _init_prod__NamedRegExp__char_class___range__92_92_char_class___range__47_47_range__60_60_range__62_62_range__92_92_(builder);
      
        _init_prod__NamedRegExp__NamedBackslash_(builder);
      
    }
  }
	
  protected static class ModuleParameters {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__Default_ModuleParameters__lit___91_layouts_LAYOUTLIST_parameters_iter_seps__TypeVar__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___93_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new LiteralStackNode<IConstructor>(2860, 4, prod__lit___93__char_class___range__93_93_, new int[] {93}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(2858, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new SeparatedListStackNode<IConstructor>(2848, 2, regular__iter_seps__TypeVar__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(2850, 0, "TypeVar", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(2852, 1, "layouts_LAYOUTLIST", null, null), new LiteralStackNode<IConstructor>(2854, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode<IConstructor>(2856, 3, "layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(2846, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(2844, 0, prod__lit___91__char_class___range__91_91_, new int[] {91}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Default_ModuleParameters__lit___91_layouts_LAYOUTLIST_parameters_iter_seps__TypeVar__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___93_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__Default_ModuleParameters__lit___91_layouts_LAYOUTLIST_parameters_iter_seps__TypeVar__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___93_(builder);
      
    }
  }
	
  protected static class RascalKeywords {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__RascalKeywords__lit_tuple_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(2876, 0, prod__lit_tuple__char_class___range__116_116_char_class___range__117_117_char_class___range__112_112_char_class___range__108_108_char_class___range__101_101_, new int[] {116,117,112,108,101}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RascalKeywords__lit_tuple_, tmp);
	}
    protected static final void _init_prod__RascalKeywords__lit_int_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(2878, 0, prod__lit_int__char_class___range__105_105_char_class___range__110_110_char_class___range__116_116_, new int[] {105,110,116}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RascalKeywords__lit_int_, tmp);
	}
    protected static final void _init_prod__RascalKeywords__lit_fail_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(2882, 0, prod__lit_fail__char_class___range__102_102_char_class___range__97_97_char_class___range__105_105_char_class___range__108_108_, new int[] {102,97,105,108}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RascalKeywords__lit_fail_, tmp);
	}
    protected static final void _init_prod__RascalKeywords__lit_mod_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(2880, 0, prod__lit_mod__char_class___range__109_109_char_class___range__111_111_char_class___range__100_100_, new int[] {109,111,100}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RascalKeywords__lit_mod_, tmp);
	}
    protected static final void _init_prod__RascalKeywords__lit_switch_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(2884, 0, prod__lit_switch__char_class___range__115_115_char_class___range__119_119_char_class___range__105_105_char_class___range__116_116_char_class___range__99_99_char_class___range__104_104_, new int[] {115,119,105,116,99,104}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RascalKeywords__lit_switch_, tmp);
	}
    protected static final void _init_prod__RascalKeywords__lit_throw_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(2886, 0, prod__lit_throw__char_class___range__116_116_char_class___range__104_104_char_class___range__114_114_char_class___range__111_111_char_class___range__119_119_, new int[] {116,104,114,111,119}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RascalKeywords__lit_throw_, tmp);
	}
    protected static final void _init_prod__RascalKeywords__lit_open_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(2888, 0, prod__lit_open__char_class___range__111_111_char_class___range__112_112_char_class___range__101_101_char_class___range__110_110_, new int[] {111,112,101,110}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RascalKeywords__lit_open_, tmp);
	}
    protected static final void _init_prod__RascalKeywords__lit_alias_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(2890, 0, prod__lit_alias__char_class___range__97_97_char_class___range__108_108_char_class___range__105_105_char_class___range__97_97_char_class___range__115_115_, new int[] {97,108,105,97,115}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RascalKeywords__lit_alias_, tmp);
	}
    protected static final void _init_prod__RascalKeywords__lit_default_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(2892, 0, prod__lit_default__char_class___range__100_100_char_class___range__101_101_char_class___range__102_102_char_class___range__97_97_char_class___range__117_117_char_class___range__108_108_char_class___range__116_116_, new int[] {100,101,102,97,117,108,116}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RascalKeywords__lit_default_, tmp);
	}
    protected static final void _init_prod__RascalKeywords__lit_throws_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(2894, 0, prod__lit_throws__char_class___range__116_116_char_class___range__104_104_char_class___range__114_114_char_class___range__111_111_char_class___range__119_119_char_class___range__115_115_, new int[] {116,104,114,111,119,115}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RascalKeywords__lit_throws_, tmp);
	}
    protected static final void _init_prod__RascalKeywords__lit_module_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(2896, 0, prod__lit_module__char_class___range__109_109_char_class___range__111_111_char_class___range__100_100_char_class___range__117_117_char_class___range__108_108_char_class___range__101_101_, new int[] {109,111,100,117,108,101}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RascalKeywords__lit_module_, tmp);
	}
    protected static final void _init_prod__RascalKeywords__lit_openrec_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(2898, 0, prod__lit_openrec__char_class___range__111_111_char_class___range__112_112_char_class___range__101_101_char_class___range__110_110_char_class___range__114_114_char_class___range__101_101_char_class___range__99_99_, new int[] {111,112,101,110,114,101,99}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RascalKeywords__lit_openrec_, tmp);
	}
    protected static final void _init_prod__RascalKeywords__lit_private_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(2900, 0, prod__lit_private__char_class___range__112_112_char_class___range__114_114_char_class___range__105_105_char_class___range__118_118_char_class___range__97_97_char_class___range__116_116_char_class___range__101_101_, new int[] {112,114,105,118,97,116,101}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RascalKeywords__lit_private_, tmp);
	}
    protected static final void _init_prod__RascalKeywords__lit_true_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(2902, 0, prod__lit_true__char_class___range__116_116_char_class___range__114_114_char_class___range__117_117_char_class___range__101_101_, new int[] {116,114,117,101}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RascalKeywords__lit_true_, tmp);
	}
    protected static final void _init_prod__RascalKeywords__lit_map_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(2904, 0, prod__lit_map__char_class___range__109_109_char_class___range__97_97_char_class___range__112_112_, new int[] {109,97,112}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RascalKeywords__lit_map_, tmp);
	}
    protected static final void _init_prod__RascalKeywords__lit_test_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(2906, 0, prod__lit_test__char_class___range__116_116_char_class___range__101_101_char_class___range__115_115_char_class___range__116_116_, new int[] {116,101,115,116}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RascalKeywords__lit_test_, tmp);
	}
    protected static final void _init_prod__RascalKeywords__lit_start_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(2908, 0, prod__lit_start__char_class___range__115_115_char_class___range__116_116_char_class___range__97_97_char_class___range__114_114_char_class___range__116_116_, new int[] {115,116,97,114,116}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RascalKeywords__lit_start_, tmp);
	}
    protected static final void _init_prod__RascalKeywords__lit_import_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(2910, 0, prod__lit_import__char_class___range__105_105_char_class___range__109_109_char_class___range__112_112_char_class___range__111_111_char_class___range__114_114_char_class___range__116_116_, new int[] {105,109,112,111,114,116}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RascalKeywords__lit_import_, tmp);
	}
    protected static final void _init_prod__RascalKeywords__lit_loc_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(2912, 0, prod__lit_loc__char_class___range__108_108_char_class___range__111_111_char_class___range__99_99_, new int[] {108,111,99}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RascalKeywords__lit_loc_, tmp);
	}
    protected static final void _init_prod__RascalKeywords__lit_assert_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(2914, 0, prod__lit_assert__char_class___range__97_97_char_class___range__115_115_char_class___range__115_115_char_class___range__101_101_char_class___range__114_114_char_class___range__116_116_, new int[] {97,115,115,101,114,116}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RascalKeywords__lit_assert_, tmp);
	}
    protected static final void _init_prod__RascalKeywords__lit_insert_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(2916, 0, prod__lit_insert__char_class___range__105_105_char_class___range__110_110_char_class___range__115_115_char_class___range__101_101_char_class___range__114_114_char_class___range__116_116_, new int[] {105,110,115,101,114,116}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RascalKeywords__lit_insert_, tmp);
	}
    protected static final void _init_prod__RascalKeywords__lit_anno_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(2918, 0, prod__lit_anno__char_class___range__97_97_char_class___range__110_110_char_class___range__110_110_char_class___range__111_111_, new int[] {97,110,110,111}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RascalKeywords__lit_anno_, tmp);
	}
    protected static final void _init_prod__RascalKeywords__lit_public_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(2920, 0, prod__lit_public__char_class___range__112_112_char_class___range__117_117_char_class___range__98_98_char_class___range__108_108_char_class___range__105_105_char_class___range__99_99_, new int[] {112,117,98,108,105,99}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RascalKeywords__lit_public_, tmp);
	}
    protected static final void _init_prod__RascalKeywords__lit_void_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(2922, 0, prod__lit_void__char_class___range__118_118_char_class___range__111_111_char_class___range__105_105_char_class___range__100_100_, new int[] {118,111,105,100}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RascalKeywords__lit_void_, tmp);
	}
    protected static final void _init_prod__RascalKeywords__lit_try_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(2924, 0, prod__lit_try__char_class___range__116_116_char_class___range__114_114_char_class___range__121_121_, new int[] {116,114,121}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RascalKeywords__lit_try_, tmp);
	}
    protected static final void _init_prod__RascalKeywords__lit_value_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(2926, 0, prod__lit_value__char_class___range__118_118_char_class___range__97_97_char_class___range__108_108_char_class___range__117_117_char_class___range__101_101_, new int[] {118,97,108,117,101}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RascalKeywords__lit_value_, tmp);
	}
    protected static final void _init_prod__RascalKeywords__lit_list_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(2928, 0, prod__lit_list__char_class___range__108_108_char_class___range__105_105_char_class___range__115_115_char_class___range__116_116_, new int[] {108,105,115,116}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RascalKeywords__lit_list_, tmp);
	}
    protected static final void _init_prod__RascalKeywords__lit_dynamic_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(2930, 0, prod__lit_dynamic__char_class___range__100_100_char_class___range__121_121_char_class___range__110_110_char_class___range__97_97_char_class___range__109_109_char_class___range__105_105_char_class___range__99_99_, new int[] {100,121,110,97,109,105,99}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RascalKeywords__lit_dynamic_, tmp);
	}
    protected static final void _init_prod__RascalKeywords__lit_tag_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(2932, 0, prod__lit_tag__char_class___range__116_116_char_class___range__97_97_char_class___range__103_103_, new int[] {116,97,103}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RascalKeywords__lit_tag_, tmp);
	}
    protected static final void _init_prod__RascalKeywords__lit_data_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(2934, 0, prod__lit_data__char_class___range__100_100_char_class___range__97_97_char_class___range__116_116_char_class___range__97_97_, new int[] {100,97,116,97}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RascalKeywords__lit_data_, tmp);
	}
    protected static final void _init_prod__RascalKeywords__lit_self_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(2936, 0, prod__lit_self__char_class___range__115_115_char_class___range__101_101_char_class___range__108_108_char_class___range__102_102_, new int[] {115,101,108,102}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RascalKeywords__lit_self_, tmp);
	}
    protected static final void _init_prod__RascalKeywords__lit_append_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(2940, 0, prod__lit_append__char_class___range__97_97_char_class___range__112_112_char_class___range__112_112_char_class___range__101_101_char_class___range__110_110_char_class___range__100_100_, new int[] {97,112,112,101,110,100}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RascalKeywords__lit_append_, tmp);
	}
    protected static final void _init_prod__RascalKeywords__lit_extend_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(2938, 0, prod__lit_extend__char_class___range__101_101_char_class___range__120_120_char_class___range__116_116_char_class___range__101_101_char_class___range__110_110_char_class___range__100_100_, new int[] {101,120,116,101,110,100}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RascalKeywords__lit_extend_, tmp);
	}
    protected static final void _init_prod__RascalKeywords__lit_type_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(2944, 0, prod__lit_type__char_class___range__116_116_char_class___range__121_121_char_class___range__112_112_char_class___range__101_101_, new int[] {116,121,112,101}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RascalKeywords__lit_type_, tmp);
	}
    protected static final void _init_prod__RascalKeywords__lit_notin_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(2942, 0, prod__lit_notin__char_class___range__110_110_char_class___range__111_111_char_class___range__116_116_char_class___range__105_105_char_class___range__110_110_, new int[] {110,111,116,105,110}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RascalKeywords__lit_notin_, tmp);
	}
    protected static final void _init_prod__RascalKeywords__lit_catch_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(2946, 0, prod__lit_catch__char_class___range__99_99_char_class___range__97_97_char_class___range__116_116_char_class___range__99_99_char_class___range__104_104_, new int[] {99,97,116,99,104}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RascalKeywords__lit_catch_, tmp);
	}
    protected static final void _init_prod__RascalKeywords__lit_one_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(2948, 0, prod__lit_one__char_class___range__111_111_char_class___range__110_110_char_class___range__101_101_, new int[] {111,110,101}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RascalKeywords__lit_one_, tmp);
	}
    protected static final void _init_prod__RascalKeywords__lit_node_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(2950, 0, prod__lit_node__char_class___range__110_110_char_class___range__111_111_char_class___range__100_100_char_class___range__101_101_, new int[] {110,111,100,101}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RascalKeywords__lit_node_, tmp);
	}
    protected static final void _init_prod__RascalKeywords__lit_str_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(2952, 0, prod__lit_str__char_class___range__115_115_char_class___range__116_116_char_class___range__114_114_, new int[] {115,116,114}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RascalKeywords__lit_str_, tmp);
	}
    protected static final void _init_prod__RascalKeywords__lit_visit_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(2954, 0, prod__lit_visit__char_class___range__118_118_char_class___range__105_105_char_class___range__115_115_char_class___range__105_105_char_class___range__116_116_, new int[] {118,105,115,105,116}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RascalKeywords__lit_visit_, tmp);
	}
    protected static final void _init_prod__RascalKeywords__lit_if_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(2958, 0, prod__lit_if__char_class___range__105_105_char_class___range__102_102_, new int[] {105,102}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RascalKeywords__lit_if_, tmp);
	}
    protected static final void _init_prod__RascalKeywords__lit_non_assoc_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(2956, 0, prod__lit_non_assoc__char_class___range__110_110_char_class___range__111_111_char_class___range__110_110_char_class___range__45_45_char_class___range__97_97_char_class___range__115_115_char_class___range__115_115_char_class___range__111_111_char_class___range__99_99_, new int[] {110,111,110,45,97,115,115,111,99}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RascalKeywords__lit_non_assoc_, tmp);
	}
    protected static final void _init_prod__RascalKeywords__lit_return_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(2960, 0, prod__lit_return__char_class___range__114_114_char_class___range__101_101_char_class___range__116_116_char_class___range__117_117_char_class___range__114_114_char_class___range__110_110_, new int[] {114,101,116,117,114,110}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RascalKeywords__lit_return_, tmp);
	}
    protected static final void _init_prod__RascalKeywords__lit_else_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(2962, 0, prod__lit_else__char_class___range__101_101_char_class___range__108_108_char_class___range__115_115_char_class___range__101_101_, new int[] {101,108,115,101}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RascalKeywords__lit_else_, tmp);
	}
    protected static final void _init_prod__RascalKeywords__lit_join_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(2968, 0, prod__lit_join__char_class___range__106_106_char_class___range__111_111_char_class___range__105_105_char_class___range__110_110_, new int[] {106,111,105,110}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RascalKeywords__lit_join_, tmp);
	}
    protected static final void _init_prod__RascalKeywords__lit_it_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(2966, 0, prod__lit_it__char_class___range__105_105_char_class___range__116_116_, new int[] {105,116}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RascalKeywords__lit_it_, tmp);
	}
    protected static final void _init_prod__RascalKeywords__lit_in_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(2964, 0, prod__lit_in__char_class___range__105_105_char_class___range__110_110_, new int[] {105,110}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RascalKeywords__lit_in_, tmp);
	}
    protected static final void _init_prod__RascalKeywords__lit_for_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(2970, 0, prod__lit_for__char_class___range__102_102_char_class___range__111_111_char_class___range__114_114_, new int[] {102,111,114}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RascalKeywords__lit_for_, tmp);
	}
    protected static final void _init_prod__RascalKeywords__lit_bracket_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(2972, 0, prod__lit_bracket__char_class___range__98_98_char_class___range__114_114_char_class___range__97_97_char_class___range__99_99_char_class___range__107_107_char_class___range__101_101_char_class___range__116_116_, new int[] {98,114,97,99,107,101,116}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RascalKeywords__lit_bracket_, tmp);
	}
    protected static final void _init_prod__RascalKeywords__lit_continue_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(2974, 0, prod__lit_continue__char_class___range__99_99_char_class___range__111_111_char_class___range__110_110_char_class___range__116_116_char_class___range__105_105_char_class___range__110_110_char_class___range__117_117_char_class___range__101_101_, new int[] {99,111,110,116,105,110,117,101}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RascalKeywords__lit_continue_, tmp);
	}
    protected static final void _init_prod__RascalKeywords__lit_set_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(2976, 0, prod__lit_set__char_class___range__115_115_char_class___range__101_101_char_class___range__116_116_, new int[] {115,101,116}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RascalKeywords__lit_set_, tmp);
	}
    protected static final void _init_prod__RascalKeywords__lit_assoc_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(2978, 0, prod__lit_assoc__char_class___range__97_97_char_class___range__115_115_char_class___range__115_115_char_class___range__111_111_char_class___range__99_99_, new int[] {97,115,115,111,99}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RascalKeywords__lit_assoc_, tmp);
	}
    protected static final void _init_prod__RascalKeywords__lit_bag_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(2980, 0, prod__lit_bag__char_class___range__98_98_char_class___range__97_97_char_class___range__103_103_, new int[] {98,97,103}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RascalKeywords__lit_bag_, tmp);
	}
    protected static final void _init_prod__RascalKeywords__lit_num_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(2982, 0, prod__lit_num__char_class___range__110_110_char_class___range__117_117_char_class___range__109_109_, new int[] {110,117,109}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RascalKeywords__lit_num_, tmp);
	}
    protected static final void _init_prod__RascalKeywords__lit_datetime_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(2984, 0, prod__lit_datetime__char_class___range__100_100_char_class___range__97_97_char_class___range__116_116_char_class___range__101_101_char_class___range__116_116_char_class___range__105_105_char_class___range__109_109_char_class___range__101_101_, new int[] {100,97,116,101,116,105,109,101}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RascalKeywords__lit_datetime_, tmp);
	}
    protected static final void _init_prod__RascalKeywords__lit_filter_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(2986, 0, prod__lit_filter__char_class___range__102_102_char_class___range__105_105_char_class___range__108_108_char_class___range__116_116_char_class___range__101_101_char_class___range__114_114_, new int[] {102,105,108,116,101,114}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RascalKeywords__lit_filter_, tmp);
	}
    protected static final void _init_prod__RascalKeywords__lit_layout_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(2992, 0, prod__lit_layout__char_class___range__108_108_char_class___range__97_97_char_class___range__121_121_char_class___range__111_111_char_class___range__117_117_char_class___range__116_116_, new int[] {108,97,121,111,117,116}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RascalKeywords__lit_layout_, tmp);
	}
    protected static final void _init_prod__RascalKeywords__lit_case_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(2990, 0, prod__lit_case__char_class___range__99_99_char_class___range__97_97_char_class___range__115_115_char_class___range__101_101_, new int[] {99,97,115,101}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RascalKeywords__lit_case_, tmp);
	}
    protected static final void _init_prod__RascalKeywords__lit_while_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(2988, 0, prod__lit_while__char_class___range__119_119_char_class___range__104_104_char_class___range__105_105_char_class___range__108_108_char_class___range__101_101_, new int[] {119,104,105,108,101}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RascalKeywords__lit_while_, tmp);
	}
    protected static final void _init_prod__RascalKeywords__lit_bool_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(2994, 0, prod__lit_bool__char_class___range__98_98_char_class___range__111_111_char_class___range__111_111_char_class___range__108_108_, new int[] {98,111,111,108}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RascalKeywords__lit_bool_, tmp);
	}
    protected static final void _init_prod__RascalKeywords__lit_any_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(2996, 0, prod__lit_any__char_class___range__97_97_char_class___range__110_110_char_class___range__121_121_, new int[] {97,110,121}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RascalKeywords__lit_any_, tmp);
	}
    protected static final void _init_prod__RascalKeywords__lit_real_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(3000, 0, prod__lit_real__char_class___range__114_114_char_class___range__101_101_char_class___range__97_97_char_class___range__108_108_, new int[] {114,101,97,108}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RascalKeywords__lit_real_, tmp);
	}
    protected static final void _init_prod__RascalKeywords__lit_finally_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(2998, 0, prod__lit_finally__char_class___range__102_102_char_class___range__105_105_char_class___range__110_110_char_class___range__97_97_char_class___range__108_108_char_class___range__108_108_char_class___range__121_121_, new int[] {102,105,110,97,108,108,121}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RascalKeywords__lit_finally_, tmp);
	}
    protected static final void _init_prod__RascalKeywords__lit_all_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(3002, 0, prod__lit_all__char_class___range__97_97_char_class___range__108_108_char_class___range__108_108_, new int[] {97,108,108}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RascalKeywords__lit_all_, tmp);
	}
    protected static final void _init_prod__RascalKeywords__lit_false_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(3004, 0, prod__lit_false__char_class___range__102_102_char_class___range__97_97_char_class___range__108_108_char_class___range__115_115_char_class___range__101_101_, new int[] {102,97,108,115,101}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RascalKeywords__lit_false_, tmp);
	}
    protected static final void _init_prod__RascalKeywords__lit_break_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(3006, 0, prod__lit_break__char_class___range__98_98_char_class___range__114_114_char_class___range__101_101_char_class___range__97_97_char_class___range__107_107_, new int[] {98,114,101,97,107}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RascalKeywords__lit_break_, tmp);
	}
    protected static final void _init_prod__RascalKeywords__lit_rel_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(3008, 0, prod__lit_rel__char_class___range__114_114_char_class___range__101_101_char_class___range__108_108_, new int[] {114,101,108}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RascalKeywords__lit_rel_, tmp);
	}
    protected static final void _init_prod__RascalKeywords__BasicType_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(3010, 0, "BasicType", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RascalKeywords__BasicType_, tmp);
	}
    protected static final void _init_prod__RascalKeywords__lit_solve_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(3012, 0, prod__lit_solve__char_class___range__115_115_char_class___range__111_111_char_class___range__108_108_char_class___range__118_118_char_class___range__101_101_, new int[] {115,111,108,118,101}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RascalKeywords__lit_solve_, tmp);
	}
    protected static final void _init_prod__RascalKeywords__lit_rat_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(3014, 0, prod__lit_rat__char_class___range__114_114_char_class___range__97_97_char_class___range__116_116_, new int[] {114,97,116}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RascalKeywords__lit_rat_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__RascalKeywords__lit_tuple_(builder);
      
        _init_prod__RascalKeywords__lit_int_(builder);
      
        _init_prod__RascalKeywords__lit_fail_(builder);
      
        _init_prod__RascalKeywords__lit_mod_(builder);
      
        _init_prod__RascalKeywords__lit_switch_(builder);
      
        _init_prod__RascalKeywords__lit_throw_(builder);
      
        _init_prod__RascalKeywords__lit_open_(builder);
      
        _init_prod__RascalKeywords__lit_alias_(builder);
      
        _init_prod__RascalKeywords__lit_default_(builder);
      
        _init_prod__RascalKeywords__lit_throws_(builder);
      
        _init_prod__RascalKeywords__lit_module_(builder);
      
        _init_prod__RascalKeywords__lit_openrec_(builder);
      
        _init_prod__RascalKeywords__lit_private_(builder);
      
        _init_prod__RascalKeywords__lit_true_(builder);
      
        _init_prod__RascalKeywords__lit_map_(builder);
      
        _init_prod__RascalKeywords__lit_test_(builder);
      
        _init_prod__RascalKeywords__lit_start_(builder);
      
        _init_prod__RascalKeywords__lit_import_(builder);
      
        _init_prod__RascalKeywords__lit_loc_(builder);
      
        _init_prod__RascalKeywords__lit_assert_(builder);
      
        _init_prod__RascalKeywords__lit_insert_(builder);
      
        _init_prod__RascalKeywords__lit_anno_(builder);
      
        _init_prod__RascalKeywords__lit_public_(builder);
      
        _init_prod__RascalKeywords__lit_void_(builder);
      
        _init_prod__RascalKeywords__lit_try_(builder);
      
        _init_prod__RascalKeywords__lit_value_(builder);
      
        _init_prod__RascalKeywords__lit_list_(builder);
      
        _init_prod__RascalKeywords__lit_dynamic_(builder);
      
        _init_prod__RascalKeywords__lit_tag_(builder);
      
        _init_prod__RascalKeywords__lit_data_(builder);
      
        _init_prod__RascalKeywords__lit_self_(builder);
      
        _init_prod__RascalKeywords__lit_append_(builder);
      
        _init_prod__RascalKeywords__lit_extend_(builder);
      
        _init_prod__RascalKeywords__lit_type_(builder);
      
        _init_prod__RascalKeywords__lit_notin_(builder);
      
        _init_prod__RascalKeywords__lit_catch_(builder);
      
        _init_prod__RascalKeywords__lit_one_(builder);
      
        _init_prod__RascalKeywords__lit_node_(builder);
      
        _init_prod__RascalKeywords__lit_str_(builder);
      
        _init_prod__RascalKeywords__lit_visit_(builder);
      
        _init_prod__RascalKeywords__lit_if_(builder);
      
        _init_prod__RascalKeywords__lit_non_assoc_(builder);
      
        _init_prod__RascalKeywords__lit_return_(builder);
      
        _init_prod__RascalKeywords__lit_else_(builder);
      
        _init_prod__RascalKeywords__lit_join_(builder);
      
        _init_prod__RascalKeywords__lit_it_(builder);
      
        _init_prod__RascalKeywords__lit_in_(builder);
      
        _init_prod__RascalKeywords__lit_for_(builder);
      
        _init_prod__RascalKeywords__lit_bracket_(builder);
      
        _init_prod__RascalKeywords__lit_continue_(builder);
      
        _init_prod__RascalKeywords__lit_set_(builder);
      
        _init_prod__RascalKeywords__lit_assoc_(builder);
      
        _init_prod__RascalKeywords__lit_bag_(builder);
      
        _init_prod__RascalKeywords__lit_num_(builder);
      
        _init_prod__RascalKeywords__lit_datetime_(builder);
      
        _init_prod__RascalKeywords__lit_filter_(builder);
      
        _init_prod__RascalKeywords__lit_layout_(builder);
      
        _init_prod__RascalKeywords__lit_case_(builder);
      
        _init_prod__RascalKeywords__lit_while_(builder);
      
        _init_prod__RascalKeywords__lit_bool_(builder);
      
        _init_prod__RascalKeywords__lit_any_(builder);
      
        _init_prod__RascalKeywords__lit_real_(builder);
      
        _init_prod__RascalKeywords__lit_finally_(builder);
      
        _init_prod__RascalKeywords__lit_all_(builder);
      
        _init_prod__RascalKeywords__lit_false_(builder);
      
        _init_prod__RascalKeywords__lit_break_(builder);
      
        _init_prod__RascalKeywords__lit_rel_(builder);
      
        _init_prod__RascalKeywords__BasicType_(builder);
      
        _init_prod__RascalKeywords__lit_solve_(builder);
      
        _init_prod__RascalKeywords__lit_rat_(builder);
      
    }
  }
	
  protected static class Strategy {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__TopDown_Strategy__lit_top_down_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(3036, 0, prod__lit_top_down__char_class___range__116_116_char_class___range__111_111_char_class___range__112_112_char_class___range__45_45_char_class___range__100_100_char_class___range__111_111_char_class___range__119_119_char_class___range__110_110_, new int[] {116,111,112,45,100,111,119,110}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__TopDown_Strategy__lit_top_down_, tmp);
	}
    protected static final void _init_prod__TopDownBreak_Strategy__lit_top_down_break_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(3038, 0, prod__lit_top_down_break__char_class___range__116_116_char_class___range__111_111_char_class___range__112_112_char_class___range__45_45_char_class___range__100_100_char_class___range__111_111_char_class___range__119_119_char_class___range__110_110_char_class___range__45_45_char_class___range__98_98_char_class___range__114_114_char_class___range__101_101_char_class___range__97_97_char_class___range__107_107_, new int[] {116,111,112,45,100,111,119,110,45,98,114,101,97,107}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__TopDownBreak_Strategy__lit_top_down_break_, tmp);
	}
    protected static final void _init_prod__Innermost_Strategy__lit_innermost_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(3040, 0, prod__lit_innermost__char_class___range__105_105_char_class___range__110_110_char_class___range__110_110_char_class___range__101_101_char_class___range__114_114_char_class___range__109_109_char_class___range__111_111_char_class___range__115_115_char_class___range__116_116_, new int[] {105,110,110,101,114,109,111,115,116}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Innermost_Strategy__lit_innermost_, tmp);
	}
    protected static final void _init_prod__BottomUp_Strategy__lit_bottom_up_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(3042, 0, prod__lit_bottom_up__char_class___range__98_98_char_class___range__111_111_char_class___range__116_116_char_class___range__116_116_char_class___range__111_111_char_class___range__109_109_char_class___range__45_45_char_class___range__117_117_char_class___range__112_112_, new int[] {98,111,116,116,111,109,45,117,112}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__BottomUp_Strategy__lit_bottom_up_, tmp);
	}
    protected static final void _init_prod__Outermost_Strategy__lit_outermost_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(3044, 0, prod__lit_outermost__char_class___range__111_111_char_class___range__117_117_char_class___range__116_116_char_class___range__101_101_char_class___range__114_114_char_class___range__109_109_char_class___range__111_111_char_class___range__115_115_char_class___range__116_116_, new int[] {111,117,116,101,114,109,111,115,116}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Outermost_Strategy__lit_outermost_, tmp);
	}
    protected static final void _init_prod__BottomUpBreak_Strategy__lit_bottom_up_break_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(3046, 0, prod__lit_bottom_up_break__char_class___range__98_98_char_class___range__111_111_char_class___range__116_116_char_class___range__116_116_char_class___range__111_111_char_class___range__109_109_char_class___range__45_45_char_class___range__117_117_char_class___range__112_112_char_class___range__45_45_char_class___range__98_98_char_class___range__114_114_char_class___range__101_101_char_class___range__97_97_char_class___range__107_107_, new int[] {98,111,116,116,111,109,45,117,112,45,98,114,101,97,107}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__BottomUpBreak_Strategy__lit_bottom_up_break_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__TopDown_Strategy__lit_top_down_(builder);
      
        _init_prod__TopDownBreak_Strategy__lit_top_down_break_(builder);
      
        _init_prod__Innermost_Strategy__lit_innermost_(builder);
      
        _init_prod__BottomUp_Strategy__lit_bottom_up_(builder);
      
        _init_prod__Outermost_Strategy__lit_outermost_(builder);
      
        _init_prod__BottomUpBreak_Strategy__lit_bottom_up_break_(builder);
      
    }
  }
	
  protected static class Char {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__Char__lit___92_char_class___range__32_32_range__34_34_range__39_39_range__45_45_range__60_60_range__62_62_range__91_93_range__98_98_range__102_102_range__110_110_range__114_114_range__116_116__tag__category___67_111_110_115_116_97_110_116(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[2];
      
      tmp[1] = new CharStackNode<IConstructor>(3054, 1, new int[][]{{32,32},{34,34},{39,39},{45,45},{60,60},{62,62},{91,93},{98,98},{102,102},{110,110},{114,114},{116,116}}, null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(3052, 0, prod__lit___92__char_class___range__92_92_, new int[] {92}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Char__lit___92_char_class___range__32_32_range__34_34_range__39_39_range__45_45_range__60_60_range__62_62_range__91_93_range__98_98_range__102_102_range__110_110_range__114_114_range__116_116__tag__category___67_111_110_115_116_97_110_116, tmp);
	}
    protected static final void _init_prod__Char__char_class___range__0_31_range__33_33_range__35_38_range__40_44_range__46_59_range__61_61_range__63_90_range__94_16777215__tag__category___67_111_110_115_116_97_110_116(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new CharStackNode<IConstructor>(3056, 0, new int[][]{{0,31},{33,33},{35,38},{40,44},{46,59},{61,61},{63,90},{94,16777215}}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Char__char_class___range__0_31_range__33_33_range__35_38_range__40_44_range__46_59_range__61_61_range__63_90_range__94_16777215__tag__category___67_111_110_115_116_97_110_116, tmp);
	}
    protected static final void _init_prod__Char__UnicodeEscape__tag__category___67_111_110_115_116_97_110_116(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(3058, 0, "UnicodeEscape", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Char__UnicodeEscape__tag__category___67_111_110_115_116_97_110_116, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__Char__lit___92_char_class___range__32_32_range__34_34_range__39_39_range__45_45_range__60_60_range__62_62_range__91_93_range__98_98_range__102_102_range__110_110_range__114_114_range__116_116__tag__category___67_111_110_115_116_97_110_116(builder);
      
        _init_prod__Char__char_class___range__0_31_range__33_33_range__35_38_range__40_44_range__46_59_range__61_61_range__63_90_range__94_16777215__tag__category___67_111_110_115_116_97_110_116(builder);
      
        _init_prod__Char__UnicodeEscape__tag__category___67_111_110_115_116_97_110_116(builder);
      
    }
  }
	
  protected static class PrePathChars {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__PrePathChars__URLChars_lit___60_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[2];
      
      tmp[1] = new LiteralStackNode<IConstructor>(3072, 1, prod__lit___60__char_class___range__60_60_, new int[] {60}, null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(3070, 0, "URLChars", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__PrePathChars__URLChars_lit___60_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__PrePathChars__URLChars_lit___60_(builder);
      
    }
  }
	
  protected static class MidPathChars {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__MidPathChars__lit___62_URLChars_lit___60_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new LiteralStackNode<IConstructor>(3088, 2, prod__lit___60__char_class___range__60_60_, new int[] {60}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(3086, 1, "URLChars", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(3084, 0, prod__lit___62__char_class___range__62_62_, new int[] {62}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__MidPathChars__lit___62_URLChars_lit___60_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__MidPathChars__lit___62_URLChars_lit___60_(builder);
      
    }
  }
	
  protected static class PostProtocolChars {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__PostProtocolChars__lit___62_URLChars_lit___58_47_47_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new LiteralStackNode<IConstructor>(3148, 2, prod__lit___58_47_47__char_class___range__58_58_char_class___range__47_47_char_class___range__47_47_, new int[] {58,47,47}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(3146, 1, "URLChars", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(3144, 0, prod__lit___62__char_class___range__62_62_, new int[] {62}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__PostProtocolChars__lit___62_URLChars_lit___58_47_47_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__PostProtocolChars__lit___62_URLChars_lit___58_47_47_(builder);
      
    }
  }
	
  protected static class SyntaxDefinition {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__Keyword_SyntaxDefinition__lit_keyword_layouts_LAYOUTLIST_defined_Sym_layouts_LAYOUTLIST_lit___61_layouts_LAYOUTLIST_production_Prod_layouts_LAYOUTLIST_lit___59__tag__Foldable(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[9];
      
      tmp[8] = new LiteralStackNode<IConstructor>(3208, 8, prod__lit___59__char_class___range__59_59_, new int[] {59}, null, null);
      tmp[7] = new NonTerminalStackNode<IConstructor>(3206, 7, "layouts_LAYOUTLIST", null, null);
      tmp[6] = new NonTerminalStackNode<IConstructor>(3204, 6, "Prod", null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(3202, 5, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode<IConstructor>(3200, 4, prod__lit___61__char_class___range__61_61_, new int[] {61}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(3198, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(3196, 2, "Sym", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(3194, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(3192, 0, prod__lit_keyword__char_class___range__107_107_char_class___range__101_101_char_class___range__121_121_char_class___range__119_119_char_class___range__111_111_char_class___range__114_114_char_class___range__100_100_, new int[] {107,101,121,119,111,114,100}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Keyword_SyntaxDefinition__lit_keyword_layouts_LAYOUTLIST_defined_Sym_layouts_LAYOUTLIST_lit___61_layouts_LAYOUTLIST_production_Prod_layouts_LAYOUTLIST_lit___59__tag__Foldable, tmp);
	}
    protected static final void _init_prod__Language_SyntaxDefinition__start_Start_layouts_LAYOUTLIST_lit_syntax_layouts_LAYOUTLIST_defined_Sym_layouts_LAYOUTLIST_lit___61_layouts_LAYOUTLIST_production_Prod_layouts_LAYOUTLIST_lit___59__tag__Foldable(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[11];
      
      tmp[10] = new LiteralStackNode<IConstructor>(3230, 10, prod__lit___59__char_class___range__59_59_, new int[] {59}, null, null);
      tmp[9] = new NonTerminalStackNode<IConstructor>(3228, 9, "layouts_LAYOUTLIST", null, null);
      tmp[8] = new NonTerminalStackNode<IConstructor>(3226, 8, "Prod", null, null);
      tmp[7] = new NonTerminalStackNode<IConstructor>(3224, 7, "layouts_LAYOUTLIST", null, null);
      tmp[6] = new LiteralStackNode<IConstructor>(3222, 6, prod__lit___61__char_class___range__61_61_, new int[] {61}, null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(3220, 5, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new NonTerminalStackNode<IConstructor>(3218, 4, "Sym", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(3216, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(3214, 2, prod__lit_syntax__char_class___range__115_115_char_class___range__121_121_char_class___range__110_110_char_class___range__116_116_char_class___range__97_97_char_class___range__120_120_, new int[] {115,121,110,116,97,120}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(3212, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(3210, 0, "Start", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Language_SyntaxDefinition__start_Start_layouts_LAYOUTLIST_lit_syntax_layouts_LAYOUTLIST_defined_Sym_layouts_LAYOUTLIST_lit___61_layouts_LAYOUTLIST_production_Prod_layouts_LAYOUTLIST_lit___59__tag__Foldable, tmp);
	}
    protected static final void _init_prod__Lexical_SyntaxDefinition__lit_lexical_layouts_LAYOUTLIST_defined_Sym_layouts_LAYOUTLIST_lit___61_layouts_LAYOUTLIST_production_Prod_layouts_LAYOUTLIST_lit___59__tag__Foldable(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[9];
      
      tmp[8] = new LiteralStackNode<IConstructor>(3248, 8, prod__lit___59__char_class___range__59_59_, new int[] {59}, null, null);
      tmp[7] = new NonTerminalStackNode<IConstructor>(3246, 7, "layouts_LAYOUTLIST", null, null);
      tmp[6] = new NonTerminalStackNode<IConstructor>(3244, 6, "Prod", null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(3242, 5, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode<IConstructor>(3240, 4, prod__lit___61__char_class___range__61_61_, new int[] {61}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(3238, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(3236, 2, "Sym", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(3234, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(3232, 0, prod__lit_lexical__char_class___range__108_108_char_class___range__101_101_char_class___range__120_120_char_class___range__105_105_char_class___range__99_99_char_class___range__97_97_char_class___range__108_108_, new int[] {108,101,120,105,99,97,108}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Lexical_SyntaxDefinition__lit_lexical_layouts_LAYOUTLIST_defined_Sym_layouts_LAYOUTLIST_lit___61_layouts_LAYOUTLIST_production_Prod_layouts_LAYOUTLIST_lit___59__tag__Foldable, tmp);
	}
    protected static final void _init_prod__Layout_SyntaxDefinition__vis_Visibility_layouts_LAYOUTLIST_lit_layout_layouts_LAYOUTLIST_defined_Sym_layouts_LAYOUTLIST_lit___61_layouts_LAYOUTLIST_production_Prod_layouts_LAYOUTLIST_lit___59__tag__Foldable(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[11];
      
      tmp[10] = new LiteralStackNode<IConstructor>(3270, 10, prod__lit___59__char_class___range__59_59_, new int[] {59}, null, null);
      tmp[9] = new NonTerminalStackNode<IConstructor>(3268, 9, "layouts_LAYOUTLIST", null, null);
      tmp[8] = new NonTerminalStackNode<IConstructor>(3266, 8, "Prod", null, null);
      tmp[7] = new NonTerminalStackNode<IConstructor>(3264, 7, "layouts_LAYOUTLIST", null, null);
      tmp[6] = new LiteralStackNode<IConstructor>(3262, 6, prod__lit___61__char_class___range__61_61_, new int[] {61}, null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(3260, 5, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new NonTerminalStackNode<IConstructor>(3258, 4, "Sym", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(3256, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(3254, 2, prod__lit_layout__char_class___range__108_108_char_class___range__97_97_char_class___range__121_121_char_class___range__111_111_char_class___range__117_117_char_class___range__116_116_, new int[] {108,97,121,111,117,116}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(3252, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(3250, 0, "Visibility", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Layout_SyntaxDefinition__vis_Visibility_layouts_LAYOUTLIST_lit_layout_layouts_LAYOUTLIST_defined_Sym_layouts_LAYOUTLIST_lit___61_layouts_LAYOUTLIST_production_Prod_layouts_LAYOUTLIST_lit___59__tag__Foldable, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__Keyword_SyntaxDefinition__lit_keyword_layouts_LAYOUTLIST_defined_Sym_layouts_LAYOUTLIST_lit___61_layouts_LAYOUTLIST_production_Prod_layouts_LAYOUTLIST_lit___59__tag__Foldable(builder);
      
        _init_prod__Language_SyntaxDefinition__start_Start_layouts_LAYOUTLIST_lit_syntax_layouts_LAYOUTLIST_defined_Sym_layouts_LAYOUTLIST_lit___61_layouts_LAYOUTLIST_production_Prod_layouts_LAYOUTLIST_lit___59__tag__Foldable(builder);
      
        _init_prod__Lexical_SyntaxDefinition__lit_lexical_layouts_LAYOUTLIST_defined_Sym_layouts_LAYOUTLIST_lit___61_layouts_LAYOUTLIST_production_Prod_layouts_LAYOUTLIST_lit___59__tag__Foldable(builder);
      
        _init_prod__Layout_SyntaxDefinition__vis_Visibility_layouts_LAYOUTLIST_lit_layout_layouts_LAYOUTLIST_defined_Sym_layouts_LAYOUTLIST_lit___61_layouts_LAYOUTLIST_production_Prod_layouts_LAYOUTLIST_lit___59__tag__Foldable(builder);
      
    }
  }
	
  protected static class Kind {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__Alias_Kind__lit_alias_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(3272, 0, prod__lit_alias__char_class___range__97_97_char_class___range__108_108_char_class___range__105_105_char_class___range__97_97_char_class___range__115_115_, new int[] {97,108,105,97,115}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Alias_Kind__lit_alias_, tmp);
	}
    protected static final void _init_prod__View_Kind__lit_view_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(3274, 0, prod__lit_view__char_class___range__118_118_char_class___range__105_105_char_class___range__101_101_char_class___range__119_119_, new int[] {118,105,101,119}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__View_Kind__lit_view_, tmp);
	}
    protected static final void _init_prod__Function_Kind__lit_function_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(3276, 0, prod__lit_function__char_class___range__102_102_char_class___range__117_117_char_class___range__110_110_char_class___range__99_99_char_class___range__116_116_char_class___range__105_105_char_class___range__111_111_char_class___range__110_110_, new int[] {102,117,110,99,116,105,111,110}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Function_Kind__lit_function_, tmp);
	}
    protected static final void _init_prod__Tag_Kind__lit_tag_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(3278, 0, prod__lit_tag__char_class___range__116_116_char_class___range__97_97_char_class___range__103_103_, new int[] {116,97,103}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Tag_Kind__lit_tag_, tmp);
	}
    protected static final void _init_prod__Data_Kind__lit_data_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(3280, 0, prod__lit_data__char_class___range__100_100_char_class___range__97_97_char_class___range__116_116_char_class___range__97_97_, new int[] {100,97,116,97}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Data_Kind__lit_data_, tmp);
	}
    protected static final void _init_prod__Anno_Kind__lit_anno_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(3282, 0, prod__lit_anno__char_class___range__97_97_char_class___range__110_110_char_class___range__110_110_char_class___range__111_111_, new int[] {97,110,110,111}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Anno_Kind__lit_anno_, tmp);
	}
    protected static final void _init_prod__Variable_Kind__lit_variable_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(3284, 0, prod__lit_variable__char_class___range__118_118_char_class___range__97_97_char_class___range__114_114_char_class___range__105_105_char_class___range__97_97_char_class___range__98_98_char_class___range__108_108_char_class___range__101_101_, new int[] {118,97,114,105,97,98,108,101}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Variable_Kind__lit_variable_, tmp);
	}
    protected static final void _init_prod__Module_Kind__lit_module_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(3286, 0, prod__lit_module__char_class___range__109_109_char_class___range__111_111_char_class___range__100_100_char_class___range__117_117_char_class___range__108_108_char_class___range__101_101_, new int[] {109,111,100,117,108,101}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Module_Kind__lit_module_, tmp);
	}
    protected static final void _init_prod__All_Kind__lit_all_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(3288, 0, prod__lit_all__char_class___range__97_97_char_class___range__108_108_char_class___range__108_108_, new int[] {97,108,108}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__All_Kind__lit_all_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__Alias_Kind__lit_alias_(builder);
      
        _init_prod__View_Kind__lit_view_(builder);
      
        _init_prod__Function_Kind__lit_function_(builder);
      
        _init_prod__Tag_Kind__lit_tag_(builder);
      
        _init_prod__Data_Kind__lit_data_(builder);
      
        _init_prod__Anno_Kind__lit_anno_(builder);
      
        _init_prod__Variable_Kind__lit_variable_(builder);
      
        _init_prod__Module_Kind__lit_module_(builder);
      
        _init_prod__All_Kind__lit_all_(builder);
      
    }
  }
	
  protected static class IntegerLiteral {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__OctalIntegerLiteral_IntegerLiteral__octal_OctalIntegerLiteral_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(3314, 0, "OctalIntegerLiteral", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__OctalIntegerLiteral_IntegerLiteral__octal_OctalIntegerLiteral_, tmp);
	}
    protected static final void _init_prod__HexIntegerLiteral_IntegerLiteral__hex_HexIntegerLiteral_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(3316, 0, "HexIntegerLiteral", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__HexIntegerLiteral_IntegerLiteral__hex_HexIntegerLiteral_, tmp);
	}
    protected static final void _init_prod__DecimalIntegerLiteral_IntegerLiteral__decimal_DecimalIntegerLiteral_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(3318, 0, "DecimalIntegerLiteral", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__DecimalIntegerLiteral_IntegerLiteral__decimal_DecimalIntegerLiteral_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__OctalIntegerLiteral_IntegerLiteral__octal_OctalIntegerLiteral_(builder);
      
        _init_prod__HexIntegerLiteral_IntegerLiteral__hex_HexIntegerLiteral_(builder);
      
        _init_prod__DecimalIntegerLiteral_IntegerLiteral__decimal_DecimalIntegerLiteral_(builder);
      
    }
  }
	
  protected static class Target {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__Empty_Target__(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new EpsilonStackNode<IConstructor>(3310, 0);
      builder.addAlternative(ObjectRascalRascal.prod__Empty_Target__, tmp);
	}
    protected static final void _init_prod__Labeled_Target__name_Name_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(3312, 0, "Name", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Labeled_Target__name_Name_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__Empty_Target__(builder);
      
        _init_prod__Labeled_Target__name_Name_(builder);
      
    }
  }
	
  protected static class start__Command {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__start__Command__layouts_LAYOUTLIST_top_Command_layouts_LAYOUTLIST_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode<IConstructor>(3326, 2, "layouts_LAYOUTLIST", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(3324, 1, "Command", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(3322, 0, "layouts_LAYOUTLIST", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__start__Command__layouts_LAYOUTLIST_top_Command_layouts_LAYOUTLIST_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__start__Command__layouts_LAYOUTLIST_top_Command_layouts_LAYOUTLIST_(builder);
      
    }
  }
	
  protected static class FunctionBody {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__Default_FunctionBody__lit___123_layouts_LAYOUTLIST_statements_iter_star_seps__Statement__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new LiteralStackNode<IConstructor>(3340, 4, prod__lit___125__char_class___range__125_125_, new int[] {125}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(3338, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new SeparatedListStackNode<IConstructor>(3332, 2, regular__iter_star_seps__Statement__layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(3334, 0, "Statement", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(3336, 1, "layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(3330, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(3328, 0, prod__lit___123__char_class___range__123_123_, new int[] {123}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Default_FunctionBody__lit___123_layouts_LAYOUTLIST_statements_iter_star_seps__Statement__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__Default_FunctionBody__lit___123_layouts_LAYOUTLIST_statements_iter_star_seps__Statement__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_(builder);
      
    }
  }
	
  protected static class UserType {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__Name_UserType__name_QualifiedName_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(3382, 0, "QualifiedName", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Name_UserType__name_QualifiedName_, tmp);
	}
    protected static final void _init_prod__Parametric_UserType__conditional__name_QualifiedName__follow__lit___91_layouts_LAYOUTLIST_lit___91_layouts_LAYOUTLIST_parameters_iter_seps__Type__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___93_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[7];
      
      tmp[6] = new LiteralStackNode<IConstructor>(3404, 6, prod__lit___93__char_class___range__93_93_, new int[] {93}, null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(3402, 5, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new SeparatedListStackNode<IConstructor>(3392, 4, regular__iter_seps__Type__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(3394, 0, "Type", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(3396, 1, "layouts_LAYOUTLIST", null, null), new LiteralStackNode<IConstructor>(3398, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode<IConstructor>(3400, 3, "layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(3390, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(3388, 2, prod__lit___91__char_class___range__91_91_, new int[] {91}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(3386, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(3384, 0, "QualifiedName", null, new ICompletionFilter[] {new StringFollowRequirement(new int[] {91})});
      builder.addAlternative(ObjectRascalRascal.prod__Parametric_UserType__conditional__name_QualifiedName__follow__lit___91_layouts_LAYOUTLIST_lit___91_layouts_LAYOUTLIST_parameters_iter_seps__Type__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___93_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__Name_UserType__name_QualifiedName_(builder);
      
        _init_prod__Parametric_UserType__conditional__name_QualifiedName__follow__lit___91_layouts_LAYOUTLIST_lit___91_layouts_LAYOUTLIST_parameters_iter_seps__Type__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___93_(builder);
      
    }
  }
	
  protected static class Import {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__Extend_Import__lit_extend_layouts_LAYOUTLIST_module_ImportedModule_layouts_LAYOUTLIST_lit___59_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new LiteralStackNode<IConstructor>(3350, 4, prod__lit___59__char_class___range__59_59_, new int[] {59}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(3348, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(3346, 2, "ImportedModule", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(3344, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(3342, 0, prod__lit_extend__char_class___range__101_101_char_class___range__120_120_char_class___range__116_116_char_class___range__101_101_char_class___range__110_110_char_class___range__100_100_, new int[] {101,120,116,101,110,100}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Extend_Import__lit_extend_layouts_LAYOUTLIST_module_ImportedModule_layouts_LAYOUTLIST_lit___59_, tmp);
	}
    protected static final void _init_prod__Default_Import__lit_import_layouts_LAYOUTLIST_module_ImportedModule_layouts_LAYOUTLIST_lit___59_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new LiteralStackNode<IConstructor>(3360, 4, prod__lit___59__char_class___range__59_59_, new int[] {59}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(3358, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(3356, 2, "ImportedModule", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(3354, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(3352, 0, prod__lit_import__char_class___range__105_105_char_class___range__109_109_char_class___range__112_112_char_class___range__111_111_char_class___range__114_114_char_class___range__116_116_, new int[] {105,109,112,111,114,116}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Default_Import__lit_import_layouts_LAYOUTLIST_module_ImportedModule_layouts_LAYOUTLIST_lit___59_, tmp);
	}
    protected static final void _init_prod__Syntax_Import__syntax_SyntaxDefinition_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(3362, 0, "SyntaxDefinition", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Syntax_Import__syntax_SyntaxDefinition_, tmp);
	}
    protected static final void _init_prod__External_Import__lit_import_layouts_LAYOUTLIST_name_QualifiedName_layouts_LAYOUTLIST_lit___61_layouts_LAYOUTLIST_at_LocationLiteral_layouts_LAYOUTLIST_lit___59_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[9];
      
      tmp[8] = new LiteralStackNode<IConstructor>(3380, 8, prod__lit___59__char_class___range__59_59_, new int[] {59}, null, null);
      tmp[7] = new NonTerminalStackNode<IConstructor>(3378, 7, "layouts_LAYOUTLIST", null, null);
      tmp[6] = new NonTerminalStackNode<IConstructor>(3376, 6, "LocationLiteral", null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(3374, 5, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode<IConstructor>(3372, 4, prod__lit___61__char_class___range__61_61_, new int[] {61}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(3370, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(3368, 2, "QualifiedName", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(3366, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(3364, 0, prod__lit_import__char_class___range__105_105_char_class___range__109_109_char_class___range__112_112_char_class___range__111_111_char_class___range__114_114_char_class___range__116_116_, new int[] {105,109,112,111,114,116}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__External_Import__lit_import_layouts_LAYOUTLIST_name_QualifiedName_layouts_LAYOUTLIST_lit___61_layouts_LAYOUTLIST_at_LocationLiteral_layouts_LAYOUTLIST_lit___59_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__Extend_Import__lit_extend_layouts_LAYOUTLIST_module_ImportedModule_layouts_LAYOUTLIST_lit___59_(builder);
      
        _init_prod__Default_Import__lit_import_layouts_LAYOUTLIST_module_ImportedModule_layouts_LAYOUTLIST_lit___59_(builder);
      
        _init_prod__Syntax_Import__syntax_SyntaxDefinition_(builder);
      
        _init_prod__External_Import__lit_import_layouts_LAYOUTLIST_name_QualifiedName_layouts_LAYOUTLIST_lit___61_layouts_LAYOUTLIST_at_LocationLiteral_layouts_LAYOUTLIST_lit___59_(builder);
      
    }
  }
	
  protected static class Body {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__Toplevels_Body__toplevels_iter_star_seps__Toplevel__layouts_LAYOUTLIST_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new SeparatedListStackNode<IConstructor>(3410, 0, regular__iter_star_seps__Toplevel__layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(3412, 0, "Toplevel", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(3414, 1, "layouts_LAYOUTLIST", null, null)}, false, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Toplevels_Body__toplevels_iter_star_seps__Toplevel__layouts_LAYOUTLIST_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__Toplevels_Body__toplevels_iter_star_seps__Toplevel__layouts_LAYOUTLIST_(builder);
      
    }
  }
	
  protected static class LAYOUT {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__LAYOUT__char_class___range__9_10_range__13_13_range__32_32_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new CharStackNode<IConstructor>(3406, 0, new int[][]{{9,10},{13,13},{32,32}}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__LAYOUT__char_class___range__9_10_range__13_13_range__32_32_, tmp);
	}
    protected static final void _init_prod__LAYOUT__Comment_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(3408, 0, "Comment", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__LAYOUT__Comment_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__LAYOUT__char_class___range__9_10_range__13_13_range__32_32_(builder);
      
        _init_prod__LAYOUT__Comment_(builder);
      
    }
  }
	
  protected static class DecimalIntegerLiteral {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__DecimalIntegerLiteral__conditional__lit_0__not_follow__char_class___range__48_57_range__65_90_range__95_95_range__97_122_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(3468, 0, prod__lit_0__char_class___range__48_48_, new int[] {48}, null, new ICompletionFilter[] {new CharFollowRestriction(new int[][]{{48,57},{65,90},{95,95},{97,122}})});
      builder.addAlternative(ObjectRascalRascal.prod__DecimalIntegerLiteral__conditional__lit_0__not_follow__char_class___range__48_57_range__65_90_range__95_95_range__97_122_, tmp);
	}
    protected static final void _init_prod__DecimalIntegerLiteral__char_class___range__49_57_conditional__iter_star__char_class___range__48_57__not_follow__char_class___range__48_57_range__65_90_range__95_95_range__97_122_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[2];
      
      tmp[1] = new ListStackNode<IConstructor>(3472, 1, regular__iter_star__char_class___range__48_57, new CharStackNode<IConstructor>(3474, 0, new int[][]{{48,57}}, null, null), false, null, new ICompletionFilter[] {new CharFollowRestriction(new int[][]{{48,57},{65,90},{95,95},{97,122}})});
      tmp[0] = new CharStackNode<IConstructor>(3470, 0, new int[][]{{49,57}}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__DecimalIntegerLiteral__char_class___range__49_57_conditional__iter_star__char_class___range__48_57__not_follow__char_class___range__48_57_range__65_90_range__95_95_range__97_122_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__DecimalIntegerLiteral__conditional__lit_0__not_follow__char_class___range__48_57_range__65_90_range__95_95_range__97_122_(builder);
      
        _init_prod__DecimalIntegerLiteral__char_class___range__49_57_conditional__iter_star__char_class___range__48_57__not_follow__char_class___range__48_57_range__65_90_range__95_95_range__97_122_(builder);
      
    }
  }
	
  protected static class StringTemplate {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__While_StringTemplate__lit_while_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_condition_Expression_layouts_LAYOUTLIST_lit___41_layouts_LAYOUTLIST_lit___123_layouts_LAYOUTLIST_preStats_iter_star_seps__Statement__layouts_LAYOUTLIST_layouts_LAYOUTLIST_body_StringMiddle_layouts_LAYOUTLIST_postStats_iter_star_seps__Statement__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[17];
      
      tmp[16] = new LiteralStackNode<IConstructor>(3516, 16, prod__lit___125__char_class___range__125_125_, new int[] {125}, null, null);
      tmp[15] = new NonTerminalStackNode<IConstructor>(3514, 15, "layouts_LAYOUTLIST", null, null);
      tmp[14] = new SeparatedListStackNode<IConstructor>(3508, 14, regular__iter_star_seps__Statement__layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(3510, 0, "Statement", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(3512, 1, "layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[13] = new NonTerminalStackNode<IConstructor>(3506, 13, "layouts_LAYOUTLIST", null, null);
      tmp[12] = new NonTerminalStackNode<IConstructor>(3504, 12, "StringMiddle", null, null);
      tmp[11] = new NonTerminalStackNode<IConstructor>(3502, 11, "layouts_LAYOUTLIST", null, null);
      tmp[10] = new SeparatedListStackNode<IConstructor>(3496, 10, regular__iter_star_seps__Statement__layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(3498, 0, "Statement", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(3500, 1, "layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[9] = new NonTerminalStackNode<IConstructor>(3494, 9, "layouts_LAYOUTLIST", null, null);
      tmp[8] = new LiteralStackNode<IConstructor>(3492, 8, prod__lit___123__char_class___range__123_123_, new int[] {123}, null, null);
      tmp[7] = new NonTerminalStackNode<IConstructor>(3490, 7, "layouts_LAYOUTLIST", null, null);
      tmp[6] = new LiteralStackNode<IConstructor>(3488, 6, prod__lit___41__char_class___range__41_41_, new int[] {41}, null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(3486, 5, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new NonTerminalStackNode<IConstructor>(3484, 4, "Expression", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(3482, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(3480, 2, prod__lit___40__char_class___range__40_40_, new int[] {40}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(3478, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(3476, 0, prod__lit_while__char_class___range__119_119_char_class___range__104_104_char_class___range__105_105_char_class___range__108_108_char_class___range__101_101_, new int[] {119,104,105,108,101}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__While_StringTemplate__lit_while_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_condition_Expression_layouts_LAYOUTLIST_lit___41_layouts_LAYOUTLIST_lit___123_layouts_LAYOUTLIST_preStats_iter_star_seps__Statement__layouts_LAYOUTLIST_layouts_LAYOUTLIST_body_StringMiddle_layouts_LAYOUTLIST_postStats_iter_star_seps__Statement__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_, tmp);
	}
    protected static final void _init_prod__IfThenElse_StringTemplate__lit_if_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_conditions_iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_layouts_LAYOUTLIST_lit___123_layouts_LAYOUTLIST_preStatsThen_iter_star_seps__Statement__layouts_LAYOUTLIST_layouts_LAYOUTLIST_thenString_StringMiddle_layouts_LAYOUTLIST_postStatsThen_iter_star_seps__Statement__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_layouts_LAYOUTLIST_lit_else_layouts_LAYOUTLIST_lit___123_layouts_LAYOUTLIST_preStatsElse_iter_star_seps__Statement__layouts_LAYOUTLIST_layouts_LAYOUTLIST_elseString_StringMiddle_layouts_LAYOUTLIST_postStatsElse_iter_star_seps__Statement__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[29];
      
      tmp[28] = new LiteralStackNode<IConstructor>(3598, 28, prod__lit___125__char_class___range__125_125_, new int[] {125}, null, null);
      tmp[27] = new NonTerminalStackNode<IConstructor>(3596, 27, "layouts_LAYOUTLIST", null, null);
      tmp[26] = new SeparatedListStackNode<IConstructor>(3590, 26, regular__iter_star_seps__Statement__layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(3592, 0, "Statement", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(3594, 1, "layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[25] = new NonTerminalStackNode<IConstructor>(3588, 25, "layouts_LAYOUTLIST", null, null);
      tmp[24] = new NonTerminalStackNode<IConstructor>(3586, 24, "StringMiddle", null, null);
      tmp[23] = new NonTerminalStackNode<IConstructor>(3584, 23, "layouts_LAYOUTLIST", null, null);
      tmp[22] = new SeparatedListStackNode<IConstructor>(3578, 22, regular__iter_star_seps__Statement__layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(3580, 0, "Statement", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(3582, 1, "layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[21] = new NonTerminalStackNode<IConstructor>(3576, 21, "layouts_LAYOUTLIST", null, null);
      tmp[20] = new LiteralStackNode<IConstructor>(3574, 20, prod__lit___123__char_class___range__123_123_, new int[] {123}, null, null);
      tmp[19] = new NonTerminalStackNode<IConstructor>(3572, 19, "layouts_LAYOUTLIST", null, null);
      tmp[18] = new LiteralStackNode<IConstructor>(3570, 18, prod__lit_else__char_class___range__101_101_char_class___range__108_108_char_class___range__115_115_char_class___range__101_101_, new int[] {101,108,115,101}, null, null);
      tmp[17] = new NonTerminalStackNode<IConstructor>(3568, 17, "layouts_LAYOUTLIST", null, null);
      tmp[16] = new LiteralStackNode<IConstructor>(3566, 16, prod__lit___125__char_class___range__125_125_, new int[] {125}, null, null);
      tmp[15] = new NonTerminalStackNode<IConstructor>(3564, 15, "layouts_LAYOUTLIST", null, null);
      tmp[14] = new SeparatedListStackNode<IConstructor>(3558, 14, regular__iter_star_seps__Statement__layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(3560, 0, "Statement", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(3562, 1, "layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[13] = new NonTerminalStackNode<IConstructor>(3556, 13, "layouts_LAYOUTLIST", null, null);
      tmp[12] = new NonTerminalStackNode<IConstructor>(3554, 12, "StringMiddle", null, null);
      tmp[11] = new NonTerminalStackNode<IConstructor>(3552, 11, "layouts_LAYOUTLIST", null, null);
      tmp[10] = new SeparatedListStackNode<IConstructor>(3546, 10, regular__iter_star_seps__Statement__layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(3548, 0, "Statement", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(3550, 1, "layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[9] = new NonTerminalStackNode<IConstructor>(3544, 9, "layouts_LAYOUTLIST", null, null);
      tmp[8] = new LiteralStackNode<IConstructor>(3542, 8, prod__lit___123__char_class___range__123_123_, new int[] {123}, null, null);
      tmp[7] = new NonTerminalStackNode<IConstructor>(3540, 7, "layouts_LAYOUTLIST", null, null);
      tmp[6] = new LiteralStackNode<IConstructor>(3538, 6, prod__lit___41__char_class___range__41_41_, new int[] {41}, null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(3536, 5, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new SeparatedListStackNode<IConstructor>(3526, 4, regular__iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(3528, 0, "Expression", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(3530, 1, "layouts_LAYOUTLIST", null, null), new LiteralStackNode<IConstructor>(3532, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode<IConstructor>(3534, 3, "layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(3524, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(3522, 2, prod__lit___40__char_class___range__40_40_, new int[] {40}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(3520, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(3518, 0, prod__lit_if__char_class___range__105_105_char_class___range__102_102_, new int[] {105,102}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__IfThenElse_StringTemplate__lit_if_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_conditions_iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_layouts_LAYOUTLIST_lit___123_layouts_LAYOUTLIST_preStatsThen_iter_star_seps__Statement__layouts_LAYOUTLIST_layouts_LAYOUTLIST_thenString_StringMiddle_layouts_LAYOUTLIST_postStatsThen_iter_star_seps__Statement__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_layouts_LAYOUTLIST_lit_else_layouts_LAYOUTLIST_lit___123_layouts_LAYOUTLIST_preStatsElse_iter_star_seps__Statement__layouts_LAYOUTLIST_layouts_LAYOUTLIST_elseString_StringMiddle_layouts_LAYOUTLIST_postStatsElse_iter_star_seps__Statement__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_, tmp);
	}
    protected static final void _init_prod__DoWhile_StringTemplate__lit_do_layouts_LAYOUTLIST_lit___123_layouts_LAYOUTLIST_preStats_iter_star_seps__Statement__layouts_LAYOUTLIST_layouts_LAYOUTLIST_body_StringMiddle_layouts_LAYOUTLIST_postStats_iter_star_seps__Statement__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_layouts_LAYOUTLIST_lit_while_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_condition_Expression_layouts_LAYOUTLIST_lit___41_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[19];
      
      tmp[18] = new LiteralStackNode<IConstructor>(3694, 18, prod__lit___41__char_class___range__41_41_, new int[] {41}, null, null);
      tmp[17] = new NonTerminalStackNode<IConstructor>(3692, 17, "layouts_LAYOUTLIST", null, null);
      tmp[16] = new NonTerminalStackNode<IConstructor>(3690, 16, "Expression", null, null);
      tmp[15] = new NonTerminalStackNode<IConstructor>(3688, 15, "layouts_LAYOUTLIST", null, null);
      tmp[14] = new LiteralStackNode<IConstructor>(3686, 14, prod__lit___40__char_class___range__40_40_, new int[] {40}, null, null);
      tmp[13] = new NonTerminalStackNode<IConstructor>(3684, 13, "layouts_LAYOUTLIST", null, null);
      tmp[12] = new LiteralStackNode<IConstructor>(3682, 12, prod__lit_while__char_class___range__119_119_char_class___range__104_104_char_class___range__105_105_char_class___range__108_108_char_class___range__101_101_, new int[] {119,104,105,108,101}, null, null);
      tmp[11] = new NonTerminalStackNode<IConstructor>(3680, 11, "layouts_LAYOUTLIST", null, null);
      tmp[10] = new LiteralStackNode<IConstructor>(3678, 10, prod__lit___125__char_class___range__125_125_, new int[] {125}, null, null);
      tmp[9] = new NonTerminalStackNode<IConstructor>(3676, 9, "layouts_LAYOUTLIST", null, null);
      tmp[8] = new SeparatedListStackNode<IConstructor>(3670, 8, regular__iter_star_seps__Statement__layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(3672, 0, "Statement", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(3674, 1, "layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[7] = new NonTerminalStackNode<IConstructor>(3668, 7, "layouts_LAYOUTLIST", null, null);
      tmp[6] = new NonTerminalStackNode<IConstructor>(3666, 6, "StringMiddle", null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(3664, 5, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new SeparatedListStackNode<IConstructor>(3658, 4, regular__iter_star_seps__Statement__layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(3660, 0, "Statement", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(3662, 1, "layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(3656, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(3654, 2, prod__lit___123__char_class___range__123_123_, new int[] {123}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(3652, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(3650, 0, prod__lit_do__char_class___range__100_100_char_class___range__111_111_, new int[] {100,111}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__DoWhile_StringTemplate__lit_do_layouts_LAYOUTLIST_lit___123_layouts_LAYOUTLIST_preStats_iter_star_seps__Statement__layouts_LAYOUTLIST_layouts_LAYOUTLIST_body_StringMiddle_layouts_LAYOUTLIST_postStats_iter_star_seps__Statement__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_layouts_LAYOUTLIST_lit_while_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_condition_Expression_layouts_LAYOUTLIST_lit___41_, tmp);
	}
    protected static final void _init_prod__For_StringTemplate__lit_for_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_generators_iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_layouts_LAYOUTLIST_lit___123_layouts_LAYOUTLIST_preStats_iter_star_seps__Statement__layouts_LAYOUTLIST_layouts_LAYOUTLIST_body_StringMiddle_layouts_LAYOUTLIST_postStats_iter_star_seps__Statement__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[17];
      
      tmp[16] = new LiteralStackNode<IConstructor>(3648, 16, prod__lit___125__char_class___range__125_125_, new int[] {125}, null, null);
      tmp[15] = new NonTerminalStackNode<IConstructor>(3646, 15, "layouts_LAYOUTLIST", null, null);
      tmp[14] = new SeparatedListStackNode<IConstructor>(3640, 14, regular__iter_star_seps__Statement__layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(3642, 0, "Statement", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(3644, 1, "layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[13] = new NonTerminalStackNode<IConstructor>(3638, 13, "layouts_LAYOUTLIST", null, null);
      tmp[12] = new NonTerminalStackNode<IConstructor>(3636, 12, "StringMiddle", null, null);
      tmp[11] = new NonTerminalStackNode<IConstructor>(3634, 11, "layouts_LAYOUTLIST", null, null);
      tmp[10] = new SeparatedListStackNode<IConstructor>(3628, 10, regular__iter_star_seps__Statement__layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(3630, 0, "Statement", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(3632, 1, "layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[9] = new NonTerminalStackNode<IConstructor>(3626, 9, "layouts_LAYOUTLIST", null, null);
      tmp[8] = new LiteralStackNode<IConstructor>(3624, 8, prod__lit___123__char_class___range__123_123_, new int[] {123}, null, null);
      tmp[7] = new NonTerminalStackNode<IConstructor>(3622, 7, "layouts_LAYOUTLIST", null, null);
      tmp[6] = new LiteralStackNode<IConstructor>(3620, 6, prod__lit___41__char_class___range__41_41_, new int[] {41}, null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(3618, 5, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new SeparatedListStackNode<IConstructor>(3608, 4, regular__iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(3610, 0, "Expression", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(3612, 1, "layouts_LAYOUTLIST", null, null), new LiteralStackNode<IConstructor>(3614, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode<IConstructor>(3616, 3, "layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(3606, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(3604, 2, prod__lit___40__char_class___range__40_40_, new int[] {40}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(3602, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(3600, 0, prod__lit_for__char_class___range__102_102_char_class___range__111_111_char_class___range__114_114_, new int[] {102,111,114}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__For_StringTemplate__lit_for_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_generators_iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_layouts_LAYOUTLIST_lit___123_layouts_LAYOUTLIST_preStats_iter_star_seps__Statement__layouts_LAYOUTLIST_layouts_LAYOUTLIST_body_StringMiddle_layouts_LAYOUTLIST_postStats_iter_star_seps__Statement__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_, tmp);
	}
    protected static final void _init_prod__IfThen_StringTemplate__lit_if_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_conditions_iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_layouts_LAYOUTLIST_lit___123_layouts_LAYOUTLIST_preStats_iter_star_seps__Statement__layouts_LAYOUTLIST_layouts_LAYOUTLIST_body_StringMiddle_layouts_LAYOUTLIST_postStats_iter_star_seps__Statement__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[17];
      
      tmp[16] = new LiteralStackNode<IConstructor>(3744, 16, prod__lit___125__char_class___range__125_125_, new int[] {125}, null, null);
      tmp[15] = new NonTerminalStackNode<IConstructor>(3742, 15, "layouts_LAYOUTLIST", null, null);
      tmp[14] = new SeparatedListStackNode<IConstructor>(3736, 14, regular__iter_star_seps__Statement__layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(3738, 0, "Statement", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(3740, 1, "layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[13] = new NonTerminalStackNode<IConstructor>(3734, 13, "layouts_LAYOUTLIST", null, null);
      tmp[12] = new NonTerminalStackNode<IConstructor>(3732, 12, "StringMiddle", null, null);
      tmp[11] = new NonTerminalStackNode<IConstructor>(3730, 11, "layouts_LAYOUTLIST", null, null);
      tmp[10] = new SeparatedListStackNode<IConstructor>(3724, 10, regular__iter_star_seps__Statement__layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(3726, 0, "Statement", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(3728, 1, "layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[9] = new NonTerminalStackNode<IConstructor>(3722, 9, "layouts_LAYOUTLIST", null, null);
      tmp[8] = new LiteralStackNode<IConstructor>(3720, 8, prod__lit___123__char_class___range__123_123_, new int[] {123}, null, null);
      tmp[7] = new NonTerminalStackNode<IConstructor>(3718, 7, "layouts_LAYOUTLIST", null, null);
      tmp[6] = new LiteralStackNode<IConstructor>(3716, 6, prod__lit___41__char_class___range__41_41_, new int[] {41}, null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(3714, 5, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new SeparatedListStackNode<IConstructor>(3704, 4, regular__iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(3706, 0, "Expression", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(3708, 1, "layouts_LAYOUTLIST", null, null), new LiteralStackNode<IConstructor>(3710, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode<IConstructor>(3712, 3, "layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(3702, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(3700, 2, prod__lit___40__char_class___range__40_40_, new int[] {40}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(3698, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(3696, 0, prod__lit_if__char_class___range__105_105_char_class___range__102_102_, new int[] {105,102}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__IfThen_StringTemplate__lit_if_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_conditions_iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_layouts_LAYOUTLIST_lit___123_layouts_LAYOUTLIST_preStats_iter_star_seps__Statement__layouts_LAYOUTLIST_layouts_LAYOUTLIST_body_StringMiddle_layouts_LAYOUTLIST_postStats_iter_star_seps__Statement__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__While_StringTemplate__lit_while_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_condition_Expression_layouts_LAYOUTLIST_lit___41_layouts_LAYOUTLIST_lit___123_layouts_LAYOUTLIST_preStats_iter_star_seps__Statement__layouts_LAYOUTLIST_layouts_LAYOUTLIST_body_StringMiddle_layouts_LAYOUTLIST_postStats_iter_star_seps__Statement__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_(builder);
      
        _init_prod__IfThenElse_StringTemplate__lit_if_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_conditions_iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_layouts_LAYOUTLIST_lit___123_layouts_LAYOUTLIST_preStatsThen_iter_star_seps__Statement__layouts_LAYOUTLIST_layouts_LAYOUTLIST_thenString_StringMiddle_layouts_LAYOUTLIST_postStatsThen_iter_star_seps__Statement__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_layouts_LAYOUTLIST_lit_else_layouts_LAYOUTLIST_lit___123_layouts_LAYOUTLIST_preStatsElse_iter_star_seps__Statement__layouts_LAYOUTLIST_layouts_LAYOUTLIST_elseString_StringMiddle_layouts_LAYOUTLIST_postStatsElse_iter_star_seps__Statement__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_(builder);
      
        _init_prod__DoWhile_StringTemplate__lit_do_layouts_LAYOUTLIST_lit___123_layouts_LAYOUTLIST_preStats_iter_star_seps__Statement__layouts_LAYOUTLIST_layouts_LAYOUTLIST_body_StringMiddle_layouts_LAYOUTLIST_postStats_iter_star_seps__Statement__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_layouts_LAYOUTLIST_lit_while_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_condition_Expression_layouts_LAYOUTLIST_lit___41_(builder);
      
        _init_prod__For_StringTemplate__lit_for_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_generators_iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_layouts_LAYOUTLIST_lit___123_layouts_LAYOUTLIST_preStats_iter_star_seps__Statement__layouts_LAYOUTLIST_layouts_LAYOUTLIST_body_StringMiddle_layouts_LAYOUTLIST_postStats_iter_star_seps__Statement__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_(builder);
      
        _init_prod__IfThen_StringTemplate__lit_if_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_conditions_iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_layouts_LAYOUTLIST_lit___123_layouts_LAYOUTLIST_preStats_iter_star_seps__Statement__layouts_LAYOUTLIST_layouts_LAYOUTLIST_body_StringMiddle_layouts_LAYOUTLIST_postStats_iter_star_seps__Statement__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_(builder);
      
    }
  }
	
  protected static class PathPart {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__NonInterpolated_PathPart__pathChars_PathChars_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(3762, 0, "PathChars", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__NonInterpolated_PathPart__pathChars_PathChars_, tmp);
	}
    protected static final void _init_prod__Interpolated_PathPart__pre_PrePathChars_layouts_LAYOUTLIST_expression_Expression_layouts_LAYOUTLIST_tail_PathTail_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(3772, 4, "PathTail", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(3770, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(3768, 2, "Expression", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(3766, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(3764, 0, "PrePathChars", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Interpolated_PathPart__pre_PrePathChars_layouts_LAYOUTLIST_expression_Expression_layouts_LAYOUTLIST_tail_PathTail_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__NonInterpolated_PathPart__pathChars_PathChars_(builder);
      
        _init_prod__Interpolated_PathPart__pre_PrePathChars_layouts_LAYOUTLIST_expression_Expression_layouts_LAYOUTLIST_tail_PathTail_(builder);
      
    }
  }
	
  protected static class RegExp {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__RegExp__char_class___range__92_92_char_class___range__47_47_range__60_60_range__62_62_range__92_92_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[2];
      
      tmp[1] = new CharStackNode<IConstructor>(3818, 1, new int[][]{{47,47},{60,60},{62,62},{92,92}}, null, null);
      tmp[0] = new CharStackNode<IConstructor>(3816, 0, new int[][]{{92,92}}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RegExp__char_class___range__92_92_char_class___range__47_47_range__60_60_range__62_62_range__92_92_, tmp);
	}
    protected static final void _init_prod__RegExp__lit___60_Name_lit___58_iter_star__NamedRegExp_lit___62_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new LiteralStackNode<IConstructor>(3830, 4, prod__lit___62__char_class___range__62_62_, new int[] {62}, null, null);
      tmp[3] = new ListStackNode<IConstructor>(3826, 3, regular__iter_star__NamedRegExp, new NonTerminalStackNode<IConstructor>(3828, 0, "NamedRegExp", null, null), false, null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(3824, 2, prod__lit___58__char_class___range__58_58_, new int[] {58}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(3822, 1, "Name", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(3820, 0, prod__lit___60__char_class___range__60_60_, new int[] {60}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RegExp__lit___60_Name_lit___58_iter_star__NamedRegExp_lit___62_, tmp);
	}
    protected static final void _init_prod__RegExp__char_class___range__60_60_expression_Expression_char_class___range__62_62__tag__category___77_101_116_97_86_97_114_105_97_98_108_101(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new CharStackNode<IConstructor>(3836, 2, new int[][]{{62,62}}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(3834, 1, "Expression", null, null);
      tmp[0] = new CharStackNode<IConstructor>(3832, 0, new int[][]{{60,60}}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RegExp__char_class___range__60_60_expression_Expression_char_class___range__62_62__tag__category___77_101_116_97_86_97_114_105_97_98_108_101, tmp);
	}
    protected static final void _init_prod__RegExp__char_class___range__0_46_range__48_59_range__61_61_range__63_91_range__93_16777215_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new CharStackNode<IConstructor>(3838, 0, new int[][]{{0,46},{48,59},{61,61},{63,91},{93,16777215}}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RegExp__char_class___range__0_46_range__48_59_range__61_61_range__63_91_range__93_16777215_, tmp);
	}
    protected static final void _init_prod__RegExp__lit___60_Name_lit___62_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new LiteralStackNode<IConstructor>(3844, 2, prod__lit___62__char_class___range__62_62_, new int[] {62}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(3842, 1, "Name", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(3840, 0, prod__lit___60__char_class___range__60_60_, new int[] {60}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RegExp__lit___60_Name_lit___62_, tmp);
	}
    protected static final void _init_prod__RegExp__Backslash_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(3846, 0, "Backslash", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RegExp__Backslash_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__RegExp__char_class___range__92_92_char_class___range__47_47_range__60_60_range__62_62_range__92_92_(builder);
      
        _init_prod__RegExp__lit___60_Name_lit___58_iter_star__NamedRegExp_lit___62_(builder);
      
        _init_prod__RegExp__char_class___range__60_60_expression_Expression_char_class___range__62_62__tag__category___77_101_116_97_86_97_114_105_97_98_108_101(builder);
      
        _init_prod__RegExp__char_class___range__0_46_range__48_59_range__61_61_range__63_91_range__93_16777215_(builder);
      
        _init_prod__RegExp__lit___60_Name_lit___62_(builder);
      
        _init_prod__RegExp__Backslash_(builder);
      
    }
  }
	
  protected static class MidStringChars {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__MidStringChars__char_class___range__62_62_iter_star__StringCharacter_char_class___range__60_60__tag__category___67_111_110_115_116_97_110_116(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new CharStackNode<IConstructor>(3880, 2, new int[][]{{60,60}}, null, null);
      tmp[1] = new ListStackNode<IConstructor>(3876, 1, regular__iter_star__StringCharacter, new NonTerminalStackNode<IConstructor>(3878, 0, "StringCharacter", null, null), false, null, null);
      tmp[0] = new CharStackNode<IConstructor>(3874, 0, new int[][]{{62,62}}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__MidStringChars__char_class___range__62_62_iter_star__StringCharacter_char_class___range__60_60__tag__category___67_111_110_115_116_97_110_116, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__MidStringChars__char_class___range__62_62_iter_star__StringCharacter_char_class___range__60_60__tag__category___67_111_110_115_116_97_110_116(builder);
      
    }
  }
	
  protected static class RegExpModifier {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__RegExpModifier__iter_star__char_class___range__100_100_range__105_105_range__109_109_range__115_115_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new ListStackNode<IConstructor>(3890, 0, regular__iter_star__char_class___range__100_100_range__105_105_range__109_109_range__115_115, new CharStackNode<IConstructor>(3892, 0, new int[][]{{100,100},{105,105},{109,109},{115,115}}, null, null), false, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RegExpModifier__iter_star__char_class___range__100_100_range__105_105_range__109_109_range__115_115_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__RegExpModifier__iter_star__char_class___range__100_100_range__105_105_range__109_109_range__115_115_(builder);
      
    }
  }
	
  protected static class layouts_$BACKTICKS {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__layouts_$BACKTICKS__(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new EpsilonStackNode<IConstructor>(3904, 0);
      builder.addAlternative(ObjectRascalRascal.prod__layouts_$BACKTICKS__, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__layouts_$BACKTICKS__(builder);
      
    }
  }
	
  protected static class Assoc {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__Associative_Assoc__lit_assoc_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(3906, 0, prod__lit_assoc__char_class___range__97_97_char_class___range__115_115_char_class___range__115_115_char_class___range__111_111_char_class___range__99_99_, new int[] {97,115,115,111,99}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Associative_Assoc__lit_assoc_, tmp);
	}
    protected static final void _init_prod__Left_Assoc__lit_left_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(3908, 0, prod__lit_left__char_class___range__108_108_char_class___range__101_101_char_class___range__102_102_char_class___range__116_116_, new int[] {108,101,102,116}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Left_Assoc__lit_left_, tmp);
	}
    protected static final void _init_prod__NonAssociative_Assoc__lit_non_assoc_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(3910, 0, prod__lit_non_assoc__char_class___range__110_110_char_class___range__111_111_char_class___range__110_110_char_class___range__45_45_char_class___range__97_97_char_class___range__115_115_char_class___range__115_115_char_class___range__111_111_char_class___range__99_99_, new int[] {110,111,110,45,97,115,115,111,99}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__NonAssociative_Assoc__lit_non_assoc_, tmp);
	}
    protected static final void _init_prod__Right_Assoc__lit_right_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(3912, 0, prod__lit_right__char_class___range__114_114_char_class___range__105_105_char_class___range__103_103_char_class___range__104_104_char_class___range__116_116_, new int[] {114,105,103,104,116}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Right_Assoc__lit_right_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__Associative_Assoc__lit_assoc_(builder);
      
        _init_prod__Left_Assoc__lit_left_(builder);
      
        _init_prod__NonAssociative_Assoc__lit_non_assoc_(builder);
      
        _init_prod__Right_Assoc__lit_right_(builder);
      
    }
  }
	
  protected static class Replacement {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__Unconditional_Replacement__replacementExpression_Expression_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(3914, 0, "Expression", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Unconditional_Replacement__replacementExpression_Expression_, tmp);
	}
    protected static final void _init_prod__Conditional_Replacement__replacementExpression_Expression_layouts_LAYOUTLIST_lit_when_layouts_LAYOUTLIST_conditions_iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new SeparatedListStackNode<IConstructor>(3924, 4, regular__iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(3926, 0, "Expression", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(3928, 1, "layouts_LAYOUTLIST", null, null), new LiteralStackNode<IConstructor>(3930, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode<IConstructor>(3932, 3, "layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(3922, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(3920, 2, prod__lit_when__char_class___range__119_119_char_class___range__104_104_char_class___range__101_101_char_class___range__110_110_, new int[] {119,104,101,110}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(3918, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(3916, 0, "Expression", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Conditional_Replacement__replacementExpression_Expression_layouts_LAYOUTLIST_lit_when_layouts_LAYOUTLIST_conditions_iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__Unconditional_Replacement__replacementExpression_Expression_(builder);
      
        _init_prod__Conditional_Replacement__replacementExpression_Expression_layouts_LAYOUTLIST_lit_when_layouts_LAYOUTLIST_conditions_iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_(builder);
      
    }
  }
	
  protected static class DataTarget {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__Empty_DataTarget__(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new EpsilonStackNode<IConstructor>(3982, 0);
      builder.addAlternative(ObjectRascalRascal.prod__Empty_DataTarget__, tmp);
	}
    protected static final void _init_prod__Labeled_DataTarget__label_Name_layouts_LAYOUTLIST_lit___58_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new LiteralStackNode<IConstructor>(3988, 2, prod__lit___58__char_class___range__58_58_, new int[] {58}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(3986, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(3984, 0, "Name", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Labeled_DataTarget__label_Name_layouts_LAYOUTLIST_lit___58_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__Empty_DataTarget__(builder);
      
        _init_prod__Labeled_DataTarget__label_Name_layouts_LAYOUTLIST_lit___58_(builder);
      
    }
  }
	
  protected static class layouts_$default$ {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__layouts_$default$__(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new EpsilonStackNode<IConstructor>(3990, 0);
      builder.addAlternative(ObjectRascalRascal.prod__layouts_$default$__, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__layouts_$default$__(builder);
      
    }
  }
	
  protected static class RealLiteral {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__RealLiteral__conditional__lit___46__not_precede__char_class___range__46_46_iter__char_class___range__48_57_opt__char_class___range__68_68_range__70_70_range__100_100_range__102_102_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new OptionalStackNode<IConstructor>(3998, 2, regular__opt__char_class___range__68_68_range__70_70_range__100_100_range__102_102, new CharStackNode<IConstructor>(4000, 0, new int[][]{{68,68},{70,70},{100,100},{102,102}}, null, null), null, null);
      tmp[1] = new ListStackNode<IConstructor>(3994, 1, regular__iter__char_class___range__48_57, new CharStackNode<IConstructor>(3996, 0, new int[][]{{48,57}}, null, null), true, null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(3992, 0, prod__lit___46__char_class___range__46_46_, new int[] {46}, new IEnterFilter[] {new CharPrecedeRestriction(new int[][]{{46,46}})}, null);
      builder.addAlternative(ObjectRascalRascal.prod__RealLiteral__conditional__lit___46__not_precede__char_class___range__46_46_iter__char_class___range__48_57_opt__char_class___range__68_68_range__70_70_range__100_100_range__102_102_, tmp);
	}
    protected static final void _init_prod__RealLiteral__iter__char_class___range__48_57_lit___46_iter_star__char_class___range__48_57_char_class___range__69_69_range__101_101_opt__char_class___range__43_43_range__45_45_iter__char_class___range__48_57_opt__char_class___range__68_68_range__70_70_range__100_100_range__102_102_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[7];
      
      tmp[0] = new ListStackNode<IConstructor>(4002, 0, regular__iter__char_class___range__48_57, new CharStackNode<IConstructor>(4004, 0, new int[][]{{48,57}}, null, null), true, null, null);
      tmp[1] = new LiteralStackNode<IConstructor>(4006, 1, prod__lit___46__char_class___range__46_46_, new int[] {46}, null, null);
      tmp[2] = new ListStackNode<IConstructor>(4008, 2, regular__iter_star__char_class___range__48_57, new CharStackNode<IConstructor>(4010, 0, new int[][]{{48,57}}, null, null), false, null, null);
      tmp[3] = new CharStackNode<IConstructor>(4012, 3, new int[][]{{69,69},{101,101}}, null, null);
      tmp[4] = new OptionalStackNode<IConstructor>(4014, 4, regular__opt__char_class___range__43_43_range__45_45, new CharStackNode<IConstructor>(4016, 0, new int[][]{{43,43},{45,45}}, null, null), null, null);
      tmp[5] = new ListStackNode<IConstructor>(4018, 5, regular__iter__char_class___range__48_57, new CharStackNode<IConstructor>(4020, 0, new int[][]{{48,57}}, null, null), true, null, null);
      tmp[6] = new OptionalStackNode<IConstructor>(4022, 6, regular__opt__char_class___range__68_68_range__70_70_range__100_100_range__102_102, new CharStackNode<IConstructor>(4024, 0, new int[][]{{68,68},{70,70},{100,100},{102,102}}, null, null), null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RealLiteral__iter__char_class___range__48_57_lit___46_iter_star__char_class___range__48_57_char_class___range__69_69_range__101_101_opt__char_class___range__43_43_range__45_45_iter__char_class___range__48_57_opt__char_class___range__68_68_range__70_70_range__100_100_range__102_102_, tmp);
	}
    protected static final void _init_prod__RealLiteral__iter__char_class___range__48_57_conditional__lit___46__not_follow__lit___46_iter_star__char_class___range__48_57_opt__char_class___range__68_68_range__70_70_range__100_100_range__102_102_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[4];
      
      tmp[3] = new OptionalStackNode<IConstructor>(4056, 3, regular__opt__char_class___range__68_68_range__70_70_range__100_100_range__102_102, new CharStackNode<IConstructor>(4058, 0, new int[][]{{68,68},{70,70},{100,100},{102,102}}, null, null), null, null);
      tmp[2] = new ListStackNode<IConstructor>(4052, 2, regular__iter_star__char_class___range__48_57, new CharStackNode<IConstructor>(4054, 0, new int[][]{{48,57}}, null, null), false, null, null);
      tmp[1] = new LiteralStackNode<IConstructor>(4050, 1, prod__lit___46__char_class___range__46_46_, new int[] {46}, null, new ICompletionFilter[] {new StringFollowRestriction(new int[] {46})});
      tmp[0] = new ListStackNode<IConstructor>(4046, 0, regular__iter__char_class___range__48_57, new CharStackNode<IConstructor>(4048, 0, new int[][]{{48,57}}, null, null), true, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RealLiteral__iter__char_class___range__48_57_conditional__lit___46__not_follow__lit___46_iter_star__char_class___range__48_57_opt__char_class___range__68_68_range__70_70_range__100_100_range__102_102_, tmp);
	}
    protected static final void _init_prod__RealLiteral__conditional__lit___46__not_precede__char_class___range__46_46_iter__char_class___range__48_57_char_class___range__69_69_range__101_101_opt__char_class___range__43_43_range__45_45_iter__char_class___range__48_57_opt__char_class___range__68_68_range__70_70_range__100_100_range__102_102_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[6];
      
      tmp[5] = new OptionalStackNode<IConstructor>(4042, 5, regular__opt__char_class___range__68_68_range__70_70_range__100_100_range__102_102, new CharStackNode<IConstructor>(4044, 0, new int[][]{{68,68},{70,70},{100,100},{102,102}}, null, null), null, null);
      tmp[4] = new ListStackNode<IConstructor>(4038, 4, regular__iter__char_class___range__48_57, new CharStackNode<IConstructor>(4040, 0, new int[][]{{48,57}}, null, null), true, null, null);
      tmp[3] = new OptionalStackNode<IConstructor>(4034, 3, regular__opt__char_class___range__43_43_range__45_45, new CharStackNode<IConstructor>(4036, 0, new int[][]{{43,43},{45,45}}, null, null), null, null);
      tmp[2] = new CharStackNode<IConstructor>(4032, 2, new int[][]{{69,69},{101,101}}, null, null);
      tmp[1] = new ListStackNode<IConstructor>(4028, 1, regular__iter__char_class___range__48_57, new CharStackNode<IConstructor>(4030, 0, new int[][]{{48,57}}, null, null), true, null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(4026, 0, prod__lit___46__char_class___range__46_46_, new int[] {46}, new IEnterFilter[] {new CharPrecedeRestriction(new int[][]{{46,46}})}, null);
      builder.addAlternative(ObjectRascalRascal.prod__RealLiteral__conditional__lit___46__not_precede__char_class___range__46_46_iter__char_class___range__48_57_char_class___range__69_69_range__101_101_opt__char_class___range__43_43_range__45_45_iter__char_class___range__48_57_opt__char_class___range__68_68_range__70_70_range__100_100_range__102_102_, tmp);
	}
    protected static final void _init_prod__RealLiteral__iter__char_class___range__48_57_char_class___range__69_69_range__101_101_opt__char_class___range__43_43_range__45_45_iter__char_class___range__48_57_opt__char_class___range__68_68_range__70_70_range__100_100_range__102_102_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new OptionalStackNode<IConstructor>(4074, 4, regular__opt__char_class___range__68_68_range__70_70_range__100_100_range__102_102, new CharStackNode<IConstructor>(4076, 0, new int[][]{{68,68},{70,70},{100,100},{102,102}}, null, null), null, null);
      tmp[3] = new ListStackNode<IConstructor>(4070, 3, regular__iter__char_class___range__48_57, new CharStackNode<IConstructor>(4072, 0, new int[][]{{48,57}}, null, null), true, null, null);
      tmp[2] = new OptionalStackNode<IConstructor>(4066, 2, regular__opt__char_class___range__43_43_range__45_45, new CharStackNode<IConstructor>(4068, 0, new int[][]{{43,43},{45,45}}, null, null), null, null);
      tmp[1] = new CharStackNode<IConstructor>(4064, 1, new int[][]{{69,69},{101,101}}, null, null);
      tmp[0] = new ListStackNode<IConstructor>(4060, 0, regular__iter__char_class___range__48_57, new CharStackNode<IConstructor>(4062, 0, new int[][]{{48,57}}, null, null), true, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RealLiteral__iter__char_class___range__48_57_char_class___range__69_69_range__101_101_opt__char_class___range__43_43_range__45_45_iter__char_class___range__48_57_opt__char_class___range__68_68_range__70_70_range__100_100_range__102_102_, tmp);
	}
    protected static final void _init_prod__RealLiteral__iter__char_class___range__48_57_char_class___range__68_68_range__70_70_range__100_100_range__102_102_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[2];
      
      tmp[1] = new CharStackNode<IConstructor>(4082, 1, new int[][]{{68,68},{70,70},{100,100},{102,102}}, null, null);
      tmp[0] = new ListStackNode<IConstructor>(4078, 0, regular__iter__char_class___range__48_57, new CharStackNode<IConstructor>(4080, 0, new int[][]{{48,57}}, null, null), true, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RealLiteral__iter__char_class___range__48_57_char_class___range__68_68_range__70_70_range__100_100_range__102_102_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__RealLiteral__conditional__lit___46__not_precede__char_class___range__46_46_iter__char_class___range__48_57_opt__char_class___range__68_68_range__70_70_range__100_100_range__102_102_(builder);
      
        _init_prod__RealLiteral__iter__char_class___range__48_57_lit___46_iter_star__char_class___range__48_57_char_class___range__69_69_range__101_101_opt__char_class___range__43_43_range__45_45_iter__char_class___range__48_57_opt__char_class___range__68_68_range__70_70_range__100_100_range__102_102_(builder);
      
        _init_prod__RealLiteral__iter__char_class___range__48_57_conditional__lit___46__not_follow__lit___46_iter_star__char_class___range__48_57_opt__char_class___range__68_68_range__70_70_range__100_100_range__102_102_(builder);
      
        _init_prod__RealLiteral__conditional__lit___46__not_precede__char_class___range__46_46_iter__char_class___range__48_57_char_class___range__69_69_range__101_101_opt__char_class___range__43_43_range__45_45_iter__char_class___range__48_57_opt__char_class___range__68_68_range__70_70_range__100_100_range__102_102_(builder);
      
        _init_prod__RealLiteral__iter__char_class___range__48_57_char_class___range__69_69_range__101_101_opt__char_class___range__43_43_range__45_45_iter__char_class___range__48_57_opt__char_class___range__68_68_range__70_70_range__100_100_range__102_102_(builder);
      
        _init_prod__RealLiteral__iter__char_class___range__48_57_char_class___range__68_68_range__70_70_range__100_100_range__102_102_(builder);
      
    }
  }
	
  protected static class layouts_LAYOUTLIST {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__layouts_LAYOUTLIST__conditional__iter_star__LAYOUT__not_follow__char_class___range__9_10_range__13_13_range__32_32_not_follow__lit___47_47_not_follow__lit___47_42_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new ListStackNode<IConstructor>(4122, 0, regular__iter_star__LAYOUT, new NonTerminalStackNode<IConstructor>(4124, 0, "LAYOUT", null, null), false, null, new ICompletionFilter[] {new CharFollowRestriction(new int[][]{{9,10},{13,13},{32,32}}), new StringFollowRestriction(new int[] {47,47}), new StringFollowRestriction(new int[] {47,42})});
      builder.addAlternative(ObjectRascalRascal.prod__layouts_LAYOUTLIST__conditional__iter_star__LAYOUT__not_follow__char_class___range__9_10_range__13_13_range__32_32_not_follow__lit___47_47_not_follow__lit___47_42_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__layouts_LAYOUTLIST__conditional__iter_star__LAYOUT__not_follow__char_class___range__9_10_range__13_13_range__32_32_not_follow__lit___47_47_not_follow__lit___47_42_(builder);
      
    }
  }
	
  protected static class StringCharacter {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__StringCharacter__lit___92_char_class___range__34_34_range__39_39_range__60_60_range__62_62_range__92_92_range__98_98_range__102_102_range__110_110_range__114_114_range__116_116_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[2];
      
      tmp[1] = new CharStackNode<IConstructor>(4136, 1, new int[][]{{34,34},{39,39},{60,60},{62,62},{92,92},{98,98},{102,102},{110,110},{114,114},{116,116}}, null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(4134, 0, prod__lit___92__char_class___range__92_92_, new int[] {92}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__StringCharacter__lit___92_char_class___range__34_34_range__39_39_range__60_60_range__62_62_range__92_92_range__98_98_range__102_102_range__110_110_range__114_114_range__116_116_, tmp);
	}
    protected static final void _init_prod__StringCharacter__UnicodeEscape_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(4138, 0, "UnicodeEscape", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__StringCharacter__UnicodeEscape_, tmp);
	}
    protected static final void _init_prod__StringCharacter__char_class___range__0_33_range__35_38_range__40_59_range__61_61_range__63_91_range__93_16777215_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new CharStackNode<IConstructor>(4140, 0, new int[][]{{0,33},{35,38},{40,59},{61,61},{63,91},{93,16777215}}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__StringCharacter__char_class___range__0_33_range__35_38_range__40_59_range__61_61_range__63_91_range__93_16777215_, tmp);
	}
    protected static final void _init_prod__StringCharacter__char_class___range__10_10_iter_star__char_class___range__9_9_range__32_32_char_class___range__39_39_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new CharStackNode<IConstructor>(4148, 2, new int[][]{{39,39}}, null, null);
      tmp[1] = new ListStackNode<IConstructor>(4144, 1, regular__iter_star__char_class___range__9_9_range__32_32, new CharStackNode<IConstructor>(4146, 0, new int[][]{{9,9},{32,32}}, null, null), false, null, null);
      tmp[0] = new CharStackNode<IConstructor>(4142, 0, new int[][]{{10,10}}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__StringCharacter__char_class___range__10_10_iter_star__char_class___range__9_9_range__32_32_char_class___range__39_39_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__StringCharacter__lit___92_char_class___range__34_34_range__39_39_range__60_60_range__62_62_range__92_92_range__98_98_range__102_102_range__110_110_range__114_114_range__116_116_(builder);
      
        _init_prod__StringCharacter__UnicodeEscape_(builder);
      
        _init_prod__StringCharacter__char_class___range__0_33_range__35_38_range__40_59_range__61_61_range__63_91_range__93_16777215_(builder);
      
        _init_prod__StringCharacter__char_class___range__10_10_iter_star__char_class___range__9_9_range__32_32_char_class___range__39_39_(builder);
      
    }
  }
	
  protected static class JustTime {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__JustTime__lit___36_84_TimePartNoTZ_TimeZonePart_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode<IConstructor>(4154, 2, "TimeZonePart", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(4152, 1, "TimePartNoTZ", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(4150, 0, prod__lit___36_84__char_class___range__36_36_char_class___range__84_84_, new int[] {36,84}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__JustTime__lit___36_84_TimePartNoTZ_TimeZonePart_, tmp);
	}
    protected static final void _init_prod__JustTime__lit___36_84_conditional__TimePartNoTZ__not_follow__char_class___range__43_43_range__45_45_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[2];
      
      tmp[1] = new NonTerminalStackNode<IConstructor>(4158, 1, "TimePartNoTZ", null, new ICompletionFilter[] {new CharFollowRestriction(new int[][]{{43,43},{45,45}})});
      tmp[0] = new LiteralStackNode<IConstructor>(4156, 0, prod__lit___36_84__char_class___range__36_36_char_class___range__84_84_, new int[] {36,84}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__JustTime__lit___36_84_conditional__TimePartNoTZ__not_follow__char_class___range__43_43_range__45_45_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__JustTime__lit___36_84_TimePartNoTZ_TimeZonePart_(builder);
      
        _init_prod__JustTime__lit___36_84_conditional__TimePartNoTZ__not_follow__char_class___range__43_43_range__45_45_(builder);
      
    }
  }
	
  protected static class Rest {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__Rest__conditional__iter_star__char_class___range__0_16777215__not_follow__char_class___range__0_16777215_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new ListStackNode<IConstructor>(4170, 0, regular__iter_star__char_class___range__0_16777215, new CharStackNode<IConstructor>(4172, 0, new int[][]{{0,16777215}}, null, null), false, null, new ICompletionFilter[] {new CharFollowRestriction(new int[][]{{0,16777215}})});
      builder.addAlternative(ObjectRascalRascal.prod__Rest__conditional__iter_star__char_class___range__0_16777215__not_follow__char_class___range__0_16777215_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__Rest__conditional__iter_star__char_class___range__0_16777215__not_follow__char_class___range__0_16777215_(builder);
      
    }
  }
	
  protected static class Range {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__FromTo_Range__start_Char_layouts_LAYOUTLIST_lit___layouts_LAYOUTLIST_end_Char_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(4182, 4, "Char", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(4180, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(4178, 2, prod__lit____char_class___range__45_45_, new int[] {45}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(4176, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(4174, 0, "Char", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__FromTo_Range__start_Char_layouts_LAYOUTLIST_lit___layouts_LAYOUTLIST_end_Char_, tmp);
	}
    protected static final void _init_prod__Character_Range__character_Char_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(4184, 0, "Char", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Character_Range__character_Char_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__FromTo_Range__start_Char_layouts_LAYOUTLIST_lit___layouts_LAYOUTLIST_end_Char_(builder);
      
        _init_prod__Character_Range__character_Char_(builder);
      
    }
  }
	
  protected static class LocationLiteral {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__Default_LocationLiteral__protocolPart_ProtocolPart_layouts_LAYOUTLIST_pathPart_PathPart_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode<IConstructor>(4190, 2, "PathPart", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(4188, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(4186, 0, "ProtocolPart", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Default_LocationLiteral__protocolPart_ProtocolPart_layouts_LAYOUTLIST_pathPart_PathPart_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__Default_LocationLiteral__protocolPart_ProtocolPart_layouts_LAYOUTLIST_pathPart_PathPart_(builder);
      
    }
  }
	
  protected static class ShellCommand {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__ListDeclarations_ShellCommand__lit_declarations_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(4192, 0, prod__lit_declarations__char_class___range__100_100_char_class___range__101_101_char_class___range__99_99_char_class___range__108_108_char_class___range__97_97_char_class___range__114_114_char_class___range__97_97_char_class___range__116_116_char_class___range__105_105_char_class___range__111_111_char_class___range__110_110_char_class___range__115_115_, new int[] {100,101,99,108,97,114,97,116,105,111,110,115}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__ListDeclarations_ShellCommand__lit_declarations_, tmp);
	}
    protected static final void _init_prod__Test_ShellCommand__lit_test_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(4194, 0, prod__lit_test__char_class___range__116_116_char_class___range__101_101_char_class___range__115_115_char_class___range__116_116_, new int[] {116,101,115,116}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Test_ShellCommand__lit_test_, tmp);
	}
    protected static final void _init_prod__ListModules_ShellCommand__lit_modules_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(4196, 0, prod__lit_modules__char_class___range__109_109_char_class___range__111_111_char_class___range__100_100_char_class___range__117_117_char_class___range__108_108_char_class___range__101_101_char_class___range__115_115_, new int[] {109,111,100,117,108,101,115}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__ListModules_ShellCommand__lit_modules_, tmp);
	}
    protected static final void _init_prod__SetOption_ShellCommand__lit_set_layouts_LAYOUTLIST_name_QualifiedName_layouts_LAYOUTLIST_expression_Expression_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(4206, 4, "Expression", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(4204, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(4202, 2, "QualifiedName", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(4200, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(4198, 0, prod__lit_set__char_class___range__115_115_char_class___range__101_101_char_class___range__116_116_, new int[] {115,101,116}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__SetOption_ShellCommand__lit_set_layouts_LAYOUTLIST_name_QualifiedName_layouts_LAYOUTLIST_expression_Expression_, tmp);
	}
    protected static final void _init_prod__Edit_ShellCommand__lit_edit_layouts_LAYOUTLIST_name_QualifiedName_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode<IConstructor>(4212, 2, "QualifiedName", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(4210, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(4208, 0, prod__lit_edit__char_class___range__101_101_char_class___range__100_100_char_class___range__105_105_char_class___range__116_116_, new int[] {101,100,105,116}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Edit_ShellCommand__lit_edit_layouts_LAYOUTLIST_name_QualifiedName_, tmp);
	}
    protected static final void _init_prod__History_ShellCommand__lit_history_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(4214, 0, prod__lit_history__char_class___range__104_104_char_class___range__105_105_char_class___range__115_115_char_class___range__116_116_char_class___range__111_111_char_class___range__114_114_char_class___range__121_121_, new int[] {104,105,115,116,111,114,121}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__History_ShellCommand__lit_history_, tmp);
	}
    protected static final void _init_prod__Quit_ShellCommand__lit_quit_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(4216, 0, prod__lit_quit__char_class___range__113_113_char_class___range__117_117_char_class___range__105_105_char_class___range__116_116_, new int[] {113,117,105,116}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Quit_ShellCommand__lit_quit_, tmp);
	}
    protected static final void _init_prod__Undeclare_ShellCommand__lit_undeclare_layouts_LAYOUTLIST_name_QualifiedName_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode<IConstructor>(4222, 2, "QualifiedName", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(4220, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(4218, 0, prod__lit_undeclare__char_class___range__117_117_char_class___range__110_110_char_class___range__100_100_char_class___range__101_101_char_class___range__99_99_char_class___range__108_108_char_class___range__97_97_char_class___range__114_114_char_class___range__101_101_, new int[] {117,110,100,101,99,108,97,114,101}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Undeclare_ShellCommand__lit_undeclare_layouts_LAYOUTLIST_name_QualifiedName_, tmp);
	}
    protected static final void _init_prod__Help_ShellCommand__lit_help_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(4224, 0, prod__lit_help__char_class___range__104_104_char_class___range__101_101_char_class___range__108_108_char_class___range__112_112_, new int[] {104,101,108,112}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Help_ShellCommand__lit_help_, tmp);
	}
    protected static final void _init_prod__Unimport_ShellCommand__lit_unimport_layouts_LAYOUTLIST_name_QualifiedName_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode<IConstructor>(4230, 2, "QualifiedName", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(4228, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(4226, 0, prod__lit_unimport__char_class___range__117_117_char_class___range__110_110_char_class___range__105_105_char_class___range__109_109_char_class___range__112_112_char_class___range__111_111_char_class___range__114_114_char_class___range__116_116_, new int[] {117,110,105,109,112,111,114,116}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Unimport_ShellCommand__lit_unimport_layouts_LAYOUTLIST_name_QualifiedName_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__ListDeclarations_ShellCommand__lit_declarations_(builder);
      
        _init_prod__Test_ShellCommand__lit_test_(builder);
      
        _init_prod__ListModules_ShellCommand__lit_modules_(builder);
      
        _init_prod__SetOption_ShellCommand__lit_set_layouts_LAYOUTLIST_name_QualifiedName_layouts_LAYOUTLIST_expression_Expression_(builder);
      
        _init_prod__Edit_ShellCommand__lit_edit_layouts_LAYOUTLIST_name_QualifiedName_(builder);
      
        _init_prod__History_ShellCommand__lit_history_(builder);
      
        _init_prod__Quit_ShellCommand__lit_quit_(builder);
      
        _init_prod__Undeclare_ShellCommand__lit_undeclare_layouts_LAYOUTLIST_name_QualifiedName_(builder);
      
        _init_prod__Help_ShellCommand__lit_help_(builder);
      
        _init_prod__Unimport_ShellCommand__lit_unimport_layouts_LAYOUTLIST_name_QualifiedName_(builder);
      
    }
  }
	
  protected static class StringMiddle {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__Mid_StringMiddle__mid_MidStringChars_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(4236, 0, "MidStringChars", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Mid_StringMiddle__mid_MidStringChars_, tmp);
	}
    protected static final void _init_prod__Interpolated_StringMiddle__mid_MidStringChars_layouts_LAYOUTLIST_expression_Expression_layouts_LAYOUTLIST_tail_StringMiddle_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(4246, 4, "StringMiddle", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(4244, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(4242, 2, "Expression", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(4240, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(4238, 0, "MidStringChars", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Interpolated_StringMiddle__mid_MidStringChars_layouts_LAYOUTLIST_expression_Expression_layouts_LAYOUTLIST_tail_StringMiddle_, tmp);
	}
    protected static final void _init_prod__Template_StringMiddle__mid_MidStringChars_layouts_LAYOUTLIST_template_StringTemplate_layouts_LAYOUTLIST_tail_StringMiddle_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(4256, 4, "StringMiddle", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(4254, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(4252, 2, "StringTemplate", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(4250, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(4248, 0, "MidStringChars", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Template_StringMiddle__mid_MidStringChars_layouts_LAYOUTLIST_template_StringTemplate_layouts_LAYOUTLIST_tail_StringMiddle_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__Mid_StringMiddle__mid_MidStringChars_(builder);
      
        _init_prod__Interpolated_StringMiddle__mid_MidStringChars_layouts_LAYOUTLIST_expression_Expression_layouts_LAYOUTLIST_tail_StringMiddle_(builder);
      
        _init_prod__Template_StringMiddle__mid_MidStringChars_layouts_LAYOUTLIST_template_StringTemplate_layouts_LAYOUTLIST_tail_StringMiddle_(builder);
      
    }
  }
	
  protected static class URLChars {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__URLChars__iter_star__char_class___range__0_8_range__11_12_range__14_31_range__33_59_range__61_123_range__125_16777215_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new ListStackNode<IConstructor>(4232, 0, regular__iter_star__char_class___range__0_8_range__11_12_range__14_31_range__33_59_range__61_123_range__125_16777215, new CharStackNode<IConstructor>(4234, 0, new int[][]{{0,8},{11,12},{14,31},{33,59},{61,123},{125,16777215}}, null, null), false, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__URLChars__iter_star__char_class___range__0_8_range__11_12_range__14_31_range__33_59_range__61_123_range__125_16777215_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__URLChars__iter_star__char_class___range__0_8_range__11_12_range__14_31_range__33_59_range__61_123_range__125_16777215_(builder);
      
    }
  }
	
  protected static class QualifiedName {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__Default_QualifiedName__conditional__names_iter_seps__Name__layouts_LAYOUTLIST_lit___58_58_layouts_LAYOUTLIST__not_follow__lit___58_58_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new SeparatedListStackNode<IConstructor>(4258, 0, regular__iter_seps__Name__layouts_LAYOUTLIST_lit___58_58_layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(4260, 0, "Name", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(4262, 1, "layouts_LAYOUTLIST", null, null), new LiteralStackNode<IConstructor>(4264, 2, prod__lit___58_58__char_class___range__58_58_char_class___range__58_58_, new int[] {58,58}, null, null), new NonTerminalStackNode<IConstructor>(4266, 3, "layouts_LAYOUTLIST", null, null)}, true, null, new ICompletionFilter[] {new StringFollowRestriction(new int[] {58,58})});
      builder.addAlternative(ObjectRascalRascal.prod__Default_QualifiedName__conditional__names_iter_seps__Name__layouts_LAYOUTLIST_lit___58_58_layouts_LAYOUTLIST__not_follow__lit___58_58_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__Default_QualifiedName__conditional__names_iter_seps__Name__layouts_LAYOUTLIST_lit___58_58_layouts_LAYOUTLIST__not_follow__lit___58_58_(builder);
      
    }
  }
	
  protected static class TimeZonePart {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__TimeZonePart__char_class___range__43_43_range__45_45_char_class___range__48_49_char_class___range__48_57_lit___58_char_class___range__48_53_char_class___range__48_57_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[6];
      
      tmp[5] = new CharStackNode<IConstructor>(4278, 5, new int[][]{{48,57}}, null, null);
      tmp[4] = new CharStackNode<IConstructor>(4276, 4, new int[][]{{48,53}}, null, null);
      tmp[3] = new LiteralStackNode<IConstructor>(4274, 3, prod__lit___58__char_class___range__58_58_, new int[] {58}, null, null);
      tmp[2] = new CharStackNode<IConstructor>(4272, 2, new int[][]{{48,57}}, null, null);
      tmp[1] = new CharStackNode<IConstructor>(4270, 1, new int[][]{{48,49}}, null, null);
      tmp[0] = new CharStackNode<IConstructor>(4268, 0, new int[][]{{43,43},{45,45}}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__TimeZonePart__char_class___range__43_43_range__45_45_char_class___range__48_49_char_class___range__48_57_lit___58_char_class___range__48_53_char_class___range__48_57_, tmp);
	}
    protected static final void _init_prod__TimeZonePart__char_class___range__43_43_range__45_45_char_class___range__48_49_char_class___range__48_57_char_class___range__48_53_char_class___range__48_57_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new CharStackNode<IConstructor>(4288, 4, new int[][]{{48,57}}, null, null);
      tmp[3] = new CharStackNode<IConstructor>(4286, 3, new int[][]{{48,53}}, null, null);
      tmp[2] = new CharStackNode<IConstructor>(4284, 2, new int[][]{{48,57}}, null, null);
      tmp[1] = new CharStackNode<IConstructor>(4282, 1, new int[][]{{48,49}}, null, null);
      tmp[0] = new CharStackNode<IConstructor>(4280, 0, new int[][]{{43,43},{45,45}}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__TimeZonePart__char_class___range__43_43_range__45_45_char_class___range__48_49_char_class___range__48_57_char_class___range__48_53_char_class___range__48_57_, tmp);
	}
    protected static final void _init_prod__TimeZonePart__char_class___range__43_43_range__45_45_char_class___range__48_49_char_class___range__48_57_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new CharStackNode<IConstructor>(4294, 2, new int[][]{{48,57}}, null, null);
      tmp[1] = new CharStackNode<IConstructor>(4292, 1, new int[][]{{48,49}}, null, null);
      tmp[0] = new CharStackNode<IConstructor>(4290, 0, new int[][]{{43,43},{45,45}}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__TimeZonePart__char_class___range__43_43_range__45_45_char_class___range__48_49_char_class___range__48_57_, tmp);
	}
    protected static final void _init_prod__TimeZonePart__lit_Z_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(4296, 0, prod__lit_Z__char_class___range__90_90_, new int[] {90}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__TimeZonePart__lit_Z_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__TimeZonePart__char_class___range__43_43_range__45_45_char_class___range__48_49_char_class___range__48_57_lit___58_char_class___range__48_53_char_class___range__48_57_(builder);
      
        _init_prod__TimeZonePart__char_class___range__43_43_range__45_45_char_class___range__48_49_char_class___range__48_57_char_class___range__48_53_char_class___range__48_57_(builder);
      
        _init_prod__TimeZonePart__char_class___range__43_43_range__45_45_char_class___range__48_49_char_class___range__48_57_(builder);
      
        _init_prod__TimeZonePart__lit_Z_(builder);
      
    }
  }
	
  protected static class PreStringChars {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__PreStringChars__char_class___range__34_34_iter_star__StringCharacter_char_class___range__60_60__tag__category___67_111_110_115_116_97_110_116(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new CharStackNode<IConstructor>(4312, 2, new int[][]{{60,60}}, null, null);
      tmp[1] = new ListStackNode<IConstructor>(4308, 1, regular__iter_star__StringCharacter, new NonTerminalStackNode<IConstructor>(4310, 0, "StringCharacter", null, null), false, null, null);
      tmp[0] = new CharStackNode<IConstructor>(4306, 0, new int[][]{{34,34}}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__PreStringChars__char_class___range__34_34_iter_star__StringCharacter_char_class___range__60_60__tag__category___67_111_110_115_116_97_110_116, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__PreStringChars__char_class___range__34_34_iter_star__StringCharacter_char_class___range__60_60__tag__category___67_111_110_115_116_97_110_116(builder);
      
    }
  }
	
  protected static class Visit {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__GivenStrategy_Visit__strategy_Strategy_layouts_LAYOUTLIST_lit_visit_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_subject_Expression_layouts_LAYOUTLIST_lit___41_layouts_LAYOUTLIST_lit___123_layouts_LAYOUTLIST_cases_iter_seps__Case__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[15];
      
      tmp[14] = new LiteralStackNode<IConstructor>(4364, 14, prod__lit___125__char_class___range__125_125_, new int[] {125}, null, null);
      tmp[13] = new NonTerminalStackNode<IConstructor>(4362, 13, "layouts_LAYOUTLIST", null, null);
      tmp[12] = new SeparatedListStackNode<IConstructor>(4356, 12, regular__iter_seps__Case__layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(4358, 0, "Case", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(4360, 1, "layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[11] = new NonTerminalStackNode<IConstructor>(4354, 11, "layouts_LAYOUTLIST", null, null);
      tmp[10] = new LiteralStackNode<IConstructor>(4352, 10, prod__lit___123__char_class___range__123_123_, new int[] {123}, null, null);
      tmp[9] = new NonTerminalStackNode<IConstructor>(4350, 9, "layouts_LAYOUTLIST", null, null);
      tmp[8] = new LiteralStackNode<IConstructor>(4348, 8, prod__lit___41__char_class___range__41_41_, new int[] {41}, null, null);
      tmp[7] = new NonTerminalStackNode<IConstructor>(4346, 7, "layouts_LAYOUTLIST", null, null);
      tmp[6] = new NonTerminalStackNode<IConstructor>(4344, 6, "Expression", null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(4342, 5, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode<IConstructor>(4340, 4, prod__lit___40__char_class___range__40_40_, new int[] {40}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(4338, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(4336, 2, prod__lit_visit__char_class___range__118_118_char_class___range__105_105_char_class___range__115_115_char_class___range__105_105_char_class___range__116_116_, new int[] {118,105,115,105,116}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(4334, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(4332, 0, "Strategy", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__GivenStrategy_Visit__strategy_Strategy_layouts_LAYOUTLIST_lit_visit_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_subject_Expression_layouts_LAYOUTLIST_lit___41_layouts_LAYOUTLIST_lit___123_layouts_LAYOUTLIST_cases_iter_seps__Case__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_, tmp);
	}
    protected static final void _init_prod__DefaultStrategy_Visit__lit_visit_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_subject_Expression_layouts_LAYOUTLIST_lit___41_layouts_LAYOUTLIST_lit___123_layouts_LAYOUTLIST_cases_iter_seps__Case__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[13];
      
      tmp[12] = new LiteralStackNode<IConstructor>(4394, 12, prod__lit___125__char_class___range__125_125_, new int[] {125}, null, null);
      tmp[11] = new NonTerminalStackNode<IConstructor>(4392, 11, "layouts_LAYOUTLIST", null, null);
      tmp[10] = new SeparatedListStackNode<IConstructor>(4386, 10, regular__iter_seps__Case__layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(4388, 0, "Case", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(4390, 1, "layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[9] = new NonTerminalStackNode<IConstructor>(4384, 9, "layouts_LAYOUTLIST", null, null);
      tmp[8] = new LiteralStackNode<IConstructor>(4382, 8, prod__lit___123__char_class___range__123_123_, new int[] {123}, null, null);
      tmp[7] = new NonTerminalStackNode<IConstructor>(4380, 7, "layouts_LAYOUTLIST", null, null);
      tmp[6] = new LiteralStackNode<IConstructor>(4378, 6, prod__lit___41__char_class___range__41_41_, new int[] {41}, null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(4376, 5, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new NonTerminalStackNode<IConstructor>(4374, 4, "Expression", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(4372, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(4370, 2, prod__lit___40__char_class___range__40_40_, new int[] {40}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(4368, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(4366, 0, prod__lit_visit__char_class___range__118_118_char_class___range__105_105_char_class___range__115_115_char_class___range__105_105_char_class___range__116_116_, new int[] {118,105,115,105,116}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__DefaultStrategy_Visit__lit_visit_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_subject_Expression_layouts_LAYOUTLIST_lit___41_layouts_LAYOUTLIST_lit___123_layouts_LAYOUTLIST_cases_iter_seps__Case__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__GivenStrategy_Visit__strategy_Strategy_layouts_LAYOUTLIST_lit_visit_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_subject_Expression_layouts_LAYOUTLIST_lit___41_layouts_LAYOUTLIST_lit___123_layouts_LAYOUTLIST_cases_iter_seps__Case__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_(builder);
      
        _init_prod__DefaultStrategy_Visit__lit_visit_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_subject_Expression_layouts_LAYOUTLIST_lit___41_layouts_LAYOUTLIST_lit___123_layouts_LAYOUTLIST_cases_iter_seps__Case__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_(builder);
      
    }
  }
	
  protected static class Command {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__Declaration_Command__declaration_Declaration_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(4318, 0, "Declaration", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Declaration_Command__declaration_Declaration_, tmp);
	}
    protected static final void _init_prod__Statement_Command__statement_Statement_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(4320, 0, "Statement", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Statement_Command__statement_Statement_, tmp);
	}
    protected static final void _init_prod__Expression_Command__expression_Expression_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(4322, 0, "Expression", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Expression_Command__expression_Expression_, tmp);
	}
    protected static final void _init_prod__Import_Command__imported_Import_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(4324, 0, "Import", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Import_Command__imported_Import_, tmp);
	}
    protected static final void _init_prod__Shell_Command__lit___58_layouts_LAYOUTLIST_command_ShellCommand_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode<IConstructor>(4330, 2, "ShellCommand", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(4328, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(4326, 0, prod__lit___58__char_class___range__58_58_, new int[] {58}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Shell_Command__lit___58_layouts_LAYOUTLIST_command_ShellCommand_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__Declaration_Command__declaration_Declaration_(builder);
      
        _init_prod__Statement_Command__statement_Statement_(builder);
      
        _init_prod__Expression_Command__expression_Expression_(builder);
      
        _init_prod__Import_Command__imported_Import_(builder);
      
        _init_prod__Shell_Command__lit___58_layouts_LAYOUTLIST_command_ShellCommand_(builder);
      
    }
  }
	
  protected static class ProtocolTail {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__Post_ProtocolTail__post_PostProtocolChars_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(4406, 0, "PostProtocolChars", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Post_ProtocolTail__post_PostProtocolChars_, tmp);
	}
    protected static final void _init_prod__Mid_ProtocolTail__mid_MidProtocolChars_layouts_LAYOUTLIST_expression_Expression_layouts_LAYOUTLIST_tail_ProtocolTail_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(4416, 4, "ProtocolTail", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(4414, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(4412, 2, "Expression", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(4410, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(4408, 0, "MidProtocolChars", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Mid_ProtocolTail__mid_MidProtocolChars_layouts_LAYOUTLIST_expression_Expression_layouts_LAYOUTLIST_tail_ProtocolTail_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__Post_ProtocolTail__post_PostProtocolChars_(builder);
      
        _init_prod__Mid_ProtocolTail__mid_MidProtocolChars_layouts_LAYOUTLIST_expression_Expression_layouts_LAYOUTLIST_tail_ProtocolTail_(builder);
      
    }
  }
	
  protected static class NamedBackslash {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__NamedBackslash__conditional__char_class___range__92_92__not_follow__char_class___range__60_60_range__62_62_range__92_92_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new CharStackNode<IConstructor>(4452, 0, new int[][]{{92,92}}, null, new ICompletionFilter[] {new CharFollowRestriction(new int[][]{{60,60},{62,62},{92,92}})});
      builder.addAlternative(ObjectRascalRascal.prod__NamedBackslash__conditional__char_class___range__92_92__not_follow__char_class___range__60_60_range__62_62_range__92_92_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__NamedBackslash__conditional__char_class___range__92_92__not_follow__char_class___range__60_60_range__62_62_range__92_92_(builder);
      
    }
  }
	
  protected static class Visibility {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__Private_Visibility__lit_private_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(4454, 0, prod__lit_private__char_class___range__112_112_char_class___range__114_114_char_class___range__105_105_char_class___range__118_118_char_class___range__97_97_char_class___range__116_116_char_class___range__101_101_, new int[] {112,114,105,118,97,116,101}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Private_Visibility__lit_private_, tmp);
	}
    protected static final void _init_prod__Default_Visibility__(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new EpsilonStackNode<IConstructor>(4456, 0);
      builder.addAlternative(ObjectRascalRascal.prod__Default_Visibility__, tmp);
	}
    protected static final void _init_prod__Public_Visibility__lit_public_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(4458, 0, prod__lit_public__char_class___range__112_112_char_class___range__117_117_char_class___range__98_98_char_class___range__108_108_char_class___range__105_105_char_class___range__99_99_, new int[] {112,117,98,108,105,99}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Public_Visibility__lit_public_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__Private_Visibility__lit_private_(builder);
      
        _init_prod__Default_Visibility__(builder);
      
        _init_prod__Public_Visibility__lit_public_(builder);
      
    }
  }
	
  protected static class PostPathChars {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__PostPathChars__lit___62_URLChars_lit___124_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new LiteralStackNode<IConstructor>(4464, 2, prod__lit___124__char_class___range__124_124_, new int[] {124}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(4462, 1, "URLChars", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(4460, 0, prod__lit___62__char_class___range__62_62_, new int[] {62}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__PostPathChars__lit___62_URLChars_lit___124_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__PostPathChars__lit___62_URLChars_lit___124_(builder);
      
    }
  }
	
  protected static class StringLiteral {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__Interpolated_StringLiteral__pre_PreStringChars_layouts_LAYOUTLIST_expression_Expression_layouts_LAYOUTLIST_tail_StringTail_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(4474, 4, "StringTail", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(4472, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(4470, 2, "Expression", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(4468, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(4466, 0, "PreStringChars", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Interpolated_StringLiteral__pre_PreStringChars_layouts_LAYOUTLIST_expression_Expression_layouts_LAYOUTLIST_tail_StringTail_, tmp);
	}
    protected static final void _init_prod__Template_StringLiteral__pre_PreStringChars_layouts_LAYOUTLIST_template_StringTemplate_layouts_LAYOUTLIST_tail_StringTail_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(4484, 4, "StringTail", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(4482, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(4480, 2, "StringTemplate", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(4478, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(4476, 0, "PreStringChars", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Template_StringLiteral__pre_PreStringChars_layouts_LAYOUTLIST_template_StringTemplate_layouts_LAYOUTLIST_tail_StringTail_, tmp);
	}
    protected static final void _init_prod__NonInterpolated_StringLiteral__constant_StringConstant_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(4486, 0, "StringConstant", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__NonInterpolated_StringLiteral__constant_StringConstant_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__Interpolated_StringLiteral__pre_PreStringChars_layouts_LAYOUTLIST_expression_Expression_layouts_LAYOUTLIST_tail_StringTail_(builder);
      
        _init_prod__Template_StringLiteral__pre_PreStringChars_layouts_LAYOUTLIST_template_StringTemplate_layouts_LAYOUTLIST_tail_StringTail_(builder);
      
        _init_prod__NonInterpolated_StringLiteral__constant_StringConstant_(builder);
      
    }
  }
	
  protected static class Renamings {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__Default_Renamings__lit_renaming_layouts_LAYOUTLIST_renamings_iter_seps__Renaming__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new SeparatedListStackNode<IConstructor>(4530, 2, regular__iter_seps__Renaming__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(4532, 0, "Renaming", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(4534, 1, "layouts_LAYOUTLIST", null, null), new LiteralStackNode<IConstructor>(4536, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode<IConstructor>(4538, 3, "layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(4528, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(4526, 0, prod__lit_renaming__char_class___range__114_114_char_class___range__101_101_char_class___range__110_110_char_class___range__97_97_char_class___range__109_109_char_class___range__105_105_char_class___range__110_110_char_class___range__103_103_, new int[] {114,101,110,97,109,105,110,103}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Default_Renamings__lit_renaming_layouts_LAYOUTLIST_renamings_iter_seps__Renaming__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__Default_Renamings__lit_renaming_layouts_LAYOUTLIST_renamings_iter_seps__Renaming__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_(builder);
      
    }
  }
	
  protected static class layouts_$QUOTES {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__layouts_$QUOTES__conditional__iter_star__char_class___range__9_10_range__13_13_range__32_32__not_follow__char_class___range__9_10_range__13_13_range__32_32_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new ListStackNode<IConstructor>(4544, 0, regular__iter_star__char_class___range__9_10_range__13_13_range__32_32, new CharStackNode<IConstructor>(4546, 0, new int[][]{{9,10},{13,13},{32,32}}, null, null), false, null, new ICompletionFilter[] {new CharFollowRestriction(new int[][]{{9,10},{13,13},{32,32}})});
      builder.addAlternative(ObjectRascalRascal.prod__layouts_$QUOTES__conditional__iter_star__char_class___range__9_10_range__13_13_range__32_32__not_follow__char_class___range__9_10_range__13_13_range__32_32_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__layouts_$QUOTES__conditional__iter_star__char_class___range__9_10_range__13_13_range__32_32__not_follow__char_class___range__9_10_range__13_13_range__32_32_(builder);
      
    }
  }
	
  protected static class Statement {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__Expression_Statement__expression_Expression_layouts_LAYOUTLIST_lit___59_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new LiteralStackNode<IConstructor>(4574, 2, prod__lit___59__char_class___range__59_59_, new int[] {59}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(4572, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(4570, 0, "Expression", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Expression_Statement__expression_Expression_layouts_LAYOUTLIST_lit___59_, tmp);
	}
    protected static final void _init_prod__Filter_Statement__lit_filter_layouts_LAYOUTLIST_lit___59_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[0] = new LiteralStackNode<IConstructor>(4576, 0, prod__lit_filter__char_class___range__102_102_char_class___range__105_105_char_class___range__108_108_char_class___range__116_116_char_class___range__101_101_char_class___range__114_114_, new int[] {102,105,108,116,101,114}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(4578, 1, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(4580, 2, prod__lit___59__char_class___range__59_59_, new int[] {59}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Filter_Statement__lit_filter_layouts_LAYOUTLIST_lit___59_, tmp);
	}
    protected static final void _init_prod__TryFinally_Statement__lit_try_layouts_LAYOUTLIST_body_Statement_layouts_LAYOUTLIST_handlers_iter_seps__Catch__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit_finally_layouts_LAYOUTLIST_finallyBody_Statement_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[9];
      
      tmp[8] = new NonTerminalStackNode<IConstructor>(4602, 8, "Statement", null, null);
      tmp[7] = new NonTerminalStackNode<IConstructor>(4600, 7, "layouts_LAYOUTLIST", null, null);
      tmp[6] = new LiteralStackNode<IConstructor>(4598, 6, prod__lit_finally__char_class___range__102_102_char_class___range__105_105_char_class___range__110_110_char_class___range__97_97_char_class___range__108_108_char_class___range__108_108_char_class___range__121_121_, new int[] {102,105,110,97,108,108,121}, null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(4596, 5, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new SeparatedListStackNode<IConstructor>(4590, 4, regular__iter_seps__Catch__layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(4592, 0, "Catch", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(4594, 1, "layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(4588, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(4586, 2, "Statement", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(4584, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(4582, 0, prod__lit_try__char_class___range__116_116_char_class___range__114_114_char_class___range__121_121_, new int[] {116,114,121}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__TryFinally_Statement__lit_try_layouts_LAYOUTLIST_body_Statement_layouts_LAYOUTLIST_handlers_iter_seps__Catch__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit_finally_layouts_LAYOUTLIST_finallyBody_Statement_, tmp);
	}
    protected static final void _init_prod__Insert_Statement__lit_insert_layouts_LAYOUTLIST_dataTarget_DataTarget_layouts_LAYOUTLIST_statement_Statement__assoc__non_assoc(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(4730, 4, "Statement", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(4728, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(4726, 2, "DataTarget", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(4724, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(4722, 0, prod__lit_insert__char_class___range__105_105_char_class___range__110_110_char_class___range__115_115_char_class___range__101_101_char_class___range__114_114_char_class___range__116_116_, new int[] {105,110,115,101,114,116}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Insert_Statement__lit_insert_layouts_LAYOUTLIST_dataTarget_DataTarget_layouts_LAYOUTLIST_statement_Statement__assoc__non_assoc, tmp);
	}
    protected static final void _init_prod__Try_Statement__lit_try_layouts_LAYOUTLIST_body_Statement_layouts_LAYOUTLIST_handlers_iter_seps__Catch__layouts_LAYOUTLIST__assoc__non_assoc(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new SeparatedListStackNode<IConstructor>(4612, 4, regular__iter_seps__Catch__layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(4614, 0, "Catch", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(4616, 1, "layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(4610, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(4608, 2, "Statement", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(4606, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(4604, 0, prod__lit_try__char_class___range__116_116_char_class___range__114_114_char_class___range__121_121_, new int[] {116,114,121}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Try_Statement__lit_try_layouts_LAYOUTLIST_body_Statement_layouts_LAYOUTLIST_handlers_iter_seps__Catch__layouts_LAYOUTLIST__assoc__non_assoc, tmp);
	}
    protected static final void _init_prod__Visit_Statement__label_Label_layouts_LAYOUTLIST_visit_Visit_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(4620, 0, "Label", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(4622, 1, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(4624, 2, "Visit", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Visit_Statement__label_Label_layouts_LAYOUTLIST_visit_Visit_, tmp);
	}
    protected static final void _init_prod__EmptyStatement_Statement__lit___59_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(4618, 0, prod__lit___59__char_class___range__59_59_, new int[] {59}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__EmptyStatement_Statement__lit___59_, tmp);
	}
    protected static final void _init_prod__Break_Statement__lit_break_layouts_LAYOUTLIST_target_Target_layouts_LAYOUTLIST_lit___59_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new LiteralStackNode<IConstructor>(4634, 4, prod__lit___59__char_class___range__59_59_, new int[] {59}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(4632, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(4630, 2, "Target", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(4628, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(4626, 0, prod__lit_break__char_class___range__98_98_char_class___range__114_114_char_class___range__101_101_char_class___range__97_97_char_class___range__107_107_, new int[] {98,114,101,97,107}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Break_Statement__lit_break_layouts_LAYOUTLIST_target_Target_layouts_LAYOUTLIST_lit___59_, tmp);
	}
    protected static final void _init_prod__Assignment_Statement__assignable_Assignable_layouts_LAYOUTLIST_operator_Assignment_layouts_LAYOUTLIST_statement_Statement_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(4644, 4, "Statement", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(4642, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(4640, 2, "Assignment", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(4638, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(4636, 0, "Assignable", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Assignment_Statement__assignable_Assignable_layouts_LAYOUTLIST_operator_Assignment_layouts_LAYOUTLIST_statement_Statement_, tmp);
	}
    protected static final void _init_prod__For_Statement__label_Label_layouts_LAYOUTLIST_lit_for_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_generators_iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_layouts_LAYOUTLIST_body_Statement_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[11];
      
      tmp[10] = new NonTerminalStackNode<IConstructor>(4674, 10, "Statement", null, null);
      tmp[9] = new NonTerminalStackNode<IConstructor>(4672, 9, "layouts_LAYOUTLIST", null, null);
      tmp[8] = new LiteralStackNode<IConstructor>(4670, 8, prod__lit___41__char_class___range__41_41_, new int[] {41}, null, null);
      tmp[7] = new NonTerminalStackNode<IConstructor>(4668, 7, "layouts_LAYOUTLIST", null, null);
      tmp[6] = new SeparatedListStackNode<IConstructor>(4658, 6, regular__iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(4660, 0, "Expression", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(4662, 1, "layouts_LAYOUTLIST", null, null), new LiteralStackNode<IConstructor>(4664, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode<IConstructor>(4666, 3, "layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(4656, 5, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode<IConstructor>(4654, 4, prod__lit___40__char_class___range__40_40_, new int[] {40}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(4652, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(4650, 2, prod__lit_for__char_class___range__102_102_char_class___range__111_111_char_class___range__114_114_, new int[] {102,111,114}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(4648, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(4646, 0, "Label", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__For_Statement__label_Label_layouts_LAYOUTLIST_lit_for_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_generators_iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_layouts_LAYOUTLIST_body_Statement_, tmp);
	}
    protected static final void _init_prod__GlobalDirective_Statement__lit_global_layouts_LAYOUTLIST_type_Type_layouts_LAYOUTLIST_names_iter_seps__QualifiedName__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___59_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[7];
      
      tmp[6] = new LiteralStackNode<IConstructor>(4696, 6, prod__lit___59__char_class___range__59_59_, new int[] {59}, null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(4694, 5, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new SeparatedListStackNode<IConstructor>(4684, 4, regular__iter_seps__QualifiedName__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(4686, 0, "QualifiedName", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(4688, 1, "layouts_LAYOUTLIST", null, null), new LiteralStackNode<IConstructor>(4690, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode<IConstructor>(4692, 3, "layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(4682, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(4680, 2, "Type", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(4678, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(4676, 0, prod__lit_global__char_class___range__103_103_char_class___range__108_108_char_class___range__111_111_char_class___range__98_98_char_class___range__97_97_char_class___range__108_108_, new int[] {103,108,111,98,97,108}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__GlobalDirective_Statement__lit_global_layouts_LAYOUTLIST_type_Type_layouts_LAYOUTLIST_names_iter_seps__QualifiedName__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___59_, tmp);
	}
    protected static final void _init_prod__AssertWithMessage_Statement__lit_assert_layouts_LAYOUTLIST_expression_Expression_layouts_LAYOUTLIST_lit___58_layouts_LAYOUTLIST_message_Expression_layouts_LAYOUTLIST_lit___59_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[9];
      
      tmp[8] = new LiteralStackNode<IConstructor>(4714, 8, prod__lit___59__char_class___range__59_59_, new int[] {59}, null, null);
      tmp[7] = new NonTerminalStackNode<IConstructor>(4712, 7, "layouts_LAYOUTLIST", null, null);
      tmp[6] = new NonTerminalStackNode<IConstructor>(4710, 6, "Expression", null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(4708, 5, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode<IConstructor>(4706, 4, prod__lit___58__char_class___range__58_58_, new int[] {58}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(4704, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(4702, 2, "Expression", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(4700, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(4698, 0, prod__lit_assert__char_class___range__97_97_char_class___range__115_115_char_class___range__115_115_char_class___range__101_101_char_class___range__114_114_char_class___range__116_116_, new int[] {97,115,115,101,114,116}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__AssertWithMessage_Statement__lit_assert_layouts_LAYOUTLIST_expression_Expression_layouts_LAYOUTLIST_lit___58_layouts_LAYOUTLIST_message_Expression_layouts_LAYOUTLIST_lit___59_, tmp);
	}
    protected static final void _init_prod__While_Statement__label_Label_layouts_LAYOUTLIST_lit_while_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_conditions_iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_layouts_LAYOUTLIST_body_Statement_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[11];
      
      tmp[10] = new NonTerminalStackNode<IConstructor>(4776, 10, "Statement", null, null);
      tmp[9] = new NonTerminalStackNode<IConstructor>(4774, 9, "layouts_LAYOUTLIST", null, null);
      tmp[8] = new LiteralStackNode<IConstructor>(4772, 8, prod__lit___41__char_class___range__41_41_, new int[] {41}, null, null);
      tmp[7] = new NonTerminalStackNode<IConstructor>(4770, 7, "layouts_LAYOUTLIST", null, null);
      tmp[6] = new SeparatedListStackNode<IConstructor>(4760, 6, regular__iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(4762, 0, "Expression", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(4764, 1, "layouts_LAYOUTLIST", null, null), new LiteralStackNode<IConstructor>(4766, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode<IConstructor>(4768, 3, "layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(4758, 5, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode<IConstructor>(4756, 4, prod__lit___40__char_class___range__40_40_, new int[] {40}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(4754, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(4752, 2, prod__lit_while__char_class___range__119_119_char_class___range__104_104_char_class___range__105_105_char_class___range__108_108_char_class___range__101_101_, new int[] {119,104,105,108,101}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(4750, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(4748, 0, "Label", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__While_Statement__label_Label_layouts_LAYOUTLIST_lit_while_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_conditions_iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_layouts_LAYOUTLIST_body_Statement_, tmp);
	}
    protected static final void _init_prod__Assert_Statement__lit_assert_layouts_LAYOUTLIST_expression_Expression_layouts_LAYOUTLIST_lit___59_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new LiteralStackNode<IConstructor>(4786, 4, prod__lit___59__char_class___range__59_59_, new int[] {59}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(4784, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(4782, 2, "Expression", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(4780, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(4778, 0, prod__lit_assert__char_class___range__97_97_char_class___range__115_115_char_class___range__115_115_char_class___range__101_101_char_class___range__114_114_char_class___range__116_116_, new int[] {97,115,115,101,114,116}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Assert_Statement__lit_assert_layouts_LAYOUTLIST_expression_Expression_layouts_LAYOUTLIST_lit___59_, tmp);
	}
    protected static final void _init_prod__FunctionDeclaration_Statement__functionDeclaration_FunctionDeclaration_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(4992, 0, "FunctionDeclaration", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__FunctionDeclaration_Statement__functionDeclaration_FunctionDeclaration_, tmp);
	}
    protected static final void _init_prod__Return_Statement__lit_return_layouts_LAYOUTLIST_statement_Statement__assoc__non_assoc(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode<IConstructor>(4720, 2, "Statement", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(4718, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(4716, 0, prod__lit_return__char_class___range__114_114_char_class___range__101_101_char_class___range__116_116_char_class___range__117_117_char_class___range__114_114_char_class___range__110_110_, new int[] {114,101,116,117,114,110}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Return_Statement__lit_return_layouts_LAYOUTLIST_statement_Statement__assoc__non_assoc, tmp);
	}
    protected static final void _init_prod__Continue_Statement__lit_continue_layouts_LAYOUTLIST_target_Target_layouts_LAYOUTLIST_lit___59_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new LiteralStackNode<IConstructor>(4796, 4, prod__lit___59__char_class___range__59_59_, new int[] {59}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(4794, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(4792, 2, "Target", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(4790, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(4788, 0, prod__lit_continue__char_class___range__99_99_char_class___range__111_111_char_class___range__110_110_char_class___range__116_116_char_class___range__105_105_char_class___range__110_110_char_class___range__117_117_char_class___range__101_101_, new int[] {99,111,110,116,105,110,117,101}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Continue_Statement__lit_continue_layouts_LAYOUTLIST_target_Target_layouts_LAYOUTLIST_lit___59_, tmp);
	}
    protected static final void _init_prod__Fail_Statement__lit_fail_layouts_LAYOUTLIST_target_Target_layouts_LAYOUTLIST_lit___59_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new LiteralStackNode<IConstructor>(4836, 4, prod__lit___59__char_class___range__59_59_, new int[] {59}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(4834, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(4832, 2, "Target", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(4830, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(4828, 0, prod__lit_fail__char_class___range__102_102_char_class___range__97_97_char_class___range__105_105_char_class___range__108_108_, new int[] {102,97,105,108}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Fail_Statement__lit_fail_layouts_LAYOUTLIST_target_Target_layouts_LAYOUTLIST_lit___59_, tmp);
	}
    protected static final void _init_prod__Append_Statement__lit_append_layouts_LAYOUTLIST_dataTarget_DataTarget_layouts_LAYOUTLIST_statement_Statement__assoc__non_assoc(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(4740, 4, "Statement", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(4738, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(4736, 2, "DataTarget", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(4734, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(4732, 0, prod__lit_append__char_class___range__97_97_char_class___range__112_112_char_class___range__112_112_char_class___range__101_101_char_class___range__110_110_char_class___range__100_100_, new int[] {97,112,112,101,110,100}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Append_Statement__lit_append_layouts_LAYOUTLIST_dataTarget_DataTarget_layouts_LAYOUTLIST_statement_Statement__assoc__non_assoc, tmp);
	}
    protected static final void _init_prod__IfThen_Statement__label_Label_layouts_LAYOUTLIST_lit_if_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_conditions_iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_layouts_LAYOUTLIST_thenStatement_Statement_layouts_LAYOUTLIST_conditional__empty__not_follow__lit_else_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[13];
      
      tmp[12] = new EmptyStackNode<IConstructor>(4904, 12, regular__empty, null, new ICompletionFilter[] {new StringFollowRestriction(new int[] {101,108,115,101})});
      tmp[11] = new NonTerminalStackNode<IConstructor>(4902, 11, "layouts_LAYOUTLIST", null, null);
      tmp[10] = new NonTerminalStackNode<IConstructor>(4900, 10, "Statement", null, null);
      tmp[9] = new NonTerminalStackNode<IConstructor>(4898, 9, "layouts_LAYOUTLIST", null, null);
      tmp[8] = new LiteralStackNode<IConstructor>(4896, 8, prod__lit___41__char_class___range__41_41_, new int[] {41}, null, null);
      tmp[7] = new NonTerminalStackNode<IConstructor>(4894, 7, "layouts_LAYOUTLIST", null, null);
      tmp[6] = new SeparatedListStackNode<IConstructor>(4884, 6, regular__iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(4886, 0, "Expression", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(4888, 1, "layouts_LAYOUTLIST", null, null), new LiteralStackNode<IConstructor>(4890, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode<IConstructor>(4892, 3, "layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(4882, 5, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode<IConstructor>(4880, 4, prod__lit___40__char_class___range__40_40_, new int[] {40}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(4878, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(4876, 2, prod__lit_if__char_class___range__105_105_char_class___range__102_102_, new int[] {105,102}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(4874, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(4872, 0, "Label", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__IfThen_Statement__label_Label_layouts_LAYOUTLIST_lit_if_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_conditions_iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_layouts_LAYOUTLIST_thenStatement_Statement_layouts_LAYOUTLIST_conditional__empty__not_follow__lit_else_, tmp);
	}
    protected static final void _init_prod__Switch_Statement__label_Label_layouts_LAYOUTLIST_lit_switch_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_expression_Expression_layouts_LAYOUTLIST_lit___41_layouts_LAYOUTLIST_lit___123_layouts_LAYOUTLIST_cases_iter_seps__Case__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[15];
      
      tmp[14] = new LiteralStackNode<IConstructor>(4870, 14, prod__lit___125__char_class___range__125_125_, new int[] {125}, null, null);
      tmp[13] = new NonTerminalStackNode<IConstructor>(4868, 13, "layouts_LAYOUTLIST", null, null);
      tmp[12] = new SeparatedListStackNode<IConstructor>(4862, 12, regular__iter_seps__Case__layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(4864, 0, "Case", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(4866, 1, "layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[11] = new NonTerminalStackNode<IConstructor>(4860, 11, "layouts_LAYOUTLIST", null, null);
      tmp[10] = new LiteralStackNode<IConstructor>(4858, 10, prod__lit___123__char_class___range__123_123_, new int[] {123}, null, null);
      tmp[9] = new NonTerminalStackNode<IConstructor>(4856, 9, "layouts_LAYOUTLIST", null, null);
      tmp[8] = new LiteralStackNode<IConstructor>(4854, 8, prod__lit___41__char_class___range__41_41_, new int[] {41}, null, null);
      tmp[7] = new NonTerminalStackNode<IConstructor>(4852, 7, "layouts_LAYOUTLIST", null, null);
      tmp[6] = new NonTerminalStackNode<IConstructor>(4850, 6, "Expression", null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(4848, 5, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode<IConstructor>(4846, 4, prod__lit___40__char_class___range__40_40_, new int[] {40}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(4844, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(4842, 2, prod__lit_switch__char_class___range__115_115_char_class___range__119_119_char_class___range__105_105_char_class___range__116_116_char_class___range__99_99_char_class___range__104_104_, new int[] {115,119,105,116,99,104}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(4840, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(4838, 0, "Label", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Switch_Statement__label_Label_layouts_LAYOUTLIST_lit_switch_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_expression_Expression_layouts_LAYOUTLIST_lit___41_layouts_LAYOUTLIST_lit___123_layouts_LAYOUTLIST_cases_iter_seps__Case__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_, tmp);
	}
    protected static final void _init_prod__Solve_Statement__lit_solve_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_variables_iter_seps__QualifiedName__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_bound_Bound_layouts_LAYOUTLIST_lit___41_layouts_LAYOUTLIST_body_Statement_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[11];
      
      tmp[0] = new LiteralStackNode<IConstructor>(4798, 0, prod__lit_solve__char_class___range__115_115_char_class___range__111_111_char_class___range__108_108_char_class___range__118_118_char_class___range__101_101_, new int[] {115,111,108,118,101}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(4800, 1, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(4802, 2, prod__lit___40__char_class___range__40_40_, new int[] {40}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(4804, 3, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new SeparatedListStackNode<IConstructor>(4806, 4, regular__iter_seps__QualifiedName__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(4808, 0, "QualifiedName", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(4810, 1, "layouts_LAYOUTLIST", null, null), new LiteralStackNode<IConstructor>(4812, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode<IConstructor>(4814, 3, "layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(4816, 5, "layouts_LAYOUTLIST", null, null);
      tmp[6] = new NonTerminalStackNode<IConstructor>(4818, 6, "Bound", null, null);
      tmp[7] = new NonTerminalStackNode<IConstructor>(4820, 7, "layouts_LAYOUTLIST", null, null);
      tmp[8] = new LiteralStackNode<IConstructor>(4822, 8, prod__lit___41__char_class___range__41_41_, new int[] {41}, null, null);
      tmp[9] = new NonTerminalStackNode<IConstructor>(4824, 9, "layouts_LAYOUTLIST", null, null);
      tmp[10] = new NonTerminalStackNode<IConstructor>(4826, 10, "Statement", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Solve_Statement__lit_solve_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_variables_iter_seps__QualifiedName__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_bound_Bound_layouts_LAYOUTLIST_lit___41_layouts_LAYOUTLIST_body_Statement_, tmp);
	}
    protected static final void _init_prod__VariableDeclaration_Statement__declaration_LocalVariableDeclaration_layouts_LAYOUTLIST_lit___59_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new LiteralStackNode<IConstructor>(4998, 2, prod__lit___59__char_class___range__59_59_, new int[] {59}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(4996, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(4994, 0, "LocalVariableDeclaration", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__VariableDeclaration_Statement__declaration_LocalVariableDeclaration_layouts_LAYOUTLIST_lit___59_, tmp);
	}
    protected static final void _init_prod__NonEmptyBlock_Statement__label_Label_layouts_LAYOUTLIST_lit___123_layouts_LAYOUTLIST_statements_iter_seps__Statement__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[7];
      
      tmp[6] = new LiteralStackNode<IConstructor>(4922, 6, prod__lit___125__char_class___range__125_125_, new int[] {125}, null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(4920, 5, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new SeparatedListStackNode<IConstructor>(4914, 4, regular__iter_seps__Statement__layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(4916, 0, "Statement", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(4918, 1, "layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(4912, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(4910, 2, prod__lit___123__char_class___range__123_123_, new int[] {123}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(4908, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(4906, 0, "Label", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__NonEmptyBlock_Statement__label_Label_layouts_LAYOUTLIST_lit___123_layouts_LAYOUTLIST_statements_iter_seps__Statement__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_, tmp);
	}
    protected static final void _init_prod__DoWhile_Statement__label_Label_layouts_LAYOUTLIST_lit_do_layouts_LAYOUTLIST_body_Statement_layouts_LAYOUTLIST_lit_while_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_condition_Expression_layouts_LAYOUTLIST_lit___41_layouts_LAYOUTLIST_lit___59_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[15];
      
      tmp[14] = new LiteralStackNode<IConstructor>(4952, 14, prod__lit___59__char_class___range__59_59_, new int[] {59}, null, null);
      tmp[13] = new NonTerminalStackNode<IConstructor>(4950, 13, "layouts_LAYOUTLIST", null, null);
      tmp[12] = new LiteralStackNode<IConstructor>(4948, 12, prod__lit___41__char_class___range__41_41_, new int[] {41}, null, null);
      tmp[11] = new NonTerminalStackNode<IConstructor>(4946, 11, "layouts_LAYOUTLIST", null, null);
      tmp[10] = new NonTerminalStackNode<IConstructor>(4944, 10, "Expression", null, null);
      tmp[9] = new NonTerminalStackNode<IConstructor>(4942, 9, "layouts_LAYOUTLIST", null, null);
      tmp[8] = new LiteralStackNode<IConstructor>(4940, 8, prod__lit___40__char_class___range__40_40_, new int[] {40}, null, null);
      tmp[7] = new NonTerminalStackNode<IConstructor>(4938, 7, "layouts_LAYOUTLIST", null, null);
      tmp[6] = new LiteralStackNode<IConstructor>(4936, 6, prod__lit_while__char_class___range__119_119_char_class___range__104_104_char_class___range__105_105_char_class___range__108_108_char_class___range__101_101_, new int[] {119,104,105,108,101}, null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(4934, 5, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new NonTerminalStackNode<IConstructor>(4932, 4, "Statement", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(4930, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(4928, 2, prod__lit_do__char_class___range__100_100_char_class___range__111_111_, new int[] {100,111}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(4926, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(4924, 0, "Label", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__DoWhile_Statement__label_Label_layouts_LAYOUTLIST_lit_do_layouts_LAYOUTLIST_body_Statement_layouts_LAYOUTLIST_lit_while_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_condition_Expression_layouts_LAYOUTLIST_lit___41_layouts_LAYOUTLIST_lit___59_, tmp);
	}
    protected static final void _init_prod__IfThenElse_Statement__label_Label_layouts_LAYOUTLIST_lit_if_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_conditions_iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_layouts_LAYOUTLIST_thenStatement_Statement_layouts_LAYOUTLIST_lit_else_layouts_LAYOUTLIST_elseStatement_Statement_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[15];
      
      tmp[14] = new NonTerminalStackNode<IConstructor>(4990, 14, "Statement", null, null);
      tmp[13] = new NonTerminalStackNode<IConstructor>(4988, 13, "layouts_LAYOUTLIST", null, null);
      tmp[12] = new LiteralStackNode<IConstructor>(4986, 12, prod__lit_else__char_class___range__101_101_char_class___range__108_108_char_class___range__115_115_char_class___range__101_101_, new int[] {101,108,115,101}, null, null);
      tmp[11] = new NonTerminalStackNode<IConstructor>(4984, 11, "layouts_LAYOUTLIST", null, null);
      tmp[10] = new NonTerminalStackNode<IConstructor>(4982, 10, "Statement", null, null);
      tmp[9] = new NonTerminalStackNode<IConstructor>(4980, 9, "layouts_LAYOUTLIST", null, null);
      tmp[8] = new LiteralStackNode<IConstructor>(4978, 8, prod__lit___41__char_class___range__41_41_, new int[] {41}, null, null);
      tmp[7] = new NonTerminalStackNode<IConstructor>(4976, 7, "layouts_LAYOUTLIST", null, null);
      tmp[6] = new SeparatedListStackNode<IConstructor>(4966, 6, regular__iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(4968, 0, "Expression", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(4970, 1, "layouts_LAYOUTLIST", null, null), new LiteralStackNode<IConstructor>(4972, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode<IConstructor>(4974, 3, "layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(4964, 5, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode<IConstructor>(4962, 4, prod__lit___40__char_class___range__40_40_, new int[] {40}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(4960, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(4958, 2, prod__lit_if__char_class___range__105_105_char_class___range__102_102_, new int[] {105,102}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(4956, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(4954, 0, "Label", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__IfThenElse_Statement__label_Label_layouts_LAYOUTLIST_lit_if_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_conditions_iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_layouts_LAYOUTLIST_thenStatement_Statement_layouts_LAYOUTLIST_lit_else_layouts_LAYOUTLIST_elseStatement_Statement_, tmp);
	}
    protected static final void _init_prod__Throw_Statement__lit_throw_layouts_LAYOUTLIST_statement_Statement__assoc__non_assoc(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode<IConstructor>(4746, 2, "Statement", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(4744, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(4742, 0, prod__lit_throw__char_class___range__116_116_char_class___range__104_104_char_class___range__114_114_char_class___range__111_111_char_class___range__119_119_, new int[] {116,104,114,111,119}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Throw_Statement__lit_throw_layouts_LAYOUTLIST_statement_Statement__assoc__non_assoc, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__Expression_Statement__expression_Expression_layouts_LAYOUTLIST_lit___59_(builder);
      
        _init_prod__Filter_Statement__lit_filter_layouts_LAYOUTLIST_lit___59_(builder);
      
        _init_prod__TryFinally_Statement__lit_try_layouts_LAYOUTLIST_body_Statement_layouts_LAYOUTLIST_handlers_iter_seps__Catch__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit_finally_layouts_LAYOUTLIST_finallyBody_Statement_(builder);
      
        _init_prod__Insert_Statement__lit_insert_layouts_LAYOUTLIST_dataTarget_DataTarget_layouts_LAYOUTLIST_statement_Statement__assoc__non_assoc(builder);
      
        _init_prod__Try_Statement__lit_try_layouts_LAYOUTLIST_body_Statement_layouts_LAYOUTLIST_handlers_iter_seps__Catch__layouts_LAYOUTLIST__assoc__non_assoc(builder);
      
        _init_prod__Visit_Statement__label_Label_layouts_LAYOUTLIST_visit_Visit_(builder);
      
        _init_prod__EmptyStatement_Statement__lit___59_(builder);
      
        _init_prod__Break_Statement__lit_break_layouts_LAYOUTLIST_target_Target_layouts_LAYOUTLIST_lit___59_(builder);
      
        _init_prod__Assignment_Statement__assignable_Assignable_layouts_LAYOUTLIST_operator_Assignment_layouts_LAYOUTLIST_statement_Statement_(builder);
      
        _init_prod__For_Statement__label_Label_layouts_LAYOUTLIST_lit_for_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_generators_iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_layouts_LAYOUTLIST_body_Statement_(builder);
      
        _init_prod__GlobalDirective_Statement__lit_global_layouts_LAYOUTLIST_type_Type_layouts_LAYOUTLIST_names_iter_seps__QualifiedName__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___59_(builder);
      
        _init_prod__AssertWithMessage_Statement__lit_assert_layouts_LAYOUTLIST_expression_Expression_layouts_LAYOUTLIST_lit___58_layouts_LAYOUTLIST_message_Expression_layouts_LAYOUTLIST_lit___59_(builder);
      
        _init_prod__While_Statement__label_Label_layouts_LAYOUTLIST_lit_while_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_conditions_iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_layouts_LAYOUTLIST_body_Statement_(builder);
      
        _init_prod__Assert_Statement__lit_assert_layouts_LAYOUTLIST_expression_Expression_layouts_LAYOUTLIST_lit___59_(builder);
      
        _init_prod__FunctionDeclaration_Statement__functionDeclaration_FunctionDeclaration_(builder);
      
        _init_prod__Return_Statement__lit_return_layouts_LAYOUTLIST_statement_Statement__assoc__non_assoc(builder);
      
        _init_prod__Continue_Statement__lit_continue_layouts_LAYOUTLIST_target_Target_layouts_LAYOUTLIST_lit___59_(builder);
      
        _init_prod__Fail_Statement__lit_fail_layouts_LAYOUTLIST_target_Target_layouts_LAYOUTLIST_lit___59_(builder);
      
        _init_prod__Append_Statement__lit_append_layouts_LAYOUTLIST_dataTarget_DataTarget_layouts_LAYOUTLIST_statement_Statement__assoc__non_assoc(builder);
      
        _init_prod__IfThen_Statement__label_Label_layouts_LAYOUTLIST_lit_if_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_conditions_iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_layouts_LAYOUTLIST_thenStatement_Statement_layouts_LAYOUTLIST_conditional__empty__not_follow__lit_else_(builder);
      
        _init_prod__Switch_Statement__label_Label_layouts_LAYOUTLIST_lit_switch_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_expression_Expression_layouts_LAYOUTLIST_lit___41_layouts_LAYOUTLIST_lit___123_layouts_LAYOUTLIST_cases_iter_seps__Case__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_(builder);
      
        _init_prod__Solve_Statement__lit_solve_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_variables_iter_seps__QualifiedName__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_bound_Bound_layouts_LAYOUTLIST_lit___41_layouts_LAYOUTLIST_body_Statement_(builder);
      
        _init_prod__VariableDeclaration_Statement__declaration_LocalVariableDeclaration_layouts_LAYOUTLIST_lit___59_(builder);
      
        _init_prod__NonEmptyBlock_Statement__label_Label_layouts_LAYOUTLIST_lit___123_layouts_LAYOUTLIST_statements_iter_seps__Statement__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_(builder);
      
        _init_prod__DoWhile_Statement__label_Label_layouts_LAYOUTLIST_lit_do_layouts_LAYOUTLIST_body_Statement_layouts_LAYOUTLIST_lit_while_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_condition_Expression_layouts_LAYOUTLIST_lit___41_layouts_LAYOUTLIST_lit___59_(builder);
      
        _init_prod__IfThenElse_Statement__label_Label_layouts_LAYOUTLIST_lit_if_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_conditions_iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_layouts_LAYOUTLIST_thenStatement_Statement_layouts_LAYOUTLIST_lit_else_layouts_LAYOUTLIST_elseStatement_Statement_(builder);
      
        _init_prod__Throw_Statement__lit_throw_layouts_LAYOUTLIST_statement_Statement__assoc__non_assoc(builder);
      
    }
  }
	
  protected static class FunctionType {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__TypeArguments_FunctionType__type_Type_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_arguments_iter_star_seps__TypeArg__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[7];
      
      tmp[6] = new LiteralStackNode<IConstructor>(5082, 6, prod__lit___41__char_class___range__41_41_, new int[] {41}, null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(5080, 5, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new SeparatedListStackNode<IConstructor>(5070, 4, regular__iter_star_seps__TypeArg__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(5072, 0, "TypeArg", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(5074, 1, "layouts_LAYOUTLIST", null, null), new LiteralStackNode<IConstructor>(5076, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode<IConstructor>(5078, 3, "layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(5068, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(5066, 2, prod__lit___40__char_class___range__40_40_, new int[] {40}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(5064, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(5062, 0, "Type", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__TypeArguments_FunctionType__type_Type_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_arguments_iter_star_seps__TypeArg__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__TypeArguments_FunctionType__type_Type_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_arguments_iter_star_seps__TypeArg__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_(builder);
      
    }
  }
	
  protected static class Case {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__PatternWithAction_Case__lit_case_layouts_LAYOUTLIST_patternWithAction_PatternWithAction__tag__Foldable(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode<IConstructor>(5102, 2, "PatternWithAction", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(5100, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(5098, 0, prod__lit_case__char_class___range__99_99_char_class___range__97_97_char_class___range__115_115_char_class___range__101_101_, new int[] {99,97,115,101}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__PatternWithAction_Case__lit_case_layouts_LAYOUTLIST_patternWithAction_PatternWithAction__tag__Foldable, tmp);
	}
    protected static final void _init_prod__Default_Case__lit_default_layouts_LAYOUTLIST_lit___58_layouts_LAYOUTLIST_statement_Statement__tag__Foldable(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(5112, 4, "Statement", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(5110, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(5108, 2, prod__lit___58__char_class___range__58_58_, new int[] {58}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(5106, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(5104, 0, prod__lit_default__char_class___range__100_100_char_class___range__101_101_char_class___range__102_102_char_class___range__97_97_char_class___range__117_117_char_class___range__108_108_char_class___range__116_116_, new int[] {100,101,102,97,117,108,116}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Default_Case__lit_default_layouts_LAYOUTLIST_lit___58_layouts_LAYOUTLIST_statement_Statement__tag__Foldable, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__PatternWithAction_Case__lit_case_layouts_LAYOUTLIST_patternWithAction_PatternWithAction__tag__Foldable(builder);
      
        _init_prod__Default_Case__lit_default_layouts_LAYOUTLIST_lit___58_layouts_LAYOUTLIST_statement_Statement__tag__Foldable(builder);
      
    }
  }
	
  protected static class Bound {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__Empty_Bound__(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new EpsilonStackNode<IConstructor>(5114, 0);
      builder.addAlternative(ObjectRascalRascal.prod__Empty_Bound__, tmp);
	}
    protected static final void _init_prod__Default_Bound__lit___59_layouts_LAYOUTLIST_expression_Expression_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode<IConstructor>(5120, 2, "Expression", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(5118, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(5116, 0, prod__lit___59__char_class___range__59_59_, new int[] {59}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Default_Bound__lit___59_layouts_LAYOUTLIST_expression_Expression_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__Empty_Bound__(builder);
      
        _init_prod__Default_Bound__lit___59_layouts_LAYOUTLIST_expression_Expression_(builder);
      
    }
  }
	
  protected static class Declaration {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__Alias_Declaration__tags_Tags_layouts_LAYOUTLIST_visibility_Visibility_layouts_LAYOUTLIST_lit_alias_layouts_LAYOUTLIST_user_UserType_layouts_LAYOUTLIST_lit___61_layouts_LAYOUTLIST_base_Type_layouts_LAYOUTLIST_lit___59_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[13];
      
      tmp[12] = new LiteralStackNode<IConstructor>(5178, 12, prod__lit___59__char_class___range__59_59_, new int[] {59}, null, null);
      tmp[11] = new NonTerminalStackNode<IConstructor>(5176, 11, "layouts_LAYOUTLIST", null, null);
      tmp[10] = new NonTerminalStackNode<IConstructor>(5174, 10, "Type", null, null);
      tmp[9] = new NonTerminalStackNode<IConstructor>(5172, 9, "layouts_LAYOUTLIST", null, null);
      tmp[8] = new LiteralStackNode<IConstructor>(5170, 8, prod__lit___61__char_class___range__61_61_, new int[] {61}, null, null);
      tmp[7] = new NonTerminalStackNode<IConstructor>(5168, 7, "layouts_LAYOUTLIST", null, null);
      tmp[6] = new NonTerminalStackNode<IConstructor>(5166, 6, "UserType", null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(5164, 5, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode<IConstructor>(5162, 4, prod__lit_alias__char_class___range__97_97_char_class___range__108_108_char_class___range__105_105_char_class___range__97_97_char_class___range__115_115_, new int[] {97,108,105,97,115}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(5160, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(5158, 2, "Visibility", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(5156, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(5154, 0, "Tags", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Alias_Declaration__tags_Tags_layouts_LAYOUTLIST_visibility_Visibility_layouts_LAYOUTLIST_lit_alias_layouts_LAYOUTLIST_user_UserType_layouts_LAYOUTLIST_lit___61_layouts_LAYOUTLIST_base_Type_layouts_LAYOUTLIST_lit___59_, tmp);
	}
    protected static final void _init_prod__DataAbstract_Declaration__tags_Tags_layouts_LAYOUTLIST_visibility_Visibility_layouts_LAYOUTLIST_lit_data_layouts_LAYOUTLIST_user_UserType_layouts_LAYOUTLIST_lit___59_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[9];
      
      tmp[8] = new LiteralStackNode<IConstructor>(5196, 8, prod__lit___59__char_class___range__59_59_, new int[] {59}, null, null);
      tmp[7] = new NonTerminalStackNode<IConstructor>(5194, 7, "layouts_LAYOUTLIST", null, null);
      tmp[6] = new NonTerminalStackNode<IConstructor>(5192, 6, "UserType", null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(5190, 5, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode<IConstructor>(5188, 4, prod__lit_data__char_class___range__100_100_char_class___range__97_97_char_class___range__116_116_char_class___range__97_97_, new int[] {100,97,116,97}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(5186, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(5184, 2, "Visibility", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(5182, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(5180, 0, "Tags", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__DataAbstract_Declaration__tags_Tags_layouts_LAYOUTLIST_visibility_Visibility_layouts_LAYOUTLIST_lit_data_layouts_LAYOUTLIST_user_UserType_layouts_LAYOUTLIST_lit___59_, tmp);
	}
    protected static final void _init_prod__Variable_Declaration__tags_Tags_layouts_LAYOUTLIST_visibility_Visibility_layouts_LAYOUTLIST_type_Type_layouts_LAYOUTLIST_variables_iter_seps__Variable__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___59_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[9];
      
      tmp[8] = new LiteralStackNode<IConstructor>(5222, 8, prod__lit___59__char_class___range__59_59_, new int[] {59}, null, null);
      tmp[7] = new NonTerminalStackNode<IConstructor>(5220, 7, "layouts_LAYOUTLIST", null, null);
      tmp[6] = new SeparatedListStackNode<IConstructor>(5210, 6, regular__iter_seps__Variable__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(5212, 0, "Variable", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(5214, 1, "layouts_LAYOUTLIST", null, null), new LiteralStackNode<IConstructor>(5216, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode<IConstructor>(5218, 3, "layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(5208, 5, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new NonTerminalStackNode<IConstructor>(5206, 4, "Type", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(5204, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(5202, 2, "Visibility", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(5200, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(5198, 0, "Tags", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Variable_Declaration__tags_Tags_layouts_LAYOUTLIST_visibility_Visibility_layouts_LAYOUTLIST_type_Type_layouts_LAYOUTLIST_variables_iter_seps__Variable__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___59_, tmp);
	}
    protected static final void _init_prod__Annotation_Declaration__tags_Tags_layouts_LAYOUTLIST_visibility_Visibility_layouts_LAYOUTLIST_lit_anno_layouts_LAYOUTLIST_annoType_Type_layouts_LAYOUTLIST_onType_Type_layouts_LAYOUTLIST_lit___64_layouts_LAYOUTLIST_name_Name_layouts_LAYOUTLIST_lit___59_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[15];
      
      tmp[14] = new LiteralStackNode<IConstructor>(5252, 14, prod__lit___59__char_class___range__59_59_, new int[] {59}, null, null);
      tmp[13] = new NonTerminalStackNode<IConstructor>(5250, 13, "layouts_LAYOUTLIST", null, null);
      tmp[12] = new NonTerminalStackNode<IConstructor>(5248, 12, "Name", null, null);
      tmp[11] = new NonTerminalStackNode<IConstructor>(5246, 11, "layouts_LAYOUTLIST", null, null);
      tmp[10] = new LiteralStackNode<IConstructor>(5244, 10, prod__lit___64__char_class___range__64_64_, new int[] {64}, null, null);
      tmp[9] = new NonTerminalStackNode<IConstructor>(5242, 9, "layouts_LAYOUTLIST", null, null);
      tmp[8] = new NonTerminalStackNode<IConstructor>(5240, 8, "Type", null, null);
      tmp[7] = new NonTerminalStackNode<IConstructor>(5238, 7, "layouts_LAYOUTLIST", null, null);
      tmp[6] = new NonTerminalStackNode<IConstructor>(5236, 6, "Type", null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(5234, 5, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode<IConstructor>(5232, 4, prod__lit_anno__char_class___range__97_97_char_class___range__110_110_char_class___range__110_110_char_class___range__111_111_, new int[] {97,110,110,111}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(5230, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(5228, 2, "Visibility", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(5226, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(5224, 0, "Tags", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Annotation_Declaration__tags_Tags_layouts_LAYOUTLIST_visibility_Visibility_layouts_LAYOUTLIST_lit_anno_layouts_LAYOUTLIST_annoType_Type_layouts_LAYOUTLIST_onType_Type_layouts_LAYOUTLIST_lit___64_layouts_LAYOUTLIST_name_Name_layouts_LAYOUTLIST_lit___59_, tmp);
	}
    protected static final void _init_prod__Function_Declaration__functionDeclaration_FunctionDeclaration_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(5254, 0, "FunctionDeclaration", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Function_Declaration__functionDeclaration_FunctionDeclaration_, tmp);
	}
    protected static final void _init_prod__Data_Declaration__tags_Tags_layouts_LAYOUTLIST_visibility_Visibility_layouts_LAYOUTLIST_lit_data_layouts_LAYOUTLIST_user_UserType_layouts_LAYOUTLIST_lit___61_layouts_LAYOUTLIST_variants_iter_seps__Variant__layouts_LAYOUTLIST_lit___124_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___59__tag__Foldable(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[13];
      
      tmp[12] = new LiteralStackNode<IConstructor>(5288, 12, prod__lit___59__char_class___range__59_59_, new int[] {59}, null, null);
      tmp[11] = new NonTerminalStackNode<IConstructor>(5286, 11, "layouts_LAYOUTLIST", null, null);
      tmp[10] = new SeparatedListStackNode<IConstructor>(5276, 10, regular__iter_seps__Variant__layouts_LAYOUTLIST_lit___124_layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(5278, 0, "Variant", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(5280, 1, "layouts_LAYOUTLIST", null, null), new LiteralStackNode<IConstructor>(5282, 2, prod__lit___124__char_class___range__124_124_, new int[] {124}, null, null), new NonTerminalStackNode<IConstructor>(5284, 3, "layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[9] = new NonTerminalStackNode<IConstructor>(5274, 9, "layouts_LAYOUTLIST", null, null);
      tmp[8] = new LiteralStackNode<IConstructor>(5272, 8, prod__lit___61__char_class___range__61_61_, new int[] {61}, null, null);
      tmp[7] = new NonTerminalStackNode<IConstructor>(5270, 7, "layouts_LAYOUTLIST", null, null);
      tmp[6] = new NonTerminalStackNode<IConstructor>(5268, 6, "UserType", null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(5266, 5, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode<IConstructor>(5264, 4, prod__lit_data__char_class___range__100_100_char_class___range__97_97_char_class___range__116_116_char_class___range__97_97_, new int[] {100,97,116,97}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(5262, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(5260, 2, "Visibility", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(5258, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(5256, 0, "Tags", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Data_Declaration__tags_Tags_layouts_LAYOUTLIST_visibility_Visibility_layouts_LAYOUTLIST_lit_data_layouts_LAYOUTLIST_user_UserType_layouts_LAYOUTLIST_lit___61_layouts_LAYOUTLIST_variants_iter_seps__Variant__layouts_LAYOUTLIST_lit___124_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___59__tag__Foldable, tmp);
	}
    protected static final void _init_prod__Tag_Declaration__tags_Tags_layouts_LAYOUTLIST_visibility_Visibility_layouts_LAYOUTLIST_lit_tag_layouts_LAYOUTLIST_kind_Kind_layouts_LAYOUTLIST_name_Name_layouts_LAYOUTLIST_lit_on_layouts_LAYOUTLIST_types_iter_seps__Type__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___59_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[15];
      
      tmp[14] = new LiteralStackNode<IConstructor>(5326, 14, prod__lit___59__char_class___range__59_59_, new int[] {59}, null, null);
      tmp[13] = new NonTerminalStackNode<IConstructor>(5324, 13, "layouts_LAYOUTLIST", null, null);
      tmp[12] = new SeparatedListStackNode<IConstructor>(5314, 12, regular__iter_seps__Type__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(5316, 0, "Type", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(5318, 1, "layouts_LAYOUTLIST", null, null), new LiteralStackNode<IConstructor>(5320, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode<IConstructor>(5322, 3, "layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[11] = new NonTerminalStackNode<IConstructor>(5312, 11, "layouts_LAYOUTLIST", null, null);
      tmp[10] = new LiteralStackNode<IConstructor>(5310, 10, prod__lit_on__char_class___range__111_111_char_class___range__110_110_, new int[] {111,110}, null, null);
      tmp[9] = new NonTerminalStackNode<IConstructor>(5308, 9, "layouts_LAYOUTLIST", null, null);
      tmp[8] = new NonTerminalStackNode<IConstructor>(5306, 8, "Name", null, null);
      tmp[7] = new NonTerminalStackNode<IConstructor>(5304, 7, "layouts_LAYOUTLIST", null, null);
      tmp[6] = new NonTerminalStackNode<IConstructor>(5302, 6, "Kind", null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(5300, 5, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode<IConstructor>(5298, 4, prod__lit_tag__char_class___range__116_116_char_class___range__97_97_char_class___range__103_103_, new int[] {116,97,103}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(5296, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(5294, 2, "Visibility", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(5292, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(5290, 0, "Tags", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Tag_Declaration__tags_Tags_layouts_LAYOUTLIST_visibility_Visibility_layouts_LAYOUTLIST_lit_tag_layouts_LAYOUTLIST_kind_Kind_layouts_LAYOUTLIST_name_Name_layouts_LAYOUTLIST_lit_on_layouts_LAYOUTLIST_types_iter_seps__Type__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___59_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__Alias_Declaration__tags_Tags_layouts_LAYOUTLIST_visibility_Visibility_layouts_LAYOUTLIST_lit_alias_layouts_LAYOUTLIST_user_UserType_layouts_LAYOUTLIST_lit___61_layouts_LAYOUTLIST_base_Type_layouts_LAYOUTLIST_lit___59_(builder);
      
        _init_prod__DataAbstract_Declaration__tags_Tags_layouts_LAYOUTLIST_visibility_Visibility_layouts_LAYOUTLIST_lit_data_layouts_LAYOUTLIST_user_UserType_layouts_LAYOUTLIST_lit___59_(builder);
      
        _init_prod__Variable_Declaration__tags_Tags_layouts_LAYOUTLIST_visibility_Visibility_layouts_LAYOUTLIST_type_Type_layouts_LAYOUTLIST_variables_iter_seps__Variable__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___59_(builder);
      
        _init_prod__Annotation_Declaration__tags_Tags_layouts_LAYOUTLIST_visibility_Visibility_layouts_LAYOUTLIST_lit_anno_layouts_LAYOUTLIST_annoType_Type_layouts_LAYOUTLIST_onType_Type_layouts_LAYOUTLIST_lit___64_layouts_LAYOUTLIST_name_Name_layouts_LAYOUTLIST_lit___59_(builder);
      
        _init_prod__Function_Declaration__functionDeclaration_FunctionDeclaration_(builder);
      
        _init_prod__Data_Declaration__tags_Tags_layouts_LAYOUTLIST_visibility_Visibility_layouts_LAYOUTLIST_lit_data_layouts_LAYOUTLIST_user_UserType_layouts_LAYOUTLIST_lit___61_layouts_LAYOUTLIST_variants_iter_seps__Variant__layouts_LAYOUTLIST_lit___124_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___59__tag__Foldable(builder);
      
        _init_prod__Tag_Declaration__tags_Tags_layouts_LAYOUTLIST_visibility_Visibility_layouts_LAYOUTLIST_lit_tag_layouts_LAYOUTLIST_kind_Kind_layouts_LAYOUTLIST_name_Name_layouts_LAYOUTLIST_lit_on_layouts_LAYOUTLIST_types_iter_seps__Type__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___59_(builder);
      
    }
  }
	
  protected static class Type {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__Function_Type__function_FunctionType_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(5130, 0, "FunctionType", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Function_Type__function_FunctionType_, tmp);
	}
    protected static final void _init_prod__Bracket_Type__lit___40_layouts_LAYOUTLIST_type_Type_layouts_LAYOUTLIST_lit___41__bracket(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new LiteralStackNode<IConstructor>(5140, 4, prod__lit___41__char_class___range__41_41_, new int[] {41}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(5138, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(5136, 2, "Type", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(5134, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(5132, 0, prod__lit___40__char_class___range__40_40_, new int[] {40}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Bracket_Type__lit___40_layouts_LAYOUTLIST_type_Type_layouts_LAYOUTLIST_lit___41__bracket, tmp);
	}
    protected static final void _init_prod__Basic_Type__basic_BasicType_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(5142, 0, "BasicType", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Basic_Type__basic_BasicType_, tmp);
	}
    protected static final void _init_prod__Symbol_Type__symbol_Sym_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(5144, 0, "Sym", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Symbol_Type__symbol_Sym_, tmp);
	}
    protected static final void _init_prod__Variable_Type__typeVar_TypeVar_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(5146, 0, "TypeVar", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Variable_Type__typeVar_TypeVar_, tmp);
	}
    protected static final void _init_prod__User_Type__conditional__user_UserType__delete__HeaderKeyword_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(5148, 0, "UserType", null, new ICompletionFilter[] {new StringMatchRestriction(new int[] {108,101,120,105,99,97,108}), new StringMatchRestriction(new int[] {105,109,112,111,114,116}), new StringMatchRestriction(new int[] {115,116,97,114,116}), new StringMatchRestriction(new int[] {115,121,110,116,97,120}), new StringMatchRestriction(new int[] {107,101,121,119,111,114,100}), new StringMatchRestriction(new int[] {101,120,116,101,110,100}), new StringMatchRestriction(new int[] {108,97,121,111,117,116})});
      builder.addAlternative(ObjectRascalRascal.prod__User_Type__conditional__user_UserType__delete__HeaderKeyword_, tmp);
	}
    protected static final void _init_prod__Selector_Type__selector_DataTypeSelector_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(5150, 0, "DataTypeSelector", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Selector_Type__selector_DataTypeSelector_, tmp);
	}
    protected static final void _init_prod__Structured_Type__structured_StructuredType_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(5152, 0, "StructuredType", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Structured_Type__structured_StructuredType_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__Function_Type__function_FunctionType_(builder);
      
        _init_prod__Bracket_Type__lit___40_layouts_LAYOUTLIST_type_Type_layouts_LAYOUTLIST_lit___41__bracket(builder);
      
        _init_prod__Basic_Type__basic_BasicType_(builder);
      
        _init_prod__Symbol_Type__symbol_Sym_(builder);
      
        _init_prod__Variable_Type__typeVar_TypeVar_(builder);
      
        _init_prod__User_Type__conditional__user_UserType__delete__HeaderKeyword_(builder);
      
        _init_prod__Selector_Type__selector_DataTypeSelector_(builder);
      
        _init_prod__Structured_Type__structured_StructuredType_(builder);
      
    }
  }
	
  protected static class Class {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__Complement_Class__lit___33_layouts_LAYOUTLIST_charClass_Class_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode<IConstructor>(5360, 2, "Class", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(5358, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(5356, 0, prod__lit___33__char_class___range__33_33_, new int[] {33}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Complement_Class__lit___33_layouts_LAYOUTLIST_charClass_Class_, tmp);
	}
    protected static final void _init_prod__Bracket_Class__lit___40_layouts_LAYOUTLIST_charclass_Class_layouts_LAYOUTLIST_lit___41__bracket(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new LiteralStackNode<IConstructor>(5404, 4, prod__lit___41__char_class___range__41_41_, new int[] {41}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(5402, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(5400, 2, "Class", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(5398, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(5396, 0, prod__lit___40__char_class___range__40_40_, new int[] {40}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Bracket_Class__lit___40_layouts_LAYOUTLIST_charclass_Class_layouts_LAYOUTLIST_lit___41__bracket, tmp);
	}
    protected static final void _init_prod__Intersection_Class__lhs_Class_layouts_LAYOUTLIST_lit___38_38_layouts_LAYOUTLIST_rhs_Class__assoc__left(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(5394, 4, "Class", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(5392, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(5390, 2, prod__lit___38_38__char_class___range__38_38_char_class___range__38_38_, new int[] {38,38}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(5388, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(5386, 0, "Class", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Intersection_Class__lhs_Class_layouts_LAYOUTLIST_lit___38_38_layouts_LAYOUTLIST_rhs_Class__assoc__left, tmp);
	}
    protected static final void _init_prod__Difference_Class__lhs_Class_layouts_LAYOUTLIST_lit___layouts_LAYOUTLIST_rhs_Class__assoc__left(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(5384, 4, "Class", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(5382, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(5380, 2, prod__lit____char_class___range__45_45_, new int[] {45}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(5378, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(5376, 0, "Class", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Difference_Class__lhs_Class_layouts_LAYOUTLIST_lit___layouts_LAYOUTLIST_rhs_Class__assoc__left, tmp);
	}
    protected static final void _init_prod__SimpleCharclass_Class__lit___91_layouts_LAYOUTLIST_ranges_iter_star_seps__Range__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___93_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new LiteralStackNode<IConstructor>(5374, 4, prod__lit___93__char_class___range__93_93_, new int[] {93}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(5372, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new SeparatedListStackNode<IConstructor>(5366, 2, regular__iter_star_seps__Range__layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(5368, 0, "Range", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(5370, 1, "layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(5364, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(5362, 0, prod__lit___91__char_class___range__91_91_, new int[] {91}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__SimpleCharclass_Class__lit___91_layouts_LAYOUTLIST_ranges_iter_star_seps__Range__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___93_, tmp);
	}
    protected static final void _init_prod__Union_Class__lhs_Class_layouts_LAYOUTLIST_lit___124_124_layouts_LAYOUTLIST_rhs_Class__assoc__left(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(5414, 4, "Class", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(5412, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(5410, 2, prod__lit___124_124__char_class___range__124_124_char_class___range__124_124_, new int[] {124,124}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(5408, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(5406, 0, "Class", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Union_Class__lhs_Class_layouts_LAYOUTLIST_lit___124_124_layouts_LAYOUTLIST_rhs_Class__assoc__left, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__Complement_Class__lit___33_layouts_LAYOUTLIST_charClass_Class_(builder);
      
        _init_prod__Bracket_Class__lit___40_layouts_LAYOUTLIST_charclass_Class_layouts_LAYOUTLIST_lit___41__bracket(builder);
      
        _init_prod__Intersection_Class__lhs_Class_layouts_LAYOUTLIST_lit___38_38_layouts_LAYOUTLIST_rhs_Class__assoc__left(builder);
      
        _init_prod__Difference_Class__lhs_Class_layouts_LAYOUTLIST_lit___layouts_LAYOUTLIST_rhs_Class__assoc__left(builder);
      
        _init_prod__SimpleCharclass_Class__lit___91_layouts_LAYOUTLIST_ranges_iter_star_seps__Range__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___93_(builder);
      
        _init_prod__Union_Class__lhs_Class_layouts_LAYOUTLIST_lit___124_124_layouts_LAYOUTLIST_rhs_Class__assoc__left(builder);
      
    }
  }
	
  protected static class Mapping__Expression {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__Default_Mapping__Expression__from_Expression_layouts_LAYOUTLIST_lit___58_layouts_LAYOUTLIST_to_Expression_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(5428, 4, "Expression", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(5426, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(5424, 2, prod__lit___58__char_class___range__58_58_, new int[] {58}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(5422, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(5420, 0, "Expression", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Default_Mapping__Expression__from_Expression_layouts_LAYOUTLIST_lit___58_layouts_LAYOUTLIST_to_Expression_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__Default_Mapping__Expression__from_Expression_layouts_LAYOUTLIST_lit___58_layouts_LAYOUTLIST_to_Expression_(builder);
      
    }
  }
	
  protected static class Comprehension {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__List_Comprehension__lit___91_layouts_LAYOUTLIST_results_iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___124_layouts_LAYOUTLIST_generators_iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___93_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[9];
      
      tmp[8] = new LiteralStackNode<IConstructor>(5472, 8, prod__lit___93__char_class___range__93_93_, new int[] {93}, null, null);
      tmp[7] = new NonTerminalStackNode<IConstructor>(5470, 7, "layouts_LAYOUTLIST", null, null);
      tmp[6] = new SeparatedListStackNode<IConstructor>(5460, 6, regular__iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(5462, 0, "Expression", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(5464, 1, "layouts_LAYOUTLIST", null, null), new LiteralStackNode<IConstructor>(5466, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode<IConstructor>(5468, 3, "layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(5458, 5, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode<IConstructor>(5456, 4, prod__lit___124__char_class___range__124_124_, new int[] {124}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(5454, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new SeparatedListStackNode<IConstructor>(5444, 2, regular__iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(5446, 0, "Expression", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(5448, 1, "layouts_LAYOUTLIST", null, null), new LiteralStackNode<IConstructor>(5450, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode<IConstructor>(5452, 3, "layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(5442, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(5440, 0, prod__lit___91__char_class___range__91_91_, new int[] {91}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__List_Comprehension__lit___91_layouts_LAYOUTLIST_results_iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___124_layouts_LAYOUTLIST_generators_iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___93_, tmp);
	}
    protected static final void _init_prod__Map_Comprehension__lit___40_layouts_LAYOUTLIST_from_Expression_layouts_LAYOUTLIST_lit___58_layouts_LAYOUTLIST_to_Expression_layouts_LAYOUTLIST_lit___124_layouts_LAYOUTLIST_generators_iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[13];
      
      tmp[12] = new LiteralStackNode<IConstructor>(5506, 12, prod__lit___41__char_class___range__41_41_, new int[] {41}, null, null);
      tmp[11] = new NonTerminalStackNode<IConstructor>(5504, 11, "layouts_LAYOUTLIST", null, null);
      tmp[10] = new SeparatedListStackNode<IConstructor>(5494, 10, regular__iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(5496, 0, "Expression", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(5498, 1, "layouts_LAYOUTLIST", null, null), new LiteralStackNode<IConstructor>(5500, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode<IConstructor>(5502, 3, "layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[9] = new NonTerminalStackNode<IConstructor>(5492, 9, "layouts_LAYOUTLIST", null, null);
      tmp[8] = new LiteralStackNode<IConstructor>(5490, 8, prod__lit___124__char_class___range__124_124_, new int[] {124}, null, null);
      tmp[7] = new NonTerminalStackNode<IConstructor>(5488, 7, "layouts_LAYOUTLIST", null, null);
      tmp[6] = new NonTerminalStackNode<IConstructor>(5486, 6, "Expression", null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(5484, 5, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode<IConstructor>(5482, 4, prod__lit___58__char_class___range__58_58_, new int[] {58}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(5480, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(5478, 2, "Expression", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(5476, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(5474, 0, prod__lit___40__char_class___range__40_40_, new int[] {40}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Map_Comprehension__lit___40_layouts_LAYOUTLIST_from_Expression_layouts_LAYOUTLIST_lit___58_layouts_LAYOUTLIST_to_Expression_layouts_LAYOUTLIST_lit___124_layouts_LAYOUTLIST_generators_iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_, tmp);
	}
    protected static final void _init_prod__Set_Comprehension__lit___123_layouts_LAYOUTLIST_results_iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___124_layouts_LAYOUTLIST_generators_iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[9];
      
      tmp[8] = new LiteralStackNode<IConstructor>(5540, 8, prod__lit___125__char_class___range__125_125_, new int[] {125}, null, null);
      tmp[7] = new NonTerminalStackNode<IConstructor>(5538, 7, "layouts_LAYOUTLIST", null, null);
      tmp[6] = new SeparatedListStackNode<IConstructor>(5528, 6, regular__iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(5530, 0, "Expression", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(5532, 1, "layouts_LAYOUTLIST", null, null), new LiteralStackNode<IConstructor>(5534, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode<IConstructor>(5536, 3, "layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(5526, 5, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode<IConstructor>(5524, 4, prod__lit___124__char_class___range__124_124_, new int[] {124}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(5522, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new SeparatedListStackNode<IConstructor>(5512, 2, regular__iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(5514, 0, "Expression", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(5516, 1, "layouts_LAYOUTLIST", null, null), new LiteralStackNode<IConstructor>(5518, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode<IConstructor>(5520, 3, "layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(5510, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(5508, 0, prod__lit___123__char_class___range__123_123_, new int[] {123}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Set_Comprehension__lit___123_layouts_LAYOUTLIST_results_iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___124_layouts_LAYOUTLIST_generators_iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__List_Comprehension__lit___91_layouts_LAYOUTLIST_results_iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___124_layouts_LAYOUTLIST_generators_iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___93_(builder);
      
        _init_prod__Map_Comprehension__lit___40_layouts_LAYOUTLIST_from_Expression_layouts_LAYOUTLIST_lit___58_layouts_LAYOUTLIST_to_Expression_layouts_LAYOUTLIST_lit___124_layouts_LAYOUTLIST_generators_iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_(builder);
      
        _init_prod__Set_Comprehension__lit___123_layouts_LAYOUTLIST_results_iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___124_layouts_LAYOUTLIST_generators_iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_(builder);
      
    }
  }
	
  protected static class FunctionModifiers {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__List_FunctionModifiers__modifiers_iter_star_seps__FunctionModifier__layouts_LAYOUTLIST_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new SeparatedListStackNode<IConstructor>(5434, 0, regular__iter_star_seps__FunctionModifier__layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(5436, 0, "FunctionModifier", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(5438, 1, "layouts_LAYOUTLIST", null, null)}, false, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__List_FunctionModifiers__modifiers_iter_star_seps__FunctionModifier__layouts_LAYOUTLIST_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__List_FunctionModifiers__modifiers_iter_star_seps__FunctionModifier__layouts_LAYOUTLIST_(builder);
      
    }
  }
	
  protected static class ProdModifier {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__Associativity_ProdModifier__associativity_Assoc_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(5542, 0, "Assoc", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Associativity_ProdModifier__associativity_Assoc_, tmp);
	}
    protected static final void _init_prod__Tag_ProdModifier__tag_Tag_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(5544, 0, "Tag", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Tag_ProdModifier__tag_Tag_, tmp);
	}
    protected static final void _init_prod__Bracket_ProdModifier__lit_bracket_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(5546, 0, prod__lit_bracket__char_class___range__98_98_char_class___range__114_114_char_class___range__97_97_char_class___range__99_99_char_class___range__107_107_char_class___range__101_101_char_class___range__116_116_, new int[] {98,114,97,99,107,101,116}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Bracket_ProdModifier__lit_bracket_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__Associativity_ProdModifier__associativity_Assoc_(builder);
      
        _init_prod__Tag_ProdModifier__tag_Tag_(builder);
      
        _init_prod__Bracket_ProdModifier__lit_bracket_(builder);
      
    }
  }
	
  protected static class BooleanLiteral {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__BooleanLiteral__lit_true_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(5556, 0, prod__lit_true__char_class___range__116_116_char_class___range__114_114_char_class___range__117_117_char_class___range__101_101_, new int[] {116,114,117,101}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__BooleanLiteral__lit_true_, tmp);
	}
    protected static final void _init_prod__BooleanLiteral__lit_false_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(5558, 0, prod__lit_false__char_class___range__102_102_char_class___range__97_97_char_class___range__108_108_char_class___range__115_115_char_class___range__101_101_, new int[] {102,97,108,115,101}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__BooleanLiteral__lit_false_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__BooleanLiteral__lit_true_(builder);
      
        _init_prod__BooleanLiteral__lit_false_(builder);
      
    }
  }
	
  protected static class Toplevel {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__GivenVisibility_Toplevel__declaration_Declaration_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(5554, 0, "Declaration", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__GivenVisibility_Toplevel__declaration_Declaration_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__GivenVisibility_Toplevel__declaration_Declaration_(builder);
      
    }
  }
	
  protected static class TypeVar {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__Free_TypeVar__lit___38_layouts_LAYOUTLIST_name_Name_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode<IConstructor>(5566, 2, "Name", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(5564, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(5562, 0, prod__lit___38__char_class___range__38_38_, new int[] {38}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Free_TypeVar__lit___38_layouts_LAYOUTLIST_name_Name_, tmp);
	}
    protected static final void _init_prod__Bounded_TypeVar__lit___38_layouts_LAYOUTLIST_name_Name_layouts_LAYOUTLIST_lit___60_58_layouts_LAYOUTLIST_bound_Type_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[7];
      
      tmp[6] = new NonTerminalStackNode<IConstructor>(5580, 6, "Type", null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(5578, 5, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode<IConstructor>(5576, 4, prod__lit___60_58__char_class___range__60_60_char_class___range__58_58_, new int[] {60,58}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(5574, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(5572, 2, "Name", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(5570, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(5568, 0, prod__lit___38__char_class___range__38_38_, new int[] {38}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Bounded_TypeVar__lit___38_layouts_LAYOUTLIST_name_Name_layouts_LAYOUTLIST_lit___60_58_layouts_LAYOUTLIST_bound_Type_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__Free_TypeVar__lit___38_layouts_LAYOUTLIST_name_Name_(builder);
      
        _init_prod__Bounded_TypeVar__lit___38_layouts_LAYOUTLIST_name_Name_layouts_LAYOUTLIST_lit___60_58_layouts_LAYOUTLIST_bound_Type_(builder);
      
    }
  }
	
  protected static class RationalLiteral {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__RationalLiteral__char_class___range__49_57_iter_star__char_class___range__48_57_char_class___range__114_114_char_class___range__48_57_conditional__iter_star__char_class___range__48_57__not_follow__char_class___range__48_57_range__65_90_range__95_95_range__97_122_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new ListStackNode<IConstructor>(5612, 4, regular__iter_star__char_class___range__48_57, new CharStackNode<IConstructor>(5614, 0, new int[][]{{48,57}}, null, null), false, null, new ICompletionFilter[] {new CharFollowRestriction(new int[][]{{48,57},{65,90},{95,95},{97,122}})});
      tmp[3] = new CharStackNode<IConstructor>(5610, 3, new int[][]{{48,57}}, null, null);
      tmp[2] = new CharStackNode<IConstructor>(5608, 2, new int[][]{{114,114}}, null, null);
      tmp[1] = new ListStackNode<IConstructor>(5604, 1, regular__iter_star__char_class___range__48_57, new CharStackNode<IConstructor>(5606, 0, new int[][]{{48,57}}, null, null), false, null, null);
      tmp[0] = new CharStackNode<IConstructor>(5602, 0, new int[][]{{49,57}}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RationalLiteral__char_class___range__49_57_iter_star__char_class___range__48_57_char_class___range__114_114_char_class___range__48_57_conditional__iter_star__char_class___range__48_57__not_follow__char_class___range__48_57_range__65_90_range__95_95_range__97_122_, tmp);
	}
    protected static final void _init_prod__RationalLiteral__char_class___range__48_57_iter_star__char_class___range__48_57_char_class___range__114_114_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new CharStackNode<IConstructor>(5622, 2, new int[][]{{114,114}}, null, null);
      tmp[1] = new ListStackNode<IConstructor>(5618, 1, regular__iter_star__char_class___range__48_57, new CharStackNode<IConstructor>(5620, 0, new int[][]{{48,57}}, null, null), false, null, null);
      tmp[0] = new CharStackNode<IConstructor>(5616, 0, new int[][]{{48,57}}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RationalLiteral__char_class___range__48_57_iter_star__char_class___range__48_57_char_class___range__114_114_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__RationalLiteral__char_class___range__49_57_iter_star__char_class___range__48_57_char_class___range__114_114_char_class___range__48_57_conditional__iter_star__char_class___range__48_57__not_follow__char_class___range__48_57_range__65_90_range__95_95_range__97_122_(builder);
      
        _init_prod__RationalLiteral__char_class___range__48_57_iter_star__char_class___range__48_57_char_class___range__114_114_(builder);
      
    }
  }
	
  protected static class UnicodeEscape {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__utf32_UnicodeEscape__lit___92_char_class___range__85_85_char_class___range__48_57_range__65_70_range__97_102_char_class___range__48_57_range__65_70_range__97_102_char_class___range__48_57_range__65_70_range__97_102_char_class___range__48_57_range__65_70_range__97_102_char_class___range__48_57_range__65_70_range__97_102_char_class___range__48_57_range__65_70_range__97_102_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[8];
      
      tmp[7] = new CharStackNode<IConstructor>(5638, 7, new int[][]{{48,57},{65,70},{97,102}}, null, null);
      tmp[6] = new CharStackNode<IConstructor>(5636, 6, new int[][]{{48,57},{65,70},{97,102}}, null, null);
      tmp[5] = new CharStackNode<IConstructor>(5634, 5, new int[][]{{48,57},{65,70},{97,102}}, null, null);
      tmp[4] = new CharStackNode<IConstructor>(5632, 4, new int[][]{{48,57},{65,70},{97,102}}, null, null);
      tmp[3] = new CharStackNode<IConstructor>(5630, 3, new int[][]{{48,57},{65,70},{97,102}}, null, null);
      tmp[2] = new CharStackNode<IConstructor>(5628, 2, new int[][]{{48,57},{65,70},{97,102}}, null, null);
      tmp[1] = new CharStackNode<IConstructor>(5626, 1, new int[][]{{85,85}}, null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(5624, 0, prod__lit___92__char_class___range__92_92_, new int[] {92}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__utf32_UnicodeEscape__lit___92_char_class___range__85_85_char_class___range__48_57_range__65_70_range__97_102_char_class___range__48_57_range__65_70_range__97_102_char_class___range__48_57_range__65_70_range__97_102_char_class___range__48_57_range__65_70_range__97_102_char_class___range__48_57_range__65_70_range__97_102_char_class___range__48_57_range__65_70_range__97_102_, tmp);
	}
    protected static final void _init_prod__utf16_UnicodeEscape__lit___92_char_class___range__117_117_char_class___range__48_57_range__65_70_range__97_102_char_class___range__48_57_range__65_70_range__97_102_char_class___range__48_57_range__65_70_range__97_102_char_class___range__48_57_range__65_70_range__97_102_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[6];
      
      tmp[5] = new CharStackNode<IConstructor>(5650, 5, new int[][]{{48,57},{65,70},{97,102}}, null, null);
      tmp[4] = new CharStackNode<IConstructor>(5648, 4, new int[][]{{48,57},{65,70},{97,102}}, null, null);
      tmp[3] = new CharStackNode<IConstructor>(5646, 3, new int[][]{{48,57},{65,70},{97,102}}, null, null);
      tmp[2] = new CharStackNode<IConstructor>(5644, 2, new int[][]{{48,57},{65,70},{97,102}}, null, null);
      tmp[1] = new CharStackNode<IConstructor>(5642, 1, new int[][]{{117,117}}, null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(5640, 0, prod__lit___92__char_class___range__92_92_, new int[] {92}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__utf16_UnicodeEscape__lit___92_char_class___range__117_117_char_class___range__48_57_range__65_70_range__97_102_char_class___range__48_57_range__65_70_range__97_102_char_class___range__48_57_range__65_70_range__97_102_char_class___range__48_57_range__65_70_range__97_102_, tmp);
	}
    protected static final void _init_prod__ascii_UnicodeEscape__lit___92_char_class___range__97_97_char_class___range__48_55_char_class___range__48_57_range__65_70_range__97_102_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[4];
      
      tmp[3] = new CharStackNode<IConstructor>(5658, 3, new int[][]{{48,57},{65,70},{97,102}}, null, null);
      tmp[2] = new CharStackNode<IConstructor>(5656, 2, new int[][]{{48,55}}, null, null);
      tmp[1] = new CharStackNode<IConstructor>(5654, 1, new int[][]{{97,97}}, null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(5652, 0, prod__lit___92__char_class___range__92_92_, new int[] {92}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__ascii_UnicodeEscape__lit___92_char_class___range__97_97_char_class___range__48_55_char_class___range__48_57_range__65_70_range__97_102_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__utf32_UnicodeEscape__lit___92_char_class___range__85_85_char_class___range__48_57_range__65_70_range__97_102_char_class___range__48_57_range__65_70_range__97_102_char_class___range__48_57_range__65_70_range__97_102_char_class___range__48_57_range__65_70_range__97_102_char_class___range__48_57_range__65_70_range__97_102_char_class___range__48_57_range__65_70_range__97_102_(builder);
      
        _init_prod__utf16_UnicodeEscape__lit___92_char_class___range__117_117_char_class___range__48_57_range__65_70_range__97_102_char_class___range__48_57_range__65_70_range__97_102_char_class___range__48_57_range__65_70_range__97_102_char_class___range__48_57_range__65_70_range__97_102_(builder);
      
        _init_prod__ascii_UnicodeEscape__lit___92_char_class___range__97_97_char_class___range__48_55_char_class___range__48_57_range__65_70_range__97_102_(builder);
      
    }
  }
	
  protected static class Prod {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__AssociativityGroup_Prod__associativity_Assoc_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_group_Prod_layouts_LAYOUTLIST_lit___41__tag__Foldable(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[7];
      
      tmp[6] = new LiteralStackNode<IConstructor>(5686, 6, prod__lit___41__char_class___range__41_41_, new int[] {41}, null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(5684, 5, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new NonTerminalStackNode<IConstructor>(5682, 4, "Prod", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(5680, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(5678, 2, prod__lit___40__char_class___range__40_40_, new int[] {40}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(5676, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(5674, 0, "Assoc", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__AssociativityGroup_Prod__associativity_Assoc_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_group_Prod_layouts_LAYOUTLIST_lit___41__tag__Foldable, tmp);
	}
    protected static final void _init_prod__Reference_Prod__lit___58_layouts_LAYOUTLIST_referenced_Name_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode<IConstructor>(5692, 2, "Name", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(5690, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(5688, 0, prod__lit___58__char_class___range__58_58_, new int[] {58}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Reference_Prod__lit___58_layouts_LAYOUTLIST_referenced_Name_, tmp);
	}
    protected static final void _init_prod__Labeled_Prod__modifiers_iter_star_seps__ProdModifier__layouts_LAYOUTLIST_layouts_LAYOUTLIST_name_Name_layouts_LAYOUTLIST_lit___58_layouts_LAYOUTLIST_args_iter_star_seps__Sym__layouts_LAYOUTLIST_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[7];
      
      tmp[6] = new SeparatedListStackNode<IConstructor>(5710, 6, regular__iter_star_seps__Sym__layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(5712, 0, "Sym", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(5714, 1, "layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(5708, 5, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode<IConstructor>(5706, 4, prod__lit___58__char_class___range__58_58_, new int[] {58}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(5704, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(5702, 2, "Name", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(5700, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new SeparatedListStackNode<IConstructor>(5694, 0, regular__iter_star_seps__ProdModifier__layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(5696, 0, "ProdModifier", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(5698, 1, "layouts_LAYOUTLIST", null, null)}, false, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Labeled_Prod__modifiers_iter_star_seps__ProdModifier__layouts_LAYOUTLIST_layouts_LAYOUTLIST_name_Name_layouts_LAYOUTLIST_lit___58_layouts_LAYOUTLIST_args_iter_star_seps__Sym__layouts_LAYOUTLIST_, tmp);
	}
    protected static final void _init_prod__Others_Prod__lit___46_46_46_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(5716, 0, prod__lit___46_46_46__char_class___range__46_46_char_class___range__46_46_char_class___range__46_46_, new int[] {46,46,46}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Others_Prod__lit___46_46_46_, tmp);
	}
    protected static final void _init_prod__First_Prod__lhs_Prod_layouts_LAYOUTLIST_conditional__lit___62__not_follow__lit___62_layouts_LAYOUTLIST_rhs_Prod__assoc__left(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(5750, 4, "Prod", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(5748, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(5746, 2, prod__lit___62__char_class___range__62_62_, new int[] {62}, null, new ICompletionFilter[] {new StringFollowRestriction(new int[] {62})});
      tmp[1] = new NonTerminalStackNode<IConstructor>(5744, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(5742, 0, "Prod", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__First_Prod__lhs_Prod_layouts_LAYOUTLIST_conditional__lit___62__not_follow__lit___62_layouts_LAYOUTLIST_rhs_Prod__assoc__left, tmp);
	}
    protected static final void _init_prod__Unlabeled_Prod__modifiers_iter_star_seps__ProdModifier__layouts_LAYOUTLIST_layouts_LAYOUTLIST_args_iter_star_seps__Sym__layouts_LAYOUTLIST_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new SeparatedListStackNode<IConstructor>(5726, 2, regular__iter_star_seps__Sym__layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(5728, 0, "Sym", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(5730, 1, "layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(5724, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new SeparatedListStackNode<IConstructor>(5718, 0, regular__iter_star_seps__ProdModifier__layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(5720, 0, "ProdModifier", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(5722, 1, "layouts_LAYOUTLIST", null, null)}, false, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Unlabeled_Prod__modifiers_iter_star_seps__ProdModifier__layouts_LAYOUTLIST_layouts_LAYOUTLIST_args_iter_star_seps__Sym__layouts_LAYOUTLIST_, tmp);
	}
    protected static final void _init_prod__All_Prod__lhs_Prod_layouts_LAYOUTLIST_lit___124_layouts_LAYOUTLIST_rhs_Prod__assoc__left(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(5740, 4, "Prod", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(5738, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(5736, 2, prod__lit___124__char_class___range__124_124_, new int[] {124}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(5734, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(5732, 0, "Prod", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__All_Prod__lhs_Prod_layouts_LAYOUTLIST_lit___124_layouts_LAYOUTLIST_rhs_Prod__assoc__left, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__AssociativityGroup_Prod__associativity_Assoc_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_group_Prod_layouts_LAYOUTLIST_lit___41__tag__Foldable(builder);
      
        _init_prod__Reference_Prod__lit___58_layouts_LAYOUTLIST_referenced_Name_(builder);
      
        _init_prod__Labeled_Prod__modifiers_iter_star_seps__ProdModifier__layouts_LAYOUTLIST_layouts_LAYOUTLIST_name_Name_layouts_LAYOUTLIST_lit___58_layouts_LAYOUTLIST_args_iter_star_seps__Sym__layouts_LAYOUTLIST_(builder);
      
        _init_prod__Others_Prod__lit___46_46_46_(builder);
      
        _init_prod__First_Prod__lhs_Prod_layouts_LAYOUTLIST_conditional__lit___62__not_follow__lit___62_layouts_LAYOUTLIST_rhs_Prod__assoc__left(builder);
      
        _init_prod__Unlabeled_Prod__modifiers_iter_star_seps__ProdModifier__layouts_LAYOUTLIST_layouts_LAYOUTLIST_args_iter_star_seps__Sym__layouts_LAYOUTLIST_(builder);
      
        _init_prod__All_Prod__lhs_Prod_layouts_LAYOUTLIST_lit___124_layouts_LAYOUTLIST_rhs_Prod__assoc__left(builder);
      
    }
  }
	
  protected static class OctalIntegerLiteral {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__OctalIntegerLiteral__char_class___range__48_48_conditional__iter__char_class___range__48_55__not_follow__char_class___range__48_57_range__65_90_range__95_95_range__97_122_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[2];
      
      tmp[1] = new ListStackNode<IConstructor>(5758, 1, regular__iter__char_class___range__48_55, new CharStackNode<IConstructor>(5760, 0, new int[][]{{48,55}}, null, null), true, null, new ICompletionFilter[] {new CharFollowRestriction(new int[][]{{48,57},{65,90},{95,95},{97,122}})});
      tmp[0] = new CharStackNode<IConstructor>(5756, 0, new int[][]{{48,48}}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__OctalIntegerLiteral__char_class___range__48_48_conditional__iter__char_class___range__48_55__not_follow__char_class___range__48_57_range__65_90_range__95_95_range__97_122_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__OctalIntegerLiteral__char_class___range__48_48_conditional__iter__char_class___range__48_55__not_follow__char_class___range__48_57_range__65_90_range__95_95_range__97_122_(builder);
      
    }
  }
	
  protected static class Pattern {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__Map_Pattern__lit___40_layouts_LAYOUTLIST_mappings_iter_star_seps__Mapping__Pattern__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new LiteralStackNode<IConstructor>(5794, 4, prod__lit___41__char_class___range__41_41_, new int[] {41}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(5792, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new SeparatedListStackNode<IConstructor>(5782, 2, regular__iter_star_seps__Mapping__Pattern__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(5784, 0, "Mapping__Pattern", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(5786, 1, "layouts_LAYOUTLIST", null, null), new LiteralStackNode<IConstructor>(5788, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode<IConstructor>(5790, 3, "layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(5780, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(5778, 0, prod__lit___40__char_class___range__40_40_, new int[] {40}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Map_Pattern__lit___40_layouts_LAYOUTLIST_mappings_iter_star_seps__Mapping__Pattern__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_, tmp);
	}
    protected static final void _init_prod__List_Pattern__lit___91_layouts_LAYOUTLIST_elements_iter_star_seps__Pattern__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___93_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new LiteralStackNode<IConstructor>(5812, 4, prod__lit___93__char_class___range__93_93_, new int[] {93}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(5810, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new SeparatedListStackNode<IConstructor>(5800, 2, regular__iter_star_seps__Pattern__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(5802, 0, "Pattern", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(5804, 1, "layouts_LAYOUTLIST", null, null), new LiteralStackNode<IConstructor>(5806, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode<IConstructor>(5808, 3, "layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(5798, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(5796, 0, prod__lit___91__char_class___range__91_91_, new int[] {91}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__List_Pattern__lit___91_layouts_LAYOUTLIST_elements_iter_star_seps__Pattern__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___93_, tmp);
	}
    protected static final void _init_prod__QualifiedName_Pattern__qualifiedName_QualifiedName_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(5822, 0, "QualifiedName", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__QualifiedName_Pattern__qualifiedName_QualifiedName_, tmp);
	}
    protected static final void _init_prod__SplicePlus_Pattern__lit___43_layouts_LAYOUTLIST_argument_Pattern_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode<IConstructor>(5820, 2, "Pattern", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(5818, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(5816, 0, prod__lit___43__char_class___range__43_43_, new int[] {43}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__SplicePlus_Pattern__lit___43_layouts_LAYOUTLIST_argument_Pattern_, tmp);
	}
    protected static final void _init_prod__AsType_Pattern__lit___91_layouts_LAYOUTLIST_type_Type_layouts_LAYOUTLIST_lit___93_layouts_LAYOUTLIST_argument_Pattern_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[7];
      
      tmp[6] = new NonTerminalStackNode<IConstructor>(5940, 6, "Pattern", null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(5938, 5, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode<IConstructor>(5936, 4, prod__lit___93__char_class___range__93_93_, new int[] {93}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(5934, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(5932, 2, "Type", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(5930, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(5928, 0, prod__lit___91__char_class___range__91_91_, new int[] {91}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__AsType_Pattern__lit___91_layouts_LAYOUTLIST_type_Type_layouts_LAYOUTLIST_lit___93_layouts_LAYOUTLIST_argument_Pattern_, tmp);
	}
    protected static final void _init_prod__Negative_Pattern__lit___layouts_LAYOUTLIST_argument_Pattern_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode<IConstructor>(5828, 2, "Pattern", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(5826, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(5824, 0, prod__lit____char_class___range__45_45_, new int[] {45}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Negative_Pattern__lit___layouts_LAYOUTLIST_argument_Pattern_, tmp);
	}
    protected static final void _init_prod__Descendant_Pattern__lit___47_layouts_LAYOUTLIST_pattern_Pattern_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode<IConstructor>(5946, 2, "Pattern", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(5944, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(5942, 0, prod__lit___47__char_class___range__47_47_, new int[] {47}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Descendant_Pattern__lit___47_layouts_LAYOUTLIST_pattern_Pattern_, tmp);
	}
    protected static final void _init_prod__MultiVariable_Pattern__qualifiedName_QualifiedName_layouts_LAYOUTLIST_lit___42_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new LiteralStackNode<IConstructor>(5852, 2, prod__lit___42__char_class___range__42_42_, new int[] {42}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(5850, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(5848, 0, "QualifiedName", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__MultiVariable_Pattern__qualifiedName_QualifiedName_layouts_LAYOUTLIST_lit___42_, tmp);
	}
    protected static final void _init_prod__TypedVariable_Pattern__type_Type_layouts_LAYOUTLIST_name_Name_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode<IConstructor>(5858, 2, "Name", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(5856, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(5854, 0, "Type", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__TypedVariable_Pattern__type_Type_layouts_LAYOUTLIST_name_Name_, tmp);
	}
    protected static final void _init_prod__Set_Pattern__lit___123_layouts_LAYOUTLIST_elements_iter_star_seps__Pattern__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new LiteralStackNode<IConstructor>(5876, 4, prod__lit___125__char_class___range__125_125_, new int[] {125}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(5874, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new SeparatedListStackNode<IConstructor>(5864, 2, regular__iter_star_seps__Pattern__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(5866, 0, "Pattern", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(5868, 1, "layouts_LAYOUTLIST", null, null), new LiteralStackNode<IConstructor>(5870, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode<IConstructor>(5872, 3, "layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(5862, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(5860, 0, prod__lit___123__char_class___range__123_123_, new int[] {123}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Set_Pattern__lit___123_layouts_LAYOUTLIST_elements_iter_star_seps__Pattern__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_, tmp);
	}
    protected static final void _init_prod__ReifiedType_Pattern__lit_type_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_symbol_Pattern_layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_definitions_Pattern_layouts_LAYOUTLIST_lit___41_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[11];
      
      tmp[10] = new LiteralStackNode<IConstructor>(5898, 10, prod__lit___41__char_class___range__41_41_, new int[] {41}, null, null);
      tmp[9] = new NonTerminalStackNode<IConstructor>(5896, 9, "layouts_LAYOUTLIST", null, null);
      tmp[8] = new NonTerminalStackNode<IConstructor>(5894, 8, "Pattern", null, null);
      tmp[7] = new NonTerminalStackNode<IConstructor>(5892, 7, "layouts_LAYOUTLIST", null, null);
      tmp[6] = new LiteralStackNode<IConstructor>(5890, 6, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(5888, 5, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new NonTerminalStackNode<IConstructor>(5886, 4, "Pattern", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(5884, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(5882, 2, prod__lit___40__char_class___range__40_40_, new int[] {40}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(5880, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(5878, 0, prod__lit_type__char_class___range__116_116_char_class___range__121_121_char_class___range__112_112_char_class___range__101_101_, new int[] {116,121,112,101}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__ReifiedType_Pattern__lit_type_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_symbol_Pattern_layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_definitions_Pattern_layouts_LAYOUTLIST_lit___41_, tmp);
	}
    protected static final void _init_prod__Splice_Pattern__lit___42_layouts_LAYOUTLIST_argument_Pattern_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode<IConstructor>(5926, 2, "Pattern", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(5924, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(5922, 0, prod__lit___42__char_class___range__42_42_, new int[] {42}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Splice_Pattern__lit___42_layouts_LAYOUTLIST_argument_Pattern_, tmp);
	}
    protected static final void _init_prod__Literal_Pattern__literal_Literal_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(5814, 0, "Literal", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Literal_Pattern__literal_Literal_, tmp);
	}
    protected static final void _init_prod__Tuple_Pattern__lit___60_layouts_LAYOUTLIST_elements_iter_seps__Pattern__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___62_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new LiteralStackNode<IConstructor>(5846, 4, prod__lit___62__char_class___range__62_62_, new int[] {62}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(5844, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new SeparatedListStackNode<IConstructor>(5834, 2, regular__iter_seps__Pattern__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(5836, 0, "Pattern", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(5838, 1, "layouts_LAYOUTLIST", null, null), new LiteralStackNode<IConstructor>(5840, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode<IConstructor>(5842, 3, "layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(5832, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(5830, 0, prod__lit___60__char_class___range__60_60_, new int[] {60}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Tuple_Pattern__lit___60_layouts_LAYOUTLIST_elements_iter_seps__Pattern__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___62_, tmp);
	}
    protected static final void _init_prod__Anti_Pattern__lit___33_layouts_LAYOUTLIST_pattern_Pattern_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode<IConstructor>(5962, 2, "Pattern", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(5960, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(5958, 0, prod__lit___33__char_class___range__33_33_, new int[] {33}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Anti_Pattern__lit___33_layouts_LAYOUTLIST_pattern_Pattern_, tmp);
	}
    protected static final void _init_prod__TypedVariableBecomes_Pattern__type_Type_layouts_LAYOUTLIST_name_Name_layouts_LAYOUTLIST_lit___58_layouts_LAYOUTLIST_pattern_Pattern_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[7];
      
      tmp[6] = new NonTerminalStackNode<IConstructor>(5976, 6, "Pattern", null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(5974, 5, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode<IConstructor>(5972, 4, prod__lit___58__char_class___range__58_58_, new int[] {58}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(5970, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(5968, 2, "Name", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(5966, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(5964, 0, "Type", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__TypedVariableBecomes_Pattern__type_Type_layouts_LAYOUTLIST_name_Name_layouts_LAYOUTLIST_lit___58_layouts_LAYOUTLIST_pattern_Pattern_, tmp);
	}
    protected static final void _init_prod__VariableBecomes_Pattern__name_Name_layouts_LAYOUTLIST_lit___58_layouts_LAYOUTLIST_pattern_Pattern_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(5956, 4, "Pattern", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(5954, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(5952, 2, prod__lit___58__char_class___range__58_58_, new int[] {58}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(5950, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(5948, 0, "Name", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__VariableBecomes_Pattern__name_Name_layouts_LAYOUTLIST_lit___58_layouts_LAYOUTLIST_pattern_Pattern_, tmp);
	}
    protected static final void _init_prod__CallOrTree_Pattern__expression_Pattern_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_arguments_iter_star_seps__Pattern__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[7];
      
      tmp[6] = new LiteralStackNode<IConstructor>(5920, 6, prod__lit___41__char_class___range__41_41_, new int[] {41}, null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(5918, 5, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new SeparatedListStackNode<IConstructor>(5908, 4, regular__iter_star_seps__Pattern__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(5910, 0, "Pattern", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(5912, 1, "layouts_LAYOUTLIST", null, null), new LiteralStackNode<IConstructor>(5914, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode<IConstructor>(5916, 3, "layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(5906, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(5904, 2, prod__lit___40__char_class___range__40_40_, new int[] {40}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(5902, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(5900, 0, "Pattern", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__CallOrTree_Pattern__expression_Pattern_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_arguments_iter_star_seps__Pattern__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__Map_Pattern__lit___40_layouts_LAYOUTLIST_mappings_iter_star_seps__Mapping__Pattern__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_(builder);
      
        _init_prod__List_Pattern__lit___91_layouts_LAYOUTLIST_elements_iter_star_seps__Pattern__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___93_(builder);
      
        _init_prod__QualifiedName_Pattern__qualifiedName_QualifiedName_(builder);
      
        _init_prod__SplicePlus_Pattern__lit___43_layouts_LAYOUTLIST_argument_Pattern_(builder);
      
        _init_prod__AsType_Pattern__lit___91_layouts_LAYOUTLIST_type_Type_layouts_LAYOUTLIST_lit___93_layouts_LAYOUTLIST_argument_Pattern_(builder);
      
        _init_prod__Negative_Pattern__lit___layouts_LAYOUTLIST_argument_Pattern_(builder);
      
        _init_prod__Descendant_Pattern__lit___47_layouts_LAYOUTLIST_pattern_Pattern_(builder);
      
        _init_prod__MultiVariable_Pattern__qualifiedName_QualifiedName_layouts_LAYOUTLIST_lit___42_(builder);
      
        _init_prod__TypedVariable_Pattern__type_Type_layouts_LAYOUTLIST_name_Name_(builder);
      
        _init_prod__Set_Pattern__lit___123_layouts_LAYOUTLIST_elements_iter_star_seps__Pattern__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_(builder);
      
        _init_prod__ReifiedType_Pattern__lit_type_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_symbol_Pattern_layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_definitions_Pattern_layouts_LAYOUTLIST_lit___41_(builder);
      
        _init_prod__Splice_Pattern__lit___42_layouts_LAYOUTLIST_argument_Pattern_(builder);
      
        _init_prod__Literal_Pattern__literal_Literal_(builder);
      
        _init_prod__Tuple_Pattern__lit___60_layouts_LAYOUTLIST_elements_iter_seps__Pattern__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___62_(builder);
      
        _init_prod__Anti_Pattern__lit___33_layouts_LAYOUTLIST_pattern_Pattern_(builder);
      
        _init_prod__TypedVariableBecomes_Pattern__type_Type_layouts_LAYOUTLIST_name_Name_layouts_LAYOUTLIST_lit___58_layouts_LAYOUTLIST_pattern_Pattern_(builder);
      
        _init_prod__VariableBecomes_Pattern__name_Name_layouts_LAYOUTLIST_lit___58_layouts_LAYOUTLIST_pattern_Pattern_(builder);
      
        _init_prod__CallOrTree_Pattern__expression_Pattern_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_arguments_iter_star_seps__Pattern__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_(builder);
      
    }
  }
	
  protected static class DateAndTime {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__DateAndTime__lit___36_DatePart_lit_T_conditional__TimePartNoTZ__not_follow__char_class___range__43_43_range__45_45_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[4];
      
      tmp[3] = new NonTerminalStackNode<IConstructor>(6004, 3, "TimePartNoTZ", null, new ICompletionFilter[] {new CharFollowRestriction(new int[][]{{43,43},{45,45}})});
      tmp[2] = new LiteralStackNode<IConstructor>(6002, 2, prod__lit_T__char_class___range__84_84_, new int[] {84}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(6000, 1, "DatePart", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(5998, 0, prod__lit___36__char_class___range__36_36_, new int[] {36}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__DateAndTime__lit___36_DatePart_lit_T_conditional__TimePartNoTZ__not_follow__char_class___range__43_43_range__45_45_, tmp);
	}
    protected static final void _init_prod__DateAndTime__lit___36_DatePart_lit_T_TimePartNoTZ_TimeZonePart_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(6014, 4, "TimeZonePart", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(6012, 3, "TimePartNoTZ", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(6010, 2, prod__lit_T__char_class___range__84_84_, new int[] {84}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(6008, 1, "DatePart", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(6006, 0, prod__lit___36__char_class___range__36_36_, new int[] {36}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__DateAndTime__lit___36_DatePart_lit_T_TimePartNoTZ_TimeZonePart_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__DateAndTime__lit___36_DatePart_lit_T_conditional__TimePartNoTZ__not_follow__char_class___range__43_43_range__45_45_(builder);
      
        _init_prod__DateAndTime__lit___36_DatePart_lit_T_TimePartNoTZ_TimeZonePart_(builder);
      
    }
  }
	
  protected static class Tag {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__Default_Tag__lit___64_layouts_LAYOUTLIST_name_Name_layouts_LAYOUTLIST_contents_TagString__tag__Folded_tag__category___67_111_109_109_101_110_116(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(6024, 4, "TagString", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(6022, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(6020, 2, "Name", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(6018, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(6016, 0, prod__lit___64__char_class___range__64_64_, new int[] {64}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Default_Tag__lit___64_layouts_LAYOUTLIST_name_Name_layouts_LAYOUTLIST_contents_TagString__tag__Folded_tag__category___67_111_109_109_101_110_116, tmp);
	}
    protected static final void _init_prod__Expression_Tag__lit___64_layouts_LAYOUTLIST_name_Name_layouts_LAYOUTLIST_lit___61_layouts_LAYOUTLIST_expression_Expression__tag__Folded_tag__category___67_111_109_109_101_110_116(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[7];
      
      tmp[6] = new NonTerminalStackNode<IConstructor>(6038, 6, "Expression", null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(6036, 5, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode<IConstructor>(6034, 4, prod__lit___61__char_class___range__61_61_, new int[] {61}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(6032, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(6030, 2, "Name", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(6028, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(6026, 0, prod__lit___64__char_class___range__64_64_, new int[] {64}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Expression_Tag__lit___64_layouts_LAYOUTLIST_name_Name_layouts_LAYOUTLIST_lit___61_layouts_LAYOUTLIST_expression_Expression__tag__Folded_tag__category___67_111_109_109_101_110_116, tmp);
	}
    protected static final void _init_prod__Empty_Tag__lit___64_layouts_LAYOUTLIST_name_Name__tag__Folded_tag__category___67_111_109_109_101_110_116(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode<IConstructor>(6044, 2, "Name", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(6042, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(6040, 0, prod__lit___64__char_class___range__64_64_, new int[] {64}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Empty_Tag__lit___64_layouts_LAYOUTLIST_name_Name__tag__Folded_tag__category___67_111_109_109_101_110_116, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__Default_Tag__lit___64_layouts_LAYOUTLIST_name_Name_layouts_LAYOUTLIST_contents_TagString__tag__Folded_tag__category___67_111_109_109_101_110_116(builder);
      
        _init_prod__Expression_Tag__lit___64_layouts_LAYOUTLIST_name_Name_layouts_LAYOUTLIST_lit___61_layouts_LAYOUTLIST_expression_Expression__tag__Folded_tag__category___67_111_109_109_101_110_116(builder);
      
        _init_prod__Empty_Tag__lit___64_layouts_LAYOUTLIST_name_Name__tag__Folded_tag__category___67_111_109_109_101_110_116(builder);
      
    }
  }
	
  public ObjectRascalRascal() {
    super();
  }

  // Parse methods    
  
  public AbstractStackNode<IConstructor>[] Literal() {
    return Literal.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] Module() {
    return Module.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] PreProtocolChars() {
    return PreProtocolChars.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] TypeArg() {
    return TypeArg.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] Variable() {
    return Variable.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] Renaming() {
    return Renaming.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] Catch() {
    return Catch.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] Signature() {
    return Signature.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] Sym() {
    return Sym.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] NonterminalLabel() {
    return NonterminalLabel.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] Header() {
    return Header.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] Commands() {
    return Commands.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] ImportedModule() {
    return ImportedModule.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] Expression() {
    return Expression.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] TagString() {
    return TagString.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] Nonterminal() {
    return Nonterminal.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] PreModule() {
    return PreModule.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] ProtocolPart() {
    return ProtocolPart.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] Comment() {
    return Comment.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] Label() {
    return Label.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] Field() {
    return Field.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] EvalCommand() {
    return EvalCommand.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] FunctionModifier() {
    return FunctionModifier.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] Assignment() {
    return Assignment.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] ProtocolChars() {
    return ProtocolChars.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] Assignable() {
    return Assignable.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] HeaderKeyword() {
    return HeaderKeyword.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] Parameters() {
    return Parameters.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] DatePart() {
    return DatePart.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] Mapping__Pattern() {
    return Mapping__Pattern.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] LocalVariableDeclaration() {
    return LocalVariableDeclaration.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] StringConstant() {
    return StringConstant.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] start__Module() {
    return start__Module.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] DataTypeSelector() {
    return DataTypeSelector.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] StringTail() {
    return StringTail.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] PatternWithAction() {
    return PatternWithAction.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] PathTail() {
    return PathTail.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] MidProtocolChars() {
    return MidProtocolChars.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] JustDate() {
    return JustDate.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] CaseInsensitiveStringConstant() {
    return CaseInsensitiveStringConstant.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] start__Commands() {
    return start__Commands.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] Backslash() {
    return Backslash.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] Tags() {
    return Tags.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] Formals() {
    return Formals.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] Start() {
    return Start.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] Name() {
    return Name.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] TimePartNoTZ() {
    return TimePartNoTZ.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] StructuredType() {
    return StructuredType.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] Declarator() {
    return Declarator.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] start__PreModule() {
    return start__PreModule.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] Variant() {
    return Variant.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] FunctionDeclaration() {
    return FunctionDeclaration.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] BasicType() {
    return BasicType.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] DateTimeLiteral() {
    return DateTimeLiteral.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] PathChars() {
    return PathChars.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] ModuleActuals() {
    return ModuleActuals.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] start__EvalCommand() {
    return start__EvalCommand.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] PostStringChars() {
    return PostStringChars.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] HexIntegerLiteral() {
    return HexIntegerLiteral.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] RegExpLiteral() {
    return RegExpLiteral.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] NamedRegExp() {
    return NamedRegExp.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] ModuleParameters() {
    return ModuleParameters.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] RascalKeywords() {
    return RascalKeywords.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] Strategy() {
    return Strategy.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] Char() {
    return Char.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] PrePathChars() {
    return PrePathChars.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] MidPathChars() {
    return MidPathChars.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] PostProtocolChars() {
    return PostProtocolChars.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] SyntaxDefinition() {
    return SyntaxDefinition.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] Kind() {
    return Kind.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] Target() {
    return Target.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] IntegerLiteral() {
    return IntegerLiteral.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] start__Command() {
    return start__Command.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] FunctionBody() {
    return FunctionBody.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] Import() {
    return Import.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] UserType() {
    return UserType.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] LAYOUT() {
    return LAYOUT.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] Body() {
    return Body.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] DecimalIntegerLiteral() {
    return DecimalIntegerLiteral.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] StringTemplate() {
    return StringTemplate.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] PathPart() {
    return PathPart.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] RegExp() {
    return RegExp.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] MidStringChars() {
    return MidStringChars.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] RegExpModifier() {
    return RegExpModifier.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] layouts_$BACKTICKS() {
    return layouts_$BACKTICKS.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] Assoc() {
    return Assoc.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] Replacement() {
    return Replacement.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] DataTarget() {
    return DataTarget.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] layouts_$default$() {
    return layouts_$default$.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] RealLiteral() {
    return RealLiteral.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] layouts_LAYOUTLIST() {
    return layouts_LAYOUTLIST.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] StringCharacter() {
    return StringCharacter.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] JustTime() {
    return JustTime.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] Rest() {
    return Rest.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] Range() {
    return Range.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] LocationLiteral() {
    return LocationLiteral.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] ShellCommand() {
    return ShellCommand.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] URLChars() {
    return URLChars.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] StringMiddle() {
    return StringMiddle.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] QualifiedName() {
    return QualifiedName.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] TimeZonePart() {
    return TimeZonePart.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] PreStringChars() {
    return PreStringChars.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] Command() {
    return Command.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] Visit() {
    return Visit.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] ProtocolTail() {
    return ProtocolTail.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] NamedBackslash() {
    return NamedBackslash.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] Visibility() {
    return Visibility.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] PostPathChars() {
    return PostPathChars.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] StringLiteral() {
    return StringLiteral.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] Renamings() {
    return Renamings.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] layouts_$QUOTES() {
    return layouts_$QUOTES.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] Statement() {
    return Statement.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] FunctionType() {
    return FunctionType.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] Case() {
    return Case.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] Bound() {
    return Bound.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] Type() {
    return Type.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] Declaration() {
    return Declaration.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] Class() {
    return Class.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] Mapping__Expression() {
    return Mapping__Expression.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] FunctionModifiers() {
    return FunctionModifiers.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] Comprehension() {
    return Comprehension.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] ProdModifier() {
    return ProdModifier.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] Toplevel() {
    return Toplevel.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] BooleanLiteral() {
    return BooleanLiteral.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] TypeVar() {
    return TypeVar.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] RationalLiteral() {
    return RationalLiteral.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] UnicodeEscape() {
    return UnicodeEscape.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] Prod() {
    return Prod.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] OctalIntegerLiteral() {
    return OctalIntegerLiteral.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] Pattern() {
    return Pattern.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] DateAndTime() {
    return DateAndTime.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] Tag() {
    return Tag.EXPECTS;
  }
}