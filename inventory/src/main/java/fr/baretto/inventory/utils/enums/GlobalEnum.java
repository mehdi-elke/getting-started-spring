package fr.baretto.inventory.utils.enums;

public enum GlobalEnum {
    // Enum for the different types of products
    OPERATION_SALE("sale"),
    OPERATION_PROCUREMENT("procurement");

    private final String type;

    GlobalEnum(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
