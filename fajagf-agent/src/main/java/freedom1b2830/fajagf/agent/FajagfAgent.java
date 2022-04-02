package freedom1b2830.fajagf.agent;

import java.io.ByteArrayInputStream;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;
import java.util.Arrays;
import java.util.concurrent.CopyOnWriteArrayList;

import freedom1b2830.fajagf.libs.FajagfAgentData;
import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.LoaderClassPath;
import javassist.scopedpool.ScopedClassPoolFactoryImpl;
import javassist.scopedpool.ScopedClassPoolRepositoryImpl;

public class FajagfAgent {

	public static final CopyOnWriteArrayList<FajagfAgentData> qArrayList = new CopyOnWriteArrayList<>();

	public static final FajagfAgentCallback callback = (Thread thread, Object[] objects) -> {
		StackTraceElement[] stackTraceElements = thread.getStackTrace();

		FajagfAgentData fajagfAgentData = new FajagfAgentData();
		fajagfAgentData.threadInfo.id = thread.getId();
		fajagfAgentData.threadInfo.threadName = thread.getName();
		fajagfAgentData.threadInfo.threadPriority = thread.getPriority();
		fajagfAgentData.threadInfo.state = thread.getState();

		int i = 0;
		for (StackTraceElement stackTraceElement : stackTraceElements) {
			// for (int i = stackTraceElements.length - 1; i >= 0; i--) {
			// StackTraceElement stackTraceElement = stackTraceElements[i];

			// if (i == 0) {

			if (i == stackTraceElements.length - 1) {
				fajagfAgentData.appendTraceElementWithLatestArgs(i, stackTraceElement, objects);
			} else {
				fajagfAgentData.appendTraceElement(i, stackTraceElement);
			}
			i++;
		}
		qArrayList.add(fajagfAgentData);
		System.err.println(fajagfAgentData);
		// Runtime.getRuntime().exit(0);
	};

	static CopyOnWriteArrayList<String> skip = new CopyOnWriteArrayList<>(Arrays.asList(

	));

	private static final ScopedClassPoolFactoryImpl scopedClassPoolFactory = new ScopedClassPoolFactoryImpl();

	public static void main(String[] args) {
	}

	public static void premain(String agentArgs, Instrumentation instrumentation) {

		ClassPool cPool = ClassPool.getDefault();
		cPool.appendClassPath(new LoaderClassPath(FajagfAgent.class.getClassLoader()));

		cPool.insertClassPath(new ClassClassPath(java.awt.event.WindowEvent.class));
		cPool.insertClassPath(new ClassClassPath(FajagfAgent.class));
		cPool.insertClassPath(new ClassClassPath(FajagfAgentCallback.class));
		cPool.insertClassPath(new ClassClassPath(Throwable.class));

		ClassFileTransformer aa = new ClassFileTransformer() {

			@Override
			public byte[] transform(Module module, ClassLoader loader, String className, Class<?> classBeingRedefined,
					ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
				ClassPool classPool = scopedClassPoolFactory.create(loader, ClassPool.getDefault(),
						ScopedClassPoolRepositoryImpl.getInstance());

				// cPool.appendClassPath(new LoaderClassPath(getClass().getClassLoader()));
				// cPool.appendClassPath(new LoaderClassPath(loader));
				byte[] bytecode = classfileBuffer;
				// if (className.contains("FajagfGui")) {
				try {
					// System.err.println(className);

					CtClass ctClass = classPool.makeClass(new ByteArrayInputStream(bytecode));

					for (CtMethod method : ctClass.getDeclaredMethods()) {
						String methodName = null;
						try {
							String nameclass = ctClass.getName();
							methodName = method.getName();
							String argsSignat = method.getSignature();

							if (method.isEmpty()) {
								continue;
							}
							System.err.println("_________ " + className);
							method.insertBefore(
									"{freedom1b2830.fajagf.agent.FajagfAgent.callback.inputList(Thread.currentThread(),$args);}");
						} catch (Exception e) {
							e.printStackTrace();
							System.err.println("1@@: " + className + " " + ctClass.isInterface());
							System.err.println("2@@: " + methodName + " " + method.isEmpty());

						}

					}
					// System.err.println("FajagfAgent.premain(OK): " + className);

					bytecode = ctClass.toBytecode();
				} catch (Exception e) {

					e.printStackTrace();
					System.err.println("FajagfAgent.premain(): " + className);
				}
				// }
				// System.err.println(className + " END");

				return bytecode;
			}

		};
		instrumentation.addTransformer(aa);
	}

}
