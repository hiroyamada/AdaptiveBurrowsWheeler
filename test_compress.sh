dir=../out/production/INF558_Project/
rm -rf playground
mkdir playground

compress -fc donner_unformated.txt > playground/original_compress
gzip -fc donner_unformated.txt > playground/original_gzip
bzip2 -fc donner_unformated.txt > playground/original_bzip2
./hiroZip donner_unformated.txt playground/original_hiroZip

ls -l $1
ls -l playground
