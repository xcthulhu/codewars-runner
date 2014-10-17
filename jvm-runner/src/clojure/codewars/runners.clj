(ns codewars.runners
  (:require [codewars.util :refer [wrap-result with-timeout timeout]]))

(defmulti code-only :language)
(defmulti full-project :language)
(defmethod code-only :default
  [{:keys [:language]}]
  (throw (IllegalArgumentException.
          (format "Language %s is not implemented"
                  (pr-str language)))))

(defmethod full-project :default
  [{:keys [:language]}]
  (throw (IllegalArgumentException.
          (format "Language %s is not implemented"
                  (pr-str language)))))

(defn- print-result [{:keys [:stdout :stderr] :as result}]
  (print stdout)
  (binding [*out* *err*] (print stderr))
  result)

(defn run
  "Run code or a test-fixture."
  [opts]
  (->> (if (contains? opts :fixture)
         (full-project opts)
         (code-only opts))
       (with-timeout timeout)
       wrap-result
       print-result
       ))
