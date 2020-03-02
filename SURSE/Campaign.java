import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDateTime;
import java.util.*;
import java.util.Map.Entry;

public class Campaign extends Observable {
    private final Integer id;
    private String name;
    private String description;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private int totalVouchers;
    private int availableVouchers;
    public enum CampaignStatusType {
        NEW, STARTED, EXPIRED, CANCELLED;
    }
    private CampaignStatusType status;
    private CampaignVoucherMap distributedVouchers;
    private String strategy;
    private Integer voucherIndex;

    public Campaign(Integer id, String name, String description, LocalDateTime startDate, LocalDateTime endDate, LocalDateTime appDate, int totalVouchers, String strategy) {
    	distributedVouchers = new CampaignVoucherMap();
    	this.id = id;
        setName(name);
        setDescription(description);
        setStartDate(startDate);
        setEndDate(endDate);
        setTotalVouchers(totalVouchers);
        computeAvailableVouchers();
        if (appDate.isBefore(startDate)) {
            setStatus(CampaignStatusType.NEW);
        } else if (appDate.isAfter(startDate) && appDate.isBefore(endDate)) {
            setStatus(CampaignStatusType.STARTED);
        } else {
            setStatus(CampaignStatusType.EXPIRED);
        }
        setStrategy(strategy);
        //indexarea voucherelor se face de la 1 pentru fiecare campanie
        voucherIndex = 1; 
    }
    public List<Voucher> getVouchers() {
        List<Voucher> list = new ArrayList<Voucher>();
        Iterator<Entry<String, ArrayList<Voucher>>> i;
        i = distributedVouchers.entrySet().iterator();
        while (i.hasNext()) {
            CampaignVoucherMap.ArrayMapEntry<String, ArrayList<Voucher>> entry;
            entry = (CampaignVoucherMap.ArrayMapEntry<String, ArrayList<Voucher>>)(i.next());
            list.addAll(entry.getValue());
        }
        return list;
    }
    public Voucher getVoucher(String code) {
        List<Voucher> vouchers = getVouchers();
        for (Voucher v : vouchers) {
            if (v.getCode().equals(code)) {
                return v;
            }
        }
        return null;
    }
    public Voucher getVoucher(Integer id) {
    	List<Voucher> vouchers = getVouchers();
        for (Voucher v : vouchers) {
            if (v.getId() == id) {
                return v;
            }
        }
        return null;
    }
    public Voucher generateVoucher(String email, String voucherType, float value) {
    	VMS service = VMS.getInstance();
        Vector<User> allUsers = service.getUsers();
        User user = null;
        for (User u : allUsers) {
            if (u.getEmail().equals(email)) {
                user = u;
                break;
            }
        }
        if (user != null) {
            Voucher v;
            if (voucherType.contains("Gift")) {
                v = new GiftVoucher(value, voucherIndex, email, id);
            } else {
                v = new LoyaltyVoucher(value, voucherIndex, email, id);
            }
            voucherIndex++;
            UserVoucherMap voucherMap = user.getUserVoucherMap();
            voucherMap.addVoucher(v);
            availableVouchers--;
            distributedVouchers.addVoucher(v);
            addObserver(user);
            return v;
        }
        return null;
    }
    public void redeemVoucher(String code, LocalDateTime date) {
        if (date.isAfter(startDate) && date.isBefore(endDate) && status != CampaignStatusType.CANCELLED) {
            Voucher v = getVoucher(code);
            if (v.getStatus() != Voucher.VoucherStatusType.USED) {
                v.setStatus(Voucher.VoucherStatusType.USED);
                v.setUsageDate(date);
            } 
        }
    }
    public Vector<User> getObservers() {
        /*utilizatorii care au primit cel putin un voucher in campania
        **curenta se afla in dictionarul de vouchere*/
        Vector<User> observers = new Vector<User>();
        VMS service = VMS.getInstance();
        Vector<User> allUsers = service.getUsers();
        Iterator<Entry<String, ArrayList<Voucher>>> i;
        i = distributedVouchers.entrySet().iterator();
        while (i.hasNext()) {
            CampaignVoucherMap.ArrayMapEntry<String, ArrayList<Voucher>> entry;
            entry = (CampaignVoucherMap.ArrayMapEntry<String, ArrayList<Voucher>>)(i.next());
            String email = entry.getKey();
            for (User u : allUsers) {
                if (u.getEmail().equals(email)) {
                    observers.add(u);
                }
            }
        }
        return observers;
    }
    public void addObserver(User user) {
        super.addObserver(user);
    }
    public void removeObserver(User user) {
        super.deleteObserver(user);
    }
    public void notifyAllObservers(Notification notification) {
    	super.setChanged();
        super.notifyObservers(notification);
    }
    public Integer getId() {
        return id;
    }
    public CampaignStatusType getStatus() {
        return status;
    }
    public int getTotalVouchers() {
        return totalVouchers;
    }
    public int getAvailableVouchers() {
        return availableVouchers;
    }
    public LocalDateTime getStartDate() {
        return startDate;
    }
    public LocalDateTime getEndDate() {
        return endDate;
    }
    public String getName() {
        return name;
    }
    public String getDescription() {
        return description;
    }
    public String getStrategy() {
        return strategy;
    }
    public CampaignVoucherMap getDistributedVouchers() {
    	return distributedVouchers;
    }
    public void setStatus(CampaignStatusType status) {
        this.status = status;
    }
    public void setTotalVouchers(int totalVouchers) {
        if (totalVouchers >= distributedVouchers.size()) {
            this.totalVouchers = totalVouchers;
        }
    }
    public void setStrategy(String strategy) {
        //to do: make strategy class or enum to implement Strategy pattern
        this.strategy = strategy;
    }
    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }
    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public void computeAvailableVouchers() {
        availableVouchers = totalVouchers - distributedVouchers.size();
    }
    public String toString() {
    	return "[" + id + ";" + name + ";" + description + ";" + startDate + ";" + endDate + "]";
    }
}