import board_component.Cell;
import board_component.Ship;
import cell_enum.CellStage;

import java.util.List;
import java.util.Random;

public class Player {
    private String name;
    private Board myBoard;
    private Board opponentBoard;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void printMyBoard() {
        System.out.printf(name + "\n");
        myBoard.printShipPosition();
    }

    public void printOpponentBoard() {
        System.out.print("machine" + "\n");
        opponentBoard.printCellStage();
    }

    public Player(String playerName) {
        this.myBoard = new Board();
        this.opponentBoard = new Board();
        this.name = playerName;
        placeShipForMyBoard();
    }

    private void placeShipForMyBoard() {
        String[] orientations = {"horizontal", "vertical"};
        for (Ship ship : myBoard.getShipList()) {
            placeShip(ship, orientations, myBoard.getCellList());
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
    // attack: give cellName, receive result of attack
    // receive attack: update myBoard, send result

    public AttackResult receiveAttack(String cellName) throws Exception {
        AttackResult attackResult = new AttackResult();
        List<Cell> cellList = myBoard.getCellList();
        Cell currentCellAttacked = cellList.stream()
                .filter(cell -> cell.getName().equalsIgnoreCase(cellName))
                .findFirst()
                .orElseThrow(() -> new Exception(cellName + " is Invalid!"));
        attackResult.setCellName(cellName);
        List<Ship> shipList = myBoard.getShipList();
        Ship currentShipAttacked = shipList.stream()
                .filter(ship -> ship.getCells().contains(currentCellAttacked))
                .findFirst()
                .orElse(null);
        if (currentShipAttacked != null) {
            boolean isNotSunk = false;
            for (Cell cellOfCurrentShip : currentShipAttacked.getCells()) {
                if (cellOfCurrentShip.getStage() == CellStage.NOT_HIT) {
                    isNotSunk = true;
                    break;
                }
            }
            if (isNotSunk) {
                attackResult.setResult(CellStage.HIT_SUCCESS);
            } else {
                for (Cell cellOfCurrentShip : currentShipAttacked.getCells()) {
                    if (cellOfCurrentShip.getStage() == CellStage.NOT_HIT) {
                        cellOfCurrentShip.setStage(CellStage.IN_SHIP_SUNK);
                    }
                }
                attackResult.setResult(CellStage.IN_SHIP_SUNK);
                List<String> cellNameOfShipSunks = currentShipAttacked.getCells().stream().map(Cell::getName).toList();
                attackResult.setListCellShipSunk(cellNameOfShipSunks);
            }
        } else {
            attackResult.setResult(CellStage.HIT_FAILED);
        }
        return attackResult;
    }

    public void updateOpponentBoard(AttackResult result) throws Exception {
        List<Cell> cellList = opponentBoard.getCellList();
        Cell currentCellAttacked = cellList.stream()
                .filter(cell -> cell.getName().equalsIgnoreCase(result.getCellName()))
                .findFirst()
                .orElseThrow(() -> new Exception(result.getCellName() + " is Invalid!"));
        if (result.getListCellShipSunk() == null) {
            currentCellAttacked.setStage(result.getResult());
        } else {
            Ship sunkShip = opponentBoard.getShipList().stream()
                    .filter(s -> s.getShipName() == result.getSunkShipName())
                    .findFirst()
                    .orElseThrow(() -> new Exception(result.getCellName() + " is Invalid!"));
            sunkShip.setSunk(true);
            List<Cell> cellOfShipSunk = opponentBoard.getCellList().stream()
                    .filter(c -> result.getListCellShipSunk().contains(c.getName()))
                    .toList();
            sunkShip.getCells().addAll(cellOfShipSunk);
        }

    }
}
