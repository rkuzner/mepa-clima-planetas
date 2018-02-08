/**
 * 
 */
package net.kuzner.app.climaplanetas.domain;

import java.awt.geom.Point2D;
import java.util.Objects;

/**
 * Representación de un planeta del sistema solar FBV
 * 
 * @author Raul Kuzner
 */
public class Planeta {

	private final String nombre;
	private final double radioOrbita;
	private final double velocidadAngular;

	/**
	 * Constructor
	 * 
	 * @param nombre
	 *            nombre del planeta
	 * @param radioOrbita
	 *            distancia desde el planeta hasta la estrella del sistema solar [en
	 *            kilometros]
	 * @param velocidadAngular
	 *            velocidad de desplazamiento del planeta en su orbita [en grados
	 *            por día] (positivo: sentido horario, negativo: sentido
	 *            antihorario)
	 * 
	 * @throws NullPointerException
	 *             si alguno de los parámetros es null
	 * @throws IllegalArgumentException
	 *             si {@code radioOrbita} es cero o negativo
	 * @throws IllegalArgumentException
	 *             si {@code velocidadAngular} es cero
	 */
	public Planeta(String nombre, Double radioOrbita, Double velocidadAngular) {
		this.nombre = Objects.requireNonNull(nombre, "falta el nombre");
		this.radioOrbita = Objects.requireNonNull(radioOrbita, "falta el radio de la órbita");
		this.velocidadAngular = Objects.requireNonNull(velocidadAngular, "falta la velocidad angular");

		if (this.radioOrbita <= 0) {
			throw new IllegalArgumentException("el radio no puede ser cero o negativo");
		}
		if (this.velocidadAngular == 0) {
			throw new IllegalArgumentException("la velocidad angular no puede ser cero");
		}
	}

	/**
	 * Obtiene el nombre de este Planeta
	 * 
	 * @return el nombre de este Planeta
	 */
	public String getNombre() {
		return this.nombre;
	}

	/**
	 * Obtiene el radio de la órbita de este Planeta
	 * 
	 * @return el radio de la órbita de este Planeta
	 */
	public double getRadioOrbita() {
		return this.radioOrbita;
	}

	/**
	 * Obtiene la velocidad angular de rotación de este Planeta
	 * 
	 * @return la velocidad angular de rotación de este Planeta
	 */
	public double getVelocidadAngular() {
		return this.velocidadAngular;
	}

	/**
	 * Calcula el ángulo de este planeta para un día específico
	 * 
	 * @param queDia
	 *            el dia para el cual se desea calcular el angulo de este Planeta
	 * @return el ángulo de este planeta para un día específico
	 */
	public double calcularAngulo(int queDia) {
		return (this.getVelocidadAngular() * queDia) % 360;
	}

	/**
	 * Calcula las coordenadas de este Planeta para un día específico
	 * 
	 * @param queDia
	 *            el día para el cual se desea calcular las coordenadas de este
	 *            Planeta
	 * 
	 * @return las coordenadas de este Planeta para el día específico
	 */
	public Point2D.Double calcularCoordenadas(int queDia) {
		return this.calcularCoordenadas(this.calcularAngulo(queDia));
	}

	/**
	 * Calcula las coordenadas de este Planeta para un ángulo específico
	 * 
	 * @param anguloGrados
	 *            el ángulo para el cual se desea calcular las coordenadas de este
	 *            Planeta
	 * 
	 * @return las coordenadas de este Planeta para el ángulo específico
	 */
	public Point2D.Double calcularCoordenadas(double anguloGrados) {
		double anguloRadianes = Math.toRadians(anguloGrados);
		double nuevoX = this.getRadioOrbita() * Math.cos(anguloRadianes);
		double nuevoY = this.getRadioOrbita() * Math.sin(anguloRadianes);

		return new Point2D.Double(nuevoX, nuevoY);
	}

	@Override
	public String toString() {
		return String.format("Planeta [%s, orbita=%s km, velocidad=%s grados por día]", nombre, radioOrbita,
				velocidadAngular);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.nombre == null) ? 0 : this.nombre.hashCode());
		long temp;
		temp = Double.doubleToLongBits(this.radioOrbita);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(this.velocidadAngular);
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
		Planeta other = (Planeta) obj;
		if (this.nombre == null) {
			if (other.nombre != null) {
				return false;
			}
		} else if (!this.nombre.equals(other.nombre)) {
			return false;
		}
		if (Double.doubleToLongBits(this.radioOrbita) != Double.doubleToLongBits(other.radioOrbita)) {
			return false;
		}
		if (Double.doubleToLongBits(this.velocidadAngular) != Double.doubleToLongBits(other.velocidadAngular)) {
			return false;
		}
		return true;
	}

}
