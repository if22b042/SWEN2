@echo off
curl -X POST "http://localhost:8080/tourlogs" -H "Content-Type: application/json" -d "{\"dateTime\": \"2024-05-13T10:00:00\", \"comment\": \"Great tour!\", \"difficulty\": \"Easy\", \"totalDistance\": 10, \"totalTime\": 120, \"rating\": 5, \"tourId\": 1}"
pause
