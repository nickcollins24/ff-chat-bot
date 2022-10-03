package ncollins.model.espn;

public enum TransactionType {
    ADD_FA(178),
    ADD_WAIVER(180),
    DROP_FA(179),
    DROP_WAIVER(181),
    DROP_ROSTER(239),
    TRADE_DECLINED(183),
    TRADE_UPHELD(185),
    TRADE_VETOED(186),
    TRADE_PROCESSED(187),
    TRADE_PROPOSED(190),
    TRADE_ACCEPTED(224),
    TRADE_BLOCK_ADDED(252);

    private final int value;

    TransactionType(final int newValue) {
        value = newValue;
    }

    public int getValue() { return value; }
}