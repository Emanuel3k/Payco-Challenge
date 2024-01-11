package com.br.emanuel3k.controller

import com.br.emanuel3k.dto.EmailForm
import com.br.emanuel3k.model.Email
import com.br.emanuel3k.service.EmailService
import io.smallrye.mutiny.Uni
import jakarta.enterprise.context.ApplicationScoped
import jakarta.validation.Valid
import jakarta.ws.rs.*
import jakarta.ws.rs.core.Response
import org.eclipse.microprofile.reactive.messaging.Channel
import org.eclipse.microprofile.reactive.messaging.Emitter
import java.net.URI
import java.util.*


@ApplicationScoped
@Path("api/emails")
class EmailController(
    private var emailService: EmailService,
) {
    @GET
    fun getAll(): Uni<List<Email>> {
        return emailService.getAll()
    }

    @GET
    @Path("/id/{id}")
    fun findById(@PathParam("id") id: UUID): Uni<Any> {
        return emailService.findById(id).onItem().transform { e ->
            e ?: Response.status(Response.Status.NOT_FOUND).entity("Email with id: $id not exists").build()
        }
    }

    @POST
    fun create(@Valid emailForm: EmailForm): Uni<Response> {
        return emailService.create(emailForm).onItem().transform { e ->
            URI.create("/api/emails/${e.id}")
        }.onItem().transform { uri ->
            Response.created(uri).build()
        }
    }

    @PUT
    @Path("/{id}")
    fun updateEmail(@PathParam("id") id: UUID): Uni<Any> {
        return emailService.update(id).onItem().transform { e ->
            e ?: Response.status(Response.Status.NOT_FOUND).entity("Email with id: $id not exists").build()
        }
    }

    /*@DELETE
    @Path("/{id}")
    fun delete(@PathParam("id") id: UUID): Uni<Response> {
        return emailService.deleteById(id).onItem().transform {
            Response.noContent().build()
        }
    }*/

}

