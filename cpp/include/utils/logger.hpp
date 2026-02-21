#pragma once
#include <cassert>
#include <filesystem>
#include <fstream>
#include <iostream>
#include <string>

namespace utils {

enum class LogLevel : uint8_t {
    TRACE = 0,
    DEBUG,
    INFO,
    WARN,
    ERROR,
    FATAL,
    COUNT  // to allow iteration through log level
};

// A file logger class, will log everything as or more critical than keptLogLevel
template <LogLevel keptLogLevel>
class Logger {
   public:
    Logger(std::string_view filename) {
        const std::filesystem::path path{filename};

        // Check if file exists
        bool fileExists = std::filesystem::exists(path);
        std::ios_base::openmode mode = fileExists ? std::ios::app : std::ios::out;

        logFile.open(path, mode);

        if (!logFile.is_open()) {
            if (fileExists) {
                throw std::runtime_error(
                    "File exists, but unable to open file for writing error logs");
            }
            throw std::runtime_error(
                "Created a file but unable to open file for writing error logs");
        }
    }

    Logger(const Logger&) = delete;
    Logger& operator=(Logger&) = delete;

    // Clean up holding the file
    ~Logger() = default;

    // Log a message to the logger's file
    // This call should be completely optimized away at compile time, if it doesn't log
    template <LogLevel thisLogsLevel>
    void log(std::string_view message) {
        assert(logFile.is_open() && "File should always be open");

        if constexpr (static_cast<int>(thisLogsLevel) >= static_cast<int>(keptLogLevel)) {
            logFile << levelToString(thisLogsLevel) << ": " << message << std::endl;
            logFile.flush();
        }
    }

   private:
    static constexpr std::string_view levelToString(LogLevel level) {
        switch (level) {
            case LogLevel::TRACE:
                return "TRACE";
            case LogLevel::DEBUG:
                return "DEBUG";
            case LogLevel::INFO:
                return "INFO";
            case LogLevel::WARN:
                return "WARN";
            case LogLevel::ERROR:
                return "ERROR";
            case LogLevel::FATAL:
                return "FATAL";
        }

        // Fallback, if adding another log level make sure to map it
        return "UNKNOWN";
    }
    std::ofstream logFile;
};

}  // namespace utils