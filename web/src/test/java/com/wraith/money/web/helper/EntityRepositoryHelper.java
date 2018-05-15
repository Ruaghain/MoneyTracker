package com.wraith.money.web.helper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wraith.money.data.BaseEntity;
import com.wraith.money.data.entity.*;
import com.wraith.money.web.dto.TransactionDto;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import org.junit.Assert;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * User: rowan.massey Date: 17/10/2014 Time: 23:45
 */
public class EntityRepositoryHelper {

	public static final String ACCOUNTS_PATH = "accounts";
	public static final String USERS_PATH = "users";
	public static final String CURRENCY_PATH = "currencies";
	public static final String ACCOUNT_TYPES_PATH = "accountTypes";
	public static final String COUNTRIES_PATH = "countries";
	public static final String ADDRESSES_PATH = "addresses";
	public static final String AUTHORITIES_PATH = "authorities";
	public static final String CATEGORIES_PATH = "categories";
	public static final String TRANSACTIONS_PATH = "transactions";
	public static final String GROUPS_PATH = "groups";
	public static final String PAYEES_PATH = "payees";
	public static final String LINKS = "_links";
	public static final String HREF = "href";
	public static final String EMBEDDED = "_embedded";
	public static final String PARENT_CATEGORY = "parentCategory";
	public static final String AUTHORITIES = "authorities";
	public static final String SELF = "self";

	private ObjectMapper mapper;
	private JSONParser parser;
	private RequestHelper requestHelper;

	/**
	 * @param handlerMapping
	 * @param handlerAdapter
	 */
	public EntityRepositoryHelper(RequestMappingHandlerMapping handlerMapping, RequestMappingHandlerAdapter handlerAdapter) {
		this.mapper = new ObjectMapper();
		this.parser = new JSONParser(JSONParser.DEFAULT_PERMISSIVE_MODE);
		this.requestHelper = new RequestHelper(handlerMapping, handlerAdapter);
	}

	/**
	 * This method creates a new instance of a transaction for the given details.
	 *
	 * @param amount
	 *            The name of the account to which this transaction belongs.
	 * @param chequeNumber
	 *            The transactions cheque number - if any.
	 * @param notes
	 *            Any extra notes associated with the transaction.
	 * @param quantity
	 *            The quantity of the transaction.
	 * @return The URI to the created transaction.
	 * @throws Exception
	 */
	public static Transaction getNewTransaction(Double amount, String chequeNumber, String notes, Integer quantity) {
		Transaction transaction = new Transaction();
		transaction.setAmount(amount);
		transaction.setCheckNumber(chequeNumber);
		transaction.setNotes(notes);
		transaction.setNumber(0);
		transaction.setQuantity(quantity);
		transaction.setTransactionDate(Calendar.getInstance().getTime());

		return transaction;
	}

	/**
	 * This method creates and instance of the address class, with default values.
	 *
	 * @param address1
	 *            The first address line.
	 * @param address2
	 *            The second address line.
	 * @param city
	 *            The addresses city.
	 * @param county
	 *            The addresses county.
	 * @return an instance of the created Address object.
	 */
	private Address getAddress(String address1, String address2, String city, String county) {
		Address address = new Address();
		address.setAddress1(address1);
		address.setAddress2(address2);
		address.setCity(city);
		address.setCounty(county);
		return address;
	}

	public JSONParser getParser() {
		return parser;
	}

	public ObjectMapper getMapper() {
		return mapper;
	}

	/**
	 * This method creates a new instance of the account object.
	 *
	 * @param name
	 *            The name of the account.
	 * @param balance
	 *            The account balance.
	 * @return An instance of the account object.
	 */
	private Account getAccount(String name, Double balance) {
		Account account = new Account();
		account.setName(name);
		account.setOpeningBalance(balance);
		account.setOpeningDate(Calendar.getInstance().getTime());
		return account;
	}

	/**
	 * This method creates a new account, and returns the accounts reference URI
	 *
	 * @param name
	 *            The name of the account.
	 * @param balance
	 *            The account balance.
	 * @return The reference URI for the created account.
	 * @throws Exception
	 */
	public String createAccount(String name, Double balance, String currencyName, String currencyISO) throws Exception {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("accountName", name);
        String recordLocation = entityRecordExists(ACCOUNTS_PATH, "findByName", parameters);
        if (!recordLocation.isEmpty()) {
            return recordLocation;
        } else {
            Account account = getAccount(name, balance);
            String accountLocation = createEntity(account, ACCOUNTS_PATH);
            String currencyLocation = createCurrency(currencyISO, currencyName);

            associateEntities(accountLocation.concat("/").concat("currency"), currencyLocation);

            return accountLocation;
        }

    }

	/**
	 * This method creates and returns a currency object.
	 *
	 * @param iso
	 *            The ISO code of the currency.
	 * @param name
	 *            The name of the currency.
	 * @return An instance of the currency object.
	 */
	private Currency getCurrency(String iso, String name) {
		Currency currency = new Currency();
		currency.setIso(iso);
		currency.setName(name);
		return currency;
	}

	/**
	 * This method creates a new currency record in the database, and returns the URI location of the created record.
	 *
	 * @param iso
	 *            The ISO code of the currency.
	 * @param name
	 *            The name of the currency.
	 * @return The URI location of the created currency.
	 * @throws Exception
	 */
	public String createCurrency(String iso, String name) throws Exception {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("currencyIso", iso);
        String recordLocation = entityRecordExists(CURRENCY_PATH, "findByIso", parameters);
        if (!recordLocation.isEmpty()) {
            return recordLocation;
        } else {
            Currency currency = getCurrency(iso, name);
            return createEntity(currency, CURRENCY_PATH);
        }
    }

	/**
	 * This method retrieves an account from the given account URI
	 *
	 * @param entityURI
	 *            The entity location.
	 * @return The response object.
	 * @throws Exception
	 */
	public MockHttpServletResponse getEntity(String entityURI) throws Exception {
		return requestHelper.performGetRequest(entityURI);
	}

	/**
	 * @param uri
	 * @param parameters
	 * @return
	 * @throws Exception
	 */
	public MockHttpServletResponse getEntity(String uri, Map parameters) throws Exception {
		return requestHelper.performGetRequest(uri, parameters);
	}

	/**
	 * @param uri
	 * @param parameters
	 * @param expectedStatus
	 * @return
	 * @throws Exception
	 */
	public MockHttpServletResponse getEntity(String uri, Map parameters, HttpStatus expectedStatus) throws Exception {
		return requestHelper.performGetRequest(uri, parameters, expectedStatus);
	}

	/**
	 * @param uri
	 * @param content
	 * @return
	 * @throws Exception
	 */
	public MockHttpServletResponse updateEntity(String uri, byte[] content) throws Exception {
		return requestHelper.performPutRequest(uri, content);
	}

	/**
	 * @param uri
	 * @param contentType
	 * @param content
	 * @return
	 * @throws Exception
	 */
	public MockHttpServletResponse updateEntity(String uri, String contentType, byte[] content) throws Exception {
		return requestHelper.performPutRequest(uri, contentType, content);
	}

	/**
	 * @param uri
	 * @return
	 * @throws Exception
	 */
	public MockHttpServletResponse deleteEntity(String uri) throws Exception {
		return requestHelper.performDeleteRequest(uri);
	}

	/**
	 * This method creates a new entity for a given entity object.
	 *
	 * @return The REST query location of the created entity.
	 * @throws Exception
	 */
	private String createEntity(BaseEntity entity, String path) throws Exception {
		byte[] entityBytes = mapper.writeValueAsBytes(entity);
		//Insert new user record.
		MockHttpServletResponse postResponse = requestHelper.performPostRequest("/api/".concat(path).concat("/"), entityBytes);
		Assert.assertNotNull(postResponse);
		return postResponse.getHeader("Location");
	}

	/**
	 * This method creates a new entity for a given entity object.
	 *
	 * @return The REST query location of the created entity.
	 * @throws Exception
	 */
	private String createTransaction(Transaction entity, String path, Map<String, String> associations) throws Exception {

		TransactionDto transactionDto = new TransactionDto();
		transactionDto.setNotes(entity.getNotes());
		transactionDto.setAccount(associations.get("account"));
		transactionDto.setAmount(entity.getAmount());
		transactionDto.setCategory(associations.get("category"));
		transactionDto.setCheckNumber(entity.getCheckNumber());
		transactionDto.setCurrency(associations.get("currency"));
		transactionDto.setNumber(entity.getNumber());
		transactionDto.setPayee(associations.get("payee"));
		transactionDto.setQuantity(entity.getQuantity());
		transactionDto.setTransactionDate(entity.getTransactionDate());
		transactionDto.setUser(associations.get("user"));
		transactionDto.setCheckNumber(entity.getCheckNumber());

		byte[] entityBytes = mapper.writeValueAsBytes(transactionDto);
		//Insert new user record.
		MockHttpServletResponse postResponse = requestHelper.performPostRequest("/api/".concat(path).concat("/"), entityBytes);
		Assert.assertNotNull(postResponse);
		return postResponse.getHeader("Location");
	}

	/**
	 * This method associates an existing parent entity, with its associated child entity, using puts.
	 *
	 * @return The REST query location of the created entity.
	 * @throws Exception
	 */
	private Boolean associateEntities(String parentEntityLocation, String childEntityLocation) throws Exception {

		MockHttpServletResponse putResponse = requestHelper.performPutRequest(parentEntityLocation, "text/uri-list",
				childEntityLocation.getBytes());
		Assert.assertNotNull(putResponse);
		return putResponse.getStatus() == HttpStatus.NO_CONTENT.value();
	}

	/**
	 * This method creates a new user object.
	 *
	 * @param userName
	 *            The users username.
	 * @param password
	 *            The password for the user.
	 * @param firstName
	 *            The users first name.
	 * @param lastName
	 *            The users last name
	 * @return A user object, containing provided information.
	 */
	private Users getUser(String userName, String password, String firstName, String lastName) {
		Users user = new Users();

		user.setUserName(userName);
		user.setPassword(password);
		user.setFirstName(firstName);
		user.setLastName(lastName);

		return user;
	}

	/**
	 * This method creates a new user to test account requests.
	 *
	 * @param userName
	 *            The user name of the created user
	 * @param password
	 *            The password for the created user.
	 * @param firstName
	 *            The users first name.
	 * @param lastName
	 *            The users last name.
	 * @throws Exception
	 */
	public String createUser(String userName, String password, String firstName, String lastName, String groupName) throws Exception {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("userName", userName);
        String recordLocation = entityRecordExists("users", "findByUserName", parameters);
        if (recordLocation == null || recordLocation.isEmpty()) {
            Users user = getUser(userName, password, firstName, lastName);
            return createEntity(user, USERS_PATH);
        } else {
            return recordLocation;
        }
    }

	/**
	 * This method creates a new user to test account requests.
	 *
	 * @param userName
	 *            The user name of the created user
	 * @param password
	 *            The password for the created user.
	 * @param firstName
	 *            The users first name.
	 * @param lastName
	 *            The users last name.
	 * @param associations
	 *            This is a list of associations.
	 * @throws Exception
	 */
	private String createUser(String userName, String password, String firstName, String lastName, Map<String, String> associations) throws Exception {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("userName", userName);
        String recordLocation = entityRecordExists("users", "findByUserName", parameters);
        if (recordLocation == null || recordLocation.isEmpty()) {
            Users user = getUser(userName, password, firstName, lastName);
            return createEntity(user, USERS_PATH);
        } else {
            return recordLocation;
        }
    }

	/**
	 * This method creates a new account type class.
	 *
	 * @param name
	 *            The name of the account type.
	 * @return An instance of the Account Type.
	 */
	private AccountType getAccountType(String name) {
		AccountType accountType = new AccountType();
		accountType.setName(name);
		return accountType;
	}

	/**
	 * This method creates a new account, and returns the accounts reference URI
	 *
	 * @param name
	 *            The name of the account.
	 * @return The reference URI for the created account.
	 * @throws Exception
	 */
	public String createAccountType(String name) throws Exception {
		AccountType accountType = getAccountType(name);
		return createEntity(accountType, ACCOUNT_TYPES_PATH);
	}

	/**
	 * This method creates a new address, saves it to the database, and returns the location of the created record.
	 *
	 * @param address1
	 *            The first address line.
	 * @param address2
	 *            The second address line.
	 * @param city
	 *            The addresses city.
	 * @param county
	 *            The addresses county.
	 * @param countryName
	 *            The addresses country.
	 * @param countryISO
	 *            The ISO code of the country.
	 * @return The URI location of the created record.
	 * @throws Exception
	 */
	public String createAddress(String address1, String address2, String city, String county, String countryName,
			String countryISO) throws Exception {
		Address address = getAddress(address1, address2, city, county);
		Country country = getCountry(countryName, countryISO);
		String addressLocation = createEntity(address, ADDRESSES_PATH);
		String countryLocation = createEntity(country, COUNTRIES_PATH);
		associateEntities(addressLocation.concat("/").concat("country"), countryLocation);

		return addressLocation;
	}

	/**
	 * This method creates a new country in the database for a given name and ISO code.
	 *
	 * @param countryISO
	 *            The country's ISO code.
	 * @param name
	 *            The name of the country.
	 * @return The URI location of the created country.
	 * @throws Exception
	 */
	public String createCountry(String countryISO, String name) throws Exception {
		Country country = getCountry(countryISO, name);
		return createEntity(country, COUNTRIES_PATH);
	}

	/**
	 * This method creates a new country object
	 *
	 * @param countryISO
	 *            The countries ISO code.
	 * @param name
	 *            The name of the country.
	 * @return An instance of the created country.
	 */
	private Country getCountry(String countryISO, String name) {
		Country country = new Country();
		country.setName(name);
		country.setIso(countryISO);
		return country;
	}

	/**
	 * This method creates a new authority in the database, and returns the URI location.
	 *
	 * @param authorityName
	 *            The name of the authority.
	 * @return The location of the created authority.
	 * @throws Exception
	 */
	public String createAuthority(String authorityName) throws Exception {
		Authorities authority = getAuthority(authorityName);
		return createEntity(authority, AUTHORITIES_PATH);
	}

	/**
	 * This method returns a new instance of the created authority.
	 *
	 * @param authorityName
	 *            The name of the authority.
	 * @return An instance of the Authorities class for the given authority name.
	 */
	private Authorities getAuthority(String authorityName) {
		Authorities authorities = new Authorities();
		authorities.setAuthority(authorityName);
		return authorities;
	}

	/**
	 * This method creates a new instance of the category object
	 *
	 * @param name
	 *            The name of the category.
	 * @return an instance of a category object.
	 */
	private Category getCategory(String name) {
		Category category = new Category();
		category.setName(name);
		return category;
	}

	/**
	 * This method creates a new category, and returns the URI location of the created category.
	 *
	 * @param name
	 *            The name of the category
	 * @return The URI location of the created category.
	 * @throws Exception
	 */
	public String createCategory(String name, String parentCategoryName) throws Exception {
		String parentCategoryLocation = "";
		if (parentCategoryName != null && !parentCategoryName.isEmpty()) {
			Category parentCategory = getCategory(parentCategoryName);
			parentCategoryLocation = createEntity(parentCategory, CATEGORIES_PATH);
		}
		Category category = getCategory(name);

		String categoryLocation = createEntity(category, CATEGORIES_PATH);

		if (parentCategoryName != null && !parentCategoryName.isEmpty()) {
			associateEntities(categoryLocation.concat("/").concat(PARENT_CATEGORY), parentCategoryLocation);
		}

		return categoryLocation;
	}

	/**
	 * This method returns a new instance of a group object with a given name, and list of authorities.
	 *
	 * @param name
	 *            The name of the group.
	 * @return A new instance of the groups object.
	 */
	private Groups getGroup(String name) {
		Groups groups = new Groups();
		groups.setName(name);
		return groups;
	}

	/**
	 * This method creates a new group in the database, along with associated authorities, if any.
	 *
	 * @param name
	 *            The name of the group.
	 * @param authorities
	 *            A list of associated authorities.
	 * @return The URI to the created group.
	 * @throws Exception
	 */
	public String createGroup(String name, Set<Authorities> authorities) throws Exception {
		Groups groups = getGroup(name);
		String groupLocation = createEntity(groups, GROUPS_PATH);
		if (!authorities.isEmpty()) {
			for (Authorities authority : authorities) {
				String authorityLocation = createEntity(authority, AUTHORITIES_PATH);
				associateEntities(groupLocation.concat("/").concat(AUTHORITIES), authorityLocation);
			}
		}
		return groupLocation;
	}

	/**
	 * @param name
	 * @return
	 * @throws Exception
	 */
	public String createPayee(String name) throws Exception {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("payeeName", name);
        String recordLocation = entityRecordExists("payees", "findByName", parameters);
        if (!recordLocation.isEmpty()) {
            return recordLocation;
        } else {
            Payee payee = getPayee(name);
            return createEntity(payee, PAYEES_PATH);
        }
    }

	/**
	 * @param name
	 * @return
	 */
	private Payee getPayee(String name) {
		Payee payee = new Payee();
		payee.setName(name);
		return payee;
	}

	/**
	 * This method creates a new transaction for the given details.
	 *
	 * @param amount
	 *            The name of the account to which this transaction belongs.
	 * @param categoryName
	 *            The category of the transaction.
	 * @param chequeNumber
	 *            The transactions cheque number - if any.
	 * @param currencyName
	 *            The transaction currency.
	 * @param currencyISO
	 *            The transaction currency's ISO name.
	 * @param notes
	 *            Any extra notes associated with the transaction.
	 * @param payeeName
	 *            The name of the payee for this transaction.
	 * @param quantity
	 *            The quantity of the transaction.
	 * @param userName
	 *            The user associated with this transaction.
	 * @return The URI to the created transaction.
	 * @throws Exception
	 */
	public String createTransaction(String accountName, Double amount, String categoryName, String chequeNumber,
                                    String currencyName, String currencyISO, String notes, String payeeName,
                                    Integer quantity, String userName, String password, String firstName, String lastName)
            throws Exception {

        String userLocation = createUser(userName, password, firstName, lastName, "");
        String accountLocation = createAccount(accountName, amount, "Euro", "EUR");
        String categoryLocation = createCategory(categoryName, null);
        String currencyLocation = createCurrency(currencyISO, currencyName);
        String payeeLocation = createPayee(payeeName);

        Map<String, String> associations = new HashMap<>();
        associations.put("user", userLocation);
        associations.put("account", accountLocation);
        associations.put("category", categoryLocation);
        associations.put("currency", currencyLocation);
        associations.put("payee", payeeLocation);

        Transaction transaction = getNewTransaction(amount, chequeNumber, notes, quantity);
        String transactionLocation = createTransaction(transaction, TRANSACTIONS_PATH, associations);

        return transactionLocation;
    }

	/**
	 * @param entity
	 * @param parameters
	 * @return
	 * @throws Exception
	 */
	public String entityRecordExists(String entity, String method, Map<String, String> parameters) throws Exception {
		//http://localhost:8080/api/users/search/findByUserName?userName=admin
		MockHttpServletResponse response = requestHelper.performGetRequest("/api/".concat(entity).concat("/").concat("search")
				.concat("/").concat(method), parameters);

		String content = response.getContentAsString();
		JSONObject entityContent = (JSONObject) getParser().parse(content);

		if (entityContent.isEmpty()) {
			return "";
		} else {
			return ((JSONObject) getJsonObjectFromArray(LINKS,
					((JSONArray) ((JSONObject) entityContent.get(EMBEDDED)).get(entity))).get(SELF)).get(HREF).toString();
		}
	}

	/**
	 * This method retrieves a specific JSONObject from the given array, based on provided parameters.
	 *
	 * @param key
	 *            The key to search for within the array of objects.
	 * @param value
	 *            The value for the given key.
	 * @param array
	 *            The array containing the given key and value.
	 * @return The JSONObject which contains the provided key and value.
	 */
	public JSONObject getJsonObjectFromArray(String key, String value, JSONArray array) {
		for (Object object : array) {
			JSONObject jsonObject = (JSONObject) object;
			if (jsonObject.containsKey(key)) {
				if (jsonObject.get(key).toString().equalsIgnoreCase(value)) {
					return jsonObject;
				}
			}
		}
		return null;
	}

	/**
	 * This method retrieves a specific JSONObject from the given array, based on provided parameters.
	 *
	 * @param key
	 *            The key to search for within the array of objects.
	 * @param array
	 *            The array containing the given key and value.
	 * @return The JSONObject which contains the provided key and value.
	 */
	public JSONObject getJsonObjectFromArray(String key, JSONArray array) {
		for (Object object : array) {
			JSONObject jsonObject = (JSONObject) object;
			if (jsonObject.containsKey(key)) {
				return (JSONObject) jsonObject.get(key);
			}
		}
		return null;
	}

}
