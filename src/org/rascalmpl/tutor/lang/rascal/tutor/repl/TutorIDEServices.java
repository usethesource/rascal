package org.rascalmpl.tutor.lang.rascal.tutor.repl;

import java.io.PrintWriter;
import java.net.URI;

import org.rascalmpl.ideservices.IDEServices;

import io.usethesource.vallang.ISourceLocation;

public class TutorIDEServices implements IDEServices {

    private final PrintWriter errorWriter;
    
    public TutorIDEServices(PrintWriter errorWriter) {
        this.errorWriter = errorWriter;
    }

    @Override
    public void jobStart(String name, int workShare, int totalWork) {
        
    }

    @Override
    public void jobStep(String name, String message, int workShare) {
        
    }

    @Override
    public int jobEnd(String name, boolean succeeded) {
        return 0;
    }

    @Override
    public boolean jobIsCanceled(String name) {
        return false;
    }

    @Override
    public void jobTodo(String name, int work) {
        
    }

    @Override
    public void warning(String message, ISourceLocation src) {
        
    }

    @Override
    public PrintWriter stderr() {
        return errorWriter;
    }

    @Override
    public void browse(URI uri, String title, int column) {
        
    }

    @Override
    public void edit(ISourceLocation path) {
        
    }

    @Override
    public void endAllJobs() {
        
    }
}
