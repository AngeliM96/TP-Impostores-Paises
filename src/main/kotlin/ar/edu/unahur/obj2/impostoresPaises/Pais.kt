package ar.edu.unahur.obj2.impostoresPaises

interface Pais {
    val nombre: String
    val codigoIso3: String
    val poblacion: Long
    val continente: String
    val bloquesRegionales: List<String>
    val idiomasOficiales: List<String>
    val paisesLimitrofes: MutableList<Pais>
    fun esLimitrofeCon(nombrePais: String) = paisesLimitrofes.any { it.nombre == nombrePais }
    fun comparteIdiomaCon(pais: Pais) = idiomasOficiales.any { it in pais.idiomasOficiales }
    fun comparteBloqueRegionalCon(pais: Pais) = bloquesRegionales.any { it in pais.bloquesRegionales }
    fun potencialAliado(pais: Pais) = comparteIdiomaCon(pais) && comparteBloqueRegionalCon(pais)
}

class PaisBase(override val nombre: String, override val codigoIso3: String, override val poblacion: Long,
               override val continente: String, override val bloquesRegionales: List<String>,
               override val idiomasOficiales: List<String>) : Pais {
    override val paisesLimitrofes = mutableListOf<Pais>()
    fun agregarPaisLimitrofe(pais: Pais) {
        paisesLimitrofes.add(pais)
        pais.paisesLimitrofes.add(this)
    }
}
