package net.kuzner.app.mepachallenge.domain;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * Permite calcular el clima en el sistema solar FBV
 *
 * @author Raul Kuzner
 */
public class Meteorologo {

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

		Collection<Pronostico> pronosticoCollection = new ArrayList<Pronostico>(cuantosDias);

		for (int queDia = 0; queDia < cuantosDias; queDia++) {
			pronosticoCollection.add(calcularClima(queDia, planetaA, planetaB, planetaC));
		}

		return pronosticoCollection;
	}

	public Prediccion predecirClima(Collection<Pronostico> pronosticoCollection) {
		Objects.requireNonNull(pronosticoCollection, "falta la colección de pronósticos");
		if (pronosticoCollection.isEmpty()) {
			throw new IllegalArgumentException("no se puede predecir sin pronósticos");
		}

		Prediccion prediccion = new Prediccion();

		for (Pronostico pronostico : pronosticoCollection) {
			prediccion.considerar(pronostico);
		}
		
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
	private Pronostico calcularClima(int queDia, Planeta planetaA, Planeta planetaB, Planeta planetaC) {
		Objects.requireNonNull(planetaA, "falta el planetaA");
		Objects.requireNonNull(planetaB, "falta el planetaB");
		Objects.requireNonNull(planetaC, "falta el planetaC");

		// me fijo si es sequía
		double anguloGradosA = planetaA.calcularAngulo(queDia);
		double anguloGradosB = planetaB.calcularAngulo(queDia);
		double anguloGradosC = planetaC.calcularAngulo(queDia);
		Pronostico pronostico = evaluarPorSequia(queDia, anguloGradosA, anguloGradosB, anguloGradosC);

		// no es sequía, me fijo si es lluvia
		Point2D.Double coordenadasA = null;
		Point2D.Double coordenadasB = null;
		Point2D.Double coordenadasC = null;
		Double distanciaAB = null;
		Double distanciaBC = null;
		Double distanciaCA = null;
		if (Objects.isNull(pronostico)) {
			coordenadasA = planetaA.calcularCoordenadas(anguloGradosA);
			coordenadasB = planetaB.calcularCoordenadas(anguloGradosB);
			coordenadasC = planetaC.calcularCoordenadas(anguloGradosC);
			distanciaAB = coordenadasA.distance(coordenadasB);
			distanciaBC = coordenadasB.distance(coordenadasC);
			distanciaCA = coordenadasC.distance(coordenadasA);

			pronostico = evaluarPorLluvia(queDia, coordenadasA, coordenadasB, coordenadasC);

			if (Objects.nonNull(pronostico)) {
				double perimetro = distanciaAB + distanciaBC + distanciaCA;
				pronostico.setPrecipitacion(perimetro);
			}
		}

		// no es sequía ni lluvia, me fijo si es presion y temperatura ideal
		if (Objects.isNull(pronostico)) {
			pronostico = evaluarPorTemperatura(queDia, distanciaAB, distanciaBC, distanciaCA);
		}

		// no es ninguno de los anteriores casos
		if (Objects.isNull(pronostico)) {
			pronostico = new Pronostico(queDia, "Estable");
		}

		return pronostico;
	}

	private Pronostico evaluarPorSequia(int queDia, double anguloGradosA, double anguloGradosB, double anguloGradosC) {
		Objects.requireNonNull(anguloGradosA, "falta el anguloGradosA");
		Objects.requireNonNull(anguloGradosB, "falta el anguloGradosB");
		Objects.requireNonNull(anguloGradosC, "falta el anguloGradosC");

		Pronostico pronostico = null;

		// me permite determinar si los ángulos estan alineados
		double preliminarA = anguloGradosA % 180;
		double preliminarB = anguloGradosB % 180;
		double preliminarC = anguloGradosC % 180;

		if ((preliminarA == preliminarB) && (preliminarB == preliminarC)) {
			pronostico = new Pronostico(queDia, "Sequía");
		}

		return pronostico;
	}

	private Pronostico evaluarPorLluvia(int queDia, Point2D.Double coordenadasA, Point2D.Double coordenadasB,
			Point2D.Double coordenadasC) {
		Objects.requireNonNull(coordenadasA, "falta coordenadasA");
		Objects.requireNonNull(coordenadasB, "falta coordenadasB");
		Objects.requireNonNull(coordenadasC, "falta coordenadasC");

		Pronostico pronostico = null;

		Point2D.Double coordenadas0 = new Point2D.Double(0, 0);

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

	private Pronostico evaluarPorTemperatura(int queDia, double distanciaAB, double distanciaBC, double distanciaCA) {
		Objects.requireNonNull(distanciaAB, "falta distanciaAB");
		Objects.requireNonNull(distanciaBC, "falta distanciaBC");
		Objects.requireNonNull(distanciaCA, "falta distanciaCA");

		Pronostico prediccion = null;

		// Los tres planetas estarán alineados entre sí, sin estar alineados a
		// la estrella cuando el perímetro del triángulo que dibujan dividido
		// en dos sea igual al lado más largo del triángulo:
		// (ab+bc+ca) / 2 == max(ab, bc, ba)
		// Debido a que los períodos son diarios, es posible que la alineación
		// se produzca entre períodos. Por esta razón, se incluye un margen para
		// considerar que están alineados.

		double margen = 1;
		double perimetro = distanciaAB + distanciaBC + distanciaCA;
		double maximaDistancia = Math.max(Math.max(distanciaAB, distanciaBC), distanciaCA);

		if (((perimetro / 2) - maximaDistancia) < margen) {
			prediccion = new Pronostico(queDia, "Presión y temperatura ideal");
		}

		return prediccion;
	}
}
