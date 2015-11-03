dir=../out/production/INF558_Project/

java -classpath $dir BWTransformer donner_unformated.txt > playground/BW
java -classpath $dir MTFCoder playground/BW > playground/MTF

compress -fc playground/MTF > playground/MTF.Z
gzip -fc playground/MTF > playground/MTF.gzip
bzip2 -fc playground/MTF > playground/MTF.bzip2


compress -fc donner_unformated.txt > playground/compressed_lz
gzip -fc donner_unformated.txt > playground/compressed_gzip
bzip2 -fc donner_unformated.txt > playground/compressed_bzip2
ls -l donner_unformated.txt
ls -l playground
