(ns codewars.runners.java
  (:require [codewars.runners :refer [code-only full-project]]
            [codewars.clojure.test]
            [clojure.java.io :as io]
            [codewars.util :as util])
  (:import [codewars.java TempDir]
           [java.net URLClassLoader]
           [javax.tools ToolProvider]
           [java.io ByteArrayOutputStream]
           [org.junit.runner JUnitCore Result]
           [codewars.java CwRunListener]))

(defn- compile!
  "Compile files using the java compiler"
  [& files]
  (let [out-stream (ByteArrayOutputStream.)
        err-stream (ByteArrayOutputStream.)
        compilation-result
        (. (ToolProvider/getSystemJavaCompiler)
           run nil out-stream err-stream
           (into-array (map str files)))]
    (-> out-stream str print)
    (if (zero? compilation-result)
      0
      (throw (RuntimeException. (str err-stream))))))

(defn- load-class
  "Load a java class in a specified directory"
  [dir class-name]
  (let [class-loader
        (URLClassLoader/newInstance
         (into-array [(io/as-url dir)]))]
    (Class/forName (name class-name) true class-loader)))

(defn- run-junit-tests
  "Run a JUnit test using the Codewars Formatter for a given fixture-class"
  [fixture-class]
  (let [runner (JUnitCore.)]
    (.addListener runner (CwRunListener.))
    (.run runner (into-array [fixture-class]))))

(defn- file-names
  "Filter a sequence of files writen by write-code! and output their names"
  [& files]
  (for [{:keys [:file-name]} files
        :when (not (nil? file-name))]
    file-name))

(defmethod code-only "java"
  [{:keys [:setup :code]}]
  (let [dir (TempDir/create "java")
        setup (when (not (empty? setup)) (util/write-code! "java" dir setup))
        code (util/write-code! "java" dir code)
        files (file-names setup code)]
    (try
      (apply compile! files)
      (-> code
          :class-name
          (->> (load-class dir))
          (.getDeclaredMethod "main" (into-array [(Class/forName "[Ljava.lang.String;")]))
          (doto (.setAccessible true))
          (.invoke nil (into-array [(into-array String [])])))
      (finally (TempDir/delete dir)))))

(defn junit-result-to-map
  [^Result result]
  {:failures (.getFailureCount result)
   :ignore (.getIgnoreCount result)
   :runs (.getRunCount result)
   :run-time (.getRunTime result)
   :successful? (.wasSuccessful result)})

(defmethod full-project "java"
  [{:keys [:fixture :setup :code]}]
  (let [dir (TempDir/create "java")
        fixture (util/write-code! "java" dir fixture)
        setup (when (not (empty? setup)) (util/write-code! "java" dir setup))
        code (util/write-code! "java" dir code)
        files (file-names fixture setup code)]
    (try
      (apply compile! files)
      (->> fixture :class-name (load-class dir) run-junit-tests junit-result-to-map)
      (finally (TempDir/delete dir)))))
