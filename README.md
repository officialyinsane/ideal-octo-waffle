### Process Restarter

Aimed for DCS dedicated servers, this initial version will take a look once a minute, and if the application isn't
running, will start it.

This was all written and tested while drinking a cup of coffee. All complaints to the management...

### How to run it?

For now, build it using Maven (or speak to me nicely), ensure you have Java 21 installed, and
then, `java -jar dcs-restart.jar` will do it. But before you get ahead of yourself, see the configuration stuff. Don't 
complain at me if you didn't read it.

### Want to change how it works?

Sure, copy `resources/application.properties` and put it along side the Jar file. Want to know what the config items
mean?

| Key           | Default Value | Meaning                                                                           |
|---------------|---------------|-----------------------------------------------------------------------------------|
| app-arguments | -server       | Arguments to pass to the application, the default tells DCS to run in server mode |
| path-to-exe   | c:\blah...    | I don't know where you installed DCS, you'll want to change this one...           |
| process-name  | DCS.exe       | Well, DUH. But if you do run multithreaded, you'll change this too.               |
