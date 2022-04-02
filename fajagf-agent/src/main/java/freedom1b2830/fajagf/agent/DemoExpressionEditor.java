package freedom1b2830.fajagf.agent;

import javassist.CannotCompileException;
import javassist.NotFoundException;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;

public class DemoExpressionEditor extends ExprEditor {
	String data;

	public String back() {
		String ret = data;
		data = null;// clear
		return ret;
	}

	@Override
	public void edit(MethodCall method) throws CannotCompileException {

		StringBuilder builder = new StringBuilder();
		builder.append(method.getClassName()).append('.').append(method.getMethodName()).append(" args: ");
		builder.append(method.getSignature());

		try {
			method.getMethod().insertBefore("System.err.println(\"123123\")");

		} catch (CannotCompileException | NotFoundException e) {
			e.printStackTrace();
		}

		// if (method.getMethodName().contains("sleep")) {
		/*
		 * System.out.println("[Instrumentation] Suppressing sleep for " +
		 * method.getClassName() + "." + method.getMethodName() + " called from " +
		 * method.getEnclosingClass().getName()
		 *
		 * + "\n" + method.getMethodName());
		 */

		// method.replace("{}");

		// method.
		// }
		// System.err.println(builder);
		data = builder.toString();
	}
}