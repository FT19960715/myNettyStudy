package com.Netty.Example.HL7.entity;

import java.io.Serializable;

public class Person implements Serializable {
    String name;

    public Person(String name, String id) {
        this.name = name;
        this.id = id;
    }

    String id;
    int age;
    String evaluation;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getEvaluation() {
        return evaluation;
    }

    public void setEvaluation(String evaluation) {
        this.evaluation = evaluation;
    }

    @Override
    public String toString() {
        return "Person{" +
                "name=" + name + '|' +
                " id=" + id + '|' +
                " age=" + age +"|"+
                " evaluation=" + evaluation + '|' +
                '}';
    }
}
