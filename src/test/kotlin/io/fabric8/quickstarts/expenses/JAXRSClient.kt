package io.fabric8.quickstarts.expenses

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider
import io.fabric8.quickstarts.expences.ExpensesService
import org.apache.cxf.jaxrs.client.JAXRSClientFactory
import java.util.*
import java.util.Collections.singletonList




/**
 *
 * client to call RESTful service
 *
 */
object JAXRSClient {


    /**
     * @return restful service stub
     */
    fun getExpenecesService(port: String, webAppPath:String):ExpensesService {
        val expencesService = JAXRSClientFactory.create(
                "http://localhost:" + port  + webAppPath,
                ExpensesService::class.java, Collections.singletonList(JacksonJsonProvider()))

        return expencesService
    }

}