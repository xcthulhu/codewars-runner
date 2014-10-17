(ns codewars.stdin
  (:require [cheshire.core :as json]
            [codewars.runners :refer [run]]
            [codewars.runners.groovy]
            [codewars.runners.clojure]
            [codewars.runners.java])
  (:gen-class))

(defn listen
  "Listens to *in* for a JSON message, parses it and calls the appropriate runner"
  [& _]
  (let [input (json/parse-stream *in* true)]
    (run input)))