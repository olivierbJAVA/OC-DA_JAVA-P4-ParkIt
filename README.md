# ParkIt - Parking System
A command line application for managing the parking system. 
This application uses Java to run and stores the data in MySQL DB.

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes. See deployment for notes on how to deploy the project on a live system.

### Prerequisites

What things you need to install the software and how to install them.

- Java 1.8
- Maven 3.6.2
- MySQL 8.0.17

### Installing

A step by step explanation that tell you how to get a development environment running:

1.Install Java:

https://docs.oracle.com/javase/8/docs/technotes/guides/install/install_overview.html

2.Install Maven:

https://maven.apache.org/install.html

3.Install MySQL:

https://dev.mysql.com/downloads/mysql/

After downloading the MySQL 8 installer and installing it, you will be asked to configure the password for the default `root` account.
This code uses the default root account to connect and the password can be set as `rootroot`. If you add another user/credentials make sure to change the same in the code base (in class *DataBaseConfig* of the package *com.parkit.parkingsystem.config* for the production database and in class *DataBaseTestConfig* of the package *com.parkit.parkingsystem.integration.config* for the test database).

### Running App

Post installation of MySQL, Java and Maven, you will have to set up the tables and data in the data base.
For this, please run the sql commands present in the `Data.sql` file under the `resources` folder in the code base.

Finally, you will be ready to import the code into an IDE of your choice and run the App.java to launch the application.

### ! IMPORTANT - Potential Time zone issue

During installing, application running or tests launching you may have an issue (depending on your configuration) related to Time zone configuration.
It is an issue due the configuration of MySQL server.

Please find below two solutions to solve this issue :

1 - Either you can add the following line in the MySQL server configuration file (*my.ini* or *my.cfg*) that is in your MySQL directory :

*default-time-zone='+02:00'*
 
Please ensure to add this line in the [mysqld] section of the configuration file.


2 - Or you can add the following line in the code in the method *getConnection()* of the class *DataBaseConfig* (package *com.parkit.parkingsystem.config*), in the URL, just after the name of the database (*prod*) :

*?zeroDateTimeBehavior=CONVERT_TO_NULL&serverTimezone=UTC*

so that you have :

DriverManager.getConnection("jdbc:mysql://localhost:3306/prod?zeroDateTimeBehavior=CONVERT_TO_NULL&serverTimezone=UTC", "root", "rootroot");

Please ensure to do the same for the test database as well in the class *DataBaseTestConfig* (package *com.parkit.parkingsystem.integration.config*) :

DriverManager.getConnection("jdbc:mysql://localhost:3306/test?zeroDateTimeBehavior=CONVERT_TO_NULL&serverTimezone=UTC", "root", "rootroot");

You can adjust the Time zone to your own Time zone obviously.


### Testing

The application has unit tests and integration tests written. These tests need to be triggered from maven-surefire plugin.

To run the tests from Maven, go to the folder that contains the pom.xml file and execute the below command.

For unit tests :

`mvn test`

 For both unit and integration tests :

`mvn verify`

### Executable Jar file

In order to generate an executable Jar file for the application from Maven, go to the folder that contains the pom.xml file and execute the below command.

`mvn package`

### Web site

A web site describing the project including the JavaDoc and some reports (tests, code coverage, bugs detection) can be generated.

In order to generate the web site for the project from Maven, go to the folder that contains the pom.xml file and execute the below command.

`mvn site`

For generating the Surefire report, please execute the below command.

`mvn surefire-report:report-only`

### Logging

The tool Log4J2 is used for logging. Logs are sent to the console and to a file (*C:\logs\parkit-app.log* by default).

You can adapt logging to your needs by editing the Log4J2 properties file : *log4j2.xml* (in the *src/main/resources/* directory).


# New features !
Two new features have recently been added :

- A say in the parking under 30 minutes is free
- Recurring users benefit from a 5% discount
