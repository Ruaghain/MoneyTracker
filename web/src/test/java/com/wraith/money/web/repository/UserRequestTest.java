package com.wraith.money.web.repository;

import com.wraith.money.data.entity.Users;
import com.wraith.money.web.helper.EntityRepositoryHelper;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;

import java.util.HashMap;
import java.util.Map;

/**
 * User: rowan.massey Date: 01/04/13 Time: 14:09
 */
public class UserRequestTest extends AbstractBaseIntegrationTests {

	@Test
	public void testResponseContentType() throws Exception {
		MockHttpServletResponse response = entityRepositoryHelper.getEntity("/users/");
		Assert.assertTrue(response != null);
		Assert.assertEquals(response.getStatus(), HttpStatus.OK.value());
		Assert.assertEquals(response.getContentType(), CONTENT_TYPE);
	}

	@Test
	public void testGetUserRequest() throws Exception {
		String content = entityRepositoryHelper.getEntity("/users/1").getContentAsString();
		Object object = entityRepositoryHelper.getParser().parse(content);
		JSONObject jsonObject = (JSONObject) object;
		Assert.assertEquals(jsonObject.get("userName"), "Admin");
		Assert.assertEquals(jsonObject.get("firstName"), "Admin");
		Assert.assertEquals(jsonObject.get("lastName"), "User");
	}

	@Test
	public void testCreateUserRequest() throws Exception {
		String resourceRequest = entityRepositoryHelper.createUser("first.person", "Password", "First", "Person", "");

		//Retrieve the inserted user record from the database, and ensure that values are correct.
		MockHttpServletResponse getResponse = entityRepositoryHelper.getEntity(resourceRequest);
		String content = getResponse.getContentAsString();
		JSONObject jsonObject = (JSONObject) entityRepositoryHelper.getParser().parse(content);
		Assert.assertEquals(jsonObject.get("userName"), "first.person");
		Assert.assertEquals(jsonObject.get("firstName"), "First");
		Assert.assertEquals(jsonObject.get("lastName"), "Person");
	}

	@Test
	public void testUpdateUserRequest() throws Exception {
		String resourceRequest = entityRepositoryHelper.createUser("second.person", "Passw0rd", "Second", "Person", "");

		//Update the previously created record.
		authenticate("second.person", "Passw0rd");

		JSONObject updatedUser = new JSONObject();
		updatedUser.put("userName", "second.person");
		updatedUser.put("firstName", "Second_Updated");
		updatedUser.put("lastName", "Person_Updated");

		byte[] updatedUserBytes = entityRepositoryHelper.getMapper().writeValueAsBytes(updatedUser);
		MockHttpServletResponse putResponse = entityRepositoryHelper.updateEntity(resourceRequest, updatedUserBytes);
		Assert.assertNotNull(putResponse);

		//Retrieve the previously updated user, and ensure the details were updated correctly.
		MockHttpServletResponse getUpdatedResponse = entityRepositoryHelper.getEntity(resourceRequest);
		String updatedContent = getUpdatedResponse.getContentAsString();
		JSONObject updatedJsonObject = (JSONObject) entityRepositoryHelper.getParser().parse(updatedContent);
		Assert.assertEquals(updatedJsonObject.get("firstName"), updatedUser.get("firstName"));
		Assert.assertEquals(updatedJsonObject.get("lastName"), updatedUser.get("lastName"));
	}

	@Test
	public void testUserUpdateRequestWithAdminPermission() throws Exception {
		String resourceRequest = entityRepositoryHelper.createUser("third.person", "Passw0rd", "Third", "Person", "");

		//Update the previously created record.
		authenticate("Admin", "Passw0rd");

		Users updatedUser = new Users();
		updatedUser.setFirstName("Third_Updated");
		updatedUser.setLastName("Person_Updated");

		byte[] updatedUserBytes = entityRepositoryHelper.getMapper().writeValueAsBytes(updatedUser);
		MockHttpServletResponse putResponse = entityRepositoryHelper.updateEntity(resourceRequest, updatedUserBytes);
		Assert.assertNotNull(putResponse);

		//Retrieve the previously updated user, and ensure the details were updated correctly.
		MockHttpServletResponse getUpdatedResponse = entityRepositoryHelper.getEntity(resourceRequest);
		String updatedContent = getUpdatedResponse.getContentAsString();
		JSONObject updatedJsonObject = (JSONObject) entityRepositoryHelper.getParser().parse(updatedContent);
		Assert.assertEquals(updatedJsonObject.get("userName"), updatedUser.getUserName());
		Assert.assertEquals(updatedJsonObject.get("firstName"), updatedUser.getFirstName());
		Assert.assertEquals(updatedJsonObject.get("lastName"), updatedUser.getLastName());
	}

	@Test
	public void testUserUpdateRequestWithCurrentUserPermission() throws Exception {
		String resourceRequest = entityRepositoryHelper.createUser("fourth.person", "Passw0rd", "Fourth", "Person", "");

		//Update the previously created record.
		authenticate("fourth.person", "Passw0rd");

		Users updatedUser = new Users();
		updatedUser.setUserName("fourth.person");
		updatedUser.setFirstName("Fourth_Updated");
		updatedUser.setLastName("Person_Updated");

		byte[] updatedUserBytes = entityRepositoryHelper.getMapper().writeValueAsBytes(updatedUser);
		MockHttpServletResponse putResponse = entityRepositoryHelper.updateEntity(resourceRequest, updatedUserBytes);
		Assert.assertNotNull(putResponse);

		//Retrieve the previously updated user, and ensure the details were updated correctly.
		MockHttpServletResponse getUpdatedResponse = entityRepositoryHelper.getEntity(resourceRequest);
		String updatedContent = getUpdatedResponse.getContentAsString();
		JSONObject updatedJsonObject = (JSONObject) entityRepositoryHelper.getParser().parse(updatedContent);
		Assert.assertEquals(updatedJsonObject.get("userName"), updatedUser.getUserName());
		Assert.assertEquals(updatedJsonObject.get("firstName"), updatedUser.getFirstName());
		Assert.assertEquals(updatedJsonObject.get("lastName"), updatedUser.getLastName());
	}

	@Test(expected = Exception.class)
	public void testUserUpdateRequestWithOtherUserPermission() throws Exception {
		String resourceRequest = entityRepositoryHelper.createUser("fifth.person", "Passw0rd", "Fifth", "Person", "");
		entityRepositoryHelper.createUser("sixth.person", "Passw0rd", "Sixth", "Person", "");

		//Update the previously created record.
		authenticate("sixth.person", "Passw0rd");
		Users updatedUser = null;//getUser("Fifth.person", "Passw0rd", "Fifth_Updated", "Person_Updated");
		byte[] updatedUserBytes = entityRepositoryHelper.getMapper().writeValueAsBytes(updatedUser);
		MockHttpServletResponse putResponse = entityRepositoryHelper.updateEntity(resourceRequest, updatedUserBytes);
		Assert.assertNotNull(putResponse);

		//Retrieve the previously updated user, and ensure the details were updated correctly.
		MockHttpServletResponse getUpdatedResponse = entityRepositoryHelper.getEntity(resourceRequest);
		String updatedContent = getUpdatedResponse.getContentAsString();
		JSONObject updatedJsonObject = (JSONObject) entityRepositoryHelper.getParser().parse(updatedContent);
		Assert.assertEquals(updatedJsonObject.get("userName"), updatedUser.getUserName());
		Assert.assertEquals(updatedJsonObject.get("firstName"), updatedUser.getFirstName());
		Assert.assertEquals(updatedJsonObject.get("lastName"), updatedUser.getLastName());
	}

	@Test
	public void testDeleteUserRequestWithCurrentUserPermission() throws Exception {
		String resourceRequest = entityRepositoryHelper.createUser("seventh.person", "Passw0rd", "Seventh", "Person", "");

		//Delete the created user.
		authenticate("seventh.person", "Passw0rd");
		MockHttpServletResponse deleteResponse = entityRepositoryHelper.deleteEntity(resourceRequest);
		Assert.assertNotNull(deleteResponse);

		//Ensure that the deleted user can't be retrieved from the database.
		entityRepositoryHelper.getEntity(resourceRequest, null, HttpStatus.NOT_FOUND);
	}

	@Test
	public void testDeleteUserRequestWithAdminPermission() throws Exception {
		String resourceRequest = entityRepositoryHelper.createUser("eighth.person", "Passw0rd", "Eighth", "Person", "");

		//Delete the created user.
		authenticate("Admin", "Passw0rd");
		MockHttpServletResponse deleteResponse = entityRepositoryHelper.deleteEntity(resourceRequest);
		Assert.assertNotNull(deleteResponse);

		//Ensure that the deleted user can't be retrieved from the database.
		entityRepositoryHelper.getEntity(resourceRequest, null, HttpStatus.NOT_FOUND);
	}

	@Test(expected = Exception.class)
	public void testDeleteUserRequestWithOtherPermission() throws Exception {
		String resourceRequest = entityRepositoryHelper.createUser("ninth.person", "Passw0rd", "Ninth", "Person", "");
		entityRepositoryHelper.createUser("tenth.person", "Passw0rd", "Tenth", "Person", "");

		//Delete the created user.
		authenticate("tenth.person", "Passw0rd");
		MockHttpServletResponse deleteResponse = entityRepositoryHelper.deleteEntity(resourceRequest);
		Assert.assertNotNull(deleteResponse);

		//Ensure that the deleted user can't be retrieved from the database.
		entityRepositoryHelper.getEntity(resourceRequest);
	}

	@Test
	public void testCreatedUserAssignedToUserGroup() throws Exception {
		String resourceRequest = entityRepositoryHelper.createUser("twelfth.person", "Passw0rd", "Twelfth", "Person", "");

		//Retrieve created user from the database.
		MockHttpServletResponse getResponse = entityRepositoryHelper.getEntity(resourceRequest);
		String content = getResponse.getContentAsString();
		JSONObject jsonObject = (JSONObject) entityRepositoryHelper.getParser().parse(content);

		//Get the groups link for the returned payload.
		JSONObject links = (JSONObject) jsonObject.get(EntityRepositoryHelper.LINKS);
		JSONObject groups = (JSONObject) links.get("groups");

		//Retrieve the groups link, and perform a request to get the information from the database.
		String groupsLink = groups.get(EntityRepositoryHelper.HREF).toString();
		MockHttpServletResponse getGroupResponse = entityRepositoryHelper.getEntity(groupsLink);
		String groupsContent = getGroupResponse.getContentAsString();
		JSONObject jsonGroupObject = (JSONObject) entityRepositoryHelper.getParser().parse(groupsContent);

		//Ensure that the response contains the name users.
		JSONArray groupsArray = ((JSONArray) ((JSONObject) (jsonGroupObject).get(EntityRepositoryHelper.EMBEDDED)).get("groups"));
		JSONObject groupContent = entityRepositoryHelper.getJsonObjectFromArray("name", "Users", groupsArray);

		Assert.assertNotNull(groupContent);
	}

	@Test
    public void testFindUserByUserName() throws Exception {
        entityRepositoryHelper.createUser("thirteenth.person", "Passw0rd", "Thirteenth", "Person", "");
        entityRepositoryHelper.createUser("fourteenth.person", "Passw0rd", "Fourteenth", "Person", "");

        //Retrieve created user from the database using search.
        Map<String, String> parameters = new HashMap<>();
        parameters.put("userName", "thirteenth.person");
        MockHttpServletResponse getResponse = entityRepositoryHelper.getEntity("/api/users/search/findByUserName", parameters);
        String content = getResponse.getContentAsString();
        Assert.assertNotNull(content);
        JSONObject sixPerson = (JSONObject) entityRepositoryHelper.getParser().parse(content);
        JSONArray userArray = (JSONArray)((JSONObject) sixPerson.get(EntityRepositoryHelper.EMBEDDED)).get("users");
        JSONObject sixthUserContent = entityRepositoryHelper.getJsonObjectFromArray("userName", "thirteenth.person", userArray);
        Assert.assertEquals(sixthUserContent.get("userName"), "thirteenth.person");

        //Retrieve created user from the database using search.
        parameters.clear();
        parameters.put("userName", "fourteenth.person");
        MockHttpServletResponse getSeventhResponse = entityRepositoryHelper.getEntity("/api/users/search/findByUserName", parameters);
        String seventhPersonContent = getSeventhResponse.getContentAsString();
        Assert.assertNotNull(seventhPersonContent);
        JSONObject seventhUser = (JSONObject) entityRepositoryHelper.getParser().parse(seventhPersonContent);
        userArray = (JSONArray)((JSONObject) seventhUser.get(EntityRepositoryHelper.EMBEDDED)).get("users");
        JSONObject seventhUserContent = entityRepositoryHelper.getJsonObjectFromArray("userName", "fourteenth.person", userArray);
        Assert.assertEquals(seventhUserContent.get("userName"), "fourteenth.person");
    }

	@Test
	public void testUserPasswordIsReturned() throws Exception {
		String resourceRequest = entityRepositoryHelper.createUser("fifteenth.person", "Passw0rd", "Fifteenth", "Person", "");

		MockHttpServletResponse getResponse = entityRepositoryHelper.getEntity(resourceRequest);
		String content = getResponse.getContentAsString();
		JSONObject jsonObject = (JSONObject) entityRepositoryHelper.getParser().parse(content);
		Assert.assertNotNull(jsonObject.get("password"));
	}
}
