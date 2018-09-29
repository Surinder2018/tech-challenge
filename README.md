# Gauge with Selenium Grid

This is Gauge and Selenium Grid project. It can
    use grid to run tests on different nodes
    can run tests without grid
    run tests on multiple browsers
    can run tests in parralle using gauge or grid


## Prerequisites
******************************************************************************************************

## Windows Installation Guide

The following software applicatiopns and plugins are needed to run and develop the UI test scripts.

1. JDK 8
   - Install JDK 8U162
   - set environment variable
     - JAVA_HOME=C:\Program Files\Java\jdk1.8.0_162
2. Maven
   - Install Maven
   - set environment variables
     - M2_HOME=C:\Program Files\Apache\maven
     - MAVEN_HOME=C:\Program Files\Apache\maven
3. Gauge
   - set environment variables
     - Use by you only
        - GAUGE_ROOT=%APPDATA%\Gauge
        - GAUGE_HOME=%APPDATA%\Gauge
     - Use by anyone who logs onto the PC/Server:
        - GAUGE_ROOT=C:\program files\gauge
        - GAUGE_HOME=C:\program files\gauge
   - Install Gauge

For details of a Tattscloud Widnows 10 setup see the following confluence page:

```html
    https://confluence.tattsgroup.io/display/GS/TatssCloud+Model+Test+Windows+10+PC
```

******************************************************************************************************

## MAC Installation Guide

This is done through brew.

1. brew install gauge (NOTE: brew only has gauge version 0.8.4)
2. brew install maven
    - NOTE: you dont need to set environment variables defined below if the installatin is done through homebrew (only on mac)

******************************************************************************************************


## Setting the Selenium Grid

* Download the latest version of Selenium Server [here](http://docs.seleniumhq.org/download/).
To set up the Hub, run
```
java -jar <path_to_selenium_server_jar> -role hub
```
This uses port 4444 by default for its web interface.

To set up a node, run
```
java -jar <path_to_selenium_server_jar> -role webdriver -hub http://localhost:4444/grid/register/ -port 5566
```
You can use the free port of choice.

To check web console, go to [http://localhost:4444/grid/console](http://localhost:4444/grid/console)

If you are running hub and nodes in different machines, `localhost` should be replaced with IP address of hub. This should also be updated in `project_dir/env/user.properties`.

******************************************************************************************************


### Install and configure common components for  Windows and MAC

1. Gauge plugins for Java, HTMl reports and XML reports as follows from an bash shell on MAC or an administrator command prompt on Windows:
    - gauge --install java
    - gauge --install html-report
    - gauge --install xml-report
    - gauge --install screenshot
2. Install idea intelliJ community edition
3. After installing IntelliJ you will need to install the Gauge plugin as follows:
   - Open the plugin UI by selecting file->settings->plugins
   - Search for Gauge and then seearch all repositories
   - Double click on the Gauge info on the left to install the plugin.
   - To enable the Gauge plugin you will need to resstart IntelliJ

******************************************************************************************************


## Executing Gauge Tests in parallel

You can execute the specification as:

```bash
  gauge run specs
```
or
```bash
  mvn gauge:execute
```
or
```bash
  mvn test
```

You can run tests in parallel

```bash
  gauge run --parallel -n=2 specs
```
default browser id CHROME but you can run on firefox

```bash
  mvn gauge:execute -DspecsDir=specs -Denv=firefox
```

## Re-run Failed

command line

```bash
  gauge run --failed specs
```

Maven

```bash
  mvn gauge:execute -DspecsDir=specs -Dflags=--failed -Denv=firefox
```

To use a different browser, set the environment property `BROWSER` in `project_dir/env/default/UImap.properties`.

```
BROWSER = IE
```






