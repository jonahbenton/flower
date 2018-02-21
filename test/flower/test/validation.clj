(ns flower.test.validation
  (:require
    [clojure.test :refer :all]
    [flower :as f]))


(deftest validation-test
  (testing "validation"
    (is (not= {} (f/flow-validation { :start identity identity :start })))
    (is (not= {} (f/flow-validation { :start identity identity identity })))
    (is (not= {} (f/flow-validation { :end identity identity :end })))
    (is (not= {} (f/flow-validation { :end identity identity :end })))
;    (is (not= {} (f/flow-validation [ :start identity identity :end ])))
    ))

