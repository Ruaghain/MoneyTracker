# MoneyTracker

[![Build Status](https://travis-ci.org/Ruaghain/MoneyTracker.svg?branch=master)](https://travis-ci.org/Ruaghain/MoneyTracker)
[![Coverage Status](https://coveralls.io/repos/Ruaghain/MoneyTracker/badge.svg?branch=master&service=github)](https://coveralls.io/github/Ruaghain/MoneyTracker?branch=master)

MoneyTracker is intended to be a simple, easy to use REST based money tracking application. It will facilitate both the tracking of transactions through a web page, and it will eventually 
facilitate the tracking of wallet expenditure, by means of a smart phone application. This application will also leverage OCR technology to read receipts and upload this to the server. 
Following is a description of what is used within the application. For details on how to develop this project. See [Development](#development)

**NOTE: This is very much a work in progress.**

## Server

As mentioned above, the server side of MoneyTracker is implemented using REST. Following the REST principles, it is also stateless. Along with using the HTTP verbs GET, PUT, POST and DELETE 
to update information. It uses spring-boot to setup the server webapp, along with hibernate, which deals with the database access. The REST server is exposed using spring-data-rest.

### <a name="setup"></a> Setup

The best way to run this application would be to use maven. Please ensure that you have downloaded it, and have set the path accordingly. The server uses [Spring Boot] (http://projects.spring.io/spring-boot/) 
to launch the spring-data-rest application, which means that it doesn't require an external Tomcat to get it running. All of the application configuration is done in the
*application.yml* file, in the resources directory of the server. To get the server running, do the following:

1. Create the MoneyTrak database in SQL Server.
2. Set the database user name and password for database access.
3. Run "mvn spring-boot:run" in the command line for the server project.
4. Access <http://localhost:8080/#/>
5. Enter in "Admin" and "Passw0rd" as the username and password to access this Url. These are the default credentials, which are automatically created.

Once the server is up and running you can run <http://localhost:8080/health> to ensure that everything is running correctly. See [Endpoints] (http://docs.spring.io/spring-boot/docs/current-SNAPSHOT/reference/htmlsingle/#production-ready) for
a list of endpoints that can be used.
 
*NOTE: All steps above, presume a default implementation. i.e. That you've made no changes to the application.yml, other than to the username and password to access the database.*

### REST

The REST functionality was achieved by leveraging [Spring Data Rest](http://www.springsource.org/spring-data/rest). It facilitates

* GET - Retrieval of required information, rendered in JSON format.
* PUT - Updating of information for a given entity, a JSON payload is required.
* POST - Inserting of information, a JSON payload is again, required.
* DELETE - Deleting of required information.

It also allows for JSONP and JSONP+E.

Access to the REST API is provided by HATEOAS, if you navigate to <http://localhost:8080/api> you'll see a list of available URLs

### Security

Most of the requests to the server need to be validated. Only Base64 encoding of the username and password is provided for (used for testing purposes).
Most requests need to have an associated **Authorization** header. The only request that requires no authentication is the POST to the Users table. In all other instances only authorised, admin users, or users who own the relevant records
will be able to access/update them.

### Data Layer

The data layer is currently implemented using [Hibernate](http://www.hibernate.org/), which uses JPA to expose the tables to Spring Data Rest. The underlying database connection to the database uses C3P0. The following tables are created:

* Account
* Account_Audit
* AccountType
* Address
* Authorities
* Category
* Country
* Currency
* Group_Authorities
* Groups
* Payee
* Payee_Audit
* Revision
* Undertaking (Transaction)
* Undertaking_Audit
* Users
* Users_Account
* Users_Audit
* Users_Groups

### Auditing

All auditing is performed, using [Hibernate Envers](http://docs.jboss.org/envers/docs/) To ensure that changes are tracked, the following several tables are audited:

* Account
* Payee
* Undertaking (Transaction)
* Users

### Logging

All logging is taken care of using [Logback](http://logback.qos.ch/). It natively speaks to SLF4J, and allows for automatic reloading of configuration files.

## Data Upload

Data uploading of financial information is supported (facilitated, by the use of Spring Batch). All files need to be in CSV format, and must conform to the following structure:
 
 * Number
 * Date
 * Account
 * Payee
 * Cleared
 * Amount
 * Category
 * Subcategory
 * Memo

To upload information, POST the file to "/$service/dataUpload", this will load the file to a temp directory, and immediately start processing the file in a Spring Batch Job. 

Note: Currently, only information exported from MS Money is supported. However, support for Allied Irish Banks new export functionality will be done. 

## Client

The client side will currently use AngularJS in conjunction Bootstrap. A single page approach to displaying information will be taken. AngularJS uses MVC to facilitate client side code,
which fits in nicely with what I want to achieve with this project. The following URLs will be useful should you wish to read up further.

  * [AngularJS](http://angularjs.org/)
  * [Bootstrap] (http://getbootstrap.com/)


## <a name="development"></a> Development

MoneyTracker leverages several frameworks to allow for easy development of the application. First and foremost, you need to clone a copy of MoneyTracker from GitHub. Once this is done, 
you need to download [NodeJS](https://nodejs.org/) if you haven't already done so. This is core to product development. Once this is installed (and you've added this to the path), navigate to 
the root directory of the MoneyTracking source, and run the following command:

* npm install

This will install everything defined in the package.json file. Once everything has been installed, you need to install everything defined in the bower.json file. Bower facilitates all the front-end
dependencies (client-side javascript files) including versioning. To do this, run the following command:
 
* bower install
 
This too, will install everything defined in the bower.json file, into a directory called vendors in /web/src/main/java/resource/static/. Now, you're at the stage where you could just run the 
application. Following the steps defined in [Setup](#setup). However if you wish to debug the server code, debug the "public static void main(String[] args)" method in ApplicationConfig.java.

To debug the client code, the server obviously needs to be running, so that REST requests can be made. I've managed to allow for hot-swapping when developing the client-side. To do this, run the 
following command in the MoneyTracker root:
 
 * grunt dev
 
[Grunt](http://gruntjs.com/) is a task manager tool, which will run serveral tasks while you're developing the code. See the Gruntfile.js for details. Once you've run the grunt task, navigate to 
http://localhost:9000 You are now ready to develop client-side. Any changes you make to any js or css file, will activate the watch task, which in turn will analyse the code, concat the relevant 
files into MoneyTracker.css and MoneyTracker.js, and run the unit tests defined for all of the relevant JavaScript files - all of this done automatically when you save the file.
       
