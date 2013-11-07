<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <title>Overlord IDP - Login</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="">

    <link href="resources/idp.css" rel="stylesheet"></link>
    <link href="resources/idp-responsive.css" rel="stylesheet"></link>

    <!-- HTML5 shim, for IE6-8 support of HTML5 elements -->
    <!--[if lt IE 9]>
      <script src="http://html5shim.googlecode.com/svn/trunk/html5.js"></script>
    <![endif]-->

    <script src="resources/jquery-1.9.1/jquery.min.js"></script>
  </head>

  <body>
    <div id="rcue-login-screen">
      <div class="login-logo"></div>
      <form class="form-signin" id="login_form" name="login_form" method="post" action="<%=application.getContextPath()%>/">
        <fieldset>
          <legend><span class="product-name">JBoss Overlord</span></legend>
          <p>
            <label>Username</label>
            <input id="username" type="text" name="JBID_USERNAME"></input>
          </p>
          <p>
            <label>Password</label>
            <input type="password" name="JBID_PASSWORD"></input>
          </p>
          <input type="submit" value="Log In"></input>
        </fieldset>
      </form>
    </div>
  </body>
  
  <script>
    $('#username').focus();
  </script>
</html>
