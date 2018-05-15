package com.wraith.money.web.repository;

import com.wraith.money.data.entity.Authorities;
import net.minidev.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;

import java.util.HashSet;
import java.util.Set;

/**
 * User: rowan.massey
 * Date: 01/05/13
 * Time: 19:49
 */
public class AuthorityRequestTest extends AbstractBaseIntegrationTests {

    /**
     * This method returns a set of authorities, based on the number passed in.
     *
     * @param authorityNames An array of authority names.
     * @return A set containing the list of authorities passed in.
     */
    public static Set<Authorities> getNewAuthoritySet(String... authorityNames) {
        Set<Authorities> authorities = new HashSet<>();
        for (String authorityName : authorityNames) {
            Authorities authority = new Authorities();
            authority.setAuthority(authorityName);

            authorities.add(authority);
        }
        return authorities;
    }

    @Test(expected = Exception.class)
    public void testCreateAuthorityWithNoAuthenticationRequest() throws Exception {
        authenticate("", "");
        String resourceRequest = entityRepositoryHelper.createAuthority("ROLE_TESTER");
        entityRepositoryHelper.getEntity(resourceRequest);
    }

    @Test
    public void testCreateAuthorityRequest() throws Exception {
        authenticate("Admin", "Passw0rd");

        String resourceRequest = entityRepositoryHelper.createAuthority("ROLE_REPORTER");

        //Retrieve the inserted authority record from the database, and ensure that values are correct.
        MockHttpServletResponse getResponse = entityRepositoryHelper.getEntity(resourceRequest);
        String content = getResponse.getContentAsString();
        JSONObject jsonObject = (JSONObject) entityRepositoryHelper.getParser().parse(content);
        Assert.assertEquals((String) jsonObject.get("authority"), "ROLE_REPORTER");
    }

    @Test(expected = Exception.class)
    public void testCreateAuthorityWithOrdinaryUserRequest() throws Exception {
		entityRepositoryHelper.createUser("twentieth.person", "Passw0rd", "Twentieth", "Person", "");
        authenticate("twentieth.person", "Passw0rd");

        entityRepositoryHelper.createAuthority("ROLE_REPORTER");

    }

    @Test
    public void testUpdateAuthorityRequest() throws Exception {
        authenticate("Admin", "Passw0rd");

        String resourceRequest = entityRepositoryHelper.createAuthority("ROLE_DEVELOPER");

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("authority", "UPDATED_ROLE_DEVELOPER");

        byte[] updatedAccountBytes = entityRepositoryHelper.getMapper().writeValueAsBytes(jsonObject);

        //Update the inserted account record from the database, and ensure that values are correct.
        MockHttpServletResponse putResponse = entityRepositoryHelper.updateEntity(resourceRequest, updatedAccountBytes);
        Assert.assertNotNull(putResponse);

        MockHttpServletResponse getResponse = entityRepositoryHelper.getEntity(resourceRequest);
        String content = getResponse.getContentAsString();
        JSONObject getJSONObject = (JSONObject) entityRepositoryHelper.getParser().parse(content);

        Assert.assertEquals((String) getJSONObject.get("authority"), "UPDATED_ROLE_DEVELOPER");
    }

    @Test(expected = Exception.class)
    public void testUpdateAuthorityWithOrdinaryUserRequest() throws Exception {
        authenticate("Admin", "Passw0rd");
        String resourceRequest = entityRepositoryHelper.createAuthority("ROLE_DEVELOPER");

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("authority", "UPDATED_ROLE_DEVELOPER");

        byte[] updatedAccountBytes = entityRepositoryHelper.getMapper().writeValueAsBytes(jsonObject);

		entityRepositoryHelper.createUser("twentyfirst.person", "Passw0rd", "Twenty First", "Person", "");
        authenticate("twentyfirst.person", "Passw0rd");
        //Update the inserted account record from the database, and ensure that values are correct.
        entityRepositoryHelper.updateEntity(resourceRequest, updatedAccountBytes);
    }

    @Test
    public void testDeleteAuthorityRequest() throws Exception {
        authenticate("Admin", "Passw0rd");

        String resourceRequest = entityRepositoryHelper.createAuthority("ROLE_SUPPORTER");

        //Delete the inserted account record from the database, and ensure that values are correct.
        MockHttpServletResponse deleteResponse = entityRepositoryHelper.deleteEntity(resourceRequest);
        Assert.assertNotNull(deleteResponse);

        //Ensure that the deleted account can't be retrieved from the database.
        entityRepositoryHelper.getEntity(resourceRequest, null, HttpStatus.NOT_FOUND);
    }

    @Test(expected = Exception.class)
    public void testDeleteAuthorityWithOrdinaryUserRequest() throws Exception {
        authenticate("Admin", "Passw0rd");

        String resourceRequest = entityRepositoryHelper.createAuthority("ROLE_SUPPORTER");

		entityRepositoryHelper.createUser("twentysecond.person", "Passw0rd", "Twenty Second", "Person", "");
        authenticate("twentysecond.person", "Passw0rd");

        //Delete the inserted account record from the database, and ensure that values are correct.
        entityRepositoryHelper.deleteEntity(resourceRequest);
    }
}
