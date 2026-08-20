# Hozzat 🏏

Hozzat is a full-stack ODI cricket match prediction application that uses historical match data, statistical analysis, and machine learning to predict the winner of a matchup.

Users select two teams and a venue, and Hozzat generates predictions using both a rule-based statistical model and a trained Random Forest classifier.

## Features

- Predict ODI match winners between teams in the historical dataset
- Compare team performance using:
  - Recent form
  - Head-to-head record
  - Venue performance
- Rule-based prediction engine
- Machine learning prediction with win probabilities
- Dynamic team and venue selection from PostgreSQL
- Responsive cricket-themed web interface
- Input validation and API error handling

## Machine Learning

Historical ODI matches are processed chronologically to prevent future match information from leaking into earlier predictions.

For every match, Hozzat generates features using only matches that occurred before that match.

### Features

The model uses 11 features:

- Team 1 recent win rate
- Team 2 recent win rate
- Team 1 recent matches available
- Team 2 recent matches available
- Team 1 head-to-head win rate
- Team 2 head-to-head win rate
- Number of previous head-to-head matches
- Team 1 venue win rate
- Team 2 venue win rate
- Team 1 matches at the venue
- Team 2 matches at the venue

### Model Evaluation

The dataset was split chronologically rather than randomly:

- Training: 1,807 matches from 2002–2019
- Testing: 452 matches from 2019–2023

Results:

| Model | Test Accuracy |
|---|---:|
| Naive Baseline | 53.10% |
| Rule-Based Baseline | 64.16% |
| Logistic Regression | 63.72% |
| Random Forest | 63.94% |

The rule-based baseline slightly outperformed the machine learning models, highlighting the importance of feature quality and providing a useful benchmark for future model improvements.

## Architecture

Hozzat combines Java and Python services:

```text
Browser
   |
   v
Spring Boot REST API
   |
   +------> PostgreSQL
   |         Historical ODI data
   |
   +------> Flask ML API
                |
                v
          Random Forest Model
