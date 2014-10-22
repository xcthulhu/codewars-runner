(ns codewars.runners.java
  (:require [codewars.runners :refer [code-only full-project]]
            [codewars.clojure.test]
            [clojure.java.io :as io]
            [codewars.util :refer [write-code!]])
  (:import [codewars.java TempDir]
           [java.net URLClassLoader]
           [javax.tools ToolProvider]
           [java.io ByteArrayOutputStream]
           [org.junit.runner JUnitCore]
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
    (codewars.clojure.test/time
     (.run runner (into-array [fixture-class])))))

(defn- file-names
  "Filter a sequence of files writen by write-code! and output their names"
  [& files]
  (for [{:keys [:file-name]} files
        :when (not (nil? file-name))]
    file-name))

(defmethod code-only "java"
  [{:keys [:setup :code]}]
  (let [dir (TempDir/create "java")
        setup (when (not (empty? setup)) (write-code! "java" dir setup))
        code (write-code! "java" dir code)
        files (file-names setup code)]
    (apply compile! files)
    (-> code
        :class-name
        (->> (load-class dir))
        (.getDeclaredMethod "main" (into-array [(Class/forName "[Ljava.lang.String;")]))
        (doto (.setAccessible true))
        (.invoke nil (into-array [(into-array String [])])))))

(defmethod full-project "java"
  [{:keys [:fixture :setup :code]}]
  (let [dir (TempDir/create "java")
        fixture (write-code! "java" dir fixture)
        setup (when (not (empty? setup)) (write-code! "java" dir setup))
        code (write-code! "java" dir code)
        files (file-names fixture setup code)]
    (apply compile! files)
    (->> fixture :class-name (load-class dir) run-junit-tests)))
