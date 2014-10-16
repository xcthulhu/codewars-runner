(ns codewars.runners.groovy
  (:require [codewars.runners :refer [code-only]])
  (:import [groovy.lang GroovyShell]))

(defmethod code-only "groovy"
  [{:keys [:setup :code]}]
    (when (not (nil? setup)) (throw (Exception. "Setup code is not supported")))
    (.evaluate (GroovyShell.) code))
