Soccer API
===================


This is a free soccer API based on JAVA and Spring (Boot) technology. This guide will explain how to deploy the API onto free platforms such as [Heroku](https://www.heroku.com "Heroku"). This is always a working copy.

----------

# Table of Contents
[1. Heroku deploy](#heroku-deploy)

[2. Consuming the api](#consuming-the-api)


## Heroku deploy
### How to begin
To deploy on Heroku, you need to create a free account. Then, connect your code (deploy) to the repository. You will be able to deploy the code with one click. 

> **Note:**
> You can also use the Heroku CLI to do this. However, the GUI is quite intuitive and you can even choose to deploy the app when changes are pushed to git. Jolly!

Then add a database addon to the project. You can use whatever database you like, but the code has only been tested on a MySQL database. Since the API is using a database management tool ([Liquibase](https://www.liquibase.org)), you can probably use whatever you like.

### Next steps
The config. Do not forget it, otherwise the app won't start. Look for the placeholders (format ${FOO.BAR}) in the heroku.properties file and make sure you set all of them. For testing, you can use dummy values for these properties. The property values are (or will be) commented, explaining their purpose.

### Start up the app
Now check the logs and make sure the app is starting up correctly. That's it!

## Consuming the api

Since this is an API, a description of it is quite necessary. A static description however, is not available. This API uses Springfox swagger to generate the API description on the fly. If you run the app locally, you can find the description at [http://localhost:8080/api/v1/api-docs](http://localhost:8080/api/v1/api-docs). The swagger GUI can be found at [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html).

> **Hint:**
> Swagger has some super code generation tools. Copy the api docs into the [Swagger Editor](http://editor.swagger.io) and just generate the code library you need. Handy.