import pandas as pd

df = pd.read_csv("data/raw/ODI_Match_info.csv")

print("Shape:", df.shape)

print("\nColumns:")
print(df.columns.tolist())

print("\nMissing values:")
print(df.isnull().sum())

print("\nDuplicate rows:", df.duplicated().sum())
print("Duplicate match IDs:", df["id"].duplicated().sum())

df["date"] = pd.to_datetime(df["date"])

print("\nFirst five dates:")
print(df["date"].head())

print("\nDate type:")
print(df["date"].dtype)

print("\nMatches with no winner:")
print(
    df[df["winner"].isna()][
        ["date", "team1", "team2", "result", "win_by_runs", "win_by_wickets", "venue"]
    ].head(20)
)
print("\nUnique teams:")
teams = sorted(set(df["team1"]) | set(df["team2"]))

for team in teams:
    print(team)

print("\nNumber of unique teams:", len(teams))

print("\nExamples of matches with missing city:")

print(
    df[df["city"].isna()][
        ["date", "team1", "team2", "venue"]
    ].head(30)
)
print("\nVenues associated with missing cities:")

print(
    df[df["city"].isna()]["venue"]
    .value_counts()
    .head(30)
)
# Fill missing cities using the venue name

venue_to_city = {
    "Harare Sports Club": "Harare",
    "Sydney Cricket Ground": "Sydney",
    "Rangiri Dambulla International Stadium": "Dambulla",
    "Sharjah Cricket Stadium": "Sharjah",
    "Melbourne Cricket Ground": "Melbourne",
    "Dubai International Cricket Stadium": "Dubai",
    "Adelaide Oval": "Adelaide",
    "Pallekele International Cricket Stadium": "Pallekele",
    "Rawalpindi Cricket Stadium": "Rawalpindi",
    "Queenstown Events Centre": "Queenstown",
    "Multan Cricket Stadium": "Multan",
    "Bulawayo Athletic Club": "Bulawayo",
    "Chittagong Divisional Stadium": "Chittagong",
    "Perth Stadium": "Perth",
    "Galle International Stadium": "Galle",
    "Mombasa Sports Club Ground": "Mombasa",
    "Dubai Sports City Cricket Stadium": "Dubai",
    "Sharjah Cricket Association Stadium": "Sharjah",
    "Sheikhupura Stadium": "Sheikhupura"
}

df["city"] = df["city"].fillna(
    df["venue"].map(venue_to_city)
)

print("\nMissing cities after venue mapping:")
print(df["city"].isna().sum())

# Save cleaned dataset
df.to_csv(
    "data/processed/odi_matches_clean.csv",
    index=False
)

print("\nCleaned dataset saved successfully.")
print("Final shape:", df.shape)