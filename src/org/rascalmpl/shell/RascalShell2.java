/*
Copyright (c) 2024, Swat.engineering
All rights reserved. 
  
Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met: 
  
1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer. 
  
2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution. 
  
THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. 
*/

package org.rascalmpl.shell;

import static org.rascalmpl.interpreter.utils.ReadEvalPrintDialogMessages.parseErrorMessage;
import static org.rascalmpl.interpreter.utils.ReadEvalPrintDialogMessages.staticErrorMessage;
import static org.rascalmpl.interpreter.utils.ReadEvalPrintDialogMessages.throwMessage;
import static org.rascalmpl.interpreter.utils.ReadEvalPrintDialogMessages.throwableMessage;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.input.NullInputStream;
import org.jline.jansi.Ansi;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.impl.completer.StringsCompleter;
import org.jline.reader.impl.history.DefaultHistory;
import org.jline.style.StyleResolver;
import org.jline.terminal.TerminalBuilder;
import org.rascalmpl.exceptions.Throw;
import org.rascalmpl.ideservices.BasicIDEServices;
import org.rascalmpl.ideservices.IDEServices;
import org.rascalmpl.interpreter.Evaluator;
import org.rascalmpl.interpreter.control_exceptions.InterruptException;
import org.rascalmpl.interpreter.control_exceptions.QuitException;
import org.rascalmpl.interpreter.env.GlobalEnvironment;
import org.rascalmpl.interpreter.env.ModuleEnvironment;
import org.rascalmpl.interpreter.load.StandardLibraryContributor;
import org.rascalmpl.interpreter.result.Result;
import org.rascalmpl.interpreter.staticErrors.StaticError;
import org.rascalmpl.interpreter.utils.RascalManifest;
import org.rascalmpl.parser.gtd.exception.ParseError;
import org.rascalmpl.repl.ReplTextWriter;
import org.rascalmpl.repl.TerminalProgressBarMonitor2;
import org.rascalmpl.uri.URIResolverRegistry;
import org.rascalmpl.uri.URIUtil;
import org.rascalmpl.values.RascalValueFactory;
import org.rascalmpl.values.ValueFactoryFactory;
import org.rascalmpl.values.parsetrees.TreeAdapter;

import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.IValueFactory;
import io.usethesource.vallang.type.Type;


public class RascalShell2  {

    private static void printVersionNumber(){
        System.err.println("Version: " + RascalManifest.getRascalVersionNumber());
    }
    
    public static void main(String[] args) throws IOException {
        System.setProperty("apple.awt.UIElement", "true"); // turns off the annoying desktop icon
        printVersionNumber();

        try {
            var term = TerminalBuilder.builder()
                .color(true)
                .encoding(StandardCharsets.UTF_8)
                .build();

            var reader = LineReaderBuilder.builder()
                .appName("Rascal REPL")
                .completer(new StringsCompleter("IO", "IOMeer", "println", "print", "printlnExp"))
                .terminal(term)
                .history(new DefaultHistory())
                .build();

            //IRascalMonitor monitor = IRascalMonitor.buildConsoleMonitor(System.in, System.out, true);
            var monitor = new TerminalProgressBarMonitor2(term);

            // var monitor = new NullRascalMonitor() {
            //     @Override
            //     public void warning(String message, ISourceLocation src) {
            //         reader.printAbove("[WARN] " + message);
            //     }
            // };

            IDEServices services = new BasicIDEServices(term.writer(), monitor);


            GlobalEnvironment heap = new GlobalEnvironment();
            ModuleEnvironment root = heap.addModule(new ModuleEnvironment(ModuleEnvironment.SHELL_MODULE, heap));
            IValueFactory vf = ValueFactoryFactory.getValueFactory();
            Evaluator evaluator = new Evaluator(vf, new NullInputStream(), OutputStream.nullOutputStream(), OutputStream.nullOutputStream(), root, heap, monitor);
            evaluator.overwritePrintWriter(monitor, new PrintWriter(monitor, true));
            evaluator.addRascalSearchPathContributor(StandardLibraryContributor.getInstance());

            URIResolverRegistry reg = URIResolverRegistry.getInstance();

            var indentedPrettyPrinter = new ReplTextWriter(true);

            while (true) {
                String line = reader.readLine(Ansi.ansi().reset().bold().toString() + "rascal> " + Ansi.ansi().boldOff().toString());
                try {

                    Result<IValue> result;

                    synchronized(evaluator) {
                        result = evaluator.eval(monitor, line, URIUtil.rootLocation("prompt"));
                        evaluator.endAllJobs();
                    }

                    if (result.isVoid()) {
                        monitor.println("ok");
                    }
                    else {
                        IValue value = result.getValue();
                        Type type = result.getStaticType();
                        
                        if (type.isAbstractData() && type.isStrictSubtypeOf(RascalValueFactory.Tree) && !type.isBottom()) {
                            monitor.write("(" + type.toString() +") `");
                            TreeAdapter.yield((IConstructor)value, true, monitor);
                            monitor.write("`");
                        }
                        else {
                            indentedPrettyPrinter.write(value, monitor);
                        }
                        monitor.println();
                    }
                }
                catch (InterruptException ie) {
                    reader.printAbove("Interrupted");
                    try {
                        ie.getRascalStackTrace().prettyPrintedString(evaluator.getStdErr(), indentedPrettyPrinter);
                    }
                    catch (IOException e) {
                    }
                }
                catch (ParseError pe) {
                    parseErrorMessage(evaluator.getStdErr(), line, "prompt", pe, indentedPrettyPrinter);
                }
                catch (StaticError e) {
                    staticErrorMessage(evaluator.getStdErr(),e, indentedPrettyPrinter);
                }
                catch (Throw e) {
                    throwMessage(evaluator.getStdErr(),e, indentedPrettyPrinter);
                }
                catch (QuitException q) {
                    reader.printAbove("Quiting REPL");
                    break;
                }
                catch (Throwable e) {
                    throwableMessage(evaluator.getStdErr(), e, evaluator.getStackTrace(), indentedPrettyPrinter);
                }
            }
            System.exit(0);
        }
        catch (Throwable e) {
            System.err.println("\n\nunexpected error: " + e.getMessage());
            e.printStackTrace(System.err);
            System.exit(1);
        }
        finally {
            System.out.flush();
            System.err.flush();
        }
    }

    private static class FakeOutput extends OutputStream {

        private final LineReader target;
        private final CharsetDecoder decoder;
        private final CharBuffer decoded;

        FakeOutput(LineReader target) {
            this.target = target;
            this.decoder = StandardCharsets.UTF_8.newDecoder();
            this.decoder.replaceWith("?");
            this.decoder.onMalformedInput(CodingErrorAction.REPLACE);
            this.decoder.onUnmappableCharacter(CodingErrorAction.REPLACE);
            this.decoded = CharBuffer.allocate(1024);
        }

        @Override
        public void write(int b) throws IOException {
            var res = decoder.decode(ByteBuffer.wrap(new byte[]{ (byte)b }), decoded, false);
            if (res.isOverflow()) {
                flush();
                write(b);
            }
        }

        @Override
        public void write(byte[] b, int off, int len) throws IOException {
            var bytes = ByteBuffer.wrap(b, off, len);
            while (bytes.hasRemaining()) {
                var res = decoder.decode(bytes, decoded, false);
                if (res.isOverflow()) {
                    flush();
                }
                else if (res.isError()) {
                    throw new IOException("Decoding failed with: " + res);
                }
            }
        }

        @Override
        public void flush() throws IOException {
            try {
                decoded.flip();
                if (decoded.hasRemaining()) {
                    this.target.printAbove(decoded.toString());
                }
            }
            finally {
                decoded.clear();
            }
        }
    }


    private static class FakePrintWriter extends PrintWriter {
        private final LineReader target;
        private final CharBuffer buffer;

        public FakePrintWriter(LineReader target, boolean autoFlush) {
            super(OutputStream.nullOutputStream(), autoFlush, StandardCharsets.UTF_8);
            this.target = target;
            this.buffer = CharBuffer.allocate(8*1024);
        }


        @Override
        public void write(int c) {
            makeRoom(1);
            this.buffer.append((char)c);
        }

        private void makeRoom(int i) {
            if (this.buffer.remaining() < i) {
                flush();
            }
        }


        @Override
        public void write(String s, int off, int len) {
            while (len > 0) {
                makeRoom(len);
                int room = Math.min(buffer.remaining(), len);
                buffer.append(s, off, room);
                off += room;
                len -= room;
            }
        }

        @Override
        public void write(char[] buf, int off, int len) {
            while (len > 0) {
                makeRoom(len);
                int room = Math.min(buffer.remaining(), len);
                buffer.put(buf, off, room);
                off += room;
                len -= room;
            }
        }

        @Override
        public void flush() {
            try {
                buffer.flip();
                if (buffer.hasRemaining()) {
                    target.printAbove(buffer.toString());
                }
            }
            finally {
                buffer.clear();
            }
        }


    }


}
