-- phpMyAdmin SQL Dump
-- version 4.7.0
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1:3307
-- Erstellungszeit: 09. Feb 2022 um 11:29
-- Server-Version: 10.1.25-MariaDB
-- PHP-Version: 7.2.9

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Datenbank: `jdbcdemo`
--

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `course`
--

CREATE TABLE `course` (
  `id` int(11) NOT NULL,
  `coursename` varchar(255) NOT NULL,
  `room` varchar(255) NOT NULL,
  `seats` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Daten für Tabelle `course`
--

INSERT INTO `course` (`id`, `coursename`, `room`, `seats`) VALUES
(1, 'physic', '22b', 33),
(2, 'maths', '11a', 56),
(3, 'informatics', '24c', 42);

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `student`
--

CREATE TABLE `student` (
  `id` int(11) NOT NULL,
  `name` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Daten für Tabelle `student`
--

INSERT INTO `student` (`id`, `name`, `email`) VALUES
(2, 'Noggolas', 'niklas@g.at'),
(3, 'Michael', 'mbogensberger@gmail.com'),
(6, 'Mingus', 'ming@mongolien.rocks\r\n'),
(7, 'Phil', 'phil@soll.at'),
(8, 'Jonas', 'jonas@em.at');

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `student_course`
--

CREATE TABLE `student_course` (
  `id` int(11) NOT NULL,
  `student_id` int(11) NOT NULL,
  `course_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Daten für Tabelle `student_course`
--

INSERT INTO `student_course` (`id`, `student_id`, `course_id`) VALUES
(1, 3, 2),
(2, 3, 1),
(3, 2, 2),
(4, 6, 1),
(5, 8, 2),
(6, 7, 2),
(7, 3, 3);

--
-- Indizes der exportierten Tabellen
--

--
-- Indizes für die Tabelle `course`
--
ALTER TABLE `course`
  ADD PRIMARY KEY (`id`);

--
-- Indizes für die Tabelle `student`
--
ALTER TABLE `student`
  ADD PRIMARY KEY (`id`);

--
-- Indizes für die Tabelle `student_course`
--
ALTER TABLE `student_course`
  ADD PRIMARY KEY (`id`),
  ADD KEY `course_id` (`course_id`),
  ADD KEY `student_id` (`student_id`);

--
-- AUTO_INCREMENT für exportierte Tabellen
--

--
-- AUTO_INCREMENT für Tabelle `course`
--
ALTER TABLE `course`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;
--
-- AUTO_INCREMENT für Tabelle `student`
--
ALTER TABLE `student`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;
--
-- AUTO_INCREMENT für Tabelle `student_course`
--
ALTER TABLE `student_course`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;
--
-- Constraints der exportierten Tabellen
--

--
-- Constraints der Tabelle `student_course`
--
ALTER TABLE `student_course`
  ADD CONSTRAINT `student_course_ibfk_1` FOREIGN KEY (`student_id`) REFERENCES `student` (`id`),
  ADD CONSTRAINT `student_course_ibfk_2` FOREIGN KEY (`course_id`) REFERENCES `course` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
