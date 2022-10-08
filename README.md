# NewsBreez-Android-App
## Api call:
Here i have used Retfofit to make the api call to newsapi.org and getting the news data. After getting the data from the responce Gson converter is used to convert json  data to custom data class. while making the api call i use the query parameter country=in to get only indian news headlines.
## Data Storage:
After getting a list of news in responce, i have used a object called NewsData to store the list. i ahve used this object to store data because it can be easily accessible from every activity in this app.
