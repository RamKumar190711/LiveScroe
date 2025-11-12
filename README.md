Live Cricket Score App
A Jetpack Compose Android app to display live, completed, and upcoming cricket matches with team flags/logos and scores. Supports international teams, IPL, WPL, and other leagues.

Features


Display live and completed matches with real-time scores.


Display upcoming matches with start times.


Show team flags and logos for international and franchise teams.


Supports T20, ODI, and Test match types with color-coded badges.


Uses Coil for image loading with SVG support.


Modern UI built using Jetpack Compose and Material 3.



Installation


Clone the repository:


git clone https://github.com/yourusername/live-cricket-score-app.git



Open in Android Studio.


Add your API key for cricket scores in ApiService.


Build and run the app on an Android device or emulator.



Dependencies


Jetpack Compose


Material3


Coil (with SVG support)


Gradle dependencies:
implementation "io.coil-kt:coil-compose:2.5.0"
implementation "io.coil-kt:coil-svg:2.5.0"


Usage
The app fetches current and upcoming matches from the API and displays:


Match name


Match type (T20 / ODI / Test)


Teams and scores


Start time for upcoming matches


Team flags/logos



Code Highlights
Get Upcoming Matches
fun List<CricketScore>.getUpcomingMatches(): List<CricketScore> {
    val now = ZonedDateTime.now()
    return this.filter { match ->
        match.dateTimeGMT?.let {
            try {
                val matchTime = ZonedDateTime.parse(it, DateTimeFormatter.ISO_DATE_TIME)
                matchTime.isAfter(now)
            } catch (e: Exception) {
                false
            }
        } ?: false
    }
}

Display Scores in Compose
ScoreItem(score = match)



Shows live/completed scores or start time for upcoming matches.


Loads flags/logos using Coil with SVG support.



Contribution


Fork the repository.


Create a new branch: git checkout -b feature/my-feature


Commit changes: git commit -am 'Add some feature'


Push to branch: git push origin feature/my-feature


Create a Pull Request.



License
This project is licensed under the MIT License – see the LICENSE file for details.

If you want, I can also create a version with all supported leagues and teams listed directly in the README so users can see which flags/logos are supported.
Do you want me to do that?
