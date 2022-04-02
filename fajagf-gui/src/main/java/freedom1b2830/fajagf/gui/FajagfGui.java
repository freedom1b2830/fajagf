package freedom1b2830.fajagf.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import freedom1b2830.fajagf.gui.tabs.ConnectionToHubPanel;

public class FajagfGui extends JFrame implements WindowListener {
	private static final long serialVersionUID = -5676643607322076547L;
	private static final Logger LOGGER = LoggerFactory.getLogger("GUI:common");
	static FajagfGui gui = new FajagfGui();

	public static void main(String[] args) {
		gui.setVisible(true);
	}

	public FajagfGui() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		addWindowListener(this);

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		double height = screenSize.getHeight();
		double width = screenSize.getWidth();
		setSize((int) (width - width / 7), (int) (height - height / 5));

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.LEFT);
		getContentPane().add(tabbedPane, BorderLayout.CENTER);

		ConnectionToHubPanel connectionToHubPanel = new ConnectionToHubPanel();
		tabbedPane.addTab("conn->hub", null, connectionToHubPanel, null);

	}

	@Override
	public void windowActivated(WindowEvent e) {
		// окно выбрано
	}

	@Override
	public void windowClosed(WindowEvent e) {
		LOGGER.info("windowClosed");
	}

	@Override
	public void windowClosing(WindowEvent e) {
		// окно закрывается
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
		// окно скрыто (выбрано другое)
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
		LOGGER.info("windowDeiconified");
	}

	@Override
	public void windowIconified(WindowEvent e) {
		LOGGER.info("windowIconified");
	}

	@Override
	public void windowOpened(WindowEvent e) {
		// открыто
	}
}
