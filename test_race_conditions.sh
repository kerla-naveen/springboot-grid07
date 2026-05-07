#!/bin/bash

echo "=== Race Condition Test: 200 Concurrent Bot Comments ==="

# Configuration
API_URL="http://localhost:8080"
POST_ID=1
MAX_BOT_COMMENTS=100
CONCURRENT_REQUESTS=200

# Clear Redis counters before test
echo "Clearing Redis counters..."
docker exec grid_redis redis-cli FLUSHALL

# Create a test post first
echo "Creating test post..."
POST_RESPONSE=$(curl -s -X POST "$API_URL/api/posts" \
  -H "Content-Type: application/json" \
  -d '{"authorId":1,"authorType":"USER","content":"Test post for race conditions"}')

POST_ID=$(echo $POST_RESPONSE | jq -r '.id')
echo "Created post ID: $POST_ID"

# Handle case where POST_ID is empty
if [ -z "$POST_ID" ] || [ "$POST_ID" = "null" ]; then
    echo "❌ Failed to create test post"
    echo "Response: $POST_RESPONSE"
    exit 1
fi

# Record start time
START_TIME=$(date +%s%N)

echo "Firing $CONCURRENT_REQUESTS concurrent requests..."

# Fire 200 concurrent requests
for i in $(seq 1 $CONCURRENT_REQUESTS); do
  {
    RESPONSE=$(curl -s -X POST "$API_URL/api/posts/$POST_ID/comments" \
      -H "Content-Type: application/json" \
      -d "{\"authorId\":$i,\"authorType\":\"BOT\",\"content\":\"Bot comment $i\"}")
    
    # Log response for debugging
    echo "Bot $i: $RESPONSE" >> race_test_responses.log
  } &
done

# Wait for all background processes to complete
wait

# Record end time
END_TIME=$(date +%s%N)
DURATION=$((($END_TIME - $START_TIME) / 1000000))

echo "All requests completed in ${DURATION}ms"

# Check results
echo "=== RESULTS ==="

# Count successful comments in database
DB_COUNT=$(docker exec grid_postgres psql -U grid_user -d grid_db -t -c "SELECT COUNT(*) FROM comment WHERE post_id = $POST_ID AND author_type = 'BOT';" | tr -d ' ')

# Check Redis counter
REDIS_COUNT=$(docker exec grid_redis redis-cli GET "post:$POST_ID:bot_count")

echo "Database bot comments: $DB_COUNT"
echo "Redis counter: $REDIS_COUNT"
echo "Expected maximum: $MAX_BOT_COMMENTS"

# Test evaluation
if [ "$DB_COUNT" -eq "$MAX_BOT_COMMENTS" ] && [ "$REDIS_COUNT" -eq "$MAX_BOT_COMMENTS" ]; then
    echo "✅ RACE CONDITION TEST PASSED"
    echo "   Exactly $MAX_BOT_COMMENTS comments allowed"
else
    echo "❌ RACE CONDITION TEST FAILED"
    echo "   Database: $DB_COUNT, Redis: $REDIS_COUNT, Expected: $MAX_BOT_COMMENTS"
    
    if [ "$DB_COUNT" -gt "$MAX_BOT_COMMENTS" ]; then
        echo "   CRITICAL: More comments than allowed in database!"
    fi
fi

# Cleanup
rm -f race_test_responses.log

echo "=== Test Complete ==="
