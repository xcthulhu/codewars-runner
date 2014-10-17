(ns codewars.zmq-test
  (:require [clojure.test :refer :all]
            [zeromq.zmq :as zmq]
            [cheshire.core :as json]
            [codewars.zmq]
            [codewars.core :refer [-main]]
            ))

(deftest server-check
  (let [server (future-call codewars.zmq/listen)]
    (with-open
        [req (doto (zmq/socket (zmq/zcontext) :req)
               (zmq/connect codewars.zmq/addr))]
      (testing "zmq can respond to ping"
        (zmq/send-str req (json/generate-string "PING"))
        (is (= (json/parse-string (String. (zmq/receive req)))
               {"type"   "status"
                "status" "ready"
                "message" "PING"})))
      (testing "zmq can handle a request"
        (zmq/send-str req
         (json/generate-string
          {:language "clojure"
           :code "(println \"blorg\")"}))
        (is (= (json/parse-string (String. (zmq/receive req)))
               {"type" "response",
                "result" nil,
                "stderr" "",
                "stdout" "blorg\n"})))
      (is (= {:result nil, :stderr "", :stdout "blorg\n"} @server)))))

(deftest main-check
  (let [main (future-call -main)]
    (with-open
        [req (doto (zmq/socket (zmq/zcontext) :req)
               (zmq/connect codewars.zmq/addr))]
      (testing "main wraps a ping"
        (zmq/send-str req (json/generate-string "PONG"))
        (is (= (json/parse-string (String. (zmq/receive req)))
               {"type"   "status"
                "status" "ready"
                "message" "PONG"})))
      (testing "main responds to a request"
        (zmq/send-str req
                      (json/generate-string
                       {:language "java"
                        :code "class AwesomeSauce { static int main(String [] args) {return 1;} }"}))
        (is (= (json/parse-string (String. (zmq/receive req)))
               {"type" "response",
                "result" 1,
                "stderr" "",
                "stdout" ""})))
      (is (= {:result 1, :stderr "", :stdout ""} @main)))))
