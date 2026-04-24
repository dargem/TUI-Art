#pragma once

#include "ecs/concepts.hpp"
#include "ecs/query.hpp"

namespace ECS {

// /**
//  * @brief Checks if the first query is dependent on the second query
//  *
//  * @tparam Query the first query to check if dependent on OtherQuery
//  * @tparam OtherQuery
//  */
// template <typename Query, typename OtherQuery>
//     requires(HasQueryInterface<Query> && HasQueryInterface<OtherQuery>)
// consteval bool isDependent() {}

/**
 * @brief A node which is a query wrapper
 *
 * @tparam Query, the query this wraps around
 * @tparam OtherQueryPack, all other queries
 * @tparam Index, the index this query exists at
 */
template <typename Query, typename OtherQueryPack, unsigned Index>
    requires IsTypePack<OtherQueryPack>
class QueryNode {
   public:
    explicit QueryNode(Query&& query) : query(std::forward(query)) {}

    // using DependentNodes = decltype(std::tuple_cat(std::conditional_t<>));

   private:
    Query query;
};

/**
 * @brief Scheduler used to execute queries
 */
template <typename... Qs>
    requires(HasQueryInterface<Qs> && ...)
class QueryRunner {
   public:
    explicit QueryRunner(Qs&&... qs) : queries(std::make_tuple(std::forward<Qs>(qs)...)) {}

   private:
    std::tuple<Qs...> queries;
};

}  // namespace ECS