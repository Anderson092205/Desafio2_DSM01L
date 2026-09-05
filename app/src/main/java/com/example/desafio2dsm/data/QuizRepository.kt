package com.example.desafio2dsm.data

import com.example.desafio2dsm.model.Question
import com.example.desafio2dsm.model.QuizCategory

object QuizRepository {

    fun getCategories(): List<QuizCategory> {
        return listOf(
            QuizCategory(
                id = "el_salvador",
                title = "Cultura e Historia de El Salvador",
                description = "Descubre y pon a prueba tus conocimientos sobre la historia, símbolos patrios y geografía salvadoreña.",
                iconName = "ic_flag",
                questions = listOf(
                    Question(
                        id = 1,
                        text = "¿Cuál es el árbol nacional de El Salvador?",
                        options = listOf("El Torogoz", "El Maquilishuat", "La Flor de Izote", "El Bálsamo"),
                        correctAnswerIndex = 1
                    ),
                    Question(
                        id = 2,
                        text = "¿En qué año se firmaron los Acuerdos de Paz de El Salvador en Chapultepec?",
                        options = listOf("1979", "1989", "1992", "1994"),
                        correctAnswerIndex = 2
                    ),
                    Question(
                        id = 3,
                        text = "¿Cuál es el volcán más alto de El Salvador?",
                        options = listOf("Volcán de Izalco", "Volcán de Santa Ana (Ilamatepec)", "Volcán de San Salvador", "Volcán de San Vicente"),
                        correctAnswerIndex = 1
                    ),
                    Question(
                        id = 4,
                        text = "¿Cuál es el ave nacional de El Salvador?",
                        options = listOf("El Guardabarranco", "El Quetzal", "El Torogoz (Talapo)", "El Colibrí"),
                        correctAnswerIndex = 2
                    ),
                    Question(
                        id = 5,
                        text = "¿Cuál es la moneda digital de curso legal introducida en El Salvador en 2021 junto con el Dólar?",
                        options = listOf("El Colón", "El Bitcoin", "El Quetzal", "El Lempira"),
                        correctAnswerIndex = 1
                    )
                )
            ),
            QuizCategory(
                id = "kotlin_android",
                title = "Kotlin y Desarrollo Android",
                description = "Pon a prueba tus conocimientos sobre sintaxis de Kotlin, componentes de Android y ciclo de vida.",
                iconName = "ic_code",
                questions = listOf(
                    Question(
                        id = 1,
                        text = "¿Cuál de las siguientes palabras clave se utiliza para declarar una variable inmutable en Kotlin?",
                        options = listOf("var", "val", "const var", "static"),
                        correctAnswerIndex = 1
                    ),
                    Question(
                        id = 2,
                        text = "¿Qué método del ciclo de vida de un Activity se ejecuta justo después de onCreate() al iniciar?",
                        options = listOf("onResume()", "onPause()", "onStart()", "onDestroy()"),
                        correctAnswerIndex = 2
                    ),
                    Question(
                        id = 3,
                        text = "¿Qué operador se utiliza en Kotlin para llamadas seguras a objetos que pueden ser nulos (Null Safety)?",
                        options = listOf("!!", "?.", "?:", "?:="),
                        correctAnswerIndex = 1
                    ),
                    Question(
                        id = 4,
                        text = "¿Cuál es el componente de Android utilizado para mostrar una lista eficiente de elementos en pantalla?",
                        options = listOf("ScrollView", "LinearLayout", "RecyclerView", "FrameLayout"),
                        correctAnswerIndex = 2
                    ),
                    Question(
                        id = 5,
                        text = "¿Cuál es el archivo donde se declaran los componentes, permisos y configuración principal de una app Android?",
                        options = listOf("build.gradle", "AndroidManifest.xml", "MainActivity.kt", "strings.xml"),
                        correctAnswerIndex = 1
                    )
                )
            ),
            QuizCategory(
                id = "geografia",
                title = "Geografía Mundial",
                description = "Preguntas sobre capitales, continentes, ríos y datos geográficos fascinantes del planeta.",
                iconName = "ic_globe",
                questions = listOf(
                    Question(
                        id = 1,
                        text = "¿Cuál es la capital oficial de Australia?",
                        options = listOf("Sídney", "Melbourne", "Canberra", "Brisbane"),
                        correctAnswerIndex = 2
                    ),
                    Question(
                        id = 2,
                        text = "¿Cuál es el río más largo del mundo?",
                        options = listOf("Río Nilo", "Río Amazonas", "Río Misisipi", "Río Yangtze"),
                        correctAnswerIndex = 1
                    ),
                    Question(
                        id = 3,
                        text = "¿En qué continente se encuentra el desierto del Sahara?",
                        options = listOf("Asia", "América del Sur", "África", "Oceanía"),
                        correctAnswerIndex = 2
                    ),
                    Question(
                        id = 4,
                        text = "¿Cuál es el país con mayor superficie territorial en el mundo?",
                        options = listOf("Canadá", "Estados Unidos", "China", "Rusia"),
                        correctAnswerIndex = 3
                    ),
                    Question(
                        id = 5,
                        text = "¿Cuál es la cordillera más larga del mundo?",
                        options = listOf("Los Alpes", "Los Andes", "El Himalaya", "Las Montañas Rocosas"),
                        correctAnswerIndex = 1
                    )
                )
            ),
            QuizCategory(
                id = "ciencia",
                title = "Ciencia y Tecnología",
                description = "Desafía tu mente con datos sobre física, química, astronomía e innovación tecnológica.",
                iconName = "ic_science",
                questions = listOf(
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
                        text = "¿Qué planeta del sistema solar es conocido como el 'Planeta Rojo'?",
                        options = listOf("Venus", "Júpiter", "Marte", "Saturno"),
                        correctAnswerIndex = 2
                    ),
                    Question(
                        id = 4,
                        text = "¿Qué partícula subatómica posee carga eléctrica negativa?",
                        options = listOf("Protón", "Neutrón", "Electrón", "Quark"),
                        correctAnswerIndex = 2
                    ),
                    Question(
                        id = 5,
                        text = "¿Qué representan las siglas 'CPU' en informática?",
                        options = listOf("Central Processing Unit", "Control Power Unit", "Computer Program Utility", "Central Performance User"),
                        correctAnswerIndex = 0
                    )
                )
            )
        )
    }
}
