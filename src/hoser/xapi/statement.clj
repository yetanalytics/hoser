(ns hoser.xapi.statement
  (:require [cheshire.core :as c]
            [clojure.string :as str]))

(defn tweet->stmt [tweet]
  (let [username (get-in tweet [:user :screen_name])
        id (:id_str tweet)
        twitter-url "https://twitter.com"]
    {:actor {:objectType "Agent"
             :account {:name username
                       :homepage twitter-url}}
     :verb {:id "http://xapidefs.yetanalytics.com/verbs/tweeted"
            :display {"en-US" "tweeted"}}
     :object {:objectType "Activity"
              :id (str/join "/" [twitter-url username "status" id])
              :display {"en-US" "tweet"}}
     }))
