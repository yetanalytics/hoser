(ns hoser.xapi.client
  (:require [cheshire.core :as c]
            [org.httpkit.client :as http]
            [hoser.util :refer [load-props]]))


(def lrs-props
  (load-props "hoser.properties"))

(def endpoint
  (str (:lrs-uri lrs-props) (:path lrs-props)))

(def options
  {:keepalive 30000
   :timeout 1000
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

(defn sync-send-stmt [stmt]
  (let [{:keys [status headers body error] :as resp} @(http/post endpoint (merge options {:body stmt}))]
    (if error
      (println "ERROR: " error)
      (println "POST: " status))))
