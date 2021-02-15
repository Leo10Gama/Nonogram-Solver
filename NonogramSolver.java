import java.util.ArrayList;
import java.util.Scanner;

public class NonogramSolver {
    // This method is primarily for prompting the user for information to create the nonogram board
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        // Testing with "truffle" 5x5 puzzle
        ArrayList<Integer> xHint1 = new ArrayList<Integer>();
        ArrayList<Integer> xHint2 = new ArrayList<Integer>();
        ArrayList<Integer> xHint3 = new ArrayList<Integer>();
        ArrayList<Integer> xHint4 = new ArrayList<Integer>();
        ArrayList<Integer> xHint5 = new ArrayList<Integer>();
        xHint1.add(1);
        xHint2.add(1); xHint2.add(1);
        xHint3.add(1); xHint3.add(1);
        xHint4.add(1); xHint4.add(3);
        xHint5.add(3);
        ArrayList<Integer> yHint1 = new ArrayList<Integer>();
        ArrayList<Integer> yHint2 = new ArrayList<Integer>();
        ArrayList<Integer> yHint3 = new ArrayList<Integer>();
        ArrayList<Integer> yHint4 = new ArrayList<Integer>();
        ArrayList<Integer> yHint5 = new ArrayList<Integer>();
        yHint1.add(2);
        yHint2.add(1); yHint2.add(1);
        yHint3.add(1); yHint3.add(2);
        yHint4.add(1); yHint4.add(2);
        yHint5.add(2);
        ArrayList<ArrayList<Integer>> xHints = new ArrayList<ArrayList<Integer>>();
        xHints.add(xHint1);
        xHints.add(xHint2);
        xHints.add(xHint3);
        xHints.add(xHint4);
        xHints.add(xHint5);
        ArrayList<ArrayList<Integer>> yHints = new ArrayList<ArrayList<Integer>>();
        yHints.add(yHint1);
        yHints.add(yHint2);
        yHints.add(yHint3);
        yHints.add(yHint4);
        yHints.add(yHint5);
        Nonogram n = new Nonogram(xHints, yHints);
        n.printItems();
        n.printPermutations();
        n.solveFullBoard();
        System.out.println("Solved and returned!");
        for (int y = 0; y < 5; y++) {
            for (int x = 0; x < 5; x++) {
                System.out.print(n.board[x][y]);
            }
            System.out.println();
        }
    }
}