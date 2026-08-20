import pandas as pd
import numpy as np
import joblib
import os
from sklearn.linear_model import LogisticRegression
from sklearn.metrics import accuracy_score, classification_report
from sklearn.ensemble import RandomForestClassifier
# ============================================================
# 1. LOAD FEATURE DATASET
# ============================================================

df = pd.read_csv("data/processed/odi_match_features.csv")

df["date"] = pd.to_datetime(df["date"])

print("Original rows:", len(df))


# ============================================================
# 2. REMOVE MATCHES WITHOUT A DECISIVE WINNER
# ============================================================

# Keep only matches where the winner is team1 or team2
df = df[
    (df["winner"] == df["team1"]) |
    (df["winner"] == df["team2"])
    ].copy()

print("Rows after removing no-results:", len(df))


# ============================================================
# 3. CREATE ML TARGET
# ============================================================

# 1 = team1 won
# 0 = team2 won
df["team1_won"] = (
        df["winner"] == df["team1"]
).astype(int)


# ============================================================
# 4. SELECT FEATURES
# ============================================================

feature_columns = [
    "team1_recent_win_rate",
    "team2_recent_win_rate",
    "team1_recent_matches",
    "team2_recent_matches",

    "team1_h2h_win_rate",
    "team2_h2h_win_rate",
    "h2h_matches",

    "team1_venue_win_rate",
    "team2_venue_win_rate",
    "team1_venue_matches",
    "team2_venue_matches"
]

X = df[feature_columns]
y = df["team1_won"]


# ============================================================
# 5. CHRONOLOGICAL TRAIN / TEST SPLIT
# ============================================================

split_index = int(len(df) * 0.80)

X_train = X.iloc[:split_index]
X_test = X.iloc[split_index:]

y_train = y.iloc[:split_index]
y_test = y.iloc[split_index:]

train_dates = df["date"].iloc[:split_index]
test_dates = df["date"].iloc[split_index:]


# ============================================================
# 6. CHECK EVERYTHING
# ============================================================

print("\nTraining rows:", len(X_train))
print("Testing rows:", len(X_test))

print(
    "Training period:",
    train_dates.min().date(),
    "to",
    train_dates.max().date()
)

print(
    "Testing period:",
    test_dates.min().date(),
    "to",
    test_dates.max().date()
)

print("\nTarget distribution:")
print(y.value_counts())

# ============================================================
# 7. TRAIN LOGISTIC REGRESSION
# ============================================================

model = LogisticRegression(max_iter=1000)

model.fit(X_train, y_train)


# ============================================================
# 8. MAKE PREDICTIONS
# ============================================================

y_pred = model.predict(X_test)


# ============================================================
# 9. EVALUATE MODEL
# ============================================================

accuracy = accuracy_score(y_test, y_pred)

print("\nLogistic Regression Results")
print("---------------------------")
print(f"Accuracy: {accuracy:.4f}")
print(f"Accuracy percentage: {accuracy * 100:.2f}%")

print("\nClassification Report:")
print(classification_report(y_test, y_pred))
# ============================================================
# 10. NAIVE BASELINE
# ============================================================

# Always predict team1 wins
naive_predictions = np.ones(len(y_test))

naive_accuracy = accuracy_score(
    y_test,
    naive_predictions
)

print("\nNaive Baseline Results")
print("----------------------")
print(
    f"Always predict team1: "
    f"{naive_accuracy * 100:.2f}%"
)


# ============================================================
# 11. RULE-BASED BASELINE
# ============================================================

team1_baseline_score = (
        0.40 * X_test["team1_recent_win_rate"]
        + 0.35 * X_test["team1_h2h_win_rate"]
        + 0.25 * X_test["team1_venue_win_rate"]
)

team2_baseline_score = (
        0.40 * X_test["team2_recent_win_rate"]
        + 0.35 * X_test["team2_h2h_win_rate"]
        + 0.25 * X_test["team2_venue_win_rate"]
)

rule_predictions = (
        team1_baseline_score > team2_baseline_score
).astype(int)

rule_accuracy = accuracy_score(
    y_test,
    rule_predictions
)

print("\nRule-Based Baseline Results")
print("---------------------------")
print(
    f"Accuracy: {rule_accuracy * 100:.2f}%"
)


# ============================================================
# 12. COMPARISON
# ============================================================

print("\nModel Comparison")
print("----------------")
print(f"Naive baseline:       {naive_accuracy * 100:.2f}%")
print(f"Rule-based baseline:  {rule_accuracy * 100:.2f}%")
print(f"Logistic Regression:  {accuracy * 100:.2f}%")

# ============================================================
# 13. RANDOM FOREST
# ============================================================

rf_model = RandomForestClassifier(
    n_estimators=200,
    max_depth=8,
    min_samples_leaf=5,
    random_state=42
)

rf_model.fit(X_train, y_train)

rf_predictions = rf_model.predict(X_test)

rf_accuracy = accuracy_score(
    y_test,
    rf_predictions
)

print("\nRandom Forest Results")
print("---------------------")
print(f"Accuracy: {rf_accuracy * 100:.2f}%")


# ============================================================
# 14. FINAL MODEL COMPARISON
# ============================================================

print("\nFull Model Comparison")
print("---------------------")
print(f"Naive baseline:       {naive_accuracy * 100:.2f}%")
print(f"Rule-based baseline:  {rule_accuracy * 100:.2f}%")
print(f"Logistic Regression:  {accuracy * 100:.2f}%")
print(f"Random Forest:        {rf_accuracy * 100:.2f}%")
# ============================================================
# 15. SAVE TRAINED ML MODEL
# ============================================================

os.makedirs("models", exist_ok=True)

joblib.dump(
    rf_model,
    "models/random_forest_model.joblib"
)

joblib.dump(
    feature_columns,
    "models/feature_columns.joblib"
)

print("\nRandom Forest model saved!")
print("Model: models/random_forest_model.joblib")
print("Features: models/feature_columns.joblib")