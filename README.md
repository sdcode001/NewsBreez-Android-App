# NewsBreez-Android-App
## App UI:
Ui of this app is fully responsive and almost similar to the suggested ui given in the assignment.
## Api call:
Here i have used Retfofit to make the api call to newsapi.org and getting the news data. After getting the data from the responce Gson converter is used to convert json  data to custom data class. while making the api call i use the query parameter country=in to get only indian news headlines.
## Data Storage:
After getting a list of news in responce, i have used a object called NewsData to store the list. i ahve used this object to store data because it can be easily accessible from every activity in this app.
## Search News Feature:
a search view is used and function is written to filter out the news list according to the searched text. this function search the typed text in the title of news and simultaneously update the recyclerview.
## Saved News Feature:
every news has a save button, on clicking this user can save the news to watch later and the button will show saved. a separate activity is used to show all the the saved news and also clicking the saved button user can unsave the news from the saved list. To achieve this feature a integer variable called "isSaved"(value 1 or 0) is used in the data class of news.
