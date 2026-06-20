@synopsis{Configuration for the `@memo` annotation that controls how memoized results expire.}
@description{
The `@memo` annotation caches the results of Rascal function calls to avoid redundant recomputation.
By default the cache is unbounded and never expires. Use `MemoExpireConfiguration` as the value of
`@memo` to apply an eviction policy:

* `expireAfter` — evicts an entry after the specified idle time since last access. Set exactly
  one of `seconds`, `minutes`, or `hours`.
* `maximumSize` — evicts least-recently-used entries once the cache exceeds the given number of entries.
}
@examples{
```rascal
import util::Memo;

// Evict cache entries that have not been accessed for 10 minutes
@memo=expireAfter(minutes=10)
int slowComputation(int n) = n * n;

// Keep at most 100 entries in the cache
@memo=maximumSize(100)
str label(int id) = "item_<id>";
```
}
module util::Memo

@synopsis{Defines when memoized cache entries should expire.}
@description{
Two eviction strategies are available:

* `expireAfter` — set exactly one of `seconds`, `minutes`, or `hours` (use `-1` to leave the
  others unset). The entry is evicted after that amount of idle time since its last access.
* `maximumSize` — the cache is capped at `entries` items; the least-recently-used entry is
  evicted when a new one would exceed the limit.
}
data MemoExpireConfiguration
    = expireAfter( // expires entries when <x> times has passed since last access
            // define only one of these limits
            int seconds = -1,
            int minutes = -1,
            int hours = -1
        )
   | maximumSize(int entries) // expires entries when the cache get's fuller than a certain size
   ;
        
