package test.rest;

import java.sql.SQLException;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import test.dao.H2InMemoryDBDAO;

@Path("/blockUpdates")
public class BlockUpdates {

	@POST
	public Response blockUpdates(String payload) {

		System.out.println("==> Payload from HTTP Request: " + payload);
		Gson gson = new Gson();
		try {
			JsonObject jsonObject = gson.fromJson(payload, JsonObject.class);
			String requestorEmail = jsonObject.get("requestor").getAsString();
			String targetEmail = jsonObject.get("target").getAsString();

			if (requestorEmail == null || requestorEmail.length() == 0 || targetEmail == null
					|| targetEmail.length() == 0) {
				return Response.serverError()
						.entity("[Error]: Invalid payload. Requestor or target email address is empty").build();
			}

			H2InMemoryDBDAO.createBlockUserRelationshipRecord(targetEmail, requestorEmail);
		
		} catch (JsonSyntaxException e) {
			return Response.serverError().entity("[Error]: Invalid JSON format payload.").build();

		}catch(SQLException e){
			return Response.serverError().entity("[Error]: exception encountered while accessing H2 in memory database.").build();
		} 
		catch (Exception e) {
			return Response.serverError().entity("[Error]: exception encountered while processing payload.").build();
		}

		String json = "{\"success\": true }";
		return Response.ok(json, MediaType.APPLICATION_JSON).build();

	}
	
}
