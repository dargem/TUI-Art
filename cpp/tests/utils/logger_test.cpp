#include "utils/logger.hpp"
#include "gtest/gtest.h"
#include <cstddef>
#include <optional>
#include <fstream>
#include <filesystem>
#include <string>
#include <iostream>

using utils::Logger;
using utils::LogLevel;

namespace
{
    // location of written out file
    constexpr std::string_view TEST_LOC{"test_logs.txt"};
    constexpr std::string_view EXAMPLE_TXT{"An example log entry"};
}

TEST(Logger, LoggersCanBeCreated)
{
    EXPECT_NO_THROW(Logger<LogLevel::INFO>{TEST_LOC}) << "Should be able to build this";
    EXPECT_NO_THROW(Logger<LogLevel::WARN>{TEST_LOC}) << "Should be able to build this";
}

TEST(Logger, LogsIntoFile)
{
    // open and clear the file
    const std::filesystem::path path{TEST_LOC};
    // opens for the file for writing and truncate file length 0 (empties it)
    std::fstream logFile(path, std::ofstream::out | std::ofstream::trunc);
    EXPECT_TRUE(logFile.is_open()) << "File should be opened";
    logFile.close();

    // Log to the file
    std::optional<Logger<LogLevel::INFO>> logger{TEST_LOC};
    logger.value().log<LogLevel::INFO>(EXAMPLE_TXT);
    // safely destroy the old logger
    logger.reset();

    logFile.open(path, std::ios::in);
    EXPECT_TRUE(logFile.is_open()) << "File should be opened";

    std::string line;
    std::getline(logFile, line);

    EXPECT_FALSE(line.empty()) << "Logged line should not be empty!";
    EXPECT_TRUE(line.contains(EXAMPLE_TXT)) << "The logged file should contain the message it outputted";
    EXPECT_TRUE(line.contains("INFO")) << "Should contain the log level of the message";
}
