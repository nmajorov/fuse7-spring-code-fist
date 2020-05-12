package io.fabric8.quickstarts.expenses

import io.fabric8.quickstarts.cxf.jaxrs.ExpensesApplication
import org.apache.commons.logging.LogFactory
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner
import java.io.InputStream
import java.time.LocalDateTime
import java.time.Month
import javax.ws.rs.core.GenericType
import javax.ws.rs.core.Response


/**
 * Test RESTful services
 *
 *
 */
@RunWith(SpringRunner::class)
@SpringBootTest(classes = arrayOf(ExpensesApplication::class), webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class SpringRestTest {

    private val logger = LogFactory.getLog(SpringRestTest::class.java)


    @Value("\${cxf.path}")
    private lateinit var cxfPathProperty: String


    @Test
    fun createExpensesTest() {
        logger.info("*** start test createExpensesTest ****")
        logger.info("*** rest path settings: $cxfPathProperty")


        val expenseService = JAXRSClient.getExpenecesService("8080", cxfPathProperty)

        val ldt = LocalDateTime.of(2019, Month.SEPTEMBER, 29, 12, 17, 0)

        val resp = expenseService.create(Expense(amount = 30.10,
                createdAT = ldt.toLocalDate(),
                //     createdAT = LocalDateTime.of(2019, Month.SEPTEMBER, 29, 12, 17, 0),
                description = "Schloss Schoenbrunn entry fee"))

        assertNotNull(resp)
        assertTrue(resp.status == 200)
        //get  object  as simple resp.entity  return stream
        val keys = resp.readEntity(object : GenericType<List<Map<String, Int>>>() {}) as List<Map<String, Int>>
        assertTrue(keys.size == 1)
        logger.info("***  response inserted ID: ${keys.get(0).get("ID")}")

    }

    @Test
    fun getAllExpensesTest() {
        logger.info("*** rest path settings: $cxfPathProperty")

        val expenseService = JAXRSClient.getExpenecesService("8080", cxfPathProperty)


        val resp = expenseService.findAll()
        assertTrue(resp.status == 200)

        val entities = resp.readEntity(object : GenericType<List<Expense>>() {

        })

        assertNotNull(entities)
        assertTrue(entities.isNotEmpty())
        assertNotNull(entities[0].id)
    }

    @Test
    fun getOneExpense() {

        logger.info("*** tes  path settings: $cxfPathProperty")

        val rest = JAXRSClient.getExpenecesService("8080", cxfPathProperty)

        val ldt = LocalDateTime.of(2019, Month.NOVEMBER, 14, 8, 7, 0)

        val newExpense = Expense(amount = 3.0,
                createdAT = ldt.toLocalDate(),
                //     createdAT = LocalDateTime.of(2019, Month.SEPTEMBER, 29, 12, 17, 0),
                description = "Coffee")

        run {
            rest.create(newExpense)
                    .readEntity(object : GenericType<List<Map<String, Int>>>() {}) as List<Map<String, Int>>
        }.let {
            //got id returned from database
            val id = it[0]["ID"]!!.toLong()
            //check  if it not null
            assertNotNull(id)
            val expenseFoundById = rest.find(id).readEntity(object : GenericType<Expense>() {})
            run {
                assertTrue(expenseFoundById.description == newExpense.description)
                assertTrue(expenseFoundById.amount == newExpense.amount)
            }
        }

    }


    @Test
    fun deleteExpense() {

        logger.info("*** deleteExpense ****")

        val rest = JAXRSClient.getExpenecesService("8080", cxfPathProperty)

        val ldt = LocalDateTime.of(2019, Month.NOVEMBER, 15, 13, 8, 0)

        val newExpense = Expense(amount = 139.20,
                createdAT = ldt.toLocalDate(),
                //     createdAT = LocalDateTime.of(2019, Month.SEPTEMBER, 29, 12, 17, 0),
                description = "Apple Magic Keyboard")

        run {
            rest.create(newExpense)
                    .readEntity(object : GenericType<List<Map<String, Int>>>() {}) as List<Map<String, Int>>
        }.let {
            //got id returned from database
            val id = it[0]["ID"]!!.toLong()
            //check  if it not null
            assertNotNull(id)
            rest.delete(id).run {
                assertTrue(this.status == 200)
                //additional checks
                // we should not find deleted entity  it in database
                val responseFromFind = rest.find(id)
                assertTrue("response type is not 404",
                        responseFromFind.status == Response.Status.NOT_FOUND.statusCode)
                val expenseFoundById = responseFromFind.readEntity(object : GenericType<Any>() {})
                logger.info("*** got response object by id which is not exist:  $expenseFoundById ****")

                when (expenseFoundById) {
                    is InputStream -> {
                        //check if stream is empty
                        // logger.info("***stream bytes: ${expenseFoundById.bufferedReader().use(BufferedReader::readText)} ****")
                        assertTrue(expenseFoundById.available() == 0)
                    }
                    else -> fail("can't detect type of response")

                }
            }

        }
    }

    @Test
    fun testUpdate(){
        logger.info("*** test update ****")

        val rest = JAXRSClient.getExpenecesService("8080", cxfPathProperty)

        // 1. create a new expense  first
        val expense = Expense(amount = 270.0,
                createdAT = let{
                    LocalDateTime.of(2019, Month.NOVEMBER, 15, 19, 38, 11)
                }.toLocalDate(),
                description = "Apple AirPods")

        run {
            // 2.  send it to rest service and get created id
            rest.create(expense)
                    .readEntity(object : GenericType<List<Map<String, Int>>>() {}) as List<Map<String, Int>>
        }.let {
            //got id returned from database
            val id = it[0]["ID"]!!.toLong()
            expense.id = id
            expense.amount = 279.10
            expense.description = "Apple AirPods Pro"
            val response = rest.update(expense)
            assertTrue("response status is not 200 but ${response.status}",
                    response.status == Response.Status.OK.statusCode)
            //GET expense from service again and check if amount changes
            val changedExpenseFromDB = rest.find(id).readEntity(object : GenericType<Expense>() {})
            assertTrue("amount  should be the same after update" ,changedExpenseFromDB.amount == expense.amount)
            assertTrue("description didn't change should be the same should be the same after update",
                    changedExpenseFromDB.description == "Apple AirPods Pro")

        }

    }

}







