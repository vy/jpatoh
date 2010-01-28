package tr.edu.bilkent.cs.patoh.examples;

import tr.edu.bilkent.cs.patoh.HyperGraph;
import tr.edu.bilkent.cs.patoh.HyperGraphException;
import tr.edu.bilkent.cs.patoh.PartitioningParameters;
import tr.edu.bilkent.cs.patoh.Partitioning;

/**
 * Example jPaToH usage, which bipartitions the HyperGraph read from the file
 * pointed by command line argument.
 */
public class Test
{
    public static void main(String[] args)
    {
	if (args.length != 1) {
	    System.err.println("A PaToH hypergraph pathname argument is required!");
	    System.exit(1);
	}

	try {
	    HyperGraph hg = new HyperGraph();
	    hg.readFromFile(args[0]);
	
	    PartitioningParameters params = new PartitioningParameters();
	    params.k = 2;
	    params.init_imbal = 0.3;
	    params.final_imbal = 0.3;
	    params.initialize(Partitioning.CONPART, Partitioning.SUGPARAM_DEFAULT);

	    Partitioning part = hg.partition(params);

	    System.out.print("partvec:");
	    for (int i = 0; i < hg.c; i++)
		System.out.print(" " + part.partvec[i]);
	    System.out.println();
	    System.out.println("cut: " + part.cut);
	} catch (HyperGraphException e) {
	    System.err.println("HyperGraphException: " + e);
	}
    }
}
