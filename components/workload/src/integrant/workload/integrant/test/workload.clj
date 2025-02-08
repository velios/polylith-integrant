(ns integrant.workload.integrant.test.workload
  (:require
    [integrant.core :as ig]
    [integrant.workload.protocol :as protocol]))

(defrecord WorkloadImp []
  protocol/Workload

  (write-text [_this text]
    (println "test -> " text)))

(defmethod ig/init-key :integrant.workload.integrant.test/workload
  [_ _]
  (->WorkloadImp))

(defmethod ig/halt-key! :integrant.workload.integrant.test/workload
  [_ _]
  nil)
