package com.aston.aston;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CarRepositoryIntegrationTest {

    private Connection connection;
    private CarRepository carRepository;

    @BeforeEach
    void setUp() throws SQLException {
        // Настройка in-memory H2 базы данных перед каждым тестом.
        // Инициализируем схему базы данных из schema.sql
        // Создаем экземпляр CarRepository
        String url = "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=0;INIT=RUNSCRIPT FROM 'classpath:schema.sql'";
        String username = "sa";
        String password = "";
        connection = DriverManager.getConnection(url, username, password);
        carRepository = new CarRepository(connection);
    }

    @AfterEach
    void tearDown() throws SQLException {
        // Очистка базы данных после каждого теста
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("DROP ALL OBJECTS DELETE FILES");
        }
        connection.close();   // Закрываем соединение
    }


    @Test
    void save_ShouldPersistCarAndSetId() {

        Car car = new Car("Test Car", "testModel", 2);

        Car savedCar = carRepository.save(car);

        assertNotNull(savedCar.getId());
        assertEquals("Test Car", savedCar.getName());
        assertEquals("testModel", savedCar.getModel());
        assertEquals(2, savedCar.getDoors());
    }

    @Test
    void findById_ShouldReturnCar_WhenCarExists() {

        Car car = new Car("Test Car", "testModel", 2);
        Car savedCar = carRepository.save(car);

        Car foundCar = carRepository.findById(savedCar.getId());

        assertNotNull(foundCar);
        assertEquals(savedCar.getId(), foundCar.getId());
        assertEquals("Test Car", foundCar.getName());
        assertEquals("testModel", foundCar.getModel());
        assertEquals(2, foundCar.getDoors());
    }

    @Test
    void findById_ShouldReturnNull_WhenCarNotExists() {

        Car foundCar = carRepository.findById(10500L);

        assertNull(foundCar);
    }

    @Test
    void findAll_ShouldReturnAllCars() {

        Car car1 = carRepository.save(new Car("Car1", "model3", 4));
        Car car2 = carRepository.save(new Car("Car2", "modelY", 5));

        List<Car> cars = carRepository.findAll();

        assertEquals(2, cars.size());
        assertTrue(cars.stream().anyMatch(u -> u.getId().equals(car1.getId())));
        assertTrue(cars.stream().anyMatch(u -> u.getId().equals(car2.getId())));
    }

    @Test
    void update_ShouldModifyExistingCar() {

        Car car = carRepository.save(new Car("Lada", "Vesta", 0));
        car.setName("Lada");
        car.setModel("Iskra");
        car.setDoors(0);

        carRepository.update(car);
        Car updatedCar = carRepository.findById(car.getId());

        assertNotNull(updatedCar);
        assertEquals("Lada", updatedCar.getName());
        assertEquals("Iskra", updatedCar.getModel());
        assertEquals(0, updatedCar.getDoors());
    }

    @Test
    void delete_ShouldRemoveCar() {

        Car car = carRepository.save(new Car("Test", "old", 0));

        carRepository.delete(car.getId());
        Car deletedCar = carRepository.findById(car.getId());

        assertNull(deletedCar);
    }
}