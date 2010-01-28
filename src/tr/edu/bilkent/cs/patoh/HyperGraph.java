package tr.edu.bilkent.cs.patoh;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.PointerByReference;

/**
 * Base class to issue HyperGraph operations: partitioning, reading from,
 * writing to file, etc. In case of a shared library error (where return status
 * is not equal to 0) methods throw {@link HyperGraphException}.
 * <p>
 * User isn't supposed to deal with JNA and binding internals. One can safely
 * use exported functions. All exported partitioning functions are implemented
 * in a thread-safe style using <code>synchronized static</code> methods. Memory
 * allocations/releases are handled by the methods.
 */
public class HyperGraph
{
    static { Native.register("patoh"); }

    public int c;
    public int n;
    public int nnz;
    public int nconst;
    public int[] cwgts;
    public int[] nwgts;
    public int[] xpins;
    public int[] pins;

    /**
     * Sole constructor. (HyperGraph fields are not initialized.)
     */
    public HyperGraph() {}

    /**
     * Construct a HyperGraph instance using supplied parameters.
     */
    public HyperGraph(int c, int n, int nnz, int nconst, int[] cwgts, int[] nwgts,
		      int[] xpins, int[] pins)
    {
        this.c = c;
        this.n = n;
        this.nnz = nnz;
        this.nconst = nconst;
        this.cwgts = cwgts;
        this.nwgts = nwgts;
        this.xpins = xpins;
        this.pins = pins;
    }

    private static int FFI(int ret, int success, String fn)
	throws HyperGraphException
    {
	if (ret != success)
	    throw new HyperGraphException(fn + "() failure: " + ret);

	return ret;
    }

    private static native int PaToH_Read_Hypergraph
        (String pathname,
         IntByReference c,
         IntByReference n,
         IntByReference nconst,
         PointerByReference cwgts,              // int **
         PointerByReference nwgts,              // int **
         PointerByReference xpins,              // int **
         PointerByReference pins);              // int **

    /**
     * Invokes <code>PaToH_Read_Hypergraph</code>.
     */
    public void readFromFile(String pathname) throws HyperGraphException
    {
        /* Read hypergraph. */
        IntByReference cref = new IntByReference();
        IntByReference nref = new IntByReference();
        IntByReference nconstref = new IntByReference();
        PointerByReference cwgtspref = new PointerByReference();
        PointerByReference nwgtspref = new PointerByReference();
        PointerByReference xpinspref = new PointerByReference();
        PointerByReference pinspref = new PointerByReference();
	FFI(PaToH_Read_Hypergraph(pathname, cref, nref, nconstref,
				  cwgtspref, nwgtspref, xpinspref,
				  pinspref),
	    0, "PaToH_Read_Hypergraph");

        /* Pack result into a fresh "HyperGraph". */
        this.c      = cref.getValue();
        this.n      = nref.getValue();
        this.nconst = nconstref.getValue();
        this.cwgts  = cwgtspref.getValue().getIntArray(0, this.c);
        this.nwgts  = nwgtspref.getValue().getIntArray(0, this.n);
        this.xpins  = xpinspref.getValue().getIntArray(0, (this.n + 1));
        this.nnz    = xpins[this.n];
        this.pins   = pinspref.getValue().getIntArray(0, this.nnz);
    }

    private static native int PaToH_Write_Hypergraph
	(String pathname,
	 int numbering,
	 int c,
	 int n,
	 int nconst,
	 int[] cwgts,
	 int[] nwgts,
	 int[] xpins,
	 int[] pins);

    /**
     * Invokes <code>PaToH_Write_Hypergraph</code>.
     */
    public void writeToFile(String pathname, int numbering)
	throws HyperGraphException
    {
	FFI(PaToH_Write_Hypergraph(pathname, numbering, this.c, this.n,
				   this.nconst, this.cwgts, this.nwgts,
				   this.xpins, this.pins),
	    0, "PaToH_Write_Hypergraph");
    }

    private static native int PaToH_Alloc
        (PartitioningParameters params,
	 int c,
	 int n,
	 int nconst,
	 int[] cwgts,
	 int[] nwgts,
	 int[] xpins,
	 int[] pins);

    private static native int PaToH_Free
        ();

    private static native int PaToH_Partition
        (PartitioningParameters params,
	 int c,
	 int n,
	 int[] cwgts,
	 int[] nwgts,
	 int[] xpins,
	 int[] pins,
	 int[] partvec,
	 int[] partweights,
	 IntByReference cut);

    private synchronized static Partitioning partition
	(HyperGraph hg, PartitioningParameters params)
	throws HyperGraphException
    {
	/* Allocate temporary (internal) resources. */
	FFI(PaToH_Alloc(params, hg.c, hg.n, hg.nconst, hg.cwgts,
			hg.nwgts, hg.xpins, hg.pins),
	    0, "PaToH_Alloc");

	/* Issue partitioning. */
	IntByReference cut = new IntByReference();
	int[] partvec = new int[hg.c];
	int[] partwgts = new int[params.k];
	FFI(PaToH_Partition(params, hg.c, hg.n, hg.cwgts, hg.nwgts,
			    hg.xpins, hg.pins, partvec, partwgts, cut),
	    0, "PaToH_Partition");

	/* Release temporary resources. */
	PaToH_Free();

	/* Pack results into a fresh "Partitioning". */
	return new Partitioning(params, partvec, partwgts, cut.getValue());
    }

    /**
     * Invokes <code>PaToH_Partition</code>.
     */
    public Partitioning partition(PartitioningParameters params)
	throws HyperGraphException
    {
	return partition(this, params);
    }

    private static native int PaToH_Partition_with_FixCells
	(PartitioningParameters params,
	 int c,
	 int n,
	 int[] cwgts,
	 int[] nwgts,
	 int[] xpins,
	 int[] pins,
	 int[] partvec,
	 int[] partwgts,
	 IntByReference cut);

    private synchronized static Partitioning partitionWithFixCells
	(HyperGraph hg, PartitioningParameters params)
	throws HyperGraphException
    {
	/* Allocate temporary (internal) resources. */
	FFI(PaToH_Alloc(params, hg.c, hg.n, hg.nconst, hg.cwgts,
			hg.nwgts, hg.xpins, hg.pins),
	    0, "PaToH_Alloc");

	/* Issue partitioning. */
	IntByReference cut = new IntByReference();
	int[] partvec = new int[hg.c];
	int[] partwgts = new int[params.k];
	FFI(PaToH_Partition_with_FixCells(params, hg.c, hg.n, hg.cwgts,
					  hg.nwgts, hg.xpins, hg.pins,
					  partvec, partwgts, cut),
	    0, "PaToH_Partition_with_FixCells");

	/* Release temporary resources. */
	PaToH_Free();

	/* Pack results into a fresh "Partitioning". */
	return new Partitioning(params, partvec, partwgts, cut.getValue());
    }

    /**
     * Invokes <code>PaToH_Partition_with_FixCells</code>.
     */
    public Partitioning partitionWithFixCells(PartitioningParameters params)
	throws HyperGraphException
    {
	return partitionWithFixCells(this, params);
    }

    private static native int PaToH_MultiConst_Partition
	(PartitioningParameters params,
	 int c,
	 int n,
	 int nconst,
	 int[] cwgts,
	 int[] nwgts,
	 int[] xpins,
	 int[] pins,
	 int[] partvec,
	 int[] partwgts,
	 IntByReference cut);

    private synchronized static Partitioning partitionMultiConst
	(HyperGraph hg, PartitioningParameters params)
	throws HyperGraphException
    {
	/* Allocate temporary (internal) resources. */
	FFI(PaToH_Alloc(params, hg.c, hg.n, hg.nconst, hg.cwgts,
			hg.nwgts, hg.xpins, hg.pins),
	    0, "PaToH_Alloc");

	/* Issue partitioning. */
	IntByReference cut = new IntByReference();
	int[] partvec = new int[hg.c];
	int[] partwgts = new int[params.k];
	FFI(PaToH_MultiConst_Partition(params, hg.c, hg.n, hg.nconst, hg.cwgts,
				       hg.nwgts, hg.xpins, hg.pins, partvec,
				       partwgts, cut),
	    0, "PaToH_MultiConst_Partition");

	/* Release temporary resources. */
	PaToH_Free();

	/* Pack results into a fresh "Partitioning". */
	return new Partitioning(params, partvec, partwgts, cut.getValue());
    }

    /**
     * Invokes <code>PaToH_MultiConst_Partition</code>.
     */
    public Partitioning partitionMultiConst(PartitioningParameters params)
	throws HyperGraphException
    {
	return partitionMultiConst(this, params);
    }

    private static native int PaToH_Check_Hypergraph
	(int c,
	 int n,
	 int nconst,
	 int[] cwgts,
	 int[] nwgts,
	 int[] xpins,
	 int[] pins);

    /**
     * Invokes <code>PaToH_Check_Hypergraph</code>.
     */
    public boolean check()
    {
	if (PaToH_Check_Hypergraph(this.c, this.n, this.nconst, this.cwgts,
				   this.nwgts, this.xpins, this.pins)
	    == 0)
	    return true;

	return false;
    }
}
