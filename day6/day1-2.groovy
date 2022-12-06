def input = new File('input.txt').text.split('').toList()
def bucket = new File('input.txt').text.split('').take(13).toList()
int marker = 13
while (marker < input.size()) {
	bucket << input[marker++]
	if (bucket.collect().unique().size() == 14) {
		println marker
		break
	}
	bucket.pop()
}