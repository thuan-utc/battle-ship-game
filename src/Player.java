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

    public Board getMyBoard() {
        return myBoard;
    }

    public void setMyBoard(Board myBoard) {
        this.myBoard = myBoard;
    }

    public Board getOpponentBoard() {
        return opponentBoard;
    }

    public void setOpponentBoard(Board opponentBoard) {
        this.opponentBoard = opponentBoard;
    }

    public void printMyBoard() {
        System.out.printf(name + "\n");
        myBoard.printShipPosition();
    }

    public void printOpponentBoard() {
        System.out.print("machine" + "\n");
        opponentBoard.printCellStage();
    }

    public void printOpponentBoardWithProbabilityContainShip() {
        opponentBoard.printCellWithProbabilityContainShip();
    }

    public void printOpponentBoardWithColor() {
        opponentBoard.printCellWithColor();
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
        Cell currentCellAttacked = myBoard.findCellByName(cellName);
        attackResult.setCellName(cellName);
        List<Ship> shipList = myBoard.getShipList();
        Ship currentShipAttacked = shipList.stream()
                .filter(ship -> ship.getCells().contains(currentCellAttacked))
                .findFirst()
                .orElse(null);
        if (currentShipAttacked != null) {
            attackResult.setResult(CellStage.HIT_SUCCESS);
            currentCellAttacked.setStage(CellStage.HIT_SUCCESS);
            boolean isSunk = true;
            for (Cell cellOfCurrentShip : currentShipAttacked.getCells()) {
                if (cellOfCurrentShip.getStage() == CellStage.NOT_HIT) {
                    isSunk = false;
                    break;
                }
            }
            if (isSunk) {
                for (Cell cellOfCurrentShip : currentShipAttacked.getCells()) {
                    cellOfCurrentShip.setStage(CellStage.IN_SHIP_SUNK);
                }
                currentShipAttacked.setSunk(true);
                attackResult.setResult(CellStage.IN_SHIP_SUNK);
                attackResult.setSunkShipName(currentShipAttacked.getShipName());
                List<String> cellNameOfShipSunks = currentShipAttacked.getCells().stream().map(Cell::getName).toList();
                attackResult.setListCellShipSunk(cellNameOfShipSunks);
            }
        } else {
            attackResult.setResult(CellStage.HIT_FAILED);
            currentCellAttacked.setStage(CellStage.HIT_FAILED);
        }
        return attackResult;
    }

    public void updateOpponentBoard(AttackResult result) throws Exception {
        Cell currentCellAttacked = opponentBoard.findCellByName(result.getCellName());
        if (result.getListCellShipSunk() == null) {
            currentCellAttacked.setStage(result.getResult());
        } else {
            Ship sunkShip = opponentBoard.findShipByName(result.getSunkShipName());
            sunkShip.setSunk(true);
            List<Cell> cellOfShipSunk = opponentBoard.getCellList().stream()
                    .filter(c -> result.getListCellShipSunk().contains(c.getName()))
                    .toList();
            cellOfShipSunk.forEach(c -> c.setStage(CellStage.IN_SHIP_SUNK));
            sunkShip.getCells().addAll(cellOfShipSunk);
        }

    }

    public boolean isLooser() {
        boolean isLooser = true;
        for (Ship ship : myBoard.getShipList()) {
            if (!ship.isSunk()) {
                isLooser = false;
                break;
            }
        }
        return isLooser;
    }
}
