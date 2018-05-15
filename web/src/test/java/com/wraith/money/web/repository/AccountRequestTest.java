package com.wraith.money.web.repository;

import java.util.HashMap;

import net.minidev.json.JSONObject;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;

import com.wraith.money.web.helper.EntityRepositoryHelper;

/**
 * User: rowan.massey Date: 18/04/13 Time: 22:00
 */
public class AccountRequestTest extends AbstractBaseIntegrationTests {

	@Test
	public void testCreateAccountRequest() throws Exception {
		authenticate("Admin", "Passw0rd");

		//Create a new account.
		String accountLocation = entityRepositoryHelper.createAccount("Account 1", 12.43, "Euro", "EUR");

		//Retrieve the inserted account record from the database, and ensure that values are correct.
		MockHttpServletResponse accountResponse = entityRepositoryHelper.getEntity(accountLocation);
		String content = accountResponse.getContentAsString();
		JSONObject accountContent = (JSONObject) entityRepositoryHelper.getParser().parse(content);

		Assert.assertEquals(accountContent.get("name"), "Account 1");
		Assert.assertEquals(accountContent.get("openingBalance"), 12.43);

		String accountCurrencyLocation = ((HashMap) ((HashMap) accountContent.get(EntityRepositoryHelper.LINKS)).get("currency"))
				.get(EntityRepositoryHelper.HREF).toString();

		MockHttpServletResponse accountCurrencyResponse = entityRepositoryHelper.getEntity(accountCurrencyLocation);
		String currencyContent = accountCurrencyResponse.getContentAsString();
		JSONObject currencyResponseObject = (JSONObject) entityRepositoryHelper.getParser().parse(currencyContent);

		Assert.assertTrue(currencyResponseObject.get("name").toString().equalsIgnoreCase("Euro"));
		Assert.assertTrue(currencyResponseObject.get("iso").toString().equalsIgnoreCase("EUR"));
	}

	@Test
	public void testUpdateAccountRequest() throws Exception {
		authenticate("Admin", "Passw0rd");

		String resourceRequest = entityRepositoryHelper.createAccount("Account 2", 12.43, "Euro", "EUR");

		JSONObject jsonObject = new JSONObject();
		jsonObject.put("name", "Updated Account 2");
		jsonObject.put("openingBalance", "1234.56");

		byte[] updatedAccountBytes = entityRepositoryHelper.getMapper().writeValueAsBytes(jsonObject);

		//Update the inserted account record from the database, and ensure that values are correct.
		MockHttpServletResponse putResponse = entityRepositoryHelper.updateEntity(resourceRequest, updatedAccountBytes);
		Assert.assertNotNull(putResponse);

		MockHttpServletResponse getResponse = entityRepositoryHelper.getEntity(resourceRequest);
		String content = getResponse.getContentAsString();
		JSONObject getJSONObject = (JSONObject) entityRepositoryHelper.getParser().parse(content);

		Assert.assertEquals(getJSONObject.get("name"), "Updated Account 2");
		Assert.assertEquals(getJSONObject.get("openingBalance"), 1234.56);
	}

	@Test(expected = Exception.class)
	public void testUpdateAccountRequestWithCurrentUser() throws Exception {
		String resourceRequest = entityRepositoryHelper.createAccount("Account 3", 12.43, "Euro", "EUR");
		entityRepositoryHelper.createUser("first.person", "Passw0rd", "first", "Person", "");

		JSONObject jsonObject = new JSONObject();
		jsonObject.put("name", "Updated Account 3");
		jsonObject.put("balance", "1234.56");

		byte[] updatedAccountBytes = entityRepositoryHelper.getMapper().writeValueAsBytes(jsonObject);

		//Update the inserted account record from the database, and ensure that values are correct.
		authenticate("first.person", "Passw0rd");
		MockHttpServletResponse putResponse = entityRepositoryHelper.updateEntity(resourceRequest, updatedAccountBytes);
		Assert.assertNotNull(putResponse);
	}

	@Test
	public void testDeleteAccountRequest() throws Exception {
		String resourceRequest = entityRepositoryHelper.createAccount("Account 4", 1245.67, "Euro", "EUR");

		//Delete the inserted account record from the database, and ensure that values are correct.
		authenticate("Admin", "Passw0rd");
		MockHttpServletResponse deleteResponse = entityRepositoryHelper.deleteEntity(resourceRequest);
		Assert.assertNotNull(deleteResponse);
		Assert.assertEquals(deleteResponse.getStatus(), HttpStatus.NO_CONTENT.value());

		//Ensure that the deleted account can't be retrieved from the database.
		entityRepositoryHelper.getEntity(resourceRequest, null, HttpStatus.NOT_FOUND);

	}

	@Test(expected = Exception.class)
	public void testDeleteAccountRequestWithCurrentUser() throws Exception {
		String resourceRequest = entityRepositoryHelper.createAccount("Account 5", 1245.67, "Euro", "EUR");
		entityRepositoryHelper.createUser("second.person", "Passw0rd", "second", "Person", "");

		//Delete the inserted account record from the database, and ensure that values are correct.
		authenticate("second.person", "Passw0rd");
		MockHttpServletResponse deleteResponse = entityRepositoryHelper.deleteEntity(resourceRequest);
		Assert.assertNotNull(deleteResponse);
	}

}
