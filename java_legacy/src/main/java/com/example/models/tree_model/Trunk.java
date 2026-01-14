package com.example.models.tree_model;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

import com.example.config.TrunkParams;
import com.example.rendering.RenderableShape;
import com.example.rendering.ShapeBasedRenderable;
import com.example.rendering.tile_providing.TileProvider;
import com.example.representations.Coord;
import com.example.representations.shapes.Beam;
import com.example.terminal.TerminalSubscriber;
import com.example.utils.Bound;
import com.example.utils.Direction;
import com.example.utils.EndPointFinder;
import com.example.utils.NumberGenerator;

public class Trunk implements ShapeBasedRenderable, TerminalSubscriber
{
    private static final double GROUND_HEIGHT = 0;
    private static final double BUFFER = 0;
    private static final double START_X_POS_RATIO = 0.5; // 0 would make it start at left, 1 at right
    private final TrunkParams parameters;
    private final LinkedList<Beam> trunk_list;
    private double start_x;
    private final BranchFactory branch_factory;
    private final TileProvider tile_provider;
    private double last_left_branch_y = GROUND_HEIGHT;
    private double last_right_branch_y = GROUND_HEIGHT;

    public Trunk(TrunkParams params, Coord location, BranchFactory branch_factory, TileProvider tile_provider)
    {
        this.parameters = params;
        this.trunk_list = new LinkedList<>();
        //this.start_x = location.x();
        this.branch_factory = branch_factory;
        this.tile_provider = tile_provider;

        trunk_list.add(
            new Beam(
                location, 
                parameters.section_length(), 
                0, 
                parameters.width()
            )
        );
    }

    public void extendTrunk()
    {
        // find parameters for the next branch section
        final Beam branch_section = trunk_list.getLast();
        final Coord next_point = branch_section.getEndCoord();
        final double next_angle = find_angle(next_point.x(), branch_section.getAngle());

        // add the next branch section to the top
        trunk_list.add(new Beam(
                next_point, 
                parameters.section_length(), 
                next_angle, parameters.width()
            )
        );
    }

    private boolean shouldCreateBranch(double last_branch_y, double current_y)
    {
        if (current_y < last_branch_y + BUFFER)
        {
            return false;
        }

        return NumberGenerator.getRandomNumber() < parameters.branch_chance();
    }
    
    private double find_angle(double current_x, Double current_angle)
    {
        if (current_angle.isNaN())
        {
            current_angle = 0.0;
        }

        double random_angle;
        do
        {
            random_angle = current_angle + NumberGenerator.getRandomNumber(-1, 1) * parameters.angle_variance();
            //System.out.println(random_angle);
        }
        while (random_angle > Math.PI/3 || random_angle < -Math.PI/3);

        final double expected_x = EndPointFinder.findEndX(current_x, random_angle, parameters.section_length());
        final double wanted_offset = (start_x - expected_x) * parameters.centralness();
        
        double asin_arg = wanted_offset / parameters.section_length();
        // Clamp to [-1, 1] to avoid NaN
        asin_arg = Math.max(-1.0, Math.min(1.0, asin_arg));
        
        return Math.asin(asin_arg) + random_angle;
    }

    @Override
    public ArrayList<RenderableShape> growAndFetchRenderable(Bound bound)
    {
        final ArrayList<RenderableShape> bound_segments = new ArrayList<>();

        while (bound.checkIsInBound(trunk_list.getLast()))
        {
            Beam last_trunk_seg = trunk_list.getLast();
            extendTrunk();
            bound_segments.add(new RenderableShape(trunk_list.getLast(), tile_provider));
            //System.out.println("printing branches");

            // Check if a left/right branch should be created
            if (shouldCreateBranch(last_left_branch_y, last_trunk_seg.getRandomIntersection().y()))
            {
                final Branch new_branch = branch_factory.getBranch(trunk_list.getLast(), Direction.LEFT);
                // fully grows the new branch
                bound_segments.addAll(new_branch.growAndFetchRenderable(bound));
                last_left_branch_y = Math.max(new_branch.getFinalCoord().y(), new_branch.getStartCoord().y());
            }

            if (shouldCreateBranch(last_right_branch_y, last_trunk_seg.getRandomIntersection().y()))
            {
                final Branch new_branch = branch_factory.getBranch(trunk_list.getLast(), Direction.RIGHT);
                // fully grows the new branch
                bound_segments.addAll(new_branch.growAndFetchRenderable(bound));
                last_right_branch_y = Math.max(new_branch.getFinalCoord().y(), new_branch.getStartCoord().y());
            }
        }
        return bound_segments;
    }

    @Override
    public void trimSegments(Bound bound)
    {
        final Iterator<Beam> beam = trunk_list.iterator();
        while (beam.hasNext()) 
        {
            if (bound.checkLowerTrimmable(beam.next())) 
            {
                beam.remove(); // safe removal with iterator
            }
        }
    }

    /**
     * an update is called when the publisher recognises terminal size has changed
     */
    @Override
    public void updateTerminalSize(int x, int y) {
        // y not needed
        start_x = x * START_X_POS_RATIO;


    }
}
