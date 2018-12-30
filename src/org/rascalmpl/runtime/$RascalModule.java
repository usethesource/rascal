package org.rascalmpl.core.library.lang.rascalcore.compile.runtime;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.rascalmpl.core.library.lang.rascalcore.compile.runtime.utils.RascalExceptionFactory;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Frame;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.InternalCompilerError;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.RascalExecutionContext;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.RascalRuntimeException;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.ToplevelType;
import org.rascalmpl.uri.SourceLocationURICompare;
import org.rascalmpl.uri.URIResolverRegistry;
import org.rascalmpl.uri.URIUtil;
import org.rascalmpl.values.ValueFactoryFactory;

import io.usethesource.vallang.IBool;
import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.IDateTime;
import io.usethesource.vallang.IInteger;
import io.usethesource.vallang.IList;
import io.usethesource.vallang.IListWriter;
import io.usethesource.vallang.IMap;
import io.usethesource.vallang.IMapWriter;
import io.usethesource.vallang.INode;
import io.usethesource.vallang.INumber;
import io.usethesource.vallang.IRational;
import io.usethesource.vallang.IReal;
import io.usethesource.vallang.ISet;
import io.usethesource.vallang.ISetWriter;
import io.usethesource.vallang.ISourceLocation;
import io.usethesource.vallang.IString;
import io.usethesource.vallang.ITuple;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.IValueFactory;
import io.usethesource.vallang.exceptions.FactTypeUseException;
import io.usethesource.vallang.exceptions.InvalidDateTimeException;
import io.usethesource.vallang.type.Type;
import io.usethesource.vallang.type.TypeFactory;
import io.usethesource.vallang.type.TypeStore;

public class $RascalModule {
	public final static IValueFactory $VF = ValueFactoryFactory.getValueFactory();
	public final static TypeFactory $TF = TypeFactory.getInstance();
	public final static TypeStore $TS = new TypeStore();
	public static final IBool Rascal_TRUE =  $VF.bool(true);
	public static final IBool Rascal_FALSE =  $VF.bool(false);

	// ---- utility methods ---------------------------------------------------

	public static IMap buildMap(IValue...values){
		IMapWriter w = $VF.mapWriter();
		for(int i = 0; i < values.length; i += 2) {
			w.put(values[i], values[i+1]);
		}
		return w.done();
	}

	private static  Integer getOptionalInt(IValue v){
		return v instanceof IInteger ? ((IInteger) v).intValue() : null;
	}

	// ---- add ---------------------------------------------------------------

	public static IValue add(IValue lhs, IValue rhs) {
		ToplevelType lhsType = ToplevelType.getToplevelType(lhs.getType());
		ToplevelType rhsType = ToplevelType.getToplevelType(rhs.getType());
		switch (lhsType) {
		case INT:
			switch (rhsType) {
			case INT:
				return ((IInteger) lhs).add((IInteger) rhs);
			case NUM:
				return ((IInteger) lhs).add((INumber) rhs);
			case REAL:
				return ((IInteger) lhs).add((IReal) rhs);
			case RAT:
				return ((IInteger) lhs).add((IRational) rhs);
			case LIST:
				return ((IList) rhs).insert(lhs);
			case SET:
				return ((ISet) rhs).insert(lhs);
			default:
				throw new InternalCompilerError("$RascalModule add: Illegal type combination: " + lhsType + " and " + rhsType);
			}
		case NUM:
			switch (rhsType) {
			case INT:
				return ((INumber) lhs).add((IInteger) rhs);
			case NUM:
				return ((INumber) lhs).add((INumber) rhs);
			case REAL:
				return ((INumber) lhs).add((IReal) rhs);
			case RAT:
				return ((INumber) lhs).add((IRational) rhs);
			case LIST:
				return ((IList) rhs).insert(lhs);
			case SET:
				return ((ISet) rhs).insert(lhs);
			default:
				throw new InternalCompilerError("$RascalModule add: Illegal type combination: " + lhsType + " and " + rhsType);
			}
		case REAL:
			switch (rhsType) {
			case INT:
				return ((IReal) lhs).add((IInteger) rhs);
			case NUM:
				return ((IReal) lhs).add((INumber) rhs);
			case REAL:
				return ((IReal) lhs).add((IReal) rhs);
			case RAT:
				return ((IReal) lhs).add((IRational) rhs);
			case LIST:
				return ((IList) rhs).insert(lhs);
			case SET:
				return ((ISet) rhs).insert(lhs);
			default:
				throw new InternalCompilerError("$RascalModule add: Illegal type combination: " + lhsType + " and " + rhsType);
			}
		case RAT:
			switch (rhsType) {
			case INT:
				return  ((IRational) lhs).add((IInteger) rhs);
			case NUM:
				return ((IRational) lhs).add((INumber) rhs);
			case REAL:
				return ((IRational) lhs).add((IReal) rhs);
			case RAT:
				return ((IRational) lhs).add((IRational) rhs);
			case LIST:
				return ((IList) rhs).insert(lhs);
			case SET:
				return ((ISet) rhs).insert(lhs);
			default:
				throw new InternalCompilerError("$RascalModule add: Illegal type combination: " + lhsType + " and " + rhsType);
			}
		case SET:
			return ((ISet) lhs).insert(rhs);

		case LIST:
			return ((IList) lhs).append(rhs);

		case LOC:
			switch (rhsType) {
			case STR:
				return aloc_add_astr((ISourceLocation) lhs, (IString) rhs);
			default:
				throw new InternalCompilerError("$RascalModule add: Illegal type combination: " + lhsType + " and " + rhsType);
			}
		case LREL:
			switch (rhsType) {
			case LIST:
			case LREL:
				return ((IList) lhs).concat((IList)rhs);
			default:
				throw new InternalCompilerError("$RascalModule add: Illegal type combination: " + lhsType + " and " + rhsType);
			}
		case MAP:
			switch (rhsType) {
			case MAP:
				return ((IMap) lhs).compose((IMap) rhs);
			default:
				throw new InternalCompilerError("$RascalModule add: Illegal type combination: " + lhsType + " and " + rhsType);
			}
		case REL:
			switch (rhsType) {
			case SET:
			case REL:
				return ((ISet) lhs).union((ISet) rhs);
			default:
				throw new InternalCompilerError("$RascalModule add: Illegal type combination: " + lhsType + " and " + rhsType);
			}
		case STR:
			switch (rhsType) {
			case STR:
				return ((IString) lhs).concat((IString) rhs);
			default:
				throw new InternalCompilerError("$RascalModule add: Illegal type combination: " + lhsType + " and " + rhsType);
			}
		case TUPLE:
			switch (rhsType) {
			case TUPLE:
				return atuple_add_atuple((ITuple) lhs, (ITuple) rhs);
			default:
				throw new InternalCompilerError("$RascalModule add: Illegal type combination: " + lhsType + " and " + rhsType);
			}
		default:
			switch (rhsType) {
			case LIST:
				return ((IList) rhs).insert(lhs);
			case SET:
				return ((ISet) rhs).insert(lhs);
			default:
				throw new InternalCompilerError("$RascalModule add: Illegal type combination: " + lhsType + " and " + rhsType);
			}
		}
	}
	public static ISourceLocation aloc_add_astr(ISourceLocation sloc, IString s) {
		String path = sloc.hasPath() ? sloc.getPath() : "";
		if(!path.endsWith("/")){
			path = path + "/";
		}
		path = path.concat(s.getValue());
		return aloc_field_update(sloc, "path", $VF.string(path));
	}


	public static ITuple atuple_add_atuple(ITuple t1, ITuple t2) {
		int len1 = t1.arity();
		int len2 = t2.arity();
		IValue elems[] = new IValue[len1 + len2];
		for(int i = 0; i < len1; i++)
			elems[i] = t1.get(i);
		for(int i = 0; i < len2; i++)
			elems[len1 + i] = t2.get(i);
		return $VF.tuple(elems);
	}


	// ---- assert_fails ------------------------------------------------------

	public static void assert_fails(IString message) {
		throw RascalExceptionFactory.assertionFailed(message);
	}

	// ---- create ------------------------------------------------------------

	public static ISourceLocation create_aloc(IString uri) {
		try {
			return URIUtil.createFromURI(uri.getValue());
		} 
		catch (URISyntaxException e) {
			// this is actually an unexpected run-time exception since Rascal prevents you from 
			// creating non-encoded 
			throw RascalExceptionFactory.malformedURI(uri.getValue());
		}
		catch (UnsupportedOperationException e) {
			throw RascalExceptionFactory.malformedURI(uri.getValue() + ":" + e.getMessage());
		}
	}

	// ---- divide ------------------------------------------------------------

	public static IValue divide(IValue lhs, IValue rhs) {
		ToplevelType lhsType = ToplevelType.getToplevelType(lhs.getType());
		ToplevelType rhsType = ToplevelType.getToplevelType(rhs.getType());
		switch (lhsType) {
		case INT:
			switch (rhsType) {
			case INT:
				return aint_divide_aint((IInteger) lhs, (IInteger) rhs);
			case NUM:
				return aint_divide_anum((IInteger) lhs, (INumber) rhs);
			case REAL:
				return aint_divide_areal((IInteger) lhs, (IReal) rhs);
			case RAT:
				return aint_divide_arat((IInteger) lhs, (IRational) rhs);
			default:
				throw new InternalCompilerError("$RascalModule divide: Illegal type combination: " + lhsType + " and " + rhsType);
			}
		case NUM:
			switch (rhsType) {
			case INT:
				return anum_divide_aint((INumber) lhs, (IInteger) rhs);
			case NUM:
				return anum_divide_anum((INumber) lhs, (INumber) rhs);
			case REAL:
				return anum_divide_areal((INumber) lhs, (IReal) rhs);
			case RAT:
				return anum_divide_arat((INumber) lhs,  (IRational) rhs);
			default:
				throw new InternalCompilerError("$RascalModule divide: Illegal type combination: " + lhsType + " and " + rhsType);
			}
		case REAL:
			switch (rhsType) {
			case INT:
				return areal_divide_aint((IReal) lhs, (IInteger) rhs);
			case NUM:
				return areal_divide_anum((IReal) lhs, (INumber) rhs);
			case REAL:
				return areal_divide_areal((IReal) lhs, (IReal) rhs);
			case RAT:
				return areal_divide_arat((IReal) lhs, (IRational) rhs);
			default:
				throw new InternalCompilerError("$RascalModule divide: Illegal type combination: " + lhsType + " and " + rhsType);
			}
		case RAT:
			switch (rhsType) {
			case INT:
				return arat_divide_aint((IRational) lhs, (IInteger) rhs);
			case NUM:
				return arat_divide_anum((IRational) lhs, (INumber) rhs);
			case REAL:
				return arat_divide_areal((IRational) lhs, (IReal) rhs);
			case RAT:
				return arat_divide_arat((IRational) lhs, (IRational) rhs);
			default:
				throw new InternalCompilerError("$RascalModule divide: Illegal type combination: " + lhsType + " and " + rhsType);
			}
		default:
			throw new InternalCompilerError("$RascalModule divide: Illegal type combination: " + lhsType + " and " + rhsType);
		}
	}

	public static IInteger aint_divide_aint(IInteger a, IInteger b) {
		try {
			return a.divide(b);
		} catch(ArithmeticException e) {
			throw RascalExceptionFactory.arithmeticException("divide by zero");
		}
	}

	public static INumber aint_divide_areal(IInteger a, IReal b) {
		try {
			return a.multiply($VF.real(1.0)).divide(b,  $VF.getPrecision());
		} catch(ArithmeticException e) {
			throw RascalExceptionFactory.arithmeticException("divide by zero");
		}
	}

	public static IRational aint_divide_arat(IInteger a, IRational b) {
		try {
			return a.toRational().divide(b);
		} catch(ArithmeticException e) {
			throw RascalExceptionFactory.arithmeticException("divide by zero");
		}
	}

	public static INumber aint_divide_anum(IInteger a, INumber b) {
		try {
			return a.multiply($VF.real(1.0)).divide(b, $VF.getPrecision());
		} catch(ArithmeticException e) {
			throw RascalExceptionFactory.arithmeticException("divide by zero");
		}
	}

	public static INumber areal_divide_aint(IReal a, IInteger b) {
		try {
			return a.divide(b, $VF.getPrecision());
		} catch(ArithmeticException e) {
			throw RascalExceptionFactory.arithmeticException("divide by zero");
		}
	}

	public static IReal areal_divide_areal(IReal a, IReal b) {
		try {
			return a.divide(b, $VF.getPrecision());
		} catch(ArithmeticException e) {
			throw RascalExceptionFactory.arithmeticException("divide by zero");
		}
	}

	public static INumber areal_divide_arat(IReal a, IRational b) {
		try {
			return a.divide(b, $VF.getPrecision());
		} catch(ArithmeticException e) {
			throw RascalExceptionFactory.arithmeticException("divide by zero");
		}
	}

	public static INumber areal_divide_anum(IReal a, INumber b) {
		try {
			return a.divide(b, $VF.getPrecision());
		} catch(ArithmeticException e) {
			throw RascalExceptionFactory.arithmeticException("divide by zero");
		}
	}

	public static IRational arat_divide_aint(IRational a, IInteger b) {
		try {
			return a.divide(b);
		} catch(ArithmeticException e) {
			throw RascalExceptionFactory.arithmeticException("divide by zero");
		}
	}

	public static IReal arat_divide_areal(IRational a, IReal b) {
		try {
			return a.multiply($VF.real(1.0)).divide(b,  $VF.getPrecision());
		} catch(ArithmeticException e) {
			throw RascalExceptionFactory.arithmeticException("divide by zero");
		}
	}

	public static IRational arat_divide_arat(IRational a, IRational b) {
		try {
			return a.toRational().divide(b);
		} catch(ArithmeticException e) {
			throw RascalExceptionFactory.arithmeticException("divide by zero");
		}
	}

	public static INumber arat_divide_anum(IRational a, INumber b) {
		try {
			return a.multiply($VF.real(1.0)).divide(b, $VF.getPrecision());
		} catch(ArithmeticException e) {
			throw RascalExceptionFactory.arithmeticException("divide by zero");
		}
	}

	public static INumber anum_divide_aint(INumber a, IInteger b) {
		try {
			return a.divide(b, $VF.getPrecision());
		} catch(ArithmeticException e) {
			throw RascalExceptionFactory.arithmeticException("divide by zero");
		}
	}

	public static INumber anum_divide_areal(INumber a, IReal b) {
		try {
			return a.divide(b, $VF.getPrecision());
		} catch(ArithmeticException e) {
			throw RascalExceptionFactory.arithmeticException("divide by zero");
		}
	}
	public static INumber anum_divide_arat(INumber a, IRational b) {
		try {
			return a.divide(b, $VF.getPrecision());
		} catch(ArithmeticException e) {
			throw RascalExceptionFactory.arithmeticException("divide by zero");
		}
	}

	public static INumber anum_divide_anum(INumber a, INumber b) {
		try {
			return a.divide(b, $VF.getPrecision());
		} catch(ArithmeticException e) {
			throw RascalExceptionFactory.arithmeticException("divide by zero");
		}
	}

	// ---- equal -------------------------------------------------------------

	public static IBool equal(IValue left, IValue right) {
		if(left.getType().isNumber() && right.getType().isNumber()){
			return ((INumber) left).equal((INumber) right);
		} else if(left.getType().isNode() && right.getType().isNode()){
			return ((INode) left).isEqual((INode) right) ? Rascal_TRUE : Rascal_FALSE;
		} else {
			return $VF.bool(left.isEqual(right));
		}
	}

	// ---- field_access ------------------------------------------------------

	public static IValue aloc_field_access(ISourceLocation sloc, String field) {
		IValue v;
		switch (field) {

		case "scheme":
			String s = sloc.getScheme();
			v = $VF.string(s == null ? "" : s);
			break;

		case "authority":
			v = $VF.string(sloc.hasAuthority() ? sloc.getAuthority() : "");
			break;

		case "host":
			if (!URIResolverRegistry.getInstance().supportsHost(sloc)) {
				throw RascalExceptionFactory.noSuchField("The scheme " + sloc.getScheme() + " does not support the host field, use authority instead.");
			}
			s = sloc.getURI().getHost();
			v = $VF.string(s == null ? "" : s);
			break;

		case "path":
			v = $VF.string(sloc.hasPath() ? sloc.getPath() : "/");
			break;

		case "parent":
			String path = sloc.getPath();
			if (path.equals("") || path.equals("/")) {
				throw RascalExceptionFactory.noParent(sloc);
			}
			int i = path.lastIndexOf("/");

			if (i != -1) {
				path = path.substring(0, i);
				if (sloc.getScheme().equalsIgnoreCase("file")) {
					// there is a special case for file references to windows paths.
					// the root path should end with a / (c:/ not c:)
					if (path.lastIndexOf((int)'/') == 0 && path.endsWith(":")) {
						path += "/";
					}
				}
				v = aloc_field_update(sloc, "path", $VF.string(path));
			} else {
				throw RascalExceptionFactory.noParent(sloc);
			}
			break;	

		case "file": 
			path = sloc.hasPath() ? sloc.getPath() : "";

			i = path.lastIndexOf((int)'/');

			if (i != -1) {
				path = path.substring(i+1);
			}
			v = $VF.string(path);	
			break;

		case "ls":
			ISourceLocation resolved = sloc;
			if(URIResolverRegistry.getInstance().exists(resolved) && URIResolverRegistry.getInstance().isDirectory(resolved)){
				IListWriter w = $VF.listWriter();

				try {
					for (ISourceLocation elem : URIResolverRegistry.getInstance().list(resolved)) {
						w.append(elem);
					}
				}
				catch (FactTypeUseException | IOException e) {
					throw RascalExceptionFactory.io($VF.string(e.getMessage()));
				}

				v = w.done();
				break;
			} else {
				throw RascalExceptionFactory.io($VF.string("You can only access ls on a directory, or a container."));
			}

		case "extension":
			path = sloc.hasPath() ? sloc.getPath() : "";
			i = path.lastIndexOf('.');
			if (i != -1) {
				v = $VF.string(path.substring(i + 1));
			} else {
				v = $VF.string("");
			}
			break;

		case "fragment":
			v = $VF.string(sloc.hasFragment() ? sloc.getFragment() : "");
			break;

		case "query":
			v = $VF.string(sloc.hasQuery() ? sloc.getQuery() : "");
			break;

		case "params":
			String query = sloc.hasQuery() ? sloc.getQuery() : "";
			IMapWriter res = $VF.mapWriter($TF.stringType(), $TF.stringType());

			if (query.length() > 0) {
				String[] params = query.split("&");
				for (String param : params) {
					String[] keyValue = param.split("=");
					res.put($VF.string(keyValue[0]), $VF.string(keyValue[1]));
				}
			}
			v = res.done();
			break;

		case "user":
			if (!URIResolverRegistry.getInstance().supportsHost(sloc)) {
				throw RascalExceptionFactory.noSuchField("The scheme " + sloc.getScheme() + " does not support the user field, use authority instead.");
			}
			s = sloc.getURI().getUserInfo();
			v = $VF.string(s == null ? "" : s);
			break;

		case "port":
			if (!URIResolverRegistry.getInstance().supportsHost(sloc)) {
				throw RascalExceptionFactory.noSuchField("The scheme " + sloc.getScheme() + " does not support the port field, use authority instead.");
			}
			int n = sloc.getURI().getPort();
			v = $VF.integer(n);
			break;	

		case "length":
			if(sloc.hasOffsetLength()){
				v = $VF.integer(sloc.getLength());
				break;
			} else {
				throw RascalExceptionFactory.unavailableInformation("length");
			}

		case "offset":
			if(sloc.hasOffsetLength()){
				v = $VF.integer(sloc.getOffset());
				break;
			} else {
				throw RascalExceptionFactory.unavailableInformation("offset");
			}

		case "begin":
			if(sloc.hasLineColumn()){
				v = $VF.tuple($VF.integer(sloc.getBeginLine()), $VF.integer(sloc.getBeginColumn()));
				break;
			} else {
				throw RascalExceptionFactory.unavailableInformation("begin");
			}
		case "end":
			if(sloc.hasLineColumn()){
				v = $VF.tuple($VF.integer(sloc.getEndLine()), $VF.integer(sloc.getEndColumn()));
				break;
			} else {
				throw RascalExceptionFactory.unavailableInformation("end");
			}

		case "uri":
			v = $VF.string(sloc.getURI().toString());
			break;

		case "top":
			v = sloc.top();
			break;

		default:
			throw RascalExceptionFactory.noSuchField(field);
		}

		return v;
	}

	public IValue adatetime_field_access(final IDateTime dt, String field) {
		IValue v;
		try {
			switch (field) {
			case "isDate":
				v = $VF.bool(dt.isDate());
				break;
			case "isTime":
				v = $VF.bool(dt.isTime());
				break;
			case "isDateTime":
				v = $VF.bool(dt.isDateTime());
				break;
			case "century":
				if (!dt.isTime()) {
					v = $VF.integer(dt.getCentury());
					break;
				}
				throw RascalExceptionFactory.unavailableInformation("Can not retrieve the century on a time value");
			case "year":
				if (!dt.isTime()) {
					v = $VF.integer(dt.getYear());
					break;
				}
				throw RascalExceptionFactory.unavailableInformation("Can not retrieve the year on a time value");

			case "month":
				if (!dt.isTime()) {
					v = $VF.integer(dt.getMonthOfYear());
					break;
				}
				throw RascalExceptionFactory.unavailableInformation("Can not retrieve the month on a time value");
			case "day":
				if (!dt.isTime()) {
					v = $VF.integer(dt.getDayOfMonth());
					break;
				}
				throw RascalExceptionFactory.unavailableInformation("Can not retrieve the day on a time value");
			case "hour":
				if (!dt.isDate()) {
					v = $VF.integer(dt.getHourOfDay());
					break;
				}
				throw RascalExceptionFactory.unavailableInformation("Can not retrieve the hour on a date value");
			case "minute":
				if (!dt.isDate()) {
					v = $VF.integer(dt.getMinuteOfHour());
					break;
				}
				throw RascalExceptionFactory.unavailableInformation("Can not retrieve the minute on a date value");
			case "second":
				if (!dt.isDate()) {
					v = $VF.integer(dt.getSecondOfMinute());
					break;
				}
				throw RascalExceptionFactory.unavailableInformation("Can not retrieve the second on a date value");
			case "millisecond":
				if (!dt.isDate()) {
					v = $VF.integer(dt.getMillisecondsOfSecond());
					break;
				}
				throw RascalExceptionFactory.unavailableInformation("Can not retrieve the millisecond on a date value");
			case "timezoneOffsetHours":
				if (!dt.isDate()) {
					v = $VF.integer(dt.getTimezoneOffsetHours());
					break;
				}
				throw RascalExceptionFactory.unavailableInformation("Can not retrieve the timezone offset hours on a date value");
			case "timezoneOffsetMinutes":
				if (!dt.isDate()) {
					v = $VF.integer(dt.getTimezoneOffsetMinutes());
					break;
				}
				throw RascalExceptionFactory.unavailableInformation("Can not retrieve the timezone offset minutes on a date value");

			case "justDate":
				if (!dt.isTime()) {
					v = $VF.date(dt.getYear(), dt.getMonthOfYear(), dt.getDayOfMonth());
					break;
				}
				throw RascalExceptionFactory.unavailableInformation("Can not retrieve the date component of a time value");
			case "justTime":
				if (!dt.isDate()) {
					v = $VF.time(dt.getHourOfDay(), dt.getMinuteOfHour(), dt.getSecondOfMinute(), 
							dt.getMillisecondsOfSecond(), dt.getTimezoneOffsetHours(),
							dt.getTimezoneOffsetMinutes());
					break;
				}
				throw RascalExceptionFactory.unavailableInformation("Can not retrieve the time component of a date value");
			default:
				throw RascalExceptionFactory.noSuchField(field);
			}
			return v;

		} catch (InvalidDateTimeException e) {
			throw RascalExceptionFactory.invalidArgument(dt);
		}
	}

	// ---- field_project -----------------------------------------------------

	@SuppressWarnings("deprecation")
	public static IValue atuple_field_project(ITuple tup, IValue... fields) {
		int n = fields.length;
		IValue [] newFields = new IValue[n];
		for(int i = 0; i < n; i++){
			IValue field = fields[i];
			newFields[i] = field.getType().isInteger() ? tup.get(((IInteger) field).intValue())
					: tup.get(((IString) field).getValue());
		}
		return (n - 1 > 1) ? $VF.tuple(newFields) : newFields[0];
	}

	public static ISet amap_field_project (IMap map, IValue... fields) {
		ISetWriter w = $VF.setWriter();
		int indexArity = fields.length;
		int intFields[] = new int[indexArity];
		for(int i = 1; i < indexArity; i++){
			intFields[i]  = ((IInteger) fields[i]).intValue();
		}
		IValue[] elems = new IValue[indexArity];
		Iterator<Entry<IValue,IValue>> iter = map.entryIterator();
		while (iter.hasNext()) {
			Entry<IValue,IValue> entry = iter.next();
			for(int j = 0; j < fields.length; j++){
				elems[j] = intFields[j] == 0 ? entry.getKey() : entry.getValue();
			}
			w.insert((indexArity > 1) ? $VF.tuple(elems) : elems[0]);
		}
		return w.done();
	}


	public static ISet arel_field_project(ISet set, IValue... fields) {
		int indexArity = fields.length;
		int intFields[] = new int[indexArity];
		for(int i = 1; i < indexArity; i++){
			intFields[i]  = ((IInteger) fields[i]).intValue();
		}

		return set.asRelation().project(intFields);
	}

	public static IList alrel_field_project(IList lrel, IValue... fields) {
		int indexArity = fields.length;
		int intFields[] = new int[indexArity];
		for(int i = 1; i < indexArity; i++){
			intFields[i]  = ((IInteger) fields[i]).intValue();
		}
		IListWriter w = $VF.listWriter();
		IValue[] elems = new IValue[indexArity];
		for(IValue vtup : lrel){
			ITuple tup = (ITuple) vtup;
			for(int j = 0; j < fields.length; j++){
				elems[j] = tup.get(intFields[j]);
			}
			w.append((indexArity > 1) ? $VF.tuple(elems) : elems[0]);
		}
		return w.done();
	}

	// ---- field_update ------------------------------------------------------

	private static ISourceLocation aloc_field_update(final ISourceLocation sloc, final String field, final IValue repl) {		
		Type replType = repl.getType();

		int iLength = sloc.hasOffsetLength() ? sloc.getLength() : -1;
		int iOffset = sloc.hasOffsetLength() ? sloc.getOffset() : -1;
		int iBeginLine = sloc.hasLineColumn() ? sloc.getBeginLine() : -1;
		int iBeginColumn = sloc.hasLineColumn() ? sloc.getBeginColumn() : -1;
		int iEndLine = sloc.hasLineColumn() ? sloc.getEndLine() : -1;
		int iEndColumn = sloc.hasLineColumn() ? sloc.getEndColumn() : -1;
		URI uri;
		boolean uriPartChanged = false;
		String scheme = sloc.getScheme();
		String authority = sloc.hasAuthority() ? sloc.getAuthority() : "";
		String path = sloc.hasPath() ? sloc.getPath() : null;
		String query = sloc.hasQuery() ? sloc.getQuery() : null;
		String fragment = sloc.hasFragment() ? sloc.getFragment() : null;

		try {
			String newStringValue = null;
			if(replType.isString()){
				newStringValue = ((IString)repl).getValue();
			}

			switch (field) {

			case "uri":
				uri = URIUtil.createFromEncoded(newStringValue);
				// now destruct it again
				scheme = uri.getScheme();
				authority = uri.getAuthority();
				path = uri.getPath();
				query = uri.getQuery();
				fragment = uri.getFragment();
				uriPartChanged = true;
				break;

			case "scheme":
				scheme = newStringValue;
				uriPartChanged = true;
				break;

			case "authority":
				authority = newStringValue;
				uriPartChanged = true;
				break;

			case "host":
				if (!URIResolverRegistry.getInstance().supportsHost(sloc)) {
					throw RascalExceptionFactory.noSuchField("The scheme " + sloc.getScheme() + " does not support the host field, use authority instead.");
				}
				uri = URIUtil.changeHost(sloc.getURI(), newStringValue);
				authority = uri.getAuthority();
				uriPartChanged = true;
				break;

			case "path":
				path = newStringValue;
				uriPartChanged = true;
				break;

			case "file": 
				int i = path.lastIndexOf("/");

				if (i != -1) {
					path = path.substring(0, i) + "/" + newStringValue;
				}
				else {
					path = path + "/" + newStringValue;	
				}	
				uriPartChanged = true;
				break;

			case "parent":
				i = path.lastIndexOf("/");
				String parent = newStringValue;
				if (i != -1) {
					path = parent + path.substring(i);
				}
				else {
					path = parent;	
				}
				uriPartChanged = true;
				break;	

			case "ls":
				throw RascalExceptionFactory.noSuchField("Cannot update the children of a location");

			case "extension":
				String ext = newStringValue;

				if (path.length() > 1) {
					int index = path.lastIndexOf('.');

					if (index == -1 && !ext.isEmpty()) {
						path = path + (!ext.startsWith(".") ? "." : "") + ext;
					}
					else if (!ext.isEmpty()) {
						path = path.substring(0, index) + (!ext.startsWith(".") ? "." : "") + ext;
					}
					else {
						path = path.substring(0, index);
					}
				}
				uriPartChanged = true;
				break;

			case "top":
				if (replType.isString()) {
					uri = URIUtil.assumeCorrect(newStringValue);
					scheme = uri.getScheme();
					authority = uri.getAuthority();
					path = uri.getPath();
					query = uri.getQuery();
					fragment = uri.getFragment();
				}
				else if (replType.isSourceLocation()) {
					ISourceLocation rep = (ISourceLocation) repl;
					scheme = rep.getScheme();
					authority = rep.hasAuthority() ? rep.getAuthority() : null;
					path = rep.hasPath() ? rep.getPath() : null;
					query = rep.hasQuery() ? rep.getQuery() : null;
					fragment = rep.hasFragment() ? rep.getFragment() : null;
				}
				uriPartChanged = true;
				break;

			case "fragment":
				fragment = newStringValue;
				uriPartChanged = true;
				break;

			case "query":
				query = newStringValue;
				uriPartChanged = true;
				break;

			case "user":
				if (!URIResolverRegistry.getInstance().supportsHost(sloc)) {
					throw RascalExceptionFactory.noSuchField("The scheme " + sloc.getScheme() + " does not support the user field, use authority instead.");
				}
				uri = sloc.getURI();
				if (uri.getHost() != null) {
					uri = URIUtil.changeUserInformation(uri, newStringValue);
				}

				authority = uri.getAuthority();
				uriPartChanged = true;
				break;

			case "port":
				if (!URIResolverRegistry.getInstance().supportsHost(sloc)) {
					throw RascalExceptionFactory.noSuchField("The scheme " + sloc.getScheme() + " does not support the port field, use authority instead.");
				}
				if (sloc.getURI().getHost() != null) {
					int port = Integer.parseInt(((IInteger) repl).getStringRepresentation());
					uri = URIUtil.changePort(sloc.getURI(), port);
				}
				authority = sloc.getURI().getAuthority();
				uriPartChanged = true;
				break;	

			case "length":
				iLength = ((IInteger) repl).intValue();
				if (iLength < 0) {
					throw RascalExceptionFactory.invalidArgument(repl);
				}
				break;

			case "offset":
				iOffset = ((IInteger) repl).intValue();
				if (iOffset < 0) {
					throw RascalExceptionFactory.invalidArgument(repl);
				}
				break;

			case "begin":
				iBeginLine = ((IInteger) ((ITuple) repl).get(0)).intValue();
				iBeginColumn = ((IInteger) ((ITuple) repl).get(1)).intValue();

				if (iBeginColumn < 0 || iBeginLine < 0) {
					throw RascalExceptionFactory.invalidArgument(repl);
				}
				break;
			case "end":
				iEndLine = ((IInteger) ((ITuple) repl).get(0)).intValue();
				iEndColumn = ((IInteger) ((ITuple) repl).get(1)).intValue();

				if (iEndColumn < 0 || iEndLine < 0) {
					throw RascalExceptionFactory.invalidArgument(repl);
				}
				break;			

			default:
				throw RascalExceptionFactory.noSuchField("Modification of field " + field + " in location not allowed");
			}

			ISourceLocation newLoc = sloc;
			if (uriPartChanged) {
				newLoc = $VF.sourceLocation(scheme, authority, path, query, fragment);
			}

			if (sloc.hasLineColumn()) {
				// was a complete loc, and thus will be now
				return $VF.sourceLocation(newLoc, iOffset, iLength, iBeginLine, iEndLine, iBeginColumn, iEndColumn);
			}

			if (sloc.hasOffsetLength()) {
				// was a partial loc

				if (iBeginLine != -1 || iBeginColumn != -1) {
					//will be complete now.
					iEndLine = iBeginLine;
					iEndColumn = iBeginColumn;
					return $VF.sourceLocation(newLoc, iOffset, iLength, iBeginLine, iEndLine, iBeginColumn, iEndColumn);
				}
				else if (iEndLine != -1 || iEndColumn != -1) {
					// will be complete now.
					iBeginLine = iEndLine;
					iBeginColumn = iEndColumn;
					return $VF.sourceLocation(newLoc, iOffset, iLength, iBeginLine, iEndLine, iBeginColumn, iEndColumn);
				}
				else {
					// remains a partial loc
					return $VF.sourceLocation(newLoc, iOffset, iLength);
				}
			}

			// used to have no offset/length or line/column info, if we are here

			if (iBeginColumn != -1 || iEndColumn != -1 || iBeginLine != -1 || iBeginColumn != -1) {
				// trying to add line/column info to a uri that has no offset length
				throw RascalExceptionFactory.invalidUseOfLocation("Can not add line/column information without offset/length");
			}

			// trying to set offset that was not there before, adding length automatically
			if (iOffset != -1 ) {
				if (iLength == -1) {
					iLength = 0;
				}
			}

			// trying to set length that was not there before, adding offset automatically
			if (iLength != -1) {
				if (iOffset == -1) {
					iOffset = 0;
				}
			}

			if (iOffset != -1 || iLength != -1) {
				// used not to no offset/length, but do now
				return $VF.sourceLocation(newLoc, iOffset, iLength);
			}

			// no updates to offset/length or line/column, and did not used to have any either:
			return newLoc;

		} catch (IllegalArgumentException e) {
			throw RascalExceptionFactory.invalidArgument();
		} catch (URISyntaxException e) {
			throw RascalExceptionFactory.malformedURI(e.getMessage());
		}
	}

	public static IDateTime adatetime_field_update(IDateTime dt, String field, IValue repl) {
		// Individual fields
		int year = dt.getYear();
		int month = dt.getMonthOfYear();
		int day = dt.getDayOfMonth();
		int hour = dt.getHourOfDay();
		int minute = dt.getMinuteOfHour();
		int second = dt.getSecondOfMinute();
		int milli = dt.getMillisecondsOfSecond();
		int tzOffsetHour = dt.getTimezoneOffsetHours();
		int tzOffsetMin = dt.getTimezoneOffsetMinutes();

		try {
			switch (field) {

			case "year":
				if (dt.isTime()) {
					throw RascalExceptionFactory.invalidUseOfTime("Can not update the year on a time value");
				}
				year = ((IInteger)repl).intValue();
				break;

			case "month":
				if (dt.isTime()) {
					throw RascalExceptionFactory.invalidUseOfTime("Can not update the month on a time value");
				}
				month = ((IInteger)repl).intValue();
				break;

			case "day":
				if (dt.isTime()) {
					throw RascalExceptionFactory.invalidUseOfTime("Can not update the day on a time value");
				}	
				day = ((IInteger)repl).intValue();
				break;

			case "hour":
				if (dt.isDate()) {
					throw RascalExceptionFactory.invalidUseOfDate("Can not update the hour on a date value");
				}	
				hour = ((IInteger)repl).intValue();
				break;

			case "minute":
				if (dt.isDate()) {
					throw RascalExceptionFactory.invalidUseOfDate("Can not update the minute on a date value");
				}
				minute = ((IInteger)repl).intValue();
				break;

			case "second":
				if (dt.isDate()) {
					throw RascalExceptionFactory.invalidUseOfDate("Can not update the second on a date value");
				}
				second = ((IInteger)repl).intValue();
				break;

			case "millisecond":
				if (dt.isDate()) {
					throw RascalExceptionFactory.invalidUseOfDate("Can not update the millisecond on a date value");
				}
				milli = ((IInteger)repl).intValue();
				break;

			case "timezoneOffsetHours":
				if (dt.isDate()) {
					throw RascalExceptionFactory.invalidUseOfDate("Can not update the timezone offset hours on a date value");
				}
				tzOffsetHour = ((IInteger)repl).intValue();
				break;

			case "timezoneOffsetMinutes":
				if (dt.isDate()) {
					throw RascalExceptionFactory.invalidUseOfDate("Can not update the timezone offset minutes on a date value");
				}
				tzOffsetMin = ((IInteger)repl).intValue();
				break;			

			default:
				throw RascalExceptionFactory.noSuchField(field);
			}
			IDateTime newdt = null;
			if (dt.isDate()) {
				newdt = $VF.date(year, month, day);
			} else if (dt.isTime()) {
				newdt = $VF.time(hour, minute, second, milli, tzOffsetHour, tzOffsetMin);
			} else {
				newdt = $VF.datetime(year, month, day, hour, minute, second, milli, tzOffsetHour, tzOffsetMin);
			}
			return newdt;
		}
		catch (IllegalArgumentException e) {
			throw RascalExceptionFactory.invalidArgument(repl, currentFrame, "Cannot update field " + field + ", this would generate an invalid datetime value"));
		}
		catch (InvalidDateTimeException e) {
			rex.getFrameObserver().exception(currentFrame,  RascalRuntimeException.invalidArgument(dt, currentFrame, e.getMessage()));
		}
	}


	// ---- intersect ---------------------------------------------------------

	public static IValue intersect(IValue left, IValue right) {
		Type leftType = left.getType();
		Type rightType = right.getType();

		switch (ToplevelType.getToplevelType(leftType)) {
		case LIST:
			switch (ToplevelType.getToplevelType(rightType)) {
			case LIST:
			case LREL:
				return ((IList) left).intersect((IList) right);
			default:
				throw new InternalCompilerError("intersect: illegal combination " + leftType + " and " + rightType);
			}
		case SET:
			switch (ToplevelType.getToplevelType(rightType)) {
			case SET:
			case REL:
				return ((ISet) left).intersect((ISet) right);
			default:
				throw new InternalCompilerError("intersect: illegal combination " + leftType + " and " + rightType);
			}
		case MAP:
			return ((IMap) left).common((IMap) right);

		default:
			throw new InternalCompilerError("intersect: illegal combination " + leftType + " and " + rightType);
		}
	}

	// ---- join --------------------------------------------------------------

	public static IList alist_join_alrel(IList left, IList right){
		if(left.length() == 0){
			return left;
		}
		if(right.length() == 0){
			return right;
		}
		Type rightType = right.get(0).getType();
		assert rightType.isTuple();

		int rarity = rightType.getArity();
		IValue fieldValues[] = new IValue[1 + rarity];
		IListWriter w = $VF.listWriter();

		for (IValue lval : left){
			fieldValues[0] = lval;
			for (IValue rtuple: right) {
				for (int i = 0; i < rarity; i++) {
					fieldValues[i + 1] = ((ITuple)rtuple).get(i);
				}
				w.append($VF.tuple(fieldValues));
			}
		}
		return w.done();
	}

	public static IList alrel_join_alrel(IList left, IList right){
		if(left.length() == 0){
			return left;
		}
		if(right.length() == 0){
			return right;
		}
		Type leftType = left.get(0).getType();
		Type rightType = right.get(0).getType();
		assert leftType.isTuple();
		assert rightType.isTuple();

		int larity = leftType.getArity();
		int rarity = rightType.getArity();
		IValue fieldValues[] = new IValue[larity + rarity];
		IListWriter w = $VF.listWriter();

		for (IValue ltuple : left){
			for (IValue rtuple: right) {
				for (int i = 0; i < larity; i++) {
					fieldValues[i] = ((ITuple)ltuple).get(i);
				}
				for (int i = larity; i < larity + rarity; i++) {
					fieldValues[i] = ((ITuple)rtuple).get(i - larity);
				}
				w.append($VF.tuple(fieldValues));
			}
		}
		return w.done();
	}

	public static IList alrel_join_alist(IList left, IList right){
		if(left.length() == 0){
			return left;
		}
		if(right.length() == 0){
			return right;
		}
		Type leftType = left.get(0).getType();
		assert leftType.isTuple();

		int larity = leftType.getArity();
		IValue fieldValues[] = new IValue[larity + 1];
		IListWriter w = $VF.listWriter();

		for (IValue ltuple : left){
			for (IValue rval: right) {
				for (int i = 0; i < larity; i++) {
					fieldValues[i] = ((ITuple)ltuple).get(i);
				}
				fieldValues[larity] = rval;
				w.append($VF.tuple(fieldValues));
			}
		}
		return w.done();
	}

	public static ISet aset_join_arel(ISet left, ISet right){
		if(left.size() == 0){
			return left;
		}
		if(right.size() == 0){
			return right;
		}
		Type rightType = right.getElementType();
		assert rightType.isTuple();

		int rarity = rightType.getArity();
		IValue fieldValues[] = new IValue[1 + rarity];
		ISetWriter w = $VF.setWriter();

		for (IValue lval : left){
			for (IValue rtuple: right) {
				fieldValues[0] = lval;
				for (int i = 0; i <  rarity; i++) {
					fieldValues[i + 1] = ((ITuple)rtuple).get(i);
				}
				w.insert($VF.tuple(fieldValues));
			}
		}
		return w.done();
	}

	public static ISet arel_join_arel(ISet left, ISet right){
		if(left.size() == 0){
			return left;
		}
		if(right.size() == 0){
			return right;
		}
		Type leftType = left.getElementType();
		Type rightType = right.getElementType();
		assert leftType.isTuple();
		assert rightType.isTuple();

		int larity = leftType.getArity();
		int rarity = rightType.getArity();
		IValue fieldValues[] = new IValue[larity + rarity];
		ISetWriter w = $VF.setWriter();

		for (IValue ltuple : left){
			for (IValue rtuple: right) {
				for (int i = 0; i < larity; i++) {
					fieldValues[i] = ((ITuple)ltuple).get(i);
				}
				for (int i = larity; i < larity + rarity; i++) {
					fieldValues[i] = ((ITuple)rtuple).get(i - larity);
				}
				w.insert($VF.tuple(fieldValues));
			}
		}
		return w.done();
	}

	public static ISet arel_join_aset(ISet left, ISet right){

		if(left.size() == 0){
			return left;
		}
		if(right.size() == 0){
			return right;
		}
		Type leftType = left.getElementType();
		assert leftType.isTuple();

		int larity = leftType.getArity();
		IValue fieldValues[] = new IValue[larity + 1];
		ISetWriter w = $VF.setWriter();

		for (IValue ltuple : left){
			for (IValue rval: right) {
				for (int i = 0; i < larity; i++) {
					fieldValues[i] = ((ITuple)ltuple).get(i);
				}
				fieldValues[larity] = rval;
				w.insert($VF.tuple(fieldValues));
			}
		}
		return w.done();
	}

	// ---- less --------------------------------------------------------------

	public static IBool less(IValue left, IValue right){

		Type leftType = left.getType();
		Type rightType = right.getType();

		if (leftType.isSubtypeOf($TF.numberType()) && rightType.isSubtypeOf($TF.numberType())) {
			return ((INumber)left).less((INumber)right);
		}

		if(!leftType.comparable(rightType)){
			return Rascal_FALSE;
		}

		switch (ToplevelType.getToplevelType(leftType)) {
		// TODO: is this really faster than a TypeVisitor?? No because getTopLevelType includes a TypeVisitor itself.
		// TODO: check type of right
		case BOOL:
			return abool_less_abool((IBool)left, (IBool)right);
		case STR:
			return astr_less_astr((IString)left, (IString)right);
		case DATETIME:
			return adatetime_less_adatetime((IDateTime)left, (IDateTime)right);
		case LOC:
			return aloc_less_aloc((ISourceLocation)left, (ISourceLocation)right);
		case LIST:
		case LREL:
			return alist_less_alist((IList)left, (IList)right);
		case SET:
		case REL:
			return aset_less_aset((ISet)left, (ISet)right);
		case MAP:
			return amap_less_amap((IMap)left, (IMap)right);
		case CONSTRUCTOR:
		case NODE:
			return anode_less_anode((INode)left, (INode)right);
		case ADT:
			//return aadt_less_aadt((IAbstractDataType)left, right);
		case TUPLE:
			return atuple_less_atuple((ITuple)left, (ITuple)right);
		default:
			throw new InternalCompilerError("less: unexpected type " + leftType);
		}
	}

	public static IBool abool_less_abool(IBool left, IBool right) {
		return  $VF.bool(!left.getValue() && right.getValue());
	}

	public static IBool astr_less_astr(IString left, IString right) {
		return $VF.bool(left.compare(right) == -1);
	}

	public static IBool adatetime_less_adatetime(IDateTime left, IDateTime right) {
		return $VF.bool(left.compareTo(right) == -1);
	}

	public static IBool aloc_less_aloc(ISourceLocation left, ISourceLocation right) {
		int compare = SourceLocationURICompare.compare(left, right);
		if (compare < 0) {
			return Rascal_TRUE;
		}
		else if (compare > 0) {
			return Rascal_FALSE;
		}

		// but the uri's are the same
		// note that line/column information is superfluous and does not matter for ordering

		if (left.hasOffsetLength()) {
			if (!right.hasOffsetLength()) {
				return Rascal_FALSE;
			}

			int roffset = right.getOffset();
			int rlen = right.getLength();
			int loffset = left.getOffset();
			int llen = left.getLength();

			if (loffset == roffset) {
				return $VF.bool(llen < rlen);
			}
			return $VF.bool(roffset < loffset && roffset + rlen >= loffset + llen);
		}
		else if (compare == 0) {
			return Rascal_FALSE;
		}

		if (!right.hasOffsetLength()) {
			throw new InternalCompilerError("offset length missing");
		}
		return Rascal_FALSE;
	}

	public static IBool atuple_less_atuple(ITuple left, ITuple right) {
		int leftArity = left.arity();
		int rightArity = right.arity();

		for (int i = 0; i < Math.min(leftArity, rightArity); i++) {
			Object result;
			if(leftArity < rightArity || i < leftArity - 1)
				result = equal(left.get(i), right.get(i));
			else
				result = less(left.get(i), right.get(i));

			if(!((IBool)result).getValue()){
				return Rascal_FALSE;
			}
		}

		return $VF.bool(leftArity <= rightArity);
	}

	public static IBool anode_less_anode(INode left, INode right) {
		int compare = left.getName().compareTo(right.getName());

		if (compare <= -1) {
			return Rascal_TRUE;
		}

		if (compare >= 1){
			return Rascal_FALSE;
		}

		// if the names are not ordered, then we order lexicographically on the arguments:

		int leftArity = left.arity();
		int rightArity = right.arity();

		Object result =  Rascal_FALSE;
		for (int i = 0; i < Math.min(leftArity, rightArity); i++) {

			if(leftArity < rightArity || i < leftArity - 1)
				result = lessequal(left.get(i), right.get(i));
			else
				result = less(left.get(i), right.get(i));

			if(!((IBool)result).getValue()){
				return Rascal_FALSE;
			}
		}

		if (!left.mayHaveKeywordParameters() && !right.mayHaveKeywordParameters()) {
			if (left.asAnnotatable().hasAnnotations() || right.asAnnotatable().hasAnnotations()) {
				// bail out 
				return Rascal_FALSE;
			}
		}

		if (!left.asWithKeywordParameters().hasParameters() && right.asWithKeywordParameters().hasParameters()) {
			return Rascal_TRUE;
		}

		if (left.asWithKeywordParameters().hasParameters() && !right.asWithKeywordParameters().hasParameters()) {
			return Rascal_FALSE;
		}

		if (left.asWithKeywordParameters().hasParameters() && right.asWithKeywordParameters().hasParameters()) {
			Map<String, IValue> paramsLeft = left.asWithKeywordParameters().getParameters();
			Map<String, IValue> paramsRight = right.asWithKeywordParameters().getParameters();
			if (paramsLeft.size() < paramsRight.size()) {
				return Rascal_TRUE;
			}
			if (paramsLeft.size() > paramsRight.size()) {
				return Rascal_FALSE;
			}
			if (paramsRight.keySet().containsAll(paramsLeft.keySet()) && !paramsRight.keySet().equals(paramsLeft.keySet())) {
				return Rascal_TRUE;
			}
			if (paramsLeft.keySet().containsAll(paramsLeft.keySet()) && !paramsRight.keySet().equals(paramsLeft.keySet())) {
				return Rascal_FALSE;
			}
			//assert paramsLeft.keySet().equals(paramsRight.keySet());
			for (String k: paramsLeft.keySet()) {
				result = less(paramsLeft.get(k), paramsRight.get(k));

				if(!((IBool)result).getValue()){
					return Rascal_FALSE;
				}
			}
		}

		return $VF.bool((leftArity < rightArity) || ((IBool)result).getValue());
	}

	public static IBool alist_less_alist(IList left, IList right) {
		if(left.length() > right.length()){
			return Rascal_FALSE;
		}
		OUTER:for (int l = 0, r = 0; l < left.length(); l++) {
			for (r = Math.max(l, r) ; r < right.length(); r++) {
				if (left.get(l).isEqual(right.get(r))) {
					r++;
					continue OUTER;
				}
			}
			return Rascal_FALSE;
		}
		return $VF.bool(left.length() != right.length());
	}

	public static IBool aset_less_aset(ISet left, ISet right) {
		return $VF.bool(!left.isEqual(right) && left.isSubsetOf(right));
	}

	public static IBool amap_less_amap(IMap left, IMap right) {
		return $VF.bool(left.isSubMap(right) && !right.isSubMap(left));
	}

	// ---- lessequal ---------------------------------------------------------

	public static IBool lessequal(IValue left, IValue right){

		Type leftType = left.getType();
		Type rightType = right.getType();

		if (leftType.isSubtypeOf($TF.numberType()) && rightType.isSubtypeOf($TF.numberType())) {
			return ((INumber)left).lessEqual((INumber)right);
		}

		if(!leftType.comparable(rightType)){
			return Rascal_FALSE;
		}

		switch (ToplevelType.getToplevelType(leftType)) {
		// TODO: is this really faster than a TypeVisitor?? No because getTopLevelType includes a TypeVisitor itself.
		// TODO: check type of right
		case BOOL:
			return abool_lessequal_abool((IBool)left, (IBool)right);
		case STR:
			return astr_lessequal_astr((IString)left, (IString)right);
		case DATETIME:
			return adatetime_lessequal_adatetime((IDateTime)left, (IDateTime)right);
		case LOC:
			return aloc_lessequal_aloc((ISourceLocation)left, (ISourceLocation)right);
		case LIST:
		case LREL:
			return alist_lessequal_alist((IList)left, (IList)right);
		case SET:
		case REL:
			return aset_lessequal_aset((ISet)left, (ISet)right);
		case MAP:
			return amap_lessequal_amap((IMap)left, (IMap)right);
		case CONSTRUCTOR:
		case NODE:
			return anode_lessequal_anode((INode)left, (INode)right);
		case ADT:
			//return aadt_lessequal_aadt((IAbstractDataType)left, right);
		case TUPLE:
			return atuple_lessequal_atuple((ITuple)left, (ITuple)right);
		default:
			throw new InternalCompilerError("less: unexpected type " + leftType);
		}
	}

	public static IBool abool_lessequal_abool(IBool left, IBool right) {
		boolean l = left.getValue();
		boolean r = right.getValue();
		return $VF.bool((!l && r) || (l == r));
	}

	public static IBool astr_lessequal_astr(IString left, IString right) {
		int c = right.compare(left);
		return $VF.bool(c == -1 || c == 0);
	}

	public static IBool adatetime_lessequal_adatetime(IDateTime left, IDateTime right) {
		int c = left.compareTo(right);
		return $VF.bool(c== -1 || c == 0);
	}


	public static IBool aloc_lessequal_aloc(ISourceLocation left, ISourceLocation right) {
		int compare = SourceLocationURICompare.compare(left, right);
		if (compare < 0) {
			return Rascal_TRUE;
		}
		else if (compare > 0) {
			return Rascal_FALSE;
		}

		// but the uri's are the same
		// note that line/column information is superfluous and does not matter for ordering

		if (left.hasOffsetLength()) {
			if (!right.hasOffsetLength()) {
				return Rascal_FALSE;
			}

			int roffset = right.getOffset();
			int rlen = right.getLength();
			int loffset = left.getOffset();
			int llen = left.getLength();

			if (loffset == roffset) {
				return $VF.bool(llen <= rlen);
			}
			return $VF.bool(roffset < loffset && roffset + rlen >= loffset + llen);
		}
		else if (compare == 0) {
			return Rascal_TRUE;
		}

		if (!right.hasOffsetLength()) {
			throw new InternalCompilerError("missing offset length");
		}
		return Rascal_FALSE;
	}

	public static IBool anode_lessequal_anode(INode left, INode right) {
		int compare = left.getName().compareTo(right.getName());

		if (compare <= -1) {
			return Rascal_TRUE;
		}

		if (compare >= 1){
			return Rascal_FALSE;
		}

		// if the names are not ordered, then we order lexicographically on the arguments:

		int leftArity = left.arity();
		int rightArity = right.arity();

		for (int i = 0; i < Math.min(leftArity, rightArity); i++) {
			if(!lessequal(left.get(i), right.get(i)).getValue()){
				return Rascal_FALSE;
			}
		}
		return $VF.bool(leftArity <= rightArity);
	}

	public static IBool atuple_lessequal_atuple(ITuple left, ITuple right) {
		int leftArity = left.arity();
		int rightArity = right.arity();

		for (int i = 0; i < Math.min(leftArity, rightArity); i++) {			
			if(!lessequal(left.get(i), right.get(i)).getValue()){
				return Rascal_FALSE;
			}
		}

		return $VF.bool(leftArity <= rightArity);
	}

	public static IBool alist_lessequal_alist(IList left, IList right) {
		if (left.length() == 0) {
			return Rascal_TRUE;
		}
		else if (left.length() > right.length()) {
			return Rascal_FALSE;
		}

		OUTER:for (int l = 0, r = 0; l < left.length(); l++) {
			for (r = Math.max(l, r) ; r < right.length(); r++) {
				if (left.get(l).isEqual(right.get(r))) {
					continue OUTER;
				}
			}
			return Rascal_FALSE;
		}

		return $VF.bool(left.length() <= right.length());
	}

	public static IBool aset_lessequal_aset(ISet left, ISet right) {
		return $VF.bool(left.size() == 0 || left.isEqual(right) || left.isSubsetOf(right));
	}

	public static IBool amap_lessequal_amap(IMap left, IMap right) {
		return $VF.bool(left.isSubMap(right));
	}

	// ---- product -----------------------------------------------------------


	public static IValue product(IValue lhs, IValue rhs) {
		ToplevelType lhsType = ToplevelType.getToplevelType(lhs.getType());
		ToplevelType rhsType = ToplevelType.getToplevelType(rhs.getType());
		switch (lhsType) {
		case INT:
			switch (rhsType) {
			case INT:
				return ((IInteger) lhs).multiply((IInteger) rhs);
			case NUM:
				return ((IInteger) lhs).multiply((INumber) rhs);
			case REAL:
				return ((IInteger) lhs).multiply((IReal) rhs);
			case RAT:
				return ((IInteger) lhs).multiply((IRational) rhs);
			default:
				throw new InternalCompilerError("Illegal type combination: " + lhsType + " and " + rhsType);
			}
		case NUM:
			switch (rhsType) {
			case INT:
				return ((INumber) lhs).multiply((IInteger) rhs);
			case NUM:
				return ((INumber) lhs).multiply((INumber) rhs);
			case REAL:
				return ((INumber) lhs).multiply((IReal) rhs);
			case RAT:
				return ((INumber) lhs).multiply((IRational) rhs);
			default:
				throw new InternalCompilerError("Illegal type combination: " + lhsType + " and " + rhsType);
			}
		case REAL:
			switch (rhsType) {
			case INT:
				return ((IReal) lhs).multiply((IInteger) rhs);
			case NUM:
				return ((IReal) lhs).multiply((INumber) rhs);
			case REAL:
				return ((IReal) lhs).multiply((IReal) rhs);
			case RAT:
				return ((IReal) lhs).multiply((IRational) rhs);
			default:
				throw new InternalCompilerError("Illegal type combination: " + lhsType + " and " + rhsType);
			}
		case RAT:
			switch (rhsType) {
			case INT:
				return ((IRational) lhs).multiply((IInteger) rhs);
			case NUM:
				return ((IRational) lhs).multiply((INumber) rhs);
			case REAL:
				return ((IRational) lhs).multiply((IReal) rhs);
			case RAT:
				return ((IRational) lhs).multiply((IRational) rhs);
			default:
				throw new InternalCompilerError("Illegal type combination: " + lhsType + " and " + rhsType);
			}
		default:
			throw new InternalCompilerError("Illegal type combination: " + lhsType + " and " + rhsType);
		}
	}


	public static IList alist_product_alist(IList left, IList right) {
		IListWriter w = $VF.listWriter();
		for(IValue l : left){
			for(IValue r : right){
				w.append($VF.tuple(l,r));
			}
		}
		return w.done();
	}

	public static ISet aset_product_aset(ISet left, ISet right) {
		ISetWriter w = $VF.setWriter();
		for(IValue l : left){
			for(IValue r : right){
				w.insert($VF.tuple(l,r));
			}
		}
		return w.done();
	}

	// ---- slice -------------------------------------------------------------

	public static IString astr_slice(final IString str,  Integer first, Integer second, Integer end){
		SliceDescriptor sd = makeSliceDescriptor(first, second, end, str.length());
		StringBuilder buffer = new StringBuilder();
		int increment = sd.second - sd.first;
		if(sd.first == sd.end || increment == 0){
			// nothing to be done
		} else
			if(sd.first <= sd.end){
				for(int i = sd.first; i >= 0 && i < sd.end; i += increment){
					buffer.appendCodePoint(str.charAt(i));
				}
			} else {
				for(int j = sd.first; j >= 0 && j > sd.end && j < str.length(); j += increment){
					buffer.appendCodePoint(str.charAt(j));
				}
			}
		return $VF.string(buffer.toString());
	}

	public static IList anode_slice(final INode node,  Integer first, Integer second, Integer end){
		SliceDescriptor sd = makeSliceDescriptor(first, second, end, node.arity());
		IListWriter w = $VF.listWriter();
		int increment = sd.second - sd.first;
		if(sd.first == sd.end || increment == 0){
			// nothing to be done
		} else
			if(sd.first <= sd.end){
				for(int i = sd.first; i >= 0 && i < sd.end; i += increment){
					w.append(node.get(i));
				}
			} else {
				for(int j = sd.first; j >= 0 && j > sd.end && j < node.arity(); j += increment){
					w.append(node.get(j));
				}
			}

		return w.done();
	}


	public IList alist_slice(IList lst, Integer first, Integer second, Integer end){
		SliceDescriptor sd = makeSliceDescriptor(first, second, end, lst.length());
		IListWriter w = $VF.listWriter();
		int increment = sd.second - sd.first;
		if(sd.first == sd.end || increment == 0){
			// nothing to be done
		} else
			if(sd.first <= sd.end){
				for(int i = sd.first; i >= 0 && i < sd.end; i += increment){
					w.append(lst.get(i));
				}
			} else {
				for(int j = sd.first; j >= 0 && j > sd.end && j < lst.length(); j += increment){
					w.append(lst.get(j));
				}
			}
		return w.done();
	}

	public static IList $makeSlice(final INode node, Integer first, Integer second, Integer end){
		SliceDescriptor sd = makeSliceDescriptor(first, second, end, node.arity());
		IListWriter w = $VF.listWriter();
		int increment = sd.second - sd.first;
		if(sd.first == sd.end || increment == 0){
			// nothing to be done
		} else
			if(sd.first <= sd.end){
				for(int i = sd.first; i >= 0 && i < sd.end; i += increment){
					w.append(node.get(i));
				}
			} else {
				for(int j = sd.first; j >= 0 && j > sd.end && j < node.arity(); j += increment){
					w.append(node.get(j));
				}
			}

		return w.done();
	}

	private static SliceDescriptor makeSliceDescriptor(Integer first, Integer second, Integer end, int len) {
		int firstIndex = 0;
		int secondIndex = 1;
		int endIndex = len;

		if(first != null){
			firstIndex = first;
			if(firstIndex < 0)
				firstIndex += len;
		}
		if(end != null){
			endIndex = end;
			if(endIndex < 0){
				endIndex += len;
			}
		}

		if(second == null){
			secondIndex = firstIndex + ((firstIndex <= endIndex) ? 1 : -1);
		} else {
			secondIndex = second;
			if(secondIndex < 0)
				secondIndex += len;
			if(!(first == null && end == null)){
				if(first == null && secondIndex > endIndex)
					firstIndex = len - 1;
				if(end == null && secondIndex < firstIndex)
					endIndex = -1;
			}
		}

		if(len == 0 || firstIndex >= len){
			firstIndex = secondIndex = endIndex = 0;
		} else if(endIndex > len){
			endIndex = len;
		} 
		//		else if(endIndex == -1){
		//			endIndex = 0;
		//		}

		return new SliceDescriptor(firstIndex, secondIndex, endIndex);
	}

	public static IString astr_slice_replace(IString str, Integer first, Integer second, Integer end, IString repl) {
		SliceDescriptor sd = makeSliceDescriptor(first, second, end, str.length());
		return  str.replace(sd.first, sd.second, sd.end, repl);
	}

	public static INode anode_slice_replace(INode node, Integer first, Integer second, Integer end, IList repl) {
		SliceDescriptor sd = makeSliceDescriptor(first, second, end, node.arity());
		return  node.replace(sd.first, sd.second, sd.end, repl);
	}

	public static IList alist_slice_replace(IList lst, Integer first, Integer second, Integer end, IList repl) {
		SliceDescriptor sd = makeSliceDescriptor(first, second, end, lst.length());
		return  updateListSlice(lst, sd, SliceOperator.replace, repl);
	}

	public static IList alist_slice_add(IList lst, Integer first, Integer second, Integer end, IList repl) {
		SliceDescriptor sd = makeSliceDescriptor(first, second, end, lst.length());
		return  updateListSlice(lst, sd, SliceOperator.add, repl);
	}

	public static IList alist_slice_subtract(IList lst, Integer first, Integer second, Integer end, IList repl) {
		SliceDescriptor sd = makeSliceDescriptor(first, second, end, lst.length());
		return  updateListSlice(lst, sd, SliceOperator.subtract, repl);
	}

	public static IList alist_slice_product(IList lst, Integer first, Integer second, Integer end, IList repl) {
		SliceDescriptor sd = makeSliceDescriptor(first, second, end, lst.length());
		return  updateListSlice(lst, sd, SliceOperator.product, repl);
	}

	public static IList alist_slice_divide(IList lst, Integer first, Integer second, Integer end, IList repl) {
		SliceDescriptor sd = makeSliceDescriptor(first, second, end, lst.length());
		return  updateListSlice(lst, sd, SliceOperator.divide, repl);
	}

	public static IList updateListSlice(final IList lst, final SliceDescriptor sd, final SliceOperator op, final IList repl){
		IListWriter w = $VF.listWriter();
		int increment = sd.second - sd.first;
		int replIndex = 0;
		int rlen = repl.length();
		boolean wrapped = false;
		if(sd.first == sd.end || increment == 0){
			// nothing to be done
		} else
			if(sd.first <= sd.end){
				assert increment > 0;
				int listIndex = 0;
				while(listIndex < sd.first){
					w.append(lst.get(listIndex++));
				}
				while(listIndex >= 0 && listIndex < sd.end){
					w.append(op.execute(lst.get(listIndex), repl.get(replIndex++)));
					if(replIndex == rlen){
						replIndex = 0;
						wrapped = true;
					}
					for(int q = 1; q < increment && listIndex + q < sd.end; q++){
						w.append(lst.get(listIndex + q));
					}
					listIndex += increment;
				}
				listIndex = sd.end;
				if(!wrapped){
					while(replIndex < rlen){
						w.append(repl.get(replIndex++));
					}
				}
				while(listIndex < lst.length()){
					w.append(lst.get(listIndex++));
				}
			} else {
				assert increment < 0;
				int j = lst.length() - 1;
				while(j > sd.first){
					w.insert(lst.get(j--));
				}
				while(j >= 0 && j > sd.end && j < lst.length()){
					w.insert(op.execute(lst.get(j), repl.get(replIndex++)));
					if(replIndex == rlen){
						replIndex = 0;
						wrapped = true;
					}
					for(int q = -1; q > increment && j + q > sd.end; q--){
						w.insert(lst.get(j + q));
					}
					j += increment;
				}
				j = sd.end;
				if(!wrapped){
					while(replIndex < rlen){
						w.insert(repl.get(replIndex++));
					}
				}

				while(j >= 0){
					w.insert(lst.get(j--));
				}

			}
		return w.done();
	}



	// ---- splice ------------------------------------------------------------
	/**
	 * Splice elements in a list writer
	 * 
	 * IListWriter w, IListOrISet val  => w with val's elements spliced in
	 */
	public static IListWriter listwriter_splice(IListWriter writer, IValue val) {
		if(val instanceof IList){
			IList lst = (IList) val;
			for(IValue v : lst){
				writer.append(v);
			}
		} else if(val instanceof ISet){
			ISet set = (ISet) val;
			for(IValue v : set){
				writer.append(v);
			}
		} else {
			writer.append((IValue) val);
		}
		return writer;
	}

	/**
	 * Splice elements in a set writer
	 * 
	 * ISetWriter w, IListOrISet val => w with val's elements spliced in
	 */

	public static ISetWriter setwriter_splice(ISetWriter writer, IValue val) {
		if(val instanceof IList){
			IList lst = (IList) val;
			for(IValue v : lst){
				writer.insert(v);
			}
		} else if(val instanceof ISet){
			ISet set = (ISet) val;
			for(IValue v : set){
				writer.insert(v);
			}
		} else {
			writer.insert((IValue) val);
		}
		return writer;
	}


	// ---- subscript ---------------------------------------------------------

	public static IString astr_subscript_int(IString str, int idx) {
		try {
			return (idx >= 0) ? str.substring(idx, idx+1)
					: str.substring(str.length() + idx, str.length() + idx + 1);
		} catch(IndexOutOfBoundsException e) {
			throw RascalExceptionFactory.indexOutOfBounds($VF.integer(idx));
		}
	}

	public static IValue atuple_subscript_int(ITuple tup, int idx) {
		try {
			return tup.get((idx >= 0) ? idx : tup.arity() + idx);
		} catch(IndexOutOfBoundsException e) {
			throw RascalExceptionFactory.indexOutOfBounds($VF.integer(idx));
		}
	}

	public static IValue anode_subscript_int(INode node, int idx) {
		try {
			if(idx < 0){
				idx =  node.arity() + idx;
			}
			return node.get(idx);  
		} catch(IndexOutOfBoundsException e) {
			throw RascalExceptionFactory.indexOutOfBounds($VF.integer(idx));
		}
	}

	public static IValue aadt_subscript_int(IConstructor cons, int idx) {
		try {
			return cons.get((idx >= 0) ? idx : (cons.arity() + idx));
		} catch(IndexOutOfBoundsException e) {
			throw RascalExceptionFactory.indexOutOfBounds($VF.integer(idx));
		}
	}

	/**
	 * Subscript of a n-ary rel with a single subscript (no set and unequal to _)
	 */
	public static ISet arel_subscript1_noset(ISet rel, IValue idx) {
		if(rel.isEmpty()){
			return rel;
		}
		return rel.asRelation().index(idx);
	}


	/**
	 * Subscript of a binary rel with a single subscript (a set but unequal to _)
	 */
	public ISet arel2_subscript1_aset(ISet rel, ISet idx) {
		if(rel.isEmpty()){
			return rel;
		}
		ISetWriter wset = $VF.setWriter();

		for (IValue v : rel) {
			ITuple tup = (ITuple)v;

			if((((ISet) idx).contains(tup.get(0)))){
				wset.insert(tup.get(1));
			} 
		}
		return wset.done();
	}


	/**
	 * Subscript of an n-ary (n > 2) rel with a single subscript (a set and unequal to _)
	 */
	public ISet arel_subscript1_aset(ISet rel, ISet index) {
		if(rel.isEmpty()){
			return rel;
		}
		int relArity = rel.getElementType().getArity();		

		ISetWriter wset = $VF.setWriter();
		IValue args[] = new IValue[relArity - 1];

		for (IValue v : rel) {
			ITuple tup = (ITuple)v;

			if((((ISet) index).contains(tup.get(0)))){
				for (int i = 1; i < relArity; i++) {
					args[i - 1] = tup.get(i);
				}
				wset.insert($VF.tuple(args));
			} 
		}
		return wset.done();
	}

	/**
	 * Subscript of rel, general case
	 * subsDesc is a subscript descriptor: an array with integers: 0: noset, 1: set, 2: wildcard
	 */

	public static ISet arel_subscript (ISet rel, IValue[] idx, int[] subsDesc) {
		if(rel.isEmpty()){
			return rel;
		}
		int indexArity = idx.length;
		int relArity = rel.getElementType().getArity();

		ISetWriter wset = $VF.setWriter();

		if(relArity - indexArity == 1){	// Return a set
			allValues:
				for (IValue v : rel) {
					ITuple tup = (ITuple)v;
					for(int k = 0; k < indexArity; k++){
						switch(subsDesc[k]){
						case 0: 
							if(!tup.get(k).isEqual(idx[k])) continue allValues; 
							continue;
						case 1: 
							if(!(((ISet)idx[k]).contains(tup.get(k)))) continue allValues;
						}
					}
					wset.insert(tup.get(indexArity));
				}
		} else {						// Return a relation
			IValue args[] = new IValue[relArity - indexArity];
			allValues:
				for (IValue v : rel) {
					ITuple tup = (ITuple)v;
					for(int k = 0; k < indexArity; k++){
						switch(subsDesc[k]){
						case 0: 
							if(!tup.get(k).isEqual(idx[k])) continue allValues; 
							continue;
						case 1: 
							if(!((ISet)idx[k]).contains(tup.get(k))) continue allValues;
						}
					}

					for (int i = indexArity; i < relArity; i++) {
						args[i - indexArity] = tup.get(i);
					}
					wset.insert($VF.tuple(args));
				}
		}

		return wset.done();
	}

	// ---- subtract ----------------------------------------------------------

	public static IValue subtract(IValue lhs, IValue rhs) {
		ToplevelType lhsType = ToplevelType.getToplevelType(lhs.getType());
		ToplevelType rhsType = ToplevelType.getToplevelType(rhs.getType());
		switch (lhsType) {
		case INT:
			switch (rhsType) {
			case INT:
				return ((IInteger) lhs).subtract((IInteger)rhs);
			case NUM:
				return ((IInteger) lhs).subtract((INumber)rhs);
			case REAL:
				return ((IInteger) lhs).subtract((IReal)rhs);
			case RAT:
				return ((IInteger) lhs).subtract((IRational)rhs);
			default:
				throw new InternalCompilerError("Illegal type combination: " + lhsType + " and " + rhsType);
			}
		case NUM:
			switch (rhsType) {
			case INT:
				return ((INumber) lhs).subtract((IInteger)rhs);
			case NUM:
				return ((INumber) lhs).subtract((INumber)rhs);
			case REAL:
				return ((INumber) lhs).subtract((IReal)rhs);
			case RAT:
				return ((INumber) lhs).subtract((IRational)rhs);
			default:
				throw new InternalCompilerError("Illegal type combination: " + lhsType + " and " + rhsType);
			}
		case REAL:
			switch (rhsType) {
			case INT:
				return ((IReal) lhs).subtract((IInteger)rhs);
			case NUM:
				return ((IReal) lhs).subtract((INumber)rhs);
			case REAL:
				return ((IReal) lhs).subtract((IReal)rhs);
			case RAT:
				return ((IReal) lhs).subtract((IRational)rhs);
			default:
				throw new InternalCompilerError("Illegal type combination: " + lhsType + " and " + rhsType);
			}
		case RAT:
			switch (rhsType) {
			case INT:
				return ((IRational) lhs).subtract((IInteger)rhs);
			case NUM:
				return ((IRational) lhs).subtract((INumber)rhs);
			case REAL:
				return ((IRational) lhs).subtract((IReal)rhs);
			case RAT:
				return ((IRational) lhs).subtract((IRational)rhs);
			default:
				throw new InternalCompilerError("Illegal type combination: " + lhsType + " and " + rhsType);
			}
		default:
			throw new InternalCompilerError("Illegal type combination: " + lhsType + " and " + rhsType);
		}
	}


	// ---- update ------------------------------------------------------------

	/**
	 * Update list element
	 * 
	 */

	public static IList alist_update(IList lst, int n, IValue v) {
		if(n < 0){
			n = lst.length() + n;
		}
		try {
			return lst.put(n, v);
		} catch (IndexOutOfBoundsException e){
			throw RascalExceptionFactory.indexOutOfBounds($VF.integer(n));

		}
	}

	/**
	 * Update map element
	 * 
	 */
	public static IMap amap_update(IMap map, IValue key, IValue v) {
		return map.put(key, v);
	}

	/**
	 * Update tuple element
	 * 
	 */

	public static ITuple atuple_update(ITuple tup, int n, IValue v) {
		try {
			return tup.set(n, v);
		} catch (IndexOutOfBoundsException e){
			throw RascalExceptionFactory.indexOutOfBounds($VF.integer(n));

		}
	}

	/**
	 * Update argument of adt constructor by its field name
	 * 
	 */

	public static IConstructor aadt_update(IConstructor cons, IString field, IValue v) {
		return cons.set(field.getValue(), v);
	}
}

enum SliceOperator {
	replace(0) {
		@Override
		public IValue execute(final IValue left, final IValue right) {
			return right;
		}
	},
	add(1) {
		@Override
		public IValue execute(final IValue left, final IValue right) {
			return $RascalModule.add(left, right);
		}
	},
	subtract(2){
		@Override
		public IValue execute(final IValue left, final IValue right) {
			return $RascalModule.subtract(left, right);
		}
	}, 
	product(3){
		@Override
		public IValue execute(final IValue left, final IValue right) {
			return $RascalModule.product(left, right);
		}
	}, 

	divide(4){
		@Override
		public IValue execute(final IValue left, final IValue right) {
			return $RascalModule.divide(left, right);
		}
	}, 

	intersect(5){
		@Override
		public IValue execute(final IValue left, final IValue right) {
			return $RascalModule.intersect(left, right);
		}
	};

	final int operator;

	public static final SliceOperator[] values = SliceOperator.values();

	public static SliceOperator fromInteger(int n) {
		return values[n];
	}

	public abstract IValue execute(final IValue left, final IValue right);

	public static SliceOperator replace() {
		return values[0];
	}

	public static SliceOperator add() {
		return values[1];
	}

	public static SliceOperator subtract() {
		return values[2];
	}

	public static SliceOperator product() {
		return values[3];
	}

	public static SliceOperator divide() {
		return values[4];
	}

	public static SliceOperator intersect() {
		return values[5];
	}

	SliceOperator(int op) {
		this.operator = op;
	}
}