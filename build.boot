(def project 'adamrenklint/boot-test-cljs)
(def version "1.0.0")

(set-env!
 :source-paths #{"src"}
 :dependencies '[[adamrenklint/boot-eval-cljs "1.2.1"]
                 [org.clojure/clojurescript "1.9.946" :scope "test"]
                 [adzerk/bootlaces       "0.1.13" :scope "test"]
                 [tolitius/boot-check    "0.1.6"  :scope "test"]
                 [adamrenklint/boot-fmt  "1.3.0"  :scope "test"]
                 [adamrenklint/boot-exec "1.0.1"  :scope "test"]])

(require '[adamrenklint.boot-test-cljs :refer [test-cljs]]
         '[adzerk.bootlaces :refer :all]
         '[adamrenklint.boot-exec :refer [exec]]
         '[adamrenklint.boot-fmt :refer [fmt]]
         '[tolitius.boot-check :as check])

(bootlaces! version)

(ns-unmap 'boot.user 'test)
(ns-unmap 'boot.user 'format)

(deftask deps [])

(deftask release []
  (comp (build-jar)
        (push-release)
        (exec :cmd "git push --tags")))

(deftask check []
  (comp (check/with-yagni)
        (check/with-eastwood)
        (check/with-kibit)
        (check/with-bikeshed)))

(deftask format []
  (fmt))

(deftask test []
  (merge-env! :source-paths #{"test"})
  (test-cljs))

(task-options!
  pom {:project     project
       :version     version
       :description "Boot task to run ClojureScript tests in Node.js, with minimal configuration, proper stacktraces and correct exit code on error or failure"
       :url         "https://github.com/adamrenklint/boot-test-cljs"
       :scm         {:url "https://github.com/adamrenklint/boot-test-cljs"}
       :license     {"MIT" "https://github.com/adamrenklint/boot-test-cljs/blob/master/LICENSE"}})
