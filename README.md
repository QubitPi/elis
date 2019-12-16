Elis
====

![](https://upload.wikimedia.org/wikipedia/commons/thumb/6/68/Olive_wreath.svg/375px-Olive_wreath.svg.png)

[Elis](https://elisolympiccoins.com/) is a Jersey+Jetty skeleton Webservice that allows any people to setup and deploy
production-quality [Elide](https://github.com/yahoo/elide) instance very easily and quickly.

[Elide](https://github.com/yahoo/elide) itself offers lots of useful tutorials on setting up "hello-world" local 
instances of Elide, but you probably don't want to CI/CD these light instances to your production environment.
[Elise](https://github.com/QubitPi/elis/src), however, only needs less than 10 minutes from you before you can have
a fully-featured Elide instance in production facing thousands of users.

## Get Elis

First you will need to fork [Elise](https://github.com/QubitPi/elis/src) or simply clone it

    git clone https://QubitPi@github.com/QubitPi/elis.git
    
At this moment you already have a customizable Elide instance on your machine!

The following auxiliary steps will concrete your awesome experience with Elide in just few minutes:

## Setup Elis

To run your own instance of Elide, all you need to do is to put your bean under package
`com.github.QubitPi.webservice.bean`. There has already been a
[bean](src/main/java/com/github/QubitPi/elis/bean/User.java) pre-installed for you, so we will use that for
our Elide runnable.

Note that you can replace that pre-installed bean with your own for your own Elide.

## Pre-Deploy

### 1. Install MySQL

Note: if you are deploying Elide/Elis locally, you can install MySQL via

    brew install mysql
    mysql -uroot
    
At this moment, Elis supports MySQL instances with the following configuration:

    CREATE USER 'elide'@'localhost' IDENTIFIED BY 'elide123';
    GRANT ALL PRIVILEGES ON *.* TO 'elide'@'localhost';

### 2. Setup Database

Login DB as

    mysql -u elide -p # password is elide123
    CREATE DATABASE elide;
    
Make sure MySQL is up and running locally using either

    mysql.server start

or

    mysql.server restart
   
## Deploy Elide/Elis

### 1. Generate WAR File
       
Create a Web Application Archive (WAR) file from the project with the command
       
    mvn clean package
           
The application you just wrote will be compiled into a WAR file, which is `target/Elis-<VERSION>.war`.This WAR file is
your entire WebApp. You will need to drop it to a Jetty server, which will extract the WAR and deploy the WebApp for 
you. We will do this in the following sections.

### 2. Download Jetty

The standalone Jetty distribution is available for download from the
[Eclipse Foundation](https://www.eclipse.org/jetty/download.html).

After the Jetty source code is downloaded, extract it. Take version `9.4.7.v20170914` as an example

    tar -xzvf jetty-distribution-9.4.7.v20170914.tar.gz
    
### 3. Create a new Jetty Base

A Jetty base directory allows the configuration and web applications of a server instance to be stored separately from
the Jetty distribution(the extracted content), so that upgrades can be done with minimal disruption. Jetty's default
configuration is based on two properties:

1. jetty.home - The property that defines the location of the jetty distribution, its libs, default modules and default
XML files (typically start.jar, lib, etc)
2. jetty.base - The property that defines the location of a specific instance of a jetty server, its configuration,
logs, and web applications (typically start.ini, start.d, logs and webapps)

We will create a Jetty base in the same directory as our distribution.

    mkdir jetty-base
    cd Jetty-base
    
Next, we enable both HTTP connector and web application deployer modules using the following command

    java -jar ../jetty-distribution-9.4.7.v20170914/start.jar --create-startd --add-to-start=http,deploy
    
You should see the following output

    MKDIR : ${jetty.base}/start.d
    INFO  : webapp          transitively enabled, ini template available with --add-to-start=webapp
    INFO  : server          transitively enabled, ini template available with --add-to-start=server
    INFO  : security        transitively enabled
    INFO  : servlet         transitively enabled
    INFO  : http            initialized in ${jetty.base}/start.d/http.ini
    INFO  : deploy          initialized in ${jetty.base}/start.d/deploy.ini
    MKDIR : ${jetty.base}/webapps
    INFO  : Base directory was modified

### 4. Deploy WebApp

Once the Jetty base directory is initialized, the content of the directory is the following

    $ ls
    start.d  webapps

Move the WAR file that's been generated to `webapps/` directory and rename it to `ROOT.war`

### 5. Run Jetty

Nagivate to Jetty base directory and start Jetty:

    java -jar ../jetty-distribution-9.4.7.v20170914/start.jar
    
## Use Elis

Now you have a perfect Elide(Elis) instance running. You can try store a new user into database via POST such as the
following:

    curl -X POST -H'Content-Type: application/vnd.api+json' -H'Accept: application/vnd.api+json' --data '{ "data":{ "type":"user", "id":"0", "attributes":{ "name":"Jack" } } }' http://localhost:8080/v1/user

Now you can see this user in `User` table under `elide` database, or you can get it via GET like this

    curl http://localhost:8080/v1/user
