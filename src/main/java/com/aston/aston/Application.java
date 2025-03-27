package com.aston.aston;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Application {
    public static void main(String[] args) {
        // Настройка подключения к H2 in-memory базе данных
        //DB_CLOSE_DELAY=0 H2 закроет БД, как только все соединения будут закрыты

        String url = "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=0";
        String username = "sa";
        String password = "";

        //try-with-resources гарантирует, что Connection закроется после выполнения блока.
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            // Создаем репозиторий и сервис
            CarRepository carRepository = new CarRepository(connection);
            CarService carService = new CarService(carRepository);

            // Пример использования
            Car car1 = new Car("Bugatti", "Veyron", 2);
            Car car2 = new Car("Renault", "Logan ", 4);

            // Сохранение авто
            carService.save(car1);
            carService.save(car2);

            // Поиск всех авто
            System.out.println("Все авто:");
            carService.findAll().forEach(System.out::println);

            // Поиск по ID
            Car foundCar = carService.findById(car2.getId());
            System.out.println("Найден автомобиль: " + foundCar);

            // Обновление авто
            foundCar.setModel("Scenic");
            foundCar.setDoors(5);
            carService.update(foundCar);
            System.out.println("После обновления: " + carService.findById(foundCar.getId()));

            // Удаление
            carService.delete(car2.getId());
            System.out.println("После удаления:");
            carService.findAll().forEach(System.out::println);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
