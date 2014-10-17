(ns codewars.core
  (:require
   [clojure.core.async :refer [go alts!!]]
   [codewars.stdin]
   [codewars.zmq])
  (:gen-class))

(defn -main
  "Multiplexes stdin and ZMQ, outputs whichever one it recieves first"
  [& _]
  (first (alts!! [(go (codewars.stdin/listen))
                  (go (codewars.zmq/listen))])))
