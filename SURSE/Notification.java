import java.time.LocalDateTime;
import java.util.*;

public class Notification {
    public enum NotificationType {
        EDIT, CANCEL;
    }
    private NotificationType type;
    private LocalDateTime date;
    private Integer campaignId;
    private List<String> voucherCodes;

    public Notification(NotificationType type, Integer campaignId, LocalDateTime date) {
        this.type = type;
        this.campaignId = campaignId;
        this.date = date;
        voucherCodes = new ArrayList<String>();
    }
    public Integer getCampaignId() {
        return campaignId;
    }
    public NotificationType getType() {
    	return type;
    }
    public LocalDateTime getDate() {
    	return date;
    }
    public void setVoucherCodes(List<String> voucherCodes) {
    	if (voucherCodes == null) {
    		this.voucherCodes.clear();
    		return;
    	}
        this.voucherCodes.addAll(voucherCodes);
    }
    public String toString() {
    	return campaignId + ";" + voucherCodes + ";" + date + ";" + type;
    }
}