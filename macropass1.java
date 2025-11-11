import java.io.*;
import java.util.*;

public class macropass1 {
    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new FileReader("mininput.txt"));
        BufferedWriter mnt = new BufferedWriter(new FileWriter("mnt.txt"));
        BufferedWriter mdt = new BufferedWriter(new FileWriter("mdt.txt"));
        BufferedWriter kp = new BufferedWriter(new FileWriter("kptab.txt"));
        BufferedWriter pntab = new BufferedWriter(new FileWriter("pntab.txt"));

        String line;
        boolean isMacro = false;
        int mdtp = 1, kpdtp = 1;

        while ((line = br.readLine()) != null) {
            line = line.trim();
            if (line.equals("MACRO")) {
                isMacro = true;
                String header = br.readLine().trim();
                String[] parts = header.split("[ ,]+");
                String macroName = parts[0];

                List<String> pntabList = new ArrayList<>();
                int pp = 0, kpCount = 0;
                int kStart = kpdtp;

                for (int i = 1; i < parts.length; i++) {
                    String[] kv = parts[i].replace("&", "").split("=");
                    pntabList.add(kv[0]);
                    if (kv.length == 2) {
                        kp.write(kv[0] + " " + kv[1] + "\n");
                        kpCount++;
                        kpdtp++;
                    } else
                        pp++;
                }

                mnt.write(macroName + "\t" + pp + "\t" + kpCount + "\t" + mdtp + "\t" + kStart + "\n");

                for (String p : pntabList)
                    pntab.write(macroName + ": " + p + "\n");

                while ((line = br.readLine()) != null) {
                    line = line.trim();
                    if (line.equals("MEND"))
                        break;

                    String[] a = line.split("[ ,]+");
                    StringBuilder sb = new StringBuilder(a[0]); // opcode / mnemonic

                    for (int i = 1; i < a.length; i++) {
                        String t = a[i];
                        sb.append("\t").append(
                        t.startsWith("&")
                         ? "(P," + (pntabList.indexOf(t.substring(1)) + 1) + ")"  : t);
                    }

                    mdt.write(sb.append('\n').toString());
                    mdtp++;
                }
                mdt.write("MEND\n");
                mdtp++;
                isMacro = false;
            }
        }

        br.close();
        mnt.close();
        mdt.close();
        kp.close();
        pntab.close();
    }
}
