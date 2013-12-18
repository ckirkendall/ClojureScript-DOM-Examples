(ns dommy-sample.client
  (:require 
   [dommy.utils :as utils]
   [dommy.core :as dommy]
   [shoreleave.remotes.http-rpc :as rpc])
  (:use-macros
   [dommy.macros :only [node sel sel1 deftemplate]])
  (:require-macros
   [shoreleave.remotes.macros :as macros]))

(declare show-info)

;;;;;;;;;;;;;;;;;;;
;; helper functions
;;;;;;;;;;;;;;;;;;;

(defn gravatar-url [id]
  (str "http://www.gravatar.com/avatar/" id))

;;;;;;;;;;;;;;;
;; templates
;;;;;;;;;;;;;;;

(deftemplate base-entry [info]
  [:div {:class "entry"}
   [:table
    [:tr
     [:td
      [:img {:src (gravatar-url (:gravatar_id info))
             :align "left" :class "img-rounded"}]]
     [:td
      [:span {:class "user-info"}
       [:div {:class "logid"} (:username info)]
       [:button {:id (str (:username info) "_btn")
                 :class "more-info" } "More Info"]]]]]])

(deftemplate add-info [info]
  [:table
   [:tr
    [:td "Name:"]
    [:td
     [:span {:class "name"} (:name info)]]]
   [:tr
    [:td "#Repos:"]
    [:td
     [:span {:class "repo-count"} (:public_repos info)]]]
   [:tr
    [:td "Followers:"]
    [:td
     [:span {:class "followers"} (:followers info)]]]])


;;;;;;;;;;;;;;;;
;; Actions
;;;;;;;;;;;;;;;;
(defn display-results [entries]
  (.log js/console (pr-str entries))
  (doseq [entry entries]
    (dommy/append! (sel1 :#results) (base-entry entry))
    (dommy/listen! (sel1 (str "#" (:username entry) "_btn"))
                   :click #(show-info (:username entry)
                                      (.-currentTarget %)))))

(defn display-info [user btn]
  (dommy/replace! btn (add-info user)))

(defn search [term]
  (doseq [node (sel :.entry)]
    (dommy/remove! node))
  (rpc/remote-callback :search-users [term]  display-results))

(defn show-info [id btn]
  (rpc/remote-callback :user-info [id] #(display-info % btn)))

(defn init []
  (dommy/listen! (sel1 :#search-btn) :click
                 #(search (dommy/value (sel1 :#search-term)))))


(set! (.-onload js/window) init)




