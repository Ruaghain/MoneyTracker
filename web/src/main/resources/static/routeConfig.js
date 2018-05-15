(function () {
    'use strict';

    moneyApp.config(["$stateProvider",
        function ($stateProvider) {
            $stateProvider.
                state("home", {
                    url: "/",
                    templateUrl: "functionality/home.html"
                }).
                state("login", {
                    url: "/login",
                    templateUrl: "functionality/login/login.html",
                    controller: "AuthController"
                }).
                state("landingPage", {
                    url: "/landingPage",
                    templateUrl: "functionality/landingpage/landing-page.html"
                }).
                state("accountType", {
                    abstract: true,
                    url: "/accountType",
                    templateUrl: "functionality/accountType/accountType.html"
                }).
                state("accountType.list", {
                    url: "/list",
                    controller: "AccountTypeListController",
                    controllerAs: "accountTypeController",
                    templateUrl: "functionality/accountType/list/accountType-list.html",
                    resolve: {
                        accountTypes: function (mnyAccountTypeService) {
                            return mnyAccountTypeService.getAllAccountTypes();
                        }
                    }
                }).
                state("accountType.new", {
                    url: "/new",
                    templateUrl: "functionality/accountType/create/accountType-add.html",
                    controller: "AccountTypeCreateController",
                    controllerAs: "accountTypeController"
                }).
                state("accountType.edit", {
                    url: "/edit/:id",
                    templateUrl: "functionality/accountType/edit/accountType-edit.html",
                    controller: "AccountTypeEditController",
                    controllerAs: "accountTypeController",
                    resolve: {
                        accountType: function (mnyAccountTypeService, $stateParams) {
                            return mnyAccountTypeService.getAccountType($stateParams.id);
                        }
                    }
                }).
                state("account", {
                    abstract: true,
                    url: "/account",
                    templateUrl: "functionality/accounts/account.html"
                }).
                state("account.list", {
                    url: "/list",
                    controller: "AccountListController",
                    controllerAs: "accountController",
                    templateUrl: "functionality/accounts/list/account-list.html",
                    resolve: {
                        accounts: function (mnyAccountService) {
                            return mnyAccountService.getAllAccounts();
                        }
                    }
                }).
                state("account.new", {
                    url: "/new",
                    templateUrl: "functionality/accounts/create/account-add.html",
                    controller: "AccountCreateController",
                    controllerAs: "accountController"
                }).
                state("account.edit", {
                    url: "/edit/:id",
                    templateUrl: "functionality/accounts/edit/account-edit.html",
                    controller: "AccountEditController",
                    controllerAs: "accountController",
                    resolve: {
                        account: function (mnyAccountService, $stateParams) {
                            return mnyAccountService.getAccount($stateParams.id);
                        }
                    }
                }).
                state("user", {
                    abstract: true,
                    url: "/user",
                    templateUrl: "functionality/users/user.html"
                }).
                state("user.list", {
                    url: "/list",
                    templateUrl: "functionality/users/list/user-list.html",
                    controller: "UserListController",
                    controllerAs: "userController",
                    resolve: {
                        users: function (mnyUserService) {
                            return mnyUserService.getAllUsers();
                        }
                    }
                }).
                state("user.new", {
                    url: "/new",
                    templateUrl: "functionality/users/create/user-add.html",
                    controller: "UserCreateController",
                    controllerAs: "userController"
                }).
                state("user.edit", {
                    url: "/edit/:id",
                    templateUrl: "functionality/users/edit/user-edit.html",
                    controller: "UserEditController",
                    controllerAs: "userController",
                    resolve: {
                        user: function (mnyUserService, $stateParams) {
                            return mnyUserService.getUser($stateParams.id);
                        }
                    }
                }).
                state("currency", {
                    abstract: true,
                    url: "/currency",
                    templateUrl: "functionality/currency/currency.html"
                }).
                state("currency.list", {
                    url: "/list",
                    templateUrl: "functionality/currency/list/currency-list.html",
                    controller: "CurrencyListController",
                    controllerAs: "currencyController",
                    resolve: {
                        currencies: function (mnyCurrencyService) {
                            return mnyCurrencyService.getAllCurrencies();
                        }
                    }
                }).
                state("currency.new", {
                    url: "/new",
                    templateUrl: "functionality/currency/create/currency-add.html",
                    controller: "CurrencyCreateController",
                    controllerAs: "currencyController"
                }).
                state("currency.edit", {
                    url: "/edit/:id",
                    templateUrl: "functionality/currency/edit/currency-edit.html",
                    controller: "CurrencyEditController",
                    controllerAs: "currencyController",
                    resolve: {
                        currency: function (mnyCurrencyService, $stateParams) {
                            return mnyCurrencyService.getCurrency($stateParams.id);
                        }
                    }
                }).
                state("transactionList", {
                    url: "/transactionList",
                    templateUrl: "functionality/transactions/list/transaction-list.html",
                    controller: "TransactionController",
                    controllerAs: "transactionController"
                }).
                state("dataUploads", {
                    url: "/dataUploads",
                    templateUrl: "functionality/dataupload/data-upload.html",
                    controller: "DataUploadController"
                });
            //.
            //otherwise("/");
        }]);
})();
