package com.decentralbank.server;

import java.sql.Timestamp;
import java.util.Date;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/")
public class RestApi {

	@GET
	@Path("contract_address")
	@Produces(MediaType.APPLICATION_JSON)
	public Transaction responseMsg(@PathParam("parameter") String parameter,
			 @QueryParam("value") String value) {

		Date date = new Date();

		MultiSignatureAddress multiSigAddress = new MultiSignatureAddress();

		multiSigAddress.setAccountName("Uwelicious");
		multiSigAddress.setAccountAddress(parameter);
		multiSigAddress.setTimeStamp(new Timestamp(date.getTime()));
		return multiSigAddress;

	}
	
	@GET
	@Path("contract/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Transaction responseMsg(@PathParam("id") String id) {

		Date date = new Date();

		Transaction transaction = new Transaction();

		transaction.setAccountName(id);
		transaction.setAccountAddress(id);
		transaction.setTimeStamp(new Timestamp(date.getTime()));
		return transaction;

	}

	

	@POST
	@Path("/contract")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response consumeJSON(Transaction transaction) {

		String output = transaction.toString();

		return Response.status(200).entity(output).build();

	}

}
