# Instalation

1. Google credentials.
2. OKTA configuration.
3. Setup the application
4. Test with postman

# Google credentials

1. Create account or login into https://console.cloud.google.com/
<details>
  <summary>2. Create OAuth credentials</summary>
  
  ![OAuth Google](https://raw.githubusercontent.com/DavidNS/test-builder/master/wiki/installation/images/GOOGLE_CREDENTIALS_1.png)
  ![OAuth Google](https://raw.githubusercontent.com/DavidNS/test-builder/master/wiki/installation/images/GOOGLE_CREDENTIALS_2.png)
</details>
<details>
  <summary>3. Store client-id and client-secret for okta</summary>
  
  ![OAuth Google](https://raw.githubusercontent.com/DavidNS/test-builder/master/wiki/images/GOOGLE_CREDENTIALS_3.png)
</details>


# OKTA configuration

1. Create account or login into https://developer.okta.com/signup/

<details>
  <summary>2. Create OAuth2 service. Allow postman as your client</summary>
  
  ![OAuth Google](https://raw.githubusercontent.com/DavidNS/test-builder/master/wiki/installation/images/OKTA_1.png)
  ![OAuth Google](https://raw.githubusercontent.com/DavidNS/test-builder/master/wiki/installation/images/OKTA_2.png)
  ![OAuth Google](https://raw.githubusercontent.com/DavidNS/test-builder/master/wiki/installation/images/OKTA_3.png)
</details>
<details>
  <summary>3. Link OKTA with google OAuth credentials. Use client-id and client-secret form Google-Credentials-Step-3</summary>
  
  ![OAuth Google](https://raw.githubusercontent.com/DavidNS/test-builder/master/wiki/installation/images/OKTA_4.png)
</details>


# Setup application

## Required environment variables

- OKTA_OAUTH_ISSUER: https://YOUR_OTKA_DOMAIN/oauth2/default
- OKTA_OAUTH_CLIENT_ID: OKTA OAuth client-id
- OKTA_OAUTH_CLIENT_SECRET: OKTA OAuth client secret

## Run on eclipse 

Require install [lombok](https://projectlombok.org/setup/eclipse). You can just run the lombok-(version).jar which shall be located on maven dependencies of the project to start the installer. Locate your eclipse and restart when instalation finishes.

## Run with docker and docker-compose.

TODO

# Test with postman

Install in your postman collection samples located in /postman.

## Required postman variables

- url: Your api url `localhost:8080`
- oauth-client-id: OKTA OAuth client-id
- oauth-client-secret: OKTA OAuth client secret
- oauth-auth-url: https://YOUR_OTKA_DOMAIN/oauth2/default/v1/authorize
- oauth-token-url: https://YOUR_OTKA_DOMAIN/oauth2/default/v1/token