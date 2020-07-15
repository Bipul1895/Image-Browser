# Image-Browser
Search and download image from Flickr and display it to the user

This android app uses flickr rss feed to display images. Data was present in JSON format. 
Downloaded JSON data using HttpURLConnection in the backgroung thread using async task.
Parsed JSON data (also in background thread). Displayed data in list view using custom array adapter.
Used Picasso library for displaying image in image view.
When an item on list view is clicked, an activity providing photo details opens up.
Handled click by implementing OnItemClickListener interface.
Provided search functionality. Flickr provides "tags" for searching images. 
The search query provided by user modifies the original Flickr URL and thus fetches the requested data.
Used Shared Preferences to share data (search query) between two activities.
