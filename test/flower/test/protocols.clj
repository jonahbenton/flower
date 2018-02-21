(ns flower.test.protocols
  (:require
    [clojure.test :refer :all]
    [flower :refer [defflow]]))

(defprotocol ProtocolTest
  ""
  (is-valid-request? [self] "")
  (perform-request [self] "")
  (return-invalid [self] "")
  (performance-successful? [self] "")
  (return-success [self] "")
  (return-error [self] ""))

(defrecord RecordTest [request response]
  ProtocolTest
    (is-valid-request?
      [self]
      true )
    (perform-request
      [self]
      self)
    (return-invalid
      [self]
      self)
    (performance-successful?
      [self]
      true)
    (return-success
      [self]
      self)
    (return-error
      [self]
      self))

(def protocol-flow-map
  { :start is-valid-request?
    is-valid-request? { true perform-request false return-invalid }
    perform-request performance-successful?
    performance-successful? { true return-success false return-error }
    return-invalid :end
    return-success :end
    return-error :end }  )

(defflow protocol-flow protocol-flow-map)

(deftest protocol-test
  (testing "protocol flow"
    (is (= (protocol-flow (->RecordTest {}  {} )) (->RecordTest {} {})))))

