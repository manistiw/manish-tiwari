# apiautomation
This is API automation project for API Playground

This project is about a small framework for running api test with allure reporting.You can refer it for

  - api automation using RestAssured
  - Using Java,Spring,TestNg and RestAssured for API automation framework
  - Allure reporting.

# What's New!

  - Used spring framework for dependency injection, singleton component creation and preparing spring test context using environment from properties file.

### Report Generation

Before execution its a good idea to install allure report for viewing reports in browser.
open powerShell if windows user and execute this 
```sh
$iex (new-object net.webclient).downloadstring('https://get.scoop.sh')
```
```sh
$ scoop install allure
```
And then move to target folder of cloned project and run allure serve
report will get open in browser

| Report Plugin | README |
| ------ | ------ |
| Allure | [https://github.com/allure-framework/allure2/blob/master/README.md]|

installation documentation: https://docs.qameta.io/allure/#_installing_a_commandline

### Running Tests

Execution requires [Java 7 or above] to run.

> running tests

```sh
$ mvn clean test -DsuiteXmlFile=testng.xml -Denvironment=stage
```
### Report Generation

Is execution successfull? Great!

Now follow following steps to see report in browser move to the folder where project is cloned and follow steps:
```sh
$ cd target
$ allure serve
```

Browser should open with details of execution. Generated reports will have steps details of tests and failed tests.

Thanks,

