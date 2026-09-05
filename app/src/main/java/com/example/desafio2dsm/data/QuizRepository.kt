package com.example.desafio2dsm.data

import com.example.desafio2dsm.model.Question
import com.example.desafio2dsm.model.QuizCategory

object QuizRepository {

    /**
     * Returns the list of all categories available.
     * Each category contains its easy and hard questions.
     */
    fun getCategories(): List<QuizCategory> = allCategories

    /**
     * Returns questions for a specific category and difficulty.
     * @param categoryId The ID of the category.
     * @param difficulty "Facil" or "Dificil"
     */
    fun getQuestions(categoryId: String, difficulty: String): List<Question> {
        return if (difficulty == "Dificil") {
            hardQuestions[categoryId] ?: emptyList()
        } else {
            easyQuestions[categoryId] ?: emptyList()
        }
    }

    /**
     * Returns the category that matches a display name (title or id).
     */
    fun getCategoryByName(name: String): QuizCategory? {
        return allCategories.find {
            it.title.equals(name, ignoreCase = true) || it.id.equals(name, ignoreCase = true)
        }
    }

    // ─── Category metadata (icon + description) ──────────────────────────────

    private val allCategories: List<QuizCategory> by lazy {
        listOf(
            QuizCategory(
                id = "el_salvador",
                title = "El Salvador",
                description = "Historia, símbolos patrios y cultura salvadoreña.",
                iconName = "ic_flag",
                questions = easyQuestions["el_salvador"]!! + hardQuestions["el_salvador"]!!
            ),
            QuizCategory(
                id = "cultura_general",
                title = "Cultura General",
                description = "Curiosidades, historia y datos del mundo.",
                iconName = "ic_globe",
                questions = easyQuestions["cultura_general"]!! + hardQuestions["cultura_general"]!!
            ),
            QuizCategory(
                id = "ciencia",
                title = "Ciencia",
                description = "Física, química, biología y astronomía.",
                iconName = "ic_science",
                questions = easyQuestions["ciencia"]!! + hardQuestions["ciencia"]!!
            ),
            QuizCategory(
                id = "tecnologia",
                title = "Tecnología",
                description = "Android, Kotlin y programación.",
                iconName = "ic_code",
                questions = easyQuestions["tecnologia"]!! + hardQuestions["tecnologia"]!!
            )
        )
    }

    // ─── Easy Questions ───────────────────────────────────────────────────────

    private val easyQuestions: Map<String, List<Question>> = mapOf(

        "el_salvador" to listOf(
            Question(
                id = 1,
                text = "¿Cuál es la capital de El Salvador?",
                options = listOf("Santa Ana", "San Salvador", "San Miguel", "Sonsonate"),
                correctAnswerIndex = 1
            ),
            Question(
                id = 2,
                text = "¿Cuál es el árbol nacional de El Salvador?",
                options = listOf("El Torogoz", "El Maquilishuat", "La Ceiba", "El Bálsamo"),
                correctAnswerIndex = 1
            ),
            Question(
                id = 3,
                text = "¿Cuál es el ave nacional de El Salvador?",
                options = listOf("El Guardabarranco", "El Quetzal", "El Torogoz", "El Colibrí"),
                correctAnswerIndex = 2
            ),
            Question(
                id = 4,
                text = "¿En qué año se firmaron los Acuerdos de Paz en Chapultepec?",
                options = listOf("1979", "1989", "1992", "1994"),
                correctAnswerIndex = 2
            ),
            Question(
                id = 5,
                text = "¿Cuál es la moneda de curso legal en El Salvador junto al Bitcoin desde 2021?",
                options = listOf("El Colón", "El Dólar Estadounidense", "El Quetzal", "El Lempira"),
                correctAnswerIndex = 1
            )
        ),

        "cultura_general" to listOf(
            Question(
                id = 1,
                text = "¿Cuántos continentes tiene el planeta Tierra?",
                options = listOf("5", "6", "7", "8"),
                correctAnswerIndex = 2
            ),
            Question(
                id = 2,
                text = "¿Cuál es el océano más grande del mundo?",
                options = listOf("Atlántico", "Índico", "Ártico", "Pacífico"),
                correctAnswerIndex = 3
            ),
            Question(
                id = 3,
                text = "¿Cuántos colores tiene el arcoíris?",
                options = listOf("5", "6", "7", "8"),
                correctAnswerIndex = 2
            ),
            Question(
                id = 4,
                text = "¿Cuál es el idioma más hablado del mundo?",
                options = listOf("Inglés", "Español", "Chino Mandarín", "Hindi"),
                correctAnswerIndex = 2
            ),
            Question(
                id = 5,
                text = "¿En qué país se ubica la Torre Eiffel?",
                options = listOf("Italia", "España", "Francia", "Alemania"),
                correctAnswerIndex = 2
            )
        ),

        "ciencia" to listOf(
            Question(
                id = 1,
                text = "¿Cuál es el planeta más cercano al Sol?",
                options = listOf("Venus", "Mercurio", "Marte", "Tierra"),
                correctAnswerIndex = 1
            ),
            Question(
                id = 2,
                text = "¿Cuál es el elemento químico del símbolo 'O'?",
                options = listOf("Oro", "Osmio", "Oxígeno", "Óxido"),
                correctAnswerIndex = 2
            ),
            Question(
                id = 3,
                text = "¿Cuántos huesos tiene el cuerpo humano adulto?",
                options = listOf("206", "212", "198", "220"),
                correctAnswerIndex = 0
            ),
            Question(
                id = 4,
                text = "¿Cuál es el planeta conocido como el 'Planeta Rojo'?",
                options = listOf("Venus", "Júpiter", "Marte", "Saturno"),
                correctAnswerIndex = 2
            ),
            Question(
                id = 5,
                text = "¿A qué velocidad viaja la luz en el vacío (aprox.)?",
                options = listOf("100,000 km/s", "300,000 km/s", "500,000 km/s", "1,000,000 km/s"),
                correctAnswerIndex = 1
            )
        ),

        "tecnologia" to listOf(
            Question(
                id = 1,
                text = "¿Qué significa 'CPU'?",
                options = listOf("Central Processing Unit", "Computer Power Unit", "Control Program Utility", "Central Power User"),
                correctAnswerIndex = 0
            ),
            Question(
                id = 2,
                text = "¿Cuál es el sistema operativo móvil desarrollado por Google?",
                options = listOf("iOS", "Windows Phone", "Android", "HarmonyOS"),
                correctAnswerIndex = 2
            ),
            Question(
                id = 3,
                text = "¿Qué lenguaje de programación es oficial para desarrollar apps Android?",
                options = listOf("Java", "Swift", "Kotlin", "Python"),
                correctAnswerIndex = 2
            ),
            Question(
                id = 4,
                text = "¿Qué significa 'HTML'?",
                options = listOf("HyperText Markup Language", "High Transfer Markup Language", "HyperText Machine Language", "High Text Markup Language"),
                correctAnswerIndex = 0
            ),
            Question(
                id = 5,
                text = "¿Cuántos bits tiene un byte?",
                options = listOf("4", "8", "16", "32"),
                correctAnswerIndex = 1
            )
        )
    )

    // ─── Hard Questions ───────────────────────────────────────────────────────

    private val hardQuestions: Map<String, List<Question>> = mapOf(

        "el_salvador" to listOf(
            Question(
                id = 1,
                text = "¿Cuál es el volcán más alto de El Salvador?",
                options = listOf("Volcán de Izalco", "Volcán de Santa Ana (Ilamatepec)", "Volcán de San Salvador", "Volcán de San Vicente"),
                correctAnswerIndex = 1
            ),
            Question(
                id = 2,
                text = "¿En qué año El Salvador adoptó el Bitcoin como moneda de curso legal?",
                options = listOf("2019", "2020", "2021", "2022"),
                correctAnswerIndex = 2
            ),
            Question(
                id = 3,
                text = "¿Cómo se llamaba el presidente de El Salvador que firmó los Acuerdos de Paz en 1992?",
                options = listOf("Nayib Bukele", "Alfredo Cristiani", "José Napoleón Duarte", "Mauricio Funes"),
                correctAnswerIndex = 1
            ),
            Question(
                id = 4,
                text = "¿Cuál es la flor nacional de El Salvador?",
                options = listOf("La Rosa de Jamaica", "La Orquídea", "La Flor de Izote", "El Girasol"),
                correctAnswerIndex = 2
            ),
            Question(
                id = 5,
                text = "¿Cuántos departamentos tiene El Salvador?",
                options = listOf("10", "12", "14", "16"),
                correctAnswerIndex = 2
            )
        ),

        "cultura_general" to listOf(
            Question(
                id = 1,
                text = "¿En qué año comenzó la Primera Guerra Mundial?",
                options = listOf("1910", "1912", "1914", "1916"),
                correctAnswerIndex = 2
            ),
            Question(
                id = 2,
                text = "¿Cuál es la capital oficial de Australia?",
                options = listOf("Sídney", "Melbourne", "Canberra", "Brisbane"),
                correctAnswerIndex = 2
            ),
            Question(
                id = 3,
                text = "¿Qué civilización construyó las pirámides de Giza?",
                options = listOf("Griega", "Romana", "Egipcia", "Mesopotámica"),
                correctAnswerIndex = 2
            ),
            Question(
                id = 4,
                text = "¿Cuál es el río más largo del mundo?",
                options = listOf("Río Nilo", "Río Amazonas", "Río Misisipi", "Río Yangtze"),
                correctAnswerIndex = 0
            ),
            Question(
                id = 5,
                text = "¿Quién pintó la Mona Lisa?",
                options = listOf("Miguel Ángel", "Rafael", "Leonardo da Vinci", "Botticelli"),
                correctAnswerIndex = 2
            )
        ),

        "ciencia" to listOf(
            Question(
                id = 1,
                text = "¿Cuál es el elemento químico más abundante en el universo?",
                options = listOf("Oxígeno", "Helio", "Hidrógeno", "Carbono"),
                correctAnswerIndex = 2
            ),
            Question(
                id = 2,
                text = "¿Quién formuló la teoría de la relatividad general?",
                options = listOf("Isaac Newton", "Albert Einstein", "Nikola Tesla", "Galileo Galilei"),
                correctAnswerIndex = 1
            ),
            Question(
                id = 3,
                text = "¿Qué partícula subatómica posee carga eléctrica negativa?",
                options = listOf("Protón", "Neutrón", "Electrón", "Quark"),
                correctAnswerIndex = 2
            ),
            Question(
                id = 4,
                text = "¿Cuál es el número atómico del Carbono?",
                options = listOf("4", "6", "8", "12"),
                correctAnswerIndex = 1
            ),
            Question(
                id = 5,
                text = "¿Cómo se llama la capa de la Tierra donde se mueven las placas tectónicas?",
                options = listOf("Núcleo", "Corteza", "Manto", "Litosfera"),
                correctAnswerIndex = 3
            )
        ),

        "tecnologia" to listOf(
            Question(
                id = 1,
                text = "¿Cuál es la palabra clave para declarar una variable inmutable en Kotlin?",
                options = listOf("var", "val", "const var", "static"),
                correctAnswerIndex = 1
            ),
            Question(
                id = 2,
                text = "¿Qué método del ciclo de vida de un Activity se llama justo después de onCreate()?",
                options = listOf("onResume()", "onPause()", "onStart()", "onDestroy()"),
                correctAnswerIndex = 2
            ),
            Question(
                id = 3,
                text = "¿Qué operador se usa en Kotlin para llamadas seguras a objetos nulos?",
                options = listOf("!!", "?.", "?:", ":?"),
                correctAnswerIndex = 1
            ),
            Question(
                id = 4,
                text = "¿Cuál es el archivo que declara componentes y permisos de una app Android?",
                options = listOf("build.gradle", "AndroidManifest.xml", "MainActivity.kt", "strings.xml"),
                correctAnswerIndex = 1
            ),
            Question(
                id = 5,
                text = "¿Cuál es el componente de Android para listas eficientes?",
                options = listOf("ScrollView", "LinearLayout", "RecyclerView", "FrameLayout"),
                correctAnswerIndex = 2
            )
        )
    )
}
