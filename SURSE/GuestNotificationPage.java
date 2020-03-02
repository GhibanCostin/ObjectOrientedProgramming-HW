import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;

public class GuestNotificationPage extends JFrame {
	private JPanel mainPanel;
	private JTable notifTable;
	private User user;
	private VMS vms;
	
	public GuestNotificationPage(User user) {
		super("My Notifications");
		vms = VMS.getInstance();
		this.user = user;
		notifTable = generateTable();
		mainPanel = new JPanel();
		mainPanel.add(new JScrollPane(notifTable));
		mainPanel.setBorder(new EmptyBorder(120, 0, 0, 0));
		this.add(mainPanel);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		this.setVisible(true);
	}
	public JTable generateTable() {
		List<Notification> notifs = user.getNotifications();
		String[] colNames = {"Notifications"};
		Object[][] data = new Object[notifs.size()][1];
		int i = 0;
		for (Notification n : notifs) {
			data[i][0] = n.toString();
			i++;
		}
		notifTable = new JTable(data, colNames);
		return notifTable;
	}
}
