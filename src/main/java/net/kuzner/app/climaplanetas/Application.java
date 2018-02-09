package net.kuzner.app.climaplanetas;

import java.util.Collection;
import java.util.Map;
import java.util.logging.Logger;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

	private final Logger logger = Logger.getLogger(this.getClass().getName());

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@SuppressWarnings("unused")
	private void calcular() {
		this.logger.fine("Inicio");

		Planeta planetaA = new Planeta("Ferengi", 500.0, -1.0);
		Planeta planetaB = new Planeta("Betasoide", 2000.0, -3.0);
		Planeta planetaC = new Planeta("Vulcano", 1000.0, 5.0);

		Meteorologo meteorologo = new Meteorologo();

		Collection<Pronostico> pronosticoCollection = meteorologo.obtenerPronosticos(3652, planetaA, planetaB,
				planetaC);
		Prediccion prediccion = meteorologo.predecirClima(pronosticoCollection);

		for (Map.Entry<String, Integer> periodosPorClimaEntry : prediccion.getPeriodosPorClima().entrySet()) {
			String clima = periodosPorClimaEntry.getKey();
			Integer periodos = periodosPorClimaEntry.getValue();
			this.logger.info(String.format("Habrá %d período(s) de %s", periodos, clima));
		}
		this.logger.info(String.format("El pico máximo de lluvia sucederá el(los) día(s): %s",
				prediccion.getDiasMaximaPrecip().toString()));

		this.logger.fine("Fin");
	}
}
