package ar.edu.unahur.obj2.impostoresPaises

import io.kotest.core.spec.style.DescribeSpec
import io.mockk.*

class ProgramaTest : DescribeSpec({
  describe("Programa") {
    val consolaMock = mockk<Consola>()
    val observatorioMock = mockk<Observatorio>()
    // Configuramos un mock para la entrada salida
    // TODO: hacer lo mismo para la RestCountriesAPI
    Programa.entradaSalida = consolaMock
    Programa.observatorio = observatorioMock
    // Indicamos que los llamados a `escribirLinea` no hacen nada (just Runs)
    every { consolaMock.escribirLinea(any()) } just Runs


    describe("Consulta 1: Saber si son limitrofes") {
      it("Caso feliz") {
        every { observatorioMock.sonLimitrofes("Argentina", "Bolivia (Plurinational State of)")} returns true
        every { consolaMock.leerLinea() } returnsMany listOf("1","Argentina", "Bolivia (Plurinational State of)")
        Programa.iniciar()
        verify {
          consolaMock.escribirLinea("Argentina y Bolivia (Plurinational State of) son paises limítrofes.")
        }
      }
      it("Caso de error") {
        every { consolaMock.leerLinea() } returnsMany listOf("1","Argentina", "Gondor")
        Programa.iniciar()
        verify {
          consolaMock.escribirLinea("No existe un pais con el nombre Gondor")
        }
      }
    }
    describe("Consulta 2: Saber si necesitan traductor") {
      it("Caso feliz") {
        every { observatorioMock.necesitanTraductor("Argentina", "Bolivia (Plurinational State of)")} returns false
        every { consolaMock.leerLinea() } returnsMany listOf("2","Argentina", "Bolivia (Plurinational State of)")
        Programa.iniciar()
        verify {
          consolaMock.escribirLinea("Argentina y Bolivia (Plurinational State of) no necesitan traductor.")
        }
      }
      it("Caso de error") {
        every { consolaMock.leerLinea() } returnsMany listOf("2","Argentina", "Narnia")
        Programa.iniciar()
        verify {
          consolaMock.escribirLinea("No existe un pais con el nombre Narnia")
        }
      }
    }
    describe("Consulta 3: Saber si son potenciales aliados") {
      it("Caso feliz") {
        every { observatorioMock.sonPotencialesAliados("Argentina", "Bolivia (Plurinational State of)")} returns true
        every { consolaMock.leerLinea() } returnsMany listOf("3","Argentina", "Bolivia (Plurinational State of)")
        // Iniciamos el programa
        Programa.iniciar()
        // Verificamos que se escribió "por pantalla" el resultado esperado
        verify {
          consolaMock.escribirLinea("Argentina y Bolivia (Plurinational State of) son potenciales aliados.")
        }
      }
      it("Caso de error") {
        every { consolaMock.leerLinea() } returnsMany listOf("3","Argentina", "Isengard")
        Programa.iniciar()
        verify {
          consolaMock.escribirLinea("No existe un pais con el nombre Isengard")
        }
      }
    }
    it("Consulta 4: Saber los cinco paises más poblados") {
      every { observatorioMock.cincoMasPoblados() } returns listOf(
        "China", "India", "United States of America", "Indonesia", "Brazil"
      )
      every { consolaMock.leerLinea() } returnsMany listOf("4")
      Programa.iniciar()
      verify {
        consolaMock.escribirLinea("Los cinco paises más poblados son: [China, India, United States of America, Indonesia, Brazil]")
      }
    }
    it("Consulta 5: Saber el continente más poblado") {
      every { observatorioMock.continenteMasPoblado() } returns "Asia"
      every { consolaMock.leerLinea() } returnsMany listOf("5")
      Programa.iniciar()
      verify {
        consolaMock.escribirLinea("El continente más poblado es: Asia")
      }
    }
  }
})
