cmake_minimum_required(VERSION 3.10)

project(TUI-Art-Cpp VERSION 1.0)

# Set C++ Standard
set(CMAKE_CXX_STANDARD 20)
set(CMAKE_CXX_STANDARD_REQUIRED True)

# Include directories
include_directories(include)

# Find all source files
file(GLOB_RECURSE SOURCES "src/*.cpp")

# Create Executable
add_executable(tui_art ${SOURCES})

# Threading library for sleep and rendering loops
find_package(Threads REQUIRED)
target_link_libraries(tui_art Threads::Threads)

# Compiler warnings
if(MSVC)
    target_compile_options(tui_art PRIVATE /W4)
else()
    target_compile_options(tui_art PRIVATE -Wall -Wextra -Wpedantic)
endif()
