<div align="center">

# 🏏 Hozzat

### ODI Cricket Match Prediction & Analytics

**Historical cricket data → statistical features → match predictions**

<br>

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge\&logo=openjdk\&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-6DB33F?style=for-the-badge\&logo=springboot\&logoColor=white)
![Python](https://img.shields.io/badge/Python-3776AB?style=for-the-badge\&logo=python\&logoColor=white)
![Flask](https://img.shields.io/badge/Flask-000000?style=for-the-badge\&logo=flask\&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-4169E1?style=for-the-badge\&logo=postgresql\&logoColor=white)
![scikit-learn](https://img.shields.io/badge/scikit--learn-F7931E?style=for-the-badge\&logo=scikitlearn\&logoColor=white)

<br>

A full-stack cricket analytics application that predicts **ODI match winners** using historical match data, statistical analysis, and machine learning.

</div>

---

## ✦ Overview

Hozzat lets users select **two international cricket teams and a venue** and generates a match prediction based on their historical performance.

Instead of relying on a single prediction method, Hozzat compares two approaches:

**📊 Statistical Prediction**
A rule-based model combining recent form, head-to-head performance, and venue history.

**🤖 Machine Learning Prediction**
A Random Forest classifier trained on historical ODI matches using features calculated without future-data leakage.

The project explores an important machine-learning question:

> **Does a more complex model actually outperform a well-designed statistical baseline?**

In Hozzat's current dataset, the answer is surprisingly **no** — the statistical baseline slightly outperforms the trained ML models.

---

## ✦ Features

* 🏏 Predict ODI match winners between historical international teams
* 📈 Analyze each team's **recent form**
* ⚔️ Compare historical **head-to-head performance**
* 🏟️ Measure **venue-specific performance**
* 🧮 Generate predictions using a custom statistical model
* 🤖 Generate ML predictions with **win probabilities**
* 🗄️ Dynamically load teams and venues from PostgreSQL
* 🔄 Connect Java and Python services through REST APIs
* 🛡️ Validate user input and handle API errors
* 📱 Responsive cricket-themed interface

---

## ✦ How It Works

```text
                     ┌──────────────────────┐
                     │       Browser        │
                     │  Team + Venue Input  │
                     └──────────┬───────────┘
                                │
                                ▼
                     ┌──────────────────────┐
                     │ Spring Boot REST API │
                     │        Java          │
                     └───────┬──────┬───────┘
                             │      │
                 ┌───────────┘      └───────────┐
                 ▼                              ▼
        ┌─────────────────┐           ┌─────────────────┐
        │   PostgreSQL    │           │  Flask ML API   │
        │                 │           │     Python      │
        │ Historical ODI  │           └────────┬────────┘
        │ Match Data      │                    │
        └─────────────────┘                    ▼
                                      ┌─────────────────┐
                                      │  Random Forest  │
                                      │      Model      │
                                      └─────────────────┘
```

### Prediction Flow

```text
User selects teams + venue
            ↓
Spring Boot validates request
            ↓
Historical matches queried
            ↓
Statistical features calculated
            ↓
      ┌─────┴─────┐
      ↓           ↓
 Rule-Based    Flask ML API
 Prediction         ↓
                Random Forest
                    ↓
      └─────┬──────┘
            ↓
 Prediction + probabilities
            ↓
      Displayed to user
```

---

## ✦ Machine Learning

One of the most important parts of Hozzat is preventing **data leakage**.

Historical ODI matches are processed **chronologically**.

For every match, features are calculated using **only matches that occurred before that match**.

This prevents information from future matches from influencing historical predictions.

### Feature Engineering

The model uses **11 historical features**:

| Category     | Feature                         |
| ------------ | ------------------------------- |
| Recent Form  | Team 1 recent win rate          |
| Recent Form  | Team 2 recent win rate          |
| Recent Form  | Team 1 recent matches available |
| Recent Form  | Team 2 recent matches available |
| Head-to-Head | Team 1 H2H win rate             |
| Head-to-Head | Team 2 H2H win rate             |
| Head-to-Head | Previous H2H match count        |
| Venue        | Team 1 venue win rate           |
| Venue        | Team 2 venue win rate           |
| Venue        | Team 1 matches at venue         |
| Venue        | Team 2 matches at venue         |

---

## ✦ Model Evaluation

To preserve the temporal structure of cricket results, the dataset was split **chronologically rather than randomly**.

| Dataset      | Period    | Matches |
| ------------ | --------- | ------: |
| **Training** | 2002–2019 |   1,807 |
| **Testing**  | 2019–2023 |     452 |

### Results

| Model                      | Test Accuracy |
| -------------------------- | ------------: |
| Naive Baseline             |        53.10% |
| 🥇 **Rule-Based Baseline** |    **64.16%** |
| Logistic Regression        |        63.72% |
| Random Forest              |        63.94% |

### What did I learn?

The Random Forest model **did not outperform the statistical baseline**.

The rule-based model achieved **64.16% accuracy**, compared with **63.94% for Random Forest**.

Rather than treating this as a failure, the result provides an important benchmark: **a more complex model does not automatically produce better predictions**.

The small performance gap suggests that future improvements should focus on richer predictive features rather than simply increasing model complexity.

Potential additions include:

* Toss winner and toss decision
* Player availability
* Team rankings
* Batting and bowling strength
* Match conditions
* Tournament context
* Venue characteristics

---

## ✦ Tech Stack

<table>
<tr>
<td><strong>Frontend</strong></td>
<td>HTML · CSS · JavaScript</td>
</tr>

<tr>
<td><strong>Backend</strong></td>
<td>Java · Spring Boot</td>
</tr>

<tr>
<td><strong>Machine Learning</strong></td>
<td>Python · Flask · scikit-learn · Pandas</td>
</tr>

<tr>
<td><strong>Database</strong></td>
<td>PostgreSQL</td>
</tr>

<tr>
<td><strong>Model</strong></td>
<td>Random Forest Classifier</td>
</tr>

<tr>
<td><strong>API</strong></td>
<td>REST</td>
</tr>
</table>

---

## ✦ API

### Statistical Prediction

```http
GET /api/predict?team1=India&team2=Australia&venue=Melbourne
```

Returns a prediction generated from historical team statistics.

### Machine Learning Prediction

```http
GET /api/ml-predict?team1=India&team2=Australia&venue=Melbourne
```

Returns the Random Forest prediction and estimated win probabilities.

### Teams

```http
GET /api/teams
```

Returns the available teams in the dataset.

### Venues

```http
GET /api/venues
```

Returns the available match venues.

---

## ✦ Project Structure

```text
hozzat/
│
├── src/
│   └── main/
│       ├── java/
│       │   └── com/hozzat/hozzat/
│       │       ├── MatchController.java
│       │       ├── MatchService.java
│       │       ├── MatchRepository.java
│       │       └── GlobalExceptionHandler.java
│       │
│       └── resources/
│           └── static/
│               └── index.html
│
├── ml/
│   ├── app.py
│   ├── train_model.py
│   └── model/
│
└── README.md
```

---

## ✦ Running Hozzat Locally

### 1. Clone the repository

```bash
git clone https://github.com/LaranyaS/hozzat.git
cd hozzat
```

### 2. Configure PostgreSQL

Create a PostgreSQL database and configure the connection in your Spring Boot application properties.

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/hozzat
spring.datasource.username=YOUR_USERNAME
spring.datasource.password=YOUR_PASSWORD
```

### 3. Start the Spring Boot backend

```bash
./mvnw spring-boot:run
```

### 4. Start the ML service

```bash
cd ml
pip install -r requirements.txt
python app.py
```

### 5. Open the application

Once both services are running, open:

```text
http://localhost:8080
```

---

## ✦ Future Improvements

* Add richer match and player-level features
* Incorporate ICC rankings
* Include toss information
* Experiment with gradient boosting models
* Improve probability calibration
* Add model explainability
* Deploy the complete application
* Expand beyond ODI cricket

---

<div align="center">

## 🏏 Built for cricket. Engineered with data.

**Java · Spring Boot · Python · Machine Learning · PostgreSQL**

<br>

⭐ If you found Hozzat interesting, consider starring the repository!

</div>
