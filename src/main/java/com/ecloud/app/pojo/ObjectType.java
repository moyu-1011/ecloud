package com.ecloud.app.pojo;

import javax.persistence.*;

@Entity
@Table(name = "object_type")
public class ObjectType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "type")
    private String type;

    public ObjectType() {
    }

    public ObjectType(Integer id, String type) {
        this.id = id;
        this.type = type;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "ObjectType{" +
                "id=" + id +
                ", type='" + type + '\'' +
                '}';
    }
}
