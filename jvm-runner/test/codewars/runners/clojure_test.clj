(ns codewars.runners.clojure-test
  (:require [clojure.test :refer :all]
            [codewars.core :refer [-main] :as core]
            [codewars.util :as util]
            [cheshire.core :as json]
            [codewars.clojure.test])
  (:import [java.util.concurrent TimeoutException]))

(deftest basic-clojure
  (testing "-main can handle a very basic clojure code and fixture"
    (with-in-str
      (json/generate-string
       {:language "clojure"
        :code "(ns foo)"
        :fixture "(ns bar)"})
      (is (= {:type :summary, :fail 0, :error 0, :pass 0, :test 0}
             (:result (-main)))))))

(deftest clojure-simple
  (testing "-main can handle a simple clojure code and fixture"
    (with-in-str
      (json/generate-string
       {:language "clojure"
        :code "(ns foo)
                   (defn wizard [] :ok)"
        :fixture "(ns bar
                    (:require [foo]
                      [clojure.test :refer :all]))
                  (deftest ok (is (= :ok (foo/wizard))))"})
      (let [test-result (-main)]
        (is (not (empty? (:stdout test-result))))
        (is (.contains (:stdout test-result) "ok"))
        (is (= {:type :summary, :fail 0, :error 0, :pass 1, :test 1}
               (:result test-result)))))))

(deftest clojure-sadpath
  (testing "-main can handle an erroneous test fixture"
    (with-in-str
      (json/generate-string
       {:language "clojure"
        :code "(ns dio)
                   (defn holy-diver [] :ride-the-tiger)"
        :fixture "(ns race.for.the.morning
                    (:require [dio]
                      [clojure.test :refer :all]))
                  (deftest oh-we-will-pray-its-all-right
                      (is (= :gotta-get-away (dio/holy-diver))))"})
        (is (= {:type :summary, :fail 1, :error 0, :pass 0, :test 1}
               (:result (-main)))))))

(deftest clojure-code-only
  (testing "-main will just run code if a fixture is not present (and *out* is being captured)"
    (with-in-str
      (json/generate-string
       {:language "clojure"
        :code "(print \"Oh no, here it comes again\")"})
      (is (= "Oh no, here it comes again"
             (:stdout (-main)))))))

(deftest clojure-err-test
  (testing "*err* is getting captured"
    (with-in-str
      (json/generate-string
       {:language "clojure"
        :code "(binding [*out* *err*] (print \"Can't remember when we came so close to love before\"))"})
      (is (= "Can't remember when we came so close to love before"
             (:stderr (-main)))))))

(deftest clojure-System.out-test
  (testing "System.out is getting captured"
    (with-in-str
      (json/generate-string
       {:language "clojure"
        :code "(.print (System/out) \"Again and again, again and again, and AGAINNN!\")"})
      (is (= "Again and again, again and again, and AGAINNN!"
             (:stdout (-main)))))))

(deftest clojure-System.err-test
  (testing "System.err is getting captured"
    (with-in-str
      (json/generate-string
       {:language "clojure"
        :code "(.print (System/err) \"Phantom figure free forever, NEON KNIGHTS!!!!!\")"})
      (is (= "Phantom figure free forever, NEON KNIGHTS!!!!!"
             (:stderr (-main)))))))


(deftest clojure-code-and-setup
  (testing "-main will just run code and read correctly from setup code"
    (with-in-str
      (json/generate-string
       {:language "clojure"
        :setup "(ns heaven.and.hell) (defn first-track [] (print \"So it's on and on and on, oh it's on and on and on\"))"
        :code "(require 'heaven.and.hell) (heaven.and.hell/first-track)"})
      (is (= "So it's on and on and on, oh it's on and on and on"
             (:stdout (-main))))))

  (testing "-main will just run code and read correctly from setup code"
    (with-in-str
      (json/generate-string
       {:language "clojure"
        :setup "(ns heaven.and.hell) (defn first-track [] (print \"In heaven and hell\"))"
        :code "(require 'heaven.and.hell) (heaven.and.hell/first-track)"})
      (is (= "In heaven and hell"
             (:stdout (-main)))))))

(deftest clojure-code-fixture-and-setup
  (testing "-main can handle a code, fixture, and setup code in clojure"
    (with-in-str
      (json/generate-string
       {:language "clojure"
        :setup "(ns fear.of.the.dark) (defn lyric [] \"I have a constant fear that someone's always near'\")"
        :code "(ns maiden-greatest-hits (:require [fear.of.the.dark :refer [lyric]])) (defn fear-of-the-dark [] (lyric))"
        :fixture "(ns maiden-test (:require
                      [maiden-greatest-hits]
                      [fear.of.the.dark :refer [lyric]]
                      [clojure.test :refer :all]))
                  (deftest maiden-rocks (is (= (maiden-greatest-hits/fear-of-the-dark) (fear.of.the.dark/lyric))))"})
      (is (= {:type :summary, :fail 0, :error 0, :pass 1, :test 1}
             (:result (-main)))))))

(deftest clojure-sad-path-0
  (testing "tests can fail"
    (with-in-str
      (json/generate-string
       {:language "clojure"
        :code "(ns empty.namespace)"
        :fixture "(ns clojure.test.example (:use clojure.test))
                  (deftest sad-path (testing \"won't work\" (is (= 2 1) \"bad math\")))"})
      (let [{:keys [result stdout]} (-main)]
        (is (not (nil? result)))
        (is (.contains stdout "FAIL"))))))

(deftest clojure-exceptions
  (testing "Exception are handled gracefully"
    (with-in-str
      (json/generate-string
       {:language "clojure"
        :code "(Integer. 1.0)"})
      (let [error-message (:stderr (-main))]
        (is (not (nil? error-message)))
        (is (.contains error-message "java.lang.IllegalArgumentException: No matching ctor found for class java.lang.Integer"))))))

(deftest clojure-timeout
  (testing "-main will timeout if a kata code takes too long"
    (with-in-str
      (json/generate-string
       {:language "clojure"
        :code "(println \"...Sleeping deeply...\")
                   (Thread/sleep 50000)"})
      (with-redefs [util/timeout 10]
        (let [result (-main)]
          (is (not (empty? (:stderr result))))
          (is (.contains (:stderr result) "TimeoutException")))))))
