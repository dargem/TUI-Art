cmake_minimum_required(VERSION 3.10)

project(TUI-Art-Cpp VERSION 1.0)

# Set C++ Standard
set(CMAKE_CXX_STANDARD 23)
set(CMAKE_CXX_STANDARD_REQUIRED True)

# Include directories
include_directories(include)

# Find all source files
file(GLOB_RECURSE SOURCES "src/*.cpp")

# Separate main.cpp from other sources to create a library
list(FILTER SOURCES EXCLUDE REGEX "src/main.cpp$")

# Create Library (Core Logic)
add_library(tui_lib ${SOURCES})
find_package(Threads REQUIRED)
target_link_libraries(tui_lib Threads::Threads)

# Create Main Executable
add_executable(tui_art src/main.cpp)
target_link_libraries(tui_art tui_lib)

# Compiler options
if(MSVC)
    target_compile_options(tui_lib PRIVATE /W4)
    target_compile_options(tui_art PRIVATE /W4)
else()
    target_compile_options(tui_lib PRIVATE -Wall -Wextra -Wpedantic)
    target_compile_options(tui_art PRIVATE -Wall -Wextra -Wpedantic)
endif()

# Testing Setup
enable_testing()

include(FetchContent)
FetchContent_Declare(
  googletest
  URL https://github.com/google/googletest/archive/refs/heads/main.zip
)
# For Windows: Prevent overriding the parent project's compiler/linker settings
set(gtest_force_shared_crt ON CACHE BOOL "" FORCE)
FetchContent_MakeAvailable(googletest)

include(GoogleTest)

add_executable(unit_tests tests/core/types.cpp)
target_link_libraries(unit_tests tui_lib GTest::gtest_main)

gtest_discover_tests(unit_tests)
