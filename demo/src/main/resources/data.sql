INSERT INTO "User" (UserId, Username, Role) VALUES (234, 'user234', 'GENERAL');
INSERT INTO "User" (UserId, Username, Role) VALUES (123, 'user123', 'GENERAL');
INSERT INTO SHOP (ShopId, Name, Description, UserId) VALUES (1, 'House of Cards', 'Unique decks of cards for sale!', 234);
INSERT INTO SHOP (ShopId, Name, Description, UserId) VALUES (2, 'Banana Utopia', 'Bananas from all around the world!', 123);
INSERT INTO "Order" (OrderId, ShopId, UserId, CreationDate, UpdateDate, Total, Status) VALUES (1, 1, 234, current_date, current_date, 17.60, 'INCOMPLETE');
INSERT INTO PRODUCT (ProductId, Name, Description, Price, ShopId) VALUES (1, 'Steel Playing Cards', 'Made out of real metal!', 11.00, 1);
INSERT INTO LINEITEM (LineItemId, ProductId, OrderId, Properties, Quantity, Price, Discount) VALUES (1, 1, 1, 'Extra info here: ...', 2, 11.00, .20);