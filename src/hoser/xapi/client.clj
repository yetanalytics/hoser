(ns hoser.xapi.client
  (:require [cheshire.core :as c]
            [org.http-kit.client :as http]
            [hoser.util :refer [load-props]]))


(def lrs-props
  (load-props "hoser.properties"))
