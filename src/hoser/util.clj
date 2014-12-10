(ns hoser.util
  (:require [clojure.java.io]))

(defn load-props
  "Load a Java properties file"
  [file-name]
  (with-open [^java.io.Reader reader (clojure.java.io/reader file-name)]
    (let [props (java.util.Properties.)]
      (.load props reader)
      (into {} (for [[k v] props] [(keyword k) (read-string v)])))))
