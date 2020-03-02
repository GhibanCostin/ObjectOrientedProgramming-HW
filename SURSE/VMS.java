import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

public class VMS {
	private Vector<Campaign> campaigns;
	private Vector<User> users;
	private LocalDateTime appDate;
    private static VMS vms = null;
    private VMS() {
		campaigns = new Vector<Campaign>();
		users = new Vector<User>();
    }
	public static VMS getInstance() {
		if (vms == null) {
            vms = new VMS();
        }
        return vms;
	}
	public Vector<Campaign> getCampaigns() {
		return campaigns;
	}
	public Campaign getCampaign(Integer id) {
		for (Campaign c : campaigns) {
			if (c.getId() == id) {
				return c;
			}
		}
		return null;
	}
	public boolean addCampaign(Campaign campaign) {
		if (getCampaign(campaign.getId()) == null) {
			campaigns.add(campaign);
			return true;
		}
		System.out.println("Error: campaign with id " + campaign.getId() + " already exists!");
		return false;
	}
	public boolean updateCampaign(Integer id, Campaign campaign) {
		Campaign c = getCampaign(id);
		if (c.getStatus() == Campaign.CampaignStatusType.NEW) {
			//c.setName(campaign.getName());
			c.setDescription(campaign.getDescription());
			c.setTotalVouchers(campaign.getTotalVouchers());
			c.setStartDate(campaign.getStartDate());
			c.setEndDate(campaign.getEndDate());
			//c.setStatus(campaign.getStatus());
			//c.setStrategy(campaign.getStrategy());
			c.computeAvailableVouchers();
			Notification notification = 
				new Notification(Notification.NotificationType.EDIT, id, appDate);
			c.notifyAllObservers(notification);
			return true;
		} else if (c.getStatus() == Campaign.CampaignStatusType.STARTED) {
			c.setTotalVouchers(campaign.getTotalVouchers());
			c.setEndDate(campaign.getEndDate());
			c.computeAvailableVouchers();
			Notification notification = 
				new Notification(Notification.NotificationType.EDIT, id, appDate);
			c.notifyAllObservers(notification);
			return true;
		} else {
			return false;
		}
	}
	public boolean cancelCampaign(Integer id) {
		Campaign c = getCampaign(id);
		if (c.getStatus() == Campaign.CampaignStatusType.NEW 
				|| c.getStatus() == Campaign.CampaignStatusType.STARTED) {
			c.setStatus(Campaign.CampaignStatusType.CANCELLED);
			Notification notification = 
				new Notification(Notification.NotificationType.CANCEL, id, appDate);
			c.notifyAllObservers(notification);
			return true;
		}
		return false;
	}
	public Vector<User> getUsers() {
		return users;
	}
	public boolean addUser(User user) {
		if (!users.contains(user)) {
			users.add(user);
			return true;
		}
		return false;
	}
	public User getUser(Integer id) {
		Vector<User> users = getUsers();
		for (User u : users) {
			if (u.getId() == id) {
				return u;
			}
		}
		return null;
	}
	public void readVMSData(String cPath, String uPath) throws IOException, DateTimeParseException {
		File cFile = new File(cPath);
		File uFile = new File(uPath);
		BufferedReader cReader = new BufferedReader(new FileReader(cFile));
		BufferedReader uReader = new BufferedReader(new FileReader(uFile));
		int numberOfCamp = Integer.parseInt(cReader.readLine());
		int numberOfUsers = Integer.parseInt(uReader.readLine());
		DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		if (appDate == null) {
			setAppDate(LocalDateTime.parse(cReader.readLine(), format));
		} else {
			//data aplicatiei a fost setata anterior, prin deschiderea GUI
			cReader.readLine();
		}
		
		for (int i = 1; i <= numberOfCamp; ++i) {
			String[] info = cReader.readLine().split(";");
			Integer id = Integer.parseInt(info[0]);
			String name = info[1];
			String description = info[2];
			LocalDateTime startDate = LocalDateTime.parse(info[3], format);
			LocalDateTime endDate = LocalDateTime.parse(info[4], format);
			int budget = Integer.parseInt(info[5]); //totalVouchers
			String strategy = info[6];
			Campaign c = new Campaign(id, name, description, startDate, 
									endDate, appDate, budget, strategy);
			addCampaign(c);
		}
		for (int i = 1; i <= numberOfUsers; ++i) {
			String[] info = uReader.readLine().split(";");
			Integer id = Integer.parseInt(info[0]);
			String name = info[1];
			String password = info[2];
			String email = info[3];
			User.UserType type; 
			type = User.UserType.valueOf(User.UserType.class, info[4]);
			User u = new User(id, name, email, password, type);
			addUser(u);
		}
		cReader.close();
		uReader.close();
	}
	public void readExecuteEventsFromFile(String ePath) throws IOException, DateTimeParseException {
		File eFile = new File(ePath);
		BufferedReader eReader = new BufferedReader(new FileReader(eFile));
		DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		setAppDate(LocalDateTime.parse(eReader.readLine(), format));
		int numOfEvents = Integer.parseInt(eReader.readLine());
		for (int i = 1; i <= numOfEvents; ++i) {
			String[] info = eReader.readLine().split(";");
			Integer userId = Integer.parseInt(info[0]);
			String action = info[1];
			switch (action) {
				case "addCampaign":
					if (getUser(userId).getType() == User.UserType.ADMIN) {
						Integer campaignId = Integer.parseInt(info[2]);
						LocalDateTime startDate = LocalDateTime.parse(info[5], format);
						LocalDateTime endDate = LocalDateTime.parse(info[6], format);
						int budget = Integer.parseInt(info[7]);
						Campaign c = new Campaign(campaignId, info[3], info[4], startDate, 
								endDate, appDate, budget, info[8]);
						addCampaign(c);
						System.out.println("Adding campaign: " + c);
					} else {
						System.out.println("Access denied! The user " + userId + " is not admin!");
					}
					break;
				case "editCampaign":
					if (getUser(userId).getType() == User.UserType.ADMIN) {
						Integer campaignId = Integer.parseInt(info[2]);
						LocalDateTime startDate = LocalDateTime.parse(info[5], format);
						LocalDateTime endDate = LocalDateTime.parse(info[6], format);
						int budget = Integer.parseInt(info[7]);
						String strategy = getCampaign(campaignId).getStrategy();
						//construim campania cu acelasi id si date modificate
						Campaign oldCamp = getCampaign(campaignId);
						Campaign c = new Campaign(campaignId, info[3], info[4], startDate, 
								endDate, appDate, budget, strategy);
						updateCampaign(campaignId, c);
						System.out.println("Updating campaign: " + oldCamp + ". The new campaign is " + c);
					} else {
						System.out.println("Access denied! The user " + userId + " is not admin!");
					}
					break;
				case "cancelCampaign":
					if (getUser(userId).getType() == User.UserType.ADMIN) {
						Integer campaignId = Integer.parseInt(info[2]);
						cancelCampaign(campaignId);
						System.out.println("Cancelling campaign: " + getCampaign(campaignId));
					} else {
						System.out.println("Access denied! The user " + userId + " is not admin!");
					}
					break;
				case "generateVoucher":
					if (getUser(userId).getType() == User.UserType.ADMIN) {
						Integer campaignId = Integer.parseInt(info[2]);
						String email = info[3];
						String voucherType = info[4];
						float value = Float.parseFloat(info[5]);
						Campaign c = getCampaign(campaignId);
						c.generateVoucher(email, voucherType, value);
						System.out.println("Voucher generated.");
					} else {
						System.out.println("Access denied! The user " + userId + " is not admin!");
					}
					break;
				case "redeemVoucher":
					if (getUser(userId).getType() == User.UserType.ADMIN) {
						Integer campaignId = Integer.parseInt(info[2]);
						Integer voucherId = Integer.parseInt(info[3]);
						LocalDateTime localDate = LocalDateTime.parse(info[4], format);
						Campaign c = getCampaign(campaignId);
						Voucher v = c.getVoucher(voucherId);
						c.redeemVoucher(v.getCode(), localDate);
						System.out.println("Voucher used.");
					} else {
						System.out.println("Access denied! The user " + userId + " is not admin!");
					}
					break;
				case "getVouchers":
					List<Voucher> vouchers = getUser(userId).getVouchers();
					System.out.println(vouchers);
					break;
				case "getObservers":
					if (getUser(userId).getType() == User.UserType.ADMIN) {
						Integer campaignId = Integer.parseInt(info[2]);
						Vector<User> observers = getCampaign(campaignId).getObservers();
						System.out.println("Campaign's observers: " + observers);
					} else {
						System.out.println("Access denied! The user " + userId + " is not admin!");
					}
					break;
				case "getNotifications":
					List<Notification> notifications;
					notifications = getUser(userId).getNotifications();
					System.out.println("Your notifications are: " + notifications);
					break;
				case "getVoucher":
					if (getUser(userId).getType() == User.UserType.ADMIN) {
						Integer campaignId = Integer.parseInt(info[2]);
						System.out.println("Nu inteleg ce ar trebui sa faca getVoucher...");
					} else {
						System.out.println("Access denied! The user " + userId + " is not admin!");
					}
					break;
				default:
					System.out.println("Error: action not recognised!");
			}
		}
		eReader.close();
	}
	public LocalDateTime getAppDate() {
		return appDate;
	}
	public void setAppDate(LocalDateTime appDate) {
		this.appDate = appDate;
	}
}
