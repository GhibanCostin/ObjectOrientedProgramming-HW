import java.util.*;

public class CampaignVoucherMap extends ArrayMap<String, ArrayList<Voucher>> {
    public boolean addVoucher(Voucher v) {
        ArrayList<Voucher> vList = get(v.getEmail());
        if (vList == null) {
        	vList = new ArrayList<Voucher>();
        }
        vList.add(v);
        vList = put(v.getEmail(), vList);
        return containsKey(v.getEmail());
    }
}