(ns adamrenklint.boot-test-cljs
  {:boot/export-tasks true}
  (:require [clojure.string :as string]
            [clojure.java.io :as io]
            [boot.core :as boot]
            [boot.util :as util]
            [clojure.set :refer [difference]]
            [adamrenklint.boot-eval-cljs :refer [eval-cljs]]))

(def extension-re #"\.clj[cs]$")

(defn- testable? [path]
  (re-find extension-re path))

(defn- path->ns [path]
  (-> path
      (string/replace "/" ".")
      (string/replace "_" "-")
      (string/replace extension-re "")
      symbol))

(defn- fileset->ns [fileset]
  (->> fileset
       :tree
       keys
       (filter testable?)
       (map path->ns)
       set))

(defn- namespaces->tests [fileset ns exclude-ns include exclude]
  (as-> (or ns (fileset->ns fileset)) $
    (difference $ exclude-ns)
    (filter #(re-find (or include #"-test$") (str %)) $)
    (remove #(re-find (or exclude #" ") (str %)) $)))

(defn- runner-source [tests]
  (string/join "\n"
    [`(~'ns boot-test-cljs.runner
        (:require [cljs.test :as ~'test]
                  adamrenklint.boot-test-cljs.core
                  ~@tests))
     `(~'defn ~'run-tests []
        (if ~(empty? tests)
          (println "\nNo ClojureScript tests found...")
          (do
            (println "\nRunning ClojureScript tests...")
            (test/run-tests ~@(map #(cons 'quote [%]) tests)))))]))

(boot/deftask test-cljs
  "Run ClojureScript tests in Node.js"
  [n ns NS         #{sym} "Set of namespace symbols to include, defaults to all namespaces in :source-path"
   x exclude-ns NS #{sym} "Set of namespace symbols to exclude"
   i include REGEX regex  "Filter for namespaces to include"
   e exclude REGEX regex  "Filter for namespaces to exclude"]
  (comp (boot/with-pre-wrap fileset
          (util/info "Generating ClojureScript test runner...\n")
          (let [tests  (namespaces->tests fileset ns exclude-ns include exclude)
                source (runner-source tests)
                path   "boot_test_cljs/runner.cljs"
                tmp    (boot/tmp-dir!)
                file   (io/file tmp path)]
            (doto file
              (io/make-parents)
              (spit source))
            (-> fileset (boot/add-resource tmp) boot/commit!)))
        (eval-cljs :fn 'boot-test-cljs.runner/run-tests)))
