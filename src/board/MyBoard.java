package board;

import board_component.Cell;
import board_component.Ship;

import java.util.List;
import java.util.Random;

public class MyBoard extends Board {
    public MyBoard() {
        super();
        List<Cell> cellList = this.getCellList();
        List<Ship> shipList = this.getShipList();
        String[] orientations = {"horizontal", "vertical"};
        for (Ship ship : shipList) {
            placeShip(ship, orientations, cellList);
        }
    }

    private static void placeShip(Ship ship, String[] orientations, List<Cell> cellList) {
        Random rand = new Random();
        String orientation = orientations[rand.nextInt(2)];
        int x, y;
        if (orientation.equalsIgnoreCase("horizontal")) {
            x = rand.nextInt(10);
            y = rand.nextInt(10 - ship.getSize());
        } else {
            x = rand.nextInt(10 - ship.getSize());
            y = rand.nextInt(10);
        }
        boolean canPlace = true;
        int positionInList100;
        for (int i = 0; i < ship.getSize(); i++) {
            positionInList100 = orientation.equalsIgnoreCase("horizontal")
                    ? x * 10 + y + i : (i + x) * 10 + y;
            Cell cell = cellList.get(positionInList100);
            if (cell.isInAShip()) {
                canPlace = false;
                break;
            }
        }
        if (canPlace) {
            for (int i = 0; i < ship.getSize(); i++) {
                positionInList100 = orientation.equalsIgnoreCase("horizontal")
                        ? x * 10 + y + i : (i + x) * 10 + y;
                Cell cell = cellList.get(positionInList100);
                cell.setInAShip(true);
                ship.getCells().add(cell);
            }
        } else {
            placeShip(ship, orientations, cellList);
        }
    }
}
