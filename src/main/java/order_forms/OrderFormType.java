package order_forms;

public enum OrderFormType {
    DCC ("DCC"), DCC_ENG ("DCC_ENG"), XWORKS_ENG_NEW ("XWP_ENG_NEW"), XWORKS_ENG_EXT ("XWP_ENG_EXT");

    String name;

    OrderFormType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
