package io.fabric8.quickstarts.expences

import io.fabric8.quickstarts.expenses.Expense
import io.swagger.annotations.*
import org.apache.camel.CamelContext
import org.springframework.stereotype.Service
import java.sql.Date
import javax.ws.rs.*
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response


//check swagger old annotation style
// /https://github.com/swagger-api/swagger-core/wiki/Annotations


/**
 * expenses rest services
 */
@Path("/expenses")
@Api(description = "the expenses API")
@Service
interface ExpensesService {

    @GET
    @Path("")
    @ApiOperation(value = "Get all expenses in system",
            response = Response::class)
    @Produces(MediaType.APPLICATION_JSON)
    fun findAll(): Response

    @DELETE
    @Path("/{id}")
    @ApiOperation("Delete an expense")
    fun delete(@PathParam("id") @ApiParam("Expense id to delete")  id:Long)

    @POST
    @Path("/")
    @Consumes("application/json")
    @Produces("application/json")
    @ApiOperation(value = "Add a new expense")
    @ApiResponses(
            ApiResponse(code = 200, message = "successful operation",response = Response::class),
            ApiResponse(code = 405, message = "invalid input")
    )
    fun create(expense: Expense) :Response


    @PUT
    @Path("/")
    @ApiOperation(value = "update an existing expense",
            notes = "")
    @ApiResponses(
            ApiResponse(code = 200, message = "successful operation"),
            ApiResponse(code = 404, message = "Expense not found")
    )
    fun update(@PathParam("id") id:Long, expense: Expense):Response


    @GET
    @Path("/{id}")
    @ApiOperation(value = "fetch an expense by id",
            notes = "", response = Expense::class)
    @ApiResponses(
            ApiResponse(code = 400, message = "Invalid id supplied"),
            ApiResponse(code = 404, message = "Expense not found")
    )
    fun find(@PathParam("id") id:Long): Response
}

@Api("/expences")
class ExpensesServiceImpl : ExpensesService {

    private var camelContext:CamelContext

    constructor(camelContext:CamelContext){
        this.camelContext = camelContext
    }


    @ApiOperation(value = "Create expense in system",
            notes = "")
    override fun create(expense: Expense): Response {
        camelContext.createFluentProducerTemplate().to("direct:in").withBody(arrayListOf(expense)).send()
        return Response.ok().build()
    }



    override fun update(id: Long, expense: Expense):Response {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    override fun find(id: Long):Response{
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    override fun findAll(): Response {
        val exchange = this.camelContext.createFluentProducerTemplate().to("direct:select").send()
        val camelResult= exchange.getIn().body as List<Map<String,Any>>
        val entities = mutableListOf<Expense>()
        //convert sql result to the entities
        camelResult.forEach{
            entities.add(Expense(id= (it.get("id".toUpperCase()) as Long),
                    description = (it.get("description".toUpperCase()) as String),
                    amount = (it.get("amount".toUpperCase()) as Long),
                    createdAT = (it.get("created".toUpperCase()) as Date).toLocalDate(),
                    tstamp = (it.get("tstamp".toUpperCase()) as Date).toLocalDate()
            ))
        }
        return Response.ok(entities, MediaType.APPLICATION_JSON).build()
    }


    @ApiOperation(value = "Delete expense by id ",
            notes = "")
    override fun delete(id: Long) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}