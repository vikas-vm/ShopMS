-- phpMyAdmin SQL Dump
-- version 5.0.4
-- https://www.phpmyadmin.net/
--
-- Host: localhost
-- Generation Time: Feb 25, 2021 at 07:34 AM
-- Server version: 10.4.17-MariaDB
-- PHP Version: 8.0.0

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `shop_sample`
--

-- --------------------------------------------------------

--
-- Table structure for table `admins`
--

CREATE TABLE `admins` (
  `id` int(11) NOT NULL,
  `username` varchar(50) NOT NULL,
  `password` varchar(50) NOT NULL,
  `time_of_creation` datetime NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `admins`
--

INSERT INTO `admins` (`id`, `username`, `password`, `time_of_creation`) VALUES
(2, 'admin', '1234', '2021-02-06 09:44:52');

-- --------------------------------------------------------

--
-- Table structure for table `appearance`
--

CREATE TABLE `appearance` (
  `id` int(11) NOT NULL,
  `themeFile` varchar(30) NOT NULL,
  `status` enum('0','1') NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `appearance`
--

INSERT INTO `appearance` (`id`, `themeFile`, `status`) VALUES
(1, 'dark-chocolate.css', '0');

-- --------------------------------------------------------

--
-- Table structure for table `categories`
--

CREATE TABLE `categories` (
  `id` int(11) NOT NULL,
  `title` varchar(20) NOT NULL,
  `description` blob DEFAULT NULL,
  `time_of_creation` datetime NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `categories`
--

INSERT INTO `categories` (`id`, `title`, `description`, `time_of_creation`) VALUES
(1, 'maida', NULL, '2021-02-06 09:40:29'),
(2, 'aata', NULL, '2021-02-11 19:19:47'),
(3, 'Rice', '', '2021-02-14 02:29:47'),
(4, 'besan', '', '2021-02-14 02:30:04'),
(5, 'shampoo', '', '2021-02-16 18:27:35'),
(6, 'Chana', 0x090a, '2021-02-17 08:35:43'),
(7, 'Daal', '', '2021-02-17 08:36:05'),
(8, 'Sarso Tel', '', '2021-02-17 08:36:26'),
(9, 'Protein Packs', '', '2021-02-17 08:50:09'),
(10, 'Mineral Water', '', '2021-02-18 10:47:36'),
(11, 'copy', 0x646464, '2021-02-23 10:23:20');

-- --------------------------------------------------------

--
-- Table structure for table `customers`
--

CREATE TABLE `customers` (
  `id` int(11) NOT NULL,
  `name` varchar(30) NOT NULL,
  `contact` varchar(12) NOT NULL,
  `email` varchar(30) NOT NULL,
  `address` varchar(40) NOT NULL,
  `regularity_count` int(11) NOT NULL,
  `time_of_creation` datetime NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `customers`
--

INSERT INTO `customers` (`id`, `name`, `contact`, `email`, `address`, `regularity_count`, `time_of_creation`) VALUES
(1, 'vikas', '8409606050', '', '', 3, '2021-02-21 14:51:43'),
(2, 'vikas', '8409606050', '', '', 2, '2021-02-21 14:57:50'),
(3, 'vikas', '8409606050', 'vikas@email.com', 'Purnea', 8, '2021-02-21 15:29:47'),
(4, 'Shubham', '8796567575', '', 'Purnea', 3, '2021-02-21 15:43:16'),
(5, 'vikas', '8409606050', '', '', 2, '2021-02-22 00:09:40'),
(6, 'vikas', '8409606050', '', '', 2, '2021-02-22 00:12:23'),
(7, 'Kamana', '78865475', 'Kammo@email.com', 'Purnea', 3, '2021-02-22 00:44:35');

-- --------------------------------------------------------

--
-- Table structure for table `items`
--

CREATE TABLE `items` (
  `id` int(11) NOT NULL,
  `cat_id` int(11) NOT NULL,
  `vo_id` int(11) NOT NULL,
  `title` varchar(30) NOT NULL,
  `mrp` float DEFAULT NULL,
  `price` float DEFAULT NULL,
  `stock` float NOT NULL,
  `initial` float DEFAULT NULL,
  `itemType` enum('0','1') NOT NULL,
  `time_of_creation` datetime NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `items`
--

INSERT INTO `items` (`id`, `cat_id`, `vo_id`, `title`, `mrp`, `price`, `stock`, `initial`, `itemType`, `time_of_creation`) VALUES
(1, 2, 1, 'Ashirwaad (5kg)', 145, 128, 35, 35, '1', '2021-02-17 08:42:23'),
(2, 2, 1, 'local', 28, 26, 33, 50, '0', '2021-02-17 08:42:49'),
(3, 1, 1, 'local', 29, 27, -7.385, 25, '0', '2021-02-17 08:43:16'),
(4, 4, 1, 'shreedeo(400g)', 55, 51, 34, 40, '1', '2021-02-17 08:44:02'),
(5, 8, 1, 'Dhara', 140, 135, 48, 50, '1', '2021-02-17 08:44:43'),
(6, 3, 1, 'basmati(10kg)', 725, 675, 92, 100, '1', '2021-02-17 08:45:42'),
(7, 3, 1, 'Plough(25kg)', 800, 774, 20, 20, '1', '2021-02-17 08:46:41'),
(8, 5, 2, 'Head&Shoulder(2in1)', 4, 3.65, 69, 100, '1', '2021-02-17 08:48:37'),
(9, 5, 2, 'Head&Shoulder', 2, 1.85, 98.5, 100, '0', '2021-02-17 08:49:12'),
(10, 5, 2, 'Clinic plus', 1, 0.9, 114, 150, '1', '2021-02-17 08:49:51'),
(11, 9, 2, 'Horlics(500g)', 380, 370, 40, 40, '1', '2021-02-17 08:50:51'),
(12, 9, 2, 'Born Vita', 395, 370, 10, 20, '1', '2021-02-17 08:51:16'),
(13, 3, 3, 'makhan malay(25kg)', 900, 875, 1, 10, '1', '2021-02-17 09:36:39'),
(14, 1, 4, 'local', 33, 30, 10.5, 30, '0', '2021-02-18 10:18:34'),
(15, 3, 4, 'rice1(25kg)', 925, 900, 0, 10, '1', '2021-02-18 10:19:40'),
(16, 10, 5, 'Bisleri', 20, 15, 42, 50, '1', '2021-02-18 10:49:21'),
(17, 2, 6, 'JeevanBhog(5kg)', 120, 140, 20, 30, '1', '2021-02-18 11:12:00'),
(18, 3, 6, 'rice', 32, 30, 60.975, 100, '0', '2021-02-18 11:12:40'),
(19, 4, 7, 'Anoop', 110, 99, 295, 300, '1', '2021-02-18 11:52:42'),
(20, 1, 8, 'local', 33, 29, 20, NULL, '0', '2021-02-21 00:32:35'),
(21, 1, 10, 'anoop', 60, 50, 10, 10, '0', '2021-02-23 10:24:54'),
(22, 3, 11, 'plough(25kg)', 820, 770, 10, 10, '0', '2021-02-23 10:54:35');

-- --------------------------------------------------------

--
-- Table structure for table `order_items`
--

CREATE TABLE `order_items` (
  `id` int(11) NOT NULL,
  `item_id` int(11) NOT NULL,
  `order_id` int(11) NOT NULL,
  `price` float DEFAULT NULL,
  `qty` varchar(50) NOT NULL,
  `time_of_creation` datetime NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `order_items`
--

INSERT INTO `order_items` (`id`, `item_id`, `order_id`, `price`, `qty`, `time_of_creation`) VALUES
(2, 10, 1, 1, '8', '2021-02-17 09:37:12'),
(3, 13, 1, 900, '2', '2021-02-17 21:57:39'),
(4, 6, 1, 725, '1', '2021-02-17 21:58:11'),
(5, 3, 1, 29, '10.795', '2021-02-18 09:22:58'),
(6, 2, 2, 28, '2', '2021-02-18 09:42:02'),
(7, 5, 3, 140, '1', '2021-02-18 09:42:29'),
(8, 2, 3, 28, '8', '2021-02-18 09:43:55'),
(9, 6, 4, 725, '5', '2021-02-18 09:46:06'),
(10, 2, 5, 28, '2', '2021-02-18 09:52:36'),
(11, 4, 6, 55, '5', '2021-02-18 09:52:57'),
(15, 14, 8, 33, '9', '2021-02-18 10:24:48'),
(16, 8, 8, 4, '20', '2021-02-18 10:51:08'),
(17, 9, 8, 2, '1.5', '2021-02-18 10:51:37'),
(18, 15, 9, 925, '8', '2021-02-18 10:52:18'),
(19, 18, 10, 32, '10.525', '2021-02-18 11:18:07'),
(20, 17, 10, 120, '5', '2021-02-18 11:18:35'),
(21, 2, 12, 28, '5', '2021-02-18 11:20:32'),
(22, 19, 13, 110, '5', '2021-02-18 11:54:41'),
(23, 12, 13, 395, '10', '2021-02-18 11:54:49'),
(24, 8, 14, 4, '11', '2021-02-19 09:52:21'),
(25, 10, 14, 1, '10', '2021-02-19 09:52:48'),
(27, 16, 15, 20, '2', '2021-02-21 15:39:46'),
(38, 16, 16, 20, '5', '2021-02-21 23:45:16'),
(39, 17, 16, 120, '1', '2021-02-21 23:55:01'),
(40, 18, 17, 32, '9', '2021-02-22 00:11:34'),
(41, 17, 18, 120, '1', '2021-02-22 00:12:41'),
(42, 14, 19, 33, '1', '2021-02-22 00:40:26'),
(43, 17, 20, 120, '1', '2021-02-22 00:42:31'),
(44, 18, 20, 32, '3', '2021-02-22 00:42:55'),
(45, 17, 21, 120, '1', '2021-02-22 00:43:19'),
(46, 18, 22, 32, '1', '2021-02-22 00:43:57'),
(47, 18, 23, 32, '1', '2021-02-22 00:44:46'),
(48, 18, 24, 32, '4', '2021-02-22 00:45:09'),
(49, 18, 25, 32, '.5', '2021-02-22 00:45:38'),
(50, 17, 26, 120, '1', '2021-02-22 23:56:39'),
(51, 18, 27, 32, '10', '2021-02-23 00:00:57'),
(52, 13, 27, 900, '3', '2021-02-23 10:19:57'),
(53, 5, 28, 140, '1', '2021-02-23 11:05:14'),
(54, 10, 28, 1, '2', '2021-02-23 11:05:20'),
(55, 4, 28, 55, '1', '2021-02-25 01:50:00'),
(56, 16, 29, 20, '1', '2021-02-25 01:54:05');

-- --------------------------------------------------------

--
-- Table structure for table `payments`
--

CREATE TABLE `payments` (
  `id` int(11) NOT NULL,
  `order_id` int(11) NOT NULL,
  `amount` float NOT NULL,
  `time_of_creation` datetime NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `payments`
--

INSERT INTO `payments` (`id`, `order_id`, `amount`, `time_of_creation`) VALUES
(1, 15, 40, '2021-02-21 15:43:16'),
(2, 16, 200, '2021-02-22 00:09:40'),
(3, 17, 300, '2021-02-22 00:12:23'),
(4, 18, 200, '2021-02-22 00:40:18'),
(5, 19, 50, '2021-02-22 00:42:24'),
(6, 20, 300, '2021-02-22 00:43:11'),
(7, 21, 160, '2021-02-22 00:43:48'),
(8, 22, 35, '2021-02-22 00:44:35'),
(9, 23, 29, '2021-02-22 00:44:59'),
(10, 24, 128, '2021-02-22 00:45:29'),
(11, 25, 43, '2021-02-22 23:56:33'),
(12, 26, 150, '2021-02-23 00:00:50'),
(13, 27, 3000, '2021-02-23 10:21:52'),
(14, 28, 147, '2021-02-25 01:53:14'),
(15, 29, 20, '2021-02-25 01:54:17');

-- --------------------------------------------------------

--
-- Table structure for table `shop_orders`
--

CREATE TABLE `shop_orders` (
  `id` int(11) NOT NULL,
  `cust_id` int(11) DEFAULT NULL,
  `total_amt` float DEFAULT NULL,
  `status` enum('0','1') NOT NULL,
  `time_of_creation` datetime NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `shop_orders`
--

INSERT INTO `shop_orders` (`id`, `cust_id`, `total_amt`, `status`, `time_of_creation`) VALUES
(1, NULL, 0, '1', '2021-02-17 08:38:27'),
(2, NULL, 0, '1', '2021-02-18 09:42:00'),
(3, NULL, 0, '1', '2021-02-18 09:42:25'),
(4, NULL, 0, '1', '2021-02-18 09:45:48'),
(5, NULL, 0, '1', '2021-02-18 09:52:25'),
(6, NULL, 0, '1', '2021-02-18 09:52:50'),
(7, NULL, 0, '1', '2021-02-18 10:17:25'),
(8, NULL, 0, '1', '2021-02-18 10:21:34'),
(9, NULL, 0, '1', '2021-02-18 10:52:14'),
(10, NULL, 0, '1', '2021-02-18 10:52:29'),
(11, NULL, 0, '1', '2021-02-18 11:19:57'),
(12, NULL, 0, '1', '2021-02-18 11:20:10'),
(13, NULL, 0, '1', '2021-02-18 11:22:48'),
(14, 3, 54, '1', '2021-02-18 11:56:20'),
(15, 4, 40, '1', '2021-02-21 15:38:12'),
(16, 5, 250, '1', '2021-02-21 17:49:23'),
(17, 6, 288, '1', '2021-02-22 00:09:47'),
(18, 1, 150, '1', '2021-02-22 00:12:26'),
(19, 3, 33, '1', '2021-02-22 00:40:19'),
(20, 3, 246, '1', '2021-02-22 00:42:26'),
(21, 3, 150, '1', '2021-02-22 00:43:13'),
(22, 7, 32, '1', '2021-02-22 00:43:49'),
(23, 7, 32, '1', '2021-02-22 00:44:37'),
(24, 7, 128, '1', '2021-02-22 00:45:01'),
(25, 3, 16, '1', '2021-02-22 00:45:30'),
(26, 4, 150, '1', '2021-02-22 23:56:35'),
(27, 3, 3020, '1', '2021-02-23 00:00:51'),
(28, 1, 197, '1', '2021-02-23 10:21:54'),
(29, 3, 20, '1', '2021-02-25 01:53:16'),
(30, NULL, NULL, '0', '2021-02-25 01:54:19');

-- --------------------------------------------------------

--
-- Table structure for table `vendors`
--

CREATE TABLE `vendors` (
  `id` int(11) NOT NULL,
  `title` varchar(20) NOT NULL,
  `address` blob DEFAULT NULL,
  `city` varchar(30) NOT NULL,
  `contact` varchar(13) DEFAULT NULL,
  `email` varchar(30) DEFAULT NULL,
  `time_of_creation` datetime NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `vendors`
--

INSERT INTO `vendors` (`id`, `title`, `address`, `city`, `contact`, `email`, `time_of_creation`) VALUES
(1, 'Laal ji', 0x4d6164687562616e69, 'Purnea', '', '', '2021-02-17 08:39:06'),
(2, 'Shree Hari Store', '', 'purnea', '', '', '2021-02-17 08:40:25'),
(3, 'Vendor2', '', 'Purnea', '8967564576', '', '2021-02-18 10:48:07');

-- --------------------------------------------------------

--
-- Table structure for table `vendor_orders`
--

CREATE TABLE `vendor_orders` (
  `id` int(11) NOT NULL,
  `v_order_title` varchar(20) NOT NULL,
  `v_id` int(11) NOT NULL,
  `inStock` enum('0','1') NOT NULL,
  `time_of_creation` datetime NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `vendor_orders`
--

INSERT INTO `vendor_orders` (`id`, `v_order_title`, `v_id`, `inStock`, `time_of_creation`) VALUES
(1, 'order1', 1, '1', '2021-02-17 08:40:58'),
(2, 'order1(17Jan2021)', 2, '1', '2021-02-17 08:47:48'),
(3, 'Order2', 1, '1', '2021-02-17 08:57:40'),
(4, 'order3', 1, '1', '2021-02-17 18:42:33'),
(5, 'order1', 3, '1', '2021-02-18 10:48:23'),
(6, 'order4', 1, '1', '2021-02-18 11:10:59'),
(7, 'order5', 1, '1', '2021-02-18 11:52:13'),
(8, 'NewOrder', 1, '0', '2021-02-20 13:26:08'),
(9, 'new order1', 1, '0', '2021-02-23 09:34:23'),
(10, 'new order2', 1, '1', '2021-02-23 10:23:43'),
(11, 'new order2', 3, '1', '2021-02-23 10:53:52');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `admins`
--
ALTER TABLE `admins`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `appearance`
--
ALTER TABLE `appearance`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `categories`
--
ALTER TABLE `categories`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `customers`
--
ALTER TABLE `customers`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `items`
--
ALTER TABLE `items`
  ADD PRIMARY KEY (`id`),
  ADD KEY `cat_id` (`cat_id`),
  ADD KEY `vo_id` (`vo_id`);

--
-- Indexes for table `order_items`
--
ALTER TABLE `order_items`
  ADD PRIMARY KEY (`id`),
  ADD KEY `item_id` (`item_id`),
  ADD KEY `order_id` (`order_id`);

--
-- Indexes for table `payments`
--
ALTER TABLE `payments`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `shop_orders`
--
ALTER TABLE `shop_orders`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `vendors`
--
ALTER TABLE `vendors`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `vendor_orders`
--
ALTER TABLE `vendor_orders`
  ADD PRIMARY KEY (`id`),
  ADD KEY `v_id` (`v_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `admins`
--
ALTER TABLE `admins`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `appearance`
--
ALTER TABLE `appearance`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT for table `categories`
--
ALTER TABLE `categories`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=12;

--
-- AUTO_INCREMENT for table `customers`
--
ALTER TABLE `customers`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT for table `items`
--
ALTER TABLE `items`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=23;

--
-- AUTO_INCREMENT for table `order_items`
--
ALTER TABLE `order_items`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=57;

--
-- AUTO_INCREMENT for table `payments`
--
ALTER TABLE `payments`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=16;

--
-- AUTO_INCREMENT for table `shop_orders`
--
ALTER TABLE `shop_orders`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=31;

--
-- AUTO_INCREMENT for table `vendors`
--
ALTER TABLE `vendors`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `vendor_orders`
--
ALTER TABLE `vendor_orders`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=12;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `items`
--
ALTER TABLE `items`
  ADD CONSTRAINT `items_ibfk_1` FOREIGN KEY (`cat_id`) REFERENCES `categories` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `items_ibfk_2` FOREIGN KEY (`vo_id`) REFERENCES `vendor_orders` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `order_items`
--
ALTER TABLE `order_items`
  ADD CONSTRAINT `order_items_ibfk_1` FOREIGN KEY (`item_id`) REFERENCES `items` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `order_items_ibfk_2` FOREIGN KEY (`order_id`) REFERENCES `shop_orders` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `vendor_orders`
--
ALTER TABLE `vendor_orders`
  ADD CONSTRAINT `vendor_orders_ibfk_1` FOREIGN KEY (`v_id`) REFERENCES `vendors` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
