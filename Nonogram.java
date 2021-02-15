import java.util.ArrayList;

public class Nonogram {
    private int xSize;
    private int ySize;
    private ArrayList<ArrayList<Integer>> xHints;   // an array of ArrayLists
    private ArrayList<ArrayList<Integer>> yHints;   // also an array of ArrayLists
    // For the board, '' represents empty, 'x' represents no fill, 'o' represents a filled square
    public char[][] board;                          // the actual image itself

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
            this.board = new char[xSize][ySize];
            for (int x = 0; x < xSize; x++) {
                for (int y = 0; y < ySize; y++) {
                    this.board[x][y] = ' ';
                }
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
                for (int j = 0; j < i; j++) currString += 'x';
                // Append cluster of o's
                for (int j = 0; j < singleHint; j++) currString += 'o';
                // Append trailing x's
                for (int j = 0; j < (capacity - i - singleHint); j++) currString += 'x';
                // Add to list
                result.add(currString);
            }
            return result;
        }
        // Recursive step
        int sum = 0;
        for (int hint : hints) sum += (hint + 1);
        sum--;  // correction for last space
        for (int i = 0; i <= (capacity - sum); i++) {
            // Generate all possible permutations for all clusters EXCEPT the first one
            ArrayList<Integer> newHints = new ArrayList<Integer>(hints);
            newHints.remove(0);
            ArrayList<String> newPerms = generatePermutations(capacity - i - hints.get(0) - 1, newHints);
            // Use new permutations to form more permutations
            for (String perm : newPerms) {
                String currString = "";
                // Append leading x's
                for (int j = 0; j < i; j++) currString += 'x';
                // Append cluster of o's
                for (int j = 0; j < hints.get(0); j++) currString += 'o';
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

    /** The more interesting method, it returns a character array of dimensions
     * `xSize` x `ySize` made to represent the completed nonogram object, filled
     * in with either 'o' to represent a filled square, or 'x' to represent an
     * empty square.
     */
    public char[][] solveFullBoard() {
        ArrayList<ArrayList<String>> xPermutations = new ArrayList<ArrayList<String>>();
        for (ArrayList<Integer> x : this.xHints) xPermutations.add(this.generatePermutations(this.xSize, x));
        ArrayList<ArrayList<String>> yPermutations = new ArrayList<ArrayList<String>>();
        for (ArrayList<Integer> y : this.yHints) yPermutations.add(this.generatePermutations(this.ySize, y));
        return this.solveBoard(this.board, xPermutations, yPermutations);
    }

    /** The helper method for `solveFullBoard()`, it analyzes all possible
     * given permutations of the board and either solves the puzzle
     * algorithmically, or returns an empty array if the puzzle cannot be
     * solved with the given permutations.
     * @param board The current board
     * @param xPermutations An ArrayList of Strings, likely provided by the
     *      `generatePermutations()` method, representing all potential
     *      row permutations
     * @param yPermutations An ArrayList of Strings, likely provided by the
     *      `generatePermutations()` method, representing all potential
     *      column permutations
     * @return Either a solved board (represented as a 2D char array) or an
     *      empty board if the solution to the nonogram does not exist
     */
    private char[][] solveBoard(char[][] board, ArrayList<ArrayList<String>> xPermutations, ArrayList<ArrayList<String>> yPermutations) {
        while (true) {     // bad practice, i know, but it works so sue me :\
            boolean madeChanges = false;
            // Loop through each row
            for (int i = 0; i < this.xSize; i++) {
                char[] temp = new char[this.xSize];
                // Go through each permutation
                for (String permutation : xPermutations.get(i)) {
                    // Verify if the permutation is still possible on the board
                    boolean validPerm = true;
                    for (int j = 0; j < permutation.length(); j++) {
                        // If a square on the board doesn't line up w the permutation, remove it
                        if ((board[j][i] != permutation.charAt(j)) && (board[j][i] != ' ')) {
                            validPerm = false;
                            break;
                        }
                    }
                    if (!validPerm) {
                        xPermutations.remove(permutation);
                        continue;
                    }
                    // Figure out how many squares to add to the board
                    for (int j = 0; j < this.xSize; j++) {
                        if (temp[j] == '\0') temp[j] = permutation.charAt(j);
                        else if (temp[j] != permutation.charAt(j)) temp[j] = ' ';  // Don't put anything
                    }
                }
                // If all the permutations have been removed, the board is impossible; return empty
                if (xPermutations.get(i).size() == 0) return new char[0][0];
                // Make actual changes to the board
                for (int j = 0; j < temp.length; j++) {
                    if ((temp[j] != ' ') && (board[j][i] != temp[j])) {
                        board[j][i] = temp[j];
                        madeChanges = true;
                    }
                }
            }
            // Loop through each column
            for (int i = 0; i < this.ySize; i++) {
                char[] temp = new char[this.ySize];
                // Go through each permutation
                for (String permutation : yPermutations.get(i)) {
                    // Verify if the permutation is still possible on the board
                    boolean validPerm = true;
                    for (int j = 0; j < permutation.length(); j++) {
                        // If a square on the board doesn't line up w the permutation, remove it
                        if ((board[i][j] != permutation.charAt(j)) && (board[i][j] != ' ')) {
                            validPerm = false;
                            break;
                        }
                    }
                    if (!validPerm) {
                        yPermutations.remove(permutation);
                        continue;
                    }
                    // Figure out how many squares to add to the board
                    for (int j = 0; j < this.ySize; j++) {
                        if (temp[j] == '\0') temp[j] = permutation.charAt(j);
                        else if (temp[j] != permutation.charAt(j)) temp[j] = ' ';  // Don't put anything
                    }
                }
                // If all the permutations have been removed, the board is impossible; return empty
                if (yPermutations.get(i).size() == 0) return new char[0][0];
                // Make actual changes to the board
                for (int j = 0; j < temp.length; j++) {
                    if ((temp[j] != ' ') && (temp[j] != ' ') && (board[i][j] != temp[j])) {
                        board[i][j] = temp[j];
                        madeChanges = true;
                    }
                }
            }
            // If one iteration completed without any changes being made, make an assumption
            if (!madeChanges) {
                // No changes made because board is solved
                boolean solved = true;
                for (int i = 0; i < this.xSize; i++) {
                    for (int j = 0; j < this.ySize; j++) {
                        if (board[i][j] == ' ') {
                            solved = false;
                            break;
                        }
                    }
                }
                if (solved) return board;
                // No changes made because we must make a contradiction (pick random permutation to be true)
                boolean madeNewChange = false;
                // Loop through rows
                for (int i = 0; (i < this.xSize) && !madeNewChange; i++) {
                    while (xPermutations.get(i).size() > 1) {
                        // If we're in this loop, at least one new change will be made
                        madeNewChange = true;
                        // Fill in assumed square on the board
                        String permutation = xPermutations.get(i).get(0);
                        for (int j = 0; j < permutation.length(); j++) {
                            board[j][i] = permutation.charAt(j);
                        }
                        // Create temporary parameters
                        char[][] tempBoard = new char[this.xSize][this.ySize];
                        for (int x = 0; x < this.xSize; x++) {
                            for (int y = 0; y < this.ySize; y++) {
                                tempBoard[x][y] = board[x][y];
                            }
                        }
                        ArrayList<ArrayList<String>> tempXPerms = new ArrayList<ArrayList<String>>();
                        for (ArrayList<String> a : xPermutations) {
                            ArrayList<String> tempA = new ArrayList<String>();
                            for (String s : a) {
                                tempA.add(new String(s));
                            }
                            tempXPerms.add(tempA);
                        }
                        ArrayList<ArrayList<String>> tempYPerms = new ArrayList<ArrayList<String>>();
                        for (ArrayList<String> a : xPermutations) {
                            ArrayList<String> tempA = new ArrayList<String>();
                            for (String s : a) {
                                tempA.add(new String(s));
                            }
                            tempYPerms.add(tempA);
                        }
                        char[][] unsolvable = new char[this.xSize][this.ySize];
                        // If board is solved from this, return it
                        if (this.solveBoard(tempBoard, tempXPerms, tempYPerms).length != 0) return tempBoard;
                        // Otherwise remove it from the collection and try with a new permutation
                        xPermutations.get(i).remove(permutation);
                    }
                }
                // Loop through columns
                for (int i = 0; (i < this.ySize) && !madeNewChange; i++) {
                    while (yPermutations.get(i).size() > 1) {
                        // If we're in this loop, at least one new change will be made
                        madeNewChange = true;
                        // Fill in assumed square on the board
                        String permutation = yPermutations.get(i).get(0);
                        for (int j = 0; j < permutation.length(); j++) {
                            board[j][i] = permutation.charAt(j);
                        }
                        // Create temporary parameters
                        char[][] tempBoard = new char[this.xSize][this.ySize];
                        for (int x = 0; x < this.xSize; x++) {
                            for (int y = 0; y < this.ySize; y++) {
                                tempBoard[x][y] = board[x][y];
                            }
                        }
                        ArrayList<ArrayList<String>> tempXPerms = new ArrayList<ArrayList<String>>();
                        for (ArrayList<String> a : xPermutations) {
                            ArrayList<String> tempA = new ArrayList<String>();
                            for (String s : a) {
                                tempA.add(new String(s));
                            }
                            tempXPerms.add(tempA);
                        }
                        ArrayList<ArrayList<String>> tempYPerms = new ArrayList<ArrayList<String>>();
                        for (ArrayList<String> a : xPermutations) {
                            ArrayList<String> tempA = new ArrayList<String>();
                            for (String s : a) {
                                tempA.add(new String(s));
                            }
                            tempYPerms.add(tempA);
                        }
                        char[][] unsolvable = new char[this.xSize][this.ySize];
                        // If board is solved from this, return it
                        if (this.solveBoard(tempBoard, tempXPerms, tempYPerms).length != 0) return tempBoard;
                        // Otherwise remove it from the collection and try with a new permutation
                        yPermutations.get(i).remove(permutation);
                    }
                }
            }
        }
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