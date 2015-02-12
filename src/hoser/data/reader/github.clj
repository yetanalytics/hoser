(ns hoser.data.reader.github
  (:require [clojure.data.csv :as csv]
            [clojure.java.io :as io]))

(defn get-issue-data [path]
  (with-open [in-file (io/reader path)]
    (doall
     (csv/read-csv in-file))))
