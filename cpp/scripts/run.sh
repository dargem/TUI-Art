# Fail on any errors
set -e

# Go a directory up
cd "$(dirname "$0")/.."

# Create and enter build
mkdir -p build
cd build

# Compile
cmake .. && make

# Run
./tui_art