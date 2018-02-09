# clima-planetas

Aplicación que permite hacer una predicción de clima del sistema solar FBV


Para inicializar una predicción por un período determinado, invocar:

	curl -X GET http://35.224.129.191/prediccion?dias=NNN

en caso que no se informe, el valor default para el parámetro 'dias' es 3652 (diez años)


Para borrar una predicción vigente, invocar:

	curl -X DELETE http://35.224.129.191/prediccion


Para consultar el clima de un determinado día, invocar:

	curl -X GET http://35.224.129.191/clima?dia=NNN

en caso que no se informe, el valor default para el parámetro 'dia' es 0 (cero: primer día de la predicción vigente)
