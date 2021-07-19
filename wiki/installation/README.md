# Instalation

1. [Google credentials](#google-credentials).
2. [OKTA configuration](#okta-configuration)
3. [Setup application](#setup-application)
4. [Setup postman](#setup-postman)

# Google credentials

1. Create account or login into https://console.cloud.google.com/

<details>
  <summary>2. Create OAuth credentials</summary>
  
  ![OAuth Google](https://raw.githubusercontent.com/DavidNS/test-builder/master/wiki/installation/images/GOOGLE_CREDENTIALS_1.png)
  ![OAuth Google](https://raw.githubusercontent.com/DavidNS/test-builder/master/wiki/installation/images/GOOGLE_CREDENTIALS_2.png)
</details>
<details>
  <summary>3. Store client-id and client-secret for okta</summary>
  
  ![OAuth Google](https://raw.githubusercontent.com/DavidNS/test-builder/master/wiki/installation/images/GOOGLE_CREDENTIALS_3.png)
</details>


# OKTA configuration

1. Create account or login into https://developer.okta.com/signup/

<details>
  <summary>2. Create OAuth2 service. Allow postman as your client</summary>
  
  ![OAuth OKTA](https://raw.githubusercontent.com/DavidNS/test-builder/master/wiki/installation/images/OKTA_1.png)
  ![OAuth OKTA](https://raw.githubusercontent.com/DavidNS/test-builder/master/wiki/installation/images/OKTA_2.png)
  ![OAuth OKTA](https://raw.githubusercontent.com/DavidNS/test-builder/master/wiki/installation/images/OKTA_3.png)
</details>
<details>
  <summary>3. Link OKTA with google OAuth credentials. Use client-id and client-secret form Google-Credentials-Step-3</summary>
  
  ![OAuth OKTA_GOOGLE](https://raw.githubusercontent.com/DavidNS/test-builder/master/wiki/installation/images/OKTA_4.png)
</details>


# Setup application

## Required environment variables

Default values:

- OKTA_OAUTH_ISSUER: `https://YOUR_OTKA_DOMAIN/oauth2/default`
- OKTA_OAUTH_CLIENT_ID: OKTA OAuth client-id
- OKTA_OAUTH_CLIENT_SECRET: OKTA OAuth client secret

## Run on eclipse 

Require install [lombok](https://projectlombok.org/setup/eclipse). You can just run the lombok-(version).jar which shall be located on maven dependencies of the project to start the installer. Locate your eclipse and restart when installation finishes.

## Run with maven

Require install [maven](https://maven.apache.org)

```bash
    sudo apt udpate
    sudo apt install maven
```

Package with maven but skiping the test and run the .jar

```bash
    mvn -Dmaven.test.skip package
    java -jar target/rest-test-builder-0.0.1-SNAPSHOT.jar
```

## Run with docker and docker-compose.

Requires:

1. Package the application with maven.
2. [Docker](https://docs.docker.com/engine/install/) and [docker-compose](https://docs.docker.com/compose/install/)
3. Edit the environment in /docker-compose.yml file

Build and start application with:

```bash
sudo docker-compose up
```

# Setup postman

We will use [postman](https://www.postman.com/) aplication as our client. Import the postman collection samples located in /postman.

## Required postman variables

Default values:

- url: `localhost:8080`
- oauth-client-id: OKTA OAuth client-id
- oauth-client-secret: OKTA OAuth client secret
- oauth-auth-url: `https://YOUR_OTKA_DOMAIN/oauth2/default/v1/authorize`
- oauth-token-url: `https://YOUR_OTKA_DOMAIN/oauth2/default/v1/token`
