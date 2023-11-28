package org.acme;

import java.net.URI;
import java.util.Objects;

import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.hibernate.service.spi.ServiceException;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@Slf4j
public class PessoaResource{

    private final PessoaService pessoaService;

    @GET
    @APIResponse(
            responseCode = "200",
            description = "Pegar todas as pessoas", //Pegar as pessoas, TUDO.
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(type = SchemaType.ARRAY, implementation = Pessoa.class)
            )
    )
    public Response get() {
        return Response.ok(pessoaService.findAll()).build();
    }

    @GET
    @Path("/{pessoaId}")
    @APIResponse(
            responseCode = "200",
            description = "Pegar as pessoas por pessoaId", //<- explicacao do metodo
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(type = SchemaType.OBJECT, implementation = Pessoa.class)
            )
    )
    @APIResponse(
            responseCode = "404",
            description = "Pessoa não existe pelo pessoaId, does not exist for customerId",
            content = @Content(mediaType = MediaType.APPLICATION_JSON)
    )
    public Response getById(@Parameter(name = "pessoaId", required = true) @PathParam("pessoaId") Integer pessoaId) {
        return pessoaService.findById(pessoaId)
                .map(pessoa -> Response.ok(pessoa).build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }



    @POST
    @APIResponse(
            responseCode = "201",
            description = " Criar a pessoa", // Cria a pessoa
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(type = SchemaType.OBJECT, implementation = Pessoa.class)
            )
    )
    @APIResponse(
            responseCode = "400",
            description = "Pessoa Invalida",
            content = @Content(mediaType = MediaType.APPLICATION_JSON)
    )
    @APIResponse(
            responseCode = "400",
            description = "A Pessoa já existe para pessoaId",
            content = @Content(mediaType = MediaType.APPLICATION_JSON)
    )
    public Response post(@NotNull @Valid Pessoa pessoa, @Context UriInfo uriInfo) {
        pessoaService.save(pessoa);
        URI uri = uriInfo.getAbsolutePathBuilder().path(Integer.toString(pessoa.getPessoaId())).build();
        return Response.created(uri).entity(pessoa).build();
    }

    @PUT
    @Path("/{customerId}")
    @APIResponse(
            responseCode = "204",
            description = "Pessoa a atualizada",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(type = SchemaType.OBJECT, implementation = Pessoa.class)
            )
    )
    @APIResponse(
            responseCode = "400",
            description = "Pessoa erro ao atualizar",
            content = @Content(mediaType = MediaType.APPLICATION_JSON)
    )
    @APIResponse(
            responseCode = "400",
            description = "Pessoa nao existe para pessoaId",
            content = @Content(mediaType = MediaType.APPLICATION_JSON)
    )
    @APIResponse(
            responseCode = "400",
            description = "A variável de caminho pessoaId não corresponde a Pessoa.pessoaId",
            content = @Content(mediaType = MediaType.APPLICATION_JSON)
    )
    @APIResponse(
            responseCode = "404",
            description = "Nenhum Pessoa encontrado para o pessoaId fornecido",
            content = @Content(mediaType = MediaType.APPLICATION_JSON)
    )
    public Response put(@Parameter(name = "pessoaId", required = true) @PathParam("pessoaId") Integer pessoaId, @NotNull @Valid Pessoa pessoa) {
        if (!Objects.equals(pessoaId, pessoa.getPessoaId())) {
            throw new ServiceException("A variável de caminho pessoaId não corresponde a Pessoa.pessoaId");
        }
        pessoaService.update(pessoa);
        return Response.status(Response.Status.NO_CONTENT).build();
    }


}