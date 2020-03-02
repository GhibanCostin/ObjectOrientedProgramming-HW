import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

public class AdminVoucherPage extends JFrame implements ActionListener {
	private Campaign camp;
	private JTable voucherTable;
	private JPanel mainPanel;
	private JPanel tablePanel;
	private JPanel addPanel;
	private JPanel redeemPanel;
	private JButton addBtn;
	private JButton redeemBtn;
	private JTextField emailField;
	private JTextField typeField;
	private JTextField valueField;
	private JTextField codeField;
	
	public AdminVoucherPage(Campaign c) {
		super("Admin Voucher Control");
		this.camp = c;
		voucherTable = generateTable();
		tablePanel = new JPanel();
		tablePanel.add(new JScrollPane(voucherTable));
		emailField = new JTextField("Email", 10);
		typeField = new JTextField("Type", 10);
		valueField = new JTextField("Value", 4);
		addBtn = new JButton("Generate");
		addBtn.addActionListener(this);
		addPanel = new JPanel();
		addPanel.add(emailField);
		addPanel.add(typeField);
		addPanel.add(valueField);
		addPanel.add(addBtn);
		codeField = new JTextField(15);
		redeemBtn = new JButton("Redeem Voucher");
		redeemBtn.setBackground(Color.PINK);
		redeemBtn.addActionListener(this);
		redeemPanel = new JPanel();
		redeemPanel.add(codeField);
		redeemPanel.add(redeemBtn);
		mainPanel = new JPanel();
		mainPanel.add(tablePanel);
		mainPanel.add(addPanel);
		mainPanel.add(redeemPanel);
		mainPanel.setLayout(new GridLayout(3, 0));
		this.add(mainPanel);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		this.setVisible(true);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == addBtn) {
			String email = emailField.getText();
			String type = typeField.getText();
			float value = -1;
			try{
				value = Float.parseFloat(valueField.getText());
			} catch (NumberFormatException err) {
				addBtn.setBackground(Color.RED);
				err.printStackTrace();
			}
			if (value != -1) {
				Voucher v = camp.generateVoucher(email, type, value);
				if (v == null) {
					addBtn.setBackground(Color.RED);
				} else {
					addBtn.setBackground(Color.GREEN);
					tablePanel.remove(0);
					voucherTable = generateTable();
					tablePanel.add(new JScrollPane(voucherTable), 0);
					tablePanel.revalidate();
					tablePanel.repaint();
				}
			}
		}
		if (e.getSource() == redeemBtn) {
			Voucher v = camp.getVoucher(codeField.getText());
			if (v == null) {
				redeemBtn.setBackground(Color.RED);
			} else {
				redeemBtn.setBackground(Color.GREEN);
				v.setStatus(Voucher.VoucherStatusType.USED);
				tablePanel.remove(0);
				voucherTable = generateTable();
				tablePanel.add(new JScrollPane(voucherTable), 0);
				tablePanel.revalidate();
				tablePanel.repaint();
			}
		}
	}
	public JTable generateTable() {
		List<Voucher> vouchers = camp.getVouchers();
		String[] colNames = {"ID", "Code", "Status", "Email"};
		Object[][] data = new Object[vouchers.size()][4];
		int i = 0;
		for (Voucher v : vouchers) {
			data[i][0] = v.getId();
			data[i][1] = v.getCode();
			data[i][2] = v.getStatus();
			data[i][3] = v.getEmail();
			i++;
		}
		voucherTable = new JTable(data, colNames);
		voucherTable.setAutoCreateRowSorter(true);
		return voucherTable;
	}
}
