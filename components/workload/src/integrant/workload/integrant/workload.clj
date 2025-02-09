(ns integrant.workload.integrant.workload
  (:require
    [integrant.core :as ig]
    [integrant.workload.protocol :as protocol]))

(defrecord WorkloadImp
  [print-base]
  protocol/Workload

  (write-text [_this text]
    (println print-base" -> " text)))

(defmethod ig/init-key :integrant.workload.integrant/workload
  [_ {:keys [print-base]}]
  (map->WorkloadImp {:print-base print-base}))

(defmethod ig/halt-key! :integrant.workload.integrant/workload
  [_ _]
  nil)