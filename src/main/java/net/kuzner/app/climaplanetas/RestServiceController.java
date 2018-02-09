package net.kuzner.app.climaplanetas;

import java.util.Collection;
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

	Collection<Pronostico> pronosticoCollection = null;

	@RequestMapping(path = "/prediccion", method = RequestMethod.GET)
	public Prediccion getPrediccion(@RequestParam(value = "dias", defaultValue = "3652") Integer cuantosDias) {
		if (Objects.isNull(this.pronosticoCollection) || this.pronosticoCollection.isEmpty()) {
			this.pronosticoCollection = meteorologo.obtenerPronosticos(cuantosDias, sistemaSolar.getPlanetaA(),
					sistemaSolar.getPlanetaB(), sistemaSolar.getPlanetaC());
		}
		return this.meteorologo.predecirClima(this.pronosticoCollection);
	}

	@RequestMapping(path = "/prediccion", method = RequestMethod.DELETE)
	public void deletePrediccion() {
		if (Objects.nonNull(this.pronosticoCollection) && !this.pronosticoCollection.isEmpty()) {
			this.pronosticoCollection.clear();
		}
	}

	@RequestMapping(path = "/clima", method = RequestMethod.GET)
	public Pronostico getPronostico(@RequestParam(value = "dia", defaultValue = "0") Integer queDia) {
		return this.meteorologo.calcularClima(queDia, this.sistemaSolar.getPlanetaA(), this.sistemaSolar.getPlanetaB(),
				this.sistemaSolar.getPlanetaC());
	}
}
