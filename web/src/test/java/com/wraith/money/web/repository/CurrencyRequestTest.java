package com.wraith.money.web.repository;

import net.minidev.json.JSONObject;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;

/**
 * This class tests all of the Currency requests.
 * <p/>
 * User: rowan.massey
 * Date: 06/05/13
 * Time: 11:44
 */
public class CurrencyRequestTest extends AbstractBaseIntegrationTests {

    @Test(expected = Exception.class)
    public void testCreateCurrencyWithNoAuthenticationRequest() throws Exception {
        authenticate("", "");
        String resourceRequest = entityRepositoryHelper.createCurrency("GBP", "British Pound");
        entityRepositoryHelper.getEntity(resourceRequest);
    }

    @Test
    public void testCreateCurrencyRequest() throws Exception {
        authenticate("Admin", "Passw0rd");

        String resourceRequest = entityRepositoryHelper.createCurrency("EUR", "Euro");

        //Retrieve the inserted authority record from the database, and ensure that values are correct.
        MockHttpServletResponse getResponse = entityRepositoryHelper.getEntity(resourceRequest);
        String content = getResponse.getContentAsString();
        JSONObject jsonObject = (JSONObject) entityRepositoryHelper.getParser().parse(content);
        Assert.assertEquals(jsonObject.get("name"), "Euro");
    }

    @Test(expected = Exception.class)
    public void testCreateCurrencyWithOrdinaryUserRequest() throws Exception {
		entityRepositoryHelper.createUser("fourtieth.person", "Passw0rd", "Fourtieth", "Person", "");
        authenticate("fourtieth.person", "Passw0rd");

        entityRepositoryHelper.createCurrency("USD", "Dollar");
    }

    @Test
    public void testUpdateCountryRequest() throws Exception {
        authenticate("Admin", "Passw0rd");

        String resourceRequest = entityRepositoryHelper.createCurrency("ZAR", "South African Rand");

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", "Updated Rand");

        byte[] updatedCountryBytes = entityRepositoryHelper.getMapper().writeValueAsBytes(jsonObject);

        //Update the inserted currency record from the database, and ensure that values are correct.
        MockHttpServletResponse putResponse = entityRepositoryHelper.updateEntity(resourceRequest, updatedCountryBytes);
        Assert.assertNotNull(putResponse);

        MockHttpServletResponse getResponse = entityRepositoryHelper.getEntity(resourceRequest);
        String content = getResponse.getContentAsString();
        JSONObject getJSONObject = (JSONObject) entityRepositoryHelper.getParser().parse(content);

		Assert.assertEquals(getJSONObject.get("name"), "Updated Rand");
    }

    @Test(expected = Exception.class)
    public void testUpdateCountryWithOrdinaryUserRequest() throws Exception {
        authenticate("Admin", "Passw0rd");

        String resourceRequest = entityRepositoryHelper.createCurrency("CAD", "Canadian Dollar");

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", "Updated Canadian Dollar");

        byte[] updatedCountryBytes = entityRepositoryHelper.getMapper().writeValueAsBytes(jsonObject);

		entityRepositoryHelper.createUser("fourtyfirst.person", "Passw0rd", "Fourty First", "Person", "");
        authenticate("fourtyfirst.person", "Passw0rd");
        //Update the inserted country record from the database, and ensure that values are correct.
        entityRepositoryHelper.updateEntity(resourceRequest, updatedCountryBytes);
    }

    @Test
    public void testDeleteCountryRequest() throws Exception {
        authenticate("Admin", "Passw0rd");

        String resourceRequest = entityRepositoryHelper.createCurrency("DKK", "Danish Krone");

        //Delete the inserted account record from the database, and ensure that values are correct.
        MockHttpServletResponse deleteResponse = entityRepositoryHelper.deleteEntity(resourceRequest);
        Assert.assertNotNull(deleteResponse);
        Assert.assertEquals(deleteResponse.getStatus(), HttpStatus.NO_CONTENT.value());

        //Ensure that the deleted account can't be retrieved from the database.
        entityRepositoryHelper.getEntity(resourceRequest, null, HttpStatus.NOT_FOUND);
    }

    @Test(expected = Exception.class)
    public void testDeleteCountryWithOrdinaryUserRequest() throws Exception {
		entityRepositoryHelper.createUser("fourtysecond.person", "Passw0rd", "Fourty Second", "Person", "");

        authenticate("Admin", "Passw0rd");
        String resourceRequest = entityRepositoryHelper.createCurrency("NZD", "New Zealand Dollar");

        authenticate("fourtysecond.person", "Passw0rd");
        //Delete the inserted account record from the database, and ensure that values are correct.
        MockHttpServletResponse deleteResponse = entityRepositoryHelper.deleteEntity(resourceRequest);
        Assert.assertNotNull(deleteResponse);

        //Ensure that the deleted account can't be retrieved from the database.
        entityRepositoryHelper.getEntity(resourceRequest);
    }
}
