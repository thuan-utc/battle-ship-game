package ship_enum;

public enum ShipName {
    AIRCRAFT_CARRIER("Aircraft Carrier"), BATTLE_SHIP("Battleship"), SUBMARINE("Submarine"),
    CRUISER("Cruiser"), DESTROYER("Destroyer");
    private String name;

    ShipName(String name) {
        this.name = name();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
