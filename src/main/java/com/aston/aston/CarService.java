package com.aston.aston;
import java.util.List;

public class CarService {
    private final CarRepository carRepository;

    public CarService(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    public Car save(Car car) {
        return carRepository.save(car);
    }

    public Car findById(Long id) {
        return carRepository.findById(id);
    }

    public List<Car> findAll() {
        return carRepository.findAll();
    }

    public void update(Car car) {
        carRepository.update(car);
    }

    public void delete(Long id) {
        carRepository.delete(id);
    }

    // Дополнительные бизнес-методы
    public boolean existsByModel(String model) {
        return carRepository.findAll().stream()
                .anyMatch(car -> car.getModel().equalsIgnoreCase(model));
    }
}


