# This application will manage users with task per user.

NOTE: Partial update of the Task should use PATCH and not PUT. PATCH is designed for updated of certain fields,
    PUT is for the replacement of the entity in totality. ... bad design.


The application requires a database connection for a MySQL database.<br>
Create a new database with the following schema:
```$xslt
taskmanager_db
```
Update the application.yml file with the username and password of the new database.<br>
Current configuration is username root with password admin.

Unit and Integration testing are done against a H2 Memory database.

Both databases are initialized with Flyway. The scripts are in the /resources/db/migrate folder.

Spring profiles determine the datasource configuration to use. There are datasource properties in the application-test.yml
file under the ./test/resources folder.

Hibernate envs are used to populate the User and Task table auditing columns.