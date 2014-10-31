package core.entity;


public enum UserRoles {
    Manager("Manager"), Buyer("Buyer"), Driver("Driver"), Admin("Admin"), Guest("Guest");

    private final String name;

    private UserRoles(String s) {
        name = s;
    }
    public String toString(){
        return name;
    }

    public boolean equalsName(String otherName){
        return name.equalsIgnoreCase(otherName);
    }
    
    public static UserRoles get(String value) {
        for (int i = 0; i < values().length; ++i) {
            if (values()[i].equalsName(value)) return values()[i];
        }
        return null;
    } 
    
    public String getDescription() {
        if (Manager.equals(this)) return "Менеджер";
        if (Buyer.equals(this)) return "Закупщик";
        if (Driver.equals(this)) return "Водитель";
        if (Admin.equals(this)) return "Администратор";
        if (Guest.equals(this)) return "Гость";
        return "";

    }
}
