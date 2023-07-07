@license{
  Copyright (c) 2009-2015 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Mark Hills - Mark.Hills@cwi.nl (CWI)}
module resource::jdbc::JDBC

import Exception;
import Type;
import Map;
import String;
import List;
import Set;
import IO;
import lang::rascal::types::AbstractType;

@synopsis{Given the name of a JDBC driver class, register it so it can be used in connections.}
@javaClass{org.rascalmpl.library.resource.jdbc.JDBC}
@reflect{
uses information about class loaders from the evaluator context
}
public java void registerJDBCClass(str className);

@synopsis{The JDBC driver name for MySQL}
public str mysqlDriver = "com.mysql.jdbc.Driver";

@synopsis{Generate a MySQL connect string.}
public str mysqlConnectString(map[str,str] properties) {
    host = ("host" notin properties) ? "127.0.0.1" : properties["host"];
    if ("database" notin properties) throw "A database must be specified";
    database = properties["database"];
    orderedParams = ["user","password"]; 
    return "jdbc:mysql://<host>/<database>?<intercalate("&",["<x>=<properties[x]>"| x <- orderedParams, x in properties, x notin {"host","database"}])>";
}

@synopsis{Indicates which drivers are needed for the different database types encoded in URIs}
public map[str,str] drivers = ( "mysql" : mysqlDriver );

@synopsis{Generates connect strings for different database types}
public map[str,str(map[str,str])] connectStringGenerators = ( "mysql" : mysqlConnectString );

@synopsis{JDBC Connection type}
data Connection = jdbcConnection(int id);

@synopsis{Create a connection based on the given connection string.}
@javaClass{org.rascalmpl.library.resource.jdbc.JDBC}
public java Connection createConnection(str connectString);

@synopsis{Close the given connection.}
@javaClass{org.rascalmpl.library.resource.jdbc.JDBC}
public java void closeConnection(Connection connection);

@synopsis{Get the types of tables available through this connection.}
@javaClass{org.rascalmpl.library.resource.jdbc.JDBC}
public java list[str] getTableTypes(Connection connection);

@synopsis{The JDBC types that could be assigned to various columns.}
data JDBCType
    = array()
    | bigInt()
    | binary()
    | bit()
    | blob()
    | boolean()
    | char()
    | clob()
    | dataLink()
    | date()
    | decimal()
    | distinct()
    | double()
    | float()
    | integer()
    | javaObject()
    | longNVarChar()
    | longVarBinary()
    | longVarChar()
    | nChar()
    | nClob()
    | null()
    | numeric()
    | nVarChar()
    | other()
    | \real()
    | ref()
    | rowId()
    | smallInt()
    | sqlXML()
    | struct()
    | time()
    | timeStamp()
    | tinyInt()
    | varBinary()
    | varChar()
    ;

@synopsis{A column in a table or view}   
data Column = column(str columnName, JDBCType columnType, bool nullable);

@synopsis{A table in a database}
data Table = table(str tableName, list[Column] columns);

@synopsis{Get the tables visible through this connection (just names).}
@javaClass{org.rascalmpl.library.resource.jdbc.JDBC}
public java set[str] getTableNames(Connection connection);

@synopsis{Get the tables visible through this connection (with column info).}
@javaClass{org.rascalmpl.library.resource.jdbc.JDBC}
public java set[Table] getTables(Connection connection);

@synopsis{Get the tables visible through this connection (with column info).}
@javaClass{org.rascalmpl.library.resource.jdbc.JDBC}
public java set[Table] getViews(Connection connection);

@synopsis{Get the Table metadata for a named table.}
@javaClass{org.rascalmpl.library.resource.jdbc.JDBC}
public java Table getTable(Connection connection, str tableName);

@synopsis{Get the Table metadata for a named view.}
@javaClass{org.rascalmpl.library.resource.jdbc.JDBC}
public Table getView(Connection connection, str viewName) = getTable(connection, viewName);

@synopsis{An exception thrown when we try to translate (or otherwise use) a JDBC type with no Rascal equivalent.}
data RuntimeException = unsupportedJDBCType(JDBCType jdbcType);

@synopsis{Get the Rascal type (as a symbol) for the given JDBC type}
public Symbol jdbc2RascalType(array()) { throw unsupportedJDBCType(array()); } 
public Symbol jdbc2RascalType(bigInt()) = \int();
public Symbol jdbc2RascalType(binary()) = \list(\int());
public Symbol jdbc2RascalType(bit()) = \bool();
public Symbol jdbc2RascalType(blob()) = \list(\int());
public Symbol jdbc2RascalType(boolean()) = \bool();
public Symbol jdbc2RascalType(char()) = \str();
public Symbol jdbc2RascalType(clob()) = \str();
public Symbol jdbc2RascalType(dataLink()) { throw unsupportedJDBCType(dataLink()); }
public Symbol jdbc2RascalType(date()) = \datetime();
public Symbol jdbc2RascalType(decimal()) = Symbol::\real();
public Symbol jdbc2RascalType(distinct()) { throw unsupportedJDBCType(distinct()); }
public Symbol jdbc2RascalType(double()) = Symbol::\real();
public Symbol jdbc2RascalType(float()) = Symbol::\real();
public Symbol jdbc2RascalType(integer()) = \int();
public Symbol jdbc2RascalType(javaObject()) { throw unsupportedJDBCType(javaObject()); }
public Symbol jdbc2RascalType(longNVarChar()) = \str();
public Symbol jdbc2RascalType(longVarBinary()) = \list(\int());
public Symbol jdbc2RascalType(longVarChar()) = \str();
public Symbol jdbc2RascalType(nChar()) = \str();
public Symbol jdbc2RascalType(nClob()) = \str();
public Symbol jdbc2RascalType(JDBCType::null()) { throw unsupportedJDBCType(JDBCType::null()); }
public Symbol jdbc2RascalType(numeric()) = Symbol::\real();
public Symbol jdbc2RascalType(nVarChar()) = \str();
public Symbol jdbc2RascalType(other()) { throw unsupportedJDBCType(other()); }
public Symbol jdbc2RascalType(JDBCType::\real()) = Symbol::\real();
public Symbol jdbc2RascalType(ref()) { throw unsupportedJDBCType(ref()); }
public Symbol jdbc2RascalType(rowId()) { throw unsupportedJDBCType(rowId()); }
public Symbol jdbc2RascalType(smallInt()) = \int();
public Symbol jdbc2RascalType(sqlXML()) { throw unsupportedJDBCType(sqlXML()); }
public Symbol jdbc2RascalType(struct()) { throw unsupportedJDBCType(struct()); }
public Symbol jdbc2RascalType(time()) = \datetime();
public Symbol jdbc2RascalType(timeStamp()) = \datetime();
public Symbol jdbc2RascalType(tinyInt()) = \int();
public Symbol jdbc2RascalType(varBinary()) = \list(\int());
public Symbol jdbc2RascalType(varChar()) = \str();

@synopsis{Represents values which may or may not be null.}
data Nullable[&T] = null() | notnull(&T item);

@synopsis{Load the contents of a table. This will turn the contents into a set, which by its nature will remove any
     duplicates and discard any order. To maintain duplicates, or the order inherent in the table,
     use loadTableOrdered instead.}
@javaClass{org.rascalmpl.library.resource.jdbc.JDBC}
public java set[&T] loadTable(type[&T] resType, Connection connection, str tableName);

@synopsis{Load the contents of a table. This will turn the contents into a set, which by its nature will remove any
     duplicates and discard any order. To maintain duplicates, or the order inherent in the table, use 
     loadTableOrdered instead. This versions uses no type information, meaning that it returns a set of values.} 
@javaClass{org.rascalmpl.library.resource.jdbc.JDBC}
public java set[value] loadTable(Connection connection, str tableName);

@synopsis{Load the contents of a table. This maintains order and duplicates, but does not provide access to the
     relational operations provided by loadTable.}
@javaClass{org.rascalmpl.library.resource.jdbc.JDBC}
public java list[&T] loadTableOrdered(type[&T] resType, Connection connection, str tableName);

@synopsis{Load the contents of a table. This maintains order and duplicates, but does not provide access to the
     relational operations provided by loadTable. Also, with no type information, this version returns a list
     of values.}
@javaClass{org.rascalmpl.library.resource.jdbc.JDBC}
public java list[value] loadTableOrdered(Connection connection, str tableName);

@resource{
jdbctables
}
@synopsis{The JDBC tables schema should be given as:
    jdbctables+connect-string
  where connect-string is the database-specific information needed to connect,
  encoded as a URI, for instance:
    jdbctables+mysql://localhost/bugs?user=my_user_name&password=my_password}
public str allTableSchemas(str moduleName, loc uri) {
    // This indicates which driver we need (MySQL, Oracle, etc)
    driverType = uri.scheme;
    if (driverType notin drivers) throw "Driver not found";

    // The URI should include the host and the database as well. The
    // path should just be the database name.
    hostName = uri.authority;
    if (uri.path != "/<uri.file>") throw "Invalid path, should just include database name";
    dbName = uri.file;
    
    // These are the rest of the params, which should include everything
    // needed to connect plus some params that alter the generation
    params = uri.params;
    
    // Now, create the connect string, based on the type of database and the
    // parameters passed in.
    params["host"] = hostName; params["database"] = dbName;
    connectString = connectStringGenerators[driverType](params);
    
    // We need to connect to get the tables that we will generate accessors for
    registerJDBCClass(drivers[driverType]);
    con = createConnection(connectString);

    // Get back a list of table types, sorted by table name
    ts = sort(toList(getTables(con)),bool(resource::jdbc::JDBC::Table t1, resource::jdbc::JDBC::Table t2) { return t1.tableName < t2.tableName; });
    
    // Then, generate the accessor function for each
    list[str] tfuns = [ ];
    for (Table t <- ts) {
        columnTypes = [ nullable ? \label("\\<cn>",\adt("Nullable",[rt])) : \label("\\<cn>",rt) | table(tn,cl) := t, column(cn,ct,nullable) <- cl, rt := jdbc2RascalType(ct) ];
        columnTuple = \tuple(columnTypes);

        tfun = "alias \\<t.tableName>RowType = <prettyPrintType(columnTuple)>;
               'alias \\<t.tableName>Type = rel[<intercalate(",",[prettyPrintType(ct) | ct <- columnTypes])>];
               '
               'public \\<t.tableName>Type \\<t.tableName>() {
               '    registerJDBCClass(\"<drivers[driverType]>\");
               '    con = createConnection(\"<connectString>\");
               '    \\<t.tableName>Type res = loadTable(#<prettyPrintType(columnTuple)>,con,\"<t.tableName>\");
               '    closeConnection(con);
               '    return res;
               '}
               '";
        
        tfuns += tfun;
    }
        
    closeConnection(con);
    
    // Generate the module, which will include accessors for each table.
    mbody = "module <moduleName>
            'import resource::jdbc::JDBC;
            '
            '<for (tfun <- tfuns) {>
            '<tfun>
            '<}>
            '";
    
    return mbody;
}

@resource{
jdbctable
}
public str tableSchema(str moduleName, loc uri) {
    // This indicates which driver we need (MySQL, Oracle, etc)
    driverType = uri.scheme;
    if (driverType notin drivers) throw "Driver not found";

    // The URI should include the host, the database, and the name of
    // the table. The path should just be database/table.
    hostName = uri.authority;
    if (uri.path != "/<uri.parent.file>/<uri.file>") throw "Invalid path, should include database name/table name";
    tableName = uri.file;
    dbName = uri.parent.file;
    
    // These are the rest of the params, which should include everything
    // needed to connect plus some params that alter the generation
    params = uri.params;
    
    // We can pass the name of the function to generate. If we did, grab it then remove
    // it from the params, which should just contain those needed by the JDBC driver.
    str funname = "resourceValue";
    if ("funname" in params) {
        funname = params["funname"];
        params = domainX(params, {"funname"});
    }
    
    // Now, create the connect string, based on the type of database and the
    // parameters passed in.
    params["host"] = hostName; params["database"] = dbName;
    connectString = connectStringGenerators[driverType](params);
    
    // We need to connect to get the type of the table, which is used below
    registerJDBCClass(drivers[driverType]);
    con = createConnection(connectString);
    t = getTable(con,tableName);
    
    columnTypes = [ nullable ? \label("\\<cn>",\adt("Nullable",[rt])) : \label("\\<cn>",rt) | table(tn,cl) := t, column(cn,ct,nullable) <- cl, rt := jdbc2RascalType(ct) ];
    columnTuple = \tuple(columnTypes);
    
    closeConnection(con);
    
    mbody = "module <moduleName>
            'import resource::jdbc::JDBC;
            '
            'alias \\<tableName>RowType = <prettyPrintType(columnTuple)>;
            'alias \\<tableName>Type = rel[<intercalate(",",[prettyPrintType(ct) | ct <- columnTypes])>];
            '
            'public \\<tableName>Type <funname>() {
            '   registerJDBCClass(\"<drivers[driverType]>\");
            '   con = createConnection(\"<connectString>\");
            '   \\<tableName>Type res = loadTable(#<prettyPrintType(columnTuple)>,con,\"<tableName>\");
            '   closeConnection(con);
            '   return res;
            '}
            '";
    
    return mbody;
}
