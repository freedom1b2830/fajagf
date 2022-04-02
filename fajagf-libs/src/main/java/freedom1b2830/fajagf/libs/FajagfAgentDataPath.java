package freedom1b2830.fajagf.libs;

public class FajagfAgentDataPath {
	int level = 0;
	FajagfAgentDataPath next;
	String className;
	String methodName;
	String filename;
	int line;
	String moduleName;

	public Object[] args;

	public FajagfAgentDataPath getForLevel(int index) {
		if (level == index) {
			return this;
		}
		if (next == null) {
			next = new FajagfAgentDataPath();
			next.level = level + 1;
		}
		return next.getForLevel(index);
	}

	public boolean hashIndex(int index) {
		if (level == index) {
			return true;
		}
		if (next == null) {
			return false;
		}
		return next.hashIndex(index);
	}

	public boolean hashNext() {
		return next != null;
	}

	public void parseStackTraceElement(StackTraceElement st) {
		moduleName = st.getModuleName();
		className = st.getClassName();
		methodName = st.getMethodName();
		filename = st.getFileName();
		line = st.getLineNumber();
	}
}