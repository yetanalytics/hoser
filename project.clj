(defproject hoser "0.1.0-SNAPSHOT"
  :description "Twitter Firehose -> xAPI"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.7.0-alpha4"]
                 [twitter-api "0.7.7"]
                 [cheshire "5.3.1"]
                 [http-kit "2.1.16"]
                 [clj-time "0.8.0"]
                 [org.apache.commons/commons-compress "1.9"]
                 [org.clojure/core.async "0.1.346.0-17112a-alpha"]])
