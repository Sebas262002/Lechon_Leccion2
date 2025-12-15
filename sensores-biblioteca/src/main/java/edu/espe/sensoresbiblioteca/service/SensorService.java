package edu.espe.sensoresbiblioteca.service;



import edu.espe.sensoresbiblioteca.model.SensorReading;
import edu.espe.sensoresbiblioteca.repository.SensorReadingRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.List;

@Service
public class SensorService {
    private final SensorReadingRepository repository;

    public SensorService(SensorReadingRepository repository) {
        this.repository = repository;
    }

    //guardar una lectura y devuelve un mono con la lectura guardada
    //mono sirve para una sola comunicacion
    public Mono<SensorReading> save (SensorReading sensorReading) {
        repository.save(sensorReading);
        return Mono.just(sensorReading);
    }

    //devuelve todas las lecturas como un FLUX
    public Flux<SensorReading> getAll() {
        return repository.findAll();
    }

    //devuelve el stream en tiempo real de las lecturas
    public Flux<SensorReading> streamAll() {
        return repository.streamAll();
    }

// Calcular el promedio de temperaturas de forma asíncrona
    public Mono<Double> calculateAverageAsync() {
        return repository.findAll()
                .map(SensorReading::getTemperature)
                .collectList()
                .flatMap(list ->
                        Mono.fromCallable(() -> {
                            // Simular operación costosa sin bloquear (sleep dentro del Callable)
                            Thread.sleep(3000);

                            // Calcular promedio
                            return list.stream()
                                    .mapToDouble(Double::doubleValue)
                                    .average()
                                    .orElse(Double.NaN);
                        })
                )
                .subscribeOn(Schedulers.boundedElastic()); // Permite el uso de sleep sin bloquear el hilo reactor
    }


}

