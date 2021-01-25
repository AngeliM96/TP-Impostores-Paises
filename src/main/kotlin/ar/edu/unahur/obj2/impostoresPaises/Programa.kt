package ar.edu.unahur.obj2.impostoresPaises


// Acá encapsulamos el manejo de la consola real, desacoplandolo del programa en sí
object Consola {
  fun leerLinea() = readLine()
  fun escribirLinea(contenido: String) {
    println(contenido)
  }
}

// El código de nuestro programa, que (eventualmente) interactuará con una persona real
object Programa {
  // Ambas son variables para poder cambiarlas en los tests
  var entradaSalida = Consola
  var api = RestCountriesAPI()
  var observatorio = Observatorio(api)
  fun iniciar() {
    var numeroOpcion: Int?
    do {
      entradaSalida.escribirLinea("""
      Hola, escribí el número de la opción que quieras saber:
      1) Si dos paises son limítrofes.
      2) Si dos paises necesitan traductor para dialogar.
      3) Si dos paises son potenciales aliados.
      4) Cuales son los 5 paises más poblados.
      5) Cuál es el continente más poblado.
      """.trimIndent())
      numeroOpcion = entradaSalida.leerLinea()?.toIntOrNull()
      if (numeroOpcion == null || numeroOpcion > 5) entradaSalida.escribirLinea("Por favor, escribí un número válido.")
    }
    while (numeroOpcion == null || numeroOpcion > 5)

    when (numeroOpcion) {
        1 -> { limitrofes() }
        2 -> { traductor() }
        3 -> { aliados() }
        4 -> { paisesMasPoblados() }
        else -> { continenteMasPoblado() }
    }
  }

  fun limitrofes() {
    val (pais1, pais2) = preguntar2Paises()

    if(ambosExisten(pais1, pais2)){
      if(observatorio.sonLimitrofes(pais1, pais2)){
        entradaSalida.escribirLinea(
          "${pais1} y ${pais2} son paises limítrofes."
        )
      }
      else {
        entradaSalida.escribirLinea(
          "${pais1} y ${pais2} no son paises limítrofes."
        )
      }
    }
  }
  fun traductor() {
    val (pais1, pais2) = preguntar2Paises()

    if(ambosExisten(pais1, pais2)) {
      if (observatorio.necesitanTraductor(pais1, pais2)) {
        entradaSalida.escribirLinea(
          "${pais1} y ${pais2} necesitan traductor."
        )
      } else {
        entradaSalida.escribirLinea(
          "${pais1} y ${pais2} no necesitan traductor."
        )
      }
    }
  }
  fun aliados() {
    val (pais1, pais2) = preguntar2Paises()

    if(ambosExisten(pais1, pais2)) {
      if (observatorio.sonPotencialesAliados(pais1, pais2)) {
        entradaSalida.escribirLinea(
          "${pais1} y ${pais2} son potenciales aliados."
        )
      } else {
        entradaSalida.escribirLinea(
          "${pais1} y ${pais2} no son potenciales aliados."
        )
      }
    }
  }
  fun paisesMasPoblados() {
    entradaSalida.escribirLinea(
      "Los cinco paises más poblados son: ${observatorio.cincoMasPoblados()}"
    )
  }
  fun continenteMasPoblado() {
    entradaSalida.escribirLinea(
      "El continente más poblado es: ${observatorio.continenteMasPoblado()}"
    )
  }

  fun preguntar2Paises(): Array<String> {
    var pais1: String
    do {
      entradaSalida.escribirLinea("Por favor, escribí el nombre del primer pais")
      pais1 = entradaSalida.leerLinea().toString()
      if(pais1.isBlank()) entradaSalida.escribirLinea("Por favor, escribí el nombre del primer pais")
    }
    while(pais1.isBlank())

    var pais2: String
    do {
      entradaSalida.escribirLinea("Por favor, escribí el nombre del segundo pais")
      pais2 = entradaSalida.leerLinea().toString()
      if(pais2.isBlank()) entradaSalida.escribirLinea("Por favor, escribí el nombre del segundo pais")
    }
    while(pais2.isBlank())
    try {
      check(existe(pais1))
    } catch (e: IllegalStateException) {
      entradaSalida.escribirLinea("No existe un pais con el nombre ${pais1}")
    }
    try {
      check(existe(pais2))
    } catch (e: IllegalStateException) {
      entradaSalida.escribirLinea("No existe un pais con el nombre ${pais2}")
    }
    return arrayOf(pais1,pais2)
  }
  fun existe(pais: String): Boolean {
    val paisEncontrado = api.buscarPaisesPorNombre(pais)
    return paisEncontrado.isNotEmpty()
  }
  fun ambosExisten(pais1: String, pais2: String) = existe(pais1) && existe(pais2)
}
