import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;

public class GuestCampaignPage extends JFrame implements ActionListener {
	private VMS vms;
	private JPanel mainPanel;
	private JTable campTable;
	private JButton vouchersBtn;
	private JButton notifBtn;
	private User user;
	
	public GuestCampaignPage(User user) {
		super("My Campaigns");
		this.user = user;
		vms = VMS.getInstance();
		campTable = generateTable();
		vouchersBtn = new JButton("See Vouchers");
		vouchersBtn.addActionListener(this);
		vouchersBtn.setBackground(Color.CYAN);
		notifBtn = new JButton("See Notifications");
		notifBtn.addActionListener(this);
		notifBtn.setBackground(Color.MAGENTA);
		mainPanel = new JPanel();
		mainPanel.add(new JScrollPane(campTable));
		mainPanel.add(vouchersBtn);
		mainPanel.add(notifBtn);
		mainPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		mainPanel.setBorder(new EmptyBorder(120, 0, 0, 0));
		this.add(mainPanel);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		this.setVisible(true);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == vouchersBtn) {
			GuestVoucherPage voucherPage = new GuestVoucherPage(user);
		}
		if (e.getSource() == notifBtn) {
			GuestNotificationPage notifPage = new GuestNotificationPage(user);
		}
	}
	public JTable generateTable() {
		String[] colNames = {"ID", "Name", "Status", "Start Date", "End Date"};
		Vector<Campaign> camps = new Vector<>();
		UserVoucherMap map = user.getUserVoucherMap();
		Iterator<Entry<Integer, ArrayList<Voucher>>> it;
		it = map.entrySet().iterator();
		//adaugam doar campaniile la care participa utilizatorul
		while (it.hasNext()) {
			UserVoucherMap.ArrayMapEntry<Integer, ArrayList<Voucher>> e;
            e = (UserVoucherMap.ArrayMapEntry<Integer, ArrayList<Voucher>>)(it.next());
            camps.add(vms.getCampaign(e.getKey()));
		}
		Object[][] data = new Object[camps.size()][5];
		int i = 0;
		for (Campaign c : camps) {
			data[i][0] = c.getId();
			data[i][1] = c.getName();
			data[i][2] = c.getStatus();
			data[i][3] = c.getStartDate();
			data[i][4] = c.getEndDate();
			i++;
		}
		campTable = new JTable(data, colNames);
		campTable.setAutoCreateRowSorter(true);
		return campTable;
	}
}
