import java.util.*;

public class MemoryAllocation {
    static void input() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter number of memory blocks: ");
        int m = sc.nextInt();
        int[] blocks = new int[m];
        System.out.println("Enter sizes of blocks:");
        for (int i = 0; i < m; i++)
            blocks[i] = sc.nextInt();
        System.out.print("Enter number of processes: ");
        int n = sc.nextInt();
        int[] processes = new int[n];
        System.out.println("Enter sizes of processes:");
        for (int i = 0; i < n; i++)
            processes[i] = sc.nextInt();
        
            
        // .clone() creates a copy of the array.
        // Why we clone it:
        // Each allocation algorithm (like First Fit or Best Fit) modifies the block
        // sizes (reduces them when memory is allocated).
        // If we didn’t use clone(), the next algorithm would run on already changed
        // memory blocks—giving wrong results.
        firstFit(blocks.clone(), processes);
        bestFit(blocks.clone(), processes);
        nextFit(blocks.clone(), processes);
        worstFit(blocks.clone(), processes);
        sc.close();
    }
    // === FIRST FIT ===
    static void firstFit(int[] blocks, int[] processes) {
        int[] allocation = new int[processes.length]; // allocation[i] will store which process got which block
        Arrays.fill(allocation, -1);

        for (int i = 0; i < processes.length; i++) {
            for (int j = 0; j < blocks.length; j++) {
                if (blocks[j] >= processes[i]) {
                    allocation[i] = j;
                    blocks[j] -= processes[i];
                    break;
                }
            }
        }
        System.out.println("First-Fit Allocation");
        print(processes, allocation);
    }
    // === BEST FIT ===
    static void bestFit(int[] blocks, int[] processes) {
        int[] allocation = new int[processes.length];
        Arrays.fill(allocation, -1);

        for (int i = 0; i < processes.length; i++) {
            int bestIdx = -1;
            int minRem = Integer.MAX_VALUE;

            for (int j = 0; j < blocks.length; j++) {
                int rem = blocks[j] - processes[i];
                if (rem >= 0 && rem < minRem) {
                    minRem = rem;
                    bestIdx = j;
                }
            }
            if (bestIdx != -1) {
                allocation[i] = bestIdx;
                blocks[bestIdx] -= processes[i];
            }
        }
        System.out.println("Best-Fit Allocation");
        print(processes, allocation);
    }

    // === NEXT FIT ===
       // Because in Next Fit, searching is circular —
                // once we reach the end of the memory blocks, we should start again from the
                // first block.

                // Without the modulus, j would go out of array range and cause an error.
                //Go to the next block, and if you reach the end, start again from the first block.

    static void nextFit(int[] blocks, int[] processes) {
        int[] allocation = new int[processes.length];
        Arrays.fill(allocation, -1);

        int last = 0;
        for (int i = 0; i < processes.length; i++) {
            boolean found = false;
            int cnt = 0; // cnt → ensures we don’t loop infinitely
            int j = last;
            while (cnt < blocks.length) {
                if (blocks[j] >= processes[i]) {
                    allocation[i] = j;
                    blocks[j] -= processes[i];
                    last = j;
                    found = true;
                    break;
                }
                j = (j + 1) % blocks.length;
                cnt++;
            }
            if (!found)
                allocation[i] = -1;
        }
        System.out.println("Next-Fit Allocation");
        print(processes, allocation);
    }

    // === WORST FIT ===
    static void worstFit(int[] blocks, int[] processes) {
        int[] allocation = new int[processes.length];
        Arrays.fill(allocation, -1);

        for (int i = 0; i < processes.length; i++) {
            int worstIdx = -1;
            int maxRem = Integer.MIN_VALUE;

            for (int j = 0; j < blocks.length; j++) {
                int rem = blocks[j] - processes[i];
                if (rem >= 0 && rem > maxRem) {
                    maxRem = rem;
                    worstIdx = j;
                }
            }
            if (worstIdx != -1) {
                allocation[i] = worstIdx;
                blocks[worstIdx] -= processes[i];
            }
        }
        System.out.println("Worst-Fit Allocation");
        print(processes, allocation);
    }

    static void print(int[] process, int[] block) {
        System.out.println("ProcessNo\tProcessSize\tBlockNo");

        for (int i = 0; i < process.length; i++) {
            System.out.print((i + 1) + "\t\t" + process[i] + "\t\t");

            if (block[i] != -1) //block[i] stores the index number (position) of the block that was given to process i.
            //it is 0 - based indexing
                System.out.println(block[i] + 1);
            else
                System.out.println("Not Allocated");
        }
    }

    public static void main(String[] args) {
        input();
    }
}
