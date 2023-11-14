package board_component;

import cell_enum.CellStage;

public class Cell {
    private String color; // red or white
    private String name; // A1, A2, .., A10, B1,.., B10
    private CellStage stage;
    private int probabilityContainsShip;
    private boolean inAShip;

    public Cell() {
        probabilityContainsShip = 0;
        stage = CellStage.NOT_HIT;
    }

    @Override
    public String toString() {
        return name;
    }

    public String printCellState() {
        return stage == CellStage.NOT_HIT ? "0"
                : stage == CellStage.HIT_FAILED ? "x"
                : stage == CellStage.HIT_SUCCESS ? "+"
                : stage == CellStage.IN_SHIP_SUNK ? "*"
                : "";
    }

    public String printShip() {
        return inAShip ? "+" : "0";
    }

    public String printProbabilityContainShip() {
        return stage == CellStage.NOT_HIT ? String.valueOf(probabilityContainsShip)
                : stage == CellStage.HIT_FAILED ? "x"
                : stage == CellStage.HIT_SUCCESS ? "+"
                : stage == CellStage.IN_SHIP_SUNK ? "*"
                : "";
    }

    public String printColor() {
        return color.equalsIgnoreCase("white") ? "0" : "1";
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getProbabilityContainsShip() {
        return probabilityContainsShip;
    }

    public void setProbabilityContainsShip(int probabilityContainsShip) {
        this.probabilityContainsShip = probabilityContainsShip;
    }

    public CellStage getStage() {
        return stage;
    }

    public void setStage(CellStage stage) {
        this.stage = stage;
    }

    public boolean isInAShip() {
        return inAShip;
    }

    public void setInAShip(boolean inAShip) {
        this.inAShip = inAShip;
    }
}
