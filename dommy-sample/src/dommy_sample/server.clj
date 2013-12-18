(ns dommy-sample.server
  (:use compojure.core)
  (:require [compojure.handler :as handler]
            [compojure.route :as route]
            [net.cgrand.enlive-html :refer [deftemplate content
                                            set-attr html append]]
            [shoreleave.middleware.rpc :as rpc :refer (defremote)]
            [tentacles.search :as search]
            [tentacles.users :as users]))

;;;;;;;;;;;;;;;;
;; tempaltes
;;;;;;;;;;;;;;;
(deftemplate index-page "public/prototype/index.html"
  []
  [:#home-btn :a] (set-attr :href "/")
  [:#user-form] (set-attr :action "/search-page"))
  

(deftemplate search-page "public/prototype/search_page.html"
  [name]
  [:head]  (append (html [:script {:src "js/main.js"}]))
  [:#name] (content name)
  [:#home-btn :a] (set-attr :href "/")
  [:#results :.entry] nil)

;;;;;;;;;;;;;;;;;
;; remote rpc
;;;;;;;;;;;;;;;;;

(defremote search-users [search-term]
  (search/search-users search-term))

(defremote user-info [id]
  (users/user id))

;;;;;;;;;;;;;;;;;;;
;; routes
;;;;;;;;;;;;;;;;;;;

(defroutes app-routes
  (GET "/" [] (index-page))
  (GET "/search-page" [name] (search-page name))
  (route/resources "/")
  (route/not-found "Not Found"))

(def app
  (-> app-routes
      rpc/wrap-rpc
      handler/site))
