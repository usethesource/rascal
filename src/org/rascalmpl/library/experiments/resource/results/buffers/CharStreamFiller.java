package org.rascalmpl.library.experiments.resource.results.buffers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.IValue;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.values.ValueFactoryFactory;

public class CharStreamFiller implements ILazyFiller {

	private ISourceLocation source;
	private IEvaluatorContext ctx;
	private InputStream is;
	private BufferedReader br;
	
	public CharStreamFiller(ISourceLocation source, IEvaluatorContext ctx) {
		this.source = source;
		this.ctx = ctx;
		this.is = null;
	}

	@Override
	public IValue[] refill(int pageSize) {
		try {
			if (is == null) {
				is = ctx.getResolverRegistry().getInputStream(source.getURI());
				br = new BufferedReader(new InputStreamReader(is));
			}
			ArrayList<String> al = new ArrayList<String>();
			int readChars = 0;
			while (readChars < pageSize) {
				int n = br.read();
				if (n != -1) {
					al.add(new String(Character.toString((char)n)));
					++readChars;
				} else {
					break;
				}
			}
			
			IValue res[] = new IValue[al.size()];
			for (int idx = 0; idx < al.size(); ++idx) res[idx] = ValueFactoryFactory.getValueFactory().string(al.get(idx));
			return res;
		} catch (IOException ioe) {
			
		}
		
		return new IValue[0];
	}

	@Override
	public ILazyFiller getBufferedFiller() {
		return new CharStreamFiller(this.source, this.ctx);
	}

}
