(ns adamrenklint.boot-test-cljs.core
  (:require [cljs.test :as test]
            [cljs.nodejs :as node]))

(enable-console-print!)

(def orig-error-report (get-method test/report [::test/default :error]))

(defmethod test/report [::test/default :error]
  [m]
  (orig-error-report m)
  (println "\nSTACKTRACE:" (.-stack (:actual m))))

(defmethod test/report [::test/default :end-run-tests]
  [report]
  (when-not (and (= (:fail report) 0) (= (:error report) 0))
    (.exit node/process 1)))
