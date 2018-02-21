(ns flower.test.webmachine
  (:require
    [clojure.test :refer :all]
    [flower :refer [defflow]]))

(def context {
  :request {
     :server-port 80
     :server-name ""
     :remote-addr ""
     :uri ""
     :query-string ""
     :scheme :http
     :request-method :get
     :protocol ""
     :headers {
        "host" ""
        "content-type" ""
        "content-length" "" }
     :body ""
     :params {
        :appVersion "1"
        :password "",
        :emailAddress "",
        :appId "1"
        :uuid "1"
        } }
  :response {
     :status 500
     :headers { "Content-Type" "text/plain" }
     :body "error" }
  :bindings {}
  :servlet-request {}
  :servlet-response {}
  :servlet-config {}
  :servlet {}
  })


(defn service-available? [context] true)
(defn known-method? [context] true)
(defn uri-too-long? [context] false)
(defn method-allowed? [context] true)
(defn malformed? [context] false)
(defn authorized? [context] true)
(defn allowed? [context] true)
(defn valid-content-header? [context] true)
(defn known-content-type? [context] true)
(defn valid-entity-length? [context] true)
(defn is-options? [context] false)
(defn accept-exists? [context] true)
(defn media-type-available? [context] true)
(defn accept-language-exists? [context] true)
(defn language-available? [context] true)
(defn accept-charset-exists? [context] true)
(defn charset-available? [context] true)
(defn accept-encoding-exists? [context] true)
(defn encoding-available? [context] true)
(defn exists? [context] true)
(defn if-match-exists? [context] true)
(defn if-match-star-exists-for-missing? [context] false)
(defn if-match-star? [context] false)
(defn if-unmodified-since-exists? [context] true)
(defn method-put? [context] false)
(defn etag-matches-for-if-match? [context] true)
(defn if-unmodified-since-valid-date? [context] true)
(defn if-none-match-exists? [context] true)
(defn put-to-different-url? [context] false)
(defn existed? [context] false)
(defn modified-since? [context] true)
(defn if-none-match-star? [context] false)
(defn if-modified-since-exists? [context] true)
(defn can-put-to-missing? [context] true)
(defn moved-permanently? [context] true)
(defn post-to-missing? [context] true)
(defn method-delete? [context] false)
(defn if-none-match? [context] false)
(defn etag-matches-for-if-none? [context] true)
(defn if-modified-since-valid-date? [context] false)
(defn conflict? [context] false)
(defn moved-temporarily? [context] false)
(defn delete! [context] context)
(defn post-to-existing? [context] true)
(defn post-to-gone? [context] true)
(defn can-post-to-gone? [context] true)
(defn post! [context] context)
(defn put! [context] context)
(defn delete-enacted? [context] true)
(defn new? [context] true)
(defn post-redirect? [context] true)
(defn put-to-existing? [context] true)
(defn multiple-representations? [context] false)
(defn respond-with-entity? [context] true)


(defn ok-200 [context] context)
(defn created-201 [context] context)
(defn options-201 [context] context)
(defn accepted-202 [context] context)

(defn moved-permanently-301 [context]
  (assoc context
    :response { :status 301
               :headers { "Content-type" "text/plain"
                         "Location" "http://" }
               :body "Moved permanently."}))

(defn moved-temporarily-302 [context]
  (assoc context
    :response { :status 302
               :headers { "Content-type" "text/plain"
                         "Location" "http://" }
               :body "Moved temporarily."}))

(defn see-other-303 [context]
  (assoc context
    :response { :status 303
               :headers { "Content-type" "text/plain"
                         "Location" "http://" }
               :body "See other."}))
(defn not-modified-304 [context]
  (assoc context
    :response { :status 304
               :headers { "Content-type" "text/plain" }
               :body "Not modified."}))

(defn moved-temporarily-307 [context]
  (assoc context
    :response { :status 307
               :headers { "Content-type" "text/plain"
                         "Location" "http://" }
               :body "Moved temporarily."}))

(defn multiple-representations-310 [context]
  (assoc context
    :response { :status 310
               :headers { "Content-type" "text/plain" }
               :body "Multiple representations."}))


(defn malformed-400 [context]
  (assoc context
    :response { :status 400
               :headers { "Content-type" "text/plain" }
               :body "Malformed request."}))
(defn unauthorized-401 [context]
  (assoc context
    :response { :status 401
               :headers { "Content-type" "text/plain" }
               :body "Unauthorized."}))

(defn forbidden-403 [context]
  (assoc context
    :response { :status 403
               :headers { "Content-type" "text/plain" }
               :body "Forbidden."}))

(defn not-found-404 [context]
  (assoc context
    :response { :status 404
               :headers { "Content-type" "text/plain" }
               :body "Not found."}))

(defn method-not-allowed-405 [context]
  (assoc context
    :response { :status 405
               :headers { "Content-type" "text/plain" }
               :body "Method not allowed."}))

(defn not-acceptable-406 [context]
  (assoc context
    :response { :status 406
               :headers { "Content-type" "text/plain" }
               :body "Not acceptable."}))

(defn conflict-409 [context]
  (assoc context
    :response { :status 409
               :headers { "Content-type" "text/plain" }
               :body "Conflict."}))

(defn gone-410 [context]
  (assoc context
    :response { :status 410
               :headers { "Content-type" "text/plain" }
               :body "Gone."}))

(defn precondition-failed-412 [context]
  (assoc context
    :response { :status 412
               :headers { "Content-type" "text/plain" }
               :body "Precondition failed."}))

(defn request-entity-too-large-413 [context]
  (assoc context
    :response { :status 413
               :headers { "Content-type" "text/plain" }
               :body "Request entity too large."}))

(defn uri-too-long-414 [context]
  (assoc context
    :response { :status 414
               :headers { "Content-type" "text/plain" }
               :body "URI too long."}))

(defn unsupported-media-type-415 [context]
  (assoc context
    :response { :status 415
               :headers { "Content-type" "text/plain" }
               :body "Unsupported media type."}))

(defn not-implemented-501 [context]
  (assoc context
    :response { :status 501
               :headers { "Content-type" "text/plain" }
               :body "Not implemented."}))

(defn unknown-method-501 [context]
  (assoc context
    :response { :status 501
               :headers { "Content-type" "text/plain" }
               :body "Unknown method."}))

(defn service-not-available-503 [context]
  (assoc context
    :response { :status 503
               :headers { "Content-type" "text/plain" }
               :body "Service not available."}))


(def webmachine
         {:start                            service-available?
          service-available?                {true known-method?
                                             false service-not-available-503}
          known-method?                     {true uri-too-long?
                                             false unknown-method-501}
          uri-too-long?                     {true uri-too-long-414
                                             false method-allowed?}
          method-allowed?                   {true malformed?
                                             false method-not-allowed-405}
          malformed?                        {true malformed-400
                                             false authorized?}
          authorized?                       {true allowed?
                                             false unauthorized-401}
          allowed?                          {true valid-content-header?
                                             false forbidden-403}
          valid-content-header?             {true known-content-type?
                                             false not-implemented-501 }
          known-content-type?               {true valid-entity-length?
                                             false unsupported-media-type-415}
          valid-entity-length?              {true is-options?
                                             false request-entity-too-large-413}
          is-options?                       {true options-201
                                             false accept-exists?}
          accept-exists?                    {true media-type-available?
                                             false accept-language-exists?}
          media-type-available?             {true accept-language-exists?
                                             false not-acceptable-406}
          accept-language-exists?           {true language-available?
                                             false accept-charset-exists?}
          language-available?               {true accept-charset-exists?
                                             false not-acceptable-406}
          accept-charset-exists?            {true charset-available?
                                             false accept-encoding-exists?}
          charset-available?                {true accept-encoding-exists?
                                             false not-acceptable-406}
          accept-encoding-exists?           {true encoding-available?
                                             false exists?}
          encoding-available?               {true exists?
                                             false not-acceptable-406}
          exists?                           {true if-match-exists?
                                             false if-match-star-exists-for-missing?}
          if-match-exists?                  {true if-match-star?
                                             false if-unmodified-since-exists?}
          if-match-star-exists-for-missing? {true precondition-failed-412
                                             false method-put?}
          if-match-star?                    {true if-unmodified-since-exists?
                                             false etag-matches-for-if-match?}
          if-unmodified-since-exists?       {true if-unmodified-since-valid-date?
                                             false if-none-match-exists?}
          method-put?                       {true put-to-different-url?
                                             false existed?}
          etag-matches-for-if-match?        {true if-unmodified-since-exists?
                                             false precondition-failed-412}
          if-unmodified-since-valid-date?   {true modified-since?
                                             false if-none-match-exists?}
          if-none-match-exists?             {true if-none-match-star?
                                             false if-modified-since-exists?}
          put-to-different-url?             {true moved-permanently-301
                                             false can-put-to-missing?}
          existed?                          {true moved-permanently?
                                             false post-to-missing?}
          post-to-missing?                  {true post-redirect?
                                             false not-found-404}
          modified-since?                   {true method-delete?
                                             false not-modified-304}
          if-none-match-star?               {true if-none-match?
                                             false etag-matches-for-if-none?}
          if-none-match?                    {true not-modified-304
                                             false precondition-failed-412}
          etag-matches-for-if-none?         {true if-none-match?
                                             false if-modified-since-exists?}
          if-modified-since-exists?         {true if-modified-since-valid-date?
                                             false method-delete?}
          can-put-to-missing?               {true conflict?
                                             false not-implemented-501}
          moved-permanently?                {true moved-permanently-301
                                             false moved-temporarily?}
          method-delete?                    {true delete!
                                             false post-to-existing?}
          moved-temporarily?                {true moved-temporarily-307
                                             false post-to-gone?}
          post-to-gone?                     {true can-post-to-gone?
                                             false gone-410}
          can-post-to-gone?                 {true post!
                                             false gone-410}
          if-modified-since-valid-date?     {true modified-since?
                                             false method-delete?}
          conflict?                         {true conflict-409
                                             false put!}
          delete!                           delete-enacted?
          put!                              new?
          post!                             post-redirect?
          post-to-existing?                 {true post!
                                             false put-to-existing?}
          put-to-existing?                  {true conflict?
                                             false multiple-representations?}
          delete-enacted?                   {true respond-with-entity?
                                             false accepted-202}
          new?                              {true created-201
                                             false respond-with-entity?}
          post-redirect?                    {true see-other-303
                                             false new?}
          multiple-representations?         {true multiple-representations-310
                                             false ok-200}
          respond-with-entity?              {true multiple-representations?
                                             false ok-200}
          ok-200                            :end
          options-201                       :end
          created-201                       :end
          accepted-202                      :end
          moved-permanently-301             :end
          see-other-303                     :end
          not-modified-304                  :end
          moved-temporarily-307             :end
          multiple-representations-310      :end
          malformed-400                     :end
          unauthorized-401                  :end
          forbidden-403                     :end
          not-found-404                     :end
          method-not-allowed-405            :end
          not-acceptable-406                :end
          conflict-409                      :end
          gone-410                          :end
          precondition-failed-412           :end
          request-entity-too-large-413      :end
          uri-too-long-414                  :end
          unsupported-media-type-415        :end
          not-implemented-501               :end
          unknown-method-501                :end
          service-not-available-503         :end
          } )

(defflow webmachine-flow webmachine)

(deftest webmachine-test
  (testing "running webmachine"
    (is (= (webmachine-flow {}) (see-other-303 {})))))
