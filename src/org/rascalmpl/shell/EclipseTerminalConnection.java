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
import java.net.InetAddress;
import java.net.Socket;
import java.util.Arrays;

import jline.Terminal;

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
    public void init() throws Exception {
        base.init();
    }

    @Override
    public void restore() throws Exception {
        base.restore();
    }

    @Override
    public void reset() throws Exception {
        base.reset();
    }

    @Override
    public boolean isSupported() {
        return base.isSupported();
    }


    @Override
    public boolean isAnsiSupported() {
        return base.isAnsiSupported();
    }

    @Override
    public OutputStream wrapOutIfNeeded(OutputStream out) {
        return base.wrapOutIfNeeded(out);
    }

    @Override
    public InputStream wrapInIfNeeded(InputStream in) throws IOException {
        return base.wrapInIfNeeded(in);
    }

    @Override
    public boolean hasWeirdWrap() {
        return base.hasWeirdWrap();
    }

    @Override
    public boolean isEchoEnabled() {
        return base.isEchoEnabled();
    }

    @Override
    public void setEchoEnabled(boolean enabled) {
        base.setEchoEnabled(enabled);
    }

    @Override
    public String getOutputEncoding() {
        return base.getOutputEncoding();
    }
    
    @Override
    public void enableInterruptCharacter() {
        base.enableInterruptCharacter();
    }
    
    @Override
    public void disableInterruptCharacter() {
        base.disableInterruptCharacter();
    }

}
