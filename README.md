# Nonogram-Solver

Nonogram puzzles, also commonly known as Picross puzzles, are logic puzzles that require the solver to fill in a grid with black squares, often to create some sort of image. The puzzles are structured such that each row and column provides a set amount of "hint numbers", which tell how many clusters of black squares are together, in order, but does not specify how many spaces exist between each cluster. For instance, on a row of size 5, a hint of "1 2" means that there exists a single black square, followed by at least one empty space, and then followed by a cluster of exactly 2.

# Running the Algorithm

After downloading the `Nonogram.java` and `NonogramSolver.java` files, one can compile both and then run the `NonogramSolver` file. A prompt will then appear to enter the width and height of the board, followed by all corresponding hint numbers, first for all `height` rows (read from the leftmost hint number rightwards), then all `width` columns (read from the topmost hint number downwards). After this, the implemented algorithm will solve the puzzle (if there is a solution). If there is no solution, a grid of `??` characters will be printed instead. Each pass of the algorithm is also visualized to aid in convenience and potentially resolve issues that may arise.

In the event that one column or row was entered incorrectly, the proper solution may not be displayed. Once the algorithm has run, there is the option to either `set x` or `set y` which will allow the hints of one of the rows or columns (respectively) to be overwritten with a new, provided set of hints. Once the new hints are entered, the algorithm is run again and a potentially more correct solution will be displayed.

# The Algorithm Itself

When solving Nonogram/Picross puzzles normally, squares and x's are placed in the grid to mark areas where squares are and are not, respectively. While there are a [variety of techniques](https://en.wikipedia.org/wiki/Nonogram#Solution_techniques) that can be used to arrive at the solution, they all use logic to accomplish the same goal: deciding which squares of a given row/column can or cannot be filled in with the provided squares, x's, and hint numbers. This algorithm makes use of this fact, rather than hard-coding each technique, for the sake of simplicity. The entire algorithm is contained within the `Nonogram.java` file and is called as `solveFullBoard()`.

First, the `Nonogram` object generates all possible permutations for each individual row and column using the `Nonogram`'s `xHints` and `yHints` attributes. These hints are stored as `ArrayList<ArrayList<Integer>>` objects, which may seem somewhat confusing at first. However, it is important to note that a single "hint" is merely an ordered list of integers, so these attributes are ordered collections of these hints, with the first `ArrayList<Integer>` object being the first row/column (leftmost or topmost), and the last object being the last row/column (rightmost or bottommost). Each permutation is generated recursively, with the algorithm making as many calls as there are `Integer` elements in a given `ArrayList`. 

The `generatePermutations()` method will return an `ArrayList<String>` object of all possible permutations of a set of hints `hints` within a given boundary `capacity`. Each `String` is made up of 'o' or 'x', with 'o' representing a filled square, and 'x' representing an empty square. If the algorithm is called with only one hint in the passed `ArrayList<Integer>` object (which must occur, since each row and column must have at least one hint number ranging 0 to its size), it will return all permutations of the given cluster within the capacity, "moving" the group from the leftmost section of the `capacity` to the rightmost, appending each permutation to an `ArrayList<String>` object to return at the end of the method. If the method is called with more than one hint in `hints`, it calls on a recursive step. First, a boundary `sum` is created to place a rightmost limit on the first hint (i.e. the rightmost spot that the first cluster can occupy that still gives enough room for the remaining hints). The method is then called again, with `capacity` decreased by the amount of leading x's + the size of the cluster + 1 (for the ending x, since the next cluster cannot touch the first), and this first hint removed from `hints`. This is repeated for each position of the first hint in each possible spot until all permutations are collected in an `ArrayList<String>` object, which is then returned.

Once all `xPermutations` and `yPermutations` are created, the `board` attribute of the `Nonogram` object is initialized to empty spaces and the `solveBoard()` method is called. Unlike the `solveFullBoard()` method, this one takes in the parameters of the board to solve, as well as each list of permutations for both rows and columns. 

This algorithm works by passing through each row and column, determining if any changes can be made to the board. These changes are determined by going through each permutation in a given row/column. The algorithm first removes any permutations which are not possible (i.e. the square on the board has been filled with either 'o' or 'x', but that square does not exist in a given permutation). If the permutation is possible, a separate `char[] temp` is filled with squares matching each past permutation (i.e. if the square hasn't been set yet, set it to the square in the permutation, but if it contradicts the square of the permutation, leave it blank). Once all permutations have been iterated through, one last check is made to determine if if any permutations still exist for the given row/column. If not, an empty `char[][]` is returned, signifying that there is no solution. Otherwise, any characters in `temp` are copied to the board (assuming the board is empty in that location). 

In the event that a pass is made through each column and row and no changes have been made, the algorithm then checks whether or not this means the board has been solved. If it has, then the board is returned and the algorithm halts. Otherwise, all rows and columns are iterated through to determine if there exists any row or column with multiple possible permutations remaining. If so, then the first permutation is filled into a `tempBoard` and the algorithm calls itself with this new `tempBoard` and the current permutations. This takes the place of the "contradiction" solution technique. If the board returned is empty, then we know that permutation is not a possible solution, and thus the next permutation is tried. Eventually, the method will either return an empty board (if the original board has no solution) or the solved board. In all cases, the final board will be returned.

# Other Methods

The `Nonogram.java` class contains other methods, such as `getWidth(), getHeight(), setXHint(int, ArrayList<Integer>), setYHint(int, ArrayList<Integer>), printBoard()`, but these are mostly standard practice algorithms not worth detailing here. Please read through their Javadoc for more information if you plan on using them.

# Closing Remarks

Does this algorithm work? Yes. Does it implement a similar line of logic and reasoning than an average person would employ solving nonogram puzzles? Yes. Does it have excellent run time? Not exactly. From my own experience, puzzles of sizes larger than 50 seem to overflow the program stack, and the time spent entering numbers definitely feels more tedious than solving a puzzle by hand. However, this algorithm serves me mostly as a "proof-of-concept" model. It can definitely be optimized, and I do hope to come back and try optimizing some aspects of it (maybe even rewrite it in another language like C?) to allow for even larger puzzles to be solved efficiently. In the meantime, dear reader, I hope you enjoy messing around with this algorithm and all it has to offer!
