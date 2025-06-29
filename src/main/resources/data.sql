-- 插入账户
INSERT INTO accounts (id, balance, is_active) VALUES
                                                  ('ACC001', 10000.00, TRUE),
                                                  ('ACC002', 500.00, TRUE),
                                                  ('ACC003', 324325.00, TRUE),
                                                  ('ACC004', 300.00, TRUE),
                                                  ('ACC005', 10000.00, TRUE),
                                                  ('ACC006', 500.00, TRUE),
                                                  ('ACC007', 20000.00, TRUE),
                                                  ('ACC008', 102304.00, FALSE),
                                                  ('ACC009', 54358435.00, TRUE),
                                                  ('ACC010', 545845.00, TRUE);

-- 插入交易
INSERT INTO transactions (transaction_id, debit_account_id, credit_account_id, amount, status, message, description, last_update_time, type) VALUES
                                                                                                                                                              ('TXN001', 'ACC001', 'ACC002', 100.00, 'SUCCESS', 'Transfer completed', 'Salary transfer', CURRENT_TIMESTAMP, 'TRANSFER'),
                                                                                                                                                              ('TXN002', NULL, 'ACC003', 2000.00, 'SUCCESS', 'Deposit completed', 'Initial deposit', CURRENT_TIMESTAMP, 'DEPOSIT'),
                                                                                                                                                              ('TXN003', 'ACC004', NULL, 100.00, 'FAILED', 'Withdrawal failed', 'ATM withdrawal', CURRENT_TIMESTAMP, 'WITHDRAWAL'),
                                                                                                                                                              ('TXN004', 'ACC002', 'ACC001', 50.00, 'PROCESSING', 'Transfer in process', 'Peer transfer', CURRENT_TIMESTAMP, 'TRANSFER');