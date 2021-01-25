package ar.edu.unahur.obj2.impostoresPaises

class PaisAdapter(val country: Country): Pais {
    override val paisesLimitrofes = mutableListOf<Pais>()
    override val nombre = country.name
    override val codigoIso3 = country.alpha3Code
    override val poblacion = country.population
    override val continente = country.region
    override val bloquesRegionales = country.regionalBlocs.map { it.name }
    override val idiomasOficiales = country.languages.map { it.name }
    val borders = country.borders
    override fun comparteIdiomaCon(pais: Pais): Boolean {
        return idiomasOficiales.any { it in pais.idiomasOficiales }
    }
}