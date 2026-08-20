import pandas as pd


# ============================================================
# 1. LOAD CLEANED ODI DATA
# ============================================================

df = pd.read_csv("data/processed/odi_matches_clean.csv")

# Convert date from text into an actual date type
df["date"] = pd.to_datetime(df["date"])

# Sort oldest → newest.
# This is extremely important because we must never use
# future matches when creating features for an older match.
df = df.sort_values("date").reset_index(drop=True)

print(df[["date", "team1", "team2", "winner"]].head())
print("\nOriginal dataset shape:", df.shape)


# ============================================================
# 2. CREATE ML FEATURES
# ============================================================

features = []

for i, match in df.iterrows():

    # --------------------------------------------------------
    # ONLY USE MATCHES THAT HAPPENED BEFORE THIS MATCH
    # --------------------------------------------------------

    history = df.iloc[:i]

    team1 = match["team1"]
    team2 = match["team2"]
    venue = match["venue"]


    # ========================================================
    # 3. RECENT FORM
    # ========================================================

    # Find all previous matches involving team 1
    team1_history = history[
        (history["team1"] == team1) |
        (history["team2"] == team1)
        ]

    # Find all previous matches involving team 2
    team2_history = history[
        (history["team1"] == team2) |
        (history["team2"] == team2)
        ]

    # Only use each team's most recent 10 matches
    team1_recent = team1_history.tail(10)
    team2_recent = team2_history.tail(10)

    # Count recent wins
    team1_wins = (team1_recent["winner"] == team1).sum()
    team2_wins = (team2_recent["winner"] == team2).sum()

    # Calculate recent win rates
    team1_recent_win_rate = (
        team1_wins / len(team1_recent)
        if len(team1_recent) > 0
        else 0
    )

    team2_recent_win_rate = (
        team2_wins / len(team2_recent)
        if len(team2_recent) > 0
        else 0
    )


    # ========================================================
    # 4. HEAD-TO-HEAD HISTORY
    # ========================================================

    # Find previous matches where these two teams played
    # against each other in either team1/team2 order.
    head_to_head = history[
        (
                (history["team1"] == team1) &
                (history["team2"] == team2)
        )
        |
        (
                (history["team1"] == team2) &
                (history["team2"] == team1)
        )
        ]

    # Count head-to-head wins
    team1_h2h_wins = (
            head_to_head["winner"] == team1
    ).sum()

    team2_h2h_wins = (
            head_to_head["winner"] == team2
    ).sum()

    # Calculate historical H2H win rates
    team1_h2h_win_rate = (
        team1_h2h_wins / len(head_to_head)
        if len(head_to_head) > 0
        else 0
    )

    team2_h2h_win_rate = (
        team2_h2h_wins / len(head_to_head)
        if len(head_to_head) > 0
        else 0
    )


    # ========================================================
    # 5. VENUE PERFORMANCE
    # ========================================================

    # Previous matches played by team 1 at this venue
    team1_venue_history = history[
        (
                (history["team1"] == team1) |
                (history["team2"] == team1)
        )
        &
        (history["venue"] == venue)
        ]

    # Previous matches played by team 2 at this venue
    team2_venue_history = history[
        (
                (history["team1"] == team2) |
                (history["team2"] == team2)
        )
        &
        (history["venue"] == venue)
        ]

    # Count venue wins
    team1_venue_wins = (
            team1_venue_history["winner"] == team1
    ).sum()

    team2_venue_wins = (
            team2_venue_history["winner"] == team2
    ).sum()

    # Calculate venue win rates
    team1_venue_win_rate = (
        team1_venue_wins / len(team1_venue_history)
        if len(team1_venue_history) > 0
        else 0
    )

    team2_venue_win_rate = (
        team2_venue_wins / len(team2_venue_history)
        if len(team2_venue_history) > 0
        else 0
    )


    # ========================================================
    # 6. HISTORY / SAMPLE SIZE COUNTS
    # ========================================================

    # How many recent matches were actually available?
    team1_recent_matches = len(team1_recent)
    team2_recent_matches = len(team2_recent)

    # How many previous H2H matches were available?
    h2h_matches = len(head_to_head)

    # How much history does each team have at this venue?
    team1_venue_matches = len(team1_venue_history)
    team2_venue_matches = len(team2_venue_history)


    # ========================================================
    # 7. SAVE ONE ML TRAINING ROW
    # ========================================================

    features.append({

        # Match information
        "date": match["date"],
        "team1": team1,
        "team2": team2,
        "venue": venue,

        # Recent form
        "team1_recent_win_rate": team1_recent_win_rate,
        "team2_recent_win_rate": team2_recent_win_rate,
        "team1_recent_matches": team1_recent_matches,
        "team2_recent_matches": team2_recent_matches,

        # Head-to-head
        "team1_h2h_win_rate": team1_h2h_win_rate,
        "team2_h2h_win_rate": team2_h2h_win_rate,
        "h2h_matches": h2h_matches,

        # Venue performance
        "team1_venue_win_rate": team1_venue_win_rate,
        "team2_venue_win_rate": team2_venue_win_rate,
        "team1_venue_matches": team1_venue_matches,
        "team2_venue_matches": team2_venue_matches,

        # Actual result — this will become our ML target
        "winner": match["winner"]
    })


# ============================================================
# 8. CREATE FEATURE DATAFRAME
# ============================================================

features_df = pd.DataFrame(features)


# ============================================================
# 9. SAVE FEATURE DATASET
# ============================================================

features_df.to_csv(
    "data/processed/odi_match_features.csv",
    index=False
)


# ============================================================
# 10. CHECK RESULTS
# ============================================================

print("\nFeature rows:")
print(features_df.head(15))

print("\nSaved feature dataset!")
print("Shape:", features_df.shape)