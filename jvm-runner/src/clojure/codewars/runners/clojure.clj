(ns codewars.runners.clojure
  (:require [codewars.runners :refer [code-only full-project]]
            [codewars.util :as util]
            [codewars.clojure.test]
            [clojure.java.io :as io])
  (:import [codewars.java TempDir])
  (:refer-clojure :exclude (add-classpath)))

(defn- add-classpath
  "Add a URL path to the system class loader"
  [new-classpath]
  (let [field (aget (.getDeclaredFields java.net.URLClassLoader) 0)]
    (.setAccessible field true)
    (let [ucp (.get field (ClassLoader/getSystemClassLoader))]
      (.addURL ucp (io/as-url new-classpath)))))

(defmethod code-only "clojure"
  [{:keys [:setup :code]}]
  (when (not (empty? setup))
    (load-string setup))
  (load-string code))

(defmethod full-project "clojure"
  [{:keys [:setup :code :fixture]}]
  (let [dir (TempDir/create "clojure")
        {fixture-ns :class-name}
        (util/write-code! "clojure" dir fixture)]
    (try
      (when (not (empty? setup)) (util/write-code! "clojure" dir setup))
      (util/write-code! "clojure" dir code)
      (add-classpath dir)
      (require fixture-ns :reload-all)
      (codewars.clojure.test/run-tests fixture-ns)
      (finally (TempDir/delete dir)))))
