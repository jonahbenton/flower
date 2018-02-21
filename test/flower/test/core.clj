(ns flower.test.core
  (:require
    [clojure.test :refer :all]
    [flower :refer [defflow]]))

; logback.configurationFile
(defn is-valid-request?
  [context]
  true)

(defn perform-request
  [context]
  (assoc context :response {:status 200 :body "Hello World!"} ))

(defn performance-successful?
  [context]
  true)

(defn return-invalid
  [context]
  (assoc context :response {:status 400 :body "Invalid!"} ))

(defn return-error
  [context]
  (assoc context :response {:status 500 :body "Error!"} ))

(defn return-success
  [context]
  (assoc context :response {:status 200 :body "Success!"} ))

(defflow simplest {:start identity identity :end})
;
(defflow basic-web-response
  {:start is-valid-request?
   is-valid-request? { true perform-request false return-invalid }
   perform-request performance-successful?
   performance-successful? { true return-success false return-error }
   return-invalid :end
   return-success :end
   return-error :end }  )

(deftest basic-test
  (testing "basic flow"
    (is (= (basic-web-response {}) {:response {:status 200 :body "Success!"}}))))

