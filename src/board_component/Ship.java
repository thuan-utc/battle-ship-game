package board_component;

import ship_enum.ShipName;

import java.util.ArrayList;
import java.util.List;

public class Ship {
    private String name; // aircraft carrier 5, battleship 4, submarine 3, cruiser 3, destroyer 2
    private int size;
    private boolean isSunk;
    private List<Cell> cells;
    private ShipName shipName;

    public Ship(String name, int size, ShipName shipName) {
        this.name = name;
        this.size = size;
        this.isSunk = false;
        cells = new ArrayList<>();
        this.shipName = shipName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public boolean isSunk() {
        return isSunk;
    }

    public void setSunk(boolean sunk) {
        isSunk = sunk;
    }

    public List<Cell> getCells() {
        return cells;
    }

    public void setCells(List<Cell> cells) {
        this.cells = cells;
    }

    public ShipName getShipName() {
        return shipName;
    }

    public void setShipName(ShipName shipName) {
        this.shipName = shipName;
    }
}
