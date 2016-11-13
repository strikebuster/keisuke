"あるディレクトリ下のファイルでサイズが最も大きいものを拡張子別に集計してCSVとして書き出す"

FileStream newFileNamed: '../sizes.csv' do: 
    [:str | 
	((('.' asFileReference allChildren groupedBy: [:each | each extension]) 
	    collect: [:files | files detectMax: [:each | each size]]) 
	    values sort: [:a :b |a size >= b size]) do:
	    [:file |
            	str nextPutAll: (', ' join: {file size. file extension. file fullName}); lf.
    	    ]
    ]

