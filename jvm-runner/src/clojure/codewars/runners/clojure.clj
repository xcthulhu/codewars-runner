(ns codewars.runners.clojure
  (:require [codewars.runners :refer [code-only full-project]]
            [codewars.util :as util]
            [codewars.clojure.test]
            [clojure.java.io :as io])
  (:import [codewars.java TempDir])
  (:refer-clojure :exclude (add-classpath)))

(defn- add-classpath
  "Add a url path to the system class loader"
  [new-classpath]
  (let [field (aget (.getDeclaredFields java.net.URLClassLoader) 0)]
    (.setAccessible field true)
    (let [ucp (.get field (ClassLoader/getSystemClassLoader))]
      (.addURL ucp (io/as-url new-classpath)))))

(defmethod code-only "clojure"
  [{:keys [:setup :code]}]
  (let [dir (TempDir/create "clojure")
        code-file (io/file dir "code.clj")]
    (when (not (empty? setup)) (util/write-code! "clojure" dir setup))
    (spit code-file code)
    (add-classpath dir)
    (load-file (str code-file))))

(defmethod full-project "clojure"
  [{:keys [:setup :code :fixture]}]
  (let [dir (TempDir/create "clojure")
        {fixture-ns :class-name}
        (util/write-code! "clojure" dir fixture)]
    (when (not (empty? setup)) (util/write-code! "clojure" dir setup))
    (util/write-code! "clojure" dir code)
    (add-classpath dir)
    (require fixture-ns)
    (codewars.clojure.test/run-tests fixture-ns)))
