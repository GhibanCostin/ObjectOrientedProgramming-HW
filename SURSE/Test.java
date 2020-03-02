import java.io.*;
import java.time.format.DateTimeParseException;
import java.util.*;

public class Test {
    public static void main(String[] args) {
    	LaunchPage firstPage = new LaunchPage("Voucher Management Service");
        VMS vms = VMS.getInstance();
        // try {
        //     vms.readVMSData("tests/test02/input/campaigns.txt", "tests/test02/input/users.txt");
        //     vms.readExecuteEventsFromFile("tests/test02/input/events.txt");
        // } catch (IOException e) {
        //     System.out.println("Error: invalid input files!");
        // } catch (DateTimeParseException e) {
        //     System.out.println("Error: parsing the input has failed!");
        //     System.out.println(e);
        // }
    }
}