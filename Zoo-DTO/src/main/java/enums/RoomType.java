package enums;

public enum RoomType {
    COMMON("common", "стандартная"),
    LARGE("large", "большая"),
    VIP("vip", "вип");

    public String russianName;
    public String name;

    RoomType(String name, String russianName) {
        this.russianName = russianName;
        this.name = name;
    }
}