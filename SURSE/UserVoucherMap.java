import java.util.*;

public class UserVoucherMap extends ArrayMap<Integer, ArrayList<Voucher>> {
    public boolean addVoucher(Voucher v) {
        ArrayList<Voucher> vList = get(v.getCampaignId());
        if (vList == null) {
        	vList = new ArrayList<Voucher>();
        }
        vList.add(v);
        vList = put(v.getCampaignId(), vList);
        return containsKey(v.getCampaignId());
    }
}