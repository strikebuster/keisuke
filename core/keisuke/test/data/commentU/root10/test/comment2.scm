;;;; Scheme
; 行コメント
#| コメントのネスト
#| 文字列結合 |#
(import (rnrs base))
(string-append "foo" "bar") ; => "foobar"
|#

(import (rnrs))
(call-with-output-string
  (lambda (p)
    (do ((ss (reverse '("foo" "bar" "baz" "quux")) (cdr ss)))
        ((null? ss))
      (display (car ss) p)))) ; => "foobarbazquux"

;;; RnRS' map takes lists, each of which has the same length.
;;; But the version takes lists unequal in length is often useful.

  ;; similar to map, but may takes lists unequal in length.
  (define (map* f xs . xss)
    (if (null? xss)                     ; fast path
        (map proc xs)                   ; same as the RnRS' map
        (let loop ((xs xs)
                   (xss xss)
                   (rs '()))
          (if (or (null? xs) (exists null? xss))
              (reverse rs)
              (loop (cdr xs)
                    (map cdr xss)
                    (cons (apply proc (car xs) (map car xss)) rs)))))
  )

(let ((w #;(* *weight* 2) 3)) ; change weight temporarily
      (h (- hight 100)) 
)
#;(define (map* f xs . xss)
    (if (null? xss)                     ; fast path
        (map proc xs)                   ; same as the RnRS' map
        (let loop ((xs xs)
                   (xss xss)
                   (rs '()))
          (if (or (null? xs) (exists null? xss))
              (reverse rs)
              #; (loop (cdr xs)
                    (map cdr xss)
                    #;(cons (apply proc (car xs) #;(map car xss)) rs)))))
  )
