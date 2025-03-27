package com.aston.aston;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

//включаем поддержку Mockito в JUnit 5
@ExtendWith(MockitoExtension.class)
class CarServiceTest {

    //создаем mock-объект CarRepository
    @Mock
    private CarRepository carRepository;

    //создаем экземпляр CarService и внедряет в него мокированные зависимости
    @InjectMocks
    private CarService carService;

    // тестовые методы
    @Test
    void save_ShouldReturnSavedCar() {

        Car carToSave = new Car("Test Car", "Test model", 2);
        Car savedCar = new Car("Test Car", "Test model", 2);
        savedCar.setId(1L);

        when(carRepository.save(carToSave)).thenReturn(savedCar);

        Car result = carService.save(carToSave);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Test Car", result.getName());
        verify(carRepository, times(1)).save(carToSave);
    }

    @Test
    void findById_ShouldReturnCar_WhenCarExists() {

        Car car = new Car("Test Car", "Test model", 2);
        car.setId(1L);

        when(carRepository.findById(1L)).thenReturn(car);

        Car result = carService.findById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(carRepository, times(1)).findById(1L);
    }

    @Test
    void findById_ShouldReturnNull_WhenCarNotExists() {

        when(carRepository.findById(1L)).thenReturn(null);

        Car result = carService.findById(1L);

        assertNull(result);
        verify(carRepository, times(1)).findById(1L);
    }

    @Test
    void findAll_ShouldReturnAllCars() {

        Car car1 = new Car("Car 1", "model1", 2);
        car1.setId(1L);
        Car car2 = new Car("Car 2", "model2", 4);
        car2.setId(2L);
        List<Car> cars = Arrays.asList(car1, car2);

        when(carRepository.findAll()).thenReturn(cars);

        List<Car> result = carService.findAll();

        assertEquals(2, result.size());
        verify(carRepository, times(1)).findAll();
    }

    @Test
    void update_ShouldCallRepositoryUpdate() {

        Car carToUpdate = new Car("Updated Car", "newModel", 5);
        carToUpdate.setId(1L);

        carService.update(carToUpdate);

        verify(carRepository, times(1)).update(carToUpdate);
    }

    @Test
    void delete_ShouldCallRepositoryDelete() {

        carService.delete(1L);

        verify(carRepository, times(1)).delete(1L);
    }

    @Test
    void existsByModel_ShouldReturnTrue_WhenModelExists() {

        Car car = new Car("Test Car", "model5", 5);
        car.setId(1L);
        List<Car> cars = List.of(car);

        when(carRepository.findAll()).thenReturn(cars);

        boolean result = carService.existsByModel("model5");

        assertTrue(result);
    }

    @Test
    void existsByModel_ShouldReturnFalse_WhenModelNotExists() {

        when(carRepository.findAll()).thenReturn(List.of());

        boolean result = carService.existsByModel("fakeModel");

        assertFalse(result);
    }
}