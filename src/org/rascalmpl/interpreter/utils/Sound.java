package org.rascalmpl.interpreter.utils;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

import org.rascalmpl.interpreter.asserts.ImplementationError;

public class Sound {
	private final SourceDataLine line;
	private AudioFormat af;
	private Note[] notes = {Note.A4, Note.A4$, Note.B4, Note.C4, Note.C4$, Note.D4, Note.D4$, Note.E4, Note.F4, Note.F4$, Note.G4, Note.G4$, Note.A5};
	
	public enum Note {
	    REST, A4, A4$, B4, C4, C4$, D4, D4$, E4, F4, F4$, G4, G4$, A5;
	    public static final int SAMPLE_RATE = 16 * 1024; // ~16KHz
	    public static final int SECONDS = 2;
	    private byte[] sin = new byte[SECONDS * SAMPLE_RATE];

	    Note() {
	        int n = this.ordinal();
	        if (n > 0) {
	            double exp = ((double) n - 1) / 12d;
	            double f = 440d * Math.pow(2d, exp);
	            for (int i = 0; i < sin.length; i++) {
	                double period = (double)SAMPLE_RATE / f;
	                double angle = 2.0 * Math.PI * i / period;
	                sin[i] = (byte)(Math.sin(angle) * 127f);
	            }
	        }
	    }

	    public byte[] data() {
	        return sin;
	    }
	}

	public Sound() {
        try {
        	af = new AudioFormat(Note.SAMPLE_RATE, 8, 1, true, true);
			line = AudioSystem.getSourceDataLine(af);
			line.open(af, Note.SAMPLE_RATE);
			line.start();
		} catch (LineUnavailableException e) {
			throw new ImplementationError("can not play sound", e);
		}
    }
	
	public void play(Note note, int ms) {
		play(line, note, ms);
		line.drain(); 
	}
	
	/**
	 * @param note something between 0 and 12
	 * @param ms
	 */
	public void play(int note, int ms) {
		play(line, notes[note], ms);
		line.drain(); 
	}
	
	@Override
	protected void finalize() throws Throwable {
		line.close();
	}

    private static void play(SourceDataLine line, Note note, int ms) {
        ms = Math.min(ms, Note.SECONDS * 1000);
        int length = Note.SAMPLE_RATE * ms / 1000;
        line.write(note.data(), 0, length);
    }
    
    public static void main(String[] args) {
		Sound s = new Sound();
		s.play(Sound.Note.B4, 1000);
		s.play(Sound.Note.C4$, 1000);
	}
}


