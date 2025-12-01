package com.example.representations.shapes;

public interface ShapeVisitor<T> 
{
    public abstract T visitPolygon(Polygon polygon);
    public abstract T visitBeam(Beam beam);
}
