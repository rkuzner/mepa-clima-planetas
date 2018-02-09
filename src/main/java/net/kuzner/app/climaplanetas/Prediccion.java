package net.kuzner.app.climaplanetas;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Representación de un predicción del clima para el sistema solar FBV
 * 
 * @author Raul Kuzner
 */
public class Prediccion {

	private int totalPeriodos = 0;

	@JsonProperty(value = "periodosPorClima")
	private final Map<String, Integer> periodosPorClimaMap;

	@JsonIgnore
	private double maximaPrecipitacion = 0;

	@JsonProperty(value = "diasConMaximaPrecipitacion")
	private final Collection<Integer> diaMaximaPrecipCollection;

	public Prediccion() {
		this.periodosPorClimaMap = new HashMap<String, Integer>();
		this.diaMaximaPrecipCollection = new ArrayList<Integer>();
	}

	public void considerar(Pronostico pronostico) {
		Objects.requireNonNull(pronostico, "falta el pronóstico");

		this.periodosPorClimaMap.merge(pronostico.getClima(), 1, Integer::sum);

		if (pronostico.getPrecipitacion() > this.maximaPrecipitacion) {
			this.diaMaximaPrecipCollection.clear();
			this.maximaPrecipitacion = pronostico.getPrecipitacion();
		}
		if (pronostico.getPrecipitacion() == this.maximaPrecipitacion) {
			this.diaMaximaPrecipCollection.add(pronostico.getDia());
		}

		this.totalPeriodos++;
	}

	/**
	 * Obtiene el total de períodos de esta Predicción
	 * 
	 * @return el total de períodos de esta Predicción
	 */
	public int getTotalPeriodos() {
		return this.totalPeriodos;
	}

	/**
	 * Obtiene la cantidad de períodos por clima de esta Predicción
	 * 
	 * @return la cantidad de períodos por clima de esta Predicción
	 */
	@JsonIgnore
	public Map<String, Integer> getPeriodosPorClima() {
		return this.periodosPorClimaMap;
	}

	/**
	 * Obtiene la lista de días que tuvieron máxima precipitación en esta Predicción
	 * 
	 * @return la lista de días que tuvieron máxima precipitación en esta Predicción
	 */
	@JsonIgnore
	public Collection<Integer> getDiasMaximaPrecip() {
		return this.diaMaximaPrecipCollection;
	}
}
