package org.ivandev.acomprar.enumeration

import org.ivandev.acomprar.Literals

object DaysOfWeekEnum {
    const val LUNES: Int = 0
    const val MARTES: Int = 1
    const val MIERCOLES: Int = 2
    const val JUEVES: Int = 3
    const val VIERNES: Int = 4
    const val SABADO: Int = 5
    const val DOMINGO: Int = 6

    // From Lunes = 0 to Domingo = 6
    fun getEnumeratedDaysOfWeek(): List<Map<String, Int>> {
        val result: MutableList<Map<String, Int>> = mutableListOf()
        val days = Literals.DaysOfWeek.getDaysOfWeek()

        days.forEachIndexed { index: Int, day: String ->
            result.add(
                mapOf(day to index)
            )
        }

        return result
    }
}