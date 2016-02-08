# AdaptiveBurrowsWheeler
Java implementation of the Burrows-Wheeler algorithm fed into an adaptive Huffman tree.

Adaptive Burrows-Wheeler was the final research project for "INF558 Information Theory" taken at Ecole Polytechnique. It is a Java implementation of the Burrows-Wheeler compression algorithm fed into an adaptive Huffman tree. The combination of the two techniques is not seen in major compression utilities, and when run against natural language text files of 500kB~1MB along with other major compression utilities, the compression efficiencies were as follows:

![result 1]
(https://github.com/yak123soba/AdaptiveBurrowsWheeler/blob/master/results/image0.png)

As the diagram shows, the compression rate was higher than gzip but worse than bzip2 for most cases. Note that there are two bars for Adaptive Burrows-Wheeler, one with Run Length Encoding (right) and another without it (left).

You can learn more about this project here: https://www.dropbox.com/s/i3w668zsjkoucp6/final_presentation_inf_558.pptx?dl=0&preview=final_presentation_inf_558.pptx
