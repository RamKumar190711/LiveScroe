package com.toqsoft.livescore.domain.utill

fun getFlagUrl(teamName: String): String {
    return when (teamName) {
        // International Men Teams
        "India" -> "https://flagcdn.com/w40/in.png"
        "Australia" -> "https://flagcdn.com/w40/au.png"
        "England" -> "https://flagcdn.com/w40/gb.png"
        "Pakistan" -> "https://flagcdn.com/w40/pk.png"
        "New Zealand" -> "https://flagcdn.com/w40/nz.png"
        "SouthAfrica" -> "https://flagcdn.com/w40/za.png"
        "Sri Lanka" -> "https://flagcdn.com/w40/lk.png"
        "Bangladesh" -> "https://flagcdn.com/w40/bd.png"
        "Zimbabwe" -> "https://flagcdn.com/w40/zw.png"
        "Afghanistan" -> "https://flagcdn.com/w40/af.png"
        "West Indies" -> "https://flagcdn.com/w40/tt.png"
        "Ireland" -> "https://flagcdn.com/w40/ie.png"
        "Nepal" -> "https://flagcdn.com/w40/np.png"
        "UAE" -> "https://flagcdn.com/w40/ae.png"
        "Scotland" -> "https://flagcdn.com/w40/sc.png"

        // IPL / Men’s Franchise Teams
        "Mumbai Indians" -> "https://upload.wikimedia.org/wikipedia/en/f/f4/Mumbai_Indians_Logo.svg"
        "Chennai Super Kings" -> "https://upload.wikimedia.org/wikipedia/en/6/6b/Chennai_Super_Kings_Logo.svg"
        "Royal Challengers Bangalore" -> "https://upload.wikimedia.org/wikipedia/en/9/9e/Royal_Challengers_Bangalore_Logo.svg"
        "Kolkata Knight Riders" -> "https://upload.wikimedia.org/wikipedia/en/2/2a/Kolkata_Knight_Riders_Logo.svg"
        "Rajasthan Royals" -> "https://upload.wikimedia.org/wikipedia/en/1/19/Rajasthan_Royals_Logo.svg"
        "Delhi Capitals" -> "https://upload.wikimedia.org/wikipedia/en/4/42/Delhi_Capitals_Logo.svg"
        "Sunrisers Hyderabad" -> "https://upload.wikimedia.org/wikipedia/en/d/d3/Sunrisers_Hyderabad_Logo.svg"
        "Lucknow Super Giants" -> "https://upload.wikimedia.org/wikipedia/en/2/21/Lucknow_Super_Giants_logo.svg"
        "Gujarat Titans" -> "https://upload.wikimedia.org/wikipedia/en/1/1a/Gujarat_Titans_Logo.svg"

        // Women’s IPL / WPL Teams
        "Mumbai Indians Women" -> "https://upload.wikimedia.org/wikipedia/en/f/f4/Mumbai_Indians_Logo.svg"
        "Delhi Capitals Women" -> "https://upload.wikimedia.org/wikipedia/en/4/42/Delhi_Capitals_Logo.svg"
        "UP Warriorz" -> "https://upload.wikimedia.org/wikipedia/en/0/0f/UP_Warriorz_Logo.svg"
        "Royal Challengers Bangalore Women" -> "https://upload.wikimedia.org/wikipedia/en/9/9e/Royal_Challengers_Bangalore_Logo.svg"
        "Gujarat Giants Women" -> "https://upload.wikimedia.org/wikipedia/en/1/1a/Gujarat_Titans_Logo.svg"

        // BBL / Other League Teams (sample)
        "Sydney Sixers" -> "https://upload.wikimedia.org/wikipedia/en/2/28/Sydney_Sixers_Logo.svg"
        "Melbourne Stars" -> "https://upload.wikimedia.org/wikipedia/en/2/2e/Melbourne_Stars_Logo.svg"
        "Brisbane Heat" -> "https://upload.wikimedia.org/wikipedia/en/9/9a/Brisbane_Heat_Logo.svg"
        "Perth Scorchers" -> "https://upload.wikimedia.org/wikipedia/en/1/1b/Perth_Scorchers_Logo.svg"
        "Adelaide Strikers" -> "https://upload.wikimedia.org/wikipedia/en/9/9f/Adelaide_Strikers_Logo.svg"

        else -> "" // placeholder or empty for unmapped team
    }
}
