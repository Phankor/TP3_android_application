package com.example.tp3_android_marchal;

public class Config {
    // fields
    int id;
    String name;
    String lastname;

    // constructors
    public Config() {}
    public Config(int id, String name, String lastname) {
        this.id = id;
        this.name = name;
        this.lastname = lastname;
    }

    // properties
    public void setID(int id) {
        this.id = id;
    }
    public int getID() {
        return this.id;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return this.name;
    }
    public void setLastname(String lastname) {
        this.lastname = lastname;
    }
    public String getLastname() {
        return this.lastname;
    }


    @Override
    public String toString() {
        return "Config{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", lastname='" + lastname + '\'' +
                '}';
    }
}
