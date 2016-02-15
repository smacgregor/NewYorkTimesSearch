# Project 2 - News Reader

News Reader is an android app that allows a user to search for news articles on the web using simple filters. The app utilizes [New York Times Search API](http://developer.nytimes.com/docs/read/article_search_api_v2).

Time spent: 22 hours spent in total

## User Storiesm

The following **required** functionality is completed:

* [X] User can **search for news article** by specifying a query and launching a search. Search displays a grid of image results from the New York Times Search API.
* [X] User can click on "settings" which allows selection of **advanced search options** to filter results
* [X] User can configure advanced search filters such as:
  * [X] Begin Date (using a date picker)
  * [X] News desk values (Arts, Fashion & Style, Sports)
  * [X] Sort order (oldest or newest)
* [X] Subsequent searches have any filters applied to the search results
* [X] User can tap on any image in results to see the full text of article **full-screen**
* [X] User can **scroll down to see more articles**. The maximum number of articles is limited by the API search.

The following **optional** features are implemented:

* [X] Implements robust error handling, [check if internet is available](http://guides.codepath.com/android/Sending-and-Managing-Network-Requests#checking-for-network-connectivity), handle error cases, network failures
* [X] Used the **ActionBar SearchView** or custom layout as the query box instead of an EditText
* [X] User can **share an article link** to their friends or email it to themselves
* [X] Replaced Filter Settings Activity with a lightweight modal overlay
* [X] Improved the user interface and experiment with image assets and/or styling and coloring

The following **bonus** features are implemented:

* [X] Use the [RecyclerView](http://guides.codepath.com/android/Using-the-RecyclerView) with the `StaggeredGridLayoutManager` to display improve the grid of image results
* [X] For different news articles that only have text or only have images, use [Heterogenous Layouts](http://guides.codepath.com/android/Heterogenous-Layouts-inside-RecyclerView) with RecyclerView
* [X] Apply the popular [Butterknife annotation library](http://guides.codepath.com/android/Reducing-View-Boilerplate-with-Butterknife) to reduce view boilerplate.
* [X] Leverage the popular [GSON library](http://guides.codepath.com/android/Using-Android-Async-Http-Client#decoding-with-gson-library) to streamline the parsing of JSON data.
* [X] Replace Picasso with [Glide](http://inthecheesefactory.com/blog/get-to-know-glide-recommended-by-google/en) for more efficient image rendering.
* [X] Use Parcelable instead of Serializable using the popular Parceler library. 
* [X] Leverage the popular GSON library to streamline the parsing of JSON data.

The following **additional** features are implemented:

* [X] Render mobile friendly web urls for articles 
* [X] When seelcting a thumbnail to use for the staggered view, filter out small thumbnail images as they don't render well on mobile.
* [X] Search by relevancy to the search query as an additional sort order
* [X] Add an additional News Desk for Foreign
* [X] Add recycler view adapter animation using https://github.com/wasabeef/recyclerview-animators (animation from the bottom)
* [X] Glide integration included changes to use ARGB_8888 for higher quality bitmaps. Make it shine!
* [X] Set the headline kicker as the title of the article activity
* [X] Use snackbar to inform the user if there's an issue with network connectivity or the server returend an error
* [X] Use snackbar to inform the user if there are no more search results matching their query
* [X] Use snackbar to inform the user that there where no search results that matched their query

## Video Walkthrough

Here's a walkthrough of implemented user stories:

<img src='https://cloud.githubusercontent.com/assets/1521460/13040442/fb4f0eea-d360-11e5-85f7-4168258ba75f.gif' title='Video Walkthrough' width='' alt='Video Walkthrough' />

GIF created with [LiceCap](http://www.cockos.com/licecap/).

## Notes

Describe any challenges encountered while building the app.

## Open-source libraries used

- [Android Async HTTP](https://github.com/loopj/android-async-http) - Simple asynchronous HTTP requests with JSON parsing
- [Glide](https://github.com/bumptech/glide) - Image loading and caching library for Android
- [Dynamic Height Image View](https://github.com/etsy/AndroidStaggeredGrid/blob/master/library/src/main/java/com/etsy/android/grid/util/DynamicHeightImageView.java)  - Used for image resizing
- [Parceler](https://github.com/johncarl81/parceler) - Remove boilerplate around making model objects parcelable
- [RecyclerView Animators](https://github.com/wasabeef/recyclerview-animators) - Make RecyclerView animations easy!
- [Gson](https://github.com/google/gson) - streamline JSON parsing into models
- [Butterknife](http://jakewharton.github.io/butterknife/) - Remove view binding boilerplate

## License

    Copyright 2016 Scott MacGregor

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
