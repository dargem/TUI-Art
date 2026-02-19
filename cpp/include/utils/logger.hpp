#pragma once
#include <fstream>
#include <string>
#include <iostream>
#include <filesystem>
#include <assert.h>

namespace utils
{

    enum class LogLevel
    {
        TRACE,
        DEBUG,
        INFO,
        WARN,
        ERROR,
        FATAL
    };

    // A file logger class, will log everything as or more critical than keptLogLevel
    template <LogLevel keptLogLevel>
    class Logger
    {
    public:
        Logger(const std::string &filename = "logs.txt")
        {
            // Check if file exists
            bool fileExists = std::filesystem::exists(filename);

            // Open in append mode if exists, otherwise create new
            std::ios_base::openmode mode = fileExists ? std::ios::app : std::ios::out;
            logFile.open(filename, mode);

            if (!logFile.is_open())
            {
                if (fileExists)
                {
                    throw std::runtime_error("File exists, but unable to open file for writing error logs");
                }
                else
                {
                    throw std::runtime_error("Created a file but unable to open file for writing error logs");
                }
            }
        }

        // Clean up holding the file
        ~Logger()
        {
            if (logFile.is_open())
            {
                logFIle.close();
            }
        }

        // Log a message to the logger's file
        // This call should be completely optimized away at compile time, if it doesn't log
        template <LogLevel thisLogsLevel>
        void log(std::string_view message)
        {
            assert(logFile.is_open() && "File should always be open");

            if constexpr (static_cast<int>(thisLogsLevel) >= static_cast<int>(keptLogLevel))
            {
                logFile << levelToString(thisLogsLevel) << ": " << message << std::endl;
                logFile.flush();
            }
        }

    private:
        static constexpr std::string_view levelToString(LogLevel level)
        {
            switch (level)
            {
            case LogLevel::TRACE return "TRACE";
            case LogLevel::DEBUG return "DEBUG";
            case LogLevel::INFO return "INFO";
            case LogLevel::WARN return "WARN";
            case LogLevel::ERROR return "ERROR";
            case LogLevel::FATAL return "FATAL";
            }
        }
        std::ofstream logFile;
    };

}