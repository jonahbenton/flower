(ns flower.test.perf
  (:require
    [clojure.test :refer :all]
    [flower :refer [defflow]]))

(defn make-counting-flow-map
  "returns a flow map with size steps, each of which increments :key in context"
  [key size]
  (loop [flow-map {}
         n size
         start :start
         keys [key] ]
    (if (= 0 n)
      (assoc flow-map start :end)
      (let [f (fn [context] (update-in context keys (fnil inc 0)))]
        (recur
          (assoc flow-map start f)
          (dec n)
          f
          keys)))))

(defflow count-100 (make-counting-flow-map :key 100))

(deftest count-100-test
  (testing "counting to 100"
    (is (= (count-100 {}) {:key 100}))))

