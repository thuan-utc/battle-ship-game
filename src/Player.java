import board.MyBoard;
import board.OpponentBoard;
import board_component.Cell;
import board_component.Ship;
import cell_enum.CellStage;
import ship_enum.ShipName;

import java.util.List;

public class Player {
    private String name;
    private MyBoard myBoard;
    private OpponentBoard opponentBoard;
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
        this.myBoard = new MyBoard();
        this.opponentBoard = new OpponentBoard();
        this.name = playerName;
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
        List<Ship> shipList = opponentBoard.getShipList();
        Ship currentShipAttacked = shipList.stream()
                .filter(ship -> ship.getCells().contains(currentCellAttacked))
                .findFirst()
                .orElse(null);
        if (result.getListCellShipSunk() == null) {
            currentCellAttacked.setStage(result.getResult());
        } else {
            if (currentShipAttacked == null) {
                throw new Exception(result.getCellName() + " is Invalid!");
            }
            currentShipAttacked.setSunk(true);
            currentCellAttacked.setStage(result.getResult());
        }

    }
}
