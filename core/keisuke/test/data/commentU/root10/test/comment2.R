# R
# コメント
cat('hello, world')
x <- 1 + 2  # x に 3 が代入される
 (1 + 2 - 3 * 4) / 5^6       # 足して引いて掛けて割って・・・

# 度数分布表を作成し，ヒストグラムを描く
dosuu.bunpu <- function(x,                           # データベクトル
                        w,                              # 階級幅
                        percent=FALSE)                  # TRUE にすると，縦軸が % 目盛りになる
{
        x <- x[!is.na(x)]                            # 欠損値を持つケースを除く
        y <- floor(x/w)                                      # 階級に分ける
        mn <- min(y)                                 # 最小の階級
        mx <- max(y)                                 # 最大の階級
        y <- y-mn+1                                  # 最小値が1になるように変換
        freq <- table(factor(y, levels=1:(mx-mn+1))) # 度数分布表（度数が 0 になる階級も確保）
        names(freq) <- mn:mx*w                               # 階級名
        pcnt <- freq/sum(freq)*100                   # パーセント
        cum.pcnt <- cumsum(pcnt)                     # 累積パーセント
        h <- if (percent) freq else pcnt             # 縦軸の選択
        barplot(h, axis.lty="solid", space=c(0, 0))     # ヒストグラムとして描く
        return(cbind(freq, pcnt, cum.pcnt))
}

