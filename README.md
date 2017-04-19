# api-gateway-pattern
Implementing API Gateway pattern with Netflix Zuul and Spring Cloud

Based on http://kubecloud.io/apigatewaypattern/

### Another reference
* https://github.com/Netflix/zuul/wiki/How-it-Works
* https://spring.io/guides/gs/routing-and-filtering/

### How to use
Gradle is required.

Run with `gradle bootRun --parallel`

Call:

```
curl -i -H "Accept: application/json" -H "Content-Type: application/json" -X GET http://localhost:8080/movies/api/v1/ghostbusters
```

and see the logs
