# clima-planetas

Aplicacion que permite hacer una prediccion de clima del sistema solar FBV


Para inicializar una prediccion por un período determinado, invocar:

	curl -X GET http://35.224.129.191/prediccion?dias=NNN

en caso que no se informe, el valor default para el parámetro dias es 3652 (diez años)


Para borrar una predicción vigente, invocar:

	curl -X DELETE http://35.224.129.191/prediccion


Para consultar el clima de un determinado día, invocar:

	curl -X GET http://35.224.129.191/clima?dia=NNN

en caso que no se informe, el valor default para el parámetro dia es 0 (cero: primer dia de la predicción vigente)