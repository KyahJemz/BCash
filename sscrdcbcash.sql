-- phpMyAdmin SQL Dump
-- version 5.2.0
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Nov 07, 2023 at 07:39 PM
-- Server version: 10.4.27-MariaDB
-- PHP Version: 8.1.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `sscrdcbcash`
--

-- --------------------------------------------------------

--
-- Table structure for table `tbl_activitylogs`
--

CREATE TABLE `tbl_activitylogs` (
  `ActivityLogs_Id` int(11) NOT NULL,
  `Account_Address` varchar(15) NOT NULL,
  `Target_Account_Address` varchar(15) NOT NULL,
  `Action` varchar(6) NOT NULL,
  `Task` varchar(255) NOT NULL,
  `Timestamp` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `tbl_actorcategory`
--

CREATE TABLE `tbl_actorcategory` (
  `ActorCategory_Id` int(11) NOT NULL,
  `Name` varchar(255) NOT NULL,
  `Code` varchar(3) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `tbl_actorcategory`
--

INSERT INTO `tbl_actorcategory` (`ActorCategory_Id`, `Name`, `Code`) VALUES
(1, 'Administrator', 'ADM'),
(2, 'Accounting', 'ACC'),
(3, 'Merchant Admin', 'MTA'),
(4, 'Merchant Staff', 'MTS'),
(5, 'User', 'USR'),
(6, 'Guest', 'GST'),
(7, 'Guardian', 'GDN');

-- --------------------------------------------------------

--
-- Table structure for table `tbl_authentications`
--

CREATE TABLE `tbl_authentications` (
  `Account_Address` varchar(15) NOT NULL,
  `AuthToken` varchar(30) DEFAULT NULL,
  `AuthExpirationTime` timestamp NULL DEFAULT NULL,
  `AuthCreationTime` timestamp NULL DEFAULT NULL,
  `OtpCode` varchar(6) DEFAULT NULL,
  `OtpCreationTime` timestamp NULL DEFAULT NULL,
  `OtpExpirationTime` timestamp NULL DEFAULT NULL,
  `IpAddress` varchar(20) NOT NULL,
  `Location` varchar(50) NOT NULL,
  `Device` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `tbl_campus`
--

CREATE TABLE `tbl_campus` (
  `Campus_Id` int(11) NOT NULL,
  `Name` varchar(255) NOT NULL,
  `Abbreviation` varchar(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `tbl_campus`
--

INSERT INTO `tbl_campus` (`Campus_Id`, `Name`, `Abbreviation`) VALUES
(1, 'Cavite Main Campus', NULL),
(2, 'Cavite Cañacao Campus', NULL);

-- --------------------------------------------------------

--
-- Table structure for table `tbl_card`
--

CREATE TABLE `tbl_card` (
  `Card_Id` int(11) NOT NULL,
  `Card_Address` varchar(15) DEFAULT NULL,
  `UsersAccount_Address` varchar(15) DEFAULT NULL,
  `Campus_Id` int(11) DEFAULT NULL,
  `IsActive` bit(1) DEFAULT b'1',
  `Notes` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `tbl_card`
--

INSERT INTO `tbl_card` (`Card_Id`, `Card_Address`, `UsersAccount_Address`, `Campus_Id`, `IsActive`, `Notes`) VALUES
(1, '11111', NULL, 1, b'1', NULL),
(2, '22222', NULL, 1, b'1', NULL),
(3, '33333', NULL, 1, b'0', NULL),
(5, '44444', NULL, 1, b'1', NULL),
(7, '55555', NULL, 1, b'0', 'Invalid card'),
(8, '66666', NULL, 1, b'1', NULL),
(9, '77777', NULL, 1, b'0', '4545'),
(10, '00000', NULL, 1, b'1', NULL),
(11, '88888', NULL, 1, b'1', NULL);

-- --------------------------------------------------------

--
-- Table structure for table `tbl_configurations`
--

CREATE TABLE `tbl_configurations` (
  `Configuration_Id` int(11) NOT NULL,
  `Title` varchar(100) NOT NULL,
  `Value` varchar(100) NOT NULL,
  `Description` text DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `tbl_configurations`
--

INSERT INTO `tbl_configurations` (`Configuration_Id`, `Title`, `Value`, `Description`) VALUES
(1, 'IsMaintenance', '0', NULL),
(2, 'Transactions', '1', NULL),
(3, 'Transfers', '1', NULL),
(4, 'CashIn', '1', NULL),
(6, 'AndroidAppVersion', '1.0', NULL),
(7, 'WebAppVersion', '1.0', NULL),
(8, 'Accounting', '1', NULL),
(9, 'MerchantAdmin', '1', NULL),
(10, 'MerchantStaff', '1', NULL),
(11, 'User', '1', NULL),
(12, 'Guest', '1', NULL),
(13, 'Guardian', '1', NULL);

-- --------------------------------------------------------

--
-- Table structure for table `tbl_guardianaccount`
--

CREATE TABLE `tbl_guardianaccount` (
  `GuardianAccount_Address` varchar(15) NOT NULL,
  `UsersAccount_Address` varchar(15) DEFAULT NULL,
  `ActorCategory_Id` int(11) NOT NULL,
  `Email` varchar(50) NOT NULL,
  `Firstname` varchar(50) NOT NULL,
  `Lastname` varchar(50) NOT NULL,
  `PinCode` varchar(255) DEFAULT NULL,
  `Campus_Id` int(11) DEFAULT NULL,
  `IsAccountActive` bit(1) DEFAULT b'1',
  `DateRegistered` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `tbl_itemscategory`
--

CREATE TABLE `tbl_itemscategory` (
  `ItemsCategory_Id` int(11) NOT NULL,
  `MerchantsCategory_Id` int(11) NOT NULL,
  `Name` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `tbl_itemscategory`
--

INSERT INTO `tbl_itemscategory` (`ItemsCategory_Id`, `MerchantsCategory_Id`, `Name`) VALUES
(1, 1, 'Test Category');

-- --------------------------------------------------------

--
-- Table structure for table `tbl_loginhistory`
--

CREATE TABLE `tbl_loginhistory` (
  `Account_Address` varchar(15) NOT NULL,
  `IpAddress` varchar(20) NOT NULL,
  `Location` varchar(50) NOT NULL,
  `Device` varchar(100) NOT NULL,
  `LastOnline` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `tbl_merchantitems`
--

CREATE TABLE `tbl_merchantitems` (
  `MerchantItems_Id` int(11) NOT NULL,
  `ItemCategory` varchar(50) DEFAULT NULL,
  `MerchantsCategory_Id` int(11) NOT NULL,
  `Name` varchar(50) NOT NULL,
  `Price` float NOT NULL,
  `Image` varchar(255) NOT NULL DEFAULT 'default.png',
  `IsActive` bit(1) DEFAULT b'1',
  `CreatedTimestamp` timestamp NOT NULL DEFAULT current_timestamp(),
  `ModifiedTimestamp` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `tbl_merchantitems`
--

INSERT INTO `tbl_merchantitems` (`MerchantItems_Id`, `ItemCategory`, `MerchantsCategory_Id`, `Name`, `Price`, `Image`, `IsActive`, `CreatedTimestamp`, `ModifiedTimestamp`) VALUES
(1, 'cat 3', 1, 'Test 1', 20, 'default.png', b'0', '2023-10-16 17:41:23', '2023-10-29 05:59:15'),
(2, 'cat 3', 1, 'Test 2', 21, 'default.png', b'1', '2023-10-16 17:41:48', '2023-10-29 05:59:15'),
(3, 'cat 3', 1, 'Test 3', 26, 'default.png', b'1', '2023-10-16 17:42:08', '2023-10-29 05:59:15'),
(4, 'cat 1', 1, 'Test 4', 10, 'default.png', b'1', '2023-10-24 11:39:14', '0000-00-00 00:00:00'),
(5, 'cat 2', 1, 'Test 5', 5, 'default.png', b'1', '2023-10-24 20:50:03', '0000-00-00 00:00:00'),
(6, 'cat 1', 1, 'Test 6', 70, 'default.png', b'1', '2023-10-25 00:51:09', '0000-00-00 00:00:00'),
(8, 'cat 2', 1, 'Test 7', 100, 'default.png', b'1', '2023-10-29 06:26:23', '0000-00-00 00:00:00'),
(9, 'cat 1', 1, 'Test 8', 50, 'default.png', b'1', '2023-10-29 06:27:16', '0000-00-00 00:00:00'),
(10, 'cat 2', 1, 'Test 9', 77, 'default.png', b'1', '2023-10-29 06:27:46', '0000-00-00 00:00:00');

-- --------------------------------------------------------

--
-- Table structure for table `tbl_merchants`
--

CREATE TABLE `tbl_merchants` (
  `WebAccounts_Address` varchar(15) NOT NULL,
  `MerchantsCategory_Id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `tbl_merchants`
--

INSERT INTO `tbl_merchants` (`WebAccounts_Address`, `MerchantsCategory_Id`) VALUES
('MTA000000000000', 1),
('MTS000000000000', 1);

-- --------------------------------------------------------

--
-- Table structure for table `tbl_merchantscategory`
--

CREATE TABLE `tbl_merchantscategory` (
  `MerchantsCategory_Id` int(11) NOT NULL,
  `Campus_Id` int(11) NOT NULL,
  `ShopName` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `tbl_merchantscategory`
--

INSERT INTO `tbl_merchantscategory` (`MerchantsCategory_Id`, `Campus_Id`, `ShopName`) VALUES
(1, 1, 'Tapsilogan'),
(2, 1, 'Canteenan'),
(4, 1, 'newtrest');

-- --------------------------------------------------------

--
-- Table structure for table `tbl_notifications`
--

CREATE TABLE `tbl_notifications` (
  `Notification_ID` int(11) NOT NULL,
  `Creator_Account_Address` varchar(15) NOT NULL,
  `Title` varchar(255) NOT NULL,
  `Content` text NOT NULL,
  `IsNew` bit(1) DEFAULT b'1',
  `Timestamp` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `tbl_notifications`
--

INSERT INTO `tbl_notifications` (`Notification_ID`, `Creator_Account_Address`, `Title`, `Content`, `IsNew`, `Timestamp`) VALUES
(7, 'ADM000000000000', 'Test 1', 'List mutation with SortedList If your RecyclerView receives updates incrementally, e.g. item X is inserted, or item Y is removed, you can use SortedList to manage your list. You define how to order items, and it will automatically trigger update signals that RecyclerView can use. SortedList works if you only need to handle insert and remove events, and has the benefit that you only ever need to have a single copy of the list in memory. It can also compute differences with replaceAll, but this method is more limited than the list diffing be', b'1', '2023-11-01 19:32:11'),
(8, 'ADM000000000000', 'Test 2', 'List mutation with SortedList If your RecyclerView receives updates incrementally, e.g. item X is inserted, or item Y is removed, you can use SortedList to manage your list. You define how to order items, and it will automatically trigger update signals that RecyclerView can use. SortedList works if you only need to handle insert and remove events, and has the benefit that you only ever need to have a single copy of the list in memory. It can also compute differences with replaceAll, but this method is more limited than the list diffing be', b'1', '2023-11-05 15:58:36'),
(9, 'ADM000000000000', 'Test 3', 'List mutation with SortedList If your RecyclerView receives updates incrementally, e.g. item X is inserted, or item Y is removed, you can use SortedList to manage your list. You define how to order items, and it will automatically trigger update signals that RecyclerView can use. SortedList works if you only need to handle insert and remove events, and has the benefit that you only ever need to have a single copy of the list in memory. It can also compute differences with replaceAll, but this method is more limited than the list diffing be', b'1', '2023-11-05 15:58:56');

-- --------------------------------------------------------

--
-- Table structure for table `tbl_ordersvalidation`
--

CREATE TABLE `tbl_ordersvalidation` (
  `WebAccounts_Address` varchar(15) NOT NULL,
  `UsersAccount_Address` varchar(15) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `tbl_remittance`
--

CREATE TABLE `tbl_remittance` (
  `Remittance_Id` int(11) NOT NULL,
  `Submitted_By` varchar(15) NOT NULL,
  `TotalOrders` varchar(11) NOT NULL,
  `TotalAmount` varchar(11) NOT NULL,
  `DateResponse` timestamp NULL DEFAULT NULL,
  `Status` varchar(25) NOT NULL DEFAULT 'Waiting',
  `Timestamp` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `tbl_schoolid`
--

CREATE TABLE `tbl_schoolid` (
  `SchoolId` varchar(20) NOT NULL,
  `Email` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `tbl_schoolid`
--

INSERT INTO `tbl_schoolid` (`SchoolId`, `Email`) VALUES
('1234567890', 'S.STEPHEN.LAYSON@SSCR.EDU');

-- --------------------------------------------------------

--
-- Table structure for table `tbl_transactionitems`
--

CREATE TABLE `tbl_transactionitems` (
  `Transaction_Address` varchar(20) NOT NULL,
  `MerchantItems_Id` int(11) NOT NULL,
  `Quantity` int(11) NOT NULL,
  `Amount` float NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `tbl_transactions`
--

CREATE TABLE `tbl_transactions` (
  `Transaction_Address` varchar(20) NOT NULL,
  `Account_Address` varchar(15) NOT NULL,
  `Status` varchar(50) NOT NULL,
  `Debit` float NOT NULL,
  `Credit` float NOT NULL,
  `Remittance_Id` int(11) DEFAULT NULL,
  `Timestamp` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `tbl_transactionsinfo`
--

CREATE TABLE `tbl_transactionsinfo` (
  `Transaction_Address` varchar(20) NOT NULL,
  `TransactionType_Id` int(11) NOT NULL,
  `Sender_Address` varchar(15) NOT NULL,
  `Receiver_Address` varchar(15) NOT NULL,
  `Status` varchar(50) NOT NULL,
  `Amount` float NOT NULL,
  `Discount` float NOT NULL,
  `TotalAmount` float NOT NULL,
  `PostedBy` varchar(15) NOT NULL,
  `Notes` text DEFAULT NULL,
  `PaymentMethod` varchar(50) DEFAULT NULL,
  `Timestamp` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `tbl_transactiontype`
--

CREATE TABLE `tbl_transactiontype` (
  `TransactionType_Id` int(11) NOT NULL,
  `Name` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `tbl_transactiontype`
--

INSERT INTO `tbl_transactiontype` (`TransactionType_Id`, `Name`) VALUES
(1, 'Cash In'),
(2, 'Transfer'),
(3, 'Purchase'),
(4, 'Cash Out');

-- --------------------------------------------------------

--
-- Table structure for table `tbl_usersaccount`
--

CREATE TABLE `tbl_usersaccount` (
  `UsersAccount_Address` varchar(15) NOT NULL,
  `ActorCategory_Id` int(11) DEFAULT NULL,
  `Email` varchar(50) DEFAULT NULL,
  `Firstname` varchar(50) NOT NULL,
  `Lastname` varchar(50) NOT NULL,
  `PinCode` varchar(255) DEFAULT NULL,
  `IsAccountActive` bit(1) DEFAULT b'1',
  `Campus_Id` int(11) DEFAULT NULL,
  `Password` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `tbl_usersdata`
--

CREATE TABLE `tbl_usersdata` (
  `UsersAccount_Address` varchar(15) NOT NULL,
  `GuardianAccount_Address` varchar(15) DEFAULT NULL,
  `SchoolPersonalId` varchar(15) DEFAULT NULL,
  `Balance` float DEFAULT 0,
  `CanDoTransfers` bit(1) DEFAULT b'1',
  `CanDoTransactions` bit(1) DEFAULT b'1',
  `CanUseCard` bit(1) DEFAULT b'1',
  `CanModifySettings` bit(1) DEFAULT b'1',
  `IsTransactionAutoConfirm` bit(1) NOT NULL DEFAULT b'0',
  `DateRegistered` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `tbl_webaccounts`
--

CREATE TABLE `tbl_webaccounts` (
  `WebAccounts_Address` varchar(15) NOT NULL,
  `ActorCategory_Id` int(11) NOT NULL,
  `Username` varchar(50) NOT NULL,
  `Password` varchar(255) NOT NULL,
  `Email` varchar(50) NOT NULL,
  `Firstname` varchar(50) NOT NULL,
  `Lastname` varchar(50) NOT NULL,
  `PinCode` varchar(255) DEFAULT NULL,
  `IsAccountActive` bit(1) DEFAULT b'1',
  `Campus_Id` int(11) DEFAULT NULL,
  `DateRegistered` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `tbl_webaccounts`
--

INSERT INTO `tbl_webaccounts` (`WebAccounts_Address`, `ActorCategory_Id`, `Username`, `Password`, `Email`, `Firstname`, `Lastname`, `PinCode`, `IsAccountActive`, `Campus_Id`, `DateRegistered`) VALUES
('ACT000000000000', 2, 'Accounting', '$2y$10$R6Sq.XaRAnrozof/p8bA.uWpZiSPia2bJoMXPRYf5vDu2N3L9KvYO', 'jameslayson.0@gmail.com', 'Accounting', 'Testing', '$2y$10$bJg1AIy6JN9P4pexLTyEfeTNnLOuO4B4D.ZbefLCUydUgeg7sYSBS', b'1', 1, '2023-10-17 16:41:18'),
('ADM000000000000', 1, 'Administrator', '$2y$10$R6Sq.XaRAnrozof/p8bA.uWpZiSPia2bJoMXPRYf5vDu2N3L9KvYO', 'jameslayson.0@gmail.com', 'Administrator11', 'Administrator1223', '$2y$10$xCekFilC440bK4Tc2T9nX.NkJlCgzpcw3Uu6w5TcXs6./TPykkr4K', b'1', 1, '2023-10-17 16:41:18'),
('MTA000000000000', 3, 'MerchantAdmin', '$2y$10$R6Sq.XaRAnrozof/p8bA.uWpZiSPia2bJoMXPRYf5vDu2N3L9KvYO', 'jameslayson.0@gmail.com', 'Merchant Admin', 'Nigga', '$2y$10$bJg1AIy6JN9P4pexLTyEfeTNnLOuO4B4D.ZbefLCUydUgeg7sYSBS', b'1', 1, '2023-10-17 16:41:18'),
('MTS000000000000', 4, 'MerchantStaff', '$2y$10$R6Sq.XaRAnrozof/p8bA.uWpZiSPia2bJoMXPRYf5vDu2N3L9KvYO', 'jameslayson.0@gmail.com', 'Merchant', 'Staff', '$2y$10$bJg1AIy6JN9P4pexLTyEfeTNnLOuO4B4D.ZbefLCUydUgeg7sYSBS', b'1', 1, '2023-10-17 16:41:18');

-- --------------------------------------------------------

--
-- Table structure for table `tbl_whitelist`
--

CREATE TABLE `tbl_whitelist` (
  `Account_Address` varchar(15) NOT NULL,
  `Whitelisted_Address` varchar(15) NOT NULL,
  `Timestamp` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `tbl_activitylogs`
--
ALTER TABLE `tbl_activitylogs`
  ADD PRIMARY KEY (`ActivityLogs_Id`);

--
-- Indexes for table `tbl_actorcategory`
--
ALTER TABLE `tbl_actorcategory`
  ADD PRIMARY KEY (`ActorCategory_Id`);

--
-- Indexes for table `tbl_authentications`
--
ALTER TABLE `tbl_authentications`
  ADD PRIMARY KEY (`Account_Address`);

--
-- Indexes for table `tbl_campus`
--
ALTER TABLE `tbl_campus`
  ADD PRIMARY KEY (`Campus_Id`);

--
-- Indexes for table `tbl_card`
--
ALTER TABLE `tbl_card`
  ADD PRIMARY KEY (`Card_Id`),
  ADD UNIQUE KEY `Card_Address` (`Card_Address`),
  ADD UNIQUE KEY `UsersAccount_Address` (`UsersAccount_Address`),
  ADD KEY `Campus_Id` (`Campus_Id`);

--
-- Indexes for table `tbl_configurations`
--
ALTER TABLE `tbl_configurations`
  ADD PRIMARY KEY (`Configuration_Id`);

--
-- Indexes for table `tbl_guardianaccount`
--
ALTER TABLE `tbl_guardianaccount`
  ADD PRIMARY KEY (`GuardianAccount_Address`),
  ADD KEY `UsersAccount_Address` (`UsersAccount_Address`),
  ADD KEY `ActorCategory_Id` (`ActorCategory_Id`),
  ADD KEY `Campus_Id` (`Campus_Id`);

--
-- Indexes for table `tbl_itemscategory`
--
ALTER TABLE `tbl_itemscategory`
  ADD PRIMARY KEY (`ItemsCategory_Id`),
  ADD KEY `MerchantsCategory_Id` (`MerchantsCategory_Id`);

--
-- Indexes for table `tbl_loginhistory`
--
ALTER TABLE `tbl_loginhistory`
  ADD PRIMARY KEY (`Account_Address`,`IpAddress`,`Location`,`Device`) USING BTREE,
  ADD UNIQUE KEY `unique_loginhistory` (`Account_Address`,`IpAddress`,`Location`,`Device`);

--
-- Indexes for table `tbl_merchantitems`
--
ALTER TABLE `tbl_merchantitems`
  ADD PRIMARY KEY (`MerchantItems_Id`),
  ADD KEY `MerchantsCategory_Id` (`MerchantsCategory_Id`);

--
-- Indexes for table `tbl_merchants`
--
ALTER TABLE `tbl_merchants`
  ADD PRIMARY KEY (`WebAccounts_Address`),
  ADD KEY `MerchantsCategory_Id` (`MerchantsCategory_Id`);

--
-- Indexes for table `tbl_merchantscategory`
--
ALTER TABLE `tbl_merchantscategory`
  ADD PRIMARY KEY (`MerchantsCategory_Id`),
  ADD KEY `Campus_Id` (`Campus_Id`);

--
-- Indexes for table `tbl_notifications`
--
ALTER TABLE `tbl_notifications`
  ADD PRIMARY KEY (`Notification_ID`),
  ADD KEY `Creator_Account_Address` (`Creator_Account_Address`);

--
-- Indexes for table `tbl_ordersvalidation`
--
ALTER TABLE `tbl_ordersvalidation`
  ADD PRIMARY KEY (`WebAccounts_Address`),
  ADD UNIQUE KEY `WebAccounts_Address` (`WebAccounts_Address`,`UsersAccount_Address`),
  ADD KEY `UsersAccount_Address` (`UsersAccount_Address`);

--
-- Indexes for table `tbl_remittance`
--
ALTER TABLE `tbl_remittance`
  ADD PRIMARY KEY (`Remittance_Id`),
  ADD KEY `Submitted_By` (`Submitted_By`);

--
-- Indexes for table `tbl_transactionitems`
--
ALTER TABLE `tbl_transactionitems`
  ADD PRIMARY KEY (`Transaction_Address`,`MerchantItems_Id`) USING BTREE,
  ADD KEY `MerchantItems_Id` (`MerchantItems_Id`);

--
-- Indexes for table `tbl_transactions`
--
ALTER TABLE `tbl_transactions`
  ADD PRIMARY KEY (`Transaction_Address`,`Account_Address`) USING BTREE,
  ADD UNIQUE KEY `Transaction_Address` (`Transaction_Address`,`Account_Address`),
  ADD KEY `Remittance_Id` (`Remittance_Id`);

--
-- Indexes for table `tbl_transactionsinfo`
--
ALTER TABLE `tbl_transactionsinfo`
  ADD PRIMARY KEY (`Transaction_Address`),
  ADD KEY `TransactionType_Id` (`TransactionType_Id`);

--
-- Indexes for table `tbl_transactiontype`
--
ALTER TABLE `tbl_transactiontype`
  ADD PRIMARY KEY (`TransactionType_Id`);

--
-- Indexes for table `tbl_usersaccount`
--
ALTER TABLE `tbl_usersaccount`
  ADD PRIMARY KEY (`UsersAccount_Address`),
  ADD KEY `ActorCategory_Id` (`ActorCategory_Id`),
  ADD KEY `Campus_Id` (`Campus_Id`);

--
-- Indexes for table `tbl_usersdata`
--
ALTER TABLE `tbl_usersdata`
  ADD PRIMARY KEY (`UsersAccount_Address`),
  ADD KEY `GuardianAccount_Address` (`GuardianAccount_Address`);

--
-- Indexes for table `tbl_webaccounts`
--
ALTER TABLE `tbl_webaccounts`
  ADD PRIMARY KEY (`WebAccounts_Address`),
  ADD KEY `ActorCategory_Id` (`ActorCategory_Id`),
  ADD KEY `Campus_Id` (`Campus_Id`);

--
-- Indexes for table `tbl_whitelist`
--
ALTER TABLE `tbl_whitelist`
  ADD PRIMARY KEY (`Account_Address`,`Whitelisted_Address`) USING BTREE,
  ADD UNIQUE KEY `Account_Address` (`Account_Address`,`Whitelisted_Address`),
  ADD KEY `Whitelisted_Address` (`Whitelisted_Address`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `tbl_activitylogs`
--
ALTER TABLE `tbl_activitylogs`
  MODIFY `ActivityLogs_Id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `tbl_actorcategory`
--
ALTER TABLE `tbl_actorcategory`
  MODIFY `ActorCategory_Id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT for table `tbl_campus`
--
ALTER TABLE `tbl_campus`
  MODIFY `Campus_Id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `tbl_card`
--
ALTER TABLE `tbl_card`
  MODIFY `Card_Id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=12;

--
-- AUTO_INCREMENT for table `tbl_configurations`
--
ALTER TABLE `tbl_configurations`
  MODIFY `Configuration_Id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=14;

--
-- AUTO_INCREMENT for table `tbl_itemscategory`
--
ALTER TABLE `tbl_itemscategory`
  MODIFY `ItemsCategory_Id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT for table `tbl_merchantitems`
--
ALTER TABLE `tbl_merchantitems`
  MODIFY `MerchantItems_Id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- AUTO_INCREMENT for table `tbl_merchantscategory`
--
ALTER TABLE `tbl_merchantscategory`
  MODIFY `MerchantsCategory_Id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `tbl_notifications`
--
ALTER TABLE `tbl_notifications`
  MODIFY `Notification_ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;

--
-- AUTO_INCREMENT for table `tbl_remittance`
--
ALTER TABLE `tbl_remittance`
  MODIFY `Remittance_Id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;

--
-- AUTO_INCREMENT for table `tbl_transactiontype`
--
ALTER TABLE `tbl_transactiontype`
  MODIFY `TransactionType_Id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `tbl_card`
--
ALTER TABLE `tbl_card`
  ADD CONSTRAINT `tbl_card_ibfk_1` FOREIGN KEY (`UsersAccount_Address`) REFERENCES `tbl_usersdata` (`UsersAccount_Address`),
  ADD CONSTRAINT `tbl_card_ibfk_2` FOREIGN KEY (`Campus_Id`) REFERENCES `tbl_campus` (`Campus_Id`);

--
-- Constraints for table `tbl_guardianaccount`
--
ALTER TABLE `tbl_guardianaccount`
  ADD CONSTRAINT `tbl_guardianaccount_ibfk_1` FOREIGN KEY (`UsersAccount_Address`) REFERENCES `tbl_usersdata` (`UsersAccount_Address`),
  ADD CONSTRAINT `tbl_guardianaccount_ibfk_2` FOREIGN KEY (`ActorCategory_Id`) REFERENCES `tbl_actorcategory` (`ActorCategory_Id`),
  ADD CONSTRAINT `tbl_guardianaccount_ibfk_3` FOREIGN KEY (`Campus_Id`) REFERENCES `tbl_campus` (`Campus_Id`);

--
-- Constraints for table `tbl_itemscategory`
--
ALTER TABLE `tbl_itemscategory`
  ADD CONSTRAINT `tbl_itemscategory_ibfk_1` FOREIGN KEY (`MerchantsCategory_Id`) REFERENCES `tbl_merchantscategory` (`MerchantsCategory_Id`);

--
-- Constraints for table `tbl_merchantitems`
--
ALTER TABLE `tbl_merchantitems`
  ADD CONSTRAINT `tbl_merchantitems_ibfk_1` FOREIGN KEY (`MerchantsCategory_Id`) REFERENCES `tbl_merchantscategory` (`MerchantsCategory_Id`);

--
-- Constraints for table `tbl_merchants`
--
ALTER TABLE `tbl_merchants`
  ADD CONSTRAINT `tbl_merchants_ibfk_1` FOREIGN KEY (`WebAccounts_Address`) REFERENCES `tbl_webaccounts` (`WebAccounts_Address`),
  ADD CONSTRAINT `tbl_merchants_ibfk_2` FOREIGN KEY (`MerchantsCategory_Id`) REFERENCES `tbl_merchantscategory` (`MerchantsCategory_Id`);

--
-- Constraints for table `tbl_merchantscategory`
--
ALTER TABLE `tbl_merchantscategory`
  ADD CONSTRAINT `tbl_merchantscategory_ibfk_1` FOREIGN KEY (`Campus_Id`) REFERENCES `tbl_campus` (`Campus_Id`);

--
-- Constraints for table `tbl_notifications`
--
ALTER TABLE `tbl_notifications`
  ADD CONSTRAINT `tbl_notifications_ibfk_1` FOREIGN KEY (`Creator_Account_Address`) REFERENCES `tbl_webaccounts` (`WebAccounts_Address`);

--
-- Constraints for table `tbl_ordersvalidation`
--
ALTER TABLE `tbl_ordersvalidation`
  ADD CONSTRAINT `tbl_ordersvalidation_ibfk_1` FOREIGN KEY (`WebAccounts_Address`) REFERENCES `tbl_webaccounts` (`WebAccounts_Address`),
  ADD CONSTRAINT `tbl_ordersvalidation_ibfk_2` FOREIGN KEY (`UsersAccount_Address`) REFERENCES `tbl_usersaccount` (`UsersAccount_Address`);

--
-- Constraints for table `tbl_remittance`
--
ALTER TABLE `tbl_remittance`
  ADD CONSTRAINT `tbl_remittance_ibfk_1` FOREIGN KEY (`Submitted_By`) REFERENCES `tbl_webaccounts` (`WebAccounts_Address`);

--
-- Constraints for table `tbl_transactionitems`
--
ALTER TABLE `tbl_transactionitems`
  ADD CONSTRAINT `tbl_transactionitems_ibfk_1` FOREIGN KEY (`MerchantItems_Id`) REFERENCES `tbl_merchantitems` (`MerchantItems_Id`),
  ADD CONSTRAINT `tbl_transactionitems_ibfk_2` FOREIGN KEY (`Transaction_Address`) REFERENCES `tbl_transactionsinfo` (`Transaction_Address`);

--
-- Constraints for table `tbl_transactions`
--
ALTER TABLE `tbl_transactions`
  ADD CONSTRAINT `tbl_transactions_ibfk_1` FOREIGN KEY (`Remittance_Id`) REFERENCES `tbl_remittance` (`Remittance_Id`);

--
-- Constraints for table `tbl_transactionsinfo`
--
ALTER TABLE `tbl_transactionsinfo`
  ADD CONSTRAINT `tbl_transactionsinfo_ibfk_1` FOREIGN KEY (`TransactionType_Id`) REFERENCES `tbl_transactiontype` (`TransactionType_Id`);

--
-- Constraints for table `tbl_usersaccount`
--
ALTER TABLE `tbl_usersaccount`
  ADD CONSTRAINT `tbl_usersaccount_ibfk_1` FOREIGN KEY (`ActorCategory_Id`) REFERENCES `tbl_actorcategory` (`ActorCategory_Id`),
  ADD CONSTRAINT `tbl_usersaccount_ibfk_2` FOREIGN KEY (`Campus_Id`) REFERENCES `tbl_campus` (`Campus_Id`);

--
-- Constraints for table `tbl_usersdata`
--
ALTER TABLE `tbl_usersdata`
  ADD CONSTRAINT `tbl_usersdata_ibfk_1` FOREIGN KEY (`GuardianAccount_Address`) REFERENCES `tbl_guardianaccount` (`GuardianAccount_Address`);

--
-- Constraints for table `tbl_webaccounts`
--
ALTER TABLE `tbl_webaccounts`
  ADD CONSTRAINT `tbl_webaccounts_ibfk_1` FOREIGN KEY (`ActorCategory_Id`) REFERENCES `tbl_actorcategory` (`ActorCategory_Id`),
  ADD CONSTRAINT `tbl_webaccounts_ibfk_2` FOREIGN KEY (`Campus_Id`) REFERENCES `tbl_campus` (`Campus_Id`);

--
-- Constraints for table `tbl_whitelist`
--
ALTER TABLE `tbl_whitelist`
  ADD CONSTRAINT `tbl_whitelist_ibfk_1` FOREIGN KEY (`Account_Address`) REFERENCES `tbl_usersaccount` (`UsersAccount_Address`),
  ADD CONSTRAINT `tbl_whitelist_ibfk_2` FOREIGN KEY (`Whitelisted_Address`) REFERENCES `tbl_usersaccount` (`UsersAccount_Address`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
