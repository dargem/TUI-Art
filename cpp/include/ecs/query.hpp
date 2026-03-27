#pragma once

#include <functional>

#include "ecs/components.hpp"

namespace ECS {

template <Component... Cs>
class Query {
   public:
    Query(std::function<void(Cs...)> process) : process{process} {}

   private:
    std::function<void(Cs...)> process;
};

}  // namespace ECS