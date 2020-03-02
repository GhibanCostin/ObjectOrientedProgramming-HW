public class GiftVoucher extends Voucher {
    private float value;

    public GiftVoucher(float value, Integer id, String email, Integer campaignId) {
        super(id, email, campaignId);
        this.value = value;
        setCode();
    }
    public void setCode() {
        code = "GiftVoucher" + getCampaignId() + "-" + getId();
    }
    public String toString() {
    	return code + ";" + getStatus() + ";" + getEmail() 
    		+ ";value " + value + ";" + getCampaignId() + ";" + getUsageDate();
    }
}