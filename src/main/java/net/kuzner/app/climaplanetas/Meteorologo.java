package net.kuzner.app.climaplanetas;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Permite calcular el clima en el sistema solar FBV
 *
 * @author Raul Kuzner
 */
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class Meteorologo {

	private final Logger logger = Logger.getLogger(this.getClass().getName());

	public Meteorologo() {
		// Nada para hacer
	}

	public Collection<Pronostico> obtenerPronosticos(int cuantosDias, Planeta planetaA, Planeta planetaB,
			Planeta planetaC) {
		Objects.requireNonNull(planetaA, "falta el planetaA");
		Objects.requireNonNull(planetaB, "falta el planetaB");
		Objects.requireNonNull(planetaC, "falta el planetaC");
		if (cuantosDias <= 0) {
			throw new IllegalArgumentException("la cantidad de días no puede ser cero o negativo");
		}
		this.logger.info("Calculando pronósticos del clima");

		Collection<Pronostico> pronosticoCollection = new ArrayList<Pronostico>(cuantosDias);

		for (int queDia = 0; queDia < cuantosDias; queDia++) {
			pronosticoCollection.add(calcularClima(queDia, planetaA, planetaB, planetaC));
		}

		if (this.logger.isLoggable(Level.INFO)) {
			this.logger.info(String.format("%d pronósticos del clima calculados", pronosticoCollection.size()));
		}
		return pronosticoCollection;
	}

	public Prediccion predecirClima(Collection<Pronostico> pronosticoCollection) {
		Objects.requireNonNull(pronosticoCollection, "falta la colección de pronósticos");
		if (pronosticoCollection.isEmpty()) {
			throw new IllegalArgumentException("no se puede predecir sin pronósticos");
		}

		this.logger.info("Considerando pronósticos del clima para realizar predicción Sistema Solar");

		Prediccion prediccion = new Prediccion();

		for (Pronostico pronostico : pronosticoCollection) {
			prediccion.considerar(pronostico);
		}

		this.logger.info("Terminé la predicción");
		return prediccion;
	}

	/**
	 * Calcula el clima para un día determinado
	 * 
	 * @param queDia
	 *            el día para el que se desea calcular el clima
	 * @param planetaA
	 *            uno de los planetas del sistema solar FBV
	 * @param planetaB
	 *            otro de los planetas del sistema solar FBV
	 * @param planetaC
	 *            otro más de los planetas del sistema solar FBV
	 * @return la predicción para un día determinado
	 * 
	 * @throws NullPointerException
	 *             si alguno de los parámetros es null
	 */
	public Pronostico calcularClima(int queDia, Planeta planetaA, Planeta planetaB, Planeta planetaC) {
		Objects.requireNonNull(planetaA, "falta el planetaA");
		Objects.requireNonNull(planetaB, "falta el planetaB");
		Objects.requireNonNull(planetaC, "falta el planetaC");

		// me fijo si es sequía
		Pronostico pronostico = evaluarPorSequia(queDia, planetaA, planetaB, planetaC);

		// no es sequía, me fijo si es lluvia
		if (Objects.isNull(pronostico)) {
			pronostico = evaluarPorLluvia(queDia, planetaA, planetaB, planetaC);
		}

		// no es sequía ni lluvia, me fijo si es presion y temperatura ideal
		if (Objects.isNull(pronostico)) {
			pronostico = evaluarPorTemperatura(queDia, planetaA, planetaB, planetaC);
		}

		// no es ninguno de los anteriores casos
		if (Objects.isNull(pronostico)) {
			pronostico = new Pronostico(queDia, "Estable");
		}

		if (this.logger.isLoggable(Level.INFO)) {
			this.logger.info(String.format("Pronóstico del clima calculado: %s", pronostico));
		}
		return pronostico;
	}

	private Pronostico evaluarPorSequia(int queDia, Planeta planetaA, Planeta planetaB, Planeta planetaC) {
		Objects.requireNonNull(planetaA, "falta el planetaA");
		Objects.requireNonNull(planetaB, "falta el planetaB");
		Objects.requireNonNull(planetaC, "falta el planetaC");

		Pronostico pronostico = null;
		double anguloGradosA = planetaA.calcularAngulo(queDia);
		double anguloGradosB = planetaB.calcularAngulo(queDia);
		double anguloGradosC = planetaC.calcularAngulo(queDia);

		// me permite determinar si los ángulos estan alineados
		double preliminarA = Math.abs(anguloGradosA % 180);
		double preliminarB = Math.abs(anguloGradosB % 180);
		double preliminarC = Math.abs(anguloGradosC % 180);

		if ((preliminarA == preliminarB) && (preliminarB == preliminarC)) {
			pronostico = new Pronostico(queDia, "Sequía");
			pronostico.setAnguloPorPlaneta(planetaA.getNombre(), anguloGradosA);
			pronostico.setAnguloPorPlaneta(planetaB.getNombre(), anguloGradosB);
			pronostico.setAnguloPorPlaneta(planetaC.getNombre(), anguloGradosC);

			if (this.logger.isLoggable(Level.FINE)) {
				this.logger.fine(String.format("día: %4d, pron: %7.7s, ang: %s", queDia, pronostico.getClima(),
						pronostico.getAnguloPorPlaneta()));
			}
		}

		return pronostico;
	}

	private Pronostico evaluarPorLluvia(int queDia, Planeta planetaA, Planeta planetaB, Planeta planetaC) {
		Objects.requireNonNull(planetaA, "falta el planetaA");
		Objects.requireNonNull(planetaB, "falta el planetaB");
		Objects.requireNonNull(planetaC, "falta el planetaC");

		Pronostico pronostico = null;
		Point2D.Double coordenadas0 = new Point2D.Double(0, 0);
		Point2D.Double coordenadasA = planetaA.calcularCoordenadas(planetaA.calcularAngulo(queDia));
		Point2D.Double coordenadasB = planetaB.calcularCoordenadas(planetaB.calcularAngulo(queDia));
		Point2D.Double coordenadasC = planetaC.calcularCoordenadas(planetaC.calcularAngulo(queDia));
		Double distanciaAB = null;
		Double distanciaBC = null;
		Double distanciaCA = null;
		Double perimetro = null;

		// calculo la orientación de los cuatro triángulos formados por:
		// - los tres planetas entre sí
		// - cada par de planetas y la estrella

		int preliminarABC = orientacionTriangulo(coordenadasA, coordenadasB, coordenadasC);
		int preliminarAB0 = orientacionTriangulo(coordenadasA, coordenadasB, coordenadas0);
		int preliminarBC0 = orientacionTriangulo(coordenadasB, coordenadasC, coordenadas0);
		int preliminarCA0 = orientacionTriangulo(coordenadasC, coordenadasA, coordenadas0);

		if (Math.abs(preliminarABC + preliminarAB0 + preliminarBC0 + preliminarCA0) == 4) {
			// si el signo de las cuatro orientaciones es el mismo,
			// entonces la estrella esta dentro del triángulo formado por los tres planetas.
			pronostico = new Pronostico(queDia, "Lluvia");

			// ademas ahora calculo el perimetro y lo guardo como representacion para
			// determinar las precipitaciones maximas
			distanciaAB = coordenadasA.distance(coordenadasB);
			distanciaBC = coordenadasB.distance(coordenadasC);
			distanciaCA = coordenadasC.distance(coordenadasA);
			perimetro = distanciaAB + distanciaBC + distanciaCA;

			pronostico.putDistanciaEntrePlanetas("AB", distanciaAB);
			pronostico.putDistanciaEntrePlanetas("BC", distanciaBC);
			pronostico.putDistanciaEntrePlanetas("CA", distanciaCA);
			pronostico.setPrecipitacion(perimetro);

			if (this.logger.isLoggable(Level.FINE)) {
				this.logger.fine(String.format("día: %4d, pron: %7.7s, ang: %s", queDia, pronostico.getClima(),
						pronostico.getDistanciaEntrePlanetas()));
			}
		}

		return pronostico;
	}

	private int orientacionTriangulo(Point2D.Double coordenadasA, Point2D.Double coordenadasB,
			Point2D.Double coordenadasC) {
		Objects.requireNonNull(coordenadasA, "falta coordenadasA");
		Objects.requireNonNull(coordenadasB, "falta coordenadasB");
		Objects.requireNonNull(coordenadasC, "falta coordenadasC");

		int orientacion = 0;
		// calculo la orientación de los triángulos con la fórmula:
		// (A.x - C.x) * (B.y - C.y) - (A.y - C.y) * (B.x - C.x)

		double ax = coordenadasA.getX();
		double ay = coordenadasA.getY();
		double bx = coordenadasB.getX();
		double by = coordenadasB.getY();
		double cx = coordenadasC.getX();
		double cy = coordenadasC.getY();

		if (((ax - cx) * (by - cy) - (ay - cy) * (bx - cx)) >= 0) {
			orientacion = 1;
		} else {
			orientacion = -1;
		}

		return orientacion;
	}

	private Pronostico evaluarPorTemperatura(int queDia, Planeta planetaA, Planeta planetaB, Planeta planetaC) {
		Objects.requireNonNull(planetaA, "falta el planetaA");
		Objects.requireNonNull(planetaB, "falta el planetaB");
		Objects.requireNonNull(planetaC, "falta el planetaC");

		Pronostico pronostico = null;
		Point2D.Double coordenadasA = planetaA.calcularCoordenadas(planetaA.calcularAngulo(queDia));
		Point2D.Double coordenadasB = planetaB.calcularCoordenadas(planetaB.calcularAngulo(queDia));
		Point2D.Double coordenadasC = planetaC.calcularCoordenadas(planetaC.calcularAngulo(queDia));
		Double distanciaAB = coordenadasA.distance(coordenadasB);
		Double distanciaBC = coordenadasB.distance(coordenadasC);
		Double distanciaCA = coordenadasC.distance(coordenadasA);
		Double perimetro = null;
		double margen = 1; // en kilometros
		Double maximaDistancia = null;

		// Los tres planetas estarán alineados entre sí, sin estar alineados a
		// la estrella cuando el perímetro del triángulo que dibujan dividido
		// en dos sea igual al lado más largo del triángulo:
		// (ab+bc+ca) / 2 == max(ab, bc, ba)
		// Debido a que los períodos son diarios, es posible que la alineación
		// se produzca entre períodos. Por esta razón, se incluye un margen para
		// considerar que están alineados.

		perimetro = distanciaAB + distanciaBC + distanciaCA;
		maximaDistancia = Math.max(Math.max(distanciaAB, distanciaBC), distanciaCA);

		if (((perimetro / 2) - maximaDistancia) < margen) {
			pronostico = new Pronostico(queDia, "Presión y temperatura ideal");

			pronostico.putDistanciaEntrePlanetas("AB", distanciaAB);
			pronostico.putDistanciaEntrePlanetas("BC", distanciaBC);
			pronostico.putDistanciaEntrePlanetas("CA", distanciaCA);

			if (this.logger.isLoggable(Level.FINE)) {
				this.logger.fine(String.format("día: %4d, pron: %7.7s, ang: %s", queDia, pronostico.getClima(),
						pronostico.getDistanciaEntrePlanetas()));
			}
		}

		return pronostico;
	}
}
