package com.wraith.money.web.repository;

import net.minidev.json.JSONObject;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;

/**
 * User: rowan.massey
 * Date: 11/05/13
 * Time: 23:07
 */
public class PayeeRequestTests extends AbstractBaseIntegrationTests {

    @Test(expected = Exception.class)
    public void testCreatePayeeWithNoAuthenticationRequest() throws Exception {
        authenticate("", "");
        String resourceRequest = entityRepositoryHelper.createPayee("Superquinn");
        entityRepositoryHelper.getEntity(resourceRequest);
    }

    @Test
    public void testCreatePayeeWithAdminUser() throws Exception {
        authenticate("Admin", "Passw0rd");
        String resourceLocation = entityRepositoryHelper.createPayee("Spar");
        MockHttpServletResponse getResponse = entityRepositoryHelper.getEntity(resourceLocation);
        String getContent = getResponse.getContentAsString();
        JSONObject getJSONObject = (JSONObject) entityRepositoryHelper.getParser().parse(getContent);
        Assert.assertEquals(getJSONObject.get("name"), "Spar");
    }

    @Test
    public void testCreatePayeeWithNormalUser() throws Exception {
		entityRepositoryHelper.createUser("sixtieth.person", "Passw0rd", "Sixtieth", "Person", "");
        authenticate("sixtieth.person", "Passw0rd");

        String resourceLocation = entityRepositoryHelper.createPayee("Tesco");
        MockHttpServletResponse getResponse = entityRepositoryHelper.getEntity(resourceLocation);
        String getContent = getResponse.getContentAsString();
        JSONObject getJSONObject = (JSONObject) entityRepositoryHelper.getParser().parse(getContent);
        Assert.assertEquals(getJSONObject.get("name"), "Tesco");
    }

    @Test
    public void testUpdatePayeeWithAdminUser() throws Exception {
        authenticate("Admin", "Passw0rd");

        String resourceLocation = entityRepositoryHelper.createPayee("Statoil");

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", "Updated Statoil");
        byte[] updatedGroupsBytes = entityRepositoryHelper.getMapper().writeValueAsBytes(jsonObject);

        //Retrieve the updated group record from the database, and ensure that values are correct.
        MockHttpServletResponse putResponse = entityRepositoryHelper.updateEntity(resourceLocation, updatedGroupsBytes);
        Assert.assertNotNull(putResponse);

        MockHttpServletResponse getResponse = entityRepositoryHelper.getEntity(resourceLocation);
        String getContent = getResponse.getContentAsString();
        JSONObject getJSONObject = (JSONObject) entityRepositoryHelper.getParser().parse(getContent);
		Assert.assertEquals(getJSONObject.get("name"), "Updated Statoil");

    }

    @Test
    public void testUpdatePayeeWithNormalUser() throws Exception {
		entityRepositoryHelper.createUser("sixtyfirst.person", "Passw0rd", "Sixty First", "Person", "");
        authenticate("sixtyfirst.person", "Passw0rd");

        String resourceLocation = entityRepositoryHelper.createPayee("Tesco");

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", "Updated Tesco");
        byte[] updatedGroupsBytes = entityRepositoryHelper.getMapper().writeValueAsBytes(jsonObject);

        //Retrieve the updated group record from the database, and ensure that values are correct.
        MockHttpServletResponse putResponse = entityRepositoryHelper.updateEntity(resourceLocation, updatedGroupsBytes);
        Assert.assertNotNull(putResponse);

        MockHttpServletResponse getResponse = entityRepositoryHelper.getEntity(resourceLocation);
        String getContent = getResponse.getContentAsString();
        JSONObject getJSONObject = (JSONObject) entityRepositoryHelper.getParser().parse(getContent);
		Assert.assertEquals(getJSONObject.get("name"), "Updated Tesco");
    }

    @Test
    public void testDeleteCategoryWithAdminUserRequest() throws Exception {
        authenticate("Admin", "Passw0rd");
        String resourceRequest = entityRepositoryHelper.createPayee("Art of Coffee");

        //Update the inserted category record from the database, and ensure that values are correct.
        MockHttpServletResponse putResponse = entityRepositoryHelper.deleteEntity(resourceRequest);
        Assert.assertNotNull(putResponse);

        entityRepositoryHelper.getEntity(resourceRequest, null, HttpStatus.NOT_FOUND);
    }

    @Test
    public void testDeleteCategoryWithNoParentForCurrentUserRequest() throws Exception {
		entityRepositoryHelper.createUser("sixtysecond.person", "Passw0rd", "Sixty Second", "Person", "");
        authenticate("sixtysecond.person", "Passw0rd");

        String resourceRequest = entityRepositoryHelper.createPayee("Starbucks");

        //Update the inserted category record from the database, and ensure that values are correct.
        MockHttpServletResponse putResponse = entityRepositoryHelper.deleteEntity(resourceRequest);
        Assert.assertNotNull(putResponse);

        entityRepositoryHelper.getEntity(resourceRequest, null, HttpStatus.NOT_FOUND);
    }
}
