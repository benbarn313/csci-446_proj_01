import java.time.LocalDateTime;
import java.util.Dictionary;
import java.util.Hashtable;

//Main class used to test the operation of the algorithms
//Generates a set of graphs and attempts to color each one with each algorithm using 3 and 4 colors
//Then stores the results of the attempted coloring using Dictionary data structures and also outputs the results

public class Main
{
    public static final int DEBUG_LEVEL = 0; //flag for triggering debug messages

    public static void main(String[] args)
    {
	    ConstraintSolver constraintSolver = new ConstraintSolver();
	    Dictionary<String, Dictionary<Integer, Result[]>> allResults = new Hashtable<>();
        LocalDateTime start, stop;
        String algName;
        Graph allGraphs[] = new Graph[10];

        //populate results dictionaries
        allResults.put("simpleBacktrack", new Hashtable<>());
        allResults.put("backtrackWithFwdCheck", new Hashtable<>());
        allResults.put("backtrackWithAC3", new Hashtable<>());
        allResults.put("geneticAlgorithm", new Hashtable<>());

        for (int i = 1; i <= allGraphs.length; i++)
        {
            allGraphs[i - 1] = new Graph(i * 10);
            if (DEBUG_LEVEL >= 1)
            {
                System.out.println("===== GRAPH " + (i - 1) + " =====");
                System.out.print(allGraphs[i - 1]);
            }
        }

        for (int numColors = 3; numColors <= 4; numColors++)
        {
            allResults.get("simpleBacktrack").put(numColors, new Result[allGraphs.length]);
            allResults.get("backtrackWithFwdCheck").put(numColors, new Result[allGraphs.length]);
            allResults.get("backtrackWithAC3").put(numColors, new Result[allGraphs.length]);
            allResults.get("geneticAlgorithm").put(numColors, new Result[allGraphs.length]);

            for (int i = 0; i <= allGraphs.length - 1; i++)
            {
                Graph myGraph = allGraphs[i];

                System.out.println(System.lineSeparator() + "===== GRAPH " + i + " =====");
                myGraph.printStats();
                System.out.println();

                algName = "simpleBacktrack";
                start = java.time.LocalDateTime.now();

                System.out.println("Starting " + algName + " with " + numColors + " colors at " + start);
                System.out.println("...");

                boolean coloringHasSolution = false;
                constraintSolver.simpleBacktrack(myGraph, numColors);
                if (constraintSolver.getCost() > 0) coloringHasSolution = true;

                stop = java.time.LocalDateTime.now();
                System.out.println("Stopped " + algName + " at " + stop);

                allResults.get(algName).get(numColors)[i] = new Result(constraintSolver.getCost(), constraintSolver.getStatesExamined(),
                        constraintSolver.getColoring(), start, stop);
                if (DEBUG_LEVEL >= 1) System.out.println(allResults.get(algName).get(numColors)[i].getColoring());
                System.out.println(allResults.get(algName).get(numColors)[i]);
                System.out.println("==========================");

                algName = "backtrackWithFwdCheck";
                start = java.time.LocalDateTime.now();
                System.out.println("Starting " + algName + " with " + numColors + " colors at " + start);
                System.out.println("...");

                constraintSolver.backtrackWithFwdCheck(myGraph, numColors);

                stop = java.time.LocalDateTime.now();
                System.out.println("Stopped " + algName + " at " + stop);

                allResults.get(algName).get(numColors)[i] = new Result(constraintSolver.getCost(), constraintSolver.getStatesExamined(),
                        constraintSolver.getColoring(), start, stop);
                if (DEBUG_LEVEL >= 1) System.out.println(allResults.get(algName).get(numColors)[i].getColoring());
                System.out.println(allResults.get(algName).get(numColors)[i]);
                System.out.println("==========================");

                algName = "backtrackWithAC3";
                start = java.time.LocalDateTime.now();
                System.out.println("Starting " + algName + " with " + numColors + " colors at " + start);
                System.out.println("...");

                constraintSolver.backtrackWithAC3(myGraph, numColors);

                stop = java.time.LocalDateTime.now();
                System.out.println("Stopped " + algName + " at " + stop);

                allResults.get(algName).get(numColors)[i] = new Result(constraintSolver.getCost(), constraintSolver.getStatesExamined(),
                        constraintSolver.getColoring(), start, stop);
                if (DEBUG_LEVEL >= 1) System.out.println(allResults.get(algName).get(numColors)[i].getColoring());
                System.out.println(allResults.get(algName).get(numColors)[i]);
                System.out.println("==========================");

                algName = "geneticAlgorithm";
                start = java.time.LocalDateTime.now();
                System.out.println("Starting " + algName + " with " + numColors + " colors at " + start);
                System.out.println("...");

                constraintSolver.localSearchGeneticAlgorithm(myGraph, numColors, coloringHasSolution);

                stop = java.time.LocalDateTime.now();
                System.out.println("Stopped " + algName + " at " + stop);

                allResults.get(algName).get(numColors)[i] = new Result(constraintSolver.getCost(), constraintSolver.getStatesExamined(),
                        constraintSolver.getColoring(), start, stop);
                if (DEBUG_LEVEL >= 1) System.out.println(allResults.get(algName).get(numColors)[i].getColoring());
                System.out.println(allResults.get(algName).get(numColors)[i]);
                System.out.println("==========================");
            }
        }
    }
}
