import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;

public class GuestVoucherPage extends JFrame {
	private JPanel mainPanel;
	private JTable voucherTable;
	private User user;
	private VMS vms;
	
	public GuestVoucherPage(User user) {
		super("My Vouchers");
		vms = VMS.getInstance();
		this.user = user;
		voucherTable = generateTable();
		mainPanel = new JPanel();
		mainPanel.add(new JScrollPane(voucherTable));
		mainPanel.setBorder(new EmptyBorder(120, 0, 0, 0));
		this.add(mainPanel);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		this.setVisible(true);
	}
	public JTable generateTable() {
		List<Voucher> vouchers = user.getVouchers();
		String[] colNames = {"ID", "Code", "Status", "Usage Date"};
		Object[][] data = new Object[vouchers.size()][4];
		int i = 0;
		for (Voucher v : vouchers) {
			data[i][0] = v.getId();
			data[i][1] = v.getCode();
			data[i][2] = v.getStatus();
			data[i][3] = v.getUsageDate();
			i++;
		}
		voucherTable = new JTable(data, colNames);
		voucherTable.setAutoCreateRowSorter(true);
		return voucherTable;
	}
}
