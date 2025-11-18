package  com.example.model_layer;
import java.util.LinkedList;

abstract class Tree
{
    final private int MAX_RANCHES = 5;
    final private int MIN_BRANCHES = 5;
    private LinkedList<BranchSection> alive_roots;

    public Tree()
    {
        /* Built the first root */
    }

    public void makeBase()
    {
        
    }
}