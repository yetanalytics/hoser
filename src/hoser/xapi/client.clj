(ns hoser.xapi.client
  (:require [cheshire.core :as c]
            [org.http-kit.client :as http]
            [hoser.util :refer [load-props]]))


(def lrs-props
  (load-props "hoser.properties"))

(def endpoint
  (str (:lrs-uri lrs-props) (:path lrs-props)))

(def options
  {:timeout 200
   :basic-auth [(:api-key lrs-props) (:api-secret lrs-props)]
   :user-agent "Hoser"
   :headers {"Accept" "application/json"
             "Content-Type" "application/json"
             "X-Experience-API-Version" "1.0.3"}})

(defn send-stmt [stmt]
  (http/post endpoint (merge options {:body stmt})
             (fn [{:keys [status headers body error]}]
               (if error
                 (println "Failed, exception is " error)
                 (println "Async HTTP POST: " status)))))
