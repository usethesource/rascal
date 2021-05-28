package org.rascalmpl.uri.libraries;

import java.io.IOException;

import org.rascalmpl.uri.AbstractSourceLocationInputOutputAdapter;
import org.rascalmpl.uri.file.FileURIResolver;
import io.usethesource.vallang.ISourceLocation;

public class CoursesURIResolver extends AbstractSourceLocationInputOutputAdapter {
    private static final class WriteableCourseResolver extends FileURIResolver {
        private final String courseSrc;

        private WriteableCourseResolver(String courseSrc) throws IOException {
            this.courseSrc = courseSrc;
        }

        @Override
        public String scheme() {
          return "courses";
        }

        @Override
        protected String getPath(ISourceLocation uri) {
          String path = uri.getPath();
          return courseSrc + (path.startsWith("/") ? path : ("/" + path));
        }
    }
    
    private static class ReadonlyCourseResolver extends ClassResourceInput {
        public ReadonlyCourseResolver() {
            super("courses", ReadonlyCourseResolver.class, "/org/rascalmpl/courses");
        }
    }
    
    public CoursesURIResolver() throws IOException {
        super(System.getProperty("rascal.courses") != null ? new WriteableCourseResolver(System.getProperty("rascal.courses")) : new ReadonlyCourseResolver());
    }
}
