![Android CI](https://github.com/ashwini009/TvFlix/workflows/Android%20CI/badge.svg?branch=master&event=push) ![GitHub top language](https://img.shields.io/github/languages/top/ashwini009/TvFlix?style=plastic) [![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT) [![API](https://img.shields.io/badge/API-21%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=21) ![GitHub stars](https://img.shields.io/github/stars/ashwini009/TvFlix?style=social) ![GitHub forks](https://img.shields.io/github/forks/ashwini009/TvFlix?style=social)


# TvFlix :tv: 

The aim of this app is to replicate the high level functionality of www.tvmaze.com and showcase an android app out of it. 
It connects with [TVDB API](https://api.thetvdb.com) to give you popular shows and let you mark anyone as favorite.
TvFlix consists of 3 pieces of UI right now:
1. Home with Popular Shows
2. Favorites
3. All Shows

This app is under development. :construction_worker: :hammer_and_wrench:

*Note: TvFlix is an unofficial app built only for learning and sharing the latest concepts with #AndroidDevs*

## Android Development and Architecture

* The entire codebase is in [Kotlin](https://kotlinlang.org/)
* Uses Kotlin [Coroutines](https://kotlinlang.org/docs/reference/coroutines/coroutines-guide.html).
* Uses MVVM Architecture by [Architecture Components](https://developer.android.com/topic/libraries/architecture/). Room, ViewModel, Paging
* Uses [Hilt Android](https://developer.android.com/training/dependency-injection/hilt-android) with [Dagger](https://dagger.dev/) for dependency injection
* Unit Testing by [Mockito](https://github.com/mockito/mockito)
* Tests Coroutines and architecture components like ViewModel
* UI Test by [Espresso](https://developer.android.com/training/testing/espresso) based on [Robot Pattern](https://academy.realm.io/posts/kau-jake-wharton-testing-robots/)
* Uses [Kotlin Coroutines Test](https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-test/) to unit test Kotlin Coroutines
* Uses [StateFlow](https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines.flow/-state-flow/) as a replacement over LiveData as a state-holder observable

## Further Reading

There are several articles written on this repository which state the design and architecture. 

### Kotlin Everywhere. Coroutines, Tests, Robots and much more…

The TvFlix complete repository has been re-written in Kotlin with Coroutines covering
Unit Tests across ViewModels and UI tests for the app.
Know more:
[Kotlin Everywhere. Coroutines, Tests, Robots and much more…](https://proandroiddev.com/kotlin-everywhere-coroutines-tests-robots-and-much-more-b02030206cc9)

### MVVM using Android Architecture Components

The codebase tries to follow Uncle Bob Clean Code Architecture with [SOLID principles](https://en.wikipedia.org/wiki/SOLID).
Know more:
[Migration from MVP to MVVM using Android Architecture Components](https://medium.com/@kumarashwini/migration-from-mvp-to-mvvm-using-android-architecture-components-4bc058a1f73c)

### Pagination using Paging Library 

The Shows screen displays the list of shows fetched from TvMaze API using [Paging3](https://developer.android.com/topic/libraries/architecture/paging/v3-overview) of Android Architecture Components. It also handles the retry if any network error occurred. Recently the repository has been [migrated to use Paging3](https://github.com/reactivedroid/TvFlix/pull/14).  
Paging3 is in heavy development, and if you want to catch up with stable library(Paging 2), then check out this blog
[Pagination using Paging Library with RxJava and Dagger](https://medium.com/@kumarashwini/pagination-using-paging-library-with-rxjava-and-dagger-d9d05dbd8eac)

### Room Persistence Library

The Favourites screen displays the list of shows marked favourites from the Home screen. The user can add/remove from 
the favorites as and when required. The implementation of the favorites is done using `Room`  Persistence Library with RxJava and Dagger. 
Know more:
[Room with RxJava and Dagger](https://medium.com/@kumarashwini/room-with-rxjava-and-dagger-2722f4420651)

### Static Code Analysis

TvFlix has Static Code Analysis tools like FindBugs, PMD and Checkstyle integrated. These tools help in finding potential bugs that would have been missed and help in making the codebase clean.
Know more:
[Static Code Analysis for Android Using FindBugs, PMD and CheckStyle](https://blog.mindorks.com/static-code-analysis-for-android-using-findbugs-pmd-and-checkstyle-3a2861834c6a)

## Contributions

If you have found an issue in this sample, please file it.
Better yet, if you want to contribute to the repository, go ahead, any kind of patches are encouraged,
and may be submitted by forking this project and submitting a pull request. 
If you have something big in mind, or any architectural change, please raise an issue first to discuss it.

## License

```
Copyright (c) 2020 Ashwini Kumar

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```
