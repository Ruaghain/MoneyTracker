package com.wraith.money.web.repository;

import com.wraith.money.web.helper.EntityRepositoryHelper;
import net.minidev.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;

/**
 * This class tests all of the category requests for all types of users.
 * <p/>
 * User: rowan.massey
 * Date: 01/05/13
 * Time: 20:40
 */
public class CategoryRequestTest extends AbstractBaseIntegrationTests {

    private static final String PARENT_CATEGORY = "parentCategory";

    @Test
    public void testCreateCategoryWithNoParentRequest() throws Exception {

        String resourceRequest = entityRepositoryHelper.createCategory("Groceries", null);

        //Retrieve the inserted authority record from the database, and ensure that values are correct.
        MockHttpServletResponse getResponse = entityRepositoryHelper.getEntity(resourceRequest);
        String content = getResponse.getContentAsString();
        JSONObject jsonObject = (JSONObject) entityRepositoryHelper.getParser().parse(content);
        Assert.assertEquals(jsonObject.get("name"), "Groceries");
    }

    @Test
    public void testUpdateCategoryWithNoParentForAdminUserRequest() throws Exception {
        String resourceRequest = entityRepositoryHelper.createCategory("Banking", null);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", "Updated Banking");

        byte[] updatedAccountBytes = entityRepositoryHelper.getMapper().writeValueAsBytes(jsonObject);

        authenticate("Admin", "Passw0rd");
        //Update the inserted category record from the database, and ensure that values are correct.
        MockHttpServletResponse putResponse = entityRepositoryHelper.updateEntity(resourceRequest, updatedAccountBytes);
        Assert.assertNotNull(putResponse);

        MockHttpServletResponse getResponse = entityRepositoryHelper.getEntity(resourceRequest);
        String content = getResponse.getContentAsString();
        JSONObject getJSONObject = (JSONObject) entityRepositoryHelper.getParser().parse(content);

        Assert.assertEquals(getJSONObject.get("name"), "Updated Banking");
    }

    @Test(expected = Exception.class)
    public void testUpdateCategoryWithNoParentForCurrentUserRequest() throws Exception {

        String resourceRequest = entityRepositoryHelper.createCategory("Revenue", null);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", "Updated Revenue");

        byte[] updatedAccountBytes = entityRepositoryHelper.getMapper().writeValueAsBytes(jsonObject);

        authenticate("seventeenth.person", "Passw0rd");
        //Update the inserted category record from the database, and ensure that values are correct.
        MockHttpServletResponse putResponse = entityRepositoryHelper.updateEntity(resourceRequest, updatedAccountBytes);
    }

    @Test
    public void testDeleteCategoryWithNoParentForAdminUserRequest() throws Exception {

        String resourceRequest = entityRepositoryHelper.createCategory("Computer", null);

        authenticate("Admin", "Passw0rd");
        //Update the inserted category record from the database, and ensure that values are correct.
        MockHttpServletResponse putResponse = entityRepositoryHelper.deleteEntity(resourceRequest);
        Assert.assertNotNull(putResponse);

        entityRepositoryHelper.getEntity(resourceRequest, null, HttpStatus.NOT_FOUND);
    }

    @Test(expected = Exception.class)
    public void testDeleteCategoryWithNoParentForCurrentUserRequest() throws Exception {
        String resourceRequest = entityRepositoryHelper.createCategory("Computer", null);

        authenticate("seventeenth.person", "Passw0rd");
        //Update the inserted category record from the database, and ensure that values are correct.
        MockHttpServletResponse putResponse = entityRepositoryHelper.deleteEntity(resourceRequest);
        Assert.assertNotNull(putResponse);

        entityRepositoryHelper.getEntity(resourceRequest);
    }

    @Test
    public void testCreateCategoryWithParentRequest() throws Exception {

        String resourceRequest = entityRepositoryHelper.createCategory("Toothpaste", "Personal Care");

        //Retrieve the inserted authority record from the database, and ensure that values are correct.
        MockHttpServletResponse getResponse = entityRepositoryHelper.getEntity(resourceRequest);
        String content = getResponse.getContentAsString();
        JSONObject jsonObject = (JSONObject) entityRepositoryHelper.getParser().parse(content);
        Assert.assertEquals(jsonObject.get("name"), "Toothpaste");

        //Get the category link for the returned payload.
        JSONObject links = (JSONObject) jsonObject.get(EntityRepositoryHelper.LINKS);
        JSONObject parentCategory = (JSONObject) links.get(PARENT_CATEGORY);

        //Retrieve the parent category link, and perform a request to get the information from the database.
        String categoryParentLink = parentCategory.get(EntityRepositoryHelper.HREF).toString();
        MockHttpServletResponse getCategoryResponse = entityRepositoryHelper.getEntity(categoryParentLink);
        JSONObject parentCategoryObject = (JSONObject) entityRepositoryHelper.getParser().parse(getCategoryResponse.getContentAsString());

        Assert.assertEquals("Personal Care", parentCategoryObject.get("name"));
    }

}
