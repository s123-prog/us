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

    void displayFrames() {
        System.out.print("Frames: ");
        for (int i = 0; i < m; i++) {
            if (frames[i] == -1)
                System.out.print("[ ] ");
            else
                System.out.print("[" + frames[i] + "] ");
        }
        System.out.println();
    }

    void runLRU() {
        for (int i = 0; i < m; i++) {
            frames[i] = -1;
        }
        pageFaults = 0;

        for (int i = 0; i < n; i++) {
            int page = pages[i];
            // Check for hit
             boolean hit = false;
            for (int j = 0; j < m; j++) {
                if (frames[j] == page) {
                    hit = true;
                    break;
                }
            }
            if (hit) {
                System.out.print("Page: " + page + " (HIT)  ");
                displayFrames();
                continue;
            }

            // Miss -> page fault
            pageFaults++;

           // So for the first few pages, we just fill up empty frames — no replacement yet.
            boolean placed = false;
            for (int j = 0; j < m; j++) {
                if (frames[j] == -1) {
                    frames[j] = page;
                    placed = true;
                    break;
                }
            }

            // No empty frame -> replace LRU
            if (!placed) {
                int lruIndex = 0;
                int smallestLastUse = Integer.MAX_VALUE; // smaller index => older

                for (int j = 0; j < m; j++) {              // check every frame one by one
                    int lastPos = -1;                      // store when this page was last used
                    for (int k = i - 1; k >= 0; k--) {     // look backward in the reference string
                        if (pages[k] == frames[j]) {       // did we find this page used before?
                            lastPos = k;                   // yes, record its position (index)
                            break;                         // stop after finding the first recent use
                        }
                    }
                    if (lastPos < smallestLastUse) {       // smaller = used earlier or never used
                        smallestLastUse = lastPos;         // update least recently used info
                        lruIndex = j;                      // remember this frame index
                    }
                }
                frames[lruIndex] = page;                   // replace that page with the new one


            System.out.print("Page: " + page + " (MISS) ");
            displayFrames();
        }

        int hits = n - pageFaults;
        System.out.println("\nTotal page faults: " + pageFaults);
        System.out.println("Total hits: " + hits);
        System.out.printf("Fault ratio: %.4f\n", (double) pageFaults / n);
        System.out.printf("Hit ratio: %.4f\n", (double) hits / n);
    }
    }
    public static void main(String[] args) {
        LRU sim = new LRU();
        sim.readInput();
        sim.runLRU();
    }
}
