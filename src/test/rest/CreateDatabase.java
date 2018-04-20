package test.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.sql.SQLException;


import test.dao.H2InMemoryDBDAO;

@Path("/createDatabase")
public class CreateDatabase {

	@GET
	public Response createDatabaseSchema() {
		String resource = "<h1> Hello from JAVA API Server</h1>";
		System.out.println("==> Test log");
		try {
			H2InMemoryDBDAO.createDbSchemaWithPreparedStatement();
			resource += "<br/> DB schema has been created successfully. <br/> You can start the RestFul testing.";

		} catch (SQLException e) {
			return Response.serverError()
					.entity("[Error]: exception encountered while accessing H2 in memory database.").build();
		} catch (Exception e) {
			resource = "[Error]: Error encountered while creating Database schema. ";
		}

		return Response.ok(resource, MediaType.TEXT_HTML).build();

	}

}
