package tr.edu.bilkent.cs.patoh;

import com.sun.jna.Structure;
import com.sun.jna.Native;

/**
 * Java equivalent of <code>PaToH_Parameters</code>, and simply extending this
 * class (probably) won't work -- try to use spare fields (<code>allargs</code>,
 * <code>inputfilename</code>, etc.) instead.
 * <p>
 * After creating a PartitioningParameters instance, before passing it to any
 * parititoning functions, you <i>must</i> invoke <code>initialize</code> method
 * of the instance.
 */
public class PartitioningParameters extends Structure
{
    static { Native.register("patoh"); }
    
    /** Miscellaneous: General Parameters */
    public int    cuttype;
    public int    k;
    public int    outputdetail;             // PATOH_OD_...
    public int    seed;
    public int    doinitperm;

    /** Miscellaneous: Net Discard Parameters */
    public int    bisec_fixednetsizetrsh;
    public float  bisec_netsizetrsh;
    public int    bisec_partmultnetsizetrsh;

    /** Miscellaneous: V-Cycle Parameter */
    public int    bigVcycle;
    public int    smallVcycle;
    public int    usesamematchinginVcycles;

    /** Miscellaneous: Heap/Bucket Parameters */
    public int    usebucket;
    public int    maxcellinheap;
    public int    heapchk_mul;
    public int    heapchk_div;

    /** Miscellaneous: Memory Allocation Parameters */
    public int    MemMul_CellNet;
    public int    MemMul_Pins;
    public int    MemMul_General;

    /** Coarsening Parameters*/
    public int    crs_VisitOrder;           // PATOH_VO_...
    public int    crs_alg;                  // PATOH_CRS_...
    public int    crs_coarsento;
    public int    crs_coarsentokmult;
    public int    crs_coarsenper;
    public float  crs_maxallowedcellwmult;
    public int    crs_idenafter;    
    public int    crs_iden_netsizetrh;
    public int    crs_useafter;
    public int    crs_useafteralg;

    /** Initial Partitioning Parameters */
    public int    nofinstances;             // Both Init. Part. & Refinement
    public int    initp_alg;                // PATOH_IPA_...
    public int    initp_runno;
    public int    initp_ghg_trybalance;
    public int    initp_refalg;             // PATOH_REFALG_...

    /** Refinement Parameters */
    public int    ref_alg;                  // PATOH_REFALG_...
    public int    ref_useafter;
    public int    ref_useafteralg;
    public int    ref_passcnt;
    public int    ref_maxnegmove;
    public float  ref_maxnegmovemult;
    public int    ref_dynamiclockcnt;

    /** Imbalance Parameters */
    public double init_imbal;
    public double final_imbal;
    public double fast_initbal_mult;
    public float  init_sol_discard_mult;
    public float  final_sol_discard_mult;

    /** Utilities (Actually, aren't used by PaToH.)  */
    public char[] allargs = new char[8192];
    public char[] inputfilename = new char[512];
    public int    noofrun;
    public int    writepartinfo;

    private static native int PaToH_Initialize_Parameters
        (PartitioningParameters params,             // PaToH_Parameters *
         int                    cuttype,
         int                    suggestByProblemType);

    /**
     * Invokes <code>Initialize_Parameters</code>.
     */
    public void initialize(int cuttype, int suggestByProblemType)
    {
	PaToH_Initialize_Parameters(this, cuttype, suggestByProblemType);
    }
}
