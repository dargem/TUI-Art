#pragma once

#include "ecs/concepts.hpp"
#include "ecs/query.hpp"

namespace ECS {

template <typename Query, typename OtherQueryPack>
    requires TypePackType<OtherQueryPack>
class QueryNode {
   public:
    explicit QueryRunner(Query&& query) : query(std::forward(query)) {}

        

   private:
    Query query;
    
};

/**
 * @brief Scheduler used to execute queries
 */
template <typename... Qs>
class QueryRunner {
   public:
    explicit QueryRunner(Qs&&... qs) : queries(std::make_tuple(std::forward<Qs>(qs)...)) {}

   private:
    std::tuple<Qs...> queries;
};

}  // namespace ECS