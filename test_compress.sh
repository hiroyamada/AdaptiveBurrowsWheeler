dir=../out/production/INF558_Project/
rm -rf playground
mkdir playground
java -classpath $dir bw.BWTransformer $1 > playground/BW
java -classpath $dir m2f.M2FCoder playground/BW > playground/MTF

compress -fc playground/MTF > playground/BW_M2F_compress
gzip -fc playground/MTF > playground/BW_M2F_gzip
bzip2 -fc playground/MTF > playground/BW_M2F_bzip2
java -classpath $dir adaptive_huffman.HuffmanCoder playground/MTF playground/BW_M2F_huffman

compress -fc donner_unformated.txt > playground/original_compress
gzip -fc donner_unformated.txt > playground/original_gzip
bzip2 -fc donner_unformated.txt > playground/original_bzip2
java -classpath $dir adaptive_huffman.HuffmanCoder donner_unformated.txt playground/original_huffman
ls -l $1
ls -l playground
