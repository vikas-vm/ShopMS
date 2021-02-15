-- phpMyAdmin SQL Dump
-- version 5.0.4
-- https://www.phpmyadmin.net/
--
-- Host: localhost
-- Generation Time: Feb 14, 2021 at 08:13 PM
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
(1, 'dark-theme.css', '0');

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
(4, 'besan', '', '2021-02-14 02:30:04');

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
(1, 1, 5, 'Ashirwaad', 30, 28, 50, NULL, '0', '2021-02-10 19:32:12'),
(2, 2, 5, 'Ashiwaad', 28, 26, 50, NULL, '0', '2021-02-12 01:07:46'),
(3, 1, 18, 'mais', 897, 87, 19, NULL, '0', '2021-02-12 01:16:44'),
(4, 1, 18, 'lkjffvk', 143, 20, 90, NULL, '0', '2021-02-12 01:17:16'),
(5, 2, 16, 'kljh', 98, 98, 89, NULL, '0', '2021-02-12 01:27:29'),
(6, 2, 16, 'oiuy', 87, 67, 566, NULL, '0', '2021-02-12 01:27:48'),
(7, 1, 5, 'uhyfriu', 0, 0, 876, NULL, '0', '2021-02-12 09:53:40'),
(8, 2, 5, 'jhgj', 0, 0, 76, NULL, '0', '2021-02-12 09:54:13'),
(9, 2, 16, 'lkjhgfc', 0, 0, 89, NULL, '0', '2021-02-13 18:39:23'),
(10, 2, 16, 'kjhgf', 0, 0, 89, NULL, '1', '2021-02-13 18:39:40'),
(11, 2, 13, 'jhu', 0, 0, 890, NULL, '0', '2021-02-13 23:35:11'),
(12, 2, 10, 'iuhyh', 0, 0, 10, NULL, '0', '2021-02-13 23:36:13'),
(13, 1, 7, 'lhg', 0, 0, 4, NULL, '1', '2021-02-14 00:45:02'),
(14, 2, 13, 'n,mdvcm', 0, 0, 60, NULL, '0', '2021-02-14 01:44:45'),
(15, 2, 7, 'kjhg', 0, 0, 98, NULL, '0', '2021-02-14 02:44:01'),
(16, 2, 16, 'lkjhgf', 0, 0, 50, NULL, '0', '2021-02-14 11:01:23'),
(17, 2, 5, 'oiuygtfr', 0, 0, 81, NULL, '0', '2021-02-14 20:43:07'),
(18, 2, 5, 'hyut', 0, 0, 987, NULL, '1', '2021-02-14 20:44:11');

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
(2, 'Lal JI', '', 'Purnea', '98765', '', '2021-02-06 10:08:25'),
(3, 'Maa General Store', 0x47616e646869204e61676172, 'Purnea', '', '', '2021-02-06 10:08:25'),
(4, 'ShuBh', 0x75797409, 'iuty', '0987654', 'iuyt', '2021-02-06 10:08:25'),
(5, 'shuaghjg', '', ';kljhigf', '', '', '2021-02-06 10:08:25'),
(6, 'shuaghjg', '', ';kljhigf', '', '', '2021-02-06 10:08:25'),
(7, 'shuaghjg', '', 'kljhigf', '', '', '2021-02-06 10:08:25'),
(8, '8uiytf', '', 'iouy', '', '', '2021-02-06 10:08:25'),
(9, 'iouy', '', 'oiuy', '', '', '2021-02-06 10:08:25'),
(10, 'oiu', '', 'oiuy', '', '', '2021-02-06 10:08:25'),
(11, 'vikas', '', 'olkjh', '', '', '2021-02-06 10:08:25'),
(12, 'vikas', '', 'olkjh', '', '', '2021-02-06 10:08:25'),
(13, 'oijhkg', '', 'ioluk', '', '', '2021-02-06 10:08:25'),
(14, 'oijhkg', '', 'ioluk', '', '', '2021-02-06 10:08:25'),
(15, 'p98768', '', 'uiyt', '', '', '2021-02-06 10:08:25'),
(16, 'oiuy', 0x75790909, '897y', 'iouy', 'iuy', '2021-02-06 10:08:25'),
(17, 'uiyt', '', 'uiyt', '', '', '2021-02-06 10:08:25'),
(18, 'iouyt', 0x6b6a6809, 'jhg', '9876', '', '2021-02-06 10:08:25'),
(19, 'lkuy', '', 'kjhg', '', '', '2021-02-06 10:08:25'),
(20, '8796', '', 'yutr', '', '', '2021-02-06 10:08:25'),
(21, '9876fgh', '', 'rfghj', '', '', '2021-02-06 10:08:25'),
(22, '8fuyguyeghfy7u', 0x09, 'fuivsidhy', '', '', '2021-02-06 10:08:25'),
(23, 'iuiy', 0x697579, 'uyg', '', '', '2021-02-06 20:42:55'),
(24, 'iou', '', 'iouy', '', '', '2021-02-06 20:44:55'),
(25, 'oiu', 0x09, 'iuoy', '', '', '2021-02-06 20:50:03'),
(26, 'oiutydr', '', 'kjhgf', '8765', '', '2021-02-08 09:22:02'),
(27, 'Shubham', 0x6a6b6779746664786376626b, 'Purnea', '9876543567', 'shubham@gmail.com', '2021-02-08 09:24:19'),
(28, 'kljh', '', 'lkm', '', '', '2021-02-11 00:00:31'),
(29, '[poiujh', '', 'oijhuy', 'opi', '', '2021-02-11 00:01:40'),
(30, ';oiutre', '', '87654e', '', '', '2021-02-11 10:47:17'),
(31, 'ou8ty', '', 'iut', '', '', '2021-02-12 00:16:51'),
(32, 'oiuytf', '', ';poiuyg', '', '', '2021-02-12 00:21:30'),
(33, 'hujhfj', '', 'hfdfuih', '', '', '2021-02-12 09:52:36'),
(34, 'hgbfhdgb', '', 'kjh', 'lkndd', '', '2021-02-14 01:41:56'),
(35, 'hjfhjg', '', 'hjukjk', '', '', '2021-02-14 01:44:12'),
(36, 'gyrg', '', 'kjfhkr', '', '', '2021-02-14 12:03:23');

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
(3, 'order1', 12, '0', '2021-02-06 10:21:46'),
(4, 'order2', 15, '0', '2021-02-06 10:21:46'),
(5, 'order3', 2, '0', '2021-02-06 10:22:23'),
(6, 'order1', 2, '0', '2021-02-08 23:16:15'),
(7, 'order1', 5, '0', '2021-02-08 23:17:49'),
(8, 'oredr2', 2, '0', '2021-02-08 23:19:18'),
(9, 'order1', 8, '0', '2021-02-08 23:20:04'),
(10, 'LIJUYTRE', 8, '0', '2021-02-09 09:14:29'),
(11, 'order4', 2, '0', '2021-02-09 09:27:58'),
(12, 'oi8utre', 2, '0', '2021-02-09 10:02:41'),
(13, 'order1', 3, '0', '2021-02-09 10:03:46'),
(14, 'oiuy', 8, '0', '2021-02-10 19:09:22'),
(15, ';lkjbh ', 29, '0', '2021-02-11 00:01:48'),
(16, 'iuytrew', 3, '0', '2021-02-11 10:47:29'),
(17, 'odrejkdbfh', 2, '0', '2021-02-11 22:23:53'),
(18, 'New1', 4, '0', '2021-02-12 01:15:54'),
(19, 'liuygtfd', 6, '0', '2021-02-12 01:21:54'),
(20, 'yugdth', 2, '0', '2021-02-12 09:53:17'),
(21, 'hjfd', 8, '0', '2021-02-14 10:40:38');

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
-- Indexes for table `items`
--
ALTER TABLE `items`
  ADD PRIMARY KEY (`id`),
  ADD KEY `cat_id` (`cat_id`),
  ADD KEY `vo_id` (`vo_id`);

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
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `items`
--
ALTER TABLE `items`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=19;

--
-- AUTO_INCREMENT for table `vendors`
--
ALTER TABLE `vendors`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=37;

--
-- AUTO_INCREMENT for table `vendor_orders`
--
ALTER TABLE `vendor_orders`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=22;

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
-- Constraints for table `vendor_orders`
--
ALTER TABLE `vendor_orders`
  ADD CONSTRAINT `vendor_orders_ibfk_1` FOREIGN KEY (`v_id`) REFERENCES `vendors` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
