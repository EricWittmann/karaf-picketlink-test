Testing in Jetty
================

To start the server, *simply* do this:

    mvn clean package -Pjetty

Then point your browser here:

    http://localhost:8181/sp1/index.html

Now you can log in with the pre-configured admin user:

admin/admin


Testing in Fuse
===============

In Fuse, configure your users/roles in the following file:

    fuse/etc/users.properties

Add some users to this file, for example:

    admin=admin,manager
    eric=eric,sales

Once this is done, fire up Fuse and run the following commands:

    features:addurl mvn:org.overlord/karaf-picketlink-test-fuse6/VERSION/xml/features
    features:install -v karaf-picketlink-test


Note: make sure that you have done a maven install from the root of the project so that
all of the modules are available in the .m2 directory.
