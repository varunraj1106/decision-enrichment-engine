INSERT INTO transactions (id, merchant_id, customer_id, amount, currency, merchant_category, source_country, destination_country, channel, decision, risk_score, created_at, processed_at)
VALUES
    ('550e8400-e29b-41d4-a716-446655440001', 'MERCH-001', 'CUST-1001', 250.00, 'EUR', 'RETAIL', 'IE', 'IE', 'POS', 'APPROVED', 10, NOW(), NOW()),
    ('550e8400-e29b-41d4-a716-446655440002', 'MERCH-002', 'CUST-1002', 15000.00, 'USD', 'GAMBLING', 'US', 'RU', 'ONLINE', 'REJECTED', 85, NOW(), NOW()),
    ('550e8400-e29b-41d4-a716-446655440003', 'MERCH-003', 'CUST-1003', 5000.00, 'GBP', 'ELECTRONICS', 'GB', 'CN', 'ONLINE', 'FLAGGED', 40, NOW(), NOW()),
    ('550e8400-e29b-41d4-a716-446655440004', 'MERCH-004', 'CUST-1004', 75.50, 'EUR', 'GROCERY', 'IE', 'IE', 'CONTACTLESS', 'APPROVED', 0, NOW(), NOW()),
    ('550e8400-e29b-41d4-a716-446655440005', 'MERCH-005', 'CUST-1005', 52000.00, 'USD', 'CRYPTO', 'US', 'KP', 'ONLINE', 'REJECTED', 95, NOW(), NOW());