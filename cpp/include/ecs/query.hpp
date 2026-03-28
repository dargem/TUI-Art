#pragma once

#include <concepts>
#include <functional>
#include <type_traits>

#include "ecs/components.hpp"

namespace ECS {

template <typename T>
struct callable_traits;

// Specialization for function pointers: Ret(*)(Args...)
template <typename Ret, typename... Args>
struct callable_traits<Ret (*)(Args...)> {
    using signature = Ret(Args...);
    using returnType = Ret;
    using args = Args...;
    using argsTuple = std::tuple<Args...>;  // Alternative access
};

// Specialization for lambdas/functors
template <typename Ret, typename Class, typename... Args>
struct callable_traits<Ret (Class::*)(Args...)> {
    using signature = Ret(Args...);
    using returnType = Ret;
    using args = Args...;
    using argsTuple = std::tuple<Args...>;  // Alternative access
};

template <typename Signature>
class Query;

template <typename R, Component... Args>
class Query<R(Args...)> {
   public:
    template <typename F>
    Query(F&& operationFunction) : operation(std::forward<F>(operationFunction)) {}

    void operator()() {
        // Runs the query
    }

   private:
    void (*operation)(Args...);
};

template <Component... Cs>
class Query {
   public:
    Query(std::function<void(Cs...)> process) : process{process} {}

   private:
    std::function<void(Cs...)> process;
};

}  // namespace ECS