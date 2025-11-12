import java.util.*;

public class fifo {
     int[] pages;
    private int frames;
    private int faults, hits;
    private final Scanner sc = new Scanner(System.in);

    // Function to take user input
    void readInput() {
        System.out.print("Enter number of frames: ");
        frames = sc.nextInt();
        System.out.print("Enter number of pages in reference string: ");
        int n = sc.nextInt();
        pages = new int[n];
        System.out.println("Enter the page reference string (space separated):");
        for (int i = 0; i < n; i++) {
            pages[i] = sc.nextInt();
        }
    }

    // FIFO Simulation function
    void simulate() {
        Set<Integer> set = new HashSet<>(frames); // To check if page is already loaded (fast lookup)
        Queue<Integer> queue = new LinkedList<>(); // To maintain FIFO order

        faults = 0;

        System.out.println("\nPage\tFrames");
        for (int p : pages) {
            if (!set.contains(p)) { // Page fault
                faults++;
                if (set.size() == frames) {
                    int e = queue.poll();
                    set.remove(e);
                }
                set.add(p);
                queue.add(p);
            }
            System.out.println(p + "\t" + queue);
        }

        hits = pages.length - faults;
    }

    // Function to print results
    void printResults() {
        System.out.println("\nTotal Pages : " + pages.length);
        System.out.println("Frames      : " + frames);
        System.out.println("Page Faults : " + faults);
        System.out.println("Fault Ratio : "+(faults * 100.0 / pages.length));
        System.out.println("Hits        : " + hits);
        System.out.println("Hit Ratio   : "+ (hits * 100.0 / pages.length));
    }


    public static void main(String[] args) {
        fifo fifo = new fifo();
        fifo.readInput();
        fifo.simulate();
        fifo.printResults();
    }
}
