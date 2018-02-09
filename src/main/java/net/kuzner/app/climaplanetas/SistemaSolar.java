package net.kuzner.app.climaplanetas;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class SistemaSolar {

	private final Logger logger = Logger.getLogger(this.getClass().getName());

	private final String nombre = "FBV";
	private final Planeta planetaA;
	private final Planeta planetaB;
	private final Planeta planetaC;

	public SistemaSolar() {
		this.logger.info("Inicializando Sistema Solar");

		this.planetaA = new Planeta("Ferengi", 500.0, -1.0);
		this.planetaB = new Planeta("Betasoide", 2000.0, -3.0);
		this.planetaC = new Planeta("Vulcano", 1000.0, 5.0);

		if (this.logger.isLoggable(Level.INFO)) {
			this.logger.info(String.format("Sistema Solar %s inicializado", this.nombre));
		}
	}

	public Planeta getPlanetaA() {
		return planetaA;
	}

	public Planeta getPlanetaB() {
		return planetaB;
	}

	public Planeta getPlanetaC() {
		return planetaC;
	}

}
