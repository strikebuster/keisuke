;;;; Clojure
; 行コメント

(comment 
(defun hoge [] #_ (abc ( comment (fuga "2)"))))
) ; コメント化
#_(defun hoge [] #_(abc (comment(comment((fuga "2)")))))) ; コメント化

(re-seq (re-pattern "\\(list a b c\\)") "(list a b c)") ; => ("(list a b c)")
(re-seq #"\(list a b c\)" "(list a b c)") ; => ("(list a b c)")

(map #(+ % %2) '(1 2 3 4 5) '(6 7 8 9 10)) ; => (7 9 11 13 15)

