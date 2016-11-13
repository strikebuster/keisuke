;;;; Lisp
;;;; ファイル全体に対するコメント

;;; いくつかの関数グループに対するコメント

(defun foo (args)
  (let ((a 0) (b 1))
    ;; 関数内コードブロックに対するコメント
    (apply #'+ a b args)    ; 一つの式に対するコメント
  ))

(print "Hello world") ;=> "Hello world"
(concatenate 'string "aaa" "bbb") ;=> "aaabbb"

#|
(ql:quickload :cl-ppcre)
#| 文字列分割 |#
(ppcre:split "," "aaa,bbb,ccc")
;=> ("aaa" "bbb" "ccc")
|#

(labels ((foo (sep str acc)
           (let ((pos (position sep str)))
             (if (null pos)
                 (nreverse (cons str acc))
                 (foo sep
                      (subseq str (1+ pos))
                      (cons (subseq str 0 pos) acc))))))
  (foo #\, "aaa,bbb,ccc" () ))
;=> ("aaa" "bbb" "ccc")

