package org.rascalmpl.repl.completers;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Set;

import org.jline.reader.Candidate;
import org.jline.reader.Completer;
import org.jline.reader.LineReader;
import org.jline.reader.ParsedLine;
import org.rascalmpl.uri.URIResolverRegistry;
import org.rascalmpl.uri.URIUtil;
import org.rascalmpl.values.IRascalValueFactory;

import io.usethesource.vallang.ISourceLocation;
import io.usethesource.vallang.IValueFactory;

public class RascalLocationCompletion implements Completer {

    private static final IValueFactory VF = IRascalValueFactory.getInstance();
    private static final URIResolverRegistry REG = URIResolverRegistry.getInstance();

    @Override
    public void complete(LineReader reader, ParsedLine line, List<Candidate> candidates) {
        if (!line.word().startsWith("|")) {
            return;
        }
        try {
            String locCandidate = line.word().substring(1);
            if (!locCandidate.contains("://")) {
                // only complete scheme
                completeSchema(candidates, locCandidate);
                return;
            }
            if (completeAuthorities(candidates, locCandidate)) {
                // we only had authorities to complete
                return;
            }

            // so we have at least a partial location
            ISourceLocation directory = VF.sourceLocation(new URI(locCandidate));
            String fileName = "";
            if (!REG.isDirectory(directory)) {
                // split filename and directory, to get to the actual directory
                String fullPath = directory.getPath();
                int lastSeparator = fullPath.lastIndexOf('/');
                fileName = fullPath.substring(lastSeparator +  1);
                fullPath = fullPath.substring(0, lastSeparator + 1);
                directory = VF.sourceLocation(directory.getScheme(), directory.getAuthority(), fullPath);
                if (!REG.isDirectory(directory)) {
                    return;
                }
            }
            for (String currentFile : REG.listEntries(directory)) {
                if (currentFile.startsWith(fileName)) {
                    add(candidates, URIUtil.getChildLocation(directory, currentFile));
                }
            }
        }
        catch (URISyntaxException|IOException e) {
        }
    }

    private void add(List<Candidate> candidates, ISourceLocation loc) {
        String locCandidate = loc.toString();
        if (REG.isDirectory(loc)) {
            // remove trailing | so we can continue
            // and add path separator
            locCandidate = locCandidate.substring(0, locCandidate.length() - 1); 
            if (!locCandidate.endsWith("/")) {
                locCandidate += "/";
            }
        }
        candidates.add(new Candidate(locCandidate, locCandidate, "location", null, null, null, false));
    }

    private boolean completeAuthorities(List<Candidate> candidates, String locCandidate) throws URISyntaxException,
        IOException {
        int lastSeparator = locCandidate.lastIndexOf('/');
        if (lastSeparator > 3 && locCandidate.substring(lastSeparator - 2, lastSeparator + 1).equals("://")) {
            // special case, we want to complete authorities (but URI's without a authority are not valid)
            String scheme = locCandidate.substring(0, lastSeparator - 2);
            String partialAuthority = locCandidate.substring(lastSeparator + 1);
            ISourceLocation root = VF.sourceLocation(scheme, "", "");
            for (String candidate: REG.listEntries(root)) {
                if (candidate.startsWith(partialAuthority)) {
                    add(candidates, URIUtil.correctLocation(scheme, candidate, ""));
                }
            }
            return true;
        }
        return false;
    }

    private void completeSchema(List<Candidate> candidates, String locCandidate) {
        filterCandidates(REG.getRegisteredInputSchemes(), candidates, locCandidate);
        filterCandidates(REG.getRegisteredLogicalSchemes(), candidates, locCandidate);
        filterCandidates(REG.getRegisteredOutputSchemes(), candidates, locCandidate);
    }

    private void filterCandidates(Set<String> src, List<Candidate> target, String prefix) {
        for (String s : src) {
            if (s.startsWith(prefix)) {
                add(target, URIUtil.rootLocation(s));
            }
        }
    }
    
}
