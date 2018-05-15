package com.wraith.money.web.repository;

import net.minidev.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;

/**
 * User: rowan.massey Date: 30/04/13 Time: 18:27
 */
public class AddressRequestTest extends AbstractBaseIntegrationTests {

    @Test(expected = Exception.class)
    public void testCreateAddressWithNoAuthenticationRequest() throws Exception {
        authenticate("", "");
        String resourceRequest = entityRepositoryHelper.createAddress("Address 1", "Address 2", "Dublin", "Dublin", "Ireland", "IRE");
        entityRepositoryHelper.getEntity(resourceRequest);
    }

    @Test
    public void testCreateAddressRequest() throws Exception {
        authenticate("Admin", "Passw0rd");

        String resourceRequest = entityRepositoryHelper.createAddress("Address 1", "Address 2", "Belfast", "Antrim", "Northern Ireland", "NI");
        //Retrieve the inserted account record from the database, and ensure that values are correct.
        MockHttpServletResponse getResponse = entityRepositoryHelper.getEntity(resourceRequest);
        String content = getResponse.getContentAsString();
        JSONObject jsonObject = (JSONObject) entityRepositoryHelper.getParser().parse(content);
        Assert.assertEquals(jsonObject.get("address1"), "Address 1");
        Assert.assertEquals(jsonObject.get("address2"), "Address 2");
        Assert.assertEquals(jsonObject.get("city"), "Belfast");
    }

    @Test
    public void testUpdateAccountTypeRequest() throws Exception {
        authenticate("Admin", "Passw0rd");

        String resourceRequest = entityRepositoryHelper.createAddress("Address 1", "Address 2", "Cork", "Cork", "Ireland", "IRE");

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("address1", "Updated Address 1");

        byte[] updatedAccountBytes = entityRepositoryHelper.getMapper().writeValueAsBytes(jsonObject);

        //Update the inserted account record from the database, and ensure that values are correct.
        MockHttpServletResponse putResponse = entityRepositoryHelper.updateEntity(resourceRequest, updatedAccountBytes);
        Assert.assertNotNull(putResponse);

        MockHttpServletResponse getResponse = entityRepositoryHelper.getEntity(resourceRequest);
        String content = getResponse.getContentAsString();
        JSONObject getJSONObject = (JSONObject) entityRepositoryHelper.getParser().parse(content);

        Assert.assertEquals(getJSONObject.get("address1"), "Updated Address 1");
    }

    @Test
    public void testDeleteAccountRequest() throws Exception {
        authenticate("Admin", "Passw0rd");

        String resourceRequest = entityRepositoryHelper.createAddress("Address 1", "Address 2", "Galway", "Galway", "Ireland", "IRE");

        //Delete the inserted account record from the database, and ensure that values are correct.
        MockHttpServletResponse deleteResponse = entityRepositoryHelper.deleteEntity(resourceRequest);
        Assert.assertNotNull(deleteResponse);

        //Ensure that the deleted account can't be retrieved from the database.
        entityRepositoryHelper.getEntity(resourceRequest, null, HttpStatus.NOT_FOUND);
    }

}
