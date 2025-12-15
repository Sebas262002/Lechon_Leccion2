package edu.espe.sensoresbiblioteca.repository;

import edu.espe.sensoresbiblioteca.model.SensorReading;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class SensorReadingRepository {
    private final List<SensorReading> storage = new CopyOnWriteArrayList<>();



    private final Sinks.Many<SensorReading> sink = Sinks.many().unicast().onBackpressureBuffer();

    //Guarda una lectura y la almacena al Strean
    public void save(SensorReading reading) {
        storage.add(reading);
        sink.tryEmitNext(reading);
    }

    //Devuelve todas las lecturas como un Flux
    public Flux<SensorReading> findAll() {
        return Flux.fromIterable(storage);
    }

    //Devuelve un stream de lectura(cada nueva lectura se va a emitir)
    public Flux<SensorReading> streamAll() {
        return sink.asFlux();
    }

}
