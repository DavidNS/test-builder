# Test-builder

API wich allows execute parallel REST request and gives stess test results.

# Requirements

1. Google credentials.
2. OKTA configuration.
3. Setup the application
4. Test with postman

# Google credentials

1. Create account or login into https://console.cloud.google.com/
2. Create OAuth credentials.
3. Store client-id and client-secret for okta.

# OKTA configuration

1. Create account or login into https://developer.okta.com/signup/
2. Create OAuth2 service.
3. Link OKTA with google OAuth credentials.

# Setup applications

1. Run on eclipse (Require install lombok. Double click on a .jar located on maven dependencies, locate your eclipse and restart).
2. Run with docker and docker-compose (TODO).

# Test with postman

Install in your postman collection samples located in /postman.

## Required postman variables

- url: Your api url `localhost:8080`
- oauth-client-id: OKTA OAuth client-id
- oauth-client-secret: OKTA OAuth client secret
- oauth-auth-url: https://YOUR_OTKA_DOMAIN/oauth2/default/v1/authorize
- oauth-token-url: https://YOUR_OTKA_DOMAIN/oauth2/default/v1/token


