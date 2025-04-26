package org.rascalmpl.shell;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.rascalmpl.interpreter.staticErrors.CommandlineError;
import org.rascalmpl.library.util.PathConfig;
import org.rascalmpl.library.util.PathConfig.RascalConfigMode;
import org.rascalmpl.uri.URIResolverRegistry;
import org.rascalmpl.uri.URIUtil;
import org.rascalmpl.values.IRascalValueFactory;
import org.rascalmpl.values.functions.IFunction;

import io.usethesource.vallang.IList;
import io.usethesource.vallang.IListWriter;
import io.usethesource.vallang.ISetWriter;
import io.usethesource.vallang.ISourceLocation;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.IValueFactory;
import io.usethesource.vallang.exceptions.FactTypeUseException;
import io.usethesource.vallang.io.StandardTextReader;
import io.usethesource.vallang.type.Type;
import io.usethesource.vallang.type.TypeFactory;

import static org.rascalmpl.uri.file.MavenRepositoryURIResolver.mavenize;
import static org.rascalmpl.uri.jar.JarURIResolver.jarify;

public class CommandlineParser {
    private final IValueFactory vf = IRascalValueFactory.getInstance();
    private final TypeFactory tf = TypeFactory.getInstance();
    private final PrintWriter out;
        
    public CommandlineParser(PrintWriter out) {
        this.out = out;
    }

    /**
     * Turns a String[] into a list[str]
     */
    public IList parsePlainCommandLineArgs(String[] commandline) {
        IListWriter w = vf.listWriter();
        for (String arg : commandline) {
            w.append(vf.string(arg));
        }
        return w.done();
    }

    /**
     * Turns the String[] into a keyword parameter map for passing to an main IFunction.
     */
    public Map<String, IValue> parseKeywordCommandLineArgs(String toolName, String[] commandline, IFunction func) {
        if (func.getType().getFieldTypes().getArity() > 0) {
            throw new CommandlineError("main function should only have keyword parameters.", func.getType().getKeywordParameterTypes(), toolName);
        }

        return parseKeywordCommandLineArgs(toolName, commandline, func.getType().getKeywordParameterTypes());
    }

    /**
     * Turns the String[] into a keyword parameter map for passing to an main IFunction.
     */
    public Map<String, IValue> parseKeywordCommandLineArgs(String name, String[] commandline, Type kwTypes) {
        Map<String, Type> expectedTypes = new HashMap<>();

        List<String> pathConfigParam = new LinkedList<>();
        String pathConfigName = null;

        for (String kwp : kwTypes.getFieldNames()) {
            var kwtype = kwTypes.getFieldType(kwp);

            if (kwtype == PathConfig.PathConfigType) {
                // special case for PathConfig unfolds into the respective fields of PathConfig
                expectedTypes.putAll(PathConfig.PathConfigFields);    
                // add project parameter for automatic pathconfig settings
                expectedTypes.put("project", tf.sourceLocationType());
                // drop the path config parameter
                pathConfigParam.add(kwp);
                pathConfigName = kwp;
            }
            else {
                expectedTypes.put(kwp, kwtype);
            }
        }

        for (String param : pathConfigParam) {
            expectedTypes.remove(param);
        }

        Map<String, IValue> params = new HashMap<>();

        for (int i = 0; i < commandline.length; i++) {
            if (List.of("-help", "--help", "/?", "?", "\\?", "-?", "--?").contains(commandline[i].trim())) {
                printMainHelpMessage(kwTypes);
                System.exit(0);
            }
            else if (commandline[i].startsWith("-")) {
                String label = commandline[i].replaceFirst("^-+", "");
                Type expected = expectedTypes.get(label);

                if (expected == null) {
                    throw new CommandlineError("unknown argument: " + label, kwTypes, name);
                }

                if (expected.isSubtypeOf(tf.boolType())) {
                    if (i == commandline.length - 1 || commandline[i+1].startsWith("-")) {
                        params.put(label, vf.bool(true));
                    }
                    else if (i < commandline.length - 1) {
                        String arg = commandline[++i].trim();
                        if (arg.equals("1") || arg.equals("true")) {
                            params.put(label, vf.bool(true));
                        }
                        else {
                            params.put(label, vf.bool(false));
                        }
                    }

                    continue;
                }
                else if (i == commandline.length - 1 || commandline[i+1].startsWith("-")) {
                    throw new CommandlineError("expected option for " + label, kwTypes, name);
                }
                else if (expected.isSubtypeOf(tf.listType(tf.sourceLocationType()))) {
                    if (!commandline[i+1].startsWith("-") && commandline[i+1].matches(".*" + File.pathSeparator + "(?!//).*")) {
                        i++;
        
                        // we want to split on ; or : but not on ://
                        String[] pathElems = commandline[i].trim().split(File.pathSeparator + "(?!//)");
                        IListWriter writer = vf.listWriter();
                        
                        Arrays.stream(pathElems).forEach(e -> {
                            writer.append(parseCommandlineOption(name, tf.sourceLocationType(), e));
                        });

                        params.put(label, writer.done());
                    }
                    else {
                        IListWriter writer = vf.listWriter();

                        while (i + 1 < commandline.length && !commandline[i+1].startsWith("-")) {
                            writer.append(parseCommandlineOption(name, expected.getElementType(), commandline[++i]));
                        }

                        params.put(label, writer.done());
                    }
                }
                else if (expected.isSubtypeOf(tf.listType(tf.valueType()))) {
                    IListWriter writer = vf.listWriter();

                    while (i + 1 < commandline.length && !commandline[i+1].startsWith("-")) {
                        writer.append(parseCommandlineOption(name, expected.getElementType(), commandline[++i]));
                    }

                    params.put(label, writer.done());
                }
                else if (expected.isSubtypeOf(tf.setType(tf.valueType()))) {
                    ISetWriter writer = vf.setWriter();

                    while (i + 1 < commandline.length && !commandline[i+1].startsWith("-")) {
                        writer.insert(parseCommandlineOption(name, expected.getElementType(), commandline[++i]));
                    }

                    params.put(label, writer.done());
                }
                else {
                    params.put(label, parseCommandlineOption(name, expected, commandline[++i]));
                }
            }
        }

        if (params.get("project") != null && pathConfigName != null) {
            // we have a project that can use automatic detection of PathConfig parameters.
            var pcfg = PathConfig.fromSourceProjectRascalManifest((ISourceLocation) params.get("project"), RascalConfigMode.INTERPRETER, true);
            var cons = pcfg.asConstructor();

            for (Entry<String, Type> entry : PathConfig.PathConfigFields.entrySet()) {
                if (params.get(entry.getKey()) != null) {
                    if (entry.getValue() == tf.sourceLocationType()) {
                        // normal loc fields overwrite the automatic ones (bin, generatedSources)
                        cons = cons.asWithKeywordParameters().setParameter(entry.getKey(), params.get(entry.getKey()));
                    }
                    else {
                        // list[loc] fields get concatenated (ignores, srcs, libs)
                        IList param1 = (IList) cons.asWithKeywordParameters().getParameter(entry.getKey());
                        if (param1 == null) {
                            param1 = vf.list();
                        }
                        IList param2 = (IList) params.get(entry.getKey());
                        if (param2 == null) {
                            param2 = vf.list();
                        }
                        cons = cons.asWithKeywordParameters().setParameter(entry.getKey(), param1.concat(param2));
                    }
                }
            }

            params.put(pathConfigName, cons);
        }
        else if (pathConfigName != null) {
            // Fold back the PathConfig-specific parameters,
            // into a fresh pathConfig constructor, and remove them from the general list.
            var pcfg = new PathConfig().asConstructor();

            for (Entry<String, Type> e : PathConfig.PathConfigFields.entrySet()) {
                var value = params.get(e.getKey());
                if (value != null) {
                    pcfg = pcfg.asWithKeywordParameters().setParameter(e.getKey(), value);
                    params.remove(e.getKey());
                }
            }

            params.put(pathConfigName, pcfg);
        }

        if (pathConfigName != null) {
            // normalize entries in libs and check srcs using mavenize and jarify, to make them into directories,
            // if they are not directories already.
            var reg = URIResolverRegistry.getInstance();
            var pcfg = params.get(pathConfigName);
            IList libs = (IList) pcfg.asWithKeywordParameters().get("libs");
            if (libs != null) {
                libs = libs.stream()
                    .map(ISourceLocation.class::cast)
                    .map(l -> reg.isDirectory(l) ? l : jarify(mavenize(l)))
                    .collect(vf.listWriter());
                pcfg = pcfg.asWithKeywordParameters().setParameter("libs", libs);
            }

            IList srcs = (IList) pcfg.asWithKeywordParameters().getParameter("srcs");
            if (srcs != null) {
                srcs = srcs.stream()
                    .map(ISourceLocation.class::cast)
                    .map(l -> reg.isDirectory(l) ? l : jarify(mavenize(l)))
                    .collect(vf.listWriter());
                pcfg = pcfg.asWithKeywordParameters().setParameter("srcs", srcs);
            }

            params.put(pathConfigName, pcfg);
        }

        return params;
    }

    private void printMainHelpMessage(Type kwTypes) {
        StackTraceElement trace[] = Thread.currentThread().getStackTrace();
        String mainClass = trace[trace.length - 1].getClassName();
        Map<String, Type> fields = new HashMap<>();
        
        out.println("Help for " + mainClass + "\n");

        for (int i = 0; i < kwTypes.getArity(); i++) {
            fields.put(kwTypes.getFieldName(i), kwTypes.getFieldType(i));
        }

        if (fields.containsValue(PathConfig.PathConfigType)) {
            fields.putAll(PathConfig.PathConfigFields);
            fields.put("project", tf.sourceLocationType());

            // drop the pcfg field
            var pcfgsKeys = fields.entrySet().stream()
                .filter(e -> e.getValue() == PathConfig.PathConfigType)
                .map(e -> e.getKey())
                .collect(Collectors.toList());
            pcfgsKeys.stream().forEach(k -> fields.remove(k));
        }

        if (fields.isEmpty()) {
            out.println("\t-help    this help message is printed.");
            out.println();
            out.println("This command has no further parameters.");
            return;
        }

        List<String> keys = fields.keySet().stream().collect(Collectors.toList());
        keys.sort(String::compareTo);
        int maxLength = keys.stream().max((a,b) -> Integer.compare(a.length(), b.length())).get().length();

        for (String key : keys) {
            String explanation = parameterTypeExplanation(key, fields.get(key));
            out.println("    -" + String.format("%-" + maxLength + "." + key.length() + "s", key) + ": " + explanation);
        }
    }

    private String parameterTypeExplanation(String key, Type type) {
        if (key.equals("help") || key.equals("h") || key.equals("?")) {
            return "this help message is printed.";
        }
        else if (type == tf.boolType()) {
            return "true or false or nothing";
        }
        else if (type == tf.stringType()) {
            return "any string value. Use \" or \' to include spaces.";
        }
        else if (type == tf.sourceLocationType()) {
            return "a path, a URI, or a Rascal source location";
        }
        else if (type == tf.listType(tf.sourceLocationType())) {
            return "a list of paths, URIs or Rascal locs separated by " + File.pathSeparator ;
        }
        else if (type.isSubtypeOf(tf.numberType())) {
            return "a numerical value";
        }
        else {
            return "a Rascal value expression of " + type;
        }
    }

    private IValue parseCommandlineOption(String toolName, Type expected, String option) {
        if (expected.isSubtypeOf(tf.stringType())) {
            // this accepts anything as a (unquoted, unescaped) string
            return vf.string(option);
        }
        else if (expected.isSubtypeOf(tf.sourceLocationType())) {
            // locations are for file and folder paths. in 3 different notations
            try {
                if (option.trim().startsWith("|") && option.trim().endsWith("|")) {
                    // vallang syntax for locs with |scheme:///|
                    return (ISourceLocation) new StandardTextReader().read(vf, expected, new StringReader(option.trim()));
                }
                else if (option.contains("://")) {
                    // encoded URI notation
                    return URIUtil.createFromURI(option.trim());
                }
                else {
                    // basic support for current and parent directory notation
                    if (option.trim().equals(".")) {
                        return URIUtil.rootLocation("cwd");
                    }
                    else if (option.trim().equals("..")) {
                        return URIUtil.correctLocation("cwd", "", "..");
                    }
                    else if (option.trim().startsWith(".." + File.separatorChar)) {
                        return parseCommandlineOption(toolName, expected, System.getProperty("user.dir") + File.separatorChar + option);
                    }
                    else if (option.trim().startsWith("." + File.separatorChar)) {
                        return parseCommandlineOption(toolName, expected, System.getProperty("user.dir") + option.substring(1));
                    }
                    // OS specific notation for file paths
                    return URIUtil.createFileLocation(option.trim());
                }
            }  
            catch (FactTypeUseException e) {
                throw new CommandlineError("expected " + expected + " but got " + option + " (" + e.getMessage() + ")", expected, toolName);
            } 
            catch (IOException e) {
                throw new CommandlineError("unxped problem while parsing commandline:" + e.getMessage(), expected, toolName);
            }     
            catch (URISyntaxException e) {
                throw new CommandlineError("expected " + expected + " but got " + option + " (" + e.getMessage() + ")", expected, toolName);
            }
        }
    
        // otherwise we use the vallang parser:
        StringReader reader = new StringReader(option);
        try {
            return new StandardTextReader().read(vf, expected, reader);
        } catch (FactTypeUseException e) {
            throw new CommandlineError("expected " + expected + " but got " + option + " (" + e.getMessage() + ")", expected, toolName);
        } catch (IOException e) {
            throw new CommandlineError("unexpected problem while parsing commandline:" + e.getMessage(), expected, toolName);
        }
    }
}
