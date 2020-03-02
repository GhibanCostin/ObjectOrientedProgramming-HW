import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.*;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class LaunchPage extends JFrame implements ActionListener {
	private JPanel mainPanel;
	private JPanel welcomePanel;
	private JPanel filesPanel;
	private JPanel loginPanel;
	private JButton filesBtn;
	private JButton loginBtn;
	private JButton campBtn;
	private JButton userBtn;
	private JFileChooser campChooser;
	private JFileChooser userChooser;
	private JLabel welcomeLabel;
	private JLabel emailLabel;
	private JLabel passLabel;
	private JTextField emailField;
	private JPasswordField passField;
	private int campApproval;
	private int userApproval;
	private VMS vms;
	private User user;
	
	public LaunchPage(String name) {
		super(name);
		campApproval = -1;
		userApproval = -1;
		welcomePanel = new JPanel();
		welcomeLabel = new JLabel("Welcome to the Voucher Management Service");
		welcomeLabel.setFont(new Font("title", Font.PLAIN, 30));
		//adaug spatiere in partea de sus (padding-top)
		welcomeLabel.setBorder(new EmptyBorder(20, 0, 0, 0));
		welcomePanel.add(welcomeLabel);
		
		filesPanel = new JPanel();
		filesPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 70, 0));
		campChooser = new JFileChooser(new File("tests/"));
		userChooser = new JFileChooser(new File("tests/"));
		filesBtn = new JButton("Add Files");
		campBtn = new JButton("Select Campaigns");
		userBtn = new JButton("Select Users");
		campBtn.addActionListener(this);
		userBtn.addActionListener(this);
		filesBtn.addActionListener(this);
		filesPanel.add(campBtn);
		filesPanel.add(userBtn);
		filesPanel.add(filesBtn);
		
		loginPanel = new JPanel();
		emailLabel = new JLabel("Email: ");
		passLabel = new JLabel("              Password: ");
		emailField = new JTextField(15);
		passField = new JPasswordField(15);
		loginBtn = new JButton("Login");
		loginBtn.addActionListener(this);
		loginPanel.add(emailLabel);
		loginPanel.add(emailField);
		loginPanel.add(passLabel);
		loginPanel.add(passField);
		loginPanel.add(loginBtn);
		
		mainPanel = new JPanel();
		mainPanel.setLayout(new GridLayout(3, 0));
		mainPanel.add(welcomePanel);
		mainPanel.add(filesPanel);
		mainPanel.add(loginPanel);
		this.add(mainPanel);
		this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == campBtn) {
			campApproval = campChooser.showOpenDialog(null);
		}
		if (e.getSource() == userBtn) {
			userApproval = userChooser.showOpenDialog(null);
		}
		if (e.getSource() == filesBtn) {
			boolean validFiles = false;
			if (campApproval == JFileChooser.APPROVE_OPTION && 
					campApproval == JFileChooser.APPROVE_OPTION) {
				File cFile = campChooser.getSelectedFile();
				File uFile = userChooser.getSelectedFile();
				if (cFile.getName().equals("campaigns.txt") && 
						uFile.getName().equals("users.txt")) {
					if (cFile.getParent().equals(uFile.getParent())){
						validFiles = true;
						campBtn.setBackground(Color.GREEN);
						userBtn.setBackground(Color.GREEN);
						String cPath = cFile.getPath();
						String uPath = uFile.getPath();
						vms = VMS.getInstance();
						try {
							/*se specifica in enunt ca in interfata grafica se
							**foloseste data curenta a sistemului, desi acest 
							**lucru face ca toate campaniile din fisier sa fie
							**expirate*/
							LocalDateTime appDate = LocalDateTime.now();
							vms.setAppDate(appDate);
							vms.readVMSData(cPath, uPath);
						} catch (DateTimeParseException | IOException e1) {
							e1.printStackTrace();
						}
					} 
				} 
			}
			if (!validFiles) {
				campBtn.setBackground(Color.RED);
				campBtn.setForeground(Color.WHITE);
				userBtn.setBackground(Color.RED);
				userBtn.setForeground(Color.WHITE);
			}
		}
		if (e.getSource() == loginBtn) {
			JLabel errorText = new JLabel("The fields should not be empty");
			errorText.setForeground(Color.RED);
			if (emailField.getText().isEmpty() || passField.getPassword().length == 0) {
				if (loginPanel.getComponentCount() == 5) {
					/*avem 5 componente fara mesajul de eroare
					**adaugam mesajul de eroare
					**evitam afisarea multipla a erorii*/
					loginPanel.add(errorText);
					loginPanel.revalidate();
				}
			} else {
				if (loginPanel.getComponentCount() == 6) {
					//stergem mesajul de eroare (indexul 5 in lista de componente)
					loginPanel.remove(5);
					loginPanel.revalidate();
					loginPanel.repaint();
				}
				//verificam daca datele introduse sunt valide
				if (vms == null) {
					errorText.setText("Choose the test files first");
					loginPanel.add(errorText);
					loginPanel.revalidate();
					loginPanel.repaint();
					return;
				}
				String email = emailField.getText();
				String pass = String.valueOf(passField.getPassword());
				Vector<User> users = vms.getUsers();
				for (User u : users) {
					if (u.getEmail().equals(email) && u.testPassword(pass)) {
						user = u;
						if (user.getType() == User.UserType.ADMIN) {
							AdminCampaignPage campPage = new AdminCampaignPage();
						} else {
							GuestCampaignPage campPage = new GuestCampaignPage(user);
						}
					}
				}
			}
		}
	}
}
