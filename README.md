
# 🗳️ U.S. Election Tweet Analysis using Java-based Hadoop MapReduce

This project applies Hadoop's Java-based MapReduce model to analyze public engagement on social media during the 2020 U.S. presidential election. Using scalable batch processing, we extract key insights from over 1.7 million tweets related to Joe Biden and Donald Trump.

---

## 📦 Dataset Overview

- **Total Size**: 1.72 million tweets
- **Format**: CSV (Comma-Separated Values), with 21 attributes per record
- **Fields of Interest**:
  - `retweet_count`, `favorite_count`
  - `source` (e.g., iPhone, Android, Web App)
  - `created_at`, `state`, `latitude`, `longitude`

**Download the dataset** from Kaggle:  
🔗 [US Election 2020 Tweets – Kaggle Dataset](https://www.kaggle.com/manchunhui/us-election-2020-tweets)

Two main files are used:
- `new_hashtag_joebiden.csv`
- `new_hashtag_donaldtrump.csv`

> ⚠️ Note: Some tweets mention both candidates. Some fields may be missing (e.g., `null`). Filtering and validation are necessary during parsing.

---

## 🎯 Project Goals

This pipeline includes **three distinct analytical jobs**, each written and compiled in Java, targeting different dimensions of tweet metadata.

---

### 🔹 Task 1: Engagement and Device Analysis

**Objective**: Compute social engagement metrics for each candidate and analyze the device used to post tweets.

- **Metrics Extracted**:
  - Total Likes
  - Total Retweets
  - Tweet count by platform (Android, iPhone, Web App)

**Output Format**:
```
Candidate, TotalLikes, TotalRetweets, AndroidCount, iPhoneCount, WebAppCount
```

---

### 🔹 Task 2: Time-Constrained Statewise Statistics

**Objective**: Measure tweet behavior by U.S. state in a defined time window (9:00 AM to 5:00 PM), focusing on candidate mentions.

- **Analyzed States**: `New York`, `Texas`, `California`, `Florida`
- **Tweet Types**:
  - Tweets mentioning both candidates
  - Tweets mentioning only Biden
  - Tweets mentioning only Trump

**Output Format**:
```
State, %Both, %BidenOnly, %TrumpOnly, TotalTweets
```

📌 **Sample Output**:
```
new york, 0.2610, 0.3633, 0.3756, 13267
```

> This line means: Out of 13,267 tweets in New York between 9AM and 5PM, 26.1% mention both Biden and Trump, 36.3% only Biden, and 37.5% only Trump.

---

### 🔹 Task 3: Geo-spatial Tweet Filtering

**Objective**: Identify tweets originating from specific locations based on longitude and latitude.

- **Target Regions**: Only tweets from `New York` and `California`
- **Coordinates Used**:
  - **New York**: Latitude 40–45, Longitude -79 to -71
  - **California**: Latitude 32–42, Longitude -124 to -114

**Final Output Format**: Same as Task 2.

> Enables understanding of geographic tweet origin trends with political context.

---

## 🧱 Java MapReduce Architecture

Each job uses a traditional three-part Java MapReduce structure:

- `TMapper.java`: Extracts and filters relevant fields
- `TReducer.java`: Aggregates metrics for each key
- `TDriver.java`: Configures input/output, and connects Mapper and Reducer to Hadoop runtime

All classes are compiled into `run.jar`.

---

## ▶️ Execution Instructions

### 1. Upload Dataset to HDFS

```bash
hdfs dfs -mkdir -p /user/hadoop/input
hdfs dfs -put new_hashtag_*.csv /user/hadoop/input/
```

### 2. Run Java MapReduce Job

```bash
hadoop jar run.jar TDriver /user/hadoop/input /user/hadoop/output/task1_result
```

### 3. View Result

```bash
hdfs dfs -cat /user/hadoop/output/task1_result/part-r-00000
```

Repeat steps for each job (`task2_result`, `task3_result`, etc.).

---

## 🧪 Output Example (Task 2)

```
california, 0.2715, 0.4012, 0.3273, 16890
```

---

## 💻 System Requirements

- Java JDK 8 or higher
- Hadoop 3.x (tested on 3.2.1)
- Fully configured HDFS and YARN services
- Data files uploaded to `/user/hadoop/input/` on HDFS

---

## 🧾 License

This project is open for educational and research purposes.  
Original dataset © [Kaggle - US Election Tweets](https://www.kaggle.com/manchunhui/us-election-2020-tweets)

---

## 👨‍💻 Author & Project Info

Developed as part of a university-level distributed systems course and final project.  
This project showcases the power of Hadoop for analyzing large-scale social media data using batch processing techniques.
