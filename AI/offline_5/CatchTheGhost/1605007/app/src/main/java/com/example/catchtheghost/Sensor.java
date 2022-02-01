package com.example.catchtheghost;

public class Sensor {
    private Cell cell;
    private int row;
    private int col;
    private int color;
    
    public final int red = 1;
    public final int orange = 2;
    public final int green = 3;
    
    private int maxDistance;

    public Sensor(Cell cell, int row, int col) {
        this.cell = cell;
        this.row = row;
        this.col = col;
        maxDistance = Math.abs(row - 1) + Math.abs(col - 1);
    }
    
    public int detectGhost(Cell ghostCell){
        int delCol = Math.abs(this.cell.getX() - ghostCell.getX());
        int delRow = Math.abs(this.cell.getY() - ghostCell.getY());
        int distance = delRow + delCol;
        if((distance*1.0)/maxDistance < 0.30){
            color = red;
        }
        else if((distance*1.0)/maxDistance < 0.60){
            color = orange;
        }
        else {
            color = green;
        }
        showColour();
        return color;
    }
    
    public double ghostProbabilityOfCell(Cell cell){
        int distance = cell.calcManhattanDistance(this.cell);
        double ratio = distance*1.0/maxDistance; 
        if(ratio < 0.30){
            if(this.color == red){
                return 0.85;
            }
            if(this.color == orange){
                return 0.007;
            }
            return 0.007;
        }
        else if(ratio < 0.60){
            if(this.color == red){
                return 0.15;
            }
            if(this.color == orange){
                return 0.90;
            }
            return 0.10;
        }
        else{
            if(this.color == red){
                return 0.05;
            }
            if(this.color == orange){
                return 0.15;
            }
            return 0.90;
        }
    }

    private void showColour(){
        if(color == red){
            System.out.println("It's RED!!");
        }
        else if(color == orange){
            System.out.println("It's ORANGE!!");
        }
        else {
            System.out.println("It's GREEN!!");
        }
    }

    public Cell getCell() {
        return cell;
    }

    public void setCell(int x, int y) {
        this.cell.setX(x);
        this.cell.setY(y);
    }
}

