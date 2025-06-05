Purpose of the App

Serve Custom (probably static initially, but might add more fluff) WebPages Via Android Devices.

User Journey

1.User creates a hotspot. 

2. User opens app. 

3. User provides HTML (do we serve images as well?) to Text Input field.

4. When another User connects to this hotspot, and goes to some site
   (127.0.0.0 or where-ever it is set, maybe a port number is also needed)

5. Then user is served the HTML page on their browser. 

Further Enhancements

1. User Can Ask Some LLM to generate a webpage.

2. So just with text idea, user can serve a website for testing.
   (Initially, this communication can happen with a local llm : seems easiest)

3. Eventually, the user can access some app, in background just by typing, and that app can get
   a server response, that will be shared to us. 
   (this needs our app to converse with another app, in the backgroud, and this might not be possible
   	or might require special permissions);

 Why

 1. For Easy Testing, this seems to very easy, espcially in office scenarios, where users are
 not allowed to host anything on their PCs, and expose the connection.
 2. It does not require a Server to host, so no additional cost or hassel.

 Do Alternatives Exist(Updates Required):

 1. Deploy Server on AWS for free( time consuming. )
 2. With some tool like, ngrok, it is possible to do the same, but not from mobile.

