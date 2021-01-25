package ar.edu.unahur.obj2.impostoresPaises

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.collections.shouldContainAll
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk

class ObservatorioTest : DescribeSpec({
    describe("Un observatorio") {
        val api = mockk<RestCountriesAPI>()
        val observatorio = Observatorio(api)
        val bloqueUNASUR = RegionalBloc("UNASUR", "Union of South American Nations")
        val spanish = Language("Spanish")
        every { api.todosLosPaises() } returns listOf(
            mockk(),
            mockk(),
            mockk()
        )
        every { api.buscarPaisesPorNombre("Argentina") } returns listOf(
            Country(
                "Argentina",
                "Buenos Aires",
                "ARG",
                "Americas",
                43590400,
                listOf("BOL"),
                listOf(spanish),
                listOf(bloqueUNASUR)
            )
        )
        every { api.buscarPaisesPorNombre("Bolivia (Plurinational State of)") } returns listOf(
            Country(
                "Bolivia (Plurinational State of)",
                "Sucre",
                "BOL",
                "Americas",
                10985059,
                listOf(
                    "ARG",
                    "BRA",
                    "CHL",
                    "PRY",
                    "PER"
                ),
                listOf(spanish),
                listOf(bloqueUNASUR)
            )
        )
        every { api.buscarPaisesPorNombre("Germany") } returns listOf(
            Country(
                "Germany",
                "Berlin",
                "DEU",
                "Europe",
                81770900,
                listOf(),
                listOf(),
                listOf()
            )
        )

        describe("Requerimiento 1: Poder consultar si son limítrofes.") {
            every { api.paisConCodigo("BOL") } returns Country(
                "Bolivia (Plurinational State of)",
                "Sucre",
                "BOL",
                "Americas",
                10985059,
                listOf(
                    "ARG",
                    "BRA",
                    "CHL",
                    "PRY",
                    "PER"
                ),
                listOf(spanish),
                listOf(bloqueUNASUR)
            )
            it("Dos paises limitrofes") {
                observatorio.sonLimitrofes("Argentina", "Bolivia (Plurinational State of)").shouldBeTrue()
            }
            it("Dos paises que no son limitrofes") {
                observatorio.sonLimitrofes("Argentina", "Germany").shouldBeFalse()
            }
        }
        describe("Requerimiento 2: Saber si se necesita traducción para que puedan dialogar") {
            it("Ambos hablan español") {
                observatorio.necesitanTraductor("Argentina", "Bolivia (Plurinational State of)").shouldBeFalse()
            }
            it("Uno habla español y el otro aleman") {
                observatorio.necesitanTraductor("Argentina", "Germany").shouldBeTrue()
            }
        }
        describe("Requerimiento 3: Conocer si son potenciales aliados") {
            every { api.buscarPaisesPorNombre("Spain") } returns listOf(
                Country(
                    "Spain",
                    "Madrid",
                    "ESP",
                    "Europe",
                    46438422,
                    listOf(),
                    listOf(),
                    listOf()
                )
            )
            it("No necesitan traductor y comparten bloque regional") {
                observatorio.sonPotencialesAliados("Argentina", "Bolivia (Plurinational State of)").shouldBeTrue()
            }
            it("No necesitan traductor pero no comparten bloque regional") {
                observatorio.sonPotencialesAliados("Argentina", "Spain").shouldBeFalse()
            }
            it("Comparten bloque regional pero necesitan traductor") {
                observatorio.sonPotencialesAliados("Spain", "Germany").shouldBeFalse()
            }
            it("Necesitan traductor y no comparten bloque regional") {
                observatorio.sonPotencialesAliados("Argentina", "Germany").shouldBeFalse()
            }
        }
        describe("Requerimiento 4: Obtener los nombres de los 5 países con mayor población") {
            it("Deberian ser estados unidos, brazil, españa, alemania y argentina") {
                every { api.todosLosPaises() } returns listOf(
                    Country(
                        "China",
                        "",
                        "",
                        "",
                        46438422,
                        listOf(),
                        listOf(),
                        listOf()
                    ),
                    Country(
                    "Brazil",
                    "",
                    "",
                    "",
                    46438422,
                    listOf(),
                    listOf(),
                    listOf()
                    ),
                    Country(
                        "Indonesia",
                        "",
                        "",
                        "",
                        46438422,
                        listOf(),
                        listOf(),
                        listOf()
                    ),
                    Country(
                        "United States of America",
                        "",
                        "",
                        "",
                        46438422,
                        listOf(),
                        listOf(),
                        listOf()
                    ),
                    Country(
                        "India",
                        "",
                        "",
                        "",
                        46438422,
                        listOf(),
                        listOf(),
                        listOf()
                    )
                )
                observatorio.cincoMasPoblados().shouldContainAll("China", "India", "United States of America", "Indonesia", "Brazil")
            }
        }
        describe("Requerimiento 5: Indicar cuál es el continente más poblado") {
            it("Debería ser Asia") {
                every { api.todosLosPaises() } returns listOf(
                    Country(
                        "China",
                        "",
                        "",
                        "Asia",
                        1439323776,
                        listOf(),
                        listOf(),
                        listOf()
                    ),
                    Country(
                        "Brazil",
                        "",
                        "",
                        "Americas",
                        212559417,
                        listOf(),
                        listOf(),
                        listOf()
                    ),
                    Country(
                        "India",
                        "",
                        "",
                        "Asia",
                        1380004385,
                        listOf(),
                        listOf(),
                        listOf()
                    )
                )
                observatorio.continenteMasPoblado().shouldBe("Asia")
            }
        }
    }
})