# boot-test-cljs

Boot task to run ClojureScript tests in Node.js, with minimal configuration, proper stacktraces and correct exit code on error or failure

[![Clojars Project](https://img.shields.io/clojars/v/adamrenklint/boot-test-cljs.svg?style=flat-square
)](https://clojars.org/adamrenklint/boot-test-cljs) [![CircleCI](https://img.shields.io/circleci/project/github/adamrenklint/boot-test-cljs.svg?style=flat-square
)](https://circleci.com/gh/adamrenklint/boot-test-cljs)

```clojure
[adamrenklint/boot-test-cljs "1.1.0"] ;; latest release
```

## Usage

Add `boot-test-cljs` to your `build.boot` dependencies and require the `test-cljs` task:

```clojure
(set-env! :dependencies '[[adamrenklint/boot-test-cljs "1.1.0"]])
(require '[adamrenklint.boot-test-cljs :refer [test-cljs]])
```

Now you can use the task to run all ClojureScript tests.

```
(deftask test []
  (merge-env! :source-paths #{"test"})
  (test-cljs))
```

## Options

```
 -n, --ns NS                 #{sym}  Set of namespace symbols to include, defaults to all namespaces in :source-path
 -x, --exclude-ns NS         #{sym}  Set of namespace symbols to exclude
 -i, --include REGEX         regex   Filter for namespaces to include
 -e, --exclude REGEX         regex   Filter for namespaces to exclude
 -c, --compiler-options OPTS edn     Options to pass to the ClojureScript compiler
```

## License

Copyright (c) 2017 [Adam Renklint](http://adamrenklint.com)

Distributed under the [MIT license](https://github.com/adamrenklint/boot-test-cljs/blob/master/LICENSE)
