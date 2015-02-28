package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter;

public class Interpreter {

	static Instruction[] program;
	static int sp;
	static Integer[] stack = new Integer[100];

	static void indirectStyle(int rep) {
		int nrIns = program.length - 1;
		for (int i = 0; i < rep; i++) {
			int pc = 0;
			sp = 0;
			try {
				while (pc < nrIns) {
					program[pc++].execute();
				}
			} catch (RuntimeException e) {
				return;
			}
		}
	}

	static void switchStyle(int rep) {
		int nrIns = program.length - 1;
		for (int i = 0; i < rep; i++) {
			int pc = 0;
			sp = 0;

			NEXT_INSTRUCTION: while (pc < nrIns) {
				switch (program[pc++]) {

				case ADD:
					stack[sp - 2] = stack[sp - 2] + stack[sp - 1];
					sp--;
					continue NEXT_INSTRUCTION;

				case CON:
					stack[sp++] = program[pc - 1].arg1;
					continue NEXT_INSTRUCTION;

				case POP:
					sp--;
					continue NEXT_INSTRUCTION;

				case HALT:
					break NEXT_INSTRUCTION;
				}
			}
		}
	}

	public static void main(String[] args) {
		int n = 1000;
		int rep = 100000;
		program = new Instruction[4 * n + 1];
		for (int i = 0; i < n; i++) {
			program[i * 4] = Instruction.CON(10);
			program[i * 4 + 1] = Instruction.CON(20);
			program[i * 4 + 2] = Instruction.ADD;
			program[i * 4 + 3] = Instruction.POP;
		}
		program[4 * n] = Instruction.HALT;  //  NOT EXECUTING

		long t1 = System.nanoTime();
		indirectStyle(rep);
		long t2 = System.nanoTime();
		switchStyle(rep);
		long t3 = System.nanoTime();

		long t_indirect = (t2 - t1) / 1000000;
		long t_switch = (t3 - t2) / 1000000;

		System.out.println("indirect: " + t_indirect + ", switch: " + t_switch + ", ratio: " + (1.0 * t_indirect / t_switch));
	}
}

enum Instruction {
	ADD {
		@Override
		public void execute() {
			Interpreter.stack[Interpreter.sp - 2] = Interpreter.stack[Interpreter.sp - 2] + Interpreter.stack[Interpreter.sp - 1];
			Interpreter.sp--;
		}
	},
	CON {
		@Override
		public void execute() {
			Interpreter.stack[Interpreter.sp] = this.arg1;
			Interpreter.sp++;
		}
	},
	POP {
		@Override
		public void execute() {
			Interpreter.sp--;
		}
	},
	HALT {
		@Override
		public void execute() {
			throw new RuntimeException();
		}
	};

	public void execute() {
		System.err.println("Not implemented instruction");
	}

	public static Instruction CON(int i) {
		Instruction res = CON;
		res.arg1 = i;
		return res;
	}

	public static final Instruction[] values = Instruction.values();

	protected int arg1 = 0;

	private Instruction() {
		arg1 = 0;
	}

	private Instruction(int arg) {
		this.arg1 = arg;
	}
}
