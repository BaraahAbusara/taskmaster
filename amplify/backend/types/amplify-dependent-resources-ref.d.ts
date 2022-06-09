export type AmplifyDependentResourcesAttributes = {
    "api": {
        "taskmaster": {
            "GraphQLAPIKeyOutput": "string",
            "GraphQLAPIIdOutput": "string",
            "GraphQLAPIEndpointOutput": "string"
        }
    },
    "auth": {
        "taskmaster567d9c06": {
            "IdentityPoolId": "string",
            "IdentityPoolName": "string",
            "UserPoolId": "string",
            "UserPoolArn": "string",
            "UserPoolName": "string",
            "AppClientIDWeb": "string",
            "AppClientID": "string"
        }
    },
    "storage": {
        "taskmaster": {
            "BucketName": "string",
            "Region": "string"
        }
    },
    "analytics": {
        "taskmaster": {
            "Region": "string",
            "Id": "string",
            "appName": "string"
        }
    },
    "predictions": {
        "translateText22c2bd2e": {
            "region": "string",
            "sourceLang": "string",
            "targetLang": "string"
        },
        "speechGenerator42487606": {
            "region": "string",
            "language": "string",
            "voice": "string"
        }
    }
}