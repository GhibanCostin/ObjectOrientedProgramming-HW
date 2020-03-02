import java.time.LocalDateTime;
import java.util.*;

public abstract class Voucher {
    private Integer id = 0;
    protected String code;
    public enum VoucherStatusType {
        USED, UNUSED;
    }
    private VoucherStatusType status;
    private LocalDateTime usageDate;
    private String email;
    private Integer campaignId;

    public Voucher(Integer id, String email, Integer campaignId) {
        setId(id);
        setEmail(email);
        setCampaignId(campaignId);
        usageDate = null;
        status = VoucherStatusType.UNUSED;
    }
    public String getEmail() {
        return email;
    }
    public Integer getCampaignId() {
        return campaignId;
    }
    public String getCode() {
        return code;
    }
    public Integer getId() {
        return id;
    }
    public VoucherStatusType getStatus() {
        return status;
    }
    public LocalDateTime getUsageDate() {
        return usageDate;
    }
    public void setId(Integer id) {
        if (this.id == 0) {
            //setam id-ul o singura data
            this.id = id;
        }
    }
    public void setStatus(VoucherStatusType status) {
        this.status = status;
    }
    public void setUsageDate(LocalDateTime date) {
        this.usageDate = date;
    }
    public void setCampaignId(Integer campaignId) {
        this.campaignId = campaignId;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    abstract public void setCode();
}