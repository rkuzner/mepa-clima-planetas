package net.kuzner.app.climaplanetas;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RestServiceController {

	@Autowired
	private SistemaSolar sistemaSolar;

	@Autowired
	private Meteorologo meteorologo;

	Map<Integer, Pronostico> pronosticoPorDia = null;
	Prediccion ultimaPrediccion = null;

	@RequestMapping(path = "/prediccion", method = RequestMethod.GET)
	public Prediccion getPrediccion(@RequestParam(value = "dias", defaultValue = "3652") Integer cuantosDias) {
		if(Objects.isNull(this.pronosticoPorDia)) {
			this.pronosticoPorDia = new HashMap<Integer, Pronostico>();
		}
		if (this.pronosticoPorDia.isEmpty()) {
			Collection<Pronostico> pronosticoCollection = meteorologo.obtenerPronosticos(cuantosDias,
					sistemaSolar.getPlanetaA(), sistemaSolar.getPlanetaB(), sistemaSolar.getPlanetaC());
			for (Pronostico pronostico : pronosticoCollection) {
				this.pronosticoPorDia.put(pronostico.getDia(), pronostico);
			}
			this.ultimaPrediccion = this.meteorologo.predecirClima(this.pronosticoPorDia.values());
		}
		return this.ultimaPrediccion;
	}

	@RequestMapping(path = "/prediccion", method = RequestMethod.DELETE)
	public void deletePrediccion() {
		if (Objects.nonNull(this.pronosticoPorDia) && !this.pronosticoPorDia.isEmpty()) {
			this.pronosticoPorDia.clear();
		}
	}

	@RequestMapping(path = "/clima", method = RequestMethod.GET)
	public Pronostico getPronostico(@RequestParam(value = "dia", defaultValue = "0") Integer queDia) {
		Pronostico pronosticoDeseado = null;
		if (Objects.nonNull(this.pronosticoPorDia) && this.pronosticoPorDia.containsKey(queDia)) {
			pronosticoDeseado = this.pronosticoPorDia.get(queDia);
		} else {
			pronosticoDeseado = this.meteorologo.calcularClima(queDia, this.sistemaSolar.getPlanetaA(),
					this.sistemaSolar.getPlanetaB(), this.sistemaSolar.getPlanetaC());
		}
		return pronosticoDeseado;
	}
}
