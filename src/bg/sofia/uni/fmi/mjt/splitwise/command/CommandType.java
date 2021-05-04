package bg.sofia.uni.fmi.mjt.splitwise.command;

public enum CommandType {
    ADD_FRIEND("add-friend"),
    CREATE_GROUP("create-group"),
    SPLIT("split"),
    SPLIT_GROUP("split-group"),
    GET_STATUS("get-status"),
    PAID("paid"),
    LOGIN("login"),
    LOGOUT("logout"),
    REGISTER("register"),
    SHOW_PAYMENTS("show-payments"),
    // todo SHOW_PAYMENTS_AFTER("show-payments-after"),
    // todo SHOW_PAYMENTS_BEFORE("show-payments-before"),
    SHOW_PAYMENTS_WITH_NAME("show-payments-name"),
    UNKNOWN("unknown-command");

    private final String description;

    CommandType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
