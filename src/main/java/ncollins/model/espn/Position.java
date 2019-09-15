package ncollins.model.espn;

public enum Position {
    QB(1),
    RB(2),
    WR(3),
    TE(4),
    K(5),
    D(16),
    FLEX(0);

    private final int value;

    Position(final int value) {
        this.value = value;
    }

    public int getValue() { return value; }
}
