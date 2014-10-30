package core.entity;


public enum UserRoles {
    OAO("ОАО"), OOO("ООО"), IP("ИП");

    private final String name;

    private UserRoles(String s) {
        name = s;
    }
    public String toString(){
        return name;
    }

    public boolean equalsName(String otherName){
        return (otherName == null)? false : name.equals(otherName);
    }
}
