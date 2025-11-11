import java.util.Arrays;
import java.util.Scanner;

public class optimal {
    private int frames; // number of frames
    private int n; // length of reference string
    private int[] ref; // reference string
    private int[][] memLayout; // snapshot of frames after each reference
    private int hits;
    private int faults;
    private boolean[] isHit; // per-step hit/fault
    private final Scanner sc = new Scanner(System.in);

    void readInput() {
        System.out.print("Enter number of frames: ");
        frames = sc.nextInt();
        System.out.print("Enter reference string length: ");
        n = sc.nextInt();
        ref = new int[n];
        System.out.println("Enter reference string (space separated):");
        for (int i = 0; i < n; i++)
            ref[i] = sc.nextInt();

        memLayout = new int[n][frames];
        isHit = new boolean[n];
    }

    void simulate() {
        int[] buffer = new int[frames];
        Arrays.fill(buffer, -1);
        hits = 0;
        faults = 0;

        for (int i = 0; i < n; i++) {
            int page = ref[i];
            // check if page is already in frames -> HIT
            boolean found = false;
            for (int j = 0; j < frames; j++) {
                if (buffer[j] == page) {
                    found = true;
                    hits++;
                    isHit[i] = true;
                    break;
                }
            }

            // MISS
            if (!found) {
                faults++;
                isHit[i] = false;

                // if empty frame exists, use it
                int empty = -1;
                for (int j = 0; j < frames; j++) {
                    if (buffer[j] == -1) {
                        empty = j;
                        break;
                    }
                }
                if (empty != -1) {
                    buffer[empty] = page;
                } else {
                    int replaceIndex = 0;
                    int farthestNextUse = -1; // choose largest nextUse (or never used again)

                    for (int j = 0; j < frames; j++) {
                        int nextUse = Integer.MAX_VALUE;
                        for (int k = i + 1; k < n; k++) {
                            if (ref[k] == buffer[j]) {
                                nextUse = k;
                                break;
                            }
                        }
                        if (nextUse > farthestNextUse) {
                            farthestNextUse = nextUse;
                            replaceIndex = j;
                        }
                    }
                    buffer[replaceIndex] = page;
                }
            }
            for (int j = 0; j < frames; j++)
                memLayout[i][j] = buffer[j];
        }
    }

    void printResults() {
    for (int i = 0; i < n; i++) {
        System.out.print("Ref " + (i + 1) + " (" + ref[i] + "): [");
        for (int j = 0; j < frames; j++)
            System.out.print((memLayout[i][j] == -1 ? "-" : memLayout[i][j]) + " ");
        System.out.println("]  " + (isHit[i] ? "Hit" : "Fault"));
    }


        System.out.println("Total pages : " + n);
        System.out.println("Hits   : " + hits);
        System.out.println("Hit %  : " + (hits * 100.0 / n));
        System.out.println("Faults : " + faults);
        System.out.println("Fault Ratio : " + (faults * 1.0 / n));

    }

    public static void main(String[] args) {
        optimal sim = new optimal();
        sim.readInput();
        sim.simulate();
        sim.printResults();
    }
}
