package org.rascalmpl.library.lang.rascal.syntax;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IValue;
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
import org.rascalmpl.values.uptr.Factory;

public class ObjectRascalRascal extends org.rascalmpl.library.lang.rascal.syntax.RascalRascal {
  
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
    
    
    _putDontNest(result, 1414, 1424);
    _putDontNest(result, 1354, 1364);
    _putDontNest(result, 1344, 1374);
    _putDontNest(result, 1286, 1296);
    _putDontNest(result, 1126, 1266);
    _putDontNest(result, 1132, 1256);
    _putDontNest(result, 1120, 1276);
    _putDontNest(result, 1078, 1186);
    _putDontNest(result, 992, 1130);
    _putDontNest(result, 1014, 1124);
    _putDontNest(result, 1120, 1384);
    _putDontNest(result, 1132, 1404);
    _putDontNest(result, 398, 444);
    _putDontNest(result, 1288, 1286);
    _putDontNest(result, 1396, 1394);
    _putDontNest(result, 1336, 1334);
    _putDontNest(result, 1394, 1404);
    _putDontNest(result, 1118, 1226);
    _putDontNest(result, 1142, 1266);
    _putDontNest(result, 1014, 1140);
    _putDontNest(result, 1248, 1306);
    _putDontNest(result, 692, 1076);
    _putDontNest(result, 1078, 1334);
    _putDontNest(result, 1176, 1424);
    _putDontNest(result, 1068, 1324);
    _putDontNest(result, 1118, 1374);
    _putDontNest(result, 1346, 1404);
    _putDontNest(result, 1288, 1334);
    _putDontNest(result, 1298, 1324);
    _putDontNest(result, 1088, 1276);
    _putDontNest(result, 992, 1098);
    _putDontNest(result, 1014, 1092);
    _putDontNest(result, 1098, 1394);
    _putDontNest(result, 1078, 1286);
    _putDontNest(result, 1088, 1384);
    _putDontNest(result, 1206, 1414);
    _putDontNest(result, 250, 454);
    _putDontNest(result, 400, 434);
    _putDontNest(result, 1364, 1394);
    _putDontNest(result, 1374, 1384);
    _putDontNest(result, 1104, 1276);
    _putDontNest(result, 1058, 1166);
    _putDontNest(result, 4704, 4982);
    _putDontNest(result, 4974, 4982);
    _putDontNest(result, 1216, 1306);
    _putDontNest(result, 1104, 1384);
    _putDontNest(result, 1208, 1424);
    _putDontNest(result, 1036, 1324);
    _putDontNest(result, 1326, 1384);
    _putDontNest(result, 1142, 1186);
    _putDontNest(result, 1068, 1256);
    _putDontNest(result, 692, 1124);
    _putDontNest(result, 1126, 1334);
    _putDontNest(result, 1068, 1404);
    _putDontNest(result, 1238, 1414);
    _putDontNest(result, 1296, 1374);
    _putDontNest(result, 1286, 1344);
    _putDontNest(result, 1308, 1354);
    _putDontNest(result, 1306, 1364);
    _putDontNest(result, 1126, 1186);
    _putDontNest(result, 1078, 1266);
    _putDontNest(result, 1120, 1196);
    _putDontNest(result, 692, 1140);
    _putDontNest(result, 1132, 1324);
    _putDontNest(result, 1142, 1334);
    _putDontNest(result, 436, 454);
    _putDontNest(result, 1334, 1344);
    _putDontNest(result, 1324, 1354);
    _putDontNest(result, 1036, 1256);
    _putDontNest(result, 1058, 1246);
    _putDontNest(result, 1104, 1196);
    _putDontNest(result, 1098, 1206);
    _putDontNest(result, 4658, 4976);
    _putDontNest(result, 1168, 1306);
    _putDontNest(result, 692, 1092);
    _putDontNest(result, 1058, 1354);
    _putDontNest(result, 1256, 1424);
    _putDontNest(result, 1142, 1286);
    _putDontNest(result, 1036, 1404);
    _putDontNest(result, 1298, 1276);
    _putDontNest(result, 1298, 1404);
    _putDontNest(result, 1088, 1196);
    _putDontNest(result, 1126, 1286);
    _putDontNest(result, 1226, 1236);
    _putDontNest(result, 1088, 1118);
    _putDontNest(result, 1216, 1246);
    _putDontNest(result, 1286, 1424);
    _putDontNest(result, 5728, 5728);
    _putDontNest(result, 1258, 1394);
    _putDontNest(result, 1248, 1384);
    _putDontNest(result, 1218, 1354);
    _putDontNest(result, 1186, 1196);
    _putDontNest(result, 1266, 1276);
    _putDontNest(result, 1288, 1414);
    _putDontNest(result, 692, 1206);
    _putDontNest(result, 1120, 1306);
    _putDontNest(result, 1276, 1404);
    _putDontNest(result, 1196, 1324);
    _putDontNest(result, 1176, 1296);
    _putDontNest(result, 1206, 1334);
    _putDontNest(result, 1246, 1374);
    _putDontNest(result, 1236, 1364);
    _putDontNest(result, 1118, 1176);
    _putDontNest(result, 1218, 1276);
    _putDontNest(result, 1098, 1140);
    _putDontNest(result, 318, 444);
    _putDontNest(result, 1336, 1414);
    _putDontNest(result, 256, 434);
    _putDontNest(result, 992, 1236);
    _putDontNest(result, 992, 1354);
    _putDontNest(result, 1104, 1306);
    _putDontNest(result, 1206, 1286);
    _putDontNest(result, 1228, 1404);
    _putDontNest(result, 1226, 1394);
    _putDontNest(result, 1216, 1384);
    _putDontNest(result, 1078, 1414);
    _putDontNest(result, 1246, 1256);
    _putDontNest(result, 1236, 1266);
    _putDontNest(result, 1098, 1124);
    _putDontNest(result, 1176, 1206);
    _putDontNest(result, 1334, 1424);
    _putDontNest(result, 4704, 4724);
    _putDontNest(result, 1014, 1226);
    _putDontNest(result, 4586, 4982);
    _putDontNest(result, 1014, 1364);
    _putDontNest(result, 1088, 1306);
    _putDontNest(result, 1268, 1364);
    _putDontNest(result, 1256, 1344);
    _putDontNest(result, 1266, 1354);
    _putDontNest(result, 1278, 1374);
    _putDontNest(result, 1208, 1296);
    _putDontNest(result, 1186, 1276);
    _putDontNest(result, 1188, 1266);
    _putDontNest(result, 1068, 1130);
    _putDontNest(result, 1198, 1256);
    _putDontNest(result, 1036, 1098);
    _putDontNest(result, 444, 414);
    _putDontNest(result, 1176, 1344);
    _putDontNest(result, 1196, 1404);
    _putDontNest(result, 1238, 1286);
    _putDontNest(result, 1276, 1324);
    _putDontNest(result, 1168, 1246);
    _putDontNest(result, 1178, 1236);
    _putDontNest(result, 692, 1384);
    _putDontNest(result, 1206, 1216);
    _putDontNest(result, 1196, 1226);
    _putDontNest(result, 1068, 1098);
    _putDontNest(result, 1036, 1130);
    _putDontNest(result, 330, 424);
    _putDontNest(result, 1188, 1364);
    _putDontNest(result, 1256, 1296);
    _putDontNest(result, 1198, 1374);
    _putDontNest(result, 1186, 1354);
    _putDontNest(result, 1142, 1414);
    _putDontNest(result, 1208, 1344);
    _putDontNest(result, 1078, 1104);
    _putDontNest(result, 1384, 1414);
    _putDontNest(result, 1286, 1306);
    _putDontNest(result, 1126, 1414);
    _putDontNest(result, 1228, 1324);
    _putDontNest(result, 1238, 1334);
    _putDontNest(result, 1178, 1394);
    _putDontNest(result, 1168, 1384);
    _putDontNest(result, 1098, 1236);
    _putDontNest(result, 1088, 1246);
    _putDontNest(result, 992, 1140);
    _putDontNest(result, 1088, 1354);
    _putDontNest(result, 1386, 1394);
    _putDontNest(result, 1346, 1354);
    _putDontNest(result, 400, 444);
    _putDontNest(result, 5390, 5390);
    _putDontNest(result, 5420, 5420);
    _putDontNest(result, 1288, 1296);
    _putDontNest(result, 1104, 1246);
    _putDontNest(result, 1058, 1196);
    _putDontNest(result, 992, 1124);
    _putDontNest(result, 1014, 1130);
    _putDontNest(result, 1098, 1344);
    _putDontNest(result, 1178, 1424);
    _putDontNest(result, 992, 1306);
    _putDontNest(result, 1104, 1354);
    _putDontNest(result, 1364, 1364);
    _putDontNest(result, 1324, 1324);
    _putDontNest(result, 1414, 1414);
    _putDontNest(result, 1334, 1334);
    _putDontNest(result, 1374, 1374);
    _putDontNest(result, 1404, 1404);
    _putDontNest(result, 1286, 1286);
    _putDontNest(result, 1132, 1226);
    _putDontNest(result, 1142, 1216);
    _putDontNest(result, 1120, 1246);
    _putDontNest(result, 1238, 1306);
    _putDontNest(result, 1132, 1374);
    _putDontNest(result, 1126, 1364);
    _putDontNest(result, 1120, 1354);
    _putDontNest(result, 1354, 1394);
    _putDontNest(result, 1344, 1384);
    _putDontNest(result, 398, 434);
    _putDontNest(result, 1286, 1334);
    _putDontNest(result, 1126, 1216);
    _putDontNest(result, 1118, 1256);
    _putDontNest(result, 4704, 4976);
    _putDontNest(result, 1014, 1098);
    _putDontNest(result, 992, 1092);
    _putDontNest(result, 1142, 1364);
    _putDontNest(result, 1188, 1414);
    _putDontNest(result, 1118, 1404);
    _putDontNest(result, 1036, 1226);
    _putDontNest(result, 1104, 1166);
    _putDontNest(result, 1058, 1276);
    _putDontNest(result, 1206, 1306);
    _putDontNest(result, 1098, 1296);
    _putDontNest(result, 1236, 1414);
    _putDontNest(result, 1036, 1374);
    _putDontNest(result, 1226, 1424);
    _putDontNest(result, 1058, 1384);
    _putDontNest(result, 1324, 1404);
    _putDontNest(result, 1088, 1166);
    _putDontNest(result, 1298, 1354);
    _putDontNest(result, 1288, 1344);
    _putDontNest(result, 1068, 1226);
    _putDontNest(result, 1078, 1216);
    _putDontNest(result, 4658, 4982);
    _putDontNest(result, 1268, 1414);
    _putDontNest(result, 1258, 1424);
    _putDontNest(result, 1118, 1324);
    _putDontNest(result, 1068, 1374);
    _putDontNest(result, 1336, 1344);
    _putDontNest(result, 1326, 1374);
    _putDontNest(result, 1120, 1166);
    _putDontNest(result, 1078, 1364);
    _putDontNest(result, 1296, 1384);
    _putDontNest(result, 426, 454);
    _putDontNest(result, 1308, 1404);
    _putDontNest(result, 1306, 1394);
    _putDontNest(result, 5720, 5738);
    _putDontNest(result, 1142, 1306);
    _putDontNest(result, 1216, 1354);
    _putDontNest(result, 1228, 1374);
    _putDontNest(result, 1188, 1334);
    _putDontNest(result, 1256, 1394);
    _putDontNest(result, 1248, 1256);
    _putDontNest(result, 1258, 1266);
    _putDontNest(result, 1288, 1424);
    _putDontNest(result, 1014, 1256);
    _putDontNest(result, 1126, 1306);
    _putDontNest(result, 1238, 1364);
    _putDontNest(result, 1278, 1404);
    _putDontNest(result, 1226, 1344);
    _putDontNest(result, 1266, 1384);
    _putDontNest(result, 1198, 1324);
    _putDontNest(result, 1178, 1296);
    _putDontNest(result, 1196, 1196);
    _putDontNest(result, 1236, 1236);
    _putDontNest(result, 1206, 1206);
    _putDontNest(result, 1120, 1176);
    _putDontNest(result, 1246, 1246);
    _putDontNest(result, 1276, 1276);
    _putDontNest(result, 330, 454);
    _putDontNest(result, 256, 444);
    _putDontNest(result, 1286, 1414);
    _putDontNest(result, 318, 434);
    _putDontNest(result, 992, 1364);
    _putDontNest(result, 1218, 1384);
    _putDontNest(result, 1248, 1354);
    _putDontNest(result, 1104, 1176);
    _putDontNest(result, 1226, 1266);
    _putDontNest(result, 1228, 1276);
    _putDontNest(result, 1216, 1256);
    _putDontNest(result, 1334, 1414);
    _putDontNest(result, 1014, 1354);
    _putDontNest(result, 1188, 1286);
    _putDontNest(result, 1246, 1404);
    _putDontNest(result, 1258, 1344);
    _putDontNest(result, 1276, 1374);
    _putDontNest(result, 1088, 1176);
    _putDontNest(result, 1336, 1424);
    _putDontNest(result, 992, 1206);
    _putDontNest(result, 1078, 1306);
    _putDontNest(result, 1278, 1324);
    _putDontNest(result, 1186, 1384);
    _putDontNest(result, 1098, 1424);
    _putDontNest(result, 1236, 1286);
    _putDontNest(result, 1226, 1296);
    _putDontNest(result, 1178, 1344);
    _putDontNest(result, 1198, 1404);
    _putDontNest(result, 1176, 1216);
    _putDontNest(result, 1058, 1130);
    _putDontNest(result, 1196, 1276);
    _putDontNest(result, 434, 414);
    _putDontNest(result, 692, 1256);
    _putDontNest(result, 1208, 1394);
    _putDontNest(result, 1268, 1334);
    _putDontNest(result, 1168, 1354);
    _putDontNest(result, 5382, 5400);
    _putDontNest(result, 1258, 1296);
    _putDontNest(result, 1246, 1324);
    _putDontNest(result, 1196, 1374);
    _putDontNest(result, 1268, 1286);
    _putDontNest(result, 1198, 1246);
    _putDontNest(result, 1186, 1226);
    _putDontNest(result, 1188, 1236);
    _putDontNest(result, 1058, 1098);
    _putDontNest(result, 1384, 1424);
    _putDontNest(result, 692, 1354);
    _putDontNest(result, 1206, 1364);
    _putDontNest(result, 1176, 1394);
    _putDontNest(result, 1236, 1334);
    _putDontNest(result, 1168, 1256);
    _putDontNest(result, 1178, 1266);
    _putDontNest(result, 1014, 1176);
    _putDontNest(result, 1268, 1306);
    _putDontNest(result, 1098, 1374);
    _putDontNest(result, 1014, 1306);
    _putDontNest(result, 1036, 1296);
    _putDontNest(result, 1058, 1334);
    _putDontNest(result, 1088, 1364);
    _putDontNest(result, 1344, 1354);
    _putDontNest(result, 1384, 1394);
    _putDontNest(result, 1132, 1276);
    _putDontNest(result, 1120, 1256);
    _putDontNest(result, 5366, 5400);
    _putDontNest(result, 1104, 1364);
    _putDontNest(result, 1306, 1296);
    _putDontNest(result, 1118, 1246);
    _putDontNest(result, 1078, 1206);
    _putDontNest(result, 1068, 1196);
    _putDontNest(result, 4724, 4982);
    _putDontNest(result, 1236, 1306);
    _putDontNest(result, 1126, 1354);
    _putDontNest(result, 1120, 1364);
    _putDontNest(result, 1196, 1424);
    _putDontNest(result, 1068, 1296);
    _putDontNest(result, 1346, 1384);
    _putDontNest(result, 5390, 5420);
    _putDontNest(result, 1088, 1256);
    _putDontNest(result, 1098, 1266);
    _putDontNest(result, 1132, 1344);
    _putDontNest(result, 1186, 1414);
    _putDontNest(result, 1058, 1286);
    _putDontNest(result, 1118, 1394);
    _putDontNest(result, 1142, 1354);
    _putDontNest(result, 1374, 1404);
    _putDontNest(result, 5400, 5410);
    _putDontNest(result, 1036, 1196);
    _putDontNest(result, 1104, 1256);
    _putDontNest(result, 4628, 4982);
    _putDontNest(result, 692, 1176);
    _putDontNest(result, 1228, 1424);
    _putDontNest(result, 1326, 1404);
    _putDontNest(result, 1306, 1344);
    _putDontNest(result, 1286, 1364);
    _putDontNest(result, 1118, 1166);
    _putDontNest(result, 1126, 1206);
    _putDontNest(result, 1068, 1276);
    _putDontNest(result, 1188, 1306);
    _putDontNest(result, 1218, 1414);
    _putDontNest(result, 1036, 1344);
    _putDontNest(result, 250, 434);
    _putDontNest(result, 1296, 1354);
    _putDontNest(result, 1336, 1394);
    _putDontNest(result, 1308, 1374);
    _putDontNest(result, 1132, 1196);
    _putDontNest(result, 1142, 1206);
    _putDontNest(result, 1132, 1296);
    _putDontNest(result, 1266, 1414);
    _putDontNest(result, 1288, 1394);
    _putDontNest(result, 1324, 1374);
    _putDontNest(result, 1036, 1276);
    _putDontNest(result, 1058, 1226);
    _putDontNest(result, 992, 1414);
    _putDontNest(result, 1276, 1424);
    _putDontNest(result, 1078, 1354);
    _putDontNest(result, 1068, 1344);
    _putDontNest(result, 1298, 1384);
    _putDontNest(result, 256, 414);
    _putDontNest(result, 424, 454);
    _putDontNest(result, 1334, 1364);
    _putDontNest(result, 1098, 1186);
    _putDontNest(result, 4704, 4704);
    _putDontNest(result, 692, 1324);
    _putDontNest(result, 1036, 1424);
    _putDontNest(result, 1216, 1364);
    _putDontNest(result, 1186, 1334);
    _putDontNest(result, 1256, 1404);
    _putDontNest(result, 1208, 1324);
    _putDontNest(result, 1226, 1374);
    _putDontNest(result, 1088, 1098);
    _putDontNest(result, 1216, 1226);
    _putDontNest(result, 1188, 1206);
    _putDontNest(result, 1256, 1266);
    _putDontNest(result, 1126, 1140);
    _putDontNest(result, 1014, 1384);
    _putDontNest(result, 1238, 1354);
    _putDontNest(result, 1228, 1344);
    _putDontNest(result, 1268, 1384);
    _putDontNest(result, 1278, 1394);
    _putDontNest(result, 1278, 1276);
    _putDontNest(result, 1266, 1256);
    _putDontNest(result, 1306, 1424);
    _putDontNest(result, 1258, 1374);
    _putDontNest(result, 1176, 1324);
    _putDontNest(result, 1068, 1424);
    _putDontNest(result, 1196, 1296);
    _putDontNest(result, 1248, 1364);
    _putDontNest(result, 1276, 1344);
    _putDontNest(result, 1088, 1130);
    _putDontNest(result, 1218, 1256);
    _putDontNest(result, 1176, 1186);
    _putDontNest(result, 5738, 5738);
    _putDontNest(result, 1014, 1246);
    _putDontNest(result, 992, 1216);
    _putDontNest(result, 1246, 1394);
    _putDontNest(result, 1058, 1414);
    _putDontNest(result, 1186, 1286);
    _putDontNest(result, 1236, 1384);
    _putDontNest(result, 1246, 1276);
    _putDontNest(result, 1104, 1130);
    _putDontNest(result, 992, 1334);
    _putDontNest(result, 1188, 1384);
    _putDontNest(result, 1198, 1394);
    _putDontNest(result, 1228, 1296);
    _putDontNest(result, 1198, 1276);
    _putDontNest(result, 1178, 1216);
    _putDontNest(result, 1078, 1124);
    _putDontNest(result, 1036, 1118);
    _putDontNest(result, 1186, 1256);
    _putDontNest(result, 1354, 1424);
    _putDontNest(result, 1364, 1414);
    _putDontNest(result, 692, 1404);
    _putDontNest(result, 1266, 1334);
    _putDontNest(result, 1168, 1364);
    _putDontNest(result, 1208, 1404);
    _putDontNest(result, 1256, 1324);
    _putDontNest(result, 1218, 1286);
    _putDontNest(result, 1178, 1374);
    _putDontNest(result, 1208, 1266);
    _putDontNest(result, 1058, 1176);
    _putDontNest(result, 1078, 1140);
    _putDontNest(result, 1168, 1226);
    _putDontNest(result, 1014, 1166);
    _putDontNest(result, 1306, 1306);
    _putDontNest(result, 1266, 1286);
    _putDontNest(result, 1132, 1424);
    _putDontNest(result, 1218, 1334);
    _putDontNest(result, 1068, 1118);
    _putDontNest(result, 1078, 1092);
    _putDontNest(result, 1196, 1246);
    _putDontNest(result, 312, 454);
    _putDontNest(result, 1396, 1414);
    _putDontNest(result, 1386, 1424);
    _putDontNest(result, 400, 414);
    _putDontNest(result, 5400, 5400);
    _putDontNest(result, 692, 1226);
    _putDontNest(result, 992, 1286);
    _putDontNest(result, 1196, 1344);
    _putDontNest(result, 1176, 1404);
    _putDontNest(result, 1276, 1296);
    _putDontNest(result, 1206, 1354);
    _putDontNest(result, 1176, 1266);
    _putDontNest(result, 1206, 1236);
    _putDontNest(result, 1132, 1394);
    _putDontNest(result, 1168, 1414);
    _putDontNest(result, 1126, 1384);
    _putDontNest(result, 1118, 1344);
    _putDontNest(result, 1384, 1404);
    _putDontNest(result, 1354, 1374);
    _putDontNest(result, 1298, 1286);
    _putDontNest(result, 1344, 1364);
    _putDontNest(result, 426, 434);
    _putDontNest(result, 1088, 1226);
    _putDontNest(result, 1258, 1306);
    _putDontNest(result, 692, 1066);
    _putDontNest(result, 1142, 1384);
    _putDontNest(result, 1098, 1216);
    _putDontNest(result, 1036, 1166);
    _putDontNest(result, 1104, 1226);
    _putDontNest(result, 4724, 4976);
    _putDontNest(result, 992, 1104);
    _putDontNest(result, 1098, 1404);
    _putDontNest(result, 1198, 1424);
    _putDontNest(result, 436, 444);
    _putDontNest(result, 5390, 5410);
    _putDontNest(result, 1132, 1246);
    _putDontNest(result, 1120, 1226);
    _putDontNest(result, 1126, 1236);
    _putDontNest(result, 1014, 1118);
    _putDontNest(result, 1226, 1306);
    _putDontNest(result, 692, 1034);
    _putDontNest(result, 1364, 1384);
    _putDontNest(result, 1288, 1324);
    _putDontNest(result, 5400, 5420);
    _putDontNest(result, 1298, 1334);
    _putDontNest(result, 1374, 1394);
    _putDontNest(result, 400, 424);
    _putDontNest(result, 1068, 1166);
    _putDontNest(result, 1142, 1236);
    _putDontNest(result, 1118, 1276);
    _putDontNest(result, 4628, 4976);
    _putDontNest(result, 1068, 1394);
    _putDontNest(result, 1104, 1286);
    _putDontNest(result, 1120, 1334);
    _putDontNest(result, 414, 454);
    _putDontNest(result, 1286, 1354);
    _putDontNest(result, 1326, 1394);
    _putDontNest(result, 1308, 1344);
    _putDontNest(result, 1058, 1256);
    _putDontNest(result, 1036, 1246);
    _putDontNest(result, 692, 1130);
    _putDontNest(result, 1078, 1384);
    _putDontNest(result, 1088, 1286);
    _putDontNest(result, 1246, 1424);
    _putDontNest(result, 1216, 1414);
    _putDontNest(result, 1118, 1296);
    _putDontNest(result, 398, 454);
    _putDontNest(result, 1306, 1374);
    _putDontNest(result, 1336, 1404);
    _putDontNest(result, 1296, 1364);
    _putDontNest(result, 992, 1424);
    _putDontNest(result, 1178, 1306);
    _putDontNest(result, 1058, 1364);
    _putDontNest(result, 1088, 1334);
    _putDontNest(result, 1036, 1394);
    _putDontNest(result, 1288, 1276);
    _putDontNest(result, 1288, 1404);
    _putDontNest(result, 1118, 1196);
    _putDontNest(result, 1068, 1246);
    _putDontNest(result, 692, 1098);
    _putDontNest(result, 1278, 1424);
    _putDontNest(result, 1120, 1286);
    _putDontNest(result, 1098, 1324);
    _putDontNest(result, 1104, 1334);
    _putDontNest(result, 1248, 1414);
    _putDontNest(result, 1324, 1344);
    _putDontNest(result, 1334, 1354);
    _putDontNest(result, 1078, 1236);
    _putDontNest(result, 992, 1266);
    _putDontNest(result, 692, 1196);
    _putDontNest(result, 1168, 1286);
    _putDontNest(result, 1218, 1364);
    _putDontNest(result, 1246, 1344);
    _putDontNest(result, 1258, 1404);
    _putDontNest(result, 1216, 1236);
    _putDontNest(result, 1186, 1206);
    _putDontNest(result, 1226, 1246);
    _putDontNest(result, 1256, 1276);
    _putDontNest(result, 1120, 1140);
    _putDontNest(result, 1298, 1414);
    _putDontNest(result, 1014, 1276);
    _putDontNest(result, 1276, 1394);
    _putDontNest(result, 1236, 1354);
    _putDontNest(result, 1308, 1424);
    _putDontNest(result, 4730, 4724);
    _putDontNest(result, 1278, 1344);
    _putDontNest(result, 1228, 1394);
    _putDontNest(result, 1198, 1296);
    _putDontNest(result, 1256, 1374);
    _putDontNest(result, 1226, 1404);
    _putDontNest(result, 1178, 1324);
    _putDontNest(result, 1176, 1196);
    _putDontNest(result, 1104, 1124);
    _putDontNest(result, 1088, 1140);
    _putDontNest(result, 1324, 1424);
    _putDontNest(result, 256, 424);
    _putDontNest(result, 4714, 4724);
    _putDontNest(result, 992, 1344);
    _putDontNest(result, 1014, 1374);
    _putDontNest(result, 1098, 1306);
    _putDontNest(result, 1238, 1384);
    _putDontNest(result, 1168, 1334);
    _putDontNest(result, 1266, 1364);
    _putDontNest(result, 1268, 1354);
    _putDontNest(result, 1104, 1140);
    _putDontNest(result, 1088, 1124);
    _putDontNest(result, 1236, 1256);
    _putDontNest(result, 1246, 1266);
    _putDontNest(result, 1014, 1196);
    _putDontNest(result, 1104, 1414);
    _putDontNest(result, 1248, 1334);
    _putDontNest(result, 1196, 1394);
    _putDontNest(result, 1036, 1104);
    _putDontNest(result, 1198, 1266);
    _putDontNest(result, 1188, 1256);
    _putDontNest(result, 5876, 5932);
    _putDontNest(result, 992, 1186);
    _putDontNest(result, 692, 1276);
    _putDontNest(result, 1258, 1324);
    _putDontNest(result, 1118, 1424);
    _putDontNest(result, 1246, 1296);
    _putDontNest(result, 1176, 1374);
    _putDontNest(result, 1088, 1414);
    _putDontNest(result, 1206, 1384);
    _putDontNest(result, 1216, 1286);
    _putDontNest(result, 1078, 1130);
    _putDontNest(result, 1168, 1236);
    _putDontNest(result, 1208, 1276);
    _putDontNest(result, 1178, 1246);
    _putDontNest(result, 1346, 1414);
    _putDontNest(result, 992, 1296);
    _putDontNest(result, 1186, 1364);
    _putDontNest(result, 1188, 1354);
    _putDontNest(result, 1216, 1334);
    _putDontNest(result, 1068, 1104);
    _putDontNest(result, 1394, 1414);
    _putDontNest(result, 318, 454);
    _putDontNest(result, 330, 434);
    _putDontNest(result, 692, 1374);
    _putDontNest(result, 1288, 1306);
    _putDontNest(result, 1198, 1344);
    _putDontNest(result, 1278, 1296);
    _putDontNest(result, 1226, 1324);
    _putDontNest(result, 1120, 1414);
    _putDontNest(result, 1248, 1286);
    _putDontNest(result, 1208, 1374);
    _putDontNest(result, 1178, 1404);
    _putDontNest(result, 1176, 1276);
    _putDontNest(result, 1078, 1098);
    _putDontNest(result, 1196, 1216);
    _putDontNest(result, 1206, 1226);
    _putDontNest(result, 1404, 1424);
    _putDontNest(result, 1104, 1344);
    _putDontNest(result, 1098, 1354);
    _putDontNest(result, 1346, 1364);
    _putDontNest(result, 1386, 1404);
    _putDontNest(result, 1296, 1286);
    _putDontNest(result, 1088, 1236);
    _putDontNest(result, 1098, 1246);
    _putDontNest(result, 1058, 1206);
    _putDontNest(result, 1256, 1306);
    _putDontNest(result, 1168, 1424);
    _putDontNest(result, 1088, 1344);
    _putDontNest(result, 434, 424);
    _putDontNest(result, 1404, 1394);
    _putDontNest(result, 1104, 1236);
    _putDontNest(result, 1132, 1364);
    _putDontNest(result, 1126, 1374);
    _putDontNest(result, 1354, 1404);
    _putDontNest(result, 1384, 1374);
    _putDontNest(result, 1306, 1324);
    _putDontNest(result, 1120, 1236);
    _putDontNest(result, 1126, 1226);
    _putDontNest(result, 4730, 4976);
    _putDontNest(result, 692, 1424);
    _putDontNest(result, 1142, 1374);
    _putDontNest(result, 1198, 1414);
    _putDontNest(result, 1120, 1344);
    _putDontNest(result, 1296, 1334);
    _putDontNest(result, 1142, 1226);
    _putDontNest(result, 1132, 1216);
    _putDontNest(result, 1118, 1266);
    _putDontNest(result, 4714, 4976);
    _putDontNest(result, 1208, 1306);
    _putDontNest(result, 1246, 1414);
    _putDontNest(result, 1216, 1424);
    _putDontNest(result, 1088, 1296);
    _putDontNest(result, 1118, 1286);
    _putDontNest(result, 1058, 1394);
    _putDontNest(result, 1036, 1364);
    _putDontNest(result, 692, 1306);
    _putDontNest(result, 1324, 1394);
    _putDontNest(result, 1288, 1374);
    _putDontNest(result, 1104, 1296);
    _putDontNest(result, 1298, 1364);
    _putDontNest(result, 1334, 1384);
    _putDontNest(result, 1036, 1216);
    _putDontNest(result, 1098, 1166);
    _putDontNest(result, 1176, 1306);
    _putDontNest(result, 1248, 1424);
    _putDontNest(result, 1068, 1364);
    _putDontNest(result, 1278, 1414);
    _putDontNest(result, 1120, 1296);
    _putDontNest(result, 444, 454);
    _putDontNest(result, 1344, 1334);
    _putDontNest(result, 1286, 1384);
    _putDontNest(result, 1118, 1186);
    _putDontNest(result, 1118, 1334);
    _putDontNest(result, 1078, 1374);
    _putDontNest(result, 1306, 1276);
    _putDontNest(result, 1308, 1394);
    _putDontNest(result, 1336, 1374);
    _putDontNest(result, 1306, 1404);
    _putDontNest(result, 1326, 1344);
    _putDontNest(result, 1078, 1226);
    _putDontNest(result, 1068, 1216);
    _putDontNest(result, 992, 1276);
    _putDontNest(result, 992, 1394);
    _putDontNest(result, 1226, 1354);
    _putDontNest(result, 1256, 1384);
    _putDontNest(result, 250, 414);
    _putDontNest(result, 1228, 1364);
    _putDontNest(result, 1198, 1334);
    _putDontNest(result, 1258, 1276);
    _putDontNest(result, 1142, 1176);
    _putDontNest(result, 1218, 1236);
    _putDontNest(result, 1296, 1414);
    _putDontNest(result, 4724, 4704);
    _putDontNest(result, 1014, 1266);
    _putDontNest(result, 1014, 1404);
    _putDontNest(result, 1216, 1344);
    _putDontNest(result, 1168, 1296);
    _putDontNest(result, 1266, 1394);
    _putDontNest(result, 1238, 1374);
    _putDontNest(result, 1268, 1404);
    _putDontNest(result, 1188, 1324);
    _putDontNest(result, 1196, 1186);
    _putDontNest(result, 1126, 1176);
    _putDontNest(result, 312, 434);
    _putDontNest(result, 4810, 4976);
    _putDontNest(result, 4730, 4714);
    _putDontNest(result, 4714, 4730);
    _putDontNest(result, 692, 1166);
    _putDontNest(result, 1258, 1354);
    _putDontNest(result, 1218, 1394);
    _putDontNest(result, 1226, 1276);
    _putDontNest(result, 1228, 1266);
    _putDontNest(result, 1326, 1424);
    _putDontNest(result, 4714, 4714);
    _putDontNest(result, 4730, 4730);
    _putDontNest(result, 692, 1296);
    _putDontNest(result, 1276, 1364);
    _putDontNest(result, 1198, 1286);
    _putDontNest(result, 1236, 1404);
    _putDontNest(result, 1248, 1344);
    _putDontNest(result, 1168, 1206);
    _putDontNest(result, 1238, 1256);
    _putDontNest(result, 1014, 1186);
    _putDontNest(result, 1014, 1324);
    _putDontNest(result, 1168, 1344);
    _putDontNest(result, 1268, 1324);
    _putDontNest(result, 1118, 1414);
    _putDontNest(result, 1246, 1286);
    _putDontNest(result, 1088, 1424);
    _putDontNest(result, 1188, 1404);
    _putDontNest(result, 1216, 1296);
    _putDontNest(result, 1186, 1394);
    _putDontNest(result, 1058, 1140);
    _putDontNest(result, 1196, 1266);
    _putDontNest(result, 1078, 1176);
    _putDontNest(result, 4760, 4982);
    _putDontNest(result, 992, 1196);
    _putDontNest(result, 1208, 1384);
    _putDontNest(result, 1278, 1334);
    _putDontNest(result, 1178, 1354);
    _putDontNest(result, 1104, 1424);
    _putDontNest(result, 1206, 1256);
    _putDontNest(result, 1176, 1246);
    _putDontNest(result, 1058, 1124);
    _putDontNest(result, 1344, 1414);
    _putDontNest(result, 1374, 1424);
    _putDontNest(result, 5876, 5926);
    _putDontNest(result, 692, 1344);
    _putDontNest(result, 1278, 1286);
    _putDontNest(result, 1120, 1424);
    _putDontNest(result, 1196, 1364);
    _putDontNest(result, 1236, 1324);
    _putDontNest(result, 1248, 1296);
    _putDontNest(result, 1188, 1226);
    _putDontNest(result, 1186, 1236);
    _putDontNest(result, 1216, 1206);
    _putDontNest(result, 692, 1246);
    _putDontNest(result, 1246, 1334);
    _putDontNest(result, 5366, 5390);
    _putDontNest(result, 1176, 1384);
    _putDontNest(result, 1206, 1374);
    _putDontNest(result, 1208, 1246);
    _putDontNest(result, 1058, 1092);
    _putDontNest(result, 1178, 1276);
    _putDontNest(result, 1406, 1424);
    _putDontNest(result, 1278, 1306);
    _putDontNest(result, 1068, 1334);
    _putDontNest(result, 1120, 1394);
    _putDontNest(result, 1078, 1324);
    _putDontNest(result, 1126, 1404);
    _putDontNest(result, 1384, 1384);
    _putDontNest(result, 1354, 1354);
    _putDontNest(result, 1326, 1334);
    _putDontNest(result, 1118, 1216);
    _putDontNest(result, 1126, 1256);
    _putDontNest(result, 1132, 1266);
    _putDontNest(result, 692, 1086);
    _putDontNest(result, 1118, 1364);
    _putDontNest(result, 1142, 1404);
    _putDontNest(result, 1036, 1286);
    _putDontNest(result, 1396, 1404);
    _putDontNest(result, 1394, 1394);
    _putDontNest(result, 1296, 1296);
    _putDontNest(result, 1344, 1344);
    _putDontNest(result, 444, 424);
    _putDontNest(result, 1424, 1424);
    _putDontNest(result, 1068, 1186);
    _putDontNest(result, 1142, 1256);
    _putDontNest(result, 1246, 1306);
    _putDontNest(result, 1036, 1334);
    _putDontNest(result, 1088, 1394);
    _putDontNest(result, 1186, 1424);
    _putDontNest(result, 1058, 1296);
    _putDontNest(result, 1098, 1384);
    _putDontNest(result, 1346, 1394);
    _putDontNest(result, 1098, 1276);
    _putDontNest(result, 4730, 4982);
    _putDontNest(result, 1104, 1394);
    _putDontNest(result, 1068, 1286);
    _putDontNest(result, 1196, 1414);
    _putDontNest(result, 1364, 1404);
    _putDontNest(result, 330, 414);
    _putDontNest(result, 1036, 1186);
    _putDontNest(result, 4714, 4982);
    _putDontNest(result, 1218, 1424);
    _putDontNest(result, 1142, 1324);
    _putDontNest(result, 1132, 1334);
    _putDontNest(result, 1286, 1374);
    _putDontNest(result, 1296, 1344);
    _putDontNest(result, 1068, 1266);
    _putDontNest(result, 1120, 1206);
    _putDontNest(result, 1198, 1306);
    _putDontNest(result, 1228, 1414);
    _putDontNest(result, 1078, 1404);
    _putDontNest(result, 1126, 1324);
    _putDontNest(result, 1308, 1364);
    _putDontNest(result, 1306, 1354);
    _putDontNest(result, 1336, 1384);
    _putDontNest(result, 1132, 1186);
    _putDontNest(result, 1078, 1256);
    _putDontNest(result, 692, 1012);
    _putDontNest(result, 250, 444);
    _putDontNest(result, 1276, 1414);
    _putDontNest(result, 1288, 1384);
    _putDontNest(result, 434, 454);
    _putDontNest(result, 1324, 1364);
    _putDontNest(result, 1058, 1236);
    _putDontNest(result, 1036, 1266);
    _putDontNest(result, 1088, 1206);
    _putDontNest(result, 692, 1118);
    _putDontNest(result, 1266, 1424);
    _putDontNest(result, 1132, 1286);
    _putDontNest(result, 1058, 1344);
    _putDontNest(result, 1334, 1374);
    _putDontNest(result, 1298, 1394);
    _putDontNest(result, 1104, 1206);
    _putDontNest(result, 1098, 1196);
    _putDontNest(result, 992, 1404);
    _putDontNest(result, 1248, 1394);
    _putDontNest(result, 1258, 1384);
    _putDontNest(result, 1196, 1334);
    _putDontNest(result, 1206, 1324);
    _putDontNest(result, 1256, 1256);
    _putDontNest(result, 1226, 1226);
    _putDontNest(result, 1088, 1104);
    _putDontNest(result, 4714, 4704);
    _putDontNest(result, 1014, 1394);
    _putDontNest(result, 1036, 1414);
    _putDontNest(result, 1218, 1344);
    _putDontNest(result, 1246, 1364);
    _putDontNest(result, 1236, 1374);
    _putDontNest(result, 1186, 1186);
    _putDontNest(result, 1216, 1216);
    _putDontNest(result, 1268, 1276);
    _putDontNest(result, 1266, 1266);
    _putDontNest(result, 1296, 1424);
    _putDontNest(result, 4810, 4982);
    _putDontNest(result, 4730, 4704);
    _putDontNest(result, 992, 1246);
    _putDontNest(result, 1014, 1216);
    _putDontNest(result, 1118, 1306);
    _putDontNest(result, 1186, 1296);
    _putDontNest(result, 1058, 1424);
    _putDontNest(result, 1216, 1394);
    _putDontNest(result, 1226, 1384);
    _putDontNest(result, 1256, 1354);
    _putDontNest(result, 1266, 1344);
    _putDontNest(result, 1098, 1130);
    _putDontNest(result, 1218, 1266);
    _putDontNest(result, 312, 444);
    _putDontNest(result, 4724, 4714);
    _putDontNest(result, 1268, 1374);
    _putDontNest(result, 1278, 1364);
    _putDontNest(result, 1068, 1414);
    _putDontNest(result, 1238, 1404);
    _putDontNest(result, 1196, 1286);
    _putDontNest(result, 1236, 1276);
    _putDontNest(result, 1326, 1414);
    _putDontNest(result, 4724, 4730);
    _putDontNest(result, 5728, 5738);
    _putDontNest(result, 1218, 1296);
    _putDontNest(result, 1186, 1266);
    _putDontNest(result, 1068, 1140);
    _putDontNest(result, 1168, 1216);
    _putDontNest(result, 1188, 1276);
    _putDontNest(result, 1374, 1414);
    _putDontNest(result, 1344, 1424);
    _putDontNest(result, 4760, 4976);
    _putDontNest(result, 692, 1394);
    _putDontNest(result, 992, 1324);
    _putDontNest(result, 5366, 5420);
    _putDontNest(result, 1276, 1334);
    _putDontNest(result, 1176, 1354);
    _putDontNest(result, 1206, 1404);
    _putDontNest(result, 1228, 1286);
    _putDontNest(result, 1068, 1124);
    _putDontNest(result, 1208, 1256);
    _putDontNest(result, 1178, 1226);
    _putDontNest(result, 1036, 1092);
    _putDontNest(result, 5390, 5400);
    _putDontNest(result, 692, 1216);
    _putDontNest(result, 1276, 1286);
    _putDontNest(result, 1238, 1324);
    _putDontNest(result, 1188, 1374);
    _putDontNest(result, 1198, 1364);
    _putDontNest(result, 1228, 1334);
    _putDontNest(result, 1196, 1236);
    _putDontNest(result, 1036, 1140);
    _putDontNest(result, 992, 1166);
    _putDontNest(result, 1266, 1296);
    _putDontNest(result, 1208, 1354);
    _putDontNest(result, 1132, 1414);
    _putDontNest(result, 1178, 1384);
    _putDontNest(result, 1186, 1344);
    _putDontNest(result, 1168, 1394);
    _putDontNest(result, 1068, 1092);
    _putDontNest(result, 1078, 1118);
    _putDontNest(result, 1206, 1246);
    _putDontNest(result, 1036, 1124);
    _putDontNest(result, 1176, 1256);
    _putDontNest(result, 5876, 5916);
    _putDontNest(result, 1276, 1306);
    _putDontNest(result, 692, 1056);
    _putDontNest(result, 1120, 1404);
    _putDontNest(result, 1132, 1384);
    _putDontNest(result, 1126, 1394);
    _putDontNest(result, 1178, 1414);
    _putDontNest(result, 1324, 1334);
    _putDontNest(result, 1104, 1216);
    _putDontNest(result, 1098, 1226);
    _putDontNest(result, 1118, 1354);
    _putDontNest(result, 1142, 1394);
    _putDontNest(result, 1374, 1364);
    _putDontNest(result, 436, 434);
    _putDontNest(result, 1364, 1374);
    _putDontNest(result, 1298, 1296);
    _putDontNest(result, 1088, 1216);
    _putDontNest(result, 1058, 1186);
    _putDontNest(result, 992, 1118);
    _putDontNest(result, 1188, 1424);
    _putDontNest(result, 1088, 1404);
    _putDontNest(result, 398, 424);
    _putDontNest(result, 5382, 5420);
    _putDontNest(result, 1344, 1394);
    _putDontNest(result, 1354, 1384);
    _putDontNest(result, 1078, 1166);
    _putDontNest(result, 1126, 1246);
    _putDontNest(result, 1132, 1236);
    _putDontNest(result, 1014, 1104);
    _putDontNest(result, 1228, 1306);
    _putDontNest(result, 1104, 1404);
    _putDontNest(result, 5392, 5410);
    _putDontNest(result, 1286, 1324);
    _putDontNest(result, 426, 444);
    _putDontNest(result, 414, 424);
    _putDontNest(result, 1308, 1334);
    _putDontNest(result, 1142, 1246);
    _putDontNest(result, 1120, 1216);
    _putDontNest(result, 1068, 1384);
    _putDontNest(result, 1288, 1354);
    _putDontNest(result, 312, 414);
    _putDontNest(result, 400, 454);
    _putDontNest(result, 1298, 1344);
    _putDontNest(result, 1058, 1266);
    _putDontNest(result, 1036, 1236);
    _putDontNest(result, 1196, 1306);
    _putDontNest(result, 1078, 1394);
    _putDontNest(result, 1226, 1414);
    _putDontNest(result, 1236, 1424);
    _putDontNest(result, 1120, 1324);
    _putDontNest(result, 1098, 1286);
    _putDontNest(result, 1334, 1404);
    _putDontNest(result, 1098, 1334);
    _putDontNest(result, 1058, 1374);
    _putDontNest(result, 1036, 1384);
    _putDontNest(result, 1104, 1324);
    _putDontNest(result, 1286, 1276);
    _putDontNest(result, 1286, 1404);
    _putDontNest(result, 1326, 1364);
    _putDontNest(result, 1176, 1176);
    _putDontNest(result, 1068, 1236);
    _putDontNest(result, 1014, 1424);
    _putDontNest(result, 692, 1104);
    _putDontNest(result, 1258, 1414);
    _putDontNest(result, 1088, 1324);
    _putDontNest(result, 1268, 1424);
    _putDontNest(result, 416, 454);
    _putDontNest(result, 1306, 1384);
    _putDontNest(result, 1336, 1354);
    _putDontNest(result, 1296, 1394);
    _putDontNest(result, 1118, 1206);
    _putDontNest(result, 1078, 1246);
    _putDontNest(result, 1126, 1166);
    _putDontNest(result, 692, 1186);
    _putDontNest(result, 1178, 1286);
    _putDontNest(result, 1218, 1374);
    _putDontNest(result, 1236, 1344);
    _putDontNest(result, 1248, 1404);
    _putDontNest(result, 1248, 1266);
    _putDontNest(result, 1196, 1206);
    _putDontNest(result, 1258, 1256);
    _putDontNest(result, 1308, 1414);
    _putDontNest(result, 992, 1256);
    _putDontNest(result, 1132, 1306);
    _putDontNest(result, 1276, 1384);
    _putDontNest(result, 1246, 1354);
    _putDontNest(result, 1246, 1236);
    _putDontNest(result, 1236, 1246);
    _putDontNest(result, 318, 424);
    _putDontNest(result, 1298, 1424);
    _putDontNest(result, 1014, 1344);
    _putDontNest(result, 992, 1374);
    _putDontNest(result, 1256, 1364);
    _putDontNest(result, 1216, 1404);
    _putDontNest(result, 1228, 1384);
    _putDontNest(result, 1168, 1324);
    _putDontNest(result, 1268, 1344);
    _putDontNest(result, 1188, 1296);
    _putDontNest(result, 1226, 1256);
    _putDontNest(result, 1216, 1266);
    _putDontNest(result, 1118, 1124);
    _putDontNest(result, 1168, 1186);
    _putDontNest(result, 4724, 4724);
    _putDontNest(result, 1178, 1334);
    _putDontNest(result, 1238, 1394);
    _putDontNest(result, 1278, 1354);
    _putDontNest(result, 1266, 1374);
    _putDontNest(result, 1098, 1176);
    _putDontNest(result, 1118, 1140);
    _putDontNest(result, 1238, 1276);
    _putDontNest(result, 1324, 1414);
    _putDontNest(result, 1196, 1384);
    _putDontNest(result, 1258, 1334);
    _putDontNest(result, 1346, 1424);
    _putDontNest(result, 692, 1266);
    _putDontNest(result, 1068, 1306);
    _putDontNest(result, 1226, 1286);
    _putDontNest(result, 1206, 1394);
    _putDontNest(result, 1236, 1296);
    _putDontNest(result, 1248, 1324);
    _putDontNest(result, 5366, 5410);
    _putDontNest(result, 1098, 1414);
    _putDontNest(result, 1176, 1364);
    _putDontNest(result, 1176, 1226);
    _putDontNest(result, 1206, 1276);
    _putDontNest(result, 424, 414);
    _putDontNest(result, 256, 454);
    _putDontNest(result, 330, 444);
    _putDontNest(result, 1014, 1206);
    _putDontNest(result, 1198, 1354);
    _putDontNest(result, 1186, 1374);
    _putDontNest(result, 1226, 1334);
    _putDontNest(result, 1058, 1104);
    _putDontNest(result, 1198, 1236);
    _putDontNest(result, 1188, 1246);
    _putDontNest(result, 1404, 1414);
    _putDontNest(result, 1298, 1306);
    _putDontNest(result, 1014, 1296);
    _putDontNest(result, 1036, 1306);
    _putDontNest(result, 1216, 1324);
    _putDontNest(result, 1268, 1296);
    _putDontNest(result, 1208, 1364);
    _putDontNest(result, 1168, 1404);
    _putDontNest(result, 1188, 1344);
    _putDontNest(result, 1258, 1286);
    _putDontNest(result, 1178, 1256);
    _putDontNest(result, 1186, 1216);
    _putDontNest(result, 1168, 1266);
    _putDontNest(result, 1208, 1226);
    _putDontNest(result, 1394, 1424);
    _putDontNest(result, 5876, 5946);
    _putDontNest(result, 692, 1364);
    _putDontNest(result, 1098, 1364);
    _putDontNest(result, 1088, 1374);
    _putDontNest(result, 1176, 1414);
    _putDontNest(result, 1306, 1286);
    _putDontNest(result, 424, 424);
    _putDontNest(result, 1346, 1374);
    _putDontNest(result, 1120, 1266);
    _putDontNest(result, 1078, 1196);
    _putDontNest(result, 1126, 1276);
    _putDontNest(result, 1068, 1206);
    _putDontNest(result, 5876, 5910);
    _putDontNest(result, 1266, 1306);
    _putDontNest(result, 1058, 1324);
    _putDontNest(result, 1104, 1374);
    _putDontNest(result, 454, 454);
    _putDontNest(result, 1142, 1276);
    _putDontNest(result, 1118, 1236);
    _putDontNest(result, 992, 1176);
    _putDontNest(result, 1142, 1344);
    _putDontNest(result, 1208, 1414);
    _putDontNest(result, 1120, 1374);
    _putDontNest(result, 1132, 1354);
    _putDontNest(result, 1296, 1324);
    _putDontNest(result, 5382, 5410);
    _putDontNest(result, 1344, 1404);
    _putDontNest(result, 1384, 1364);
    _putDontNest(result, 1098, 1256);
    _putDontNest(result, 1036, 1206);
    _putDontNest(result, 1088, 1266);
    _putDontNest(result, 692, 1414);
    _putDontNest(result, 1118, 1384);
    _putDontNest(result, 1206, 1424);
    _putDontNest(result, 1078, 1296);
    _putDontNest(result, 1126, 1344);
    _putDontNest(result, 1306, 1334);
    _putDontNest(result, 5392, 5420);
    _putDontNest(result, 1104, 1266);
    _putDontNest(result, 4974, 4976);
    _putDontNest(result, 1218, 1306);
    _putDontNest(result, 1058, 1404);
    _putDontNest(result, 1036, 1354);
    _putDontNest(result, 1288, 1364);
    _putDontNest(result, 318, 414);
    _putDontNest(result, 1324, 1384);
    _putDontNest(result, 406, 454);
    _putDontNest(result, 1132, 1206);
    _putDontNest(result, 1142, 1196);
    _putDontNest(result, 1238, 1424);
    _putDontNest(result, 250, 424);
    _putDontNest(result, 1298, 1374);
    _putDontNest(result, 1334, 1394);
    _putDontNest(result, 1126, 1196);
    _putDontNest(result, 1078, 1276);
    _putDontNest(result, 1120, 1186);
    _putDontNest(result, 1186, 1306);
    _putDontNest(result, 1068, 1354);
    _putDontNest(result, 1126, 1296);
    _putDontNest(result, 1078, 1344);
    _putDontNest(result, 1326, 1354);
    _putDontNest(result, 1286, 1394);
    _putDontNest(result, 1104, 1186);
    _putDontNest(result, 1014, 1414);
    _putDontNest(result, 1256, 1414);
    _putDontNest(result, 1142, 1296);
    _putDontNest(result, 1296, 1276);
    _putDontNest(result, 1308, 1384);
    _putDontNest(result, 1296, 1404);
    _putDontNest(result, 1336, 1364);
    _putDontNest(result, 1088, 1186);
    _putDontNest(result, 1058, 1216);
    _putDontNest(result, 1238, 1344);
    _putDontNest(result, 1226, 1364);
    _putDontNest(result, 1176, 1286);
    _putDontNest(result, 1216, 1374);
    _putDontNest(result, 1228, 1354);
    _putDontNest(result, 1218, 1246);
    _putDontNest(result, 1248, 1276);
    _putDontNest(result, 1306, 1414);
    _putDontNest(result, 992, 1384);
    _putDontNest(result, 1266, 1404);
    _putDontNest(result, 1268, 1394);
    _putDontNest(result, 1208, 1334);
    _putDontNest(result, 1278, 1384);
    _putDontNest(result, 1186, 1324);
    _putDontNest(result, 1132, 1176);
    _putDontNest(result, 312, 424);
    _putDontNest(result, 692, 1334);
    _putDontNest(result, 1208, 1286);
    _putDontNest(result, 1248, 1374);
    _putDontNest(result, 1258, 1364);
    _putDontNest(result, 1218, 1404);
    _putDontNest(result, 1228, 1256);
    _putDontNest(result, 1168, 1196);
    _putDontNest(result, 1216, 1276);
    _putDontNest(result, 4704, 4730);
    _putDontNest(result, 992, 1226);
    _putDontNest(result, 692, 1286);
    _putDontNest(result, 1236, 1394);
    _putDontNest(result, 1206, 1296);
    _putDontNest(result, 1246, 1384);
    _putDontNest(result, 1176, 1334);
    _putDontNest(result, 1078, 1424);
    _putDontNest(result, 1276, 1354);
    _putDontNest(result, 1178, 1206);
    _putDontNest(result, 1118, 1130);
    _putDontNest(result, 1238, 1266);
    _putDontNest(result, 4704, 4714);
    _putDontNest(result, 1014, 1236);
    _putDontNest(result, 4586, 4976);
    _putDontNest(result, 1188, 1394);
    _putDontNest(result, 1186, 1404);
    _putDontNest(result, 1198, 1384);
    _putDontNest(result, 1256, 1334);
    _putDontNest(result, 1266, 1324);
    _putDontNest(result, 1196, 1256);
    _putDontNest(result, 1178, 1364);
    _putDontNest(result, 1238, 1296);
    _putDontNest(result, 1168, 1374);
    _putDontNest(result, 1068, 1176);
    _putDontNest(result, 1206, 1266);
    _putDontNest(result, 1176, 1236);
    _putDontNest(result, 1364, 1424);
    _putDontNest(result, 1354, 1414);
    _putDontNest(result, 1058, 1306);
    _putDontNest(result, 1014, 1334);
    _putDontNest(result, 1196, 1354);
    _putDontNest(result, 1126, 1424);
    _putDontNest(result, 1206, 1344);
    _putDontNest(result, 1186, 1246);
    _putDontNest(result, 1198, 1226);
    _putDontNest(result, 1058, 1118);
    _putDontNest(result, 414, 414);
    _putDontNest(result, 1296, 1306);
    _putDontNest(result, 1014, 1286);
    _putDontNest(result, 1256, 1286);
    _putDontNest(result, 1218, 1324);
    _putDontNest(result, 1142, 1424);
    _putDontNest(result, 1168, 1276);
    _putDontNest(result, 1036, 1176);
    _putDontNest(result, 1208, 1236);
    _putDontNest(result, 1188, 1216);
    _putDontNest(result, 398, 414);
    _putDontNest(result, 1386, 1414);
    _putDontNest(result, 1396, 1424);
    _putDontNest(result, 692, 1236);
      
    return result;
  }
    
  protected static IntegerMap _initDontNestGroups() {
    IntegerMap result = org.rascalmpl.library.lang.rascal.syntax.RascalRascal._initDontNestGroups();
    int resultStoreId = result.size();
    
    
    ++resultStoreId;
    
    result.putUnsafe(5390, resultStoreId);
    result.putUnsafe(5366, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(1374, resultStoreId);
    result.putUnsafe(1384, resultStoreId);
    result.putUnsafe(1364, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(5728, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(1324, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(5738, resultStoreId);
    result.putUnsafe(5720, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(5876, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(1228, resultStoreId);
    result.putUnsafe(1258, resultStoreId);
    result.putUnsafe(1256, resultStoreId);
    result.putUnsafe(1238, resultStoreId);
    result.putUnsafe(1266, resultStoreId);
    result.putUnsafe(1248, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(4730, resultStoreId);
    result.putUnsafe(4714, resultStoreId);
    result.putUnsafe(4724, resultStoreId);
    result.putUnsafe(4704, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(1414, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(4974, resultStoreId);
    result.putUnsafe(4586, resultStoreId);
    result.putUnsafe(4810, resultStoreId);
    result.putUnsafe(4760, resultStoreId);
    result.putUnsafe(4628, resultStoreId);
    result.putUnsafe(4658, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(1404, resultStoreId);
    result.putUnsafe(1386, resultStoreId);
    result.putUnsafe(1396, resultStoreId);
    result.putUnsafe(1394, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(414, resultStoreId);
    result.putUnsafe(444, resultStoreId);
    result.putUnsafe(424, resultStoreId);
    result.putUnsafe(434, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(1068, resultStoreId);
    result.putUnsafe(1036, resultStoreId);
    result.putUnsafe(1078, resultStoreId);
    result.putUnsafe(1014, resultStoreId);
    result.putUnsafe(1058, resultStoreId);
    result.putUnsafe(992, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(1088, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(5400, resultStoreId);
    result.putUnsafe(5382, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(1246, resultStoreId);
    result.putUnsafe(1236, resultStoreId);
    result.putUnsafe(1218, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(1196, resultStoreId);
    result.putUnsafe(1186, resultStoreId);
    result.putUnsafe(1168, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(5392, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(426, resultStoreId);
    result.putUnsafe(436, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(5420, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(1118, resultStoreId);
    result.putUnsafe(1098, resultStoreId);
    result.putUnsafe(1104, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(454, resultStoreId);
    result.putUnsafe(406, resultStoreId);
    result.putUnsafe(416, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(398, resultStoreId);
    result.putUnsafe(318, resultStoreId);
    result.putUnsafe(330, resultStoreId);
    result.putUnsafe(250, resultStoreId);
    result.putUnsafe(312, resultStoreId);
    result.putUnsafe(256, resultStoreId);
    result.putUnsafe(400, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(1198, resultStoreId);
    result.putUnsafe(1226, resultStoreId);
    result.putUnsafe(1208, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(692, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(1354, resultStoreId);
    result.putUnsafe(1346, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(1126, resultStoreId);
    result.putUnsafe(1120, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(1278, resultStoreId);
    result.putUnsafe(1276, resultStoreId);
    result.putUnsafe(1306, resultStoreId);
    result.putUnsafe(1288, resultStoreId);
    result.putUnsafe(1286, resultStoreId);
    result.putUnsafe(1268, resultStoreId);
    result.putUnsafe(1298, resultStoreId);
    result.putUnsafe(1296, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(1132, resultStoreId);
    result.putUnsafe(1176, resultStoreId);
    result.putUnsafe(1142, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(1406, resultStoreId);
    result.putUnsafe(1424, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(1326, resultStoreId);
    result.putUnsafe(1308, resultStoreId);
    result.putUnsafe(1336, resultStoreId);
    result.putUnsafe(1334, resultStoreId);
    result.putUnsafe(1344, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(1178, resultStoreId);
    result.putUnsafe(1206, resultStoreId);
    result.putUnsafe(1188, resultStoreId);
    result.putUnsafe(1216, resultStoreId);
      
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
  private static final IConstructor prod__RascalKeywords__lit_constructor_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"constructor\")],{})", Factory.Production);
  private static final IConstructor prod__CharacterClass_Sym__charClass_Class_ = (IConstructor) _read("prod(label(\"CharacterClass\",sort(\"Sym\")),[label(\"charClass\",sort(\"Class\"))],{})", Factory.Production);
  private static final IConstructor prod__ListModules_ShellCommand__lit_modules_ = (IConstructor) _read("prod(label(\"ListModules\",sort(\"ShellCommand\")),[lit(\"modules\")],{})", Factory.Production);
  private static final IConstructor prod__Char__UnicodeEscape__tag__category___67_111_110_115_116_97_110_116 = (IConstructor) _read("prod(lex(\"Char\"),[lex(\"UnicodeEscape\")],{tag(category(\"Constant\"))})", Factory.Production);
  private static final IConstructor prod__IfDefinedOrDefault_Assignable__receiver_Assignable_layouts_LAYOUTLIST_lit___63_layouts_LAYOUTLIST_defaultExpression_Expression_ = (IConstructor) _read("prod(label(\"IfDefinedOrDefault\",sort(\"Assignable\")),[label(\"receiver\",sort(\"Assignable\")),layouts(\"LAYOUTLIST\"),lit(\"?\"),layouts(\"LAYOUTLIST\"),label(\"defaultExpression\",sort(\"Expression\"))],{})", Factory.Production);
  private static final IConstructor prod__RascalKeywords__lit_fail_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"fail\")],{})", Factory.Production);
  private static final IConstructor prod__start__Commands__layouts_LAYOUTLIST_top_Commands_layouts_LAYOUTLIST_ = (IConstructor) _read("prod(start(sort(\"Commands\")),[layouts(\"LAYOUTLIST\"),label(\"top\",sort(\"Commands\")),layouts(\"LAYOUTLIST\")],{})", Factory.Production);
  private static final IConstructor prod__NamedRegExp__char_class___range__92_92_char_class___range__47_47_range__60_60_range__62_62_range__92_92_ = (IConstructor) _read("prod(lex(\"NamedRegExp\"),[\\char-class([range(92,92)]),\\char-class([range(47,47),range(60,60),range(62,62),range(92,92)])],{})", Factory.Production);
  private static final IConstructor prod__RationalLiteral__char_class___range__49_57_iter_star__char_class___range__48_57_char_class___range__114_114_char_class___range__49_57_conditional__iter_star__char_class___range__48_57__not_follow__char_class___range__48_57_range__65_90_range__95_95_range__97_122_ = (IConstructor) _read("prod(lex(\"RationalLiteral\"),[\\char-class([range(49,57)]),\\iter-star(\\char-class([range(48,57)])),\\char-class([range(114,114)]),\\char-class([range(49,57)]),conditional(\\iter-star(\\char-class([range(48,57)])),{\\not-follow(\\char-class([range(48,57),range(65,90),range(95,95),range(97,122)]))})],{})", Factory.Production);
  private static final IConstructor prod__TimeZonePart__lit_Z_ = (IConstructor) _read("prod(lex(\"TimeZonePart\"),[lit(\"Z\")],{})", Factory.Production);
  private static final IConstructor prod__RascalKeywords__lit_default_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"default\")],{})", Factory.Production);
  private static final IConstructor prod__RegExpLiteral__lit___47_iter_star__RegExp_lit___47_RegExpModifier_ = (IConstructor) _read("prod(lex(\"RegExpLiteral\"),[lit(\"/\"),\\iter-star(lex(\"RegExp\")),lit(\"/\"),lex(\"RegExpModifier\")],{})", Factory.Production);
  private static final IConstructor prod__HeaderKeyword__lit_layout_ = (IConstructor) _read("prod(keywords(\"HeaderKeyword\"),[lit(\"layout\")],{})", Factory.Production);
  private static final IConstructor prod__lit_fail__char_class___range__102_102_char_class___range__97_97_char_class___range__105_105_char_class___range__108_108_ = (IConstructor) _read("prod(lit(\"fail\"),[\\char-class([range(102,102)]),\\char-class([range(97,97)]),\\char-class([range(105,105)]),\\char-class([range(108,108)])],{})", Factory.Production);
  private static final IConstructor prod__RationalLiteral__char_class___range__48_48_char_class___range__114_114_ = (IConstructor) _read("prod(lex(\"RationalLiteral\"),[\\char-class([range(48,48)]),\\char-class([range(114,114)])],{})", Factory.Production);
  private static final IConstructor prod__List_Comprehension__lit___91_layouts_LAYOUTLIST_results_iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___124_layouts_LAYOUTLIST_generators_iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___93_ = (IConstructor) _read("prod(label(\"List\",sort(\"Comprehension\")),[lit(\"[\"),layouts(\"LAYOUTLIST\"),label(\"results\",\\iter-seps(sort(\"Expression\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\"|\"),layouts(\"LAYOUTLIST\"),label(\"generators\",\\iter-seps(sort(\"Expression\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\"]\")],{})", Factory.Production);
  private static final IConstructor regular__iter__char_class___range__117_117 = (IConstructor) _read("regular(iter(\\char-class([range(117,117)])))", Factory.Production);
  private static final IConstructor prod__lit_repeat__char_class___range__114_114_char_class___range__101_101_char_class___range__112_112_char_class___range__101_101_char_class___range__97_97_char_class___range__116_116_ = (IConstructor) _read("prod(lit(\"repeat\"),[\\char-class([range(114,114)]),\\char-class([range(101,101)]),\\char-class([range(112,112)]),\\char-class([range(101,101)]),\\char-class([range(97,97)]),\\char-class([range(116,116)])],{})", Factory.Production);
  private static final IConstructor prod__Name__conditional__seq___conditional__char_class___range__65_90_range__95_95_range__97_122__not_precede__char_class___range__65_90_range__95_95_range__97_122_conditional__iter_star__char_class___range__48_57_range__65_90_range__95_95_range__97_122__not_follow__char_class___range__48_57_range__65_90_range__95_95_range__97_122__delete__RascalKeywords_ = (IConstructor) _read("prod(lex(\"Name\"),[conditional(seq([conditional(\\char-class([range(65,90),range(95,95),range(97,122)]),{\\not-precede(\\char-class([range(65,90),range(95,95),range(97,122)]))}),conditional(\\iter-star(\\char-class([range(48,57),range(65,90),range(95,95),range(97,122)])),{\\not-follow(\\char-class([range(48,57),range(65,90),range(95,95),range(97,122)]))})]),{delete(keywords(\"RascalKeywords\"))})],{})", Factory.Production);
  private static final IConstructor prod__lit_parameter__char_class___range__112_112_char_class___range__97_97_char_class___range__114_114_char_class___range__97_97_char_class___range__109_109_char_class___range__101_101_char_class___range__116_116_char_class___range__101_101_char_class___range__114_114_ = (IConstructor) _read("prod(lit(\"parameter\"),[\\char-class([range(112,112)]),\\char-class([range(97,97)]),\\char-class([range(114,114)]),\\char-class([range(97,97)]),\\char-class([range(109,109)]),\\char-class([range(101,101)]),\\char-class([range(116,116)]),\\char-class([range(101,101)]),\\char-class([range(114,114)])],{})", Factory.Production);
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
  private static final IConstructor prod__While_Statement__label_Label_layouts_LAYOUTLIST_lit_while_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_conditions_iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_layouts_LAYOUTLIST_body_Statement_ = (IConstructor) _read("prod(label(\"While\",sort(\"Statement\")),[label(\"label\",sort(\"Label\")),layouts(\"LAYOUTLIST\"),lit(\"while\"),layouts(\"LAYOUTLIST\"),lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"conditions\",\\iter-seps(sort(\"Expression\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\")\"),layouts(\"LAYOUTLIST\"),label(\"body\",sort(\"Statement\"))],{})", Factory.Production);
  private static final IConstructor prod__OctalIntegerLiteral_IntegerLiteral__octal_OctalIntegerLiteral_ = (IConstructor) _read("prod(label(\"OctalIntegerLiteral\",sort(\"IntegerLiteral\")),[label(\"octal\",lex(\"OctalIntegerLiteral\"))],{})", Factory.Production);
  private static final IConstructor prod__lit_when__char_class___range__119_119_char_class___range__104_104_char_class___range__101_101_char_class___range__110_110_ = (IConstructor) _read("prod(lit(\"when\"),[\\char-class([range(119,119)]),\\char-class([range(104,104)]),\\char-class([range(101,101)]),\\char-class([range(110,110)])],{})", Factory.Production);
  private static final IConstructor prod__lit_type__char_class___range__116_116_char_class___range__121_121_char_class___range__112_112_char_class___range__101_101_ = (IConstructor) _read("prod(lit(\"type\"),[\\char-class([range(116,116)]),\\char-class([range(121,121)]),\\char-class([range(112,112)]),\\char-class([range(101,101)])],{})", Factory.Production);
  private static final IConstructor prod__RascalKeywords__lit_void_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"void\")],{})", Factory.Production);
  private static final IConstructor prod__NamedRegExp__char_class___range__0_46_range__48_59_range__61_61_range__63_91_range__93_65535_ = (IConstructor) _read("prod(lex(\"NamedRegExp\"),[\\char-class([range(0,46),range(48,59),range(61,61),range(63,91),range(93,65535)])],{})", Factory.Production);
  private static final IConstructor prod__LessThan_Expression__lhs_Expression_layouts_LAYOUTLIST_conditional__lit___60__not_follow__lit___layouts_LAYOUTLIST_rhs_Expression__assoc__non_assoc = (IConstructor) _read("prod(label(\"LessThan\",sort(\"Expression\")),[label(\"lhs\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),conditional(lit(\"\\<\"),{\\not-follow(lit(\"-\"))}),layouts(\"LAYOUTLIST\"),label(\"rhs\",sort(\"Expression\"))],{assoc(\\non-assoc())})", Factory.Production);
  private static final IConstructor prod__NonEmptyBlock_Expression__lit___123_layouts_LAYOUTLIST_statements_iter_seps__Statement__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_ = (IConstructor) _read("prod(label(\"NonEmptyBlock\",sort(\"Expression\")),[lit(\"{\"),layouts(\"LAYOUTLIST\"),label(\"statements\",\\iter-seps(sort(\"Statement\"),[layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\"}\")],{})", Factory.Production);
  private static final IConstructor prod__Tuple_Assignable__lit___60_layouts_LAYOUTLIST_elements_iter_seps__Assignable__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___62_ = (IConstructor) _read("prod(label(\"Tuple\",sort(\"Assignable\")),[lit(\"\\<\"),layouts(\"LAYOUTLIST\"),label(\"elements\",\\iter-seps(sort(\"Assignable\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\"\\>\")],{})", Factory.Production);
  private static final IConstructor prod__lit_modules__char_class___range__109_109_char_class___range__111_111_char_class___range__100_100_char_class___range__117_117_char_class___range__108_108_char_class___range__101_101_char_class___range__115_115_ = (IConstructor) _read("prod(lit(\"modules\"),[\\char-class([range(109,109)]),\\char-class([range(111,111)]),\\char-class([range(100,100)]),\\char-class([range(117,117)]),\\char-class([range(108,108)]),\\char-class([range(101,101)]),\\char-class([range(115,115)])],{})", Factory.Production);
  private static final IConstructor prod__PreStringChars__char_class___range__34_34_iter_star__StringCharacter_char_class___range__60_60__tag__category___67_111_110_115_116_97_110_116 = (IConstructor) _read("prod(lex(\"PreStringChars\"),[\\char-class([range(34,34)]),\\iter-star(lex(\"StringCharacter\")),\\char-class([range(60,60)])],{tag(category(\"Constant\"))})", Factory.Production);
  private static final IConstructor prod__List_Commands__commands_iter_seps__Command__layouts_LAYOUTLIST_ = (IConstructor) _read("prod(label(\"List\",sort(\"Commands\")),[label(\"commands\",\\iter-seps(sort(\"Command\"),[layouts(\"LAYOUTLIST\")]))],{})", Factory.Production);
  private static final IConstructor prod__Bracket_ProdModifier__lit_bracket_ = (IConstructor) _read("prod(label(\"Bracket\",sort(\"ProdModifier\")),[lit(\"bracket\")],{})", Factory.Production);
  private static final IConstructor regular__iter_seps__Command__layouts_LAYOUTLIST = (IConstructor) _read("regular(\\iter-seps(sort(\"Command\"),[layouts(\"LAYOUTLIST\")]))", Factory.Production);
  private static final IConstructor prod__Default_Catch__lit_catch_layouts_LAYOUTLIST_lit___58_layouts_LAYOUTLIST_body_Statement__tag__Foldable = (IConstructor) _read("prod(label(\"Default\",sort(\"Catch\")),[lit(\"catch\"),layouts(\"LAYOUTLIST\"),lit(\":\"),layouts(\"LAYOUTLIST\"),label(\"body\",sort(\"Statement\"))],{tag(Foldable())})", Factory.Production);
  private static final IConstructor prod__lit_catch__char_class___range__99_99_char_class___range__97_97_char_class___range__116_116_char_class___range__99_99_char_class___range__104_104_ = (IConstructor) _read("prod(lit(\"catch\"),[\\char-class([range(99,99)]),\\char-class([range(97,97)]),\\char-class([range(116,116)]),\\char-class([range(99,99)]),\\char-class([range(104,104)])],{})", Factory.Production);
  private static final IConstructor prod__Post_StringTail__post_PostStringChars_ = (IConstructor) _read("prod(label(\"Post\",sort(\"StringTail\")),[label(\"post\",lex(\"PostStringChars\"))],{})", Factory.Production);
  private static final IConstructor prod__lit_case__char_class___range__99_99_char_class___range__97_97_char_class___range__115_115_char_class___range__101_101_ = (IConstructor) _read("prod(lit(\"case\"),[\\char-class([range(99,99)]),\\char-class([range(97,97)]),\\char-class([range(115,115)]),\\char-class([range(101,101)])],{})", Factory.Production);
  private static final IConstructor prod__Default_PreModule__header_Header_layouts_LAYOUTLIST_conditional__empty__not_follow__HeaderKeyword_layouts_LAYOUTLIST_rest_iter_star_seps__Word__layouts_LAYOUTLIST_ = (IConstructor) _read("prod(label(\"Default\",sort(\"PreModule\")),[label(\"header\",sort(\"Header\")),layouts(\"LAYOUTLIST\"),conditional(empty(),{\\not-follow(keywords(\"HeaderKeyword\"))}),layouts(\"LAYOUTLIST\"),label(\"rest\",\\iter-star-seps(lex(\"Word\"),[layouts(\"LAYOUTLIST\")]))],{})", Factory.Production);
  private static final IConstructor prod__lit___47_61__char_class___range__47_47_char_class___range__61_61_ = (IConstructor) _read("prod(lit(\"/=\"),[\\char-class([range(47,47)]),\\char-class([range(61,61)])],{})", Factory.Production);
  private static final IConstructor regular__iter_seps__Case__layouts_LAYOUTLIST = (IConstructor) _read("regular(\\iter-seps(sort(\"Case\"),[layouts(\"LAYOUTLIST\")]))", Factory.Production);
  private static final IConstructor regular__seq___char_class___range__44_44_range__46_46_char_class___range__48_57_opt__seq___char_class___range__48_57_opt__char_class___range__48_57 = (IConstructor) _read("regular(seq([\\char-class([range(44,44),range(46,46)]),\\char-class([range(48,57)]),opt(seq([\\char-class([range(48,57)]),opt(\\char-class([range(48,57)]))]))]))", Factory.Production);
  private static final IConstructor prod__DataAbstract_Declaration__tags_Tags_layouts_LAYOUTLIST_visibility_Visibility_layouts_LAYOUTLIST_lit_data_layouts_LAYOUTLIST_user_UserType_layouts_LAYOUTLIST_lit___59_ = (IConstructor) _read("prod(label(\"DataAbstract\",sort(\"Declaration\")),[label(\"tags\",sort(\"Tags\")),layouts(\"LAYOUTLIST\"),label(\"visibility\",sort(\"Visibility\")),layouts(\"LAYOUTLIST\"),lit(\"data\"),layouts(\"LAYOUTLIST\"),label(\"user\",sort(\"UserType\")),layouts(\"LAYOUTLIST\"),lit(\";\")],{})", Factory.Production);
  private static final IConstructor prod__RegExp__lit___60_Name_lit___58_iter_star__NamedRegExp_lit___62_ = (IConstructor) _read("prod(lex(\"RegExp\"),[lit(\"\\<\"),lex(\"Name\"),lit(\":\"),\\iter-star(lex(\"NamedRegExp\")),lit(\"\\>\")],{})", Factory.Production);
  private static final IConstructor prod__SetOption_ShellCommand__lit_set_layouts_LAYOUTLIST_name_QualifiedName_layouts_LAYOUTLIST_expression_Expression_ = (IConstructor) _read("prod(label(\"SetOption\",sort(\"ShellCommand\")),[lit(\"set\"),layouts(\"LAYOUTLIST\"),label(\"name\",sort(\"QualifiedName\")),layouts(\"LAYOUTLIST\"),label(\"expression\",sort(\"Expression\"))],{})", Factory.Production);
  private static final IConstructor prod__LAYOUT__Comment_ = (IConstructor) _read("prod(lex(\"LAYOUT\"),[lex(\"Comment\")],{})", Factory.Production);
  private static final IConstructor regular__iter_seps__TypeVar__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST = (IConstructor) _read("regular(\\iter-seps(sort(\"TypeVar\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")]))", Factory.Production);
  private static final IConstructor prod__IfDefined_Assignment__lit___63_61_ = (IConstructor) _read("prod(label(\"IfDefined\",sort(\"Assignment\")),[lit(\"?=\")],{})", Factory.Production);
  private static final IConstructor prod__HeaderKeyword__lit_import_ = (IConstructor) _read("prod(keywords(\"HeaderKeyword\"),[lit(\"import\")],{})", Factory.Production);
  private static final IConstructor prod__Java_FunctionModifier__lit_java_ = (IConstructor) _read("prod(label(\"Java\",sort(\"FunctionModifier\")),[lit(\"java\")],{})", Factory.Production);
  private static final IConstructor prod__Default_Case__lit_default_layouts_LAYOUTLIST_lit___58_layouts_LAYOUTLIST_statement_Statement__tag__Foldable = (IConstructor) _read("prod(label(\"Default\",sort(\"Case\")),[lit(\"default\"),layouts(\"LAYOUTLIST\"),lit(\":\"),layouts(\"LAYOUTLIST\"),label(\"statement\",sort(\"Statement\"))],{tag(Foldable())})", Factory.Production);
  private static final IConstructor prod__lit___47_47__char_class___range__47_47_char_class___range__47_47_ = (IConstructor) _read("prod(lit(\"//\"),[\\char-class([range(47,47)]),\\char-class([range(47,47)])],{})", Factory.Production);
  private static final IConstructor prod__lit_help__char_class___range__104_104_char_class___range__101_101_char_class___range__108_108_char_class___range__112_112_ = (IConstructor) _read("prod(lit(\"help\"),[\\char-class([range(104,104)]),\\char-class([range(101,101)]),\\char-class([range(108,108)]),\\char-class([range(112,112)])],{})", Factory.Production);
  private static final IConstructor prod__lit_num__char_class___range__110_110_char_class___range__117_117_char_class___range__109_109_ = (IConstructor) _read("prod(lit(\"num\"),[\\char-class([range(110,110)]),\\char-class([range(117,117)]),\\char-class([range(109,109)])],{})", Factory.Production);
  private static final IConstructor prod__ReifiedAdt_BasicType__lit_adt_ = (IConstructor) _read("prod(label(\"ReifiedAdt\",sort(\"BasicType\")),[lit(\"adt\")],{})", Factory.Production);
  private static final IConstructor prod__lit_tag__char_class___range__116_116_char_class___range__97_97_char_class___range__103_103_ = (IConstructor) _read("prod(lit(\"tag\"),[\\char-class([range(116,116)]),\\char-class([range(97,97)]),\\char-class([range(103,103)])],{})", Factory.Production);
  private static final IConstructor prod__Char__lit___92_char_class___range__32_32_range__34_34_range__39_39_range__45_45_range__60_60_range__62_62_range__91_93_range__98_98_range__102_102_range__110_110_range__114_114_range__116_116__tag__category___67_111_110_115_116_97_110_116 = (IConstructor) _read("prod(lex(\"Char\"),[lit(\"\\\\\"),\\char-class([range(32,32),range(34,34),range(39,39),range(45,45),range(60,60),range(62,62),range(91,93),range(98,98),range(102,102),range(110,110),range(114,114),range(116,116)])],{tag(category(\"Constant\"))})", Factory.Production);
  private static final IConstructor prod__NotFollow_Sym__symbol_Sym_layouts_LAYOUTLIST_lit___33_62_62_layouts_LAYOUTLIST_match_Sym__assoc__left = (IConstructor) _read("prod(label(\"NotFollow\",sort(\"Sym\")),[label(\"symbol\",sort(\"Sym\")),layouts(\"LAYOUTLIST\"),lit(\"!\\>\\>\"),layouts(\"LAYOUTLIST\"),label(\"match\",sort(\"Sym\"))],{assoc(left())})", Factory.Production);
  private static final IConstructor regular__iter__char_class___range__48_55 = (IConstructor) _read("regular(iter(\\char-class([range(48,55)])))", Factory.Production);
  private static final IConstructor regular__iter__char_class___range__48_57 = (IConstructor) _read("regular(iter(\\char-class([range(48,57)])))", Factory.Production);
  private static final IConstructor prod__Template_StringMiddle__mid_MidStringChars_layouts_LAYOUTLIST_template_StringTemplate_layouts_LAYOUTLIST_tail_StringMiddle_ = (IConstructor) _read("prod(label(\"Template\",sort(\"StringMiddle\")),[label(\"mid\",lex(\"MidStringChars\")),layouts(\"LAYOUTLIST\"),label(\"template\",sort(\"StringTemplate\")),layouts(\"LAYOUTLIST\"),label(\"tail\",sort(\"StringMiddle\"))],{})", Factory.Production);
  private static final IConstructor prod__RascalKeywords__lit_join_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"join\")],{})", Factory.Production);
  private static final IConstructor prod__Default_Assignment__lit___61_ = (IConstructor) _read("prod(label(\"Default\",sort(\"Assignment\")),[lit(\"=\")],{})", Factory.Production);
  private static final IConstructor prod__RegExp__char_class___range__0_46_range__48_59_range__61_61_range__63_91_range__93_65535_ = (IConstructor) _read("prod(lex(\"RegExp\"),[\\char-class([range(0,46),range(48,59),range(61,61),range(63,91),range(93,65535)])],{})", Factory.Production);
  private static final IConstructor prod__start__Module__layouts_LAYOUTLIST_top_Module_layouts_LAYOUTLIST_ = (IConstructor) _read("prod(start(sort(\"Module\")),[layouts(\"LAYOUTLIST\"),label(\"top\",sort(\"Module\")),layouts(\"LAYOUTLIST\")],{})", Factory.Production);
  private static final IConstructor prod__Empty_Tag__lit___64_layouts_LAYOUTLIST_name_Name__tag__Foldable_tag__category___67_111_109_109_101_110_116 = (IConstructor) _read("prod(label(\"Empty\",sort(\"Tag\")),[lit(\"@\"),layouts(\"LAYOUTLIST\"),label(\"name\",lex(\"Name\"))],{tag(Foldable()),tag(category(\"Comment\"))})", Factory.Production);
  private static final IConstructor prod__lit___58_47_47__char_class___range__58_58_char_class___range__47_47_char_class___range__47_47_ = (IConstructor) _read("prod(lit(\"://\"),[\\char-class([range(58,58)]),\\char-class([range(47,47)]),\\char-class([range(47,47)])],{})", Factory.Production);
  private static final IConstructor prod__BottomUp_Strategy__lit_bottom_up_ = (IConstructor) _read("prod(label(\"BottomUp\",sort(\"Strategy\")),[lit(\"bottom-up\")],{})", Factory.Production);
  private static final IConstructor prod__Parameters_Header__tags_Tags_layouts_LAYOUTLIST_lit_module_layouts_LAYOUTLIST_name_QualifiedName_layouts_LAYOUTLIST_params_ModuleParameters_layouts_LAYOUTLIST_imports_iter_star_seps__Import__layouts_LAYOUTLIST_ = (IConstructor) _read("prod(label(\"Parameters\",sort(\"Header\")),[label(\"tags\",sort(\"Tags\")),layouts(\"LAYOUTLIST\"),lit(\"module\"),layouts(\"LAYOUTLIST\"),label(\"name\",sort(\"QualifiedName\")),layouts(\"LAYOUTLIST\"),label(\"params\",sort(\"ModuleParameters\")),layouts(\"LAYOUTLIST\"),label(\"imports\",\\iter-star-seps(sort(\"Import\"),[layouts(\"LAYOUTLIST\")]))],{})", Factory.Production);
  private static final IConstructor prod__RegExp__char_class___range__92_92_char_class___range__47_47_range__60_60_range__62_62_range__92_92_ = (IConstructor) _read("prod(lex(\"RegExp\"),[\\char-class([range(92,92)]),\\char-class([range(47,47),range(60,60),range(62,62),range(92,92)])],{})", Factory.Production);
  private static final IConstructor prod__CallOrTree_Expression__expression_Expression_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_arguments_iter_star_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_ = (IConstructor) _read("prod(label(\"CallOrTree\",sort(\"Expression\")),[label(\"expression\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"arguments\",\\iter-star-seps(sort(\"Expression\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\")\")],{})", Factory.Production);
  private static final IConstructor prod__Sequence_Sym__lit___40_layouts_LAYOUTLIST_first_Sym_layouts_LAYOUTLIST_sequence_iter_seps__Sym__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_ = (IConstructor) _read("prod(label(\"Sequence\",sort(\"Sym\")),[lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"first\",sort(\"Sym\")),layouts(\"LAYOUTLIST\"),label(\"sequence\",\\iter-seps(sort(\"Sym\"),[layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\")\")],{})", Factory.Production);
  private static final IConstructor prod__Char__OctalEscapeSequence__tag__category___67_111_110_115_116_97_110_116 = (IConstructor) _read("prod(lex(\"Char\"),[lex(\"OctalEscapeSequence\")],{tag(category(\"Constant\"))})", Factory.Production);
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
  private static final IConstructor prod__lit_on__char_class___range__111_111_char_class___range__110_110_ = (IConstructor) _read("prod(lit(\"on\"),[\\char-class([range(111,111)]),\\char-class([range(110,110)])],{})", Factory.Production);
  private static final IConstructor prod__lit___47_42__char_class___range__47_47_char_class___range__42_42_ = (IConstructor) _read("prod(lit(\"/*\"),[\\char-class([range(47,47)]),\\char-class([range(42,42)])],{})", Factory.Production);
  private static final IConstructor prod__lit_is__char_class___range__105_105_char_class___range__115_115_ = (IConstructor) _read("prod(lit(\"is\"),[\\char-class([range(105,105)]),\\char-class([range(115,115)])],{})", Factory.Production);
  private static final IConstructor prod__lit_it__char_class___range__105_105_char_class___range__116_116_ = (IConstructor) _read("prod(lit(\"it\"),[\\char-class([range(105,105)]),\\char-class([range(116,116)])],{})", Factory.Production);
  private static final IConstructor prod__TryFinally_Statement__lit_try_layouts_LAYOUTLIST_body_Statement_layouts_LAYOUTLIST_handlers_iter_seps__Catch__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit_finally_layouts_LAYOUTLIST_finallyBody_Statement_ = (IConstructor) _read("prod(label(\"TryFinally\",sort(\"Statement\")),[lit(\"try\"),layouts(\"LAYOUTLIST\"),label(\"body\",sort(\"Statement\")),layouts(\"LAYOUTLIST\"),label(\"handlers\",\\iter-seps(sort(\"Catch\"),[layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\"finally\"),layouts(\"LAYOUTLIST\"),label(\"finallyBody\",sort(\"Statement\"))],{})", Factory.Production);
  private static final IConstructor prod__User_Type__conditional__user_UserType__delete__HeaderKeyword_ = (IConstructor) _read("prod(label(\"User\",sort(\"Type\")),[conditional(label(\"user\",sort(\"UserType\")),{delete(keywords(\"HeaderKeyword\"))})],{})", Factory.Production);
  private static final IConstructor prod__Post_PathTail__post_PostPathChars_ = (IConstructor) _read("prod(label(\"Post\",sort(\"PathTail\")),[label(\"post\",lex(\"PostPathChars\"))],{})", Factory.Production);
  private static final IConstructor prod__Abstract_FunctionDeclaration__tags_Tags_layouts_LAYOUTLIST_visibility_Visibility_layouts_LAYOUTLIST_signature_Signature_layouts_LAYOUTLIST_lit___59_ = (IConstructor) _read("prod(label(\"Abstract\",sort(\"FunctionDeclaration\")),[label(\"tags\",sort(\"Tags\")),layouts(\"LAYOUTLIST\"),label(\"visibility\",sort(\"Visibility\")),layouts(\"LAYOUTLIST\"),label(\"signature\",sort(\"Signature\")),layouts(\"LAYOUTLIST\"),lit(\";\")],{})", Factory.Production);
  private static final IConstructor prod__FieldAccess_Assignable__receiver_Assignable_layouts_LAYOUTLIST_lit___46_layouts_LAYOUTLIST_field_Name_ = (IConstructor) _read("prod(label(\"FieldAccess\",sort(\"Assignable\")),[label(\"receiver\",sort(\"Assignable\")),layouts(\"LAYOUTLIST\"),lit(\".\"),layouts(\"LAYOUTLIST\"),label(\"field\",lex(\"Name\"))],{})", Factory.Production);
  private static final IConstructor prod__Bracket_Expression__lit___40_layouts_LAYOUTLIST_expression_Expression_layouts_LAYOUTLIST_lit___41__bracket = (IConstructor) _read("prod(label(\"Bracket\",sort(\"Expression\")),[lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"expression\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\")\")],{bracket()})", Factory.Production);
  private static final IConstructor prod__lit_loc__char_class___range__108_108_char_class___range__111_111_char_class___range__99_99_ = (IConstructor) _read("prod(lit(\"loc\"),[\\char-class([range(108,108)]),\\char-class([range(111,111)]),\\char-class([range(99,99)])],{})", Factory.Production);
  private static final IConstructor prod__Layout_SyntaxDefinition__vis_Visibility_layouts_LAYOUTLIST_lit_layout_layouts_LAYOUTLIST_defined_Sym_layouts_LAYOUTLIST_lit___61_layouts_LAYOUTLIST_production_Prod_layouts_LAYOUTLIST_lit___59__tag__Foldable = (IConstructor) _read("prod(label(\"Layout\",sort(\"SyntaxDefinition\")),[label(\"vis\",sort(\"Visibility\")),layouts(\"LAYOUTLIST\"),lit(\"layout\"),layouts(\"LAYOUTLIST\"),label(\"defined\",sort(\"Sym\")),layouts(\"LAYOUTLIST\"),lit(\"=\"),layouts(\"LAYOUTLIST\"),label(\"production\",sort(\"Prod\")),layouts(\"LAYOUTLIST\"),lit(\";\")],{tag(Foldable())})", Factory.Production);
  private static final IConstructor prod__Precede_Sym__match_Sym_layouts_LAYOUTLIST_lit___60_60_layouts_LAYOUTLIST_symbol_Sym__assoc__right = (IConstructor) _read("prod(label(\"Precede\",sort(\"Sym\")),[label(\"match\",sort(\"Sym\")),layouts(\"LAYOUTLIST\"),lit(\"\\<\\<\"),layouts(\"LAYOUTLIST\"),label(\"symbol\",sort(\"Sym\"))],{assoc(right())})", Factory.Production);
  private static final IConstructor prod__BooleanLiteral__lit_false_ = (IConstructor) _read("prod(lex(\"BooleanLiteral\"),[lit(\"false\")],{})", Factory.Production);
  private static final IConstructor prod__lit_in__char_class___range__105_105_char_class___range__110_110_ = (IConstructor) _read("prod(lit(\"in\"),[\\char-class([range(105,105)]),\\char-class([range(110,110)])],{})", Factory.Production);
  private static final IConstructor prod__Assert_Statement__lit_assert_layouts_LAYOUTLIST_expression_Expression_layouts_LAYOUTLIST_lit___59_ = (IConstructor) _read("prod(label(\"Assert\",sort(\"Statement\")),[lit(\"assert\"),layouts(\"LAYOUTLIST\"),label(\"expression\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\";\")],{})", Factory.Production);
  private static final IConstructor prod__ReifiedConstructor_BasicType__lit_constructor_ = (IConstructor) _read("prod(label(\"ReifiedConstructor\",sort(\"BasicType\")),[lit(\"constructor\")],{})", Factory.Production);
  private static final IConstructor prod__Literal_Pattern__literal_Literal_ = (IConstructor) _read("prod(label(\"Literal\",sort(\"Pattern\")),[label(\"literal\",sort(\"Literal\"))],{})", Factory.Production);
  private static final IConstructor prod__lit_declarations__char_class___range__100_100_char_class___range__101_101_char_class___range__99_99_char_class___range__108_108_char_class___range__97_97_char_class___range__114_114_char_class___range__97_97_char_class___range__116_116_char_class___range__105_105_char_class___range__111_111_char_class___range__110_110_char_class___range__115_115_ = (IConstructor) _read("prod(lit(\"declarations\"),[\\char-class([range(100,100)]),\\char-class([range(101,101)]),\\char-class([range(99,99)]),\\char-class([range(108,108)]),\\char-class([range(97,97)]),\\char-class([range(114,114)]),\\char-class([range(97,97)]),\\char-class([range(116,116)]),\\char-class([range(105,105)]),\\char-class([range(111,111)]),\\char-class([range(110,110)]),\\char-class([range(115,115)])],{})", Factory.Production);
  private static final IConstructor prod__lit_right__char_class___range__114_114_char_class___range__105_105_char_class___range__103_103_char_class___range__104_104_char_class___range__116_116_ = (IConstructor) _read("prod(lit(\"right\"),[\\char-class([range(114,114)]),\\char-class([range(105,105)]),\\char-class([range(103,103)]),\\char-class([range(104,104)]),\\char-class([range(116,116)])],{})", Factory.Production);
  private static final IConstructor prod__DatePart__char_class___range__48_57_char_class___range__48_57_char_class___range__48_57_char_class___range__48_57_char_class___range__48_49_char_class___range__48_57_char_class___range__48_51_char_class___range__48_57_ = (IConstructor) _read("prod(lex(\"DatePart\"),[\\char-class([range(48,57)]),\\char-class([range(48,57)]),\\char-class([range(48,57)]),\\char-class([range(48,57)]),\\char-class([range(48,49)]),\\char-class([range(48,57)]),\\char-class([range(48,51)]),\\char-class([range(48,57)])],{})", Factory.Production);
  private static final IConstructor prod__Closure_Expression__type_Type_layouts_LAYOUTLIST_parameters_Parameters_layouts_LAYOUTLIST_lit___123_layouts_LAYOUTLIST_statements_iter_seps__Statement__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_ = (IConstructor) _read("prod(label(\"Closure\",sort(\"Expression\")),[label(\"type\",sort(\"Type\")),layouts(\"LAYOUTLIST\"),label(\"parameters\",sort(\"Parameters\")),layouts(\"LAYOUTLIST\"),lit(\"{\"),layouts(\"LAYOUTLIST\"),label(\"statements\",\\iter-seps(sort(\"Statement\"),[layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\"}\")],{})", Factory.Production);
  private static final IConstructor prod__Anti_Pattern__lit___33_layouts_LAYOUTLIST_pattern_Pattern_ = (IConstructor) _read("prod(label(\"Anti\",sort(\"Pattern\")),[lit(\"!\"),layouts(\"LAYOUTLIST\"),label(\"pattern\",sort(\"Pattern\"))],{})", Factory.Production);
  private static final IConstructor prod__DoWhile_Statement__label_Label_layouts_LAYOUTLIST_lit_do_layouts_LAYOUTLIST_body_Statement_layouts_LAYOUTLIST_lit_while_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_condition_Expression_layouts_LAYOUTLIST_lit___41_layouts_LAYOUTLIST_lit___59_ = (IConstructor) _read("prod(label(\"DoWhile\",sort(\"Statement\")),[label(\"label\",sort(\"Label\")),layouts(\"LAYOUTLIST\"),lit(\"do\"),layouts(\"LAYOUTLIST\"),label(\"body\",sort(\"Statement\")),layouts(\"LAYOUTLIST\"),lit(\"while\"),layouts(\"LAYOUTLIST\"),lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"condition\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\")\"),layouts(\"LAYOUTLIST\"),lit(\";\")],{})", Factory.Production);
  private static final IConstructor prod__RascalKeywords__lit_test_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"test\")],{})", Factory.Production);
  private static final IConstructor prod__Join_Expression__lhs_Expression_layouts_LAYOUTLIST_lit_join_layouts_LAYOUTLIST_rhs_Expression__assoc__left = (IConstructor) _read("prod(label(\"Join\",sort(\"Expression\")),[label(\"lhs\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"join\"),layouts(\"LAYOUTLIST\"),label(\"rhs\",sort(\"Expression\"))],{assoc(left())})", Factory.Production);
  private static final IConstructor prod__Innermost_Strategy__lit_innermost_ = (IConstructor) _read("prod(label(\"Innermost\",sort(\"Strategy\")),[lit(\"innermost\")],{})", Factory.Production);
  private static final IConstructor prod__RascalKeywords__lit_start_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"start\")],{})", Factory.Production);
  private static final IConstructor prod__Throw_Statement__lit_throw_layouts_LAYOUTLIST_statement_Statement__assoc__non_assoc = (IConstructor) _read("prod(label(\"Throw\",sort(\"Statement\")),[lit(\"throw\"),layouts(\"LAYOUTLIST\"),label(\"statement\",sort(\"Statement\"))],{assoc(\\non-assoc())})", Factory.Production);
  private static final IConstructor prod__Expression_Statement__expression_Expression_layouts_LAYOUTLIST_lit___59_ = (IConstructor) _read("prod(label(\"Expression\",sort(\"Statement\")),[label(\"expression\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\";\")],{})", Factory.Production);
  private static final IConstructor prod__Char__char_class___range__0_31_range__33_33_range__35_38_range__40_44_range__46_59_range__61_61_range__63_90_range__94_65535__tag__category___67_111_110_115_116_97_110_116 = (IConstructor) _read("prod(lex(\"Char\"),[\\char-class([range(0,31),range(33,33),range(35,38),range(40,44),range(46,59),range(61,61),range(63,90),range(94,65535)])],{tag(category(\"Constant\"))})", Factory.Production);
  private static final IConstructor prod__lit___59__char_class___range__59_59_ = (IConstructor) _read("prod(lit(\";\"),[\\char-class([range(59,59)])],{})", Factory.Production);
  private static final IConstructor prod__lit___58__char_class___range__58_58_ = (IConstructor) _read("prod(lit(\":\"),[\\char-class([range(58,58)])],{})", Factory.Production);
  private static final IConstructor prod__Insert_Statement__lit_insert_layouts_LAYOUTLIST_dataTarget_DataTarget_layouts_LAYOUTLIST_statement_Statement__assoc__non_assoc = (IConstructor) _read("prod(label(\"Insert\",sort(\"Statement\")),[lit(\"insert\"),layouts(\"LAYOUTLIST\"),label(\"dataTarget\",sort(\"DataTarget\")),layouts(\"LAYOUTLIST\"),label(\"statement\",sort(\"Statement\"))],{assoc(\\non-assoc())})", Factory.Production);
  private static final IConstructor prod__lit___61__char_class___range__61_61_ = (IConstructor) _read("prod(lit(\"=\"),[\\char-class([range(61,61)])],{})", Factory.Production);
  private static final IConstructor prod__lit_data__char_class___range__100_100_char_class___range__97_97_char_class___range__116_116_char_class___range__97_97_ = (IConstructor) _read("prod(lit(\"data\"),[\\char-class([range(100,100)]),\\char-class([range(97,97)]),\\char-class([range(116,116)]),\\char-class([range(97,97)])],{})", Factory.Production);
  private static final IConstructor prod__Data_Kind__lit_data_ = (IConstructor) _read("prod(label(\"Data\",sort(\"Kind\")),[lit(\"data\")],{})", Factory.Production);
  private static final IConstructor prod__lit___60__char_class___range__60_60_ = (IConstructor) _read("prod(lit(\"\\<\"),[\\char-class([range(60,60)])],{})", Factory.Production);
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
  private static final IConstructor prod__Conditional_FunctionDeclaration__tags_Tags_layouts_LAYOUTLIST_visibility_Visibility_layouts_LAYOUTLIST_signature_Signature_layouts_LAYOUTLIST_lit___61_layouts_LAYOUTLIST_expression_Expression_layouts_LAYOUTLIST_lit_when_layouts_LAYOUTLIST_conditions_iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___59__tag__Foldable = (IConstructor) _read("prod(label(\"Conditional\",sort(\"FunctionDeclaration\")),[label(\"tags\",sort(\"Tags\")),layouts(\"LAYOUTLIST\"),label(\"visibility\",sort(\"Visibility\")),layouts(\"LAYOUTLIST\"),label(\"signature\",sort(\"Signature\")),layouts(\"LAYOUTLIST\"),lit(\"=\"),layouts(\"LAYOUTLIST\"),label(\"expression\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"when\"),layouts(\"LAYOUTLIST\"),label(\"conditions\",\\iter-seps(sort(\"Expression\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\";\")],{tag(Foldable())})", Factory.Production);
  private static final IConstructor prod__lit___40__char_class___range__40_40_ = (IConstructor) _read("prod(lit(\"(\"),[\\char-class([range(40,40)])],{})", Factory.Production);
  private static final IConstructor prod__lit___43__char_class___range__43_43_ = (IConstructor) _read("prod(lit(\"+\"),[\\char-class([range(43,43)])],{})", Factory.Production);
  private static final IConstructor prod__lit___42__char_class___range__42_42_ = (IConstructor) _read("prod(lit(\"*\"),[\\char-class([range(42,42)])],{})", Factory.Production);
  private static final IConstructor prod__RascalKeywords__lit_try_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"try\")],{})", Factory.Production);
  private static final IConstructor prod__lit____char_class___range__45_45_ = (IConstructor) _read("prod(lit(\"-\"),[\\char-class([range(45,45)])],{})", Factory.Production);
  private static final IConstructor prod__OctalIntegerLiteral__char_class___range__48_48_conditional__iter__char_class___range__48_55__not_follow__char_class___range__48_57_range__65_90_range__95_95_range__97_122_ = (IConstructor) _read("prod(lex(\"OctalIntegerLiteral\"),[\\char-class([range(48,48)]),conditional(iter(\\char-class([range(48,55)])),{\\not-follow(\\char-class([range(48,57),range(65,90),range(95,95),range(97,122)]))})],{})", Factory.Production);
  private static final IConstructor prod__lit___44__char_class___range__44_44_ = (IConstructor) _read("prod(lit(\",\"),[\\char-class([range(44,44)])],{})", Factory.Production);
  private static final IConstructor prod__RascalKeywords__lit_non_terminal_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"non-terminal\")],{})", Factory.Production);
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
  private static final IConstructor prod__TagString__lit___123_iter_star__alt___seq___char_class___range__92_92_char_class___range__125_125_char_class___range__0_124_range__126_65535_lit___125_ = (IConstructor) _read("prod(lex(\"TagString\"),[lit(\"{\"),\\iter-star(alt({seq([\\char-class([range(92,92)]),\\char-class([range(125,125)])]),\\char-class([range(0,124),range(126,65535)])})),lit(\"}\")],{})", Factory.Production);
  private static final IConstructor prod__lit___38__char_class___range__38_38_ = (IConstructor) _read("prod(lit(\"&\"),[\\char-class([range(38,38)])],{})", Factory.Production);
  private static final IConstructor prod__lit_datetime__char_class___range__100_100_char_class___range__97_97_char_class___range__116_116_char_class___range__101_101_char_class___range__116_116_char_class___range__105_105_char_class___range__109_109_char_class___range__101_101_ = (IConstructor) _read("prod(lit(\"datetime\"),[\\char-class([range(100,100)]),\\char-class([range(97,97)]),\\char-class([range(116,116)]),\\char-class([range(101,101)]),\\char-class([range(116,116)]),\\char-class([range(105,105)]),\\char-class([range(109,109)]),\\char-class([range(101,101)])],{})", Factory.Production);
  private static final IConstructor prod__Comprehension_Expression__comprehension_Comprehension_ = (IConstructor) _read("prod(label(\"Comprehension\",sort(\"Expression\")),[label(\"comprehension\",sort(\"Comprehension\"))],{})", Factory.Production);
  private static final IConstructor prod__Associativity_ProdModifier__associativity_Assoc_ = (IConstructor) _read("prod(label(\"Associativity\",sort(\"ProdModifier\")),[label(\"associativity\",sort(\"Assoc\"))],{})", Factory.Production);
  private static final IConstructor prod__Bracket_Class__lit___40_layouts_LAYOUTLIST_charclass_Class_layouts_LAYOUTLIST_lit___41__bracket = (IConstructor) _read("prod(label(\"Bracket\",sort(\"Class\")),[lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"charclass\",sort(\"Class\")),layouts(\"LAYOUTLIST\"),lit(\")\")],{bracket()})", Factory.Production);
  private static final IConstructor prod__NonInterpolated_ProtocolPart__protocolChars_ProtocolChars_ = (IConstructor) _read("prod(label(\"NonInterpolated\",sort(\"ProtocolPart\")),[label(\"protocolChars\",lex(\"ProtocolChars\"))],{})", Factory.Production);
  private static final IConstructor prod__lit_Z__char_class___range__90_90_ = (IConstructor) _read("prod(lit(\"Z\"),[\\char-class([range(90,90)])],{})", Factory.Production);
  private static final IConstructor prod__lit___91__char_class___range__91_91_ = (IConstructor) _read("prod(lit(\"[\"),[\\char-class([range(91,91)])],{})", Factory.Production);
  private static final IConstructor prod__Free_TypeVar__lit___38_layouts_LAYOUTLIST_name_Name_ = (IConstructor) _read("prod(label(\"Free\",sort(\"TypeVar\")),[lit(\"&\"),layouts(\"LAYOUTLIST\"),label(\"name\",lex(\"Name\"))],{})", Factory.Production);
  private static final IConstructor prod__lit___94__char_class___range__94_94_ = (IConstructor) _read("prod(lit(\"^\"),[\\char-class([range(94,94)])],{})", Factory.Production);
  private static final IConstructor prod__RascalKeywords__lit_append_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"append\")],{})", Factory.Production);
  private static final IConstructor prod__lit___92__char_class___range__92_92_ = (IConstructor) _read("prod(lit(\"\\\\\"),[\\char-class([range(92,92)])],{})", Factory.Production);
  private static final IConstructor prod__RascalKeywords__lit_notin_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"notin\")],{})", Factory.Production);
  private static final IConstructor prod__lit___93__char_class___range__93_93_ = (IConstructor) _read("prod(lit(\"]\"),[\\char-class([range(93,93)])],{})", Factory.Production);
  private static final IConstructor prod__Anno_Kind__lit_anno_ = (IConstructor) _read("prod(label(\"Anno\",sort(\"Kind\")),[lit(\"anno\")],{})", Factory.Production);
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
  private static final IConstructor prod__ReifiedType_Expression__basicType_BasicType_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_arguments_iter_star_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_ = (IConstructor) _read("prod(label(\"ReifiedType\",sort(\"Expression\")),[label(\"basicType\",sort(\"BasicType\")),layouts(\"LAYOUTLIST\"),lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"arguments\",\\iter-star-seps(sort(\"Expression\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\")\")],{})", Factory.Production);
  private static final IConstructor prod__Associative_Assoc__lit_assoc_ = (IConstructor) _read("prod(label(\"Associative\",sort(\"Assoc\")),[lit(\"assoc\")],{})", Factory.Production);
  private static final IConstructor prod__lit___33_58_61__char_class___range__33_33_char_class___range__58_58_char_class___range__61_61_ = (IConstructor) _read("prod(lit(\"!:=\"),[\\char-class([range(33,33)]),\\char-class([range(58,58)]),\\char-class([range(61,61)])],{})", Factory.Production);
  private static final IConstructor prod__Bag_BasicType__lit_bag_ = (IConstructor) _read("prod(label(\"Bag\",sort(\"BasicType\")),[lit(\"bag\")],{})", Factory.Production);
  private static final IConstructor prod__lit___125__char_class___range__125_125_ = (IConstructor) _read("prod(lit(\"}\"),[\\char-class([range(125,125)])],{})", Factory.Production);
  private static final IConstructor prod__RascalKeywords__lit_while_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"while\")],{})", Factory.Production);
  private static final IConstructor prod__lit___124__char_class___range__124_124_ = (IConstructor) _read("prod(lit(\"|\"),[\\char-class([range(124,124)])],{})", Factory.Production);
  private static final IConstructor prod__Modulo_Expression__lhs_Expression_layouts_LAYOUTLIST_lit___37_layouts_LAYOUTLIST_rhs_Expression__assoc__left = (IConstructor) _read("prod(label(\"Modulo\",sort(\"Expression\")),[label(\"lhs\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"%\"),layouts(\"LAYOUTLIST\"),label(\"rhs\",sort(\"Expression\"))],{assoc(left())})", Factory.Production);
  private static final IConstructor prod__lit___42_47__char_class___range__42_42_char_class___range__47_47_ = (IConstructor) _read("prod(lit(\"*/\"),[\\char-class([range(42,42)]),\\char-class([range(47,47)])],{})", Factory.Production);
  private static final IConstructor prod__TypedVariable_Pattern__type_Type_layouts_LAYOUTLIST_name_Name_ = (IConstructor) _read("prod(label(\"TypedVariable\",sort(\"Pattern\")),[label(\"type\",sort(\"Type\")),layouts(\"LAYOUTLIST\"),label(\"name\",lex(\"Name\"))],{})", Factory.Production);
  private static final IConstructor prod__QualifiedName_Expression__qualifiedName_QualifiedName_ = (IConstructor) _read("prod(label(\"QualifiedName\",sort(\"Expression\")),[label(\"qualifiedName\",sort(\"QualifiedName\"))],{})", Factory.Production);
  private static final IConstructor prod__Binding_Catch__lit_catch_layouts_LAYOUTLIST_pattern_Pattern_layouts_LAYOUTLIST_lit___58_layouts_LAYOUTLIST_body_Statement__tag__Foldable = (IConstructor) _read("prod(label(\"Binding\",sort(\"Catch\")),[lit(\"catch\"),layouts(\"LAYOUTLIST\"),label(\"pattern\",sort(\"Pattern\")),layouts(\"LAYOUTLIST\"),lit(\":\"),layouts(\"LAYOUTLIST\"),label(\"body\",sort(\"Statement\"))],{tag(Foldable())})", Factory.Production);
  private static final IConstructor prod__Conditional_Replacement__replacementExpression_Expression_layouts_LAYOUTLIST_lit_when_layouts_LAYOUTLIST_conditions_iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_ = (IConstructor) _read("prod(label(\"Conditional\",sort(\"Replacement\")),[label(\"replacementExpression\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"when\"),layouts(\"LAYOUTLIST\"),label(\"conditions\",\\iter-seps(sort(\"Expression\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")]))],{})", Factory.Production);
  private static final IConstructor prod__Index_Field__fieldIndex_IntegerLiteral_ = (IConstructor) _read("prod(label(\"Index\",sort(\"Field\")),[label(\"fieldIndex\",sort(\"IntegerLiteral\"))],{})", Factory.Production);
  private static final IConstructor prod__RascalKeywords__lit_finally_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"finally\")],{})", Factory.Production);
  private static final IConstructor prod__Implication_Expression__lhs_Expression_layouts_LAYOUTLIST_lit___61_61_62_layouts_LAYOUTLIST_rhs_Expression__assoc__non_assoc = (IConstructor) _read("prod(label(\"Implication\",sort(\"Expression\")),[label(\"lhs\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"==\\>\"),layouts(\"LAYOUTLIST\"),label(\"rhs\",sort(\"Expression\"))],{assoc(\\non-assoc())})", Factory.Production);
  private static final IConstructor prod__Labeled_Prod__modifiers_iter_star_seps__ProdModifier__layouts_LAYOUTLIST_layouts_LAYOUTLIST_name_Name_layouts_LAYOUTLIST_lit___58_layouts_LAYOUTLIST_args_iter_star_seps__Sym__layouts_LAYOUTLIST_ = (IConstructor) _read("prod(label(\"Labeled\",sort(\"Prod\")),[label(\"modifiers\",\\iter-star-seps(sort(\"ProdModifier\"),[layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),label(\"name\",lex(\"Name\")),layouts(\"LAYOUTLIST\"),lit(\":\"),layouts(\"LAYOUTLIST\"),label(\"args\",\\iter-star-seps(sort(\"Sym\"),[layouts(\"LAYOUTLIST\")]))],{})", Factory.Production);
  private static final IConstructor prod__lit_o__char_class___range__111_111_ = (IConstructor) _read("prod(lit(\"o\"),[\\char-class([range(111,111)])],{})", Factory.Production);
  private static final IConstructor prod__MidStringChars__char_class___range__62_62_iter_star__StringCharacter_char_class___range__60_60__tag__category___67_111_110_115_116_97_110_116 = (IConstructor) _read("prod(lex(\"MidStringChars\"),[\\char-class([range(62,62)]),\\iter-star(lex(\"StringCharacter\")),\\char-class([range(60,60)])],{tag(category(\"Constant\"))})", Factory.Production);
  private static final IConstructor prod__Expression_Tag__lit___64_layouts_LAYOUTLIST_name_Name_layouts_LAYOUTLIST_lit___61_layouts_LAYOUTLIST_expression_Expression__tag__Foldable_tag__category___67_111_109_109_101_110_116 = (IConstructor) _read("prod(label(\"Expression\",sort(\"Tag\")),[lit(\"@\"),layouts(\"LAYOUTLIST\"),label(\"name\",lex(\"Name\")),layouts(\"LAYOUTLIST\"),lit(\"=\"),layouts(\"LAYOUTLIST\"),label(\"expression\",sort(\"Expression\"))],{tag(Foldable()),tag(category(\"Comment\"))})", Factory.Production);
  private static final IConstructor prod__Intersection_Assignment__lit___38_61_ = (IConstructor) _read("prod(label(\"Intersection\",sort(\"Assignment\")),[lit(\"&=\")],{})", Factory.Production);
  private static final IConstructor prod__Labeled_DataTarget__label_Name_layouts_LAYOUTLIST_lit___58_ = (IConstructor) _read("prod(label(\"Labeled\",sort(\"DataTarget\")),[label(\"label\",lex(\"Name\")),layouts(\"LAYOUTLIST\"),lit(\":\")],{})", Factory.Production);
  private static final IConstructor prod__lit_unimport__char_class___range__117_117_char_class___range__110_110_char_class___range__105_105_char_class___range__109_109_char_class___range__112_112_char_class___range__111_111_char_class___range__114_114_char_class___range__116_116_ = (IConstructor) _read("prod(lit(\"unimport\"),[\\char-class([range(117,117)]),\\char-class([range(110,110)]),\\char-class([range(105,105)]),\\char-class([range(109,109)]),\\char-class([range(112,112)]),\\char-class([range(111,111)]),\\char-class([range(114,114)]),\\char-class([range(116,116)])],{})", Factory.Production);
  private static final IConstructor prod__Map_BasicType__lit_map_ = (IConstructor) _read("prod(label(\"Map\",sort(\"BasicType\")),[lit(\"map\")],{})", Factory.Production);
  private static final IConstructor prod__RegExpModifier__iter_star__char_class___range__100_100_range__105_105_range__109_109_range__115_115_ = (IConstructor) _read("prod(lex(\"RegExpModifier\"),[\\iter-star(\\char-class([range(100,100),range(105,105),range(109,109),range(115,115)]))],{})", Factory.Production);
  private static final IConstructor prod__lit___42_61__char_class___range__42_42_char_class___range__61_61_ = (IConstructor) _read("prod(lit(\"*=\"),[\\char-class([range(42,42)]),\\char-class([range(61,61)])],{})", Factory.Production);
  private static final IConstructor prod__Range_Expression__lit___91_layouts_LAYOUTLIST_first_Expression_layouts_LAYOUTLIST_lit___46_46_layouts_LAYOUTLIST_last_Expression_layouts_LAYOUTLIST_lit___93_ = (IConstructor) _read("prod(label(\"Range\",sort(\"Expression\")),[lit(\"[\"),layouts(\"LAYOUTLIST\"),label(\"first\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"..\"),layouts(\"LAYOUTLIST\"),label(\"last\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"]\")],{})", Factory.Production);
  private static final IConstructor prod__TimeZonePart__char_class___range__43_43_range__45_45_char_class___range__48_49_char_class___range__48_57_char_class___range__48_53_char_class___range__48_57_ = (IConstructor) _read("prod(lex(\"TimeZonePart\"),[\\char-class([range(43,43),range(45,45)]),\\char-class([range(48,49)]),\\char-class([range(48,57)]),\\char-class([range(48,53)]),\\char-class([range(48,57)])],{})", Factory.Production);
  private static final IConstructor regular__alt___conditional__char_class___range__42_42__not_follow__char_class___range__47_47_char_class___range__0_41_range__43_65535 = (IConstructor) _read("regular(alt({conditional(\\char-class([range(42,42)]),{\\not-follow(\\char-class([range(47,47)]))}),\\char-class([range(0,41),range(43,65535)])}))", Factory.Production);
  private static final IConstructor prod__lit_edit__char_class___range__101_101_char_class___range__100_100_char_class___range__105_105_char_class___range__116_116_ = (IConstructor) _read("prod(lit(\"edit\"),[\\char-class([range(101,101)]),\\char-class([range(100,100)]),\\char-class([range(105,105)]),\\char-class([range(116,116)])],{})", Factory.Production);
  private static final IConstructor prod__Variable_Declaration__tags_Tags_layouts_LAYOUTLIST_visibility_Visibility_layouts_LAYOUTLIST_type_Type_layouts_LAYOUTLIST_variables_iter_seps__Variable__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___59_ = (IConstructor) _read("prod(label(\"Variable\",sort(\"Declaration\")),[label(\"tags\",sort(\"Tags\")),layouts(\"LAYOUTLIST\"),label(\"visibility\",sort(\"Visibility\")),layouts(\"LAYOUTLIST\"),label(\"type\",sort(\"Type\")),layouts(\"LAYOUTLIST\"),label(\"variables\",\\iter-seps(sort(\"Variable\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\";\")],{})", Factory.Production);
  private static final IConstructor prod__RegExp__lit___60_Name_lit___62_ = (IConstructor) _read("prod(lex(\"RegExp\"),[lit(\"\\<\"),lex(\"Name\"),lit(\"\\>\")],{})", Factory.Production);
  private static final IConstructor prod__DoWhile_StringTemplate__lit_do_layouts_LAYOUTLIST_lit___123_layouts_LAYOUTLIST_preStats_iter_star_seps__Statement__layouts_LAYOUTLIST_layouts_LAYOUTLIST_body_StringMiddle_layouts_LAYOUTLIST_postStats_iter_star_seps__Statement__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_layouts_LAYOUTLIST_lit_while_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_condition_Expression_layouts_LAYOUTLIST_lit___41_ = (IConstructor) _read("prod(label(\"DoWhile\",sort(\"StringTemplate\")),[lit(\"do\"),layouts(\"LAYOUTLIST\"),lit(\"{\"),layouts(\"LAYOUTLIST\"),label(\"preStats\",\\iter-star-seps(sort(\"Statement\"),[layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),label(\"body\",sort(\"StringMiddle\")),layouts(\"LAYOUTLIST\"),label(\"postStats\",\\iter-star-seps(sort(\"Statement\"),[layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\"}\"),layouts(\"LAYOUTLIST\"),lit(\"while\"),layouts(\"LAYOUTLIST\"),lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"condition\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\")\")],{})", Factory.Production);
  private static final IConstructor prod__IfThenElse_StringTemplate__lit_if_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_conditions_iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_layouts_LAYOUTLIST_lit___123_layouts_LAYOUTLIST_preStatsThen_iter_star_seps__Statement__layouts_LAYOUTLIST_layouts_LAYOUTLIST_thenString_StringMiddle_layouts_LAYOUTLIST_postStatsThen_iter_star_seps__Statement__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_layouts_LAYOUTLIST_lit_else_layouts_LAYOUTLIST_lit___123_layouts_LAYOUTLIST_preStatsElse_iter_star_seps__Statement__layouts_LAYOUTLIST_layouts_LAYOUTLIST_elseString_StringMiddle_layouts_LAYOUTLIST_postStatsElse_iter_star_seps__Statement__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_ = (IConstructor) _read("prod(label(\"IfThenElse\",sort(\"StringTemplate\")),[lit(\"if\"),layouts(\"LAYOUTLIST\"),lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"conditions\",\\iter-seps(sort(\"Expression\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\")\"),layouts(\"LAYOUTLIST\"),lit(\"{\"),layouts(\"LAYOUTLIST\"),label(\"preStatsThen\",\\iter-star-seps(sort(\"Statement\"),[layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),label(\"thenString\",sort(\"StringMiddle\")),layouts(\"LAYOUTLIST\"),label(\"postStatsThen\",\\iter-star-seps(sort(\"Statement\"),[layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\"}\"),layouts(\"LAYOUTLIST\"),lit(\"else\"),layouts(\"LAYOUTLIST\"),lit(\"{\"),layouts(\"LAYOUTLIST\"),label(\"preStatsElse\",\\iter-star-seps(sort(\"Statement\"),[layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),label(\"elseString\",sort(\"StringMiddle\")),layouts(\"LAYOUTLIST\"),label(\"postStatsElse\",\\iter-star-seps(sort(\"Statement\"),[layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\"}\")],{})", Factory.Production);
  private static final IConstructor prod__RationalLiteral__char_class___range__49_57_iter_star__char_class___range__48_57_char_class___range__114_114_ = (IConstructor) _read("prod(lex(\"RationalLiteral\"),[\\char-class([range(49,57)]),\\iter-star(\\char-class([range(48,57)])),\\char-class([range(114,114)])],{})", Factory.Production);
  private static final IConstructor prod__lit_node__char_class___range__110_110_char_class___range__111_111_char_class___range__100_100_char_class___range__101_101_ = (IConstructor) _read("prod(lit(\"node\"),[\\char-class([range(110,110)]),\\char-class([range(111,111)]),\\char-class([range(100,100)]),\\char-class([range(101,101)])],{})", Factory.Production);
  private static final IConstructor prod__TimeZonePart__char_class___range__43_43_range__45_45_char_class___range__48_49_char_class___range__48_57_lit___58_char_class___range__48_53_char_class___range__48_57_ = (IConstructor) _read("prod(lex(\"TimeZonePart\"),[\\char-class([range(43,43),range(45,45)]),\\char-class([range(48,49)]),\\char-class([range(48,57)]),lit(\":\"),\\char-class([range(48,53)]),\\char-class([range(48,57)])],{})", Factory.Production);
  private static final IConstructor regular__iter_seps__Name__layouts_LAYOUTLIST_lit___58_58_layouts_LAYOUTLIST = (IConstructor) _read("regular(\\iter-seps(lex(\"Name\"),[layouts(\"LAYOUTLIST\"),lit(\"::\"),layouts(\"LAYOUTLIST\")]))", Factory.Production);
  private static final IConstructor prod__RascalKeywords__lit_true_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"true\")],{})", Factory.Production);
  private static final IConstructor prod__Solve_Statement__lit_solve_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_variables_iter_seps__QualifiedName__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_bound_Bound_layouts_LAYOUTLIST_lit___41_layouts_LAYOUTLIST_body_Statement_ = (IConstructor) _read("prod(label(\"Solve\",sort(\"Statement\")),[lit(\"solve\"),layouts(\"LAYOUTLIST\"),lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"variables\",\\iter-seps(sort(\"QualifiedName\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),label(\"bound\",sort(\"Bound\")),layouts(\"LAYOUTLIST\"),lit(\")\"),layouts(\"LAYOUTLIST\"),label(\"body\",sort(\"Statement\"))],{})", Factory.Production);
  private static final IConstructor prod__Visit_Expression__label_Label_layouts_LAYOUTLIST_visit_Visit_ = (IConstructor) _read("prod(label(\"Visit\",sort(\"Expression\")),[label(\"label\",sort(\"Label\")),layouts(\"LAYOUTLIST\"),label(\"visit\",sort(\"Visit\"))],{})", Factory.Production);
  private static final IConstructor prod__Actuals_ImportedModule__name_QualifiedName_layouts_LAYOUTLIST_actuals_ModuleActuals_ = (IConstructor) _read("prod(label(\"Actuals\",sort(\"ImportedModule\")),[label(\"name\",sort(\"QualifiedName\")),layouts(\"LAYOUTLIST\"),label(\"actuals\",sort(\"ModuleActuals\"))],{})", Factory.Production);
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
  private static final IConstructor regular__opt__TimeZonePart = (IConstructor) _read("regular(opt(lex(\"TimeZonePart\")))", Factory.Production);
  private static final IConstructor prod__IterSep_Sym__lit___123_layouts_LAYOUTLIST_symbol_Sym_layouts_LAYOUTLIST_sep_Sym_layouts_LAYOUTLIST_lit___125_layouts_LAYOUTLIST_lit___43_ = (IConstructor) _read("prod(label(\"IterSep\",sort(\"Sym\")),[lit(\"{\"),layouts(\"LAYOUTLIST\"),label(\"symbol\",sort(\"Sym\")),layouts(\"LAYOUTLIST\"),label(\"sep\",sort(\"Sym\")),layouts(\"LAYOUTLIST\"),lit(\"}\"),layouts(\"LAYOUTLIST\"),lit(\"+\")],{})", Factory.Production);
  private static final IConstructor prod__Value_BasicType__lit_value_ = (IConstructor) _read("prod(label(\"Value\",sort(\"BasicType\")),[lit(\"value\")],{})", Factory.Production);
  private static final IConstructor prod__Difference_Class__lhs_Class_layouts_LAYOUTLIST_lit___layouts_LAYOUTLIST_rhs_Class__assoc__left = (IConstructor) _read("prod(label(\"Difference\",sort(\"Class\")),[label(\"lhs\",sort(\"Class\")),layouts(\"LAYOUTLIST\"),lit(\"-\"),layouts(\"LAYOUTLIST\"),label(\"rhs\",sort(\"Class\"))],{assoc(left())})", Factory.Production);
  private static final IConstructor prod__Constructor_Assignable__name_Name_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_arguments_iter_seps__Assignable__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_ = (IConstructor) _read("prod(label(\"Constructor\",sort(\"Assignable\")),[label(\"name\",lex(\"Name\")),layouts(\"LAYOUTLIST\"),lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"arguments\",\\iter-seps(sort(\"Assignable\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\")\")],{})", Factory.Production);
  private static final IConstructor prod__CallOrTree_Pattern__expression_Pattern_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_arguments_iter_star_seps__Pattern__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_ = (IConstructor) _read("prod(label(\"CallOrTree\",sort(\"Pattern\")),[label(\"expression\",sort(\"Pattern\")),layouts(\"LAYOUTLIST\"),lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"arguments\",\\iter-star-seps(sort(\"Pattern\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\")\")],{})", Factory.Production);
  private static final IConstructor prod__IfThen_StringTemplate__lit_if_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_conditions_iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_layouts_LAYOUTLIST_lit___123_layouts_LAYOUTLIST_preStats_iter_star_seps__Statement__layouts_LAYOUTLIST_layouts_LAYOUTLIST_body_StringMiddle_layouts_LAYOUTLIST_postStats_iter_star_seps__Statement__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_ = (IConstructor) _read("prod(label(\"IfThen\",sort(\"StringTemplate\")),[lit(\"if\"),layouts(\"LAYOUTLIST\"),lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"conditions\",\\iter-seps(sort(\"Expression\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\")\"),layouts(\"LAYOUTLIST\"),lit(\"{\"),layouts(\"LAYOUTLIST\"),label(\"preStats\",\\iter-star-seps(sort(\"Statement\"),[layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),label(\"body\",sort(\"StringMiddle\")),layouts(\"LAYOUTLIST\"),label(\"postStats\",\\iter-star-seps(sort(\"Statement\"),[layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\"}\")],{})", Factory.Production);
  private static final IConstructor prod__Complement_Class__lit___33_layouts_LAYOUTLIST_charClass_Class_ = (IConstructor) _read("prod(label(\"Complement\",sort(\"Class\")),[lit(\"!\"),layouts(\"LAYOUTLIST\"),label(\"charClass\",sort(\"Class\"))],{})", Factory.Production);
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
  private static final IConstructor regular__seq___char_class___range__92_92_char_class___range__125_125 = (IConstructor) _read("regular(seq([\\char-class([range(92,92)]),\\char-class([range(125,125)])]))", Factory.Production);
  private static final IConstructor prod__Tuple_Pattern__lit___60_layouts_LAYOUTLIST_elements_iter_seps__Pattern__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___62_ = (IConstructor) _read("prod(label(\"Tuple\",sort(\"Pattern\")),[lit(\"\\<\"),layouts(\"LAYOUTLIST\"),label(\"elements\",\\iter-seps(sort(\"Pattern\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\"\\>\")],{})", Factory.Production);
  private static final IConstructor prod__Right_Assoc__lit_right_ = (IConstructor) _read("prod(label(\"Right\",sort(\"Assoc\")),[lit(\"right\")],{})", Factory.Production);
  private static final IConstructor prod__RascalKeywords__lit_bag_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"bag\")],{})", Factory.Production);
  private static final IConstructor prod__NamedRegExp__lit___60_Name_lit___62_ = (IConstructor) _read("prod(lex(\"NamedRegExp\"),[lit(\"\\<\"),lex(\"Name\"),lit(\"\\>\")],{})", Factory.Production);
  private static final IConstructor prod__Empty_Sym__lit___40_layouts_LAYOUTLIST_lit___41_ = (IConstructor) _read("prod(label(\"Empty\",sort(\"Sym\")),[lit(\"(\"),layouts(\"LAYOUTLIST\"),lit(\")\")],{})", Factory.Production);
  private static final IConstructor regular__seq___conditional__char_class___range__65_90_range__95_95_range__97_122__not_precede__char_class___range__65_90_range__95_95_range__97_122_conditional__iter_star__char_class___range__48_57_range__65_90_range__95_95_range__97_122__not_follow__char_class___range__48_57_range__65_90_range__95_95_range__97_122 = (IConstructor) _read("regular(seq([conditional(\\char-class([range(65,90),range(95,95),range(97,122)]),{\\not-precede(\\char-class([range(65,90),range(95,95),range(97,122)]))}),conditional(\\iter-star(\\char-class([range(48,57),range(65,90),range(95,95),range(97,122)])),{\\not-follow(\\char-class([range(48,57),range(65,90),range(95,95),range(97,122)]))})]))", Factory.Production);
  private static final IConstructor prod__lit_outermost__char_class___range__111_111_char_class___range__117_117_char_class___range__116_116_char_class___range__101_101_char_class___range__114_114_char_class___range__109_109_char_class___range__111_111_char_class___range__115_115_char_class___range__116_116_ = (IConstructor) _read("prod(lit(\"outermost\"),[\\char-class([range(111,111)]),\\char-class([range(117,117)]),\\char-class([range(116,116)]),\\char-class([range(101,101)]),\\char-class([range(114,114)]),\\char-class([range(109,109)]),\\char-class([range(111,111)]),\\char-class([range(115,115)]),\\char-class([range(116,116)])],{})", Factory.Production);
  private static final IConstructor prod__Negation_Expression__lit___33_layouts_LAYOUTLIST_argument_Expression_ = (IConstructor) _read("prod(label(\"Negation\",sort(\"Expression\")),[lit(\"!\"),layouts(\"LAYOUTLIST\"),label(\"argument\",sort(\"Expression\"))],{})", Factory.Production);
  private static final IConstructor prod__Interpolated_StringMiddle__mid_MidStringChars_layouts_LAYOUTLIST_expression_Expression_layouts_LAYOUTLIST_tail_StringMiddle_ = (IConstructor) _read("prod(label(\"Interpolated\",sort(\"StringMiddle\")),[label(\"mid\",lex(\"MidStringChars\")),layouts(\"LAYOUTLIST\"),label(\"expression\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),label(\"tail\",sort(\"StringMiddle\"))],{})", Factory.Production);
  private static final IConstructor prod__RascalKeywords__lit_case_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"case\")],{})", Factory.Production);
  private static final IConstructor prod__RascalKeywords__lit_layout_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"layout\")],{})", Factory.Production);
  private static final IConstructor prod__IterStarSep_Sym__lit___123_layouts_LAYOUTLIST_symbol_Sym_layouts_LAYOUTLIST_sep_Sym_layouts_LAYOUTLIST_lit___125_layouts_LAYOUTLIST_lit___42_ = (IConstructor) _read("prod(label(\"IterStarSep\",sort(\"Sym\")),[lit(\"{\"),layouts(\"LAYOUTLIST\"),label(\"symbol\",sort(\"Sym\")),layouts(\"LAYOUTLIST\"),label(\"sep\",sort(\"Sym\")),layouts(\"LAYOUTLIST\"),lit(\"}\"),layouts(\"LAYOUTLIST\"),lit(\"*\")],{})", Factory.Production);
  private static final IConstructor regular__iter_star_seps__Word__layouts_LAYOUTLIST = (IConstructor) _read("regular(\\iter-star-seps(lex(\"Word\"),[layouts(\"LAYOUTLIST\")]))", Factory.Production);
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
  private static final IConstructor prod__Rule_Declaration__tags_Tags_layouts_LAYOUTLIST_lit_rule_layouts_LAYOUTLIST_name_Name_layouts_LAYOUTLIST_patternAction_PatternWithAction_layouts_LAYOUTLIST_lit___59_ = (IConstructor) _read("prod(label(\"Rule\",sort(\"Declaration\")),[label(\"tags\",sort(\"Tags\")),layouts(\"LAYOUTLIST\"),lit(\"rule\"),layouts(\"LAYOUTLIST\"),label(\"name\",lex(\"Name\")),layouts(\"LAYOUTLIST\"),label(\"patternAction\",sort(\"PatternWithAction\")),layouts(\"LAYOUTLIST\"),lit(\";\")],{})", Factory.Production);
  private static final IConstructor prod__Default_LocationLiteral__protocolPart_ProtocolPart_layouts_LAYOUTLIST_pathPart_PathPart_ = (IConstructor) _read("prod(label(\"Default\",sort(\"LocationLiteral\")),[label(\"protocolPart\",sort(\"ProtocolPart\")),layouts(\"LAYOUTLIST\"),label(\"pathPart\",sort(\"PathPart\"))],{})", Factory.Production);
  private static final IConstructor prod__lit_top_down__char_class___range__116_116_char_class___range__111_111_char_class___range__112_112_char_class___range__45_45_char_class___range__100_100_char_class___range__111_111_char_class___range__119_119_char_class___range__110_110_ = (IConstructor) _read("prod(lit(\"top-down\"),[\\char-class([range(116,116)]),\\char-class([range(111,111)]),\\char-class([range(112,112)]),\\char-class([range(45,45)]),\\char-class([range(100,100)]),\\char-class([range(111,111)]),\\char-class([range(119,119)]),\\char-class([range(110,110)])],{})", Factory.Production);
  private static final IConstructor prod__lit___46_46_46__char_class___range__46_46_char_class___range__46_46_char_class___range__46_46_ = (IConstructor) _read("prod(lit(\"...\"),[\\char-class([range(46,46)]),\\char-class([range(46,46)]),\\char-class([range(46,46)])],{})", Factory.Production);
  private static final IConstructor prod__EndOfLine_Sym__symbol_Sym_layouts_LAYOUTLIST_lit___36_ = (IConstructor) _read("prod(label(\"EndOfLine\",sort(\"Sym\")),[label(\"symbol\",sort(\"Sym\")),layouts(\"LAYOUTLIST\"),lit(\"$\")],{})", Factory.Production);
  private static final IConstructor prod__lit_top_down_break__char_class___range__116_116_char_class___range__111_111_char_class___range__112_112_char_class___range__45_45_char_class___range__100_100_char_class___range__111_111_char_class___range__119_119_char_class___range__110_110_char_class___range__45_45_char_class___range__98_98_char_class___range__114_114_char_class___range__101_101_char_class___range__97_97_char_class___range__107_107_ = (IConstructor) _read("prod(lit(\"top-down-break\"),[\\char-class([range(116,116)]),\\char-class([range(111,111)]),\\char-class([range(112,112)]),\\char-class([range(45,45)]),\\char-class([range(100,100)]),\\char-class([range(111,111)]),\\char-class([range(119,119)]),\\char-class([range(110,110)]),\\char-class([range(45,45)]),\\char-class([range(98,98)]),\\char-class([range(114,114)]),\\char-class([range(101,101)]),\\char-class([range(97,97)]),\\char-class([range(107,107)])],{})", Factory.Production);
  private static final IConstructor prod__lit_view__char_class___range__118_118_char_class___range__105_105_char_class___range__101_101_char_class___range__119_119_ = (IConstructor) _read("prod(lit(\"view\"),[\\char-class([range(118,118)]),\\char-class([range(105,105)]),\\char-class([range(101,101)]),\\char-class([range(119,119)])],{})", Factory.Production);
  private static final IConstructor prod__lit_innermost__char_class___range__105_105_char_class___range__110_110_char_class___range__110_110_char_class___range__101_101_char_class___range__114_114_char_class___range__109_109_char_class___range__111_111_char_class___range__115_115_char_class___range__116_116_ = (IConstructor) _read("prod(lit(\"innermost\"),[\\char-class([range(105,105)]),\\char-class([range(110,110)]),\\char-class([range(110,110)]),\\char-class([range(101,101)]),\\char-class([range(114,114)]),\\char-class([range(109,109)]),\\char-class([range(111,111)]),\\char-class([range(115,115)]),\\char-class([range(116,116)])],{})", Factory.Production);
  private static final IConstructor prod__lit_one__char_class___range__111_111_char_class___range__110_110_char_class___range__101_101_ = (IConstructor) _read("prod(lit(\"one\"),[\\char-class([range(111,111)]),\\char-class([range(110,110)]),\\char-class([range(101,101)])],{})", Factory.Production);
  private static final IConstructor prod__SetAnnotation_Expression__expression_Expression_layouts_LAYOUTLIST_lit___91_layouts_LAYOUTLIST_lit___64_layouts_LAYOUTLIST_name_Name_layouts_LAYOUTLIST_lit___61_layouts_LAYOUTLIST_value_Expression_layouts_LAYOUTLIST_lit___93_ = (IConstructor) _read("prod(label(\"SetAnnotation\",sort(\"Expression\")),[label(\"expression\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"[\"),layouts(\"LAYOUTLIST\"),lit(\"@\"),layouts(\"LAYOUTLIST\"),label(\"name\",lex(\"Name\")),layouts(\"LAYOUTLIST\"),lit(\"=\"),layouts(\"LAYOUTLIST\"),label(\"value\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"]\")],{})", Factory.Production);
  private static final IConstructor prod__Empty_DataTarget__ = (IConstructor) _read("prod(label(\"Empty\",sort(\"DataTarget\")),[],{})", Factory.Production);
  private static final IConstructor prod__lit_reified__char_class___range__114_114_char_class___range__101_101_char_class___range__105_105_char_class___range__102_102_char_class___range__105_105_char_class___range__101_101_char_class___range__100_100_ = (IConstructor) _read("prod(lit(\"reified\"),[\\char-class([range(114,114)]),\\char-class([range(101,101)]),\\char-class([range(105,105)]),\\char-class([range(102,102)]),\\char-class([range(105,105)]),\\char-class([range(101,101)]),\\char-class([range(100,100)])],{})", Factory.Production);
  private static final IConstructor regular__iter_seps__Sym__layouts_LAYOUTLIST = (IConstructor) _read("regular(\\iter-seps(sort(\"Sym\"),[layouts(\"LAYOUTLIST\")]))", Factory.Production);
  private static final IConstructor prod__IfDefinedOtherwise_Expression__lhs_Expression_layouts_LAYOUTLIST_lit___63_layouts_LAYOUTLIST_rhs_Expression__assoc__non_assoc = (IConstructor) _read("prod(label(\"IfDefinedOtherwise\",sort(\"Expression\")),[label(\"lhs\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"?\"),layouts(\"LAYOUTLIST\"),label(\"rhs\",sort(\"Expression\"))],{assoc(\\non-assoc())})", Factory.Production);
  private static final IConstructor regular__iter_star_seps__Range__layouts_LAYOUTLIST = (IConstructor) _read("regular(\\iter-star-seps(sort(\"Range\"),[layouts(\"LAYOUTLIST\")]))", Factory.Production);
  private static final IConstructor prod__lit_while__char_class___range__119_119_char_class___range__104_104_char_class___range__105_105_char_class___range__108_108_char_class___range__101_101_ = (IConstructor) _read("prod(lit(\"while\"),[\\char-class([range(119,119)]),\\char-class([range(104,104)]),\\char-class([range(105,105)]),\\char-class([range(108,108)]),\\char-class([range(101,101)])],{})", Factory.Production);
  private static final IConstructor regular__iter_seps__TypeArg__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST = (IConstructor) _read("regular(\\iter-seps(sort(\"TypeArg\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")]))", Factory.Production);
  private static final IConstructor prod__lit_notin__char_class___range__110_110_char_class___range__111_111_char_class___range__116_116_char_class___range__105_105_char_class___range__110_110_ = (IConstructor) _read("prod(lit(\"notin\"),[\\char-class([range(110,110)]),\\char-class([range(111,111)]),\\char-class([range(116,116)]),\\char-class([range(105,105)]),\\char-class([range(110,110)])],{})", Factory.Production);
  private static final IConstructor regular__iter_seps__Renaming__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST = (IConstructor) _read("regular(\\iter-seps(sort(\"Renaming\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")]))", Factory.Production);
  private static final IConstructor regular__alt___seq___char_class___range__92_92_char_class___range__125_125_char_class___range__0_124_range__126_65535 = (IConstructor) _read("regular(alt({seq([\\char-class([range(92,92)]),\\char-class([range(125,125)])]),\\char-class([range(0,124),range(126,65535)])}))", Factory.Production);
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
  private static final IConstructor prod__Default_Tag__lit___64_layouts_LAYOUTLIST_name_Name_layouts_LAYOUTLIST_contents_TagString__tag__Foldable_tag__category___67_111_109_109_101_110_116 = (IConstructor) _read("prod(label(\"Default\",sort(\"Tag\")),[lit(\"@\"),layouts(\"LAYOUTLIST\"),label(\"name\",lex(\"Name\")),layouts(\"LAYOUTLIST\"),label(\"contents\",lex(\"TagString\"))],{tag(Foldable()),tag(category(\"Comment\"))})", Factory.Production);
  private static final IConstructor prod__Language_SyntaxDefinition__start_Start_layouts_LAYOUTLIST_lit_syntax_layouts_LAYOUTLIST_defined_Sym_layouts_LAYOUTLIST_lit___61_layouts_LAYOUTLIST_production_Prod_layouts_LAYOUTLIST_lit___59__tag__Foldable = (IConstructor) _read("prod(label(\"Language\",sort(\"SyntaxDefinition\")),[label(\"start\",sort(\"Start\")),layouts(\"LAYOUTLIST\"),lit(\"syntax\"),layouts(\"LAYOUTLIST\"),label(\"defined\",sort(\"Sym\")),layouts(\"LAYOUTLIST\"),lit(\"=\"),layouts(\"LAYOUTLIST\"),label(\"production\",sort(\"Prod\")),layouts(\"LAYOUTLIST\"),lit(\";\")],{tag(Foldable())})", Factory.Production);
  private static final IConstructor prod__Default_Import__lit_import_layouts_LAYOUTLIST_module_ImportedModule_layouts_LAYOUTLIST_lit___59_ = (IConstructor) _read("prod(label(\"Default\",sort(\"Import\")),[lit(\"import\"),layouts(\"LAYOUTLIST\"),label(\"module\",sort(\"ImportedModule\")),layouts(\"LAYOUTLIST\"),lit(\";\")],{})", Factory.Production);
  private static final IConstructor prod__Switch_Statement__label_Label_layouts_LAYOUTLIST_lit_switch_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_expression_Expression_layouts_LAYOUTLIST_lit___41_layouts_LAYOUTLIST_lit___123_layouts_LAYOUTLIST_cases_iter_seps__Case__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_ = (IConstructor) _read("prod(label(\"Switch\",sort(\"Statement\")),[label(\"label\",sort(\"Label\")),layouts(\"LAYOUTLIST\"),lit(\"switch\"),layouts(\"LAYOUTLIST\"),lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"expression\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\")\"),layouts(\"LAYOUTLIST\"),lit(\"{\"),layouts(\"LAYOUTLIST\"),label(\"cases\",\\iter-seps(sort(\"Case\"),[layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\"}\")],{})", Factory.Production);
  private static final IConstructor prod__ReifiedNonTerminal_BasicType__lit_non_terminal_ = (IConstructor) _read("prod(label(\"ReifiedNonTerminal\",sort(\"BasicType\")),[lit(\"non-terminal\")],{})", Factory.Production);
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
  private static final IConstructor prod__ReifiedType_BasicType__lit_type_ = (IConstructor) _read("prod(label(\"ReifiedType\",sort(\"BasicType\")),[lit(\"type\")],{})", Factory.Production);
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
  private static final IConstructor prod__lit_rule__char_class___range__114_114_char_class___range__117_117_char_class___range__108_108_char_class___range__101_101_ = (IConstructor) _read("prod(lit(\"rule\"),[\\char-class([range(114,114)]),\\char-class([range(117,117)]),\\char-class([range(108,108)]),\\char-class([range(101,101)])],{})", Factory.Production);
  private static final IConstructor prod__lit___60_61_61_62__char_class___range__60_60_char_class___range__61_61_char_class___range__61_61_char_class___range__62_62_ = (IConstructor) _read("prod(lit(\"\\<==\\>\"),[\\char-class([range(60,60)]),\\char-class([range(61,61)]),\\char-class([range(61,61)]),\\char-class([range(62,62)])],{})", Factory.Production);
  private static final IConstructor prod__StringConstant__lit___34_iter_star__StringCharacter_lit___34__tag__category___67_111_110_115_116_97_110_116 = (IConstructor) _read("prod(lex(\"StringConstant\"),[lit(\"\\\"\"),\\iter-star(lex(\"StringCharacter\")),lit(\"\\\"\")],{tag(category(\"Constant\"))})", Factory.Production);
  private static final IConstructor regular__iter_star_seps__Mapping__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST = (IConstructor) _read("regular(\\iter-star-seps(\\parameterized-sort(\"Mapping\",[sort(\"Expression\")]),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")]))", Factory.Production);
  private static final IConstructor regular__iter__char_class___range__0_8_range__11_12_range__14_31_range__33_65535 = (IConstructor) _read("regular(iter(\\char-class([range(0,8),range(11,12),range(14,31),range(33,65535)])))", Factory.Production);
  private static final IConstructor prod__lit_str__char_class___range__115_115_char_class___range__116_116_char_class___range__114_114_ = (IConstructor) _read("prod(lit(\"str\"),[\\char-class([range(115,115)]),\\char-class([range(116,116)]),\\char-class([range(114,114)])],{})", Factory.Production);
  private static final IConstructor prod__layouts_$QUOTES__conditional__iter_star__char_class___range__9_10_range__13_13_range__32_32__not_follow__char_class___range__9_10_range__13_13_range__32_32_ = (IConstructor) _read("prod(layouts(\"$QUOTES\"),[conditional(\\iter-star(\\char-class([range(9,10),range(13,13),range(32,32)])),{\\not-follow(\\char-class([range(9,10),range(13,13),range(32,32)]))})],{})", Factory.Production);
  private static final IConstructor regular__iter_seps__Field__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST = (IConstructor) _read("regular(\\iter-seps(sort(\"Field\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")]))", Factory.Production);
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
  private static final IConstructor prod__Assignment_Statement__assignable_Assignable_layouts_LAYOUTLIST_operator_Assignment_layouts_LAYOUTLIST_statement_Statement_ = (IConstructor) _read("prod(label(\"Assignment\",sort(\"Statement\")),[label(\"assignable\",sort(\"Assignable\")),layouts(\"LAYOUTLIST\"),label(\"operator\",sort(\"Assignment\")),layouts(\"LAYOUTLIST\"),label(\"statement\",sort(\"Statement\"))],{})", Factory.Production);
  private static final IConstructor prod__RascalKeywords__lit_rule_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"rule\")],{})", Factory.Production);
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
  private static final IConstructor prod__DateAndTime__lit___36_DatePart_lit_T_TimePartNoTZ_opt__TimeZonePart_ = (IConstructor) _read("prod(lex(\"DateAndTime\"),[lit(\"$\"),lex(\"DatePart\"),lit(\"T\"),lex(\"TimePartNoTZ\"),opt(lex(\"TimeZonePart\"))],{})", Factory.Production);
  private static final IConstructor prod__OctalEscapeSequence__lit___92_char_class___range__48_55_conditional__char_class___range__48_55__not_follow__char_class___range__48_55_ = (IConstructor) _read("prod(lex(\"OctalEscapeSequence\"),[lit(\"\\\\\"),\\char-class([range(48,55)]),conditional(\\char-class([range(48,55)]),{\\not-follow(\\char-class([range(48,55)]))})],{})", Factory.Production);
  private static final IConstructor prod__lit_non_assoc__char_class___range__110_110_char_class___range__111_111_char_class___range__110_110_char_class___range__45_45_char_class___range__97_97_char_class___range__115_115_char_class___range__115_115_char_class___range__111_111_char_class___range__99_99_ = (IConstructor) _read("prod(lit(\"non-assoc\"),[\\char-class([range(110,110)]),\\char-class([range(111,111)]),\\char-class([range(110,110)]),\\char-class([range(45,45)]),\\char-class([range(97,97)]),\\char-class([range(115,115)]),\\char-class([range(115,115)]),\\char-class([range(111,111)]),\\char-class([range(99,99)])],{})", Factory.Production);
  private static final IConstructor prod__Syntax_Import__syntax_SyntaxDefinition_ = (IConstructor) _read("prod(label(\"Syntax\",sort(\"Import\")),[label(\"syntax\",sort(\"SyntaxDefinition\"))],{})", Factory.Production);
  private static final IConstructor prod__lit_private__char_class___range__112_112_char_class___range__114_114_char_class___range__105_105_char_class___range__118_118_char_class___range__97_97_char_class___range__116_116_char_class___range__101_101_ = (IConstructor) _read("prod(lit(\"private\"),[\\char-class([range(112,112)]),\\char-class([range(114,114)]),\\char-class([range(105,105)]),\\char-class([range(118,118)]),\\char-class([range(97,97)]),\\char-class([range(116,116)]),\\char-class([range(101,101)])],{})", Factory.Production);
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
  private static final IConstructor regular__iter_star__alt___conditional__char_class___range__42_42__not_follow__char_class___range__47_47_char_class___range__0_41_range__43_65535 = (IConstructor) _read("regular(\\iter-star(alt({conditional(\\char-class([range(42,42)]),{\\not-follow(\\char-class([range(47,47)]))}),\\char-class([range(0,41),range(43,65535)])})))", Factory.Production);
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
  private static final IConstructor prod__Comment__lit___47_42_iter_star__alt___conditional__char_class___range__42_42__not_follow__char_class___range__47_47_char_class___range__0_41_range__43_65535_lit___42_47__tag__category___67_111_109_109_101_110_116 = (IConstructor) _read("prod(lex(\"Comment\"),[lit(\"/*\"),\\iter-star(alt({conditional(\\char-class([range(42,42)]),{\\not-follow(\\char-class([range(47,47)]))}),\\char-class([range(0,41),range(43,65535)])})),lit(\"*/\")],{tag(category(\"Comment\"))})", Factory.Production);
  private static final IConstructor prod__lit_true__char_class___range__116_116_char_class___range__114_114_char_class___range__117_117_char_class___range__101_101_ = (IConstructor) _read("prod(lit(\"true\"),[\\char-class([range(116,116)]),\\char-class([range(114,114)]),\\char-class([range(117,117)]),\\char-class([range(101,101)])],{})", Factory.Production);
  private static final IConstructor prod__History_ShellCommand__lit_history_ = (IConstructor) _read("prod(label(\"History\",sort(\"ShellCommand\")),[lit(\"history\")],{})", Factory.Production);
  private static final IConstructor prod__TopDownBreak_Strategy__lit_top_down_break_ = (IConstructor) _read("prod(label(\"TopDownBreak\",sort(\"Strategy\")),[lit(\"top-down-break\")],{})", Factory.Production);
  private static final IConstructor prod__start__Command__layouts_LAYOUTLIST_top_Command_layouts_LAYOUTLIST_ = (IConstructor) _read("prod(start(sort(\"Command\")),[layouts(\"LAYOUTLIST\"),label(\"top\",sort(\"Command\")),layouts(\"LAYOUTLIST\")],{})", Factory.Production);
  private static final IConstructor prod__HeaderKeyword__lit_syntax_ = (IConstructor) _read("prod(keywords(\"HeaderKeyword\"),[lit(\"syntax\")],{})", Factory.Production);
  private static final IConstructor prod__DateTime_BasicType__lit_datetime_ = (IConstructor) _read("prod(label(\"DateTime\",sort(\"BasicType\")),[lit(\"datetime\")],{})", Factory.Production);
  private static final IConstructor prod__lit_extend__char_class___range__101_101_char_class___range__120_120_char_class___range__116_116_char_class___range__101_101_char_class___range__110_110_char_class___range__100_100_ = (IConstructor) _read("prod(lit(\"extend\"),[\\char-class([range(101,101)]),\\char-class([range(120,120)]),\\char-class([range(116,116)]),\\char-class([range(101,101)]),\\char-class([range(110,110)]),\\char-class([range(100,100)])],{})", Factory.Production);
  private static final IConstructor prod__lit_append__char_class___range__97_97_char_class___range__112_112_char_class___range__112_112_char_class___range__101_101_char_class___range__110_110_char_class___range__100_100_ = (IConstructor) _read("prod(lit(\"append\"),[\\char-class([range(97,97)]),\\char-class([range(112,112)]),\\char-class([range(112,112)]),\\char-class([range(101,101)]),\\char-class([range(110,110)]),\\char-class([range(100,100)])],{})", Factory.Production);
  private static final IConstructor prod__lit_constructor__char_class___range__99_99_char_class___range__111_111_char_class___range__110_110_char_class___range__115_115_char_class___range__116_116_char_class___range__114_114_char_class___range__117_117_char_class___range__99_99_char_class___range__116_116_char_class___range__111_111_char_class___range__114_114_ = (IConstructor) _read("prod(lit(\"constructor\"),[\\char-class([range(99,99)]),\\char-class([range(111,111)]),\\char-class([range(110,110)]),\\char-class([range(115,115)]),\\char-class([range(116,116)]),\\char-class([range(114,114)]),\\char-class([range(117,117)]),\\char-class([range(99,99)]),\\char-class([range(116,116)]),\\char-class([range(111,111)]),\\char-class([range(114,114)])],{})", Factory.Production);
  private static final IConstructor prod__Extend_Import__lit_extend_layouts_LAYOUTLIST_module_ImportedModule_layouts_LAYOUTLIST_lit___59_ = (IConstructor) _read("prod(label(\"Extend\",sort(\"Import\")),[lit(\"extend\"),layouts(\"LAYOUTLIST\"),label(\"module\",sort(\"ImportedModule\")),layouts(\"LAYOUTLIST\"),lit(\";\")],{})", Factory.Production);
  private static final IConstructor prod__Alias_Kind__lit_alias_ = (IConstructor) _read("prod(label(\"Alias\",sort(\"Kind\")),[lit(\"alias\")],{})", Factory.Production);
  private static final IConstructor prod__All_Expression__lit_all_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_generators_iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_ = (IConstructor) _read("prod(label(\"All\",sort(\"Expression\")),[lit(\"all\"),layouts(\"LAYOUTLIST\"),lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"generators\",\\iter-seps(sort(\"Expression\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\")\")],{})", Factory.Production);
  private static final IConstructor prod__Default_Mapping__Pattern__from_Pattern_layouts_LAYOUTLIST_lit___58_layouts_LAYOUTLIST_to_Pattern_ = (IConstructor) _read("prod(label(\"Default\",\\parameterized-sort(\"Mapping\",[sort(\"Pattern\")])),[label(\"from\",sort(\"Pattern\")),layouts(\"LAYOUTLIST\"),lit(\":\"),layouts(\"LAYOUTLIST\"),label(\"to\",sort(\"Pattern\"))],{})", Factory.Production);
  private static final IConstructor prod__lit_assoc__char_class___range__97_97_char_class___range__115_115_char_class___range__115_115_char_class___range__111_111_char_class___range__99_99_ = (IConstructor) _read("prod(lit(\"assoc\"),[\\char-class([range(97,97)]),\\char-class([range(115,115)]),\\char-class([range(115,115)]),\\char-class([range(111,111)]),\\char-class([range(99,99)])],{})", Factory.Production);
  private static final IConstructor prod__Continue_Statement__lit_continue_layouts_LAYOUTLIST_target_Target_layouts_LAYOUTLIST_lit___59_ = (IConstructor) _read("prod(label(\"Continue\",sort(\"Statement\")),[lit(\"continue\"),layouts(\"LAYOUTLIST\"),label(\"target\",sort(\"Target\")),layouts(\"LAYOUTLIST\"),lit(\";\")],{})", Factory.Production);
  private static final IConstructor prod__Tag_Kind__lit_tag_ = (IConstructor) _read("prod(label(\"Tag\",sort(\"Kind\")),[lit(\"tag\")],{})", Factory.Production);
  private static final IConstructor prod__lit_map__char_class___range__109_109_char_class___range__97_97_char_class___range__112_112_ = (IConstructor) _read("prod(lit(\"map\"),[\\char-class([range(109,109)]),\\char-class([range(97,97)]),\\char-class([range(112,112)])],{})", Factory.Production);
  private static final IConstructor prod__lit_real__char_class___range__114_114_char_class___range__101_101_char_class___range__97_97_char_class___range__108_108_ = (IConstructor) _read("prod(lit(\"real\"),[\\char-class([range(114,114)]),\\char-class([range(101,101)]),\\char-class([range(97,97)]),\\char-class([range(108,108)])],{})", Factory.Production);
  private static final IConstructor regular__iter_star__char_class___range__45_45_range__48_57_range__65_90_range__95_95_range__97_122 = (IConstructor) _read("regular(\\iter-star(\\char-class([range(45,45),range(48,57),range(65,90),range(95,95),range(97,122)])))", Factory.Production);
  private static final IConstructor regular__iter_star_seps__Mapping__Pattern__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST = (IConstructor) _read("regular(\\iter-star-seps(\\parameterized-sort(\"Mapping\",[sort(\"Pattern\")]),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")]))", Factory.Production);
  private static final IConstructor prod__ReifiedFunction_BasicType__lit_fun_ = (IConstructor) _read("prod(label(\"ReifiedFunction\",sort(\"BasicType\")),[lit(\"fun\")],{})", Factory.Production);
  private static final IConstructor prod__Division_Assignment__lit___47_61_ = (IConstructor) _read("prod(label(\"Division\",sort(\"Assignment\")),[lit(\"/=\")],{})", Factory.Production);
  private static final IConstructor prod__Product_Assignment__lit___42_61_ = (IConstructor) _read("prod(label(\"Product\",sort(\"Assignment\")),[lit(\"*=\")],{})", Factory.Production);
  private static final IConstructor prod__PostStringChars__char_class___range__62_62_iter_star__StringCharacter_char_class___range__34_34__tag__category___67_111_110_115_116_97_110_116 = (IConstructor) _read("prod(lex(\"PostStringChars\"),[\\char-class([range(62,62)]),\\iter-star(lex(\"StringCharacter\")),\\char-class([range(34,34)])],{tag(category(\"Constant\"))})", Factory.Production);
  private static final IConstructor prod__lit_void__char_class___range__118_118_char_class___range__111_111_char_class___range__105_105_char_class___range__100_100_ = (IConstructor) _read("prod(lit(\"void\"),[\\char-class([range(118,118)]),\\char-class([range(111,111)]),\\char-class([range(105,105)]),\\char-class([range(100,100)])],{})", Factory.Production);
  private static final IConstructor prod__FromTo_Range__start_Char_layouts_LAYOUTLIST_lit___layouts_LAYOUTLIST_end_Char_ = (IConstructor) _read("prod(label(\"FromTo\",sort(\"Range\")),[label(\"start\",lex(\"Char\")),layouts(\"LAYOUTLIST\"),lit(\"-\"),layouts(\"LAYOUTLIST\"),label(\"end\",lex(\"Char\"))],{})", Factory.Production);
  private static final IConstructor prod__TimeZonePart__char_class___range__43_43_range__45_45_char_class___range__48_49_char_class___range__48_57_ = (IConstructor) _read("prod(lex(\"TimeZonePart\"),[\\char-class([range(43,43),range(45,45)]),\\char-class([range(48,49)]),\\char-class([range(48,57)])],{})", Factory.Production);
  private static final IConstructor prod__Map_Comprehension__lit___40_layouts_LAYOUTLIST_from_Expression_layouts_LAYOUTLIST_lit___58_layouts_LAYOUTLIST_to_Expression_layouts_LAYOUTLIST_lit___124_layouts_LAYOUTLIST_generators_iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_ = (IConstructor) _read("prod(label(\"Map\",sort(\"Comprehension\")),[lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"from\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\":\"),layouts(\"LAYOUTLIST\"),label(\"to\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"|\"),layouts(\"LAYOUTLIST\"),label(\"generators\",\\iter-seps(sort(\"Expression\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\")\")],{})", Factory.Production);
  private static final IConstructor prod__Shell_Command__lit___58_layouts_LAYOUTLIST_command_ShellCommand_ = (IConstructor) _read("prod(label(\"Shell\",sort(\"Command\")),[lit(\":\"),layouts(\"LAYOUTLIST\"),label(\"command\",sort(\"ShellCommand\"))],{})", Factory.Production);
  private static final IConstructor prod__Quit_ShellCommand__lit_quit_ = (IConstructor) _read("prod(label(\"Quit\",sort(\"ShellCommand\")),[lit(\"quit\")],{})", Factory.Production);
  private static final IConstructor prod__empty__ = (IConstructor) _read("prod(empty(),[],{})", Factory.Production);
  private static final IConstructor prod__IfThenElse_Expression__condition_Expression_layouts_LAYOUTLIST_lit___63_layouts_LAYOUTLIST_thenExp_Expression_layouts_LAYOUTLIST_lit___58_layouts_LAYOUTLIST_elseExp_Expression__assoc__left = (IConstructor) _read("prod(label(\"IfThenElse\",sort(\"Expression\")),[label(\"condition\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"?\"),layouts(\"LAYOUTLIST\"),label(\"thenExp\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\":\"),layouts(\"LAYOUTLIST\"),label(\"elseExp\",sort(\"Expression\"))],{assoc(left())})", Factory.Production);
  private static final IConstructor prod__DateAndTimeLiteral_DateTimeLiteral__dateAndTime_DateAndTime_ = (IConstructor) _read("prod(label(\"DateAndTimeLiteral\",sort(\"DateTimeLiteral\")),[label(\"dateAndTime\",lex(\"DateAndTime\"))],{})", Factory.Production);
  private static final IConstructor prod__Negative_Expression__lit___layouts_LAYOUTLIST_argument_Expression_ = (IConstructor) _read("prod(label(\"Negative\",sort(\"Expression\")),[lit(\"-\"),layouts(\"LAYOUTLIST\"),label(\"argument\",sort(\"Expression\"))],{})", Factory.Production);
  private static final IConstructor prod__TopDown_Strategy__lit_top_down_ = (IConstructor) _read("prod(label(\"TopDown\",sort(\"Strategy\")),[lit(\"top-down\")],{})", Factory.Production);
  private static final IConstructor prod__lit_switch__char_class___range__115_115_char_class___range__119_119_char_class___range__105_105_char_class___range__116_116_char_class___range__99_99_char_class___range__104_104_ = (IConstructor) _read("prod(lit(\"switch\"),[\\char-class([range(115,115)]),\\char-class([range(119,119)]),\\char-class([range(105,105)]),\\char-class([range(116,116)]),\\char-class([range(99,99)]),\\char-class([range(104,104)])],{})", Factory.Production);
  private static final IConstructor prod__DefaultStrategy_Visit__lit_visit_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_subject_Expression_layouts_LAYOUTLIST_lit___41_layouts_LAYOUTLIST_lit___123_layouts_LAYOUTLIST_cases_iter_seps__Case__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_ = (IConstructor) _read("prod(label(\"DefaultStrategy\",sort(\"Visit\")),[lit(\"visit\"),layouts(\"LAYOUTLIST\"),lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"subject\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\")\"),layouts(\"LAYOUTLIST\"),lit(\"{\"),layouts(\"LAYOUTLIST\"),label(\"cases\",\\iter-seps(sort(\"Case\"),[layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\"}\")],{})", Factory.Production);
  private static final IConstructor prod__Default_Parameters__lit___40_layouts_LAYOUTLIST_formals_Formals_layouts_LAYOUTLIST_lit___41_ = (IConstructor) _read("prod(label(\"Default\",sort(\"Parameters\")),[lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"formals\",sort(\"Formals\")),layouts(\"LAYOUTLIST\"),lit(\")\")],{})", Factory.Production);
  private static final IConstructor prod__lit_solve__char_class___range__115_115_char_class___range__111_111_char_class___range__108_108_char_class___range__118_118_char_class___range__101_101_ = (IConstructor) _read("prod(lit(\"solve\"),[\\char-class([range(115,115)]),\\char-class([range(111,111)]),\\char-class([range(108,108)]),\\char-class([range(118,118)]),\\char-class([range(101,101)])],{})", Factory.Production);
  private static final IConstructor prod__lit_fun__char_class___range__102_102_char_class___range__117_117_char_class___range__110_110_ = (IConstructor) _read("prod(lit(\"fun\"),[\\char-class([range(102,102)]),\\char-class([range(117,117)]),\\char-class([range(110,110)])],{})", Factory.Production);
  private static final IConstructor prod__Lexical_SyntaxDefinition__lit_lexical_layouts_LAYOUTLIST_defined_Sym_layouts_LAYOUTLIST_lit___61_layouts_LAYOUTLIST_production_Prod_layouts_LAYOUTLIST_lit___59__tag__Foldable = (IConstructor) _read("prod(label(\"Lexical\",sort(\"SyntaxDefinition\")),[lit(\"lexical\"),layouts(\"LAYOUTLIST\"),label(\"defined\",sort(\"Sym\")),layouts(\"LAYOUTLIST\"),lit(\"=\"),layouts(\"LAYOUTLIST\"),label(\"production\",sort(\"Prod\")),layouts(\"LAYOUTLIST\"),lit(\";\")],{tag(Foldable())})", Factory.Production);
  private static final IConstructor prod__lit_layout__char_class___range__108_108_char_class___range__97_97_char_class___range__121_121_char_class___range__111_111_char_class___range__117_117_char_class___range__116_116_ = (IConstructor) _read("prod(lit(\"layout\"),[\\char-class([range(108,108)]),\\char-class([range(97,97)]),\\char-class([range(121,121)]),\\char-class([range(111,111)]),\\char-class([range(117,117)]),\\char-class([range(116,116)])],{})", Factory.Production);
  private static final IConstructor regular__seq___char_class___range__48_57_opt__char_class___range__48_57 = (IConstructor) _read("regular(seq([\\char-class([range(48,57)]),opt(\\char-class([range(48,57)]))]))", Factory.Production);
  private static final IConstructor prod__List_FunctionModifiers__modifiers_iter_star_seps__FunctionModifier__layouts_LAYOUTLIST_ = (IConstructor) _read("prod(label(\"List\",sort(\"FunctionModifiers\")),[label(\"modifiers\",\\iter-star-seps(sort(\"FunctionModifier\"),[layouts(\"LAYOUTLIST\")]))],{})", Factory.Production);
  private static final IConstructor prod__NonInterpolated_StringLiteral__constant_StringConstant_ = (IConstructor) _read("prod(label(\"NonInterpolated\",sort(\"StringLiteral\")),[label(\"constant\",lex(\"StringConstant\"))],{})", Factory.Production);
  private static final IConstructor prod__lit_non_terminal__char_class___range__110_110_char_class___range__111_111_char_class___range__110_110_char_class___range__45_45_char_class___range__116_116_char_class___range__101_101_char_class___range__114_114_char_class___range__109_109_char_class___range__105_105_char_class___range__110_110_char_class___range__97_97_char_class___range__108_108_ = (IConstructor) _read("prod(lit(\"non-terminal\"),[\\char-class([range(110,110)]),\\char-class([range(111,111)]),\\char-class([range(110,110)]),\\char-class([range(45,45)]),\\char-class([range(116,116)]),\\char-class([range(101,101)]),\\char-class([range(114,114)]),\\char-class([range(109,109)]),\\char-class([range(105,105)]),\\char-class([range(110,110)]),\\char-class([range(97,97)]),\\char-class([range(108,108)])],{})", Factory.Production);
  private static final IConstructor prod__Division_Expression__lhs_Expression_layouts_LAYOUTLIST_lit___47_layouts_LAYOUTLIST_rhs_Expression__assoc__left = (IConstructor) _read("prod(label(\"Division\",sort(\"Expression\")),[label(\"lhs\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"/\"),layouts(\"LAYOUTLIST\"),label(\"rhs\",sort(\"Expression\"))],{assoc(left())})", Factory.Production);
  private static final IConstructor prod__Product_Expression__lhs_Expression_layouts_LAYOUTLIST_lit___42_layouts_LAYOUTLIST_rhs_Expression__assoc__left = (IConstructor) _read("prod(label(\"Product\",sort(\"Expression\")),[label(\"lhs\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"*\"),layouts(\"LAYOUTLIST\"),label(\"rhs\",sort(\"Expression\"))],{assoc(left())})", Factory.Production);
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
  private static final IConstructor prod__RascalKeywords__lit_fun_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"fun\")],{})", Factory.Production);
  private static final IConstructor prod__RascalKeywords__lit_if_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"if\")],{})", Factory.Production);
  private static final IConstructor prod__Loc_BasicType__lit_loc_ = (IConstructor) _read("prod(label(\"Loc\",sort(\"BasicType\")),[lit(\"loc\")],{})", Factory.Production);
  private static final IConstructor prod__Num_BasicType__lit_num_ = (IConstructor) _read("prod(label(\"Num\",sort(\"BasicType\")),[lit(\"num\")],{})", Factory.Production);
  private static final IConstructor prod__Mid_PathTail__mid_MidPathChars_layouts_LAYOUTLIST_expression_Expression_layouts_LAYOUTLIST_tail_PathTail_ = (IConstructor) _read("prod(label(\"Mid\",sort(\"PathTail\")),[label(\"mid\",lex(\"MidPathChars\")),layouts(\"LAYOUTLIST\"),label(\"expression\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),label(\"tail\",sort(\"PathTail\"))],{})", Factory.Production);
  private static final IConstructor prod__RascalKeywords__lit_it_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"it\")],{})", Factory.Production);
  private static final IConstructor prod__RascalKeywords__lit_in_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"in\")],{})", Factory.Production);
  private static final IConstructor prod__RascalKeywords__lit_else_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"else\")],{})", Factory.Production);
  private static final IConstructor prod__RascalKeywords__lit_for_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"for\")],{})", Factory.Production);
  private static final IConstructor prod__StringCharacter__char_class___range__0_33_range__35_38_range__40_59_range__61_61_range__63_91_range__93_65535_ = (IConstructor) _read("prod(lex(\"StringCharacter\"),[\\char-class([range(0,33),range(35,38),range(40,59),range(61,61),range(63,91),range(93,65535)])],{})", Factory.Production);
  private static final IConstructor prod__lit_lexical__char_class___range__108_108_char_class___range__101_101_char_class___range__120_120_char_class___range__105_105_char_class___range__99_99_char_class___range__97_97_char_class___range__108_108_ = (IConstructor) _read("prod(lit(\"lexical\"),[\\char-class([range(108,108)]),\\char-class([range(101,101)]),\\char-class([range(120,120)]),\\char-class([range(105,105)]),\\char-class([range(99,99)]),\\char-class([range(97,97)]),\\char-class([range(108,108)])],{})", Factory.Production);
  private static final IConstructor prod__Tuple_Expression__lit___60_layouts_LAYOUTLIST_elements_iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___62_ = (IConstructor) _read("prod(label(\"Tuple\",sort(\"Expression\")),[lit(\"\\<\"),layouts(\"LAYOUTLIST\"),label(\"elements\",\\iter-seps(sort(\"Expression\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\"\\>\")],{})", Factory.Production);
  private static final IConstructor prod__lit___60_45__char_class___range__60_60_char_class___range__45_45_ = (IConstructor) _read("prod(lit(\"\\<-\"),[\\char-class([range(60,60)]),\\char-class([range(45,45)])],{})", Factory.Production);
  private static final IConstructor prod__StepRange_Expression__lit___91_layouts_LAYOUTLIST_first_Expression_layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_second_Expression_layouts_LAYOUTLIST_lit___46_46_layouts_LAYOUTLIST_last_Expression_layouts_LAYOUTLIST_lit___93_ = (IConstructor) _read("prod(label(\"StepRange\",sort(\"Expression\")),[lit(\"[\"),layouts(\"LAYOUTLIST\"),label(\"first\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\"),label(\"second\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"..\"),layouts(\"LAYOUTLIST\"),label(\"last\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"]\")],{})", Factory.Production);
  private static final IConstructor prod__lit_filter__char_class___range__102_102_char_class___range__105_105_char_class___range__108_108_char_class___range__116_116_char_class___range__101_101_char_class___range__114_114_ = (IConstructor) _read("prod(lit(\"filter\"),[\\char-class([range(102,102)]),\\char-class([range(105,105)]),\\char-class([range(108,108)]),\\char-class([range(116,116)]),\\char-class([range(101,101)]),\\char-class([range(114,114)])],{})", Factory.Production);
  private static final IConstructor prod__MidPathChars__lit___62_URLChars_lit___60_ = (IConstructor) _read("prod(lex(\"MidPathChars\"),[lit(\"\\>\"),lex(\"URLChars\"),lit(\"\\<\")],{})", Factory.Production);
  private static final IConstructor prod__MidTemplate_StringTail__mid_MidStringChars_layouts_LAYOUTLIST_template_StringTemplate_layouts_LAYOUTLIST_tail_StringTail_ = (IConstructor) _read("prod(label(\"MidTemplate\",sort(\"StringTail\")),[label(\"mid\",lex(\"MidStringChars\")),layouts(\"LAYOUTLIST\"),label(\"template\",sort(\"StringTemplate\")),layouts(\"LAYOUTLIST\"),label(\"tail\",sort(\"StringTail\"))],{})", Factory.Production);
  private static final IConstructor prod__String_Literal__stringLiteral_StringLiteral_ = (IConstructor) _read("prod(label(\"String\",sort(\"Literal\")),[label(\"stringLiteral\",sort(\"StringLiteral\"))],{})", Factory.Production);
  private static final IConstructor prod__Alternative_Sym__lit___40_layouts_LAYOUTLIST_first_Sym_layouts_LAYOUTLIST_lit___124_layouts_LAYOUTLIST_alternatives_iter_seps__Sym__layouts_LAYOUTLIST_lit___124_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_ = (IConstructor) _read("prod(label(\"Alternative\",sort(\"Sym\")),[lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"first\",sort(\"Sym\")),layouts(\"LAYOUTLIST\"),lit(\"|\"),layouts(\"LAYOUTLIST\"),label(\"alternatives\",\\iter-seps(sort(\"Sym\"),[layouts(\"LAYOUTLIST\"),lit(\"|\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\")\")],{})", Factory.Production);
  private static final IConstructor prod__lit___58_58__char_class___range__58_58_char_class___range__58_58_ = (IConstructor) _read("prod(lit(\"::\"),[\\char-class([range(58,58)]),\\char-class([range(58,58)])],{})", Factory.Production);
  private static final IConstructor prod__lit_start__char_class___range__115_115_char_class___range__116_116_char_class___range__97_97_char_class___range__114_114_char_class___range__116_116_ = (IConstructor) _read("prod(lit(\"start\"),[\\char-class([range(115,115)]),\\char-class([range(116,116)]),\\char-class([range(97,97)]),\\char-class([range(114,114)]),\\char-class([range(116,116)])],{})", Factory.Production);
  private static final IConstructor prod__lit___58_61__char_class___range__58_58_char_class___range__61_61_ = (IConstructor) _read("prod(lit(\":=\"),[\\char-class([range(58,58)]),\\char-class([range(61,61)])],{})", Factory.Production);
  private static final IConstructor prod__NonEquals_Expression__lhs_Expression_layouts_LAYOUTLIST_lit___33_61_layouts_LAYOUTLIST_rhs_Expression__assoc__non_assoc = (IConstructor) _read("prod(label(\"NonEquals\",sort(\"Expression\")),[label(\"lhs\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"!=\"),layouts(\"LAYOUTLIST\"),label(\"rhs\",sort(\"Expression\"))],{assoc(\\non-assoc())})", Factory.Production);
  private static final IConstructor prod__TimePartNoTZ__char_class___range__48_50_char_class___range__48_57_char_class___range__48_53_char_class___range__48_57_char_class___range__48_53_char_class___range__48_57_opt__seq___char_class___range__44_44_range__46_46_char_class___range__48_57_opt__seq___char_class___range__48_57_opt__char_class___range__48_57_ = (IConstructor) _read("prod(lex(\"TimePartNoTZ\"),[\\char-class([range(48,50)]),\\char-class([range(48,57)]),\\char-class([range(48,53)]),\\char-class([range(48,57)]),\\char-class([range(48,53)]),\\char-class([range(48,57)]),opt(seq([\\char-class([range(44,44),range(46,46)]),\\char-class([range(48,57)]),opt(seq([\\char-class([range(48,57)]),opt(\\char-class([range(48,57)]))]))]))],{})", Factory.Production);
  private static final IConstructor prod__Dynamic_LocalVariableDeclaration__lit_dynamic_layouts_LAYOUTLIST_declarator_Declarator_ = (IConstructor) _read("prod(label(\"Dynamic\",sort(\"LocalVariableDeclaration\")),[lit(\"dynamic\"),layouts(\"LAYOUTLIST\"),label(\"declarator\",sort(\"Declarator\"))],{})", Factory.Production);
  private static final IConstructor regular__iter_star__alt___seq___char_class___range__92_92_char_class___range__125_125_char_class___range__0_124_range__126_65535 = (IConstructor) _read("regular(\\iter-star(alt({seq([\\char-class([range(92,92)]),\\char-class([range(125,125)])]),\\char-class([range(0,124),range(126,65535)])})))", Factory.Production);
  private static final IConstructor prod__lit_for__char_class___range__102_102_char_class___range__111_111_char_class___range__114_114_ = (IConstructor) _read("prod(lit(\"for\"),[\\char-class([range(102,102)]),\\char-class([range(111,111)]),\\char-class([range(114,114)])],{})", Factory.Production);
  private static final IConstructor prod__Replacing_PatternWithAction__pattern_Pattern_layouts_LAYOUTLIST_lit___61_62_layouts_LAYOUTLIST_replacement_Replacement_ = (IConstructor) _read("prod(label(\"Replacing\",sort(\"PatternWithAction\")),[label(\"pattern\",sort(\"Pattern\")),layouts(\"LAYOUTLIST\"),lit(\"=\\>\"),layouts(\"LAYOUTLIST\"),label(\"replacement\",sort(\"Replacement\"))],{})", Factory.Production);
  private static final IConstructor prod__UnicodeEscape__lit___92_iter__char_class___range__117_117_char_class___range__48_57_range__65_70_range__97_102_char_class___range__48_57_range__65_70_range__97_102_char_class___range__48_57_range__65_70_range__97_102_char_class___range__48_57_range__65_70_range__97_102_ = (IConstructor) _read("prod(lex(\"UnicodeEscape\"),[lit(\"\\\\\"),iter(\\char-class([range(117,117)])),\\char-class([range(48,57),range(65,70),range(97,102)]),\\char-class([range(48,57),range(65,70),range(97,102)]),\\char-class([range(48,57),range(65,70),range(97,102)]),\\char-class([range(48,57),range(65,70),range(97,102)])],{})", Factory.Production);
  private static final IConstructor prod__View_Kind__lit_view_ = (IConstructor) _read("prod(label(\"View\",sort(\"Kind\")),[lit(\"view\")],{})", Factory.Production);
  private static final IConstructor prod__Default_FunctionDeclaration__tags_Tags_layouts_LAYOUTLIST_visibility_Visibility_layouts_LAYOUTLIST_signature_Signature_layouts_LAYOUTLIST_body_FunctionBody__tag__Foldable = (IConstructor) _read("prod(label(\"Default\",sort(\"FunctionDeclaration\")),[label(\"tags\",sort(\"Tags\")),layouts(\"LAYOUTLIST\"),label(\"visibility\",sort(\"Visibility\")),layouts(\"LAYOUTLIST\"),label(\"signature\",sort(\"Signature\")),layouts(\"LAYOUTLIST\"),label(\"body\",sort(\"FunctionBody\"))],{tag(Foldable())})", Factory.Production);
  private static final IConstructor prod__Try_Statement__lit_try_layouts_LAYOUTLIST_body_Statement_layouts_LAYOUTLIST_handlers_iter_seps__Catch__layouts_LAYOUTLIST__assoc__non_assoc = (IConstructor) _read("prod(label(\"Try\",sort(\"Statement\")),[lit(\"try\"),layouts(\"LAYOUTLIST\"),label(\"body\",sort(\"Statement\")),layouts(\"LAYOUTLIST\"),label(\"handlers\",\\iter-seps(sort(\"Catch\"),[layouts(\"LAYOUTLIST\")]))],{assoc(\\non-assoc())})", Factory.Production);
  private static final IConstructor prod__IterStar_Sym__symbol_Sym_layouts_LAYOUTLIST_lit___42_ = (IConstructor) _read("prod(label(\"IterStar\",sort(\"Sym\")),[label(\"symbol\",sort(\"Sym\")),layouts(\"LAYOUTLIST\"),lit(\"*\")],{})", Factory.Production);
  private static final IConstructor prod__DatePart__char_class___range__48_57_char_class___range__48_57_char_class___range__48_57_char_class___range__48_57_lit___char_class___range__48_49_char_class___range__48_57_lit___char_class___range__48_51_char_class___range__48_57_ = (IConstructor) _read("prod(lex(\"DatePart\"),[\\char-class([range(48,57)]),\\char-class([range(48,57)]),\\char-class([range(48,57)]),\\char-class([range(48,57)]),lit(\"-\"),\\char-class([range(48,49)]),\\char-class([range(48,57)]),lit(\"-\"),\\char-class([range(48,51)]),\\char-class([range(48,57)])],{})", Factory.Production);
  private static final IConstructor prod__RegExp__char_class___range__60_60_expression_Expression_char_class___range__62_62__tag__category___77_101_116_97_86_97_114_105_97_98_108_101 = (IConstructor) _read("prod(lex(\"RegExp\"),[\\char-class([range(60,60)]),label(\"expression\",sort(\"Expression\")),\\char-class([range(62,62)])],{tag(category(\"MetaVariable\"))})", Factory.Production);
  private static final IConstructor prod__RascalKeywords__lit_switch_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"switch\")],{})", Factory.Production);
  private static final IConstructor prod__For_Statement__label_Label_layouts_LAYOUTLIST_lit_for_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_generators_iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_layouts_LAYOUTLIST_body_Statement_ = (IConstructor) _read("prod(label(\"For\",sort(\"Statement\")),[label(\"label\",sort(\"Label\")),layouts(\"LAYOUTLIST\"),lit(\"for\"),layouts(\"LAYOUTLIST\"),lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"generators\",\\iter-seps(sort(\"Expression\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\")\"),layouts(\"LAYOUTLIST\"),label(\"body\",sort(\"Statement\"))],{})", Factory.Production);
  private static final IConstructor prod__lit_left__char_class___range__108_108_char_class___range__101_101_char_class___range__102_102_char_class___range__116_116_ = (IConstructor) _read("prod(lit(\"left\"),[\\char-class([range(108,108)]),\\char-class([range(101,101)]),\\char-class([range(102,102)]),\\char-class([range(116,116)])],{})", Factory.Production);
  private static final IConstructor prod__All_Prod__lhs_Prod_layouts_LAYOUTLIST_lit___124_layouts_LAYOUTLIST_rhs_Prod__assoc__left = (IConstructor) _read("prod(label(\"All\",sort(\"Prod\")),[label(\"lhs\",sort(\"Prod\")),layouts(\"LAYOUTLIST\"),lit(\"|\"),layouts(\"LAYOUTLIST\"),label(\"rhs\",sort(\"Prod\"))],{assoc(left())})", Factory.Production);
  private static final IConstructor prod__RascalKeywords__lit_throws_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"throws\")],{})", Factory.Production);
  private static final IConstructor prod__Backslash__conditional__char_class___range__92_92__not_follow__char_class___range__47_47_range__60_60_range__62_62_range__92_92_ = (IConstructor) _read("prod(lex(\"Backslash\"),[conditional(\\char-class([range(92,92)]),{\\not-follow(\\char-class([range(47,47),range(60,60),range(62,62),range(92,92)]))})],{})", Factory.Production);
  private static final IConstructor regular__empty = (IConstructor) _read("regular(empty())", Factory.Production);
  private static final IConstructor regular__iter_star__char_class___range__0_9_range__11_65535 = (IConstructor) _read("regular(\\iter-star(\\char-class([range(0,9),range(11,65535)])))", Factory.Production);
  private static final IConstructor prod__PrePathChars__URLChars_lit___60_ = (IConstructor) _read("prod(lex(\"PrePathChars\"),[lex(\"URLChars\"),lit(\"\\<\")],{})", Factory.Production);
  private static final IConstructor prod__lit_bracket__char_class___range__98_98_char_class___range__114_114_char_class___range__97_97_char_class___range__99_99_char_class___range__107_107_char_class___range__101_101_char_class___range__116_116_ = (IConstructor) _read("prod(lit(\"bracket\"),[\\char-class([range(98,98)]),\\char-class([range(114,114)]),\\char-class([range(97,97)]),\\char-class([range(99,99)]),\\char-class([range(107,107)]),\\char-class([range(101,101)]),\\char-class([range(116,116)])],{})", Factory.Production);
  private static final IConstructor prod__DateTime_Literal__dateTimeLiteral_DateTimeLiteral_ = (IConstructor) _read("prod(label(\"DateTime\",sort(\"Literal\")),[label(\"dateTimeLiteral\",sort(\"DateTimeLiteral\"))],{})", Factory.Production);
  private static final IConstructor regular__iter_star__char_class___range__0_8_range__11_12_range__14_31_range__33_59_range__61_123_range__125_65535 = (IConstructor) _read("regular(\\iter-star(\\char-class([range(0,8),range(11,12),range(14,31),range(33,59),range(61,123),range(125,65535)])))", Factory.Production);
  private static final IConstructor prod__Parametrized_Sym__conditional__nonterminal_Nonterminal__follow__lit___91_layouts_LAYOUTLIST_lit___91_layouts_LAYOUTLIST_parameters_iter_seps__Sym__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___93_ = (IConstructor) _read("prod(label(\"Parametrized\",sort(\"Sym\")),[conditional(label(\"nonterminal\",lex(\"Nonterminal\")),{follow(lit(\"[\"))}),layouts(\"LAYOUTLIST\"),lit(\"[\"),layouts(\"LAYOUTLIST\"),label(\"parameters\",\\iter-seps(sort(\"Sym\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\"]\")],{})", Factory.Production);
  private static final IConstructor prod__TimeLiteral_DateTimeLiteral__time_JustTime_ = (IConstructor) _read("prod(label(\"TimeLiteral\",sort(\"DateTimeLiteral\")),[label(\"time\",lex(\"JustTime\"))],{})", Factory.Production);
  private static final IConstructor prod__TypedVariableBecomes_Pattern__type_Type_layouts_LAYOUTLIST_name_Name_layouts_LAYOUTLIST_lit___58_layouts_LAYOUTLIST_pattern_Pattern_ = (IConstructor) _read("prod(label(\"TypedVariableBecomes\",sort(\"Pattern\")),[label(\"type\",sort(\"Type\")),layouts(\"LAYOUTLIST\"),label(\"name\",lex(\"Name\")),layouts(\"LAYOUTLIST\"),lit(\":\"),layouts(\"LAYOUTLIST\"),label(\"pattern\",sort(\"Pattern\"))],{})", Factory.Production);
  private static final IConstructor prod__start__PreModule__layouts_LAYOUTLIST_top_PreModule_layouts_LAYOUTLIST_ = (IConstructor) _read("prod(start(sort(\"PreModule\")),[layouts(\"LAYOUTLIST\"),label(\"top\",sort(\"PreModule\")),layouts(\"LAYOUTLIST\")],{})", Factory.Production);
  private static final IConstructor prod__DateLiteral_DateTimeLiteral__date_JustDate_ = (IConstructor) _read("prod(label(\"DateLiteral\",sort(\"DateTimeLiteral\")),[label(\"date\",lex(\"JustDate\"))],{})", Factory.Production);
  private static final IConstructor prod__TimePartNoTZ__char_class___range__48_50_char_class___range__48_57_lit___58_char_class___range__48_53_char_class___range__48_57_lit___58_char_class___range__48_53_char_class___range__48_57_opt__seq___char_class___range__44_44_range__46_46_char_class___range__48_57_opt__seq___char_class___range__48_57_opt__char_class___range__48_57_ = (IConstructor) _read("prod(lex(\"TimePartNoTZ\"),[\\char-class([range(48,50)]),\\char-class([range(48,57)]),lit(\":\"),\\char-class([range(48,53)]),\\char-class([range(48,57)]),lit(\":\"),\\char-class([range(48,53)]),\\char-class([range(48,57)]),opt(seq([\\char-class([range(44,44),range(46,46)]),\\char-class([range(48,57)]),opt(seq([\\char-class([range(48,57)]),opt(\\char-class([range(48,57)]))]))]))],{})", Factory.Production);
  private static final IConstructor prod__Name_Field__fieldName_Name_ = (IConstructor) _read("prod(label(\"Name\",sort(\"Field\")),[label(\"fieldName\",lex(\"Name\"))],{})", Factory.Production);
  private static final IConstructor prod__StringCharacter__OctalEscapeSequence_ = (IConstructor) _read("prod(lex(\"StringCharacter\"),[lex(\"OctalEscapeSequence\")],{})", Factory.Production);
  private static final IConstructor prod__OctalEscapeSequence__lit___92_conditional__char_class___range__48_55__not_follow__char_class___range__48_55_ = (IConstructor) _read("prod(lex(\"OctalEscapeSequence\"),[lit(\"\\\\\"),conditional(\\char-class([range(48,55)]),{\\not-follow(\\char-class([range(48,55)]))})],{})", Factory.Production);
  private static final IConstructor prod__Bounded_TypeVar__lit___38_layouts_LAYOUTLIST_name_Name_layouts_LAYOUTLIST_lit___60_58_layouts_LAYOUTLIST_bound_Type_ = (IConstructor) _read("prod(label(\"Bounded\",sort(\"TypeVar\")),[lit(\"&\"),layouts(\"LAYOUTLIST\"),label(\"name\",lex(\"Name\")),layouts(\"LAYOUTLIST\"),lit(\"\\<:\"),layouts(\"LAYOUTLIST\"),label(\"bound\",sort(\"Type\"))],{})", Factory.Production);
  private static final IConstructor prod__layouts_$BACKTICKS__ = (IConstructor) _read("prod(layouts(\"$BACKTICKS\"),[],{})", Factory.Production);
  private static final IConstructor prod__Arbitrary_PatternWithAction__pattern_Pattern_layouts_LAYOUTLIST_lit___58_layouts_LAYOUTLIST_statement_Statement_ = (IConstructor) _read("prod(label(\"Arbitrary\",sort(\"PatternWithAction\")),[label(\"pattern\",sort(\"Pattern\")),layouts(\"LAYOUTLIST\"),lit(\":\"),layouts(\"LAYOUTLIST\"),label(\"statement\",sort(\"Statement\"))],{})", Factory.Production);
  private static final IConstructor prod__ReifiedTypeParameter_BasicType__lit_parameter_ = (IConstructor) _read("prod(label(\"ReifiedTypeParameter\",sort(\"BasicType\")),[lit(\"parameter\")],{})", Factory.Production);
  private static final IConstructor prod__ReifiedReifiedType_BasicType__lit_reified_ = (IConstructor) _read("prod(label(\"ReifiedReifiedType\",sort(\"BasicType\")),[lit(\"reified\")],{})", Factory.Production);
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
  private static final IConstructor prod__It_Expression__lit_it_ = (IConstructor) _read("prod(label(\"It\",sort(\"Expression\")),[lit(\"it\")],{})", Factory.Production);
  private static final IConstructor prod__HeaderKeyword__lit_keyword_ = (IConstructor) _read("prod(keywords(\"HeaderKeyword\"),[lit(\"keyword\")],{})", Factory.Production);
  private static final IConstructor prod__RascalKeywords__lit_list_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"list\")],{})", Factory.Production);
  private static final IConstructor prod__lit_undeclare__char_class___range__117_117_char_class___range__110_110_char_class___range__100_100_char_class___range__101_101_char_class___range__99_99_char_class___range__108_108_char_class___range__97_97_char_class___range__114_114_char_class___range__101_101_ = (IConstructor) _read("prod(lit(\"undeclare\"),[\\char-class([range(117,117)]),\\char-class([range(110,110)]),\\char-class([range(100,100)]),\\char-class([range(101,101)]),\\char-class([range(99,99)]),\\char-class([range(108,108)]),\\char-class([range(97,97)]),\\char-class([range(114,114)]),\\char-class([range(101,101)])],{})", Factory.Production);
  private static final IConstructor regular__opt__seq___char_class___range__44_44_range__46_46_char_class___range__48_57_opt__seq___char_class___range__48_57_opt__char_class___range__48_57 = (IConstructor) _read("regular(opt(seq([\\char-class([range(44,44),range(46,46)]),\\char-class([range(48,57)]),opt(seq([\\char-class([range(48,57)]),opt(\\char-class([range(48,57)]))]))])))", Factory.Production);
  private static final IConstructor regular__iter_star__StringCharacter = (IConstructor) _read("regular(\\iter-star(lex(\"StringCharacter\")))", Factory.Production);
  private static final IConstructor prod__Union_Class__lhs_Class_layouts_LAYOUTLIST_lit___124_124_layouts_LAYOUTLIST_rhs_Class__assoc__left = (IConstructor) _read("prod(label(\"Union\",sort(\"Class\")),[label(\"lhs\",sort(\"Class\")),layouts(\"LAYOUTLIST\"),lit(\"||\"),layouts(\"LAYOUTLIST\"),label(\"rhs\",sort(\"Class\"))],{assoc(left())})", Factory.Production);
  private static final IConstructor prod__StringCharacter__char_class___range__10_10_iter_star__char_class___range__9_9_range__32_32_char_class___range__39_39_ = (IConstructor) _read("prod(lex(\"StringCharacter\"),[\\char-class([range(10,10)]),\\iter-star(\\char-class([range(9,9),range(32,32)])),\\char-class([range(39,39)])],{})", Factory.Production);
  private static final IConstructor prod__Default_FunctionBody__lit___123_layouts_LAYOUTLIST_statements_iter_star_seps__Statement__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_ = (IConstructor) _read("prod(label(\"Default\",sort(\"FunctionBody\")),[lit(\"{\"),layouts(\"LAYOUTLIST\"),label(\"statements\",\\iter-star-seps(sort(\"Statement\"),[layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\"}\")],{})", Factory.Production);
  private static final IConstructor prod__Test_ShellCommand__lit_test_ = (IConstructor) _read("prod(label(\"Test\",sort(\"ShellCommand\")),[lit(\"test\")],{})", Factory.Production);
  private static final IConstructor prod__RascalKeywords__lit_tag_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"tag\")],{})", Factory.Production);
  private static final IConstructor prod__Annotation_Assignable__receiver_Assignable_layouts_LAYOUTLIST_lit___64_layouts_LAYOUTLIST_annotation_Name_ = (IConstructor) _read("prod(label(\"Annotation\",sort(\"Assignable\")),[label(\"receiver\",sort(\"Assignable\")),layouts(\"LAYOUTLIST\"),lit(\"@\"),layouts(\"LAYOUTLIST\"),label(\"annotation\",lex(\"Name\"))],{})", Factory.Production);
  private static final IConstructor prod__RascalKeywords__lit_extend_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"extend\")],{})", Factory.Production);
  private static final IConstructor prod__StringCharacter__UnicodeEscape_ = (IConstructor) _read("prod(lex(\"StringCharacter\"),[lex(\"UnicodeEscape\")],{})", Factory.Production);
  private static final IConstructor prod__Follow_Sym__symbol_Sym_layouts_LAYOUTLIST_lit___62_62_layouts_LAYOUTLIST_match_Sym__assoc__left = (IConstructor) _read("prod(label(\"Follow\",sort(\"Sym\")),[label(\"symbol\",sort(\"Sym\")),layouts(\"LAYOUTLIST\"),lit(\"\\>\\>\"),layouts(\"LAYOUTLIST\"),label(\"match\",sort(\"Sym\"))],{assoc(left())})", Factory.Production);
  private static final IConstructor prod__URLChars__iter_star__char_class___range__0_8_range__11_12_range__14_31_range__33_59_range__61_123_range__125_65535_ = (IConstructor) _read("prod(lex(\"URLChars\"),[\\iter-star(\\char-class([range(0,8),range(11,12),range(14,31),range(33,59),range(61,123),range(125,65535)]))],{})", Factory.Production);
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
  private static final IConstructor prod__Comment__lit___47_47_iter_star__char_class___range__0_9_range__11_65535_char_class___range__10_10__tag__category___67_111_109_109_101_110_116 = (IConstructor) _read("prod(lex(\"Comment\"),[lit(\"//\"),\\iter-star(\\char-class([range(0,9),range(11,65535)])),\\char-class([range(10,10)])],{tag(category(\"Comment\"))})", Factory.Production);
  private static final IConstructor prod__RealLiteral__conditional__lit___46__not_precede__char_class___range__46_46_iter__char_class___range__48_57_char_class___range__69_69_range__101_101_opt__char_class___range__43_43_range__45_45_iter__char_class___range__48_57_opt__char_class___range__68_68_range__70_70_range__100_100_range__102_102_ = (IConstructor) _read("prod(lex(\"RealLiteral\"),[conditional(lit(\".\"),{\\not-precede(\\char-class([range(46,46)]))}),iter(\\char-class([range(48,57)])),\\char-class([range(69,69),range(101,101)]),opt(\\char-class([range(43,43),range(45,45)])),iter(\\char-class([range(48,57)])),opt(\\char-class([range(68,68),range(70,70),range(100,100),range(102,102)]))],{})", Factory.Production);
  private static final IConstructor prod__ReifiedType_Pattern__basicType_BasicType_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_arguments_iter_star_seps__Pattern__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_ = (IConstructor) _read("prod(label(\"ReifiedType\",sort(\"Pattern\")),[label(\"basicType\",sort(\"BasicType\")),layouts(\"LAYOUTLIST\"),lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"arguments\",\\iter-star-seps(sort(\"Pattern\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\")\")],{})", Factory.Production);
  private static final IConstructor prod__RascalKeywords__lit_datetime_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"datetime\")],{})", Factory.Production);
  private static final IConstructor prod__GreaterThanOrEq_Expression__lhs_Expression_layouts_LAYOUTLIST_lit___62_61_layouts_LAYOUTLIST_rhs_Expression__assoc__non_assoc = (IConstructor) _read("prod(label(\"GreaterThanOrEq\",sort(\"Expression\")),[label(\"lhs\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"\\>=\"),layouts(\"LAYOUTLIST\"),label(\"rhs\",sort(\"Expression\"))],{assoc(\\non-assoc())})", Factory.Production);
  private static final IConstructor prod__RascalKeywords__lit_bool_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"bool\")],{})", Factory.Production);
  private static final IConstructor prod__OctalEscapeSequence__lit___92_char_class___range__48_51_char_class___range__48_55_char_class___range__48_55_ = (IConstructor) _read("prod(lex(\"OctalEscapeSequence\"),[lit(\"\\\\\"),\\char-class([range(48,51)]),\\char-class([range(48,55)]),\\char-class([range(48,55)])],{})", Factory.Production);
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
  private static final IConstructor prod__lit_adt__char_class___range__97_97_char_class___range__100_100_char_class___range__116_116_ = (IConstructor) _read("prod(lit(\"adt\"),[\\char-class([range(97,97)]),\\char-class([range(100,100)]),\\char-class([range(116,116)])],{})", Factory.Production);
  private static final IConstructor prod__RascalKeywords__lit_rel_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"rel\")],{})", Factory.Production);
  private static final IConstructor prod__Tuple_BasicType__lit_tuple_ = (IConstructor) _read("prod(label(\"Tuple\",sort(\"BasicType\")),[lit(\"tuple\")],{})", Factory.Production);
  private static final IConstructor prod__JustTime__lit___36_84_TimePartNoTZ_opt__TimeZonePart_ = (IConstructor) _read("prod(lex(\"JustTime\"),[lit(\"$T\"),lex(\"TimePartNoTZ\"),opt(lex(\"TimeZonePart\"))],{})", Factory.Production);
  private static final IConstructor prod__Map_Pattern__lit___40_layouts_LAYOUTLIST_mappings_iter_star_seps__Mapping__Pattern__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_ = (IConstructor) _read("prod(label(\"Map\",sort(\"Pattern\")),[lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"mappings\",\\iter-star-seps(\\parameterized-sort(\"Mapping\",[sort(\"Pattern\")]),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\")\")],{})", Factory.Production);
  private static final IConstructor prod__Unconditional_Replacement__replacementExpression_Expression_ = (IConstructor) _read("prod(label(\"Unconditional\",sort(\"Replacement\")),[label(\"replacementExpression\",sort(\"Expression\"))],{})", Factory.Production);
  private static final IConstructor regular__iter_seps__Sym__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST = (IConstructor) _read("regular(\\iter-seps(sort(\"Sym\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")]))", Factory.Production);
  private static final IConstructor prod__FieldUpdate_Expression__expression_Expression_layouts_LAYOUTLIST_lit___91_layouts_LAYOUTLIST_key_Name_layouts_LAYOUTLIST_lit___61_layouts_LAYOUTLIST_replacement_Expression_layouts_LAYOUTLIST_lit___93_ = (IConstructor) _read("prod(label(\"FieldUpdate\",sort(\"Expression\")),[label(\"expression\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"[\"),layouts(\"LAYOUTLIST\"),label(\"key\",lex(\"Name\")),layouts(\"LAYOUTLIST\"),lit(\"=\"),layouts(\"LAYOUTLIST\"),label(\"replacement\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"]\")],{})", Factory.Production);
  private static final IConstructor prod__lit_dynamic__char_class___range__100_100_char_class___range__121_121_char_class___range__110_110_char_class___range__97_97_char_class___range__109_109_char_class___range__105_105_char_class___range__99_99_ = (IConstructor) _read("prod(lit(\"dynamic\"),[\\char-class([range(100,100)]),\\char-class([range(121,121)]),\\char-class([range(110,110)]),\\char-class([range(97,97)]),\\char-class([range(109,109)]),\\char-class([range(105,105)]),\\char-class([range(99,99)])],{})", Factory.Production);
  private static final IConstructor prod__QualifiedName_Pattern__qualifiedName_QualifiedName_ = (IConstructor) _read("prod(label(\"QualifiedName\",sort(\"Pattern\")),[label(\"qualifiedName\",sort(\"QualifiedName\"))],{})", Factory.Production);
  private static final IConstructor regular__iter_seps__Sym__layouts_LAYOUTLIST_lit___124_layouts_LAYOUTLIST = (IConstructor) _read("regular(\\iter-seps(sort(\"Sym\"),[layouts(\"LAYOUTLIST\"),lit(\"|\"),layouts(\"LAYOUTLIST\")]))", Factory.Production);
  private static final IConstructor prod__MultiVariable_Pattern__qualifiedName_QualifiedName_layouts_LAYOUTLIST_lit___42_ = (IConstructor) _read("prod(label(\"MultiVariable\",sort(\"Pattern\")),[label(\"qualifiedName\",sort(\"QualifiedName\")),layouts(\"LAYOUTLIST\"),lit(\"*\")],{})", Factory.Production);
  private static final IConstructor prod__Is_Expression__expression_Expression_layouts_LAYOUTLIST_lit_is_layouts_LAYOUTLIST_name_Name_ = (IConstructor) _read("prod(label(\"Is\",sort(\"Expression\")),[label(\"expression\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"is\"),layouts(\"LAYOUTLIST\"),label(\"name\",lex(\"Name\"))],{})", Factory.Production);
  private static final IConstructor prod__RascalKeywords__lit_throw_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"throw\")],{})", Factory.Production);
  private static final IConstructor prod__Public_Visibility__lit_public_ = (IConstructor) _read("prod(label(\"Public\",sort(\"Visibility\")),[lit(\"public\")],{})", Factory.Production);
  private static final IConstructor prod__RascalKeywords__lit_module_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"module\")],{})", Factory.Production);
  private static final IConstructor prod__Mid_StringMiddle__mid_MidStringChars_ = (IConstructor) _read("prod(label(\"Mid\",sort(\"StringMiddle\")),[label(\"mid\",lex(\"MidStringChars\"))],{})", Factory.Production);
  private static final IConstructor prod__CaseInsensitiveLiteral_Sym__cistring_CaseInsensitiveStringConstant_ = (IConstructor) _read("prod(label(\"CaseInsensitiveLiteral\",sort(\"Sym\")),[label(\"cistring\",lex(\"CaseInsensitiveStringConstant\"))],{})", Factory.Production);
  private static final IConstructor prod__RascalKeywords__lit_private_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"private\")],{})", Factory.Production);
  private static final IConstructor prod__lit_list__char_class___range__108_108_char_class___range__105_105_char_class___range__115_115_char_class___range__116_116_ = (IConstructor) _read("prod(lit(\"list\"),[\\char-class([range(108,108)]),\\char-class([range(105,105)]),\\char-class([range(115,115)]),\\char-class([range(116,116)])],{})", Factory.Production);
  private static final IConstructor prod__ReifyType_Expression__lit___35_layouts_LAYOUTLIST_type_Type_ = (IConstructor) _read("prod(label(\"ReifyType\",sort(\"Expression\")),[lit(\"#\"),layouts(\"LAYOUTLIST\"),label(\"type\",sort(\"Type\"))],{})", Factory.Production);
  private static final IConstructor prod__Variable_Type__typeVar_TypeVar_ = (IConstructor) _read("prod(label(\"Variable\",sort(\"Type\")),[label(\"typeVar\",sort(\"TypeVar\"))],{})", Factory.Production);
  private static final IConstructor prod__RascalKeywords__lit_map_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"map\")],{})", Factory.Production);
  private static final IConstructor prod__Append_Statement__lit_append_layouts_LAYOUTLIST_dataTarget_DataTarget_layouts_LAYOUTLIST_statement_Statement__assoc__non_assoc = (IConstructor) _read("prod(label(\"Append\",sort(\"Statement\")),[lit(\"append\"),layouts(\"LAYOUTLIST\"),label(\"dataTarget\",sort(\"DataTarget\")),layouts(\"LAYOUTLIST\"),label(\"statement\",sort(\"Statement\"))],{assoc(\\non-assoc())})", Factory.Production);
  private static final IConstructor prod__Left_Assoc__lit_left_ = (IConstructor) _read("prod(label(\"Left\",sort(\"Assoc\")),[lit(\"left\")],{})", Factory.Production);
  private static final IConstructor prod__Import_Command__imported_Import_ = (IConstructor) _read("prod(label(\"Import\",sort(\"Command\")),[label(\"imported\",sort(\"Import\"))],{})", Factory.Production);
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
  private static final IConstructor prod__Module_Kind__lit_module_ = (IConstructor) _read("prod(label(\"Module\",sort(\"Kind\")),[lit(\"module\")],{})", Factory.Production);
  private static final IConstructor prod__lit_bottom_up_break__char_class___range__98_98_char_class___range__111_111_char_class___range__116_116_char_class___range__116_116_char_class___range__111_111_char_class___range__109_109_char_class___range__45_45_char_class___range__117_117_char_class___range__112_112_char_class___range__45_45_char_class___range__98_98_char_class___range__114_114_char_class___range__101_101_char_class___range__97_97_char_class___range__107_107_ = (IConstructor) _read("prod(lit(\"bottom-up-break\"),[\\char-class([range(98,98)]),\\char-class([range(111,111)]),\\char-class([range(116,116)]),\\char-class([range(116,116)]),\\char-class([range(111,111)]),\\char-class([range(109,109)]),\\char-class([range(45,45)]),\\char-class([range(117,117)]),\\char-class([range(112,112)]),\\char-class([range(45,45)]),\\char-class([range(98,98)]),\\char-class([range(114,114)]),\\char-class([range(101,101)]),\\char-class([range(97,97)]),\\char-class([range(107,107)])],{})", Factory.Production);
  private static final IConstructor prod__Integer_Literal__integerLiteral_IntegerLiteral_ = (IConstructor) _read("prod(label(\"Integer\",sort(\"Literal\")),[label(\"integerLiteral\",sort(\"IntegerLiteral\"))],{})", Factory.Production);
  private static final IConstructor prod__PatternWithAction_Case__lit_case_layouts_LAYOUTLIST_patternWithAction_PatternWithAction__tag__Foldable = (IConstructor) _read("prod(label(\"PatternWithAction\",sort(\"Case\")),[lit(\"case\"),layouts(\"LAYOUTLIST\"),label(\"patternWithAction\",sort(\"PatternWithAction\"))],{tag(Foldable())})", Factory.Production);
  private static final IConstructor prod__Rule_Kind__lit_rule_ = (IConstructor) _read("prod(label(\"Rule\",sort(\"Kind\")),[lit(\"rule\")],{})", Factory.Production);
  private static final IConstructor regular__iter_seps__Variable__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST = (IConstructor) _read("regular(\\iter-seps(sort(\"Variable\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")]))", Factory.Production);
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
  private static final IConstructor regular__iter_star_seps__Tag__layouts_LAYOUTLIST = (IConstructor) _read("regular(\\iter-star-seps(sort(\"Tag\"),[layouts(\"LAYOUTLIST\")]))", Factory.Production);
  private static final IConstructor prod__RascalKeywords__lit_repeat_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"repeat\")],{})", Factory.Production);
  private static final IConstructor prod__Void_BasicType__lit_void_ = (IConstructor) _read("prod(label(\"Void\",sort(\"BasicType\")),[lit(\"void\")],{})", Factory.Production);
  private static final IConstructor prod__Reducer_Expression__lit___40_layouts_LAYOUTLIST_init_Expression_layouts_LAYOUTLIST_lit___124_layouts_LAYOUTLIST_result_Expression_layouts_LAYOUTLIST_lit___124_layouts_LAYOUTLIST_generators_iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_ = (IConstructor) _read("prod(label(\"Reducer\",sort(\"Expression\")),[lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"init\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"|\"),layouts(\"LAYOUTLIST\"),label(\"result\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"|\"),layouts(\"LAYOUTLIST\"),label(\"generators\",\\iter-seps(sort(\"Expression\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\")\")],{})", Factory.Production);
  private static final IConstructor prod__VariableBecomes_Pattern__name_Name_layouts_LAYOUTLIST_lit___58_layouts_LAYOUTLIST_pattern_Pattern_ = (IConstructor) _read("prod(label(\"VariableBecomes\",sort(\"Pattern\")),[label(\"name\",lex(\"Name\")),layouts(\"LAYOUTLIST\"),lit(\":\"),layouts(\"LAYOUTLIST\"),label(\"pattern\",sort(\"Pattern\"))],{})", Factory.Production);
  private static final IConstructor prod__RascalKeywords__lit_bracket_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"bracket\")],{})", Factory.Production);
  private static final IConstructor prod__RealLiteral__iter__char_class___range__48_57_char_class___range__68_68_range__70_70_range__100_100_range__102_102_ = (IConstructor) _read("prod(lex(\"RealLiteral\"),[iter(\\char-class([range(48,57)])),\\char-class([range(68,68),range(70,70),range(100,100),range(102,102)])],{})", Factory.Production);
  private static final IConstructor prod__Default_LocalVariableDeclaration__declarator_Declarator_ = (IConstructor) _read("prod(label(\"Default\",sort(\"LocalVariableDeclaration\")),[label(\"declarator\",sort(\"Declarator\"))],{})", Factory.Production);
  private static final IConstructor prod__RealLiteral__iter__char_class___range__48_57_lit___46_iter_star__char_class___range__48_57_char_class___range__69_69_range__101_101_opt__char_class___range__43_43_range__45_45_iter__char_class___range__48_57_opt__char_class___range__68_68_range__70_70_range__100_100_range__102_102_ = (IConstructor) _read("prod(lex(\"RealLiteral\"),[iter(\\char-class([range(48,57)])),lit(\".\"),\\iter-star(\\char-class([range(48,57)])),\\char-class([range(69,69),range(101,101)]),opt(\\char-class([range(43,43),range(45,45)])),iter(\\char-class([range(48,57)])),opt(\\char-class([range(68,68),range(70,70),range(100,100),range(102,102)]))],{})", Factory.Production);
  private static final IConstructor prod__RascalKeywords__lit_num_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"num\")],{})", Factory.Production);
  private static final IConstructor prod__VoidClosure_Expression__parameters_Parameters_layouts_LAYOUTLIST_lit___123_layouts_LAYOUTLIST_statements_iter_star_seps__Statement__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_ = (IConstructor) _read("prod(label(\"VoidClosure\",sort(\"Expression\")),[label(\"parameters\",sort(\"Parameters\")),layouts(\"LAYOUTLIST\"),lit(\"{\"),layouts(\"LAYOUTLIST\"),label(\"statements\",\\iter-star-seps(sort(\"Statement\"),[layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\"}\")],{})", Factory.Production);
  private static final IConstructor prod__LAYOUT__char_class___range__9_10_range__13_13_range__32_32_ = (IConstructor) _read("prod(lex(\"LAYOUT\"),[\\char-class([range(9,10),range(13,13),range(32,32)])],{})", Factory.Production);
  private static final IConstructor prod__MidInterpolated_StringTail__mid_MidStringChars_layouts_LAYOUTLIST_expression_Expression_layouts_LAYOUTLIST_tail_StringTail_ = (IConstructor) _read("prod(label(\"MidInterpolated\",sort(\"StringTail\")),[label(\"mid\",lex(\"MidStringChars\")),layouts(\"LAYOUTLIST\"),label(\"expression\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),label(\"tail\",sort(\"StringTail\"))],{})", Factory.Production);
  private static final IConstructor prod__BooleanLiteral__lit_true_ = (IConstructor) _read("prod(lex(\"BooleanLiteral\"),[lit(\"true\")],{})", Factory.Production);
  private static final IConstructor prod__lit_public__char_class___range__112_112_char_class___range__117_117_char_class___range__98_98_char_class___range__108_108_char_class___range__105_105_char_class___range__99_99_ = (IConstructor) _read("prod(lit(\"public\"),[\\char-class([range(112,112)]),\\char-class([range(117,117)]),\\char-class([range(98,98)]),\\char-class([range(108,108)]),\\char-class([range(105,105)]),\\char-class([range(99,99)])],{})", Factory.Production);
  private static final IConstructor prod__GivenStrategy_Visit__strategy_Strategy_layouts_LAYOUTLIST_lit_visit_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_subject_Expression_layouts_LAYOUTLIST_lit___41_layouts_LAYOUTLIST_lit___123_layouts_LAYOUTLIST_cases_iter_seps__Case__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_ = (IConstructor) _read("prod(label(\"GivenStrategy\",sort(\"Visit\")),[label(\"strategy\",sort(\"Strategy\")),layouts(\"LAYOUTLIST\"),lit(\"visit\"),layouts(\"LAYOUTLIST\"),lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"subject\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\")\"),layouts(\"LAYOUTLIST\"),lit(\"{\"),layouts(\"LAYOUTLIST\"),label(\"cases\",\\iter-seps(sort(\"Case\"),[layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\"}\")],{})", Factory.Production);
  private static final IConstructor prod__lit_renaming__char_class___range__114_114_char_class___range__101_101_char_class___range__110_110_char_class___range__97_97_char_class___range__109_109_char_class___range__105_105_char_class___range__110_110_char_class___range__103_103_ = (IConstructor) _read("prod(lit(\"renaming\"),[\\char-class([range(114,114)]),\\char-class([range(101,101)]),\\char-class([range(110,110)]),\\char-class([range(97,97)]),\\char-class([range(109,109)]),\\char-class([range(105,105)]),\\char-class([range(110,110)]),\\char-class([range(103,103)])],{})", Factory.Production);
  private static final IConstructor prod__Composition_Expression__lhs_Expression_layouts_LAYOUTLIST_lit_o_layouts_LAYOUTLIST_rhs_Expression__assoc__left = (IConstructor) _read("prod(label(\"Composition\",sort(\"Expression\")),[label(\"lhs\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"o\"),layouts(\"LAYOUTLIST\"),label(\"rhs\",sort(\"Expression\"))],{assoc(left())})", Factory.Production);
  private static final IConstructor prod__NonterminalLabel__char_class___range__97_122_conditional__iter_star__char_class___range__48_57_range__65_90_range__95_95_range__97_122__not_follow__char_class___range__48_57_range__65_90_range__95_95_range__97_122_ = (IConstructor) _read("prod(lex(\"NonterminalLabel\"),[\\char-class([range(97,122)]),conditional(\\iter-star(\\char-class([range(48,57),range(65,90),range(95,95),range(97,122)])),{\\not-follow(\\char-class([range(48,57),range(65,90),range(95,95),range(97,122)]))})],{})", Factory.Production);
  private static final IConstructor prod__lit_value__char_class___range__118_118_char_class___range__97_97_char_class___range__108_108_char_class___range__117_117_char_class___range__101_101_ = (IConstructor) _read("prod(lit(\"value\"),[\\char-class([range(118,118)]),\\char-class([range(97,97)]),\\char-class([range(108,108)]),\\char-class([range(117,117)]),\\char-class([range(101,101)])],{})", Factory.Production);
  private static final IConstructor prod__Literal_Expression__literal_Literal_ = (IConstructor) _read("prod(label(\"Literal\",sort(\"Expression\")),[label(\"literal\",sort(\"Literal\"))],{})", Factory.Production);
  private static final IConstructor prod__Word__conditional__iter__char_class___range__0_8_range__11_12_range__14_31_range__33_65535__not_follow__char_class___range__0_8_range__11_12_range__14_31_range__33_65535_ = (IConstructor) _read("prod(lex(\"Word\"),[conditional(iter(\\char-class([range(0,8),range(11,12),range(14,31),range(33,65535)])),{\\not-follow(\\char-class([range(0,8),range(11,12),range(14,31),range(33,65535)]))})],{})", Factory.Production);
  private static final IConstructor prod__RegExp__Backslash_ = (IConstructor) _read("prod(lex(\"RegExp\"),[lex(\"Backslash\")],{})", Factory.Production);
  private static final IConstructor prod__IsDefined_Expression__argument_Expression_layouts_LAYOUTLIST_lit___63_ = (IConstructor) _read("prod(label(\"IsDefined\",sort(\"Expression\")),[label(\"argument\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"?\")],{})", Factory.Production);
  private static final IConstructor prod__lit_module__char_class___range__109_109_char_class___range__111_111_char_class___range__100_100_char_class___range__117_117_char_class___range__108_108_char_class___range__101_101_ = (IConstructor) _read("prod(lit(\"module\"),[\\char-class([range(109,109)]),\\char-class([range(111,111)]),\\char-class([range(100,100)]),\\char-class([range(117,117)]),\\char-class([range(108,108)]),\\char-class([range(101,101)])],{})", Factory.Production);
  private static final IConstructor prod__RascalKeywords__lit_solve_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"solve\")],{})", Factory.Production);
  private static final IConstructor prod__NamedRegExp__NamedBackslash_ = (IConstructor) _read("prod(lex(\"NamedRegExp\"),[lex(\"NamedBackslash\")],{})", Factory.Production);
    
  // Item declarations
	
	
  protected static class Literal {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__DateTime_Literal__dateTimeLiteral_DateTimeLiteral_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode(36, 0, "DateTimeLiteral", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__DateTime_Literal__dateTimeLiteral_DateTimeLiteral_, tmp);
	}
    protected static final void _init_prod__String_Literal__stringLiteral_StringLiteral_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode(38, 0, "StringLiteral", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__String_Literal__stringLiteral_StringLiteral_, tmp);
	}
    protected static final void _init_prod__Real_Literal__realLiteral_RealLiteral_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode(40, 0, "RealLiteral", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Real_Literal__realLiteral_RealLiteral_, tmp);
	}
    protected static final void _init_prod__Location_Literal__locationLiteral_LocationLiteral_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode(42, 0, "LocationLiteral", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Location_Literal__locationLiteral_LocationLiteral_, tmp);
	}
    protected static final void _init_prod__Rational_Literal__rationalLiteral_RationalLiteral_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode(44, 0, "RationalLiteral", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Rational_Literal__rationalLiteral_RationalLiteral_, tmp);
	}
    protected static final void _init_prod__RegExp_Literal__regExpLiteral_RegExpLiteral_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode(46, 0, "RegExpLiteral", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RegExp_Literal__regExpLiteral_RegExpLiteral_, tmp);
	}
    protected static final void _init_prod__Boolean_Literal__booleanLiteral_BooleanLiteral_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode(48, 0, "BooleanLiteral", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Boolean_Literal__booleanLiteral_BooleanLiteral_, tmp);
	}
    protected static final void _init_prod__Integer_Literal__integerLiteral_IntegerLiteral_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode(50, 0, "IntegerLiteral", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Integer_Literal__integerLiteral_IntegerLiteral_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
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
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__Default_Module__header_Header_layouts_LAYOUTLIST_body_Body_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode(56, 2, "Body", null, null);
      tmp[1] = new NonTerminalStackNode(54, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(52, 0, "Header", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Default_Module__header_Header_layouts_LAYOUTLIST_body_Body_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__Default_Module__header_Header_layouts_LAYOUTLIST_body_Body_(builder);
      
    }
  }
	
  protected static class PreProtocolChars {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__PreProtocolChars__lit___124_URLChars_lit___60_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[2] = new LiteralStackNode(64, 2, prod__lit___60__char_class___range__60_60_, new char[] {60}, null, null);
      tmp[1] = new NonTerminalStackNode(62, 1, "URLChars", null, null);
      tmp[0] = new LiteralStackNode(60, 0, prod__lit___124__char_class___range__124_124_, new char[] {124}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__PreProtocolChars__lit___124_URLChars_lit___60_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__PreProtocolChars__lit___124_URLChars_lit___60_(builder);
      
    }
  }
	
  protected static class TypeArg {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__Default_TypeArg__type_Type_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode(116, 0, "Type", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Default_TypeArg__type_Type_, tmp);
	}
    protected static final void _init_prod__Named_TypeArg__type_Type_layouts_LAYOUTLIST_name_Name_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode(122, 2, "Name", null, null);
      tmp[1] = new NonTerminalStackNode(120, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(118, 0, "Type", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Named_TypeArg__type_Type_layouts_LAYOUTLIST_name_Name_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__Default_TypeArg__type_Type_(builder);
      
        _init_prod__Named_TypeArg__type_Type_layouts_LAYOUTLIST_name_Name_(builder);
      
    }
  }
	
  protected static class Variable {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__UnInitialized_Variable__name_Name_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode(104, 0, "Name", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__UnInitialized_Variable__name_Name_, tmp);
	}
    protected static final void _init_prod__Initialized_Variable__name_Name_layouts_LAYOUTLIST_lit___61_layouts_LAYOUTLIST_initial_Expression_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(114, 4, "Expression", null, null);
      tmp[3] = new NonTerminalStackNode(112, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(110, 2, prod__lit___61__char_class___range__61_61_, new char[] {61}, null, null);
      tmp[1] = new NonTerminalStackNode(108, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(106, 0, "Name", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Initialized_Variable__name_Name_layouts_LAYOUTLIST_lit___61_layouts_LAYOUTLIST_initial_Expression_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__UnInitialized_Variable__name_Name_(builder);
      
        _init_prod__Initialized_Variable__name_Name_layouts_LAYOUTLIST_lit___61_layouts_LAYOUTLIST_initial_Expression_(builder);
      
    }
  }
	
  protected static class Catch {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__Default_Catch__lit_catch_layouts_LAYOUTLIST_lit___58_layouts_LAYOUTLIST_body_Statement__tag__Foldable(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(150, 4, "Statement", null, null);
      tmp[3] = new NonTerminalStackNode(148, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(146, 2, prod__lit___58__char_class___range__58_58_, new char[] {58}, null, null);
      tmp[1] = new NonTerminalStackNode(144, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(142, 0, prod__lit_catch__char_class___range__99_99_char_class___range__97_97_char_class___range__116_116_char_class___range__99_99_char_class___range__104_104_, new char[] {99,97,116,99,104}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Default_Catch__lit_catch_layouts_LAYOUTLIST_lit___58_layouts_LAYOUTLIST_body_Statement__tag__Foldable, tmp);
	}
    protected static final void _init_prod__Binding_Catch__lit_catch_layouts_LAYOUTLIST_pattern_Pattern_layouts_LAYOUTLIST_lit___58_layouts_LAYOUTLIST_body_Statement__tag__Foldable(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[7];
      
      tmp[6] = new NonTerminalStackNode(164, 6, "Statement", null, null);
      tmp[5] = new NonTerminalStackNode(162, 5, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode(160, 4, prod__lit___58__char_class___range__58_58_, new char[] {58}, null, null);
      tmp[3] = new NonTerminalStackNode(158, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(156, 2, "Pattern", null, null);
      tmp[1] = new NonTerminalStackNode(154, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(152, 0, prod__lit_catch__char_class___range__99_99_char_class___range__97_97_char_class___range__116_116_char_class___range__99_99_char_class___range__104_104_, new char[] {99,97,116,99,104}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Binding_Catch__lit_catch_layouts_LAYOUTLIST_pattern_Pattern_layouts_LAYOUTLIST_lit___58_layouts_LAYOUTLIST_body_Statement__tag__Foldable, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__Default_Catch__lit_catch_layouts_LAYOUTLIST_lit___58_layouts_LAYOUTLIST_body_Statement__tag__Foldable(builder);
      
        _init_prod__Binding_Catch__lit_catch_layouts_LAYOUTLIST_pattern_Pattern_layouts_LAYOUTLIST_lit___58_layouts_LAYOUTLIST_body_Statement__tag__Foldable(builder);
      
    }
  }
	
  protected static class Renaming {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__Default_Renaming__from_Name_layouts_LAYOUTLIST_lit___61_62_layouts_LAYOUTLIST_to_Name_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(140, 4, "Name", null, null);
      tmp[3] = new NonTerminalStackNode(138, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(136, 2, prod__lit___61_62__char_class___range__61_61_char_class___range__62_62_, new char[] {61,62}, null, null);
      tmp[1] = new NonTerminalStackNode(134, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(132, 0, "Name", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Default_Renaming__from_Name_layouts_LAYOUTLIST_lit___61_62_layouts_LAYOUTLIST_to_Name_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__Default_Renaming__from_Name_layouts_LAYOUTLIST_lit___61_62_layouts_LAYOUTLIST_to_Name_(builder);
      
    }
  }
	
  protected static class Signature {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__WithThrows_Signature__modifiers_FunctionModifiers_layouts_LAYOUTLIST_type_Type_layouts_LAYOUTLIST_name_Name_layouts_LAYOUTLIST_parameters_Parameters_layouts_LAYOUTLIST_lit_throws_layouts_LAYOUTLIST_exceptions_iter_seps__Type__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[11];
      
      tmp[10] = new SeparatedListStackNode(200, 10, regular__iter_seps__Type__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST, new NonTerminalStackNode(202, 0, "Type", null, null), new AbstractStackNode[]{new NonTerminalStackNode(204, 1, "layouts_LAYOUTLIST", null, null), new LiteralStackNode(206, 2, prod__lit___44__char_class___range__44_44_, new char[] {44}, null, null), new NonTerminalStackNode(208, 3, "layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[0] = new NonTerminalStackNode(180, 0, "FunctionModifiers", null, null);
      tmp[1] = new NonTerminalStackNode(182, 1, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(184, 2, "Type", null, null);
      tmp[3] = new NonTerminalStackNode(186, 3, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new NonTerminalStackNode(188, 4, "Name", null, null);
      tmp[5] = new NonTerminalStackNode(190, 5, "layouts_LAYOUTLIST", null, null);
      tmp[6] = new NonTerminalStackNode(192, 6, "Parameters", null, null);
      tmp[7] = new NonTerminalStackNode(194, 7, "layouts_LAYOUTLIST", null, null);
      tmp[8] = new LiteralStackNode(196, 8, prod__lit_throws__char_class___range__116_116_char_class___range__104_104_char_class___range__114_114_char_class___range__111_111_char_class___range__119_119_char_class___range__115_115_, new char[] {116,104,114,111,119,115}, null, null);
      tmp[9] = new NonTerminalStackNode(198, 9, "layouts_LAYOUTLIST", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__WithThrows_Signature__modifiers_FunctionModifiers_layouts_LAYOUTLIST_type_Type_layouts_LAYOUTLIST_name_Name_layouts_LAYOUTLIST_parameters_Parameters_layouts_LAYOUTLIST_lit_throws_layouts_LAYOUTLIST_exceptions_iter_seps__Type__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_, tmp);
	}
    protected static final void _init_prod__NoThrows_Signature__modifiers_FunctionModifiers_layouts_LAYOUTLIST_type_Type_layouts_LAYOUTLIST_name_Name_layouts_LAYOUTLIST_parameters_Parameters_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[7];
      
      tmp[6] = new NonTerminalStackNode(178, 6, "Parameters", null, null);
      tmp[5] = new NonTerminalStackNode(176, 5, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new NonTerminalStackNode(174, 4, "Name", null, null);
      tmp[3] = new NonTerminalStackNode(172, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(170, 2, "Type", null, null);
      tmp[1] = new NonTerminalStackNode(168, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(166, 0, "FunctionModifiers", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__NoThrows_Signature__modifiers_FunctionModifiers_layouts_LAYOUTLIST_type_Type_layouts_LAYOUTLIST_name_Name_layouts_LAYOUTLIST_parameters_Parameters_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__WithThrows_Signature__modifiers_FunctionModifiers_layouts_LAYOUTLIST_type_Type_layouts_LAYOUTLIST_name_Name_layouts_LAYOUTLIST_parameters_Parameters_layouts_LAYOUTLIST_lit_throws_layouts_LAYOUTLIST_exceptions_iter_seps__Type__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_(builder);
      
        _init_prod__NoThrows_Signature__modifiers_FunctionModifiers_layouts_LAYOUTLIST_type_Type_layouts_LAYOUTLIST_name_Name_layouts_LAYOUTLIST_parameters_Parameters_(builder);
      
    }
  }
	
  protected static class Sym {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__Empty_Sym__lit___40_layouts_LAYOUTLIST_lit___41_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[2] = new LiteralStackNode(228, 2, prod__lit___41__char_class___range__41_41_, new char[] {41}, null, null);
      tmp[1] = new NonTerminalStackNode(226, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(224, 0, prod__lit___40__char_class___range__40_40_, new char[] {40}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Empty_Sym__lit___40_layouts_LAYOUTLIST_lit___41_, tmp);
	}
    protected static final void _init_prod__CharacterClass_Sym__charClass_Class_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode(230, 0, "Class", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__CharacterClass_Sym__charClass_Class_, tmp);
	}
    protected static final void _init_prod__Sequence_Sym__lit___40_layouts_LAYOUTLIST_first_Sym_layouts_LAYOUTLIST_sequence_iter_seps__Sym__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[7];
      
      tmp[6] = new LiteralStackNode(248, 6, prod__lit___41__char_class___range__41_41_, new char[] {41}, null, null);
      tmp[5] = new NonTerminalStackNode(246, 5, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new SeparatedListStackNode(240, 4, regular__iter_seps__Sym__layouts_LAYOUTLIST, new NonTerminalStackNode(242, 0, "Sym", null, null), new AbstractStackNode[]{new NonTerminalStackNode(244, 1, "layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[3] = new NonTerminalStackNode(238, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(236, 2, "Sym", null, null);
      tmp[1] = new NonTerminalStackNode(234, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(232, 0, prod__lit___40__char_class___range__40_40_, new char[] {40}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Sequence_Sym__lit___40_layouts_LAYOUTLIST_first_Sym_layouts_LAYOUTLIST_sequence_iter_seps__Sym__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_, tmp);
	}
    protected static final void _init_prod__NotPrecede_Sym__match_Sym_layouts_LAYOUTLIST_lit___33_60_60_layouts_LAYOUTLIST_symbol_Sym__assoc__right(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(434, 4, "Sym", null, null);
      tmp[3] = new NonTerminalStackNode(432, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(430, 2, prod__lit___33_60_60__char_class___range__33_33_char_class___range__60_60_char_class___range__60_60_, new char[] {33,60,60}, null, null);
      tmp[1] = new NonTerminalStackNode(428, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(426, 0, "Sym", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__NotPrecede_Sym__match_Sym_layouts_LAYOUTLIST_lit___33_60_60_layouts_LAYOUTLIST_symbol_Sym__assoc__right, tmp);
	}
    protected static final void _init_prod__IterStar_Sym__symbol_Sym_layouts_LAYOUTLIST_lit___42_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[2] = new LiteralStackNode(254, 2, prod__lit___42__char_class___range__42_42_, new char[] {42}, null, null);
      tmp[1] = new NonTerminalStackNode(252, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(250, 0, "Sym", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__IterStar_Sym__symbol_Sym_layouts_LAYOUTLIST_lit___42_, tmp);
	}
    protected static final void _init_prod__Labeled_Sym__symbol_Sym_layouts_LAYOUTLIST_label_NonterminalLabel_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode(260, 2, "NonterminalLabel", null, null);
      tmp[1] = new NonTerminalStackNode(258, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(256, 0, "Sym", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Labeled_Sym__symbol_Sym_layouts_LAYOUTLIST_label_NonterminalLabel_, tmp);
	}
    protected static final void _init_prod__Parameter_Sym__lit___38_layouts_LAYOUTLIST_nonterminal_Nonterminal_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode(266, 2, "Nonterminal", null, null);
      tmp[1] = new NonTerminalStackNode(264, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(262, 0, prod__lit___38__char_class___range__38_38_, new char[] {38}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Parameter_Sym__lit___38_layouts_LAYOUTLIST_nonterminal_Nonterminal_, tmp);
	}
    protected static final void _init_prod__IterStarSep_Sym__lit___123_layouts_LAYOUTLIST_symbol_Sym_layouts_LAYOUTLIST_sep_Sym_layouts_LAYOUTLIST_lit___125_layouts_LAYOUTLIST_lit___42_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[9];
      
      tmp[8] = new LiteralStackNode(284, 8, prod__lit___42__char_class___range__42_42_, new char[] {42}, null, null);
      tmp[7] = new NonTerminalStackNode(282, 7, "layouts_LAYOUTLIST", null, null);
      tmp[6] = new LiteralStackNode(280, 6, prod__lit___125__char_class___range__125_125_, new char[] {125}, null, null);
      tmp[5] = new NonTerminalStackNode(278, 5, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new NonTerminalStackNode(276, 4, "Sym", null, null);
      tmp[3] = new NonTerminalStackNode(274, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(272, 2, "Sym", null, null);
      tmp[1] = new NonTerminalStackNode(270, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(268, 0, prod__lit___123__char_class___range__123_123_, new char[] {123}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__IterStarSep_Sym__lit___123_layouts_LAYOUTLIST_symbol_Sym_layouts_LAYOUTLIST_sep_Sym_layouts_LAYOUTLIST_lit___125_layouts_LAYOUTLIST_lit___42_, tmp);
	}
    protected static final void _init_prod__Alternative_Sym__lit___40_layouts_LAYOUTLIST_first_Sym_layouts_LAYOUTLIST_lit___124_layouts_LAYOUTLIST_alternatives_iter_seps__Sym__layouts_LAYOUTLIST_lit___124_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[9];
      
      tmp[8] = new LiteralStackNode(310, 8, prod__lit___41__char_class___range__41_41_, new char[] {41}, null, null);
      tmp[7] = new NonTerminalStackNode(308, 7, "layouts_LAYOUTLIST", null, null);
      tmp[6] = new SeparatedListStackNode(298, 6, regular__iter_seps__Sym__layouts_LAYOUTLIST_lit___124_layouts_LAYOUTLIST, new NonTerminalStackNode(300, 0, "Sym", null, null), new AbstractStackNode[]{new NonTerminalStackNode(302, 1, "layouts_LAYOUTLIST", null, null), new LiteralStackNode(304, 2, prod__lit___124__char_class___range__124_124_, new char[] {124}, null, null), new NonTerminalStackNode(306, 3, "layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[5] = new NonTerminalStackNode(296, 5, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode(294, 4, prod__lit___124__char_class___range__124_124_, new char[] {124}, null, null);
      tmp[3] = new NonTerminalStackNode(292, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(290, 2, "Sym", null, null);
      tmp[1] = new NonTerminalStackNode(288, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(286, 0, prod__lit___40__char_class___range__40_40_, new char[] {40}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Alternative_Sym__lit___40_layouts_LAYOUTLIST_first_Sym_layouts_LAYOUTLIST_lit___124_layouts_LAYOUTLIST_alternatives_iter_seps__Sym__layouts_LAYOUTLIST_lit___124_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_, tmp);
	}
    protected static final void _init_prod__Follow_Sym__symbol_Sym_layouts_LAYOUTLIST_lit___62_62_layouts_LAYOUTLIST_match_Sym__assoc__left(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(424, 4, "Sym", null, null);
      tmp[3] = new NonTerminalStackNode(422, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(420, 2, prod__lit___62_62__char_class___range__62_62_char_class___range__62_62_, new char[] {62,62}, null, null);
      tmp[1] = new NonTerminalStackNode(418, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(416, 0, "Sym", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Follow_Sym__symbol_Sym_layouts_LAYOUTLIST_lit___62_62_layouts_LAYOUTLIST_match_Sym__assoc__left, tmp);
	}
    protected static final void _init_prod__EndOfLine_Sym__symbol_Sym_layouts_LAYOUTLIST_lit___36_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[2] = new LiteralStackNode(316, 2, prod__lit___36__char_class___range__36_36_, new char[] {36}, null, null);
      tmp[1] = new NonTerminalStackNode(314, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(312, 0, "Sym", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__EndOfLine_Sym__symbol_Sym_layouts_LAYOUTLIST_lit___36_, tmp);
	}
    protected static final void _init_prod__Precede_Sym__match_Sym_layouts_LAYOUTLIST_lit___60_60_layouts_LAYOUTLIST_symbol_Sym__assoc__right(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(444, 4, "Sym", null, null);
      tmp[3] = new NonTerminalStackNode(442, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(440, 2, prod__lit___60_60__char_class___range__60_60_char_class___range__60_60_, new char[] {60,60}, null, null);
      tmp[1] = new NonTerminalStackNode(438, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(436, 0, "Sym", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Precede_Sym__match_Sym_layouts_LAYOUTLIST_lit___60_60_layouts_LAYOUTLIST_symbol_Sym__assoc__right, tmp);
	}
    protected static final void _init_prod__Column_Sym__symbol_Sym_layouts_LAYOUTLIST_lit___64_layouts_LAYOUTLIST_column_IntegerLiteral_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(326, 4, "IntegerLiteral", null, null);
      tmp[3] = new NonTerminalStackNode(324, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(322, 2, prod__lit___64__char_class___range__64_64_, new char[] {64}, null, null);
      tmp[1] = new NonTerminalStackNode(320, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(318, 0, "Sym", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Column_Sym__symbol_Sym_layouts_LAYOUTLIST_lit___64_layouts_LAYOUTLIST_column_IntegerLiteral_, tmp);
	}
    protected static final void _init_prod__Nonterminal_Sym__conditional__nonterminal_Nonterminal__not_follow__lit___91_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode(328, 0, "Nonterminal", null, new ICompletionFilter[] {new StringFollowRestriction(new char[] {91})});
      builder.addAlternative(ObjectRascalRascal.prod__Nonterminal_Sym__conditional__nonterminal_Nonterminal__not_follow__lit___91_, tmp);
	}
    protected static final void _init_prod__Optional_Sym__symbol_Sym_layouts_LAYOUTLIST_lit___63_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[2] = new LiteralStackNode(334, 2, prod__lit___63__char_class___range__63_63_, new char[] {63}, null, null);
      tmp[1] = new NonTerminalStackNode(332, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(330, 0, "Sym", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Optional_Sym__symbol_Sym_layouts_LAYOUTLIST_lit___63_, tmp);
	}
    protected static final void _init_prod__Start_Sym__lit_start_layouts_LAYOUTLIST_lit___91_layouts_LAYOUTLIST_nonterminal_Nonterminal_layouts_LAYOUTLIST_lit___93_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[7];
      
      tmp[6] = new LiteralStackNode(350, 6, prod__lit___93__char_class___range__93_93_, new char[] {93}, null, null);
      tmp[5] = new NonTerminalStackNode(348, 5, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new NonTerminalStackNode(346, 4, "Nonterminal", null, null);
      tmp[3] = new NonTerminalStackNode(344, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(342, 2, prod__lit___91__char_class___range__91_91_, new char[] {91}, null, null);
      tmp[1] = new NonTerminalStackNode(340, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(338, 0, prod__lit_start__char_class___range__115_115_char_class___range__116_116_char_class___range__97_97_char_class___range__114_114_char_class___range__116_116_, new char[] {115,116,97,114,116}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Start_Sym__lit_start_layouts_LAYOUTLIST_lit___91_layouts_LAYOUTLIST_nonterminal_Nonterminal_layouts_LAYOUTLIST_lit___93_, tmp);
	}
    protected static final void _init_prod__CaseInsensitiveLiteral_Sym__cistring_CaseInsensitiveStringConstant_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode(336, 0, "CaseInsensitiveStringConstant", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__CaseInsensitiveLiteral_Sym__cistring_CaseInsensitiveStringConstant_, tmp);
	}
    protected static final void _init_prod__Unequal_Sym__symbol_Sym_layouts_LAYOUTLIST_lit___92_layouts_LAYOUTLIST_match_Sym__assoc__left(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(454, 4, "Sym", null, null);
      tmp[3] = new NonTerminalStackNode(452, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(450, 2, prod__lit___92__char_class___range__92_92_, new char[] {92}, null, null);
      tmp[1] = new NonTerminalStackNode(448, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(446, 0, "Sym", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Unequal_Sym__symbol_Sym_layouts_LAYOUTLIST_lit___92_layouts_LAYOUTLIST_match_Sym__assoc__left, tmp);
	}
    protected static final void _init_prod__NotFollow_Sym__symbol_Sym_layouts_LAYOUTLIST_lit___33_62_62_layouts_LAYOUTLIST_match_Sym__assoc__left(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(414, 4, "Sym", null, null);
      tmp[3] = new NonTerminalStackNode(412, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(410, 2, prod__lit___33_62_62__char_class___range__33_33_char_class___range__62_62_char_class___range__62_62_, new char[] {33,62,62}, null, null);
      tmp[1] = new NonTerminalStackNode(408, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(406, 0, "Sym", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__NotFollow_Sym__symbol_Sym_layouts_LAYOUTLIST_lit___33_62_62_layouts_LAYOUTLIST_match_Sym__assoc__left, tmp);
	}
    protected static final void _init_prod__Literal_Sym__string_StringConstant_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode(352, 0, "StringConstant", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Literal_Sym__string_StringConstant_, tmp);
	}
    protected static final void _init_prod__Parametrized_Sym__conditional__nonterminal_Nonterminal__follow__lit___91_layouts_LAYOUTLIST_lit___91_layouts_LAYOUTLIST_parameters_iter_seps__Sym__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___93_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[7];
      
      tmp[6] = new LiteralStackNode(374, 6, prod__lit___93__char_class___range__93_93_, new char[] {93}, null, null);
      tmp[5] = new NonTerminalStackNode(372, 5, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new SeparatedListStackNode(362, 4, regular__iter_seps__Sym__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST, new NonTerminalStackNode(364, 0, "Sym", null, null), new AbstractStackNode[]{new NonTerminalStackNode(366, 1, "layouts_LAYOUTLIST", null, null), new LiteralStackNode(368, 2, prod__lit___44__char_class___range__44_44_, new char[] {44}, null, null), new NonTerminalStackNode(370, 3, "layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[3] = new NonTerminalStackNode(360, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(358, 2, prod__lit___91__char_class___range__91_91_, new char[] {91}, null, null);
      tmp[1] = new NonTerminalStackNode(356, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(354, 0, "Nonterminal", null, new ICompletionFilter[] {new StringFollowRequirement(new char[] {91})});
      builder.addAlternative(ObjectRascalRascal.prod__Parametrized_Sym__conditional__nonterminal_Nonterminal__follow__lit___91_layouts_LAYOUTLIST_lit___91_layouts_LAYOUTLIST_parameters_iter_seps__Sym__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___93_, tmp);
	}
    protected static final void _init_prod__IterSep_Sym__lit___123_layouts_LAYOUTLIST_symbol_Sym_layouts_LAYOUTLIST_sep_Sym_layouts_LAYOUTLIST_lit___125_layouts_LAYOUTLIST_lit___43_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[9];
      
      tmp[8] = new LiteralStackNode(392, 8, prod__lit___43__char_class___range__43_43_, new char[] {43}, null, null);
      tmp[7] = new NonTerminalStackNode(390, 7, "layouts_LAYOUTLIST", null, null);
      tmp[6] = new LiteralStackNode(388, 6, prod__lit___125__char_class___range__125_125_, new char[] {125}, null, null);
      tmp[5] = new NonTerminalStackNode(386, 5, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new NonTerminalStackNode(384, 4, "Sym", null, null);
      tmp[3] = new NonTerminalStackNode(382, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(380, 2, "Sym", null, null);
      tmp[1] = new NonTerminalStackNode(378, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(376, 0, prod__lit___123__char_class___range__123_123_, new char[] {123}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__IterSep_Sym__lit___123_layouts_LAYOUTLIST_symbol_Sym_layouts_LAYOUTLIST_sep_Sym_layouts_LAYOUTLIST_lit___125_layouts_LAYOUTLIST_lit___43_, tmp);
	}
    protected static final void _init_prod__StartOfLine_Sym__lit___94_layouts_LAYOUTLIST_symbol_Sym_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode(398, 2, "Sym", null, null);
      tmp[1] = new NonTerminalStackNode(396, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(394, 0, prod__lit___94__char_class___range__94_94_, new char[] {94}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__StartOfLine_Sym__lit___94_layouts_LAYOUTLIST_symbol_Sym_, tmp);
	}
    protected static final void _init_prod__Iter_Sym__symbol_Sym_layouts_LAYOUTLIST_lit___43_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[2] = new LiteralStackNode(404, 2, prod__lit___43__char_class___range__43_43_, new char[] {43}, null, null);
      tmp[1] = new NonTerminalStackNode(402, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(400, 0, "Sym", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Iter_Sym__symbol_Sym_layouts_LAYOUTLIST_lit___43_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
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
      
    }
  }
	
  protected static class NonterminalLabel {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__NonterminalLabel__char_class___range__97_122_conditional__iter_star__char_class___range__48_57_range__65_90_range__95_95_range__97_122__not_follow__char_class___range__48_57_range__65_90_range__95_95_range__97_122_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[2];
      
      tmp[1] = new ListStackNode(466, 1, regular__iter_star__char_class___range__48_57_range__65_90_range__95_95_range__97_122, new CharStackNode(468, 0, new char[][]{{48,57},{65,90},{95,95},{97,122}}, null, null), false, null, new ICompletionFilter[] {new CharFollowRestriction(new char[][]{{48,57},{65,90},{95,95},{97,122}})});
      tmp[0] = new CharStackNode(464, 0, new char[][]{{97,122}}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__NonterminalLabel__char_class___range__97_122_conditional__iter_star__char_class___range__48_57_range__65_90_range__95_95_range__97_122__not_follow__char_class___range__48_57_range__65_90_range__95_95_range__97_122_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__NonterminalLabel__char_class___range__97_122_conditional__iter_star__char_class___range__48_57_range__65_90_range__95_95_range__97_122__not_follow__char_class___range__48_57_range__65_90_range__95_95_range__97_122_(builder);
      
    }
  }
	
  protected static class Header {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__Default_Header__tags_Tags_layouts_LAYOUTLIST_lit_module_layouts_LAYOUTLIST_name_QualifiedName_layouts_LAYOUTLIST_imports_iter_star_seps__Import__layouts_LAYOUTLIST_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[7];
      
      tmp[6] = new SeparatedListStackNode(508, 6, regular__iter_star_seps__Import__layouts_LAYOUTLIST, new NonTerminalStackNode(510, 0, "Import", null, null), new AbstractStackNode[]{new NonTerminalStackNode(512, 1, "layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[5] = new NonTerminalStackNode(506, 5, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new NonTerminalStackNode(504, 4, "QualifiedName", null, null);
      tmp[3] = new NonTerminalStackNode(502, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(500, 2, prod__lit_module__char_class___range__109_109_char_class___range__111_111_char_class___range__100_100_char_class___range__117_117_char_class___range__108_108_char_class___range__101_101_, new char[] {109,111,100,117,108,101}, null, null);
      tmp[1] = new NonTerminalStackNode(498, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(496, 0, "Tags", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Default_Header__tags_Tags_layouts_LAYOUTLIST_lit_module_layouts_LAYOUTLIST_name_QualifiedName_layouts_LAYOUTLIST_imports_iter_star_seps__Import__layouts_LAYOUTLIST_, tmp);
	}
    protected static final void _init_prod__Parameters_Header__tags_Tags_layouts_LAYOUTLIST_lit_module_layouts_LAYOUTLIST_name_QualifiedName_layouts_LAYOUTLIST_params_ModuleParameters_layouts_LAYOUTLIST_imports_iter_star_seps__Import__layouts_LAYOUTLIST_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[9];
      
      tmp[8] = new SeparatedListStackNode(530, 8, regular__iter_star_seps__Import__layouts_LAYOUTLIST, new NonTerminalStackNode(532, 0, "Import", null, null), new AbstractStackNode[]{new NonTerminalStackNode(534, 1, "layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[7] = new NonTerminalStackNode(528, 7, "layouts_LAYOUTLIST", null, null);
      tmp[6] = new NonTerminalStackNode(526, 6, "ModuleParameters", null, null);
      tmp[5] = new NonTerminalStackNode(524, 5, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new NonTerminalStackNode(522, 4, "QualifiedName", null, null);
      tmp[3] = new NonTerminalStackNode(520, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(518, 2, prod__lit_module__char_class___range__109_109_char_class___range__111_111_char_class___range__100_100_char_class___range__117_117_char_class___range__108_108_char_class___range__101_101_, new char[] {109,111,100,117,108,101}, null, null);
      tmp[1] = new NonTerminalStackNode(516, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(514, 0, "Tags", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Parameters_Header__tags_Tags_layouts_LAYOUTLIST_lit_module_layouts_LAYOUTLIST_name_QualifiedName_layouts_LAYOUTLIST_params_ModuleParameters_layouts_LAYOUTLIST_imports_iter_star_seps__Import__layouts_LAYOUTLIST_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__Default_Header__tags_Tags_layouts_LAYOUTLIST_lit_module_layouts_LAYOUTLIST_name_QualifiedName_layouts_LAYOUTLIST_imports_iter_star_seps__Import__layouts_LAYOUTLIST_(builder);
      
        _init_prod__Parameters_Header__tags_Tags_layouts_LAYOUTLIST_lit_module_layouts_LAYOUTLIST_name_QualifiedName_layouts_LAYOUTLIST_params_ModuleParameters_layouts_LAYOUTLIST_imports_iter_star_seps__Import__layouts_LAYOUTLIST_(builder);
      
    }
  }
	
  protected static class Commands {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__List_Commands__commands_iter_seps__Command__layouts_LAYOUTLIST_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new SeparatedListStackNode(580, 0, regular__iter_seps__Command__layouts_LAYOUTLIST, new NonTerminalStackNode(582, 0, "Command", null, null), new AbstractStackNode[]{new NonTerminalStackNode(584, 1, "layouts_LAYOUTLIST", null, null)}, true, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__List_Commands__commands_iter_seps__Command__layouts_LAYOUTLIST_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__List_Commands__commands_iter_seps__Command__layouts_LAYOUTLIST_(builder);
      
    }
  }
	
  protected static class ImportedModule {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__Renamings_ImportedModule__name_QualifiedName_layouts_LAYOUTLIST_renamings_Renamings_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode(614, 2, "Renamings", null, null);
      tmp[1] = new NonTerminalStackNode(612, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(610, 0, "QualifiedName", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Renamings_ImportedModule__name_QualifiedName_layouts_LAYOUTLIST_renamings_Renamings_, tmp);
	}
    protected static final void _init_prod__ActualsRenaming_ImportedModule__name_QualifiedName_layouts_LAYOUTLIST_actuals_ModuleActuals_layouts_LAYOUTLIST_renamings_Renamings_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(624, 4, "Renamings", null, null);
      tmp[3] = new NonTerminalStackNode(622, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(620, 2, "ModuleActuals", null, null);
      tmp[1] = new NonTerminalStackNode(618, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(616, 0, "QualifiedName", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__ActualsRenaming_ImportedModule__name_QualifiedName_layouts_LAYOUTLIST_actuals_ModuleActuals_layouts_LAYOUTLIST_renamings_Renamings_, tmp);
	}
    protected static final void _init_prod__Default_ImportedModule__name_QualifiedName_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode(626, 0, "QualifiedName", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Default_ImportedModule__name_QualifiedName_, tmp);
	}
    protected static final void _init_prod__Actuals_ImportedModule__name_QualifiedName_layouts_LAYOUTLIST_actuals_ModuleActuals_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode(632, 2, "ModuleActuals", null, null);
      tmp[1] = new NonTerminalStackNode(630, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(628, 0, "QualifiedName", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Actuals_ImportedModule__name_QualifiedName_layouts_LAYOUTLIST_actuals_ModuleActuals_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__Renamings_ImportedModule__name_QualifiedName_layouts_LAYOUTLIST_renamings_Renamings_(builder);
      
        _init_prod__ActualsRenaming_ImportedModule__name_QualifiedName_layouts_LAYOUTLIST_actuals_ModuleActuals_layouts_LAYOUTLIST_renamings_Renamings_(builder);
      
        _init_prod__Default_ImportedModule__name_QualifiedName_(builder);
      
        _init_prod__Actuals_ImportedModule__name_QualifiedName_layouts_LAYOUTLIST_actuals_ModuleActuals_(builder);
      
    }
  }
	
  protected static class Expression {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__FieldUpdate_Expression__expression_Expression_layouts_LAYOUTLIST_lit___91_layouts_LAYOUTLIST_key_Name_layouts_LAYOUTLIST_lit___61_layouts_LAYOUTLIST_replacement_Expression_layouts_LAYOUTLIST_lit___93_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[11];
      
      tmp[10] = new LiteralStackNode(1012, 10, prod__lit___93__char_class___range__93_93_, new char[] {93}, null, null);
      tmp[0] = new NonTerminalStackNode(992, 0, "Expression", null, null);
      tmp[1] = new NonTerminalStackNode(994, 1, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(996, 2, prod__lit___91__char_class___range__91_91_, new char[] {91}, null, null);
      tmp[3] = new NonTerminalStackNode(998, 3, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new NonTerminalStackNode(1000, 4, "Name", null, null);
      tmp[5] = new NonTerminalStackNode(1002, 5, "layouts_LAYOUTLIST", null, null);
      tmp[6] = new LiteralStackNode(1004, 6, prod__lit___61__char_class___range__61_61_, new char[] {61}, null, null);
      tmp[7] = new NonTerminalStackNode(1006, 7, "layouts_LAYOUTLIST", null, null);
      tmp[8] = new NonTerminalStackNode(1008, 8, "Expression", null, null);
      tmp[9] = new NonTerminalStackNode(1010, 9, "layouts_LAYOUTLIST", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__FieldUpdate_Expression__expression_Expression_layouts_LAYOUTLIST_lit___91_layouts_LAYOUTLIST_key_Name_layouts_LAYOUTLIST_lit___61_layouts_LAYOUTLIST_replacement_Expression_layouts_LAYOUTLIST_lit___93_, tmp);
	}
    protected static final void _init_prod__Range_Expression__lit___91_layouts_LAYOUTLIST_first_Expression_layouts_LAYOUTLIST_lit___46_46_layouts_LAYOUTLIST_last_Expression_layouts_LAYOUTLIST_lit___93_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[9];
      
      tmp[0] = new LiteralStackNode(714, 0, prod__lit___91__char_class___range__91_91_, new char[] {91}, null, null);
      tmp[1] = new NonTerminalStackNode(716, 1, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(718, 2, "Expression", null, null);
      tmp[3] = new NonTerminalStackNode(720, 3, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode(722, 4, prod__lit___46_46__char_class___range__46_46_char_class___range__46_46_, new char[] {46,46}, null, null);
      tmp[5] = new NonTerminalStackNode(724, 5, "layouts_LAYOUTLIST", null, null);
      tmp[6] = new NonTerminalStackNode(726, 6, "Expression", null, null);
      tmp[7] = new NonTerminalStackNode(728, 7, "layouts_LAYOUTLIST", null, null);
      tmp[8] = new LiteralStackNode(730, 8, prod__lit___93__char_class___range__93_93_, new char[] {93}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Range_Expression__lit___91_layouts_LAYOUTLIST_first_Expression_layouts_LAYOUTLIST_lit___46_46_layouts_LAYOUTLIST_last_Expression_layouts_LAYOUTLIST_lit___93_, tmp);
	}
    protected static final void _init_prod__LessThanOrEq_Expression__lhs_Expression_layouts_LAYOUTLIST_lit___60_61_layouts_LAYOUTLIST_rhs_Expression__assoc__non_assoc(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(1276, 4, "Expression", null, null);
      tmp[3] = new NonTerminalStackNode(1274, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(1272, 2, prod__lit___60_61__char_class___range__60_60_char_class___range__61_61_, new char[] {60,61}, null, null);
      tmp[1] = new NonTerminalStackNode(1270, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(1268, 0, "Expression", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__LessThanOrEq_Expression__lhs_Expression_layouts_LAYOUTLIST_lit___60_61_layouts_LAYOUTLIST_rhs_Expression__assoc__non_assoc, tmp);
	}
    protected static final void _init_prod__Or_Expression__lhs_Expression_layouts_LAYOUTLIST_lit___124_124_layouts_LAYOUTLIST_rhs_Expression__assoc__left(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(1424, 4, "Expression", null, null);
      tmp[3] = new NonTerminalStackNode(1422, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(1420, 2, prod__lit___124_124__char_class___range__124_124_char_class___range__124_124_, new char[] {124,124}, null, null);
      tmp[1] = new NonTerminalStackNode(1418, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(1416, 0, "Expression", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Or_Expression__lhs_Expression_layouts_LAYOUTLIST_lit___124_124_layouts_LAYOUTLIST_rhs_Expression__assoc__left, tmp);
	}
    protected static final void _init_prod__Bracket_Expression__lit___40_layouts_LAYOUTLIST_expression_Expression_layouts_LAYOUTLIST_lit___41__bracket(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new LiteralStackNode(776, 4, prod__lit___41__char_class___range__41_41_, new char[] {41}, null, null);
      tmp[3] = new NonTerminalStackNode(774, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(772, 2, "Expression", null, null);
      tmp[1] = new NonTerminalStackNode(770, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(768, 0, prod__lit___40__char_class___range__40_40_, new char[] {40}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Bracket_Expression__lit___40_layouts_LAYOUTLIST_expression_Expression_layouts_LAYOUTLIST_lit___41__bracket, tmp);
	}
    protected static final void _init_prod__Any_Expression__lit_any_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_generators_iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[7];
      
      tmp[0] = new LiteralStackNode(778, 0, prod__lit_any__char_class___range__97_97_char_class___range__110_110_char_class___range__121_121_, new char[] {97,110,121}, null, null);
      tmp[1] = new NonTerminalStackNode(780, 1, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(782, 2, prod__lit___40__char_class___range__40_40_, new char[] {40}, null, null);
      tmp[3] = new NonTerminalStackNode(784, 3, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new SeparatedListStackNode(786, 4, regular__iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST, new NonTerminalStackNode(788, 0, "Expression", null, null), new AbstractStackNode[]{new NonTerminalStackNode(790, 1, "layouts_LAYOUTLIST", null, null), new LiteralStackNode(792, 2, prod__lit___44__char_class___range__44_44_, new char[] {44}, null, null), new NonTerminalStackNode(794, 3, "layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[5] = new NonTerminalStackNode(796, 5, "layouts_LAYOUTLIST", null, null);
      tmp[6] = new LiteralStackNode(798, 6, prod__lit___41__char_class___range__41_41_, new char[] {41}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Any_Expression__lit_any_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_generators_iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_, tmp);
	}
    protected static final void _init_prod__Is_Expression__expression_Expression_layouts_LAYOUTLIST_lit_is_layouts_LAYOUTLIST_name_Name_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(1066, 4, "Name", null, null);
      tmp[3] = new NonTerminalStackNode(1064, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(1062, 2, prod__lit_is__char_class___range__105_105_char_class___range__115_115_, new char[] {105,115}, null, null);
      tmp[1] = new NonTerminalStackNode(1060, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(1058, 0, "Expression", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Is_Expression__expression_Expression_layouts_LAYOUTLIST_lit_is_layouts_LAYOUTLIST_name_Name_, tmp);
	}
    protected static final void _init_prod__TransitiveClosure_Expression__argument_Expression_layouts_LAYOUTLIST_conditional__lit___43__not_follow__lit___61_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[2] = new LiteralStackNode(1124, 2, prod__lit___43__char_class___range__43_43_, new char[] {43}, null, new ICompletionFilter[] {new StringFollowRestriction(new char[] {61})});
      tmp[1] = new NonTerminalStackNode(1122, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(1120, 0, "Expression", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__TransitiveClosure_Expression__argument_Expression_layouts_LAYOUTLIST_conditional__lit___43__not_follow__lit___61_, tmp);
	}
    protected static final void _init_prod__Map_Expression__lit___40_layouts_LAYOUTLIST_mappings_iter_star_seps__Mapping__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new LiteralStackNode(816, 4, prod__lit___41__char_class___range__41_41_, new char[] {41}, null, null);
      tmp[3] = new NonTerminalStackNode(814, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new SeparatedListStackNode(804, 2, regular__iter_star_seps__Mapping__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST, new NonTerminalStackNode(806, 0, "Mapping__Expression", null, null), new AbstractStackNode[]{new NonTerminalStackNode(808, 1, "layouts_LAYOUTLIST", null, null), new LiteralStackNode(810, 2, prod__lit___44__char_class___range__44_44_, new char[] {44}, null, null), new NonTerminalStackNode(812, 3, "layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[1] = new NonTerminalStackNode(802, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(800, 0, prod__lit___40__char_class___range__40_40_, new char[] {40}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Map_Expression__lit___40_layouts_LAYOUTLIST_mappings_iter_star_seps__Mapping__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_, tmp);
	}
    protected static final void _init_prod__Has_Expression__expression_Expression_layouts_LAYOUTLIST_lit_has_layouts_LAYOUTLIST_name_Name_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(1076, 4, "Name", null, null);
      tmp[3] = new NonTerminalStackNode(1074, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(1072, 2, prod__lit_has__char_class___range__104_104_char_class___range__97_97_char_class___range__115_115_, new char[] {104,97,115}, null, null);
      tmp[1] = new NonTerminalStackNode(1070, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(1068, 0, "Expression", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Has_Expression__expression_Expression_layouts_LAYOUTLIST_lit_has_layouts_LAYOUTLIST_name_Name_, tmp);
	}
    protected static final void _init_prod__Set_Expression__lit___123_layouts_LAYOUTLIST_elements_iter_star_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new LiteralStackNode(836, 4, prod__lit___125__char_class___range__125_125_, new char[] {125}, null, null);
      tmp[3] = new NonTerminalStackNode(834, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new SeparatedListStackNode(824, 2, regular__iter_star_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST, new NonTerminalStackNode(826, 0, "Expression", null, null), new AbstractStackNode[]{new NonTerminalStackNode(828, 1, "layouts_LAYOUTLIST", null, null), new LiteralStackNode(830, 2, prod__lit___44__char_class___range__44_44_, new char[] {44}, null, null), new NonTerminalStackNode(832, 3, "layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[1] = new NonTerminalStackNode(822, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(820, 0, prod__lit___123__char_class___range__123_123_, new char[] {123}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Set_Expression__lit___123_layouts_LAYOUTLIST_elements_iter_star_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_, tmp);
	}
    protected static final void _init_prod__All_Expression__lit_all_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_generators_iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[7];
      
      tmp[6] = new LiteralStackNode(858, 6, prod__lit___41__char_class___range__41_41_, new char[] {41}, null, null);
      tmp[5] = new NonTerminalStackNode(856, 5, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new SeparatedListStackNode(846, 4, regular__iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST, new NonTerminalStackNode(848, 0, "Expression", null, null), new AbstractStackNode[]{new NonTerminalStackNode(850, 1, "layouts_LAYOUTLIST", null, null), new LiteralStackNode(852, 2, prod__lit___44__char_class___range__44_44_, new char[] {44}, null, null), new NonTerminalStackNode(854, 3, "layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[3] = new NonTerminalStackNode(844, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(842, 2, prod__lit___40__char_class___range__40_40_, new char[] {40}, null, null);
      tmp[1] = new NonTerminalStackNode(840, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(838, 0, prod__lit_all__char_class___range__97_97_char_class___range__108_108_char_class___range__108_108_, new char[] {97,108,108}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__All_Expression__lit_all_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_generators_iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_, tmp);
	}
    protected static final void _init_prod__Equals_Expression__lhs_Expression_layouts_LAYOUTLIST_lit___61_61_layouts_LAYOUTLIST_rhs_Expression__assoc__non_assoc(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(1344, 4, "Expression", null, null);
      tmp[3] = new NonTerminalStackNode(1342, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(1340, 2, prod__lit___61_61__char_class___range__61_61_char_class___range__61_61_, new char[] {61,61}, null, null);
      tmp[1] = new NonTerminalStackNode(1338, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(1336, 0, "Expression", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Equals_Expression__lhs_Expression_layouts_LAYOUTLIST_lit___61_61_layouts_LAYOUTLIST_rhs_Expression__assoc__non_assoc, tmp);
	}
    protected static final void _init_prod__ReifyType_Expression__lit___35_layouts_LAYOUTLIST_type_Type_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode(864, 2, "Type", null, null);
      tmp[1] = new NonTerminalStackNode(862, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(860, 0, prod__lit___35__char_class___range__35_35_, new char[] {35}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__ReifyType_Expression__lit___35_layouts_LAYOUTLIST_type_Type_, tmp);
	}
    protected static final void _init_prod__AsType_Expression__lit___91_layouts_LAYOUTLIST_type_Type_layouts_LAYOUTLIST_lit___93_layouts_LAYOUTLIST_argument_Expression_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[7];
      
      tmp[6] = new NonTerminalStackNode(1118, 6, "Expression", null, null);
      tmp[5] = new NonTerminalStackNode(1116, 5, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode(1114, 4, prod__lit___93__char_class___range__93_93_, new char[] {93}, null, null);
      tmp[3] = new NonTerminalStackNode(1112, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(1110, 2, "Type", null, null);
      tmp[1] = new NonTerminalStackNode(1108, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(1106, 0, prod__lit___91__char_class___range__91_91_, new char[] {91}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__AsType_Expression__lit___91_layouts_LAYOUTLIST_type_Type_layouts_LAYOUTLIST_lit___93_layouts_LAYOUTLIST_argument_Expression_, tmp);
	}
    protected static final void _init_prod__Reducer_Expression__lit___40_layouts_LAYOUTLIST_init_Expression_layouts_LAYOUTLIST_lit___124_layouts_LAYOUTLIST_result_Expression_layouts_LAYOUTLIST_lit___124_layouts_LAYOUTLIST_generators_iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[13];
      
      tmp[12] = new LiteralStackNode(912, 12, prod__lit___41__char_class___range__41_41_, new char[] {41}, null, null);
      tmp[11] = new NonTerminalStackNode(910, 11, "layouts_LAYOUTLIST", null, null);
      tmp[10] = new SeparatedListStackNode(900, 10, regular__iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST, new NonTerminalStackNode(902, 0, "Expression", null, null), new AbstractStackNode[]{new NonTerminalStackNode(904, 1, "layouts_LAYOUTLIST", null, null), new LiteralStackNode(906, 2, prod__lit___44__char_class___range__44_44_, new char[] {44}, null, null), new NonTerminalStackNode(908, 3, "layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[9] = new NonTerminalStackNode(898, 9, "layouts_LAYOUTLIST", null, null);
      tmp[8] = new LiteralStackNode(896, 8, prod__lit___124__char_class___range__124_124_, new char[] {124}, null, null);
      tmp[7] = new NonTerminalStackNode(894, 7, "layouts_LAYOUTLIST", null, null);
      tmp[6] = new NonTerminalStackNode(892, 6, "Expression", null, null);
      tmp[5] = new NonTerminalStackNode(890, 5, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode(888, 4, prod__lit___124__char_class___range__124_124_, new char[] {124}, null, null);
      tmp[3] = new NonTerminalStackNode(886, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(884, 2, "Expression", null, null);
      tmp[1] = new NonTerminalStackNode(882, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(880, 0, prod__lit___40__char_class___range__40_40_, new char[] {40}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Reducer_Expression__lit___40_layouts_LAYOUTLIST_init_Expression_layouts_LAYOUTLIST_lit___124_layouts_LAYOUTLIST_result_Expression_layouts_LAYOUTLIST_lit___124_layouts_LAYOUTLIST_generators_iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_, tmp);
	}
    protected static final void _init_prod__Closure_Expression__type_Type_layouts_LAYOUTLIST_parameters_Parameters_layouts_LAYOUTLIST_lit___123_layouts_LAYOUTLIST_statements_iter_seps__Statement__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[9];
      
      tmp[8] = new LiteralStackNode(934, 8, prod__lit___125__char_class___range__125_125_, new char[] {125}, null, null);
      tmp[7] = new NonTerminalStackNode(932, 7, "layouts_LAYOUTLIST", null, null);
      tmp[6] = new SeparatedListStackNode(926, 6, regular__iter_seps__Statement__layouts_LAYOUTLIST, new NonTerminalStackNode(928, 0, "Statement", null, null), new AbstractStackNode[]{new NonTerminalStackNode(930, 1, "layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[5] = new NonTerminalStackNode(924, 5, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode(922, 4, prod__lit___123__char_class___range__123_123_, new char[] {123}, null, null);
      tmp[3] = new NonTerminalStackNode(920, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(918, 2, "Parameters", null, null);
      tmp[1] = new NonTerminalStackNode(916, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(914, 0, "Type", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Closure_Expression__type_Type_layouts_LAYOUTLIST_parameters_Parameters_layouts_LAYOUTLIST_lit___123_layouts_LAYOUTLIST_statements_iter_seps__Statement__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_, tmp);
	}
    protected static final void _init_prod__Intersection_Expression__lhs_Expression_layouts_LAYOUTLIST_lit___38_layouts_LAYOUTLIST_rhs_Expression__assoc__left(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(1226, 4, "Expression", null, null);
      tmp[3] = new NonTerminalStackNode(1224, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(1222, 2, prod__lit___38__char_class___range__38_38_, new char[] {38}, null, null);
      tmp[1] = new NonTerminalStackNode(1220, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(1218, 0, "Expression", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Intersection_Expression__lhs_Expression_layouts_LAYOUTLIST_lit___38_layouts_LAYOUTLIST_rhs_Expression__assoc__left, tmp);
	}
    protected static final void _init_prod__Visit_Expression__label_Label_layouts_LAYOUTLIST_visit_Visit_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode(942, 2, "Visit", null, null);
      tmp[1] = new NonTerminalStackNode(940, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(938, 0, "Label", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Visit_Expression__label_Label_layouts_LAYOUTLIST_visit_Visit_, tmp);
	}
    protected static final void _init_prod__And_Expression__lhs_Expression_layouts_LAYOUTLIST_lit___38_38_layouts_LAYOUTLIST_rhs_Expression__assoc__left(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(1414, 4, "Expression", null, null);
      tmp[3] = new NonTerminalStackNode(1412, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(1410, 2, prod__lit___38_38__char_class___range__38_38_char_class___range__38_38_, new char[] {38,38}, null, null);
      tmp[1] = new NonTerminalStackNode(1408, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(1406, 0, "Expression", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__And_Expression__lhs_Expression_layouts_LAYOUTLIST_lit___38_38_layouts_LAYOUTLIST_rhs_Expression__assoc__left, tmp);
	}
    protected static final void _init_prod__Join_Expression__lhs_Expression_layouts_LAYOUTLIST_lit_join_layouts_LAYOUTLIST_rhs_Expression__assoc__left(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(1186, 4, "Expression", null, null);
      tmp[3] = new NonTerminalStackNode(1184, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(1182, 2, prod__lit_join__char_class___range__106_106_char_class___range__111_111_char_class___range__105_105_char_class___range__110_110_, new char[] {106,111,105,110}, null, null);
      tmp[1] = new NonTerminalStackNode(1180, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(1178, 0, "Expression", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Join_Expression__lhs_Expression_layouts_LAYOUTLIST_lit_join_layouts_LAYOUTLIST_rhs_Expression__assoc__left, tmp);
	}
    protected static final void _init_prod__Tuple_Expression__lit___60_layouts_LAYOUTLIST_elements_iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___62_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new LiteralStackNode(962, 4, prod__lit___62__char_class___range__62_62_, new char[] {62}, null, null);
      tmp[3] = new NonTerminalStackNode(960, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new SeparatedListStackNode(950, 2, regular__iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST, new NonTerminalStackNode(952, 0, "Expression", null, null), new AbstractStackNode[]{new NonTerminalStackNode(954, 1, "layouts_LAYOUTLIST", null, null), new LiteralStackNode(956, 2, prod__lit___44__char_class___range__44_44_, new char[] {44}, null, null), new NonTerminalStackNode(958, 3, "layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[1] = new NonTerminalStackNode(948, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(946, 0, prod__lit___60__char_class___range__60_60_, new char[] {60}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Tuple_Expression__lit___60_layouts_LAYOUTLIST_elements_iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___62_, tmp);
	}
    protected static final void _init_prod__In_Expression__lhs_Expression_layouts_LAYOUTLIST_lit_in_layouts_LAYOUTLIST_rhs_Expression__assoc__non_assoc(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(1266, 4, "Expression", null, null);
      tmp[3] = new NonTerminalStackNode(1264, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(1262, 2, prod__lit_in__char_class___range__105_105_char_class___range__110_110_, new char[] {105,110}, null, null);
      tmp[1] = new NonTerminalStackNode(1260, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(1258, 0, "Expression", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__In_Expression__lhs_Expression_layouts_LAYOUTLIST_lit_in_layouts_LAYOUTLIST_rhs_Expression__assoc__non_assoc, tmp);
	}
    protected static final void _init_prod__StepRange_Expression__lit___91_layouts_LAYOUTLIST_first_Expression_layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_second_Expression_layouts_LAYOUTLIST_lit___46_46_layouts_LAYOUTLIST_last_Expression_layouts_LAYOUTLIST_lit___93_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[13];
      
      tmp[12] = new LiteralStackNode(988, 12, prod__lit___93__char_class___range__93_93_, new char[] {93}, null, null);
      tmp[11] = new NonTerminalStackNode(986, 11, "layouts_LAYOUTLIST", null, null);
      tmp[10] = new NonTerminalStackNode(984, 10, "Expression", null, null);
      tmp[9] = new NonTerminalStackNode(982, 9, "layouts_LAYOUTLIST", null, null);
      tmp[8] = new LiteralStackNode(980, 8, prod__lit___46_46__char_class___range__46_46_char_class___range__46_46_, new char[] {46,46}, null, null);
      tmp[7] = new NonTerminalStackNode(978, 7, "layouts_LAYOUTLIST", null, null);
      tmp[6] = new NonTerminalStackNode(976, 6, "Expression", null, null);
      tmp[5] = new NonTerminalStackNode(974, 5, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode(972, 4, prod__lit___44__char_class___range__44_44_, new char[] {44}, null, null);
      tmp[3] = new NonTerminalStackNode(970, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(968, 2, "Expression", null, null);
      tmp[1] = new NonTerminalStackNode(966, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(964, 0, prod__lit___91__char_class___range__91_91_, new char[] {91}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__StepRange_Expression__lit___91_layouts_LAYOUTLIST_first_Expression_layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_second_Expression_layouts_LAYOUTLIST_lit___46_46_layouts_LAYOUTLIST_last_Expression_layouts_LAYOUTLIST_lit___93_, tmp);
	}
    protected static final void _init_prod__Match_Expression__pattern_Pattern_layouts_LAYOUTLIST_lit___58_61_layouts_LAYOUTLIST_expression_Expression__assoc__non_assoc(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(1364, 4, "Expression", null, null);
      tmp[3] = new NonTerminalStackNode(1362, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(1360, 2, prod__lit___58_61__char_class___range__58_58_char_class___range__61_61_, new char[] {58,61}, null, null);
      tmp[1] = new NonTerminalStackNode(1358, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(1356, 0, "Pattern", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Match_Expression__pattern_Pattern_layouts_LAYOUTLIST_lit___58_61_layouts_LAYOUTLIST_expression_Expression__assoc__non_assoc, tmp);
	}
    protected static final void _init_prod__ReifiedType_Expression__basicType_BasicType_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_arguments_iter_star_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[7];
      
      tmp[6] = new LiteralStackNode(690, 6, prod__lit___41__char_class___range__41_41_, new char[] {41}, null, null);
      tmp[5] = new NonTerminalStackNode(688, 5, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new SeparatedListStackNode(678, 4, regular__iter_star_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST, new NonTerminalStackNode(680, 0, "Expression", null, null), new AbstractStackNode[]{new NonTerminalStackNode(682, 1, "layouts_LAYOUTLIST", null, null), new LiteralStackNode(684, 2, prod__lit___44__char_class___range__44_44_, new char[] {44}, null, null), new NonTerminalStackNode(686, 3, "layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[3] = new NonTerminalStackNode(676, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(674, 2, prod__lit___40__char_class___range__40_40_, new char[] {40}, null, null);
      tmp[1] = new NonTerminalStackNode(672, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(670, 0, "BasicType", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__ReifiedType_Expression__basicType_BasicType_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_arguments_iter_star_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_, tmp);
	}
    protected static final void _init_prod__CallOrTree_Expression__expression_Expression_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_arguments_iter_star_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[7];
      
      tmp[6] = new LiteralStackNode(712, 6, prod__lit___41__char_class___range__41_41_, new char[] {41}, null, null);
      tmp[5] = new NonTerminalStackNode(710, 5, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new SeparatedListStackNode(700, 4, regular__iter_star_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST, new NonTerminalStackNode(702, 0, "Expression", null, null), new AbstractStackNode[]{new NonTerminalStackNode(704, 1, "layouts_LAYOUTLIST", null, null), new LiteralStackNode(706, 2, prod__lit___44__char_class___range__44_44_, new char[] {44}, null, null), new NonTerminalStackNode(708, 3, "layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[3] = new NonTerminalStackNode(698, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(696, 2, prod__lit___40__char_class___range__40_40_, new char[] {40}, null, null);
      tmp[1] = new NonTerminalStackNode(694, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(692, 0, "Expression", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__CallOrTree_Expression__expression_Expression_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_arguments_iter_star_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_, tmp);
	}
    protected static final void _init_prod__Negation_Expression__lit___33_layouts_LAYOUTLIST_argument_Expression_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode(1098, 2, "Expression", null, null);
      tmp[1] = new NonTerminalStackNode(1096, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(1094, 0, prod__lit___33__char_class___range__33_33_, new char[] {33}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Negation_Expression__lit___33_layouts_LAYOUTLIST_argument_Expression_, tmp);
	}
    protected static final void _init_prod__List_Expression__lit___91_layouts_LAYOUTLIST_elements_iter_star_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___93_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new LiteralStackNode(748, 4, prod__lit___93__char_class___range__93_93_, new char[] {93}, null, null);
      tmp[3] = new NonTerminalStackNode(746, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new SeparatedListStackNode(736, 2, regular__iter_star_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST, new NonTerminalStackNode(738, 0, "Expression", null, null), new AbstractStackNode[]{new NonTerminalStackNode(740, 1, "layouts_LAYOUTLIST", null, null), new LiteralStackNode(742, 2, prod__lit___44__char_class___range__44_44_, new char[] {44}, null, null), new NonTerminalStackNode(744, 3, "layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[1] = new NonTerminalStackNode(734, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(732, 0, prod__lit___91__char_class___range__91_91_, new char[] {91}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__List_Expression__lit___91_layouts_LAYOUTLIST_elements_iter_star_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___93_, tmp);
	}
    protected static final void _init_prod__VoidClosure_Expression__parameters_Parameters_layouts_LAYOUTLIST_lit___123_layouts_LAYOUTLIST_statements_iter_star_seps__Statement__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[7];
      
      tmp[6] = new LiteralStackNode(766, 6, prod__lit___125__char_class___range__125_125_, new char[] {125}, null, null);
      tmp[5] = new NonTerminalStackNode(764, 5, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new SeparatedListStackNode(758, 4, regular__iter_star_seps__Statement__layouts_LAYOUTLIST, new NonTerminalStackNode(760, 0, "Statement", null, null), new AbstractStackNode[]{new NonTerminalStackNode(762, 1, "layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[3] = new NonTerminalStackNode(756, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(754, 2, prod__lit___123__char_class___range__123_123_, new char[] {123}, null, null);
      tmp[1] = new NonTerminalStackNode(752, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(750, 0, "Parameters", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__VoidClosure_Expression__parameters_Parameters_layouts_LAYOUTLIST_lit___123_layouts_LAYOUTLIST_statements_iter_star_seps__Statement__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_, tmp);
	}
    protected static final void _init_prod__Subscript_Expression__expression_Expression_layouts_LAYOUTLIST_lit___91_layouts_LAYOUTLIST_subscripts_iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___93_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[7];
      
      tmp[6] = new LiteralStackNode(1034, 6, prod__lit___93__char_class___range__93_93_, new char[] {93}, null, null);
      tmp[5] = new NonTerminalStackNode(1032, 5, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new SeparatedListStackNode(1022, 4, regular__iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST, new NonTerminalStackNode(1024, 0, "Expression", null, null), new AbstractStackNode[]{new NonTerminalStackNode(1026, 1, "layouts_LAYOUTLIST", null, null), new LiteralStackNode(1028, 2, prod__lit___44__char_class___range__44_44_, new char[] {44}, null, null), new NonTerminalStackNode(1030, 3, "layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[3] = new NonTerminalStackNode(1020, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(1018, 2, prod__lit___91__char_class___range__91_91_, new char[] {91}, null, null);
      tmp[1] = new NonTerminalStackNode(1016, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(1014, 0, "Expression", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Subscript_Expression__expression_Expression_layouts_LAYOUTLIST_lit___91_layouts_LAYOUTLIST_subscripts_iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___93_, tmp);
	}
    protected static final void _init_prod__Subtraction_Expression__lhs_Expression_layouts_LAYOUTLIST_lit___layouts_LAYOUTLIST_rhs_Expression__assoc__left(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(1236, 4, "Expression", null, null);
      tmp[3] = new NonTerminalStackNode(1234, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(1232, 2, prod__lit____char_class___range__45_45_, new char[] {45}, null, null);
      tmp[1] = new NonTerminalStackNode(1230, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(1228, 0, "Expression", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Subtraction_Expression__lhs_Expression_layouts_LAYOUTLIST_lit___layouts_LAYOUTLIST_rhs_Expression__assoc__left, tmp);
	}
    protected static final void _init_prod__NotIn_Expression__lhs_Expression_layouts_LAYOUTLIST_lit_notin_layouts_LAYOUTLIST_rhs_Expression__assoc__non_assoc(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(1256, 4, "Expression", null, null);
      tmp[3] = new NonTerminalStackNode(1254, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(1252, 2, prod__lit_notin__char_class___range__110_110_char_class___range__111_111_char_class___range__116_116_char_class___range__105_105_char_class___range__110_110_, new char[] {110,111,116,105,110}, null, null);
      tmp[1] = new NonTerminalStackNode(1250, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(1248, 0, "Expression", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__NotIn_Expression__lhs_Expression_layouts_LAYOUTLIST_lit_notin_layouts_LAYOUTLIST_rhs_Expression__assoc__non_assoc, tmp);
	}
    protected static final void _init_prod__GreaterThan_Expression__lhs_Expression_layouts_LAYOUTLIST_lit___62_layouts_LAYOUTLIST_rhs_Expression__assoc__non_assoc(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(1296, 4, "Expression", null, null);
      tmp[3] = new NonTerminalStackNode(1294, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(1292, 2, prod__lit___62__char_class___range__62_62_, new char[] {62}, null, null);
      tmp[1] = new NonTerminalStackNode(1290, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(1288, 0, "Expression", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__GreaterThan_Expression__lhs_Expression_layouts_LAYOUTLIST_lit___62_layouts_LAYOUTLIST_rhs_Expression__assoc__non_assoc, tmp);
	}
    protected static final void _init_prod__Enumerator_Expression__pattern_Pattern_layouts_LAYOUTLIST_lit___60_45_layouts_LAYOUTLIST_expression_Expression__assoc__non_assoc(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(1374, 4, "Expression", null, null);
      tmp[3] = new NonTerminalStackNode(1372, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(1370, 2, prod__lit___60_45__char_class___range__60_60_char_class___range__45_45_, new char[] {60,45}, null, null);
      tmp[1] = new NonTerminalStackNode(1368, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(1366, 0, "Pattern", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Enumerator_Expression__pattern_Pattern_layouts_LAYOUTLIST_lit___60_45_layouts_LAYOUTLIST_expression_Expression__assoc__non_assoc, tmp);
	}
    protected static final void _init_prod__GreaterThanOrEq_Expression__lhs_Expression_layouts_LAYOUTLIST_lit___62_61_layouts_LAYOUTLIST_rhs_Expression__assoc__non_assoc(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(1306, 4, "Expression", null, null);
      tmp[3] = new NonTerminalStackNode(1304, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(1302, 2, prod__lit___62_61__char_class___range__62_62_char_class___range__61_61_, new char[] {62,61}, null, null);
      tmp[1] = new NonTerminalStackNode(1300, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(1298, 0, "Expression", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__GreaterThanOrEq_Expression__lhs_Expression_layouts_LAYOUTLIST_lit___62_61_layouts_LAYOUTLIST_rhs_Expression__assoc__non_assoc, tmp);
	}
    protected static final void _init_prod__Modulo_Expression__lhs_Expression_layouts_LAYOUTLIST_lit___37_layouts_LAYOUTLIST_rhs_Expression__assoc__left(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(1206, 4, "Expression", null, null);
      tmp[3] = new NonTerminalStackNode(1204, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(1202, 2, prod__lit___37__char_class___range__37_37_, new char[] {37}, null, null);
      tmp[1] = new NonTerminalStackNode(1200, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(1198, 0, "Expression", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Modulo_Expression__lhs_Expression_layouts_LAYOUTLIST_lit___37_layouts_LAYOUTLIST_rhs_Expression__assoc__left, tmp);
	}
    protected static final void _init_prod__Equivalence_Expression__lhs_Expression_layouts_LAYOUTLIST_lit___60_61_61_62_layouts_LAYOUTLIST_rhs_Expression__assoc__non_assoc(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(1404, 4, "Expression", null, null);
      tmp[3] = new NonTerminalStackNode(1402, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(1400, 2, prod__lit___60_61_61_62__char_class___range__60_60_char_class___range__61_61_char_class___range__61_61_char_class___range__62_62_, new char[] {60,61,61,62}, null, null);
      tmp[1] = new NonTerminalStackNode(1398, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(1396, 0, "Expression", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Equivalence_Expression__lhs_Expression_layouts_LAYOUTLIST_lit___60_61_61_62_layouts_LAYOUTLIST_rhs_Expression__assoc__non_assoc, tmp);
	}
    protected static final void _init_prod__GetAnnotation_Expression__expression_Expression_layouts_LAYOUTLIST_lit___64_layouts_LAYOUTLIST_name_Name_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(1140, 4, "Name", null, null);
      tmp[3] = new NonTerminalStackNode(1138, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(1136, 2, prod__lit___64__char_class___range__64_64_, new char[] {64}, null, null);
      tmp[1] = new NonTerminalStackNode(1134, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(1132, 0, "Expression", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__GetAnnotation_Expression__expression_Expression_layouts_LAYOUTLIST_lit___64_layouts_LAYOUTLIST_name_Name_, tmp);
	}
    protected static final void _init_prod__TransitiveReflexiveClosure_Expression__argument_Expression_layouts_LAYOUTLIST_conditional__lit___42__not_follow__lit___61_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[0] = new NonTerminalStackNode(1126, 0, "Expression", null, null);
      tmp[1] = new NonTerminalStackNode(1128, 1, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(1130, 2, prod__lit___42__char_class___range__42_42_, new char[] {42}, null, new ICompletionFilter[] {new StringFollowRestriction(new char[] {61})});
      builder.addAlternative(ObjectRascalRascal.prod__TransitiveReflexiveClosure_Expression__argument_Expression_layouts_LAYOUTLIST_conditional__lit___42__not_follow__lit___61_, tmp);
	}
    protected static final void _init_prod__QualifiedName_Expression__qualifiedName_QualifiedName_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode(818, 0, "QualifiedName", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__QualifiedName_Expression__qualifiedName_QualifiedName_, tmp);
	}
    protected static final void _init_prod__Addition_Expression__lhs_Expression_layouts_LAYOUTLIST_lit___43_layouts_LAYOUTLIST_rhs_Expression__assoc__left(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(1246, 4, "Expression", null, null);
      tmp[3] = new NonTerminalStackNode(1244, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(1242, 2, prod__lit___43__char_class___range__43_43_, new char[] {43}, null, null);
      tmp[1] = new NonTerminalStackNode(1240, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(1238, 0, "Expression", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Addition_Expression__lhs_Expression_layouts_LAYOUTLIST_lit___43_layouts_LAYOUTLIST_rhs_Expression__assoc__left, tmp);
	}
    protected static final void _init_prod__NoMatch_Expression__pattern_Pattern_layouts_LAYOUTLIST_lit___33_58_61_layouts_LAYOUTLIST_expression_Expression__assoc__non_assoc(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(1384, 4, "Expression", null, null);
      tmp[3] = new NonTerminalStackNode(1382, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(1380, 2, prod__lit___33_58_61__char_class___range__33_33_char_class___range__58_58_char_class___range__61_61_, new char[] {33,58,61}, null, null);
      tmp[1] = new NonTerminalStackNode(1378, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(1376, 0, "Pattern", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__NoMatch_Expression__pattern_Pattern_layouts_LAYOUTLIST_lit___33_58_61_layouts_LAYOUTLIST_expression_Expression__assoc__non_assoc, tmp);
	}
    protected static final void _init_prod__IfThenElse_Expression__condition_Expression_layouts_LAYOUTLIST_lit___63_layouts_LAYOUTLIST_thenExp_Expression_layouts_LAYOUTLIST_lit___58_layouts_LAYOUTLIST_elseExp_Expression__assoc__left(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[9];
      
      tmp[8] = new NonTerminalStackNode(1324, 8, "Expression", null, null);
      tmp[7] = new NonTerminalStackNode(1322, 7, "layouts_LAYOUTLIST", null, null);
      tmp[6] = new LiteralStackNode(1320, 6, prod__lit___58__char_class___range__58_58_, new char[] {58}, null, null);
      tmp[5] = new NonTerminalStackNode(1318, 5, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new NonTerminalStackNode(1316, 4, "Expression", null, null);
      tmp[3] = new NonTerminalStackNode(1314, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(1312, 2, prod__lit___63__char_class___range__63_63_, new char[] {63}, null, null);
      tmp[1] = new NonTerminalStackNode(1310, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(1308, 0, "Expression", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__IfThenElse_Expression__condition_Expression_layouts_LAYOUTLIST_lit___63_layouts_LAYOUTLIST_thenExp_Expression_layouts_LAYOUTLIST_lit___58_layouts_LAYOUTLIST_elseExp_Expression__assoc__left, tmp);
	}
    protected static final void _init_prod__SetAnnotation_Expression__expression_Expression_layouts_LAYOUTLIST_lit___91_layouts_LAYOUTLIST_lit___64_layouts_LAYOUTLIST_name_Name_layouts_LAYOUTLIST_lit___61_layouts_LAYOUTLIST_value_Expression_layouts_LAYOUTLIST_lit___93_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[13];
      
      tmp[12] = new LiteralStackNode(1166, 12, prod__lit___93__char_class___range__93_93_, new char[] {93}, null, null);
      tmp[11] = new NonTerminalStackNode(1164, 11, "layouts_LAYOUTLIST", null, null);
      tmp[10] = new NonTerminalStackNode(1162, 10, "Expression", null, null);
      tmp[9] = new NonTerminalStackNode(1160, 9, "layouts_LAYOUTLIST", null, null);
      tmp[8] = new LiteralStackNode(1158, 8, prod__lit___61__char_class___range__61_61_, new char[] {61}, null, null);
      tmp[7] = new NonTerminalStackNode(1156, 7, "layouts_LAYOUTLIST", null, null);
      tmp[6] = new NonTerminalStackNode(1154, 6, "Name", null, null);
      tmp[5] = new NonTerminalStackNode(1152, 5, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode(1150, 4, prod__lit___64__char_class___range__64_64_, new char[] {64}, null, null);
      tmp[3] = new NonTerminalStackNode(1148, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(1146, 2, prod__lit___91__char_class___range__91_91_, new char[] {91}, null, null);
      tmp[1] = new NonTerminalStackNode(1144, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(1142, 0, "Expression", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__SetAnnotation_Expression__expression_Expression_layouts_LAYOUTLIST_lit___91_layouts_LAYOUTLIST_lit___64_layouts_LAYOUTLIST_name_Name_layouts_LAYOUTLIST_lit___61_layouts_LAYOUTLIST_value_Expression_layouts_LAYOUTLIST_lit___93_, tmp);
	}
    protected static final void _init_prod__FieldAccess_Expression__expression_Expression_layouts_LAYOUTLIST_lit___46_layouts_LAYOUTLIST_field_Name_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(1086, 4, "Name", null, null);
      tmp[3] = new NonTerminalStackNode(1084, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(1082, 2, prod__lit___46__char_class___range__46_46_, new char[] {46}, null, null);
      tmp[1] = new NonTerminalStackNode(1080, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(1078, 0, "Expression", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__FieldAccess_Expression__expression_Expression_layouts_LAYOUTLIST_lit___46_layouts_LAYOUTLIST_field_Name_, tmp);
	}
    protected static final void _init_prod__NonEquals_Expression__lhs_Expression_layouts_LAYOUTLIST_lit___33_61_layouts_LAYOUTLIST_rhs_Expression__assoc__non_assoc(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(1334, 4, "Expression", null, null);
      tmp[3] = new NonTerminalStackNode(1332, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(1330, 2, prod__lit___33_61__char_class___range__33_33_char_class___range__61_61_, new char[] {33,61}, null, null);
      tmp[1] = new NonTerminalStackNode(1328, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(1326, 0, "Expression", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__NonEquals_Expression__lhs_Expression_layouts_LAYOUTLIST_lit___33_61_layouts_LAYOUTLIST_rhs_Expression__assoc__non_assoc, tmp);
	}
    protected static final void _init_prod__Composition_Expression__lhs_Expression_layouts_LAYOUTLIST_lit_o_layouts_LAYOUTLIST_rhs_Expression__assoc__left(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(1176, 4, "Expression", null, null);
      tmp[3] = new NonTerminalStackNode(1174, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(1172, 2, prod__lit_o__char_class___range__111_111_, new char[] {111}, null, null);
      tmp[1] = new NonTerminalStackNode(1170, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(1168, 0, "Expression", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Composition_Expression__lhs_Expression_layouts_LAYOUTLIST_lit_o_layouts_LAYOUTLIST_rhs_Expression__assoc__left, tmp);
	}
    protected static final void _init_prod__Implication_Expression__lhs_Expression_layouts_LAYOUTLIST_lit___61_61_62_layouts_LAYOUTLIST_rhs_Expression__assoc__non_assoc(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(1394, 4, "Expression", null, null);
      tmp[3] = new NonTerminalStackNode(1392, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(1390, 2, prod__lit___61_61_62__char_class___range__61_61_char_class___range__61_61_char_class___range__62_62_, new char[] {61,61,62}, null, null);
      tmp[1] = new NonTerminalStackNode(1388, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(1386, 0, "Expression", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Implication_Expression__lhs_Expression_layouts_LAYOUTLIST_lit___61_61_62_layouts_LAYOUTLIST_rhs_Expression__assoc__non_assoc, tmp);
	}
    protected static final void _init_prod__Negative_Expression__lit___layouts_LAYOUTLIST_argument_Expression_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode(1104, 2, "Expression", null, null);
      tmp[1] = new NonTerminalStackNode(1102, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(1100, 0, prod__lit____char_class___range__45_45_, new char[] {45}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Negative_Expression__lit___layouts_LAYOUTLIST_argument_Expression_, tmp);
	}
    protected static final void _init_prod__LessThan_Expression__lhs_Expression_layouts_LAYOUTLIST_conditional__lit___60__not_follow__lit___layouts_LAYOUTLIST_rhs_Expression__assoc__non_assoc(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(1286, 4, "Expression", null, null);
      tmp[3] = new NonTerminalStackNode(1284, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(1282, 2, prod__lit___60__char_class___range__60_60_, new char[] {60}, null, new ICompletionFilter[] {new StringFollowRestriction(new char[] {45})});
      tmp[1] = new NonTerminalStackNode(1280, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(1278, 0, "Expression", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__LessThan_Expression__lhs_Expression_layouts_LAYOUTLIST_conditional__lit___60__not_follow__lit___layouts_LAYOUTLIST_rhs_Expression__assoc__non_assoc, tmp);
	}
    protected static final void _init_prod__NonEmptyBlock_Expression__lit___123_layouts_LAYOUTLIST_statements_iter_seps__Statement__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new LiteralStackNode(878, 4, prod__lit___125__char_class___range__125_125_, new char[] {125}, null, null);
      tmp[3] = new NonTerminalStackNode(876, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new SeparatedListStackNode(870, 2, regular__iter_seps__Statement__layouts_LAYOUTLIST, new NonTerminalStackNode(872, 0, "Statement", null, null), new AbstractStackNode[]{new NonTerminalStackNode(874, 1, "layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[1] = new NonTerminalStackNode(868, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(866, 0, prod__lit___123__char_class___range__123_123_, new char[] {123}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__NonEmptyBlock_Expression__lit___123_layouts_LAYOUTLIST_statements_iter_seps__Statement__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_, tmp);
	}
    protected static final void _init_prod__IfDefinedOtherwise_Expression__lhs_Expression_layouts_LAYOUTLIST_lit___63_layouts_LAYOUTLIST_rhs_Expression__assoc__non_assoc(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(1354, 4, "Expression", null, null);
      tmp[3] = new NonTerminalStackNode(1352, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(1350, 2, prod__lit___63__char_class___range__63_63_, new char[] {63}, null, null);
      tmp[1] = new NonTerminalStackNode(1348, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(1346, 0, "Expression", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__IfDefinedOtherwise_Expression__lhs_Expression_layouts_LAYOUTLIST_lit___63_layouts_LAYOUTLIST_rhs_Expression__assoc__non_assoc, tmp);
	}
    protected static final void _init_prod__FieldProject_Expression__expression_Expression_layouts_LAYOUTLIST_lit___60_layouts_LAYOUTLIST_fields_iter_seps__Field__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___62_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[7];
      
      tmp[6] = new LiteralStackNode(1056, 6, prod__lit___62__char_class___range__62_62_, new char[] {62}, null, null);
      tmp[5] = new NonTerminalStackNode(1054, 5, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new SeparatedListStackNode(1044, 4, regular__iter_seps__Field__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST, new NonTerminalStackNode(1046, 0, "Field", null, null), new AbstractStackNode[]{new NonTerminalStackNode(1048, 1, "layouts_LAYOUTLIST", null, null), new LiteralStackNode(1050, 2, prod__lit___44__char_class___range__44_44_, new char[] {44}, null, null), new NonTerminalStackNode(1052, 3, "layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[3] = new NonTerminalStackNode(1042, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(1040, 2, prod__lit___60__char_class___range__60_60_, new char[] {60}, null, null);
      tmp[1] = new NonTerminalStackNode(1038, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(1036, 0, "Expression", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__FieldProject_Expression__expression_Expression_layouts_LAYOUTLIST_lit___60_layouts_LAYOUTLIST_fields_iter_seps__Field__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___62_, tmp);
	}
    protected static final void _init_prod__It_Expression__lit_it_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(936, 0, prod__lit_it__char_class___range__105_105_char_class___range__116_116_, new char[] {105,116}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__It_Expression__lit_it_, tmp);
	}
    protected static final void _init_prod__Literal_Expression__literal_Literal_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode(944, 0, "Literal", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Literal_Expression__literal_Literal_, tmp);
	}
    protected static final void _init_prod__IsDefined_Expression__argument_Expression_layouts_LAYOUTLIST_lit___63_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[2] = new LiteralStackNode(1092, 2, prod__lit___63__char_class___range__63_63_, new char[] {63}, null, null);
      tmp[1] = new NonTerminalStackNode(1090, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(1088, 0, "Expression", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__IsDefined_Expression__argument_Expression_layouts_LAYOUTLIST_lit___63_, tmp);
	}
    protected static final void _init_prod__Division_Expression__lhs_Expression_layouts_LAYOUTLIST_lit___47_layouts_LAYOUTLIST_rhs_Expression__assoc__left(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(1216, 4, "Expression", null, null);
      tmp[3] = new NonTerminalStackNode(1214, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(1212, 2, prod__lit___47__char_class___range__47_47_, new char[] {47}, null, null);
      tmp[1] = new NonTerminalStackNode(1210, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(1208, 0, "Expression", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Division_Expression__lhs_Expression_layouts_LAYOUTLIST_lit___47_layouts_LAYOUTLIST_rhs_Expression__assoc__left, tmp);
	}
    protected static final void _init_prod__Comprehension_Expression__comprehension_Comprehension_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode(990, 0, "Comprehension", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Comprehension_Expression__comprehension_Comprehension_, tmp);
	}
    protected static final void _init_prod__Product_Expression__lhs_Expression_layouts_LAYOUTLIST_lit___42_layouts_LAYOUTLIST_rhs_Expression__assoc__left(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(1196, 4, "Expression", null, null);
      tmp[3] = new NonTerminalStackNode(1194, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(1192, 2, prod__lit___42__char_class___range__42_42_, new char[] {42}, null, null);
      tmp[1] = new NonTerminalStackNode(1190, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(1188, 0, "Expression", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Product_Expression__lhs_Expression_layouts_LAYOUTLIST_lit___42_layouts_LAYOUTLIST_rhs_Expression__assoc__left, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__FieldUpdate_Expression__expression_Expression_layouts_LAYOUTLIST_lit___91_layouts_LAYOUTLIST_key_Name_layouts_LAYOUTLIST_lit___61_layouts_LAYOUTLIST_replacement_Expression_layouts_LAYOUTLIST_lit___93_(builder);
      
        _init_prod__Range_Expression__lit___91_layouts_LAYOUTLIST_first_Expression_layouts_LAYOUTLIST_lit___46_46_layouts_LAYOUTLIST_last_Expression_layouts_LAYOUTLIST_lit___93_(builder);
      
        _init_prod__LessThanOrEq_Expression__lhs_Expression_layouts_LAYOUTLIST_lit___60_61_layouts_LAYOUTLIST_rhs_Expression__assoc__non_assoc(builder);
      
        _init_prod__Or_Expression__lhs_Expression_layouts_LAYOUTLIST_lit___124_124_layouts_LAYOUTLIST_rhs_Expression__assoc__left(builder);
      
        _init_prod__Bracket_Expression__lit___40_layouts_LAYOUTLIST_expression_Expression_layouts_LAYOUTLIST_lit___41__bracket(builder);
      
        _init_prod__Any_Expression__lit_any_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_generators_iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_(builder);
      
        _init_prod__Is_Expression__expression_Expression_layouts_LAYOUTLIST_lit_is_layouts_LAYOUTLIST_name_Name_(builder);
      
        _init_prod__TransitiveClosure_Expression__argument_Expression_layouts_LAYOUTLIST_conditional__lit___43__not_follow__lit___61_(builder);
      
        _init_prod__Map_Expression__lit___40_layouts_LAYOUTLIST_mappings_iter_star_seps__Mapping__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_(builder);
      
        _init_prod__Has_Expression__expression_Expression_layouts_LAYOUTLIST_lit_has_layouts_LAYOUTLIST_name_Name_(builder);
      
        _init_prod__Set_Expression__lit___123_layouts_LAYOUTLIST_elements_iter_star_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_(builder);
      
        _init_prod__All_Expression__lit_all_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_generators_iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_(builder);
      
        _init_prod__Equals_Expression__lhs_Expression_layouts_LAYOUTLIST_lit___61_61_layouts_LAYOUTLIST_rhs_Expression__assoc__non_assoc(builder);
      
        _init_prod__ReifyType_Expression__lit___35_layouts_LAYOUTLIST_type_Type_(builder);
      
        _init_prod__AsType_Expression__lit___91_layouts_LAYOUTLIST_type_Type_layouts_LAYOUTLIST_lit___93_layouts_LAYOUTLIST_argument_Expression_(builder);
      
        _init_prod__Reducer_Expression__lit___40_layouts_LAYOUTLIST_init_Expression_layouts_LAYOUTLIST_lit___124_layouts_LAYOUTLIST_result_Expression_layouts_LAYOUTLIST_lit___124_layouts_LAYOUTLIST_generators_iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_(builder);
      
        _init_prod__Closure_Expression__type_Type_layouts_LAYOUTLIST_parameters_Parameters_layouts_LAYOUTLIST_lit___123_layouts_LAYOUTLIST_statements_iter_seps__Statement__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_(builder);
      
        _init_prod__Intersection_Expression__lhs_Expression_layouts_LAYOUTLIST_lit___38_layouts_LAYOUTLIST_rhs_Expression__assoc__left(builder);
      
        _init_prod__Visit_Expression__label_Label_layouts_LAYOUTLIST_visit_Visit_(builder);
      
        _init_prod__And_Expression__lhs_Expression_layouts_LAYOUTLIST_lit___38_38_layouts_LAYOUTLIST_rhs_Expression__assoc__left(builder);
      
        _init_prod__Join_Expression__lhs_Expression_layouts_LAYOUTLIST_lit_join_layouts_LAYOUTLIST_rhs_Expression__assoc__left(builder);
      
        _init_prod__Tuple_Expression__lit___60_layouts_LAYOUTLIST_elements_iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___62_(builder);
      
        _init_prod__In_Expression__lhs_Expression_layouts_LAYOUTLIST_lit_in_layouts_LAYOUTLIST_rhs_Expression__assoc__non_assoc(builder);
      
        _init_prod__StepRange_Expression__lit___91_layouts_LAYOUTLIST_first_Expression_layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_second_Expression_layouts_LAYOUTLIST_lit___46_46_layouts_LAYOUTLIST_last_Expression_layouts_LAYOUTLIST_lit___93_(builder);
      
        _init_prod__Match_Expression__pattern_Pattern_layouts_LAYOUTLIST_lit___58_61_layouts_LAYOUTLIST_expression_Expression__assoc__non_assoc(builder);
      
        _init_prod__ReifiedType_Expression__basicType_BasicType_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_arguments_iter_star_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_(builder);
      
        _init_prod__CallOrTree_Expression__expression_Expression_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_arguments_iter_star_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_(builder);
      
        _init_prod__Negation_Expression__lit___33_layouts_LAYOUTLIST_argument_Expression_(builder);
      
        _init_prod__List_Expression__lit___91_layouts_LAYOUTLIST_elements_iter_star_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___93_(builder);
      
        _init_prod__VoidClosure_Expression__parameters_Parameters_layouts_LAYOUTLIST_lit___123_layouts_LAYOUTLIST_statements_iter_star_seps__Statement__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_(builder);
      
        _init_prod__Subscript_Expression__expression_Expression_layouts_LAYOUTLIST_lit___91_layouts_LAYOUTLIST_subscripts_iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___93_(builder);
      
        _init_prod__Subtraction_Expression__lhs_Expression_layouts_LAYOUTLIST_lit___layouts_LAYOUTLIST_rhs_Expression__assoc__left(builder);
      
        _init_prod__NotIn_Expression__lhs_Expression_layouts_LAYOUTLIST_lit_notin_layouts_LAYOUTLIST_rhs_Expression__assoc__non_assoc(builder);
      
        _init_prod__GreaterThan_Expression__lhs_Expression_layouts_LAYOUTLIST_lit___62_layouts_LAYOUTLIST_rhs_Expression__assoc__non_assoc(builder);
      
        _init_prod__Enumerator_Expression__pattern_Pattern_layouts_LAYOUTLIST_lit___60_45_layouts_LAYOUTLIST_expression_Expression__assoc__non_assoc(builder);
      
        _init_prod__GreaterThanOrEq_Expression__lhs_Expression_layouts_LAYOUTLIST_lit___62_61_layouts_LAYOUTLIST_rhs_Expression__assoc__non_assoc(builder);
      
        _init_prod__Modulo_Expression__lhs_Expression_layouts_LAYOUTLIST_lit___37_layouts_LAYOUTLIST_rhs_Expression__assoc__left(builder);
      
        _init_prod__Equivalence_Expression__lhs_Expression_layouts_LAYOUTLIST_lit___60_61_61_62_layouts_LAYOUTLIST_rhs_Expression__assoc__non_assoc(builder);
      
        _init_prod__GetAnnotation_Expression__expression_Expression_layouts_LAYOUTLIST_lit___64_layouts_LAYOUTLIST_name_Name_(builder);
      
        _init_prod__TransitiveReflexiveClosure_Expression__argument_Expression_layouts_LAYOUTLIST_conditional__lit___42__not_follow__lit___61_(builder);
      
        _init_prod__QualifiedName_Expression__qualifiedName_QualifiedName_(builder);
      
        _init_prod__Addition_Expression__lhs_Expression_layouts_LAYOUTLIST_lit___43_layouts_LAYOUTLIST_rhs_Expression__assoc__left(builder);
      
        _init_prod__NoMatch_Expression__pattern_Pattern_layouts_LAYOUTLIST_lit___33_58_61_layouts_LAYOUTLIST_expression_Expression__assoc__non_assoc(builder);
      
        _init_prod__IfThenElse_Expression__condition_Expression_layouts_LAYOUTLIST_lit___63_layouts_LAYOUTLIST_thenExp_Expression_layouts_LAYOUTLIST_lit___58_layouts_LAYOUTLIST_elseExp_Expression__assoc__left(builder);
      
        _init_prod__SetAnnotation_Expression__expression_Expression_layouts_LAYOUTLIST_lit___91_layouts_LAYOUTLIST_lit___64_layouts_LAYOUTLIST_name_Name_layouts_LAYOUTLIST_lit___61_layouts_LAYOUTLIST_value_Expression_layouts_LAYOUTLIST_lit___93_(builder);
      
        _init_prod__FieldAccess_Expression__expression_Expression_layouts_LAYOUTLIST_lit___46_layouts_LAYOUTLIST_field_Name_(builder);
      
        _init_prod__NonEquals_Expression__lhs_Expression_layouts_LAYOUTLIST_lit___33_61_layouts_LAYOUTLIST_rhs_Expression__assoc__non_assoc(builder);
      
        _init_prod__Composition_Expression__lhs_Expression_layouts_LAYOUTLIST_lit_o_layouts_LAYOUTLIST_rhs_Expression__assoc__left(builder);
      
        _init_prod__Implication_Expression__lhs_Expression_layouts_LAYOUTLIST_lit___61_61_62_layouts_LAYOUTLIST_rhs_Expression__assoc__non_assoc(builder);
      
        _init_prod__Negative_Expression__lit___layouts_LAYOUTLIST_argument_Expression_(builder);
      
        _init_prod__LessThan_Expression__lhs_Expression_layouts_LAYOUTLIST_conditional__lit___60__not_follow__lit___layouts_LAYOUTLIST_rhs_Expression__assoc__non_assoc(builder);
      
        _init_prod__NonEmptyBlock_Expression__lit___123_layouts_LAYOUTLIST_statements_iter_seps__Statement__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_(builder);
      
        _init_prod__IfDefinedOtherwise_Expression__lhs_Expression_layouts_LAYOUTLIST_lit___63_layouts_LAYOUTLIST_rhs_Expression__assoc__non_assoc(builder);
      
        _init_prod__FieldProject_Expression__expression_Expression_layouts_LAYOUTLIST_lit___60_layouts_LAYOUTLIST_fields_iter_seps__Field__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___62_(builder);
      
        _init_prod__It_Expression__lit_it_(builder);
      
        _init_prod__Literal_Expression__literal_Literal_(builder);
      
        _init_prod__IsDefined_Expression__argument_Expression_layouts_LAYOUTLIST_lit___63_(builder);
      
        _init_prod__Division_Expression__lhs_Expression_layouts_LAYOUTLIST_lit___47_layouts_LAYOUTLIST_rhs_Expression__assoc__left(builder);
      
        _init_prod__Comprehension_Expression__comprehension_Comprehension_(builder);
      
        _init_prod__Product_Expression__lhs_Expression_layouts_LAYOUTLIST_lit___42_layouts_LAYOUTLIST_rhs_Expression__assoc__left(builder);
      
    }
  }
	
  protected static class TagString {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__TagString__lit___123_iter_star__alt___seq___char_class___range__92_92_char_class___range__125_125_char_class___range__0_124_range__126_65535_lit___125_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[2] = new LiteralStackNode(1440, 2, prod__lit___125__char_class___range__125_125_, new char[] {125}, null, null);
      tmp[1] = new ListStackNode(1428, 1, regular__iter_star__alt___seq___char_class___range__92_92_char_class___range__125_125_char_class___range__0_124_range__126_65535, new AlternativeStackNode(1430, 0, regular__alt___seq___char_class___range__92_92_char_class___range__125_125_char_class___range__0_124_range__126_65535, new AbstractStackNode[]{new SequenceStackNode(1432, 0, regular__seq___char_class___range__92_92_char_class___range__125_125, new AbstractStackNode[]{new CharStackNode(1434, 0, new char[][]{{92,92}}, null, null), new CharStackNode(1436, 1, new char[][]{{125,125}}, null, null)}, null, null), new CharStackNode(1438, 0, new char[][]{{0,124},{126,65535}}, null, null)}, null, null), false, null, null);
      tmp[0] = new LiteralStackNode(1426, 0, prod__lit___123__char_class___range__123_123_, new char[] {123}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__TagString__lit___123_iter_star__alt___seq___char_class___range__92_92_char_class___range__125_125_char_class___range__0_124_range__126_65535_lit___125_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__TagString__lit___123_iter_star__alt___seq___char_class___range__92_92_char_class___range__125_125_char_class___range__0_124_range__126_65535_lit___125_(builder);
      
    }
  }
	
  protected static class Nonterminal {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__Nonterminal__conditional__char_class___range__65_90__not_precede__char_class___range__65_90_conditional__iter_star__char_class___range__48_57_range__65_90_range__95_95_range__97_122__not_follow__char_class___range__48_57_range__65_90_range__95_95_range__97_122_delete__RascalReservedKeywords_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[2];
      
      tmp[1] = new ListStackNode(1454, 1, regular__iter_star__char_class___range__48_57_range__65_90_range__95_95_range__97_122, new CharStackNode(1456, 0, new char[][]{{48,57},{65,90},{95,95},{97,122}}, null, null), false, null, new ICompletionFilter[] {new CharFollowRestriction(new char[][]{{48,57},{65,90},{95,95},{97,122}})});
      tmp[0] = new CharStackNode(1452, 0, new char[][]{{65,90}}, new IEnterFilter[] {new CharPrecedeRestriction(new char[][]{{65,90}})}, null);
      builder.addAlternative(ObjectRascalRascal.prod__Nonterminal__conditional__char_class___range__65_90__not_precede__char_class___range__65_90_conditional__iter_star__char_class___range__48_57_range__65_90_range__95_95_range__97_122__not_follow__char_class___range__48_57_range__65_90_range__95_95_range__97_122_delete__RascalReservedKeywords_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__Nonterminal__conditional__char_class___range__65_90__not_precede__char_class___range__65_90_conditional__iter_star__char_class___range__48_57_range__65_90_range__95_95_range__97_122__not_follow__char_class___range__48_57_range__65_90_range__95_95_range__97_122_delete__RascalReservedKeywords_(builder);
      
    }
  }
	
  protected static class PreModule {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__Default_PreModule__header_Header_layouts_LAYOUTLIST_conditional__empty__not_follow__HeaderKeyword_layouts_LAYOUTLIST_rest_iter_star_seps__Word__layouts_LAYOUTLIST_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new SeparatedListStackNode(1482, 4, regular__iter_star_seps__Word__layouts_LAYOUTLIST, new NonTerminalStackNode(1484, 0, "Word", null, null), new AbstractStackNode[]{new NonTerminalStackNode(1486, 1, "layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[3] = new NonTerminalStackNode(1480, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new EmptyStackNode(1478, 2, regular__empty, null, new ICompletionFilter[] {new StringFollowRestriction(new char[] {108,101,120,105,99,97,108}), new StringFollowRestriction(new char[] {105,109,112,111,114,116}), new StringFollowRestriction(new char[] {115,116,97,114,116}), new StringFollowRestriction(new char[] {115,121,110,116,97,120}), new StringFollowRestriction(new char[] {108,97,121,111,117,116}), new StringFollowRestriction(new char[] {101,120,116,101,110,100}), new StringFollowRestriction(new char[] {107,101,121,119,111,114,100})});
      tmp[1] = new NonTerminalStackNode(1476, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(1474, 0, "Header", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Default_PreModule__header_Header_layouts_LAYOUTLIST_conditional__empty__not_follow__HeaderKeyword_layouts_LAYOUTLIST_rest_iter_star_seps__Word__layouts_LAYOUTLIST_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__Default_PreModule__header_Header_layouts_LAYOUTLIST_conditional__empty__not_follow__HeaderKeyword_layouts_LAYOUTLIST_rest_iter_star_seps__Word__layouts_LAYOUTLIST_(builder);
      
    }
  }
	
  protected static class ProtocolPart {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__NonInterpolated_ProtocolPart__protocolChars_ProtocolChars_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode(1490, 0, "ProtocolChars", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__NonInterpolated_ProtocolPart__protocolChars_ProtocolChars_, tmp);
	}
    protected static final void _init_prod__Interpolated_ProtocolPart__pre_PreProtocolChars_layouts_LAYOUTLIST_expression_Expression_layouts_LAYOUTLIST_tail_ProtocolTail_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(1500, 4, "ProtocolTail", null, null);
      tmp[3] = new NonTerminalStackNode(1498, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(1496, 2, "Expression", null, null);
      tmp[1] = new NonTerminalStackNode(1494, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(1492, 0, "PreProtocolChars", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Interpolated_ProtocolPart__pre_PreProtocolChars_layouts_LAYOUTLIST_expression_Expression_layouts_LAYOUTLIST_tail_ProtocolTail_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__NonInterpolated_ProtocolPart__protocolChars_ProtocolChars_(builder);
      
        _init_prod__Interpolated_ProtocolPart__pre_PreProtocolChars_layouts_LAYOUTLIST_expression_Expression_layouts_LAYOUTLIST_tail_ProtocolTail_(builder);
      
    }
  }
	
  protected static class Comment {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__Comment__lit___47_42_iter_star__alt___conditional__char_class___range__42_42__not_follow__char_class___range__47_47_char_class___range__0_41_range__43_65535_lit___42_47__tag__category___67_111_109_109_101_110_116(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[2] = new LiteralStackNode(1546, 2, prod__lit___42_47__char_class___range__42_42_char_class___range__47_47_, new char[] {42,47}, null, null);
      tmp[1] = new ListStackNode(1538, 1, regular__iter_star__alt___conditional__char_class___range__42_42__not_follow__char_class___range__47_47_char_class___range__0_41_range__43_65535, new AlternativeStackNode(1540, 0, regular__alt___conditional__char_class___range__42_42__not_follow__char_class___range__47_47_char_class___range__0_41_range__43_65535, new AbstractStackNode[]{new CharStackNode(1542, 0, new char[][]{{42,42}}, null, new ICompletionFilter[] {new CharFollowRestriction(new char[][]{{47,47}})}), new CharStackNode(1544, 0, new char[][]{{0,41},{43,65535}}, null, null)}, null, null), false, null, null);
      tmp[0] = new LiteralStackNode(1536, 0, prod__lit___47_42__char_class___range__47_47_char_class___range__42_42_, new char[] {47,42}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Comment__lit___47_42_iter_star__alt___conditional__char_class___range__42_42__not_follow__char_class___range__47_47_char_class___range__0_41_range__43_65535_lit___42_47__tag__category___67_111_109_109_101_110_116, tmp);
	}
    protected static final void _init_prod__Comment__lit___47_47_iter_star__char_class___range__0_9_range__11_65535_char_class___range__10_10__tag__category___67_111_109_109_101_110_116(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[2] = new CharStackNode(1554, 2, new char[][]{{10,10}}, null, null);
      tmp[1] = new ListStackNode(1550, 1, regular__iter_star__char_class___range__0_9_range__11_65535, new CharStackNode(1552, 0, new char[][]{{0,9},{11,65535}}, null, null), false, null, null);
      tmp[0] = new LiteralStackNode(1548, 0, prod__lit___47_47__char_class___range__47_47_char_class___range__47_47_, new char[] {47,47}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Comment__lit___47_47_iter_star__char_class___range__0_9_range__11_65535_char_class___range__10_10__tag__category___67_111_109_109_101_110_116, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__Comment__lit___47_42_iter_star__alt___conditional__char_class___range__42_42__not_follow__char_class___range__47_47_char_class___range__0_41_range__43_65535_lit___42_47__tag__category___67_111_109_109_101_110_116(builder);
      
        _init_prod__Comment__lit___47_47_iter_star__char_class___range__0_9_range__11_65535_char_class___range__10_10__tag__category___67_111_109_109_101_110_116(builder);
      
    }
  }
	
  protected static class Label {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__Empty_Label__(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new EpsilonStackNode(1556, 0);
      builder.addAlternative(ObjectRascalRascal.prod__Empty_Label__, tmp);
	}
    protected static final void _init_prod__Default_Label__name_Name_layouts_LAYOUTLIST_lit___58_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[2] = new LiteralStackNode(1562, 2, prod__lit___58__char_class___range__58_58_, new char[] {58}, null, null);
      tmp[1] = new NonTerminalStackNode(1560, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(1558, 0, "Name", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Default_Label__name_Name_layouts_LAYOUTLIST_lit___58_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__Empty_Label__(builder);
      
        _init_prod__Default_Label__name_Name_layouts_LAYOUTLIST_lit___58_(builder);
      
    }
  }
	
  protected static class Field {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__Index_Field__fieldIndex_IntegerLiteral_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode(1610, 0, "IntegerLiteral", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Index_Field__fieldIndex_IntegerLiteral_, tmp);
	}
    protected static final void _init_prod__Name_Field__fieldName_Name_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode(1612, 0, "Name", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Name_Field__fieldName_Name_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__Index_Field__fieldIndex_IntegerLiteral_(builder);
      
        _init_prod__Name_Field__fieldName_Name_(builder);
      
    }
  }
	
  protected static class FunctionModifier {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__Java_FunctionModifier__lit_java_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(1638, 0, prod__lit_java__char_class___range__106_106_char_class___range__97_97_char_class___range__118_118_char_class___range__97_97_, new char[] {106,97,118,97}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Java_FunctionModifier__lit_java_, tmp);
	}
    protected static final void _init_prod__Default_FunctionModifier__lit_default_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(1640, 0, prod__lit_default__char_class___range__100_100_char_class___range__101_101_char_class___range__102_102_char_class___range__97_97_char_class___range__117_117_char_class___range__108_108_char_class___range__116_116_, new char[] {100,101,102,97,117,108,116}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Default_FunctionModifier__lit_default_, tmp);
	}
    protected static final void _init_prod__Test_FunctionModifier__lit_test_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(1642, 0, prod__lit_test__char_class___range__116_116_char_class___range__101_101_char_class___range__115_115_char_class___range__116_116_, new char[] {116,101,115,116}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Test_FunctionModifier__lit_test_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__Java_FunctionModifier__lit_java_(builder);
      
        _init_prod__Default_FunctionModifier__lit_default_(builder);
      
        _init_prod__Test_FunctionModifier__lit_test_(builder);
      
    }
  }
	
  protected static class ProtocolChars {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__ProtocolChars__char_class___range__124_124_URLChars_conditional__lit___58_47_47__not_follow__char_class___range__9_10_range__13_13_range__32_32_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[2] = new LiteralStackNode(1700, 2, prod__lit___58_47_47__char_class___range__58_58_char_class___range__47_47_char_class___range__47_47_, new char[] {58,47,47}, null, new ICompletionFilter[] {new CharFollowRestriction(new char[][]{{9,10},{13,13},{32,32}})});
      tmp[1] = new NonTerminalStackNode(1698, 1, "URLChars", null, null);
      tmp[0] = new CharStackNode(1696, 0, new char[][]{{124,124}}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__ProtocolChars__char_class___range__124_124_URLChars_conditional__lit___58_47_47__not_follow__char_class___range__9_10_range__13_13_range__32_32_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__ProtocolChars__char_class___range__124_124_URLChars_conditional__lit___58_47_47__not_follow__char_class___range__9_10_range__13_13_range__32_32_(builder);
      
    }
  }
	
  protected static class Assignment {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__Subtraction_Assignment__lit___45_61_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(1682, 0, prod__lit___45_61__char_class___range__45_45_char_class___range__61_61_, new char[] {45,61}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Subtraction_Assignment__lit___45_61_, tmp);
	}
    protected static final void _init_prod__Intersection_Assignment__lit___38_61_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(1684, 0, prod__lit___38_61__char_class___range__38_38_char_class___range__61_61_, new char[] {38,61}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Intersection_Assignment__lit___38_61_, tmp);
	}
    protected static final void _init_prod__Default_Assignment__lit___61_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(1686, 0, prod__lit___61__char_class___range__61_61_, new char[] {61}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Default_Assignment__lit___61_, tmp);
	}
    protected static final void _init_prod__IfDefined_Assignment__lit___63_61_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(1688, 0, prod__lit___63_61__char_class___range__63_63_char_class___range__61_61_, new char[] {63,61}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__IfDefined_Assignment__lit___63_61_, tmp);
	}
    protected static final void _init_prod__Division_Assignment__lit___47_61_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(1690, 0, prod__lit___47_61__char_class___range__47_47_char_class___range__61_61_, new char[] {47,61}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Division_Assignment__lit___47_61_, tmp);
	}
    protected static final void _init_prod__Product_Assignment__lit___42_61_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(1692, 0, prod__lit___42_61__char_class___range__42_42_char_class___range__61_61_, new char[] {42,61}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Product_Assignment__lit___42_61_, tmp);
	}
    protected static final void _init_prod__Addition_Assignment__lit___43_61_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(1694, 0, prod__lit___43_61__char_class___range__43_43_char_class___range__61_61_, new char[] {43,61}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Addition_Assignment__lit___43_61_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__Subtraction_Assignment__lit___45_61_(builder);
      
        _init_prod__Intersection_Assignment__lit___38_61_(builder);
      
        _init_prod__Default_Assignment__lit___61_(builder);
      
        _init_prod__IfDefined_Assignment__lit___63_61_(builder);
      
        _init_prod__Division_Assignment__lit___47_61_(builder);
      
        _init_prod__Product_Assignment__lit___42_61_(builder);
      
        _init_prod__Addition_Assignment__lit___43_61_(builder);
      
    }
  }
	
  protected static class Assignable {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__Bracket_Assignable__lit___40_layouts_LAYOUTLIST_arg_Assignable_layouts_LAYOUTLIST_lit___41__bracket(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new LiteralStackNode(1754, 4, prod__lit___41__char_class___range__41_41_, new char[] {41}, null, null);
      tmp[3] = new NonTerminalStackNode(1752, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(1750, 2, "Assignable", null, null);
      tmp[1] = new NonTerminalStackNode(1748, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(1746, 0, prod__lit___40__char_class___range__40_40_, new char[] {40}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Bracket_Assignable__lit___40_layouts_LAYOUTLIST_arg_Assignable_layouts_LAYOUTLIST_lit___41__bracket, tmp);
	}
    protected static final void _init_prod__Variable_Assignable__qualifiedName_QualifiedName_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode(1756, 0, "QualifiedName", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Variable_Assignable__qualifiedName_QualifiedName_, tmp);
	}
    protected static final void _init_prod__Tuple_Assignable__lit___60_layouts_LAYOUTLIST_elements_iter_seps__Assignable__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___62_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[0] = new LiteralStackNode(1768, 0, prod__lit___60__char_class___range__60_60_, new char[] {60}, null, null);
      tmp[1] = new NonTerminalStackNode(1770, 1, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new SeparatedListStackNode(1772, 2, regular__iter_seps__Assignable__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST, new NonTerminalStackNode(1774, 0, "Assignable", null, null), new AbstractStackNode[]{new NonTerminalStackNode(1776, 1, "layouts_LAYOUTLIST", null, null), new LiteralStackNode(1778, 2, prod__lit___44__char_class___range__44_44_, new char[] {44}, null, null), new NonTerminalStackNode(1780, 3, "layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[3] = new NonTerminalStackNode(1782, 3, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode(1784, 4, prod__lit___62__char_class___range__62_62_, new char[] {62}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Tuple_Assignable__lit___60_layouts_LAYOUTLIST_elements_iter_seps__Assignable__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___62_, tmp);
	}
    protected static final void _init_prod__IfDefinedOrDefault_Assignable__receiver_Assignable_layouts_LAYOUTLIST_lit___63_layouts_LAYOUTLIST_defaultExpression_Expression_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(1766, 4, "Expression", null, null);
      tmp[3] = new NonTerminalStackNode(1764, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(1762, 2, prod__lit___63__char_class___range__63_63_, new char[] {63}, null, null);
      tmp[1] = new NonTerminalStackNode(1760, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(1758, 0, "Assignable", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__IfDefinedOrDefault_Assignable__receiver_Assignable_layouts_LAYOUTLIST_lit___63_layouts_LAYOUTLIST_defaultExpression_Expression_, tmp);
	}
    protected static final void _init_prod__Annotation_Assignable__receiver_Assignable_layouts_LAYOUTLIST_lit___64_layouts_LAYOUTLIST_annotation_Name_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(1794, 4, "Name", null, null);
      tmp[3] = new NonTerminalStackNode(1792, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(1790, 2, prod__lit___64__char_class___range__64_64_, new char[] {64}, null, null);
      tmp[1] = new NonTerminalStackNode(1788, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(1786, 0, "Assignable", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Annotation_Assignable__receiver_Assignable_layouts_LAYOUTLIST_lit___64_layouts_LAYOUTLIST_annotation_Name_, tmp);
	}
    protected static final void _init_prod__FieldAccess_Assignable__receiver_Assignable_layouts_LAYOUTLIST_lit___46_layouts_LAYOUTLIST_field_Name_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(1804, 4, "Name", null, null);
      tmp[3] = new NonTerminalStackNode(1802, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(1800, 2, prod__lit___46__char_class___range__46_46_, new char[] {46}, null, null);
      tmp[1] = new NonTerminalStackNode(1798, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(1796, 0, "Assignable", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__FieldAccess_Assignable__receiver_Assignable_layouts_LAYOUTLIST_lit___46_layouts_LAYOUTLIST_field_Name_, tmp);
	}
    protected static final void _init_prod__Subscript_Assignable__receiver_Assignable_layouts_LAYOUTLIST_lit___91_layouts_LAYOUTLIST_subscript_Expression_layouts_LAYOUTLIST_lit___93_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[7];
      
      tmp[6] = new LiteralStackNode(1818, 6, prod__lit___93__char_class___range__93_93_, new char[] {93}, null, null);
      tmp[5] = new NonTerminalStackNode(1816, 5, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new NonTerminalStackNode(1814, 4, "Expression", null, null);
      tmp[3] = new NonTerminalStackNode(1812, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(1810, 2, prod__lit___91__char_class___range__91_91_, new char[] {91}, null, null);
      tmp[1] = new NonTerminalStackNode(1808, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(1806, 0, "Assignable", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Subscript_Assignable__receiver_Assignable_layouts_LAYOUTLIST_lit___91_layouts_LAYOUTLIST_subscript_Expression_layouts_LAYOUTLIST_lit___93_, tmp);
	}
    protected static final void _init_prod__Constructor_Assignable__name_Name_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_arguments_iter_seps__Assignable__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[7];
      
      tmp[6] = new LiteralStackNode(1840, 6, prod__lit___41__char_class___range__41_41_, new char[] {41}, null, null);
      tmp[5] = new NonTerminalStackNode(1838, 5, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new SeparatedListStackNode(1828, 4, regular__iter_seps__Assignable__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST, new NonTerminalStackNode(1830, 0, "Assignable", null, null), new AbstractStackNode[]{new NonTerminalStackNode(1832, 1, "layouts_LAYOUTLIST", null, null), new LiteralStackNode(1834, 2, prod__lit___44__char_class___range__44_44_, new char[] {44}, null, null), new NonTerminalStackNode(1836, 3, "layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[3] = new NonTerminalStackNode(1826, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(1824, 2, prod__lit___40__char_class___range__40_40_, new char[] {40}, null, null);
      tmp[1] = new NonTerminalStackNode(1822, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(1820, 0, "Name", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Constructor_Assignable__name_Name_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_arguments_iter_seps__Assignable__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
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
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__HeaderKeyword__lit_start_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(1878, 0, prod__lit_start__char_class___range__115_115_char_class___range__116_116_char_class___range__97_97_char_class___range__114_114_char_class___range__116_116_, new char[] {115,116,97,114,116}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__HeaderKeyword__lit_start_, tmp);
	}
    protected static final void _init_prod__HeaderKeyword__lit_keyword_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(1880, 0, prod__lit_keyword__char_class___range__107_107_char_class___range__101_101_char_class___range__121_121_char_class___range__119_119_char_class___range__111_111_char_class___range__114_114_char_class___range__100_100_, new char[] {107,101,121,119,111,114,100}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__HeaderKeyword__lit_keyword_, tmp);
	}
    protected static final void _init_prod__HeaderKeyword__lit_syntax_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(1882, 0, prod__lit_syntax__char_class___range__115_115_char_class___range__121_121_char_class___range__110_110_char_class___range__116_116_char_class___range__97_97_char_class___range__120_120_, new char[] {115,121,110,116,97,120}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__HeaderKeyword__lit_syntax_, tmp);
	}
    protected static final void _init_prod__HeaderKeyword__lit_import_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(1884, 0, prod__lit_import__char_class___range__105_105_char_class___range__109_109_char_class___range__112_112_char_class___range__111_111_char_class___range__114_114_char_class___range__116_116_, new char[] {105,109,112,111,114,116}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__HeaderKeyword__lit_import_, tmp);
	}
    protected static final void _init_prod__HeaderKeyword__lit_lexical_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(1886, 0, prod__lit_lexical__char_class___range__108_108_char_class___range__101_101_char_class___range__120_120_char_class___range__105_105_char_class___range__99_99_char_class___range__97_97_char_class___range__108_108_, new char[] {108,101,120,105,99,97,108}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__HeaderKeyword__lit_lexical_, tmp);
	}
    protected static final void _init_prod__HeaderKeyword__lit_extend_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(1888, 0, prod__lit_extend__char_class___range__101_101_char_class___range__120_120_char_class___range__116_116_char_class___range__101_101_char_class___range__110_110_char_class___range__100_100_, new char[] {101,120,116,101,110,100}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__HeaderKeyword__lit_extend_, tmp);
	}
    protected static final void _init_prod__HeaderKeyword__lit_layout_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(1890, 0, prod__lit_layout__char_class___range__108_108_char_class___range__97_97_char_class___range__121_121_char_class___range__111_111_char_class___range__117_117_char_class___range__116_116_, new char[] {108,97,121,111,117,116}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__HeaderKeyword__lit_layout_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
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
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__Default_Parameters__lit___40_layouts_LAYOUTLIST_formals_Formals_layouts_LAYOUTLIST_lit___41_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new LiteralStackNode(1948, 4, prod__lit___41__char_class___range__41_41_, new char[] {41}, null, null);
      tmp[3] = new NonTerminalStackNode(1946, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(1944, 2, "Formals", null, null);
      tmp[1] = new NonTerminalStackNode(1942, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(1940, 0, prod__lit___40__char_class___range__40_40_, new char[] {40}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Default_Parameters__lit___40_layouts_LAYOUTLIST_formals_Formals_layouts_LAYOUTLIST_lit___41_, tmp);
	}
    protected static final void _init_prod__VarArgs_Parameters__lit___40_layouts_LAYOUTLIST_formals_Formals_layouts_LAYOUTLIST_lit___46_46_46_layouts_LAYOUTLIST_lit___41_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[7];
      
      tmp[6] = new LiteralStackNode(1962, 6, prod__lit___41__char_class___range__41_41_, new char[] {41}, null, null);
      tmp[5] = new NonTerminalStackNode(1960, 5, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode(1958, 4, prod__lit___46_46_46__char_class___range__46_46_char_class___range__46_46_char_class___range__46_46_, new char[] {46,46,46}, null, null);
      tmp[3] = new NonTerminalStackNode(1956, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(1954, 2, "Formals", null, null);
      tmp[1] = new NonTerminalStackNode(1952, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(1950, 0, prod__lit___40__char_class___range__40_40_, new char[] {40}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__VarArgs_Parameters__lit___40_layouts_LAYOUTLIST_formals_Formals_layouts_LAYOUTLIST_lit___46_46_46_layouts_LAYOUTLIST_lit___41_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__Default_Parameters__lit___40_layouts_LAYOUTLIST_formals_Formals_layouts_LAYOUTLIST_lit___41_(builder);
      
        _init_prod__VarArgs_Parameters__lit___40_layouts_LAYOUTLIST_formals_Formals_layouts_LAYOUTLIST_lit___46_46_46_layouts_LAYOUTLIST_lit___41_(builder);
      
    }
  }
	
  protected static class DatePart {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__DatePart__char_class___range__48_57_char_class___range__48_57_char_class___range__48_57_char_class___range__48_57_lit___char_class___range__48_49_char_class___range__48_57_lit___char_class___range__48_51_char_class___range__48_57_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[10];
      
      tmp[9] = new CharStackNode(1982, 9, new char[][]{{48,57}}, null, null);
      tmp[8] = new CharStackNode(1980, 8, new char[][]{{48,51}}, null, null);
      tmp[7] = new LiteralStackNode(1978, 7, prod__lit____char_class___range__45_45_, new char[] {45}, null, null);
      tmp[6] = new CharStackNode(1976, 6, new char[][]{{48,57}}, null, null);
      tmp[5] = new CharStackNode(1974, 5, new char[][]{{48,49}}, null, null);
      tmp[4] = new LiteralStackNode(1972, 4, prod__lit____char_class___range__45_45_, new char[] {45}, null, null);
      tmp[3] = new CharStackNode(1970, 3, new char[][]{{48,57}}, null, null);
      tmp[2] = new CharStackNode(1968, 2, new char[][]{{48,57}}, null, null);
      tmp[1] = new CharStackNode(1966, 1, new char[][]{{48,57}}, null, null);
      tmp[0] = new CharStackNode(1964, 0, new char[][]{{48,57}}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__DatePart__char_class___range__48_57_char_class___range__48_57_char_class___range__48_57_char_class___range__48_57_lit___char_class___range__48_49_char_class___range__48_57_lit___char_class___range__48_51_char_class___range__48_57_, tmp);
	}
    protected static final void _init_prod__DatePart__char_class___range__48_57_char_class___range__48_57_char_class___range__48_57_char_class___range__48_57_char_class___range__48_49_char_class___range__48_57_char_class___range__48_51_char_class___range__48_57_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[8];
      
      tmp[7] = new CharStackNode(1998, 7, new char[][]{{48,57}}, null, null);
      tmp[6] = new CharStackNode(1996, 6, new char[][]{{48,51}}, null, null);
      tmp[5] = new CharStackNode(1994, 5, new char[][]{{48,57}}, null, null);
      tmp[4] = new CharStackNode(1992, 4, new char[][]{{48,49}}, null, null);
      tmp[3] = new CharStackNode(1990, 3, new char[][]{{48,57}}, null, null);
      tmp[2] = new CharStackNode(1988, 2, new char[][]{{48,57}}, null, null);
      tmp[1] = new CharStackNode(1986, 1, new char[][]{{48,57}}, null, null);
      tmp[0] = new CharStackNode(1984, 0, new char[][]{{48,57}}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__DatePart__char_class___range__48_57_char_class___range__48_57_char_class___range__48_57_char_class___range__48_57_char_class___range__48_49_char_class___range__48_57_char_class___range__48_51_char_class___range__48_57_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__DatePart__char_class___range__48_57_char_class___range__48_57_char_class___range__48_57_char_class___range__48_57_lit___char_class___range__48_49_char_class___range__48_57_lit___char_class___range__48_51_char_class___range__48_57_(builder);
      
        _init_prod__DatePart__char_class___range__48_57_char_class___range__48_57_char_class___range__48_57_char_class___range__48_57_char_class___range__48_49_char_class___range__48_57_char_class___range__48_51_char_class___range__48_57_(builder);
      
    }
  }
	
  protected static class Mapping__Pattern {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__Default_Mapping__Pattern__from_Pattern_layouts_LAYOUTLIST_lit___58_layouts_LAYOUTLIST_to_Pattern_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(2008, 4, "Pattern", null, null);
      tmp[3] = new NonTerminalStackNode(2006, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(2004, 2, prod__lit___58__char_class___range__58_58_, new char[] {58}, null, null);
      tmp[1] = new NonTerminalStackNode(2002, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(2000, 0, "Pattern", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Default_Mapping__Pattern__from_Pattern_layouts_LAYOUTLIST_lit___58_layouts_LAYOUTLIST_to_Pattern_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__Default_Mapping__Pattern__from_Pattern_layouts_LAYOUTLIST_lit___58_layouts_LAYOUTLIST_to_Pattern_(builder);
      
    }
  }
	
  protected static class LocalVariableDeclaration {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__Dynamic_LocalVariableDeclaration__lit_dynamic_layouts_LAYOUTLIST_declarator_Declarator_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode(2018, 2, "Declarator", null, null);
      tmp[1] = new NonTerminalStackNode(2016, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(2014, 0, prod__lit_dynamic__char_class___range__100_100_char_class___range__121_121_char_class___range__110_110_char_class___range__97_97_char_class___range__109_109_char_class___range__105_105_char_class___range__99_99_, new char[] {100,121,110,97,109,105,99}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Dynamic_LocalVariableDeclaration__lit_dynamic_layouts_LAYOUTLIST_declarator_Declarator_, tmp);
	}
    protected static final void _init_prod__Default_LocalVariableDeclaration__declarator_Declarator_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode(2020, 0, "Declarator", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Default_LocalVariableDeclaration__declarator_Declarator_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__Dynamic_LocalVariableDeclaration__lit_dynamic_layouts_LAYOUTLIST_declarator_Declarator_(builder);
      
        _init_prod__Default_LocalVariableDeclaration__declarator_Declarator_(builder);
      
    }
  }
	
  protected static class StringConstant {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__StringConstant__lit___34_iter_star__StringCharacter_lit___34__tag__category___67_111_110_115_116_97_110_116(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[2] = new LiteralStackNode(2034, 2, prod__lit___34__char_class___range__34_34_, new char[] {34}, null, null);
      tmp[1] = new ListStackNode(2030, 1, regular__iter_star__StringCharacter, new NonTerminalStackNode(2032, 0, "StringCharacter", null, null), false, null, null);
      tmp[0] = new LiteralStackNode(2028, 0, prod__lit___34__char_class___range__34_34_, new char[] {34}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__StringConstant__lit___34_iter_star__StringCharacter_lit___34__tag__category___67_111_110_115_116_97_110_116, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__StringConstant__lit___34_iter_star__StringCharacter_lit___34__tag__category___67_111_110_115_116_97_110_116(builder);
      
    }
  }
	
  protected static class start__Module {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__start__Module__layouts_LAYOUTLIST_top_Module_layouts_LAYOUTLIST_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode(2040, 2, "layouts_LAYOUTLIST", null, null);
      tmp[1] = new NonTerminalStackNode(2038, 1, "Module", null, null);
      tmp[0] = new NonTerminalStackNode(2036, 0, "layouts_LAYOUTLIST", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__start__Module__layouts_LAYOUTLIST_top_Module_layouts_LAYOUTLIST_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__start__Module__layouts_LAYOUTLIST_top_Module_layouts_LAYOUTLIST_(builder);
      
    }
  }
	
  protected static class DataTypeSelector {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__Selector_DataTypeSelector__sort_QualifiedName_layouts_LAYOUTLIST_lit___46_layouts_LAYOUTLIST_production_Name_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(2050, 4, "Name", null, null);
      tmp[3] = new NonTerminalStackNode(2048, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(2046, 2, prod__lit___46__char_class___range__46_46_, new char[] {46}, null, null);
      tmp[1] = new NonTerminalStackNode(2044, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(2042, 0, "QualifiedName", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Selector_DataTypeSelector__sort_QualifiedName_layouts_LAYOUTLIST_lit___46_layouts_LAYOUTLIST_production_Name_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__Selector_DataTypeSelector__sort_QualifiedName_layouts_LAYOUTLIST_lit___46_layouts_LAYOUTLIST_production_Name_(builder);
      
    }
  }
	
  protected static class StringTail {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__MidInterpolated_StringTail__mid_MidStringChars_layouts_LAYOUTLIST_expression_Expression_layouts_LAYOUTLIST_tail_StringTail_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(2060, 4, "StringTail", null, null);
      tmp[3] = new NonTerminalStackNode(2058, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(2056, 2, "Expression", null, null);
      tmp[1] = new NonTerminalStackNode(2054, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(2052, 0, "MidStringChars", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__MidInterpolated_StringTail__mid_MidStringChars_layouts_LAYOUTLIST_expression_Expression_layouts_LAYOUTLIST_tail_StringTail_, tmp);
	}
    protected static final void _init_prod__MidTemplate_StringTail__mid_MidStringChars_layouts_LAYOUTLIST_template_StringTemplate_layouts_LAYOUTLIST_tail_StringTail_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(2070, 4, "StringTail", null, null);
      tmp[3] = new NonTerminalStackNode(2068, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(2066, 2, "StringTemplate", null, null);
      tmp[1] = new NonTerminalStackNode(2064, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(2062, 0, "MidStringChars", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__MidTemplate_StringTail__mid_MidStringChars_layouts_LAYOUTLIST_template_StringTemplate_layouts_LAYOUTLIST_tail_StringTail_, tmp);
	}
    protected static final void _init_prod__Post_StringTail__post_PostStringChars_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode(2072, 0, "PostStringChars", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Post_StringTail__post_PostStringChars_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__MidInterpolated_StringTail__mid_MidStringChars_layouts_LAYOUTLIST_expression_Expression_layouts_LAYOUTLIST_tail_StringTail_(builder);
      
        _init_prod__MidTemplate_StringTail__mid_MidStringChars_layouts_LAYOUTLIST_template_StringTemplate_layouts_LAYOUTLIST_tail_StringTail_(builder);
      
        _init_prod__Post_StringTail__post_PostStringChars_(builder);
      
    }
  }
	
  protected static class PatternWithAction {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__Replacing_PatternWithAction__pattern_Pattern_layouts_LAYOUTLIST_lit___61_62_layouts_LAYOUTLIST_replacement_Replacement_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(2082, 4, "Replacement", null, null);
      tmp[3] = new NonTerminalStackNode(2080, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(2078, 2, prod__lit___61_62__char_class___range__61_61_char_class___range__62_62_, new char[] {61,62}, null, null);
      tmp[1] = new NonTerminalStackNode(2076, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(2074, 0, "Pattern", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Replacing_PatternWithAction__pattern_Pattern_layouts_LAYOUTLIST_lit___61_62_layouts_LAYOUTLIST_replacement_Replacement_, tmp);
	}
    protected static final void _init_prod__Arbitrary_PatternWithAction__pattern_Pattern_layouts_LAYOUTLIST_lit___58_layouts_LAYOUTLIST_statement_Statement_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(2092, 4, "Statement", null, null);
      tmp[3] = new NonTerminalStackNode(2090, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(2088, 2, prod__lit___58__char_class___range__58_58_, new char[] {58}, null, null);
      tmp[1] = new NonTerminalStackNode(2086, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(2084, 0, "Pattern", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Arbitrary_PatternWithAction__pattern_Pattern_layouts_LAYOUTLIST_lit___58_layouts_LAYOUTLIST_statement_Statement_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__Replacing_PatternWithAction__pattern_Pattern_layouts_LAYOUTLIST_lit___61_62_layouts_LAYOUTLIST_replacement_Replacement_(builder);
      
        _init_prod__Arbitrary_PatternWithAction__pattern_Pattern_layouts_LAYOUTLIST_lit___58_layouts_LAYOUTLIST_statement_Statement_(builder);
      
    }
  }
	
  protected static class MidProtocolChars {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__MidProtocolChars__lit___62_URLChars_lit___60_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[2] = new LiteralStackNode(2144, 2, prod__lit___60__char_class___range__60_60_, new char[] {60}, null, null);
      tmp[1] = new NonTerminalStackNode(2142, 1, "URLChars", null, null);
      tmp[0] = new LiteralStackNode(2140, 0, prod__lit___62__char_class___range__62_62_, new char[] {62}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__MidProtocolChars__lit___62_URLChars_lit___60_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__MidProtocolChars__lit___62_URLChars_lit___60_(builder);
      
    }
  }
	
  protected static class PathTail {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__Mid_PathTail__mid_MidPathChars_layouts_LAYOUTLIST_expression_Expression_layouts_LAYOUTLIST_tail_PathTail_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(2136, 4, "PathTail", null, null);
      tmp[3] = new NonTerminalStackNode(2134, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(2132, 2, "Expression", null, null);
      tmp[1] = new NonTerminalStackNode(2130, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(2128, 0, "MidPathChars", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Mid_PathTail__mid_MidPathChars_layouts_LAYOUTLIST_expression_Expression_layouts_LAYOUTLIST_tail_PathTail_, tmp);
	}
    protected static final void _init_prod__Post_PathTail__post_PostPathChars_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode(2138, 0, "PostPathChars", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Post_PathTail__post_PostPathChars_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__Mid_PathTail__mid_MidPathChars_layouts_LAYOUTLIST_expression_Expression_layouts_LAYOUTLIST_tail_PathTail_(builder);
      
        _init_prod__Post_PathTail__post_PostPathChars_(builder);
      
    }
  }
	
  protected static class JustDate {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__JustDate__lit___36_DatePart_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[2];
      
      tmp[1] = new NonTerminalStackNode(2158, 1, "DatePart", null, null);
      tmp[0] = new LiteralStackNode(2156, 0, prod__lit___36__char_class___range__36_36_, new char[] {36}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__JustDate__lit___36_DatePart_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__JustDate__lit___36_DatePart_(builder);
      
    }
  }
	
  protected static class Backslash {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__Backslash__conditional__char_class___range__92_92__not_follow__char_class___range__47_47_range__60_60_range__62_62_range__92_92_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new CharStackNode(2182, 0, new char[][]{{92,92}}, null, new ICompletionFilter[] {new CharFollowRestriction(new char[][]{{47,47},{60,60},{62,62},{92,92}})});
      builder.addAlternative(ObjectRascalRascal.prod__Backslash__conditional__char_class___range__92_92__not_follow__char_class___range__47_47_range__60_60_range__62_62_range__92_92_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__Backslash__conditional__char_class___range__92_92__not_follow__char_class___range__47_47_range__60_60_range__62_62_range__92_92_(builder);
      
    }
  }
	
  protected static class start__Commands {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__start__Commands__layouts_LAYOUTLIST_top_Commands_layouts_LAYOUTLIST_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode(2180, 2, "layouts_LAYOUTLIST", null, null);
      tmp[1] = new NonTerminalStackNode(2178, 1, "Commands", null, null);
      tmp[0] = new NonTerminalStackNode(2176, 0, "layouts_LAYOUTLIST", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__start__Commands__layouts_LAYOUTLIST_top_Commands_layouts_LAYOUTLIST_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__start__Commands__layouts_LAYOUTLIST_top_Commands_layouts_LAYOUTLIST_(builder);
      
    }
  }
	
  protected static class CaseInsensitiveStringConstant {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__CaseInsensitiveStringConstant__lit___39_iter_star__StringCharacter_lit___39__tag__category___67_111_110_115_116_97_110_116(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[2] = new LiteralStackNode(2174, 2, prod__lit___39__char_class___range__39_39_, new char[] {39}, null, null);
      tmp[1] = new ListStackNode(2170, 1, regular__iter_star__StringCharacter, new NonTerminalStackNode(2172, 0, "StringCharacter", null, null), false, null, null);
      tmp[0] = new LiteralStackNode(2168, 0, prod__lit___39__char_class___range__39_39_, new char[] {39}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__CaseInsensitiveStringConstant__lit___39_iter_star__StringCharacter_lit___39__tag__category___67_111_110_115_116_97_110_116, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__CaseInsensitiveStringConstant__lit___39_iter_star__StringCharacter_lit___39__tag__category___67_111_110_115_116_97_110_116(builder);
      
    }
  }
	
  protected static class Tags {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__Default_Tags__tags_iter_star_seps__Tag__layouts_LAYOUTLIST_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new SeparatedListStackNode(2188, 0, regular__iter_star_seps__Tag__layouts_LAYOUTLIST, new NonTerminalStackNode(2190, 0, "Tag", null, null), new AbstractStackNode[]{new NonTerminalStackNode(2192, 1, "layouts_LAYOUTLIST", null, null)}, false, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Default_Tags__tags_iter_star_seps__Tag__layouts_LAYOUTLIST_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__Default_Tags__tags_iter_star_seps__Tag__layouts_LAYOUTLIST_(builder);
      
    }
  }
	
  protected static class Formals {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__Default_Formals__formals_iter_star_seps__Pattern__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new SeparatedListStackNode(2202, 0, regular__iter_star_seps__Pattern__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST, new NonTerminalStackNode(2204, 0, "Pattern", null, null), new AbstractStackNode[]{new NonTerminalStackNode(2206, 1, "layouts_LAYOUTLIST", null, null), new LiteralStackNode(2208, 2, prod__lit___44__char_class___range__44_44_, new char[] {44}, null, null), new NonTerminalStackNode(2210, 3, "layouts_LAYOUTLIST", null, null)}, false, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Default_Formals__formals_iter_star_seps__Pattern__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__Default_Formals__formals_iter_star_seps__Pattern__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_(builder);
      
    }
  }
	
  protected static class Start {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__Present_Start__lit_start_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(2212, 0, prod__lit_start__char_class___range__115_115_char_class___range__116_116_char_class___range__97_97_char_class___range__114_114_char_class___range__116_116_, new char[] {115,116,97,114,116}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Present_Start__lit_start_, tmp);
	}
    protected static final void _init_prod__Absent_Start__(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new EpsilonStackNode(2214, 0);
      builder.addAlternative(ObjectRascalRascal.prod__Absent_Start__, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__Present_Start__lit_start_(builder);
      
        _init_prod__Absent_Start__(builder);
      
    }
  }
	
  protected static class Name {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__Name__char_class___range__92_92_char_class___range__65_90_range__95_95_range__97_122_conditional__iter_star__char_class___range__45_45_range__48_57_range__65_90_range__95_95_range__97_122__not_follow__char_class___range__45_45_range__48_57_range__65_90_range__95_95_range__97_122_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[2] = new ListStackNode(2220, 2, regular__iter_star__char_class___range__45_45_range__48_57_range__65_90_range__95_95_range__97_122, new CharStackNode(2222, 0, new char[][]{{45,45},{48,57},{65,90},{95,95},{97,122}}, null, null), false, null, new ICompletionFilter[] {new CharFollowRestriction(new char[][]{{45,45},{48,57},{65,90},{95,95},{97,122}})});
      tmp[1] = new CharStackNode(2218, 1, new char[][]{{65,90},{95,95},{97,122}}, null, null);
      tmp[0] = new CharStackNode(2216, 0, new char[][]{{92,92}}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Name__char_class___range__92_92_char_class___range__65_90_range__95_95_range__97_122_conditional__iter_star__char_class___range__45_45_range__48_57_range__65_90_range__95_95_range__97_122__not_follow__char_class___range__45_45_range__48_57_range__65_90_range__95_95_range__97_122_, tmp);
	}
    protected static final void _init_prod__Name__conditional__seq___conditional__char_class___range__65_90_range__95_95_range__97_122__not_precede__char_class___range__65_90_range__95_95_range__97_122_conditional__iter_star__char_class___range__48_57_range__65_90_range__95_95_range__97_122__not_follow__char_class___range__48_57_range__65_90_range__95_95_range__97_122__delete__RascalKeywords_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new SequenceStackNode(2224, 0, regular__seq___conditional__char_class___range__65_90_range__95_95_range__97_122__not_precede__char_class___range__65_90_range__95_95_range__97_122_conditional__iter_star__char_class___range__48_57_range__65_90_range__95_95_range__97_122__not_follow__char_class___range__48_57_range__65_90_range__95_95_range__97_122, new AbstractStackNode[]{new CharStackNode(2226, 0, new char[][]{{65,90},{95,95},{97,122}}, new IEnterFilter[] {new CharPrecedeRestriction(new char[][]{{65,90},{95,95},{97,122}})}, null), new ListStackNode(2228, 1, regular__iter_star__char_class___range__48_57_range__65_90_range__95_95_range__97_122, new CharStackNode(2230, 0, new char[][]{{48,57},{65,90},{95,95},{97,122}}, null, null), false, null, new ICompletionFilter[] {new CharFollowRestriction(new char[][]{{48,57},{65,90},{95,95},{97,122}})})}, null, new ICompletionFilter[] {new StringMatchRestriction(new char[] {105,110}), new StringMatchRestriction(new char[] {105,109,112,111,114,116}), new StringMatchRestriction(new char[] {97,108,108}), new StringMatchRestriction(new char[] {102,97,108,115,101}), new StringMatchRestriction(new char[] {97,110,110,111}), new StringMatchRestriction(new char[] {98,114,97,99,107,101,116}), new StringMatchRestriction(new char[] {108,97,121,111,117,116}), new StringMatchRestriction(new char[] {106,111,105,110}), new StringMatchRestriction(new char[] {100,97,116,97}), new StringMatchRestriction(new char[] {105,116}), new StringMatchRestriction(new char[] {115,119,105,116,99,104}), new StringMatchRestriction(new char[] {99,97,115,101}), new StringMatchRestriction(new char[] {114,101,116,117,114,110}), new StringMatchRestriction(new char[] {115,116,114}), new StringMatchRestriction(new char[] {119,104,105,108,101}), new StringMatchRestriction(new char[] {115,111,108,118,101}), new StringMatchRestriction(new char[] {100,121,110,97,109,105,99}), new StringMatchRestriction(new char[] {110,111,116,105,110}), new StringMatchRestriction(new char[] {101,108,115,101}), new StringMatchRestriction(new char[] {105,110,115,101,114,116}), new StringMatchRestriction(new char[] {116,121,112,101}), new StringMatchRestriction(new char[] {99,97,116,99,104}), new StringMatchRestriction(new char[] {116,114,121}), new StringMatchRestriction(new char[] {110,117,109}), new StringMatchRestriction(new char[] {110,111,100,101}), new StringMatchRestriction(new char[] {102,105,110,97,108,108,121}), new StringMatchRestriction(new char[] {112,114,105,118,97,116,101}), new StringMatchRestriction(new char[] {116,114,117,101}), new StringMatchRestriction(new char[] {98,97,103}), new StringMatchRestriction(new char[] {118,111,105,100}), new StringMatchRestriction(new char[] {97,115,115,111,99}), new StringMatchRestriction(new char[] {110,111,110,45,97,115,115,111,99}), new StringMatchRestriction(new char[] {116,101,115,116}), new StringMatchRestriction(new char[] {105,102}), new StringMatchRestriction(new char[] {114,101,97,108}), new StringMatchRestriction(new char[] {108,105,115,116}), new StringMatchRestriction(new char[] {102,97,105,108}), new StringMatchRestriction(new char[] {114,101,108}), new StringMatchRestriction(new char[] {116,97,103}), new StringMatchRestriction(new char[] {97,112,112,101,110,100}), new StringMatchRestriction(new char[] {101,120,116,101,110,100}), new StringMatchRestriction(new char[] {114,101,112,101,97,116}), new StringMatchRestriction(new char[] {111,110,101}), new StringMatchRestriction(new char[] {116,104,114,111,119}), new StringMatchRestriction(new char[] {115,101,116}), new StringMatchRestriction(new char[] {115,116,97,114,116}), new StringMatchRestriction(new char[] {97,110,121}), new StringMatchRestriction(new char[] {109,111,100,117,108,101}), new StringMatchRestriction(new char[] {105,110,116}), new StringMatchRestriction(new char[] {112,117,98,108,105,99}), new StringMatchRestriction(new char[] {98,111,111,108}), new StringMatchRestriction(new char[] {118,97,108,117,101}), new StringMatchRestriction(new char[] {110,111,110,45,116,101,114,109,105,110,97,108}), new StringMatchRestriction(new char[] {114,117,108,101}), new StringMatchRestriction(new char[] {102,117,110}), new StringMatchRestriction(new char[] {99,111,110,115,116,114,117,99,116,111,114}), new StringMatchRestriction(new char[] {100,97,116,101,116,105,109,101}), new StringMatchRestriction(new char[] {97,115,115,101,114,116}), new StringMatchRestriction(new char[] {108,111,99}), new StringMatchRestriction(new char[] {100,101,102,97,117,108,116}), new StringMatchRestriction(new char[] {116,104,114,111,119,115}), new StringMatchRestriction(new char[] {116,117,112,108,101}), new StringMatchRestriction(new char[] {102,111,114}), new StringMatchRestriction(new char[] {118,105,115,105,116}), new StringMatchRestriction(new char[] {97,108,105,97,115}), new StringMatchRestriction(new char[] {109,97,112})});
      builder.addAlternative(ObjectRascalRascal.prod__Name__conditional__seq___conditional__char_class___range__65_90_range__95_95_range__97_122__not_precede__char_class___range__65_90_range__95_95_range__97_122_conditional__iter_star__char_class___range__48_57_range__65_90_range__95_95_range__97_122__not_follow__char_class___range__48_57_range__65_90_range__95_95_range__97_122__delete__RascalKeywords_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__Name__char_class___range__92_92_char_class___range__65_90_range__95_95_range__97_122_conditional__iter_star__char_class___range__45_45_range__48_57_range__65_90_range__95_95_range__97_122__not_follow__char_class___range__45_45_range__48_57_range__65_90_range__95_95_range__97_122_(builder);
      
        _init_prod__Name__conditional__seq___conditional__char_class___range__65_90_range__95_95_range__97_122__not_precede__char_class___range__65_90_range__95_95_range__97_122_conditional__iter_star__char_class___range__48_57_range__65_90_range__95_95_range__97_122__not_follow__char_class___range__48_57_range__65_90_range__95_95_range__97_122__delete__RascalKeywords_(builder);
      
    }
  }
	
  protected static class TimePartNoTZ {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__TimePartNoTZ__char_class___range__48_50_char_class___range__48_57_char_class___range__48_53_char_class___range__48_57_char_class___range__48_53_char_class___range__48_57_opt__seq___char_class___range__44_44_range__46_46_char_class___range__48_57_opt__seq___char_class___range__48_57_opt__char_class___range__48_57_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[7];
      
      tmp[6] = new OptionalStackNode(2264, 6, regular__opt__seq___char_class___range__44_44_range__46_46_char_class___range__48_57_opt__seq___char_class___range__48_57_opt__char_class___range__48_57, new SequenceStackNode(2266, 0, regular__seq___char_class___range__44_44_range__46_46_char_class___range__48_57_opt__seq___char_class___range__48_57_opt__char_class___range__48_57, new AbstractStackNode[]{new CharStackNode(2268, 0, new char[][]{{44,44},{46,46}}, null, null), new CharStackNode(2270, 1, new char[][]{{48,57}}, null, null), new OptionalStackNode(2272, 2, regular__opt__seq___char_class___range__48_57_opt__char_class___range__48_57, new SequenceStackNode(2274, 0, regular__seq___char_class___range__48_57_opt__char_class___range__48_57, new AbstractStackNode[]{new CharStackNode(2276, 0, new char[][]{{48,57}}, null, null), new OptionalStackNode(2278, 1, regular__opt__char_class___range__48_57, new CharStackNode(2280, 0, new char[][]{{48,57}}, null, null), null, null)}, null, null), null, null)}, null, null), null, null);
      tmp[5] = new CharStackNode(2262, 5, new char[][]{{48,57}}, null, null);
      tmp[4] = new CharStackNode(2260, 4, new char[][]{{48,53}}, null, null);
      tmp[3] = new CharStackNode(2258, 3, new char[][]{{48,57}}, null, null);
      tmp[2] = new CharStackNode(2256, 2, new char[][]{{48,53}}, null, null);
      tmp[1] = new CharStackNode(2254, 1, new char[][]{{48,57}}, null, null);
      tmp[0] = new CharStackNode(2252, 0, new char[][]{{48,50}}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__TimePartNoTZ__char_class___range__48_50_char_class___range__48_57_char_class___range__48_53_char_class___range__48_57_char_class___range__48_53_char_class___range__48_57_opt__seq___char_class___range__44_44_range__46_46_char_class___range__48_57_opt__seq___char_class___range__48_57_opt__char_class___range__48_57_, tmp);
	}
    protected static final void _init_prod__TimePartNoTZ__char_class___range__48_50_char_class___range__48_57_lit___58_char_class___range__48_53_char_class___range__48_57_lit___58_char_class___range__48_53_char_class___range__48_57_opt__seq___char_class___range__44_44_range__46_46_char_class___range__48_57_opt__seq___char_class___range__48_57_opt__char_class___range__48_57_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[9];
      
      tmp[8] = new OptionalStackNode(2298, 8, regular__opt__seq___char_class___range__44_44_range__46_46_char_class___range__48_57_opt__seq___char_class___range__48_57_opt__char_class___range__48_57, new SequenceStackNode(2300, 0, regular__seq___char_class___range__44_44_range__46_46_char_class___range__48_57_opt__seq___char_class___range__48_57_opt__char_class___range__48_57, new AbstractStackNode[]{new CharStackNode(2302, 0, new char[][]{{44,44},{46,46}}, null, null), new CharStackNode(2304, 1, new char[][]{{48,57}}, null, null), new OptionalStackNode(2306, 2, regular__opt__seq___char_class___range__48_57_opt__char_class___range__48_57, new SequenceStackNode(2308, 0, regular__seq___char_class___range__48_57_opt__char_class___range__48_57, new AbstractStackNode[]{new CharStackNode(2310, 0, new char[][]{{48,57}}, null, null), new OptionalStackNode(2312, 1, regular__opt__char_class___range__48_57, new CharStackNode(2314, 0, new char[][]{{48,57}}, null, null), null, null)}, null, null), null, null)}, null, null), null, null);
      tmp[7] = new CharStackNode(2296, 7, new char[][]{{48,57}}, null, null);
      tmp[6] = new CharStackNode(2294, 6, new char[][]{{48,53}}, null, null);
      tmp[5] = new LiteralStackNode(2292, 5, prod__lit___58__char_class___range__58_58_, new char[] {58}, null, null);
      tmp[4] = new CharStackNode(2290, 4, new char[][]{{48,57}}, null, null);
      tmp[3] = new CharStackNode(2288, 3, new char[][]{{48,53}}, null, null);
      tmp[2] = new LiteralStackNode(2286, 2, prod__lit___58__char_class___range__58_58_, new char[] {58}, null, null);
      tmp[1] = new CharStackNode(2284, 1, new char[][]{{48,57}}, null, null);
      tmp[0] = new CharStackNode(2282, 0, new char[][]{{48,50}}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__TimePartNoTZ__char_class___range__48_50_char_class___range__48_57_lit___58_char_class___range__48_53_char_class___range__48_57_lit___58_char_class___range__48_53_char_class___range__48_57_opt__seq___char_class___range__44_44_range__46_46_char_class___range__48_57_opt__seq___char_class___range__48_57_opt__char_class___range__48_57_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__TimePartNoTZ__char_class___range__48_50_char_class___range__48_57_char_class___range__48_53_char_class___range__48_57_char_class___range__48_53_char_class___range__48_57_opt__seq___char_class___range__44_44_range__46_46_char_class___range__48_57_opt__seq___char_class___range__48_57_opt__char_class___range__48_57_(builder);
      
        _init_prod__TimePartNoTZ__char_class___range__48_50_char_class___range__48_57_lit___58_char_class___range__48_53_char_class___range__48_57_lit___58_char_class___range__48_53_char_class___range__48_57_opt__seq___char_class___range__44_44_range__46_46_char_class___range__48_57_opt__seq___char_class___range__48_57_opt__char_class___range__48_57_(builder);
      
    }
  }
	
  protected static class StructuredType {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__Default_StructuredType__basicType_BasicType_layouts_LAYOUTLIST_lit___91_layouts_LAYOUTLIST_arguments_iter_seps__TypeArg__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___93_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[7];
      
      tmp[6] = new LiteralStackNode(2356, 6, prod__lit___93__char_class___range__93_93_, new char[] {93}, null, null);
      tmp[5] = new NonTerminalStackNode(2354, 5, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new SeparatedListStackNode(2344, 4, regular__iter_seps__TypeArg__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST, new NonTerminalStackNode(2346, 0, "TypeArg", null, null), new AbstractStackNode[]{new NonTerminalStackNode(2348, 1, "layouts_LAYOUTLIST", null, null), new LiteralStackNode(2350, 2, prod__lit___44__char_class___range__44_44_, new char[] {44}, null, null), new NonTerminalStackNode(2352, 3, "layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[3] = new NonTerminalStackNode(2342, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(2340, 2, prod__lit___91__char_class___range__91_91_, new char[] {91}, null, null);
      tmp[1] = new NonTerminalStackNode(2338, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(2336, 0, "BasicType", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Default_StructuredType__basicType_BasicType_layouts_LAYOUTLIST_lit___91_layouts_LAYOUTLIST_arguments_iter_seps__TypeArg__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___93_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__Default_StructuredType__basicType_BasicType_layouts_LAYOUTLIST_lit___91_layouts_LAYOUTLIST_arguments_iter_seps__TypeArg__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___93_(builder);
      
    }
  }
	
  protected static class Declarator {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__Default_Declarator__type_Type_layouts_LAYOUTLIST_variables_iter_seps__Variable__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[2] = new SeparatedListStackNode(2380, 2, regular__iter_seps__Variable__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST, new NonTerminalStackNode(2382, 0, "Variable", null, null), new AbstractStackNode[]{new NonTerminalStackNode(2384, 1, "layouts_LAYOUTLIST", null, null), new LiteralStackNode(2386, 2, prod__lit___44__char_class___range__44_44_, new char[] {44}, null, null), new NonTerminalStackNode(2388, 3, "layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[1] = new NonTerminalStackNode(2378, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(2376, 0, "Type", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Default_Declarator__type_Type_layouts_LAYOUTLIST_variables_iter_seps__Variable__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__Default_Declarator__type_Type_layouts_LAYOUTLIST_variables_iter_seps__Variable__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_(builder);
      
    }
  }
	
  protected static class start__PreModule {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__start__PreModule__layouts_LAYOUTLIST_top_PreModule_layouts_LAYOUTLIST_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode(2400, 2, "layouts_LAYOUTLIST", null, null);
      tmp[1] = new NonTerminalStackNode(2398, 1, "PreModule", null, null);
      tmp[0] = new NonTerminalStackNode(2396, 0, "layouts_LAYOUTLIST", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__start__PreModule__layouts_LAYOUTLIST_top_PreModule_layouts_LAYOUTLIST_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__start__PreModule__layouts_LAYOUTLIST_top_PreModule_layouts_LAYOUTLIST_(builder);
      
    }
  }
	
  protected static class Variant {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__NAryConstructor_Variant__name_Name_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_arguments_iter_star_seps__TypeArg__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[7];
      
      tmp[6] = new LiteralStackNode(2450, 6, prod__lit___41__char_class___range__41_41_, new char[] {41}, null, null);
      tmp[5] = new NonTerminalStackNode(2448, 5, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new SeparatedListStackNode(2438, 4, regular__iter_star_seps__TypeArg__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST, new NonTerminalStackNode(2440, 0, "TypeArg", null, null), new AbstractStackNode[]{new NonTerminalStackNode(2442, 1, "layouts_LAYOUTLIST", null, null), new LiteralStackNode(2444, 2, prod__lit___44__char_class___range__44_44_, new char[] {44}, null, null), new NonTerminalStackNode(2446, 3, "layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[3] = new NonTerminalStackNode(2436, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(2434, 2, prod__lit___40__char_class___range__40_40_, new char[] {40}, null, null);
      tmp[1] = new NonTerminalStackNode(2432, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(2430, 0, "Name", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__NAryConstructor_Variant__name_Name_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_arguments_iter_star_seps__TypeArg__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__NAryConstructor_Variant__name_Name_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_arguments_iter_star_seps__TypeArg__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_(builder);
      
    }
  }
	
  protected static class FunctionDeclaration {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__Conditional_FunctionDeclaration__tags_Tags_layouts_LAYOUTLIST_visibility_Visibility_layouts_LAYOUTLIST_signature_Signature_layouts_LAYOUTLIST_lit___61_layouts_LAYOUTLIST_expression_Expression_layouts_LAYOUTLIST_lit_when_layouts_LAYOUTLIST_conditions_iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___59__tag__Foldable(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[15];
      
      tmp[14] = new LiteralStackNode(2488, 14, prod__lit___59__char_class___range__59_59_, new char[] {59}, null, null);
      tmp[13] = new NonTerminalStackNode(2486, 13, "layouts_LAYOUTLIST", null, null);
      tmp[12] = new SeparatedListStackNode(2476, 12, regular__iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST, new NonTerminalStackNode(2478, 0, "Expression", null, null), new AbstractStackNode[]{new NonTerminalStackNode(2480, 1, "layouts_LAYOUTLIST", null, null), new LiteralStackNode(2482, 2, prod__lit___44__char_class___range__44_44_, new char[] {44}, null, null), new NonTerminalStackNode(2484, 3, "layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[11] = new NonTerminalStackNode(2474, 11, "layouts_LAYOUTLIST", null, null);
      tmp[10] = new LiteralStackNode(2472, 10, prod__lit_when__char_class___range__119_119_char_class___range__104_104_char_class___range__101_101_char_class___range__110_110_, new char[] {119,104,101,110}, null, null);
      tmp[9] = new NonTerminalStackNode(2470, 9, "layouts_LAYOUTLIST", null, null);
      tmp[8] = new NonTerminalStackNode(2468, 8, "Expression", null, null);
      tmp[7] = new NonTerminalStackNode(2466, 7, "layouts_LAYOUTLIST", null, null);
      tmp[6] = new LiteralStackNode(2464, 6, prod__lit___61__char_class___range__61_61_, new char[] {61}, null, null);
      tmp[5] = new NonTerminalStackNode(2462, 5, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new NonTerminalStackNode(2460, 4, "Signature", null, null);
      tmp[3] = new NonTerminalStackNode(2458, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(2456, 2, "Visibility", null, null);
      tmp[1] = new NonTerminalStackNode(2454, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(2452, 0, "Tags", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Conditional_FunctionDeclaration__tags_Tags_layouts_LAYOUTLIST_visibility_Visibility_layouts_LAYOUTLIST_signature_Signature_layouts_LAYOUTLIST_lit___61_layouts_LAYOUTLIST_expression_Expression_layouts_LAYOUTLIST_lit_when_layouts_LAYOUTLIST_conditions_iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___59__tag__Foldable, tmp);
	}
    protected static final void _init_prod__Default_FunctionDeclaration__tags_Tags_layouts_LAYOUTLIST_visibility_Visibility_layouts_LAYOUTLIST_signature_Signature_layouts_LAYOUTLIST_body_FunctionBody__tag__Foldable(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[7];
      
      tmp[6] = new NonTerminalStackNode(2502, 6, "FunctionBody", null, null);
      tmp[5] = new NonTerminalStackNode(2500, 5, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new NonTerminalStackNode(2498, 4, "Signature", null, null);
      tmp[3] = new NonTerminalStackNode(2496, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(2494, 2, "Visibility", null, null);
      tmp[1] = new NonTerminalStackNode(2492, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(2490, 0, "Tags", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Default_FunctionDeclaration__tags_Tags_layouts_LAYOUTLIST_visibility_Visibility_layouts_LAYOUTLIST_signature_Signature_layouts_LAYOUTLIST_body_FunctionBody__tag__Foldable, tmp);
	}
    protected static final void _init_prod__Abstract_FunctionDeclaration__tags_Tags_layouts_LAYOUTLIST_visibility_Visibility_layouts_LAYOUTLIST_signature_Signature_layouts_LAYOUTLIST_lit___59_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[7];
      
      tmp[6] = new LiteralStackNode(2516, 6, prod__lit___59__char_class___range__59_59_, new char[] {59}, null, null);
      tmp[5] = new NonTerminalStackNode(2514, 5, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new NonTerminalStackNode(2512, 4, "Signature", null, null);
      tmp[3] = new NonTerminalStackNode(2510, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(2508, 2, "Visibility", null, null);
      tmp[1] = new NonTerminalStackNode(2506, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(2504, 0, "Tags", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Abstract_FunctionDeclaration__tags_Tags_layouts_LAYOUTLIST_visibility_Visibility_layouts_LAYOUTLIST_signature_Signature_layouts_LAYOUTLIST_lit___59_, tmp);
	}
    protected static final void _init_prod__Expression_FunctionDeclaration__tags_Tags_layouts_LAYOUTLIST_visibility_Visibility_layouts_LAYOUTLIST_signature_Signature_layouts_LAYOUTLIST_lit___61_layouts_LAYOUTLIST_expression_Expression_layouts_LAYOUTLIST_lit___59__tag__Foldable(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[11];
      
      tmp[10] = new LiteralStackNode(2538, 10, prod__lit___59__char_class___range__59_59_, new char[] {59}, null, null);
      tmp[9] = new NonTerminalStackNode(2536, 9, "layouts_LAYOUTLIST", null, null);
      tmp[8] = new NonTerminalStackNode(2534, 8, "Expression", null, null);
      tmp[7] = new NonTerminalStackNode(2532, 7, "layouts_LAYOUTLIST", null, null);
      tmp[6] = new LiteralStackNode(2530, 6, prod__lit___61__char_class___range__61_61_, new char[] {61}, null, null);
      tmp[5] = new NonTerminalStackNode(2528, 5, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new NonTerminalStackNode(2526, 4, "Signature", null, null);
      tmp[3] = new NonTerminalStackNode(2524, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(2522, 2, "Visibility", null, null);
      tmp[1] = new NonTerminalStackNode(2520, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(2518, 0, "Tags", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Expression_FunctionDeclaration__tags_Tags_layouts_LAYOUTLIST_visibility_Visibility_layouts_LAYOUTLIST_signature_Signature_layouts_LAYOUTLIST_lit___61_layouts_LAYOUTLIST_expression_Expression_layouts_LAYOUTLIST_lit___59__tag__Foldable, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__Conditional_FunctionDeclaration__tags_Tags_layouts_LAYOUTLIST_visibility_Visibility_layouts_LAYOUTLIST_signature_Signature_layouts_LAYOUTLIST_lit___61_layouts_LAYOUTLIST_expression_Expression_layouts_LAYOUTLIST_lit_when_layouts_LAYOUTLIST_conditions_iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___59__tag__Foldable(builder);
      
        _init_prod__Default_FunctionDeclaration__tags_Tags_layouts_LAYOUTLIST_visibility_Visibility_layouts_LAYOUTLIST_signature_Signature_layouts_LAYOUTLIST_body_FunctionBody__tag__Foldable(builder);
      
        _init_prod__Abstract_FunctionDeclaration__tags_Tags_layouts_LAYOUTLIST_visibility_Visibility_layouts_LAYOUTLIST_signature_Signature_layouts_LAYOUTLIST_lit___59_(builder);
      
        _init_prod__Expression_FunctionDeclaration__tags_Tags_layouts_LAYOUTLIST_visibility_Visibility_layouts_LAYOUTLIST_signature_Signature_layouts_LAYOUTLIST_lit___61_layouts_LAYOUTLIST_expression_Expression_layouts_LAYOUTLIST_lit___59__tag__Foldable(builder);
      
    }
  }
	
  protected static class BasicType {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__Bag_BasicType__lit_bag_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(2566, 0, prod__lit_bag__char_class___range__98_98_char_class___range__97_97_char_class___range__103_103_, new char[] {98,97,103}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Bag_BasicType__lit_bag_, tmp);
	}
    protected static final void _init_prod__ReifiedTypeParameter_BasicType__lit_parameter_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(2568, 0, prod__lit_parameter__char_class___range__112_112_char_class___range__97_97_char_class___range__114_114_char_class___range__97_97_char_class___range__109_109_char_class___range__101_101_char_class___range__116_116_char_class___range__101_101_char_class___range__114_114_, new char[] {112,97,114,97,109,101,116,101,114}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__ReifiedTypeParameter_BasicType__lit_parameter_, tmp);
	}
    protected static final void _init_prod__ReifiedType_BasicType__lit_type_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(2570, 0, prod__lit_type__char_class___range__116_116_char_class___range__121_121_char_class___range__112_112_char_class___range__101_101_, new char[] {116,121,112,101}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__ReifiedType_BasicType__lit_type_, tmp);
	}
    protected static final void _init_prod__ReifiedReifiedType_BasicType__lit_reified_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(2572, 0, prod__lit_reified__char_class___range__114_114_char_class___range__101_101_char_class___range__105_105_char_class___range__102_102_char_class___range__105_105_char_class___range__101_101_char_class___range__100_100_, new char[] {114,101,105,102,105,101,100}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__ReifiedReifiedType_BasicType__lit_reified_, tmp);
	}
    protected static final void _init_prod__String_BasicType__lit_str_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(2574, 0, prod__lit_str__char_class___range__115_115_char_class___range__116_116_char_class___range__114_114_, new char[] {115,116,114}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__String_BasicType__lit_str_, tmp);
	}
    protected static final void _init_prod__DateTime_BasicType__lit_datetime_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(2576, 0, prod__lit_datetime__char_class___range__100_100_char_class___range__97_97_char_class___range__116_116_char_class___range__101_101_char_class___range__116_116_char_class___range__105_105_char_class___range__109_109_char_class___range__101_101_, new char[] {100,97,116,101,116,105,109,101}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__DateTime_BasicType__lit_datetime_, tmp);
	}
    protected static final void _init_prod__Set_BasicType__lit_set_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(2578, 0, prod__lit_set__char_class___range__115_115_char_class___range__101_101_char_class___range__116_116_, new char[] {115,101,116}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Set_BasicType__lit_set_, tmp);
	}
    protected static final void _init_prod__ReifiedAdt_BasicType__lit_adt_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(2580, 0, prod__lit_adt__char_class___range__97_97_char_class___range__100_100_char_class___range__116_116_, new char[] {97,100,116}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__ReifiedAdt_BasicType__lit_adt_, tmp);
	}
    protected static final void _init_prod__Bool_BasicType__lit_bool_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(2582, 0, prod__lit_bool__char_class___range__98_98_char_class___range__111_111_char_class___range__111_111_char_class___range__108_108_, new char[] {98,111,111,108}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Bool_BasicType__lit_bool_, tmp);
	}
    protected static final void _init_prod__ReifiedConstructor_BasicType__lit_constructor_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(2584, 0, prod__lit_constructor__char_class___range__99_99_char_class___range__111_111_char_class___range__110_110_char_class___range__115_115_char_class___range__116_116_char_class___range__114_114_char_class___range__117_117_char_class___range__99_99_char_class___range__116_116_char_class___range__111_111_char_class___range__114_114_, new char[] {99,111,110,115,116,114,117,99,116,111,114}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__ReifiedConstructor_BasicType__lit_constructor_, tmp);
	}
    protected static final void _init_prod__Relation_BasicType__lit_rel_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(2586, 0, prod__lit_rel__char_class___range__114_114_char_class___range__101_101_char_class___range__108_108_, new char[] {114,101,108}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Relation_BasicType__lit_rel_, tmp);
	}
    protected static final void _init_prod__Void_BasicType__lit_void_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(2588, 0, prod__lit_void__char_class___range__118_118_char_class___range__111_111_char_class___range__105_105_char_class___range__100_100_, new char[] {118,111,105,100}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Void_BasicType__lit_void_, tmp);
	}
    protected static final void _init_prod__List_BasicType__lit_list_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(2590, 0, prod__lit_list__char_class___range__108_108_char_class___range__105_105_char_class___range__115_115_char_class___range__116_116_, new char[] {108,105,115,116}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__List_BasicType__lit_list_, tmp);
	}
    protected static final void _init_prod__ReifiedNonTerminal_BasicType__lit_non_terminal_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(2592, 0, prod__lit_non_terminal__char_class___range__110_110_char_class___range__111_111_char_class___range__110_110_char_class___range__45_45_char_class___range__116_116_char_class___range__101_101_char_class___range__114_114_char_class___range__109_109_char_class___range__105_105_char_class___range__110_110_char_class___range__97_97_char_class___range__108_108_, new char[] {110,111,110,45,116,101,114,109,105,110,97,108}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__ReifiedNonTerminal_BasicType__lit_non_terminal_, tmp);
	}
    protected static final void _init_prod__Num_BasicType__lit_num_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(2594, 0, prod__lit_num__char_class___range__110_110_char_class___range__117_117_char_class___range__109_109_, new char[] {110,117,109}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Num_BasicType__lit_num_, tmp);
	}
    protected static final void _init_prod__Loc_BasicType__lit_loc_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(2596, 0, prod__lit_loc__char_class___range__108_108_char_class___range__111_111_char_class___range__99_99_, new char[] {108,111,99}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Loc_BasicType__lit_loc_, tmp);
	}
    protected static final void _init_prod__Value_BasicType__lit_value_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(2598, 0, prod__lit_value__char_class___range__118_118_char_class___range__97_97_char_class___range__108_108_char_class___range__117_117_char_class___range__101_101_, new char[] {118,97,108,117,101}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Value_BasicType__lit_value_, tmp);
	}
    protected static final void _init_prod__ReifiedFunction_BasicType__lit_fun_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(2600, 0, prod__lit_fun__char_class___range__102_102_char_class___range__117_117_char_class___range__110_110_, new char[] {102,117,110}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__ReifiedFunction_BasicType__lit_fun_, tmp);
	}
    protected static final void _init_prod__Tuple_BasicType__lit_tuple_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(2602, 0, prod__lit_tuple__char_class___range__116_116_char_class___range__117_117_char_class___range__112_112_char_class___range__108_108_char_class___range__101_101_, new char[] {116,117,112,108,101}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Tuple_BasicType__lit_tuple_, tmp);
	}
    protected static final void _init_prod__Map_BasicType__lit_map_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(2604, 0, prod__lit_map__char_class___range__109_109_char_class___range__97_97_char_class___range__112_112_, new char[] {109,97,112}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Map_BasicType__lit_map_, tmp);
	}
    protected static final void _init_prod__Int_BasicType__lit_int_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(2606, 0, prod__lit_int__char_class___range__105_105_char_class___range__110_110_char_class___range__116_116_, new char[] {105,110,116}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Int_BasicType__lit_int_, tmp);
	}
    protected static final void _init_prod__Node_BasicType__lit_node_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(2608, 0, prod__lit_node__char_class___range__110_110_char_class___range__111_111_char_class___range__100_100_char_class___range__101_101_, new char[] {110,111,100,101}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Node_BasicType__lit_node_, tmp);
	}
    protected static final void _init_prod__Real_BasicType__lit_real_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(2610, 0, prod__lit_real__char_class___range__114_114_char_class___range__101_101_char_class___range__97_97_char_class___range__108_108_, new char[] {114,101,97,108}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Real_BasicType__lit_real_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__Bag_BasicType__lit_bag_(builder);
      
        _init_prod__ReifiedTypeParameter_BasicType__lit_parameter_(builder);
      
        _init_prod__ReifiedType_BasicType__lit_type_(builder);
      
        _init_prod__ReifiedReifiedType_BasicType__lit_reified_(builder);
      
        _init_prod__String_BasicType__lit_str_(builder);
      
        _init_prod__DateTime_BasicType__lit_datetime_(builder);
      
        _init_prod__Set_BasicType__lit_set_(builder);
      
        _init_prod__ReifiedAdt_BasicType__lit_adt_(builder);
      
        _init_prod__Bool_BasicType__lit_bool_(builder);
      
        _init_prod__ReifiedConstructor_BasicType__lit_constructor_(builder);
      
        _init_prod__Relation_BasicType__lit_rel_(builder);
      
        _init_prod__Void_BasicType__lit_void_(builder);
      
        _init_prod__List_BasicType__lit_list_(builder);
      
        _init_prod__ReifiedNonTerminal_BasicType__lit_non_terminal_(builder);
      
        _init_prod__Num_BasicType__lit_num_(builder);
      
        _init_prod__Loc_BasicType__lit_loc_(builder);
      
        _init_prod__Value_BasicType__lit_value_(builder);
      
        _init_prod__ReifiedFunction_BasicType__lit_fun_(builder);
      
        _init_prod__Tuple_BasicType__lit_tuple_(builder);
      
        _init_prod__Map_BasicType__lit_map_(builder);
      
        _init_prod__Int_BasicType__lit_int_(builder);
      
        _init_prod__Node_BasicType__lit_node_(builder);
      
        _init_prod__Real_BasicType__lit_real_(builder);
      
    }
  }
	
  protected static class DateTimeLiteral {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__DateAndTimeLiteral_DateTimeLiteral__dateAndTime_DateAndTime_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode(2614, 0, "DateAndTime", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__DateAndTimeLiteral_DateTimeLiteral__dateAndTime_DateAndTime_, tmp);
	}
    protected static final void _init_prod__TimeLiteral_DateTimeLiteral__time_JustTime_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode(2616, 0, "JustTime", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__TimeLiteral_DateTimeLiteral__time_JustTime_, tmp);
	}
    protected static final void _init_prod__DateLiteral_DateTimeLiteral__date_JustDate_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode(2618, 0, "JustDate", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__DateLiteral_DateTimeLiteral__date_JustDate_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__DateAndTimeLiteral_DateTimeLiteral__dateAndTime_DateAndTime_(builder);
      
        _init_prod__TimeLiteral_DateTimeLiteral__time_JustTime_(builder);
      
        _init_prod__DateLiteral_DateTimeLiteral__date_JustDate_(builder);
      
    }
  }
	
  protected static class PathChars {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__PathChars__URLChars_char_class___range__124_124_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[2];
      
      tmp[1] = new CharStackNode(2638, 1, new char[][]{{124,124}}, null, null);
      tmp[0] = new NonTerminalStackNode(2636, 0, "URLChars", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__PathChars__URLChars_char_class___range__124_124_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__PathChars__URLChars_char_class___range__124_124_(builder);
      
    }
  }
	
  protected static class ModuleActuals {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__Default_ModuleActuals__lit___91_layouts_LAYOUTLIST_types_iter_seps__Type__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___93_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new LiteralStackNode(2732, 4, prod__lit___93__char_class___range__93_93_, new char[] {93}, null, null);
      tmp[3] = new NonTerminalStackNode(2730, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new SeparatedListStackNode(2720, 2, regular__iter_seps__Type__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST, new NonTerminalStackNode(2722, 0, "Type", null, null), new AbstractStackNode[]{new NonTerminalStackNode(2724, 1, "layouts_LAYOUTLIST", null, null), new LiteralStackNode(2726, 2, prod__lit___44__char_class___range__44_44_, new char[] {44}, null, null), new NonTerminalStackNode(2728, 3, "layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[1] = new NonTerminalStackNode(2718, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(2716, 0, prod__lit___91__char_class___range__91_91_, new char[] {91}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Default_ModuleActuals__lit___91_layouts_LAYOUTLIST_types_iter_seps__Type__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___93_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__Default_ModuleActuals__lit___91_layouts_LAYOUTLIST_types_iter_seps__Type__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___93_(builder);
      
    }
  }
	
  protected static class PostStringChars {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__PostStringChars__char_class___range__62_62_iter_star__StringCharacter_char_class___range__34_34__tag__category___67_111_110_115_116_97_110_116(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[2] = new CharStackNode(2744, 2, new char[][]{{34,34}}, null, null);
      tmp[1] = new ListStackNode(2740, 1, regular__iter_star__StringCharacter, new NonTerminalStackNode(2742, 0, "StringCharacter", null, null), false, null, null);
      tmp[0] = new CharStackNode(2738, 0, new char[][]{{62,62}}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__PostStringChars__char_class___range__62_62_iter_star__StringCharacter_char_class___range__34_34__tag__category___67_111_110_115_116_97_110_116, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__PostStringChars__char_class___range__62_62_iter_star__StringCharacter_char_class___range__34_34__tag__category___67_111_110_115_116_97_110_116(builder);
      
    }
  }
	
  protected static class HexIntegerLiteral {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__HexIntegerLiteral__char_class___range__48_48_char_class___range__88_88_range__120_120_conditional__iter__char_class___range__48_57_range__65_70_range__97_102__not_follow__char_class___range__48_57_range__65_90_range__95_95_range__97_122_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[2] = new ListStackNode(2750, 2, regular__iter__char_class___range__48_57_range__65_70_range__97_102, new CharStackNode(2752, 0, new char[][]{{48,57},{65,70},{97,102}}, null, null), true, null, new ICompletionFilter[] {new CharFollowRestriction(new char[][]{{48,57},{65,90},{95,95},{97,122}})});
      tmp[1] = new CharStackNode(2748, 1, new char[][]{{88,88},{120,120}}, null, null);
      tmp[0] = new CharStackNode(2746, 0, new char[][]{{48,48}}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__HexIntegerLiteral__char_class___range__48_48_char_class___range__88_88_range__120_120_conditional__iter__char_class___range__48_57_range__65_70_range__97_102__not_follow__char_class___range__48_57_range__65_90_range__95_95_range__97_122_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__HexIntegerLiteral__char_class___range__48_48_char_class___range__88_88_range__120_120_conditional__iter__char_class___range__48_57_range__65_70_range__97_102__not_follow__char_class___range__48_57_range__65_90_range__95_95_range__97_122_(builder);
      
    }
  }
	
  protected static class OctalEscapeSequence {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__OctalEscapeSequence__lit___92_conditional__char_class___range__48_55__not_follow__char_class___range__48_55_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[2];
      
      tmp[1] = new CharStackNode(2756, 1, new char[][]{{48,55}}, null, new ICompletionFilter[] {new CharFollowRestriction(new char[][]{{48,55}})});
      tmp[0] = new LiteralStackNode(2754, 0, prod__lit___92__char_class___range__92_92_, new char[] {92}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__OctalEscapeSequence__lit___92_conditional__char_class___range__48_55__not_follow__char_class___range__48_55_, tmp);
	}
    protected static final void _init_prod__OctalEscapeSequence__lit___92_char_class___range__48_55_conditional__char_class___range__48_55__not_follow__char_class___range__48_55_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[2] = new CharStackNode(2762, 2, new char[][]{{48,55}}, null, new ICompletionFilter[] {new CharFollowRestriction(new char[][]{{48,55}})});
      tmp[1] = new CharStackNode(2760, 1, new char[][]{{48,55}}, null, null);
      tmp[0] = new LiteralStackNode(2758, 0, prod__lit___92__char_class___range__92_92_, new char[] {92}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__OctalEscapeSequence__lit___92_char_class___range__48_55_conditional__char_class___range__48_55__not_follow__char_class___range__48_55_, tmp);
	}
    protected static final void _init_prod__OctalEscapeSequence__lit___92_char_class___range__48_51_char_class___range__48_55_char_class___range__48_55_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[4];
      
      tmp[3] = new CharStackNode(2770, 3, new char[][]{{48,55}}, null, null);
      tmp[2] = new CharStackNode(2768, 2, new char[][]{{48,55}}, null, null);
      tmp[1] = new CharStackNode(2766, 1, new char[][]{{48,51}}, null, null);
      tmp[0] = new LiteralStackNode(2764, 0, prod__lit___92__char_class___range__92_92_, new char[] {92}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__OctalEscapeSequence__lit___92_char_class___range__48_51_char_class___range__48_55_char_class___range__48_55_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__OctalEscapeSequence__lit___92_conditional__char_class___range__48_55__not_follow__char_class___range__48_55_(builder);
      
        _init_prod__OctalEscapeSequence__lit___92_char_class___range__48_55_conditional__char_class___range__48_55__not_follow__char_class___range__48_55_(builder);
      
        _init_prod__OctalEscapeSequence__lit___92_char_class___range__48_51_char_class___range__48_55_char_class___range__48_55_(builder);
      
    }
  }
	
  protected static class RegExpLiteral {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__RegExpLiteral__lit___47_iter_star__RegExp_lit___47_RegExpModifier_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[4];
      
      tmp[3] = new NonTerminalStackNode(2780, 3, "RegExpModifier", null, null);
      tmp[2] = new LiteralStackNode(2778, 2, prod__lit___47__char_class___range__47_47_, new char[] {47}, null, null);
      tmp[1] = new ListStackNode(2774, 1, regular__iter_star__RegExp, new NonTerminalStackNode(2776, 0, "RegExp", null, null), false, null, null);
      tmp[0] = new LiteralStackNode(2772, 0, prod__lit___47__char_class___range__47_47_, new char[] {47}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RegExpLiteral__lit___47_iter_star__RegExp_lit___47_RegExpModifier_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__RegExpLiteral__lit___47_iter_star__RegExp_lit___47_RegExpModifier_(builder);
      
    }
  }
	
  protected static class NamedRegExp {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__NamedRegExp__lit___60_Name_lit___62_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[2] = new LiteralStackNode(2786, 2, prod__lit___62__char_class___range__62_62_, new char[] {62}, null, null);
      tmp[1] = new NonTerminalStackNode(2784, 1, "Name", null, null);
      tmp[0] = new LiteralStackNode(2782, 0, prod__lit___60__char_class___range__60_60_, new char[] {60}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__NamedRegExp__lit___60_Name_lit___62_, tmp);
	}
    protected static final void _init_prod__NamedRegExp__char_class___range__0_46_range__48_59_range__61_61_range__63_91_range__93_65535_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new CharStackNode(2788, 0, new char[][]{{0,46},{48,59},{61,61},{63,91},{93,65535}}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__NamedRegExp__char_class___range__0_46_range__48_59_range__61_61_range__63_91_range__93_65535_, tmp);
	}
    protected static final void _init_prod__NamedRegExp__char_class___range__92_92_char_class___range__47_47_range__60_60_range__62_62_range__92_92_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[2];
      
      tmp[1] = new CharStackNode(2792, 1, new char[][]{{47,47},{60,60},{62,62},{92,92}}, null, null);
      tmp[0] = new CharStackNode(2790, 0, new char[][]{{92,92}}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__NamedRegExp__char_class___range__92_92_char_class___range__47_47_range__60_60_range__62_62_range__92_92_, tmp);
	}
    protected static final void _init_prod__NamedRegExp__NamedBackslash_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode(2794, 0, "NamedBackslash", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__NamedRegExp__NamedBackslash_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__NamedRegExp__lit___60_Name_lit___62_(builder);
      
        _init_prod__NamedRegExp__char_class___range__0_46_range__48_59_range__61_61_range__63_91_range__93_65535_(builder);
      
        _init_prod__NamedRegExp__char_class___range__92_92_char_class___range__47_47_range__60_60_range__62_62_range__92_92_(builder);
      
        _init_prod__NamedRegExp__NamedBackslash_(builder);
      
    }
  }
	
  protected static class ModuleParameters {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__Default_ModuleParameters__lit___91_layouts_LAYOUTLIST_parameters_iter_seps__TypeVar__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___93_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new LiteralStackNode(2818, 4, prod__lit___93__char_class___range__93_93_, new char[] {93}, null, null);
      tmp[3] = new NonTerminalStackNode(2816, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new SeparatedListStackNode(2806, 2, regular__iter_seps__TypeVar__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST, new NonTerminalStackNode(2808, 0, "TypeVar", null, null), new AbstractStackNode[]{new NonTerminalStackNode(2810, 1, "layouts_LAYOUTLIST", null, null), new LiteralStackNode(2812, 2, prod__lit___44__char_class___range__44_44_, new char[] {44}, null, null), new NonTerminalStackNode(2814, 3, "layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[1] = new NonTerminalStackNode(2804, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(2802, 0, prod__lit___91__char_class___range__91_91_, new char[] {91}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Default_ModuleParameters__lit___91_layouts_LAYOUTLIST_parameters_iter_seps__TypeVar__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___93_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__Default_ModuleParameters__lit___91_layouts_LAYOUTLIST_parameters_iter_seps__TypeVar__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___93_(builder);
      
    }
  }
	
  protected static class RascalKeywords {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__RascalKeywords__lit_tuple_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(2834, 0, prod__lit_tuple__char_class___range__116_116_char_class___range__117_117_char_class___range__112_112_char_class___range__108_108_char_class___range__101_101_, new char[] {116,117,112,108,101}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RascalKeywords__lit_tuple_, tmp);
	}
    protected static final void _init_prod__RascalKeywords__lit_constructor_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(2836, 0, prod__lit_constructor__char_class___range__99_99_char_class___range__111_111_char_class___range__110_110_char_class___range__115_115_char_class___range__116_116_char_class___range__114_114_char_class___range__117_117_char_class___range__99_99_char_class___range__116_116_char_class___range__111_111_char_class___range__114_114_, new char[] {99,111,110,115,116,114,117,99,116,111,114}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RascalKeywords__lit_constructor_, tmp);
	}
    protected static final void _init_prod__RascalKeywords__lit_int_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(2838, 0, prod__lit_int__char_class___range__105_105_char_class___range__110_110_char_class___range__116_116_, new char[] {105,110,116}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RascalKeywords__lit_int_, tmp);
	}
    protected static final void _init_prod__RascalKeywords__lit_fail_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(2840, 0, prod__lit_fail__char_class___range__102_102_char_class___range__97_97_char_class___range__105_105_char_class___range__108_108_, new char[] {102,97,105,108}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RascalKeywords__lit_fail_, tmp);
	}
    protected static final void _init_prod__RascalKeywords__lit_switch_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(2842, 0, prod__lit_switch__char_class___range__115_115_char_class___range__119_119_char_class___range__105_105_char_class___range__116_116_char_class___range__99_99_char_class___range__104_104_, new char[] {115,119,105,116,99,104}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RascalKeywords__lit_switch_, tmp);
	}
    protected static final void _init_prod__RascalKeywords__lit_rule_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(2844, 0, prod__lit_rule__char_class___range__114_114_char_class___range__117_117_char_class___range__108_108_char_class___range__101_101_, new char[] {114,117,108,101}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RascalKeywords__lit_rule_, tmp);
	}
    protected static final void _init_prod__RascalKeywords__lit_throw_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(2846, 0, prod__lit_throw__char_class___range__116_116_char_class___range__104_104_char_class___range__114_114_char_class___range__111_111_char_class___range__119_119_, new char[] {116,104,114,111,119}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RascalKeywords__lit_throw_, tmp);
	}
    protected static final void _init_prod__RascalKeywords__lit_alias_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(2848, 0, prod__lit_alias__char_class___range__97_97_char_class___range__108_108_char_class___range__105_105_char_class___range__97_97_char_class___range__115_115_, new char[] {97,108,105,97,115}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RascalKeywords__lit_alias_, tmp);
	}
    protected static final void _init_prod__RascalKeywords__lit_default_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(2850, 0, prod__lit_default__char_class___range__100_100_char_class___range__101_101_char_class___range__102_102_char_class___range__97_97_char_class___range__117_117_char_class___range__108_108_char_class___range__116_116_, new char[] {100,101,102,97,117,108,116}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RascalKeywords__lit_default_, tmp);
	}
    protected static final void _init_prod__RascalKeywords__lit_throws_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(2852, 0, prod__lit_throws__char_class___range__116_116_char_class___range__104_104_char_class___range__114_114_char_class___range__111_111_char_class___range__119_119_char_class___range__115_115_, new char[] {116,104,114,111,119,115}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RascalKeywords__lit_throws_, tmp);
	}
    protected static final void _init_prod__RascalKeywords__lit_module_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(2854, 0, prod__lit_module__char_class___range__109_109_char_class___range__111_111_char_class___range__100_100_char_class___range__117_117_char_class___range__108_108_char_class___range__101_101_, new char[] {109,111,100,117,108,101}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RascalKeywords__lit_module_, tmp);
	}
    protected static final void _init_prod__RascalKeywords__lit_true_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(2858, 0, prod__lit_true__char_class___range__116_116_char_class___range__114_114_char_class___range__117_117_char_class___range__101_101_, new char[] {116,114,117,101}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RascalKeywords__lit_true_, tmp);
	}
    protected static final void _init_prod__RascalKeywords__lit_private_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(2856, 0, prod__lit_private__char_class___range__112_112_char_class___range__114_114_char_class___range__105_105_char_class___range__118_118_char_class___range__97_97_char_class___range__116_116_char_class___range__101_101_, new char[] {112,114,105,118,97,116,101}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RascalKeywords__lit_private_, tmp);
	}
    protected static final void _init_prod__RascalKeywords__lit_map_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(2860, 0, prod__lit_map__char_class___range__109_109_char_class___range__97_97_char_class___range__112_112_, new char[] {109,97,112}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RascalKeywords__lit_map_, tmp);
	}
    protected static final void _init_prod__RascalKeywords__lit_test_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(2862, 0, prod__lit_test__char_class___range__116_116_char_class___range__101_101_char_class___range__115_115_char_class___range__116_116_, new char[] {116,101,115,116}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RascalKeywords__lit_test_, tmp);
	}
    protected static final void _init_prod__RascalKeywords__lit_start_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(2864, 0, prod__lit_start__char_class___range__115_115_char_class___range__116_116_char_class___range__97_97_char_class___range__114_114_char_class___range__116_116_, new char[] {115,116,97,114,116}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RascalKeywords__lit_start_, tmp);
	}
    protected static final void _init_prod__RascalKeywords__lit_import_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(2866, 0, prod__lit_import__char_class___range__105_105_char_class___range__109_109_char_class___range__112_112_char_class___range__111_111_char_class___range__114_114_char_class___range__116_116_, new char[] {105,109,112,111,114,116}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RascalKeywords__lit_import_, tmp);
	}
    protected static final void _init_prod__RascalKeywords__lit_loc_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(2868, 0, prod__lit_loc__char_class___range__108_108_char_class___range__111_111_char_class___range__99_99_, new char[] {108,111,99}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RascalKeywords__lit_loc_, tmp);
	}
    protected static final void _init_prod__RascalKeywords__lit_assert_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(2870, 0, prod__lit_assert__char_class___range__97_97_char_class___range__115_115_char_class___range__115_115_char_class___range__101_101_char_class___range__114_114_char_class___range__116_116_, new char[] {97,115,115,101,114,116}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RascalKeywords__lit_assert_, tmp);
	}
    protected static final void _init_prod__RascalKeywords__lit_insert_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(2872, 0, prod__lit_insert__char_class___range__105_105_char_class___range__110_110_char_class___range__115_115_char_class___range__101_101_char_class___range__114_114_char_class___range__116_116_, new char[] {105,110,115,101,114,116}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RascalKeywords__lit_insert_, tmp);
	}
    protected static final void _init_prod__RascalKeywords__lit_anno_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(2874, 0, prod__lit_anno__char_class___range__97_97_char_class___range__110_110_char_class___range__110_110_char_class___range__111_111_, new char[] {97,110,110,111}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RascalKeywords__lit_anno_, tmp);
	}
    protected static final void _init_prod__RascalKeywords__lit_public_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(2876, 0, prod__lit_public__char_class___range__112_112_char_class___range__117_117_char_class___range__98_98_char_class___range__108_108_char_class___range__105_105_char_class___range__99_99_, new char[] {112,117,98,108,105,99}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RascalKeywords__lit_public_, tmp);
	}
    protected static final void _init_prod__RascalKeywords__lit_void_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(2878, 0, prod__lit_void__char_class___range__118_118_char_class___range__111_111_char_class___range__105_105_char_class___range__100_100_, new char[] {118,111,105,100}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RascalKeywords__lit_void_, tmp);
	}
    protected static final void _init_prod__RascalKeywords__lit_try_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(2880, 0, prod__lit_try__char_class___range__116_116_char_class___range__114_114_char_class___range__121_121_, new char[] {116,114,121}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RascalKeywords__lit_try_, tmp);
	}
    protected static final void _init_prod__RascalKeywords__lit_value_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(2882, 0, prod__lit_value__char_class___range__118_118_char_class___range__97_97_char_class___range__108_108_char_class___range__117_117_char_class___range__101_101_, new char[] {118,97,108,117,101}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RascalKeywords__lit_value_, tmp);
	}
    protected static final void _init_prod__RascalKeywords__lit_non_terminal_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(2884, 0, prod__lit_non_terminal__char_class___range__110_110_char_class___range__111_111_char_class___range__110_110_char_class___range__45_45_char_class___range__116_116_char_class___range__101_101_char_class___range__114_114_char_class___range__109_109_char_class___range__105_105_char_class___range__110_110_char_class___range__97_97_char_class___range__108_108_, new char[] {110,111,110,45,116,101,114,109,105,110,97,108}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RascalKeywords__lit_non_terminal_, tmp);
	}
    protected static final void _init_prod__RascalKeywords__lit_list_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(2886, 0, prod__lit_list__char_class___range__108_108_char_class___range__105_105_char_class___range__115_115_char_class___range__116_116_, new char[] {108,105,115,116}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RascalKeywords__lit_list_, tmp);
	}
    protected static final void _init_prod__RascalKeywords__lit_dynamic_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(2888, 0, prod__lit_dynamic__char_class___range__100_100_char_class___range__121_121_char_class___range__110_110_char_class___range__97_97_char_class___range__109_109_char_class___range__105_105_char_class___range__99_99_, new char[] {100,121,110,97,109,105,99}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RascalKeywords__lit_dynamic_, tmp);
	}
    protected static final void _init_prod__RascalKeywords__lit_tag_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(2890, 0, prod__lit_tag__char_class___range__116_116_char_class___range__97_97_char_class___range__103_103_, new char[] {116,97,103}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RascalKeywords__lit_tag_, tmp);
	}
    protected static final void _init_prod__RascalKeywords__lit_data_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(2892, 0, prod__lit_data__char_class___range__100_100_char_class___range__97_97_char_class___range__116_116_char_class___range__97_97_, new char[] {100,97,116,97}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RascalKeywords__lit_data_, tmp);
	}
    protected static final void _init_prod__RascalKeywords__lit_extend_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(2896, 0, prod__lit_extend__char_class___range__101_101_char_class___range__120_120_char_class___range__116_116_char_class___range__101_101_char_class___range__110_110_char_class___range__100_100_, new char[] {101,120,116,101,110,100}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RascalKeywords__lit_extend_, tmp);
	}
    protected static final void _init_prod__RascalKeywords__lit_append_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(2894, 0, prod__lit_append__char_class___range__97_97_char_class___range__112_112_char_class___range__112_112_char_class___range__101_101_char_class___range__110_110_char_class___range__100_100_, new char[] {97,112,112,101,110,100}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RascalKeywords__lit_append_, tmp);
	}
    protected static final void _init_prod__RascalKeywords__lit_type_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(2900, 0, prod__lit_type__char_class___range__116_116_char_class___range__121_121_char_class___range__112_112_char_class___range__101_101_, new char[] {116,121,112,101}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RascalKeywords__lit_type_, tmp);
	}
    protected static final void _init_prod__RascalKeywords__lit_notin_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(2898, 0, prod__lit_notin__char_class___range__110_110_char_class___range__111_111_char_class___range__116_116_char_class___range__105_105_char_class___range__110_110_, new char[] {110,111,116,105,110}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RascalKeywords__lit_notin_, tmp);
	}
    protected static final void _init_prod__RascalKeywords__lit_catch_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(2902, 0, prod__lit_catch__char_class___range__99_99_char_class___range__97_97_char_class___range__116_116_char_class___range__99_99_char_class___range__104_104_, new char[] {99,97,116,99,104}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RascalKeywords__lit_catch_, tmp);
	}
    protected static final void _init_prod__RascalKeywords__lit_one_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(2904, 0, prod__lit_one__char_class___range__111_111_char_class___range__110_110_char_class___range__101_101_, new char[] {111,110,101}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RascalKeywords__lit_one_, tmp);
	}
    protected static final void _init_prod__RascalKeywords__lit_node_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(2906, 0, prod__lit_node__char_class___range__110_110_char_class___range__111_111_char_class___range__100_100_char_class___range__101_101_, new char[] {110,111,100,101}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RascalKeywords__lit_node_, tmp);
	}
    protected static final void _init_prod__RascalKeywords__lit_str_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(2908, 0, prod__lit_str__char_class___range__115_115_char_class___range__116_116_char_class___range__114_114_, new char[] {115,116,114}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RascalKeywords__lit_str_, tmp);
	}
    protected static final void _init_prod__RascalKeywords__lit_repeat_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(2910, 0, prod__lit_repeat__char_class___range__114_114_char_class___range__101_101_char_class___range__112_112_char_class___range__101_101_char_class___range__97_97_char_class___range__116_116_, new char[] {114,101,112,101,97,116}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RascalKeywords__lit_repeat_, tmp);
	}
    protected static final void _init_prod__RascalKeywords__lit_visit_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(2912, 0, prod__lit_visit__char_class___range__118_118_char_class___range__105_105_char_class___range__115_115_char_class___range__105_105_char_class___range__116_116_, new char[] {118,105,115,105,116}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RascalKeywords__lit_visit_, tmp);
	}
    protected static final void _init_prod__RascalKeywords__lit_fun_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(2914, 0, prod__lit_fun__char_class___range__102_102_char_class___range__117_117_char_class___range__110_110_, new char[] {102,117,110}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RascalKeywords__lit_fun_, tmp);
	}
    protected static final void _init_prod__RascalKeywords__lit_if_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(2918, 0, prod__lit_if__char_class___range__105_105_char_class___range__102_102_, new char[] {105,102}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RascalKeywords__lit_if_, tmp);
	}
    protected static final void _init_prod__RascalKeywords__lit_non_assoc_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(2916, 0, prod__lit_non_assoc__char_class___range__110_110_char_class___range__111_111_char_class___range__110_110_char_class___range__45_45_char_class___range__97_97_char_class___range__115_115_char_class___range__115_115_char_class___range__111_111_char_class___range__99_99_, new char[] {110,111,110,45,97,115,115,111,99}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RascalKeywords__lit_non_assoc_, tmp);
	}
    protected static final void _init_prod__RascalKeywords__lit_return_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(2920, 0, prod__lit_return__char_class___range__114_114_char_class___range__101_101_char_class___range__116_116_char_class___range__117_117_char_class___range__114_114_char_class___range__110_110_, new char[] {114,101,116,117,114,110}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RascalKeywords__lit_return_, tmp);
	}
    protected static final void _init_prod__RascalKeywords__lit_else_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(2922, 0, prod__lit_else__char_class___range__101_101_char_class___range__108_108_char_class___range__115_115_char_class___range__101_101_, new char[] {101,108,115,101}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RascalKeywords__lit_else_, tmp);
	}
    protected static final void _init_prod__RascalKeywords__lit_in_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(2924, 0, prod__lit_in__char_class___range__105_105_char_class___range__110_110_, new char[] {105,110}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RascalKeywords__lit_in_, tmp);
	}
    protected static final void _init_prod__RascalKeywords__lit_join_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(2928, 0, prod__lit_join__char_class___range__106_106_char_class___range__111_111_char_class___range__105_105_char_class___range__110_110_, new char[] {106,111,105,110}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RascalKeywords__lit_join_, tmp);
	}
    protected static final void _init_prod__RascalKeywords__lit_it_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(2926, 0, prod__lit_it__char_class___range__105_105_char_class___range__116_116_, new char[] {105,116}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RascalKeywords__lit_it_, tmp);
	}
    protected static final void _init_prod__RascalKeywords__lit_for_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(2930, 0, prod__lit_for__char_class___range__102_102_char_class___range__111_111_char_class___range__114_114_, new char[] {102,111,114}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RascalKeywords__lit_for_, tmp);
	}
    protected static final void _init_prod__RascalKeywords__lit_bracket_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(2932, 0, prod__lit_bracket__char_class___range__98_98_char_class___range__114_114_char_class___range__97_97_char_class___range__99_99_char_class___range__107_107_char_class___range__101_101_char_class___range__116_116_, new char[] {98,114,97,99,107,101,116}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RascalKeywords__lit_bracket_, tmp);
	}
    protected static final void _init_prod__RascalKeywords__lit_set_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(2934, 0, prod__lit_set__char_class___range__115_115_char_class___range__101_101_char_class___range__116_116_, new char[] {115,101,116}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RascalKeywords__lit_set_, tmp);
	}
    protected static final void _init_prod__RascalKeywords__lit_assoc_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(2936, 0, prod__lit_assoc__char_class___range__97_97_char_class___range__115_115_char_class___range__115_115_char_class___range__111_111_char_class___range__99_99_, new char[] {97,115,115,111,99}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RascalKeywords__lit_assoc_, tmp);
	}
    protected static final void _init_prod__RascalKeywords__lit_bag_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(2938, 0, prod__lit_bag__char_class___range__98_98_char_class___range__97_97_char_class___range__103_103_, new char[] {98,97,103}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RascalKeywords__lit_bag_, tmp);
	}
    protected static final void _init_prod__RascalKeywords__lit_num_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(2940, 0, prod__lit_num__char_class___range__110_110_char_class___range__117_117_char_class___range__109_109_, new char[] {110,117,109}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RascalKeywords__lit_num_, tmp);
	}
    protected static final void _init_prod__RascalKeywords__lit_datetime_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(2942, 0, prod__lit_datetime__char_class___range__100_100_char_class___range__97_97_char_class___range__116_116_char_class___range__101_101_char_class___range__116_116_char_class___range__105_105_char_class___range__109_109_char_class___range__101_101_, new char[] {100,97,116,101,116,105,109,101}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RascalKeywords__lit_datetime_, tmp);
	}
    protected static final void _init_prod__RascalKeywords__lit_layout_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(2948, 0, prod__lit_layout__char_class___range__108_108_char_class___range__97_97_char_class___range__121_121_char_class___range__111_111_char_class___range__117_117_char_class___range__116_116_, new char[] {108,97,121,111,117,116}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RascalKeywords__lit_layout_, tmp);
	}
    protected static final void _init_prod__RascalKeywords__lit_case_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(2946, 0, prod__lit_case__char_class___range__99_99_char_class___range__97_97_char_class___range__115_115_char_class___range__101_101_, new char[] {99,97,115,101}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RascalKeywords__lit_case_, tmp);
	}
    protected static final void _init_prod__RascalKeywords__lit_while_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(2944, 0, prod__lit_while__char_class___range__119_119_char_class___range__104_104_char_class___range__105_105_char_class___range__108_108_char_class___range__101_101_, new char[] {119,104,105,108,101}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RascalKeywords__lit_while_, tmp);
	}
    protected static final void _init_prod__RascalKeywords__lit_bool_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(2950, 0, prod__lit_bool__char_class___range__98_98_char_class___range__111_111_char_class___range__111_111_char_class___range__108_108_, new char[] {98,111,111,108}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RascalKeywords__lit_bool_, tmp);
	}
    protected static final void _init_prod__RascalKeywords__lit_any_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(2952, 0, prod__lit_any__char_class___range__97_97_char_class___range__110_110_char_class___range__121_121_, new char[] {97,110,121}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RascalKeywords__lit_any_, tmp);
	}
    protected static final void _init_prod__RascalKeywords__lit_real_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(2956, 0, prod__lit_real__char_class___range__114_114_char_class___range__101_101_char_class___range__97_97_char_class___range__108_108_, new char[] {114,101,97,108}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RascalKeywords__lit_real_, tmp);
	}
    protected static final void _init_prod__RascalKeywords__lit_finally_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(2954, 0, prod__lit_finally__char_class___range__102_102_char_class___range__105_105_char_class___range__110_110_char_class___range__97_97_char_class___range__108_108_char_class___range__108_108_char_class___range__121_121_, new char[] {102,105,110,97,108,108,121}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RascalKeywords__lit_finally_, tmp);
	}
    protected static final void _init_prod__RascalKeywords__lit_all_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(2958, 0, prod__lit_all__char_class___range__97_97_char_class___range__108_108_char_class___range__108_108_, new char[] {97,108,108}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RascalKeywords__lit_all_, tmp);
	}
    protected static final void _init_prod__RascalKeywords__lit_false_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(2960, 0, prod__lit_false__char_class___range__102_102_char_class___range__97_97_char_class___range__108_108_char_class___range__115_115_char_class___range__101_101_, new char[] {102,97,108,115,101}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RascalKeywords__lit_false_, tmp);
	}
    protected static final void _init_prod__RascalKeywords__lit_rel_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(2962, 0, prod__lit_rel__char_class___range__114_114_char_class___range__101_101_char_class___range__108_108_, new char[] {114,101,108}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RascalKeywords__lit_rel_, tmp);
	}
    protected static final void _init_prod__RascalKeywords__BasicType_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode(2964, 0, "BasicType", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RascalKeywords__BasicType_, tmp);
	}
    protected static final void _init_prod__RascalKeywords__lit_solve_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(2966, 0, prod__lit_solve__char_class___range__115_115_char_class___range__111_111_char_class___range__108_108_char_class___range__118_118_char_class___range__101_101_, new char[] {115,111,108,118,101}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RascalKeywords__lit_solve_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__RascalKeywords__lit_tuple_(builder);
      
        _init_prod__RascalKeywords__lit_constructor_(builder);
      
        _init_prod__RascalKeywords__lit_int_(builder);
      
        _init_prod__RascalKeywords__lit_fail_(builder);
      
        _init_prod__RascalKeywords__lit_switch_(builder);
      
        _init_prod__RascalKeywords__lit_rule_(builder);
      
        _init_prod__RascalKeywords__lit_throw_(builder);
      
        _init_prod__RascalKeywords__lit_alias_(builder);
      
        _init_prod__RascalKeywords__lit_default_(builder);
      
        _init_prod__RascalKeywords__lit_throws_(builder);
      
        _init_prod__RascalKeywords__lit_module_(builder);
      
        _init_prod__RascalKeywords__lit_true_(builder);
      
        _init_prod__RascalKeywords__lit_private_(builder);
      
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
      
        _init_prod__RascalKeywords__lit_non_terminal_(builder);
      
        _init_prod__RascalKeywords__lit_list_(builder);
      
        _init_prod__RascalKeywords__lit_dynamic_(builder);
      
        _init_prod__RascalKeywords__lit_tag_(builder);
      
        _init_prod__RascalKeywords__lit_data_(builder);
      
        _init_prod__RascalKeywords__lit_extend_(builder);
      
        _init_prod__RascalKeywords__lit_append_(builder);
      
        _init_prod__RascalKeywords__lit_type_(builder);
      
        _init_prod__RascalKeywords__lit_notin_(builder);
      
        _init_prod__RascalKeywords__lit_catch_(builder);
      
        _init_prod__RascalKeywords__lit_one_(builder);
      
        _init_prod__RascalKeywords__lit_node_(builder);
      
        _init_prod__RascalKeywords__lit_str_(builder);
      
        _init_prod__RascalKeywords__lit_repeat_(builder);
      
        _init_prod__RascalKeywords__lit_visit_(builder);
      
        _init_prod__RascalKeywords__lit_fun_(builder);
      
        _init_prod__RascalKeywords__lit_if_(builder);
      
        _init_prod__RascalKeywords__lit_non_assoc_(builder);
      
        _init_prod__RascalKeywords__lit_return_(builder);
      
        _init_prod__RascalKeywords__lit_else_(builder);
      
        _init_prod__RascalKeywords__lit_in_(builder);
      
        _init_prod__RascalKeywords__lit_join_(builder);
      
        _init_prod__RascalKeywords__lit_it_(builder);
      
        _init_prod__RascalKeywords__lit_for_(builder);
      
        _init_prod__RascalKeywords__lit_bracket_(builder);
      
        _init_prod__RascalKeywords__lit_set_(builder);
      
        _init_prod__RascalKeywords__lit_assoc_(builder);
      
        _init_prod__RascalKeywords__lit_bag_(builder);
      
        _init_prod__RascalKeywords__lit_num_(builder);
      
        _init_prod__RascalKeywords__lit_datetime_(builder);
      
        _init_prod__RascalKeywords__lit_layout_(builder);
      
        _init_prod__RascalKeywords__lit_case_(builder);
      
        _init_prod__RascalKeywords__lit_while_(builder);
      
        _init_prod__RascalKeywords__lit_bool_(builder);
      
        _init_prod__RascalKeywords__lit_any_(builder);
      
        _init_prod__RascalKeywords__lit_real_(builder);
      
        _init_prod__RascalKeywords__lit_finally_(builder);
      
        _init_prod__RascalKeywords__lit_all_(builder);
      
        _init_prod__RascalKeywords__lit_false_(builder);
      
        _init_prod__RascalKeywords__lit_rel_(builder);
      
        _init_prod__RascalKeywords__BasicType_(builder);
      
        _init_prod__RascalKeywords__lit_solve_(builder);
      
    }
  }
	
  protected static class Strategy {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__TopDown_Strategy__lit_top_down_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(2988, 0, prod__lit_top_down__char_class___range__116_116_char_class___range__111_111_char_class___range__112_112_char_class___range__45_45_char_class___range__100_100_char_class___range__111_111_char_class___range__119_119_char_class___range__110_110_, new char[] {116,111,112,45,100,111,119,110}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__TopDown_Strategy__lit_top_down_, tmp);
	}
    protected static final void _init_prod__TopDownBreak_Strategy__lit_top_down_break_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(2990, 0, prod__lit_top_down_break__char_class___range__116_116_char_class___range__111_111_char_class___range__112_112_char_class___range__45_45_char_class___range__100_100_char_class___range__111_111_char_class___range__119_119_char_class___range__110_110_char_class___range__45_45_char_class___range__98_98_char_class___range__114_114_char_class___range__101_101_char_class___range__97_97_char_class___range__107_107_, new char[] {116,111,112,45,100,111,119,110,45,98,114,101,97,107}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__TopDownBreak_Strategy__lit_top_down_break_, tmp);
	}
    protected static final void _init_prod__Innermost_Strategy__lit_innermost_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(2992, 0, prod__lit_innermost__char_class___range__105_105_char_class___range__110_110_char_class___range__110_110_char_class___range__101_101_char_class___range__114_114_char_class___range__109_109_char_class___range__111_111_char_class___range__115_115_char_class___range__116_116_, new char[] {105,110,110,101,114,109,111,115,116}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Innermost_Strategy__lit_innermost_, tmp);
	}
    protected static final void _init_prod__BottomUp_Strategy__lit_bottom_up_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(2994, 0, prod__lit_bottom_up__char_class___range__98_98_char_class___range__111_111_char_class___range__116_116_char_class___range__116_116_char_class___range__111_111_char_class___range__109_109_char_class___range__45_45_char_class___range__117_117_char_class___range__112_112_, new char[] {98,111,116,116,111,109,45,117,112}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__BottomUp_Strategy__lit_bottom_up_, tmp);
	}
    protected static final void _init_prod__Outermost_Strategy__lit_outermost_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(2996, 0, prod__lit_outermost__char_class___range__111_111_char_class___range__117_117_char_class___range__116_116_char_class___range__101_101_char_class___range__114_114_char_class___range__109_109_char_class___range__111_111_char_class___range__115_115_char_class___range__116_116_, new char[] {111,117,116,101,114,109,111,115,116}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Outermost_Strategy__lit_outermost_, tmp);
	}
    protected static final void _init_prod__BottomUpBreak_Strategy__lit_bottom_up_break_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(2998, 0, prod__lit_bottom_up_break__char_class___range__98_98_char_class___range__111_111_char_class___range__116_116_char_class___range__116_116_char_class___range__111_111_char_class___range__109_109_char_class___range__45_45_char_class___range__117_117_char_class___range__112_112_char_class___range__45_45_char_class___range__98_98_char_class___range__114_114_char_class___range__101_101_char_class___range__97_97_char_class___range__107_107_, new char[] {98,111,116,116,111,109,45,117,112,45,98,114,101,97,107}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__BottomUpBreak_Strategy__lit_bottom_up_break_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__TopDown_Strategy__lit_top_down_(builder);
      
        _init_prod__TopDownBreak_Strategy__lit_top_down_break_(builder);
      
        _init_prod__Innermost_Strategy__lit_innermost_(builder);
      
        _init_prod__BottomUp_Strategy__lit_bottom_up_(builder);
      
        _init_prod__Outermost_Strategy__lit_outermost_(builder);
      
        _init_prod__BottomUpBreak_Strategy__lit_bottom_up_break_(builder);
      
    }
  }
	
  protected static class Char {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__Char__lit___92_char_class___range__32_32_range__34_34_range__39_39_range__45_45_range__60_60_range__62_62_range__91_93_range__98_98_range__102_102_range__110_110_range__114_114_range__116_116__tag__category___67_111_110_115_116_97_110_116(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[2];
      
      tmp[1] = new CharStackNode(3016, 1, new char[][]{{32,32},{34,34},{39,39},{45,45},{60,60},{62,62},{91,93},{98,98},{102,102},{110,110},{114,114},{116,116}}, null, null);
      tmp[0] = new LiteralStackNode(3014, 0, prod__lit___92__char_class___range__92_92_, new char[] {92}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Char__lit___92_char_class___range__32_32_range__34_34_range__39_39_range__45_45_range__60_60_range__62_62_range__91_93_range__98_98_range__102_102_range__110_110_range__114_114_range__116_116__tag__category___67_111_110_115_116_97_110_116, tmp);
	}
    protected static final void _init_prod__Char__char_class___range__0_31_range__33_33_range__35_38_range__40_44_range__46_59_range__61_61_range__63_90_range__94_65535__tag__category___67_111_110_115_116_97_110_116(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new CharStackNode(3018, 0, new char[][]{{0,31},{33,33},{35,38},{40,44},{46,59},{61,61},{63,90},{94,65535}}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Char__char_class___range__0_31_range__33_33_range__35_38_range__40_44_range__46_59_range__61_61_range__63_90_range__94_65535__tag__category___67_111_110_115_116_97_110_116, tmp);
	}
    protected static final void _init_prod__Char__OctalEscapeSequence__tag__category___67_111_110_115_116_97_110_116(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode(3020, 0, "OctalEscapeSequence", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Char__OctalEscapeSequence__tag__category___67_111_110_115_116_97_110_116, tmp);
	}
    protected static final void _init_prod__Char__UnicodeEscape__tag__category___67_111_110_115_116_97_110_116(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode(3022, 0, "UnicodeEscape", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Char__UnicodeEscape__tag__category___67_111_110_115_116_97_110_116, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__Char__lit___92_char_class___range__32_32_range__34_34_range__39_39_range__45_45_range__60_60_range__62_62_range__91_93_range__98_98_range__102_102_range__110_110_range__114_114_range__116_116__tag__category___67_111_110_115_116_97_110_116(builder);
      
        _init_prod__Char__char_class___range__0_31_range__33_33_range__35_38_range__40_44_range__46_59_range__61_61_range__63_90_range__94_65535__tag__category___67_111_110_115_116_97_110_116(builder);
      
        _init_prod__Char__OctalEscapeSequence__tag__category___67_111_110_115_116_97_110_116(builder);
      
        _init_prod__Char__UnicodeEscape__tag__category___67_111_110_115_116_97_110_116(builder);
      
    }
  }
	
  protected static class PrePathChars {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__PrePathChars__URLChars_lit___60_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[2];
      
      tmp[1] = new LiteralStackNode(3036, 1, prod__lit___60__char_class___range__60_60_, new char[] {60}, null, null);
      tmp[0] = new NonTerminalStackNode(3034, 0, "URLChars", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__PrePathChars__URLChars_lit___60_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__PrePathChars__URLChars_lit___60_(builder);
      
    }
  }
	
  protected static class MidPathChars {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__MidPathChars__lit___62_URLChars_lit___60_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[2] = new LiteralStackNode(3042, 2, prod__lit___60__char_class___range__60_60_, new char[] {60}, null, null);
      tmp[1] = new NonTerminalStackNode(3040, 1, "URLChars", null, null);
      tmp[0] = new LiteralStackNode(3038, 0, prod__lit___62__char_class___range__62_62_, new char[] {62}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__MidPathChars__lit___62_URLChars_lit___60_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__MidPathChars__lit___62_URLChars_lit___60_(builder);
      
    }
  }
	
  protected static class PostProtocolChars {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__PostProtocolChars__lit___62_URLChars_lit___58_47_47_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[2] = new LiteralStackNode(3094, 2, prod__lit___58_47_47__char_class___range__58_58_char_class___range__47_47_char_class___range__47_47_, new char[] {58,47,47}, null, null);
      tmp[1] = new NonTerminalStackNode(3092, 1, "URLChars", null, null);
      tmp[0] = new LiteralStackNode(3090, 0, prod__lit___62__char_class___range__62_62_, new char[] {62}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__PostProtocolChars__lit___62_URLChars_lit___58_47_47_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__PostProtocolChars__lit___62_URLChars_lit___58_47_47_(builder);
      
    }
  }
	
  protected static class SyntaxDefinition {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__Keyword_SyntaxDefinition__lit_keyword_layouts_LAYOUTLIST_defined_Sym_layouts_LAYOUTLIST_lit___61_layouts_LAYOUTLIST_production_Prod_layouts_LAYOUTLIST_lit___59__tag__Foldable(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[9];
      
      tmp[8] = new LiteralStackNode(3180, 8, prod__lit___59__char_class___range__59_59_, new char[] {59}, null, null);
      tmp[7] = new NonTerminalStackNode(3178, 7, "layouts_LAYOUTLIST", null, null);
      tmp[6] = new NonTerminalStackNode(3176, 6, "Prod", null, null);
      tmp[5] = new NonTerminalStackNode(3174, 5, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode(3172, 4, prod__lit___61__char_class___range__61_61_, new char[] {61}, null, null);
      tmp[3] = new NonTerminalStackNode(3170, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(3168, 2, "Sym", null, null);
      tmp[1] = new NonTerminalStackNode(3166, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(3164, 0, prod__lit_keyword__char_class___range__107_107_char_class___range__101_101_char_class___range__121_121_char_class___range__119_119_char_class___range__111_111_char_class___range__114_114_char_class___range__100_100_, new char[] {107,101,121,119,111,114,100}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Keyword_SyntaxDefinition__lit_keyword_layouts_LAYOUTLIST_defined_Sym_layouts_LAYOUTLIST_lit___61_layouts_LAYOUTLIST_production_Prod_layouts_LAYOUTLIST_lit___59__tag__Foldable, tmp);
	}
    protected static final void _init_prod__Language_SyntaxDefinition__start_Start_layouts_LAYOUTLIST_lit_syntax_layouts_LAYOUTLIST_defined_Sym_layouts_LAYOUTLIST_lit___61_layouts_LAYOUTLIST_production_Prod_layouts_LAYOUTLIST_lit___59__tag__Foldable(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[11];
      
      tmp[10] = new LiteralStackNode(3202, 10, prod__lit___59__char_class___range__59_59_, new char[] {59}, null, null);
      tmp[9] = new NonTerminalStackNode(3200, 9, "layouts_LAYOUTLIST", null, null);
      tmp[8] = new NonTerminalStackNode(3198, 8, "Prod", null, null);
      tmp[7] = new NonTerminalStackNode(3196, 7, "layouts_LAYOUTLIST", null, null);
      tmp[6] = new LiteralStackNode(3194, 6, prod__lit___61__char_class___range__61_61_, new char[] {61}, null, null);
      tmp[5] = new NonTerminalStackNode(3192, 5, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new NonTerminalStackNode(3190, 4, "Sym", null, null);
      tmp[3] = new NonTerminalStackNode(3188, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(3186, 2, prod__lit_syntax__char_class___range__115_115_char_class___range__121_121_char_class___range__110_110_char_class___range__116_116_char_class___range__97_97_char_class___range__120_120_, new char[] {115,121,110,116,97,120}, null, null);
      tmp[1] = new NonTerminalStackNode(3184, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(3182, 0, "Start", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Language_SyntaxDefinition__start_Start_layouts_LAYOUTLIST_lit_syntax_layouts_LAYOUTLIST_defined_Sym_layouts_LAYOUTLIST_lit___61_layouts_LAYOUTLIST_production_Prod_layouts_LAYOUTLIST_lit___59__tag__Foldable, tmp);
	}
    protected static final void _init_prod__Lexical_SyntaxDefinition__lit_lexical_layouts_LAYOUTLIST_defined_Sym_layouts_LAYOUTLIST_lit___61_layouts_LAYOUTLIST_production_Prod_layouts_LAYOUTLIST_lit___59__tag__Foldable(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[9];
      
      tmp[8] = new LiteralStackNode(3220, 8, prod__lit___59__char_class___range__59_59_, new char[] {59}, null, null);
      tmp[7] = new NonTerminalStackNode(3218, 7, "layouts_LAYOUTLIST", null, null);
      tmp[6] = new NonTerminalStackNode(3216, 6, "Prod", null, null);
      tmp[5] = new NonTerminalStackNode(3214, 5, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode(3212, 4, prod__lit___61__char_class___range__61_61_, new char[] {61}, null, null);
      tmp[3] = new NonTerminalStackNode(3210, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(3208, 2, "Sym", null, null);
      tmp[1] = new NonTerminalStackNode(3206, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(3204, 0, prod__lit_lexical__char_class___range__108_108_char_class___range__101_101_char_class___range__120_120_char_class___range__105_105_char_class___range__99_99_char_class___range__97_97_char_class___range__108_108_, new char[] {108,101,120,105,99,97,108}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Lexical_SyntaxDefinition__lit_lexical_layouts_LAYOUTLIST_defined_Sym_layouts_LAYOUTLIST_lit___61_layouts_LAYOUTLIST_production_Prod_layouts_LAYOUTLIST_lit___59__tag__Foldable, tmp);
	}
    protected static final void _init_prod__Layout_SyntaxDefinition__vis_Visibility_layouts_LAYOUTLIST_lit_layout_layouts_LAYOUTLIST_defined_Sym_layouts_LAYOUTLIST_lit___61_layouts_LAYOUTLIST_production_Prod_layouts_LAYOUTLIST_lit___59__tag__Foldable(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[11];
      
      tmp[10] = new LiteralStackNode(3242, 10, prod__lit___59__char_class___range__59_59_, new char[] {59}, null, null);
      tmp[9] = new NonTerminalStackNode(3240, 9, "layouts_LAYOUTLIST", null, null);
      tmp[8] = new NonTerminalStackNode(3238, 8, "Prod", null, null);
      tmp[7] = new NonTerminalStackNode(3236, 7, "layouts_LAYOUTLIST", null, null);
      tmp[6] = new LiteralStackNode(3234, 6, prod__lit___61__char_class___range__61_61_, new char[] {61}, null, null);
      tmp[5] = new NonTerminalStackNode(3232, 5, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new NonTerminalStackNode(3230, 4, "Sym", null, null);
      tmp[3] = new NonTerminalStackNode(3228, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(3226, 2, prod__lit_layout__char_class___range__108_108_char_class___range__97_97_char_class___range__121_121_char_class___range__111_111_char_class___range__117_117_char_class___range__116_116_, new char[] {108,97,121,111,117,116}, null, null);
      tmp[1] = new NonTerminalStackNode(3224, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(3222, 0, "Visibility", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Layout_SyntaxDefinition__vis_Visibility_layouts_LAYOUTLIST_lit_layout_layouts_LAYOUTLIST_defined_Sym_layouts_LAYOUTLIST_lit___61_layouts_LAYOUTLIST_production_Prod_layouts_LAYOUTLIST_lit___59__tag__Foldable, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__Keyword_SyntaxDefinition__lit_keyword_layouts_LAYOUTLIST_defined_Sym_layouts_LAYOUTLIST_lit___61_layouts_LAYOUTLIST_production_Prod_layouts_LAYOUTLIST_lit___59__tag__Foldable(builder);
      
        _init_prod__Language_SyntaxDefinition__start_Start_layouts_LAYOUTLIST_lit_syntax_layouts_LAYOUTLIST_defined_Sym_layouts_LAYOUTLIST_lit___61_layouts_LAYOUTLIST_production_Prod_layouts_LAYOUTLIST_lit___59__tag__Foldable(builder);
      
        _init_prod__Lexical_SyntaxDefinition__lit_lexical_layouts_LAYOUTLIST_defined_Sym_layouts_LAYOUTLIST_lit___61_layouts_LAYOUTLIST_production_Prod_layouts_LAYOUTLIST_lit___59__tag__Foldable(builder);
      
        _init_prod__Layout_SyntaxDefinition__vis_Visibility_layouts_LAYOUTLIST_lit_layout_layouts_LAYOUTLIST_defined_Sym_layouts_LAYOUTLIST_lit___61_layouts_LAYOUTLIST_production_Prod_layouts_LAYOUTLIST_lit___59__tag__Foldable(builder);
      
    }
  }
	
  protected static class Kind {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__Alias_Kind__lit_alias_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(3244, 0, prod__lit_alias__char_class___range__97_97_char_class___range__108_108_char_class___range__105_105_char_class___range__97_97_char_class___range__115_115_, new char[] {97,108,105,97,115}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Alias_Kind__lit_alias_, tmp);
	}
    protected static final void _init_prod__View_Kind__lit_view_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(3246, 0, prod__lit_view__char_class___range__118_118_char_class___range__105_105_char_class___range__101_101_char_class___range__119_119_, new char[] {118,105,101,119}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__View_Kind__lit_view_, tmp);
	}
    protected static final void _init_prod__Function_Kind__lit_function_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(3248, 0, prod__lit_function__char_class___range__102_102_char_class___range__117_117_char_class___range__110_110_char_class___range__99_99_char_class___range__116_116_char_class___range__105_105_char_class___range__111_111_char_class___range__110_110_, new char[] {102,117,110,99,116,105,111,110}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Function_Kind__lit_function_, tmp);
	}
    protected static final void _init_prod__Tag_Kind__lit_tag_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(3250, 0, prod__lit_tag__char_class___range__116_116_char_class___range__97_97_char_class___range__103_103_, new char[] {116,97,103}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Tag_Kind__lit_tag_, tmp);
	}
    protected static final void _init_prod__Data_Kind__lit_data_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(3252, 0, prod__lit_data__char_class___range__100_100_char_class___range__97_97_char_class___range__116_116_char_class___range__97_97_, new char[] {100,97,116,97}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Data_Kind__lit_data_, tmp);
	}
    protected static final void _init_prod__Anno_Kind__lit_anno_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(3254, 0, prod__lit_anno__char_class___range__97_97_char_class___range__110_110_char_class___range__110_110_char_class___range__111_111_, new char[] {97,110,110,111}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Anno_Kind__lit_anno_, tmp);
	}
    protected static final void _init_prod__Rule_Kind__lit_rule_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(3256, 0, prod__lit_rule__char_class___range__114_114_char_class___range__117_117_char_class___range__108_108_char_class___range__101_101_, new char[] {114,117,108,101}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Rule_Kind__lit_rule_, tmp);
	}
    protected static final void _init_prod__Variable_Kind__lit_variable_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(3258, 0, prod__lit_variable__char_class___range__118_118_char_class___range__97_97_char_class___range__114_114_char_class___range__105_105_char_class___range__97_97_char_class___range__98_98_char_class___range__108_108_char_class___range__101_101_, new char[] {118,97,114,105,97,98,108,101}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Variable_Kind__lit_variable_, tmp);
	}
    protected static final void _init_prod__Module_Kind__lit_module_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(3260, 0, prod__lit_module__char_class___range__109_109_char_class___range__111_111_char_class___range__100_100_char_class___range__117_117_char_class___range__108_108_char_class___range__101_101_, new char[] {109,111,100,117,108,101}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Module_Kind__lit_module_, tmp);
	}
    protected static final void _init_prod__All_Kind__lit_all_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(3262, 0, prod__lit_all__char_class___range__97_97_char_class___range__108_108_char_class___range__108_108_, new char[] {97,108,108}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__All_Kind__lit_all_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__Alias_Kind__lit_alias_(builder);
      
        _init_prod__View_Kind__lit_view_(builder);
      
        _init_prod__Function_Kind__lit_function_(builder);
      
        _init_prod__Tag_Kind__lit_tag_(builder);
      
        _init_prod__Data_Kind__lit_data_(builder);
      
        _init_prod__Anno_Kind__lit_anno_(builder);
      
        _init_prod__Rule_Kind__lit_rule_(builder);
      
        _init_prod__Variable_Kind__lit_variable_(builder);
      
        _init_prod__Module_Kind__lit_module_(builder);
      
        _init_prod__All_Kind__lit_all_(builder);
      
    }
  }
	
  protected static class IntegerLiteral {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__OctalIntegerLiteral_IntegerLiteral__octal_OctalIntegerLiteral_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode(3288, 0, "OctalIntegerLiteral", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__OctalIntegerLiteral_IntegerLiteral__octal_OctalIntegerLiteral_, tmp);
	}
    protected static final void _init_prod__HexIntegerLiteral_IntegerLiteral__hex_HexIntegerLiteral_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode(3290, 0, "HexIntegerLiteral", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__HexIntegerLiteral_IntegerLiteral__hex_HexIntegerLiteral_, tmp);
	}
    protected static final void _init_prod__DecimalIntegerLiteral_IntegerLiteral__decimal_DecimalIntegerLiteral_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode(3292, 0, "DecimalIntegerLiteral", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__DecimalIntegerLiteral_IntegerLiteral__decimal_DecimalIntegerLiteral_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__OctalIntegerLiteral_IntegerLiteral__octal_OctalIntegerLiteral_(builder);
      
        _init_prod__HexIntegerLiteral_IntegerLiteral__hex_HexIntegerLiteral_(builder);
      
        _init_prod__DecimalIntegerLiteral_IntegerLiteral__decimal_DecimalIntegerLiteral_(builder);
      
    }
  }
	
  protected static class Target {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__Empty_Target__(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new EpsilonStackNode(3284, 0);
      builder.addAlternative(ObjectRascalRascal.prod__Empty_Target__, tmp);
	}
    protected static final void _init_prod__Labeled_Target__name_Name_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode(3286, 0, "Name", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Labeled_Target__name_Name_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__Empty_Target__(builder);
      
        _init_prod__Labeled_Target__name_Name_(builder);
      
    }
  }
	
  protected static class start__Command {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__start__Command__layouts_LAYOUTLIST_top_Command_layouts_LAYOUTLIST_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode(3300, 2, "layouts_LAYOUTLIST", null, null);
      tmp[1] = new NonTerminalStackNode(3298, 1, "Command", null, null);
      tmp[0] = new NonTerminalStackNode(3296, 0, "layouts_LAYOUTLIST", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__start__Command__layouts_LAYOUTLIST_top_Command_layouts_LAYOUTLIST_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__start__Command__layouts_LAYOUTLIST_top_Command_layouts_LAYOUTLIST_(builder);
      
    }
  }
	
  protected static class FunctionBody {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__Default_FunctionBody__lit___123_layouts_LAYOUTLIST_statements_iter_star_seps__Statement__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new LiteralStackNode(3314, 4, prod__lit___125__char_class___range__125_125_, new char[] {125}, null, null);
      tmp[3] = new NonTerminalStackNode(3312, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new SeparatedListStackNode(3306, 2, regular__iter_star_seps__Statement__layouts_LAYOUTLIST, new NonTerminalStackNode(3308, 0, "Statement", null, null), new AbstractStackNode[]{new NonTerminalStackNode(3310, 1, "layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[1] = new NonTerminalStackNode(3304, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(3302, 0, prod__lit___123__char_class___range__123_123_, new char[] {123}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Default_FunctionBody__lit___123_layouts_LAYOUTLIST_statements_iter_star_seps__Statement__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__Default_FunctionBody__lit___123_layouts_LAYOUTLIST_statements_iter_star_seps__Statement__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_(builder);
      
    }
  }
	
  protected static class Import {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__Extend_Import__lit_extend_layouts_LAYOUTLIST_module_ImportedModule_layouts_LAYOUTLIST_lit___59_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new LiteralStackNode(3348, 4, prod__lit___59__char_class___range__59_59_, new char[] {59}, null, null);
      tmp[3] = new NonTerminalStackNode(3346, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(3344, 2, "ImportedModule", null, null);
      tmp[1] = new NonTerminalStackNode(3342, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(3340, 0, prod__lit_extend__char_class___range__101_101_char_class___range__120_120_char_class___range__116_116_char_class___range__101_101_char_class___range__110_110_char_class___range__100_100_, new char[] {101,120,116,101,110,100}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Extend_Import__lit_extend_layouts_LAYOUTLIST_module_ImportedModule_layouts_LAYOUTLIST_lit___59_, tmp);
	}
    protected static final void _init_prod__Default_Import__lit_import_layouts_LAYOUTLIST_module_ImportedModule_layouts_LAYOUTLIST_lit___59_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new LiteralStackNode(3358, 4, prod__lit___59__char_class___range__59_59_, new char[] {59}, null, null);
      tmp[3] = new NonTerminalStackNode(3356, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(3354, 2, "ImportedModule", null, null);
      tmp[1] = new NonTerminalStackNode(3352, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(3350, 0, prod__lit_import__char_class___range__105_105_char_class___range__109_109_char_class___range__112_112_char_class___range__111_111_char_class___range__114_114_char_class___range__116_116_, new char[] {105,109,112,111,114,116}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Default_Import__lit_import_layouts_LAYOUTLIST_module_ImportedModule_layouts_LAYOUTLIST_lit___59_, tmp);
	}
    protected static final void _init_prod__Syntax_Import__syntax_SyntaxDefinition_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode(3360, 0, "SyntaxDefinition", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Syntax_Import__syntax_SyntaxDefinition_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__Extend_Import__lit_extend_layouts_LAYOUTLIST_module_ImportedModule_layouts_LAYOUTLIST_lit___59_(builder);
      
        _init_prod__Default_Import__lit_import_layouts_LAYOUTLIST_module_ImportedModule_layouts_LAYOUTLIST_lit___59_(builder);
      
        _init_prod__Syntax_Import__syntax_SyntaxDefinition_(builder);
      
    }
  }
	
  protected static class UserType {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__Name_UserType__name_QualifiedName_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode(3316, 0, "QualifiedName", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Name_UserType__name_QualifiedName_, tmp);
	}
    protected static final void _init_prod__Parametric_UserType__conditional__name_QualifiedName__follow__lit___91_layouts_LAYOUTLIST_lit___91_layouts_LAYOUTLIST_parameters_iter_seps__Type__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___93_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[7];
      
      tmp[6] = new LiteralStackNode(3338, 6, prod__lit___93__char_class___range__93_93_, new char[] {93}, null, null);
      tmp[5] = new NonTerminalStackNode(3336, 5, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new SeparatedListStackNode(3326, 4, regular__iter_seps__Type__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST, new NonTerminalStackNode(3328, 0, "Type", null, null), new AbstractStackNode[]{new NonTerminalStackNode(3330, 1, "layouts_LAYOUTLIST", null, null), new LiteralStackNode(3332, 2, prod__lit___44__char_class___range__44_44_, new char[] {44}, null, null), new NonTerminalStackNode(3334, 3, "layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[3] = new NonTerminalStackNode(3324, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(3322, 2, prod__lit___91__char_class___range__91_91_, new char[] {91}, null, null);
      tmp[1] = new NonTerminalStackNode(3320, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(3318, 0, "QualifiedName", null, new ICompletionFilter[] {new StringFollowRequirement(new char[] {91})});
      builder.addAlternative(ObjectRascalRascal.prod__Parametric_UserType__conditional__name_QualifiedName__follow__lit___91_layouts_LAYOUTLIST_lit___91_layouts_LAYOUTLIST_parameters_iter_seps__Type__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___93_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__Name_UserType__name_QualifiedName_(builder);
      
        _init_prod__Parametric_UserType__conditional__name_QualifiedName__follow__lit___91_layouts_LAYOUTLIST_lit___91_layouts_LAYOUTLIST_parameters_iter_seps__Type__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___93_(builder);
      
    }
  }
	
  protected static class Body {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__Toplevels_Body__toplevels_iter_star_seps__Toplevel__layouts_LAYOUTLIST_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new SeparatedListStackNode(3370, 0, regular__iter_star_seps__Toplevel__layouts_LAYOUTLIST, new NonTerminalStackNode(3372, 0, "Toplevel", null, null), new AbstractStackNode[]{new NonTerminalStackNode(3374, 1, "layouts_LAYOUTLIST", null, null)}, false, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Toplevels_Body__toplevels_iter_star_seps__Toplevel__layouts_LAYOUTLIST_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__Toplevels_Body__toplevels_iter_star_seps__Toplevel__layouts_LAYOUTLIST_(builder);
      
    }
  }
	
  protected static class Word {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__Word__conditional__iter__char_class___range__0_8_range__11_12_range__14_31_range__33_65535__not_follow__char_class___range__0_8_range__11_12_range__14_31_range__33_65535_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new ListStackNode(3366, 0, regular__iter__char_class___range__0_8_range__11_12_range__14_31_range__33_65535, new CharStackNode(3368, 0, new char[][]{{0,8},{11,12},{14,31},{33,65535}}, null, null), true, null, new ICompletionFilter[] {new CharFollowRestriction(new char[][]{{0,8},{11,12},{14,31},{33,65535}})});
      builder.addAlternative(ObjectRascalRascal.prod__Word__conditional__iter__char_class___range__0_8_range__11_12_range__14_31_range__33_65535__not_follow__char_class___range__0_8_range__11_12_range__14_31_range__33_65535_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__Word__conditional__iter__char_class___range__0_8_range__11_12_range__14_31_range__33_65535__not_follow__char_class___range__0_8_range__11_12_range__14_31_range__33_65535_(builder);
      
    }
  }
	
  protected static class LAYOUT {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__LAYOUT__char_class___range__9_10_range__13_13_range__32_32_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new CharStackNode(3362, 0, new char[][]{{9,10},{13,13},{32,32}}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__LAYOUT__char_class___range__9_10_range__13_13_range__32_32_, tmp);
	}
    protected static final void _init_prod__LAYOUT__Comment_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode(3364, 0, "Comment", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__LAYOUT__Comment_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__LAYOUT__char_class___range__9_10_range__13_13_range__32_32_(builder);
      
        _init_prod__LAYOUT__Comment_(builder);
      
    }
  }
	
  protected static class DecimalIntegerLiteral {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__DecimalIntegerLiteral__conditional__lit_0__not_follow__char_class___range__48_57_range__65_90_range__95_95_range__97_122_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(3428, 0, prod__lit_0__char_class___range__48_48_, new char[] {48}, null, new ICompletionFilter[] {new CharFollowRestriction(new char[][]{{48,57},{65,90},{95,95},{97,122}})});
      builder.addAlternative(ObjectRascalRascal.prod__DecimalIntegerLiteral__conditional__lit_0__not_follow__char_class___range__48_57_range__65_90_range__95_95_range__97_122_, tmp);
	}
    protected static final void _init_prod__DecimalIntegerLiteral__char_class___range__49_57_conditional__iter_star__char_class___range__48_57__not_follow__char_class___range__48_57_range__65_90_range__95_95_range__97_122_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[2];
      
      tmp[1] = new ListStackNode(3432, 1, regular__iter_star__char_class___range__48_57, new CharStackNode(3434, 0, new char[][]{{48,57}}, null, null), false, null, new ICompletionFilter[] {new CharFollowRestriction(new char[][]{{48,57},{65,90},{95,95},{97,122}})});
      tmp[0] = new CharStackNode(3430, 0, new char[][]{{49,57}}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__DecimalIntegerLiteral__char_class___range__49_57_conditional__iter_star__char_class___range__48_57__not_follow__char_class___range__48_57_range__65_90_range__95_95_range__97_122_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__DecimalIntegerLiteral__conditional__lit_0__not_follow__char_class___range__48_57_range__65_90_range__95_95_range__97_122_(builder);
      
        _init_prod__DecimalIntegerLiteral__char_class___range__49_57_conditional__iter_star__char_class___range__48_57__not_follow__char_class___range__48_57_range__65_90_range__95_95_range__97_122_(builder);
      
    }
  }
	
  protected static class StringTemplate {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__While_StringTemplate__lit_while_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_condition_Expression_layouts_LAYOUTLIST_lit___41_layouts_LAYOUTLIST_lit___123_layouts_LAYOUTLIST_preStats_iter_star_seps__Statement__layouts_LAYOUTLIST_layouts_LAYOUTLIST_body_StringMiddle_layouts_LAYOUTLIST_postStats_iter_star_seps__Statement__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[17];
      
      tmp[16] = new LiteralStackNode(3476, 16, prod__lit___125__char_class___range__125_125_, new char[] {125}, null, null);
      tmp[15] = new NonTerminalStackNode(3474, 15, "layouts_LAYOUTLIST", null, null);
      tmp[14] = new SeparatedListStackNode(3468, 14, regular__iter_star_seps__Statement__layouts_LAYOUTLIST, new NonTerminalStackNode(3470, 0, "Statement", null, null), new AbstractStackNode[]{new NonTerminalStackNode(3472, 1, "layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[13] = new NonTerminalStackNode(3466, 13, "layouts_LAYOUTLIST", null, null);
      tmp[12] = new NonTerminalStackNode(3464, 12, "StringMiddle", null, null);
      tmp[11] = new NonTerminalStackNode(3462, 11, "layouts_LAYOUTLIST", null, null);
      tmp[10] = new SeparatedListStackNode(3456, 10, regular__iter_star_seps__Statement__layouts_LAYOUTLIST, new NonTerminalStackNode(3458, 0, "Statement", null, null), new AbstractStackNode[]{new NonTerminalStackNode(3460, 1, "layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[9] = new NonTerminalStackNode(3454, 9, "layouts_LAYOUTLIST", null, null);
      tmp[8] = new LiteralStackNode(3452, 8, prod__lit___123__char_class___range__123_123_, new char[] {123}, null, null);
      tmp[7] = new NonTerminalStackNode(3450, 7, "layouts_LAYOUTLIST", null, null);
      tmp[6] = new LiteralStackNode(3448, 6, prod__lit___41__char_class___range__41_41_, new char[] {41}, null, null);
      tmp[5] = new NonTerminalStackNode(3446, 5, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new NonTerminalStackNode(3444, 4, "Expression", null, null);
      tmp[3] = new NonTerminalStackNode(3442, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(3440, 2, prod__lit___40__char_class___range__40_40_, new char[] {40}, null, null);
      tmp[1] = new NonTerminalStackNode(3438, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(3436, 0, prod__lit_while__char_class___range__119_119_char_class___range__104_104_char_class___range__105_105_char_class___range__108_108_char_class___range__101_101_, new char[] {119,104,105,108,101}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__While_StringTemplate__lit_while_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_condition_Expression_layouts_LAYOUTLIST_lit___41_layouts_LAYOUTLIST_lit___123_layouts_LAYOUTLIST_preStats_iter_star_seps__Statement__layouts_LAYOUTLIST_layouts_LAYOUTLIST_body_StringMiddle_layouts_LAYOUTLIST_postStats_iter_star_seps__Statement__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_, tmp);
	}
    protected static final void _init_prod__IfThenElse_StringTemplate__lit_if_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_conditions_iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_layouts_LAYOUTLIST_lit___123_layouts_LAYOUTLIST_preStatsThen_iter_star_seps__Statement__layouts_LAYOUTLIST_layouts_LAYOUTLIST_thenString_StringMiddle_layouts_LAYOUTLIST_postStatsThen_iter_star_seps__Statement__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_layouts_LAYOUTLIST_lit_else_layouts_LAYOUTLIST_lit___123_layouts_LAYOUTLIST_preStatsElse_iter_star_seps__Statement__layouts_LAYOUTLIST_layouts_LAYOUTLIST_elseString_StringMiddle_layouts_LAYOUTLIST_postStatsElse_iter_star_seps__Statement__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[29];
      
      tmp[28] = new LiteralStackNode(3558, 28, prod__lit___125__char_class___range__125_125_, new char[] {125}, null, null);
      tmp[27] = new NonTerminalStackNode(3556, 27, "layouts_LAYOUTLIST", null, null);
      tmp[26] = new SeparatedListStackNode(3550, 26, regular__iter_star_seps__Statement__layouts_LAYOUTLIST, new NonTerminalStackNode(3552, 0, "Statement", null, null), new AbstractStackNode[]{new NonTerminalStackNode(3554, 1, "layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[25] = new NonTerminalStackNode(3548, 25, "layouts_LAYOUTLIST", null, null);
      tmp[24] = new NonTerminalStackNode(3546, 24, "StringMiddle", null, null);
      tmp[23] = new NonTerminalStackNode(3544, 23, "layouts_LAYOUTLIST", null, null);
      tmp[22] = new SeparatedListStackNode(3538, 22, regular__iter_star_seps__Statement__layouts_LAYOUTLIST, new NonTerminalStackNode(3540, 0, "Statement", null, null), new AbstractStackNode[]{new NonTerminalStackNode(3542, 1, "layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[21] = new NonTerminalStackNode(3536, 21, "layouts_LAYOUTLIST", null, null);
      tmp[20] = new LiteralStackNode(3534, 20, prod__lit___123__char_class___range__123_123_, new char[] {123}, null, null);
      tmp[19] = new NonTerminalStackNode(3532, 19, "layouts_LAYOUTLIST", null, null);
      tmp[18] = new LiteralStackNode(3530, 18, prod__lit_else__char_class___range__101_101_char_class___range__108_108_char_class___range__115_115_char_class___range__101_101_, new char[] {101,108,115,101}, null, null);
      tmp[17] = new NonTerminalStackNode(3528, 17, "layouts_LAYOUTLIST", null, null);
      tmp[16] = new LiteralStackNode(3526, 16, prod__lit___125__char_class___range__125_125_, new char[] {125}, null, null);
      tmp[15] = new NonTerminalStackNode(3524, 15, "layouts_LAYOUTLIST", null, null);
      tmp[14] = new SeparatedListStackNode(3518, 14, regular__iter_star_seps__Statement__layouts_LAYOUTLIST, new NonTerminalStackNode(3520, 0, "Statement", null, null), new AbstractStackNode[]{new NonTerminalStackNode(3522, 1, "layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[13] = new NonTerminalStackNode(3516, 13, "layouts_LAYOUTLIST", null, null);
      tmp[12] = new NonTerminalStackNode(3514, 12, "StringMiddle", null, null);
      tmp[11] = new NonTerminalStackNode(3512, 11, "layouts_LAYOUTLIST", null, null);
      tmp[10] = new SeparatedListStackNode(3506, 10, regular__iter_star_seps__Statement__layouts_LAYOUTLIST, new NonTerminalStackNode(3508, 0, "Statement", null, null), new AbstractStackNode[]{new NonTerminalStackNode(3510, 1, "layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[9] = new NonTerminalStackNode(3504, 9, "layouts_LAYOUTLIST", null, null);
      tmp[8] = new LiteralStackNode(3502, 8, prod__lit___123__char_class___range__123_123_, new char[] {123}, null, null);
      tmp[7] = new NonTerminalStackNode(3500, 7, "layouts_LAYOUTLIST", null, null);
      tmp[6] = new LiteralStackNode(3498, 6, prod__lit___41__char_class___range__41_41_, new char[] {41}, null, null);
      tmp[5] = new NonTerminalStackNode(3496, 5, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new SeparatedListStackNode(3486, 4, regular__iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST, new NonTerminalStackNode(3488, 0, "Expression", null, null), new AbstractStackNode[]{new NonTerminalStackNode(3490, 1, "layouts_LAYOUTLIST", null, null), new LiteralStackNode(3492, 2, prod__lit___44__char_class___range__44_44_, new char[] {44}, null, null), new NonTerminalStackNode(3494, 3, "layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[3] = new NonTerminalStackNode(3484, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(3482, 2, prod__lit___40__char_class___range__40_40_, new char[] {40}, null, null);
      tmp[1] = new NonTerminalStackNode(3480, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(3478, 0, prod__lit_if__char_class___range__105_105_char_class___range__102_102_, new char[] {105,102}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__IfThenElse_StringTemplate__lit_if_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_conditions_iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_layouts_LAYOUTLIST_lit___123_layouts_LAYOUTLIST_preStatsThen_iter_star_seps__Statement__layouts_LAYOUTLIST_layouts_LAYOUTLIST_thenString_StringMiddle_layouts_LAYOUTLIST_postStatsThen_iter_star_seps__Statement__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_layouts_LAYOUTLIST_lit_else_layouts_LAYOUTLIST_lit___123_layouts_LAYOUTLIST_preStatsElse_iter_star_seps__Statement__layouts_LAYOUTLIST_layouts_LAYOUTLIST_elseString_StringMiddle_layouts_LAYOUTLIST_postStatsElse_iter_star_seps__Statement__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_, tmp);
	}
    protected static final void _init_prod__DoWhile_StringTemplate__lit_do_layouts_LAYOUTLIST_lit___123_layouts_LAYOUTLIST_preStats_iter_star_seps__Statement__layouts_LAYOUTLIST_layouts_LAYOUTLIST_body_StringMiddle_layouts_LAYOUTLIST_postStats_iter_star_seps__Statement__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_layouts_LAYOUTLIST_lit_while_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_condition_Expression_layouts_LAYOUTLIST_lit___41_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[19];
      
      tmp[18] = new LiteralStackNode(3654, 18, prod__lit___41__char_class___range__41_41_, new char[] {41}, null, null);
      tmp[17] = new NonTerminalStackNode(3652, 17, "layouts_LAYOUTLIST", null, null);
      tmp[16] = new NonTerminalStackNode(3650, 16, "Expression", null, null);
      tmp[15] = new NonTerminalStackNode(3648, 15, "layouts_LAYOUTLIST", null, null);
      tmp[14] = new LiteralStackNode(3646, 14, prod__lit___40__char_class___range__40_40_, new char[] {40}, null, null);
      tmp[13] = new NonTerminalStackNode(3644, 13, "layouts_LAYOUTLIST", null, null);
      tmp[12] = new LiteralStackNode(3642, 12, prod__lit_while__char_class___range__119_119_char_class___range__104_104_char_class___range__105_105_char_class___range__108_108_char_class___range__101_101_, new char[] {119,104,105,108,101}, null, null);
      tmp[11] = new NonTerminalStackNode(3640, 11, "layouts_LAYOUTLIST", null, null);
      tmp[10] = new LiteralStackNode(3638, 10, prod__lit___125__char_class___range__125_125_, new char[] {125}, null, null);
      tmp[9] = new NonTerminalStackNode(3636, 9, "layouts_LAYOUTLIST", null, null);
      tmp[8] = new SeparatedListStackNode(3630, 8, regular__iter_star_seps__Statement__layouts_LAYOUTLIST, new NonTerminalStackNode(3632, 0, "Statement", null, null), new AbstractStackNode[]{new NonTerminalStackNode(3634, 1, "layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[7] = new NonTerminalStackNode(3628, 7, "layouts_LAYOUTLIST", null, null);
      tmp[6] = new NonTerminalStackNode(3626, 6, "StringMiddle", null, null);
      tmp[5] = new NonTerminalStackNode(3624, 5, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new SeparatedListStackNode(3618, 4, regular__iter_star_seps__Statement__layouts_LAYOUTLIST, new NonTerminalStackNode(3620, 0, "Statement", null, null), new AbstractStackNode[]{new NonTerminalStackNode(3622, 1, "layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[3] = new NonTerminalStackNode(3616, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(3614, 2, prod__lit___123__char_class___range__123_123_, new char[] {123}, null, null);
      tmp[1] = new NonTerminalStackNode(3612, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(3610, 0, prod__lit_do__char_class___range__100_100_char_class___range__111_111_, new char[] {100,111}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__DoWhile_StringTemplate__lit_do_layouts_LAYOUTLIST_lit___123_layouts_LAYOUTLIST_preStats_iter_star_seps__Statement__layouts_LAYOUTLIST_layouts_LAYOUTLIST_body_StringMiddle_layouts_LAYOUTLIST_postStats_iter_star_seps__Statement__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_layouts_LAYOUTLIST_lit_while_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_condition_Expression_layouts_LAYOUTLIST_lit___41_, tmp);
	}
    protected static final void _init_prod__For_StringTemplate__lit_for_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_generators_iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_layouts_LAYOUTLIST_lit___123_layouts_LAYOUTLIST_preStats_iter_star_seps__Statement__layouts_LAYOUTLIST_layouts_LAYOUTLIST_body_StringMiddle_layouts_LAYOUTLIST_postStats_iter_star_seps__Statement__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[17];
      
      tmp[16] = new LiteralStackNode(3608, 16, prod__lit___125__char_class___range__125_125_, new char[] {125}, null, null);
      tmp[15] = new NonTerminalStackNode(3606, 15, "layouts_LAYOUTLIST", null, null);
      tmp[14] = new SeparatedListStackNode(3600, 14, regular__iter_star_seps__Statement__layouts_LAYOUTLIST, new NonTerminalStackNode(3602, 0, "Statement", null, null), new AbstractStackNode[]{new NonTerminalStackNode(3604, 1, "layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[13] = new NonTerminalStackNode(3598, 13, "layouts_LAYOUTLIST", null, null);
      tmp[12] = new NonTerminalStackNode(3596, 12, "StringMiddle", null, null);
      tmp[11] = new NonTerminalStackNode(3594, 11, "layouts_LAYOUTLIST", null, null);
      tmp[10] = new SeparatedListStackNode(3588, 10, regular__iter_star_seps__Statement__layouts_LAYOUTLIST, new NonTerminalStackNode(3590, 0, "Statement", null, null), new AbstractStackNode[]{new NonTerminalStackNode(3592, 1, "layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[9] = new NonTerminalStackNode(3586, 9, "layouts_LAYOUTLIST", null, null);
      tmp[8] = new LiteralStackNode(3584, 8, prod__lit___123__char_class___range__123_123_, new char[] {123}, null, null);
      tmp[7] = new NonTerminalStackNode(3582, 7, "layouts_LAYOUTLIST", null, null);
      tmp[6] = new LiteralStackNode(3580, 6, prod__lit___41__char_class___range__41_41_, new char[] {41}, null, null);
      tmp[5] = new NonTerminalStackNode(3578, 5, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new SeparatedListStackNode(3568, 4, regular__iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST, new NonTerminalStackNode(3570, 0, "Expression", null, null), new AbstractStackNode[]{new NonTerminalStackNode(3572, 1, "layouts_LAYOUTLIST", null, null), new LiteralStackNode(3574, 2, prod__lit___44__char_class___range__44_44_, new char[] {44}, null, null), new NonTerminalStackNode(3576, 3, "layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[3] = new NonTerminalStackNode(3566, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(3564, 2, prod__lit___40__char_class___range__40_40_, new char[] {40}, null, null);
      tmp[1] = new NonTerminalStackNode(3562, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(3560, 0, prod__lit_for__char_class___range__102_102_char_class___range__111_111_char_class___range__114_114_, new char[] {102,111,114}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__For_StringTemplate__lit_for_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_generators_iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_layouts_LAYOUTLIST_lit___123_layouts_LAYOUTLIST_preStats_iter_star_seps__Statement__layouts_LAYOUTLIST_layouts_LAYOUTLIST_body_StringMiddle_layouts_LAYOUTLIST_postStats_iter_star_seps__Statement__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_, tmp);
	}
    protected static final void _init_prod__IfThen_StringTemplate__lit_if_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_conditions_iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_layouts_LAYOUTLIST_lit___123_layouts_LAYOUTLIST_preStats_iter_star_seps__Statement__layouts_LAYOUTLIST_layouts_LAYOUTLIST_body_StringMiddle_layouts_LAYOUTLIST_postStats_iter_star_seps__Statement__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[17];
      
      tmp[16] = new LiteralStackNode(3704, 16, prod__lit___125__char_class___range__125_125_, new char[] {125}, null, null);
      tmp[15] = new NonTerminalStackNode(3702, 15, "layouts_LAYOUTLIST", null, null);
      tmp[14] = new SeparatedListStackNode(3696, 14, regular__iter_star_seps__Statement__layouts_LAYOUTLIST, new NonTerminalStackNode(3698, 0, "Statement", null, null), new AbstractStackNode[]{new NonTerminalStackNode(3700, 1, "layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[13] = new NonTerminalStackNode(3694, 13, "layouts_LAYOUTLIST", null, null);
      tmp[12] = new NonTerminalStackNode(3692, 12, "StringMiddle", null, null);
      tmp[11] = new NonTerminalStackNode(3690, 11, "layouts_LAYOUTLIST", null, null);
      tmp[10] = new SeparatedListStackNode(3684, 10, regular__iter_star_seps__Statement__layouts_LAYOUTLIST, new NonTerminalStackNode(3686, 0, "Statement", null, null), new AbstractStackNode[]{new NonTerminalStackNode(3688, 1, "layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[9] = new NonTerminalStackNode(3682, 9, "layouts_LAYOUTLIST", null, null);
      tmp[8] = new LiteralStackNode(3680, 8, prod__lit___123__char_class___range__123_123_, new char[] {123}, null, null);
      tmp[7] = new NonTerminalStackNode(3678, 7, "layouts_LAYOUTLIST", null, null);
      tmp[6] = new LiteralStackNode(3676, 6, prod__lit___41__char_class___range__41_41_, new char[] {41}, null, null);
      tmp[5] = new NonTerminalStackNode(3674, 5, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new SeparatedListStackNode(3664, 4, regular__iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST, new NonTerminalStackNode(3666, 0, "Expression", null, null), new AbstractStackNode[]{new NonTerminalStackNode(3668, 1, "layouts_LAYOUTLIST", null, null), new LiteralStackNode(3670, 2, prod__lit___44__char_class___range__44_44_, new char[] {44}, null, null), new NonTerminalStackNode(3672, 3, "layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[3] = new NonTerminalStackNode(3662, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(3660, 2, prod__lit___40__char_class___range__40_40_, new char[] {40}, null, null);
      tmp[1] = new NonTerminalStackNode(3658, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(3656, 0, prod__lit_if__char_class___range__105_105_char_class___range__102_102_, new char[] {105,102}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__IfThen_StringTemplate__lit_if_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_conditions_iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_layouts_LAYOUTLIST_lit___123_layouts_LAYOUTLIST_preStats_iter_star_seps__Statement__layouts_LAYOUTLIST_layouts_LAYOUTLIST_body_StringMiddle_layouts_LAYOUTLIST_postStats_iter_star_seps__Statement__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__While_StringTemplate__lit_while_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_condition_Expression_layouts_LAYOUTLIST_lit___41_layouts_LAYOUTLIST_lit___123_layouts_LAYOUTLIST_preStats_iter_star_seps__Statement__layouts_LAYOUTLIST_layouts_LAYOUTLIST_body_StringMiddle_layouts_LAYOUTLIST_postStats_iter_star_seps__Statement__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_(builder);
      
        _init_prod__IfThenElse_StringTemplate__lit_if_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_conditions_iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_layouts_LAYOUTLIST_lit___123_layouts_LAYOUTLIST_preStatsThen_iter_star_seps__Statement__layouts_LAYOUTLIST_layouts_LAYOUTLIST_thenString_StringMiddle_layouts_LAYOUTLIST_postStatsThen_iter_star_seps__Statement__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_layouts_LAYOUTLIST_lit_else_layouts_LAYOUTLIST_lit___123_layouts_LAYOUTLIST_preStatsElse_iter_star_seps__Statement__layouts_LAYOUTLIST_layouts_LAYOUTLIST_elseString_StringMiddle_layouts_LAYOUTLIST_postStatsElse_iter_star_seps__Statement__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_(builder);
      
        _init_prod__DoWhile_StringTemplate__lit_do_layouts_LAYOUTLIST_lit___123_layouts_LAYOUTLIST_preStats_iter_star_seps__Statement__layouts_LAYOUTLIST_layouts_LAYOUTLIST_body_StringMiddle_layouts_LAYOUTLIST_postStats_iter_star_seps__Statement__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_layouts_LAYOUTLIST_lit_while_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_condition_Expression_layouts_LAYOUTLIST_lit___41_(builder);
      
        _init_prod__For_StringTemplate__lit_for_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_generators_iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_layouts_LAYOUTLIST_lit___123_layouts_LAYOUTLIST_preStats_iter_star_seps__Statement__layouts_LAYOUTLIST_layouts_LAYOUTLIST_body_StringMiddle_layouts_LAYOUTLIST_postStats_iter_star_seps__Statement__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_(builder);
      
        _init_prod__IfThen_StringTemplate__lit_if_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_conditions_iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_layouts_LAYOUTLIST_lit___123_layouts_LAYOUTLIST_preStats_iter_star_seps__Statement__layouts_LAYOUTLIST_layouts_LAYOUTLIST_body_StringMiddle_layouts_LAYOUTLIST_postStats_iter_star_seps__Statement__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_(builder);
      
    }
  }
	
  protected static class PathPart {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__NonInterpolated_PathPart__pathChars_PathChars_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode(3716, 0, "PathChars", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__NonInterpolated_PathPart__pathChars_PathChars_, tmp);
	}
    protected static final void _init_prod__Interpolated_PathPart__pre_PrePathChars_layouts_LAYOUTLIST_expression_Expression_layouts_LAYOUTLIST_tail_PathTail_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(3726, 4, "PathTail", null, null);
      tmp[3] = new NonTerminalStackNode(3724, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(3722, 2, "Expression", null, null);
      tmp[1] = new NonTerminalStackNode(3720, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(3718, 0, "PrePathChars", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Interpolated_PathPart__pre_PrePathChars_layouts_LAYOUTLIST_expression_Expression_layouts_LAYOUTLIST_tail_PathTail_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__NonInterpolated_PathPart__pathChars_PathChars_(builder);
      
        _init_prod__Interpolated_PathPart__pre_PrePathChars_layouts_LAYOUTLIST_expression_Expression_layouts_LAYOUTLIST_tail_PathTail_(builder);
      
    }
  }
	
  protected static class RegExp {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__RegExp__char_class___range__92_92_char_class___range__47_47_range__60_60_range__62_62_range__92_92_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[2];
      
      tmp[1] = new CharStackNode(3772, 1, new char[][]{{47,47},{60,60},{62,62},{92,92}}, null, null);
      tmp[0] = new CharStackNode(3770, 0, new char[][]{{92,92}}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RegExp__char_class___range__92_92_char_class___range__47_47_range__60_60_range__62_62_range__92_92_, tmp);
	}
    protected static final void _init_prod__RegExp__lit___60_Name_lit___58_iter_star__NamedRegExp_lit___62_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new LiteralStackNode(3784, 4, prod__lit___62__char_class___range__62_62_, new char[] {62}, null, null);
      tmp[3] = new ListStackNode(3780, 3, regular__iter_star__NamedRegExp, new NonTerminalStackNode(3782, 0, "NamedRegExp", null, null), false, null, null);
      tmp[2] = new LiteralStackNode(3778, 2, prod__lit___58__char_class___range__58_58_, new char[] {58}, null, null);
      tmp[1] = new NonTerminalStackNode(3776, 1, "Name", null, null);
      tmp[0] = new LiteralStackNode(3774, 0, prod__lit___60__char_class___range__60_60_, new char[] {60}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RegExp__lit___60_Name_lit___58_iter_star__NamedRegExp_lit___62_, tmp);
	}
    protected static final void _init_prod__RegExp__char_class___range__60_60_expression_Expression_char_class___range__62_62__tag__category___77_101_116_97_86_97_114_105_97_98_108_101(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[2] = new CharStackNode(3790, 2, new char[][]{{62,62}}, null, null);
      tmp[1] = new NonTerminalStackNode(3788, 1, "Expression", null, null);
      tmp[0] = new CharStackNode(3786, 0, new char[][]{{60,60}}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RegExp__char_class___range__60_60_expression_Expression_char_class___range__62_62__tag__category___77_101_116_97_86_97_114_105_97_98_108_101, tmp);
	}
    protected static final void _init_prod__RegExp__char_class___range__0_46_range__48_59_range__61_61_range__63_91_range__93_65535_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new CharStackNode(3792, 0, new char[][]{{0,46},{48,59},{61,61},{63,91},{93,65535}}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RegExp__char_class___range__0_46_range__48_59_range__61_61_range__63_91_range__93_65535_, tmp);
	}
    protected static final void _init_prod__RegExp__lit___60_Name_lit___62_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[2] = new LiteralStackNode(3798, 2, prod__lit___62__char_class___range__62_62_, new char[] {62}, null, null);
      tmp[1] = new NonTerminalStackNode(3796, 1, "Name", null, null);
      tmp[0] = new LiteralStackNode(3794, 0, prod__lit___60__char_class___range__60_60_, new char[] {60}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RegExp__lit___60_Name_lit___62_, tmp);
	}
    protected static final void _init_prod__RegExp__Backslash_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode(3800, 0, "Backslash", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RegExp__Backslash_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__RegExp__char_class___range__92_92_char_class___range__47_47_range__60_60_range__62_62_range__92_92_(builder);
      
        _init_prod__RegExp__lit___60_Name_lit___58_iter_star__NamedRegExp_lit___62_(builder);
      
        _init_prod__RegExp__char_class___range__60_60_expression_Expression_char_class___range__62_62__tag__category___77_101_116_97_86_97_114_105_97_98_108_101(builder);
      
        _init_prod__RegExp__char_class___range__0_46_range__48_59_range__61_61_range__63_91_range__93_65535_(builder);
      
        _init_prod__RegExp__lit___60_Name_lit___62_(builder);
      
        _init_prod__RegExp__Backslash_(builder);
      
    }
  }
	
  protected static class MidStringChars {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__MidStringChars__char_class___range__62_62_iter_star__StringCharacter_char_class___range__60_60__tag__category___67_111_110_115_116_97_110_116(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[2] = new CharStackNode(3834, 2, new char[][]{{60,60}}, null, null);
      tmp[1] = new ListStackNode(3830, 1, regular__iter_star__StringCharacter, new NonTerminalStackNode(3832, 0, "StringCharacter", null, null), false, null, null);
      tmp[0] = new CharStackNode(3828, 0, new char[][]{{62,62}}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__MidStringChars__char_class___range__62_62_iter_star__StringCharacter_char_class___range__60_60__tag__category___67_111_110_115_116_97_110_116, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__MidStringChars__char_class___range__62_62_iter_star__StringCharacter_char_class___range__60_60__tag__category___67_111_110_115_116_97_110_116(builder);
      
    }
  }
	
  protected static class RegExpModifier {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__RegExpModifier__iter_star__char_class___range__100_100_range__105_105_range__109_109_range__115_115_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new ListStackNode(3844, 0, regular__iter_star__char_class___range__100_100_range__105_105_range__109_109_range__115_115, new CharStackNode(3846, 0, new char[][]{{100,100},{105,105},{109,109},{115,115}}, null, null), false, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RegExpModifier__iter_star__char_class___range__100_100_range__105_105_range__109_109_range__115_115_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__RegExpModifier__iter_star__char_class___range__100_100_range__105_105_range__109_109_range__115_115_(builder);
      
    }
  }
	
  protected static class layouts_$BACKTICKS {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__layouts_$BACKTICKS__(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new EpsilonStackNode(3858, 0);
      builder.addAlternative(ObjectRascalRascal.prod__layouts_$BACKTICKS__, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__layouts_$BACKTICKS__(builder);
      
    }
  }
	
  protected static class Assoc {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__Associative_Assoc__lit_assoc_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(3860, 0, prod__lit_assoc__char_class___range__97_97_char_class___range__115_115_char_class___range__115_115_char_class___range__111_111_char_class___range__99_99_, new char[] {97,115,115,111,99}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Associative_Assoc__lit_assoc_, tmp);
	}
    protected static final void _init_prod__Left_Assoc__lit_left_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(3862, 0, prod__lit_left__char_class___range__108_108_char_class___range__101_101_char_class___range__102_102_char_class___range__116_116_, new char[] {108,101,102,116}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Left_Assoc__lit_left_, tmp);
	}
    protected static final void _init_prod__NonAssociative_Assoc__lit_non_assoc_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(3864, 0, prod__lit_non_assoc__char_class___range__110_110_char_class___range__111_111_char_class___range__110_110_char_class___range__45_45_char_class___range__97_97_char_class___range__115_115_char_class___range__115_115_char_class___range__111_111_char_class___range__99_99_, new char[] {110,111,110,45,97,115,115,111,99}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__NonAssociative_Assoc__lit_non_assoc_, tmp);
	}
    protected static final void _init_prod__Right_Assoc__lit_right_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(3866, 0, prod__lit_right__char_class___range__114_114_char_class___range__105_105_char_class___range__103_103_char_class___range__104_104_char_class___range__116_116_, new char[] {114,105,103,104,116}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Right_Assoc__lit_right_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__Associative_Assoc__lit_assoc_(builder);
      
        _init_prod__Left_Assoc__lit_left_(builder);
      
        _init_prod__NonAssociative_Assoc__lit_non_assoc_(builder);
      
        _init_prod__Right_Assoc__lit_right_(builder);
      
    }
  }
	
  protected static class Replacement {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__Unconditional_Replacement__replacementExpression_Expression_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode(3868, 0, "Expression", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Unconditional_Replacement__replacementExpression_Expression_, tmp);
	}
    protected static final void _init_prod__Conditional_Replacement__replacementExpression_Expression_layouts_LAYOUTLIST_lit_when_layouts_LAYOUTLIST_conditions_iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new SeparatedListStackNode(3878, 4, regular__iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST, new NonTerminalStackNode(3880, 0, "Expression", null, null), new AbstractStackNode[]{new NonTerminalStackNode(3882, 1, "layouts_LAYOUTLIST", null, null), new LiteralStackNode(3884, 2, prod__lit___44__char_class___range__44_44_, new char[] {44}, null, null), new NonTerminalStackNode(3886, 3, "layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[3] = new NonTerminalStackNode(3876, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(3874, 2, prod__lit_when__char_class___range__119_119_char_class___range__104_104_char_class___range__101_101_char_class___range__110_110_, new char[] {119,104,101,110}, null, null);
      tmp[1] = new NonTerminalStackNode(3872, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(3870, 0, "Expression", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Conditional_Replacement__replacementExpression_Expression_layouts_LAYOUTLIST_lit_when_layouts_LAYOUTLIST_conditions_iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__Unconditional_Replacement__replacementExpression_Expression_(builder);
      
        _init_prod__Conditional_Replacement__replacementExpression_Expression_layouts_LAYOUTLIST_lit_when_layouts_LAYOUTLIST_conditions_iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_(builder);
      
    }
  }
	
  protected static class DataTarget {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__Empty_DataTarget__(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new EpsilonStackNode(3936, 0);
      builder.addAlternative(ObjectRascalRascal.prod__Empty_DataTarget__, tmp);
	}
    protected static final void _init_prod__Labeled_DataTarget__label_Name_layouts_LAYOUTLIST_lit___58_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[2] = new LiteralStackNode(3942, 2, prod__lit___58__char_class___range__58_58_, new char[] {58}, null, null);
      tmp[1] = new NonTerminalStackNode(3940, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(3938, 0, "Name", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Labeled_DataTarget__label_Name_layouts_LAYOUTLIST_lit___58_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__Empty_DataTarget__(builder);
      
        _init_prod__Labeled_DataTarget__label_Name_layouts_LAYOUTLIST_lit___58_(builder);
      
    }
  }
	
  protected static class layouts_$default$ {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__layouts_$default$__(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new EpsilonStackNode(3944, 0);
      builder.addAlternative(ObjectRascalRascal.prod__layouts_$default$__, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__layouts_$default$__(builder);
      
    }
  }
	
  protected static class RealLiteral {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__RealLiteral__conditional__lit___46__not_precede__char_class___range__46_46_iter__char_class___range__48_57_opt__char_class___range__68_68_range__70_70_range__100_100_range__102_102_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[2] = new OptionalStackNode(3952, 2, regular__opt__char_class___range__68_68_range__70_70_range__100_100_range__102_102, new CharStackNode(3954, 0, new char[][]{{68,68},{70,70},{100,100},{102,102}}, null, null), null, null);
      tmp[1] = new ListStackNode(3948, 1, regular__iter__char_class___range__48_57, new CharStackNode(3950, 0, new char[][]{{48,57}}, null, null), true, null, null);
      tmp[0] = new LiteralStackNode(3946, 0, prod__lit___46__char_class___range__46_46_, new char[] {46}, new IEnterFilter[] {new CharPrecedeRestriction(new char[][]{{46,46}})}, null);
      builder.addAlternative(ObjectRascalRascal.prod__RealLiteral__conditional__lit___46__not_precede__char_class___range__46_46_iter__char_class___range__48_57_opt__char_class___range__68_68_range__70_70_range__100_100_range__102_102_, tmp);
	}
    protected static final void _init_prod__RealLiteral__iter__char_class___range__48_57_lit___46_iter_star__char_class___range__48_57_char_class___range__69_69_range__101_101_opt__char_class___range__43_43_range__45_45_iter__char_class___range__48_57_opt__char_class___range__68_68_range__70_70_range__100_100_range__102_102_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[7];
      
      tmp[0] = new ListStackNode(3956, 0, regular__iter__char_class___range__48_57, new CharStackNode(3958, 0, new char[][]{{48,57}}, null, null), true, null, null);
      tmp[1] = new LiteralStackNode(3960, 1, prod__lit___46__char_class___range__46_46_, new char[] {46}, null, null);
      tmp[2] = new ListStackNode(3962, 2, regular__iter_star__char_class___range__48_57, new CharStackNode(3964, 0, new char[][]{{48,57}}, null, null), false, null, null);
      tmp[3] = new CharStackNode(3966, 3, new char[][]{{69,69},{101,101}}, null, null);
      tmp[4] = new OptionalStackNode(3968, 4, regular__opt__char_class___range__43_43_range__45_45, new CharStackNode(3970, 0, new char[][]{{43,43},{45,45}}, null, null), null, null);
      tmp[5] = new ListStackNode(3972, 5, regular__iter__char_class___range__48_57, new CharStackNode(3974, 0, new char[][]{{48,57}}, null, null), true, null, null);
      tmp[6] = new OptionalStackNode(3976, 6, regular__opt__char_class___range__68_68_range__70_70_range__100_100_range__102_102, new CharStackNode(3978, 0, new char[][]{{68,68},{70,70},{100,100},{102,102}}, null, null), null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RealLiteral__iter__char_class___range__48_57_lit___46_iter_star__char_class___range__48_57_char_class___range__69_69_range__101_101_opt__char_class___range__43_43_range__45_45_iter__char_class___range__48_57_opt__char_class___range__68_68_range__70_70_range__100_100_range__102_102_, tmp);
	}
    protected static final void _init_prod__RealLiteral__iter__char_class___range__48_57_conditional__lit___46__not_follow__lit___46_iter_star__char_class___range__48_57_opt__char_class___range__68_68_range__70_70_range__100_100_range__102_102_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[4];
      
      tmp[3] = new OptionalStackNode(4010, 3, regular__opt__char_class___range__68_68_range__70_70_range__100_100_range__102_102, new CharStackNode(4012, 0, new char[][]{{68,68},{70,70},{100,100},{102,102}}, null, null), null, null);
      tmp[2] = new ListStackNode(4006, 2, regular__iter_star__char_class___range__48_57, new CharStackNode(4008, 0, new char[][]{{48,57}}, null, null), false, null, null);
      tmp[1] = new LiteralStackNode(4004, 1, prod__lit___46__char_class___range__46_46_, new char[] {46}, null, new ICompletionFilter[] {new StringFollowRestriction(new char[] {46})});
      tmp[0] = new ListStackNode(4000, 0, regular__iter__char_class___range__48_57, new CharStackNode(4002, 0, new char[][]{{48,57}}, null, null), true, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RealLiteral__iter__char_class___range__48_57_conditional__lit___46__not_follow__lit___46_iter_star__char_class___range__48_57_opt__char_class___range__68_68_range__70_70_range__100_100_range__102_102_, tmp);
	}
    protected static final void _init_prod__RealLiteral__conditional__lit___46__not_precede__char_class___range__46_46_iter__char_class___range__48_57_char_class___range__69_69_range__101_101_opt__char_class___range__43_43_range__45_45_iter__char_class___range__48_57_opt__char_class___range__68_68_range__70_70_range__100_100_range__102_102_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[6];
      
      tmp[5] = new OptionalStackNode(3996, 5, regular__opt__char_class___range__68_68_range__70_70_range__100_100_range__102_102, new CharStackNode(3998, 0, new char[][]{{68,68},{70,70},{100,100},{102,102}}, null, null), null, null);
      tmp[4] = new ListStackNode(3992, 4, regular__iter__char_class___range__48_57, new CharStackNode(3994, 0, new char[][]{{48,57}}, null, null), true, null, null);
      tmp[3] = new OptionalStackNode(3988, 3, regular__opt__char_class___range__43_43_range__45_45, new CharStackNode(3990, 0, new char[][]{{43,43},{45,45}}, null, null), null, null);
      tmp[2] = new CharStackNode(3986, 2, new char[][]{{69,69},{101,101}}, null, null);
      tmp[1] = new ListStackNode(3982, 1, regular__iter__char_class___range__48_57, new CharStackNode(3984, 0, new char[][]{{48,57}}, null, null), true, null, null);
      tmp[0] = new LiteralStackNode(3980, 0, prod__lit___46__char_class___range__46_46_, new char[] {46}, new IEnterFilter[] {new CharPrecedeRestriction(new char[][]{{46,46}})}, null);
      builder.addAlternative(ObjectRascalRascal.prod__RealLiteral__conditional__lit___46__not_precede__char_class___range__46_46_iter__char_class___range__48_57_char_class___range__69_69_range__101_101_opt__char_class___range__43_43_range__45_45_iter__char_class___range__48_57_opt__char_class___range__68_68_range__70_70_range__100_100_range__102_102_, tmp);
	}
    protected static final void _init_prod__RealLiteral__iter__char_class___range__48_57_char_class___range__69_69_range__101_101_opt__char_class___range__43_43_range__45_45_iter__char_class___range__48_57_opt__char_class___range__68_68_range__70_70_range__100_100_range__102_102_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new OptionalStackNode(4028, 4, regular__opt__char_class___range__68_68_range__70_70_range__100_100_range__102_102, new CharStackNode(4030, 0, new char[][]{{68,68},{70,70},{100,100},{102,102}}, null, null), null, null);
      tmp[3] = new ListStackNode(4024, 3, regular__iter__char_class___range__48_57, new CharStackNode(4026, 0, new char[][]{{48,57}}, null, null), true, null, null);
      tmp[2] = new OptionalStackNode(4020, 2, regular__opt__char_class___range__43_43_range__45_45, new CharStackNode(4022, 0, new char[][]{{43,43},{45,45}}, null, null), null, null);
      tmp[1] = new CharStackNode(4018, 1, new char[][]{{69,69},{101,101}}, null, null);
      tmp[0] = new ListStackNode(4014, 0, regular__iter__char_class___range__48_57, new CharStackNode(4016, 0, new char[][]{{48,57}}, null, null), true, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RealLiteral__iter__char_class___range__48_57_char_class___range__69_69_range__101_101_opt__char_class___range__43_43_range__45_45_iter__char_class___range__48_57_opt__char_class___range__68_68_range__70_70_range__100_100_range__102_102_, tmp);
	}
    protected static final void _init_prod__RealLiteral__iter__char_class___range__48_57_char_class___range__68_68_range__70_70_range__100_100_range__102_102_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[2];
      
      tmp[1] = new CharStackNode(4036, 1, new char[][]{{68,68},{70,70},{100,100},{102,102}}, null, null);
      tmp[0] = new ListStackNode(4032, 0, regular__iter__char_class___range__48_57, new CharStackNode(4034, 0, new char[][]{{48,57}}, null, null), true, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RealLiteral__iter__char_class___range__48_57_char_class___range__68_68_range__70_70_range__100_100_range__102_102_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__RealLiteral__conditional__lit___46__not_precede__char_class___range__46_46_iter__char_class___range__48_57_opt__char_class___range__68_68_range__70_70_range__100_100_range__102_102_(builder);
      
        _init_prod__RealLiteral__iter__char_class___range__48_57_lit___46_iter_star__char_class___range__48_57_char_class___range__69_69_range__101_101_opt__char_class___range__43_43_range__45_45_iter__char_class___range__48_57_opt__char_class___range__68_68_range__70_70_range__100_100_range__102_102_(builder);
      
        _init_prod__RealLiteral__iter__char_class___range__48_57_conditional__lit___46__not_follow__lit___46_iter_star__char_class___range__48_57_opt__char_class___range__68_68_range__70_70_range__100_100_range__102_102_(builder);
      
        _init_prod__RealLiteral__conditional__lit___46__not_precede__char_class___range__46_46_iter__char_class___range__48_57_char_class___range__69_69_range__101_101_opt__char_class___range__43_43_range__45_45_iter__char_class___range__48_57_opt__char_class___range__68_68_range__70_70_range__100_100_range__102_102_(builder);
      
        _init_prod__RealLiteral__iter__char_class___range__48_57_char_class___range__69_69_range__101_101_opt__char_class___range__43_43_range__45_45_iter__char_class___range__48_57_opt__char_class___range__68_68_range__70_70_range__100_100_range__102_102_(builder);
      
        _init_prod__RealLiteral__iter__char_class___range__48_57_char_class___range__68_68_range__70_70_range__100_100_range__102_102_(builder);
      
    }
  }
	
  protected static class layouts_LAYOUTLIST {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__layouts_LAYOUTLIST__conditional__iter_star__LAYOUT__not_follow__char_class___range__9_10_range__13_13_range__32_32_not_follow__lit___47_47_not_follow__lit___47_42_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new ListStackNode(4076, 0, regular__iter_star__LAYOUT, new NonTerminalStackNode(4078, 0, "LAYOUT", null, null), false, null, new ICompletionFilter[] {new CharFollowRestriction(new char[][]{{9,10},{13,13},{32,32}}), new StringFollowRestriction(new char[] {47,47}), new StringFollowRestriction(new char[] {47,42})});
      builder.addAlternative(ObjectRascalRascal.prod__layouts_LAYOUTLIST__conditional__iter_star__LAYOUT__not_follow__char_class___range__9_10_range__13_13_range__32_32_not_follow__lit___47_47_not_follow__lit___47_42_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__layouts_LAYOUTLIST__conditional__iter_star__LAYOUT__not_follow__char_class___range__9_10_range__13_13_range__32_32_not_follow__lit___47_47_not_follow__lit___47_42_(builder);
      
    }
  }
	
  protected static class StringCharacter {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__StringCharacter__OctalEscapeSequence_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode(4088, 0, "OctalEscapeSequence", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__StringCharacter__OctalEscapeSequence_, tmp);
	}
    protected static final void _init_prod__StringCharacter__lit___92_char_class___range__34_34_range__39_39_range__60_60_range__62_62_range__92_92_range__98_98_range__102_102_range__110_110_range__114_114_range__116_116_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[2];
      
      tmp[1] = new CharStackNode(4092, 1, new char[][]{{34,34},{39,39},{60,60},{62,62},{92,92},{98,98},{102,102},{110,110},{114,114},{116,116}}, null, null);
      tmp[0] = new LiteralStackNode(4090, 0, prod__lit___92__char_class___range__92_92_, new char[] {92}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__StringCharacter__lit___92_char_class___range__34_34_range__39_39_range__60_60_range__62_62_range__92_92_range__98_98_range__102_102_range__110_110_range__114_114_range__116_116_, tmp);
	}
    protected static final void _init_prod__StringCharacter__UnicodeEscape_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode(4094, 0, "UnicodeEscape", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__StringCharacter__UnicodeEscape_, tmp);
	}
    protected static final void _init_prod__StringCharacter__char_class___range__0_33_range__35_38_range__40_59_range__61_61_range__63_91_range__93_65535_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new CharStackNode(4096, 0, new char[][]{{0,33},{35,38},{40,59},{61,61},{63,91},{93,65535}}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__StringCharacter__char_class___range__0_33_range__35_38_range__40_59_range__61_61_range__63_91_range__93_65535_, tmp);
	}
    protected static final void _init_prod__StringCharacter__char_class___range__10_10_iter_star__char_class___range__9_9_range__32_32_char_class___range__39_39_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[2] = new CharStackNode(4104, 2, new char[][]{{39,39}}, null, null);
      tmp[1] = new ListStackNode(4100, 1, regular__iter_star__char_class___range__9_9_range__32_32, new CharStackNode(4102, 0, new char[][]{{9,9},{32,32}}, null, null), false, null, null);
      tmp[0] = new CharStackNode(4098, 0, new char[][]{{10,10}}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__StringCharacter__char_class___range__10_10_iter_star__char_class___range__9_9_range__32_32_char_class___range__39_39_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__StringCharacter__OctalEscapeSequence_(builder);
      
        _init_prod__StringCharacter__lit___92_char_class___range__34_34_range__39_39_range__60_60_range__62_62_range__92_92_range__98_98_range__102_102_range__110_110_range__114_114_range__116_116_(builder);
      
        _init_prod__StringCharacter__UnicodeEscape_(builder);
      
        _init_prod__StringCharacter__char_class___range__0_33_range__35_38_range__40_59_range__61_61_range__63_91_range__93_65535_(builder);
      
        _init_prod__StringCharacter__char_class___range__10_10_iter_star__char_class___range__9_9_range__32_32_char_class___range__39_39_(builder);
      
    }
  }
	
  protected static class JustTime {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__JustTime__lit___36_84_TimePartNoTZ_opt__TimeZonePart_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[2] = new OptionalStackNode(4110, 2, regular__opt__TimeZonePart, new NonTerminalStackNode(4112, 0, "TimeZonePart", null, null), null, null);
      tmp[1] = new NonTerminalStackNode(4108, 1, "TimePartNoTZ", null, null);
      tmp[0] = new LiteralStackNode(4106, 0, prod__lit___36_84__char_class___range__36_36_char_class___range__84_84_, new char[] {36,84}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__JustTime__lit___36_84_TimePartNoTZ_opt__TimeZonePart_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__JustTime__lit___36_84_TimePartNoTZ_opt__TimeZonePart_(builder);
      
    }
  }
	
  protected static class Range {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__FromTo_Range__start_Char_layouts_LAYOUTLIST_lit___layouts_LAYOUTLIST_end_Char_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(4132, 4, "Char", null, null);
      tmp[3] = new NonTerminalStackNode(4130, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(4128, 2, prod__lit____char_class___range__45_45_, new char[] {45}, null, null);
      tmp[1] = new NonTerminalStackNode(4126, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(4124, 0, "Char", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__FromTo_Range__start_Char_layouts_LAYOUTLIST_lit___layouts_LAYOUTLIST_end_Char_, tmp);
	}
    protected static final void _init_prod__Character_Range__character_Char_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode(4134, 0, "Char", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Character_Range__character_Char_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__FromTo_Range__start_Char_layouts_LAYOUTLIST_lit___layouts_LAYOUTLIST_end_Char_(builder);
      
        _init_prod__Character_Range__character_Char_(builder);
      
    }
  }
	
  protected static class LocationLiteral {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__Default_LocationLiteral__protocolPart_ProtocolPart_layouts_LAYOUTLIST_pathPart_PathPart_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode(4140, 2, "PathPart", null, null);
      tmp[1] = new NonTerminalStackNode(4138, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(4136, 0, "ProtocolPart", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Default_LocationLiteral__protocolPart_ProtocolPart_layouts_LAYOUTLIST_pathPart_PathPart_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__Default_LocationLiteral__protocolPart_ProtocolPart_layouts_LAYOUTLIST_pathPart_PathPart_(builder);
      
    }
  }
	
  protected static class ShellCommand {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__ListDeclarations_ShellCommand__lit_declarations_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(4142, 0, prod__lit_declarations__char_class___range__100_100_char_class___range__101_101_char_class___range__99_99_char_class___range__108_108_char_class___range__97_97_char_class___range__114_114_char_class___range__97_97_char_class___range__116_116_char_class___range__105_105_char_class___range__111_111_char_class___range__110_110_char_class___range__115_115_, new char[] {100,101,99,108,97,114,97,116,105,111,110,115}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__ListDeclarations_ShellCommand__lit_declarations_, tmp);
	}
    protected static final void _init_prod__Test_ShellCommand__lit_test_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(4144, 0, prod__lit_test__char_class___range__116_116_char_class___range__101_101_char_class___range__115_115_char_class___range__116_116_, new char[] {116,101,115,116}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Test_ShellCommand__lit_test_, tmp);
	}
    protected static final void _init_prod__ListModules_ShellCommand__lit_modules_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(4146, 0, prod__lit_modules__char_class___range__109_109_char_class___range__111_111_char_class___range__100_100_char_class___range__117_117_char_class___range__108_108_char_class___range__101_101_char_class___range__115_115_, new char[] {109,111,100,117,108,101,115}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__ListModules_ShellCommand__lit_modules_, tmp);
	}
    protected static final void _init_prod__SetOption_ShellCommand__lit_set_layouts_LAYOUTLIST_name_QualifiedName_layouts_LAYOUTLIST_expression_Expression_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(4156, 4, "Expression", null, null);
      tmp[3] = new NonTerminalStackNode(4154, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(4152, 2, "QualifiedName", null, null);
      tmp[1] = new NonTerminalStackNode(4150, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(4148, 0, prod__lit_set__char_class___range__115_115_char_class___range__101_101_char_class___range__116_116_, new char[] {115,101,116}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__SetOption_ShellCommand__lit_set_layouts_LAYOUTLIST_name_QualifiedName_layouts_LAYOUTLIST_expression_Expression_, tmp);
	}
    protected static final void _init_prod__Edit_ShellCommand__lit_edit_layouts_LAYOUTLIST_name_QualifiedName_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode(4162, 2, "QualifiedName", null, null);
      tmp[1] = new NonTerminalStackNode(4160, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(4158, 0, prod__lit_edit__char_class___range__101_101_char_class___range__100_100_char_class___range__105_105_char_class___range__116_116_, new char[] {101,100,105,116}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Edit_ShellCommand__lit_edit_layouts_LAYOUTLIST_name_QualifiedName_, tmp);
	}
    protected static final void _init_prod__History_ShellCommand__lit_history_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(4164, 0, prod__lit_history__char_class___range__104_104_char_class___range__105_105_char_class___range__115_115_char_class___range__116_116_char_class___range__111_111_char_class___range__114_114_char_class___range__121_121_, new char[] {104,105,115,116,111,114,121}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__History_ShellCommand__lit_history_, tmp);
	}
    protected static final void _init_prod__Quit_ShellCommand__lit_quit_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(4166, 0, prod__lit_quit__char_class___range__113_113_char_class___range__117_117_char_class___range__105_105_char_class___range__116_116_, new char[] {113,117,105,116}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Quit_ShellCommand__lit_quit_, tmp);
	}
    protected static final void _init_prod__Undeclare_ShellCommand__lit_undeclare_layouts_LAYOUTLIST_name_QualifiedName_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode(4172, 2, "QualifiedName", null, null);
      tmp[1] = new NonTerminalStackNode(4170, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(4168, 0, prod__lit_undeclare__char_class___range__117_117_char_class___range__110_110_char_class___range__100_100_char_class___range__101_101_char_class___range__99_99_char_class___range__108_108_char_class___range__97_97_char_class___range__114_114_char_class___range__101_101_, new char[] {117,110,100,101,99,108,97,114,101}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Undeclare_ShellCommand__lit_undeclare_layouts_LAYOUTLIST_name_QualifiedName_, tmp);
	}
    protected static final void _init_prod__Help_ShellCommand__lit_help_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(4174, 0, prod__lit_help__char_class___range__104_104_char_class___range__101_101_char_class___range__108_108_char_class___range__112_112_, new char[] {104,101,108,112}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Help_ShellCommand__lit_help_, tmp);
	}
    protected static final void _init_prod__Unimport_ShellCommand__lit_unimport_layouts_LAYOUTLIST_name_QualifiedName_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode(4180, 2, "QualifiedName", null, null);
      tmp[1] = new NonTerminalStackNode(4178, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(4176, 0, prod__lit_unimport__char_class___range__117_117_char_class___range__110_110_char_class___range__105_105_char_class___range__109_109_char_class___range__112_112_char_class___range__111_111_char_class___range__114_114_char_class___range__116_116_, new char[] {117,110,105,109,112,111,114,116}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Unimport_ShellCommand__lit_unimport_layouts_LAYOUTLIST_name_QualifiedName_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
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
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__Mid_StringMiddle__mid_MidStringChars_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode(4186, 0, "MidStringChars", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Mid_StringMiddle__mid_MidStringChars_, tmp);
	}
    protected static final void _init_prod__Interpolated_StringMiddle__mid_MidStringChars_layouts_LAYOUTLIST_expression_Expression_layouts_LAYOUTLIST_tail_StringMiddle_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(4196, 4, "StringMiddle", null, null);
      tmp[3] = new NonTerminalStackNode(4194, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(4192, 2, "Expression", null, null);
      tmp[1] = new NonTerminalStackNode(4190, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(4188, 0, "MidStringChars", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Interpolated_StringMiddle__mid_MidStringChars_layouts_LAYOUTLIST_expression_Expression_layouts_LAYOUTLIST_tail_StringMiddle_, tmp);
	}
    protected static final void _init_prod__Template_StringMiddle__mid_MidStringChars_layouts_LAYOUTLIST_template_StringTemplate_layouts_LAYOUTLIST_tail_StringMiddle_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(4206, 4, "StringMiddle", null, null);
      tmp[3] = new NonTerminalStackNode(4204, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(4202, 2, "StringTemplate", null, null);
      tmp[1] = new NonTerminalStackNode(4200, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(4198, 0, "MidStringChars", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Template_StringMiddle__mid_MidStringChars_layouts_LAYOUTLIST_template_StringTemplate_layouts_LAYOUTLIST_tail_StringMiddle_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__Mid_StringMiddle__mid_MidStringChars_(builder);
      
        _init_prod__Interpolated_StringMiddle__mid_MidStringChars_layouts_LAYOUTLIST_expression_Expression_layouts_LAYOUTLIST_tail_StringMiddle_(builder);
      
        _init_prod__Template_StringMiddle__mid_MidStringChars_layouts_LAYOUTLIST_template_StringTemplate_layouts_LAYOUTLIST_tail_StringMiddle_(builder);
      
    }
  }
	
  protected static class URLChars {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__URLChars__iter_star__char_class___range__0_8_range__11_12_range__14_31_range__33_59_range__61_123_range__125_65535_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new ListStackNode(4182, 0, regular__iter_star__char_class___range__0_8_range__11_12_range__14_31_range__33_59_range__61_123_range__125_65535, new CharStackNode(4184, 0, new char[][]{{0,8},{11,12},{14,31},{33,59},{61,123},{125,65535}}, null, null), false, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__URLChars__iter_star__char_class___range__0_8_range__11_12_range__14_31_range__33_59_range__61_123_range__125_65535_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__URLChars__iter_star__char_class___range__0_8_range__11_12_range__14_31_range__33_59_range__61_123_range__125_65535_(builder);
      
    }
  }
	
  protected static class QualifiedName {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__Default_QualifiedName__conditional__names_iter_seps__Name__layouts_LAYOUTLIST_lit___58_58_layouts_LAYOUTLIST__not_follow__lit___58_58_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new SeparatedListStackNode(4208, 0, regular__iter_seps__Name__layouts_LAYOUTLIST_lit___58_58_layouts_LAYOUTLIST, new NonTerminalStackNode(4210, 0, "Name", null, null), new AbstractStackNode[]{new NonTerminalStackNode(4212, 1, "layouts_LAYOUTLIST", null, null), new LiteralStackNode(4214, 2, prod__lit___58_58__char_class___range__58_58_char_class___range__58_58_, new char[] {58,58}, null, null), new NonTerminalStackNode(4216, 3, "layouts_LAYOUTLIST", null, null)}, true, null, new ICompletionFilter[] {new StringFollowRestriction(new char[] {58,58})});
      builder.addAlternative(ObjectRascalRascal.prod__Default_QualifiedName__conditional__names_iter_seps__Name__layouts_LAYOUTLIST_lit___58_58_layouts_LAYOUTLIST__not_follow__lit___58_58_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__Default_QualifiedName__conditional__names_iter_seps__Name__layouts_LAYOUTLIST_lit___58_58_layouts_LAYOUTLIST__not_follow__lit___58_58_(builder);
      
    }
  }
	
  protected static class TimeZonePart {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__TimeZonePart__char_class___range__43_43_range__45_45_char_class___range__48_49_char_class___range__48_57_lit___58_char_class___range__48_53_char_class___range__48_57_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[6];
      
      tmp[5] = new CharStackNode(4228, 5, new char[][]{{48,57}}, null, null);
      tmp[4] = new CharStackNode(4226, 4, new char[][]{{48,53}}, null, null);
      tmp[3] = new LiteralStackNode(4224, 3, prod__lit___58__char_class___range__58_58_, new char[] {58}, null, null);
      tmp[2] = new CharStackNode(4222, 2, new char[][]{{48,57}}, null, null);
      tmp[1] = new CharStackNode(4220, 1, new char[][]{{48,49}}, null, null);
      tmp[0] = new CharStackNode(4218, 0, new char[][]{{43,43},{45,45}}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__TimeZonePart__char_class___range__43_43_range__45_45_char_class___range__48_49_char_class___range__48_57_lit___58_char_class___range__48_53_char_class___range__48_57_, tmp);
	}
    protected static final void _init_prod__TimeZonePart__char_class___range__43_43_range__45_45_char_class___range__48_49_char_class___range__48_57_char_class___range__48_53_char_class___range__48_57_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new CharStackNode(4238, 4, new char[][]{{48,57}}, null, null);
      tmp[3] = new CharStackNode(4236, 3, new char[][]{{48,53}}, null, null);
      tmp[2] = new CharStackNode(4234, 2, new char[][]{{48,57}}, null, null);
      tmp[1] = new CharStackNode(4232, 1, new char[][]{{48,49}}, null, null);
      tmp[0] = new CharStackNode(4230, 0, new char[][]{{43,43},{45,45}}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__TimeZonePart__char_class___range__43_43_range__45_45_char_class___range__48_49_char_class___range__48_57_char_class___range__48_53_char_class___range__48_57_, tmp);
	}
    protected static final void _init_prod__TimeZonePart__char_class___range__43_43_range__45_45_char_class___range__48_49_char_class___range__48_57_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[2] = new CharStackNode(4244, 2, new char[][]{{48,57}}, null, null);
      tmp[1] = new CharStackNode(4242, 1, new char[][]{{48,49}}, null, null);
      tmp[0] = new CharStackNode(4240, 0, new char[][]{{43,43},{45,45}}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__TimeZonePart__char_class___range__43_43_range__45_45_char_class___range__48_49_char_class___range__48_57_, tmp);
	}
    protected static final void _init_prod__TimeZonePart__lit_Z_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(4246, 0, prod__lit_Z__char_class___range__90_90_, new char[] {90}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__TimeZonePart__lit_Z_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__TimeZonePart__char_class___range__43_43_range__45_45_char_class___range__48_49_char_class___range__48_57_lit___58_char_class___range__48_53_char_class___range__48_57_(builder);
      
        _init_prod__TimeZonePart__char_class___range__43_43_range__45_45_char_class___range__48_49_char_class___range__48_57_char_class___range__48_53_char_class___range__48_57_(builder);
      
        _init_prod__TimeZonePart__char_class___range__43_43_range__45_45_char_class___range__48_49_char_class___range__48_57_(builder);
      
        _init_prod__TimeZonePart__lit_Z_(builder);
      
    }
  }
	
  protected static class PreStringChars {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__PreStringChars__char_class___range__34_34_iter_star__StringCharacter_char_class___range__60_60__tag__category___67_111_110_115_116_97_110_116(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[2] = new CharStackNode(4262, 2, new char[][]{{60,60}}, null, null);
      tmp[1] = new ListStackNode(4258, 1, regular__iter_star__StringCharacter, new NonTerminalStackNode(4260, 0, "StringCharacter", null, null), false, null, null);
      tmp[0] = new CharStackNode(4256, 0, new char[][]{{34,34}}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__PreStringChars__char_class___range__34_34_iter_star__StringCharacter_char_class___range__60_60__tag__category___67_111_110_115_116_97_110_116, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__PreStringChars__char_class___range__34_34_iter_star__StringCharacter_char_class___range__60_60__tag__category___67_111_110_115_116_97_110_116(builder);
      
    }
  }
	
  protected static class Visit {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__GivenStrategy_Visit__strategy_Strategy_layouts_LAYOUTLIST_lit_visit_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_subject_Expression_layouts_LAYOUTLIST_lit___41_layouts_LAYOUTLIST_lit___123_layouts_LAYOUTLIST_cases_iter_seps__Case__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[15];
      
      tmp[14] = new LiteralStackNode(4336, 14, prod__lit___125__char_class___range__125_125_, new char[] {125}, null, null);
      tmp[13] = new NonTerminalStackNode(4334, 13, "layouts_LAYOUTLIST", null, null);
      tmp[12] = new SeparatedListStackNode(4328, 12, regular__iter_seps__Case__layouts_LAYOUTLIST, new NonTerminalStackNode(4330, 0, "Case", null, null), new AbstractStackNode[]{new NonTerminalStackNode(4332, 1, "layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[11] = new NonTerminalStackNode(4326, 11, "layouts_LAYOUTLIST", null, null);
      tmp[10] = new LiteralStackNode(4324, 10, prod__lit___123__char_class___range__123_123_, new char[] {123}, null, null);
      tmp[9] = new NonTerminalStackNode(4322, 9, "layouts_LAYOUTLIST", null, null);
      tmp[8] = new LiteralStackNode(4320, 8, prod__lit___41__char_class___range__41_41_, new char[] {41}, null, null);
      tmp[7] = new NonTerminalStackNode(4318, 7, "layouts_LAYOUTLIST", null, null);
      tmp[6] = new NonTerminalStackNode(4316, 6, "Expression", null, null);
      tmp[5] = new NonTerminalStackNode(4314, 5, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode(4312, 4, prod__lit___40__char_class___range__40_40_, new char[] {40}, null, null);
      tmp[3] = new NonTerminalStackNode(4310, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(4308, 2, prod__lit_visit__char_class___range__118_118_char_class___range__105_105_char_class___range__115_115_char_class___range__105_105_char_class___range__116_116_, new char[] {118,105,115,105,116}, null, null);
      tmp[1] = new NonTerminalStackNode(4306, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(4304, 0, "Strategy", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__GivenStrategy_Visit__strategy_Strategy_layouts_LAYOUTLIST_lit_visit_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_subject_Expression_layouts_LAYOUTLIST_lit___41_layouts_LAYOUTLIST_lit___123_layouts_LAYOUTLIST_cases_iter_seps__Case__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_, tmp);
	}
    protected static final void _init_prod__DefaultStrategy_Visit__lit_visit_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_subject_Expression_layouts_LAYOUTLIST_lit___41_layouts_LAYOUTLIST_lit___123_layouts_LAYOUTLIST_cases_iter_seps__Case__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[13];
      
      tmp[12] = new LiteralStackNode(4366, 12, prod__lit___125__char_class___range__125_125_, new char[] {125}, null, null);
      tmp[11] = new NonTerminalStackNode(4364, 11, "layouts_LAYOUTLIST", null, null);
      tmp[10] = new SeparatedListStackNode(4358, 10, regular__iter_seps__Case__layouts_LAYOUTLIST, new NonTerminalStackNode(4360, 0, "Case", null, null), new AbstractStackNode[]{new NonTerminalStackNode(4362, 1, "layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[9] = new NonTerminalStackNode(4356, 9, "layouts_LAYOUTLIST", null, null);
      tmp[8] = new LiteralStackNode(4354, 8, prod__lit___123__char_class___range__123_123_, new char[] {123}, null, null);
      tmp[7] = new NonTerminalStackNode(4352, 7, "layouts_LAYOUTLIST", null, null);
      tmp[6] = new LiteralStackNode(4350, 6, prod__lit___41__char_class___range__41_41_, new char[] {41}, null, null);
      tmp[5] = new NonTerminalStackNode(4348, 5, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new NonTerminalStackNode(4346, 4, "Expression", null, null);
      tmp[3] = new NonTerminalStackNode(4344, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(4342, 2, prod__lit___40__char_class___range__40_40_, new char[] {40}, null, null);
      tmp[1] = new NonTerminalStackNode(4340, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(4338, 0, prod__lit_visit__char_class___range__118_118_char_class___range__105_105_char_class___range__115_115_char_class___range__105_105_char_class___range__116_116_, new char[] {118,105,115,105,116}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__DefaultStrategy_Visit__lit_visit_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_subject_Expression_layouts_LAYOUTLIST_lit___41_layouts_LAYOUTLIST_lit___123_layouts_LAYOUTLIST_cases_iter_seps__Case__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__GivenStrategy_Visit__strategy_Strategy_layouts_LAYOUTLIST_lit_visit_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_subject_Expression_layouts_LAYOUTLIST_lit___41_layouts_LAYOUTLIST_lit___123_layouts_LAYOUTLIST_cases_iter_seps__Case__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_(builder);
      
        _init_prod__DefaultStrategy_Visit__lit_visit_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_subject_Expression_layouts_LAYOUTLIST_lit___41_layouts_LAYOUTLIST_lit___123_layouts_LAYOUTLIST_cases_iter_seps__Case__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_(builder);
      
    }
  }
	
  protected static class Command {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__Declaration_Command__declaration_Declaration_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode(4290, 0, "Declaration", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Declaration_Command__declaration_Declaration_, tmp);
	}
    protected static final void _init_prod__Statement_Command__statement_Statement_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode(4292, 0, "Statement", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Statement_Command__statement_Statement_, tmp);
	}
    protected static final void _init_prod__Expression_Command__expression_Expression_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode(4294, 0, "Expression", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Expression_Command__expression_Expression_, tmp);
	}
    protected static final void _init_prod__Import_Command__imported_Import_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode(4296, 0, "Import", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Import_Command__imported_Import_, tmp);
	}
    protected static final void _init_prod__Shell_Command__lit___58_layouts_LAYOUTLIST_command_ShellCommand_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode(4302, 2, "ShellCommand", null, null);
      tmp[1] = new NonTerminalStackNode(4300, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(4298, 0, prod__lit___58__char_class___range__58_58_, new char[] {58}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Shell_Command__lit___58_layouts_LAYOUTLIST_command_ShellCommand_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__Declaration_Command__declaration_Declaration_(builder);
      
        _init_prod__Statement_Command__statement_Statement_(builder);
      
        _init_prod__Expression_Command__expression_Expression_(builder);
      
        _init_prod__Import_Command__imported_Import_(builder);
      
        _init_prod__Shell_Command__lit___58_layouts_LAYOUTLIST_command_ShellCommand_(builder);
      
    }
  }
	
  protected static class ProtocolTail {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__Post_ProtocolTail__post_PostProtocolChars_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode(4378, 0, "PostProtocolChars", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Post_ProtocolTail__post_PostProtocolChars_, tmp);
	}
    protected static final void _init_prod__Mid_ProtocolTail__mid_MidProtocolChars_layouts_LAYOUTLIST_expression_Expression_layouts_LAYOUTLIST_tail_ProtocolTail_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(4388, 4, "ProtocolTail", null, null);
      tmp[3] = new NonTerminalStackNode(4386, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(4384, 2, "Expression", null, null);
      tmp[1] = new NonTerminalStackNode(4382, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(4380, 0, "MidProtocolChars", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Mid_ProtocolTail__mid_MidProtocolChars_layouts_LAYOUTLIST_expression_Expression_layouts_LAYOUTLIST_tail_ProtocolTail_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__Post_ProtocolTail__post_PostProtocolChars_(builder);
      
        _init_prod__Mid_ProtocolTail__mid_MidProtocolChars_layouts_LAYOUTLIST_expression_Expression_layouts_LAYOUTLIST_tail_ProtocolTail_(builder);
      
    }
  }
	
  protected static class NamedBackslash {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__NamedBackslash__conditional__char_class___range__92_92__not_follow__char_class___range__60_60_range__62_62_range__92_92_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new CharStackNode(4424, 0, new char[][]{{92,92}}, null, new ICompletionFilter[] {new CharFollowRestriction(new char[][]{{60,60},{62,62},{92,92}})});
      builder.addAlternative(ObjectRascalRascal.prod__NamedBackslash__conditional__char_class___range__92_92__not_follow__char_class___range__60_60_range__62_62_range__92_92_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__NamedBackslash__conditional__char_class___range__92_92__not_follow__char_class___range__60_60_range__62_62_range__92_92_(builder);
      
    }
  }
	
  protected static class Visibility {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__Private_Visibility__lit_private_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(4426, 0, prod__lit_private__char_class___range__112_112_char_class___range__114_114_char_class___range__105_105_char_class___range__118_118_char_class___range__97_97_char_class___range__116_116_char_class___range__101_101_, new char[] {112,114,105,118,97,116,101}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Private_Visibility__lit_private_, tmp);
	}
    protected static final void _init_prod__Default_Visibility__(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new EpsilonStackNode(4428, 0);
      builder.addAlternative(ObjectRascalRascal.prod__Default_Visibility__, tmp);
	}
    protected static final void _init_prod__Public_Visibility__lit_public_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(4430, 0, prod__lit_public__char_class___range__112_112_char_class___range__117_117_char_class___range__98_98_char_class___range__108_108_char_class___range__105_105_char_class___range__99_99_, new char[] {112,117,98,108,105,99}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Public_Visibility__lit_public_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__Private_Visibility__lit_private_(builder);
      
        _init_prod__Default_Visibility__(builder);
      
        _init_prod__Public_Visibility__lit_public_(builder);
      
    }
  }
	
  protected static class PostPathChars {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__PostPathChars__lit___62_URLChars_lit___124_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[2] = new LiteralStackNode(4436, 2, prod__lit___124__char_class___range__124_124_, new char[] {124}, null, null);
      tmp[1] = new NonTerminalStackNode(4434, 1, "URLChars", null, null);
      tmp[0] = new LiteralStackNode(4432, 0, prod__lit___62__char_class___range__62_62_, new char[] {62}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__PostPathChars__lit___62_URLChars_lit___124_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__PostPathChars__lit___62_URLChars_lit___124_(builder);
      
    }
  }
	
  protected static class StringLiteral {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__Interpolated_StringLiteral__pre_PreStringChars_layouts_LAYOUTLIST_expression_Expression_layouts_LAYOUTLIST_tail_StringTail_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(4446, 4, "StringTail", null, null);
      tmp[3] = new NonTerminalStackNode(4444, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(4442, 2, "Expression", null, null);
      tmp[1] = new NonTerminalStackNode(4440, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(4438, 0, "PreStringChars", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Interpolated_StringLiteral__pre_PreStringChars_layouts_LAYOUTLIST_expression_Expression_layouts_LAYOUTLIST_tail_StringTail_, tmp);
	}
    protected static final void _init_prod__Template_StringLiteral__pre_PreStringChars_layouts_LAYOUTLIST_template_StringTemplate_layouts_LAYOUTLIST_tail_StringTail_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(4456, 4, "StringTail", null, null);
      tmp[3] = new NonTerminalStackNode(4454, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(4452, 2, "StringTemplate", null, null);
      tmp[1] = new NonTerminalStackNode(4450, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(4448, 0, "PreStringChars", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Template_StringLiteral__pre_PreStringChars_layouts_LAYOUTLIST_template_StringTemplate_layouts_LAYOUTLIST_tail_StringTail_, tmp);
	}
    protected static final void _init_prod__NonInterpolated_StringLiteral__constant_StringConstant_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode(4458, 0, "StringConstant", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__NonInterpolated_StringLiteral__constant_StringConstant_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__Interpolated_StringLiteral__pre_PreStringChars_layouts_LAYOUTLIST_expression_Expression_layouts_LAYOUTLIST_tail_StringTail_(builder);
      
        _init_prod__Template_StringLiteral__pre_PreStringChars_layouts_LAYOUTLIST_template_StringTemplate_layouts_LAYOUTLIST_tail_StringTail_(builder);
      
        _init_prod__NonInterpolated_StringLiteral__constant_StringConstant_(builder);
      
    }
  }
	
  protected static class Renamings {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__Default_Renamings__lit_renaming_layouts_LAYOUTLIST_renamings_iter_seps__Renaming__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[2] = new SeparatedListStackNode(4502, 2, regular__iter_seps__Renaming__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST, new NonTerminalStackNode(4504, 0, "Renaming", null, null), new AbstractStackNode[]{new NonTerminalStackNode(4506, 1, "layouts_LAYOUTLIST", null, null), new LiteralStackNode(4508, 2, prod__lit___44__char_class___range__44_44_, new char[] {44}, null, null), new NonTerminalStackNode(4510, 3, "layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[1] = new NonTerminalStackNode(4500, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(4498, 0, prod__lit_renaming__char_class___range__114_114_char_class___range__101_101_char_class___range__110_110_char_class___range__97_97_char_class___range__109_109_char_class___range__105_105_char_class___range__110_110_char_class___range__103_103_, new char[] {114,101,110,97,109,105,110,103}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Default_Renamings__lit_renaming_layouts_LAYOUTLIST_renamings_iter_seps__Renaming__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__Default_Renamings__lit_renaming_layouts_LAYOUTLIST_renamings_iter_seps__Renaming__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_(builder);
      
    }
  }
	
  protected static class layouts_$QUOTES {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__layouts_$QUOTES__conditional__iter_star__char_class___range__9_10_range__13_13_range__32_32__not_follow__char_class___range__9_10_range__13_13_range__32_32_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new ListStackNode(4516, 0, regular__iter_star__char_class___range__9_10_range__13_13_range__32_32, new CharStackNode(4518, 0, new char[][]{{9,10},{13,13},{32,32}}, null, null), false, null, new ICompletionFilter[] {new CharFollowRestriction(new char[][]{{9,10},{13,13},{32,32}})});
      builder.addAlternative(ObjectRascalRascal.prod__layouts_$QUOTES__conditional__iter_star__char_class___range__9_10_range__13_13_range__32_32__not_follow__char_class___range__9_10_range__13_13_range__32_32_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__layouts_$QUOTES__conditional__iter_star__char_class___range__9_10_range__13_13_range__32_32__not_follow__char_class___range__9_10_range__13_13_range__32_32_(builder);
      
    }
  }
	
  protected static class Statement {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__Expression_Statement__expression_Expression_layouts_LAYOUTLIST_lit___59_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[2] = new LiteralStackNode(4558, 2, prod__lit___59__char_class___range__59_59_, new char[] {59}, null, null);
      tmp[1] = new NonTerminalStackNode(4556, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(4554, 0, "Expression", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Expression_Statement__expression_Expression_layouts_LAYOUTLIST_lit___59_, tmp);
	}
    protected static final void _init_prod__Filter_Statement__lit_filter_layouts_LAYOUTLIST_lit___59_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[0] = new LiteralStackNode(4560, 0, prod__lit_filter__char_class___range__102_102_char_class___range__105_105_char_class___range__108_108_char_class___range__116_116_char_class___range__101_101_char_class___range__114_114_, new char[] {102,105,108,116,101,114}, null, null);
      tmp[1] = new NonTerminalStackNode(4562, 1, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(4564, 2, prod__lit___59__char_class___range__59_59_, new char[] {59}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Filter_Statement__lit_filter_layouts_LAYOUTLIST_lit___59_, tmp);
	}
    protected static final void _init_prod__TryFinally_Statement__lit_try_layouts_LAYOUTLIST_body_Statement_layouts_LAYOUTLIST_handlers_iter_seps__Catch__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit_finally_layouts_LAYOUTLIST_finallyBody_Statement_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[9];
      
      tmp[8] = new NonTerminalStackNode(4586, 8, "Statement", null, null);
      tmp[7] = new NonTerminalStackNode(4584, 7, "layouts_LAYOUTLIST", null, null);
      tmp[6] = new LiteralStackNode(4582, 6, prod__lit_finally__char_class___range__102_102_char_class___range__105_105_char_class___range__110_110_char_class___range__97_97_char_class___range__108_108_char_class___range__108_108_char_class___range__121_121_, new char[] {102,105,110,97,108,108,121}, null, null);
      tmp[5] = new NonTerminalStackNode(4580, 5, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new SeparatedListStackNode(4574, 4, regular__iter_seps__Catch__layouts_LAYOUTLIST, new NonTerminalStackNode(4576, 0, "Catch", null, null), new AbstractStackNode[]{new NonTerminalStackNode(4578, 1, "layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[3] = new NonTerminalStackNode(4572, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(4570, 2, "Statement", null, null);
      tmp[1] = new NonTerminalStackNode(4568, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(4566, 0, prod__lit_try__char_class___range__116_116_char_class___range__114_114_char_class___range__121_121_, new char[] {116,114,121}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__TryFinally_Statement__lit_try_layouts_LAYOUTLIST_body_Statement_layouts_LAYOUTLIST_handlers_iter_seps__Catch__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit_finally_layouts_LAYOUTLIST_finallyBody_Statement_, tmp);
	}
    protected static final void _init_prod__Insert_Statement__lit_insert_layouts_LAYOUTLIST_dataTarget_DataTarget_layouts_LAYOUTLIST_statement_Statement__assoc__non_assoc(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(4714, 4, "Statement", null, null);
      tmp[3] = new NonTerminalStackNode(4712, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(4710, 2, "DataTarget", null, null);
      tmp[1] = new NonTerminalStackNode(4708, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(4706, 0, prod__lit_insert__char_class___range__105_105_char_class___range__110_110_char_class___range__115_115_char_class___range__101_101_char_class___range__114_114_char_class___range__116_116_, new char[] {105,110,115,101,114,116}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Insert_Statement__lit_insert_layouts_LAYOUTLIST_dataTarget_DataTarget_layouts_LAYOUTLIST_statement_Statement__assoc__non_assoc, tmp);
	}
    protected static final void _init_prod__Try_Statement__lit_try_layouts_LAYOUTLIST_body_Statement_layouts_LAYOUTLIST_handlers_iter_seps__Catch__layouts_LAYOUTLIST__assoc__non_assoc(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new SeparatedListStackNode(4596, 4, regular__iter_seps__Catch__layouts_LAYOUTLIST, new NonTerminalStackNode(4598, 0, "Catch", null, null), new AbstractStackNode[]{new NonTerminalStackNode(4600, 1, "layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[3] = new NonTerminalStackNode(4594, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(4592, 2, "Statement", null, null);
      tmp[1] = new NonTerminalStackNode(4590, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(4588, 0, prod__lit_try__char_class___range__116_116_char_class___range__114_114_char_class___range__121_121_, new char[] {116,114,121}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Try_Statement__lit_try_layouts_LAYOUTLIST_body_Statement_layouts_LAYOUTLIST_handlers_iter_seps__Catch__layouts_LAYOUTLIST__assoc__non_assoc, tmp);
	}
    protected static final void _init_prod__Visit_Statement__label_Label_layouts_LAYOUTLIST_visit_Visit_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[0] = new NonTerminalStackNode(4604, 0, "Label", null, null);
      tmp[1] = new NonTerminalStackNode(4606, 1, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(4608, 2, "Visit", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Visit_Statement__label_Label_layouts_LAYOUTLIST_visit_Visit_, tmp);
	}
    protected static final void _init_prod__EmptyStatement_Statement__lit___59_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(4602, 0, prod__lit___59__char_class___range__59_59_, new char[] {59}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__EmptyStatement_Statement__lit___59_, tmp);
	}
    protected static final void _init_prod__Break_Statement__lit_break_layouts_LAYOUTLIST_target_Target_layouts_LAYOUTLIST_lit___59_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new LiteralStackNode(4618, 4, prod__lit___59__char_class___range__59_59_, new char[] {59}, null, null);
      tmp[3] = new NonTerminalStackNode(4616, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(4614, 2, "Target", null, null);
      tmp[1] = new NonTerminalStackNode(4612, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(4610, 0, prod__lit_break__char_class___range__98_98_char_class___range__114_114_char_class___range__101_101_char_class___range__97_97_char_class___range__107_107_, new char[] {98,114,101,97,107}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Break_Statement__lit_break_layouts_LAYOUTLIST_target_Target_layouts_LAYOUTLIST_lit___59_, tmp);
	}
    protected static final void _init_prod__Assignment_Statement__assignable_Assignable_layouts_LAYOUTLIST_operator_Assignment_layouts_LAYOUTLIST_statement_Statement_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(4628, 4, "Statement", null, null);
      tmp[3] = new NonTerminalStackNode(4626, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(4624, 2, "Assignment", null, null);
      tmp[1] = new NonTerminalStackNode(4622, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(4620, 0, "Assignable", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Assignment_Statement__assignable_Assignable_layouts_LAYOUTLIST_operator_Assignment_layouts_LAYOUTLIST_statement_Statement_, tmp);
	}
    protected static final void _init_prod__For_Statement__label_Label_layouts_LAYOUTLIST_lit_for_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_generators_iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_layouts_LAYOUTLIST_body_Statement_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[11];
      
      tmp[10] = new NonTerminalStackNode(4658, 10, "Statement", null, null);
      tmp[9] = new NonTerminalStackNode(4656, 9, "layouts_LAYOUTLIST", null, null);
      tmp[8] = new LiteralStackNode(4654, 8, prod__lit___41__char_class___range__41_41_, new char[] {41}, null, null);
      tmp[7] = new NonTerminalStackNode(4652, 7, "layouts_LAYOUTLIST", null, null);
      tmp[6] = new SeparatedListStackNode(4642, 6, regular__iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST, new NonTerminalStackNode(4644, 0, "Expression", null, null), new AbstractStackNode[]{new NonTerminalStackNode(4646, 1, "layouts_LAYOUTLIST", null, null), new LiteralStackNode(4648, 2, prod__lit___44__char_class___range__44_44_, new char[] {44}, null, null), new NonTerminalStackNode(4650, 3, "layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[5] = new NonTerminalStackNode(4640, 5, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode(4638, 4, prod__lit___40__char_class___range__40_40_, new char[] {40}, null, null);
      tmp[3] = new NonTerminalStackNode(4636, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(4634, 2, prod__lit_for__char_class___range__102_102_char_class___range__111_111_char_class___range__114_114_, new char[] {102,111,114}, null, null);
      tmp[1] = new NonTerminalStackNode(4632, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(4630, 0, "Label", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__For_Statement__label_Label_layouts_LAYOUTLIST_lit_for_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_generators_iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_layouts_LAYOUTLIST_body_Statement_, tmp);
	}
    protected static final void _init_prod__GlobalDirective_Statement__lit_global_layouts_LAYOUTLIST_type_Type_layouts_LAYOUTLIST_names_iter_seps__QualifiedName__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___59_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[7];
      
      tmp[6] = new LiteralStackNode(4680, 6, prod__lit___59__char_class___range__59_59_, new char[] {59}, null, null);
      tmp[5] = new NonTerminalStackNode(4678, 5, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new SeparatedListStackNode(4668, 4, regular__iter_seps__QualifiedName__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST, new NonTerminalStackNode(4670, 0, "QualifiedName", null, null), new AbstractStackNode[]{new NonTerminalStackNode(4672, 1, "layouts_LAYOUTLIST", null, null), new LiteralStackNode(4674, 2, prod__lit___44__char_class___range__44_44_, new char[] {44}, null, null), new NonTerminalStackNode(4676, 3, "layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[3] = new NonTerminalStackNode(4666, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(4664, 2, "Type", null, null);
      tmp[1] = new NonTerminalStackNode(4662, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(4660, 0, prod__lit_global__char_class___range__103_103_char_class___range__108_108_char_class___range__111_111_char_class___range__98_98_char_class___range__97_97_char_class___range__108_108_, new char[] {103,108,111,98,97,108}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__GlobalDirective_Statement__lit_global_layouts_LAYOUTLIST_type_Type_layouts_LAYOUTLIST_names_iter_seps__QualifiedName__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___59_, tmp);
	}
    protected static final void _init_prod__AssertWithMessage_Statement__lit_assert_layouts_LAYOUTLIST_expression_Expression_layouts_LAYOUTLIST_lit___58_layouts_LAYOUTLIST_message_Expression_layouts_LAYOUTLIST_lit___59_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[9];
      
      tmp[8] = new LiteralStackNode(4698, 8, prod__lit___59__char_class___range__59_59_, new char[] {59}, null, null);
      tmp[7] = new NonTerminalStackNode(4696, 7, "layouts_LAYOUTLIST", null, null);
      tmp[6] = new NonTerminalStackNode(4694, 6, "Expression", null, null);
      tmp[5] = new NonTerminalStackNode(4692, 5, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode(4690, 4, prod__lit___58__char_class___range__58_58_, new char[] {58}, null, null);
      tmp[3] = new NonTerminalStackNode(4688, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(4686, 2, "Expression", null, null);
      tmp[1] = new NonTerminalStackNode(4684, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(4682, 0, prod__lit_assert__char_class___range__97_97_char_class___range__115_115_char_class___range__115_115_char_class___range__101_101_char_class___range__114_114_char_class___range__116_116_, new char[] {97,115,115,101,114,116}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__AssertWithMessage_Statement__lit_assert_layouts_LAYOUTLIST_expression_Expression_layouts_LAYOUTLIST_lit___58_layouts_LAYOUTLIST_message_Expression_layouts_LAYOUTLIST_lit___59_, tmp);
	}
    protected static final void _init_prod__While_Statement__label_Label_layouts_LAYOUTLIST_lit_while_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_conditions_iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_layouts_LAYOUTLIST_body_Statement_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[11];
      
      tmp[10] = new NonTerminalStackNode(4760, 10, "Statement", null, null);
      tmp[9] = new NonTerminalStackNode(4758, 9, "layouts_LAYOUTLIST", null, null);
      tmp[8] = new LiteralStackNode(4756, 8, prod__lit___41__char_class___range__41_41_, new char[] {41}, null, null);
      tmp[7] = new NonTerminalStackNode(4754, 7, "layouts_LAYOUTLIST", null, null);
      tmp[6] = new SeparatedListStackNode(4744, 6, regular__iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST, new NonTerminalStackNode(4746, 0, "Expression", null, null), new AbstractStackNode[]{new NonTerminalStackNode(4748, 1, "layouts_LAYOUTLIST", null, null), new LiteralStackNode(4750, 2, prod__lit___44__char_class___range__44_44_, new char[] {44}, null, null), new NonTerminalStackNode(4752, 3, "layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[5] = new NonTerminalStackNode(4742, 5, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode(4740, 4, prod__lit___40__char_class___range__40_40_, new char[] {40}, null, null);
      tmp[3] = new NonTerminalStackNode(4738, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(4736, 2, prod__lit_while__char_class___range__119_119_char_class___range__104_104_char_class___range__105_105_char_class___range__108_108_char_class___range__101_101_, new char[] {119,104,105,108,101}, null, null);
      tmp[1] = new NonTerminalStackNode(4734, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(4732, 0, "Label", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__While_Statement__label_Label_layouts_LAYOUTLIST_lit_while_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_conditions_iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_layouts_LAYOUTLIST_body_Statement_, tmp);
	}
    protected static final void _init_prod__Assert_Statement__lit_assert_layouts_LAYOUTLIST_expression_Expression_layouts_LAYOUTLIST_lit___59_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new LiteralStackNode(4770, 4, prod__lit___59__char_class___range__59_59_, new char[] {59}, null, null);
      tmp[3] = new NonTerminalStackNode(4768, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(4766, 2, "Expression", null, null);
      tmp[1] = new NonTerminalStackNode(4764, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(4762, 0, prod__lit_assert__char_class___range__97_97_char_class___range__115_115_char_class___range__115_115_char_class___range__101_101_char_class___range__114_114_char_class___range__116_116_, new char[] {97,115,115,101,114,116}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Assert_Statement__lit_assert_layouts_LAYOUTLIST_expression_Expression_layouts_LAYOUTLIST_lit___59_, tmp);
	}
    protected static final void _init_prod__FunctionDeclaration_Statement__functionDeclaration_FunctionDeclaration_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode(4976, 0, "FunctionDeclaration", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__FunctionDeclaration_Statement__functionDeclaration_FunctionDeclaration_, tmp);
	}
    protected static final void _init_prod__Return_Statement__lit_return_layouts_LAYOUTLIST_statement_Statement__assoc__non_assoc(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode(4704, 2, "Statement", null, null);
      tmp[1] = new NonTerminalStackNode(4702, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(4700, 0, prod__lit_return__char_class___range__114_114_char_class___range__101_101_char_class___range__116_116_char_class___range__117_117_char_class___range__114_114_char_class___range__110_110_, new char[] {114,101,116,117,114,110}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Return_Statement__lit_return_layouts_LAYOUTLIST_statement_Statement__assoc__non_assoc, tmp);
	}
    protected static final void _init_prod__Continue_Statement__lit_continue_layouts_LAYOUTLIST_target_Target_layouts_LAYOUTLIST_lit___59_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new LiteralStackNode(4780, 4, prod__lit___59__char_class___range__59_59_, new char[] {59}, null, null);
      tmp[3] = new NonTerminalStackNode(4778, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(4776, 2, "Target", null, null);
      tmp[1] = new NonTerminalStackNode(4774, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(4772, 0, prod__lit_continue__char_class___range__99_99_char_class___range__111_111_char_class___range__110_110_char_class___range__116_116_char_class___range__105_105_char_class___range__110_110_char_class___range__117_117_char_class___range__101_101_, new char[] {99,111,110,116,105,110,117,101}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Continue_Statement__lit_continue_layouts_LAYOUTLIST_target_Target_layouts_LAYOUTLIST_lit___59_, tmp);
	}
    protected static final void _init_prod__Fail_Statement__lit_fail_layouts_LAYOUTLIST_target_Target_layouts_LAYOUTLIST_lit___59_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new LiteralStackNode(4820, 4, prod__lit___59__char_class___range__59_59_, new char[] {59}, null, null);
      tmp[3] = new NonTerminalStackNode(4818, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(4816, 2, "Target", null, null);
      tmp[1] = new NonTerminalStackNode(4814, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(4812, 0, prod__lit_fail__char_class___range__102_102_char_class___range__97_97_char_class___range__105_105_char_class___range__108_108_, new char[] {102,97,105,108}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Fail_Statement__lit_fail_layouts_LAYOUTLIST_target_Target_layouts_LAYOUTLIST_lit___59_, tmp);
	}
    protected static final void _init_prod__Append_Statement__lit_append_layouts_LAYOUTLIST_dataTarget_DataTarget_layouts_LAYOUTLIST_statement_Statement__assoc__non_assoc(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(4724, 4, "Statement", null, null);
      tmp[3] = new NonTerminalStackNode(4722, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(4720, 2, "DataTarget", null, null);
      tmp[1] = new NonTerminalStackNode(4718, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(4716, 0, prod__lit_append__char_class___range__97_97_char_class___range__112_112_char_class___range__112_112_char_class___range__101_101_char_class___range__110_110_char_class___range__100_100_, new char[] {97,112,112,101,110,100}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Append_Statement__lit_append_layouts_LAYOUTLIST_dataTarget_DataTarget_layouts_LAYOUTLIST_statement_Statement__assoc__non_assoc, tmp);
	}
    protected static final void _init_prod__IfThen_Statement__label_Label_layouts_LAYOUTLIST_lit_if_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_conditions_iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_layouts_LAYOUTLIST_thenStatement_Statement_layouts_LAYOUTLIST_conditional__empty__not_follow__lit_else_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[13];
      
      tmp[12] = new EmptyStackNode(4888, 12, regular__empty, null, new ICompletionFilter[] {new StringFollowRestriction(new char[] {101,108,115,101})});
      tmp[11] = new NonTerminalStackNode(4886, 11, "layouts_LAYOUTLIST", null, null);
      tmp[10] = new NonTerminalStackNode(4884, 10, "Statement", null, null);
      tmp[9] = new NonTerminalStackNode(4882, 9, "layouts_LAYOUTLIST", null, null);
      tmp[8] = new LiteralStackNode(4880, 8, prod__lit___41__char_class___range__41_41_, new char[] {41}, null, null);
      tmp[7] = new NonTerminalStackNode(4878, 7, "layouts_LAYOUTLIST", null, null);
      tmp[6] = new SeparatedListStackNode(4868, 6, regular__iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST, new NonTerminalStackNode(4870, 0, "Expression", null, null), new AbstractStackNode[]{new NonTerminalStackNode(4872, 1, "layouts_LAYOUTLIST", null, null), new LiteralStackNode(4874, 2, prod__lit___44__char_class___range__44_44_, new char[] {44}, null, null), new NonTerminalStackNode(4876, 3, "layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[5] = new NonTerminalStackNode(4866, 5, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode(4864, 4, prod__lit___40__char_class___range__40_40_, new char[] {40}, null, null);
      tmp[3] = new NonTerminalStackNode(4862, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(4860, 2, prod__lit_if__char_class___range__105_105_char_class___range__102_102_, new char[] {105,102}, null, null);
      tmp[1] = new NonTerminalStackNode(4858, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(4856, 0, "Label", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__IfThen_Statement__label_Label_layouts_LAYOUTLIST_lit_if_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_conditions_iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_layouts_LAYOUTLIST_thenStatement_Statement_layouts_LAYOUTLIST_conditional__empty__not_follow__lit_else_, tmp);
	}
    protected static final void _init_prod__Switch_Statement__label_Label_layouts_LAYOUTLIST_lit_switch_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_expression_Expression_layouts_LAYOUTLIST_lit___41_layouts_LAYOUTLIST_lit___123_layouts_LAYOUTLIST_cases_iter_seps__Case__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[15];
      
      tmp[14] = new LiteralStackNode(4854, 14, prod__lit___125__char_class___range__125_125_, new char[] {125}, null, null);
      tmp[13] = new NonTerminalStackNode(4852, 13, "layouts_LAYOUTLIST", null, null);
      tmp[12] = new SeparatedListStackNode(4846, 12, regular__iter_seps__Case__layouts_LAYOUTLIST, new NonTerminalStackNode(4848, 0, "Case", null, null), new AbstractStackNode[]{new NonTerminalStackNode(4850, 1, "layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[11] = new NonTerminalStackNode(4844, 11, "layouts_LAYOUTLIST", null, null);
      tmp[10] = new LiteralStackNode(4842, 10, prod__lit___123__char_class___range__123_123_, new char[] {123}, null, null);
      tmp[9] = new NonTerminalStackNode(4840, 9, "layouts_LAYOUTLIST", null, null);
      tmp[8] = new LiteralStackNode(4838, 8, prod__lit___41__char_class___range__41_41_, new char[] {41}, null, null);
      tmp[7] = new NonTerminalStackNode(4836, 7, "layouts_LAYOUTLIST", null, null);
      tmp[6] = new NonTerminalStackNode(4834, 6, "Expression", null, null);
      tmp[5] = new NonTerminalStackNode(4832, 5, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode(4830, 4, prod__lit___40__char_class___range__40_40_, new char[] {40}, null, null);
      tmp[3] = new NonTerminalStackNode(4828, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(4826, 2, prod__lit_switch__char_class___range__115_115_char_class___range__119_119_char_class___range__105_105_char_class___range__116_116_char_class___range__99_99_char_class___range__104_104_, new char[] {115,119,105,116,99,104}, null, null);
      tmp[1] = new NonTerminalStackNode(4824, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(4822, 0, "Label", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Switch_Statement__label_Label_layouts_LAYOUTLIST_lit_switch_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_expression_Expression_layouts_LAYOUTLIST_lit___41_layouts_LAYOUTLIST_lit___123_layouts_LAYOUTLIST_cases_iter_seps__Case__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_, tmp);
	}
    protected static final void _init_prod__Solve_Statement__lit_solve_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_variables_iter_seps__QualifiedName__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_bound_Bound_layouts_LAYOUTLIST_lit___41_layouts_LAYOUTLIST_body_Statement_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[11];
      
      tmp[0] = new LiteralStackNode(4782, 0, prod__lit_solve__char_class___range__115_115_char_class___range__111_111_char_class___range__108_108_char_class___range__118_118_char_class___range__101_101_, new char[] {115,111,108,118,101}, null, null);
      tmp[1] = new NonTerminalStackNode(4784, 1, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(4786, 2, prod__lit___40__char_class___range__40_40_, new char[] {40}, null, null);
      tmp[3] = new NonTerminalStackNode(4788, 3, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new SeparatedListStackNode(4790, 4, regular__iter_seps__QualifiedName__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST, new NonTerminalStackNode(4792, 0, "QualifiedName", null, null), new AbstractStackNode[]{new NonTerminalStackNode(4794, 1, "layouts_LAYOUTLIST", null, null), new LiteralStackNode(4796, 2, prod__lit___44__char_class___range__44_44_, new char[] {44}, null, null), new NonTerminalStackNode(4798, 3, "layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[5] = new NonTerminalStackNode(4800, 5, "layouts_LAYOUTLIST", null, null);
      tmp[6] = new NonTerminalStackNode(4802, 6, "Bound", null, null);
      tmp[7] = new NonTerminalStackNode(4804, 7, "layouts_LAYOUTLIST", null, null);
      tmp[8] = new LiteralStackNode(4806, 8, prod__lit___41__char_class___range__41_41_, new char[] {41}, null, null);
      tmp[9] = new NonTerminalStackNode(4808, 9, "layouts_LAYOUTLIST", null, null);
      tmp[10] = new NonTerminalStackNode(4810, 10, "Statement", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Solve_Statement__lit_solve_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_variables_iter_seps__QualifiedName__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_bound_Bound_layouts_LAYOUTLIST_lit___41_layouts_LAYOUTLIST_body_Statement_, tmp);
	}
    protected static final void _init_prod__VariableDeclaration_Statement__declaration_LocalVariableDeclaration_layouts_LAYOUTLIST_lit___59_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[2] = new LiteralStackNode(4982, 2, prod__lit___59__char_class___range__59_59_, new char[] {59}, null, null);
      tmp[1] = new NonTerminalStackNode(4980, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(4978, 0, "LocalVariableDeclaration", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__VariableDeclaration_Statement__declaration_LocalVariableDeclaration_layouts_LAYOUTLIST_lit___59_, tmp);
	}
    protected static final void _init_prod__NonEmptyBlock_Statement__label_Label_layouts_LAYOUTLIST_lit___123_layouts_LAYOUTLIST_statements_iter_seps__Statement__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[7];
      
      tmp[6] = new LiteralStackNode(4906, 6, prod__lit___125__char_class___range__125_125_, new char[] {125}, null, null);
      tmp[5] = new NonTerminalStackNode(4904, 5, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new SeparatedListStackNode(4898, 4, regular__iter_seps__Statement__layouts_LAYOUTLIST, new NonTerminalStackNode(4900, 0, "Statement", null, null), new AbstractStackNode[]{new NonTerminalStackNode(4902, 1, "layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[3] = new NonTerminalStackNode(4896, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(4894, 2, prod__lit___123__char_class___range__123_123_, new char[] {123}, null, null);
      tmp[1] = new NonTerminalStackNode(4892, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(4890, 0, "Label", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__NonEmptyBlock_Statement__label_Label_layouts_LAYOUTLIST_lit___123_layouts_LAYOUTLIST_statements_iter_seps__Statement__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_, tmp);
	}
    protected static final void _init_prod__DoWhile_Statement__label_Label_layouts_LAYOUTLIST_lit_do_layouts_LAYOUTLIST_body_Statement_layouts_LAYOUTLIST_lit_while_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_condition_Expression_layouts_LAYOUTLIST_lit___41_layouts_LAYOUTLIST_lit___59_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[15];
      
      tmp[14] = new LiteralStackNode(4936, 14, prod__lit___59__char_class___range__59_59_, new char[] {59}, null, null);
      tmp[13] = new NonTerminalStackNode(4934, 13, "layouts_LAYOUTLIST", null, null);
      tmp[12] = new LiteralStackNode(4932, 12, prod__lit___41__char_class___range__41_41_, new char[] {41}, null, null);
      tmp[11] = new NonTerminalStackNode(4930, 11, "layouts_LAYOUTLIST", null, null);
      tmp[10] = new NonTerminalStackNode(4928, 10, "Expression", null, null);
      tmp[9] = new NonTerminalStackNode(4926, 9, "layouts_LAYOUTLIST", null, null);
      tmp[8] = new LiteralStackNode(4924, 8, prod__lit___40__char_class___range__40_40_, new char[] {40}, null, null);
      tmp[7] = new NonTerminalStackNode(4922, 7, "layouts_LAYOUTLIST", null, null);
      tmp[6] = new LiteralStackNode(4920, 6, prod__lit_while__char_class___range__119_119_char_class___range__104_104_char_class___range__105_105_char_class___range__108_108_char_class___range__101_101_, new char[] {119,104,105,108,101}, null, null);
      tmp[5] = new NonTerminalStackNode(4918, 5, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new NonTerminalStackNode(4916, 4, "Statement", null, null);
      tmp[3] = new NonTerminalStackNode(4914, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(4912, 2, prod__lit_do__char_class___range__100_100_char_class___range__111_111_, new char[] {100,111}, null, null);
      tmp[1] = new NonTerminalStackNode(4910, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(4908, 0, "Label", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__DoWhile_Statement__label_Label_layouts_LAYOUTLIST_lit_do_layouts_LAYOUTLIST_body_Statement_layouts_LAYOUTLIST_lit_while_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_condition_Expression_layouts_LAYOUTLIST_lit___41_layouts_LAYOUTLIST_lit___59_, tmp);
	}
    protected static final void _init_prod__IfThenElse_Statement__label_Label_layouts_LAYOUTLIST_lit_if_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_conditions_iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_layouts_LAYOUTLIST_thenStatement_Statement_layouts_LAYOUTLIST_lit_else_layouts_LAYOUTLIST_elseStatement_Statement_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[15];
      
      tmp[14] = new NonTerminalStackNode(4974, 14, "Statement", null, null);
      tmp[13] = new NonTerminalStackNode(4972, 13, "layouts_LAYOUTLIST", null, null);
      tmp[12] = new LiteralStackNode(4970, 12, prod__lit_else__char_class___range__101_101_char_class___range__108_108_char_class___range__115_115_char_class___range__101_101_, new char[] {101,108,115,101}, null, null);
      tmp[11] = new NonTerminalStackNode(4968, 11, "layouts_LAYOUTLIST", null, null);
      tmp[10] = new NonTerminalStackNode(4966, 10, "Statement", null, null);
      tmp[9] = new NonTerminalStackNode(4964, 9, "layouts_LAYOUTLIST", null, null);
      tmp[8] = new LiteralStackNode(4962, 8, prod__lit___41__char_class___range__41_41_, new char[] {41}, null, null);
      tmp[7] = new NonTerminalStackNode(4960, 7, "layouts_LAYOUTLIST", null, null);
      tmp[6] = new SeparatedListStackNode(4950, 6, regular__iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST, new NonTerminalStackNode(4952, 0, "Expression", null, null), new AbstractStackNode[]{new NonTerminalStackNode(4954, 1, "layouts_LAYOUTLIST", null, null), new LiteralStackNode(4956, 2, prod__lit___44__char_class___range__44_44_, new char[] {44}, null, null), new NonTerminalStackNode(4958, 3, "layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[5] = new NonTerminalStackNode(4948, 5, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode(4946, 4, prod__lit___40__char_class___range__40_40_, new char[] {40}, null, null);
      tmp[3] = new NonTerminalStackNode(4944, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(4942, 2, prod__lit_if__char_class___range__105_105_char_class___range__102_102_, new char[] {105,102}, null, null);
      tmp[1] = new NonTerminalStackNode(4940, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(4938, 0, "Label", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__IfThenElse_Statement__label_Label_layouts_LAYOUTLIST_lit_if_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_conditions_iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_layouts_LAYOUTLIST_thenStatement_Statement_layouts_LAYOUTLIST_lit_else_layouts_LAYOUTLIST_elseStatement_Statement_, tmp);
	}
    protected static final void _init_prod__Throw_Statement__lit_throw_layouts_LAYOUTLIST_statement_Statement__assoc__non_assoc(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode(4730, 2, "Statement", null, null);
      tmp[1] = new NonTerminalStackNode(4728, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(4726, 0, prod__lit_throw__char_class___range__116_116_char_class___range__104_104_char_class___range__114_114_char_class___range__111_111_char_class___range__119_119_, new char[] {116,104,114,111,119}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Throw_Statement__lit_throw_layouts_LAYOUTLIST_statement_Statement__assoc__non_assoc, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
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
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__TypeArguments_FunctionType__type_Type_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_arguments_iter_star_seps__TypeArg__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[7];
      
      tmp[6] = new LiteralStackNode(5066, 6, prod__lit___41__char_class___range__41_41_, new char[] {41}, null, null);
      tmp[5] = new NonTerminalStackNode(5064, 5, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new SeparatedListStackNode(5054, 4, regular__iter_star_seps__TypeArg__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST, new NonTerminalStackNode(5056, 0, "TypeArg", null, null), new AbstractStackNode[]{new NonTerminalStackNode(5058, 1, "layouts_LAYOUTLIST", null, null), new LiteralStackNode(5060, 2, prod__lit___44__char_class___range__44_44_, new char[] {44}, null, null), new NonTerminalStackNode(5062, 3, "layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[3] = new NonTerminalStackNode(5052, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(5050, 2, prod__lit___40__char_class___range__40_40_, new char[] {40}, null, null);
      tmp[1] = new NonTerminalStackNode(5048, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(5046, 0, "Type", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__TypeArguments_FunctionType__type_Type_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_arguments_iter_star_seps__TypeArg__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__TypeArguments_FunctionType__type_Type_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_arguments_iter_star_seps__TypeArg__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_(builder);
      
    }
  }
	
  protected static class Case {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__PatternWithAction_Case__lit_case_layouts_LAYOUTLIST_patternWithAction_PatternWithAction__tag__Foldable(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode(5090, 2, "PatternWithAction", null, null);
      tmp[1] = new NonTerminalStackNode(5088, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(5086, 0, prod__lit_case__char_class___range__99_99_char_class___range__97_97_char_class___range__115_115_char_class___range__101_101_, new char[] {99,97,115,101}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__PatternWithAction_Case__lit_case_layouts_LAYOUTLIST_patternWithAction_PatternWithAction__tag__Foldable, tmp);
	}
    protected static final void _init_prod__Default_Case__lit_default_layouts_LAYOUTLIST_lit___58_layouts_LAYOUTLIST_statement_Statement__tag__Foldable(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(5100, 4, "Statement", null, null);
      tmp[3] = new NonTerminalStackNode(5098, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(5096, 2, prod__lit___58__char_class___range__58_58_, new char[] {58}, null, null);
      tmp[1] = new NonTerminalStackNode(5094, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(5092, 0, prod__lit_default__char_class___range__100_100_char_class___range__101_101_char_class___range__102_102_char_class___range__97_97_char_class___range__117_117_char_class___range__108_108_char_class___range__116_116_, new char[] {100,101,102,97,117,108,116}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Default_Case__lit_default_layouts_LAYOUTLIST_lit___58_layouts_LAYOUTLIST_statement_Statement__tag__Foldable, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__PatternWithAction_Case__lit_case_layouts_LAYOUTLIST_patternWithAction_PatternWithAction__tag__Foldable(builder);
      
        _init_prod__Default_Case__lit_default_layouts_LAYOUTLIST_lit___58_layouts_LAYOUTLIST_statement_Statement__tag__Foldable(builder);
      
    }
  }
	
  protected static class Bound {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__Empty_Bound__(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new EpsilonStackNode(5102, 0);
      builder.addAlternative(ObjectRascalRascal.prod__Empty_Bound__, tmp);
	}
    protected static final void _init_prod__Default_Bound__lit___59_layouts_LAYOUTLIST_expression_Expression_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode(5108, 2, "Expression", null, null);
      tmp[1] = new NonTerminalStackNode(5106, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(5104, 0, prod__lit___59__char_class___range__59_59_, new char[] {59}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Default_Bound__lit___59_layouts_LAYOUTLIST_expression_Expression_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__Empty_Bound__(builder);
      
        _init_prod__Default_Bound__lit___59_layouts_LAYOUTLIST_expression_Expression_(builder);
      
    }
  }
	
  protected static class Type {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__Function_Type__function_FunctionType_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode(5310, 0, "FunctionType", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Function_Type__function_FunctionType_, tmp);
	}
    protected static final void _init_prod__Bracket_Type__lit___40_layouts_LAYOUTLIST_type_Type_layouts_LAYOUTLIST_lit___41__bracket(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new LiteralStackNode(5320, 4, prod__lit___41__char_class___range__41_41_, new char[] {41}, null, null);
      tmp[3] = new NonTerminalStackNode(5318, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(5316, 2, "Type", null, null);
      tmp[1] = new NonTerminalStackNode(5314, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(5312, 0, prod__lit___40__char_class___range__40_40_, new char[] {40}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Bracket_Type__lit___40_layouts_LAYOUTLIST_type_Type_layouts_LAYOUTLIST_lit___41__bracket, tmp);
	}
    protected static final void _init_prod__Basic_Type__basic_BasicType_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode(5322, 0, "BasicType", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Basic_Type__basic_BasicType_, tmp);
	}
    protected static final void _init_prod__Symbol_Type__symbol_Sym_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode(5324, 0, "Sym", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Symbol_Type__symbol_Sym_, tmp);
	}
    protected static final void _init_prod__Variable_Type__typeVar_TypeVar_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode(5326, 0, "TypeVar", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Variable_Type__typeVar_TypeVar_, tmp);
	}
    protected static final void _init_prod__User_Type__conditional__user_UserType__delete__HeaderKeyword_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode(5328, 0, "UserType", null, new ICompletionFilter[] {new StringMatchRestriction(new char[] {108,101,120,105,99,97,108}), new StringMatchRestriction(new char[] {105,109,112,111,114,116}), new StringMatchRestriction(new char[] {115,116,97,114,116}), new StringMatchRestriction(new char[] {115,121,110,116,97,120}), new StringMatchRestriction(new char[] {107,101,121,119,111,114,100}), new StringMatchRestriction(new char[] {108,97,121,111,117,116}), new StringMatchRestriction(new char[] {101,120,116,101,110,100})});
      builder.addAlternative(ObjectRascalRascal.prod__User_Type__conditional__user_UserType__delete__HeaderKeyword_, tmp);
	}
    protected static final void _init_prod__Selector_Type__selector_DataTypeSelector_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode(5330, 0, "DataTypeSelector", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Selector_Type__selector_DataTypeSelector_, tmp);
	}
    protected static final void _init_prod__Structured_Type__structured_StructuredType_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode(5332, 0, "StructuredType", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Structured_Type__structured_StructuredType_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
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
	
  protected static class Declaration {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__Rule_Declaration__tags_Tags_layouts_LAYOUTLIST_lit_rule_layouts_LAYOUTLIST_name_Name_layouts_LAYOUTLIST_patternAction_PatternWithAction_layouts_LAYOUTLIST_lit___59_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[9];
      
      tmp[8] = new LiteralStackNode(5134, 8, prod__lit___59__char_class___range__59_59_, new char[] {59}, null, null);
      tmp[7] = new NonTerminalStackNode(5132, 7, "layouts_LAYOUTLIST", null, null);
      tmp[6] = new NonTerminalStackNode(5130, 6, "PatternWithAction", null, null);
      tmp[5] = new NonTerminalStackNode(5128, 5, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new NonTerminalStackNode(5126, 4, "Name", null, null);
      tmp[3] = new NonTerminalStackNode(5124, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(5122, 2, prod__lit_rule__char_class___range__114_114_char_class___range__117_117_char_class___range__108_108_char_class___range__101_101_, new char[] {114,117,108,101}, null, null);
      tmp[1] = new NonTerminalStackNode(5120, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(5118, 0, "Tags", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Rule_Declaration__tags_Tags_layouts_LAYOUTLIST_lit_rule_layouts_LAYOUTLIST_name_Name_layouts_LAYOUTLIST_patternAction_PatternWithAction_layouts_LAYOUTLIST_lit___59_, tmp);
	}
    protected static final void _init_prod__Alias_Declaration__tags_Tags_layouts_LAYOUTLIST_visibility_Visibility_layouts_LAYOUTLIST_lit_alias_layouts_LAYOUTLIST_user_UserType_layouts_LAYOUTLIST_lit___61_layouts_LAYOUTLIST_base_Type_layouts_LAYOUTLIST_lit___59_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[13];
      
      tmp[12] = new LiteralStackNode(5160, 12, prod__lit___59__char_class___range__59_59_, new char[] {59}, null, null);
      tmp[11] = new NonTerminalStackNode(5158, 11, "layouts_LAYOUTLIST", null, null);
      tmp[10] = new NonTerminalStackNode(5156, 10, "Type", null, null);
      tmp[9] = new NonTerminalStackNode(5154, 9, "layouts_LAYOUTLIST", null, null);
      tmp[8] = new LiteralStackNode(5152, 8, prod__lit___61__char_class___range__61_61_, new char[] {61}, null, null);
      tmp[7] = new NonTerminalStackNode(5150, 7, "layouts_LAYOUTLIST", null, null);
      tmp[6] = new NonTerminalStackNode(5148, 6, "UserType", null, null);
      tmp[5] = new NonTerminalStackNode(5146, 5, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode(5144, 4, prod__lit_alias__char_class___range__97_97_char_class___range__108_108_char_class___range__105_105_char_class___range__97_97_char_class___range__115_115_, new char[] {97,108,105,97,115}, null, null);
      tmp[3] = new NonTerminalStackNode(5142, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(5140, 2, "Visibility", null, null);
      tmp[1] = new NonTerminalStackNode(5138, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(5136, 0, "Tags", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Alias_Declaration__tags_Tags_layouts_LAYOUTLIST_visibility_Visibility_layouts_LAYOUTLIST_lit_alias_layouts_LAYOUTLIST_user_UserType_layouts_LAYOUTLIST_lit___61_layouts_LAYOUTLIST_base_Type_layouts_LAYOUTLIST_lit___59_, tmp);
	}
    protected static final void _init_prod__DataAbstract_Declaration__tags_Tags_layouts_LAYOUTLIST_visibility_Visibility_layouts_LAYOUTLIST_lit_data_layouts_LAYOUTLIST_user_UserType_layouts_LAYOUTLIST_lit___59_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[9];
      
      tmp[8] = new LiteralStackNode(5178, 8, prod__lit___59__char_class___range__59_59_, new char[] {59}, null, null);
      tmp[7] = new NonTerminalStackNode(5176, 7, "layouts_LAYOUTLIST", null, null);
      tmp[6] = new NonTerminalStackNode(5174, 6, "UserType", null, null);
      tmp[5] = new NonTerminalStackNode(5172, 5, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode(5170, 4, prod__lit_data__char_class___range__100_100_char_class___range__97_97_char_class___range__116_116_char_class___range__97_97_, new char[] {100,97,116,97}, null, null);
      tmp[3] = new NonTerminalStackNode(5168, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(5166, 2, "Visibility", null, null);
      tmp[1] = new NonTerminalStackNode(5164, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(5162, 0, "Tags", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__DataAbstract_Declaration__tags_Tags_layouts_LAYOUTLIST_visibility_Visibility_layouts_LAYOUTLIST_lit_data_layouts_LAYOUTLIST_user_UserType_layouts_LAYOUTLIST_lit___59_, tmp);
	}
    protected static final void _init_prod__Variable_Declaration__tags_Tags_layouts_LAYOUTLIST_visibility_Visibility_layouts_LAYOUTLIST_type_Type_layouts_LAYOUTLIST_variables_iter_seps__Variable__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___59_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[9];
      
      tmp[8] = new LiteralStackNode(5204, 8, prod__lit___59__char_class___range__59_59_, new char[] {59}, null, null);
      tmp[7] = new NonTerminalStackNode(5202, 7, "layouts_LAYOUTLIST", null, null);
      tmp[6] = new SeparatedListStackNode(5192, 6, regular__iter_seps__Variable__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST, new NonTerminalStackNode(5194, 0, "Variable", null, null), new AbstractStackNode[]{new NonTerminalStackNode(5196, 1, "layouts_LAYOUTLIST", null, null), new LiteralStackNode(5198, 2, prod__lit___44__char_class___range__44_44_, new char[] {44}, null, null), new NonTerminalStackNode(5200, 3, "layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[5] = new NonTerminalStackNode(5190, 5, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new NonTerminalStackNode(5188, 4, "Type", null, null);
      tmp[3] = new NonTerminalStackNode(5186, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(5184, 2, "Visibility", null, null);
      tmp[1] = new NonTerminalStackNode(5182, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(5180, 0, "Tags", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Variable_Declaration__tags_Tags_layouts_LAYOUTLIST_visibility_Visibility_layouts_LAYOUTLIST_type_Type_layouts_LAYOUTLIST_variables_iter_seps__Variable__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___59_, tmp);
	}
    protected static final void _init_prod__Annotation_Declaration__tags_Tags_layouts_LAYOUTLIST_visibility_Visibility_layouts_LAYOUTLIST_lit_anno_layouts_LAYOUTLIST_annoType_Type_layouts_LAYOUTLIST_onType_Type_layouts_LAYOUTLIST_lit___64_layouts_LAYOUTLIST_name_Name_layouts_LAYOUTLIST_lit___59_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[15];
      
      tmp[14] = new LiteralStackNode(5234, 14, prod__lit___59__char_class___range__59_59_, new char[] {59}, null, null);
      tmp[13] = new NonTerminalStackNode(5232, 13, "layouts_LAYOUTLIST", null, null);
      tmp[12] = new NonTerminalStackNode(5230, 12, "Name", null, null);
      tmp[11] = new NonTerminalStackNode(5228, 11, "layouts_LAYOUTLIST", null, null);
      tmp[10] = new LiteralStackNode(5226, 10, prod__lit___64__char_class___range__64_64_, new char[] {64}, null, null);
      tmp[9] = new NonTerminalStackNode(5224, 9, "layouts_LAYOUTLIST", null, null);
      tmp[8] = new NonTerminalStackNode(5222, 8, "Type", null, null);
      tmp[7] = new NonTerminalStackNode(5220, 7, "layouts_LAYOUTLIST", null, null);
      tmp[6] = new NonTerminalStackNode(5218, 6, "Type", null, null);
      tmp[5] = new NonTerminalStackNode(5216, 5, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode(5214, 4, prod__lit_anno__char_class___range__97_97_char_class___range__110_110_char_class___range__110_110_char_class___range__111_111_, new char[] {97,110,110,111}, null, null);
      tmp[3] = new NonTerminalStackNode(5212, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(5210, 2, "Visibility", null, null);
      tmp[1] = new NonTerminalStackNode(5208, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(5206, 0, "Tags", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Annotation_Declaration__tags_Tags_layouts_LAYOUTLIST_visibility_Visibility_layouts_LAYOUTLIST_lit_anno_layouts_LAYOUTLIST_annoType_Type_layouts_LAYOUTLIST_onType_Type_layouts_LAYOUTLIST_lit___64_layouts_LAYOUTLIST_name_Name_layouts_LAYOUTLIST_lit___59_, tmp);
	}
    protected static final void _init_prod__Function_Declaration__functionDeclaration_FunctionDeclaration_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode(5236, 0, "FunctionDeclaration", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Function_Declaration__functionDeclaration_FunctionDeclaration_, tmp);
	}
    protected static final void _init_prod__Data_Declaration__tags_Tags_layouts_LAYOUTLIST_visibility_Visibility_layouts_LAYOUTLIST_lit_data_layouts_LAYOUTLIST_user_UserType_layouts_LAYOUTLIST_lit___61_layouts_LAYOUTLIST_variants_iter_seps__Variant__layouts_LAYOUTLIST_lit___124_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___59__tag__Foldable(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[13];
      
      tmp[12] = new LiteralStackNode(5270, 12, prod__lit___59__char_class___range__59_59_, new char[] {59}, null, null);
      tmp[11] = new NonTerminalStackNode(5268, 11, "layouts_LAYOUTLIST", null, null);
      tmp[10] = new SeparatedListStackNode(5258, 10, regular__iter_seps__Variant__layouts_LAYOUTLIST_lit___124_layouts_LAYOUTLIST, new NonTerminalStackNode(5260, 0, "Variant", null, null), new AbstractStackNode[]{new NonTerminalStackNode(5262, 1, "layouts_LAYOUTLIST", null, null), new LiteralStackNode(5264, 2, prod__lit___124__char_class___range__124_124_, new char[] {124}, null, null), new NonTerminalStackNode(5266, 3, "layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[9] = new NonTerminalStackNode(5256, 9, "layouts_LAYOUTLIST", null, null);
      tmp[8] = new LiteralStackNode(5254, 8, prod__lit___61__char_class___range__61_61_, new char[] {61}, null, null);
      tmp[7] = new NonTerminalStackNode(5252, 7, "layouts_LAYOUTLIST", null, null);
      tmp[6] = new NonTerminalStackNode(5250, 6, "UserType", null, null);
      tmp[5] = new NonTerminalStackNode(5248, 5, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode(5246, 4, prod__lit_data__char_class___range__100_100_char_class___range__97_97_char_class___range__116_116_char_class___range__97_97_, new char[] {100,97,116,97}, null, null);
      tmp[3] = new NonTerminalStackNode(5244, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(5242, 2, "Visibility", null, null);
      tmp[1] = new NonTerminalStackNode(5240, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(5238, 0, "Tags", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Data_Declaration__tags_Tags_layouts_LAYOUTLIST_visibility_Visibility_layouts_LAYOUTLIST_lit_data_layouts_LAYOUTLIST_user_UserType_layouts_LAYOUTLIST_lit___61_layouts_LAYOUTLIST_variants_iter_seps__Variant__layouts_LAYOUTLIST_lit___124_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___59__tag__Foldable, tmp);
	}
    protected static final void _init_prod__Tag_Declaration__tags_Tags_layouts_LAYOUTLIST_visibility_Visibility_layouts_LAYOUTLIST_lit_tag_layouts_LAYOUTLIST_kind_Kind_layouts_LAYOUTLIST_name_Name_layouts_LAYOUTLIST_lit_on_layouts_LAYOUTLIST_types_iter_seps__Type__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___59_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[15];
      
      tmp[14] = new LiteralStackNode(5308, 14, prod__lit___59__char_class___range__59_59_, new char[] {59}, null, null);
      tmp[13] = new NonTerminalStackNode(5306, 13, "layouts_LAYOUTLIST", null, null);
      tmp[12] = new SeparatedListStackNode(5296, 12, regular__iter_seps__Type__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST, new NonTerminalStackNode(5298, 0, "Type", null, null), new AbstractStackNode[]{new NonTerminalStackNode(5300, 1, "layouts_LAYOUTLIST", null, null), new LiteralStackNode(5302, 2, prod__lit___44__char_class___range__44_44_, new char[] {44}, null, null), new NonTerminalStackNode(5304, 3, "layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[11] = new NonTerminalStackNode(5294, 11, "layouts_LAYOUTLIST", null, null);
      tmp[10] = new LiteralStackNode(5292, 10, prod__lit_on__char_class___range__111_111_char_class___range__110_110_, new char[] {111,110}, null, null);
      tmp[9] = new NonTerminalStackNode(5290, 9, "layouts_LAYOUTLIST", null, null);
      tmp[8] = new NonTerminalStackNode(5288, 8, "Name", null, null);
      tmp[7] = new NonTerminalStackNode(5286, 7, "layouts_LAYOUTLIST", null, null);
      tmp[6] = new NonTerminalStackNode(5284, 6, "Kind", null, null);
      tmp[5] = new NonTerminalStackNode(5282, 5, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode(5280, 4, prod__lit_tag__char_class___range__116_116_char_class___range__97_97_char_class___range__103_103_, new char[] {116,97,103}, null, null);
      tmp[3] = new NonTerminalStackNode(5278, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(5276, 2, "Visibility", null, null);
      tmp[1] = new NonTerminalStackNode(5274, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(5272, 0, "Tags", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Tag_Declaration__tags_Tags_layouts_LAYOUTLIST_visibility_Visibility_layouts_LAYOUTLIST_lit_tag_layouts_LAYOUTLIST_kind_Kind_layouts_LAYOUTLIST_name_Name_layouts_LAYOUTLIST_lit_on_layouts_LAYOUTLIST_types_iter_seps__Type__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___59_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__Rule_Declaration__tags_Tags_layouts_LAYOUTLIST_lit_rule_layouts_LAYOUTLIST_name_Name_layouts_LAYOUTLIST_patternAction_PatternWithAction_layouts_LAYOUTLIST_lit___59_(builder);
      
        _init_prod__Alias_Declaration__tags_Tags_layouts_LAYOUTLIST_visibility_Visibility_layouts_LAYOUTLIST_lit_alias_layouts_LAYOUTLIST_user_UserType_layouts_LAYOUTLIST_lit___61_layouts_LAYOUTLIST_base_Type_layouts_LAYOUTLIST_lit___59_(builder);
      
        _init_prod__DataAbstract_Declaration__tags_Tags_layouts_LAYOUTLIST_visibility_Visibility_layouts_LAYOUTLIST_lit_data_layouts_LAYOUTLIST_user_UserType_layouts_LAYOUTLIST_lit___59_(builder);
      
        _init_prod__Variable_Declaration__tags_Tags_layouts_LAYOUTLIST_visibility_Visibility_layouts_LAYOUTLIST_type_Type_layouts_LAYOUTLIST_variables_iter_seps__Variable__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___59_(builder);
      
        _init_prod__Annotation_Declaration__tags_Tags_layouts_LAYOUTLIST_visibility_Visibility_layouts_LAYOUTLIST_lit_anno_layouts_LAYOUTLIST_annoType_Type_layouts_LAYOUTLIST_onType_Type_layouts_LAYOUTLIST_lit___64_layouts_LAYOUTLIST_name_Name_layouts_LAYOUTLIST_lit___59_(builder);
      
        _init_prod__Function_Declaration__functionDeclaration_FunctionDeclaration_(builder);
      
        _init_prod__Data_Declaration__tags_Tags_layouts_LAYOUTLIST_visibility_Visibility_layouts_LAYOUTLIST_lit_data_layouts_LAYOUTLIST_user_UserType_layouts_LAYOUTLIST_lit___61_layouts_LAYOUTLIST_variants_iter_seps__Variant__layouts_LAYOUTLIST_lit___124_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___59__tag__Foldable(builder);
      
        _init_prod__Tag_Declaration__tags_Tags_layouts_LAYOUTLIST_visibility_Visibility_layouts_LAYOUTLIST_lit_tag_layouts_LAYOUTLIST_kind_Kind_layouts_LAYOUTLIST_name_Name_layouts_LAYOUTLIST_lit_on_layouts_LAYOUTLIST_types_iter_seps__Type__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___59_(builder);
      
    }
  }
	
  protected static class Class {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__Complement_Class__lit___33_layouts_LAYOUTLIST_charClass_Class_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode(5366, 2, "Class", null, null);
      tmp[1] = new NonTerminalStackNode(5364, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(5362, 0, prod__lit___33__char_class___range__33_33_, new char[] {33}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Complement_Class__lit___33_layouts_LAYOUTLIST_charClass_Class_, tmp);
	}
    protected static final void _init_prod__Bracket_Class__lit___40_layouts_LAYOUTLIST_charclass_Class_layouts_LAYOUTLIST_lit___41__bracket(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new LiteralStackNode(5410, 4, prod__lit___41__char_class___range__41_41_, new char[] {41}, null, null);
      tmp[3] = new NonTerminalStackNode(5408, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(5406, 2, "Class", null, null);
      tmp[1] = new NonTerminalStackNode(5404, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(5402, 0, prod__lit___40__char_class___range__40_40_, new char[] {40}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Bracket_Class__lit___40_layouts_LAYOUTLIST_charclass_Class_layouts_LAYOUTLIST_lit___41__bracket, tmp);
	}
    protected static final void _init_prod__Intersection_Class__lhs_Class_layouts_LAYOUTLIST_lit___38_38_layouts_LAYOUTLIST_rhs_Class__assoc__left(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(5400, 4, "Class", null, null);
      tmp[3] = new NonTerminalStackNode(5398, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(5396, 2, prod__lit___38_38__char_class___range__38_38_char_class___range__38_38_, new char[] {38,38}, null, null);
      tmp[1] = new NonTerminalStackNode(5394, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(5392, 0, "Class", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Intersection_Class__lhs_Class_layouts_LAYOUTLIST_lit___38_38_layouts_LAYOUTLIST_rhs_Class__assoc__left, tmp);
	}
    protected static final void _init_prod__Difference_Class__lhs_Class_layouts_LAYOUTLIST_lit___layouts_LAYOUTLIST_rhs_Class__assoc__left(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(5390, 4, "Class", null, null);
      tmp[3] = new NonTerminalStackNode(5388, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(5386, 2, prod__lit____char_class___range__45_45_, new char[] {45}, null, null);
      tmp[1] = new NonTerminalStackNode(5384, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(5382, 0, "Class", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Difference_Class__lhs_Class_layouts_LAYOUTLIST_lit___layouts_LAYOUTLIST_rhs_Class__assoc__left, tmp);
	}
    protected static final void _init_prod__SimpleCharclass_Class__lit___91_layouts_LAYOUTLIST_ranges_iter_star_seps__Range__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___93_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new LiteralStackNode(5380, 4, prod__lit___93__char_class___range__93_93_, new char[] {93}, null, null);
      tmp[3] = new NonTerminalStackNode(5378, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new SeparatedListStackNode(5372, 2, regular__iter_star_seps__Range__layouts_LAYOUTLIST, new NonTerminalStackNode(5374, 0, "Range", null, null), new AbstractStackNode[]{new NonTerminalStackNode(5376, 1, "layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[1] = new NonTerminalStackNode(5370, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(5368, 0, prod__lit___91__char_class___range__91_91_, new char[] {91}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__SimpleCharclass_Class__lit___91_layouts_LAYOUTLIST_ranges_iter_star_seps__Range__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___93_, tmp);
	}
    protected static final void _init_prod__Union_Class__lhs_Class_layouts_LAYOUTLIST_lit___124_124_layouts_LAYOUTLIST_rhs_Class__assoc__left(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(5420, 4, "Class", null, null);
      tmp[3] = new NonTerminalStackNode(5418, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(5416, 2, prod__lit___124_124__char_class___range__124_124_char_class___range__124_124_, new char[] {124,124}, null, null);
      tmp[1] = new NonTerminalStackNode(5414, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(5412, 0, "Class", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Union_Class__lhs_Class_layouts_LAYOUTLIST_lit___124_124_layouts_LAYOUTLIST_rhs_Class__assoc__left, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__Complement_Class__lit___33_layouts_LAYOUTLIST_charClass_Class_(builder);
      
        _init_prod__Bracket_Class__lit___40_layouts_LAYOUTLIST_charclass_Class_layouts_LAYOUTLIST_lit___41__bracket(builder);
      
        _init_prod__Intersection_Class__lhs_Class_layouts_LAYOUTLIST_lit___38_38_layouts_LAYOUTLIST_rhs_Class__assoc__left(builder);
      
        _init_prod__Difference_Class__lhs_Class_layouts_LAYOUTLIST_lit___layouts_LAYOUTLIST_rhs_Class__assoc__left(builder);
      
        _init_prod__SimpleCharclass_Class__lit___91_layouts_LAYOUTLIST_ranges_iter_star_seps__Range__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___93_(builder);
      
        _init_prod__Union_Class__lhs_Class_layouts_LAYOUTLIST_lit___124_124_layouts_LAYOUTLIST_rhs_Class__assoc__left(builder);
      
    }
  }
	
  protected static class Mapping__Expression {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__Default_Mapping__Expression__from_Expression_layouts_LAYOUTLIST_lit___58_layouts_LAYOUTLIST_to_Expression_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(5434, 4, "Expression", null, null);
      tmp[3] = new NonTerminalStackNode(5432, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(5430, 2, prod__lit___58__char_class___range__58_58_, new char[] {58}, null, null);
      tmp[1] = new NonTerminalStackNode(5428, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(5426, 0, "Expression", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Default_Mapping__Expression__from_Expression_layouts_LAYOUTLIST_lit___58_layouts_LAYOUTLIST_to_Expression_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__Default_Mapping__Expression__from_Expression_layouts_LAYOUTLIST_lit___58_layouts_LAYOUTLIST_to_Expression_(builder);
      
    }
  }
	
  protected static class Comprehension {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__List_Comprehension__lit___91_layouts_LAYOUTLIST_results_iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___124_layouts_LAYOUTLIST_generators_iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___93_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[9];
      
      tmp[8] = new LiteralStackNode(5478, 8, prod__lit___93__char_class___range__93_93_, new char[] {93}, null, null);
      tmp[7] = new NonTerminalStackNode(5476, 7, "layouts_LAYOUTLIST", null, null);
      tmp[6] = new SeparatedListStackNode(5466, 6, regular__iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST, new NonTerminalStackNode(5468, 0, "Expression", null, null), new AbstractStackNode[]{new NonTerminalStackNode(5470, 1, "layouts_LAYOUTLIST", null, null), new LiteralStackNode(5472, 2, prod__lit___44__char_class___range__44_44_, new char[] {44}, null, null), new NonTerminalStackNode(5474, 3, "layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[5] = new NonTerminalStackNode(5464, 5, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode(5462, 4, prod__lit___124__char_class___range__124_124_, new char[] {124}, null, null);
      tmp[3] = new NonTerminalStackNode(5460, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new SeparatedListStackNode(5450, 2, regular__iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST, new NonTerminalStackNode(5452, 0, "Expression", null, null), new AbstractStackNode[]{new NonTerminalStackNode(5454, 1, "layouts_LAYOUTLIST", null, null), new LiteralStackNode(5456, 2, prod__lit___44__char_class___range__44_44_, new char[] {44}, null, null), new NonTerminalStackNode(5458, 3, "layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[1] = new NonTerminalStackNode(5448, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(5446, 0, prod__lit___91__char_class___range__91_91_, new char[] {91}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__List_Comprehension__lit___91_layouts_LAYOUTLIST_results_iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___124_layouts_LAYOUTLIST_generators_iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___93_, tmp);
	}
    protected static final void _init_prod__Map_Comprehension__lit___40_layouts_LAYOUTLIST_from_Expression_layouts_LAYOUTLIST_lit___58_layouts_LAYOUTLIST_to_Expression_layouts_LAYOUTLIST_lit___124_layouts_LAYOUTLIST_generators_iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[13];
      
      tmp[12] = new LiteralStackNode(5512, 12, prod__lit___41__char_class___range__41_41_, new char[] {41}, null, null);
      tmp[11] = new NonTerminalStackNode(5510, 11, "layouts_LAYOUTLIST", null, null);
      tmp[10] = new SeparatedListStackNode(5500, 10, regular__iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST, new NonTerminalStackNode(5502, 0, "Expression", null, null), new AbstractStackNode[]{new NonTerminalStackNode(5504, 1, "layouts_LAYOUTLIST", null, null), new LiteralStackNode(5506, 2, prod__lit___44__char_class___range__44_44_, new char[] {44}, null, null), new NonTerminalStackNode(5508, 3, "layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[9] = new NonTerminalStackNode(5498, 9, "layouts_LAYOUTLIST", null, null);
      tmp[8] = new LiteralStackNode(5496, 8, prod__lit___124__char_class___range__124_124_, new char[] {124}, null, null);
      tmp[7] = new NonTerminalStackNode(5494, 7, "layouts_LAYOUTLIST", null, null);
      tmp[6] = new NonTerminalStackNode(5492, 6, "Expression", null, null);
      tmp[5] = new NonTerminalStackNode(5490, 5, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode(5488, 4, prod__lit___58__char_class___range__58_58_, new char[] {58}, null, null);
      tmp[3] = new NonTerminalStackNode(5486, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(5484, 2, "Expression", null, null);
      tmp[1] = new NonTerminalStackNode(5482, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(5480, 0, prod__lit___40__char_class___range__40_40_, new char[] {40}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Map_Comprehension__lit___40_layouts_LAYOUTLIST_from_Expression_layouts_LAYOUTLIST_lit___58_layouts_LAYOUTLIST_to_Expression_layouts_LAYOUTLIST_lit___124_layouts_LAYOUTLIST_generators_iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_, tmp);
	}
    protected static final void _init_prod__Set_Comprehension__lit___123_layouts_LAYOUTLIST_results_iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___124_layouts_LAYOUTLIST_generators_iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[9];
      
      tmp[8] = new LiteralStackNode(5546, 8, prod__lit___125__char_class___range__125_125_, new char[] {125}, null, null);
      tmp[7] = new NonTerminalStackNode(5544, 7, "layouts_LAYOUTLIST", null, null);
      tmp[6] = new SeparatedListStackNode(5534, 6, regular__iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST, new NonTerminalStackNode(5536, 0, "Expression", null, null), new AbstractStackNode[]{new NonTerminalStackNode(5538, 1, "layouts_LAYOUTLIST", null, null), new LiteralStackNode(5540, 2, prod__lit___44__char_class___range__44_44_, new char[] {44}, null, null), new NonTerminalStackNode(5542, 3, "layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[5] = new NonTerminalStackNode(5532, 5, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode(5530, 4, prod__lit___124__char_class___range__124_124_, new char[] {124}, null, null);
      tmp[3] = new NonTerminalStackNode(5528, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new SeparatedListStackNode(5518, 2, regular__iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST, new NonTerminalStackNode(5520, 0, "Expression", null, null), new AbstractStackNode[]{new NonTerminalStackNode(5522, 1, "layouts_LAYOUTLIST", null, null), new LiteralStackNode(5524, 2, prod__lit___44__char_class___range__44_44_, new char[] {44}, null, null), new NonTerminalStackNode(5526, 3, "layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[1] = new NonTerminalStackNode(5516, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(5514, 0, prod__lit___123__char_class___range__123_123_, new char[] {123}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Set_Comprehension__lit___123_layouts_LAYOUTLIST_results_iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___124_layouts_LAYOUTLIST_generators_iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__List_Comprehension__lit___91_layouts_LAYOUTLIST_results_iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___124_layouts_LAYOUTLIST_generators_iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___93_(builder);
      
        _init_prod__Map_Comprehension__lit___40_layouts_LAYOUTLIST_from_Expression_layouts_LAYOUTLIST_lit___58_layouts_LAYOUTLIST_to_Expression_layouts_LAYOUTLIST_lit___124_layouts_LAYOUTLIST_generators_iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_(builder);
      
        _init_prod__Set_Comprehension__lit___123_layouts_LAYOUTLIST_results_iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___124_layouts_LAYOUTLIST_generators_iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_(builder);
      
    }
  }
	
  protected static class FunctionModifiers {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__List_FunctionModifiers__modifiers_iter_star_seps__FunctionModifier__layouts_LAYOUTLIST_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new SeparatedListStackNode(5440, 0, regular__iter_star_seps__FunctionModifier__layouts_LAYOUTLIST, new NonTerminalStackNode(5442, 0, "FunctionModifier", null, null), new AbstractStackNode[]{new NonTerminalStackNode(5444, 1, "layouts_LAYOUTLIST", null, null)}, false, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__List_FunctionModifiers__modifiers_iter_star_seps__FunctionModifier__layouts_LAYOUTLIST_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__List_FunctionModifiers__modifiers_iter_star_seps__FunctionModifier__layouts_LAYOUTLIST_(builder);
      
    }
  }
	
  protected static class ProdModifier {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__Associativity_ProdModifier__associativity_Assoc_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode(5548, 0, "Assoc", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Associativity_ProdModifier__associativity_Assoc_, tmp);
	}
    protected static final void _init_prod__Tag_ProdModifier__tag_Tag_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode(5550, 0, "Tag", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Tag_ProdModifier__tag_Tag_, tmp);
	}
    protected static final void _init_prod__Bracket_ProdModifier__lit_bracket_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(5552, 0, prod__lit_bracket__char_class___range__98_98_char_class___range__114_114_char_class___range__97_97_char_class___range__99_99_char_class___range__107_107_char_class___range__101_101_char_class___range__116_116_, new char[] {98,114,97,99,107,101,116}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Bracket_ProdModifier__lit_bracket_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__Associativity_ProdModifier__associativity_Assoc_(builder);
      
        _init_prod__Tag_ProdModifier__tag_Tag_(builder);
      
        _init_prod__Bracket_ProdModifier__lit_bracket_(builder);
      
    }
  }
	
  protected static class BooleanLiteral {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__BooleanLiteral__lit_true_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(5562, 0, prod__lit_true__char_class___range__116_116_char_class___range__114_114_char_class___range__117_117_char_class___range__101_101_, new char[] {116,114,117,101}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__BooleanLiteral__lit_true_, tmp);
	}
    protected static final void _init_prod__BooleanLiteral__lit_false_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(5564, 0, prod__lit_false__char_class___range__102_102_char_class___range__97_97_char_class___range__108_108_char_class___range__115_115_char_class___range__101_101_, new char[] {102,97,108,115,101}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__BooleanLiteral__lit_false_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__BooleanLiteral__lit_true_(builder);
      
        _init_prod__BooleanLiteral__lit_false_(builder);
      
    }
  }
	
  protected static class Toplevel {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__GivenVisibility_Toplevel__declaration_Declaration_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode(5560, 0, "Declaration", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__GivenVisibility_Toplevel__declaration_Declaration_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__GivenVisibility_Toplevel__declaration_Declaration_(builder);
      
    }
  }
	
  protected static class TypeVar {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__Free_TypeVar__lit___38_layouts_LAYOUTLIST_name_Name_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode(5572, 2, "Name", null, null);
      tmp[1] = new NonTerminalStackNode(5570, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(5568, 0, prod__lit___38__char_class___range__38_38_, new char[] {38}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Free_TypeVar__lit___38_layouts_LAYOUTLIST_name_Name_, tmp);
	}
    protected static final void _init_prod__Bounded_TypeVar__lit___38_layouts_LAYOUTLIST_name_Name_layouts_LAYOUTLIST_lit___60_58_layouts_LAYOUTLIST_bound_Type_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[7];
      
      tmp[6] = new NonTerminalStackNode(5586, 6, "Type", null, null);
      tmp[5] = new NonTerminalStackNode(5584, 5, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode(5582, 4, prod__lit___60_58__char_class___range__60_60_char_class___range__58_58_, new char[] {60,58}, null, null);
      tmp[3] = new NonTerminalStackNode(5580, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(5578, 2, "Name", null, null);
      tmp[1] = new NonTerminalStackNode(5576, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(5574, 0, prod__lit___38__char_class___range__38_38_, new char[] {38}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Bounded_TypeVar__lit___38_layouts_LAYOUTLIST_name_Name_layouts_LAYOUTLIST_lit___60_58_layouts_LAYOUTLIST_bound_Type_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__Free_TypeVar__lit___38_layouts_LAYOUTLIST_name_Name_(builder);
      
        _init_prod__Bounded_TypeVar__lit___38_layouts_LAYOUTLIST_name_Name_layouts_LAYOUTLIST_lit___60_58_layouts_LAYOUTLIST_bound_Type_(builder);
      
    }
  }
	
  protected static class RationalLiteral {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__RationalLiteral__char_class___range__48_48_char_class___range__114_114_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[2];
      
      tmp[1] = new CharStackNode(5610, 1, new char[][]{{114,114}}, null, null);
      tmp[0] = new CharStackNode(5608, 0, new char[][]{{48,48}}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RationalLiteral__char_class___range__48_48_char_class___range__114_114_, tmp);
	}
    protected static final void _init_prod__RationalLiteral__char_class___range__49_57_iter_star__char_class___range__48_57_char_class___range__114_114_char_class___range__49_57_conditional__iter_star__char_class___range__48_57__not_follow__char_class___range__48_57_range__65_90_range__95_95_range__97_122_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new ListStackNode(5622, 4, regular__iter_star__char_class___range__48_57, new CharStackNode(5624, 0, new char[][]{{48,57}}, null, null), false, null, new ICompletionFilter[] {new CharFollowRestriction(new char[][]{{48,57},{65,90},{95,95},{97,122}})});
      tmp[3] = new CharStackNode(5620, 3, new char[][]{{49,57}}, null, null);
      tmp[2] = new CharStackNode(5618, 2, new char[][]{{114,114}}, null, null);
      tmp[1] = new ListStackNode(5614, 1, regular__iter_star__char_class___range__48_57, new CharStackNode(5616, 0, new char[][]{{48,57}}, null, null), false, null, null);
      tmp[0] = new CharStackNode(5612, 0, new char[][]{{49,57}}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RationalLiteral__char_class___range__49_57_iter_star__char_class___range__48_57_char_class___range__114_114_char_class___range__49_57_conditional__iter_star__char_class___range__48_57__not_follow__char_class___range__48_57_range__65_90_range__95_95_range__97_122_, tmp);
	}
    protected static final void _init_prod__RationalLiteral__char_class___range__49_57_iter_star__char_class___range__48_57_char_class___range__114_114_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[2] = new CharStackNode(5632, 2, new char[][]{{114,114}}, null, null);
      tmp[1] = new ListStackNode(5628, 1, regular__iter_star__char_class___range__48_57, new CharStackNode(5630, 0, new char[][]{{48,57}}, null, null), false, null, null);
      tmp[0] = new CharStackNode(5626, 0, new char[][]{{49,57}}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RationalLiteral__char_class___range__49_57_iter_star__char_class___range__48_57_char_class___range__114_114_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__RationalLiteral__char_class___range__48_48_char_class___range__114_114_(builder);
      
        _init_prod__RationalLiteral__char_class___range__49_57_iter_star__char_class___range__48_57_char_class___range__114_114_char_class___range__49_57_conditional__iter_star__char_class___range__48_57__not_follow__char_class___range__48_57_range__65_90_range__95_95_range__97_122_(builder);
      
        _init_prod__RationalLiteral__char_class___range__49_57_iter_star__char_class___range__48_57_char_class___range__114_114_(builder);
      
    }
  }
	
  protected static class UnicodeEscape {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__UnicodeEscape__lit___92_iter__char_class___range__117_117_char_class___range__48_57_range__65_70_range__97_102_char_class___range__48_57_range__65_70_range__97_102_char_class___range__48_57_range__65_70_range__97_102_char_class___range__48_57_range__65_70_range__97_102_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[6];
      
      tmp[5] = new CharStackNode(5646, 5, new char[][]{{48,57},{65,70},{97,102}}, null, null);
      tmp[4] = new CharStackNode(5644, 4, new char[][]{{48,57},{65,70},{97,102}}, null, null);
      tmp[3] = new CharStackNode(5642, 3, new char[][]{{48,57},{65,70},{97,102}}, null, null);
      tmp[2] = new CharStackNode(5640, 2, new char[][]{{48,57},{65,70},{97,102}}, null, null);
      tmp[1] = new ListStackNode(5636, 1, regular__iter__char_class___range__117_117, new CharStackNode(5638, 0, new char[][]{{117,117}}, null, null), true, null, null);
      tmp[0] = new LiteralStackNode(5634, 0, prod__lit___92__char_class___range__92_92_, new char[] {92}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__UnicodeEscape__lit___92_iter__char_class___range__117_117_char_class___range__48_57_range__65_70_range__97_102_char_class___range__48_57_range__65_70_range__97_102_char_class___range__48_57_range__65_70_range__97_102_char_class___range__48_57_range__65_70_range__97_102_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__UnicodeEscape__lit___92_iter__char_class___range__117_117_char_class___range__48_57_range__65_70_range__97_102_char_class___range__48_57_range__65_70_range__97_102_char_class___range__48_57_range__65_70_range__97_102_char_class___range__48_57_range__65_70_range__97_102_(builder);
      
    }
  }
	
  protected static class Prod {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__AssociativityGroup_Prod__associativity_Assoc_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_group_Prod_layouts_LAYOUTLIST_lit___41__tag__Foldable(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[7];
      
      tmp[6] = new LiteralStackNode(5674, 6, prod__lit___41__char_class___range__41_41_, new char[] {41}, null, null);
      tmp[5] = new NonTerminalStackNode(5672, 5, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new NonTerminalStackNode(5670, 4, "Prod", null, null);
      tmp[3] = new NonTerminalStackNode(5668, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(5666, 2, prod__lit___40__char_class___range__40_40_, new char[] {40}, null, null);
      tmp[1] = new NonTerminalStackNode(5664, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(5662, 0, "Assoc", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__AssociativityGroup_Prod__associativity_Assoc_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_group_Prod_layouts_LAYOUTLIST_lit___41__tag__Foldable, tmp);
	}
    protected static final void _init_prod__Reference_Prod__lit___58_layouts_LAYOUTLIST_referenced_Name_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode(5680, 2, "Name", null, null);
      tmp[1] = new NonTerminalStackNode(5678, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(5676, 0, prod__lit___58__char_class___range__58_58_, new char[] {58}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Reference_Prod__lit___58_layouts_LAYOUTLIST_referenced_Name_, tmp);
	}
    protected static final void _init_prod__Labeled_Prod__modifiers_iter_star_seps__ProdModifier__layouts_LAYOUTLIST_layouts_LAYOUTLIST_name_Name_layouts_LAYOUTLIST_lit___58_layouts_LAYOUTLIST_args_iter_star_seps__Sym__layouts_LAYOUTLIST_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[7];
      
      tmp[6] = new SeparatedListStackNode(5698, 6, regular__iter_star_seps__Sym__layouts_LAYOUTLIST, new NonTerminalStackNode(5700, 0, "Sym", null, null), new AbstractStackNode[]{new NonTerminalStackNode(5702, 1, "layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[5] = new NonTerminalStackNode(5696, 5, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode(5694, 4, prod__lit___58__char_class___range__58_58_, new char[] {58}, null, null);
      tmp[3] = new NonTerminalStackNode(5692, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(5690, 2, "Name", null, null);
      tmp[1] = new NonTerminalStackNode(5688, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new SeparatedListStackNode(5682, 0, regular__iter_star_seps__ProdModifier__layouts_LAYOUTLIST, new NonTerminalStackNode(5684, 0, "ProdModifier", null, null), new AbstractStackNode[]{new NonTerminalStackNode(5686, 1, "layouts_LAYOUTLIST", null, null)}, false, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Labeled_Prod__modifiers_iter_star_seps__ProdModifier__layouts_LAYOUTLIST_layouts_LAYOUTLIST_name_Name_layouts_LAYOUTLIST_lit___58_layouts_LAYOUTLIST_args_iter_star_seps__Sym__layouts_LAYOUTLIST_, tmp);
	}
    protected static final void _init_prod__Others_Prod__lit___46_46_46_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(5704, 0, prod__lit___46_46_46__char_class___range__46_46_char_class___range__46_46_char_class___range__46_46_, new char[] {46,46,46}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Others_Prod__lit___46_46_46_, tmp);
	}
    protected static final void _init_prod__First_Prod__lhs_Prod_layouts_LAYOUTLIST_conditional__lit___62__not_follow__lit___62_layouts_LAYOUTLIST_rhs_Prod__assoc__left(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(5738, 4, "Prod", null, null);
      tmp[3] = new NonTerminalStackNode(5736, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(5734, 2, prod__lit___62__char_class___range__62_62_, new char[] {62}, null, new ICompletionFilter[] {new StringFollowRestriction(new char[] {62})});
      tmp[1] = new NonTerminalStackNode(5732, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(5730, 0, "Prod", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__First_Prod__lhs_Prod_layouts_LAYOUTLIST_conditional__lit___62__not_follow__lit___62_layouts_LAYOUTLIST_rhs_Prod__assoc__left, tmp);
	}
    protected static final void _init_prod__Unlabeled_Prod__modifiers_iter_star_seps__ProdModifier__layouts_LAYOUTLIST_layouts_LAYOUTLIST_args_iter_star_seps__Sym__layouts_LAYOUTLIST_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[2] = new SeparatedListStackNode(5714, 2, regular__iter_star_seps__Sym__layouts_LAYOUTLIST, new NonTerminalStackNode(5716, 0, "Sym", null, null), new AbstractStackNode[]{new NonTerminalStackNode(5718, 1, "layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[1] = new NonTerminalStackNode(5712, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new SeparatedListStackNode(5706, 0, regular__iter_star_seps__ProdModifier__layouts_LAYOUTLIST, new NonTerminalStackNode(5708, 0, "ProdModifier", null, null), new AbstractStackNode[]{new NonTerminalStackNode(5710, 1, "layouts_LAYOUTLIST", null, null)}, false, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Unlabeled_Prod__modifiers_iter_star_seps__ProdModifier__layouts_LAYOUTLIST_layouts_LAYOUTLIST_args_iter_star_seps__Sym__layouts_LAYOUTLIST_, tmp);
	}
    protected static final void _init_prod__All_Prod__lhs_Prod_layouts_LAYOUTLIST_lit___124_layouts_LAYOUTLIST_rhs_Prod__assoc__left(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(5728, 4, "Prod", null, null);
      tmp[3] = new NonTerminalStackNode(5726, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(5724, 2, prod__lit___124__char_class___range__124_124_, new char[] {124}, null, null);
      tmp[1] = new NonTerminalStackNode(5722, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(5720, 0, "Prod", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__All_Prod__lhs_Prod_layouts_LAYOUTLIST_lit___124_layouts_LAYOUTLIST_rhs_Prod__assoc__left, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
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
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__OctalIntegerLiteral__char_class___range__48_48_conditional__iter__char_class___range__48_55__not_follow__char_class___range__48_57_range__65_90_range__95_95_range__97_122_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[2];
      
      tmp[1] = new ListStackNode(5746, 1, regular__iter__char_class___range__48_55, new CharStackNode(5748, 0, new char[][]{{48,55}}, null, null), true, null, new ICompletionFilter[] {new CharFollowRestriction(new char[][]{{48,57},{65,90},{95,95},{97,122}})});
      tmp[0] = new CharStackNode(5744, 0, new char[][]{{48,48}}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__OctalIntegerLiteral__char_class___range__48_48_conditional__iter__char_class___range__48_55__not_follow__char_class___range__48_57_range__65_90_range__95_95_range__97_122_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__OctalIntegerLiteral__char_class___range__48_48_conditional__iter__char_class___range__48_55__not_follow__char_class___range__48_57_range__65_90_range__95_95_range__97_122_(builder);
      
    }
  }
	
  protected static class Pattern {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__Map_Pattern__lit___40_layouts_LAYOUTLIST_mappings_iter_star_seps__Mapping__Pattern__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new LiteralStackNode(5782, 4, prod__lit___41__char_class___range__41_41_, new char[] {41}, null, null);
      tmp[3] = new NonTerminalStackNode(5780, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new SeparatedListStackNode(5770, 2, regular__iter_star_seps__Mapping__Pattern__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST, new NonTerminalStackNode(5772, 0, "Mapping__Pattern", null, null), new AbstractStackNode[]{new NonTerminalStackNode(5774, 1, "layouts_LAYOUTLIST", null, null), new LiteralStackNode(5776, 2, prod__lit___44__char_class___range__44_44_, new char[] {44}, null, null), new NonTerminalStackNode(5778, 3, "layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[1] = new NonTerminalStackNode(5768, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(5766, 0, prod__lit___40__char_class___range__40_40_, new char[] {40}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Map_Pattern__lit___40_layouts_LAYOUTLIST_mappings_iter_star_seps__Mapping__Pattern__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_, tmp);
	}
    protected static final void _init_prod__List_Pattern__lit___91_layouts_LAYOUTLIST_elements_iter_star_seps__Pattern__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___93_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new LiteralStackNode(5800, 4, prod__lit___93__char_class___range__93_93_, new char[] {93}, null, null);
      tmp[3] = new NonTerminalStackNode(5798, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new SeparatedListStackNode(5788, 2, regular__iter_star_seps__Pattern__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST, new NonTerminalStackNode(5790, 0, "Pattern", null, null), new AbstractStackNode[]{new NonTerminalStackNode(5792, 1, "layouts_LAYOUTLIST", null, null), new LiteralStackNode(5794, 2, prod__lit___44__char_class___range__44_44_, new char[] {44}, null, null), new NonTerminalStackNode(5796, 3, "layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[1] = new NonTerminalStackNode(5786, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(5784, 0, prod__lit___91__char_class___range__91_91_, new char[] {91}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__List_Pattern__lit___91_layouts_LAYOUTLIST_elements_iter_star_seps__Pattern__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___93_, tmp);
	}
    protected static final void _init_prod__ReifiedType_Pattern__basicType_BasicType_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_arguments_iter_star_seps__Pattern__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[7];
      
      tmp[6] = new LiteralStackNode(5822, 6, prod__lit___41__char_class___range__41_41_, new char[] {41}, null, null);
      tmp[5] = new NonTerminalStackNode(5820, 5, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new SeparatedListStackNode(5810, 4, regular__iter_star_seps__Pattern__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST, new NonTerminalStackNode(5812, 0, "Pattern", null, null), new AbstractStackNode[]{new NonTerminalStackNode(5814, 1, "layouts_LAYOUTLIST", null, null), new LiteralStackNode(5816, 2, prod__lit___44__char_class___range__44_44_, new char[] {44}, null, null), new NonTerminalStackNode(5818, 3, "layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[3] = new NonTerminalStackNode(5808, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(5806, 2, prod__lit___40__char_class___range__40_40_, new char[] {40}, null, null);
      tmp[1] = new NonTerminalStackNode(5804, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(5802, 0, "BasicType", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__ReifiedType_Pattern__basicType_BasicType_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_arguments_iter_star_seps__Pattern__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_, tmp);
	}
    protected static final void _init_prod__Literal_Pattern__literal_Literal_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode(5824, 0, "Literal", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Literal_Pattern__literal_Literal_, tmp);
	}
    protected static final void _init_prod__QualifiedName_Pattern__qualifiedName_QualifiedName_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode(5826, 0, "QualifiedName", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__QualifiedName_Pattern__qualifiedName_QualifiedName_, tmp);
	}
    protected static final void _init_prod__AsType_Pattern__lit___91_layouts_LAYOUTLIST_type_Type_layouts_LAYOUTLIST_lit___93_layouts_LAYOUTLIST_argument_Pattern_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[7];
      
      tmp[6] = new NonTerminalStackNode(5910, 6, "Pattern", null, null);
      tmp[5] = new NonTerminalStackNode(5908, 5, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode(5906, 4, prod__lit___93__char_class___range__93_93_, new char[] {93}, null, null);
      tmp[3] = new NonTerminalStackNode(5904, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(5902, 2, "Type", null, null);
      tmp[1] = new NonTerminalStackNode(5900, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(5898, 0, prod__lit___91__char_class___range__91_91_, new char[] {91}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__AsType_Pattern__lit___91_layouts_LAYOUTLIST_type_Type_layouts_LAYOUTLIST_lit___93_layouts_LAYOUTLIST_argument_Pattern_, tmp);
	}
    protected static final void _init_prod__Descendant_Pattern__lit___47_layouts_LAYOUTLIST_pattern_Pattern_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode(5916, 2, "Pattern", null, null);
      tmp[1] = new NonTerminalStackNode(5914, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(5912, 0, prod__lit___47__char_class___range__47_47_, new char[] {47}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Descendant_Pattern__lit___47_layouts_LAYOUTLIST_pattern_Pattern_, tmp);
	}
    protected static final void _init_prod__MultiVariable_Pattern__qualifiedName_QualifiedName_layouts_LAYOUTLIST_lit___42_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[2] = new LiteralStackNode(5850, 2, prod__lit___42__char_class___range__42_42_, new char[] {42}, null, null);
      tmp[1] = new NonTerminalStackNode(5848, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(5846, 0, "QualifiedName", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__MultiVariable_Pattern__qualifiedName_QualifiedName_layouts_LAYOUTLIST_lit___42_, tmp);
	}
    protected static final void _init_prod__Tuple_Pattern__lit___60_layouts_LAYOUTLIST_elements_iter_seps__Pattern__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___62_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new LiteralStackNode(5844, 4, prod__lit___62__char_class___range__62_62_, new char[] {62}, null, null);
      tmp[3] = new NonTerminalStackNode(5842, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new SeparatedListStackNode(5832, 2, regular__iter_seps__Pattern__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST, new NonTerminalStackNode(5834, 0, "Pattern", null, null), new AbstractStackNode[]{new NonTerminalStackNode(5836, 1, "layouts_LAYOUTLIST", null, null), new LiteralStackNode(5838, 2, prod__lit___44__char_class___range__44_44_, new char[] {44}, null, null), new NonTerminalStackNode(5840, 3, "layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[1] = new NonTerminalStackNode(5830, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(5828, 0, prod__lit___60__char_class___range__60_60_, new char[] {60}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Tuple_Pattern__lit___60_layouts_LAYOUTLIST_elements_iter_seps__Pattern__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___62_, tmp);
	}
    protected static final void _init_prod__Anti_Pattern__lit___33_layouts_LAYOUTLIST_pattern_Pattern_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[0] = new LiteralStackNode(5928, 0, prod__lit___33__char_class___range__33_33_, new char[] {33}, null, null);
      tmp[1] = new NonTerminalStackNode(5930, 1, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(5932, 2, "Pattern", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Anti_Pattern__lit___33_layouts_LAYOUTLIST_pattern_Pattern_, tmp);
	}
    protected static final void _init_prod__TypedVariableBecomes_Pattern__type_Type_layouts_LAYOUTLIST_name_Name_layouts_LAYOUTLIST_lit___58_layouts_LAYOUTLIST_pattern_Pattern_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[7];
      
      tmp[6] = new NonTerminalStackNode(5946, 6, "Pattern", null, null);
      tmp[5] = new NonTerminalStackNode(5944, 5, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(5934, 0, "Type", null, null);
      tmp[1] = new NonTerminalStackNode(5936, 1, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(5938, 2, "Name", null, null);
      tmp[3] = new NonTerminalStackNode(5940, 3, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode(5942, 4, prod__lit___58__char_class___range__58_58_, new char[] {58}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__TypedVariableBecomes_Pattern__type_Type_layouts_LAYOUTLIST_name_Name_layouts_LAYOUTLIST_lit___58_layouts_LAYOUTLIST_pattern_Pattern_, tmp);
	}
    protected static final void _init_prod__VariableBecomes_Pattern__name_Name_layouts_LAYOUTLIST_lit___58_layouts_LAYOUTLIST_pattern_Pattern_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(5926, 4, "Pattern", null, null);
      tmp[3] = new NonTerminalStackNode(5924, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(5922, 2, prod__lit___58__char_class___range__58_58_, new char[] {58}, null, null);
      tmp[1] = new NonTerminalStackNode(5920, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(5918, 0, "Name", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__VariableBecomes_Pattern__name_Name_layouts_LAYOUTLIST_lit___58_layouts_LAYOUTLIST_pattern_Pattern_, tmp);
	}
    protected static final void _init_prod__TypedVariable_Pattern__type_Type_layouts_LAYOUTLIST_name_Name_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode(5856, 2, "Name", null, null);
      tmp[1] = new NonTerminalStackNode(5854, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(5852, 0, "Type", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__TypedVariable_Pattern__type_Type_layouts_LAYOUTLIST_name_Name_, tmp);
	}
    protected static final void _init_prod__Set_Pattern__lit___123_layouts_LAYOUTLIST_elements_iter_star_seps__Pattern__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new LiteralStackNode(5874, 4, prod__lit___125__char_class___range__125_125_, new char[] {125}, null, null);
      tmp[3] = new NonTerminalStackNode(5872, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new SeparatedListStackNode(5862, 2, regular__iter_star_seps__Pattern__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST, new NonTerminalStackNode(5864, 0, "Pattern", null, null), new AbstractStackNode[]{new NonTerminalStackNode(5866, 1, "layouts_LAYOUTLIST", null, null), new LiteralStackNode(5868, 2, prod__lit___44__char_class___range__44_44_, new char[] {44}, null, null), new NonTerminalStackNode(5870, 3, "layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[1] = new NonTerminalStackNode(5860, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(5858, 0, prod__lit___123__char_class___range__123_123_, new char[] {123}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Set_Pattern__lit___123_layouts_LAYOUTLIST_elements_iter_star_seps__Pattern__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_, tmp);
	}
    protected static final void _init_prod__CallOrTree_Pattern__expression_Pattern_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_arguments_iter_star_seps__Pattern__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[7];
      
      tmp[6] = new LiteralStackNode(5896, 6, prod__lit___41__char_class___range__41_41_, new char[] {41}, null, null);
      tmp[5] = new NonTerminalStackNode(5894, 5, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new SeparatedListStackNode(5884, 4, regular__iter_star_seps__Pattern__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST, new NonTerminalStackNode(5886, 0, "Pattern", null, null), new AbstractStackNode[]{new NonTerminalStackNode(5888, 1, "layouts_LAYOUTLIST", null, null), new LiteralStackNode(5890, 2, prod__lit___44__char_class___range__44_44_, new char[] {44}, null, null), new NonTerminalStackNode(5892, 3, "layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[3] = new NonTerminalStackNode(5882, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(5880, 2, prod__lit___40__char_class___range__40_40_, new char[] {40}, null, null);
      tmp[1] = new NonTerminalStackNode(5878, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(5876, 0, "Pattern", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__CallOrTree_Pattern__expression_Pattern_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_arguments_iter_star_seps__Pattern__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__Map_Pattern__lit___40_layouts_LAYOUTLIST_mappings_iter_star_seps__Mapping__Pattern__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_(builder);
      
        _init_prod__List_Pattern__lit___91_layouts_LAYOUTLIST_elements_iter_star_seps__Pattern__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___93_(builder);
      
        _init_prod__ReifiedType_Pattern__basicType_BasicType_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_arguments_iter_star_seps__Pattern__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_(builder);
      
        _init_prod__Literal_Pattern__literal_Literal_(builder);
      
        _init_prod__QualifiedName_Pattern__qualifiedName_QualifiedName_(builder);
      
        _init_prod__AsType_Pattern__lit___91_layouts_LAYOUTLIST_type_Type_layouts_LAYOUTLIST_lit___93_layouts_LAYOUTLIST_argument_Pattern_(builder);
      
        _init_prod__Descendant_Pattern__lit___47_layouts_LAYOUTLIST_pattern_Pattern_(builder);
      
        _init_prod__MultiVariable_Pattern__qualifiedName_QualifiedName_layouts_LAYOUTLIST_lit___42_(builder);
      
        _init_prod__Tuple_Pattern__lit___60_layouts_LAYOUTLIST_elements_iter_seps__Pattern__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___62_(builder);
      
        _init_prod__Anti_Pattern__lit___33_layouts_LAYOUTLIST_pattern_Pattern_(builder);
      
        _init_prod__TypedVariableBecomes_Pattern__type_Type_layouts_LAYOUTLIST_name_Name_layouts_LAYOUTLIST_lit___58_layouts_LAYOUTLIST_pattern_Pattern_(builder);
      
        _init_prod__VariableBecomes_Pattern__name_Name_layouts_LAYOUTLIST_lit___58_layouts_LAYOUTLIST_pattern_Pattern_(builder);
      
        _init_prod__TypedVariable_Pattern__type_Type_layouts_LAYOUTLIST_name_Name_(builder);
      
        _init_prod__Set_Pattern__lit___123_layouts_LAYOUTLIST_elements_iter_star_seps__Pattern__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_(builder);
      
        _init_prod__CallOrTree_Pattern__expression_Pattern_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_arguments_iter_star_seps__Pattern__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_(builder);
      
    }
  }
	
  protected static class DateAndTime {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__DateAndTime__lit___36_DatePart_lit_T_TimePartNoTZ_opt__TimeZonePart_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new OptionalStackNode(5978, 4, regular__opt__TimeZonePart, new NonTerminalStackNode(5980, 0, "TimeZonePart", null, null), null, null);
      tmp[3] = new NonTerminalStackNode(5976, 3, "TimePartNoTZ", null, null);
      tmp[2] = new LiteralStackNode(5974, 2, prod__lit_T__char_class___range__84_84_, new char[] {84}, null, null);
      tmp[1] = new NonTerminalStackNode(5972, 1, "DatePart", null, null);
      tmp[0] = new LiteralStackNode(5970, 0, prod__lit___36__char_class___range__36_36_, new char[] {36}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__DateAndTime__lit___36_DatePart_lit_T_TimePartNoTZ_opt__TimeZonePart_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__DateAndTime__lit___36_DatePart_lit_T_TimePartNoTZ_opt__TimeZonePart_(builder);
      
    }
  }
	
  protected static class Tag {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__Default_Tag__lit___64_layouts_LAYOUTLIST_name_Name_layouts_LAYOUTLIST_contents_TagString__tag__Foldable_tag__category___67_111_109_109_101_110_116(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(6008, 4, "TagString", null, null);
      tmp[3] = new NonTerminalStackNode(6006, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(6004, 2, "Name", null, null);
      tmp[1] = new NonTerminalStackNode(6002, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(6000, 0, prod__lit___64__char_class___range__64_64_, new char[] {64}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Default_Tag__lit___64_layouts_LAYOUTLIST_name_Name_layouts_LAYOUTLIST_contents_TagString__tag__Foldable_tag__category___67_111_109_109_101_110_116, tmp);
	}
    protected static final void _init_prod__Expression_Tag__lit___64_layouts_LAYOUTLIST_name_Name_layouts_LAYOUTLIST_lit___61_layouts_LAYOUTLIST_expression_Expression__tag__Foldable_tag__category___67_111_109_109_101_110_116(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[7];
      
      tmp[6] = new NonTerminalStackNode(6022, 6, "Expression", null, null);
      tmp[5] = new NonTerminalStackNode(6020, 5, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode(6018, 4, prod__lit___61__char_class___range__61_61_, new char[] {61}, null, null);
      tmp[3] = new NonTerminalStackNode(6016, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(6014, 2, "Name", null, null);
      tmp[1] = new NonTerminalStackNode(6012, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(6010, 0, prod__lit___64__char_class___range__64_64_, new char[] {64}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Expression_Tag__lit___64_layouts_LAYOUTLIST_name_Name_layouts_LAYOUTLIST_lit___61_layouts_LAYOUTLIST_expression_Expression__tag__Foldable_tag__category___67_111_109_109_101_110_116, tmp);
	}
    protected static final void _init_prod__Empty_Tag__lit___64_layouts_LAYOUTLIST_name_Name__tag__Foldable_tag__category___67_111_109_109_101_110_116(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode(6028, 2, "Name", null, null);
      tmp[1] = new NonTerminalStackNode(6026, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(6024, 0, prod__lit___64__char_class___range__64_64_, new char[] {64}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Empty_Tag__lit___64_layouts_LAYOUTLIST_name_Name__tag__Foldable_tag__category___67_111_109_109_101_110_116, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__Default_Tag__lit___64_layouts_LAYOUTLIST_name_Name_layouts_LAYOUTLIST_contents_TagString__tag__Foldable_tag__category___67_111_109_109_101_110_116(builder);
      
        _init_prod__Expression_Tag__lit___64_layouts_LAYOUTLIST_name_Name_layouts_LAYOUTLIST_lit___61_layouts_LAYOUTLIST_expression_Expression__tag__Foldable_tag__category___67_111_109_109_101_110_116(builder);
      
        _init_prod__Empty_Tag__lit___64_layouts_LAYOUTLIST_name_Name__tag__Foldable_tag__category___67_111_109_109_101_110_116(builder);
      
    }
  }
	
  public ObjectRascalRascal() {
    super();
  }

  // Parse methods    
  
  public AbstractStackNode[] Literal() {
    return Literal.EXPECTS;
  }
  public AbstractStackNode[] Module() {
    return Module.EXPECTS;
  }
  public AbstractStackNode[] PreProtocolChars() {
    return PreProtocolChars.EXPECTS;
  }
  public AbstractStackNode[] Variable() {
    return Variable.EXPECTS;
  }
  public AbstractStackNode[] TypeArg() {
    return TypeArg.EXPECTS;
  }
  public AbstractStackNode[] Renaming() {
    return Renaming.EXPECTS;
  }
  public AbstractStackNode[] Catch() {
    return Catch.EXPECTS;
  }
  public AbstractStackNode[] Signature() {
    return Signature.EXPECTS;
  }
  public AbstractStackNode[] Sym() {
    return Sym.EXPECTS;
  }
  public AbstractStackNode[] NonterminalLabel() {
    return NonterminalLabel.EXPECTS;
  }
  public AbstractStackNode[] Header() {
    return Header.EXPECTS;
  }
  public AbstractStackNode[] Commands() {
    return Commands.EXPECTS;
  }
  public AbstractStackNode[] ImportedModule() {
    return ImportedModule.EXPECTS;
  }
  public AbstractStackNode[] Expression() {
    return Expression.EXPECTS;
  }
  public AbstractStackNode[] TagString() {
    return TagString.EXPECTS;
  }
  public AbstractStackNode[] Nonterminal() {
    return Nonterminal.EXPECTS;
  }
  public AbstractStackNode[] PreModule() {
    return PreModule.EXPECTS;
  }
  public AbstractStackNode[] ProtocolPart() {
    return ProtocolPart.EXPECTS;
  }
  public AbstractStackNode[] Comment() {
    return Comment.EXPECTS;
  }
  public AbstractStackNode[] Label() {
    return Label.EXPECTS;
  }
  public AbstractStackNode[] Field() {
    return Field.EXPECTS;
  }
  public AbstractStackNode[] FunctionModifier() {
    return FunctionModifier.EXPECTS;
  }
  public AbstractStackNode[] Assignment() {
    return Assignment.EXPECTS;
  }
  public AbstractStackNode[] ProtocolChars() {
    return ProtocolChars.EXPECTS;
  }
  public AbstractStackNode[] Assignable() {
    return Assignable.EXPECTS;
  }
  public AbstractStackNode[] HeaderKeyword() {
    return HeaderKeyword.EXPECTS;
  }
  public AbstractStackNode[] Parameters() {
    return Parameters.EXPECTS;
  }
  public AbstractStackNode[] DatePart() {
    return DatePart.EXPECTS;
  }
  public AbstractStackNode[] Mapping__Pattern() {
    return Mapping__Pattern.EXPECTS;
  }
  public AbstractStackNode[] LocalVariableDeclaration() {
    return LocalVariableDeclaration.EXPECTS;
  }
  public AbstractStackNode[] StringConstant() {
    return StringConstant.EXPECTS;
  }
  public AbstractStackNode[] start__Module() {
    return start__Module.EXPECTS;
  }
  public AbstractStackNode[] DataTypeSelector() {
    return DataTypeSelector.EXPECTS;
  }
  public AbstractStackNode[] StringTail() {
    return StringTail.EXPECTS;
  }
  public AbstractStackNode[] PatternWithAction() {
    return PatternWithAction.EXPECTS;
  }
  public AbstractStackNode[] PathTail() {
    return PathTail.EXPECTS;
  }
  public AbstractStackNode[] MidProtocolChars() {
    return MidProtocolChars.EXPECTS;
  }
  public AbstractStackNode[] JustDate() {
    return JustDate.EXPECTS;
  }
  public AbstractStackNode[] CaseInsensitiveStringConstant() {
    return CaseInsensitiveStringConstant.EXPECTS;
  }
  public AbstractStackNode[] start__Commands() {
    return start__Commands.EXPECTS;
  }
  public AbstractStackNode[] Backslash() {
    return Backslash.EXPECTS;
  }
  public AbstractStackNode[] Tags() {
    return Tags.EXPECTS;
  }
  public AbstractStackNode[] Formals() {
    return Formals.EXPECTS;
  }
  public AbstractStackNode[] Start() {
    return Start.EXPECTS;
  }
  public AbstractStackNode[] Name() {
    return Name.EXPECTS;
  }
  public AbstractStackNode[] TimePartNoTZ() {
    return TimePartNoTZ.EXPECTS;
  }
  public AbstractStackNode[] StructuredType() {
    return StructuredType.EXPECTS;
  }
  public AbstractStackNode[] Declarator() {
    return Declarator.EXPECTS;
  }
  public AbstractStackNode[] start__PreModule() {
    return start__PreModule.EXPECTS;
  }
  public AbstractStackNode[] Variant() {
    return Variant.EXPECTS;
  }
  public AbstractStackNode[] FunctionDeclaration() {
    return FunctionDeclaration.EXPECTS;
  }
  public AbstractStackNode[] BasicType() {
    return BasicType.EXPECTS;
  }
  public AbstractStackNode[] DateTimeLiteral() {
    return DateTimeLiteral.EXPECTS;
  }
  public AbstractStackNode[] PathChars() {
    return PathChars.EXPECTS;
  }
  public AbstractStackNode[] ModuleActuals() {
    return ModuleActuals.EXPECTS;
  }
  public AbstractStackNode[] PostStringChars() {
    return PostStringChars.EXPECTS;
  }
  public AbstractStackNode[] HexIntegerLiteral() {
    return HexIntegerLiteral.EXPECTS;
  }
  public AbstractStackNode[] OctalEscapeSequence() {
    return OctalEscapeSequence.EXPECTS;
  }
  public AbstractStackNode[] RegExpLiteral() {
    return RegExpLiteral.EXPECTS;
  }
  public AbstractStackNode[] NamedRegExp() {
    return NamedRegExp.EXPECTS;
  }
  public AbstractStackNode[] ModuleParameters() {
    return ModuleParameters.EXPECTS;
  }
  public AbstractStackNode[] RascalKeywords() {
    return RascalKeywords.EXPECTS;
  }
  public AbstractStackNode[] Strategy() {
    return Strategy.EXPECTS;
  }
  public AbstractStackNode[] Char() {
    return Char.EXPECTS;
  }
  public AbstractStackNode[] PrePathChars() {
    return PrePathChars.EXPECTS;
  }
  public AbstractStackNode[] MidPathChars() {
    return MidPathChars.EXPECTS;
  }
  public AbstractStackNode[] PostProtocolChars() {
    return PostProtocolChars.EXPECTS;
  }
  public AbstractStackNode[] SyntaxDefinition() {
    return SyntaxDefinition.EXPECTS;
  }
  public AbstractStackNode[] Kind() {
    return Kind.EXPECTS;
  }
  public AbstractStackNode[] Target() {
    return Target.EXPECTS;
  }
  public AbstractStackNode[] IntegerLiteral() {
    return IntegerLiteral.EXPECTS;
  }
  public AbstractStackNode[] start__Command() {
    return start__Command.EXPECTS;
  }
  public AbstractStackNode[] FunctionBody() {
    return FunctionBody.EXPECTS;
  }
  public AbstractStackNode[] UserType() {
    return UserType.EXPECTS;
  }
  public AbstractStackNode[] Import() {
    return Import.EXPECTS;
  }
  public AbstractStackNode[] LAYOUT() {
    return LAYOUT.EXPECTS;
  }
  public AbstractStackNode[] Word() {
    return Word.EXPECTS;
  }
  public AbstractStackNode[] Body() {
    return Body.EXPECTS;
  }
  public AbstractStackNode[] DecimalIntegerLiteral() {
    return DecimalIntegerLiteral.EXPECTS;
  }
  public AbstractStackNode[] StringTemplate() {
    return StringTemplate.EXPECTS;
  }
  public AbstractStackNode[] PathPart() {
    return PathPart.EXPECTS;
  }
  public AbstractStackNode[] RegExp() {
    return RegExp.EXPECTS;
  }
  public AbstractStackNode[] MidStringChars() {
    return MidStringChars.EXPECTS;
  }
  public AbstractStackNode[] RegExpModifier() {
    return RegExpModifier.EXPECTS;
  }
  public AbstractStackNode[] layouts_$BACKTICKS() {
    return layouts_$BACKTICKS.EXPECTS;
  }
  public AbstractStackNode[] Assoc() {
    return Assoc.EXPECTS;
  }
  public AbstractStackNode[] Replacement() {
    return Replacement.EXPECTS;
  }
  public AbstractStackNode[] DataTarget() {
    return DataTarget.EXPECTS;
  }
  public AbstractStackNode[] layouts_$default$() {
    return layouts_$default$.EXPECTS;
  }
  public AbstractStackNode[] RealLiteral() {
    return RealLiteral.EXPECTS;
  }
  public AbstractStackNode[] layouts_LAYOUTLIST() {
    return layouts_LAYOUTLIST.EXPECTS;
  }
  public AbstractStackNode[] StringCharacter() {
    return StringCharacter.EXPECTS;
  }
  public AbstractStackNode[] JustTime() {
    return JustTime.EXPECTS;
  }
  public AbstractStackNode[] Range() {
    return Range.EXPECTS;
  }
  public AbstractStackNode[] LocationLiteral() {
    return LocationLiteral.EXPECTS;
  }
  public AbstractStackNode[] ShellCommand() {
    return ShellCommand.EXPECTS;
  }
  public AbstractStackNode[] URLChars() {
    return URLChars.EXPECTS;
  }
  public AbstractStackNode[] StringMiddle() {
    return StringMiddle.EXPECTS;
  }
  public AbstractStackNode[] QualifiedName() {
    return QualifiedName.EXPECTS;
  }
  public AbstractStackNode[] TimeZonePart() {
    return TimeZonePart.EXPECTS;
  }
  public AbstractStackNode[] PreStringChars() {
    return PreStringChars.EXPECTS;
  }
  public AbstractStackNode[] Command() {
    return Command.EXPECTS;
  }
  public AbstractStackNode[] Visit() {
    return Visit.EXPECTS;
  }
  public AbstractStackNode[] ProtocolTail() {
    return ProtocolTail.EXPECTS;
  }
  public AbstractStackNode[] NamedBackslash() {
    return NamedBackslash.EXPECTS;
  }
  public AbstractStackNode[] Visibility() {
    return Visibility.EXPECTS;
  }
  public AbstractStackNode[] PostPathChars() {
    return PostPathChars.EXPECTS;
  }
  public AbstractStackNode[] StringLiteral() {
    return StringLiteral.EXPECTS;
  }
  public AbstractStackNode[] Renamings() {
    return Renamings.EXPECTS;
  }
  public AbstractStackNode[] layouts_$QUOTES() {
    return layouts_$QUOTES.EXPECTS;
  }
  public AbstractStackNode[] Statement() {
    return Statement.EXPECTS;
  }
  public AbstractStackNode[] FunctionType() {
    return FunctionType.EXPECTS;
  }
  public AbstractStackNode[] Case() {
    return Case.EXPECTS;
  }
  public AbstractStackNode[] Bound() {
    return Bound.EXPECTS;
  }
  public AbstractStackNode[] Declaration() {
    return Declaration.EXPECTS;
  }
  public AbstractStackNode[] Type() {
    return Type.EXPECTS;
  }
  public AbstractStackNode[] Class() {
    return Class.EXPECTS;
  }
  public AbstractStackNode[] Mapping__Expression() {
    return Mapping__Expression.EXPECTS;
  }
  public AbstractStackNode[] FunctionModifiers() {
    return FunctionModifiers.EXPECTS;
  }
  public AbstractStackNode[] Comprehension() {
    return Comprehension.EXPECTS;
  }
  public AbstractStackNode[] ProdModifier() {
    return ProdModifier.EXPECTS;
  }
  public AbstractStackNode[] Toplevel() {
    return Toplevel.EXPECTS;
  }
  public AbstractStackNode[] BooleanLiteral() {
    return BooleanLiteral.EXPECTS;
  }
  public AbstractStackNode[] TypeVar() {
    return TypeVar.EXPECTS;
  }
  public AbstractStackNode[] RationalLiteral() {
    return RationalLiteral.EXPECTS;
  }
  public AbstractStackNode[] UnicodeEscape() {
    return UnicodeEscape.EXPECTS;
  }
  public AbstractStackNode[] Prod() {
    return Prod.EXPECTS;
  }
  public AbstractStackNode[] OctalIntegerLiteral() {
    return OctalIntegerLiteral.EXPECTS;
  }
  public AbstractStackNode[] Pattern() {
    return Pattern.EXPECTS;
  }
  public AbstractStackNode[] DateAndTime() {
    return DateAndTime.EXPECTS;
  }
  public AbstractStackNode[] Tag() {
    return Tag.EXPECTS;
  }
}