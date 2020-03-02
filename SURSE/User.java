import java.time.LocalDateTime;
import java.util.*;
import java.util.Map.Entry;

public class User implements Observer {
    private final Integer id;
    private String name;
    private String email;
    private String password;
    public enum UserType {
        ADMIN, GUEST;
    }
    private UserType type;
    private UserVoucherMap voucherMap;
    private List<Notification> notifications;

    public User(Integer id, String name, String email, String password, UserType type) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.type = type;
        voucherMap = new UserVoucherMap();
        notifications = new ArrayList<Notification>();
    }
    public String getEmail() {
        return email;
    }
    public UserVoucherMap getUserVoucherMap() {
        return voucherMap;
    }
    public List<Voucher> getVouchers() {
    	List<Voucher> vouchers = new ArrayList<Voucher>();
    	Iterator<Entry<Integer, ArrayList<Voucher>>> i;
    	i = voucherMap.entrySet().iterator();
    	while (i.hasNext()) {
    		UserVoucherMap.ArrayMapEntry<Integer, ArrayList<Voucher>> entry;
            entry = (UserVoucherMap.ArrayMapEntry<Integer, ArrayList<Voucher>>)(i.next());
            vouchers.addAll(entry.getValue());
    	}
    	return vouchers;
    }
    public List<Notification> getNotifications() {
    	return notifications;
    }
    public Integer getId() {
    	return id;
    }
    public UserType getType() {
    	return type;
    }
    public boolean testPassword(String password) {
    	if (this.password.equals(password)) {
    		return true;
    	}
    	return false;
    }
    public void update(Notification notification) {
    	/*notification este aceeasi pentru toti observatorii,
    	** dar fiecare observator are alte vouchere in lista*/
    	Integer cId = notification.getCampaignId();
    	LocalDateTime d = notification.getDate();
    	Notification newNot = new Notification(notification.getType(), cId, d);
        List<Voucher> vouchers = voucherMap.get(notification.getCampaignId());
        List<String> voucherCodes = new ArrayList<String>();
        for (Voucher v : vouchers) {
            voucherCodes.add(v.getCode());
        }
        newNot.setVoucherCodes(voucherCodes);
        notifications.add(newNot);
    }
	@Override
	public void update(Observable arg0, Object arg1) {
		update((Notification)arg1);
	}
	public String toString() {
		return "[" + id + ";" + name + ";" + email + ";" + type +"]";
	}
}