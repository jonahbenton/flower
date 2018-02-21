(ns flower.test.multimethods
  (:require
    [clojure.test :refer :all]
    [flower :refer [defflow]]))

(defmulti is-valid-request? :flow-name)
(defmulti perform-request :flow-name)
(defmulti return-invalid :flow-name)
(defmulti performance-successful? :flow-name)
(defmulti return-success :flow-name)
(defmulti return-error :flow-name)


(defmethod is-valid-request? :default [context] true)
(defmethod perform-request :default [context] context)
(defmethod return-invalid :default [context] context)
(defmethod performance-successful? :default [context] true)
(defmethod return-success :default [context] context)
(defmethod return-error :default [context] context)

(def multimethod-flow-map
  { :start is-valid-request?
    is-valid-request? { true perform-request false return-invalid }
    perform-request performance-successful?
    performance-successful? { true return-success false return-error }
    return-invalid :end
    return-success :end
    return-error :end }  )

(defflow multimethod-flow multimethod-flow-map)

(deftest multimethod-test
  (testing "multimethod flow"
    (is (= (multimethod-flow {:flow-name :test-flow})  {:flow-name :test-flow}))))


