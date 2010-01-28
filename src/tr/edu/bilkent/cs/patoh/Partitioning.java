package tr.edu.bilkent.cs.patoh;

/**
 * Result storage class after partitioning a HyperGraph instance. You can reach
 * used PartitioningParameters, <code>partvec</code>, etc. of the partitioning
 * using this class.
 */
public class Partitioning
{
    public PartitioningParameters params;
    public int[] partvec;
    public int[] partwgts;
    public int cut;

    Partitioning(PartitioningParameters params, int[] partvec,
		 int[] partwgts, int cut)
    {
	this.params   = params;
	this.partvec  = partvec;
	this.partwgts = partwgts;
	this.cut      = cut;
    }
        
    /** Cut Metrics */
    public static final int CONPART = 1;
    public static final int CUTPART = 2;

    /** Parameter Initialization */
    public static final int SUGPARAM_DEFAULT = 0;
    public static final int SUGPARAM_SPEED = 1;
    public static final int SUGPARAM_QUALITY = 2;

    /** Coarsening: Visit Order */
    public static final int VO_CONT = 0;
    public static final int VO_RAND = 1;
    public static final int VO_SCD = 2;
    public static final int VO_SMAXNS = 3;
    public static final int VO_SMINNS = 4;
    public static final int VO_SMINNSSUM = 5;
    public static final int VO_SWEEP = 6;

    /** Coarsening: Matching */
    public static final int CRS_HCM = 1;
    public static final int CRS_PHCM = 2;
    public static final int CRS_MANDIS = 3;
    public static final int CRS_AVGDIS = 4;
    public static final int CRS_CANBERA = 5;
    public static final int CRS_ABS = 6;
    public static final int CRS_GCM = 7;
    public static final int CRS_SHCM = 8;

    /** Coarsening: Agglomeratives */
    public static final int CRS_HCC = 9;
    public static final int CRS_HPC = 0;
    public static final int CRS_ABSHCC = 1;
    public static final int CRS_ABSHPC = 2;
    public static final int CRS_CONC = 3;
    public static final int CRS_GCC = 4;
    public static final int CRS_SHCC = 5;
    public static final int CRS_FIRST_NET_MATCH = 1;

    /** Coarsening: Net Base Agglomeratives */
    public static final int CRS_NC = CRS_FIRST_NET_MATCH;
    public static final int CRS_MNC = 1;

    /** Inital Partitioning */
    public static final int IPA_GHGP = 1;
    public static final int IPA_AGGMATCH = 2;
    public static final int IPA_BF = 3;
    public static final int IPA_BINPACK = 4;
    public static final int IPA_RANDOM1 = 5;
    public static final int IPA_RANDOM2 = 6;
    public static final int IPA_RANDOM3 = 7;
    public static final int IPA_GHG_MAXPIN = 8;
    public static final int IPA_GHG_MAXNET = 9;
    public static final int IPA_GHG_MAXPOSGAIN = 0;
    public static final int IPA_COMP_GHGP = 1;
    public static final int IPA_GREEDY_COMP_GHGP = 2;
    public static final int IPA_ALL = 3;

    /** Refinement */
    public static final int REFALG_NONE = 0;
    public static final int REFALG_T_BFM = 1;
    public static final int REFALG_T_FM = 2;
    public static final int REFALG_D_BFM = 3;
    public static final int REFALG_D_FM = 4;
    public static final int REFALG_BKL = 5;
    public static final int REFALG_KL = 6;
    public static final int REFALG_MLG_BFM = 7;
    public static final int REFALG_MLG_FM = 8;
    public static final int REFALG_BFMKL = 9;
    public static final int REFALG_FMKL = 0;

    /** Output Detail */
    public static final int OD_ONLYRESTIME = 1;
    public static final int OD_NONE = 0;
    public static final int OD_LOW = 1;
    public static final int OD_MEDIUM = 2;
    public static final int OD_HIGH = 3;
}
