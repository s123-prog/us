import java.util.*;
class Process {
    int pid, at, bt, rt, ct, tat, wt;
    boolean completed;
    Process(int pid, int at, int bt) {
        this.pid = pid;
        this.at = at;
        this.bt = bt;
        this.rt = bt;
    }
}

public class srtf {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter number of processes: ");
        int n = sc.nextInt();
        Process[] p = new Process[n];
        for (int i = 0; i < n; i++) {
            System.out.println("Process " + (i + 1));
            System.out.print("Arrival Time: ");
            int at = sc.nextInt();
            System.out.print("Burst Time: ");
            int bt = sc.nextInt();
            p[i] = new Process(i + 1, at, bt);
        }

        int time = 0, completed = 0;
        List<Integer> gantt = new ArrayList<>();
        Process current = null;
        int totalTAT = 0, totalWT = 0;

        while (completed < n) {
            int minRT = Integer.MAX_VALUE;
            Process shortest = null;
            for (int i = 0; i < n; i++) {
                if (p[i].at <= time && !p[i].completed && p[i].rt < minRT) {
                    minRT = p[i].rt;
                    shortest = p[i];
                }
            }
            if (shortest == null) {
                time++; // No process arrived, CPU idle
                continue;
            }
            if (current != shortest) {
                gantt.add(shortest.pid);
                current = shortest;
            }
            shortest.rt--;
            time++;
            if (shortest.rt == 0) {
                shortest.ct = time;
                shortest.tat = shortest.ct - shortest.at;
                shortest.wt = shortest.tat - shortest.bt;
                shortest.completed = true;
                completed++;
                totalTAT += shortest.tat;
                totalWT += shortest.wt;
            }
        }
        sc.close();

        System.out.println("\nPID\tAT\tBT\tCT\tTAT\tWT");
        for (int i = 0; i < n; i++) {
            System.out.println("P" + p[i].pid + "\t" + p[i].at + "\t" + p[i].bt + "\t" + p[i].ct + "\t" + p[i].tat
                    + "\t" + p[i].wt);
        }
        if (n > 0) {
            double avgTAT = (double) totalTAT / n;
            double avgWT = (double) totalWT / n;
            System.out.printf("\nAverage Turnaround Time = %.2f", avgTAT);
            System.out.printf("\nAverage Waiting Time    = %.2f\n", avgWT);
        }
        System.out.println("\nGantt Chart:");
        for (int i = 0; i < gantt.size(); i++) {
            System.out.print("|  P" + gantt.get(i) + "  ");
        }
        System.out.println("|");
        
            System.out.print(p[0].at);
            for (int i = 0; i < n; i++) {
                System.out.printf("%6d", p[i].ct);
            }
            System.out.println();
        
    }
}
