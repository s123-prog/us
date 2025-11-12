
// Pass1Mini.java
import java.io.*;
import java.util.*;

public class Pass1Mini {
  static boolean lit(String s) {
    return s != null && s.startsWith("='") && s.endsWith("'");
  }

  static int idx(LinkedHashMap<String, ?> m, String k) {
    int i = 1;
    for (String x : m.keySet()) {
      if (x.equals(k))
        return i;
      i++;
    }
    return i;
  }

  public static void main(String[] a) throws Exception {
    Map<String, String> OP = Map.of(
        "START", "AD 01", "END", "AD 02", "STOP", "IS 00",
        "MOVER", "IS 04", "MOVEM", "IS 05", "ADD", "IS 01", "SUB", "IS 02",
        "DS", "DL 02");
    Map<String, String> REG = Map.of("AREG", "1", "BREG", "2", "CREG", "3");

    LinkedHashMap<String, Integer> SYM = new LinkedHashMap<>();
    LinkedHashMap<String, Integer> LIT = new LinkedHashMap<>();
    List<String> IC = new ArrayList<>();
    int LC = 0;

    try (BufferedReader br = new BufferedReader(new FileReader("in.txt"))) {
      String s;
      while ((s = br.readLine()) != null) {
        s = s.trim();
        if (s.isEmpty())
          continue;
        String[] p = s.split("[\\s,]+");
        String lbl = null, opc = null, a1 = null, a2 = null;

        if (OP.containsKey(p[0])) {
          opc = p[0];
          if (p.length > 1)
            a1 = p[1];
          if (p.length > 2)
            a2 = p[2];
        } else {
          lbl = p[0];
          opc = p.length > 1 ? p[1] : "";
          if (p.length > 2)
            a1 = p[2];
          if (p.length > 3)
            a2 = p[3];
          SYM.put(lbl, LC);
        }

        if ("START".equals(opc)) {
          LC = Integer.parseInt(a1);
          IC.add("AD\t01\tC\t" + a1);
          continue;
        }

        if ("END".equals(opc)) {
          for (String k : LIT.keySet())
            if (LIT.get(k) == null)
              LIT.put(k, LC++); // assign literal addrs
          IC.add("AD\t02");
          break;
        }

        if ("DS".equals(opc)) {
          IC.add("DL\t02\tC\t" + a1);
          LC += Integer.parseInt(a1);
          continue;
        }

        String[] oc = OP.getOrDefault(opc, "?? ??").split("\\s+");
        String reg = (a1 != null && REG.containsKey(a1)) ? REG.get(a1) : null;
        String mem = null;

        String opnd = (a2 != null) ? a2 : (!REG.containsKey(a1) ? a1 : null);
        if (opnd != null) {
          if (lit(opnd)) {
            LIT.putIfAbsent(opnd, null);
            mem = "L\t" + idx(LIT, opnd);
          } else {
            SYM.putIfAbsent(opnd, -1);
            mem = "S\t" + idx(SYM, opnd);
          }
        }

        IC.add(oc[0] + "\t" + oc[1] + (reg != null ? "\t" + reg : "") + (mem != null ? "\t" + mem : ""));
        LC++;
      }
    }

    try (PrintWriter w = new PrintWriter("IC.txt")) {
      for (String r : IC)
        w.println(r);
    }
    try (PrintWriter w = new PrintWriter("SYMTAB.txt")) {
      for (var e : SYM.entrySet())
        w.println(e.getKey() + "\t" + e.getValue());
    }
    try (PrintWriter w = new PrintWriter("LITTAB.txt")) {
      for (var e : LIT.entrySet())
        w.println(e.getKey() + "\t" + e.getValue());
    }

    System.out.println("Done -> IC.txt, SYMTAB.txt, LITTAB.txt");
  }
}
