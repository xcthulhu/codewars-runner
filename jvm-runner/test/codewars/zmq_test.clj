(ns codewars.zmq-test
  (:require [clojure.test :refer :all]
            [zeromq.zmq :as zmq]
            [cheshire.core :as json]
            [codewars.zmq]))

(deftest rep-req-test
  (with-open [rep (doto (zmq/socket (zmq/zcontext) :rep)
                    (zmq/connect codewars.zmq/addr))
              req (doto (zmq/socket (zmq/zcontext) :req)
                    (zmq/bind codewars.zmq/addr))]
    (zmq/send-str req "hello")
    (is (= "hello" (String. (zmq/receive rep))))
    (zmq/send-str rep "Bonjour")
    (is (= "Bonjour" (String. (zmq/receive req))))))

;;(deftest server-check
;;  (let [server (future-call codewars.zmq/server)]
;;    (with-open
;;        [req (doto (zmq/socket (zmq/zcontext) :req)
;;               (zmq/connect codewars.zmq/addr))]
;;      (zmq/send-str req (json/generate-string "PING"))
;;      (is (= (json/parse-string (String. (zmq/receive req)))
;;               {"type"   "JVM status"
;;                "status" "ready"}))
;;      (zmq/send-str req (json/generate-string "PING")))))
