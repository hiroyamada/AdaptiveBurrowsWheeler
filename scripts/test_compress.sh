dir=../out/production/AdaptiveBurrowsWheeler/
rm -rf playground
mkdir playground

compress -fc $1 > playground/original_compress
gzip -fc $1 > playground/original_gzip
bzip2 -fc $1 > playground/original_bzip2
java -classpath $dir adaptive_bw.AdaptiveBurrowsWheeler $1 playground/original_adaptive_bw
java -classpath $dir adaptive_bw.AdaptiveBurrowsWheeler -r $1 playground/original_adaptive_bw_rle

ls -l $1
ls -l playground
