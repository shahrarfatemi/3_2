package com.example.lineofaction;

import java.util.Objects;

public class Position {
    int x,y;
    int id;

    public Position(int x, int y) {
        this.x = x;//col
        this.y = y;//row
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double distant(Position position){
        double dY = position.getY() - this.getY();
        double dX = position.getX() - this.getX();
        return Math.sqrt(Math.pow(dX,2)+Math.pow(dY,2));
    }

    @Override
    public String toString() {
        return "x "+getX()+" y "+getY()+"\n";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Position)) return false;
        Position position = (Position) o;
        return getX() == position.getX() &&
                getY() == position.getY();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getX(), getY());
    }
}
