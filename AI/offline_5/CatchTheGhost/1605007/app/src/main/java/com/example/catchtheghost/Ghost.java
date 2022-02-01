package com.example.catchtheghost;

import java.util.Random;
// x =>=>=>=>
// y downwards
public class Ghost {
    private Cell currentCell;
    private int ROW;
    private int COLUMN;
    private double moveProbabilty;

    public Ghost(Cell currentCell, int ROW, int COLUMN) {
        this.currentCell = currentCell;
        this.ROW = ROW;
        this.COLUMN = COLUMN;
    }

    Cell makeMovement(){
        Random random = new Random();
        this.moveProbabilty = random.nextDouble();
        System.out.println("move : "+this.moveProbabilty);
        if(this.moveProbabilty < 0.008){
            System.out.println("don't move");
        }
        else if(this.moveProbabilty < 0.04){
            Cell temp = makeDiagonalMovement();
            currentCell.setX(temp.getX());
            currentCell.setY(temp.getY());
            System.out.println("move diag "+currentCell);
        }
        else{
            Cell temp = makeSideWiseMovement();
            currentCell.setX(temp.getX());
            currentCell.setY(temp.getY());
            System.out.println("move sidewise "+currentCell);
        }
        return currentCell;
    }

    private Cell makeSideWiseMovement(){
        Random random = new Random();
        int sides;
        if(currentCell.getX() == 0){
            if(currentCell.getY() == 0){//(0,0)
                sides = random.nextInt(2);
                return (sides == 0)?new Cell(currentCell.getX() + 1, currentCell.getY()):
                        new Cell(currentCell.getX(), currentCell.getY() + 1);
            }
            if(currentCell.getY() == ROW - 1){//(0,row-1)
                sides = random.nextInt(2);
                return (sides == 0)?new Cell(currentCell.getX() + 1, currentCell.getY()):
                        new Cell(currentCell.getX(), currentCell.getY() - 1);
            }
            else{
                sides = random.nextInt(3);
                switch (sides){
                    case 0 : return new Cell(currentCell.getX(), currentCell.getY() - 1);//(0,row--)

                    case 1 : return new Cell(currentCell.getX() + 1, currentCell.getY());//(1,row)

                    case 2 : return new Cell(currentCell.getX(), currentCell.getY() + 1);//(0,row++)
                }
            }
        }
        else if(currentCell.getX() == COLUMN - 1){
            if(currentCell.getY() == 0){//(col - 1,0)
                sides = random.nextInt(2);
                return (sides == 0)?new Cell(currentCell.getX() - 1, currentCell.getY()):
                        new Cell(currentCell.getX(), currentCell.getY() + 1);
            }
            if(currentCell.getY() == ROW - 1){//(col - 1,row-1)
                sides = random.nextInt(2);
                return (sides == 0)?new Cell(currentCell.getX() - 1, currentCell.getY()):
                        new Cell(currentCell.getX(), currentCell.getY() - 1);
            }
            else{
                sides = random.nextInt(3);
                switch (sides){
                    case 0 : return new Cell(currentCell.getX(), currentCell.getY() - 1);//(col-1,row--)

                    case 1 : return new Cell(currentCell.getX() - 1, currentCell.getY());//(col-2,row)

                    case 2 : return new Cell(currentCell.getX(), currentCell.getY() + 1);//(col-1,row++)
                }
            }
        }
        else if(currentCell.getY() == 0){
            sides = random.nextInt(3);
            switch (sides){//baame,niche,daane
                case 0 : return new Cell(currentCell.getX() - 1, currentCell.getY());

                case 1 : return new Cell(currentCell.getX(), currentCell.getY() + 1);

                case 2 : return new Cell(currentCell.getX() + 1, currentCell.getY());
            }
        }
        else if(currentCell.getY() == ROW - 1){
            sides = random.nextInt(3);
            switch (sides){//baame,uporee,daane
                case 0 : return new Cell(currentCell.getX() - 1, currentCell.getY());

                case 1 : return new Cell(currentCell.getX(), currentCell.getY() -1);

                case 2 : return new Cell(currentCell.getX() + 1, currentCell.getY());
            }
        }
        else{
            sides = random.nextInt(4);
            switch (sides){//baame,uporee,daane,niche
                case 0 : return new Cell(currentCell.getX() - 1, currentCell.getY());

                case 1 : return new Cell(currentCell.getX(), currentCell.getY() -1);

                case 2 : return new Cell(currentCell.getX() + 1, currentCell.getY());

                case 3 : return new Cell(currentCell.getX(), currentCell.getY() + 1);
            }
        }
        return new Cell(-1,-1);
    }

    private Cell makeDiagonalMovement(){
        Random random = new Random();
        int sides;
        boolean columnWise;
        if(currentCell.getX() == 0){
            if(currentCell.getY() == 0){//(0,0)
                return new Cell(currentCell.getX() + 1, currentCell.getY() + 1);
            }
            if(currentCell.getY() == ROW - 1){//(0,row-1)
                return new Cell(currentCell.getX() + 1, currentCell.getY() - 1);
            }
            else{//daaneupore,daaneniche
                sides = random.nextInt(2);
                switch (sides){
                    case 0 : return new Cell(currentCell.getX() + 1, currentCell.getY() - 1);

                    case 1 : return new Cell(currentCell.getX() + 1, currentCell.getY() + 1);//(1,row)
                }
            }
        }
        else if(currentCell.getX() == COLUMN - 1){
            if(currentCell.getY() == 0){//(col - 1,0)
                return new Cell(currentCell.getX() - 1, currentCell.getY() + 1);
            }
            if(currentCell.getY() == ROW - 1){//(0,row-1)
                return new Cell(currentCell.getX() - 1, currentCell.getY() - 1);
            }
            else{//baameupore,baameniche
                sides = random.nextInt(2);
                switch (sides){
                    case 0 : return new Cell(currentCell.getX() - 1, currentCell.getY() - 1);

                    case 1 : return new Cell(currentCell.getX() - 1, currentCell.getY() + 1);//(1,row)
                }
            }
        }
        else if(currentCell.getY() == 0){//baameniche,daaneniche
            sides = random.nextInt(2);
            switch (sides){
                case 0 : return new Cell(currentCell.getX() - 1, currentCell.getY() + 1);

                case 1 : return new Cell(currentCell.getX() + 1, currentCell.getY() + 1);//(1,row)
            }
        }
        else if(currentCell.getY() == ROW - 1){//baameupore,daaneupore
            sides = random.nextInt(2);
            switch (sides){
                case 0 : return new Cell(currentCell.getX() - 1, currentCell.getY() - 1);

                case 1 : return new Cell(currentCell.getX() + 1, currentCell.getY() - 1);//(1,row)
            }
        }
        else{
            sides = random.nextInt(4);
            switch (sides){//n-w,n-e,s-e,s-w
                case 0 : return new Cell(currentCell.getX() - 1, currentCell.getY() - 1);

                case 1 : return new Cell(currentCell.getX() + 1, currentCell.getY() -1);

                case 2 : return new Cell(currentCell.getX() + 1, currentCell.getY() + 1);

                case 3 : return new Cell(currentCell.getX() - 1, currentCell.getY() + 1);
            }
        }
        return new Cell(-1,-1);
    }

    public Cell getCurrentCell() {
        return currentCell;
    }
}
