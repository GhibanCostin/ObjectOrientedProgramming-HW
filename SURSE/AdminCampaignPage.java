import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class AdminCampaignPage extends JFrame implements ActionListener {
	private JPanel mainPanel;
	private JPanel tablePanel;
	private JPanel fieldsPanel;
	private JPanel infoPanel;
	private JPanel voucherPanel;
	private JTable campTable;
	private JButton addBtn;
	private JButton editBtn;
	private JButton cancelBtn;
	private JButton infoBtn;
	private JButton voucherBtn;
	private JTextField idField;
	private JTextField nameField;
	private JTextField descField;
	private JTextField startField;
	private JTextField endField;
	private JTextField budgetField;
	private JTextField strategyField;
	private JTextArea infoArea;
	private VMS vms;
	
	public AdminCampaignPage() {
		super("Admin Campaigns");
		vms = VMS.getInstance();
		campTable = generateTable();
		editBtn = new JButton("Edit Campaign");
		editBtn.addActionListener(this);
		cancelBtn = new JButton("Cancel Campaign");
		cancelBtn.addActionListener(this);
		tablePanel = new JPanel();
		tablePanel.setBorder(new EmptyBorder(10, 0, 0, 0));
		tablePanel.add(new JScrollPane(campTable));
		tablePanel.add(editBtn);
		tablePanel.add(cancelBtn);
		addBtn = new JButton("Add Campaign");
		addBtn.addActionListener(this);
		tablePanel.add(addBtn);
		idField = new JTextField("ID");
		nameField = new JTextField("Name");
		descField = new JTextField("Description");
		startField = new JTextField("yyyy-MM-dd HH:mm");
		endField = new JTextField("yyyy-MM-dd HH:mm");
		budgetField = new JTextField("Budget");
		strategyField = new JTextField("Strategy");
		fieldsPanel = new JPanel();
		fieldsPanel.add(idField);
		fieldsPanel.add(nameField);
		fieldsPanel.add(descField);
		fieldsPanel.add(startField);
		fieldsPanel.add(endField);
		fieldsPanel.add(budgetField);
		fieldsPanel.add(strategyField);
		infoBtn = new JButton("info");
		infoBtn.addActionListener(this);
		infoArea = new JTextArea("Select a campaign from the table to get further details.", 4, 50);
		infoPanel = new JPanel();
		infoPanel.add(infoBtn);
		infoPanel.add(new JScrollPane(infoArea));
		voucherBtn = new JButton("Voucher Control");
		voucherBtn.setBackground(Color.CYAN);
		voucherBtn.addActionListener(this);
		voucherPanel = new JPanel();
		voucherPanel.add(voucherBtn);
		mainPanel = new JPanel();
		mainPanel.add(tablePanel);
		mainPanel.add(fieldsPanel);
		mainPanel.add(infoPanel);
		mainPanel.add(voucherPanel);
		mainPanel.setLayout(new GridLayout(4, 0));
		this.add(mainPanel);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		this.setVisible(true);
	}
	@Override
	public void actionPerformed(ActionEvent e) throws DateTimeParseException {
		if (e.getSource() == editBtn) {
			int id = Integer.parseInt(idField.getText());
			int budget = Integer.parseInt(budgetField.getText());
			DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
			LocalDateTime start = LocalDateTime.parse(startField.getText(), format);
			LocalDateTime end = LocalDateTime.parse(endField.getText(), format);
			Campaign c = new Campaign(id, nameField.getText(), descField.getText(), 
					start, end, vms.getAppDate(), budget, strategyField.getText());
			int index = campTable.getSelectedRow();
			int campId = (Integer)campTable.getValueAt(index, 0);
			if (campId != -1) {
				if (vms.updateCampaign(campId, c) == true) {
					editBtn.setBackground(Color.GREEN);
					infoArea.setText("Modificarea a reusit.");
					infoArea.setForeground(Color.GREEN);
					tablePanel.remove(0);
					campTable = generateTable();
					tablePanel.add(new JScrollPane(campTable), 0);
					tablePanel.revalidate();
					tablePanel.repaint();
				} else {
					editBtn.setBackground(Color.RED);
					infoArea.setText("Modificarea a esuat.");
					infoArea.setForeground(Color.RED);
				}
			}
		}
		if (e.getSource() == cancelBtn) {
			int index = campTable.getSelectedRow();
			if (index != -1) {
				vms.cancelCampaign((Integer)campTable.getValueAt(index, 0));
			}
		}
		if (e.getSource() == addBtn) {
			JTextField[] fields = {idField, nameField, descField, startField, 
					endField, budgetField, strategyField};
			for (JTextField field : fields) {
				if (field.getText().isEmpty()) {
					field.setBackground(Color.RED);
					return;
				}
			}
			int id = Integer.parseInt(idField.getText());
			int budget = Integer.parseInt(budgetField.getText());
			DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
			LocalDateTime start = LocalDateTime.parse(startField.getText(), format);
			LocalDateTime end = LocalDateTime.parse(endField.getText(), format);
			Campaign c = new Campaign(id, nameField.getText(), descField.getText(), 
					start, end, vms.getAppDate(), budget, strategyField.getText());
			if (vms.addCampaign(c) == true) {
				addBtn.setBackground(Color.GREEN);
				tablePanel.remove(0);
				campTable = generateTable();
				tablePanel.add(new JScrollPane(campTable), 0);
				tablePanel.revalidate();
				tablePanel.repaint();
			} else {
				addBtn.setBackground(Color.RED);
				infoArea.setText("Adaugarea a esuat. Se poate ca ID-ul ales sa fie "
						+ "deja asociat unei campanii.");
				infoArea.setForeground(Color.RED);
			}
		}
		if (e.getSource() == infoBtn) {
			int index = campTable.getSelectedRow();
			if (index != -1) {
				String info = "Campaign " + campTable.getValueAt(index, 1) + ":";
				Campaign c = vms.getCampaign((Integer)campTable.getValueAt(index, 0));
				info = info + "\n" + c.getDescription() + "\nStatus: " + c.getStatus();
				infoArea.setText(info);
				infoArea.setForeground(Color.BLACK);
			}
		}
		if (e.getSource() == voucherBtn) {
			int index = campTable.getSelectedRow();
			if (index != -1) {
				Campaign c = vms.getCampaign((Integer)campTable.getValueAt(index, 0));
				AdminVoucherPage voucherPage = new AdminVoucherPage(c);
			}
		}
	}
	public JTable generateTable() {
		String[] colNames = {"ID", "Name", "Start Date", "End Date"};
		Vector<Campaign> camps = vms.getCampaigns();
		Object[][] data = new Object[camps.size()][4];
		int i = 0;
		for (Campaign c : camps) {
			data[i][0] = c.getId();
			data[i][1] = c.getName();
			data[i][2] = c.getStartDate();
			data[i][3] = c.getEndDate();
			i++;
		}
		campTable = new JTable(data, colNames);
		campTable.setAutoCreateRowSorter(true);
		return campTable;
	}
}
