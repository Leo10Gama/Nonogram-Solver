import java.util.ArrayList;

public class Nonogram {
    private int xSize;
    private int ySize;
    private ArrayList<ArrayList<Integer>> xHints;   // an array of ArrayLists
    private ArrayList<ArrayList<Integer>> yHints;   // also an array of ArrayLists
    // For the board, '' represents empty, 'x' represents no fill, 'o' represents a filled square
    public char[][] board;                  // the actual image itself

    public Nonogram() {
        this(0, 0, null, null);
    }

    public Nonogram(ArrayList<ArrayList<Integer>> xHints, ArrayList<ArrayList<Integer>> yHints) {
        this(xHints.size(), yHints.size(), xHints, yHints);
    }

    private Nonogram(int xSize, int ySize, ArrayList<ArrayList<Integer>> xHints, ArrayList<ArrayList<Integer>> yHints) {
        if (xSize != xHints.size() || ySize != yHints.size()) {   // Sizes of hints do not match with given parameters
            this.xSize = 0;
            this.ySize = 0;
            this.xHints = null;
            this.yHints = null;
        } else {
            this.xSize = xSize;
            this.ySize = ySize;
            this.xHints = new ArrayList<ArrayList<Integer>>();
            for (ArrayList<Integer> item : xHints) {
                this.xHints.add(new ArrayList<Integer>(item));
            }
            this.yHints = new ArrayList<ArrayList<Integer>>();
            for (ArrayList<Integer> item : yHints) {
                this.yHints.add(new ArrayList<Integer>(item));
            }
        }
    }

    /** Create an array of strings made to represent each possible permutation
     * of the given hints in a row/column of a given size using recursion.
     * @param capacity The size of the row/column
     * @param hints An ArrayList of integers, with each integer representing
     *      how many consecutive squares are filled
     * @return An ArrayList of strings, where each string is `size` characters
     *      long and consists of only 'o' or 'x', representing a filled or empty
     *      square respectively
     */
    private ArrayList<String> generatePermutations(int capacity, ArrayList<Integer> hints) {
        ArrayList<String> result = new ArrayList<String>();
        // Base case
        if (hints.size() == 1) {
            int singleHint = hints.get(0);
            for (int i = 0; i <= (capacity - singleHint); i++) {  // # of ways to arrange one group in `capacity` spaces
                String currString = "";
                // Append leading x's
                for (int j = 0; j < i; j++) {
                    currString += 'x';
                }
                // Append cluster of o's
                for (int j = 0; j < singleHint; j++) {
                    currString += 'o';
                }
                // Append trailing x's
                for (int j = 0; j < (capacity - i - singleHint); j++) {
                    currString += 'x';
                }
                // Add to list
                result.add(currString);
            }
            return result;
        }
        // Recursive step
        int sum = 0;
        for (int hint : hints) sum += (hint + 1);
        sum--;
        for (int i = 0; i <= (capacity - sum); i++) {
            ArrayList<Integer> newHints = new ArrayList<Integer>(hints);
            newHints.remove(0);
            ArrayList<String> newPerms = generatePermutations(capacity - i - hints.get(0) - 1, newHints);
            for (String perm : newPerms) {
                String currString = "";
                // Append leading x's
                for (int j = 0; j < i; j++) {
                    currString += 'x';
                }
                // Append cluster of o's
                for (int j = 0; j < hints.get(0); j++) {
                    currString += 'o';
                }
                // Append at least one space
                currString += 'x';
                // Append permutation
                currString += perm;
                // Append this string to list
                result.add(currString);
            }
        }
        return result;
    }

    /** Getter method for the xSize property, AKA width of the nonogram board
     * @return The integer value of the nonogram board's width
     */
    public int getXSize() {
        return this.xSize;
    }

    /** Getter method for the ySize property, AKA height of the nonogram board
     * @return The integer value of the nonogram board's height
     */
    public int getYSize() {
        return this.ySize;
    }

    // Debug method
    public void printItems() {
        System.out.println("xSize:\t" + this.xSize + "\nySize:\t" + this.ySize + "\nxHints: ");
        for (ArrayList<Integer> hintList : this.xHints) {
            System.out.print("\t");
            for (Integer hintNum : hintList) {
                System.out.print(hintNum + " ");
            }
            System.out.println("");
        }
        System.out.println("yHints:");
        for (ArrayList<Integer> hintList : this.yHints) {
            System.out.print("\t");
            for (Integer hintNum : hintList) {
                System.out.print(hintNum + " ");
            }
            System.out.println("");
        }
    }

    // Another debug method
    public void printPermutations() {
        System.out.println("Permutations:");
        for (ArrayList<Integer> row : this.xHints) {
            System.out.print("For hint ");
            for (int i : row) System.out.print(i + " ");
            System.out.println(",");
            ArrayList<String> myPerms = this.generatePermutations(this.getXSize(), row);
            for (String perm : myPerms) {
                System.out.println("\t" + perm);
            }
        }
        for (ArrayList<Integer> col : this.yHints) {
            System.out.print("For hint ");
            for (int i : col) System.out.print(i + " ");
            System.out.println(",");
            ArrayList<String> myPerms = this.generatePermutations(this.getYSize(), col);
            for (String perm : myPerms) {
                System.out.println("\t" + perm);
            }
        }
    }
}