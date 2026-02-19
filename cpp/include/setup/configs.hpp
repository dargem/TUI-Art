#pragma once

#include "utils/logger.hpp"

namespace Setup {
// Log level of the program
inline constexpr utils::LogLevel APP_LOG_LEVEL = utils::LogLevel::INFO;
// Default log file of the program
inline constexpr std::string_view LOG_LOCATION{"log.txt"};
// Concrete logger object using the preset
using AppLogger = utils::Logger<APP_LOG_LEVEL>;
}  // namespace Setup