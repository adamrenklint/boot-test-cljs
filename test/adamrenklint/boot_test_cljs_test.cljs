(ns adamrenklint.boot-test-cljs-test
  (:require [cljs.test :refer-macros [deftest is]]))

(deftest faux-test
  (is (= 42 (+ 40 2))))
