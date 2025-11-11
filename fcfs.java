import java.util.*;
class Process {
    int pid, at, bt, ct, tat, wt;
    Process(int pid, int at, int bt) {
        this.pid = pid;
        this.at = at;
        this.bt = bt;
    }
}
public class fcfs {
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

        // Sort processes by arrival time (ascending). FCFS schedules by arrival order
        Arrays.sort(p, Comparator.comparingInt(x -> x.at));

        int time = 0;
        int totalTAT = 0;
        int totalWT = 0;

        for (int i = 0; i < n; i++) {
            if (time < p[i].at)
                time = p[i].at;
            p[i].ct = time + p[i].bt;
            p[i].tat = p[i].ct - p[i].at;
            p[i].wt = p[i].tat - p[i].bt;
            time = p[i].ct;
            totalTAT += p[i].tat;
            totalWT += p[i].wt;
        }
        System.out.println("\nPID\tAT\tBT\tCT\tTAT\tWT");
        for (int i = 0; i < n; i++) {
            System.out.println("P" + p[i].pid + "\t" + p[i].at + "\t" + p[i].bt + "\t" + p[i].ct + "\t" + p[i].tat
                    + "\t" + p[i].wt);
        }
        if (n > 0) {
            double avgTAT = totalTAT / n;
            double avgWT = totalWT / n;
            System.out.println("\nAverage Turnaround Time = " + avgTAT);
            System.out.println("Average Waiting Time    = " + avgWT);
        }

        System.out.println("\nGantt Chart:");
        for (int i = 0; i < n; i++) {
            System.out.print("|  P" + p[i].pid + "  ");
        }
        System.out.println("|");

        System.out.print(p[0].at);
        for (int i = 0; i < n; i++) {
            System.out.printf("%6d", p[i].ct);
        }
        sc.close();
    }
}
