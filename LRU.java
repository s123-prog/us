import java.util.Scanner;

public class LRU {
    private int[] pages; // reference string
    private int n; // length of reference string
    private int[] frames; // frames array
    private int m; // number of frames
    private int pageFaults;
    private final Scanner sc = new Scanner(System.in);

    void readInput() {
        System.out.print("Enter length of reference string: ");
        n = sc.nextInt();
        pages = new int[n];
        System.out.println("Enter reference string (space separated):");
        for (int i = 0; i < n; i++)
            pages[i] = sc.nextInt();
        System.out.print("Enter number of frames: ");
        m = sc.nextInt();
        frames = new int[m];
    }

    void displayFrames(int page, boolean hit) {
        System.out.print("Page " + page + " -> ");
        for (int i = 0; i < m; i++) {
            if (frames[i] == -1)
                System.out.print("[ ] ");
            else
                System.out.print("[" + frames[i] + "] ");
        }
        System.out.println(hit ? " (HIT)" : " (MISS)");
    }

    void runLRU() {
        for (int i = 0; i < m; i++) {
            frames[i] = -1;
        }
        pageFaults = 0;

        for (int i = 0; i < n; i++) {
            int page = pages[i];
            boolean hit = false;
            for (int j = 0; j < m; j++) {
                if (frames[j] == page) {
                    hit = true;
                    break;
                }
            }

            if (!hit) {   // MISS case
                pageFaults++;

                boolean placed = false;
                for (int j = 0; j < m; j++) {
                    if (frames[j] == -1) {
                        frames[j] = page;
                        placed = true;
                        break;
                    }
                }

                if (!placed) {
                    int lruIndex = 0;
                    int smallestLastUse = Integer.MAX_VALUE;

                    for (int j = 0; j < m; j++) {
                        int lastPos = -1;
                        for (int k = i - 1; k >= 0; k--) {
                            if (pages[k] == frames[j]) {
                                lastPos = k;
                                break;
                            }
                        }

                        if (lastPos < smallestLastUse) {
                            smallestLastUse = lastPos;
                            lruIndex = j;
                        }
                    }

                    frames[lruIndex] = page;
                }
            }

            // Print frames every iteration (show HIT / MISS)
            displayFrames(page, hit);
        }
        System.out.println("\n--- Result ---");
        int hits = n - pageFaults;
        System.out.println("Total page faults: " + pageFaults);
        System.out.println("Total hits: " + hits);
        System.out.printf("Fault ratio: %.2f\n", (double) pageFaults *100.0 / n);
        System.out.printf("Hit ratio: %.2f\n", (double) hits*100.0 / n);
    }

    public static void main(String[] args) {
        LRU sim = new LRU();
        sim.readInput();
        sim.runLRU();
    }
}
