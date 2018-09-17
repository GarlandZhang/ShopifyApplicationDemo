INSERT INTO SHOP (ShopId, Name, Description, VendorId) VALUES (1, 'House of Cards', 'Unique decks of cards for sale!', 234);
INSERT INTO SHOP (ShopId, Name, Description, VendorId) VALUES (2, 'Banana Utopia', 'Bananas from all around the world!', 123);
INSERT INTO "Order" (OrderId, ShopId, CreationDate, UpdateDate, Total) VALUES (1, 1, current_date, current_date, 22.00);
INSERT INTO PRODUCT (ProductId, Name, Description, Price, ShopId) VALUES (1, 'Steel Playing Cards', 'Made out of real metal!', 11.00, 1);
INSERT INTO LINEITEM (LineItemId, ProductId, OrderId, Properties, Quantity, Price, Discount) VALUES (1, 1, 1, 'Extra info here: ...', 2, 11.00, 0);