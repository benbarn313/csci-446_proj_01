public class Main
{
    public static final int DEBUG_LEVEL = 0; //flag for triggering debug messages

    public static void main(String[] args)
    {
	    Graph myGraph = new Graph(90);
	    if (DEBUG_LEVEL >= 1) System.out.print(myGraph);

	    ConstraintSolver constraintSolver = new ConstraintSolver();
        int numColors = 4;

        constraintSolver.backtrackWithFwdCheck(myGraph, numColors);
        System.out.println(constraintSolver.getColoring());
        System.out.println(constraintSolver.getCost());

        constraintSolver.backtrackWithAC3(myGraph, numColors);
        System.out.println(constraintSolver.getColoring());
        System.out.println(constraintSolver.getCost());

	    constraintSolver.simpleBacktrack(myGraph, numColors);
	    System.out.println(constraintSolver.getColoring());
	    System.out.println(constraintSolver.getCost());

        constraintSolver.localSearchGeneticAlgorithm(myGraph, numColors);
        System.out.println(constraintSolver.getColoring());
        System.out.println(constraintSolver.getCost());
    }

    private static void test()
    {
        int stuff[][] = new int[4][3];
        int stuff2[] = {1, 2, 3};
        int stuff3[] = {4, 5, 6};
        stuff[1] = stuff2;
        stuff[2] = stuff3;

        for (int i = 0; i <= stuff[1].length - 1; i++) System.out.print(stuff[1][i]);
        System.out.println();
        for (int i = 0; i <= stuff[2].length - 1; i++) System.out.print(stuff[2][i]);
        System.out.println();
        System.out.println(stuff.length + " | " + stuff[1].length);
    }
}
