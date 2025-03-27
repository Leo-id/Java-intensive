package com.aston.aston;

/**
 * Создаем класс Car для представления в базе данных
 * который будет содержать такие параметры как название марки
 * модель и количество дверей
 */
public class Car {
    private Long id;
    private String name;
    private String model;
    private int doors;

    // создаем конструкторы
    public Car() {
    }

    public Car(String name, String model, int doors) {
        this.name = name;
        this.model = model;
        this.doors = doors;
    }

    // создаем геттеры и сеттеры
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getDoors() {
        return doors;
    }

    public void setDoors(int doors) {
        this.doors = doors;
    }

    // переопределим toString для вывода информации
    @Override
    public String toString() {
        return "Car{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", model='" + model + '\'' +
                ", doors=" + doors +
                '}';
    }
}