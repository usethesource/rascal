package org.rascalmpl.core.uri.libraries;

import org.rascalmpl.core.uri.AbstractSourceLocationInputOutputAdapter;
import org.rascalmpl.core.uri.file.FileURIResolver;
import io.usethesource.vallang.ISourceLocation;

public class CoursesURIResolver extends AbstractSourceLocationInputOutputAdapter {
    private static final class WriteableCourseResolver extends FileURIResolver {
        private final String courseSrc;

        private WriteableCourseResolver(String courseSrc) {
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
    
    public CoursesURIResolver() {
        super(System.getProperty("rascal.courses") != null ? new WriteableCourseResolver(System.getProperty("rascal.courses")) : new ReadonlyCourseResolver());
    }
}
