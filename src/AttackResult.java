import cell_enum.CellStage;
import ship_enum.ShipName;

import java.util.List;

public class AttackResult {
    private String cellName;
    private CellStage result;
    private List<String> listCellShipSunk;
    private ShipName sunkShipName;

    public AttackResult() {
    }

    public AttackResult(String cellName, CellStage result, List<String> listCellShipSunk) {
        this.cellName = cellName;
        this.result = result;
        this.listCellShipSunk = listCellShipSunk;
    }

    public String getCellName() {
        return cellName;
    }

    public void setCellName(String cellName) {
        this.cellName = cellName;
    }

    public CellStage getResult() {
        return result;
    }

    public void setResult(CellStage result) {
        this.result = result;
    }

    public List<String> getListCellShipSunk() {
        return listCellShipSunk;
    }

    public void setListCellShipSunk(List<String> listCellShipSunk) {
        this.listCellShipSunk = listCellShipSunk;
    }

    public ShipName getSunkShipName() {
        return sunkShipName;
    }

    public void setSunkShipName(ShipName sunkShipName) {
        this.sunkShipName = sunkShipName;
    }
}
