package net.kuzner.app.climaplanetas;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Representación de un pronóstico del clima para el sistema solar FBV
 * 
 * @author Raul Kuzner
 */
public class Pronostico {

	@JsonIgnore
	private final Logger logger = Logger.getLogger(this.getClass().getName());

	private final int dia;

	private final String clima;

	@JsonIgnore
	private Map<String, Double> anguloPorPlanetaMap;

	@JsonIgnore
	private Map<String, Double> distanciaEntrePlanetasMap;

	@JsonIgnore
	private double precipitacion;

	/**
	 * Constructor
	 * 
	 * @param dia
	 *            el día para este Pronóstico
	 * @param clima
	 *            el clima para este día
	 * 
	 * @throws NullPointerException
	 *             si {@code clima} es null
	 * @throws IllegalArgumentException
	 *             si {@code clima} es vacío
	 */
	public Pronostico(int dia, String clima) {
		this.dia = dia;
		this.clima = Objects.requireNonNull(clima, "falta el clima para este día");
		if (this.clima.isEmpty()) {
			throw new IllegalArgumentException("el clima debe informarse");
		}
		this.logger.fine("Pronóstico inicializado");
	}

	/**
	 * Obtiene el día de este Pronóstico
	 * 
	 * @return el día de este Pronóstico
	 */
	public int getDia() {
		return this.dia;
	}

	/**
	 * Obtiene el clima para este día
	 * 
	 * @return el clima para este día
	 */
	public String getClima() {
		return this.clima;
	}

	@JsonIgnore
	public Map<String, Double> getAnguloPorPlaneta() {
		return this.anguloPorPlanetaMap;
	}

	public void setAnguloPorPlaneta(String planeta, double anguloGrados) {
		if (Objects.isNull(this.anguloPorPlanetaMap)) {
			this.anguloPorPlanetaMap = new HashMap<String, Double>();
		}
		this.anguloPorPlanetaMap.put(planeta, anguloGrados);
		this.logger.fine("anguloPorPlaneta agregado");
	}

	@JsonIgnore
	public Map<String, Double> getDistanciaEntrePlanetas() {
		return this.distanciaEntrePlanetasMap;
	}

	public void putDistanciaEntrePlanetas(String planeta, double distancia) {
		if (Objects.isNull(this.distanciaEntrePlanetasMap)) {
			this.distanciaEntrePlanetasMap = new HashMap<String, Double>();
		}
		this.distanciaEntrePlanetasMap.put(planeta, distancia);
		this.logger.fine("distanciaEntrePlanetas agregado");
	}

	/**
	 * Obtiene el nivel de precipitación para este día
	 * 
	 * @return el nivel de precipitación para este día
	 */
	public double getPrecipitacion() {
		return precipitacion;
	}

	/**
	 * Establece el nivel de precipitación para este día
	 * 
	 * @param precipitacion
	 *            el nivel de precipitación para este día
	 */
	public void setPrecipitacion(double precipitacion) {
		if (precipitacion < 0) {
			throw new IllegalArgumentException("la precipitación debe ser positiva");
		}
		this.precipitacion = precipitacion;
	}

	@Override
	public String toString() {
		return String.format("Pronostico [dia=%s, clima=%s, precipitacion=%s]", this.dia, this.clima,
				this.precipitacion);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.clima == null) ? 0 : this.clima.hashCode());
		result = prime * result + this.dia;
		long temp;
		temp = Double.doubleToLongBits(this.precipitacion);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Pronostico other = (Pronostico) obj;
		if (this.clima == null) {
			if (other.clima != null) {
				return false;
			}
		} else if (!this.clima.equals(other.clima)) {
			return false;
		}
		if (this.dia != other.dia) {
			return false;
		}
		if (Double.doubleToLongBits(this.precipitacion) != Double.doubleToLongBits(other.precipitacion)) {
			return false;
		}
		return true;
	}

}
