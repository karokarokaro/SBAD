package core.database;

import core.Logger;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: KARO
 * Date: 16.08.13
 * Time: 23:45
 * To change this template use File | Settings | File Templates.
 */
public class DBObject {
    private BigInteger id;
    private String name;
    private String description;
    private List<DBAttribute> attributes;
    private BigInteger typeId;
    private String typeName;
    private String typeDescription;

    public void setAttributes(List<DBAttribute> attributes) {
        this.attributes = attributes;
    }

    public boolean deleteObject() {
        return core.helpers.DBHelper.deleteObject(id);
    }
    public DBAttribute getAttributeById(BigInteger id) {
        for (DBAttribute dbAttribute: attributes) {
            if (dbAttribute.getId().equals(id)) return dbAttribute;
        }
        return null;
    }

    public DBAttribute getAttributeById(String id) {
        return getAttributeById(new BigInteger(id));
    }

    public DBAttribute getAttributeByValueId(String id) {
        return getAttributeByValueId(new BigInteger(id));
    }

    public DBAttribute getAttributeByValueId(BigInteger valueId) {
        for (DBAttribute dbAttribute: attributes) {
            if (dbAttribute.getValueId() == null) Logger.error(dbAttribute.getId().toString());
            if (dbAttribute.getValueId() != null && dbAttribute.getValueId().equals(valueId)) return dbAttribute;
        }
        return null;
    }
    public List<DBAttribute> getAttributesById(BigInteger id) {
        List<DBAttribute> res = new ArrayList<DBAttribute>();
        for (DBAttribute dbAttribute: attributes) {
            if (dbAttribute.getId().equals(id)) res.add(dbAttribute);
        }
        return res;
    }

    public List<DBAttribute> getAttributesById(String id) {
        return getAttributesById(new BigInteger(id));
    }

    public String getTypeName() {
        return typeName;
    }

    public DBObject setTypeName(String typeName) {
        this.typeName = typeName;
        return this;
    }

    public String getTypeDescription() {
        return typeDescription;
    }

    public DBObject setTypeDescription(String typeDescription) {
        this.typeDescription = typeDescription;
        return this;
    }

    public BigInteger getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public DBObject setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public DBObject setDescription(String description) {
        this.description = description;
        return this;
    }

    public List<DBAttribute> getAttributes() {
        return attributes;
    }

    public DBAttribute getAttributeByName(String name) {
        if (name == null) return null;
        for (DBAttribute dbAttribute: attributes) {
            if (name.toLowerCase().equals(dbAttribute.getName().toLowerCase())) return dbAttribute;
        }
        return null;
    }

    public BigInteger getTypeId() {
        return typeId;
    }

    public DBObject setTypeId(BigInteger typeId) {
        this.typeId = typeId;
        return this;
    }

    public DBObject(BigInteger id) {
        this.id = id;
        attributes = new ArrayList<DBAttribute>();
    }
}
