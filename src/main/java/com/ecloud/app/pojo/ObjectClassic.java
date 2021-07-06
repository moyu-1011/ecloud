package com.ecloud.app.pojo;

import javax.persistence.*;

@Entity
@Table(name = "object_classic")
public class ObjectClassic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column
    private String name;
    @Column
    private String classic;

    public ObjectClassic() {
    }

    public ObjectClassic(Integer id, String name, String classic) {
        this.id = id;
        this.name = name;
        this.classic = classic;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClassic() {
        return classic;
    }

    public void setClassic(String classic) {
        this.classic = classic;
    }

    @Override
    public String toString() {
        return "ObjectClassic{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", classic='" + classic + '\'' +
                '}';
    }
}
