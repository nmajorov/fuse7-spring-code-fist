package io.fabric8.quickstarts.expenses

import io.swagger.annotations.*
import org.apache.camel.CamelContext
import org.apache.camel.component.sql.SqlConstants
import org.apache.commons.logging.LogFactory
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
@Api(value="the expenses api")
@Service
interface ExpensesService {

    @GET
    @Path("")
    @ApiOperation(value = "Get all expenses in system")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiResponses(ApiResponse(code = 200, message = "successful operation",response = Expense::class ,
            responseContainer = "Array"))
    fun findAll(): Response

    @DELETE
    @Path("/{id}")
    @ApiOperation("Delete an expense")
    @ApiResponses(
            ApiResponse(code = 200, message = "successful operation",response = Response::class),
            ApiResponse(code = 400, message = "invalid input"),
            ApiResponse(code = 404 , message = "expense not found")
    )
    fun delete(@PathParam("id") @ApiParam("Expense id to delete")  id:Long) :Response

    @POST
    @Path("/")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
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
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiResponses(
            ApiResponse(code = 200, message = "successful operation")
    )
    fun update(expense: Expense):Response


    @GET
    @Path("/{id}")
    @ApiOperation(value = "fetch an expense by id",
            notes = "", response = Response::class)
    @ApiResponses(
            ApiResponse(code = 400, message = "invalid id supplied"),
            ApiResponse(code = 404, message = "expense not found"),
            ApiResponse(code = 200, message = "successful operation")
    )
    @Produces("application/json")
    fun find(@PathParam("id") id:Long): Response
}


/**
 * Implementation of expenses service.
 *  
 */
@Api("/expenses")
class ExpensesServiceImpl : ExpensesService {

    private val logger = LogFactory.getLog(ExpensesServiceImpl::class.java)

    private var camelContext:CamelContext

    constructor(camelContext:CamelContext){
        this.camelContext = camelContext

    }


    @ApiOperation(value = "Create expense in system",
            notes = "")
    override fun create(expense: Expense): Response {
        logger.info("got expense to insert: $expense")
        val endpoint = camelContext.getEndpoint("direct:insert")
        val exchange =  endpoint.createExchange();
        exchange.getIn().setHeader(SqlConstants.SQL_RETRIEVE_GENERATED_KEYS, true);
        exchange.getIn().setHeader(SqlConstants.SQL_GENERATED_COLUMNS, arrayOf("ID"))
        exchange.getIn().body = (expense)


        val out = camelContext.createProducerTemplate().send(endpoint,exchange).out

        val generatedKeys = out.getHeader(SqlConstants.SQL_GENERATED_KEYS_DATA, List::class.java)
                as List<Map<String, Int>>

        logger.info("get generated keys ${generatedKeys}")

        return Response.ok(generatedKeys, MediaType.APPLICATION_JSON).build()
    }



    override fun update(expense: Expense) :Response  {
        logger.info("call update expense: $expense")
        val template = this.camelContext.createFluentProducerTemplate()
        expense.id?.run {
            //id is not null do update and return ok
            val exchange =template.to("direct:update-one").withBody(expense).send()
            return Response.ok().build()
        }

        //id is null return error
        logger.error("can't update expense: $expense cause id is null or not provided ")
        return Response.status(Response.Status.BAD_REQUEST.statusCode,
                "Expense id is not provided").build()


    }



    override fun find(id: Long):Response{
        logger.info("call find by id: $id")
        val exchange = this.camelContext.createFluentProducerTemplate().to("direct:select-one")
                .withBody(id).send()
        val camelResult= exchange.getIn().body as List<Map<String,Any>>

        if (camelResult.isNotEmpty()){
            logger.info("result is not empty")
            //convert sql result to the entities
            camelResult.get(0).let{
                val entity =Expense(id= (it.get("id".toUpperCase()) as Long),
                        description = (it.get("description".toUpperCase()) as String),
                        amount = (it.get("amount".toUpperCase()) as Double),
                        createdAT = (it.get("created".toUpperCase()) as Date).toLocalDate(),
                        tstamp = (it.get("tstamp".toUpperCase()) as Date).toLocalDate()
                )

                return Response.ok(entity, MediaType.APPLICATION_JSON).build()
            }
        }


        val builder = Response.status(Response.Status.NOT_FOUND)
        return builder.build()

    }


    override fun findAll(): Response {
        val exchange = this.camelContext.createFluentProducerTemplate().to("direct:select").send()
        val camelResult= exchange.getIn().body as List<Map<String,Any>>
        val entities = mutableListOf<Expense>()
        //convert sql result to the entities
        camelResult.forEach {
            entities.add(Expense(id= (it.get("id".toUpperCase()) as Long),
                    description = (it.get("description".toUpperCase()) as String),
                    amount = (it.get("amount".toUpperCase()) as Double),
                    createdAT = (it.get("created".toUpperCase()) as Date).toLocalDate(),
                    tstamp = (it.get("tstamp".toUpperCase()) as Date).toLocalDate()
            ))
        }
        return Response.ok(entities, MediaType.APPLICATION_JSON).build()
    }



    override fun delete(id: Long) : Response {
        logger.info("got expense id to delete: $id")
        this.camelContext.createFluentProducerTemplate().to("direct:delete-one")
                .withBody(id).send()
        return Response.ok().build()
    }

}