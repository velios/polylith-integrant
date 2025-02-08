(ns integrant.workload.integrant.workload
  (:require
    [integrant.core :as ig]
    [integrant.workload.protocol :as protocol]))

(defrecord WorkloadImp []
  protocol/Workload

  (write-text [_this text]
    (println "prod -> " text)))

(defmethod ig/init-key :integrant.workload.integrant/workload
  [_ _]
  (->WorkloadImp))

(defmethod ig/halt-key! :integrant.workload.integrant/workload
  [_ _]
  nil)