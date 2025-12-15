package edu.espe.sensoresbiblioteca.controller;

import edu.espe.sensoresbiblioteca.model.SensorReading;
import edu.espe.sensoresbiblioteca.service.SensorService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.Instant;

@RestController
@RequestMapping("/api/sensors")
public class SensorController {
    private final SensorService service;

    public SensorController(SensorService service) {
        this.service = service;
    }

    @PostMapping
    public Mono<SensorReading> saveReading(@RequestBody SensorReading sensorReading) {
        if (sensorReading.getTimestamp() == null){
            sensorReading.setTimestamp(Instant.now());
        }
        return service.save(sensorReading);
    }

    //Devolver todas las lecturas almacenadas
    @GetMapping
    public Flux<SensorReading> getAllReadings(){
        return service.getAll();
    }

    //Streaming de lecturas en tiempo real usando Server-Sent events
    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<SensorReading> streamReadings(){
        return  service.streamAll();
    }

    //Devolver el promedio de temperaturas calculadas de forma asíncrona
    @GetMapping("/average")
    public Mono<Double> calculateAverage() {
        return service.calculateAverageAsync();
    }

    // Nuevo endpoint: promedio de temperatura por sensor con retardo asíncrono de 1 segundo
    @GetMapping("/{id}/average")
    public Mono<Double> averageBySensor(@PathVariable("id") String sensorId) {
        return service.getAll()
                .filter(r -> sensorId.equals(r.getId()))
                .map(SensorReading::getTemperature)
                .collectList()
                .flatMap(list -> {
                    if (list.isEmpty()) {
                        // Retornar NaN o 0.0 según prefieras; aquí usamos NaN para indicar no hay datos
                        return Mono.delay(Duration.ofSeconds(1)).thenReturn(Double.NaN);
                    }
                    double avg = list.stream().mapToDouble(Double::doubleValue).average().orElse(Double.NaN);
                    // Simular retardo asíncrono de 1 segundo sin bloquear el event-loop
                    return Mono.delay(Duration.ofSeconds(1)).thenReturn(avg);
                });
    }

    // Nuevo endpoint: genera lecturas automáticas cada segundo durante 10 segundos y las emite al servicio
    @GetMapping("/generate")
    public Mono<String> generateReadings() {
        // Genera una lectura por segundo durante 10 segundos
        return Flux.interval(Duration.ofSeconds(1))
                .take(10)
                .map(i -> {
                    SensorReading r = new SensorReading(20.0 + Math.random() * 10.0, "sim-" + (i % 3), Instant.now());
                    return r;
                })
                .flatMap(service::save)
                .then(Mono.just("Generación terminada"));
    }
}
