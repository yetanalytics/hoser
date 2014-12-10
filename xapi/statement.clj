(ns hoser.xapi.statement
  (:require [[cheshire.core :as c]])
  (:import java.net.URI :as URI))

(defn create-actor [^String account ^URI homepage]
  {:account {:name account
             :homepage homepage}})
