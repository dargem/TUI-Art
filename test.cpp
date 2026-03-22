#include <iostream>

// The template declaration lists both parameter packs
template <typename... T1_types, typename... T2_types>
void process_multiple_packs(T1_types... args1, T2_types... args2) {
    std::cout << "First pack size: " << sizeof...(T1_types) << std::endl;
    std::cout << "Second pack size: " << sizeof...(T2_types) << std::endl;

    // You can process the packs using techniques like fold expressions (C++17) or recursion
    // For example, printing the elements (requires C++17 fold expression for direct use here)
    // ((std::cout << args1 << " "), ...);
    // ((std::cout << args2 << " "), ...);
    // std::cout << std::endl;
}

int main() {
    // Calling the function with two distinct sets of arguments.
    // The compiler deduces the types for T1_types and T2_types based on the call.
    process_multiple_packs(1, 2.5, 'a', "hello", true);

    // Another example
    process_multiple_packs(10, 20);

    return 0;
}
