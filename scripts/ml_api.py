from flask import Flask, request, jsonify
import joblib
import pandas as pd

app = Flask(__name__)

# Load trained model and expected feature columns
model = joblib.load("models/random_forest_model.joblib")
feature_columns = joblib.load("models/feature_columns.joblib")


@app.route("/predict", methods=["POST"])
def predict():

    data = request.get_json()

    # Build one feature row using the incoming JSON
    input_data = pd.DataFrame([data])

    # Make sure columns are in exactly the same order
    # used when the model was trained
    input_data = input_data[feature_columns]

    prediction = model.predict(input_data)[0]

    probabilities = model.predict_proba(input_data)[0]

    return jsonify({
        "prediction": int(prediction),
        "team1WinProbability": float(probabilities[1]),
        "team2WinProbability": float(probabilities[0])
    })


if __name__ == "__main__":
    app.run(port=5000)