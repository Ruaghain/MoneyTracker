package com.wraith.money.web.repository;

import net.minidev.json.JSONObject;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;

/**
 * User: rowan.massey
 * Date: 23/04/13
 * Time: 19:19
 */
public class AccountTypeRequestTest extends AbstractBaseIntegrationTests {

    @Test
    public void testCreateAccountTypeRequest() throws Exception {
        String resourceRequest = entityRepositoryHelper.createAccountType("Banking 1");

        //Retrieve the inserted account record from the database, and ensure that values are correct.
        authenticate("Admin", "Passw0rd");
        MockHttpServletResponse getResponse = entityRepositoryHelper.getEntity(resourceRequest);
        String content = getResponse.getContentAsString();
        JSONObject jsonObject = (JSONObject) entityRepositoryHelper.getParser().parse(content);
        Assert.assertEquals((String) jsonObject.get("name"), "Banking 1");
    }

    @Test
    public void testUpdateAccountTypeRequest() throws Exception {
        String resourceRequest = entityRepositoryHelper.createAccountType("Banking 2");

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", "Updated Banking 2");

        byte[] updatedAccountBytes = entityRepositoryHelper.getMapper().writeValueAsBytes(jsonObject);

        //Update the inserted account record from the database, and ensure that values are correct.
        authenticate("Admin", "Passw0rd");
        MockHttpServletResponse putResponse = entityRepositoryHelper.updateEntity(resourceRequest, updatedAccountBytes);
        Assert.assertNotNull(putResponse);

        MockHttpServletResponse getResponse = entityRepositoryHelper.getEntity(resourceRequest);
        String content = getResponse.getContentAsString();
        JSONObject getJSONObject = (JSONObject) entityRepositoryHelper.getParser().parse(content);

        Assert.assertEquals((String) getJSONObject.get("name"), "Updated Banking 2");
    }

    @Test(expected = Exception.class)
    public void testUpdateAccountRequestWithCurrentUser() throws Exception {
        String resourceRequest = entityRepositoryHelper.createAccountType("Banking 3");
		entityRepositoryHelper.createUser("first.person", "Passw0rd", "first", "Person", "");

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", "Updated Banking 3");

        byte[] updatedAccountBytes = entityRepositoryHelper.getMapper().writeValueAsBytes(jsonObject);

        //Update the inserted account record from the database, and ensure that values are correct.
        authenticate("first.person", "Passw0rd");
        MockHttpServletResponse putResponse = entityRepositoryHelper.updateEntity(resourceRequest, updatedAccountBytes);
        Assert.assertNotNull(putResponse);
    }

    @Test
    public void testDeleteAccountRequest() throws Exception {
        String resourceRequest = entityRepositoryHelper.createAccountType("Banking 4");

        //Delete the inserted account record from the database, and ensure that values are correct.
        authenticate("Admin", "Passw0rd");
        MockHttpServletResponse deleteResponse = entityRepositoryHelper.deleteEntity(resourceRequest);
        Assert.assertNotNull(deleteResponse);

        //Ensure that the deleted account can't be retrieved from the database.
        entityRepositoryHelper.getEntity(resourceRequest, null, HttpStatus.NOT_FOUND);
    }

    @Test(expected = Exception.class)
    public void testDeleteAccountRequestWithCurrentUser() throws Exception {
        String resourceRequest = entityRepositoryHelper.createAccountType("Banking 5");
		entityRepositoryHelper.createUser("second.person", "Passw0rd", "second", "Person", "");

        //Delete the inserted account record from the database, and ensure that values are correct.
        authenticate("second.person", "Passw0rd");
        MockHttpServletResponse deleteResponse = entityRepositoryHelper.deleteEntity(resourceRequest);
        Assert.assertNotNull(deleteResponse);
    }

}
