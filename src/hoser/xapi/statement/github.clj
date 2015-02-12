(ns hoser.xapi.statement.github
  (:require [cheshire.core :as c]))

;; translations
;; repo-name -> Use for activity type, add issues path
;; actor-login -> Agent/account, homepage is https://github.com
;; event -> verb
;; issue-url -> activity IRI
;; created-at -> timestamp
;; html-url -> moreinfo HR uri
;; title -> description of activity

;; "resources/github/issues/results-20150212-125746.csv"

(defn actor [actor-login]
  {:objectType "Agent"
   :account {:homePage "https://github.com"
             :name actor-login}})

(defn verb [event]
  {:id (str "https://xapinet.org/verbs/" event)
   :display {:en-US event}})

(defn activity-type [repo-name]
  (str "https://github.com/" repo-name "/issues"))

(defn activity [issue-url html-url title repo-name]
  {:id issue-url
   :objectType "Activity"
   :definition {:name {:en-US "issue"}
                :description {:en-US title}
                :type (activity-type repo-name)
                :moreInfo html-url}})

(defn parse-created-at [created-at-str]
  (let [[date time] (clojure.string/split created-at-str #"\s")]
    (str date \T time \Z)))

(defn issue->statement [[_ ;; type is not used
                         repo-name
                         actor-login
                         event
                         issue-url
                         html-url
                         title
                         created-at]]
  {:actor (actor actor-login)
   :verb (verb event)
   :object (activity issue-url html-url title repo-name)
   :timestamp (parse-created-at created-at)})

(defn issues->statements [s-data]
  (c/generate-string (into []
                           (for [s s-data]
                             (issue->statement s)))))
