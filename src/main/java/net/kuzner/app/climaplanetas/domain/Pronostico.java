package net.kuzner.app.mepachallenge.domain;

import java.util.Objects;

/**
 * Representación de un pronóstico del clima para el sistema solar FBV
 * 
 * @author Raul Kuzner
 */
public class Pronostico {

	private final int dia;
	private final String clima;
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
