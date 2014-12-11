(ns hoser.xapi.statement
  (:require [cheshire.core :as c]
            [clojure.string :as str]
            [clj-time.coerce :as coerce]))

;; I don't like hard-coding all this stuff, but for
;; the current purposes, I don't see a better alternative.

(defn tweet->stmt*
  "Converts a tweet object (after JSON parsing) to an xAPI Statement"
  [tweet]
  (let [username (get-in tweet [:user :screen_name])
        id (:id_str tweet)
        twitter-url "https://twitter.com"
        timestamp (coerce/to-date
                   (coerce/from-long
                    (read-string
                     (:timestamp_ms tweet))))]
    {:actor {:objectType "Agent"
             :name username
             :account {:name username
                       :homePage twitter-url}}
     :verb {:id "http://xapidefs.yetanalytics.com/verbs/tweeted"
            :display {"en-US" "tweeted"}}
     :object {:objectType "Activity"
              :id (str/join "/" [twitter-url username "status" id])
              :definition {:name {"en-US" "tweet"}
                           :description {"en-US" "A user posted to Twitter"}}}
     :result {:response (:text tweet)}
     :timestamp timestamp
     }))

(defn tweet->stmt [tweet]
  (c/generate-string (tweet->stmt* tweet)))
