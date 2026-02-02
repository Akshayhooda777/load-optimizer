# SmartLoad Optimization API

A Spring Boot–based microservice that optimizes truck load selection based on weight, volume, payout, and constraints.

---

## 🚀 How to Run

```bash
# 1️⃣ Clone the repository
git clone https://github.com/Akshayhooda777/load-optimizer.git

# 2️⃣ Navigate to the project folder
cd load-optimizer

# 3️⃣ Start the application using Docker
docker compose up --build



## 🧪 Example Request (Test the API)
```bash
curl -X POST http://localhost:8080/api/v1/load-optimizer/optimize \
-H "Content-Type: application/json" \
-d '{
  "truck": {
    "id": "truck-123",
    "maxWeightLbs": 44000,
    "maxVolumeCuft": 3000
  },
  "orders": [
    {
      "id": "ord-001",
      "payoutCents": 250000,
      "weightLbs": 18000,
      "volumeCuft": 1200,
      "origin": "Los Angeles, CA",
      "destination": "Dallas, TX",
      "pickupDate": "2025-12-05",
      "deliveryDate": "2025-12-09",
      "isHazmat": false
    },
    {
      "id": "ord-002",
      "payoutCents": 180000,
      "weightLbs": 12000,
      "volumeCuft": 900,
      "origin": "Los Angeles, CA",
      "destination": "Dallas, TX",
      "pickupDate": "2025-12-04",
      "deliveryDate": "2025-12-10",
      "isHazmat": false
    },
    {
      "id": "ord-003",
      "payoutCents": 320000,
      "weightLbs": 30000,
      "volumeCuft": 1800,
      "origin": "Los Angeles, CA",
      "destination": "Dallas, TX",
      "pickupDate": "2025-12-06",
      "deliveryDate": "2025-12-08",
      "isHazmat": true
    }
  ]
}'
