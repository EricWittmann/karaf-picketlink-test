karaf-picketlink-test
=====================

To start the server, do this:

----
mvn clean package -Pjetty
----

Then point your browser here:

----
http://localhost:8181/sp1/index.html
----

Testing in Fuse
~~~~~~~~~~~~~~~

----
features:addurl mvn:org.overlord/karaf-picketlink-test-fuse6/VERSION/xml/features
features:install -v karaf-picketlink-test
----
