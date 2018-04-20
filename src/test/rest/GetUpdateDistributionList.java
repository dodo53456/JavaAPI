package test.rest;

import java.sql.SQLException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import test.dao.H2InMemoryDBDAO;
import test.json.DistributionListResponse;

@Path("/getUpdateDistributionList")
public class GetUpdateDistributionList {

	@POST
	public Response addFriend(String payload) {

		System.out.println("==> Payload from HTTP Request: " + payload);
		DistributionListResponse response = new DistributionListResponse();
		Gson gson = new Gson();
		try {

			JsonObject jsonObject = gson.fromJson(payload, JsonObject.class);
			String sender = jsonObject.get("sender").getAsString();
			String text = jsonObject.get("text").getAsString();

			if ((sender == null) || (sender.length() == 0)) {
				return Response.serverError().entity("[Error]: invalid json input. Sender email can not be empty.")
						.build();
			}

			// Get distribution list from DB
			List<String> distributionList = H2InMemoryDBDAO.getDistributionListByEmail(sender);

			// Get email address from input text
			// Using regex to get email from input text string
			Matcher m = Pattern.compile("[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+").matcher(text);
			while (m.find()) {
				String emailInText = m.group();
				// Only when the email does not exist on the list from DB 
				// and the email did not request a block update to the sender
				if (!distributionList.contains(emailInText) && !H2InMemoryDBDAO.isUserBlocked(sender, emailInText))
					distributionList.add(m.group());
			}

			// Create Distribution List JSON response
			response.setRecipients(distributionList);
			if (distributionList.size() == 0) {
				response.setSuccess(false);
			} else {
				response.setSuccess(true);
			}

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
