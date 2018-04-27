# Popular Movies

[![Crates.io](https://img.shields.io/crates/l/rustc-serialize.svg?maxAge=2592000)]()

This is the working app that I developed from scratch for the Android Developer Nanodegree course from Udacity.
The app makes use of different features of Android. Such as - Adaptative UI, Use of Loaders, RecyclerView, Content Providers and so on.

## Main Features

- Find out what are the most popular and highest rated movies from [The Movie Database](https://www.themoviedb.org/documentation/api).
- You can also read information about a specific movie, for example - its average score, release date and overview.
- Trailers and Reviews for each movie are also shown when those are available.
- You might also create a list with your favourites movies and have those available even when you are offline.

## Libraries used in this project
* [Glide](https://github.com/bumptech/glide)
* [Junit](https://developer.android.com/training/testing/unit-testing/local-unit-tests.html)
* [RecyclerView](https://developer.android.com/reference/android/support/v7/widget/RecyclerView)
* [CardView](https://developer.android.com/reference/android/support/v7/widget/CardView)


## Working with the source code

The app retrieves movies from [The Movie Database](https://www.themoviedb.org/documentation/api) API.

In order to use it, you must provide your own api key and place it in:

```
app/src/main/assets/prod.properties

API_KEY=[YOUR_OWN_KEY]
```

## Screenshots
![Main Screen](https://github.com/marcosmcb/popular-movies-app/blob/master/screen_shots/main_screen.jpg){:height="60%" width="60%"}.
![Main Screen Menu](https://github.com/marcosmcb/popular-movies-app/blob/master/screen_shots/main_screen_menu.jpg){:height="60%" width="60%"}.
![Movie Detail Screen](https://github.com/marcosmcb/popular-movies-app/blob/master/screen_shots/movie_detail_screen.jpg){:height="60%" width="60%"}.
![Movie Added to Favourite](https://github.com/marcosmcb/popular-movies-app/blob/master/screen_shots/movie_added_fav.jpg){:height="60%" width="60%"}.
![Movie Landscape](https://github.com/marcosmcb/popular-movies-app/blob/master/screen_shots/movie_landscape.jpg){:height="60%" width="60%"}.



## License

Copyright 2018 Marcos Cavalcante

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.