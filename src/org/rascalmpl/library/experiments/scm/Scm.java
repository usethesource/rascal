package org.rascalmpl.library.experiments.scm;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.imp.pdb.facts.IBool;
import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IListWriter;
import org.eclipse.imp.pdb.facts.IMap;
import org.eclipse.imp.pdb.facts.IMapWriter;
import org.eclipse.imp.pdb.facts.ISet;
import org.eclipse.imp.pdb.facts.ISetWriter;
import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.rascalmpl.interpreter.result.RascalFunction;
import org.rascalmpl.library.experiments.scm.ScmTypes.ChangeSet;
import org.rascalmpl.library.experiments.scm.ScmTypes.Info;
import org.rascalmpl.library.experiments.scm.ScmTypes.Repository;
import org.rascalmpl.library.experiments.scm.ScmTypes.Resource;
import org.rascalmpl.library.experiments.scm.ScmTypes.Revision;
import org.rascalmpl.library.experiments.scm.ScmTypes.WcResource;
import org.rascalmpl.library.experiments.scm.cvs.CvsProvider;
import org.rascalmpl.library.experiments.scm.git.GitProvider;
import org.rascalmpl.library.experiments.scm.svn.SvnProvider;

public class Scm {
	
	private static final CvsProvider cvsProvider = new CvsProvider();
	private static final SvnProvider svnProvider = new SvnProvider();
	private static final GitProvider gitProvider = new GitProvider();
		
    public Scm(IValueFactory factory) {
    	
	}
    
    private static ScmProvider<?> getProviderFor(IConstructor repository) {
    	Repository type = Repository.from(repository);
    	switch (type) {
			case CVS:
				return cvsProvider;
			case SVN:
				return svnProvider;
			case GIT:
				return gitProvider;
			default:
				throw new IllegalArgumentException("Can't find the right repository configuration for '" + type + "'");
		}
    }
    
    /**
	 * Checkout the resources of the given repository according to the checkoutUnit.
	 * @param checkoutUnit of type {@link ScmTypes.CheckoutUnit} containing information about the resources to checkout
	 * @param repository of type {@link Repository} containing information about the repository to checkout from.
	 */
    public static void checkoutResources(IConstructor checkoutUnit, IConstructor repository) throws ScmProviderException {
    	getProviderFor(repository).checkoutResources(checkoutUnit, repository);
    }
    
    /**
	 * Gets the resources on the workspace specified by the repository configuration.
	 * @param repository of type {@link Repository} containing information about the repository workspace to get
	 * the resources from.
	 * @return a set of resources as an {@link WcResource} with optionally {@link Revision} and {@link Info} fields.
	 */
    public static ISet getResources(IConstructor repository) throws ScmProviderException {
    	return getProviderFor(repository).getResources(repository);
    }
    
    public static void getChangesets(IConstructor repository, IValue callBack) throws ScmProviderException {
    	if (!(repository.getType().isSubtypeOf(ScmTypes.AbstractDataType.REPOSITORY.getType()))) {
			throw new IllegalArgumentException("configuration should be of the type REPOSITORY");
		}
    	if (!(callBack instanceof RascalFunction)) {
			throw new IllegalArgumentException("extractFacts should be of the type RascalFunction");
		}
    	System.out.println("void getChangesets(" + repository + ", " + callBack + ")");
    	try {
    		ScmLogEntryHandler<?> handler = getProviderFor(repository).extractLogs(repository, (RascalFunction) callBack, null);
    	} catch (Exception e) {
    		e.printStackTrace();
    		System.out.println("StopTimer:" + Timer.stopTimer());
    	}
    }
    
    public static IList getChangesets(IConstructor repository) {
    	if (!(repository.getType().isSubtypeOf(ScmTypes.AbstractDataType.REPOSITORY.getType()))) {
			throw new IllegalArgumentException("configuration should be of the type REPOSITORY");
		}
    	IListWriter writer = ScmTypes.VF.listWriter(ChangeSet.getAbstractType());
    	System.out.println("IList getChangesets(" + repository + ")");
    	
    	try {
    		ScmLogEntryHandler<?> handler = getProviderFor(repository).extractLogs(repository, null, writer);
    	} catch (Exception e) {
    		e.printStackTrace();
    		System.out.println("StopTimer:" + Timer.stopTimer());
    	}
    	
    	return writer.done();
    }
    
    public static ISet mergeRevisions(ISet resources) {
    	
    	
    	return null;
    }
    
    public static ISet buildResourceTree(ISet resourceFiles) {
    	return buildResourceTree(resourceFiles, ScmTypes.VF.map(ScmTypes.TF.sourceLocationType(), ScmTypes.Resource.getAbstractType()));
    }
    
	public static ISet buildResourceTree(ISet resourceFiles, IMap resourceDirs) {
		Map<String, Set<IConstructor>> directoryFiles = new HashMap<String, Set<IConstructor>>();
		
		for (IValue iValue : resourceFiles) {
			IConstructor file = (IConstructor) iValue;
			String path = Resource.getId(file).getURI().getPath();
			int dirIndex = path.lastIndexOf(File.separatorChar);
			
			path = path.substring(0, dirIndex);
			
			Set<IConstructor> dirFiles = directoryFiles.get(path);
			if(dirFiles == null) {
				dirFiles = new HashSet<IConstructor>();
				directoryFiles.put(path, dirFiles);
			}
			dirFiles.add(file);
		}
		
		Map<String, Set<String>> subDirectories = new HashMap<String, Set<String>>();
		Set<String> roots = new HashSet<String>();
		for (String subDir : directoryFiles.keySet()) {
			String parent;
			String child = subDir;
			int dirIndex;
			while(true) {
				dirIndex = child.lastIndexOf(File.separatorChar);
//				if (dirIndex < 0) {
//					roots.add(child);
//					break;
//				}
				parent = child.substring(0, dirIndex);
				if (parent.equals("")) {
					roots.add(child);
					break;
				}
				
				Set<String> childs = subDirectories.get(parent);
				if (childs == null) {
					childs = new HashSet<String>();
					subDirectories.put(parent, childs);
				}
				
				childs.add(child);
				child = parent;
			}
		}
	
		ISetWriter rootResources = ScmTypes.VF.setWriter(ScmTypes.AbstractDataType.RESOURCE.getType());
		for (String root : roots) {
			IConstructor rootResource = buildResourceTree(root, resourceDirs, directoryFiles, subDirectories);
			rootResources.insert(rootResource);
		}
		return rootResources.done();
	}
	/**
	 * files:
	 * A/readme.txt
	 * A/B/file.txt
	 * A/B/readme.txt
	 * A/C/info.txt
	 * A/C/D/k.txt
	 * B/help.txt
	 * 
	 * directoryFiles:
	 * A		-> A/readme.txt
	 * A/B 		-> A/B/file.txt
	 *        	   A/B/readme.txt
	 * A/C		-> A/C/info.txt
	 * A/C/D	-> A/C/D/k.txt
	 * B		-> B/help.txt
	 * 
	 * roots:
	 * A
	 * B
	 * 
	 * subDirectories:
	 * A 	->  A/B
	 * 			A/C
	 * 			B
	 * A/C	->	A/C/D
	 * 
	 * resourceTree
	 * A 		-> 	A/readme.txt
	 * 				A/B
	 * 				A/C
	 * A/B		->	A/B/file.txt
	 *        		A/B/readme.txt
	 * A/C  	->  A/C/info.txt
	 * A/C/D	-> 	A/C/D/k.txt
	 * B		->	B/help.txt
	 */
	private static IConstructor buildResourceTree(String root, IMap resourceDirs,
			Map<String, Set<IConstructor>> directoryFiles, Map<String, Set<String>> subDirectories) {
		
		ISetWriter rootResources = ScmTypes.VF.setWriter(ScmTypes.AbstractDataType.RESOURCE.getType());
		
		if (directoryFiles.containsKey(root)) {
			for (IConstructor file : directoryFiles.get(root)) {
				rootResources.insert(file);
			}
		}
		
		if (subDirectories.containsKey(root)) {
			for (String subDir : subDirectories.get(root)) {
				IConstructor subDirConstructor = buildResourceTree(subDir, resourceDirs, directoryFiles, subDirectories);
				rootResources.insert(subDirConstructor);
			}
		}
		ISourceLocation rootLocation = ScmTypes.VF.sourceLocation(Scm.encodePath(root));
		ISet content = rootResources.done();
//		if (resourceDirs.containsKey(rootLocation)) {
//			IConstructor resource = (IConstructor)resourceDirs.get(rootLocation);
//			IConstructor revision = ScmTypes.Resource.getRevision(resource);
//			IConstructor change = ScmTypes.Resource.getChange(resource);
//			return Resource.FOLDER_REV_CONTENT.make(rootLocation, revision, change, content);
//		} else {
			
//		}
		
		return content.size() > 0 ? Resource.FOLDER_CONTENT.make(rootLocation, content) : Resource.FOLDER.make(rootLocation);
	}

    public static String encodePath(String path) {
    	if (path.indexOf('{') >= 0 || path.indexOf('}') >= 0 || path.indexOf(' ') >= 0) {
			StringBuilder builder = new StringBuilder();
			for (int i = 0; i < path.length(); i++) {
				char c = path.charAt(i);
				switch (c) {
					case '{':
						builder.append("%7B");
						break;
					case '}':
						builder.append("%7D");
						break;
					case ' ':
						builder.append("%20");
						break;
					default:
						builder.append(c);
						break;
				}
			}
			return builder.toString();
		}
    	return path;
    }
    
    /**
     * Creates a resource id with the given path and filename.
     * @param workspace path not ending with a / or \
     * @param filePath not starting with a / or \
     * @return the full path to the file as an ISourceLocation
     */
    public static ISourceLocation createResourceId(String workspace, String filePath) {
    	return ScmTypes.VF.sourceLocation(workspace + "/" + Scm.encodePath(filePath));
    }

    public static IBool isDirectory(ISourceLocation location) {
    	return ScmTypes.VF.bool(new File(location.getURI().getPath()).isDirectory());
    }

    public static IList listFilesAndDirs(ISourceLocation directory) {
    	return listFilesAndDirs(directory, (FileFilter) null);
    }
    
    public static IList listFiles(ISourceLocation directory) {
    	return listFilesAndDirs(directory, new FileFilter() {
			public boolean accept(File pathname) {
				return pathname.isFile();
			}
		});
    }
    
    public static IList listDirs(ISourceLocation directory) {
    	return listFilesAndDirs(directory, new FileFilter() {
			public boolean accept(File pathname) {
				return pathname.isDirectory();
			}
		});
    }
    
    public static IList listFiles(ISourceLocation directory, IString filterRegex) {
    	
    	final String regex = filterRegex.getValue();
    	FileFilter fileFilter = new FileFilter() {
			public boolean accept(File pathname) {
				/*System.out.println("File:" + pathname + " has path '" + 
					pathname.getPath() + "' and abs: '" + 
					pathname.getAbsolutePath() + "'");*/
				return !pathname.isDirectory() && pathname.getPath().matches(regex);
			}
		};
		
    	return listFilesAndDirs(directory, fileFilter);
    }
    
    public static IList listDirs(ISourceLocation directory, IString filterRegex) {
    	
    	final String regex = filterRegex.getValue();
    	FileFilter fileFilter = new FileFilter() {
			public boolean accept(File pathname) {
				return pathname.isDirectory() && pathname.getPath().matches(regex);
			}
		};
		
    	return listFilesAndDirs(directory, fileFilter);
    }
    
    /**
     * Lists the content of the given directory.
     * @param directory to list the direct sub files and folders of
     * @param filter if null, no filter will be used
     * @return the list of direct subfiles and subdirs of the given path.
     */
    private static IList listFilesAndDirs(ISourceLocation directory, FileFilter filter) {
    	IListWriter writer = ScmTypes.VF.listWriter(ScmTypes.TF.sourceLocationType());
    	String path = directory.getURI().getPath();
    	File dir = new File(path);
    	if (!dir.isDirectory()) {
    		throw new IllegalArgumentException(directory + " is not a directory.");
    	}
    	
    	
    	
    	File[] files = filter == null ? dir.listFiles() : dir.listFiles(filter);
    	for (File file : files) {
    		writer.append(ScmTypes.VF.sourceLocation(Scm.encodePath(file.getAbsolutePath())));
		}
    	return writer.done();
    }
    
    public static IMap linesCount(ISet locations) throws ScmProviderException {
    	IMapWriter results = ScmTypes.VF.mapWriter(Resource.getAbstractType(), ScmTypes.TF.integerType());
    	
    	for (IValue iValue : locations) {
    		IConstructor resource = (IConstructor) iValue;
			ISourceLocation loc = Resource.getId(resource);
			FileReader fr = null;
	    	LineNumberReader lnr = null;
			try {
				File file = new File(loc.getURI());
				if (file.isDirectory()) {
					System.err.println("Skipping " + file + " because it's directory");
					continue;
				}
				fr = new FileReader(file);
				lnr = new LineNumberReader(fr);
				
				while (lnr.readLine() != null) {}
				
				results.put(resource, ScmTypes.VF.integer(lnr.getLineNumber()));
			} catch (FileNotFoundException e) {
				System.err.println("Cannot find the file, just skipping it:" + e.getMessage());
				//throw new ScmProviderException(e.getMessage(), e);
			} catch (IOException e) {
				System.err.println("IO Exception, just skipping the current file:" + e.getMessage());
				//throw new ScmProviderException(e.getMessage(), e);
			} finally {
				if (fr != null) {
					try {
						fr.close();
					} catch (IOException e) {
						throw new ScmProviderException(e.getMessage(), e);
					}
	            }
		        if (lnr != null) {
		        	try {
						lnr.close();
					} catch (IOException e) {
						throw new ScmProviderException(e.getMessage(), e);
					}
		        }
			}
		}
    	return results.done();
    }

	
    
}