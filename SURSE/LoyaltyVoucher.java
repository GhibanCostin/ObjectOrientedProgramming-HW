public class LoyaltyVoucher extends Voucher {
    private float deduction;

    public LoyaltyVoucher(float deduction, Integer id, String email, Integer campaignId) {
        super(id, email, campaignId);
        this.deduction = deduction;
        setCode();
    }
    public void setCode() {
        code = "LoyaltyVoucher" + getCampaignId() + "-" + getId();
    }
    public String toString() {
    	return code + ";" + getStatus() + ";" + getEmail() 
    		+ ";value " + deduction + ";" + getCampaignId() + ";" + getUsageDate();
    }
}