import java.util.ArrayList;
import java.util.Scanner;

public class NonogramSolver {
    // This method is primarily for prompting the user for information to create the nonogram board
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        System.out.print("Enter width (int x): ");
        int width = input.nextInt();
        System.out.print("Enter height (int y): ");
        int height = input.nextInt();
        ArrayList<ArrayList<Integer>> xHints = new ArrayList<ArrayList<Integer>>();
        ArrayList<ArrayList<Integer>> yHints = new ArrayList<ArrayList<Integer>>();
        input.nextLine();
        for (int i = 0; i < height; i++) {
            ArrayList<Integer> temp = new ArrayList<Integer>();
            System.out.print("Enter hints (left to right, separated by one space) for row " + (i + 1) + "/" + height + ": ");
            String tempHints = input.nextLine();
            String[] allHints = tempHints.split(" ");
            for (String h : allHints) temp.add(Integer.parseInt(h));
            xHints.add(temp);
        }
        for (int i = 0; i < width; i++) {
            ArrayList<Integer> temp = new ArrayList<Integer>();
            System.out.print("Enter hints (top to bottom, separated by one space) for column " + (i + 1) + "/" + width + ": ");
            String tempHints = input.nextLine();
            String[] allHints = tempHints.split(" ");
            for (String h : allHints) temp.add(Integer.parseInt(h));
            yHints.add(temp);
        }
        Nonogram myNonogram = new Nonogram(xHints, yHints);
        System.out.println("Solving...");
        myNonogram.solveFullBoard();
        System.out.println("Solution:\n");
        System.out.println(myNonogram.printBoard());
    }
}