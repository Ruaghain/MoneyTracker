package com.wraith.money.web.repository;

import net.minidev.json.JSONObject;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;

/**
 * User: rowan.massey
 * Date: 05/05/13
 * Time: 21:07
 */
public class CountryRequestTest extends AbstractBaseIntegrationTests {

    @Test(expected = Exception.class)
    public void testCreateCountryWithNoAuthenticationRequest() throws Exception {
        authenticate("", "");
        String resourceRequest = entityRepositoryHelper.createCountry("IRE", "Ireland");
        entityRepositoryHelper.getEntity(resourceRequest);
    }

    @Test
    public void testCreateCountryRequest() throws Exception {
        authenticate("Admin", "Passw0rd");

        String resourceRequest = entityRepositoryHelper.createCountry("UK", "United Kingdom");

        //Retrieve the inserted authority record from the database, and ensure that values are correct.
        MockHttpServletResponse getResponse = entityRepositoryHelper.getEntity(resourceRequest);
        String content = getResponse.getContentAsString();
        JSONObject jsonObject = (JSONObject) entityRepositoryHelper.getParser().parse(content);
		Assert.assertEquals(jsonObject.get("name"), "United Kingdom");
    }

    @Test(expected = Exception.class)
    public void testCreateCountryWithOrdinaryUserRequest() throws Exception {
		entityRepositoryHelper.createUser("thirtieth.person", "Passw0rd", "Thirtieth", "Person", "");
        authenticate("thirtieth.person", "Passw0rd");

        entityRepositoryHelper.createCountry("FR", "France");
    }

    @Test
    public void testUpdateCountryRequest() throws Exception {
        authenticate("Admin", "Passw0rd");

        String resourceRequest = entityRepositoryHelper.createCountry("DE", "Germany");

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", "Updated Germany");

        byte[] updatedCountryBytes = entityRepositoryHelper.getMapper().writeValueAsBytes(jsonObject);

        //Update the inserted country record from the database, and ensure that values are correct.
        MockHttpServletResponse putResponse = entityRepositoryHelper.updateEntity(resourceRequest, updatedCountryBytes);
        Assert.assertNotNull(putResponse);

        MockHttpServletResponse getResponse = entityRepositoryHelper.getEntity(resourceRequest);
        String content = getResponse.getContentAsString();
        JSONObject getJSONObject = (JSONObject) entityRepositoryHelper.getParser().parse(content);

		Assert.assertEquals(getJSONObject.get("name"), "Updated Germany");
    }

    @Test(expected = Exception.class)
    public void testUpdateCountryWithOrdinaryUserRequest() throws Exception {
        authenticate("Admin", "Passw0rd");

        String resourceRequest = entityRepositoryHelper.createCountry("US", "United States of America");

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", "Updated United States of America");

        byte[] updatedCountryBytes = entityRepositoryHelper.getMapper().writeValueAsBytes(jsonObject);

		entityRepositoryHelper.createUser("thirtyfirst.person", "Passw0rd", "Thirty First", "Person", "");
        authenticate("thirtyfirst.person", "Passw0rd");
        //Update the inserted country record from the database, and ensure that values are correct.
        entityRepositoryHelper.updateEntity(resourceRequest, updatedCountryBytes);
    }

    @Test
    public void testDeleteCountryRequest() throws Exception {
        authenticate("Admin", "Passw0rd");

        String resourceRequest = entityRepositoryHelper.createCountry("CA", "Canada");

        //Delete the inserted account record from the database, and ensure that values are correct.
        MockHttpServletResponse deleteResponse = entityRepositoryHelper.deleteEntity(resourceRequest);
        Assert.assertNotNull(deleteResponse);

        //Ensure that the deleted account can't be retrieved from the database.
        entityRepositoryHelper.getEntity(resourceRequest, null, HttpStatus.NOT_FOUND);
    }

    @Test(expected = Exception.class)
    public void testDeleteCountryWithOrdinaryUserRequest() throws Exception {
		entityRepositoryHelper.createUser("thirtysecond.person", "Passw0rd", "Thirty Second", "Person", "");

        authenticate("Admin", "Passw0rd");
        String resourceRequest = entityRepositoryHelper.createCountry("AU", "Australia");

        authenticate("thirtysecond.person", "Passw0rd");
        //Delete the inserted account record from the database, and ensure that values are correct.
        MockHttpServletResponse deleteResponse = entityRepositoryHelper.deleteEntity(resourceRequest);
        Assert.assertNotNull(deleteResponse);

        //Ensure that the deleted account can't be retrieved from the database.
        entityRepositoryHelper.getEntity(resourceRequest);
    }
}
