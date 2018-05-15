package com.wraith.money.web.helper;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.junit.Assert;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import com.wraith.money.web.repository.AbstractBaseIntegrationTests;

/**
 * User: rowan.massey Date: 17/10/2014 Time: 23:53
 */
public class RequestHelper {

	public static final String BASE_URI = "/api";
	protected RequestMappingHandlerAdapter handlerAdapter;
	private RequestMappingHandlerMapping handlerMapping;

	public RequestHelper(RequestMappingHandlerMapping handlerMapping, RequestMappingHandlerAdapter handlerAdapter) {
		this.handlerMapping = handlerMapping;
		this.handlerAdapter = handlerAdapter;
	}

	/**
	 * This method strips the 'http://localhost' from the provided uri and returns just the resource request. e.g. /user/1
	 *
	 * @param uri
	 *            The uri from which to remove the localhost string.
	 * @return A resource uri. e.g. /user/1
	 */
	private String getResourceURI(String uri) {
		String LOCALHOST = "http://localhost";
		return StringUtils.remove(uri, LOCALHOST);
	}

	/**
	 * This method performs a GET request to the server to retrieve records, along with the associated parameters - if any.
	 *
	 * @param uri
	 *            The uri on which the request will be performed.
	 * @param parameters
	 *            The parameters that will get passed to the request.
	 * @return The response object from the server.
	 */
	public MockHttpServletResponse performGetRequest(String uri, Map parameters) throws Exception {
		return performGetRequest(uri, parameters, HttpStatus.OK);
	}

	/**
	 * This method performs a GET request to the server to retrieve records, along with the associated parameters - if any.
	 *
	 * @param uri
	 *            The uri on which the request will be performed.
	 * @param parameters
	 *            The parameters that will get passed to the request.
	 * @return The response object from the server.
	 */
	public MockHttpServletResponse performGetRequest(String uri, Map parameters, HttpStatus expectedStatus) throws Exception {
		if (!uri.contains(BASE_URI)) {
			uri = BASE_URI.concat(uri);
		}

		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();

		uri = getResourceURI(uri);

		request.setMethod("GET");
		request.setRequestURI(uri);

		if (parameters != null) {
			request.setParameters(parameters);
		}

		Object handler = handlerMapping.getHandler(request).getHandler();
		handlerAdapter.handle(request, response, handler);
		Assert.assertEquals(expectedStatus.value(), response.getStatus());
		return response;
	}

	/**
	 * This method performs a GET request to the server to retrieve records.
	 *
	 * @param uri
	 *            The uri on which the request will be performed.
	 * @return The response object from the server.
	 */
	public MockHttpServletResponse performGetRequest(String uri) throws Exception {
		return performGetRequest(uri, null);
	}

	/**
	 * This method performs a POST request to the server to create records.
	 *
	 * @param uri
	 *            The uri to which the content will be posted to.
	 * @param content
	 *            A byte array of the contents of the request to send to the server.
	 * @return The response from the server.
	 */
	public MockHttpServletResponse performPostRequest(String uri, byte[] content) throws Exception {
		if (!uri.contains(BASE_URI)) {
			uri = BASE_URI.concat(uri);
		}

		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();

		uri = getResourceURI(uri);

		request.setMethod("POST");
		request.setRequestURI(uri);
		request.setContentType("application/hal+json");
		request.setContent(content);

		Object handler = handlerMapping.getHandler(request).getHandler();
		handlerAdapter.handle(request, response, handler);
		Assert.assertEquals(response.getStatus(), HttpStatus.CREATED.value());
		return response;
	}

	/**
	 * This method performs a POST request to the server to create records.
	 *
	 * @param uri
	 *            The uri to which the content will be posted to.
	 * @param content
	 *            A byte array of the contents of the request to send to the server.
	 * @return The response from the server.
	 */
	public MockHttpServletResponse performPutRequest(String uri, byte[] content) throws Exception {
		if (!uri.contains(BASE_URI)) {
			uri = BASE_URI.concat(uri);
		}

		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();

		uri = getResourceURI(uri);

		request.setMethod("PUT");
		request.setRequestURI(uri);
		request.setContentType(AbstractBaseIntegrationTests.CONTENT_TYPE);
		request.setContent(content);

		Object handler = handlerMapping.getHandler(request).getHandler();
		handlerAdapter.handle(request, response, handler);
		Assert.assertEquals(response.getStatus(), HttpStatus.NO_CONTENT.value());
		return response;
	}

	/**
	 * This method performs a POST request to the server to create records.
	 *
	 * @param uri
	 *            The uri to which the content will be posted to.
	 * @param content
	 *            A byte array of the contents of the request to send to the server.
	 * @param contentType
	 *            This is the content type of the request.
	 * @return The response from the server.
	 */
	public MockHttpServletResponse performPutRequest(String uri, String contentType, byte[] content) throws Exception {
		if (!uri.contains(BASE_URI)) {
			uri = BASE_URI.concat(uri);
		}

		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();

		uri = getResourceURI(uri);

		request.setMethod("PUT");
		request.setRequestURI(uri);
		request.setContentType(contentType);
		request.setContent(content);

		Object handler = handlerMapping.getHandler(request).getHandler();
		handlerAdapter.handle(request, response, handler);
		Assert.assertEquals(response.getStatus(), HttpStatus.NO_CONTENT.value());
		return response;
	}

	/**
	 * This method performs a DELETE request to the server to delete a specific record.
	 *
	 * @param uri
	 *            The uri to which the content will be posted to.
	 * @return The response from the server.
	 */
	public MockHttpServletResponse performDeleteRequest(String uri) throws Exception {
		if (!uri.contains(BASE_URI)) {
			uri = BASE_URI.concat(uri);
		}

		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();

		uri = getResourceURI(uri);

		request.setMethod("DELETE");
		request.setRequestURI(uri);

		Object handler = handlerMapping.getHandler(request).getHandler();
		handlerAdapter.handle(request, response, handler);
		Assert.assertEquals(response.getStatus(), HttpStatus.NO_CONTENT.value());
		return response;
	}
}
