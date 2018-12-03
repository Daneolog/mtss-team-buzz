package filedatabase;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class AdminCommandLineApp {
    public static void printUsage() {
        System.out.println("Arguments: FILE IP_ADDRESS PORT DATABASE USER PASSWORD");
    }

    public static void main(String[] args) {
        if(args.length != 6) {
            printUsage();
            System.exit(-1);
        }
        FileReader fileReader = null;
        try {
            fileReader = new FileReader(new File(args[0]));
        } catch (IOException e) {
            System.out.println(String.format("Could not open FILE %s", args[0]));
            System.out.println(e.getMessage());
            System.exit(-1);
        }
        String ip = args[1];
        int port = -1;
        try {
            port = Integer.parseInt(args[2]);
        } catch (NumberFormatException e) {
            System.out.println(String.format("Could not convert PORT %s to an integer.", args[2]));
            System.out.println(e.getMessage());
            System.exit(-1);
        }
        String db = args[3];
        String user = args[4];
        String password = args[5];
        DBClass dbclass = new DBClass(ip, port, db, user, password);
        APCParser apcParser = new APCParser(fileReader, dbclass);
        apcParser.parse();
    }
}
