import java.util.*;

class Process {
    int pid, at, bt, priority, ct, tat, wt;
    boolean completed;

    Process(int pid, int at, int bt, int priority) {
        this.pid = pid;
        this.at = at;
        this.bt = bt;
        this.priority = priority; // (higher number = higher priority)
        this.completed = false;
    }
}

public class priority {
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
            System.out.print("Priority : ");
            int priority = sc.nextInt();
            p[i] = new Process(i + 1, at, bt, priority);
        }

        int completed = 0;
        int time = 0;
        int totalTAT = 0, totalWT = 0;
        List<Integer> gantt = new ArrayList<>();

        System.out.println("\nPID\tAT\tBT\tPriority\tCT\tTAT\tWT");

        while (completed < n) {
            int idx = -1;
            int highestPriority = -1;
            for (int i = 0; i < n; i++) {
                if (!p[i].completed && p[i].at <= time) {
                    if (p[i].priority > highestPriority) {
                        highestPriority = p[i].priority;
                        idx = i;
                    } else if (p[i].priority == highestPriority) {
                        // Tie-breaker: earlier arrival
                        if (p[i].at < p[idx].at)
                            idx = i;
                    }
                }
            }
            if (idx == -1) {
                time++;
                continue;
            }
            gantt.add(p[idx].pid);
            p[idx].ct = time + p[idx].bt;
            p[idx].tat = p[idx].ct - p[idx].at;
            p[idx].wt = p[idx].tat - p[idx].bt;
            p[idx].completed = true;
            totalTAT += p[idx].tat;
            totalWT += p[idx].wt;
            time = p[idx].ct;
            completed++;

            System.out.println("P" + p[idx].pid + "\t" + p[idx].at + "\t" + p[idx].bt
                    + "\t" + p[idx].priority + "\t\t" + p[idx].ct
                    + "\t" + p[idx].tat + "\t" + p[idx].wt);
        }
        double avgTAT = (double) totalTAT / n;
        double avgWT = (double) totalWT / n;
        System.out.println("\nAverage Turnaround Time = " + avgTAT);
        System.out.println("Average Waiting Time    = " + avgWT);

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
