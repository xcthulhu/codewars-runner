(ns codewars.runners.groovy-test
  (:require [clojure.test :refer :all]
            [cheshire.core :as json]
            [codewars.core :refer [-main] :as core]))

(deftest groovy-solution-only
  (testing "-main can handle a groovy solution with no fixture"
    (with-in-str
      (json/generate-string
       {:language "groovy"
        :solution "1 + 1"})
      (is (= 2 (:result (-main)))))))

(deftest groovy-java-out
  (testing "-main can handle a groovy solution with no setup code but no fixture"
    (with-in-str
      (json/generate-string
       {:language "groovy"
        :solution "print 'Hello Groovy!'"})
      (is (= "Hello Groovy!" (:stdout (-main)))))))
