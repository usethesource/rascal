/** 
 * Copyright (c) 2016, Davy Landman, Centrum Wiskunde & Informatica (CWI) 
 * All rights reserved. 
 *  
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met: 
 *  
 * 1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer. 
 *  
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution. 
 *  
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. 
 */ 
package org.rascalmpl.shell;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.function.IntConsumer;
import java.util.function.IntSupplier;

import org.jline.terminal.Attributes;
import org.jline.terminal.Cursor;
import org.jline.terminal.MouseEvent;
import org.jline.terminal.Size;
import org.jline.terminal.Terminal;
import org.jline.utils.ColorPalette;
import org.jline.utils.InfoCmp.Capability;
import org.jline.utils.NonBlockingReader;


public class EclipseTerminalConnection implements Terminal {
    public static final byte[] HEADER = new byte[] { 0x42, 0x42 };
    public static final int GET_WIDTH = 1;
    public static final int GET_HEIGHT = 2;
    private final Terminal base;
    private final DataOutputStream send;
    private final DataInputStream recv;
    private final Socket eclipseConnection;
    private int previousWidth;
    private int previousHeight;

    public EclipseTerminalConnection(Terminal base, int port) throws IOException {
        this.base = base;
        this.eclipseConnection = new Socket(InetAddress.getLoopbackAddress(), port);
        this.eclipseConnection.setTcpNoDelay(true);
        this.eclipseConnection.setReceiveBufferSize(1);
        this.eclipseConnection.setSendBufferSize(1);
        this.eclipseConnection.setKeepAlive(true);
        this.eclipseConnection.setSoTimeout(10_000);
        send = new DataOutputStream(this.eclipseConnection.getOutputStream());
        recv = new DataInputStream(this.eclipseConnection.getInputStream());
        send.write(HEADER);
        byte[] serverHeader = new byte[HEADER.length];
        if (recv.read(serverHeader) != serverHeader.length || !Arrays.equals(HEADER, serverHeader)) {
            throw new IOException("Non matching server");
        }
    }
    @Override
    public int getWidth() {
        return askForWidth();
    }
    @Override
    public int getHeight() {
        return askForHeight();
    }

    private int askForWidth() {
        try {
            send.writeByte(GET_WIDTH);
            return previousWidth = recv.readInt();
        }
        catch (IOException e) {
            return previousWidth;
        }
    }

    private int askForHeight() {
        try {
            send.writeByte(GET_HEIGHT);
            return previousHeight = recv.readInt();
        }
        catch (IOException e) {
            return previousHeight;
        }
    }

    @Override
    public Size getSize() {
        return new Size(askForWidth(), askForHeight());
    }
    @Override
    public void setSize(Size size) {
        base.setSize(size);
    }

    @Override
    public void close() throws IOException {
        base.close();
    }
    @Override
    public String getName() {
        return base.getName();
    }
    @Override
    public SignalHandler handle(Signal signal, SignalHandler handler) {
        return base.handle(signal, handler);
    }
    @Override
    public void raise(Signal signal) {
        base.raise(signal);
    }
    @Override
    public NonBlockingReader reader() {
        return base.reader();
    }
    @Override
    public PrintWriter writer() {
        return base.writer();
    }
    @Override
    public Charset encoding() {
        return base.encoding();
    }
    @Override
    public InputStream input() {
        return base.input();
    }
    @Override
    public OutputStream output() {
        return base.output();
    }
    @Override
    public boolean canPauseResume() {
        return base.canPauseResume();
    }
    @Override
    public void pause() {
        base.pause();
    }
    @Override
    public void pause(boolean wait) throws InterruptedException {
        base.pause(wait);
    }
    @Override
    public void resume() {
        base.resume();
    }
    @Override
    public boolean paused() {
        return base.paused();
    }
    @Override
    public Attributes enterRawMode() {
        return base.enterRawMode();
    }
    @Override
    public boolean echo() {
        return base.echo();
    }
    @Override
    public boolean echo(boolean echo) {
        return base.echo(echo);
    }
    @Override
    public Attributes getAttributes() {
        return base.getAttributes();
    }
    @Override
    public void setAttributes(Attributes attr) {
        base.setAttributes(attr);
    }
    @Override
    public void flush() {
        base.flush();
    }
    @Override
    public String getType() {
        return base.getType();
    }
    @Override
    public boolean puts(Capability capability, Object... params) {
        return base.puts(capability, params);
    }
    @Override
    public boolean getBooleanCapability(Capability capability) {
        return base.getBooleanCapability(capability);
    }
    @Override
    public Integer getNumericCapability(Capability capability) {
        return base.getNumericCapability(capability);
    }
    @Override
    public String getStringCapability(Capability capability) {
        return base.getStringCapability(capability);
    }
    @Override
    public Cursor getCursorPosition(IntConsumer discarded) {
        return base.getCursorPosition(discarded);
    }
    @Override
    public boolean hasMouseSupport() {
        return base.hasMouseSupport();
    }
    @Override
    public boolean trackMouse(MouseTracking tracking) {
        return base.trackMouse(tracking);
    }
    @Override
    public MouseEvent readMouseEvent() {
        return base.readMouseEvent();
    }
    @Override
    public MouseEvent readMouseEvent(IntSupplier reader) {
        return base.readMouseEvent(reader);
    }
    @Override
    public boolean hasFocusSupport() {
        return base.hasFocusSupport();
    }
    @Override
    public boolean trackFocus(boolean tracking) {
        return base.trackFocus(tracking);
    }
    @Override
    public ColorPalette getPalette() {
        return base.getPalette();
    }

}
