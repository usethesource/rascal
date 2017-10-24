module lang::rascal::boot::Kernel
 
/*
 * The Kernel module collects all Rascal modules that are needed
 * to compile and execute Rascal programs.
 *
 * The Kernel is self-contained and comprehensive: a compiled version of the Kernel
 * is all that is needed for a full bootstrap of Rascal.
 *
 * An up-to-date, compiled version of the Kernel should always reside in the /boot directory 
 * of the Rascal project.
 */
 

// this group of modules takes care of running the compiler
extend experiments::Compiler::Compile;
extend experiments::Compiler::Execute;
extend experiments::Compiler::CompileMuLibrary;
extend lang::rascal::grammar::Bootstrap;

// this for IDE features
extend experiments::Compiler::Summary;
extend lang::rascal::ide::Outline;

// this is for the documentation compiler and help server
extend util::Webserver;
extend experiments::tutor3::QuestionCompiler;
extend experiments::Compiler::RascalExtraction::RascalExtraction;
extend experiments::Compiler::RascalExtraction::ExtractDoc;
extend experiments::Compiler::RascalExtraction::ExtractInfo;