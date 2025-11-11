import java.util.*;
class Process {
    int pid, at, bt, rt, ct, tat, wt;
    Process(int pid, int at, int bt) {
        this.pid = pid;
        this.at = at;
        this.bt = bt;
        this.rt = bt;
    }
}
class GanttEntry {
    int pid, endTime;
    GanttEntry(int pid, int endTime) {
        this.pid = pid;
        this.endTime = endTime;
    }
}
public class roundrobin {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter number of processes: ");
        int n = sc.nextInt();
        Process[] p = new Process[n];
        for (int i = 0; i < n; i++) {
            System.out.println("\nProcess " + (i + 1));
            System.out.print("Arrival Time: ");
            int at = sc.nextInt();
            System.out.print("Burst Time: ");
            int bt = sc.nextInt();
            p[i] = new Process(i + 1, at, bt);
        }
        System.out.print("\nEnter Time Quantum: ");
        int tq = sc.nextInt();

        Queue<Process> queue = new LinkedList<>();
        List<GanttEntry> gantt = new ArrayList<>();
        boolean[] visited = new boolean[n];

        int time = 0, completed = 0;
        while (completed < n) {
            for (int i = 0; i < n; i++) {
                if (p[i].at <= time && !visited[i]) {
                    queue.add(p[i]);
                    visited[i] = true;
                }
            }
            if (queue.isEmpty()) {
                time++;
                continue;
            }
            Process curr = queue.poll();
            int execTime = Math.min(tq, curr.rt);

            time += execTime;
            curr.rt -= execTime;
            gantt.add(new GanttEntry(curr.pid, time));

            for (int i = 0; i < n; i++) {
                if (p[i].at > time - execTime && p[i].at <= time && !visited[i]) {
                    queue.add(p[i]);
                    visited[i] = true;
                }
            }
            if (curr.rt == 0) {
                curr.ct = time;
                curr.tat = curr.ct - curr.at;
                curr.wt = curr.tat - curr.bt;
                completed++;
            } else {
                queue.add(curr);
            }
        }

        System.out.println("\nPID\tAT\tBT\tCT\tTAT\tWT");
        double totalTAT = 0, totalWT = 0;
        for (Process proc : p) {
            System.out.println("P" + proc.pid + "\t" + proc.at + "\t" + proc.bt + "\t" +
                    proc.ct + "\t" + proc.tat + "\t" + proc.wt);
            totalTAT += proc.tat;
            totalWT += proc.wt;
        }

        System.out.printf("\nAverage Turnaround Time = %.2f", totalTAT / n);
        System.out.printf("\nAverage Waiting Time    = %.2f\n", totalWT / n);

        // FIXED Gantt printing: use gantt list instead of p[]
        System.out.println("\nGantt Chart:");
        for (GanttEntry g : gantt) {
            System.out.print("|  P" + g.pid + "  ");
        }
        System.out.println("|");

        System.out.print("0");
        for (GanttEntry g : gantt) {
            System.out.printf("%6d", g.endTime);
        }
        System.out.println();

        sc.close();
    }
}
