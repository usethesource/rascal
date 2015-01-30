package org.rascalmpl.library.lang.java.m3.internal;

import java.io.IOException;
import java.net.URISyntaxException;

import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.type.TypeStore;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.uri.URIResolverRegistry;

public class JarConverter extends M3Converter {
	JarConverter(TypeStore typeStore) {
		super(typeStore);
	}
	
	public void convert(ISourceLocation jarLoc, IEvaluatorContext ctx) {
		try {
			ClassReader cr = new ClassReader(URIResolverRegistry.getInstance().getInputStream(jarLoc));
			cr.accept(new JarConverter.ASMClassConverter(Opcodes.ASM4, jarLoc), 0);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	class ASMClassConverter extends ClassVisitor {
		private final String jarFile;
		
		public ASMClassConverter(int api, ISourceLocation jarLoc) {
			super(api);
			this.jarFile = extractJarName(jarLoc);
		}

		public ASMClassConverter(int api, ClassVisitor cv, ISourceLocation jarLoc) {
			super(api, cv);
			this.jarFile = extractJarName(jarLoc);
		}

		private String extractJarName(ISourceLocation jarLoc) {
			String tmp = jarLoc.getPath().substring(0, jarLoc.getPath().indexOf("!"));
			return tmp.substring(tmp.lastIndexOf("/")+1);
		}
		
		@Override
		public void visit(int version, int access, String name, String signature,
				String superName, String[] interfaces) {
			try {
				JarConverter.this.insert(JarConverter.this.declarations, values.sourceLocation("java+class", jarFile, "/"+name), values.sourceLocation(jarFile));
				JarConverter.this.insert(JarConverter.this.extendsRelations, values.sourceLocation("java+class", jarFile, "/"+name), values.sourceLocation("java+class", "", "/"+superName));
				for (String interfce : interfaces) {
					JarConverter.this.insert(JarConverter.this.implementsRelations, values.sourceLocation("java+class", jarFile, "/"+name), values.sourceLocation("java+interface", "", "/"+interfce));
				}
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				throw new RuntimeException("Should not happen", e);
			}
		}

		@Override
		public void visitSource(String source, String debug) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void visitOuterClass(String owner, String name, String desc) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void visitAttribute(Attribute attr) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void visitInnerClass(String name, String outerName,
				String innerName, int access) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public FieldVisitor visitField(int access, String name, String desc,
				String signature, Object value) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public MethodVisitor visitMethod(int access, String name, String desc,
				String signature, String[] exceptions) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void visitEnd() {
			// TODO Auto-generated method stub
			
		}
	}
}
