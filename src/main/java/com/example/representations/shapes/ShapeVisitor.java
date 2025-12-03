package com.example.representations.shapes;

/**
 * A visitor for geometric shapes
 * @param C return type produced by the visitor
 * @param R context's type passed to the visitor
 */
public interface ShapeVisitor<C, R> 
{
    /**
     * Used to perform actions on a polygon
     * @param polygon a polygon shape to be visited
     * @param context object providing context to the visitor
     * @return the result of the visitor's operation
     */
    public abstract R visitPolygon(Polygon polygon, C context);

    /**
     * Used to perform actions on a beam
     * @param beam a beam shape to be visited
     * @param context object providing context to the visitor
     * @return the result of the visitor's operation
     */
    public abstract R visitBeam(Beam beam, C context);
}
