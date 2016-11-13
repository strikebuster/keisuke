-- Haskell
-- 行コメント
absolute :: Int -> Int
absolute x = if x < 0 then -x else x

-- ごあいさつ
greeting :: Bool -> Int -> String
greeting is_woman age | is_woman && age < 20  = "I have never seen such a beautiful girl like you!"
                      | is_woman              = "Nice to meet you."
                      | otherwise             = "Sorry, I am busy now." 
{- ブロックコメント
	{- ネストが可能 円柱の体積 -}
cylinder :: Double -> Double -> Double
cylinder r h = h * base
    where square x = x * x
          base = 3.14 * square r
-}
{- 円柱の体積 -}
cylinder :: Double -> Double -> Double
cylinder r h = let {square x = x * x
                    base = 3.14 * square r }
               in h * base

-- インデントでブロックを表す
main = do putStrLn "hello"
          putStrLn "world"
　　　　　　　　　　print (absolute (-5))
          print (greeting(True 20))
--          print (cylinder 2.0 5.0)

