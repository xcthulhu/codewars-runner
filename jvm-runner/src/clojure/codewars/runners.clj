(ns codewars.runners
  (:require [codewars.util :refer [wrap-result with-timeout timeout]]))

(defmulti solution-only :language)
(defmulti full-project :language)
(defmethod solution-only :default
  [{:keys [:language]}]
  (throw (IllegalArgumentException.
          (format "Language %s is not implemented"
                  (pr-str language)))))

(defmethod full-project :default
  [{:keys [:language]}]
  (throw (IllegalArgumentException.
          (format "Language %s is not implemented"
                  (pr-str language)))))

(defn run
  "Run solution code or a test-fixture."
  [opts]
  (wrap-result
   (with-timeout timeout
     (if (contains? opts :fixture)
       (full-project opts)
       (solution-only opts)))))
