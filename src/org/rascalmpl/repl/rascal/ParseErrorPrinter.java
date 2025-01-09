package org.rascalmpl.repl.rascal;


import java.io.PrintWriter;
import java.util.List;
import java.util.ArrayList;

import org.jline.jansi.Ansi;
import org.jline.jansi.Ansi.Attribute;
import org.rascalmpl.interpreter.utils.ReadEvalPrintDialogMessages;
import org.rascalmpl.parser.gtd.exception.ParseError;
import org.rascalmpl.repl.output.IAnsiCommandOutput;
import org.rascalmpl.repl.output.ICommandOutput;
import org.rascalmpl.repl.output.IErrorCommandOutput;
import org.rascalmpl.repl.output.IOutputPrinter;
import org.rascalmpl.repl.output.MimeTypes;
import org.rascalmpl.repl.output.impl.AsciiStringOutputPrinter;

import io.usethesource.vallang.ISourceLocation;
import io.usethesource.vallang.io.StandardTextWriter;

public class ParseErrorPrinter {

    public static IErrorCommandOutput parseErrorMaybePrompt(ParseError pe, ISourceLocation promptRoot, String input, PrintWriter stdOut, boolean ansiSupported, int promptOffset) {
        if (pe.getLocation().top().equals(promptRoot) && ansiSupported) {
            // it's an prompt root
            return buildPromptError(pe, input, stdOut, promptOffset);
        }
        return new IErrorCommandOutput() {
            @Override
            public ICommandOutput getError() {
                return new ICommandOutput() {
                    @Override
                    public IOutputPrinter asPlain() {
                        return defaultPrinter(pe, promptRoot, input);
                    }
                };
            }
            @Override
            public IOutputPrinter asPlain() {
                return defaultPrinter(pe, promptRoot, input);
            }
            
        };
    }

    private static IOutputPrinter defaultPrinter(ParseError pe, ISourceLocation promptRoot, String input) {
        return new IOutputPrinter() {
            @Override
            public void write(PrintWriter target, boolean unicodeSupported) {
                ReadEvalPrintDialogMessages.parseErrorMessage(target, input, promptRoot.getScheme(), pe, new StandardTextWriter(true)); 
            }
            @Override
            public String mimeType() {
                return MimeTypes.PLAIN_TEXT;
            }
        };
    }
    private static void writeUnderLine(PrintWriter out, int column, String line) {
        writeUnderLine(out, column, line, 0, line.length());
    }

    private static void writeUnderLine(PrintWriter out, int column, String line, int offset, int length) {
        var ansi = Ansi.ansi();
        if (column >= 0) {
            ansi = ansi.cursorToColumn(column);
        }
        out.write(ansi
            .reset()
            .fgRed()
            .bold()
            .a(Attribute.UNDERLINE)
            .a(line, offset, offset + length)
            .reset()
            .toString()
        );
    }
    
    private static IErrorCommandOutput buildPromptError(ParseError pe, String input, PrintWriter stdOut, int promptOffset) {
        // we know we support ansi, so let's rewrite the input for the parts where the error is
        highlightErrorInInput(pe, input, stdOut, promptOffset);


        return new IErrorCommandOutput() {
            @Override
            public ICommandOutput getError() {
                return new IAnsiCommandOutput() {
                    @Override
                    public IOutputPrinter asAnsi() {
                        return new IOutputPrinter() {
                            @Override
                            public void write(PrintWriter target, boolean unicodeSupported) {
                                target.write(unicodeSupported ? "❌ " : "! ");
                                target.write("There was a parse error in the input, see the ");
                                writeUnderLine(target, -1, "highlighted");
                                target.print(" section (line: ");
                                target.print(pe.getBeginLine() - 1);
                                target.print(" column: ");
                                target.print(pe.getBeginColumn());
                                target.println(")");

                            }
                            @Override
                            public String mimeType() {
                                return MimeTypes.ANSI;
                            }
                        };
                    }
                    
                    @Override
                    public IOutputPrinter asPlain() {
                        return new AsciiStringOutputPrinter("! Parse error highlighted");
                    }
                    
                };
            }
            @Override
            public IOutputPrinter asPlain() {
                return new AsciiStringOutputPrinter("! Parse error highlighted");
            }
        };
    }

    /**
     * Overwrite the existing prompt input, and overwrite the sections with highlighted errors
     */
    private static void highlightErrorInInput(ParseError pe, String input, PrintWriter stdOut, int promptOffset) {
        stdOut.write(Ansi.ansi().saveCursorPosition().cursorUpLine().toString());
        try {
            var lines = properSplit(input);
            int currentLine = lines.size();
            while (currentLine > pe.getEndLine()) {
                stdOut.write(Ansi.ansi().cursorUpLine().toString());
                currentLine--;
            }
            while (currentLine >= pe.getBeginLine()) {
                String thisLine = lines.get(currentLine - 1);
                int offset = 0;
                int endOffset = thisLine.length();
                if (pe.getBeginLine() == currentLine) {
                    // first line
                    offset = pe.getBeginColumn();
                }
                if (pe.getEndLine() == currentLine) {
                    // last line
                    endOffset = pe.getEndColumn();
                }
                writeUnderLine(stdOut, promptOffset + offset, thisLine, offset, endOffset - offset);
                stdOut.write(Ansi.ansi().cursorUpLine().toString());
                currentLine--;
            }
        }
        finally {
            stdOut.write(Ansi.ansi().restoreCursorPosition().toString());
        }
    }

    private static List<String> properSplit(String line) {
        List<String> lines = new ArrayList<>();
        int start = 0;
        int end = 0;
        while (end < line.length()) {
            int nextLine = line.indexOf('\n', end);
            if (nextLine == -1) {
                lines.add(line.substring(start));
                break;
            }
            else {
                start = end;
                end = nextLine + 1;
                lines.add(line.substring(start, end - 1));
            }
        }
        if (end == line.length()) {
            // newline at end means ended with an empty line, which would be tru in most cases
            lines.add("");
        }
        return lines;
    } 
    
}
