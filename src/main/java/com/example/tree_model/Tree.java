package  com.example.tree_model;

public class Tree
{
    final private int MAX_RANCHES = 5;
    final private int MIN_BRANCHES = 5;
    //private LinkedList<BranchSection> alive_roots;

    private final Trunk trunk;
    private final BranchFactory branch_factory;

    public Tree(Trunk trunk, BranchFactory branch_factory)
    {
        this.trunk = trunk;
        this.branch_factory = branch_factory;
    }
    

}