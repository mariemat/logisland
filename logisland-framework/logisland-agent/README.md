# Swagger Jersey generated server

## Overview
This server was generated by the [swagger-codegen](https://github.com/swagger-api/swagger-codegen) project. By using the 
[OpenAPI-Spec](https://github.com/swagger-api/swagger-core/wiki) from a remote server, you can easily generate a server stub.  This
is an example of building a swagger-enabled JAX-RS server.

This example uses the [JAX-RS](https://jax-rs-spec.java.net/) framework.

To run the server, please execute the following:

```
mvn clean package jetty:run
```

You can then view the swagger listing here:

```
http://localhost:8080/agent/api/v0.10.0/swagger.json
```

Note that if you have configured the `host` to be something other than localhost, the calls through
swagger-ui will be directed to that host and not localhost!


```
swagger-codegen generate --group-id com.hurence.logisland  --artifact-id logisland-agent --artifact-version 0.10.0-SNAPSHOT --api-package com.hurence.logisland.agent.rest.api --model-package com.hurence.logisland.agent.rest.model -o logisland-framework/logisland-agent -l jaxrs --template-dir logisland-framework/logisland-agent/src/main/raml/templates -i logisland-framework/logisland-agent/src/main/raml/api-swagger.yaml
```