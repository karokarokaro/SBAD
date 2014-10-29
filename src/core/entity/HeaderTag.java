package core.entity;

import java.util.Map;

public class HeaderTag {
    private String name;
    private Map<String, String> attributes;
    private String value;

    public String getName() {
        return name;
    }

    public HeaderTag setName(String name) {
        this.name = name;
        return this;
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }

    public HeaderTag setAttributes(Map<String, String> attributes) {
        this.attributes = attributes;
        return this;
    }

    public String getValue() {
        return value;
    }

    public HeaderTag setValue(String value) {
        this.value = value;
        return this;
    }
}
