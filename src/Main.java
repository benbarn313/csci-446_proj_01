public class Main
{
    public static final int DEBUG_LEVEL = 1; //flag for triggering debug messages

    public static void main(String[] args)
    {
	    Graph myGraph = new Graph(100);
	    if (DEBUG_LEVEL >= 1) System.out.print(myGraph);

	    ConstraintSolver constraintSolver = new ConstraintSolver();
        int numColors = 4;

        constraintSolver.backtrackWithFwdCheck(myGraph, numColors);
        System.out.println(constraintSolver.getColoring());
        System.out.println(constraintSolver.getCost());

	    constraintSolver.simpleBacktrack(myGraph, numColors);
	    System.out.println(constraintSolver.getColoring());
	    System.out.println(constraintSolver.getCost());


    }
}
