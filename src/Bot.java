import board_component.Cell;
import board_component.Ship;
import cell_enum.CellStage;

import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * do thám tìm vị trí tàu và đánh chìm
 */
public class Bot extends Player {
    /**
     * attack only white or Black
     */
    public static boolean attackWhite = true;
    private final static int boardSize = 10;

    public Bot(String playerName) {
        super(playerName);
    }

    public static void updateBoardProbability(Board board, AttackResult result) throws Exception {
        Cell lastAttack = board.findCellByName(result.getCellName());
        if (lastAttack == null) {
            return;
        }
        lastAttack.setStage(result.getResult());
        if (result.getSunkShipName() == null) {
            if (lastAttack.getStage() == CellStage.HIT_SUCCESS) {
                /* tăng chỉ số khả năng chứa tàu của 4 ô kề cùng hàng, cột -> hunting */
                String cellName = lastAttack.getName();
                increaseProbabilityAround(board, cellName);
                /*nếu có 2 điểm kề nhau bắn trúng trở lên, tăng khả năng theo hướng cột hoặc hàng*/
                increaseProbabilityByDirection(board, cellName);

            }
        } else {
            // liệt kê ra các cell thuộc tàu đã chìm, các cell xung quanh nếu lớn hơn 0 và chưa bị bắn phải giảm đi 1
            //todo issue: chỉ được giảm các ô mà do tàu này làm tăng khả năng
            result.getListCellShipSunk().forEach(currentCellName -> decreaseProbability(board, currentCellName));
            // tăng bù lại cho các ô bị giảm sai
            board.getCellList().forEach(cell -> {
                if (cell.getStage() == CellStage.HIT_SUCCESS) {
                    Coordinates coordinates = getCoordinates(cell.getName());
                    if (isValidPosition(coordinates.row - 1, coordinates.column)) {
                        Cell cellUp = board.getCellList().get((coordinates.row - 1) * boardSize + coordinates.column);
                        if (cellUp.getStage() == CellStage.NOT_HIT && cellUp.getProbabilityContainsShip() == 0) {
                            cellUp.setProbabilityContainsShip(1);
                        }
                    }
                    if (isValidPosition(coordinates.row + 1, coordinates.column)) {
                        Cell cellDown = board.getCellList().get((coordinates.row + 1) * boardSize + coordinates.column);
                        if (cellDown.getStage() == CellStage.NOT_HIT && cellDown.getProbabilityContainsShip() == 0) {
                            cellDown.setProbabilityContainsShip(1);
                        }
                    }
                    if (isValidPosition(coordinates.row, coordinates.column - 1)) {
                        Cell cellLeft = board.getCellList().get(coordinates.row * boardSize + coordinates.column - 1);
                        if (cellLeft.getStage() == CellStage.NOT_HIT && cellLeft.getProbabilityContainsShip() == 0) {
                            cellLeft.setProbabilityContainsShip(1);
                        }
                    }
                    if (isValidPosition(coordinates.row, coordinates.column + 1)) {
                        Cell cellRight = board.getCellList().get(coordinates.row * boardSize + coordinates.column + 1);
                        if (cellRight.getStage() == CellStage.NOT_HIT && cellRight.getProbabilityContainsShip() == 0) {
                            cellRight.setProbabilityContainsShip(1);
                        }
                    }
                }
            });
        }
        // implement đánh từ giữa ra, tìm các vùng có khả năng chứa tàu lớn nhất chưa chìm
        List<Ship> unSunkShip = board.getShipList().stream()
                .filter(ship -> !ship.isSunk()).toList();

        // theo row
        int maxUnSunkShipLength = unSunkShip.stream().map(Ship::getSize).max(Comparator.naturalOrder()).orElse(0);
        for (int i = 0; i < boardSize; i++) {
            int positionInList100ByRow;
            int positionInList100ByColumn;
            for (int j = 0; j < boardSize; j++) {

            }
        }
    }

    private static void increaseProbabilityByDirection(Board board, String cellName) {
        Coordinates result = getCoordinates(cellName);
        int row = result.row();
        int column = result.column();
        Cell cell;
        //update up
        int positionInList100 = (row - 1) * boardSize + column;
        if (isValidPosition(row - 1, column)) {
            cell = board.getCellList().get(positionInList100);
            if (cell.getStage() == CellStage.HIT_SUCCESS) {
                // ô hiện tại và trên nó đã bắn trúng -> tìm ô ở dưới và trên của cụm ô hiện tại
                updateCellDown(board, row, column, 1);// ô dưới ô hiện tại
                updateCellUp(board, row - 1, column, 1); // ô trên của ô trên cùng
            }
        }
        //update down
        positionInList100 = (row + 1) * boardSize + column;
        if (isValidPosition(row + 1, column)) {
            cell = board.getCellList().get(positionInList100);
            if (cell.getStage() == CellStage.HIT_SUCCESS) {
                // ô hiện tại và dưới nó đã bắn trúng -> tìm dưới và trên của cụm ô hiện tại
                updateCellUp(board, row, column, 1);
                updateCellDown(board, row + 1, column, 1);
            }
        }
        //update left
        positionInList100 = row * boardSize + column - 1;
        if (isValidPosition(row, column - 1)) {
            cell = board.getCellList().get(positionInList100);
            if (cell.getStage() == CellStage.HIT_SUCCESS) {
                // ô hiện tại và bên trái nó đã bắn trúng -> tìm ô ở bên phải và trái của cụm ô hiện tại
                updateCellRight(board, row, column, 1);
                updateCellLeft(board, row, column - 1, 1);
            }
        }
        //update right
        positionInList100 = row * boardSize + column + 1;
        if (isValidPosition(row, column + 1)) {
            cell = board.getCellList().get(positionInList100);
            if (cell.getStage() == CellStage.HIT_SUCCESS) {
                // ô hiện tại và bên phải nó đã bắn trúng -> tìm ô ở bên trái
                updateCellLeft(board, row, column, 1);
                updateCellRight(board, row, column + 1, 1);
            }
        }
    }

    private static void increaseProbabilityAround(Board board, String cellName) {
        Coordinates result = getCoordinates(cellName);
        //update up
        updateCellUp(board, result.row(), result.column(), 1);
        //update down
        updateCellDown(board, result.row(), result.column(), 1);
        //update left
        updateCellLeft(board, result.row(), result.column(), 1);
        //update right
        updateCellRight(board, result.row(), result.column(), 1);
    }

    private static void updateCellUp(Board board, int row, int column, int value) {
        int positionInList100;
        positionInList100 = (row - 1) * boardSize + column;
        if (isValidPosition(row - 1, column)) {
            updateValidCell(board, positionInList100, value);
        }
    }

    private static void updateCellDown(Board board, int row, int column, int value) {
        int positionInList100;
        positionInList100 = (row + value) * boardSize + column;
        if (isValidPosition(row + value, column)) {
            updateValidCell(board, positionInList100, value);
        }
    }

    private static void updateCellRight(Board board, int row, int column, int value) {
        int positionInList100;
        positionInList100 = row * boardSize + column + 1;
        if (isValidPosition(row, column + 1)) {
            updateValidCell(board, positionInList100, value);
        }
    }

    private static void updateCellLeft(Board board, int row, int column, int value) {
        int positionInList100 = row * boardSize + column - 1;
        if (isValidPosition(row, column - 1)) {
            updateValidCell(board, positionInList100, value);
        }
    }

    private static void updateValidCell(Board board, int positionInList100, int value) {
        Cell cell = board.getCellList().get(positionInList100);
        if (cell.getStage() == CellStage.NOT_HIT) {
            int newValue = cell.getProbabilityContainsShip() + value;
            if (newValue >= 0) {
                cell.setProbabilityContainsShip(newValue);
            }
        }
    }

    private static boolean isValidPosition(int row, int column) {
        return isValidRow(row) && isValidColumn(column);
    }

    private static boolean isValidColumn(int column) {
        return 0 <= column && column <= 9;
    }

    private static boolean isValidRow(int row) { // start from 0
        return 0 <= row && row <= 9;
    }

    private static void decreaseProbability(Board board, String cellName) {
        Coordinates result = getCoordinates(cellName);
        //update up
        updateCellUp(board, result.row(), result.column(), -1);
        //update down
        updateCellDown(board, result.row(), result.column(), -1);
        //update left
        updateCellLeft(board, result.row(), result.column(), -1);
        //update right
        updateCellRight(board, result.row(), result.column(), -1);
    }

    private static Coordinates getCoordinates(String cellName) {
        int column, row;
        char columnName = cellName.charAt(0);
        String rowName = cellName.substring(1);
        row = Integer.parseInt(rowName) - 1;
        column = (int) columnName - (int) 'A';
        return new Coordinates(row, column);
    }

    private record Coordinates(Integer row, Integer column) {
    }

    public static String findNextAttack(Board board) {
        boolean attackRandom = true;
        int maxProb = 0;
        List<Cell> cellList = board.getCellList();
        for (Cell cell : cellList) {
            if (cell.getStage() == CellStage.NOT_HIT && cell.getProbabilityContainsShip() > maxProb) {
                maxProb = cell.getProbabilityContainsShip();
                attackRandom = false;
            }
        }
        List<Cell> selectedCells;
        Random random = new Random();
        if (attackRandom) {
            String colorAttack = getColorAttack();
            selectedCells = getAllCellsNotHitWithColor(cellList, colorAttack);
            if (selectedCells.isEmpty()) {
                attackWhite = !attackWhite;
                colorAttack = getColorAttack();
                selectedCells = getAllCellsNotHitWithColor(cellList, colorAttack);
            }
        } else {
            int finalMaxProb = maxProb;
            selectedCells = cellList.stream()
                    .filter(c -> c.getProbabilityContainsShip() == finalMaxProb && c.getStage() == CellStage.NOT_HIT)
                    .collect(Collectors.toList());
        }
        return selectedCells.get(random.nextInt(selectedCells.size())).getName();
    }

    private static String getColorAttack() {
        return attackWhite ? "white" : "black";
    }

    private static List<Cell> getAllCellsNotHitWithColor(List<Cell> cellList, String colorAttack) {
        return cellList.stream()
                .filter(c -> c.getColor().equalsIgnoreCase(colorAttack) && c.getStage() == CellStage.NOT_HIT)
                .toList();
    }
}
