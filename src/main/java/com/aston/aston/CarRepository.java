package com.aston.aston;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CarRepository {
    private final Connection connection;

    public CarRepository(Connection connection) {    // connection для создания соединения
        this.connection = connection;
        createTableIfNotExists();
    }
    // Создает таблицу при инициализации (если она не существует)
    private void createTableIfNotExists() {
        String sql = "CREATE TABLE IF NOT EXISTS cars (" +
                "id BIGINT AUTO_INCREMENT PRIMARY KEY, " +
                "name VARCHAR(255) NOT NULL, " +
                "model VARCHAR(255) NOT NULL, " +
                "doors INT)";

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            throw new RuntimeException("creation error", e);  // выбрасываем ошибку если таблица не будет создана
        }
    }

    /**
     *
     * @param car
     * @return Car
     * метод отвечает за сохранения объекта в базу данных
     * используем PreparedStatement что бы сделать запрос с параметрами
     */
    public Car save(Car car) {
        String sql = "INSERT INTO cars (name, model, doors) VALUES (?, ?, ?)";

        try (PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, car.getName());
            pstmt.setString(2, car.getModel());
            pstmt.setInt(3, car.getDoors());
            pstmt.executeUpdate();

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    car.setId(generatedKeys.getLong(1));
                }
            }
            return car;
        } catch (SQLException e) {
            throw new RuntimeException("save error", e); // выбрасываем ошибку если Car не будет сохранен
        }
    }

    /**
     *
     * @param id - id объекта Car
     * @return возвращает объект по номеру id
     */
    public Car findById(Long id) {
        String sql = "SELECT * FROM cars WHERE id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setLong(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next() ? mapRowToCar(rs) : null;
            }
        } catch (SQLException e) {
            throw new RuntimeException("search error", e); // выбрасываем ошибку если Car не будет найден
        }
    }

    /**
     *
     * @return метод должен вернуть все объекты таблицы Cars
     */
    public List<Car> findAll() {
        List<Car> cars = new ArrayList<>();
        String sql = "SELECT * FROM cars";

        try (Statement stmt = connection.createStatement();
             ResultSet resultSet = stmt.executeQuery(sql)) {

            while (resultSet.next()) {
                cars.add(mapRowToCar(resultSet));
            }
            return cars;
        } catch (SQLException e) {
            throw new RuntimeException("Could not find all", e);// выбрасываем ошибку
        }
    }

    /**
     *
     * @param car
     * метод изменяет параметры объекта
     */
    public void update(Car car) {
        String sql = "UPDATE cars SET name = ?, model = ?, doors = ? WHERE id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, car.getName());
            pstmt.setString(2, car.getModel());
            pstmt.setInt(3, car.getDoors());
            pstmt.setLong(4, car.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update", e); // выбрасываем ошибку в случае если обновление не произошло
        }
    }

    /**
     *
     * @param id - id объекта
     * метод удаляет объект по id
     */
    public void delete(Long id) {
        String sql = "DELETE FROM cars WHERE id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setLong(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete car", e); // выбрасываем ошибку если не удалось обновить
        }
    }

    /**
     *
     * @param rs
     * @return
     * @throws SQLException
     * Метод создает новый объект Car и затем используется в других методах для упрощения
     */
    private Car mapRowToCar(ResultSet rs) throws SQLException {
        Car car = new Car();
        car.setId(rs.getLong("id"));
        car.setName(rs.getString("name"));
        car.setModel(rs.getString("model"));
        car.setDoors(rs.getInt("doors"));
        return car;
    }

}
