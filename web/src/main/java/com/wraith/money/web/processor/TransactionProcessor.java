package com.wraith.money.web.processor;

import com.wraith.money.data.entity.*;
import com.wraith.money.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * User: rowan.massey Date: 11/09/2014 Time: 16:16
 */
@Component
public class TransactionProcessor implements ItemProcessor<MoneyTransaction, Transaction> {

	private static final Logger LOG = LoggerFactory.getLogger(TransactionProcessor.class);

	@Inject
	CurrencyRepository currencyRepository;
	@Inject
	AccountRepository accountRepository;
	@Inject
	UsersRepository usersRepository;
	@Inject
	PayeeRepository payeeRepository;
	@Inject
	CategoryRepository categoryRepository;
	@Inject
	AccountTypeRepository accountTypeRepository;

	@Override
	public Transaction process(MoneyTransaction item) throws Exception {
		LOG.debug(String.format("Transforming MS Money item for account '%s' and category '%s', with and amount of '%s'",
				item.getAccount(), item.getCategory(), item.getAmount()));

		Transaction transaction = new Transaction();
		transaction.setCurrency(getBaseCurrency());
		transaction.setAccount(getAccount(item.getAccount(), "Banking"));
		Double amount = NumberFormat.getInstance(java.util.Locale.getDefault()).parse(item.getAmount()).doubleValue();
		transaction.setAmount(amount);
		transaction.setTransactionDate(new SimpleDateFormat("dd/MM/yyyy").parse(item.getDate()));
		transaction.setPayee(getPayee(item.getPayee()));
		transaction.setCheckNumber(item.getNumber());
		transaction.setNotes(item.getMemo());
		transaction.setCategory(getCategory(item.getCategory(), item.getSubcategory()));

		return transaction;
	}

	/**
	 * @return
	 */
	private Currency getBaseCurrency() {
		List<Currency> currencies = currencyRepository.findByIso("EUR");
		if (!currencies.isEmpty()) {
			return currencies.get(0);
		} else {
			Currency currency = new Currency();
			currency.setIso("EUR");
			currency.setName("Euro");
			return currencyRepository.save(currency);
		}
	}

	/**
	 * @param accountName
	 * @param accountType
	 * @return
	 */
	private Account getAccount(String accountName, String accountType) {
		List<Account> accounts = accountRepository.findByName(accountName);
		if (!accounts.isEmpty()) {
			return accounts.get(0);
		} else {
			Account account = new Account();
			account.setCurrency(getBaseCurrency());
			account.setName(accountName);
			account.setType(getAccountType(accountType));
			account.setOpeningBalance(0.0);
			return accountRepository.save(account);
		}
	}

	/**
	 * @param payeeName
	 * @return
	 */
	private Payee getPayee(String payeeName) {
		if (payeeName.isEmpty()) {
			payeeName = "Unknown";
		}
		List<Payee> payees = payeeRepository.findByName(payeeName);
		if (!payees.isEmpty()) {
			return payees.get(0);
		} else {
			Payee payee = new Payee();
			payee.setName(payeeName);
			payeeRepository.save(payee);
			return payeeRepository.findByName(payeeName).get(0);
		}
	}

	/**
	 * @param accountTypeName
	 * @return
	 */
	private AccountType getAccountType(String accountTypeName) {
		List<AccountType> accountTypes = accountTypeRepository.findByName(accountTypeName);
		if (!accountTypes.isEmpty()) {
			return accountTypes.get(0);
		} else {
			AccountType accountType = new AccountType();
			accountType.setName(accountTypeName);
			accountTypeRepository.save(accountType);
			return accountTypeRepository.findByName(accountTypeName).get(0);
		}
	}

	/**
	 * @param categoryName
	 * @param subCategoryName
	 * @return
	 */
	private Category getCategory(String categoryName, String subCategoryName) {
		if (!subCategoryName.isEmpty()) {
			List<Category> categories = categoryRepository.findByName(subCategoryName);
			if (!categories.isEmpty()) {
				return categories.get(0);
			} else {
				Category parentCategory = getCategory(categoryName, "");
				Category subCategory = new Category();
				subCategory.setName(subCategoryName);
				subCategory.setParentCategory(parentCategory);
				return categoryRepository.save(subCategory);
			}
		} else {
			if (!categoryRepository.findByName(categoryName).isEmpty()) {
				return categoryRepository.findByName(categoryName).get(0);
			} else {
				Category category = new Category();
				category.setName(categoryName);
				return categoryRepository.save(category);
			}
		}
	}

	/**
	 * @return
	 */
	private Users getUser() {
		return usersRepository.findAll().get(0);
	}
}
