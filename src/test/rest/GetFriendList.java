package test.rest;

import java.sql.SQLException;
import java.util.List;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import test.dao.H2InMemoryDBDAO;
import test.json.FriendListResponse;

@Path("/getFriendList")
public class GetFriendList {

	@POST
	public Response addFriend(String payload) {

		System.out.println("==> Payload from HTTP Request: " + payload);
		FriendListResponse response = new FriendListResponse();
		Gson gson = new Gson();
		try {

			JsonObject jsonObject = gson.fromJson(payload, JsonObject.class);
			String email = jsonObject.get("email").getAsString();

			if ((email == null) || (email.length() == 0)) {
				return Response.serverError().entity("[Error]: invalid json input.").build();
			}

			List<String> friendList = H2InMemoryDBDAO.getFriendListByEmail(email);

			
			//Create Friend List JSON Response
			response.setCount(friendList.size());
			if (friendList.size() == 0) {
				response.setSuccess(false);
			} else {
				response.setSuccess(true);
			}
			response.setFriends(friendList);

		} catch (JsonSyntaxException e) {

			return Response.serverError().entity("[Error]: Invalid JSON format payload.").build();

		} catch (SQLException e) {
			return Response.serverError()
					.entity("[Error]: exception encountered while accessing H2 in memory database.").build();
		} catch (Exception e) {
			return Response.serverError().entity("[Error]: exception encountered while processing payload.").build();
		}

		String json = gson.toJson(response);
		return Response.ok(json, MediaType.APPLICATION_JSON).build();

	}

}
