package core.database;

import java.math.BigInteger;
import java.sql.Timestamp;


public class DBAttribute {
    private BigInteger id;
    private String name;
    private String description;
    private BigInteger valueId;
    private BigInteger valueTypeId;
    private String valueTypeName;
    private String valueTypeUsing;
    private String textValue;
    private Timestamp timestampValue;
    private BigInteger idValue;
    private Double doubleValue;

    public BigInteger getValueId() {
        return valueId;
    }

    public DBAttribute setValueId(BigInteger valueId) {
        this.valueId = valueId;
        return this;
    }

    public String getValueTypeUsing() {
        return valueTypeUsing;
    }

    public DBAttribute setValueTypeUsing(String valueTypeUsing) {
        this.valueTypeUsing = valueTypeUsing;
        return this;
    }

    public BigInteger getValueTypeId() {
        return valueTypeId;
    }

    public DBAttribute setValueTypeId(BigInteger valueTypeId) {
        this.valueTypeId = valueTypeId;
        return this;
    }

    public String getValueTypeName() {
        return valueTypeName;
    }

    public DBAttribute setValueTypeName(String valueTypeName) {
        this.valueTypeName = valueTypeName;
        return this;
    }

    public BigInteger getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public DBAttribute setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public DBAttribute setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getTextValue() {
        return textValue;
    }

    public DBAttribute setTextValue(String textValue) {
        this.textValue = textValue;
        return this;
    }

    public Timestamp getTimestampValue() {
        return timestampValue;
    }

    public DBAttribute setTimestampValue(Timestamp timestampValue) {
        this.timestampValue = timestampValue;
        return this;
    }

    public BigInteger getIdValue() {
        return idValue;
    }

    public DBAttribute setIdValue(BigInteger idValue) {
        this.idValue = idValue;
        return this;
    }

    public double getDoubleValue() {
        return doubleValue;
    }

    public DBAttribute setDoubleValue(double doubleValue) {
        this.doubleValue = doubleValue;
        return this;
    }

    public DBAttribute(BigInteger id) {
        this.id = id;
    }

    public boolean getBooleanValue() {
        if (valueTypeUsing.equals("double")) {
            return doubleValue.intValue() != 0;
        } else {
            //todo
            return false;
        }
    }

    public Integer getIntValue() {
        if (valueTypeUsing.equals("double")) {
            return doubleValue.intValue();
        } else {
            //todo
            return 0;
        }
    }

    public boolean equals(DBAttribute dbAttribute) {
        Object val1 = null;
        Object val2 = null;
        if ("text".equals(valueTypeUsing)) {
            val1 = getTextValue();
            val2 = dbAttribute.getTextValue();
        } else if ("id".equals(valueTypeUsing)) {
            val1 = getIdValue();
            val2 = dbAttribute.getIdValue();
        } else if ("ts".equals(valueTypeUsing)) {
            val1 = getTimestampValue();
            val2 = dbAttribute.getTimestampValue();
        } else if ("double".equals(valueTypeUsing)) {
            val1 = getDoubleValue();
            val2 = dbAttribute.getDoubleValue();
        }
        return (val1 == null && val2 == null) || (val1 != null && val1.equals(val2));
    }
}
