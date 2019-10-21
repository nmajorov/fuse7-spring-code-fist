package io.fabric8.quickstarts.expenses

import io.fabric8.quickstarts.cxf.jaxrs.SampleRestApplication
import org.apache.commons.logging.LogFactory
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner
import java.time.LocalDateTime
import java.time.Month
import javax.ws.rs.core.GenericType


/**
 * Test RESTful services
 *
 *
 */
@RunWith(SpringRunner::class)
@SpringBootTest(classes = arrayOf(SampleRestApplication::class), webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class SpringRestTest {

    private val logger = LogFactory.getLog(SpringRestTest::class.java)


    @Value("\${cxf.path}")
    private lateinit var cxfPathProperty: String



    @Test
    fun createExpensesTest() {
       logger.info("*** rest path settings: ${cxfPathProperty}")

		val expenseService = JAXRSClient.getExpenecesService("8080",cxfPathProperty)

        var ldt =LocalDateTime.of(2019, Month.SEPTEMBER, 29, 12, 17, 0)

       val resp= expenseService.create(Expense(amount = 30,
          createdAT = ldt.toLocalDate(),
       //     createdAT = LocalDateTime.of(2019, Month.SEPTEMBER, 29, 12, 17, 0),
               description = "Schloss Schoenbrunn entry fee"))

//        Assert.assertEquals("Bar", simpleComponent.foo())
        assertNotNull(resp)
    }

    @Test
    fun getAllExpensesTest() {
        logger.info("*** rest path settings: ${cxfPathProperty}" )

        val expenseService = JAXRSClient.getExpenecesService("8080",cxfPathProperty)



        val resp= expenseService.findAll()
        assertTrue(resp.status == 200)

        val entities = resp.readEntity(object : GenericType<List<Expense>>() {

        })

        assertNotNull(entities)
        assertTrue(entities.isNotEmpty())
        assertNotNull(entities[0].id)
    }
}





