package com.jtransc.dynarec.evaluators;

import com.jtransc.dynarec.AnyInvoke;
import com.jtransc.dynarec.Expr;
import com.jtransc.dynarec.Function;
import com.jtransc.dynarec.Stm;

public class Interpreter {
	Object result;
	boolean completed = false;

	static public AnyInvoke compile(final Function function) {
		return new AnyInvoke() {
			final Interpreter interpreter = new Interpreter();

			@Override
			public Object invoke(Object... params) {
				return interpreter.interpret(function);
			}
		};
	}

	public Object interpret(Function function) {
		interpret(function.stm);
		return result;
	}

	private void interpret(Stm stm) {
		if (stm instanceof Stm.Return) {
			result = interpret(((Stm.Return) stm).expr);
			completed = true;
		} else {
			throw new RuntimeException("Unknown stm " + stm);
		}
	}

	private Object interpret(Expr expr) {
		if (expr instanceof Expr.IntLiteral) {
			return ((Expr.IntLiteral) expr).value;
		} else if (expr instanceof Expr.Binop) {
			final Expr.Binop binop = (Expr.Binop) expr;
			Object left = interpret(binop.left);
			Object right = interpret(binop.right);
			switch (binop.op) {
				case IADD:
					return ((Integer) left) + ((Integer) right);
				case ISUB:
					return ((Integer) left) - ((Integer) right);
				default:
					throw new RuntimeException("Not implemented binary operator " + binop.op);
			}
		} else {
			throw new RuntimeException("Unknown expr " + expr);
		}
	}
}