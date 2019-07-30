# TvMaze
The aim of this app is to replicate the high level functionality of www.tvmaze.com and showcase an android app out of it. 
It connects with [TVDB API](api.thetvdb.com) to give you popular shows and let you mark anyone as favorite
TvMaze consists of 3 pieces of UI right now, Home with Popular Shows, Favorites and All Shows. This app is under heavy development

## Android Development and Architecture
* The entire codebase is in [Kotlin](https://kotlinlang.org/)
* Uses Kotlin [Coroutines](https://kotlinlang.org/docs/reference/coroutines/coroutines-guide.html)
* Uses MVVM Architecture by [Architecture Components](https://developer.android.com/topic/libraries/architecture/). Room, LiveData, ViewModel, Paging
* Uses [dagger-android](https://google.github.io/dagger/android.html) for dependency injection
* Unit Testing by [Mockito](https://github.com/mockito/mockito)
* Tests Coroutines and architecture components like ViewModel
* UI Test by [Espresso](https://developer.android.com/training/testing/espresso) based on [Robot Pattern](https://academy.realm.io/posts/kau-jake-wharton-testing-robots/)

*Note* For reference, the Java Codebase has been tagged on `tvmaze_java`. Just checkout the tag and you are in TvMaze Java Land.

## Further Reading
There are several articles written on this repository which states the design and architecture. 

### Kotlin Everywhere. Coroutines, Tests, Robots and much more…
The TvMaze complete repository has been re-written in Kotlin with Coroutines covering
Unit Tests across ViewModels and UI tests for the app.
Know more:
[Kotlin Everywhere. Coroutines, Tests, Robots and much more…](https://medium.com/@kumarashwini/kotlin-everywhere-coroutines-tests-robots-and-much-more-b02030206cc9)

### MVVM using Android Architecture Components
The codebase tries to follow Uncle Bob Clean Code Architecture with [SOLID principles](https://en.wikipedia.org/wiki/SOLID).
Know more:
[Migration from MVP to MVVM using Android Architecture Components](https://medium.com/@kumarashwini/migration-from-mvp-to-mvvm-using-android-architecture-components-4bc058a1f73c)

### Pagination using Paging Library 
The Shows screen displays the list of shows fetched from TvMaze API using `PagingLibray` of Android Architecture Components. 
It also handles the retry if any network error occurred.
Know more:
[Pagination using Paging Library with RxJava and Dagger](https://medium.com/@kumarashwini/pagination-using-paging-library-with-rxjava-and-dagger-d9d05dbd8eac)

### Room Persistence Library
The Favourites screen displays the list of shows marked favourites from the Home screen. The user can add/remove from 
the favorites as and when required. The implementation of the favorites is done using `Room`  Persistence Library with RxJava and Dagger. 
Know more:
[Room with RxJava and Dagger](https://medium.com/@kumarashwini/room-with-rxjava-and-dagger-2722f4420651)

### Static Code Analysis
It also has Static Code Analysis tools integrated. Want to know more. 
Know more:
[Static Code Analysis for Android Using FindBugs, PMD and CheckStyle](https://blog.mindorks.com/static-code-analysis-for-android-using-findbugs-pmd-and-checkstyle-3a2861834c6a)

## Contributions

If you've found an issue in this sample, please file an issue.
Better yet, if you want to contribute to the repository, go ahead, any kind of patches are encouraged,
and may be submitted by forking this project and submitting a pull request. 
If you have something big in mind, or any architectural change, plz raise an issue first to discuss it.
