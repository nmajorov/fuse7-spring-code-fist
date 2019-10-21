package io.fabric8.quickstarts.expenses

import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.commons.logging.LogFactory
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.springframework.boot.test.autoconfigure.json.JsonTest
import org.springframework.boot.test.json.JacksonTester
import java.time.LocalDate


/**
 * test object mapping
 *
 */
@JsonTest
class JacksonTest {

    private val logger = LogFactory.getLog(SpringRestTest::class.java)


    lateinit  var json: JacksonTester<List<Expense>>


    @Before
    fun setup() {
        val objectMapper = ObjectMapper()
        JacksonTester.initFields(this, objectMapper)
    }

    @Test
    @Throws(Exception::class)
    fun testSerializeSingleObject() {
         var expense = Expense()

        logger.info("************ json from single empty object: " + this.json.write(listOf(expense)).toString())


        expense= Expense(amount = 30,
                createdAT = LocalDate.of(2019,10,1),
                description = "Schloss Schoenbrunn entry fee")

        logger.info("************ json from initialized object: " + this.json.write(listOf(expense)).toString())
        var listExpenses = this.json.parse("[{\"amount\":30,\"id\":null," +
        "\"description\":\"Schloss Schoenbrunn entry fee\",\"createdAT\":\"2019-10-01\",\"tstamp\":\"2019-10-01\"}]").`object` as List<Expense>

        assertEquals(expense.amount , listExpenses[0].amount)
        assertEquals(expense.description ,  listExpenses[0].description)
        assertEquals(expense.createdAT.toString(), listExpenses[0].createdAT.toString())


    }

    @Test
    @Throws(Exception::class)
    fun testSerializeCollection(){

        val simpleCollection = arrayListOf<Expense>(Expense(amount = 30,
                createdAT = LocalDate.of(2019,10,1),
                description = "Schloss Schoenbrunn entry fee"),Expense(amount = 10,
                createdAT = LocalDate.of(2019,10,1),
                description = "Lunch"))

        val jsonFromObj = this.json.write(simpleCollection)
        logger.info("************ json array : ${jsonFromObj}" )
        val  testJSON ="""
            [{"ID":"1", "DESCRIPTION":"Lunch", "AMOUNT":"10", "CREATED":"2019-09-29", "TSTAMP":"2019-10-05"},
             {"ID":"2", 
            "DESCRIPTION":"Schloss Schoenbrunn entry fee", "AMOUNT":"30", "CREATED":"2019-09-29", "TSTAMP":"2019-10-05"}]
        """.trimIndent().toLowerCase()

        logger.info("************ json array from string : ${testJSON}" )
        val entities: List<Expense> = this.json.parse(testJSON).`object` as List<Expense>
        assertTrue(entities is List<Expense> )

    }
}