include $(top_srcdir)/Makefile.meta
include $(top_srcdir)/Makefile.java.meta

ACLOCAL_AMFLAGS = -I .

SUBDIRS = lib resources

RASCAL_LIBRARY_PATH = $(SGLR_INVOKER_PREFIX)/lib:$(SGLR_PREFIX)/lib:$(ERROR_SUPPORT_PREFIX)/lib:$(CONFIG_SUPPORT_PREFIX)/lib:$(PTABLE_SUPPORT_PREFIX)/lib:$(PT_SUPPORT_PREFIX)/lib:$(ATERM_PREFIX)/lib
RASCAL_PARSETABLE_FILE = $(prefix)/share/rascal.trm.tbl

bin_SCRIPTS = rascal rascal-libpath

CLEANFILES += rascal rascal-libpath

EXTRA_DIST += $(wildcard *.src)

RASCAL_LOCAL_JARS=`echo $(JAVA_LOCAL_JARS) | sed "s@lib/@$(prefix)/share/@g;"`
RASCAL_CLASS_PATH=$(prefix)/share/$(JAVA_JAR):$(RASCAL_LOCAL_JARS):$(EXTERNAL_JARS)

% : %.sh.src Makefile
	sed "s@__RASCAL_LIBRARY_PATH__@$(RASCAL_LIBRARY_PATH)@g;\
	s@__RASCAL_CLASS_PATH__@$(RASCAL_CLASS_PATH)@g;\
	s@__RASCAL_PARSETABLE_FILE__@$(RASCAL_PARSETABLE_FILE)@g;" < $< > $@ && chmod +x $@
