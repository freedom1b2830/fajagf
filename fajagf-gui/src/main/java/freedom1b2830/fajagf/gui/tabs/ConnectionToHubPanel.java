package freedom1b2830.fajagf.gui.tabs;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.SwingConstants;

import freedom1b2830.fajagf.gui.network.NetworkManager;
import freedom1b2830.fajagf.libs.Config;

public class ConnectionToHubPanel extends JPanel {
	enum connectionState {
		DISCONNECTED, CONNECTING, CONNECTED, NOCONNECTED, DISCONNECTING
	}

	private static final long serialVersionUID = -3830306436607040897L;
	private JTextField tfHost;
	private JTextField tfPort;

	private connectionState state = connectionState.NOCONNECTED;

	JLabel lblConState;

	JButton btnConnect;

	public ConnectionToHubPanel() {
		SpringLayout springLayout = new SpringLayout();
		setLayout(springLayout);

		tfHost = new JTextField("127.0.0.1");
		tfHost.setHorizontalAlignment(SwingConstants.CENTER);
		tfHost.setToolTipText("host");
		springLayout.putConstraint(SpringLayout.NORTH, tfHost, 40, SpringLayout.NORTH, this);
		springLayout.putConstraint(SpringLayout.WEST, tfHost, 90, SpringLayout.WEST, this);
		springLayout.putConstraint(SpringLayout.EAST, tfHost, -90, SpringLayout.EAST, this);
		add(tfHost);
		tfHost.setColumns(10);

		tfPort = new JTextField(Integer.toString(Config.SERVERPORT));
		tfPort.setHorizontalAlignment(SwingConstants.CENTER);
		tfPort.setToolTipText("port");
		springLayout.putConstraint(SpringLayout.NORTH, tfPort, 22, SpringLayout.SOUTH, tfHost);
		springLayout.putConstraint(SpringLayout.WEST, tfPort, 90, SpringLayout.WEST, this);
		springLayout.putConstraint(SpringLayout.EAST, tfPort, -90, SpringLayout.EAST, this);
		add(tfPort);
		tfPort.setColumns(10);

		lblConState = new JLabel(state.name());
		lblConState.setHorizontalAlignment(SwingConstants.CENTER);
		springLayout.putConstraint(SpringLayout.NORTH, lblConState, 54, SpringLayout.SOUTH, tfPort);
		springLayout.putConstraint(SpringLayout.WEST, lblConState, 90, SpringLayout.WEST, this);
		springLayout.putConstraint(SpringLayout.EAST, lblConState, -90, SpringLayout.EAST, this);
		add(lblConState);

		btnConnect = new JButton("CONNECT");
		springLayout.putConstraint(SpringLayout.SOUTH, btnConnect, -11, SpringLayout.NORTH, lblConState);

		springLayout.putConstraint(SpringLayout.WEST, btnConnect, 90, SpringLayout.WEST, this);
		springLayout.putConstraint(SpringLayout.EAST, btnConnect, -90, SpringLayout.EAST, this);
		btnConnect.addActionListener(e -> {
			try {
				connect();
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
		});

		add(btnConnect);
	}

	protected void connect() throws InterruptedException {
		switch (state) {
		case NOCONNECTED -> {
			state = connectionState.CONNECTING;
			updateView();
			connectBtnLock();
			boolean status = false;
			try {
				status = NetworkManager.connect(tfHost.getText(), Integer.valueOf(tfPort.getText()));
			} catch (Throwable e) {
				e.printStackTrace();
				state = connectionState.NOCONNECTED;
				connectBtnUnLock();
				updateView();
				return;
			}
			if (status) {
				state = connectionState.CONNECTED;
				btnConnect.setText("DISCONNECT");
			}
			connectBtnUnLock();
			updateView();
		}
		case CONNECTED -> {
			state = connectionState.DISCONNECTING;
			updateView();
			connectBtnLock();

			NetworkManager.disconnect();
			btnConnect.setText("CONNECT");
			state = connectionState.DISCONNECTED;

			connectBtnUnLock();
			updateView();
		}
		case DISCONNECTED -> {
			state = connectionState.NOCONNECTED;
			connect();
		}

		default -> throw new IllegalArgumentException("Unexpected value: " + state);
		}

		// TODO Auto-generated method stub

	}

	private void connectBtnLock() {
		btnConnect.setEnabled(false);
	}

	private void connectBtnUnLock() {
		btnConnect.setEnabled(true);
	}

	private void updateView() {
		lblConState.setText(state.name());

	}
}
