# Getting started

1. [Postman collection](#postman-collection)
2. [Get access token](#get-access-token)
3. [Create and execute test](#create-and-execute-test)
4. [Swagger definition](#swagger-definition)

# Postman collection
  
<details>
  <summary>Once we have imported the postman collection it should look like this</summary>

  ![Postman Folders](https://raw.githubusercontent.com/DavidNS/test-builder/master/wiki/gettingstarted/images/POSTMAN_1.png)
  
</details>


# Get access token

To be able to send a request first is needed to request a token.
  
<details>
  <summary>Open main folder settings to see authentication screen. Click on get access token to start login process</summary>

  ![Token Google](https://raw.githubusercontent.com/DavidNS/test-builder/master/wiki/gettingstarted/images/POSTMAN_2.png)
  
</details>
<details>
  <summary>A web browser shall display a login window. Open the help options and select sign in with google</summary>

  ![Token Google](https://raw.githubusercontent.com/DavidNS/test-builder/master/wiki/gettingstarted/images/POSTMAN_OKTA_LOGIN.png)
  
</details>
<details>
  <summary>When authentication is completed you should receive a token in postman</summary>

  ![Token Google](https://raw.githubusercontent.com/DavidNS/test-builder/master/wiki/gettingstarted/images/POSTMAN_3.png)
  
</details>

# Create and execute test

<details>
  <summary>Create a user-workspace-project-test by sending post</summary>

  ![Token Google](https://raw.githubusercontent.com/DavidNS/test-builder/master/wiki/gettingstarted/images/POSTMAN_4.png)
  
</details>

<details>
  <summary>The update step request specifies your test configuration. Replace your authorization token and send the request</summary>

  ![Token Google](https://raw.githubusercontent.com/DavidNS/test-builder/master/wiki/gettingstarted/images/POSTMAN_5.png)
  ![Token Google](https://raw.githubusercontent.com/DavidNS/test-builder/master/wiki/gettingstarted/images/POSTMAN_6.png)
  
</details>

<details>
  <summary>Execute your test and check the results</summary>

  ![Token Google](https://raw.githubusercontent.com/DavidNS/test-builder/master/wiki/gettingstarted/images/POSTMAN_7.png)
  
</details>

# Swagger definition

The swagger definition is located by default in:

- http://localhost:8080/swagger-ui/index.html?configUrl=/v3/api-docs/swagger-config