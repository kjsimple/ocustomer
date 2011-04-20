Installation
	
   1. Install software
      
      The opencustomer application works within a Tomcat server and with a
      MySQL Database.
      You have to install yourself:
      
         Java 5 and higher            (http://java.sun.com)
         Apache Tomcat 5.5 and higher (http://tomcat.apache.org)
         MySQL Database 5 and higher  (http://www.mysql.com)
         
      For the configuration and deployment of opencustomer we use:
      
         Ant 1.6 or higher            (http://ant.apache.org)
	
   2. Change build.properties
   
      Please change the build.properties file to work with your system. The 
      most important change is:
      
         server.root - The path to your tomcat server
	
   3. Update configurations
	
      If you have installed the tool 'patch' on your computer and the tool is 
      on your execution path, you can call:
      
         ant tomcat_configure
         
      In all other cases, you have to patch the files manually.

   4. Install database
   
      You have to use a MySQL Database 5. 
      
      Create a schema and a user/password for this schema.
      
      Run the database scripts to create the database as necessary:
      
         /database/create.sql
         /database/data.sql
         
      One of the patches is changing the server.xml of your tomcat. The patch
      works with:
         
         server   = 127.0.0.1
         port     = 3306
         
         schema   = opencustomer
         user     = opencustomer
         password =
         
      If you want to use different database settings, you have to change the 
      database settings in the server.xml yourself.

   5. Deploy application
   
      You can build and deploy the application by calling:
       
         ant deploy
         
   6. Start using OpenCustomer
   
      When importing data in the database a user will be created:
      
         user:     admin
         password: admin
         
      Please change the password of this user immediately
      
      This user is an administrative user, because he belongs to an 
      administrative role. This usershould only be used for administrative 
      purpose.
      Create new users for the daily work using not administrative roles.
      
      Users in administrative roles are restricted in the normal application.