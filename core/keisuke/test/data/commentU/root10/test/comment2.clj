;;;; Clojure
; 行コメント

(xyz(comment 
(defun hoge [] (fuga))
) ; コメント化
abc def)
(xyz #_(defun hoge [] (fuga))　; コメント化
 abc def)

(re-seq (re-pattern "\\(list a b c\\)") "(list a b c)") ; => ("(list a b c)")
(re-seq #"\(list a b c\)" "(list a b c)") ; => ("(list a b c)")

(map #(+ % %2) '(1 2 3 4 5) '(6 7 8 9 10)) ; => (7 9 11 13 15)

