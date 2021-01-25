package ar.edu.unahur.obj2.impostoresPaises

class Observatorio(val api: RestCountriesAPI) {
    fun buscarPaisConNombre(nombre: String) = PaisAdapter(api.buscarPaisesPorNombre(nombre).first())
    fun sonLimitrofes(nombrePais1: String, nombrePais2: String): Boolean {
        val pais1 = buscarPaisConNombre(nombrePais1)
        agregarLimitrofes(pais1)
        return pais1.esLimitrofeCon(nombrePais2)
    }
    fun necesitanTraductor(nombrePais1: String, nombrePais2: String): Boolean {
        val pais1 = buscarPaisConNombre(nombrePais1)
        val pais2 = buscarPaisConNombre(nombrePais2)
        return !pais1.comparteIdiomaCon(pais2)
    }
    fun sonPotencialesAliados(nombrePais1: String, nombrePais2: String): Boolean {
        val pais1 = buscarPaisConNombre(nombrePais1)
        val pais2 = buscarPaisConNombre(nombrePais2)
        return pais1.potencialAliado(pais2)
    }
    fun paises() = api.todosLosPaises().map { PaisAdapter(it) }
    fun cincoMasPoblados() = paises().sortedByDescending { it.poblacion }.slice(0..4).map { it.nombre }
    fun continenteMasPoblado() = paises().groupBy { it.continente }.mapValues { poblacionContinente(it.key) }.maxByOrNull { it.value }?.key
    fun poblacionContinente(continente: String) = paises().filter { it.continente == continente }.sumOf { it.poblacion }
    fun agregarLimitrofes(pais: PaisAdapter) {
        pais.borders.forEach { pais.paisesLimitrofes.add(PaisAdapter(api.paisConCodigo(it))) }
    }
}



