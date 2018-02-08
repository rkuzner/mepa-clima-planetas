package net.kuzner.app.climaplanetas.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Representación de un predicción del clima para el sistema solar FBV
 * 
 * @author Raul Kuzner
 */
public class Prediccion {

	private int totalPeriodos = 0;
	private final Map<String, Integer> periodosPorClima;
	private double maximaPrecipitacion = 0;
	private Collection<Integer> diaMaximaPrecipCollection;

	public Prediccion() {
		this.periodosPorClima = new HashMap<String, Integer>();
		this.diaMaximaPrecipCollection = new ArrayList<>();
	}

	public void considerar(Pronostico pronostico) {
		Objects.requireNonNull(pronostico, "falta el pronóstico");

		int cantidadPeriodos = this.periodosPorClima.getOrDefault(pronostico.getClima(), 0);
		this.periodosPorClima.put(pronostico.getClima(), ++cantidadPeriodos);
		// this.periodosPorClima.merge(pronostico.getClima(), 1, Integer::sum);

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
	public Map<String, Integer> getPeriodosPorClima() {
		return this.periodosPorClima;
	}

	/**
	 * Obtiene la lista de días que tuvieron máxima precipitación en esta Predicción
	 * 
	 * @return la lista de días que tuvieron máxima precipitación en esta Predicción
	 */
	public Collection<Integer> getDiasMaximaPrecip() {
		return this.diaMaximaPrecipCollection;
	}
}
