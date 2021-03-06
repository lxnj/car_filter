DELIMITER //

CREATE TRIGGER setAuctionFeeTrg_INSERT BEFORE INSERT ON car
FOR EACH ROW
BEGIN
	IF NEW.Price <= 5000 THEN
		SET NEW.AuctionFee = 300;
	ELSEIF NEW.Price <= 10000 THEN
		SET NEW.AuctionFee = 400;
	ELSEIF NEW.Price <= 15000 THEN
		SET NEW.AuctionFee = 450;
	ELSEIF NEW.Price <= 20000 THEN
		SET NEW.AuctionFee = 550;
	ELSEIF NEW.Price <= 25000 THEN 
		SET NEW.AuctionFee = 650;
	ELSE
		SET NEW.AuctionFee = ceil((GREATEST((NEW.Price-25000), 0)/10000)) * 100 + 650;
	END IF;
END //

CREATE TRIGGER setAuctionFeeTrg_UPDATE BEFORE UPDATE ON car
FOR EACH ROW
BEGIN
	IF NEW.Price <= 5000 THEN
		SET NEW.AuctionFee = 300;
	ELSEIF NEW.Price <= 10000 THEN
		SET NEW.AuctionFee = 400;
	ELSEIF NEW.Price <= 15000 THEN
		SET NEW.AuctionFee = 450;
	ELSEIF NEW.Price <= 20000 THEN
		SET NEW.AuctionFee = 550;
	ELSEIF NEW.Price <= 25000 THEN 
		SET NEW.AuctionFee = 650;
	ELSE
		SET NEW.AuctionFee = ceil((GREATEST((NEW.Price-25000), 0)/10000)) * 100 + 650;
	END IF;
END //

DELIMITER ;