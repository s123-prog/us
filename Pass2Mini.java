import java.io.*;
import java.util.*;

public class Pass2Mini {
  static List<Integer> loadAddrs(String f) throws Exception {
    List<Integer> a = new ArrayList<>();
    a.add(0);
    try (BufferedReader br = new BufferedReader(new FileReader(f))) {
      for (String line; (line = br.readLine()) != null;)
        a.add(Integer.parseInt(line.trim().split("\\s+")[1]));
    }
    return a;

  }

  static String pad3(int x) {
    return String.format("%03d", x);
  }

  public static void main(String[] args) throws Exception {
    List<Integer> SYM = loadAddrs("SYMTAB.txt");
    List<Integer> LIT = loadAddrs("LITTAB.txt");

    try (BufferedReader br = new BufferedReader(new FileReader("IC.txt"));
        PrintWriter out = new PrintWriter("MachineCode.txt")) {

      String line;
      int LC = 0;
      while ((line = br.readLine()) != null) {
        line = line.trim();
        if (line.isEmpty())
          continue;
        String[] t = line.split("\\s+");

        if (t[0].equals("AD")) { // Assembler directives → no code
          if (t.length >= 4 && t[1].equals("01")) // START
            LC = Integer.parseInt(t[3]);
          if (t[1].equals("02"))
            break; // END
          continue;
        }

        if (t[0].equals("DL") && t[1].equals("02")) {
          out.println(LC++ + ": 00 0 000");
          continue;
        }

        String op  = t.length > 1 ? t[1] : "00";
        String reg = t.length > 2 && t[2].matches("\\d") ? t[2] : "0";
        String mem = t.length >= 5 ? pad3((t[3].equals("S") ? SYM : LIT).get(Integer.parseInt(t[4]))) : "000";

        System.out.println(LC++ + ": " + op + " " + reg + " " + mem);

    }
    System.out.println("Done -> MachineCode.txt");
  }
}
