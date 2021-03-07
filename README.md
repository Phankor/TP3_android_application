# TP3_android_application

- You have to put your id inside the app to connect to your bank account, then you will be able to see it when refreshing.

- The user data is secured with an hashing for the id (created with the function encryptThisString), and the others data are
secured by an encryption with the Base64 library 

- I hide the API url by storing it inside an environmental varaible, so only my machine knows it,
 it reads these values in some way and inject them in the code at build time.

-bonus : how works my app?
this app consist of only one screen were you have one text field to put the id. Once you you wrote an id on it you can press 
the button connection which will keep the id selected in the data of the app, and now you are connected as the account with the
same id. Now you can refresh to see the data.
Between the buttons "connection" and "reset" they are two list_view which will display the information of the account
with the id we are linked.
The button "Refresh" serves to refresh the value with the id.
The button Reset serves to clear the database and change the id to none. Hence you can reconnect with a new id