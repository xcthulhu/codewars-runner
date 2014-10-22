(ns codewars.runners.clojure
  (:require [codewars.runners :refer [code-only full-project]]
            [codewars.name-space :refer [name-space]]
            [codewars.clojure.test]))

(defmethod code-only "clojure"
  [{:keys [:setup :code]}]
  (when (not (empty? setup)) (load-string setup))
  (load-string code))

(defmethod full-project "clojure"
  [{:keys [:setup :code :fixture]}]
  (when (not (empty? setup)) (load-string setup))
  (load-string code)
  (load-string fixture)
  (codewars.clojure.test/run-tests (name-space :clojure fixture)))
