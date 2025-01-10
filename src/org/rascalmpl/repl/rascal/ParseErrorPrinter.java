/*
 * Copyright (c) 2023-2025, NWO-I CWI and Swat.engineering
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package org.rascalmpl.repl.rascal;


import java.io.PrintWriter;
import java.util.List;
import java.util.ArrayList;

import org.jline.jansi.Ansi;
import org.jline.jansi.Ansi.Attribute;
import org.jline.terminal.Terminal;
import org.jline.utils.InfoCmp.Capability;
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

/**
 * This class wraps a parse error message. 
 * With a special case for when the ParseError was in the prompt of the REPL, in that case we try to highligh the parse error.
 */
public class ParseErrorPrinter {

    /**
     * Generate an error output for a parse error, and if a prompt, highlight the error in the input
     * @param pe the parse error
     * @param promptRoot the location that the prompt would be parsed under
     * @param input original input text
     * @param term the terminal where we want to print the error location, if supported
     * @param promptOffset position on the line where the prompt ends
     * @return
     */
    public static IErrorCommandOutput parseErrorMaybePrompt(ParseError pe, ISourceLocation promptRoot, String input, Terminal term, int promptOffset) {
        if (pe.getLocation().top().equals(promptRoot) && ansiSupported(term)) {
            // it's an prompt root
            return buildPromptError(pe, input, term.writer(), promptOffset);
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

    private static boolean ansiSupported(Terminal term) {
        return term.getStringCapability(Capability.cursor_down) != null
            && term.getStringCapability(Capability.save_cursor) != null
            && term.getStringCapability(Capability.restore_cursor) != null
            ;
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
                                target.print(" text (line: ");
                                target.print(pe.getBeginLine());
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
            // first we go up untill the end of the error
            while (currentLine > pe.getEndLine()) {
                stdOut.write(Ansi.ansi().cursorUpLine().toString());
                currentLine--;
            }
            // then we highlight the error parts of the lines
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
                if (thisLine.isEmpty()&& endOffset == 0) {
                    // if the error is at the end of the input
                    // put a space there and make the error higlight that space
                    thisLine = " ";
                    endOffset = 1;
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

    /**
     * Work around string.split not generating empty lines
     */
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
