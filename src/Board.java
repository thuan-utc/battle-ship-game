import board_component.Cell;
import board_component.Ship;
import ship_enum.ShipName;

import java.util.ArrayList;
import java.util.List;

public class Board {
    private List<Cell> cellList;
    private List<Ship> shipList;

    public Board() {
        initialCells();
        initialShips();
    }

    private void initialShips() {
        shipList = new ArrayList<>(5);
        shipList.add(new Ship("Aircraft Carrier", 5, ShipName.AIRCRAFT_CARRIER));
        shipList.add(new Ship("Battleship", 4, ShipName.BATTLE_SHIP));
        shipList.add(new Ship("Submarine", 3, ShipName.SUBMARINE));
        shipList.add(new Ship("Cruiser", 3, ShipName.CRUISER));
        shipList.add(new Ship("Destroyer", 2, ShipName.DESTROYER));
    }

    private void initialCells() {
        cellList = new ArrayList<>(100);
        String[] columns = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J"};
        String[] rows = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
        String color = "white";
        for (String row : rows) {
            for (String col : columns) {
                Cell cell = new Cell();
                String cellName = col + row;
                cell.setName(cellName);
                cell.setColor(color);
                color = color.equalsIgnoreCase("white") ? "black" : "white";
                cellList.add(cell);
            }
        }
    }

    public void printShipPosition() {
        int rowIndex = 0;
        String[] columns = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J"};
        System.out.print("   ");
        for (String column : columns) {
            System.out.printf("%-2s", column);
        }
        System.out.println();
        for (int i = 0; i < 10; i++) {
            System.out.printf("%-3d", ++rowIndex);
            for (int j = 0; j < 10; j++) {
                System.out.printf("%-2s", getCellList().get(i * 10 + j).printShip());
            }
            System.out.println();
        }
    }

    public void printCellStage() {
        int rowIndex = 0;
        String[] columns = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J"};
        System.out.print("   ");
        for (String column : columns) {
            System.out.printf("%-2s", column);
        }
        System.out.println();
        for (int i = 0; i < 10; i++) {
            System.out.printf("%-3d", ++rowIndex);
            for (int j = 0; j < 10; j++) {
                System.out.printf("%-2s", getCellList().get(i * 10 + j).printCellState());
            }
            System.out.println();
        }
    }

    public void printCellWithProbabilityContainShip() {
        int rowIndex = 0;
        String[] columns = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J"};
        System.out.print("   ");
        for (String column : columns) {
            System.out.printf("%-2s", column);
        }
        System.out.println();
        for (int i = 0; i < 10; i++) {
            System.out.printf("%-3d", ++rowIndex);
            for (int j = 0; j < 10; j++) {
                System.out.printf("%-2s", getCellList().get(i * 10 + j).printProbabilityContainShip());
            }
            System.out.println();
        }
    }

    public Cell findCellByName(String cellName) throws Exception {
        return cellList.stream()
                .filter(cell -> cell.getName().equalsIgnoreCase(cellName))
                .findFirst()
                .orElseThrow(() -> new Exception(cellName + " is Invalid!"));
    }

    public Ship findShipByName(ShipName shipName) throws Exception {
        return shipList.stream()
                .filter(ship -> ship.getShipName() == shipName)
                .findFirst()
                .orElseThrow(() -> new Exception(shipName + " is Invalid!"));
    }

    public List<Cell> getCellList() {
        return cellList;
    }

    public void setCellList(List<Cell> cellList) {
        this.cellList = cellList;
    }

    public List<Ship> getShipList() {
        return shipList;
    }

    public void setShipList(List<Ship> shipList) {
        this.shipList = shipList;
    }
}
