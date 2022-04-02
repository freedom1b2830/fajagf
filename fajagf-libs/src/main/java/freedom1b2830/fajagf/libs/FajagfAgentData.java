package freedom1b2830.fajagf.libs;

public class FajagfAgentData {

	public FajagfAgentDataThreadInfo threadInfo = new FajagfAgentDataThreadInfo();
	public final FajagfAgentDataPath path = new FajagfAgentDataPath();

	public void appendTraceElement(int index, StackTraceElement stackTraceElement) {
		appendTraceElementWithLatestArgs(index, stackTraceElement, null);
	}

	public void appendTraceElementWithLatestArgs(int index, StackTraceElement stackTraceElement, Object[] objects) {

		FajagfAgentDataPath data = path.getForLevel(index);
		data.parseStackTraceElement(stackTraceElement);
		if (objects != null) {
			// TODO parse
			data.args = objects;
		}
		// System.out.println("FajagfAgent.FajagfAgentData.appendTraceElementWithLatestArgs():\n"
		// + this);

	}

	@Override
	public String toString() {
		StringBuilder ret = new StringBuilder();
		ret.append("FajagfAgentData:").append('\n');
		ret.append('\t').append("thread:").append('\n');
		ret.append('\t').append('\t').append("id:").append(threadInfo.id).append('\n');
		ret.append('\t').append('\t').append("name:").append(threadInfo.threadName).append('\n');
		ret.append('\t').append('\t').append("Priority:").append(threadInfo.threadPriority).append('\n');
		ret.append('\t').append('\t').append("state:").append(threadInfo.state).append('\n');
		ret.append('\t').append("path:").append('\n');
		ret.append(toString(path));
		return ret.toString();
	}

	private String toString(FajagfAgentDataPath path2) {
		StringBuilder ret = new StringBuilder();
		int levels = path2.level;
		String tabs = "";
		for (int i = 0; i <= levels; i++) {
			tabs = tabs + "\t";
		}
		ret.append(tabs).append("moduleName:").append(path2.moduleName).append('\n');
		ret.append(tabs).append("className:").append(path2.className).append('\n');
		ret.append(tabs).append("methodName:").append(path2.methodName).append('\n');
		ret.append(tabs).append("filename:").append(path2.filename).append('\n');
		ret.append(tabs).append("line:").append(path2.line).append('\n');
		if (path2.hashNext()) {
			ret.append(toString(path2.next));
		}
		return ret.toString();
	}
}