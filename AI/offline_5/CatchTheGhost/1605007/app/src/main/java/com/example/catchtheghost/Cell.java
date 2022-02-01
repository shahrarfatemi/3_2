package com.example.catchtheghost;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public class Cell {
    private int x,y;
    private double ghostProbability;

    public Cell(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void initializeProbability(double ghostProbability){
        this.setGhostProbability(ghostProbability);
    }

    public int calcManhattanDistance(Cell cell){
        int delCol = Math.abs(this.getX() - cell.getX());
        int delRow = Math.abs(this.getY() - cell.getY());
        int distance = delRow + delCol;
        return distance;
    }

    public double getGhostProbability() {
        return ghostProbability;
    }

    public void setGhostProbability(double ghostProbability) {
        Double truncatedDouble = BigDecimal.valueOf(ghostProbability)
                .setScale(3, RoundingMode.HALF_UP)
                .doubleValue();
        this.ghostProbability = truncatedDouble;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cell cell = (Cell) o;
        return x == cell.x &&
                y == cell.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return "Cell{" +
                "x=" + x +
                ", y=" + y +
                "}";
    }
}
