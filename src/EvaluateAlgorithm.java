import board_component.Cell;
import cell_enum.CellStage;

public class EvaluateAlgorithm {
    // attack only white or Black
    public static boolean attackWhite = true; // target parity
    //
    public static void updateBoard(Board board, Cell lastAttack, Integer sunkShipSize) {
        if (lastAttack == null) {
            return;
        }
        if (sunkShipSize == null) {
            if (lastAttack.getStage() == CellStage.HIT_FAILED) {

            } else if (lastAttack.getStage() == CellStage.HIT_SUCCESS) {
                // tăng chỉ số khả năng chứa tàu của 4 ô kề cùng hàng, cột //hunting
            }
        } else {
            // liệt kê ra các cell thuộc tàu đã chìm, thêm vào danh sách các tàu đã chìm (cập nhật opponetBoard's ship)
        }
        // ccần implement đánh từ giữa ra, tìm cacs vùng có khả năng chứa tàu lớn nhất chưa chìm
    }
}
