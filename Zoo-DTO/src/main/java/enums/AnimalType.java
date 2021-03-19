package enums;

public enum AnimalType {
    DOG("dog","собака"),
    CAT("cat","кошка"),
    REPTILE("reptile","рептилия"),
    RODENT("rodent","грызун"),
    BIRD("bird","птица"),
    OTHER("other","другое");
    public String russianName;
    public String name;

    AnimalType(String name,String russianName) {
        this.russianName = russianName;
        this.name = name;
    }
}
