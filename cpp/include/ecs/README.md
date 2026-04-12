A custom ECS that relies heavily on variadic templates, concepts and views.
All entity types must be defined at compile time and an entity is templated with its components.
This means entities cannot be given/lose components so its fun but probably a bad idea.
Queries are given to a query runner at compile time also, where a query is templated with a function.
The query runner deduces what the query modifies, ie non const reference is a write.
It does a topological sort at compile time and flattens it out to run queries (a system) in parallel.
A side effect is that queries cannot be modified at runtime,
is an interesting idea for stuff like physics simulations where you probably don't need this runtime flexibility.
The overhead removed probably isn't major though so I doubt its much of an improvement.
