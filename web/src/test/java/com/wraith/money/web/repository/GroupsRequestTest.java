package com.wraith.money.web.repository;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;

import com.wraith.money.web.helper.EntityRepositoryHelper;

/**
 * This class tests all the permatations of the Group requests.
 * <p/>
 * User: rowan.massey
 * Date: 06/05/13
 * Time: 13:17
 */
public class GroupsRequestTest extends AbstractBaseIntegrationTests {

    @Test(expected = Exception.class)
    public void testCreateGroupsWithNoAuthenticationRequest() throws Exception {
        authenticate("", "");
        String resourceRequest = entityRepositoryHelper.createGroup("Reporters", null);
        entityRepositoryHelper.getEntity(resourceRequest);
    }

    @Test
    public void testCreateGroupsRequest() throws Exception {
        authenticate("Admin", "Passw0rd");

        String resourceRequest = entityRepositoryHelper.createGroup("Developers", AuthorityRequestTest.getNewAuthoritySet("ROLE_DEVELOPER", "ROLE_REPORTER"));

        //Retrieve the inserted group record from the database, and ensure that values are correct.
        MockHttpServletResponse getResponse = entityRepositoryHelper.getEntity(resourceRequest);
        String content = getResponse.getContentAsString();
        JSONObject jsonObject = (JSONObject) entityRepositoryHelper.getParser().parse(content);
        Assert.assertEquals(jsonObject.get("name"), "Developers");

        //Get the groups link for the returned payload.
        JSONObject links = (JSONObject) jsonObject.get(EntityRepositoryHelper.LINKS);
        JSONObject authorities = (JSONObject) links.get(EntityRepositoryHelper.AUTHORITIES);

        //Retrieve the groups link, and perform a request to get the information from the database.
        String authoritiesLink = authorities.get(EntityRepositoryHelper.HREF).toString();
        MockHttpServletResponse getGroupResponse = entityRepositoryHelper.getEntity(authoritiesLink);
        String groupsContent = getGroupResponse.getContentAsString();
        JSONObject jsonGroupObject = (JSONObject) entityRepositoryHelper.getParser().parse(groupsContent);

        //Ensure that the response contains the name users.
        JSONObject data = (JSONObject) jsonGroupObject.get(EntityRepositoryHelper.EMBEDDED);
        JSONObject reporterAuthority = entityRepositoryHelper.getJsonObjectFromArray("authority", "ROLE_REPORTER", (JSONArray) data.get(EntityRepositoryHelper.AUTHORITIES));
        JSONObject developerAuthority = entityRepositoryHelper.getJsonObjectFromArray("authority", "ROLE_DEVELOPER", (JSONArray) data.get(EntityRepositoryHelper.AUTHORITIES));

        Assert.assertNotNull(developerAuthority);
        Assert.assertNotNull(reporterAuthority);
    }

    @Test(expected = Exception.class)
    public void testCreateGroupsWithOrdinaryUserRequest() throws Exception {
		entityRepositoryHelper.createUser("fiftieth.person", "Passw0rd", "Fiftieth", "Person", "");
        authenticate("fiftieth.person", "Passw0rd");

        entityRepositoryHelper.createGroup("Quality Assurance", null);
    }

    @Test
    public void testUpdateGroupsRequest() throws Exception {
        authenticate("Admin", "Passw0rd");

        String resourceRequest = entityRepositoryHelper.createGroup("Testers", AuthorityRequestTest.getNewAuthoritySet("ROLE_TESTER"));

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", "Updated Testers");
        byte[] updatedGroupsBytes = entityRepositoryHelper.getMapper().writeValueAsBytes(jsonObject);

        //Retrieve the updated group record from the database, and ensure that values are correct.
        MockHttpServletResponse putResponse = entityRepositoryHelper.updateEntity(resourceRequest, updatedGroupsBytes);
        Assert.assertNotNull(putResponse);

        MockHttpServletResponse getResponse = entityRepositoryHelper.getEntity(resourceRequest);
        String getContent = getResponse.getContentAsString();
        JSONObject getJSONObject = (JSONObject) entityRepositoryHelper.getParser().parse(getContent);
        Assert.assertEquals(getJSONObject.get("name"), "Updated Testers");
    }

    @Test(expected = Exception.class)
    public void testUpdateGroupsWithOrdinaryUserRequest() throws Exception {
        authenticate("Admin", "Passw0rd");

        String resourceRequest = entityRepositoryHelper.createGroup("Marketing", AuthorityRequestTest.getNewAuthoritySet("ROLE_MARKETING"));

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", "Updated Marketing");
        byte[] updatedGroupsBytes = entityRepositoryHelper.getMapper().writeValueAsBytes(jsonObject);

		entityRepositoryHelper.createUser("fiftyfirst.person", "Passw0rd", "Fifty First", "Person", "");
        authenticate("fiftyfirst.person", "Passw0rd");

        //Retrieve the updated group record from the database, and ensure that values are correct.
        entityRepositoryHelper.updateEntity(resourceRequest, updatedGroupsBytes);
    }

    @Test
    public void testDeleteCategoryWithAdminUserRequest() throws Exception {
        authenticate("Admin", "Passw0rd");
        String resourceRequest = entityRepositoryHelper.createGroup("Publishers", AuthorityRequestTest.getNewAuthoritySet("ROLE_PUBLISHERS"));

        //Update the inserted category record from the database, and ensure that values are correct.
        MockHttpServletResponse putResponse = entityRepositoryHelper.deleteEntity(resourceRequest);
        Assert.assertNotNull(putResponse);

        entityRepositoryHelper.getEntity(resourceRequest, null, HttpStatus.NOT_FOUND);
    }

    @Test(expected = Exception.class)
    public void testDeleteCategoryWithNoParentForCurrentUserRequest() throws Exception {
		entityRepositoryHelper.createUser("fiftysecond.person", "Passw0rd", "Fifty Second", "Person", "");
        authenticate("fiftysecond.person", "Passw0rd");

        String resourceRequest = entityRepositoryHelper.createGroup("Editors", AuthorityRequestTest.getNewAuthoritySet("ROLE_EDITORS"));

        //Update the inserted category record from the database, and ensure that values are correct.
        MockHttpServletResponse putResponse = entityRepositoryHelper.deleteEntity(resourceRequest);
        Assert.assertNotNull(putResponse);

        entityRepositoryHelper.getEntity(resourceRequest);
    }

}
