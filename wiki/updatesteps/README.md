# Update steps

Currently the application allows us to create a user->workspace->project->test. Based mainly on the structure of the postman application. Each test contains a list of steps which will be executed following the step order.

1. [Kind of steps](#kind-of-steps)
1. [Reserved names](#reserved-names)

# Kind of steps

## Edit field step

This step get an input json and transform the value of one of their keys through one of the functions written in the `EditScripts.class`. To check the name of the allowed methods and their parameters the applications provides a GET /editscripts endpoint. (TODO: This endpoint have currently a bug and also show inherit methods)


Structure:

```json
{
    "name": "EditFieldStep",
    "stepOrder": 0,
    "stepKind": "EDIT_FIELD",
    "stepModel": {
        "inJson": "{\"user\":{ \"name\":\"Mario\", \"id\":\"\"  },\"randomNumber\":\"\"}",
        "plainKeyVsMehtod": {
            "user.id": "getNewDNINumber",
            "requestID": "generateRandomIntIntRange 1 100000"
        }
    }
}
```

Expected step output:

```json
{
    "user":{
    	"name": "Mario",
    	"id": "94663147T"
    },
    "randomNumber" : "95844"
}
```

## Map fields step

This steps allows us to combine previous steps inputs or outputs to generate combinations with their results.

Structure:

```json

{
    "name": "MapFieldStep",
    "stepOrder": 1,
    "stepKind": "MAP_FIELD",
    "stepModel": {
        "inJsons": [
            "{\"name\":\"Bender\"}",
            "{\"lastname\":\"Rodriguez\"}"
        ],
        "outJson": {
            "mergedName": "Name: /AND/STEP_1.IN_0.name/AND/, Lastname: /AND/STEP_1.IN_1.lastname/AND/, ID: /AND/STEP_0.OUT.user.id"
        }
    }
}
```

Expected output:

```json
{
    "mergedName": "Name: Bender, Lastname: Rodriguez, ID: 94663147T"
}
```

## Request step

This step allows us to send requests to collect some kind of data needed previous the main request step.

Structure:

```json
{
    "name": "RequestStep",
    "stepOrder": 2,
    "stepKind": "SEND_REQUEST",
    "stepModel": {
	    "url": "http://localhost:8080/<<requestParam>>",
	    "urlParamKeyVSCombination": {
	    	"requestParam": "STEP_1.OUT.mergedName"
	     },
	     "addHeaders":{
	     	"SomeHeadderKey" : "SomeHeadderValue",
	     	"SomeHeadderKey2" : "SomeHeadderValue2"
	     },
	     "deleteHeaders":{
	       "SomeHeadderKey"
	     },
	     "method": "POST",
	     "inJson": "STEP_1.OUT"
    }
}
```
Expected output:

- The output as string received from the endpoint.

## Main Request Step

Each test must have one and only one main request step. This is the request which we will send to the tested endpoint. The main request step contains inside the structure of the [request step](#request-step) and also the stressConditions and the expectedPerformaceResults.


```json
{
    "name": "MainRequestStep",
    "stepOrder": 3,
    "stepKind": "SEND_MAIN_REQUEST",
    "stepModel": {
        "requestStepModel": {
            "url": "http://localhost:8080/<<requestParam>>",
            "urlParamKeyVSCombination": {
                "requestParam": "STEP_1.OUT.mergedName"
            },
            "addHeaders":{
                "Authorization" : "Bearer MY_TOKEN"
            },
            "method": "GET"
        },
        "stressConditions": {
            "numberOfParallelRequest": 1,
            "delayBetweenParallelRequest": 0,
            "numberOfTest": 1,
            "delayBetweenParallelTest": 3000
        },
        "expectedPerformaceResults": {
            "expectedSingleTime": 3000,
            "expectedPararellTime": 3000,
            "expectedTotalTime": 3000,
            "forceTimeoutByMaxExpectedTime": false,
            "responseSuccessKind": "AS_EXPECTED",
            "expectedHttpStatus": 200,
            "output": "Hellooooooooo /AND/STEP_1.OUT.mergedName"
        }
    }
}
```

# Reserved names

The application allows us to use reserved names to indicate a reference to the already provided inputs/outputs of already processed steps.

The expressions to refer this are:

- `STEP_`+`STEP_INDEX`: To reference the step. (STEP_0)
- `IN_`+`IN_INDEX` or `OUT`: Reference to input or output. Note that output not needs indicate an index, but input requires it because the map field step (IN_0 || OUT)
- `.` : Dot symbol is used to indicate the end of reserved name. (STEP_0.IN_1.user.name || STEP_1.OUT)
- `/AND/` : This symbol is used to start new reserved name combination. (STEP_0.IN_1.user.name/AND/STEP_0.IN_1.user.lastname)
- `<<myKeyIdentifier>>`: This symbol is used to identify our replacements. Currently is only used in the request step for the url params.
