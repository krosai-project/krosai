package io.kamo.ktor.client.ai.core.test.chat.function

import io.kamo.ktor.client.ai.core.chat.function.buildFunctionCall
import io.kamo.ktor.client.ai.core.chat.function.withCall
import io.kamo.ktor.client.ai.core.util.createJsonElement
import kotlinx.serialization.Serializable
import kotlin.test.Test
import kotlin.test.assertEquals

class FunctionCallBuilderTest {
    @Test
    fun one_parameter() {
        val json = """
        {
             "name": "kamo"
        }
        """.trimIndent()

        buildFunctionCall(::oneParameterFunction) {
            withCall(::oneParameterFunction)
        }.call(json)
            .let {
                println(it)
                assertEquals(it, "name: kamo")
            }
    }

    @Test
    fun multi_parameter() {
        val json = """
        {
           "name": "kamo",
           "age": "11"
        }
        """.trimIndent()

        buildFunctionCall(::multiParameterFunction) {
            withCall(::multiParameterFunction)
        }.call(json)
            .let {
                println(it)
                assertEquals(it, "name: kamo, age: 11")
            }
    }


    @Test
    fun one_object_parameter() {
        val json = """
        {
           "name": "kamo",
           "age": "11"
        }
        """.trimIndent()

        buildFunctionCall(::oneObjectParameterFunction) {
            withCall(::oneObjectParameterFunction)
        }.call(json)
            .let {
                println(it)
                assertEquals(it, "student: Student(name=kamo, age=11)")
            }
    }

    @Test
    fun multi_object_parameter() {
        val json = """
        {
           "name": "kamo",
           "age": "11",
           "s2": {
               "name": "akino",
               "age": "12"
           }
        }
        """.trimIndent()

        buildFunctionCall(::multiObjectParameterFunction) {
            withCall(::multiObjectParameterFunction)
        }.call(json)
            .let {
                println(it)
                assertEquals(it, "s1: Student(name=kamo, age=11); s2: Student(name=akino, age=12)")
            }
    }

    @Test
    fun one_array_parameter() {
        val json = """
            [
                {
                   "name": "kamo",
                   "age": "11"
                }
            ]
        """.trimIndent()

        buildFunctionCall(::oneArrayParameterFunction) {
            withCall(::oneArrayParameterFunction)
        }.call(json)
            .let {
                println(it)
                assertEquals(it, "student: Student(name=kamo, age=11)")
            }
    }

    @Test
    fun multi_array_parameter() {
        val json = """
            [
                {
                   "name": "kamo",
                   "age": "11"
                },
                {
                   "name": "akino",
                   "age": "12"
                }
            ]
        """.trimIndent()

        buildFunctionCall(::multiArrayParameterFunction) {
            withCall(::multiArrayParameterFunction)
        }.call(json)
            .let {
                println(it)
                assertEquals(it, "Student(name=kamo, age=11); Student(name=akino, age=12)")
            }
    }

    @Test
    fun resolve_input_schema() {
        buildFunctionCall(::multiArrayParameterFunction) {
            withCall { it: Student ->
                println(it)
            }
        }.also {
            println(it.inputSchema)
            assertEquals(
                it.inputSchema.toString(),
                """{"${'$'}schema":"https://json-schema.org/draft/2020-12/schema","type":"object","required":["name","age"],"properties":{"name":{"type":"string"},"age":{"type":"int"}}}"""
            )
        }
    }

    @Test
    fun create_json_element() {
        val list = listOf(
            Student("kamo", 12),
            1, "akino",
            mapOf("par1" to 2, "par2" to "hi")
        )
        createJsonElement(list).also {
            println(it)
            assertEquals(
                it.toString(),
                """[{"${'$'}schema":"https://json-schema.org/draft/2020-12/schema","type":"object","required":["name","age"],"properties":{"name":{"type":"string"},"age":{"type":"int"}}},1,"akino",{"par1":2,"par2":"hi"}]"""
            )
        }
    }

}


@Serializable
data class Student(val name: String, val age: Int)

fun oneParameterFunction(name: String): String {
    return "name: $name"
}

fun multiParameterFunction(name: String, age: Int): String {
    return "name: $name, age: $age"
}

fun oneObjectParameterFunction(student: Student): String {
    return "student: $student"
}

fun multiObjectParameterFunction(name: String, age: Int, student: Student): String {
    return "s1: Student(name=$name, age=$age); s2: $student"
}

fun oneArrayParameterFunction(student: Student): String {
    return "student: $student"
}

fun multiArrayParameterFunction(vararg students: Student): String {
    return students.joinToString("; ")
}
